/*
 * Copyright 2011 SIB Visions GmbH
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
 * 16.09.2011 - [JR] - creation
 */
package javax.rad.remote.event;

import javax.rad.remote.AbstractConnection;

/**
 * The <code>CallEvent</code> class contains information about object and action calls.
 * The difference between object and action calls is that the object name is <code>null</code>
 * for action calls. And the action name is the method name.
 * 
 * @author René Jahn
 */
public class CallEvent extends ConnectionEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the object name (<code>null</code> for action calls). */
	private String sObjectName;
	
	/** the method or action name. */
	private String sMethodName;
	
	/** the call parameters. */
	private Object[] oParams;
	
	/** whether the call is a callback call. */
	private boolean bIsCallBack;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>CallEvent</code>.
	 * 
	 * @param pConnection the connection
	 * @param pObjectName the name of the object or <code>null</code> for an action call
	 * @param pMethodName the method or action name
	 * @param pParams the parameter
	 * @param pIsCallBack <code>true</code> if the call is a callback call, <code>false</code> otherwise
	 */
	public CallEvent(AbstractConnection pConnection, String pObjectName, String pMethodName, Object[] pParams, boolean pIsCallBack)
	{
	    super(pConnection);
	    
		sObjectName = pObjectName;
		sMethodName = pMethodName;
		oParams 	= pParams;
		bIsCallBack = pIsCallBack;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the object name.
	 * 
	 * @return the name of the object or <code>null</code> for an action call
	 */
	public String getObjectName()
	{
		return sObjectName;
	}
	
	/**
	 * Gets the method or action name.
	 * 
	 * @return the name
	 */
	public String getMethodName()
	{
		return sMethodName;
	}
	
	/**
	 * Gets the parameter list.
	 * 
	 * @return the parameter
	 */
	public Object[] getParameter()
	{
		return oParams;
	}
	
	/**
	 * Gets whether the call is a callback call.
	 * 
	 * @return <code>true</code> if the call is a callback call, <code>false</code> otherwise
	 */
	public boolean isCallBack()
	{
		return bIsCallBack;
	}
	
}	// CallEvent
