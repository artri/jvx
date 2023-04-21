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
 * 11.10.2008 - [JR] - creation
 * 07.11.2008 - [JR] - used TabEvent as parameter for methods
 * 11.12.2008 - [JR] - selectTab, deselectTab defined
 * 11.08.2009 - [JR] - moveTab defined
 */
package com.sibvisions.rad.ui.swing.ext.event;


/**
 * The listener interface for receiving "interesting" events 
 * on a tabbed pane.
 * 
 * @author René Jahn
 */
public interface ITabListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invoked when a tab will be closed (before it will be closed).
	 *  
	 * @param pEvent the event
	 * @throws Exception if the close operation is not possible
	 */
	public void closeTab(TabEvent pEvent) throws Exception;
	
	/**
	 * Invoked when a tab will be deselected.
	 * 
	 * @param pEvent the event
	 * @throws Exception when the tab should not be deselected
	 */
	public void deselectTab(TabEvent pEvent) throws Exception;
	
	
	/**
	 * Invoked when a tab is the curent selected tab.
	 * 
	 * @param pEvent the event
	 */
	public void selectTab(TabEvent pEvent);
	
	/**
	 * Invoked when a tab was moved from one index to another index.
	 * 
	 * @param pEvent the event
	 */
	public void moveTab(TabEvent pEvent);
			
}	// ITabListener
