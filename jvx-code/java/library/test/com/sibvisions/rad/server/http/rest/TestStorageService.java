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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rad.type.bean.Bean;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import com.sibvisions.util.type.BeanUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests {@link CallServerResource} functionality.
 * 
 * @author René Jahn
 */
public class TestStorageService extends BaseServiceTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a data request.
	 * 
	 * @param pLCOClass the class name of the life-cycle object
	 * @param pStorageName the name of the storage
	 * @param pQuery additional query/condition parameters
	 * @return the request
	 */
	protected ClientResource createDataRequest(String pLCOClass, String pStorageName, String pQuery)
	{
		String sQuery = "";
		
		if (pQuery != null)
		{
			sQuery = "?" + pQuery;
		}
		
		String sURL = getBaseURL() + pLCOClass + "/data/" + pStorageName + sQuery;
		System.out.println("request URL = " + sURL);
		
		ClientResource cres = new ClientResource(sURL);
		cres.setChallengeResponse(getChallengeResponse());
		
		return cres;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests get data request.
	 * 
	 * @throws Exception if get data fails
	 */
	@Test
	public void testFetch() throws Exception
	{
		ClientResource cres = createDataRequest("demo.special.Address", "address", null);
		
		Representation rep = cres.get();

		Object oResult = JSONUtil.getObject(rep);
		
		Assert.assertTrue(oResult instanceof List);
		
		List liResult = (List)oResult;
		
		Assert.assertFalse(liResult.isEmpty());
		
		Object oRecord = liResult.get(0);
		
		Assert.assertTrue(oRecord instanceof HashMap);
		
		HashMap hmpRecord = (HashMap)oRecord;
		
		Assert.assertTrue(hmpRecord.containsKey("ID"));
	}		
		
	/**
	 * Tests _condition query parameter.
	 * 
	 * @throws Exception if fetching with condition fails
	 */
    @Test
    public void testFetchWithCondition() throws Exception
    {
        ClientResource cres = createDataRequest("demo.special.Address", "address", "_condition=" + 
                                                CodecUtil.encodeURLParameter("{j:new Equals('ID', new java.math.BigDecimal(1)).or(new Equals('ID', 2))}"));
        
        Representation rep = cres.get();

        Object oResult = JSONUtil.getObject(rep);
        
        Assert.assertTrue(oResult instanceof List);
        
        List liResult = (List)oResult;
        
        Assert.assertEquals(2, liResult.size());
	}
    
	/**
	 * Tests _condition query parameter.
	 * 
	 * @throws Exception if fetching with condition fails
	 */
    @Test
    public void testFetchWithTimestamp() throws Exception
    {
    	ClientResource cres = createDataRequest("Company", "persons", null);
    	
        Representation rep = cres.get();

        Object oResult = JSONUtil.getObject(rep);
        
        Assert.assertTrue(oResult instanceof List);
        
        List liResult = (List)oResult;
        
        Assert.assertTrue(liResult.size() > 0);

    	cres = createDataRequest("Company", "persons/?gebdat=" + CodecUtil.encodeURLParameter((String)((Map)liResult.get(0)).get("GEBDAT")), null);

        rep = cres.get();

        oResult = JSONUtil.getObject(rep);
        
        Assert.assertTrue(oResult instanceof List);
        
        liResult = (List)oResult;
        
        Assert.assertEquals(1, liResult.size());
    }

	/**
	 * Tests _sort query parameter.
	 * 
	 * @throws Exception if fetching with sort definition fails
	 */
    @Test
    public void testFetchWithSortDefinition() throws Exception
    {
        ClientResource cres = createDataRequest("demo.special.Address", "address", "_sort=" + 
                                                CodecUtil.encodeURLParameter("{j:new SortDefinition(false, 'ID')}"));
        
        Representation rep = cres.get();

        Object oResult = JSONUtil.getObject(rep);
        
        Assert.assertTrue(oResult instanceof List);
        
        List liResult = (List)oResult;

        Assert.assertTrue(liResult.size() > 10);
        Assert.assertTrue(liResult.get(0) instanceof HashMap);
        
        for (int i = 0, curId = ((BigInteger)((HashMap)liResult.get(0)).get("ID")).intValue(), recId = -1; i < 10; i++)
        {
        	HashMap record = (HashMap)liResult.get(i);
        	
        	recId = ((BigInteger)record.get("ID")).intValue();

        	//makes no sense to check first record
        	if (i > 0)
        	{
        		Assert.assertTrue("Record ID: " + recId + " is not lower than the previous ID: " + curId, recId < curId);
        	}
        	
        	curId = recId;
        }
	}
    
    
	/**
	 * Tests delete multiple records.
	 * 
	 * @throws Exception if delete fails
	 */
	@Test
	public void testDelete() throws Exception
	{
		Series<Header> header = new Series(Header.class);
		//name is always converted to lowercase.... (awful)
		header.add("x-dryrun", "true");
		
		ClientResource cres = createDataRequest("demo.special.Address", "address", "stiege=");
		cres.getRequest().getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, header);
		
		Representation rep = cres.delete();
		
		Integer iCount = JSONUtil.getObject(rep, Integer.class);

		//The address table is created with random data. It is possible that different numbers of
		//records are deleted after the database is recreated.
		//It is possible that no record is deleted, but hopefully it does not happen and the random
		//number generator works :)
		if (iCount.intValue() <= 0)
		{
			Assert.fail("No records deleted!");
		}
	}
	
	/**
	 * Tests delete a single record.
	 * 
	 * @throws Exception if delete fails
	 */
	@Test
	public void testDeleteWithPK() throws Exception
	{
		Series<Header> header = new Series(Header.class);
		//name is always converted to lowercase.... (awful)
		header.add("x-dryrun", "true");
		
		ClientResource cres = createDataRequest("demo.special.Address", "address/1", null);
		cres.getRequest().getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, header);
		
		Representation rep = cres.delete();
		
		Integer iCount = JSONUtil.getObject(rep, Integer.class);

		Assert.assertEquals(1, iCount.intValue());
		
		//add an additional query condition
		cres = createDataRequest("demo.special.Address", "address/1", "stiege=");
		cres.getRequest().getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, header);
		
		try
		{
			rep = cres.delete();
			
			Assert.fail("Deleted invalid records!");
		}
		catch (Exception e)
		{
			Assert.assertEquals(Status.CLIENT_ERROR_NOT_FOUND, cres.getStatus());
		}
	}

	/**
	 * Test insert a new record.
	 * 
	 * @throws Exception if insert fails
	 */
	@Test
	public void testInsert() throws Exception
	{
		ClientResource cres = createDataRequest("Address", "address", null);
		
		Bean bnNew = new Bean();
		bnNew.put("POST_ID", Integer.valueOf(0));
		bnNew.put("STRA_ID", Integer.valueOf(0));
		bnNew.put("hausnummer", Integer.valueOf(9999));

		Representation rep = cres.post(bnNew, MediaType.APPLICATION_JSON);
		
		Bean bean = JSONUtil.getObject(rep, Bean.class);
		
		cres = createDataRequest("Address", "address/" + bean.get("ID"), null);

		cres.delete();
	}
	
	/**
	 * Test update a single record.
	 * 
	 * @throws Exception if update fails
	 */
	@Test
	public void testUpdate() throws Exception
	{
		Bean bnNew = new Bean();
		bnNew.put("ID", "1");
		bnNew.put("hausnummer", "0");
		bnNew.put("stiege", "0");
		bnNew.put("tuernummer", "0");
		bnNew.put("adre_id", null);
		
		ClientResource cres = createDataRequest("demo.special.Address", "address/1", null);

		//1) Get old record
		List<Object> liRecord = JSONUtil.getObject(cres.get(), List.class);
		
		Assert.assertTrue("Address with identifier 1 was not found!", !liRecord.isEmpty());
		
		Bean bnOld = new Bean();
		bnOld.putAll((HashMap)liRecord.get(0));
		
		//2) Update record
		Representation rep = cres.put(bnNew, MediaType.APPLICATION_JSON);
		
		Bean bnCurrent = JSONUtil.getObject(rep, Bean.class);
		
		Assert.assertEquals(((Number)bnCurrent.get("HAUSNUMMER")).intValue(), Integer.parseInt((String)bnNew.get("hausnummer")));
		Assert.assertEquals(((Number)bnCurrent.get("STIEGE")).intValue(), Integer.parseInt((String)bnNew.get("stiege")));
		Assert.assertEquals(((Number)bnCurrent.get("TUERNUMMER")).intValue(), Integer.parseInt((String)bnNew.get("tuernummer")));

		//3) Update with original values
		rep = cres.put(bnOld, MediaType.APPLICATION_JSON);

		bnCurrent = JSONUtil.getObject(rep, Bean.class);
		
		Assert.assertEquals(bnCurrent.get("HAUSNUMMER"), bnOld.get("HAUSNUMMER"));
		Assert.assertEquals(bnCurrent.get("STIEGE"), bnOld.get("STIEGE"));
		Assert.assertEquals(bnCurrent.get("TUERNUMMER"), bnOld.get("TUERNUMMER"));
		
		//-----------------------------------------------------------------
		// TEST date columns
		//-----------------------------------------------------------------
		
		//DATE
		cres = createDataRequest("Company", "persons/0", null);
		
		List<Object> liPersons = JSONUtil.getObject(cres.get(), List.class);
		
		Bean bnPerson = new Bean();
		bnPerson.putAll((HashMap)liPersons.get(0));
		
		bnPerson.put("GEBDAT", "1975-06-02T10:00:00.000+0000");
		
		//date-update
		cres.put(bnPerson, MediaType.APPLICATION_JSON);
		
		//1) Get record
		liRecord = JSONUtil.getObject(cres.get(), List.class);
		
		Assert.assertTrue("Person with identifier 0 was not found!", !liRecord.isEmpty());
		
		bnCurrent = new Bean();
		bnCurrent.putAll((HashMap)liRecord.get(0));	
		
		Assert.assertEquals("1975-06-01T23:00:00.000+0000", bnCurrent.get("GEBDAT"));
	}
	
	/**
	 * Tests an option request to retrieve the metadata.
	 * 
	 * @throws Exception if request fails
	 */
	@Test
	public void testOptions() throws Exception
	{
		ClientResource cres = createDataRequest("demo.special.Address", "address", null);
		
		Representation rep = cres.options();

		Object oResult = JSONUtil.getObject(rep);
		
		List<Object> liColumnNames = (List<Object>)BeanUtil.get(oResult, "columnNames");
		
		Assert.assertArrayEquals(new String[] {"ID", "POST_ID", "POST_PLZ", "STRA_ID", "STRA_NAME", "HAUSNUMMER", "STIEGE", "TUERNUMMER"}, 
				                 liColumnNames.toArray());
		
		
		cres = createDataRequest("demo.special.Address", "addressWithOptions", null);
		
		try
		{
			rep = cres.options();
			
			Assert.fail("Options are available but shouldn't!");
		}
		catch (ResourceException e)
		{
			Assert.assertEquals(404,  e.getStatus().getCode());
		}
	}
	
	/**
	 * Test getting the username from the server-side session.
	 * 
	 * @throws Exception if update fails
	 */
	@Test
	public void testGetSessionUserName() throws Exception
	{
		Bean bnNew = new Bean();
		bnNew.put("NAME", "What's the name?");
		
		ClientResource cres = createDataRequest("Session", "company/0", null);

		//1) Get old record
		List<Object> liCompany = JSONUtil.getObject(cres.get(), List.class);
		
		Bean bnOld = new Bean();
		bnOld.putAll((HashMap)liCompany.get(0));
		
		//2) Update record
		Representation rep = cres.put(bnNew, MediaType.APPLICATION_JSON);
		
		Bean bnCurrent = JSONUtil.getObject(rep, Bean.class);
		
		Assert.assertEquals((String)bnCurrent.get("NAME"), "rene");
	}	
	
	/**
	 * Test insert a new record with nullable column.
	 * 
	 * @throws Exception if insert fails
	 */
	@Test
	public void testInsertNullable() throws Exception
	{
		ClientResource cres = createDataRequest("StorageDataBookTest", "TESTDEFAULTS", null);

		Bean bnRecord = new Bean();
		bnRecord.put("ACTIVE", "N");
		bnRecord.put("DATETIMEVAL", null);

		Representation rep = cres.post(bnRecord, MediaType.APPLICATION_JSON);
		
		Bean bean = JSONUtil.getObject(rep, Bean.class);
	}
	
	/**
	 * Tests binary data.
	 * 
	 * @throws Exception if test fails
	 */
    @Test
    public void testBinary() throws Exception
    {
    	TestCallService tcs = new TestCallService();
    	tcs.setApplicationName(getApplicationName());
    	
    	tcs.testResetBinaryData();
    	
    	ClientResource cres = createDataRequest("Address", "binaryData", null);
    	
        Representation rep = cres.get();

        Object oResult = JSONUtil.getObject(rep);
        
        Assert.assertTrue(oResult instanceof List);
        Assert.assertEquals(1, ((List)oResult).size());
        
		Bean bnRecord = new Bean();
		bnRecord.put("ID", BigDecimal.valueOf(2));
		bnRecord.put("FILENAME", "simple_test.png");
		bnRecord.put("CONTENT", FileUtil.getContent(ResourceUtil.getResourceAsStream("/com/sibvisions/rad/application/images/frame.png")));

		rep = cres.post(bnRecord, MediaType.APPLICATION_JSON);

        rep = cres.get();

        oResult = JSONUtil.getObject(rep);

        Assert.assertTrue(oResult instanceof List);
        Assert.assertEquals(2, ((List)oResult).size());
    }
	
}	// TestStorageService
