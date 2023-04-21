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
 * 20.08.2009 - [HM] - creation
 */
package javax.rad.type.bean;


/**
 * The <code>IBeanType</code> is the class descriptor for {@link IBean}s.
 * 
 * @param <T> the type.
 * 
 * @author Martin Handsteiner
 */
public interface IBeanType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the class name or identifier of the bean class.
	 * This name has to be unique for every different bean class.
	 * 
	 * @return the class name.
	 */
//  Try to simplify the IBeanType Interface, so this method is not necessary.	
//	
//  !!! If we remove this comment, then we have to change the javadoc in AbstractBeanType and
//      change the method location !!!
//	public String getClassName();
	
	/**
	 * Gets the property names.
	 * 
	 * @return the property names.
	 */
	public String[] getPropertyNames();
	
	/**
	 * Gets the amount of properties.
	 * 
	 * @return the amount of properties.
	 */
	public int getPropertyCount();
	
	/**
	 * Gets the index of the property name.
	 * 
	 * @param pPropertyName the property name.
	 * @return the index.
	 */
	public int getPropertyIndex(String pPropertyName);
	
	/**
	 * Gets the bean property for the given index.
	 * 
	 * @param pIndex the index.
	 * @return the bean property.
	 */
	public PropertyDefinition getPropertyDefinition(int pIndex);
	
	/**
	 * Gets the bean property for the given property name.
	 * 
	 * @param pPropertyName the property name.
	 * @return the bean property.
	 */
	public PropertyDefinition getPropertyDefinition(String pPropertyName);
	
}	// IBeanType
