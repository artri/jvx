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
package javax.rad.model.condition;

import javax.rad.model.IDataRow;

/**
 * The <code>ICondition</code> specifies the interface for all <code>Condition</code>'s.
 * It also supports combine <code>ICondition</code>'s to handle grouped logical operations.
 * like "a AND (b or c)".
 * 
 * Example:
 * 
 * <pre>
 * ... in Memory
 * ICondition filter = new Equals("ID", 15).and(new Equals("NAME", "a").or(new Equals("NAME", "b")));
 * System.out.println(new SQLSupport(filter) + "= " + filter.isFulfilled(dbMemDataBook.getRow(0)));
 *
 * ... with a RemoteDataBook e.g. for Database
 * DataRow drFilter = dbStorageDataBook.createEmptyRow();
 * drFilter.setDataCellValue("ADRESSE", "z*");
 * drFilter.setDataCellValue("PLZ", "1010");
 * 
 * ICondtion cFilter = new LikeIgnoreCase(drFilter, "ADRESSE").and(new Equals(drFilter, "PLZ")); 
 * dbStorageDataBook.setFilter(filter);
 * </pre> 
 * 
 * @see javax.rad.model.IDataBook
 * @see javax.rad.model.IDataRow
 * 
 * @author Roland Hörmann, Martin Handsteiner
 */
public interface ICondition extends Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns true, if the <code>ICondition</code> is fulfilled.
	 * 
	 * @param pDataRow
	 * 			the <code>IDataRow</code> to use for the check of this <code>ICondition</code>.
	 * 
	 * @return true, if the <code>ICondition</code> is fulfilled.
	 */
	public boolean isFulfilled(IDataRow pDataRow);
	
	/**
	 * It connects with an logic AND the specified <code>ICondition</code>.
	 * 
	 * @param pCondition
	 * 			the <code>ICondition</code> to add
	 * @return this <code>ICondition</code> itself as result.
	 */
	public And and(ICondition pCondition);
	
	/**
	 * It connects with an logic OR the specified <code>ICondition</code>.
     *
	 * @param pCondition
	 * 			the <code>ICondition</code> to add
	 * @return this <code>ICondition</code> itself as result.
	 */
	public Or or(ICondition pCondition);
	
	/**
	 * Return a clone of the specific ICondition.
	 * 
	 * @return a clone of the specific ICondition.
	 */
	public ICondition clone();

} 	// ICondition
