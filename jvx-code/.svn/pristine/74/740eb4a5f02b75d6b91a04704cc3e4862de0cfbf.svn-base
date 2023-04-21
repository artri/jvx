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
 * 12.11.2014 - [JR] - creation
 */
package com.sibvisions.rad.remote;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.SessionExpiredException;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.http.HttpConnection;

/**
 * Tests retry feature of {@link AbstractSerializedConnection}.
 * 
 * @author René Jahn
 */
public class TestCommunicationRetry
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests a multiple call within a single request and without exception.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testCommunicationId() throws Throwable
    {
        Throwable thError = null;
        
        HttpConnection conDemo = new HttpConnection("http://localhost/JVx.Server/services/Server");
        conDemo.setConnectionTimeout(2000);

        ConnectionInfo coninfo = new ConnectionInfo();
        

        coninfo.getProperties().put(IConnectionConstants.APPLICATION, "demo");
        coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
        coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        try
        {
            conDemo.open(coninfo);
                
            for (int i = 1; i < 20; i++)
            {
                try
                {
                    System.out.println("START: " + i);
                    
                    String sResult = (String)conDemo.call
                    (
                        coninfo,
                        new String[] {null},
                        new String[] {"delayedAction"},
                        new Object[][] {new Long[] {Long.valueOf(15000)}},
                        null
                    )[0];
                    
                    System.out.println("DONE: " + i + ", " + sResult);                
                    
                    Assert.assertEquals("finished", sResult);
                }
                catch (SessionExpiredException se)
                {
                    //Session expired is allowed, e.g. if we restart the server

                    try
                    {
                        conDemo.close(coninfo);
                    }
                    catch (Throwable th)
                    {
                        //ignore
                    }
                    
                    conDemo.open(coninfo);
                }
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
    
}   // TestCommunicationRetry
