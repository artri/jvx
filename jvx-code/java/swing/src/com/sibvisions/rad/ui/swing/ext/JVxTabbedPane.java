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
 * 10.10.2008 - [JR] - creation
 * 14.10.2008 - [JR] - processMouseEvent: don't remove listeners -> it's enough to suppress super call
 * 07.11.2008 - [JR] - used TabEvent for TabListener
 * 08.12.2008 - [JR] - insertTab: show tab in SCROLL Layout
 * 10.12.2008 - [JR] - showTab implemented to show the tab when inserted and selected [BUGFIX]
 * 04.02.2009 - [JR] - showTab: called to often, because iShowTab was not handled correct (bugfix)
 *                   - showTab: don't translate X and Y but invalidate the view instead of the tab (bugfix)
 * 21.09.2009 - [JR] - processMouseEvent: handled fireCloseTab exceptions correctly (unset the close selected state)
 * 05.08.2009 - [JR] - implemented tab navigation with Ctrl + PageUp/Down
 * 11.08.2009 - [JR] - support for disabled icons    
 *                   - moveTab events supported
 * 12.10.2009 - [JR] - updateTabIcon: null text (use "minwidth") [BUGFIX]  
 * 13.12.2013 - [JR] - #893: removeTabListener fixed 
 * 18.08.2016 - [JR] - #1670: removed showTab from insertTab   
 * 28.02.2020 - [JR] - #2213: fixed tab rendering
 * 09.09.2020 - [JR] - #2421: getPossibleIndexAfterRemove implemented  
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.rad.util.ExceptionHandler;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.UIResource;

import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.ext.event.ITabListener;
import com.sibvisions.rad.ui.swing.ext.event.TabEvent;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.ImageUtil;

/**
 * The <code>JVxTabbedPane</code> extends the {@link JTabbedPane} and allows special
 * tab options, like closing.
 * 
 * @author René Jahn
 */
