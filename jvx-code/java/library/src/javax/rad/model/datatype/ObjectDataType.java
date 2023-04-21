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
 * 08.09.2010 - [RH] - creation
 * 31.10.2010 - [JR] - removed compareTo because it only calls super.compareTo
 */
package javax.rad.model.datatype;

import java.sql.Types;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

/**
 * A <code>ObjectDataType</code> is the data type class of a 
 * Java Object as <code>ColumnDefinition</code>.
 *  
 * @author Roland Hörmann
 */
public class ObjectDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.JAVA_OBJECT;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a <code>ObjectDataType</code>.
	 */
	public ObjectDataType()
	{
	}
	
	/**
	 * Constructs a <code>ObjectDataType</code> cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public ObjectDataType(ICellEditor pCellEditor)
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
		return Object.class;
	}
	
    /**
     * {@inheritDoc}
     */
    public Object convertAndCheckToTypeClass(Object pObject) throws ModelException
	{		
    	return pObject;
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
	public Object convertToTypeClass(Object pObject) throws ModelException
	{
    	return pObject;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ObjectDataType clone()
	{
		return (ObjectDataType)super.clone();
	}

} 	// ObjectDataType
