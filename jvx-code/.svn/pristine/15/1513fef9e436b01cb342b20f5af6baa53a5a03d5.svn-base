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
 * 01.02.2009 - [JR] - overwritten createConnectionProperties -> don't set the properties 
 *                     in the constructor
 * 04.02.2009 - [JR] - reopen overwritten
 * 09.04.2009 - [JR] - Alive: changed sleep time and used the up-to-date lastCallTime value
 * 10.04.2009 - [JR] - SubConnectionListener implemented for sub connections
 * 27.04.2009 - [JR] - replaced logging
 * 12.05.2009 - [JR] - createSubConnection: checked if the connection is open
 * 11.06.2009 - [JR] - Alive: used Reflective.invokeLater to use the UI thread for notifications!
 * 04.10.2009 - [JR] - setNewPassword: old password as parameter
 * 24.10.2009 - [JR] - setAliveInterval with negative value supported -> stops alive check [FEATURE]
 * 23.02.2010 - [JR] - #18: setAliveInterval, createConnectionProperties: alive property is now numeric and not String
 * 18.05.2010 - [JR] - create an instance of Reflective to ensure the correct UI thread! 
 * 04.08.2012 - [JR] - close(): close sub connections when connection is closed
 * 17.02.2013 - [JR] - alive check handling improved setAliveCheckEnabled
 * 27.02.2013 - [JR] - getAliveInterval implemented
 * 11.07.2013 - [JR] - #728: isCalling used for alive check
 */
package javax.rad.remote;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.remote.event.CallBackResultEvent;
import javax.rad.remote.event.CallErrorEvent;
import javax.rad.remote.event.CallEvent;
import javax.rad.remote.event.ConnectionEvent;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionListener;
import javax.rad.remote.event.PropertyEvent;
import javax.rad.util.UIInvoker;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>MasterConnection</code> provides sub connections and
 * an alive check for all known connections.
 * 
 * @author René Jahn
 * @see IConnection
 */
