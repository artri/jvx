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
 * 01.10.2012 - [CB] - creation
 * 10.01.2013 - [SW] - implementation
 */
package com.sibvisions.rad.ui.vaadin.impl.layout;

import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.Style;
import javax.rad.ui.container.IGroupPanel;
import javax.rad.ui.layout.IBorderLayout;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.IVaadinContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinLabel;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * The <code>VaadinBorderLayout</code> class is the vaadin implementation of {@link IBorderLayout}.
 * 
 * @author Stefan Wurm
 */
public class VaadinBorderLayout extends AbstractVaadinLayout<VerticalLayout, String>
                                implements IBorderLayout
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** HorizontalLayout. **/
	private HorizontalLayout centerLayout;

	/** default component north. **/
	protected IComponent northComponent;
	
	/** default component westComponent. **/
	protected IComponent westComponent;
	
	/** default component centerComponent. **/
	protected IComponent centerComponent;
	
	/** default component eastComponent. **/
	protected IComponent eastComponent;
	
	/** default component southComponent. **/
	protected IComponent southComponent;
	
	/** The parent component. **/
	private SingleComponentContainer parent = null;
	
	/** For the horizontalGap. **/
	private Component horizontalGapWestCenter = new Label("");
	
	/** For the horizontalGap. **/
	private Component horizontalGapCenterEast = new Label("");
	
	/** For the verticalGap. **/
	private Component verticalGapNorthCenter = new Label("");	
	
	/** For the verticalGap. **/
	private Component verticalGapCenterSouth = new Label("");	
	
	/** Norht set. **/
	private boolean bNorthComponentSet = false;
	
	/** East set. **/
	private boolean bEastComponentSet = false;
	
	/** Center set. **/
	private boolean bCenterComponentSet = true;
	
	/** West set. **/
	private boolean bWestComponentSet = false;
	
	/** South set. **/
	private boolean bSouthComponentSet = false;	
	
	/** the margins of the layout. */
	protected VaadinInsets margins = new VaadinInsets(0, 0, 0, 0);
	
	/** the horizontal gap. */
	protected int horizontalGap = 0;
	
	/** the vertical gap. */
	protected int verticalGap = 0;	
	
	/** Padding Top Attribute. **/
	private CssExtensionAttribute paddingTop;
	
	/** Padding Bottom Attribute. **/
	private CssExtensionAttribute paddingBottom;
	
	/** Padding Left Attribute. **/
	private CssExtensionAttribute paddingLeft;
	
	/** Padding Right Attribute. **/
	private CssExtensionAttribute paddingRight;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinBorderLayout</code>.
     *
     * @see javax.rad.ui.layout.IBorderLayout
     */
	public VaadinBorderLayout()
	{
		super(new VerticalLayout());
		
		centerLayout = new HorizontalLayout();
		
		northComponent = new VaadinLabel("");
		Style.addStyleNames(northComponent, "northlabel");
		
		westComponent = new VaadinLabel("");
		Style.addStyleNames(westComponent, "westlabel");
		
		centerComponent = new VaadinLabel("");
		Style.addStyleNames(centerComponent, "centerlabel");
		
		eastComponent = new VaadinLabel("");
		Style.addStyleNames(eastComponent, "eastlabel");
		
		southComponent = new VaadinLabel("");
		Style.addStyleNames(southComponent, "southlabel");
		
		resource.setSizeUndefined();
		centerLayout.setSizeUndefined();
		
		resource.setMargin(false);
		centerLayout.setMargin(false);
		resource.setSpacing(false);
		centerLayout.setSpacing(false);
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
			if (bNorthComponentSet || bSouthComponentSet)
			{
				resource.removeAllComponents();
			}
			
			if (bWestComponentSet || bEastComponentSet)
			{
				centerLayout.removeAllComponents();
			}
			
			if (!VaadinUtil.isParentNull(northComponent))
			{
				northComponent = new VaadinLabel("");
			}
			
			if (!VaadinUtil.isParentNull(southComponent))
			{
				southComponent = new VaadinLabel("");
			}
			
			if (!VaadinUtil.isParentNull(westComponent))
			{
				westComponent = new VaadinLabel("");
			}
			
			if (!VaadinUtil.isParentNull(eastComponent))
			{
				eastComponent = new VaadinLabel("");
			}
			
			if (bNorthComponentSet && bCenterComponentSet && !bSouthComponentSet && !bEastComponentSet && !bWestComponentSet)
			{
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(getVaadinComponent(centerComponent));
				resource.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				setParentForResource(resource);
			}										
			else if (bSouthComponentSet && bCenterComponentSet && !bNorthComponentSet  && !bEastComponentSet && !bWestComponentSet)
			{
				resource.addComponent(getVaadinComponent(centerComponent));
				resource.addComponent(verticalGapCenterSouth);
				resource.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				resource.addComponent(getVaadinComponent(southComponent));
	
				setParentForResource(resource);
			}					
			else if (bEastComponentSet && bCenterComponentSet && !bWestComponentSet && !bNorthComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				setParentForResource(centerLayout);
			}			
			else if (bWestComponentSet && bCenterComponentSet && !bEastComponentSet && !bNorthComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
	
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				setParentForResource(centerLayout);
			}									
			else if (bNorthComponentSet && bSouthComponentSet && bCenterComponentSet && !bEastComponentSet && !bWestComponentSet)
			{
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(getVaadinComponent(centerComponent));
				
				if (!(centerComponent instanceof VaadinLabel) 
						|| ((centerComponent instanceof VaadinLabel) 
							&& (((VaadinLabel)centerComponent).getText() != null && ((VaadinLabel)centerComponent).getText().length() > 0)))
				{
					resource.addComponent(verticalGapCenterSouth);
				}
	
				resource.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				resource.addComponent(getVaadinComponent(southComponent));
	
				setParentForResource(resource);
			}		
			else if (bNorthComponentSet && bEastComponentSet && bCenterComponentSet && !bWestComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
				
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}		
			else if (bNorthComponentSet && bWestComponentSet && bCenterComponentSet && !bEastComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
	
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
				
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}				
			else if (bSouthComponentSet && bEastComponentSet && bCenterComponentSet && !bWestComponentSet && !bNorthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}		
			else if (bSouthComponentSet && bWestComponentSet && bCenterComponentSet && !bEastComponentSet && !bNorthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
	
				centerLayout.addComponent(getVaadinComponent(centerComponent));	
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}			
			else if (bWestComponentSet && bEastComponentSet && bCenterComponentSet && !bNorthComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
				
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				
				if (!(centerComponent instanceof VaadinLabel) 
						|| ((centerComponent instanceof VaadinLabel) 
						    && (((VaadinLabel)centerComponent).getText() != null && ((VaadinLabel)centerComponent).getText().length() > 0)))
				{
					centerLayout.addComponent(horizontalGapCenterEast);
				}
	
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				setParentForResource(centerLayout);
			}			
			else if (bNorthComponentSet && bSouthComponentSet && bEastComponentSet && bCenterComponentSet && !bWestComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}						
			else if (bNorthComponentSet && bSouthComponentSet && bWestComponentSet && bCenterComponentSet && !bEastComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}			
			else if (bNorthComponentSet && bEastComponentSet && bWestComponentSet && bCenterComponentSet && !bSouthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
	
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
				
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}			
			else if (bSouthComponentSet && bEastComponentSet && bCenterComponentSet && bWestComponentSet && !bNorthComponentSet)
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
	
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			}				
			else if (bSouthComponentSet && bEastComponentSet && bCenterComponentSet && bWestComponentSet && bNorthComponentSet) // All is set
			{
				centerLayout.addComponent(getVaadinComponent(westComponent));
				centerLayout.addComponent(horizontalGapWestCenter);
				
				centerLayout.addComponent(getVaadinComponent(centerComponent));
				centerLayout.addComponent(horizontalGapCenterEast);
				centerLayout.setExpandRatio(getVaadinComponent(centerComponent), 1);
	
				centerLayout.addComponent(getVaadinComponent(eastComponent));
	
				resource.addComponent(getVaadinComponent(northComponent));
				resource.addComponent(verticalGapNorthCenter);
	
				resource.addComponent(centerLayout);
	
				resource.addComponent(verticalGapCenterSouth);
				resource.addComponent(getVaadinComponent(southComponent));
	
				resource.setExpandRatio(centerLayout, 1);
				
				setParentForResource(resource);
			} 
			else if (!bNorthComponentSet && !bSouthComponentSet && !bEastComponentSet && !bWestComponentSet && bCenterComponentSet) // only center component is set
			{
	        	setParentForResource(getVaadinComponent(centerComponent));
	    	}   			
		}
		
		performLayout();
		
		repaintMargins();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public void addComponent(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pConstraints == null)
		{
			pConstraints = IBorderLayout.CENTER;
			((VaadinComponentBase<?, ?>)pComponent).setConstraints(pConstraints);
		}	
		
		String constraint = (String)pConstraints;

		if (NORTH.equalsIgnoreCase(constraint)) 
		{
			northComponent = pComponent;
			
			bNorthComponentSet = true;	
		} 
		else if (WEST.equalsIgnoreCase(constraint)) 
		{			
			westComponent = pComponent;

			bWestComponentSet = true;
		} 
		else if (CENTER.equalsIgnoreCase(constraint)) 
		{
			centerComponent = pComponent;

			bCenterComponentSet = true;				
		} 
		else if (EAST.equalsIgnoreCase(constraint)) 
		{
			eastComponent = pComponent;

			bEastComponentSet = true;
				
		} 
		else if (SOUTH.equalsIgnoreCase(constraint)) 
		{
			southComponent = pComponent;

			bSouthComponentSet = true;
		}
		
		markComponentsChanged();
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void removeComponent(IComponent pComponent)
	{
	    if (!removeFromVaadin(pComponent, resource))
	    {
	        removeFromVaadin(pComponent, centerLayout);
	    }
	    
	    //re-set
		if (pComponent == northComponent) 
		{
			bNorthComponentSet = false;
			
			northComponent = new VaadinLabel("");
		} 
		else if (pComponent == westComponent) 
		{
			bWestComponentSet = false;
			
			westComponent = new VaadinLabel("");
		} 
		else if (pComponent == centerComponent) 
		{
			//bCenterComponentSet should always be true
			//bCenterComponentSet = false;		
			
			centerComponent = new VaadinLabel("");
		} 
		else if (pComponent == eastComponent) 
		{
			bEastComponentSet = false;		
			
			eastComponent = new VaadinLabel("");
		} 
		else if (pComponent == southComponent) 
		{
			bSouthComponentSet = false;		
			
			southComponent = new VaadinLabel("");
		}
		
		markComponentsChanged();
	}		

	/**
	 * {@inheritDoc}
	 */
	public String getConstraints(IComponent pComponent)
	{
		if (pComponent == northComponent) 
		{
			return UIBorderLayout.NORTH;
		} 
		else if (pComponent == westComponent) 
		{
			return UIBorderLayout.WEST;
		} 
		else if (pComponent == centerComponent) 
		{
			return UIBorderLayout.CENTER;	
		} 
		else if (pComponent == eastComponent) 
		{
			return UIBorderLayout.EAST;	
		} 
		else if (pComponent == southComponent) 
		{
			return UIBorderLayout.SOUTH;
		}
		
		return UIBorderLayout.CENTER;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComponent, String pConstraints)
	{
		if (pConstraints != null)
		{	
			removeComponent(pComponent); 
	
			addComponent(pComponent, pConstraints, -1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	
    public VaadinInsets getMargins()
    {
        return margins;
    }
    
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	margins = (VaadinInsets)pMargins;
    	
    	repaintMargins();
    }

	/**
	 * {@inheritDoc}
	 */
    public int getHorizontalGap()
    {
    	return horizontalGap;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHorizontalGap(int pHorizontalGap)
    {
    	horizontalGap = pHorizontalGap;

		horizontalGapWestCenter.setWidth(horizontalGap, Unit.PIXELS);

		horizontalGapCenterEast.setWidth(horizontalGap, Unit.PIXELS);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getVerticalGap()
    {
    	return verticalGap;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setVerticalGap(int pVerticalGap)
    {
    	verticalGap = pVerticalGap;

		verticalGapNorthCenter.setHeight(pVerticalGap, Unit.PIXELS);

		verticalGapCenterSouth.setHeight(pVerticalGap, Unit.PIXELS);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns only the center component if no other component is set.
	 * If more then only the center component is set the total layout is returned.
	 * 
	 * @return the center component or the total layout.
	 */
    @Override
	public Component getResource()
	{
		return resource;
	}		
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentContainer(IVaadinContainer pContainer)
    {
        super.setParentContainer(pContainer);

        if (pContainer instanceof IGroupPanel)
        {
            paddingTop = new CssExtensionAttribute("padding-top", null, "v-panel-content", CssExtensionAttribute.SEARCH_UP);
            paddingBottom = new CssExtensionAttribute("padding-bottom", null, "v-panel-content", CssExtensionAttribute.SEARCH_UP);
            paddingLeft = new CssExtensionAttribute("padding-left", null, "v-panel-content", CssExtensionAttribute.SEARCH_UP);
            paddingRight = new CssExtensionAttribute("padding-right", null, "v-panel-content", CssExtensionAttribute.SEARCH_UP);
        }
        else
        {
            paddingTop = new CssExtensionAttribute("padding-top", null, "v-csslayout", CssExtensionAttribute.SEARCH_UP);
            paddingBottom = new CssExtensionAttribute("padding-bottom", null, "v-csslayout", CssExtensionAttribute.SEARCH_UP);
            paddingLeft = new CssExtensionAttribute("padding-left", null, "v-csslayout", CssExtensionAttribute.SEARCH_UP);
            paddingRight = new CssExtensionAttribute("padding-right", null, "v-csslayout", CssExtensionAttribute.SEARCH_UP);
        }
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~     

	/**
	 * Returns the vaadin component for the {@link IComponent}.
	 * 
	 * @param pComponent the IComponent
	 * @return the vaadin component.
	 */
	private Component getVaadinComponent(IComponent pComponent)
	{
		if (pComponent == null)
		{
			pComponent = new VaadinLabel("");
		}
		
		return (Component)pComponent.getResource();
	}
	
	/**
	 * Sets the parent for the given component.
	 * 
	 * @param pResource the component.
	 */
	private void setParentForResource(Component pResource)
	{
		if (parent == null && resource.getParent() instanceof SingleComponentContainer)
		{
			parent = (SingleComponentContainer)resource.getParent();
		}
		
		if (parent != null)
		{
			Component parentFromResource = pResource.getParent();
			
			if (parentFromResource == null)
			{
				parent.setContent(pResource);
			}
		}
	}
	
	/**
	 * Forces the layout to apply layout logic to all its child components.
	 */
	private void performLayout()
	{		
		if (resource != null)
		{
			if (VaadinUtil.isParentHeightDefined(resource))
			{
				VaadinUtil.setComponentHeight(resource, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			VaadinUtil.setComponentWidth(resource, 100f, Unit.PERCENTAGE);
		}
		
		if (centerLayout != null)
		{
			if (VaadinUtil.isParentHeightDefined(centerLayout))
			{
				VaadinUtil.setComponentHeight(centerLayout, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentHeight(centerLayout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			if (VaadinUtil.isParentWidthDefined(centerLayout))
			{
				VaadinUtil.setComponentWidth(centerLayout, 100f, Unit.PERCENTAGE);
			}
			else
			{
				VaadinUtil.setComponentWidth(centerLayout, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}			
			
			Component component = getVaadinComponent(centerComponent);
			
			if (centerLayout.getComponentIndex(component) > 0)
			{
				centerLayout.setExpandRatio(component, 1);
			}
		}
		
		if (bNorthComponentSet)
		{
			setIgnorePerformLayout(northComponent, true);
			
			try
			{
				VaadinComponentBase<?, ?> northVaadinComp = (VaadinComponentBase<?, ?>)northComponent;
				if (northVaadinComp.isPreferredSizeSet()) // reinitialize width and height.
				{
					northVaadinComp.setSize(northVaadinComp.getPreferredSize());
				}
				else
				{
					northVaadinComp.setSizeUndefined();
				}
				
				Component component = getVaadinComponent(northComponent);
				
				applySize(northVaadinComp, "max", northVaadinComp.getMaximumSize(), 
						  northVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentWidthDefined(component), 
						  northVaadinComp.isMaximumSizeSet());
				applySize(northVaadinComp, "min", northVaadinComp.getMinimumSize(), 
						  northVaadinComp.isMinimumSizeSet(), 
						  northVaadinComp.isMinimumSizeSet());
	
				if (VaadinUtil.isParentWidthDefined(component))
				{
					northVaadinComp.setWidthFull();
				}
			}
			finally
			{
				setIgnorePerformLayout(northComponent, false);
			}
		}
		
		if (bSouthComponentSet)
		{
			setIgnorePerformLayout(southComponent, true);
			
			try
			{
				VaadinComponentBase<?, ?> southVaadinComp = (VaadinComponentBase<?, ?>)southComponent;
				if (southVaadinComp.isPreferredSizeSet()) // reinitialize width and height.
				{
					southVaadinComp.setSize(southVaadinComp.getPreferredSize());
				}
				else
				{
					southVaadinComp.setSizeUndefined();
				}
				
				Component component = getVaadinComponent(southComponent);
				
				applySize(southVaadinComp, "max", southVaadinComp.getMaximumSize(), 
						  southVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentWidthDefined(component), 
						  southVaadinComp.isMaximumSizeSet());
				applySize(southVaadinComp, "min", southVaadinComp.getMinimumSize(), 
						  southVaadinComp.isMinimumSizeSet(), 
						  southVaadinComp.isMinimumSizeSet());
	
				if (VaadinUtil.isParentWidthDefined(component))
				{
					southVaadinComp.setWidthFull();
				}
			}
			finally
			{
				setIgnorePerformLayout(northComponent, false);
			}
		}
		
		if (bWestComponentSet)
		{
			setIgnorePerformLayout(westComponent, true);
			
			try
			{
				VaadinComponentBase<?, ?> westVaadinComp = (VaadinComponentBase<?, ?>)westComponent;
				if (westVaadinComp.isPreferredSizeSet()) // reinitialize width and height.
				{
					westVaadinComp.setSize(westVaadinComp.getPreferredSize());
				}
				else
				{
			        westVaadinComp.setSizeUndefined();
				}
				
				Component component = getVaadinComponent(westComponent);
				
				applySize(westVaadinComp, "max", westVaadinComp.getMaximumSize(), 
						  westVaadinComp.isMaximumSizeSet(), 
						  westVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentHeightDefined(component));
				applySize(westVaadinComp, "min", westVaadinComp.getMinimumSize(), 
						  westVaadinComp.isMinimumSizeSet(), 
						  westVaadinComp.isMinimumSizeSet());
	
				if (VaadinUtil.isParentHeightDefined(component))
				{
					westVaadinComp.setHeightFull();
				}
			}
			finally
			{
				setIgnorePerformLayout(westComponent, false);
			}
		}
		
		if (bEastComponentSet)
		{
			setIgnorePerformLayout(eastComponent, true);
			
			try
			{
				VaadinComponentBase<?, ?> eastVaadinComp = (VaadinComponentBase<?, ?>)eastComponent;
				if (eastVaadinComp.isPreferredSizeSet()) // reinitialize width and height.
				{
					eastVaadinComp.setSize(eastVaadinComp.getPreferredSize());
				}
				else
				{
			        eastVaadinComp.setSizeUndefined();
				}
				
				Component component = getVaadinComponent(eastComponent);
				
				applySize(eastVaadinComp, "max", eastVaadinComp.getMaximumSize(), 
						  eastVaadinComp.isMaximumSizeSet(), 
						  eastVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentHeightDefined(component));
				applySize(eastVaadinComp, "min", eastVaadinComp.getMinimumSize(), 
						  eastVaadinComp.isMinimumSizeSet(), 
						  eastVaadinComp.isMinimumSizeSet());
	
				if (VaadinUtil.isParentHeightDefined(component))
				{
					eastVaadinComp.setHeightFull();
				}
			}
			finally
			{
				setIgnorePerformLayout(eastComponent, false);
			}
		}
		
		if (bCenterComponentSet)
		{
			setIgnorePerformLayout(centerComponent, true);
			
			try
			{
				VaadinComponentBase<?, ?> centerVaadinComp = (VaadinComponentBase<?, ?>)centerComponent;
				if (centerVaadinComp.isPreferredSizeSet()) // reinitialize width and height.
				{
					centerVaadinComp.setSize(centerComponent.getPreferredSize());
				}
				else
				{
					centerVaadinComp.setSizeUndefined();
				}
				
				Component component = getVaadinComponent(centerComponent);
				
				applySize(centerVaadinComp, "max", centerVaadinComp.getMaximumSize(), 
						  centerVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentWidthDefined(component), 
						  centerVaadinComp.isMaximumSizeSet() && !VaadinUtil.isParentHeightDefined(component));
				applySize(centerVaadinComp, "min", centerVaadinComp.getMinimumSize(), 
						  centerVaadinComp.isMinimumSizeSet(), 
						  centerVaadinComp.isMinimumSizeSet());
	
				if (VaadinUtil.isParentWidthDefined(component) && VaadinUtil.isParentHeightDefined(component))
				{
					centerVaadinComp.setSizeFull();
				}
				else if (VaadinUtil.isParentHeightDefined(component))
				{
					centerVaadinComp.setHeightFull();
				}
				else if (VaadinUtil.isParentWidthDefined(component))
				{
					centerVaadinComp.setWidthFull();
				}
			}
			finally
			{
				setIgnorePerformLayout(centerComponent, false);
			}
		}				
	}
	
	/**
	 * Sets and removes the margins from the components and layouts.
	 */
	private void repaintMargins()
	{
        CssExtension centerComponentExtension = ((VaadinComponentBase<?, ?>)centerComponent).getCssExtension();
        
        centerComponentExtension.removeAttribute(paddingTop);
        centerComponentExtension.removeAttribute(paddingBottom);
        centerComponentExtension.removeAttribute(paddingLeft);
        centerComponentExtension.removeAttribute(paddingRight);  

        if (parent != null && parent.getContent() != null)
        {
            Component actualResource = parent.getContent();

            CssExtension cssExtensionBorderLayout = VaadinUtil.getCssExtension((AbstractComponent) actualResource);
            
            cssExtensionBorderLayout.removeAttribute("padding-top");
            cssExtensionBorderLayout.removeAttribute("padding-left");
            cssExtensionBorderLayout.removeAttribute("padding-right");
            cssExtensionBorderLayout.removeAttribute("padding-bottom");  
            
            if (actualResource instanceof AbstractLayout)
            {
                if (margins != null)
                {
                    paddingTop.setValue(margins.getTop() + "px");
                    cssExtensionBorderLayout.addAttribute(paddingTop);
                    
                    paddingBottom.setValue(margins.getBottom() + "px");
                    cssExtensionBorderLayout.addAttribute(paddingBottom);
                    
                    paddingLeft.setValue(margins.getLeft() + "px");
                    cssExtensionBorderLayout.addAttribute(paddingLeft);
                    
                    paddingRight.setValue(margins.getRight() + "px");
                    cssExtensionBorderLayout.addAttribute(paddingRight);
                }               
            }
            else
            {
                if (margins != null)
                {
                    paddingTop.setValue(margins.getTop() + "px");
                    centerComponentExtension.addAttribute(paddingTop);
                    
                    paddingBottom.setValue(margins.getBottom() + "px");
                    centerComponentExtension.addAttribute(paddingBottom);
                    
                    paddingLeft.setValue(margins.getLeft() + "px");
                    centerComponentExtension.addAttribute(paddingLeft);
                    
                    paddingRight.setValue(margins.getRight() + "px");
                    centerComponentExtension.addAttribute(paddingRight);
                }               
            }
        }
	}
	
	/**
	 * Gets the layout, used for west, center, east components.
	 * 
	 * @return the layout
	 */
	public HorizontalLayout getCenterLayout()
	{
	    return centerLayout;
	}
	
}	// VaadinBorderLayout
