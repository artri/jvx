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

import java.util.Comparator;

import javax.swing.JComponent;

/**
 * The <code>TabIndexComperator</code> orders by tab index.
 *  
 * @author Martin Handsteiner
 */
public class TabIndexComparator implements Comparator, java.io.Serializable
{
	/** the original comparator. */
	TabIndexFocusTraversalPolicy tabIndexFocusTraversalPolicy;
	
    /**
     * Constructs a TabIndexComperator.
     * @param pTabIndexFocusTraversalPolicy the tabIndexFocusTraversalPolicy.
     */
    public TabIndexComparator(TabIndexFocusTraversalPolicy pTabIndexFocusTraversalPolicy) 
    {
    	tabIndexFocusTraversalPolicy = pTabIndexFocusTraversalPolicy;
    }
    
    /**
     * {@inheritDoc}
     */
	public int compare(Object pObj1, Object pObj2)
	{
		if (pObj1 == pObj2)
		{
			return 0;
		}
		
		int indexObj1 = Integer.MAX_VALUE;
		if (pObj1 instanceof JComponent)
		{
			Object valObj1 = ((JComponent)pObj1).getClientProperty("tabIndex");
			
			if (valObj1 instanceof Number)
			{
				indexObj1 = ((Number)valObj1).intValue();
			}
		}
		int indexObj2 = Integer.MAX_VALUE;
		if (pObj2 instanceof JComponent)
		{
			Object valObj2 = ((JComponent)pObj2).getClientProperty("tabIndex");
			
			if (valObj2 instanceof Number)
			{
				indexObj2 = ((Number)valObj2).intValue();
			}
		}

		if (indexObj1 == indexObj2)
		{
			Comparator comparator = tabIndexFocusTraversalPolicy.getComparator();
			JComponent centerComponent = tabIndexFocusTraversalPolicy.getCenterComponent();
			
			if (centerComponent != null) // sort components before center component to the end.
			{
				int compare = comparator.compare(pObj2, centerComponent) - comparator.compare(pObj1, centerComponent);
				// if both are before the center component let them compare with comparator (-1 - (-1) = 0)
				// if both are after the center component let them compare with comparator (1 - 1 = 0)
				// if pObj2 is after and pObj1 is before the center component (1 - (-1) = 2) means pObj1 is ordered after pObj2
				// if pObj2 is before and pObj1 is after the center component (-1 - 1 = -2) means pObj1 is ordered before pObj2
				if (compare != 0)
				{
					return compare;
				}
			}
			return comparator.compare(pObj1, pObj2);
		}
		else if (indexObj1 < indexObj2)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
	
	
	
}
