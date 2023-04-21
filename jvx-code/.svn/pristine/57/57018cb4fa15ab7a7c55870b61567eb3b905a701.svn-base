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
 * 30.10.2010 - [JR] - creation
 * 09.11.2010 - [JR] - added logger support
 * 04.01.2011 - [RH] - #233: Server Side Trigger implementation -> logger, createCSV function moved to AbstractStorage.
 * 16.02.2011 - [JR] - #287: removed fetch with group and name
 * 10.03.2011 - [JR] - #307: check global metadata cache option in constructor
 * 19.08.2011 - [JR] - #459: metadatacache option support
 * 23.11.2011 - [JR] - map metadata groups to application name
 * 06.06.2013 - [JR] - #669: removeMetaData implemented
 */
package com.sibvisions.rad.persist;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.rad.model.MetaDataCacheOption;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.ICachedStorage;
import javax.rad.persist.MetaData;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.GroupHashtable;

/**
 * The <code>AbstractCachedStorage</code> implements the caching mechanism of {@link ICachedStorage}. It
 * is the abstract base class for storages with standard meta data caching.
 * 
 * @author René Jahn
 */
public abstract class AbstractCachedStorage extends AbstractStorage 
                                            implements ICachedStorage 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the mapping between cache groups and the application (appname -> (lco-class -> lco-class.storagename, metadata)). */ 
	private static HashMap<String, GroupHashtable<String, String, MetaData>> hmpMetaDataCache = new HashMap<String, GroupHashtable<String, String, MetaData>>(); 
	
	/** whether the metadata cache is global (not update everytime). */
	private static boolean bGlobalMetaDataCache = false;
	
	/** the metadata cache option for this instance (default: Default). */
	private MetaDataCacheOption mdcCacheOption = MetaDataCacheOption.Default;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractCachedStorage</code>. This constructor checks if the global metadata
	 * cache option is en- or disabled.
	 */
	public AbstractCachedStorage()
	{
		IConfiguration cfg = SessionContext.getCurrentServerConfig();
		
		if (cfg != null)
		{
			String sCaching = cfg.getProperty("/server/globalmetadatacache", "OFF");

			AbstractCachedStorage.setGlobalMetaDataCacheEnabled("ON".equals(sCaching));
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public MetaData getMetaData(String pGroup, String pName) throws DataSourceException
	{
		MetaData md;

		if (isMetaDataCacheEnabled())
		{
			//global cache: don't update metadata after first caching
			md = getMetaDataFromCache(pGroup, pName);
			
			if (md != null)
			{
				return md;
			}
		}
		
		md = getMetaData();

		//update cache
		putMetaDataToCache(pGroup, pName, md);
		
		return md;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, MetaData> getMetaDataFromCache(String pGroup)
	{
		return getMetaData(pGroup);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the current application name from the {@link SessionContext}.
	 * 
	 * @return the application name or <code>null</code> if context is not available
	 */
	private static String getApplicationName()
	{
		ISession sess = SessionContext.getCurrentSession();
		
		if (sess != null)
		{
			return sess.getApplicationName();
		}
		
		return null;
	}
	
	/**
	 * Puts meta data to the cache.
	 * 
	 * @param pGroup the group name e.g. the life-cycle name
	 * @param pName the name in the group 
	 * @param pMetaData the meta data to cache
	 */
	protected void putMetaDataToCache(String pGroup, String pName, MetaData pMetaData)
	{
		if (pGroup != null)
		{
			String sAppName = getApplicationName();
			
			GroupHashtable<String, String, MetaData> ghtMetaData = hmpMetaDataCache.get(sAppName);

			if (pMetaData != null)
			{
				if (ghtMetaData == null)
				{
					ghtMetaData = new GroupHashtable<String, String, MetaData>();
					
					hmpMetaDataCache.put(sAppName, ghtMetaData);
				}
				
				ghtMetaData.put(pGroup, pGroup + "." + pName, pMetaData);
			}
			else if (ghtMetaData != null)
			{
				//remove from cache
				ghtMetaData.remove(pGroup, pGroup + "." + pName);
			}
		}
	}
	
	/**
	 * Gets meta data from the cache.
	 * 
	 * @param pGroup the group name e.g. the life-cycle name
	 * @param pName the name in the group
	 * @return the cached meta data or <code>null</code> if meta data are not cached
	 */
	protected MetaData getMetaDataFromCache(String pGroup, String pName)
	{
		MetaData md = null;
		
		if (pGroup != null)
		{
			GroupHashtable<String, String, MetaData> ghtMetaData = hmpMetaDataCache.get(getApplicationName());
			
			if (ghtMetaData == null)
			{
				return null;
			}
			
			return ghtMetaData.get(pGroup, pGroup + "." + pName);
		}

		return md;
	}
	
	/**
	 * Gets meta data which are cached under a specific name.
	 *   
	 * @param pGroup the cache name
	 * @return the meta data or <code>null</code> if there are no meta data with the name
	 */
	public static Hashtable<String, MetaData> getMetaData(String pGroup)
	{
		if (pGroup != null)
		{
			GroupHashtable<String, String, MetaData> ghtMetaData = hmpMetaDataCache.get(getApplicationName());
			
			if (ghtMetaData == null)
			{
				return null;
			}
			
			Hashtable<String, MetaData> htCache = ghtMetaData.get(pGroup);
			
			if (htCache != null)
			{
				return (Hashtable<String, MetaData>)htCache.clone();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the available metadata cache groups for the given application.
	 *  
	 * @param pApplicationName the application name
	 * @return the available cache groups
	 */
	public static List<String> getMetaDataCacheGroups(String pApplicationName)
	{
		GroupHashtable<String, String, MetaData> ghtMetaData = hmpMetaDataCache.get(getApplicationName());
		
		if (ghtMetaData == null)
		{
			return null;
		}			

		List<String> liGroups = new ArrayUtil<String>();
		
		for (Enumeration<String> en = ghtMetaData.groups(); en.hasMoreElements();)
		{
			liGroups.add(en.nextElement());
		}

		return liGroups;
	}
	
	/**
	 * Removes all metadata for the given group.
	 * 
	 * @param pGroup the group name
	 * @return <code>true</code> if metadata were removed, <code>false</code> otherwise
	 */
	public static boolean removeMetaData(String pGroup)
	{
		if (pGroup != null)
		{
			GroupHashtable<String, String, MetaData> ghtMetaData = hmpMetaDataCache.get(getApplicationName());
			
			if (ghtMetaData == null)
			{
				return false;
			}			
			
			return ghtMetaData.remove(pGroup);
		}
		
		return false;
	}
	
	/**
	 * Clears the meta data cache.
	 */
	public static void clearMetaData()
	{
		hmpMetaDataCache.clear();
	}
	
	/**
	 * Clears the meta data cache for the given application.
	 * 
	 * @param pApplicationName the application name
	 */
	public static void clearMetaData(String pApplicationName)
	{
		hmpMetaDataCache.remove(pApplicationName);
	}
	
	/**
	 * Sets the global meta data cache en-/disabled. If it is enabled, then the meta data won't be updated
	 * after they are in the cache.
	 * 
	 * @param pEnabled <code>true</code> to enable meta data cache, <code>false</code> otherwise
	 */
	public static void setGlobalMetaDataCacheEnabled(boolean pEnabled)
	{
		//switch from Cache-ON to Cache-OFF -> clear cache
		if (bGlobalMetaDataCache && !pEnabled)
		{
			clearMetaData();
		}
		
		bGlobalMetaDataCache = pEnabled;
	}
	
	/**
	 * Gets whether the global meta data cache is enabled.
	 * 
	 * @return <code>true</code> if meta data cache is enabled, <code>false</code> otherwise
	 */
	public static boolean isGlobalMetaDataCacheEnabled()
	{
		return bGlobalMetaDataCache;
	}

	/**
	 * Sets the metadata cache option for this instance. If a session cache option is set, the
	 * instance cache option overrules the session setting.
	 * 
	 * @param pOption the metadata cache option 
	 * @see #isMetaDataCacheEnabled()
	 */
	public void setMetaDataCacheOption(MetaDataCacheOption pOption)
	{
		if (pOption == null)
		{
			mdcCacheOption = MetaDataCacheOption.Default;
		}
		else
		{
			mdcCacheOption = pOption;
		}
	}
	
	/**
	 * Gets the metadata cache option for this instance.
	 * 
	 * @return the metadata cache option
	 * @see #setMetaDataCacheOption(MetaDataCacheOption)
	 */
	public MetaDataCacheOption getMetaDataCacheOption()
	{
		return mdcCacheOption;
	}

	/**
	 * Gets whether the meta data cache should be used.
	 * 
	 * @return <code>true</code> when the metadata cache should be used. This means that
	 *         either the instance cache option is {@link MetaDataCacheOption#On}, the session cache option is {@link MetaDataCacheOption#On} 
	 *         or the cache options are set to {@link MetaDataCacheOption#Default} and the global meta data cache is
	 *         enabled.<br>
	 *         The instance cache option overrules the session cache option.
	 */
	protected boolean isMetaDataCacheEnabled()
	{
		MetaDataCacheOption option;
		
		//instance option overrules session option!
		
		if (mdcCacheOption != MetaDataCacheOption.Default)
		{
			option = mdcCacheOption;
		}
		else
		{
			//get the cache option from the session
			ISession sess = SessionContext.getCurrentSession();
			
			if (sess != null)
			{
				Object oOption = (String)sess.getProperty(IConnectionConstants.METADATA_CACHEOPTION);
				
				if (oOption == null)
				{
					option = MetaDataCacheOption.Default;
				}
				else
				{
					option = MetaDataCacheOption.resolve((String)oOption);
				}
			}
			else
			{
				option = MetaDataCacheOption.Default;
			}
		}
		
		return option == MetaDataCacheOption.On || (bGlobalMetaDataCache && option == MetaDataCacheOption.Default);		
	}
	
}	// AbstractCachedMetaData
