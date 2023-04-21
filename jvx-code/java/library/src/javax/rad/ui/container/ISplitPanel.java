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

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;

/**
 * Platform and technology independent SplitPanel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JSplitPane
 */
public interface ISplitPanel extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Vertical split indicates the <code>Component</code>s are
     * split along the y axis.  For example the two
     * <code>Component</code>s will be split one on top of the other.
     */
    public static final int SPLIT_TOP_BOTTOM = 0;

    /**
     * Horizontal split indicates the <code>Component</code>s are
     * split along the x axis.  For example the two
     * <code>Component</code>s will be split one to the left of the
     * other.
     */
    public static final int SPLIT_LEFT_RIGHT = 1;

    /**
     * The Divider is fixed to the top border during resize.
     */
    public static final int DIVIDER_TOP_LEFT = 0;

    /**
     * The Divider is fixed to the bottom border during resize.
     */
    public static final int DIVIDER_BOTTOM_RIGHT = 1;

    /**
     * The Divider is fixed to the right border during resize.
     */
    public static final int DIVIDER_RELATIVE = 2;

    /**
     * Used to add a <code>Component</code> to the left/top of the other
     * <code>Component</code>.
     */
    public static final String FIRST_COMPONENT = "FIRST_COMPONENT";

    /**
     * Used to add a <code>Component</code> to the right/bottom of the other
     * <code>Component</code>.
     */
    public static final String SECOND_COMPONENT = "SECOND_COMPONENT";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the orientation.
     * 
     * @return an integer giving the orientation
     * @see #setOrientation
     */
    public int getOrientation();

    /**
     * Sets the orientation, or how the splitter is divided. The options
     * are:<ul>
     * <li>SPLIT_TOP_BOTTOM  (above/below orientation of components)
     * <li>SPLIT_LEFT_RIGHT  (left/right orientation of components)
     * </ul>
     *
     * @param pOrientation an integer specifying the orientation
     * @throws IllegalArgumentException if orientation is not one of: {@link #SPLIT_LEFT_RIGHT} or {@link #SPLIT_TOP_BOTTOM}.
     */
    public void setOrientation(int pOrientation);
    
    /**
     * Returns the component to the left (or above) the divider.
     *
     * @return the <code>IComponent</code> displayed in that position
     */
    public IComponent getFirstComponent();

    /**
     * Sets the component to the left (or above) the divider.
     *
     * @param pComponent the <code>IComponent</code> to display in that position
     */
    public void setFirstComponent(IComponent pComponent);

    /**
     * Returns the component to the right (or below) the divider.
     *
     * @return the <code>Component</code> displayed in that position
     */
    public IComponent getSecondComponent();

    /**
     * Sets the component to the right (or below) the divider.
     *
     * @param pComponent the <code>IComponent</code> to display in that position
     */
    public void setSecondComponent(IComponent pComponent);

    /**
     * Gets the position of the divider.
     *
     * @return the position of the divider.
     */
    public int getDividerPosition();

    /**
     * Sets the position of the divider.
     *
     * @param pDividerPosition the position of the divider.
     */
    public void setDividerPosition(int pDividerPosition);
	
    /**
     * Gets the divider alignment.
     * 
     * @return the divider alignment: {@link #DIVIDER_TOP_LEFT}, 
     *         {@link #DIVIDER_BOTTOM_RIGHT}, {@link #DIVIDER_RELATIVE}
     */
    public int getDividerAlignment();

    /**
     * Sets the divider alignment.
     * 
     * @param pDividerAlignment the divider alignment: {@link #DIVIDER_TOP_LEFT}, 
     *                          {@link #DIVIDER_BOTTOM_RIGHT}, {@link #DIVIDER_RELATIVE}
     */
	public void setDividerAlignment(int pDividerAlignment);

}	// ISplitPanel
