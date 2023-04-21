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
 * 31.03.2011 - [JR] - #161: extended from ITranslatable
 */
package javax.rad.model.ui;

import javax.rad.model.ModelException;
import javax.rad.util.ITranslator;

/**
 * The <code>IControl</code> inform about changes in the 
 * <code>IDataRow/IDataBook</code> and GUI controls need to repaint the data part.
 * It also inform the GUI controls, that the editing mode in the control should be 
 * save or cancel the last change.
 * 
 * @see javax.rad.model.IDataRow
 * @see javax.rad.model.IDataBook
 * @author Roland Hörmann
 */
public interface IControl extends ITranslatable, 
								  ITranslator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * The control need to check if the part is visible and then repaint the
	 * part.
	 * Notify repaint is called on every change. The IControl has to catch the change,  
	 * and return immediate.
	 * The change analysis and display has to be done with invoke later by the control.
	 */
	public void notifyRepaint();

	/**
	 * Informs the GUI control, that the last edit should be set into the 
	 * <code>IDataBook</code> or <code>IDataRow</code>.
	 * @throws ModelException if the value can not be stored.
	 */
	public void saveEditing() throws ModelException;

	/**
	 * Informs the GUI control, that the last edit should be canceled(restored)
	 * the correct value is in the <code>DataBook</code>.
	 */
	public void cancelEditing();

} 	// IControl
