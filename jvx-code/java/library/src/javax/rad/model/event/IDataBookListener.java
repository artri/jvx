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
 * 01.10.2008 - [RH] - creation
 */
package javax.rad.model.event;

import java.io.Serializable;

/**
 * The <code>IDataBookListener</code> informs about changes in an {@link javax.rad.model.IDataBook}.
 * 
 * @see javax.rad.model.IDataBook
 * 
 * @author Roland Hörmann
 */
public interface IDataBookListener extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notifies that an {@link javax.rad.model.IDataBook} is changed. 
	 * 
	 * @param pDataBookEvent gives information about the changed {@link javax.rad.model.IDataBook}.
	 * @throws Throwable if an exception occurs.
	 */
	public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable;	
	
} 	// IDataBookListener
