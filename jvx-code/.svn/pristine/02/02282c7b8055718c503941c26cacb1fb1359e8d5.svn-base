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
 * 06.10.2017 - [RZ] - Craetion
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.databinding;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;

import com.vaadin.server.Setter;

/**
 * The {@link DataBookSelectionSetter} is a {@link Setter} implementation which
 * "just" updates the selection in the {@link IDataBook}.
 * 
 * @author Robert Zenz
 */
public class DataBookSelectionSetter implements Setter<Integer, Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link IDataBook}. */
	private IDataBook dataBook = null;
	
	/**
	 * Creates a new instance of {@link DataBookSelectionSetter}.
	 *
	 * @param pDataBook the {@link IDataBook}.
	 */
	public DataBookSelectionSetter(IDataBook pDataBook)
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
	public void accept(Integer pIndex, Object pFieldValue)
	{
		try
		{
			int index = pIndex.intValue();
			
			if (index != dataBook.getSelectedRow())
			{
				dataBook.setSelectedRow(index);
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
}	// DataBookSelectionSetter
