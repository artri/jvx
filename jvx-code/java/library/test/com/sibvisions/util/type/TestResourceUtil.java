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
 * 08.04.2009 - [JR] - creation
 * 15.07.2009 - [JR] - testPath implemented
 * 13.05.2011 - [JR] - #353: test case
 */
package com.sibvisions.util.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import javax.rad.ui.IFactory;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * Tests {@link ResourceUtil} methods.
 * 
 * @author René Jahn
 * @see ResourceUtil
 */
public class TestResourceUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link ResourceUtil#getResourceStream(String)} method.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetResourceStream() throws Exception
	{
		XmlWorker xmw = new XmlWorker();
		
		XmlNode xmn = xmw.read(ResourceUtil.getResourceAsStream("/com/sibvisions/util/xml/simple.xml"));
		
		
		Assert.assertEquals("1000", xmn.getNode("/server/buttondelay").getValue());
		
		
		//Temp-File erstellen und absoluten Pfad testen
		File fiTemp = new File(System.getProperty("java.io.tmpdir"), "resource1.xml");
		FileOutputStream fosTemp = new FileOutputStream(fiTemp);

		xmn.setNode("/server/buttondelay", "2000");
		
		xmw.write(fosTemp, xmn);
		
		fosTemp.close();

		FileInputStream fisTemp = new FileInputStream(fiTemp);
		
		try
		{
			xmn = xmw.read(fisTemp);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			xmn = null;
		}
		finally
		{
			fisTemp.close();
		}

		Assert.assertEquals("2000", xmn.getNode("/server/buttondelay").getValue());
		
		Assert.assertTrue("Can not delete temporary file!", fiTemp.delete());
	
		//Temp-File erstellen und den Namen testen
		fiTemp = new File(ResourceUtil.getPathForClass("/com/sibvisions/util/xml/TestXmlWorker.class"), "simple.xml");
		fosTemp = new FileOutputStream(fiTemp);

		xmn.setNode("/server/buttondelay", "3000");
		
		xmw.write(fosTemp, xmn);
		
		fosTemp.close();

		fisTemp = new FileInputStream(fiTemp);
		
		try
		{
			xmn = xmw.read(fisTemp);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			xmn = null;
		}
		finally
		{
			fisTemp.close();
		}
		
		Assert.assertEquals("3000", xmn.getNode("/server/buttondelay").getValue());
		
		Assert.assertTrue("Can not delete temporary file!", fiTemp.delete());
		
		//Jetzt das gleiche ohne Temp-File
		xmn = xmw.read(ResourceUtil.getResourceAsStream("/com/sibvisions/util/xml/simple.xml"));

		Assert.assertEquals("1000", xmn.getNode("/server/buttondelay").getValue());
	}
	
	/**
	 * Tests {@link ResourceUtil#getPathForClass(String)}, {@link ResourceUtil#getLocationForClass(String)}
	 * and {@link ResourceUtil#getFileForClass(String)}.
	 */
	@Test
	public void testPath()
	{
		File fiCheck;

		/*
		 * ../jdk1.5.0_15/jre/lib
		 * ../jdk1.5.0_15/jre/lib/rt.jar
		 * ../jdk1.5.0_15/jre/lib/rt.jar
		 */

		int iVersion;
		
		try
		{
			iVersion = Integer.parseInt(System.getProperty("java.vm.specification.version"));
		}
		catch (Exception e)
		{
			//-> 1.8 = 8
			iVersion = 8;
		}
		
		if (iVersion > 8)
		{
			//Module system
			
			fiCheck = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(new String())));
			
			Assert.assertTrue(fiCheck.isDirectory());
			Assert.assertEquals("", fiCheck.getName());
			
			fiCheck = new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(new String())));

			Assert.assertFalse(fiCheck.exists());
			Assert.assertEquals("java.base", fiCheck.getName());
			
			fiCheck = new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(new String())));
			
			Assert.assertFalse(fiCheck.exists());
			Assert.assertEquals("java.base", fiCheck.getName());
		}
		else
		{
			//Simple classpath
			
			fiCheck = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(new String())));
		
			Assert.assertTrue(fiCheck.isDirectory());
			Assert.assertEquals("lib", fiCheck.getName());
			
			fiCheck = new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(new String())));
	
			Assert.assertTrue(fiCheck.isFile());
			Assert.assertEquals("rt.jar", fiCheck.getName());
			
			fiCheck = new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(new String())));
			
			Assert.assertTrue(fiCheck.isFile());
			Assert.assertEquals("rt.jar", fiCheck.getName());
		}

		/*
		 * ../jvx/trunk/java/library/classes
		 * ../jvx/trunk/java/library/classes
		 * ../jvx/trunk/java/library/classes/com/sibvisions/util/type/TestResourceUtil.class
		 */
		
		fiCheck = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(this)));

		Assert.assertTrue(fiCheck.isDirectory());
		//dont't check the name because it's different during automatic build

		fiCheck = new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(this)));
		
		Assert.assertTrue(fiCheck.isDirectory());
		//dont't check the name because it's different during automatic build
		
		fiCheck = ResourceUtil.getFileForClass(ResourceUtil.getFqClassName(this));
		
		Assert.assertTrue(fiCheck.isFile());
		Assert.assertEquals("class", FileUtil.getExtension(fiCheck.getName()));
	}

	/**
	 * Tests empty resource search.
	 */
	@Test
	public void testEmptyResource353()
	{
		URL url = ResourceUtil.getResource(null);
		
		Assert.assertNull("Empty resource should not be found!", url);

		url = ResourceUtil.getResource("");
		
		Assert.assertNull("Empty resource should not be found!", url);
		
		url = ResourceUtil.getResource("  ");
		
		Assert.assertNull("Empty resource should not be found!", url);
	}

	/**
	 * Tests {@link ResourceUtil#getInterfaces(Class, Class...)}.
	 */
	@Test
	public void testGetInterfaces()
	{
		Assert.assertArrayEquals(new Class<?>[] {IFactory.class}, ResourceUtil.getInterfaces(AbstractFactory.class, false));
		Assert.assertArrayEquals(new Class<?>[] {}, ResourceUtil.getInterfaces(AbstractFactory.class, false, ITestFactory.class));
		Assert.assertArrayEquals(new Class<?>[] {IFactory.class}, ResourceUtil.getInterfaces(AbstractFactory.class, false, IFactory.class));
		
		Assert.assertArrayEquals(new Class<?>[] {ITestFactory.class, IFactory.class}, ResourceUtil.getInterfaces(TestFactory.class, false));
		Assert.assertArrayEquals(new Class<?>[] {ITestFactory.class, IFactory.class}, ResourceUtil.getInterfaces(TestFactory.class, false, IFactory.class));

		Assert.assertArrayEquals(new Class<?>[] {ITestFactory.class, IFactory.class}, ResourceUtil.getInterfaces(BugfixTestFactory.class, false, IFactory.class));
		
		Assert.assertArrayEquals(new Class<?>[] {ITestFactory.class, IFactory.class, ITechnology.class}, 
				                 ResourceUtil.getInterfaces(BugfixTestFactory.class, true, ITestFactory.class));
	}

    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * A simple interface.
	 * 
	 * @author René Jahn
	 */
	public interface ITechnology
	{
	}
	
	/**
	 * A custom factory interface for custom factory tests.
	 * 
	 * @author René Jahn
	 */
	public interface ITestFactory extends IFactory,
	                                      ITechnology
	{
	}	// ITestFactory
	
	/**
	 * A simple factory without logic.
	 * 
	 * @author René Jahn
	 */
	public abstract class AbstractFactory implements IFactory
	{
	}	// AbstractFactory
	
	/**
	 * A custom factory that extends an existing factory and implements
	 * its own interface.
	 * 
	 * @author René Jahn
	 */
	public abstract class TestFactory extends AbstractFactory 
	                                  implements ITestFactory
	{
	}	// TestFactory
	
	/**
	 * A custom factory that extends an existing custom factory and implements
	 * its own.
	 * 
	 * @author René Jahn
	 */
	public abstract class BugfixTestFactory extends TestFactory
	{
	}	// BugfixTestFactory
	
}	// TestResourceUtil
