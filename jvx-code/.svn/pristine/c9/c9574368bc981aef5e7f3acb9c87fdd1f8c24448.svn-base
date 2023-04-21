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
 * 26.11.2012 - [HM] - creation
 * 11.01.2013 - [SW] - implementation 
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFlowLayout;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * The <code>VaadinFlowLayout</code> class is the vaadin implementation of {@link IFlowLayout}.
 * 
 * @author Benedikt Cermak
 * @author Thomas Krautinger
 * @author Stefan Wurm
 */
public class VaadinFlowLayout extends AbstractVaadinLayout<HorizontalLayout, Object>
                              implements IFlowLayout
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** Current layout (horizontal or vertical). **/
	private AbstractOrderedLayout layout;
	
	/** CssExtension for the Vertical Layout. **/
	private CssExtension cssExtensionFlowLayout = null;		
	
	/** CssExtension for the layout. **/
	private CssExtension cssExtensionLayout = new CssExtension();	
	
	/** Vertical or Horizontal Orientation. **/
	private int orientation = HORIZONTAL;
	
	/** All Components in this FlowLayout. **/
	private List<IComponent> components = new ArrayList<IComponent>();
	
	/** Horizontal Gap. **/
	private int iHorizontalGap = 0;
	
	/** Vertical Gap. **/
	private int iVerticalGap = 0;
	
	/** Margin. **/
	private IInsets margins = new VaadinInsets(0, 0, 0, 0);
	
	/** Alignment of the components in the FlowLayout. **/
	private int componentAlignment = ALIGN_CENTER;
		
	/** Horizontal alignment of the layout inside the FlowLayout. **/
	private int horizontalAlignment = ALIGN_CENTER;	
	
	/** Vertical alignment of the layout inside the FlowLayout. **/
	private int verticalAlignment = ALIGN_CENTER;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an instance of the <code>VaadinFlowLayout</code>.
	 */
	public VaadinFlowLayout()
	{
		this(HORIZONTAL, 5, 5);
	}
	
	/**
	 * Creates an instance of the <code>VaadinFlowLayout</code> with the given orientation.
	 * 
	 * @param pOrientation horizontal or vertical orientation
	 */
	public VaadinFlowLayout(int pOrientation)
	{
		this(pOrientation, 5, 5);
	}
	
	/**
	 * Creates an instance of the <code>VaadinFlowLayout</code>.
	 *  
	 * @param pOrientation horizontal or vertical
	 * @param pHorizontalGap horizontal gap
	 * @param pVerticalGap vertical gap
	 */
	public VaadinFlowLayout(int pOrientation, int pHorizontalGap, int pVerticalGap) 
	{
		super(new HorizontalLayout());
		
		orientation = pOrientation;
		iHorizontalGap = pHorizontalGap;
		iVerticalGap = pVerticalGap;
		
		if (orientation == VERTICAL)
		{
			layout = new VerticalLayout();
		}
		else
		{
			layout = new HorizontalLayout();
		}
		
		resource.setMargin(false);
		resource.setSpacing(false);
		resource.setSizeUndefined();
		
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setSizeUndefined();
		
		cssExtensionLayout.extend(layout);
		
		resource.addComponent(layout);	
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
			layout.removeAllComponents();
			cssExtensionLayout.remove();
			
			if (orientation == HORIZONTAL) 
			{
				layout = new HorizontalLayout();
			} 
			else
			{
				layout = new VerticalLayout();				
			}
			
			layout.setMargin(false);
			layout.setSpacing(false);
			layout.setSizeUndefined();
			
			cssExtensionLayout = new CssExtension();
			cssExtensionLayout.extend(layout);
			
			for (int i = 0, cnt = components.size(); i < cnt; i++) 
			{
				IComponent component = components.get(i);
				
				if (i > 0)
				{
					if (orientation == HORIZONTAL)
					{
						if (iHorizontalGap > 0)
						{	
							Label expandingGap = new Label();
							
							VaadinUtil.setComponentWidth(expandingGap, iHorizontalGap, Unit.PIXELS);
							VaadinUtil.setComponentHeight(expandingGap, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
							
							layout.addComponent(expandingGap);
						}
					}
					else
					{
						if (iVerticalGap > 0)
						{	
							Label expandingGap = new Label();
							
							VaadinUtil.setComponentWidth(expandingGap, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
							VaadinUtil.setComponentHeight(expandingGap, iVerticalGap, Unit.PIXELS);
							
							layout.addComponent(expandingGap);
						}
					}
				}
				
				if (VaadinUtil.isParentNull(component))
				{
					layout.addComponent((Component) component.getResource());
				}
			}
			
			resource.addComponent(layout);
			resource.setExpandRatio(layout, 1f);
		}
		
		performLayout();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * {@inheritDoc}
     */
	public int getOrientation() 
	{
		return orientation;
	}
	
	/**
     * {@inheritDoc}
     */
	public void setOrientation(int pOrientation) 
	{	
		orientation = pOrientation;
		
		markComponentsChanged();
	}
	
    /**
	 * {@inheritDoc}
	 */
	public int getComponentAlignment()
	{
		return componentAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setComponentAlignment(int pComponentAlignment)
	{
		componentAlignment = pComponentAlignment;
			
		markComponentsChanged();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoWrap()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoWrap(boolean pAutoWrap) 
	{	
		if (orientation == HORIZONTAL) 
		{
			if (pAutoWrap) 
			{
				cssExtensionLayout.addAttribute("white-space", "initial", true);
			} 
			else 
			{

				cssExtensionLayout.addAttribute("white-space", "nowrap", true);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public Object getConstraints(IComponent pComp)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComp, Object pConstraints)
	{
		//throw new IllegalArgumentException("Constraints can't been set!");
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
    	margins = (VaadinInsets)pMargins;
    	
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
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalGap(int pHorizontalGap) 
	{	
		iHorizontalGap = pHorizontalGap;
		
		markComponentsChanged();
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
	public void setVerticalGap(int pVerticalGap) 
	{
		iVerticalGap = pVerticalGap;		

		markComponentsChanged();
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
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		markComponentsChanged();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}	

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		markComponentsChanged();
	}
	
	/**
     * {@inheritDoc}
     */
	public void addComponent(IComponent pComponent, Object pConstraint, int pIndex)
	{
		if (pIndex < 0)
		{
			pIndex = components.size();
		}

		components.add(pIndex, pComponent);	
		
		markComponentsChanged();
	}
			
	 /**
     * {@inheritDoc}
     */
	public void removeComponent(IComponent pComponent)
	{	
		components.remove(pComponent);
		
		removeFromVaadin(pComponent, layout);
		
		markComponentsChanged();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	private void performLayout()
	{
		VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
		
//		if (VaadinUtil.isParentWidthDefined(resource))
//		{
//			VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
//		}
//		else
//		{
//			VaadinUtil.setComponentWidth(resource, Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
//		}
		
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		if (horizontalAlignment == ALIGN_STRETCH && VaadinUtil.isParentWidthDefined(layout))
		{
			VaadinUtil.setComponentWidth(layout, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentWidth(layout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		if (verticalAlignment == ALIGN_STRETCH && VaadinUtil.isParentHeightDefined(layout))
		{
			VaadinUtil.setComponentHeight(layout, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentHeight(layout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
		
		resource.setComponentAlignment(layout, VaadinUtil.getVaadinAlignment(horizontalAlignment, verticalAlignment));
		
		float maxComponentWidth = getMaxComponentWidth();
		float maxComponentHeight = getMaxComponentHeight();
		float componentWidth = VaadinUtil.SIZE_UNDEFINED;
		float componentHeight = VaadinUtil.SIZE_UNDEFINED;
		
		for (IComponent component : components)
		{
			setIgnorePerformLayout(component, true);
			
			try
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
				
				boolean horizontalStretched = componentAlignment == ALIGN_STRETCH 
						&& VaadinUtil.isParentWidthDefined((Component) component.getResource()) && horizontalAlignment == ALIGN_STRETCH;
				boolean verticalStretched = componentAlignment == ALIGN_STRETCH 
						&& VaadinUtil.isParentHeightDefined((Component) component.getResource()) && verticalAlignment == ALIGN_STRETCH;
				
				applySize(componentBase, "max", componentBase.getMaximumSize(), 
						componentBase.isMaximumSizeSet() && !horizontalStretched, 
						componentBase.isMaximumSizeSet() && !verticalStretched);
				applySize(componentBase, "min", componentBase.getMinimumSize(), 
						componentBase.isMinimumSizeSet(), 
						componentBase.isMinimumSizeSet());
				
				componentWidth = ((Component) component.getResource()).getWidth();
				componentHeight = ((Component) component.getResource()).getHeight();
				
				if (componentAlignment == ALIGN_STRETCH)
				{
					if (horizontalStretched)
					{
						componentBase.setWidthFull();
					}
					else
					{
						if (orientation == VERTICAL)
						{
							VaadinUtil.setComponentWidth((Component)component.getResource(), Math.max(componentWidth, maxComponentWidth), Unit.PIXELS);
						}
						else
						{
							VaadinUtil.setComponentWidth((Component)component.getResource(), Math.max(componentWidth, VaadinUtil.SIZE_UNDEFINED), Unit.PIXELS);
						}
					}
					
					if (verticalStretched)
					{
						componentBase.setHeightFull();
					}
					else
					{
						if (orientation == VERTICAL)
						{
							VaadinUtil.setComponentHeight((Component)component.getResource(), Math.max(componentHeight, VaadinUtil.SIZE_UNDEFINED), Unit.PIXELS);	
						}
						else
						{
							VaadinUtil.setComponentHeight((Component)component.getResource(), Math.max(componentHeight, maxComponentHeight), Unit.PIXELS);
						}
					}
	
				}
				else
				{
					if (layout.getComponentIndex((Component) component.getResource()) >= 0)
					{
						layout.setComponentAlignment((Component)component.getResource(), VaadinUtil.getVaadinAlignment(componentAlignment, componentAlignment));		
					}
				}
			}
			finally
			{
				setIgnorePerformLayout(component, false);
			}
		}
	}	
	
	/**
	 * Returns the height of the highest component.
	 * 
	 * @return the height of the highest component.
	 */
	private float getMaxComponentHeight()
	{
		float maxHeight = VaadinUtil.SIZE_UNDEFINED;

		for (IComponent component : components)
		{
			IDimension size = component.getSize();
			
			if (size != null)
			{
				maxHeight = Math.max(maxHeight, size.getHeight());
			}
		}

		return maxHeight;
		
	}
	
	/**
	 * Returns the width of the widest component.
	 * 
	 * @return the width of the widest component.
	 */
	private float getMaxComponentWidth()
	{
		float maxWidth = VaadinUtil.SIZE_UNDEFINED;
		
		for (IComponent component : components)
		{
			IDimension size = component.getSize();
			
			if (size != null)
			{
				maxWidth = Math.max(maxWidth, size.getWidth());
			}
		}

		return maxWidth;
	}

	/**
	 * Gets the css extension.
	 * 
	 * @return the css extension
	 */
	public CssExtension getCssExtension()
	{
		if (cssExtensionFlowLayout == null)
		{
			cssExtensionFlowLayout = new CssExtension();
			cssExtensionFlowLayout.extend(resource);
		}

		return cssExtensionFlowLayout;
	}		
	
}	// VaadinFlowLayout
