/*
 * Copyright 2009 SIB Visions GmbH
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
 * 01.10.2008 - [RH] - creation
 */
package javax.rad.model;

import java.lang.ref.WeakReference;

import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;

import com.sibvisions.util.ArrayUtil;

/**
 * A <code>ColumnView</code> contains all column names that should be shown.
 * 
 * @see javax.rad.model.IRowDefinition
 * 
 * @author Martin Handsteiner
 */
public class ColumnView
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** All column names that should be shown. */
	private ArrayUtil<String>					auColumnNames		= new ArrayUtil<String>();
	
	/** The <code>ArrayUtil</code> of all <code>IRowDefinitions</code>. */
	private ArrayUtil<WeakReference<IRowDefinition>>	auRowDefinitions;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new ColumnView.
	 * 
	 * @param pColumnNames the column names. 
	 */
	public ColumnView(String... pColumnNames)
	{
		addColumnNames(pColumnNames);
	}
	
	/**
	 * Constructs a new ColumnView with the same columns as the given column view.
	 * 
	 * @param pColumnView the column view. 
	 */
	public ColumnView(ColumnView pColumnView)
	{
		addColumnNames(pColumnView.getColumnNames());
	}
	
	/**
	 * Constructs a new ColumnView for all columns of the given row definition.
	 * 
	 * @param pRowDefinition the column view. 
	 */
	public ColumnView(IRowDefinition pRowDefinition)
	{
		addColumnNames(pRowDefinition.getColumnNames());
	}
	
	/**
	 * Constructs a new ColumnView for all columns of the given data row.
	 * 
	 * @param pDataRow the column view. 
	 */
	public ColumnView(IDataRow pDataRow)
	{
		addColumnNames(pDataRow.getRowDefinition().getColumnNames());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
	    return "ColumnView: " + auColumnNames;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the column count of this <code>IRowDefintion</code>.
	 * 
	 * @return the column count of this <code>IRowDefintion</code>.
	 */
	public int getColumnCount()
	{
		return auColumnNames.size();
	}

	/**
	 * Returns the column name for the given index.
	 * 
	 * @param pIndex the index.
	 * @return the column name for the given index.
	 */
	public String getColumnName(int pIndex)
	{
		return auColumnNames.get(pIndex);
	}

	/**
	 * Returns the index of the column name.
	 * 
	 * @param pColumnName	name of the column.
	 * @return the index of the column name.
	 */
	public int getColumnNameIndex(String pColumnName)
	{
		return auColumnNames.indexOf(pColumnName);
	}

	/**
	 * Returns the String[] of all column names.
	 * 
	 * @return the String[] of all column names.
	 */
	public String[] getColumnNames()
	{
		return auColumnNames.toArray(new String[auColumnNames.size()]);
	}
	
	/**
	 * Sets the String[] of all column names.
	 * 
	 * @param pColumnNames the String[] of all column names.
	 */
	public void setColumnNames(String... pColumnNames)
	{
		auColumnNames.clear();
		addColumnNames(0, pColumnNames);
	}
	
	/**
	 * Adds the column names at the end.
	 * 
	 * @param pColumnNames the column names.
	 */
	public void addColumnNames(String... pColumnNames)
	{
		addColumnNames(auColumnNames.size(), pColumnNames);
	}
	
	/**
	 * Adds the column names at the specific index.
	 * 
	 * @param pIndex the index.
	 * @param pColumnNames the column names.
	 */
	public void addColumnNames(int pIndex, String... pColumnNames)
	{
		auColumnNames.addAll(pIndex, pColumnNames);
		
		notifyRepaint();
	}
	
	/**
	 * Removes the column names.
	 * 
	 * @param pColumnNames the column names.
	 */
	public void removeColumnNames(String... pColumnNames)
	{
		auColumnNames.removeAll(pColumnNames);
		
		notifyRepaint();
	}
	
	/**
	 * Removes the column name at the specific index.
	 * 
	 * @param pIndex the index.
	 */
	public void removeColumnName(int pIndex)
	{
		auColumnNames.remove(pIndex);

		notifyRepaint();
	}
	
	/**
	 * Moves the column name from an index to an index.
	 * 
	 * @param pFromIndex the index of the column name to be moved.
	 * @param pToIndex the index where the column name should be moved to.
	 */
	public void moveColumnName(int pFromIndex, int pToIndex)
	{
		String columnName = auColumnNames.remove(pFromIndex);
		auColumnNames.add(pToIndex, columnName);

		notifyRepaint();
	}
	
	/**
	 * Adds an IRowDefinition.
	 * An IRowDefintion has to add and remove itself, if a ColumnView shows the specific IRowDefinition.
	 * 
	 * @param pRowDefinition	the IRowDefinition to add
	 */
	public void addRowDefinition(IRowDefinition pRowDefinition)
	{
		boolean add = true;
		if (auRowDefinitions == null)
		{
			auRowDefinitions = new ArrayUtil<WeakReference<IRowDefinition>>();
		}
		else
		{
			for (int i = auRowDefinitions.size() - 1; i >= 0; i--)
			{
				IRowDefinition rowDefinition = auRowDefinitions.get(i).get();
				if (rowDefinition == null)
				{
					auRowDefinitions.remove(i);
				}
				else if (rowDefinition == pRowDefinition)
				{
					add = false;
				}
			}
		}
		if (add)
		{
			auRowDefinitions.add(new WeakReference<IRowDefinition>(pRowDefinition));
		}
	}

	/**
	 * Removes an IRowDefinition.
	 * An IRowDefintion has to add and remove itself, if a ColumnView shows the specific IRowDefinition.
	 * 
	 * @param pRowDefinition	the IRowDefinition to remove
	 */
	public void removeRowDefinition(IRowDefinition pRowDefinition)
	{
		if (auRowDefinitions != null)
		{
			for (int i = auRowDefinitions.size() - 1; i >= 0; i--)
			{
				IRowDefinition rowDefinition = auRowDefinitions.get(i).get();
				if (rowDefinition == null || rowDefinition == pRowDefinition)
				{
					auRowDefinitions.remove(i);
				}
			}
		}
	}

	/**
	 * Gets all IRowDefinition that are currently added.
	 * 
	 * @return all IRowDefinition that are currently added.
	 */
	public IRowDefinition[] getRowDefinitions()
	{
		ArrayUtil<IRowDefinition> result = new ArrayUtil<IRowDefinition>();
		if (auRowDefinitions != null)
		{
			for (int i = auRowDefinitions.size() - 1; i >= 0; i--)
			{
				IRowDefinition rowDefinition = auRowDefinitions.get(i).get();
				if (rowDefinition == null)
				{
					auRowDefinitions.remove(i);
				}
				else
				{
					result.add(0, rowDefinition);
				}
			}
		}
		return result.toArray(new IRowDefinition[result.size()]);
	}
	
	/**
	 * Invokes for each <code>IControl</code> the <code>notifyRepaint()</code> method.<br>
	 */
	public void notifyRepaint()
	{
		if (auRowDefinitions != null)
		{
			for (int i = auRowDefinitions.size() - 1; i >= 0; i--)
			{
				IRowDefinition rowDefinition = auRowDefinitions.get(i).get();
				
				if (rowDefinition !=  null)
				{
					IDataBook[] dataBooks = rowDefinition.getDataBooks();

					for (IDataBook dataBook : dataBooks)
					{
						IControl[] controls = dataBook.getControls();
						for (IControl control : controls)
						{
							if (!(control instanceof IEditorControl))
							{
								control.notifyRepaint();
							}
						}
					}
				}
			}
		}
	}
		
} 	// ColumnView
