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
 * 04.06.2009 - [JR] - getSubNodes made public
 *                   - getNodes(null) calls getSubNodes
 * 16.07.2009 - [JR] - set/getLineNumber implemented  
 * 30.11.2010 - [JR] - getNode(String, int): 
 *                     * don't allow indexes > 0 when no element is in the list
 *                     * insert when sub list is empty [BUGFIX]
 *                     * insert creates hierarchy
 *                   - setNode, insertNode 
 *                     * removed clone because it destroys everything (not possible to remove same object)
 *                     * replace nodes
 *                   - insert: reparent nodes
 *                   - indexOf implemented
 * 21.02.2011 - [JR] - #293: createXmlStructure: attribute value quoting
 * 09.03.2011 - [JR] - createXmlStructure: insert new lines option     
 * 10.03.2011 - [JR] - implemented toString(boolean), getXmlValue       
 * 03.12.2011 - [JR] - #9: getFullName implemented    
 * 26.01.2012 - [JR] - #543: don't prepare comment characters   
 * 17.04.2013 - [JR] - DTD support
 * 18.02.2014 - [JR] - implemented Serializable
 */
package com.sibvisions.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Internalize;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>XmlNode</code> encapsulates the information of a xml tag.
 * A node can be a comment, tag or xml. The xml type is the first
 * node of a xml file. The xml node contains all other sub xml nodes.
 *   
 * @author René Jahn
 */
