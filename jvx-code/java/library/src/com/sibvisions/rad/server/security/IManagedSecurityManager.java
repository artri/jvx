/*
 * Copyright 2022 SIB Visions GmbH
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
 * 29.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.IConfiguration;

/**
 * The <code>IManagedSecurityManager</code> extends the {@link ISecurityManager}
 * and adds custom class loading support and access to the configuration. It should 
 * be used for class loaders which should be able to delegate to other security manager
 * instances.
 *  
 * @author René Jahn
 */
public interface IManagedSecurityManager extends ISecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the custom class loader.
	 * 
	 * @param pLoader the class loader
	 */
	public void setClassLoader(ClassLoader pLoader);
	
	/**
	 * Gets the custom class loader.
	 * 
	 * @return the class loader
	 */
	public ClassLoader getClassLoader();
	
	/**
	 * Sets the configuration.
	 * 
	 * @param pConfig the configuration
	 */
	public void setConfiguration(IConfiguration pConfig);
	
	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 */
	public IConfiguration getConfiguration();
	
}	// IManagedSecurityManager
