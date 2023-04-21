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
 * 16.02.2021 - [DJ] - creation
 */
package javax.rad.persist;

import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.type.bean.IBean;

/**
 * The <code>ISubStorage</code> interface defines sub storage specific methods.
 * 
 * @author Jozef Dorko
 */
public interface ISubStorage extends IStorage
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates unique sub storage name.
     * 
     * @return the sub storage name.
     */
    public String createSubStorageName();
    
    /**
     * Returns the requested list of beans from the storage.
     * It uses the filter to reduce the result, and the sort for the order.
     * 
     * @param pFilter the filter as <code>ICondition</code> to use
     * @param pSort the <code>SortDefinition</code> to use
     * @param pFromRow the from row index to request from storage
     * @param pMinimumRowCount the minimum row count to request, beginning from the pFromRow.
     * @return the requested list of Beans from the storage.
     * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage.
     */
    public List<IBean> fetchBean(ICondition pFilter, SortDefinition pSort, int pFromRow, int pMinimumRowCount) throws DataSourceException;
    
}   // ISubStorage
