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
 * 27.03.2010 - [JR] - creation
 * 15.02.2011 - [JR] - #287: removed fetch method
 */
package javax.rad.persist;

import java.util.Hashtable;

/**
 * The <code>ICachedStorage</code> extends the {@link IStorage} with meta data caching 
 * features. The meta data of storages doesn't change in production mode, so we will
 * use a caching mechanism.
 * 
 * @author René Jahn
 */
public interface ICachedStorage extends IStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the meta data for this AbstractStorage from the storage as <code>MetaData</code> and places
	 * the MetaData to the cache with a group name and meta data name.
	 * 
	 * @param pGroup the cache group name
	 * @param pName the name for the meta data in the cache group
	 * @return the meta data for this AbstractStorage from the storage as <code>MetaData</code>.
	 * @throws DataSourceException if an <code>Exception</code> occur during getting the meta data from the storage 
	 */
	public MetaData getMetaData(String pGroup, String pName) throws DataSourceException;
	
	/**
	 * Returns all available meta data from the cache for a specific cache group.
	 * 
	 * @param pGroup the cache group name
	 * @return a {@link Hashtable} with the name of the meta data in the cache group and the meta data as value
	 */
	public Hashtable<String, MetaData> getMetaDataFromCache(String pGroup);
	
} 	// ICachedStorage
