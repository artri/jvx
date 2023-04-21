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

import java.util.Date;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import com.sibvisions.rad.ui.celleditor.AbstractDateCellEditor;
import com.sibvisions.rad.ui.web.impl.WebFactory;
import com.sibvisions.util.log.LoggerFactory;

/**
 * Web server implementation of {@link javax.rad.ui.celleditor.IDateCellEditor}.
 * 
 * @author Martin Handsteiner
 */
public class WebDateCellEditor extends AbstractDateCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WebDateCellEditor</code>.
	 *
	 * @see javax.rad.ui.celleditor.IDateCellEditor
	 */
	public WebDateCellEditor()
	{
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
            if (!Date.class.isAssignableFrom(pDataRow.getRowDefinition().getColumnDefinition(pColumnName).getDataType().getTypeClass()))
            {
                LoggerFactory.getInstance(AbstractDateCellEditor.class).error("DateCellEditor is used for a column, that does not store dates!");
            }
        }
        catch (ModelException me)
        {
            //nothing to be done
        }

		return new DefaultCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDateFormat()
	{
		String format = super.getDateFormat();

		boolean bDate = format.contains("y") || format.contains("M") || format.contains("d") || format.contains("D");
		boolean bTime = format.contains("h") || format.contains("H") || format.contains("k") || format.contains("K") 
				        || format.contains("m") || format.contains("s");
		boolean bHour = format.contains("h") || format.contains("H") || format.contains("k") || format.contains("K");
		boolean bMinute = format.contains("m");
		boolean bSecond = format.contains("s");
		boolean bAmPm = format.contains("a");
		
		setProperty("isDateEditor", Boolean.valueOf(bDate));
		setProperty("isTimeEditor", Boolean.valueOf(bTime));
		setProperty("isHourEditor", Boolean.valueOf(bHour));
		setProperty("isMinuteEditor", Boolean.valueOf(bMinute));
		setProperty("isSecondEditor", Boolean.valueOf(bSecond));
		setProperty("isAmPmEditor", Boolean.valueOf(bAmPm));
		
		return format;
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
	
}	// WebDateCellEditor
