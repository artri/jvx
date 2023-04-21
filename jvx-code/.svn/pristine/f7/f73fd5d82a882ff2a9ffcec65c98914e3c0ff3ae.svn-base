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
 * 11.10.2009 - [JR] - used JVxPanel instead of JPanel
 *                   - get/setBackgroundImage implemented
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.rad.ui.IPoint;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;

import com.sibvisions.rad.ui.awt.impl.AwtPoint;
import com.sibvisions.rad.ui.swing.ext.JVxPanel;
import com.sibvisions.rad.ui.swing.ext.JVxScrollPane;
import com.sibvisions.rad.ui.swing.impl.SwingScrollComponent;

/**
 * The <code>SwingPanel</code> is the <code>IPanel</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JScrollPane
 * @see IScrollPanel
 */
public class SwingScrollPanel extends SwingScrollComponent<JVxScrollPane, JVxPanel> 
                              implements IScrollPanel 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The inner panel. */
    private SwingPanel innerPanel;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>SwingPanel</code>.
     */
    public SwingScrollPanel()
    {
        this(new SwingPanel());
    }
    
	/**
	 * Creates a new instance of <code>SwingPanel</code>.
	 * 
	 * @param pInnerPanel the inner panel.
	 */
	private SwingScrollPanel(SwingPanel pInnerPanel)
	{
		super(new JVxScrollPane(pInnerPanel.getResource()));
		
		innerPanel = pInnerPanel;
		
		resource.setBorder(null);
		resource.getHorizontalScrollBar().setUnitIncrement(10);
		resource.getVerticalScrollBar().setUnitIncrement(10);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutIntern(LayoutManager pLayoutManager)
	{
		component.setLayout(pLayoutManager);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	{
		component.add(pComponent, pConstraints, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeIntern(Component pComponent)
	{
		component.remove(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the inner panel.
	 * 
	 * @return the inner panel.
	 */
	public IPanel getInnerPanel()
	{
	    return innerPanel;
	}
	
    /**
     * Gets the scroll position.
     * 
     * @return the scroll position.
     */
    public IPoint getScrollPosition()
    {
        return new AwtPoint(resource.getHorizontalScrollBar().getValue(), resource.getVerticalScrollBar().getValue());
    }
    
    /**
     * Sets the scroll position.
     * 
     * @param pScrollPosition the scroll position.
     */
    public void setScrollPosition(IPoint pScrollPosition)
    {
        if (pScrollPosition == null)
        {
            resource.getHorizontalScrollBar().setValue(0);
            resource.getVerticalScrollBar().setValue(0);
        }
        else
        {
            resource.getHorizontalScrollBar().setValue(pScrollPosition.getX());
            resource.getVerticalScrollBar().setValue(pScrollPosition.getY());
        }
    }
    
}	// SwingScrollPanel
