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
 * 25.09.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.engine.util.Base64;
import org.restlet.representation.InputRepresentation;

import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests functionality of {@link JSONUtil}.
 * 
 * @author René Jahn
 */
public class TestJSONUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tests JSON deserialization.
     * 
     * @throws Exception if deserialization fails
     */
    @Test
    public void testBinary() throws Exception
    {
        long lNow = System.currentTimeMillis();
        
        InputRepresentation irep = new InputRepresentation(ResourceUtil.getResourceAsStream("/com/sibvisions/rad/server/http/rest/json_binary.txt"));
        
        HashMap hmp = (HashMap)JSONUtil.getObject(irep);
        
        Base64.decode((String)((HashMap)((ArrayList)hmp.get("newCells")).get(1)).get("value"));
        
        //max 2 seconds
        Assert.assertTrue(System.currentTimeMillis() - lNow < 2000);
    }
    
}   // TestJSONUtil
