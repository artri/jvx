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
 * Platform and technology independent Color definition.
 * 
 * @author Martin Handsteiner
 * @see java.awt.Color
 * @see java.awt.SystemColor
 */
public interface IColor extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The constant for control background color. */
	public static final String	CONTROL_BACKGROUND						= "IControl.background";
	/** The constant for control alternate background color. */
	public static final String	CONTROL_ALTERNATE_BACKGROUND			= "IControl.alternateBackground";
	/** The constant for control foreground color. */
	public static final String	CONTROL_FOREGROUND						= "IControl.foreground";
	/** The constant for control active selection background color. */
	public static final String	CONTROL_ACTIVE_SELECTION_BACKGROUND		= "IControl.activeSelectionBackground";
	/** The constant for control active selection foreground color. */
	public static final String	CONTROL_ACTIVE_SELECTION_FOREGROUND		= "IControl.activeSelectionForeground";
	/** The constant for control inactive selection background color. */
	public static final String	CONTROL_INACTIVE_SELECTION_BACKGROUND	= "IControl.inactiveSelectionBackground";
	/** The constant for control inactive selection foreground color. */
	public static final String	CONTROL_INACTIVE_SELECTION_FOREGROUND	= "IControl.inactiveSelectionForeground";
	/** The constant for control mandatory background color. */
	public static final String	CONTROL_MANDATORY_BACKGROUND			= "IControl.mandatoryBackground";
	/** The constant for control read only background color. */
	public static final String	CONTROL_READ_ONLY_BACKGROUND			= "IControl.readOnlyBackground";
	/** The constant for control read only background color. */
	public static final String	INVALID_EDITOR_BACKGROUND				= "IControl.invalidEditorBackground";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the red component in the range 0-255 in the default sRGB space.
	 * 
	 * @return the red component.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 */
	public int getRed();

	/**
	 * Returns the green component in the range 0-255 in the default sRGB space.
	 * 
	 * @return the green component.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 */
	public int getGreen();

	/**
	 * Returns the blue component in the range 0-255 in the default sRGB space.
	 * 
	 * @return the blue component.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 */
	public int getBlue();

	/**
	 * Returns the alpha component in the range 0-255.
	 * 
	 * @return the alpha component.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 */
	public int getAlpha();

	/**
	 * Returns the red, green, blue and alpha component as one integer.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
	 * 
	 * @return the red, green, blue and alpha component as one integer.
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 */
	public int getRGBA();

}	// IColor
