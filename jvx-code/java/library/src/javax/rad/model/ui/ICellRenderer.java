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
package javax.rad.model.ui;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;

/**
 * This class renders a cell of a GUI control. <br>
 * It is used as part of the rendering process in a GUI control and renders the
 * data depending part.
 * 
 * @param <C> Placeholder for the library dependent component type.
 * 
 * @author Roland Hörmann, Martin Handsteiner
 */
public interface ICellRenderer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Configures a IComponent for rendering. The IComponent can be reused for performance reasons.
	 * After a new call of getCellRendererComponent, the previous IComponent can't be used for rendering
	 * anymore.
	 * 
	 * @param pParentComponent  the Parent Component from which.
	 * @param pDataPage 		the DataPage of the row to be rendered.
	 * @param pRowNumber 		the row number of the row to be rendered.
	 * @param pDataRow   		the row to be rendered (is the same as pDataPage.getRow(pRowNumber)).
	 * @param pColumnName		the column name to be rendered.
	 * @param pIsSelected 		true, if the cell is selected (shown with selected background).
	 * @param pHasFocus			true, if the cell has the focus (cell is the current editing cell).
	 * @return the renderer Component. It can only be used until the next call of this function.
	 */
	public C getCellRendererComponent(C pParentComponent,
			                          IDataPage pDataPage,
			                          int       pRowNumber,
			                          IDataRow  pDataRow,
			                          String    pColumnName,
			                          boolean   pIsSelected,
			                          boolean   pHasFocus);
	
} 	// ICellRenderer
