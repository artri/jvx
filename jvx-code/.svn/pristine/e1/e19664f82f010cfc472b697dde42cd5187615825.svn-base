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
 * 02.03.2010 - [RH] - creation
 * 25.03.2010 - [JR] - #92: set/getDefaltValue implemented
 * 28.03.2010 - [JR] - #47: allowed values implemented
 * 21.04.2010 - [JR] - getDataType now returns an IDataType
 * 23.11.2010 - [RH] - get/setSQLTypeName added
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented
 * 17.12.2011 - [JR] - #498: detected type introduced            
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.Types;

import javax.rad.model.datatype.IDataType;
import javax.rad.model.reference.StorageReferenceDefinition;
import javax.rad.persist.ColumnMetaData;

/**
 * A <code>ServerColumnMetaData</code> is a description of the data type and other
 * attributes of a table storage column. (persists on server)
 * It also includes the server relevant infos, in addition to the <code>ColumnMetaData</code> just for the client.
 *  
 * @see javax.rad.model.datatype.IDataType
 * @see javax.rad.persist.ColumnMetaData
 * 
 * @author Roland Hörmann
 */
public class ServerColumnMetaData implements Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The ColumnMetaData for the client. */
	private ColumnMetaData	columnMetaData;
	
	/** 
	 * The name the use in the database (real column) to query the column. e.g. w.name alias -> sRealQueryColumnName=w.name, ColumnName = alias. 
	 * The real DB column name, which is specified from the developer over the queryColumns to use for query, filter and sort (only Query side)
	 */
	private String			sRealQueryColumnName;
	
	/** 
	 * The name the use in the database (real column, case sensitive) for the column. e.g. DfDfD  
	 * The real column name for writeback, insert, update, delete, and query if no special queryColumn is specified.
	 */
	private Name			nColumnName;

	/** The SQL Type identifier. */
	private int				iSQLType;
	
	/** 
	 * the detected type identfier. This is important if a SQL type <code>Types.OTHER</code> is detected, but 
	 * another type should be used. */
	private int				iDetectedType = Types.OTHER;
	
	/** The SQL Type name. */
	private String			sSQLTypeName;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>ServerColumnMetaData</code> with defaults.
	 */
	public ServerColumnMetaData()
	{
	}
	
	/**
	 * Constructs a <code>ServerColumnMetaData</code> with a specific name.
	 * 
	 * @param pName the column name
	 */
	public ServerColumnMetaData(Name pName)
	{
		this(pName, new ColumnMetaData());
	}
		
	/**
	 * Constructs a <code>ServerColumnMetaData</code> with a specific name and Column MetaData.
	 * 
	 * @param pName the column name
	 * @param pColumnMetaData the column metaData
	 */
	public ServerColumnMetaData(Name pName, ColumnMetaData pColumnMetaData)
	{
		nColumnName    = pName;
		columnMetaData = pColumnMetaData;
		
		columnMetaData.setName(pName.getName());
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
		
		sbResult.append("Server");
		sbResult.append(columnMetaData);
		sbResult.append(",sRealQueryColumnName=");
		sbResult.append(sRealQueryColumnName);
		sbResult.append(",sColumnName=");
		sbResult.append(nColumnName);
		
		return sbResult.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServerColumnMetaData clone()
	{
		try
		{
			ServerColumnMetaData result = (ServerColumnMetaData)super.clone();
			
			result.columnMetaData = columnMetaData.clone();
			
			return result;
		}
		catch (CloneNotSupportedException cnse)
		{
		    // this shouldn't happen, since we are Cloneable
		    throw new InternalError();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the ColumnMetaData client infos.
	 * 
	 * @return the ColumnMetaData client infos.
	 */
	public ColumnMetaData getColumnMetaData()
	{
		return columnMetaData;
	}

	/**
	 * Sets the ColumnMetaData client infos.
	 * 
	 * @param pColumnMetaData the ColumnMetaData client infos.
	 */
	public void setColumnMetaData(ColumnMetaData pColumnMetaData)
	{
		columnMetaData = pColumnMetaData;
	}

	/**
	 * It gets the corresponding <code>IDataType</code> to the meta data column.
	 * 
	 * @return  to the meta data column the corresponding <code>IDataType</code>.
	 */
	public IDataType getDataType()
	{
		return columnMetaData.getDataType();
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getName()
	{
		return columnMetaData.getName();
	}
	
	/**
	 * Returns the quoted column name.
	 * 
	 * @return the quoted column name
	 */
	public String getQuotedName()
	{
		return nColumnName.getQuotedName();
	}
	
	/**
	 * Returns the name object for that column. 
	 *
	 * @return the name object for that column. 
	 */
	public Name getColumnName()
	{
		return nColumnName;	
	}	

	/**
	 * Returns the default label.
	 *
	 * @return the default label.
	 */
	public String getLabel()
	{
		return columnMetaData.getLabel();
	}

	/**
	 * Sets default label.
	 *
	 * @param pLabel the default label. to set
	 */
	public void setLabel(String pLabel)
	{
		columnMetaData.setLabel(pLabel);
	}
	
	/**
	 * Sets the used data type this <code>ColumnMetaData</code>.
	 * 
	 * @param pTypeIdentifier the type identifier for this <code>ColumnMetaData</code>.
	 */
	public void setTypeIdentifier(int pTypeIdentifier)
	{
		columnMetaData.setTypeIdentifier(pTypeIdentifier);
	}

	/**
	 * Returns the used data type for this <code>ServerColumnMetaData</code>.
	 * 
	 * @return the java data type class for this <code>ServerColumnMetaData</code>.
	 */
	public int getTypeIdentifier()
	{
		return columnMetaData.getTypeIdentifier();
	}

	/**
	 * Sets whether values in this column may be null.
	 * 
	 * @param pNullable
	 *            true if values in this column may be null.
	 */
	public void setNullable(boolean pNullable)
	{
		columnMetaData.setNullable(pNullable);
	}

	/**
	 * Returns true if values in this column may be null.
	 * 
	 * @return true if values in this column may be null.
	 */
	public boolean isNullable()
	{
		return columnMetaData.isNullable();
	}

	/**
	 * Returns the precision/size of this column. 
	 * 
	 * @return the precision/size of this column. 
	 */
	public int getPrecision()
	{
		return columnMetaData.getPrecision();
	}

	/**
	 * Sets the precision/size of this column. 
	 * 
	 * @param pPrecision
	 *            the precision/size of this column.
	 */
	public void setPrecision(int pPrecision)
	{
		columnMetaData.setPrecision(pPrecision);
	}

	/**	
	 * Returns the scale of this column. 
	 * 
	 * @return the scale of this column. 
	 */
	public int getScale()
	{
		return columnMetaData.getScale();
	}

	/**
	 * Sets the scale of this column. 
	 * 
	 * @param pScale
	 *            the scale of this column.
	 */
	public void setScale(int pScale)
	{
		columnMetaData.setScale(pScale);
	}

	/**	
	 * Returns the SQL type of this column. 
	 * 
	 * @return the SQL type of this column. 
	 */
	public int getSQLType()
	{
		return iSQLType;
	}

	/**
	 * Sets the SQL type of this column. 
	 * 
	 * @param pSQLType the SQL type of this column.
	 */
	public void setSQLType(int pSQLType)
	{
		iSQLType = pSQLType;
	}

	/**	
	 * Returns the detected type of this column. 
	 * 
	 * @return the detected type of this column. 
	 */
	public int getDetectedType()
	{
		if (iDetectedType == Types.OTHER)
		{
			return iSQLType;
		}
		else
		{
			return iDetectedType;
		}
	}

	/**
	 * Sets the detected type of this column. 
	 * 
	 * @param pDetectedType the detected type of this column.
	 */
	public void setDetectedType(int pDetectedType)
	{
		iDetectedType = pDetectedType;
	}
	
	/**	
	 * Returns the SQL type name of this column. 
	 * 
	 * @return the SQL type name of this column. 
	 */
	public String getSQLTypeName()
	{
		return sSQLTypeName;
	}

	/**
	 * Sets the SQL type name of this column. 
	 * 
	 * @param pSQLTypeName the SQL type name of this column.
	 */
	public void setSQLTypeName(String pSQLTypeName)
	{
		sSQLTypeName = pSQLTypeName;
	}
	
	/**
	 * Sets whether this column is writeable.
	 * 
	 * @param pWriteable	true if column is writeable.
	 */
	public void setWritable(boolean pWriteable)
	{
		columnMetaData.setWritable(pWriteable);
	}

	/**
	 * Returns whether this column is writeable.
	 * 
	 * @return true if column is writeable.
	 */
	public boolean isWritable()
	{
		return columnMetaData.isWritable();
	}

	/**
	 * Sets whether this column is calculated.
	 * 
	 * @param pCalculated	true if column is calculated.
	 */
	public void setCalculated(boolean pCalculated)
	{
		columnMetaData.setCalculated(pCalculated);
	}

	/**
	 * Returns whether this column is writeable.
	 * 
	 * @return true if column is writeable.
	 */
	public boolean isCalculated()
	{
		return columnMetaData.isCalculated();
	}

	/**
	 * Returns if this <code>ColumnMetaData</code> is signed.
	 *
	 * @return if this <code>ColumnMetaData</code> is signed.
	 */
	public boolean isSigned()
	{
		return columnMetaData.isSigned();
	}

	/**
	 * Sets if this <code>ColumnMetaData</code> is signed.
	 *
	 * @param pSigned true, if signed.
	 */
	public void setSigned(boolean pSigned)
	{
		columnMetaData.setSigned(pSigned);
	}
	
	/**
	 * Returns <code>true</code> if this <code>ColumnMetaData</code> is an auto increment column.
	 *
	 * @return <code>true</code> if this <code>ColumnMetaData</code> is an auto increment column.
	 */
	public boolean isAutoIncrement()
	{
		return columnMetaData.isAutoIncrement();
	}

	/**
	 * Sets if this <code>ColumnMetaData</code> is an auto increment column.
	 *
	 * @param pAutoIncrement 
	 *			the bAutoIncrement to set
	 */
	public void setAutoIncrement(boolean pAutoIncrement)
	{
		columnMetaData.setAutoIncrement(pAutoIncrement);
	}

	/**
	 * Returns the link reference for a server side dropdown list (automatic linked celleditor).
	 *
	 * @return the link reference for a server side dropdown list.
	 */
	public StorageReferenceDefinition getLinkReference()
	{
		return columnMetaData.getLinkReference();
	}

	/**
	 * Sets the link reference for a server side Dropdown list (automatic linked celleditor).
	 *
	 * @param pLinkReference the link reference to set
	 */
	public void setLinkReference(StorageReferenceDefinition pLinkReference)
	{
		columnMetaData.setLinkReference(pLinkReference);
	}	

	/**
	 * Sets the default value of this column.
	 * 
	 * @param pValue the default value
	 */
	public void setDefaultValue(Object pValue)
	{
		columnMetaData.setDefaultValue(pValue);
	}
	
	/**
	 * Gets the default value of this column.
	 * 
	 * @return the default value or <code>null</code> if the column has no default value
	 */
	public Object getDefaultValue()
	{
		return columnMetaData.getDefaultValue();
	}
	
	/**
	 * Sets the allowed values for this column.
	 * 
	 * @param pValues the allowed values or <code>null</code> when any value is allowed
	 */
	public void setAllowedValues(Object[] pValues)
	{
		columnMetaData.setAllowedValues(pValues);
	}
	
	/**
	 * Gets the allowed values for this column.
	 * 
	 * @return an {@link Object}[] with values, possible for the column.
	 */
	public Object[] getAllowedValues()
	{
		return columnMetaData.getAllowedValues();
	}		
	
	/**
	 * Returns the used name in the database (real column) for the query column.
	 *
	 * @return the name for the query column.
	 */
	public String getRealQueryColumnName()
	{
		return sRealQueryColumnName;
	}

	/**
	 * The name the use in the database (real column) to query the column. 
	 * e.g. w.name alias -&gt; sRealQueryColumnName=w.name, ColumnName = alias.
	 * It sets the real DB column name, which is specified from the developer 
	 * over the queryColumns to use for query, filter and sort (only Query side)
	 *
	 * @param pRealQueryColumnName the name the use in the database (real column) 
	 *                             to query the column.
	 */
	public void setRealQueryColumnName(String pRealQueryColumnName)
	{
		sRealQueryColumnName = pRealQueryColumnName;
	}
		
} 	// ServerColumnMetaData
