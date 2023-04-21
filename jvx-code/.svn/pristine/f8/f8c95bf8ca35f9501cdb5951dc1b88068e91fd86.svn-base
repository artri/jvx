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
 */
package javax.rad.genui;

import javax.rad.ui.IFont;

/**
 * Platform and technology independent Font.
 * 
 * @author Martin Handsteiner
 */
public class UIFont extends UIFactoryResource<IFont> 
                    implements IFont
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The default font is taken at runtime from label. */
	private static UIFont defaultFont = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>UIFont</code> from the specified name, 
     * style and point size.
     * <p>
     * The font name can be a font face name or a font family name.
     * It is used together with the style to find an appropriate font face.
     * When a font family name is specified, the style argument is used to
     * select the most appropriate face from the family. When a font face
     * name is specified, the face's style and the style argument are
     * merged to locate the best matching font from the same family.
     * For example if face name "Arial Bold" is specified with style
     * <code>UIFont.ITALIC</code>, the font system looks for a face in the
     * "Arial" family that is bold and italic, and may associate the font
     * instance with the physical font face "Arial Bold Italic".
     * The style argument is merged with the specified face's style, not
     * added or subtracted.
     * This means, specifying a bold face and a bold style does not
     * double-embolden the font, and specifying a bold face and a plain
     * style does not lighten the font.
     * <p>
     * If no face for the requested style can be found, the font system
     * may apply algorithmic styling to achieve the desired style.
     * For example, if <code>ITALIC</code> is requested, but no italic
     * face is available, glyphs from the plain face may be algorithmically
     * obliqued (slanted).
     * <p>
     * Font name lookup is case insensitive, using the case folding
     * rules of the US locale.
     * <p>
     * If the <code>name</code> parameter represents something other than a
     * logical font, i.e. is interpreted as a physical font face or family, and
     * this cannot be mapped by the implementation to a physical font or a
     * compatible alternative, then the font system will map the Font
     * instance to "Dialog", such that for example, the family as reported
     * by {@link #getFamily() getFamily} will be "Dialog".
     * <p>
     *
     * @param pName the font name. This can be a font face name or a font
     *        family name, and may represent either a logical font or a physical
     *        font found in this <code>GraphicsEnvironment</code>.
     *        The family names for logical fonts are: Dialog, DialogInput,
     *        Monospaced, Serif, or SansSerif. Pre-defined String constants exist
     *        for all of these names, eg @see #DIALOG. If <code>name</code> is
     *        <code>null</code>, the <em>logical font name</em> of the new
     *        <code>UIFont</code> as returned by <code>getName()</code>is set to
     *        the name "Default".
     * @param pStyle the style constant for the <code>UIFont</code>
     *        The style argument is an integer bitmask that may
     *        be PLAIN, or a bitwise union of BOLD and/or ITALIC
     *        (for example, ITALIC or BOLD|ITALIC).
     *        If the style argument does not conform to one of the expected
     *        integer bitmasks then the style is set to PLAIN.
     * @param pSize the point size of the <code>UIFont</code>
     * @see IFont
     * @see #getAvailableFontFamilyNames
     */
    public UIFont(String pName, int pStyle, int pSize)
    {
    	super(UIFactoryManager.getFactory().createFont(pName == null ? "Default" : pName, pStyle, pSize));
    }
    
    /**
     * Internal creation of an UIFont with an given IFont.
     * @param pFont the IFont.
     */
    protected UIFont(IFont pFont)
    {
    	super(pFont);
    }
      
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
    {
    	return getUIResource().getName();
    }

	/**
	 * {@inheritDoc}
	 */
	public String getFamily()
    {
    	return getUIResource().getFamily();
    }

	/**
	 * {@inheritDoc}
	 */
	public String getFontName()
    {
    	return getUIResource().getFontName();
    }

	/**
	 * {@inheritDoc}
	 */
	public int getStyle()
    {
    	return getUIResource().getStyle();
    }

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
    {
    	return getUIResource().getSize();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
    {
		if (pObject instanceof IFont)
		{
			IFont font = (IFont)pObject;
			
			return getName().equals(font.getName()) && getStyle() == font.getStyle() && getSize() == font.getSize();
		}
		
    	return false;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
    {
		return getName().hashCode() + getStyle() * 13 + getSize() * 31;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
    {
	    String  strStyle;

	    switch (getStyle())
	    {
	    	case IFont.PLAIN: strStyle = "plain"; break;
	    	case IFont.BOLD: strStyle = "bold"; break;
	    	case IFont.ITALIC: strStyle = "italic"; break;
	    	default: strStyle = "bolditalic";
	    }

		return getClass().getName() + "[" + getName() + ", " + strStyle + ", " + getSize() + "]";
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Derives this font with a new style.
	 * @param pStyle the style.
	 * @return the new Font.
	 */
	public IFont deriveFont(int pStyle)
	{
		return new UIFont(getName(), pStyle, getSize());
	}
	
	/**
	 * Derives this font with a new style.
	 * @param pStyle the style.
	 * @param pSize the size.
	 * @return the new Font.
	 */
	public IFont deriveFont(int pStyle, int pSize)
	{
		return new UIFont(getName(), pStyle, pSize);
	}
	
    /**
     * Returns the default <code>UIFont</code>.
     * 
     * @return the <code>IFont</code>
     * @see IFont
     */
    public static UIFont getDefaultFont()
    {   	
    	if (defaultFont == null)
    	{
    		IFont ftLabel = UIFactoryManager.getFactory().createLabel().getFont();
    		
    		if (ftLabel == null)
    		{
    			defaultFont = new UIFont("Default", IFont.PLAIN, 12);
    		}
    		else
    		{
    			defaultFont = new UIFont("Default", IFont.PLAIN, ftLabel.getSize());
    		}
    	}
    	
    	return defaultFont;
    }
    
	/**
     * Returns an array containing the names of all font families in this
     * <code>GraphicsEnvironment</code> localized for the default locale,
     * as returned by <code>Locale.getDefault()</code>.
     * <p>
     * Typical usage would be for presentation to a user for selection of
     * a particular family name. An application can then specify this name
     * when creating a font, in conjunction with a style, such as bold or
     * italic, giving the font system flexibility in choosing its own best
     * match among multiple fonts in the same font family.
     *
     * @return an array of <code>String</code> containing font family names
     *         localized for the default locale, or a suitable alternative
     *         name if no name exists for this locale.
     * @see IFont
     * @see IFont#getFamily
     */
    public static String[] getAvailableFontFamilyNames()
    {
    	return UIFactoryManager.getFactory().getAvailableFontFamilyNames();
    }

}	// UIFont
