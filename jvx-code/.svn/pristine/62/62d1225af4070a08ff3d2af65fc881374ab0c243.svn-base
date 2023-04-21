/*
 * Copyright 2011 SIB Visions GmbH
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
 * 08.04.2011 - [HM] - creation
 */
package com.sibvisions.apps.simpleapp;

import java.util.ArrayList;

import javax.rad.ui.component.IToggleActionComponent;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.IMouseListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIMouseEvent;

/**
 * The <code>ActionGroup</code> handles several <code>IToggleActionComponent</code>,
 * and allows only one of them to be selected.
 * 
 * @author Martin Handsteiner
 */
class ActionGroup implements IActionListener,
                             IMouseListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The event handler. */
	private ActionHandler eventAction = new ActionHandler();
	
	/** The list of buttons. */
	private ArrayList<IToggleActionComponent> buttons = new ArrayList<IToggleActionComponent>();
	
	/** the last sent index index for the action event. */
	private int iLastClickIndex = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ButtonGroup</code>.
	 */
	public ActionGroup()
	{
	}
	
	/**
	 * Creates a new instance of <code>ButtonGroup</code>.
	 * 
	 * @param pButtons the buttons.
	 */
	public ActionGroup(IToggleActionComponent... pButtons)
	{
		addButtons(pButtons);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void action(UIActionEvent pActionEvent)
	{
		setSelectedButton((IToggleActionComponent)pActionEvent.getSource());
		fireAction(pActionEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(UIMouseEvent pMouseEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(UIMouseEvent pMouseEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(UIMouseEvent pMouseEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(UIMouseEvent pMouseEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(UIMouseEvent pMouseEvent)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an action event.
	 * 
	 * @param pComponent the action component
	 * @return the action event.
	 */
	private UIActionEvent createActionEvent(IToggleActionComponent pComponent)
	{
		String actionCommand;
		if (pComponent == null)
		{
			actionCommand = null;
		}
		else
		{
			actionCommand = pComponent.getActionCommand();
		}
		
		return new UIActionEvent(pComponent, UIActionEvent.ACTION_PERFORMED, System.currentTimeMillis(), 0, actionCommand);
	}
	
	/**
	 * The event handler for actions.
	 * 
	 * @return the event handler for actions.
	 */
	public ActionHandler eventAction()
	{
		return eventAction;
	}
	
	/**
	 * Adds a button to this group. If a button it is removed and added again at the end.
	 * 
	 * @param pButton the button.
	 */
	public void addButton(IToggleActionComponent pButton)
	{
		if (pButton != null)
		{
			pButton.setSelected(false);
			
			removeButton(pButton);
			buttons.add(pButton);
			
			pButton.eventAction().addListener(this);
			pButton.eventMouseClicked().addListener(this);
		}
	}
	
	/**
	 * Adds buttons to this group. If a button it is removed and added again at the end.
	 * 
	 * @param pButtons the buttons.
	 */
	public void addButtons(IToggleActionComponent... pButtons)
	{
		if (pButtons != null)
		{
			for (int i = 0; i < pButtons.length; i++)
			{
				addButton(pButtons[i]);
			}
		}
	}
	
	/**
	 * Removes a button from this group.
	 * 
	 * @param pButton the button.
	 */
	public void removeButton(IToggleActionComponent pButton)
	{
		if (pButton != null)
		{
			pButton.eventAction().removeListener(this);
			pButton.eventMouseClicked().removeListener(this);
			buttons.remove(pButton);
		}
	}
	
	/**
	 * Sets the selected button.
	 * 
	 * @param pButton the button.
	 */
	public void setSelectedButton(IToggleActionComponent pButton)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			boolean isSelected = pButton == buttons.get(i);
			
			buttons.get(i).setSelected(isSelected);
		}
		
		fireAction(createActionEvent(pButton));
	}
	
	/**
	 * Gets the selected button.
	 * 
	 * @return the selected button.
	 */
	public IToggleActionComponent getSelectedButton()
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			if (buttons.get(i).isSelected())
			{
				return buttons.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the selected index.
	 * 
	 * @param pIndex the selected index.
	 */
	public void setSelectedIndex(int pIndex)
	{
		if (pIndex < 0 || pIndex >= buttons.size())
		{
			setSelectedButton(null);
		}
		else
		{
			setSelectedButton(buttons.get(pIndex));
		}
	}
	
	/**
	 * Gets the selected index.
	 * 
	 * @return the selected index.
	 */
	public int getSelectedIndex()
	{
		return buttons.indexOf(getSelectedButton());
	}
	
	/**
	 * Fires the action event.
	 * 
	 * @param pEvent the event
	 */
	private void fireAction(UIActionEvent pEvent)
	{
		if (eventAction != null)
		{
			int iIndex = buttons.indexOf(pEvent.getSource());
			
			if (iLastClickIndex != iIndex)
			{
				eventAction.dispatchEvent(pEvent);
				
				iLastClickIndex = iIndex;
			}
		}
	}

}	// ActionGroup
