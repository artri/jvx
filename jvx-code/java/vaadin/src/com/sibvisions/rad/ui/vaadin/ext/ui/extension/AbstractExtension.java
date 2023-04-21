/*
 * Copyright 2018 SIB Visions GmbH
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
 * 14.02.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

import com.vaadin.ui.UI;

/**
 * The <code>AbstractExtension</code> is the base class for custom extensions.
 *  
 * @author René Jahn
 */
public abstract class AbstractExtension extends com.vaadin.server.AbstractExtension
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Forces retransfer of this extensions state.
     */
    protected void clearState()
    {
        UI ui = getUI();
        
        if (ui != null)
        {
            ui.getConnectorTracker().setDiffState(this, null);
        }
    }

}   // AbstractExtension
