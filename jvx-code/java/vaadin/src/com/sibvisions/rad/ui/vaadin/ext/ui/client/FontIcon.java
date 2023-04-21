/*
 * Copyright 2017 SIB Visions GmbH
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
 * 14.11.2017 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

/**
 * The {@link FontIcon} is an {@link com.vaadin.client.ui.FontIcon} to retain
 * compatibility with already existing applications.
 * 
 * @author Robert Zenz
 */
public class FontIcon extends com.vaadin.client.ui.FontIcon
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FontIcon}.
	 */
	public FontIcon()
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
	protected void setFontFamily(String pFontFamily)
	{
		// To keep compatibility with already existing stylesheets, we must
		// replace the "FontAwesomeLabelAddon" font family with the "old" one,
		// "FontAwesome".
		if ("FontAwesomeLabelAddon".equals(pFontFamily))
		{
			super.setFontFamily("FontAwesome");
		}
		else
		{
			super.setFontFamily(pFontFamily);
		}
	}
	
}	// FontIcon
