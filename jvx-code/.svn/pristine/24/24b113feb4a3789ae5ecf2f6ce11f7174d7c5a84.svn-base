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
 * 23.11.2009 - [RH] - creation
 * 02.03.2010 - [RH] - indices functions moved to ServerMetaData class.
 * 16.02.2011 - [JR] - #287: clone implemented
 * 22.02.2013 - [JR] - #641: Feature support
 * 23.02.2013 - [JR] - equals and hashCode implemented
 */
package javax.rad.persist;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.rad.model.ModelException;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>MetaData</code> is a description of all columns as <code>ColumnMetaData</code>,
 * the Primary Key columns, Representation columns and auto increment columns. 
 *  
 * @see javax.rad.persist.ColumnMetaData
 * 
 * @author Roland Hörmann
 */
public class MetaData implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Supported features. */
	public enum Feature
	{
		/** Sort data. */
		Sort,
		/** Filter data. */
		Filter,
		/** write back data. */
		WriteBack,
	}
	
	/** The array of all <code>ColumnMetaData</code>. */
	private ArrayUtil<ColumnMetaData> auColumnMetaData = new ArrayUtil<ColumnMetaData>();

	/** Maps column names to indexes of the column definition. */
	private Map<String, Integer> hmColumnMetaDataMap = null;

	/** The primary column names. */
	private String[] saPrimaryKeyColumnNames;

	/** The Representation column names. */
	private String[] saRepresentationColumnNames;

	/** The auto increment column names. */
	private String[] saAutoIncrementColumnNames;

	/** The list of supported features. */
	private Feature[] faFeatures = new Feature[] {Feature.Sort, Feature.Filter, Feature.WriteBack};
	
	/** The column names. */
	private transient String[] 	saColumnNames;
	
	/** the visible column names. */
	private String[] saVisibleColumnNames;
	
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
		
		sbResult.append("ColumnMetaData :: ");
		sbResult.append(auColumnMetaData.toString());
		
		if (saPrimaryKeyColumnNames != null)
		{
			sbResult.append("PrimaryKeyColumnNames :: ");
			sbResult.append(StringUtil.toString(saPrimaryKeyColumnNames));
		}
		if (saRepresentationColumnNames != null)
		{
			sbResult.append("UniqueKeyColumnNames :: ");
			sbResult.append(StringUtil.toString(saRepresentationColumnNames));
		}
		if (saAutoIncrementColumnNames != null)
		{
			sbResult.append("AutoIncrementKeyColumnNames :: ");
			sbResult.append(StringUtil.toString(saAutoIncrementColumnNames));
		}
		if (saVisibleColumnNames != null)
		{
			sbResult.append("VisibleColumnNames :: ");
			sbResult.append(StringUtil.toString(saVisibleColumnNames));
		}		
		return sbResult.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int iPrime = 31;
		
		int iResult = iPrime + ((auColumnMetaData == null) ? 0 : auColumnMetaData.hashCode());
		iResult = iPrime * iResult + Arrays.hashCode(faFeatures);
		iResult = iPrime * iResult + Arrays.hashCode(saAutoIncrementColumnNames);
		iResult = iPrime * iResult + Arrays.hashCode(saPrimaryKeyColumnNames);
		iResult = iPrime * iResult + Arrays.hashCode(saRepresentationColumnNames);
		iResult = iPrime * iResult + Arrays.hashCode(saVisibleColumnNames);
		
		return iResult;
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
		
		MetaData mdCompare = (MetaData)pObject;
		
		if (auColumnMetaData == null)
		{
			if (mdCompare.auColumnMetaData != null)
			{
				return false;
			}
		}
		else if (!auColumnMetaData.equals(mdCompare.auColumnMetaData))
		{
			return false;
		}
		
		if (!Arrays.equals(faFeatures, mdCompare.faFeatures))
		{
			return false;
		}
		
		if (!Arrays.equals(saAutoIncrementColumnNames, mdCompare.saAutoIncrementColumnNames))
		{
			return false;
		}
		
		if (!Arrays.equals(saPrimaryKeyColumnNames, mdCompare.saPrimaryKeyColumnNames))
		{
			return false;
		}
		
		if (!Arrays.equals(saRepresentationColumnNames, mdCompare.saRepresentationColumnNames))
		{
			return false;
		}
		
		if (!Arrays.equals(saVisibleColumnNames, mdCompare.saVisibleColumnNames))
		{
			return false;
		}
		
		return true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds an new ColumnMetaData column.
	 * 
	 * @param pColumnMetaData	the column meta data to add
	 */
	public void addColumnMetaData(ColumnMetaData pColumnMetaData)
	{
		auColumnMetaData.add(pColumnMetaData);

		saColumnNames = null;
		hmColumnMetaDataMap = null;
	}
	
	/**
	 * returns all ColumnMetaData columns.
	 * 
	 * @return all ColumnMetaData columns.
	 */
	public ColumnMetaData[] getColumnMetaData()
	{
		return auColumnMetaData.toArray(new ColumnMetaData[auColumnMetaData.size()]);
	}

	/**
	 * Sets the column meta data.
	 * It is ensured, that no existing column meta data is cleared, or the index is changed.
	 * 
	 * @param pColumnMetaData all ColumnMetaData columns to add.
	 */
	public void setColumnMetaData(ColumnMetaData[] pColumnMetaData)
	{
		if (pColumnMetaData != null && pColumnMetaData.length > 0)
		{
			for (int i = 0; i < pColumnMetaData.length; i++)
			{
				ColumnMetaData cmd = pColumnMetaData[i];

				int index = getColumnMetaDataIndex(cmd.getName());
				if (index < 0)
				{
					auColumnMetaData.add(cmd);
				}
				else
				{
					auColumnMetaData.set(index, cmd);
				}
			}
			
			saColumnNames = null;
			hmColumnMetaDataMap = null;
		}
	}

	/**
	 * Returns the specific ColumnMetaData column.
	 * 
	 * @param pColumnName	the column name to use.
	 * @return the specific ColumnMetaData column.
	 * @throws ModelException	if the column name doesn't exists
	 */
	public ColumnMetaData getColumnMetaData(String pColumnName) throws ModelException
	{
		int i = getColumnMetaDataIndex(pColumnName);
		
		if (i >= 0)
		{
			return auColumnMetaData.get(i);
		}
		throw new ModelException("Column '" + pColumnName + "' doesn't exists in MetaData!");				
	}

	/**
	 * Returns the specific ColumnMetaData column.
	 * 
	 * @param pIndex	the column index to use.
	 * @return the specific ColumnMetaData column.
	 */
	public ColumnMetaData getColumnMetaData(int pIndex)
	{
		return auColumnMetaData.get(pIndex);
	}

	/**
	 * Returns the ColumnMetaData count.
	 * 
	 * @return the ColumnMetaData count.
	 */
	public int getColumnMetaDataCount()
	{
		return auColumnMetaData.size();
	}

	/**
	 * Returns the specific ColumnMetaData column index. -1 if the Column name doesn't exist.
	 * 
	 * @param pColumnName	the column name to use.
	 * @return the specific ColumnMetaData column index.
	 */
	public int getColumnMetaDataIndex(String pColumnName)
	{
		if (hmColumnMetaDataMap == null)
		{
			hmColumnMetaDataMap = new HashMap<String, Integer>();
			
			for (int i = 0, size = auColumnMetaData.size(); i < size; i++)
			{
				hmColumnMetaDataMap.put(auColumnMetaData.get(i).getName(), Integer.valueOf(i));
			}
		}
		
		Integer index = hmColumnMetaDataMap.get(pColumnName);
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
	 * Returns the Primary Key column names.
	 *
	 * @return the Primary Key column names.
	 */
	public String[] getPrimaryKeyColumnNames()
	{
		return saPrimaryKeyColumnNames;
	}
	
	/**
	 * Sets the Primary Key column names.
	 *
	 * @param pPrimaryKeyColumnNames 
	 *			the Primary Key column names to set
	 */
	public void setPrimaryKeyColumnNames(String[] pPrimaryKeyColumnNames)
	{
		saPrimaryKeyColumnNames = pPrimaryKeyColumnNames;
	}

	/**
	 * Returns the Representation column names. Thats all Unique Key columns as default behavior.
	 *
	 * @return the Representation column names.
	 */
	public String[] getRepresentationColumnNames()
	{
		return saRepresentationColumnNames;
	}

	/**
	 * Sets the Representation column names. 
	 *
	 * @param pRepresentationColumnNames 
	 *			the Representation column names to set
	 */
	public void setRepresentationColumnNames(String[] pRepresentationColumnNames)
	{
		saRepresentationColumnNames = pRepresentationColumnNames;
	}
	
    /**
	 * Returns the visible column names.
	 *
	 * @return the visible column names.
	 */
	public String[] getVisibleColumnNames()
	{
		return saVisibleColumnNames;
	}
	
	/**
	 * Sets the visible column names.
	 *
	 * @param pVisibleColumnNames 
	 *			the visible column names to set
	 */
	public void setVisibleColumnNames(String[] pVisibleColumnNames)
	{
		saVisibleColumnNames = pVisibleColumnNames;
	}	
	
	/**
	 * Returns the auto increment column names.
	 *
	 * @return the auto increment column names.
	 */
	public String[] getAutoIncrementColumnNames()
	{
		return saAutoIncrementColumnNames;
	}

	/**
	 * Sets the auto increment column names.
	 *
	 * @param pAutoIncrementColumnNames 
	 *			the auto increment column names. to set
	 */
	public void setAutoIncrementColumnNames(String[] pAutoIncrementColumnNames)
	{
		saAutoIncrementColumnNames = pAutoIncrementColumnNames;
	}

	/**
	 * Sets supported features.
	 * 
	 * @param pFeatures the supported features
	 */
	public void setFeatures(Feature... pFeatures)
	{
		if (pFeatures == null)
		{
			faFeatures = new Feature[0];
		}
		else
		{
			faFeatures = pFeatures;
		}
	}
	
	/**
	 * Adds support for a feature.
	 * 
	 * @param pFeature the feature
	 */
	public void addFeature(Feature pFeature)
	{
		if (!ArrayUtil.contains(faFeatures, pFeature))
		{
			faFeatures = ArrayUtil.add(faFeatures, pFeature);
		}
	}
	
	/**
	 * Removes support for a feature.
	 * 
	 * @param pFeature the feature
	 */
	public void removeFeature(Feature pFeature)
	{
		faFeatures = ArrayUtil.remove(faFeatures, pFeature);
	}

	/**
	 * Gets supported features.
	 * 
	 * @return the supported features
	 */
	public Feature[] getFeatures()
	{
		return faFeatures;
	}
	
	/**
	 * Gets whether a specific feature is supported.
	 * 
	 * @param pFeature the feature
	 * @return <code>true</code> if the feature is supported, <code>false</code> otherwise
	 */
	public boolean isSupported(Feature pFeature)
	{
		return ArrayUtil.contains(faFeatures, pFeature);
	}
	
	/**
	 * Returns all column names of the ColumnMetaData list.
	 * <p>
	 * Please note that the returned array is a cached instance, there for
	 * changing it will result in changing the cached version, which will result
	 * in undefined behavior. If you want to modify the array, copy or clone it
	 * before modifying it.
	 * 
	 * @return all column names of the ColumnMetaData list.
	 */
	public String[] getColumnNames()
	{
		if (saColumnNames == null)
		{
			saColumnNames = new String[auColumnMetaData.size()]; 
			
			for (int i = 0; i < saColumnNames.length; i++)
			{
				saColumnNames[i] = auColumnMetaData.get(i).getName();
			}
		}
		return saColumnNames;
	}

} 	// MetaData
