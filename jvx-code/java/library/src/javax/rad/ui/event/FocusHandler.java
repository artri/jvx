/*
 * Copyright 2011 SIB Visions GmbH
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
 * 18.02.2014 - [HM] - creation
 * 27.20.2015 - [JR] - changed constructor
 */
package javax.rad.ui.event;

import javax.rad.util.RuntimeEventHandler;

/**
 * The <code>FocusHandler</code> is a <code>RuntimeEventHandler</code> that 
 * handles <code>IFocusListener</code>. 
 * 
 * @author Martin Handsteiner
 * 
 * @param <L> the Listener type
 */
public class FocusHandler<L> extends RuntimeEventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>FocusHandler</code>.
     * 
     * @param pClass the listener class
     */
    public FocusHandler(Class<L> pClass)
    {
        super(pClass);
    }

}	// FocusHandler
