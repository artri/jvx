/*
 * Copyright 2013 SIB Visions GmbH
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
 * 07.05.2013 - [JR] - creation
 */
package com.sibvisions.rad.server.http;

import javax.rad.io.RemoteFileHandle;
import javax.rad.remote.IConnection;
import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.HttpUtil;

/**
 * Tests the functionality of {@link DownloadServlet}.
 * 
 * @author René Jahn
 */
public class TestDownloadServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of {@link HttpConnection} for JVx server.
	 * see https://code.google.com/p/chromium/issues/detail?id=100011
	 * 
	 * @return the connection
	 * @throws Exception if connection creation fails
	 */
	protected IConnection createConnection() throws Exception
	{
	    String sUrl = getURL();
		
		HttpConnection con = new HttpConnection(sUrl);
		con.setDownloadURL(sUrl.replace("/services/Server", "/services/Download"));
        con.setUploadURL(sUrl.replace("/services/Server", "/services/Upload"));
		
		return con;
	}
	
	/**
	 * Gets the Server service URL.
	 * 
	 * @return the URL
	 */
	private String getURL()
	{
        // see build.xml
        String sUrl = System.getProperty("com.sibvisions.rad.remote.TestHttpConnection");
        
        if (sUrl == null)
        {
            sUrl = "http://localhost/JVx.Server/services/Server";
        }
        
        return sUrl;
	}
	
	/**
	 * Gets the HTTP status code of the GET request for the given URL.
	 *   
	 * @param pURL the URL
	 * @return the status code
	 */
	private int get(String pURL)
	{
	    try
	    {
	        HttpUtil.get(pURL);
	        
	        return 200;
	    }
	    catch (Exception ex)
	    {
	        int iPos = ex.getMessage().indexOf("response code: ");

	        if (iPos > 0)
	        {
	            return Integer.parseInt(ex.getMessage().substring(iPos + 15, iPos + 18));
	        }
	        
	        ex.printStackTrace();
	        
	        return 404;
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests downloading a file that contains commas in the filename.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testBug659() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setLifeCycleName("demo.special.Bug659");
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();
		
		RemoteFileHandle rfh = (RemoteFileHandle)appcon.callAction("getFile");
		
		Assert.assertEquals("My_application, and,some,other,data.txt", rfh.getFileName());
		
		Assert.assertEquals("Welcome JVx!", new String(FileUtil.getContent(rfh.getInputStream())));
	}
	
	/**
	 * Tests downloading "any" file.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testBug1605() throws Throwable
	{
	    String sURL = getURL();
	    
	    sURL = sURL.replace("/Server", "/Download");
	    
	    Assert.assertEquals(403, get(sURL + "?RESOURCE=../../test.jpg"));
	    Assert.assertEquals(403, get(sURL + "?RESOURCE=C:/test/../../../../../../../../../rad/apps/demo/config.xml"));
	}
	
}	// TestDownloadServlet
