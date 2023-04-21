/*
 * Copyright 2015 SIB Visions GmbH
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
 * 15.01.2015 - [JR] - creation
 */
package javax.rad.genui.event;

/**
 * The <code>IUIResourceListener</code> is used to inform about changes in the 
 * {@link javax.rad.genui.UIResource}.
 * 
 * @see javax.rad.model.IDataSource
 * 
 * @author René Jahn
 */
public interface IUIResourceListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notifies that parameter was changed. 
	 * 
	 * @param pEvent gives information about the changes
	 * @throws Throwable if there is an error.
	 */
	public void resourceChanged(ResourceEvent pEvent) throws Throwable;

} 	// IUIResourceListener
