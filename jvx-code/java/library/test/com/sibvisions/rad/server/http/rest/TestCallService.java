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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.sibvisions.util.FileViewer;
import com.sibvisions.util.type.BeanUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.HttpUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests {@link CallServerResource} functionality.
 * 
 * @author René Jahn
 */
public class TestCallService extends BaseServiceTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a call request.
	 * 
	 * @param pLCOClass the class name of the life-cycle object
	 * @param pObjectName the name of the object or null for a standard action call
	 * @param pActionName the name of the action
	 * @return the request
	 */
	protected ClientResource createCallRequest(String pLCOClass, String pObjectName, String pActionName)
	{
		ClientResource cres;
		
		String sURL;
		
		if (pObjectName == null)
		{
			sURL = getBaseURL() + pLCOClass + "/action/" + pActionName;
		}
		else
		{
			sURL = getBaseURL() + pLCOClass + "/object/" + pObjectName + "/" + pActionName;
		}
		
		System.out.println("request URL = " + sURL);

		cres = new ClientResource(sURL);	
		cres.setChallengeResponse(getChallengeResponse());
		
		return cres;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests an action call without parameters, as get request.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testActionWithoutParameters() throws Exception
	{
		ClientResource cres = createCallRequest("demo.special.Address", null, "getReport");

		Assert.assertEquals("**************************************************** This is a report call", JSONUtil.getObject(cres.get()));
	}
	
	/**
	 * Tests an object call without parameters, as get request.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testObjectCallWithoutParameters() throws Exception
	{
		ClientResource cres = createCallRequest("demo.special.Address", "address.metaData", "getColumnNames");
		
		Assert.assertArrayEquals(new String[] {"ID", "POST_ID", "POST_PLZ", "STRA_ID", "STRA_NAME", 
				                               "HAUSNUMMER", "STIEGE", "TUERNUMMER"},
				                 ((List)JSONUtil.getObject(cres.get())).toArray());		
	}

	/**
	 * Tests an action call with parameters, as post and put request.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testActionWithParameters() throws Exception
	{
		testActionWithParameters("getInput");
		testActionWithParameters("getInputWithArray");
	}
	
	/**
	 * Tests an action call with parameters, as post and put request.
	 * 
	 * @param pMethod the action method to use
	 * @throws Exception if request fails
	 */
	private void testActionWithParameters(String pMethod) throws Exception
	{
		ClientResource cres = createCallRequest("demo.special.Address", null, pMethod);

		Object[] obj = new Object[] { BigDecimal.valueOf(123) };
		Integer iValue = Integer.valueOf(1);
		
		//POST request with parameters

		Object[] oResult = JSONUtil.getObject(cres.post(new Object[] {obj, iValue}, MediaType.APPLICATION_JSON), Object[].class);
		
		Assert.assertArrayEquals(new Object[] {BigInteger.valueOf(123), BigInteger.valueOf(1)}, oResult);
		
		//PUT request with parameters
		
		oResult = JSONUtil.getObject(cres.put(new Object[] {obj, iValue}, MediaType.APPLICATION_JSON), Object[].class);
		
		Assert.assertArrayEquals(new Object[] {BigInteger.valueOf(123), BigInteger.valueOf(1)}, oResult);
		
		//POST request without parameters
		
		cres = createCallRequest("demo.special.Address", null, "getReport");
		
		Assert.assertEquals("**************************************************** This is a report call", JSONUtil.getObject(cres.post(null)));
	}
	
	/**
	 * Tests an action call with parameters, as post and put request.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testObjectWithParameters() throws Exception
	{
		ClientResource cres = createCallRequest("demo.special.Address", "address", "getMetaData");

		//POST request with parameters

		Object oResult = JSONUtil.getObject(cres.post(new Object[] {"demo.special.Address", "address"}, MediaType.APPLICATION_JSON));
		
		List<Object> liColumnNames = (List<Object>)BeanUtil.get(oResult, "columnNames");
		
		Assert.assertArrayEquals(new String[] {"ID", "POST_ID", "POST_PLZ", "STRA_ID", "STRA_NAME", "HAUSNUMMER", "STIEGE", "TUERNUMMER"}, 
				                 liColumnNames.toArray());
	}	
	
	/**
	 * Tests file download.
	 * 
	 * @throws Exception if download fails
	 */
	@Test
	public void testDownload() throws Exception
	{
	    ClientResource cres = createCallRequest("demo.special.Address", null, "getRemoteFileHandle");

	    Representation rep = cres.get();

	    Disposition disp = rep.getDisposition();
	    
	    Assert.assertNotNull(disp);
	    Assert.assertEquals(disp.getFilename(), "jndi_config.xml");
	    
	    Assert.assertArrayEquals(FileUtil.getContent(ResourceUtil.getResourceAsStream("/jndi_config.xml")), FileUtil.getContent(rep.getStream())); 
	}
	
	/**
	 * Tests getting a report with a simple HTTP get request and custom parameters (condition, sort).
	 *  
	 * @throws Exception if creating report fails
	 */
	@Test
	public void testGetReport() throws Exception
	{
	    HashMap<String, String> hmpProperties = new HashMap<String, String>();
	    //rene:rene (see https://www.base64encode.org/)
	    hmpProperties.put("Authorization", "Basic cmVuZTpyZW5l");
	    
	    String sURL = getBaseURL() + "Address/object/adrData/createCSV";

	    System.out.println(sURL);
	    
	    String sSort = "{j:new SortDefinition(new String[] {'HAUSNUMMER', 'STIEGE'}, new boolean[] {true, false})}";
	    String sCondition = "{j:new LessEquals('HAUSNUMMER', 10)}";
	    
	    File fiTemp = File.createTempFile("report", ".csv");
	    
	    FileUtil.save(fiTemp, FileUtil.getContent(HttpUtil.get(sURL, new ByteArrayInputStream(("[\"test.rtf\", null, null, \"" + 
	                                                                                           sCondition + "\", \"" + 
	                                                                                           sSort + "\"]").getBytes("UTF-8")), 
                                                               hmpProperties)));
	    
	    try
	    {
	        FileViewer.open(fiTemp);
	    }
	    catch (Exception e)
	    {
	        //e.g. CI
	        System.out.println(fiTemp.getAbsolutePath());
	    }
	}
	
	/**
	 * Tests getting the username from the server-side session.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSessionUserName() throws Exception
	{
		ClientResource cres = createCallRequest("Session", null, "getSessionUserName");
		
		Object o = JSONUtil.getObject(cres.get(MediaType.APPLICATION_JSON));
		
		Assert.assertEquals("rene", o);
	}
	
	/**
	 * Tests resetting binary data.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testResetBinaryData() throws Exception
	{
		ClientResource cres = createCallRequest("Session", null, "resetBinaryData");
		
		cres.get(MediaType.APPLICATION_JSON);
	}
	
}	// TestCallService
