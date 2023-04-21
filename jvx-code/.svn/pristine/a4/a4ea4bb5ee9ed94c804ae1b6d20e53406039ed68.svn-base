/*
 * Copyright 2020 SIB Visions GmbH
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
 * 04.02.2020 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.feature;

import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicAttributes;

/**
 * The {@link FeatureUtil} contains {@link IFeature} dependent utility methods.
 * 
 * @author René Jahn
 */
public final class FeatureUtil 
{
	/**
	 * Invisible constructor because <code>FeatureUtil</code> is a utility class.
	 */
	private FeatureUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Applies the given feature.
	 * 
	 * @param pAttributes the dynamic attributes handler
	 * @param pFeature the feature to apply
	 */
	public static void apply(IDynamicAttributes pAttributes, IFeature... pFeature)
	{
		if (pFeature != null)
		{
			boolean bAutoComplete = false;
			
			for (int i = 0; i < pFeature.length; i++)
			{
				if (!bAutoComplete 
					&& pFeature[i] instanceof IAutoCompleteFeature)
				{
					AttributesExtension ext = pAttributes.getAttributesExtension();

					if (((IAutoCompleteFeature)pFeature[i]).isAutoCompleteSet())
					{
						if (!((IAutoCompleteFeature)pFeature[i]).isAutoComplete())
						{
							ext.setAttribute("autocomplete", "off");
						}
						else
						{
							ext.removeAttribute("autocomplete");
						}
						
						bAutoComplete = true;
					}
				}
			}
		}
	}
}
