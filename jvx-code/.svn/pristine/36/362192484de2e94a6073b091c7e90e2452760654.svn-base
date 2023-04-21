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
 * 30.06.2011 - [JR] - creation
 */
package com.sibvisions.rad.server;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.Zone;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * Tests for {@link MasterSession}.
 * 
 * @author René Jahn
 */
public class TestMasterSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests live config option.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testLiveConfig() throws Throwable
	{
		Server srv = Server.getInstance();

		ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
		chtAuth.put(IConnectionConstants.APPLICATION, "xmlusers");
		chtAuth.put(IConnectionConstants.USERNAME, "rene");
		chtAuth.put(IConnectionConstants.PASSWORD, "rene");

		Zone zone = Configuration.getServerZone();
		zone.setSaveImmediate(false);
		zone.setUpdateEnabled(false);
		zone.setProperty("/server/liveconfig", "false");

		Object oMasSessId = srv.createSession(chtAuth);

		ApplicationZone appzone = Configuration.getApplicationZone("xmlusers");
		appzone.setUpdateEnabled(false);
		appzone.setSaveImmediate(false);

		try
		{
			//modify config
			appzone.setProperty("/application/dummy", "NEW");
			
			MasterSession mas = (MasterSession)srv.getSessionManager().get(oMasSessId);
			
			Assert.assertNull("Application config contains live values!", mas.getConfig().getProperty("/application/dummy"));
		}
		finally
		{
			srv.destroySession(oMasSessId);
		}
		
		zone.setProperty("/server/liveconfig", "true");

		oMasSessId = srv.createSession(chtAuth);

		try
		{
			//modify config
			appzone.reload();
			appzone.setProperty("/application/dummy", "NEW");
			
			MasterSession mas = (MasterSession)srv.getSessionManager().get(oMasSessId);
			
			Assert.assertEquals("NEW", mas.getConfig().getProperty("/application/dummy"));
		}
		finally
		{
			srv.destroySession(oMasSessId);
		}
	}

    /**
     * Tests post authentication.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testSessionValidator() throws Throwable
    {
        Server srv = Server.getInstance();

        ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
        chtAuth.put(IConnectionConstants.APPLICATION, "xmlusers");
        chtAuth.put(IConnectionConstants.USERNAME, "rene");
        chtAuth.put(IConnectionConstants.PASSWORD, "rene");

        ApplicationZone appzone = Configuration.getApplicationZone("xmlusers");
        appzone.setUpdateEnabled(false);
        appzone.setSaveImmediate(false);

        appzone.setProperty("/application/lifecycle/mastersession/postAuthClass", PostAuthentication.class.getName());
        
        Object oMasSessId = null;
        
        try
        {
            oMasSessId = srv.createSession(chtAuth);
        }
        catch (SecurityException se)
        {
            Assert.assertEquals("Access denied (post)!", ExceptionUtil.getRootCause(se).getMessage());
        }
        finally
        {
            srv.destroySession(oMasSessId);
        }

        appzone.reload();
        appzone.setProperty("/application/lifecycle/mastersession/preAuthClass", PreAuthentication.class.getName());
        
        try
        {
            oMasSessId = srv.createSession(chtAuth);
        }
        catch (SecurityException se)
        {
            Assert.assertEquals("Access denied (pre)!", ExceptionUtil.getRootCause(se).getMessage());
        }
        finally
        {
        	//restore the config for other tests
        	appzone.reload();
        	
            srv.destroySession(oMasSessId);
        }
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>PreAuthentication</code> is a helper class for pre session authentication tests.
     * 
     * @author René Jahn
     */
    public static class PreAuthentication implements ISessionValidator
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void validate(ISession pSession, ISecurityManager pSecurityManager) throws Exception
        {
            throw new SecurityException("Access denied (pre)!");
        }
    }

    /**
     * The <code>PostAuthentication</code> is a helper class for post session authentication tests.
     * 
     * @author René Jahn
     */
    public static class PostAuthentication implements ISessionValidator
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void validate(ISession pSession, ISecurityManager pSecurityManager) throws Exception
        {
            throw new SecurityException("Access denied (post)!");
        }
    }
    
}	// TestMasterSession
