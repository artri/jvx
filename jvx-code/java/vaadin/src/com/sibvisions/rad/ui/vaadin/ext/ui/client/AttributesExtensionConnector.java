/*
 * Copyright 2017 SIB Visions GmbH
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
 * 31.01.2017 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The {@link AttributesExtensionConnector} is the client side logic of the
 * {@link AttributesExtension}.
 * 
 * @author Robert Zenz
 */
@Connect(AttributesExtension.class)
public class AttributesExtensionConnector extends AbstractExtensionConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Set} of names of attached attributes. */
	private Set<String> addedAttributeNames = new HashSet<String>();
	
	/** The {@link Set} of names of removed attributes. */
	private Set<String> removedAttributeNames = new HashSet<String>();
	
	/** The {@link Element} which has the attributes set. */
	private Element element = null;
	
	/** If the attached attributes have been propagated to all children. */
	private boolean allChildren = false;
	
	/** If the attached attributes have been propagated to input children. */
	private boolean inputChildren = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributesExtensionState getState()
	{
		return (AttributesExtensionState)super.getState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStateChanged(StateChangeEvent pStateChangeEvent)
	{
		super.onStateChanged(pStateChangeEvent);
		
		update();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void extend(ServerConnector pTarget)
	{
		if (pTarget instanceof ComponentConnector)
		{
			element = ((ComponentConnector)pTarget).getWidget().getElement();
		}
		else
		{
			element = null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Updates all attributes to the current element (if any).
	 */
	private void update()
	{
		if (element == null)
		{
			return;
		}
		
		removedAttributeNames = new HashSet<String>(addedAttributeNames);
		
		AttributesExtensionState state = getState();
		
		//set attribute for the "first" found input element
		boolean bInputSet = false;
		
		boolean bStateInput = state.propagateToInput;
		boolean bIsInput = isInput(element);
		
		for (Entry<String, String> attribute : state.attributes.entrySet())
		{
			String key = attribute.getKey();

			if (!bStateInput || bIsInput)
			{
				setAttribute(element, key, attribute.getValue());
				
				bInputSet = true;
			}
				
			addedAttributeNames.add(key);
			removedAttributeNames.remove(key);
		}
		
		for (String removedAttributeName : removedAttributeNames)
		{
			element.removeAttribute(removedAttributeName);
			
			addedAttributeNames.remove(removedAttributeName);
		}
		
		if (allChildren || inputChildren)
		{
			remove(element);
		}
		
		allChildren = state.propagateToAllChildren;
		inputChildren = state.propagateToInput;
		
		if (allChildren || (inputChildren && !bInputSet))
		{
			propagate(element);
		}
	}
		
	/**
	 * Removes all attached attributes from the children (and their children) of
	 * the given {@link Node}.
	 * 
	 * @param pParent the {@link Node} at which to start.
	 */
	private void remove(Node pParent)
	{
		for (int index = 0; index < pParent.getChildCount(); index++)
		{
			Node childNode = pParent.getChild(index);
			
			if (childNode instanceof Element && childNode.getNodeType() != Node.TEXT_NODE)
			{
				Element childElement = (Element)childNode;
				
				if (isValidChild(childElement))
				{
					for (String attributeName : removedAttributeNames)
					{
						childElement.removeAttribute(attributeName);
					}
				}
			}
			
			remove(childNode);
		}
	}

	/**
	 * Propagates the attached attributes to all children (and their children)
	 * of the given {@link Node}.
	 * 
	 * @param pParent the {@link Node} at which to start.
	 */
	private void propagate(Node pParent)
	{
		AttributesExtensionState state = getState();
		
		for (int index = 0; index < pParent.getChildCount(); index++)
		{
			Node childNode = pParent.getChild(index);
			
			if (childNode instanceof Element && childNode.getNodeType() != Node.TEXT_NODE)
			{
				Element childElement = (Element)childNode;
				
				if (isValidChild(childElement))
				{
					for (String attributeName : addedAttributeNames)
					{
						setAttribute(childElement, attributeName, state.attributes.get(attributeName));
					}
				}				
			}
			
			propagate(childNode);
		}
	}
	
	/**
	 * Gets wheter the given element is a valid child element regarding the state.
	 * 
	 * @param pElement the element to check
	 * @return <code>true</code> if element is valid because of the state, <code>false</code> otherwise
	 */
	private boolean isValidChild(Element pElement)
	{
		if (allChildren)
		{
			return true;
		}
		
		if (inputChildren)
		{
			return isInput(pElement);
		}
		
		return false;
	}

	/**
	 * Gets whether the given element is an input element.
	 *  
	 * @param pElement the element to check
	 * @return <code>true</code> if element is an input or button element, <code>false</code> otherwise
	 */
	private boolean isInput(Element pElement)
	{
		return (ButtonElement.is(pElement) 
			    || InputElement.is(pElement)
			    || TextAreaElement.is(pElement)
			    || SelectElement.is(pElement));		
	}
	
	/**
	 * Sets the attribute for the given element. This method takes care of aria- tags.
	 * 
	 * @param pElement the element to change
	 * @param pName the attribute name
	 * @param pValue the attribute value
	 */
	private void setAttribute(Element pElement, String pName, String pValue)
	{
		//if we set aria- attributes, don't set if aria-hidden is set
		if (pName.startsWith("aria-"))
		{
			if (Boolean.parseBoolean(pElement.getAttribute("aria-hidden")))
			{
				return;
			}
		}
		
		pElement.setAttribute(pName, pValue);	
	}
		
}	// AttributesExtensionConnector
