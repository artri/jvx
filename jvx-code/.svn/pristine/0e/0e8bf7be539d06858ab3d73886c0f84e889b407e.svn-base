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
 * 18.05.2010 - [JR] - create an instance of Reflective to ensure the correct UI thread!
 * 03.06.2014 - [JR] - #1054: set event source
 * 31.08.2017 - [JR] - #1819: don't send callback results 
 */
package com.sibvisions.rad.server;

import javax.rad.remote.IConnection;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.server.ResultObject;

import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>Call</code> is an abstract view of a method call. A method
 * call needs information for the execution:
 * <ul>
 *   <li>synchronous or asynchronous calls</li>
 *   <li>an object name</li>
 *   <li>a method name</li>
 *   <li>parameters</li>
 * </ul>
 * @author René Jahn
 */
final class Call
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the listener for results. */
	private ICallBackListener listener;
	
	/** the callback for results. */
	private Object oCallBackId;
	
	/** the object name for the call. */
	private String sObjectName;
	
	/** the method name for the call. */
	private String sMethodName;
	
	/** the parameters for the call. */
	private Object[] oParams;
	
	/** the creation time. */
	private long lCreation = System.currentTimeMillis();
	
	/** whether this call is an explicite callback. */
	private boolean bForceCallBack = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Call</code>.
	 * 
	 * @param pCallBackListener a callback listener for an asynchronous call or <code>null</code> for a synchronous call
	 * @param pObjectName the object for the call
	 * @param pMethodName the method to call
	 * @param pParams the call parameters
	 */
	Call(ICallBackListener pCallBackListener, String pObjectName, String pMethodName, Object... pParams)
	{
		listener = pCallBackListener;
		
		sObjectName = pObjectName;
		sMethodName = pMethodName;
		oParams = pParams;
	}
	
	/**
	 * Creates a new instance of <code>Call</code>.
	 * 
	 * @param pCallBackId a callback id for an asynchronous call or <code>null</code> for a synchronous call
	 * @param pObjectName the object for the call
	 * @param pMethodName the method to call
	 * @param pParams the call parameters
	 */
	Call(Object pCallBackId, String pObjectName, String pMethodName, Object... pParams)
	{
		oCallBackId = pCallBackId;

		sObjectName = pObjectName;
		sMethodName = pMethodName;
		oParams = pParams;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the object name for the method call.
	 * 
	 * @return the object name
	 */
	public String getObjectName()
	{
		return sObjectName;
	}
	
	/**
	 * Gets the method name for the call.
	 * 
	 * @return the method name
	 */
	public String getMethodName()
	{
		return sMethodName;
	}
	
	/**
	 * Gets the parameters for the call.
	 * 
	 * @return the parameters
	 */
	public Object[] getParameters()
	{
		return oParams;
	}
	
	/**
	 * Checks if the call is asynchronous (callback) or synchronous.  
	 * 
	 * @return <code>true</code> if the call is asynchronous
	 */
	public boolean isCallBack()
	{
		return listener != null || oCallBackId != null || bForceCallBack;
	}
	
	/**
	 * Formats the call to a readable format: <code>objectName</code>.<code>methodName</code>
	 * or <code>methodName</code> if the <code>objectName == null</code>. 
	 * 
	 * @return the method format
	 */
	public String formatMethod()
	{
		if (sObjectName == null)
		{
			return sMethodName;
		}
		else
		{
			return sObjectName + "." + sMethodName;
		}
	}
	
	/**
	 * Handles a successfull asynchronous call. If a callback listener was specified, then
	 * it will be notified. The result will be added as callback result to the session if
	 * a callback id was specified. It no callback listener and no callback id was specified
	 * then a {@link RuntimeException} will be thrown.
	 *   
	 * @param pSession the caller session
	 * @param pResult the result from an asynchronous call
	 * @throws RuntimeException if no callback id and no callback listener was found
	 */
	void success(AbstractSession pSession, Object pResult)
	{
		sendResult(pSession, IConnection.TYPE_CALLBACK_RESULT, pResult);
	}

	/**
	 * Handles an exception thrown from an asynchronous call. If a callback listener was specified, then
	 * it will be notified. The exception will be added as callback result to the session if
	 * a callback id was specified. It no callback listener and no callback id was specified
	 * then a {@link RuntimeException} will be thrown.
	 *   
	 * @param pSession the caller session
	 * @param pError the exception from an asynchronous call
	 * @throws RuntimeException if no callback id and no callback listener was found
	 */
	void error(AbstractSession pSession, Throwable pError)
	{
		sendResult(pSession, IConnection.TYPE_CALLBACK_ERROR, pError);
	}
	
	/**
	 * Handles the callback result via invokeLater.
	 * 
	 * @param pSession the caller session
	 * @param pType the result type {@link IConnection#TYPE_CALLBACK_ERROR} or {@link IConnection#TYPE_CALLBACK_RESULT}
	 * @param pResult the result/error object
	 */
	private void sendResult(final AbstractSession pSession, final byte pType, final Object pResult)
	{
	    //#1819
        if (pSession.getSessionManager().isValid(pSession))
        {
            if (oCallBackId != null)
            {
                pSession.addCallBackResult(new ResultObject(pType, pResult, oCallBackId));
            }
            else if (listener != null)
            {
            	try
            	{
    	            listener.callBack(new CallBackEvent(pSession.getSessionManager().getServer(),
    	                                                sObjectName, 
    	                                                sMethodName, 
    	                                                IConnection.TYPE_CALLBACK_RESULT == pType ? pResult : null,
    	                                                (Throwable)(IConnection.TYPE_CALLBACK_ERROR == pType ? pResult : null),
    	                                                lCreation,
    	                                                System.currentTimeMillis()));
            	}
            	catch (Throwable th)
            	{
            		LoggerFactory.getInstance(Call.class).error(th);
            	}
            }
            else if (!bForceCallBack)
            {
                throw new RuntimeException("Callback not possible without callback id or callback listener!");
            }
        }
        else
        {
            LoggerFactory.getInstance(Call.class).info("Callback suppressed because session is not valid!");
        }
	}
	
	/**
	 * Sets whether this call is a callback regardless of the callback settings (listener, id).
	 * 
	 * @param pForceCallBack <code>true</code> to set this call as "forced" callback, <code>false</code> otherwise
	 */
	void setForceCallBack(boolean pForceCallBack)
	{
		bForceCallBack = pForceCallBack;		
	}

	/**
	 * Gets wheter this call is a callback regardless of the callback settings (listener, id).
	 * 
	 * @return <code>true</code> if this call is as "forced" callback, <code>false</code> otherwise
	 */
	boolean isForceCallBack()
	{
		return bForceCallBack;
	}
	
}	// Call
