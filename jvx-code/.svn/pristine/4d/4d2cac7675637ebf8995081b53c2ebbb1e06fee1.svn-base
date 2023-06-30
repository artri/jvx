/*
 * Copyright 2012 SIB Visions GmbH
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
 * 08.07.2012 - [TK] - creation
 */
package com.sibvisions.util.security;

import java.net.URL;
import java.net.URLConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Tests the functionality of {@link SSLProvider}.
 * 
 * @author Martin Handsteiner
 */
public class TestSSLProvider
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests strings.
	 */
	@Test
	public void testBadSSL()
	{
	    try
	    {
            LoggerFactory.getInstance(SSLProvider.class.getName()).setLevel(LogLevel.ALL);

            SSLProvider.init();
	        
            URL urlWrongHost = new URL("https://wrong.host.badssl.com/");

            try
	        {
                URLConnection conn = urlWrongHost.openConnection();
	            
	            conn.getInputStream();

	            Assert.fail("Bad ssl but no exception!");
	        }
	        catch (Exception ex)
	        {
	            // Ok, because certificate is bad!
	            
	            SSLProvider.addHostToWhiteList("*.badssl.com");
	            
	            URLConnection conn = urlWrongHost.openConnection();

                conn.getInputStream();
	        }

            SSLProvider.removeAllHostsFromWhiteList();
            
            URL urlSelfSigned = new URL("https://self-signed.badssl.com/");

            try
            {
                URLConnection conn = urlSelfSigned.openConnection();
                
                conn.getInputStream();

                Assert.fail("Bad ssl but no exception!");
            }
            catch (Exception ex)
            {
                // Ok, because certificate is bad!
                
                SSLProvider.addHostToWhiteList("*.badssl.com");
                
                URLConnection conn = urlSelfSigned.openConnection();

                conn.getInputStream();
            }

	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    finally
	    {
	        SSLProvider.restore();
	    }
	}
	
	
}	// TestMD4MessageDigest

