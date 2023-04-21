/*
 * Copyright 2018 SIB Visions GmbH
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
 * 22.10.2018 - [HM] - creation
 */
package javax.rad.application.genui.responsive;

/**
 * The <code>IResponsiveResource</code> is standard interface for all responsive resources.
 * 
 * @author Martin Handsteiner
 */
public interface IResponsiveResource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets whether we're in responsive mode.
	 * 
	 * @return <code>true</code> if responsive mode, <code>false</code> otherwise.
	 */
	public Boolean isResponsive();

	/**
	 * Gets the current responsive mode.
	 * It the responsive mode is set to null, the global setting of application or factory is used.
	 * 
	 * @param pResponsive the current display mode.
	 */
	public void setResponsive(Boolean pResponsive);

	/**
	 * Gets whether the responsive mode is set on this component.
	 * 
	 * @return whether the responsive mode is set on this component.
	 */
	public boolean isResponsiveSet();
	
	/**
	 * Notification about responsive/layout mode changed.
	 * 
	 * @param pOld the old mode
	 * @param pNew the new mode
	 */
	public void responsiveModeChanged(ResponsiveUtil.LayoutMode pOld, ResponsiveUtil.LayoutMode pNew);

}   // IResponsiveResource
