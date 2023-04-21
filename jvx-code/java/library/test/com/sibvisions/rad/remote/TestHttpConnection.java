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
 * 23.12.2011 - [JR] - testBug482: closed connection to fix test cases
 */
package com.sibvisions.rad.remote;

import java.net.MalformedURLException;
import java.util.Properties;

import javax.rad.model.MetaDataCacheOption;
import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.SubConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.util.type.CommonUtil;

/**
 * Tests the http implementation of <code>IConnection</code>.
 * 
 * @author René Jahn
 * @see IConnection
 */
public class TestHttpConnection extends BaseConnectionTest
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IConnection createConnection(ISerializer pSerializer) throws Throwable
	{
	    return createHttpConnection();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>HttpConnection</code>.
	 * 
	 * @return the connection
	 * @throws Throwable if instance creation failed
	 */
	public static HttpConnection createHttpConnection() throws Throwable
	{
        // see build.xml
        String sUrl = System.getProperty("com.sibvisions.rad.remote.TestHttpConnection");
        
        if (sUrl == null)
        {
        	if (LauncherUtil.isMacOS())
        	{
        		sUrl = "http://localhost:8081/JVx.Server/services/Server";
        	}
        	else
        	{
        		sUrl = "http://localhost/JVx.Server/services/Server";
        	}
        		
        }

        HttpConnection con = new HttpConnection(sUrl);
        con.setDownloadURL(sUrl.replace("/services/Server", "/services/Download"));
        con.setUploadURL(sUrl.replace("/services/Server", "/services/Upload"));
        con.setRetryDuringOpen(false);
        con.setRetryDuringClose(false);
        
        return con;
	    
	}
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the constructor of {@link HttpConnection} with {@link Properties} as
	 * parameter.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testPropertiesConstructor() throws Exception
	{
		Properties prop = new Properties();
		prop.setProperty(HttpConnection.PROP_SERIALIZER, "asd");
		prop.setProperty(HttpConnection.PROP_SERVICE, "http://www.123.com");
		
		//--------------------------------------------------
		// Ungültiger Serializer
		//--------------------------------------------------
		
		try
		{
			new HttpConnection(prop);
			
			Assert.fail("Invalid serializer!");
		}
		catch (ClassNotFoundException cnfe)
		{
			//der Serializer kann nicht instanziert werden!
		}
			
		//--------------------------------------------------
		// Ungültiger URL
		//--------------------------------------------------
		
		prop.remove(HttpConnection.PROP_SERIALIZER);
		prop.setProperty(HttpConnection.PROP_SERVICE, "ole:www.123.com");

		try
		{
			new HttpConnection(prop);
			
			Assert.fail("Invalid servlet URL!");
		}
		catch (MalformedURLException mfue)
		{
			//untültiger URL
		}
		
		prop.remove(HttpConnection.PROP_SERVICE);
		
		try
		{
			new HttpConnection(prop);
			
			Assert.fail("Invalid servlet URL!");
		}
		catch (MalformedURLException mfue)
		{
			//keine URL
		}

		//--------------------------------------------------
		// Alles gültig
		//--------------------------------------------------
		
		prop.setProperty(HttpConnection.PROP_SERIALIZER, "com.sibvisions.rad.remote.ByteSerializer");
		prop.setProperty(HttpConnection.PROP_SERVICE, "http://www.123.com/services/Server");
		
		new HttpConnection(prop);
	}
	
	/**
	 * Tests the initialization of ssl.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSslInit() throws Exception
	{
		new HttpConnection("https://www.123.com/services/Server");
	}

	/**
	 * Tests the number of result objects for a remote call.
	 * 
	 * @throws Throwable if remote call fails
	 */
	@Test
	public void testBug482() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");

		appcon.open();
		
		Throwable thError = null;

		try
		{
			//set a "lazy" property
			appcon.setProperty(IConnectionConstants.METADATA_CACHEOPTION, MetaDataCacheOption.Default.toString());
			
			//this calls sends the 2 action calls and the above property!
			Object[] oCalls = appcon.callAction(new String[] {"getData", "getData"});
			
			//the result should not contain the property!
			Assert.assertEquals(2, oCalls.length);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appcon.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
			if (thError != null)
			{
				throw thError;
			}
		}
	}
	
	/**
	 * Tests feature request #29.
	 * 
	 * @throws Throwable if test fails
	 * @see https://oss.sibvisions.com/index.php?do=details&task_id=29
	 */
	@Test
	public void testStackTraceFeature29() throws Throwable
	{
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");

        appcon.open();
        
        Throwable thError = null;
        
        try
        {
            appcon.callAction("immediateException");
        }
        catch (Throwable th)
        {
            thError = th;
            
            Assert.assertEquals("IMMEDIATE_EXCEPTION", th.getMessage());
            
            StackTraceElement[] ste = th.getStackTrace();
            
            boolean bFound = false;
            
            for (int i = 0; i < ste.length && !bFound; i++)
            {
                if (ste[i].getClassName().startsWith(".........."))
                {
                    bFound = true;
                }
            }
            
            Assert.assertTrue("Client/Server separator wasn't found!", bFound);
            
            thError = null;
        }
        finally
        {
            try
            {
                appcon.close();
            }
            catch (Throwable th)
            {
                if (thError == null)
                {
                    thError = th;
                }
            }
            
            if (thError != null)
            {
                throw thError;
            }
        }
	}

    /**
     * Tests the number of result objects for a remote call.
     * 
     * @throws Throwable if remote call fails
     */
    @Test
    public void testHttp500Bug1842() throws Throwable
    {
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");

        appcon.open();
        
        Throwable thError = null;

        try
        {
            appcon.callAction("delayedException", Long.valueOf(120000L));
        }
        catch (Throwable th)
        {
            Assert.assertEquals("The statement execution took too long!", th.getMessage());
        }
        finally
        {
            try
            {
                appcon.close();
            }
            catch (Throwable th)
            {
                if (thError == null)
                {
                    thError = th;
                }
            }
            
            if (thError != null)
            {
                throw thError;
            }
        }
    }
    
    /**
     * Tests <a href="https://oss.sibvisions.com/index.php?do=details&task_id=2023">https://oss.sibvisions.com/index.php?do=details&task_id=2023</a>.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testSessionExpiredFR2023() throws Throwable
    {
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");

        appcon.open();
        
        ConnectionInfo cinf = new ConnectionInfo();
        cinf.setConnectionId(appcon.getConnectionId());
        
        //Close the connection -> forces Session Expired Exception
        con.close(cinf);
        
        Object oId = appcon.getConnectionId();
        
        try
        {
        	try
        	{
                cinf.setConnectionId(appcon.getConnectionId());
                
                con.close(cinf);
        		
        		Assert.fail("Session isn't expired!");
        	}
        	catch (SessionExpiredException see)
        	{
        		see.printStackTrace();
        		//perfect
        	}
        	
        	try
        	{
                cinf.setConnectionId(appcon.getConnectionId());

                con.close(cinf);

        		Assert.fail("Session isn't expired!");
        	}
        	catch (SessionExpiredException see)
        	{
        		//perfect
        	}

            cinf.setConnectionId(appcon.getConnectionId());

            con.close(cinf);
        	
    		Assert.fail("Session isn't expired!");
        }
        catch (SessionExpiredException see)
        {
        	//perfect
        }
        finally
        {
        	CommonUtil.close(appcon);
        }
    }
    
    /**
     * Tests long polling http connection with connection timeouts and retry count. 
     * 
     * @throws Throwable
     */
    @Test
    public void testBug2855() throws Throwable
    {
		HttpConnection con = createHttpConnection();
		con.setRetryCount(20);
		con.setConnectionTimeout(4000);
		
		MasterConnection appcon = new MasterConnection(con);
		appcon.setAliveInterval(1001);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");

		appcon.open();
		
		SubConnection scon = appcon.createSubConnection("demo.StorageDataBookTest");
		scon.open();
		
		Throwable thError = null;

		try
		{
			String sResult = (String)scon.callAction("delayedAction", Integer.valueOf(65000));
			
			Assert.assertEquals("finished", sResult);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appcon.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
			if (thError != null)
			{
				throw thError;
			}
		}
    }
	
}	// TestHttpConnection
