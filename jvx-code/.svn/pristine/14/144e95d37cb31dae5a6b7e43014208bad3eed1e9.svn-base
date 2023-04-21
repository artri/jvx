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
 * 14.11.2008 - [HM] - creation
 * 03.02.2011 - [JR] - #273: createColor implemented
 * 30.09.2015 - [JR] - StringUtil.parseColor used
 */
package javax.rad.genui;

import java.util.HashMap;
import java.util.IdentityHashMap;

import javax.rad.ui.IColor;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent Color.
 * 
 * @author Martin Handsteiner
 */
public class UIColor extends UIFactoryResource<IColor> 
                     implements IColor
{ 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/** The color white.  In the default sRGB space. */
    public static final UIColor white     = new UIColor(255, 255, 255);
    /** The color light gray.  In the default sRGB space. */
    public static final UIColor lightGray = new UIColor(192, 192, 192);
    /** The color gray.  In the default sRGB space. */
    public static final UIColor gray      = new UIColor(128, 128, 128);
    /** The color dark gray.  In the default sRGB space. */
    public static final UIColor darkGray  = new UIColor(64, 64, 64);
    /** The color black.  In the default sRGB space. */
    public static final UIColor black 	= new UIColor(0, 0, 0);
    /** The color red.  In the default sRGB space. */
    public static final UIColor red       = new UIColor(255, 0, 0);
    /** The color pink.  In the default sRGB space. */
    public static final UIColor pink      = new UIColor(255, 175, 175);
    /** The color orange.  In the default sRGB space. */
    public static final UIColor orange 	= new UIColor(255, 200, 0);
    /** The color yellow.  In the default sRGB space. */
    public static final UIColor yellow 	= new UIColor(255, 255, 0);
    /** The color green.  In the default sRGB space. */
    public static final UIColor green 	= new UIColor(0, 255, 0);
    /** The color magenta.  In the default sRGB space. */
    public static final UIColor magenta	= new UIColor(255, 0, 255);
    /** The color cyan.  In the default sRGB space. */
    public static final UIColor cyan 	= new UIColor(0, 255, 255);
    /** The color blue.  In the default sRGB space. */
    public static final UIColor blue 	= new UIColor(0, 0, 255);

	/** The color for control background color. */
    public static final UIColor controlBackground 					= UIColor.getSystemColor(CONTROL_BACKGROUND);
	/** The color for control alternate background color. */
    public static final UIColor controlAlternateBackground 			= UIColor.getSystemColor(CONTROL_ALTERNATE_BACKGROUND);
	/** The color for control foreground color. */
    public static final UIColor controlForeground 					= UIColor.getSystemColor(CONTROL_FOREGROUND);
	/** The color for control active selection background color. */
    public static final UIColor controlActiveSelectionBackground 	= UIColor.getSystemColor(CONTROL_ACTIVE_SELECTION_BACKGROUND);
	/** The color for control active selection foreground color. */
    public static final UIColor controlActiveSelectionForeground 	= UIColor.getSystemColor(CONTROL_ACTIVE_SELECTION_FOREGROUND);
	/** The color for control inactive selection background color. */
    public static final UIColor controlInactiveSelectionBackground 	= UIColor.getSystemColor(CONTROL_INACTIVE_SELECTION_BACKGROUND);
	/** The color for control inactive selection foreground color. */
    public static final UIColor controlInactiveSelectionForeground 	= UIColor.getSystemColor(CONTROL_INACTIVE_SELECTION_FOREGROUND);
	/** The constant for control mandatory background color. */
    public static final UIColor controlMandatoryBackground 			= UIColor.getSystemColor(CONTROL_MANDATORY_BACKGROUND);
	/** The constant for control read only background color. */
    public static final UIColor controlReadOnlyBackground 			= UIColor.getSystemColor(CONTROL_READ_ONLY_BACKGROUND);
	/** The color for invalid editor background color. */
    public static final UIColor invalidEditorBackground 			= UIColor.getSystemColor(INVALID_EDITOR_BACKGROUND);

    /** Stores the system colors. */
    private static HashMap<String, UIColor> systemColors;
    /** Stores the system colors. */
    private static IdentityHashMap<UIColor, String> systemColorsName;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates an UIColor of an IColor for compatibility reasons.
     * @param pColor the IColor.
     */
    protected UIColor(IColor pColor)
    {
    	super(pColor);
    }
    
    /**
     * Creates an UIColor of given color definition.
     * 
     * @param pColor the color definition, e.g. 100,100, 100 or #123123
     */
    public UIColor(String pColor)
    {
    	super(createColorIntern(pColor, true));
    }

    /**
     * Creates an opaque sRGB color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param pRGBA the combined RGB components
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getRGBA
     */
    public UIColor(int pRGBA)
	{
		super(UIFactoryManager.getFactory().createColor(0xFF000000 | pRGBA));
	}

    /**
     * Creates an sRGB color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>pHasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param pRGBA the combined RGBA components
     * @param pHasalpha <code>true</code> if the alpha bits are valid;
     *        <code>false</code> otherwise
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getAlpha
     * @see IColor#getRGBA
     */
    public UIColor(int pRGBA, boolean pHasalpha)
	{
		super(UIFactoryManager.getFactory().createColor(pHasalpha ? pRGBA : 0xFF000000 | pRGBA));
	}

    /**
     * Creates an opaque sRGB color with the specified red, green, 
     * and blue values in the range (0 - 255). The actual color 
     * used in rendering depends on finding the best match given 
     * the color space available for a given output device.  
     * Alpha is defaulted to 255.
     *
     * @param pR the red component
     * @param pG the green component
     * @param pB the blue component
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getRGBA
     */
    public UIColor(int pR, int pG, int pB)
	{
		super(UIFactoryManager.getFactory().createColor(0xFF000000 | ((pR & 0xFF) << 16) | ((pG & 0xFF) << 8) | (pB & 0xFF)));
	}

    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha
     * values in the range (0 - 255).
     *
     * @param pR the red component
     * @param pG the green component
     * @param pB the blue component
     * @param pA the alpha component
     * @see IColor
     * @see IColor#getRed
     * @see IColor#getGreen
     * @see IColor#getBlue
     * @see IColor#getAlpha
     * @see IColor#getRGBA
     */
    public UIColor(int pR, int pG, int pB, int pA)
	{
        //(Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue)
		super(UIFactoryManager.getFactory().createColor(((pA & 0xFF) << 24) | ((pR & 0xFF) << 16) | ((pG & 0xFF) << 8) | (pB & 0xFF)));
	}
    
    /**
     * Creates a color with given definition.
     * 
     * @param pColor the color definition
     * @param pStrict whether to allow null image creation if color is "wrong" 
     * @return the color or <code>null</code> if color creation is not possible
     * @throws {@link IllegalArgumentException} if color creation fails and strict mode is disabled
     */
    private static IColor createColorIntern(String pColor, boolean pStrict)
    {
    	int[] iaColor = StringUtil.parseColor(pColor);
    	
    	if (iaColor != null)
    	{
	    	if (iaColor.length == 3)
	        {
	            return UIFactoryManager.getFactory().createColor(0xFF000000 | ((iaColor[0] & 0xFF) << 16) | ((iaColor[1] & 0xFF) << 8) | (iaColor[2] & 0xFF));
	        }
	        else if (iaColor.length == 4)
	        {
	            return UIFactoryManager.getFactory().createColor(((iaColor[3] & 0xFF) << 24) | ((iaColor[0] & 0xFF) << 16) | ((iaColor[1] & 0xFF) << 8) | (iaColor[2] & 0xFF));
	        }
    	}
    	
		if (pStrict)
		{
			throw new IllegalArgumentException("Invalid color " + pColor);
		}
		else
		{
			return null;
		}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getRed()
    {
	    if (getUIResource() == null)
	    {
	        return 0;
	    }
	    else
	    {
	        return getUIResource().getRed();
	    }
    }

	/**
	 * {@inheritDoc}
	 */
	public int getGreen()
    {
        if (getUIResource() == null)
        {
            return 0;
        }
        else
        {
            return getUIResource().getGreen();
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public int getBlue()
    {
        if (getUIResource() == null)
        {
            return 0;
        }
        else
        {
            return getUIResource().getBlue();
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public int getAlpha()
    {
        if (getUIResource() == null)
        {
            return 0;
        }
        else
        {
            return getUIResource().getAlpha();
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getRGBA()
    {
        if (getUIResource() == null)
        {
            return 0;
        }
        else
        {
            return getUIResource().getRGBA();
        }
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
    {
		if (pObject instanceof IColor)
		{
			if (systemColorsName.get(this) != null)
			{
				return this == pObject;
			}
			else
			{
				return getRGBA() == ((IColor)pObject).getRGBA();
			}
		}
		
    	return false;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
    {
		String name = systemColorsName.get(this);
		
		if (name == null)
		{
			return getRGBA();
		}
		else
		{
			return name.hashCode();
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
    {
		String name = systemColorsName.get(this);
		
		if (name == null)
		{
			return getClass().getName() + "[" + toHex() + "]";
		}
		else
		{
			return getClass().getName() + "[" + name + "=" + (getUIResource() == null ? "<None>" : toHex()) + "]";
		}
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * This encapsulate symbolic colors representing the color of
     * native GUI objects on a system.  For systems which support the dynamic
     * update of the system colors (when the user changes the colors)
     * the actual RGB values of these symbolic colors will also change
     * dynamically.  In order to compare the "current" RGB value of a
     * <code>SystemColor</code> object with a non-symbolic Color object,
     * <code>getRGB</code> should be used rather than <code>equals</code>.
     * <p>
     * Note that the way in which these system colors are applied to GUI objects
     * may vary slightly from platform to platform since GUI objects may be
     * rendered differently on each platform.
     *
     * @param pType the type
     * @return the <code>IColor</code>
     * @see IColor
     */
    public static UIColor getSystemColor(String pType)
    {
    	if (systemColors == null)
    	{
    		systemColors = new HashMap<String, UIColor>();
    		systemColorsName = new IdentityHashMap<UIColor, String>();
    	}
    	
    	UIColor color = systemColors.get(pType);
    	if (color == null)
    	{
    		color = new UIColor(UIFactoryManager.getFactory().getSystemColor(pType));
    		
    		systemColors.put(pType, color);
    		systemColorsName.put(color, pType);
    	}
    	return color;
    }

    /**
     * Gets the name of a system color. If it is not a system color, null is returned.
     *
     * @param pColor the color
     * @return the name of a system color. If it is not a system color, null is returned.
     */
    public static String getSystemColorName(UIColor pColor)
    {
    	return systemColorsName.get(pColor);
    }

    /**
     * Sets the given <code>IColor</code> as SystemColor.
     * If <code>pSystemColor</code> is <code>null</code> the 
     * original <code>SystemColor</code> is restored.
     *
     * @param pType the type
     * @param pSystemColor the <code>IColor</code>
     * @see IColor
     */
    public static void setSystemColor(String pType, IColor pSystemColor)
    {
    	if (pSystemColor instanceof UIColor)
    	{
    		pSystemColor = ((UIColor)pSystemColor).getUIResource();
    	}
    	UIFactoryManager.getFactory().setSystemColor(pType, pSystemColor);
    	
    	UIColor systemColor = systemColors.get(pType);
    	
    	if (systemColor != null)
    	{
    		systemColor.setUIResource(pSystemColor);
    	}
    }
    
	/**
	 * Creates a color from a color definition, e.g. #FF00FF or RGB values.
	 * 
	 * @param pValue the value string
	 * @return the color or <code>null</code> if the color values are invalid
	 * @see StringUtil#parseColor(String)
	 */
	public static UIColor createColor(String pValue)
	{
		IColor color = createColorIntern(pValue, false);
		
		if (color != null)
		{
			return new UIColor(color);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the hex representation of the given {@link IColor}. The returned
	 * representation is in the format {@code #RRGGBB} (like {@code #FF9900})
	 * and is compatible with {@link #createColor(String)}.
	 * 
	 * @param pColor the {@link IColor} to get the hex representation of.
	 * @return the hex representation.
	 */
	public static String toHex(IColor pColor)
	{
	    return "#" + Integer.toString(0x1000000 | (pColor.getRGBA() & 0xffffff), 16).substring(1).toUpperCase() 
	               + (pColor.getAlpha() != 0xFF ? Integer.toString(0x100 | (pColor.getAlpha() & 0xff), 16).substring(1).toUpperCase() : "");
    }
	
	/**
	 * Returns the hex representation of this {@link IColor}. The returned
	 * representation is in the format {@code #RRGGBB} (like {@code #FF9900})
	 * and is compatible with {@link #createColor(String)}.
	 * 
	 * @return the hex representation.
	 * @see #toHex(IColor)
	 */
	public String toHex()
	{
	    return toHex(this);
	}
	
}	// UIColor
