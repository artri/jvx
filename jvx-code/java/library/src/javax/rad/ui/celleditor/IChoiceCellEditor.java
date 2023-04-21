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
 * 05.11.2008 - [HM] - creation
 */
package javax.rad.ui.celleditor;

import javax.rad.model.ui.ICellRenderer;

/**
 * Platform and technology independent choice editor definition.<br>
 * A choice editor is an extended checkbox. More states and images can be defined.
 * This is necessary for database driven applications, as in the simplest case
 * a boolean column can have the states: true, false, null.<br>
 * But it can also be mapped on any character or number based column with any states .<br>
 * eg:<br>
 *    - "Y", "N", null<br>
 *    - "Created", "In progress", "Done", "Canceled"<br>
 *    - new BigDecimal(0), new BigDecimal(1)<br>
 *    ...<br>
 *    
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @param <C> Placeholder for the library dependent component type.
 */
public interface IChoiceCellEditor<C> extends IStyledCellEditor, ICellRenderer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets all allowed values.
	 * 
	 * @return all allowed values.
	 */
	public Object[] getAllowedValues();

	/**
	 * Sets all allowed values.
	 * 
	 * @param pAllowedValues all allowed values.
	 */
	public void setAllowedValues(Object[] pAllowedValues);

	/**
	 * Gets the image names that are used for displaying the corresponding values.
	 * 
	 * @return the image names.
	 */
	public String[] getImageNames();

	/**
	 * Sets the image names that are used for displaying the corresponding values.
	 * 
	 * @param pImageNames the image names.
	 */
	public void setImageNames(String[] pImageNames);

	/**
	 * Gets the default image that is shown if selectedIndex is -1.
	 * 
	 * @return the default image.
	 */
	public String getDefaultImageName();

	/**
	 * Sets the default image name that is shown if selectedIndex is -1.
	 * 
	 * @param pDefaultImageName the default image name.
	 */
	public void setDefaultImageName(String pDefaultImageName);
	
	
}	// IChoiceCellEditor
