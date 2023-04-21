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
 */
package com.sibvisions.rad.server.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests the {@link UpToDateConfigFile} methods.
 * 
 * @author René Jahn
 * @see UpToDateConfigFile
 */
public class TestUpToDateConfigFile
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
        System.clearProperty(IPackageSetup.CONFIG_BASEDIR);

        //use a "wrong" basedir
        Configuration.clearCache();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the constructors of {@link UpToDateConfigFile}.
	 * 
	 * @throws Throwable if the test fails 
	 */
	@Test
	public void testConstructor() throws Throwable
	{
		new UpToDateConfigFile(Configuration.getApplicationsDir(), "demo/config.xml");
		new UpToDateConfigFile(new File(Configuration.getApplicationsDir(), "demo/config.xml"));
	}

	/**
	 * Tests the property access with {@link UpToDateConfigFile#getProperty(String)} and 
	 * {@link UpToDateConfigFile#getProperties(String)}.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testProperties() throws Throwable
	{
		UpToDateConfigFile config = new UpToDateConfigFile(Configuration.getApplicationsDir(), "demo/config.xml");
		
		config.getProperty("/application/securitymanager/class");
		config.getProperties("/application/securitymanager/class");
	}
	
	/**
	 * Tests the replacement of the XML nodes and property place holders with
	 * imported property files.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testIncludes() throws Throwable
	{
		System.setProperty("testproperty", "sysproperty");
		
		UpToDateConfigFile config = new UpToDateConfigFile(ResourceUtil.getFileForClass("/config_import.xml"));
		config.setSaveImmediate(false);
		
		Assert.assertEquals("securitymanager", config.getProperty("/application/securitymanager/class"));
		Assert.assertEquals("accesscontroller", config.getProperty("/application/securitymanager/accesscontroller"));
		Assert.assertEquals("impvalue", config.getProperty("/application/property/value"));
		Assert.assertEquals("impvalue", config.getProperty("/application/testproperty"));
		Assert.assertEquals("impvalue2", config.getProperty("/application/testproperty2"));
		Assert.assertEquals("impvalue2_concat_sysproperty", config.getProperty("/application/partialproperty"));
		Assert.assertEquals("${invalid", config.getProperty("/application/invalid"));
		
		config.setProperty("/application/property/value", "change");
		
		try
		{
			config.save();
			Assert.fail("No exception thrown");
		}
		catch (Exception exc)
		{
			// Do nothing
		}
	}
	
	/**
	 * Tests the include tag with index in imported properties.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testIncludeWithIndex() throws Throwable
	{
        UpToDateConfigFile cfgInput = new UpToDateConfigFile(ResourceUtil.getFileForClass("/config_import_index.xml"));
        UpToDateConfigFile cfgResult = new UpToDateConfigFile(ResourceUtil.getFileForClass("/config_import_index.xml"));
        
        
        Assert.assertEquals(cfgInput.getNode("/application").toString(), cfgResult.getNode("/application").toString());
	}

	/**
	 * Tests value replacement without include tag. It's not possible to use placeholders without include
	 * tags.
	 * 
	 * @throws Throwable if test fails
	 */
    @Test
    public void testReplaceWithoutInclude() throws Throwable
    {
        UpToDateConfigFile cfgInput = new UpToDateConfigFile(ResourceUtil.getFileForClass("/config_replace.xml"));
        
        Assert.assertEquals("${classname}", cfgInput.getProperty("/application/replace"));
    }
	
}	// TestUpToDateConfigFile
