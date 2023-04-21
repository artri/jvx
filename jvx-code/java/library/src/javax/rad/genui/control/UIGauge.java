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
 * 22.02.2021 - [HM] - creation
 */
package javax.rad.genui.control;

import java.beans.Beans;
import java.math.BigDecimal;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.control.IGauge;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent Gauge.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIGauge extends AbstractControllable<IGauge> 
                     implements IGauge
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Internal data for design mode. */
    private static IDataBook internalDesignModeData = null;

    /** The set dataBook. */
    private transient IDataRow dataRow = null;
    /** The x column name. */
    private transient String columnName = null;
    /** The title. */
    private transient String sTitle = null;

    /** whether we're in design mode. */
    private transient boolean designMode = Beans.isDesignTime();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIGauge</code>.
     *
     * @see IGauge
     */
	public UIGauge()
	{
		this(UIFactoryManager.getFactory().createGauge());
	}

    /**
     * Creates a new instance of <code>UIGauge</code> with the given gauge.
     *
     * @param pMeter the meter 
     * @see IGauge
     */
	protected UIGauge(IGauge pMeter)
	{
		super(pMeter);

		setMaximumSize(800, 600);
		
		try
        {
            installGauge();
        }
        catch (ModelException e)
        {
            // Ignore
        }
	}
	
	/**
	 * Creates a new instance of {@link UIGauge}.
	 *
	 * @param pDataRow the {@link IDataRow data row}.
	 * @param pColumnName the {@link String column name}.
     * @throws ModelException if it fails
	 * @see #setDataRow(IDataRow)
	 * @see #setColumnName(String)
	 */
	public UIGauge(IDataRow pDataRow, String pColumnName) throws ModelException
	{
		this();
		
		setDataRow(pDataRow);
		setColumnName(pColumnName);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
    public void setDataRow(IDataRow pDataRow) throws ModelException
    {
        dataRow = pDataRow;
        
        installGauge();
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
    public void setColumnName(String pColumnName) throws ModelException
    {
        columnName = pColumnName;
        
        installGauge();
    }
    
    /**
     * {@inheritDoc}
     */
    public String getLabelColumnName()
    {
    	return uiResource.getLabelColumnName();
    }

    /**
     * {@inheritDoc}
     */
    public void setLabelColumnName(String pColumn)
    {
    	uiResource.setLabelColumnName(pColumn);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinValue()
    {
        return uiResource.getMinValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinValue(double pMinValue)
    {
        uiResource.setMinValue(pMinValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxValue()
    {
        return uiResource.getMaxValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxValue(double pMaxValue)
    {
        uiResource.setMaxValue(pMaxValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinWarningValue()
    {
        return uiResource.getMinWarningValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinWarningValue(double pMinWarningValue)
    {
        uiResource.setMinWarningValue(pMinWarningValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxWarningValue()
    {
        return uiResource.getMaxWarningValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxWarningValue(double pMaxWarningValue)
    {
        uiResource.setMaxWarningValue(pMaxWarningValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinErrorValue()
    {
        return uiResource.getMinErrorValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMinErrorValue(double pMinErrorValue)
    {
        uiResource.setMinErrorValue(pMinErrorValue);
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxErrorValue()
    {
        return uiResource.getMaxErrorValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxErrorValue(double pMaxErrorValue)
    {
        uiResource.setMaxErrorValue(pMaxErrorValue);
    }

	/** 
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook() 
	{
        IDataRow dr = getDataRow();
        if (dr instanceof IDataBook)
        {
            return (IDataBook)dr;
        }
        else
        {
            return null;
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGaugeStyle()
	{
		return uiResource.getGaugeStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGaugeStyle(int pMeterStyle)
	{
		uiResource.setGaugeStyle(pMeterStyle);
	}
	
    /**
     * {@inheritDoc}
     */
    public String getTitle()
    {
        return sTitle;
    }

    /**
     * {@inheritDoc}
     */
    public void setTitle(String pTitle)
    {
        sTitle = pTitle;
        
        uiResource.setTitle(sTitle);
    }

    /**
     * {@inheritDoc}
     */
    public ICellEditor getCellEditor()
    {
        return uiResource.getCellEditor();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setCellEditor(ICellEditor pCellEditor) throws ModelException
    {
        uiResource.setCellEditor(pCellEditor);
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
    	uiResource.notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
    	uiResource.saveEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() 
	{
    	uiResource.cancelEditing();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();

		super.updateTranslation();
		
		if (bChanged)
		{
			uiResource.setTranslation(getCurrentTranslation());
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createComponentName()
    {
        String sColName = getColumnName();
        
        if (!StringUtil.isEmpty(sColName))
        {
            String name = createComponentNamePrefix();
            
            IDataRow row = getDataRow();
            
            if (row instanceof IDataBook)
            {
                IDataBook dataBook = (IDataBook)row;
                
                name = name + "_" + dataBook.getName();
            }
            
            name = name + "_" + sColName;
            name = incrementNameIfExists(name, getExistingNames(), false);
            
            return name;
        }
        
        return super.createComponentName();
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Installs either a dummy binding for design mode or the real binding.
	 * 
     * @throws ModelException if it fails
	 */
	protected void installGauge() throws ModelException
	{
	    if (designMode && (dataRow == null || columnName == null))
	    {
            uiResource.setDataRow(getInternalDesignModeData());
            uiResource.setColumnName("A");
	    }
	    else
	    {
	        uiResource.setDataRow(dataRow);
            uiResource.setColumnName(columnName);
	    }
	}
	
	/**
	 * Gets internal data for design mode.
	 * 
	 * @return internal data for design mode.
	 * @throws ModelException if it fails
	 */
	protected static IDataBook getInternalDesignModeData() throws ModelException
	{
	    if (internalDesignModeData == null)
	    {
	    	try
	    	{
		        internalDesignModeData = (IDataBook)Class.forName("com.sibvisions.rad.model.mem.MemDataBook").newInstance();
		        internalDesignModeData.setName("internalDesignModeData");
		        internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("TYP"));
	            internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("A", new BigDecimalDataType()));
	            internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("B", new BigDecimalDataType()));
	            internalDesignModeData.open();
		        
	            internalDesignModeData.insert(false);
	            internalDesignModeData.setValues(null, new Object[] {"A", BigDecimal.valueOf(4), BigDecimal.valueOf(1)});
	            internalDesignModeData.saveSelectedRow();
	            internalDesignModeData.setSelectedColumn("A");
	        }
	        catch (Exception ex)
	        {
	            // ignore
	        }
	    }
	    
	    return internalDesignModeData;
	}
	
}	// UIGauge
