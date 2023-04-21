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
 */
package javax.rad.model.event;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;

/**
 * The {@link IReadOnlyChecker} allows to define a specific value as readonly.
 * <p>
 * The "value" in this case refers to a position defined by row and column in
 * the {@link IDataBook} and could also be called a cell.
 * <p>
 * It is not possible to set a not editable value to editable with this, only an
 * editable value to readonly.
 * 
 * @see IDataBook
 * 
 * @author Martin Handsteiner
 */
public interface IReadOnlyChecker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Is called to determine whether the value at the location (row/column)
	 * should be readonly or not.
	 * 
	 * @param pDataBook the {@link IDataBook}.
	 * @param pDataPage the {@link IDataPage}.
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the name of the column.
	 * @param pRow the index of the row.
	 * @param pColumn the index of the column.
	 * @return {@code true} if the value should be readonly, {@code false}
	 *         otherwise. Note that this does only determine whether the value
	 *         should be readonly and does not have an impact on already
	 *         readonly values.
	 * @throws Throwable if an error occurs.
	 */
	public boolean isReadOnly(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable;
	
} 	// IReadOnlyChecker
