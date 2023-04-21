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
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

import com.sibvisions.util.Internalize;
import com.sibvisions.util.type.NumberUtil;

/**
 * A <code>BigDecimalDataType</code> is the data type class for a 
 * <code>BigDecimal</code> column.
 * It stores type specific informations like precision and scale.<br>
 *  
 * @author Roland Hörmann
 */
public class BigDecimalDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.DECIMAL;
	
	/** The scale of the <code>BigDecimalDataType</code>. */
	private int	iScale			= -1;
	
	/** The precision of the <code>BigDecimalDataType</code>. */
	private int	iPrecision		= 0;
	
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
	public BigDecimalDataType()
	{
	}
	
	/**
	 * Constructs a default BigDecimal data type with the given precision and scale.
	 * 
	 * @param pPrecision the precision
	 * @param pScale the scale
	 */
	public BigDecimalDataType(int pPrecision, int pScale)
	{
		setPrecision(pPrecision);
		setScale(pScale);
	}
	
	/**
	 * Constructs a default BigDecimal data type with undefined precision, scale and the given cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public BigDecimalDataType(ICellEditor pCellEditor)
	{
		setCellEditor(pCellEditor);
	}
	
	/**
	 * Constructs a default BigDecimal data type with the given precision, scale and cell editor.
	 * 
	 * @param pPrecision the precision
	 * @param pScale the scale
	 * @param pCellEditor the cell editor
	 */
	public BigDecimalDataType(int pPrecision, int pScale, ICellEditor pCellEditor)
	{
		setPrecision(pPrecision);
		setScale(pScale);
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
		return BigDecimal.class;
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
	 * Trims unnecessary scale (0.00000) from a BigDecimal.
	 * @param pObject the BigDecimal to trim
	 * @return the trimmed BigDecimal
	 */
	public static final BigDecimal trimScale(BigDecimal pObject)
	{
		return removeExponent(pObject.stripTrailingZeros());
	}
	
	/**
	 * Trims unnecessary scale (0.00000) from a BigDecimal.
	 * @param pObject the BigDecimal to trim
	 * @return the trimmed BigDecimal
	 */
	public static final BigDecimal removeExponent(BigDecimal pObject)
	{
		if (pObject.scale() < 0)
		{
			return pObject.setScale(0);
		}
		else
		{
			return pObject;
		}
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
		else if (pObject instanceof BigDecimal) 
		{
			return removeExponent((BigDecimal)pObject);
		}
		else if (pObject instanceof Byte
				 || pObject instanceof Short
				 || pObject instanceof Integer
				 || pObject instanceof Long) 
		{
			return removeExponent(BigDecimal.valueOf(((Number)pObject).longValue()));
		}
		else if (pObject instanceof BigInteger) 
		{
			return removeExponent(new BigDecimal((BigInteger)pObject));
		}
		else if (pObject instanceof Number) 
		{
			double value = ((Number)pObject).doubleValue();
			
			if (value != value)
			{
				return null;
			}
			else
			{
				return trimScale(BigDecimal.valueOf(((Number)pObject).doubleValue()));
			}
		}
    	else if (pObject instanceof Date) 
    	{
    		Date dObject = (Date)pObject;	    		
    		return removeExponent(BigDecimal.valueOf(dObject.getTime()));
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
				return removeExponent(new BigDecimal(sObject));
			}
			catch (Exception ex)
			{
				try
				{
					return removeExponent((BigDecimal)numberUtil.parse(sObject));
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
    public BigDecimal convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
    	BigDecimal bdObject = (BigDecimal)convertToTypeClass(pObject);
		
    	if (bdObject == null)
    	{
    		return null;
    	}
    	
		// check if negative value is allowed
		if (bdObject.signum() < 0 
			&& !isSigned())
		{
			throw new ModelException("BigDecimal needs to be positive! - " + bdObject);			
		}
		
		int iObjectScale = bdObject.scale();
		int iObjectPrecision = bdObject.precision();
		if (iObjectScale == 0 && iObjectPrecision == 1 && BigDecimal.ZERO.equals(bdObject))
		{
		    iObjectPrecision = 0;
		}

		if ((getPrecision() <= 0 || iObjectPrecision - iObjectScale <= getPrecision() - Math.max(iObjectScale, getScale()))
			 && (getScale() < 0 || iObjectScale <= getScale()))
		{
			return Internalize.intern(trimScale(bdObject));
		}
		else
		{
			throw new ModelException("BigDecimal too large! - (precision/scale) from (" + 
									 iObjectPrecision + "/" + iObjectScale + ") to (" + 
									 getPrecision() + "/" + getScale() + ")");
		}
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
		if (getPrecision() == 0 && getScale() < 0)
		{
			return 38; 
		}
		int size = getPrecision();
		if (getScale() != 0) // If scale is allowed, a comma needs possible 1 character
		{
			size++;
		}
		if (getScale() > 0 && getPrecision() == getScale()) // If scale equals precision the number would be formatted to 0,xxx. 
		{
			size++;
		}
		if (isSigned()) // If the number is signed, the sign need possible 1 character
		{
			size++;
		}
		return size; 
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimalDataType clone()
	{
	    BigDecimalDataType dataType = (BigDecimalDataType)super.clone();
	    
	    dataType.numberUtil = new NumberUtil();
	    dataType.setNumberFormat(getNumberFormat());
	    
	    return dataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the precision of the <code>BigDecimalDataType</code>.
	 * 
	 * @return the precision of the <code>BigDecimalDataType</code>.
	 */
	public int getPrecision()
	{
		return iPrecision;
	}

	/**
	 * Sets the precision of the <code>BigDecimalDataType</code>.
	 * Example:
	 *
	 * <pre>
	 * number(5,2) means Precision = 5, Scale = 2
	 * number(5)   means Precision = 5, Scale = 0
	 * </pre>
	 * 
	 * @param pPrecision the precision of the <code>BigDecimalDataType</code>.
	 */
	public void setPrecision(int pPrecision)
	{
		if (pPrecision <= 0)
		{
			pPrecision = 0;
		}
		iPrecision = pPrecision;
	}
	
	/**
	 * Returns the scale of the <code>BigDecimalDataType</code>.
	 * 
	 * @return the scale of the <code>BigDecimalDataType</code>.
	 */
	public int getScale()
	{
		return iScale;
	}

	/**
	 * Sets the scale of the <code>BigDecimalDataType</code>.
	 * Example:
     *
     * <pre>
     * number(5,2) means Precision = 5, Scale = 2
     * number(5)   means Precision = 5, Scale = 0
     * </pre>
	 * 
	 * @param pScale the scale of the <code>BigDecimalDataType</code>.
	 */	
	public void setScale(int pScale)
	{
		if (pScale < -1)
		{
			pScale = -1;
		}
		iScale = pScale;
	}
	
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
	
} 	// BigDecimalDataType
