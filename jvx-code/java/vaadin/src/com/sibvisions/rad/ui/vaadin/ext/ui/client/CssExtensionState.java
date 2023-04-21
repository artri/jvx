/*
 * Copyright 2013 SIB Visions GmbH
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
 * 08.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.vaadin.shared.communication.SharedState;

/**
 * The <code>CssExtensionState</code> class holds all {@link CssExtensionAttribute}s.
 * 
 * @author Stefan Wurm
 */
public class CssExtensionState extends SharedState
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/** All CssExtensionAttributes. **/
	private Set<CssExtensionAttribute> attributes = new HashSet<CssExtensionAttribute>();
	
	/** All CssExtensionAttributes removed. **/
	private Set<CssExtensionAttribute> attributesRemoved = new HashSet<CssExtensionAttribute>();	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Adds a {@link CssExtensionAttribute} to the list.
	 * 
	 * @param pAttribute the cssExtensionAttribute.
	 * @param force <code>true</code> if it is required to add a style explicit
	 * @return true if the extension should be marked as dirty.
	 */
	public boolean addAttribute(CssExtensionAttribute pAttribute, boolean force)
	{
		if (isAttributeAlreadyAdded(pAttribute))
		{
			CssExtensionAttribute attribute = getCssExtensionAttributeFromCollection(pAttribute);
			
			if (attribute.getValue().equals(pAttribute.getValue()) && !force)
			{
				return false;
			}
			else
			{
				attributes.remove(pAttribute);
			}
		}

		attributesRemoved.remove(pAttribute);
		attributes.add(pAttribute);		
		
		return true;
	}
	
	/**
	 * Removes the given attribute from the list.
	 * 
	 * @param pAttribute the attribute
	 * @param pForce <code>true</code> if it is required to remove a style which is not explicit added.
	 * @return true if the extension should be marked as dirty.
	 */
	public boolean removeAttribute(CssExtensionAttribute pAttribute, boolean pForce)
	{
		if (!isAttributeAlreadyAdded(pAttribute) && !pForce)
		{
			return false;
		}
		
		if (isAttributeAlreadyRemoved(pAttribute))
		{
			return false;
		}
		
		attributes.remove(pAttribute);
		attributesRemoved.add(pAttribute);		
		
		return true;
	}
	
	/**
	 * Returns the list of the {@link CssExtensionAttribute}s.
	 * 
	 * @return the CssExtensionAttributes in a List
	 */
	public Set<CssExtensionAttribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * Sets the list of the {@link CssExtensionAttribute}s.
	 * 
	 * @param pAttributes the CssExtensionAttributes in a List
	 */
	public void setAttributes(Set<CssExtensionAttribute> pAttributes)
	{
		attributes = pAttributes;
	}

	/**
	 * Returns the removed Attributes as {@link Set}.
	 * 
	 * @return the set
	 */
	public Set<CssExtensionAttribute> getAttributesRemoved()
	{
		return attributesRemoved;
	}

	/**
	 * Sets the removed Attributes.
	 * 
	 * @param pAttributesRemoved HashSet
	 */
	public void setAttributesRemoved(Set<CssExtensionAttribute> pAttributesRemoved)
	{
		attributesRemoved = pAttributesRemoved;
	}

	/**
	 * Returns true if the collection attributesRemoved contains the pAttribute.
	 * 
	 * @param pAttribute the attribute.
	 * @return true if the collection attributesRemoved contains the pAttribute.
	 */
	private boolean isAttributeAlreadyRemoved(CssExtensionAttribute pAttribute)
	{
		if (attributesRemoved.contains(pAttribute))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * Returns true if the collection attributes contains the pAttribute.
	 * 
	 * @param pAttribute the attribute.
	 * @return true if the collection attributes contains the pAttribute.
	 */
	private boolean isAttributeAlreadyAdded(CssExtensionAttribute pAttribute)
	{
		if (attributes.contains(pAttribute))
		{
			return true;
		}
		
		return false;
	}	
	
	/**
	 * Returns the CssExtensionAttribute from the collection attributes which has the same
	 * searchDirection, elementClassName and attribute.
	 * 
	 * @param pAttribute the attribute
	 * @return the CssExtensionAttribute
	 */
	private CssExtensionAttribute getCssExtensionAttributeFromCollection(CssExtensionAttribute pAttribute)
	{
		Iterator<CssExtensionAttribute> iterator = attributes.iterator();
		
		while (iterator.hasNext())
		{
			CssExtensionAttribute attribute = iterator.next();
			
			if (attribute.equals(pAttribute))
			{
				return attribute;
			}
		}
		
		return null;
	}
	
} 	// CssExtensionState
