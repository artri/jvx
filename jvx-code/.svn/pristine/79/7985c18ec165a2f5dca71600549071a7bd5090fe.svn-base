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
 * 08.04.2009 - [JR] - setImage: set a fixed size ImageIcon as icon and not a JVxResizableImageIcon
 */
package com.sibvisions.rad.ui.swing.impl.menu;

import javax.rad.ui.IImage;
import javax.rad.ui.menu.IMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.sibvisions.rad.ui.swing.impl.component.SwingAbstractButton;
import com.sibvisions.util.type.ImageUtil;

/**
 * The <code>SwingMenuItem</code> is the <code>IMenuItem</code>
 * implementation for swing.
 * 
 * @param <C> the instance of JMenuItem
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JMenuItem
 * @see javax.rad.ui.menu.IMenuItem
 */
public class SwingMenuItem<C extends JMenuItem> extends SwingAbstractButton<C> 
                                                implements IMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the maximum size of menu images. */
	public static final int MAX_IMAGESIZE = 16;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingMenuItem</code>.
	 * 
	 * @param pMenuItem instance of JMenuItem
	 */
	protected SwingMenuItem(C pMenuItem)
	{
		super(pMenuItem);
	}

	/**
	 * Creates a new instance of <code>SwingMenuItem</code>.
	 */
	public SwingMenuItem()
	{
		super((C)new JMenuItem());
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
    	if (pImage == null)
    	{
    		resource.setIcon(null);
    	}
    	else
    	{
       		resource.setIcon(ImageUtil.getScaledIcon((ImageIcon)pImage.getResource(), 
       				                                 Math.min(MAX_IMAGESIZE, pImage.getWidth()), 
       				                                 Math.min(MAX_IMAGESIZE, pImage.getHeight()),
       				                                 false));
    	}

   		image = pImage;
    }	

}	// SwingMenuItem
