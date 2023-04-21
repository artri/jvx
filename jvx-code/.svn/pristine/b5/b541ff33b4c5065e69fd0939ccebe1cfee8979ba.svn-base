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
 * 23.11.2015 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.support;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;

import com.sibvisions.util.type.CommonUtil;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

/**
 * The {@link CheckBoxRendererClickListener} is a
 * {@link SelectionSettingClickListener} extension which alternates between the
 * values of an {@link ICheckBoxCellEditor} by setting it on the associated
 * {@link IDataBook} .
 * 
 * @author Robert Zenz
 */
public class CheckBoxRendererClickListener extends SelectionSettingClickListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The associated {@link ICheckBoxCellEditor}. */
	private ICheckBoxCellEditor<?> checkBoxCellEditor;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link CheckBoxRendererClickListener}.
	 *
	 * @param pTableControl the {@link ITableControl table control}.
	 * @param pCheckBoxCellEditor the {@link ICheckBoxCellEditor check box cell
	 *            editor}.
	 */
	public CheckBoxRendererClickListener(ITableControl pTableControl, ICheckBoxCellEditor pCheckBoxCellEditor)
	{
		super(pTableControl);
		
		checkBoxCellEditor = pCheckBoxCellEditor;
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
			if (CommonUtil.equals(pDataBook.getValue(pColumnName), checkBoxCellEditor.getSelectedValue()))
			{
				pDataBook.setValue(pColumnName, checkBoxCellEditor.getDeselectedValue());
			}
			else
			{
				pDataBook.setValue(pColumnName, checkBoxCellEditor.getSelectedValue());
			}
		}
	}
	
}	// CheckBoxRendererClickListener
