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
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.AttributesExtensionState;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.ui.Component;

/**
 * The {@link AttributesExtension} allows to set any attribute on the
 * extended {@link Component}.
 * 
 * @author Robert Zenz
 */
public class AttributesExtension extends AbstractComponentExtension
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AttributesExtensionState getState()
	{
		return (AttributesExtensionState)super.getState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AttributesExtensionState getState(boolean markAsDirty)
	{
		return (AttributesExtensionState)super.getState(markAsDirty);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Removes the attribute with the given name (if any).
	 * 
	 * @param pName the name of the attribute, can not be {@code null} or
	 *            {@link StringUtil#isEmpty(String) empty}.
	 */
	public void removeAttribute(String pName)
	{
		if (StringUtil.isEmpty(pName))
		{
			throw new IllegalArgumentException("Attribute name can not be null or empty.");
		}
		
		if (getState(false).attributes.containsKey(pName))
		{
			getState().attributes.remove(pName);
		}
	}
	
	/**
	 * Sets the attribute with the given value. If the given value is
	 * {@code null} or {@link StringUtil#isEmpty(String)} the attribute will be
	 * removed.
	 * 
	 * @param pName the name of the attributes, can not be {@code null} or
	 *            {@link StringUtil#isEmpty(String) empty}.
	 * @param pValue the value of the attribute. {@code null} or
	 *            {@link StringUtil#isEmpty(String) empty} to remove it.
	 */
	public void setAttribute(String pName, String pValue)
	{
		if (StringUtil.isEmpty(pName))
		{
			throw new IllegalArgumentException("Attribute name can not be null or empty.");
		}
		
		if (!StringUtil.isEmpty(pValue))
		{
			if (!pValue.equals(getState(false).attributes.get(pName)))
			{
				getState().attributes.put(pName, pValue);
			}
		}
		else
		{
			removeAttribute(pName);
		}
	}
	
	/**
	 * If the attributes are propagated to all children (and their children).
	 * 
	 * @return {@code true} if the attributes are propagated to all children.
	 */
	public boolean isPropagateToAllChildren()
	{
		return getState(false).propagateToAllChildren;
	}

	/**
	 * Sets if the attributes should be propagated to all children (and their
	 * children).
	 * 
	 * @param pPropagate {@code true} if the attributes should be propagated to
	 *            all children (and their children).
	 */
	public void setPropagateToAllChildren(boolean pPropagate)
	{
		if (getState(false).propagateToAllChildren != pPropagate)
		{
			getState().propagateToAllChildren = pPropagate;
		}
	}
	
	/**
	 * If the attributes are propagated to all input children.
	 * 
	 * @return {@code true} if the attributes are propagated to all input children.
	 */
	public boolean isPropagateToInput()
	{
		return getState(false).propagateToInput;
	}

	/**
	 * Sets if the attributes should be propagated to the all input children like input and button.
	 * 
	 * @param pPropagate {@code true} if the attributes should be propagated to
	 *                   all input children.
	 */
	public void setPropagateToInput(boolean pPropagate)
	{
		if (getState(false).propagateToInput != pPropagate)
		{
			getState().propagateToInput = pPropagate;
		}
	}
	
}	// AttributesExtension
