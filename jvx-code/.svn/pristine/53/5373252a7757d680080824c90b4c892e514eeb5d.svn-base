/*
 * Copyright 2011 SIB Visions GmbH
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
 * 19.08.2011 - [JR] - creation
 */
package javax.rad.model;

/**
 * The {@link MetaDataCacheOption} indicates the behavior of the metadata cache.
 * 
 * @author René Jahn
 */
public enum MetaDataCacheOption
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Use default/global settings. */
	Default,
	
	/** Always cache the metadata. */
	On,
	
	/** Do not cache the metadata. */
	Off;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Resolves the cache option specified by a string. The detection is case
	 * insensitive. It's possible to resolve <code>DEFAULT</code> as
	 * {@link MetaDataCacheOption#Default}.
	 * 
	 * @param pName the option name.
	 * @return the cache option.
	 * @throws IllegalArgumentException if <code>pName</code> is
	 *             <code>null</code> or the name is unknown.
	 */
	public static MetaDataCacheOption resolve(String pName)
	{
		if (pName != null)
		{
			String sName = pName.toUpperCase();
			
			if ("DEFAULT".equals(sName))
			{
				return Default;
			}
			else if ("ON".equals(sName))
			{
				return On;
			}
			else if ("OFF".equals(sName))
			{
				return Off;
			}
		}
		
		throw new IllegalArgumentException("Unknown enum: " + pName);
	}
	
}	// MetaDataCacheOption
