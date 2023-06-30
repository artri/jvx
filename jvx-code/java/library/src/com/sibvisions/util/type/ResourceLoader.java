/*
 * Copyright 2023 SIB Visions GmbH
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
 * 08.06.2023 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.util.List;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>ResourceLoader</code> is a container object for resource loading.
 * 
 * @author René Jahn
 */
public class ResourceLoader
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/** the default class loader. */
	ClassLoader clDefault = null;
	
	/** the list of resource archives. */
	List<IResourceArchive> liResourceArchive = null;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ResourceLoader</code>.
     */
    public ResourceLoader()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Sets the default class loader. The default class loader will be used if not specific class
	 * loader is defined when loading resources.
	 * 
	 * @param pClassLoader a class loader
	 */
	public void setDefaultClassLoader(ClassLoader pClassLoader)
	{
		clDefault = pClassLoader;
	}
	
	/**
	 * Gets the default class loader.
	 * 
	 * @return the class loader or <code>null</code> if not explicitly set
	 * @see #setDefaultClassLoader(ClassLoader)
	 */
	public ClassLoader getDefaultClassLoader()
	{
	    return clDefault;
	}
	
	/**
	 * Adds a resource archive to the given position in the list of known archives.
	 * 
	 * @param pIndex the list index
	 * @param pArchive the resource archive
	 */
	public void addResourceArchive(int pIndex, IResourceArchive pArchive)
	{
		if (pArchive != null)
		{
			if (liResourceArchive == null)
			{
				liResourceArchive = new ArrayUtil<IResourceArchive>();
			}
			
			if (pIndex < 0)
			{
				liResourceArchive.add(liResourceArchive.size(), pArchive);
			}
			else
			{
				liResourceArchive.add(pIndex, pArchive);
			}
		}
	}
	
	/**
	 * Removes a resource archive from the list of known archives.
	 * 
	 * @param pArchive the resource archive
	 */
	public void removeResourceArchive(IResourceArchive pArchive)
	{
		if (pArchive != null && liResourceArchive != null)
		{
			liResourceArchive.remove(pArchive);
			
			if (liResourceArchive.size() == 0)
			{
				liResourceArchive = null;
			}
		}
	}

	/**
	 * Removes all resource archives from the list of known archives.
	 */
	public void removeAllResourceArchives()
	{
		if (liResourceArchive != null)
		{
			liResourceArchive.clear();
			
			liResourceArchive = null;
		}
	}
	
	/**
	 * Gets the current list of known resource archives.
	 * 
	 * @return the list of resource archives
	 */
	public List<IResourceArchive> getResourceArchives()
	{
		return liResourceArchive;
	}	
    
}   // ResourceLoader
