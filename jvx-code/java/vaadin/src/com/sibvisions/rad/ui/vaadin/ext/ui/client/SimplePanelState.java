/*
 * Copyright 2015 SIB Visions GmbH
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
 * 22.10.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.vaadin.shared.ui.csslayout.CssLayoutState;


/**
 * The <code>SimplePanelState</code> adds support for background style properties.
 * 
 * @author René Jahn
 */
public class SimplePanelState extends CssLayoutState
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** background-repeat. */
    public String backgroundRepeat = null;
    /** background-size. */
    public String backgroundSize = null;
    
}   // SimplePanelState
