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
 * 15.10.2012 - [CB] - creation
 * 01.10.2015 - [JR] - FontAwesome support
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IIcon;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.layout.IVaadinLayout;
import com.vaadin.server.FontIcon;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Image;

/**
 * The <code>VaadinIcon</code> is the vaadin implementation of {@link IIcon}.
 * 
 * @author Benedikt Cermak
 * @see com.vaadin.ui.Image
 * @see javax.rad.ui.component.IIcon
 */
public class VaadinIcon extends VaadinComponent<SimplePanel> 
                        implements IIcon
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the IconImage. */ 
    private VaadinImage vaadinImage;
    
    /** The vaadin image component. **/
    private Image image = new Image();
    
    /** CssExtension to send style attributes to the client component. **/
    private CssExtension cssInnerIconExtension = null;
    
    /** CssExtension to send style attributes to the client component. **/
    private CssExtension cssIconExtension = null;
    
    /** the main icon panel for the css display: table. It is important do do vertical alignment. */
    private SimplePanel panIcon = new SimplePanel();

    /** the inner Panel for the css display: table-cell. It is important to do vertical alignment. **/
    private SimplePanel panIconInner = new SimplePanel();
    
    /** The {@link CssExtension} which is used for the image. */
    private CssExtension cssImageExtension = null;
    
    /** If the aspect ratio of the image should be preserved when stretched. */
    private boolean preserveAspectRatio = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinIcon</code>.
     */
    public VaadinIcon()
    {
        super(new SimplePanel());
        
        resource.setSizeUndefined();
        panIcon.setSizeFull();
        panIconInner.setSizeFull();
        
        panIcon.setStyleName("jvxicon");
        
        panIconInner.setStyleName("jvxinnericon");
            
        panIcon.setContent(panIconInner);
        resource.setContent(panIcon);

        horizontalAlignment = ALIGN_CENTER;
        verticalAlignment = ALIGN_CENTER;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public IImage getImage()
    {
        return vaadinImage;
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreserveAspectRatio()
	{
		return preserveAspectRatio;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		if (preserveAspectRatio != pPreserveAspectRatio)
		{ 
			preserveAspectRatio = pPreserveAspectRatio;
		
			setHorizontalAlignment(horizontalAlignment);
			setVerticalAlignment(verticalAlignment);
		}
	}

    /**
     * {@inheritDoc}
     */
    public void setImage(IImage pImage)
    {
    	if (pImage != vaadinImage)
    	{
	        if (vaadinImage != null)
	        {
	            String styleName = vaadinImage.getStyleName();
	            
	            if (styleName != null)
	            {
	                panIconInner.removeStyleName(styleName);
	            }
	
	            VaadinUtil.removeFontIconStyles(this, (Resource)vaadinImage.getResource());
	        }
	        
	        vaadinImage = (VaadinImage) pImage;
	
            panIconInner.setContent(null);
	        if (vaadinImage != null)
	        {
	            String styleName = vaadinImage.getStyleName();
	            
	            if (styleName != null)
	            {
	                panIconInner.addStyleName(styleName);
	            }           
	            
	            if (vaadinImage.getResource() instanceof FontIcon)
	            {
	                //works without caption!
	                image.setIcon(vaadinImage.getResource());
	                
	                //reset source, otherwise the image will be shown
	                image.setSource(null);
	            }
	            else
	            {
	                //reset source, otherwise the icon/caption will be shown
	                image.setIcon(null);
	
	                //don't use setIcon because the caption will be shown!
	                image.setSource(vaadinImage.getResource());
	            }
	
	            image.setStyleName("jvximage");
	            
	            if (vaadinImage.getResource() instanceof FontIcon)
	            {
	                //hide the image tag
	                image.addStyleName("imghidden");
	            }
	            
	            panIconInner.setContent(image);
	                
	            setHorizontalAlignment(horizontalAlignment);
	            setVerticalAlignment(verticalAlignment);
	            
	            VaadinUtil.applyFontIconStyles(this, (Resource)vaadinImage.getResource(), "v-icon");
	            
	            image.setHeightUndefined();         
	            image.setWidthUndefined();
	        }
	        
	        if (getParent() != null && getParent().getLayout() != null)
	        {
	            ((IVaadinLayout)getParent().getLayout()).markDirty();
	        }
    	}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(IDimension pSize)
    {
        if (pSize == null)
        {
            resource.setSizeUndefined();

            bounds.setWidth(-1);
            bounds.setHeight(-1);
        }
        else
        {
            bounds.setWidth(pSize.getWidth());
            bounds.setHeight(pSize.getHeight());
            
            if (pSize.getWidth() < 0)
            {
                setWidth(VaadinUtil.SIZE_UNDEFINED);
            }
            else
            {
                setWidth(pSize.getWidth());
            }
            
            if (pSize.getHeight() < 0)
            {
                setHeight(VaadinUtil.SIZE_UNDEFINED);
            }
            else
            {
                setHeight(pSize.getHeight());
            }   
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setHorizontalAlignment(int pHorizontalAlignment)
    {
        horizontalAlignment = pHorizontalAlignment; 
        
        CssExtension cssInner = getCssInnerIconExtension();
        
        cssInner.removeAttribute("text-align");
        
        if (vaadinImage != null && vaadinImage.getResource() instanceof FontIcon && horizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
        {
            pHorizontalAlignment = IAlignmentConstants.ALIGN_CENTER;
        }
        
        if (pHorizontalAlignment == IAlignmentConstants.ALIGN_STRETCH && VaadinUtil.isParentWidthDefined(resource))
        {
            setImageWidth(100f, Unit.PERCENTAGE);
        }
        else if (vaadinImage != null && !isSizeSet() && !isPreferredSizeSet()) //#1517: if size was set -> don't change the size
        {
            int iWidth = vaadinImage.getWidth();
            if (iWidth >= 0)
            {
                setImageWidth(iWidth, Unit.PIXELS);
            }
        }
            
        if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
        {
            cssInner.addAttribute("text-align", "left");
        }
        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
        {
            cssInner.addAttribute("text-align", "center");
        }
        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
        {
            cssInner.addAttribute("text-align", "right");
        }
//        else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
//        {
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVerticalAlignment(int pVerticalAlignment)
    {
        verticalAlignment = pVerticalAlignment;
        
        CssExtension cssInner = getCssInnerIconExtension();
        
        CssExtension cssIcon = getCssOuterIconExtension();

        cssIcon.removeAttribute("display");
        cssInner.removeAttribute("display");
        cssInner.removeAttribute("vertical-align");
        
        if (vaadinImage != null && vaadinImage.getResource() instanceof FontIcon && verticalAlignment == IAlignmentConstants.ALIGN_STRETCH)
        {
            pVerticalAlignment = IAlignmentConstants.ALIGN_CENTER;
        }
        
        if (pVerticalAlignment == IAlignmentConstants.ALIGN_STRETCH && VaadinUtil.isParentWidthDefined(resource))
        {
            setImageHeight(100f, Unit.PERCENTAGE);
        }
        else if (vaadinImage != null && !isSizeSet() && !isPreferredSizeSet()) //#1517: if size was set -> don't change the size
        {
            int iHeight = vaadinImage.getHeight();
            if (iHeight >= 0)
            {
                setImageHeight(iHeight, Unit.PIXELS);
            }
        }
            
        if (pVerticalAlignment == IAlignmentConstants.ALIGN_BOTTOM)
        {
            cssIcon.addAttribute("display", "table");
            cssInner.addAttribute("display", "table-cell");
            cssInner.addAttribute("vertical-align", "bottom");
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_CENTER)
        {
            cssIcon.addAttribute("display", "table");
            cssInner.addAttribute("display", "table-cell");
            cssInner.addAttribute("vertical-align", "middle");
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_TOP)
        {
            cssIcon.addAttribute("display", "table");
            cssInner.addAttribute("display", "table-cell");
            cssInner.addAttribute("vertical-align", "top");
        }
        else if (pVerticalAlignment == IAlignmentConstants.ALIGN_STRETCH)
        {
            cssIcon.addAttribute("display", "block");
            cssInner.addAttribute("display", "block");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setWidthFull()
    {
        if (VaadinUtil.isParentWidthDefined(resource))
        {
            ((AbstractComponent)resource).setWidth(100f, Unit.PERCENTAGE);
            
            if (getHorizontalAlignment() == ALIGN_STRETCH)
            {   
                setImageWidth(100f, Unit.PERCENTAGE);
            }
            else
            {
                if (vaadinImage != null)
                {
                    int iWidth = vaadinImage.getWidth();
                    
                    if (iWidth >= 0)
                    {
                        setImageWidth(iWidth, Unit.PIXELS);
                    }
                }
            }
        }
        else
        {
            setWidthUndefined();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeightFull()
    {
        if (VaadinUtil.isParentHeightDefined(resource))
        {
            ((AbstractComponent)resource).setHeight(100f, Unit.PERCENTAGE);
            
            if (getVerticalAlignment() == ALIGN_STRETCH)
            {   
            	setImageHeight(100f, Unit.PERCENTAGE);
            }
            else
            {
                if (vaadinImage != null)
                {
                    int iHeight = vaadinImage.getHeight();
                    
                    if (iHeight >= 0)
                    {
                    	setImageHeight(iHeight, Unit.PIXELS);
                    }
                }
            }
        }
        else
        {
            setHeightUndefined();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setWidthUndefined()
    {
        ((AbstractComponent)resource).setWidth(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
        
        if (vaadinImage != null)
        {
            int iWidth = vaadinImage.getWidth();
            
            if (iWidth >= 0)
            {
                setImageWidth(iWidth, Unit.PIXELS);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeightUndefined()
    {
        ((AbstractComponent)resource).setHeight(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
        
        if (vaadinImage != null)
        {
            int iHeight = vaadinImage.getHeight();
            
            if (iHeight >= 0)
            {
                setImageHeight(iHeight, Unit.PIXELS);
            }
        }
    }   
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSizeFull()
    {
        setWidthFull();
        setHeightFull();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSizeUndefined()
    {
        setWidthUndefined();
        setHeightUndefined();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets the width for the the icon.
     * 
     * @param pWidth the width
     */
    public void setWidth(float pWidth)
    {
        bounds.setWidth((int)pWidth);
        
        VaadinUtil.setComponentWidth(resource, pWidth, Unit.PIXELS);
        
        if (vaadinImage != null)
        {
            if (pWidth == VaadinUtil.SIZE_UNDEFINED)
            {
                int iWidth = vaadinImage.getWidth();
                
                if (iWidth >= 0)
                {
                    setImageWidth(iWidth, Unit.PIXELS);
                }
            }
            else
            {
                setImageWidth(pWidth, Unit.PIXELS);
            }
        }
    }
    
    /**
     * Sets the height for the the icon.
     * 
     * @param pHeight the height
     */
    public void setHeight(float pHeight)
    {
        bounds.setHeight((int)pHeight);
        
        VaadinUtil.setComponentHeight(resource, pHeight, Unit.PIXELS);
        
        if (vaadinImage != null)
        {
            if (pHeight == VaadinUtil.SIZE_UNDEFINED)
            {
                int iHeight = vaadinImage.getHeight();
                
                if (iHeight >= 0)
                {
                    setImageHeight(iHeight, Unit.PIXELS);
                }
            }
            else
            {
                setImageHeight(pHeight, Unit.PIXELS);
            }
        }
    }
    
    /**
     * Gets the icon resource. This resource should be used for styling.
     * 
     * @return the icon resource
     */
    public SimplePanel getIconResource()
    {
        return panIcon;
    }
    
    /**
     * Gets the css extension.
     * 
     * @return the css extension
     * @throws RuntimeException if resource is not an instance of {@link AbstractComponent}
     */
    public CssExtension getCssInnerIconExtension()
    {
        if (cssInnerIconExtension == null)
        {
            cssInnerIconExtension = new CssExtension();
            cssInnerIconExtension.extend(panIconInner);
        }
        
        return cssInnerIconExtension;
    }
    
    /**
     * Gets the css extension.
     * 
     * @return the css extension
     * @throws RuntimeException if resource is not an instance of {@link AbstractComponent}
     */
    public CssExtension getCssOuterIconExtension()
    {
        if (cssIconExtension == null)
        {
            cssIconExtension = new CssExtension();
            cssIconExtension.extend(panIcon);
        }
        
        return cssIconExtension;
    }
    
    /**
     * Sets the image height.
     * 
     * @param pValue the value.
     * @param pUnit the unit.
     */
    private void setImageHeight(float pValue, Unit pUnit)
    {
    	image.setHeight(pValue, pUnit);
    	
    	fixImageSize();
    }

    /**
     * Sets the image width.
     * 
     * @param pValue the value.
     * @param pUnit the unit.
     */
    private void setImageWidth(float pValue, Unit pUnit)
    {
    	image.setWidth(pValue, pUnit);
    	
    	fixImageSize();
    }
    
    /**
     * Fixes the size of the images according to if the aspect ratio should be
     * preserved.
     */
    private void fixImageSize()
    {
    	if (cssImageExtension == null)
    	{
    		cssImageExtension = new CssExtension();
    		cssImageExtension.extend(image);
    	}
    	
		cssImageExtension.removeAttribute("max-height");
		cssImageExtension.removeAttribute("max-width");
		cssImageExtension.removeAttribute("object-fit");
		
    	if (preserveAspectRatio)
    	{
	    	if (isStretched(image.getWidth(), image.getWidthUnits())
	    			&& !isStretched(image.getHeight(), image.getHeightUnits()))
	    	{
	    		image.setHeightUndefined();
	    	}
	    	else if (!isStretched(image.getWidth(), image.getWidthUnits())
		    			&& isStretched(image.getHeight(), image.getHeightUnits()))
	    	{
	    		image.setWidthUndefined();
	    	}
	    	else if (isStretched(image.getWidth(), image.getWidthUnits())
	    			&& isStretched(image.getHeight(), image.getHeightUnits()))
	    	{
	    		cssImageExtension.addAttribute("max-height", "100%");
	    		cssImageExtension.addAttribute("max-width", "100%");
	    		cssImageExtension.addAttribute("object-fit", "contain");
	    	}
    	}
    }
    
    /**
     * If the given values equal stretching.
     * 
     * @param pValue the value.
     * @param pUnit the unit.
     * @return {@code true} if stretching.
     */
    private boolean isStretched(float pValue, Unit pUnit)
    {
    	return Math.abs(100.0f - pValue) < 0.01f && pUnit == Unit.PERCENTAGE;
    }
    
}   // VaadinIcon
