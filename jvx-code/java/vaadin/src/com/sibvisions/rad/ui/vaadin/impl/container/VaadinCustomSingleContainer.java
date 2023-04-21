/*
 * Copyright 2013 SIB Visions GmbH
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
 * 27.03.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.container;

import javax.rad.ui.IComponent;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinSingleComponentContainer;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The <code>VaadinCustomSingleContainer</code> class is a cointainer that allows using custom 
 * {@link SingleComponentContainer}s.
 * 
 * @author René Jahn
 */
public class VaadinCustomSingleContainer extends VaadinSingleComponentContainer<SingleComponentContainer>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>VaadinCustomSingleContainer</code>.
     *
     * @param pContainer an instance of {@link SingleComponentContainer}
     * @see javax.rad.ui.IContainer
     */
	public VaadinCustomSingleContainer(SingleComponentContainer pContainer)
	{
		super(pContainer);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void performLayout()
    {
    	super.performLayout();

    	fixChildrenSizes();
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Fixes the sizes of all children dependent on the current parent.
	 */
	private void fixChildrenSizes() 
	{
        for (IComponent component : getComponents())
        {
            if (VaadinUtil.isParentWidthDefined(resource))
            {
                ((VaadinComponentBase<?, ?>) component).setWidthFull();
            }
            else
            {
                ((VaadinComponentBase<?, ?>) component).setWidthUndefined();
            }
            
            if (VaadinUtil.isParentHeightDefined(resource))
            {
                ((VaadinComponentBase<?, ?>) component).setHeightFull();
            }
            else
            {
                ((VaadinComponentBase<?, ?>) component).setHeightUndefined();
            }
        }
    }   

} 	// VaadinCustomSingleContainer
