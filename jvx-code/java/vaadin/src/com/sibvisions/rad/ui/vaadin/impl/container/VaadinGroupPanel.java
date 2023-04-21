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
 * 18.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.container.IGroupPanel;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;
import com.vaadin.ui.Panel;

/**
 * The <code>VaadinGroupPanel</code> class is the vaadin implementation of {@link IGroupPanel}.
 * 
 * @author Stefan Wurm
 */
public class VaadinGroupPanel extends VaadinSingleComponentContainer<Panel> 
                              implements IGroupPanel 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinGroupPanel</code> with the given panel title.
     */
    public VaadinGroupPanel()
    {
        this(null);
    }
    
	/**
	 * Creates a new instance of <code>VaadinGroupPanel</code> with the given panel title.
	 * 
	 * @param pTitle the title of the panel
	 */
	public VaadinGroupPanel(String pTitle)
	{
		super(new Panel(pTitle));

		resource.setSizeUndefined();
		
		resource.setStyleName("v-grouppanel");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
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
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
	{		
		font = pFont;
		
		if (pFont != null) 
		{
			VaadinFont vaadinFont = new VaadinFont(pFont.getFontName(), pFont.getStyle(), pFont.getSize());

			getCssExtension().addAttributes(vaadinFont.getStyleAttributes("v-panel-caption", CssExtensionAttribute.SEARCH_DOWN, true));	
		}
		else
		{
			CssExtension csse = getCssExtension();

			CssExtensionAttribute attrib;
			
			for (String sFont : new String[] {"font-weight", "font-style", "font-family", "font-size"})
			{
				attrib = new CssExtensionAttribute(sFont, null, "v-panel-caption", CssExtensionAttribute.SEARCH_DOWN);
				attrib.setExactMatch(true);
				
				csse.removeAttribute(attrib);
			}
		}		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pForeground)
	{
		foreground = pForeground;
				
		if (foreground != null) 
		{
			CssExtensionAttribute attribute = new CssExtensionAttribute("color", ((VaadinColor)pForeground).getStyleValueRGB(), 
																		"v-panel-caption", CssExtensionAttribute.SEARCH_DOWN);
			attribute.setExactMatch(true);
			
			getCssExtension().addAttribute(attribute);	
		}
		else
		{
			CssExtensionAttribute attribute = new CssExtensionAttribute("color", null, 
																		"v-panel-caption", CssExtensionAttribute.SEARCH_DOWN);
			attribute.setExactMatch(true);
			
			getCssExtension().removeAttribute(attribute);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		CssExtensionAttribute cssAttribute = new CssExtensionAttribute("text-align", "left", "v-panel-caption", CssExtensionAttribute.SEARCH_DOWN);
		cssAttribute.setExactMatch(true);
		
		CssExtension csse = getCssExtension();
		
		csse.removeAttribute(cssAttribute);
		
		if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
		{
			cssAttribute.setValue("left");
			csse.addAttribute(cssAttribute);
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
		{
			cssAttribute.setValue("center");
			csse.addAttribute(cssAttribute);
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
		{
			cssAttribute.setValue("right");
			csse.addAttribute(cssAttribute);
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
		{
			// Do nothing.
		}
		
		horizontalAlignment = pHorizontalAlignment;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
	}

} 	// VaadinGroupPanel
