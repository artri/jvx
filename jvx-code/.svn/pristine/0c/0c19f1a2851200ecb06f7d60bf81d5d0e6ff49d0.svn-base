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
 * 23.04.2013 - [SW] - creation
 * 10.12.2015 - [JR] - save TargetComponent as weak reference (mem leak)
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;

import com.sibvisions.rad.ui.vaadin.ext.ui.MouseOverButton;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable.TableComponent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * The <code>ShortcutHandler</code> class handles the shortcut key events for editors.
 *   
 * @author Stefan Wurm
 */
public class ShortcutHandler implements Handler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The action for escape key. **/
	public static final Action ACTION_ESCAPE = new ShortcutAction("ESCAPE", ShortcutAction.KeyCode.ESCAPE, null);
	
	/** The action for enter key. **/
	public static final Action ACTION_ENTER = new ShortcutAction("ENTER", ShortcutAction.KeyCode.ENTER, null);
	
	/** The action for shift + enter key. **/
	public static final Action ACTION_SHIFT_ENTER = new ShortcutAction("Shift+ENTER", ShortcutAction.KeyCode.ENTER, new int[] {ShortcutAction.ModifierKey.SHIFT});
	
	/** The action for ctrl + enter key. **/
	public static final Action ACTION_CTRL_ENTER = new ShortcutAction("CTRL+ENTER", ShortcutAction.KeyCode.ENTER, new int[] {ShortcutAction.ModifierKey.CTRL}); 

	/** The action for alt + enter key. **/
	public static final Action ACTION_ALT_ENTER = new ShortcutAction("ALT+ENTER", ShortcutAction.KeyCode.ENTER, new int[] {ShortcutAction.ModifierKey.ALT}); 
	
	/** The action for meta + enter key. **/
	public static final Action ACTION_META_ENTER = new ShortcutAction("META+ENTER", ShortcutAction.KeyCode.ENTER, new int[] {ShortcutAction.ModifierKey.META}); 
	
	/** The action for insert key. **/
	public static final Action ACTION_INSERT = new ShortcutAction("INSERT", ShortcutAction.KeyCode.INSERT, new int[] {ShortcutAction.ModifierKey.CTRL});
	
	/** The action for delete key. **/
	public static final Action ACTION_DELETE = new ShortcutAction("DELTE", ShortcutAction.KeyCode.DELETE, new int[] {ShortcutAction.ModifierKey.CTRL});
		
	/** The action for F2 key. **/
	public static final Action ACTION_EDIT = new ShortcutAction("F2", ShortcutAction.KeyCode.F2, null);
	
	/** The action for shift + tab key. **/
	public static final Action ACTION_SHIFT_TAB = new ShortcutAction("Shift+TAB", ShortcutAction.KeyCode.TAB, new int[] {ShortcutAction.ModifierKey.SHIFT});
	
	/** The action for tab key. **/
	public static final Action ACTION_TAB = new ShortcutAction("Shift+TAB", ShortcutAction.KeyCode.TAB, null);
		
	/** The actual component. **/
	private WeakReference<IEditorComponent> wrefTargetEditor = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */	
	public Action[] getActions(Object target, Object sender)
	{
		return new Action[] {ACTION_ESCAPE,
							 ACTION_ENTER,
							 ACTION_SHIFT_ENTER,
							 ACTION_CTRL_ENTER,
							 ACTION_ALT_ENTER,
							 ACTION_META_ENTER,
							 ACTION_INSERT,
							 ACTION_DELETE,
							 ACTION_EDIT};
	}

	/**
	 * {@inheritDoc}
	 */	
	public void handleAction(Action action, Object sender, Object target)
	{		
	    IEditorComponent component;
	    
	    if (wrefTargetEditor != null)
	    {
	        component = wrefTargetEditor.get();
	    }
	    else
	    {
	        component = null;
	    }
	    
		if (target == null && component != null && component instanceof TableComponent)
		{
			((TableComponent)component).handleActionForTargetIsNull(action);
		}
		else if (target instanceof IEditorComponent)
		{
			((IEditorComponent)target).handleAction(action);
		}
		
		if ((action == ACTION_ENTER || action == ACTION_SHIFT_ENTER || action == ACTION_CTRL_ENTER || action == ACTION_ALT_ENTER || action == ACTION_META_ENTER)
				&& sender != null && sender instanceof Component)
		{
			MouseOverButton button = searchButtonWithShortcut((Component) sender, (ShortcutAction)action);
			if (button == null && action == ACTION_ENTER)
			{
				button = searchDefaultButton((Component) sender);
			}
			
			if (button != null)
			{
				button.click();
			}
		}

		wrefTargetEditor = null;
	}
	
	/**
	 * Sets the target component.
	 * 
	 * @param pTargetComponent the target component.
	 */
	public void setTargetComponent(IEditorComponent pTargetComponent)
	{
		wrefTargetEditor = new WeakReference<IEditorComponent>(pTargetComponent);
	}
	
	/**
	 * Searches the default button in the layout.
	 * 
	 * @param pComponent the start component.
	 * @return the default button if exists.
	 */
	private MouseOverButton searchDefaultButton(Component pComponent)
	{
		if (pComponent instanceof MouseOverButton && ((MouseOverButton) pComponent).isVisible() && ((MouseOverButton) pComponent).isDefault())
		{
			return (MouseOverButton) pComponent;
		}
		else if (pComponent instanceof HasComponents)
		{
			Iterator<Component> iterator = ((HasComponents) pComponent).iterator();
			
			while (iterator.hasNext())
			{
				MouseOverButton button = searchDefaultButton(iterator.next());
				
				if (button != null)
				{
					return button;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Searches the button with given shortcut in the layout.
	 * 
	 * @param pComponent Start component in the recursion.
	 * @param pShortcut shortcut.
	 * @return Button if found.
	 */
	private MouseOverButton searchButtonWithShortcut(Component pComponent, ShortcutAction pShortcut)
	{
		if (pComponent instanceof MouseOverButton && ((MouseOverButton) pComponent).isVisible() && matchButtonShortcut((MouseOverButton)pComponent, pShortcut))
		{
			return (MouseOverButton) pComponent;
		}
		else if (pComponent instanceof HasComponents)
		{
			Iterator<Component> iterator = ((HasComponents) pComponent).iterator();
			
			while (iterator.hasNext())
			{
				MouseOverButton button = searchButtonWithShortcut(iterator.next(), pShortcut);
				
				if (button != null)
				{
					return button;
				}
			}
		}
		
		return null;
	}

	/**
	 * Evaluates if button and current action has the same shortcut. Shortcuts are considered identical if both keyCode and all modifiers match.
	 * 
	 * @param pButton Verified button.
	 * @param pShortcutAction Current action with shortcut definition - key and modifiers.
	 * @return true if shortcut matches.
	 */
	private boolean matchButtonShortcut(MouseOverButton pButton, ShortcutAction pShortcutAction)
	{
		if (pButton.getClickShortcut() != null && pShortcutAction != null && pButton.getClickShortcut().getKeyCode() == pShortcutAction.getKeyCode())
		{
			int[] actionModifiers = pShortcutAction.getModifiers();
			int[] buttonModifiers = pButton.getClickShortcut().getModifiers();
			
			Arrays.sort(actionModifiers);
			Arrays.sort(buttonModifiers);
			return Arrays.equals(actionModifiers, buttonModifiers);
		}
		return false;
	}
	
}	// ShortcutHandler
