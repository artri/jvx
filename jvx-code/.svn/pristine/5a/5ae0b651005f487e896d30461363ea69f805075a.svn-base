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

import javax.rad.model.IDataBook;

/**
 * The <code>ITreeControl</code> is an IControl that displays the hierarchy of one or more IDataBooks.
 * 
 * @author Martin Handsteiner
 */
public interface ITreeControl extends IEditableControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the DataBooks displayed by this control.
     *
     * @return the DataBooks.
     * @see #setDataBooks
     */
    public IDataBook[] getDataBooks();

    /**
     * Sets the DataBooks displayed by this control.
     * 
	 * @param pDataBooks the DataBooks
     * @see #getDataBooks
     */
    public void setDataBooks(IDataBook... pDataBooks);
	
    /**
	 * Gets the active data book.
	 * @return the active data book.
	 */
	public IDataBook getActiveDataBook();

} 	// ITreeControl
