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
 * 22.10.2008 - [JR] - close, drag support
 * 05.08.2009 - [JR] - set/isNavigationKeysEnabled implemented
 * 25.07.2013 - [JR] - #732: event act-/deactivated tab implemented
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.util.ArrayList;

import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.swing.ext.JVxTabbedPane;
import com.sibvisions.rad.ui.swing.ext.event.ITabListener;
import com.sibvisions.rad.ui.swing.ext.event.TabEvent;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingTabsetPanel</code> is the <code>ITabsetPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTabbedPane
 * @see ITabsetPanel
 */
public class SwingTabsetPanel extends SwingComponent<JVxTabbedPane> 
                              implements ITabsetPanel,
                                         ITabListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** images of the tabs. */
	private ArrayList<IImage> images = new ArrayList<IImage>();
	
	/** the EventHandler for tabClosed. */
	private TabsetHandler<ITabClosedListener> eventTabClosed = null;
	/** the EventHandler for tabMoved. */
	private TabsetHandler<ITabMovedListener> eventTabMoved = null;
	/** the EventHandler for tabActivated. */
	private TabsetHandler<ITabActivatedListener> eventTabActivated = null;
	/** the EventHandler for tabDeactivated. */
	private TabsetHandler<ITabDeactivatedListener> eventTabDeactivated = null;
	
	/** whether the tab listener was added. */
	private boolean bTabListenerAdded = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingTabsetPanel</code>.
	 */
	public SwingTabsetPanel()
	{
		super(new JVxTabbedPane());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//ITABLISTENER

	/**
	 * {@inheritDoc}
	 */
	public void selectTab(TabEvent pEvent)
	{
		if (eventTabActivated != null)
		{
			eventTabActivated.dispatchEvent(new UITabsetEvent(eventSource, 
													          UITabsetEvent.TABSET_ACTIVATED, 
													          pEvent.getWhen(), 
													          pEvent.getModifiers(), 
													          pEvent.getOldTabIndex(),
													          pEvent.getNewTabIndex()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deselectTab(TabEvent pEvent) throws Exception
	{
		if (eventTabDeactivated != null)
		{
			eventTabDeactivated.dispatchEvent(new UITabsetEvent(eventSource, 
													            UITabsetEvent.TABSET_DEACTIVATED, 
													            pEvent.getWhen(), 
													            pEvent.getModifiers(), 
													            pEvent.getOldTabIndex(),
													            pEvent.getNewTabIndex()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void closeTab(TabEvent pEvent) throws Exception
	{
		images.remove(pEvent.getOldTabIndex());
		
		if (eventTabClosed != null)
		{
			eventTabClosed.dispatchEvent(new UITabsetEvent(eventSource, 
													       UITabsetEvent.TABSET_CLOSED, 
													       pEvent.getWhen(), 
													       pEvent.getModifiers(), 
													       pEvent.getOldTabIndex(),
													       pEvent.getNewTabIndex()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void moveTab(TabEvent pEvent)
	{
		int oldIndex = pEvent.getOldTabIndex();
		int newIndex = pEvent.getNewTabIndex();
		
		IImage image = images.get(oldIndex); 
		images.remove(oldIndex);
		images.add(newIndex, image);
		
		if (eventTabMoved != null)
		{
			eventTabMoved.dispatchEvent(new UITabsetEvent(eventSource, 
													      UITabsetEvent.TABSET_MOVED, 
													      pEvent.getWhen(), 
													      pEvent.getModifiers(), 
													      oldIndex,
													      newIndex));
		}
	}
	
	//ITABSETPANEL
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabledAt(int pTabPosition)
	{
		return resource.isEnabledAt(pTabPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabledAt(int pTabPosition, boolean pEnabled)
	{
		resource.setEnabledAt(pTabPosition, pEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTabPlacement()
	{
		return resource.getTabPlacement();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabPlacement(int pPlacement)
	{
		resource.setTabPlacement(pPlacement);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabLayoutPolicy()
	{
		return resource.getTabLayoutPolicy();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabLayoutPolicy(int pLayoutPolicy)
	{
		resource.setTabLayoutPolicy(pLayoutPolicy);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getSelectedIndex()
	{
		return resource.getSelectedIndex();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelectedIndex(int pIndex)
	{
		resource.setSelectedIndex(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getIconAt(int pIndex)
	{
		return images.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setIconAt(int pIndex, IImage pImage)
	{
    	if (pImage == null)
    	{
    		resource.setIconAt(pIndex, null);
    	}
    	else
    	{
    		resource.setIconAt(pIndex, (ImageIcon)pImage.getResource());
    	}

		images.set(pIndex, pImage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosableAt(int pTabPosition)
	{
		return resource.isEnabledAt(pTabPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setClosableAt(int pTabPosition, boolean pEnabled)
	{
		resource.setClosableAt(pTabPosition, pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDraggable()
	{
		return resource.isDraggable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDraggable(boolean pDraggable)
	{
		resource.setDraggable(pDraggable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTextAt(int pIndex, String pText)
	{
		resource.setTitleAt(pIndex, pText);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTextAt(int pIndex)
	{
		return resource.getTitleAt(pIndex);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		resource.setNavigationKeysEnabled(pEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return resource.isNavigationKeysEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabClosedListener> eventTabClosed()
	{
		if (eventTabClosed == null)
		{
			eventTabClosed = new TabsetHandler<ITabClosedListener>(ITabClosedListener.class);

			if (!bTabListenerAdded)
			{
				resource.addTabListener(this);
				
				bTabListenerAdded = true;
			}
		}
		
		return eventTabClosed;
	}

	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabMovedListener> eventTabMoved()
	{
		if (eventTabMoved == null)
		{
			eventTabMoved = new TabsetHandler<ITabMovedListener>(ITabMovedListener.class);
			
			if (!bTabListenerAdded)
			{
				resource.addTabListener(this);
				
				bTabListenerAdded = true;
			}
		}
		
		return eventTabMoved;
	}

	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabActivatedListener> eventTabActivated()
	{
		if (eventTabActivated == null)
		{
			eventTabActivated = new TabsetHandler<ITabActivatedListener>(ITabActivatedListener.class);
			
			if (!bTabListenerAdded)
			{
				resource.addTabListener(this);
				
				bTabListenerAdded = true;
			}
		}
		
		return eventTabActivated;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabDeactivatedListener> eventTabDeactivated()
	{
		if (eventTabDeactivated == null)
		{
			eventTabDeactivated = new TabsetHandler<ITabDeactivatedListener>(ITabDeactivatedListener.class);
			
			if (!bTabListenerAdded)
			{
				resource.addTabListener(this);
				
				bTabListenerAdded = true;
			}
		}
		
		return eventTabDeactivated;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayout(ILayout pLayout)
	{
		// Do not set any layout to a tabset panel!
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pIndex < 0)
		{
			images.add(null);
		}
		else
		{
			images.add(pIndex, null);
		}
		
		super.add(pComponent, pConstraints, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		super.remove(pIndex);

		images.remove(pIndex);
	}
	
}	// SwingTabsetPanel