public class MasterConnection extends AbstractConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** time between two alive checks. */
	private static final long ALIVECHECK_DELAY = 30000L;


    /** the reference to the correct UI thread. */
    private UIInvoker uiInvoker = new UIInvoker();
	
	/** the internal listener for sub connections. */
	private SubConnectionListener sclSubCon = null;
	
	/** the list of opened sub connections. */
	private ArrayUtil<WeakReference<SubConnection>> auSubConnections = null;
	
	/** thread for set alive on the server. */
	private Thread 	thAliveCheck 		= null;
	
	/** object for synchronized access to the sub connection references. */
	private Object 	oSyncSubConnections	= new Object();

	/** the internal callback result listener. */
	private InternalCallBackResultListener cbrListener;
	
	/** the delay between two alive calls. */
	private long 	lAliveInterval 		= ALIVECHECK_DELAY;
	
	/** a flag that indicates that the alive-check is active. */
	private boolean bAliveCheckIsActive = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AppliationConnection</code> with an
	 * <code>IConnection</code> implementation.
	 * 
	 * @param pConnection the <code>IConnection</code> implementation
	 */
	public MasterConnection(IConnection pConnection)
	{
		super(pConnection);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openConnection() throws Throwable
	{
        connection.open(coninf);
        
        if (cbrListener == null)
        {
            cbrListener = new InternalCallBackResultListener();
        }
        
        connection.addCallBackResultListener(cbrListener);
        
        startAliveCheck();
	}

    /**
     * {@inheritDoc}
     */
    @Override
	protected UIInvoker getUIInvoker()
	{
	    return uiInvoker;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws Throwable
	{
		ArrayUtil<WeakReference<SubConnection>> auSubCache;
		
		synchronized (oSyncSubConnections)
		{
    		if (auSubConnections != null)
    		{
    		    auSubCache = auSubConnections.clone();
    		}
    		else
    		{
    			auSubCache = null;
    		}
    		
    		//reset cached properties (because alive check tries to access the list)
    		auSubConnections = null;
		}

		stopAliveCheck();

        //it's sufficient to close the master because all sub connections will be closed
        //automatically on the server

		//close sub connections silent (without call)
        if (auSubCache != null)
        {
            SubConnection subcon;
            
            for (int i = 0, anz = auSubCache.size(); i < anz; i++)
            {
                subcon = auSubCache.get(i).get();
                
                if (subcon != null)
                {
                    subcon.removeConnectionListener(sclSubCon);
                    subcon.close(false);
                }
            }
        }
        
		//close the connection
		super.close();
		
		if (cbrListener != null)
		{
		    getConnection().removeCallBackResultListener(cbrListener);
		}
	}	

    /**
     * Reopens this and all sub connections.
     * 
     * @param pProperties additional properties before reopening the connection
     * @throws Throwable if an error occurs while opening this or any sub connection
     */
    @Override
    public void reopen(Map<String, Object> pProperties) throws Throwable
    {
        reopen(pProperties, false);
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    protected ChangedHashtable<String, Object> createConnectionProperties()
    {
    	ChangedHashtable<String, Object> chtProperties = super.createConnectionProperties();

    	//Be careful with settings, because the super constructor calls this method before this was instantiated.
    	//This means that the members have jvm default values!!!
    	chtProperties.put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(ALIVECHECK_DELAY));
    	
    	return chtProperties;
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Reopens this connection.
     * 
     * @param pCloseSubConnections <code>true</code> to close sub connections and don't reopen them, 
     *                             <code>false</code> if sub connections should be reopened
     * @throws Throwable if an error occurs while reopening this or any sub connection
     */
    public void reopen(boolean pCloseSubConnections) throws Throwable
    {
        reopen(null, pCloseSubConnections);
    }
    
    /**
     * Reopens this connection.
     * 
     * @param pProperties additional properties before ropening the connection
     * @param pCloseSubConnections <code>true</code> to close sub connections and don't reopen them, 
     *                             <code>false</code> if sub connections should be reopened
     * @throws Throwable if an error occurs while reopening this or any sub connection
     */
    public void reopen(Map<String, Object> pProperties, boolean pCloseSubConnections) throws Throwable
    {
        stopAliveCheck();
        
        List<SubConnection> liSubCons = getSubConnections();
        
        if (liSubCons != null)
        {
            SubConnection subcon;
            
            //remove listener, otherwise it could fire a call error notification with the new master-connection
            //because the subconnection is still a children. The sub connection should do nothing with the old
            //connection because the id has changed!
            for (int i = 0, anz = liSubCons.size(); i < anz; i++)
            {
                subcon = liSubCons.get(i);
                subcon.removeConnectionListener(sclSubCon);
                
                if (pCloseSubConnections)
                {
                    //close client-side only
                    subcon.close(false);
                }
            }
        }
        
        super.reopen(pProperties);
        
        startAliveCheck();
        
        //no custom close is needed because
        if (!pCloseSubConnections)
        {
            if (liSubCons != null)
            {
                SubConnection subcon;
                
                for (int i = 0, anz = liSubCons.size(); i < anz; i++)
                {
                    subcon = liSubCons.get(i);
                    
                    subcon.reopen();
                }
            }
        }
        else
        {
            //already closed via super.reopen, so it's enough to reset the cache 
            synchronized (oSyncSubConnections)
            {
                auSubConnections = null;
            }
        }
    }
	
	/**
	 * Returns a new sub connection.
	 * 
	 * @param pLifeCycleName the name of the life-cycle object for the sub connection
	 * @return the new <code>SubConnection</code> for the sub connection
	 * @throws Throwable if it is not possible to create the new sub connection
	 */
	public SubConnection createSubConnection(String pLifeCycleName) throws Throwable
	{
		if (!isOpen())
		{
			throw new IllegalStateException("The connection is not open!");
		}
		
		try
		{
			SubConnection subcon = new SubConnection(this);
			
			subcon.setLifeCycleName(pLifeCycleName);
			
			startAliveCheck();
			
			return subcon;
		}
		catch (Throwable th)
		{
		    throw handleCallError(th);
		}
	}
	
	/**
	 * Adds a sub connection to the internal list of sub connections.
	 * 
	 * @param pSubConnection the sub connection
	 */
	protected void addSubConnection(SubConnection pSubConnection)
	{
		synchronized (oSyncSubConnections)
		{
			if (pSubConnection != null)
			{
				if (auSubConnections == null)
				{
					auSubConnections = new ArrayUtil<WeakReference<SubConnection>>();
					auSubConnections.add(new WeakReference<SubConnection>(pSubConnection));
				}
				else if (auSubConnections.indexOf(pSubConnection) < 0)
				{
					auSubConnections.add(new WeakReference<SubConnection>(pSubConnection));
				}
			}
		}
		
		if (pSubConnection != null)
		{
            if (sclSubCon == null)
            {
                sclSubCon = new SubConnectionListener();
            }
            
            pSubConnection.addConnectionListener(sclSubCon);
		}
	}

	/**
	 * Removes a sub connection from the internal list of sub connections.
	 * 
	 * @param pSubConnection the sub connection
	 */
	protected void removeSubConnection(SubConnection pSubConnection)
	{
		synchronized (oSyncSubConnections)
		{
			if (auSubConnections != null && pSubConnection != null)
			{
		        pSubConnection.removeConnectionListener(sclSubCon);

			    auSubConnections.remove(pSubConnection);
				
				if (auSubConnections.size() == 0)
				{
					auSubConnections = null;
				}
			}
		}
	}
	
	/**
	 * Returns a cloned list of sub connections. Don't reference
	 * to the connections to avoid problems with memory and gc.
	 * 
	 * @return all known sub connections or null if there is no sub connection
	 */
	public List<SubConnection> getSubConnections()
	{
		synchronized (oSyncSubConnections)
		{
			if (auSubConnections != null)
			{
				ArrayUtil<SubConnection> auResult = null;
				
				WeakReference<SubConnection> wrSub;
				
				SubConnection scSub;

				
				for (int i = 0, anz = auSubConnections.size(); i < anz;) 
				{
					wrSub = auSubConnections.get(i);
					
					scSub = wrSub.get();
					
					if (scSub == null)
					{
						auSubConnections.remove(i);
						anz--;
					}
					else
					{
						if (auResult == null)
						{
							auResult = new ArrayUtil<SubConnection>();
						}
						
						auResult.add(scSub);
						
						i++;
					}
				}
				
				if (auSubConnections.size() == 0)
				{
					auSubConnections = null;
				}
				
				return auResult;			
			}
			else
			{
				return null;
			}
		}
	}
	
    /**
     * Starts the alive checker thread if it's not already running.
     */
    private void startAliveCheck()
    {
    	if (lAliveInterval >= 0 && ThreadHandler.isStopped(thAliveCheck) && isOpen())
    	{
    		thAliveCheck = ThreadHandler.start(new Alive());
    	}
    }
    
    /**
     * Stops the alive checker thread if it's running.
     */
    private void stopAliveCheck()
    {
    	thAliveCheck = ThreadHandler.stop(thAliveCheck);
    }
    
    /**
     * Sets the interval of the alive thread. The default value
     * is {@link #ALIVECHECK_DELAY}.
     * 
     * @param pInterval interval in millis. If the value is smaller
     *        or equal than one second, the default value will be set.
     *        An interval with a negative value will disable the alive check
     */
    public void setAliveInterval(long pInterval)
    {
    	if (pInterval >= 0 && pInterval <= 1000L)
    	{
    		lAliveInterval = MasterConnection.ALIVECHECK_DELAY;
    		
    		coninf.getProperties().put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(lAliveInterval));

    		startAliveCheck();
    	}
    	else
    	{
    		lAliveInterval = pInterval;
    		
    		if (lAliveInterval < 0)
    		{
    			stopAliveCheck();
    			
        		coninf.getProperties().put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(lAliveInterval));
    		}
    		else
    		{
        		coninf.getProperties().put(IConnectionConstants.ALIVEINTERVAL, Long.valueOf(lAliveInterval));

        		startAliveCheck();
    		}
    	}
    }
    
    /**
     * Gets the current alive interval.
     * 
     * @return the interval
     */
    public long getAliveInterval()
    {
    	Long lValue = (Long)coninf.getProperties().get(IConnectionConstants.ALIVEINTERVAL);
    	
    	if (lValue != null)
    	{
    		return lValue.longValue();
    	}
    	else
    	{
    		return lAliveInterval;
    	}
    }
    
    /**
     * Sets the new password for the current connection.
     * 
     * @param pOldPassword the old password
     * @param pNewPassword the new password
     * @throws Throwable if the password can not be changed
     */
    public void setNewPassword(String pOldPassword, String pNewPassword) throws Throwable
    {
    	coninf.getProperties().put(IConnectionConstants.OLDPASSWORD, pOldPassword);
    	coninf.getProperties().put(IConnectionConstants.NEWPASSWORD, pNewPassword);
    	
    	//sets the password immediately or implicit when the connection will be opened
    	if (isOpen())
    	{
    	    long lStart = System.currentTimeMillis();
    	
    	    try
    	    {
    	        connection.setNewPassword(coninf, pOldPassword, pNewPassword);
    	        
    	        logCommunication(lStart, "setNewPassword", null);
    	    }
    	    catch (Throwable th)
    	    {
    	        logCommunication(lStart, "setNewPassword", th);
    	        
    	        throw th;
    	    }
    	}
    }
    
    /**
     * Sends the alive information to the server and checks all sub connections if they
     * are still valid.
     *  
     * @throws Throwable if the alive check fails
     */
    public void setAndCheckAlive() throws Throwable
    {
		try
		{
		    if (bAliveCheckIsActive)
		    {
		        return;
		    }
		    
			bAliveCheckIsActive = true;
		
			ConnectionInfo[] ciSubCons = null;
			
			Hashtable<Object, SubConnection> htSubCons = new Hashtable<Object, SubConnection>();
			
			SubConnection scSub;

			List<SubConnection> liSubCons = getSubConnections();
			
			if (liSubCons != null)
			{
				ArrayUtil<ConnectionInfo> auOpenedSubConInfos = new ArrayUtil<ConnectionInfo>();
				
				//prepare the opened subconnections for the alive-check
				for (int i = 0, anz = liSubCons.size(); i < anz; i++)
				{
					scSub = liSubCons.get(i);
					
					if (scSub.isOpen())
					{
						auOpenedSubConInfos.add(scSub.coninf);
    					
    					htSubCons.put(scSub.coninf.getConnectionId(), scSub);
					}
				}
				
				//check if there is an opened sub connection
				if (auOpenedSubConInfos.size() > 0)
				{
    				ciSubCons = new ConnectionInfo[auOpenedSubConInfos.size()];
    				auOpenedSubConInfos.toArray(ciSubCons);
				}
			}
			
			//perform alive check
			ciSubCons = getConnection().setAndCheckAlive(coninf, ciSubCons);

			if (ciSubCons != null)
			{
				//handle invalid sessions
				Object oConId;
				
				for (int i = 0, anz = ciSubCons.length; i < anz; i++)
				{
					oConId = ciSubCons[i].getConnectionId();
					
					scSub = htSubCons.get(oConId);
					
					synchronized (oSyncSubConnections)
					{
						//fire the event only if the sub connection is still known!
						if (auSubConnections != null && auSubConnections.contains(scSub))
						{
							final SubConnection scSubCopy  = scSub;
							final Object        oConIdCopy = oConId;
							
							//use the UI thread!!!
							//otherwise UI exceptions occur
				    		getUIInvoker().invokeLater(new Runnable()
				    		{
				    			public void run()
				    			{
				    				scSubCopy.fireCallError(setConnection(new SessionExpiredException("" + oConIdCopy)));
				    			}
				    		});
						}
					}
				}
			}

			//clean reference (avoid memory leak)
			ciSubCons = null;
			htSubCons = null;
			scSub = null;
		}
		finally
		{
			bAliveCheckIsActive = false;
		}
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * The <code>AliveThread</code> calls the alive method from
     * the remote server. It enables the server to retrieve alive information
     * without explicit application calls.<p>
     * The alive will be sent when no other call was made for a
     * preconfigured period.
     */
    private final class Alive extends Thread
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	public void run()
    	{
    		long lDiff;
    		
    		ILogger log = null;
    		
    		try
    		{
    		    long lCallTime;
    		    
	    		while (lAliveInterval >= 0 && !ThreadHandler.isStopped())
	    		{
	    		    lCallTime = coninf.getLastCallTime();
	
	    		    if (lCallTime > 0)
	    		    {
    	    			if (lCallTime < System.currentTimeMillis() - lAliveInterval 
    	    				&& !connection.isCalling()) 
    	    			{
    	    			    setAndCheckAlive();
    	    			}
	    	
    	    			//the delay between two alive checks should not be larger than expected
    	    			lDiff = System.currentTimeMillis() - coninf.getLastCallTime();
    	    			
    	    			if (lDiff >= 0 && lDiff < lAliveInterval)
    	    			{
    	    				//don't wait exact the alive-interval (add some delay for flexibility)
    	    				Thread.sleep(lAliveInterval - lDiff + 500);
    	    			}
    	    			else
    	    			{
    	    				//should not happen (only if the time calculation is wrong)
    	    				//don't produce a deadlock in that case!
    	    				Thread.sleep(500);
    	    			}
    	    		}
	    		    else
	    		    {
	    		        Thread.sleep(lAliveInterval);
	    		    }
	    		}
    		}
    		catch (InterruptedException ie)
    		{
    			if (log == null)
    			{
    				log = LoggerFactory.getInstance(getClass());
    			}
    			
    			log.debug(ie);
    		}
    		catch (final Throwable th)
    		{
    			if (log == null)
    			{
    				log = LoggerFactory.getInstance(getClass());
    			}
    			
    			log.debug(th);

				//use the UI thread!!!
				//otherwise UI exceptions occur
    			getUIInvoker().invokeLater(new Runnable()
    			{
    				public void run()
    				{
                        //e.g. if the session is expired or a NullPointerException occured in the thread :(
    		            fireCallError(setConnection(th));
    				}
    			});
    		}
    	}
    	
    }	// Alive
    
    /**
     * The <code>SubConnectionListener</code> handles connection events from sub connections.
     * 
     * @author René Jahn
     */
    private final class SubConnectionListener implements IConnectionListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
		public void callError(CallErrorEvent pEvent)
		{
			//if the alive check is active -> don't check alive, because it will be done!!!
			if (!bAliveCheckIsActive)
			{
                bAliveCheckIsActive = true;

                try
                {
                    if (pEvent.getCause() instanceof SessionExpiredException)
    				{
    	                //if a sub connection was expired -> check the master-connection
    					try
    					{
    						getConnection().setAndCheckAlive(coninf, null);
    					}
    					catch (Throwable th)
    					{
    						fireCallError(setConnection(th));
    					}
    				}
                }
                finally
                {
                    bAliveCheckIsActive = false;
                }
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionOpened(ConnectionEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionReOpened(ConnectionEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionClosed(ConnectionEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionCalled(CallEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void objectCalled(CallEvent pEvent)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void propertyChanged(PropertyEvent pEvent)
		{
		}
    	
    }	// SubConnectionListener
    
    /**
     * The <code>InternalCallBackResultListener</code> sends the callback result events to
     * the right connection/listener.
     * 
     * @author René Jahn
     */
    private final class InternalCallBackResultListener implements ICallBackResultListener
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void callBackResult(CallBackResultEvent pEvent) throws Throwable
        {
            String sConId = pEvent.getConnectionId();
            
            if (getConnectionId().equals(sConId))
            {
                fireCallBackResult(pEvent);
            }
            else
            {
                ArrayUtil<WeakReference<SubConnection>> auSubCache;
                
                synchronized (oSyncSubConnections)
                {
                    if (auSubConnections != null)
                    {
                        auSubCache = auSubConnections.clone();
                    }
                    else
                    {
                        auSubCache = null;
                    }
                }
    
                if (auSubCache != null)
                {
                    SubConnection subcon;
                    
                    for (int i = 0, anz = auSubCache.size(); i < anz; i++)
                    {
                        subcon = auSubCache.get(i).get();
                        
                        if (subcon != null && subcon.getConnectionId().equals(sConId))
                        {
                            subcon.fireCallBackResult(pEvent);
                        }
                    }
                }
            }
        }
        
    }

    
}	// MasterConnection
