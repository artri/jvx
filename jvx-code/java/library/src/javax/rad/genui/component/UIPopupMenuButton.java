/*
 * Copyright 2018 SIB Visions GmbH
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
 * 21.03.2018 - [JR] - creation
 */
package javax.rad.genui.component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIInsets;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.genui.menu.UIPopupMenu;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;

/**
 * Platform and technology independent popup menu button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public class UIPopupMenuButton extends AbstractUILabeledIcon<IPopupMenuButton> 
                               implements IPopupMenuButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the default menu item. */
    private transient IMenuItem miDefault;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIPopupMenuButton</code>.
     *
     * @see IPopupMenuButton
     */
	public UIPopupMenuButton()
	{
		this(UIFactoryManager.getFactory().createPopupMenuButton());
	}

    /**
     * Creates a new instance of <code>UIPopupMenuButton</code> with the given
     * button.
     *
     * @param pButton the button
     * @see IPopupMenuButton
     */
	protected UIPopupMenuButton(IPopupMenuButton pButton)
	{
		super(pButton);
	}	
	
    /**
     * Creates a new instance of <code>UIPopupMenuButton</code>.
     *
     * @param pText the label of the button.
     * @see IPopupMenuButton
     */
	public UIPopupMenuButton(String pText)
	{
		this();
		
		setText(pText);
	}

	/**
	 * Creates a new instance of <code>UIPopupMenuButton</code>.
	 *
	 * @param pText the {@link String text}.
	 * @param pImage the {@link IImage image}.
	 * @see #setImage(IImage)
	 * @see #setText(String)
	 */
	public UIPopupMenuButton(String pText, IImage pImage)
	{
		this();
		
		setText(pText);
		setImage(pImage);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public void setDefaultMenuItem(IMenuItem pItem)
    {
        miDefault = pItem;
        
        if (pItem instanceof UIMenuItem)
        {
        	uiResource.setDefaultMenuItem(((UIMenuItem)pItem).getUIResource());
        }
        else
        {
        	uiResource.setDefaultMenuItem(pItem);
        }
    }

    /**
     * {@inheritDoc}
     */
    public IMenuItem getDefaultMenuItem()
    {
        return miDefault;
    }	
    
	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
    	return uiResource.getMargins();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pMargins instanceof UIInsets)
		{
	    	uiResource.setMargins(((UIInsets)pMargins).getUIResource());
		}
		else
		{
	    	uiResource.setMargins(pMargins);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalPosition)
    {
    	uiResource.setVerticalTextPosition(pVerticalPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
    {
    	return uiResource.getVerticalTextPosition();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
    {
    	uiResource.setHorizontalTextPosition(pHorizontalPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
    {
    	return uiResource.getHorizontalTextPosition();
    }
	
	/**
	 * {@inheritDoc}
	 */
    public int getImageTextGap()
    {
    	return uiResource.getImageTextGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setImageTextGap(int pImageTextGap)
    {
    	uiResource.setImageTextGap(pImageTextGap);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPopupMenu(IPopupMenu pMenu)
    {
        if (popupMenu instanceof UIPopupMenu)
        {
            ((UIPopupMenu)popupMenu).setTranslation(null);
        }
        
        popupMenu = pMenu;
        
        if (popupMenu instanceof UIPopupMenu)
        {
            uiResource.setPopupMenu(((UIPopupMenu)popupMenu).getUIResource());
            
            ((UIPopupMenu)popupMenu).checkTranslation(this);
        }
        else
        {
            uiResource.setPopupMenu(popupMenu);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doTriggerPopMenu(UIMouseEvent pMouseEvent)
    {
        //won't work
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify()
    {
        super.addNotify();

        if (popupMenu instanceof UIPopupMenu)
        {
            ((UIPopupMenu)popupMenu).checkTranslation(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify()
    {
        super.removeNotify();

        if (popupMenu instanceof UIPopupMenu)
        {
            ((UIPopupMenu)popupMenu).setTranslation(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void updateTranslation()
	{
		super.updateTranslation();
		
        if (popupMenu instanceof UIPopupMenu)
        {
            ((UIPopupMenu)this.popupMenu).checkTranslation(this);
        }
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Sets the margins with primitive types.
	 * 
	 * @param pTop the top insets.
	 * @param pLeft the left insets.
	 * @param pBottom the bottom insets.
	 * @param pRight the right insets.
	 */
    public void setMargins(int pTop, int pLeft, int pBottom, int pRight)
    {
    	uiResource.setMargins(getFactory().createInsets(pTop, pLeft, pBottom, pRight));
    }    
    
}	// UIPopupMenuButton
