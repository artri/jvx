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
 * 12.04.2019 - [DJ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

/**
 * The {@code MouseDownServerRpc} class is the RPC-Interface for the {@link com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel}.
 * 
 * @author Jozef Dorko
 */
public interface MouseDownServerRpc extends ServerRpc
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Called by mouse down.
     * 
     * @param pEvent the MouseEvent
     */
    public void onMouseDown(MouseEventDetails pEvent);
}
