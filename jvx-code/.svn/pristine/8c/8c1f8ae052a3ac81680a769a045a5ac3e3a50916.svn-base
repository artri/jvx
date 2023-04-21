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
 * 07.06.2016 - [JR] - creation
 */
package javax.rad.remote.event;

/**
 * The <code>ICallBackResultListener</code> is the listener for server-sent
 * callback objects.
 * 
 * @author René Jahn
 */
public interface ICallBackResultListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * If the <code>IConnection</code> detects a callback result, then this
	 * method will be called, to notify the client listener.
	 * 
	 * @param pEvent event of a server-sent callback 
	 * @throws Throwable if callback-result handling fails
	 */
	public void callBackResult(CallBackResultEvent pEvent) throws Throwable;
	
}	// ICallBackResultListener
