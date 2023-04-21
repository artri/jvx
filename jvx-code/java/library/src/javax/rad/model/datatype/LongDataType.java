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
 * 30.01.2009 - [RH] - if not allowed value, then anything is allowed. precision <=0 && scale <=-1 -> ADDED
 * 13.03.2010 - [JR] - #88: getTypeIdentifier implemented
 * 24.06.2011 - [JR] - #401: trim strings in automatic conversion
 * 26.05.2013 - [RH] - #663: BigDecimalDataType return wrong size, if scale == -1 and precision==0 
 */
package javax.rad.model.datatype;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

import com.sibvisions.util.Internalize;
import com.sibvisions.util.type.NumberUtil;

/**
 * A <code>LongDataType</code> is the data type class for a 
 * <code>BIGINT</code> column.
 *  
 * @author Martin Handsteiner
 */
public class LongDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.BIGINT;
	
	/** Indicates if the <code>BigDecimalDataType</code> is signed. */
	private boolean	bSigned		= true;
	
	/** The cell renderer. */
	private NumberUtil numberUtil = new NumberUtil();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a default BigDecimal data type with undefined precision and scale.
	 */
	public LongDataType()
	{
	}
	
	/**
	 * Constructs a default BigDecimal data type with undefined precision, scale and the given cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public LongDataType(ICellEditor pCellEditor)
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
		return Long.class;
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
		
		try
		{
			return numberUtil.format((Number)pObject);
		}
		catch (Exception ex)
		{
			return pObject.toString();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String convertToUnifiedString(Object pObject)
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
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Long) 
		{
			return pObject;
		}
		else if (pObject instanceof Number) 
		{
			return Long.valueOf(((Number)pObject).longValue());
		}
    	else if (pObject instanceof Date) 
    	{
    		return Long.valueOf(((Date)pObject).getTime());
    	}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString().trim();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			try
			{
				return Long.valueOf(sObject);
			}
			catch (Exception ex)
			{
				try
				{
					return Long.valueOf(((BigDecimal)numberUtil.parse(sObject)).longValue());
				}
				catch (Exception exc)
				{
					// conversion failed
				}
			}
		}		
		throw new ModelException("Conversion failed! Type not supported ! from " +  
				pObject.getClass().getName() + " to " + getTypeClass().getName());
	}
	
    /**
     * {@inheritDoc}
     */
    public Long convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
    	Long lObject = (Long)convertToTypeClass(pObject);
		
    	if (lObject == null)
    	{
    		return null;
    	}
    	
		// check if negative value is allowed
		if (lObject.longValue() < 0 
			&& !isSigned())
		{
			throw new ModelException("BigDecimal needs to be positive! - " + lObject);			
		}

		return Internalize.intern(lObject);
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
		return 19; 
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public LongDataType clone()
	{
		return (LongDataType) super.clone();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns if the <code>BigDecimalDataType</code> is signed.
	 * 
	 * @return if the <code>BigDecimalDataType</code> is signed.
	 */
	public boolean isSigned()
	{
		return bSigned;
	}

	/**
	 * Sets if the <code>BigDecimalDataType</code> is signed.
	 * 
	 * @param pSigned 
	 * 			the signed value of the <code>BigDecimalDataType</code>.
	 */	
	public void setSigned(boolean pSigned)
	{
		bSigned = pSigned;
	}
	
	/**
	 * Returns the current <code>NumberFormat</code>.
	 * 
	 * @return the current <code>NumberFormat</code>.
	 */
	public String getNumberFormat()
	{
		return numberUtil.getNumberPattern();
	}
	
	/**
	 * Sets the current <code>NumberFormat</code>.
	 * 
	 * @param pNumberFormat the new <code>NumberFormat</code>.
	 */
	public void setNumberFormat(String pNumberFormat)
	{
		numberUtil.setNumberPattern(pNumberFormat);
	}
	
} 	// LongDataType
