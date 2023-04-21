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
 * 10.02.2009 - [JR] - getClassNameMapping: added full qualified class name to mapping [BUGFIX]
 * 11.02.2009 - [JR] - createObject: AExchangeManager included
 * 12.02.2009 - [JR] - TYPE_SESSION splitted to TYPE_SUBSESSION and TYPE_MASTERSESSION
 * 10.05.2009 - [JR] - getObject: support for dot separated object names
 * 18.11.2009 - [JR] - #33: putObject implemented
 * 27.01.2010 - [JR] - invoke implemented
 * 15.02.2010 - [JR] - createInstance: checked GenericBean instance
 * 22.02.2010 - [JR] - #67: getObject: Map support
 *                   - getObject: object not found throws an exception [BUGFIX]
 * 05.03.2010 - [JR] - invoke: user friendly message when the object is null
 * 23.03.2010 - [JR] - #103: getObject now sets the object name and the method
 * 24.03.2010 - [JR] - #105: putObject: dot notation support
 *                   - Map instead of Bean
 * 22.12.2010 - [JR] - initApplicationObject: check ClassNotFoundException to allow missing application objects
 * 25.12.2010 - [JR] - removed final from class 
 * 01.03.2011 - [JR] - initSessionObject: injectObjects after caching the object 
 *                                        (allows LCO access for inject objects constructor)
 * 02.03.2011 - [JR] - #297: updateSessionObject (put object even if it exists -> support object changing)
 * 25.05.2011 - [JR] - getSessionObjectInternal implemented  
 * 26.05.2011 - [JR] - #363: ILifeCycleObject handling  
 * 21.11.2012 - [JR] - #535: check object and method access via IObjectAccessProvider
 * 23.08.2013 - [JR] - #774: check if session is available after method invocation
 * 29.01.2014 - [JR] - #935: session isolation feature    
 * 11.05.2014 - [JR] - #1033: Replacement annotation checked (object access)      
 * 09.09.2014 - [JR] - #1105: implicit LCO support     
 * 12.09.2014 - [JR] - #1106: strict isolation support     
 * 16.10.2014 - [JR] - #1144: Replacement annotation checked (action calls)      
 * 18.12.2014 - [JR] - #1217: removed object id 
 * 09.03.2015 - [JR] - #965: Used existing annotations
 * 29.05.2015 - [JR] - #1397: notify callhandler about object creation
 * 21.02.2019 - [JR] - #1990: initial method/object name set
 */
package com.sibvisions.rad.server;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ISession;
import javax.rad.server.InjectObject;
import javax.rad.server.SessionContext;
import javax.rad.server.UnknownObjectException;
import javax.rad.server.event.ISessionListener;
import javax.rad.type.bean.Bean;

import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.rad.server.annotation.NotAccessible;
import com.sibvisions.rad.server.annotation.Replacement;
import com.sibvisions.rad.server.annotation.StrictIsolation;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.rad.server.security.IObjectAccessController;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DefaultObjectProvider</code> manages the remote accessible objects. It compiles
 * source files and offers always the current object.
 * 
 * @author René Jahn
 */
