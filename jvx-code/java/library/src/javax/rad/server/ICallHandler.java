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
 * 27.05.2015 - [JR] - creation
 * 29.05.2015 - [JR] - refactoring (invokeLater -> invokeAfterLatCall, invokeAfterCall)
 */
package javax.rad.server;

import javax.rad.server.event.CallEventHandler;
import javax.rad.server.event.type.IAfterCallListener;
import javax.rad.server.event.type.IAfterLastCallListener;
import javax.rad.server.event.type.IBeforeCallListener;
import javax.rad.server.event.type.IBeforeFirstCallListener;

/**
 * The <code>ICallHandler</code> allows listening on remote calls and offers an
 * invokeLater mechanism like on UI side.
 * 
 * @author René Jahn
 */
public interface ICallHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the handler for "before first call" events.
     * 
     * @return the event handler
     */
    public CallEventHandler<IBeforeFirstCallListener> eventBeforeFirstCall();
    
    /**
     * Gets the handler for "after last call" events.
     * 
     * @return the event handler
     */
    public CallEventHandler<IAfterLastCallListener> eventAfterLastCall();

    /**
     * Gets the handler for "before call" events.
     * 
     * @return the event handler
     */
    public CallEventHandler<IBeforeCallListener> eventBeforeCall();

    /**
     * Gets the handler for "after call" events.
     * 
     * @return the event handler
     */
    public CallEventHandler<IAfterCallListener> eventAfterCall();

    /**
     * Adds an operation to the "after call" queue. Such operations will be automatically executed after a call.
     * 
     * @param pRunnable the operation
     */
    public void invokeAfterCall(Runnable pRunnable);
    
    /**
     * Adds an operation to the "after call" queue. Such operations will be automatically executed after a call.
     * 
     * @param pObject the object which contains the operation
     * @param pMethod the operation method
     */
    public void invokeAfterCall(Object pObject, String pMethod);
    
    /**
     * Adds an operation to the "after last call" queue. Such operations will be automatically executed after the last
     * call.
     * 
     * @param pRunnable the operation
     */
    public void invokeAfterLastCall(Runnable pRunnable);
    
    /**
     * Adds an operation to the "after last call" queue. Such operations will be automatically executed after the last
     * call.
     * 
     * @param pObject the object which contains the operation
     * @param pMethod the operation method
     */
    public void invokeAfterLastCall(Object pObject, String pMethod);
    
    /**
     * Adds an operation to the invokeFinally queue. Such operations will be automatically executed after the last
     * call and after all invokeLater operations.
     * 
     * @param pRunnable the operation
     */
    public void invokeFinally(Runnable pRunnable);
    
    /**
     * Adds an operation to the invokeFinally queue. Such operations will be automatically executed after the last
     * call and after all invokeLater operations.
     * 
     * @param pObject the object which contains the operation
     * @param pMethod the operation method
     */
    public void invokeFinally(Object pObject, String pMethod);
    
}   // ICallHandler
