/*
 * Copyright 2023 SIB Visions GmbH
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
 * 18.05.2023 - [JR] - creation
 */
package javax.rad.model.ui;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;

/**
 * The <code>IDataRowControl</code> is an IControl that binds an {@link IDataRow}.
 * 
 * @author Ren� Jahn
 */
public interface IDataRowControl extends IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the DataRow displayed by this control.
     *
     * @return the DataRow.
     * @see #setDataRow
     */
    public IDataRow getDataRow();

    /**
     * Sets the DataRow displayed by this control.
     * 
	 * @param pDataRow the DataRow
     * @throws ModelException if the column name is invalid
     * @see #getDataRow
     */
    public void setDataRow(IDataRow pDataRow) throws ModelException;
    
} 	// IDataRowControl
