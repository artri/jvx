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
 * 12.12.2008 - [JR] - testIsUpToDate
 */
package com.sibvisions.rad.server.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.config.Configuration.ApplicationListOption;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * Tests the Configuration methods.
 * 
 * @author René Jahn
 * @see Configuration
 */
public class TestConfiguration
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
		System.setProperty(IPackageSetup.CONFIG_BASEDIR, "");
		System.setProperty(IPackageSetup.CONFIG_SEARCH_CLASSPATH, "false");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	   /**
     * Creates an external application config file.
     * 
     * @throws IOException if the file can not be written.
     */
    private void createExternalApplicationConfig() throws IOException
    {
        //-------------------------------------------------
        //create external application directory
        //-------------------------------------------------
        
        File fiTmpApps = new File(System.getProperty("java.io.tmpdir"), "/apps/");
        
        FileUtil.delete(fiTmpApps);
        
        fiTmpApps.mkdirs();
        
        File fiExternal = new File(fiTmpApps, "external");
        fiExternal.mkdir();

        //-------------------------------------------------
        //create dummy config.xml
        //-------------------------------------------------
        
        File fiConfig = new File(fiExternal, "config.xml");

        XmlNode ndConfig = new XmlNode("application");
        ndConfig.setNode("/securitymanager/class", "com.sibvisions.rad.server.security.XmlSecurityManager");
        ndConfig.setNode("/securitymanager/passwordalgorithm", "SHA-256");

        XmlWorker xmw = new XmlWorker();
        xmw.write(fiConfig, ndConfig);
    }

    /**
     * Deletes the external application.
     */
    private void deleteExternalApplication()
    {
        File fiTmpApps = new File(System.getProperty("java.io.tmpdir"), "/apps/");
        
        FileUtil.delete(fiTmpApps);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests to get directory locations form the configuration.
	 */
	@Test
	public void testDirectories()
	{
		//production check
		String sDir = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(Configuration.class))).getParent();

		
		Assert.assertEquals(new File(sDir, "rad"), Configuration.getConfigurationDir());
		Assert.assertEquals(new File(sDir, "rad/apps"), Configuration.getApplicationsDir());
		

		//development check
		String sTemp = System.getProperty("java.io.tmpdir");

		System.setProperty(IPackageSetup.CONFIG_BASEDIR, sTemp);
		
		new File(sTemp, "rad/apps").mkdirs();
		
		Assert.assertEquals(new File(sTemp, "rad"), Configuration.getConfigurationDir());
		Assert.assertEquals(new File(sTemp, "rad/apps"), Configuration.getApplicationsDir());
	}
	
	/**
	 * Tests to get an application zone.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testApplicationZone() throws Throwable
	{
		//existing application
		Configuration.getApplicationZone("demo");	

		//unknown application
		try
		{
			Configuration.getApplicationZone("unknown");	
	
			Assert.fail("Application \"unknown\" found!");
		}
		catch (Exception be)
		{
			Assert.assertTrue(be.getMessage(), be.getMessage().startsWith("Invalid configuration file: "));
		}
		
		//Invalid application
		try
		{
			Configuration.getApplicationZone("");	
	
			Assert.fail("Application \"\" found!");
		}
		catch (Exception be)
		{
			Assert.assertTrue(be.getMessage(), be.getMessage().startsWith("Invalid configuration file: "));
		}

		// Dummy Zone erstellen, einlesen, und dann löschen und nochmal drauf zugreifen -> Exception muss folgen!
		
		File fiApps = Configuration.getApplicationsDir();
		
		File fiDummy = new File(fiApps, "dummy");
		
		fiDummy.mkdirs();
		
		File fiConfig = new File(fiDummy, "config.xml");
		
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(fiConfig);
			
			fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<application>\n</application>".getBytes());
		}
		finally
		{
			if (fos != null)
			{
				fos.close();
			}
		}
		
		Configuration.getApplicationZone("dummy");
		
		fiConfig.delete();
		fiDummy.delete();

		try
		{
			Configuration.getApplicationZone("dummy");
			
			Assert.fail("Application found!");
		}
		catch (Exception be)
		{
			Assert.assertTrue(be.getMessage(), be.getMessage().startsWith("Invalid configuration file: "));
		}
	}
	
	/**
	 * Tests to get the server zone.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testServerZone() throws Throwable
	{
		Zone zone = Configuration.getServerZone();
		
		Assert.assertEquals("360", zone.getProperty("/server/timeout/mastersession"));
	}

	/**
	 * Tests accessing an application which is stored in an external apps folder.
	 * 
	 * @throws Throwable if accessing application fails
	 */
	@Test
	public void testExternalApplication() throws Throwable
	{
		createExternalApplicationConfig();
		
		try
		{
    		ApplicationZone appzone = Configuration.getApplicationZone("external");
    		
    		Assert.assertNotNull(appzone);
		}
		finally
		{
		    deleteExternalApplication();
		}
	}

	/**
	 * Tests {@link Configuration#listApplicationNames(ApplicationListOption)} names. 
	 * This test checks usage of external app folders.
	 */
	@Test
	public void testListApplicationNames()
	{
		List<String> liApps = Configuration.listApplicationNames(ApplicationListOption.All);
		
		List<String> liExpectedApplicationNames = Arrays.asList("annotations", "callhandler", "datasources", "defaultdatasource", 
		                                                        "demo", "democopy", "invalid", "mfa", "mfa_multiwait", "mfa_wait", "mfa_url",
		                                                        "plugin", "propertytransfer", "pwdencryption", "pwdvalidation", "xmlusers");
		
		assertEqualIgnoreOrder(liExpectedApplicationNames, liApps);
	}
	
	/**
	 * Asserts that two lists are the same regardless of sort order of contained elements.
	 * @param <T> type of list must be comparable to be sorted.
	 * @param pExpected the list of expected values
	 * @param pActual the list actual values 
	 */
	private <T extends Comparable<T>> void assertEqualIgnoreOrder(List<T> pExpected, List<T> pActual)
	{
		Collections.sort(pExpected);
		Collections.sort(pActual);
		
		Assert.assertEquals(pExpected, pActual);
	}

	/**
	 * Tests whether an application is valid. This test checks external apps.
	 * 
	 * @throws IOException if the external config file can not be written.
	 */
	@Test
	public void testIsApplication() throws IOException
	{
		createExternalApplicationConfig();

		try
		{
    		Assert.assertFalse("Found application that is not an application!", Configuration.isApplication("noapp"));
    		Assert.assertTrue("Demo application not found!", Configuration.isApplication("demo"));
    		Assert.assertTrue("External application not found!", Configuration.isApplication("external"));
        }
        finally
        {
            deleteExternalApplication();
        }
	}
	
} 	// TestConfiguration
