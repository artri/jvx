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
 * 20.09.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.grid;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.widget.grid.DefaultEditorEventHandler;
import com.vaadin.client.widgets.Grid.Column;
import com.vaadin.client.widgets.Grid.EditorDomEvent;

/**
 * The {@link JVxEditorEventHandler} is a {@link DefaultEditorEventHandler}
 * extension which correctly propagates the selection if a different row is
 * edited.
 * 
 * @author Robert Zenz
 * @param <T> the type.
 */
public class JVxEditorEventHandler<T> extends DefaultEditorEventHandler<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JVxEditorEventHandler}.
	 */
	public JVxEditorEventHandler()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void editRow(EditorDomEvent<T> pEvent, int pRowIndex, int pColIndex)
	{
		if (pEvent.getGrid().isEditorActive())
		{
			// If the editor is active, we must trigger all editors that they commit
			// their changes to the server.
			for (Column<?, T> column : pEvent.getGrid().getColumns())
			{
				Widget editorWidget = pEvent.getGrid().getEditorWidget(column);
				
				if (editorWidget instanceof ChangeHandler)
				{
					((ChangeHandler)editorWidget).onChange(null);
				}
			}
		}
		
		// Set the next-to-be-edited row as selected row.
		// This will also be send to the server.
		pEvent.getGrid().select(pEvent.getGrid().getDataSource().getRow(pRowIndex));
		
		super.editRow(pEvent, pRowIndex, pColIndex);
	}
	
}	// JVxEditorEventHandler
