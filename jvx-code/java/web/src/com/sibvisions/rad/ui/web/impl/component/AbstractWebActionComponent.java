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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;

/**
 * Web server implementation of {@link IActionComponent}.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractWebActionComponent extends AbstractWebLabeledIconComponent
                                                 implements IActionComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** EventHandler for actionPerformed. */
	private ActionHandler eventActionPerformed = null;
	
	/** ActionCommand. */
	private String actionCommand = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWebActionComponent</code>.
     *
     * @see IActionComponent
     */
	protected AbstractWebActionComponent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public Key getAccelerator()
    {
    	return getProperty("accelerator", null);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pAccelerator)
    {
    	setProperty("accelerator", pAccelerator);
    }

	/**
	 * {@inheritDoc}
	 */
	public String getActionCommand()
    {
    	return actionCommand;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setActionCommand(String pActionCommand)
    {
		actionCommand = pActionCommand;
    }
	
	/**
	 * {@inheritDoc}
	 */
    public ActionHandler eventAction()
    {
		if (eventActionPerformed == null)
		{
			eventActionPerformed = new ActionHandler();
			
			setProperty("action", eventActionPerformed);
		}
		return eventActionPerformed;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Executes the click and sends the action event. This method takes care of a
     * possible component state. Use {@link #clickIgnoreState()} if state should
     * be ignored.
     */
	public void click()
	{
		clickIntern();
	}
	
    /**
     * Executes the click and sends the action event. This method should be used if
     * the component has a state and the state shouldn't be changed by clicking the
     * component.
     */
	public void clickIgnoreState()
	{
		clickIntern();
	}
	
	/**
	 * Sends the action event to registered listeners, if component is visible and enabled.
	 */
	private void clickIntern()
	{
		if (isEnabled() && isVisible())
		{
			if (eventActionPerformed != null && eventActionPerformed.isDispatchable())
			{
				getFactory().synchronizedDispatchEvent(eventActionPerformed, new UIActionEvent(getEventSource(), 
						 							   UIActionEvent.ACTION_PERFORMED, 
						 							   System.currentTimeMillis(), 
						 							   0, 
						 							   getActionCommand()));
			}
		}
		else
		{
			error("Tried to click button '", getName(), "' but it's not possible (enabled: ", Boolean.valueOf(isEnabled()), ", visible: ", Boolean.valueOf(isVisible()), ")");
		}
	}    
    
}	// AbstractWebActionComponent
