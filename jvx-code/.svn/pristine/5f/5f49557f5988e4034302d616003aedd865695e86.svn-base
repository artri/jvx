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
 * 01.10.2008 - [HM] - creation
 * 05.06.2009 - [JR] - set/getTranslation implemented
 */
package com.sibvisions.rad.ui.swing.impl.control;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.swing.ext.JVxGauge;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingChart</code> is the <code>IChart</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 */
public class SwingGauge extends SwingComponent<JVxGauge>
                        implements IGauge
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingChart</code>.
	 */
	public SwingGauge()
	{
		super(new JVxGauge());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataRow getDataRow()
	{
		return resource.getDataRow();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataRow(IDataRow pDataRow)
	{
	    IDataRow book = resource.getDataRow();
	    
	    if (book != null)
	    {
	        book.removeControl(this);
	    }
	    
		resource.setDataRow(pDataRow);
		
		if (pDataRow != null)
		{
		    pDataRow.addControl(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGaugeStyle()
	{
		return resource.getGaugeStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGaugeStyle(int pGaugeStyle)
	{
		resource.setGaugeStyle(pGaugeStyle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return resource.getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
	{
		resource.setTitle(pTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnName()
	{
		return resource.getColumnName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnName(String pColumnName)
	{
		resource.setColumnName(pColumnName);
	}
	
    /**
     * {@inheritDoc}
     */
    public String getLabelColumnName()
    {
    	return resource.getLabelColumnName();
    }

    /**
     * {@inheritDoc}
     */
    public void setLabelColumnName(String pColumn)
    {
    	resource.setLabelColumnName(pColumn);
    }
    
    /**
     * {@inheritDoc}
     */
    public double getMinValue()
    {
        return resource.getMinValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinValue(double pMinValue)
    {
        resource.setMinValue(pMinValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxValue()
    {
        return resource.getMaxValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxValue(double pMaxValue)
    {
        resource.setMaxValue(pMaxValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinWarningValue()
    {
        return resource.getMinWarningValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinWarningValue(double pMinWarningValue)
    {
        resource.setMinWarningValue(pMinWarningValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxWarningValue()
    {
        return resource.getMaxWarningValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxWarningValue(double pMaxWarningValue)
    {
        resource.setMaxWarningValue(pMaxWarningValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinErrorValue()
    {
        return resource.getMinErrorValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinErrorValue(double pMinErrorValue)
    {
        resource.setMinErrorValue(pMinErrorValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxErrorValue()
    {
        return resource.getMaxErrorValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxErrorValue(double pMaxErrorValue)
    {
        resource.setMaxErrorValue(pMaxErrorValue);
    }
    
    /**
     * {@inheritDoc}
     */
    public ICellEditor getCellEditor()
    {
        return resource.getCellEditor();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setCellEditor(ICellEditor pCellEditor) throws ModelException
    {
        resource.setCellEditor(pCellEditor);
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		resource.notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		resource.cancelEditing();
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		resource.saveEditing();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		resource.setTranslation(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TranslationMap getTranslation()
	{
		return resource.getTranslation();
	}
	
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        resource.setTranslationEnabled(pEnabled);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return resource.isTranslationEnabled();
    }
	
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        return resource.translate(pText);
    }
	
}	// SwingChart
