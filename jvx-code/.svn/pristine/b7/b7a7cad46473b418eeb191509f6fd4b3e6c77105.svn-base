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
 * 25.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.MouseOverButtonState;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.MouseOverServerRpc;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;

/**
 * The <code>MouseOverButton</code> class is the server-side implementation of a button that 
 * handles mouse over events.
 * 
 * @author Stefan Wurm
 */
public class MouseOverButton extends Button
                             implements IDynamicCss
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The icon for the mouse over event. **/
	private Resource mouseOverIcon = null;
	
	/** The standard icon. **/
	private Resource mouseOutIcon = null;
	
	/** The CSS extension. */
	private CssExtension cssExtension = null;
	
	/**
	 * If the button is a default button.
	 */
	private boolean isDefault = false;
	
	/** The Implementation of the RPC for the MouseOver interaction. **/
	private MouseOverServerRpc rpc = new MouseOverServerRpc() 
	{
		public void mouseOver(MouseEventDetails pEvent)
		{
			if (mouseOverIcon != null && isEnabled())
			{
				MouseOverButton.this.setIcon(mouseOverIcon);
				MouseOverButton.this.markAsDirty();
			}
		}

		public void mouseOut(MouseEventDetails pEvent)
		{
			if (mouseOutIcon != null && isEnabled())
			{
				MouseOverButton.this.setIcon(mouseOutIcon);
				MouseOverButton.this.markAsDirty();
			}
		}
	};
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates a new instance of <code>MouseOverButton</code>.
	 */
	public MouseOverButton() 
	{
		super();
	}
		
	/**
	 * Creates a new instance of <code>MouseOverButton</code> with caption.
	 * 
	 * @param pCaption the caption
	 */
	public MouseOverButton(String pCaption) 
	{
		super(pCaption);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public CssExtension getCssExtension()
	{
        if (cssExtension == null)
        {
            cssExtension = new CssExtension();
            cssExtension.extend(this);
        }
        
        return cssExtension;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MouseOverButtonState getState() 
	{
		return (MouseOverButtonState)super.getState();
	}		

    /**
     * {@inheritDoc}
     */
    @Override
	public void setIcon(Resource pResource)
	{
        VaadinUtil.removeFontIconStyles(this, getIcon());
        
	    super.setIcon(pResource);
	    
	    VaadinUtil.applyFontIconStyles(this, pResource, "v-icon");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
	/**
	 * Sets a mouse over image for the button.
	 * 
	 * @param pIcon the mouse over image.
	 */
	public void setMouseOverImage(Resource pIcon)
	{
		if (pIcon != null)
		{
			getState().setRegisterMouseHandler(true);
			registerRpc(rpc);
		}
		else
		{
			getState().setRegisterMouseHandler(false);
		}
		
		mouseOverIcon = pIcon;
		mouseOutIcon = getIcon();
	}
	
	/**
	 * Is the button a default button.
	 * 
	 * @return <code>true</code> if the button is a default button, <code>false</code> otherwise
	 */
	public boolean isDefault() 
	{
		return isDefault;
	}

	/**
	 * Sets the button as default button.
	 * 
	 * @param pDefault <code>true</code> to set the button as default button, <code>false</code> otherwise
	 */
	public void setDefault(boolean pDefault) 
	{
		isDefault = pDefault;
	}

	/**
	 * Sets the icon before or after the text.
	 * 
	 * @param pBeforeText true if the icon should be before the text.
	 */
	public void setIconBeforeText(boolean pBeforeText)
	{
		getState().setIconBeforeText(pBeforeText);
	}

	/**
	 * Returns shortcut.
	 * 
	 * @return shortcut
	 */
	public ClickShortcut getClickShortcut()
	{
		return clickShortcut;
	}
	
}	// MouseOverButton
