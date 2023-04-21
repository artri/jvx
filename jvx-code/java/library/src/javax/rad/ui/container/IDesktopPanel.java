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
 * 12.10.2008 - [JR] - setTabMode defined
 * 10.12.2008 - [JR] - defined navigation key methods
 * 18.12.2008 - [JR] - removed navigation key methods
 * 20.02.2009 - [JR] - added navigation key method 
 */
package javax.rad.ui.container;

import javax.rad.ui.IContainer;

/**
 * Platform and technology independent DesktopPanel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JDesktopPane
 */
public interface IDesktopPanel extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the display mode of the desktop to show tabs instead of frames.
	 * 
	 * @param pTabMode <code>true</code> to frames as tabs; <code>false</code> to frames
	 */
	public void setTabMode(boolean pTabMode);
	
	/**
	 * Gets the display mode of the desktop.
	 * 
	 * @return <code>true</code> if the desktop shows tabs instead of frames, or
	 *         <code>false</code> if the desktop shows internal frames
	 */
	public boolean isTabMode();
	
	/**
	 * En- or disables the frame navigation with the keyboard.
	 * 
	 * @param pEnabled <code>true</code> to enable the navigation with the keyboard, otherwise <code>false</code>
	 */
	public void setNavigationKeysEnabled(boolean pEnabled);
	
	/**
	 * Determines whether the navigation with the keyboard is enabled.
	 * 
	 * @return <code>true</code> if the keyboard navigation is enabled, otherwise <code>false</code>
	 */
	public boolean isNavigationKeysEnabled();
	
}	// IDesktopPanel
