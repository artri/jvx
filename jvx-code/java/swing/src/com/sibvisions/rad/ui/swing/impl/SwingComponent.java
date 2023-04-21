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
 * 01.10.2008 - [HM] - creation
 * 24.11.2008 - [JR] - STRETCH constant included
 *                   - used JVxConstants instead of SwingConstants     
 * 18.05.2014 - [JR] - #1040: don't set empty tooltip text 
 * 07.02.2022 - [JR] - #2856: try to use default swing font and calculate size                            
 */
package com.sibvisions.rad.ui.swing.impl;

import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.sibvisions.rad.ui.awt.impl.AwtContainer;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SwingComponent</code> extends AwtComponent with setting the 
 * alignment.
 * 
 * @author Martin Handsteiner
 * @param <C> an instance of {@link JComponent}
 */
public class SwingComponent<C extends JComponent> extends AwtContainer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingComponent</code>.
	 * 
	 * @param pJComponent An instance of {@link JComponent}
	 */
	public SwingComponent(C pJComponent)
	{
		super(pJComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		super.setBackground(pBackground);
		
		resource.setOpaque(pBackground != null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(Integer pTabIndex)
	{
		super.setTabIndex(pTabIndex);
		
		resource.putClientProperty("tabIndex", pTabIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pText)
	{
	    //#1040
	    if (StringUtil.isEmpty(pText))
	    {
	        resource.setToolTipText(null);
	    }
	    else
	    {
	        resource.setToolTipText(pText);
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText()
	{
		return resource.getToolTipText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage capture(int pWidth, int pHeight)
	{
		return new SwingImage(null, new ImageIcon(createImage(resource, pWidth, pHeight)));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);
		
		if (pVisible)// && isBackgroundSet())
		{
			IColor colBack = getBackground();
			
			if (colBack != null && resource.isOpaque() && colBack.getAlpha() != 0xFF)
			{
				if (resource.getParent() != null)
				{
					//otherwise, a transparency problem may occur
					resource.getParent().repaint();
				}
			}
		}
	}
	
	
}	// SwingComponent
