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

import javax.rad.genui.celleditor.UITextCellEditor;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;

/**
 * The {@link UIStringField} is a stand-alone editor which acts like a
 * {@link UIEditor} but does not need to be bound against an
 * {@link javax.rad.model.IDataRow}.
 * 
 * @author Martin Handsteiner
 */
public class UIStringField extends AbstractUIField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The used datatype. */
    private transient UITextCellEditor cellEditor;
    
	/** The used datatype. */
	private transient StringDataType dataType;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UIStringField}.
	 */
	public UIStringField()
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
	    cellEditor = new UITextCellEditor();
	    
	    dataType = new StringDataType();
	    dataType.setCellEditor(cellEditor);
	    
	    return dataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the value as <code>String</code>.
     * 
     * @return the value as <code>String</code>.
     */
    public String getValueAsString()
    {
        return (String)getValue();
    }
    
    /**
     * Gets the allowed length.
     * 
     * @return the allowed length.
     */
    public int getLength()
    {
        return dataType.getSize();
    }
    
    /**
     * Sets the allowed length.
     * 
     * @param pLength the allowed length.
     */
    public void setLength(int pLength)
    {
        dataType.setSize(pLength);
    }
    
    /**
     * Gets the content type.
     * 
     * @return the content type.
     */
    public String getContentType()
    {
        return cellEditor.getContentType();
    }
    
    /**
     * Sets the content type.
     * 
     * @param pContentType the content type.
     */
    public void setContentType(String pContentType)
    {
        cellEditor.setContentType(pContentType);
        
        recreateEditor();
    }
    
}	// UINumberField
