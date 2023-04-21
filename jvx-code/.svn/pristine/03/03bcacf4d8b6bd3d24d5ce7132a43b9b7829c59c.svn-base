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
 * 20.03.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IInsets;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;

import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;

/**
 * The <code>VaadinPopupMenuButton</code> is the {@link IPopupMenuButton} implementation for 
 * vaadin.
 * 
 * @author René Jahn
 * @see IPopupMenuButton
 */
public class VaadinPopupMenuButton extends AbstractVaadinLabeledIcon<WrappedPopupMenuButton>
                                   implements IPopupMenuButton
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Margin for the component. **/
	private IInsets insMargins = new VaadinInsets(0, 0, 0, 0);
	
	/** the vertical text position. */
	private int iVerticalPosition;
	
	/** the horizontal text position. */
	private int iHorizontalPosition;
	
	/** the image text gap. */
	private int iImageTextGap = 5;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinPopupMenuButton</code>.
     */
    public VaadinPopupMenuButton()
    {
        super(new WrappedPopupMenuButton());
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void setPopupMenu(IPopupMenu pMenu)
    {
        resource.setPopupMenu(pMenu);
    }
    
    /**
     * {@inheritDoc}
     */
    public IPopupMenu getPopupMenu()
    {
        return resource.getPopupMenu();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDefaultMenuItem(IMenuItem pItem)
    {
        resource.setDefaultMenuItem(pItem);
    }
    
    /**
     * {@inheritDoc}
     */
    public IMenuItem getDefaultMenuItem()
    {
        return resource.getDefaultMenuItem();
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
		return insMargins;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		CssExtension csse = getCssExtension();
		
		csse.addAttribute("padding-top", pMargins.getTop() + "px");
		csse.addAttribute("padding-left", pMargins.getLeft() + "px");
		csse.addAttribute("padding-right", pMargins.getRight() + "px");
		csse.addAttribute("padding-bottom", pMargins.getBottom() + "px");			
		
		insMargins = pMargins;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalPosition)
	{
		iVerticalPosition = pVerticalPosition;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
	{
		return iVerticalPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
	{
		iHorizontalPosition = pHorizontalPosition;
	}	

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
	{
		return iHorizontalPosition;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setImageTextGap(int pImageTextGap)
    {
    	iImageTextGap = pImageTextGap;
    }

    /**
	 * {@inheritDoc}
	 */
    public int getImageTextGap()
    {
    	return iImageTextGap;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public CssExtension getCssExtension() 
    {
    	return resource.getCssExtension();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocusable(boolean pFocusable)
    {
        super.setFocusable(pFocusable);
        
        if (isFocusable())
        {
            Integer tabIndex = getTabIndex();
            
            resource.setTabIndex(tabIndex == null ? 0 : tabIndex.intValue());
        }
        else
        {
            resource.setTabIndex(-2);
        }
    }
    
}   // VaadinPopupMenuButton
