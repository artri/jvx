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
 * 22.11.2008 - [JR] - creation
 * 04.12.2008 - [JR] - showMessage, openContent, openWorkScreen now throws Throwable
 * 17.02.2009 - [JR] - openContent: add opener parameter
 * 20.02.2009 - [JR] - moved 'showMessage' to IApplication 
 */
package javax.rad.application;

import java.util.Map;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent definition for an application with work-screens. 
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public interface IWorkScreenApplication extends IApplication
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The enum for modal content or workscreens. */
	public enum Modality
	{
		/** modal mode. */
		Modal,
		/** not modal mode. */
		NotModal,
		/** use the modal option from the work screen. */
		WorkScreen
	};
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Opens a work screen.
	 * 
	 * @param pClassName the class name of the work screen
	 * @param pModality the modality mode
	 * @param pParameter the work screen parameters
	 * @return the work screen
	 * @throws Throwable if the content could not be initialized
	 */
	public IWorkScreen openWorkScreen(String pClassName, Modality pModality, Map<String, Object> pParameter) throws Throwable;

	/**
	 * Gets all open work screens.
	 * 
	 * @return the work screens.
	 */
	public IWorkScreen[] getWorkScreens();
	
	/**
	 * Gets all roles of the current user.
	 * 
	 * @return the roles.
	 */
	public String[] getRoles();
	
	/**
	 * Gets true, of the current user has the given role.
	 * 
	 * @param pRoleName the name of the role
	 * @return <code>true</code> if the user has the specified role
	 */
	public boolean hasRole(String pRoleName);
	
	/**
	 * Opens a content in an internal frame. It can be specified, if it should be modal or not.
	 * 
	 * @param pOpener the component which opened the content 
	 * @param pTitle the title of the internal frame.
	 * @param pModal the modal flag.
	 * @param pContent the content that will be shown
	 * @throws Throwable if the content could not be initialized
	 */
	public void openContent(IComponent pOpener, String pTitle, boolean pModal, IContent pContent) throws Throwable;
	
	/**
	 * Opens content in an internal frame. It can be specified, if it should be modal or not.
	 * 
	 * @param pOpener the component which opened the content 
	 * @param pTitle the title of the internal frame.
	 * @param pModal the modal flag.
	 * @param pClassName the class name of the content.
	 * @param pParameter the constructor parameter of the content.
	 * @return the content that is shown.
	 * @throws Throwable if the content could not be initialized
	 */
	public IContent openContent(IComponent pOpener, String pTitle, boolean pModal, String pClassName, Object... pParameter) throws Throwable;
	
	/**
	 * Gets all open contents.
	 * 
	 * @return the contents.
	 */
	public IContent[] getContents();
	
	/**
	 * Closes the given content.
	 * 
	 * @param pContent the content to close.
	 */
	public void close(IContent pContent);
		
}	// IApplication
