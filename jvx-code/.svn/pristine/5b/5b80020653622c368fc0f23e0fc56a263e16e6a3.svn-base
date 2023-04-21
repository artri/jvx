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
 * 12.04.2013 - [JR] - convertToTypeClass: check Timestamp.valueOf first
 */
package javax.rad.model.datatype;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

import com.sibvisions.util.Internalize;
import com.sibvisions.util.type.DateUtil;

/**
 * A <code>TimestampDataType</code> is the data type class 
 * of a <code>Timestamp</code> <code>ColumnDefinition</code>.
 *  
 * @author Roland Hörmann
 */
public class TimestampDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.TIMESTAMP;

	/** The format to convert a String to a Timestamp. */
	private static final String FORMAT = "0000-01-01 00:00:00.000000000";
	
	/** The fractional seconds precision. */
	private int iFractionalSecondsPrecision;
	
	/** The cell renderer. */
	private DateUtil dateUtil = new DateUtil();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Intitialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>ObjectDataType</code>.
	 */
	public TimestampDataType()
	{
	}
	
	/**
	 * Constructs a <code>ObjectDataType</code> cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public TimestampDataType(ICellEditor pCellEditor)
	{
		setCellEditor(pCellEditor);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementations
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
		return Timestamp.class;
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
			return dateUtil.format((Date)pObject);
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
    public Object convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
    	return Internalize.intern(convertToTypeClass(pObject));    	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object convertToTypeClass(Object pObject) throws ModelException
	{
		// TODO immutable timestamp! 
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Timestamp) 
		{
			return pObject;
		}
		else if (pObject instanceof Number) 
		{
			return new Timestamp(((Number)pObject).longValue());
		}
    	else if (pObject instanceof Date) 
    	{
    		return new Timestamp(((Date)pObject).getTime());
    	}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			else 
			{
			    String lowerValue = sObject.toLowerCase();
			    
			    if (lowerValue.startsWith("current_timestamp") || "current_date".equals(lowerValue) || "sysdate".equals(lowerValue) 
			            || "now()".equals(lowerValue) || "getdate()".equals(lowerValue))
			    {
			        return new Timestamp(new Date().getTime());
			    }
			}
			
			try
			{
				if (sObject.length() < FORMAT.length())
				{
					return Timestamp.valueOf(sObject.concat(FORMAT.substring(sObject.length())));
				}
				else
				{
					return Timestamp.valueOf(sObject);
				}
			}
			catch (Exception ex)
			{
				try
				{
					return new Timestamp(((Date)dateUtil.parse(sObject)).getTime());
				}
				catch (Exception exc)
				{
					// Conversion failed
				}
			}
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
		//TODO [HM] Displaysize.
		return FORMAT.length();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public TimestampDataType clone()
	{
	    TimestampDataType dataType = (TimestampDataType)super.clone();
	        
	    dataType.dateUtil = new DateUtil();
	    dataType.setDateFormat(getDateFormat());
	        
	    return dataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the current <code>DateFormat</code>.
	 * 
	 * @return the current <code>DateFormat</code>.
	 */
	public String getDateFormat()
	{
		return dateUtil.getDatePattern();
	}
	
	/**
	 * Sets the current <code>DateFormat</code>.
	 * 
	 * @param pDateFormat
	 * 				the new <code>DateFormat</code>.
	 */
	public void setDateFormat(String pDateFormat)
	{
		dateUtil.setDatePattern(pDateFormat);
	}

	/**
	 * Returns the fractional seconds precision of the <code>TimestampDataType</code>.
	 * 
	 * @return the fractional seconds precision of the <code>TimestampDataType</code>. 
	 */
    public int getFractionalSecondsPrecision()
    {
        return iFractionalSecondsPrecision;
    }

    /**
     * Sets the fractional seconds precision of the <code>TimestampDataType</code>.
     * For example in timestamp(6) fractional seconds precision = 6.
     * 
     * @param pFractionalSecondsPrecision the fractional seconds precision
     */
    public void setFractionalSecondsPrecision(int pFractionalSecondsPrecision)
    {
        iFractionalSecondsPrecision = pFractionalSecondsPrecision;
    }
	
} 	// TimestampDataType

