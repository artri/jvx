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
 * 13.11.2008 - [RH] - unnecessary throw ModelExcpetions removed
 * 07.04.2009 - [RH] - Interface review 
 *                     get/setDataRow removed
 *                     add/removeDataBook added
 *                     clone to createRowDefinition renamed
 *                     removeColumn removed 
 *                     add/getColumn renamed to add/getColumnDefintion and ModelException added
 * 31.03.2011 - [JR] - #318: add/removeControl, getControls added
 */
package javax.rad.model;

import javax.rad.model.ui.IControl;

/**
 * The {@link IRowDefinition} defines the layout of the model. It defines
 * columns by using the {@link ColumnDefinition}.
 * 
 * @see ColumnDefinition
 * 
 * @author Roland Hörmann
 */
public interface IRowDefinition extends Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** A constant for using all columns. */
	public static final String[] ALL_COLUMNS = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link IDataBook} at this {@link IRowDefinition}, which
	 * means that the given {@link IDataBook} is using this
	 * {@link IRowDefinition}.
	 * <p>
	 * The added {@link IDataBook} has to remove itself when appropriate.
	 * 
	 * @param pDataBook the {@link IDataBook} to register.
	 * @throws ModelException if adding it was not possible.
	 * @see #getDataBooks()
	 * @see #removeDataBook(IDataBook)
	 */
	public void addDataBook(IDataBook pDataBook) throws ModelException;
	
	/**
	 * Removes the given {@link IDataBook} from this {@link IRowDefinition}.
	 * <p>
	 * The registered {@link IDataBook} has to remove itself when appropriate.
	 *
	 * @param pDataBook the {@link IDataBook} to remove.
	 * @see #addDataBook(IDataBook)
	 * @see #getDataBooks()
	 */
	public void removeDataBook(IDataBook pDataBook);
	
	/**
	 * Gets the array if {@link IDataBook}s which have been added.
	 * 
	 * @return the array if {@link IDataBook}s which have been added. Note that
	 *         this array is only a clone and changing it won't change the
	 *         internal state of the {@link IRowDefinition}.
	 * @see #addDataBook(IDataBook)
	 * @see #removeDataBook(IDataBook)
	 */
	public IDataBook[] getDataBooks();
	
	/**
	 * Creates a {@link IRowDefinition} with only the {@link ColumnDefinition}s
	 * which have been specified.
	 * <p>
	 * If the given column names is {@code null} this instance is returned,
	 * additionally the instances of the {@link IRowDefinition}s can be cached,
	 * so that they same instance is returned for the same column names.
	 * 
	 * @param pColumnNames the names of the {@link ColumnDefinition}s to copy.
	 * @return a {@link IRowDefinition} with only the {@link ColumnDefinition}s
	 *         which have been specified.
	 * @throws ModelException if the {@link IRowDefinition} couldn't be
	 *             constructed.
	 */
	public IRowDefinition createRowDefinition(String[] pColumnNames) throws ModelException;
	
	/**
	 * Adds the given {@link ColumnDefinition}.
	 * <p>
	 * Implementations need to check that the given {@link ColumnDefinition} is
	 * not {@link ColumnDefinition#getRowDefinition() added} to another
	 * {@link IRowDefinition}. Also it is not allowed to add a
	 * {@link ColumnDefinition} after an {@link IDataBook} has been
	 * {@link #addDataBook(IDataBook) added}.
	 * 
	 * @param pColumnDefinition the {@link ColumnDefinition} to add.
	 * @throws ModelException if there is already a {@link ColumnDefinition}
	 *             with that name or if the given {@link ColumnDefinition} is
	 *             already added to another {@link IRowDefinition} or a
	 *             {@link IDataBook} has already been
	 *             {@link ColumnDefinition#getRowDefinition() added}.
	 */
	public void addColumnDefinition(ColumnDefinition pColumnDefinition) throws ModelException;
	
	/**
	 * Gets the {@link ColumnDefinition} with the given name.
	 * 
	 * @param pColumnName name of the {@link ColumnDefinition} to get.
	 * @return the {@link ColumnDefinition} with the given name.
	 * @throws ModelException if there is no {@link ColumnDefinition} with that
	 *             name.
	 */
	public ColumnDefinition getColumnDefinition(String pColumnName) throws ModelException;
	
	/**
	 * Gets the {@link ColumnDefinition} with the given index.
	 * 
	 * @param pColumnIndex index of the {@link ColumnDefinition}.
	 * @return the {@link ColumnDefinition} with the given index.
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public ColumnDefinition getColumnDefinition(int pColumnIndex);
	
	/**
	 * Gets the index of the {@link ColumnDefinition} with the given name.
	 * Returns {@code -1} if there is no {@link ColumnDefinition} with the given
	 * name.
	 * 
	 * @param pColumnName name of the {@link ColumnDefinition}.
	 * @return the index of the {@link ColumnDefinition} with the given name,
	 *         {@code -1} if there is none.
	 * @see #getColumnDefinitionIndexes(String[])
	 */
	public int getColumnDefinitionIndex(String pColumnName);
	
    /**
     * Gets true, if there exists a {@link ColumnDefinition} with the given name.
     * 
     * @param pColumnName name of the {@link ColumnDefinition}.
     * @return true, if there exists a {@link ColumnDefinition} with the given name
     */
    public boolean containsColumnName(String pColumnName);
    
	/**
	 * Returns the indexes of the {@link ColumnDefinition}s with the given
	 * names.
	 * <p>
	 * If any of the given names does not exist, the resulting array will
	 * contain {@code -1} at that position.
	 * 
	 * @param pColumnNames the names of the {@link ColumnDefinition}s.
	 * @return the indexes of the {@link ColumnDefinition}s with the given
	 *         names, {@code -1} for non existing names.
	 * @see #getColumnDefinitionIndex(String)
	 */
	public int[] getColumnDefinitionIndexes(String[] pColumnNames);
	
	/**
	 * Gets the count of {@link #addColumnDefinition(ColumnDefinition) added}
	 * {@link ColumnDefinition}s.
	 * 
	 * @return the count of {@link #addColumnDefinition(ColumnDefinition) added}
	 *         {@link ColumnDefinition}s.
	 * @see #addColumnDefinition(ColumnDefinition)
	 */
	public int getColumnCount();
	
	/**
	 * Gets all added {@link ColumnDefinition}s as array.
	 * <p>
	 * The returned array is a copy, so changing the array will not change
	 * the {@link IRowDefinition}.
	 * 
	 * @return all added {@link ColumnDefinition}s as array.
	 * @since 2.5
	 */
	public ColumnDefinition[] getColumnDefinitions();
	
	/**
	 * Gets the names of all {@link #addColumnDefinition(ColumnDefinition)
	 * added} {@link ColumnDefinition}s.
	 * 
	 * @return the names of all {@link #addColumnDefinition(ColumnDefinition)
	 *         added} {@link ColumnDefinition}s.
	 * @see #addColumnDefinition(ColumnDefinition)
	 */
	public String[] getColumnNames();
	
	/**
	 * Gets the names of all {@link #addColumnDefinition(ColumnDefinition)
	 * added} {@link ColumnDefinition}s which represent a primary key column.
	 * 
	 * @return the names of all {@link #addColumnDefinition(ColumnDefinition)
	 *         added} {@link ColumnDefinition}s.
	 * @see #getPrimaryKeyColumnNames()
	 */
	public String[] getPrimaryKeyColumnNames();
	
	/**
	 * Sets the names of all {@link #addColumnDefinition(ColumnDefinition)
	 * added} {@link ColumnDefinition}s which represent a primary key column.
	 * 
	 * @param pColumnNames the new String[] with the column names to set the
	 *            primary key
	 * @throws ModelException if an {@link IDataBook} has already been
	 *             {@link ColumnDefinition#getRowDefinition() added}.
	 * @see #getPrimaryKeyColumnNames()
	 */
	public void setPrimaryKeyColumnNames(String[] pColumnNames) throws ModelException;
	
	/**
	 * Gets all classes for which {@link ColumnView} are set.
	 * 
	 * @return all classes for which {@link ColumnView} are set.
	 * @see #getColumnView(Class)
	 * @see #setColumnView(Class, ColumnView)
	 */
	public Class<? extends IControl>[] getColumnViewClasses();
	
	/**
	 * Gets the {@link ColumnView} to use for the given {@link IControl}. The
	 * given {@link IControl} class can also be {@code null} to get the default
	 * {@link ColumnView}.
	 * <p>
	 * If there is no {@link ColumnView} for the given class the default one
	 * will be returned.
	 * <p>
	 * The {@link ColumnView} is stored to the base interface, so it does not
	 * matter if you use the interface or the implementing class.
	 * 
	 * @param pTargetControl the class of the {@link IControl} for which to get
	 *            the {@link ColumnView}, can be {@code null} for the default
	 *            one.
	 * @return the {@link ColumnView} for the given {@link IControl} class.
	 * @see #getColumnViewClasses()
	 * @see #setColumnView(Class, ColumnView)
	 */
	public ColumnView getColumnView(Class<? extends IControl> pTargetControl);
	
	/**
	 * Sets the {@link ColumnView} for the given {@link IControl}. The given
	 * {@link IControl} class can also be {@code null} to set the default
	 * {@link ColumnView}.
	 * <p>
	 * The {@link ColumnView} is stored to the base interface, so it does not
	 * matter if you use the interface or the implementing class.
	 * 
	 * @param pTargetControl the class of the {@link IControl} for which to set
	 *            the {@link ColumnView}, can be {@code null} to set the default
	 *            one.
	 * @param pColumnView the {@link ColumnView} for the given {@link IControl}
	 *            class.
	 * @throws ModelException if any of the column names specified in the
	 *             {@link ColumnView} does not exist in this
	 *             {@link IRowDefinition}.
	 * @see #getColumnViewClasses()
	 * @see #getColumnView(Class)
	 */
	public void setColumnView(Class<? extends IControl> pTargetControl, ColumnView pColumnView) throws ModelException;
	
	/**
	 * Sets that specified columns as read only.
	 * 
	 * @param pColumnNames the names of all read only columns, {@code null} for
	 *            all.
	 * @throws ModelException if any of the column names does not exist.
	 * @see #getReadOnly()
	 */
	public void setReadOnly(String[] pColumnNames) throws ModelException;
	
	/**
	 * Gets the names of the columns which are read only.
	 * 
	 * @return the names of the read only columns.
	 * @see #setReadOnly(String[])
	 */
	public String[] getReadOnly();
	
	/**
	 * Adds the given {@link IControl} so that it is
	 * {@link IControl#notifyRepaint() notified} if the {@link IRowDefinition}
	 * changes.
	 * 
	 * @param pControl the {@link IControl} to add.
	 * @see #getControls()
	 * @see #removeControl(IControl)
	 */
	public void addControl(IControl pControl);
	
	/**
	 * Removes the given {@link IControl}.
	 * 
	 * @param pControl the {@link IControl} to remove.
	 * @see #addControl(IControl)
	 * @see #getControls()
	 */
	public void removeControl(IControl pControl);
	
	/**
	 * Returns all added {@link IControl}s.
	 * 
	 * @return all added {@link IControl}s.
	 * @see #addControl(IControl)
	 * @see #removeControl(IControl)
	 */
	public IControl[] getControls();
	
} 	// IRowDefinition
