/*
 * Copyright 2015 SIB Visions GmbH
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
 * 28.05.2015 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.io.File;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.remote.TestHttpConnection;
import com.sibvisions.util.ArrayUtil;

/**
 * Tests integration of {@link javax.rad.server.ICallHandler}.
 * 
 * @author René Jahn
 */
public class TestCallHandlerIntegration
{
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
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tests master session (only) events with given connection.
     * 
     * @param pConnection the connection
     * @throws Throwable if test fails
     */
    private void testMasterSessionListener(IConnection pConnection) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.SessionWithCallHandler");
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "rene");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
        
        try
        {
            pConnection.open(ciMaster);
            
            pConnection.openSub(ciMaster, ciScreen);
            
            pConnection.call(ciMaster, new String[] {null}, new String[] {"getName"}, null, null);
            pConnection.call(ciMaster, new String[] {null, null}, new String[] {"getName", "getName"}, null, null);

            pConnection.call(ciScreen, new String[] {null}, new String[] {"getInt"}, null, null);
            
            try
            {
                pConnection.call(ciMaster, new String[] {null, null, null}, new String[] {"getName", "getNameWithException", "getName"}, null, null);
                
                Assert.fail("Call didn't throw an Exception!");
            }
            catch (Exception e)
            {
                Assert.assertEquals("LCO", e.getMessage());
            }
            
            String sResult = "Before FIRST call\n" +
                             "Before call: getName\n" +
                             "After call: getName, error: false\n" +
                             "After LAST call: false\n" +
                             "invokeAfterLastCall!\n" +

                             "Before FIRST call\n" +
                             "Before call: getName\n" + 
                             "After call: getName, error: false\n" +
                             "Before call: getName\n" +
                             "After call: getName, error: false\n" +
                             "After LAST call: false\n" +
                             "invokeAfterLastCall!\n" +
                             "invokeAfterLastCall!\n" +
                                
                             "Before FIRST call\n" +
                             "Before call: getInt\n" +
                             "After call: getInt, error: false\n" + 
                             "After LAST call: false\n" +

                             "Before FIRST call\n" +
                             "Before call: getName\n" +
                             "After call: getName, error: false\n" +
                             "Before call: getNameWithException\n" +
                             "After call: getNameWithException, error: true\n" +
                             "After LAST call: true\n" +
                             "invokeAfterLastCall!\n" +
                                
                             "Before FIRST call\n" +
                             "Before call: getEvents\n" +
                             "After call: getEvents, error: false\n" +
                             "After LAST call: false";
            
            checkEvents(pConnection, ciMaster, sResult);
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }
    
    /**
     * Tests sub session events (only) with given connection.
     * 
     * @param pConnection the connection
     * @throws Throwable if test fails
     */
    private void testScreenSessionListener(IConnection pConnection) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.Session");
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "rene");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.ScreenWithCallHandler");

        ConnectionInfo ciScreenNoListener = new ConnectionInfo();
        ciScreenNoListener.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
        
        
        try
        {
            pConnection.open(ciMaster);
            pConnection.openSub(ciMaster, ciScreen);
            pConnection.openSub(ciMaster, ciScreenNoListener);
            
            pConnection.call(ciMaster, new String[] {null}, new String[] {"getInt"}, null, null);
            pConnection.call(ciMaster, new String[] {null, null}, new String[] {"getInt", "getInt"}, null, null);

            pConnection.call(ciScreen, new String[] {null}, new String[] {"getInt"}, null, null);
            pConnection.call(ciScreenNoListener, new String[] {null}, new String[] {"getInt"}, null, null);

            String sResult = "Before FIRST call (screen)\n" +
                             "Before call (screen): getInt\n" +
                             "After call (screen): getInt, error: false\n" + 
                             "After LAST call (screen): false";
            
            checkEvents(pConnection, ciMaster, sResult);
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }
    
    /**
     * Tests master and sub session events with given connection.
     * 
     * @param pConnection the connection
     * @throws Throwable if test fails
     */
    private void testMasterAndScreenSessionListener(IConnection pConnection) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.SessionWithCallHandler");
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "rene");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.ScreenSessionWithCallHandler");

        ConnectionInfo ciScreenNoListener = new ConnectionInfo();
        ciScreenNoListener.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
        
        
        try
        {
            pConnection.open(ciMaster);
            pConnection.openSub(ciMaster, ciScreen);
            pConnection.openSub(ciMaster, ciScreenNoListener);
            
            pConnection.call(ciMaster, new String[] {null}, new String[] {"getInt"}, null, null);
            pConnection.call(ciMaster, new String[] {null, null}, new String[] {"getInt", "getInt"}, null, null);

            pConnection.call(ciScreen, new String[] {null}, new String[] {"getInt"}, null, null);
            pConnection.call(ciScreenNoListener, new String[] {null}, new String[] {"getInt"}, null, null);
            
            String sResult = "Before FIRST call\n" +
                             "Before call: getInt\n" +
                             "After call: getInt, error: false\n" + 
                             "After LAST call: false\n" +
                                
                             "Before FIRST call\n" +
                             "Before call: getInt\n" +
                             "After call: getInt, error: false\n" + 
                             "Before call: getInt\n" +
                             "After call: getInt, error: false\n" +
                             "After LAST call: false\n" + 
                                
                             "Before FIRST call\n" +
                             "Before FIRST call (screen)\n" +
                             "Before call: getInt\n" + 
                             "Before call (screen): getInt\n" +
                             "After call (screen): getInt, error: false\n" +
                             "After call: getInt, error: false\n" + 
                             "After LAST call (screen): false\n" +
                             "After LAST call: false\n" +
                                
                             "Before FIRST call\n" +
                             "Before call: getInt\n" +
                             "After call: getInt, error: false\n" + 
                             "After LAST call: false\n" +

                             "Before FIRST call\n" +
                             "Before call: getEvents\n" +
                             "After call: getEvents, error: false\n" + 
                             "After LAST call: false";
            
            checkEvents(pConnection, ciMaster, sResult);
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }
    
    /**
     * Tests event order with implicite master session creation and given connection.
     * 
     * @param pConnection the connection
     * @param pWithMasterCallListener whether the implicite master should add a call listener
     * @throws Throwable if test fails
     */
    private void testImpliciteMasterSessionCreation(IConnection pConnection, boolean pWithMasterCallListener) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        
        if (pWithMasterCallListener)
        {
            ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.SessionWithCallHandler");
        }
        else
        {
            ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.Session");
        }
        
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "rene");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.ScreenSessionWithCallHandler");

        ConnectionInfo ciScreenNoListener = new ConnectionInfo();
        ciScreenNoListener.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
        
        
        try
        {
            pConnection.open(ciMaster);
            pConnection.openSub(ciMaster, ciScreen);
            pConnection.openSub(ciMaster, ciScreenNoListener);

            pConnection.call(ciScreen, new String[] {null}, new String[] {"getInt"}, null, null);
            pConnection.call(ciScreenNoListener, new String[] {null}, new String[] {"getInt"}, null, null);
            
            String sResult;
            
            if (pWithMasterCallListener)
            {
                sResult = "Before FIRST call\n" +
                          "Before FIRST call (screen)\n" +
                          "Before call: getInt\n" + 
                          "Before call (screen): getInt\n" +
                          "After call (screen): getInt, error: false\n" +
                          "After call: getInt, error: false\n" + 
                          "After LAST call (screen): false\n" +
                          "After LAST call: false\n" +
                                    
                          "Before FIRST call\n" +
                          "Before call: getInt\n" +
                          "After call: getInt, error: false\n" + 
                          "After LAST call: false\n" +
    
                          "Before FIRST call\n" +
                          "Before call: getEvents\n" +
                          "After call: getEvents, error: false\n" + 
                          "After LAST call: false";
            }
            else
            {
                sResult = "Before FIRST call (screen)\n" +
                          "Before call (screen): getInt\n" +
                          "After call (screen): getInt, error: false\n" +
                          "After LAST call (screen): false";
            }
            
            checkEvents(pConnection, ciMaster, sResult);
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }    

    /**
     * Tests call order of invoke... calls of {@link javax.rad.server.ICallHandler} with given connection.
     * 
     * @param pConnection the connection
     * @throws Throwable if test fails
     */
    private void testInvokeAfterCalls(IConnection pConnection) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.SessionWithCallHandler");
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "rene");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        try
        {
            pConnection.open(ciMaster);
            
            pConnection.call(ciMaster, new String[] {null}, new String[] {"addInvokeAfterCall"}, null, null);
            pConnection.call(ciMaster, new String[] {null, null, null}, 
                                       new String[] {"addInvokeAfterCall", "addInvokeAfterLastCall", "addInvokeFinally"}, null, null);
            
            String sResult = "Before FIRST call\n" + 
                             "Before call: addInvokeAfterCall\n" + 
                             "After call: addInvokeAfterCall, error: false\n" + 
                             "invokeAfterCall\n" + 
                             "After LAST call: false\n" + 
            
                             "Before FIRST call\n" + 
                             "Before call: addInvokeAfterCall\n" + 
                             "After call: addInvokeAfterCall, error: false\n" + 
                             "invokeAfterCall\n" + 
                             "Before call: addInvokeAfterLastCall\n" + 
                             "After call: addInvokeAfterLastCall, error: false\n" + 
                             "Before call: addInvokeFinally\n" + 
                             "After call: addInvokeFinally, error: false\n" + 
                             "After LAST call: false\n" + 
                             "invokeAfterLastCall\n" + 
                             "invokeFinally\n" + 
            
                             "Before FIRST call\n" + 
                             "Before call: getEvents\n" + 
                             "After call: getEvents, error: false\n" + 
                             "After LAST call: false";            
            
            checkEvents(pConnection, ciMaster, sResult);
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }
    
    /**
     * Tests call handler events without real object/action call.
     * 
     * @param pConnection the connection to use
     * @throws Throwable if test fails
     */
    private void testBug1550(IConnection pConnection) throws Throwable
    {
        ConnectionInfo ciMaster = new ConnectionInfo();
        ciMaster.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.SessionWithSessionCallHandler");
        ciMaster.getProperties().put(IConnectionConstants.APPLICATION, "callhandler");
        ciMaster.getProperties().put(IConnectionConstants.USERNAME, "dummy");
        ciMaster.getProperties().put(IConnectionConstants.PASSWORD, "dummy");

        ConnectionInfo ciScreen = new ConnectionInfo();
        ciScreen.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.special.Address");
        
        try
        {
            pConnection.open(ciMaster);

            String sResult = "Before FIRST call\n" +
                             "Before call: getEvents\n" +
                             "After call: getEvents, error: false\n" +
                             "After LAST call: false";
            
            checkEvents(pConnection, ciMaster, sResult);            
        }
        finally
        {
            close(pConnection, ciMaster);
        }
    }
    
    /**
     * Checks server-side event queue with expected result.
     * 
     * @param pConnection the connection
     * @param pConInfo the connection information for the remote session
     * @param pExpected the expected result
     * @throws Throwable if server-side queue and the expected result are different
     */
    private void checkEvents(IConnection pConnection, ConnectionInfo pConInfo, String pExpected) throws Throwable
    {
        ArrayUtil<String> liEvents = (ArrayUtil<String>)pConnection.call(pConInfo, new String[] {null}, new String[] {"getEvents"}, null, null)[0];

        StringBuilder sbEvents = new StringBuilder();
        
        for (String sEvent : liEvents)
        {
            if (sbEvents.length() > 0)
            {
                sbEvents.append("\n");
            }
            
            sbEvents.append(sEvent);
        }
        
        Assert.assertEquals(pExpected, sbEvents.toString());
    }
    
    /**
     * Closes the given connection.
     * 
     * @param pConnection the connection
     * @param pInfo the connection info
     */
    private void close(IConnection pConnection, ConnectionInfo pInfo)
    {
        try
        {
            pConnection.close(pInfo);
        }
        catch (Throwable th)
        {
            //ignore
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests call events with global (= master session) listener.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testMasterSessionListener() throws Throwable
    {
        testMasterSessionListener(new DirectServerConnection());
        testMasterSessionListener(TestHttpConnection.createHttpConnection());
    }
  
    /**
     * Tests call events with screen (= sub session) listener.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testScreenSessionListener() throws Throwable
    {
        testScreenSessionListener(new DirectServerConnection());
        testScreenSessionListener(TestHttpConnection.createHttpConnection());
    }
    
    /**
     * Tests call events with global (= master session) and screen (= sub session) 
     * listener.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testMasterAndScreenSessionListener() throws Throwable
    {
        testMasterAndScreenSessionListener(new DirectServerConnection());
        testMasterAndScreenSessionListener(TestHttpConnection.createHttpConnection());
    }

    /**
     * Tests call events with screen listener and implicite created master LCO, because
     * we don't send a call for the master session.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testImpliciteMasterSessionCreation() throws Throwable
    {
        testImpliciteMasterSessionCreation(new DirectServerConnection(), true);
        testImpliciteMasterSessionCreation(TestHttpConnection.createHttpConnection(), true);

        testImpliciteMasterSessionCreation(new DirectServerConnection(), false);
        testImpliciteMasterSessionCreation(TestHttpConnection.createHttpConnection(), false);
    }

    /**
     * Tests {@link javax.rad.server.ICallHandler} invoke... methods.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testInvokeAfterCalls() throws Throwable
    {
        testInvokeAfterCalls(new DirectServerConnection());
        testInvokeAfterCalls(TestHttpConnection.createHttpConnection());
    }
    
    /**
     * Tests https://oss.sibvisions.com/index.php?do=details&task_id=1550.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testBug1550() throws Throwable
    {
        testBug1550(new DirectServerConnection());
        testBug1550(TestHttpConnection.createHttpConnection());
    }
    
}   // TestCallHandlerIntegration
