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
 * 16.05.2010 - [JR] - creation
 * 11.02.2011 - [JR] - cell formatter implemented
 */
package com.sibvisions.rad.ui.web.impl.control;

import javax.rad.ui.control.ICellFormatable;
import javax.rad.ui.control.ICellFormatter;

import com.sibvisions.rad.ui.web.impl.WebComponent;

/**
 * The <code>AbstractCellFormatable</code> is a {@link WebComponent} which enables cell formatting.
 *  
 * @author René Jahn
 */
public abstract class AbstractCellFormatable extends WebComponent
										     implements ICellFormatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The current CellFormatter. */
	private ICellFormatter cellFormatter = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the current cell formatter.
	 * 
	 * @return the cell formatter
	 */
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter;
	}
	
	/**
	 * Sets the cell formatter.
	 * 
	 * @param pFormatter the cell formatter.
	 */
	public void setCellFormatter(ICellFormatter pFormatter)
	{
		cellFormatter = pFormatter;
	}
	
}	// AbstractCellFormatable
