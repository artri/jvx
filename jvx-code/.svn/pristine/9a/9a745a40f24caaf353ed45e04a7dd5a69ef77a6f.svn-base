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
 * 11.09.2015 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.support;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.celleditor.IChoiceCellEditor;

import com.sibvisions.util.ArrayUtil;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

/**
 * The {@link ChoiceRendererClickListener} is a {@link RendererClickListener}
 * implementation which selects the next value of an {@link IChoiceCellEditor}
 * by setting it on the associated {@link IDataBook}.
 * 
 * @author Robert Zenz
 */
public class ChoiceRendererClickListener extends SelectionSettingClickListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The associated {@link IChoiceCellEditor}. */
	private IChoiceCellEditor choiceCellEditor;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ChoiceRendererClickListener}.
	 *
	 * @param pTableControl the {@link ITableControl table control}.
	 * @param pChoiceCellEditor the {@link IChoiceCellEditor choice cell
	 *            editor}.
	 */
	public ChoiceRendererClickListener(ITableControl pTableControl, IChoiceCellEditor pChoiceCellEditor)
	{
		super(pTableControl);
		
		choiceCellEditor = pChoiceCellEditor;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(
			RendererClickEvent pEvent,
			ITableControl pTableControl,
			IDataBook pDataBook,
			String pColumnName,
			int pRowIndex) throws ModelException
	{
		super.onClick(pEvent, pTableControl, pDataBook, pColumnName, pRowIndex);
		
		if (!pDataBook.isReadOnly() && pDataBook.isUpdateAllowed())
		{
			pDataBook.setValue(pColumnName, getNextValue(pDataBook.getValue(pColumnName)));
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the next value of the {@link #choiceCellEditor} based on the given
	 * value.
	 * 
	 * @param pCurrentValue the current value.
	 * @return the next value.
	 */
	private Object getNextValue(Object pCurrentValue)
	{
		int index = ArrayUtil.indexOf(choiceCellEditor.getAllowedValues(), pCurrentValue);
		index = index + 1;
		index = index % choiceCellEditor.getAllowedValues().length;
		
		return choiceCellEditor.getAllowedValues()[index];
	}
	
}	// ChoiceRendererClickListener
