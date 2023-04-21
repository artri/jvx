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
 * 08.12.2008 - [JR] - default button functionality implemented
 * 10.12.2008 - [JR] - moved accelerator methods from UIMenuItem
 * 09.06.2009 - [JR] - cached image (cast safe when the user calls getImage)
 * 20.07.2009 - [JR] - set/getMargins implemented
 * 17.10.2009 - [JR] - supported mouseover/mousepressed images
 */
package javax.rad.genui.component;

import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.component.IButton;

/**
 * Platform and technology independent button.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <C> instance of IButton
 */
public abstract class AbstractUIButton<C extends IButton> extends AbstractUIActionComponent<C> 
                                                          implements IButton 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mouse over image. */
	private transient IImage imgOver = null;
	
	/** the mouse pressed image. */
	private transient IImage imgPressed = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractUIButton</code>.
     *
     * @param pButton the IButton.
     * @see IButton
     */
	protected AbstractUIButton(C pButton)
	{
		super(pButton);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getImageTextGap()
    {
    	return uiResource.getImageTextGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setImageTextGap(int pImageTextGap)
    {
    	uiResource.setImageTextGap(pImageTextGap);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalPosition)
    {
    	uiResource.setVerticalTextPosition(pVerticalPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
    {
    	return uiResource.getVerticalTextPosition();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
    {
    	uiResource.setHorizontalTextPosition(pHorizontalPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
    {
    	return uiResource.getHorizontalTextPosition();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
    	return uiResource.getMargins();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pMargins instanceof UIInsets)
		{
	    	uiResource.setMargins(((UIInsets)pMargins).getUIResource());
		}
		else
		{
	    	uiResource.setMargins(pMargins);
		}
	}

	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderOnMouseEntered()
    {
    	return uiResource.isBorderOnMouseEntered();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
    {
    	uiResource.setBorderOnMouseEntered(pBorderOnMouseEntered);
    }

    /**
	 * {@inheritDoc}
	 */
    public void setBorderPainted(boolean pBorderPainted)
    {
    	uiResource.setBorderPainted(pBorderPainted);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public boolean isBorderPainted()
    {
    	return uiResource.isBorderPainted();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setMouseOverImage(IImage pImage)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pImage instanceof UIImage)
		{
	    	uiResource.setMouseOverImage(((UIImage)pImage).getUIResource());
		}
		else
		{
	    	uiResource.setMouseOverImage(pImage);
		}
		
		imgOver = pImage;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public IImage getMouseOverImage()
    {
    	return imgOver;
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
		
		imgPressed = pImage;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public IImage getPressedImage()
    {
    	return imgPressed;
    }

    /**
	 * {@inheritDoc}
	 */
    public void setDefaultButton(boolean pDefault)
    {
    	uiResource.setDefaultButton(pDefault);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isDefaultButton()
    {
    	return uiResource.isDefaultButton();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Sets the insets with primitive types.
	 * 
	 * @param pTop the top insets.
	 * @param pLeft the left insets.
	 * @param pBottom the bottom insets.
	 * @param pRight the right insets.
	 */
    public void setMargins(int pTop, int pLeft, int pBottom, int pRight)
    {
    	uiResource.setMargins(getFactory().createInsets(pTop, pLeft, pBottom, pRight));
    }    
    
}	// AbstractUIButton
