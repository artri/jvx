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
 */
package javax.rad.ui.component;

/**
 * Platform and technology independent text area definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	ITextField
 */
public interface ITextArea extends ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the number of rows in this <code>ITextArea</code>.
     *
     * @return the number of columns &gt;= 0
     */
    public int getRows();

    /**
     * Sets the number of rows in this <code>ITextArea</code>,
     * and then invalidate the layout.
     *
     * @param pRows the number of rows &gt;= 0
     * @exception IllegalArgumentException if <code>rows</code>
     *		      is less than 0
     */
    public void setRows(int pRows);

	/**
	 * Gets the current state of the word wrap mode.
	 * 
	 * @return the current state of the word wrap mode.
	 */
	public boolean isWordWrap();

	/**
	 * Sets the current state of the word wrap mode.
	 * 
	 * @param pWordWrap the current state of the word wrap mode.
	 */
	public void setWordWrap(boolean pWordWrap);


}	// ITextArea
