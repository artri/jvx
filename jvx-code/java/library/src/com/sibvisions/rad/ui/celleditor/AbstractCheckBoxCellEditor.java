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

import javax.rad.ui.celleditor.ICheckBoxCellEditor;

/**
 * The {@link AbstractCheckBoxCellEditor} is an {@link ICheckBoxCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 * @param <C> the type of the content.
 */
public abstract class AbstractCheckBoxCellEditor<C> extends AbstractStyledCellEditor 
                                                    implements ICheckBoxCellEditor<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The deselected value. */
    protected transient Object deselectedValue;
	
	/** The selected value. */
    protected transient Object selectedValue;
	
	/** The text. */
    protected transient String text;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractCheckBoxCellEditor}.
	 */
	protected AbstractCheckBoxCellEditor()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link AbstractCheckBoxCellEditor}.
	 *
	 * @param pSelectedValue the {@link Object selected value}.
	 * @param pDeselectedValue the {@link Object deselected value}.
	 */
	protected AbstractCheckBoxCellEditor(Object pSelectedValue, Object pDeselectedValue)
	{
		super();
		
		setSelectedValue(pSelectedValue);
		setDeselectedValue(pDeselectedValue);
	}
	
	/**
	 * Creates a new instance of {@link AbstractCheckBoxCellEditor}.
	 *
	 * @param pSelectedValue the {@link Object selected value}.
	 * @param pDeselectedValue the {@link Object deselected value}.
	 * @param pText the {@link String text}.
	 */
	protected AbstractCheckBoxCellEditor(Object pSelectedValue, Object pDeselectedValue, String pText)
	{
		super();
		
		selectedValue = pSelectedValue;
		deselectedValue = pDeselectedValue;
		text = pText;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public Object getDeselectedValue()
	{
		return deselectedValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDeselectedValue(Object pDeselectedValue)
	{
		deselectedValue = pDeselectedValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getSelectedValue()
	{
		return selectedValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelectedValue(Object pSelectedValue)
	{
		selectedValue = pSelectedValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
	{
		text = pText;
	}
	
}	// AbstractCheckBoxCellEditor
