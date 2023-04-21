/*
 * Copyright 2009 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 01.10.2008 - [JR] - creation
 * 05.10.2008 - [JR] - setNewPassword implemented
 * 30.01.2009 - [JR] - check and use client compression support for results
 * 01.02.2009 - [JR] - compression property checked for stream compression
 * 03.02.2009 - [JR] - don't close streams: called flush or finish(-> zip)
 * 04.04.2009 - [JR] - process: used session.getProperties().getChanges(String.class) instead of session.getChangedProperties()
 * 13.05.2009 - [JR] - reflective ObjectProvider instantiation   
 * 25.05.2009 - [JR] - getProperty, setProperty, getCallBackResults, setAndCheckAlive now throws Throwable
 * 27.05.2009 - [JR] - implemented IDirectServer  
 * 04.10.2009 - [JR] - setNewPassword: old password as parameter      
 * 07.10.2009 - [JR] - IResponse.setProperty support  
 * 28.10.2009 - [JR] - getPropertyIntern: only String supported [BUGFIX] 
 * 14.11.2009 - [JR] - create monitoring object through the constructor, otherwise it doesn't receive events
 *                     from the SessionManager because it will be used too late! [BUGFIX]
 * 28.01.2010 - [JR] - removed serializer caching (serializers may not be stateless)
 * 23.02.2010 - [JR] - #18: 
 *                     * getPropertyIntern: use IConnectionConstants.PROPERTY_CLASSES for property transfer
 *                     * getPropertiesIntern: return only IConnectionConstants.PROPERTY_CLASSES properties
 * 02.03.2010 - [JR] - used List instead of ArrayUtil for property transfer 
 * 25.12.2010 - [JR] - #228: fixed objectprovider detection 
 * 29.12.2010 - [JR] - #231: sessionmanager creation with config property
 * 31.07.2011 - [JR] - #16: prepareException used     
 * 22.09.2011 - [JR] - #476: send changed request properties if no connection is available
 * 21.11.2012 - [JR] - #535: changed objectprovider and sessionmanager name to .../name because we'll use sub tags
 * 28.02.2013 - [JR] - #643: 
 *                     * validateCallBack implemented
 *                     * executeCallBack and executeActionCallBack returns Object (if callback is null)
 * 15.10.2013 - [JR] - DefaultSessionManager.setControllerInterval called   
 * 14.11.2014 - [JR] - #1177: IServerPlugin support       
 * 13.01.2015 - [JR] - #1224: initLogging introduced 
 * 26.01.2015 - [JR] - #1238: Exception recording       
 * 30.01.2015 - [JR] - read content from stream (empty stream) if possible    
 * 22.04.2015 - [JR] - better logging for "invalid communication state"  
 * 06.05.2015 - [JR] - #1378: initLogging in getInstance      
 * 28.08.2015 - [JR] - #1397: new methods of IDirectServer implemented (ICallHandler integration)   
 * 29.10.2015 - [JR] - #1503: init logfactory only if needed
 * 30.10.2015 - [JR] - #1505: handle serialization exception
 * 05.05.2017 - [JR] - #1788: don't remove default serializer
 * 21.11.2017 - [JR] - #1856: session lock mechanism used
 * 12.03.2019 - [JR] - #1998: stop now destroys session manager and sets instance to null
 */
package com.sibvisions.rad.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.naming.InitialContext;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SessionCancelException;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ISession;
import javax.rad.server.ResultObject;
import javax.rad.server.ServerContext;
import javax.rad.server.event.ISessionListener;
import javax.rad.server.push.IPushReceiver;
import javax.rad.server.push.PushMessage;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.rad.remote.mfa.MFAException;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.ServerZone;
import com.sibvisions.rad.server.plugin.IServerPlugin;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.io.MagicByteInputStream;
import com.sibvisions.util.io.NonClosingInputStream;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>Server</code> is the general remote server implementation.
 * It's independent of the communication protocol and handles client requests.
 *
 * The configuration of the server will be made in the <pre>server.xml</pre> file.
 * It contains the database connect information.
 * 
 * @author René Jahn
 */
