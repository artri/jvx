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
 * 28.04.2009 - [RH] - creation
 * 03.05.2009 - [RH] - get/setLabel and static helper functions added 
 * 14.07.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 27.09.2009 - [RH] - SQL Type added
 * 23.11.2009 - [RH] - get/setLinkReference added for automatic linked celleditor support
 *                     AutoIncrement, PrimaryKey moved to MetaData class
 * 04.12.2009 - [RH] - QueryColumns with alias bug fixed - set/getRealColumnName added          
 * 02.03.2010 - [RH] - AutoLinkStorage, RealColumnName functions moved to ServerColumnMetaData class.
 * 13.03.2010 - [JR] - #88: used TYPE_IDENTIFIER instead of java class names
 * 25.03.2010 - [JR] - #92: set/getDefaultValue implemented
 * 28.03.2010 - [JR] - #47: allowed values implemented
 * 30.03.2010 - [RH] - #94: Default Label should be set in the server class 
 * 16.02.2011 - [JR] - #287: clone implemented
 * 17.12.2011 - [JR] - moved SQL type to ServerColumnMetaData (it is a server info)    
 * 23.02.2013 - [JR] - equals and hashCode implemented         
 */
package javax.rad.persist;

import java.io.Serializable;
import java.util.Arrays;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.BooleanDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.LongDataType;
import javax.rad.model.datatype.ObjectDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.reference.StorageReferenceDefinition;

import com.sibvisions.util.type.StringUtil;

/**
 * A <code>ColumnMetaData</code> is a description of the data type and other
 * attributes of a table storage column. (persists on server)
 *  
 * @see javax.rad.model.datatype.IDataType
 * 
 * @author Roland Hörmann
 */
