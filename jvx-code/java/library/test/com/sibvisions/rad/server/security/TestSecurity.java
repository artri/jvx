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
 * 06.06.2010 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.server.DirectServerConnection;

/**
 * Tests common security cases without a specific security manager. 
 * 
 * @author René Jahn
 */
public class TestSecurity
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests authentication with password encryption.
	 * 
	 * @throws Throwable if the authentication fails
	 */
	@Test
	public void testPasswordEncryption() throws Throwable
	{
		/*
			//Creates the test file
			  
			XmlNode node = new XmlNode("users");
			
			XmlNode ndUser = new XmlNode("user");
			ndUser.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "rene"));
			ndUser.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "password", "rene"));
			
			node.add(ndUser);
			
			XmlNode ndUser2 = new XmlNode("user");
			ndUser2.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "enc"));
			ndUser2.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "password", ((char)127) + SecureHash.getHash("MD5", "my$Encrypted.Pa$$word".getBytes())));
			
			node.add(ndUser2);
			
			XmlWorker work = new XmlWorker();
			work.write(new FileOutputStream("C:\\users.xml"), node);
		*/
		
		MasterConnection macon = new MasterConnection(new DirectServerConnection());

		//First user (plain text password)
		macon.setApplicationName("pwdencryption");
		macon.setUserName("rene");
		macon.setPassword("rene");
		
		macon.open();
		macon.close();
		
		//second user (encrypted password)
		macon.setUserName("enc");
		macon.setPassword("my$Encrypted.Pa$$word");
		
		macon.open();
		macon.close();
		
		//try the encrypted password
		macon.setPassword("#3837643931303136343532373964333332623636643733636263323634663564#");
		
		try
		{
			macon.open();
			
			Assert.fail("Connection opened with invalid password!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("Invalid password for 'enc' and application 'pwdencryption'", se.getMessage());
		}
	}
	
}	// TestSecurity
