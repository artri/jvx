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
 * 24.09.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.menu;

import javax.rad.ui.IContainer;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.ISeparator;

import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;

/**
 * The <code>VaadinSeparator</code> class is the vaadin implementation of {@link ISeparator}.
 * 
 * @author René Jahn
 */
public class VaadinSeparator extends VaadinComponent<WrappedSeparator> 
                             implements ISeparator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * new instance.
	 */
	public VaadinSeparator() 
	{
		super(new WrappedSeparator());
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setId(String pId)
    {
        super.setId(pId);
        
        //forward id to menubar because vaadin doesn't set IDs to menu items!
        IContainer parent = getParent();

        while (parent != null && !(parent instanceof IMenuBar))
        {
            parent = parent.getParent();
        }
        
        if (parent != null)
        {
            ((VaadinMenuBar)parent).setId(this, pId);
        }
    }

} 	// VaadinSeparator
