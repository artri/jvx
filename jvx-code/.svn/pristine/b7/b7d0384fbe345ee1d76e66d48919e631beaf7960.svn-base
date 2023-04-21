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
 * 08.10.2008 - [HM] - creation
 * 08.12.2008 - [JR] - setDefaultButton implemented
 * 11.10.2009 - [JR] - setBorderOnMouseEntered: set opaque property
 * 21.02.2011 - [JR] - #292: mouseEntered checks if button is enabled 
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * The <code>JVxButton</code> is a <code>JButton</code>
 * extension.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JButton
 */
public class JVxPopupMenu extends JPopupMenu
                       implements PopupMenuListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the components that does not cancel the popup. */
    private Map<JComponent, Object> doNotCancelPopupComponents = new HashMap<JComponent, Object>();
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Constructs a <code>JVxPopupMenu</code> without an "invoker".
     */
    public JVxPopupMenu() 
    {
        this(null);
    }

    /**
     * Constructs a <code>JPopupMenu</code> with the specified title.
     *
     * @param pLabel  the string that a UI may use to display as a title 
     * for the popup menu.
     */
    public JVxPopupMenu(String pLabel) 
    {
        super(pLabel);
        
        addPopupMenuListener(this);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void popupMenuWillBecomeVisible(PopupMenuEvent pEvent)
    {
        Container parent = getInvoker().getParent();
        while (parent.getParent() != null)
        {
            parent = parent.getParent();
        }
        removeDoNotCancelPopupComponents(parent);
    }

    /**
     * {@inheritDoc}
     */
    public void popupMenuWillBecomeInvisible(PopupMenuEvent pEvent)
    {
        addDoNotCancelPopupComponents();
    }

    /**
     * {@inheritDoc}
     */
    public void popupMenuCanceled(PopupMenuEvent pEvent)
    {
        
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * True, if pComp is a child of pParent.
     * 
     * @param pParent the parent.
     * @param pComp the child.
     * @return True, if pComp is a child of pParent.
     */
    private boolean isChild(Component pParent, Component pComp)
    {
        while (pComp != null)
        {
            if (pComp == pParent)
            {
                return true;
            }
            pComp = pComp.getParent();
        }
        return false;
    }
    
    /**
     * Removes client property doNotCancelPopup.
     * 
     * @param pParent component to test
     */
    private void removeDoNotCancelPopupComponents(Component pParent)
    {
        if (pParent instanceof Container)
        {
            if (pParent instanceof JComponent)
            {
                JComponent comp = (JComponent)pParent;
                Object property = comp.getClientProperty("doNotCancelPopup");
                if (property != null)
                {
                    if (!isChild(getInvoker(), comp))
                    {
                        doNotCancelPopupComponents.put(comp, property);
                        comp.putClientProperty("doNotCancelPopup", null);
                    }
                }
            }
            Container cont = (Container)pParent;
            for (int i = 0; i < cont.getComponentCount(); i++)
            {
                removeDoNotCancelPopupComponents(cont.getComponent(i));
            }
        }
    }

    /**
     * Adds client property doNotCancelPopup.
     */
    private void addDoNotCancelPopupComponents()
    {
        for (Map.Entry<JComponent, Object> entry : doNotCancelPopupComponents.entrySet())
        {
            entry.getKey().putClientProperty("doNotCancelPopup", entry.getValue());
        }
        doNotCancelPopupComponents.clear();
    }
    

}	// JVxPopupMenu
