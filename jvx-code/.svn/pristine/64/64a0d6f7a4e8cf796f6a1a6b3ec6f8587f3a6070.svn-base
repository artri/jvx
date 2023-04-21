/*
 * Copyright 2013 SIB Visions GmbH
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
 * 11.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.sibvisions.rad.ui.vaadin.ext.INamedResource;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

/**
 * The <code>ChoiceField</code> class is the server-side component for a choice field. A choice field has 
 * multiple possible values but only one value is selected. The value will be represented as an image. 
 * This component handles value toggling.
 * 
 * @author Stefan Wurm
 */
public class ChoiceField extends NativeButton 
                         implements ClickListener,
                                    IDynamicCss
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** A empty object array. */
	private static final Object[] EMPTY_OBJECTARRAY = new Object[] {};

	/** A empty image array. */
	private static final Resource[] EMPTY_IMAGEARRAY = new Resource[] {};
	
	/** The list of allowed values. */
	private Object[] allowedValues; 
	
	/** The list of allowed values. */
	private Resource[] images;
	
	/** The default image shown when selectedIndex is -1. */
	private Resource defaultImage = null;
	
	/** the CSS extension. */
	private CssExtension cssExtension;
	
	/** the attributes extension. */
	private AttributesExtension attExtension;
	
	/** The selected index. */
	private boolean imageBorderVisible = false;

	/** The selected index. */
	private int selectedIndex = -1;	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ChoiceField</code>.
	 */
	public ChoiceField() 
	{
		this(null, null);
	}
	
	/**
	 * Creates a new instance of <code>ChoiceField</code> with predefined values.
	 * 
	 * @param pAllowedValues the allowed values
	 * @param pImages the images for the allowed values
	 */
	public ChoiceField(Object[] pAllowedValues, Resource[] pImages)
	{
		super();
	
		setStyleName("link v-choicefield");
		addStyleName("v-button-caption-nooutline");
		
		cssExtension = new CssExtension();
		cssExtension.extend(this);
		
		cssExtension.addAttribute("min-width", "20px");
		cssExtension.addAttribute("min-height", "20px");
		
		addClickListener(this);
		
		setAllowedValues(pAllowedValues);
		setImages(pImages);
		
		addClickListener(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * If the button is clicked, select the next index.
	 * 
	 * @param pEvent the click event.
	 */
	public void buttonClick(ClickEvent pEvent)
	{
		if (isEnabled())
		{
			selectNextIndex();
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public CssExtension getCssExtension()
	{
	    return cssExtension;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIcon(Resource pIcon)
	{
		if (getIcon() instanceof INamedResource)
		{
			removeStyleName(((INamedResource)getIcon()).getStyleName());
		}

        VaadinUtil.removeFontIconStyles(this, getIcon());
		
		super.setIcon(pIcon);
		
		if (pIcon instanceof INamedResource)
		{
			addStyleName(((INamedResource)pIcon).getStyleName());
		}
	
		VaadinUtil.applyFontIconStyles(this, pIcon, "v-icon");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the allowed values.
	 * 
	 * @return the allowed values.
	 */
	public Object[] getAllowedValues()
	{
		return allowedValues;
	}

	/**
	 * Sets the allowed values.
	 * 
	 * @param pAllowedValues the allowed values.
	 */
	public void setAllowedValues(Object[] pAllowedValues)
	{
		Object item = getSelectedItem();
		
		if (pAllowedValues == null)
		{
			allowedValues = EMPTY_OBJECTARRAY;
		}
		else
		{
			allowedValues = pAllowedValues;
		}
	
		setSelectedItem(item);
	}

	/**
	 * Returns all Images as a resource.
	 * 
	 * @return all Images as a resource
	 */
	public Resource[] getImages()
	{
		return images;
	}

	/**
	 * Sets all images.
	 * 
	 * @param pImages all images.
	 */
	public void setImages(Resource[] pImages)
	{
		if (pImages == null || pImages.length == 0)
		{
			images = EMPTY_IMAGEARRAY;
		}
		else
		{
			images = pImages;
		}
		
		setSelectedIndex(getSelectedIndex());
	}

	/**
	 * Returns the default image. 
	 * If no default image is set the return value is null.
	 * 
	 * @return the default image. null if no default value is set.
	 */
	public Resource getDefaultImage()
	{
		return defaultImage;
	}

	/**
	 * Sets the default value.
	 * 
	 * @param pDefaultImage the default value.
	 */
	public void setDefaultImage(Resource pDefaultImage)
	{
		defaultImage = pDefaultImage;
		
		setSelectedIndex(getSelectedIndex());
	}

	/**
	 * Returns true if the border is visible.
	 * False if the border is not visible.
	 * 
	 * @return <code>true</code> if the border is visible.
	 */
	public boolean isImageBorderVisible()
	{
		return imageBorderVisible;
	}

	/**
	 * Sets if the border around the image should be visible.
	 * 
	 * @param pImageBorderVisible <code>true</code> if the border should be visible.
	 */
	public void setImageBorderVisible(boolean pImageBorderVisible)
	{
		imageBorderVisible = pImageBorderVisible;
		
		if (imageBorderVisible)
		{
			addStyleName("v-icon-border");
		}
		else
		{
			removeStyleName("v-icon-border");
		}
		
		markAsDirty();
	}
	
	/**
	 * Returns the selected index.
	 * 
	 * @return the selected index.
	 */
	public int getSelectedIndex()
	{
		return selectedIndex;
	}

	/**
	 * Sets the selected index.
	 * 
	 * @param pSelectedIndex the selected index.
	 */
	public void setSelectedIndex(int pSelectedIndex)
	{
		selectedIndex = pSelectedIndex;
		
		if ((selectedIndex < 0 || selectedIndex >= allowedValues.length)) 
		{
		    setIcon(defaultImage);
		}
		else
		{
			if (allowedValues.length > images.length)
			{
				if (selectedIndex >= images.length)
				{
					setIcon(defaultImage);
				}
				else
				{
					setIcon(images[selectedIndex]);
				}
			}
			else
			{
				setIcon(images[selectedIndex]);
			}
		}
		
		if (allowedValues.length == 2)
		{
			AccessibilityUtil.setPressed(getAttributesExtension(), selectedIndex == 0);
		}
	}
	
	/**
	 * Selects the next index. This functionality is round robin. 
	 */
	public void selectNextIndex()
	{
		if (isEnabled())
		{
			if (selectedIndex + 1 < allowedValues.length)
			{
				setSelectedIndex(selectedIndex + 1);
			}
			else
			{
				setSelectedIndex(0);
			}
		}
	}	
	
	/**
	 * Gets the current selected value, or null, if no one is selected.
	 * Be careful, null can also be a allowed value. 
	 * @return the selected value.
	 */
	public Object getSelectedItem()
	{
		if (selectedIndex < 0 || allowedValues == null || selectedIndex >= allowedValues.length)
		{
			return null;
		}
		else
		{
			return allowedValues[selectedIndex];
		}
	}

	/**
	 * Sets the value that should be selected. If the value i not included in the 
	 * allowed values list, the selected index is set to -1.
	 * Be careful, null can also be a allowed value. 
	 * 
	 * @param pSelectedItem the value to select.
	 */
	public void setSelectedItem(Object pSelectedItem)
	{
		for (int i = 0; i < allowedValues.length; i++)
		{
			if (pSelectedItem == allowedValues[i] || (pSelectedItem != null && pSelectedItem.equals(allowedValues[i])))
			{
				setSelectedIndex(i);
				return;
			}
		}

		setSelectedIndex(-1);
	}	
	
	/**
	 * Returns the selected image.
	 * 
	 * @return the selected image as resource.
	 */
	public Resource getSelectedImage()
	{
		if (selectedIndex >= 0) 
		{
			if (allowedValues.length > images.length)
			{
				if (selectedIndex >= images.length)
				{
					return defaultImage;
				}
				else
				{
					return images[selectedIndex];
				}
			}
			else
			{
				return images[selectedIndex];
			}
		}
		
		return defaultImage;
	}
	
	/**
	 * Creates the attributes extension if needed.
	 * 
	 * @return the extension
	 */
	public AttributesExtension getAttributesExtension()
	{
		if (attExtension == null)
		{
			attExtension = new AttributesExtension();
			attExtension.extend(this);
		}
		
		return attExtension;
	}
	
} 	// ChoiceField