public class Server implements IDirectServer
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** internal exception. */
    private static final Exception EXCEPTION_CONTENT;
    
    /** the magic byte sequence. */
    private static final byte[] MAGIC_BYTES = new byte[] {(byte)0x80, (byte)0x17, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
    
    /** the server sequence. */
    private static AtomicInteger aiSequence = new AtomicInteger(0);
    
    /** server logger. */
    private ILogger log;
    
    /** the singleton server instance. */
    private static Server instance = null; 
    
    /** the session manager. */
    private DefaultSessionManager sessman = null;
    
    /** the object provider. */
    private AbstractObjectProvider objectprov = null;
    
    /** the monitoring object. */
    private Monitoring monitoring = null;
    
    /** the cached response. */
    private WeakHashMap<ISession, CachedResponse> whmResponse = new WeakHashMap<ISession, Server.CachedResponse>();

    /** the serializer class cache. */
    private Map<Object, SerializerInfo> mapSerializer = Collections.synchronizedMap(new HashMap<Object, SerializerInfo>());
    
    /** the list of plugins. */
    private List<IServerPlugin> liInstalledPlugins = new ArrayUtil<IServerPlugin>();
    
    /** the list of allowed serializer class names. */
    private List<String> liSerializerAccess = new ArrayUtil<String>();
    
    /** the initial system identifier. */
    private String sInitialSystemIdentifier;
    
    /** the instance key, to identify the server. */
    private String sInstanceKey;
    
    /** the startup time. */
    private long lStartupTime = System.currentTimeMillis();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        EXCEPTION_CONTENT = new Exception("Response content is not available!");
        EXCEPTION_CONTENT.setStackTrace(new StackTraceElement[0]);
    }
    
    /**
     * Creates a new instance of <code>Server</code>.
     */
    public Server()
    {
    	this(true);
    }
    
    /**
     * Creates a new instance of <code>Server</code>.
     * 
     * @param pStartPlugins <code>true</code> to start configured server plugins, <code>false</code> otherwise
     */
    public Server(boolean pStartPlugins)
    {
        Thread thShutdown = new Thread()
        {
            public void run()
            {
                //try to uninstall the plugins
                uninstallPlugins();
            }
        };
        
        Runtime.getRuntime().addShutdownHook(thShutdown);
        
        //UniversalSerializer is always available
        liSerializerAccess.add(UniversalSerializer.class.getName());
        
        sInstanceKey = System.getProperty(IPackageSetup.SERVER_INSTANCEKEY);
        
        if (StringUtil.isEmpty(sInstanceKey))
        {
            sInstanceKey = Long.toString(lStartupTime, Character.MAX_RADIX) + "#" + aiSequence.incrementAndGet();
        }
        
        ServerZone zone = Configuration.getServerZone();
        
        try
        {
        	List<String> li = zone.getProperties("/server/serializer/allow");
        	
        	if (li != null)
        	{
        		for (String ser : li)
        		{
        			liSerializerAccess.add(ser);
        		}
        	}
        	
        	li = zone.getProperties("/server/serializer/deny");
        	
        	if (li != null)
        	{
        		for (String ser : li)
        		{
        			liSerializerAccess.remove(ser);
        		}
        	}
        }
        catch (Throwable th)
        {
        	log.debug("Custom serializer allow/deny rules failed", th);
        }
        
        log = LoggerFactory.getInstance(Server.class);
        
        try
        {
            String sSessMan = zone.getProperty("/server/sessionmanager/class");
            
            sessman = (DefaultSessionManager)Reflective.construct(sSessMan, this);
            
            log.debug("Use ", sSessMan, " as SessionManager");
        }
        catch (Throwable th)
        {
            log.debug("NO PROBLEM! Use default SessionManager", th);
            
            sessman = new DefaultSessionManager(this);
        }
        
        mapSerializer.put(null, new SerializerInfo(UniversalSerializer.class));
        
        sessman.addSessionListener(new SessionListener(this));
        
        try
        {
            String sInterval = zone.getProperty("/server/sessionmanager/controllerInterval");
            
            if (!StringUtil.isEmpty(sInterval))
            {
                DefaultSessionManager.setControllerInterval(Long.parseLong(sInterval));
            }
        }
        catch (Throwable th)
        {
            log.debug(th);
        }
        
        try
        {
            String sObjProvider = zone.getProperty("/server/objectprovider/class");
        
            objectprov = (AbstractObjectProvider)Reflective.construct(sObjProvider, this);
            
            log.debug("Use ", sObjProvider, " as ObjectProvider");
        }
        catch (Throwable th)
        {
            log.debug("NO PROBLEM! Use default ObjectProvider", th);
            
            objectprov = new DefaultObjectProvider(this); 
        }
        
        monitoring = new Monitoring(this);

        if (pStartPlugins)
        {
	        HashMap<String, Boolean> configuredPlugins = new HashMap<String, Boolean>();
	        
	        try
	        {
	            //Plugin initialization
	            List<XmlNode> liPlugins = zone.getNodes("/server/plugin");
	            
	            if (liPlugins != null)
	            {
	                for (XmlNode node : liPlugins)
	                {
	                    String sClass = node.getNodeValue("/class");
	                    
	                    String sEnabled = node.getNodeValue("enabled");
	                    
	                	boolean bEnabled = sEnabled == null || Boolean.parseBoolean(sEnabled);
	                	
	                	configuredPlugins.put(sClass, Boolean.valueOf(bEnabled));
	
	                	if (bEnabled)
	                    {
	                        try
	                        {
	                            IServerPlugin plugin = (IServerPlugin)Reflective.construct(sClass);
	                            plugin.install(this);
	                            
	                            liInstalledPlugins.add(plugin);
	                            
	                            log.info("Plugin '", plugin.getClass(), "' installation successful (Configuration)!");
	                        }
	                        catch (Throwable th)
	                        {
	                            log.error("Can't install plugin '", sClass, "'", th);
	                        }
	                    }
	                }
	            }
	        }
	        catch (Exception e)
	        {
	            log.debug("Can't access plugin configuration!", e);
	        }
	        
	        try
	        {
	        	//Plugin initialization
	    		Iterator<IServerPlugin> iter = ServiceLoader.load(IServerPlugin.class, getClass().getClassLoader()).iterator();
	
	    		while (iter.hasNext()) 
	    		{
	            	IServerPlugin plugin = iter.next();
	
	            	// if true, it is already installed, if false, it should not be installed, if null, it has to be installed.
	            	if (!configuredPlugins.containsKey(plugin.getClass().getName()))
	            	{
	                    try
	                    {
	                		plugin.install(this);
	                		
	                		liInstalledPlugins.add(plugin);
	                		
	                        log.info("Plugin '", plugin.getClass(), "' installation successful (ServiceLoader)!");
	                    }
	                    catch (Throwable th)
	                    {
	                        log.error("Can't install plugin '", plugin.getClass(), "'", th);
	                    }
	            	}
	    		}
	        }
	        catch (Throwable th)
	        {
	            log.debug("Loading plugin with ServiceLoader failed!", th);
	        }
        }
    }
    
    /**
     * Gets the current server instance as singleton.
     * 
     * @return the singleton server instance
     */
    public static synchronized Server getInstance()
    {
        if (instance == null)
        {
            try
            {
                InitialContext ctxt = new InitialContext();
    
                try
                {
                    instance = (Server)ctxt.lookup("java:/comp/env/jvx/server");
                }
                finally
                {
                    ctxt.close();
                }
            }
            catch (Exception ex)
            {
                LoggerFactory.getInstance(Server.class).debug("Server is not configured as JNDI resource", ex);
            }

            //no JNDI server available
            if (instance == null)
            {
                instance = new Server();
            }
        }
        
        return instance;
    }

    /**
     * Stops the server.
     */
    public synchronized void stop()
    {
    	sessman.destroy();

    	try
        {
            uninstallPlugins();
        }
        finally
        {
            //stop all known threads
            ThreadHandler.stop();
        }
    	
    	instance = null;
    }    

    /**
     * Uninstalls all installed plugins.
     */
    private synchronized void uninstallPlugins()
    {
        if (liInstalledPlugins != null)
        {
            try
            {
                for (IServerPlugin plugin : liInstalledPlugins)
                {
                    try
                    {
                        plugin.uninstall(this);
                    }
                    catch (Throwable th)
                    {
                        LoggerFactory.getInstance(Server.class).error("Can't unistall plugin '", plugin.getClass().getName(), "'", th);                    
                    }
                }
            }
            finally
            {
                liInstalledPlugins.clear();
            }
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // IDirectServer

    /**
     * {@inheritDoc}
     */
    public final DefaultSessionManager getSessionManager()
    {
        return sessman;
    }
    
    /**
     * {@inheritDoc}
     */
    public final AbstractObjectProvider getObjectProvider()
    {
        return objectprov;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object createSession(ChangedHashtable<String, Object> pProperties) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();

        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_CREATE_SESSION);

            try
            {
                Throwable thReturn = null;
                Object oReturn = null;
        
                long lStart = System.currentTimeMillis();
                
                
                try
                {
                    oReturn = createSessionIntern(null, null, pProperties);
        
                    return oReturn;
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(null,
                            "direct",
                            IConnection.OBJ_SESSION, 
                            IConnection.MET_SESSION_CREATE, 
                            new Object[] {pProperties}, 
                            null, 
                            oReturn,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object createSubSession(Object pSessionId, 
                                   ChangedHashtable<String, Object> pProperties) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_CREATE_SUBSESSION);

            try
            {
                Throwable thReturn = null;
                Object oReturn = null;
                
                long lStart = System.currentTimeMillis();
                
                
                try
                {
                    oReturn = createSubSessionIntern(null, sessman.get(pSessionId), pProperties);
                    
                    return oReturn;
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            IConnection.OBJ_SESSION, 
                            IConnection.MET_SESSION_SUBSESSION_CREATE, 
                            new Object[] {pProperties}, 
                            null, 
                            oReturn, 
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }

    /**
     * {@inheritDoc}
     */
    public void destroySession(Object pSessionId)
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_DESTROY_SESSION);

            try
            {
                long lStart = System.currentTimeMillis();
                
                try
                {
                    destroySessionIntern(pSessionId);
                }
                catch (RuntimeException re)
                {
                    if (record != null)
                    {
                        record.setException(re);
                    }
                    
                    throw re;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            IConnection.OBJ_SESSION, 
                            IConnection.MET_SESSION_DESTROY, 
                            null, 
                            null, 
                            null,
                            null,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object execute(Object pSessionId, String pObjectName, String pMethod, Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXECUTE, pObjectName, pMethod, pParams);

            try
            {
                Throwable thReturn = null;
                
                Object oReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                try
                {
                    AbstractSession session = sessman.get(pSessionId);

                    configureContext(session);
                    
                    oReturn = executeIntern(session, new Call(null, pObjectName, pMethod, pParams));
                    
                    return oReturn;
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId,
                            "direct",
                            pObjectName, 
                            pMethod, 
                            pParams, 
                            null, 
                            oReturn,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void executeCallBack(Object pSessionId, 
                                Object pCallBackId, 
                                String pObjectName, 
                                String pMethod, 
                                Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXEC_CALLBACK, pObjectName, pMethod, pParams);

            try
            {
                Throwable thReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    validateCallBack(session, pObjectName);
                    
                    Call call = new Call(pCallBackId, pObjectName, pMethod, pParams);
                    call.setForceCallBack(true);
                    
                    executeIntern(session, call);
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            pObjectName, 
                            pMethod, 
                            pParams, 
                            pCallBackId, 
                            null,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void executeCallBack(Object pSessionId, 
                                ICallBackListener pCallBackListener, 
                                String pObjectName, 
                                String pMethod, 
                                Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXEC_CALLBACK, pObjectName, pMethod, pParams);

            try
            {
                Throwable thReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    validateCallBack(session, pObjectName);
                    
                    Call call = new Call(pCallBackListener, pObjectName, pMethod, pParams);
                    call.setForceCallBack(true);
                    
                    executeIntern(session, call);
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            pObjectName, 
                            pMethod, 
                            pParams, 
                            pCallBackListener, 
                            null,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object executeAction(Object pSessionId, String pAction, Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXEC_ACTION, pAction, pParams);

            try
            {
                Throwable thReturn = null;
                Object oReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    oReturn = executeIntern(session, new Call(null, null, pAction, pParams));
                    
                    return oReturn;
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            null, 
                            pAction,
                            pParams, 
                            null, 
                            oReturn,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeActionCallBack(Object pSessionId, Object pCallBackId, String pAction, Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXEC_ACTIONCALLBACK, pAction, pParams);

            try
            {
                Throwable thReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    validateCallBack(session, null);
                    
                    Call call = new Call(pCallBackId, null, pAction, pParams);
                    call.setForceCallBack(true);
                    
                    executeIntern(session, call);
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            null, 
                            pAction, 
                            pParams, 
                            pCallBackId, 
                            null,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void executeActionCallBack(Object pSessionId, ICallBackListener pCallBackListener, String pAction, Object... pParams) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_EXEC_ACTIONCALLBACK, pAction, pParams);
            
            try
            {
                Throwable thReturn = null;
                
                long lStart = System.currentTimeMillis();
        
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    validateCallBack(session, null);
                    
                    Call call = new Call(pCallBackListener, null, pAction, pParams);
                    call.setForceCallBack(true);
                    
                    executeIntern(session, call);
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId,
                            "direct",
                            null, 
                            pAction, 
                            pParams, 
                            pCallBackListener, 
                            null,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setProperty(Object pSessionId, String pName, Object pValue) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Throwable thReturn = null;
    
            long lStart = System.currentTimeMillis();
            
            try
            {
                AbstractSession session = sessman.get(pSessionId);
                
                configureContext(session);
                
                setPropertyIntern(session, pName, pValue);
            }
            catch (Throwable th)
            {
                thReturn = AbstractSecurityManager.prepareException(th);
                
                throw th;
            }
            finally
            {
                if (log.isEnabled(LogLevel.DEBUG))
                {
                    log(pSessionId, 
                        "direct",
                        IConnection.OBJ_SESSION, 
                        IConnection.MET_SESSION_SET_PROPERTY, 
                        new Object[] {pName, pValue}, 
                        null, 
                        null,
                        thReturn,
                        System.currentTimeMillis() - lStart);
                }
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getProperty(Object pSessionId, String pName) throws Throwable
    {
        Throwable thReturn = null;
        
        Object oResult = null;
        
        long lStart = System.currentTimeMillis();
        
        try
        {
            oResult = getPropertyIntern(sessman.get(pSessionId), pName);
        }
        catch (Throwable th)
        {
            thReturn = AbstractSecurityManager.prepareException(th);
            
            throw th;
        }
        finally
        {
            if (log.isEnabled(LogLevel.DEBUG))
            {
                log(pSessionId, 
                    "direct",
                    IConnection.OBJ_SESSION, 
                    IConnection.MET_SESSION_GET_PROPERTY, 
                    null, 
                    null, 
                    oResult,
                    thReturn,
                    System.currentTimeMillis() - lStart);
            }
        }
        
        return oResult;
    }
    
    /**
     * {@inheritDoc}
     */
    public ChangedHashtable<String, Object> getProperties(Object pSessionId) throws Throwable
    {
        Throwable thReturn = null;
        
        ChangedHashtable<String, Object> htResult = null;
        
        long lStart = System.currentTimeMillis();
        
        try
        {
            htResult = getPropertiesIntern(sessman.get(pSessionId));
        }
        catch (Throwable th)
        {
            thReturn = AbstractSecurityManager.prepareException(th);
            
            throw th;
        }
        finally
        {
            if (log.isEnabled(LogLevel.DEBUG))
            {
                log(pSessionId, 
                    "direct",
                    IConnection.OBJ_SESSION, 
                    IConnection.MET_SESSION_GET_PROPERTIES, 
                    null, 
                    null, 
                    htResult,
                    thReturn,
                    System.currentTimeMillis() - lStart);
            }
        }
        
        return htResult;
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultObject> getCallBackResults(Object pSessionId) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Throwable thReturn = null;
    
            ArrayUtil<ResultObject> auResult = null;
    
            long lStart = System.currentTimeMillis();
            
            
            try
            {
                AbstractSession session = sessman.get(pSessionId);
                
                configureContext(session);
                
                auResult = getCallBackResultsIntern(session);
            }
            catch (Throwable th)
            {
                thReturn = AbstractSecurityManager.prepareException(th);
    
                throw th;
            }
            finally
            {
                if (log.isEnabled(LogLevel.DEBUG))
                {
                    log(pSessionId, 
                        "direct",
                        IConnection.OBJ_SESSION, 
                        "getCallBackResults", 
                        new Object[] {pSessionId}, 
                        null, 
                        auResult,
                        thReturn,
                        System.currentTimeMillis() - lStart);
                }
            }
        
            return auResult;
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object[] setAndCheckAlive(Object pSessionId, Object... pSubSessionId) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Throwable thReturn = null;
            
            Object[] oResult = null;
            
            long lStart = System.currentTimeMillis();
            
            try
            {
                AbstractSession session = sessman.get(pSessionId);
                
                configureContext(session);
                
                oResult = setAndCheckAliveIntern(session, pSubSessionId);
            }
            catch (Throwable th)
            {
                thReturn = AbstractSecurityManager.prepareException(th);
                
                throw th;
            }
            finally
            {
                if (log.isEnabled(LogLevel.DEBUG))
                {
                    log(pSessionId, 
                        "direct",
                        IConnection.OBJ_SESSION, 
                        IConnection.MET_SESSION_SETCHECKALIVE, 
                        pSubSessionId, 
                        null, 
                        oResult,
                        thReturn,
                        System.currentTimeMillis() - lStart);
                }
            }
            
            return oResult;
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setNewPassword(Object pSessionId, String pOldPassword, String pNewPassword) throws Throwable
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_SET_NEWPASSWORD);

            try
            {
                Throwable thReturn = null;
                
                long lStart = System.currentTimeMillis();
                
                try
                {
                    AbstractSession session = sessman.get(pSessionId);
                    
                    configureContext(session);
                    
                    setNewPasswordInternal(session, pOldPassword, pNewPassword);
                }
                catch (Throwable th)
                {
                    if (record != null)
                    {
                        record.setException(th);
                    }
                    
                    thReturn = AbstractSecurityManager.prepareException(th);
                    
                    throw th;
                }
                finally
                {
                    if (log.isEnabled(LogLevel.DEBUG))
                    {
                        log(pSessionId, 
                            "direct",
                            IConnection.OBJ_SESSION, 
                            IConnection.MET_SESSION_SET_NEW_PASSWORD, 
                            new Object[] {"****"}, 
                            null, 
                            null,
                            thReturn,
                            System.currentTimeMillis() - lStart);
                    }
                }
            }
            finally
            {
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void beforeFirstCall(Object pSessionId)
    {
        if (pSessionId != null)
        {
            AbstractSession session;
            
            if (pSessionId instanceof AbstractSession)
            {
                session = (AbstractSession)pSessionId;
            }
            else
            {
                session = sessman.get(pSessionId);
            }

            session.getCallHandler().fireBeforeFirstCall();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void afterLastCall(Object pSessionId, boolean pCallError)
    {
        if (pSessionId != null)
        {
            AbstractSession session;
            
            if (pSessionId instanceof AbstractSession)
            {
                session = (AbstractSession)pSessionId;
            }
            else
            {
                session = sessman.get(pSessionId);
            }

            session.getCallHandler().fireAfterLastCall(pCallError);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void registerPushReceiver(Object pSessionId, IPushReceiver pReceiver)
    {
        // Forwarding
        sessman.registerPushReceiver(pSessionId, pReceiver);
    }
    
    /**
     * {@inheritDoc}
     */
    public void unregisterPushReceiver(Object pSessionId)
    {
        //Forwarding
        sessman.unregisterPushReceiver(pSessionId);
    }    
    
    /**
     * {@inheritDoc}
     */
    public void push(PushMessage pMessage)
    {
        sessman.push(pMessage);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** 
     * Gets the instance key of this server instance. The key is unique in the same VM but it's
     * not an UUID. It contains the current time in millis and an instance number.
     * 
     * @return the instance key
     */
    public String getInstanceKey()
    {
        return sInstanceKey;
    }
    
    /**
     * Gets the server startup time. This time is the time of instance creation.
     * 
     * @return the startup time
     */
    public long getStartupTime()
    {
        return lStartupTime;
    }
    
    /**
     * Processes client requests which uses the communication protocol.
     * 
     * @param pRequest the request
     * @param pResponse the response
     * @return the accessed session or <code>null</code> if the session is not available
     * @throws Exception if a problem occurs while accessing the in- or output stream
     */
    @SuppressWarnings("resource")
	public ISession process(IRequest pRequest, IResponse pResponse) throws Exception
    {
        ServerContext ctxt = createServerContextIntern();
        
        try
        {
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_PROCESS);

            AbstractSession session = null;

            try
            {
                InputStream in;
                
                ByteArrayOutputStream baosResponse;
                
                ISerializer serializer = null;
                
                ArrayUtil<ResultObject> auResult = null;
                
                Long lCommunicationId = null;
                
                int iCallCount;
                
                ChangedHashtable<String, Object> chtProperties = null;
        
                CachedResponse crPending = null;
                
                MagicByteInputStream mbisRequest = null;
    
                Object oSessionId = null;
                
                try
                {
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    // REQUEST
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
                    in = pRequest.getInputStream();
        
                    if (!(in instanceof BufferedInputStream))
                    {
                        in = new BufferedInputStream(in);
                    }
                    
                    //Request-Header (same info as in AbstractSerializedConnection.java)
                    //
                    //<STREAM-IDENTIFIER>       1Byte  (A = Acknowledge; E = Established) 
                    //<OPTION-FLAG-1>           1Byte  (0x01 = UNCOMPRESSED; 0x02 = COMPRESSED>
                    // - <SERIALIZER-CLASSNAME> xBytes (only with A, via DataOutputStream)
                    // - <SESSION-ID>           xBytes (via DataOutputStream)
                    // - <COMMUNICATION-ID>     xBytes (via ISerializer (optional))
                    // - <CALL-COUNT>           xBytes (via ISerializer)
                    // - <CALL-PARAMETER>       xBytes (via ISerializer)
                    //    - [Objectname, Method, Parameter, CallBack-ID]
                    
                    int iRead;
                    
                    try
                    {
                        iRead = in.read();
                    }
                    catch (SocketException se)
                    {
                        //can't handle request if stream is closed
                        pResponse.close();
                        
                        throw se;
                    }
    
                    //EOF?
                    if (iRead < 0)
                    {
                        pResponse.close();
    
                        //Invalid stream identifier exception will follow
                        throw new SocketException("Client stream is closed!");
                    }
                    
                    char chStreamID = (char)iRead;
    
                    if (chStreamID != IConnection.FLAG_ACKNOWLEDGE && chStreamID != IConnection.FLAG_ESTABLISHED)
                    {                    
                        throw new IOException("Invalid stream identifier '" + chStreamID + "'");
                    }
        
                    int iMode = in.read();
                    
                    InputStream isContent;
                    
                    //Check compression option
                    if (iMode == IConnection.MODE_COMPRESSED)
                    {
                        mbisRequest = new MagicByteInputStream(new NonClosingInputStream(in), MAGIC_BYTES);
       
                        isContent = new GZIPInputStream(mbisRequest);
                    }
                    else
                    {
                        isContent = in;
                    }
                    
                    DataInputStream disContent = new DataInputStream(isContent);
        
                    String sSerializer = null;
        
                    //The serializer classname is only available when the connection will be opened!
                    if (chStreamID == IConnection.FLAG_ACKNOWLEDGE)
                    {
                        sSerializer = disContent.readUTF();
                        
                        checkSerializerAccess(sSerializer);
                        
                        serializer = (ISerializer)Class.forName(sSerializer).newInstance();
                    }
        
                    //Read session id
                    oSessionId = disContent.readUTF();
                            
                    //null session id will be transmitted as empty string
                    if (((String)oSessionId).trim().length() == 0)
                    {
                        oSessionId = null;
                    }
        
                    if (oSessionId != null)
                    {
                        try
                        {
                            session = sessman.get(oSessionId);
                        }
                        catch (SessionExpiredException se)
                        {
                            clearBuffer(disContent, oSessionId);
                            
                            throw createSessionCancelException(se);
                        }

                        session.lock();
                        
                        configureContext(session);
                        
                        serializer = session.getSerializer();
                    }
                    
                    //Now a serializer must be available. Either through a new connection command (acknowledge) or
                    //from an already created session
                    if (serializer == null)
                    {
                        clearBuffer(disContent, oSessionId);
                        
                        throw new SecurityException("Invalid serializer '" + sSerializer + "'");
                    }
    
                    //support for communication based on "communication id"
                    Object oRead = serializer.read(disContent);
                    
                    if (oRead instanceof String)
                    {
                        lCommunicationId = Long.valueOf((String)oRead);
                        
                        if (record != null)
                        {
                            record.setParameter(oSessionId, lCommunicationId);
                        }
                        
                        if (session != null)
                        {
                            ISession sessMaster = sessman.getMasterSession(session);
                            
                            crPending = whmResponse.get(sessMaster);
            
                            if (crPending != null)
                            {
                                if (crPending.id.equals(lCommunicationId))
                                {
                                    if (crPending.content == null)
                                    {
                                        synchronized(crPending)
                                        {
                                            crPending.wait(10000L);
                                        }
                                    }
                                    
                                    if (crPending.content != null)
                                    {
                                        sendContent(pResponse, crPending.content);
                                        
                                        return session;
                                    }
                                    else
                                    {
                                        pResponse.close();
            
                                        throw EXCEPTION_CONTENT;
                                    }
                                }
                                else if (crPending.id.longValue() + 1 != lCommunicationId.longValue())
                                {
                                    //read whole stream
                                    iCallCount = ((Integer)serializer.read(disContent)).intValue();
    
                                    Object[] oCommand;
                                    
                                    ArrayUtil<Object[]> liObjects = null;
                                    
                                    for (int i = 0; i < iCallCount; i++)
                                    {
                                        oCommand = (Object[])serializer.read(disContent);
                                        
                                        if (record != null)
                                        {
                                            if (liObjects == null)
                                            {
                                                //we need the current record parameters as well
                                                liObjects = new ArrayUtil<Object[]>(record.getParameter());
                                            }
                                            
                                            liObjects.add(oCommand);
                                        }
                                        
                                        error(session != null ? session.getId() : null,
                                              lCommunicationId,
                                              (String)oCommand[0], 
                                              (String)oCommand[1], 
                                              (Object[])oCommand[2], 
                                              (Object)oCommand[3], 
                                              null,
                                              null,
                                              -1);
                                    }
                                    
                                    if (record != null)
                                    {
                                        if (liObjects != null)
                                        {
                                            //change parameters, if stream contained additional commands
                                            record.setParameter(liObjects.toArray(new Object[liObjects.size()]));
                                        }
                                    }
                                    
                                    throw new IOException("Invalid communication state! " + (crPending.id.longValue() + 1) + " <> " + lCommunicationId);
                                }
                            }      
                            
                            crPending = new CachedResponse(lCommunicationId);
                            
                            whmResponse.put(sessMaster, crPending);
                        }
                        
                        oRead = serializer.read(disContent);
                    }
                    else if (oRead == null)
                    {
                        //null communication id is allowed -> no retry support
                        oRead = serializer.read(disContent);
                    }
                    
                    //Handle calls
                    iCallCount = ((Integer)oRead).intValue();
                    
                    auResult = new ArrayUtil<ResultObject>(iCallCount);
                    
                    Throwable thReturn;
                    
                    Object oReturn;
                    Object[] oCommand;
                    
                    long lStart;
                    
                    boolean bFirstCall = false;
                    boolean bCallError = false;
    
                    try
                    {
                        for (int i = 0; i < iCallCount; i++)
                        {
                            lStart = System.currentTimeMillis();
                            
                            oCommand = (Object[])serializer.read(disContent);
                            
                            if (!bFirstCall && session != null)
                            {
                                try
                                {
                                    beforeFirstCall(session);
                                }
                                finally
                                {
                                    bFirstCall = true;
                                }
                            }

                            try
                            {
                                //Request parameter validation
                                if (oCommand.length == 4
                                    && (oCommand[0] == null || oCommand[0].getClass() == String.class)
                                    && oCommand[1].getClass() == String.class
                                    && (oCommand[2] == null || oCommand[2] instanceof Object[]))
                                {
                                    thReturn = null;
                                    oReturn = null;
                                    
                                    try
                                    {
                                        oReturn = process(pRequest,
                                                          serializer,
                                                          session, 
                                                          (String)oCommand[0], 
                                                          (String)oCommand[1], 
                                                          (Object[])oCommand[2], 
                                                          (Object)oCommand[3],
                                                          chtProperties);
                                    }
                                    catch (Throwable th)
                                    {
                                        thReturn = th;

                                        throw th;
                                    }
                                    finally
                                    {
                                        if (log.isEnabled(LogLevel.DEBUG))
                                        {
                                            log(session != null ? session.getId() : null,
                                                lCommunicationId,
                                                (String)oCommand[0], 
                                                (String)oCommand[1], 
                                                (Object[])oCommand[2], 
                                                (Object)oCommand[3], 
                                                oReturn,
                                                thReturn,
                                                System.currentTimeMillis() - lStart);
                                        }
                                    }
                
                                    //Special handling of properties
                                    if (IConnection.OBJ_SESSION.equals((String)oCommand[0]) 
                                        && IConnection.MET_SESSION_SET_PROPERTY.equals((String)oCommand[1]))
                                    {
                                        List<Object[]> auProperties = (List<Object[]>)oReturn;
                
                                        if (auProperties != null)
                                        {
                                            //Init properties for later use
                                            chtProperties = new ChangedHashtable<String, Object>();
                                            
                                            Object[] oProp;
                                            
                                            for (int j = 0, anzj = auProperties.size(); j < anzj; j++)
                                            {
                                                oProp = auProperties.get(j);
                                                
                                                chtProperties.put((String)oProp[0], oProp[1], false);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        //No properties -> access the session
                                        if (session == null) 
                                        {
                                            //save the session instance for the case that more than one call was submitted 
                                            //within a single request
                                            session = sessman.get(oReturn);
                                            session.lock();
                                            
                                            ISession sessMaster = sessman.getMasterSession(session); 
                                            
                                            CachedResponse crOld = whmResponse.get(sessMaster);
                                            
                                            if (crOld != null && crOld.id.longValue() + 1 != lCommunicationId.longValue())
                                            {
                                                throw new IOException("Invalid communication state! " + (crOld.id.longValue() + 1) + " <> " + lCommunicationId);
                                            }
                                        }
                                    
                                        //Properties will be handled seperately, so don't add to the result list!
                                        auResult.add(new ResultObject(IConnection.TYPE_CALL_RESULT, oReturn));
                                    }
                                }
                                else
                                {
                                    log.debug("Invalid request parameters!", oCommand);
                                    
                                    throw new SecurityException("Invalid request parameters!");
                                }
                            }
                            catch (Throwable thro)
                            {
                                //empty the stream
                                try
                                {
                                    for (int j = i + 1; j < iCallCount; j++)
                                    {
                                        serializer.read(disContent);
                                    }
                                }
                                catch (Throwable throwa)
                                {
                                    //can't handle this case
                                }
                                
                                //a problem occured
                                bCallError = true;

                                throw thro;
                            }
                        }
                    }
                    finally
                    {
                        if (bFirstCall)
                        {
                            afterLastCall(session, bCallError);
                        }
                    }
                }
                catch (Throwable th)
                {                
                    if (th == EXCEPTION_CONTENT)
                    {
                        throw EXCEPTION_CONTENT;
                    }
                    else
                    {
                        if (!pResponse.isClosed())
                        {
                            if (record != null)
                            {
                                record.setException(th);
                            }
                            
                            if (!(th instanceof MFAException))
                            {
                            	log.error(th);
                            }
                            else
                            {
                            	log.debug(th);
                            }
                            
                            if (auResult == null)
                            {
                                auResult = new ArrayUtil<ResultObject>(1);              
                            }
                            
                            auResult.add(new ResultObject(IConnection.TYPE_CALL_ERROR, AbstractSecurityManager.prepareException(th)));
                        }
                        else
                        {
                            if (th instanceof Exception)
                            {
                                throw (Exception)th;
                            }
                            else
                            {
                                throw new IOException("Unexpected server exception!", th);
                            }
                        }
                    }
                }
                finally
                {
                    if (mbisRequest != null)
                    {
                        mbisRequest.close();
                    }
                    
                    try
                    {
                        pRequest.close();
                    }
                    catch (Throwable th)
                    {
                        log.error(th);
                    }
        
                    if (!pResponse.isClosed())
                    {
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        // RESPONSE
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
                        baosResponse = new ByteArrayOutputStream();
                        
                        //Response-Header (same as in AbstractSerializedConnection.java)
                        //
                        //<STREAM-IDENTIFIER>  1Byte  (E = Established; B = Broken) 
                        //<OPTION-FLAG-1>      1Byte  (0x01 = UNCOMPRESSED; 0x02 = COMPRESSED>
                        // - <RESULT-COUNT>    xBytes (only with E, via ISerializer)
                        // - <RESULT-TYPE>     1Byte  (only with E, via ISerializer)            
                        // - <RESULT-OBJECT>   xBytes (only with E, via ISerializer)            
                        // - <RESULT-TEXT>     xBytes (only with B, via DataInputStream)
            
                        if (serializer == null)
                        {
                            //Without serializer the only possibility to send back an error messages is serialization 
                            //via DataOutputStream e.g when the session timed out
                            
                            baosResponse.write(IConnection.FLAG_BROKEN);
                            baosResponse.write(IConnection.MODE_UNCOMPRESSED);
                            
                            DataOutputStream dosContent  = new DataOutputStream(baosResponse);
                            
                            try
                            {
                                Throwable thResult;
                                
                                //Only the first Exception will be sent to the client. Normally in that special case,
                                //thats enough information
                                for (int i = 0, anz = auResult.size(), iExceptionCount = 0; i < anz && iExceptionCount == 0; i++)
                                {
                                    if (auResult.get(i).getType() == IConnection.TYPE_CALL_ERROR)
                                    {
                                        //When the stream is broken, serialize the exception via DataOutputStream!
                                        
                                        thResult = (Throwable)auResult.get(i).getObject(); 
                                        
                                        if (thResult != null)
                                        {
                                            StackTraceElement[] stack = thResult.getStackTrace();
                                            
                                            dosContent.writeUTF(thResult.getClass().getName());
                                            dosContent.writeUTF((String)CommonUtil.nvl(thResult.getMessage(), ""));
                                            
                                            if (stack != null)
                                            {
                                                dosContent.write(stack.length);
                                                
                                                for (int j = 0, anzStack = stack.length; j < anzStack; j++)
                                                {
                                                    dosContent.writeUTF((String)CommonUtil.nvl(stack[j].getClassName(), ""));
                                                    dosContent.writeUTF((String)CommonUtil.nvl(stack[j].getMethodName(), ""));
                                                    dosContent.writeUTF((String)CommonUtil.nvl(stack[j].getFileName(), ""));
                                                    dosContent.writeInt(stack[j].getLineNumber());
                                                }
                                            }
                                            else
                                            {
                                                dosContent.write(0);
                                            }
                                        }
                                        else
                                        {
                                            dosContent.writeUTF("");
                                        }
                                        
                                        iExceptionCount++;
                                    }
                                }
                            }
                            finally
                            {
                                dosContent.flush();
                            }
                        }
                        else
                        {
                            ArrayUtil<ResultObject> auOrderedResult = null;
            
                            List<Entry<String, Object>> liProperties;
                            
                            //Send the properties, if available!
                            //Here we have two solutions:
                            //a) the session is created -> use session properties
                            //b) the session creation fails -> use request properties
            
                            if (session != null)
                            {
                                liProperties = session.getProperties().getChanges(IConnectionConstants.PROPERTY_CLASSES);
                            }
                            else if (chtProperties != null)
                            {
                                liProperties = chtProperties.getChanges(IConnectionConstants.PROPERTY_CLASSES);
                            }
                            else
                            {
                                liProperties = null;
                            }
                            
                            if (liProperties != null && liProperties.size() > 0)
                            {
                                //use a transferable object-type!
                                List<Object[]> liTransferProperties = new ArrayUtil<Object[]>(liProperties.size());
            
                                for (Entry<String, Object> entry : liProperties)
                                {
                                    liTransferProperties.add(new Object[] {entry.getKey(), entry.getValue()});
                                    
                                    //forward the property to the response!
                                    pResponse.setProperty(entry.getKey(), entry.getValue());
                                }
                                
                                if (auOrderedResult == null)
                                {
                                    auOrderedResult = new ArrayUtil<ResultObject>();
                                }
                                
                                auOrderedResult.add(new ResultObject(IConnection.TYPE_PROPERTY_RESULT, liTransferProperties));
                            }
                            
                            //Send callback results in any available response!
                            if (session != null)
                            {
                                //handle the callback results
                                
                                if (auOrderedResult == null)
                                {
                                    auOrderedResult = new ArrayUtil<ResultObject>();
                                }
            
                                ArrayUtil<ResultObject> auCallBack = getCallBackResultsIntern(session);
                                
                                if (auCallBack != null)
                                {
                                    auOrderedResult.addAll(auCallBack);
                                }
                            }
                            
                            //Add all available results to the end of the response
                            
                            if (auResult != null)
                            {
                                if (auOrderedResult == null)
                                {
                                    auOrderedResult = new ArrayUtil<ResultObject>();
                                }
                                
                                auOrderedResult.addAll(auResult);
                            }
                            
                            iCallCount = auOrderedResult != null ? auOrderedResult.size() : 0;
    
                            ByteArrayOutputStream baosContent = new ByteArrayOutputStream();
                            DataOutputStream dosContent  = new DataOutputStream(baosContent);
                            
                            try
                            {
                                serializer.write(dosContent, Integer.valueOf(iCallCount));

                                if (auOrderedResult != null)
                                {
                                    ResultObject roReturn;
                                    
                                    try
                                    {
                                        for (int i = 0, cnt = auOrderedResult.size(); i < cnt; i++)
                                        {
                                            roReturn = auOrderedResult.get(i);

                                            serializer.write(dosContent, Byte.valueOf(roReturn.getType()));
                                            serializer.write(dosContent, roReturn.getObject());
                                            
                                            //add the callback-id to the result. That's important for the client
                                            //to refer the right callback worker
                                            if (roReturn.getType() == IConnection.TYPE_CALLBACK_ERROR 
                                                || roReturn.getType() == IConnection.TYPE_CALLBACK_RESULT
                                                || roReturn.getType() == IConnection.TYPE_CALLBACKRESULT_RESULT)
                                            {
                                                serializer.write(dosContent, roReturn.getCallBackId());
                                            }
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        //serialization failed!
                                        dosContent.close();
                                        
                                        //re-create
                                        baosContent = new ByteArrayOutputStream();
                                        dosContent  = new DataOutputStream(baosContent);
                                        
                                        //write "wrong" count, to be sure that the client validation (if any) doesn't fail
                                        serializer.write(dosContent, Integer.valueOf(iCallCount));
                                        
                                        serializer.write(dosContent, Byte.valueOf(IConnection.TYPE_CALL_ERROR));
                                        //serialization of an unknown exception isn't a problem! 
                                        serializer.write(dosContent, e);
                                    }
                                }
                            }
                            finally
                            {
                                dosContent.close();
                            }
                
                            baosResponse.write(IConnection.FLAG_ESTABLISHED);
                            
                            //Compression will be used when the client-support for compression is enabled
                            boolean bCompressionSupported = session != null 
                                                            //getProperties().get -> don't change the access/alive time because the session will not time out!
                                                            && Boolean.parseBoolean((String)session.getProperties().get(IConnectionConstants.COMPRESSION));
            
                            if (bCompressionSupported && baosContent.size() > IConnection.COMPRESSION_BYTES)
                            {
                                baosResponse.write(IConnection.MODE_COMPRESSED);
                                
                                GZIPOutputStream zosContent = new GZIPOutputStream(baosResponse);
                                baosContent.writeTo(zosContent);
                                zosContent.close();
    
                                baosResponse.write(MAGIC_BYTES);
                            }
                            else
                            {
                                baosResponse.write(IConnection.MODE_UNCOMPRESSED);
    
                                baosContent.writeTo(baosResponse);
                            }
                        }
            
                        if (crPending != null)
                        {
                            crPending.content = baosResponse;
                            
                            synchronized (crPending)
                            {
                                crPending.notifyAll();
                            }
                        }
                        else if (session != null && oSessionId == null)
                        {
                            ISession sessMaster = sessman.getMasterSession(session); 
                            
                            CachedResponse crOld = whmResponse.get(sessMaster);
                            
                            if (crOld == null || crOld.id.longValue() + 1 == lCommunicationId.longValue())
                            {
                                //only put new session
                                whmResponse.put(sessman.getMasterSession(session), new CachedResponse(lCommunicationId, baosResponse));
                            }
                        }
    
                        sendContent(pResponse, baosResponse);
                    }
                }
    
                return session;
            }
            catch (Exception e)
            {
                if (record != null)
                {
                    record.setException(e);
                }
                
                throw e;
            }
            finally
            {
                if (session != null)
                {
                    session.unlock();
                }
                
                CommonUtil.close(record);
            }
        }
        finally
        {
        	if (ctxt != null)
        	{
        		ctxt.release();
        	}
        }
    }

    /**
     * Sends the content to the client.
     * 
     * @param pResponse the response
     * @param pContent the content to send
     * @throws IOException if sending fails
     */
    private void sendContent(IResponse pResponse, ByteArrayOutputStream pContent) throws IOException
    {
        try
        {
            OutputStream osResponse = pResponse.getOutputStream(); 
            
            pContent.writeTo(osResponse);           
            osResponse.flush();
        }
        finally
        {
            pResponse.close();
        }
    }
    
    /**
     * Executes a remote method call request.
     *
     * @param pRequest the request which executes the command
     * @param pSerializer the serializer implementation from the request
     * @param pSession the session
     * @param pObjectName server object name/alias
     * @param pMethod method name which should be called
     * @param pParams parameters for the method call
     * @param pCallBackId identifier for asynchronous calls
     * @param pProperties the session properties
     * @return result of method call or null if it's an asynchronous method call
     * @throws Throwable if an error occurs during execution
     */
    private Object process(IRequest pRequest,
                           ISerializer pSerializer,
                           AbstractSession pSession, 
                           String pObjectName, 
                           String pMethod, 
                           Object[] pParams, 
                           Object pCallBackId,
                           ChangedHashtable<String, Object> pProperties) throws Throwable
    {
        if (pCallBackId != null)
        {
            validateCallBack(pSession, pObjectName);
        }
        
        //Special handling of the session managers
        if (pObjectName != null && IConnection.OBJ_SESSION.equals(pObjectName))
        {
            if (IConnection.MET_SESSION_CREATE.equals(pMethod))
            {
                if (pSession != null)
                {
                    //Sessions can't be created more than once!
                    throw new SecurityException("Session is already open!");
                }
                else
                {
                    return createSessionIntern
                    (
                        pRequest, 
                        pSerializer, 
                        pProperties
                    );
                }
            }       
            else if (IConnection.MET_SESSION_SUBSESSION_CREATE.equals(pMethod))
            {
                if (pSession != null)
                {
                    //Sessions can't be created more than once!
                    throw new SecurityException("Session is already open!");
                }
                else
                {
                    try
                    {
                        return createSubSessionIntern(pRequest, sessman.get(pParams[0]), pProperties);
                    }
                    catch (Throwable th)
                    {
                        throw createSessionCancelException(th);
                    }
                }
            }
            else if (IConnection.MET_SESSION_SET_PROPERTY.equals(pMethod) && pSession == null)
            {
                //If the session is not available yet, cache the properties because the next operation
                //should be the create statement
                return (List<Object[]>)pParams[0];
            }
            else if (pSession != null)
            {
                if (IConnection.MET_SESSION_SETCHECKALIVE.equals(pMethod))
                {
                    Record record = ProtocolFactory.openRecord(ICategoryConstants.SERVER, ICommandConstants.SERVER_PROCESS);
                    
                    try
                    {
                        if (record != null)
                        {
                            record.addIdentifier(pSession.getId());
                            record.setParameter(pParams);
                        }
                        
                        return setAndCheckAliveIntern(pSession, pParams);
                    }
                    finally
                    {
                        CommonUtil.close(record);
                    }
                }
                else if (IConnection.MET_SESSION_DESTROY.equals(pMethod))
                {
                    destroySessionIntern(pSession.getId());
                    
                    return null;
                }
                else if (IConnection.MET_SESSION_GET_PROPERTY.equals(pMethod))
                {
                    return getPropertyIntern(pSession, (String)pParams[0]);
                }
                else if (IConnection.MET_SESSION_GET_PROPERTIES.equals(pMethod))
                {
                    return getPropertiesIntern(pSession);
                }
                else if (IConnection.MET_SESSION_SET_PROPERTY.equals(pMethod))
                {
                    setPropertiesIntern(pSession, (List<Object[]>)pParams[0]);
                    
                    return null;
                }
                else if (IConnection.MET_SESSION_SET_NEW_PASSWORD.equals(pMethod))
                {
                    setNewPasswordInternal(pSession, (String)pParams[0], (String)pParams[1]);
                    
                    return null;
                }
            }
        }
    
        //Forward all other calls directly to the session
        return executeIntern(pSession, new Call(pCallBackId, pObjectName, pMethod, pParams));
    }
    
    /**
     * Creates a SessionCancelException as copy from root cause.
     * @param pCause the cause.
     * @return SessionCancelException
     */
    private SessionCancelException createSessionCancelException(Throwable pCause)
    {
        SessionCancelException sce = new SessionCancelException(pCause.getMessage(), pCause.getCause());
        sce.setStackTrace(pCause.getStackTrace());

        return sce;
    }
    
    /**
     * Executes a remote method call request through the session.
     *
     * @param pSession the session
     * @param pCall the call information
     * @return result of method call or null if it's an asynchronous method call
     * @throws Throwable if an error occurs during execution
     */ 
    private Object executeIntern(AbstractSession pSession, Call pCall) throws Throwable
    {
        //no Session -> no call
        if (pSession != null)
        {
            return pSession.execute(pCall);
        }
        
        throw new SecurityException("No session for call '" + pCall.formatMethod() + "'");
    }
        
    /**
     * Validate if call backs are allowed with given session or objectname.
     * 
     * @param pSession the session
     * @param pObjectName the object name
     * @throws SecurityException if call back is not allowed
     */
    private void validateCallBack(AbstractSession pSession, String pObjectName)
    {
        //Callbacks are bound to a session! Not possible without!
        if (pSession == null)
        {
            throw new SecurityException("Call back is not allowed!");
        }
        
        //Special handling of the session managers
        if (pObjectName != null && IConnection.OBJ_SESSION.equals(pObjectName))
        {
            throw new SecurityException("Call back is not allowed!");
        }
    }
    
    /**
     * Creates a new session through the session manager.
     *
     * @param pRequest the request which creates the session
     * @param pSerializer the serializer implementation for the new session
     * @param pProperties the initial session properties
     * @return session identifier of newly created <code>Session</code>
     * @throws Throwable if the session can not be created
     */
    private Object createSessionIntern(IRequest pRequest, 
                                       ISerializer pSerializer, 
                                       ChangedHashtable<String, Object> pProperties) throws Throwable
    {
        return sessman.createSession(pRequest, pSerializer, pProperties);
    }
    
    /**
     * Creates a sub session through the session manager.
     * 
     * @param pRequest the request which creates the sub session
     * @param pSession a valid session
     * @param pProperties the initial session properties
     * @return session identifier of newly created <code>SubSession</code>
     * @throws Throwable if the session can not be created
     */
    private Object createSubSessionIntern(IRequest pRequest, 
                                          AbstractSession pSession, 
                                          ChangedHashtable<String, Object> pProperties) throws Throwable
    {
        return sessman.createSubSession(pRequest, pSession, pProperties);
    }
    
    /**
     * Destroyes a session through the session manager.
     * 
     * @param pSessionId session identifier
     */ 
    private void destroySessionIntern(Object pSessionId)
    {
        sessman.destroy(pSessionId);
    }

    /**
     * Sets a session property.
     * 
     * @param pSession the session
     * @param pName the property name
     * @param pValue the value of the property or <code>null</code> to delete the property
     */
    private void setPropertyIntern(AbstractSession pSession, String pName, Object pValue)
    {
        pSession.setProperty(pName, pValue);
    }
    
    /**
     * Sets multiple properties.
     * 
     * @param pSession the session
     * @param pProperties the key/value pairs as list of <code>Object[]</code>
     */
    private void setPropertiesIntern(AbstractSession pSession, List<Object[]> pProperties)
    {
        pSession.setProperties(pProperties);
    }
    
    /**
     * Gets the value of a session property.
     * 
     * @param pSession the session
     * @param pName the property name
     * @return the value of the property or <code>null</code> if the property is not available
     */
    private Object getPropertyIntern(AbstractSession pSession, String pName)
    {
        Object o = pSession.getProperty(pName);

        if (o != null)
        {
            for (int i = 0; i < IConnectionConstants.PROPERTY_CLASSES.length; i++)
            {
                if (IConnectionConstants.PROPERTY_CLASSES[i].isAssignableFrom(o.getClass()))
                {
                    return o;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Gets a clone of accessible session properties.
     * 
     * @param pSession the session
     * @return a {@link ChangedHashtable} with the property names and values
     */
    private ChangedHashtable<String, Object> getPropertiesIntern(AbstractSession pSession)
    {
        List<Entry<String, Object>> liValues = pSession.getProperties().getMapping(IConnectionConstants.PROPERTY_CLASSES);
        
        ChangedHashtable<String, Object> chtProps = new ChangedHashtable<String, Object>();
        
        Object oValue;
        
        for (Entry<String, Object> entry : liValues)
        {
            oValue = entry.getValue();
            
            if (oValue != null)
            {
                chtProps.put(entry.getKey(), oValue, false);
            }
        }
        
        return chtProps;
    }
    
    /**
     * Returns all available objects of asynchronous executions for a session.
     * 
     * @param pSession the session
     * @return result objects or null if there are no result objects for the session
     */
    private ArrayUtil<ResultObject> getCallBackResultsIntern(AbstractSession pSession)
    {
        if (pSession != null)
        {
            return pSession.removeCallBackResults();
        }
        
        return null;
    }

    /**
     * Sets the alive state for a session and validates the alive
     * state of sub sessions.
     * 
     * @param pSession the master sessin
     * @param pSubSessionId a list of sub sessions
     * @return the list of invalid sub sessions
     */
    private Object[] setAndCheckAliveIntern(AbstractSession pSession, Object[] pSubSessionId)
    {
        //it's sufficient to set the alive of the main session, because the sub sessions
        //return the alive from the parent session, and have not an explicit alive time
        pSession.setLastAliveTime(System.currentTimeMillis());
        
        if (pSubSessionId == null || pSubSessionId.length == 0)
        {
            return null;
        }
        else
        {
            ArrayUtil<Object> auInvalidSessions = new ArrayUtil<Object>();

            for (int i = 0, anz = pSubSessionId.length; i < anz; i++)
            {
                try
                {
                    //don't perform the check if the session is a SubSession and is sub
                    //of the pSession (not imporant because it's only a check and no special
                    //access)
                    sessman.get(pSubSessionId[i]); 
                }
                catch (RuntimeException re)
                {
                    auInvalidSessions.add(pSubSessionId[i]);
                }
            }
            
            if (auInvalidSessions.size() > 0)
            {
                Object[] oInvalidSessions = new Object[auInvalidSessions.size()];
                auInvalidSessions.toArray(oInvalidSessions);
                
                return oInvalidSessions;
            }
            else
            {
                return null;
            }
        }
    }
    
    /**
     * Sets a new password for the user of a session.
     * 
     * @param pSession the session
     * @param pOldPassword the old password
     * @param pNewPassword the new password
     * @throws Throwable if an error occurs during execution
     */
    private void setNewPasswordInternal(AbstractSession pSession, String pOldPassword, String pNewPassword) throws Throwable
    {
        pSession.setNewPassword(pOldPassword, pNewPassword);
    }

    /**
     * Returns the monitoring object for this server.
     * 
     * @return the monitoring object
     */
    public final Monitoring getMonitoring()
    {
        return monitoring;
    }
    
    /**
     * Logs a remote call.
     * 
     * @param pSessionId the session identifier
     * @param pCommunicationId the communication id
     * @param pObjectName server object name/alias
     * @param pMethod method name which should be called
     * @param pParams parameters for the method call
     * @param pCallBackId identifier for asynchronous calls
     * @param pResult result of a method call or null if it's an asynchronous method call
     * @param pError the exception if the method call throws an exception
     * @param pDuration the duration of the call
     */
    private final void log(Object pSessionId, 
                           Object pCommunicationId,
                           String pObjectName, 
                           String pMethod, 
                           Object[] pParams, 
                           Object pCallBackId, 
                           Object pResult, 
                           Throwable pError,
                           long pDuration)
    {
        log.debug("SESSION-ID: ",
                  pSessionId,
                  "\nCOMMUNICATION-ID: ",
                  pCommunicationId,
                  "\nOBJECTNAME: ",
                  pObjectName,
                  "\nMETHOD:     ",
                  pMethod,
                  "\nPARAMS:     ",
                  pParams,
                  "\nCALLBACK:   ",
                  pCallBackId,
                  "\nRESULT:     ",
                  pResult,
                  "\nEXCEPTION:  ",
                  pError,
                  pError != null ? "DURATION:   " : "\nDURATION:   ",
                  Long.valueOf(pDuration),
                  " ms");
    }

    /**
     * Logs a remote call as error.
     * 
     * @param pSessionId the session identifier
     * @param pCommunicationId the communication id
     * @param pObjectName server object name/alias
     * @param pMethod method name which should be called
     * @param pParams parameters for the method call
     * @param pCallBackId identifier for asynchronous calls
     * @param pResult result of a method call or null if it's an asynchronous method call
     * @param pError the exception if the method call throws an exception
     * @param pDuration the duration of the call
     */
    private final void error(Object pSessionId, 
                             Object pCommunicationId,
                             String pObjectName, 
                             String pMethod, 
                             Object[] pParams, 
                             Object pCallBackId, 
                             Object pResult, 
                             Throwable pError,
                             long pDuration)
    {
        log.error("SESSION-ID: ",
                  pSessionId,
                  "\nCOMMUNICATION-ID: ",
                  pCommunicationId,
                  "\nOBJECTNAME: ",
                  pObjectName,
                  "\nMETHOD:     ",
                  pMethod,
                  "\nPARAMS:     ",
                  pParams,
                  "\nCALLBACK:   ",
                  pCallBackId,
                  "\nRESULT:     ",
                  pResult,
                  "\nEXCEPTION:  ",
                  pError,
                  pError != null ? "DURATION:   " : "\nDURATION:   ",
                  Long.valueOf(pDuration),
                  " ms");
    }
    
    /**
     * Creates the new server context with default initialization.
     * 
     * @return the context
     */
    private final ServerContext createServerContextIntern()
    {
    	if (ServerContext.getCurrentInstance() != null)
    	{
    		//don't create a new instance
    		return null;
    	}

    	ServerContext ctxt = new ServerContextImpl(this);
	        
        if (sInitialSystemIdentifier == null)
        {
            sInitialSystemIdentifier = ctxt.getSystemIdentifier();
        }
    	
        return ctxt;
    }

    /**
     * Creates a new server context with initial system identifier.
     * 
     * @return the context
     */
    final ServerContext createServerContext()
    {
    	if (ServerContext.getCurrentInstance() != null)
    	{
    		//don't create a new instance
    		return null;
    	}

    	ServerContextImpl ctxt = new ServerContextImpl(this, sInitialSystemIdentifier);

        return ctxt;
    }
    
    /**
     * Configures the current server context with given session if necessary.
     * 
     * @param pSession the session to use
     */
    private void configureContext(ISession pSession)
    {
    	ServerContext ctxt = ServerContext.getCurrentInstance();
    	
    	if (ctxt != null)
    	{
    		ISession session = ctxt.getSession();
    		
    		if (session == null)
    		{
    			if (ctxt instanceof AbstractServerContext)
    			{
    				((AbstractServerContext)ctxt).setSession(pSession);
    			}
    		}
    	}
    }
    
    /**
     * Clears the stream (read all available bytes). The serializer for the given session
     * will be used, if possible
     * 
     * @param pStream the stream
     * @param pSessionId the session id
     */
    private void clearBuffer(DataInputStream pStream, Object pSessionId)
    {
        //DON'T remove the session-id -> maybe another request 
        SerializerInfo sinf = mapSerializer.get(pSessionId);
        
        if (sinf == null)
        {
            //default
            sinf = mapSerializer.get(null);
        }
        
        try
        {
            ISerializer ser = sinf.clsSerializer.newInstance();
            
            Object o = ser.read(pStream);
            
            log.debug("First: ", o);

            if (o instanceof String)
            {
                o = ser.read(pStream);
                
                log.debug("Additional: ", o);
            }
            
            int iCallCount = ((Integer)o).intValue();
            
            for (int i = 0; i < iCallCount; i++)
            {
                o = ser.read(pStream);
                                
                log.debug("Command: ", o);
            }
        }
        catch (Exception e)
        {
            //we can't handle this exception
        }
    }
    
    /**
     * Checks whether a specific serializer class can be used. The configuration is done with server zone.
     * Simply configure the class with
     * 
     * @param pClassName the serializer class name
     */
    protected void checkSerializerAccess(String pClassName)
    {
    	if (pClassName == null
    		|| !liSerializerAccess.contains(pClassName))
    	{
    		throw new SecurityException("Access to '" + pClassName + "' is denied!");
    	}
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>CachedResponse</code> is the last sent response. It will be used for
     * supporting connections with retry mechanism.
     * 
     * @author René Jahn
     */
    private static final class CachedResponse
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the communication id. */
        protected Long id;

        /** the cached data. */
        protected ByteArrayOutputStream content;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>CachedResponse</code> without content.
         * 
         * @param pId the id
         */
        private CachedResponse(Long pId)
        {
            id = pId;
        }

        /**
         * Creates a new instance of <code>CachedResponse</code> with given id and content.
         * 
         * @param pId the id
         * @param pContent the content
         */
        private CachedResponse(Long pId, ByteArrayOutputStream pContent)
        {
            id = pId;
            content = pContent;
        }
        
    }   // CachedResponse
    
    /**
     * The <code>SerializerInfo</code> is a simple container for a serializer class name 
     * and the object creation time. It will be used for caching.
     * 
     * @author René Jahn
     */
    private static final class SerializerInfo
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** the serializer class. */
        private Class<? extends ISerializer> clsSerializer;

        /** the creation time. */
        private long lCreation = System.currentTimeMillis();
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>SerializerInfo</code> for the given
         * class.
         * 
         * @param pClass the class
         */
        private SerializerInfo(Class<? extends ISerializer> pClass)
        {
            clsSerializer = pClass;
        }
        
    }   // SerializerInfo

    /**
     * The <code>SessionListener</code> is the internal session listener.
     * 
     * @author René Jahn
     */
    private static final class SessionListener implements ISessionListener
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the server. */
        private Server server;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>InternalSessionListener</code> for 
         * the given server.
         * 
         * @param pServer the server
         */
        private SessionListener(Server pServer)
        {
            server = pServer;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        public void sessionCreated(ISession pSession)
        {
        }

        /**
         * {@inheritDoc}
         */
        public void sessionDestroyed(ISession pSession)
        {
            //clear cache (only an instance of MasterSession will clear the cache)
            server.whmResponse.remove(pSession);
            
            if (pSession instanceof AbstractSession)
            {
                ISerializer ser = ((AbstractSession)pSession).getSerializer();
                
                //save sessions with custom serializer
                if (ser != null)
                {
                    //cleanup (max. 60 minutes)
                    if (server.mapSerializer.size() > 1)
                    {
                        Map<Object, SerializerInfo> mapCopy;
                        
                        synchronized (server.mapSerializer)
                        {
                        	mapCopy = new HashMap<Object, SerializerInfo>(server.mapSerializer);
                        }
                        
                        long lNow = System.currentTimeMillis();
                        long lCreation;
                        
                        for (Entry<Object, SerializerInfo> entry : mapCopy.entrySet())
                        {
                            //ignore default serializer (NPE otherwise in following check - see #1788)
                            if (entry.getKey() != null)
                            {
                                lCreation = entry.getValue().lCreation;
                                
                                if (lCreation > 0 && lCreation + 3600000 < lNow)
                                {
                                    server.mapSerializer.remove(entry.getKey());
                                }
                            }
                        }
                    }
                    
                    SerializerInfo sinf = server.mapSerializer.get(null);
                    
                    if (sinf.clsSerializer != ser.getClass())
                    {
                        server.mapSerializer.put(pSession.getId(), new SerializerInfo(ser.getClass()));
                    }
                }
            }
        }
        
    }   // SessionListener

}   // Server
