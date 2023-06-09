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
 * 05.10.2008 - [JR] - createSession: moved authentication to MasterSession
 * 27.04.2009 - [JR] - replaced logging
 * 06.05.2009 - [JR] - implemented AbstractSessionManager
 * 10.05.2009 - [JR] - getSecurityManager: used AbstractSession as parameter instead of the Application name
 * 17.08.2009 - [JR] - CONTROLLER_DELAY: 10000L instead of 5000L
 * 07.10.2009 - [JR] - called logout from the SecurityManager when a session will be destroyed
 * 14.11.2009 - [JR] - destroy: don't throw invalid session id - throw SessionExpired (user-friendly) and of
 *                     course, it's possible!
 * 25.12.2010 - [JR] - removed final from class   
 * 29.12.2010 - [JR] - #231: createSecurityManager implemented     
 * 14.02.2011 - [JR] - #285: getSecurityManager checks the configuration   
 * 11.05.2011 - [JR] - getSecurityManagerFromCache implemented
 *                   - cached securitymanager class name (fix for sub classes)   
 * 25.05.2011 - [JR] - #362: destroy sub sessions with destroy()
 *                   - #364: destroy: call logout() of security manager only for master sessions
 * 23.08.2013 - [JR] - #774: isAvailable implemented    
 * 17.09.2013 - [JR] - removed Memory.gc
 * 10.10.2013 - [JR] - destroy session if postCreate... fails              
 * 15.10.2013 - [JR] - setControllerInterval implemented      
 * 18.12.2013 - [JR] - destroy session with id and not with session instance     
 * 01.02.2014 - [JR] - #939: create security manager with custom class loader  
 * 21.02.2014 - [JR] - #956: cache security manager per session 
 * 12.06.2014 - [JR] - #1066: release session controlled security manager
 * 02.08.2014 - [JR] - #1094: remove SubSession from MasterSession in destroy 
 * 26.01.2015 - [JR] - #1238: Exception recording
 * 06.05.2015 - [JR] - synchronized access to event listener
 * 08.06.2016 - [JR] - #25: isValid implemented
 * 23.06.2016 - [JR] - push feature   
 * 12.03.2019 - [JR] - #1998: destroy method implemented                       
 */
package com.sibvisions.rad.server;

import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.SessionInvalidatedException;
import javax.rad.server.AbstractSessionManager;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.ServerContext;
import javax.rad.server.event.FailedSessionEvent;
import javax.rad.server.event.IFailedSessionListener;
import javax.rad.server.event.ISessionListener;
import javax.rad.server.push.IPushHandler;
import javax.rad.server.push.IPushReceiver;
import javax.rad.server.push.PushMessage;

import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.GroupHashtable;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DefaultSessionManager</code> handles the access to all sessions
 * created through client connections. 
 * 
 * @author Ren� Jahn
 */
