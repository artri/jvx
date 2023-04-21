/*
 * Copyright 2014 SIB Visions GmbH 
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
 * 29.03.2014 - [JR] - creation
 * 14.02.2018 - [JR] - support for byte[]
 */
package com.sibvisions.rad.ui.vaadin.ext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.util.type.ResourceUtil;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.util.FileTypeResolver;

/**
 * The <code>VaCachedResource</code> loads resources from the classpath with the current or 
 * specific class loaders. The resource itself is cached per UI and will be reused.
 * 
 * @author René Jahn
 * @see com.vaadin.server.ExternalResource
 */
public class VaCachedResource extends ExternalResource
                              implements INamedResource,
                                         IDownloadStream
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** the resource path for request handling. */
    public static final String RESOURCE_PATH = "cachedresource";
    

    /** Default buffer size for this stream resource. */
    private int iBufferSize = 0;

    /** Default cache time for this stream resource. */
    private long lCacheTime = DownloadStream.DEFAULT_CACHETIME;
    
    
	/** the class loader for image loading. */
	private ClassLoader loader;

	/** the key for caching. */
	private String sCacheKey;
	
	/** the Unique identifier of the resource within the application. */
	private String sResourceName;
	
	/** The style name for the resource. **/
	private String sStyleName;
	
	/** the content. */
	private byte[] content;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaCachedResource</code>.
	 * 
	 * @param pCacheKey the cache identifier
	 * @param pResourceName the unique identifier of the resource within the application
	 * @see com.vaadin.server.ExternalResource
	 */
	public VaCachedResource(String pCacheKey, String pResourceName) 
	{
		this(pCacheKey, null, pResourceName, null);
    }
	
    /**
     * Creates a new instance of <code>VaCachedResource</code> with static content.
     * 
     * @param pCacheKey the cache identifier
     * @param pResourceName the unique identifier of the resource within the application
     * @param pContent the static content
     */
	public VaCachedResource(String pCacheKey, String pResourceName, byte[] pContent)
	{
	    this(pCacheKey, pResourceName, pContent, null);
	}
	
	/**
	 * Creates a new instance of <code>VaCachedResource</code>.
	 * 
	 * @param pCacheKey the cache identifier
	 * @param pResourceName the unique identifier of the resource within the application
	 * @param pStyleName the CSS name
	 * @see com.vaadin.server.ExternalResource
	 */
	public VaCachedResource(String pCacheKey, String pResourceName, String pStyleName) 
	{
		this(pCacheKey, null, pResourceName, pStyleName);
    }	
	
	/**
	 * Creates a new instance of <code>VaCachedResource</code> with static content.
	 * 
	 * @param pCacheKey the cache identifier
	 * @param pResourceName the unique identifier of the resource within the application
	 * @param pContent the static content
	 * @param pStyleName the CSS name
	 */
	public VaCachedResource(String pCacheKey, String pResourceName, byte[] pContent, String pStyleName)
	{
	    this(pCacheKey, null, pResourceName, pStyleName);
	    
	    content = pContent;
	}

	/**
	 * Creates a new instance of <code>VaCachedResource</code>.
	 * 
	 * @param pCacheKey the cache identifier
	 * @param pLoader the class loader for resource loading
	 * @param pResourceName the unique identifier of the resource within the application
	 * @see com.vaadin.server.ExternalResource
	 */
	public VaCachedResource(String pCacheKey, ClassLoader pLoader, String pResourceName)
	{
		this(pCacheKey, pLoader, pResourceName, null);
	}
	
	/**
	 * Creates a new instance of <code>VaCachedResource</code>.
	 * 
	 * @param pCacheKey the cache identifier
	 * @param pLoader the class loader for resource loading
	 * @param pResourceName the unique identifier of the resource within the application
	 * @param pStyleName the CSS name
	 * @see com.vaadin.server.ExternalResource
	 */
	public VaCachedResource(String pCacheKey, ClassLoader pLoader, String pResourceName, String pStyleName)
	{
        super(createResourceURL(pCacheKey, pResourceName));
        
        sCacheKey = pCacheKey;
        sResourceName = pResourceName;
        sStyleName = pStyleName;
        
        if (pLoader == null)
        {
        	if (Thread.currentThread().getContextClassLoader() != getClass().getClassLoader())
        	{
        		loader = Thread.currentThread().getContextClassLoader();
        	}
        }
        
        if (loader == null)
        {
        	loader = pLoader;
        }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getResourceName()
	{
		return sResourceName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyleName()
	{
		return sStyleName;
	}
	
    /**
     * {@inheritDoc}
     */
    public void setStyleName(String pStyleName)
    {
        sStyleName = pStyleName;
    }
    
    /**
     * {@inheritDoc}
     */
    public DownloadStream getStream() 
    {
        InputStream is;
        
        if (content == null)
        {
            ClassLoader cl = loader;
            
            if (cl == null)
            {
                cl = Thread.currentThread().getContextClassLoader();
            }
    
            is = ResourceUtil.getResourceAsStream(cl, sResourceName);
            
            //Fallback, if there's a problem with Classloader
            if (is == null)
            {
                is = ResourceUtil.getResourceAsStream(sResourceName);
            }
        }
        else
        {
            is = new ByteArrayInputStream(content);
        }
        
        DownloadStream ds = new DownloadStream(is, getMIMEType(), getFilename());
        ds.setBufferSize(getBufferSize());
        ds.setCacheTime(getCacheTime());
        
        return ds;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getMIMEType() 
    {
        return FileTypeResolver.getMIMEType(sResourceName);
    }	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the file name from the resource name.
     * 
     * @return the file name
     */
    public String getFilename()
    {
        String[] parts = sResourceName.split("/");
        
        return parts[parts.length - 1];
    }
    
	/**
	 * Creates the URL for the given resource.
	 * 
	 * @param pCacheKey the cache key for the resource
	 * @param pResourceName the resource name (usually full qualified)
	 * @return the URL for requesting the resource
	 */
	private static String createResourceURL(String pCacheKey, String pResourceName)
	{
        String[] parts = pResourceName.split("/");

        //app://APP/classloader/[idcode]/[cachekey]/[filename]
        return ApplicationConstants.APP_PROTOCOL_PREFIX + 
               ApplicationConstants.APP_PATH + '/' + 
               RESOURCE_PATH + '/' +
               VaadinUI.getIdentityCode() + '/' +
               pCacheKey + '/' + 
               parts[parts.length - 1];
	}

	/**
	 * Gets the cache key.
	 * 
	 * @return the key
	 */
	public String getCacheKey()
	{
	    return sCacheKey;
	}
	
    /**
     * Gets the size of the download buffer used for this resource.
     * 
     * <p>
     * If the buffer size is 0, the buffer size is decided by the terminal
     * adapter. The default value is 0.
     * </p>
     * 
     * @return the size of the buffer in bytes.
     */
    public int getBufferSize() 
    {
        return iBufferSize;
    }

    /**
     * Sets the size of the download buffer used for this resource.
     * 
     * @param pBufferSize the size of the buffer in bytes.
     * @see #getBufferSize()
     */
    public void setBufferSize(int pBufferSize) 
    {
        iBufferSize = pBufferSize;
    }

    /**
     * Gets the length of cache expiration time.
     * 
     * <p>
     * This gives the adapter the possibility cache streams sent to the client.
     * The caching may be made in adapter or at the client if the client
     * supports caching. Default is {@link DownloadStream#DEFAULT_CACHETIME}.
     * </p>
     * 
     * @return Cache time in milliseconds
     */
    public long getCacheTime() 
    {
        return lCacheTime;
    }

    /**
     * Sets the length of cache expiration time.
     * 
     * <p>
     * This gives the adapter the possibility cache streams sent to the client.
     * The caching may be made in adapter or at the client if the client
     * supports caching. Zero or negative value disables the caching of this
     * stream.
     * </p>
     * 
     * @param pCacheTime the cache time in milliseconds.
     * 
     */
    public void setCacheTime(long pCacheTime) 
    {
        lCacheTime = pCacheTime;
    }	

} 	// VaCachedResource