public class ColumnMetaData implements Serializable,
                                       Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The name of the <code>ColumnMetaData</code>. */
	private String			sName;
	
	/** The default label of the <code>ColumnMetaData</code>. */
	private String			sLabel;

	/** The data type identifier of the <code>ColumnMetaData</code>. */
	private int		   	 	iTypeIdentifier = StringDataType.TYPE_IDENTIFIER;

	/** If this <code>ColumnDefinition</code> can be null. */
	private boolean			bNullable = true;

	/** The precision/size of this <code>ColumnMetaData</code>. */
	private int			    iPrecision = Integer.MAX_VALUE;
		
	/** The scale of this <code>ColumnMetaData</code>. */
	private int			    iScale = -1;

	/** Determines if this <code>ColumnMetaData</code> is signed. */
	private boolean		    bSigned = true;

	/** If this <code>ColumnMetaData</code> is writeable. */
	private boolean			bWriteable	= false;

	/** If this <code>ColumnMetaData</code> is calculated. */
	private boolean			bCalculated	= true;

	/** If this <code>ColumnMetaData</code> is an auto increment column. */
	private boolean			bAutoIncrement	= false;

	/** If this <code>ColumnMetaData</code> fetches larger objects lazily. */ 
	private boolean			bFetchLargeObjectsLazy = true;
	
	/** Whether this column is required in the filter. */
    private boolean         bFilterRequired = false;
	
	/** the default value for the column. */
	private Object 			oDefault;
	
	/** the allowed values for the column. */
	private Object[]		oAllowedValues;

	/** The link reference for a server side dropdown list (automatic linked celleditor). */
	private StorageReferenceDefinition	srdLinkReference;
	
	/** Cached data type for performance. */
	protected IDataType cachedDataType = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>ColumnMetaData</code> with defaults.
	 */
	public ColumnMetaData()
	{
	}
	
	/**
	 * Constructs a <code>ColumnMetaData</code> with a specific name.
	 * 
	 * @param pName the column name
	 */
	public ColumnMetaData(String pName)
	{
		sName = pName;
	}
	
	/**
	 * Constructs a <code>ColumnMetaData</code> with a specific name and data type identifier.
	 * 
	 * @param pName the column name
	 * @param pTypeIdentifier the data type identifier
	 */
	public ColumnMetaData(String pName, int pTypeIdentifier)
	{
		sName = pName;
		iTypeIdentifier = pTypeIdentifier;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sbResult = new StringBuilder();
		
		sbResult.append("ColumnMetaData ::[");
		sbResult.append(sName);
		sbResult.append("],Type=");
		sbResult.append(iTypeIdentifier);
		sbResult.append(",Label=");
		sbResult.append(sLabel);
		sbResult.append(",Nullable=");
		sbResult.append(bNullable);
		sbResult.append(",Precision=");
		sbResult.append(iPrecision);
		sbResult.append(",Scale=");
		sbResult.append(iScale);
		sbResult.append(",Signed=");
		sbResult.append(bSigned);
		sbResult.append(",Writeable=");
		sbResult.append(bWriteable);
        sbResult.append(",AutoIncrement=");
        sbResult.append(bAutoIncrement);
		sbResult.append(",Calculated=");
		sbResult.append(bCalculated);
		sbResult.append(",FetchLargeObjectsLazy=");
		sbResult.append(bFetchLargeObjectsLazy);
		sbResult.append(",FilterRequired=");
		sbResult.append(bFilterRequired);
		sbResult.append(",Default=");
		sbResult.append(oDefault);
		sbResult.append(",Allowed=");
		sbResult.append(StringUtil.toString(oAllowedValues));
		
		return sbResult.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMetaData clone()
	{
		try
		{
			ColumnMetaData cmd = (ColumnMetaData)super.clone();
		
			if (oAllowedValues != null)
			{
				cmd.oAllowedValues = oAllowedValues.clone();
			}
			
			return cmd;
		}
		catch (CloneNotSupportedException cnse)
		{
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		if (this == pObject)
		{
			return true;
		}
		
		if (pObject == null)
		{
			return false;
		}
		
		if (getClass() != pObject.getClass())
		{
			return false;
		}
		
		ColumnMetaData cmdCompare = (ColumnMetaData)pObject;
		
		if (bAutoIncrement != cmdCompare.bAutoIncrement)
		{
			return false;
		}
		
		if (bNullable != cmdCompare.bNullable)
		{
			return false;
		}
		
		if (bSigned != cmdCompare.bSigned)
		{
			return false;
		}
		
		if (bWriteable != cmdCompare.bWriteable)
		{
			return false;
		}
		
		if (bCalculated != cmdCompare.bCalculated)
		{
			return false;
		}
		
		if (iTypeIdentifier != cmdCompare.iTypeIdentifier)
		{
			return false;
		}
		
		if (iPrecision != cmdCompare.iPrecision)
		{
			return false;
		}
		
		if (iScale != cmdCompare.iScale)
		{
			return false;
		}
		
		if (!Arrays.equals(oAllowedValues, cmdCompare.oAllowedValues))
		{
			return false;
		}
		
		if (oDefault == null)
		{
			if (cmdCompare.oDefault != null)
			{
				return false;
			}
		}
		else if (!oDefault.equals(cmdCompare.oDefault))
		{
			return false;
		}
		
		if (sLabel == null)
		{
			if (cmdCompare.sLabel != null)
			{
				return false;
			}
		}
		else if (!sLabel.equals(cmdCompare.sLabel))
		{
			return false;
		}
		
		if (sName == null)
		{
			if (cmdCompare.sName != null)
			{
				return false;
			}
		}
		else if (!sName.equals(cmdCompare.sName))
		{
			return false;
		}
		
		if (srdLinkReference == null)
		{
			if (cmdCompare.srdLinkReference != null)
			{
				return false;
			}
		}
		else if (!srdLinkReference.equals(cmdCompare.srdLinkReference))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int iPrime = 7;
		
		int iResult = iPrime + (bAutoIncrement ? 1231 : 1237);
		iResult = iPrime * iResult + (bNullable ? 1231 : 1237);
		iResult = iPrime * iResult + (bSigned ? 1231 : 1237);
		iResult = iPrime * iResult + (bWriteable ? 1231 : 1237);
		iResult = iPrime * iResult + (bCalculated ? 1231 : 1237);
		iResult = iPrime * iResult + (bFetchLargeObjectsLazy ? 1231 : 1237);
		iResult = iPrime * iResult + (bFilterRequired ? 1231 : 1237);
		iResult = iPrime * iResult + iTypeIdentifier;
		iResult = iPrime * iResult + iPrecision;
		iResult = iPrime * iResult + iScale;
		iResult = iPrime * iResult + Arrays.hashCode(oAllowedValues);
		iResult = iPrime * iResult + ((oDefault == null) ? 0 : oDefault.hashCode());
		iResult = iPrime * iResult + ((sLabel == null) ? 0 : sLabel.hashCode());
		iResult = iPrime * iResult + ((sName == null) ? 0 : sName.hashCode());
		iResult = iPrime * iResult + ((srdLinkReference == null) ? 0 : srdLinkReference.hashCode());
		return iResult;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Set the name of the column.
	 * 
	 * @param pName column name
	 */
	public void setName(String pName)
	{
		sName = pName;
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getName()
	{
		return sName;
	}

	/**
	 * Returns the default label.
	 *
	 * @return the default label.
	 */
	public String getLabel()
	{
		if (sLabel == null)
		{
			return ColumnMetaData.getDefaultLabel(sName);
		}
		return sLabel;
	}

	/**
	 * Sets default label.
	 *
	 * @param pLabel the default label. to set
	 */
	public void setLabel(String pLabel)
	{
		sLabel = pLabel;
	}
	
	/**
	 * If large objects in this column should be lazily fetched,
	 * meaning that they are only send to the client if the client actually
	 * requests that very value.
	 * 
	 * The definition of "large object" is a matter of implementation of the
	 * data access class, and so is if this property is honored or not.
	 * 
	 * @return {@code true} if large objects should be fetched lazy.
	 */
	public boolean isFetchLargeObjectsLazy()
	{
		return bFetchLargeObjectsLazy;
	}
	
	/**
	 * Sets if large objects in this column should be lazily fetched,
	 * meaning that they are only send to the client if the client actually
	 * requests that very value.
	 * 
	 * The definition of "large object" is a matter of implementation of the
	 * data access class, and so is if this property is honored or not.
	 * 
	 * @param pFetchLargeObjectsLazy {@code true} if large objects should be fetched lazy.
	 */
	public void setFetchLargeObjectsLazy(boolean pFetchLargeObjectsLazy)
	{
		bFetchLargeObjectsLazy = pFetchLargeObjectsLazy;
	}

	/**
     * Whether the column is required in the filter or not.
     * 
     * @return true if column is required in the filter
     */
	public boolean isFilterRequired()
    {
        return bFilterRequired;
    }

	/**
     * Sets whether this column is required in the filter or not.
     * 
     * @param pFilterRequired true if column is required in the filter
     */
    public void setFilterRequired(boolean pFilterRequired)
    {
        bFilterRequired = pFilterRequired;
    }

    /**
	 * Sets the used data type for this <code>ColumnMetaData</code>.
	 * 
	 * @param pTypeIdentifier the data type identifier for this <code>ColumnMetaData</code>.
	 */
	public void setTypeIdentifier(int pTypeIdentifier)
	{
		iTypeIdentifier = pTypeIdentifier;
		
		cachedDataType = null;
	}

	/**
	 * Returns the used data type for this <code>ColumnMetaData</code>.
	 * 
	 * @return the data type identifier for this <code>ColumnMetaData</code>.
	 */
	public int getTypeIdentifier()
	{
		return iTypeIdentifier;
	}

	/**
	 * Sets whether values in this column may be null.
	 * 
	 * @param pNullable true if values in this column may be null.
	 */
	public void setNullable(boolean pNullable)
	{
		bNullable = pNullable;
	}

	/**
	 * Returns true if values in this column may be null.
	 * 
	 * @return true if values in this column may be null.
	 */
	public boolean isNullable()
	{
		return bNullable;
	}

	/**
	 * Returns the precision/size of this column. 
	 * 
	 * @return the precision/size of this column. 
	 */
	public int getPrecision()
	{
		return iPrecision;
	}

	/**
	 * Sets the precision/size of this column. 
	 * 
	 * @param pPrecision
	 *            the precision/size of this column.
	 */
	public void setPrecision(int pPrecision)
	{
		iPrecision = pPrecision;
		
		cachedDataType = null;
	}

	/**	
	 * Returns the scale of this column. 
	 * 
	 * @return the scale of this column. 
	 */
	public int getScale()
	{
		return iScale;
	}

	/**
	 * Sets the scale of this column. 
	 * 
	 * @param pScale
	 *            the scale of this column.
	 */
	public void setScale(int pScale)
	{
		iScale = pScale;
		
		cachedDataType = null;
	}

	/**
	 * Sets whether this column is writeable.
	 * 
	 * @param pWriteable	true if column is writeable.
	 */
	public void setWritable(boolean pWriteable)
	{
		bWriteable = pWriteable;
	}

	/**
	 * Returns whether this column is writeable.
	 * 
	 * @return true if column is writeable.
	 */
	public boolean isWritable()
	{
		return bWriteable;
	}

	/**
	 * Sets whether this column is calculated.
	 * 
	 * @param pCalculated	true if column is calculated.
	 */
	public void setCalculated(boolean pCalculated)
	{
		bCalculated = pCalculated;
	}

	/**
	 * Returns whether this column is calculated.
	 * 
	 * @return true if column is calculated.
	 */
	public boolean isCalculated()
	{
		return bCalculated;
	}

	/**
	 * Returns if this <code>ColumnMetaData</code> is signed.
	 *
	 * @return if this <code>ColumnMetaData</code> is signed.
	 */
	public boolean isSigned()
	{
		return bSigned;
	}

	/**
	 * Sets if this <code>ColumnMetaData</code> is signed.
	 *
	 * @param pSigned true, if signed.
	 */
	public void setSigned(boolean pSigned)
	{
		bSigned = pSigned;
		
		cachedDataType = null;
	}
	
	/**
	 * Returns <code>true</code> if this <code>ColumnMetaData</code> is an auto increment column.
	 *
	 * @return <code>true</code> if this <code>ColumnMetaData</code> is an auto increment column.
	 */
	public boolean isAutoIncrement()
	{
		return bAutoIncrement;
	}

	/**
	 * Sets if this <code>ColumnMetaData</code> is an auto increment column.
	 *
	 * @param autoIncrement 
	 *			the bAutoIncrement to set
	 */
	public void setAutoIncrement(boolean autoIncrement)
	{
		bAutoIncrement = autoIncrement;
	}

	/**
	 * Returns the link reference for a server side dropdown list (automatic linked celleditor).
	 *
	 * @return the link reference for an server side dropdown list (automatic linked celleditor).
	 */
	public StorageReferenceDefinition getLinkReference()
	{
		return srdLinkReference;
	}

	/**
	 * Sets the link reference for an server side Dropdown list (automatic linked celleditor).
	 *
	 * @param pLinkReference the link reference to set
	 */
	public void setLinkReference(StorageReferenceDefinition pLinkReference)
	{
		srdLinkReference = pLinkReference;
	}	
	
	/**
	 * It converts a server ColumnMetaData to an client ColumnDefinition and returns it.
	 *  
	 * @return the ColumnDefinition for the ColumnMetaData.
	 * @throws ModelException	if the ColumnDefintion couldn't created
	 */
	public ColumnDefinition createColumnDefinition() throws ModelException
	{
		return initializeColumnDefinition(new ColumnDefinition());
	}
	
	/**
	 * Initializes the ColumnDefinition with the given <code>ColumnMetaData</code>.
	 * 
	 * @param pColumnDefinition		the ColumnDefinition to use.
	 * @return ColumnDefinition the result column definition
	 * @throws ModelException	if the ColumnDefintion couldn't created
	 */
	protected ColumnDefinition initializeColumnDefinition(ColumnDefinition pColumnDefinition) throws ModelException
	{
		pColumnDefinition.setName(getName());
        pColumnDefinition.setDefaultLabel(getLabel());
		pColumnDefinition.setWritable(isWritable());
		pColumnDefinition.setNullable(isNullable());
		pColumnDefinition.setDefaultValue(getDefaultValue());
		pColumnDefinition.setAllowedValues(getAllowedValues());
		pColumnDefinition.setFilterRequired(isFilterRequired());

		pColumnDefinition.setDataType(createDataType());
		
        // All server columns are writeable from the view of a client. Thats because this columns are written to the server.
        // If the server writes this changes to the storage depend on the writable state of the column from the server and
        // not from the client.
		pColumnDefinition.setWritable(true);
		
		// Initialize filterable and sortable
		// - Calculated columns cannot be filtered and sorted
		// - Binary types cannot be filtered and sorted
		// - Clobs cannot be filtered and sorted, but we cannot check it here, these columns has to be disabled manually.
		boolean filterAndSortable = !isCalculated() 
		        && getTypeIdentifier() != BinaryDataType.TYPE_IDENTIFIER;
		
		pColumnDefinition.setFilterable(filterAndSortable);
		pColumnDefinition.setSortable(filterAndSortable);
        
		return pColumnDefinition; 
	}
	
	/**
	 * It gets the corresponding <code>IDataType</code> to the meta data column.
	 * 
	 * @return  to the meta data column the corresponding <code>IDataType</code>.
	 */
	public IDataType getDataType()
	{
		if (cachedDataType == null)
		{
			cachedDataType = createDataType();
		}
		
		return cachedDataType;
	}

	/**
	 * It creates the corresponding <code>IDataType</code> to the meta data column.
	 * 
	 * @return  to the meta data column the corresponding <code>IDataType</code>.
	 */
	public IDataType createDataType()
	{
		switch (getTypeIdentifier())
		{
			case StringDataType.TYPE_IDENTIFIER:

				StringDataType dtString = new StringDataType();
				dtString.setSize(getPrecision());
				dtString.setAutoTrimEnd(dtString.getSize() > 2); // if it is more than a char(2) column it is supposed to be a text that should be trimmed.  
				
				return dtString;
				
			case BooleanDataType.TYPE_IDENTIFIER:

				return new BooleanDataType();
				
			case BigDecimalDataType.TYPE_IDENTIFIER:

				BigDecimalDataType dtBigDecimal = new BigDecimalDataType();
				dtBigDecimal.setPrecision(getPrecision());
				dtBigDecimal.setScale(getScale());
				dtBigDecimal.setSigned(isSigned());
				
				return dtBigDecimal;

			case LongDataType.TYPE_IDENTIFIER:

				LongDataType dtLong = new LongDataType();
				dtLong.setSigned(isSigned());
				
				return dtLong;

			case TimestampDataType.TYPE_IDENTIFIER:

			    TimestampDataType dtTimestamp = new TimestampDataType();
			    dtTimestamp.setFractionalSecondsPrecision(getScale());
			    
				return dtTimestamp;

			case BinaryDataType.TYPE_IDENTIFIER:

				BinaryDataType dtBinary = new BinaryDataType();
				dtBinary.setSize(getPrecision());
				
				return dtBinary;

			case ObjectDataType.TYPE_IDENTIFIER:

				return new ObjectDataType();
				
			default:
				
				throw new IllegalArgumentException("Type identifier " + getTypeIdentifier() + " is not supported!");
		}
	}
	
	/**
	 * It returns an default label for the pName.
	 * 
	 * @param pName	the Column Name to use.
	 * @return the default column label
	 */
	public static String getDefaultLabel(String pName)
	{
		return StringUtil.formatInitCap(pName);
	}

	/**
	 * Sets the default value of this column.
	 * 
	 * @param pValue the default value
	 */
	public void setDefaultValue(Object pValue)
	{
		oDefault = pValue;
	}
	
	/**
	 * Gets the default value of this column.
	 * 
	 * @return the default value or <code>null</code> if the column has no default value
	 */
	public Object getDefaultValue()
	{
		return oDefault;
	}
	
	/**
	 * Sets the allowed values for this column.
	 * 
	 * @param pValues the allowed values or <code>null</code> when any value is allowed
	 */
	public void setAllowedValues(Object[] pValues)
	{
		oAllowedValues = pValues;
	}
	
	/**
	 * Gets the allowed values for this column.
	 * 
	 * @return an {@link Object}[] with values, possible for the column.
	 */
	public Object[] getAllowedValues()
	{
		return oAllowedValues;
	}	
	
} 	// ColumnMetaData

