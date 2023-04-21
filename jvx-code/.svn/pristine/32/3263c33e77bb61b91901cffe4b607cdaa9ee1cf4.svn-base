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
 * 09.05.2009 - [JR] - creation
 * 20.06.2010 - [JR] - refactoring: removed getDirectory
 * 01.12.2010 - [JR] - getNode defined
 */
package javax.rad.server;

import java.util.List;

import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>IConfiguration</code> defines the access to the application or server
 * configuration. The configuration contains properties/settings.
 * 
 * @author René Jahn
 */
public interface IConfiguration
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the value of a property from the configuration.
	 *
	 * @param pName the property name (e.g /application/securitymanager/class)
	 * @return the value for the property or <code>null</code> if the property is not available
	 */
	public String getProperty(String pName);
	
	/**
	 * Gets the value of a property from the configuration.
	 *
	 * @param pName the property name (e.g /application/securitymanager/class)
	 * @param pDefault the default value if the property is not available
	 * @return the value for the property or <code>pDefault</code> if the property is not available
	 */
	public String getProperty(String pName, String pDefault);
	
	/**
	 * Gets a list of values for a property which exists more than once.
	 * 
	 * @param pName the property name
	 * @return the list of values or <code>null</code> if the property is not available
	 */
	public List<String> getProperties(String pName);
	
	/**
	 * Gets the value for a property in xml representation.
	 * 
	 * @param pName the property name
	 * @return the available xml node or <code>null</code> if the property is not available
	 */
	public XmlNode getNode(String pName);
	
	/**
	 * Gets a list of values for a property in xml representation.
	 * 
	 * @param pName the property name
	 * @return the list of available xml nodes or <code>null</code> if the property is not available
	 */
	public List<XmlNode> getNodes(String pName);
	
}	// IConfiguration
