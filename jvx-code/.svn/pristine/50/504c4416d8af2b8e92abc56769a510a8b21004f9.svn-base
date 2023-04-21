/*
 * Copyright 2022 SIB Visions GmbH
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
 * 14.12.2022 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import com.vaadin.ui.AbstractComponent;

/**
 * The <code>AbstractVaadinCaptionComponent</code> extends the {@link AbstractVaadinActionComponent} and handles 
 * html captions.
 * 
 * @author René Jahn
 * @param <C> an instance of {@link AbstractComponent}
 */
public abstract class AbstractVaadinCaptionComponent<C extends AbstractComponent> extends AbstractVaadinActionComponent<C>
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractVaadinCaptionComponent</code>.
	 * 
	 * @param pComponent an instance of {@link com.vaadin.ui.AbstractComponent}
	 */
	public AbstractVaadinCaptionComponent(C pComponent)
	{
		super(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		super.setText(pText);
		
		resource.setCaptionAsHtml(isHtml(pText));
	}
	
}	// AbstractVaadinCaptionComponent
