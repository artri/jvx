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
 * 24.08.2011 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.io.InputStream;
import java.net.URL;

/**
 * The <code>IResourceArchive</code> defines the access to resources which are available in archives.
 * 
 * @author René Jahn
 */
public interface IResourceArchive
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the stream for the given resource.
	 * 
	 * @param pResourceName the name of the resource, e.g. /package/&lt;classname&gt;.class
	 * @return the stream for the resource or <code>null</code> if the resource was not found
	 */
	public InputStream getInputStream(String pResourceName);
	
	/**
	 * Gets the URL for the given resource.
	 * 
	 * @param pResourceName the name of the resource, e.g. /package/&lt;classname&gt;.class
	 * @return the URL for the resource or <code>null</code> if the resource was not found
	 */
	public URL getURL(String pResourceName);
	
}	// IResourceArchive
