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
 * 23.02.2010 - [JR] - #18: testDirectObjectProperties: test case extended 
 */
package remote.net;

import javax.rad.remote.IConnection;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.event.ICallBackListener;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.BaseConnectionTest;
import com.sibvisions.rad.remote.ISerializer;

/**
 * Tests the IPC implementation of <code>IConnection</code>. It contains special
 * tests which are only possible with a VMConnection.
 * 
 * @author René Jahn
 */
public final class TestVMConnection extends BaseConnectionTest
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
		return new VMConnection(pSerializer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests session properties, set at client-side and read from server-side.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDirectObjectProperties() throws Throwable
	{
		Throwable thError = null;
		
		VMConnection con = (VMConnection)createConnection();
		
		MasterConnection macon = new MasterConnection(con);


		macon.setApplicationName("demo");
		macon.setUserName("rene");
		macon.setPassword("rene");
		macon.open();
		
		Object obj = new Object();
		
		//pure Objects won't be sent to the server!
		macon.setProperty("object", obj);

		//Integer will be sent to the server!
		macon.setProperty("integer", Integer.valueOf(12));
		
		//send properties to the server!
		macon.call((ICallBackListener[])null, new String[] {}, new String[] {}, null);

		try
		{
			//direct access to the server side session!
			Assert.assertNull(con.getServer().getServer().getProperty(macon.getConnectionId(), "object"));
			Assert.assertEquals(Integer.valueOf(12), con.getServer().getServer().getProperty(macon.getConnectionId(), "integer"));
			
			try
			{
				con.getServer().getServer().getProperty(null, "object");
				
				Assert.fail("Get property with \"null\" as session id is allowed!");
			}
			catch (Throwable th)
			{
				Assert.assertEquals("Invalid session id 'null'", th.getMessage());
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
				macon.close();
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
	 * Tests to destroy an invalid session.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDirectSessionDestroy() throws Throwable
	{
		Throwable thError = null;
		
		VMConnection con = (VMConnection)createConnection();
		
		MasterConnection macon = new MasterConnection(con);


		macon.setApplicationName("demo");
		macon.setUserName("rene");
		macon.setPassword("rene");
		macon.open();

		try
		{
			try
			{
				con.getServer().getServer().destroySession("INVALID");
			}
			catch (Throwable th)
			{
				thError = th;
			}
			finally
			{
				try
				{
					macon.close();
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
			Assert.assertEquals("Session expired 'INVALID'", th.getMessage());
		}
	}
	
	/**
	 * Tests session properties, set at client-side and read from server-side.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDirectSetGetProperty() throws Throwable
	{
		Throwable thError = null;
		
		VMConnection con = (VMConnection)createConnection();
		
		MasterConnection macon = new MasterConnection(con);


		macon.setApplicationName("demo");
		macon.setUserName("rene");
		macon.setPassword("rene");
		macon.setAliveInterval(60000);
		macon.open();
		
		//hiermit findet noch keine Übertragung statt!
		macon.setProperty("object", "valid");
		
		try
		{
			//direkter Zugriff auf die Server-side Session!
			Assert.assertNull(con.getServer().getServer().getProperty(macon.getConnectionId(), "object"));
			
			//Übertragung zum Server durchführen!
			macon.setTimeout(5);
			
			Assert.assertEquals("valid", con.getServer().getServer().getProperty(macon.getConnectionId(), "object"));

			//auch hier wird nicht sofort zum Server übertragen
			macon.setProperty("object", null);
			
			//Übertragung zum Server durchführen!
			macon.setTimeout(4);

			Assert.assertEquals(macon.getProperty("object"),  con.getServer().getServer().getProperty(macon.getConnectionId(), "object"));
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				macon.close();
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
	
}	// TestVMConnection
