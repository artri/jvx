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
 * 02.11.2008 - [RH] - conversion of object to storage and back removed 
 * 13.11.2008 - [RH] - clone() added
 * 18.04.2009 - [RH] - javadoc reviewed
 * 13.03.2010 - [JR] - #88: getTypeIdentifier defined
 */
package javax.rad.model.datatype;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellRenderer;

/**
 * A <code>IDataType</code> is the data type of the <code>ColumnDefinition</code>. 
 * It stores type specific informations like size, precision, ...<br>
 * On the other hand, it stores the <code>ICellRenderer</code> and 
 * <code>ICellEditor</code>. That is because in most cases the GUI control (drawing 
 * and editing) depends on the data type. 
 * 
 * @author Roland Hörmann
 */
public interface IDataType extends Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the data type identifier.
	 * 
	 * @return a unique type identifier
	 */
	public int getTypeIdentifier();
	
	/**
	 * Clone an <code>IDataType</code>.
	 * 
	 * @see java.lang.Object#clone()
	 * 
	 * @return a clone of this <code>IDataType</code>
	 */
	public IDataType clone();
	
	/**
	 * Returns the class of the <code>Object</code>'s handled by <code>IDataType</code>.
	 * 
	 * @return the class of the <code>Object</code>'s handled by <code>IDataType</code>.
	 */
	public Class getTypeClass();
	
	/**
	 * Returns the hash code of the given object or null if the object is null.
	 * 
	 * @param pObject
	 * 			a getTypeClass() object of one <code>IDataType</code>
	 * @return the hash code of the given object or null if the object is null.
	 */
	public int hashCode(Object pObject);
	
	/**
	 * Compares <code>IDataType</code> objects in the rules of the <code>Comparable</code> interface.
	 * Implementations need to take care about to null objects are equal.
     *
	 * @param pObject1
	 * 			a getTypeClass() object of one <code>IDataType</code>
	 * @param pObject2
	 * 			a getTypeClass() object of one <code>IDataType</code>
	 * @return -1 if pObject1 is smaller, 0 if equal and +1 if it is greater then pObject2
	 */
	public int compareTo(Object pObject1, Object pObject2);
	
	/**
	 * Converts the oObject to an <code>Object</code> which is an instance of 
	 * the class which is handled by <code>IDataType</code>.
	 * 
	 * @param pObject
	 *            the source <code>Object</code> to convert
	 * @return the converted <code>Object</code> that is instance of the class which is
	 *         handled by <code>IDataType</code>.
	 * @throws ModelException 
	 * 		   if the class/type of oObject is not supported 
	 */
	public Object convertToTypeClass(Object pObject) throws ModelException;

	/**
	 * Converts the oObject to an <code>Object</code> which is an instance of the class
	 * which is handled by <code>IDataType</code>. The resulting <code>Object</code> is 
	 * also checked against the type attributes like size, precision,...
	 * 
	 * @param pObject
	 *            the source <code>Object</code> to convert
	 * @return the converted <code>Object</code> that is instance of the class which is
	 *         handled by <code>IDataType</code>.
	 * @throws ModelException
	 *             if the <code>Object</code> is outside the limits of the 
	 *             <code>DataType</code> attributes
	 */
	public Object convertAndCheckToTypeClass(Object pObject) throws ModelException;

	/**
	 * Converts the oObject into a <code>String</code> which represents the 
	 * <code>Object</code>.
	 * It considers the current language and number or date formats.
	 * 
	 * @param pObject
	 *            the source <code>Object</code> to convert
	 * @return the <code>String</code> representation of the source <code>Object</code>
	 */
	public String convertToString(Object pObject);

	/**
	 * Converts the oObject into a unified <code>String</code> which represents the 
	 * <code>Object</code>.
	 * It is independent of the current language and number or date formats.
	 * The method <code>convertToTypeClass</code> should be able to convert it back to the correct value. 
	 * 
	 * @param pObject the source <code>Object</code> to convert
	 * @return the <code>String</code> representation of the source <code>Object</code>
	 */
	public String convertToUnifiedString(Object pObject);
	
	/**
	 * Prepares the given value.
	 * 
	 * This is used for preparing values for client-side usage and occurs
	 * before the client side receives the value for the first time.
	 * 
	 * If there is nothing to be done, the same object as passed in needs
	 * to be returned.
	 *  
	 * @param pObject the object to prepare.
	 * @return the prepared object. If there is nothing to do, the same object.
	 * @throws ModelException if the preparation failed.
	 */
	public Object prepareValue(Object pObject) throws ModelException;
	
	/**
	 * Sets the <code>CellEditor</code> for this <code>IDataType</code>.
	 * 
	 * @param pCellEditor
	 *            the new <code>ICellEditor</code>
	 */
	public void setCellEditor(ICellEditor pCellEditor);

	/**
	 * Returns the <code>CellEditor</code> for this <code>IDataType</code>.
	 * If no cell editor is set, <code>getDefaultCellEditor()</code> is returned.
	 * 
	 * @return the <code>CellEditor</code> for this <code>IDataType</code>.
	 */
	public ICellEditor getCellEditor();

	/**
	 * Sets the <code>CellRenderer</code> for this <code>IDataType</code>.
	 * 
	 * @param pCellRenderer
	 *            the new <code>ICellRenderer</code>
	 */
	public void setCellRenderer(ICellRenderer pCellRenderer);

	/**
	 * Returns the <code>CellRenderer</code> for this <code>IDataType</code>.
	 * If no cell renderer is set, <code>getDefaultCellRenderer()</code> is returned.
	 * 
	 * @return the <code>CellRenderer</code> for this <code>IDataType</code>.
	 */
	public ICellRenderer getCellRenderer();
	
	/**
	 * Sets the default <code>CellEditor</code> for this <code>IDataType</code>.
	 * The default cell editor or renderer is set by the model, if it decides to automatically
	 * set a cell editor or renderer.
	 * 
	 * @param pCellEditor
	 *            the new <code>ICellEditor</code>
	 */
	public void setDefaultCellEditor(ICellEditor pCellEditor);

	/**
	 * Returns the default <code>CellEditor</code> for this <code>IDataType</code>.
	 * The default cell editor or renderer is set by the model, if it decides to automatically
	 * set a cell editor or renderer.
	 * 
	 * @return the <code>CellEditor</code> for this <code>IDataType</code>.
	 */
	public ICellEditor getDefaultCellEditor();

	/**
	 * Sets the default <code>CellRenderer</code> for this <code>IDataType</code>.
	 * The default cell editor or renderer is set by the model, if it decides to automatically
	 * set a cell editor or renderer.
	 * 
	 * @param pCellRenderer
	 *            the new <code>ICellRenderer</code>
	 */
	public void setDefaultCellRenderer(ICellRenderer pCellRenderer);

	/**
	 * Returns the default <code>CellRenderer</code> for this <code>IDataType</code>.
	 * The default cell editor or renderer is set by the model, if it decides to automatically
	 * set a cell editor or renderer.
	 * 
	 * @return the <code>CellRenderer</code> for this <code>IDataType</code>.
	 */
	public ICellRenderer getDefaultCellRenderer();
	
} 	// IDataType

