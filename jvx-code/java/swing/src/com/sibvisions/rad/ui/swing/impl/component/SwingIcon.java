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
 * 16.10.2008 - [JR] - used JVxIcon instead of JLabel
 */
package com.sibvisions.rad.ui.swing.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.IIcon;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.swing.ext.JVxIcon;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingIcon</code> is the <code>IIcon</code>
 * implementation for swing. It displays an area for an image.
 * and does not react to input events. As a result, it cannot 
 * get the keyboard focus.
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JLabel
 * @see javax.rad.ui.component.IIcon
 */
public class SwingIcon extends SwingComponent<JVxIcon> 
                       implements IIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the IconImage. */ 
	private IImage image = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingIcon</code>.
	 */
	public SwingIcon()
	{
		super(new JVxIcon());

		// Default vertical label alignment in swing is center.
		super.setHorizontalAlignment(ALIGN_CENTER);
		super.setVerticalAlignment(ALIGN_CENTER);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    public IImage getImage()
	{
    	return image;
	}

	/**
	 * {@inheritDoc}
	 */
    public void setImage(IImage pImage)
	{
		if (pImage == null)
		{
			resource.setImage(null);
		}
		else
		{
			resource.setImageIcon((ImageIcon)pImage.getResource());
		}

    	image = pImage;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreserveAspectRatio()
	{
		return resource.isPreserveAspectRatio();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		resource.setPreserveAspectRatio(pPreserveAspectRatio);
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		resource.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));

		super.setHorizontalAlignment(pHorizontalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		resource.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(pVerticalAlignment));

		super.setVerticalAlignment(pVerticalAlignment);
	}

}	// SwingIcon
