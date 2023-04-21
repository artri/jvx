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
 * 11.03.2011 - [RH] - #308 - DB specific automatic quoting implemented
 * 19.08.2011 - [JR] - #458: clone(): Name[] cloned     
 * 11.07.2013 - [JR] - #727: PrimaryKeyType introduced
 */
package com.sibvisions.rad.persist.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.rad.model.ModelException;
import javax.rad.persist.MetaData;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.BeanUtil;

/**
 * The <code>ServerMetaData</code> is a description of all columns as <code>ServerColumnMetaData</code>,
 * the Primary Key columns, Representation columns and auto increment columns. 
 * It also includes the server relevant infos, in addition to the <code>MetaData</code> just for the client.
 *  
 * @see com.sibvisions.rad.persist.jdbc.ServerColumnMetaData
 * 
 * @author Roland Hörmann
 */
public class ServerMetaData
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the possible types for the pk columns. */
	public enum PrimaryKeyType
	{
		/** primary key is used. */
		PrimaryKeyColumns,
		/** unique key is used. */
		UniqueKeyColumns,
		/** all columns are used. */
		AllColumns
	};

	/** The MetaData for the client. */
	private MetaData metaData;

	/** The array of all <code>ServerColumnMetaData</code>. */
	private ArrayUtil<ServerColumnMetaData>	auServerColumnMetaData = new ArrayUtil<ServerColumnMetaData>();

	/** Maps column names to indexes of the column definition. */
	private Map<String, Integer> hmServerColumnMetaDataMap = null;

	/** the primary key type. */
	private PrimaryKeyType pktype = PrimaryKeyType.PrimaryKeyColumns;

	/** The writable column names. */
	private transient String[] 	saWritableColumnNames;
	
	/** Caches the writable columns indices for fast access. */
	private transient int[]		iaWritableColumnIndices;
	
	/** Caches the PK column indices for fast access. */
	private transient int[] 	iaPrimaryKeyColumnIndices;

	/** Caches the auto increment column indices for fast access. */
	private transient int[] 	iaAutoIncrementColumnIndices;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ServerMetaData</code> with new {@link MetaData}.
	 */
	public ServerMetaData()
	{
		setMetaData(null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the MetaData client infos.
	 * 
	 * @return the MetaData client infos.
	 */
	public MetaData getMetaData()
	{
		return metaData;
	}	
	
	/**
	 * Sets all ServerMetaData columns.
	 * 
	 * @param pMetaData	the meta data.
	 */
	public void setMetaData(MetaData pMetaData)
	{
		if (pMetaData == null)
		{
			metaData = new MetaData();
		}
		else
		{
			metaData = pMetaData;
		}
		
		auServerColumnMetaData.clear();

		iaWritableColumnIndices = null;
		iaPrimaryKeyColumnIndices = null;
		iaAutoIncrementColumnIndices = null;
		saWritableColumnNames = null;
		hmServerColumnMetaDataMap = null;
	}

	/**
	 * Adds an new ServerColumnMetaData column.
	 * 
	 * @param pServerColumnMetaData	the column meta data to add
	 */
	public void addServerColumnMetaData(ServerColumnMetaData pServerColumnMetaData)
	{
		int index = metaData.getColumnMetaDataIndex(pServerColumnMetaData.getName());
		if (index < 0)
		{
			if (auServerColumnMetaData.size() < metaData.getColumnMetaDataCount())
			{
				auServerColumnMetaData.setSize(metaData.getColumnMetaDataCount());
			}
			
			metaData.addColumnMetaData(pServerColumnMetaData.getColumnMetaData());
			auServerColumnMetaData.add(pServerColumnMetaData);
		}
		else
		{
			if (index >= auServerColumnMetaData.size())
			{
				auServerColumnMetaData.setSize(index + 1);
			}
			
			pServerColumnMetaData.setColumnMetaData(metaData.getColumnMetaData(index));
			
			auServerColumnMetaData.set(index, pServerColumnMetaData);
		}

		iaWritableColumnIndices = null;
		iaPrimaryKeyColumnIndices = null;
		iaAutoIncrementColumnIndices = null;
		saWritableColumnNames = null;
		hmServerColumnMetaDataMap = null;
	}
	
	/**
	 * Adds an new ServerColumnMetaData column.
	 * 
	 * @param pServerColumnMetaData	the column meta data to add
	 */
	public void setServerColumnMetaData(ServerColumnMetaData[] pServerColumnMetaData)
	{
		for (ServerColumnMetaData scmd : pServerColumnMetaData)
		{
			addServerColumnMetaData(scmd);
		}
	}
	
	/**
	 * Returns all ServerColumnMetaData columns.
	 * 
	 * @return all ServerColumnMetaData columns.
	 */
	public ServerColumnMetaData[] getServerColumnMetaData()
	{
		if (auServerColumnMetaData.size() < metaData.getColumnMetaDataCount())
		{
			auServerColumnMetaData.setSize(metaData.getColumnMetaDataCount());
		}
		return auServerColumnMetaData.toArray(new ServerColumnMetaData[auServerColumnMetaData.size()]);
	}

	/**
	 * Returns the specific ServerColumnMetaData column.
	 * 
	 * @param pServerColumnName		the column name to use.
	 * @return the specific ServerColumnMetaData column.
	 * @throws ModelException	if the column name doesn't exist
	 */
	public ServerColumnMetaData getServerColumnMetaData(String pServerColumnName) throws ModelException
	{
		int i = getServerColumnMetaDataIndex(pServerColumnName);
		
		if (i >= 0)
		{
			return auServerColumnMetaData.get(i);
		}
		throw new ModelException("Column '" + pServerColumnName + "' doesn't exist in MetaData!");
	}

	/**
	 * Returns the specific ServerColumnMetaData column index. -1 if the Column name doesn't exist.
	 * 
	 * @param pIndex	the column index to use.
	 * @return the specific ServerColumnMetaData column index.
	 */
	public ServerColumnMetaData getServerColumnMetaData(int pIndex)
	{
		if (auServerColumnMetaData.size() < metaData.getColumnMetaDataCount())
		{
			auServerColumnMetaData.setSize(metaData.getColumnMetaDataCount());
		}
		return auServerColumnMetaData.get(pIndex);
	}
	
	/**
	 * Returns the specific ServerColumnMetaData column index. -1 if the Column name doesn't exist.
	 * 
	 * @param pServerColumnName	the column name to use.
	 * @return the specific ServerColumnMetaData column index.
	 */
	public int getServerColumnMetaDataIndex(String pServerColumnName)
	{
		if (hmServerColumnMetaDataMap == null)
		{
			hmServerColumnMetaDataMap = new HashMap<String, Integer>();
			
			for (int i = 0, size = auServerColumnMetaData.size(); i < size; i++)
			{
				ServerColumnMetaData scmd = auServerColumnMetaData.get(i);
				if (scmd != null)
				{
					hmServerColumnMetaDataMap.put(scmd.getName(), Integer.valueOf(i));
				}	
			}
		}
		
		Integer index = hmServerColumnMetaDataMap.get(pServerColumnName);
		if (index == null)
		{
			return -1;
		}
		else
		{
			return index.intValue();
		}
	}
	
	/**
	 * Returns the specific ServerColumnMetaData column index. -1 if the Column name doesn't exist.
	 * 
	 * @param pServerColumnMetaData	the server column meta data.
	 * @param pServerColumnName	the column name to use.
	 * @return the specific ServerColumnMetaData column index.
	 */
	public static int getServerColumnMetaDataIndex(ServerColumnMetaData[] pServerColumnMetaData, String pServerColumnName)
	{
		for (int i = 0; i < pServerColumnMetaData.length; i++)
		{
			ServerColumnMetaData scmd = pServerColumnMetaData[i];
			if (scmd != null)
			{
				String colName = scmd.getName();
				if (colName == pServerColumnName || colName.equals(pServerColumnName))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Returns all writable column name indices.
	 *
	 * @return all writable column name indices.
	 */
	public int[] getWritableColumnIndices()
	{
		if (iaWritableColumnIndices == null)
		{
			// Oracle has a bug in several database versions, that does not allow to
			// insert a varchar column after a lob column, in case 32UTF8 encoding and
			// the varchar is > 1000 characters. So, it doesn't hurt, to enforce
			// statement creation with lob columns at the end.
			int[] iaIndicesNoLob = new int[auServerColumnMetaData.size()]; 
			int[] iaIndicesLob = new int[auServerColumnMetaData.size()]; 
			int iNL = 0;
			int iL = 0;
			for (int j = 0, size = auServerColumnMetaData.size(); j < size; j++)
			{
				ServerColumnMetaData scmd = auServerColumnMetaData.get(j);
				if (scmd != null && scmd.isWritable())
				{
					if (scmd.getPrecision() > 4000)
					{
						iaIndicesLob[iL] = j;
						iL++;
					}
					else
					{
						iaIndicesNoLob[iNL] = j;
						iNL++;
					}
				}
			}
			iaWritableColumnIndices = new int[iL + iNL]; 
			System.arraycopy(iaIndicesNoLob, 0, iaWritableColumnIndices, 0, iNL);
			System.arraycopy(iaIndicesLob, 0, iaWritableColumnIndices, iNL, iL);
		}
		return iaWritableColumnIndices;
	}
	
	/**
	 * Returns all writable column names.
	 *
	 * @return all writable column names.
	 */
	public String[] getWritableColumnNames()
	{
		if (saWritableColumnNames == null)
		{
			int[] indices = getWritableColumnIndices();
			
			saWritableColumnNames = new String[indices.length];
			
			for (int i = 0; i < indices.length; i++)
			{
		        saWritableColumnNames[i] = auServerColumnMetaData.get(indices[i]).getName();
			}
		}
		
		return saWritableColumnNames;
	}
	
	/**
	 * Returns the Primary Key column name indices.
	 *
	 * @return the Primary Key column name indices.
	 */
	public int[] getPrimaryKeyColumnIndices()
	{
		String[] saPrimaryKeyColumnNames = metaData.getPrimaryKeyColumnNames();
		
		if (saPrimaryKeyColumnNames != null && iaPrimaryKeyColumnIndices == null)
		{
			iaPrimaryKeyColumnIndices = new int[saPrimaryKeyColumnNames.length]; 
			
			for (int i = 0; i < saPrimaryKeyColumnNames.length; i++)
			{
				iaPrimaryKeyColumnIndices[i] = getServerColumnMetaDataIndex(saPrimaryKeyColumnNames[i]);
			}
		}
		return iaPrimaryKeyColumnIndices;
	}
	
    /**
	 * Returns the Primary Key column names.
	 *
	 * @return the Primary Key column names.
	 */
	public String[] getPrimaryKeyColumnNames()
	{
		return metaData.getPrimaryKeyColumnNames();
	}
	
	/**
	 * Sets the Primary Key column names.
	 *
	 * @param pPrimaryKeyColumnNames 
	 *			the Primary Key column names to set
	 */
	public void setPrimaryKeyColumnNames(String[] pPrimaryKeyColumnNames)
	{
		metaData.setPrimaryKeyColumnNames(pPrimaryKeyColumnNames);
		iaPrimaryKeyColumnIndices = null;
	}

	/**
	 * Sets the Primary Key column names.
	 *
	 * @param pPrimaryKeyColumnNames 
	 *			the Primary Key column names to set
	 */
	public void setPrimaryKeyColumnNames(Name[] pPrimaryKeyColumnNames)
	{
		metaData.setPrimaryKeyColumnNames(BeanUtil.toArray(pPrimaryKeyColumnNames, new String [pPrimaryKeyColumnNames.length], "name"));
		iaPrimaryKeyColumnIndices = null;
	}

	/**
	 * Returns the Representation column names. Thats all Unique Key columns as default behavior.
	 *
	 * @return the Representation column names.
	 */
	public String[] getRepresentationColumnNames()
	{
		return metaData.getRepresentationColumnNames();
	}

	/**
	 * Sets the Representation column names. 
	 *
	 * @param pRepresentationColumnNames 
	 *			the Representation column names to set
	 */
	public void setRepresentationColumnNames(String[] pRepresentationColumnNames)
	{
		metaData.setRepresentationColumnNames(pRepresentationColumnNames);
	}
	
	/**
	 * Sets the Representation column names. 
	 *
	 * @param pRepresentationColumnNames 
	 *			the Representation column names to set
	 */
	public void setRepresentationColumnNames(Name[] pRepresentationColumnNames)
	{
		metaData.setRepresentationColumnNames(BeanUtil.toArray(pRepresentationColumnNames, new String [pRepresentationColumnNames.length], "name"));
	}
	
	/**
	 * Returns the auto increment column names.
	 *
	 * @return the auto increment column names.
	 */
	public String[] getAutoIncrementColumnNames()
	{
		return metaData.getAutoIncrementColumnNames();
	}

	/**
	 * Returns the auto increment column name indices.
	 *
	 * @return the auto increment column name indices.
	 */
	public int[] getAutoIncrementColumnIndices()
	{
		String[] saAutoIncrementColumnNames = metaData.getAutoIncrementColumnNames();
		
		if (saAutoIncrementColumnNames != null && iaAutoIncrementColumnIndices == null)
		{
			iaAutoIncrementColumnIndices = new int[saAutoIncrementColumnNames.length]; 
			
			for (int i = 0; i < saAutoIncrementColumnNames.length; i++)
			{
				iaAutoIncrementColumnIndices[i] = getServerColumnMetaDataIndex(saAutoIncrementColumnNames[i]);
			}
		}
		return iaAutoIncrementColumnIndices;
	}
	
	/**
	 * Sets the auto increment column names.
	 *
	 * @param pAutoIncrementColumnNames 
	 *			the auto increment column names. to set
	 */
	public void setAutoIncrementColumnNames(String[] pAutoIncrementColumnNames)
	{
		metaData.setAutoIncrementColumnNames(pAutoIncrementColumnNames);
		iaAutoIncrementColumnIndices = null;
	}

	/**
	 * Sets the auto increment column names.
	 *
	 * @param pAutoIncrementColumnNames 
	 *			the auto increment column names. to set
	 */
	public void setAutoIncrementColumnNames(Name[] pAutoIncrementColumnNames)
	{
		metaData.setAutoIncrementColumnNames(BeanUtil.toArray(pAutoIncrementColumnNames, new String [pAutoIncrementColumnNames.length], "name"));
		iaAutoIncrementColumnIndices = null;
	}

	/**
	 * Returns all column names of the ColumnMetaData list.
	 * 
	 * @return all column names of the ColumnMetaData list.
	 */
	public String[] getColumnNames()
	{
		return metaData.getColumnNames();
	}

	/**
	 * Gets the type of the Primary key columns.
	 * 
	 * @return the pk type
	 * @see PrimaryKeyType
	 */
	public PrimaryKeyType getPrimaryKeyType()
	{
		return pktype;
	}
	
	/**
	 * Gets the type of the Primary key columns.
	 * 
	 * @param pType the type
	 */
	public void setPrimaryKeyType(PrimaryKeyType pType)
	{
		pktype = pType;
	}

} 	// ServerMetaData
