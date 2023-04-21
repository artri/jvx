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
 * 17.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.IVaadinContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;

/**
 * The <code>VaadinAbsoluteLayout</code> class is the layout, when there is no layout set (layout = null).
 * It is possible to set the position of the components with x and y coordinates.
 * 
 * @author Stefan Wurm
 */
public class VaadinAbsoluteLayout extends AbstractVaadinLayout<AbsoluteLayout, Object>
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Horizontal Gap. **/
	private int iHorizontalGap = 5;
	
	/** Vertical Gap. **/
	private int iVerticalGap = 5;
	
	/** Margin. **/
	private IInsets margins = new VaadinInsets(10, 10, 10, 10);

	/** All Components in this FormLayout. **/
	private List<IComponent> components = new ArrayList<IComponent>();
	
	/** The cssExtension for the absoluteLayout. **/
	private CssExtension cssExtension;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinAbsoluteLayout</code>.
	 */
	public VaadinAbsoluteLayout() 
	{
		super(new AbsoluteLayout());
		
		resource.setSizeUndefined();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void repaintLayout(boolean pComponentsChanged)
	{
		if (pComponentsChanged)
		{
			resource.removeAllComponents();
			
			for (IComponent component : components)
			{
				if (VaadinUtil.isParentNull(component))
				{
					if (component.getBounds() != null)
					{
						resource.addComponent((Component) component.getResource(), "top:" + component.getBounds().getY() + "px; left:" + component.getBounds().getX() + "px");
					}
					else
					{
						resource.addComponent((Component) component.getResource());
					}
				}
			}
		}
		
		performLayout();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * {@inheritDoc}
	 */	
	public void addComponent(IComponent pComponent, Object pConstraint, int pIndex)
	{
		components.add(pComponent);
		
		markComponentsChanged();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public void removeComponent(IComponent pComponent)
	{
		components.remove(pComponent);
		
		removeFromVaadin(pComponent, resource);
		
		markComponentsChanged();
	}	
	
	/**
	 * {@inheritDoc}
	 */	
	public Object getConstraints(IComponent pComponent)
	{
		if (components.contains(pComponent))
		{
			return ((VaadinComponentBase<?, ?>) pComponent).getConstraints();
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComponent, Object pConstraints)
	{
		if (components.contains(pComponent))
		{
			((VaadinComponentBase<?, ?>) pComponent).setConstraints(pConstraints);
		}
		
		markComponentsChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
		return margins;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		margins = pMargins;
		
		CssExtension csse = getCssExtension();
		
		if (margins != null)
		{		
			csse.addAttribute("padding-top", margins.getTop() + "px");
			csse.addAttribute("padding-left", margins.getLeft() + "px");
			csse.addAttribute("padding-right", margins.getRight() + "px");
			csse.addAttribute("padding-bottom", margins.getBottom() + "px");
		}
		else
		{
			csse.removeAttribute("padding-top");
			csse.removeAttribute("padding-left");
			csse.removeAttribute("padding-right");
			csse.removeAttribute("padding-bottom");				
		}

		resource.markAsDirty();		
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalGap()
	{
		return iHorizontalGap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalGap(int pHorizontalGap)
	{
		iHorizontalGap = pHorizontalGap;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalGap()
	{
		return iVerticalGap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalGap(int pVerticalGap)
	{
		iVerticalGap = pVerticalGap;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	private void performLayout() 
	{
		if (VaadinUtil.isParentWidthDefined(resource))
		{
			VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		for (IComponent component : components)
		{
			VaadinComponentBase<?, ?> componentBase = (VaadinComponentBase<?, ?>)component;
			
			if (componentBase.isPreferredSizeSet()) // Reinitialize the size of the components.
			{
				componentBase.setSize(componentBase.getPreferredSize());
			}
			else
			{
				componentBase.setSizeUndefined();
			}
			applySize(componentBase, "max", componentBase.getMaximumSize(), 
					componentBase.isMaximumSizeSet(), 
					componentBase.isMaximumSizeSet());
			applySize(componentBase, "min", componentBase.getMinimumSize(), 
					componentBase.isMinimumSizeSet(), 
					componentBase.isMinimumSizeSet());
			
			if (component instanceof IVaadinContainer)
			{
				((IVaadinContainer) component).performLayout();
			}
		}
	}	

	/**
	 * Gets the css extension.
	 * 
	 * @return the css extension
	 */
	public CssExtension getCssExtension()
	{
		if (cssExtension == null)
		{
			cssExtension = new CssExtension();
			cssExtension.extend(resource);
		}
		
		return cssExtension;
	}	
	
} 	// VaadinAbsoluteLayout
