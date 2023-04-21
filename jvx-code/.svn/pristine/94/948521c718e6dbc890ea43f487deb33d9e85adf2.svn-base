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
 * 11.10.2009 - [JR] - constructor: removed spaces
 */
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Insets;

import javax.rad.ui.IInsets;
import javax.rad.ui.component.ICheckBox;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.swing.ext.JVxCheckBox;

/**
 * The <code>SwingCheckBox</code> is the <code>ICheckBox</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JCheckBox
 * @see javax.rad.ui.component.ICheckBox
 */
public class SwingCheckBox extends SwingToggleButton<JCheckBox>
                           implements ICheckBox
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingCheckBox</code>.
	 */
	public SwingCheckBox()
	{
		super(new JVxCheckBox(), false);

		initOriginalBackground(null);
		
		setMargins(new AwtInsets(3, 0, 3, 0));
	}

	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	super.setMargins(pMargins);
    	
    	if (pMargins != null)
    	{
    		Insets ins = (Insets)pMargins.getResource();
    		
    		if (ins.left == 0 && ins.right == 0 && ins.top == 0 && ins.bottom == 0)
    		{
    			resource.setBorder(BorderFactory.createEmptyBorder());
    		}
    		else
    		{
    			resource.setBorder(new EmptyBorder(ins));
    		}
    	}
    	else
    	{
    		resource.setBorder(BorderFactory.createEmptyBorder());
    	}
    }
    
}	// SwingCheckBox
