/*
 * Copyright 2013 SIB Visions GmbH
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
 * 26.02.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.event.ITabsetListener;
import javax.rad.ui.event.UITabsetEvent;

import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;

/**
 * The <code>VaadinDesktopPanel</code> class is the vaadin implementation of {@link IDesktopPanel}.
 * 
 * @author Stefan Wurm
 */
public class VaadinDesktopPanel extends VaadinPanel 
                                implements IDesktopPanel, 
                                           ITabsetListener, 
                                           Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Vaadin TabSheet component. **/
	private VaadinTabsetPanel tabsetPanel;

	/** If tab mode is enabled. **/
	private boolean tabMode = false;
	
	/** A marker for the panel if it is dirty and should be repainted. **/
	private boolean dirty = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinDesktopPanel</code>.
     */
	public VaadinDesktopPanel()
	{
		super();
		
		setLayout(new VaadinClientBorderLayout());
		
		resource.setStyleName("jvxdesktop");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent instanceof VaadinInternalFrame)
		{
			((VaadinInternalFrame)pComponent).getResource().setContent(((VaadinInternalFrame)pComponent).getRootPane().getResource());
		}

		super.addToVaadin(pComponent, pConstraints, pIndex);
		
		markDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromVaadin(IComponent pComponent)
	{
		super.removeFromVaadin(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		dirty = false;
        		
		repaintPanel();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabMode(boolean pTabMode)
	{
		if (tabMode != pTabMode)
		{
			tabMode = pTabMode;
			
			markDirty();
		}		
	}	

	/**
	 * {@inheritDoc}
	 */
	public boolean isTabMode()
	{
		return tabMode;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
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
	public void tabClosed(UITabsetEvent pTabsetEvent)
	{
		// Get the VaadinInternalFrame component
		IComponent component = getComponent(pTabsetEvent.getNewIndex()); 
		
		if (component instanceof VaadinInternalFrame)
		{
			((VaadinInternalFrame)component).performWindowClosing();
		}
		
		markDirty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void tabMoved(UITabsetEvent pTabsetEvent)
	{
		//Not needed
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void tabActivated(UITabsetEvent pTabsetEvent)
	{
		//Not needed
	}

	/**
	 * {@inheritDoc}
	 */
	public void tabDeactivated(UITabsetEvent pTabsetEvent)
	{
		//Not needed
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Mark the layout as dirty and call invokeLater.
	 */
	void markDirty()
	{
		if (!dirty)
		{
			dirty = true;

			getFactory().invokeLater(this);
		}
	}	
	
	/**
	 * Returns the center component from the BorderLayout of the VaadinDesktopPanel.
	 * The component is null if no center component is set.
	 * 
	 * @return the center component. null if no center component is set.
	 */
	private IComponent getCenterComponent()
	{
		for (IComponent component : getComponents())
		{
			if (!(component instanceof VaadinInternalFrame))
			{
				if (((VaadinComponentBase<?, ?>) component).getConstraints() == null 
				        || ((VaadinComponentBase<?, ?>) component).getConstraints().equals(VaadinClientBorderLayout.CENTER))
				{
					return component;
				}
			}
		}
		
		return null;
	}	
	
	/** 
	 * Repaints the Layout. Adds the TabsetPanel into the center, if tabMode = true.
	 */
	private void repaintPanel()
	{
		if (tabMode)
		{
			if (tabsetPanel == null)
			{
				tabsetPanel = (VaadinTabsetPanel) getFactory().createTabsetPanel();
				tabsetPanel.eventTabClosed().addListener(this);
			}
			
			tabsetPanel.getResource().removeAllComponents();
			
			for (IComponent component : getComponents())
			{
				if (component instanceof VaadinInternalFrame)
				{
				    VaadinInternalFrame frame = (VaadinInternalFrame)component; 
				    
				    if (frame.isVisible() || frame.getTab() != null)
				    {
    					VaadinRootPane vaadinRootPane = frame.getRootPane();
    	
    					tabsetPanel.addToVaadin(vaadinRootPane, frame.getTitle(), -1);
    					
    					int index = tabsetPanel.getTabIndex(vaadinRootPane);			
    					tabsetPanel.setIconAt(index, (VaadinImage)frame.getIconImage());				
    					tabsetPanel.setClosableAt(index, frame.isClosable());
    	
                        frame.setVisible(false);
    					frame.setTab(tabsetPanel.getTab(vaadinRootPane));
				    }
				}
			}

			IComponent centerComponent = getCenterComponent();
          
			if (centerComponent != null)
			{
			    super.removeFromVaadin(centerComponent);
			}
			if (tabsetPanel.getResource().getParent() == null)
			{
			    super.addToVaadin(tabsetPanel, VaadinClientBorderLayout.CENTER, -1);
			}
		}
		else
		{
			for (IComponent component : getComponents())
			{
				if (component instanceof VaadinInternalFrame)
				{
                    VaadinInternalFrame frame = (VaadinInternalFrame)component; 
                    
                    if (!frame.isVisible() && frame.getTab() != null)
                    {
                        frame.setTab(null);
                        frame.setVisible(true);		
					
                        frame.getResource().setContent(frame.getRootPane().getResource());
                    }
				}
			}
			
            IComponent centerComponent = getCenterComponent();
            
            if (tabsetPanel != null)
            {
                super.removeFromVaadin(tabsetPanel);
            }
			if (centerComponent != null && ((VaadinComponentBase<?, ?>)centerComponent).getResource().getParent() == null)
			{
				super.addToVaadin(centerComponent, VaadinClientBorderLayout.CENTER, -1);
			}
		}
	}	

}	// VaadinDesktopPanel
