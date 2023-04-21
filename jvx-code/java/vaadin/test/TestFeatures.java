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
 * 23.08.2014 - [JR] - creation
 */
import java.net.URI;

import org.junit.Test;

/**
 * Tests URL redirect features.
 * 
 * @author René Jahn
 */
public class TestFeatures
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tests URL splitting.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testURLSplit() throws Exception
    {
        String sRedirectURL = "http://www.server.com:8080/redirect/to/place/web/guest/index.html";
        String sOriginalURL = "http://www.server.com:8080/web/guest/index.html";
        
        URI uriRedirect = new URI(sRedirectURL);
        URI uriOriginal = new URI(sOriginalURL);
        
        String sRedirectPath = uriRedirect.getPath();
        String sOriginalPath = uriOriginal.getPath();
        
        int iPos = sRedirectPath.indexOf(sOriginalPath);
        
        if (iPos > 0)
        {
            System.out.println(sRedirectPath.subSequence(0, iPos));
        }
    }
    
}   // TestFeatures
