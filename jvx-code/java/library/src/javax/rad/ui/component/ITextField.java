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
 * 01.10.2008 - [HM] - creation
 * 18.03.2011 - [JR] - #314: border visibility support for TextField/Area
 */
package javax.rad.ui.component;

/**
 * Platform and technology independent text field definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.TextField
 * @see	javax.swing.JTextField
 */
public interface ITextField extends ILabel,
                                    IEditable,
                                    IPlaceholder
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the number of columns in this <code>ITextField</code>.
     *
     * @return the number of columns &gt;= 0
     */
    public int getColumns();

    /**
     * Sets the number of columns in this <code>ITextField</code>,
     * and then invalidate the layout.
     *
     * @param pColumns the number of columns &gt;= 0
     * @exception IllegalArgumentException if <code>columns</code>
     *		      is less than 0
     */
    public void setColumns(int pColumns);

    /**
     * Sets the border of the text field visible or invisible.
     * 
     * @param pVisible <code>true</code> to set the border visible or <code>false</code> to hide
     *                 the border
     */
    public void setBorderVisible(boolean pVisible);
    
    /**
     * Returns whether the the border of the text field is visible.
     * 
     * @return <code>true</code> if the border is visible, <code>false</code> if the border is invisible
     */
    public boolean isBorderVisible();

    /**
     * Selects the whole text, and sets the caret position to the end.
     */
    public void selectAll();

}	// ITextField
