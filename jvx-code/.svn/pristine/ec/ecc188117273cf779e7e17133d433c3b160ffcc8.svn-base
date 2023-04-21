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
package com.sibvisions.util;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests the functionality of {@link SecureHash}.
 * 
 * @author René Jahn
 */
public class TestSecureHash
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link SecureHash#getHash(String, byte[])}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testStaticHash() throws Exception
	{
		Assert.assertEquals("b3ed2e824a55286bb4f24e2f4b670f7f", 
				            SecureHash.getHash(SecureHash.MD5, "ThisIsMyTestMessage$1.0".getBytes()));
		Assert.assertEquals("7278d011ee6502952d43452ba4937d5cdcece621", 
				            SecureHash.getHash(SecureHash.SHA, "ThisIsMyTestMessage$1.0".getBytes()));
		Assert.assertEquals("8bba9160b1dc173d928cfccfcb1f857e8b4b150d031447ae546366b9a056ff15", 
				            SecureHash.getHash(SecureHash.SHA_256, "ThisIsMyTestMessage$1.0".getBytes()));
		Assert.assertEquals("1ad48b83be0f635c6af75f85af162c584abb44a538aa97c9ca12a8d33bfc822c10bcb2e33ccf2b89fe22dc8f1fa33672", 
				            SecureHash.getHash(SecureHash.SHA_384, "ThisIsMyTestMessage$1.0".getBytes()));
		Assert.assertEquals("24bc93fd4581f8e5b3b88e655b1259eb527964de2dbc7e2baa7bf3ca290ac50640913662a19994c7e7aca25c8f85b92b56c3a393d63207dd1b4cbc5ec70f69bd", 
				            SecureHash.getHash(SecureHash.SHA_512, "ThisIsMyTestMessage$1.0".getBytes()));
		
		Assert.assertEquals("b3ed2e824a55286bb4f24e2f4b670f7f", 
				            SecureHash.getHash(SecureHash.MD5, new ByteArrayInputStream("ThisIsMyTestMessage$1.0".getBytes())));
		Assert.assertEquals("7278d011ee6502952d43452ba4937d5cdcece621", 
				            SecureHash.getHash(SecureHash.SHA, new ByteArrayInputStream("ThisIsMyTestMessage$1.0".getBytes())));
		Assert.assertEquals("8bba9160b1dc173d928cfccfcb1f857e8b4b150d031447ae546366b9a056ff15", 
				            SecureHash.getHash(SecureHash.SHA_256, new ByteArrayInputStream("ThisIsMyTestMessage$1.0".getBytes())));
		Assert.assertEquals("1ad48b83be0f635c6af75f85af162c584abb44a538aa97c9ca12a8d33bfc822c10bcb2e33ccf2b89fe22dc8f1fa33672", 
				            SecureHash.getHash(SecureHash.SHA_384, new ByteArrayInputStream("ThisIsMyTestMessage$1.0".getBytes())));
		Assert.assertEquals("24bc93fd4581f8e5b3b88e655b1259eb527964de2dbc7e2baa7bf3ca290ac50640913662a19994c7e7aca25c8f85b92b56c3a393d63207dd1b4cbc5ec70f69bd", 
				            SecureHash.getHash(SecureHash.SHA_512, new ByteArrayInputStream("ThisIsMyTestMessage$1.0".getBytes())));
	}
	
	/**
	 * Tests {@link SecureHash} as instance.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testHash() throws Exception
	{
		SecureHash sh = new SecureHash(SecureHash.SHA_512);
		
		sh.add("ThisIsMyTestMessage$1.0".getBytes());

		Assert.assertEquals("24bc93fd4581f8e5b3b88e655b1259eb527964de2dbc7e2baa7bf3ca290ac50640913662a19994c7e7aca25c8f85b92b56c3a393d63207dd1b4cbc5ec70f69bd", 
	                        sh.getHash());

		//should be the same as before (be careful - the MessageDigest resets the digest after calculating the hash)!
		Assert.assertEquals("24bc93fd4581f8e5b3b88e655b1259eb527964de2dbc7e2baa7bf3ca290ac50640913662a19994c7e7aca25c8f85b92b56c3a393d63207dd1b4cbc5ec70f69bd", 
		                    sh.getHash()); 
	}

	/**
	 * Tests MD4MessageDigest via {@link SecureHash}.
	 * 
	 * @throws NoSuchAlgorithmException if MD4 is not a valid message digest
	 */
	@Test
	public void testMD4() throws NoSuchAlgorithmException
	{
		SecureHash sh = new SecureHash(SecureHash.MD4);
		
		sh.add("Welcome".getBytes());
	}
	
	/**
	 * Tests {@link SecureHash#reset()}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testReset() throws Exception
	{
		SecureHash sh = new SecureHash(SecureHash.SHA_512);

		sh.add("NULL".getBytes());
		sh.reset();
		
		sh.add("ThisIsMyTestMessage$1.0".getBytes());

		Assert.assertEquals("24bc93fd4581f8e5b3b88e655b1259eb527964de2dbc7e2baa7bf3ca290ac50640913662a19994c7e7aca25c8f85b92b56c3a393d63207dd1b4cbc5ec70f69bd", 
				            sh.getHash()); 
	}
	
	/**
	 * Test case for ticket #1447 (hash code for zip streams).
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testZipInputStream1447() throws Exception
	{
	    String sEmpty = SecureHash.getHash(SecureHash.MD5, new byte[0]);
	    
	    ZipInputStream zis = new ZipInputStream(ResourceUtil.getResourceAsStream("/com/sibvisions/util/hash_check.jar"));
	    
	    String sJar = SecureHash.getHash(SecureHash.MD5, zis);
	    
	    Assert.assertNotEquals("Hash detection failed!", sEmpty, sJar);
	}
	
}	// TestSecureHash
