/*
 * Copyright 2022 SIB Visions GmbH
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
 * 10.05.2022 - [JR] - creation
 */
package com.sibvisions.rad.genui;

import java.util.Map;

import javax.rad.genui.UIColor;
import javax.rad.genui.UIImage;
import javax.rad.ui.IColor;
import javax.rad.ui.IImage;

/**
 * The {@link UIFontIcon} is helper class for easier {@link IImage} creation for 
 * supported font icon libraries.
 * 
 * @author René Jahn
 */
public final class UIFontIcon
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/** supported libraries, see <a href="http://jiconfont.github.io/">JIconFont</a>. */
	public static enum Library
	{
		/** material icons. */
		Material,
		/** elusive icons. */
		Elusive,
		/** entypo icons. */
		Entypo,
		/** font awesome icons. */
		FontAwesome,
		/** iconic icons. */
		Iconic,
		/** typicons icons. */
		Typicons
	}
	
    /** the property name for font-size. */
    public static final String PROP_SIZE = "size";
    
    /** the property name for font-color. */
    public static final String PROP_COLOR = "color";
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invisible constructor because <code>UIFontIcon</code> is a utility class.
     */
    private UIFontIcon()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName)
    {
        return new ImageBuilder(pLibrary, pName).build();
    }
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pSize image size
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, int pSize)
    {
        return new ImageBuilder(pLibrary, pName).withSize(pSize).build();
    }
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pColor image color
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, IColor pColor)
    {
        return new ImageBuilder(pLibrary, pName).withColor(pColor).build();
    }
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pSize image size
     * @param pColor image color
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, int pSize, IColor pColor)
    {
        return new ImageBuilder(pLibrary, pName).withSize(pSize).withColor(pColor).build();
    }
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pProperties image properties
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, Map<String, String> pProperties)
    {
        return new ImageBuilder(pLibrary, pName).withProperties(pProperties).build();
    }

    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pSize image size
     * @param pProperties image properties
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, int pSize, Map<String, String> pProperties)
    {
        return new ImageBuilder(pLibrary, pName).withSize(pSize).withProperties(pProperties).build();
    }
    
    /**
     * Gets {@link IImage}.
     * 
     * @param pLibrary the library
     * @param pName image name
     * @param pSize image size
     * @param pColor image color
     * @param pProperties image properties
     * @return image
     */
    public static IImage getImage(Library pLibrary, String pName, int pSize, IColor pColor, Map<String, String> pProperties)
    {
        return new ImageBuilder(pLibrary, pName).withSize(pSize).withColor(pColor).withProperties(pProperties).build();
    }

    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * Helper class implements builder design pattern. Is used by {@link UIFontIcon} to build {@link IImage}.
     * 
     * @author René Jahn
     */
    private static class ImageBuilder
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** image name. */
        private String imageName;
        
        /** image path (full image name). */
        private String imagePath;
        
        /** image size. */
        private Integer size = null;
        
        /** image color. */
        private IColor color = null;
        
        /** image properties. */
        private Map<String, String> properties = null;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>ImageBuilder</code> with specified image.
         * 
         * @param pLibrary the library
         * @param pImageName image name
         */
        public ImageBuilder(Library pLibrary, String pImageName)
        {
        	imageName = pImageName;
            imagePath = "JIconFont." + pLibrary.name().toLowerCase() + "." + imageName;
        }
        
        /**
         * Sets size in builder.
         * 
         * @param pSize image size
         * @return builder itself
         */
        public ImageBuilder withSize(int pSize)
        {
            size = Integer.valueOf(pSize);
            
            return this;
        }
        
        /**
         * Sets {@link IColor} in builder.
         * 
         * @param pColor image color
         * @return builder itself
         */
        public ImageBuilder withColor(IColor pColor)
        {
            color = pColor;
            
            return this;
        }
        
        /**
         * Sets image properties in builder.
         * 
         * @param pProperties image properties
         * @return builder itself
         */
        public ImageBuilder withProperties(Map<String, String> pProperties)
        {
            properties = pProperties;
            
            return this;
        }
        
        /**
         * Builds the {@link IImage}. Transforms all properties set in builder into string and gets the {@link IImage}.
         * 
         * @return image
         */
        public IImage build()
        {
            if (imageName == null)
            {
                throw new IllegalStateException("Image name cannot be null!");
            }
            
            StringBuilder sb = new StringBuilder(imagePath);
            sb.append(";");
            
            if (size != null)
            {
                sb.append("size=");
                sb.append(size);
                sb.append(";");
            }
            
            if (color != null)
            {
                sb.append("color=");
                sb.append(UIColor.toHex(color));
                sb.append(";");
            }
            
            if (properties != null && !properties.isEmpty())
            {
                for (Map.Entry<String, String> entry : properties.entrySet())
                {
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append(";");
                }
            }
            
            //we use UIIMage to use advanced caching
            return UIImage.getImage(sb.toString());
            
            //works, but without advanced caching
            //return new SwingImage(imageName, JVxUtil.getIcon(sb.toString()));
        }
        
    }   // ImageBuilder

}   // UIFontIcon
