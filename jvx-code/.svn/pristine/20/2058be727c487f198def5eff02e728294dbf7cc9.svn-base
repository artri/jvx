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
package com.sibvisions.rad.ui.vaadin.impl;

import javax.rad.ui.IComponent;

import com.sibvisions.rad.ui.vaadin.impl.feature.FeatureUtil;
import com.sibvisions.rad.ui.vaadin.impl.feature.IAutoCompleteFeature;
import com.sibvisions.rad.ui.vaadin.impl.feature.IFeature;
import com.vaadin.ui.Component;

/**
 * The <code>VaadinFeaturedComponent</code> is a simple {@link VaadinComponent} with implementations
 * for {@link IFeature}s.
 * 
 * @author René Jahn 
 *
 * @param <C> the component
 */
public class VaadinFeaturedComponent<C extends Component> extends VaadinComponent<C> 
                                                          implements IAutoCompleteFeature 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** whether the autocomplete feature is enabled. */
	private Boolean bAutoComplete; 

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinFeaturedComponent</code>.
	 * 
	 * @param pComponent an instance of {@link Component}.
	 * @see IComponent
	 */
	protected VaadinFeaturedComponent(C pComponent)
	{
		super(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoComplete(boolean pAutoComplete)
	{
		bAutoComplete = Boolean.valueOf(pAutoComplete);

		updateFeatures();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoComplete()
	{
		return bAutoComplete == null || bAutoComplete.booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoCompleteSet()
	{
		return bAutoComplete != null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined features
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Updates/Applies features.
	 */
	protected void updateFeatures()
	{
		FeatureUtil.apply(this, this);
	}

}	// VaadinFeaturedComponent
