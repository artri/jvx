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
 * 08.04.2009 - [JR] - creation
 * 15.07.2009 - [JR] - removed getBaseDir (use getPathForClass(...) instead)
 * 09.11.2010 - [JR] - added ClassLoader parameters
 * 26.12.2010 - [JR] - setDefaultClassLoader
 *                   - getPrivilegedResourceAsStream now checkes SecurityException
 * 12.05.2011 - [JR] - getResource, getResourceAsStream: check relative file only in "jar" case
 * 13.05.2011 - [JR] - #353: getPrivilegedResource: check empty resource
 * 24.08.2011 - [JR] - #464: resource archive support    
 * 22.08.2012 - [JR] - getResourceAsStream now uses the pLoader for getLocationForClass
 * 23.12.2012 - [JR] - getFqClassName: check String parameter
 * 28.12.2012 - [JR] - getFqClassName: String parameter check (must be a classname)  
 * 23.10.2015 - [JR] - load resources preferred as stream instead of URLs
 * 22.01.2016 - [JR] - introduced classloader for current thread (not the same as Thread' 
 *                     current context classloader)  
 * 29.03.2017 - [JR] - getThreadClassLoader implemented                          
 */
package com.sibvisions.util.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>ResourceUtil</code> contains resource and class 
 * dependent utility methods.
 * 
 * @author Ren� Jahn
 */
