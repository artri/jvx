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
package javax.rad.server.event.type;

import javax.rad.server.event.SessionEvent;

/**
 * The <code>IBeforeFirstCallListener</code> will be used for notifications before the first 
 * call. If one call request contains multiple calls, it's important to know the first call.
 * If there's only one call per call request, this listener won't help.
 * 
 * @author Ren� Jahn
 */
public interface IBeforeFirstCallListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invoked before the first call.
     * 
     * @param pEvent the event
	 * @throws Throwable if there is an error.
     */
    public void beforeFirstCall(SessionEvent pEvent) throws Throwable;
    
}   // IBeforeFirstCallListener
