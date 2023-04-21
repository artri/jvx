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
 * 01.10.2008 - [HM] - creation
 */
package javax.rad.ui;

/**
 * Platform and technology independent Font definition.
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Font
 */
public interface IFont extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	 * Constants to be used for logical font family names.
	 */

	/**
	 * A String constant for the canonical family name of the
	 * logical font "Dialog". It is useful in Font construction
	 * to provide compile-time verification of the name.
	 */
	public static final String	DIALOG			= "Dialog";

	/**
	 * A String constant for the canonical family name of the
	 * logical font "DialogInput". It is useful in Font construction
	 * to provide compile-time verification of the name.
	 */
	public static final String	DIALOG_INPUT	= "DialogInput";

	/**
	 * A String constant for the canonical family name of the
	 * logical font "SansSerif". It is useful in Font construction
	 * to provide compile-time verification of the name.
	 */
	public static final String	SANS_SERIF		= "SansSerif";

	/**
	 * A String constant for the canonical family name of the
	 * logical font "Serif". It is useful in Font construction
	 * to provide compile-time verification of the name.
	 */
	public static final String	SERIF			= "Serif";

	/**
	 * A String constant for the canonical family name of the
	 * logical font "Monospaced". It is useful in Font construction
	 * to provide compile-time verification of the name.
	 */
	public static final String	MONOSPACED		= "Monospaced";

	/*
	 * Constants to be used for styles. Can be combined to mix
	 * styles.
	 */

	/** The plain style constant.*/
	public static final int		PLAIN			= 0;

	/**
	 * The bold style constant.  This can be combined with the other style
	 * constants (except PLAIN) for mixed styles.
	 */
	public static final int		BOLD			= 1;

	/**
	 * The italicized style constant.  This can be combined with the other
	 * style constants (except PLAIN) for mixed styles.
	 */
	public static final int		ITALIC			= 2;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the logical name of this <code>IFont</code>.
	 * Use <code>getFamily</code> to get the family name of the font.
	 * Use <code>getFontName</code> to get the font face name of the font.
	 * 
	 * @return a <code>String</code> representing the logical name of
	 *         this <code>IFont</code>.
	 * @see #getFamily
	 * @see #getFontName
	 */
	public String getName();

	/**
	 * Returns the family name of this <code>IFont</code>.  
	 * 
	 * <p>The family name of a font is font specific. Two fonts such as 
	 * Helvetica Italic and Helvetica Bold have the same family name, 
	 * <i>Helvetica</i>, whereas their font face names are 
	 * <i>Helvetica Bold</i> and <i>Helvetica Italic</i>. The list of 
	 * available family names may be obtained by using the 
	 * {@link java.awt.GraphicsEnvironment#getAvailableFontFamilyNames()} method.
	 * 
	 * <p>Use <code>getName</code> to get the logical name of the font.
	 * Use <code>getFontName</code> to get the font face name of the font.
	 * 
	 * @return a <code>String</code> that is the family name of this
	 *         <code>IFont</code>.
	 * 
	 * @see #getName
	 * @see #getFontName
	 */
	public String getFamily();

	/**
	 * Returns the font face name of this <code>IFont</code>. For example,
	 * Helvetica Bold could be returned as a font face name.
	 * Use <code>getFamily</code> to get the family name of the font.
	 * Use <code>getName</code> to get the logical name of the font.
	 * 
	 * @return a <code>String</code> representing the font face name of 
	 *         this <code>IFont</code>.
	 * @see #getFamily
	 * @see #getName
	 */
	public String getFontName();

	/**
	 * Returns the style of this <code>IFont</code>. The style can be
	 * PLAIN, BOLD, ITALIC, or BOLD+ITALIC.
	 * 
	 * @return the style of this <code>IFont</code>
	 */
	public int getStyle();

	/**
	 * Returns the point size of this <code>IFont</code>, rounded to
	 * an integer.
	 * Most users are familiar with the idea of using <i>point size</i> to
	 * specify the size of glyphs in a font. This point size defines a
	 * measurement between the baseline of one line to the baseline of the
	 * following line in a single spaced text document. The point size is
	 * based on <i>typographic points</i>, approximately 1/72 of an inch.
	 * <p>
	 * The Java(tm)2D API adopts the convention that one point is
	 * equivalent to one unit in user coordinates.  When using a
	 * normalized transform for converting user space coordinates to
	 * device space coordinates 72 user
	 * space units equal 1 inch in device space.  In this case one point
	 * is 1/72 of an inch.
	 * 
	 * @return the point size of this <code>IFont</code> in 1/72 of an 
	 *         inch units.
	 */
	public int getSize();

}	// IFont
