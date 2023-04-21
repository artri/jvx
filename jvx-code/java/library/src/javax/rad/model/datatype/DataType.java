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
 * 02.11.2008 - [RH] - use Internalize.intern at convertToType; compareTo uses == because of Internalize
 * 02.11.2008 - [RH] - conversion of object to storage and back removed 
 * 18.04.2009 - [RH] - DBType property removed, javadoc reviewed.
 */
package javax.rad.model.datatype;

import java.io.Serializable;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellRenderer;

/**
 * A <code>DataType</code> is the abstract base class of all <code>ColumnDefinition</code>
 * data types. They store type specific informations like size, precision, ...<br>
 * It can also used to convert between a standard Java data type and a memory
 * optimized storage <code>Object</code>.<br>
 * <br>
 * On the other hand, it stores the <code>ICellRenderer</code> and 
 * <code>ICellEditor</code>. That is because in most cases the GUI control (drawing 
 * and editing) depends on the data type. 
 * 
 * @author Roland Hörmann
 */
public abstract class DataType implements IDataType, 
                                          Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The <code>ICellEditor</code> of this <code>DataType</code>. */
	private transient ICellEditor	ceCellEditor;

	/** The <code>ICellRenderer</code> of this <code>DataType</code>. */
	private transient ICellRenderer	crCellRenderer;

	/** The default <code>ICellEditor</code> of this <code>DataType</code>. */
	private transient ICellEditor	ceDefaultCellEditor;

	/** The default <code>ICellRenderer</code> of this <code>DataType</code>. */
	private transient ICellRenderer	crDefaultCellRenderer;

	/** The Size of the <code>DataType</code>. */
	private int						iSize = Integer.MAX_VALUE;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Object pObject1, Object pObject2)
	{
		// possible, because of Internalize.intern()
		if (pObject1 == pObject2)
		{
			return 0;
		}
		else if (pObject1 != null && pObject2 != null)
		{
			if (pObject1 instanceof Comparable)
			{
				try
				{
					return ((Comparable)pObject1).compareTo(convertToTypeClass(pObject2));
				}
				catch (Exception ex)
				{
					return 1;
				}
			}
			else if (pObject1.equals(pObject2))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		else if (pObject1 == null && pObject2 != null)
		{
			return -1;
		}
		else if (pObject1 != null && pObject2 == null)
		{
			return 1;
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode(Object pObject)
	{
		if (pObject == null)
		{
			return -1;
		}
		return pObject.hashCode();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public IDataType clone()
	{
		try 
		{
			return (IDataType) super.clone();
		}
		catch (CloneNotSupportedException cloneNotSupportedException)
		{
			// should not occur
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String convertToUnifiedString(Object pObject)
	{
		return convertToString(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object prepareValue(Object pObject) throws ModelException
	{
		return pObject;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCellEditor(ICellEditor pCellEditor)
	{
		ceCellEditor = pCellEditor;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getCellEditor()
	{
		if (ceCellEditor == null)
		{
			return getDefaultCellEditor();
		}
		else
		{
			return ceCellEditor;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCellRenderer(ICellRenderer pCellRenderer)
	{
		crCellRenderer = pCellRenderer;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellRenderer getCellRenderer()
	{
		if (crCellRenderer == null)
		{
			return getDefaultCellRenderer();
		}
		else
		{
			return crCellRenderer;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDefaultCellEditor(ICellEditor pDefaultCellEditor)
	{
		ceDefaultCellEditor = pDefaultCellEditor;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getDefaultCellEditor()
	{
		return ceDefaultCellEditor;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultCellRenderer(ICellRenderer pDefaultCellRenderer)
	{
		crDefaultCellRenderer = pDefaultCellRenderer;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellRenderer getDefaultCellRenderer()
	{
		return crDefaultCellRenderer;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the size of the DataType.
	 * 
	 * @return the size of the DataType.
	 */
	public int getSize()
	{
		return iSize;
	}

	/**
	 * Sets the size of the <code>DataType</code>.
	 * 
	 * @param pSize the size of the <code>DataType</code>.
	 */
	public void setSize(int pSize)
	{
		if (pSize < 0)
		{
			pSize = 0;
		}
		
		iSize = pSize;
	}
	
} 	// DataType

