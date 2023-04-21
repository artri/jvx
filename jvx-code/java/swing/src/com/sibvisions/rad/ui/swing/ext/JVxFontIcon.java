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
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JVxFontIcon</code> is a wrapper for <a href="http://jiconfont.github.io/">JIconFont</a>. It
 * works without references to the libraries. If libraries are in classpath, usage of font icons will work,
 * otherwise icons are not supported.
 * 
 * @author René Jahn
 */
public final class JVxFontIcon
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
     * Invisible constructor because <code>JVxFontIcon</code> is a utility class.
     */
	private JVxFontIcon()
	{	
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the icon with given definition.
	 * 
	 * @param pDefinition the icon definition: library.name[;prop1=value1;prop2=value2].
	 *                    The default icon size is 16.
	 * @return the icon or <code>null</code> if icon name is invalid
	 * @throws IllegalArgumentException if library name is not supported/known
	 */
    public static ImageIcon createIcon(String pDefinition)
	{
		return parse(pDefinition);
	}
	
    /**
     * Parses given icon definition.
     * 
     * @param pDefinition the icon definition: library.name[;prop1=value1;prop2=value2]. 
     *                    The default icon size is 16.
     * @return the icon or <code>null</code> if icon name is invalid
     * @throws IllegalArgumentException if library name is not supported/known
     */
    private static ImageIcon parse(String pDefinition)
    {
        if (StringUtil.isEmpty(pDefinition))
        {
            throw new IllegalArgumentException("Invalid icon definition: " + pDefinition);
        }

        HashMap<String, String> hmpProps = LauncherUtil.splitImageProperties(pDefinition);
        
        String sFullName = hmpProps.get("name");
        
        int iDot = sFullName.indexOf('.');
        
        String sLibrary = sFullName.substring(0, iDot);
        String sName = sFullName.substring(iDot + 1);
        
        String sSize = hmpProps.get("size");
        
        if (sSize == null)
        {
            sSize = hmpProps.get("font-size");
            
            if (sSize == null)
            {
                sSize = "16";
            }
        }
        
        int size;
        
        try
        {
            size = Integer.parseInt(sSize);
        }
        catch (NumberFormatException nfe)
        {
            //remove optional unit
            sSize = StringUtil.getText(sSize, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '.');
            
            size = Integer.parseInt(sSize);            
        }
        
        String sColor = hmpProps.get("color");
        
        int[] iaColor = StringUtil.parseColor(sColor);
        
        Color color = null;
        
        if (iaColor != null)
        {
            if (iaColor.length == 3)
            {
                color = new Color(iaColor[0], iaColor[1], iaColor[2]);
            }
            else
            {
                color = new Color(iaColor[0], iaColor[1], iaColor[2], iaColor[3]);
            }
        }
        
        return createIcon(sLibrary, sName, size, color);
    }

    /**
     * Creates the icon.
     * 
     * @param pLibrary the library name (material, elusive, entypo, fontawesome, iconic, typicons)
     * @param pIcon the icon name
     * @param pSize the icon size
     * @param pColor the color or <code>null</code> for black
     * @return the icon or <code>null</code> if icon name is invalid
     */
    private static ImageIcon createIcon(String pLibrary, String pIcon, int pSize, Color pColor)
    {
    	Object oLibrary = null;
    	Object oIconEnum = null;
    	
		try
		{
			Class clsFont;
			
			ClassLoader loader = ResourceUtil.getResourceClassLoader(null);
			
	    	if ("material".equals(pLibrary))
	    	{
	    		//IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
	    		
	    		clsFont = Class.forName("jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons", false, loader);
	    	}
	    	else if ("elusive".equals(pLibrary))
	    	{
	    		clsFont = Class.forName("jiconfont.icons.elusive.Elusive", false, loader);
	    	}
	    	else if ("entypo".equals(pLibrary))
	    	{
	    		clsFont = Class.forName("jiconfont.icons.entypo.Entypo", false, loader);
	    	}
	    	else if ("fontawesome".equals(pLibrary))
	    	{
	    		clsFont = Class.forName("jiconfont.icons.font_awesome.FontAwesome", false, loader);
	    	}
	    	else if ("iconic".equals(pLibrary))
	    	{
	    		clsFont = Class.forName("jiconfont.icons.iconic.Iconic", false, loader);
	    	}
	    	else if ("typicons".equals(pLibrary))
	    	{
	    		clsFont = Class.forName("jiconfont.icons.typicons.Typicons", false, loader);
	    	}
	    	else 
	    	{
	    		throw new IllegalArgumentException("Icon font library '" + pLibrary + "' is not supported!");
	    	}
	    	
    		Method meth = clsFont.getMethod("getIconFont");
    		
    		oLibrary = meth.invoke(null);
    		
    		Object[] oIcons = clsFont.getEnumConstants();

    		Class<?> clsIconCode = Class.forName("jiconfont.IconCode", false, loader);
    		meth = clsIconCode.getMethod("name");
    		
    		for (int i = 0; i < oIcons.length && oIconEnum == null; i++)
    		{
    			if (pIcon.equals(meth.invoke(oIcons[i])))
    			{
    				oIconEnum = oIcons[i];
    			}
    		}
    	
	    	if (oLibrary != null && oIconEnum != null)
	    	{
	    		//IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CAMERA, 40, new Color(0, 150, 0));
	    		
				Class<?> cls = Class.forName("jiconfont.swing.IconFontSwing", false, loader);
				
				Class<?> clsIconFont = Class.forName("jiconfont.IconFont", false, loader);
				
				meth = cls.getMethod("register", clsIconFont);
				
				meth.invoke(null, oLibrary);

				if (pColor == null)
				{
					meth = cls.getMethod("buildIcon", clsIconCode, float.class);
					
					return (ImageIcon)meth.invoke(null, oIconEnum, Float.valueOf(pSize));
				}
				else
				{
					meth = cls.getMethod("buildIcon", clsIconCode, float.class, Color.class);
					
					return (ImageIcon)meth.invoke(null, oIconEnum, Float.valueOf(pSize), pColor);
				}
	    	}
	    	
	    	return null;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
    }
    
}	// JVxFontIcon
