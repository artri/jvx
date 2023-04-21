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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of {@link MD4MessageDigest}.
 * 
 * @author Thomas Krautinger
 */
public class TestMD4MessageDigest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests strings.
	 */
	@Test
	public void testStrings()
	{
		MD4MessageDigest md4 = new MD4MessageDigest();
		
		//test empty message
		md4.update(new byte[]{});
		
		byte[] mdResult = new byte[]{(byte)0x31, (byte)0xd6, (byte)0xcf, (byte)0xe0, (byte)0xd1, (byte)0x6a, (byte)0xe9, (byte)0x31, 
				                     (byte)0xb7, (byte)0x3c, (byte)0x59, (byte)0xd7, (byte)0xe0, (byte)0xc0, (byte)0x89, (byte)0xc0};
		
		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "a"
		md4.update("a".getBytes());
		
		mdResult = new byte[]{(byte)0xbd, (byte)0xe5, (byte)0x2c, (byte)0xb3, (byte)0x1d, (byte)0xe3, (byte)0x3e, (byte)0x46, 
							  (byte)0x24, (byte)0x5e, (byte)0x05, (byte)0xfb, (byte)0xdb, (byte)0xd6, (byte)0xfb, (byte)0x24};

		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "abc"
		md4.update("abc".getBytes());
		
		mdResult = new byte[]{(byte)0xa4, (byte)0x48, (byte)0x01, (byte)0x7a, (byte)0xaf, (byte)0x21, (byte)0xd8, (byte)0x52, 
							  (byte)0x5f, (byte)0xc1, (byte)0x0a, (byte)0xe8, (byte)0x7a, (byte)0xa6, (byte)0x72, (byte)0x9d};

		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "message digest"
		md4.update("message digest".getBytes());
		
		mdResult = new byte[]{(byte)0xd9, (byte)0x13, (byte)0x0a, (byte)0x81, (byte)0x64, (byte)0x54, (byte)0x9f, (byte)0xe8, 
							  (byte)0x18, (byte)0x87, (byte)0x48, (byte)0x06, (byte)0xe1, (byte)0xc7, (byte)0x01, (byte)0x4b};

		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "abcdefghijklmnopqrstuvwxyz"
		md4.update("abcdefghijklmnopqrstuvwxyz".getBytes());
		
		mdResult = new byte[]{(byte)0xd7, (byte)0x9e, (byte)0x1c, (byte)0x30, (byte)0x8a, (byte)0xa5, (byte)0xbb, (byte)0xcd, 
							  (byte)0xee, (byte)0xa8, (byte)0xed, (byte)0x63, (byte)0xdf, (byte)0x41, (byte)0x2d, (byte)0xa9};

		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
		md4.update("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".getBytes());
		
		mdResult = new byte[]{(byte)0x04, (byte)0x3f, (byte)0x85, (byte)0x82, (byte)0xf2, (byte)0x41, (byte)0xdb, (byte)0x35, 
							  (byte)0x1c, (byte)0xe6, (byte)0x27, (byte)0xe1, (byte)0x53, (byte)0xe7, (byte)0xf0, (byte)0xe4};

		Assert.assertArrayEquals(mdResult, md4.digest());
		
		//test the string "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
		md4.update("12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes());
		
		mdResult = new byte[]{(byte)0xe3, (byte)0x3b, (byte)0x4d, (byte)0xdc, (byte)0x9c, (byte)0x38, (byte)0xf2, (byte)0x19, 
							  (byte)0x9c, (byte)0x3e, (byte)0x7b, (byte)0x16, (byte)0x4f, (byte)0xcc, (byte)0x05, (byte)0x36};

		Assert.assertArrayEquals(mdResult, md4.digest());		
	}
	
	/**
	 * Tests strings with offset.
	 */
	@Test
	public void testStringWithOffset()
	{
		MD4MessageDigest md4 = new MD4MessageDigest();
		
		byte[] content = "12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes();
		byte[] buffer = new byte[content.length + 20]; //append 10 bytes before and after the content
		Arrays.fill(buffer, (byte)0xA); //fill the buffer with other values then 0
		System.arraycopy(content, 0, buffer, 10, content.length);
		
		md4.update(buffer, 10, content.length);
		
		byte []mdResult = new byte[]{(byte)0xe3, (byte)0x3b, (byte)0x4d, (byte)0xdc, (byte)0x9c, (byte)0x38, (byte)0xf2, (byte)0x19, 
							         (byte)0x9c, (byte)0x3e, (byte)0x7b, (byte)0x16, (byte)0x4f, (byte)0xcc, (byte)0x05, (byte)0x36};

		Assert.assertArrayEquals(mdResult, md4.digest());
	}
	
}	// TestMD4MessageDigest

