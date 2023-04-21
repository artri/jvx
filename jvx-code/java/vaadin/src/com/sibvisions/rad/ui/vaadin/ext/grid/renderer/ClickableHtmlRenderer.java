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
package com.sibvisions.rad.ui.vaadin.ext.grid.renderer;

import com.vaadin.ui.renderers.ClickableRenderer;

/**
 * The {@link ClickableHtmlRenderer} is a {@link ClickableRenderer} extension
 * which allows to display a HTML string which can be clicked.
 * 
 * @author Robert Zenz
 */
public class ClickableHtmlRenderer extends ClickableRenderer<Integer, String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ClickableHtmlRenderer}.
	 */
	public ClickableHtmlRenderer()
	{
		super(String.class);
	}
	
	/**
	 * Creates a new instance of {@link ClickableHtmlRenderer}.
	 *
	 * @param pListener the
	 *            {@link com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener
	 *            listener}.
	 */
	public ClickableHtmlRenderer(RendererClickListener pListener)
	{
		this();
		
		addClickListener(pListener);
	}
	
}	// ClickableHtmlRenderer