public class DefaultSessionManager extends AbstractSessionManager
                                   implements IPushHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** the cache mode for security manager(s). */
    private enum CacheMode
    {
        /** cache per application (global). */
        Application,
        /** cache per session. */
        Session
    };
    
	/** delay for the <code>Controller</code> between two checks. */
	private static final long CONTROLLER_INTERVAL = 10000L;

	/** the relevant system properties. */
	private static final String[] USED_SYSPROPS = {"user.timezone", "user.name", "os.name", "os.version", 
												   "os.arch", "java.vendor", "java.version", 
												   "java.class.version", "java.vm.name", "file.encoding", 
												   "file.separator", "path.separator", "line.separator"};
	
	/** the name for the initialization property. */
	private static final String PROPERTY_INIT = "<init>";
	
	/** the non serializable init object. */
	private static final Object INIT = new Object();
	
	
	/** list of sessions. */
	private Hashtable<Object, AbstractSession> htSessions = new Hashtable<Object, AbstractSession>();
	
	/** list of used security managers (per application). */
	private Hashtable<String, ISecurityManager> htApplicationSecManager = null;

    /** list of used security managers (per application and session). */
    private GroupHashtable<String, Object, ISecurityManager> ghtSessionSecManager = null;
	
	/** list of used security manager classes. */
	private Hashtable<String, String> htSecManagerClass = new Hashtable<String, String>();

	/** list of all registered session event listeners. */
	private ArrayUtil<ISessionListener> auSessionListeners = new ArrayUtil<ISessionListener>();
	
	/** list of all registered failed session event listeners. */
	private ArrayUtil<IFailedSessionListener> auFailedSessionListeners = new ArrayUtil<IFailedSessionListener>();

	/** mapping of push receiver to session id. */
    private Hashtable<Object, IPushReceiver> htPushReceiver = new Hashtable<Object, IPushReceiver>();

    /** object for the synchronized access to session objects. */
	private Object oSync = new Object();
	
	/** controller thread for continuous session verifying. */
	private Thread thController = null;
	
	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(getClass());
	
	/** the controller check interval. */
	private static long lControllerInterval = CONTROLLER_INTERVAL;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an instance of <code>DefaultSessionManager</code> for a special
	 * communication server.
	 * 
	 * @param pServer communication server
	 */
	protected DefaultSessionManager(Server pServer)
	{
		super(pServer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public final AbstractSession get(Object pSessionId)
	{
		AbstractSession session = null;
		
		if (pSessionId != null)
		{
			synchronized (oSync)
			{
				session = htSessions.get(pSessionId);
			}

			if (session != null)
			{
				long lNow = System.currentTimeMillis();

				if (session.isInactive(lNow) || !session.isAlive(lNow))
				{
				    //don't use setProperty, because we won't change the access time
				    session.getProperties().put("expired", Boolean.TRUE);

				    log.info("Destroy invalid session: ", pSessionId, ", Inactive: ", Boolean.valueOf(session.isInactive(lNow)), 
				    		                                          ", Alive: ", Boolean.valueOf(session.isAlive(lNow)),
				    		                                          ", Now: ", Long.valueOf(lNow),
				    		                                          ", Access time: ", Long.valueOf(session.getLastAccessTime()),
				    		                                          ", Start time: ", Long.valueOf(session.getStartTime()));				    
				    
					destroy(pSessionId);
					
					throw new SessionInvalidatedException("Session expired '" + pSessionId + "'");
				}
				
				return session;
			}
			else
			{
				//If the Controller kills a session, then the session won't be found, though
				//the session-id is vaild! It's a better behaviour to send the client a session expired
				//message instead of invalid session-id. It's user friendly.
				//
				//If the session-id is a fake id (hack attempt), then the message is not correct, but
				//in that case the hacker should believe everything is fine.
				throw new SessionExpiredException("Session expired '" + pSessionId + "'");
			}
		}
		else
		{
			throw new SecurityException("Invalid session id '" + pSessionId + "'");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void addSessionListener(ISessionListener pListener)
	{
	    synchronized (auSessionListeners)
	    {
	        auSessionListeners.add(pListener);
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void removeSessionListener(ISessionListener pListener)
	{
        synchronized (auSessionListeners)
        {
			auSessionListeners.remove(pListener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final ISessionListener[] getSessionListeners()
	{
        synchronized (auSessionListeners)
        {
			ISessionListener[] islResult = new ISessionListener[auSessionListeners.size()];
			
			return auSessionListeners.toArray(islResult);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void addFailedSessionListener(IFailedSessionListener pListener)
	{
	    synchronized (auFailedSessionListeners)
	    {
	    	auFailedSessionListeners.add(pListener);
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void removeFailedSessionListener(IFailedSessionListener pListener)
	{
        synchronized (auFailedSessionListeners)
        {
        	auFailedSessionListeners.remove(pListener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final IFailedSessionListener[] getFailedSessionListeners()
	{
        synchronized (auFailedSessionListeners)
        {
        	IFailedSessionListener[] islResult = new IFailedSessionListener[auFailedSessionListeners.size()];
			
			return auFailedSessionListeners.toArray(islResult);
		}
	}
	
    /**
     * Gets whether a session is known from this session manager. This means whether the session is
     * in the list of currently opened sessions. 
     * 
     * @param pSessionOrId session identifier or session instance
     * @return <code>true</code> if the session is available, <code>false</code> otherwise
     */
    public boolean isAvailable(Object pSessionOrId)
    {
        if (pSessionOrId != null)
        {
            Object oId;
            
            if (pSessionOrId instanceof ISession)
            {
                oId = ((ISession)pSessionOrId).getId();
            }
            else
            {
                oId = pSessionOrId;
            }
            
            //don't check inactivity or alive of session, because this method only checks if the session
            //is "known"

            synchronized (oSync)
            {
                return htSessions.get(oId) != null;
            }
        }
        
        return false;
    }	
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void registerPushReceiver(Object pConnectionId, IPushReceiver pReceiver)
	{
	    if (pConnectionId == null)
	    {
	        throw new IllegalArgumentException("Can't register a push receiver without connection identifier!");
	    }

        if (pReceiver == null)
        {
            throw new IllegalArgumentException("Push receiver can't be null!");
        }
	    
        htPushReceiver.put(pConnectionId, pReceiver);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void unregisterPushReceiver(Object pConnectionId)
	{
        if (pConnectionId == null)
        {
            throw new IllegalArgumentException("Can't unregister a push receiver without connection identifier!");
        }
	    
        htPushReceiver.remove(pConnectionId);
	}

    /**
     * {@inheritDoc}
     */
	public void push(PushMessage pMessage)
	{
	    IPushReceiver receiver = htPushReceiver.get(pMessage.getSession().getId());

        if (receiver != null)
        {
            receiver.receivedMessage(pMessage);
        }
        else
        {
            log.error("Can't push to session ", pMessage.getSession().getId(), " because receiver wasn't found!");
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an authenticated session for an application.
	 * 
	 * @param pRequest the request which creates the session
	 * @param pSerializer the serializer for the session
	 * @param pProperties the initial session properties
	 * @return session identifier of newly created <code>Session</code>
	 * @throws Throwable if the security manager detects a problem or the session properties can not be set
	 */
	public final Object createSession(IRequest pRequest, 
									  ISerializer pSerializer, 
									  ChangedHashtable<String, Object> pProperties) throws Throwable
	{
	    Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_MASTER);
	    
	    ChangedHashtable<String, Object> chtProps = null;
	    
	    try
	    {
	        Record recProp = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_INIT_PROPERTIES);
	        
	        try
	        {
    	        chtProps = getInitialProperties(pProperties, pRequest);
    	        
    	        if (recProp != null)
    	        {
    	        	recProp.setParameter(secureProperties(chtProps));
    	        }
	        }
	        finally
	        {
	            CommonUtil.close(recProp);
	        }
	        
	        //put a special, non serializable, object
	        chtProps.put(PROPERTY_INIT, INIT, false);
	        
            //Start/Create a NEW session
            MasterSession session = new MasterSession(this, chtProps);

            try
	        {
        		ServerContext ctxt = ServerContext.getCurrentInstance();
        		
        		if (ctxt instanceof AbstractServerContext)
        		{
        		    ((AbstractServerContext)ctxt).setSession(session);
        		}
    
        		if (record != null)
        		{
        		    record.addIdentifier(session.getId());
        		    
        		    HttpContext hctxt = HttpContext.getCurrentInstance();
        		    
        		    String sAgent = null;
        		    
        		    if (hctxt != null)
        		    {
        		        try
        		        {
            		        //Reflective to avoid references
            		        sAgent = (String)Reflective.call(hctxt.getRequest(), "getHeader", "User-Agent");
        		        }
        		        catch (Throwable th)
        		        {
        		            log.debug(th);
        		        }
        		    }
        		    
        		    record.setParameter(session.getLifeCycleName(), session.getUserName(), CommonUtil.nvl(sAgent, "unknown"));
        		}
        		
                Record recConfig = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_POST_CREATE);
                
                try
                {
            		session.setSerializer(pSerializer);		
            		
            		configureListener(session);
            		
            		addIntern(session);
            		
            		try
            		{
            			postCreateSession(session);
            		}
            		catch (Throwable th)
            		{
            			destroy(session.getId());
            			
            			throw th;
            		}
            		
            		return session.getId();
                }
                finally
                {
                    CommonUtil.close(recConfig);
                }
	        }
	        finally
	        {
	            session.chtExternalProperties.remove(PROPERTY_INIT);
	            session.chtProperties.remove(PROPERTY_INIT);
	        }
	    }
	    catch (Throwable th)
	    {
	    	if (record != null)
	    	{
	    		record.setException(th);
	    	}
	    	
	        try
	        {
	            sessionCreationError(th);
	        }
	        catch (Throwable thr)
	        {
	            log.error(thr);
	        }
	        
	    	fireSessionFailed(MasterSession.class, th, chtProps != null ? chtProps : pProperties);
	        
	        throw th;
	    }
	    finally
	    {
	        CommonUtil.close(record);
	    }
	}

	/**
	 * Creates a new <code>SubSession</code> for an already authenticated main session.
	 * This method doesn't check if the session is valid.
	 * 
	 * @param pRequest the request which creates the sub session
	 * @param pSession a valid session
	 * @param pProperties the initial session properties
	 * @return the new <code>SubSession</code>
	 * @throws Throwable the session properties can not be set
	 */
	public final Object createSubSession(IRequest pRequest, 
			              				 AbstractSession pSession, 
			              				 ChangedHashtable<String, Object> pProperties) throws Throwable
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_SUB);
        
        ChangedHashtable<String, Object> chtProps = null;

        try
        {
            if (record != null)
            {
                record.addIdentifier(pSession.getId());
                record.addIdentifier("MASTER");
            }
        
            Record recProp = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_INIT_PROPERTIES);
            
            try
            {
                chtProps = getInitialProperties(pProperties, pRequest);
                
    	        if (recProp != null)
    	        {
    	        	recProp.setParameter(secureProperties(chtProps));
    	        }
            }
            finally
            {
                CommonUtil.close(recProp);
            }

            //put a special, non serializable, object
            chtProps.put(PROPERTY_INIT, INIT, false);
            
            //A Sub Session can only be created through a Master Session
    		MasterSession sessMaster = (MasterSession)getMasterSession(pSession);

    		SubSession session = new SubSession(sessMaster, chtProps);

    		try
    		{
                ServerContext ctxt = ServerContext.getCurrentInstance();
                
                if (ctxt instanceof AbstractServerContext)
                {
                    ((AbstractServerContext)ctxt).setSession(session);
                }
        		
                if (record != null)
                {
                    record.addIdentifier(session.getId());
    
                    record.setParameter(session.getLifeCycleName());
                }
        		
                Record recConfig = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_POST_CREATE);
                
                try
                {
                	configureListener(session);
                	
            		addIntern(session);
            		
            		try
            		{
            			postCreateSubSession(sessMaster, session);
            		}
            		catch (Throwable th)
            		{
            			destroy(session.getId());
            			
            			throw th;
            		}
            		
            		return session.getId();
                }
                finally
                {
                    CommonUtil.close(recConfig);
                }
    		}
            finally
            {
                session.chtExternalProperties.remove(PROPERTY_INIT);
                session.chtProperties.remove(PROPERTY_INIT);
            }
        }
        catch (Throwable th)
        {
	    	if (record != null)
	    	{
	    		record.setException(th);
	    	}

            try
            {
                subSessionCreationError(th);
            }
            catch (Throwable thr)
            {
                log.error(thr);
            }
            
	    	fireSessionFailed(SubSession.class, th, chtProps != null ? chtProps : pProperties);
            
            throw th;
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * Destroyes a session and dependent sub sessions.
	 * 
	 * @param pSessionOrId session identifier or session instance
	 * @throws SecurityException if the session identifier is unknown
	 */
	public final void destroy(Object pSessionOrId)
	{
	    Object oId;
	    
	    if (pSessionOrId instanceof ISession)
	    {
	        oId = ((ISession)pSessionOrId).getId();
	    }
	    else
	    {
	        oId = pSessionOrId;
	    }
	    
	    destroy(oId, false);
	}
	
	/**
	 * Destroys all sessions and releases all cached security managers.
	 */
	final void destroy()
	{
		//Get a clone of session ids to be independent of create and destroy calls
		ArrayUtil<Object> alSessionIds = getSessionIds();
		
		for (Object obj : alSessionIds)
		{
			try
			{
				destroy(obj);
			}
			catch (Throwable th)
			{
				//ignore
			}
		}
		
		synchronized (oSync)
		{
			htSessions.clear();
		}

		ArrayUtil<ISecurityManager> auManagers = new ArrayUtil<ISecurityManager>();
		
		if (htApplicationSecManager != null)
		{
			try
			{
				auManagers.addAll(htApplicationSecManager.values());
			}
			catch (Throwable th)
			{
				//ignore
			}
			
			htApplicationSecManager.clear();
		}
		
		if (ghtSessionSecManager != null)
		{
			try
			{
				for (Enumeration<String> en = ghtSessionSecManager.groups(); en.hasMoreElements();)
				{
					for (Enumeration<ISecurityManager> enSec = ghtSessionSecManager.elements(en.nextElement()); enSec.hasMoreElements();)
					{
						auManagers.add(enSec.nextElement());
					}
				}
			}
			catch (Throwable th)
			{
				//ignore
			}
			
			ghtSessionSecManager.clear();
		}
		
		for (ISecurityManager manager : auManagers)
		{
			try
			{
				manager.release();
			}
			catch (Throwable th)
			{
				//ignore
			}
		}
	}
	
    /**
     * Destroyes a session and dependent sub sessions.
     * 
     * @param pSessionId session identifier
     * @param pImpliciteDestroy whether the destroy call is called during another destroy call (hierarchy)
     * @throws SecurityException if the session identifier is unknown
     */
	private void destroy(Object pSessionId, boolean pImpliciteDestroy)
	{
		if (pSessionId != null)
		{
            ISession sessOld = null;

            Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_DESTROY);
		    
            try
            {
    		    if (record != null)
    		    {
    		        record.addIdentifier(pSessionId);
    		    }
    		    
    			ISession session;
    
    			synchronized (oSync)
    			{
    				session = htSessions.remove(pSessionId);
    			}
    
                try
                {
        			if (session == null)
        			{
        			    log.info("Destroy session but not found: ", pSessionId);

        			    //cleanup: to be sure
        			    if (ghtSessionSecManager != null)
        			    {
        			        synchronized (ghtSessionSecManager)
        			        {
            			        List<ISecurityManager> liSecs = ghtSessionSecManager.removeKey(pSessionId);
            			        
            			        if (liSecs != null)
            			        {
            			            for (ISecurityManager isec : liSecs)
            			            {
            			                releaseSecurityManager(isec);
            			            }
            			        }
        			        }
        			    }
        			    
        				//same reason as described in get(Object)
        				throw new SessionExpiredException("Session expired '" + pSessionId + "'");
        			}
        			else
        			{
        				if (session instanceof AbstractSession)
        				{
        					((AbstractSession)session).setDestroying(true);
        				}
        				
        				log.info("Destroy session: ", pSessionId);
        				
        				ServerContext ctxt = ServerContext.getCurrentInstance();
        			    
        	            if (ctxt instanceof AbstractServerContext)
        	            {
        	                sessOld = ((AbstractServerContext)ctxt).getSession();
        	                
        	                ((AbstractServerContext)ctxt).setSession(session);
        	            }
        			}
    
    				if (session instanceof MasterSession)
    				{
    				    ISecurityManager secman;
    				    
    				    boolean bReleaseSecMan = false;
    				    
                        //it's not possible to authenticate without security manager -> a securitymanager must be present!
    				    if (getCacheMode(session) == CacheMode.Application)
    				    {
        					secman = htApplicationSecManager.get(session.getApplicationName());
    				    }
    				    else
    				    {
    				        secman = ghtSessionSecManager.remove(session.getApplicationName(), session.getId());
    				        
    				        bReleaseSecMan = true;
    				    }

    				    try
    				    {
        				    //this is possible if e.g. removeSecurityManagersFromCache was called
        				    if (secman != null)
        				    {
	        					try
	        					{
	        						secman.logout(session);
	        					}
	        					catch (Throwable th)
	        					{
	        						//should not happen!
	        						log.error(th);
	        					}
        				    }
        					
        					//Discard all dependent sessions
        					ArrayUtil<SubSession> auSubSessions = ((MasterSession)session).removeSubSessions();
        					
        					if (auSubSessions != null)
        					{
        						for (int i = 0, anz = auSubSessions.size(); i < anz; i++)
        						{
        							try
        							{
        								destroy(auSubSessions.get(i).getId(), true);
        							}
        							catch (Throwable se)
        							{
        								log.debug(se);
        							}
        						}
        					}
    				    }
    				    finally
    				    {
    				        if (bReleaseSecMan)
    				        {
    				            releaseSecurityManager(secman);
    				        }
    				    }
    				}
    				else if (session instanceof SubSession)
    				{
    				    if (!pImpliciteDestroy && record != null)
    				    {
    				        //fake identifier because the search-path should be the same in any case:
    				        //* if master automatically closes subs
    				        //* if sub will be closed manually
                            record.addIdentifier("DESTROY", 0);
    				        record.addIdentifier(((SubSession)session).getMasterSession().getId(), 0);
    				    }
    				    
    				    //This will be called if a master connection will be closed, but does nothing in that case because subsession 
    				    //is already removed
    				    ((SubSession)session).getMasterSession().removeSubSession((SubSession)session);
    				}
    			}
    			finally
    			{
    				//notify listeners
    				fireSessionDestroyed(session);
    			}
		    }
		    finally
		    {
		        CommonUtil.close(record);
		        
		        if (sessOld != null)
		        {
		            ((AbstractServerContext)ServerContext.getCurrentInstance()).setSession(sessOld);
		        }
		        
                htPushReceiver.remove(pSessionId);
		    }
		}
	}

	/**
	 * Configures listeners.
	 * 
	 * @param pSession the session
	 */
	protected void configureListener(AbstractSession pSession)
	{
	}
	
	/**
	 * Adds a session to the internal store of known sessions.
	 * 
	 * @param pSession the session
	 */
	private void addIntern(AbstractSession pSession)
	{
		synchronized (oSync)
		{
			htSessions.put(pSession.getId(), pSession);
		}
		
		//notify listeners
		fireSessionCreated(pSession);
		
		startController();
	}
	
	/**
	 * Gets the security manager for an application.
	 * 
	 * @param pSession the accessing session
	 * @return the security manager or null if the application has no security manager
	 * @throws Exception if it's not possible to initialize a defined security manager
	 */
	final ISecurityManager getSecurityManager(ISession pSession) throws Exception
	{
		String sApplicationName = pSession.getApplicationName();
		
		ISecurityManager ismSecurity = null;
		
		
        String sSecMan = null;

        CacheMode cacheMode = getCacheMode(pSession);
        
        //Ideally the SecurityManager will be reused
		if (cacheMode == CacheMode.Application)
		{
    		if (htApplicationSecManager != null)
    		{
    			ismSecurity = htApplicationSecManager.get(sApplicationName);
    		}
		}
		else 
		{
            if (ghtSessionSecManager != null)
            {
                ismSecurity = ghtSessionSecManager.get(sApplicationName, pSession.getId());
            }
		}
		
        sSecMan = pSession.getConfig().getProperty("/application/securitymanager/class");

        if (ismSecurity != null)
        {
            //check if the security manager was changed
            
            if (sSecMan == null || !sSecMan.equals(htSecManagerClass.get(sApplicationName)))
            {
                htSecManagerClass.remove(sApplicationName);

                if (htApplicationSecManager != null)
            	{
	                htApplicationSecManager.remove(sApplicationName);
            	}
            	
            	if (ghtSessionSecManager != null)
            	{
            		ghtSessionSecManager.remove(sApplicationName, pSession.getId());
            	}
                
                releaseSecurityManager(ismSecurity);
                ismSecurity = null;
            }
        }
		
		if (ismSecurity == null)
		{
			try
			{
				ismSecurity = createSecurityManager(pSession);

				if (cacheMode == CacheMode.Application)
				{
    				if (htApplicationSecManager == null)
    				{
    					htApplicationSecManager = new Hashtable<String, ISecurityManager>();
    				}
    				
    				htApplicationSecManager.put(sApplicationName, ismSecurity);
				}
				else
				{
                    if (ghtSessionSecManager == null)
                    {
                        ghtSessionSecManager = new GroupHashtable<String, Object, ISecurityManager>();
                    }
                    
                    ghtSessionSecManager.put(sApplicationName, pSession.getId(), ismSecurity);
				}
				
				//it is important to cache the configured class because it is possible that createSecurityManager returns a different class!
				htSecManagerClass.put(sApplicationName, sSecMan);
			}
			catch (Throwable th)
			{
				throw new Exception("Error during instantiation of security manager", th);
			}
		}
		
		return ismSecurity;
	}
	
	/**
	 * Gets the security manager from the cache.
	 * 
	 * @param pApplicationName the name of the application
	 * @return the security manager or <code>null</code> if no security manager was created for the given application
	 */
	final ISecurityManager[] getSecurityManagerFromCache(String pApplicationName)
	{
	    ArrayUtil<ISecurityManager> auSecMans = null;
	    
		if (htApplicationSecManager != null)
		{
		    auSecMans = new ArrayUtil<ISecurityManager>();
		    
		    ISecurityManager secman = htApplicationSecManager.get(pApplicationName); 
		    
		    if (secman != null)
		    {
		        auSecMans.add(secman);
		    }
		}

		if (ghtSessionSecManager != null)
		{
		    Hashtable<Object, ISecurityManager> htSecMan = ghtSessionSecManager.get(pApplicationName);
		    
		    if (htSecMan != null)
		    {
		        if (auSecMans == null)
		        {
		            auSecMans = new ArrayUtil<ISecurityManager>();
		        }
		        
		        for (Enumeration<ISecurityManager> enSec = htSecMan.elements(); enSec.hasMoreElements();)
		        {
		            auSecMans.add(enSec.nextElement());
		        }
		    }
		}
		
		if (auSecMans != null && !auSecMans.isEmpty())
		{
		    return auSecMans.toArray(new ISecurityManager[auSecMans.size()]);
		}
		
		return null;
	}
	
	/**
	 * Removes all security managers from the cache.
	 * 
	 * @param pApplicationName the application name
	 */
	final void removeSecurityManagersFromCache(String pApplicationName)
	{
		ArrayUtil<ISecurityManager> auManagers = new ArrayUtil<ISecurityManager>();
		
		if (htApplicationSecManager != null)
		{
			try
			{
				auManagers.add(htApplicationSecManager.remove(pApplicationName));
			}
			catch (Throwable th)
			{
				//ignore
			}
			
			htApplicationSecManager.clear();
		}
		
		if (ghtSessionSecManager != null)
		{
			try
			{
				auManagers.addAll(ghtSessionSecManager.removeKey(pApplicationName));
			}
			catch (Throwable th)
			{
				//ignore
			}
		}
		
		for (ISecurityManager manager : auManagers)
		{
			try
			{
				manager.release();
			}
			catch (Throwable th)
			{
				//ignore
			}
		}
	}
	
	/**
	 * Creates a new security manager instance for the given session.
	 * 
	 * @param pSession the session which needs a security manager
	 * @return the security manager
	 * @throws Exception if the class or default constructor was not found
	 */
	protected ISecurityManager createSecurityManager(ISession pSession) throws Exception
	{
		return createSecurityManager(null, pSession);
	}

	/**
	 * Creates a new security manager instance for the given session and class loader.
	 * 
	 * @param pLoader the class loader to load the security manager
	 * @param pSession the session which needs a security manager
	 * @return the security manager
	 * @throws Exception if the class or default constructor was not found
	 */
	protected ISecurityManager createSecurityManager(ClassLoader pLoader, ISession pSession) throws Exception
	{
		//forward this call because we need a separate method for subclasses, to over-write
		//the creation
		return AbstractSecurityManager.createSecurityManager(pLoader, pSession);
	}
	
	/**
	 * Releases the given security manager and catch all exceptions.
	 * 
	 * @param pManager the security manager to release
	 */
	protected void releaseSecurityManager(ISecurityManager pManager)
	{
		if (pManager != null)
		{
		    try
		    {
		        pManager.release();
		    }
		    catch (Throwable th)
		    {
		        log.debug(th);
		    }
		}
	}
	
	/**
	 * Gets the number of opened sessions.
	 * 
	 * @return session count
	 */
	final int getSessionCount()
	{
		synchronized (oSync)
		{
			return htSessions.size();
		}
	}
	
	/**
	 * Returns a copy of the session ids of all opened sessions.
	 * 
	 * @return list of opened sessions, guaranteed not null
	 */
	final ArrayUtil<Object> getSessionIds()
	{
		synchronized (oSync)
		{
			return new ArrayUtil<Object>(htSessions.keySet());
		}
	}
	
	/**
	 * Notifies all registered session listeners that a session was created.
	 *  
	 * @param pSession newly created session 
	 */
	private void fireSessionCreated(ISession pSession)
	{
		if (pSession == null)
		{
			return;
		}
		
		ArrayUtil<ISessionListener> auCopy;
		
		synchronized (auSessionListeners)
	    {
		    auCopy = new ArrayUtil<ISessionListener>(auSessionListeners);
	    }
		
    	for (int i = 0, anz = auCopy.size(); i < anz; i++)
    	{
    	    auCopy.get(i).sessionCreated(pSession);
    	}
	}
	
	/**
	 * Notifies all registered session listeners that a session was destroyed.
	 *  
	 * @param pSession destroyed session 
	 */
	private void fireSessionDestroyed(ISession pSession)
	{		
		if (pSession == null)
		{
			return;
		}
		
        ArrayUtil<ISessionListener> auCopy;
        
        synchronized (auSessionListeners)
        {
            auCopy = new ArrayUtil<ISessionListener>(auSessionListeners);
        }
        
        for (int i = 0, anz = auCopy.size(); i < anz; i++)
        {
			auCopy.get(i).sessionDestroyed(pSession);
		}
	}
	
	/**
	 * Notifies all registered failed session listeners that that session creation failed.
	 * 
	 * @param pClass the session class
	 * @param pCause the failure cause
	 * @param pProperties the properties used for session creation
	 */
	private void fireSessionFailed(Class<? extends ISession> pClass, Throwable pCause, ChangedHashtable<String, Object> pProperties)
	{
		ArrayUtil<IFailedSessionListener> auCopy;
		
		synchronized (auFailedSessionListeners)
	    {
		    auCopy = new ArrayUtil<IFailedSessionListener>(auFailedSessionListeners);
	    }
		
		FailedSessionEvent event = new FailedSessionEvent(pClass, pCause, pProperties);
		
    	for (int i = 0, anz = auCopy.size(); i < anz; i++)
    	{
    	    auCopy.get(i).sessionFailed(event);
    	}
	}
	
	/**
	 * Starts the session controller if it's not already started.
	 */
	private void startController()
	{
		if (ThreadHandler.isStopped(thController))
		{
			thController = ThreadHandler.start(new Controller());

			log.debug("Start Controller: ", thController.getName());
		}
	}
	
	/**
	 * Validate the activity of all opened sessions. If a session exceeds the
	 * maximum inactive time, it will be destroyed.
	 */
	private void validateSessions()
	{
		ISession session;
		
		//Get a clone of session ids to be independent of create and destroy calls
		ArrayUtil<Object> alSessionIds = getSessionIds();
		
		long lNow = System.currentTimeMillis();
		
		Object oId;
		
		
		//Multi-threading support
		ServerContext ctxt = ((Server)getServer()).createServerContext();
		
		try
		{
    		Record record = null;
    		
            for (int i = 0, anz = alSessionIds.size(); i < anz; i++)
    		{
    			oId = alSessionIds.get(i);
    			
    			//Thread safety of the Hashtable is sufficient, because we work with
    			//a clone of session ids
    			session = htSessions.get(oId);
    			
    			if (ctxt instanceof AbstractServerContext)
    			{
    			    ((AbstractServerContext)ctxt).setSession(session);
    			}
    			
    			//maybe already destroyed?
    			if (session != null)
    			{
    				if (session.isInactive(lNow) || !session.isAlive(lNow))
    				{
    				    //don't use setProperty, because we won't change the access time
    				    session.getProperties().put("expired", Boolean.TRUE);
    				    
		                record = ProtocolFactory.openRecord(ICategoryConstants.SESSION_MANAGER, ICommandConstants.SESSMAN_VALIDATE);

		                try
		                {
    		                if (record != null)
    		                {
    		                    record.addIdentifier("(thread)");
    		                }
        				    
                            log.info("Destroy invalid session: ", oId, ", Inactive: ", Boolean.valueOf(session.isInactive(lNow)), 
                                                                       ", Alive: ", Boolean.valueOf(session.isAlive(lNow)), 
                                                                       ", Now: ", Long.valueOf(lNow),
                                                                       ", Access time: ", Long.valueOf(session.getLastAccessTime()),
                                                                       ", Start time: ", Long.valueOf(session.getStartTime()));                 
        					
        					destroy(oId);
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
		                    CommonUtil.close(record);
		                }
    				}
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
	 * Sets the default properties for new sessions.
	 * 
	 * @param pProperties the initial client properties
	 * @param pRequest the request which started the session
	 * @return the initial session properties
	 * @throws Exception if an error occurs while setting properties
	 */
	private ChangedHashtable<String, Object> getInitialProperties(ChangedHashtable<String, Object> pProperties, 
			                                                      IRequest pRequest) throws Exception
	{
		ChangedHashtable<String, Object> chtProperties;
		
		//-----------------------------------------------------------
		// Take client properties
		//-----------------------------------------------------------

		if (pProperties != null)
		{
			chtProperties = pProperties;
		}
		else
		{
			chtProperties = new ChangedHashtable<String, Object>();
		}
		
		//-----------------------------------------------------------
		// Take server information
		//-----------------------------------------------------------
		
		chtProperties.put(IConnectionConstants.PREFIX_SERVER + "server_version", com.sibvisions.rad.IPackageSetup.SERVER_VERSION);
		chtProperties.put(IConnectionConstants.PREFIX_SERVER + "spec_version", javax.rad.IPackageSetup.SPEC_VERSION);

		try
		{
            InetAddress iaLocal = InetAddress.getLocalHost();
    		
    		chtProperties.put(IConnectionConstants.PREFIX_SERVER + "hostname", iaLocal.getHostName());
    		chtProperties.put(IConnectionConstants.PREFIX_SERVER + "address", iaLocal.getHostAddress());
		}
		catch (Exception e)
		{
		    //possible if resolving hostname fails 
		    log.error(e);
		}
		
		chtProperties.put(IConnectionConstants.CREATIONTIME_SERVER, new Date());

        //-----------------------------------------------------------
        // Server specific properties
        //-----------------------------------------------------------

		if (ProtocolFactory.getInstance() != null)
		{
		    chtProperties.put(IConnectionConstants.PREFIX_SERVER + "usingProtocol", "true");
		}
		
		//-----------------------------------------------------------
		// System Properties
		//-----------------------------------------------------------

		String sValue;
		String sPrefix = IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SYSPROP;
		
		for (int i = 0, anz = DefaultSessionManager.USED_SYSPROPS.length; i < anz; i++)
		{
			sValue = System.getProperty(DefaultSessionManager.USED_SYSPROPS[i]);
			
			if (sValue != null)
			{
				chtProperties.put(sPrefix + USED_SYSPROPS[i], sValue);
			}
		}

		//-----------------------------------------------------------
		// Request information
		//-----------------------------------------------------------

		if (pRequest != null)
		{
			Hashtable<String, Object> htAccessProp = pRequest.getProperties();
			
			if (htAccessProp != null)
			{
				sPrefix = IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_REQUEST;
				
				for (Map.Entry<String, Object> entry : htAccessProp.entrySet())
				{
					if (entry.getValue() instanceof String)
					{
						chtProperties.put(sPrefix + entry.getKey(), (String)entry.getValue());
					}
				}
			}
		}

		ServerContext ctxt = ServerContext.getCurrentInstance();
		
		if (ctxt != null)
		{
		    //add the systemIdentifier as non serializable property for later use in Controller thread (validateSessions - via AbstractServerContext.setSession)
		    String sIdentifier = ctxt.getSystemIdentifier();
		    
		    if (sIdentifier != null)
		    {
		        chtProperties.put(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_REQUEST + "systemIdentifier", new SystemIdentifier(sIdentifier));
		    }
		}
		
		return chtProperties;
	}
	
	/**
	 * Configures a session after it is created.
	 * 
	 * @param pSession the session
	 */
	protected void postCreateSession(ISession pSession)
	{
	}

	/**
	 * Configures a sub session after it is created.
	 * 
	 * @param pMaster the master session
	 * @param pSession the session
	 */
	protected void postCreateSubSession(ISession pMaster, ISession pSession)
	{
	}
	
	/**
	 * Handles a session creation error.
	 * 
	 * @param pCause the cause
	 * @see #createSession(IRequest, ISerializer, ChangedHashtable)
	 */
    protected void sessionCreationError(Throwable pCause)
    {
    }
	
    /**
     * Handles a sub session creation error.
     * 
     * @param pCause the cause
     * @see #createSubSession(IRequest, AbstractSession, ChangedHashtable)
     */
    protected void subSessionCreationError(Throwable pCause)
    {
    }
	
	/**
	 * Gets whether the given session is initializing.
	 * 
	 * @param pSession the session
	 * @return whether the session is initializing
	 */
	public boolean isInitializing(ISession pSession)
	{
	    //don't change access time
	    return pSession != null && pSession.getProperties().get(PROPERTY_INIT) == INIT;
	}

    /**
     * Gets whether a session is valid. This means that the session is in the list of currently opened sessions
     * and is alive and not invalid. 
     * 
     * @param pSessionOrId session identifier or session instance
     * @return <code>true</code> if the session is valid, <code>false</code> otherwise
     */
	public boolean isValid(Object pSessionOrId)
	{
        if (pSessionOrId != null)
        {
            Object oId;
            
            if (pSessionOrId instanceof ISession)
            {
                oId = ((ISession)pSessionOrId).getId();
            }
            else
            {
                oId = pSessionOrId;
            }

            ISession session;
            
            synchronized (oSync)
            {
                session = htSessions.get(oId);
            }
            
            if (session != null)
            {
                long lNow = System.currentTimeMillis();
                
                return !session.isInactive(lNow) && session.isAlive(lNow);
            }
        }
        
        return false;
	}
	
	/**
	 * Sets the "validate session" controller check interval.
	 * 
	 * @param pMilliSeconds the milliseconds between two checks. If the value is &lt;= 0,
	 *                      the default value will be used.
	 */
	public static void setControllerInterval(long pMilliSeconds)
	{
		if (pMilliSeconds <= 0)
		{
			lControllerInterval = CONTROLLER_INTERVAL;
		}
		else
		{
			lControllerInterval = pMilliSeconds;
		}
	}

	/**
	 * Gets the "validate session" controlleer check interval.
	 * 
	 * @return the milliseconds between two checks
	 */
	public static long getControllerInterval()
	{
		return lControllerInterval;
	}
	
	/**
	 * Gets the expected cache mode for the given session.
	 * 
	 * @param pSession the session
	 * @return the configured mode or the already detected mode from the session
	 */
	private CacheMode getCacheMode(ISession pSession)
	{
	    CacheMode cacheMode = (CacheMode)pSession.getProperty(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "cacheMode");
	    
	    if (cacheMode != null)
	    {
	        return cacheMode;
	    }

	    //detect the cacheMode for the given session
	    
	    IConfiguration config = pSession.getConfig();
	    
	    String sMode = config.getProperty("/application/securitymanager/cacheMode");
	    
	    try
	    {
	        cacheMode = CacheMode.valueOf(StringUtil.formatInitCap(sMode));
	    }
	    catch (Exception e)
	    {
	        //default cache mode is application
	        cacheMode = CacheMode.Application;
	    }
	    
	    //save cacheMode for later use, because it shouldn't be possible to change the cache mode while
	    //the application is in use
	    
	    if (pSession instanceof SubSession)
	    {
            ((SubSession)pSession).getMasterSession().getProperties().put(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "cacheMode", cacheMode);
	    }

	    pSession.getProperties().put(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "cacheMode", cacheMode);
	    
	    return cacheMode;
	}

	/**
	 * Gets the master session for the given session.
	 * 
	 * @param pSession a sub or master session
	 * @return the master session or <code>null</code> if given session was <code>null</code>
	 */
	ISession getMasterSession(ISession pSession)
	{
        //A Sub Session can only be created through a Master Session
        if (pSession instanceof SubSession)
        {
            return ((SubSession)pSession).getMasterSession();
        }
        else
        {
            return pSession;
        }
	}
	
	/**
     * Gets if at least one {@link IPushReceiver} was registered for the given session.
     * 
     * @param pSession the session
     * @return <code>true</code> if a receiver was registered
     */
    boolean hasPushReceiver(ISession pSession)
    {
        return htPushReceiver.get(pSession.getId()) != null;
    }
    
    /**
     * Clones the given properties and removes all sensitive data.
     * 
     * @param pProperties the properties with sensitive data
     * @return the properties without sensitive data
     */
    static Map<String, Object> secureProperties(ChangedHashtable<String, Object> pProperties)
    {
		//no passwords
		Map<String, Object> map = new HashMap<String, Object>(pProperties);
		map.remove(IConnectionConstants.PASSWORD);
		map.remove(IConnectionConstants.OLDPASSWORD);
		map.remove(IConnectionConstants.NEWPASSWORD);
		
		return map;
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>Controller</code> checks the session activity
	 * and kills zombies.
	 * 
	 * @author Ren� Jahn
	 */
	private final class Controller extends Thread
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			try
			{
				while (!ThreadHandler.isStopped())
				{
					validateSessions();

					//use current interval
					Thread.sleep(DefaultSessionManager.lControllerInterval);
				}
			}
			catch (InterruptedException ie)
			{
				//everything is fine
			}
			catch (Throwable th)
			{
				//doesn't matter, because the controller will be started when a new session will
				//be created
				log.error(th);
			}
			
			log.debug("Controller stopped: ", getName());
		}
		
	}	// Controller
	
	/**
	 * The <code>SystemIdentifier</code> is a non serializable property wrapper.
	 * 
	 * @author Ren� Jahn
	 */
	static final class SystemIdentifier 
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /** the system identifier. */
	    String systemIdentifier;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Sets the system identifier.
	     * 
	     * @param pIdentifier the identifier
	     */
	    private SystemIdentifier(String pIdentifier)
	    {
	        systemIdentifier = pIdentifier;
	    }
	    
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public String toString()
	    {
	        return systemIdentifier;
	    }
	    
	}  // SystemIdentifier
	
}	// DefaultSessionManager
