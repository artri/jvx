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

import java.math.BigDecimal;

import javax.rad.genui.celleditor.UINumberCellEditor;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.IDataType;

/**
 * The {@link UINumberField} is a stand-alone editor which acts like a
 * {@link UIEditor} but does not need to be bound against an
 * {@link javax.rad.model.IDataRow}.
 * 
 * @author Martin Handsteiner
 */
public class UINumberField extends AbstractUIField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The used datatype. */
    private transient UINumberCellEditor cellEditor;
    
	/** The used datatype. */
	private transient BigDecimalDataType dataType;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UINumberField}.
	 */
	public UINumberField()
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
	    cellEditor = new UINumberCellEditor();
	    
	    dataType = new BigDecimalDataType();
	    dataType.setCellEditor(cellEditor);
	    dataType.setNumberFormat(cellEditor.getNumberFormat());
	    
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
    public BigDecimal getValueAsBigDecimal()
    {
        return (BigDecimal)getValue();
    }
    
    /**
     * Gets the value as int.
     * 
     * @return the value as int.
     */
    public int getValueAsInt()
    {
        BigDecimal value = getValueAsBigDecimal();
        if (value == null)
        {
            return 0;
        }
        else
        {
            return value.intValue();
        }
    }

    /**
     * Gets the value as int.
     * 
     * @return the value as int.
     */
    public double getValueAsDouble()
    {
        BigDecimal value = getValueAsBigDecimal();
        if (value == null)
        {
            return Double.NaN;
        }
        else
        {
            return value.doubleValue();
        }
    }

    /**
     * Gets the allowed precision.
     * 
     * @return the allowed precision.
     */
    public int getPrecision()
    {
        return dataType.getPrecision();
    }
    
    /**
     * Sets the allowed precision.
     * 
     * @param pPrecision the allowed precision.
     */
    public void setPrecision(int pPrecision)
    {
        dataType.setPrecision(pPrecision);
    }
    
    /**
     * Gets the allowed scale.
     * 
     * @return the allowed scale.
     */
    public int getScale()
    {
        return dataType.getScale();
    }
    
    /**
     * Sets the allowed scale.
     * 
     * @param pScale the allowed scale.
     */
    public void setScale(int pScale)
    {
        dataType.setScale(pScale);
    }
    
    /**
     * Gets the allowed signed.
     * 
     * @return the allowed signed.
     */
    public boolean isSigned()
    {
        return dataType.isSigned();
    }
    
    /**
     * Sets the allowed signed.
     * 
     * @param pSigned the allowed signed.
     */
    public void setSigned(boolean pSigned)
    {
        dataType.setSigned(pSigned);
    }
    
    /**
     * Gets the number format.
     * 
     * @return the number format.
     */
    public String getNumberFormat()
    {
        return dataType.getNumberFormat();
    }
    
    /**
     * Sets the number format.
     * 
     * @param pNumberFormat the number format.
     */
    public void setNumberFormat(String pNumberFormat)
    {
        dataType.setNumberFormat(pNumberFormat);
        cellEditor.setNumberFormat(pNumberFormat);
    }
    
}	// UINumberField
