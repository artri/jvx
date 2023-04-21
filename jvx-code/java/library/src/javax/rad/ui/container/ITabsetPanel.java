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
 * 22.10.2008 - [JR] - closable support
 *                   - drag support
 * 04.08.2009 - [JR] - set/isNavigationKeysEnabled defined
 * 11.08.2009 - [JR] - eventTabClosed, eventTabMoved defined
 * 25.07.2013 - [JR] - #732: eventTabActivated, eventTabDeactivated defined                    
 */
package javax.rad.ui.container;

import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;

/**
 * Platform and technology independent TabSetPanel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTabbedPane
 */
public interface ITabsetPanel extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/* 
	 * The constant values for PLACEMENT and TAB_LAYOUT comes from SwingConstants and
	 * JTabbedPane. Thus a translation is not necessary.
	 */
	
	/** the constant value for top tab placement. */
	public static final int PLACEMENT_TOP = 1;
	
	/** the constant value for left tab placement. */
	public static final int PLACEMENT_LEFT = 2;

	/** the constant value for bottom tab placement. */
	public static final int PLACEMENT_BOTTOM = 3;

	/** the constant value for right tab placement. */
	public static final int PLACEMENT_RIGHT = 4;
	
   /**
    * The tab layout policy for wrapping tabs in multiple runs when all
    * tabs will not fit within a single run.
    */
    public static final int TAB_LAYOUT_WRAP = 0;

   /**
    * Tab layout policy for providing a subset of available tabs when all
    * the tabs will not fit within a single run.  If all the tabs do
    * not fit within a single run the look and feel will provide a way
    * to navigate to hidden tabs.
    */
    public static final int TAB_LAYOUT_SCROLL = 1;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the tab placement.
	 * 
	 * @param pPlacement one of the following values: {@link #PLACEMENT_TOP}, {@link #PLACEMENT_LEFT}, 
	 *                   {@link #PLACEMENT_RIGHT}, {@link #PLACEMENT_BOTTOM} 
	 */
	public void setTabPlacement(int pPlacement);
	
	/**
	 * Gets the tab placement.
	 * 
	 * @return one of the following values: {@link #PLACEMENT_TOP}, {@link #PLACEMENT_LEFT},
	 *         {@link #PLACEMENT_RIGHT}, {@link #PLACEMENT_BOTTOM}
	 */
	public int getTabPlacement();
	
	/**
	 * Sets a tab en- or disabled.
	 * 
	 * @param pTabPosition the position of the tab
	 * @param pEnabled <code>true</code> to enable and <code>false</code> to disable the tab
	 */
	public void setEnabledAt(int pTabPosition, boolean pEnabled);
	
	/**
	 * Returns if a tab is en- or disabled.
	 * 
	 * @param pTabPosition the position of the tab
	 * @return <code>true</code> if the tab is enabled or <code>false</code> if the tab is disabled
	 */
	public boolean isEnabledAt(int pTabPosition);
	
    /**
     * Sets the policy which the tabbedpane will use in laying out the tabs
     * when all the tabs will not fit within a single run.       
     * Possible values are:
     * <ul>
     * <li><code>ITabsetPanel.TAB_LAYOUT_WRAP</code>
     * <li><code>ITabsetPanel.TAB_LAYOUT_SCROLL</code>
     * </ul>
     * 
     * The default value, if not set by the UI, is <code>ITabsetPanel.TAB_LAYOUT_WRAP</code>.
     *
     * @param pLayoutPolicy the policy used to layout the tabs
     * @exception IllegalArgumentException if layoutPolicy value isn't one of the above valid values
     * @see #getTabLayoutPolicy
     */
	public void setTabLayoutPolicy(int pLayoutPolicy);
	
    /**
     * Returns the policy used by the tabbedpane to layout the tabs when all the
     * tabs will not fit within a single run.
     * 
     * @return the tab layout policy
     * @see #setTabLayoutPolicy
     */
	public int getTabLayoutPolicy();
	
    /**
     * Sets the selected index for this tabbedpane. The index must be
     * a valid tab index or -1, which indicates that no tab should be selected
     * (can also be used when there are no tabs in the tabbedpane).  If a -1
     * value is specified when the tabbedpane contains one or more tabs, then
     * the results will be implementation defined.
     *
     * @param pIndex the index to be selected
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (index &lt; -1 || index &gt;= tab count)
     *
     * @see #getSelectedIndex
     */
	public void setSelectedIndex(int pIndex);
	
    /**
     * Returns the currently selected index for this tabbedpane.
     * Returns -1 if there is no currently selected tab.
     *
     * @return the index of the selected tab
     * @see #setSelectedIndex
     */   
	public int getSelectedIndex();

    /**
     * Sets the icon at <code>pIndex</code> to <code>pImage</code> which can be
     * <code>null</code>. This does not set disabled icon at <code>icon</code>.
     * If the new Icon is different than the current Icon and disabled icon
     * is not explicitly set, the LookAndFeel will be asked to generate a disabled
     * Icon. To explicitly set disabled icon, use <code>setDisableIconAt()</code>. 
     * An internal exception is raised if there is no tab at that index. 
     *
     * @param pIndex the tab index where the icon should be set 
     * @param pImage the icon to be displayed in the tab
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (pIndex &lt; 0 || pIndex &gt;= tab count)
     *
     * @see #getIconAt
     */
	public void setIconAt(int pIndex, IImage pImage);
	
    /**
     * Returns the tab icon at <code>pIndex</code>.
     *
     * @param pIndex the index of the item being queried
     * @return the icon at <code>pIndex</code>
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (pIndex &lt; 0 || pIndex &gt;= tab count)
     *
     * @see #setIconAt
     */	
	public IImage getIconAt(int pIndex);
	
	/**
	 * Sets a tab closable.
	 * 
	 * @param pTabPosition the position of the tab
	 * @param pClosable <code>true</code> to set closable and <code>false</code> to be not closable
	 */
	public void setClosableAt(int pTabPosition, boolean pClosable);
	
	/**
	 * Returns whether a tab is closable.
	 * 
	 * @param pTabPosition the position of the tab
	 * @return <code>true</code> if the tab is closable or <code>false</code> if the tab is not closable
	 */
	public boolean isClosableAt(int pTabPosition);
	
	/**
	 * Sets a tabs draggable.
	 * 
	 * @param pDraggable <code>true</code> to set the tabs draggable and <code>false</code> prevent.
	 */
	public void setDraggable(boolean pDraggable);
	
	/**
	 * Returns whether the tabs are draggable.
	 * 
	 * @return <code>true</code> if the tabs are draggable or <code>false</code> if the they are not draggable.
	 */
	public boolean isDraggable();
	
    /**
     * Sets the text at <code>pIndex</code> to <code>pText</code> which
     * can be <code>null</code>. 
     *
     * @param pIndex the tab index where the text should be set 
     * @param pText the text to be displayed in the tab
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (pIndex &lt; 0 || pIndex &gt;= tab count)
     * @see #getTextAt(int)
     */
	public void setTextAt(int pIndex, String pText);
	
    /**
     * Returns the tab text at <code>pIndex</code>.
     *
     * @param pIndex the index of the tab
     * @return the text at <code>pIndex</code>
     * @exception IndexOutOfBoundsException if index is out of range 
     *            (pIndex &lt; 0 || pIndex &gt;= tab count)
     * @see #setTextAt(int, String)
     */
	public String getTextAt(int pIndex); 
	
	/**
	 * En- or disables the tab navigation with the keyboard.
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
	
    /**
     * The TabsetHandler for the closed event.
     * 
     * The TabClosed event occurs whenever a tab has been closed by the user.
     * 
     * @return the TabsetHandler for the closed event.
     */
	public TabsetHandler<ITabClosedListener> eventTabClosed();	

    /**
     * The TabsetHandler for the moved event.
     * 
     * The TabMoved event occurs always after a tab has been dragged.
     * 
     * @return the TabsetHandler for the moved event.
     */
	public TabsetHandler<ITabMovedListener> eventTabMoved();
	
    /**
     * The TabsetHandler for the activated event.
     * 
     * The TabActivated event occurs whenever a tab has been selected and became active.
     * 
     * @return the TabsetHandler for the activated event.
     */
	public TabsetHandler<ITabActivatedListener> eventTabActivated();

    /**
     * The TabsetHandler for the activated event.
     * 
     * The TabDeactivated event occurs whenever a tab is not the selected/active tab any longer.
     * 
     * @return the TabsetHandler for the deactivated event.
     */
	public TabsetHandler<ITabDeactivatedListener> eventTabDeactivated();
	
}	// ITabsetPanel
