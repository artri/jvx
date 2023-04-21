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
 * 18.11.2009 - [JR] - #33: put session test
 * 24.03.2010 - [JR] - #105: test case for dot notation
 */
package com.sibvisions.rad.server;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.rad.application.ILauncher;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.SessionContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.server.annotation.Inherit;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.ChangedHashtable;

import remote.RemoteFile;

/**
 * Tests the ObjectProvider methods.
 * 
 * @author René Jahn
 * @see ObjectProvider
 */
public class TestObjectProvider
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets values before each test.
     */
    @Before
    public void beforeTest()
    {
        ApplicationLCOTest.iCountAppConstructor = 0;        
        ApplicationLCOTest.iCountAppPostConstruct = 0;
        ApplicationLCOTest.iCountAppDestroy = 0;
        ApplicationLCOTest.iCountAppPreDestroy = 0;
        
        SessionLCOTest.iCountSessionConstructor = 0;        
        SessionLCOTest.iCountSessionPostConstruct = 0;
        SessionLCOTest.iCountSessionDestroy = 0;
        SessionLCOTest.iCountSessionPreDestroy = 0;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests to get a life-cycle object for an application.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetLifeCycleObject() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
		
		chtProperties.put(IConnectionConstants.APPLICATION, "demo");
		chtProperties.put(IConnectionConstants.USERNAME, "rene");
		chtProperties.put(IConnectionConstants.PASSWORD, "rene");
		
		MasterSession session1 = new MasterSession(srv.getSessionManager(), chtProperties);
		MasterSession session2 = new MasterSession(srv.getSessionManager(), chtProperties);

		ChangedHashtable<String, Object> chtPropertiesCopy = new ChangedHashtable<String, Object>();
		
		chtPropertiesCopy.put(IConnectionConstants.APPLICATION, "democopy");
		chtPropertiesCopy.put(IConnectionConstants.USERNAME, "rene");
		chtPropertiesCopy.put(IConnectionConstants.PASSWORD, "rene");
		
		MasterSession session3 = new MasterSession(srv.getSessionManager(), chtPropertiesCopy);

		//-------------------------------------------------------------------------------------
		// Session-1
		//-------------------------------------------------------------------------------------

		SessionContext ctxt = session1.createSessionContext(null, null);
		
		try
		{
			Object oSession1 = ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session1, null);
			
			Assert.assertTrue(oSession1.getClass().getName().equals("demo.Session"));
		}
		finally
		{
			ctxt.release();
		}
	
		session1.execute(new Call(null, "application", "addUserToACL", "Application, WDaniel"));

		String sACLAppl = (String)session1.execute(new Call(null, "Application", "getUserACL"));

		//-------------------------------------------------------------------------------------
		// Session-2
		//-------------------------------------------------------------------------------------
		
		ctxt = session2.createSessionContext(null, null);
		
		try
		{
			Object oSession2 = ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session2, null);
		
			Assert.assertTrue(oSession2.getClass().getName().equals("demo.Session"));
		}
		finally
		{
			ctxt.release();
		}
			
		//An dieser Stelle muss die ACL noch immer ident sein, da es sich um Objekte der 
		//Application life-cycle Objekte handelt
		Assert.assertEquals(sACLAppl, (String)session2.execute(new Call(null, "application", "getUserACL")));
		
		//-------------------------------------------------------------------------------------
		// Session-3
		//-------------------------------------------------------------------------------------
		
		ctxt = session3.createSessionContext(null, null);
		
		try
		{
			((DefaultObjectProvider)srv.getObjectProvider()).getObject(session3, null);
		}
		finally
		{
			ctxt.release();
		}
		
		//Application ACL MUSS leer sein -> andere Applikation
		Assert.assertEquals("[]", (String)session3.execute(new Call(null, "application", "getUserACL")));
		
		//Die Application ACL von Session 1 und 2 MUSS noch immer gültig sein, weil sich dort das Objekt
		//nicht geändert hat!
		Assert.assertEquals(sACLAppl, (String)session2.execute(new Call(null, "application", "getUserACL")));
	}

	/**
	 * Tests to get a session object for an application. The object
	 * name will be dot notated.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetSessionObject() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
		
		chtProperties.put(IConnectionConstants.APPLICATION, "demo");
		chtProperties.put(IConnectionConstants.USERNAME, "rene");
		chtProperties.put(IConnectionConstants.PASSWORD, "rene");
		
		MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
		
		SessionContext ctxt = session.createSessionContext("remoteFile", null);
		
		try
		{
			RemoteFile remFile = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile");
			
			Assert.assertEquals("session.xml", remFile.getName());
			
			RemoteFile remParentFile = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile.parent");
			
			Assert.assertEquals(Configuration.getApplicationsDir().getAbsolutePath(), remParentFile.getAbsolutePath());
		}
		finally
		{
			ctxt.release();
		}
	}
	
	/**
	 * Tests to put a session object for an application.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testPutSessionObject() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
		
		chtProperties.put(IConnectionConstants.APPLICATION, "demo");
		chtProperties.put(IConnectionConstants.USERNAME, "rene");
		chtProperties.put(IConnectionConstants.PASSWORD, "rene");
		
		MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
		
		SessionContext ctxt = session.createSessionContext("remoteFile", null);
		
		try
		{
			//Put an object directly to the object provider
			
			RemoteFile remFile = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile");
			
			Assert.assertEquals("session.xml", remFile.getName());
			
			RemoteFile remFileNew = new RemoteFile(new File("put.xml"));
			
			RemoteFile remFileOld = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).putObject(session, "remoteFile", remFileNew);
			
			Assert.assertEquals(remFileOld, remFile);
			
			remFile = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile");
			
			Assert.assertEquals("put.xml", remFile.getName());
			Assert.assertEquals(remFile, remFileNew);
			
			// Same as above but via ISession
			
			remFile = (RemoteFile)session.get("remoteFile");
			
			Assert.assertEquals(remFile, remFileNew);
			
			session.put("remoteFile", remFileOld);
			
			remFile = (RemoteFile)session.get("remoteFile");
			
			Assert.assertEquals(remFile, remFileOld);
		}
		finally
		{
			ctxt.release();
		}
	}
	
	/**
	 * Tests to put a session object, with dot notation, for an application.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testPutSessionObjectDotNotated() throws Throwable
	{
		Server srv = Server.getInstance();
		
		ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
		
		chtProperties.put(IConnectionConstants.APPLICATION, "demo");
		chtProperties.put(IConnectionConstants.USERNAME, "rene");
		chtProperties.put(IConnectionConstants.PASSWORD, "rene");
		
		MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);
		
		SessionContext ctxt = session.createSessionContext("remoteFile.parent", null);
		
		try
		{
			RemoteFile remFirst = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile");
				
			RemoteFile remNewParent = new RemoteFile(Configuration.getConfigurationDir()); 
			
			((DefaultObjectProvider)srv.getObjectProvider()).putObject(session, "remoteFile.parent", remNewParent);
			
			RemoteFile remParent = (RemoteFile)((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile.parent");

			Assert.assertEquals(remNewParent.getAbsolutePath(), remParent.getAbsolutePath());
			
			
			RemoteFile remSecond = new RemoteFile(new File("/root/dummy.txt")); 
			
			((DefaultObjectProvider)srv.getObjectProvider()).putObject(session, "remoteFile", remSecond);
			
			Assert.assertTrue("RemoteFile not changed!", !remFirst.getAbsolutePath().equals(remSecond.getAbsolutePath()));
		}
		finally
		{
			ctxt.release();
		}
	}

    /**
     * Tests to get a sub object. This case tests {@link com.sibvisions.rad.server.annotation.Accessible} annotation.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testAccessibleAnnotation() throws Throwable
    {
        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);

        SessionContext ctxt = session.createSessionContext("remoteFile.notAccessibleParent", null);
                
        try
        {
            ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile.notAccessibleParent");
            
            Assert.fail("Access restriction didn't work!");
        }
        catch (Exception e)
        {
            Assert.assertEquals("Access to remoteFile.notAccessibleParent denied!", e.getMessage());
        }
        finally
        {
            ctxt.release();
        }
        
        ctxt = session.createSessionContext("remoteFile.parent", "name");
        
        try
        {
            Assert.assertEquals("apps", ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "remoteFile.parent.name"));
        }
        finally
        {
            ctxt.release();
        }
    }
	
    /**
     * Tests to get a not accessible object. This test case tests {@link com.sibvisions.rad.server.annotation.NotAccessible} annotation. 
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testNotAccessibleAnnotation() throws Throwable
    {
        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);

        SessionContext ctxt = session.createSessionContext(null, "notAccessible");
        
        try
        {
            try
            {
                ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "notAccessible");
                
                Assert.fail("Checking NotAccessible annotation failed!");
                
            }
            catch (Exception e)
            {
                Assert.assertEquals("Access to notAccessible denied!", e.getMessage());
            }

            try
            {
                ((DefaultObjectProvider)srv.getObjectProvider()).invoke(session, null, "getNotAccessible");
                
                Assert.fail("Checking NotAccessible annotation failed!");
            }
            catch (Exception e)
            {
                Assert.assertEquals("Access to getNotAccessible denied!", e.getMessage());
            }
        }        
        finally
        {
            ctxt.release();
        }
    }
    
    /**
     * Tests to get a not accessible object. This test case tests {@link com.sibvisions.rad.server.annotation.NotAccessible} annotation. 
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testNotAccessibleEnvironmentAnnotation() throws Throwable
    {
        Server srv = Server.getInstance();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "demo");
        chtProperties.put(IConnectionConstants.USERNAME, "rene");
        chtProperties.put(IConnectionConstants.PASSWORD, "rene");
        chtProperties.put(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT, ILauncher.ENVIRONMENT_WEB + ":rest");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);

        SessionContext ctxt = session.createSessionContext(null, "notAccessibleREST");
        
        try
        {
            try
            {
                ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "notAccessibleREST");
                
                Assert.fail("Checking NotAccessibleREST annotation failed!");
            }
            catch (Exception e)
            {
                Assert.assertEquals("Access to notAccessibleREST denied!", e.getMessage());
            }
            
            session.getProperties().put(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT, ILauncher.ENVIRONMENT_DESKTOP);
            
            try
            {
                ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "notAccessibleREST");

                //everything is fine
            }
            catch (Exception e)
            {
                Assert.fail("Access to notAccessibleREST is denied!");
            }
            
            session.getProperties().put(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT, null);

            try
            {
                ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, "notAccessibleREST");

                //everything is fine
            }
            catch (Exception e)
            {
                Assert.fail("Access to notAccessibleREST is denied!");
            }
        }        
        finally
        {
            ctxt.release();
        }
    }
    

    /**
     * Tests post construct annotation. 
     * (see https://oss.sibvisions.com/index.php?do=details&task_id=965&project=2)
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testObjectLifecycleAnnotations() throws Throwable
    {
        //we need a new server, otherwise the counter may be invalid
        Server srv = new Server();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "annotations");
        chtProperties.put(IConnectionConstants.USERNAME, "admin");
        chtProperties.put(IConnectionConstants.PASSWORD, "admin");
        
        MasterSession session = new MasterSession(srv.getSessionManager(), chtProperties);

        try
        {
            ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, null);
            ((DefaultObjectProvider)srv.getObjectProvider()).sessionDestroyed(session);

            //1x super call in Session constructor
            //1x Application LCO creation
            Assert.assertEquals(2, ApplicationLCOTest.iCountAppConstructor);
            Assert.assertEquals(1, ApplicationLCOTest.iCountAppPostConstruct);

            Assert.assertEquals(1, SessionLCOTest.iCountSessionConstructor);
            Assert.assertEquals(1, SessionLCOTest.iCountSessionPostConstruct);
            
            Assert.assertEquals(1, ApplicationLCOTest.iCountAppDestroy);
            Assert.assertEquals(0, ApplicationLCOTest.iCountAppPreDestroy);

            Assert.assertEquals(1, SessionLCOTest.iCountSessionDestroy);
            Assert.assertEquals(2, SessionLCOTest.iCountSessionPreDestroy);
            
            session = new MasterSession(srv.getSessionManager(), chtProperties);
            
            ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, null);
            ((DefaultObjectProvider)srv.getObjectProvider()).sessionDestroyed(session);
            
            Assert.assertEquals(3, ApplicationLCOTest.iCountAppConstructor);
            Assert.assertEquals(1, ApplicationLCOTest.iCountAppPostConstruct);

            Assert.assertEquals(2, SessionLCOTest.iCountSessionConstructor);
            Assert.assertEquals(2, SessionLCOTest.iCountSessionPostConstruct);
            
            Assert.assertEquals(2, ApplicationLCOTest.iCountAppDestroy);
            Assert.assertEquals(0, ApplicationLCOTest.iCountAppPreDestroy);

            Assert.assertEquals(2, SessionLCOTest.iCountSessionDestroy);
            Assert.assertEquals(4, SessionLCOTest.iCountSessionPreDestroy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    
    
    /**
     * This case tests {@link com.sibvisions.rad.server.annotation.Inherit} annotation.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testInheritAnnotation() throws Throwable
    {
        //we need a new server, otherwise other tests may be invalid
        Server srv = new Server();
        
        ChangedHashtable<String, Object> chtProperties = new ChangedHashtable<String, Object>();
        
        chtProperties.put(IConnectionConstants.APPLICATION, "annotations");
        chtProperties.put(IConnectionConstants.USERNAME, "admin");
        chtProperties.put(IConnectionConstants.PASSWORD, "admin");
        
        ChangedHashtable<String, Object> chtSubProperties = new ChangedHashtable<String, Object>(chtProperties);
        chtSubProperties.put(IConnectionConstants.LIFECYCLENAME, SubLCOTest.class.getName());
        
        //create sessions via server, otherwise the cache will be cleared after invoke 
        //(because session manager doesn't cache the session)
        Object oMasterId = srv.createSession(chtProperties);
        Object oSubId = srv.createSubSession(oMasterId, chtSubProperties);
        
        MasterSession session = (MasterSession)srv.getSessionManager().get(oMasterId);
        SubSession sub = (SubSession)srv.getSessionManager().get(oSubId);

        SessionContext ctxt = sub.createSessionContext(null, "getId");
                
        Long lSessionLCOValue;
        Long lSubLCOValue;
        
        try
        {
            //method should return the identityHashCode of session LCO
            lSessionLCOValue = (Long)((DefaultObjectProvider)srv.getObjectProvider()).invoke(sub, null, "getId");
            //@Inherit method should return the identityHashCode of sub LCO
            lSubLCOValue = (Long)((DefaultObjectProvider)srv.getObjectProvider()).invoke(sub, null, "getIdInherit");
            
            Object oSessionLCO = ((DefaultObjectProvider)srv.getObjectProvider()).getObject(session, null);
            Object oSubLCO = ((DefaultObjectProvider)srv.getObjectProvider()).getObject(sub, null);
            
            Assert.assertEquals(lSessionLCOValue.longValue(), System.identityHashCode(oSessionLCO));
            Assert.assertEquals(lSubLCOValue.longValue(), System.identityHashCode(oSubLCO));
            
            //overwritten method should return the identityHashCode of sub LCO
            lSubLCOValue = (Long)((DefaultObjectProvider)srv.getObjectProvider()).invoke(sub, null, "getId2");
            
            Assert.assertEquals(lSubLCOValue.longValue(), System.identityHashCode(oSubLCO));
        }
        finally
        {
            ctxt.release();

            srv.destroySession(oMasterId);
        }
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * An application level LCO for annotations test application.
     * 
     * @author René Jahn
     */
    public static class ApplicationLCOTest extends GenericBean
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** counter for constructor calls. */
        static int iCountAppConstructor   = 0;
        /** counter for post-construct (via annotation) calls. */
        static int iCountAppPostConstruct = 0;
        /** counter for destroy calls. */
        static int iCountAppDestroy = 0;
        /** counter for pre-destroy (via annotation) calls. */ 
        static int iCountAppPreDestroy = 0;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>ApplicationLCOTest</code>.
         */
        public ApplicationLCOTest()
        {
            iCountAppConstructor++;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void destroy()
        {
            super.destroy();
            
            iCountAppDestroy++;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Invoked after construction (not after constructor).
         */
        @PostConstruct
        public void doPostConstruct()
        {
            iCountAppPostConstruct++;
        }

        /**
         * Invoked before destroying.
         */
        @PreDestroy
        private void doDestroy()
        {
            iCountAppPreDestroy++;
        }
        
    }   // ApplicationLCOTest
    
    /**
     * A (master)session level LCO for annotations test application.
     * 
     * @author René Jahn
     */
    public static class SessionLCOTest extends ApplicationLCOTest
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** counter for constructor calls. */
        static int iCountSessionConstructor   = 0;
        /** counter for post-construct (via annotation) calls. */
        static int iCountSessionPostConstruct = 0;
        /** counter for destroy calls. */
        static int iCountSessionDestroy = 0;
        /** counter for pre-destroy (via annotation) calls. */ 
        static int iCountSessionPreDestroy = 0;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>SessionLCOTest</code>.
         */
        public SessionLCOTest()
        {
            iCountSessionConstructor++;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void destroy()
        {
            super.destroy();
            
            iCountSessionDestroy++;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Invoked after construction (not after constructor).
         */
        @PostConstruct
        public void doPostConstruct()
        {
            iCountSessionPostConstruct++;
        }
        
        /**
         * Invoked before destroying.
         */
        @PreDestroy
        private void doDestroy()
        {
            iCountSessionPreDestroy++;
        }
        
        /**
         * Invoked before destroying (test for multiple destroy methods). 
         */
        @PreDestroy
        private void doDestroy2()
        {
            iCountSessionPreDestroy++;
        }
        
        /**
         * Gets the identity hash code for this instance.
         * 
         * @return {@link System#identityHashCode(Object)}
         */
        public long getId()
        {
            return System.identityHashCode(this);
        }
        
        /**
         * Gets the identity hash code for this instance.
         * 
         * @return {@link System#identityHashCode(Object)}
         */
        public long getId2()
        {
            return System.identityHashCode(this);
        }

        /**
         * Gets the identity hash code for this instance.
         * 
         * @return {@link System#identityHashCode(Object)}
         */
        @Inherit
        public long getIdInherit()
        {
            return System.identityHashCode(this);
        }
        
    }   // SessionLCOTest

    /**
     * An (sub)session level LCO for annotations test application.
     * 
     * @author René Jahn
     */
    public static class SubLCOTest extends SessionLCOTest
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         * @return
         */
        @Override
        public long getId2()
        {
            return super.getId2();
        }

    }   // SubLCOTest
    
} 	// TestObjectProvider
