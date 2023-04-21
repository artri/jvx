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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.celleditor;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import com.sibvisions.rad.ui.celleditor.AbstractNumberCellEditor;
import com.sibvisions.rad.ui.web.impl.WebFactory;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Web server implementation of {@link javax.rad.ui.celleditor.INumberCellEditor}.
 * 
 * @author Martin Handsteiner
 */
public class WebNumberCellEditor extends AbstractNumberCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WebNumberCellEditor</code>.
	 *
	 * @see javax.rad.ui.celleditor.INumberCellEditor
	 */
	public WebNumberCellEditor()
	{
		this(null);
	}
	
	/**
     * Creates a new instance of <code>WebNumberCellEditor</code> with the given number format.
     * 
     * @param pNumberFormat the number format.
     */
    public WebNumberCellEditor(String pNumberFormat)
    {
        super(pNumberFormat);
    }
    

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
	{
        try
        {
            if (!Number.class.isAssignableFrom(pDataRow.getRowDefinition().getColumnDefinition(pColumnName).getDataType().getTypeClass()))
            {
                LoggerFactory.getInstance(AbstractNumberCellEditor.class).error("NumberCellEditor is used for a column, that does not store numbers!");
            }
        }
        catch (ModelException me)
        {
            //nothing to be done
        }

		return new DefaultCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName()
	{
		return WebFactory.getClassName(this);
	}	
	
}	// WebNumberCellEditor
