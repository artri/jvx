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
 * 01.10.2008 - [JR] - creation
 * 17.10.2008 - [JR] - setTabMode: set frames unselected before setVisible (bugfix)
 * 07.11.2008 - [JR] - used TabEvent for TabListener
 * 11.11.2008 - [JR] - setTabMode: fixed bug with invisible index
 * 15.11.2008 - [JR] - addTab: checked maximized state
 * 25.11.2008 - [JR] - attachContent was called through the super-constructor and
 *                     setLayout -> content panel was added twice [BUGFIX]
 *                   - used InternalContentPane for background color handling
 * 11.12.2008 - [JR] - implemented selectTab, deselectTab
 *                   - focus handling: try to set the focus when add/select/remove tabs    
 * 18.12.2008 - [JR] - navigation with CTRL-ALT or CTRL+SHIFT-ALT (works with tab and frame mode) 
 * 04.02.2009 - [JR] - getTabbedPane implemented    
 * 19.02.2009 - [JR] - ignore frame switching when AltGr was pressed
 * 20.02.2009 - [JR] - navigation key en/disable   
 * 21.02.2009 - [JR] - improved frame switching/activation support
 * 01.09.2009 - [JR] - addTab: set the size of the rootPane for fixed frames otherwise it is the preferred size of the
 *                     components [BUGFIX]       
 * 16.12.2009 - [JR] - #39: set layout when set through UI
 * 13.11.2011 - [JR] - #503: 
 *                     * set an internal frame visible after remove in tabbed mode
 *                     * addImpl: always add the frame, even if it is displayed as tab
 * 03.08.2012 - [JR] - Memory leak prevention (frame cache, jdk 1.6)
 * 18.11.2013 - [JR] - #831: clearFramesCache implemented (from JVxInternalFrame)  
 * 22.01.2014 - [JR] - #919: clearFramesCache for disposed frames                     
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.rad.util.SilentAbortException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameListener;

import com.sibvisions.rad.ui.swing.ext.event.ITabListener;
import com.sibvisions.rad.ui.swing.ext.event.TabEvent;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>JVxDesktopPane</code> extends a <code>JDesktopPane</code> and
 * enables the usage of a background image.
 * 
 * @author René Jahn
 */
