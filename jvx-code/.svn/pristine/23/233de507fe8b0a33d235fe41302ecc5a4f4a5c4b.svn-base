/*
 * Copyright 2015 SIB Visions GmbH
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
 * 28.07.2015 - [JR] - creation
 */
package com.sibvisions.rad.persist;

import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.event.DataBookEvent;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * The <code>MemDataBookStorage</code> is a simple {@link AbstractMemStorage} without specific insert/update/delete
 * implementation. All calls are forwarded to the external {@link MemDataBook}.
 * 
 * @author René Jahn
 */
public class MemDataBookStorage extends AbstractMemStorage
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>MemDataBookStorage</code>.
     * 
     * @param pBook the data book
     */
    public MemDataBookStorage(MemDataBook pBook)
    {
        super(pBook);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public final RowDefinition getRowDefinition() throws ModelException
    {
        return (RowDefinition)getDataBook().getRowDefinition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadData(MemDataBook pBook, ICondition pFilter) throws ModelException
    {
        pBook.setFilter(pFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(DataBookEvent pEvent) throws ModelException
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(DataBookEvent pEvent) throws ModelException
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(DataBookEvent pEvent) throws ModelException
    {
    }
    
}   // MemDataBookStorage
