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
 * 24.09.2013 - [JR] - creation
 */
package javax.rad.genui.menu;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.menu.ISeparator;

/**
 * Platform and technology independent menu separator.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public class UISeparator extends UIComponent<ISeparator>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UISeparator</code>.
     *
     * @see ISeparator
     */
	public UISeparator()
	{
		super(UIFactoryManager.getFactory().createSeparator());
	}
	
    /**
     * Creates a new instance of <code>UISeparator</code> with the given
     * separator.
     *
     * @param pSeparator the menu item
     * @see ISeparator
     */
	protected UISeparator(ISeparator pSeparator)
	{
		super(pSeparator);
	}
	
}	// UISeparator
