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
 * 11.10.2008 - [RH] - toString() optimized, (DataBook) checks changed to IDataBook
 * 02.11.2008 - [RH] - use IDataBook instead of DataBook
 * 13.11.2008 - [RH] - getDefaultTableColumnNames added
 *                     setXXXColumnNames() are allowed before the Columns added. Not Existing will not be used
 * 19.11.2008 - [RH] - getFilter moved to MemDataBook  
 * 07.04.2009 - [RH] - Interface review 
 *                     get/setDataRow removed
 *                     add/removeDataBook added
 *                     clone to createRowDefinition renamed
 *                     removeColumn removed 
 *                     add/getColumn renamed to add/getColumnDefintion and ModelException added 
 * 16.04.2009 - [RH] - PK, Table/Tree/EditorColumns Checks moved to MemDataBook.open()
 * 18.04.2009 - [RH] - NLS removed
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 28.11.2009 - [RH] - duplicate column name bug fixed [BUGFIX]
 * 09.12.2009 - [RH] - get/setReadOnly added
 * 30.01.2010 - [JR] - #46: getDefaultTableColumnNames changed
 * 25.03.2010 - [JR] - #98; set/getDefaultIgnoreColumnNames implemented and used in getDefaltTableColumnNames 
 * 23.02.2011 - [RH] - #259: Better Exception text to help the developer to find the problem
 * 31.03.2011 - [JR] - #318: add/removeControl, getControls implemented
 * 05.07.2011 - [JR] - #412: isColumnIgnored implemented
 * 08.07.2011 - [JR] - checked null in getMasterReference [BUGFIX] 
 */
package javax.rad.model;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.celleditor.ILinkedCellEditor;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.IntHashMap;
import com.sibvisions.util.type.StringUtil;

/**
 * A <code>RowDefinition</code> contains all <code>ColumnDefintion</code>'s of a 
 * <code>DataRow</code>. <br>
 * 
 * <br>Example:
 * <pre>
 * <code>
 * // construct a RowDefinition
 * RowDefinition rdRowDefinition = new RowDefinition();
 * 
 * // construct some ColumnDefinitions
 * ColumnDefinition cdId   = new ColumnDefinition("id");
 * ColumnDefinition cdName = new ColumnDefinition("name");
 * 
 * rdRowDefinition.addColumnDefinition(cdId);		
 * rdRowDefinition.addColumnDefinition(cdName);
 * </code>
 * </pre>
 * 
 * @see javax.rad.model.IRowDefinition
 * @see javax.rad.model.ColumnDefinition
 * 
 * @author Roland Hörmann
 */
