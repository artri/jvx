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
package demo;

import javax.annotation.PostConstruct;
import javax.rad.server.ICallHandler;
import javax.rad.server.ServerContext;
import javax.rad.server.SessionContext;
import javax.rad.server.event.CallEvent;
import javax.rad.server.event.CallResultEvent;
import javax.rad.server.event.SessionEvent;

/**
 * Session object for unit tests (with call handler).
 * 
 * @author René Jahn
 */
public class SessionWithCallHandler extends Session
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
        ICallHandler handler = ServerContext.getCurrentInstance().getCallHandler();

        if (handler != null)
        {
            handler.eventBeforeFirstCall().addListener(this, "doBeforeFirstCall");
            handler.eventBeforeCall().addListener(this, "doBeforeCall");
            handler.eventAfterCall().addListener(this, "doAfterCall");
            handler.eventAfterLastCall().addListener(this, "doAfterLastCall");
        }
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the name.
     * 
     * @return <code>LCO</code>
     */
    public String getName()
    {
        ServerContext.getCurrentInstance().getCallHandler().invokeAfterLastCall(new Runnable()
        {
            public void run()
            {
                addEvent("invokeAfterLastCall!");
            }
        });
        
        return "LCO";
    }

    /**
     * Throws an exception.
     * 
     * @return no value
     * @throws RuntimeException with message <code>LCO</code>
     */
    public String getNameWithException()
    {
        throw new RuntimeException("LCO");
    }

    /**
     * Simply adds an invoke after call operation. 
     */
    public void addInvokeAfterCall()
    {
        SessionContext.getCurrentInstance().getCallHandler().invokeAfterCall(new Runnable()
        {
            public void run()
            {
                addEvent("invokeAfterCall");
            }
        });
    }
    
    /**
     * Simply adds an invoke after last call operation. 
     */
    public void addInvokeAfterLastCall()
    {
        SessionContext.getCurrentInstance().getCallHandler().invokeAfterLastCall(new Runnable()
        {
            public void run()
            {
                addEvent("invokeAfterLastCall");
            }
        });
    }
    
    /**
     * Simply adds an invoke finally call operation. 
     */
    public void addInvokeFinally()
    {
        SessionContext.getCurrentInstance().getCallHandler().invokeFinally(new Runnable()
        {
            public void run()
            {
                addEvent("invokeFinally");
            }
        });
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
        addEvent("Before FIRST call");
    }

    /**
     * Invoked before call.
     * 
     * @param pEvent the event
     */
    public void doBeforeCall(CallEvent pEvent)
    {
        addEvent("Before call: " + pEvent.getMethodName());
    }

    /**
     * Invoked after call.
     * 
     * @param pEvent the event
     */
    public void doAfterCall(CallResultEvent pEvent)
    {
        addEvent("After call: " + pEvent.getMethodName() + ", error: " +  pEvent.hasError());
    }
    
    /**
     * Invoked after last call.
     * 
     * @param pEvent the event
     */
    public void doAfterLastCall(SessionEvent pEvent)
    {
        addEvent("After LAST call: " + pEvent.hasError());
    }
    
}	// SessionWithCallHandler
