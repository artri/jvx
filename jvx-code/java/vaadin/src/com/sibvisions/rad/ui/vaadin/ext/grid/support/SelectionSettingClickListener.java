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
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

/**
 * The {@link SelectionSettingClickListener} is a {@link RendererClickListener}
 * implementation which sets the selection to the {@link ITableControl}.
 * <p>
 * Extending classes should override the
 * {@link #onClick(com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent, ITableControl, IDataBook, String, int)}
 * method.
 * 
 * @author Robert Zenz
 */
public class SelectionSettingClickListener implements RendererClickListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The associated {@link ITableControl}. */
	private ITableControl tableControl;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link SelectionSettingClickListener}.
	 *
	 * @param pTableControl the {@link ITableControl}.
	 */
	public SelectionSettingClickListener(ITableControl pTableControl)
	{
		tableControl = pTableControl;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void click(RendererClickEvent pEvent)
	{ 
		try
		{
			onClick(
					pEvent,
					tableControl,
					tableControl.getDataBook(),
					(String)pEvent.getColumn().getId(),
					((Integer)pEvent.getItem()).intValue());
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked on a click event.
	 * 
	 * @param pEvent the original {@link RendererClickEvent}.
	 * @param pTableControl the {@link ITableControl}.
	 * @param pDataBook the {@link IDataBook}.
	 * @param pColumnName the column name.
	 * @param pRowIndex the row index.
	 * @throws ModelException when working with the {@link IDataBook} failed.
	 */
	public void onClick(
			RendererClickEvent pEvent,
			ITableControl pTableControl,
			IDataBook pDataBook,
			String pColumnName,
			int pRowIndex) throws ModelException
	{
		pDataBook.setSelectedRow(pRowIndex);
		pDataBook.setSelectedColumn(pColumnName);
	}
	
}	// SelectionSettingClickListener
