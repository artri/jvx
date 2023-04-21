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
 * 19.11.2009 - [HM] - creation
 * 09.01.2011 - [JR] - getImageFromURI: checked null prefix (app context reload)
 * 21.02.2011 - [JR] - #62: saveAs with output stream 
 * 28.02.2011 - [JR] - setImageMapping: clear image cache 
 * 21.08.2013 - [JR] - <init>: checked if image != null  
 */
package com.sibvisions.rad.ui.web.impl;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.rad.ui.IImage;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.ImageUtil.ImageFormat;
import com.sibvisions.util.type.imageio.MetaDataImage;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of image definition.
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Image
 */
public final class WebImage extends WebResource 
                            implements IImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Unique Image counter. */
	private static int imageNameCounter = 0;
	
	/** the cache for static created images (saves memory). */
	private static HashMap<String, WebImage> imageCacheStatic = new HashMap<String, WebImage>();
	
    /** the cache for dynamic created images (saves memory). */
    private static HashMap<String, WeakReference<byte[]>> imageCacheDynamic = new HashMap<String, WeakReference<byte[]>>();
    
	/** the cache for created images (saves Memory). */
	private static WeakHashMap<byte[], SoftReference<WebImage>> createImageCache = new WeakHashMap<byte[], SoftReference<WebImage>>();
	
	/** the cache for created images (saves Memory). */
	private static WeakHashMap<WebImage, WebImage> scaledImageCache = new WeakHashMap<WebImage, WebImage>();

	/** the mapping names for images. */
	private static Hashtable<String, String> imageMapping = new Hashtable<String, String>();
	
	/** Unique Image counter. */
	private static String urlPrefix = null;
	
	/** the context class loader. */
	private ClassLoader loader;
	
	/** The image name. */
	private String imageName;
	/** The image properties. */
	private String imageProps = null;
	/** The image data. */
	private byte[] data;
	
	/** The width. */
	private int width;
	/** The height. */
	private int height;
	
	/** the creation date. */
	private long lCreationDate;
	/** whether the image is a dynamic image. */
	private boolean bDynamic;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Creates an empty image with given dynamic property.
	 * 
	 * @param pImageName the image name
	 * @param pDynamic whether the image is dynamic
	 */
	private WebImage(String pImageName, boolean pDynamic)
	{
        loader = Thread.currentThread().getContextClassLoader();
        
        imageName = pImageName;
        
        lCreationDate = System.currentTimeMillis();
        
        bDynamic = pDynamic;
        
        width = 0;
        height = 0;
	}
	
	/**
	 * Creates a new instance of <code>WebImage</code> for specific byte data
	 * and an image name.
	 * 
	 * @param pImageName the name of the image specified with <code>byData</code>
	 * @param pData the image data
	 * @throws Exception if image can not be created
	 */
	private WebImage(String pImageName, byte[] pData) throws Exception
	{
        //custom data means dynamic image
	    this(pImageName, pData != null);

	    data = pData;
	    
		if (imageName.charAt(0) != '/' && !imageName.startsWith("FontAwesome."))
		{
			imageName = "/" + imageName;
		}
		
		HashMap<String, String> hmpProp = LauncherUtil.splitImageProperties(imageName);
		
		imageName = hmpProp.remove("name");

		if (imageName.startsWith("FontAwesome."))
		{
			String sSize = hmpProp.remove("size");
			
			if (StringUtil.isEmpty(sSize))
			{
				width = 16;
			}
			else
			{
				try
				{
					width = Integer.parseInt(sSize);
				}
				catch (Exception ex)
				{
					width = 0;
				}
			}
			
			height = width;
		}
		else
		{
			Image image = null;

			byte[] byData = FileUtil.getContent(getInputStream());
			
			ByteArrayInputStream bais = new ByteArrayInputStream(byData);
			
			try
			{
				image = new MetaDataImage(pImageName, byData, ImageIO.read(bais)).getImage();
			}
			catch (Exception ex)
			{
				LoggerFactory.getInstance(WebImage.class).debug("Can't load image!", pImageName, ex);
			}
			finally
			{
				CommonUtil.close(bais);
			}
			
			if (image != null)
			{
				width = image.getWidth(null);
				height = image.getHeight(null);
			}
		}

		if (!hmpProp.isEmpty())
		{
			StringBuilder sbImage = new StringBuilder();
	
			for (Entry<String, String> prop : hmpProp.entrySet())
			{
				if (sbImage.length() > 0)
				{
					sbImage.append(";");
				}
				
				sbImage.append(prop.getKey());
				sbImage.append("=");
				sbImage.append(prop.getValue());
			}
			
			imageProps = sbImage.toString();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
    public String getAsString()
    {
    	return (urlPrefix == null ? "" : urlPrefix) + imageName + (imageProps != null ? ";" + imageProps : "") + "," + width + "," + height + "," + bDynamic;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
		// TODO [HM] don't know where to save image.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Creates a new instance of <code>WebImage</code> with a specific image name.
     *
     * @param pImageName the Image name.
     * @return the WebImage.
     * @see IImage
     */
	public static WebImage getImage(String pImageName)
	{
		// don't load null resources
		if (pImageName == null)
		{
			return null;
		}
		
		String sImageName = getImageMapping(pImageName);
    	
		WebImage image = imageCacheStatic.get(sImageName);
		
		if (image == null)
		{
            WeakReference<byte[]> imageDataRef = imageCacheDynamic.get(sImageName);

            if (imageDataRef != null)
            {
                byte[] imageData = imageDataRef.get(); 
                
                if (imageData == null)
                {
                    imageCacheDynamic.remove(sImageName);
                    
                    return new WebImage(sImageName, true); 
                }
                else
                {
                    return getImage(sImageName, imageData);
                }
            }
		    
			try
			{
				image = new WebImage(sImageName, null);
				
				if (image.imageName.startsWith("/"))
				{
					imageCacheStatic.put(image.imageName, image);
				}
				else
				{
					//otherwise parameters of e.g. FontAwesome are not part of the image
					imageCacheStatic.put(sImageName, image);
				}
			}
			catch (Exception ex)
			{
				LoggerFactory.getInstance(WebImage.class).debug("Can't load image!", sImageName, ex);
			}
		}
		
		return image;
	}
	
	/**
	 * Creates a new instance of <code>WebImage</code> for specific byte data
	 * and an image name.
	 * 
	 * @param pImageName the name of the image specified with <code>byData</code>
	 * @param pData the image data
     * @return the WebImage.
	 */
	public static WebImage getImage(String pImageName, byte[] pData)
	{
		if (pData == null)
		{
			return null;
		}

		SoftReference<WebImage> wrImage = createImageCache.get(pData);
		WebImage image = null;
		
		if (wrImage != null)
		{
			image = wrImage.get();
		}

        String imageName;
        if (pImageName == null)
        {
            if (image == null)
            {
                imageName = "/internal/Image" + imageNameCounter++;
                
                ImageFormat iformat = ImageUtil.getImageFormat(pData);
                
                if (iformat != ImageFormat.UNKNOWN)
                {
                	imageName += "." + iformat.toString().toLowerCase();
                }
                
                imageCacheDynamic.put(imageName, new WeakReference(pData));
            }
            else
            {
                imageName = image.getImageName();
            }
        }
        else
        {
            imageName = pImageName;
        }

		if (image == null)
		{
			try
			{
				image = new WebImage(imageName, pData);
				
				createImageCache.put(pData, new SoftReference(image));
			}
			catch (Exception ex)
			{
				LoggerFactory.getInstance(WebImage.class).debug("Can't load image!", imageName, ex);
			}
		}
		
		if (pImageName != null && !pImageName.startsWith("/internal/Image") && image != null)
		{
			imageCacheStatic.put(pImageName, image);
		}

		return image;
	}
	
	/**
	 * Gets the image stream.
	 * 
	 * @return image stream.
	 */
	public InputStream getInputStream()
	{
		if (data == null)
		{
			return ResourceUtil.getResourceAsStream(loader, imageName);
		}
		else
		{
			return new ByteArrayInputStream(data);
		}
	}

	/**
	 * Gets the image name.
	 * 
	 * @return image name.
	 */
	public String getImageName()
	{
		return imageName;
	}

	/**
	 * Gets the image stream.
	 * 
	 * @return image stream.
	 */
	public byte[] getData()
	{
		if (data == null)
		{
			return FileUtil.getContent(ResourceUtil.getResourceAsStream(loader, imageName));
		}
		else
		{
			return data;
		}
	}
	
	/**
	 * Gets whether the image is a dynamic image.
	 * 
	 * @return <code>true</code> if image was created with custom data, <code>false</code> otherwise
	 */
	public boolean isDynamic()
	{
		return bDynamic;
	}
	
	/**
	 * Gets the creation date.
	 * 
	 * @return the creation date in millis
	 */
	public long getCreationDate()
	{
		return lCreationDate;
	}

	/**
     * Gets the URL for the given image name.
     * 
     * @param pImageName the image name.
     * @return the Image URI.
     */
    public static String getImageURI(String pImageName)
    {
    	WebImage image = getImage(pImageName);
    	
    	if (image == null)
    	{
    		return null;
    	}
    	else
    	{
    		return image.getAsString();
    	}
    }
    
	/**
     * Gets the URL for the given image name and data.
     * 
     * @param pImageName the image name.
     * @param pData the data.
     * @return the Image URI.
     */
    public static String getImageURI(String pImageName, byte[] pData)
    {
    	WebImage image = getImage(pImageName, pData);
    	
    	if (image == null)
    	{
    		return null;
    	}
    	else
    	{
    		return image.getAsString();
    	}
    }
    
	/**
     * Gets the WebImage from a given request URI.
     * 
     * @param pRequestURI the request URI.
     * @return the WebImage.
     */
    public static WebImage getImageFromURI(String pRequestURI)
    {
    	//possible after app context reload
    	if (urlPrefix == null)
    	{
    		return null;
    	}
    	
		return imageCacheStatic.get(pRequestURI.substring(urlPrefix.length()));
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
                imageCacheStatic.remove(pImageName);
            }
        }
        else
        {
        	if (pImageName == null)
        	{
            	imageMapping.remove(pMappingName);
        	}
        	else
        	{
            	imageMapping.put(pMappingName, pImageName);
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
     * Gets the url prefix.
     * 
     * @return the url prefix.
     */
    public static String getURLPrefix()
    {
    	return urlPrefix;
    }
    
    /**
     * Sets the url prefix.
     * 
     * @param pUrlPrefix the url prefix.
     */
    public static void setURLPrefix(String pUrlPrefix)
    {
    	urlPrefix = pUrlPrefix;
    }

    /**
     * Creates a scaled image.
     * 
     * @param pImage the image to scale
     * @return the scaled image
     */
    public static WebImage createScaledImage(WebImage pImage)
    {
		if (pImage == null)
		{
			return null;
		}
		else
		{
			if (pImage.getWidth() > 16 || pImage.getHeight() > 16)
			{
				WebImage scaledImage = scaledImageCache.get(pImage);
				
				if (scaledImage == null)
				{
					ByteArrayOutputStream imageData = new ByteArrayOutputStream(); 
					
					try
					{
						String sName = pImage.getImageName();
						
						String sExt = FileUtil.getExtension(sName);
						
						ImageUtil.createScaledImage(pImage.getInputStream(), 16, 16, true, imageData, sExt != null ? sExt.toLowerCase() : "png");
						
						scaledImage = getImage(FileUtil.removeExtension(sName) + "$16x16." + sExt, imageData.toByteArray());
						scaledImage.imageName = sName;
					}
					catch (Exception ex)
					{
						scaledImage = pImage;
					}
				}
				
				scaledImageCache.put(pImage, scaledImage);

				return scaledImage;
			}
			
			return pImage;
		}
    }
    
} 	// WebImage
