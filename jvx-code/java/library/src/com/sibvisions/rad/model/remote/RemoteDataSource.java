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
 * 07.05.2009 - [RH] - created 
 * 23.11.2009 - [RH] - ColumnMetaData is replaced with the MetaData class
 * 25.03.2010 - [JR] - #103: cache meta data not static
 * 30.03.2010 - [JR] - #111: cache role
 * 19.08.2011 - [JR] - #459: cache option support
 *                   - putGlobalMetaDataIntern implemented and added reverted deleted check
 *                     in putGlobalMetaData (see svn history, 15.02.2011)
 *                   - setConnection() now clears the local metadata cache          
 * 05.06.2013 - [JR] - #669: remove "empty" metadata from cache
 * 06.06.2013 - [JR] - #670: cache metadata per connection (MasterConnection)                               
 */
package com.sibvisions.rad.model.remote;

import java.util.Hashtable;
import java.util.WeakHashMap;

import javax.rad.application.IConnectable;
import javax.rad.model.MetaDataCacheOption;
import javax.rad.model.ModelException;
import javax.rad.persist.MetaData;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SubConnection;
import javax.rad.remote.event.CallErrorEvent;
import javax.rad.remote.event.CallEvent;
import javax.rad.remote.event.ConnectionEvent;
import javax.rad.remote.event.IConnectionListener;
import javax.rad.remote.event.PropertyEvent;

import com.sibvisions.rad.model.mem.MemDataSource;

/**
 * The <code>RemoteDataSource</code> is a remote DataSource for all RemoteDataBook's,
 * which uses a connection to the server to forward the storage operation to server objects.
 * 
 * @see com.sibvisions.rad.model.remote.RemoteDataBook  
 * @see com.sibvisions.rad.model.mem.MemDataSource
 * @author Roland H�rmann
 */
