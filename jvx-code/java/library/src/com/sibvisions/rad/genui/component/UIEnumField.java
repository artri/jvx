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

import javax.rad.genui.control.UIEditor;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.ObjectDataType;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;

/**
 * The {@link UIEnumField} is a stand-alone number editor which acts like a
 * {@link UIEditor} but does not need to be bound against an
 * {@link javax.rad.model.IDataRow}.
 * 
 * @author Martin Handsteiner
 */
public class UIEnumField extends AbstractUIField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The used datatype. */
    private transient UIEnumCellEditor cellEditor;
    
	/** The used datatype. */
	private transient ObjectDataType dataType;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UIEnumField}.
	 */
	public UIEnumField()
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
	    cellEditor = new UIEnumCellEditor();
	    
	    dataType = new ObjectDataType();
	    dataType.setCellEditor(cellEditor);
	    
	    return dataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets all allowed values.
     * 
     * @return all allowed values.
     */
    public Object[] getAllowedValues()
    {
        return cellEditor.getAllowedValues();
    }

    /**
     * Sets all allowed values.
     * 
     * @param pAllowedValues all allowed values.
     */
    public void setAllowedValues(Object[] pAllowedValues)
    {
        cellEditor.setAllowedValues(pAllowedValues);
    }
    
    /**
     * Gets all display values.
     * 
     * @return all display values.
     */
    public String[] getDisplayValues()
    {
        return cellEditor.getDisplayValues();
    }
    
    /**
     * Sets all display values.
     * 
     * @param pDisplayValues all display values.
     */
    public void setDisplayValues(String[] pDisplayValues)
    {
        cellEditor.setDisplayValues(pDisplayValues);
    }
    
    /**
     * Sets validation enabled.
     * 
     * @param pEnabled <code>true</code> to validate the value, <code>false</code> otherwise
     */
    public void setValidationEnabled(boolean pEnabled)
    {
        cellEditor.setValidationEnabled(pEnabled);
    }
    
    /**
     * Returns whether validation is enabled.
     * 
     * @return <code>true</code> if value will be validated, <code>false</code> otherwise
     */
    public boolean isValidationEnabled()
    {
        return cellEditor.isValidationEnabled();
    }
    
}	// UIEnumField
