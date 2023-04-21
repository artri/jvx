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
 * 27.11.2008 - [HM] - creation
 * 19.07.2009 - [JR] - setText: repaint [BUGFIX]
 * 21.09.2011 - [JR] - #471: setText now checks empty text
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Color;
import java.awt.Font;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.container.IGroupPanel;
import javax.swing.JPanel;

import com.sibvisions.rad.ui.swing.ext.JVxPanel;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingGroupPanel</code> is the <code>IGroupPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	JPanel
 * @see IGroupPanel
 */
public class SwingGroupPanel extends SwingComponent<JVxPanel> 
							 implements IGroupPanel 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingGroupPanel</code>.
	 */
	public SwingGroupPanel()
	{
		super(new JVxPanel());

        resource.setTitledBorderVisible(true);

        setHorizontalAlignment(ALIGN_LEFT);
		setVerticalAlignment(ALIGN_TOP);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return resource.getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
	{
	    resource.setTitle(pText);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		super.setHorizontalAlignment(pHorizontalAlignment);
		
		resource.setHorizontalTitleAlignment(pHorizontalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		super.setVerticalAlignment(pVerticalAlignment);
		
		resource.setVerticalTitleAlignment(pVerticalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
	{
		super.setFont(pFont);
		
		if (pFont == null) 
		{
	        resource.setTitleFont(null);
		}
		else
		{
		    resource.setTitleFont((Font)pFont.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pColor)
	{
		super.setForeground(pColor);
		
		if (pColor == null)
		{
		    resource.setTitleColor(null);
		}
		else
		{
		    resource.setTitleColor((Color)pColor.getResource());
		}
	}

}	// SwingGroupPanel
