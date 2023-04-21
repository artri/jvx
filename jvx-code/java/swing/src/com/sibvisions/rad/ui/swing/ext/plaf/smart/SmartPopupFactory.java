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
 * 01.10.2008 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 * The <code>SmartPopupFactory</code> extends the <code>PopupFactory</code>
 * and handles the creation of popups for the Smart/LF.
 * 
 * @author René Jahn
 */
public final class SmartPopupFactory extends PopupFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the singleton instance of <code>SmartPopupFactory</code>. */
    private static SmartPopupFactory instance = null;

    /** the original factory before installing the <code>SmartPopupFactory</code>. */
    private static PopupFactory popfOrig;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>SmartPopupFactory</code> class 
	 * is a utility class.
	 */
    private SmartPopupFactory()
    {
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a <code>Popup</code> for the Component <code>pOwner</code>
     * containing the Component <code>pContents</code>. <code>pOwner</code>
     * is used to determine which <code>Window</code> the new
     * <code>Popup</code> will parent the <code>Component</code> the
     * <code>Popup</code> creates to. A null <code>pOwner</code> implies there
     * is no valid parent. <code>pContents</code> and
     * <code>pY</code> specify the preferred initial location to place
     * the <code>Popup</code> at. Based on screen size, or other paramaters,
     * the <code>Popup</code> may not display at <code>pX</code> and
     * <code>pY</code>.

     * @param pOwner    Component mouse coordinates are relative to, may be null
     * @param pContents Contents of the Popup
     * @param pX        Initial x screen coordinate
     * @param pY        Initial y screen coordinate
     * @exception IllegalArgumentException if contents is null
     * @return Popup containing Contents
     */
    @Override
    public Popup getPopup(Component pOwner, Component pContents, int pX, int pY)
    {
        Popup popup;

        //Bei jdk 1.5 wird das InternalFrame Menü abhängig vom Button angezeigt!
        //Ab 1.6 wird das InternalFrame verwendet (was auch die saubere Variante ist)
        if (pOwner instanceof AbstractButton && SmartLookAndFeel.isNorthPaneMenuButton(pOwner.getName()))
        {
        	//2 px sind bei den Insets von 4 passend!
        	popup = super.getPopup(pOwner, pContents, pX + 2, pY + 2);        	
        }
        else
        {
        	popup = super.getPopup(pOwner, pContents, pX, pY);        	
        }
        
        return popup;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets the <code>SmartPopupFactory</code> as default factory for popups and
     * stores the original factory for uninstall. 
     */
    public static void install()
    {
    	if (instance == null)
    	{
    		instance = new SmartPopupFactory();
    		
            popfOrig = getSharedInstance();
            setSharedInstance(instance);
    	}
    }

    /**
     * Sets the original <code>PopupFactory</code> as default factory for popups.
     */
    public static void uninstall()
    {
        if (instance != null)
        {
            setSharedInstance(popfOrig);
            
            popfOrig = null;
            instance = null;
        }
    }

}	// SmartPopupFactory
