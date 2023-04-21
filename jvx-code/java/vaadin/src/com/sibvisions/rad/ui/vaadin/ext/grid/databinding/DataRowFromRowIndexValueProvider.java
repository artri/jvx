/*
 * Copyright 2017 SIB Visions GmbH
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
 * 06.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.databinding;

import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;

import com.vaadin.data.ValueProvider;

/**
 * The {@link DataRowFromRowIndexValueProvider} is an {@link ValueProvider} implementation
 * which provides an {@link IChangeableDataRow}.
 * 
 * @author Robert Zenz
 */
public class DataRowFromRowIndexValueProvider implements ValueProvider<Integer, IChangeableDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cached {@link IChangeableDataRow}. */
	private IChangeableDataRow cachedDataRow = null;
	
	/** The index of the current row for which the cache is valid. */
	private int cachedIndex = -1;
	
	/** The backing {@link IDataBook}. */
	private IDataBook dataBook = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataRowFromRowIndexValueProvider}.
	 *
	 * @param pDataBook the {@link IDataBook}.
	 */
	public DataRowFromRowIndexValueProvider(IDataBook pDataBook)
	{
		super();
		
		dataBook = pDataBook;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChangeableDataRow apply(Integer pSource)
	{
		if (pSource == null)
		{
			return null;
		}
		
		checkCache(pSource.intValue());
		
		return cachedDataRow;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the cache is invalid, if yes the current/new/valid value is
	 * cached.
	 * 
	 * @param pRequestedIndex the index that has been requested.
	 */
	protected void checkCache(int pRequestedIndex)
	{
		if (cachedIndex != pRequestedIndex)
		{
			try
			{
				cachedDataRow = dataBook.getDataRow(pRequestedIndex);
				cachedIndex = pRequestedIndex;
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
}	// DataRowFromRowIndexValueProvider
