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
 * 02.10.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.config;

import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.fileupload.FileItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.naming.ContextFactory;
import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests the Configuration methods without explicit basedir.
 * 
 * @author René Jahn
 * @see Configuration
 */
public class TestPlainConfiguration
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Reset configuration before each test.
     */
    @Before
    public void beforeTest()
    {
        //use a "wrong" basedir
        System.setProperty(IPackageSetup.CONFIG_BASEDIR, ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(FileItem.class)));
        System.setProperty(IPackageSetup.CONFIG_SEARCH_CLASSPATH, "true");
        
        Configuration.clearCache();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link Configuration#getServerZone()}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testServerZone() throws Exception
	{
	    //config should be found in classpath
	    ServerZone zone = Configuration.getServerZone();
	    
	    Assert.assertTrue(zone.isStream());
	    Assert.assertTrue(zone.isVirtual());
	    Assert.assertNull(zone.getFile());
	    
	    Assert.assertEquals("UNDEFINED", zone.getProperty("/server/globalmetadatacache"));
	}

    /**
     * Tests {@link Configuration#getApplicationZone(String)}.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testApplicationZone() throws Exception
    {
        //config should be found in classpath
        ApplicationZone zone = Configuration.getApplicationZone("testapp");
  
        Assert.assertTrue(zone.isStream());
        Assert.assertTrue(zone.isVirtual());
        Assert.assertNull(zone.getFile());

        Assert.assertEquals("com.sibvisions.rad.server.security.XmlSecurityManager", zone.getProperty("/application/securitymanager/class"));

        //Check correts "implicit" Server zone access
        Assert.assertEquals("UNDEFINED", zone.getProperty("/application/globalmetadatacache"));
        
        //no app directory was used, but config was found in classpath (only useful for "standalone" application
        zone = Configuration.getApplicationZone("notavalidapp");
        
        Assert.assertTrue(zone.isStream());
        Assert.assertTrue(zone.isVirtual());
        Assert.assertNull(zone.getFile());

        Assert.assertEquals("UNDEFINED", zone.getProperty("/application/securitymanager/class"));
    }
    
    /**
     * Tests zone access with JNDI configurations.
     * 
     * @throws Exception if accessing JNDI resources fails
     */
    @Test
    public void testJNDI() throws Exception
    {
        System.setProperty(IPackageSetup.CONFIG_SEARCH_CLASSPATH, "false");

        //Setup temporary context
        
        String sOldProp = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, ContextFactory.class.getName());

        try
        {
            InitialContext ctxt = new InitialContext();
            
            //Check DBAccess detection
    
            ctxt.bind("java:/comp/env/jvx/server/config", new IVirtualZone()
            {
                public InputStream getConfiguration()
                {
                    return ResourceUtil.getResourceAsStream("/jndi_server.xml");
                }
            });
            ctxt.bind("java:/comp/env/jvx/myjndiapp/config", "/jndi_config.xml");
            
            ApplicationZone zone = Configuration.getApplicationZone("myjndiapp");
            
            Assert.assertEquals("JNDI_APP", zone.getProperty("/application/securitymanager/class"));
            
            ServerZone szone = Configuration.getServerZone();
            
            Assert.assertEquals("JNDI_SERVER", szone.getProperty("/server/globalmetadatacache"));
        }
        finally
        {
            if (sOldProp == null)
            {
                System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
            }
            else
            {
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY, sOldProp);
            }
        }
    }
	
} 	// TestPlainConfiguration
