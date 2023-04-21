/*
 * Copyright 2016 SIB Visions GmbH
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
 * 20.09.2016 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.support;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ITableControl;

import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

/**
 * The {@link EditingClickListener} is a {@link SelectionSettingClickListener}
 * extension which starts the edit upon double-click.
 * 
 * @author Robert Zenz
 */
public class EditingClickListener extends SelectionSettingClickListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link EditingClickListener}.
	 *
	 * @param pTableControl the {@link ITableControl}.
	 */
	public EditingClickListener(ITableControl pTableControl)
	{
		super(pTableControl);
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
		
		if (pEvent.isDoubleClick())
		{
			pTableControl.startEditing();
		}
	}
	
}	// EditingClickListener
