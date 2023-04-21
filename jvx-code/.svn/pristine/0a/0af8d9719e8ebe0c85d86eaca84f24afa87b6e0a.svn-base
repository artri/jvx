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
 * 03.05.2009 - [JR] - setParent overwritten
 * 05.02.2011 - [JR] - setParent allows null (see changes from 22.12.2010)
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.menu;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.menu.IMenuBar;

/**
 * Platform and technology independent menu bar.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JMenuBar
 */
public class UIMenuBar extends UIContainer<IMenuBar> 
                       implements IMenuBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>UIMenuBar</code>.
     *
     * @see IMenuBar
     */
	public UIMenuBar()
	{
		super(UIFactoryManager.getFactory().createMenuBar());
	}

    /**
     * Creates a new instance of <code>UIMenuBar</code> wit the given 
     * menu bar.
     *
     * @param pMenuBar the menu bar
     * @see IMenuBar
     */
	protected UIMenuBar(IMenuBar pMenuBar)
	{
		super(pMenuBar);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IContainer pParent)
	{
		if (pParent == null || pParent instanceof IFrame)
		{
			parent = pParent;
		}
		else
		{
			throw new IllegalArgumentException("Only 'IFrame' instances are allowed as parent of an 'UIMenuBar'");
		}
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
    	if (pConstraints instanceof String)
    	{
    		IContainer parent = AbstractUIMenuItem.getMenu(this, (String)pConstraints);
        	
    		parent.add(pComponent, null, pIndex);
    	}
    	else
    	{
    		super.add(pComponent, pConstraints, pIndex);
    	}
    }
    

}	// UIMenuBar
