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
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.celleditor.ITextCellEditor;

/**
 * Platform and technology independent text editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UITextCellEditor extends UIInplaceCellEditor<ITextCellEditor> 
                              implements ITextCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITextCellEditor</code>.
     *
     * @see ITextCellEditor
     */
	public UITextCellEditor()
	{
		super(UIFactoryManager.getFactory().createTextCellEditor());
	}

    /**
     * Creates a new instance of <code>UITextCellEditor</code> with the given
     * text cell editor.
     *
     * @param pEditor the text cell editor
     * @see ITextCellEditor
     */
	protected UITextCellEditor(ITextCellEditor pEditor)
	{
		super(pEditor);
	}
	
    /**
     * Creates a new instance of <code>UITextCellEditor</code>.
     *
     * @param pHorizontalAlignment the horizontal alignment. 
     * @see ITextCellEditor
     */
	public UITextCellEditor(int pHorizontalAlignment)
	{
		super(UIFactoryManager.getFactory().createTextCellEditor());
		
		setHorizontalAlignment(pHorizontalAlignment);
	}

    /**
     * Creates a new instance of <code>UITextCellEditor</code>.
     *
     * @param pContentType the contenttype.
     * @see ITextCellEditor
     */
	public UITextCellEditor(String pContentType)
	{
		super(UIFactoryManager.getFactory().createTextCellEditor());
		
		setContentType(pContentType);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getContentType()
	{
		return getUIResource().getContentType();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContentType(String pContentType)
	{
		getUIResource().setContentType(pContentType);
	}

}	// UITextCellEditor
