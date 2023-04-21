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
 * 29.04.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.event;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;

/**
 * The <code>UIListener</code> class informs about states in {@link VaadinUI}.
 * 
 * @author Ren� Jahn
 */
public interface UIListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invoked when UI will be reloaded.
     * 
     * @param pUI the Vaadin UI
     */
    public void reload(VaadinUI pUI);
    
}	// UIListener