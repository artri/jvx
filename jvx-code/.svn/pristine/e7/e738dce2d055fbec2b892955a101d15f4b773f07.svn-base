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
 * 27.05.2009 - [JR] - creation
 * 27.05.2015 - [JR] - #1397: afterLastCall, beforeFirstCall defined
 */
package com.sibvisions.rad.server;

import javax.rad.remote.event.ICallBackListener;
import javax.rad.server.IServer;
import javax.rad.server.push.IPushHandler;

/**
 * The <code>IDirectServer</code> interface defines a way to handle callbacks direct without
 * callback-ID mapping. It's an extension of {@link IServer} because an {@link IServer} implementation
 * is usable via EJB. This extension is only needed if the server and the client runs in the
 * same VM.
 * 
 * @author René Jahn
 */
public interface IDirectServer extends IServer,
                                       IPushHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Executes an asynchronous method call.
	 *  
	 * @param pSessionId session identifier
	 * @param pCallBackListener the callback listener
	 * @param pObjectName server object name/alias
	 * @param pMethod method name which should be called
	 * @param pParams parameters for the method call
	 * @throws Throwable if an error occurs during execution
	 */
	public void executeCallBack(Object pSessionId, ICallBackListener pCallBackListener, String pObjectName, String pMethod, Object... pParams) throws Throwable;

	/**
	 * Executes an asynchronous action call.
	 *  
	 * @param pSessionId session identifier
	 * @param pCallBackListener the callback listener
	 * @param pAction action which should be called
	 * @param pParams parameters for the action call
	 * @throws Throwable if an error occurs during execution
	 */
	public void executeActionCallBack(Object pSessionId, ICallBackListener pCallBackListener, String pAction, Object... pParams) throws Throwable;
	
	/**
	 * Notifies the server that at least one call will follow. It's possible that multiple calls will be
	 * executed in batch mode.
	 * 
	 * @param pSessionId the session id which will execute at least one call
	 */
    public void beforeFirstCall(Object pSessionId);

    /**
     * Notifies the server that all calls were executed. This method will be invoked even if one
     * call throwed an exception. 
     * 
     * @param pSessionId the session id which executed at least one call
     * @param pCallError <code>true</code> if at least one call throwed an exception
     */
    public void afterLastCall(Object pSessionId, boolean pCallError);
	
}	// IDirectServer
