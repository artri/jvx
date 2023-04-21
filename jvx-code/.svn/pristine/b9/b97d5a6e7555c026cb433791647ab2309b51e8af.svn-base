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
 * 28.11.2008 - [HM] - creation
 */
package javax.rad.ui.layout;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.ILayout;

/**
 * Platform and technology independent flow layout definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public interface IFlowLayout extends ILayout<Object>, 
                                     IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Constant for horizontal flow layout. */
	public static final int HORIZONTAL = 0;
	
	/** Constant for vertical vertical layout. */
	public static final int VERTICAL   = 1;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the orientation of this flow layout.
	 * It can be <code>HORIZONTAL</code> or <code>VERTICAL</code>.
	 *
	 * @return the orientation.
	 */
	public int getOrientation();

	/**
	 * Sets the orientation of this flow layout.
	 * It can be <code>HORIZONTAL</code> or <code>VERTICAL</code>.
	 *
	 * @param pOrientation the orientation.
	 */
	public void setOrientation(int pOrientation);

	/**
	 * Gets the alignment between the components. For horizontal orientation
	 * the component alignment can be ALIGN_TOP, ALIGN_CENTER, ALIGN_BOTTOM or ALIGN_STRETCH
	 * for vertical orientation ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT or ALIGN_STRETCH.
	 * Switching the orientation maps top to left and bottom to right alignment.
	 * 
	 * @return the component alignment.
	 */
	public int getComponentAlignment();

	/**
	 * Sets the alignment between the components. For horizontal orientation
	 * the component alignment can be ALIGN_TOP, ALIGN_CENTER, ALIGN_BOTTOM or ALIGN_STRETCH
	 * for vertical orientation ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT or ALIGN_STRETCH.
	 * Switching the orientation maps top to left and bottom to right alignment.
	 * 
	 * @param pComponentAlignment the component alignment.
	 */
	public void setComponentAlignment(int pComponentAlignment);

	/**
	 * Gets the current state of the automatic wrap mode.
	 * 
	 * @return <code>true</code> if the automatic wrap mode is enabled; otherwise <code>false</code>
	 */
	public boolean isAutoWrap();

	/**
	 * Sets the automatic wrap mode. This means that the layout acts like
	 * the {@link java.awt.FlowLayout}.
	 * 
	 * @param pAutoWrap <code>true</code> to enabled the auto wrap mode, <code>false</code> otherwise
	 */
	public void setAutoWrap(boolean pAutoWrap);

}	// IFlowLayout
