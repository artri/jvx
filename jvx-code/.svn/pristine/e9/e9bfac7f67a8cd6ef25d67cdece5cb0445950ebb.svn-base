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
 * 19.10.2008 - [JR] - creation
 * 15.09.2014 - [RZ] - get/setActionCommand() is no longer supposed to affect the text
 */
package javax.rad.ui.component;

import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;

/**
 * Platform and technology independent action component definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Button
 * @see	javax.swing.JButton
 */
public interface IActionComponent extends ILabeledIcon 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the key combination which invokes the component's
     * action listeners without selecting. It is the
     * UI's responsibility to install the correct action.
     *
     * @param pKey the <code>Key</code> which will serve as an accelerator 
     */
    public void setAccelerator(Key pKey); 

    /**
     * Returns the <code>Key</code> which serves as an accelerator 
     * for the button.
     * 
     * @return a <code>Key</code> object identifying the accelerator key
     */
    public Key getAccelerator();
	
    /**
     * Returns the command name of the action event fired by this action component.
     * 
     * @return the action command.
     */
	public String getActionCommand();
	
    /**
     * Sets the command name for the action event fired
     * by this action component.
     * 
     * @param pActionCommand a string used to set the action command.
     * @see java.awt.event.ActionEvent
     */
	public void setActionCommand(String pActionCommand);

    /**
     * The EventHandler for the action event.
     * 
     * @return the EventHandler for the action event.
     */
	public ActionHandler eventAction();
	
}	// IActionComponent
