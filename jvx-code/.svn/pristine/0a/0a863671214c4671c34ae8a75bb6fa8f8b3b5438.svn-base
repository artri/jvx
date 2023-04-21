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
 * 05.06.2009 - [JR] - set/getTranslation added
 */
package javax.rad.ui.control;

import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IComponent;
import javax.rad.util.TranslationMap;

/**
 * Platform and technology independent Chart definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IChart extends IComponent, 
                                ITableControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Style constant for showing a line chart. */
    public static final int STYLE_LINES = 0;
    
    /** Style constant for showing an area chart. */
    public static final int STYLE_AREA = 1;
    
    /** Style constant for showing a bar chart. */
    public static final int STYLE_BARS = 2;
    
    /** Style constant for showing a pie chart. */
    public static final int STYLE_PIE = 3;
    
    /** Style constant for showing an step line chart. */
    public static final int STYLE_STEPLINES = 100;
    
    /** Style constant for showing an area chart. */
    public static final int STYLE_STACKEDAREA = 101;
    
    /** Style constant for showing an area chart. */
    public static final int STYLE_STACKEDPERCENTAREA = 201;
    
    /** Style constant for showing a stacked bar chart. */
    public static final int STYLE_STACKEDBARS = 102;
    
    /** Style constant for showing a stacked bar chart. */
    public static final int STYLE_STACKEDPERCENTBARS = 202;
    
    /** Style constant for showing a overlapped bar chart. */
    public static final int STYLE_OVERLAPPEDBARS = 302;

    /** Style constant for showing a bar chart. */
    public static final int STYLE_HBARS = 1002;
    
    /** Style constant for showing a stacked bar chart. */
    public static final int STYLE_STACKEDHBARS = 1102;
    
    /** Style constant for showing a stacked bar chart. */
    public static final int STYLE_STACKEDPERCENTHBARS = 1202;
    
    /** Style constant for showing a overlapped bar chart. */
    public static final int STYLE_OVERLAPPEDHBARS = 1302;

    /** Style constant for showing a ring chart. */
    public static final int STYLE_RING = 103;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the chart style.
	 * 
	 * @return the chart style.
	 * @see #STYLE_AREA
	 * @see #STYLE_BARS
	 * @see #STYLE_LINES
	 * @see #STYLE_PIE
	 */
	public int getChartStyle();

	/**
	 * Sets the chart style.
	 * 
	 * @param pChartStyle the chart style.
	 * @see #STYLE_AREA
	 * @see #STYLE_BARS
	 * @see #STYLE_LINES
	 * @see #STYLE_PIE
	 */
	public void setChartStyle(int pChartStyle);

	/**
	 * Gets the title.
	 * 
	 * @return the title.
	 */
	public String getTitle();

	/**
	 * Sets the title.
	 * 
	 * @param pTitle the title.
	 */
	public void setTitle(String pTitle);

	/**
	 * Gets the x axis title.
	 * 
	 * @return the x axis title.
	 */
	public String getXAxisTitle();

	/**
	 * Sets the x axis title.
	 * 
	 * @param pXAxisTitle the x axis title.
	 */
	public void setXAxisTitle(String pXAxisTitle);

	/**
	 * Gets the y axis title.
	 * 
	 * @return the y axis title.
	 */
	public String getYAxisTitle();

	/**
	 * Sets the y axis title.
	 * 
	 * @param pYAxisTitle the y axis title.
	 */
	public void setYAxisTitle(String pYAxisTitle);

	/**
	 * Gets the x column name.
	 * 
	 * @return the x column name.
	 */
	public String getXColumnName();

	/**
	 * Sets the x column name.
	 * 
	 * @param pXColumnName the x column name.
	 */
	public void setXColumnName(String pXColumnName);
	
	/**
	 * Gets the y column names.
	 * 
	 * @return the y column names.
	 */
	public String[] getYColumnNames();

	/**
	 * Sets the y column names.
	 * 
	 * @param pYColumnNames y column names.
	 */
	public void setYColumnNames(String[] pYColumnNames);
	
	/**
	 * Gets the translation for this table.
	 * 
	 * @return the translation mapping
	 */
	public TranslationMap getTranslation();

	/**
	 * Sets the translation for this table.
	 * 
	 * @param pTranslation the translation mapping
	 */
	public void setTranslation(TranslationMap pTranslation);
	
}	// IChart
