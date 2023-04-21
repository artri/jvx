/*
 * Copyright 2018 SIB Visions GmbH
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
 * 20.06.2018 - [JR] - creation
 * 21.02.2019 - [JR] - #1990: initial method/object name introduced
 */
package com.sibvisions.rad.server;

import java.util.Map;

import javax.rad.application.ILauncher;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.AbstractObjectProvider;
import javax.rad.server.ICloseableSession;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.InjectObject;
import javax.rad.server.SessionContext;
import javax.rad.server.security.IAccessChecker;

import com.sibvisions.rad.server.config.ServerZone;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractSessionContext</code> is a general {@link javax.rad.server.SessionContext} implementation.
 * 
 * @author René Jahn
 */
public abstract class AbstractSessionContext extends SessionContext
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the master session. */
    private WrappedSession wsessMaster;

    /** the associated session. */
	private WrappedSession wsessCurrent;
	
	/** the access checker. */
	private WrappedAccessChecker wachecker;
	
    /** the name of the object from which the method will be called. */
	private String sObjectName = null;
	
	/** the name of the method which will be called. */
	private String sMethodName = null;
	
	/** the original name of the object from the call, without any changes. */
	private String sInitialObjectName;

	/** the original method name from the call, without any chnages. */
	private String sInitialMethodName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SessionContextImpl</code> for a specific
	 * {@link ISession}.
	 * 
	 * @param pSession the associated session for this {@link javax.rad.server.SessionContext}
	 */
	protected AbstractSessionContext(ISession pSession)
	{
		if (pSession instanceof AbstractSession)
		{
			wsessCurrent = new WrappedSession((AbstractSession)pSession);
		}
		else if (pSession instanceof WrappedSession)
		{
			wsessCurrent = (WrappedSession)pSession;
		}
		else if (pSession instanceof DirectServerSession)
		{
			wsessCurrent = new WrappedSession((AbstractSession)((DirectServerSession)pSession).getSession());
		}
		else
		{
			throw new IllegalArgumentException("Given session doesn't offer an 'AbstractSession'!");
		}
		
		setCurrentInstance(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy()
	{
		setCurrentInstance(null);

		wsessMaster = null;
		wsessCurrent = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISession getSession()
	{
		return wsessCurrent;
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public ISession getMasterSession()
    {
        if (wsessMaster != null)
        {
            return wsessMaster;
        }
        else
        {
            if (wsessCurrent == null)
            {
                return null;
            }
            
            AbstractSession sessorig = wsessCurrent.session;
                    
            if (sessorig instanceof MasterSession)
            {
                wsessMaster = wsessCurrent;
                
                return wsessMaster;
            }
            else if (sessorig instanceof SubSession)
            {
                wsessMaster = new WrappedSession(((SubSession)sessorig).getMasterSession());
                
                return wsessMaster;
            }
        }
        
        return null;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConfiguration getSessionConfig()
	{
		if (wsessCurrent == null)
		{
			return null;
		}
		
		return wsessCurrent.getConfig();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConfiguration getServerConfig()
	{
		if (wsessCurrent == null)
		{
			return null;
		}
		
        ServerZone zone = wsessCurrent.session.getApplicationZone().getServerZone();
        
        if (zone != null)
        {
            return zone.getConfig();
        }
        
        return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConnection getServerConnection()
	{
		if (wsessCurrent == null)
		{
			return null;
		}
		
		return new DirectServerConnection((IDirectServer)wsessCurrent.session.getSessionManager().getServer());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObjectName()
	{
		return sObjectName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMethodName()
	{
		return sMethodName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InjectObject putObject(InjectObject pObject)
	{
		return wsessCurrent.session.putObject(pObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InjectObject removeObject(InjectObject pObject)
	{
		return wsessCurrent.session.removeObject(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InjectObject getObject(String pName)
	{
		return wsessCurrent.session.getObject(pName);
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ICloseableSession createSubSession(String pLifeCycleName, Map<String, Object> pProperties) throws Throwable
    {
        ChangedHashtable<String, Object> chtProperties = DirectServerSession.createProperties(pProperties);
        chtProperties.put(IConnectionConstants.LIFECYCLENAME, pLifeCycleName);
        
        Object oSessionId = Server.getInstance().createSubSession(getMasterSession().getId(), chtProperties);
        
        return new WrappedSession(Server.getInstance().getSessionManager().get(oSessionId), true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAccessChecker getAccessChecker()
    {
    	if (wachecker == null)
    	{
    		wachecker = new WrappedAccessChecker(wsessCurrent.session.getAccessController());
    	}
    		
    	return wachecker;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getClassLoader()
    {
    	try
    	{
    		Object oLCO = wsessCurrent.session.get(null);
    		
    		if (oLCO != null)
    		{
    			return oLCO.getClass().getClassLoader();
    		}
    		else
    		{
    			AbstractObjectProvider prov = wsessCurrent.session.getObjectProvider();
    			
    			ClassLoader loader = prov.getClassLoader(wsessCurrent.session);
    			
    			if (loader == null)
    			{
    				return prov.getClass().getClassLoader();
    			}
    			else
    			{
    				return loader;
    			}
    		}
    	}
    	catch (Throwable th)
    	{
    		throw new RuntimeException("ClassLoader was not found!");
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the name of the current object from which the method will be called.
	 * 
	 * @param pObjectName the object name or <code>null</code> if the object is unknown
	 * @see #getObjectName()
	 */
	protected void setObjectName(String pObjectName)
	{
		sObjectName = pObjectName;
	}
	
	/**
	 * Sets the name of the method which will be called.
	 * 
	 * @param pMethodName the method name or <code>null</code> if the method is unknown
	 * @see #getMethodName()
	 */
	protected void setMethodName(String pMethodName)
	{
		sMethodName = pMethodName;
	}
	
	/**
	 * Gets the initial object name. If no specific initial object name is set, the current
	 * object name will be returned.
	 * 
	 * @return the initial object name
	 * @see #getObjectName()
	 */
	public String getInitialObjectName()
	{
		return sInitialObjectName == null ? sObjectName : sInitialObjectName;
	}
	
	/**
	 * Sets the specific initial object name.
	 * 
	 * @param pInitialObjectName the object name
	 */
	protected void setInitialObjectName(String pInitialObjectName)
	{
		sInitialObjectName = pInitialObjectName;
	}

	/**
	 * Gets the initial method name. If no specific initial method name is set, the current
	 * method name will be returned.
	 * 
	 * @return the initial method name
	 * @see #getMethodName()
	 */
	public String getInitialMethodName()
	{
		return sInitialMethodName == null ? sMethodName : sInitialMethodName;
	}
	
	/**
	 * Sets the specific initial method name.
	 * 
	 * @param pInitialMethodName the method name
	 */
	protected void setInitialMethodName(String pInitialMethodName)
	{
		sInitialMethodName = pInitialMethodName;
	}
	
	/**
	 * Gets the name of the environment, from the master session. 
	 * 
	 * @return the environment name or <code>null</code> if not set
	 */
	public String getEnvironmentName()
	{
		//this call doesn't change the access time
		return (String)getMasterSession().getProperties().get(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT);		
	}
	
	/**
	 * Gets the name of the environment, from the master session, without parameter. 
	 * The parameter is defined with <code>name:parameter</code>.
	 * 
	 * @return the environment name without parameter or <code>null</code> if not set
	 */
	public String getSimpleEnvironmentName()
	{
	    String sName = getEnvironmentName();
	    
        if (!StringUtil.isEmpty(sName))
	    {
    	    int iPos = sName.indexOf(":");
    	    
    	    if (iPos >= 0)
    	    {
    	        sName = sName.substring(0, iPos);
    	    }
	    }
	    
	    return sName;
	}	
	
	/**
	 * Gets the parameter from the the environment name, from the master session. 
	 * The parameter is defined with <code>name:parameter</code>.
	 * 
	 * @return the name without parameter or <code>null</code> if not set
	 */
	public String getEnvironmentNameParameter()
	{
	    String sName = getEnvironmentName();
	    
        if (!StringUtil.isEmpty(sName))
	    {
    	    int iPos = sName.indexOf(":");
    	    
    	    if (iPos >= 0)
    	    {
    	        sName = sName.substring(iPos + 1);
    	    }
	    }
	    
	    return sName;
	}	
	
}	// SessionContextImpl
