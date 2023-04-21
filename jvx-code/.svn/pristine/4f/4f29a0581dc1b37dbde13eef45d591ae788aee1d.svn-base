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
 * 31.03.2009 - [JR] - creation
 * 23.07.2009 - [JR] - extended from IPanel
 * 11.10.2009 - [JR] - extended from IContainer (otherwise all subclasses have IPanel methods!)
 */
package javax.rad.ui.container;

import javax.rad.ui.IContainer;

/**
 * Platform and technology independent toolbar panel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * A root panel is a special container with toolbar areas.
 * 
 * @author René Jahn
 */
public interface IToolBarPanel extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the top toolbar area. */
	public static final int AREA_TOP = 0;
	
	/** the left toolbar area. */
	public static final int AREA_LEFT = 1;

	/** the bottom toolbar area. */
	public static final int AREA_BOTTOM = 2;

	/** the right toolbar area. */
	public static final int AREA_RIGHT = 3;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a toolbar to this panel as last component.
	 * 
	 * @param pToolBar the toolbar to be added
	 */
	public void addToolBar(IToolBar pToolBar);

	/**
	 * Adds a toolbar to this panel at a specified index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the index for the toolbar              
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex);
	
	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pIndex the index
	 */
	public void removeToolBar(int pIndex);
	
	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pToolBar the toolbar
	 */
	public void removeToolBar(IToolBar pToolBar);
	
	/**
	 * Removes all toolbars from this panel.
	 */
	public void removeAllToolBars();
	
	/**
	 * Gets the number of <code>IToolBar</code>s in this panel.
	 * 
	 * @return the number of toolbars
	 */
	public int getToolBarCount();
	
	/**
	 * Gets the {@link IToolBar} from a specific index.
	 *  
	 * @param pIndex the index
	 * @return the toolbar at <code>pIndex</code>
	 */
	public IToolBar getToolBar(int pIndex);
	
	/**
	 * Gets the n<sup>th</sup> position of an <code>IToolBar</code> in this panel.
	 * 
	 * @param pToolBar the <code>IToolBar</code> to search
	 * @return the n<sup>th</sup> position of <code>pToolBar</code> in this panel or
	 *         <code>-1</code> if <code>pToolBar</code> is not added
	 */
	public int indexOfToolBar(IToolBar pToolBar);
	
	/**
	 * Sets the display area where the toolbars will be added.
	 * 
	 * @param pArea an area constant {@link #AREA_TOP}, {@link #AREA_LEFT}, 
	 *              {@link #AREA_BOTTOM}, {@link #AREA_RIGHT}
	 */
	public void setToolBarArea(int pArea);

	/**
	 * Gets the area where the toolbar(s) are added.
	 * 
	 * @return an area constant {@link #AREA_TOP}, {@link #AREA_LEFT}, 
	 *         {@link #AREA_BOTTOM}, {@link #AREA_RIGHT}
	 */
	public int getToolBarArea();
	
}	// IToolBarPanel
