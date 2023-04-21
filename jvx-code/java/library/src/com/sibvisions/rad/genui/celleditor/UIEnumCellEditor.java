/*
 * Copyright 2014 SIB Visions GmbH
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
 * 22.04.2014 - [RZ] - #1014: creation
 */
package com.sibvisions.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UIComboCellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.ObjectDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * The {@code UIEnumCellEditor} is a special blend of an {@link javax.rad.ui.celleditor.IChoiceCellEditor} and
 * {@link ILinkedCellEditor}. It allows to set an array of allowed values together with an array of display 
 * values.
 * 
 * @author Robert Zenz
 */
public class UIEnumCellEditor extends UIComboCellEditor<ILinkedCellEditor>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the display column. */
	private static final String DISPLAY_COLUMN_NAME = "DISPLAY";
	
	/** The name of the values column. */
	private static final String VALUE_COLUMN_NAME = "VALUE";
	
	/** The allowed values. */
	private transient Object[] allowedValues = null;
	
	/** The name of the bound column. */
	private transient String columnName = null;
	
	/** The displayed values. */
	private transient String[] displayValues = null;
	
	/**
	 * The {@link ReferenceDefinition} that is passed on to the
	 * {@link ILinkedCellEditor}.
	 */
	private transient ReferenceDefinition linkReference = new ReferenceDefinition();

	/** The string data type. */
	private transient StringDataType displayValueDataType = new StringDataType();
	
	/** The referenced data book. */
	private transient IDataBook referencedDataBook = new MemDataBook();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UIEnumCellEditor}.
	 */
	public UIEnumCellEditor()
	{
		super(UIFactoryManager.getFactory().createLinkedCellEditor());
		
		try
		{
			referencedDataBook.setName("REFERENCE");
			
			IRowDefinition rowdef = referencedDataBook.getRowDefinition();
			
			rowdef.addColumnDefinition(new ColumnDefinition(VALUE_COLUMN_NAME, new ObjectDataType()));
			rowdef.addColumnDefinition(new ColumnDefinition(DISPLAY_COLUMN_NAME, displayValueDataType));
			
			referencedDataBook.open();
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
		
		linkReference.setReferencedColumnNames(new String[] {VALUE_COLUMN_NAME});
		linkReference.setReferencedDataBook(referencedDataBook);
		
		ILinkedCellEditor ced = getUIResource();
		
		ced.setLinkReference(linkReference);
		ced.setColumnView(new ColumnView(DISPLAY_COLUMN_NAME));
		ced.setDisplayReferencedColumnName(DISPLAY_COLUMN_NAME);
		ced.setTableHeaderVisible(false);
	}
	
	/**
	 * Creates a new instance of {@link UIEnumCellEditor}.
	 * 
	 * @param pAllowedValues the allowed values.
	 */
	public UIEnumCellEditor(Object[] pAllowedValues)
	{
		this(pAllowedValues, null);
	}
	
	/**
	 * Creates a new instance of {@link UIEnumCellEditor}.
	 * 
	 * @param pAllowedValues the allowed values.
	 * @param pDisplayValues the display values.
	 */
	public UIEnumCellEditor(Object[] pAllowedValues, String[] pDisplayValues)
	{
		this();
		
		setAllowedValues(pAllowedValues);
		setDisplayValues(pDisplayValues);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener,
													  IDataRow pDataRow,
													  String pColumnName)
	{
		displayValueDataType.setTranslator(pCellEditorListener.getControl());
			
		return super.createCellEditorHandler(pCellEditorListener, pDataRow, pColumnName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets all allowed values.
	 * 
	 * @return all allowed values.
	 */
	public Object[] getAllowedValues()
	{
		return allowedValues;
	}
	
	/**
	 * Sets all allowed values.
	 * 
	 * @param pAllowedValues all allowed values.
	 */
	public void setAllowedValues(Object[] pAllowedValues)
	{
		allowedValues = pAllowedValues;
		
		updateReferencedDataBook();
	}
	
	/**
	 * Gets the column name.
	 * 
	 * @return the column name.
	 */
	public String getColumnName()
	{
		return columnName;
	}
	
	/**
	 * Sets the column name.
	 * 
	 * @param pColumnName the column name.
	 */
	public void setColumnName(String pColumnName)
	{
		columnName = pColumnName;
		
		if (columnName == null)
		{
            linkReference.setColumnNames(null);
		}
		else
		{
		    linkReference.setColumnNames(new String[] { columnName });
		}
	}
	
	/**
	 * Gets all display values.
	 * 
	 * @return all display values.
	 */
	public String[] getDisplayValues()
	{
		return displayValues;
	}
	
	/**
	 * Sets all display values.
	 * 
	 * @param pDisplayValues all display values.
	 */
	public void setDisplayValues(String[] pDisplayValues)
	{
		displayValues = pDisplayValues;
		
		updateReferencedDataBook();
	}
	
	/**
	 * Updates the referenced data book with the given values for the given
	 * column. Also sets the same value into the secondary column if it the
	 * value of the secondary column is null.
	 */
	private void updateReferencedDataBook()
	{
		try
		{
			referencedDataBook.setReadOnly(false);
			referencedDataBook.setFilter(null);
			referencedDataBook.setSort(null);
			
			int length = allowedValues == null ? 0 : allowedValues.length;
			
			for (int i = 0; i < length; i++)
			{
				if (i >= referencedDataBook.getRowCount())
				{
					referencedDataBook.insert(false);
				}
				else
				{
					referencedDataBook.setSelectedRow(i);
				}
				
				referencedDataBook.setValue(VALUE_COLUMN_NAME, allowedValues[i]);
				
				if (displayValues != null && i < displayValues.length)
				{
					referencedDataBook.setValue(DISPLAY_COLUMN_NAME, displayValues[i]);
				}
				else
				{
					referencedDataBook.setValue(DISPLAY_COLUMN_NAME, allowedValues[i]);
				}
			}
			
			for (int i = length, count = referencedDataBook.getRowCount(); i < count; i++)
			{
				referencedDataBook.setSelectedRow(length);
				referencedDataBook.delete();
			}
			
			referencedDataBook.saveAllRows();
			referencedDataBook.setSelectedRow(-1);
			
			referencedDataBook.setReadOnly(true);
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
	}
	
	/**
	 * Sets validation enabled.
	 * 
	 * @param pEnabled <code>true</code> to validate the value, <code>false</code> otherwise
	 */
	public void setValidationEnabled(boolean pEnabled)
	{
		getUIResource().setValidationEnabled(pEnabled);
	}
	
	/**
	 * Returns whether validation is enabled.
	 * 
	 * @return <code>true</code> if value will be validated, <code>false</code> otherwise
	 */
	public boolean isValidationEnabled()
	{
		return getUIResource().isValidationEnabled();
	}
	
}	// UIEnumCellEditor
