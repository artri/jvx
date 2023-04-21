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
 * 14.02.2011 - [JR] - creation
 * 25.05.2011 - [JR] - #362: test connection remove
 */
package com.sibvisions.rad.server;

import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;
import javax.rad.server.ISession;
import javax.rad.server.event.FailedSessionEvent;
import javax.rad.server.event.IFailedSessionListener;
import javax.rad.server.event.ISessionListener;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.ServerZone;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.util.ChangedHashtable;

/**
 * Tests the functionality of <code>DefaultSessionManager</code>.
 *  
 * @author René Jahn
 */
public class TestDefaultSessionManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests creation of security manager after the config has changed.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSecurityManagerChange285() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
		chtAuth.put(IConnectionConstants.APPLICATION, "demo");
		chtAuth.put(IConnectionConstants.USERNAME, "rene");
		chtAuth.put(IConnectionConstants.PASSWORD, "rene");
		
		Object oSessId = srv.createSession(chtAuth);

		ISecurityManager secFirst = srv.getSessionManager().getSecurityManager(srv.getSessionManager().get(oSessId));

		try
		{
			srv.destroySession(oSessId);
			
			oSessId = srv.createSession(chtAuth);
	
			//same securitymanager should be used!
			ISecurityManager secSecond = srv.getSessionManager().getSecurityManager(srv.getSessionManager().get(oSessId));
			
			Assert.assertSame("Different security manager instances used!", secFirst, secSecond);
	
			ApplicationZone zone = Configuration.getApplicationZone("demo");
			
			//don't change the file!
			zone.setUpdateEnabled(false);
			zone.setSaveImmediate(false);
			
			zone.setProperty("/application/securitymanager/class", "com.sibvisions.rad.server.security.XmlSecurityManager");
			zone.setProperty("/application/securitymanager/userfile", "../xmlusers/users.xml");
			
			srv.destroySession(oSessId);
			
			//creates a new session with our temporary changed configuration!
			oSessId = srv.createSession(chtAuth);
	
			//new securitymanager should be created!
			ISecurityManager secThird = srv.getSessionManager().getSecurityManager(srv.getSessionManager().get(oSessId));
			
			Assert.assertNotSame("Same security manager instances used!", secSecond, secThird);
			
			srv.destroySession(oSessId);
		}
		finally
		{
			try
			{
				srv.destroySession(oSessId);
			}
			catch (Exception e)
			{
				//nothing to be done
			}
		}
	}

	/**
	 * Test completely remove of sessions from the session manager cache.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSubConnectionRemove362() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
		chtAuth.put(IConnectionConstants.APPLICATION, "xmlusers");
		chtAuth.put(IConnectionConstants.USERNAME, "rene");
		chtAuth.put(IConnectionConstants.PASSWORD, "rene");

		ChangedHashtable<String, Object> chtSub = new ChangedHashtable<String, Object>();
		
		Object oMasSessId = srv.createSession(chtAuth);
		
		srv.createSubSession(oMasSessId, chtSub);
		srv.createSubSession(oMasSessId, chtSub);

		//master-destroy should destroy subs too
		srv.destroySession(oMasSessId);
		
		Assert.assertEquals(0, srv.getSessionManager().getSessionCount());
	}
	
	/**
	 * Tests object access.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testObjectAccess535() throws Throwable
	{
		ServerZone zone = Configuration.getServerZone();
		zone.setSaveImmediate(false);
		zone.setProperty("/server/objectprovider/accesscontroller", "com.sibvisions.rad.server.security.SimpleAddressAccessController");

		try
		{
    		IDirectServer server = new Server();
    		
    		DirectServerConnection con = new DirectServerConnection(server);
    
    		
    		MasterConnection macon = null;
    		
    		try
    		{
    			macon = new MasterConnection(con);
    			macon.setLifeCycleName("demo.special.Address");
    			macon.setApplicationName("demo");
    			macon.setUserName("rene");
    			macon.setPassword("rene");
    			macon.open();
    	
    			macon.call("address", "getMetaData");
    			
    			try
    			{
    				macon.call("application", "getAbsolutePath");
    			}
    			catch (SecurityException se)
    			{
    				if (!"Access to 'application' is denied!".equals(se.getMessage()))
    				{
    					Assert.fail("Access to application should not be possible!");
    				}
    			}
    		}
    		finally
    		{
    			macon.close();
    		}
		}
		finally
		{
		    zone.reload();
		}
	}

	/**
	 * Test completely remove of sessions from the session manager cache.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testFailedSessionListener() throws Throwable
	{
		Server srv = Server.getInstance();
		
		IFailedSessionListener fsl = new IFailedSessionListener() 
		{
			@Override
			public void sessionFailed(FailedSessionEvent pEvent) 
			{
				Assert.assertNotNull(pEvent.getCause());
				Assert.assertTrue(pEvent.getCause().getMessage().startsWith("Invalid configuration file"));
			}
		};
		
		ISessionListener sl = new ISessionListener() 
		{
			@Override
			public void sessionDestroyed(ISession pSession) 
			{
				Assert.assertEquals(Boolean.TRUE.toString(), pSession.getProperty("userlogout"));
			}
			
			@Override
			public void sessionCreated(ISession pSession) 
			{
			}
		};
		
		srv.getSessionManager().addFailedSessionListener(fsl);
		
		try
		{
			srv.getSessionManager().addSessionListener(sl);

			try
			{
				ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
				chtAuth.put(IConnectionConstants.APPLICATION, "--noapp");
				chtAuth.put(IConnectionConstants.USERNAME, "rene");
				chtAuth.put(IConnectionConstants.PASSWORD, "rene");
		
				try
				{
					srv.createSession(chtAuth);
					
					Assert.fail("Invalid session created!");
				}
				catch (Exception e)
				{
					//we catch the exception with failed listener
				}
				
				chtAuth.put(IConnectionConstants.APPLICATION, "xmlusers");
				
				Object oMasSessId = srv.createSession(chtAuth);
				
				try
				{
					srv.setProperty(oMasSessId, "userlogout", Boolean.TRUE.toString());
				}
				finally
				{
					srv.destroySession(oMasSessId);
				}
			}
			finally
			{
				srv.getSessionManager().removeSessionListener(sl);
			}
		}
		finally
		{
			srv.getSessionManager().removeFailedSessionListener(fsl);	
		}
	}	
	
}	// TestDefaultSessionManager
