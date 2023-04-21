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
 * 20.04.2009 - [HM] - creation
 */
package javax.rad.ui.control;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;

/**
 * Platform and technology independent CellFormatter definition. It is designed
 * for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface ICellFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link ICellFormat} for the specified cell.
	 * <p>
	 * A note for implementations, the created {@link ICellFormat} can and
	 * should be cached they do not change.
	 * 
	 * @param pDataBook the {@link IDataBook}.
	 * @param pDataPage the {@link IDataPage}.
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the name of the column.
	 * @param pRow the index of the displayed row. This is not necessarily the
	 *            same as the model index. If you want to get the value, use
	 *            {@code pDataRow}.
	 * @param pColumn the index of the displayed column. This is not necessarily
	 *            the same as the model index. If you want to get the value, use
	 *            {@code pColumnName} and {@code pDataRow}.
	 * @return the {@link ICellFormat} for the specified cell.
	 * @throws Throwable if an error occurs.
	 */
	public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable;
	
}	// ICellFormatter
