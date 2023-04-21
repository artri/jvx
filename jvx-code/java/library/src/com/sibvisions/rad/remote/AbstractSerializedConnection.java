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
 * 05.10.2008 - [JR] - new password mechanism implemented
 * 27.10.2008 - [JR] - implemented changed IConnection methods:
 *                     open, openSub
 *                   - always send the properties when the connection
 *                     is not open
 * 28.10.2008 - [JR] - setProperty: send only changed properties immediately
 * 30.10.2008 - [JR] - setProperty: deny client properties only when connected   
 * 01.02.2009 - [JR] - callIntern: compression property checked for stream compression    
 * 04.02.2009 - [JR] - reopen implemented  
 * 04.04.2009 - [JR] - callIntern: used new ChangedHashtable features (get...(String.class))     
 * 05.04.2009 - [JR] - no WeakReference cache for CallBackListener's
 *                   - KeyValueList for Connection-Id/CallBack-Id's (removed when te connection was closed) 
 * 12.05.2009 - [JR] - call: added not open check        
 *                   - getInputStream/getOutputStream throws Throwable (more flexibility for sub classes) 
 * 13.05.2009 - [JR] - doCallBack: SwingUtilities.invokeLater replaced with a generic way
 * 12.08.2009 - [JR] - callIntern: throw CommunicationException when a SocketException occurs
 * 04.10.2009 - [JR] - setNewPassword: old password as parameter 
 * 21.12.2009 - [JR] - BROKEN: refactoring stack creation (ArrayUtil not necessary)
 * 20.01.2010 - [JR] - Properties constructor added (used from HttpConnection)
 *                   - callIntern: used BufferedInputStream for reading response
 * 23.02.2010 - [JR] - #18: callIntern: use IConnectionConstants.PROPERTY_CLASSES for property transfer     
 * 06.03.2010 - [JR] - #72: UniversalSerializer as default serializer    
 * 29.04.2010 - [JR] - #119: open(): serializer.init called   
 * 01.05.2010 - [JR] - #119: open(): removed reset call       
 * 18.05.2010 - [JR] - create an instance of Reflective to ensure the correct UI thread! 
 * 08.10.2010 - [JR] - #138: callIntern: catched Throwable instead of SocketException because NoRoutToHostException
 *                           is an IOException and not a SocketException
 * 12.10.2011 - [JR] - #482: count result objects and don't use the call count returned from the server,
 *                           because it contains callback results and properties 
 * 11.07.2013 - [JR] - #728: isCalling implemented      
 * 04.04.2014 - [RZ] - #997: implemented addPropertyChangedListener and removePropertyChangedListener
 * 03.06.2014 - [JR] - #1054: used CallBackForward       
 * 30.01.2015 - [JR] - don't read inputstream after response was read (removed Android specific handling) 
 * 29.06.2015 - [JR] - #1422: avoid Multi-Threading problems   
 * 31.10.2015 - [JR] - Auto-EOF option introduced (e.g. Android socket implementation)
 * 11.05.2016 - [JR] - #1603: decrease communication ID  
 * 31.05.2016 - [JR] - #29: prepareException introduced 
 * 12.01.2017 - [JR] - #1744: send NOPARAMETER instead of null       
 * 01.11.2017 - [JR] - decrease communication id even if disabled during call execution  
 * 04.05.2019 - [JR] - #2023: check SessionExpired and avoid calls                          
 */
package com.sibvisions.rad.remote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NoRouteToHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.rad.remote.ConnectionException;
import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SessionCancelException;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.UnauthorizedException;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.CallBackResultEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.ICallBackResultListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.KeyValueList;
import com.sibvisions.util.io.MagicByteInputStream;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;

/**
 * This is the default <code>IConnection</code> implementation. It's independent
 * of the communication protocol.
 * 
 * @author René Jahn
 */
