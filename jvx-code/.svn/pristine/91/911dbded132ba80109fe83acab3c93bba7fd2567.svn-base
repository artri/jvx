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
 * 28.05.2009 - [JR] - setParent: check IToolBarPanel instead of UIToolBarPanel
 *                     (important because the launcher is technology dependent)
 * 05.02.2011 - [JR] - #278: setParent now checks null parent
 * 04.10.2011 - [JR] - #477: update translation when parent is changed                      
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIInsets;
import javax.rad.ui.IContainer;
import javax.rad.ui.IInsets;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

/**
 * Platform and technology independent TabSetPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIToolBar extends UIContainer<IToolBar>
                       implements IToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIToolBar</code>.
     *
     * @see IToolBar
     */
	public UIToolBar()
	{
		super(UIFactoryManager.getFactory().createToolBar());
	}

    /**
     * Creates a new instance of <code>UIToolBar</code> with the given
     * toolbar.
     *
     * @param pToolBar the toolbar
     * @see IToolBar
     */
	protected UIToolBar(IToolBar pToolBar)
	{
		super(pToolBar);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getOrientation()
    {
    	return uiResource.getOrientation();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setOrientation(int pOrientation)
    {
    	uiResource.setOrientation(pOrientation);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
    {
    	return uiResource.getMargins();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pMargins instanceof UIInsets)
		{
	    	uiResource.setMargins(((UIInsets)pMargins).getUIResource());
		}
		else
		{
	    	uiResource.setMargins(pMargins);
		}
    }
    
	/**
	 * Sets the insets with primitive types.
	 * 
	 * @param pTop the top insets.
	 * @param pLeft the left insets.
	 * @param pBottom the bottom insets.
	 * @param pRight the right insets.
	 */
    public void setMargins(int pTop, int pLeft, int pBottom, int pRight)
    {
    	uiResource.setMargins(getFactory().createInsets(pTop, pLeft, pBottom, pRight));
    }

	/**
	 * {@inheritDoc}
	 */
    public void setMovable(boolean pMovable)
    {
    	uiResource.setMovable(pMovable);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isMovable()
    {
    	return uiResource.isMovable();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * {@inheritDoc}
	 */
	public void setParent(IContainer pParent)
	{
		if ((pParent == null || pParent instanceof IToolBarPanel)
			&& (parent == null || parent instanceof IToolBarPanel))
		{
			IToolBarPanel tbp = (IToolBarPanel)pParent;
			
			if (pParent == null)
			{
				if (parent != null && ((IToolBarPanel)parent).indexOfToolBar(this) >= 0)
				{
				    throw new IllegalArgumentException("Can't unset parent, because this component is still added!");
				}
			}
			else
			{
				if (tbp.indexOfToolBar(this) < 0)
				{
					throw new IllegalArgumentException("Can't set parent, because this component is not added!");
				}
			}
	  
			boolean bUpdateTrans = parent != null && parent != pParent;
			
			parent = pParent;
			
			if (bUpdateTrans && isNotified())
			{
				updateTranslation();
			}
		}
		else
		{
			super.setParent(pParent);
		}
	}
	
}	// UIToolBar
