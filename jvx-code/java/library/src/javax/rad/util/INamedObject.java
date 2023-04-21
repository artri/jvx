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
 * 08.05.2013 - [HM] - creation
 */
package javax.rad.util;

/**
 * The <code>INamedObject</code> is the base for all classes or interfaces 
 * with the property "name".
 * 
 * @author Martin Handsteiner
 */
public interface INamedObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of the object.
	 * 
	 *  @return the name of the object.
	 */
	public String getName();

	/**
	 * Sets the name of the object.
	 * 
	 *  @param pName the name of the object.
	 *  @throws Exception if setting the name fails
	 */
	public void setName(String pName) throws Exception;

}	// INamedObject
