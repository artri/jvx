/*
 * Copyright 2014 SIB Visions GmbH
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
 * 27.03.2014 - [HM] - creation
 */
package javax.rad.ui.celleditor;

import javax.rad.model.ui.ICellRenderer;

/**
 * Platform and technology independent checkbox editor definition.<br>
 *     
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @param <C> Placeholder for the library dependent component type.
 */
public interface ICheckBoxCellEditor<C> extends IStyledCellEditor, ICellRenderer<C>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Display type constant for checkbox. */
    public static final String STYLE_CHECKBOX      = "";
    /** Display type constant for radio button. */
    public static final String STYLE_RADIOBUTTON   = "ui-radiobutton";
    /** Display type constant for switch. */
    public static final String STYLE_SWITCH        = "ui-switch";
    /** Display type constant for togglebutton. */
    public static final String STYLE_TOGGLEBUTTON  = "ui-togglebutton";
    /** Display type constant for button. */
    public static final String STYLE_BUTTON        = "ui-button";
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the value for selected state.
	 * 
	 * @return all allowed values.
	 */
	public Object getSelectedValue();

	/**
	 * Sets the value for selected state.
	 * 
	 * @param pSelectedValue the value for selected state.
	 */
	public void setSelectedValue(Object pSelectedValue);

	/**
	 * Gets the value for deselected state.
	 * 
	 * @return the value for deselected state.
	 */
	public Object getDeselectedValue();

	/**
	 * Sets the value for deselected state.
	 * 
	 * @param pDeselectedValue the value for deselected state.
	 */
	public void setDeselectedValue(Object pDeselectedValue);

	/**
	 * Gets the text of the checkbox button.
	 * If no text is set, the column label is used.
	 * 
	 * @return the text of the checkbox button.
	 */
	public String getText();

	/**
	 * Sets the text of the checkbox button.
	 * If no text is set, the column label is used.
	 * 
	 * @param pText the text of the checkbox button.
	 */
	public void setText(String pText);
	
}	// ICheckBoxCellEditor
