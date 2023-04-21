/*
 * Copyright 2019 SIB Visions GmbH
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
 * 03.04.2019 - [DJ] - creation
 */
package javax.rad.ui.control;

/**
 * Platform and technology independent definition of component, which supports cell tooltip functionality. It is
 * designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Jozef Dorko
 */
public interface ICellToolTipable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link ICellToolTip}.
	 * <p>
	 * The {@link ICellToolTip} will be called for every cell and can return a
	 * tooltip for that cell.
	 * 
	 * @return the {@link ICellToolTip}.
	 * @see #setCellToolTip(ICellToolTip)
	 */
	public ICellToolTip getCellToolTip();
	
	/**
	 * Sets the {@link ICellToolTip}.
	 * <p>
	 * The {@link ICellToolTip} will be called for every cell and can set a
	 * tooltip for that cell.
	 *
	 * @param pCellTooltip the {@link ICellToolTip}.
	 * @see #getCellToolTip()
	 */
	public void setCellToolTip(ICellToolTip pCellTooltip);
	
}   // ICellToolTipable
