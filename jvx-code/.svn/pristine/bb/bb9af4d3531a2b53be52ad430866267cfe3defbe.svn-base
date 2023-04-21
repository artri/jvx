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
 * 29.05.2015 - [JR] - creation
 */
package demo.special;

import javax.annotation.PostConstruct;
import javax.rad.server.ICallHandler;
import javax.rad.server.ServerContext;
import javax.rad.server.SessionContext;
import javax.rad.server.event.CallEvent;
import javax.rad.server.event.CallResultEvent;
import javax.rad.server.event.SessionEvent;

import demo.SessionWithCallHandler;

/**
 * Screen object for unit tests (with call handler).
 * 
 * @author René Jahn
 */
public class ScreenSessionWithCallHandler extends SessionWithCallHandler
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the name.
     * 
     * @return <code>LCO</code>
     */
    @Override
    public String getName()
    {
        ServerContext.getCurrentInstance().getCallHandler().invokeAfterLastCall(new Runnable()
        {
            public void run()
            {
                addEvent("invokeAfterLastCall (screen)!");
            }
        });
        
        return "SCREEN_SESSION";
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Event handling
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invoked before first call.
     * 
     * @param pEvent the event
     */
    public void doBeforeFirstCall(SessionEvent pEvent)
    {
        addEvent("Before FIRST call (screen)");
    }

    /**
     * Invoked before call.
     * 
     * @param pEvent the event
     */
    public void doBeforeCall(CallEvent pEvent)
    {
        addEvent("Before call (screen): " + pEvent.getMethodName());
    }

    /**
     * Invoked after call.
     * 
     * @param pEvent the event
     */
    public void doAfterCall(CallResultEvent pEvent)
    {
        addEvent("After call (screen): " + pEvent.getMethodName() + ", error: " +  pEvent.hasError());
    }
    
    /**
     * Invoked after last call.
     * 
     * @param pEvent the event
     */
    public void doAfterLastCall(SessionEvent pEvent)
    {
        addEvent("After LAST call (screen): " + pEvent.hasError());
    }
    
}	// ScreenSessionWithCallHandler
