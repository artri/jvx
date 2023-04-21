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
 * 20.01.2010 - [HM] - creation
 * 25.08.2016 - [JR] - #1674: putAll introduced 
 */
package javax.rad.type.bean;

/**
 * The definition of a <code>Bean</code> interface.
 * It has to give access to the property methods with generic get and set methods.
 * 
 * @author Martin Handsteiner
 * @see java.util.Map
 */
public interface IBean extends Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property names available by this bean.
	 * @return the property names.
	 */
	public IBeanType getBeanType();
	
	/**
	 * Gets the value of the property name.
	 * 
	 * @param pPropertyName the property name.
	 * @return the value of the property name.
	 */
	public Object get(String pPropertyName);
	
	/**
	 * Sets the value of the property name.
	 * 
	 * @param pPropertyName the property name.
	 * @param pValue the value of the property name.
	 * @return the replaced value.
	 */
	public Object put(String pPropertyName, Object pValue);
	
	/**
	 * Puts all properties from the given object/bean into the current bean. Existing
	 * properties will be overwritten.
	 * 
	 * @param pObject the object to copy
	 */
	public void putAll(Object pObject);
	
	/**
	 * Returns a clone of this bean.
	 * 
	 * @return the cloned bean.
	 */
	public abstract IBean clone();
	
}	// IBean