public final class XmlNode implements Cloneable,
                                      Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Constant value for the type of declaration elements. */
	public static final short TYPE_DECLARATION = -1;

	/** Constant value for the doctype. */
	public static final short TYPE_DOCTYPE = -2;
	
	/** Constant value for the type of comment nodes. */
	public static final short TYPE_COMMENT = 0;
	
	/** Constant value for the type of standard nodes. */
	public static final short TYPE_TEXT = 1;

    /** Constant value for the type of attributes. */
	public static final short TYPE_ATTRIBUTE = 2;
	
	/** Constant value for the type of standard nodes. */
	public static final short TYPE_TEXTPART = 3;

    /** Constant value for the type of standard nodes. */
    public static final short TYPE_CDATA = 4;
	
	/** Constant value for the name of comment nodes. */
	public static final String NAME_COMMENT = "comment";

	/** Constant value for the name of comment nodes. */
	public static final String NAME_TEXTPART = "textpart";

	/** Constant value the name of the declaration node (<code>&lt;?xml?&gt;</code>). */
	public static final String NAME_XMLDECLARATION = "xml";

	/** Constant value the name of the doctype node (<code>&lt;?xml?&gt;</code>). */
	public static final String NAME_DOCTYPE = "DOCTYPE";
	
	/** Constant value for getting a node. */
	private static final int MODE_GET = 0;
	
	/** Constant value for getting a node list. */
	private static final int MODE_GET_LIST = 1;

	/** Constant value for setting a node. */
	private static final int MODE_SET = 2;
	
	/** Constant value for deleting a node. */
	private static final int MODE_DELETE = 3;
	
	/** Constant value for counting node occurrence. */
	private static final int MODE_COUNT = 4;
	
	/** Constant value for inserting a node at a defined position. */
	private static final int MODE_INSERT = 5;
	
	/** A cache for building indents. */
	private static StringBuilder sbIndentCache = new StringBuilder();

	/** parent node, when the current node is a sub node. */
	private transient XmlNode xmnParent = null;
	
	/** Node name. */
	private transient String sName = null;
	
	/** Node value. */
	private transient String sValue = null;
	
	/** list of all sub nodes with the original sort order. */
	private transient ArrayUtil<XmlNode> auSubNodes = null;
	
	/** the line number of the tag occurence. */
	private transient int iLineNumber = -1;
	
	/** the column number of the tag occurence. */
	private transient int iColumnNumber = -1;
	
	/** node type (default: {@link #TYPE_TEXT}. */
	private transient short shType = XmlNode.TYPE_TEXT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>XmlNode</code>
	 * without a parent. The node type is {@link #TYPE_TEXT}.
	 * 
	 * @param pName name of the node
	 */
	public XmlNode(String pName)
	{
		this(XmlNode.TYPE_TEXT, pName, null, null);
	}

	/**
	 * Creates a new instance of <code>XmlNode</code> without a parent.
	 * 
	 * @param pType node type
	 * @param pName name of the node
	 */
	public XmlNode(short pType, String pName)
	{
		this(pType, pName, null, null);
	}
	
	/**
	 * Creates a new instance of <code>XmlNode</code> without a parent.
	 * 
	 * @param pType node type
	 * @param pValue value of the node
	 * @param pName name of the node
	 */
	public XmlNode(short pType, String pName, String pValue)
	{
		this(pType, pName, pValue, null);
	}

	/**
	 * Creates a new instance of <code>XmlNode</code> with a parent.
	 * 
	 * @param pType node type
	 * @param pName name of the node
	 * @param pParentNode parent <code>XmlNode</code> or <code>null</code>
	 */
	public XmlNode(short pType, String pName, XmlNode pParentNode)
	{
		this(pType, pName, null, pParentNode);
	}
	
	/**
	 * Creates a new instance of <code>XmlNode</code>.
	 *
	 * @param pType node type
	 * @param pName name of the node
	 * @param pValue value of the node
	 * @param pParentNode parent <code>XmlNode</code> or <code>null</code>
	 */
	public XmlNode(short pType, String pName, String pValue, XmlNode pParentNode)
	{
		this.shType = pType;
		this.sName = Internalize.intern(pName);
		this.sValue = Internalize.intern(pValue);

		if (pParentNode != null)
		{
			pParentNode.add(this);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public String toString() 
	{ 
		return toString(false);
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public Object clone()
	{
		XmlNode xmnClone = new XmlNode(getType(), getName(), getValue());
		
		//Don't set the PARENT because the parent contains the original node!
		//If we write the parent to a stream, it contains not the clone data!
		
		if (auSubNodes != null)
		{
			for (int i = 0, anz = auSubNodes.size(); i < anz; i++)
			{
				xmnClone.add((XmlNode)auSubNodes.get(i).clone());
			}
		}
		
		return xmnClone;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Clones the given node, and ensures, that the node name is correct for the given destination key.
	 * @param pDestinationKey the destination key.
	 * @return the cloned node with correct name.
	 */
	public XmlNode cloneAs(String pDestinationKey)
	{
		String key;
		if (pDestinationKey == null)
		{
			key = getName();
		}
		else
		{
			key = pDestinationKey;
			if (key.endsWith("/"))
			{
				key = key.substring(0, key.length() - 1);
			}
			int index = key.lastIndexOf('/');
			if (index >= 0)
			{
				key = key.substring(index + 1);
			}
		}
		
		XmlNode xmnClone = new XmlNode(getType(), key, getValue());

		List<XmlNode> subNodes = getNodes();
		for (int i = 0, anz = subNodes.size(); i < anz; i++)
		{
			xmnClone.add((XmlNode)subNodes.get(i).clone());
		}

		return xmnClone;
	}

	/**
	 * Clones the given node, and ensures, that the node name is correct for the given destination key.
	 * @param pDestinationKey the destination key.
	 * @param pNode the node.
	 * @return the cloned node with correct name.
	 */
	public static XmlNode cloneAs(String pDestinationKey, XmlNode pNode)
	{
		if (pNode == null)
		{
			return null;
		}
		else
		{
			return pNode.cloneAs(pDestinationKey);
		}
	}

	/**
	 * Sets the type.
	 * 
	 * @param pType the type
	 */
	void setType(short pType)
	{
	    shType = pType;
	}
	
	/**
	 * Gets the node from the given index.
	 * 
	 * @param pIndex the index.
	 * @return the node.
	 */
	public XmlNode get(int pIndex)
	{
		if (auSubNodes == null)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + " and should be between 0 and size 0!");
		}
		else
		{
			return auSubNodes.get(pIndex);
		}
	}
	
	/**
	 * Gets the first text node.
	 * In case of declaration node, this is the root node, or null, if it does not exist.
	 * 
	 * @return the first text node (root node).
	 */
	public XmlNode getFirstTextNode()
	{
		if (auSubNodes != null)
		{
			for (XmlNode node : auSubNodes)
			{
				if (node.getType() == TYPE_TEXT
				    || node.getType() == TYPE_CDATA)
				{
					return node;
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds a <code>XmlNode</code> as a known sub node, at the end of
	 * the known sub nodes list.
	 * 
	 * @param pNode instance of the sub node
	 */
	public void add(XmlNode pNode)
	{
		insert(size(), pNode);
	}
	
	/**
	 * Inserts a <code>XmlNode</code> as a known sub node at a defined
	 * position, in the sub node list.
	 * 
	 * @param pPosition position at which the specified element is to be inserted
	 * @param pNode instance of the sub node
	 */
	public void insert(int pPosition, XmlNode pNode)
	{
		//Instantiation on demand
		if (auSubNodes == null)
		{
			auSubNodes = new ArrayUtil<XmlNode>(1);
		}
	
		//Save the element order
		auSubNodes.add(pPosition, pNode);
		
		//remove from parent
		if (pNode.xmnParent != null)
		{
			pNode.xmnParent.remove(pNode);
		}
		
		//Save the reference to the parent node, for sub nodes
		pNode.xmnParent = this;
	}
	
	/**
	 * Adds all the nodes as a known sub node, at the end of
	 * the known sub nodes list.
	 * 
	 * @param pNodes the list of nodes
	 */
	public void addAll(Collection<XmlNode> pNodes)
	{
		insertAll(size(), pNodes);
	}
	
	/**
	 * Adds all the nodes as a known sub node, at the end of
	 * the known sub nodes list.
	 * 
	 * @param pIndex the index where to insert
	 * @param pNodes the list of nodes
	 */
	public void insertAll(int pIndex, Collection<XmlNode> pNodes)
	{
		if (pNodes != null)
		{
			for (XmlNode node : pNodes)
			{
				insert(pIndex++, node);
			}
		}
	}
	
	/**
	 * Gets the list of sub <code>XmlNode</code> elements.
	 * null if there are no sub nodes.
	 * 
	 * @return list of sub elements
	 * @deprecated since 2.6 use {@link #getNodes()} instead.
	 */
	@Deprecated
	public List<XmlNode> getSubNodes()
	{
		return auSubNodes;
	}
	
	/**
	 * Sets the list of sub <code>XmlNode</code> elements.
	 * 
	 * @param pSubNodes list of sub elements
	 * @deprecated since 2.6 use {@link #setNodes(List)} instead.
	 */
	@Deprecated
	public void setSubNodes(List<XmlNode> pSubNodes)
	{
		setNodes(pSubNodes);
	}

	/**
	 * Sets the list of sub <code>XmlNode</code> elements.
	 * if the given list is an ArrayUtil, it is not cloned and used 
	 * directly. This is for memory and performance reasons. 
	 * 
	 * @param pNodes list of sub elements
	 */
	public void setNodes(List<XmlNode> pNodes)
	{
		clearSubNodes();
		
		if (pNodes != null && pNodes.size() > 0)
		{
			if (pNodes instanceof ArrayUtil)
			{
				auSubNodes = (ArrayUtil)pNodes;
			}
			else
			{
				auSubNodes = new ArrayUtil<XmlNode>(pNodes);
			}

			for (int i = 0, size = auSubNodes.size(); i < size; i++)
			{
				XmlNode xmnSub = auSubNodes.get(i);
				if (xmnSub.xmnParent != null)
				{
					xmnSub.xmnParent.remove(xmnSub);
				}
				
				//Save the reference to the parent node, for sub nodes
				xmnSub.xmnParent = this;
			}
		}
	}
	
	/**
	 * Gets the list of sub <code>XmlNode</code> elements.
	 * The result is an empty or unmodifiable list of sub nodes.
	 * This is for memory and performance reasons using big xml files. 
	 * 
	 * @return list of sub elements
	 */
	public List<XmlNode> getNodes()
	{
		if (auSubNodes == null)
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			return Collections.unmodifiableList(auSubNodes);
		}
	}

	/**
	 * Gets the list of sub <code>XmlNode</code> elements of the given type.
	 * The result is an empty list, if no sub nodes with the type exists.
	 * 
	 * @param pType the type
	 * @return list of sub elements
	 */
	public List<XmlNode> getNodes(short pType)
	{
		if (auSubNodes == null)
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			List<XmlNode> result = new ArrayList<XmlNode>();
			
			for (XmlNode node : auSubNodes)
			{
				if (node.getType() == pType)
				{
					result.add(node);
				}
			}

			return result;
		}
	}

	/**
	 * Remove a sub <code>XmlNode</code> from the known sub nodes.
	 * 
	 * @param xmnSub instance of the sub node
	 * @return true if the node was removed otherwise false
	 */
	public boolean remove(XmlNode xmnSub)
	{
		if (auSubNodes != null)
		{
			boolean bRemove = auSubNodes.remove(xmnSub); 
			
			if (bRemove)
			{
				xmnSub.xmnParent = null;
			}
			
			if (auSubNodes.isEmpty())
			{
				auSubNodes = null;
			}
			
			return bRemove;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Remove a sub <code>XmlNode</code> from the known sub nodes.
	 * 
	 * @param pIndex the index
	 * @return the removed <code>XmlNode</code>
	 */
	public XmlNode remove(int pIndex)
	{
		if (auSubNodes == null)
		{
			throw new IndexOutOfBoundsException("The index is " + pIndex + " and should be between 0 and size 0!");
		}
		else
		{
			XmlNode node = auSubNodes.remove(pIndex); 
			
			if (node != null)
			{
				node.xmnParent = null;
			}
			
			if (auSubNodes.isEmpty())
			{
				auSubNodes = null;
			}
			
			return node;
		}
	}
	
	/**
	 * Removes this node from its parent.
	 * 
	 * @return <code>true</code> if removed or <code>false</code> otherwise
	 */
	public boolean removeFromParent()
	{
		if (xmnParent != null)
		{
			return xmnParent.remove(this);
		}
		
		return false;
	}

	/**
	 * Clears the list of known sub elements.
	 */
	public void clearSubNodes()
	{
		if (auSubNodes != null)
		{
			for (int i = 0, size = auSubNodes.size(); i < size; i++)
			{
				auSubNodes.get(i).xmnParent = null;
			}
			
			auSubNodes.clear();
			
			auSubNodes = null;
		}
	}
	
	/**
	 * Gets the parent <code>XmlNode</code>.
	 * 
	 * @return parent <code>XmlNode</code> or <code>null</code> if the
	 *         node has no parent
	 */
	public XmlNode getParent()
	{
		return xmnParent;
	}
	
	/**
	 * Gets the node type.
	 * 
	 * @return type of the node ({@link #TYPE_ATTRIBUTE}, {@link #TYPE_COMMENT}, {@link #TYPE_TEXT},  
	 *         {@link #TYPE_CDATA}, {@link #TYPE_DECLARATION}), {@link #TYPE_DOCTYPE})  
	 */
	public short getType()
	{
		return shType;
	}

	/**
	 * Gets the node name.
	 * 
	 * @return the node name
	 */
	public String getName()
	{
		return sName;
	}
	
	/**
	 * Gets the absolute name without indizes.
	 * 
	 * @return the absolute name e.g. /config/sub/object/element
	 */
	String getFullName()
	{
		String sFullName;
		
		if (xmnParent != null 
			&& xmnParent.shType != XmlNode.TYPE_DECLARATION
			&& xmnParent.shType != XmlNode.TYPE_DOCTYPE)
		{
			sFullName = xmnParent.getFullName() + "/" + sName;
		}
		else
		{
			sFullName = "/" + sName; 
		}
		
		return sFullName;
	}
	
	/**
	 * Sets the node value.
	 * 
	 * @param pValue node value
	 */
	public void setValue(String pValue)
	{
		this.sValue = Internalize.intern(pValue);
	}
	
	/**
	 * Gets the node value.
	 * 
	 * @return node value
	 */
	public String getValue()
	{
		return sValue;
	}
	
	/**
	 * Gets the XML representation of this nodes value.
	 * 
	 * @return the value as XML value
	 */
	private String getXmlValue()
	{
		if (shType == TYPE_CDATA)
		{
			if (sValue == null || sValue.length() == 0)
			{
				return "<![CDATA[]]>";
			}
			
			return "<![CDATA[" + sValue + "]]>";
		}
		else
		{
		    return getXmlValue(sValue);
		}
	}
	
	/**
	 * Returns the given value as XML value.
	 * 
	 * @param pValue the value
	 * @return the value as XML value
	 */
	public static String getXmlValue(String pValue)
	{
		if (pValue == null || pValue.length() == 0)
		{
			return null;
		}
		
		return prepareCharacters(pValue);
	}
	
	/**
	 * Gets a specific (sub) <code>XmlNode</code> based on the current
	 * <code>XmlNode</code>. To get a special element of a node list, use
	 * brackets to define the element position e.g. /document/nodes(2)
	 * 
	 * @param pNodePath Node path e.g. /document/singlenode
	 * @return null if the <code>XmlNode</code> is not present or the position is invalid, 
	 *         otherwise the <code>XmlNode</code>
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public XmlNode getNode(String pNodePath)
	{
		if (StringUtil.isEmpty(pNodePath) || "/".equals(pNodePath))
		{
			return this;
		}
		else
		{
			return (XmlNode)getNode(pNodePath, XmlNode.MODE_GET);
		}
	}
	
	/**
	 * Gets the value of a specific (sub) <code>XmlNode</code> based on the current
	 * <code>XmlNode</code>. To get a special element of a node list, use
	 * brackets to define the element position e.g. /document/nodes(2)
	 * 
	 * @param pNodePath Node path e.g. /document/singlenode
	 * @return null if the <code>XmlNode</code> is not present or the position is invalid, 
	 *         otherwise the value of the <code>XmlNode</code>
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public String getNodeValue(String pNodePath)
	{
		if (StringUtil.isEmpty(pNodePath) || "/".equals(pNodePath))
		{
			return getValue();
		}
		else
		{
			XmlNode node = (XmlNode)getNode(pNodePath, XmlNode.MODE_GET);
			
			if (node == null)
			{
				return null;
			}
			else
			{
				return node.getValue();
			}
		}
	}
	
	/**
	 * Gets a list of <code>XmlNode</code>s based on the current
	 * <code>XmlNode</code>. If you define a special list element, 
	 * you will get a list with only one element instead of all 
	 * list elements.
	 * 
	 * @param pNodePath Node path e.g. /document/nodelist or <code>null</code> to get all sub nodes
	 * @return null if the <code>XmlNode</code> is not present or the position is invalid, 
	 *         otherwise the list with <code>XmlNode</code>s
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 * @see #getNodes()                                 
	 */
	public List<XmlNode> getNodes(String pNodePath)
	{
		if (StringUtil.isEmpty(pNodePath) || "/".equals(pNodePath))
		{
			return getNodes();
		}
		else
		{
			return (List<XmlNode>)getNode(pNodePath, XmlNode.MODE_GET_LIST);
		}
	}
	
	/**
	 * Count the elements of a node list.
	 * 
	 * @param pNodePath Node path e.g. /document/nodelist
	 * @return 0 if the list is empty or the element does not exists, otherwise the element count
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public int getNodeCount(String pNodePath)
	{
		if (StringUtil.isEmpty(pNodePath) || "/".equals(pNodePath))
		{
			if (auSubNodes == null)
			{
				return 0;
			}
			else
			{
				return auSubNodes.size();
			}
		}
		else
		{
			Integer iSize = (Integer)getNode(pNodePath, XmlNode.MODE_COUNT);
			
			if (iSize == null)
			{
				return 0;
			}
			else
			{
				return iSize.intValue();
			}
		}
	}
	
	/**
	 * Returns the number of sub elements. The <code>size()</code> includes
	 * all element types (comment, attribute, ...). 
	 * 
	 * @return the number of sub elements
	 */
	public int size()
	{
		if (auSubNodes != null)
		{
			return auSubNodes.size();
		}
		
		return 0;
	}
	
	/**
	 * Returns the number of sub elements of the defined type. 
	 * 
	 * @param pType type of sub elements to count
	 * @return the number of sub elements
	 */
	public int size(short pType)
	{
		if (auSubNodes != null)
		{
			int iSize = 0;

			for (int i = 0, anz = auSubNodes.size(); i < anz; i++)
			{
				XmlNode xmn = auSubNodes.get(i);
				
				if (pType == xmn.getType())
				{
					iSize++;
				}
			}
			
			return iSize;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Removes a specific (sub) <code>XmlNode</code> based on the current
	 * <code>XmlNode</code>.
	 * 
	 * @param pNodePath Node path e.g. /document/singlenode
	 * @return null if the <code>XmlNode</code> is not present or the position is invalid, 
	 *         otherwise the removed <code>XmlNode</code>
	 * @see #getNode(String)
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public XmlNode removeNode(String pNodePath)
	{
		return (XmlNode)getNode(pNodePath, XmlNode.MODE_DELETE);
	}
	
	/**
	 * Sets a specific (sub) <code>XmlNode</code> based on the current
	 * <code>XmlNode</code>. If the hierarchy is not present, this method
	 * will create the necessary elements.
	 * 
	 * @param pNodePath Node path e.g. /document/singlenode
	 * @param pValue the <code>pValue</code> can be an instance of <code>XmlNode</code> or any other object. If 
	 *               the <code>pValue</code> is not an instance of <code>XmlNode</code> then the node value will 
	 *               be set to <code>pValue.toString()</code>. Otherwise, if <code>pValue</code> is 
	 *               an instance of <code>XmlNode</code>, the <code>pValue</code> will be used as replacement 
	 *               for the specified node (the name of the node will be used instead of the name defined in the path). 
	 *               The node value will be set to <code>null</code> if <code>pValue == null</code>.
	 * @return null if the <code>XmlNode</code> is not present or the position is invalid, 
	 *         otherwise the new <code>XmlNode</code>
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public XmlNode setNode(String pNodePath, Object pValue)
	{
		XmlNode xmnSet = (XmlNode)getNode(pNodePath, XmlNode.MODE_SET);
		
		
		if (pValue instanceof XmlNode)
		{
			replace(xmnSet, (XmlNode)pValue);
			
			return (XmlNode)pValue; 
		}
		else
		{
			if (pValue != null)
			{
				xmnSet.setValue(pValue.toString());
			}
			else
			{
				xmnSet.setValue(null);
			}

			return xmnSet;
		}
	}
	
	/**
	 * Inserts a specific <code>XmlNode</code> at a defined position
	 * based on the current <code>XmlNode</code>. If the hierarchy is not present, this method
	 * will create the necessary elements. 
	 * 
	 * @param pNodePath Node path with or without position e.g. /document/singlenode(2). The
	 *                  <code>pValue</code> will be inserted before the found node or after
	 *                  the last occurence of the <code>pNodePath</code>, if the position is <code>{@link #size()} + 1</code>
	 *                  or not defined.
	 * @param pValue the value can be an instance of <code>XmlNode</code> or any other object. If
	 *               <code>pValue</code> is not an instance of <code>XmlNode</code>, the node
	 *               value will be <code>pValue.toString()</code>. If it is an instance of
	 *               <code>XmlNode</code> then the <code>pValue</code> will be used as node (the name of the node
	 *               will be used instead of the name defined in the path). 
	 *               The node value will be set to <code>null</code> if <code>pValue == null</code>.
	 * @return null if the position, if needed, is invalid or <code>pValue</code> is null, 
	 *         otherwise the inserted <code>XmlNode</code>
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	public XmlNode insertNode(String pNodePath, Object pValue)
	{
		XmlNode xmnInsert = (XmlNode)getNode(pNodePath, XmlNode.MODE_INSERT);
		
		
		if (pValue instanceof XmlNode)
		{
			replace(xmnInsert, (XmlNode)pValue);
			
			return (XmlNode)pValue;
		}
		else
		{
			if (pValue != null)
			{
				xmnInsert.setValue(pValue.toString());
			}
			else
			{
				xmnInsert.setValue(null);
			}

			return xmnInsert;
		}
	}
	
	/**
	 * Performs some operations, like GET, SET, DELETE on special nodes.
	 * 
	 * @param pNodePath Node path e.g. /document/singlenode
	 * @param pMode operation mode: {@link #MODE_GET}, {@link #MODE_SET}, {@link #MODE_DELETE}
	 * @return affected <code>XmlNode</code>
	 * @throws IllegalArgumentException if the path contains an incomplete or an invalid index
	 * @throws IndexOutOfBoundsException if the path contains an index which is smaller or 
	 *                                   larger than the allowed length 
	 */
	private Object getNode(String pNodePath, int pMode)
	{
		XmlNode xmnSearch = null;
		XmlNode xmnFound  = null;
		
		List<String> liPath;
		
		List<XmlNode> alSearchSubNodes;
		List<XmlNode> alFoundNodes = null;
		
		String sPath;
		
		int iElementPosStart;
		int iElementPosEnd;
		int iElementPos;
		int iElementCount;
		int iSize = 0;
		int iLastFoundElementIndex;
		
		boolean bFound = false;
		boolean bCancel = false;
		
		boolean bIsLastPathElement;
		boolean bUseMaxIndex;
		
		//Ignore delimiter on first position! 
		if (pNodePath.charAt(0) == '/')
		{
			pNodePath = pNodePath.substring(1);
		}
		
		liPath = StringUtil.separateList(pNodePath, "/", true);		
		
		for (int i = 0, anzPath = liPath.size(); i < anzPath; i++)
		{
			sPath = liPath.get(i);
			
			bIsLastPathElement = (i == anzPath - 1);
			
			//------------------------------------------------
			// search the desired element possition, if
			// specified
			// The position will be defined with brackets: (..)
			//------------------------------------------------
			
			iElementPosStart = sPath.indexOf('(');
			iElementPosEnd   = -1;
			iElementPos      = -1;
			iElementCount    = 0;
			iLastFoundElementIndex = -1;
			
			//the first character can not be a position indication!
			if (iElementPosStart > 0)
			{
				iElementPosEnd = sPath.indexOf(')', iElementPosStart + 1);
				
				if (iElementPosEnd <= iElementPosStart)
				{
					//Invalid position
					throw new IllegalArgumentException("Invalid index position: [start: " + iElementPosStart + ", end: " + iElementPosEnd);
				}
				
				try
				{
					iElementPos = Integer.parseInt(sPath.substring(iElementPosStart + 1, iElementPosEnd));
				}
				catch (NumberFormatException nfe)
				{
					//Invalid position
					throw new IllegalArgumentException("Invalid index: " + sPath.substring(iElementPosStart + 1, iElementPosEnd));
				}

				sPath = sPath.substring(0, iElementPosStart);
			}
			
			//use the element count as index (e.g. insert without index)
			bUseMaxIndex = bIsLastPathElement && pMode == XmlNode.MODE_INSERT && iElementPos < 0; 
			
			//------------------------------------------------
			// Prepare element search
			//------------------------------------------------
			
			if (xmnSearch == null)
			{
				//Start search depending on the current object
				xmnSearch = this;
			}

			bFound = false;
			
			alSearchSubNodes = xmnSearch.auSubNodes;
			
			//Attention, the list will be instantiated if there is at least one sub element!
			if (alSearchSubNodes != null)
			{
				//------------------------------------------------
				// Plausibility check before we start with our
				// search
				//------------------------------------------------
				
				//At this position we have to use ALL sub elements for the check, otherwise we have
				//to count the elements
				//-> For performance reasons it is sufficient to use ALL elements
				if (iElementPosStart > 0)
				{
					//When a position is specified, it must also be possible!
					switch (pMode)
					{
						case XmlNode.MODE_GET:
						case XmlNode.MODE_GET_LIST:
						case XmlNode.MODE_COUNT:
							//Object can not be found!
							if (iElementPos < 0 || iElementPos > alSearchSubNodes.size() - 1)
							{
								return null;
							}
							break;
							
						case XmlNode.MODE_INSERT:
						case XmlNode.MODE_SET:
							//It's always possible to add an element to the end of the list!
							if (iElementPos < 0 || iElementPos > alSearchSubNodes.size())
							{
    							//Invalid index
    							throw new IndexOutOfBoundsException("Invalid index: " + iElementPos);
							}
							break;

						default:
							if (iElementPos < 0 || iElementPos > alSearchSubNodes.size() - 1)
							{
    							throw new IndexOutOfBoundsException("Invalid index: " + iElementPos);
							}
					}
				}

				//------------------------------------------------
				// Perform search
				//------------------------------------------------

				bCancel = false;
				
				//Search until all elements was processed or an element was found
				for (int j = 0, anzSubs = alSearchSubNodes.size(); j < anzSubs && !bCancel; j++)
				{
					xmnFound = alSearchSubNodes.get(j);
					
					if (sPath.equals(xmnFound.getName()))
					{
						if (iElementPos >= 0 || bUseMaxIndex)
						{
							if (iElementCount == iElementPos)
							{
								xmnSearch = xmnFound;
								bFound = true;
								bCancel = true;
							}
								
							iElementCount++;
							iLastFoundElementIndex = j;
						}
						else
						{
							//Don't use "multi-check" for the last search-path, otherwise it's not possible to
							//perform counting or list detection
							if ((pMode != XmlNode.MODE_COUNT && pMode != XmlNode.MODE_GET_LIST) || !bIsLastPathElement)
							{
								//-> insert is allowed without index -> the node will be added at the end!
								if (pMode != XmlNode.MODE_INSERT || iElementPos >= 0)
								{
									//There must be no more nodes, otherwise it can not be clearly decided
									//which node to take
									for (int k = j + 1; k < anzSubs; k++)
									{
										if (sPath.equals(alSearchSubNodes.get(k).getName()))
										{
											throw new IllegalArgumentException("Missing index for " + sPath);
										}
									}
								}

								xmnSearch = xmnFound;
							}
							
							bFound = true;
							bCancel = true;
							
							iLastFoundElementIndex = 0;
						}

						if (bIsLastPathElement && bFound)
						{
							//last element: build the element list
							if (pMode == XmlNode.MODE_COUNT || pMode == XmlNode.MODE_GET_LIST)
							{
								//Iterate ALL elements, if not a specific element was indicated
								bCancel = iElementPos >= 0;
								
								if (pMode == XmlNode.MODE_COUNT)
								{
									iSize++;
								}
								else
								{
									if (alFoundNodes == null)
									{
										alFoundNodes = new ArrayUtil<XmlNode>();
									}
									
									alFoundNodes.add(xmnFound);
								}
							}
						}
					}
				}
			}
			else if (iElementPos > 0)
			{
				//No sub elements -> don't allow an index > 0 because it is not clear for the developer
				
				//Invalid index
				throw new IndexOutOfBoundsException("Invalid index: " + iElementPos);
			}
 			
			if (!bFound)
			{
				if (pMode == XmlNode.MODE_SET || pMode == XmlNode.MODE_INSERT)
				{
					//If a node will be inserted, but without a specific index, then we insert the node 
					//at the end of the list
					if (bUseMaxIndex)
					{
						iElementPos = iElementCount;
					}
					
					//The index, if present, may not have the element count + 1!
					//(If there are two elements, then an insert is possible for position 3 (means index = 2))
					//The check can be done only after completing the count and not in the above
					//plausibility check, because there it is not known which element is how often available!
					
					if (bIsLastPathElement && iElementPos >= 0 && iElementPos > iElementCount)
					{
						throw new IndexOutOfBoundsException("The size is " + iElementCount + " and the index is " + iElementPos);
					}

					//If an element should be inserted at the end, then the element index is not available!
					//but the element does!
					//
					//
					//<server>
					//  <input/>
					//  <output/>
					//  <output/>
					//</server>
					//
					//The call: setNode("/server/input(1)", null) should return following result:
					//
					//<server>
					//  <input/>
					//  <input/>
					//  <output/>
					//  <output/>
					//</server>
					//
					//und nicht:
					//
					//<server>
					//  <input/>
					//  <output/>
					//  <output/>
					//  <input/>
					//</server>
					
                    if (pMode == XmlNode.MODE_INSERT && iLastFoundElementIndex >= 0)
                    {
                    	XmlNode xmnInsert = new XmlNode(XmlNode.TYPE_TEXT, sPath);
                    	
                    	xmnSearch.insert(iLastFoundElementIndex + 1, xmnInsert);
                    	
                    	xmnSearch = xmnInsert;
                    }
                    else
                    {						
                    	//Create the element structure
    					xmnSearch = new XmlNode(XmlNode.TYPE_TEXT, sPath, xmnSearch);
                    }    					
				}
				else
				{
					//nothing found -> stop
					return null;
				}
			}
			else if (bIsLastPathElement)
			{
				//When the element was found -> remove without side effects!
				if (pMode == XmlNode.MODE_DELETE)
				{
					xmnSearch.getParent().remove(xmnSearch);
				}
				else if (pMode == XmlNode.MODE_INSERT && iLastFoundElementIndex >= 0)
				{
					XmlNode xmnInsert = new XmlNode(XmlNode.TYPE_TEXT, sPath);
					
					//ATTENTION -> add to the parent, because xmnSearch was already replaced!
					xmnSearch.getParent().insert(iLastFoundElementIndex, xmnInsert);
					
					xmnSearch = xmnInsert;
				}
			}
		}
		
		//------------------------------------------------
		// Return the result dependent of the mode
		//------------------------------------------------
		
		switch (pMode)
		{
			case XmlNode.MODE_GET_LIST:
				return alFoundNodes;
			
			case XmlNode.MODE_COUNT:
				return Integer.valueOf(iSize);
				
			default:
				return xmnSearch;
		}
	}
	
	/**
	 * Create the xml structure based on the current node.
	 * 
	 * @param pOut stream for writing the xml structure
	 * @param pIndent indentation for sub nodes
	 * @param pInsertNewLines <code>true</code> to insert new lines
	 * @throws IOException if the output operation to <code>pOut</code> fails or
	 *                     the encoding UTF-8 is not usable
	 */
	public void createXml(OutputStream pOut, int pIndent, boolean pInsertNewLines) throws IOException
	{
		if (pOut != null)
		{
			OutputStreamWriter osw = new OutputStreamWriter(pOut, "UTF-8");
			
			if (getType() == XmlNode.TYPE_DECLARATION)
			{
				createXmlStructure(osw, -1, pIndent, pInsertNewLines);
			}
			else
			{
				createXmlStructure(osw, 0, pIndent, pInsertNewLines);
			}
			
			osw.flush();
		}
	}
	
	/**
	 * Gets the indent.
	 * @param pIndent the indent.
	 * @return the indent string.
	 */
	private static String getIndent(int pIndent)
	{
		if (pIndent > sbIndentCache.length())
		{
			synchronized (sbIndentCache)
			{
				for (int i = sbIndentCache.length(); i < pIndent; i++)
				{
					sbIndentCache.append(' ');
				}
			}
		}
		
		return sbIndentCache.substring(0, pIndent);
	}
	
	/**
	 * Create the xml structurce, recursive, based on the current node.
	 * 
	 * @param pOut writer for writing the xml structure
	 * @param pDepth hierarchy depth
	 * @param pIndent indentation for sub nodes
	 * @param pInsertNewLines <code>true</code> to insert new lines
	 * @throws IOException if the output operation to <code>pOut</code> fails
	 */
	private void createXmlStructure(Writer pOut, int pDepth, int pIndent, boolean pInsertNewLines) throws IOException
	{
		String sFormatedValue;
		
		boolean bIsMultiLine = false;
		boolean bIsEmpty = false;
		boolean bHasSubNodes = false;
		
		int iSubElementCount = 0;
		
		//------------------------------------------------
		// not output stream -> finish
		//------------------------------------------------

		if (pOut == null)
		{
			return;
		}
		
		//------------------------------------------------
		// Identify indentation
		//------------------------------------------------
		
		String sIndent = getIndent(Math.max(0, pDepth) * pIndent);
		if (shType == TYPE_TEXTPART && sIndent.length() >= pIndent)
		{
			pOut.append(sIndent.substring(pIndent));
		}
		else
		{
			pOut.append(sIndent);
		}
		
		if (shType == XmlNode.TYPE_COMMENT)
		{
			sFormatedValue = sValue;
			
	        if (sFormatedValue != null) 
	        {
	        	//every comment gets a new line
        		sFormatedValue = sFormatedValue.replace("\n", "\n" + sIndent);
	        }			
		}
		else
		{
			sFormatedValue = getXmlValue();
		}
	        
        if (sFormatedValue != null) 
        {
        	bIsMultiLine = sFormatedValue.indexOf('\n') >= 0;
        }	        
		
		//------------------------------------------------
		// Comments: one line per comment or multiline
		//------------------------------------------------
		
		if (shType == XmlNode.TYPE_COMMENT)
		{
			if (pInsertNewLines && pDepth == 1)
			{
				pOut.append('\n');
				pOut.append(sIndent);
			}
			
			pOut.append("<!--");
			
			if (!bIsMultiLine)
			{
				pOut.append(' ');
			}
			else
			{
				pOut.append('\n');
				pOut.append(sIndent);
			}
			
			pOut.append(sFormatedValue);
			
			if (!bIsMultiLine)
			{
				pOut.append(' ');
			}
			else
			{
				pOut.append('\n');
				pOut.append(sIndent);
			}
			
			pOut.append("-->");
			
			//not important for top level, because the line break will be print out
			//with the subnode loop
			if (pDepth > 0)
			{
				pOut.append("\n");
			}
		}
		else
		{
			//------------------------------------------------
    		// Print out the element
			//------------------------------------------------
			
    		if (shType == XmlNode.TYPE_DECLARATION)
    		{
    			pOut.append("<?");
    		}
    		else if (shType == XmlNode.TYPE_DOCTYPE)
    		{
    			pOut.append("<!");
    		}
    		else if (shType != TYPE_TEXTPART)
    		{
    			if (pInsertNewLines && pDepth == 1)
    			{
	    			pOut.append('\n');
	    			pOut.append(sIndent);
    			}
    			
    			pOut.append("<");    			
    		}
    		
    		if (shType != TYPE_TEXTPART)
    		{
    			pOut.append(getName());
    		}
    		
    		if (shType == XmlNode.TYPE_DOCTYPE)
    		{
    			String sNodeName = getNodeValue("name");
    			
    			if (sNodeName != null)
    			{
    				pOut.append(" ");
    				pOut.append(sNodeName);
    			}

    			String sPublicId = getNodeValue("publicId");
    			
    			if (sPublicId != null)
    			{
        			pOut.append(" PUBLIC");
	    			pOut.append(" \"");
	    			pOut.append(sPublicId);
	    			pOut.append("\"");
    			}
    			
    			String sSystemId = getNodeValue("systemId");
    			
    			if (sSystemId != null)
    			{
    				if (sPublicId == null)
    				{
    					pOut.append(" SYSTEM");
    				}
    				
	    			pOut.append(" \"");
	    			pOut.append(sSystemId);
	    			pOut.append("\"");
    			}
    		}
    		else
    		{
				//------------------------------------------------
	    		// Include element attributes
				//------------------------------------------------
	
	    		if (auSubNodes != null)
	    		{
	    			String sTempValue;
	    			
	    			//Print out all sub nodes
	        		for (int i = 0, anz = auSubNodes.size(); i < anz; i++)
	        		{
	        			XmlNode xmnSub = auSubNodes.get(i);
	        			
	        			//Ignore sub nodes from attribute elements!
	        		
	        			if (xmnSub.getType() == XmlNode.TYPE_ATTRIBUTE)
	        			{
	        				pOut.append(" ");
	        				pOut.append(xmnSub.getName());
	        				pOut.append("=\"");
	        				
	        				sTempValue = xmnSub.getXmlValue();
	        				
	        				if (sTempValue != null)
	        				{
	        					pOut.append(sTempValue);
	        				}
	        				
	        				pOut.append("\"");
	        			}
	        			else
	        			{
	        				iSubElementCount++;
	        			}
	        		}
	    		}
    		}
    		
			//------------------------------------------------
    		// TAG Start complete
    		//
    		// - Consider empty tags
    		// - Consider declaration tags
			//------------------------------------------------
    		
    		boolean bEmptyValue = sFormatedValue == null || sFormatedValue.length() == 0;

    		bHasSubNodes = (auSubNodes != null && iSubElementCount > 0);
    		bIsEmpty     = bEmptyValue && !bHasSubNodes;
    		
    		if (shType == XmlNode.TYPE_DECLARATION)
    		{
    			pOut.append("?>");
    		}
    		else if (shType == XmlNode.TYPE_DOCTYPE)
    		{
    			//Elements with sub elemets or a value
    			pOut.append(">");
    		}
    		else if (shType != TYPE_TEXTPART)
    		{
    			if (bIsEmpty)
    			{
	    			//Empty elements
	    			pOut.append("/>");
    			}
    			else
    			{
        			//Elements with sub elemets or a value
        			pOut.append(">");
    			}
    		}
    		
			//------------------------------------------------
    		// Print out the value
			//------------------------------------------------
    		
    		if (!bEmptyValue)
    		{
    			//The value will be print out in the same line if there are no sub elements
    			//and the value is not multi line
    			if (bIsMultiLine || bHasSubNodes)
    			{
    				pOut.append("\n");
    				pOut.append(sIndent);
    			}
    			
    			pOut.append(sFormatedValue);

    			if (bIsMultiLine && !bHasSubNodes)
    			{
    				pOut.append("\n");
    				pOut.append(sIndent);
    			}
    		}
    		
			//------------------------------------------------
    		// Print out sub elements
			//------------------------------------------------
    		
    		if (iSubElementCount > 0)
    		{
				pOut.append("\n");
				
				short shSubType;
				
        		for (int i = 0, anz = auSubNodes.size(); i < anz; i++)
        		{
        			XmlNode xmnSub = auSubNodes.get(i);
        			
        			shSubType = xmnSub.getType();
        			
        			//Attributes are already printed out
        			if (shSubType != XmlNode.TYPE_ATTRIBUTE)
        			{
        				//e.g. important if there are comments before and after the first TAG
        				//     or to insert space between the declaration element and the first
        				//     TAG
            			if (pDepth < 0)
            			{
            				if (shSubType != XmlNode.TYPE_DOCTYPE)
            				{
            					pOut.append("\n");
            				}
            			}

            			xmnSub.createXmlStructure(pOut, pDepth + 1, pIndent, pInsertNewLines);
            			
        				if (pDepth < 0 && shSubType == XmlNode.TYPE_DOCTYPE)
        				{
        					pOut.append("\n");
        				}
        			}
        		}
        		
        		pOut.append(sIndent);
    		}
    		
			//------------------------------------------------
    		// Complete TAG
			//------------------------------------------------

    		if (pInsertNewLines && pDepth == 0)
    		{
    			pOut.append("\n");
    		}

    		//Empty, declaration and doctype elements do not have an end TAG
    		if (shType != XmlNode.TYPE_DECLARATION
    			&& shType != XmlNode.TYPE_DOCTYPE
    			&& shType != XmlNode.TYPE_TEXTPART
    			&& !bIsEmpty)
    		{
    			pOut.append("</");
    			pOut.append(getName());
    			pOut.append(">");
    		}
    		
    		if (pDepth > 0)
    		{
    			pOut.append("\n");
    		}
		}
	}
	
	/**
	 * Creates the xml declaration tag &lt;?xml version=\"1.0\" encoding=\"utf-8\"&gt;.
	 * 
	 * @return xml declaration node
	 */
	public static XmlNode createXmlDeclaration()
	{
		XmlNode xmnDeclaration = new XmlNode(XmlNode.TYPE_DECLARATION, XmlNode.NAME_XMLDECLARATION);
		
		xmnDeclaration.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "version", "1.0", xmnDeclaration));
		xmnDeclaration.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "encoding", "UTF-8", xmnDeclaration));
		
		return xmnDeclaration;
	}
	
	/**
	 * Creates the xml doctype tag &lt;!DOCTYPE name PUBLIC \"publicId\" \"systemId\"&gt;.
	 * 
	 * @param pName the doctype name
	 * @param pPublicId the public id
	 * @param pSystemId the system id
	 * @return xml doctype node
	 */
	public static XmlNode createDoctype(String pName, String pPublicId, String pSystemId)
	{
		XmlNode xmnDoctype = new XmlNode(XmlNode.TYPE_DOCTYPE, XmlNode.NAME_DOCTYPE);
		
		xmnDoctype.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "name", pName, xmnDoctype));
		xmnDoctype.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "publicId", pPublicId, xmnDoctype));
		xmnDoctype.add(new XmlNode(XmlNode.TYPE_ATTRIBUTE, "systemId", pSystemId, xmnDoctype));

		return xmnDoctype;
	}
	
	/**
	 * Replace special, not allowed, characters in xml values.
	 * 
	 * @param sText original text
	 * @return new text with replaced characters
	 */
	private static String prepareCharacters(String sText)
	{
		StringBuilder sbNewText = new StringBuilder();
		
		char chText;
		
		for (int i = 0; i < sText.length(); i++)
		{
			chText = sText.charAt(i);
			
			switch (chText)
			{
				case '&': 
					sbNewText.append("&amp;"); 
					break;
				case '<': 
					sbNewText.append("&lt;"); 
					break;
				case '>': 
					sbNewText.append("&gt;"); 
					break;
				case '\'': 
					sbNewText.append("&apos;"); 
					break;
				case '\"': 
					sbNewText.append("&quot;"); 
					break;
				case 'ß':
					//see http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references
					sbNewText.append("&#223;");
					break;
				case 'Ä':
					sbNewText.append("&#196;");
					break;
				case 'Ö':
					sbNewText.append("&#214;");
					break;
				case 'Ü':
					sbNewText.append("&#220;");
					break;
				case 'ä':
					sbNewText.append("&#228;");
					break;
				case 'ö':
					sbNewText.append("&#246;");
					break;
				case 'ü':
					sbNewText.append("&#252;");
					break;					
				default: 
					sbNewText.append(chText);				
			}
		}
		
		return sbNewText.toString();
	}
	
	/**
	 * Gets a mostly proper tag name. 
	 * If the string starts with a digit or colon, an underscore is attached. 
	 * The allowed start characters are: underscore, letters.
	 * The allowed characters are: underscore, colon, dot, hyphen, letters, digits.
	 * 
	 * @param pName the preferred name
	 * @return the name to use
	 */
	public static String getValidTagName(String pName)
	{
	    if (pName == null || pName.length() == 0)
	    {
	        return pName;
	    }

	    StringBuilder result = new StringBuilder(pName.length() + 1);
	    for (int i = 0, length = pName.length(); i < length; i++)
	    {
	        char ch = pName.charAt(i);
	        if (i == 0)
	        {
	            if (ch == ':' || Character.isDigit(ch))
	            {
	                result.append('_');
	                result.append(ch);
	            }
	            else if (ch == '_' || Character.isLetter(ch))
	            {
	                result.append(ch);
	            }
	        }
	        else if (ch == ':' || ch == '_' || ch == '-' || ch == '.' || Character.isLetter(ch) || Character.isDigit(ch))
	        {
	            result.append(ch);
	        }
	    }
	    
	    return result.toString();
	}
	
	/**
	 * Sets the line number of the tag occurence.
	 * 
	 * @param pLine the line number
	 */
	void setLineNumber(int pLine)
	{
		iLineNumber = pLine;
	}
	
	/**
	 * Gets the line number of the tag occurence.
	 * 
	 * @return the line number
	 */
	public int getLineNumber()
	{
		return iLineNumber;
	}

	/**
	 * Sets the column number behind the start-tag.
	 * 
	 * @param pColumn the column number
	 */
	void setColumnNumber(int pColumn)
	{
		iColumnNumber = pColumn;
	}
	
	/**
	 * Gets the column number behind the start-tag tag.
	 * 
	 * @return the column number
	 */
	public int getColumnNumber()
	{
		return iColumnNumber;
	}

	/**
	 * Replaces one node with another node. The <code>pOld</code> node will be removed and the
	 * <code>pNew</code> node will be inserted at the correct position.
	 * 
	 * @param pOld the node to replace
	 * @param pNew the replacement
	 */
	static void replace(XmlNode pOld, XmlNode pNew)
	{
		if (pOld != pNew)
		{
			int iPos;
			
			if (pOld.xmnParent != null)
			{
				iPos = pOld.xmnParent.auSubNodes.indexOf(pOld);
				
				pOld.xmnParent.auSubNodes.set(iPos, pNew);
				
				pNew.xmnParent = pOld.xmnParent;
				pOld.xmnParent = null;
			}
		}
	}

	/**
	 * Returns the index of the first occurrence of the specified node with the given value, or <code>-1</code> 
	 * if this node does not contain the specified node with the given value. 
	 * 
	 * @param pNodePath the node path
	 * @param pValue the search value
	 * @return the position or <code>-1</code> if the node was not found
	 * @throws IndexOutOfBoundsException if the start position is invalid
	 */
	public int indexOf(String pNodePath, String pValue)
	{
		return indexOf(pNodePath, pValue, 0);
	}
	
	/**
	 * Returns the index of the first occurrence of the specified node with the given value, or <code>-1</code> 
	 * if this node does not contain the specified node with the given value. 
	 * 
	 * @param pNodePath the node path
	 * @param pValue the search value
	 * @param pStartPos the start position of the search
	 * @return the position or <code>-1</code> if the node was not found
	 * @throws IndexOutOfBoundsException if the start position is invalid
	 */
	public int indexOf(String pNodePath, String pValue, int pStartPos)
	{
		if (pStartPos < 0)
		{
			throw new IndexOutOfBoundsException("Invalid start position: " + pStartPos);
		}

		//Ignore delimiter on first position! 
		if (pNodePath.charAt(0) == '/')
		{
			pNodePath = pNodePath.substring(1);
		}
		
		return searchIndex(pNodePath, pValue, pStartPos);
	}
	
	/**
	 * Searches the index of a node for a given value in this node.
	 * 
	 * @param pNodePath the node to search
	 * @param pValue the expected value
	 * @param pStartPos start index
	 * @return the index of the found node or <code>-1</code> if the node was not found
	 */
	private int searchIndex(String pNodePath, String pValue, int pStartPos)
	{
		int iPos = pNodePath.indexOf('/');
		
		String sCurrent;
		String sNext;

		if (iPos > 0)
		{
			sCurrent = pNodePath.substring(0, iPos);
			sNext = pNodePath.substring(iPos + 1);
		}
		else
		{
			sCurrent = pNodePath;
			sNext = null;
		}
		
		List<XmlNode> liNodes = (List<XmlNode>)getNode(sCurrent, MODE_GET_LIST);

		if (liNodes != null)
		{
			int iFound;

			for (int i = pStartPos, anz = liNodes.size(); i < anz; i++)
			{
				if (sNext != null)
				{
					//start-position is for sub nodes alwas 0 (position is only important for first node)
					iFound = liNodes.get(i).searchIndex(sNext, pValue, 0);
					
					if (iFound >= 0)
					{
						return i;
					}
				}
				else
				{
					if (CommonUtil.equals(pValue, liNodes.get(i).getValue()))
					{
						return i;
					}
				}
			}			
		}

		return -1;
	}

	/**
	 * Returns the string representation of this node.
	 * 
	 * @param pInsertNewLines <code>true</code> to insert new lines
	 * @return the string representation of this node
	 */
	public String toString(boolean pInsertNewLines)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		
		try
		{
			createXml(bos, 2, pInsertNewLines);
			
			return new String(bos.toByteArray(), "UTF-8");
		}
		catch (IOException ioe)
		{
			return "Document creation failed: " + ioe.getMessage();
		}
	}
	
    /**
     * Writes this Object to the given stream.
     * 
     * @param pObjectOutputStream the ObjectOutputStream.
     * @throws IOException if an IOException occurs.
     */
    private void writeObject(ObjectOutputStream pObjectOutputStream) throws IOException
    {
        pObjectOutputStream.defaultWriteObject();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        GZIPOutputStream zos = new GZIPOutputStream(baos);
        
        try
        {
            if (shType != TYPE_DECLARATION || shType == TYPE_DOCTYPE)
            {
                zos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n".getBytes("ISO-8859-1"));
            }
        
            zos.write(toString(false).getBytes("UTF-8"));
            
            if (shType == TYPE_COMMENT || shType == TYPE_DOCTYPE)
            {
                //Attention! If we write only a comment, parsing will fail because it's not a valid xml
                zos.write("\n<empty/>".getBytes("ISO-8859-1"));
            }
            
            zos.flush();
        }
        finally
        {
            try
            {
                zos.close();
            }
            catch (Exception e)
            {
                //nothing to be done
            }
        }
        
        byte[] byData = baos.toByteArray();
        
        if (byData == null)
        {
            pObjectOutputStream.writeInt(-1);
        }
        else
        {
            pObjectOutputStream.writeInt(byData.length);
            
            if (shType == TYPE_DECLARATION)
            {
                //mark: declaration is temporary 
                pObjectOutputStream.writeByte(0);
            }
            else if (shType == TYPE_COMMENT)
            {
                //mark: first tag is temporary
                pObjectOutputStream.writeByte(2);
            }
            else if (shType == TYPE_DOCTYPE)
            {
                //mark: first tag is temporary
                pObjectOutputStream.writeByte(3);
            }
            else
            {
                //mark: full xml with declaration was written
                pObjectOutputStream.writeByte(1);
            }
            
            pObjectOutputStream.write(byData);
        }
    }
    
    /**
     * Reads this Object from the given stream.
     * 
     * @param pStream the input stream.
     * @throws IOException if an IOException occurs.
     * @throws ClassNotFoundException if a class does not exist.
     */
    private void readObject(ObjectInputStream pStream) throws IOException, ClassNotFoundException
    {
        pStream.defaultReadObject();
        
        int len = pStream.readInt();
        if (len > 0)
        {
            byte byType = pStream.readByte();
            
            byte[] byData = new byte[len];
            
            pStream.readFully(byData);
            
            XmlWorker xmw = new XmlWorker();
            
            GZIPInputStream zis = null;
            
            try
            {
                zis = new GZIPInputStream(new ByteArrayInputStream(byData));
                
                XmlNode xmn = xmw.read(zis);

                //if we didn't receive a full XML, use the first tag after declaration
                //no matter if first tag is a comment or the start tag!
                if (byType != 0)
                {
                    List<XmlNode> liNodes = xmn.getNodes();
                    short shFoundType;
                    
                    for (int i = 0, cnt = liNodes.size(); i < cnt; i++)
                    {
                        shFoundType = liNodes.get(i).getType();
                        
                        //ignore declaration
                        if (shFoundType != TYPE_DECLARATION && shFoundType != TYPE_ATTRIBUTE)
                        {
                            xmn = liNodes.get(i);
                            i = cnt;
                        }
                    }
                }
                
                xmnParent = xmn.xmnParent;
                sName = xmn.sName;
                sValue = xmn.sValue;
                auSubNodes = xmn.auSubNodes;
                iLineNumber = xmn.iLineNumber;
                iColumnNumber = xmn.iColumnNumber;
                shType = xmn.shType;
            }
            catch (Exception e)
            {
                IOException ioe = new IOException(e.getMessage());
                ioe.setStackTrace(e.getStackTrace());

                throw ioe;
            }
            finally
            {
                try
                {
                   zis.close(); 
                }
                catch (Exception e)
                {
                    //nothing to be done
                }
            }
        }
    }
	
}	// XmlNode
