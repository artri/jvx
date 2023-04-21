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

import javax.rad.ui.celleditor.IComboCellEditor;

/**
 * The {@link AbstractComboCellEditor} is an {@link IComboCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractComboCellEditor extends AbstractInplaceCellEditor 
                                                      implements IComboCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** if the popup should open automatically. */
	private transient boolean bAutoOpenPopup = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractComboCellEditor}.
	 */
	protected AbstractComboCellEditor()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link AbstractComboCellEditor}.
	 *
	 * @param pInitialAutoOpenPopup the initial auto open popup value.
	 */
	protected AbstractComboCellEditor(boolean pInitialAutoOpenPopup)
	{
		super();
		
		bAutoOpenPopup = pInitialAutoOpenPopup;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoOpenPopup()
	{
		return bAutoOpenPopup;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDirectCellEditor()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoOpenPopup(boolean pAutoOpenPopup)
	{
		bAutoOpenPopup = pAutoOpenPopup;
	}
	
}	// AbstractComboCellEditor
