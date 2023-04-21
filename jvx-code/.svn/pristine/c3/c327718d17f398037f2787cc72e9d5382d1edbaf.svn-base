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
package javax.rad.server;

/**
 * The <code>ResultObject</code> encapsulates the return type and value of
 * a remote call.
 * 
 * @author René Jahn
 */
public final class ResultObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** return type of remote method call. */
	private byte byType;
	
	/** return value of remote method call. */
	private Object object;
	
	/** callback identifier sent by the client, for asynchronous calls. */
	private Object oCallBackId;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ResultObject</code>.
	 * 
	 * @param pType result type
	 * @param pObject result object
	 */
	public ResultObject(byte pType, Object pObject)
	{
		this(pType, pObject, null);
	}
	
	/**
	 * Creates a new instance of <code>ResultObject</code> for an 
	 * asynchronous call.
	 * 
	 * @param pType result type
	 * @param pObject result object
	 * @param pCallBackId callback identifier for the result
	 */
	public ResultObject(byte pType, Object pObject, Object pCallBackId)
	{
		this.byType = pType;
		this.object = pObject;
		this.oCallBackId = pCallBackId;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the type of a remote method call.
	 * 
	 * @return one of the following {@link javax.rad.remote.IConnection#TYPE_CALL_RESULT}, {@link javax.rad.remote.IConnection#TYPE_CALL_ERROR},
	 *         {@link javax.rad.remote.IConnection#TYPE_CALLBACK_RESULT}, {@link javax.rad.remote.IConnection#TYPE_CALLBACK_ERROR}
	 */
	public final byte getType()
	{
		return byType;
	}
	
	/**
	 * Gets the result of a remote method call.
	 * 
	 * @return any object or null
	 */
	public final Object getObject()
	{
		return object;
	}
	
	/**
	 * Gets the callback identifier from an asynchronous method call.
	 * 
	 * @return callback identifier
	 */
	public final Object getCallBackId()
	{
		return oCallBackId;
	}
	
}	// ResultObject
