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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

/**
 * The {@link VClickableHtmlRenderer} is a {@link ClickableRenderer} extension
 * which allows to display a HTML string which can also be clicked.
 * 
 * @author Robert Zenz
 */
public class VClickableHtmlRenderer extends ClickableRenderer<String, SimplePanel>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ApplicationConnection} used to resolve URIs. */
	private ApplicationConnection applicationConnection = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VClickableHtmlRenderer}.
	 */
	public VClickableHtmlRenderer()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimplePanel createWidget()
	{
		SimplePanel panel = new SimplePanel();
		panel.addDomHandler(this, ClickEvent.getType());
		panel.setStyleName("v-clickablehtmlrenderer");
		
		return panel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(RendererCellReference pCell, String pData, SimplePanel pWidget)
	{
		StringBuilder data = new StringBuilder();
		
		if (pData != null)
		{
			// We cannot use Pattern on the client...so let's do it the hard way.
			
			int index = pData.indexOf("\"app://");
			int lastIndex = 0;
			
			while (index >= 0)
			{
				// Append everything until oour find.
				data.append(pData.substring(lastIndex, index));
				
				// Now find the next quote, that will be the end of the URI.
				int nextQuoteIndex = pData.indexOf('"', index + 1);
				
				// Append the quote which we matched.
				data.append("\"");
				
				if (nextQuoteIndex >= 0)
				{
					// Resolve the URI.
					String uri = pData.substring(index + 1, nextQuoteIndex);
					uri = applicationConnection.translateVaadinUri(uri);
					
					data.append(uri);
					data.append("\"");
					
					lastIndex = nextQuoteIndex + 1;
				}
				else
				{
					// Smells malformed, let's continue.
					lastIndex = index + 1;
				}
				
				index = pData.indexOf("\"app://", lastIndex);
			}
			
			// Append the remaining parts.
			data.append(pData.substring(lastIndex));
		}
		
		pWidget.getElement().setInnerSafeHtml(SafeHtmlUtils.fromSafeConstant(data.toString()));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link ApplicationConnection application connection} which is
	 * used to resolve URIs.
	 *
	 * @return the {@link ApplicationConnection application connection} which is
	 *         used to resolve URIs.
	 */
	public ApplicationConnection getApplicationConnection()
	{
		return applicationConnection;
	}
	
	/**
	 * Sets the {@link ApplicationConnection application connection} which is
	 * used to resolve URIs.
	 *
	 * @param pApplicationConnection the new {@link ApplicationConnection
	 *            application connection} which is used to resolve URIs.
	 */
	public void setApplicationConnection(ApplicationConnection pApplicationConnection)
	{
		applicationConnection = pApplicationConnection;
	}
	
}	// VClickableHtmlRenderer
