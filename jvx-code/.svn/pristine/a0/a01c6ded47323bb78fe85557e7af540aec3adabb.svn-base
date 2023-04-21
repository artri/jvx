/*
 * Copyright 2018 SIB Visions GmbH
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
 * 21.03.2018 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.IInsets;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.component.ILabeledIcon;

import com.sibvisions.rad.ui.web.impl.WebInsets;

/**
 * Web server implementation of {@link ILabeledIcon}.
 * 
 * @author René Jahn
 */
public abstract class AbstractWebMarginActionComponent extends AbstractWebActionComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebMarginActionComponent</code>.
     *
     * @see IActionComponent
     */
	protected AbstractWebMarginActionComponent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets space for margin between the border and
     * the content. Setting to <code>null</code> will cause the component to
     * use the default margin.
     *
     * @param pMargins the space between the border and content
     */
	public void setMargins(IInsets pMargins)
	{
		setProperty("margins", pMargins);
	}

    /**
     * Returns the margin between the component's border and the content.
     * 
     * @return an {@link WebInsets} object specifying the margin
     *		   between the component's border and the content
     * @see #setMargins(IInsets)
     */
	public WebInsets getMargins()
	{
    	return getProperty("margins", new WebInsets());
	}

}	// AbstractWebMarginActionComponent
