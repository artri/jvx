/*
 * Copyright 2014 SIB Visions GmbH
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
 * 25.03.2014 - [HM] - creation
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>XmlNodeSerializer</code> class is the type serializer implementation for {@link XmlNode}.
 *  
 * @author Martin Handsteiner
 */
public class XmlNodeSerializer implements ITypeSerializer<XmlNode>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>XmlNode</code> type. */
	public static final int TYPE_XMLNODE = 72;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<XmlNode> getTypeClass()
	{
		return XmlNode.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_XMLNODE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_XMLNODE;
	}

	/**
	 * {@inheritDoc}
	 */
	public XmlNode read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		XmlNode result = new XmlNode(pIn.readShort(), (String)pSerializer.read(pIn, pCache), (String)pSerializer.read(pIn, pCache));
		
		result.setNodes((List<XmlNode>)pSerializer.read(pIn, pCache));
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, XmlNode pObject, TypeCache pCache) throws Exception
	{
		pOut.writeByte(TYPE_XMLNODE);
		
		pOut.writeShort(pObject.getType());
		pSerializer.write(pOut, pObject.getName(), pCache);
		pSerializer.write(pOut, pObject.getValue(), pCache);
		pSerializer.write(pOut, pObject.size() == 0 ? null : pObject.getNodes(), pCache);
		//                      1 byte for null, 2 bytes for pObject.getNodes(), but reading does not unnecessarily create empty lists.
	}
	
}	// XmlNodeSerializer
