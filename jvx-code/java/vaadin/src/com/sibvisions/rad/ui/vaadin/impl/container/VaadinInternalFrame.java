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
 * 17.10.2012 - [CB] - creation
 * 26.02.2013 - [SW] - implementation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.event.UIWindowEvent;

import com.sibvisions.rad.ui.vaadin.ext.ui.InternalFrame;
import com.sibvisions.rad.ui.vaadin.impl.VaadinDimension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.vaadin.ui.TabSheet.Tab;

/**
 * The <code>VaadinInternalFrame</code> class is the vaadin implementation of {@link IInternalFrame}.
 * 
 * @author Benedikt Cermak
 * @author Stefan Wurm
 */
public class VaadinInternalFrame extends VaadinFrame<InternalFrame> 
                                 implements IInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** The desktop panel, where to display this internal frame. */
	private VaadinDesktopPanel desktopPanel;

	/** The TabSheet.Tab component for the tab mode. **/
	protected Tab tab;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinInternalFrame</code>.
     *
     * @param pDesktopPanel the associated desktop for the internal frame
     * @see javax.rad.ui.container.IDesktopPanel
     */
	public VaadinInternalFrame(IDesktopPanel pDesktopPanel)
	{
		super(new InternalFrame());
	
		desktopPanel = (VaadinDesktopPanel)pDesktopPanel;

		setContent(new VaadinPanel());

		resource.center();
		resource.setContent(getRootPane().getResource());
		
		addInternStyleName("v-icon-16");
		
		setSize(new VaadinDimension(320, 130));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setMaximizable(boolean pMaximizable)
	{
        resource.setMaximizable(pMaximizable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximizable()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setClosable(boolean pClosable)
	{
		resource.setClosable(pClosable);
		
		if (tab != null)
		{
			tab.setClosable(pClosable);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isClosable()
	{
		return resource.isClosable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIconifiable(boolean pIconifiable)
	{
		//Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isIconifiable()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close()
	{
	    //windowClosing event will be fired in VaadinUI.removeWindow(...) triggered from close()
		resource.close();
		
		if (resource.getParent() == null)
		{
            IContainer con = getParent();
            
            if (con != null)
            {
                con.remove(this);
            }
		}
		
		//Important here, because windowClose event is user-triggered (via UI close button (x))
		if (eventWindowClosed != null)
		{
            getFactory().synchronizedDispatchEvent(eventWindowClosed, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_CLOSED,
                                                                                        System.currentTimeMillis(), 0));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isClosed()
	{
		return getParent() == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setModal(boolean pModal)
	{
		resource.setModal(pModal);

		// reinitialize the desktop panel.
		desktopPanel.setTabMode(desktopPanel.isTabMode());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isModal()
	{
		return resource.isModal();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

    /**
     * {@inheritDoc}
     */
    @Override
    public void pack()
    {
        resource.pack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(IDimension pSize)
    {
        super.setSize(pSize);
        
        if (pSize != null)
        {
            resource.undoPack();
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
	    if (pVisible != isVisible())
	    {
    		super.setVisible(pVisible);

    		if (desktopPanel != null)
    		{
    		    desktopPanel.markDirty();
    		}
    		
    		if (pVisible)
    		{
    			requestFirstFocusableComponentFocus(this);
    		}
        }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String pTitle)
	{
		super.setTitle(pTitle);
		
		if (tab != null)
		{
			tab.setCaption(pTitle);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconImage(IImage pIconImage)
	{
		super.setIconImage(pIconImage);
		
		if (tab != null)
		{
			if (pIconImage != null && pIconImage.getResource() != null)
			{
				tab.setIcon(((VaadinImage) pIconImage).getResource());
			}
			else
			{
				tab.setIcon(null);
			}
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose()
    {
        if (!isDisposed())
        {
            super.dispose();

            //doesn't trigger window closing event
            if (resource.getParent() != null)
            {
                getFactory().getUI().removeWindow(resource, this);
            }
            
            IContainer con = getParent();
            
            if (con != null)
            {
                con.remove(this);
            }
            
            //Important here, because windowClose event is user-triggered (via UI close button (x))
            if (eventWindowClosed != null)
            {
                getFactory().synchronizedDispatchEvent(eventWindowClosed, new UIWindowEvent(eventSource, UIWindowEvent.WINDOW_CLOSED,
                                                                                            System.currentTimeMillis(), 0));
            }
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{		
		getRootPane().addComponentToContent(pComponent, pConstraints, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromVaadin(IComponent pComponent)
	{
		getRootPane().removeComponentFromContent(pComponent);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(IPoint pLocation)
    {
        super.setLocation(pLocation);
        
        resource.setPositionX(pLocation.getX());
        resource.setPositionY(pLocation.getY());
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Returns the TabSheet.Tab component.
	 * 
	 * @return the Tab component.
	 */
	Tab getTab()
	{
		return tab;
	}

	/**
	 * Sets the TabSheet.Tab component.
	 * 
	 * @param pTab the tab component.
	 */
	void setTab(Tab pTab)
	{
		tab = pTab;
	}

	/**
	 * Gives the focus to the first relevant component in Screen.
	 * 
	 * @param pComponent the component.
	 */
	public static void requestFirstFocusableComponentFocus(IComponent pComponent)
	{
		ArrayList<IComponent> components = new ArrayList<IComponent>();

		fillInComponents(pComponent, components);
		
		if (components.size() > 0)
		{
			components.get(0).requestFocus();
		}
	}
	
	/**
	 * Fills all focusable components.
	 * @param pComponent the root component.
	 * @param pComponents the list.
	 */
	private static void fillInComponents(IComponent pComponent, List<IComponent> pComponents)
	{
		if (pComponent.isVisible())
		{
			boolean isContainer = pComponent instanceof IContainer;
			
			if (pComponent.isFocusable() && pComponent.isEnabled() && !isContainer)
			{
				Integer tabIndex = pComponent.getTabIndex();
				if (tabIndex == null || pComponents.size() == 0)
				{
					pComponents.add(pComponent);
				}
				else
				{
					int pos = 0;
					Integer curTabIndex = pComponents.get(pos).getTabIndex();
					while (curTabIndex != null && curTabIndex.intValue() <= tabIndex.intValue())
					{
						pos++;
						curTabIndex = (pos < pComponents.size()) ? pComponents.get(pos).getTabIndex() : null;
					}
					pComponents.add(pos, pComponent);
				}
			}
			
			if (isContainer)
			{
				IContainer container = (IContainer)pComponent;
				
				for (int i = 0, count = container.getComponentCount(); i < count; i++)
				{
					fillInComponents(container.getComponent(i), pComponents);
				}
			}
		}
	}

}	// VaadinInternalFrame
