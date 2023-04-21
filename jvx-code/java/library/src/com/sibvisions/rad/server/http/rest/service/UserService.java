/*
 * Copyright 2021 SIB Visions GmbH
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
 * 17.02.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.http.rest.JSONUtil;
import com.sibvisions.rad.server.http.rest.RESTAdapter;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>UserService</code> is a special user service which allows registration of specific services.
 * 
 * @author René Jahn
 */
public class UserService extends AbstractCustomService
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Executes special user calls with max. one simple parameter.
	 * 
	 * @return the result
	 */
	@Get
	public Representation execute()
	{
		Request request = getRequest();
		
		ConcurrentMap<String, Object> cmpAttrib = request.getAttributes();

		String sAction = (String)cmpAttrib.get(RESTAdapter.PARAM_USER_ACTION);
		String sParam =  (String)cmpAttrib.get(RESTAdapter.PARAM_USER_ACTION_PARAM);

		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_GET);

		if (record != null)
		{
			record.setParameter(sAction, sParam);
		}
		
		try
		{
			LoggerFactory.getInstance(UserService.class).debug(sAction);
			
        	String sApplicationName = (String)getAttribute(RESTAdapter.PARAM_APP_NAME);
        	
			//implicite application check
			Configuration.getApplicationZone(sApplicationName);
			
			LoggerFactory.getInstance(UserService.class).debug(sAction);
			
			Representation repResult = delegateGet(UserService.class, sApplicationName, sAction, sParam);
			
			applySession(record);
			
			return repResult;					
		}
		catch (Throwable th)
		{
			LoggerFactory.getInstance(UserService.class).error(th);
			
			if (record != null)
			{
				record.setException(th);
			}
			
		    return handleException(th);
		}
		finally
		{
			try
			{
				destroySessions();
			}
			finally
			{
				CommonUtil.close(record);
			}
		}
	}
	
	/**
	 * Executes special user calls.
	 * 
	 * @param pParameter the parameters
	 * @return the result
	 */
	@Post
	public Representation execute(Representation pParameter)
	{
		Request request = getRequest();
		
		ConcurrentMap<String, Object> cmpAttrib = request.getAttributes();
		
		String sAction = (String)cmpAttrib.get(RESTAdapter.PARAM_USER_ACTION);
		String sParam =  (String)cmpAttrib.get(RESTAdapter.PARAM_USER_ACTION_PARAM);

		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_POST);
		
		try
		{
			Map<String, Object> mpInput = (Map<String, Object>)JSONUtil.getObject(pParameter, HashMap.class);
			
			if (record != null)
			{
				record.setParameter(sAction, sParam, mpInput);
			}

			String sApplicationName = (String)getAttribute(RESTAdapter.PARAM_APP_NAME);

	    	//implicite application check
			Configuration.getApplicationZone(sApplicationName);
			
			LoggerFactory.getInstance(UserService.class).debug(sAction);
			
			Representation repResult = delegatePost(UserService.class, sApplicationName, sAction, sParam, mpInput);
			
			applySession(record);
			
			return repResult;
		}
		catch (Throwable th)
		{
			LoggerFactory.getInstance(UserService.class).error(th);			
			
			if (record != null)
			{
				record.setException(th);
			}
			
		    return handleException(th);
		}
		finally
		{
			try
			{
				destroySessions();
			}
			finally
			{
				CommonUtil.close(record);
			}
		}
	}
	
	/**
	 * Registers a service delegation instance for an application and a custom action. This method doesn't
	 * override existing registrations. If an action has a delegate instance, it will kept.
	 * 
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pDelegate the delegation instance
	 * @return <code>true</code> if registration was successful, <code>false</code> if action already contains
	 *         a delegation instance
	 */
	public static boolean register(String pApplicationName, String pAction, ICustomServiceDelegate pDelegate)
	{
		return register(UserService.class, pApplicationName, pAction, pDelegate);
	}
	
	/**
	 * Unregisters a service delegation (GET and/or POST).
	 * 
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pDelegate the delegation instance
	 * @param <D> the delegation type
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	public static <D extends ICustomServiceDelegate> boolean unregister(String pApplicationName, String pAction, D pDelegate)
	{
		return unregister(UserService.class, pApplicationName, pAction, pDelegate);
	}
	
	/**
	 * Unregisters a service delegation (GET and POST).
	 * 
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	public static boolean unregister(String pApplicationName, String pAction)
	{
		return unregister(UserService.class, pApplicationName, pAction);
	}	
	
	/**
	 * Unregisters a service delegation (GET and/or POST).
	 * 
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pClass the delegate class type to remove
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	public static boolean unregister(String pApplicationName, String pAction, Class<? extends ICustomServiceDelegate> pClass)
	{
		return unregister(UserService.class, pApplicationName, pAction, pClass);
	}
	
	/**
	 * Checks if a custom service delegate is already registered.
	 * 
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pClass the delegate type class to check
	 * @return <code>true</code> if a delegate instance is registered, <code>false</code> otherwise
	 */
	public static boolean isRegistered(String pApplicationName, String pAction, Class<? extends ICustomServiceDelegate> pClass)
	{
		return isRegistered(UserService.class, pApplicationName, pAction, pClass);
	}
	
}	// UserService
