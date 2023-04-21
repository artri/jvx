/*
 * Copyright 2014 SIB Visions GmbH
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
 * 13.09.2014 - [JR] - creation
 */
package com.sibvisions.rad.server;

import javax.rad.remote.IConnectionConstants;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ChangedHashtable;

import demo.InstanceChecker;
import demo.QuickConnect;
import demo.Session;
import demo.SessionIsolated;
import demo.special.ScreenIsolation;
import demo.special.ScreenWithSessionIsolation;

/**
 * Tests the ObjectProvider session isolation feature.
 * 
 * @author René Jahn
 * @see ObjectProvider
 */
public class TestObjectProviderSessionIsolation
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests simple session isolation with one LCO without inheritance.
	 *  
	 * @throws Throwable if test fails
	 */
	@Test
	public void testSimpleLifeCycleIsolation() throws Throwable
	{
	    InstanceChecker.clear();
	    
        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.LIFECYCLENAME, QuickConnect.class.getName());
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
        
        String sValue = (String)session.callAction("getValue");
     
        Assert.assertEquals(QuickConnect.class.getName(), sValue);
	}

	/**
	 * Tests session isolation with an LCO that extends an isolated LCO (means no isolation).
	 * 
	 * @throws Throwable if test fails
	 */
    @Test
    public void testInheritedLifeCycleIsolation() throws Throwable
    {
        InstanceChecker.clear();

        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.LIFECYCLENAME, ScreenWithSessionIsolation.class.getName());
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
        
        String sValue = (String)session.callAction("getValue");
     
        Assert.assertEquals(demo.Application.class.getName() + "\n" + SessionIsolated.class.getName() + "\n" + 
                            ScreenWithSessionIsolation.class.getName() + "\n" + demo.Application.class.getName(), sValue);
    }

    /**
     * Test an isolated LCO that extends the standard session LCO.
     * (This use-case doesn't solve a real-world problem and shouldn't be used, but we need a test for this)
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testScreenIsolation() throws Throwable
    {
        InstanceChecker.clear();

        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
        
        ChangedHashtable<String, Object> chtPropertiesSub = new ChangedHashtable<String, Object>();
        
        chtPropertiesSub.put(IConnectionConstants.LIFECYCLENAME, ScreenIsolation.class.getName());
        
        SubSession sessSub = new SubSession(session, chtPropertiesSub);
        
        String sValue = (String)sessSub.callAction("getValue");
     
        Assert.assertEquals(demo.Application.class.getName() + "\n" + Session.class.getName() + "\n" + 
                            ScreenIsolation.class.getName(), sValue);
    }
	
    /**
     * Test sub session creation with an isolated master session.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testSubSessionCreationWithIsolation() throws Throwable
    {
        InstanceChecker.clear();
        
        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.LIFECYCLENAME, SessionIsolated.class.getName());
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);

        ChangedHashtable<String, Object> chtPropertiesSub = new ChangedHashtable<String, Object>();
        
        chtPropertiesSub.put(IConnectionConstants.LIFECYCLENAME, ScreenWithSessionIsolation.class.getName());
        
        try
        {
            new SubSession(session, chtPropertiesSub);
            
            Assert.fail("It shouldn't be possible to create a sub session from an isolated session!");
        }
        catch (Exception e)
        {
            Assert.assertEquals("It's not possible to create a sub session from an isolated session!", e.getMessage());
        }
        finally
        {
            String sValue = (String)session.callAction("getValue");
     
            Assert.assertEquals(demo.Application.class.getName() + "\n" + SessionIsolated.class.getName(), sValue);
        }
    }    
    
} 	// TestObjectProviderSessionIsolation
