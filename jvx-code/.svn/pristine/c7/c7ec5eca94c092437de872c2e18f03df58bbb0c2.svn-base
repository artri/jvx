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
 * Alignment constants definitions.
 * 
 * @author Martin Handsteiner
 */
public interface IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Left Align for this element. */
	public static final int	ALIGN_LEFT		= 0;

	/** Center Align for this element. This is used for horizontal and vertical alignment. */
	public static final int	ALIGN_CENTER	= 1;

	/** Right Align for this element. */
	public static final int	ALIGN_RIGHT		= 2;

	/** Top Align for this element. */
	public static final int	ALIGN_TOP		= 0;

	/** Bottom Align for this element. */
	public static final int	ALIGN_BOTTOM	= 2;

	/** Stretch Align for this element. This is used for horizontal and vertical alignment. 
	 *  If stretching is not possible this constant should have the same result as ALIGN_CENTER */
	public static final int	ALIGN_STRETCH	= 3;

	/** Default align is for components, that have the possibility to change align independently. 
	 *  DEFAULT means, what ever the component want, else use the direct setting. */
	public static final int	ALIGN_DEFAULT	= -1;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the alignment along the x axis.  This specifies how
	 * the component would like to be aligned relative to other
	 * components. Possible values are defined in AlignmentConstants.
	 * 
	 * @return the horizontal alignment
	 */
	public int getHorizontalAlignment();

	/**
	 * Sets the horizontal alignment.
	 * Possible values are defined in AlignmentConstants.
	 *
	 * @param pHorizontalAlignment  the new vertical alignment
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment);

	/**
	 * Returns the alignment along the y axis.  This specifies how
	 * the component would like to be aligned relative to other
	 * components.  Possible values are defined in AlignmentConstants.
	 * 
	 * @return the vertical alignment
	 */
	public int getVerticalAlignment();

	/**
	 * Sets the vertical alignment.
	 * Possible values are defined in AlignmentConstants.
	 *
	 * @param pVerticalAlignment  the new vertical alignment
	 */
	public void setVerticalAlignment(int pVerticalAlignment);
	
}	// IAlignmentConstants
