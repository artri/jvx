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
 * 21.08.2009 - [JR] - movable support
 */
package javax.rad.ui.container;

import javax.rad.ui.IContainer;
import javax.rad.ui.IInsets;

/**
 * Platform and technology independent TabSetPanel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JToolBar
 */
public interface IToolBar extends IContainer
{

    /** Horizontal orientation. */
    public static final int HORIZONTAL = 0;
    
    /** Vertical orientation. */
    public static final int VERTICAL   = 1; 
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the current orientation of the tool bar.  The value is either
     * <code>HORIZONTAL</code> or <code>VERTICAL</code>.
     *
     * @return an integer representing the current orientation -- either
     *		<code>HORIZONTAL</code> or <code>VERTICAL</code>
     * @see #setOrientation
     */
    public int getOrientation();

    /**
     * Sets the orientation of the tool bar.  The orientation must have
     * either the value <code>HORIZONTAL</code> or <code>VERTICAL</code>.
     * If <code>orientation</code> is
     * an invalid value, an exception will be thrown.
     *
     * @param pOrientation  the new orientation -- either <code>HORIZONTAL</code> or <code>VERTICAL</code>
     * @exception IllegalArgumentException if orientation is neither
     *		<code>HORIZONTAL</code> nor <code>VERTICAL</code>
     * @see #getOrientation
     */
    public void setOrientation(int pOrientation);
	
    /**
     * Returns the margin between the tool bar's border and
     * its buttons.
     *
     * @return an <code>Insets</code> object containing the margin values
     */
    public IInsets getMargins();

    /**
     * Sets the margin between the tool bar's border and
     * its buttons. Setting to <code>null</code> causes the tool bar to
     * use the default margins. The tool bar's default <code>Border</code>
     * object uses this value to create the proper margin.
     * However, if a non-default border is set on the tool bar,
     * it is that <code>Border</code> object's responsibility to create the
     * appropriate margin space (otherwise this property will
     * effectively be ignored).
     *
     * @param pMargin an <code>Insets</code> object that defines the space
     * 	between the border and the buttons
     */
    public void setMargins(IInsets pMargin);
	
    /**
     * Sets whether the toolbar is movable. Typically, a movable tool bar can be
     * dragged into a different position within the same container or out into 
     * its own window.
     * 
     * @param pMovable <code>true</code> if the toolbar is movable, <code>false</code> otherwise
     */
    public void setMovable(boolean pMovable);
    
    /**
     * Returns whether the toolbar is movable.
     * 
     * @return <code>true</code> if the toolbar is movable
     */
    public boolean isMovable();
	
}	// IToolBar
