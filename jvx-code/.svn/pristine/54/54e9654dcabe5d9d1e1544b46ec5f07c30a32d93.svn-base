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
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Color;
import java.awt.Insets;

import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.component.IFormatableButton;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingAbstractFormatableButton</code> is a labeled icon button
 * implementation for swing.
 * 
 * @param <C> instance of AbstractButton.
 * 
 * @author René Jahn
 * @see	javax.swing.AbstractButton
 * @see javax.rad.ui.component.ILabeledIcon
 */
public abstract class SwingAbstractFormatableButton<C extends AbstractButton> extends SwingComponent<C> 
                                                                              implements IFormatableButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the icon image. */ 
	protected IImage image = null;
	
    /** the margin. */
    private Insets margin;
	
    /** the cached default gap. */
    private int iDefaultGap = -1;
    
	/** whether to use a dummy image. */
	private boolean bDummyImage;
	
	/** The original color of the button. */
	private Color originalBackground;
    /** The original opaque value. */
    private boolean originalOpaque;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingAbstractFormatableButton</code>.
	 * 
	 * @param pAbstractButton the instance of AbstractButton.
	 */
	public SwingAbstractFormatableButton(C pAbstractButton)
	{
		this(pAbstractButton, false);
	}
	
	/**
	 * Creates a new instance of <code>SwingAbstractFormatableButton</code>.
	 * 
	 * @param pAbstractButton the button instance.
	 * @param pDummyImage <code>true</code> to initializes a dummy image
	 */
	protected SwingAbstractFormatableButton(C pAbstractButton, boolean pDummyImage)
	{
		super(pAbstractButton);

        if (resource.isBackgroundSet())
        {
            originalBackground = resource.getBackground();
        }
        else
        {
            originalBackground = null;
        }
        originalOpaque = resource.isOpaque();

        // set correct default values.
		super.setHorizontalAlignment(SwingFactory.getHorizontalAlignment(resource.getHorizontalAlignment()));
		super.setVerticalAlignment(SwingFactory.getVerticalAlignment(resource.getVerticalAlignment()));

		margin = resource.getMargin();
		
		bDummyImage = pDummyImage;
		
		if (bDummyImage)
		{
			initDummyImage();
		}
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// IButton
	
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
    	if (iDefaultGap != -1)
    	{
    		resource.setIconTextGap(iDefaultGap);
    		
    		iDefaultGap = -1;
    	}

    	if (pImage == null)
    	{
    		resource.setIcon(null);
    	}
    	else
    	{
    		resource.setIcon((ImageIcon)pImage.getResource());
    	}

    	image = pImage;
    	
       	if (bDummyImage && image == null)
    	{
    		initDummyImage();
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
	
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	if (pMargins != null)
    	{
    		margin = (Insets)pMargins.getResource();
    		
    		resource.setMargin(margin);
    	}
    	else
    	{
    		resource.setMargin(null);

    		margin = resource.getMargin();
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
    {
    	if (margin != null)
    	{
    		return new AwtInsets(margin);
    	}
    	else
    	{
    		return new AwtInsets(resource.getMargin());
    	}
    }	
	
	/**
	 * {@inheritDoc}
	 */
    public int getImageTextGap()
    {
    	if (iDefaultGap != -1)
    	{
    		return iDefaultGap;
    	}
    	else
    	{
    		return resource.getIconTextGap();
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void setImageTextGap(int pImageTextGap)
    {
    	//no more resets!
    	iDefaultGap = -1;
    	
    	resource.setIconTextGap(pImageTextGap);
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
		super.setHorizontalAlignment(pHorizontalAlignment);
		resource.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		super.setVerticalAlignment(pVerticalAlignment);
		resource.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(pVerticalAlignment));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		if (pBackground != null || SwingFactory.isLaFOpaque())
		{
			super.setBackground(pBackground);
		}
		else
		{
		    resource.setBackground(originalBackground);
		    resource.setOpaque(originalOpaque);
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Inits the original background.
	 * 
	 * @param pBackground the new original background
	 */
	protected void initOriginalBackground(Color pBackground)
	{
	    originalBackground = pBackground;
	    
	    resource.setBackground(originalBackground);
	}
	
    /**
     * Initializes a dummy image.
     */
    private void initDummyImage()
    {
		//very strange, but Mac LaF doesn't recognize the margins unless there is an icon!
		if (SwingFactory.isMacLaF())
		{
			iDefaultGap = resource.getIconTextGap();

			resource.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/1x1.png"));
			resource.setIconTextGap(0);
			
			//important for LaF calculation -> hack
			resource.setMargin(resource.getMargin());
		}    	
    }

}	// SwingAbstractFormatableButton
