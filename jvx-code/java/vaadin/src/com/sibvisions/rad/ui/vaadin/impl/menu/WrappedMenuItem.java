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
 * 18.10.2012 - [CB] - creation
 * 18.07.2013 - [JR] - forward description to menu item
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IImage;

import com.sibvisions.rad.ui.vaadin.ext.INamedResource;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar;

/**
 * The <code>WrappedMenuItem</code> is an {@link AbstractComponent} that holds a
 * {@link com.vaadin.ui.MenuBar.MenuItem}.
 * 
 * @author Benedikt Cermak
 * @see com.vaadin.ui.MenuBar.MenuItem
 */
public class WrappedMenuItem extends AbstractComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The sText from the menu item. **/
	private String sText = "";
	
	/** the original style name. */
	private String sOriginalStyle;
	
	/** additional style names. */
	private List<String> liStyles;
	
	/** the menu item. */
	private Object menuItem = null;
	
	/** whether this item is a checked checkbox. */
	private boolean bChecked = false;
	
	/** whether this item is visible. */
	private boolean bVisible = true;
	
	/** whether this item is enabled. */
	private boolean bEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WrappedMenuItem</code>.
	 * 
	 * @see com.vaadin.ui.MenuBar.MenuItem
	 */
	public WrappedMenuItem() 
	{
		super();
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible()
	{
		return bVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		bVisible = pVisible;

		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				((MenuBar.MenuItem)menuItem).setVisible(pVisible);
			}
			else
			{
				((com.vaadin.contextmenu.MenuItem)menuItem).setVisible(pVisible);
			}
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnabled)
	{
		bEnabled = pEnabled;
		
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				((MenuBar.MenuItem)menuItem).setEnabled(pEnabled);
			}
			else
			{
				((com.vaadin.contextmenu.MenuItem)menuItem).setEnabled(pEnabled);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		return bEnabled;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(String pText)
	{
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				((MenuBar.MenuItem)menuItem).setDescription(pText);
			}
			else
			{
				((com.vaadin.contextmenu.MenuItem)menuItem).setDescription(pText);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription()
	{
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				return ((MenuBar.MenuItem)menuItem).getDescription();
			}
			else
			{
				return ((com.vaadin.contextmenu.MenuItem)menuItem).getDescription();
			}
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStyleName()
	{
	    if (menuItem != null)
        {
            if (menuItem instanceof MenuBar.MenuItem)
            {
                return ((MenuBar.MenuItem)menuItem).getStyleName();
            }
            else
            {
            	return ((com.vaadin.contextmenu.MenuItem)menuItem).getStyleName();
            }
        }
        
        return super.getStyleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setStyleName(String pStyleName)
    {
		sOriginalStyle = pStyleName;
		
        super.setStyleName(pStyleName);

        if (menuItem != null)
        {
            if (menuItem instanceof MenuBar.MenuItem)
            {
                ((MenuBar.MenuItem)menuItem).setStyleName(pStyleName);
            }
            else
            {
            	((com.vaadin.contextmenu.MenuItem)menuItem).setStyleName(pStyleName);
            }
            
            updateStyleName();
        }
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon(Resource pIcon)
    {
        super.setIcon(pIcon);

        if (menuItem != null)
        {
            if (menuItem instanceof MenuBar.MenuItem)
            {
                ((MenuBar.MenuItem)menuItem).setIcon(pIcon);
            }
            else
            {
            	((com.vaadin.contextmenu.MenuItem)menuItem).setIcon(pIcon);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStyleName(String pStyle)
    {
    	super.addStyleName(pStyle);
    	
    	addStyleNameIntern(pStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeStyleName(String pStyle)
    {
    	super.removeStyleName(pStyle);
    	
    	removeStyleNameIntern(pStyle);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Returns the menu item.
	 * 
	 * @return the item
	 * @param <T> the expected return type
	 */
	public <T> T getMenuItem()
	{
		return (T)menuItem;
	}

	/**
	 * Sets the MenuItem.
	 * 
	 * @param pMenuItem the MenuItem
	 */
	public void setMenuItem(Object pMenuItem)
	{
		if (pMenuItem != null)
		{
			if (pMenuItem instanceof MenuBar.MenuItem)
			{
				sOriginalStyle = ((MenuBar.MenuItem)pMenuItem).getStyleName();
			}
			else
			{
				sOriginalStyle = ((com.vaadin.contextmenu.MenuItem)pMenuItem).getStyleName();
			}
		}
		else
		{
			sOriginalStyle = null;
		}
		
		if (menuItem != null)
		{
			if (menuItem != null)
			{
				Resource icon;
				
				if (menuItem instanceof MenuBar.MenuItem)
				{
					icon = ((MenuBar.MenuItem)menuItem).getIcon(); 
				}
				else
				{
					icon = ((com.vaadin.contextmenu.MenuItem)menuItem).getIcon();
				}
				
				if (icon instanceof INamedResource)
				{
					removeStyleNameIntern(((INamedResource)icon).getStyleName());
				}
			}
		}
		
		menuItem = pMenuItem;
		
		setText(sText);

		if (menuItem != null)
		{
			Resource icon;
			
			if (menuItem instanceof MenuBar.MenuItem)
			{
				icon = ((MenuBar.MenuItem)menuItem).getIcon(); 
			}
			else
			{
				icon = ((com.vaadin.contextmenu.MenuItem)menuItem).getIcon();
			}

			if (icon instanceof INamedResource)
			{
				addStyleNameIntern(((INamedResource)icon).getStyleName());
			}
			else
			{
				updateStyleName();
			}
		}
	}
	
	/**
	 * Gets the item sText.
	 * 
	 * @return the sText
	 */
	public String getText()
    {
		return sText;
    }

	/**
	 * Sets the item sText.
	 * 
	 * @param pText the sText
	 */
	public void setText(String pText)
    {
		sText = pText;
		
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				((MenuBar.MenuItem)menuItem).setText(pText);
			}
			else
			{
				((com.vaadin.contextmenu.MenuItem)menuItem).setText(pText);
			}
		}
    }
	
	/**
	 * Sets the image.
	 * 
	 * @param pImage the image
	 */
    public void setImage(IImage pImage)
    {
    	if (menuItem != null)
    	{
    		VaadinImage image = (VaadinImage)pImage;
    		
    		if (menuItem instanceof MenuBar.MenuItem)
    		{
	    		if (image != null)
	    		{
	    			String styleName = image.getStyleName();
	    			
	    			if (styleName != null)
	    			{
	    				((MenuBar.MenuItem)menuItem).setStyleName(styleName);
	    			}
	    		}  
	    		
	    		if (image == null)
	    		{
	    			((MenuBar.MenuItem)menuItem).setIcon(null);
	    		}
	    		else
	    		{
	    			((MenuBar.MenuItem)menuItem).setIcon(image.getResource());
	    		}
    		}
    		else
    		{
	    		if (image == null)
	    		{
	    			((com.vaadin.contextmenu.MenuItem)menuItem).setIcon(null);
	    		}
	    		else
	    		{
	    			((com.vaadin.contextmenu.MenuItem)menuItem).setIcon(image.getResource());
	    		}
    		}
    	}
    }
    
    /**
     * Sets whether the item is checked.
     * 
     * @param pChecked <code>true</code> to check, <code>false</code> otherwise
     */
    public void setChecked(boolean pChecked)
    {
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				((MenuBar.MenuItem)menuItem).setChecked(pChecked);
			}
			else
			{
				((com.vaadin.contextmenu.MenuItem)menuItem).setChecked(pChecked);
			}
		}
		else
		{
			bChecked = pChecked;
		}
    }
    
    /**
     * Gets whether the item is checked.
     * 
     * @return <code>true</code> if the item is checked, <code>false</code> otherwise
     */
    public boolean isChecked()
    {
		if (menuItem != null)
		{
			if (menuItem instanceof MenuBar.MenuItem)
			{
				return ((MenuBar.MenuItem)menuItem).isChecked();
			}
			else
			{
				return ((com.vaadin.contextmenu.MenuItem)menuItem).isChecked();
			}
		}
		
		return bChecked;
    }
    
    /**
     * Adds a style name to the internal cache.
     * 
     * @param pStyle the style name
     */
    private void addStyleNameIntern(String pStyle)
    {
    	if (liStyles == null)
    	{
    		liStyles = new ArrayList<String>();
    	}
    	    	
    	if (!liStyles.contains(pStyle))
    	{
    		liStyles.add(pStyle);
    	
	    	updateStyleName();
    	}
    }

    /**
     * Removes a style name from the internal cache.
     * 
     * @param pStyle the style name
     */
    private void removeStyleNameIntern(String pStyle)
    {
    	if (liStyles != null)
    	{
    		liStyles.remove(pStyle);
    		
    		if (liStyles.size() == 0)
    		{
    			liStyles = null;
    		}

    		updateStyleName();
    	}
    }
    
    /**
     * Updates the style name with cached information.
     */
    private void updateStyleName()
    {
    	StringBuilder sbStyle = new StringBuilder();
    	
    	if (liStyles != null)
    	{
			sbStyle.append(StringUtil.concat(" ", liStyles));
    	}

    	if (sOriginalStyle != null)
    	{
    		sbStyle.insert(0, " ");
    		sbStyle.insert(0, sOriginalStyle);
    	}

    	if (sbStyle.length() > 0)
    	{
	    	if (menuItem != null)
	    	{
	    		if (menuItem instanceof MenuBar.MenuItem)
	    		{
	    			((MenuBar.MenuItem)menuItem).setStyleName(sbStyle.toString());
	    		}
	    		else
	    		{
	    			((com.vaadin.contextmenu.MenuItem)menuItem).setStyleName(sbStyle.toString());
	    		}
	    	}
    	}
    }
   
} 	// WrappedMenuItem
