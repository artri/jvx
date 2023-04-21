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
 * 01.02.2017 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link AccessibilityUtil} provides util methods for setting WAI-ARIA
 * attributes.
 * 
 * @author Robert Zenz
 */
public final class AccessibilityUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the WAI-ARIA "label" attribute. */
	public static final String ATTRIBUTE_LABEL = "aria-label";
	
	/** the pressed state. */
	public static final String ATTRIBUTE_PRESSED = "aria-pressed";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance required.
	 */
	private AccessibilityUtil()
	{
		// No instance required.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the label into the given {@link AttributesExtension}.
	 * 
	 * @param pExtension the {@link AttributesExtension} to use.
	 * @param pPossibleLabels the possible labels, the first one which is
	 *            neither {@code null} or {@link StringUtil#isEmpty(String)
	 *            empty} will be used. If none are fitting or none are provided,
	 *            the attribute will be removed.
	 */
	public static final void setLabel(AttributesExtension pExtension, String... pPossibleLabels)
	{
		if (pPossibleLabels != null && pPossibleLabels.length > 0)
		{
			for (String possibleLabel : pPossibleLabels)
			{
				if (!StringUtil.isEmpty(possibleLabel))
				{
					pExtension.setAttribute(ATTRIBUTE_LABEL, possibleLabel);
					return;
				}
			}
		}
		
		pExtension.removeAttribute(ATTRIBUTE_LABEL);
	}
	
	/**
	 * Sets the pressed state into the given {@link AttributesExtension}.
	 * 
	 * @param pExtension the {@link AttributesExtension} to use.
	 * @param pPressed {@code true} to set the state pressed, {@code false} otherwise.
	 */
	public static final void setPressed(AttributesExtension pExtension, boolean pPressed)
	{
		pExtension.setAttribute(ATTRIBUTE_PRESSED, pPressed ? "true" : "false");
	}
	
}	// AccessibilityUtil
