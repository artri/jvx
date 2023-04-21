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
 * 10.01.2011 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.InvalidObjectException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ITableControl;
import javax.rad.util.TranslationMap;
import javax.swing.JPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.TableOrder;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>JVxChart</code> is a simple chart panel which also implements the {@link ITableControl}
 * interface.
 *  
 * @author Martin Handsteiner
 */
public class JVxChart extends JPanel
                      implements ITableControl,
                                 Runnable,
                                 ChartMouseListener
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
    
    /** The default colors. */
    public static final Color[] DEFAULT_COLORS = new Color[] {
            new Color(94,  144, 233), // #5E90E9
            new Color(210, 110, 104), // #D26E68
            new Color(172, 219, 108), // #ACDB6C
            new Color(240, 221, 106), // #F0DD6A
            new Color(126, 216, 211), // #7ED8D3
            new Color(211, 111, 163), // #D36FA3
            new Color(104,  95, 170), // #685FAA
            new Color(228, 133,  80), // #E48550
            new Color(70,   97, 140), // #46618C
            new Color(152,  87,  80), // #985750
            new Color(141, 173,  95), // #8DAD5F
            new Color(181, 168, 102), // #B5A866
            new Color(73,  119, 128), // #497780
            new Color(133,  65, 110), // #85416E
            new Color(82,   88, 162), // #5258A2
            new Color(140,  92,  66)  // #8C5C42
    };

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The chart panel. */
	private ChartPanel chartPanel = null;

	/** The dataset. */
	private AbstractDataset dataset = null;

	/** The x axis. */
	private Axis vaX = null;
	
	/** The y axis. */
	private Axis vaY = null;

	/** The DataBook to be shown. */
	private IDataBook dataBook = null;
	
	/** The chart style. */
	private int chartStyle = STYLE_LINES;
	
	/** The title. */
	private String sTitle = null;
	
	/** The x axis title. */
	private String sXTitle = null;
	
	/** The y axis title. */
	private String sYTitle = null;
	
	/** The x column name. */
	private String sXColumnName = null;
	
	/** The y column names. */
	private String[] saYColumnNames = null;
	
	/** The translation mapping. */
	private TranslationMap tmpTranslation = null;
	
	/** Tells, if notifyRepaint is called the first time. */
	private boolean bFirstNotifyRepaintCall = true;
	
    /** Whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    static
//    {
//        for (int i = 0; i < DEFAULT_COLORS.length; i++)
//        {
//            DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[i] = DEFAULT_COLORS[i];
//        }
//    }
    
	/** 
	 * Constructs a <code>JVxChart</code>.
	 */
	public JVxChart()
	{
		setLayout(new BorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//ITABLECONTROL
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (bFirstNotifyRepaintCall)
		{
			bFirstNotifyRepaintCall = false;

			JVxUtil.invokeLater(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEditing()
	{
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		if (dataset != null)
		{
			try
			{
				dataset.validateObject();
			}
			catch (Exception e)
			{
			    e.printStackTrace();
				// Ignore Exception
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		// Do nothing
	}

	//ITranslatable
	
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (tmpTranslation != pTranslation)
    	{
        	tmpTranslation = pTranslation;

        	if (vaX != null)
        	{
        		vaX.setLabel(translate(sXTitle));
        	}
        	
        	if (vaY != null)
        	{
        		vaY.setLabel(translate(sYTitle));
        	}

        	if (chartPanel != null)
        	{
        		chartPanel.getChart().setTitle(translate(sTitle));
        	}
        	
        	notifyRepaint();
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
    	return tmpTranslation;
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
    
    // RUNNABLE
    
    /**
     * {@inheritDoc}
     */
	public void run()
	{
		cancelEditing();
		
		bFirstNotifyRepaintCall = true;
	}
    
    //CHARTMOUSELISTENER
    
    /**
     * {@inheritDoc}
     */
    public void chartMouseClicked(ChartMouseEvent pEvent)
    {
        ChartEntity entity = pEvent.getEntity();

        try
        {
            if (entity instanceof XYItemEntity)
            {
                XYItemEntity xyEntity = (XYItemEntity)entity;
                
                dataBook.setSelectedRow(xyEntity.getItem());
                dataBook.setSelectedColumn(saYColumnNames[xyEntity.getSeriesIndex()]);
                
                dispatchMouseEvent(pEvent.getTrigger());
            }
            else if (entity instanceof PieSectionEntity)
            {             
                PieSectionEntity pieEntity = (PieSectionEntity)entity;
                
                if (saYColumnNames.length > 1)
                {
                    dataBook.setSelectedColumn(saYColumnNames[pieEntity.getSectionIndex()]);
                    
                    dispatchMouseEvent(pEvent.getTrigger());
                }
                else
                {
                    int selected = -1;
                    for (int i = 0, count = dataBook.getRowCount(); i < count && selected < 0; i++)
                    {
                        if (pieEntity.getSectionKey().equals(translate(dataBook.getDataRow(i).getValueAsString(sXColumnName))))
                        {
                            selected = i;
                        }
                    }
                    dataBook.setSelectedRow(selected);
                    dataBook.setSelectedColumn(saYColumnNames[0]);
                    
                    dispatchMouseEvent(pEvent.getTrigger());
                }
            }
        }
        catch (Exception ex)
        {
            // Ignore
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void chartMouseMoved(ChartMouseEvent pEvent)
    {
        // Not yet implemented
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the mouse event based on the given event with the given ID.
     * 
     * @param pEvent the event
     * @param pId the id
     * @return the mouse event
     */
    protected MouseEvent getMouseEvent(MouseEvent pEvent, int pId)
    {
        return new MouseEvent(this, pId, 
                pEvent.getWhen(), pEvent.getModifiers(), pEvent.getX(), pEvent.getY(), pEvent.getClickCount(), pEvent.isPopupTrigger(), pEvent.getButton());
    }

    /**
     * Sends pressed, clicked and released when entity is selected.
     *  
     * @param pEvent the original mouse event.
     */
    protected void dispatchMouseEvent(MouseEvent pEvent)
    {
        dispatchEvent(getMouseEvent(pEvent, MouseEvent.MOUSE_PRESSED));
        dispatchEvent(getMouseEvent(pEvent, MouseEvent.MOUSE_RELEASED));
        dispatchEvent(getMouseEvent(pEvent, MouseEvent.MOUSE_CLICKED));
    }

    
	/**
     * Gets the DataBook shown by this control.
     *
     * @return the DataBook.
     * @see #setDataBook
     */
	public IDataBook getDataBook()
	{
		return dataBook;
	}
	
	/**
     * Sets the DataBook shown by this control.
     *
     * @param pDataBook the DataBook.
     * @see #getDataBook
     */
	public void setDataBook(IDataBook pDataBook)
	{
		uninstallChart();
		
		dataBook = pDataBook;
		
		installChart();
	}
	
    /**
     * Gets the base style of the chart (the lowest 2 digits of constant).
     * 
     * @return the base style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
     */
    public int getBaseStyle()
    {
        
        return chartStyle % 100;
    }

    /**
     * Gets the sub style of the chart (the lowest 2 digits of constant).
     * 
     * @return the base style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
     */
    public int getSubStyle()
    {
        
        return chartStyle % 1000;
    }

    /**
     * Gets the sub style of the chart (the lowest 2 digits of constant).
     * 
     * @return the base style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
     */
    public boolean isHorizontalStyle()
    {
        
        return chartStyle / 1000 != 0;
    }

	/**
	 * Gets the style of the chart.
	 * 
     * @return the chart style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
     */
	public int getChartStyle()
	{
		return chartStyle;
	}

	/**
	 * Sets the style of the chart.
	 * 
	 * @param pChartStyle the chart style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
	 */
	public void setChartStyle(int pChartStyle)
	{
		if (pChartStyle != chartStyle)
		{
			uninstallChart();
			
			chartStyle = pChartStyle;
	
			installChart();
		}
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title.
	 */
	public String getTitle()
	{
		return sTitle;
	}

	/**
	 * Sets the title.
	 * 
	 * @param pTitle the title.
	 */
	public void setTitle(String pTitle)
	{
		sTitle = pTitle;
		
        if (chartPanel != null)
        {
            chartPanel.getChart().setTitle(translate(sTitle));
        }
	}

	/**
	 * Gets the x axis title.
	 * 
	 * @return the x axis title.
	 */
	public String getXAxisTitle()
	{
		return sXTitle;
	}

	/**
	 * Sets the x axis title.
	 * 
	 * @param pXAxisTitle the x axis title.
	 */
	public void setXAxisTitle(String pXAxisTitle)
	{
		sXTitle = pXAxisTitle;
		
        if (vaX != null)
        {
            vaX.setLabel(translate(sXTitle));
        }
	}

	/**
	 * Gets the y axis title.
	 * 
	 * @return the y axis title.
	 */
	public String getYAxisTitle()
	{
		return sYTitle;
	}

	/**
	 * Sets the y axis title.
	 * 
	 * @param pYAxisTitle the y axis title.
	 */
	public void setYAxisTitle(String pYAxisTitle)
	{
		sYTitle = pYAxisTitle;

        if (vaY != null)
        {
            vaY.setLabel(translate(sYTitle));
        }
	}

	/**
	 * Gets the x column name.
	 * 
	 * @return the x column name.
	 */
	public String getXColumnName()
	{
		return sXColumnName;
	}

	/**
	 * Sets the x column name.
	 * 
	 * @param pXColumnName the x column name.
	 */
	public void setXColumnName(String pXColumnName)
	{
		uninstallChart();
		
		sXColumnName = pXColumnName;
		
		installChart();
	}

	/**
	 * Gets the y column names.
	 * 
	 * @return the y column names.
	 */
	public String[] getYColumnNames()
	{
		return saYColumnNames;
	}

	/**
	 * Sets the y column names.
	 * 
	 * @param pYColumnNames y column names.
	 */
	public void setYColumnNames(String[] pYColumnNames)
	{
		uninstallChart();
		
		saYColumnNames = pYColumnNames;
		
		installChart();
	}	
	
	/**
	 * Uninstalls the CellEditor and its CellEditorComponent.
	 */
	private void uninstallChart()
	{
		if (chartPanel != null)
		{
			dataBook.removeControl(this);
			
			remove(chartPanel);
			
			chartPanel = null;
			dataset = null;
		}
	}

	/**
     * Gets the translation.
     *
     * @param pText the text.
     * @return the translation.
     */
	public String translate(String pText)
	{
		if (!bTranslationEnabled || tmpTranslation == null || pText == null)
		{
			return pText;
		}
		else
		{
			return tmpTranslation.translate(pText);
		}
	}

	/**
	 * Installs the CellEditor and its CellEditorComponent.
	 */
	private void installChart()
	{
		if (dataBook != null && sXColumnName != null && saYColumnNames != null)
		{
			try
			{
			    Plot plot = null;
				
                if (getBaseStyle() == STYLE_PIE)
				{
					dataset = new DataBookCategoryDataset(dataBook, sXColumnName, saYColumnNames, this);
					
					if (getSubStyle() == STYLE_RING)
					{
	                    plot = new RingPlot(new CategoryToPieDataset((CategoryDataset)dataset, TableOrder.BY_ROW, 0));
	                    
					}
					else
					{
					    plot = new PiePlot(new CategoryToPieDataset((CategoryDataset)dataset, TableOrder.BY_ROW, 0));
					}
					
                    ((PiePlot)plot).setShadowPaint(null);
					((PiePlot)plot).setLabelGenerator(new StandardPieSectionLabelGenerator(
							"{0}: {2}",
							new DecimalFormat("0"),
							new DecimalFormat("0%")));
				}
				else
				{
					ColumnDefinition xColumnDefinition = dataBook.getRowDefinition().getColumnDefinition(sXColumnName);
					
					boolean isXDate = Date.class.isAssignableFrom(xColumnDefinition.getDataType().getTypeClass());
					boolean isXString = String.class.isAssignableFrom(xColumnDefinition.getDataType().getTypeClass());
					
					if (isXString)
					{
						dataset = new DataBookCategoryDataset(dataBook, sXColumnName, saYColumnNames, this);
					}
					else
					{
						dataset = new DataBookXYDataset(dataBook, sXColumnName, saYColumnNames, this);
					}

					AbstractRenderer renderer;
					if (getSubStyle() == STYLE_AREA)
					{
						if (isXString)
						{
							renderer = new AreaRenderer();
						}
						else
						{
							renderer = new XYAreaRenderer(XYAreaRenderer.AREA);
						}
					}
					else if (getSubStyle() == STYLE_STACKEDAREA || getSubStyle() == STYLE_STACKEDPERCENTAREA)
                    {
                        if (isXString)
                        {
                            renderer = new StackedAreaRenderer();
                            ((StackedAreaRenderer)renderer).setRenderAsPercentages(getSubStyle() == STYLE_STACKEDPERCENTAREA);
                        }
                        else
                        {
                            // STYLE_STACKEDPERCENTAREA is not supported in JFreeChart, so it is rendered in not percentage mode...
                            renderer = new StackedXYAreaRenderer2();
                        }
                    }
					else if (getBaseStyle() == STYLE_BARS)
					{
					    if (getSubStyle() == STYLE_STACKEDBARS || getSubStyle() == STYLE_STACKEDPERCENTBARS)
	                    {
	                        if (isXString)
	                        {
	                            renderer = new FixedStackedBarRenderer();
	                            ((StackedBarRenderer)renderer).setBarPainter(new StandardBarPainter());
	                            ((StackedBarRenderer)renderer).setItemMargin(0.1d);
	                            ((StackedBarRenderer)renderer).setShadowVisible(false);
	                            ((StackedBarRenderer)renderer).setRenderAsPercentages(getSubStyle() == STYLE_STACKEDPERCENTBARS);
	                        }
	                        else
	                        {
	                            renderer = new StackedXYBarRenderer(0.1d);
	                            ((StackedXYBarRenderer)renderer).setBarPainter(new StandardXYBarPainter());
	                            ((StackedXYBarRenderer)renderer).setShadowVisible(false);
	                            ((StackedXYBarRenderer)renderer).setRenderAsPercentages(getSubStyle() == STYLE_STACKEDPERCENTBARS);
	                        }
	                    }
	                    else if (getSubStyle() == STYLE_OVERLAPPEDBARS)
	                    {
	                        if (isXString)
	                        {
	                            renderer = new FixedLayeredBarRenderer();
	                            ((LayeredBarRenderer)renderer).setBarPainter(new StandardBarPainter());
	                            ((LayeredBarRenderer)renderer).setItemMargin(0.1d);
	                            ((LayeredBarRenderer)renderer).setDrawBarOutline(false);
	                            ((LayeredBarRenderer)renderer).setShadowVisible(false);
	                        }
	                        else
	                        {
	                            renderer = new XYBarRenderer(0.1d);
	                            ((XYBarRenderer)renderer).setBarPainter(new StandardXYBarPainter());
	                            ((XYBarRenderer)renderer).setDrawBarOutline(false);
	                            ((XYBarRenderer)renderer).setShadowVisible(false);
	                        }
	                    }
					    else
					    {
    						if (isXString)
    						{
    							renderer = new BarRenderer();
                                ((BarRenderer)renderer).setBarPainter(new StandardBarPainter());
                                ((BarRenderer)renderer).setItemMargin(0.1d);
                                ((BarRenderer)renderer).setShadowVisible(false);
    						}
    						else
    						{
    							renderer = new ClusteredXYBarRenderer(0.1d, true);
                                ((ClusteredXYBarRenderer)renderer).setBarPainter(new StandardXYBarPainter());
                                ((ClusteredXYBarRenderer)renderer).setShadowVisible(false);
    						}
					    }
					}
                    else if (getSubStyle() == STYLE_STEPLINES)
                    {
                        if (isXString)
                        {
                            renderer = new CategoryStepRenderer();
                        }
                        else
                        {
                            renderer = new XYStepRenderer();
//                            ((XYStepRenderer)renderer).setStepPoint(0);
                        }
                    }
					else // if (chartStyle == STYLE_LINES)
					{
						if (isXString)
						{
							renderer = new LineAndShapeRenderer(true, true);
						}
						else
						{
							renderer = new XYLineAndShapeRenderer(true, true);
						}
					}
					
					if (isXDate)
					{
						vaX = new DateAxis(sXTitle);
					}
					else if (isXString)
					{
						vaX = new CategoryAxis(sXTitle);
						if (getBaseStyle() != STYLE_BARS)
						{
							((CategoryAxis)vaX).setCategoryMargin(0);
						}
					}
					else
					{
						vaX = new NumberAxis(sXTitle);
				        ((NumberAxis)vaX).setAutoRangeIncludesZero(false);
					}
					
			        vaY = new NumberAxis(sYTitle);
                    if (getSubStyle() == STYLE_STACKEDPERCENTBARS || getSubStyle() == STYLE_STACKEDPERCENTAREA)
                    {
                        ((NumberAxis)vaY).setNumberFormatOverride(new DecimalFormat("#%"));
                    }
                    else
                    {
                        ((NumberAxis)vaY).setNumberFormatOverride(null);
                    }
			        
			        if (isXString)
			        {
			        	plot = new CategoryPlot((CategoryDataset)dataset, (CategoryAxis)vaX, (ValueAxis)vaY, (CategoryItemRenderer)renderer);
                        if (isHorizontalStyle())
                        {
                            ((CategoryPlot)plot).setOrientation(PlotOrientation.HORIZONTAL);
                        }
			        }
			        else
			        {
			        	plot = new XYPlot((XYDataset)dataset, (ValueAxis)vaX, (ValueAxis)vaY, (XYItemRenderer)renderer);
			        	
                        if (isHorizontalStyle())
	                    {
	                        ((XYPlot)plot).setOrientation(PlotOrientation.HORIZONTAL);
	                    }
			        }
			        
//			        if (chartStyle == STYLE_AREA || chartStyle == STYLE_OVERLAPPEDBARS)
//			        {
//			        	plot.setForegroundAlpha(0.80F);
//			        }
	
			        if (renderer instanceof XYItemRenderer)
			        {
				        ((XYItemRenderer)renderer).setBaseToolTipGenerator(new StandardXYToolTipGenerator());
				        ((XYItemRenderer)renderer).setURLGenerator(new StandardXYURLGenerator());
			        }
			        else if (renderer instanceof AbstractCategoryItemRenderer)
			        {
			        	((AbstractCategoryItemRenderer)renderer).setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			        }
				}
                plot.setForegroundAlpha(0.75f);
				
				JFreeChart chart = new JFreeChart(sTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
				
				// Hide the legend if it is a pie chart.
				chart.getLegend().setVisible(getBaseStyle() != STYLE_PIE);
				
				chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new Dimension(75, 50));
				chartPanel.addChartMouseListener(this);
				
	            if (vaX != null)
	            {
	                vaX.setLabel(translate(sXTitle));
	            }
	            
	            if (vaY != null)
	            {
	                vaY.setLabel(translate(sYTitle));
	            }

	            chartPanel.getChart().setTitle(translate(sTitle));
				
				add(chartPanel, BorderLayout.CENTER);
				
				dataBook.addControl(this);
				
				notifyRepaint();
			}
			catch (ModelException ex)
			{
				// Do Nothing
			}
		}
	}

	/**
	 * Gets the ChartPanel displayed, or null if non is displayed.
	 * @return the ChartPanel.
	 */
	public ChartPanel getChartPanel()
	{
		return chartPanel;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link DataBookCategoryDataset} extends the
	 * {@link DefaultCategoryDataset} and provides data from an
	 * {@link IDataBook}.
	 * 
	 * @author Robert Zenz
	 */
	public static class DataBookCategoryDataset extends DefaultCategoryDataset
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	    /** The associated chart. */
	    private JVxChart chart;
		
	    /** The DataBook that serves as data source. */
	    private IDataBook dataBook;
	    
	    /** Column name for the x axis. */
	    private String xColumnName;
	    
	    /** Column names for the y axis. */
	    private String[] yColumnNames;
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
		/**
		 * Creates a new instance of {@link DataBookCategoryDataset}.
		 *
		 * @param pDatabook the {@link IDataBook databook}.
		 * @param pXColumnName the {@link String X column name}.
		 * @param pYColumnNames the {@link String Y column names}.
		 * @param pChart the associated chart.
		 */
    	public DataBookCategoryDataset(IDataBook pDatabook, String pXColumnName, String[] pYColumnNames, JVxChart pChart)
		{
			dataBook = pDatabook;
			xColumnName = pXColumnName;
			yColumnNames = pYColumnNames;
			chart = pChart;
		}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
		/**
	     * {@inheritDoc}
	     */
	    @Override
		public void validateObject() throws InvalidObjectException
		{
			clear();
			
			try
			{
			    PiePlot piePlot = null;
                if (chart.getBaseStyle() == STYLE_PIE)
                {
			        piePlot = (PiePlot)chart.chartPanel.getChart().getPlot();
                }
			    
				if (piePlot != null && yColumnNames.length > 1)
				{
					String rowKey = chart.translate(dataBook.getValueAsString(xColumnName));
					if (rowKey == null)
					{
					    rowKey = "";
					}
					
					for (String yColumnName : yColumnNames)
					{
						String yColumnTitle = getColumnLabel(yColumnName);
						BigDecimal value = (BigDecimal)dataBook.getValue(yColumnName);
						
						addValue(value, rowKey, yColumnTitle);

						piePlot.setExplodePercent(yColumnTitle, 0.0d);
					}
					
					if (ArrayUtil.contains(yColumnNames, dataBook.getSelectedColumn()))
					{
					    piePlot.setExplodePercent(getColumnLabel(dataBook.getSelectedColumn()), 0.1d);
					}
				}
				else
				{
					dataBook.fetchAll();
					
					for (int index = 0; index < dataBook.getRowCount(); index++)
					{
						IDataRow dataRow = dataBook.getDataRow(index);
						String rowKey =  chart.translate(dataRow.getValueAsString(xColumnName));
						if (rowKey == null)
						{
						    rowKey = "";
						}

						for (String yColumnName : yColumnNames)
						{
							String yColumnTitle = getColumnLabel(yColumnName);
							
							BigDecimal value = (BigDecimal)dataRow.getValue(yColumnName);

                            BigDecimal oldValue;
                            try
                            {
                                oldValue = (BigDecimal)getValue(yColumnTitle, rowKey);
                            }
                            catch (Exception ex)
                            {
                                oldValue = null;
                            }
							if (oldValue == null)
							{
							    addValue(value, yColumnTitle, rowKey);
							}
							else if (value != null)
							{
							    addValue(oldValue.add(value), yColumnTitle, rowKey);
							}

                            if (piePlot != null)
							{
								piePlot.setExplodePercent(rowKey, 0.0d);
							}
						}
					}

					if (piePlot != null)
					{
					    String rowKey =  chart.translate(dataBook.getValueAsString(xColumnName));
					    if (rowKey != null)
					    {
					        piePlot.setExplodePercent(rowKey, 0.1d);
					    }
					}
				}
			}
			catch (ModelException e)
			{
			    e.printStackTrace();
				throw new InvalidObjectException(e.getMessage());
			}
			
			super.validateObject();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the label for the given column.
		 * 
		 * @param pColumnName the name of the column.
		 * @return the label of the column.
		 * @throws ModelException if accessing the column information failed.
		 */
		private String getColumnLabel(String pColumnName) throws ModelException
		{
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(pColumnName);
			
			String columnLabel = columnDefinition.getLabel();
			
			if (columnLabel == null)
			{
				columnLabel = columnDefinition.getDefaultLabel();
			}
			
			columnLabel = chart.translate(columnLabel);
			
			return columnLabel;
		}
	
	}	// DataBookCategoryDataset
	
	/**
	 * This class provides an {@link AbstractIntervalXYDataset} implementation for a DataBook.  
	 */
	public static class DataBookXYDataset extends AbstractIntervalXYDataset 
	                                      implements XYDataset, 
	                                                 TableXYDataset 
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The range. */
		private static double percentage = 0.9d;
		
	    /** The associated chart. */
	    private JVxChart chart;

	    /** The DataBook that serves as data source. */
	    private IDataBook dataBook;

	    /** Column name for the x axis. */
	    private String xColumnName;
	    
	    /** Column names for the y axis. */
	    private String[] yColumnNames;

	    /** minimal difference of x Values. */
	    private BigDecimal minXDiff = null;

	    /** If there is a date axis. */
	    private boolean bDateAxis;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Creates a new JDBCXYDataset (initially empty) with no database
	     * connection.
	     * 
	     * @param pDataBook the DataBook
	     * @param pXColumnName the x column name
	     * @param pYColumnNames the y column names
	     * @param pChart the chart
	     * 
	     * @throws ModelException if an Exception occurs.
	     */
	    public DataBookXYDataset(IDataBook pDataBook, String pXColumnName, String[] pYColumnNames, JVxChart pChart) throws ModelException
	    {
	    	dataBook = pDataBook;
	    	
	    	xColumnName = pXColumnName;
	    	yColumnNames = pYColumnNames; 
	    	
	    	chart = pChart;
    		bDateAxis = Date.class.isAssignableFrom(dataBook.getRowDefinition().getColumnDefinition(xColumnName).getDataType().getTypeClass());
	    }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
	     * {@inheritDoc}
	     */
	    @Override
		public void validateObject() throws InvalidObjectException
		{
	    	minXDiff = null;
	    	
			super.validateObject();
		}
	    
	    /**
	     * {@inheritDoc}
	     */
	    public Number getX(int pSeriesIndex, int pItemIndex)
	    {
	    	try
	    	{
		    	IDataRow dataRow = dataBook.getDataRow(pItemIndex);

		    	if (bDateAxis)
		    	{
		    		return Long.valueOf(((Date)dataRow.getValue(xColumnName)).getTime());
		    	}
		    	else
		    	{
		    		return (Number)dataRow.getValue(xColumnName);
		    	}
	    	}
	    	catch (Exception ex)
	    	{
	    		return null;
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public Number getY(int pSeriesIndex, int pItemIndex) 
	    {
	    	try
	    	{
		    	IDataRow dataRow = dataBook.getDataRow(pItemIndex);

		        return (Number)dataRow.getValue(yColumnNames[pSeriesIndex]);
	    	}
	    	catch (Exception ex)
	    	{
	    		return null;
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
		public Number getStartX(int pSeriesIndex, int pItemIndex)
		{
			return getX(pSeriesIndex, pItemIndex);
		}

	    /**
	     * {@inheritDoc}
	     */
		public Number getEndX(int pSeriesIndex, int pItemIndex)
		{
			if (minXDiff == null)
			{
				int count = getItemCount();
				
				Number value = getX(pSeriesIndex, 0);
				
				for (int i = 1; i < count; i++)
				{
					Number nextValue = getX(pSeriesIndex, i);
					
					if (nextValue != null)
					{
						if (value != null)
						{
							double diff = Math.abs(nextValue.doubleValue() - value.doubleValue());
							
							if (minXDiff == null || diff < minXDiff.doubleValue())
							{
								minXDiff = BigDecimal.valueOf(diff);
							}
						}
						value = nextValue;
					}
				}
				
				if (minXDiff == null)
				{
					if (value == null || value.doubleValue() == 0)
					{
						minXDiff = BigDecimal.valueOf(1);
					}
					else
					{
						minXDiff = BigDecimal.valueOf(value.doubleValue());
					}
					
				}
			}
			
			Number startX = getX(pSeriesIndex, pItemIndex);
			
			if (startX == null)
			{
				return null;
			}
			else
			{
				return BigDecimal.valueOf(startX.doubleValue() + minXDiff.doubleValue() * percentage);
			}
		}

	    /**
	     * {@inheritDoc}
	     */
		public Number getStartY(int pSeriesIndex, int pItemIndex)
		{
			return getY(pSeriesIndex, pItemIndex);
		}

	    /**
	     * {@inheritDoc}
	     */
		public Number getEndY(int pSeriesIndex, int pItemIndex)
		{
			double start = getY(pSeriesIndex, pItemIndex).doubleValue();
			
			double diff;
			if (pItemIndex < getItemCount() - 1)
			{
				diff = Math.abs(getY(pSeriesIndex, pItemIndex + 1).doubleValue() - start);
			}
			else if (pItemIndex == 0)
			{
				if (start == 0)
				{
					diff = start;
				}
				else
				{
					diff = 1;
				}
			}
			else
			{
				diff = Math.abs(start - getY(pSeriesIndex, pItemIndex - 1).doubleValue());
			}
			
			return BigDecimal.valueOf(start + diff * percentage);
		}

	    /**
	     * {@inheritDoc}
	     */
	    public int getItemCount(int seriesIndex) 
	    {
	    	return getItemCount();
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public int getItemCount() 
	    {
	    	try
	    	{
	    		dataBook.fetchAll();
	    		
	    		return dataBook.getRowCount();
	    	}
	    	catch (Exception ex)
	    	{
	    		return 0;
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public int getSeriesCount() 
	    {
	        return yColumnNames.length;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public Comparable getSeriesKey(int pSeriesIndex) 
	    {
	    	try
	    	{
		        return chart.translate(dataBook.getRowDefinition().getColumnDefinition(yColumnNames[pSeriesIndex]).getLabel());
	    	}
	    	catch (Exception ex)
	    	{
		    	return chart.translate(yColumnNames[pSeriesIndex]);
	    	}
	    }
	    
	}	// DataBookXYDataset

	/**
	 * Fixed bar width on StackedBarRenderer. 
	 * 
	 * @author Martin Handsteiner
	 */
	public static class FixedStackedBarRenderer extends StackedBarRenderer
	{
	    /**
	     * Constructs a <code>FixedStackedBarRenderer</code>.
	     */
	    public FixedStackedBarRenderer()
	    {
	        super();
	    }
	    
        /**
         * Constructs a <code>FixedStackedBarRenderer</code>.
         * 
         * @param pRenderAsPercentage render as percentage.
         */
        public FixedStackedBarRenderer(boolean pRenderAsPercentage)
        {
            super(pRenderAsPercentage);
        }

        /**
         * Corrects the bar width calculation.
         */
        @Override
        protected void calculateBarWidth(CategoryPlot pPlot, Rectangle2D pDataArea, int pRendererIndex, CategoryItemRendererState pState) 
        {
            CategoryAxis domainAxis = getDomainAxis(pPlot, pRendererIndex);
            CategoryDataset dset = pPlot.getDataset(pRendererIndex);
            if (dset != null) 
            {
               int columns = dset.getColumnCount();
               int rows = dset.getRowCount();
               double space = 0.0D;
               PlotOrientation orientation = pPlot.getOrientation();
               if (orientation == PlotOrientation.HORIZONTAL) 
               {
                  space = pDataArea.getHeight();
               } 
               else if (orientation == PlotOrientation.VERTICAL) 
               {
                  space = pDataArea.getWidth();
               }

               double maxWidth = space * getMaximumBarWidth();
               double categoryMargin = 0.0D;
               double currentItemMargin = 0.0D;
               if (columns > 1) 
               {
                  categoryMargin = domainAxis.getCategoryMargin();
               }
               if (rows > 1) 
               {
                  currentItemMargin = this.getItemMargin();
               }

               double used = space * (1.0D - domainAxis.getLowerMargin() - domainAxis.getUpperMargin() - categoryMargin - currentItemMargin);
               if (rows * columns > 0) 
               {
                  pState.setBarWidth(Math.min(used / (double)columns, maxWidth));
               } 
               else 
               {
                  pState.setBarWidth(Math.min(used, maxWidth));
               }
            }
        }
	}
	
    /**
     * Fixed bar width on LayeredBarRenderer. 
     * 
     * @author Martin Handsteiner
     */
    public static class FixedLayeredBarRenderer extends LayeredBarRenderer
    {
        /**
         * Corrects the bar width calculation.
         */
        @Override
        protected void calculateBarWidth(CategoryPlot pPlot, Rectangle2D pDataArea, int pRendererIndex, CategoryItemRendererState pState) 
        {
            CategoryAxis domainAxis = getDomainAxis(pPlot, pRendererIndex);
            CategoryDataset dset = pPlot.getDataset(pRendererIndex);
            if (dset != null) 
            {
               int columns = dset.getColumnCount();
               int rows = dset.getRowCount();
               double space = 0.0D;
               PlotOrientation orientation = pPlot.getOrientation();
               if (orientation == PlotOrientation.HORIZONTAL) 
               {
                  space = pDataArea.getHeight();
               } 
               else if (orientation == PlotOrientation.VERTICAL) 
               {
                  space = pDataArea.getWidth();
               }
    
               double maxWidth = space * getMaximumBarWidth();
               double categoryMargin = 0.0D;
               double currentItemMargin = 0.0D;
               if (columns > 1) 
               {
                  categoryMargin = domainAxis.getCategoryMargin();
               }
               if (rows > 1) 
               {
                  currentItemMargin = getItemMargin();
               }
    
               double used = space * (1.0D - domainAxis.getLowerMargin() - domainAxis.getUpperMargin() - categoryMargin - currentItemMargin);
               if (rows * columns > 0) 
               {
                  pState.setBarWidth(Math.min(used / (double)columns, maxWidth));
               } 
               else 
               {
                  pState.setBarWidth(Math.min(used, maxWidth));
               }
            }
        }
    }
    
}	// JVxChart
