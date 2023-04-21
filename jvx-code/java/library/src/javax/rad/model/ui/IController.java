/*
 * Copyright 2014 SIB Visions GmbH
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
 * 20.02.2014 - [HM] - creation
 * 24.06.2014 - [JR] - #1078: set/getControllerProperty introduced
 */
package javax.rad.model.ui;

/**
 * The <code>IController</code> to allow external control for {@link IControl}s.
 * 
 * @author Martin Handsteiner
 */
public interface IController
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the default name of the search-visible property. */
    public static final String PROPERTY_SEARCH_VISIBLE = "PROPERTY_SEARCH_VISIBLE";
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the active <code>IControllable</code> that should be controlled by this <code>IController</code>.
	 * 
	 * @return the active <code>IControllable</code>
	 */
	public IControllable getActiveControllable();
	
	/**
	 * Sets the active <code>IControllable</code> that should be controlled by this <code>IController</code>.
	 * 
	 * @param pActiveControllable the current <code>IControllable</code>
	 */
	public void setActiveControllable(IControllable pActiveControllable);
	
	/**
	 * Sets a property for the controller.
	 * 
	 * @param pName the property name
	 * @param pValue the value. If the value is <code>null</code> the property will be removed
	 */
	public void setControllerProperty(String pName, Object pValue);
	
	/**
	 * Gets the current value for a controller property.
	 * 
	 * @param pName the property name
	 * @return the value or <code>null</code> if the property was not found
	 */
	public Object getControllerProperty(String pName);

}	// IController
