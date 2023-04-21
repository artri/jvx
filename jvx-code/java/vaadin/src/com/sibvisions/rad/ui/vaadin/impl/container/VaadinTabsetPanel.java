/*
 * Copyright 2012 SIB Visions GmbH
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
 * 23.10.2012 - [CB] - creation
 * 25.07.2013 - [JR] - #732: tab activated/deactivated events implemented
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.vaadin.shared.ui.tabsheet.TabsheetClientRpc;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents.ComponentDetachEvent;
import com.vaadin.ui.HasComponents.ComponentDetachListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * The <code>VaadinTabsetPanel</code> class is the vaadin implementation of
 * {@link ITabsetPanel}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinTabsetPanel extends VaadinContainer<TabSheet>
		                       implements ITabsetPanel,
		                                  CloseHandler,
		                                  SelectedTabChangeListener,
		                                  ComponentDetachListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** images of the tabs. */
	private ArrayList<IImage> auImages = new ArrayList<IImage>();
	
	/** the last selected tab. */
	private WeakReference<Tab> wrLastTab;
	
	/** the EventHandler for tabClosed. */
	private TabsetHandler<ITabClosedListener> eventTabClosed = null;
	
	/** the EventHandler for tabMoved. */
	private TabsetHandler<ITabMovedListener> eventTabMoved = null;
	
	/** the EventHandler for tabActivated. */
	private TabsetHandler<ITabActivatedListener> eventTabActivated = null;
	
	/** the EventHandler for tabDeactivated. */
	private TabsetHandler<ITabDeactivatedListener> eventTabDeactivated = null;
	
	/** whether the tab selection changed listener was added. */
	private boolean bSelectionListenerAdded = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinTabsetPanel</code>.
	 *
	 * @see ITabsetPanel
	 */
	public VaadinTabsetPanel()
	{
		super(new TabSheet()
        {
		    public void setSelectedTab(Component pComponent, boolean pUserOriginated) 
		    {
		        if (pComponent == null || pComponent.equals(getSelectedTab()))
		        {
		            // Inform the client in any case, to ensure, the flag, to ignore mouse clicks is restored!
		            getRpcProxy(TabsheetClientRpc.class).revertToSharedStateSelection();
		        }
		        else
		        {
		            super.setSelectedTab(pComponent, pUserOriginated);
		        }
		    }
        });
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabPlacement(int pTabPlacement)
	{
		//Not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabPlacement()
	{
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setEnabledAt(int pIndex, boolean pEnabled)
	{
		resource.getTab(pIndex).setEnabled(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabledAt(int pIndex)
	{
		return resource.getTab(pIndex).isEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabLayoutPolicy(int pTabLayoutPolicy)
	{
		//Not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabLayoutPolicy()
	{
		return TAB_LAYOUT_SCROLL;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelectedIndex(int pSelectedIndex)
	{
		resource.setSelectedTab(pSelectedIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getSelectedIndex()
	{
		return resource.getTabPosition(resource.getTab(resource.getSelectedTab()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getIconAt(int pIndex)
	{
		return auImages.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setIconAt(int pIndex, IImage pImage)
	{
		if (pImage == null)
		{
			resource.getTab(pIndex).setIcon(null);
		}
		else
		{
			resource.getTab(pIndex).setIcon(((VaadinImage)pImage).getResource());
		}
		
		auImages.set(pIndex, pImage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setClosableAt(int pIndex, boolean pClosable)
	{
		resource.getTab(pIndex).setClosable(pClosable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosableAt(int pIndex)
	{
		return resource.getTab(pIndex).isClosable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDraggable(boolean pDraggable)
	{
		//Not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDraggable()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTextAt(int pIndex, String pText)
	{
		resource.getTab(pIndex).setCaption(pText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTextAt(int pIndex)
	{
		return resource.getTab(pIndex).getCaption();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pNavigationKeysEnabled)
	{
		//Not supported
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabClosedListener> eventTabClosed()
	{
		if (eventTabClosed == null)
		{
			eventTabClosed = new TabsetHandler<ITabClosedListener>(ITabClosedListener.class);
			
			resource.setCloseHandler(this);
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
			
			if (!bSelectionListenerAdded)
			{
				resource.addSelectedTabChangeListener(this);
				
				bSelectionListenerAdded = true;
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
			
			resource.addComponentDetachListener(this);
			
			if (!bSelectionListenerAdded)
			{
				resource.addSelectedTabChangeListener(this);
				
				bSelectionListenerAdded = true;
			}
		}
		
		return eventTabDeactivated;
	}
	
	//Listeners
	
	/**
	 * {@inheritDoc}
	 */
	public void onTabClose(TabSheet pTabsheet, Component pTabContent)
	{
		if (eventTabClosed != null)
		{
			int index = resource.getTabPosition(resource.getTab(pTabContent));
		
			pTabsheet.removeComponent(pTabContent);
			
			getFactory().synchronizedDispatchEvent(eventTabClosed,
					                               new UITabsetEvent(eventSource,
                                        							 UITabsetEvent.TABSET_CLOSED,
                                        							 System.currentTimeMillis(),
                                        							 0,
                                        							 index,
                                        							 index));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void componentDetachedFromContainer(ComponentDetachEvent pEvent)
	{
		Component comp = pEvent.getComponent();
		
		//the component is removed, but is still in the list of tabs.
		//It's not possible to send deactivated in selectedTabChange, because it's already removed from the tab list
		
		Tab tab = resource.getTab(comp);
		
		if (tab != null)
		{
			int index = resource.getTabPosition(tab);
			
			if (eventTabDeactivated != null)
			{
				if (index >= 0)
				{
					getFactory().synchronizedDispatchEvent(
							eventTabDeactivated,
							new UITabsetEvent(
									eventSource,
									UITabsetEvent.TABSET_DEACTIVATED,
									System.currentTimeMillis(),
									0,
									index,
									index));
				}
			}
		}
		
		wrLastTab = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void selectedTabChange(SelectedTabChangeEvent pEvent)
	{
		int iOldIndex = -1;
		
		//works as long as the tab wasn't removed
		if (wrLastTab != null)
		{
			Tab tab = wrLastTab.get();
			
			if (tab != null)
			{
				iOldIndex = resource.getTabPosition(tab);
				
				if (eventTabDeactivated != null)
				{
					if (iOldIndex >= 0)
					{
						getFactory().synchronizedDispatchEvent(
								eventTabDeactivated,
								new UITabsetEvent(
										eventSource,
										UITabsetEvent.TABSET_DEACTIVATED,
										System.currentTimeMillis(),
										0,
										iOldIndex,
										iOldIndex));
					}
				}
			}
		}
		
		Component compSelected = resource.getSelectedTab();
		
		if (compSelected != null)
		{
			Tab tab = resource.getTab(compSelected);
			
			if (tab != null)
			{
				wrLastTab = new WeakReference<Tab>(tab);
				
				if (eventTabActivated != null)
				{
					int index = resource.getTabPosition(tab);
					
					if (index >= 0)
					{
						getFactory().synchronizedDispatchEvent(
								eventTabActivated,
								new UITabsetEvent(
										eventSource,
										UITabsetEvent.TABSET_ACTIVATED,
										System.currentTimeMillis(),
										0,
										iOldIndex,
										index));
					}
				}
			}
			else
			{
				wrLastTab = null;
			}
		}
		else
		{
			wrLastTab = null;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * {@inheritDoc}
     */
    public void setForeground(IColor pForeground)
    {
        foreground = pForeground;
                
        if (foreground != null) 
        {
            CssExtensionAttribute attribute = new CssExtensionAttribute("color", ((VaadinColor)pForeground).getStyleValueRGB(), 
                                                                        "v-tabsheet-caption", CssExtensionAttribute.SEARCH_DOWN);
            attribute.setExactMatch(true);
            attribute.setMultipleMatch(true);
            
            getCssExtension().addAttribute(attribute);  
        }
        else
        {
            CssExtensionAttribute attribute = new CssExtensionAttribute("color", null, 
                                                                        "v-tabsheet-caption", CssExtensionAttribute.SEARCH_DOWN);
            attribute.setExactMatch(true);
            attribute.setMultipleMatch(true);
            
            getCssExtension().removeAttribute(attribute);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText()
	{
		return resource.getDescription();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		super.setParent(pParent);
		
		fixChildrenSizes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performLayout()
	{
		super.performLayout();
		
		fixChildrenSizes();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pIndex >= 0 && pIndex < resource.getComponentCount())
		{
			auImages.add(pIndex, null);
			resource.addTab((Component)pComponent.getResource(), null, null, pIndex).setCaption((String)pConstraints);
		}
		else
		{
			auImages.add(null);
			resource.addTab((Component)pComponent.getResource(), resource.getComponentCount()).setCaption((String)pConstraints);
		}
        initTabStyles();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromVaadin(IComponent pComponent)
	{
		Tab tab = resource.getTab((Component)pComponent.getResource());
		
		if (tab != null)
		{
			int iPos = resource.getTabPosition(tab);
			
			resource.removeTab(tab);
			
			auImages.remove(iPos);
		}
		initTabStyles();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Sets style with tab number on tab.
	 */
	private void initTabStyles()
	{
	    for (int i = 0, count = resource.getComponentCount(); i < count; i++)
	    {
	        resource.getTab(i).setStyleName("" + i);
	    }
        setForeground(foreground);
	}
	
	/**
	 * Returns the tab index for the given component.
	 * 
	 * @param pComponent the component
	 * @return the tab index.
	 */
	public int getTabIndex(IComponent pComponent)
	{
		return resource.getTabPosition(getTab(pComponent));
	}
	
	/**
	 * Returns the vaadin tab for the given component.
	 * 
	 * @param pComponent the component
	 * @return the vaadin tab.
	 */
	public Tab getTab(IComponent pComponent)
	{
		return resource.getTab((Component)pComponent.getResource());
	}

	/**
	 * Fixes the sizes of all children dependent on the current parent.
	 */
	private void fixChildrenSizes() 
	{
		for (IComponent component : getComponents())
		{
			if (VaadinUtil.isWidthUndefined(resource, false))
			{
				((VaadinComponentBase<?, ?>) component).setWidthUndefined();
			}
			else
			{
				((VaadinComponentBase<?, ?>) component).setWidthFull();
			}
			
			if (VaadinUtil.isHeightUndefined(resource, false))
			{
				((VaadinComponentBase<?, ?>) component).setHeightUndefined();
			}
			else
			{
				((VaadinComponentBase<?, ?>) component).setHeightFull();
			}
		}
	}
	
}	// VaadinTabsetPanel
