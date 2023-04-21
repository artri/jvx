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
 * 25.06.2009 - [HM] - creation
 */
package javax.rad.ui.event;

import javax.rad.util.RuntimeEventHandler;


/**
 * The <code>KeyHandler</code> is a <code>EventHandler</code> that 
 * handles <code>IKeyListener</code>. 
 * 
 * @author Martin Handsteiner
 * 
 * @param <L> the Listener type
 */
public  class KeyHandler<L> extends RuntimeEventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>KeyHandler</code>.
     * 
     * @param pClass the listener class
     */
    public KeyHandler(Class<L> pClass)
    {
        super(pClass);
    }
	
}	// KeyHandler
