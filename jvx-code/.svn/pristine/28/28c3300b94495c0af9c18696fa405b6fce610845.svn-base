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
 * 01.10.2008 - [JR] - creation
 */
package javax.rad.remote.event;

/**
 * The <code>ICallBackListener</code> interface enables the asynchronous
 * communication between client and server. The callback detection
 * will be managed by an <code>IConnection</code> implementation.
 * 
 * @author René Jahn
 */
public interface ICallBackListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * If the <code>IConnection</code> detects a callback, then this
	 * method will be called, to notify the client listener.
	 * 
	 * @param pEvent result, of an asynchronous method call
	 * @throws Throwable if callback handling fails
	 */
	public void callBack(CallBackEvent pEvent) throws Throwable;
	
}	// ICallBackListener
