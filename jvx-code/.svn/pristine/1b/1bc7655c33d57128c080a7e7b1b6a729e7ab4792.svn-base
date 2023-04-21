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
 * 22.12.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext.focus;

import java.awt.Component;
import java.awt.Container;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * The <code>originalComperator</code> enables tab index based .
 *  
 * @author Martin Handsteiner
 */
public class TabIndexFocusTraversalPolicy extends LayoutFocusTraversalPolicy implements Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Original comperator. */
	private Comparator originalComperator;

	/** Center component for compare. */
	private JComponent centerComponent = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Constructs a LayoutFocusTraversalPolicy.
     */
    public TabIndexFocusTraversalPolicy() 
    {
    	super();
    	
    	originalComperator = super.getComparator();
    	
    	setComparator(new TabIndexComparator(this));
    	setImplicitDownCycleTraversal(false); // Avoid implicit down cycle traversal, as in jdk 1.7 > it leads in focus jump from last to first tab index 
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected Comparator getComparator() 
    {
    	return originalComperator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getComponentAfter(Container pContainer, Component pComponent) 
    {
    	searchCenterComponent(pContainer);
    	
    	return super.getComponentAfter(pContainer, pComponent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getComponentBefore(Container pContainer, Component pComponent) 
    {
    	searchCenterComponent(pContainer);
    	
    	return super.getComponentBefore(pContainer, pComponent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getFirstComponent(Container pContainer) 
    {
    	searchCenterComponent(pContainer);
    	
    	return super.getFirstComponent(pContainer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getLastComponent(Container pContainer) 
    {
    	searchCenterComponent(pContainer);
    	
    	return super.getLastComponent(pContainer);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean accept(Component pComponent) 
    {
    	return super.accept(pComponent) && !(pComponent instanceof JTextComponent && !((JTextComponent)pComponent).isEditable());  
	}
    
    /**
     * {@inheritDoc}
     */
	public void run()
	{
		centerComponent = null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Find the center component.
     * @return the center component.
     */
	protected JComponent getCenterComponent() 
	{
		return centerComponent;
	}

    /**
     * Find the center component.
     * @param pContainer the container.
     */
	private final void searchCenterComponent(Container pContainer) 
    {
    	if (centerComponent == null)
    	{
    		searchCenterComponent(pContainer, Integer.MIN_VALUE);

    		SwingUtilities.invokeLater(this);
    	}
    }
    
    /**
     * Find the center component.
     * @param pContainer the container.
     * @param pCurrentMaxIndex the current max index.
     */
    @SuppressWarnings("deprecation")
	private final void searchCenterComponent(Container pContainer, int pCurrentMaxIndex) 
    {
		if (pContainer.isVisible() && pContainer.isDisplayable()) 
    	{
			for (int i = 0, count = pContainer.getComponentCount(); i < count; i++) 
			{
				Component comp = pContainer.getComponent(i);

	    		if (comp instanceof JComponent)
	    		{
	    			JComponent component = (JComponent)comp;
	    			Object tabIndex = component.getClientProperty("tabIndex");
	    			
	    			if (tabIndex instanceof Number)
	    			{
	    				int index = ((Number)tabIndex).intValue();
	    				
	    				if (index > pCurrentMaxIndex)
	    				{
	    					pCurrentMaxIndex = index;
	    					centerComponent = component;	
	    				}
	    			}
	    		}

				if ((comp instanceof Container)
		                && !((Container)comp).isFocusTraversalPolicyProvider()
		                && !((Container)comp).isFocusCycleRoot()
		                && !((comp instanceof JComponent) 
		                     && ((JComponent)comp).isManagingFocus())) 
		        {
			    	searchCenterComponent((Container)comp, pCurrentMaxIndex);
		        }
			}
    	}
    }

    
} 	// TabIndexFocusTraversalPolicy
