/*
 * Copyright 2009 SIB Visions GmbH
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
 * 17.11.2008 - [HM] - creation
 */
package javax.rad.genui.celleditor;

import javax.rad.ui.celleditor.IInplaceCellEditor;

/**
 * Platform and technology independent editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <CE> an instance of IInplaceCellEditor.
 */
public class UIInplaceCellEditor<CE extends IInplaceCellEditor> extends UICellEditor<CE> 
                                                                implements IInplaceCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Constructs a new <code>UIInplaceCellEditor</code>.
	 * 
	 * @param pCEResource the CellEditor resource.
	 * @see IInplaceCellEditor  
	 */
	protected UIInplaceCellEditor(CE pCEResource)
	{
		super(pCEResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	public int getPreferredEditorMode()
	{
		return getUIResource().getPreferredEditorMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreferredEditorMode(int pPreferredEditorMode)
	{
		getUIResource().setPreferredEditorMode(pPreferredEditorMode);
	}
    
}	// UIInplaceCellEditor
