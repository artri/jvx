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
 * 24.10.2012 - [JR] - #604: added constructor
 * 13.08.2013 - [JR] - #756: changed add order
 * 05.10.2013 - [JR] - #826: use add instead of addSeparator
 */
package javax.rad.genui.menu;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.event.PopupMenuHandler;
import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.util.TranslationMap;

/**
 * Platform and technology independent popup menu.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIPopupMenu extends UIContainer<IPopupMenu> 
                         implements IPopupMenu
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIPopupMenu</code>.
     *
     * @see IPopupMenu
     */
	public UIPopupMenu()
	{
		super(UIFactoryManager.getFactory().createPopupMenu());
	}

    /**
     * Creates a new instance of <code>UIPopupMenu</code> with the given
     * popup menu.
     *
     * @param pMenu the popup menu
     * @see IPopupMenu
     */
	protected UIPopupMenu(IPopupMenu pMenu)
	{
		super(pMenu);
	}

	/**
	 * Creates a new instance of {@link UIPopupMenu}.
	 *
	 * @param pMenuItems the {@link IComponent menu items}.
	 * @see #add(IComponent)
	 */
	public UIPopupMenu(IComponent... pMenuItems)
	{
		this();
		
		if (pMenuItems != null && pMenuItems.length > 0)
		{
			for (IComponent menuItem : pMenuItems)
			{
				add(menuItem);
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void addSeparator()
    {
    	addSeparator(-1);
    }

	/**
	 * {@inheritDoc}
	 */
    public void addSeparator(int pIndex)
    {
    	UISeparator separator = new UISeparator();
    	
    	if (pIndex < 0)
    	{
        	components.add(separator);
    	}
    	else
    	{
        	components.add(pIndex, separator);
    	}
    	
    	separator.setParent(this);

    	try
    	{
    		uiResource.add(separator.getUIResource(), null, pIndex);
    	}
    	catch (RuntimeException re)
    	{
    		components.remove(separator);
    		separator.setParent(null);
    		
    		throw re;
    	}
    	catch (Error e)
    	{
    		components.remove(separator);
    		separator.setParent(null);
    		
    		throw e;
    	}
    	
    	if (isNotified() && !separator.isNotified())
    	{
    		separator.addNotify();
    	}    	
    }

	/**
	 * {@inheritDoc}
	 */
    public void show(IComponent pOrigin, int pX, int pY)
    {
        checkTranslation(pOrigin);
        
        if (pOrigin instanceof UIComponent)
        {
        	uiResource.show((IComponent)((UIComponent)pOrigin).getUIResource(), pX, pY);
        }
        else
        {
        	uiResource.show(pOrigin, pX, pY);
        }
    }

	/**
	 * The PopupMenuHandler for the popup menu will become visible event.
	 * 
	 * @return the PopupMenuHandler for the focus gained event.
	 */
	public PopupMenuHandler<IPopupMenuWillBecomeVisibleListener> eventPopupMenuWillBecomeVisible()
	{
		return uiResource.eventPopupMenuWillBecomeVisible();
	}
	
	/**
	 * The PopupMenuHandler for the popup menu will become invisible event.
	 * 
	 * @return the PopupMenuHandler for the popup menu will become invisible event.
	 */
	public PopupMenuHandler<IPopupMenuWillBecomeInvisibleListener> eventPopupMenuWillBecomeInvisible()
	{
		return uiResource.eventPopupMenuWillBecomeInvisible();
	}

	/**
	 * The PopupMenuHandler for the popup menu canceled event.
	 * 
	 * @return the PopupMenuHandler for the popup menu canceled event.
	 */
	public PopupMenuHandler<IPopupMenuCanceledListener> eventPopupMenuCanceled()
	{
		return uiResource.eventPopupMenuCanceled();
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
    	if (pConstraints instanceof String)
    	{
    		IContainer parent = AbstractUIMenuItem.getMenu(this, (String)pConstraints);
        	
    		parent.add(pComponent, null, pIndex);
    	}
    	else
    	{
    		super.add(pComponent, pConstraints, pIndex);
    	}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Checks if the correct translation map is used.
     * 
     * @param pOrigin the base component
     */
    public void checkTranslation(IComponent pOrigin)
    {
        //search the translation because we are not added
        if (pOrigin instanceof UIComponent)
        {
            IComponent comp = pOrigin;
            
            TranslationMap trans = ((UIComponent)comp).getTranslation();

            while (trans == null && comp != null)
            {
                if (comp instanceof UIComponent)
                {
                    trans = ((UIComponent)comp).getTranslation();
                }
                
                comp = comp.getParent();
            }
            
            if (getTranslation() != trans)
            {
                setTranslation(trans);
            }
        }

        if (!isNotified())
        {
            super.addNotify();
        }
    }
    
}	// UIPopupMenu
