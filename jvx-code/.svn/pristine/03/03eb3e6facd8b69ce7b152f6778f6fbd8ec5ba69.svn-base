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
 * @see ICallBackListener
 */
public class CallBackEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** the event source object. */
    private Object oSource;
    
	/** object name of the remote method call. */
	private String sObjectName;
	
	/** method name of the remote call. */
	private String sMethodName;
	
	/** return value from the server for the method call. */
	private Object object;
	
	/** exception from the server for the method call. */
	private Throwable throwable;
	
	/** request time of the remote method call, in millis. */
	private long lRequestTime;
	
	/** response time of the result, in millis. */
	private long lResponseTime;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>CallBackEvent</code>.
	 * 
	 * @param pSource the event source
	 * @param pObjectName object name for the remote method call
	 * @param pMethodName method name for the remote call
	 * @param pObject result value from the server for the remote method call. If 
	 *                the method call returned a result, the exception is undefined!
	 * @param pThrowable exception from the server for the remote method call. If 
	 *                   the method call returned an error, the result is undefined!
	 * @param pRequest request time of the remote method call, in millis
	 * @param pResponse response time fo the result, in millis
	 */
	public CallBackEvent
	(
	    Object pSource,
		String pObjectName, 
		String pMethodName, 
		Object pObject,
		Throwable pThrowable,
		long pRequest, 
		long pResponse)
	{
	    oSource     = pSource;
		sObjectName = pObjectName;
		sMethodName = pMethodName;
		object      = pObject;
		throwable   = pThrowable;
		
		lRequestTime  = pRequest;
		lResponseTime = pResponse;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the source object of this event.
	 * 
	 * @return the source object
	 */
	public Object getSource()
	{
	    return oSource;
	}
	
	/**
	 * Gets the return value from a remote method call.
	 * 
	 * @return the return value from the remote server
	 * @throws Throwable if the remote method call throwed an error
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
	
	/**
	 * Determines if the result object is an exception, thrown by a call.
	 * 
	 * @return <code>true</code> if the result object is an exception thrown by a call.
	 *         <code>false</code> if the result of a call is an exception or the result
	 *         had no errors
	 */
	public boolean isError()
	{
		return throwable != null;
	}
	
	/**
	 * Gets the exception thrown by the call. 
	 * In case an exception occurred, getObject will throw this exception.
	 * It is sometimes useful, to get the exception, if it exists, without the need, to call getObject, and catch the Throwable. 
	 * 
	 * @return the Throwable that is possibly thrown from the call or null if the server call had no exception.
	 */
	public Throwable getThrowable()
	{
	    return throwable;
	}
	
	/**
	 * Returns the object name of the remote method call.
	 * 
	 * @return object name
	 */
	public String getObjectName()
	{
		return sObjectName;
	}
	
	/**
	 * Returns the method name of the remote call.
	 * 
	 * @return method name
	 */
	public String getMethodName()
	{
		return sMethodName;
	}
	
	/** 
	 * Returns the time at which the remote method call was sent to the remote
	 * server.
	 * 
	 * @return time, in millis
	 */
	public long getRequestTime()
	{
		return lRequestTime;
	}

	/**
	 * Returns the time which the result, from the remote server, was received.
	 * 
	 * @return time in millis
	 */
	public long getResponseTime()
	{
		return lResponseTime;
	}

}	// CallBackEvent
