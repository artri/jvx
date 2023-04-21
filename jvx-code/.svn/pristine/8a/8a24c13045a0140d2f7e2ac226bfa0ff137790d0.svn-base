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
 * 03.06.2014 - [JR] - #1054
 *                     * source object added
 *                     * internal access to call result and exception                   
 */
package javax.rad.remote.event;

/**
 * The <code>CallBackEvent</code> will be sent to an <code>ICallBackListener</code>
 * if an asynchronous method call returns a result.
 * 
 * @author René Jahn
 */
public class CallBackResultEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** return object from the server. */
	private Object object;
	
    /** the instruction identifier for the callback result. */
    private String sInstruction;
    
    /** the connection identifier. */
    private String sId;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>CallBackEvent</code>.
	 *
	 * @param pInstruction the instruction identifier
	 * @param pObject object sent from the server
	 */
	public CallBackResultEvent(String pInstruction, Object pObject)
	{
	    int iSplit = pInstruction.lastIndexOf("@");
	    
	    sInstruction = pInstruction.substring(0, iSplit);
	    sId = pInstruction.substring(iSplit + 1);
	    
	    object       = pObject;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the instruction identifier.
	 * 
	 * @return the instruction identifier
	 */
	public String getInstruction()
	{
	    return sInstruction;
	}
	
	/**
	 * Gets the connection identifier.
	 * 
	 * @return the connection identifier
	 */
	public String getConnectionId()
	{
	    return sId;
	}
	
	/**
	 * Gets the server-sent object.
	 * 
	 * @return the object from the server
	 */
	public Object getObject()
	{
		return object;
	}

}	// CallBackResultEvent
