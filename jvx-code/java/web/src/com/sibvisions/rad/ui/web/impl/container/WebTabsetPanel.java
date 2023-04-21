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
 * 20.11.2009 - [HM] - creation
 * 25.07.2013 - [JR] - #732: just for compiler 
 * 01.08.2013 - [JR] - #732: close/activate/deactivate events implemented
 */
package com.sibvisions.rad.ui.web.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.rad.ui.web.impl.WebContainer;
import com.sibvisions.rad.ui.web.impl.WebImage;
import com.sibvisions.rad.ui.web.impl.WebResource;
import com.sibvisions.util.ArrayUtil;

/**
 * Web server implementation of {@link ITabsetPanel}.
 * 
 * @author Martin Handsteiner
 */
public class WebTabsetPanel extends WebContainer
                            implements ITabsetPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** images of the tabs. */
	private ArrayUtil<TabInfo> tabInfos = new ArrayUtil<TabInfo>();

	/** the EventHandler for tabClosed. */
	private TabsetHandler<ITabClosedListener>       eventTabClosed      = null;
	/** the EventHandler for tabMoved. */
	private TabsetHandler<ITabMovedListener>        eventTabMoved       = null;
	/** the EventHandler for tabActivated. */
	private TabsetHandler<ITabActivatedListener>    eventTabActivated   = null;
	/** the EventHandler for tabDeactivated. */
	private TabsetHandler<ITabDeactivatedListener>  eventTabDeactivated = null;
	
	/** the last closed tab. */
	private int iLastClosedTab = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebTabsetPanel</code>.
     *
     * @see ITabsetPanel
     */
	public WebTabsetPanel()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setTabPlacement(int pTabPlacement)
    {
		setProperty("tabPlacement", Integer.valueOf(pTabPlacement));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabPlacement()
    {
    	return ((Integer)getProperty("tabPlacement", Integer.valueOf(PLACEMENT_TOP))).intValue();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setEnabledAt(int pIndex, boolean pEnabled)
    {
		tabInfos.get(pIndex).enabled = pEnabled;
		
		setComponentConstraints(pIndex);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabledAt(int pIndex)
    {
    	return tabInfos.get(pIndex).enabled;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabLayoutPolicy(int pTabLayoutPolicy)
    {
		setProperty("tabLayoutPolicy", Integer.valueOf(pTabLayoutPolicy));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabLayoutPolicy()
    {
    	return ((Integer)getProperty("tabLayoutPolicy", Integer.valueOf(ITabsetPanel.TAB_LAYOUT_WRAP))).intValue();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelectedIndex(int pIndex)
    {
		if (pIndex > tabInfos.size() - 1)
		{
			throw new IndexOutOfBoundsException("Index: " + pIndex + ", Tab count: " + tabInfos.size());
		}
		
		int iOldIndex = getSelectedIndex();

		if (eventTabDeactivated != null && iOldIndex >= 0)
		{
			getFactory().synchronizedDispatchEvent(eventTabDeactivated, new UITabsetEvent(getEventSource(), 
					                                            						  UITabsetEvent.TABSET_DEACTIVATED, 
					                                            						  System.currentTimeMillis(),
					                                            						  0,
					                                            						  iOldIndex,
					                                            						  iOldIndex));
		}
		
		setSelectedIndexIntern(pIndex);
		
		if (eventTabActivated != null)
		{
			getFactory().synchronizedDispatchEvent(eventTabActivated, new UITabsetEvent(getEventSource(), 
					                                          							UITabsetEvent.TABSET_ACTIVATED, 
					                                          							System.currentTimeMillis(),
					                                          							0,
					                                          							iOldIndex,
					                                          							getSelectedIndex()));
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getSelectedIndex()
    {
    	return ((Integer)getProperty("selectedIndex", Integer.valueOf(-1))).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public IImage getIconAt(int pIndex)
	{
		return tabInfos.get(pIndex).image;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setIconAt(int pIndex, IImage pImage)
	{
		tabInfos.get(pIndex).image = WebImage.createScaledImage((WebImage)pImage);
		
		setComponentConstraints(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setClosableAt(int pIndex, boolean pClosable)
    {
		tabInfos.get(pIndex).closable = pClosable;

		setComponentConstraints(pIndex);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosableAt(int pIndex)
    {
    	return tabInfos.get(pIndex).closable;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setDraggable(boolean pDraggable)
    {
    	setProperty("draggable", Boolean.valueOf(pDraggable));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDraggable()
    {
    	return ((Boolean)getProperty("draggable", Boolean.FALSE)).booleanValue();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setTextAt(int pIndex, String pText)
	{
		tabInfos.get(pIndex).text = pText;

		setComponentConstraints(pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTextAt(int pIndex)
	{
		return tabInfos.get(pIndex).text;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pNavigationKeysEnabled)
	{
		setProperty("navigationKeysEnabled", Boolean.valueOf(pNavigationKeysEnabled));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return ((Boolean)getProperty("navigationKeysEnabled", Boolean.FALSE)).booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabClosedListener> eventTabClosed()
	{
		if (eventTabClosed == null)
		{
			eventTabClosed = new TabsetHandler<ITabClosedListener>(ITabClosedListener.class);
			
			setProperty("tabClosed", eventTabClosed);
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

			setProperty("tabMoved", eventTabMoved);
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
			
			setProperty("tabActivated", eventTabActivated);
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
			
			setProperty("tabDeactivated", eventTabDeactivated);
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
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		iLastClosedTab = -1;
		
		super.add(pComponent, pConstraints, pIndex);
		
		TabInfo tabInfo = new TabInfo();
		if (pConstraints instanceof String)
		{
			tabInfo.text = (String)pConstraints;
		}
		
		((WebComponent)pComponent).setConstraints(tabInfo);

		if (pIndex >= 0 && pIndex < tabInfos.size())
		{
			tabInfos.add(pIndex, tabInfo);
		}
		else if (pIndex < 0)
		{
			tabInfos.add(tabInfo);
		}
		
		if (tabInfos.size() == 1)
		{
			setSelectedIndex(0);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		int oldSelectedIndex = getSelectedIndex();
		
		// The newSelectedIndex is hopefully calculated correctly, anyway better than newIndex = oldIndex
		int newSelectedIndex = oldSelectedIndex;

		//delete a tab before the current selection: -1
		if (pIndex < oldSelectedIndex)
		{
			newSelectedIndex--;
		}
		else if (pIndex == oldSelectedIndex)
		{
			newSelectedIndex = getPossibleIndexAfterRemove(newSelectedIndex);
		}

		if (eventTabDeactivated != null && oldSelectedIndex >= pIndex) // Even if newSelectedIndex == oldSelectedIndex, due to remove, the tab behind is changed.
		{
			getFactory().synchronizedDispatchEvent(eventTabDeactivated, new UITabsetEvent(getEventSource(), 
																						  UITabsetEvent.TABSET_DEACTIVATED, 
																						  System.currentTimeMillis(),
																						  0,
																						  oldSelectedIndex,
																						  newSelectedIndex));
		}
		
		super.remove(pIndex);
		
		if (pIndex < tabInfos.size())
		{
			tabInfos.remove(pIndex);
		}

		if (tabInfos.isEmpty())
		{
			setSelectedIndexIntern(-1);
		}
		else
		{
			newSelectedIndex = getPossibleIndexAfterRemove(newSelectedIndex);
			
			if (newSelectedIndex != oldSelectedIndex)
			{
				setSelectedIndexIntern(newSelectedIndex);
			}
		}
		
		if (eventTabActivated != null && oldSelectedIndex >= pIndex)
		{
			getFactory().synchronizedDispatchEvent(eventTabActivated, new UITabsetEvent(getEventSource(), 
																					    UITabsetEvent.TABSET_DEACTIVATED, 
																					    System.currentTimeMillis(),
																					    0,
																					    oldSelectedIndex,
																					    getSelectedIndex()));
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Use-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the component constraints after changing TabInfo.
	 * @param pIndex the index.
	 */
	private void setComponentConstraints(int pIndex)
	{
		WebComponent component = getComponent(pIndex);
		component.setConstraints(null);
		component.setConstraints(tabInfos.get(pIndex));
		
		setCommandProperty("_constraintsChanged", Boolean.TRUE);
	}

	/**
	 * Closes a tab.
	 * 
	 * @param pIndex the index
	 */
	public void closeTabAt(int pIndex)
	{
		if (eventTabDeactivated != null)
		{
			getFactory().synchronizedDispatchEvent(eventTabDeactivated, new UITabsetEvent(getEventSource(), 
																						  UITabsetEvent.TABSET_DEACTIVATED, 
																						  System.currentTimeMillis(),
																						  0,
																						  pIndex,
																						  -1));
		}

		if (eventTabClosed != null)
		{
			getFactory().synchronizedDispatchEvent(eventTabClosed, new UITabsetEvent(getEventSource(), 
																					 UITabsetEvent.TABSET_CLOSED, 
																					 System.currentTimeMillis(),
																					 0,
																					 pIndex,
																					 pIndex));
		}
		
		iLastClosedTab = pIndex;
		
		remove(pIndex);
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
		
		int iTabCount = tabInfos.size();
		
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

	/**
	 * Sets the selected index property.
	 * 
	 * @param pIndex the index
	 */
	private void setSelectedIndexIntern(int pIndex)
	{
		setProperty("selectedIndex", Integer.valueOf(pIndex));
	}
	
	/**
	 * Gets the index of the last closed tab. The index will be set to -1
	 * automatically.
	 * 
	 * @return the last closed tab index or <code>-1</code> if no tab was closed
	 */
	public int getAndResetLastClosedIndex()
	{
		int iIndex = iLastClosedTab;
		
		iLastClosedTab = -1;
		
		return iIndex;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Stores Informations to a tab.
	 */
	private class TabInfo extends WebResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the tab image. */
		protected WebImage image = null;
		/** the tab image. */
		protected String text = null;
		/** the tab closable flag. */
		protected boolean closable = false;
		/** the tab enabled flag. */
		protected boolean enabled = true;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getAsString()
		{
			return enabled + ";" + closable + ";" + text + (image == null ? "" : ";" + image.getAsString());
		}
		
	}	// TabInfo
	
}	// WebTabsetPanel
