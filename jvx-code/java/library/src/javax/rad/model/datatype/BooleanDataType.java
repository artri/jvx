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
 * 13.11.2008 - [RH] - clone moved to DataType()
 * 13.03.2010 - [JR] - #88: getTypeIdentifier implemented
 */
package javax.rad.model.datatype;

import java.sql.Types;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

/**
 * A <code>BooleanDataType</code> is the data type class of 
 * a <code>Boolean</code> <code>ColumnDefinition</code>.<br>
 *  
 * @author Roland Hörmann
 */
public class BooleanDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.BOOLEAN;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a <code>BooleanDataType</code>.
	 */
	public BooleanDataType()
	{
	}

	/**
	 * Constructs a <code>BooleanDataType</code> cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public BooleanDataType(ICellEditor pCellEditor)
	{
		setCellEditor(pCellEditor);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getTypeIdentifier()
	{
		return TYPE_IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getTypeClass()
	{
		return Boolean.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String convertToString(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}		
		
		return pObject.toString();
	}
	
    /**
     * {@inheritDoc}
     */
    public Object convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
    	Boolean bObject = (Boolean)convertToTypeClass(pObject);
    	
    	if (bObject == null)
    	{
    		return null;
    	}
    	
    	return bObject.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object convertToTypeClass(Object pObject) throws ModelException
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Boolean)
		{
			//optimization -> don't change it  
			return pObject;
		}		
		else if (pObject instanceof Number)
		{
			Number nObject = (Number)pObject;
			return Boolean.valueOf(nObject.intValue() != 0);
		}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			
			return Boolean.valueOf(sObject);
		}
		throw new ModelException("Conversion failed! Type not supported ! from " +  
			pObject.getClass().getName() + " to " + getTypeClass().getName());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize()
	{
		return 1;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public BooleanDataType clone()
	{
		return (BooleanDataType) super.clone();
	}	

} // BooleanDataType

