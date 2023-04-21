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
 * 09.01.2010 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.control;

import java.beans.Beans;
import java.math.BigDecimal;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.ui.control.IChart;

/**
 * Platform and technology independent Chart.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIChart extends AbstractControllable<IChart> 
                     implements IChart
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Internal data for design mode. */
    private static IDataBook internalDesignModeData = null;

    /** The set dataBook. */
    private transient IDataBook dataBook = null;
    /** The x column name. */
    private transient String xColumnName = null;
    /** The y column names. */
    private transient String[] yColumnNames = null;

    /** The title. */
    private transient String sTitle = null;
    /** The x axis title. */
    private transient String sXAxisTitle = null;
    /** The y axis title. */
    private transient String sYAxisTitle = null;
    
    /** whether we're in design mode. */
    private transient boolean designMode = Beans.isDesignTime();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIChart</code>.
     *
     * @see IChart
     */
	public UIChart()
	{
		this(UIFactoryManager.getFactory().createChart());
	}

    /**
     * Creates a new instance of <code>UIChart</code> with the given chart.
     *
     * @param pChart the chart 
     * @see IChart
     */
	protected UIChart(IChart pChart)
	{
		super(pChart);

		setMaximumSize(800, 600);
		
		installChart();
	}
	
	/**
	 * Creates a new instance of {@link UIChart}.
	 *
	 * @param pDataBook the {@link IDataBook data book}.
	 * @see #setDataBook(IDataBook)
	 */
	public UIChart(IDataBook pDataBook)
	{
		this();
		
		setDataBook(pDataBook);
	}

	/**
	 * Creates a new instance of {@link UIChart}.
	 *
	 * @param pDataBook the {@link IDataBook data book}.
	 * @param pXColumnName the {@link String X column name}.
	 * @param pYColumnNames the {@link String Y column names}.
	 * @see #setDataBook(IDataBook)
	 * @see #setXColumnName(String)
	 * @see #setYColumnNames(String[])
	 */
	public UIChart(IDataBook pDataBook, String pXColumnName, String[] pYColumnNames)
	{
		this();
		
		setDataBook(pDataBook);
		setXColumnName(pXColumnName);
		setYColumnNames(pYColumnNames);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public IDataBook getDataBook()
    {
    	return dataBook;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDataBook(IDataBook pDataBook)
    {
        dataBook = pDataBook;
        
        installChart();
    }
	
	/** 
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook() 
	{
		return getDataBook();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getChartStyle()
	{
		return uiResource.getChartStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChartStyle(int pChartStyle)
	{
		uiResource.setChartStyle(pChartStyle);
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
	public String getXAxisTitle()
	{
		return sXAxisTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setXAxisTitle(String pXAxisTitle)
	{
	    sXAxisTitle = pXAxisTitle;
	    
		uiResource.setXAxisTitle(sXAxisTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getYAxisTitle()
	{
		return sYAxisTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setYAxisTitle(String pYAxisTitle)
	{
	    sYAxisTitle = pYAxisTitle;
	    
		uiResource.setYAxisTitle(sYAxisTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getXColumnName()
	{
		return xColumnName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setXColumnName(String pXColumnName)
	{
		xColumnName = pXColumnName;
        
        installChart();
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getYColumnNames()
	{
		return yColumnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setYColumnNames(String[] pYColumnNames)
	{
        yColumnNames = pYColumnNames;
        
        installChart();
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
	public void startEditing() 
	{
    	uiResource.startEditing();
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
        if (getDataBook() != null)
        {
            String name = createComponentNamePrefix() + "_" + getDataBook().getName();
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
	 */
	protected void installChart()
	{
	    if (designMode && (dataBook == null || xColumnName == null || yColumnNames == null))
	    {
            uiResource.setDataBook(getInternalDesignModeData());
            uiResource.setXColumnName("TYP");
            uiResource.setYColumnNames(new String[] {"A", "B"});
	    }
	    else
	    {
	        uiResource.setDataBook(dataBook);
            uiResource.setXColumnName(xColumnName);
            uiResource.setYColumnNames(yColumnNames);
	    }
	}
	
	/**
	 * Gets internal data for design mode.
	 * 
	 * @return internal data for design mode.
	 */
	protected static IDataBook getInternalDesignModeData()
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
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"B", BigDecimal.valueOf(3), BigDecimal.valueOf(2)});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"C", BigDecimal.valueOf(2), BigDecimal.valueOf(3)});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"D", BigDecimal.valueOf(1), BigDecimal.valueOf(4)});
                internalDesignModeData.saveSelectedRow();
                internalDesignModeData.setSelectedRow(0);
                internalDesignModeData.setSelectedColumn("A");
	        }
	        catch (Exception ex)
	        {
	            // ignore
	        }
	    }
	    
	    return internalDesignModeData;
	}
	
}	// UIChart
