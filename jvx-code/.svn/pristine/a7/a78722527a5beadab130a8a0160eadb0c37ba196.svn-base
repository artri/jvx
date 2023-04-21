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
 * 14.11.2009 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.ICallBackListener;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.BaseConnectionTest;
import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.util.ArrayUtil;

/**
 * Tests the functionality of {@link DirectServerConnection}.
 * 
 * @author René Jahn
 */
public class TestDirectServerConnection extends BaseConnectionTest
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
		return new DirectServerConnection();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests if call info is added only once.
	 * (see https://oss.sibvisions.com/index.php?do=details&task_id=1555)
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testCallInfo() throws Throwable
	{
        Throwable thError = null;
        
        final IConnection con = createConnection();
        final ArrayUtil<Throwable> liResult = new ArrayUtil<Throwable>();
        
        ConnectionInfo coninfo = new ConnectionInfo();
        
        
        coninfo.getProperties().put(IConnectionConstants.APPLICATION, "xmlusers");
        coninfo.getProperties().put(IConnectionConstants.LIFECYCLENAME, "demo.Session");
        coninfo.getProperties().put(IConnectionConstants.USERNAME, "rene");
        coninfo.getProperties().put(IConnectionConstants.PASSWORD, "rene");

        try
        {
            con.open(coninfo);
            
            con.call(coninfo, new String[] {null}, new String[] {"immediateException"}, null, new ICallBackListener[] {new ICallBackListener()
            {
                public void callBack(CallBackEvent pEvent)
                {
                    try
                    {
                        pEvent.getObject();
                        
                        Assert.fail("Exception expected!");
                    }
                    catch (Throwable th)
                    {
                        liResult.add(th);
                    }
                    finally
                    {
                        synchronized (con)
                        {
                            con.notify();
                        }
                    }
                }
            } });
            
            synchronized (con)
            {
                con.wait();
            }

            try
            {
                con.call(coninfo, new String[] {null}, new String[] {"immediateException"}, null, null);
            }
            catch (Throwable th)
            {
                liResult.add(th);
            }
            
            Assert.assertEquals(2, liResult.size());

            for (int i = 0, cnt = liResult.size(); i < cnt; i++)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                
                liResult.get(i).printStackTrace(pw);
                
                String sStack = sw.toString();
                int iPos = sStack.indexOf("at demo.Session.<null>.immediateException(Unknown Source)");
                
                Assert.assertTrue(iPos > 0);
                
                Assert.assertEquals("CallInfo added multiple times [Exception# " + i + "]!", 
                                    -1, sStack.indexOf("at demo.Session.<null>.immediateException(Unknown Source)", iPos + 20));
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
     * Tests basic server-side callback functionality without alive thread.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testCallBackResultTicket25WithPush() throws Throwable
    {
        callBackResultTicket25(false);
    }
	
}	// TestDirectServerConnection
