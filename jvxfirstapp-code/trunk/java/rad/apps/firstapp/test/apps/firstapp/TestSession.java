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
 * 29.10.2013 - [JR] - creation
 */
package apps.firstapp;

import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.server.DirectServerConnection;

/**
 * Tests {@link Session}.
 * 
 * @author René Jahn
 */
public class TestSession
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests a simple action call.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testGetApplicationName() throws Throwable
	{
		String sApplicationName = "firstapp";
		
		DirectServerConnection dscon = new DirectServerConnection();
		
		MasterConnection macon = new MasterConnection(dscon);
		macon.setApplicationName(sApplicationName);
		macon.setUserName("admin");
		macon.setPassword("admin");
		macon.open();
		
		String sCheckName = (String)macon.callAction("getApplicationName");

		Assert.assertEquals(sApplicationName, sCheckName);
	}

}	// TestSession
