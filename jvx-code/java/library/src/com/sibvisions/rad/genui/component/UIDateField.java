/*
 * Copyright 2013 SIB Visions GmbH
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
 * 28.10.2022 - [HM] - creation
 */
package com.sibvisions.rad.genui.component;

import java.sql.Timestamp;

import javax.rad.genui.celleditor.UIDateCellEditor;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.TimestampDataType;

/**
 * The {@link UIDateField} is a stand-alone date editor which acts like a
 * {@link UIEditor} but does not need to be bound against an
 * {@link javax.rad.model.IDataRow}.
 * 
 * @author Martin Handsteiner
 */
public class UIDateField extends AbstractUIField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The used datatype. */
    private transient UIDateCellEditor cellEditor;
    
	/** The used datatype. */
	private transient TimestampDataType dataType;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UIDateField}.
	 */
	public UIDateField()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	protected IDataType createDataType()
	{
	    cellEditor = new UIDateCellEditor();
	        
	    dataType = new TimestampDataType();
	    dataType.setCellEditor(cellEditor);
	    dataType.setDateFormat(cellEditor.getDateFormat());
	    
	    return dataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the value as <code>BigDecimal</code>.
     * 
     * @return the value as <code>BigDecimal</code>.
     */
    public Timestamp getValueAsTimestamp()
    {
        return (Timestamp)getValue();
    }
    
    /**
     * Gets the date format.
     * 
     * @return the date format.
     */
    public String getDateFormat()
    {
        return dataType.getDateFormat();
    }
    
    /**
     * Sets the date format.
     * 
     * @param pDateFormat the date format.
     */
    public void setDateFormat(String pDateFormat)
    {
        dataType.setDateFormat(pDateFormat);
        cellEditor.setDateFormat(pDateFormat);
    }
    
}	// UIDateField
