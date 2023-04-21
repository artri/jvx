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
 * 16.11.2008 - [HM] - creation
 */
package javax.rad.genui.component;

import javax.rad.ui.component.IToggleButton;

/**
 * Platform and technology independent toggle button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @param <C> instance of IToggleButton
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Button
 * @see	javax.swing.JToggleButton
 */
public abstract class AbstractUIToggleButton<C extends IToggleButton> extends AbstractUIButton<C> 
                                                                      implements IToggleButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIToggleButton</code>.
     *
     * @param pToggleButton the IToggleButton.
     * @see IToggleButton
     */
	protected AbstractUIToggleButton(C pToggleButton)
	{
		super(pToggleButton);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
    {
    	return uiResource.isSelected();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSelected(boolean pPressed)
    {
    	uiResource.setSelected(pPressed);
    }
    
}	// AbstractUIToggleButton
