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
 * 04.01.2013 - [SW] - creation
 * 07.08.2015 - [JR] - #1445: redesign
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.vaadin.ui.CheckBox;

/**
 * The <code>RadioButton</code> class is the server-side component of a radio button.
 * We extend the CheckBox because a RadioButton acts like a CheckBox. The real difference
 * for our implementation is the style.
 * 
 * @author Stefan Wurm
 */
public class RadioButton extends CheckBox
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>RadioButton</code>.
     */
    public RadioButton()
    {
        super();

        //would also be possible with a custom state class, but we save class files :)
        setPrimaryStyleName("v-radiobutton");        
    }
    
} 	// RadioButton
