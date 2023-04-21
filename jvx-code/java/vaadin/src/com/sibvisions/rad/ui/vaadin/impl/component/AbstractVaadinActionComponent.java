/*
 * Copyright 2012 SIB Visions GmbH
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
 * 17.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.IInsets;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.event.ActionHandler;

import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinInsets;
import com.vaadin.ui.Component;

/**
 * The <code>AbstractVaadinActionComponent</code> is the base class for vaadin implementations of {@link IActionComponent}.
 *
 * @author Benedikt Cermak
 * @param <C> an instance of {@link Component}
 */
public abstract class AbstractVaadinActionComponent<C extends Component> extends AbstractVaadinLabeledIcon<C>
                                                                         implements IActionComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** EventHandler for actionPerformed. */
	protected ActionHandler eventActionPerformed = null;

	/** the action command. */
	protected String sActionCommand = null;
	
	/** Margin for the component. **/
	private IInsets insMargins = new VaadinInsets(0, 0, 0, 0);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractVaadinActionComponent</code>.
     *
     * @param pComponent an instance of {@link Component}
     * @see IActionComponent
     */
	protected AbstractVaadinActionComponent(C pComponent)
	{
		super(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getActionCommand()
    {
    	return sActionCommand;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setActionCommand(String pActionCommand)
    {
		sActionCommand = pActionCommand;
    }

	/**
	 * {@inheritDoc}
	 */
	public IInsets getMargins()
	{
		return insMargins;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMargins(IInsets pMargins)
	{
		insMargins = pMargins;
		
		updateMargins();
	}

	/**
	 * {@inheritDoc}
	 */
	public ActionHandler eventAction()
	{
		if (eventActionPerformed == null)
		{
			eventActionPerformed = new ActionHandler();
		}
		return eventActionPerformed;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Updates the margins of the component.
	 */
    protected void updateMargins()
    {
        CssExtension csse = getCssExtension();

        if (insMargins == null)
        {
            csse.removeAttribute("padding-top");
            csse.removeAttribute("padding-left");
            csse.removeAttribute("padding-right");
            csse.removeAttribute("padding-bottom");           
        }
        else
        {
            csse.addAttribute("padding-top", insMargins.getTop() + "px");
            csse.addAttribute("padding-left", insMargins.getLeft() + "px");
            csse.addAttribute("padding-right", insMargins.getRight() + "px");
            csse.addAttribute("padding-bottom", insMargins.getBottom() + "px");           
        }
    }
    
	/**
	 * Gets whether an action event was assigned to this component.
	 * 
	 * @return <code>true</code> if an action event was added, <code>false</code> otherwise
	 */
	public boolean hasActionEventHandler()
	{
		return eventActionPerformed != null && eventActionPerformed.getListenerCount() > 0;
	}
	
}	// AbstractVaadinActionComponent
