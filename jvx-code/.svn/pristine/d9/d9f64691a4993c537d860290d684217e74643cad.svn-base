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

import java.util.Locale;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.celleditor.INumberCellEditor;

/**
 * Platform and technology independent number editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UINumberCellEditor extends UIInplaceCellEditor<INumberCellEditor> 
                                implements INumberCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UINumberCellEditor</code>.
     *
     * @see INumberCellEditor
     */
	public UINumberCellEditor()
	{
		super(UIFactoryManager.getFactory().createNumberCellEditor());
	}

    /**
     * Creates a new instance of <code>UINumberCellEditor</code> with the given
     * number cell editor.
     *
     * @param pEditor the number cell editor
     * @see INumberCellEditor
     */
	protected UINumberCellEditor(INumberCellEditor pEditor)
	{
		super(pEditor);
	}
	
    /**
     * Creates a new instance of <code>UINumberCellEditor</code>.
     *
     * @param pHorizontalAlignment the horizontal alignment
     * @see INumberCellEditor
     */
	public UINumberCellEditor(int pHorizontalAlignment)
	{
		super(UIFactoryManager.getFactory().createNumberCellEditor());
		
		setHorizontalAlignment(pHorizontalAlignment);
	}

    /**
     * Creates a new instance of <code>UINumberCellEditor</code> with the given format.
     *
     * @param pNumberFormat then NumberFormat.
     * @see INumberCellEditor
     */
	public UINumberCellEditor(String pNumberFormat)
	{
		super(UIFactoryManager.getFactory().createNumberCellEditor());
		
		getUIResource().setNumberFormat(pNumberFormat);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getNumberFormat()
	{
		return getUIResource().getNumberFormat();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNumberFormat(String pNumberFormat)
	{
		getUIResource().setNumberFormat(pNumberFormat);
	}

    /**
     * {@inheritDoc}
     */
    public Locale getLocale()
    {
        return getUIResource().getLocale();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocale(Locale pLocale)
    {
        getUIResource().setLocale(pLocale);
    }

}	// UINumberCellEditor
