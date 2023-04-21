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
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;

import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.component.AbstractVaadinActionComponent;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * The <code>VaadinMenuItem</code> class is the vaadin implementation of {@link IMenuItem}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinMenuItem extends AbstractVaadinActionComponent<WrappedMenuItem>
                            implements IMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the Menu Command. */
	private com.vaadin.contextmenu.Menu.Command contextCommand;
	
	/** the Menu Command. */
	private Command vaadinCommand;
	
	/** the accelerator for this Key. */
	private Key accelerator = null;	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinMenuItem</code>.
     *
     * @see javax.rad.ui.menu.IMenuItem
     */
	public VaadinMenuItem()
	{
		this(null);
	}
	
	/**
     * Creates a new instance of <code>VaadinMenuItem</code> with the given item text.
     *
     * @param pText the item text
     * @see javax.rad.ui.menu.IMenuItem
     */
	public VaadinMenuItem(String pText)
	{
		super(new WrappedMenuItem());

		setText(pText);	
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    public Key getAccelerator()
    {
    	return accelerator;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pAccelerator)
    {
    	// not supported
    	accelerator = pAccelerator;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public String getText()
    {
		return resource.getText();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
    {
		resource.setText(pText);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setImage(IImage pImage)
    {
		image = (VaadinImage)pImage;
		
    	resource.setImage(pImage); 	
    }		
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setId(String pId)
	{
	    super.setId(pId);
	    
	    //forward id to menubar because vaadin doesn't set IDs to menu items!
        IContainer parent = getParent();

        while (parent != null && !(parent instanceof IMenuBar))
        {
            parent = parent.getParent();
        }
        
        if (parent != null)
        {
            ((VaadinMenuBar)parent).setId(this, pId);
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the menu command.
	 * 
	 * @return the menu command
	 * @see com.vaadin.ui.MenuBar.Command
	 */	
	com.vaadin.contextmenu.Menu.Command getContextCommand()
	{
		if (contextCommand == null)
		{
			contextCommand = new com.vaadin.contextmenu.Menu.Command()
			{
				@Override
				public void menuSelected(com.vaadin.contextmenu.MenuItem pSelectedItem)
				{
		        	if (eventActionPerformed != null)
		        	{
		        		getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(eventSource,
		    																 					 	   UIActionEvent.ACTION_PERFORMED, 
		    																 					 	   System.currentTimeMillis(), 
		    																 					 	   0, 
		    																 					 	   sActionCommand));
		    		}
				}
			};
		}
		
		return contextCommand;
	}
	
	/**
	 * Gets the menu command.
	 * 
	 * @return the menu command
	 * @see com.vaadin.ui.MenuBar.Command
	 */	
	Command getVaadinCommand()
	{
		if (vaadinCommand == null)
		{
			vaadinCommand = new Command()
			{
		        public void menuSelected(MenuItem pSelectedItem) 
		        {
		        	if (eventActionPerformed != null)
		        	{
		        		getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(eventSource,
		    																 					 	   UIActionEvent.ACTION_PERFORMED, 
		    																 					 	   System.currentTimeMillis(), 
		    																 					 	   0, 
		    																 					 	   sActionCommand));
		    		}
		        }
			};
		}
		
		return vaadinCommand;
	}
	
	/**
	 * Gets the current icon resource.
	 * 
	 * @return the icon or <code>null</code> if no image is set
	 */
	Resource getIcon()
	{
		if (image == null)
		{
			return resource.getIcon();
		}
		else
		{
			return image.getResource();
		}
	}
	
}	// VaadinMenuItem
