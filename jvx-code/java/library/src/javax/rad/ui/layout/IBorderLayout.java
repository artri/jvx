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
package javax.rad.ui.layout;

import javax.rad.ui.ILayout;

/**
 * Platform and technology independent BorderLayout definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.BorderLayout
 */
public interface IBorderLayout extends ILayout<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/* 
	 * The constants come from BorderLayout, and because of that we avoid translation
	 * of the values
	 */	
	
	/** The north layout constraint (top of container). */
    public static final String NORTH  = "North";

    /** The south layout constraint (bottom of container). */
    public static final String SOUTH  = "South";

    /** The east layout constraint (right side of container). */
    public static final String EAST   = "East";

    /** The west layout constraint (left side of container). */
    public static final String WEST   = "West";

    /** The center layout constraint (middle of container). */
    public static final String CENTER = "Center";
    
}	// IBorderLayout
