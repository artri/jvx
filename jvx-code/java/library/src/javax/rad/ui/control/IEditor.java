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
 * 04.11.2008 - [HM] - creation
 * 07.04.2009 - [JR] - setColumnName, setDataRow, setCellEditor now throws a ModelException
 */
package javax.rad.ui.control;

import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;

/**
 * Platform and technology independent Editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IEditor extends IComponent, 
                                 IEditorControl,
                                 IAlignmentConstants,
                                 ICellFormatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tells whether the CellEditor should save immediate.
	 * @return whether the CellEditor should save immediate.
	 */
    public boolean isSavingImmediate();

	/**
	 * Sets whether the CellEditor should save immediate.
	 * @param pSavingImmediate true, if the CellEditor should save immediate.
	 */
    public void setSavingImmediate(boolean pSavingImmediate);

    /**
     * Returns whether the border of the text field is visible.
     * 
     * @return <code>true</code> if the border is visible, <code>false</code> if the border is invisible
     */
    public boolean isBorderVisible();

	/**
     * Sets the border of the text field visible or invisible.
     * 
     * @param pVisible <code>true</code> to set the border visible or <code>false</code> to hide
     *                 the border
     */
    public void setBorderVisible(boolean pVisible);
    
}	// IEditor
