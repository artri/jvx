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
 */
package javax.rad.ui.celleditor;

import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.Style;

/**
 * Platform and technology independent styled cell editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IStyledCellEditor extends ICellEditor, 
                                           IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the style definition. A style can be used to customize a component.
	 * 
	 * @param pStyle the style
	 */
    public void setStyle(Style pStyle);
    
    /**
     * Gets the style definition.
     * 
     * @return the style definition
     * @see #setStyle(Style)
     */
    public Style getStyle();

}	// IStyledCellEditor
