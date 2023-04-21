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
 * 19.01.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext.format;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;

/**
 * The <code>ICellFormatter</code> allows cell oriented formatting of the displayed	
 * information.
 *  
 * @author Martin Handsteiner
 */
public interface ICellFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the format for the given cell.
	 * 
	 * @param pDataBook the DataBook
	 * @param pDataPage the DataPage
	 * @param pDataRow  the DataRow
	 * @param pColumnName the column name
	 * @param pRow      the Row number
	 * @param pColumn   the Column number
	 * @return the format for the given cell.
	 * @throws Throwable if an exception, it is ignored.
	 */
	public CellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable;
	
}	// ICellFormatter
