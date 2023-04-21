/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.04.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.component.ILabel;

/**
 * Web server implementation of a {@link ILabel}.
 * 
 * @author Ren� Jahn
 */
public abstract class AbstractWebLabelComponent extends AbstractWebAlignmentComponent
                                                implements ILabel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebLabelComponent</code>.
     *
     * @see IAlignmentConstants
     */
	public AbstractWebLabelComponent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getText()
    {
    	return getProperty("text", "");
    }

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
    {
		setProperty("text", pText);
    }

}	// AbstractWebLabelComponent
