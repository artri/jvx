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
package javax.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIImage;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.component.ILabeledIcon;

/**
 * Platform and technology independent label with icon component.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * 
 * @param <C> instance of ILabeledIcon
 */
public abstract class AbstractUILabeledIcon<C extends ILabeledIcon> extends UIComponent<C> 
                                                                    implements ILabeledIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the image. */
	private transient IImage image = null;
	
	/** the action component text. */
	private transient String sText = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractUIActionComponent</code>.
     *
     * @param pActionComponent the {@link IActionComponent}.
     * @see IActionComponent
     */
	protected AbstractUILabeledIcon(C pActionComponent)
	{
		super(pActionComponent);
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
    public String getText()
    {
        return sText;
    }

    /**
     * {@inheritDoc}
     */
    public void setText(String pText)
    {
        sText = pText;
        
        setAndTranslateText(sText);
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();
		
		super.updateTranslation();
		
		if (bTranslate && bChanged)
		{
			setAndTranslateText(sText);
		}
	}

	/**
	 * This method translates the given text and sets the translated text into the UI resource.
	 * 
	 * @param pText the untranslated text
	 */
	protected void setAndTranslateText(String pText)
	{
		uiResource.setText(translate(sText));
	}
	
}	// AbstractUILabeledIcon
