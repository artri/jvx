/*
 * Copyright 2012 SIB Visions GmbH
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
 * 17.10.2012 - [CB] - creation
 * 11.04.2019 - [DJ] - #1694: button alignment made more consistent with other implementations
 * 17.04.2019 - [DJ] - #1694: flex property used for button alignment(jvx_valo bugfix)
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IButton;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIKeyEvent;

import com.sibvisions.rad.ui.vaadin.ext.ui.AccessibilityUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverButton;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.util.ArrayUtil;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * The <code>AbstractVaadinButton</code> is the base class for vaadin implementations of <code>IButton</code>.
 * 
 * @author Benedikt Cermak 
 * @see javax.rad.ui.component.IButton
 * @see	MouseOverButton
 */
public abstract class AbstractVaadinButton extends AbstractVaadinCaptionComponent<MouseOverButton> 
                                           implements IButton,
                                                      ClickListener                                                                              
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the accelerator for this Key. */
	private Key keyAccelerator = null;

	/** mouse over image. **/
	private VaadinImage imgMouseOver = null;

	/** vertical Position of the text. **/
	private int iVerticalPosition = IAlignmentConstants.ALIGN_CENTER;
	
	/** horizontal Position of the text. **/
	private int iHorizontalPosition = IAlignmentConstants.ALIGN_RIGHT;
	
	/** the gap between the icon and the text. **/
	private int iImageTextGap = 4;
	
	/** border on Mouse entered. **/
	private boolean bBorderOnMouseEntered = false;
	
	/** border painted. **/
	private boolean bBorderPainted = true;

	/** indicates whether this button acts as default-button. */
	private boolean bIsDefault = false;

	/** indicates whether the image text-gap was set manually. */
	private boolean bImageTextGapSet = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractVaadinButton</code>.
	 * 
	 * @param pAbstractButton an instance of {@link com.vaadin.ui.Button}
	 */
	public AbstractVaadinButton(MouseOverButton pAbstractButton)
	{
		super(pAbstractButton);
		
		//no text -> sets margins
		setText(null);
		
		resource.setStyleName("jvxbutton");
		addInternStyleName("default-padding");
		
		setHorizontalAlignment(ALIGN_CENTER);
        setVerticalAlignment(ALIGN_CENTER);
        setVerticalTextPosition(ALIGN_CENTER);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public Key getAccelerator()
	{
		return keyAccelerator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAccelerator(Key pAccelerator)
	{
		if (pAccelerator != null) 
		{
			int keyCode = pAccelerator.getKeyChar();
			switch(keyCode)
			{
				case UIKeyEvent.VK_ENTER: keyCode = KeyCode.ENTER; break;
				default:
			}
			
			int modifiers = pAccelerator.getModifiers();
			
			int[] vaadinModifiers = mapToVaadinModifiers(modifiers);
			
			resource.setClickShortcut(keyCode, vaadinModifiers);
		 	
		 	keyAccelerator = pAccelerator;
		}
	}

	/**
	 * Maps the modifiers bitMask to vaadin modifiers array.
	 * 
	 * @param modifiers 
	 * @return array of vaadin modifiers
	 */
	private int[] mapToVaadinModifiers(int modifiers)
	{
		int[] vaadinModifiers = new int[4];
		int i = 0;
		if ((modifiers & UIKeyEvent.SHIFT_MASK) == UIKeyEvent.SHIFT_MASK)
		{
			vaadinModifiers[i++] = ShortcutAction.ModifierKey.SHIFT;
		}
		if ((modifiers & UIKeyEvent.CTRL_MASK) == UIKeyEvent.CTRL_MASK)
		{
			vaadinModifiers[i++] = ShortcutAction.ModifierKey.CTRL;
		}
		if ((modifiers & UIKeyEvent.ALT_MASK) == UIKeyEvent.ALT_MASK)
		{
			vaadinModifiers[i++] = ShortcutAction.ModifierKey.ALT;
		}
		if ((modifiers & UIKeyEvent.META_MASK) == UIKeyEvent.META_MASK)
		{
			vaadinModifiers[i++] = ShortcutAction.ModifierKey.META;
		}
		vaadinModifiers = ArrayUtil.truncate(vaadinModifiers, i);
		return vaadinModifiers;
	}
	/**
	 * {@inheritDoc}
	 */
	public int getImageTextGap()
	{
		return iImageTextGap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setImageTextGap(int pImageTextGap)
	{
		bImageTextGapSet = true;
		
		iImageTextGap = pImageTextGap; 
		
		updateGap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalTextPosition(int pVerticalPosition)
	{
		CssExtension csse = getCssExtension();
		
        positionCaptionBesideImage();
		
        // special behaviour of vertical alignment if horizontally centered
		if (iHorizontalPosition == IAlignmentConstants.ALIGN_CENTER)
		{
	        if (pVerticalPosition == IAlignmentConstants.ALIGN_BOTTOM)
	        {
	            addTextPositionAttributesToExtension(csse, "center", "column");
	            
	            resource.setIconBeforeText(true);
	        }
	        else if (pVerticalPosition == IAlignmentConstants.ALIGN_CENTER)
	        {
	            if (verticalAlignment == IAlignmentConstants.ALIGN_CENTER
	                    && horizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
	            {
	                // all ALIGN_CENTER
                    positionCaptionOverImage();
	            }
	            else
	            {
	                addTextPositionAttributesToExtension(csse, "center", "row");
	                
	                resource.setIconBeforeText(true);
	            }
	        }
	        else if (pVerticalPosition == IAlignmentConstants.ALIGN_TOP)
	        {
	            addTextPositionAttributesToExtension(csse, "center", "column");
	            
	            resource.setIconBeforeText(false);
	        }
		}
        else
        {
            if (pVerticalPosition == IAlignmentConstants.ALIGN_BOTTOM)
            {
                addTextPositionAttributesToExtension(csse, "flex-end", "row");
            }
            else if (pVerticalPosition == IAlignmentConstants.ALIGN_CENTER)
            {
                addTextPositionAttributesToExtension(csse, "center", "row");
            }
            else if (pVerticalPosition == IAlignmentConstants.ALIGN_TOP)
            {
                addTextPositionAttributesToExtension(csse, "flex-start", "row");
            }
		}

		iVerticalPosition = pVerticalPosition;
		
		updateGap();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalTextPosition()
	{
		return iVerticalPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalTextPosition(int pHorizontalPosition)
	{
		CssExtension csse = getCssExtension();
		
		positionCaptionBesideImage();
		
		if (pHorizontalPosition == IAlignmentConstants.ALIGN_LEFT)
		{
			resource.setIconBeforeText(false);
			
			addTextPositionAttributesToExtension(csse, getVerticalTextPositionValue(), "row");
		}
		else if (pHorizontalPosition == IAlignmentConstants.ALIGN_CENTER)
		{
			if (iVerticalPosition == IAlignmentConstants.ALIGN_CENTER
			        && verticalAlignment == IAlignmentConstants.ALIGN_CENTER
			        && horizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
			{
			    positionCaptionOverImage();
			}
			else
			{
	            if (iVerticalPosition == IAlignmentConstants.ALIGN_TOP)
	            {
	                addTextPositionAttributesToExtension(csse, "center", "column");
	                
	                resource.setIconBeforeText(false);
	            }
	            else if (iVerticalPosition == IAlignmentConstants.ALIGN_BOTTOM)
	            {
	                addTextPositionAttributesToExtension(csse, "center", "column");
	                
	                resource.setIconBeforeText(true);
	            }
	            else
	            {
	                addTextPositionAttributesToExtension(csse, "center", "row");
	                
	                resource.setIconBeforeText(true);
	            }
			}
		}
		else if (pHorizontalPosition == IAlignmentConstants.ALIGN_RIGHT)
		{
			resource.setIconBeforeText(true);
			
			addTextPositionAttributesToExtension(csse, getVerticalTextPositionValue(), "row");
		}
	
		iHorizontalPosition = pHorizontalPosition;
		
		updateGap();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalTextPosition()
	{
		return iHorizontalPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderOnMouseEntered()
	{
		return bBorderOnMouseEntered;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
	{
		updateBorderOnMouseEnteredStyles(pBorderOnMouseEntered);

		setBorderPainted(!pBorderOnMouseEntered);
		
		bBorderOnMouseEntered = pBorderOnMouseEntered;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderPainted(boolean pBorderPainted)
	{
		updateBorderPaintedStyles(pBorderPainted);
		
		bBorderPainted = pBorderPainted;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderPainted()
	{
		return bBorderPainted;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMouseOverImage(IImage pImage)
	{
		imgMouseOver = (VaadinImage)pImage;
		
		if (imgMouseOver == null)
		{
		    resource.setMouseOverImage(null);
		}
		else
		{
		    resource.setMouseOverImage(imgMouseOver.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getMouseOverImage()
	{
		return imgMouseOver;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPressedImage(IImage pImage)
	{
		// Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getPressedImage()
	{
		// Not supported
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultButton(boolean pDefault)
	{
		resource.setDefault(pDefault);
		
		bIsDefault = pDefault;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDefaultButton()
	{
		return bIsDefault;
	}

	// ClickListener
	
	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent pEvent)
	{
		if (eventActionPerformed != null)
		{
			getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(eventSource, 
																	                       UIActionEvent.ACTION_PERFORMED, 
																	                       System.currentTimeMillis(), 
																	                       0, 
																	                       sActionCommand));
		}
	}	
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnabled)
	{
		super.setEnabled(pEnabled);

		updateBorderOnMouseEnteredStyles(isBorderOnMouseEntered());
		updateBorderPaintedStyles(isBorderPainted());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		super.setText(pText);
		
		if (pText == null || pText.length() <= 0)
		{
			addInternStyleName("no-caption");
		}
		else
		{
			removeInternStyleName("no-caption");
		}

		updateGap();
		
		updateAccessibility();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pToolTipText)
	{
		super.setToolTipText(pToolTipText);
		
		updateAccessibility();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImage(IImage pImage)
	{
		super.setImage(pImage);
		
		if (getMouseOverImage() != null) // reset the MouseOverButton with the new icon
		{
			setMouseOverImage(imgMouseOver);
		}
		
		updateGap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(IDimension pSize)
	{
		super.setSize(pSize);
		
		if (pSize != null)
		{
			if (pSize.getWidth() <= 32 && pSize.getHeight() <= 32)
			{
				removeInternStyleName("default-padding");
			}
			else
			{
				addInternStyleName("default-padding");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionHandler eventAction()
	{
		if (eventActionPerformed == null)
		{
			resource.addClickListener(this);
		}
		
		return super.eventAction();
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public void setVerticalAlignment(int pVerticalAlignment)
    {
        CssExtension csse = getCssExtension();
        
        CssExtensionAttribute csaVerticalAlign = new CssExtensionAttribute();
        csaVerticalAlign.setElementClassName("v-button");
        csaVerticalAlign.setAttribute("align-content");
        
        positionCaptionBesideImage();
        
        if (isEveryAlignmentCentered())
        {
            addTextPositionAttributesToExtension(csse, "center", "row");
        }
        
        if (pVerticalAlignment == IAlignmentConstants.ALIGN_BOTTOM)
        {
            csaVerticalAlign.setValue("end");
            csse.addAttribute(csaVerticalAlign);
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_CENTER)
        {
            csaVerticalAlign.setValue("center");
            csse.addAttribute(csaVerticalAlign);
            if (horizontalAlignment == IAlignmentConstants.ALIGN_CENTER
                    && iHorizontalPosition == IAlignmentConstants.ALIGN_CENTER
                    && iVerticalPosition == IAlignmentConstants.ALIGN_CENTER)
            {
                positionCaptionOverImage();
            }
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_TOP)
        {
            csaVerticalAlign.setValue("start");
            csse.addAttribute(csaVerticalAlign);
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_STRETCH)
        {
            resource.setHeight("100%");
        }

        verticalAlignment = pVerticalAlignment;
    }
	
	/**
     * {@inheritDoc}
     */
	@Override
    public void setHorizontalAlignment(int pHorizontalAlignment)
    {
	    CssExtension csse = getCssExtension();
        
	    CssExtensionAttribute csaHorizontalAlign = new CssExtensionAttribute();
	    csaHorizontalAlign.setElementClassName("v-button");
	    csaHorizontalAlign.setAttribute("justify-content");
	    
        csse.removeAttribute(csaHorizontalAlign);
        
        positionCaptionBesideImage();
        
        if (isEveryAlignmentCentered())
        {
            addTextPositionAttributesToExtension(csse, "center", null);
        }
        
        if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
        {
            csaHorizontalAlign.setValue("start");
            csse.addAttribute(csaHorizontalAlign);
        }
        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
        {
            csaHorizontalAlign.setValue("center");
            csse.addAttribute(csaHorizontalAlign);
            
            if (verticalAlignment == IAlignmentConstants.ALIGN_CENTER
                    && iHorizontalPosition == IAlignmentConstants.ALIGN_CENTER
                    && iVerticalPosition == IAlignmentConstants.ALIGN_CENTER)
            {
                positionCaptionOverImage();
            }
        }
        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
        {
            csaHorizontalAlign.setValue("end");
            csse.addAttribute(csaHorizontalAlign);
        }
        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
        {
            resource.setWidth("100%");
        }
        
        horizontalAlignment = pHorizontalAlignment;
        
	    updateGap();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Updates the text gap. This method takes care of the current text position.
	 */
	private void updateGap()
	{
		String sMargin = null;
		removeCaptionMargins();
		
		if (iHorizontalPosition == ALIGN_CENTER)
		{
		    if (iVerticalPosition == IAlignmentConstants.ALIGN_BOTTOM)
	        {
	            sMargin = "margin-top";
	        }
		    else if (iVerticalPosition == IAlignmentConstants.ALIGN_TOP)
	        {
	            sMargin = "margin-bottom";
	        }
		    else if (!isEveryAlignmentCentered())
		    {
		        sMargin = "margin-left";
		    }
		}
		else if (iHorizontalPosition == IAlignmentConstants.ALIGN_LEFT)
		{
            sMargin = "margin-right";
		}
        else if (!isEveryAlignmentCentered())
        {
            sMargin = "margin-left";
        }
		
		if (sMargin != null)
		{
			CssExtensionAttribute cssAttribute = null;
			
            String sText = getText();
            
            if (sText != null && sText.trim().length() > 0 && (getImage() != null || resource.getIcon() != null))
            {
    			if (!bImageTextGapSet)
    			{
					cssAttribute = new CssExtensionAttribute(sMargin, "4px");
    			}
    			else
    			{
    				cssAttribute = new CssExtensionAttribute(sMargin, iImageTextGap + "px");
    			}
            }

			if (cssAttribute != null)
			{
				cssAttribute.setElementClassName("v-button-caption");
				cssAttribute.setSearchDirection(CssExtensionAttribute.SEARCH_DOWN);
		
				getCssExtension().addAttribute(cssAttribute);
			}
		}
	}
	
	/**
	 * Updates the styles regarding the border painted option.
	 * 
	 * @param pBorderPainted <code>true</code> to paint the border, <code>false</code> to hide it
	 */
	protected void updateBorderPaintedStyles(boolean pBorderPainted)
	{
		removeInternStyleName("v-button-noborder");
		
		if (!pBorderPainted)
		{
			addInternStyleName("v-button-noborder");
		}
	}
	
	/**
	 * Updates the styles regarding the border on mouse-over option.
	 * 
	 * @param pBorderOnMouseEntered <code>true</code> to show the border on mouse-over, 
	 *                              <code>false</code> to show it always
	 */
	protected void updateBorderOnMouseEnteredStyles(boolean pBorderOnMouseEntered)
	{
		removeInternStyleName("v-button-borderonmouse");

		if (pBorderOnMouseEntered)
		{
			if (isEnabled())
			{
				addInternStyleName("v-button-borderonmouse");
			}
		}

	}
	
	/**
	 * Positions caption over the image. Removes margins and sets the position of caption over the button image. Both image and caption are centered.
	 */
	private void positionCaptionOverImage()
	{
	    CssExtension csse = getCssExtension();
        csse.addAttribute("display", "block");
        
        removeAttributeFromClass(csse, "v-button-wrap", "display");
	    
	    CssExtensionAttribute csaPosition = new CssExtensionAttribute("position", "absolute", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaTop = new CssExtensionAttribute("top", "50%", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaLeft = new CssExtensionAttribute("left", "50%", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaTransform = new CssExtensionAttribute("transform", "translate(-50%, -50%)", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        //remove all margins, no gap needed, because now caption should be positioned directly over the image
        removeCaptionMargins();
        
        csse.addAttribute(csaPosition);
        csse.addAttribute(csaTop);
        csse.addAttribute(csaLeft);
        csse.addAttribute(csaTransform);
	}

	/**
	 * Removes all margin properties from css.
	 */
    private void removeCaptionMargins()
    {
        CssExtension csse = getCssExtension();
        
        CssExtensionAttribute csaMarginTop = new CssExtensionAttribute("margin-top", "4px", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaMarginBottom = new CssExtensionAttribute("margin-bottom", "4px", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaMarginLeft = new CssExtensionAttribute("margin-left", "4px", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaMarginRight = new CssExtensionAttribute("margin-right", "4px", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        
        csse.removeAttribute(csaMarginTop);
        csse.removeAttribute(csaMarginBottom);
        csse.removeAttribute(csaMarginRight);
        csse.removeAttribute(csaMarginLeft);
    }
	
	/**
	 * Removes all css attributes needed for caption positioned over the button image.
	 */
	private void positionCaptionBesideImage()
	{
	    CssExtension csse = getCssExtension();
        csse.addAttribute("display", "inline-grid");
        
        CssExtensionAttribute csaPosition = new CssExtensionAttribute("position", "absolute", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaTop = new CssExtensionAttribute("top", "50%", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaLeft = new CssExtensionAttribute("left", "50%", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaTransform = new CssExtensionAttribute("transform", "translate(-50%, -50%)", "v-button-caption", CssExtensionAttribute.SEARCH_DOWN);
        
        csse.removeAttribute(csaPosition);
        csse.removeAttribute(csaTop);
        csse.removeAttribute(csaLeft);
        csse.removeAttribute(csaTransform);
        
        updateGap();
	}
	
	/**
	 * Checks if all text and position alignments are {@code IAlignmentConstants.ALIGN_CENTER}. In this special case caption should be positioned over the button image.
	 * 
	 * @return Returns {@code true} if special case, {@code false} for other cases.
	 */
	private boolean isEveryAlignmentCentered()
	{
	    return (horizontalAlignment == IAlignmentConstants.ALIGN_CENTER
	            && verticalAlignment == IAlignmentConstants.ALIGN_CENTER
                    && iHorizontalPosition == IAlignmentConstants.ALIGN_CENTER
                    && iVerticalPosition == IAlignmentConstants.ALIGN_CENTER
                    );
	}
	
	/**
	 * Removes display attribute from specified css class.
	 * 
	 * @param pExtension the css extension
	 * @param pClassName the css class name
	 * @param pAttribute the attribute name
	 */
	private void removeAttributeFromClass(CssExtension pExtension, String pClassName, String pAttribute)
    {
        CssExtensionAttribute csaFlex = new CssExtensionAttribute();
        csaFlex.setElementClassName(pClassName);
        csaFlex.setAttribute(pAttribute);
        csaFlex.setSearchDirection(CssExtensionAttribute.SEARCH_DOWN);
        pExtension.removeAttribute(csaFlex);
    }
	
	/**
	 * Adds text position attributes to the specified css class. It adds two attributes: display and align-items.
	 * 
	 * @param pExtension the css extension
	 * @param pAlignmentValue the alignment attribute value
	 * @param pDirection whether icon and caption should be aligned in a row or a column
	 */
	private void addTextPositionAttributesToExtension(CssExtension pExtension, String pAlignmentValue, String pDirection)
    {
        CssExtensionAttribute csaFlex = new CssExtensionAttribute("display", "inline-flex", "v-button-wrap", CssExtensionAttribute.SEARCH_DOWN);
        CssExtensionAttribute csaAlignment = new CssExtensionAttribute("align-items", pAlignmentValue, "v-button-wrap", CssExtensionAttribute.SEARCH_DOWN);
        
        pExtension.addAttribute(csaFlex);
        pExtension.addAttribute(csaAlignment);
        
        if (pDirection != null)
        {
            CssExtensionAttribute csaColumnDirection = new CssExtensionAttribute("flex-direction", pDirection, "v-button-wrap", CssExtensionAttribute.SEARCH_DOWN);
            
            pExtension.addAttribute(csaColumnDirection);
        }
    }
	
	/**
	 * Maps vertical text position int values to css attribute value.
	 * 
	 * @return the attribute value
	 */
	private String getVerticalTextPositionValue()
    {
	    if (iVerticalPosition == IAlignmentConstants.ALIGN_TOP)
	    {
	        return "flex-start";
	    }
	    else if (iVerticalPosition == IAlignmentConstants.ALIGN_BOTTOM)
	    {
	        return "flex-end";
	    }
	    else
	    {
	        return "center";
	    }
    }
	
	/**
	 * Updates the accessibility attributes of this button.
	 */
	private void updateAccessibility()
	{
		AccessibilityUtil.setLabel(getAttributesExtension(), getToolTipText(), getText());
	}
	
}	// AbstractVaadinButton
