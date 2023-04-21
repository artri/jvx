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
 */
package com.sibvisions.rad.server.http.rest.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

import org.restlet.Request;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBCredentials;
import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.http.rest.RESTAdapter;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.rad.server.security.DBSecurityManager;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>AdminService</code> is a special administrative service.
 * 
 * @author René Jahn
 */
public class AdminService extends AbstractCustomService
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Executes special admin calls with max. one simple parameter. This method only works
	 * if custom delegates are set.
	 * 
	 * @return the result
	 */
	@Get
	public Representation execute()
	{
		Request request = getRequest();
		
		info("ADMIN-URL: ", request.getResourceRef());
		
		if (((RESTAdapter)getApplication()).isAdminServiceAvailable())
		{
			Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_GET);

			ConcurrentMap<String, Object> cmpAttrib = request.getAttributes();

			String sAction = (String)cmpAttrib.get(RESTAdapter.PARAM_ADMIN_ACTION);
			String sParam = (String)cmpAttrib.get(RESTAdapter.PARAM_ADMIN_ACTION_PARAM);
			
			if (record != null)
			{
				record.setParameter(sAction, sParam);
			}
			
			try
			{
				LoggerFactory.getInstance(AdminService.class).debug(sAction);
				
	        	String sApplicationName = (String)getAttribute(RESTAdapter.PARAM_APP_NAME);
	        	
				//implicite application check
				ApplicationZone zone = Configuration.getApplicationZone(sApplicationName);
				
				LoggerFactory.getInstance(AdminService.class).debug(sAction);
				
				boolean bDefaultEnabled = ((RESTAdapter)getApplication()).isDefaultAdminServiceEnabled(sAction);
				
				if ("checkDB".equals(sAction))
				{
					if (bDefaultEnabled)
					{
						DBCredentials dbcred = DBSecurityManager.getCredentials(zone.getConfig());
	
						//no credentials -> no database access possible (should not happen, only if configuration is not finished)
						if (dbcred != null)
						{
							try
							{
								DBAccess dba = DBAccess.getDBAccess(dbcred);
								dba.open();
								
								setStatus(Status.SUCCESS_NO_CONTENT);
								
							    return toInternalRepresentation(null);
	
							}
							catch (Throwable th)
							{
								setStatus(Status.SERVER_ERROR_INTERNAL);
								
							    return null;
							}
						}
						else
						{
							setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
							
						    return null;
						}
					}
					else
					{
						debug("Default admin service '", sAction, "' is not enabled!");

						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						
						return null;
					}
				}
				else
				{
					if (((RESTAdapter)getApplication()).isAdminServiceEnabled(sAction))
					{
						Representation repResult = delegateGet(AdminService.class, sApplicationName, sAction, sParam);
						
						applySession(record);
						
						return repResult;
					}
					else
					{
						debug("Admin service '", sAction, "' is not enabled!");
	
						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						
						return null;
					}
				}
			}
			catch (Throwable th)
			{
				LoggerFactory.getInstance(AdminService.class).error(th);
				
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
		else
		{
			debug("Admin services are not available!");

			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			
			return null;
		}
	}
	
	/**
	 * Executes special admin calls.
	 * 
	 * @param pParameter the parameters
	 * @return the result
	 */
	@Post
	public Representation execute(Representation pParameter)
	{
		Request request = getRequest();
		
		info("ADMIN-URL-WITH-PARAMS: ", request.getResourceRef());
		
		if (((RESTAdapter)getApplication()).isAdminServiceAvailable())
		{
			Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_POST);

			ConcurrentMap<String, Object> cmpAttrib = request.getAttributes();
			
			String sAction = (String)cmpAttrib.get(RESTAdapter.PARAM_ADMIN_ACTION);

			try
			{
				String sParam = (String)cmpAttrib.get(RESTAdapter.PARAM_ADMIN_ACTION_PARAM);

				Map<String, Object> mpInput = getInput(pParameter);
				
				if (record != null)
				{
					record.setParameter(sAction, sParam, mpInput);
				}

				String sApplicationName = (String)getAttribute(RESTAdapter.PARAM_APP_NAME);

		    	//implicite application check
				Configuration.getApplicationZone(sApplicationName);
				
				LoggerFactory.getInstance(AdminService.class).debug(sAction);
				
				boolean bDefaultEnabled = ((RESTAdapter)getApplication()).isDefaultAdminServiceEnabled(sAction);
				
				if ("changePassword".equals(sAction))
				{
					if (bDefaultEnabled)
					{
						HashMap<String, Object> hmpParams = new HashMap<String, Object>();
		
						String sUserName = (String)mpInput.get("username");
						
						if (sUserName == null)
						{
							sUserName = sParam;
						}
						
						String sOldPassword = (String)mpInput.get("oldpassword");
						String sNewPassword = (String)mpInput.get("newpassword");
						
						if (sNewPassword != null)
						{
							hmpParams.put(IConnectionConstants.OLDPASSWORD, sOldPassword);
							
							if (!hmpParams.containsKey(IConnectionConstants.NEWPASSWORD))
							{
								hmpParams.put(IConnectionConstants.NEWPASSWORD, sNewPassword);
							}
						}
		
						ISession session = createSession(sApplicationName, sUserName, sOldPassword, hmpParams);
						
						if (record != null)
						{
							record.addIdentifier(session.getId());
						}
						
						setStatus(Status.SUCCESS_NO_CONTENT);
						
						return toInternalRepresentation(null);
					}
					else
					{
						debug("Default admin service '", sAction, "' is not enabled!");

						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						
						return null;
					}
				}
				else if ("testAuthentication".equals(sAction))
				{
					if (bDefaultEnabled)
					{
						String sUserName = (String)mpInput.get("username");
						
						if (sUserName == null)
						{
							sUserName = sParam;
						}
		
						ISession session = createSession(sApplicationName, sUserName, (String)mpInput.get("password"), null);
						
						if (record != null)
						{
							record.addIdentifier(session.getId());
						}
						
						setStatus(Status.SUCCESS_NO_CONTENT);
						
						return toInternalRepresentation(null);
					}
					else
					{
						debug("Default admin service '", sAction, "' is not enabled!");

						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						
						return null;
					}
				}
				else
				{
					if (((RESTAdapter)getApplication()).isAdminServiceEnabled(sAction))
					{
						Representation repResult = delegatePost(AdminService.class, sApplicationName, sAction, sParam, mpInput);
						
						applySession(record);
						
						return repResult;
					}
					else
					{
						debug("Admin service '", sAction, "' is not enabled!");

						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						
						return null;
					}
				}
			}
			catch (Throwable th)
			{
				LoggerFactory.getInstance(AdminService.class).error(th);			
				
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
		else
		{
			debug("Admin services are not available!");

			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			
			return null;
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
		return register(AdminService.class, pApplicationName, pAction, pDelegate);
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
		return unregister(AdminService.class, pApplicationName, pAction, pDelegate);
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
		return unregister(AdminService.class, pApplicationName, pAction);
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
		return unregister(AdminService.class, pApplicationName, pAction, pClass);
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
		return isRegistered(AdminService.class, pApplicationName, pAction, pClass);
	}
	
}	// AdminService
