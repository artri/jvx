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
 * 16.07.2009 - [JR] - set/getSchema, set/isValidationEnabled implemented
 *                   - ErrorHandler methods overwritten
 *                   - Locator cached
 * 09.03.2011 - [JR] - simple namespace support 
 * 13.05.2011 - [JR] - #351: at least one character for attributes and comments - otherwise null
 * 03.12.2011 - [JR] - #9: encrypt/decrypt nodes
 * 03.03.2012 - [JR] - #551: use StringBuilder and Element to remove string concatenate operations
 * 17.04.2013 - [JR] - disabled validation if not enabled (also disabled external resource downloads)
 *                   - DTD support                  
 * 14.05.2013 - [JR] - changed value building in characters method -> didn't understand the old implementation!
 * 16.05.2013 - [JR] - characters: length check [BUGFIX]    
 * 10.10.2013 - [JR] - #829: DTD validation OR XSD validation (dynamic validation)
 * 07.04.2015 - [JR] - check unrecognized features (not all JVMs has support for every feature)  
 * 25.02.2018 - [JR] - #1895: node decyption with correct charset
 */
package com.sibvisions.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Simple parser and writer for xml files. The parser extends the <code>DefaultHandler</code>
 * to overwrite the necessary functions.<br>
 * The newly created xml files will be encoded in UTF-8.
 * <p>
 * Example to work with a xml file:
 * 
 * <pre>
 * XmlWorker xmw = new XmlWorker();
 * 
 * XmlNode xmnRead;
 * XmlNode xmnChange;
 * 
 * ArrayList&lt;XmlNode&gt; alElements;
 * 
 * 
 * // Parse a xml file
 * xmnRead = xmw.read(new FileInputStream("example.xml"));
 *     
 * // Access a node list
 * alElements = xmnRead.getNodes("/archive/element");
 * 
 * // Count node list elements
 * System.out.println(xmnRead.size("/archive/element"));
 * 
 * // Access a node from a node list
 * System.out.println(xmnRead.getNode("/archive/element(0)"));
 * 
 * // Access a single node
 * System.out.println(xmnRead.getNode("/archive/name"));
 * 
 * // Access an attribute from a node
 * System.out.println(xmnRead.getNode("/archive/element/attribute0"));
 * 
 * // Change the value of a node
 * xmnRead.setNode("/archive/element(1)/user", "xml");
 * 
 * // Replace one node with another node (changes the node type to attribute and uses the new node name)
 * xmnChange = new XmlNode(XmlNode.TYPE_ATTRIBUTE, "new");
 * xmnChange.setValue("changed");
 * 
 * xmnRead.setNode("/archive/element(1)/type", xmnChange);
 * 
 * // Add a new node (the required elements (newelement, type) 
 * // will be created automaticly)
 * xmnRead.setNode("/archive/newelement/type", "new");
 *
 * // Set a new attribute with the name real_attrib_name
 * xmnRead.insertNode("/archive/element/attrib_name", new XmlNode(XmlNode.TYPE_ATTRIBUTE, "real_attrib_name", "value"));
 * 
 * // Remove a node
 * xmnRead.removeNode("/archive/element(2)");
 * 
 * // Insert a list-node with a value, between first and second element
 * xmnRead.insertNode("/archive/element(1)", "between");
 * 
 * // Insert a new-node without a value, between first and second element
 * xmnRead.insertNode("/archive/element(1)", new XmlNode("break"));
 *
 * // Insert a new-node without a value, as last node in the "element" list
 * xmnRead.insertNode("/archive/element", new XmlNode("last"));
 * 
 * // Save the new xml structure
 * xmw.write(new FileOutputStream("example_v2.xml"), xmnRead);
 * </pre>
 * 
 * @author René Jahn
 */
