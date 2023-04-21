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
package com.sibvisions.rad.ui.vaadin.ext.grid.field;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.control.ICellFormatter;

import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * The {@link CellEditorField} is a {@link CustomField} extension which
 * encapsulates an {@link ICellEditor}.
 * 
 * @author Robert Zenz
 */
public class CellEditorField extends CustomField<Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ICellEditor}. */
	private ICellEditor cellEditor;
	
	/** The created {@link ICellEditorHandler}. */
	private ICellEditorHandler<?> cellEditorHandler;
	
	/** The name of the column. */
	private String columnName;
	
	/** The underlying {@link IDataBook}. */
	private IDataBook dataBook;
	
	/** The parent {@link IControl}. */
	private IControl control;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link CellEditorField}.
	 *
	 * @param pCellEditor the {@link ICellEditor}.
	 * @param pControl the {@link IControl}.
	 * @param pDataBook the {@link IDataBook}.
	 * @param pColumnName the {@link String column name}.
	 */
	public CellEditorField(ICellEditor pCellEditor, IControl pControl, IDataBook pDataBook, String pColumnName)
	{
		super();
		
		cellEditor = pCellEditor;
		control = pControl;
		dataBook = pDataBook;
		columnName = pColumnName;
		
		cellEditorHandler = cellEditor.createCellEditorHandler(new BasicCellEditorListener(), dataBook, columnName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component initContent()
	{
		Component content = (Component)cellEditorHandler.getCellEditorComponent();
		content.setSizeFull();
		
		return content;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear()
	{
		try
		{
			cellEditorHandler.cancelEditing();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doSetValue(Object pValue)
	{
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue()
	{
		// Nothing to do.
		
		return null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Commits all changes.
	 * 
	 * @see ICellEditorHandler#saveEditing()
	 */
	public void commit()
	{
		try
		{
			cellEditorHandler.saveEditing();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Discards all changes.
	 * 
	 * @see ICellEditorHandler#cancelEditing()
	 */
	public void discard()
	{
		try
		{
			cellEditorHandler.cancelEditing();
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link BasicCellEditorListener} is an {@link ICellEditorListener} and
	 * {@link ICellFormatterEditorListener} implementation which does only the
	 * basics.
	 * 
	 * @author Robert Zenz
	 */
	private final class BasicCellEditorListener implements ICellEditorListener, ICellFormatterEditorListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link BasicCellEditorListener}.
		 */
		public BasicCellEditorListener()
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void editingComplete(String pCompleteType)
		{
			//			commit();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void editingStarted()
		{
			// Not needed, there for empty.
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ICellFormatter getCellFormatter()
		{
			// Not needed, there for empty.
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IControl getControl()
		{
			return control;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isSavingImmediate()
		{
			// Not needed, there for empty.
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setCellFormatter(ICellFormatter pCellFormatter)
		{
			// Not needed, there for empty.
		}
		
	}	// BasicCellEditorListener
	
}	// CellEditorField
