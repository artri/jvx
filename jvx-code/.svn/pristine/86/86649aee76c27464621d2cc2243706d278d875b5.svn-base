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
 * 28.08.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui;

import java.util.prefs.Preferences;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link LauncherUtil}.
 * 
 * @author René Jahn
 */
public class TestLauncherUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests setting registry key by URL.
	 * 
	 * @throws Exception if setting key fails
	 */
	@Test
	public void testSetRegistryKey() throws Exception
	{
		String sCharacters = "http://www.sibvisions.com/app/visionx/Projekt/verwaltung/ThisIsMyVeryLongApplicationName";

		sCharacters = sCharacters.replace("://", "/").replace(":", "/");
		
		LauncherUtil.setRegistryKey(sCharacters, "key", "value");
		
		Assert.assertEquals("value", LauncherUtil.getRegistryKey(sCharacters, "key"));
		
		LauncherUtil.setRegistryKey(sCharacters, "key", null);

		Assert.assertNull(LauncherUtil.getRegistryKey(sCharacters, "key"));
	}
	
	/**
	 * Tests if accessing registry key with more than 80 characters fail.
	 */
	@Test
	public void testSetLongRegistryKey()
	{
		String sName = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789-";
		
		try
		{
			//max 80 characters are allowed (tested under Windows)
			Preferences.userRoot().node(sName);
			
			Assert.fail("Key too long!");
		}
		catch (Exception e)
		{
			//ok
		}
	}
}
