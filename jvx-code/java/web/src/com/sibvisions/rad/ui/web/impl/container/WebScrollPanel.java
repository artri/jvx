/*
 * Copyright 2019 SIB Visions GmbH
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
 * 10.09.2019 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.container;

import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;

import com.sibvisions.rad.ui.web.impl.WebContainer;

/**
 * Web server implementation of {@link IScrollPanel}.
 * 
 * @author Ren� Jahn
 */
public class WebScrollPanel extends WebContainer
                            implements IScrollPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebPanel</code>.
     *
     * @see IPanel
     */
	public WebScrollPanel()
	{
	}
	
}	// WebScrollPanel
