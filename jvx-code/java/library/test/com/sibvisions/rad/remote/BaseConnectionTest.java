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
 * 02.11.2008 - [JR] - added user validation tests (Valid_from, Valid_To, Inactive, Change_pwd)
 * 12.05.2009 - [JR] - added:
 *                     * testCallWithoutConnectionId
 *                     * testCallWithInvalidConnectionInfo
 * 04.10.2009 - [JR] - testChangePassword
 * 28.02.2013 - [JR] - #643: test added 
 * 12.07.2013 - [JR] - #728: test added
 * 04.04.2014 - [RZ] - #997: added testTicket997
 * 09.04.2014 - [RZ] - fixed testTicket997 so that it works with every connection 
 */
package com.sibvisions.rad.remote;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.rad.io.FileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.SubConnection;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;

import remote.net.ObjectSerializer;

/**
 * Tests the http implementation of <code>IConnection</code>.
 * 
 * @author René Jahn
 * @see IConnection
 */
public abstract class BaseConnectionTest implements ICallBackListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** number of calls for performance tests. */
	private static final int PERFORMANCE_CALLS = 100000;

	/** the call result (for accessing via thread). */
	private String sResult;
	
	/** the current number of threads. */
	private int iThreads = 0;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Apply the configuration for all tests.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		//important for code coverage, because the coverage uses a temp directory and not
		//the expected directory tree...
		System.setProperty(IPackageSetup.CONFIG_BASEDIR, new File("").getAbsolutePath());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void callBack(CallBackEvent ce)
	{
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Returns a new <code>AbstractSerializedConnection</code>.
	 *
	 * @param pSerializer the <code>ISerializer</code> implementation
	 * @return a new instance of <code>AbstractSerializedConnection</code>
	 * @throws Throwable if an error occurs during connection creation
	 * @see ISerializer
	 */
	protected abstract IConnection createConnection(ISerializer pSerializer) throws Throwable;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns a new <code>AbstractSerializedConnection</code>.
	 * 
	 * @return a new instance of <code>AbstractSerializedConnection</code>
	 * @throws Throwable if an error occurs during connection creation
	 */
	protected IConnection createConnection() throws Throwable
	{
		return createConnection(null);
	}
	
	/**
	 * Returns the current connection properties.
	 * 
	 * @return the connection properties
	 */
	protected ChangedHashtable<String, String> createProperties()
	{
		return new ChangedHashtable<String, String>();
	}

	/**
	 * Starts multiple threads and calls server methods.
	 */
	public void callServerMethodsConcurrently()
	{
		for (int i = 0; i < 10; i++)
		{
			Thread th = new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						callServerMethods();
					}
					catch (Throwable th)
					{
						th.printStackTrace();
					}
					finally
					{
						iThreads--;
					}
				}
			});
			
			th.start();
			
			iThreads++;
		}
		
		while (iThreads > 0)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				//nothing to be done
			}
		}
	}
	
	/**
	 * Test the performance of remote calls with one single and multiple requests.
	 * 
	 * @throws Throwable if the test fails
	 * @see {@link #PERFORMANCE_CALLS}
	 */
	protected void callServerMethods() throws Throwable
	{
		IConnection conDemo = createConnection();

		String[] sObjects;
		String[] sMethods;
		Object[][] oParams;

		Object[] oResult;
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		StringBuilder sbACL = new StringBuilder();
		
		String sServerACL;
		
		long lFirst;
		long lSecond;
		long lNow;
		

		sObjects = new String[BaseConnectionTest.PERFORMANCE_CALLS];
		sMethods = new String[BaseConnectionTest.PERFORMANCE_CALLS];
		oParams = new Object[BaseConnectionTest.PERFORMANCE_CALLS][];
		
		sObjects[0] = "RemoteFile";
		sMethods[0] = "clearACL";
		oParams[0]  = null;
		
		sbACL.append("[");
		
		for (int i = 1; i < BaseConnectionTest.PERFORMANCE_CALLS - 1; i++)
		{
			sObjects[i] = "RemoteFile";
			sMethods[i] = "addUserToACL";
			oParams[i]  = new String[] {"" + i};
			
			if (i > 1)
			{
				sbACL.append(", "); 
			}
			
			sbACL.append("" + i);
		}
		
		sbACL.append("]");			

		sObjects[BaseConnectionTest.PERFORMANCE_CALLS - 1] = "RemoteFile";
		sMethods[BaseConnectionTest.PERFORMANCE_CALLS - 1] = "getUserACL";
		oParams[BaseConnectionTest.PERFORMANCE_CALLS - 1]  = null;
		
		lNow = System.currentTimeMillis();
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		conDemo.open(coninfo);

		oResult = conDemo.call
		(
			coninfo,
			sObjects,
			sMethods,
			oParams,
			null
		);

		conDemo.close(coninfo);

		lFirst = System.currentTimeMillis() - lNow;
		
		sServerACL = (String)oResult[BaseConnectionTest.PERFORMANCE_CALLS - 1];

		Assert.assertEquals(sbACL.toString(), sServerACL);
		
		lNow = System.currentTimeMillis();
		
		conDemo.open(coninfo);

		conDemo.call
		(
			coninfo,
			new String[] {"RemoteFile"},
			new String[] {"clearACL"},
			null,
			null
		);		
		
		for (int i = 0; i < BaseConnectionTest.PERFORMANCE_CALLS - 2; i++)
		{
			oResult = conDemo.call
			(
				coninfo,
				new String[]  {"RemoteFile"},
				new String[]  {"addUserToACL"},
				new String[][] { {"" + (i + 1)} },
				null
			);		
		}
		
		oResult = conDemo.call
		(
			coninfo,
			new String[] {"RemoteFile"},
			new String[] {"getUserACL"},
			null,
			null
		);		

		conDemo.close(coninfo);
		
		lSecond = System.currentTimeMillis() - lNow;

		Assert.assertEquals(sbACL.toString(), oResult[0]);
		
		System.out.println("MULTI:  " + lFirst);
		System.out.println("SINGLE: " + lSecond);
	}
	
    /**
     * Asserts that the given {@link PropertyEvent} matches the given values.
     * 
     * @param pPropertyEvent the {@PropertyEvent} to assert
     * @param pPropertyName the name of the property
     * @param pOldValue the old value of the property
     * @param pNewValue the new value of the property
     */
    private void assertPropertyEvent(PropertyEvent pPropertyEvent, String pPropertyName, Object pOldValue, Object pNewValue)
    {
        Assert.assertEquals(pPropertyName, pPropertyEvent.getPropertyName());
        Assert.assertEquals(pOldValue, pPropertyEvent.getOldValue());
        Assert.assertEquals(pNewValue, pPropertyEvent.getNewValue());
    }
    
    /**
     * Tests basic server-side callback functionality.
     * 
     * @param pUseAlive <code>true</code> to use alive thread, <code>false</code> otherwise
     * @throws Throwable if test fails
     */
    protected void callBackResultTicket25(boolean pUseAlive) throws Throwable
    {
        IConnection con = createConnection();

        CallBackResultCollectionListener listener = new CallBackResultCollectionListener("INT_VALUE");
        CallBackResultCollectionListener listenerMaster = new CallBackResultCollectionListener("INT_VALUE");
        CallBackResultCollectionListener listenerSub = new CallBackResultCollectionListener("INT_VALUE");
        
        con.addCallBackResultListener(listener);
        
        MasterConnection appcon = new MasterConnection(con);
        appcon.addCallBackResultListener(listenerMaster);
        
        if (pUseAlive)
        {
            appcon.setAliveInterval(1001);
        }
        else
        {
            appcon.setAliveInterval(-1);
        }
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");
        appcon.open();

        try
        {
            SubConnection sub = appcon.createSubConnection("demo.StorageDataBookTest");
            sub.addCallBackResultListener(listenerSub);
            sub.open();

            int iApp = 20;
            int iSub = 5;
            
            appcon.callAction("startCallBackThread", Integer.valueOf(0), Integer.valueOf(iApp));
            sub.callAction("startCallBackThread", Integer.valueOf(4), Integer.valueOf(iSub));
            
            Thread.sleep(7000);
            
            Assert.assertEquals(iApp + iSub, listener.getList().size());
            Assert.assertEquals(iApp, listenerMaster.getList().size());
            Assert.assertEquals(iSub, listenerSub.getList().size());
            
            for (int i = 0; i < iApp; i++)
            {
                Assert.assertEquals(listenerMaster.getList().get(i), Integer.valueOf(i));
            }

            for (int i = 0, v = 4; i < iSub; i++)
            {
                Assert.assertEquals(listenerSub.getList().get(i), Integer.valueOf(i + v));
            }            
        }
        finally
        {
            CommonUtil.close(appcon);
        }    
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests an AbstractSerializedConnection with an invalid application name.
	 */
	@Test
	public void testInvalidApplicationName()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "undefined");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "username");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "password");
		
			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid application name!");
		}
		catch (Throwable th)
		{
			Assert.assertTrue("Invalid config: " + th.getMessage(), th.getMessage().startsWith("Invalid configuration file: "));
		}
	}

	/**
	 * Tests an AbstractSerializedConnection with an application name which is not fully configured.
	 */
	@Test
	public void testInvalidConfiguredApplicationName()
	{
		Throwable thError = null;

		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "invalid");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "username");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "password");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid configured application!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Error during instantiation of security manager", th.getMessage());
		}
	}	
	
	/**
	 * Tests an AbstractSerializedConnection with an invalid username.
	 */
	@Test
	public void testInvalidUsername()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "username");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "password");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid username!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("User 'username' was not found for application 'demo'", th.getMessage());
		}
	}

	/**
	 * Tests an AbstractSerializedConnection with an invalid password.
	 */
	@Test
	public void testInvalidPassword()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "password");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid password!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Invalid password for 'rene' and application 'demo'", th.getMessage());
		}
	}

	/**
	 * Tests create and destroy session.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testOpenClose() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
			
			con.call(coninfo, new String[] {null}, new String[] {"getData"}, null, null);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo);
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
	 * Tests opening a connection twice.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testOpenOpen() throws Throwable
	{
		Throwable thError = null;
		
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			try
			{
				con.open(coninfo);
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Opened twice!");

		}
		catch (Throwable th)
		{
			Assert.assertEquals("Session is already open!", th.getMessage());
		}		
	}

	/**
	 * Tests closing a session twice.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCloseClose() throws Throwable
	{
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();

		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
			con.close(coninfo);
			
			con.close(coninfo);
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Connection not open", th.getMessage());
		}			
	}
	
	/**
	 * Tests to change the password. This test fails after first run, because
	 * the password will be changed permanent!
	 * 
	 * @throws Throwable if the test fails
	 */
