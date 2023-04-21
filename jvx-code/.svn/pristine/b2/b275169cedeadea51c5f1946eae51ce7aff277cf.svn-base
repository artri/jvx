/*
 * Copyright 2016 SIB Visions GmbH
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
package demo;

import javax.annotation.PostConstruct;
import javax.rad.server.ICallHandler;
import javax.rad.server.SessionContext;

/**
 * Session object for unit tests (with call handler).
 * 
 * @author René Jahn
 */
public class SessionWithSessionCallHandler extends SessionWithCallHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Configures event handling.
	 */
    @PostConstruct
	public void createSession()
	{
        ICallHandler handler = SessionContext.getCurrentInstance().getCallHandler();

        handler.eventBeforeFirstCall().addListener(this, "doBeforeFirstCall");
        handler.eventBeforeCall().addListener(this, "doBeforeCall");
        handler.eventAfterCall().addListener(this, "doAfterCall");
        handler.eventAfterLastCall().addListener(this, "doAfterLastCall");
	}
    
}	// SessionWithSessionCallHandler
