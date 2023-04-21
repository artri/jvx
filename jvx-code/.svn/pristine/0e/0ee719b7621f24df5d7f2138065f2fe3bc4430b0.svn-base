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
 * 17.11.2008 - [HM] - creation
 * 10.12.2008 - [JR] - moved accelerator methods to UIButton
 * 19.10.2009 - [JR] - extends AbstractUIActionComponent instead of AbstractUIButton
 */
package javax.rad.genui.menu;

import javax.rad.genui.component.AbstractUIActionComponent;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuItem;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent MenuItem.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <C> instance of IMenuItem
 */
public abstract class AbstractUIMenuItem<C extends IMenuItem> extends AbstractUIActionComponent<C> 
                                                              implements IMenuItem
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIMenuItem</code>.
     *
     * @param pMenuItem the IMenuItem.
     * @see IMenuItem
     */
	protected AbstractUIMenuItem(C pMenuItem)
	{
		super(pMenuItem);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the menu with the given menu text.
     * @param pParent the parent to search.
     * @param pMenuText the menu text.
     * @return the menu.
     */
    public static IMenu findMenu(IContainer pParent, String pMenuText)
    {
		for (int i = 0, count = pParent.getComponentCount(); i < count; i++)
    	{
    		IComponent component = pParent.getComponent(i);
    		
    		if (component instanceof IMenu && pMenuText.equals(((IMenu)component).getText()))
    		{
    			IMenu menu = (IMenu)component;
    			
    			if (pMenuText.equals(menu.getText()))
    			{
    				return menu;
    			}
    		}
    	}
		
		return null;
    }
    
    /**
     * Gets the specific menu from the given parent.
     * The sub menus are seperated by /.
     * 
     * @param pParent the parent.
     * @param pGroup the group.
     * @return the menu.
     */
    public static IContainer getMenu(IContainer pParent, String pGroup)
    {
    	if (StringUtil.isEmpty(pGroup))
    	{
    		return pParent;
    	}
    	else
    	{
    		if (pGroup.startsWith("/"))
    		{
    			pGroup = pGroup.substring(1);
    		}
    		int index = pGroup.indexOf("/");
    		String menuText;
    	    if (index < 0)
    		{
    	    	menuText = pGroup;
    	    	pGroup = null;
    		}
    	    else
    	    {
    	    	menuText = pGroup.substring(0, index);
    	    	pGroup = pGroup.substring(index + 1);
    	    }
    		
    	    IContainer menu = findMenu(pParent, menuText);
    	    if (menu == null)
    	    {
    	    	menu = new UIMenu(menuText);
        		
        		pParent.add(menu);
    	    }
    	    
    	    return getMenu(menu, pGroup);
    	}
    }

}	// UIMenuItem
