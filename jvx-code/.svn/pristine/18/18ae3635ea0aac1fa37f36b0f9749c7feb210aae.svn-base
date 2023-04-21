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
 * 12.08.2013 - [JR] - create resource optionally via UI
 * 24.09.2013 - [JR] - support images with byte[]
 * 20.10.2013 - [JR] - init(): close stream [BUGFIX]
 * 13.09.2015 - [JR] - FontIcon support (FontAwesome, VaadinIcons)
 * 27.10.2015 - [JR] - #1501: Creating an instance without name
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.rad.ui.IImage;

import com.kbdunn.vaadin.addons.fontawesome.FontAwesome;
import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.vaadin.ext.FontResource;
import com.sibvisions.rad.ui.vaadin.ext.IDownloadStream;
import com.sibvisions.rad.ui.vaadin.ext.INamedResource;
import com.sibvisions.rad.ui.vaadin.ext.VaByteArrayResource;
import com.sibvisions.rad.ui.vaadin.ext.VaCachedResource;
import com.sibvisions.rad.ui.vaadin.ext.VaClassResource;
import com.sibvisions.util.SecureHash;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.imageio.MetaDataImage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.DownloadStream;
import com.vaadin.ui.UI;

/**
 * The <code>VaadinImage</code> class is the vaadin implementation of {@link IImage}.
 * 
 * @author Benedikt Cermak
 * @see	com.vaadin.ui.Image
 */
