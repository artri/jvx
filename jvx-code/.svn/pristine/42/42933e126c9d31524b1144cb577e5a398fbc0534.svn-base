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

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;

/**
 * The {@link IRowCalculator} is used for calculating/providing data dynamically
 * at runtime, without firing associated events (for example
 * {@link IDataBook#eventValuesChanged()}).
 * 
 * @see IDataBook
 * 
 * @author Martin Handsteiner
 */
public interface IRowCalculator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Is called when a new row is loaded, inserted or updated.
	 * <p>
	 * The values in the given {@link IDataRow} can be changed without that
	 * associated events (for example {@link IDataBook#eventValuesChanged()} is
	 * fired.
	 * 
	 * @param pDataBook the {@link IDataBook}.
	 * @param pDataPage the {@link IDataPage}.
	 * @param pDataRow the {@link IDataRow} into which the new data should be
	 *            set.
	 * @throws Throwable if an error occurs.
	 */
	public void calculateRow(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow) throws Throwable;
	
} 	// IRowCalculator
