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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;

import com.sibvisions.rad.ui.vaadin.ext.data.AbstractDataBookDataProvider;
import com.vaadin.data.provider.Query;

/**
 * The {@link RowIndexDataProvider} is an {@link AbstractDataBookDataProvider}
 * which returns the indexes of the selected rows as {@link Integer}s.
 * 
 * @author Robert Zenz
 */
public class RowIndexDataProvider extends AbstractDataBookDataProvider<Integer>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link RowIndexDataProvider}.
	 *
	 * @param pDataBook the {@link IDataBook}.
	 */
	public RowIndexDataProvider(IDataBook pDataBook)
	{
		super(pDataBook);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Stream<Integer> getItems(Query<Integer, Object> pQuery, int pStartRowIndex, int pEndRowIndex) throws ModelException
	{
		List<Integer> indexes = new ArrayList<Integer>(pEndRowIndex - pStartRowIndex);
		
		for (int index = pStartRowIndex; index < pEndRowIndex; index++)
		{
			indexes.add(Integer.valueOf(index));
		}
		
		return indexes.stream();
	}
	
}	// RowIndexDataProvider