public class RemoteDataSource extends MemDataSource
                              implements IConnectable
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The meta data cache roles. */
	public enum MetaDataCacheRole
	{
		/** Global caching, over all data source. */
		Global,
		/** Cache per data source instance. */
		DataSource,
		/** No caching of meta data. */
		Off;
		
		/**
		 * Resolves the cache role specified by a string. The detection is case insensitive.
		 * It's possible to resolve <code>GLOBAL</code> as {@link MetaDataCacheRole#Global}.
		 * 
		 * @param pName the role name
		 * @return the cache role
		 * @throws IllegalArgumentException if <code>pName</code> is <code>null</code> or the name is unknown
		 */
		public static MetaDataCacheRole resolve(String pName)
		{
			if (pName != null)
			{
				String sName = pName.toUpperCase();
				
				if ("GLOBAL".equals(sName))
				{
					return Global;
				}
				else if ("DATASOURCE".equals(sName))
				{
					return DataSource;
				}
				else if ("OFF".equals(sName))
				{
					return Off;
				}
			}
			
			throw new IllegalArgumentException("Unknown enum: " + pName);
		}
	}
	
    /** Global MetaData Client Cache. */
    private static WeakHashMap<AbstractConnection, Hashtable<String, MetaData>> hmpGlobalMetaDataCache = null;
    
    /** the cache role for meta data caching (default: {@link MetaDataCacheRole#DataSource}). */
    private static MetaDataCacheRole cacheRole = MetaDataCacheRole.DataSource;
    
	/** Connection to the server for the net communication. */
	private transient AbstractConnection acConnection;
	
	/** The {@link IConnectionListener} that closes the {@link RemoteDataSource} if the connection is closed. */
	private transient IConnectionListener connectionListener = null;
	
	/** Local MetaData Client Cache. */
	private transient Hashtable<String, MetaData> htLocalMetaDataCache = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * It constructs a new RemoteDataSource.
	 */
	public RemoteDataSource()
	{
		super();
		
		connectionListener = new ClosingConnectionListener(this);
	}
	
	/**
	 * It constructs a new RemoteDataSource with the given AbstractConnection.
	 *  
	 * @param pConnection	the AbstractConnection to use for the communication to the server.
	 */
	public RemoteDataSource(AbstractConnection pConnection)
	{
		this();
		
		acConnection = pConnection;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public AbstractConnection getConnection()
	{
		return acConnection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConnection(AbstractConnection pConnection)
	{
		if (acConnection != null)
		{
			acConnection.removeConnectionListener(connectionListener);
		}
		if (acConnection != pConnection)
		{
			clearMetaData();
		}
		
		acConnection = pConnection;

		if (acConnection != null)
		{
			acConnection.addConnectionListener(connectionListener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isConnected()
	{
		return acConnection != null && acConnection.isOpen();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws ModelException
	{
		if (!isOpen())
		{
			if (acConnection == null)
			{
				throw new ModelException("Connection is null! -> open not possible");
			}
			
			super.open();
			
			// Make sure that the listener can't be added twice, no matter what.
			acConnection.removeConnectionListener(connectionListener);
			acConnection.addConnectionListener(connectionListener);
		}
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public synchronized void close()
	{
		acConnection.removeConnectionListener(connectionListener);
		
		super.close();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the meta data from the client cache.
	 * 
	 * @param pKey is the name of the meta data object 
	 * @return the {@link MetaData} from the client cache
	 */
	public MetaData getMetaData(String pKey)
	{
		MetaDataCacheOption option = getMetaDataCacheOption();
		
		if (option == MetaDataCacheOption.Default || option == MetaDataCacheOption.On)
		{
			if (cacheRole == MetaDataCacheRole.Global || option == MetaDataCacheOption.On)
			{
				return getGlobalMetaData(acConnection, pKey);
			}
			else if (cacheRole == MetaDataCacheRole.DataSource)
			{
				if (htLocalMetaDataCache == null)
				{
					return null;
				}
				
				return htLocalMetaDataCache.get(pKey);
			}
		}
		
		return null;
	}

	/**
	 * Puts the meta data to the client cache.
	 * 
	 * @param pKey is the name of the meta data object
	 * @param pMetaData the {@link MetaData} to cache
	 */
	public void putMetaData(String pKey, MetaData pMetaData)
	{
		MetaDataCacheOption option = getMetaDataCacheOption();
		
		if (option == MetaDataCacheOption.Default || option == MetaDataCacheOption.On)
		{
			if (cacheRole == MetaDataCacheRole.Global || option == MetaDataCacheOption.On)
			{
				putGlobalMetaDataIntern(acConnection, pKey, pMetaData);
			}
			else if (cacheRole == MetaDataCacheRole.DataSource)
			{
				if (htLocalMetaDataCache == null)
				{
					htLocalMetaDataCache = new Hashtable<String, MetaData>();
				}
				
				htLocalMetaDataCache.put(pKey, pMetaData);
			}
		}
	}

	/**
	 * Clears the local meta data cache.
	 */
	public void clearMetaData()
	{
		if (htLocalMetaDataCache != null)
		{
			htLocalMetaDataCache.clear();
		}
	}
	
	/**
	 * Returns the meta data from the global client cache.
	 * 
	 * @param pConnection the connection associated with cached metadata 
	 * @param pKey is the name of the meta data object 
	 * @return the {@link MetaData} from the client cache
	 */
	public static MetaData getGlobalMetaData(AbstractConnection pConnection, String pKey)
	{
		if (hmpGlobalMetaDataCache == null)
		{
			return null;
		}
		
		if (pConnection == null)
		{
			return null;
		}
		
		Hashtable<String, MetaData> htCache = hmpGlobalMetaDataCache.get(getConnection(pConnection));
		
		if (htCache == null)
		{
			return null;
		}
		
		return htCache.get(pKey);
	}

	/**
	 * Puts the meta data to the global client cache.
	 * 
	 * @param pConnection the connection associated with cached metadata 
	 * @param pKey is the name of the meta data object
	 * @param pMetaData the {@link MetaData} to cache
	 */
	public static void putGlobalMetaData(AbstractConnection pConnection, String pKey, MetaData pMetaData)
	{
		if (cacheRole == MetaDataCacheRole.Global)
		{
			putGlobalMetaDataIntern(pConnection, pKey, pMetaData);
		}
	}
	
	/**
	 * Puts the meta data to the global client cache, without checks.
	 * 
	 * @param pConnection the connection associated with cached metadata 
	 * @param pKey is the name of the meta data object
	 * @param pMetaData the {@link MetaData} to cache
	 */
	private static void putGlobalMetaDataIntern(AbstractConnection pConnection, String pKey, MetaData pMetaData)
	{
		//no connection -> no caching
		if (pConnection == null)
		{
			return;
		}
		
		Hashtable<String, MetaData> htCache;
		
		if (hmpGlobalMetaDataCache == null)
		{
			hmpGlobalMetaDataCache = new WeakHashMap<AbstractConnection, Hashtable<String, MetaData>>();
		}

		AbstractConnection con = getConnection(pConnection);
		
		htCache = hmpGlobalMetaDataCache.get(con);
		
		if (pMetaData.getColumnNames().length == 0)
		{
			if (htCache != null)
			{
				htCache.remove(pKey);
			}
		}
		else
		{
			if (htCache == null)
			{
				htCache = new Hashtable<String, MetaData>();
				
				hmpGlobalMetaDataCache.put(con, htCache);
			}
			
			htCache.put(pKey, pMetaData);
		}
	}

	/**
	 * Clears the complete meta data cache.
	 */
	public static void clearGlobalMetaData()
	{
		clearGlobalMetaData(null);
	}
	
	/**
	 * Clears the meta data cache for the given application.
	 * 
	 * @param pConnection the connection associated with cached metadata 
	 */
	public static void clearGlobalMetaData(AbstractConnection pConnection)
	{
		if (hmpGlobalMetaDataCache != null)
		{
			AbstractConnection con = getConnection(pConnection);
			
			if (con == null)
			{
				hmpGlobalMetaDataCache.clear();
			}
			else
			{
				hmpGlobalMetaDataCache.remove(con);
			}
		}
	}
	
	/**
	 * Sets the global meta data cache role.
	 * 
	 * @param pCacheRole the cache role to use
	 */
	public static void setMetaDataCacheRole(MetaDataCacheRole pCacheRole)
	{
		cacheRole = pCacheRole;
	}
	
	/**
	 * Gets whether the global or the local cache is used.
	 *  
	 * @return <code>true</code> means that the global cache is enabled
	 */
	public static MetaDataCacheRole getMetaDataCacheRole()
	{
		return cacheRole;
	}
	
	/**
	 * Gets the metadata cache option from the connection.
	 * 
	 * @return the cache option
	 */
	public MetaDataCacheOption getMetaDataCacheOption()
	{
		Object oOption;
		
		try
		{
			oOption = acConnection.getProperty(IConnectionConstants.METADATA_CACHEOPTION);
		}
		catch (Throwable th)
		{
			//should not happen because this property does not need a remote call
			oOption = null;
		}
			
		if (oOption == null)
		{
			return MetaDataCacheOption.Default;
		}
		else
		{
			return MetaDataCacheOption.resolve((String)oOption);
		}
	}

	/**
	 * Gets the master connection for the given connection. If the given connectio already is the
	 * the master connection, it will be returned.
	 * 
	 * @param pConnection the connection
	 * @return the master connection
	 */
	private static AbstractConnection getConnection(AbstractConnection pConnection)
	{
		if (pConnection instanceof SubConnection)
		{
			return ((SubConnection)pConnection).getMasterConnection();
		}
		
		return pConnection;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ClosingConnectionListener} is an {@link IConnectionListener}
	 * implementation that closes a given {@link RemoteDataSource} if
	 * the {@link IConnectionListener#connectionClosed(ConnectionEvent)} method
	 * is invoked.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ClosingConnectionListener implements IConnectionListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The data source. */
		private RemoteDataSource dataSource;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ClosingConnectionListener}.
		 * 
		 * @param pDataSource the data source.
		 */
		public ClosingConnectionListener(RemoteDataSource pDataSource)
		{
			dataSource = pDataSource;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void actionCalled(CallEvent pEvent)
		{
			// Not needed
		}

		/**
		 * {@inheritDoc}
		 */
		public void callError(CallErrorEvent pEvent)
		{
			// Not needed
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionClosed(ConnectionEvent pEvent)
		{
			dataSource.close();
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionOpened(ConnectionEvent pEvent)
		{
			// Not needed
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionReOpened(ConnectionEvent pEvent)
		{
			// Not needed
		}

		/**
		 * {@inheritDoc}
		 */
		public void objectCalled(CallEvent pEvent)
		{
			// Not needed
		}

		/**
		 * {@inheritDoc}
		 */
		public void propertyChanged(PropertyEvent pEvent)
		{
			// Not needed
		}
		
	}	// ClosingConnectionListener
	
} // RemoteDataSource
