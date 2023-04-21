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
 * 27.08.2009 - [HM] - creation
 */
package javax.rad.ui.control;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.ui.IImage;

/**
 * Platform and technology independent NodeFormatter definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface INodeFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the image for the given node.
	 * 
	 * @param pDataBook the IDataBook.
	 * @param pDataPage the IDataPage.
	 * @param pDataRow the IDataRow.
	 * @param pColumnName the column name.
	 * @param pRow the row index.
	 * @param pExpanded true if expanded.
	 * @param pLeaf true if leaf.
	 * @return the ICellFormat for the specified cell.
	 */
	public IImage getNodeImage(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, boolean pExpanded, boolean pLeaf);

}	// INodeFormatter
