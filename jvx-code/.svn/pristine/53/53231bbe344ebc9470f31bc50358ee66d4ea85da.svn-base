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

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFactory;

import com.sibvisions.rad.ui.vaadin.impl.IVaadinContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinContainerBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinResourceBase;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * The <code>AbstractVaadinLayout</code> class is the base for all layouts.
 * It contains all generic functions.
 * 
 * @author Martin Handsteiner
 * 
 * @param <R> the type of the layout constraints.
 * @param <CO> the type of the layout constraints.
 */
public abstract class AbstractVaadinLayout<R, CO> extends VaadinResourceBase<R> 
                                                  implements IVaadinLayout<CO>, Runnable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the container with this layout. */
    private IVaadinContainer parent;

    /** this layout is dirty. */
    private boolean dirty = false;
    
    /** whether components have changed. */
    private boolean componentsChanged = false;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Creates a new instance of <code>VaadinAbsoluteLayout</code>.
	 * 
	 * @param pResource the layout resource.
	 */
	protected AbstractVaadinLayout(R pResource) 
	{
		super(pResource);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		

	/**
	 * {@inheritDoc}
	 */
	public final void markDirty()
	{
		if (!dirty)
		{
			IFactory factory = UIFactoryManager.getFactory();
			
			if (factory != null)
			{
				dirty = true;
				
				factory.invokeLater(this);
			}
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public final void markComponentsChanged()
	{
		if (!componentsChanged)
		{
			componentsChanged = true;

			if (!dirty)
			{
				markDirty();
			}
			else
			{
				IFactory factory = UIFactoryManager.getFactory();
				
				if (factory != null)
				{
					factory.invokeLater(new Runnable() 
					{
						@Override
						public void run() 
						{
							//only do this, if not already executed in run()
							if (componentsChanged)
							{
								try
								{
									repaintLayout(true);
								}
								finally
								{
									componentsChanged = false;
								}
							}
						}
					});
				}
			}
		}
	}

    /**
     * {@inheritDoc}
     */
	public final void run()
	{
		boolean reset = componentsChanged;
			
	    try
	    {
	        repaintLayout(componentsChanged);
	    }
	    finally
	    {
	        dirty = false;
	    
	        //reset only if property was recognized from "repaintLayout"
	        if (reset)
	        {
	        	componentsChanged = false;
	        }
	    }
	}	

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * Repaints the layout.
	 * 
	 * @param pComponentsChanged whether the hierarchy has changed
	 */
	public abstract void repaintLayout(boolean pComponentsChanged);

	/**
	 * Performs {@link IVaadinContainer#performLayout()} if given component is a container.
	 * 
	 * @param pComponent the component to check
	 */
	protected void performLayout(IComponent pComponent)
	{
		if (pComponent instanceof IVaadinContainer)
		{
			((IVaadinContainer) pComponent).performLayout();
		}
	}
	
	/**
	 * Sets the css style for max/ min width and height.
	 * @param pComponent the component
	 * @param pStylePrefix the style prefix (max/ min)
	 * @param pSize the size to set
	 * @param pSetWidth true, if width should be ignored
	 * @param pSetHeight true, if height should be ignored
	 */
	public void applySize(VaadinComponentBase pComponent, String pStylePrefix, IDimension pSize, boolean pSetWidth, boolean pSetHeight)
	{
		String styleWidth = pStylePrefix + "-width";
		if (pSetWidth && pSize.getWidth() >= 0 && pSize.getWidth() < Integer.MAX_VALUE)
		{
			pComponent.getCssExtension().addAttribute(styleWidth, pSize.getWidth() + "px");
		}
		else
		{
			pComponent.getCssExtension().removeAttribute(styleWidth);
		}
		String styleHeight = pStylePrefix + "-height";
		if (pSetHeight && pSize.getHeight() >= 0 && pSize.getHeight() < Integer.MAX_VALUE)
		{
			pComponent.getCssExtension().addAttribute(styleHeight, pSize.getHeight() + "px");
		}
		else
		{
			pComponent.getCssExtension().removeAttribute(styleHeight);
		}
	}
	
	/**
	 * Removes a component from it's vaadin parent component.
	 * 
	 * @param pComponent the component
	 * @param pContainer the expected container
	 * @return <code>true</code> if component was removed from the expected parent, <code>false</code> otherwise
	 */
	protected boolean removeFromVaadin(IComponent pComponent, AbstractComponentContainer pContainer)
	{
        Component comp = (Component)pComponent.getResource();
        
        HasComponents compParent = comp.getParent();

        if (compParent == pContainer)
        {
            pContainer.removeComponent(comp);
            
            return true;
        }

        return false;
	}
	
	/**
	 * Sets the parent container.
	 * 
	 * @param pContainer the container
	 */
	public void setParentContainer(IVaadinContainer pContainer)
	{
	    parent = pContainer;
	}
	
	/**
	 * Gets the parent container.
	 * 
	 * @return the container
	 */
	public IVaadinContainer getParentContainer()
	{
	    return parent;
	}
	
	/**
	 * Forwards to {@link VaadinContainerBase#setIgnorePerformLayout(boolean)}.
	 * 
	 * @param pComponent the component
	 * @param pIgnore <code>true</code> to ignore, <code>false</code> otherwise
	 */
	protected void setIgnorePerformLayout(IComponent pComponent, boolean pIgnore)
	{
		if (pComponent instanceof VaadinContainerBase)
		{
			((VaadinContainerBase)pComponent).setIgnorePerformLayout(pIgnore);
		}
	}

} 	// AbstractVaadinLayout
