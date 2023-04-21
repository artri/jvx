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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import javax.rad.ui.celleditor.IChoiceCellEditor;

/**
 * The {@link AbstractChoiceCellEditor} is an {@link IChoiceCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 * @param <C> the type of the content.
 */
public abstract class AbstractChoiceCellEditor<C> extends AbstractStyledCellEditor 
                                                  implements IChoiceCellEditor<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The allowed values. */
	protected transient Object[] allowedValues;
	
	/** The name of the default image. */
	protected transient String defaultImageName;
	
	/** The name of the images to represent the values. */
	protected transient String[] imageNames;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractChoiceCellEditor}.
	 */
	protected AbstractChoiceCellEditor()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link AbstractChoiceCellEditor}.
	 *
	 * @param pAllowedValues the {@link Object allowed values}.
	 * @param pImageNames the {@link String image names}.
	 */
	protected AbstractChoiceCellEditor(Object[] pAllowedValues, String[] pImageNames)
	{
		super();
		
		setAllowedValues(pAllowedValues);
		setImageNames(pImageNames);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public Object[] getAllowedValues()
	{
		return allowedValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDefaultImageName()
	{
		return defaultImageName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getImageNames()
	{
		return imageNames;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAllowedValues(Object[] pAllowedValues)
	{
		allowedValues = pAllowedValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDefaultImageName(String pDefaultImageName)
	{
		defaultImageName = pDefaultImageName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setImageNames(String[] pImageNames)
	{
		imageNames = pImageNames;
	}
	
}	// AbstractChoiceCellEditor
