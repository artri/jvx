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
 * 12.02.2021 - [HM] - creation
 */
package javax.rad.persist;

import java.util.Map;

/**
 * The <code>ILinkableStorage</code> extends the {@link IStorage} with the possibility of defining <code>StorageReferenceDefinition</code>.
 * 
 * @author Martin Handsteiner
 */
public interface ILinkableStorage extends IStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Adds a sub storage to the internal list of all sub storages.
     * 
     * @param pSubStorage   the sub storage to use.
     * @return  the storage name to use.
     */
    public String putSubStorage(ISubStorage pSubStorage);
    
    /**
     * Gets a sub storage from the internal list of all sub storages.
     * 
     * @param pStorageName  the storage name to use.
     * @return previous sub storage which was associated with the storage name  
     */
    public ISubStorage getSubStorage(String pStorageName);
	
    /**
     * Gets all known sub storages as key/value pair.
     * 
     * @return the key/value pair with subtablename and substorage or <code>null</code> if no substorages are
     *         known
     */
    public Map<String, ISubStorage> getSubStorages();
    
    /**
     * Creates and sets a new <code>StorageReferenceDefinition</code> with the specified <code>ISubStorage</code> and
     * columns and reference columns on all <code>pColumns</code>. 
     * 
     * @param pColumns          the columns to use.
     * @param pStorage          the <code>ISubStorage</code> to use.
     * @param pReferenceColumns the reference columns to use.
     * @throws DataSourceException  if the <code>StorageReferenceDefinition</code> couldn't created.
     */
    public void createAutomaticLinkReference(String[] pColumns, ISubStorage pStorage, String[] pReferenceColumns) throws DataSourceException;

} 	// ILinkableStorage
