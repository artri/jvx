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
 * 14.11.2008 - [HM] - creation
 */
package javax.rad.genui;

import javax.rad.ui.ICursor;

/**
 * Platform and technology independent Cursor.
 * 
 * @author Martin Handsteiner
 */
public class UICursor extends UIResource<ICursor> 
                      implements ICursor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UICursor</code>.
     *
     * @param pCursor the ICursor.
     * @see ICursor
     */
	protected UICursor(ICursor pCursor)
	{
		super(pCursor);
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getType()
    {
    	return uiResource.getType();
    }

	/**
	 * {@inheritDoc}
	 */
    public String getName()
    {
    	return uiResource.getName();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Gets a <code>ICursor</code> object with the specified type.
     * 
     * @param pType the type of cursor
     * @return the <code>ICursor</code>
     * @see ICursor
     */
    public static ICursor getPredefinedCursor(int pType)
	{
		return UIFactoryManager.getFactory().getPredefinedCursor(pType);
	}
    
    /**
     * Returns a system-specific custom <code>ICursor</code> object matching the 
     * specified name.  Cursor names are, for example: "Invalid.16x16"
     * 
     * @param pCursorName a string describing the desired system-specific custom cursor 
     * @return the system specific custom cursor named
     * @see ICursor
     */
    public static ICursor getSystemCustomCursor(String pCursorName)
	{
    	return UIFactoryManager.getFactory().getSystemCustomCursor(pCursorName);
	}

}	// UICursor
