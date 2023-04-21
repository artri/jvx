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

import javax.rad.ui.celleditor.IInplaceCellEditor;

/**
 * The {@link AbstractInplaceCellEditor} is an {@link IInplaceCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractInplaceCellEditor extends AbstractStyledCellEditor 
                                                implements IInplaceCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The preferred editor mode.
	 * 
	 * @see IInplaceCellEditor#DOUBLE_CLICK
	 * @see IInplaceCellEditor#SINGLE_CLICK
	 */
	private transient int preferredEditorMode = DOUBLE_CLICK;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractInplaceCellEditor}.
	 */
	protected AbstractInplaceCellEditor()
	{
		super();
		
		setVerticalAlignment(ALIGN_TOP);
	}
	
	/**
	 * Creates a new instance of {@link AbstractInplaceCellEditor}.
	 *
	 * @param pInitialPreferredEditorMode the initial preferred editor mode.
	 */
	protected AbstractInplaceCellEditor(int pInitialPreferredEditorMode)
	{
		this();
		
		setPreferredEditorMode(pInitialPreferredEditorMode);
	}
	
	/**
	 * Creates a new instance of {@link AbstractInplaceCellEditor}.
	 *
	 * @param pPreferredEditorMode the preferred editor mode.
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 */
	public AbstractInplaceCellEditor(int pPreferredEditorMode, int pHorizontalAlignment, int pVerticalAlignment)
	{
		this();
		
		setHorizontalAlignment(pHorizontalAlignment);
        setVerticalAlignment(pVerticalAlignment);
		setPreferredEditorMode(pPreferredEditorMode);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * {@inheritDoc}
	 */
	public int getPreferredEditorMode()
	{
		return preferredEditorMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPreferredEditorMode(int pPreferredEditorMode)
	{
		preferredEditorMode = pPreferredEditorMode;
	}
	
}	// AbstractInplaceCellEditor
