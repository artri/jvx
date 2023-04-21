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
 * 17.11.2008 - [HM] - creation
 * 06.03.2010 - [JR] - get/setConstraints: used ComponentUIResource instead of UIResource [BUGFIX]
 */
package javax.rad.genui;

import java.util.HashMap;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;
import javax.rad.ui.IResource;

/**
 * Platform and technology independent layout.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 *  
 * @param <C> instance of ILayout
 * @param <CO> type of the constraints.
 * 
 * @author Martin Handsteiner
 */
public abstract class UILayout<C extends ILayout<CO>, CO> extends UIResource<C> 
                                         implements ILayout<CO>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The constraints for all components used by this layout. */
	protected transient HashMap<IComponent, CO> componentConstraints = new HashMap<IComponent, CO>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UILayout</code>.
     *
     * @param pLayout the ILayout.
     * @see ILayout
     */
	protected UILayout(C pLayout)
	{
		super(pLayout);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public CO getConstraints(IComponent pComp)
	{
		CO constraints = componentConstraints.get(pComp);
		
		if (constraints == null)
		{
			constraints = getConstraintsIntern(pComp);

			if (constraints != null)
			{
				componentConstraints.put(pComp, constraints);
			}
		}
		
		return constraints;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConstraints(IComponent pComp, CO pConstraints)
	{
		CO constraints = componentConstraints.get(pComp);
		
		if (constraints == null)
		{
			constraints = getConstraintsIntern(pComp);

			if (constraints != null)
			{
				if (pConstraints instanceof IResource)
				{
					componentConstraints.put(pComp, pConstraints);
				}
				else
				{
					componentConstraints.put(pComp, constraints);
				}
			}
		}
		else
		{
			if (pConstraints == null)
			{
				componentConstraints.remove(pComp);
			}
			else
			{
				setConstraintsIntern(pComp, pConstraints);
	
				componentConstraints.put(pComp, pConstraints);
			}
		}
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
	 * Sets the insets with primitive types.
	 * 
	 * @param pTop the top insets.
	 * @param pLeft the left insets.
	 * @param pBottom the bottom insets.
	 * @param pRight the right insets.
	 */
    public void setMargins(int pTop, int pLeft, int pBottom, int pRight)
    {
    	uiResource.setMargins(UIFactoryManager.getFactory().createInsets(pTop, pLeft, pBottom, pRight));
    }

    /**
	 * {@inheritDoc}
	 */
    public int getHorizontalGap()
    {
    	return uiResource.getHorizontalGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHorizontalGap(int pHorizontalGap)
    {
    	uiResource.setHorizontalGap(pHorizontalGap);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getVerticalGap()
    {
    	return uiResource.getVerticalGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setVerticalGap(int pVerticalGap)
    {
    	uiResource.setVerticalGap(pVerticalGap);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * The container of this layout is added to ui.
     * @param pContainer the container
     */
    public void addNotify(UIContainer pContainer)
    {
    }
    
    /**
     * The container of this layout is removed from ui.
     * @param pContainer the container
     */
    public void removeNotify(UIContainer pContainer)
    {
    }

    /**
     * This informs the layout, that the visiblilty of an added component is changed.
     * @param pContainer the container
     * @param pComponent the component
     */
    public void componentChanged(UIContainer pContainer, UIComponent pComponent)
    {
    }
    
	/**
	 * Gets the constraints from the correct ui resource.
	 * @param pComp the component
	 * @return the constraint
	 */
	protected CO getConstraintsIntern(IComponent pComp)
	{
		if (pComp instanceof UIComponent)
		{
			return uiResource.getConstraints(((UIComponent)pComp).getComponentUIResource());
		}
		else
		{
			return uiResource.getConstraints(pComp);
		}
	}

	/**
	 * Sets the constraints from the correct ui resource.
	 * @param pComp the component
	 * @param pConstraints the constraint
	 */
	protected void setConstraintsIntern(IComponent pComp, CO pConstraints)
	{
		CO constraints = pConstraints;
		while (constraints instanceof UIResource<?>)
		{
			constraints = (CO)((UIResource<?>)constraints).getUIResource();
		}

		if (pComp instanceof UIComponent)
		{
			uiResource.setConstraints(((UIComponent)pComp).getComponentUIResource(), constraints);
		}
		else
		{
			uiResource.setConstraints(pComp, constraints);
		}
	}

	/**
	 * Give internal add access for the possibility to have additional "not real" added components with automatic translation.
	 * @param pContainer the container where to add internally.
	 * @param pComponent the component to add.
	 */
	protected void addInternalComponent(UIContainer pContainer, UIComponent pComponent)
	{
		pContainer.addInternal(null, -1, pComponent, null, -1);
	}

	/**
	 * Give internal remove access for the possibility to have additional "not real" added components with automatic translation.
	 * @param pContainer the container where to remove internally.
	 * @param pComponent the component to remove.
	 */
	protected void removeInternalComponent(UIContainer pContainer, UIComponent pComponent)
	{
		pContainer.removeInternal(pComponent);
	}

}	// UILayout
