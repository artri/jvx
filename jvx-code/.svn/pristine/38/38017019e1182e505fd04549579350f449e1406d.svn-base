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
import javax.rad.util.ITranslator;

import com.sibvisions.util.Internalize;
import com.sibvisions.util.type.StringUtil;

/**
 * A <code>StringDataType</code> is the data type class of a 
 * <code>String</code> <code>ColumnDefinition</code>. 
 *  
 * @author Roland Hörmann
 */
public class StringDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.VARCHAR;

	/** The translator for translating the text. */
	private ITranslator translator;
	
	/** Enable autotrim to avoid whitespaces at the begin and end of texts. */ 
	private boolean autoTrimEnd = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>StringDataType</code> with the maximum <code>Integer</code> size.
	 */
	public StringDataType()
	{
	}

	/**
	 * Constructs a <code>StringDataType</code> with the given <code>Integer</code> size.
	 * 
	 * @param pSize the size
	 */
	public StringDataType(int pSize)
	{
		setSize(pSize);
	}
	
	/**
	 * Constructs a <code>StringDataType</code> with the maximum <code>Integer</code> size and the given cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public StringDataType(ICellEditor pCellEditor)
	{
		setCellEditor(pCellEditor);
	}
	
	/**
	 * Constructs a <code>StringDataType</code> with the given <code>Integer</code> size and the given cell editor.
	 * 
	 * @param pSize the size
	 * @param pCellEditor the cell editor
	 */
	public StringDataType(int pSize, ICellEditor pCellEditor)
	{
		setSize(pSize);
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
		return String.class;
	}
	
    /**
     * {@inheritDoc}
     */
    public Object convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
		String sObject = (String)convertToTypeClass(pObject);
		if (sObject == null)
		{
			return null;
		}
		else if (isAutoTrimEnd())
		{
		    sObject = StringUtil.rtrim(sObject);
		    if (sObject.length() == 0)
            {
                return null;
            }
		}

		if (sObject.length() > getSize())
		{
			throw new ModelException("String is too large! - length from " + 
									 sObject.length() + " to " + getSize());
		}

		return Internalize.intern(sObject);
	}

	/**
	 * {@inheritDoc}
	 */
    public String convertToString(Object pObject)
	{
		return (String)pObject;
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
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			
			return sObject;
		}
		else if (pObject instanceof byte[])
		{
			byte[] baObject = (byte[])pObject;
			if (baObject.length == 0)
			{
				return null;
			}
			String sObject = new String(baObject);

			if (sObject.length() == 0)
			{
				return null;
			}
			
			return sObject;
		}
		else
		{
			String sObject = pObject.toString();

			if (sObject.length() == 0)
			{
				return null;
			}
			
			return sObject;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object prepareValue(Object pObject) throws ModelException
	{
		if (translator == null)
		{
			return pObject;
		}
		else
		{
			return translator.translate((String)pObject);
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public StringDataType clone()
	{
		return (StringDataType) super.clone();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the translator for translating.
	 * @return the translator
	 */
	public ITranslator getTranslator() 
	{
		return translator;
	}

	/**
	 * Sets the translator for translating.
	 * If a translator is set, the values are translated.
	 * 
	 * @param pTranslator the translator
	 */
	public void setTranslator(ITranslator pTranslator) 
	{
		translator = pTranslator;
	}

	/**
	 * Gets, if string ends are auto trimmed.
	 * All white spaces are removed at the end.
	 * 
	 * @return true, if string ends are auto trimmed.
	 */
	public boolean isAutoTrimEnd() 
	{
		return autoTrimEnd;
	}

	/**
	 * Sets, if string ends are auto trimmed.
	 * All white spaces are removed at the end.
	 * 
	 * @param pAutoTrimEnd true, if string ends are auto trimmed.
	 */
	public void setAutoTrimEnd(boolean pAutoTrimEnd) 
	{
		autoTrimEnd = pAutoTrimEnd;
	}
	
} 	// StringDataType