public class JVxTabbedPane extends JTabbedPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the close icon for closable tabs. */
	private static Image icoClose;
	
	/** the close icon for the selected tabs. */
	private static Image icoCloseSelect;
	
	/** the close icon when the mouse is over the close icon position. */
	private static Image icoCloseOver;
	
	/** the close icon when the close icon is pressed. */
	private static Image icoClosePress;
	
	
	/** the tab where the mouse pressed event was fired. */
	private int iPressed = -1;
	
	/** the dragged tab index. */
	private int iDragged = -1;
	
	/** the tab with selected close icon. */
	private int iClose = -1; 

	/** the cache for invoke later tab showing. */
	private volatile int iShowTab = -1;
	
	/** the cache for tab title and icon. */
	private ArrayUtil<TabInfo> auTabInfo = null; 
	
	/** the registered tab listeners. */
	private ArrayUtil<ITabListener> auTabListener = null;
	
	/** the viewport for a scroll tab layout. */ 
	private JViewport vpScroll = null;
	
	/** mark to ignore the automatic selection of tabs. */
	private boolean bIgnoreSelection = false;
	
	/** mark to use draggable tabs. */
	private boolean bDraggable = false;
	
	/** this flag indicates if it's allowed to navigate between the tabs with the keyboard. */
	private boolean bNavigationKeyEnabled = false;
	
	/** whether default settings should be overwritten. */
	private boolean bOverrideDefaults;

	/** ignore pref size calculation. */
	private boolean ignorePrefSize = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>JVxTabbedPane</code> with default
	 * settings.
	 */
	public JVxTabbedPane()
	{
		this(true);
	}
	
	/**
	 * Creates a new instanceof <code>JVxTabbedPane</code> with optional default
	 * settings.
	 * 
	 * @param pOverrideDefaults <code>true</code> to over-write defaults, <code>false</code> to keep defaults
	 */
	protected JVxTabbedPane(boolean pOverrideDefaults)
	{
		bOverrideDefaults = pOverrideDefaults;

		initIcons();
		
		if (bOverrideDefaults)
		{
			setBackground(null);
		}
	}
	
	/**
	 * Initializes the default icons.
	 */
	private void initIcons()
	{
		if (icoClose == null)
		{
			icoClose       = JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/tabclose.png");
			icoCloseSelect = JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/tabclose_select.png");
			icoCloseOver   = JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/tabclose_over.png");
			icoClosePress  = JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/tabclose_press.png");
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getMinimumSize()
    {
        int compCount = getComponentCount();
        
        if (isMinimumSizeSet() || compCount == 0)
        {
            return super.getMinimumSize();
        }
        
        HashMap<Component, Dimension> prefSizes = new HashMap<Component, Dimension>();

        for (int i = 0, count = getTabCount(); i < count; i++)
        {
            Component comp = getComponentAt(i);
            if (comp.isPreferredSizeSet())
            {
                prefSizes.put(comp, comp.getPreferredSize());
            }
            comp.setPreferredSize(new Dimension());
        }
        
        invalidate();
        Dimension minSize = super.getMinimumSize();

        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0)
        {
            Component comp = getComponentAt(selectedIndex);
            comp.setPreferredSize(prefSizes.get(comp));
            Dimension ms = JVxUtil.getMinimumSize(comp);
            
            switch (getTabPlacement()) 
            {
                case LEFT:
                case RIGHT:
                    int height = getHeight();
                    minSize.height = Math.max(minSize.height, ms.height);
                    comp.setPreferredSize(new Dimension(ms.width, height <= 0 ? minSize.height : height));
                    minSize.width = super.getMinimumSize().width;
                    break;
                case TOP:
                case BOTTOM:
                default:
                    int width = getWidth();
                    minSize.width = Math.max(minSize.width, ms.width);
                    comp.setPreferredSize(new Dimension(width <= 0 ? minSize.width : width, ms.height));
                    minSize.height = super.getMinimumSize().height;
            }
        }

        for (int i = 0, count = getTabCount(); i < count; i++)
        {
            Component comp = getComponentAt(i);
            comp.setPreferredSize(prefSizes.get(comp));
        }
        
        return minSize;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize()
    {
        int compCount = getComponentCount();
        
        if (ignorePrefSize || isPreferredSizeSet() || compCount == 0)
        {
            return super.getPreferredSize();
        }
        
        ignorePrefSize = true;

        HashMap<Component, Dimension> prefSizes = new HashMap<Component, Dimension>();
        for (int i = 0, count = getTabCount(); i < count; i++)
        {
            Component comp = getComponentAt(i);
            if (comp.isPreferredSizeSet())
            {
                prefSizes.put(comp, comp.getPreferredSize());
            }
            comp.setPreferredSize(new Dimension());
        }
        
        invalidate();
        Dimension prefSize = super.getPreferredSize();
        
        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0)
        {
            Component comp = getComponentAt(selectedIndex);
            comp.setPreferredSize(prefSizes.get(comp));
            Dimension ps = JVxUtil.getPreferredSize(comp);
            
            switch (getTabPlacement()) 
            {
                case LEFT:
                case RIGHT:
                    int height = getHeight();
                    prefSize.height = Math.max(prefSize.height, ps.height);
                    comp.setPreferredSize(new Dimension(ps.width, height <= 0 ? prefSize.height : height));
                    prefSize.width = super.getPreferredSize().width;
                    break;
                case TOP:
                case BOTTOM:
                default:
                    int width = getWidth();
                    prefSize.width = Math.max(prefSize.width, ps.width);
                    comp.setPreferredSize(new Dimension(width <= 0 ? prefSize.width : width, ps.height));
                    prefSize.height = super.getPreferredSize().height;
            }
        }

        for (int i = 0, count = getTabCount(); i < count; i++)
        {
            Component comp = getComponentAt(i);
            comp.setPreferredSize(prefSizes.get(comp));
        }
        
        ignorePrefSize = false;
        return prefSize;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int indexAtLocation(int pX, int pY) 
    {
        int selectedIndex = getSelectedIndex();
        
        if (selectedIndex >= 0 && getBoundsAt(selectedIndex).contains(pX, pY))
        {
            return selectedIndex;
        }
        
        return super.indexAtLocation(pX, pY);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void setUI(TabbedPaneUI pUI)
	{
		super.setUI(pUI);
		
		updateTabIcons();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
    {
        // Stop animation thread, if component is not showing anymore.
        return isShowing() && super.imageUpdate(img, infoflags, x, y, w, h);
    }
    
	/**
	 * Handles the {@link MouseEvent#MOUSE_MOVED} event and set the
	 * rollover for the close button of tabs.
	 * 
	 * @param pEvent the mouse event
	 */
	protected void processMouseMotionEvent(MouseEvent pEvent)
	{
	    int iTab = getUI().tabForCoordinate(this, pEvent.getX(), pEvent.getY());


	    unsetCloseRollover(iTab);
	    
    	//mark the close button when the mouse is over

	    if (pEvent.getID() == MouseEvent.MOUSE_MOVED)
	    {
	    	updateMouseOverClose(pEvent, iTab);
	    }
	    else if (pEvent.getID() == MouseEvent.MOUSE_DRAGGED 
	    		 && isDraggable())
	    {
	    	//more than one runs -> the user will have problems because the rows will be changed!
	    	//-> it's better do disable dragging when the tab has more than one runs
	    	if (getTabRunCount() == 1 && iTab >= 0 
	    		&& ((iPressed >= 0 && iDragged == -1 && isEnabledAt(iPressed)) || iDragged >= 0))
	    	{
		    	//dragging is not possible when the mouse is over a close button
		    	//or the close was pressed and the mouse is dragging
	    		//or the tab is disabled
		    	boolean bOverClose = getTabInfo(iTab).isCloseRollover();
		    	boolean bClosePressed;
		    	
		    	if (iPressed >= 0)
		    	{
		    		bClosePressed = getTabInfo(iPressed).isClosePressed();
		    	}
		    	else
		    	{
		    		bClosePressed = false;
		    	}
		    	
		    	if (!bOverClose && !bClosePressed)
		    	{
			    	if (iDragged == -1)
			    	{
			    		iDragged = iTab;
			    		putClientProperty("TabbedPane.dragging", Boolean.TRUE);
			    		repaint();
			    		
				    	setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			    	}
			    	
			    	if (iDragged >= 0 && iTab >= 0 && iDragged != iTab)
			    	{
			    		//get old values
			    		Component comp = getComponentAt(iDragged);
			    		TabInfo tabinfo = getTabInfo(iDragged);
			    		String sToolTip = getToolTipTextAt(iDragged);
			    		
			    		// Disable the events.
			    		ArrayUtil<ITabListener> tempTabListener = auTabListener;
			    		auTabListener = null;
			    		
			    		// Actually move the tab.
				    	removeTabAt(iDragged);
				    	insertTab(tabinfo.getTitle(), tabinfo.getIcon(), comp, sToolTip, iTab);
		
			    		setClosableAt(iTab, tabinfo.isClosable());
			    		
			    		Icon icoDisabled = tabinfo.getDisabledIcon();
			    		if (icoDisabled != null)
			    		{
			    			setDisabledIconAt(iTab, icoDisabled);
			    		}
			    		
			    		// Re-enable the events.
			    		auTabListener = tempTabListener;
			    		
			    		fireMoveTab(iDragged, iTab);
			    		
			    		// Disable the events again to suppress deactivated and
			    		// activated events which might be fired because of
			    		// the setSelectedTabIndex(int) call.
			    		auTabListener = null;
			    		
			    		iDragged = iTab;
			    		
			    		setSelectedIndex(iTab);
			    		
			    		// Re-enable the events.
			    		auTabListener = tempTabListener;
			    	}
		    	}
	    	}
	    }
		
    	super.processMouseMotionEvent(pEvent);
	}
	
	/**
	 * Handles the {@link MouseEvent#MOUSE_PRESSED}, {@link MouseEvent#MOUSE_RELEASED} and {@link MouseEvent#MOUSE_EXITED}
	 * events. The close button of tabs will be selected or marked as pressed.
	 * 
	 * @param pEvent the mouse event
	 */
	protected void processMouseEvent(MouseEvent pEvent)
	{
		int iTab = getUI().tabForCoordinate(this, pEvent.getX(), pEvent.getY());

	    int iEventID = pEvent.getID();

	    boolean bConsumed = false;
	    
	    
	    if (iEventID == MouseEvent.MOUSE_PRESSED)
	    {
	    	if (iTab >= 0)
	    	{
		    	TabInfo tabinfo = getTabInfo(iTab);
		    	
		    	iPressed = iTab;
		    	if (tabinfo.isClosable() && tabinfo.getCloseBounds() != null)
		    	{
			    	tabinfo.setClosePressed(tabinfo.isCloseRollover());
					
					repaint(getUI().getTabBounds(this, iTab));
					
					//avoid selecting, and maybe re-arange the tabs
					if (tabinfo.isCloseRollover())
					{
						bConsumed = true;
					}
		    	}
	    	}
	    	else
	    	{
	    		iPressed = -1;
	    	}
	    }
	    else if (iEventID == MouseEvent.MOUSE_RELEASED)
		{
			int iRelease = -1;
	    	
	    	//stop dragging
	    	if (iDragged >= 0)
	    	{
	    		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    		putClientProperty("TabbedPane.dragging", null);
		    	
		    	iDragged = -1;
	    	}
	    	else
	    	{
	    		//close is not possible in drag mode!
		    	if (iTab >= 0 && iPressed == iTab)
				{
		    		iRelease = iTab;
				}
				else if (iPressed >= 0)
				{
					iRelease = iPressed;
				}
	    	}
	    	
	    	if (iRelease >= 0)
	    	{
				TabInfo tabinfo = getTabInfo(iRelease);
				
				if (tabinfo != null && tabinfo.isClosable() && isEnabledAt(iRelease))
				{
	    			boolean bIntersects = tabinfo.getCloseBounds().intersects(translateX(pEvent.getX()), 
	    					                                                  translateY(pEvent.getY()), 1, 1);
			    	
			    	if (bIntersects)
			    	{
			    		try
			    		{
			    			int iLastSelected = getSelectedIndex();
			    			
			    			bIgnoreSelection = true;
			    			
			    			try
			    			{
			    				Component comBeforeclose = getComponentAt(iRelease);
			    				
					    		fireCloseTab(iRelease);
					    		
					    		//if the tab was removed e.g when the JVxDesktopPane is used and the tab-mode is active
					    		//                           then the frame will remove the tab automaticaly!
					    		if (iRelease < getTabCount() && comBeforeclose == getComponentAt(iRelease))
					    		{
					    			removeTabAt(iRelease);
					    		}
					    		
				    			//check if the mouse is over another close icon
					    		iTab = getUI().tabForCoordinate(this, pEvent.getX(), pEvent.getY());
			
					    		if (iTab >= 0)
					    		{
					    			tabinfo = getTabInfo(iTab);		    	
			
					    			if (tabinfo.isClosable() && tabinfo.getCloseBounds() != null)
					    			{
							    		//paint immediately, to recalculate the icon bounds
						    			paintImmediately(getUI().getTabBounds(this, iTab));
				
						    			bIntersects = tabinfo.getCloseBounds().intersects(translateX(pEvent.getX()), 
						    					                                          translateY(pEvent.getY()), 1, 1);
						    			
						    			tabinfo.setCloseRollover(bIntersects);
						    			tabinfo.update();
						    			
						    			if (bIntersects)
						    			{
						    				iClose = iTab;
				
						    				repaint(getUI().getTabBounds(this, iTab));
						    			}
					    			}
					    		}
			    				bIgnoreSelection = false;
			    			}
			    			catch (Exception e)
			    			{
			    				bIgnoreSelection = false;
			    				//reset the close pressed state when an exception occurs
						    	tabinfo.setClosePressed(false);
			    				
			    				throw e;
			    			}
			    			
			    			//update the selection manually because the desktopmanager
			    			//will select frame oriented (maybe called in fireCloseTab)
			    			if (iRelease < iLastSelected)
			    			{
			    				int iNewIndex = getPossibleIndexAfterRemove(iLastSelected - 1);

			    				//if the tab is behind the selection or the selected 
			    				//tab will be removed, the selection will made from
			    				//the tabbed pane itself!
			    				setSelectedIndex(iNewIndex);
			    			}
			    			
			    			showTab(getSelectedIndex());
			    		}
			    		catch (Exception e)
			    		{
			    			e.printStackTrace();
			    			//nothing to be done!
			    		}
			    	}
			    	else
			    	{
			    		tabinfo.setCloseRollover(false);
			    		tabinfo.setClosePressed(false);
			    		
			    		repaint(getUI().getTabBounds(this, iRelease));
			    	}
				}
	    	}
	    	
	    	iPressed = -1;
		}
	    else if (iEventID == MouseEvent.MOUSE_EXITED)
	    {
	    	unsetCloseRollover(iTab);
	    }

	    if (!bConsumed)
	    {
	    	super.processMouseEvent(pEvent);
	    	
	    	if (iEventID == MouseEvent.MOUSE_RELEASED)
	    	{
	    		updateMouseOverClose(pEvent, iTab);
	    	}
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitleAt(int pIndex, String pTitle)
	{
		TabInfo tabinfo = getTabInfo(pIndex);
		tabinfo.setTitle(pTitle);

		super.setTitleAt(pIndex, "");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconAt(int pIndex, Icon pIcon)
	{
		TabInfo tabinfo = getTabInfo(pIndex);
		tabinfo.setIcon(pIcon);
		
		super.setIconAt(pIndex, tabinfo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Icon getIconAt(int pIndex)
	{
		TabInfo info = (TabInfo)super.getIconAt(pIndex);

		info.setEnabled(isEnabled() && isEnabledAt(pIndex));
		
		return info;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Icon getDisabledIconAt(int pIndex)
	{
		return getIconAt(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisabledIconAt(int pIndex, Icon pIcon)
	{
		TabInfo tabinfo = getTabInfo(pIndex);
		tabinfo.setDisabledIcon(pIcon);
		
		super.setDisabledIconAt(pIndex, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertTab(String pTitle, Icon pIcon, Component pComponent, String pTip, final int pIndex)
	{
		TabInfo tabinfo = createTabInfo(pIndex, pComponent);
		tabinfo.setTitle(pTitle);
		tabinfo.setIcon(pIcon);
		
		super.insertTab(null, tabinfo, pComponent, pTip, pIndex);

		//don't call showTab (see #1670): The developer should set the selected index, if needed
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTabAt(int pIndex)
	{
		int oldSelectedIndex = getSelectedIndex();
		
		// The newSelectedIndex is hopefully calculated correctly, anyway better than newIndex = oldIndex
		int newSelectedIndex = oldSelectedIndex;
		
		if (pIndex < oldSelectedIndex)
		{
			newSelectedIndex--;
		}
		else if (oldSelectedIndex == pIndex)
		{
			newSelectedIndex = getPossibleIndexAfterRemove(newSelectedIndex);
		}
		
		if (oldSelectedIndex >= pIndex) // Even if newSelectedIndex == oldSelectedIndex, due to remove, the tab behind is changed.
		{
			//Needed because super.removeTabAt doesn't call setSelectedIndex (it uses setSelectedIndexImpl)
			fireDeselectTab(oldSelectedIndex, newSelectedIndex); 
		}

		setCloseSelected(oldSelectedIndex, false);

		super.removeTabAt(pIndex);
		
		int iCurSelection = getSelectedIndex();
		int iNewSelection = getPossibleIndexAfterRemove(iCurSelection);
		
		if (iNewSelection != iCurSelection)
		{
			super.setSelectedIndex(iNewSelection);
		}
		
		//reset mouse infos
		auTabInfo.remove(pIndex);
		iPressed = -1;
		iClose = -1;

		//needed because super.removeTabAt invokes setSelectedIndexImpl instead of
		//setSelectedIndex
		setCloseSelected(getSelectedIndex(), true);

		if (oldSelectedIndex >= pIndex)
		{
			//Needed because super.removeTabAt doesn't call setSelectedIndex (it uses setSelectedIndexImpl)
			fireSelectTab(oldSelectedIndex, getSelectedIndex()); // The real new selected index is used instead of calculated newSelectedIndex.
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(Color pForeground)
	{
		super.setForeground(pForeground);
		
		updateTabIcons();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font pFont)
	{
		super.setFont(pFont);
		
		updateTabIcons();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabLayoutPolicy(int pTabLayoutPolicy)
	{
		super.setTabLayoutPolicy(pTabLayoutPolicy);

		if (bOverrideDefaults
			&& isBackgroundSet() && getBackground() instanceof UIResource)
		{
			setBackground(null);
		}
		//detect the viewport for close position calculation!
		if (pTabLayoutPolicy == JTabbedPane.SCROLL_TAB_LAYOUT)
		{
			Component comp;
			
			//not supported from all layouts e.g. Apple
			for (int i = 0, anz = getComponentCount(); i < anz; i++) 
			{
				comp = getComponent(i);
				
				if (comp instanceof JViewport 
					&& "TabbedPane.scrollableViewport".equals(comp.getName()))
				{
					vpScroll = (JViewport)comp;
				}
			}
		}
		else
		{
			vpScroll = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedIndex(int pIndex)
	{		
		if (!bIgnoreSelection)
		{
			int iOldSelectedIndex = getSelectedIndex();
	
			if (iOldSelectedIndex == pIndex)
			{
				return;
			}
		
			setCloseSelected(iOldSelectedIndex, false);

			fireDeselectTab(iOldSelectedIndex, pIndex);
			
			super.setSelectedIndex(pIndex);
	
			showTab(pIndex);
			
			setCloseSelected(pIndex, true);
			
			fireSelectTab(iOldSelectedIndex, pIndex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean processKeyBinding(KeyStroke pStroke, KeyEvent pEvent, int pModifier, boolean pPressed)
	{
		if (isNavigationKeysEnabled())
		{
			int iMove = 0;

			if (pEvent.isControlDown() && pEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
			{
				//backward
				iMove = -1;
			}
			else if (pEvent.isControlDown() && pEvent.getKeyCode() == KeyEvent.VK_PAGE_UP)
			{
				//forward
				iMove = 1;
			}

			//should navigate?
			if (iMove != 0)
			{
				int iIndex = getSelectedIndex();
				int iNewIndex = iIndex;
				
				do
				{
					iNewIndex += iMove;
					
					if (iNewIndex < 0)
					{
						iNewIndex = getTabCount() - 1;
					}
					else if (iNewIndex > getTabCount() - 1)
					{
						iNewIndex = 0;
					}
					
					if (isEnabledAt(iNewIndex))
					{
						setSelectedIndex(iNewIndex);
						
						return true;
					}
				}
				while (iNewIndex != iIndex);
			}
		}
		
		return super.processKeyBinding(pStroke, pEvent, pModifier, pPressed);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds an {@link ITabListener} to receive tab events from this
	 * tabbed pane. If listener <code>pListener</code> is <code>null</code>
	 * no exception is thrown and no action is performed.
	 * 
	 * @param pListener the listener implementation
	 */
	public void addTabListener(ITabListener pListener)
	{
		if (auTabListener == null)
		{
			auTabListener = new ArrayUtil<ITabListener>();
		}
		
		auTabListener.add(pListener);
	}
	
	/**
	 * Removes the specified {@link ITabListener} so that it no longer
     * receives tab events from this tabbed pane. This method performs 
     * no function, nor does it throw an exception, if the listener 
     * specified by the argument was not previously added to this component.
     * If listener <code>pListener</code> is <code>null</code>,
     * no exception is thrown and no action is performed.
     * 
	 * @param pListener the listener implementation
	 */
	public void removeTabListener(ITabListener pListener)
	{
		if (auTabListener != null)
		{
			auTabListener.remove(pListener);
			
			if (auTabListener.size() == 0)
			{
				auTabListener = null;
			}
		}
	}
	
	/**
	 * Invokes the {@link ITabListener#closeTab(TabEvent)} method from registered
	 * {@link ITabListener}.
	 * 
	 * @param pIndex the tab index
	 */
	private void fireCloseTab(int pIndex)
	{
		if (auTabListener != null)
		{
			TabEvent event = new TabEvent(this, 
                    					  pIndex,
                    					  pIndex,
                    					  AwtFactory.getMostRecentEventTime(), 
                    					  AwtFactory.getCurrentModifiers());
			try 
			{
				for (int i = 0, anz = auTabListener.size(); i < anz; i++)
				{
					auTabListener.get(i).closeTab(event);
				}
			} 
			catch (Exception pAbortException) 
			{
				ExceptionHandler.raise(pAbortException);
			}
		}
	}
	
	/**
	 * Invokes the {@link ITabListener#selectTab(TabEvent)} method from registered
	 * {@link ITabListener}.
	 * 
	 * @param pOldIndex the tab index of the previous selection
	 * @param pNewIndex the tab index of the current selection
	 */
	private void fireSelectTab(int pOldIndex, int pNewIndex)
	{
		if (auTabListener != null)
		{
			TabEvent event = new TabEvent(this, 
                    					  pOldIndex,
                    					  pNewIndex,
                    					  AwtFactory.getMostRecentEventTime(), 
                    					  AwtFactory.getCurrentModifiers());
			
			for (int i = 0, anz = auTabListener.size(); i < anz; i++)
			{
				auTabListener.get(i).selectTab(event);
			}
		}
	}
	
	/**
	 * Invokes the {@link ITabListener#deselectTab(TabEvent)} method from registered
	 * {@link ITabListener}.
	 * 
	 * @param pOldIndex the old selected tab index
	 * @param pNewIndex the new selected tab index
	 */
	private void fireDeselectTab(int pOldIndex, int pNewIndex)
	{
		if (auTabListener != null)
		{
			TabEvent event = new TabEvent(this, 
                    					  pOldIndex, 
                    					  pNewIndex,
                    					  AwtFactory.getMostRecentEventTime(), 
                    					  AwtFactory.getCurrentModifiers());
			try 
			{
				for (int i = 0, anz = auTabListener.size(); i < anz; i++)
				{
					auTabListener.get(i).deselectTab(event);
				}
			} 
			catch (Exception pAbortException) 
			{
				ExceptionHandler.raise(pAbortException);
			}
		}
	}

	/**
	 * Invokes the {@link ITabListener#moveTab(TabEvent)} method from registered
	 * {@link ITabListener}.
	 * 
	 * @param pOldIndex the old tab index
	 * @param pNewIndex the new tab index
	 */
	private void fireMoveTab(int pOldIndex, int pNewIndex)
	{
		if (auTabListener != null)
		{
			TabEvent event = new TabEvent(this, 
										  pOldIndex, 
										  pNewIndex,
                    					  AwtFactory.getMostRecentEventTime(), 
                    					  AwtFactory.getCurrentModifiers());
			
			for (int i = 0, anz = auTabListener.size(); i < anz; i++)
			{
				auTabListener.get(i).moveTab(event);
			}
		}
	}
	
	/**
	 * Creates a new tab information for an index.
	 * 
	 * @param pIndex the tab index
	 * @param pComponent the component
	 * @return the new tab information
	 */
	private TabInfo createTabInfo(int pIndex, Component pComponent)
	{
		if (pIndex < 0)
		{
			return null;
		}
		
		if (auTabInfo == null)
		{
			auTabInfo = new ArrayUtil<TabInfo>();
		}
		
		TabInfo tabinfo = new TabInfo(this);
		tabinfo.component = pComponent;
		
		auTabInfo.add(pIndex, tabinfo);
		
		return tabinfo;
	}
	
	/**
	 * Returns the additional tab information for an index.
	 * 
	 * @param pIndex the tab index
	 * @return the tab information or null if the tab index is unavailable
	 */
	private TabInfo getTabInfo(int pIndex)
	{
		if (pIndex < 0)
		{
			return null;
		}
		
		if (auTabInfo == null)
		{
			auTabInfo = new ArrayUtil<TabInfo>();
		}

		if (pIndex < auTabInfo.size())
		{
			return auTabInfo.get(pIndex);
		}
		
		return null;
	}
	
	/**
	 * Updates/Recreates all dynamic tab icons and repaints the tabbed pane.
	 */
	private void updateTabIcons()
	{
		//Update the icons when the UI changes
		if (auTabInfo != null)
		{
			for (int i = 0, anz = auTabInfo.size(); i < anz; i++)
			{
				auTabInfo.get(i).update();
			}
			
			repaint();
		}
	}
	
	/**
	 * Unsets the rollover information of the corresponding tab.
	 * 
	 * @param pIndex the tab index
	 */
	private void unsetCloseRollover(int pIndex)
	{
	    if (pIndex != iClose && iClose >= 0)
	    {
	    	//cleanup (important when the mouse was moved so fast that the close icon is selected and the next
	    	//         event fires outside the tab!)
	    	TabInfo tabinfo = getTabInfo(iClose);
	    	tabinfo.setCloseRollover(false);
	    	
			repaint(getUI().getTabBounds(this, iClose));

			iClose = -1;
	    }
	}
	
	/**
	 * Set/Unset the closable option for a tab.
	 * 
	 * @param pIndex the tab index
	 * @param pClosable <code>true</code> to enable the close option; otherwise <code>false</code>
	 */
	public void setClosableAt(int pIndex, boolean pClosable)
	{
		TabInfo tabinfo = getTabInfo(pIndex);
		
		if (tabinfo != null)
		{
			tabinfo.setClosable(pClosable);
			
			if (isShowing())
			{
				repaint(getUI().getTabBounds(this, pIndex));
			}
		}
	}
	
	/**
	 * Calculates the x position dependent of the scrollable tab layout.
	 * 
	 * @param pX the original x position
	 * @return the translated x position, when the scroll tab layout is used,
	 *         otherwise the original x position
	 */
	private int translateX(int pX)
	{
		if (vpScroll != null)
		{
			return pX - vpScroll.getLocation().x + vpScroll.getViewPosition().x;
		}
		
		return pX;
	}
	
	/**
	 * Calculates the y position dependent of the scrollable tab layout.
	 * 
	 * @param pY the original y position
	 * @return the translated y position, when the scroll tab layout is used,
	 *         otherwise the original y position
	 */
	private int translateY(int pY)
	{
		if (vpScroll != null)
		{
			return pY - vpScroll.getLocation().y + vpScroll.getViewPosition().y;
		}
		
		return pY;
	}
	
	/**
	 * Sets the close icon of a tab selected.
	 * 
	 * @param pIndex the tab index
	 * @param pSelected <code>true</code> if the close icon should be selected; otherwise <code>false</code>
	 */
	private void setCloseSelected(int pIndex, boolean pSelected)
	{
		//set NEW selection
		TabInfo tabinfo = getTabInfo(pIndex);

		if (tabinfo != null)
		{
			tabinfo.setSelected(pSelected);
		}
	}
	
	/**
	 * Updates the mouse over close information dependent of the mouse
	 * position.
	 * 
	 * @param pEvent the mouse event
	 * @param pTab the tab corresponding to the mouse event
	 */
	private void updateMouseOverClose(MouseEvent pEvent, int pTab)
	{
		if (pTab >= 0)
		{
			TabInfo tabinfo = getTabInfo(pTab);
	
	    	if (tabinfo.isClosable() && isEnabledAt(pTab) && tabinfo.getCloseBounds() != null)
	    	{
		    	boolean bIntersects = tabinfo.getCloseBounds().intersects(translateX(pEvent.getX()), 
		    			                                                  translateY(pEvent.getY()), 1, 1);

		    	tabinfo.setCloseRollover(bIntersects);
		    	
		    	//avoid synchronization and timing problems
		    	if (bIntersects)
		    	{
		    		iClose = pTab;
		    	}
				
				repaint(getUI().getTabBounds(this, pTab));
	    	}
		}
	}
	
	/**
	 * Sets the drag option for tabs. If the option is enabled, it's
	 * possible to change the position of tabs.
	 *  
	 * @param pDraggable <code>true</code> to enable the drag option, otherwise <code>false</code>
	 */
	public void setDraggable(boolean pDraggable)
	{
		bDraggable = pDraggable;
	}
	
	/**
	 * Returns the drag option of tabs.
	 * 
	 * @return <code>true</code> if it's possible to drag tabs, <code>false</code> otherwise
	 */
	public boolean isDraggable()
	{
		return bDraggable;
	}
	
	/**
	 * Ensures that a tab will be completely displayed, when the {@link JTabbedPane#SCROLL_TAB_LAYOUT}
	 * is used.
	 * 
	 * @param pIndex the tab index
	 */
	private void showTab(int pIndex)
	{
		//perform "invokeLater" only once!
		//important if switched between tab and frame view from JVxDesktopPane
		if (vpScroll != null
			&& pIndex >= 0
			&& iShowTab == -1)
		{
			SwingUtilities.invokeLater
			(
				new Runnable()
				{
					public void run()
					{
						if (iShowTab >= 0 && iShowTab < getTabCount())
						{
							Rectangle rect = JVxTabbedPane.this.getUI().getTabBounds(JVxTabbedPane.this, iShowTab);
							
							if (rect.x < 0)
							{
								//needed if the last tab is selected and the first will be seleted
								vpScroll.setViewPosition(new Point(translateX(rect.x), translateY(rect.y)));
							}
							else
							{
								vpScroll.getView().invalidate();
								vpScroll.scrollRectToVisible(new Rectangle(rect.x, rect.y, rect.width, rect.height));
							}
							
							iShowTab = -1;
						}
					}
				}
			);
		}
			
		iShowTab = pIndex;
	}
	
	/**
	 * En- or disables the tab navigation with the keyboard.
	 * 
	 * @param pEnabled <code>true</code> to enable the navigation with the keyboard, otherwise <code>false</code>
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		bNavigationKeyEnabled = pEnabled;
	}
	
	/**
	 * Determines whether the navigation with the keyboard is enabled.
	 * 
	 * @return <code>true</code> if the keyboard navigation is enabled, otherwise <code>false</code>
	 */
	public boolean isNavigationKeysEnabled()
	{
		return bNavigationKeyEnabled;
	}
	
	/**
	 * Gets the possible index after a tab was removed. This method takes care of disabled
	 * tab sheets.
	 * 
	 * @param pCurrentIndex the current "selected" index
	 * @return the possible "selected" index or <code>-1</code> if no index is possible (because no tab sheet is enabled)
	 */
	private int getPossibleIndexAfterRemove(int pCurrentIndex)
	{
		int iCurSelection = pCurrentIndex;
		
		int iTabCount = getTabCount();
		
		if (iCurSelection >= iTabCount)
		{
			iCurSelection = iTabCount - 1;
		}
		
		int iNewSelection = iCurSelection;
		
		//check previous
		while (iNewSelection >= 0 && !isEnabledAt(iNewSelection))
		{
			iNewSelection--;
		}
		
		if (iNewSelection < 0 && iCurSelection >= 0)
		{
			iNewSelection = iCurSelection;
			
			//check next
			while (iNewSelection < iTabCount && !isEnabledAt(iNewSelection))
			{
				iNewSelection++;
			}
			
			if (iNewSelection >= iTabCount)
			{
				return -1;
			}
		}
		
		return iNewSelection;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>TabInfo</code> holds the UI information for a single tab:
	 * <ul>
	 *   <li>closable</li>
	 *   <li>title</li>
	 *   <li>icon</li>
	 *   <li>...</li>
	 * </ul>
	 * 
	 * @author René Jahn
	 */
	private static final class TabInfo implements Icon
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the component for this tab. */
		private Component component;
		
		/** the reference to the tabbed pane. */
		private JVxTabbedPane tabParent;
		
		/** the title of the tab. */
		private String sTitle = null;
		
		/** the icon of the tab. */
		private Icon icon = null;
		
		/** the temporary created disabled icon. */
		private Icon iconDisabledTemp = null;
		
		/** the disabled icon of the tab. */
		private Icon iconDisabled = null;
	
		/** the icon size. */
		private Dimension dimSize;
		
		/** the icon size. */
		private Dimension dimIconSize;

		/** the current position of the close icon, relative to the tabbed pane bounds. */
		private Rectangle rectCloseInTab;
		
		/** the font metrics. */
		private FontMetrics fmFont;
		
		/** the font. */
		private Font font = null;

		/** the foreground color. */
		private Color color = null;
		
		/** the close icon size. */
		private int iCloseSize;
		
		/** the close icon gap. */
		private int iCloseGap;
		
		/** the mark if the tab for this info is selected. */
		private boolean bSelected = false;
		
		/** the closable option for the tab. */
		private boolean bClosable = false;
		
		/** the mark if the mouse is over the close symbol. */
		private boolean bCloseRollover = false;
		
		/** the mark if the mouse is/was pressed over the close symbol. */
		private boolean bClosePressed = false;
		
		/** the mark if the tab is enabled. */
		private boolean bEnabled = true;
		   
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>TabInfo</code> for a tabbed pane.
		 * 
		 * @param pTabPane the tabbed pane for which the <code>TabInfo</code>
		 *                 will be created
		 */
		private TabInfo(JVxTabbedPane pTabPane)
		{
			tabParent = pTabPane;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void paintIcon(Component pComponent, Graphics pGraphics, int pX, int pY)
		{
			Insets insets = tabParent.getInsets();

			int iTop = pY;
			
			if (tabParent.getTabPlacement() == SwingConstants.BOTTOM)
			{
				iTop += -insets.top;
			}
			else
			{
				iTop += insets.top;
			}
			
			//show ICON
			
			int iX = pX + insets.left;
			
			if (bEnabled)
			{
				if (icon != null)
				{
					icon.paintIcon(tabParent, pGraphics, iX, iTop + (dimSize.height - dimIconSize.height) / 2);
					
					iX += icon.getIconWidth();
				}
			}
			else
			{
				if (iconDisabled != null)
				{
					iconDisabled.paintIcon(tabParent, pGraphics, iX, iTop + (dimSize.height - dimIconSize.height) / 2);
					
					iX += iconDisabled.getIconWidth();
				}
				else if (icon != null)
				{
					if (iconDisabledTemp == null)
					{
						iconDisabledTemp = UIManager.getLookAndFeel().getDisabledIcon(tabParent, icon);
					}						
				
					iconDisabledTemp.paintIcon(tabParent, pGraphics, iX, iTop + (dimSize.height - dimIconSize.height) / 2);
					
					iX += iconDisabledTemp.getIconWidth();
				}
			}
			
			//show TITLE
			
			if (sTitle != null)
			{
				int iGap = UIManager.getInt("TabbedPane.textIconGap");

				int iStringWidth = fmFont.stringWidth(sTitle); 
				int iStringHeight = fmFont.getHeight();
				
				if (icon != null)
				{					
					iX += iGap;
				}
				
				pGraphics.setFont(font);
				pGraphics.setColor(color);
				
				
				int iTabIdx = tabParent.auTabInfo.indexOf(this);
				
				Color colBack;
				
				if (iTabIdx < tabParent.getTabCount())
				{
					colBack = tabParent.getBackgroundAt(iTabIdx);
				}
				else
				{
					colBack = tabParent.getBackground();
				}
				
				if (bEnabled)
				{
	                Color fg = color;
	                
	                if (bSelected && (fg instanceof UIResource)) 
	                {
	                    Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
	                    
	                    if (selectedFG != null) 
	                    {
	                        fg = selectedFG;
	                    }
	                }
	                
	                pGraphics.setColor(fg);
					pGraphics.drawString(sTitle, iX, iTop + (dimSize.height - iStringHeight) / 2 + fmFont.getAscent());
				}
				else
				{
					pGraphics.setColor(colBack.brighter());
					pGraphics.drawString(sTitle, iX, iTop + (dimSize.height - iStringHeight) / 2 + fmFont.getAscent());
	                pGraphics.setColor(colBack.darker());
					pGraphics.drawString(sTitle, iX - 1, iTop + (dimSize.height - iStringHeight) / 2 + fmFont.getAscent() - 1);
				}
				
				//prepare the position for the close icon
				iX += iStringWidth + iCloseGap;
			}
			
			//show CLOSE icon
			
			if (bClosable)
			{
				int iY = iTop + (dimSize.height - iCloseSize) / 2;

				if (bClosePressed)
				{
					pGraphics.drawImage(icoClosePress, iX, iY, tabParent);
				}
				else if (bCloseRollover)
				{
					pGraphics.drawImage(icoCloseOver, iX, iY, tabParent);
				}
				else if (bSelected)
				{
					pGraphics.drawImage(icoCloseSelect, iX, iY, tabParent);
				}
				else
				{
					pGraphics.drawImage(icoClose, iX, iY, tabParent);
				}
				
				rectCloseInTab = new Rectangle(iX, iY, iCloseSize, iCloseSize);
			}
			else
			{
				rectCloseInTab = null;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getIconWidth()
		{
			return dimSize.width;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getIconHeight()
		{
			return dimSize.height;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Sets the tab icon.
		 * 
		 * @param pIcon the tab icon
		 */
		private void setIcon(Icon pIcon)
		{
			//otherwise Mac LaF creates a copy of our icon and we get exceptions!
			//no problem with windows/linux!
			if (pIcon != null && pIcon.getIconHeight() > 16)
			{
				icon = ImageUtil.getScaledIcon(pIcon, pIcon.getIconWidth(), 16, true);
			}
			else
			{
				icon = pIcon;
			}
			
			iconDisabledTemp = null;
			
			update();
		}
		
		/**
		 * Gets the original tab icon.
		 * 
		 * @return the tab icon
		 */
		private Icon getIcon()
		{
			return icon;
		}
		
		/**
		 * Sets the disabled tab icon.
		 * 
		 * @param pIcon the disabled tab icon
		 */
		private void setDisabledIcon(Icon pIcon)
		{
			//otherwise Mac LaF creates a copy of our icon and we get exceptions!
			//no problem with windows/linux!
			if (pIcon != null && pIcon.getIconHeight() > 16)
			{
				iconDisabled = ImageUtil.getScaledIcon(pIcon, pIcon.getIconWidth(), 16, true);
			}
			else
			{
				iconDisabled = pIcon;
			}
			
			iconDisabledTemp = null;
			
			update();
		}

		/**
		 * Gets the disabled tab icon.
		 * 
		 * @return the disabled tab icon
		 */
		private Icon getDisabledIcon()
		{
			return iconDisabled;
		}
		
		/**
		 * Sets the tab title.
		 * 
		 * @param pTitle the title
		 */
		private void setTitle(String pTitle)
		{
			sTitle = pTitle;
			
			update();
		}
		
		/**
		 * Gets the tab title.
		 * 
		 * @return the title
		 */
		private String getTitle()
		{
			return sTitle;
		}
		
		/**
		 * Sets whether the tab is closable.
		 * 
		 * @param pClosable <code>true</code> to set the tab closable; <code>false</code> otherwise
		 */
		private void setClosable(boolean pClosable)
		{
			if (bClosable != pClosable)
			{
				bClosable = pClosable;
				
				update();
			}
		}
		
		/**
		 * Returns whether the tab is closable.
		 * 
		 * @return <code>true</code> if the tab is closable; <code>false</code> otherwise
		 */
		private boolean isClosable()
		{
			return bClosable;
		}
		
		/**
		 * Gets the current bounds of the close button, relative to the tabbed pane.
		 * 
		 * @return the bounds of the 
		 */
		private Rectangle getCloseBounds()
		{
			return rectCloseInTab;
		}
		
		/**
		 * Sets the close button rollover.
		 * 
		 * @param pRollover <code>true</code> if the mouse is over the close button; otherwise <code>false</code>
		 */
		private void setCloseRollover(boolean pRollover)
		{
			if (bCloseRollover != pRollover)
			{
				bCloseRollover = pRollover;
				
				update();
			}
		}
		
		/**
		 * Gets if the mouse is over the close button.
		 * 
		 * @return <code>true</code> if the mouse is over the close button; otherwise <code>false</code>
		 */
		private boolean isCloseRollover()
		{
			return bCloseRollover;
		}
		
		/**
		 * Sets the close button pressed.
		 * 
		 * @param pClosePressed <code>true</code> if the close button is pressed; otherwise <code>false</code>
		 */
		private void setClosePressed(boolean pClosePressed)
		{
			if (bClosePressed != pClosePressed)
			{
				bClosePressed = pClosePressed;
				
				update();
			}
		}
		
		/**
		 * Gets if the close button is pressed.
		 * 
		 * @return <code>true</code> if the close button of the tab is pressed; otherwise <code>false</code>
		 */
		private boolean isClosePressed()
		{
			return bClosePressed;
		}
		
		/**
		 * Sets the tab selected.
		 * 
		 * @param pSelected <code>true</code> if the tab is selected; otherwise <code>false</code>
		 */
		private void setSelected(boolean pSelected)
		{
			if (bSelected != pSelected)
			{
				bSelected = pSelected;
				
				update();
			}
		}

		/**
		 * Sets the enabled flag of this tab icon.
		 * 
		 * @param pEnabled <code>true</code> to enable the tab/icon; otherwise <code>false</code>
		 */
		private void setEnabled(boolean pEnabled)
		{
			if (bEnabled != pEnabled)
			{
				bEnabled = pEnabled;
				
				update();
			}
		}
		
		/**
		 * Creates the dynamic icon for this tab.
		 */
		private void update()
		{
			Insets insets = tabParent.getInsets();
			
			int iGap = UIManager.getInt("TabbedPane.textIconGap");
			
			int iIconHeight = 0;
			int iIconWidth  = 0;

			//calculate the height of the dynamic image
			
			int iHeight = insets.top + insets.bottom;
			int iWidth  = insets.left + insets.right;
			
			//check for minimum sizes
			if (iWidth < 5)
			{
				iWidth = 5;
			}
			
			if (iHeight < 5)
			{
				iHeight = 5;
			}
			
			//ICON
			
			if (icon != null)
			{
				iIconHeight = icon.getIconHeight();
				iIconWidth = icon.getIconWidth();
			}

			if (iconDisabled != null)
			{
				iIconHeight = Math.max(iIconHeight, iconDisabled.getIconHeight());
				iIconWidth = Math.max(iIconWidth, iconDisabled.getIconWidth());
			}

			dimIconSize = new Dimension(iIconWidth, iIconHeight);
			
			iHeight = Math.max(iHeight, iIconHeight);
			iWidth  = Math.max(iWidth, iIconWidth);
			
			//TITLE
			
			int iStringHeight = 0;
			int iStringWidth  = 0;
			
			if (sTitle != null)
			{
				if (icon != null)
				{
					iWidth += iGap;					
				}
				
				if (component.isFontSet())
				{
					font = component.getFont();
					if (font instanceof UIResource)
					{
						font = tabParent.getFont();
					}
				}
				else
				{
					font = tabParent.getFont();
				}
				
				if (component.isForegroundSet())
				{
					color = component.getForeground();
					
					if (color instanceof UIResource)
					{
						color = tabParent.getForeground();
					}
				}
				else
				{
					color = tabParent.getForeground();
				}
				
				fmFont = tabParent.getFontMetrics(font);
				
				iStringWidth = fmFont.stringWidth(sTitle); 
				iStringHeight = fmFont.getHeight();
				
				iWidth += iStringWidth;
				iHeight = Math.max(iHeight, insets.top + insets.bottom + iStringHeight);
			}

			// CLOSE icon
			
			iCloseSize = 0;
			iCloseGap  = 0;

			if (bClosable)
			{
				iCloseSize = icoClose.getHeight(null);
				iCloseGap = 10;
				
				if (icon != null || sTitle != null)
				{
					iWidth += iCloseGap;
				}
				
				iWidth += iCloseSize;
				iHeight = Math.max(iHeight, insets.top + insets.bottom + iCloseSize);
			}
			
			dimSize = new Dimension(iWidth, iHeight);
			
			tabParent.repaint();
		}
		
	}	// TabInfo
	
}	// JVxTabbedPane
