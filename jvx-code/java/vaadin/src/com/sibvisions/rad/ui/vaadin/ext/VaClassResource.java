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
 * 18.10.2012 - [CB] - creation
 * 12.08.2013 - [JR] - use a custom class loader for resource loading
 */
package com.sibvisions.rad.ui.vaadin.ext;

import java.io.InputStream;

import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.vaadin.server.ClassResource;
import com.vaadin.server.DownloadStream;

/**
 * The <code>VaClassResource</code> loads resources from the classpath with the current or 
 * specific class loaders.
 * 
 * @author Benedikt Cermak
 * @see com.vaadin.server.ClassResource
 */
public class VaClassResource extends ClassResource
                             implements INamedResource,
                                        IDownloadStream
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the class loader for image loading. */
	private ClassLoader loader;

	/** the Unique identifier of the resource within the application. */
	private String sResourceName;
	
	/** The style name for the resource. **/
	private String sStyleName;
	
	/** the file name. */
	private String sFileName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaClassResource</code>.
	 * 
	 * @param pResourceName the unique identifier of the resource within the application.
	 * @see com.vaadin.server.ClassResource
	 */
	public VaClassResource(String pResourceName) 
	{
		this(null, pResourceName, null);
    }
	
	/**
	 * Creates a new instance of <code>VaClassResource</code>.
	 * 
	 * @param pResourceName the unique identifier of the resource within the application.
	 * @param pStyleName the CSS name
	 * @see com.vaadin.server.ClassResource
	 */
	public VaClassResource(String pResourceName, String pStyleName) 
	{
		this(null, pResourceName, pStyleName);
    }	

	/**
	 * Creates a new instance of <code>VaClassResource</code>.
	 * 
	 * @param pLoader the class loader for resource loading.
	 * @param pResourceName the unique identifier of the resource within the application.
	 * @see com.vaadin.server.ClassResource
	 */
	public VaClassResource(ClassLoader pLoader, String pResourceName)
	{
		this(pLoader, pResourceName, null);
	}
	
	/**
	 * Creates a new instance of <code>VaClassResource</code>.
	 * 
	 * @param pLoader the class loader for resource loading.
	 * @param pResourceName the unique identifier of the resource within the application.
	 * @param pStyleName the CSS name
	 * @see com.vaadin.server.ClassResource
	 */
	public VaClassResource(ClassLoader pLoader, String pResourceName, String pStyleName)
	{
        super(pResourceName);
        
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	@Override
    public DownloadStream getStream() 
    {
		ClassLoader cl = loader;
		
		if (cl == null)
		{
			cl = Thread.currentThread().getContextClassLoader();
		}

		InputStream is = ResourceUtil.getResourceAsStream(cl, sResourceName);
		
        //Fallback, if there's a problem with Classloader
        if (is == null)
        {
            is = ResourceUtil.getResourceAsStream(sResourceName);
        }
		
		DownloadStream ds = new DownloadStream(is, getMIMEType(), getFilename());
		
        ds.setBufferSize(getBufferSize());
        ds.setCacheTime(getCacheTime());
        
        return ds;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFilename()
    {
        if (sFileName == null)
        {
            return super.getFilename();    
        }
        else
        {
            return sFileName;
        }
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the file name. The file name could be set if it's different to the resource name.
	 * Be careful and don't add <code>?</code> as additional url parameter because the request
	 * won't handle additional parameters.
	 * 
	 * @param pFileName the file name
	 */
	public void setFilename(String pFileName)
	{
	    sFileName = FileUtil.getName(pFileName);
	}
	
} 	// VaClassResource
