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
 */
package com.sibvisions.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Locale;

import org.junit.Assert;

import com.sibvisions.rad.remote.ByteSerializer;
import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Base class for XML tests..
 * 
 * @author René Jahn
 * @see com.sibvisions.util.xml.XmlWorker
 * @see com.sibvisions.util.xml.XmlNode
 */
public class XmlTestBase
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Reads in a xml file.
	 * 
	 * @param pFileName name of the xml file (without path and leading slash)
	 * @return xml file as <code>XmlNode</code>
	 * @throws Exception if the read process causes problems
	 */
	public XmlNode readXml(String pFileName) throws Exception
	{
		return readXml(pFileName, null, false, true);
	}

	/**
	 * Read in a xml file without validation options. You can en-/disable validation. The
	 * validation without schema validates against a DTD.
	 * 
	 * @param pFileName name of the xml file (without path and leading slash)
	 * @param pSchema the resource path to the schema. It's important to set the parameter <code>pValidation</code>
	 *                to <code>true</code> if you wish to enable the validation
	 * @param pValidation <code>true</code> to enable the validation
	 * @param pFormat <code>true</code> to format tag values
	 * @return xml file as <code>XmlNode</code>
	 * @throws Exception if the read process causes problems
	 */
	public XmlNode readXml(String pFileName, String pSchema, boolean pValidation, boolean pFormat) throws Exception
	{
		//jdk 1.7 translates messages with default locale
		Locale locOld = Locale.getDefault();
		
		try
		{
			Locale.setDefault(Locale.ENGLISH);
			
			XmlWorker xmp = new XmlWorker();
			xmp.setValidationEnabled(pValidation);
			xmp.setFormatWhitespaces(pFormat);
			xmp.setSchema(pSchema);
	
			return readXml(xmp, pFileName);
		}
		finally
		{
			Locale.setDefault(locOld);
		}
	}	
	
	/**
	 * Read in a xml.
	 *
	 * @param pWorker the xml worker
	 * @param pFileName name of the xml file (without path and leading slash)
	 * @return xml file as <code>XmlNode</code>
	 * @throws Exception if the read process causes problems
	 */
	public XmlNode readXml(XmlWorker pWorker, String pFileName) throws Exception
	{
		return pWorker.read(ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/" + pFileName));	
	}

    /**
     * Serializes the given XML node via {@link ByteSerializer}.
     * 
     * @param pNode the XML node
     * @throws Exception if serialization fails
     */
    @SuppressWarnings("deprecation")
    protected void serialize(XmlNode pNode) throws Exception
    {
    	com.sibvisions.rad.remote.ByteSerializer ser = new com.sibvisions.rad.remote.ByteSerializer();
    	ser.setObjectStreamEnabled(true);
    	
        serialize(pNode, ser);
    }

    /**
     * Serializes the given XML node with the given {@link ISerializer}.
     * 
     * @param pNode the XML node
     * @param pSerializer the serializer
     * @throws Exception if serialization fails
     */
    protected void serialize(XmlNode pNode, ISerializer pSerializer) throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        DataOutputStream dos = new DataOutputStream(baos);
        
        pSerializer.write(dos, pNode);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        
        DataInputStream dis = new DataInputStream(bais);
        
        XmlNode xmnResult = (XmlNode)pSerializer.read(dis);
        
        Assert.assertEquals(pNode.toString(), xmnResult.toString());
    }
    
}	// TestXmlWorker
