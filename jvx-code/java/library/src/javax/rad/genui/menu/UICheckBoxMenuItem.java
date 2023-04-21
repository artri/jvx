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
 */
package javax.rad.genui.menu;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.ui.IImage;
import javax.rad.ui.menu.ICheckBoxMenuItem;

/**
 * Platform and technology independent checkbox menuitem.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UICheckBoxMenuItem extends AbstractUIMenuItem<ICheckBoxMenuItem> 
                                implements ICheckBoxMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the selected image. */
	private transient IImage imageSelected = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UICheckBoxMenuItem</code>.
     *
     * @see ICheckBoxMenuItem
     */
	public UICheckBoxMenuItem()
	{
		super(UIFactoryManager.getFactory().createCheckBoxMenuItem());
	}

    /**
     * Creates a new instance of <code>UICheckBoxMenuItem</code> with the given
     * menu item.
     *
     * @param pMenuItem the menu item
     * @see ICheckBoxMenuItem
     */
	protected UICheckBoxMenuItem(ICheckBoxMenuItem pMenuItem)
	{
		super(pMenuItem);
	}
	
	/**
     * Creates a new instance of <code>UICheckBoxMenuItem</code>.
     *
     * @param pText the text.
     * @see ICheckBoxMenuItem
     */
	public UICheckBoxMenuItem(String pText)
	{
		this();
		
		setText(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return uiResource.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setSelected(boolean pPressed)
    {
    	uiResource.setSelected(pPressed);
    }

	/**
	 * {@inheritDoc}
	 */
    public void setPressedImage(IImage pImage)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pImage instanceof UIImage)
		{
	    	uiResource.setPressedImage(((UIImage)pImage).getUIResource());
		}
		else
		{
	    	uiResource.setPressedImage(pImage);
		}
		
		imageSelected = pImage;
    }

	/**
	 * {@inheritDoc}
	 */
    public IImage getPressedImage()
    {
    	return imageSelected;
    }
	
}	// UICheckBoxMenuItem
