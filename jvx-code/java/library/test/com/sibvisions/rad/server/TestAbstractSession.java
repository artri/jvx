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
 * 27.02.2011 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.InjectObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.xml.XmlNode;

/**
 * Tests the functionality of {@link AbstractSession}.
 * 
 * @author René Jahn
 */
public class TestAbstractSession
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the master session. */
	private MasterSession 	master;
	
	/** the company sub session. */
	private SubSession 	  	subCompany;
	
	/** th user sub session. */
	private SubSession		subUser;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates master and sub sessions.
	 * 
	 * @throws Throwable if session creation failed
	 */
	@Before
	public void beforeTest() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtAuth = new ChangedHashtable<String, Object>();
		chtAuth.put(IConnectionConstants.APPLICATION, "demo");
		chtAuth.put(IConnectionConstants.USERNAME, "rene");
		chtAuth.put(IConnectionConstants.PASSWORD, "rene");

		ChangedHashtable<String, Object> chtAuthSubCompany = new ChangedHashtable<String, Object>();
		chtAuthSubCompany.put(IConnectionConstants.LIFECYCLENAME, "demo.Company");

		ChangedHashtable<String, Object> chtAuthSubUser = new ChangedHashtable<String, Object>();
		chtAuthSubUser.put(IConnectionConstants.LIFECYCLENAME, "demo.User");
		
		Object oSessId = srv.createSession(chtAuth);

		master = (MasterSession)srv.getSessionManager().get(oSessId);

		Object oSubId = srv.createSubSession(oSessId, chtAuthSubCompany);
		
		subCompany = (SubSession)srv.getSessionManager().get(oSubId);
		
		oSubId = srv.createSubSession(oSessId, chtAuthSubUser);
		
		subUser = (SubSession)srv.getSessionManager().get(oSubId);
	}
	
	/**
	 * Destroys all sessions.
	 * 
	 * @throws Exception if session close fails
	 */
	@After
	public void afterTest() throws Exception
	{
		Server srv = Server.getInstance();
		
		srv.destroySession(master.getId());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets all injectable objects from the given session.
	 * 
	 * @param pSession the session
	 * @return the list of injectable objects
	 * @throws Exception if object detection failed
	 */
	private List<InjectObject> getInjectObjects(AbstractSession pSession) throws Exception
	{
		List<InjectObject> liResult = new ArrayUtil<InjectObject>();
		
		for (Enumeration<InjectObject> en = pSession.getInjectObjects(); en.hasMoreElements();)
		{
			liResult.add(en.nextElement());
		}
		
		return liResult;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests object injection without inject restrictions. The injected objects 
	 * are available for all sessions.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInjectObjectsAllSessions() throws Throwable
	{
		List<InjectObject> liObj = getInjectObjects(master);
			
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
			
		liObj = getInjectObjects(subCompany);
			
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		
		liObj = getInjectObjects(subUser);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
	}		

	/**
	 * Tests object injection for Sub sessions.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInjectObjectSubSession() throws Throwable
	{
		ApplicationZone zone = master.getApplicationZone();
		
		//don't change the file!
		zone.setUpdateEnabled(false);
		zone.setSaveImmediate(false);
		
		//ONLY SUB sessions gets the configured inject object

		XmlNode node = zone.getNode("/application/inject");
		
		XmlNode nodeAllow = new XmlNode("allow");
		nodeAllow.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "instanceof", "com.sibvisions.rad.server.SubSession"));
		
		node.add(nodeAllow);
		
		zone.setNode("/application/inject", node);

		List<InjectObject> liObj = getInjectObjects(master);
		
		Assert.assertEquals(0, liObj.size());
		
		liObj = getInjectObjects(subCompany);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		
		liObj = getInjectObjects(subUser);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
	}
	
	/**
	 * Tests object injection for Sub sessions.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDuplicateObjectName() throws Throwable
	{
		ApplicationZone zone = master.getApplicationZone();
		
		//don't change the file!
		zone.setUpdateEnabled(false);
		zone.setSaveImmediate(false);
		
		//ONLY SUB sessions gets the configured inject object

		XmlNode node = zone.getNode("/application");
		
		XmlNode nodeSecondMonitoring = new XmlNode("inject");
		nodeSecondMonitoring.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "monitoring"));
		nodeSecondMonitoring.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "class", "java.lang.String"));
		
		node.add(nodeSecondMonitoring);
		
		zone.setNode("/application", node);

		try
		{
			getInjectObjects(master);
			
			Assert.fail("Duplicate object name is used!");
		}
		catch (IllegalArgumentException iae)
		{
			if (!"Object name 'monitoring' is already used!".equals(iae.getMessage()))
			{
				throw iae;
			}
		}
	}
	
	/**
	 * Tests object injection for Master sessions.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInjectObjectMasterSession() throws Throwable
	{
		ApplicationZone zone = master.getApplicationZone();
		
		//don't change the file!
		zone.setUpdateEnabled(false);
		zone.setSaveImmediate(false);
		
		//ONLY MASTER session gets the configured inject object

		XmlNode node = zone.getNode("/application/inject");
		
		XmlNode nodeAllow = new XmlNode("allow");
		nodeAllow.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "instanceof", "com.sibvisions.rad.server.MasterSession"));
		
		node.add(nodeAllow);
		
		zone.setNode("/application/inject", node);

		List<InjectObject> liObj = getInjectObjects(master);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		
		liObj = getInjectObjects(subCompany);
		
		Assert.assertEquals(0, liObj.size());
		
		liObj = getInjectObjects(subUser);
		
		Assert.assertEquals(0, liObj.size());
	}
	
	/**
	 * Tests object injection for s specific session.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInjectObjectSpecificSession() throws Throwable
	{
		ApplicationZone zone = master.getApplicationZone();
		
		//don't change the file!
		zone.setUpdateEnabled(false);
		zone.setSaveImmediate(false);
		
		//ONLY dem.User SUB session gets the configured inject object

		XmlNode node = zone.getNode("/application/inject");
		
		XmlNode nodeAllow = new XmlNode("allow");
		nodeAllow.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "lifecyclename", "demo.User"));
		
		node.add(nodeAllow);
		
		zone.setNode("/application/inject", node);

		List<InjectObject> liObj = getInjectObjects(master);
		
		Assert.assertEquals(0, liObj.size());
		
		liObj = getInjectObjects(subCompany);
		
		Assert.assertEquals(0, liObj.size());
		
		liObj = getInjectObjects(subUser);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
	}		

	/**
	 * Tests object injection with class name.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetInjectByClass() throws Throwable
	{
		ApplicationZone zone = master.getApplicationZone();
		
		//don't change the file!
		zone.setUpdateEnabled(false);
		zone.setSaveImmediate(false);
		
		XmlNode node = zone.getNode("/application/inject");
		node.removeNode("/object");
		node.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "class", "java.util.Vector"));
		
		zone.setNode("/application/inject", node);

		
		List<InjectObject> liObj = getInjectObjects(master);
		
		Assert.assertEquals(1, liObj.size());
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		Assert.assertTrue("Injected object is not a Vector!", (liObj.get(0).getObject() instanceof Vector));

		liObj = getInjectObjects(subCompany);
		
		Assert.assertEquals(1, liObj.size());
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		Assert.assertTrue("Injected object is not a Vector!", (liObj.get(0).getObject() instanceof Vector));

		liObj = getInjectObjects(subUser);
		
		Assert.assertEquals(1, liObj.size());
		Assert.assertEquals("monitoring", liObj.get(0).getName());
		Assert.assertTrue("Injected object is not a Vector!", (liObj.get(0).getObject() instanceof Vector));
	}		
	
	/**
	 * Tests manual object injection with a preconfigured object.
	 */
	@Test
	public void testChangePreconfiguredObject()
	{
		try
		{
			master.putObject(new InjectObject("monitoring", "OBJECT"));
			
			Assert.fail("It is not allowed to change a preconfigured object!");
		}
		catch (RuntimeException re)
		{
			if (!"It is not allowed to change the preconfigured object 'monitoring'!".equals(re.getMessage()))
			{
				throw re;
			}
		}
	}
	
	/**
	 * Tests manual object injection with an invalid object name.
	 */
	@Test
	public void testInvalidObjectName()
	{
		try
		{
			master.putObject(new InjectObject("!monitoring", "OBJECT"));
			
			Assert.fail("Invalid object name used!");
		}
		catch (RuntimeException re)
		{
			if (!"The object name '!monitoring' is not valid!".equals(re.getMessage()))
			{
				throw re;
			}
		}
	}
	
	/**
	 * Tests remove a preconfigured object.
	 */
	@Test
	public void testRemoveObject()
	{
		try
		{
			master.removeObject(master.getObject("monitoring"));
			
			Assert.fail("It is not allowed to remove a preconfigured object!");
		}
		catch (RuntimeException re)
		{
			if (!"It is not allowed to remove the preconfigured object 'monitoring'!".equals(re.getMessage()))
			{
				throw re;
			}
		}
	}
	
	/**
	 * Tests programatically inject an object.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testPutObject() throws Throwable
	{
		String sInstance = new String("OBJECT");

		master.putObject(new InjectObject("string", sInstance));
		
		//call -> creates a SessionContext...
		String sValue = (String)master.call("string", "toString");
		
		Assert.assertEquals(sInstance, sValue);
		
		//change injected object (-> changes life-cycle object too

		String sInstance2 = new String("NEW OBJECT");
		
		master.putObject(new InjectObject("string", sInstance2));
		
		//call -> creates a SessionContext...
		sValue = (String)master.call("string", "toString");
		
		Assert.assertEquals(sInstance2, sValue);
	}	

	/**
	 * Tests whether a session is initializing.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testIsInitializing() throws Throwable
	{
	    Assert.assertFalse(Server.getInstance().getSessionManager().isInitializing(master));
	    
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "callhandler");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "dummy");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "dummy");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
	        
        IConnection con = new DirectServerConnection();
        
        try
        {
            con.open(ciMaster);

            Assert.assertEquals(Boolean.TRUE, ciMaster.getProperties().get("isInitializing"));
            
            Assert.assertFalse(Server.getInstance().getSessionManager().isInitializing(master));
        }
        finally
        {
            try
            {
                con.close(ciMaster);
            }
            catch (Throwable th)
            {
                //ignore
            }
        }
	}
	
}	// TestAbstractSession
