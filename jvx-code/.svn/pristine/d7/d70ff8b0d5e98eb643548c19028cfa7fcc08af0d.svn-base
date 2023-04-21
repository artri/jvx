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
 * 23.01.2009 - [RH] - throws ModelException added
 */
package javax.rad.model.event;

import java.io.Serializable;

/**
 * The <code>IDataRowListener</code> is used to inform about changes in the 
 * {@link javax.rad.model.IDataBook}, via {@link javax.rad.model.IDataBook#setValue(String, Object)} or 
 * {@link javax.rad.model.IDataBook#setValues(String[], Object[])}.
 * 
 * @see javax.rad.model.IDataRow
 * 
 * @author Roland Hörmann
 */
public interface IDataRowListener extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notifies that values on an {@link javax.rad.model.IDataBook} are changed. 
	 * 
	 * @param pDataRowEvent gives information about the changed {@link javax.rad.model.IDataBook}.
	 * @throws Throwable if an exception occurs.
	 */
	public void valuesChanged(DataRowEvent pDataRowEvent) throws Throwable;

} 	// IDataRowListener
