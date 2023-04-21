/*
 * Copyright 2012 SIB Visions GmbH
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
 * 15.07.2012 - [JR] - creation
 */
package com.sibvisions.util.security;

import java.security.Provider;
import java.security.Security;

/**
 * The <code>SecurityProvider</code> is a {@link Provider} that mapps all additional
 * message digests and offers them through {@link Security}. 
 * 
 * @author René Jahn
 */
public class SecurityProvider extends Provider
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the provider name. */
	public static final String NAME = "JVx";
	
	/** the version. */
	public static final float VERSION = 1.0f;
	
	/** the provider information. */
	public static final String INFO = NAME + " Security Provider v" + VERSION;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SecurityProvider</code>.
	 */
	public SecurityProvider()
	{
		this(NAME, VERSION, INFO);
	}
	
	/**
	 * Crates a new instance of <code>SecurityProvider</code> with the given
	 * information.
	 * 
	 * @param name the provider name
	 * @param version the version
	 * @param info detailed information about the provider
	 */
	protected SecurityProvider(String name, double version, String info)
	{
		super(name, version, info);
		
		initDigests();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes all additional digests.
	 */
	private void initDigests()
	{
		put("MessageDigest.MD4", MD4MessageDigest.class.getName());
		put("Alg.Alias.MessageDigest.1.2.840.113549.2.4", "MD4");
	}
	
	/**
	 * Adds the security provider to the list of known providers, if not already done.
	 */
	public static void init()
	{
    	Provider prov = Security.getProvider(NAME);
    	
    	if (prov == null)
    	{
    		Security.addProvider(new SecurityProvider());
    	}
	}
    
}	// SecurityProvider
