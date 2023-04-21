/*
 * Copyright 2013 SIB Visions GmbH
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
 * 27.09.2013 - [HM] - creation
 */
package javax.rad.ui.celleditor;


/**
 * Platform and technology independent in place cell editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IInplaceCellEditor extends IStyledCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Open Editor on double click. This should be the default behaviour. */
	public static final int	DOUBLE_CLICK	= 0;

	/** Open Editor with single click. */
	public static final int	SINGLE_CLICK	= 1;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the preferred Editor Mode.
	 * 
	 * Different Platforms are open to define own editor modes.
	 * As this is meant to be extended, own modes should have constants &gt;= 100, to avoid unwanted interactions. 
	 * If a platform does not support a editor mode, it should use the default.
	 * 
	 * @return the preferred Editor Mode.
	 * @see #DOUBLE_CLICK
	 * @see #SINGLE_CLICK
	 */
	public int getPreferredEditorMode();

	/**
	 * Sets the preferred Editor Mode.
	 * 
	 * Different Platforms are open to define own editor modes.
	 * As this is meant to be extended, own modes should have constants &gt;= 100, to avoid unwanted interactions. 
	 * If a platform does not support a editor mode, it should use the default.
	 * 
	 * @param pPreferredEditorMode the preferred Editor Mode.
	 * @see #DOUBLE_CLICK
	 * @see #SINGLE_CLICK
	 */
	public void setPreferredEditorMode(int pPreferredEditorMode);

}	// IInplaceCellEditor
