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
 * 28.05.2015 - [JR] - creation
 */
package javax.rad.server.event;

import javax.rad.util.EventHandler;

/**
 * The <code>CallEventHandler</code> is an {@link EventHandler} which handles
 * {@link ICallListener}.
 * 
 * 
 * @author René Jahn
 *
 * @param <L> the listener type
 */
public class CallEventHandler<L> extends EventHandler<L>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>CallEventHandler</code>.
     *  
     * @param pClass the listener class
     */
    public CallEventHandler(Class<L> pClass)
    {
        super(pClass);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object dispatchEvent(Object... pEventParameter)
    {
        try
        {
            return super.dispatchEvent(pEventParameter);
        }
        catch (Throwable pThrowable)
        {
            throw new RuntimeException("Exception in listener!", pThrowable);
        }
    }

}   // CallEventHandler
