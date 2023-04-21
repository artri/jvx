/*
 * Copyright 2019 SIB Visions GmbH
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
 * 09.04.2019 - [DJ] - creation
 */
package javax.rad.genui;

import java.util.Map;

import javax.rad.ui.IColor;

/**
 * The {@link UIFontAwesome} is helper class for easier {@link UIImage} creation.
 * 
 * @author Jozef Dorko
 */
public final class UIFontAwesome
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Standard supported style property. Can be used in property map. */
    public static final String PROP_STYLENAME = "style";
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invisible constructor because <code>UIFontAwesome</code> is a utility class.
     */
    private UIFontAwesome()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @return image
     */
    public static UIImage getImage(String pName)
    {
        return new ImageBuilder(pName).build();
    }
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pSize image size
     * @return image
     */
    public static UIImage getImage(String pName, int pSize)
    {
        return new ImageBuilder(pName).withSize(pSize).build();
    }
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pColor image color
     * @return image
     */
    public static UIImage getImage(String pName, IColor pColor)
    {
        return new ImageBuilder(pName).withColor(pColor).build();
    }
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pSize image size
     * @param pColor image color
     * @return image
     */
    public static UIImage getImage(String pName, int pSize, IColor pColor)
    {
        return new ImageBuilder(pName).withSize(pSize).withColor(pColor).build();
    }
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pProperties image properties
     * @return image
     */
    public static UIImage getImage(String pName, Map<String, String> pProperties)
    {
        return new ImageBuilder(pName).withProperties(pProperties).build();
    }

    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pSize image size
     * @param pProperties image properties
     * @return image
     */
    public static UIImage getImage(String pName, int pSize, Map<String, String> pProperties)
    {
        return new ImageBuilder(pName).withSize(pSize).withProperties(pProperties).build();
    }
    
    /**
     * Gets {@link UIImage}.
     * 
     * @param pName image name
     * @param pSize image size
     * @param pColor image color
     * @param pProperties image properties
     * @return image
     */
    public static UIImage getImage(String pName, int pSize, IColor pColor, Map<String, String> pProperties)
    {
        return new ImageBuilder(pName).withSize(pSize).withColor(pColor).withProperties(pProperties).build();
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * Helper class implements builder design pattern. Is used by {@link UIFontAwesome} to build {@link UIImage}.
     * 
     * @author Jozef Dorko
     */
    private static class ImageBuilder
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** Image name. */
        private String imageName;
        
        /** Image size. */
        private Integer size = null;
        
        /** Image color. */
        private IColor color = null;
        
        /** Image properties. */
        private Map<String, String> properties = null;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>ImageBuilder</code> with specified image name.
         * 
         * @param pImageName image name
         */
        public ImageBuilder(String pImageName)
        {
            imageName = pImageName;
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
         * Builds the {@link UIImage}. Transforms all properties set in builder into string and gets the {@link UIImage}.
         * 
         * @return image
         */
        public UIImage build()
        {
            if (imageName == null)
            {
                throw new IllegalStateException("Image name cannot be null!");
            }
            
            StringBuilder sb = new StringBuilder(imageName);
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
            
            return UIImage.getImage(sb.toString());
        }
        
    }   // ImageBuilder

}   // UIFontAwesome