public class DefaultObjectProvider extends AbstractObjectProvider
                                   implements ISessionListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the logger instance. */
    private ILogger log = LoggerFactory.getInstance(getClass());
    
    /** the cache for application life cycle objects. */
    private Hashtable<String, Map> htApplicationObjects = null;
    
    /** the cache for session life cycle objects. */
    private Hashtable<Object, Map> htSessionObjects = null;
    
    /** the cache for session life cycle objects. */
    private Hashtable<Object, List<ISessionListener>> htSessionListeners = null;
    
    /** the cache for session life cycle objects. */
    private Hashtable<Object, List<ILifeCycleObjectListener>> htLifeCycleObjectListeners = null;

    /** the object access controller. */
    private IObjectAccessController oaController = null;
    
    /** cache of construct methods. */
    private WeakHashMap<Class<?>, SoftReference<Method>> whmpConstruct;
    
    /** cache of pre-destroy methods. */
    private WeakHashMap<Class<?>, List<SoftReference<Method>>> whmpDestroy;
    
	/** list of all registered lifecycle object event listeners. */
	private ArrayUtil<ILifeCycleObjectListener> auLifeCycleObjectListeners = new ArrayUtil<ILifeCycleObjectListener>();


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates an instance of <code>AbstractObjectProvider</code>.
     * 
     * @param pServer communication server
     */
    protected DefaultObjectProvider(Server pServer)
    {
        super(pServer);
        
        pServer.getSessionManager().addSessionListener(this);

        //#535
        try
        {
            String sObjectAccess = Configuration.getServerZone().getProperty("/server/objectprovider/accesscontroller");
        
            if (!StringUtil.isEmpty(sObjectAccess))
            {
                oaController = (IObjectAccessController)Reflective.construct(sObjectAccess);

                log.debug("Use ", sObjectAccess, " as ObjectAccessController");
            }
        }
        catch (Throwable th)
        {
            log.debug("Can't use configured ObjectAccessController!", th);
            
            oaController = null;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void sessionCreated(ISession pSession)
    {
    	//TODO this (custom session listener) code should be moved to DefaultSessionManager if all dependencies are refactored -> requires some month
    	initSessionListener(pSession);
    }

    /**
     * {@inheritDoc}
     */
    public void sessionDestroyed(ISession pSession)
    {
        destroySession(pSession);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getObject(ISession pSession, String pObjectName) throws Throwable
    {
        synchronized (pSession)
        {
            Map mapLifeCycle = getSessionObject(pSession);
            
            //#1105
            if (mapLifeCycle instanceof ImplicitLifeCycleObject)
            {
                throw new ClassNotFoundException("Missing instance name");              
            }
            
            //search the object name within the life-cycle object
            
            if (pObjectName == null || pObjectName.trim().length() == 0)
            {
                if (mapLifeCycle == null)
                {
                    throw new UnknownObjectException(pSession.getLifeCycleName());
                }
                
                //an action call doesn't need an object name
                return mapLifeCycle;
            }
            else
            {
                //search the desired object
                ArrayUtil<String> auNames = StringUtil.separateList(pObjectName, ".", true);
                
                //get the callable object
                Object oInvoke = mapLifeCycle;
                Object oResult;
                String sObjectName;
                
                StringBuilder sbCurrentObjectName = new StringBuilder();
                
                AbstractSessionContext context = ((AbstractSessionContext)SessionContext.getCurrentInstance());
                
                String sOriginalMethodName = context.getMethodName();
                
                context.setInitialMethodName(sOriginalMethodName);
                context.setInitialObjectName(context.getObjectName());
                context.setMethodName(null);
                
                IObjectAccessController controller = getObjectAccessController();
                
                for (int i = 0, anz = auNames.size(); i < anz; i++)
                {
                    sObjectName = auNames.get(i);
                    
                    if (sbCurrentObjectName.length() > 0)
                    {
                        sbCurrentObjectName.append(".");
                    }
                    
                    sbCurrentObjectName.append(sObjectName);
                    
                    context.setObjectName(sbCurrentObjectName.toString());
                    
                    if (i == anz - 1)
                    {
                        context.setMethodName(sOriginalMethodName);
                    }
    
                    if (oInvoke == null)
                    {
                        throw new UnknownObjectException(sbCurrentObjectName.toString());
                    }
                    
                    try
                    {
                        String sMethodName = StringUtil.formatMethodName("get", sObjectName);
                        
                        Method met = Reflective.getMethod(oInvoke.getClass(), sMethodName); 

                        if (i > 0)
                        {
                            oInvoke = invokeSubMethod(pSession, met, oInvoke, sbCurrentObjectName);
                        }
                        else
                        {
                            oInvoke = invokeMethod(pSession, met, oInvoke, sbCurrentObjectName);
                        }
                    }
                    catch (NoSuchMethodException nsme)
                    {
                        boolean bFound = false;
                        
                        //first object: check replacements
                        if (i == 0)
                        {
                            Replacement replace;
                        
                            Method[] methods = oInvoke.getClass().getMethods();
                            
                            if (methods != null)
                            {
                                for (int j = 0; j < methods.length && !bFound; j++)
                                {                                    
                                    if (methods[j].getParameterTypes().length == 0 && methods[j].getReturnType() != Void.TYPE)
                                    {
                                        replace = methods[j].getAnnotation(Replacement.class);
                                        
                                        if (replace != null)
                                        {
                                            if (sObjectName.equals(replace.name()))
                                            {
                                                if (i > 0)
                                                {
                                                    oInvoke = invokeSubMethod(pSession, methods[j], oInvoke, sbCurrentObjectName);
                                                }
                                                else
                                                {
                                                    oInvoke = invokeMethod(pSession, methods[j], oInvoke, sbCurrentObjectName);
                                                }
                                                
                                                bFound = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!bFound)
                        {
                            if (oInvoke instanceof Map)
                            {
                                oResult = ((Map)oInvoke).get(sObjectName);
                                
                                if (oResult == null && !((Map)oInvoke).containsKey(sObjectName))
                                {
                                    throw new UnknownObjectException(sObjectName);
                                }
                                
                                //use the result!
                                oInvoke = oResult;
                            }
                            else
                            {
                                throw new UnknownObjectException(sObjectName, nsme);
                            }
                        }
                    }
                    
                    //#535
                    if (controller != null && !controller.isObjectAccessAllowed(this, pSession, mapLifeCycle, sbCurrentObjectName.toString(), oInvoke))
                    {
                        throw new SecurityException("Access to '" + sObjectName + "' is denied!");
                    }
                }
                
                //don't check null, because a getXXX method exists and returns null (maybe expected)!
                return oInvoke;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object putObject(ISession pSession, String pObjectName, Object pObject) throws Throwable
    {
        if (pObjectName == null)
        {
            return null;
        }
        
        synchronized (pSession)
        {
            Object objSession;
            
            String sObjectName;
            
            int iPos = pObjectName.lastIndexOf(".");
            
            if (iPos > 0)
            {
            	AbstractSessionContext context = ((AbstractSessionContext)SessionContext.getCurrentInstance()); 
                
                //cache the old method name because the getObject method changes the method name, and in that special
                //case it's not correct to set a method name!
                String sOldMethodName = context.getMethodName();
                
                context.setInitialMethodName(sOldMethodName);
                context.setInitialObjectName(context.getObjectName());
                context.setMethodName(null);
                
                objSession = getObject(pSession, pObjectName.substring(0, iPos));
                
                context.setMethodName(sOldMethodName);
                
                sObjectName = pObjectName.substring(iPos + 1);
            }
            else
            {
                objSession = getSessionObject(pSession);
                 
                sObjectName = pObjectName;
            }
            
            try
            {
                return Reflective.call(objSession, StringUtil.formatMethodName("set", sObjectName), pObject);
            }
            catch (NoSuchMethodException nsme)
            {
                if (objSession instanceof Map)
                {
                    return ((Map)objSession).put(sObjectName, pObject);
                }
                else
                {
                    throw new RuntimeException("Can't set object '" + pObjectName + "'", nsme);
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object invoke(ISession pSession, String pObjectName, String pMethodName, Object... pParams) throws Throwable
    {
        Object obj = getObject(pSession, pObjectName);
        
        try
        {
            //it's possible that a null object will be returned as valid object, but that's not a valid call!
            if (obj == null)
            {
                throw new RuntimeException("The Object '" + pObjectName + "' is known but 'null' was returned!");
            }
            
            //#535
            IObjectAccessController controller = getObjectAccessController();
            
            if (controller != null && !controller.isMethodInvocationAllowed(this, pSession, pObjectName, obj, pMethodName, pParams))
            {
                throw new SecurityException("Invocation of '" + pMethodName + "' is not allowed!");
            }
            
            try
            {
                if (obj instanceof GenericBean)
                {
                    //try to call the action generic
                    return ((GenericBean)obj).invoke(pMethodName, pParams);
                }
                else
                {
                	//call the action by name, and all methods because we are not a generic bean
//                  
//                  Method method = Reflective.getMethodForCall(obj.getClass(), false, pMethodName, pParams);
//
//                	if (method == null)
//                	{
//                		throw new NoSuchMethodException(Reflective.getMethodDeclaration(obj.getClass(), pMethodName, pParams));
//                	}
//                	
//                	if (!AccessHelper.isAccessible(method))
//                	{
//                		throw new SecurityException("Access to " + pMethodName + " denied!");
//                		
//                	}
//                	
//                	return Reflective.call(obj, method, pParams);
                    
                    //call the action by name, and all methods because we are not a generic bean
                    return Reflective.call(obj, pMethodName, pParams);
                }
            }
            catch (NoSuchMethodException nsme)
            {
                //check replacements
                Replacement replace;
            
                Method[] methods = obj.getClass().getDeclaredMethods();
                
                if (methods != null)
                {
                    for (int i = 0; i < methods.length; i++)
                    {                                    
                        replace = methods[i].getAnnotation(Replacement.class);
                        
                        if (replace != null)
                        {
                            if (pMethodName.equals(replace.name()))
                            {
                                return Reflective.call(obj, true, methods[i].getName(), pParams);
                            }
                        }
                    }
                }
                
                throw nsme;
            }
        }
        finally
        {
            DefaultSessionManager sman = getServer().getSessionManager(); 
            
            //if this call was an async call and the session is not longer valid, ensure that all objects are removed
            //if session is initializing, don't destroy the created objects because it's possible that the security manager
            //did create the objects during authentication
            if (!sman.isAvailable(pSession) && !sman.isInitializing(pSession))
            {
                destroySession(pSession);
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public Server getServer()
    {
        return (Server)super.getServer();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the life-cycle object for a session. 
     * 
     * @param pSession the accessing session 
     * @return the life-cycle object for the session
     * @throws Exception if the life-cycle object can not be created
     */
    protected Map getSessionObject(ISession pSession) throws Exception
    {
        synchronized (pSession)
        {
            Map mapSession = null;
            
            AbstractSession session = (AbstractSession)pSession;
            
            if (htSessionObjects != null)
            {
                mapSession = htSessionObjects.get(session.getId());
            }
            
            if (mapSession == null)
            {
                mapSession = initSessionObject(session, true);
            }
            else
            {
                updateSessionObject(session, mapSession);
            }
            
            return mapSession;
        }
    }
    
    /**
     * Gets whether this object provider contains least one object for the given session.
     * 
     * @param pSession the session
     * @return <code>true</code> if this provider contains at least one object for <code>pSession</code>, 
     *         <code>false</code> otherwise
     */
    public boolean hasObject(ISession pSession)
    {
        synchronized (pSession)
        {
            return htSessionObjects != null && htSessionObjects.get(pSession.getId()) != null;
        }
    }
    
    /**
     * Creates and initializes a life-cycle object for a session.
     * 
     * @param pSession the accessing session 
     * @param pNotifyCallHandler <code>true</code> to notify Session' call handler
     * @return the life-cycle object for the session
     * @throws Exception if the life-cycle object can not be created
     */
    private Map initSessionObject(AbstractSession pSession, boolean pNotifyCallHandler) throws Exception
    {
        synchronized (pSession)
        {
            Map map;
            
            if (pSession instanceof SubSession)
            {
                map = createInstance(pSession, pSession.getLifeCycleName());

                if (!isIsolated(map))
                {
                    AbstractSession sessParent = ((SubSession)pSession).getMasterSession();
                    
                    Map mapMaster = null;
                    
                    synchronized (sessParent)
                    {
                        if (htSessionObjects != null)
                        {
                            mapMaster = htSessionObjects.get(sessParent.getId());
                        }
                        
                        //If the Master-session didn't execute commands -> the life-cycle object is not
                        //available. In that case it's important to create the object.
                        //But be careful! We need a SessionContext for the MasterSession, otherwise we have
                        //a SessionContext for the SubSession!
                        if (mapMaster == null)
                        {               
                            //object and method name are not known. Of course, we could check if a SessionContext
                            //is available, but it's better to give the life-cycle object always consistent values!
                            SessionContext context = sessParent.createSessionContext(null, null);
                            
                            try
                            {
                                //DON'T notify call handler, because this would fire events and following postObjectCreation would
                                //fire same events again
                                mapMaster = initSessionObject(sessParent, false);
                            }
                            finally
                            {
                                context.release();
                            }
                        }
                    }

                    setParent(pSession, map, mapMaster);
                }
            }
            else
            {
                String sLCOName = pSession.getLifeCycleName();
                
                //#1105
                if (!StringUtil.isEmpty(sLCOName))
                {
                    map = createInstance(pSession, sLCOName);
                }
                else
                {
                    map = new ImplicitLifeCycleObject();
                }

                if (!isIsolated(map))
                {
                    setParent(pSession, map, getApplicationObject(pSession));
                }               
            }

            if (htSessionObjects == null)
            {
                htSessionObjects = new Hashtable<Object, Map>();
            }
            
            htSessionObjects.put(pSession.getId(), map);

            if (pNotifyCallHandler)
            {
                pSession.getCallHandler().postObjectCreation();
            }
            
            //inject after put to Hashtable -> that makes it possible that injected objects have access to
            //the life-cycle object in the constructor
            injectObjects(pSession, map);
            
            fireObjectCreated(pSession, map, false);

            return map;
        }
    }
    
    /**
     * Updates an existing life-cycle object with current session information.
     * 
     * @param pSession the session
     * @param pMap the associated life-cycle object
     * @throws Exception if the session access fails
     */
    private void updateSessionObject(AbstractSession pSession, Map pMap) throws Exception
    {
        List<Entry<String, InjectObject>> liChanges = pSession.getChangedInjectObjects();
        
        if (liChanges != null)
        {
            Entry<String, InjectObject> entry;
            
            InjectObject injobj;
            
            for (int i = 0, anz = liChanges.size(); i < anz; i++)
            {
                entry = liChanges.get(i);

                injobj = entry.getValue();

                if (injobj == null)
                {
                    //if the object was not an injected object -> no problem because it will be createad again
                    pMap.remove(entry.getKey());
                }
                else
                {
                    pMap.put(injobj.getName(), injobj.getObject());
                }
            }
        }
    }
    
    /**
     * Gets the life-cycle object for an application.
     * 
     * @param pSession the accessing session 
     * @return the life-cycle object for the application
     * @throws Exception if the life-cycle object can not be created
     */
    protected synchronized Map getApplicationObject(AbstractSession pSession) throws Exception
    {
        Map mapApplication = null;

        String sApplicationName = pSession.getApplicationName(); 
        
        if (htApplicationObjects != null)
        {
            mapApplication = htApplicationObjects.get(sApplicationName);
        }
        
        if (mapApplication == null)
        {
            mapApplication = initApplicationObject(pSession);
        }
        
        return mapApplication;
    }
    
    /**
     * Creates and initializes a life-cycle object for an application.
     * 
     * @param pSession the accessing session
     * @return the new application object
     * @throws Exception if the life-cycle object can not be created
     */
    private Map initApplicationObject(AbstractSession pSession) throws Exception
    {
        String sApplication = pSession.getApplicationZone().getProperty("/application/lifecycle/application");
        
        if (!StringUtil.isEmpty(sApplication))
        {
            try
            {
                Map mapApplication = createInstance(pSession, sApplication);
                
                if (htApplicationObjects == null)
                {
                    htApplicationObjects = new Hashtable<String, Map>();
                }
                
                htApplicationObjects.put(pSession.getApplicationName(), mapApplication);
                
                fireObjectCreated(pSession, mapApplication, true);
                
                return mapApplication;
            }
            catch (ClassNotFoundException cnfe)
            {
                log.error("Application object '" + sApplication + "' was not found!", cnfe);

                //don't create a empty Map, to support application loading for new Master sessions
                //e.g. if we change the configuration
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates a new {@link Map} instance with a specific class name and, if possible, sets a parent object.
     * 
     * @param pSession the calling session
     * @param pInstanceName the full qualified class name for the instance
     * @return the new instance
     * @throws Exception if the instance can not be created
     */
    protected Map createInstance(AbstractSession pSession, String pInstanceName) throws Exception
    {
        if (pInstanceName != null)
        {
            Object objInstance;

            ClassLoader loader = getClassLoader(pSession);
            
            Class<?> clazz;

            try
            {
	            if (loader == null)
	            {
	                clazz = Class.forName(pInstanceName);
	            }
	            else
	            {
	                clazz = Class.forName(pInstanceName, true, loader);
	            }
            }
            catch (ClassNotFoundException cnfe)
            {
            	//support for Replacement
            	IAccessController acc = pSession.getAccessController();
           	
            	if (acc != null)
            	{
	            	String sClassName = acc.find(loader, pInstanceName);
	            	
	            	if (sClassName != null)
	            	{
	    	            if (loader == null)
	    	            {
	    	                clazz = Class.forName(sClassName);
	    	            }
	    	            else
	    	            {
	    	                clazz = Class.forName(sClassName, true, loader);
	    	            }
	            	}
	            	else
	            	{
	            		throw cnfe;
	            	}
            	}
            	else
            	{
            		throw cnfe;
            	}
            }
            
            objInstance = clazz.newInstance();            
            
            if (!(objInstance instanceof Map))
            {
                throw new RuntimeException("The lifecycle object '" + pInstanceName + "' has to be a Map!");
            }

            Method metConstruct = null;
            
            //means: class doesn't have a method with @PostConstruct
            boolean bNullMethod = false;
            
            if (whmpConstruct != null)
            {
            	SoftReference<Method> sref = whmpConstruct.get(clazz);
                
                if (sref == null)
                {
                    bNullMethod = whmpConstruct.containsKey(clazz);
                }
                else
                {
                    metConstruct = sref.get();
                }            
            }
            
            if (!bNullMethod)
            {
                if (metConstruct == null)
                {
                	searchSpecialMethods(objInstance);
                	
                	SoftReference<Method> sref = whmpConstruct.get(clazz); 
                	
                	if (sref != null)
                	{
                    	metConstruct = sref.get(); 
                	}
                }
                
                //Found -> execute
                if (metConstruct != null)
                {
                    try
                    {
                        Reflective.invoke(objInstance, metConstruct);
                    }
                    catch (Throwable th)
                    {
                        Throwable thr = th;
                        
                        //otherwise we'll see the "wrapped" exception as well
                        if (thr instanceof InvocationTargetException)
                        {
                            thr = ((InvocationTargetException)thr).getTargetException();
                        }
                        
                        if (thr instanceof Exception)
                        {
                            throw (Exception)thr;
                        }
                        
                        throw new RuntimeException(thr);
                    }
                }
            }
            
            return (Map)objInstance;
        }
        else
        {
            throw new ClassNotFoundException("Missing instance name");
        }
    }
       
    /**
     * Search special methods, e.g. annotated with {@link PostConstruct} and {@link PreDestroy}.
     * 
     * @param pInstance the instance to use
     */
    private void searchSpecialMethods(Object pInstance)
    {
    	Class<?> clazz = pInstance.getClass();
    	
    	Method metConstruct = null;
    	
    	List<SoftReference<Method>> liDestroy = new ArrayUtil<SoftReference<Method>>(); 
        
        for (Method method : clazz.getDeclaredMethods())
        {
            if (method.getParameterTypes().length == 0 
                && method.getReturnType().equals(Void.TYPE) 
                && !Modifier.isStatic(method.getModifiers()))
            {
                if (method.isAnnotationPresent(PostConstruct.class))
                {
                    if (metConstruct != null)
                    {
                        throw new IllegalStateException("It's not allowed to define @PostConstrut on more than one method!");
                    }
                    
                    metConstruct = method;
                }
                
                if (method.isAnnotationPresent(PreDestroy.class))
                {
                    liDestroy.add(new SoftReference<Method>(method));
                }
            }
        }
        
        //@PostConstruct CACHE
        
        if (whmpConstruct == null)
        {
            whmpConstruct = new WeakHashMap<Class<?>, SoftReference<Method>>();
        }
         
        if (metConstruct == null)
        {
            whmpConstruct.put(clazz, null);
        }
        else
        {
        	whmpConstruct.put(clazz, new SoftReference(metConstruct));
        }
        
        //@PreDestroy CACHE
        
        if (whmpDestroy == null)
        {
        	whmpDestroy = new WeakHashMap<Class<?>, List<SoftReference<Method>>>();
        }
        
        if (liDestroy.isEmpty())
        {
            whmpDestroy.put(clazz, null);
        }
        else
        {
            whmpDestroy.put(clazz, liDestroy);
        }
    }
    
    /**
     * Gets whether the given object is an isolated object. This means that {@link StrictIsolation}
     * annotation was added.
     * 
     * @param pInstance the object to check
     * @return <code>true</code> if isolated, <code>false</code> otherwise
     */
    protected boolean isIsolated(Object pInstance)
    {
        return pInstance != null && isIsolated(pInstance.getClass());
    }
    
    /**
     * Gets whether the given session is isolated. This means that the LCO contains the {@link StrictIsolation}
     * annotation.
     * 
     * @param pSession the session to check
     * @return <code>true</code> if isolated, <code>false</code> otherwise
     */
    public static boolean isIsolated(AbstractSession pSession)
    {
        AbstractObjectProvider prov = pSession.getObjectProvider();

        if (prov instanceof DefaultObjectProvider)
        {
            DefaultObjectProvider dprov = (DefaultObjectProvider)prov;
            
            synchronized (pSession)
            {
                if (dprov.htSessionObjects != null)
                {
                    Object obj = dprov.htSessionObjects.get(pSession.getId());
                    
                    //LCO of session was created -> check the LCO 
                    if (obj != null)
                    {
                        return dprov.isIsolated(obj);
                    }
                }
                
                //LCO NOT initialized (don't create it)
                
                String sLCOName = pSession.getLifeCycleName();
                
                if (sLCOName != null)
                {
                    Class<?> clazz;
                    
                    try
                    {
                        ClassLoader loader = dprov.getClassLoader(pSession);
                        
                        if (loader == null)
                        {
                            clazz = Class.forName(sLCOName);
                        }
                        else
                        {
                            clazz = Class.forName(sLCOName, true, loader);
                        }
                    }
                    catch (Exception e)
                    {
                        dprov.log.error(e);
                        
                        return false;
                    }
                    
                    return dprov.isIsolated(clazz);
                }
            }
        }
        
        return false;
    }
    
    /**
     * Gets whether the class has {@link StrictIsolation} annotation.
     * 
     * @param pClass the class
     * @return <code>true</code> if class is "isolated", <code>false</code> otherwise
     */
    private boolean isIsolated(Class<?> pClass)
    {
        return pClass != null && pClass.getAnnotation(StrictIsolation.class) != null;
    }
    
    /**
     * Sets the parent for a LCO. This doesn't work if given instance is not a {@link GenericBean} or given parent
     * is not a {@link Bean}.
     * 
     * @param pSession the current session
     * @param pInstance the LCO
     * @param pParent the parent
     */
    protected void setParent(ISession pSession, Map pInstance, Map pParent)
    {
        //---------------------------------------------------------------------
        // Use the "members" of the parent 
        //---------------------------------------------------------------------
        
        if (pParent != null)
        {
            if (pInstance instanceof GenericBean && pParent instanceof Bean)
            {
                ((GenericBean)pInstance).setParent((Bean)pParent);
            }
            else
            {
                log.info("Can't set parent for: ", pSession.getLifeCycleName(), " because the life-cycle object is not instance of GenericBean");
            }
        }
    }
    
    /**
     * Injects the available objects from the session context into the sessions life-cycle object.
     * 
     * @param pSession the accessing session
     * @param pLifeCycleObject the life-cycle object
     * @throws Exception if the injection configuration is invalid
     */
    private void injectObjects(AbstractSession pSession, Map pLifeCycleObject) throws Exception
    {
        String sName;
        
        InjectObject injobj;
        
        for (Enumeration<InjectObject> en = pSession.getInjectObjects(); en.hasMoreElements();)
        {
            injobj = en.nextElement();
        
            sName = injobj.getName();
            
            pLifeCycleObject.put(sName, injobj.getObject());
        }
    }

    /**
     * Remove injected objects, if needed.
     * 
     * @param pSession the session
     * @param pLifeCycleObject the life-cycle object
     * @throws Exception if accessing injected objects failed
     */
    private void removeInjectedObjects(AbstractSession pSession, Map pLifeCycleObject) throws Exception
    {
        String sName;
        
        InjectObject injobj;
        
        for (Enumeration<InjectObject> en = pSession.getInjectObjects(); en.hasMoreElements();)
        {
            injobj = en.nextElement();
        
            if (injobj.isExternal())
            {
                sName = injobj.getName();
                
                pLifeCycleObject.remove(sName);
            }
        }
    }
    
    /**
     * Sets the object access controller.
     * 
     * @param pController the controller
     */
    public void setObjectAccessController(IObjectAccessController pController)
    {
        oaController = pController;
    }
    
    /**
     * Gets the object access controller.
     * 
     * @return the controller
     */
    public IObjectAccessController getObjectAccessController()
    {
        return oaController;
    }

    /**
     * Destroys all session listeners.
     * 
     * @param pSession the session to use
     */
    final void destroySessionListener(ISession pSession)
    {
        if (htSessionListeners != null)
        {
            List<ISessionListener> liCachedListener = htSessionListeners.remove(pSession.getId());
            
            if (liCachedListener != null)
            {
                for (ISessionListener sesslis : liCachedListener)
                {
                    try
                    {
                        sesslis.sessionDestroyed(pSession);
                    }
                    catch (Throwable th)
                    {
                        log.debug(th);
                    }
                }
            }
        }
    }

    /**
     * Destroys all lifecycle object listeners.
     * 
     * @param pSession the session to use
     * @param pLCO the lifecycle object
     */
    final void destroyLifeCycleObjectListener(ISession pSession, Map pLCO)
    {
        if (htLifeCycleObjectListeners != null)
        {
            List<ILifeCycleObjectListener> liCachedListener = htLifeCycleObjectListeners.remove(pSession.getId());
            
            if (liCachedListener != null)
            {
                for (ILifeCycleObjectListener sesslis : liCachedListener)
                {
                    try
                    {
                        sesslis.objectDestroyed(pSession, pLCO);
                    }
                    catch (Throwable th)
                    {
                        log.debug(th);
                    }
                }
            }
        }
    }
    
    /**
     * Removes and destroys the life-cycle object for the given session.
     * 
     * @param pSession the session
     */
    final void destroySession(ISession pSession)
    {
    	destroySessionListener(pSession);
    	
    	Map mpLco;
    	
        if (htSessionObjects != null)
        {
            mpLco = htSessionObjects.remove(pSession.getId());
        }
        else
        {
        	mpLco = null;
        }
    	
        SessionContext context = SessionContext.getCurrentInstance();
        
        boolean bNewContext;
        
        if (context == null)
        {
            context = ((AbstractSession)pSession).createSessionContext(null, "destroy");
            
            bNewContext = true;
        }
        else
        {
            bNewContext = false;
        }

        try
        {
	        fireObjectDestroyed(pSession, mpLco);
	            
	        if (mpLco != null)
	        {
	
	            if (whmpDestroy != null)
	            {
	            	List<SoftReference<Method>> liDestroy = whmpDestroy.get(mpLco.getClass());
	                
	            	Method met;
	
	            	if (liDestroy != null)
	            	{
	                    //If at least one reference is null -> search again
	                    for (int i = 0, cnt = liDestroy.size(); i < cnt; i++)
	                    {
	                    	met = liDestroy.get(i).get();
	                    	
	                    	if (met == null)
	                    	{
	                    		searchSpecialMethods(mpLco);
	                    		
	                    		liDestroy = whmpDestroy.get(mpLco.getClass());
	                    		
	                    		i = cnt;
	                    	}
	                    }                		
	            	}
	            	
	                if (liDestroy != null)
	                {
	                    //Invoke @PreDestroy methods
	                    for (int i = 0, cnt = liDestroy.size(); i < cnt; i++)
	                    {
	                    	met = liDestroy.get(i).get();
	                    	
	                        if (met != null)
	                        {
	                            try
	                            {
	                                Reflective.invoke(mpLco, met);
	                            }
	                            catch (Throwable th)
	                            {
	                                log.debug(th);
	                            }
	                        }
	                    }
	                }
	            }
	            
	            if (mpLco instanceof ILifeCycleObject)
	            {
	                try
	                {
	                    //don't close injected objects
	                    removeInjectedObjects((AbstractSession)pSession, mpLco);
	
	                    ((ILifeCycleObject)mpLco).destroy();
	                }
	                catch (Throwable th)
	                {
	                    log.debug(th);
	                }
	            }
	            else if (!mpLco.isEmpty())
	            {
	                for (Iterator<Entry<Object, Object>> it = mpLco.entrySet().iterator(); it.hasNext();)
	                {
	                    CommonUtil.close(it.next().getValue());
	                }
	            }
	        }
        }
    	finally
    	{
    		if (bNewContext)
    		{
    			context.release();
    		}
    	}
    }

    /**
     * Invokes the given method and checks if it's not accesible. A method is not accessible if the {@link NotAccessible}
     * annotation is present.
     * 
     * @param pSession the session to use
     * @param pMethod the method to call
     * @param pObject the object that contains the method
     * @param pObjectName the object name (only for exception handling)
     * @return the result of the method call
     * @throws Throwable if method call fails
     * @throws SecurityException if access to method was explicitely denied
     */
    private Object invokeMethod(ISession pSession, Method pMethod, Object pObject, StringBuilder pObjectName) throws Throwable
    {
    	if (AccessHelper.isNotAccesible(pMethod))
        {
            throw new SecurityException("Access to " + pObjectName + " denied!");
        }
        
        try
        {
            return pMethod.invoke(pObject);
        }
        catch (InvocationTargetException ite)
        {
            throw ite.getCause();
        }
    }

    /**
     * Invokes the given method and checks if it's accessible. A method is accessible if the {@link Accessible}
     * annotation is present and the environment check is valid.
     * 
     * @param pSession the session to use
     * @param pMethod the method to call
     * @param pObject the object that contains the method
     * @param pObjectName the object name (only for exception handling)
     * @return the result of the method call
     * @throws Exception if method call fails
     * @throws SecurityException if access to method was not explicitly granted
     */
    private Object invokeSubMethod(ISession pSession, Method pMethod, Object pObject, StringBuilder pObjectName) throws Exception
    {
        if (!AccessHelper.isAccessible(pMethod))
        {
            throw new SecurityException("Access to " + pObjectName + " denied!");
        }
        
        return pMethod.invoke(pObject);
    }
    
    /**
     * Initializes the session listener.
     * 
     * @param pSession the session
     */
    private void initSessionListener(ISession pSession)
    {
        Iterator<ISessionListener> itListeners = null;

        if (pSession instanceof SubSession)
    	{
        	if (htSessionListeners != null)
        	{
	    		List<ISessionListener> liCachedListener = htSessionListeners.get(((SubSession)pSession).getMasterSession().getId());
	    		
	    		if (liCachedListener != null)
	    		{
	    			itListeners = liCachedListener.iterator();
	    		}
        	}
    	}
    	else 
    	{
	        ClassLoader loader = getClassLoader(pSession);
	        
	        try
	        {
	            if (loader == null)
	            {
	                itListeners = ServiceLoader.load(ISessionListener.class, getClass().getClassLoader()).iterator();
	            }
	            else
	            {
	                itListeners = ServiceLoader.load(ISessionListener.class, loader).iterator();
	            }
	        }
	        catch (Throwable th)
	        {
	            log.error("Loading session listener with ServiceLoader failed!", th);
	        }
    	}
    	
        if (itListeners != null)
        {
        	try
        	{
	        	while (itListeners.hasNext()) 
		        {
		            ISessionListener sesslis = itListeners.next();
		            
		            try
		            {
		                sesslis.sessionCreated(pSession);
		                
		                if (htSessionListeners == null)
		                {
		                    htSessionListeners = new Hashtable<Object, List<ISessionListener>>();
		                }
		
		                List<ISessionListener> liCachedListener = htSessionListeners.get(pSession.getId());
		                
		                if (liCachedListener == null)
		                {
		                    liCachedListener = new ArrayList<ISessionListener>();
		                    
		                    htSessionListeners.put(pSession.getId(), liCachedListener);
		                }
		                
		                liCachedListener.add(sesslis);
		            }
		            catch (Throwable th)
		            {
		                log.debug(th);
		            }
		        }
	        }
	        catch (Throwable th)
	        {
	            log.error("Iterate session listeners failed!", th);
	        }
        }
    }
    
    /**
     * Initializes the lifecycle object listener.
     * 
     * @param pSession the session
     * @param pLCO the lifecycle object
     * @param pApplication whether the lifecycle object is an application or session object
     */
    private void initLifeCycleObjectListener(ISession pSession, Map pLCO, boolean pApplication)
    {
        Iterator<ILifeCycleObjectListener> itListeners = null;
        
    	//default: cache all listeners
        boolean bNewListener = true;

        if (htLifeCycleObjectListeners != null)
    	{
    		List<ILifeCycleObjectListener> liCachedListener = htLifeCycleObjectListeners.get(pSession.getId());
    		
    		if (liCachedListener != null)
    		{
    			//listeners available -> don't cache again
    			bNewListener = false;
    			
    			itListeners = liCachedListener.iterator();
    		}
    	}

        if (itListeners == null)
        {
	        if (pSession instanceof SubSession)
	    	{
	        	if (htLifeCycleObjectListeners != null)
	        	{
		    		List<ILifeCycleObjectListener> liCachedListener = htLifeCycleObjectListeners.get(((SubSession)pSession).getMasterSession().getId());
		    		
		    		if (liCachedListener != null)
		    		{
		    			itListeners = liCachedListener.iterator();
		    		}
	        	}
	    	}
	    	else 
	    	{
		        ClassLoader loader = getClassLoader(pSession);
		        
		        try
		        {
		            if (loader == null)
		            {
		                itListeners = ServiceLoader.load(ILifeCycleObjectListener.class, getClass().getClassLoader()).iterator();
		            }
		            else
		            {
		                itListeners = ServiceLoader.load(ILifeCycleObjectListener.class, loader).iterator();
		            }
		        }
		        catch (Throwable th)
		        {
		            log.error("Loading lifecycle object listener with ServiceLoader failed!", th);
		        }
	    	}
        }
    	
        if (itListeners != null)
        {
        	try
        	{
	        	while (itListeners.hasNext()) 
		        {
	        		ILifeCycleObjectListener lcolis = itListeners.next();
		            
		            try
		            {
		            	if (pApplication)
		            	{
		            		lcolis.applicationObjectCreated(pSession, pLCO);
		            	}
		            	else
		            	{
		            		lcolis.sessionObjectCreated(pSession, pLCO);
		            	}
		                
		            	
		            	if (bNewListener)
		            	{
			                if (htLifeCycleObjectListeners == null)
			                {
			                	htLifeCycleObjectListeners = new Hashtable<Object, List<ILifeCycleObjectListener>>();
			                }
			
			                List<ILifeCycleObjectListener> liCachedListener = htLifeCycleObjectListeners.get(pSession.getId());
			                
			                if (liCachedListener == null)
			                {
			                    liCachedListener = new ArrayList<ILifeCycleObjectListener>();
			                    
			                    htLifeCycleObjectListeners.put(pSession.getId(), liCachedListener);
			                }
			                
			                liCachedListener.add(lcolis);
		            	}
		            }
		            catch (Throwable th)
		            {
		                log.debug(th);
		            }
		        }
	        }
	        catch (Throwable th)
	        {
	            log.error("Iterate lifecycle object listeners failed!", th);
	        }
        }
    }
    
	/**
	 * Adds a life-cycle object listener.
	 * 
	 * @param pListener the listener
	 */
	public final void addObjectListener(ILifeCycleObjectListener pListener)
	{
	    synchronized (auLifeCycleObjectListeners)
	    {
	    	auLifeCycleObjectListeners.add(pListener);
	    }
	}
	
	/**
	 * Removes a life-cycle object listener.
	 * 
	 * @param pListener the listener
	 */
	public final void removeObjectListener(ILifeCycleObjectListener pListener)
	{
        synchronized (auLifeCycleObjectListeners)
        {
        	auLifeCycleObjectListeners.remove(pListener);
		}
	}
	
	/**
	 * Gets all registered life-cycle object listeners.
	 * 
	 * @return the listeners
	 */
	public final ILifeCycleObjectListener[] getObjectListeners()
	{
        synchronized (auLifeCycleObjectListeners)
        {
        	ILifeCycleObjectListener[] islResult = new ILifeCycleObjectListener[auLifeCycleObjectListeners.size()];
			
			return auLifeCycleObjectListeners.toArray(islResult);
		}
	}

	/**
	 * Notifies all registered lifecycle object listeners that a session lifecycle object was created.
	 *  
	 * @param pSession destroyed session 
	 * @param pLCO the lifecycle object
	 * @param pApplication whether an application object or a session object was created
	 */
	private void fireObjectCreated(ISession pSession, Map pLCO, boolean pApplication)
	{		
		if (pSession == null)
		{
			return;
		}
		
        SessionContext context = SessionContext.getCurrentInstance();
        
        boolean bNewContext;
        
        if (context == null)
        {
            context = ((AbstractSession)pSession).createSessionContext(null, "create");
            
            bNewContext = true;
        }
        else
        {
            bNewContext = false;
        }

        try
        {
	        initLifeCycleObjectListener(pSession, pLCO, pApplication);
			
	        ArrayUtil<ILifeCycleObjectListener> auCopy;
	        
	        synchronized (auLifeCycleObjectListeners)
	        {
	            auCopy = new ArrayUtil<ILifeCycleObjectListener>(auLifeCycleObjectListeners);
	        }
	        
	        ILifeCycleObjectListener list;
	        
	        for (int i = 0, anz = auCopy.size(); i < anz; i++)
	        {
				list = auCopy.get(i);
				
				if (pApplication)
				{
					list.applicationObjectCreated(pSession, pLCO);
				}
				else
				{
					list.sessionObjectCreated(pSession, pLCO);
				}
			}
        }
        finally
        {
        	if (bNewContext)
        	{
        		context.release();
        	}
        }
	}
	
	/**
	 * Notifies all registered lifecycle object listeners that a lifecycle object was destroyed.
	 *  
	 * @param pSession destroyed session 
	 * @param pLCO the lifecycle object
	 */
	private void fireObjectDestroyed(ISession pSession, Map pLCO)
	{		
		if (pSession == null)
		{
			return;
		}
		
    	destroyLifeCycleObjectListener(pSession, pLCO);
		
        ArrayUtil<ILifeCycleObjectListener> auCopy;
        
        synchronized (auLifeCycleObjectListeners)
        {
            auCopy = new ArrayUtil<ILifeCycleObjectListener>(auLifeCycleObjectListeners);
        }
        
        for (int i = 0, anz = auCopy.size(); i < anz; i++)
        {
			auCopy.get(i).objectDestroyed(pSession, pLCO);
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * A marker class. It's inherited from GenericBean because of Parent hierarchy!
     * 
     * @author René Jahn
     */
    public static final class ImplicitLifeCycleObject extends GenericBean
    {
    }   // ImplicitLifeCycleObject
    
}   // DefaultObjectProvider