public abstract class AbstractSerializedConnection implements IConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the property name for the serializer. */
	public static final String PROP_SERIALIZER = "serializer";
	
	/** the magic byte sequence. */
	private static final byte[] MAGIC_BYTES = new byte[] {(byte)0x80, (byte)0x17, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
	
	/** call-back key/listener mapping. */
	private Hashtable<Object, CallBackInfo> htCallBack = null;
	
	/** connection-id to call-back key mapping. */
	private KeyValueList<Object, Object> kvlConCallBack = null;
	
    /** synchronization object for the synchronous communication. */
	private Object oSync = new Object();
	
	/** the used serializer for the communication between client and server. */
	private ISerializer serializer = null;

    /** the list of {@link IConnectionPropertyChangedListener}s. */
    private ArrayUtil<IConnectionPropertyChangedListener> auPropertyChangedListeners;

    /** the list of {@link ICallBackResultListener}s. */
    private ArrayUtil<ICallBackResultListener> auCallBackResultListeners;
    
    /** the increasing communication number. */
    private volatile long lCommunicationId;
    
    /** sequence for callback id generation. */
	private long lSequence = 0;

	/** the retry count. */
	private int iRetryCount = 6;
	
	/** the retry interval. */
	private int iRetryInterval = 6000;
	
	/** the temporary max. wait time (disabled). */
	private long lMaxWaitTime = -1;
	
	/** whether a call is active. */
	private boolean bCalling = false;

	/** whether retry should be checked during open call. */
	private boolean bRetryOpen = true;
	
    /** whether retry should be checked during close call. */
    private boolean bRetryClose = false;

    /** whether retry should be checked during alive check call. */
    private boolean bRetryAlive = false;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AbstractSerializedConnection</code> with 
	 * properties containing relevant information. The supported property
	 * keys are:
	 * <ul>
	 *   <li>AbstractSerializedConnection.PROP_SERIALIZER</li>
	 * </ul>
	 * 
	 * @param pProperties the properties for the connection
	 * @throws ClassNotFoundException if the serializer is defined and could not be created                                  
	 */
	public AbstractSerializedConnection(Properties pProperties) throws ClassNotFoundException
	{
		this(createSerializer(pProperties.getProperty(PROP_SERIALIZER)));
	}

	/**
	 * Creates a new instance of <code>AbstractSerializedConnection</code>.
	 * 
	 * @param pSerializer the serializer for the communication between client and server or 
	 *                    null to use the default serializer
	 * @see ISerializer
	 * @see ByteSerializer
	 */
	public AbstractSerializedConnection(ISerializer pSerializer)
	{
		if (pSerializer == null)
		{
			this.serializer = new UniversalSerializer();
		}
		else
		{
			this.serializer = pSerializer;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void open(ConnectionInfo pConnectionInfo) throws Throwable
	{
		Object oConnectionId = callInternSynced(pConnectionInfo,
    										    new String[] {IConnection.OBJ_SESSION}, 
    										    new String[] {IConnection.MET_SESSION_CREATE}, 
    										    null, 
    										    null,
    										    bRetryOpen)[0];
		
		pConnectionInfo.setConnectionId(oConnectionId);

		setExpired(pConnectionInfo, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void openSub(ConnectionInfo pConnectionInfo, ConnectionInfo pConnectionInfoSub) throws Throwable
	{
		//Don't call through the existing session, because the properties will not
		//set into the sub session!
		Object oConnectionId = callInternSynced(pConnectionInfoSub,
    										    new String[] {IConnection.OBJ_SESSION}, 
    										    new String[] {IConnection.MET_SESSION_SUBSESSION_CREATE}, 
    										    new Object[][] { {pConnectionInfo.getConnectionId()} }, 
    										    null,
    										    bRetryOpen)[0];
		
		pConnectionInfoSub.setConnectionId(oConnectionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOpen(ConnectionInfo pConnectionInfo)
	{
		return pConnectionInfo != null && pConnectionInfo.getConnectionId() != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close(ConnectionInfo pConnectionInfo) throws Throwable
	{
		if (isOpen(pConnectionInfo))
		{
			try
			{
				callInternSynced(pConnectionInfo,
            					 new String[] {IConnection.OBJ_SESSION}, 
            					 new String[] {IConnection.MET_SESSION_DESTROY}, 
            					 null, 
            					 null,
            					 bRetryClose);
			}
			finally
			{
				Object oConId = pConnectionInfo.getConnectionId();
				
				pConnectionInfo.setConnectionId(null);
				
				setExpired(pConnectionInfo, false);
				
				if (kvlConCallBack != null)
				{
					//remove the cached call-back information for the connection! 
					List<Object> liCallBackInfo = kvlConCallBack.remove(oConId);
					
					if (liCallBackInfo != null)
					{
						for (int i = 0, anz = liCallBackInfo.size(); i < anz; i++)
						{
							htCallBack.remove(liCallBackInfo.get(i));
						}
					}
				}
			}
		}
		else
		{
			throw new IllegalStateException("Connection not open");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object[] call(ConnectionInfo pConnectionInfo, 
						 String[] pObjectName, 
						 String[] pMethod, 
						 Object[][] pParams, 
						 ICallBackListener[] pCallBack) throws Throwable
	{
		if (pConnectionInfo == null)
		{
			throw new IllegalArgumentException("Invalid connection information: null");
		}

		if (pConnectionInfo.getConnectionId() == null)
		{
			throw new IllegalStateException("The connection is not open!");
		}
		
		return callInternSynced(pConnectionInfo, pObjectName, pMethod, pParams, pCallBack, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ConnectionInfo[] setAndCheckAlive(ConnectionInfo pConnectionInfo, ConnectionInfo[] pSubConnections) throws Throwable
	{
		Hashtable<Object, ConnectionInfo> htMapping;
		
		Object[] oConIds;
		
		//cache the subconnections for analyzing the result object
		if (pSubConnections != null)
		{
			int anz = pSubConnections.length;
			
			htMapping = new Hashtable<Object, ConnectionInfo>(anz);
			
			oConIds = new Object[anz];
			
			for (int i = 0; i < anz; i++)
			{
				oConIds[i] = pSubConnections[i].getConnectionId();
				
				htMapping.put(oConIds[i], pSubConnections[i]);
			}
		}
		else
		{
			htMapping = null;
			oConIds   = null;
		}
		
		oConIds = (Object[])callInternSynced(pConnectionInfo,
									         new String[] {IConnection.OBJ_SESSION}, 
									         new String[] {IConnection.MET_SESSION_SETCHECKALIVE}, 
									         new Object[][] {oConIds}, 
									         null,
									         bRetryAlive)[0];
		
		if (oConIds == null)
		{
			return null;
		}
		else
		{
			//use the untouched input-objects as result
			
			int anz = oConIds.length;
			
			ConnectionInfo[] ciResult = new ConnectionInfo[anz];
			
			for (int i = 0; i < anz; i++)
			{
				ciResult[i] = htMapping.get(oConIds[i]);
			}
			
			return ciResult;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setProperty(ConnectionInfo pConnectionInfo, String pName, Object pValue) throws Throwable
	{
	    if (pName != null)
	    {
    		//client properties can only be changed when the connection is closed!
    		if (pName.startsWith(IConnectionConstants.PREFIX_CLIENT))
    		{
    			if (isOpen(pConnectionInfo))
    			{
    				throw new SecurityException("Client properties are not accessible after the connection was opened!");
    			}
    
    			Object oOldValue = pConnectionInfo.getProperties().put(pName, pValue);
    			
    			firePropertyChanged(pName, oOldValue, pValue, false);
    		}
    		else
    		{
    			Object oOldValue = pConnectionInfo.getProperties().put(pName, pValue);
    			
                firePropertyChanged(pName, oOldValue, pValue, false);
    
                //send only changed session parameters immediately to the server
    			if (!CommonUtil.equals(oOldValue, pValue))
    			{
    	            if (pName != null && pName.startsWith(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION) && isOpen(pConnectionInfo))
    				{
    					callInternSynced(pConnectionInfo, 
    							         new String[] {}, 
    							         new String[] {}, 
    							         null, 
    							         null,
    							         true);
    				}
    			}
    		}
	    }
	    else
	    {
	        throw new IllegalArgumentException("Property name is undefined!");
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getProperty(ConnectionInfo pConnectionInfo, String pName) throws Throwable
	{
		if (pConnectionInfo != null)
		{
			return pConnectionInfo.getProperties().get(pName);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, Object> getProperties(ConnectionInfo pConnectionInfo) throws Throwable
	{
		if (pConnectionInfo != null)
		{
			return (Hashtable<String, Object>)pConnectionInfo.getProperties().clone();
		}
		
		return null;
	}

    /**
     * {@inheritDoc}
     */
    public void setNewPassword(ConnectionInfo pConnectionInfo, String pOldPassword, String pNewPassword) throws Throwable
    {
        callInternSynced(pConnectionInfo,
                         new String[] {IConnection.OBJ_SESSION}, 
                         new String[] {IConnection.MET_SESSION_SET_NEW_PASSWORD}, 
                         new Object[][] { {pOldPassword, pNewPassword} }, 
                         null,
                         true);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCalling()
    {
        return bCalling;
    }       

    /**
	 * {@inheritDoc}
	 */
	public void addPropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		if (auPropertyChangedListeners == null)
		{
			auPropertyChangedListeners = new ArrayUtil<IConnectionPropertyChangedListener>();
		}
		
		if (!auPropertyChangedListeners.contains(pListener))
		{
		    auPropertyChangedListeners.add(pListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangedListener(IConnectionPropertyChangedListener pListener)
	{
		if (auPropertyChangedListeners != null)
		{
			auPropertyChangedListeners.remove(pListener);
		}
	}
	
    /**
     * {@inheritDoc}
     */
    public void addCallBackResultListener(ICallBackResultListener pListener)
    {
        if (auCallBackResultListeners == null)
        {
            auCallBackResultListeners = new ArrayUtil<ICallBackResultListener>();
        }
        
        if (!auCallBackResultListeners.contains(pListener))
        {
            auCallBackResultListeners.add(pListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeCallBackResultListener(ICallBackResultListener pListener)
    {
        if (auCallBackResultListeners != null)
        {
            auCallBackResultListeners.remove(pListener);
        }
    }   
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the <code>OutputStream</code> for submitting requests to the
	 * server.
	 * 
	 * @param pConnectionInfo the connection information
	 * @return output stream
	 * @throws Throwable if it's not possible to get the output stream
	 */
	public abstract OutputStream getOutputStream(ConnectionInfo pConnectionInfo) throws Throwable;

	/**
	 * Gets the <code>InputStream</code> for reading the response from the 
	 * server.
	 * 
	 * @param pConnectionInfo the connection information
	 * @return input stream
	 * @throws Throwable if it's not possible to get the the input stream
	 */
	public abstract InputStream getInputStream(ConnectionInfo pConnectionInfo) throws Throwable;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable
	{
		htCallBack = null;
		kvlConCallBack = null;
		
		super.finalize();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the serializer from a given class name.
	 * 
	 * @param pClassName the {@link ISerializer} implementation class
	 * @return the serializer implementation
	 * @throws ClassNotFoundException if the serializer could not be created
	 */
	protected static ISerializer createSerializer(String pClassName) throws ClassNotFoundException
	{
		if (pClassName != null)
		{
			ISerializer serializer = null;
			
			try
			{
				serializer = (ISerializer)Class.forName(pClassName).newInstance();
				
				return serializer;
			}
			catch (RuntimeException e)
			{
				throw e;
			}
			catch (ClassNotFoundException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new ClassNotFoundException(pClassName);
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Calls desired methods from a remote server object, synchronized.
	 *
	 * @param pConnectionInfo the connection information
	 * @param pObjectName list of server object names/aliases
	 * @param pMethod method names which should be called
	 * @param pParams parameters for the method calls
	 * @param pCallBack callback listeners for asynchronous or null for synchronous calls
	 * @param pRetryEnabled <code>true</code> to do retries, <code>false</code> otherwise 
	 * @return result list from the remote method calls
	 * @throws Throwable communication error, security checks, invalid method, ...
	 */
	private Object[] callInternSynced(ConnectionInfo pConnectionInfo,
        						      String[] pObjectName, 
        							  String[] pMethod, 
        							  Object[][] pParams, 
        							  ICallBackListener[] pCallBack,
        							  boolean pRetryEnabled) throws Throwable
	{
	    synchronized (oSync)
	    {
	        return callIntern(pConnectionInfo, pObjectName, pMethod, pParams, pCallBack, pRetryEnabled);
		}
	}

    /**
     * Calls desired methods from a remote server object.
     *
     * @param pConnectionInfo the connection information
     * @param pObjectName list of server object names/aliases
     * @param pMethod method names which should be called
     * @param pParams parameters for the method calls
     * @param pCallBack callback listeners for asynchronous or null for synchronous calls
     * @param pRetryEnabled <code>true</code> to do retries, <code>false</code> otherwise
     * @return result list from the remote method calls
     * @throws Throwable communication error, security checks, invalid method, ...
     */
	@SuppressWarnings("resource")
	private Object[] callIntern(ConnectionInfo pConnectionInfo,
	                            String[] pObjectName,
	                            String[] pMethod,
	                            Object[][] pParams,
	                            ICallBackListener[] pCallBack,
	                            boolean pRetryEnabled) throws Throwable
    {
	    if (isExpired(pConnectionInfo))
		{
			throw new SessionExpiredException("Session expired '" + pConnectionInfo.getConnectionId() + "'");
		}
		
        try
        {
            bCalling = true;

            boolean bReadMagicByte = isReadingMagicByteEnabled();
            boolean bWriteMagicByte = isWritingMagicByteEnabled();
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Validation
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
            //It's allowed to let the object name empty -> action call
            //It's allowed to let the method name empty -> doesn't call anything but transfers callback results/properties
            
            if (pMethod == null)
            {
                throw new IllegalArgumentException("No remote method specified!");
            }
            
            if (pObjectName != null && pMethod.length != pObjectName.length)
            {
                throw new IllegalArgumentException("More or less objects than methods!");
            }

            if (pParams != null && pParams.length != pMethod.length)
            {
                throw new IllegalArgumentException("More or less params than methods!");
            }
            
            Object oConnectionId = pConnectionInfo.getConnectionId();
            
            //Without a connection id, the only allowed call is the session create/open call
            if (oConnectionId == null)
            {
                //this code can only be reached when callIntern was used wrong, because the empty
                //session id will be checked in callIntern
                if (pObjectName == null 
                    || !IConnection.OBJ_SESSION.equals(pObjectName[0]) 
                    || (!IConnection.MET_SESSION_CREATE.equals(pMethod[0]) 
                        && !IConnection.MET_SESSION_SUBSESSION_CREATE.equals(pMethod[0])))
                {
                    throw new IOException("Connection is not open!");
                }
            }
    
            if (pCallBack != null && pCallBack.length != pMethod.length)
            {
                throw new IllegalArgumentException("More or less callbacks than methods!");
            }
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // setup call(s)
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            Object oBeforeCall = initCall(pConnectionInfo);
            
            try
            {
                Object[] oCallBack;
                
                int iCallCount = pMethod.length;
                
                //Map callbacks for asynchronous execution
                if (pCallBack != null)
                {
                    oCallBack = new Object[pCallBack.length];
                    
                    for (int i = 0, anz = pCallBack.length; i < anz; i++)
                    {
                        if (pCallBack[i] != null)
                        {
                            oCallBack[i] = createCallBackId();
                            
                            if (htCallBack == null)
                            {
                                htCallBack = new Hashtable<Object, CallBackInfo>();
                                
                                kvlConCallBack = new KeyValueList<Object, Object>();
                            }
    
                            //Map the callback information for the callback id because we need
                            //the information for Callback listener notifications!
                            htCallBack.put(oCallBack[i], 
                                           new CallBackInfo(this,
                                                            oConnectionId, 
                                                            pObjectName != null ? pObjectName[i] : null, 
                                                            pMethod[i], 
                                                            pCallBack[i]));
                            
                            //Cache the created callback ids. Otherwise it's not possible to remove the
                            //callback identifiers when a connection will be closed!
                            kvlConCallBack.put(oConnectionId, oCallBack[i]);
                        }
                    }
                }
                else
                {
                    oCallBack = null;
                }
        
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // REQUEST
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
                ByteArrayOutputStream baosContent = new ByteArrayOutputStream(IConnection.COMPRESSION_BYTES);
                DataOutputStream dosContent  = new DataOutputStream(baosContent);
                
                if (oConnectionId == null)
                {
                    //first request -> send the serializer classname!
                    dosContent.writeUTF(serializer.getClass().getName());
                    //no connection id (= null), causes NullPointerException. Use "" instead!
                    dosContent.writeUTF("");
                }
                else
                {
                    dosContent.writeUTF((String)pConnectionInfo.getConnectionId());
                }
                
                List<Entry<String, Object>> liProperties;
    
                //When the connection is already open -> transfer only property changes. When
                //establishing the connection, all current properties will be sent to the server!
                //Important when open/close/open the connection!
                if (isOpen(pConnectionInfo))
                {
                    liProperties = pConnectionInfo.getProperties().getChanges(IConnectionConstants.PROPERTY_CLASSES);
                }
                else
                {
                    liProperties = pConnectionInfo.getProperties().getMapping(IConnectionConstants.PROPERTY_CLASSES);
    
                    //with the next call -> only changes will be transfered!
                    pConnectionInfo.getProperties().clearChanges();
                }
                
                String sCID;
                
                boolean bUseCommunicationId = iRetryCount > 0;

                float fCurrentRetryCount = iRetryCount;
                
                try
                {
                    if (bUseCommunicationId)
                    {
                        sCID = createCommunicationId();
    
                        //Write communication id
                        serializer.write(dosContent, sCID);
                    }
                    
                    //Write call count
                    iCallCount += (liProperties != null ? 1 : 0);
        
                    serializer.write(dosContent, Integer.valueOf(iCallCount));
                    
                    //Send properties before sending calls. Thats important that the server/session
                    //can use the properties before accessing the session!
                    if (liProperties != null)
                    {
                        //use a transferable object-type!
                        List<Object[]> liTransferProperties = new ArrayUtil<Object[]>(liProperties.size());
                        
                        for (Entry<String, Object> entry : liProperties)
                        {
                            liTransferProperties.add(new Object[] {entry.getKey(), entry.getValue()});
                        }
                        
                        serializer.write(dosContent,
                                         new Object[] {IConnection.OBJ_SESSION,
                                                       IConnection.MET_SESSION_SET_PROPERTY,
                                                       new Object[] {liTransferProperties},
                                                       null});
                    }
        
                    //Call(s)
                    for (int i = 0, anz = pMethod.length; i < anz; i++)
                    {
                        serializer.write(dosContent, 
                                         new Object[] {pObjectName != null ? pObjectName[i] : null, 
                                                       pMethod[i], 
                                                       pParams != null ? pParams[i] : NOPARAMETER,
                                                       oCallBack != null ? oCallBack[i] : null});
                    }
                    
                    dosContent.close();
                }
                catch (Throwable th)
                {
                    if (bUseCommunicationId)
                    {
                        //#1603
                        decreaseCommunicationId();
                    }
                    
                    throw th;
                }
                
                //Request-Header (same as in in Server.java)
                //
                //<STREAM-IDENTIFIER>       1Byte  (A = Acknowledge; E = Established) 
                //<OPTION-FLAG-1>           1Byte  (0x01 = UNCOMPRESSED; 0x02 = COMPRESSED)
                // - <SERIALIZER-CLASSNAME> xBytes (only with A, via DataOutputStream)
                // - <SESSION-ID>           xBytes (via DataOutputStream)
                // - <CALL-COUNT>           xBytes (via ISerializer)
                // - <CALL-PARAMETER>       xBytes (via ISerializer)
                //    - [Objectname, Method, Parameter, CallBack-ID]
                
                OutputStream osRequest;
    
                MagicByteInputStream mbisResponse = null;
                
                Exception exLast = null;
                
                int iFailure = 0;
                
                long lStart;
                long lEnd;
                long lDiff;
                
                Long lAliveInterval = (Long)pConnectionInfo.getProperties().get(IConnectionConstants.ALIVEINTERVAL);
               
                if (lAliveInterval != null)
                {
                	Integer iAliveFactor = (Integer)pConnectionInfo.getProperties().get(IConnectionConstants.ALIVEFACTOR);
                	
                	int iFactor;
                	
                	if (iAliveFactor != null)
                	{
                		iFactor = iAliveFactor.intValue();
                		
                		if (iFactor <= 0)
                		{
                			iFactor = 4;
                		}
                	}
                	else
                	{
                		//default value
                		iFactor = 4;
                	}
                	
                	//We detect the max wait time for this connection and independent of the given connection info. It's possible that
                	//the alive interval is not set for every connection info, but we use the "best found".
                	//A sub connection doesn't have a alive interval, but the master connection has it... so we support this case
                	
                	//wait time: shorter than alive-timeout
                	lMaxWaitTime = (iFactor - 2) * lAliveInterval.longValue();
                }
                
                do
                {
                    lStart = System.currentTimeMillis();
                    lEnd = -1;
                    
                    try
                    {
                        try
                        {
                            osRequest = getOutputStream(pConnectionInfo);
                        }
                        catch (UnauthorizedException uae)
                        {
                            throw uae;
                        }
                        catch (Throwable th)
                        {
                            if (bUseCommunicationId)
                            {
                                checkSessionCancel(th);
                            }
                            
                            checkSessionExpired(pConnectionInfo, th);
                         
                            if (th instanceof ConnectionException)
                            {
                                throw (ConnectionException)th;
                            }
                            
                            throw new ConnectionException(th);
                        }
                        
                        if (oConnectionId == null)
                        {
                            osRequest.write(IConnection.FLAG_ACKNOWLEDGE);
                        }
                        else
                        {
                            osRequest.write(IConnection.FLAG_ESTABLISHED);
                        }
                    
                        boolean bCompressionSupported = Boolean.parseBoolean((String)pConnectionInfo.getProperties().get(IConnectionConstants.COMPRESSION));

                        //Use compression when the compression mode is enabled and the maximum number of uncompressed bytes are reached 
                        if (bCompressionSupported && baosContent.size() >= IConnection.COMPRESSION_BYTES)
                        {
                            osRequest.write(IConnection.MODE_COMPRESSED);
                            
                            GZIPOutputStream zosContent = new GZIPOutputStream(osRequest);
                            baosContent.writeTo(zosContent);
                            zosContent.finish();
                            
                            if (bWriteMagicByte)
                            {
                                osRequest.write(MAGIC_BYTES);
                            }
                        }
                        else
                        {
                            osRequest.write(IConnection.MODE_UNCOMPRESSED);

                            baosContent.writeTo(osRequest);
                        }
                        
                        osRequest.flush();
            
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        // RESPONSE
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
                        InputStream isResponseOrig;
                        
                        try
                        {
                            //store the reference to the original stream, for reading until EOF
                            isResponseOrig = getInputStream(pConnectionInfo);
                        }
                        catch (UnauthorizedException uae)
                        {
                            throw uae;
                        }
                        catch (Throwable th)
                        {
                            if (bUseCommunicationId)
                            {
                                checkSessionCancel(th);
                            }
                            
                            checkSessionExpired(pConnectionInfo, th);
                            
                            if (th instanceof ConnectionException)
                            {
                                throw (ConnectionException)th;
                            }
                            
                            throw new ConnectionException(th);
                        }
            
                        if (!(isResponseOrig instanceof BufferedInputStream))
                        {
                            isResponseOrig = new BufferedInputStream(isResponseOrig);
                        }
                        
                        //now it's ok to think that the transfer was successful
                        pConnectionInfo.setLastCallTime(System.currentTimeMillis());
                        
                        //Response-Header (same as in Server.java)
                        //
                        //<STREAM-IDENTIFIER>  1Byte  (E = Established; B = Broken) 
                        //<OPTION-FLAG-1>      1Byte  (0x01 = UNCOMPRESSED; 0x02 = COMPRESSED>
                        // - <RESULT-COUNT>    xBytes (only with E, via ISerializer)
                        // - <RESULT-TYPE>     1Byte  (only with E, via ISerializer)            
                        // - <RESULT-OBJECT>   xBytes (only with E, via ISerializer)            
                        // - <RESULT-TEXT>     xBytes (only with B, via DataInputStream)
                        
                        //Get stream identifier
                        char chStreamID = (char)isResponseOrig.read();

                        if (chStreamID != IConnection.FLAG_ESTABLISHED 
                            && chStreamID != IConnection.FLAG_BROKEN)
                        {
                            throw new IOException("Invalid stream identifier '" + chStreamID + "'");
                        }

                        int iMode = isResponseOrig.read();
                        
                        InputStream isResponse;                        

                        //Handle compression
                        if (iMode == IConnection.MODE_COMPRESSED)
                        {
                            if (bReadMagicByte)
                            {
                                mbisResponse = new MagicByteInputStream(isResponseOrig, MAGIC_BYTES, isAutoEOFEnabled());
                                
                                isResponse = new GZIPInputStream(mbisResponse);
                            }
                            else
                            {
                                isResponse = new GZIPInputStream(isResponseOrig);
                            }
                        }
                        else
                        {
                            isResponse = isResponseOrig;
                        }
                        
                        DataInputStream disContent = new DataInputStream(isResponse);
            
                        try
                        {
                            if (chStreamID == IConnection.FLAG_BROKEN)
                            {
                                //Broken streams -> deserialize the exception with DataInputStream!
                                
                                Throwable thResult = (Throwable)Class.forName(disContent.readUTF()).getConstructor(new Class[] {String.class}).newInstance(disContent.readUTF());
                                
                                //Get the number of StackTrace elements
                                int iCount = disContent.read();
                                
                                if (iCount > 0)
                                {
                                    //assemble the StackTrace
                                    StackTraceElement[] stack = new StackTraceElement[iCount];
                                    
                                    for (int i = 0; i < iCount; i++)
                                    {
                                        stack[i] = new StackTraceElement(disContent.readUTF(), disContent.readUTF(), disContent.readUTF(), disContent.readInt()); 
                                    }
                                    
                                    thResult.setStackTrace(stack);
                                }
                                
                                if (bUseCommunicationId)
                                {
                                    checkSessionCancel(thResult);
                                }
                                
                                checkSessionExpired(pConnectionInfo, thResult);
    
                                //create new Throwable because we want to know the full client stack trace!
                                LoggerFactory.getInstance(AbstractSerializedConnection.class).debug(new Throwable("Connection broken!", thResult));
                                
                                throw thResult;
                            }
                            else
                            {
                                iCallCount = ((Integer)serializer.read(disContent)).intValue();

                                Object   oCallBackId;
                                Object   oReturn;
                                
                                ArrayUtil<Object> auResult = new ArrayUtil<Object>(iCallCount);

                                Throwable thCall = null;
                                
                                byte byResultType;
                                
                                
                                //Interpret call results
                                for (int i = 0; i < iCallCount; i++)
                                {
                                    oCallBackId = null;
                                    
                                    oReturn = serializer.read(disContent);
                                    
                                    //Check result types
                                    if (oReturn.getClass() == Byte.class)
                                    {
                                        byResultType = ((Byte)oReturn).byteValue();
                        
                                        oReturn = serializer.read(disContent);
                                        
                                        if (byResultType == IConnection.TYPE_CALLBACK_ERROR 
                                            || byResultType == IConnection.TYPE_CALLBACK_RESULT
                                            || byResultType == IConnection.TYPE_CALLBACKRESULT_RESULT)
                                        {
                                            oCallBackId = serializer.read(disContent);

                                            if (byResultType == IConnection.TYPE_CALLBACKRESULT_RESULT)
                                            {
                                                doCallBackResult(oCallBackId, oReturn);
                                            }
                                            else
                                            {
                                                //Exceptions from async calls -> forward via callback listener
                                                doCallBack(byResultType, oCallBackId, oReturn);
                                            }
                                        }
                                        else if (byResultType == IConnection.TYPE_PROPERTY_RESULT)
                                        {
                                            ChangedHashtable<String, Object> chtProperties = pConnectionInfo.getProperties();
                                            
                                            if (chtProperties != null)
                                            {
                                                //Set the properties received from server as they are!
                                                ArrayUtil<Object[]> auProperties = (ArrayUtil<Object[]>)oReturn;
                                            
                                                Object[] oProps;
                                                
                                                for (int k = 0, anz = auProperties.size(); k < anz; k++)
                                                {
                                                    oProps = (Object[])auProperties.get(k); 
                                                    
                                                    Object oOldValue = chtProperties.put((String)oProps[0], (Object)oProps[1], false);
            
                                                    firePropertyChanged((String)oProps[0], oOldValue, (Object)oProps[1], true);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            //if one call had an error -> check other errors but ignore the results
                                            if (thCall != null)
                                            {
                                                if (byResultType == IConnection.TYPE_CALL_ERROR)
                                                {
                                                    if (bUseCommunicationId)
                                                    {
                                                        checkSessionCancel((Throwable)oReturn);
                                                    }
                                                    
                                                    checkSessionExpired(pConnectionInfo, (Throwable)oReturn);
                                                }
                                            }
                                            else
                                            {
                                                if (byResultType == IConnection.TYPE_CALL_ERROR || byResultType == IConnection.TYPE_CALL_RESULT)
                                                {
                                                    if (byResultType == IConnection.TYPE_CALL_ERROR)
                                                    {
                                                        if (bUseCommunicationId)
                                                        {
                                                            //Exceptions from sync calls -> throw "immediate" (after reading all results)
                                                            checkSessionCancel((Throwable)oReturn);
                                                        }
                                                        
                                                        checkSessionExpired(pConnectionInfo, (Throwable)oReturn);
                                                
                                                        thCall = (Throwable)oReturn;
                                                    }
                                                    else
                                                    {
                                                        auResult.add(oReturn);
                                                    }
                                                }
                                                else
                                                {
                                                    thCall = new IOException("Invalid return type");
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        //can't clear buffer because we don't know the protocol
                                        
                                        throw new IOException("Invalid response type");
                                    }
                                }
                                
                                //throw call error, after all results were read
                                if (thCall != null)
                                {
                                    throw thCall;
                                }

                                return auResult.toArray(new Object[auResult.size()]);
                            }
                        }
                        finally
                        {
                            if (mbisResponse != null)
                            {
                                mbisResponse.close();
                            }
                            
                            isResponse.close();
                            disContent.close();
                        }
                    }
                    catch (ConnectionException ex)
                    {
                        exLast = ex;
    
                        lEnd = System.currentTimeMillis();
                        iFailure++;
    
                        ILogger logger = LoggerFactory.getInstance(AbstractSerializedConnection.class);
                        
                        if (Math.round(fCurrentRetryCount) > 0 && iFailure <= Math.round(fCurrentRetryCount) && pRetryEnabled)
                        {
                            logger.debug("Failure #", Integer.valueOf(iFailure), ex);
                        }
                        else
                        {
                            logger.error(ex);
                        }
                    }
                    catch (Throwable th)
                    {
                        throw prepareException(th);
                    }

                    //don't wait after last retry call
                    if (pRetryEnabled && Math.round(fCurrentRetryCount) > 0 && iFailure < Math.round(fCurrentRetryCount))
                    {
	                    lDiff = 0;
	                    
	                    if (lEnd > 0)
	                    {
	                        lDiff = lEnd - lStart;
	                    }

	                    if (lDiff < iRetryInterval)
	                    {
	                    	long lWaitTime = iRetryInterval - lDiff;
	                    	
	                        if (lMaxWaitTime > 0 && lWaitTime > lMaxWaitTime)
	                    	{
	                    		float fWaitTimeDiff = (lWaitTime - lMaxWaitTime) / (float)iRetryInterval;
	                    		
	                    		//increase retry count - otherwise the retry-time is not the same as without alive limitation
	                    		//so, we calculate an internal retry count which is more than the user-defined retry count,
	                    		//but the retry time is similar
	                    		
	                    		//this is only important if timeout is smaller than the retry interval 
	                    		
	                    		fCurrentRetryCount += fWaitTimeDiff;
	                    		
	                    		lWaitTime = lMaxWaitTime;
	                    	}
	                    	
		                    try
		                    {
		                        Thread.sleep(lWaitTime);
		                    }
		                    catch (InterruptedException ie)
		                    {
		                        //ignore
		                    }
	                    }
                    }
                }
                while (pRetryEnabled && Math.round(fCurrentRetryCount) > 0 && iFailure <= Math.round(fCurrentRetryCount) && !isExpired(pConnectionInfo));
                
                Throwable thFailureCheck = prepareAfterRetryException(exLast);
                
        		if (thFailureCheck instanceof NoRouteToHostException)
        		{
                    if (bUseCommunicationId)
                    {
                        decreaseCommunicationId();
                    }

                    throw new ConnectionException(thFailureCheck);
        		}
                
                throw thFailureCheck;
            }
            finally
            {
                releaseCall(pConnectionInfo, oBeforeCall);
            }
        }
        finally
        {
            bCalling = false;
        }
    }
	
	/**
	 * Will be invoked before a call starts.
	 * 
	 * @param pConnectionInfo the connection info
	 * @return a custom object that will be used as parameter for -{@link #releaseCall(ConnectionInfo, Object)}
	 */
	protected Object initCall(ConnectionInfo pConnectionInfo)
	{
        //relevant for sub classes
	    return null;
	}

	/**
	 * Will be invoked after a call was executed, if {@link #initCall(ConnectionInfo)} was successfully invoked.
	 * 
     * @param pConnectionInfo the connection info
	 * @param pInit the object whic was created in {@link #initCall(ConnectionInfo)}
	 */
    protected void releaseCall(ConnectionInfo pConnectionInfo, Object pInit)
    {
        //relevant for sub classes
    }
	
	/**
	 * Creates a new communication identifier for the given connection identifier.
	 * 
	 * @return the communication identifier
	 */
    private String createCommunicationId()
    {
        return "" + lCommunicationId++; 
    }
	
    /**
     * Create a new id for a callback interface.
     * 
     * @return unique id
     */
    private synchronized Object createCallBackId()
    {
    	//ascending number + SC (= Serialized Connection -> derived from the classname)
    	return Long.toHexString(++lSequence) + "SC";
    }
    
    /**
     * Notifies an <code>ICallBackListener</code> with the result of an asynchronous
     * method call.
     * 
     * @param pResultType {@link IConnection#TYPE_CALLBACK_RESULT} or {@link IConnection#TYPE_CALLBACK_ERROR}
     * @param pCallBackId identifier for the callback mapping
     * @param pResult result of the remote method call
     */
    private void doCallBack(byte pResultType, Object pCallBackId, Object pResult)
    {
    	CallBackInfo cbiInfo = htCallBack.remove(pCallBackId);

    	
    	if (cbiInfo != null)
    	{
    		//cleanup
    		kvlConCallBack.remove(cbiInfo.getConnectionId(), pCallBackId);
    		
    		if (htCallBack.size() == 0)
    		{
    			htCallBack = null;
    			kvlConCallBack = null;
    		}
    		
    		ICallBackListener cblistener = cbiInfo.getCallBackListener();
    		
    		if (cblistener != null)
    		{
    		    Throwable throwable = null;
    		    Object object = null;
    		    
    		    switch (pResultType)
                {
                    case IConnection.TYPE_CALLBACK_ERROR:
                        throwable = (Throwable)pResult;
                        break;
                    case IConnection.TYPE_CALLBACK_RESULT:
                    default:
                        object = pResult;
                        break;
                }    		    
    		    
    		    try
    		    {
	    		    cblistener.callBack
	                (
	                    new CallBackEvent
	                    (
	                        cbiInfo.getConnection(),
	                        cbiInfo.getObjectName(), 
	                        cbiInfo.getMethodName(), 
	                        object,
	                        prepareException(throwable),
	                        cbiInfo.getCreateTime(),
	                        System.currentTimeMillis()
	                    )
	                );
            	}
            	catch (Throwable th)
            	{
            		LoggerFactory.getInstance(AbstractSerializedConnection.class).error(th);
            	}
    		}
    	}
    	else
    	{
    	    //don't throw an Exception here, because callbacks shouldn't interrupt other calls
    	    LoggerFactory.getInstance(AbstractSerializedConnection.class).info("CallBack ID not found: '", pCallBackId, "'");
    	}
    }
    
    /**
     * Fires the callback result event on all registered {@link ICallBackResultListener}s.
     * 
     * @param pCallBackId the callback id (= instruction) 
     * @param pResult the result object 
     * @throws Throwable if listener notifaction failed
     */
    private void doCallBackResult(Object pCallBackId, Object pResult) throws Throwable
    {
        if (auCallBackResultListeners != null)
        {
            CallBackResultEvent event = new CallBackResultEvent((String)pCallBackId, pResult);
            
            for (int i = 0, cnt = auCallBackResultListeners.size(); i < cnt; i++)
            {
                auCallBackResultListeners.get(i).callBackResult(event);
            }
        }
    }
    
	/**
	 * Fires the property changed event on all registered {@link IConnectionPropertyChangedListener}s
	 * if the parameters <code>pOldValue</code> and <code>pNewValue</code> are
	 * not the same and both are not null.
	 * 
	 * @param pName the name of the property
	 * @param pOldValue the old value of the property
	 * @param pNewValue the new value of the property
	 * @param pContinueOnException whether an exception shouldn't stop notification of listeners
	 * @throws Throwable if listener notification failed
	 */
	private void firePropertyChanged(String pName, Object pOldValue, Object pNewValue, boolean pContinueOnException) throws Throwable
	{
		if (auPropertyChangedListeners != null)
		{
			if (!CommonUtil.equals(pOldValue, pNewValue))
			{
				PropertyEvent event = new PropertyEvent(pName, pOldValue, pNewValue);
				
				for (int i = 0, cnt = auPropertyChangedListeners.size(); i < cnt; i++)
				{
				    try
				    {
				        auPropertyChangedListeners.get(i).propertyChanged(event);
                    }
                    catch (Throwable th)
                    {
                        if (!pContinueOnException)
                        {
                            throw th;
                        }
                        
                        LoggerFactory.getInstance(AbstractSerializedConnection.class).error(th);
                    }
				}
			}
		}
	}

	/**
	 * The number of retries before the connection will be marked as broken.
	 * 
	 * @param pRetryCount the number of retries
	 */
	public void setRetryCount(int pRetryCount)
	{
	    iRetryCount = pRetryCount;
	}
	
	/**
	 * Gets the number of retries if a communication error occurs.
	 * 
	 * @return the number of retries
	 * @see #setRetryCount(int)
	 */
	public int getRetryCount()
	{
	    return iRetryCount;
	}
	
	/**
	 * Sets whether open should retry if a communication error occurs.
	 * 
	 * @param pRetryOpen <code>true</code> to retry if open failed at first attempt
	 */
	public void setRetryDuringOpen(boolean pRetryOpen)
	{
	    bRetryOpen = pRetryOpen;
	}
	
	/**
	 * Gets whether open should retry if a communication error occurs.
	 * 
	 * @return <code>true</code> if open will retry if failed at first attempt, <code>false</code>
	 *         to throw an Exception after first attempt (immediate)
	 */
	public boolean isRetryDuringOpen()
	{
	    return bRetryOpen;
	}
	
    /**
     * Sets whether close should retry if a communication error occurs.
     * 
     * @param pRetryClose <code>true</code> to retry if close failed at first attempt
     */
    public void setRetryDuringClose(boolean pRetryClose)
    {
        bRetryClose = pRetryClose;
    }
    
    /**
     * Gets whether close should retry if a communication error occurs.
     * 
     * @return <code>true</code> if close will retry if failed at first attempt, <code>false</code>
     *         to throw an Exception after first attempt (immediate)
     */
    public boolean isRetryDuringClose()
    {
        return bRetryClose;
    }

    /**
     * Sets whether alive check should retry if a communication error occurs.
     * 
     * @param pRetryAlive <code>true</code> to retry if alive check failed at first attempt
     */
    public void setRetryDuringAliveCheck(boolean pRetryAlive)
    {
        bRetryAlive = pRetryAlive;
    }
    
    /**
     * Gets whether alive check should retry if a communication error occurs.
     * 
     * @return <code>true</code> if alive check will retry if failed at first attempt, <code>false</code>
     *         to throw an Exception after first attempt (immediate)
     */
    public boolean isRetryDuringAliveCheck()
    {
        return bRetryAlive;
    }
    
	/**
	 * Sets the retry interval in milliseconds. The interval is the wait time between
	 * two requests.
	 * 
	 * @param pInterval the interval
	 */
	public void setRetryInterval(int pInterval)
	{
	    iRetryInterval = pInterval;
	}
	
	/**
	 * Gets the retry interval.
	 * 
	 * @return the interval in milliseconds.
	 * @see #setRetryInterval(int)
	 */
	public int getRetryInterval()
	{
	    return iRetryInterval;
	}

	/**
	 * Checks if a {@link SessionCancelException} occured because this Exception decreases the
	 * communication id.
	 * 
	 * @param pThrowable the exception to check
	 */
	private void checkSessionCancel(Throwable pThrowable)
	{
	    if (pThrowable instanceof SessionCancelException)
	    {
            decreaseCommunicationId();
	    }
	}
		
	/**
	 * Checks if a {@link SessionExpiredException} occured because this Exception prevents further calls.
	 * 
	 * @param pConnectionInfo the connection info
	 * @param pThrowable the exception to check
	 */
	private void checkSessionExpired(ConnectionInfo pConnectionInfo, Throwable pThrowable)
	{
		if (pThrowable instanceof SessionExpiredException)
		{
			String[] sInfo = pThrowable.getMessage().split("'");
			
			if (sInfo.length == 3)
			{
				if (CommonUtil.equals(sInfo[1], pConnectionInfo.getConnectionId()))
				{
					setExpired(pConnectionInfo, true);
				}
			}
			else
			{
				setExpired(pConnectionInfo, true);
			}
		}
	}

	/**
	 * Decreases the communication id by 1.
	 */
	private void decreaseCommunicationId()
	{
        lCommunicationId--;
	}
	
	/**
	 * Gets the current serializer.
	 * 
	 * @return the serializer
	 */
	protected ISerializer getSerializer()
	{
	    return serializer;
	}

	/**
	 * Whether this connection should read the magic byte sequence.
	 * 
	 * @return <code>true</code>
	 */
	protected boolean isReadingMagicByteEnabled()
	{
	    return true;
	}
	
    /**
     * Whether this connection should write the magic byte sequence.
     * 
     * @return <code>true</code>
     */
	protected boolean isWritingMagicByteEnabled()
	{
	    return true;
	}
	
	/**
	 * Gets whether automatic EOF should be detected for response stream. This option needs {@link #isReadingMagicByteEnabled()}
	 * to be enabled. The default implementation doesn't use this option (returns <code>false</code>).
	 * 
	 * @return <code>true</code> if auto EOF detection is enabled, <code>false</code> otherwise.  
	 * @see #isReadingMagicByteEnabled()
	 * @see MagicByteInputStream#MagicByteInputStream(InputStream, byte[], boolean)
	 */
	protected boolean isAutoEOFEnabled()
	{
	    return false;
	}
	
	/**
	 * Prepares the given exception if needed. It's possible to change the stack trace or to create a completely new
	 * exception. 
	 * 
	 * @param pThrowable the original exception
	 * @return the exception to use
	 */
	protected Throwable prepareException(Throwable pThrowable)
	{
	    return pThrowable;
	}
	
    /**
     * Prepares the final exception after last retry. This methods forwards the exception to {@link #prepareException(Throwable)}.
     * 
     * @param pThrowable the original exception
     * @return the exception to use
     */
	protected Throwable prepareAfterRetryException(Throwable pThrowable)
	{
	    return prepareException(pThrowable);
	}
	
	/**
	 * Fills in the current call-stack in the given exception.
	 * 
	 * @param pThrowable the exception from a call
	 * @return the "marked" exception or <code>null</code> if given exception is null
	 */
	protected Throwable fillInStackTrace(Throwable pThrowable)
	{
        if (pThrowable == null)
        {
            return null;
        }
        
        StackTraceElement[] ste = pThrowable.getStackTrace();
        
        Exception e = new Exception();
        
        StackTraceElement[] steCurrent = e.getStackTrace();

        ArrayUtil<StackTraceElement> auElements = new ArrayUtil<StackTraceElement>();
        
        if (ste != null)
        {
            auElements.addAll(ste);
        }

        auElements.add(new StackTraceElement("................................................................", "...................", null, -2));
        
        //remove fillInStackTrace method
        int iStartPos = 1;
        
        if ("prepareException".equals(steCurrent[1].getMethodName()))
        {
            iStartPos++;
        }
        
        for (int i = iStartPos, cnt = steCurrent.length; i < cnt; i++)
        {
            auElements.add(steCurrent[i]);
        }
        
        pThrowable.setStackTrace(auElements.toArray(new StackTraceElement[auElements.size()]));
        
        return pThrowable;
	}
	
	/**
	 * Gets whether the given connection info is marked as expired.
	 * 
	 * @param pConnectionInfo the connection info
	 * @return <code>true</code> if connection info is expired
	 */
	private boolean isExpired(ConnectionInfo pConnectionInfo)
	{
		Object oExp = pConnectionInfo.getProperties().get("#expired"); 
		
		return oExp != null && (oExp instanceof Boolean) && ((Boolean)oExp).booleanValue();
	}
	
	/**
	 * Sets expired state of the given connection info.
	 * 
	 * @param pConnectionInfo the connection info
	 * @param pExpired <code>true</code> to set the connection info expired, <code>false</code> otherwise
	 */
	private void setExpired(ConnectionInfo pConnectionInfo, boolean pExpired)
	{
		if (pExpired)
		{
			pConnectionInfo.getProperties().put("#expired", Boolean.TRUE, false);
		}
		else
		{
			pConnectionInfo.getProperties().remove("#expired", false);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * The <code>CallBackInfo</code> is a POJO that holds information about asynchronous
     * remote method calls.
     * 
     * @author René Jahn
     */
    private static final class CallBackInfo
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
        /** the connection. */
        private AbstractSerializedConnection connection;
        
    	/** the connection id associated with this call-back info. */
    	private Object oConnectionId;
    	
    	/** callback listener which should get the result of a remote method call. */
    	private ICallBackListener cblListener;
    	
    	/** object name of the remote method call. */
    	private String sObjectName;
    	
    	/** method name of the remote call. */
    	private String sMethodName;

    	/** create time, in millis. */
    	private long lCreated = System.currentTimeMillis();
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Creates a new instance of <code>CallBackInfo</code> which holds the information for
    	 * an asynchronous remote method call.
    	 * 
    	 * @param pConnection the associated connection
    	 * @param pConnectionId the connection id associated with this call-back information
    	 * @param pObjectName object name for the remote method call
    	 * @param pMethodName method name for the remote call
    	 * @param pListener callback listener which should get the result of a remote method call
    	 */
    	private CallBackInfo(AbstractSerializedConnection pConnection, Object pConnectionId, 
    	                     String pObjectName, String pMethodName, ICallBackListener pListener)
    	{
    	    connection = pConnection;
    		oConnectionId = pConnectionId;
    		sObjectName  = pObjectName;
    		sMethodName  = pMethodName;
    		cblListener = pListener;
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Gets the connection.
    	 * 
    	 * @return the connection
    	 */
    	private AbstractSerializedConnection getConnection()
    	{
    	    return connection;
    	}
    	
    	/**
    	 * Returns the connection id associated with this information object.
    	 * 
    	 * @return the connection id
    	 */
    	private Object getConnectionId()
    	{
    		return oConnectionId;
    	}
    	
    	/**
    	 * Returns the object name of the remote method call.
    	 * 
    	 * @return object name
    	 */
    	private String getObjectName()
    	{
    		return sObjectName;
    	}
    	
    	/**
    	 * Returns the method name of the remote call.
    	 * 
    	 * @return method name
    	 */
    	private String getMethodName()
    	{
    		return sMethodName;
    	}
    	
    	/**
    	 * Gets the listener which should get the result of a remote method call.
    	 * 
    	 * @return callback listener
    	 */
    	private ICallBackListener getCallBackListener()
    	{
    		return cblListener;
    	}
    	
    	/**
    	 * Gets the creation time of the object.
    	 * 
    	 * @return create time in millis
    	 */
    	private long getCreateTime()
    	{
    		return lCreated;
    	}
    	
    }	// CallBackInfo
    
}	// AbstractSerializedConnection
