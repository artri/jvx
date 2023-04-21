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
 * 20.11.2008 - [HM] - creation
 * 25.11.2008 - [JR] - defined application and content pane
 * 17.02.2009 - [JR] - set/getCursor defined
 * 19.02.2009 - [JR] - removed set/getCursor -> only available in IComponent
 * 20.02.2009 - [JR] - added 'showMessage' from IWorkScreenApplication
 *                   - extend IMessageConstants
 * 22.04.2009 - [JR] - removed log handling          
 * 12.06.2010 - [JR] - show... now returns IContent 
 *                   - show... generic opener
 */
package javax.rad.application;

import javax.rad.ui.IContainer;

/**
 * Platform and technology independent application definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IApplication extends IContent, 
                                      IMessageConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns the platform independent launcher which is associated with
     * this factory.
     * 
     * @return the associated launcher
     */
    public ILauncher getLauncher();
    
	/** 
	 * Shows a message.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener/parent component which wants to display the message
	 * @param pIconType the icon type {@link #MESSAGE_ICON_INFO}, {@link #MESSAGE_ICON_WARNING}, {@link #MESSAGE_ICON_ERROR}
	 * @param pButtonType the button type {@link #MESSAGE_BUTTON_OK_CANCEL}, {@link #MESSAGE_BUTTON_YES_NO}, {@link #MESSAGE_BUTTON_OK}
	 * @param pMessage the message
	 * @param pOkAction the action name to call when ok or yes was clicked
	 * @param pCancelAction the action name to call when cancel or close was clicked
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized         
	 */
	public <OP> IContent showMessage(OP pOpener, 
   				 				     int pIconType, 
								     int pButtonType, 
								     String pMessage, 
								     String pOkAction, 
								     String pCancelAction) throws Throwable;

	/**
	 * Gets the standard application pane where the content will
	 * be added.
	 * 
	 * @return the application pane
	 */
	public IContainer getApplicationPane();
	
	/**
	 * Gets the content pane for the application. The content pane will be used
	 * for the application UI.
	 * 
	 * @return the content pane
	 */
	public IContainer getContentPane();
	
	/**
	 * Sets additional parameters for the application. This method should be used for additional configuration
	 * steps.
	 * 
	 * @param pName the parameter name
	 * @param pValue the parameter 
	 * @return the previous value for the parameter name or <code>null</code> if the parameter was not set before
	 */
	public Object setParameter(String pName, Object pValue);
	
	/**
	 * Gets the value of an additional parameter from the application.
	 * 
	 * @param pName the parameter name
	 * @return the parameter
	 */
	public Object getParameter(String pName);
	
	/**
	 * Gets whether the given parameter is set.
	 * 
	 * @param pName the parameter name
	 * @return <code>true</code> if the parameter is set, <code>false</code> otherwise
	 */
	public boolean isParameterSet(String pName);
	
	/**
	 * Removes the parameter from the parameter list.
	 * 
	 * @param pName the parameter name
	 * @return the old object
	 */
	public Object removeParameter(String pName);	

}	// IApplication
