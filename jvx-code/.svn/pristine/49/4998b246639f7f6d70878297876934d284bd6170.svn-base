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
 * 03.07.2009 - [RH] - creation
 * 21.10.2009 - [RH] - isChangedColumnName [BUGFIXED]
 */
package javax.rad.model.event;

import javax.rad.model.IDataRow;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>DataRowEvent</code> gives information about changes in the 
 * {@link IDataRow}.
 * 
 * @see javax.rad.model.IDataRow
 * 
 * @author Martin Handsteiner
 */
public class DataRowEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The changed IDataRow. */
	private IDataRow changedDataRow;
	
	/** The changed column names. */
	private String[] changedColumnNames;
	
	/** The original IDataRow. */
	private IDataRow originalDataRow;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DataRowEvent</code>.
	 * 
	 * @param pChangedDataRow the changed IDataRow.
	 * @param pChangedColumnNames the changed column names.
	 * @param pOriginalDataRow the old IDataRow.
	 */
	public DataRowEvent(IDataRow pChangedDataRow, String[] pChangedColumnNames, IDataRow pOriginalDataRow)
	{
		changedDataRow = pChangedDataRow;
		changedColumnNames = pChangedColumnNames;
		originalDataRow = pOriginalDataRow;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the IDataRow that is changed.
	 * 
	 * @return the IDataRow that is changed.
	 */
	public IDataRow getChangedDataRow()
	{
		return changedDataRow;
	}
	
	/**
	 * Gets the column names whose values changed.
	 * 
	 * @return the column names whose values changed.
	 */
	public String[] getChangedColumnNames()
	{
		return changedColumnNames;
	}
	
	/**
	 * Gets the original IDataRow before change.
	 * 
	 * @return the original IDataRow before change.
	 */
	public IDataRow getOriginalDataRow()
	{
		return originalDataRow;
	}

	/**
	 * Gets true, if the given column name is in the list of changed column names.
	 * @param pColumnName the column name
	 * @return true, if the given column name is in the list of changed column names.
	 */
	public boolean isChangedColumnName(String pColumnName)
	{
		return ArrayUtil.indexOf(changedColumnNames, pColumnName) >= 0;
	}
	
}	// DataRowEvent
