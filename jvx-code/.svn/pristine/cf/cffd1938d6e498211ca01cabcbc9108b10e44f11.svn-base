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
 * 05.12.2010 - [JR] - creation
 * 23.12.2011 - [JR] - fixed testCopy
 * 18.03.2013 - [JR] - testZip
 */
package com.sibvisions.util.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.rad.ui.IResource;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.FileSearch;

/**
 * Tests {@link FileUtil} methods.
 * 
 * @author René Jahn
 * @see FileUtil
 */
public class TestFileUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test directory copy.
	 * 
	 * @throws IOException if the test fails
	 */
	@Test
	public void testCopy() throws IOException
	{
		File fiTest = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(this)), "../testdbs");
	
		if (!fiTest.exists())
		{
			//important for automatic builds (CI)
			fiTest = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(this)), "../../../library/testdbs");
		}
		
		File fiTemp = new File(System.getProperty("java.io.tmpdir"), "copy");

		FileUtil.delete(fiTemp);
		
		FileUtil.copy(fiTest, fiTemp, "!*/.*", "*/create.sql", "*/", "!*/*.tmp");
		
		FileSearch fs = new FileSearch();
		fs.search(fiTemp, true);

		Assert.assertEquals(12, fs.getFoundFiles().size());
		Assert.assertEquals(15, fs.getFoundDirectories().size());
	}

	/**
	 * Tests {@link FileUtil#like(String, String)}.
	 */
	@Test
	public void testLike()
	{
		Assert.assertTrue(FileUtil.like("WebContent/WEB-INF/rad/apps/newapp/config.xml", "WebContent/WEB-INF/rad/apps/*/config.xml"));
		
		Assert.assertFalse(FileUtil.like("/home/app.jar", "/home/**/*.jar"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "/home/**/*.txt"));
		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/home/*.txt"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "/home/*/*/*.txt"));
		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/home/*/*/*/*.txt"));
		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/home/*/*.txt"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "/home/**.txt"));
		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/home/**.txt2"));

		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "*"));
		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "**"));

		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/*/"));

		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/*"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "**/**"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "**/*.txt"));
		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "**/*.*"));
		
		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "*.*"));

		Assert.assertFalse(FileUtil.like("/home/user/app/unknown.txt", "/*/*.*"));

		Assert.assertTrue(FileUtil.like("/home/user/app/unknown.txt", "/**/*.*"));
		
		Assert.assertTrue(FileUtil.like("unknown.txt", "*.txt"));
		Assert.assertTrue(FileUtil.like("unknown.txt", "*.*"));
		Assert.assertTrue(FileUtil.like("unknown.txt", "**"));
		Assert.assertTrue(FileUtil.like("unknown.txt", "*"));
	}

	/**
	 * Tests zip file creation.
	 * 
	 * @throws IOException if creation fails
	 */
	@Test
	public void testZip() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		FileUtil.zip(baos, ZipOutputStream.DEFLATED, 
				     new String[] {ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(IResource.class))}, 
				     "*.png", "*.jpg", "!*/demo/*");
		
		List<String> liFiles = FileUtil.listZipEntries(new ByteArrayInputStream(baos.toByteArray()));

		System.out.println(StringUtil.toString(liFiles));
		
		//96: CI system
		//100: dev system
		Assert.assertTrue("At least 96 resources are expected!", liFiles.size() >= 96);
		Assert.assertTrue(liFiles.indexOf("com/") >= 0);
		Assert.assertTrue(liFiles.indexOf("com/sibvisions/") >= 0);
		Assert.assertTrue(liFiles.indexOf("com/sibvisions/rad/") >= 0);
		Assert.assertTrue(liFiles.indexOf("com/sibvisions/rad/application/") >= 0);
		Assert.assertTrue(liFiles.indexOf("com/sibvisions/rad/application/images/") >= 0);
		Assert.assertTrue(liFiles.indexOf("com/sibvisions/rad/application/images/background.png") >= 0);
	}
	
	/**
	 * Tests zip compression with umlauts.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testZipUmlauts() throws Exception
	{
	    File fiDir = new File(System.getProperty("java.io.tmpdir"), "/zip");
	    
	    try
	    {
    	    FileUtil.delete(fiDir);
    	    fiDir.mkdirs();
    	    
    	    File fiTxt = new File(fiDir, "ölapaü.txt");
    	    FileUtil.save(fiTxt, "This is a text file".getBytes());
    	    
    	    File fiZip = new File(fiDir, "umlauts.zip");
    	    
    	    FileOutputStream fos = new FileOutputStream(fiZip);
    	    
    	    try
    	    {
    	        FileUtil.zip(fos, ZipOutputStream.DEFLATED, new String[] {fiDir.getAbsolutePath()}, "!*/umlauts.zip");
    	    }
    	    finally
    	    {
    	        try
    	        {
    	            fos.close();
                }
                catch (Exception e)
                {
                    //ignore
                }
    	    }
    	    
    	    FileInputStream fis = new FileInputStream(fiZip);
    	    
    	    try
    	    {
        	    List<String> liEntries = FileUtil.listZipEntries(fis);
        	    
        	    Assert.assertEquals(liEntries.get(0), "ölapaü.txt");
    	    }
    	    finally
    	    {
    	        try
    	        {
    	            fis.close();
    	        }
    	        catch (Exception e)
    	        {
    	            //ignore
    	        }
    	    }
	    }
	    finally
	    {
	        FileUtil.delete(fiDir);
	    }
	}
	
	/**
	 * Tests {@link FileUtil#getDirectory(String)}.
	 */
	@Test
	public void testGetDirectory()
	{
        Assert.assertEquals("C:", FileUtil.getDirectory("C:\\temp"));
	    Assert.assertEquals("C:\\temp", FileUtil.getDirectory("C:\\temp\\test.pdf"));
	    Assert.assertEquals("C:\\temp\\subfolder", FileUtil.getDirectory("C:\\temp\\subfolder\\test.pdf"));
	    
        Assert.assertEquals(null, FileUtil.getDirectory("/tmp"));
	    Assert.assertEquals("/tmp/subfolder", FileUtil.getDirectory("/tmp/subfolder/test.pdf"));
	}
	
	/**
	 * Tests {@link FileUtil#hasGZIPHeader(byte[])}.
	 */
	@Test
	public void testHasGZIPHeaderWithBytes()
	{
		Assert.assertFalse(FileUtil.hasGZIPHeader((byte[])null));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new byte[0]));
		
		Assert.assertFalse(FileUtil.hasGZIPHeader(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new byte[] { 31, 0, 0, 0, 0, 0, 0, 0, 0, 0 }));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new byte[] { 31, -117, 0, 0, 0, 0, 0, 0, 0, 0 }));
		
		Assert.assertTrue(FileUtil.hasGZIPHeader(new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 }));
	}
	
	/**
	 * Tests {@link FileUtil#hasGZIPHeader(java.io.InputStream)}.
	 * 
	 * @throws IOException when the test fails.
	 */
	@Test
	public void testHasGZIPHeaderWithStream() throws IOException
	{
		Assert.assertFalse(FileUtil.hasGZIPHeader((InputStream)null));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[0])));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[] { 31, 0, 0, 0, 0, 0, 0, 0, 0, 0 })));
		Assert.assertFalse(FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[] { 31, -117, 0, 0, 0, 0, 0, 0, 0, 0 })));
		
		Assert.assertTrue(FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 })));
		
		// Test error behavior.
		try
		{
			FileUtil.hasGZIPHeader(new ByteArrayInputStream(new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 })
			{
				@Override
				public boolean markSupported()
				{
					return false;
				}
				
			});
			
			// Exception expected.
			Assert.fail("The function was handed a Stream which does not support marking, that should have thrown an exception but did not.");
		}
		catch (IOException e)
		{
			// Expected behavior.
		}
		
		// Test if the rewind of the stream is correct.
		InputStream stream = new ByteArrayInputStream(new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 });
		Assert.assertTrue(FileUtil.hasGZIPHeader(stream));
		Assert.assertEquals(
				"Rewinding of the stream failed, expected to read 15 bytes, but got something different.",
				15,
				stream.read(new byte[4096]));
	}
	
}	// TestFileUtil
