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
 * 12.05.2010 - [JR] - opener: generic type definition
 */
package javax.rad.application;

import javax.rad.ui.IComponent;


/**
 * Platform and technology independent windowed content definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IContent extends IComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Notifies the content, that it is now positioned and showing.
	 */
	public void notifyVisible();

	/**
	 * Notifies the content, that it will be destroyed.
	 * 
	 * @throws SecurityException to prevent from being destroyed.
	 */
	public void notifyDestroy();	
	
	/**
	 * Gets whether the content is already destroyed.
	 * 
	 * @return <code>true</code> if content is destroyed, <code>false</code> otherwise
	 */
	public boolean isDestroyed();
	
	/**
	 * Sets the opener of the content.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener component or <code>null</code> if the component is 
	 *                unknown
	 */
	public <OP> void setOpener(OP pOpener);
	
	/**
	 * Gets the opener of the content.
	 * 
	 * @param <OP> the opener type
	 * @return the opener component or <code>null</code> if the opener is unknown
	 */
	public <OP> OP getOpener();

}	// IContent
