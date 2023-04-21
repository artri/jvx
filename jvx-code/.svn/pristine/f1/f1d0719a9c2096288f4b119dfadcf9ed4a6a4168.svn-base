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
 * 09.04.2009 - [RH] - interface review - compareTo uses SortDefinition
 *                                        equals added
 *                                        getValue(int) added
 *                                        getValue(s) throw no ModelException anymore
 *                                        register,unregisterEditingControl removed
 *                                        clone renamed to createDataRow()
 *                                        IChangeableDataRow methods/functionality moved to ChangeableDataRow
 * 16.04.2009 - [RH] - remove/add/getDetailDataBooks moved to IDataBook.
 * 18.04.2009 - [RH] - get/set/DataPage/RowIndex moved to ChangeableDataRow  
 * 30.03.2010 - [RH] - #6: getValueAsString in IDataRow                                                                       
 * 06.05.2010 - [JR] - getValuesAsString defined
 * 03.04.2014 - [RZ] - #2 - added eventValuesChanged(String pColumnName)                                                                 
 */
package javax.rad.model;

import javax.rad.model.event.DataRowHandler;
import javax.rad.model.ui.IControl;

/**
 * An {@link IDataRow} is a storage independent representation of one row of
 * data. It contains the column definitions and the values for each column.
 * 
 * @see IRowDefinition
 * @see IDataPage
 * @see IDataBook
 * 
 * @author Roland Hörmann
 */
