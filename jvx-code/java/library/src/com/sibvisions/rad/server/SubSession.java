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
 * 18.11.2008 - [JR] - setLastAccesTime/setLastAliveTime: made init safe
 * 11.02.2009 - [JR] - changed properties to <String, Object> instead of <String, String>
 * 17.08.2009 - [JR] - setExecuting overwritten [BUGFIX]
 * 23.02.2010 - [JR] - #18: constructor: set inactive interval direct 
 * 07.06.2010 - [JR] - #49: access control support
 * 18.11.2010 - [JR] - #206: getApplicationZone made protected
 * 13.12.2011 - [JR] - #523: set application/username/password as property
 * 28.05.2015 - [JR] - #1397: callhandler integration 
 * 21.11.2017 - [JR] - #1856: lock mechanism
 */
package com.sibvisions.rad.server;

import java.util.HashMap;
import java.util.Map;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ResultObject;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>SubSession</code> is a server side session which
 * will be started when a sub session will be opened on the client.
 * 
 * @author René Jahn
 */
final class SubSession extends AbstractSession
                       implements ISubSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the master session. */
	private MasterSession msMaster;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SubSession</code>.
	 * 
	 * @param pMaster the master session
	 * @param pProperties the initial session properties
	 * @throws Exception if the configuration of the application is invalid
	 */
	SubSession(MasterSession pMaster, ChangedHashtable<String, Object> pProperties) throws Exception
	{
		super(pMaster.getSessionManager(), pProperties);

		try
		{
    		this.msMaster = pMaster;
    		
    		msMaster.addSubSession(this);
    		
    		msMaster.setLastAccessTime(getLastAccessTime());
    
            Record record = ProtocolFactory.openRecord(ICategoryConstants.SESSION, ICommandConstants.SESSION_CHECK_ACCESS);
    
            try
            {
        		//Isolated means no access to other sessions!
                if (DefaultObjectProvider.isIsolated(msMaster))
                {
                    throw new SecurityException("It's not possible to create a sub session from an isolated session!");
                }
    		
                checkAccess();
            }
            finally
            {
                CommonUtil.close(record);
            }
            
            record = ProtocolFactory.openRecord(ICategoryConstants.SESSION, ICommandConstants.SESSION_CONFIGURATION);
    		
            try
            {
        		//#523
        		//set properties into the session -> transfers the values to the client too 
        		setUserName(pMaster.getUserName());
        		setPassword(pMaster.getPassword());
        		setApplicationName(pMaster.getApplicationName());
        		
                //-1 means: use the timeout of the master session. The sub session will time-out after the master session
        		//          is timed-out
    		    initMaxInactiveInterval(pProperties, pMaster.getApplicationZone(), "subsession", -1);
        		
        		//Use the serializer from the master session!
        		setSerializer(msMaster.getSerializer());
        		
        		if (record != null)
        		{
        			//no password logging
        			Map<String, Object> map = new HashMap<String, Object>(chtProperties);
        			map.remove(IConnectionConstants.PASSWORD);
        			
        			record.setParameter(map);
        		}
            }
            finally
            {
                CommonUtil.close(record);
            }
		}
		catch (Exception e)
		{
		    uninit();

		    throw e;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected ApplicationZone getApplicationZone()
	{
		return msMaster.getApplicationZone();
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public IAccessController getAccessController()
	{
		return msMaster.getAccessController();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public final void setNewPassword(String pOldPassword, String pNewPassword) throws Exception
    {
        msMaster.setNewPassword(pOldPassword, pNewPassword);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IConfiguration getConfig()
	{
		return msMaster.getConfig();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MasterSession getMasterSession()
	{
		return msMaster;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		msMaster.removeSubSession(this);
		
		super.finalize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInactive(long pAccessTime)
	{
		int iInterval = getMaxInactiveInterval();
		
		//If the Sub Session has its own timeout, that is shorter than the timeout of the
		//Master session, then this will be used! Otherwise the timeout of the master session
		//will be used.
		//If the Sub Session has a longer timeout than the Master Session, then the client
		//connection will be notified through the alive check or through the master connection
		//listener. That's not a problem!)
		if (iInterval > 0 && iInterval < msMaster.getMaxInactiveInterval())
		{
			return super.isInactive(pAccessTime);
		}
		else
		{
			return msMaster.isInactive(pAccessTime);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastAccessTime(long pLastAccessTime)
	{
		super.setLastAccessTime(pLastAccessTime);

		//called from the super class constructor, before the master session will be set!
		if (msMaster != null)
		{
			msMaster.setLastAccessTime(pLastAccessTime);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastAliveTime(long pLastAliveTime)
	{
		//called from the super class constructor, before the master session will be set!
		if (msMaster != null)
		{
			//The sub session has no alive -> uses the master session alive (BTW only MasterConnection has an Alive thread)
			msMaster.setLastAliveTime(pLastAliveTime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastAliveTime()
	{
		//The sub session has no alive -> uses the master session alive (BTW only MasterConnection has an Alive thread)
		return msMaster.getLastAliveTime();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addCallBackResult(ResultObject pResult)
	{
		//Callbacks will be handled through the master session
		msMaster.addCallBackResult(pResult);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	ArrayUtil<ResultObject> removeCallBackResults()
	{
		//Callbacks will be handled through the master session
		return msMaster.removeCallBackResults();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setExecuting(boolean pExecuting)
	{
		//sync the execution with the master session, otherwise long sync calls let the master session
		//expire because the alive will not set from the client if the IConnection handles requests
		//syncronized!!!
		msMaster.setExecuting(pExecuting);
		
		super.setExecuting(pExecuting);
	}
	
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Record openRecord(String pCommand, Object... pParameter)
    {
        Record record = super.openRecord(pCommand, pParameter);
        
        if (record != null)
        {
            record.addIdentifier(msMaster.getId(), 0);
            record.addIdentifier("MASTER", 1);
        }
        
        return record;
    }    	

    /**
     * {@inheritDoc}
     */
    public void lock()
    {
        msMaster.lock();
        
        super.lock();
    }

    /**
     * Unlocks the session.
     * 
     * @see #lock()
     */
    public void unlock()
    {
        super.unlock();
        
        msMaster.unlock();
    }    
	
}	// SubSession
