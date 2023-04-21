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
 * 16.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.component.ITextArea;

/**
 * Platform and technology independent text area.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UITextArea extends AbstractUITextField<ITextArea> 
                        implements ITextArea
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITextArea</code>.
     *
     * @see ITextArea
     */
	public UITextArea()
	{
		this(UIFactoryManager.getFactory().createTextArea());
	}

    /**
     * Creates a new instance of <code>UITextArea</code> with the given
     * text area.
     * 
     * @param pTextArea the text area
     * @see ITextArea
     */
	protected UITextArea(ITextArea pTextArea)
	{
		super(pTextArea);
	}
	
    /**
     * Creates a new instance of <code>UITextArea</code>.
     *
     * @param pText the text.
     * @see ITextArea
     */
	public UITextArea(String pText)
	{
		this();
		
		setText(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getRows()
    {
    	return uiResource.getRows();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setRows(int pRows)
    {
    	uiResource.setRows(pRows);
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isWordWrap()
	{
		return uiResource.isWordWrap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWordWrap(boolean pWordWrap)
	{
		uiResource.setWordWrap(pWordWrap);
	}

}	// UITextArea
