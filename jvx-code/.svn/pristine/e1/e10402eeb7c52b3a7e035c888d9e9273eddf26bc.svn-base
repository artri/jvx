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
 * 02.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.IWindow;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.container.AbstractVaadinWindow;
import com.sibvisions.rad.ui.vaadin.impl.layout.IVaadinLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinAbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.Window;

/**
 * The <code>VaadinSingleContainer</code> class is the vaadin implementation of
 * {@link javax.rad.ui.IContainer} for SingleComponentContainer.
 * 
 * @author Stefan Wurm
 * 
 * @param <CR> an instance of {@link Component}
 * @param <C> an instance of {@link SingleComponentContainer}
 */
public abstract class VaadinSingleContainer<CR extends Component, C extends SingleComponentContainer> extends VaadinContainerBase<CR, C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinSingleContainer</code>.
	 *
	 * @param pContainer a SingleComponentContainer
	 */
	protected VaadinSingleContainer(C pContainer)
	{
		super(pContainer);
		
		setDefaultLayout(createDefaultLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent instanceof IWindow)
		{
			Window window = (Window)((Component)pComponent.getResource());
			
			if (!VaadinUtil.isWindowAlreadyAttached(window))
			{
				getFactory().getUI().addWindow(window, (AbstractVaadinWindow<?>)pComponent);
			}
		}
		else
		{
			((VaadinComponentBase<?, ?>)pComponent).setConstraints(pConstraints);
			
			IVaadinLayout<?> layout = (IVaadinLayout<?>)getLayout();
			
			layout.addComponent(pComponent, pConstraints, pIndex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeFromVaadin(IComponent pComponent)
	{
		if (pComponent instanceof IWindow)
		{
			Window window = (Window)((Component)pComponent.getResource());
			
			getFactory().getUI().removeWindow(window, (AbstractVaadinWindow<?>)pComponent);
		}
		else
		{
			IVaadinLayout<?> layout = (IVaadinLayout<?>)getLayout();
			
			layout.removeComponent(pComponent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayout(ILayout pLayout)
	{
		super.setLayout(pLayout);
		
		resource.setContent((Component)getLayout().getResource());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the default Layout.
	 * 
	 * @return the default Layout
	 */
	private ILayout<?> createDefaultLayout()
	{
		VaadinAbsoluteLayout absoluteLayout = new VaadinAbsoluteLayout();
		absoluteLayout.setMargins(new VaadinInsets(0, 0, 0, 0));
		
		return absoluteLayout;
	}
	
}	// VaadinSingleContainer
