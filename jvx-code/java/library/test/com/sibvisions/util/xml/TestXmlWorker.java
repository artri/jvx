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
 * 01.10.2008 - [JR] - creation
 * 13.05.2011 - [JR] - test351
 * 17.04.2013 - [JR] - Doctype support test
 */
package com.sibvisions.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Test cases for relevant methods of XML access class <code>TextXmlNode</code>.
 * 
 * @author René Jahn
 * @see com.sibvisions.util.xml.XmlWorker
 * @see com.sibvisions.util.xml.XmlNode
 */
public class TestXmlWorker extends XmlTestBase
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Read in a xml file without an error.
	 * 
	 * @throws Exception if the test fails
	 * @throws java.io.IOException if the test fails
	 */
/*	
	@Test
	public void testMassRead() throws Exception
	{
		XmlWorker xmp = new XmlWorker();
		
		XmlNode xmnRead;
		
		long lTime = System.currentTimeMillis();
//try
//{

//		FIFOHashtable fht = XMLTracker.readXML(new FileInputStream("D:\\000000_mp3liste.m3t"));
//		FIFOHashtable fht = XMLTracker.readXML(new FileInputStream("D:\\singlefile.m3t"));
//		hm.util.FIFOHashtable fht = hm.util.XMLTracker.readXML(new java.io.FileInputStream("D:\\40mb.m3t"));

		System.gc();
		
//		System.out.println(System.currentTimeMillis() - lTime);

//		lTime = System.currentTimeMillis();
		
//System.out.println(fht.getElement("MP3INVENTORY2.MP3FILE[16544].ID3v1.Comment_ID3v1"));
//System.out.println(fht.getElement("MP3INVENTORY.MP3FILE[16544].ID3v1.Comment_ID3v1"));
//System.out.println(fht);
//System.out.println(FIFOHashtable.INSTANCECOUNT);

//System.out.println(System.currentTimeMillis() - lTime);
		
//}
//catch (Throwable th)
//{
//System.out.println(XMLTracker.iAnz);
//}
		
// 		System.out.println((java.lang.Runtime.getRuntime().totalMemory() - java.lang.Runtime.getRuntime().freeMemory()) / 1024f / 1024f);		

 		
//		xmnRead = xmp.read(new java.io.FileInputStream("D:\\000000_mp3liste.m3t"));
		xmnRead = xmp.read(new java.io.FileInputStream("D:\\40mb.m3t"));
//		xmnRead = xmp.read(new FileInputStream("D:\\singlefile.m3t"));
		
		System.out.println(System.currentTimeMillis() - lTime);
		
//		xmnRead.createXml(new java.io.FileOutputStream("D:\\outtest.xml"), 1);
		
//		System.out.println(xmnRead);
		
		lTime = System.currentTimeMillis();

System.out.println("NODE FOUND: " + xmnRead.getNode("/MP3INVENTORY2/MP3FILE(16544)/ID3v1/Comment_ID3v1"));
for (int i = 16000; i < 17000; i++)
{
//	System.out.println("NODE FOUND: " + xmnRead.getNode("/MP3INVENTORY/MP3FILE(" + i + ")/ID3v1/Comment_ID3v1"));
	xmnRead.getNode("/MP3INVENTORY/MP3FILE(" + i + ")/ID3v1/Comment_ID3v1");	
}
//System.out.println("NODE FOUND: " + xmnRead.getNode("/MP3INVENTORY/MP3FILE/ID3v1/Genre"));		
//System.out.println("NODE FOUND: " + xmnRead.getNode("/MP3INVENTORY/MP3FILE/ID3v1/Genre"));		
		
		System.out.println(System.currentTimeMillis() - lTime);
		
System.out.println((java.lang.Runtime.getRuntime().totalMemory() - java.lang.Runtime.getRuntime().freeMemory()) / 1024f / 1024f);		
		
//		System.out.println(xmnRead);
	}	
*/	
	/**
	 * Read in a xml file without an error.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testRead() throws Exception
	{
		XmlNode xmnSimple = readXml("simple.xml");

		
		Assert.assertEquals
		(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"\n" +
			"<!-- Vor Beginn! -->\n" +
			"<server>\n" +
			"  <leer/>\n" +
			"  <leer/>\n" +
			"  <!-- Test: STARTPORT -->\n" +
			"  <startport>2001</startport>\n" +
			"  <audio>off</audio>\n" +
			"  <serial>COM1</serial>\n" +
			"  <buttondelay>1000</buttondelay>\n" +
			"  <media>\n" +
			"    <directory>C:\\Temp\\smedia</directory>\n" +
			"  </media>\n" +
			"  <!--\n" +
			"  Test: TCP Angaben\n" +
			"  in der zweiten Zeile\n" +
			"  und in der dritten Zeile\n" +
			"  \n" +
			"  nach einer Leerzeile\n" +
			"  -->\n" +
			"  <tcp>\n" +
			"    <bindaddress>0.0.0.0</bindaddress>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </tcp>\n" +
			"  <db>\n" +
			"    <name>SERVER_1</name>\n" +
			"    <path>c:\\temp\\pbxdb</path>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </db>\n" +
			"  <report name=\"Standard\">\n" +
			"    <fontdirectory>C:\\temp\\pdffonts</fontdirectory>\n" +
			"    <format>pdf</format>\n" +
			"  </report>\n" +
			"  <report name=\"User\">\n" +
			"    <fontdirectory>C:\\temp\\xlsfonts</fontdirectory>\n" +
			"    <format>xls</format>\n" +
			"  </report>\n" +
			"  <text>\n" +
			"  Hans\n" +
			" Huber\n" +
			" Pauli\n" +
			"    <comment>Kommentar</comment>\n" +
			"    <comment2>Kommentar2</comment2>\n" +
			"  </text>\n" +
			"  <text>\n" +
			"  Das ist ein Text\n" +
			"von Hansi Huber\n" +
			"  </text>\n" +
			"</server>\n" +
			"<!-- Nach Ende -->",
			xmnSimple.toString()
		);
	}

	/**
	 * Read in a xml file without an error, change the result and read the
	 * file again. The changes should be discarded.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testReadReset() throws Exception
	{
		XmlWorker xmp = new XmlWorker();
		
		XmlNode xmn = xmp.read(ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/simple.xml"));

		
		Assert.assertEquals
		(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"\n" +
			"<!-- Vor Beginn! -->\n" +
			"<server>\n" +
			"  <leer/>\n" +
			"  <leer/>\n" +
			"  <!-- Test: STARTPORT -->\n" +
			"  <startport>2001</startport>\n" +
			"  <audio>off</audio>\n" +
			"  <serial>COM1</serial>\n" +
			"  <buttondelay>1000</buttondelay>\n" +
			"  <media>\n" +
			"    <directory>C:\\Temp\\smedia</directory>\n" +
			"  </media>\n" +
			"  <!--\n" +
			"  Test: TCP Angaben\n" +
			"  in der zweiten Zeile\n" +
			"  und in der dritten Zeile\n" +
			"  \n" +
			"  nach einer Leerzeile\n" +
			"  -->\n" +
			"  <tcp>\n" +
			"    <bindaddress>0.0.0.0</bindaddress>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </tcp>\n" +
			"  <db>\n" +
			"    <name>SERVER_1</name>\n" +
			"    <path>c:\\temp\\pbxdb</path>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </db>\n" +
			"  <report name=\"Standard\">\n" +
			"    <fontdirectory>C:\\temp\\pdffonts</fontdirectory>\n" +
			"    <format>pdf</format>\n" +
			"  </report>\n" +
			"  <report name=\"User\">\n" +
			"    <fontdirectory>C:\\temp\\xlsfonts</fontdirectory>\n" +
			"    <format>xls</format>\n" +
			"  </report>\n" +
			"  <text>\n" +
			"  Hans\n" +
			" Huber\n" +
			" Pauli\n" +
			"    <comment>Kommentar</comment>\n" +
			"    <comment2>Kommentar2</comment2>\n" +
			"  </text>\n" +
			"  <text>\n" +
			"  Das ist ein Text\n" +
			"von Hansi Huber\n" +
			"  </text>\n" +
			"</server>\n" +
			"<!-- Nach Ende -->",
			xmn.toString()
		);
		
		xmn.setNode("/server/buttondelay", "2000");
		
		Assert.assertEquals("2000", xmn.getNode("/server/buttondelay").getValue());

		//discard changes
		xmn = xmp.read(ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/simple.xml"));

		Assert.assertEquals
		(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"\n" +
			"<!-- Vor Beginn! -->\n" +
			"<server>\n" +
			"  <leer/>\n" +
			"  <leer/>\n" +
			"  <!-- Test: STARTPORT -->\n" +
			"  <startport>2001</startport>\n" +
			"  <audio>off</audio>\n" +
			"  <serial>COM1</serial>\n" +
			"  <buttondelay>1000</buttondelay>\n" +
			"  <media>\n" +
			"    <directory>C:\\Temp\\smedia</directory>\n" +
			"  </media>\n" +
			"  <!--\n" +
			"  Test: TCP Angaben\n" +
			"  in der zweiten Zeile\n" +
			"  und in der dritten Zeile\n" +
			"  \n" +
			"  nach einer Leerzeile\n" +
			"  -->\n" +
			"  <tcp>\n" +
			"    <bindaddress>0.0.0.0</bindaddress>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </tcp>\n" +
			"  <db>\n" +
			"    <name>SERVER_1</name>\n" +
			"    <path>c:\\temp\\pbxdb</path>\n" +
			"    <user>user</user>\n" +
			"    <pwd>password</pwd>\n" +
			"  </db>\n" +
			"  <report name=\"Standard\">\n" +
			"    <fontdirectory>C:\\temp\\pdffonts</fontdirectory>\n" +
			"    <format>pdf</format>\n" +
			"  </report>\n" +
			"  <report name=\"User\">\n" +
			"    <fontdirectory>C:\\temp\\xlsfonts</fontdirectory>\n" +
			"    <format>xls</format>\n" +
			"  </report>\n" +
			"  <text>\n" +
			"  Hans\n" +
			" Huber\n" +
			" Pauli\n" +
			"    <comment>Kommentar</comment>\n" +
			"    <comment2>Kommentar2</comment2>\n" +
			"  </text>\n" +
			"  <text>\n" +
			"  Das ist ein Text\n" +
			"von Hansi Huber\n" +
			"  </text>\n" +
			"</server>\n" +
			"<!-- Nach Ende -->",
			xmn.toString()
		);
	}
	
	/**
	 * Try to read a not well formed xml file.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = SAXException.class)
	public void testNotWellFormedXml() throws Exception
	{
		readXml("not_well_formed.xml");
	}
	
	/**
	 * Read in a xml file and try to get a node in a node list with a letter
	 * as index.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetNodeWithLetterIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//Invalid index
		xmnRead.getNode("/server/report(A)/format");
	}
	
	/**
	 * Read in a xml file and try to get a node in a node list with an
	 * incomplete index definition.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetNodeWithIncompleteIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//Invalid index
		xmnRead.getNode("/server/report(2/format");
	}
	

	/**
	 * Read in a xml file and try to get a node in a node list without an
	 * index.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetNodeWithoutIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//Missing index for tag 'report'
		xmnRead.getNode("/server/report/format");
	}

	/**
	 * Read in a xml file and try to get a node in a node list with an
	 * index out of bounds.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetNodeWithIndexOutOfBounds() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//Invalid index
		Assert.assertNull(xmnRead.getNode("/server/report(5)/format"));
		Assert.assertNull(xmnRead.getNode("/server/report(-1)/format"));
	}

	/**
	 * Read in a xml file and check specific nodes or values.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetNode() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		//------------------------------------------------
		// Check unavailable tags
		//------------------------------------------------

		Assert.assertNull(xmnRead.getNode("/unknown"));
		Assert.assertNull(xmnRead.getNode("/server/unknown"));

		//------------------------------------------------
		// Check values
		//------------------------------------------------

		Assert.assertNotNull("Tag '/server/media' not found!", xmnRead.getNode("/server/media"));
		Assert.assertNotNull("Tag '/server/media/directory' not found!", xmnRead.getNode("/server/media/directory"));
		Assert.assertNotNull("Tag '/server/report(1)/directory' not found!", xmnRead.getNode("/server/report(1)/format"));

		//System.out.println("/server/media = [" + xmnRead.getNode("/server/media").getValue() + "]");
		
		Assert.assertNull(xmnRead.getNode("/server/media").getValue());

		//System.out.println("/server/media/directory = [" + xmnRead.getNode("/server/media/directory").getValue() + "]");
		
		Assert.assertEquals("C:\\Temp\\smedia", xmnRead.getNode("/server/media/directory").getValue());
		
		//------------------------------------------------
		// Check index
		//------------------------------------------------

		//System.out.println("/server/report(1)/format = [" + xmnRead.getNode("/server/report(1)/format").getValue() + "]");

		Assert.assertEquals("xls", xmnRead.getNode("/server/report(1)/format").getValue());
		
		//------------------------------------------------
		// Read attributes
		//------------------------------------------------
		
		Assert.assertEquals("User", xmnRead.getNode("/server/report(1)/name").getValue());
	}
	
	/**
	 * Read in a xml file and check specific nodes list.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetNodes() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		List<XmlNode> xmnList;
		
		
		xmnList = xmnRead.getNodes("/server/report(0)");
		
		Assert.assertNotNull(xmnList);
		Assert.assertEquals(1, xmnList.size());

		xmnList = xmnRead.getNodes("/server/report(1)");
		
		Assert.assertNotNull(xmnList);
		Assert.assertEquals(1, xmnList.size());
		
		xmnList = xmnRead.getNodes("/server/report");
		
		Assert.assertNotNull(xmnList);
		Assert.assertEquals(2, xmnList.size());
		
		xmnList = xmnRead.getNodes("/server/leer");
		
		Assert.assertNotNull(xmnList);
		Assert.assertEquals(2, xmnList.size());
		
		xmnList = xmnRead.getNodes("/server/audio");
		
		Assert.assertNotNull(xmnList);
		Assert.assertEquals(1, xmnList.size());
		
		xmnList = xmnRead.getNodes("/server/unknown");
		
		Assert.assertNull(xmnList);
	}
	
	/**
	 * Read in a xml file and remove a specific node.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testRemoveNode() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		Assert.assertNotNull(xmnRead.getNode("/server/text(0)/comment"));
		
		xmnRead.removeNode("/server/text(0)/comment");
		
		Assert.assertNull(xmnRead.getNode("/server/text(0)/comment"));
		
		Assert.assertNotNull(xmnRead.getNode("/server/text(1)"));

		xmnRead.removeNode("/server/text(1)");
		
		Assert.assertNull(xmnRead.getNode("/server/text(1)"));
	}

	/**
	 * Read in a xml file and try to set a node in a node list without
	 * an index.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testSetNodeWithoutIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//Missing index for tag 'report'
		xmnRead.setNode("/server/report/format", "ppt");
	}
	
	/**
	 * Read in a xml file and try to set a specific node at an negative position.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testSetNodeWithNegativePosition() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		//Invalid index
		xmnRead.setNode("/server/report(-1)/format", "xxx");
	}
	
	/**
	 * Read in a xml file and try to set a specific node at an negative position.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testSetNodeWithInvalidPosition() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		//Invalid index
		xmnRead.setNode("/server/report(1000)/format", "xxx");
	}	
	
	/**
	 * Read in a xml file and set a specific node.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSetNode() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		XmlNode xmnSet;
		
		
		//------------------------------------------------
		// Delete a single element
		//------------------------------------------------

		Assert.assertNotNull(xmnRead.getNode("/server/audio"));

		xmnRead.setNode("/server/audio", null);
		
		Assert.assertNull(xmnRead.getNode("/server/audio").getValue());
		
		//------------------------------------------------
		// Add a new element
		//------------------------------------------------

		Assert.assertNull(xmnRead.getNode("/server/domain"));

		xmnRead.setNode("/server/domain", "www.jarex.at");
		
		Assert.assertNotNull(xmnRead.getNode("/server/domain"));

		//------------------------------------------------
		// Add an element to an existing element list
		//------------------------------------------------
		
		xmnRead.setNode("/server/report(2)/format", "ppt");
		
		Assert.assertEquals("ppt", xmnRead.getNode("/server/report(2)/format").getValue());
		
		//------------------------------------------------
		// Overwrite an element at a specific position
		// of an element list
		//------------------------------------------------

		Assert.assertNotNull("Tag '/server/report(1)/format' not found!", xmnRead.getNode("/server/report(1)/format"));
		Assert.assertEquals(xmnRead.getNode("/server/report(1)/format").getValue(), "xls");

		xmnRead.setNode("/server/report(1)/format", "doc");

		Assert.assertEquals(xmnRead.getNode("/server/report(1)/format").getValue(), "doc");

		//------------------------------------------------
		// Set a subnode as attribute
		//------------------------------------------------
		
		Assert.assertNotNull("Tag '/server/media/directory' not found!", xmnRead.getNode("/server/media/directory"));
		Assert.assertNull("Tag '/server/media/directory/attribute1' found!", xmnRead.getNode("/server/media/directory/attribute1"));
		
		xmnSet = new XmlNode(XmlNode.TYPE_ATTRIBUTE, "attribute1");
		xmnSet.setValue("NEUER WERT");
		
		xmnRead.setNode("/server/media/directory/attribute1", xmnSet);
		
		Assert.assertNotNull(xmnRead.getNode("/server/media/directory/attribute1"));
		Assert.assertEquals(xmnRead.getNode("/server/media/directory/attribute1").getValue(), "NEUER WERT");

		//------------------------------------------------
		// Overwrite all sub elements of a specific 
		// element
		//------------------------------------------------
		
		Assert.assertNotNull("Tag '/server/report(0)/format' not found!", xmnRead.getNode("/server/report(0)/format"));
		Assert.assertEquals("pdf", xmnRead.getNode("/server/report(0)/format").getValue());
		
		xmnSet = new XmlNode(XmlNode.TYPE_TEXT, "report");
		xmnSet.setValue("replaced value");
		
		xmnRead.setNode("/server/report(0)", xmnSet);
		
		Assert.assertNull(xmnRead.getNode("/server/report(0)/format"));
		Assert.assertEquals(xmnRead.getNode("/server/report(0)").getValue(), "replaced value");
	}
	
	/**
	 * Tests {@link XmlNode#setNode(String, Object)} with an index for the parent node.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSetNodeWithParentIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		xmnRead.setNode("/server/a/b/c/d/e(0)/f", "1");
		xmnRead.setNode("/server/a/b/c/d/e(1)/f", "2");
	}

	/**
	 * Tests {@link XmlNode#setNode(String, Object)} with an index for the parent node, but the parent
	 * does not exist.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSetNodeWithValidParentIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		xmnRead.setNode("/server/a/b/c/d/e(0)/f", "1");
	}
	
	/**
	 * Tests {@link XmlNode#setNode(String, Object)} with an invalid index for the parent node, because
	 * the parent does not exist.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testSetNodeWithInvalidParentIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		xmnRead.setNode("/server/a/b/c/d/e(1)/f", "1");
	}
	
	/**
	 * Read in a xml file and removes all sub nodes from an element.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testClearSubNodes() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");

		
		//------------------------------------------------
		// Delete all sub elements
		//------------------------------------------------
		
		Assert.assertNotNull("Tag '/server/report(0)/format' not found!", xmnRead.getNode("/server/report(0)/format"));
		Assert.assertEquals(3, xmnRead.getNode("/server/report(0)").size());
		
		xmnRead.getNode("/server/report(0)").clearSubNodes();
		
		Assert.assertEquals(0, xmnRead.getNode("/server/report(0)").size());
	}
	
	/**
	 * Read in a xml file and insert a specific node.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testInsertNode() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		XmlNode xmnInsert;
		XmlNode xmnInsAttrib;
		XmlNode xmnInsElement;
		XmlNode xmnInsSubElement;

		
		//------------------------------------------------
		// Insert empty elements at specific positions
		//------------------------------------------------

		Assert.assertNotNull("Tag '/server/report(0)' not found!", xmnRead.getNode("/server/report(0)"));
		Assert.assertNotNull("Tag '/server/report(1)' not found!", xmnRead.getNode("/server/report(1)"));
		
		Assert.assertEquals(xmnRead.getNode("/server/report(0)/format").getValue(), "pdf");
		Assert.assertEquals(xmnRead.getNode("/server/report(1)/format").getValue(), "xls");

		xmnRead.insertNode("/server/report(1)", null);
		xmnRead.setNode("/server/report(1)/format", "CDR");

		Assert.assertEquals(xmnRead.getNode("/server/report(0)/format").getValue(), "pdf");
		Assert.assertEquals(xmnRead.getNode("/server/report(1)/format").getValue(), "CDR");
		Assert.assertEquals(xmnRead.getNode("/server/report(2)/format").getValue(), "xls");
		
		xmnRead.insertNode("/server/report(0)", null);
		xmnRead.setNode("/server/report(0)/format", "ABC");

		Assert.assertEquals(xmnRead.getNode("/server/report(0)/format").getValue(), "ABC");
		Assert.assertEquals(xmnRead.getNode("/server/report(1)/format").getValue(), "pdf");
		Assert.assertEquals(xmnRead.getNode("/server/report(2)/format").getValue(), "CDR");
		Assert.assertEquals(xmnRead.getNode("/server/report(3)/format").getValue(), "xls");

		//Rather it should be an add and not an insert, because position 4 is not available yet! 
		xmnRead.insertNode("/server/report(4)", null);
		xmnRead.setNode("/server/report(4)/format", "ENDE");
		
		Assert.assertEquals(xmnRead.getNode("/server/report(4)/format").getValue(), "ENDE");
		
		//Test if a element will be added without an explicit position
		xmnRead.setNode("/server/domain2", "www.nolimit.at");
		
		Assert.assertEquals(xmnRead.getNode("/server/domain2").getValue(), "www.nolimit.at");
		
		//------------------------------------------------
		// Insert elements
		//------------------------------------------------

		xmnInsert = new XmlNode("ABCD");
		
		xmnInsAttrib  = new XmlNode(XmlNode.TYPE_ATTRIBUTE, "attrib1", xmnInsert);
		xmnInsAttrib.setValue("Attribut");
		
		xmnInsElement = new XmlNode(XmlNode.TYPE_TEXT, "element1", xmnInsert);
		xmnInsElement.setValue("Element");
		
		xmnInsSubElement = new XmlNode(XmlNode.TYPE_TEXT, "subelement1", xmnInsElement);
		xmnInsSubElement.setValue("Sub-Element<>");
		
        xmnRead.insertNode("/server/startport(1)", "ABCD");
        xmnRead.insertNode("/server/startport(1)", xmnInsert);
        xmnRead.insertNode("/server/startport(0)", "VORNE");
        xmnRead.insertNode("/server/startport(3)", "ENDE");

        Assert.assertEquals("VORNE", xmnRead.getNode("/server/startport(0)").getValue());
        Assert.assertEquals("2001", xmnRead.getNode("/server/startport(1)").getValue());
        Assert.assertEquals("ABCD", xmnRead.getNode("/server/startport(2)").getValue());
        Assert.assertEquals("ENDE", xmnRead.getNode("/server/startport(3)").getValue());
        Assert.assertNotNull(xmnRead.getNode("/server/ABCD"));
        Assert.assertEquals("Element", xmnRead.getNode("/server/ABCD/element1").getValue());
        
		//------------------------------------------------
		// Clone Test
		//------------------------------------------------

        //Check that the inserted node is not a clone
        
        xmnInsert.setValue("BUG");
        xmnInsElement.setValue("Neuer Wert");
        xmnInsSubElement.setValue("Neuer Sub-Element-Wert");
                
        Assert.assertNotNull(xmnRead.getNode("/server/ABCD").getValue());
        Assert.assertEquals("Neuer Wert", xmnRead.getNode("/server/ABCD/element1").getValue());
        Assert.assertEquals("Neuer Sub-Element-Wert", xmnRead.getNode("/server/ABCD/element1/subelement1").getValue());
        
		//------------------------------------------------
		// Insert already inserted node on another place
		//------------------------------------------------
        
        xmnRead.getNode("/server/media").add(xmnInsert);

        Assert.assertNull(xmnRead.getNode("/server/ABCD"));
        Assert.assertNotNull(xmnRead.getNode("/server/media/ABCD").getValue());        
	}	
	
	/**
	 * Tests {@link XmlNode#insertNode(String, Object)} where the structure does not exists. It will
	 * be created on demand.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testInsertCreateStructure() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		//2 is after 1
		xmnRead.insertNode("/server/a/f", "1");
		xmnRead.insertNode("/server/a/f", "2");
		
		Assert.assertEquals("<a>\n  <f>1</f>\n  <f>2</f>\n</a>", xmnRead.getNode("/server/a/").toString());
		
		xmnRead = readXml("simple.xml");

		//1 is after 2
		xmnRead.insertNode("/server/a/f", "1");
		xmnRead.insertNode("/server/a/f(0)", "2");
		
		Assert.assertEquals("<a>\n  <f>2</f>\n  <f>1</f>\n</a>", xmnRead.getNode("/server/a/").toString());
		
		xmnRead = readXml("simple.xml");

		//2 is after 1
		xmnRead.insertNode("/server/a/f", "1");
		xmnRead.insertNode("/server/a/f(1)", "2");
		
		Assert.assertEquals("<a>\n  <f>1</f>\n  <f>2</f>\n</a>", xmnRead.getNode("/server/a/").toString());
	}
	
	/**
	 * Tests {@link XmlNode#insertNode(String, Object)} without index definition.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testInsertWithoutIndex() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		xmnRead.insertNode("/server/new/a", "A");
		xmnRead.insertNode("/server/new/a", "B");
		xmnRead.insertNode("/server/new/a", "C");
		
		XmlNode xmnSearch = xmnRead.getNode("/server/new");
		
		Assert.assertEquals(0, xmnSearch.indexOf("/a", "A"));
		Assert.assertEquals(1, xmnSearch.indexOf("/a", "B"));
		Assert.assertEquals(2, xmnSearch.indexOf("/a", "C"));
	}

	/**
	 * Read in a xml file and try to insert a specific node at an invalid position.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testInsertNodeWithIndexOutOfBounds() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		Assert.assertNull("Tag '/server/report(3)' found!", xmnRead.getNode("/server/report(3)"));
		
		//It's not possible to insert an element at the end!
		xmnRead.insertNode("/server/report(3)", null);
	}	
	
	/**
	 * Read in a xml file and try to insert a specific node at an negative position.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testInsertNodeWithNegativePosition() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		//Invalid index
		xmnRead.insertNode("/server/report(-1)/format", "xxx");
	}	
	
	/**
	 * Read in a xml file and try to insert a specific node at an invalid position.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testInsertNodeWithInvalidPosition() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		
		//Invalid index
		xmnRead.insertNode("/server/report(1000)/format", "xxx");
	}	

	/**
	 * Read in a xml file and check the occurence of nodes.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetNodeCount() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		int iSize;
		
		
		iSize = xmnRead.getNodeCount("/server/report(1000)");
		
		Assert.assertEquals(0, iSize);

		iSize = xmnRead.getNodeCount("/server/report(0)");
		
		Assert.assertEquals(1, iSize);

		iSize = xmnRead.getNodeCount("/server/report(1)");
		
		Assert.assertEquals(1, iSize);
		
		iSize = xmnRead.getNodeCount("/server/report");
		
		Assert.assertEquals(2, iSize);
		
		iSize = xmnRead.getNodeCount("/server/leer");
		
		Assert.assertEquals(2, iSize);
		
		iSize = xmnRead.getNodeCount("/server/audio");
		
		Assert.assertEquals(1, iSize);
		
		iSize = xmnRead.getNodeCount("/server/unknown");
		
		Assert.assertEquals(0, iSize);
	}
	
	/**
	 * Read in a xml file and check the size of nodes.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSize() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		int iSize;
		
		
		iSize = xmnRead.getNode("/server/report(0)").size();
		
		Assert.assertEquals(3, iSize);
		
		iSize = xmnRead.getNode("/server/report(0)").size(XmlNode.TYPE_ATTRIBUTE);
		
		Assert.assertEquals(1, iSize);
		
		iSize = xmnRead.getNode("/server/buttondelay").size();
		
		Assert.assertEquals(0, iSize);
	}	
	
	/**
	 * Writes a new created xml file to stdout.
	 * 
	 * @throws Exception if the xml file could not be created
	 */
	@Test
	public void testWrite() throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		XmlWorker xmw = new XmlWorker();
		
		XmlNode xmn = new XmlNode("start");
		XmlNode xmnTag1 = new XmlNode(XmlNode.TYPE_TEXT, "tag1", xmn);
		XmlNode xmnTag11 = new XmlNode(XmlNode.TYPE_TEXT, "tag1_1", xmnTag1);
		XmlNode xmnTag2 = new XmlNode(XmlNode.TYPE_TEXT, "tag2", xmn);
		XmlNode xmnTag2Attrib = new XmlNode(XmlNode.TYPE_ATTRIBUTE, "attribute", xmnTag2);
		
		xmnTag1.setValue("Hallo");
		xmnTag11.setValue("Testwert 1.1\nNeue Zeile");
		xmnTag2.setValue("Testwert 2");
		xmnTag2Attrib.setValue("tofile");
		
		xmw.setIndentation(3);
		xmw.write(bos, xmn);
		
		Assert.assertEquals
		(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"\n" + 
			"<start>\n" +
			"   <tag1>\n" +
			"   Hallo\n" +
			"      <tag1_1>\n" +
			"      Testwert 1.1\n" +
			"Neue Zeile\n" +
			"      </tag1_1>\n" +
			"   </tag1>\n" +
			"   <tag2 attribute=\"tofile\">Testwert 2</tag2>\n" +
			"</start>", 
			bos.toString()
		);		
	}

	/**
	 * Tests DTD validation.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testDTDValidation() throws Exception
	{
		//ignores DTD validation
		readXml("video_dtd_not_valid.xml");
		
		readXml("video_dtd.xml", null, false, true);

		try
		{
			readXml("video_dtd_not_valid.xml", null, true, true);
			
			Assert.fail("DTD was not used for validation!");
		}
		catch (SAXParseException spe)
		{
			Assert.assertEquals("Element type \"music2\" must be declared.", spe.getMessage());
		}
	}
	
	/**
	 * Test schema validation.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSchemaValidationWithGivenSchema() throws Exception
	{
		try
		{
			readXml("video_xsd_not_valid.xml", "/com/sibvisions/util/xml/video.xsd", true, true);
		}
		catch (SAXParseException spe)
		{
			Assert.assertTrue(spe.getMessage(), StringUtil.like(spe.getMessage(),
					                            "cvc-complex-type.2.4.a: Invalid content was found starting with element 'music2'. One of '{*music}' is expected."));
		}
		
		readXml("video_xsd_not_valid.xml", "/com/sibvisions/util/xml/video.xsd", false, true);
		
		readXml("video_xsd.xml", "/com/sibvisions/util/xml/video.xsd", true, true);
	}

	/**
	 * Test schema validation.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testSchemaValidationWithInternalSchema() throws Exception
	{
		try
		{
			readXml("video_xsd_not_valid_schema_in_file.xml", null, true, true);
			
			Assert.fail("XSD Validation doesn't work!");
		}
		catch (SAXParseException spe)
		{
			Assert.assertTrue(spe.getMessage(), StringUtil.like(spe.getMessage(),
					                            "cvc-complex-type.2.4.a: Invalid content was found starting with element 'music2'. One of '{*music}' is expected."));
		}
	}
	
	/**
	 * Tests {@link XmlNode#getLineNumber()} and {@link XmlNode#getColumnNumber()}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testLineAndColumnNumber() throws Exception
	{
		XmlNode xmnRead = readXml("simple.xml");
		
		Assert.assertEquals(29, xmnRead.getNode("/server/tcp/pwd").getLineNumber());
		Assert.assertEquals(10, xmnRead.getNode("/server/tcp/pwd").getColumnNumber());
		
		Assert.assertEquals(49, xmnRead.getNode("/server/text(0)").getLineNumber());
		Assert.assertEquals(9, xmnRead.getNode("/server/text(0)").getColumnNumber());
	}

	/**
	 * Tests {@link XmlNode#indexOf(String, String, int)}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testIndexOf() throws Exception
	{
		XmlNode xmnRead = XmlNode.createXmlDeclaration();
		
		xmnRead.insertNode("/server/a/b/c", "A");
		xmnRead.insertNode("/server/a/b/c", "B");
		xmnRead.insertNode("/server/a/b/c", "A");
		xmnRead.insertNode("/server/a/b/c", "B");
		xmnRead.insertNode("/server/a/b/c", "A");
		xmnRead.insertNode("/server/a/b(1)/c", "A");
		xmnRead.insertNode("/server/a/b/b", "A");
		
		XmlNode xmnSearch = xmnRead.getNode("/server/a/b(0)"); 
		
		Assert.assertEquals(0, xmnSearch.indexOf("/c", "A", 0));
		Assert.assertEquals(2, xmnSearch.indexOf("/c", "A", 1));
		Assert.assertEquals(4, xmnSearch.indexOf("/c", "A", 3));
		Assert.assertEquals(0, xmnSearch.indexOf("/b", "A", 0));
		
		//add more b nodes -> indexOf should check the different nodes internal
		xmnRead.insertNode("/server/a/b(1)/1", "A");
		xmnRead.insertNode("/server/a/b(2)/2", "A");
		xmnRead.insertNode("/server/a(1)/b/3", "A");
		
		Assert.assertEquals(0, xmnRead.indexOf("/server/a/b/b", "A", 0));
		
        xmnRead = XmlNode.createXmlDeclaration();
        
        xmnRead.insertNode("/server/embedded/entry/info", new XmlNode("info"));
        xmnRead.insertNode("/server/embedded/entry/name", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "one"));
        xmnRead.insertNode("/server/embedded/entry(1)/info", new XmlNode("info"));
        xmnRead.insertNode("/server/embedded/entry(1)/name", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "two"));
        xmnRead.insertNode("/server/embedded/entry(2)/info", new XmlNode("info"));
        xmnRead.insertNode("/server/embedded/entry(2)/name", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "two"));
        xmnRead.insertNode("/server/embedded/entry(3)/info", new XmlNode("info"));
        xmnRead.insertNode("/server/embedded/entry(3)/name", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", "three"));

        XmlNode xmnS = xmnRead.getNode("/server/embedded");
        
        Assert.assertEquals(0, xmnS.indexOf("/entry/name", "one"));
        Assert.assertEquals(1, xmnS.indexOf("/entry/name", "two"));
        Assert.assertEquals(3, xmnS.indexOf("/entry/name", "three"));
        Assert.assertEquals(-1, xmnS.indexOf("/entry/name", "two", 3));
	}

	/**
	 * Tests attribute quoting.
	 */
	@Test
	public void testQuote293()
	{
		XmlNode ndMain = new XmlNode("main");
		XmlNode ndComent = new XmlNode(XmlNode.TYPE_ATTRIBUTE, "comment", "ABC.\".\" DEF", ndMain);
		
		Assert.assertEquals("<main comment=\"ABC.&quot;.&quot; DEF\"/>", ndMain.toString());
		Assert.assertEquals("<comment>ABC.&quot;.&quot; DEF</comment>", ndComent.toString());
	}

	/**
	 * Tests xml with namespaces.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testNs() throws Exception
	{
		XmlNode xmnRead = readXml("web.xml");
		
		byte[] byResult = FileUtil.getContent(ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/web.xml.result"));
		byte[] byRead = xmnRead.toString().replace("\n", "\r\n").getBytes();
		
		Assert.assertEquals(new String(byResult), new String(byRead));
	}

	/**
	 * Tests empty tag, attribute and comment values.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testEmptyValues351() throws Exception
	{
		XmlNode xmnRead = readXml("empty.xml");
		
		Assert.assertNull(xmnRead.getNode("/server/empty").getValue());
		Assert.assertNull(xmnRead.getNode("/server/filled/empty").getValue());
		Assert.assertNull(xmnRead.getNode("/server/comment").getValue());
		
	}

	/**
	 * Tests encryption.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testEncrypt() throws Exception
	{
		XmlWorker xmw = new XmlWorker();
		xmw.setEncrypted("/config/password", true);
		xmw.setEncrypted("/config/authentication/param/element", true);
		
		XmlNode xmnReadDec = readXml("decrypted.xml");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		xmw.write(baos, xmnReadDec);
		
		XmlNode xmnReadEnc = xmw.read(new ByteArrayInputStream(baos.toByteArray()));
		
		Assert.assertEquals(xmnReadDec.toString(), xmnReadEnc.toString());
	}

	/**
	 * Tests decryption.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testDecrypt() throws Exception
	{
		XmlWorker xmw = new XmlWorker();
		xmw.setEncrypted("/config/password", true);
		xmw.setEncrypted("/config/authentication/param/element", true);
		
		XmlNode xmnReadEnc = readXml(xmw, "encrypted.xml");
		XmlNode xmnReadDec = readXml(xmw, "decrypted.xml");
		
		Assert.assertEquals(xmnReadDec.toString(), xmnReadEnc.toString());
	}
	
	/**
	 * Tests decryption.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testAutomaticDecrypt() throws Exception
	{
		XmlWorker xmw = new XmlWorker();
		xmw.setAutomaticDecrypt(true);
		
		XmlNode xmnReadEnc = readXml(xmw, "encrypted.xml");
		XmlNode xmnReadDec = readXml(xmw, "decrypted.xml");
		
		Assert.assertEquals(xmnReadDec.toString(), xmnReadEnc.toString());
	}

	/**
	 * Tests doctype detection.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testDTD() throws Exception
	{
		XmlNode xmnRead = readXml("liferay-portlet.xml");

		Assert.assertEquals("<!DOCTYPE liferay-portlet-app PUBLIC \"-//Liferay//DTD Portlet Application 6.1.0//EN\" \"http://www.liferay.com/dtd/liferay-portlet-app_6_1_0.dtd\">", 
				            xmnRead.getNode("/DOCTYPE").toString());
	}

	/**
	 * Tests parsing with text that is split by other tags.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testElementSplit() throws Exception
	{
		XmlNode xmnRead = readXml("elementsplit.xml");
		
		String sResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
	    "<server>\n" +
		"  <text>\n" +
		"  Hans\n" +
		" Huber\n" +
		" Pauli\n" +
		"    <comment>C1</comment>\n" +
		"    <comment2>C2</comment2>\n" +
		"  </text>\n" +
		"  <text2>\n" +
		"  Das ist ein Text\n" +
		"von Hansi Huber.\n" +
		"  </text2>\n" +
		"  <text3>\n" +
		"  Das ist ein Text\n" +
		" von Hansi Huber.\n" +
		"  </text3>\n" +		
		"</server>";		
		
		Assert.assertEquals(sResult, xmnRead.toString());
	}
	
	/**
	 * Tests parsing whitespace count before and after protected characters.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testProtectedChars() throws Exception
	{
		String file = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		    "<message>Tag  &lt;Contact&gt;  of \n template</message>";
		
		XmlNode xml = new XmlWorker().read(new ByteArrayInputStream(file.getBytes("UTF-8")));
		
		
		Assert.assertEquals("Tag  <Contact>  of \n template", xml.getNodeValue("/message"));
	}

    /**
     * Tests parsing with CDATA.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testCDATA() throws Exception
    {
        XmlNode xmnCData = new XmlNode(XmlNode.TYPE_CDATA, "firstelement");
        xmnCData.setValue("Hallo Test\n><>>");

        Assert.assertEquals("<firstelement>\n<![CDATA[Hallo Test\n><>>]]>\n</firstelement>", xmnCData.toString());
        
        XmlNode xmnRead = readXml("cdata.xml", null, false, false);
 
        Assert.assertEquals(XmlNode.TYPE_CDATA, xmnRead.getNode("/server/text").getType());
        Assert.assertEquals("  This is some\n\tfunny\n\t\tmultine\ntext<<<>>\n \t  which should make trouble.", xmnRead.getNodeValue("/server/text"));
        
        Assert.assertEquals("  This is some\n\tfunny\n\t\tmultine\ntext\n \t  which should make trouble.", xmnRead.getNodeValue("/server/nocdata"));
        
        XmlNode xmnEmptyCDtata = new XmlNode(XmlNode.TYPE_CDATA, "value", "");
        
        Assert.assertEquals("<value><![CDATA[]]></value>", xmnEmptyCDtata.toString());
        
        Assert.assertEquals(XmlNode.TYPE_CDATA, xmnRead.getNode("/server/emptycdata").getType());
        Assert.assertEquals(XmlNode.TYPE_TEXT, xmnRead.getNode("/server/emptytext").getType());
    }	

    /**
     * Tests decrypt with umlauts.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testDecryptWithUmlauts1895() throws Exception
    {
        XmlNode xmn = new XmlNode("master");

        xmn.add(new XmlNode(XmlNode.TYPE_TEXT, "detail", "Ärü"));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        XmlWorker xmw = new XmlWorker();
        xmw.setEncrypted("/master/detail", true);
        xmw.write(baos, xmn);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        
        xmw = new XmlWorker();
        xmw.setAutomaticDecrypt(true);

        xmn = xmw.read(bais);
        
        Assert.assertEquals("Ärü", xmn.getNodeValue("/master/detail"));
    }
    
    /**
     * Tests special characters.
     * 
     * @throws Exception the characters
     */
    @Test
    public void testSpecialChar() throws Exception
    {
    	XmlNode xmnRead = readXml("specialchar.xml");
    	
    	System.out.println("V = " + xmnRead.getNodeValue("/Module/Graphics/GraphicsText"));
    }
    
}	// TestXmlWorker