public class JVxDesktopPane extends JDesktopPane
                            implements ITabListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the display mode for content. */
    public enum DisplayMode
    {
        /** tabset mode. */
        Tabset,
        /** frame mode. */
        Frame,
        /** internal frame mode. */
        InternalFrame
    };
    
	/** the layer for the tab control in tabbed display mode. */
	private static final Integer TAB_LAYER = Integer.valueOf(FRAME_CONTENT_LAYER.intValue() + 1);
	
	/** Constant for not pressed CTRL. */
	private static final long CTRL_NOT_PRESSED = Long.MAX_VALUE - 1000;
	
	/** the internal content panel. */
	//don't set null because the parent constructor call setLayout
	//if set to null, the content-pane will added twice!
	private InternalContentPanel icpContent;
	
	/** the layout manager for the desktop pane. */
	private InternalContentLayout layout;
	
	/** the tabbed pane for tabbed mode. */
	private JVxTabbedPane tabs = null;
	
	/** the container for the tabbed mode tab pane. */
	private JVxPanel panTabContainer = null;
	
	/** the list of currently added internal frames. */
	private ArrayUtil<WeakReference<JInternalFrame>> auFrameCache = null;	

	/** the mapped content of internal frames in tab mode. */
	private Hashtable<JVxInternalFrame, Component> htFrameContent = null;

	/** the reverse map for content of internal frames in tab mode. */
	private Hashtable<Component, JVxInternalFrame> htContentFrame = null;
	
    /** the map for content of internal frames in frame. */
    private Hashtable<JVxInternalFrame, JFrame> htFrames = null;

    /** the current display mode. */
	private DisplayMode displayMode = DisplayMode.InternalFrame;
	
	/** the draggable option for tabs in tabbed mode. */
	private boolean bDraggableTabs = false;
	
	/** this counter is needed to suppress frame delegation events. */
	private int iIgnoreFrameCalls = 0;
	
	/** this counter is needed for suppress toFront delegation calls. */
	private int iIgnoreToFront = 0;

	/** the mark to call setFocus() only one time. */
	private boolean bSetFocusOnlyOnce = false;
	
	/** the flag indicates that the tab selection change should be completely ignored. */
	private boolean bIgnoreTabSelection = false;
	
	/** the flag indicates that the focus should not be changed when the tab selection changes. */
	private boolean bIgnoreTabFocus = false;
	
	/** the timestamp when Ctrl was pressed. */
	private long lCtrlWhen = CTRL_NOT_PRESSED;

	/** this flag indicates if it's allowed to navigate between the frames with the keyboard. */
	private boolean bNavigationKeyEnabled = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>JVxDesktopPane</code> with the {@link JVxDesktopManager}.
	 */
	public JVxDesktopPane()
	{
		//#39
		//e.g. MacOS sets its own layout through installUI, and that
		//causes problems with background images ...
		if (getLayout() != null)
		{
			setLayout(new BorderLayout());
		}
		
		setDesktopManager(new JVxDesktopManager());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// TabListener
	
	/**
	 * {@inheritDoc}
	 */
	public void closeTab(TabEvent pEvent) throws Exception
	{
		if (isTabMode())
		{
			Component comTab = tabs.getComponentAt(pEvent.getOldTabIndex());
			
			JVxInternalFrame frame = htContentFrame.get(comTab);
			
			if (frame != null)
			{
				frame.doDefaultCloseAction();
				
				if (frame.isClosed())
				{
					htContentFrame.remove(comTab);
					htFrameContent.remove(frame);
				}
				else
				{
					throw new SilentAbortException("Frame '" + frame.getTitle() + "' is not closable!");
				}
				
				//don't use tabs.getTabCount(), because this event happens before the
				//tab will be removed!
				if (htFrameContent.size() == 0)
				{
					panTabContainer.setVisible(false);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deselectTab(TabEvent pEvent) 
	{
		if (!bIgnoreTabSelection)
		{
			int iIndex = pEvent.getOldTabIndex();

			if (iIndex >= 0)
			{
				JVxInternalFrame frame = htContentFrame.get(tabs.getComponentAt(iIndex));
				
				//possible, when switching to tab mode
				if (frame != null)
				{
					if (!bIgnoreTabFocus)
					{
						frame.storeFocus();
					}
					
					iIgnoreFrameCalls++;
					
					try
					{
						//see comment in addTab
						frame.setVisible(true);
						frame.setSelected(false);
						frame.setVisible(false);
					}
					catch (PropertyVetoException pve)
					{
						//nothing to be done
					}
					finally
					{
						iIgnoreFrameCalls--;
					}
				}
			}
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectTab(TabEvent pEvent)
	{
		if (!bIgnoreTabSelection)
		{
			int iIndex = pEvent.getNewTabIndex();

			if (iIndex >= 0)
			{
				final JVxInternalFrame frame = htContentFrame.get(tabs.getComponentAt(iIndex));
				
				//possible, when switching to tab mode
				if (frame != null)
				{
					iIgnoreFrameCalls++;
					
					try
					{
						//see comment in addTab
						frame.setVisible(true);
						frame.setSelected(true);
						frame.setVisible(false);
					}
					catch (PropertyVetoException pve)
					{
						//nothing to be done
					}
					finally
					{
						iIgnoreFrameCalls--;
					}
					
					if (!bSetFocusOnlyOnce)
					{
						if (!bIgnoreTabFocus && frame.isFocusStored())
						{
							SwingUtilities.invokeLater
							(
								new Runnable()
								{
									public void run()
									{
										bSetFocusOnlyOnce = false;		
			
										//always set the focus to the tab content
										frame.restoreFocus();
									}
								}
							);				
						}
						else
						{
							//without invokeLater the focus will be set to another component!
							SwingUtilities.invokeLater
							(
								new Runnable()
								{
									public void run()
									{
										bSetFocusOnlyOnce = false;		
			
										//always set the focus to the tab content
										KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(tabs);
									}
								}
							);				
						}
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void moveTab(TabEvent pEvent)
	{
		//not needed
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public void updateUI()
	{
		if (icpContent != null)
		{
			//don't allow a different layout
			LayoutManager lmOld = icpContent.getLayout();
			
			super.updateUI();
			
			if (icpContent.getLayout() != lmOld)
			{
				icpContent.setLayout(lmOld);
			}
		}
		else
		{
			super.updateUI();
		}
	}
	
	/**
	 * Sets the layout manager for the content pane.
	 * 
	 * @param pLayout the layout manager
	 */
	@Override
	public void setLayout(LayoutManager pLayout)
	{
		if (pLayout != null || icpContent != null)
		{
			attachContentPane();
			
			icpContent.setLayout(pLayout);
		}
		
		//create internal content layout
		if (layout == null)
		{
			layout = new InternalContentLayout();
			
			super.setLayout(layout);
		}
	}
	
	/**
	 * Gets the layout manager for the content pane.
	 * 
	 * @return the layout manager
	 */
	@Override
	public LayoutManager getLayout()
	{
		if (icpContent != null)
		{
			return icpContent.getLayout();
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestFocus()
	{
		// Prevent that requestFocus of a desktop pane request whole window focus.
		// It is very unlikely, that someone wants to request window focus with a desktop pane.
		super.requestFocusInWindow();
	}
	
	/**
	 * Adds a {@link Component} to the desktop. If the component is an internal frame,
	 * then it will be added to the destops {@link JLayeredPane} otherwise it will
	 * be added to the content pane of the desktop.
	 * 
	 * @param pComponent the component to be added
	 * @param pConstraints constraints an object expressing layout constraints 
	 *                     for this component
	 * @param pIndex index the position in the container's list at which to
	 *               insert the component, where <code>-1</code> means append 
	 *               to the end
     * @exception IllegalArgumentException if <code>index</code> is invalid
     * @exception IllegalArgumentException if adding the container's parent
     *			  to itself
     * @throws IllegalArgumentException if <code>comp</code> has been added
     *         to the <code>Container</code> more than once
     * @exception IllegalArgumentException if adding a window to a container
     * @see       #add(Component)       
     * @see       #add(Component, int)       
     * @see       #add(Component, java.lang.Object)       
     * @see       java.awt.LayoutManager
     * @see       java.awt.LayoutManager2
	 */
	@Override
	protected void addImpl(Component pComponent, Object pConstraints, int pIndex)
	{
		JInternalFrame frame = null;
		
		//try to cache the internal frame
		if (pComponent instanceof JDesktopIcon)
		{
			frame = ((JDesktopIcon)pComponent).getInternalFrame();
		}
		else if (pComponent instanceof JInternalFrame)
		{
			frame = (JInternalFrame)pComponent;
		}
		
		if (frame != null)
		{
			if (auFrameCache == null)
			{
				auFrameCache = new ArrayUtil<WeakReference<JInternalFrame>>();
			}

			String sName = "" + System.identityHashCode(this);
			
			//the order will be used if an internal frame will be iconified
			Integer iPos = (Integer)frame.getClientProperty(sName);
			
			if (iPos != null)
			{
				auFrameCache.add(Math.min(auFrameCache.size(), iPos.intValue()), new WeakReference<JInternalFrame>(frame));
				
				frame.putClientProperty(sName, null);
			}
			else
			{
				auFrameCache.add(new WeakReference<JInternalFrame>(frame));
			}
		}
		
		//If the user adds a component to a special layer (maybe modal) or an internal frame
		//will be added -> it goes to the desktop pane
		if ((pConstraints != null && pConstraints instanceof Integer) 
			|| pComponent instanceof JInternalFrame 
			|| pComponent instanceof JDesktopIcon)
		{
			super.addImpl(pComponent, pConstraints, pIndex);

			if (pComponent instanceof JVxInternalFrame 
				&& pComponent.isVisible()
				&& !((JVxInternalFrame)pComponent).isModal())
			{
			    if (isTabMode())
			    {
			        addTab((JVxInternalFrame)pComponent);
			    }
			    else if (displayMode == DisplayMode.Frame)
			    {
			        addFrame((JVxInternalFrame)pComponent);
			    }
			}
		}
		else
		{
			//all other objects will be added to the content pane
			attachContentPane();
			
			icpContent.add(pComponent, pConstraints, pIndex);
			
			//important to ensure the component size, layout, ...
			icpContent.validate();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		//don't hold a reference
		setSelectedFrame(null);
	}
	
	/**
	 * Removes all the components from the desktop and
	 * the content pane, if attached.
	 */
	@Override
	public void removeAll()
	{
		super.removeAll();
		
		if (icpContent != null)
		{
			icpContent.removeAll();
		}
		
		htContentFrame = null;
		htFrameContent = null;
		
		auFrameCache = null;
	}
	
	/**
	 * Removes a component from the desktop or the content pane.
	 * 
	 * @param pComponent the component to be removed
	 */
	@Override
	public void remove(Component pComponent)
	{
		//update frame cache
		if (auFrameCache != null)
		{
			JInternalFrame frame = null;

			if (pComponent instanceof JInternalFrame)
			{
				frame = ((JInternalFrame)pComponent);
				
			}
			else if (pComponent instanceof JDesktopIcon)
			{
				frame = ((JDesktopIcon)pComponent).getInternalFrame();
			}
			
			if (frame != null)
			{
				int iPos = auFrameCache.indexOf(frame);

				if (iPos >= 0)
				{
					auFrameCache.remove(iPos);
					
					frame.putClientProperty("" + System.identityHashCode(this), Integer.valueOf(iPos));
					
					if (auFrameCache.size() == 0)
					{
						auFrameCache = null;
					}
				}
			}
		}
		
		Container con = pComponent.getParent();
		
		if (con == null)
		{
			return;
		}
		
		//removes the component from the desktop or the
		//content pane
		if (con == this)
		{
			boolean bSetVisible = false;
			
			if (pComponent instanceof JVxInternalFrame && isTabMode())
			{
				if (isVisible((JVxInternalFrame)pComponent))
				{
					bSetVisible = true;
				}
			}
			
			super.remove(pComponent);

			JInternalFrame nextFrame = null;
			if (auFrameCache != null)
			{
				for (int i = 0, anz = auFrameCache.size(); i < anz && nextFrame == null; i++)
				{
					JInternalFrame frame = auFrameCache.get(i).get();
					
					if (frame != null && frame.isVisible() && !frame.isIcon())
					{
						nextFrame = frame;
					}
				}
			}
				
			//Important to receive key events (e.g if all internal frames are iconified)
			if (nextFrame == null)
			{
				Component focusComp = null;
				if (pComponent instanceof JVxInternalFrame)
				{
					focusComp = ((JVxInternalFrame)pComponent).getPreviousFocusOwner();
					if (focusComp != null && !focusComp.isShowing())
					{
						focusComp = null;
					}
				}
				
				if (focusComp == null)
				{
					Container root = getFocusCycleRootAncestor();
					if (root != null && root.getFocusTraversalPolicy() != null)
					{
						focusComp = root.getFocusTraversalPolicy().getFirstComponent(this);
						
						if (focusComp == null)
						{
							focusComp = root.getFocusTraversalPolicy().getFirstComponent(root);
						}
					}
				}
				if (focusComp == null)
				{
					focusComp = this;
				}
				focusComp.requestFocusInWindow();
			}
			else
			{
				nextFrame.restoreSubcomponentFocus();
			}

			//in Tab-Mode an internal frame is always invisible!
			//if we remove the frame from the desktop and it was visible as tab -> update the frame visibility,
			//otherwise it is not possible to add it again, because addImpl checks the visibility!
			if (bSetVisible)
			{
				((JVxInternalFrame)pComponent).setVisibleIntern(true);
			}
		}
		else if (con == tabs)
		{
			//this case should not happen with default API calls, but possible with plain swing
			tabs.remove(pComponent);
			
			JInternalFrame frame = htContentFrame.remove(pComponent);
			htFrameContent.remove(frame);

			//clean objects
			if (htContentFrame.size() == 0)
			{
				htFrameContent = null;
				htContentFrame = null;
				
				panTabContainer.setVisible(false);
			}
		}
		else if (con == icpContent)
		{
			icpContent.remove(pComponent);
		}
		
		if (getAllFrames().length == 0)
		{
			setSelectedFrame(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean processKeyBinding(KeyStroke pStroke, KeyEvent pEvent, int pModifier, boolean pPressed)
	{
		//no navigation keys if at least one frame is modal or the navigation key option
		//is disabled
		if (!bNavigationKeyEnabled || JVxInternalFrame.isModalLayerPanelVisible(this))
		{
			return super.processKeyBinding(pStroke, pEvent, pModifier, pPressed);
		}
		else
		{	
			//When ALTGr was pressed, it's not allowed to switch between the frames
			//It's possible to switch with AltGr + Atlt or Ctrl + AltGr
			if (pEvent.getKeyCode() == KeyEvent.VK_CONTROL)
			{
				if (pPressed)
				{
					if (lCtrlWhen == CTRL_NOT_PRESSED) // Only track first press.
					{
						lCtrlWhen = pEvent.getWhen();
					}
				}
				else
				{
					lCtrlWhen = CTRL_NOT_PRESSED; // Reset on release.
				}
			}
			
			if (auFrameCache != null 
				&& pPressed 
				&& pEvent.getKeyCode() == KeyEvent.VK_ALT 
				&& lCtrlWhen + 100 < pEvent.getWhen())
			{
				int iDiff;
				if (pEvent.isShiftDown())
				{
					iDiff = -1;
				}
				else
				{
					iDiff = 1;
				}
				
				JInternalFrame frame;
				
				if (isTabMode())
				{
					int iPos = tabs.getSelectedIndex();
					
					if (iPos >= 0)
					{
						iPos += iDiff;
						
						if (iPos < 0)
						{
							iPos = tabs.getTabCount() - 1;
						}
						else if (iPos >= tabs.getTabCount())
						{
							iPos = 0;
						}
						
						tabs.setSelectedIndex(iPos);
					}
				}
				else
				{
					int iCurrentSelection = auFrameCache.indexOf(getSelectedFrame());
					int iPos = iCurrentSelection;
					int iFrameCount = auFrameCache.size();
					int iCurrentFrame = 0;
					
					if (iCurrentSelection != -1)
					{
						do
						{
							//don't loop endless!
							iCurrentFrame++;
							
							iPos += iDiff;
							
							if (iPos < 0)
							{
								iPos = auFrameCache.size() - 1;
							}
							else if (iPos >= auFrameCache.size())
							{
								iPos = 0;
							}
							
							frame = (JInternalFrame)auFrameCache.get(iPos).get();
							
							if (frame == null)
							{
								auFrameCache.remove(iPos);
							}
							else if (!frame.isVisible() && !frame.isIcon())
							{
								frame = null;
							}
						}
						while (frame == null && iPos != iCurrentSelection && iCurrentFrame <= iFrameCount);
						
						if (frame != null)
						{
							try
							{
								if (frame.isIcon())
								{
									frame.setIcon(false);
								}
		
								frame.toFront();
							}
							catch (PropertyVetoException pve)
							{
								//nothing to be done!
							}
						}
					}
				}
				
				return true;
			}
			else
			{
			    if (pEvent.getKeyCode() == KeyEvent.VK_ALT && lCtrlWhen + 100 > pEvent.getWhen())
			    {
			        lCtrlWhen = CTRL_NOT_PRESSED;
			    }
				return super.processKeyBinding(pStroke, pEvent, pModifier, pPressed);
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the content pane to the desktops FRAME_CONTENT_LAYER, if it's not
	 * already done.
	 */
	private void attachContentPane()
	{
		if (icpContent == null)
		{
			icpContent = new InternalContentPanel();
			
			super.addImpl(icpContent, JLayeredPane.FRAME_CONTENT_LAYER, 0);
		}
	}
	
	/**
	 * Sets the display mode of the content.
	 * 
	 * @param pMode the display mode
	 */
	public void setDisplayMode(DisplayMode pMode)
	{
		if (pMode != displayMode)
		{
			if (pMode == DisplayMode.Tabset)
			{
				tabs = new JVxTabbedPane(false);
				tabs.setTabLayoutPolicy(JVxTabbedPane.SCROLL_TAB_LAYOUT);
				tabs.setDraggable(bDraggableTabs);
				tabs.addTabListener(this);
				//opaque on desktop
				tabs.setOpaque(true);
				
				JVxBorderLayout blTabContainer = new JVxBorderLayout();
				
				panTabContainer = new JVxPanel(blTabContainer);
				panTabContainer.setSize(getSize());
				panTabContainer.setVisible(false);
				
				JVxIcon icoLine = new JVxIcon(JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/1x1_gray.png"));
				icoLine.setHorizontalAlignment(JVxConstants.STRETCH);
				
				panTabContainer.add(icoLine, JVxBorderLayout.NORTH);
				panTabContainer.add(tabs, JVxBorderLayout.CENTER);
				
				htFrameContent = new Hashtable<JVxInternalFrame, Component>();
				htContentFrame = new Hashtable<Component, JVxInternalFrame>();
				
				JInternalFrame[] frames = super.getAllFrames();
				
				int iSelected = -1;
				
				for (int i = 0, anz = frames.length, j = 0; i < anz; i++)
				{
					if (frames[i] instanceof JVxInternalFrame
						&& frames[i].isVisible()
						&& !((JVxInternalFrame)frames[i]).isModal())
					{
						if (frames[i].isSelected())
						{
							iSelected = j;
						}
						
						addTab((JVxInternalFrame)frames[i]);
						
						//otherwise it's possible that invisible frames damage the index!
						j++;
					}
				}
				
				panTabContainer.setSize(getSize());
				panTabContainer.setVisible(false);
				
				super.add(panTabContainer, TAB_LAYER, 0);

				//don' save the old focus, because it can be a menu call!
				bIgnoreTabFocus = true;
				
				try
				{
					panTabContainer.setVisible(tabs.getTabCount() > 0);
					tabs.setSelectedIndex(iSelected < 0 ? tabs.getTabCount() - 1 : iSelected);
				}
				finally
				{
					bIgnoreTabFocus = false;
				}
				
				tabs.requestFocusInWindow();	
				
//				SwingUtilities.invokeLater
//				(
//					new Runnable()
//					{
//						public void run()
//						{
//							bSetFocusOnlyOnce = false;		
//
//							//always set the focus to the tab content
//							KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(tabs);
//						}
//					}
//				);				
			}
			else if (pMode == DisplayMode.InternalFrame)
			{
				if (displayMode == DisplayMode.Tabset)
				{
					Component compSelected = tabs.getSelectedComponent();
		
					super.remove(panTabContainer);
					repaint();
					
					panTabContainer = null;
					tabs = null;
		
					//switch back from tabs to internal frames
					JVxInternalFrame frame;
					
					JRootPane rootPane;
					
					for (Map.Entry<JVxInternalFrame, Component> entry : htFrameContent.entrySet())
					{
						frame = entry.getKey(); 
	
						//unset the preferred size
						rootPane = (JRootPane)((Container)entry.getValue()).getComponent(0);
						rootPane.setPreferredSize(null);
						
						frame.setRootPane(rootPane);
						frame.setVisible(true);
					}
		
					//use selected tab as selected frame
					//needed because frame.setVisible changed the selection!
					if (compSelected != null)
					{
						frame = htContentFrame.get(compSelected);
			
						if (frame != null)
						{
							try
							{
								frame.setSelected(true);
							}
							catch (PropertyVetoException pve)
							{
								//ignore
							}
						}
					}
					
					//reset cache
					htFrameContent = null;
					htContentFrame = null;
				}
				else if (displayMode == DisplayMode.Frame)
				{
	                //switch back from frames to internal frames
	                JVxInternalFrame frame;
	                
	                JFrame jframe;
	                
	                JRootPane rootPane;
	                
	                Component comp;
	                
	                HashMap<JVxInternalFrame, JFrame> htCopy = new HashMap<JVxInternalFrame, JFrame>(htFrames); 
	                
	                for (Map.Entry<JVxInternalFrame, JFrame> entry : htCopy.entrySet())
	                {
	                    frame = entry.getKey(); 
	
	                    jframe = entry.getValue();
	                    
	                    //unset the preferred size
	                    rootPane = (JRootPane)((Container)jframe).getComponent(0);
	                    rootPane.setPreferredSize(new Dimension(0, 0));
	                    
	                    frame.setRootPane(rootPane);
	
	                    //remove before setVisible to avoid delegation
	                    htFrames.remove(frame);
	                    comp = htFrameContent.remove(frame);
	                    htContentFrame.remove(comp);
	                    
	                    frame.setVisible(true);
	                    
	                    jframe.dispose();
	                }
				    
	                //reset cache
	                htFrameContent = null;
	                htContentFrame = null;
	                htFrames = null;
	
	                comp = this;
	                JFrame frTop = null; 
	                
	                while ((comp = comp.getParent()) != null)
	                {
	                    if (comp instanceof JFrame)
	                    {
	                        frTop = (JFrame)comp;
	                    }
	                }
	
	                //try to bring frame to front
	                if (frTop != null)
	                {
	                    final JFrame frTopFinal = frTop;
	                    
	                    SwingUtilities.invokeLater(new Runnable()
	                    {
	                        public void run()
	                        {
	                            frTopFinal.toFront();
	                            requestFocus();                    
	                        }
	                    });
	                }
				}
			}
			else if (pMode == DisplayMode.Frame)
			{
	            htFrameContent = new Hashtable<JVxInternalFrame, Component>();
	            htContentFrame = new Hashtable<Component, JVxInternalFrame>();
	            
	            JInternalFrame[] frames = super.getAllFrames();
	            
	            JInternalFrame frame = null;
	            
	            for (int i = 0, anz = frames.length; i < anz; i++)
	            {
	                if (frames[i] instanceof JVxInternalFrame
	                    && frames[i].isVisible()
	                    && !((JVxInternalFrame)frames[i]).isModal())
	                {
	                    if (frames[i].isSelected())
	                    {
	                        frame = frames[i];
	                    }
	                    
	                    addFrame((JVxInternalFrame)frames[i]);
	                }
	            }
	            
	            if (frame != null)
	            {
	                JFrame jframe = htFrames.get(frame);
	                
	                if (jframe != null)
	                {
	                    jframe.toFront();
	                }
	            }
			}
		}
		
		displayMode = pMode;
	}
	
	/**
	 * Returns if the desktop pane is in tab mode. That means that
	 * all visible and non modal internal frames will be shown as
	 * tab page.
	 * 
	 * @return <code>true</code> if the desktop pane is in tab mode,
	 *         otherwise <code>false</code>
	 */
	public DisplayMode getDisplayMode()
	{
		return displayMode;
	}
	
	/**
	 * Gets whether tab mode is active.
	 * 
	 * @return <code>true</code> if tab mode is active, <code>false</code> otherwise
	 */
	public boolean isTabMode()
	{
		// It has to be exactly this way because:
		// - switch from tabmode to internalframemode, tabs is already set to null before setting the JVxInternalFrame to visible. 
		//   setVisible routes back to JVxDesktopPane, and if displayMode is used, it is still tabmode
		// - switch from internalframemode to tabmode, isSelected is called, but tabs is already != null so isSelected reroutes to Tabsetpanel
		//   so getParent() has to be considered too, to ensure, it is still not fully tabmode.
	    return panTabContainer != null && panTabContainer.getParent() != null;
	}
	
	/**
	 * Sets whether tab mode is active.
	 * 
	 * @param pTabMode <code>true</code> if tab mode is active, <code>false</code> otherwise
	 */
	public void setTabMode(boolean pTabMode)
	{
		if (pTabMode)
		{
			setDisplayMode(DisplayMode.Tabset);
		}
		else
		{
			setDisplayMode(DisplayMode.InternalFrame);
		}
	}
	
    /**
	 * Sets the drag option for tabs in tabbed mode. If the option is enabled, it's
	 * possible to change the position of tabs.
	 *  
	 * @param pDraggable <code>true</code> to enable the drag option, otherwise <code>false</code>
	 */
	public void setTabsDraggable(boolean pDraggable)
	{
		bDraggableTabs = pDraggable;
		
		if (isTabMode())
		{
			tabs.setDraggable(pDraggable);
		}
	}
	
	/**
	 * Returns the drag option of tabs in tabbed mode.
	 * 
	 * @return <code>true</code> if it's possible to drag tabs, <code>false</code> otherwise
	 */
	public boolean isTabsDraggable()
	{
		return bDraggableTabs;
	}
	
	/**
	 * Sets the layout of a tab maximized or preferred. 
	 * 
	 * @param pConstraint the constraints of the layout for the tab
	 * @param pPreferred <code>true</code> true to set the layout preferred, <code>false</code> to
	 *                   set it maximized
	 */
	private void setPreferredSizeAnchors(JVxFormLayout.Constraint pConstraint, boolean pPreferred)
	{
		pConstraint.getTopAnchor().setAutoSize(pPreferred);
		pConstraint.getLeftAnchor().setAutoSize(pPreferred);
		pConstraint.getBottomAnchor().setAutoSize(pPreferred);
		pConstraint.getRightAnchor().setAutoSize(pPreferred);
	}

    /**
     * Adds an internal frame as tab.
     *  
     * @param pFrame the frame
     */
    protected void addTab(JVxInternalFrame pFrame)
    {
        addContent(pFrame, DisplayMode.Tabset);
    }	
	
    /**
     * Adds an internal frame as frame.
     *  
     * @param pFrame the frame
     */
    protected void addFrame(JVxInternalFrame pFrame)
    {
        addContent(pFrame, DisplayMode.Frame);
    }
    
    /**
     * Adds content with support for the given display mode.
     * 
     * @param pFrame the frame to add
     * @param pMode the display mode
     */
	private void addContent(JVxInternalFrame pFrame, DisplayMode pMode)
	{
		Container contFrameRoot = pFrame.getRootPane();

		//add fix sized frames
		Container contTab = new Container();
		
		JVxFormLayout flTab = new JVxFormLayout();
		flTab.setMargins(new Insets(0, 0, 0, 0));
		
		contTab.setLayout(flTab);
		
		contTab.add(contFrameRoot, new JVxFormLayout.Constraint(flTab.getTopMarginAnchor(),
															    flTab.getLeftMarginAnchor(),
															    flTab.getBottomMarginAnchor(),
															    flTab.getRightMarginAnchor()));

		//map the content of the internal frame to switch back to window mode
		//important to set cache before setSelectedIndex will be called!
		htFrameContent.put(pFrame, contTab);
		htContentFrame.put(contTab, pFrame);

		boolean bFixed = !pFrame.isMaximum() && !pFrame.isResizable();

		if (bFixed)
		{
			contFrameRoot.setPreferredSize(contFrameRoot.getSize());
		}
		
		//preferred or maximized
		//maximized frames are not resizable 
		setPreferredSizeAnchors(flTab.getConstraint(contFrameRoot), bFixed);
		
		switch (pMode)
		{
		    case Tabset:
		        bIgnoreTabFocus = true;

		        try
		        {
		            tabs.addTab(pFrame.getTitle(), pFrame.getFrameIcon(), contTab);
		            
		            int iTabIndex = tabs.getTabCount() - 1;
		    
		            tabs.setClosableAt(iTabIndex, pFrame.isClosable());
		            tabs.setSelectedIndex(iTabIndex);
		            
		            panTabContainer.setVisible(true);
		        }
		        finally
		        {
		            bIgnoreTabFocus = false;
		        }
		        
		        break;
		    case Frame:
		        JFrame frame = new JFrame(pFrame.getTitle());
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        
		        ImageIcon icon = ((ImageIcon)pFrame.getFrameIcon());
		        
		        if (icon != null)
		        {
		            frame.setIconImage(icon.getImage());
		        }
		        
		        frame.setLayout(new JVxBorderLayout());
		        frame.setSize(pFrame.getWidth(), pFrame.getHeight());
		        frame.setLocation(pFrame.getLocationOnScreen());
		        frame.add(contTab);
		        
		        frame.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent pEvent)
                    {
                        Component comTab = ((JFrame)pEvent.getWindow()).getContentPane().getComponent(0);
                        
                        JVxInternalFrame frame = htContentFrame.get(comTab);
                        
                        if (frame != null)
                        {
                            frame.doDefaultCloseAction();
                            
                            if (frame.isClosed())
                            {
                                htContentFrame.remove(comTab);
                                htFrameContent.remove(frame);
                                
                                htFrames.remove(frame);
                                
                                if (htFrames.isEmpty())
                                {
                                    htFrames = null;
                                }
                            }
                        }
                    }
                });
		        
		        if (htFrames == null)
		        {
		            htFrames = new Hashtable<JVxInternalFrame, JFrame>();
		        }
		        
		        htFrames.put(pFrame, frame);
		        
		        frame.setVisible(true);
		        break;
		    default: 
		        throw new RuntimeException("Unrecognized display mode: " + pMode);
		}
		
		//hide frame and cache reference
		iIgnoreFrameCalls++;
		
		//2 to avoid switching (with 1 the tab would be changed)
		//test-case: open a frame in tabbed mode, when the active frame throws an error
		iIgnoreToFront += 2;
		
		try
		{
			//without this calls, the root pane will not handle mousemotion events correctly!
			//test: move over a text editor or a split pane divider!
			//tests should include: 
			// * switching frame to tab mode with opened frames
			// * add open frames when in tab mode
			// * set tab mode (with no frames) and open a frame
			
			//remove the listeners to suppress unwanted selection changes!
			InternalFrameListener[] lis = pFrame.getInternalFrameListeners();
			
			if (lis != null)
			{
				for (int i = 0, anz = lis.length; i < anz; i++)
				{
					pFrame.removeInternalFrameListener(lis[i]);
				}
			}
			
			try
			{
				pFrame.setVisible(true);
				pFrame.setVisible(false);
			}
			finally
			{
				if (lis != null)
				{
					for (int i = 0, anz = lis.length; i < anz; i++)
					{
						pFrame.addInternalFrameListener(lis[i]);
					}
				}
			}
		}
		finally
		{
			iIgnoreToFront -= 2;
			iIgnoreFrameCalls--;
		}
	}
	
	/**
	 * Gets the tab index of an internal frame.
	 *  
	 * @param pFrame the internal frame
	 * @return the tab index of the frame or <code>-1</code> if the tabbed pane
	 *         doesn't contain the frame as tab.
	 */
	private int getTabIndex(JVxInternalFrame pFrame)
	{
		if (isTabMode())
		{
			Component comp = htFrameContent.get(pFrame);
			
			if (comp != null)
			{
				return tabs.indexOfComponent(comp);
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the tab pane.
	 * 
	 * @return the tab pane or <code>null</code> if the tab mode is not active
	 */
	public JVxTabbedPane getTabbedPane()
	{
		return tabs;
	}

	/**
	 * En- or disables the frame navigation with the keyboard.
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
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Delegation methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Disposes an internal frame. The frame will be removed from the tabbed
	 * when the tab mode is active.
	 * 
	 * @param pFrame the internal frame
	 */
	void dispose(JVxInternalFrame pFrame)
	{
		setVisible(pFrame, false);
	}
	
	/**
	 * Gets whether an internal frame is visible as tab in tabbed mode.
	 * 
	 * @param pFrame the internal frame
	 * @return <code>true</code> if the frame is displayed as tab
	 */
	boolean isVisible(JVxInternalFrame pFrame)
	{
		//let the frame do it's job!
		if (iIgnoreFrameCalls == 0)
		{
			if (getTabIndex(pFrame) >= 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sets the visibility of an internal frame if desktop is not in internal frame mode. 
	 * 
	 * @param pFrame the internal frame
	 * @param pVisible <code>true</code> to add the internal frame with current display mode, 
	 *                 when internal frame mode is not active. <code>false</code> to
	 *                 hide the internal frame.
	 * @return <code>true</code> if the frame is not an internal frame; <code>false</code> if
	 *         the frame is displayed as a real internal frame. In that case
	 *         the internal frame have to set the visibility
	 */
	boolean setVisible(JVxInternalFrame pFrame, boolean pVisible)
	{
		//let the frame do it's job!
		if (iIgnoreFrameCalls == 0)
		{
            if (!pFrame.isModal())
            {
    			if (isTabMode())
    			{
					if (pVisible)
					{
						Component comp = htFrameContent.get(pFrame);
						
						if (comp == null)
						{
							addTab(pFrame);
						}
						
						return true;
					}
					else
					{
						Component comp = htFrameContent.get(pFrame);
						
						if (comp != null)
						{
							bIgnoreTabSelection = true;
							
							try
							{
								tabs.remove(comp);
							}
							finally
							{
								bIgnoreTabSelection = false;
							}
							
							//hide the tab and make the background image visible
							if (tabs.getTabCount() == 0)
							{
								panTabContainer.setVisible(false);
							}
							
							//give back the content to the frame
							pFrame.setRootPane((JRootPane)((Container)comp).getComponent(0));
							
							htFrameContent.remove(pFrame);
							htContentFrame.remove(comp);
							
							return true;
						}
					}
				}
    			else if (displayMode == DisplayMode.Frame)
    			{
    			    JFrame frame = htFrames.get(pFrame);
    			    
    			    if (frame != null)
    			    {
//    			        frame.setVisible(pVisible);
//    			        
//    			        return true;
    			    }
    			}
			}
		}
		
		return false;
	}

	/**
	 * Gets the selection/active state of an internal frame. The tab
	 * of the frame will be selected if the tab mode is active.
	 * 
	 * @param pFrame the internal frame
	 * @return <code>true</code> if the frame is a tab; and is selected.
	 */
	boolean isSelected(JVxInternalFrame pFrame)
	{
		int index = getTabIndex(pFrame);
			
		if (index >= 0)
		{
			return tabs.getSelectedIndex() == index;
		}
		
		return false;
	}

	/**
	 * Sets the selection/active state of an internal frame. The tab
	 * of the frame will be selected if the tab mode is active.
	 * 
	 * @param pFrame the internal frame
	 * @param pSelected <code>true</code> if the frame should be selected, otherwise <code>false</code>
	 * @return <code>true</code> if the frame is a tab; <code>false</code> if
	 *         the frame is displayed as a real internal frame. In that case
	 *         the internal frame have to set the selection.
	 */
	boolean setSelected(JVxInternalFrame pFrame, boolean pSelected)
	{
		//let the frame do it's job!
		if (iIgnoreFrameCalls == 0)
		{
			int index = getTabIndex(pFrame);
			
			if (index >= 0)
			{
				tabs.setSelectedIndex(index);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sets the selection/active state of an internal frame. The tab of
	 * the frame will be selected if the tab mode is active.
	 * 
	 * @param pFrame the internal frame
	 * @return <code>true</code> if the frame is a tab; <code>false</code> if
	 *         the frame is displayed as a real internal frame. In that case
	 *         the internal frame have to set the selection.
	 */
	boolean toFront(JVxInternalFrame pFrame)
	{
		if (isTabMode())
		{
			int index = getTabIndex(pFrame);
			
			//only important if the user calls toFront manually during frame selection change!
			
			//don't switch if addTab was called
			//don't switch if the user tries to select an existing tab
			//don't do the selection change more than twice (not once because it's possible
			//that the user changes the frame selection during another will be selected ->
			//e.g. in an error case)
			if (iIgnoreToFront <= 1 && tabs.getSelectedIndex() != index)
			{
				iIgnoreToFront++;
				
				try
				{
					if (index >= 0)
					{
						tabs.setSelectedIndex(index);
						
						return true;
					}
				}
				finally
				{
					iIgnoreToFront--;
				}
			}
		}
		
		return false;
	}

	/**
	 * Sets an internal frame en- or disabled. The tab
	 * of the frame will be en- or disabled.
	 * 
	 * @param pFrame the internal frame
	 * @param pEnabled <code>true</code> if the frame should be enabled, otherwise <code>false</code>
	 */
	void setEnabled(JVxInternalFrame pFrame, boolean pEnabled)
	{
		int index = getTabIndex(pFrame);
		
		if (index >= 0)
		{
			tabs.setEnabledAt(index, pEnabled);
		}
	}
	
	/**
	 * Sets an internal resizable or fixed siz. The tab
	 * of the frame will be set maximized or preferred.
	 * 
	 * @param pFrame the internal frame
	 * @param pResizable <code>true</code> if the frame should be resizable, otherwise <code>false</code>
	 */
	void setResizable(JVxInternalFrame pFrame, boolean pResizable)
	{
		if (isTabMode())
		{
			Container cont = (Container)htFrameContent.get(pFrame);
			
			if (cont != null)
			{
				JVxFormLayout flLayout = (JVxFormLayout)cont.getLayout();
				
				setPreferredSizeAnchors(flLayout.getConstraint(cont.getComponent(0)), !pResizable);
				
				//otherwise the content will stay preferred (if it was the previous state)
				if (pResizable)
				{
					flLayout.setMargins(new Insets(0, 0, 0, 0));
				}
			}
		}
	}

	/**
	 * Sets the icon of an internal frame. The tab icon
	 * will be changed if the tab mode is active.
	 *  
	 * @param pFrame the internal frame
	 * @param pIcon the icon
	 */
	void setIcon(JVxInternalFrame pFrame, Icon pIcon)
	{
		int index = getTabIndex(pFrame);
		
		if (index >= 0)
		{
			tabs.setIconAt(index, pIcon);
		}
	}
	
	/**
	 * Sets the title of an internal frame. The tab title
	 * will be changed if the tab mode is active.
	 *  
	 * @param pFrame the internal frame
	 * @param pTitle the title
	 */
	void setTitle(JVxInternalFrame pFrame, String pTitle)
	{
		int index = getTabIndex(pFrame);
		
		if (index >= 0)
		{
			tabs.setTitleAt(index, pTitle);
		}
	}
	
	/**
	 * Sets an internal frame closable. The tab
	 * of the frame will be closable if the tab mode is active.
	 * 
	 * @param pFrame the internal frame
	 * @param pClosable <code>true</code> if the frame should be closable, otherwise <code>false</code>
	 */
	void setClosable(JVxInternalFrame pFrame, boolean pClosable)
	{
		int index = getTabIndex(pFrame);
		
		if (index >= 0)
		{
			tabs.setClosableAt(index, pClosable);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>InternalContentPanel</code> handles all non {@link JVxInternalFrame}
	 * content in it's own panel.
	 * 
	 * @author René Jahn
	 */
	final class InternalContentPanel extends JPanel
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>InternalContentPanel</code> with
		 * a {@link BorderLayout}.
		 */
		private InternalContentPanel()
		{
			setLayout(new BorderLayout(0, 0));
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the background color. The desktop pane background will be
		 * used for look and feel compatibility reasons.
		 * 
		 * @return the background color
		 */
		@Override
		public Color getBackground()
		{
			return JVxDesktopPane.this.getBackground();
		}
		
	}	// InternalContentPanel
	
	/**
	 * The <code>ContentLayout</code> layouts the content pane of the desktop.
	 * All other components will be ignored.
	 * 
	 * @author René Jahn
	 */
	private class InternalContentLayout implements LayoutManager
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the default preferred size of the desktop, if there is no content pane. */
		private Dimension dimDefault = new Dimension(0, 0);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void addLayoutComponent(String pName, Component pComponent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void removeLayoutComponent(Component pComponent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public Dimension minimumLayoutSize(Container pParent)
		{
			return preferredLayoutSize(pParent);
		}

		/**
		 * {@inheritDoc}
		 */
		public Dimension preferredLayoutSize(Container pParent)
		{
			if (icpContent != null)
			{
				return icpContent.getPreferredSize();
			}
			
			return dimDefault;
		}

		/**
		 * {@inheritDoc}
		 */
		public void layoutContainer(Container pParent)
		{
			if (icpContent != null)
			{
				icpContent.setBounds(0, 0, pParent.getWidth(), pParent.getHeight());
			}
			
			if (panTabContainer != null)
			{
				panTabContainer.setBounds(0, 0, pParent.getWidth(), pParent.getHeight());
			}
		}
		
	}	// ContentLayout
	
}	// JVxDesktopPane