public class RowDefinition implements IRowDefinition, 
                                      Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * <code>String</code>[] of all column names (with or without wildcards) which 
	 * will be ignored as detault table columns.
	 * (default: <code>ID</code>, <code>*_ID</code>, <code>*_INTERN</code>
	 */
	private static String[]						saDefaultIgnoredColumnNames = {"id", "*_id", "*_intern", "ID", "*_ID", "*_INTERN"};

	
	/** The reference to the corresponding <code>IDataBook</code>'s. */
	private transient ArrayUtil<IDataBook>		auDataBooks;

	/** The column views for this RowDefinition. */ 
	private transient ArrayUtil<Class<? extends IControl>>   			auColumnViewClasses;
	/** The column views for this RowDefinition. */ 
	private transient HashMap<Class<? extends IControl>, ColumnView>   	hmColumnViews;

	/** The default column view for this RowDefinition. */ 
	private transient ColumnView 				defaultColumnView;
	
	/** Array of all <code>ColumnDefinition</code>'s. */
	private ArrayUtil<ColumnDefinition>			auColumnDefinitions	= new ArrayUtil<ColumnDefinition>();
	
	/** Maps column names to indexes of the column definition. */
	private Map<String, Integer> 				hmColumnDefinitionsMap = new HashMap<String, Integer>();

	/** Maps column names to indexes of the column definition. */
	private transient IntHashMap<int[]> 		hmColumnArrayDefinitionsMap = new IntHashMap<int[]>();

	/** Maps column names to indexes of the column definition. */
	private transient IntHashMap<RowDefinition> hmRowDefinitionsMap = new IntHashMap<RowDefinition>();

	/** <code>String</code>[] of all all column names. */
	private transient String[]					saColumnNames	= new String[] {};
	
	/** <code>String</code>[] of all primary key columns. */
	private String[]							saPKColumnNames;

	/** The <code>ArrayUtil</code> of all <code>IRepaintListeners</code>. */
	private transient ArrayUtil<WeakReference<IControl>>	auControls;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addDataBook(IDataBook pDataBook) throws ModelException
	{
		if (pDataBook != null)
		{
			if (auDataBooks == null)
			{
				auDataBooks = new ArrayUtil<IDataBook>();
				
				auDataBooks.add(pDataBook);
			}
			else if (auDataBooks.indexOfReference(pDataBook) < 0)
			{
				auDataBooks.add(pDataBook);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeDataBook(IDataBook pDataBook)
	{
		int i = auDataBooks.indexOfReference(pDataBook);
		if (i >= 0)
		{
			auDataBooks.remove(i);
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	public IDataBook[] getDataBooks()
	{
		if (auDataBooks == null)
		{
			return new IDataBook[] {};
		}
		else
		{
			return auDataBooks.toArray(new IDataBook[auDataBooks.size()]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IRowDefinition createRowDefinition(String[] pColumnNames) throws ModelException
	{
		if (pColumnNames == null)
		{
			return this;
		}
		else
		{
			int key = getStringArrayHashCode(pColumnNames);
			RowDefinition rowDefinition = hmRowDefinitionsMap.get(key);
			
			if (rowDefinition == null || getStringArrayHashCode(rowDefinition.saColumnNames) != key)
			{
				rowDefinition = new RowDefinition();

				int[] columnIndexes = getColumnDefinitionIndexes(pColumnNames);
				for (int i = 0; i < columnIndexes.length; i++)
				{
					int columnIndex = columnIndexes[i];
					if (columnIndex < 0)
					{
						throw new ModelException("Column '" + pColumnNames[i] + 
								"' doesn't exists in RowDefinition! - Check that the DataBook is open or the column is added before.");				
					}
					else
					{
						ColumnDefinition columnDef = (ColumnDefinition)getColumnDefinition(columnIndex).clone();
						columnDef.setNullable(true);
			
						rowDefinition.addColumnDefinition(columnDef);
					}
				}
				// [HM] if it is another RowDefinition, the primary key columns are a problem (maybe they are not available anymore)
	/*			if (saPKColumnNames != null)
				{
					rowDefinition.setPrimaryKeyColumnNames(saPKColumnNames.clone());
				}*/
				hmRowDefinitionsMap.put(key, rowDefinition);
			}
			return rowDefinition;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addColumnDefinition(ColumnDefinition pColumnDefinition) throws ModelException
	{
		if (pColumnDefinition != null)
		{
			if (pColumnDefinition.getRowDefinition() != null)
			{
				throw new ModelException("Column '" + pColumnDefinition.getName() + "' already added to another RowDefinition! - " + 
						                 pColumnDefinition.getRowDefinition().toString());
			}
			if (ArrayUtil.contains(saColumnNames, pColumnDefinition.getName()))
			{
				throw new ModelException("Column '" + pColumnDefinition.getName() + "' already exists in RowDefinition!");
			}
			
			if (auDataBooks != null && auDataBooks.size() > 0)
			{
				throw new ModelException("RowDefinition is already in use (IDataBook '" + auDataBooks.get(0).getName() + "' is open)! -> Changes not possible!");				
			}

			auColumnDefinitions.add(pColumnDefinition);
			saColumnNames = ArrayUtil.add(saColumnNames, pColumnDefinition.getName());
			hmColumnDefinitionsMap.put(pColumnDefinition.getName(), Integer.valueOf(auColumnDefinitions.size() - 1));
			
			pColumnDefinition.setRowDefinition(this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ColumnDefinition getColumnDefinition(String pColumnName) throws ModelException
	{
		int i = getColumnDefinitionIndex(pColumnName);
		if (i < 0)
		{
			// #259 - Better Exception text to help the developer to find the problem. 
			throw new ModelException("Column '" + pColumnName + "' doesn't exists in RowDefinition! - Check that the DataBook is open or the column is added before.");				
		}
		ColumnDefinition cdColumnDefinition = auColumnDefinitions.get(i);
		return cdColumnDefinition;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ColumnDefinition getColumnDefinition(int pColumnIndex)
	{
		return auColumnDefinitions.get(pColumnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumnDefinitionIndex(String pColumnName)
	{
		Integer index = hmColumnDefinitionsMap.get(pColumnName);
		if (index == null)
		{
			return -1;
		}
		else
		{
			return index.intValue();
		}
	}

    /**
     * {@inheritDoc}
     */
	public boolean containsColumnName(String pColumnName)
	{
	    return hmColumnDefinitionsMap.get(pColumnName) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int[] getColumnDefinitionIndexes(String[] pColumnNames)
	{
		if (pColumnNames == null)
		{
			return null;
		}
		else
		{
			int key = getStringArrayHashCode(pColumnNames);
			int[] columnIndexes = hmColumnArrayDefinitionsMap.get(key);
			
			if (columnIndexes == null)
			{
				columnIndexes = new int[pColumnNames.length];
				for (int i = 0; i < columnIndexes.length; i++)
				{
					Integer index =  hmColumnDefinitionsMap.get(pColumnNames[i]);
					if (index == null)
					{
						columnIndexes[i] = -1;
					}
					else
					{
						columnIndexes[i] = index.intValue();
					}
				}
				
				hmColumnArrayDefinitionsMap.put(key, columnIndexes);
			}
			return columnIndexes;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumnCount()
	{
		return saColumnNames.length;
	}

	/**
	 * {@inheritDoc}
	 */
	public ColumnDefinition[] getColumnDefinitions()
	{
		return auColumnDefinitions.toArray(new ColumnDefinition[auColumnDefinitions.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getColumnNames()
	{
		return saColumnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getPrimaryKeyColumnNames()
	{
		return saPKColumnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPrimaryKeyColumnNames(String[] pColumnNames) throws ModelException
	{
		if (auDataBooks != null && auDataBooks.size() > 0)
		{
			throw new ModelException("RowDefinition is already in use (IDataBook is open)! -> Changes not possible! - " + 
	                 auDataBooks.get(0).toString());				
		}
		
		if (pColumnNames == null)
		{
			saPKColumnNames = null;
		}
		else
		{
			saPKColumnNames = pColumnNames.clone();
		}
	}

	/**
	 * Gets the classes for which ColumnViews are set.
	 * @return the classes for which ColumnViews are set.
	 */
	public Class<? extends IControl>[] getColumnViewClasses()
	{
		if (auColumnViewClasses == null)
		{
			return new Class[0];
		}
		else
		{
			return auColumnViewClasses.toArray(new Class[auColumnViewClasses.size()]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ColumnView getColumnView(Class<? extends IControl> pTargetControl)
	{
		if (pTargetControl != null && auColumnViewClasses != null)
		{
			for (int i = 0, size = auColumnViewClasses.size(); i < size; i++)
			{
				Class<? extends IControl> columnViewClass = auColumnViewClasses.get(i);
				
 				if (columnViewClass.isAssignableFrom(pTargetControl))
				{
					return hmColumnViews.get(columnViewClass);
				}
			}
		}
		if (defaultColumnView == null)
		{
			defaultColumnView = getDefaultColumnView();
			
			defaultColumnView.addRowDefinition(this);
		}
		
		return defaultColumnView;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setColumnView(Class<? extends IControl> pTargetControl, ColumnView pColumnView) throws ModelException
	{	
		if (pColumnView == null)
		{
			if (pTargetControl == null && defaultColumnView != null)
			{
				defaultColumnView.removeRowDefinition(this);
				
				defaultColumnView = null;
			}
			else if (hmColumnViews != null)
			{
				ColumnView columnView = hmColumnViews.remove(pTargetControl);
				
				if (columnView != null)
				{
					columnView.removeRowDefinition(this);

					auColumnViewClasses.remove(pTargetControl);

					if (hmColumnViews.size() == 0)
					{
						hmColumnViews = null;
						auColumnViewClasses = null;
					}
				}
			}
		}
		else
		{
			checkColumnNames(pColumnView.getColumnNames());

			if (pTargetControl == null)
			{
				if (defaultColumnView != null)
				{
					defaultColumnView.removeRowDefinition(this);
				}
				
				defaultColumnView = pColumnView;
				
				defaultColumnView.addRowDefinition(this);
			}
			else
			{
				if (hmColumnViews == null)
				{
					hmColumnViews = new HashMap<Class<? extends IControl>, ColumnView>();
					auColumnViewClasses = new ArrayUtil<Class<? extends IControl>>();
				}
				
				ColumnView columnView = hmColumnViews.put(pTargetControl, pColumnView);
				
				if (columnView == null)
				{
					int i = 0;
					int size = auColumnViewClasses.size();
					while (i < size && !auColumnViewClasses.get(i).isAssignableFrom(pTargetControl))
					{
						i++;
					}
					auColumnViewClasses.add(i, pTargetControl);
				}
				else
				{
					columnView.removeRowDefinition(this);
				}
				pColumnView.addRowDefinition(this);
			}
		}
		if (auDataBooks != null)
		{
			for (int i = 0; i < auDataBooks.size(); i++)
			{
				IDataBook dataBook = auDataBooks.get(i);
				
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

	/**
	 * {@inheritDoc}
	 */
	public void setReadOnly(String[] pColumnNames) throws ModelException
	{
		if (pColumnNames == null || pColumnNames.length == 0)
		{
			pColumnNames = getColumnNames();
		}
		for (int i = 0; i < pColumnNames.length; i++)
		{
			getColumnDefinition(pColumnNames[i]).setReadOnly(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getReadOnly()
	{
		ArrayUtil<String> auResultColumnNames = new ArrayUtil<String>();
		for (int i = 0; i < auColumnDefinitions.size(); i++)
		{
			if (auColumnDefinitions.get(i).isReadOnly())
			{
				auResultColumnNames.add(auColumnDefinitions.get(i).getName());
			}
		}
		return auResultColumnNames.toArray(new String[auResultColumnNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addControl(IControl pControl)
	{
		if (auControls == null)
		{
			auControls = new ArrayUtil<WeakReference<IControl>>();
		}
		else
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				if (auControls.get(i).get() == null)
				{
					auControls.remove(i);
				}
			}
		}
		
		if (auControls.indexOfReference(pControl) < 0)
		{		
			auControls.add(new WeakReference<IControl>(pControl));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeControl(IControl pControl)
	{
		if (auControls != null)
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				IControl control = auControls.get(i).get();
				if (control == null || control == pControl)
				{
					auControls.remove(i);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IControl[] getControls()
	{
		ArrayUtil<IControl> result = new ArrayUtil<IControl>();
		if (auControls != null)
		{
			for (int i = auControls.size() - 1; i >= 0; i--)
			{
				IControl control = auControls.get(i).get();
				if (control == null)
				{
					auControls.remove(i);
				}
				else
				{
					result.add(0, control);
				}
			}
		}
		return result.toArray(new IControl[result.size()]);
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
		StringBuilder sbResult = new StringBuilder();
		
		sbResult.append("[");
		
		ColumnDefinition columnDef;
		
		boolean bAttribute;
		
		for (int i = 0, cnt = auColumnDefinitions.size(); i < cnt; i++)
		{
			if (i > 0)
			{
				sbResult.append(", ");
			}
			
			columnDef = auColumnDefinitions.get(i);
			
			sbResult.append(columnDef.getName());
			
			bAttribute = false;
			
			if (ArrayUtil.indexOf(saPKColumnNames, columnDef.getName()) >= 0)
			{
				sbResult.append(" (");

				sbResult.append("PK");
			
				bAttribute = true;
			}

			if (columnDef.isWritable())
			{
				if (bAttribute)
				{
					sbResult.append(", ");
				}
				else
				{
					sbResult.append(" (");
					
					bAttribute = true;
				}
				
				sbResult.append("writable");
			}
			
			if (bAttribute)
			{
				sbResult.append(")");
			}
		}

		sbResult.append("]");
		
		return sbResult.toString();
	}	
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the table columns exists in the RowDefinition.
	 * 
	 * @param pTableColumnNames	the table column names array to check
	 * @throws ModelException if not columns exists in the RowDefinition
	 */
	private void checkColumnNames(String[] pTableColumnNames) throws ModelException
	{
		for (int i = 0; pTableColumnNames != null && i < pTableColumnNames.length; i++)
		{
		    getColumnDefinition(pTableColumnNames[i]);
		}
	}
	
	/**
	 * Returns the default table columns names. <br>
	 * Thats all columns without the PK columns.
	 * 
	 * @return the default table columns names.
	 */
	public ColumnView getDefaultColumnView()
	{
		if (getColumnCount() == 0)
		{
			throw new IllegalStateException("It is not allowed to call getDefaultColumnView, if the RowDefinition has no columns!");
		}
		
		ColumnView columnView = new ColumnView();
		
		for (int i = 0; i < saColumnNames.length; i++)
		{
			String sColumn = saColumnNames[i];
			
			if (!(auColumnDefinitions.get(i).getDataType() instanceof BinaryDataType))
			{
				if (!isColumnIgnored(sColumn) && !isMasterLinkColumn(sColumn))
				{
					columnView.addColumnNames(sColumn);
				}
			}
		}
		if (columnView.getColumnCount() == 0)
		{
			for (int i = 0; i < saColumnNames.length; i++)
			{
				String sColumn = saColumnNames[i];
				
				if (!(auColumnDefinitions.get(i).getDataType() instanceof BinaryDataType))
				{
					if (!isColumnIgnored(sColumn))
					{
						columnView.addColumnNames(sColumn);
					}
				}
			}
		}
		
		return columnView;
	}
	
	/**
	 * Gets all columns with a LinkReference including pColumnName.
	 * 
	 * @param pColumnName the column name.
	 * @return all columns with a LinkReference including pColumnName.
	 */
	public String[] getLinkReferenceColumnNames(String pColumnName)
	{
		ArrayUtil<String> columnNames = new ArrayUtil<String>();

		for (int i = 0, size = auColumnDefinitions.size(); i < size; i++)
		{
			ICellEditor cellEditor = auColumnDefinitions.get(i).getDataType().getCellEditor();
			
			if (cellEditor instanceof ILinkedCellEditor)
			{
				ReferenceDefinition ref = ((ILinkedCellEditor)cellEditor).getLinkReference();
				
				if (ArrayUtil.indexOf(ref.getColumnNames(), pColumnName) >= 0)
				{
					columnNames.add(saColumnNames[i]);
				}
			}
		}
		if (columnNames.size() == 0)
		{
			return null;
		}
		else
		{
			return columnNames.toArray(new String[columnNames.size()]);
		}
	}
	
	/**
	 * Gets the link reference of the column name.
	 * 
	 * @param pColumnName the column name.
	 * @return the link reference
	 */
	public ReferenceDefinition getLinkReference(String pColumnName)
	{
		ReferenceDefinition linkReference = null;
		for (int i = 0, size = auColumnDefinitions.size(); i < size; i++)
		{
			ICellEditor cellEditor = auColumnDefinitions.get(i).getDataType().getCellEditor();
			
			if (cellEditor instanceof ILinkedCellEditor)
			{
				ReferenceDefinition ref = ((ILinkedCellEditor)cellEditor).getLinkReference();
				
				if (ArrayUtil.indexOf(ref.getColumnNames(), pColumnName) >= 0
						&& (linkReference == null || ref.getColumnNames().length < linkReference.getColumnNames().length))
				{
					linkReference = ref;
				}
			}
		}
		return linkReference;
	}
	
	/**
	 * Gets the master reference.
	 * 
	 * @return the master reference
	 */
	public ReferenceDefinition getMasterReference()
	{
		if (auDataBooks != null)
		{
			for (IDataBook dataBook : auDataBooks)
			{
				if (dataBook.getMasterReference() != null)
				{
					return dataBook.getMasterReference();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets true, if the column is a LinkReferenceColumn for the MasterReference definition.
	 * 
	 * @param pColumnName the column name.
	 * @return true, if the column is a LinkReferenceColumn for the MasterReference definition.
	 */
	public boolean isMasterLinkColumn(String pColumnName)
	{
		ReferenceDefinition master = getMasterReference();
		ReferenceDefinition link = getLinkReference(pColumnName);
		
		return master != null 
		 		&& (ArrayUtil.indexOf(master.getColumnNames(), pColumnName) >= 0
		 				|| (link != null
		 						&& ArrayUtil.containsAll(link.getColumnNames(), master.getColumnNames())));
	}
	
	/**
	 * Sets a list of columns which are excluded from the table columns when no
	 * specific table columns are set. The column names are specified with or
	 * without wildcard, e.g. *_id. The column names are case insensitive.
	 * 
	 * @param pColumnNames a list of column names to ignore
	 */
	public static void setDefaultIgnoredColumnNames(String... pColumnNames)
	{
		saDefaultIgnoredColumnNames = pColumnNames;
	}
	
	/**
	 * Gets the list of columns which are not included in the table columns when
	 * no specific table columns are set.
	 * 
	 * @return the list of ignored column names
	 * @see #setDefaultIgnoredColumnNames(String...)
	 */
	public static String[] getDefaultIgnoredColumnNames()
	{
		return saDefaultIgnoredColumnNames;
	}

	/**
	 * Gets whether a column name is ignored through the default ignored column names.
	 * 
	 * @param pColumnName the column name to check
	 * @return <code>true</code> when the column is ignored, <code>false</code> if it is not ignored
	 */
	public static boolean isColumnIgnored(String pColumnName)
	{
		if (saDefaultIgnoredColumnNames == null)
		{
			return false;
		}
		else
		{
			for (int j = 0; j < saDefaultIgnoredColumnNames.length; j++)
			{
				if (StringUtil.like(pColumnName, saDefaultIgnoredColumnNames[j]))
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	/**
	 * Gets the hash code for the given string array.
	 * @param pColumnNames the string array
	 * @return the hash code
	 */
	private static final int getStringArrayHashCode(String[] pColumnNames)
	{
		int hash = pColumnNames.length * 29;
		
		for (int i = 0; i < pColumnNames.length; i++)
		{
			hash = (hash * 13) + pColumnNames[i].hashCode();
		}

		return hash;
	}
	
} 	// RowDefinition
