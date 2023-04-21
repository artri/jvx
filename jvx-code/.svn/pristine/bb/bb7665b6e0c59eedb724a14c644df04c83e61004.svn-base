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
 * 16.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IIcon;

/**
 * Platform and technology independent Icon.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIIcon extends UIComponent<IIcon> 
                    implements IIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** The image. */
	private transient IImage image;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIIcon</code>.
     *
     * @see IIcon
     */
	public UIIcon()
	{
		this(UIFactoryManager.getFactory().createIcon());
	}

    /**
     * Creates a new instance of <code>UIIcon</code> with the given
     * icon.
     *
     * @param pIcon the icon
     * @see IIcon
     */
	protected UIIcon(IIcon pIcon)
	{
		super(pIcon);
		
        setMaximumSize(800, 600);
	}
	
    /**
     * Creates a new instance of <code>UIIcon</code>.
     *
     * @param pImage the Image.
     * @see IIcon
     */
	public UIIcon(IImage pImage)
	{
		this();
		
		setImage(pImage);
	}
	
    /**
     * Creates a new instance of <code>UIIcon</code>.
     *
     * @param pImageName the Image name.
     * @see IIcon
     */
	public UIIcon(String pImageName)
	{
		this();
		
		setImage(UIImage.getImage(pImageName));
	}
	
    /**
     * Creates a new instance of <code>UIIcon</code>.
     *
     * @param pImage the Image.
     * @param pHorizontalAlignment the horizontal alignment.
     * @param pVerticalAlignment the vertical alignment.
     * @see IIcon
     */
	public UIIcon(IImage pImage, int pHorizontalAlignment, int pVerticalAlignment)
	{
		this();
		
		setImage(pImage);
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
	}
	
    /**
     * Creates a new instance of <code>UIIcon</code>.
     *
     * @param pImageName the Image name.
     * @param pHorizontalAlignment the horizontal alignment.
     * @param pVerticalAlignment the vertical alignment.
     * @see IIcon
     */
	public UIIcon(String pImageName, int pHorizontalAlignment, int pVerticalAlignment)
	{
	    this();
        
        setImage(UIImage.getImage(pImageName));
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
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
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pImage instanceof UIImage)
		{
	    	uiResource.setImage(((UIImage)pImage).getUIResource());
		}
		else
		{
	    	uiResource.setImage(pImage);
		}
		image = pImage;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
    {
    	return uiResource.getHorizontalAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
    {
    	uiResource.setHorizontalAlignment(pHorizontalAlignment);
    }

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
    {
    	return uiResource.getVerticalAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
    {
    	uiResource.setVerticalAlignment(pVerticalAlignment);
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreserveAspectRatio()
	{
		return uiResource.isPreserveAspectRatio();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		uiResource.setPreserveAspectRatio(pPreserveAspectRatio);
	}
	
}	// UIIcon
