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
 * 24.09.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext;

import com.vaadin.server.Resource;

/**
 * The <code>INamedResource</code> defines a resource with an additional name and
 * css name.
 *  
 * @author René Jahn
 */
public interface INamedResource extends Resource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the resource name.
	 * 
	 * @return the resource name
	 */
	public String getResourceName();

    /**
     * Sets the style name.
     * 
     * @param pStyleName the style name
     */
    public void setStyleName(String pStyleName);
	
	/**
	 * Returns the style name.
	 * 
	 * @return the style name.
	 */
	public String getStyleName();
	
}	// INamedResource
