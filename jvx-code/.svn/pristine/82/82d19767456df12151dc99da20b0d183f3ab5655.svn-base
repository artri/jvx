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
 * 11.12.2013 - [JR] - #891: set default orientation to LEFT_RIGHT
 *                   - #892: added additional constructors  
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.container.ISplitPanel;

/**
 * Platform and technology independent SplitPanel.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UISplitPanel extends UIContainer<ISplitPanel> 
                          implements ISplitPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The left or top Component. */
	private transient UIPanel	defaultFirstComponent = new UIPanel(new UIBorderLayout());

	/** The right or bottom Component. */
	private transient UIPanel	defaultSecondComponent = new UIPanel(new UIBorderLayout());

	/** The left or top Component. */
	private transient IComponent	firstComponent;

	/** The right or bottom Component. */
	private transient IComponent	secondComponent;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UISplitPanel</code>.
     *
     * @see ISplitPanel
     */
	public UISplitPanel()
	{
		this(UIFactoryManager.getFactory().createSplitPanel(), SPLIT_LEFT_RIGHT, DIVIDER_RELATIVE);
	}

    /**
     * Creates a new instance of <code>UISplitPanel</code> with the given
     * orientation.
     *
     * @param pOrientation the orientation
     * @see #setOrientation(int)
     */
	public UISplitPanel(int pOrientation)
	{
		this(UIFactoryManager.getFactory().createSplitPanel(), pOrientation, DIVIDER_RELATIVE);
	}

    /**
     * Creates a new instance of <code>UISplitPanel</code> with the given
     * orientation and alignment.
     *
     * @param pOrientation the orientation
     * @param pAlignment the divider alignment
     * @see #setOrientation(int)
     * @see #setDividerAlignment(int)
     */
	public UISplitPanel(int pOrientation, int pAlignment)
	{
		this(UIFactoryManager.getFactory().createSplitPanel(), pOrientation, pAlignment);
	}
	
    /**
     * Creates a new instance of <code>UISplitPanel</code> with the given
     * split panel.
     *
     * @param pPanel the split panel
     * @see ISplitPanel
     */
	protected UISplitPanel(ISplitPanel pPanel)
	{
		this(pPanel, SPLIT_LEFT_RIGHT, DIVIDER_RELATIVE);
	}
	
    /**
     * Creates a new instance of <code>UISplitPanel</code> with the given
     * split panel, orientation and divider alignment.
     *
     * @param pPanel the split panel
     * @param pOrientation the orientation
     * @param pAlignment the divider alignment
     * @see ISplitPanel
     */
	protected UISplitPanel(ISplitPanel pPanel, int pOrientation, int pAlignment)
	{
		super(pPanel);
		
		setOrientation(pOrientation);
		setDividerAlignment(pAlignment);
		
		setFirstComponent(defaultFirstComponent);
		setSecondComponent(defaultSecondComponent);
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
    public IComponent getFirstComponent()
    {
    	return firstComponent;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setFirstComponent(IComponent pComponent)
    {
        if (pComponent == null) 
        {
        	if (firstComponent != defaultFirstComponent)
        	{
        		add(defaultFirstComponent, FIRST_COMPONENT);
        	}
        } 
        else
        {
            add(pComponent, FIRST_COMPONENT);
        }
    }

	/**
	 * {@inheritDoc}
	 */
    public IComponent getSecondComponent()
    {
    	return secondComponent;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSecondComponent(IComponent pComponent)
    {
        if (pComponent == null) 
        {
        	if (secondComponent != defaultSecondComponent)
        	{
        		add(defaultSecondComponent, SECOND_COMPONENT);
        	}
        } 
        else 
        {
            add(pComponent, SECOND_COMPONENT);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
        if (pConstraints == FIRST_COMPONENT && firstComponent != null)
        {
        	remove(firstComponent);
        }
        else if (pConstraints == SECOND_COMPONENT && secondComponent != null)
        {
        	remove(secondComponent);
        }
        else if (pConstraints == null)
        {
        	if (firstComponent == null)
        	{
        		pConstraints = FIRST_COMPONENT;
        	}
        	else if (secondComponent == null)
        	{
        		pConstraints = SECOND_COMPONENT;
        	}
        	else if (firstComponent == defaultFirstComponent)
        	{
        		remove(defaultFirstComponent);
        		pConstraints = FIRST_COMPONENT;
        	}
        	else if (secondComponent == defaultSecondComponent)
        	{
        		remove(defaultSecondComponent);
        		pConstraints = SECOND_COMPONENT;
        	}
        }
        if (pConstraints == FIRST_COMPONENT)
        {
        	super.add(pComponent, FIRST_COMPONENT, 0);
        	firstComponent = pComponent;
        }
        else if (pConstraints == SECOND_COMPONENT)
        {
        	super.add(pComponent, SECOND_COMPONENT, -1);
        	secondComponent = pComponent;
        }
        else
        {
        	throw new IllegalArgumentException("SplitPanel can only handle first and second component!");
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
	{
		IComponent comp = getComponent(pIndex);
		
		super.remove(pIndex);
		
		if (comp == firstComponent)
		{
			firstComponent = null;	
		}
		else if (comp == secondComponent)
		{
			secondComponent = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
    public int getDividerPosition()
    {
    	return uiResource.getDividerPosition();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDividerPosition(int pDividerPosition)
    {
    	uiResource.setDividerPosition(pDividerPosition);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getDividerAlignment()
    {
    	return uiResource.getDividerAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setDividerAlignment(int pDividerAlignment)
	{
		uiResource.setDividerAlignment(pDividerAlignment);
	}

}	// UISplitPanel
