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
 * 11.12.2011 - [JR] - creation
 * 18.06.2015 - [JR] - #1413: IFileHandle support
 * 21.06.2018 - [JR] - recording implemented
 * 04.10.2018 - [JR] - convert list to array (fallback for parameter call)
 */
package com.sibvisions.rad.server.http.rest.service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.rad.server.ISession;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.rest.JSONUtil;
import com.sibvisions.rad.server.http.rest.LifeCycleConnector;
import com.sibvisions.rad.server.http.rest.RESTAdapter;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>CallService</code> allows action or object calls.
 * 
 * @author René Jahn
 */
public class CallService extends AbstractService
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Executes an object or action call without parameters.
	 * 
	 * @return the call result in JSON representation
	 */
	@Get
	public Representation call()
	{
		info("CALL-URL: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_GET);

		try
		{
			DirectServerSession session = getSession();
		
			ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();
	
			String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
			String sActionName = (String)cmpAttrib.get(RESTAdapter.PARAM_ACTION_NAME);
	
			Object oResult;
			
			if (sObjectName != null)
			{
				oResult = session.call(sObjectName, sActionName);
			}
			else
			{
				oResult = session.callAction(sActionName);
			}

			Representation rep = toInternalRepresentation(oResult);

			setStatus(Status.SUCCESS_OK);
			
			return rep;
		}
		catch (Throwable th)
		{		
			if (record != null)
			{
				record.setException(th);
			}
				
			return handleException(th);
		}
		finally
		{
			CommonUtil.close(record);
		}
	}

	/**
	 * Executes an object or action call with or without parameters.
	 * 
	 * @param pParameter the call parameter
	 * @return call result in JSON representation
	 */
	@Put
	@Post
	public Representation call(Representation pParameter)
	{
		info("CALL-URL-WITH-PARAMS: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_POST);
		
		try
		{
			Object[] oParams;
			
			if (pParameter != null)
			{
				oParams = JSONUtil.getObject(pParameter, Object[].class);
			}
			else
			{
				oParams = null;
			}
			
			if (record != null)
			{
				record.setParameter(oParams);
			}
			DirectServerSession session = ((LifeCycleConnector)getRequest().getClientInfo().getUser()).getSession();
			
			ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();
	
			String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
			String sActionName = (String)cmpAttrib.get(RESTAdapter.PARAM_ACTION_NAME);
	
			Object oResult;
			
			try
			{
			    oResult = call(session, sObjectName, sActionName, oParams);
			}
			catch (NoSuchMethodException nsme)
			{
				Object[] oConverted = convertParameter(oParams);
				
				try
				{
					oResult = call(session, sObjectName, sActionName, oConverted);
				}
				catch (NoSuchMethodException nsmex)
				{
					oConverted = convertListToArray(oConverted);
					
					oResult = call(session, sObjectName, sActionName, oConverted);
				}
			}
			
		    return toInternalRepresentation(oResult);
		}
		catch (Throwable th)
		{			
			if (record != null)
			{
				record.setException(th);
			}
			
		    return handleException(th);
		}
		finally
		{
			CommonUtil.close(record);
		}
	}
	
	/**
	 * Executes a session call.
	 * 
	 * @param pSession the session to use
	 * @param pObjectName the object name (null will call an action)
	 * @param pActionName the action name
	 * @param pParams the optional parameters
	 * @return the return value from the call
	 * @throws Throwable if object/action doesn't exist or call throws an error
	 */
	private Object call(ISession pSession, String pObjectName, String pActionName, Object[] pParams) throws Throwable
	{
        if (pObjectName != null)
        {
            return pSession.call(pObjectName, pActionName, pParams);
        }
        else
        {
            return pSession.callAction(pActionName, pParams);
        }
	}
	
	/**
	 * Converts {@link List} values to <code>Object[]</code>s.
	 * 
	 * @param pValues the original values
	 * @return an array with <code>Object[]</code>s instead of {@link List}s.
	 */
	private Object[] convertListToArray(Object[] pValues)
	{
		if (pValues == null)
		{
			return null;
		}
		
		//don't create new array instances if not needed. A new instance only is needed
		//if the original array contains a List instance
		Object[] values = null;
		
		for (int i = 0; i < pValues.length; i++)
		{
			if (pValues[i] instanceof List)
			{
				if (values == null)
				{
					values = pValues.clone();
				}
				
				List list = (List)pValues[i];
				values[i] = convertListToArray(list.toArray(new Object[list.size()]));
			}
			else if (values != null)
			{
				values[i] = pValues[i];
			}
		}
			
		return values != null ? values : pValues;
	}
	
}	// CallService
