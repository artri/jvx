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
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IImage;
import javax.rad.ui.component.ILabeledIcon;

import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.vaadin.ui.Component;

/**
 * The <code>AbstractVaadinLabeledIcon</code> is the base class for vaadin implementations of {@link ILabeledIcon}.
 *
 * @author René Jahn
 * @param <C> an instance of {@link Component}
 */
public abstract class AbstractVaadinLabeledIcon<C extends Component> extends VaadinComponent<C>
                                                                     implements ILabeledIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the image. */ 
	protected VaadinImage image = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractVaadinLabeledIcon</code>.
     *
     * @param pComponent an instance of {@link Component}
     */
	protected AbstractVaadinLabeledIcon(C pComponent)
	{
		super(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getText()
    {
    	return resource.getCaption();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
    {
		resource.setCaption(pText);
    }

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
		if (image != null)
		{
			String styleName = image.getStyleName();
			
			if (styleName != null)
			{
				removeInternStyleName(styleName);
			}
		}
    	
    	image = (VaadinImage)pImage;
    	
		if (image != null)
		{
			String styleName = image.getStyleName();
			
			if (styleName != null)
			{
				addInternStyleName(styleName);
			}
			
            resource.setIcon(image.getResource());
		}    	
		else
		{
            resource.setIcon(null);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreserveAspectRatio()
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		// Does nothing.
	}
	
}	// AbstractVaadinLabeledIcon
