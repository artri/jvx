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

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.control.IChart;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.swing.ext.JVxChart;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingChart</code> is the <code>IChart</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 */
public class SwingChart extends SwingComponent<JVxChart>
                        implements IChart
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingChart</code>.
	 */
	public SwingChart()
	{
		super(new JVxChart());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getDataBook()
	{
		return resource.getDataBook();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataBook(IDataBook pDataBook)
	{
	    IDataBook book = resource.getDataBook();
	    
	    if (book != null)
	    {
	        book.removeControl(this);
	    }
	    
		resource.setDataBook(pDataBook);
		
		if (pDataBook != null)
		{
		    pDataBook.addControl(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getChartStyle()
	{
		return resource.getChartStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChartStyle(int pChartStyle)
	{
		resource.setChartStyle(pChartStyle);
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
	public String getXAxisTitle()
	{
		return resource.getXAxisTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setXAxisTitle(String pXAxisTitle)
	{
		resource.setXAxisTitle(pXAxisTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getYAxisTitle()
	{
		return resource.getYAxisTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setYAxisTitle(String pYAxisTitle)
	{
		resource.setYAxisTitle(pYAxisTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getXColumnName()
	{
		return resource.getXColumnName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setXColumnName(String pXColumnName)
	{
		resource.setXColumnName(pXColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getYColumnNames()
	{
		return resource.getYColumnNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setYColumnNames(String[] pYColumnNames)
	{
		resource.setYColumnNames(pYColumnNames);
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
	public void startEditing()
	{
		resource.startEditing();
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
