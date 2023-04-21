/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.04.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl;

/**
 * The <code>IPropertyHandler</code> class defines handling of component properties.
 * 
 * @author René Jahn
 * @see IPropertyHandler
 */
public interface IPropertyHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the value for the given property name.
	 * 
	 * @param <T> the value type
	 * @param pPropertyName the property name.
	 * @param pDefaultValue the default value, if the property is not set.
	 * @return the value.
	 */
	public <T> T getProperty(String pPropertyName, T pDefaultValue);
	
	/**
	 * Sets a property values and tracks the changes.
	 * 
	 * @param pPropertyName the property name.
	 * @param pValue the value.
	 */
	public void setProperty(String pPropertyName, Object pValue);

	/**
	 * Sets a property values and tracks the changes, but allow to override the old value.
	 * 
	 * @param pPropertyName the property name
	 * @param pOverride <code>true</code> to set the property and track changed independent of the old value
	 *                  of the property
	 * @param pValue the value
	 */
	public void setProperty(String pPropertyName, Object pValue, boolean pOverride);	
	
	/**
	 * Checks if a the value of a property is currently marked as changed.
	 * 
	 * @param pPropertyName the property name
	 * @return <code>true</code> if the property value has changed since last access
	 */
	public boolean isChanged(String pPropertyName);
	
}	// IPropertyHandler
