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
 * 18.11.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.impl;

import javax.rad.application.ILauncher;

/**
 * The <code>ILookAndFeelConfiguration</code> should be used to set custom LaF properties
 * before the LaF will be set via {@link SwingFactory}.
 * 
 * @author René Jahn
 */
public interface ILookAndFeelConfiguration
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the LaF default properties.
	 * 
	 * @param pLauncher the launcher
	 * @param pLaFClassName the look and feel class name
	 */
	public void setDefaults(ILauncher pLauncher, String pLaFClassName);
	
}	// ILookAndFeelConfiguration
