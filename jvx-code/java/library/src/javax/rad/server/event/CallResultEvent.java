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

import javax.rad.server.ISession;

/**
 * The <code>CallResultEvent</code> serves the result from a method call. The result can be
 * any object (also an exception) or a call exception.
 * 
 * @author René Jahn
 */
public class CallResultEvent extends CallEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** return value from the method call. */
    private Object object;
    
    /** exception from method call. */
    private Throwable throwable;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>CallResultEvent</code>.
     * 
     * @param pSession the session
     * @param pSource the source session
     * @param pObjectName the object name for the call
     * @param pMethodName the method name for the call
     * @param pParameter the parameter list
     * @param pIsCallBack whether the call is a callback call
     * @param pObject result value from the method call. If 
     *                the method call returned a result, the exception is undefined!
     * @param pThrowable exception from the the remote method call. If 
     *                   the method call returned an error, the result is undefined!
     */
    public CallResultEvent(ISession pSession, 
                           ISession pSource,
                           String pObjectName, 
                           String pMethodName, 
                           Object[] pParameter,
                           boolean pIsCallBack,
                           Object pObject,
                           Throwable pThrowable)
    {
        super(pSession, pSource, pObjectName, pMethodName, pParameter, pIsCallBack, pThrowable != null);
        
        object = pObject;
        throwable = pThrowable;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the return value from the method call.
     * 
     * @return the return value from the call
     * @throws Throwable if the method call throwed an error
     */
    public Object getObject() throws Throwable
    {
        if (throwable != null)
        {
            throw throwable;
        }
        else
        {
            return object;
        }
    }    
    
}   // CallResultEvent
