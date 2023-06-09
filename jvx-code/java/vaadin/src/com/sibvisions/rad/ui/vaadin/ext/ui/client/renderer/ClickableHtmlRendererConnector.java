/*
 * Copyright 2015 SIB Visions GmbH
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
 * 11.09.2015 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.connectors.ClickableRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer.RendererClickHandler;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

/**
 * The {@link ClickableHtmlRendererConnector} is a
 * {@link ClickableRendererConnector} extension which is used by the
 * {@link com.sibvisions.rad.ui.vaadin.ext.grid.renderer.ClickableHtmlRenderer}.
 * 
 * @author Robert Zenz
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.grid.renderer.ClickableHtmlRenderer.class)
public class ClickableHtmlRendererConnector extends ClickableRendererConnector<String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ClickableHtmlRendererConnector}.
	 */
	public ClickableHtmlRendererConnector()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VClickableHtmlRenderer getRenderer()
	{
		return (VClickableHtmlRenderer)super.getRenderer();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VClickableHtmlRenderer createRenderer()
	{
		VClickableHtmlRenderer renderer = GWT.create(VClickableHtmlRenderer.class);
		
		renderer.setApplicationConnection(getConnection());
		
		return renderer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HandlerRegistration addClickHandler(RendererClickHandler<JsonObject> pHandler)
	{
		return getRenderer().addClickHandler(pHandler);
	}
	
}	// ClickableHtmlRendererConnector
