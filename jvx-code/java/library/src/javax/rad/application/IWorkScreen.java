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
 * 28.01.2009 - [JR] - getApplication returns IWorkScreenApplication instead of IApplication
 * 20.09.2013 - [JR] - #798: notifyActivate defined
 * 17.10.2013 - [JR] - #842: isParameterSet, removeParameter introduced
 */
package javax.rad.application;

/**
 * Platform and technology independent work screen definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IWorkScreen extends IContent,
									 IMessageConstants,
                                     IDataConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Notifies the IWorkScreen, that it will be "visible again".
	 */
	public void notifyActivate();
	
	/**
	 * Gets the main application of this work-screen.
	 * 
	 * @return the main application base
	 */
	public IWorkScreenApplication getApplication();
	
	/**
	 * Returns the modal state of this workscreen.
	 * 
	 * @return <code>true</code> if this workscreen should be modal, otherwise <code>false</code>
	 */
	public boolean isModal();
	
	/**
	 * Sets whether this workscreen should be modal.
	 * 
	 * @param pModal <code>true</code> if this workscreen should be modal, otherwise <code>false</code>
	 */
	public void setModal(boolean pModal);
	
	/**
	 * Sets additional parameters for the work-screen. This method should be used for additional configuration
	 * steps.
	 * 
	 * @param pName the parameter name
	 * @param pValue the parameter 
	 * @return the previous value for the parameter name or <code>null</code> if the parameter was not set before
	 */
	public Object setParameter(String pName, Object pValue);
	
	/**
	 * Gets the value of an additional parameter from the work-screen.
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
	
}	// IWorkScreen
