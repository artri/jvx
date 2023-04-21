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
 * 29.01.2011 - [JR] - creation
 */
package javax.rad.remote;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.DirectServerConnection;

/**
 * Tests the functionality of {@link SubConnection}.
 * 
 * @author René Jahn
 */
public class TestSubConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Apply the configuration for all tests.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		//Important for coverage tool, because the coverage toole uses another classes directory
		//as the directory configured for eclipse
		System.setProperty(IPackageSetup.CONFIG_BASEDIR, new File("").getAbsolutePath());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests ticket #test207. Use compression property from master connection.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void test207() throws Throwable
	{
		DirectServerConnection scon = new DirectServerConnection();
		
		MasterConnection macon = new MasterConnection(scon);
		macon.setApplicationName("xmlusers");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		Assert.assertEquals("true", macon.getProperty(IConnectionConstants.COMPRESSION));
		
		macon.setProperty(IConnectionConstants.COMPRESSION, "false");
		
		Assert.assertEquals("false", (String)macon.getProperty(IConnectionConstants.COMPRESSION));

		macon.open();

		
		//constructor sets compression option with value from master connection!
		SubConnection subcon = macon.createSubConnection(null);
		
		Assert.assertEquals("false", (String)subcon.getProperty(IConnectionConstants.COMPRESSION));

		subcon.open();
	}
	
}	// TestSubConnection