public final class ResourceUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default class loader if no specific class loader is used. */
	private static ClassLoader clDefault = null;
	
	/** The current thread classloader instance. */
    private static ThreadLocal<ClassLoader> thlClassLoader = null;
	
	/** the list of resource archives. */
	private static List<IResourceArchive> liResourceArchive = null;
	
	/** the current thread sync object. */
	private static Object oSyncThreadCl = new Object();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>ResourceUtil</code> is a utility
	 * class.
	 */
	private ResourceUtil()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a resource archive to the list of known archives.
	 * 
	 * @param pArchive the resource archive
	 */
	public static void addResourceArchive(IResourceArchive pArchive)
	{
		addResourceArchive(-1, pArchive);
	}
	
	/**
	 * Adds a resource archive to the given position in the list of known archives.
	 * 
	 * @param pIndex the list index
	 * @param pArchive the resource archive
	 */
	public static void addResourceArchive(int pIndex, IResourceArchive pArchive)
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
	public static void removeResourceArchive(IResourceArchive pArchive)
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
	public static void removeAllResourceArchives()
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
	public static List<IResourceArchive> getResourceArchives()
	{
		return liResourceArchive;
	}
	
	/**
	 * Finds a resource with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource stream or <code>null</code> if no resource with this name is found
	 * @see #getResourceAsStream(ClassLoader, String)
	 */
	public static InputStream getResourceAsStream(String pResource)
	{
		return getResourceAsStream(getInternalResourceClassLoader(), pResource);
	}
	
	/**
	 * Finds a resource with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource URL or <code>null</code> if no resource with this name is found
	 * @see #getResource(ClassLoader, String)
	 */
	public static URL getResource(String pResource)
	{
		return getResource(getInternalResourceClassLoader(), pResource);
	}

	/**
	 * Finds all resources with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource URLs
	 * @see #getResources(ClassLoader, String)
	 */
	public static Enumeration<URL> getResources(String pResource)
	{
		return getResources(getInternalResourceClassLoader(), pResource);
	}
	
	/**
	 * Finds a resource with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader.
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource stream or <code>null</code> if no resource with this name is found
	 */
	public static InputStream getResourceAsStream(ClassLoader pLoader, String pResource)
	{
		if (StringUtil.isEmpty(pResource))
		{
			return null;
		}
		
		try
		{
	    	//Use the whole path
		    File fiResource = new File(pResource);
		    
		    if (fiResource.exists())
		    {
		    	try
		    	{
		    		return new FileInputStream(fiResource);
		    	}
		    	catch (IOException ioe)
		    	{
		    		return null;
		    	}
		    }
		    else
		    {
		    	String sLoc = getLocationForClass(pLoader, getFqClassName(ResourceUtil.class));
		    	
		    	if (sLoc != null && sLoc.toLowerCase().endsWith(".jar"))
		    	{
			    	//Check the directory, which contains class or jar files
			    	File fiName = new File(new File(sLoc).getParentFile(), fiResource.getName());
			    	if (fiName.exists())
			    	{
				    	try
				    	{
				    		return new FileInputStream(fiName);
				    	}
				    	catch (IOException ioe)
				    	{
				    		return null;
				    	}
			    	}
		    	}
		    }
		}
		catch (SecurityException se)
		{
			//e.g. not allowed with unsigned applets!
		}
		
		if (liResourceArchive != null)
		{
			InputStream is = null;
			
			for (int i = 0, anz = liResourceArchive.size(); i < anz && is == null; i++)
			{
				is = liResourceArchive.get(i).getInputStream(pResource);
			}
			
			if (is != null)
			{
				return is;
			}
		}
	    
	    return getPrivilegedResourceAsStream(pLoader, pResource);
	}
	
	/**
	 * Finds a resource with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader.
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource URL or <code>null</code> if no resource with this name is found
	 */
	public static URL getResource(ClassLoader pLoader, String pResource)
	{
		if (StringUtil.isEmpty(pResource))
		{
			return null;
		}
		
		try
		{
	    	//Use the whole path
		    File fiResource = new File(pResource);
		    
		    if (fiResource.exists())
		    {
		    	try
		    	{
		    		return fiResource.toURI().toURL();
		    	}
		    	catch (IOException ioe)
		    	{
		    		return null;
		    	}
		    }
		    else
		    {
		    	String sLoc = getLocationForClass(getFqClassName(ResourceUtil.class));
		    	
		    	if (sLoc != null && sLoc.toLowerCase().endsWith(".jar"))
		    	{
			    	//Check the directory, which contains class or jar files
			    	File fiName = new File(new File(sLoc).getParentFile(), fiResource.getName());
			    	
			    	if (fiName.exists())
			    	{
				    	try
				    	{
				    		return fiName.toURI().toURL();
				    	}
				    	catch (IOException ioe)
				    	{
				    		return null;
				    	}
			    	}
		    	}
		    }
		}
		catch (SecurityException se)
		{
			//e.g. not allowed with unsigned applets!
		}
		
		if (liResourceArchive != null)
		{
			URL url = null;
			
			for (int i = 0, anz = liResourceArchive.size(); i < anz && url == null; i++)
			{
				url = liResourceArchive.get(i).getURL(pResource);
			}
			
			if (url != null)
			{
				return url;
			}
		}
		
		return getPrivilegedResource(pLoader, pResource);
	}
	
	/**
	 * Finds all resources with a given name. The search strategy is
	 * <ol>
	 * <li>search a file with <code>pResource</code> as absolute path</li>
	 * <li>extract the filename of <code>pResource</code>, if it's an absolute path, and search 
	 *     in the classes or lib directory where this util class is stored</li>
	 * <li>delegate the search to the {@link ClassLoader}</li>
	 * </ol>
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader.
	 * @param pResource one of the following: /package/&lt;classname&gt;.class, 
	 *                  C:\temp\config.xml, /tmp/config.xml
	 * @return the resource URL or <code>null</code> if no resource with this name is found
	 */
	public static Enumeration<URL> getResources(ClassLoader pLoader, String pResource)
	{
		if (StringUtil.isEmpty(pResource))
		{
			return null;
		}

		LinkedHashMap<URL, Object> lmpUrls = new LinkedHashMap<URL, Object>();
		
		try
		{
	    	//Use the whole path
		    File fiResource = new File(pResource);
		    
		    if (fiResource.exists())
		    {
		    	try
		    	{
		    		lmpUrls.put(fiResource.toURI().toURL(), null);
		    	}
		    	catch (IOException ioe)
		    	{
		    		//ignore
		    	}
		    }
		    else
		    {
		    	String sLoc = getLocationForClass(getFqClassName(ResourceUtil.class));
		    	
		    	if (sLoc != null && sLoc.toLowerCase().endsWith(".jar"))
		    	{
			    	//Check the directory, which contains class or jar files
			    	File fiName = new File(new File(sLoc).getParentFile(), fiResource.getName());
			    	
			    	if (fiName.exists())
			    	{
				    	try
				    	{
				    		URL url = fiName.toURI().toURL();
				    		
				    		if (!lmpUrls.containsKey(url))
				    		{
				    			lmpUrls.put(url, null);
				    		}
				    	}
				    	catch (IOException ioe)
				    	{
				    		//ignore
				    	}
			    	}
		    	}
		    }
		}
		catch (SecurityException se)
		{
			//e.g. not allowed with unsigned applets!
		}
		
		if (liResourceArchive != null)
		{
			URL url = null;
			
			for (int i = 0, anz = liResourceArchive.size(); i < anz; i++)
			{
				url = liResourceArchive.get(i).getURL(pResource);
				
				if (url != null)
				{
					if (!lmpUrls.containsKey(url))
					{
						lmpUrls.put(url, null);
					}
				}
			}
		}
		
		Enumeration<URL> urls = getPrivilegedResources(pLoader, pResource);
		
		addResources(lmpUrls, urls);

		return Collections.enumeration(lmpUrls.keySet());
	}
	
	/**
	 * Gets a resource as stream, from the given {@link ClassLoader}.
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader
	 * @param pResource the resource path e.g /com/sibvisions/util/type/ResourceUtil.class
	 * @return the resource as {@link InputStream} or <code>null</code> if the resource was not found
	 */
	private static InputStream getPrivilegedResourceAsStream(ClassLoader pLoader, String pResource)
	{
        if (StringUtil.isEmpty(pResource))
        {
            return null;
        }
	    
		final String[] sResource = formatResource(pResource);
		
        InputStream stream = null;

        //FIRST: Try to use context classloader

        ClassLoader clThread; 
                
        try
        {
            clThread = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException se)
        {
            //e.g. SecurityManager restricts access
            clThread = null;
        }
	    
	    if (clThread != null)
	    {
	        stream = clThread.getResourceAsStream(sResource[1]);
	    }

        // SECOND: Try to use current or given classloader 
	    
	    if (stream == null)
	    {
	        if (pLoader == null)
	        {
	            stream = ResourceUtil.class.getResourceAsStream(sResource[0]);
	        }
	        else
	        {
    			stream = pLoader.getResourceAsStream(sResource[1]);
	    	}
	    }
    	    
        //THIRD: run privileged
	    
	    if (stream == null)
	    {
            stream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
            {
                public Object run()
                {
                    ClassLoader loader = null;

                    try
                    {
                        loader = Thread.currentThread().getContextClassLoader();
                    }
                    catch (SecurityException se)
                    {
                        // nothing to be done
                    }

                    if (loader != null)
                    {
                        return loader.getResourceAsStream(sResource[1]);
                    }
                    else
                    {
                        return ClassLoader.getSystemResourceAsStream(sResource[1]);
                    }
                }
            });
	    }
	    
        //FOURTH: should we use a default class loader?

	    ClassLoader clInternal = getInternalResourceClassLoader();
	    
	    if (stream == null && clInternal != null)
        {
            stream = clInternal.getResourceAsStream(sResource[1]);
        }	    
	    
	    //FINALLY: try to load as resource (why? because it solved securitymanager problems)
	    
	    if (stream == null)
	    {
		    final URL url = getPrivilegedResource(pLoader, pResource);
			
			if (url != null)
			{
				try
				{
					return url.openStream();
				}
				catch (SecurityException se)
				{
					return (InputStream)AccessController.doPrivileged(new PrivilegedAction() 
					{
						public Object run() 
						{
							try
							{
								return url.openStream();
							}
							catch (IOException ioe)
							{
								return null;
							}
						}
					});
				}
				catch (IOException ioe)
				{
					return null;
				}
			}
	    }
	    
	    return stream;
	}

	/**
	 * Gets the URL to a resource, from the current {@link ClassLoader}. 
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader
	 * @param pResource the resource path e.g /com/sibvisions/util/type/ResourceUtil
	 * @return the resource as {@link URL} or <code>null</code> if the resource was not found
	 */
	private static URL getPrivilegedResource(ClassLoader pLoader, String pResource)
	{
		if (StringUtil.isEmpty(pResource))
		{
			return null;
		}
	
		final String[] sResource = formatResource(pResource);
	
	    URL url = null; 
	    
	    
        ClassLoader clThread; 
        
        try
        {
            clThread = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException se)
        {
            //e.g. SecurityManager restricts access
            clThread = null;
        }
        
        if (clThread != null)
        {
            url = clThread.getResource(sResource[1]);
        }

        if (url == null)
        {
    	    if (pLoader == null)
    	    {
    	    	url = ResourceUtil.class.getResource(sResource[0]);
    	    }
    	    else
    	    {
    	    	url = pLoader.getResource(sResource[1]);
    	    }
        }
        
	    if (url == null)
	    {
	    	url = (URL)AccessController.doPrivileged(new PrivilegedAction() 
			{
				public Object run()
				{
					ClassLoader loader = null;
					
					try
					{
						loader = Thread.currentThread().getContextClassLoader();
					}
					catch (SecurityException se)
					{
						//nothing to be done
					}
            	
                    if (loader != null) 
                    {
                        return loader.getResource(sResource[1]);
                    } 
                    else 
                    {
                        return ClassLoader.getSystemResource(sResource[1]);
                    }
                }
            });
	    }
	    
	    ClassLoader clInternal = getInternalResourceClassLoader();
	    
	    if (url == null && clInternal != null)
	    {
	        url = clInternal.getResource(sResource[1]);
	    }
	    
	    return url;
	}
	
	/**
	 * Gets all URLs to a resource, from the current {@link ClassLoader}. 
	 * 
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader
	 * @param pResource the resource path e.g /com/sibvisions/util/type/ResourceUtil
	 * @return the resources as {@link Enumeration} or <code>null</code> if the resource was not found
	 */
	private static Enumeration<URL> getPrivilegedResources(ClassLoader pLoader, String pResource)
	{
		if (StringUtil.isEmpty(pResource))
		{
			return null;
		}
	
		final String[] sResource = formatResource(pResource);
	
	    Enumeration<URL> urls = null; 
	    
	    LinkedHashMap<URL, Object> lmpResources = new LinkedHashMap<URL, Object>();
	    
        ClassLoader clThread; 
        
        try
        {
            clThread = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException se)
        {
            //e.g. SecurityManager restricts access
            clThread = null;
        }
        
        try
        {
	        if (clThread != null)
	        {
	            urls = clThread.getResources(sResource[1]);
	            
	            addResources(lmpResources, urls);
	        }
	
    	    if (pLoader == null)
    	    {
    	    	ClassLoader cl = ResourceUtil.class.getClassLoader();
    	    	
    	    	if (cl != null)
    	    	{
        	    	urls = cl.getResources(sResource[0]);
    	    	}
    	    	else
    	    	{
    	    		urls = ClassLoader.getSystemResources(sResource[0]);
    	    	}
    	    }
    	    else
    	    {
    	    	urls = pLoader.getResources(sResource[1]);
    	    }
    	    
    	    addResources(lmpResources, urls);
        }
        catch (IOException ioe)
        {
        	urls = null;
        }
        
    	urls = (Enumeration<URL>)AccessController.doPrivileged(new PrivilegedAction() 
		{
			public Object run()
			{
				ClassLoader loader = null;
				
				try
				{
					loader = Thread.currentThread().getContextClassLoader();
				}
				catch (SecurityException se)
				{
					//nothing to be done
				}
        	
				try
				{
                    if (loader != null) 
                    {
                        return loader.getResources(sResource[1]);
                    } 
                    else 
                    {
                        return ClassLoader.getSystemResources(sResource[1]);
                    }
				}
				catch (IOException ioe)
				{
					return null;
				}
            }
        });
    	
    	addResources(lmpResources, urls);
	    
	    ClassLoader clInternal = getInternalResourceClassLoader();
	    
	    if ((urls == null || !urls.hasMoreElements()) 
	    	&& clInternal != null)
	    {
	    	try
	    	{
	    		urls = clInternal.getResources(sResource[1]);
	    		
	    		addResources(lmpResources, urls);
	    	}
	    	catch (IOException e)
	    	{
	    		urls = null;
	    	}
	    }
	    
	    if (lmpResources.isEmpty())
	    {
	    	return ArrayUtil.emptyEnumeration();
	    }
	    else
	    {
	    	return Collections.enumeration(lmpResources.keySet());
	    }
	}	
	
	/**
	 * Adds all available URLs to the given map, if not already available.
	 * 
	 * @param pMap the map
	 * @param pValues the values to add
	 */
	private static void addResources(Map<URL, Object> pMap, Enumeration<URL> pValues)
	{
		if (pValues != null)
		{
			URL url;
			
			while (pValues.hasMoreElements())
			{
				url = pValues.nextElement();
				
				if (!pMap.containsKey(url))
				{
					pMap.put(url, null);
				}
			}
		}
	}

	/**
	 * Searches the directory where a class is stored. If the class is 
	 * part of a jar archive then the directory in which the jar is stored will be used.
	 *  
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return directory in which the class is stored or null if the class can not be found
	 */
	public static String getPathForClass(String pClassName)
	{
		String sPath = getLocationForClass(pClassName);
		
		
		if (sPath != null)
		{
			File fiPath = new File(sPath);
			
			if (!fiPath.isDirectory())
			{
				return fiPath.getParentFile().getAbsolutePath();
			}
			
			return fiPath.getAbsolutePath();
		}
		
		return null;
	}

	/**
	 * Searches the location where a class is stored. It can be a directory or jar file. 
	 *  
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return diretory, jar file or null if the class can not be found in the classpath
	 * @see #getLocationForClass(ClassLoader, String)
	 */
	public static String getLocationForClass(String pClassName)
	{
		return getLocationForClass(getInternalResourceClassLoader(), pClassName);
	}
	
	/**
	 * Searches the location where a class is stored. It can be a directory a local/remote or virtual file. 
	 *  
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return the location or <code>null</code> if the file wasn't found in classpath
	 * @see #getRawLocationForClass(ClassLoader, String)
	 */
	public static String getRawLocationForClass(String pClassName)
	{
		return getRawLocationForClass(getInternalResourceClassLoader(), pClassName);
	}
	
	/**
	 * Searches the location where a class is stored. It can be a directory a local/remote or virtual file. 
	 *  
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader.
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return the location or <code>null</code> if the file wasn't found in classpath
	 */
	public static String getRawLocationForClass(ClassLoader pLoader, String pClassName)
	{
		URL urlResource = getPrivilegedResource(pLoader, pClassName);
		
		if (urlResource == null)
		{
			return null;
		}
		
		String sPath = urlResource.toExternalForm();
		sPath = sPath.replace("%20", " ");
	
		//The mark, if the file is included in a jar file
		int iCallSignPos = sPath.indexOf("!");
		
		if (iCallSignPos >= 0)
		{
			//file is included in a jar file
			sPath = sPath.substring(0, iCallSignPos);
		}
		else
		{
			//Default search in "classes" folder
			sPath = sPath.substring(0, sPath.indexOf(pClassName));
		}    	
		
		return sPath;
	}
		
	/**
	 * Searches the location where a class is stored. It can be a directory or jar file. 
	 *  
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader.
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return diretory, jar file or null if the class can not be found in the classpath
	 */
	public static String getLocationForClass(ClassLoader pLoader, String pClassName)
	{
		URL urlResource = getPrivilegedResource(pLoader, pClassName);
		
		if (urlResource == null)
		{
			return null;
		}
		
		String sPath = urlResource.getFile();
		
		//file: remove (the first / will be preserved)
		if (sPath.toUpperCase().startsWith("FILE:"))
		{
			sPath = sPath.substring(5);
		}
		
		sPath = sPath.replace("%20", " ");
	
		//The mark, if the file is included in a jar file
		int iCallSignPos = sPath.indexOf("!");
		
		if (iCallSignPos >= 0)
		{
			//file is included in a jar file
			sPath = sPath.substring(0, iCallSignPos);
		}
		else
		{
			//Default search in "classes" folder
			sPath = sPath.substring(0, sPath.indexOf(pClassName));
		}    	
		
		if (sPath != null && !sPath.toLowerCase().startsWith("http"))
		{
			return new File(sPath).getAbsolutePath();
		}
		
		return sPath;
	}

	/**
	 * Searches the file in which a class is stored. It can be a class- or jar file. 
	 *  
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return class-, jar file or null if the class can not be found in the classpath
	 * @see #getFileForClass(ClassLoader, String)
	 */
	public static File getFileForClass(String pClassName)
	{
		return getFileForClass(getInternalResourceClassLoader(), pClassName);
	}
	
	/**
	 * Searches the file in which a class is stored. It can be a class- or jar file. 
	 *  
	 * @param pLoader a specific class loader or <code>null</code> to use the system 
	 *                class loader
	 * @param pClassName z.B.: /package/&lt;class&gt;.class
	 * @return class-, jar file or null if the class can not be found in the classpath
	 */
	public static File getFileForClass(ClassLoader pLoader, String pClassName)
	{
		URL urlResource = getPrivilegedResource(pLoader, pClassName);
		
		if (urlResource == null)
		{
			return null;
		}
		
		String sPath = urlResource.getFile();
		
		//file: remove (the first / will be preserved)
		if (sPath.toUpperCase().startsWith("FILE:"))
		{
			sPath = sPath.substring(5);
		}
		
		sPath = sPath.replace("%20", " ");
	
		//The mark, if the file is included in a jar file
		int iCallSignPos = sPath.indexOf("!");
		
		if (iCallSignPos >= 0)
		{
			//file is included in a jar file
			sPath = sPath.substring(0, sPath.indexOf("!"));
		}
		
		if (sPath != null && !sPath.toLowerCase().startsWith("http"))
		{
			return new File(sPath);
		}
		
		return null;
	}

	/**
	 * Returns the full qualified class name for an object.
	 * 
	 * @param pScope a class or object instance
	 * @return the full qualified class name e.g. /package/&lt;class&gt;.class
	 */
	public static String getFqClassName(Object pScope)
	{
		String sFqClassName;
		
		
		if (pScope instanceof Class<?>)
		{
			sFqClassName = getName((Class<?>)pScope);			
		}
		else if (pScope != null)
		{
			if (pScope instanceof String)
			{
				String sValue = (String)pScope;
				
				//must be a classname, otherwise we use the class
				if (sValue.indexOf(".") >= 0 || sValue.indexOf("/") >= 0)
				{
					sFqClassName = (String)pScope;
				}
				else
				{
					sFqClassName = getName(pScope.getClass());
				}
			}
			else
			{
				sFqClassName = getName(pScope.getClass());
			}
		}
		else
		{
			return null;
		}
		
		sFqClassName = sFqClassName.replace('.', '/');
		sFqClassName = "/" + sFqClassName + ".class";
	
		return sFqClassName;
	}

	/**
	 * Gets the name of a class. It checks if the class is an anonymous
	 * or inner class.
	 * 
	 * @param pClass class from which you want the name
	 * @return full qualified class name e.g. com.sibvisions.rad.util.type.ResourceUtil
	 */
	public static String getName(Class<?> pClass)
	{
		if (pClass != null)
		{
			String sName = pClass.getName();
			
			int iPos = sName.indexOf("$");
			
			//It's possible to use the superclass, but it's better to return the
			//class in which the anonymous or inner class was declared!
			if (iPos > 0)
			{
				return sName.substring(0, iPos);
			}
			else
			{
				return sName;
			}
		}
		
		return null;
	}

	/**
	 * Gets the string value of a system property, if the property is accessible. Some
	 * properties are not accessible in applets. In that case the default value
	 * will be returned.
	 * 
	 * @param pName the name of the system property
	 * @param pDefaultValue the default value if the property is <code>null</code> 
	 *                      or is not accessible
	 * @return the value of the system property, if accessible and not is <code>null</code>, 
	 *         otherwise the <code>pDefaultValue</code>
	 */
	public static String getAccessibleProperty(String pName, String pDefaultValue)
	{
		try
		{
			String sValue = System.getProperty(pName);
			
			if (sValue == null)
			{
				return pDefaultValue;
			}
			else
			{
				return sValue;
			}
		}
		catch (SecurityException se)
		{
			//in applets only some properties are accessible
		}
		
		return pDefaultValue;
	}

	/**
	 * Gets the boolean value of a system property, if the property is accessible. Some
	 * properties are not accessible in applets. In that case the default value
	 * will be returned.
	 * 
	 * @param pName the name of the system property
	 * @param pDefaultValue the default value if the property is <code>null</code> 
	 *                      or is not accessible
	 * @return the value of the system property, if accessible and not is <code>null</code>, 
	 *         otherwise the <code>pDefaultValue</code>
	 */
	public static boolean getAccessibleBoolean(String pName, boolean pDefaultValue)
	{
		try
		{
			String sValue = System.getProperty(pName);
			
			if (sValue == null)
			{
				return pDefaultValue;
			}
			else
			{
				return Boolean.valueOf(sValue).booleanValue();
			}
		}
		catch (SecurityException se)
		{
			//in applets only some properties are accessible
		}
		
		return pDefaultValue;
	}

	/**
	 * Gets the package name from a class independent of {@link Class#getPackage()}.
	 * 
	 * @param pClass the loaded class
	 * @return the package name for the class
	 */
	public static String getPackage(Class<?> pClass)
	{
		if (pClass.getPackage() != null)
		{
			return pClass.getPackage().getName();
		}
		else
		{
			String sName = pClass.getName();
	
			return sName.substring(0, sName.length() - pClass.getSimpleName().length() - 1);
		}
	}

	/**
	 * Sets the default class loader. The default class loader will be used if not specific class
	 * loader is defined when loading resources.
	 * 
	 * @param pClassLoader a class loader
	 */
	public static void setDefaultClassLoader(ClassLoader pClassLoader)
	{
		clDefault = pClassLoader;
	}
	
	/**
	 * Gets the default class loader.
	 * 
	 * @return the class loader or <code>null</code> if not explicitely set
	 * @see #setDefaultClassLoader(ClassLoader)
	 */
	public static ClassLoader getDefaultClassLoader()
	{
	    return clDefault;
	}
	
	/**
	 * Sets the resource class loader for the current thread. It's possible to set a custom thread class loader
	 * which is not the same as the {@link Thread#getContextClassLoader()}.
	 * 
	 * @param pClassLoader the class loader to use
	 */
	public static void setThreadClassLoader(ClassLoader pClassLoader)
	{
	    synchronized (oSyncThreadCl)
	    {
    	    if (pClassLoader != null)
    	    {
    	        thlClassLoader = new ThreadLocal<ClassLoader>();
    	        thlClassLoader.set(pClassLoader);
    	    }
    	    else
    	    {
    	        thlClassLoader = null;
    	    }
	    }
	}
	
	/**
	 * Gets the resource class loader for the current thread, if set.
	 * 
	 * @return the class loader or <code>null</code> if not set
	 */
	public static ClassLoader getThreadClassLoader()
	{
	    synchronized (oSyncThreadCl)
	    {
	        if (thlClassLoader != null)
	        {
	            return thlClassLoader.get();
	        }
	        else
	        {
	            return null;
	        }
	    }
	}
	
	/**
	 * Gets the internal resource class loader.
	 * 
	 * @return the default class loader if set or the thread class loader if set or <code>null</code> if 
	 *         no class loaders were set manually in this utility
	 * @see #setDefaultClassLoader(ClassLoader)         
	 * @see #setThreadClassLoader(ClassLoader)         
	 */
	private static ClassLoader getInternalResourceClassLoader()
	{
	    if (clDefault != null)
	    {
	        return clDefault;
	    }
	    else
	    {
	        synchronized (oSyncThreadCl)
	        {
	            if (thlClassLoader != null)
	            {
	                return thlClassLoader.get();
	            }
	        }
	    }
	    
	    return null;
	}

	/**
	 * Get the current class loader for a given object. If a default class loader is set, the default
	 * class loader is returned. If no default class loader is set, the class loader of the object is
	 * used. If the object is <code>null</code> the class loader of this util class is returned.
	 * 
	 * @param pObject the object
	 * @return the class loader
	 */
	public static ClassLoader getResourceClassLoader(Object pObject)
	{
	    ClassLoader clInternal = getInternalResourceClassLoader();
	    
	    if (clInternal != null)
	    {
	        return clInternal;
	    }
	    else
	    {
		    if (pObject != null)
			{
				return pObject.getClass().getClassLoader();
			}
			else
			{
				return ResourceUtil.class.getClassLoader();
			}
		}
	}

	/**
	 * Gets interfaces from a specific class and all parent classes, recursively. It also is possible to
	 * get the parent interfaces from an interface.
	 * 
	 * @param pClass the start class
	 * @param pCheckInterfaces <code>true</code> to get all parent interfaces from a detected interface
	 * @param pIncluded the allowed interfaces. A detected interface must be an instance of at least one
	 *                  interface in the included list. The list won't be used for super interfaces.
	 * @return the list of found interfaces
	 */
	public static Class<?>[] getInterfaces(Class<?> pClass, boolean pCheckInterfaces, Class<?>... pIncluded)
	{
		ArrayUtil<Class<?>> auIFaces = new ArrayUtil<Class<?>>();

		getInterfaces(auIFaces, pClass, pCheckInterfaces, pIncluded);
		
		return auIFaces.toArray(new Class<?>[auIFaces.size()]);
	}
	
	/**
	 * Gets interfaces from a specific class and all parent classes, recursively. It also is possible to
	 * get the parent interfaces from an interface.
	 * 
	 * @param pInterfaces all found interfaces
	 * @param pClass the start class
	 * @param pCheckInterfaces <code>true</code> to get all parent interfaces from a detected interface
	 * @param pIncluded the allowed interfaces. A detected interface must be an instance of at least one
	 *                  interface in the included list. The list won't be used for super interfaces.
	 */
	public static void getInterfaces(List<Class<?>> pInterfaces, Class<?> pClass, boolean pCheckInterfaces, Class<?>... pIncluded)
	{
		Class<?> clazz = pClass;
		
		Class<?>[] clsIfaces;
		
		boolean bAdd;
		
		while (clazz != null) 
		{
			clsIfaces = clazz.getInterfaces();
			
			for (Class<?> iface : clsIfaces) 
			{
				bAdd = pIncluded == null || pIncluded.length == 0;
				
				for (int i = 0; !bAdd && i < pIncluded.length; i++)
				{
					if (pIncluded[i].isAssignableFrom(iface))
					{
						bAdd = true;
					}
				}
				
				if (bAdd && !pInterfaces.contains(iface))
				{
					pInterfaces.add(iface);
					
					if (pCheckInterfaces)
					{
						getInterfaces(pInterfaces, iface, pCheckInterfaces);
					}
				}
			}
			clazz = clazz.getSuperclass();
		 }
	}

	/**
	 * Formats the given resource as loadable resource.
	 * 
	 * @param pResource the resource e.g. com/sibvisions/resources/images/ok.png
	 * @return The formatted resources as array of two elements [0].../com/sibvisions/resource/images/ok.png, 
	 *         [1]...com/sibvisions/resources/images/ok.png
	 *         <p/>
	 *         (The first element has always a leading slash and the second element never has a leading slash)
	 */
	private static final String[] formatResource(String pResource)
	{
	    String[] sResources = new String[2];
	    
        if (pResource.charAt(0) != '/')
        {
            sResources[0] = "/" + pResource;
            sResources[1] = pResource;
        }
        else
        {
            sResources[0] = pResource;
            sResources[1] = pResource.substring(1);
        }
        
        return sResources;
	}
	
}	// ResourceUtil
