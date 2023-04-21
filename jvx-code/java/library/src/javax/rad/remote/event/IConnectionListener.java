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
 * 16.09.2011 - [JR] - #23: additional events
 * 04.04.2014 - [RZ] - #997: extracted propertyChanged() into IConnectionPropertyChanged
 */
package javax.rad.remote.event;

/**
 * The <code>IConnectionListener</code> interface should be used
 * to get notifications when communication errors occurs.
 * 
 * @author René Jahn
 */
public interface IConnectionListener extends IConnectionPropertyChangedListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * An error occured during remote communication.
	 * 
	 * @param pEvent the error information
	 */
	public void callError(CallErrorEvent pEvent);
	
	/**
	 * A connection was opened.
	 * 
	 * @param pEvent the connection information
	 */
	public void connectionOpened(ConnectionEvent pEvent);

	/**
	 * A connection was re-opened.
	 * 
	 * @param pEvent the connection information
	 */
	public void connectionReOpened(ConnectionEvent pEvent);
	
	/**
	 * A connection was closed.
	 * 
	 * @param pEvent the connection information
	 */
	public void connectionClosed(ConnectionEvent pEvent);
	
	/**
	 * An action was called.
	 * 
	 * @param pEvent the call information
	 */
	public void actionCalled(CallEvent pEvent);
	
	/**
	 * An object was called.
	 * 
	 * @param pEvent the call information
	 */
	public void objectCalled(CallEvent pEvent);
	
}	// IConnectionListener
