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
 * 17.11.2008 - [HM] - creation
 * 26.06.2009 - [JR] - remove: index < size - 1 --> index < size [BUGFIX]
 * 04.08.2009 - [JR] - set/isNavigationKeysEnabled implemented
 * 24.10.2012 - [JR] - #604: added constructor
 * 25.07.2013 - [JR] - #732: eventTabActivated, eventTabDeactivated implemented
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled 
 */
package javax.rad.genui.container;

import java.util.ArrayList;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;

/**
 * Platform and technology independent TabSetPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UITabsetPanel extends UIContainer<ITabsetPanel> 
                           implements ITabsetPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the EventHandler for tabClosed. */
	private transient TabsetHandler<ITabClosedListener> eventTabClosed = null;
	
	/** the EventHandler for tabMoved. */
	private transient TabsetHandler<ITabMovedListener> eventTabMoved = null;
	
	/** images of the tabs. */
	private transient ArrayList<IImage> images = new ArrayList<IImage>();
	
	/** text of the tabs. */
	private transient ArrayList<String> texts = new ArrayList<String>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITabsetPanel</code>.
     *
     * @see ITabsetPanel
     */
	public UITabsetPanel()
	{
		this(UIFactoryManager.getFactory().createTabsetPanel());
	}

    /**
     * Creates a new instance of <code>UITabsetPanel</code> with the given
     * tabset panel.
     *
     * @param pPanel the tabset panel
     * @see ITabsetPanel
     */
	protected UITabsetPanel(ITabsetPanel pPanel)
	{
		super(pPanel);
		
		uiResource.eventTabClosed().addListener(this, "doTabClosed");
		uiResource.eventTabMoved().addListener(this, "doTabMoved");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setTabPlacement(int pPlacement)
    {
    	uiResource.setTabPlacement(pPlacement);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabPlacement()
    {
    	return uiResource.getTabPlacement();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setEnabledAt(int pTabPosition, boolean pEnabled)
    {
    	uiResource.setEnabledAt(pTabPosition, pEnabled);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabledAt(int pTabPosition)
    {
    	return uiResource.isEnabledAt(pTabPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setTabLayoutPolicy(int pLayoutPolicy)
    {
    	uiResource.setTabLayoutPolicy(pLayoutPolicy);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getTabLayoutPolicy()
    {
    	return uiResource.getTabLayoutPolicy();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelectedIndex(int pIndex)
    {
    	uiResource.setSelectedIndex(pIndex);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getSelectedIndex()
    {
    	return uiResource.getSelectedIndex();
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
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
	    if (isNotified() || pIndex < getComponentCount())
	    {
    		if (pImage instanceof UIImage)
    		{
    	    	uiResource.setIconAt(pIndex, ((UIImage)pImage).getUIResource());
    		}
    		else
    		{
    	    	uiResource.setIconAt(pIndex, pImage);
    		}
	    }
	    else
	    {
	        while (pIndex >= images.size())
	        {
	            images.add(null);
	        }
	    }

	    images.set(pIndex, pImage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setClosableAt(int pTabPosition, boolean pClosable)
    {
    	uiResource.setClosableAt(pTabPosition, pClosable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosableAt(int pTabPosition)
    {
    	return uiResource.isClosableAt(pTabPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setDraggable(boolean pDraggable)
    {
    	uiResource.setDraggable(pDraggable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDraggable()
    {
    	return uiResource.isDraggable();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setTextAt(int pIndex, String pText)
	{
	    uiResource.setTextAt(pIndex, translate(pText));
		
		texts.set(pIndex, pText);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTextAt(int pIndex)
	{
		return texts.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		uiResource.setNavigationKeysEnabled(pEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isNavigationKeysEnabled()
	{
		return uiResource.isNavigationKeysEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabClosedListener> eventTabClosed()
	{
		if (eventTabClosed == null)
		{ 
			eventTabClosed = new TabsetHandler<ITabClosedListener>(ITabClosedListener.class);
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
		return uiResource.eventTabActivated();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TabsetHandler<ITabDeactivatedListener> eventTabDeactivated()
	{
		return uiResource.eventTabDeactivated();
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
        int index = indexOf(pComponent);
        if (index >= 0) // remove component already here and not in super.add to simplify code
        {
            remove(index);
            
            if (pIndex > index)
            {
                pIndex--;
            }
        }

        int existingImageIndex = -1; // add text and image to list before super.add, to ensure they are available in an event cause by addNotify
        if (pIndex < 0)
        {
            texts.add((String)pConstraints);
            if (images.size() < texts.size())
            {
                images.add(null);
            }
            else
            {
                existingImageIndex = texts.size() - 1;
            }
        }
        else
        {
            texts.add(pIndex, (String)pConstraints);
            if (images.size() < texts.size())
            {
                images.add(pIndex, null);
            }
            else
            {
                existingImageIndex = pIndex;
            }
        }

        super.add(pComponent, translate((String)pConstraints), pIndex);
        
        if (existingImageIndex >= 0 && existingImageIndex < getComponentCount()) // existing image can only be set afterwards.
        {
            setIconAt(existingImageIndex, images.get(existingImageIndex));
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		super.remove(pIndex);

		images.remove(pIndex);
		texts.remove(pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();

		super.updateTranslation();

		if (bTranslate && bChanged)
		{
			//Update tab texts
			for (int i = 0, anz = texts.size(); i < anz; i++)
			{
				uiResource.setTextAt(i, translate(texts.get(i)));
			}
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notification that a tab was closed.
	 * 
	 * @param pEvent the event from the tabset
	 */
	public void doTabClosed(UITabsetEvent pEvent)
	{
		int iIndex = pEvent.getOldIndex();

		//possible if components were added to resource
		if (images.size() > 0 && texts.size() > 0 && components.size() > 0)
		{
			images.remove(iIndex);
			texts.remove(iIndex);
			components.remove(iIndex);
		}
		
		if (eventTabClosed != null)
		{
			eventTabClosed.dispatchEvent(pEvent);
		}
	}
	
	/**
	 * Notfication that a tab was moved.
	 * 
	 * @param pEvent the event from the tabset
	 */
	public void doTabMoved(UITabsetEvent pEvent)
	{
		int iOld = pEvent.getOldIndex();
		int iNew = pEvent.getNewIndex();

		//possible if components were added to resource
		if (images.size() > 0 && texts.size() > 0 && components.size() > 0)
		{
			UIComponent<?> comp = getComponent(iOld);
			String text  = texts.get(iOld);
			IImage image = images.get(iOld);
			
			images.remove(iOld);
			texts.remove(iOld);
			components.remove(iOld);
			
			components.add(iNew, comp);
			texts.add(iNew, text);
			images.add(iNew, image);
		}
		
		if (eventTabMoved != null)
		{
			eventTabMoved.dispatchEvent(pEvent);
		}
	}

	/**
	 * Selects the tab, if it exists.
	 * @param pIndex the tab position
	 */
	public void setSelectedIndexIfExists(int pIndex)
	{
		if (pIndex < getComponentCount())
		{
			setSelectedIndex(pIndex);
		}
	}
	
	/**
	 * Sets the tab enabled, if it exists.
	 * @param pTabPosition the tab position
	 * @param pEnabled true, if enabled.
	 */
	public void setEnabledAtIfExists(int pTabPosition, boolean pEnabled)
	{
		if (pTabPosition < getComponentCount())
		{
			setEnabledAt(pTabPosition, pEnabled);
		}
	}
	
	/**
	 * Sets the tab text, if it exists.
	 * @param pTabPosition the tab position
	 * @param pText the text
	 */
	public void setTextAtIfExists(int pTabPosition, String pText)
	{
		if (pTabPosition < getComponentCount())
		{
			setTextAt(pTabPosition, pText);
		}
	}
	
	/**
	 * Sets the tab icon, if it exists.
	 * @param pIndex the tab position
	 * @param pImage the image
	 */
	public void setIconAtIfExists(int pIndex, IImage pImage)
	{
		if (pIndex < getComponentCount())
		{
			setIconAt(pIndex, pImage);
		}
	}
	
	/**
	 * Sets the tab closable, if it exists.
	 * @param pTabPosition the tab position
	 * @param pClosable true, if closable
	 */
	public void setClosableAtIfExists(int pTabPosition, boolean pClosable)
	{
		if (pTabPosition < getComponentCount())
		{
			setClosableAt(pTabPosition, pClosable);
		}
	}
	

}	// UITabsetPanel