//	@Test
	public void testChangePassword() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
			
			con.setNewPassword(coninfo, "rene", "unknown");
			con.close(coninfo);
			
			Assert.assertNull("New password property is not UNSET!", con.getProperty(coninfo, IConnectionConstants.NEWPASSWORD));
			Assert.assertNull("Old password property is not UNSET!", con.getProperty(coninfo, IConnectionConstants.OLDPASSWORD));
			
			try
			{
				coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

				con.open(coninfo);
				
				Assert.fail("Password not changed!");
			}
			catch (Throwable th)
			{
				coninfo.getProperties().put(IConnectionConstants.PASSWORD, "unknown");

				if ("Invalid password for 'rene' and application 'demo'".equals(th.getMessage()))
				{
					con.open(coninfo);
					con.setNewPassword(coninfo, "unknown", "rene");
					
					Assert.assertNull("New password property is not UNSET!", con.getProperty(coninfo, IConnectionConstants.NEWPASSWORD));
					Assert.assertNull("Old password property is not UNSET!", con.getProperty(coninfo, IConnectionConstants.OLDPASSWORD));
				}
				else
				{
					throw th;
				}
			}

			try
			{
				//Test pwd change with invalid old password
				con.setNewPassword(coninfo, "invalid", "rene2");

				Assert.fail("Password changed!");
			}
			catch (Exception e)
			{
				Assert.assertEquals("Invalid password for 'rene' and application 'demo'", e.getMessage());
			}
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo);
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
	 * Tests a simple action call with a special serializer.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSerializerConnection() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection(new ObjectSerializer());
		
		ConnectionInfo coninfo = new ConnectionInfo();

		String sPath;
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
			
			sPath = (String)con.call(coninfo, new String[] {"RemoteFile"}, new String[] {"getAbsolutePath"}, null, null)[0];
			
			Assert.assertNotNull(sPath);
			Assert.assertTrue("Invalid path: " + sPath, sPath.substring(sPath.length() - 11).equals("session.xml"));
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo);
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
	 * Tests a global object call.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSessionCount() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		ConnectionInfo coninfo = new ConnectionInfo();
		
		Integer iCount;
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			conDemo.open(coninfo);
			conDemo.close(coninfo);
			conDemo.open(coninfo);
			conDemo.close(coninfo);
			conDemo.open(coninfo);
			conDemo.close(coninfo);
			conDemo.open(coninfo);
			conDemo.close(coninfo);
			conDemo.open(coninfo);
			
			iCount = (Integer)(conDemo.call
			(
				coninfo,
				new String[] {"monitoring"}, 
				new String[] {"getSessionCount"}, 
				null,
				null
			)[0]);
			
			Assert.assertEquals(Integer.valueOf(1), iCount);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				conDemo.close(coninfo);
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
	 * Tests to get a list of opened sessions.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetSessionIds() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		ArrayUtil<Object> alSessionIds;
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			conDemo.open(coninfo);
			
			alSessionIds = (ArrayUtil<Object>)conDemo.call
			(
				coninfo,
				new String[] {"monitoring"}, 
				new String[] {"getSessionIds"}, 
				null,
				null
			)[0];
			
			Assert.assertEquals(1, alSessionIds.size());
			Assert.assertEquals(coninfo.getConnectionId(), alSessionIds.get(0));
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				conDemo.close(coninfo);
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
	 * Tests a multiple call within a single request and without exception.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testMultipleCall() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		Object[] oResult;
		
		ConnectionInfo coninfo = new ConnectionInfo();
		

		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			conDemo.open(coninfo);
			
			oResult = conDemo.call
			(
				coninfo,
				new String[] {"RemoteFile", null, "RemoteFile", null, "RemoteFile", null},
				new String[] {"addUserToACL", "getData", "addUserToACL", "getData", "getUserACL", "getData"},
				new Object[][] {new String[] {"JRx"}, null, new String[] {"HM"}, null, null, null},
				null
			);
	
			Assert.assertEquals("[JRx, HM]", oResult[4]);
			Assert.assertEquals("getData()", oResult[5]);
			
			conDemo.close(coninfo);
			conDemo.open(coninfo);
			
			oResult = conDemo.call
			(
				coninfo,
				new String[] {"RemoteFile"},
				new String[] {"getUserACL"},
				null,
				null
			);		
			
			Assert.assertEquals("[]", oResult[0]);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				conDemo.close(coninfo);
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
	 * Tests a multiple call within a single call and with an exception
	 * after some calls.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testMultipleCallWithExceptionBetween() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		Object[] oResult;
		
		ConnectionInfo coninfo = new ConnectionInfo();
		

		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			conDemo.open(coninfo);
			
			try
			{
				oResult = conDemo.call
				(
					coninfo,
					new String[] {"RemoteFile", null, "RemoteFile", null, "RemoteFile", null},
					new String[] {"addUserToACL", "getData", "method1", "addUserToACL", "getData", "getUserACL"},
					new Object[][] {new String[] {"JRx"}, null, new String[] {"HM"}, null, null, null},
					null
				);
				
				Assert.fail("Unknown method called!");
			}
			catch (NoSuchMethodException nsme)
			{
				oResult = conDemo.call
				(
					coninfo,
					new String[] {"RemoteFile"},
					new String[] {"getUserACL"},
					null,
					null
				);		
				
				Assert.assertEquals("[JRx]", oResult[0]);
			}
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				conDemo.close(coninfo);
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
	 * Tests if the session expires.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSessionTimeout() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();

		ConnectionInfo coninfo = new ConnectionInfo();
		

		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			try
			{
				con.open(coninfo);
				con.setProperty(coninfo, IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(8));
				
				Thread.sleep(12000);
				
				//Test as string
				con.setProperty(coninfo, IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, "40");
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Connection is still open!");
		}
		catch (Throwable th)
		{
			Assert.assertTrue("Invalid expired exception: " + th.getClass(), th instanceof SessionExpiredException);
		}	
	}
	
	/**
	 * Test a callback without a session. 
	 * This is a test for a special exception from the server
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInvalidCallBack() throws Throwable
	{
		IConnection con = createConnection();
		
		ConnectionInfo	coninfo = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
			
			//CallBack is not possible with predefined objects/methods!
			con.call
			(
				coninfo,
				new String[] { IConnection.OBJ_SESSION },
				new String[] { IConnection.MET_SESSION_DESTROY }, 
				new Object[][] { {coninfo.getConnectionId()} }, 
				new ICallBackListener[] { this }
			);
			
			Assert.fail("Not allowed call-back done!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Call back is not allowed!", th.getMessage());
		}		
		finally
		{
		    try
		    {
		        con.close(coninfo);
		    }
		    catch (Throwable th)
		    {
		        //ignore
		    }
		}
	}
	
	/**
	 * Tests if the session controller destroyes the session automatically.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSessionController() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		Object oConIdDestroy = null;
		Object oResult;
		

		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			conDemo.open(coninfo);
			conDemo.setProperty(coninfo, IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(8));
			
			oConIdDestroy = coninfo.getConnectionId();
			
			//should be enough time for the Controller to kill the session
			Thread.sleep(12000);
			
			try
			{
				conDemo.close(coninfo);
				
				Assert.fail("Connection is still open!");
			}
			catch (Throwable th)
			{
				Assert.assertTrue("Invalid expired exception!", th instanceof SessionExpiredException);
			}
			
			conDemo.open(coninfo);

			oResult = conDemo.call
			(
				coninfo,
				new String[] {"monitoring"}, 
				new String[] {"getLastDestroyedSessionId"},
				null,
				null
			)[0];
			
			Assert.assertEquals(oConIdDestroy, oResult);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				conDemo.close(coninfo);
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
	 * Tests a simple call with null as ConnectionInfo parameter.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallWithoutConnectionInfo() throws Throwable
	{
		Throwable thError = null;
		
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo1 = new ConnectionInfo();
		
		
		coninfo1.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo1.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo1.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			try
			{
				con.open(coninfo1);
				
				con.call(null, new String[] {}, new String[] {}, null, null);
				
				Assert.fail("Call without ConnectionInfo succeeded!");
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo1);
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
		catch (Throwable th)
		{
			Assert.assertEquals("Invalid connection information: null", th.getMessage());
		}
	}
	
	/**
	 * Tests getProperty with null as ConnectionInfo parameter.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetPropertyWithoutConnectionInfo() throws Throwable
	{
		Throwable thError = null;
		
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo1 = new ConnectionInfo();
		
		
		coninfo1.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo1.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo1.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo1);
			
			Assert.assertEquals("rene", con.getProperty(coninfo1, "client.username"));
			Assert.assertNull(con.getProperty(null, "client.username"));
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo1);
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
	 * Tests calls with invalid parameters.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallWithInvalidParameters() throws Throwable
	{
		Throwable thError = null;
		
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo1 = new ConnectionInfo();
		

		coninfo1.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo1.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo1.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo1);
			
			try
			{
				con.call(coninfo1, new String[] {}, null, null, null);
			}
			catch (Throwable th)
			{
				Assert.assertEquals("No remote method specified!", th.getMessage());
			}
			
			try
			{
				con.call(coninfo1, new String[] {}, new String[] {"checkMail"}, null, null);
			}
			catch (Throwable th)
			{
				Assert.assertEquals("More or less objects than methods!", th.getMessage());
			}

			try
			{
				con.call(coninfo1, null, new String[] {"checkMail"}, new Object[][] {}, null);
			}
			catch (Throwable th)
			{
				Assert.assertEquals("More or less params than methods!", th.getMessage());
			}
			
			try
			{
				con.call(coninfo1, null, new String[] {"checkMail"}, new Object[][] { {null} }, new ICallBackListener[] { });
			}
			catch (Throwable th)
			{
				Assert.assertEquals("More or less callbacks than methods!", th.getMessage());
			}
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo1);
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
	 * Tests to create a sub connection from a master and sub connection.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSubConnection() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		ConnectionInfo coninfoSub = new ConnectionInfo();
		ConnectionInfo coninfoSubSub = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);
	
			coninfoSub.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.User");

			//Sub connection von einer Master connection erstellen
			con.openSub(coninfo, coninfoSub);

			coninfoSubSub.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.User");

			//Sub connection von einer sub connection erstellen (erstellt
			//eine sub connection von der Master connection)
			con.openSub(coninfoSub, coninfoSubSub);
			
			//Call an action, to verify that the life-cycle object will be created/used
			con.call(coninfoSub, null, new String[] {"checkMail"}, null, null);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo);
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
	 * Tests open a connection with change password flag.
	 */
	@Test
	public void testChangePasswordEnabled()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "change_pwd");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "change_pwd");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Change password not detected!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Please change your password", th.getMessage());
		}
	}
	
	/**
	 * Tests open a connection with an inactive user..
	 */
	@Test
	public void testInactiveUser()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "inactive");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "inactive");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Inactive flag not detected!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("User 'inactive' is inactive for application 'demo'", th.getMessage());
		}
	}
	
	/**
	 * Tests open a connection with an inactive user..
	 */
	@Test
	public void testValidFromUser()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "valid_from");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "valid_from");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid_from not detected!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("User 'valid_from' is expired for application 'demo'", th.getMessage());
		}
	}
	
	/**
	 * Tests open a connection with an inactive user..
	 */
	@Test
	public void testValidFromToUser()
	{
		Throwable thError = null;
		
		
		try
		{
			IConnection con = createConnection();
			
			ConnectionInfo coninfo = new ConnectionInfo();
			
			
			coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
			coninfo.getProperties().put(IConnectionConstants.USERNAME, "valid_from_to");
			coninfo.getProperties().put(IConnectionConstants.PASSWORD, "valid_from_to");

			try
			{
				con.open(coninfo);
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					con.close(coninfo);
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
			
			Assert.fail("Valid_from and Valid_to not detected!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("User 'valid_from_to' is expired for application 'demo'", th.getMessage());
		}
	}

	/**
	 * Tests a call with an invalid {@link ConnectionInfo}.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallWithInvalidConnectionInfo() throws Throwable
	{
		IConnection con = createConnection();

		try
		{
			con.call(null, new String[] {}, null, null, null);
			
			Assert.fail("Call without connectionInfo succeeded!");
		}
		catch (IllegalArgumentException iae)
		{
			Assert.assertEquals("Invalid connection information: null", iae.getMessage());
		}
	}
	
	/**
	 * Tests a call without a connection id.
	 *
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallWithoutConnectionId() throws Throwable
	{
		IConnection con = createConnection();

		try
		{
			con.call(new ConnectionInfo(), new String[] {}, null, null, null);
			
			Assert.fail("Call without connection id succeeded!");
		}
		catch (IllegalStateException ise)
		{
			Assert.assertEquals("The connection is not open!", ise.getMessage());
		}
	}

	/**
	 * Tests if property transfer works when it is not possible to open a connection. The server
	 * has no session in that case, but property transfer should work, e.g. the security manager
	 * sets a property and denies the authentication. In that case the client should be able to
	 * read the changed/new property.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testPropertyTransferWithSessionCreationException() throws Throwable
	{
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "propertytransfer");

		Assert.assertNull("property.new is already set, but should not!", coninfo.getProperties().get("property.new"));
		
		try
		{
			con.open(coninfo);
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Denied", th.getMessage());
			
			Assert.assertEquals("SENT", coninfo.getProperties().get("property.new"));
		}
		finally
		{
			try
			{
				con.close(coninfo);
			}
			catch (Throwable th)
			{
				//nothing to be done
			}
		}
	}

	/**
	 * Test ticket: https://oss.sibvisions.com/index.php?do=details&task_id=643&project=2. 
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBack643() throws Throwable
	{
		IConnection con = createConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);

			final Object[] oData = new Object[1];
			
			//CallBack is not possible with predefined objects/methods!
			con.call
			(
				coninfo,
				null,
				new String[] { "delayedGetData" }, 
				null, 
				new ICallBackListener[] { new ICallBackListener()
				{
					public void callBack(CallBackEvent pEvent)
					{
						try
						{
							oData[0] = pEvent.getObject();
						}
						catch (Throwable th)
						{
							Assert.fail(th.getMessage());
						}
					}
				} 
				}
			);
			
			Thread.sleep(6000);

			//important for non direct connections
			con.setAndCheckAlive(coninfo, null);
			
			Assert.assertEquals("delayedGetData()", (String)oData[0]);
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			
			Assert.assertEquals("Call back is not allowed!", th.getMessage());
		}			
		finally
		{
		    try
		    {
		        con.close(coninfo);
		    }
		    catch (Throwable th)
		    {
		        //ignore
		    }
		}
	}
	
	/**
	 * Tests if {@link IConnection#isCalling()} works.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testIsCalling728() throws Throwable
	{
		final IConnection con = createConnection();
		
		final List<Long> liNotCalling = new ArrayUtil<Long>();
		final List<Long> liCalling = new ArrayUtil<Long>();
		
		ConnectionInfo coninfo = new ConnectionInfo();

		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

		try
		{
			con.open(coninfo);

			Thread th = new Thread(new Runnable()
			{
				public void run()
				{
					while (sResult == null)
					{
						try
						{
							Thread.sleep(200);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						
						if (!con.isCalling())
						{
							liNotCalling.add(Long.valueOf(System.currentTimeMillis()));
						}
						else
						{
							liCalling.add(Long.valueOf(System.currentTimeMillis()));
						}
					}
				}
			});
			
			th.start();
			
			Thread.sleep(1000);
			
			long lStart = System.currentTimeMillis();
			
			sResult = (String)con.call(coninfo, new String[] {null}, new String[] {"delayedAction"}, new Object[][] { new Object[] {Long.valueOf(5000)} }, null)[0];
			
			long lEnd = System.currentTimeMillis();
			
			long lValue;
			
			for (int i = 0, anz = liNotCalling.size(); i < anz; i++)
			{
				lValue = liNotCalling.get(i).longValue();
				
				Assert.assertTrue("Wrong calling state!", lValue <= lStart);
			}

			for (int i = 0, anz = liCalling.size(); i < anz; i++)
			{
				lValue = liCalling.get(i).longValue();
				
				Assert.assertTrue("Wrong calling state: " + new Date(lValue) + " >= " + new Date(lStart) + " and " + new Date(lValue) + " <= " + new Date(lEnd), 
						          (lValue >= lStart && lValue <= lEnd));
			}
			
			Assert.assertEquals("finished", sResult);
		}
		finally
		{
			try
			{
				con.close(coninfo);
			}
			catch (Exception e)
			{
				//nothing to be done
			}
		}
	}
	
	/**
	 * Test for Ticket #997.
	 * Allow to attach a listener for changed properties.
	 * 
	 *  @throws Throwable if the test fails
	 */
	@Test
	public void testTicket997() throws Throwable
	{
		IConnection con = createConnection();
		
		PropertyChangedListener listener = new PropertyChangedListener();
		
		con.addPropertyChangedListener(listener);
				
		ConnectionInfo coninfo = new ConnectionInfo();
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");
		
		// Test before open
		con.setProperty(coninfo, "TESTPROP", "1");
		Assert.assertEquals(1, listener.getEvents().size());
		assertPropertyEvent(listener.getEvents().get(0), "TESTPROP", null, "1");
		
		con.open(coninfo);
		
		// Fetch the size after the opening of the connection.
		// Might very well be that there are a lot of changed properties.
		int sizeAfterOpen = listener.getEvents().size();
		
		// Test after the connection is open.
		con.setProperty(coninfo, "TESTPROP", "2");
		
		Assert.assertEquals(sizeAfterOpen + 1, listener.getEvents().size());
		assertPropertyEvent(listener.getEvents().get(sizeAfterOpen), "TESTPROP", "1", "2");
		
		// Let's test null
		con.setProperty(coninfo, "NULLPROP", null);
		Assert.assertEquals(sizeAfterOpen + 1, listener.getEvents().size());
		
		con.setProperty(coninfo, "NULLPROP", null);
		Assert.assertEquals(sizeAfterOpen + 1, listener.getEvents().size());
		
		con.setProperty(coninfo, "NULLPROP", "1");
		con.setProperty(coninfo, "NULLPROP", null);
		Assert.assertEquals(sizeAfterOpen + 3, listener.getEvents().size());
		assertPropertyEvent(listener.getEvents().get(sizeAfterOpen + 1), "NULLPROP", null, "1");
		assertPropertyEvent(listener.getEvents().get(sizeAfterOpen + 2), "NULLPROP", "1", null);
		
		// Test removal of listener
		con.removePropertyChangedListener(listener);
		
		con.setProperty(coninfo, "TESTPROP", "3");
		Assert.assertEquals(sizeAfterOpen + 3, listener.getEvents().size());
		
		con.close(coninfo);
	}
	
    /**
     * Tests getting content length.
     * 
     * @throws Throwable if test failed
     */
    @Test
    public void testUpload() throws Throwable
    {
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");
        appcon.open();

        try
        {
            for (int i = 0; i < 20; i++)
            {
                FileHandle fh = new FileHandle(ResourceUtil.getFileForClass("/com/sibvisions/DeliveryNoteTemplate.rtf"));
                
                RemoteFileHandle rfh = (RemoteFileHandle)appcon.callAction("createRTFReport", fh);
    
                Assert.assertTrue(rfh.getLength() > 0);
                Assert.assertTrue(fh.getLength() > rfh.getLength());
                
                /*
                File fiTemp = File.createTempFile("rtfReportResult", ".rtf");
                FileUtil.save(fiTemp, rfh.getInputStream());
                
                FileViewer.open(fiTemp);
                */
            }            
        }
        finally
        {
            CommonUtil.close(appcon);
        }
    } 
    
    /**
     * Test case for https://oss.sibvisions.com/index.php?do=details&task_id=1505. Checks if communication
     * is still valid after Server tried to send a not serializable object.
     * 
     * @throws Throwable if test case failed
     */
    @Test
    public void testWriteResponseError1505() throws Throwable
    {
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");
        appcon.open();

        try
        {
            appcon.callAction("getNotSerializableFile");

            if (!(con instanceof DirectServerConnection))
            {
                Assert.fail("File isn't serializable!");
            }
        }
        catch (IOException ioe)
        {
            Assert.assertEquals("There is no ITypeSerializer registered for Objects of instance java.io.File!", ioe.getMessage());
        }
        finally
        {
            appcon.close();
        }    
    }
    
    /**
     * Tests basic server-side callback functionality with alive thread.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testCallBackResultTicket25() throws Throwable
    {
        callBackResultTicket25(true);
    }
    
    /**
     * Tests callback result transfer if an exception occurs in the call which contains the result of the callback.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testMultipleCallWithExceptionAndCallBack() throws Throwable
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
            final ArrayUtil<String> liResult = new ArrayUtil<String>();
            
            appcon.callAction(new ICallBackListener()
            {
                public void callBack(CallBackEvent pEvent) throws Throwable
                {
                    liResult.add((String)pEvent.getObject());
                }
        
            }, "delayedAction", Integer.valueOf(1000));
            
            Thread.sleep(2000);
            
            try
            {
                appcon.callAction(new String[] {"getReport", "getReport", "immediateException", "getReport"});
                
                Assert.fail("Expected exception wasn't thrown!");
            }
            catch (Exception e)
            {
                Assert.assertEquals("IMMEDIATE_EXCEPTION", e.getMessage());
            }

            Assert.assertEquals(1, liResult.size());
            Assert.assertEquals(liResult.get(0), "finished");
        }
        catch (Throwable th)
        {
            thError = th;
            
            Assert.assertEquals("Remote communication failed!", th.getMessage());
            
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
     * Tests method call without parameter.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testNoParameter() throws Throwable
    {
        Throwable thError = null;
        
        
        IConnection con = createConnection();
        
        ConnectionInfo coninfo = new ConnectionInfo();

        coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
        coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        Object[] oResult;
        
        try
        {
            con.open(coninfo);
            
            try
            {
                oResult = con.call
                (
                    coninfo,
                    new String[] {"RemoteFile"},
                    new String[] {"addUserToACL"},
                    null,
                    null
                );
                
                Assert.fail("Invalid method call!");
            }
            catch (NoSuchMethodException nsme)
            {
                //expected
            }
            
            oResult = con.call
            (
                coninfo,
                new String[] {"RemoteFile"},
                new String[] {"addUserToACL"},
                new Object[][] {null},
                null
            );
    
            oResult = con.call
            (
                coninfo,
                new String[] {"RemoteFile"},
                new String[] {"getUserACL"},
                null,
                null
            );

            Assert.assertEquals("[null]", oResult[0]);
        }
        catch (Throwable th)
        {
            thError = th;
        }
        finally
        {
            try
            {
                con.close(coninfo);
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
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * An {@link IConnectionPropertyChangedListener} implementation that does record
	 * all events in a list.
	 * 
	 * @author Robert Zenz
	 */
	private static final class PropertyChangedListener implements IConnectionPropertyChangedListener
	{
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the list that holds all events. */
		private ArrayUtil<PropertyEvent> events = new ArrayUtil<PropertyEvent>();
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets called if a property changes.
		 * 
		 * @param pEvent the {@link PropertyEvent}
		 */
		public void propertyChanged(PropertyEvent pEvent)
		{
			events.add(pEvent);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets all events as list.
		 * 
		 * @return the events as list.
		 */
		public List<PropertyEvent> getEvents()
		{
			return events;
		}
		
	}	// PropertyChangedListener
	
}	// BaseConnectionTest
