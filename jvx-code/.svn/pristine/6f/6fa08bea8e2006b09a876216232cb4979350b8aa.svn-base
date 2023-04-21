package com.sibvisions.jvx;

import java.util.HashMap;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.resource.ClientResource;

import com.sibvisions.rad.server.http.rest.BaseServiceTest;
import com.sibvisions.rad.server.http.rest.JSONUtil;

public class TestCustomRESTServices extends BaseServiceTest
{
	@Test
	public void testGet() throws Exception
	{
		ClientResource cres = new ClientResource(getBaseURL() + "uzone/healthCheck");	
		cres.get();
		
		HashMap<String, Object> hmpResult = (HashMap<String, Object>)JSONUtil.getObject(cres.getResponse().getEntity());

		Assert.assertEquals(200, cres.getStatus().getCode());
		Assert.assertEquals("SUCCESS", hmpResult.get("code"));
		Assert.assertEquals("GET is working!", hmpResult.get("message"));
		Assert.assertEquals(2, hmpResult.size());
	}

	@Test
	public void testPost() throws Exception
	{
		IBean bean = new Bean();
		bean.put("username", "@john.doe");
		bean.put("firstName", "John");
		bean.put("lastName", "Doe");

		ClientResource cres = new ClientResource(getBaseURL() + "uzone/healthCheck");	
		cres.post(bean);
		
		HashMap<String, Object> hmpResult = (HashMap<String, Object>)JSONUtil.getObject(cres.getResponse().getEntity());

		Assert.assertEquals(200, cres.getStatus().getCode());
		Assert.assertEquals("SUCCESS", hmpResult.get("code"));
		Assert.assertEquals("POST is working!", hmpResult.get("message"));
		Assert.assertEquals(5, hmpResult.size());
		Assert.assertEquals(bean.get("username"), hmpResult.get("username"));
	}
	
}