public class XmlWorker extends DefaultHandler 
                       implements LexicalHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Startnode of an xml file. */
	private XmlNode xmnStart = null;
	
	/** the document locator. */
	private Locator locator = null;
	
	/** List of opened/started tags. */
	private ArrayUtil<Element> auTags = null;
	
	/** Contains marks when an element value is splitted with sub elements. */
	private ArrayUtil<Boolean> auBreak = null;

	/** the list of namespaces. */
	private List<XmlNode> liNamespaces = null;
	
	/** Contains the resource path of the schema, if necessary. */
	private String sSchema = null;
	
	/** the reusable StringBuilder for value operations. */
	private StringBuilder sbValueTrim = null;
	
	/** the list of encrypted nodes. */
	private List<String> liEncryptedNodes = null;	
	
	/** xml output indentation (default: 2). */
	private int iIndentation = 2;
	
	/** the flag indicates whether new lines should be inserted for better readable xml files. */
	private boolean bInsertNewLines = false;
	
	/** whether or not the declaration element should be created automatically. */
	private boolean bCreateDeclaration = true;
	
	/** whether or not decryption should be done automatically. */
	private boolean bAutoDecrypt = false;
	
	/** whether validation should be done. */
	private boolean bValidation = false;
	
	/** whether whitespaces in tag values should be optimized/formatted. */
	private boolean bFormatWhitespaces = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void comment(char[] pChar, int pStart, int pLength) throws SAXException
	{
		XmlNode xmnComment = new XmlNode(XmlNode.TYPE_COMMENT, XmlNode.NAME_COMMENT);
		
		xmnComment.setLineNumber(locator.getLineNumber());
		xmnComment.setColumnNumber(locator.getColumnNumber());

		//at least one character
		if (pLength > 0)
		{
			String sComment = new String(pChar, pStart, pLength);
	
			xmnComment.setValue(trimValue(sComment, true));
		}
		
		//The current element will be added to the parent element.
		//Thats important to create the hirarchy
		auTags.get(auTags.size() - 1).node.add(xmnComment);
	}

	/**
	 * {@inheritDoc}
	 */
	public void startCDATA() throws SAXException
	{
        int iSize = auTags.size() - 1;

        Element elCurrent = auTags.get(iSize);
        
        if (elCurrent.value != null)
        {
        	if (elCurrent.text == null)
        	{
        		elCurrent.text = new ArrayUtil<Text>();
        	}
        	
        	elCurrent.text.add(new Text(new StringBuilder(elCurrent.value), elCurrent.cdata));
        	elCurrent.cdata = true;
            elCurrent.value.setLength(0);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public void endCDATA() throws SAXException
	{
        int iSize = auTags.size() - 1;

        Element elCurrent = auTags.get(iSize);
        
    	if (elCurrent.text == null)
    	{
    		elCurrent.text = new ArrayUtil<Text>();
    	}
    	
    	elCurrent.text.add(new Text(elCurrent.value != null ? new StringBuilder(elCurrent.value) : null, elCurrent.cdata));

    	elCurrent.value = null;
        
        elCurrent.cdata = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void startDTD(String name, String publicId, String systemId) throws SAXException
	{
		auTags.get(0).node.add(XmlNode.createDoctype(name, publicId, systemId));
	}

	/**
	 * {@inheritDoc}
	 */
	public void endDTD() throws SAXException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEntity(String name) throws SAXException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void endEntity(String name) throws SAXException
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * Caches the document locator for line numbering.
	 * 
	 * @param pLocator the locator instance
	 */
	@Override 
	public void setDocumentLocator(Locator pLocator)
	{
		locator = pLocator;
	}
	
    /**
	 * Throws the parse exception if validation is enabled.
	 * 
	 * @param pError {@inheritDoc}
	 * @throws SAXException {@inheritDoc}
	 * @see #setValidationEnabled(boolean)
	 */
	@Override 
	public void warning(SAXParseException pError) throws SAXException
	{
		if (isValidationEnabled())
		{
			throw pError;
		}
	}

	/**
	 * Throws the parse exception if validation is enabled.
	 * 
	 * @param pError {@inheritDoc}
	 * @throws SAXException {@inheritDoc}
	 * @see #setValidationEnabled(boolean)
	 */
	@Override 
	public void error(SAXParseException pError) throws SAXException
	{
		if (isValidationEnabled())
		{
			throw pError;
		}
	}
	
    /**
	 * Throws the parse exception if validation is enabled.
	 * 
	 * @param pError {@inheritDoc}
	 * @throws SAXException {@inheritDoc}
	 * @see #setValidationEnabled(boolean)
	 */
	@Override 
	public void fatalError(SAXParseException pError) throws SAXException
	{
		if (isValidationEnabled())
		{
			throw pError;
		}
	}
	
    /**
	 * {@inheritDoc}
	 */
	@Override 
	public void startDocument() 
	{ 
		if (bCreateDeclaration)
		{
			XmlNode xmnXml = XmlNode.createXmlDeclaration();
	
			auTags = new ArrayUtil<Element>();
			auTags.add(new Element(xmnXml));
		}
		
		auBreak = new ArrayUtil<Boolean>();		
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void endDocument() 
	{ 
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void startElement(String pNameSpaceURI, String pLocalName, String pName, Attributes pAttr) throws SAXException 
    { 
		XmlNode xmnNewElement = new XmlNode(pName);
		XmlNode xmnAttribute;

		int iLine = locator.getLineNumber();
		int iCol  = locator.getColumnNumber();
		
		xmnNewElement.setLineNumber(iLine);
		xmnNewElement.setColumnNumber(iCol);
		
		//Create the xml structure, only when there are TAGs
		if (xmnStart == null)
		{
			if (auTags != null)
			{
				xmnStart = auTags.get(0).node;
			}
			else
			{
				xmnStart = xmnNewElement;
				
				auTags = new ArrayUtil<Element>();
				auTags.add(new Element(xmnStart));
			}
		}

		//use namespaces
		if (liNamespaces != null)
		{
			for (XmlNode ndNs : liNamespaces)
			{
				xmnNewElement.add(ndNs);
			}
			
			liNamespaces = null;
		}
		
		String sValue;
		
		//use attributes
		for (int i = 0, anz = pAttr.getLength(); i < anz; i++)
		{
			xmnAttribute = new XmlNode(XmlNode.TYPE_ATTRIBUTE, pAttr.getQName(i));
			
			sValue = pAttr.getValue(i);
			
			//at least one character
			if (sValue != null && sValue.trim().length() > 0)
			{
				xmnAttribute.setValue(pAttr.getValue(i));
			}
			
			xmnAttribute.setLineNumber(iLine);
			xmnAttribute.setColumnNumber(iCol);
			
			xmnNewElement.add(xmnAttribute);
		}
		
		auTags.add(new Element(xmnNewElement));
		
		if (!auBreak.isEmpty())
		{
			//If we are a sub element, then the previous element will be
			//marked suspended.
			//-> Important for building the value
			auBreak.remove(auBreak.size() - 1);
			auBreak.add(Boolean.TRUE);
		}
		
		//always: will be removed in endElement
		auBreak.add(Boolean.FALSE);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void characters(char[] pChar, int pStart, int pLength)
	{
		Element elCurrent = auTags.get(auTags.size() - 1);
		
		StringBuilder sbTagValue = elCurrent.value;
		
		String sNewValue = new String(pChar, pStart, pLength);
		
		boolean bBreak = auBreak.get(auBreak.size() - 1).booleanValue(); 

		
		//Value is split with a sub element
        if (bBreak || sbTagValue == null || sbTagValue.length() == 0)
        {
        	auBreak.remove(auBreak.size() - 1);
        	auBreak.add(Boolean.FALSE);
        }				
		
		//use StringBuilder for fast processing
		if (sbTagValue == null)
		{
			sbTagValue = new StringBuilder();
			elCurrent.value = sbTagValue;
		}
			
		sbTagValue.append(sNewValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
    public void endElement(String pURI, String pLocalName, String pName) throws SAXException
    {
		int iSize = auTags.size() - 1;

		Element elCurrent = auTags.get(iSize);
		
		XmlNode xmnClose = elCurrent.node;
		
        if (elCurrent.text != null)
        {
        	if (elCurrent.value != null)
        	{
        		elCurrent.text.add(new Text(elCurrent.value, elCurrent.cdata));
        	}
        	
        	StringBuilder sbValueClose = new StringBuilder();

        	Text text;
        	
        	boolean bText = false;
        	
        	for (int i = 0, cnt = elCurrent.text.size(); i < cnt; i++)
        	{
        		text = elCurrent.text.get(i);
        		
        		if (text.cdata)
        		{
        			sbValueClose.append(text.value);
        		}
        		else
        		{
        			String sText;

        			if (text.value != null)
        			{
	        			if (i == 0 || i == cnt - 1)
	        			{
	        				sText = text.value.toString().trim();
	        			}
	        			else
	        			{
	        				sText = optimizeValue(text.value);
	        			}
	        			
	        			if (sText != null && sText.length() > 0)
	        			{
	            			bText = true;
	        				
	        				sbValueClose.append(sText);
	        			}
        			}
        		}
        	}
        	
        	if (bText)
        	{
        		//mixed content -> use text type
        		xmnClose.setType(XmlNode.TYPE_TEXT);
        	}
        	else
        	{
        		xmnClose.setType(XmlNode.TYPE_CDATA);
        	}

            xmnClose.setValue(sbValueClose.toString());
        }
        else
        {
    		String sValueClose = optimizeValue(elCurrent.value);
    		
    		if (sValueClose != null)
    		{
    			xmnClose.setValue(sValueClose);
    		}
        }
        
		//Assign element when it was closed!
		if (pName.equals(xmnClose.getName()))
		{
			auTags.remove(elCurrent);

			XmlNode xmnParent = auTags.get(iSize - 1).node;
			
			//possible if start element is not created automatically
			if (xmnParent != xmnClose)
			{
				//Add the current element to the parent element: create hirarchy
				xmnParent.add(xmnClose);
			}
			
			auBreak.remove(auBreak.size() - 1);
		}
		else
		{
			throw new SAXException("Element '" + pName + "' was not closed!");
		}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void startPrefixMapping(String pPrefix, String pUri)
    {
		if (liNamespaces == null)
		{
			liNamespaces = new ArrayUtil<XmlNode>();
		}
		
		String sPrefix;
		
		if (pPrefix != null && pPrefix.length() > 0)
		{
			sPrefix = "xmlns:" + pPrefix;
		}
		else
		{
			sPrefix = "xmlns"; 
		}
		
		liNamespaces.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, sPrefix, pUri));
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Parses a xml file and create a <code>XmlNode</code> structure.
	 * 
	 * @param pSource XML input file
	 * @return xml file in node structure
	 * @throws SAXException if the file has parse errors
	 * @throws IOException if any IO error occurs 
	 * @throws ParserConfigurationException if the SAX parser is not well configured
	 */
	public XmlNode read(File pSource) throws SAXException,
	                                         IOException,
	                                         ParserConfigurationException
	{
		return readAndClose(new FileInputStream(pSource));
    }

	/**
	 * Parses a xml file and create a <code>XmlNode</code> structure.
	 * This closes the stream after reading.
	 * 
	 * @param pXmlSource XML input source
	 * @return xml file in node structure
	 * @throws SAXException if the xml has parse errors
	 * @throws IOException if any IO error occurs 
	 * @throws ParserConfigurationException if the SAX parser is not well configured
	 */
	public XmlNode readAndClose(InputStream pXmlSource) throws SAXException,
	                                                           IOException,
	                                                           ParserConfigurationException
	{
		try
		{
			return read(pXmlSource);
		}
		finally
		{
			CommonUtil.close(pXmlSource);
		}
	}
	
	/**
	 * Parses a xml file and create a <code>XmlNode</code> structure.
	 * 
	 * @param pXmlSource XML input source
	 * @return xml file in node structure
	 * @throws SAXException if the xml has parse errors
	 * @throws IOException if any IO error occurs 
	 * @throws ParserConfigurationException if the SAX parser is not well configured
	 */
	public XmlNode read(InputStream pXmlSource) throws SAXException,
	                                                   IOException,
	                                                   ParserConfigurationException
	{
		SAXParserFactory saxpf = SAXParserFactory.newInstance();
		
		//Reset
		locator = null;
		
		xmnStart = null;
		auTags = null;
		auBreak = null;

		sbValueTrim = null;
		
		saxpf.setNamespaceAware(true);

		//Features overview: http://xerces.apache.org/xerces-c/program-sax2-3.html
		
		//use schema validation if necessary
		if (bValidation)
		{
			if (sSchema != null)
			{
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				StreamSource ssSchema = new StreamSource(ResourceUtil.getResourceAsStream(sSchema));
			   
				saxpf.setSchema(factory.newSchema(ssSchema));
			}
			else
			{
				//DTD or XSD validation
				saxpf.setFeature("http://xml.org/sax/features/validation", false);
				
				try
				{
    				saxpf.setFeature("http://apache.org/xml/features/validation/dynamic", true);
    				saxpf.setFeature("http://apache.org/xml/features/validation/schema", true);
				}
	            catch (SAXNotRecognizedException snre)
	            {
	                //no chance
	            }
			}
		}
		else
		{
			saxpf.setValidating(false);

			saxpf.setFeature("http://xml.org/sax/features/validation", false);
			
			try
			{
    			saxpf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    			saxpf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			}
			catch (SAXNotRecognizedException snre)
			{
			    //no chance
			}
			
			saxpf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			saxpf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);			
		}

		SAXParser saxp = saxpf.newSAXParser();

		if (bValidation && sSchema == null)
		{
			//this sets Schema validation enabled, but be careful if XML uses DTD validation instead of XSD validation.
			//To support dtd OR xsd validation, we must set "http://apache.org/xml/features/validation/dynamic" feature true
			saxp.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		}
		
		saxp.setProperty("http://xml.org/sax/properties/lexical-handler", this);
		saxp.parse(pXmlSource, this);

		return decrypt(xmnStart);
	}
	
	/**
	 * Writes the structure of a <code>XmlNode</code> as xml stream to the specified
	 * target file.
	 * 
	 * @param pTarget target file for the xml content
	 * @param pNode node for xml creation
	 * @throws IOException if the xml can not be created
	 */
	public void write(File pTarget, XmlNode pNode) throws IOException
	{
		FileOutputStream fos = null;
		
		try
		{
			fos = new FileOutputStream(pTarget);
			
			write(fos, pNode);
		}
		finally
		{
		    CommonUtil.close(fos);
		}
	}
	
	/**
	 * Writes the structure of a <code>XmlNode</code> as xml stream to the specified
	 * target stream.
	 * 
	 * @param pXmlTarget target stream for the xml content
	 * @param pNode node for xml creation
	 * @throws IOException if the xml can not be created
	 */
	public void write(OutputStream pXmlTarget, XmlNode pNode) throws IOException
	{
		XmlNode xmnDeclaration = null;

		XmlNode xmnWrite;
		
		if (liEncryptedNodes != null && !liEncryptedNodes.isEmpty())
		{
			//we need a copy, because we change the node if encryption is needed
			xmnWrite = (XmlNode)pNode.clone();
		}
		else
		{
			xmnWrite = pNode;
		}
		
		//The first element has to be a declaration element
		if (pNode.getType() != XmlNode.TYPE_DECLARATION)
		{
			xmnDeclaration = XmlNode.createXmlDeclaration();
			
			xmnDeclaration.add(xmnWrite);
		}
		else
		{
			xmnDeclaration = xmnWrite;
		}

		encrypt(xmnDeclaration);
		
		xmnDeclaration.createXml(pXmlTarget, iIndentation, bInsertNewLines);
		
		if (xmnWrite.getType() != XmlNode.TYPE_DECLARATION)
		{
			xmnDeclaration.remove(xmnWrite);
		}
	}
	
	/**
	 * Sets the xml output indentation.
	 * 
	 * @param pIndent space character count
	 */
	public void setIndentation(int pIndent)
	{
		this.iIndentation = pIndent;
	}
	
	/**
	 * Gets the xml output indentation. This methods doesn't support the
	 * intentation of read xml files.
	 * 
	 * @return space character count
	 */
	public int getIndentation()
	{
		return iIndentation;
	}
	
	/**
	 * Prepares a xml value for later use. The whitespace characters at the
	 * beginning and the end of the value will be removed.
	 * 
	 * @param pValue xml value
	 * @param pLineMode true if every line should be trimed separat. The delimiter is <code>\n</code>
	 * @return xml value without leading and trailing whitespaces
	 */
	private String trimValue(String pValue, boolean pLineMode)
	{
		if (pValue == null)
		{
			return null;
		}
		
		pValue = pValue.trim();
		
		if (pValue.length() == 0)
		{
			return null;
		}
		
		//If desired, the string will be processed line oriented:
		//Every line will be trimed and not only the result string
		if (pLineMode)
		{
			ArrayUtil<String> auLines = StringUtil.separateList(pValue, "\n", true);
			
			if (!auLines.isEmpty())
			{
				if (sbValueTrim == null)
				{
					sbValueTrim = new StringBuilder();
				}
				else
				{
					sbValueTrim.setLength(0);
				}
    			
    			for (int i = 0, anz = auLines.size(); i < anz; i++)
    			{
    				String sLine = auLines.get(i);
    				
    				if (sbValueTrim.length() > 0)
    				{
    					sbValueTrim.append("\n");
    				}
    				
    				sbValueTrim.append(sLine);
    			}
    			
    			pValue = sbValueTrim.toString();
    			
				sbValueTrim.setLength(0);
			}
		}
		
		return pValue;
	}
	
	/**
	 * Sets the resource name of the schema file for validation of the xml.
	 * 
	 * @param pSchema the resource name of the schema file /package/schema.xsd
	 */
	public void setSchema(String pSchema)
	{
		sSchema = pSchema;
	}
	
	/**
	 * Gets the resource name of the schema file for validation of the xml.
	 * 
	 * @return the resource name or <code>null</code> if schema validation is disabled
	 */
	public String getSchema()
	{
		return sSchema;
	}
	
	/**
	 * Set the general validation of xml en- or disabled. The validation is disabled by default.
	 * 
	 * @param pValidation <code>true</code> to enable the general validation or <code>false</code>
	 *                    to disable it
	 * @see #isValidationEnabled()
	 */
	public void setValidationEnabled(boolean pValidation)
	{
		bValidation = pValidation;
	}

	/**
	 * Gets the validation state.
	 * 
	 * @return <code>true</code> if validation of xml is enabled, <code>false</code> otherwise
	 * @see #setValidationEnabled(boolean)
	 */
	public boolean isValidationEnabled()
	{
		return bValidation;  
	}

	/**
	 * Sets whether new lines should be inserted for better readable xml files. This option is
	 * only used when a node will be written to an {@link OutputStream}.
	 * 
	 * The default setting is <code>false</code>.
	 * 
	 * @param pInsertNewLines <code>true</code> to insert new lines
	 * @see #write(OutputStream, XmlNode)
	 */
	public void setInsertNewLines(boolean pInsertNewLines)
	{
		bInsertNewLines = pInsertNewLines;
	}
	
	/**
	 * Gets whether new lines will be inserted for better readable.
	 * 
	 * @return <code>true</code> if new lines will be inserted, <code>false</code> otherwise
	 * @see #setInsertNewLines(boolean)
	 */
	public boolean isInsertNewLines()
	{
		return bInsertNewLines;
	}
	
	/**
	 * Sets the encryption mode of a node.
	 * 
	 * @param pNodeName the node name without index. It is not possible to encrypt single list elements.
	 * @param pEncrypt <code>true</code> to encrypt the node, <code>false</code> otherwise
	 */
	public void setEncrypted(String pNodeName, boolean pEncrypt)
	{
		if (pEncrypt)
		{
			if (liEncryptedNodes == null)
			{
				liEncryptedNodes = new ArrayUtil<String>();
			}
			
			if (liEncryptedNodes.indexOf(pNodeName) < 0)
			{
				liEncryptedNodes.add(pNodeName);
			}
		}
		else if (liEncryptedNodes != null)
		{
			liEncryptedNodes.remove(pNodeName);
			
			if (liEncryptedNodes.isEmpty())
			{
				liEncryptedNodes = null;
			}
		}
	}
	
	/**
	 * Gets whether the node with the given name should be encrypted.
	 * 
	 * @param pNodeName the node name without index
	 * @return <code>true</code> if the node name is encrypted
	 */
	public boolean isEncrypted(String pNodeName)
	{
		return liEncryptedNodes != null && liEncryptedNodes.contains(pNodeName);
	}

	/**
	 * Sets whether automatic decryption should be used.
	 * 
	 * @param pAutoDecrypt <code>true</code> to decrypt all tags with the attribute <code>encoded="true"</code>,
	 *                     <code>false</code> to ignore automatic decryption
	 */
	public void setAutomaticDecrypt(boolean pAutoDecrypt)
	{
		bAutoDecrypt = pAutoDecrypt;
	}
	
	/**
	 * Gets whether automatic decryption is enabled.
	 * 
	 * @return <code>true</code> if automatic node decryption is enabled, <code>false</code> otherwise
	 */
	public boolean isAutomaticDecrypt()
	{
		return bAutoDecrypt;
	}
	
	/**
	 * Encrypts the given XmlNode with predefined settings.
	 * 
	 * @param pNode the original node
	 * @return the encrypted node(s)
	 * @see #setEncrypted(String, boolean)
	 */
	private XmlNode encrypt(XmlNode pNode)
	{
		for (XmlNode node : pNode.getNodes())
		{
			encrypt(node);
		}
		
		if (liEncryptedNodes != null
			&& pNode.getType() != XmlNode.TYPE_DECLARATION && liEncryptedNodes.contains(pNode.getFullName()))
		{
			XmlNode node = new XmlNode(XmlNode.TYPE_TEXT, pNode.getName(), encrypt(pNode.toString()));
			node.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "encrypted", "true"));
			
			XmlNode.replace(pNode, node);
			
			return node;
		}
		
		return pNode;
	}

	/**
	 * Decrypts the given node.
	 * 
	 * @param pNode the encrypted node(s)
	 * @return the decrypted node
	 * @throws SAXException if the xml has parse errors
	 * @throws IOException if any IO error occurs 
	 * @throws ParserConfigurationException if the SAX parser is not well configured
	 */
	private XmlNode decrypt(XmlNode pNode) throws IOException,
	                                              SAXException,
	                                              ParserConfigurationException
	{
		for (XmlNode node : pNode.getNodes())
		{
			decrypt(node);
		}
		
		if (pNode.getType() != XmlNode.TYPE_DECLARATION && Boolean.parseBoolean(pNode.getNodeValue("encrypted")) 
			&& (bAutoDecrypt
				|| (liEncryptedNodes != null && liEncryptedNodes.contains(pNode.getFullName()))))
		{
			String sDecrypted = decrypt(pNode.getValue());
			
			if (sDecrypted != null)
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(sDecrypted.getBytes("UTF-8"));
				
				boolean bOldCreateDeclaration = bCreateDeclaration;
	
				try
				{
					bCreateDeclaration = false;

					XmlNode node = read(bais);
					
					XmlNode.replace(pNode, node);
					
					return node;
				}
				finally
				{
					bCreateDeclaration = bOldCreateDeclaration;
				}
			}
		}
		
		return pNode;
	}
	
	/**
	 * Encrypts the given text.
	 * 
	 * @param pText the plain text
	 * @return the encrypted text
	 */
	private String encrypt(String pText)
	{
		if (pText == null)
		{
			return null;
		}
		
		try
		{
			//NO real protection but not human readable!

			ByteArrayOutputStream baos = new ByteArrayOutputStream();  

			GZIPOutputStream zos = new GZIPOutputStream(baos);
			zos.write(pText.getBytes("UTF-8"));
			
			zos.close();
			baos.close();
		
			return CodecUtil.encodeHex(baos.toByteArray());
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Decrypts the given text.
	 * 
	 * @param pText the encrypted text
	 * @return the decrypted text
	 */
	private String decrypt(String pText)
	{
		try
		{
			byte[] byValues = CodecUtil.decodeHexAsBytes(pText);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(byValues);
			
			GZIPInputStream zis = new GZIPInputStream(bis);
			
		    return new String(FileUtil.getContent(zis, true), "UTF-8");
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Reads a file and returns the node.
	 * 
	 * @param pFile the file
	 * @return the parsed xml node
	 * @throws Exception if reading file failed
	 */
	public static XmlNode readNode(File pFile) throws Exception
	{
	    return new XmlWorker().read(pFile);
	}
	
    /**
     * Reads a stream and returns the node.
     * 
     * @param pStream the stream
     * @return the parsed xml node
     * @throws Exception if reading file failed
     */
	public static XmlNode readNode(InputStream pStream) throws Exception
	{
	    return new XmlWorker().read(pStream);
	}
	
	/**
	 * Writes a node into the given file.
	 * 
	 * @param pTarget target file for the xml content
	 * @param pNode node for xml creation
	 * @throws IOException if the xml can not be created
	 */
	public static void writeNode(File pTarget, XmlNode pNode) throws IOException
	{
		new XmlWorker().write(pTarget, pNode);
	}
	
	/**
	 * Writes a node into the given stream.
	 * 
	 * @param pXmlTarget target stream for the xml content
	 * @param pNode node for xml creation
	 * @throws IOException if the xml can not be created
	 */
	public static void writeNode(OutputStream pXmlTarget, XmlNode pNode) throws IOException
	{
		new XmlWorker().write(pXmlTarget, pNode);
	}
	
	/**
	 * Sets whether to format whitespaces in tag values.
	 * 
	 * @param pFormat <code>true</code> to format, <code>false</code> to keep the value as it is
	 */
	public void setFormatWhitespaces(boolean pFormat)
	{
	    bFormatWhitespaces = pFormat;
	}
	
	/**
	 * Gets whether to format whitespaces in tag values.
	 * 
	 * @return <code>true</code> if whitespaces in tag values will be formatted, <code>false</code> otherwise
	 */
	public boolean isFormatWhitespaces()
	{
	    return bFormatWhitespaces;
	}
	
	/**
	 * Optimizes the given value and removes whitespaces when needed.
	 * 
	 * @param pValue the value to use
	 * @return the optimized value
	 */
	private String optimizeValue(StringBuilder pValue)
	{
		//cleanup
		if (pValue != null)
		{
		    if (bFormatWhitespaces)
		    {
    			//optimize \n followed by whitespaces -> reduce to "\n "
    			int i = 0;
    			
    			char ch;
    			
    			StringBuilder sbValue = new StringBuilder(pValue.toString().trim());
    			
    			while (i < sbValue.length())
    			{
    				ch = sbValue.charAt(i);
    				
    				if (ch == '\n')
    				{
    					for (int j = i, anz = sbValue.length(); j < anz; j++)
    					{
    						if (!Character.isWhitespace(sbValue.charAt(j)))
    						{
    							if (j - i > 1)
    							{
    								sbValue = sbValue.replace(i, j, "\n ");
    								
    								i++;
    							}
    
    							j = anz;
    						}
    					}
    				}
    				
    				i++;
    			}
    			
                if (sbValue.length() > 0)
                {
                	return sbValue.toString();
                }
		    }
		    else
		    {
		    	return pValue.toString();
		    }
		}
		
		return null;
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>Element</code> class is used to save an {@link XmlNode} togeter with the expected
	 * value. The value is a growing string, until the tag ends in the xml structure. It is important
	 * to use a growing string, because of performance with string concatenate.
	 * 
	 * @author René Jahn
	 */
	private static final class Element
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the xml node. */
		private XmlNode node;
		
		/** the node value. */
		private StringBuilder value = null;
		
		/** cdata entries. */
		private List<Text> text = null;
		
		/** cdata. */
		private boolean cdata;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Element</code> for the given node.
		 * 
		 * @param pNode the XML node
		 */
		private Element(XmlNode pNode)
		{
			node = pNode;
		}
		
	}	// Element
	
	/**
	 * The <code>Text</code> is a text part from parsed XML file.
	 * 
	 * @author René Jahn
	 */
	private static final class Text
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** whether the text is cdata. */
		private boolean cdata;
		
		/** the value. */
		private StringBuilder value;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Text</code>.
		 * 
		 * @param pValue the value
		 * @param pCData <code>true</code> if text is CDATA
		 */
		private Text(StringBuilder pValue, boolean pCData)
		{
			value = pValue;
			cdata = pCData;
		}
		
	}	// Text
	
}	// XmlWorker
