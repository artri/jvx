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
 * 13.05.2011 - [JR] - creation
 * 26.01.2012 - [JR] - #543: test case added
 */
package com.sibvisions.util.xml;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link XmlNode}.
 * 
 * @author René Jahn
 */
public class TestXmlNode extends XmlTestBase
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link XmlNode#getNodeValue(String)}.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testGetNodeValue352() throws Exception
	{
		XmlNode node = readXml("simple.xml");
		
		Assert.assertEquals("C:\\Temp\\smedia", node.getNodeValue("/server/media/directory"));
		Assert.assertNull("Value found for unknown node!", node.getNodeValue("/server/notfound"));
	}

	/**
	 * Tests comment encoding with special characters.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testCommentEncodingBug543() throws Exception
	{
		XmlNode node = readXml("comment_tags.xml");
		
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
                            "<info>\n" +
                            "  <!-- <company>http://www.sibvisions.com</company> -->\n" +
                            "</info>", node.toString());
		
		node = new XmlNode(XmlNode.TYPE_TEXT, "company", "<value>");
		
		Assert.assertEquals("<company>&lt;value&gt;</company>", node.toString());
	}
	
	/**
	 * Tests comment encoding with special characters.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testTextPart() throws Exception
	{
		XmlNode xmn = XmlNode.createXmlDeclaration();
        
		xmn.insertNode("/root", "[LOOP@datarows]");
		
		xmn.insertNode("/root/DataRow/FirstName", "[FIRST_NAME]");
		xmn.insertNode("/root/DataRow/LastName", "[FIRST_NAME]");

		xmn.getNode("/root").add(new XmlNode(XmlNode.TYPE_TEXTPART, XmlNode.NAME_TEXTPART, "[LOOP@datarows]"));
		
		System.out.println(xmn);
		
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
				            "<root>\n" + 
				            "[LOOP@datarows]\n" + 
				            "  <DataRow>\n" + 
				            "    <FirstName>[FIRST_NAME]</FirstName>\n" + 
				            "    <LastName>[FIRST_NAME]</LastName>\n" + 
				            "  </DataRow>\n" + 
				            "[LOOP@datarows]\n" + 
				            "</root>", xmn.toString());
	}

	/**
	 * Tests serialization.
	 * 
	 * @throws Exception if serialization fails
	 */
	@Test
	public void testSerialize() throws Exception
	{
        XmlNode xmn = readXml("simple.xml");
        serialize(xmn);
        serialize(xmn.getNode("/server/startport"));
        serialize(xmn.get(2));
        serialize(xmn.get(0));
        
        xmn = readXml("video_dtd.xml");
        serialize(xmn);
        
        for (int i = 0, cnt = xmn.size(); i < cnt; i++)
        {
            serialize(xmn.get(i));
        }
        
        xmn = readXml("video_xsd.xml");
        serialize(xmn);
        
        xmn = readXml("video_xsd_not_valid.xml");
        serialize(xmn);
	}

	/**
	 * Tests XML Tag name preparation.
	 */
	@Test
	public void testNumericTagName()
	{
	    XmlNode xmn = new XmlNode("8080");
	    
	    //The tag name is not valid, but our XmlNode shouldn't change it
	    
	    Assert.assertEquals("8080", xmn.getName());
	    
	    Assert.assertEquals("_8080", XmlNode.getValidTagName(xmn.getName()));
	}
	
	/**
	 * Tests setting a node with "/" identifier.
	 */
	@Test
	public void testSetNodeWithRoot()
	{
	    XmlNode xmn = new XmlNode("8080");
	    
	    try
	    {
	    	xmn.setNode("/", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "documentbase", "test"));
	    }
	    catch (Exception ex)
	    {
	    	Assert.fail("Setting root node failed with " + ex.getMessage());
	    }
	}

    /**
     * Tests root node access.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testGetNodeValueRoot() throws Exception
    {
        XmlNode node = readXml("simple.xml");

        Assert.assertEquals(node, node.getNode("/"));
        Assert.assertEquals(node, node.getNode(""));
        Assert.assertEquals(node, node.getNode(null));
    }
    
    /**
     * Tests setting attribute node.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testSetAttribute() throws Exception
    {
        XmlNode node = readXml("proxy.xml");
        
        node.setNode("/server/proxy/secure", "false");
        node.setNode("/server/proxy/secureNew", "true");
        node.setNode("/server/proxy/secureAttrib", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "secureAttrib", "false"));
        node.setNode("/server/proxy/secureExists", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "secureExists", "true"));
        
        System.out.println(node);
    	
    }

    /**
     * Tests getting valid tag name.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testValidTagName() throws Exception
    {
        Assert.assertEquals("_:messageööäa", XmlNode.getValidTagName(":messageööäa"));
        Assert.assertEquals("m_e:ssag-.eööäa", XmlNode.getValidTagName("\"m_#+*~e:s sag-.,eöö äa\""));
    }
    
    /**
     * Tests getting valid value.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testValidXmlValue() throws Exception
    {
        Assert.assertEquals("&#246;&#228;&#223;&#252;&#220;&#214;&#196;&lt;&gt;abc012-/", XmlNode.getXmlValue("öäßüÜÖÄ<>abc012-/"));
    }
    
}	// TestXmlNode
