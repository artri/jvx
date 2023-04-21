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
package javax.rad.ui.container;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.IMenuBar;

/**
 * Platform and technology independent Frame definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Frame
 * @see	javax.swing.JFrame
 */
public interface IFrame extends IWindow,
                                IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Frame is in the "normal" state.  This symbolic constant names a
     * frame state with all state bits cleared.
     * 
     * @see #setState(int)
     * @see #getState
     */
    public static final int NORMAL = 0;

    /**
     * This state bit indicates that frame is iconified.
     * 
     * @see #setState(int)
     * @see #getState
     */
    public static final int ICONIFIED = 1;

    /**
     * This state bit indicates that frame is maximized in the
     * horizontal direction.
     * 
     * @see #setState(int)
     * @see #getState
     */
    public static final int MAXIMIZED_HORIZ = 2;

    /**
     * This state bit indicates that frame is maximized in the
     * vertical direction.
     * 
     * @see #setState(int)
     * @see #getState
     */
    public static final int MAXIMIZED_VERT = 4;

    /**
     * This state bit mask indicates that frame is fully maximized
     * (that is both horizontally and vertically).  It is just a
     * convenience alias for
     * <code>MAXIMIZED_VERT&nbsp;|&nbsp;MAXIMIZED_HORIZ</code>.
     *
     * <p>Note that the correct test for frame being fully maximized is
     * <pre>
     *     (state &amp; IFrame.MAXIMIZED_BOTH) == IFrame.MAXIMIZED_BOTH
     * </pre>
     *
     * <p>To test is frame is maximized in <em>some</em> direction use
     * <pre>
     *     (state &amp; IFrame.MAXIMIZED_BOTH) != 0
     * </pre>
     * 
     * @see #setState(int)
     * @see #getState
     */
    public static final int MAXIMIZED_BOTH = MAXIMIZED_VERT | MAXIMIZED_HORIZ;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the title of the frame. The title is displayed in the
	 * frame's border.
	 * 
	 * @return the title of this frame, or an empty string ("")
	 *         if this frame doesn't have a title.
	 * @see #setTitle(String)
	 */
	public String getTitle();

	/**
	 * Sets the title for this frame to the specified string.
	 * 
	 * @param pTitle the title to be displayed in the frame's border.
	 *        A <code>null</code> value is treated as an empty string, "".
	 * @see #getTitle
	 */
	public void setTitle(String pTitle);

	/**
	 * Returns the image to be displayed as the icon for this frame. 
	 * 
	 * @return the icon image for this frame, or <code>null</code> 
	 *         if this frame doesn't have an icon image.
	 * @see #setIconImage(IImage)
	 */
	public IImage getIconImage();

	/**
	 * Sets the image to be displayed as the icon for this window.
	 * 
	 * @param pIconImage the icon image to be displayed.
	 *        If this parameter is <code>null</code> then the
	 *        icon image is set to the default image, which may vary
	 *        with platform.            
	 * @see #getIconImage
	 */
	public void setIconImage(IImage pIconImage);

    /**
     * Gets the state of this frame.
     * 
     * @return either <code>IFrame.NORMAL</code>, <code>IFrame.ICONIFIED</code>,
     *                <code>IFrame.MAXIMIZED_HORIZ</code>, <code>IFrame.MAXIMIZED_HORIZ</code>, <code>IFrame.MAXIMIZED_BOTH</code>
     * @see     #setState(int)
     */
    public int getState();

    /**
     * Sets the state of this frame.
     * 
     * @param pState either <code>IFrame.NORMAL</code>, <code>IFrame.ICONIFIED</code>,
     *                      <code>IFrame.MAXIMIZED_HORIZ</code>, <code>IFrame.MAXIMIZED_HORIZ</code>, <code>IFrame.MAXIMIZED_BOTH</code>
     * @see #getState
     * @see #setState(int)
     */
    public void setState(int pState);

    /**
     * Indicates whether this frame is resizable by the user.  
     * By default, all frames are initially resizable.
     *  
     * @return    <code>true</code> if the user can resize this frame; 
     *                        <code>false</code> otherwise.
     * @see       #setResizable(boolean)
     */
    public boolean isResizable();

    /**
     * Sets whether this frame is resizable by the user.
     *   
     * @param    pResizable  <code>true</code> if this frame is resizable; 
     *                       <code>false</code> otherwise.
     * @see      #isResizable
     */
    public void setResizable(boolean pResizable);

    /**
     * Sets the menubar for this frame.
     * 
     * @param pMenuBar the menubar being placed in the frame
     * @see #getMenuBar
     */
    public void setMenuBar(IMenuBar pMenuBar);

    /**
     * Returns the menubar set on this frame.
     * 
     * @return the menubar for this frame
     * @see #setMenuBar
     */
    public IMenuBar getMenuBar(); 
    
}	// IFrame
