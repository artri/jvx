/*
 * Copyright 2019 SIB Visions GmbH
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
 * 04.07.2019 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service.config;

import java.util.HashMap;

import com.sibvisions.rad.server.http.rest.service.StorageService;

/**
 * The <code>StorageServiceConfig</code> defines options for {@link StorageService}. It can be used to configure
 * a storage instance.
 * 
 * @author René Jahn
 */
public class StorageServiceConfig
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
	/** The service options. */
	public enum Option
	{
		/** MetaData. */
		MetaData,
		/** Fetching. */
		Fetch,
		/** Inserting. */
		Insert,
		/** Updating. */
		Update,
		/** Deleting. */
		Delete
	}
	
	/** the options. */
	private HashMap<Option, Boolean> hmpOptions = new HashMap<Option, Boolean>();
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets an option enabled.
	 * 
	 * @param pOption the option
	 * @param pEnable <code>true</code> to enable, <code>false</code> to disable
	 */
	public void setOptionEnabled(Option pOption, boolean pEnable)
	{
		if (pEnable)
		{
			hmpOptions.remove(pOption);
		}
		else
		{
			hmpOptions.put(pOption, Boolean.FALSE);
		}
	}
	
	/**
	 * Gets whether an option is enabled.
	 * 
	 * @param pOption the option
	 * @return <code>true</code> if option is enabled, <code>false</code> if disabled
	 */
	public boolean isOptionEnabled(Option pOption)
	{
		Boolean value = hmpOptions.get(pOption);
		
		if (value != null && !value.booleanValue())
		{
			return false;
		}
		
		return true;
	}
	
}	// StorageServiceConfig
