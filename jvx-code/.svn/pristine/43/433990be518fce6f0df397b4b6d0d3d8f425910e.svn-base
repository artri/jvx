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
 * 14.11.2009 - [JR] - creation
 */
package com.sibvisions.rad.server.security.validation;

import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.rad.server.PreconfiguredSession;

/**
 * Tests the functionality of {@link DefaultPasswordValidator}.
 * 
 * @author René Jahn
 */
public class TestDefaultPasswordValidator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the test session. */
	private PreconfiguredSession session;
	
	/** the password validator. */
	private DefaultPasswordValidator pwval;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes each test. 
	 */
	@Before
	public void beforeTest()
	{
		session = new PreconfiguredSession();
		pwval   = new DefaultPasswordValidator();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests a new password with <code>null</code> value.
	 */
	@Test
	public void testNullPassword()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "0");

		pwval.checkPassword(session, null);
	}
	
	/**
	 * Tests the minimum length.
	 */
	@Test
	public void testMinLength()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "10");
		
		try
		{
			pwval.checkPassword(session, "one");
			
			Assert.fail("Passwor too short!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 10",
					            se.getMessage());
		}
		
		pwval.checkPassword(session, "morethantencharacters");
	}

	/**
	 * Tests digit.
	 */
	@Test
	public void testDigit()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "3");
		session.setConfigValue("/application/securitymanager/passwordvalidator/digit", "true");
		
		try
		{
			pwval.checkPassword(session, "four");
			
			Assert.fail("No digit!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 3, at least one digit",
					            se.getMessage());
		}
		
		pwval.checkPassword(session, "test1234");
	}

	/**
	 * Tests letter.
	 */
	@Test
	public void testLetter()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "3");
		session.setConfigValue("/application/securitymanager/passwordvalidator/letter", "true");
		
		try
		{
			pwval.checkPassword(session, "1234");
			
			Assert.fail("No letters!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 3, at least one letter",
					            se.getMessage());
		}
		
		pwval.checkPassword(session, "1234A");
	}
	
	/**
	 * Tests mixed case letters.
	 */
	@Test
	public void testMixedCaseLetters()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "3");
		session.setConfigValue("/application/securitymanager/passwordvalidator/letter", "true");
		session.setConfigValue("/application/securitymanager/passwordvalidator/mixedcase", "true");
		
		try
		{
			pwval.checkPassword(session, "abc");
			
			Assert.fail("No mixed case letters!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 3, at least two letters (mixed case)",
					            se.getMessage());
		}
		
		pwval.checkPassword(session, "abcABC");
	}
	
	/**
	 * Tests special character.
	 */
	@Test
	public void testSpecialChar()
	{
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "3");
		session.setConfigValue("/application/securitymanager/passwordvalidator/specialchar", "true");
		
		try
		{
			pwval.checkPassword(session, "1aA");
			
			Assert.fail("No special character!");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 3, at least one special character",
					            se.getMessage());
		}
		
		pwval.checkPassword(session, "1aA$");
	}
	
	/**
	 * Tests change password through a remote call.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testChangePassword() throws Throwable
	{
		MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		macon.setApplicationName("pwdvalidation");
		macon.setUserName("rene");
		macon.setPassword("rene123Rene.");
		
		macon.open();
		
		try
		{
			macon.setNewPassword("rene", "re");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Minimum length = 5, at least one digit, " +
					            "at least two letters (mixed case), at least one special character, " +
					            "Password is not equal to the Username",
					            se.getMessage());
		}
		
		//set a new password
		macon.setNewPassword("rene123Rene.", "A1b2.");

		//reset the password!
		macon.setNewPassword("A1b2.", "rene123Rene.");
	}

	/**
	 * Tests password not equal to the username.
	 */
	@Test
	public void testNotEqual()
	{
		session.setUserName("asdf");
		session.setConfigValue("/application/securitymanager/passwordvalidator/minlength", "0");
		session.setConfigValue("/application/securitymanager/passwordvalidator/notequaluser", "true");
		
		try
		{
			pwval.checkPassword(session, "asdf");
			
			Assert.fail("Password equals Username");
		}
		catch (SecurityException se)
		{
			Assert.assertEquals("The new password should comply with the following rules: Password is not equal to the Username",
					            se.getMessage());
		}
	}
	
	
}	// TestDefaultPasswordValidator