public interface IDataRow extends Comparable<IDataRow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link IRowDefinition} that is used.
	 * <p>
	 * The {@link IRowDefinition} contains all the information about the
	 * columns.
	 * 
	 * @return the {@link IRowDefinition} that is used.
	 */
	public IRowDefinition getRowDefinition();
	
	/**
	 * Returns the value of the column by index.
	 * 
	 * @param pColumnIndex the column index.
	 * @return the value of the column.
	 * @throws ModelException if the column index is out of bounds.
	 * @see #getValue(String)
	 */
	public Object getValue(int pColumnIndex) throws ModelException;
	
	/**
	 * Returns the value of the named column.
	 * 
	 * @param pColumnName the name of the column.
	 * @return the value of the named column.
	 * @throws ModelException if there is no column with the given name.
	 * @see #getValue(int)
	 */
	public Object getValue(String pColumnName) throws ModelException;
	
    /**
     * Returns the unprepared raw value of the column by index.
     * 
     * @param pColumnIndex the column index.
     * @return the unprepared raw value of the column by index.
     * @throws ModelException if there is no column with the given name.
     * @see #getValue(int)
     * @see #getValue(String)
     * @see javax.rad.model.datatype.IDataType#prepareValue(Object)
     */
    public Object getRawValue(int pColumnIndex) throws ModelException;
    
    /**
     * Returns the unprepared raw value of the named column.
     * 
     * @param pColumnName the name of the column.
     * @return the value of the named column converted to a {@link String}.
     * @throws ModelException if there is no column with the given name.
     * @see #getValue(int)
     * @see #getValue(String)
     * @see javax.rad.model.datatype.IDataType#prepareValue(Object)
     */
    public Object getRawValue(String pColumnName) throws ModelException;
    
    /**
     * Returns the value of the column by index converted to a {@link String}.
     * 
     * @param pColumnIndex the column index.
     * @return the value of the named column converted to a {@link String}.
     * @throws ModelException if there is no column with the given name.
     * @see #getValue(int)
     * @see #getValue(String)
     * @see javax.rad.model.datatype.IDataType#convertToString(Object)
     */
    public String getValueAsString(int pColumnIndex) throws ModelException;
    
	/**
	 * Returns the value of the named column converted to a {@link String}.
	 * 
	 * @param pColumnName the name of the column.
	 * @return the value of the named column converted to a {@link String}.
	 * @throws ModelException if there is no column with the given name.
	 * @see #getValue(int)
	 * @see #getValue(String)
	 * @see javax.rad.model.datatype.IDataType#convertToString(Object)
	 */
	public String getValueAsString(String pColumnName) throws ModelException;
	
	/**
	 * Sets the value of the named column.
	 * <p>
	 * After the value has changed the {@link #eventValuesChanged() value
	 * changed event} and {@link #eventValuesChanged(String) value changed (by
	 * name) event} are invoked and fired. Afterwards all registered
	 * {@link IControl}s are notified of the change by invoking
	 * {@link IControl#notifyRepaint()}.
	 * 
	 * @param pColumnName the column name.
	 * @param pValue the new value for the named column.
	 * @throws ModelException if there is no column with the given name or the
	 *             given value can not be converted to the {@link javax.rad.model.datatype.IDataType} of
	 *             the column.
	 * @see #eventValuesChanged()
	 * @see #eventValuesChanged(String)
	 * @see javax.rad.model.datatype.IDataType#convertToTypeClass(Object)
	 */
	public void setValue(String pColumnName, Object pValue) throws ModelException;
	
	/**
	 * Gets the values from all specified columns as an {@link Object} array.
	 * <p>
	 * Note that the returned {@link Object} array does not represent an
	 * internal state of the {@link IDataRow}. Modifying the array does not
	 * change the {@link IDataRow}.
	 * 
	 * @param pColumnNames the names of the columns from which to get the
	 *            values. Can be {@code null} for all columns (which is equal to
	 *            {@link IRowDefinition#getColumnNames()}.
	 * @return the values from all specified columns as an {@link Object} array.
	 * @throws ModelException if there is no column with one of the specified
	 *             names.
	 * @see #getValue(int)
	 * @see #getValue(String)
	 */
	public Object[] getValues(String[] pColumnNames) throws ModelException;
	
	/**
	 * Gets the values from all specified columns and converts them to a
	 * {@link String} array.
	 * <p>
	 * Note that the returned {@link String} array does not represent an
	 * internal state of the {@link IDataRow}. Modifying the array does not
	 * change the {@link IDataRow}.
	 * 
	 * @param pColumnNames the names of the columns from which to get the
	 *            values. Can be {@code null} for all columns.
	 * @return the values from all specified columns as a {@link String} array.
	 * @throws ModelException if there is no column with one of the specified
	 *             names.
	 * @see #getValueAsString(String)
	 * @see #getValues(String[])
	 * @see javax.rad.model.datatype.IDataType#convertToString(Object)
	 */
	public String[] getValuesAsString(String[] pColumnNames) throws ModelException;
	
	/**
	 * Sets all columns of the given names to the given values.
	 * <p>
	 * The {@link String} array which represents the column names is directly
	 * mapped to the {@link Object} array which holds the values. So they must
	 * be equal in length.
	 * <p>
	 * After the value has changed the {@link #eventValuesChanged() value
	 * changed event} and {@link #eventValuesChanged(String) value changed (by
	 * name) event} are invoked and fired. Afterwards all registered
	 * {@link IControl}s are notified of the change by invoking
	 * {@link IControl#notifyRepaint()}.
	 * 
	 * @param pColumnNames a {@link String} array of the column names. Can be
	 *            {@code null} for all columns (which is equal to
	 *            {@link IRowDefinition#getColumnNames()}.
	 * @param pValues a {@link Object} array of values to set in the
	 *            corresponding columns, or null, to set all values to null.
	 * @throws ModelException if there is no column with one of the specified
	 *             names or if any of the values could not be converted to the
	 *             {@link javax.rad.model.datatype.IDataType} of the column.
	 * @see #eventValuesChanged()
	 * @see #eventValuesChanged(String)
	 * @see #setValue(String, Object)
	 * @see javax.rad.model.datatype.IDataType#convertToTypeClass(Object)
	 */
	public void setValues(String[] pColumnNames, Object[] pValues) throws ModelException;
	
	/**
	 * Compares this {@link IDataRow} with the given {@link IDataRow}, by
	 * considering the given {@link SortDefinition} and comparing the values of
	 * both rows according to the {@link SortDefinition}.
	 * <p>
	 * It reacts for the most part like a "normal" {@code compareTo} would,
	 * except that it takes {@link SortDefinition#isAscending()} into
	 * consideration, and if it is {@code false} the result will be negated.
	 * 
	 * @param pDataRow the @link DataRow} to compare with.
	 * @param pSortDefinition the {@link SortDefinition} to us for the
	 *            comparison.
	 * @return a negative integer, zero, or a positive integer as this
	 *         {@link IDataRow} is less than, equal to, or greater than the
	 *         specified {@link IDataRow}.
	 */
	public int compareTo(IDataRow pDataRow, SortDefinition pSortDefinition);
	
	/**
	 * Compares this {@link IDataRow} with the given {@link IDataRow} for
	 * equality, but only compares the values in the column whose names are
	 * given, ignoring all not given columns.
	 * 
	 * @param pDataRow the {@link IDataRow} to compare with this {@link IDataRow}
	 * @param pColumnNames a {@link String} array of the names of the columns to
	 *            compare.
	 * @return {@code true} if they are found equal.
	 */
	public boolean equals(IDataRow pDataRow, String[] pColumnNames);
	
	/**
	 * Creates and returns a new {@link IDataRow} which only contains the
	 * specified columns and their values.
	 * 
	 * @param pColumnNames a {@link String} array of column names. Can be
	 *            {@code null} for all columns (which is equal to
	 *            {@link IRowDefinition#getColumnNames()}.
	 * @return a new {@link IDataRow} which only contains the specified columns
	 *         and their values.
	 * @throws ModelException if new {@link IDataRow} could not be constructed.
	 */
	public IDataRow createDataRow(String[] pColumnNames) throws ModelException;
	
	/**
	 * Creates and returns a new empty {@link IDataRow} which only contains the
	 * specified columns without any values.
	 * 
	 * @param pColumnNames a {@link String} array of column names. Can be
	 *            {@code null} for all columns (which is equal to
	 *            {@link IRowDefinition#getColumnNames()}.
	 * @return a new empty {@link IDataRow} which only contains the specified
	 *         columns without any values.
	 * @throws ModelException if new {@link IDataRow} could not be constructed.
	 * @deprecated since 2.5, use {@link #createEmptyDataRow(String[])} instead.
	 */
	@Deprecated
	public IDataRow createEmptyRow(String[] pColumnNames) throws ModelException;
	
	/**
	 * Creates and returns a new empty {@link IDataRow} which only contains the
	 * specified columns without any values.
	 * 
	 * @param pColumnNames a {@link String} array of column names. Can be
	 *            {@code null} for all columns (which is equal to
	 *            {@link IRowDefinition#getColumnNames()}.
	 * @return a new empty {@link IDataRow} which only contains the specified
	 *         columns without any values.
	 * @throws ModelException if new {@link IDataRow} could not be constructed.
	 */
	public IDataRow createEmptyDataRow(String[] pColumnNames) throws ModelException;
	
	/**
	 * Adds the given {@link IControl} to this {@link IDataRow} as control.
	 * <p>
	 * The registered {@link IControl} will be notified of changes in this
	 * {@link IDataRow}.
	 * 
	 * @param pControl the {@link IControl} to add.
	 * @see #removeControl(IControl)
	 * @see #getControls()
	 */
	public void addControl(IControl pControl);
	
	/**
	 * Removes the given {@link IControl} to this {@link IDataRow} as control.
	 * 
	 * @param pControl the {@link IControl} to remove.
	 * @see #addControl(IControl)
	 * @see #getControls()
	 */
	public void removeControl(IControl pControl);
	
	/**
	 * Gets all registered {@link IControl}s as array.
	 * 
	 * @return all registered {@link IControl}s as array.
	 */
	public IControl[] getControls();
	
	/**
	 * Gets the {@link DataRowHandler} for the values changed event.
	 * <p>
	 * The values changed event is fired every time a value changes in this
	 * {@link IDataRow}.
	 * 
	 * @return the {@link DataRowHandler} for the values changed event.
	 * @see #eventValuesChanged(String)
	 * @see #setValue(String, Object)
	 * @see #setValues(String[], Object[])
	 */
	public DataRowHandler eventValuesChanged();
	
	/**
	 * Gets the {@link DataRowHandler} for the values changed event for the
	 * specified column.
	 * <p>
	 * The values changed event is fired every time a value changes in this
	 * {@link IDataRow}.
	 * 
	 * @param pColumnName the name of the column.
	 * @return the {@link DataRowHandler} for the values changed event for
	 *         the specified column.
	 * @see #eventValuesChanged()
	 * @see #setValue(String, Object)
	 * @see #setValues(String[], Object[])
	 */
	public DataRowHandler eventValuesChanged(String pColumnName);
	
	/**
	 * Informs all {@link #getControls() registered controls} that there are new
	 * values and that they must redraw themselves. For this purpose the
	 * {@link IControl#notifyRepaint()} method is invoked on all registered
	 * {@link IControl}s.
	 * 
	 * @see IControl#notifyRepaint()
	 */
	public void notifyRepaintControls();
	
	/**
	 * Informs all {@link #getControls() registered controls} that they should
	 * save their values. For this purpose the {@link IControl#saveEditing()}
	 * method is invoked on all registered {@link IControl}s.
	 * 
	 * @throws ModelException if saving of any of the registered
	 *             {@link IControl}s fails.
	 * @see IControl#notifyRepaint()
	 */
	public void saveEditingControls() throws ModelException;
	
	/**
	 * Informs all {@link #getControls() registered controls} that they should
	 * cancel any pending editing operation and revert to the values in this
	 * {@link IDataRow}. For this purpose the {@link IControl#cancelEditing()}
	 * method is invoked on all registered {@link IControl}s.
	 */
	public void cancelEditingControls();
	
    /**
     * Gets whether dispatching events is enabled.
     * All model events will be suppressed, doesn't matter, if a specific event handler is enabled or not.
     * It is ensured, that internal listeners will still be invoked.
     * 
     * @return <code>true</code> if enabled, <code>false</code> if disabled
     */
    public boolean isDispatchEventsEnabled();
    
    /**
     * Sets enabled state of event dispatching.
     * All model events will be suppressed, doesn't matter, if a specific event handler is enabled or not.
     * It is ensured, that internal listeners will still be invoked.
     * 
     * @param pEnabled <code>true</code> to enable, <code>false</code> to disable
     */
    public void setDispatchEventsEnabled(boolean pEnabled);
	
} 	// IDataRow
