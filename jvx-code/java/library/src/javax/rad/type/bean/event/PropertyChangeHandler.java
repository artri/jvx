/*
 * Copyright 2021 SIB Visions GmbH
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
 * 30.09.2021 - [DJ] - creation
 */
package javax.rad.type.bean.event;

import javax.rad.util.EventHandler;

/**
 * The <code>PropertyChangeHandler</code> is a {@link EventHandler} that 
 * handles {@link IPropertyChangedListener}.
 * 
 * @author Jozef Dorko
 */
public class PropertyChangeHandler extends EventHandler<IPropertyChangedListener>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>PropertyChangeHandler</code>. 
     */
    public PropertyChangeHandler()
    {
        super(IPropertyChangedListener.class);
    }

}   // PropertyChangeHandler
