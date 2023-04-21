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
 * 19.09.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import org.junit.Assert;
import org.junit.Test;

/**
 * The <code>TestDefaultAccessController</code> tests functionality of {@link DefaultAccessController}.
 * 
 * @author René Jahn
 */
public class TestDefaultAccessController
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tests {@link DefaultAccessController#find(String)}.
     */
    @Test
    public void testFind()
    {
        DefaultAccessController dacc = new DefaultAccessController();
        
        Assert.assertNull(dacc.find(null, "Contacts"));
        
        String sLCOSession = "com.sibvisions.apps.demo.Session";
        
        dacc.addAccess(sLCOSession);
        
        Assert.assertEquals(sLCOSession, dacc.find(null, "Session"));
        Assert.assertEquals(sLCOSession, dacc.find(null, ".Session"));
        Assert.assertEquals(sLCOSession, dacc.find(null, "demo.Session"));
        Assert.assertNull(dacc.find(null, "apps.Session"));
        
        dacc.addAccess("com.sibvisions.apps.new.Session");
        
        //NOT unique
        Assert.assertNull(dacc.find(null, "Session"));
        Assert.assertNull(dacc.find(null, ".Session"));

        Assert.assertEquals(sLCOSession, dacc.find(null, "demo.Session"));
    }
    
}   // TestDefaultAccessController
