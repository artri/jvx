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
package com.sibvisions.rad.ui.swing.impl.menu;

import javax.rad.ui.menu.ISeparator;
import javax.swing.JPopupMenu;

import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingSeparator</code> is the <code>ISeparator</code>
 * implementation for swing.
 * 
 * @author René Jahn
 * @see	javax.swing.JPopupMenu.Separator
 * @see javax.rad.ui.menu.ISeparator
 */
public class SwingSeparator extends SwingComponent<JPopupMenu.Separator> 
                            implements ISeparator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingSeparator</code>.
	 */
	public SwingSeparator()
	{
		super(new JPopupMenu.Separator());
	}

}	// SwingSeparator
