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
 * 26.02.2021 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.control;

import java.math.BigDecimal;

import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.web.impl.WebComponent;
import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IGauge}.
 * 
 * @author Martin Handsteiner
 */
public class WebGauge extends WebComponent
                      implements IGauge,
                                 Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the data book. */
	private IDataRow dataRow = null;
	
	/** The column name for the value. */
	private String columnName = null;
	
	/** The (optional) column name for the label. */
	private String columnNameLabel = null;
	
	/** The translation map for internal translations. */
	private TranslationMap translation = null;
	
	/** reduces notifyRepaint calls. */
	private boolean firstNotifyRepaintCall = true;
	
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebGauge</code>.
     *
     * @see javax.rad.ui.control.IGauge
     */
	public WebGauge()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		firstNotifyRepaintCall = true;
		
		if (dataRow != null && columnName != null)
		{
			IRowDefinition rowDef = dataRow.getRowDefinition();
			
            String sLabel;
			
	    	try
	    	{
	            if (columnNameLabel != null)
	            {
	            	sLabel = getTranslation(dataRow.getValueAsString(columnNameLabel));
	            }
	            else
	            {
	                sLabel = getTranslation(rowDef.getColumnDefinition(columnName).getLabel());
	                
	                if (!StringUtil.isEmpty(sLabel))
	                {
	                	sLabel = dataRow.getValueAsString(columnName) + " " + sLabel;
	                }
	                else
	                {
	                	sLabel = dataRow.getValueAsString(columnName);
					}
	            }
	            
	            setProperty("columnLabel", sLabel);
	    	}
	    	catch (Exception ex)
	    	{
	            setProperty("columnLabel", getTranslation(columnNameLabel != null ? columnNameLabel : columnName));
	    	}
			
	    	try
	    	{
				setProperty("data", convertObject(dataRow.getValue(columnName)));
	    	}
	    	catch (Exception ex)
	    	{
	    		// Do Nothing
	    	}
		}
	}
	
	/**
	 * Converts the object.
	 * @param pObject the object
	 * @return the converted object
	 */
	private Object convertObject(Object pObject)
	{
		if (pObject instanceof BigDecimal)
		{
			BigDecimal decimal = (BigDecimal)pObject;
			
			if (decimal.scale() == 0)
			{
				return Integer.valueOf(decimal.intValue());
			}
			else
			{
				return Float.valueOf(decimal.floatValue());
			}
		}
		
		return pObject;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public IDataRow getDataRow()
    {
    	return dataRow;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDataRow(IDataRow pDataRow)
    {
    	if (dataRow != null)
    	{
    	    dataRow.removeControl(this);
    	    
    	    getFactory().getLauncher().register(dataRow, null);
    	}
    	
    	dataRow = pDataRow;
    	
    	setProperty("dataRow", dataRow, true);
    	
    	if (dataRow != null)
    	{
    	    dataRow.addControl(this);
    	    
    	    getFactory().getLauncher().register(dataRow, this);
    		
    		notifyRepaint();
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getGaugeStyle()
	{
		return ((Integer)getProperty("gaugeStyle", Integer.valueOf(STYLE_METER))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGaugeStyle(int pGaugeStyle)
	{
		setProperty("gaugeStyle", Integer.valueOf(pGaugeStyle));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return getProperty("title", null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
	{
		setProperty("title", pTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnName(String pColumnName)
	{
	    columnName = pColumnName;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public String getLabelColumnName()
    {
    	return columnNameLabel;
    }	

	/**
	 * {@inheritDoc}
	 */
    public void setLabelColumnName(String pColumnName)
    {
    	columnNameLabel = pColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public double getMinValue()
    {
        return getProperty("minValue", Double.valueOf(0)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinValue(double pMinValue)
    {
        setProperty("minValue", Double.valueOf(pMinValue));
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxValue()
    {
        return getProperty("maxValue", Double.valueOf(100)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxValue(double pMaxValue)
    {
        setProperty("maxValue", Double.valueOf(pMaxValue));
    }

    /**
     * {@inheritDoc}
     */
    public double getMinWarningValue()
    {
        return getProperty("minWarningValue", Double.valueOf(Double.NaN)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinWarningValue(double pMinWarningValue)
    {
        setProperty("minWarningValue", Double.valueOf(pMinWarningValue));
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxWarningValue()
    {
        return getProperty("maxWarningValue", Double.valueOf(Double.NaN)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxWarningValue(double pMaxWarningValue)
    {
        setProperty("maxWarningValue", Double.valueOf(pMaxWarningValue));
    }

    /**
     * {@inheritDoc}
     */
    public double getMinErrorValue()
    {
        return getProperty("minErrorValue", Double.valueOf(Double.NaN)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinErrorValue(double pMinErrorValue)
    {
        setProperty("minErrorValue", Double.valueOf(pMinErrorValue));
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxErrorValue()
    {
        return getProperty("maxErrorValue", Double.valueOf(Double.NaN)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxErrorValue(double pMaxErrorValue)
    {
        setProperty("maxErrorValue", Double.valueOf(pMaxErrorValue));
    }

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
		if (firstNotifyRepaintCall)
		{
			firstNotifyRepaintCall = false;
			getFactory().invokeLater(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() 
	{
	}

    /**
     * {@inheritDoc}
     */
    public ICellEditor getCellEditor()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setCellEditor(ICellEditor pCellEditor) throws ModelException
    {
    }

	/**
	 * {@inheritDoc}
	 */
    public TranslationMap getTranslation()
    {
    	return translation;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		translation = pTranslation;
	}
	
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }
	
    /**
     * {@inheritDoc}
     */
	public String translate(String pText)
	{
		if (bTranslationEnabled && translation != null)
		{
			return translation.translate(pText);
		}
		else
		{
			return pText;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Gets the translation.
     *
     * @param pText the text.
     * @return the translation.
     */
	public String getTranslation(String pText)
	{
		if (translation == null || pText == null)
		{
			return pText;
		}
		else
		{
			return translation.translate(pText);
		}
	}

}	// WebGauge
