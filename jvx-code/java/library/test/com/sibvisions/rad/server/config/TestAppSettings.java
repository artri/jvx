/*
 * Copyright 2013 SIB Visions GmbH
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
 * 16.06.2013 - [JR] - creation
 */
package com.sibvisions.rad.server.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;

/**
 * Tests the {@link ApplicationZone} methods.
 * 
 * @author René Jahn
 */
public class TestAppSettings
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
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link AppSettings} initialization with and without configuration file.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testNoXml() throws Throwable
	{
		File fiAppsSav = new File(Configuration.getConfigurationDir(), "apps.xml.sav");
		File fiApps = new File(Configuration.getConfigurationDir(), "apps.xml");
		
		Assert.assertTrue("Rename apps.xml not possible!", fiApps.renameTo(fiAppsSav));
		
		try
		{
			AppSettings aset = new AppSettings(Configuration.getConfigurationDir());

			Assert.assertNull(aset.getAppLocations());
		}
		finally
		{
			fiAppsSav.renameTo(fiApps);
		}
		
		AppSettings aset = new AppSettings(Configuration.getConfigurationDir());

		Assert.assertEquals(1, aset.getAppLocations().size());
	}	

	/**
	 * Tests system property replacement.
	 * 
	 * @throws Throwable if reading config fails
	 */
	@Test
	public void testReplace() throws Throwable
	{
		String sTempDir = System.getProperty("java.io.tmpdir");
		
		AppSettings aset = new AppSettings(Configuration.getConfigurationDir());
		
		Assert.assertEquals(sTempDir + "/apps/", aset.getAppLocations().get(0).getPath());
	}
	
} 	// TestAppSettings
