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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.component;

import javax.rad.ui.component.ITextField;
import javax.swing.JTextField;

import com.sibvisions.rad.ui.swing.ext.WrappedInsetsBorder;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingTextField</code> is the <code>ITextField</code>
 * implementation for swing.
 * 
 * @param <C> the instanceof JTextField
 * 
 * @author Martin Handsteiner
 * @see	java.awt.TextField
 * @see	javax.swing.JTextField
 */
public class SwingTextField<C extends JTextField> extends SwingTextComponent<C, C> 
							                      implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingTextField</code>.
	 * 
	 * @param pTextField instance of JTextField
	 */
	protected SwingTextField(C pTextField)
	{
		super(pTextField);
		
		if (SwingFactory.isMacLaF())
		{
			pTextField.setBorder(new WrappedInsetsBorder(pTextField.getBorder()));
		}

		// set correct default values.
		super.setHorizontalAlignment(SwingFactory.getHorizontalAlignment(resource.getHorizontalAlignment()));
	}

	/**
	 * Creates a new instance of <code>SwingTextField</code>.
	 */
	public SwingTextField()
	{
		this((C)new JTextField());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * {@inheritDoc}
	 */
    public int getColumns()
    {
    	return component.getColumns();
    }

    /**
	 * {@inheritDoc}
	 */
    public void setColumns(int pColumns)
    {
    	component.setColumns(pColumns);
    }

    /**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		component.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));

		super.setHorizontalAlignment(pHorizontalAlignment);
	}
	
}	// SwingTextField
