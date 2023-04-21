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
 * 12.12.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import http.RestContextListener;


/**
 * Tests {@link CallServerResource} functionality.
 * 
 * @author René Jahn
 */
public class TestAdminService extends BaseServiceTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Posts an admin request.
	 * 
	 * @param pActionName the name of the action
	 * @param pParameter the parameter
	 * @return the response from the post
	 */
	protected ClientResource post(String pActionName, IBean pParameter)
	{
		ClientResource cres = new ClientResource(getBaseURL() + "_admin/" + pActionName);	
		//cres.setChallengeResponse(getChallengeResponse());

		try
		{
			cres.post(pParameter);
		}
		catch (ResourceException rse)
		{
			rse.printStackTrace();
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		
		return cres;
	}
	
	/**
	 * Sends an admin request.
	 *  
	 * @param pActionName the name of the application
	 * @return the response from the request
	 */
	protected ClientResource get(String pActionName)
	{
		ClientResource cres = new ClientResource(getBaseURL() + "_admin/" + pActionName);	
		//cres.setChallengeResponse(getChallengeResponse());

		try
		{
			cres.get();
		}
		catch (ResourceException rse)
		{
			rse.printStackTrace();
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		
		return cres;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests changing password.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testChangePassword() throws Exception
	{
		IBean bean = new Bean();
		bean.put("username", "rene");
		bean.put("oldpassword", "rene");
		bean.put("newpassword", "admin");
		
		try
		{
			ClientResource res = post("changePassword", bean);
			
			Assert.assertEquals(204, res.getStatus().getCode());
			
			//reset the password
			bean.put("oldpassword", "admin");
			bean.put("newpassword", "rene");
			
			res = post("changePassword", bean);
			
			Assert.assertEquals(204, res.getStatus().getCode());
		}
		catch (ResourceException re)
		{
			int iCode = re.getStatus().getCode();
			
			if (iCode == 401)
			{
				bean.put("oldpassword", "admin");
				bean.put("newpassword", "rene");
				
				ClientResource res = post("changePassword", bean);
				
				Assert.assertEquals(204, res.getStatus().getCode());
			}
			else
			{
				Assert.assertEquals(204, iCode);
			}
		}
	}
	
	/**
	 * Tests changing password.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testAuthenticate() throws Exception
	{
		IBean bean = new Bean();
		bean.put("password", "rene");

		ClientResource res = post("testAuthentication/rene", bean);

		bean.put("username", "rene");
		
		res = post("testAuthentication", bean);
		
		Assert.assertEquals(204, res.getStatus().getCode());

		setApplicationName("demo-wrong");
		
		res = post("testAuthentication", bean);
		
		Assert.assertEquals(400, res.getStatus().getCode());
	}
	
	/**
	 * Tests checking database.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testCheckDB() throws Exception
	{
		ClientResource res = get("checkDB");

		Assert.assertEquals(204, res.getStatus().getCode());
	}
	
	/**
	 * Tests custom admin service.
	 * 
	 * @throws Exception if test fails
	 * @see RestContextListener
	 */
	@Test
	public void testClearDB() throws Exception
	{
		setApplicationName("demo-wrong");
		
		IBean bean = new Bean();
		bean.put("username", "rene");
		bean.put("password", "rene");

		ClientResource res = post("doClearDB", bean);
		
		Assert.assertEquals(400, res.getStatus().getCode());
		
		setApplicationName("demo");
		
		res = post("doClearDB", bean);

		Assert.assertEquals(204, res.getStatus().getCode());
	}
		
}	// TestAdminService
