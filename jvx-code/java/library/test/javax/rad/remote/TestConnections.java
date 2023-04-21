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
 * 12.05.2009 - [JR] - added:
 *                     * testCreateSubWhenNotOpen
 *                     * testMasterCallWhenNotOpen
 *                     * testSubCallWhenNotOpen
 * 15.02.2010 - [JR] - testNonGenerigBeanObjectCall implemented 
 * 07.06.2010 - [JR] - #49: security test case       
 * 28.02.2013 - [JR] - #643: test cases added             
 */
package javax.rad.remote;

import java.io.File;
import java.util.List;

import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.CallErrorEvent;
import javax.rad.remote.event.CallEvent;
import javax.rad.remote.event.ConnectionEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.remote.event.IConnectionListener;
import javax.rad.remote.event.IConnectionPropertyChangedListener;
import javax.rad.remote.event.PropertyEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.event.IExceptionListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

import remote.net.VMConnection;

/**
 * The <code>TestConnections</code> class tests the behaviour of
 * {@lin MasterConnection} and {@link SubConnection}.
 * 
 * @author René Jahn
 * @see MasterConnection
 * @see SubConnection
 */
public class TestConnections implements ICallBackListener,
                                        IConnectionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** undefined callback type. */
	private static final int CALLBACK_UNDEFINED = -1;
	
	/** the callback call didn't throw an exception. */
	private static final int CALLBACK_OK = 0;
	
	/** the callback call throwed an exception. */
	private static final int CALLBACK_ERROR = 1;

	
	/** type of the callback result .*/
	private int iCallBackType = TestConnections.CALLBACK_UNDEFINED;
	
	/** the callback return value. */
	private Object oCallBack = null;
	
	/** the events from the IConnectionListener. */
	private ArrayUtil<String> auCallErrorList;
	
	/** dummy info for testing CallBackEvent methods. */
	private String sCallBackInfo = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Apply the configuration for all tests.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		//Important for coverage tool, because the coverage toole uses another classes directory
		//as the directory configured for eclipse
		System.setProperty(IPackageSetup.CONFIG_BASEDIR, new File("").getAbsolutePath());
	}

	/**
	 * reset callback results.
	 */
	@Before
	public void beforeTest()
	{
		iCallBackType = TestConnections.CALLBACK_UNDEFINED;
		oCallBack     = null;
		
		sCallBackInfo = null;
		
		auCallErrorList = new ArrayUtil<String>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//ICallBackListener
	
	/**
	 * {@inheritDoc}
	 */
	public void callBack(CallBackEvent pEvent)
	{
		try
		{
			oCallBack = pEvent.getObject();
			
			iCallBackType = CALLBACK_OK;
		}
		catch (Throwable th)
		{
			oCallBack = th;
			
			iCallBackType = CALLBACK_ERROR;
		}

		//Simple tests for CallBackEvent
		sCallBackInfo = pEvent.getObjectName()   + " - " +
						pEvent.getMethodName()   + " - " +
						pEvent.getRequestTime()  + " - " + 
						pEvent.getResponseTime() + " - ";
	}
	
	//IConnectionListener
	
	/**
	 * {@inheritDoc}
	 */
	public void callError(CallErrorEvent pEvent)
	{
System.out.println(pEvent.getCause());

		AbstractConnection con = pEvent.getConnection();
		
		String sText;
		
		String sClass;
		
		if (pEvent.getCause() instanceof SessionCancelException)
		{
		    sClass = SessionExpiredException.class.getName();
		}
		else
		{
		    sClass = pEvent.getCause().getClass().getName();
		}
		
		
		if (con instanceof SubConnection)
		{
			sText = "WS " + sClass;
		}
		else
		{
			sText = "APP " + sClass;
		}

		if (!auCallErrorList.contains(sText))
		{
			auCallErrorList.add(sText);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void connectionOpened(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionReOpened(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionClosed(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionCalled(CallEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectCalled(CallEvent pEvent)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void propertyChanged(PropertyEvent pEvent)
	{
	}

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
		return new VMConnection();
	}
	
	/**
	 * Checks if the current connection listener result matches a list
	 * of expected values.
	 * 
	 * @param pExpected the list of expected values
	 */
	public void checkConnectionListenerResult(String[] pExpected)
	{
		Assert.assertEquals("The number of results and the expected number of values doesn't match!", pExpected.length, auCallErrorList.size());
		
		for (int i = 0, anz = auCallErrorList.size(); i < anz; i++)
		{
			if (!(auCallErrorList.get(i).startsWith(pExpected[i])))
			{
				Assert.fail("Invalid listener result [" + i + "] " + auCallErrorList.get(i) + " != " + pExpected[i]);
			}
		}
	}
	
	/**
	 * Callback listener method for #644.
	 * 
	 * @param pEvent the event
	 * @throws Throwable if event fails
	 */
	public void doCallBack(CallBackEvent pEvent) throws Throwable
	{
        oCallBack = pEvent.getObject();
	}

    /**
     * Callback listener method for #644.
     * 
     * @param pEvent the event
     * @throws Throwable if event fails
     */
    public void doCallBack1(CallBackEvent pEvent) throws Throwable
    {
        oCallBack = StringUtil.concat("1:", (oCallBack != null ? oCallBack : ""), CommonUtil.nvl(pEvent.getObject(), ""));
    }

    /**
     * Callback listener method for #644.
     * 
     * @param pEvent the event
     * @throws Throwable if event fails
     */
    public void doCallBack2(CallBackEvent pEvent) throws Throwable
    {
        oCallBack = StringUtil.concat("2:", (oCallBack != null ? oCallBack : ""), CommonUtil.nvl(pEvent.getObject(), ""));
    }
    
    /**
     * Callback listener method for #644.
     * 
     * @throws Throwable a SecurityException in any case
     */
    public void doCallBackThrowsError()
    {
        throw new SecurityException("Access denied to doCallBackThrowsError()");
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests that all {@link SubConnection}s, {@link RemoteDataSource}s and
	 * {@link RemoteDataBook}s are closed if a {@link MasterConnection} is
	 * closed.
	 *  
	 * @throws Throwable the test fails.
	 */
	@Test
	public void test1098CloseClosesAllSubConnectionsAndDataSources() throws Throwable
	{
		MasterConnection connection = new MasterConnection(new VMConnection());
		connection.setApplicationName("demo");
		connection.setUserName("rene");
		connection.setPassword("rene");
		connection.open();

		SubConnection sub = connection.createSubConnection("demo.StorageDataBookTest");
		sub.open();
		
		RemoteDataSource dataSource = new RemoteDataSource(sub);
		dataSource.open();
		
		RemoteDataBook dataBook = new RemoteDataBook();
		dataBook.setDataSource(dataSource);
		dataBook.setName("TEST");
		dataBook.open();
		
		// Sanity checks.
		Assert.assertTrue("The MasterConnection should be open, but it is closed.", connection.isOpen());
		Assert.assertTrue("The SubConnection should be open, but it is closed.", sub.isOpen());
		Assert.assertTrue("The RemoteDataSource should be open, but it is closed.", dataSource.isOpen());
		Assert.assertTrue("The RemoteDataBook should be open, but it is closed.", dataBook.isOpen());
		
		connection.close();
		
		Assert.assertFalse("The MasterConnection should be closed, but it is open.", connection.isOpen());
		Assert.assertFalse("The SubConnection should be closed, but it is open.", sub.isOpen());
		Assert.assertFalse("The RemoteDataSource should be closed, but it is open.", dataSource.isOpen());
		Assert.assertFalse("The RemoteDataBook should be closed, but it is open.", dataBook.isOpen());
	}
	
	/**
	 * Tests if an {@link IConnectionListener} is able to remove itself during
	 * an event without exception.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void test1178ConnectionListenerRemovesItselfDuringEvent() throws Throwable
	{
		MasterConnection connection = new MasterConnection(new VMConnection());
		connection.setApplicationName("demo");
		connection.setUserName("rene");
		connection.setPassword("rene");
		connection.open();
		
		connection.addConnectionListener(new DummyConnectionListener());
		connection.addConnectionListener(new DummyConnectionListener());
		connection.addConnectionListener(new SelfRemovingOnCloseConnectionListener());
		connection.addConnectionListener(new DummyConnectionListener());
		
		connection.close();
	}
	
	/**
	 * Tests an action call with a sub connection.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSubConnectionAction() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		SubConnection screen = appcon.createSubConnection("demo.User");
		screen.open();
		
		
		try
		{
			screen.callAction("checkMail");
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				screen.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
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
	 * Tests a application object call.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testApplicationObjectCall() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();
		IConnection conDemoCopy = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		MasterConnection appconDemoCopy = new MasterConnection(conDemoCopy);
		
		String sACL;
		
		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();
			
			appconDemoCopy.setApplicationName("democopy");
			appconDemoCopy.setUserName("rene");
			appconDemoCopy.setPassword("rene");
			appconDemoCopy.open();
			
			appconDemo.call("Application", "clearACL");
			appconDemo.call("Application", "addUserToACL", "JRx");
			appconDemo.call("Application", "addUserToACL", "HM");
			appconDemo.call("Application", "addUserToACL", "RH");
			appconDemo.call("Application", "addUserToACL", "Moby Dick");
	
			appconDemoCopy.call("Application", "clearACL");
			appconDemoCopy.call("Application", "addUserToACL", "Ed Hardy");
			appconDemoCopy.call("Application", "addUserToACL", "Bär");
			
			appconDemo.close();
			appconDemoCopy.close();
			
			//Tests if the application object will be reused (if the same instance will be
			//used more than once)
			appconDemo.open();
			appconDemoCopy.open();
			
			appconDemo.call("Application", "addUserToACL", "Meister");
			appconDemo.call("Application", "addUserToACL", "WDaniel");
			
			appconDemoCopy.call("Application", "addUserToACL", "WTobias");
			
			sACL = (String)appconDemo.call("Application", "getUserACL");
			
			Assert.assertEquals("[JRx, HM, RH, Moby Dick, Meister, WDaniel]", sACL);
			
			sACL = (String)appconDemoCopy.call("Application", "getUserACL");
			
			Assert.assertEquals("[Ed Hardy, Bär, WTobias]", sACL);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
			try
			{
				appconDemoCopy.close();
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
	 * Tests a session object call.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSessionObjectCall() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();
		IConnection conDemoCopy = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		MasterConnection appconDemoCopy = new MasterConnection(conDemoCopy);
		
		String sACL;
		
		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();
			
			appconDemoCopy.setApplicationName("democopy");
			appconDemoCopy.setUserName("rene");
			appconDemoCopy.setPassword("rene");
			appconDemoCopy.open();
			
			appconDemo.call("RemoteFile", "clearACL");
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			appconDemo.call("RemoteFile", "addUserToACL", "HM");
			appconDemo.call("RemoteFile", "addUserToACL", "RH");
			appconDemo.call("RemoteFile", "addUserToACL", "Moby Dick");
	
			appconDemoCopy.call("RemoteFile", "clearACL");
			appconDemoCopy.call("RemoteFile", "addUserToACL", "Ed Hardy");
			appconDemoCopy.call("RemoteFile", "addUserToACL", "Bär");
			
			appconDemo.close();
			appconDemoCopy.close();
			
			//Tests if the application object will be reused (if the same instance will be
			//used more than once)
			appconDemo.open();
			appconDemoCopy.open();
			
			appconDemo.call("RemoteFile", "addUserToACL", "Meister");
			appconDemo.call("RemoteFile", "addUserToACL", "WDaniel");
			
			appconDemoCopy.call("RemoteFile", "addUserToACL", "WTobias");
			
			sACL = (String)appconDemo.call("RemoteFile", "getUserACL");
			
			Assert.assertEquals("[Meister, WDaniel]", sACL);
			
			sACL = (String)appconDemoCopy.call("RemoteFile", "getUserACL");
			
			Assert.assertEquals("[WTobias]", sACL);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
			try
			{
				appconDemoCopy.close();
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
	 * Tests a simple action call with callback functionality.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBack() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		
		long lNow = System.currentTimeMillis();

		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();

			//delayedGetData, because the probability is hight that the
			//Callback result will be returned within the same call!
			appconDemo.callAction(this, "delayedGetData");
			
			Thread.sleep(8000);
			
			//If we didn't get the async result with the last call, we should get
			//it now!
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			
			Thread.sleep(1000);

			Assert.assertNotNull("Kein CallBack Objekt vorhanden", oCallBack);
			Assert.assertNotNull(sCallBackInfo);
			Assert.assertEquals("delayedGetData()", oCallBack);
			Assert.assertEquals(CALLBACK_OK, iCallBackType);

			//the execution should not take more than 6 seconds!
			//The check is important because the server object uses a delay of
			//3 seconds and that should have not effect for later calls.
			
			long lTime = System.currentTimeMillis() - lNow;
			
			Assert.assertTrue("Die Abarbeitung dauerte länger als 10,5 Sekunden: " + lTime, lTime < 10500);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Tests a call of an undefined method with callback functionality.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBackException() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		
		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();

			//The method is not available and the exception should be return whithin the same
			//call (Should be. Otherwise we will get the result with the next call)
			appconDemo.callAction(this, "method1");

			Thread.sleep(8000);
			
			//If we didn't get the lazy exception with the last call, we should get
			//it now!
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			
			Thread.sleep(1000);

			Assert.assertTrue("Result object is not an instance of Throwable", oCallBack instanceof Throwable);
			Assert.assertNotNull(sCallBackInfo);
			Assert.assertNotNull("No callback result available", oCallBack);
			Assert.assertEquals(CALLBACK_ERROR, iCallBackType);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Tests a call of a method which throws an exception, with callback functionality.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBackLazyException() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		
		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();

			//The method is not available and the exception should be return whithin the same
			//call (Should be. Otherwise we will get the result with the next call)
			appconDemo.callAction(this, "delayedException");

			Thread.sleep(8000);
			
			//If we didn't get the lazy exception with the last call, we should get
			//it now!
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			
			Thread.sleep(1000);

			Assert.assertTrue("Result object is not an instance of Throwable", oCallBack instanceof Throwable);
			Assert.assertNotNull(sCallBackInfo);
			Assert.assertNotNull("No callback object available", oCallBack);
			Assert.assertEquals(CALLBACK_ERROR, iCallBackType);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Test the callback functionality with the alive thread.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBackThroughAlive() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = createConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		appconDemo.setAliveInterval(5000);
		

		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();
			
			//delayedGetData, because the probability is hight that the
			//Callback result will be returned within the same call!
			appconDemo.callAction(this, "delayedGetData");
			
			Thread.sleep(7000);
			
			Assert.assertEquals(CALLBACK_OK, iCallBackType);
			Assert.assertNotNull(oCallBack);
			Assert.assertNotNull(sCallBackInfo);

			//Reset for the next test!
			oCallBack = null;
			iCallBackType = TestConnections.CALLBACK_UNDEFINED;
			
			appconDemo.call("RemoteFile", "getAbsolutePath");

			Thread.sleep(2500);

			appconDemo.call("RemoteFile", "getAbsolutePath");

			appconDemo.callAction(this, "delayedGetData");

			Thread.sleep(15000);

			Assert.assertEquals(CALLBACK_OK, iCallBackType);
			Assert.assertNotNull(oCallBack);
			Assert.assertNotNull(sCallBackInfo);

			//Reset
			oCallBack = null;
			iCallBackType = TestConnections.CALLBACK_UNDEFINED;
			
			appconDemo.call("RemoteFile", "getAbsolutePath");
			
			Assert.assertEquals(TestConnections.CALLBACK_UNDEFINED, iCallBackType);
			Assert.assertNull(oCallBack);
			Assert.assertNotNull(sCallBackInfo);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Tests the session timeout.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testTimeouts() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();
		appcon.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(15));

		//the name doesn't matter because the session timed out before the following
		//checkMail call will be executed!
		SubConnection screen = appcon.createSubConnection("demo.User");
		
		screen.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(7));
		screen.open();

		Thread.sleep(11000);
		
		//Now the connection/screen should be timed out
		try
		{
			try
			{
				screen.callAction("checkMail");
				
				Assert.fail("SubConnection is still open!");
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					screen.close();
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
			Assert.assertTrue("Invalid expired exception!", th instanceof SessionExpiredException);
		}
		
		thError = null;
		
		//The Master connection should work (not timed out)
		try
		{
			Assert.assertEquals("getData()", appcon.callAction("getData"));
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
	 * Tests set and get properties for the application and sub connection.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testProperties() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		SubConnection screen = appcon.createSubConnection("demo.User");
		
		screen.open();
		
		try
		{
			Assert.assertEquals("demo.Session", appcon.getLifeCycleName());
			Assert.assertEquals("demo.User", screen.getLifeCycleName());
			
			screen.setTimeout(5);
			
			appcon.setTimeout(2);
			
			Assert.assertEquals(Integer.valueOf(5), screen.getProperty("server.session.timeout"));
			Assert.assertEquals(Integer.valueOf(2), appcon.getProperty("server.session.timeout"));
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				screen.close();
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
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
	 * Tests set client properties which is not allowed.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSetClientProperties() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);


		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();
		
		try
		{
			try
			{
				appcon.setProperty(IConnectionConstants.USERNAME, "unknown");
				
				Assert.fail("It's not allowed to change client properties!");
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
		catch (Throwable th)
		{
			Assert.assertEquals("Client properties are not accessible after the connection was opened!", th.getMessage());
		}
	}

	/**
	 * Tests the connection listener handling without alive check.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testManualConnectionListener() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		appcon.addConnectionListener(this);
		appcon.setAliveInterval(-1);
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		SubConnection screen = appcon.createSubConnection("demo.User");
		
		screen.addConnectionListener(this);
		screen.open();
		
		appcon.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(15));
		screen.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(7));

		Thread.sleep(11000);
		
		//Now the connection/screen should be timed out
		try
		{
			try
			{
				screen.callAction("checkMail");
				
				Assert.fail("SubConnection is still open!");
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					screen.close();
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
			Assert.assertTrue("Invalid expired exception!", th instanceof SessionExpiredException);
		}
		
		thError = null;
		
		//The Master connection should work (not timed out)
		try
		{
			Assert.assertEquals("getData()", appcon.callAction("getData"));
		}
		catch (Throwable th)
		{
			try
			{
				appcon.close();
			}
			catch (Throwable thr)
			{
				//egal
			}
			
			throw th;
		}
		
		Thread.sleep(19000);
		
		//Now the Master connection should be timed out
		try
		{
			try
			{
				Assert.assertEquals("getData()", appcon.callAction("getData"));
				
				Assert.fail("MasterConnection is still open!");
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
		catch (Throwable th)
		{
			Assert.assertTrue("Invalid expired exception!", th instanceof SessionExpiredException);
		}

		checkConnectionListenerResult(new String[] {"WS javax.rad.remote.SessionExpiredException", "APP javax.rad.remote.SessionExpiredException"});
	}
	
	/**
	 * Tests the connection listener handling only with alive check.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testAutomaticConnectionListener() throws Throwable
	{
		LoggerFactory.init(null);		
//		LoggerFactory.setLevel("com.sibvisions.rad.server", LogLevel.ALL);
//		LoggerFactory.setLevel("javax.rad", LogLevel.ALL);
		LoggerFactory.setLevel("com.sibvisions.rad.server.DefaultSessionManager", LogLevel.ALL);
		
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		appcon.addConnectionListener(this);
		appcon.setAliveInterval(3000L);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		SubConnection screen = appcon.createSubConnection("demo.User");
		screen.addConnectionListener(this);
		screen.open();
		
		appcon.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(20));
		screen.setProperty(IConnectionConstants.SESSIONTIMEOUT_IN_SECONDS, Integer.valueOf(7));

		//not 2 minutes, because the Sub connection will touch the Master connection
		//during destroy
		Thread.sleep(25000);
		
		checkConnectionListenerResult(new String[] {"WS javax.rad.remote.SessionExpiredException", "APP javax.rad.remote.SessionExpiredException"});
	}
	
	/**
	 * Tests the getSubConnections method from the MasterConnection class.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSubMasterConnections() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.addConnectionListener(this);
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		SubConnection screen = appcon.createSubConnection("demo.User");

		screen.addConnectionListener(this);
		screen.open();
		
		List<SubConnection> liScreens = appcon.getSubConnections();

		screen.close();
		appcon.close();

		Assert.assertNotNull("No screens opened?!", liScreens);
		Assert.assertEquals(1, liScreens.size());
		
		liScreens = appcon.getSubConnections();
		
		Assert.assertNull("Screens are opened!?", liScreens);		
	}
	
	/**
	 * Tests {@link MasterConnection#createSubConnection(String)} when the master connection is not open.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCreateSubWhenNotOpen() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		try
		{
			appcon.createSubConnection("unknown");
			
			Assert.fail("Master connection is not open!");
		}
		catch (IllegalStateException ise)
		{
			Assert.assertEquals("The connection is not open!", ise.getMessage());
		}
	}
	
	/**
	 * Tests a simple call on {@link MasterConnection} when the connection is not open.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testMasterCallWhenNotOpen() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);

		try
		{
			appcon.call("unknwon", "unknown");
			
			Assert.fail("Master connection is not open!");
		}
		catch (IllegalStateException ise)
		{
			Assert.assertEquals("The connection is not open!", ise.getMessage());
		}
	}
	
	/**
	 * Tests a simple call on {@link SubConnection} when the connection is not open.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSubCallWhenNotOpen() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		try
		{
			SubConnection subcon = appcon.createSubConnection("demo.User");
			
			subcon.call("unknwon", "unknown");
			
			Assert.fail("Sub connection is not open!");
		}
		catch (IllegalStateException ise)
		{
			Assert.assertEquals("The connection is not open!", ise.getMessage());
		}
		finally
		{
			appcon.close();
		}
	}
	
	/**
	 * Tests sub calls through SessionContext.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSessionContextCall() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();
		
		SubConnection subcon = appcon.createSubConnection("demo.User");
		subcon.open();
		
		String sResult = (String)subcon.callAction("callSub");
		
		Assert.assertEquals("null.callSub -> [from: null.callSub] null.callMe", sResult);
		
		subcon.close();
		appcon.close();
	}

	/**
	 * Tests a simple call on {@link SubConnection} when the connection is not open.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testNonGenericBeanObjectCall() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		try
		{
			SubConnection subcon = appcon.createSubConnection("java.util.Vector");
			subcon.open();
			
			try
			{
				subcon.call("put", "KEY", "VALUE");
			
				Assert.fail("Only GenericBean lifecycle objects are allowed!");
			}
			catch (RuntimeException re)
			{
				Assert.assertEquals("The lifecycle object 'java.util.Vector' has to be a Map!", re.getMessage());
			}
			finally
			{
				subcon.close();
			}
		}
		finally
		{
			appcon.close();
		}
	}
	
	/**
	 * Tests a select statement sent from the client.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void test2TierStatementCall() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();

		try
		{
			String sStmt = (String)appcon.callAction("execute", "select * from strassen");
			
			RemoteDataSource rds = new RemoteDataSource(appcon);
			rds.open();
			
			RemoteDataBook rdb = new RemoteDataBook();
			rdb.setDataSource(rds);
			rdb.setName(sStmt);
			rdb.open();
		}
		finally
		{
			appcon.close();
		}
	}	

	/**
	 * Tests the access controller.
	 * 
	 * @throws Throwable if the access control doesn't work
	 */
	@Test
	public void test49() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("no_access");
		appcon.setPassword("no_access");

		appcon.open();

		try
		{
			SubConnection sucon = appcon.createSubConnection("demo.User");
			sucon.open();
			
			Assert.fail("Connection opened without access!");
		}
		catch (Throwable th)
		{
			Assert.assertEquals("Access to 'demo.User' denied!", th.getMessage());
		}

		appcon.close();

		appcon.setUserName("rene");
		appcon.setPassword("rene");
		appcon.open();
		
		SubConnection sucon = appcon.createSubConnection("demo.User");
		sucon.open();
		sucon.close();
		
		appcon.close();
	}
	
	/**
	 * Tests if sub connections are closed when master connection is closed.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testMasterClose() throws Throwable
	{
		IConnection con = createConnection();
		
		MasterConnection appcon = new MasterConnection(con);
		
		appcon.setApplicationName("demo");
		appcon.setUserName("rene");
		appcon.setPassword("rene");

		appcon.open();
		
		SubConnection sucon = appcon.createSubConnection("demo.User");
		sucon.open();

		appcon.close();
		
		Assert.assertFalse(sucon.isOpen());
		Assert.assertFalse(appcon.isOpen());
		
		Assert.assertNull(sucon.getConnectionId());
		Assert.assertNull(appcon.getConnectionId());
	}

	/**
	 * Tests ticket https://oss.sibvisions.com/index.php?do=details&task_id=643&project=2.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBack643() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = new DirectServerConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);
		
		long lNow = System.currentTimeMillis();

		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();

			//delayedGetData, because the probability is hight that the
			//Callback result will be returned within the same call!
			appconDemo.callAction(this, "delayedGetData");
			
			Thread.sleep(8000);
			
			//If we didn't get the async result with the last call, we should get
			//it now!
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			
			Thread.sleep(1000);

			Assert.assertNotNull("No callback object available", oCallBack);
			Assert.assertNotNull(sCallBackInfo);
			Assert.assertEquals("delayedGetData()", oCallBack);
			Assert.assertEquals(CALLBACK_OK, iCallBackType);

			//the execution should not take more than 6 seconds!
			//The check is important because the server object uses a delay of
			//3 seconds and that should have not effect for later calls.
			
			long lTime = System.currentTimeMillis() - lNow;
			
			Assert.assertTrue("Execution time was longer than 10.5 seconds: " + lTime, lTime < 10500);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Tests callback but without listener.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCallBackWithoutListener() throws Throwable
	{
		Throwable thError = null;
		
		IConnection conDemo = new DirectServerConnection();

		MasterConnection appconDemo = new MasterConnection(conDemo);

		
		try
		{
			appconDemo.setApplicationName("demo");
			appconDemo.setUserName("rene");
			appconDemo.setPassword("rene");
			appconDemo.open();

			//delayedGetData, because the probability is hight that the
			//Callback result will be returned within the same call!
			appconDemo.callAction(new ICallBackListener[] {null, null}, new String[] {"delayedGetData", "delayedGetData"}, null);
			
			Thread.sleep(8000);
			
			//If we didn't get the async result with the last call, we should get
			//it now!
			appconDemo.call("RemoteFile", "addUserToACL", "JRx");
			
			Thread.sleep(1000);

			Assert.assertNull("Callback object available!", oCallBack);
			Assert.assertNull(sCallBackInfo);
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				appconDemo.close();
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
	 * Tests feature request #1225. Checks if single connection property events will be fired.
	 * 
	 * @throws Throwable if test fails
	 * @see https://oss.sibvisions.com/index.php?do=details&task_id=1225
	 */
	@Test
	public void test1225PropertyListener() throws Throwable
	{
	    final ArrayUtil<String> liProps = new ArrayUtil<String>();
        final ArrayUtil<String> liAllProps = new ArrayUtil<String>();
	    
        IConnection con = createConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");
        appcon.addPropertyChangedListener("application.mode", new IConnectionPropertyChangedListener()
        {
            public void propertyChanged(PropertyEvent pEvent)
            {
                liProps.add(pEvent.getPropertyName() + " = " + pEvent.getNewValue());
            }
        });
        appcon.open();

        //after open -> we won't get notified about "standard" properties
        appcon.addPropertyChangedListener(null, new IConnectionPropertyChangedListener()
        {
            public void propertyChanged(PropertyEvent pEvent)
            {
                liAllProps.add(pEvent.getPropertyName() + " = " + pEvent.getNewValue());
            }
        });
        
        appcon.setProperty("application.list", "simple");
        appcon.callAction("setConnectionProperty", "application.mode", "JUnit Test");
        appcon.callAction("setConnectionProperty", "application.shutdown", "true");
        appcon.callAction(new String[] {"setConnectionProperty", "setConnectionProperty"}, 
                          new Object[][] {new Object[] {"application.list", "details"},
                                          new Object[] {"application.mode", "exit"}});
        
        String[] saResult = liProps.toArray(new String[liProps.size()]);
        String[] saAllResult = liAllProps.toArray(new String[liAllProps.size()]);
	    
        Assert.assertArrayEquals(new String[] {"application.mode = JUnit Test", "application.mode = exit"}, saResult);
        Assert.assertArrayEquals(new String[] {"application.list = simple",
                                               "application.mode = JUnit Test",
                                               "application.shutdown = true",
                                               "application.list = details",
                                               "application.mode = exit"}, saAllResult);
	}
	
    /**
     * Tests callback with objects and slots.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testCallBackWithSlots644() throws Throwable
    {
        oCallBack = null;
        Throwable thError = null;
        
        IConnection conDemo = new DirectServerConnection();

        MasterConnection appconDemo = new MasterConnection(conDemo);
        appconDemo.setAliveInterval(800);

        
        try
        {
            appconDemo.setApplicationName("demo");
            appconDemo.setUserName("rene");
            appconDemo.setPassword("rene");
            appconDemo.open();

            //action call
            appconDemo.callBackAction(this, "doCallBack", "delayedGetData");
            
            Thread.sleep(5000);

            Assert.assertEquals("delayedGetData()", oCallBack);
            
            oCallBack = null;
            
            //object call
            appconDemo.callBack(this, "doCallBack", "RemoteFile", "getAbsolutePath");
            
            Thread.sleep(2000);

            String sPath = new File("session.xml").getAbsolutePath(); 
            
            Assert.assertEquals(sPath, oCallBack);
            
            oCallBack = null;

            //multiple callback listeners
            appconDemo.callBack(new Object[] {this, this}, 
                                new String[] {"doCallBack1", "doCallBack2"}, 
                                new String[] {"RemoteFile", "RemoteFile"}, 
                                new String[] {"getAbsolutePath", "getAbsolutePath"},
                                null);
            
            Thread.sleep(2000);

            Assert.assertEquals("1:" + sPath + "2:" + sPath, oCallBack);

            final ArrayUtil<Throwable> liException = new ArrayUtil<Throwable>();

            IExceptionListener elistener = new IExceptionListener()
            {
                public void handleException(Throwable pThrowable)
                {
                    liException.add(pThrowable);
                }
            };
            
            try
            {
                ExceptionHandler.addExceptionListener(elistener);
                
                //exception
                appconDemo.callBack(this, "doCallBackThrowsError", "RemoteFile", "getAbsolutePath");
                
                Thread.sleep(2000);
                
                Assert.assertEquals(1, liException.size());
                Assert.assertEquals("Access denied to doCallBackThrowsError()", liException.get(0).getMessage());
            }
            finally
            {
                ExceptionHandler.removeExceptionListener(elistener);
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
                appconDemo.close();
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
	 * An {@link IConnectionListener} implementation that does nothing.
	 * 
	 * @author Robert Zenz
	 */
	private static final class DummyConnectionListener implements IConnectionListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of {@link DummyConnectionListener}.
		 */
		public DummyConnectionListener()
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void propertyChanged(PropertyEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void callError(CallErrorEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionOpened(ConnectionEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionReOpened(ConnectionEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionClosed(ConnectionEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionCalled(CallEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void objectCalled(CallEvent pEvent)
		{
			// Does nothing.
		}
		
	}	// DummyConnectionListener
	
	/**
	 * An {@link IConnectionListener} implementation that removes itself during
	 * the close event.
	 * 
	 * @author Robert Zenz
	 */
	private static final class SelfRemovingOnCloseConnectionListener implements IConnectionListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of
		 * {@link SelfRemovingOnCloseConnectionListener}.
		 */
		public SelfRemovingOnCloseConnectionListener()
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void propertyChanged(PropertyEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void callError(CallErrorEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionOpened(ConnectionEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionReOpened(ConnectionEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void connectionClosed(ConnectionEvent pEvent)
		{
			pEvent.getConnection().removeConnectionListener(this);
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionCalled(CallEvent pEvent)
		{
			// Does nothing.
		}

		/**
		 * {@inheritDoc}
		 */
		public void objectCalled(CallEvent pEvent)
		{
			// Does nothing.
		}
		
	}	// SelfRemovingOnCloseConnectionListener
	
}	// TestConnections
