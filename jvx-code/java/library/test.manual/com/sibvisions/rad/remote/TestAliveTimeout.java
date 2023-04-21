/*
 * Copyright 2017 SIB Visions GmbH
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
 * 21.12.2017 - [JR] - creation
 */
package com.sibvisions.rad.remote;

import javax.rad.remote.IConnection;
import javax.rad.remote.MasterConnection;

import org.junit.Test;

import com.sibvisions.rad.server.AbstractSession;

/**
 * Tests alive timeout of {@link AbstractSession}.
 * 
 * @author René Jahn
 */
public class TestAliveTimeout
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests alive check on server-side. This methods needs configuration in server/config.xml.
     * 
     * @throws Throwable if test fails
     */
    @Test
    public void testAliveCheck() throws Throwable
    {
        IConnection con = TestHttpConnection.createHttpConnection();
        
        MasterConnection appcon = new MasterConnection(con);
        
        appcon.setApplicationName("demo");
        appcon.setUserName("rene");
        appcon.setPassword("rene");

        appcon.open();
    }
    
}   // TestAliveTimeout
