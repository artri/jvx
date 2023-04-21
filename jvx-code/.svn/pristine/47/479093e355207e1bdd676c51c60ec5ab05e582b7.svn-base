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
 * 20.11.2009 - [HM] - creation
 * 23.07.2010 - [JR] - #149: setDataBook: RESTORE, RELOAD events set
 */
package com.sibvisions.rad.ui.web.impl.control;

import java.util.Arrays;

import javax.rad.model.IDataBook;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.ui.control.IChart;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.web.impl.WebComponent;

/**
 * Web server implementation of {@link IChart}.
 * 
 * @author Martin Handsteiner
 */
public class WebChart extends WebComponent
                      implements IChart,
                                 Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the data book. */
	private IDataBook dataBook = null;
	
	/** The x column name. */
	private String xColumnName = null;
	
	/** The y column names. */
	private String[] yColumnNames = null;
	
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
     * Creates a new instance of <code>WebChart</code>.
     *
     * @see javax.rad.ui.control.IChart
     */
	public WebChart()
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
		
		
		if (dataBook != null && xColumnName != null && yColumnNames != null)
		{
			IRowDefinition rowDef = dataBook.getRowDefinition();
			
			String[] sYLabels = new String[yColumnNames.length];
			
			for (int i = 0; i < yColumnNames.length; i++)
			{
		    	try
		    	{
		    		sYLabels[i] = translate(rowDef.getColumnDefinition(yColumnNames[i]).getLabel());
		    	}
		    	catch (Exception ex)
		    	{
		    		sYLabels[i] = translate(yColumnNames[i]);
		    	}
			}

            String[] sOldYLabels = getProperty("yColumnLabels", null);
            
            if (!Arrays.equals(sOldYLabels, sYLabels))
            {
                setProperty("yColumnLabels", sYLabels);
            }
			
			String sXLabel;
			
	    	try
	    	{
	    		sXLabel = rowDef.getColumnDefinition(xColumnName).getLabel();
	    	}
	    	catch (Exception ex)
	    	{
	    		sXLabel = xColumnName;
	    	}
	    	
			setProperty("xColumnLabel", translate(sXLabel));
		}			
	}
		
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
    	if (dataBook != null)
    	{
    		dataBook.removeControl(this);
    		
    		getFactory().getLauncher().register(dataBook, null);
    	}
    	
    	dataBook = pDataBook;
    	
    	setProperty("dataBook", dataBook, true);

    	if (dataBook != null)
    	{
    		dataBook.addControl(this);
    		
    		getFactory().getLauncher().register(dataBook, this);
    		
    		notifyRepaint();
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getChartStyle()
	{
		return ((Integer)getProperty("chartStyle", Integer.valueOf(STYLE_LINES))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChartStyle(int pChartStyle)
	{
		setProperty("chartStyle", Integer.valueOf(pChartStyle));
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
	public String getXAxisTitle()
	{
		return getProperty("xAxisTitle", null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setXAxisTitle(String pXAxisTitle)
	{
		setProperty("xAxisTitle", pXAxisTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getYAxisTitle()
	{
		return getProperty("yAxisTitle", null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setYAxisTitle(String pYAxisTitle)
	{
		setProperty("yAxisTitle", pYAxisTitle);
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
		
		setProperty("xColumnName", pXColumnName, true);
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
		
		setProperty("yColumnNames", pYColumnNames, true);
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
	public void startEditing()
	{
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

}	// WebChart