public final class VaadinImage extends VaadinResource<INamedResource, INamedResource> 
                               implements IImage
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the mapping names for images. */
	private static HashMap<String, String> imageMapping = new HashMap<String, String>();
	
	/** the reverse mapping names for images. */
	private static HashMap<String, String> reverseImageMapping = new HashMap<String, String>();
	
    /** the cache for created images (saves memory). */
    private static HashMap<String, VaadinImage> htImageCache = new HashMap<String, VaadinImage>();
	
    /** the cache for created images (increases speed). */
    private static WeakHashMap<byte[], WeakReference<VaadinImage>> htCreateImageCache = new WeakHashMap<byte[], WeakReference<VaadinImage>>();

	/** The widht of the image. **/
	private int width;
	
	/** The height of the image. **/
	private int height;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
     * Creates a new instance of <code>VaadinImage</code> with the given image name.
     *
     * @param pImageName the path to the Image Resource
     */
	protected VaadinImage(String pImageName)
	{
		super(createResource(pImageName));
		
		init();
	}

	/**
     * Creates a new instance of <code>VaadinImage</code> with the given image name
     * andimage data.
     *
     * @param pImageName the path to the Image Resource
     * @param pData the image data
     */
	protected VaadinImage(String pImageName, byte[] pData)
	{
		super(createResource(pImageName, pData));

		init();
	}

	/**
	 * Initializes properties.
	 */
	private void init()
	{
	    if (resource instanceof IDownloadStream)
	    {
    		DownloadStream dstream = ((IDownloadStream)resource).getStream();
    		
    		if (dstream != null)
    		{
    			Image img = null;
    			
    			InputStream stream = null;
    			
    			try
    			{
    				stream = dstream.getStream();
    				
    				if (stream != null)
    				{
    					byte[] byData = FileUtil.getContent(stream);
    					
    					ByteArrayInputStream bais = new ByteArrayInputStream(byData);
    					
    					try
    					{
        					img = new MetaDataImage(resource.getResourceName(), byData, ImageIO.read(bais)).getImage();
    					}
    					finally
    					{
    						CommonUtil.close(bais);
    					}
    					
    					if (img != null)
    					{
	    					width = img.getWidth(null);
	    					height = img.getHeight(null);
    					}
    					else
    					{
    						width = -1;
    						height = -1;
    					}
    				}
    				else
    				{
    				    //shouldn't happen
    				    width = -1;
    				    height = -1;
    				}
    			}
    			catch (Exception e) // Invalid jpg causes an IllegalArgumentException, not an IOException, so we should catch all Exceptions
    			{
    				LoggerFactory.getInstance(VaadinImage.class).debug("Can't load image!", resource.getResourceName(), e);
    				
    				//use default
    				width = -1;
    				height = -1;
    			}
    			finally
    			{
    			    CommonUtil.close(stream);
    			}
    		}	
    		else
    		{
    		    width = 0;
    		    height = 0;
    		}
	    }
	    else if (resource instanceof FontResource)
    	{
    		width = ((FontResource)resource).getSize();
    		height = width;
    	}
	    else
	    {
	        width = -1;
	        height = -1;
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getImageName() 
	{
		return resource.getResourceName();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getWidth() 
	{
		return width;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeight() 
	{
		return height;
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException 
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates the resource for the image.
     * 
     * @param pImageName the image name
     * @return the resource
     */
	public static INamedResource createResource(String pImageName)
	{
        String sMapping = getImageMapping(pImageName);
	    
	    if (sMapping != null 
	        && (sMapping.startsWith("FontAwesome.") || sMapping.startsWith("VaadinIcons.")))
	    {
            HashMap<String, String> hmpProp = LauncherUtil.splitImageProperties(sMapping.substring(12));

            FontResource res;
            
            String sName = hmpProp.remove("name").toUpperCase();
            sName = sName.replace("-", "_");
            
            try
            {
            	if (Character.isDigit(sName.charAt(0)))
            	{
            		sName = "_" + sName;
            	}
            	
                if (sMapping.startsWith("FontAwesome."))
                {
                    res = new FontResource(sMapping, FontAwesome.valueOf(sName));
                }
                else
                {
                    res = new FontResource(sMapping, VaadinIcons.valueOf(sName));
                }
            }
            catch (IllegalArgumentException iae)
            {
                //e.g. different FontIcon versions!
                LoggerFactory.getInstance(VaadinImage.class).error(iae);
                
                return null;
            }
            
            String sStyle = hmpProp.remove("style");
            
            if (StringUtil.isEmpty(sStyle))
            {
                //fi means FontIcon
                sStyle = "fi_" + getStyleName(sName);
            }
            
	        res.setStyleName(sStyle);
	        
	        String sKey;
	        String sValue;

	        String sMapProp = hmpProp.remove("mapping");
	        
            //mark the resource "mapped". This makes it possible to define general css settings
	        res.setMapped(sMapping != pImageName || Boolean.parseBoolean(sMapProp));
	        
	        for (Entry<String, String> entry : hmpProp.entrySet())
	        {
	            sKey = entry.getKey();
	            sValue = entry.getValue();

	            if ("size".equals(sKey))
	            {
	                //size doesn't work!
	                sKey = "font-size";
	                
	                try
	                {
	                    int iSize = Integer.parseInt(sValue);
	                    
	                    res.setSize(iSize);
	                    
	                    //default unit: pixel
	                    sValue += "px";
	                }
	                catch (NumberFormatException nfe)
	                {
	                    //value contains unit
	                }
	            }
	            
	            res.addCustomStyleProperty(sKey, sValue);
	        }
	        
	        return res;
	    }

	    if (sMapping == null)
	    {
	        return null;
	    }
	    else
	    {
    	    int iPos = sMapping.indexOf(";");
    	    
            String sResource = sMapping;
            String sStyle = null;
    
    	    if (iPos >= 0)
    	    {
    	    	sResource = sMapping.substring(0, iPos);
    	    	
    	    	HashMap<String, String> hmpProps = LauncherUtil.splitImageProperties(sMapping.substring(0));
    	        
    	    	sStyle = hmpProps.get("style");
    	    }
    
            if (sStyle == null)
            {
                sStyle = getStyleName(sResource);
            }
    
    		return createResource(sResource, sStyle);
	    }
	}
	
    /**
     * Creates the resource for the image.
     * 
     * @param pResourceName the resource name/path
     * @param pStyleName the CSS name
     * @return the resource
     */
    private static INamedResource createResource(String pResourceName, String pStyleName)
    {
        if (pResourceName == null)
        {
            return null;
        }
        
    	UI ui = UI.getCurrent();
    	
    	if (ui == null || !(ui instanceof VaadinUI))
    	{
    		return new VaClassResource(pResourceName, pStyleName);
    	}
    	else
    	{
    		VaCachedResource res = ((VaadinUI)UI.getCurrent()).createCachedResource(pResourceName);
    		res.setStyleName(pStyleName);
    		
    		return res;
    	}
    }

    /**
     * Creates the resource for the image, using the given image data.
     * 
     * @param pName the image name (maybe a resource name)
     * @param pData the image data. If image data is <code>null</code> a resource with the
     *              given name will be searched
     * @return the image resource
     */
    private static INamedResource createResource(String pName, byte[] pData)
    {
        if (pName == null)
        {
            return null;
        }
        
    	String sStyleName = getStyleName(pName);
    	
    	if (pData == null)
    	{
    		return createResource(pName, sStyleName);
    	}
    	
    	UI ui = UI.getCurrent();
    	
    	if (ui == null || !(ui instanceof VaadinUI))
    	{
        	return new VaByteArrayResource(pName, pData, sStyleName);
    	}
    	else
    	{
			VaCachedResource res = ((VaadinUI)UI.getCurrent()).createCachedResource(pName, pData);
			res.setStyleName(sStyleName);
			
			return res;
    	}
    }
    
	/**
	 * Returns the name of the image.
	 * 
	 * @return the name of the image.
	 */
	public String getName()
	{
		return resource.getResourceName();
	}
	
	/**
	 * Returns the style name.
	 * 
	 * @param pName the name.
	 * @return the style name.
	 */
	public static String getStyleName(String pName)
	{
		String name = reverseImageMapping.get(pName);
		
		if (name == null)
		{
			name = pName;
		}
		
		if (name != null)
		{
			int lastIndexFirst = name.lastIndexOf("/");
			int lastIndexLast = name.lastIndexOf(".");
			
			if (lastIndexFirst < 0)
			{
				lastIndexFirst = 0;
			}
			else
			{
				lastIndexFirst++;
			}
			
			if (lastIndexLast < 0)
			{
				lastIndexLast = name.length();
			}
			
			name = StringUtil.convertToName(name.substring(lastIndexFirst, lastIndexLast)).toLowerCase();
		}

		return name;
	}
	
	/**
	 * Returns the style name.
	 * 
	 * @return the style name.
	 */
	public String getStyleName()
	{
		return resource.getStyleName();
	}
	
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @return the image name.
     */
    public static String getImageMapping(String pMappingName)
    {
    	if (pMappingName == null)
    	{
    		return null;
    	}
    	
    	String imgName = imageMapping.get(pMappingName);
    	
    	if (imgName == null)
    	{
        	return pMappingName;
    	}
    	else
    	{
        	return imgName;
    	}
    }
    
    /**
     * Sets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @param pImageName the image name.
     */
    public static void setImageMapping(String pMappingName, String pImageName)
    {
        if (pMappingName == null)
        {
            if (pImageName != null)
            {
                VaadinUI.removeCachedResource(pImageName);
            }
        }
        else
        {
        	if (pImageName == null)
        	{
        	    String sResName = imageMapping.remove(pMappingName);
        	    
        		reverseImageMapping.remove(sResName);
        		
        		htImageCache.remove(pMappingName);
        	}
        	else
        	{
        	    String sResName = imageMapping.put(pMappingName, pImageName);
        	    
        		reverseImageMapping.remove(sResName);
            	reverseImageMapping.put(pImageName, pMappingName);

        		htImageCache.remove(pMappingName);
        	}
        }
    }

    /**
     * Gets all used mapping names.
     * 
     * @return the mapping names.
     */
    public static String[] getImageMappingNames()
    {
    	return imageMapping.keySet().toArray(new String[imageMapping.size()]);
    }
    
    /**
     * Creates a new {@link VaadinImage} instance based on the image name.
     * 
     * @param pImageName the path to the Image Resource
     * @return the image or <code>null</code> if the image name is <code>null</code>
     */
    public static VaadinImage getImage(String pImageName)
    {
        // don't load null resources
        if (pImageName == null)
        {
            return null;
        }

        VaadinImage image = htImageCache.get(pImageName);

        if (image == null)
        {
        	image = new VaadinImage(pImageName);
            
            if (image != null)
            {
                htImageCache.put(pImageName, image);
            }
        }
        
        return image;
    }

    /**
     * Creates a new {@link VaadinImage} instance based on the image name and image data.
     * 
     * @param pImageName the path to the Image Resource
     * @param pData the image data
     * @return the image or <code>null</code> if the image name is <code>null</code>
     */
    public static VaadinImage getImage(String pImageName, byte[] pData)
    {
        if (pData == null)
        {
            //maybe the image name is mapped
            return getImage(pImageName);
        }
        
        WeakReference<VaadinImage> wrImage = htCreateImageCache.get(pData);
        VaadinImage image = null;
        if (wrImage != null)
        {
            image = wrImage.get();
        }
        
        if (image == null)
        {
            if (pImageName != null)
            {
            	image = new VaadinImage(pImageName, pData);
            }
            else
            {
                try
                {
                    String sTempName = "ondemand_" + SecureHash.getHash(SecureHash.MD5, pData); // Faster but not loadbalancable would be: System.identityHashCode(pData);
                    
                    switch (ImageUtil.getImageFormat(pData))
                    {
                        case BMP:
                            sTempName += ".bmp";
                            break;
                        case GIF:
                            sTempName += ".gif";
                            break;
                        case PNG:
                            sTempName += ".png";
                            break;
                        case TIFF:
                            sTempName += ".tif";
                            break;
                        case WMF:
                            sTempName += ".wmf";
                            break;
                        case JPG:
                        case JPG2000:
                            sTempName += ".jpg";
                            break;
                        default:
                            throw new RuntimeException("Unsupported image type!");
                    }
                    
                    image = new VaadinImage(sTempName, pData);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            
            htCreateImageCache.put(pData, new WeakReference(image));
        }
        
        if (pImageName != null)
        {
            htImageCache.put(pImageName, image);
        }
        
        return image;
    }
    
} 	// VaadinImage
