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
 * 15.07.2009 - [MH] - creation
 */
package javax.rad.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.ByteSerializer;
import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests {@link FileHandle} functionality.
 * 
 * @see javax.rad.io.FileHandle
 * 
 * @author Martin Handsteiner
 */
@SuppressWarnings("deprecation")
public class TestFileHandle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the length calculation with gzipped content.
	 * 
	 * @throws Exception if the test fails
	 */	
	@Test
	public void testGzipLengthCalculation() throws Exception
	{
		FileHandle handle = new FileHandle();
		
		handle.setContent(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
		
		Assert.assertTrue(handle.getLength() == 10);

		byte[] zipped = (byte[])handle.getContentDefinition();
		
		handle.setContentDefinition(zipped);
		
		Assert.assertTrue(handle.getLength() == 10);

		byte[] bytes = new byte[(int)handle.getLength()];
		handle.getInputStream().read(bytes);
	}
	
	/**
	 * Tests {@link FileHandle#getLength()}.
	 *  
	 * @throws IOException if the test fails
	 */
	@Test
	public void testGetLength() throws IOException
	{
		File fiXml = new File(ResourceUtil.getFileForClass(ResourceUtil.getFqClassName(getClass())).getParentFile(), "sample.xml");
		
		FileHandle handle = new FileHandle(fiXml.getAbsolutePath());

		Assert.assertEquals(353614, handle.getLength());
	}
	
	/**
	 * Tests {@link FileHandle#getLength()} with a serialized {@link FileHandle}.
	 *  
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSerializedGetLength() throws Exception
	{
		ByteSerializer ser = new ByteSerializer();
		ser.setObjectStreamEnabled(true);
		
		File fiXml = new File(ResourceUtil.getFileForClass(ResourceUtil.getFqClassName(getClass())).getParentFile(), "sample.xml");
		
		FileHandle handle = new FileHandle(fiXml);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		DataOutputStream dos = new DataOutputStream(baos);
		
		ser.write(dos, handle);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		DataInputStream dis = new DataInputStream(bais);
		
		Object oResult = ser.read(dis);
		
		Assert.assertEquals(353614, ((FileHandle)oResult).getLength());
	}

	/**
	 * Tests the file content before and after serialization.
	 *  
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSerializedGZIPContent() throws Exception
	{
		ByteSerializer ser = new ByteSerializer();
		ser.setObjectStreamEnabled(true);
		
		File fiXml = new File(ResourceUtil.getFileForClass(ResourceUtil.getFqClassName(getClass())).getParentFile(), "sample.xml");
		
		FileHandle handle = new FileHandle(fiXml);

		byte[] byBefore = FileUtil.getContent(new FileInputStream(fiXml));
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		DataOutputStream dos = new DataOutputStream(baos);
		
		ser.write(dos, handle);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		DataInputStream dis = new DataInputStream(bais);
		
		Object oResult = ser.read(dis);

		byte[] byAfter = FileUtil.getContent(((FileHandle)oResult).getInputStream());

		
		Assert.assertArrayEquals(byBefore, byAfter);
	}

	/**
	 * Tests serialization as {@link FileHandle} via {@link UniversalSerializer}.
	 * 
	 * @throws Exception if test failed
	 */
	@Test
	public void testSerializedFileHandleContent() throws Exception
	{
	    UniversalSerializer user = new UniversalSerializer();
	    
        File fiXml = new File(ResourceUtil.getFileForClass(ResourceUtil.getFqClassName(getClass())).getParentFile(), "sample.xml");
        
        FileHandle handle = new FileHandle(fiXml);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        DataOutputStream dos = new DataOutputStream(baos);

        user.write(dos, handle);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        
        DataInputStream dis = new DataInputStream(bais);
        
        FileHandle fhResult = (FileHandle)user.read(dis);
        
        byte[] byOriginal = FileUtil.getContent(fiXml);
        byte[] bySerialized = FileUtil.getContent(fhResult.getInputStream()); 
        
        Assert.assertArrayEquals(byOriginal, bySerialized);
	}
	
}	// TestFileHandle
