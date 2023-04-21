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
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.InvalidObjectException;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.IEditorControl;
import javax.rad.util.TranslationMap;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CenterTextMode;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.TextAnchor;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JVxGauge</code> is a simple gauge panel which also implements the {@link IEditorControl}
 * interface.
 *  
 * @author Martin Handsteiner
 */
public class JVxGauge extends JPanel
                      implements IEditorControl,
                                 Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Style constant for showing a speedometer. */
	public static final int STYLE_SPEEDOMETER = 0;
	
	/** Style constant for showing an meter. */
	public static final int STYLE_METER = 1;
	
	/** Style constant for showing a flat. */
	public static final int STYLE_RING = 2;
    
    /** Style constant for showing a flat. */
    public static final int STYLE_FLAT = 3;

    /** Transparent. */
    protected static final Color TRANSPARENT = new Color(0, true);
    
    /** Light green. */
    private static final Color LIGHT_GREEN = new Color(0, 255, 0, 128);     // new Color(85, 191, 59)   // #55BF3B
    /** Light orange. */
    private static final Color LIGHT_ORANGE = new Color(255, 200, 0, 128);  // new Color(221, 223, 13)  // #DDDF0D
    /** Light red. */
    private static final Color LIGHT_RED = new Color(255, 0, 0, 128);       // new Color(223, 83, 83)   // #DF5353
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The chart panel. */
	private ChartPanel chartPanel = null;

	/** The dataset. */
	private AbstractDataset dataset = null;

	/** The DataBook to be shown. */
	private IDataRow dataRow = null;
	
	/** The gauge style. */
	private int gaugeStyle = STYLE_SPEEDOMETER;
	
	/** The title. */
	private String sTitle = null;
	
	/** The column name for the value. */
	private String sColumnName = null;
	
	/** The (optional) column name for the label. */
	private String sColumnNameLabel = null;
	
	/** The min value. */
	private double minValue = 0;
    /** The max value. */
    private double maxValue = 100;
    /** The min warning value. */
    private double minWarningValue = Double.NaN;
    /** The max warning value. */
    private double maxWarningValue = Double.NaN;
    /** The min error value. */
    private double minErrorValue = Double.NaN;
    /** The max error value. */
    private double maxErrorValue = Double.NaN;
	
    /** The translation mapping. */
    private TranslationMap tmpTranslation = null;
    
	/** Tells, if notifyRepaint is called the first time. */
	private boolean bFirstNotifyRepaintCall = true;
	
    /** Whether the translation is enabled. */
    private boolean bTranslationEnabled = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a <code>JVxGauge</code>.
	 */
	public JVxGauge()
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

    //ITranslatable
    
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        if (tmpTranslation != pTranslation)
        {
            tmpTranslation = pTranslation;

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
    
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Gets the DataRow shown by this control.
     *
     * @return the DataRow.
     * @see #setDataRow
     */
	public IDataRow getDataRow()
	{
		return dataRow;
	}
	
	/**
     * Sets the DataRow shown by this control.
     *
     * @param pDataRow the DataRow.
     * @see #getDataRow
     */
	public void setDataRow(IDataRow pDataRow)
	{
		uninstallGauge();
		
		dataRow = pDataRow;
		
		installGauge();
	}
	
	/**
	 * Gets the style of the gauge.
	 * 
     * @return the gauge style.
     */
	public int getGaugeStyle()
	{
		return gaugeStyle;
	}

	/**
	 * Sets the style of the gauge.
	 * 
	 * @param pGaugeStyle the gauge style.
	 */
	public void setGaugeStyle(int pGaugeStyle)
	{
		if (pGaugeStyle != gaugeStyle)
		{
			uninstallGauge();
			
			gaugeStyle = pGaugeStyle;
	
			installGauge();
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
	 * Gets the x column name.
	 * 
	 * @return the x column name.
	 */
	public String getColumnName()
	{
		return sColumnName;
	}

	/**
	 * Sets the column name.
	 * 
	 * @param pColumnName the column name.
	 */
	public void setColumnName(String pColumnName)
	{
		uninstallGauge();
		
		sColumnName = pColumnName;
		
		installGauge();
	}
	
    /**
     * Gets the column name which will be used for the label.
     * 
     * @return the column name
     */
    public String getLabelColumnName()
    {
    	return sColumnNameLabel;
    }	

    /**
     * Sets the column name which should be used for the label.
     * 
     * @param pColumnName the column name
     */
    public void setLabelColumnName(String pColumnName)
    {
		uninstallGauge();

		sColumnNameLabel = pColumnName;
		
		installGauge();
    }

    /**
     * Gets the min value.
     * 
     * @return the min value.
     */
	public double getMinValue()
    {
        return minValue;
    }

	/**
     * Gets the max value.
     * 
     * @param pMinValue the max value.
     */
    public void setMinValue(double pMinValue)
    {
        uninstallGauge();
        
        if (Double.isNaN(pMinValue))
        {
            minValue = 0;
        }
        else
        {
            minValue = pMinValue;
        }
        
        installGauge();
    }

    /**
     * Gets the max value.
     * 
     * @return the max value.
     */
    public double getMaxValue()
    {
        return maxValue;
    }

    /**
     * Gets the min value.
     * 
     * @param pMaxValue the min value.
     */
    public void setMaxValue(double pMaxValue)
    {
        uninstallGauge();
        
        if (Double.isNaN(pMaxValue))
        {
            maxValue = 100;
        }
        else
        {
            maxValue = pMaxValue;
        }
        
        
        installGauge();
    }

    /**
     * Gets the min warning value.
     * 
     * @return the min warning value.
     */
    public double getMinWarningValue()
    {
        return minWarningValue;
    }

    /**
     * Gets the min warning value.
     * 
     * @param pMinWarningValue the min warning value.
     */
    public void setMinWarningValue(double pMinWarningValue)
    {
        uninstallGauge();
        
        minWarningValue = pMinWarningValue;
        
        installGauge();
    }

    /**
     * Gets the max value.
     * 
     * @return the max value.
     */
    public double getMaxWarningValue()
    {
        return maxWarningValue;
    }

    /**
     * Gets the max warning value.
     * 
     * @param pMaxWarningValue the max warning value.
     */
    public void setMaxWarningValue(double pMaxWarningValue)
    {
        uninstallGauge();
        
        maxWarningValue = pMaxWarningValue;
        
        installGauge();
    }

    /**
     * Gets the min error value.
     * 
     * @return the min error value.
     */
    public double getMinErrorValue()
    {
        return minErrorValue;
    }

    /**
     * Gets the min error value.
     * 
     * @param pMinErrorValue the min error value.
     */
    public void setMinErrorValue(double pMinErrorValue)
    {
        uninstallGauge();
        
        minErrorValue = pMinErrorValue;
        
        installGauge();
    }

    /**
     * Gets the max error value.
     * 
     * @return the max error value.
     */
    public double getMaxErrorValue()
    {
        return maxErrorValue;
    }

    /**
     * Gets the max error value.
     * 
     * @param pMaxErrorValue the max error value.
     */
    public void setMaxErrorValue(double pMaxErrorValue)
    {
        uninstallGauge();
        
        maxErrorValue = pMaxErrorValue;
        
        installGauge();
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
	 * Uninstalls the CellEditor and its CellEditorComponent.
	 */
	private void uninstallGauge()
	{
		if (chartPanel != null)
		{
			dataRow.removeControl(this);
			
			remove(chartPanel);
			
			chartPanel = null;
			dataset = null;
		}
	}

	/**
	 * Installs the CellEditor and its CellEditorComponent.
	 */
	private void installGauge()
	{
		if (dataRow != null && sColumnName != null)
		{
			try
			{
				Plot plot = null;
				
				if (getGaugeStyle() == STYLE_METER)
                {
                    plot = new DialPlot();
                    dataset = new DataRowValueDataset(dataRow, sColumnName);

                    ((DialPlot)plot).setDataset((ValueDataset)dataset);

                    ((DialPlot)plot).setBackground(new DialBackground());
                    ((DialPlot)plot).setView(0.21d, 0.0d, 0.58d, 0.3d);
                    
                    ArcDialFrame dialFrame = new ArcDialFrame(60.0d, 60.0d);
                    dialFrame.setInnerRadius(0.6d);
                    dialFrame.setOuterRadius(0.9d);
                    dialFrame.setForegroundPaint(Color.darkGray);
                    dialFrame.setStroke(new BasicStroke(3.0f));
                    ((DialPlot)plot).setDialFrame(dialFrame);
                    
                    double minVal = getMinValue();
                    if (!Double.isNaN(getMinErrorValue()))
                    {
                        StandardDialRange range = new StandardDialRange(minVal, getMinErrorValue(), LIGHT_RED);
                        range.setInnerRadius(0.875d);
                        range.setOuterRadius(0.88d);
                        ((DialPlot)plot).addLayer(range);
                        
                        minVal = getMinErrorValue();
                    }
                    if (!Double.isNaN(getMinWarningValue()))
                    {
                        StandardDialRange range = new StandardDialRange(minVal, getMinWarningValue(), LIGHT_ORANGE);
                        range.setInnerRadius(0.875d);
                        range.setOuterRadius(0.88d);
                        ((DialPlot)plot).addLayer(range);
                        
                        minVal = getMinWarningValue();
                    }
                    double maxVal = getMaxValue();
                    if (!Double.isNaN(getMaxErrorValue()))
                    {
                        StandardDialRange range = new StandardDialRange(getMaxErrorValue(), maxVal, LIGHT_RED);
                        range.setInnerRadius(0.875d);
                        range.setOuterRadius(0.88d);
                        ((DialPlot)plot).addLayer(range);
                        
                        maxVal = getMaxErrorValue();
                    }
                    if (!Double.isNaN(getMaxWarningValue()))
                    {
                        StandardDialRange range = new StandardDialRange(getMaxWarningValue(), maxVal, LIGHT_ORANGE);
                        range.setInnerRadius(0.875d);
                        range.setOuterRadius(0.88d);
                        ((DialPlot)plot).addLayer(range);
                        
                        maxVal = getMaxWarningValue();
                    }
                    StandardDialRange range = new StandardDialRange(minVal, maxVal, LIGHT_GREEN);
                    range.setInnerRadius(0.875d);
                    range.setOuterRadius(0.88d);
                    ((DialPlot)plot).addLayer(range);
                    
                    String axisText = dataRow.getRowDefinition().getColumnDefinition(sColumnName).getLabel();
                    DialTextAnnotation annotation = new DialTextAnnotation(translate(axisText));
                    annotation.setFont(new Font("Dialog", 1, 14));
                    annotation.setAngle(90);
                    annotation.setRadius(0.66d);
                    ((DialPlot)plot).addLayer(annotation);

                    DialValueIndicator dvi = new DialValueIndicator(0);
                    dvi.setAngle(90);
                    dvi.setRadius(0.7d);
                    ((DialPlot)plot).addLayer(dvi);

                    StandardDialScale scale = new StandardDialScale(getMinValue(), getMaxValue(), 115, -50, (getMaxValue() - getMinValue()) / 4, 4);
                    scale.setTickRadius(0.87d);
                    scale.setTickLabelOffset(0.07d);
                    scale.setTickLabelFont(new Font("Dialog", 0, 14));
                    ((DialPlot)plot).addScale(0, scale);
                    
                    DialPointer needle = new DialPointer.Pin();
                    needle.setRadius(0.84D);
                    ((DialPlot)plot).addPointer(needle);
                }
                else if (getGaugeStyle() == STYLE_RING)
                {
                    plot = new RingPlot();
                    dataset = new DataRowPieDataset(dataRow, sColumnName, sColumnNameLabel, this, (RingPlot)plot);
                    
                    ((RingPlot)plot).setDataset((PieDataset)dataset);

                    ((RingPlot)plot).setCenterTextMode(CenterTextMode.FIXED);
                    ((RingPlot)plot).setCenterTextFont(new Font("SansSerif", 1, 18));
                    ((RingPlot)plot).setCenterTextColor(Color.GRAY);
                   
                    plot.setBackgroundPaint(null);
                    plot.setOutlineVisible(false);
                    ((RingPlot)plot).setLabelGenerator(null);
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_GREEN);
                    ((RingPlot)plot).setSectionPaint("EMPTY", Color.gray);
                    ((RingPlot)plot).setSectionDepth(0.35D);
                    ((RingPlot)plot).setSeparatorsVisible(false);
                    ((RingPlot)plot).setSectionOutlinesVisible(false);
                    ((RingPlot)plot).setShadowPaint(null);
                }
				else if (getGaugeStyle() == STYLE_FLAT)
				{
                    plot = new HalfRingPlot();
                    dataset = new DataRowPieDataset(dataRow, sColumnName, sColumnNameLabel, this, (RingPlot)plot);
                    
                    ((RingPlot)plot).setDataset((PieDataset)dataset);

                    ((RingPlot)plot).setCenterTextMode(CenterTextMode.FIXED);
                    ((RingPlot)plot).setCenterTextFont(new Font("SansSerif", 1, 18));
                    ((RingPlot)plot).setCenterTextColor(Color.GRAY);
                   
                    plot.setBackgroundPaint(null);
                    plot.setOutlineVisible(false);
                    ((RingPlot)plot).setLabelGenerator(null);
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_GREEN);
                    ((RingPlot)plot).setSectionPaint("EMPTY", Color.gray);
                    ((RingPlot)plot).setSectionDepth(0.35D);
                    ((RingPlot)plot).setSeparatorsVisible(false);
                    ((RingPlot)plot).setSectionOutlinesVisible(false);
                    ((RingPlot)plot).setShadowPaint(null);
				    
				    // We do not use MeterPlot for now, as it is not really beautiful.
//                    plot = new FixedMeterPlot();
//                    dataset = new DataRowValueDataset(dataRow, sColumnName);
//
//                    ((MeterPlot)plot).setDataset((ValueDataset)dataset);
//                    
//				    ((MeterPlot)plot).setDialShape(DialShape.CHORD);
//				    ((MeterPlot)plot).setRange(new Range(getMinValue(), getMaxValue()));
//				    
//                    double minVal = getMinValue();
//                    if (!Double.isNaN(getMinErrorValue()))
//                    {
//                        ((MeterPlot)plot).addInterval(
//                                new MeterInterval(translate("Critical"), new Range(minVal, getMinErrorValue()), TRANSPARENT, null, LIGHT_RED));
//                        
//                        minVal = getMinErrorValue();
//                    }
//                    if (!Double.isNaN(getMinWarningValue()))
//                    {
//                        ((MeterPlot)plot).addInterval(
//                                new MeterInterval(translate("Warning"), new Range(minVal, getMinWarningValue()), TRANSPARENT, null, LIGHT_ORANGE));
//                        
//                        minVal = getMinWarningValue();
//                    }
//                    double maxVal = getMaxValue();
//                    if (!Double.isNaN(getMaxErrorValue()))
//                    {
//                        ((MeterPlot)plot).addInterval(
//                                new MeterInterval(translate("Critical"), new Range(getMaxErrorValue(), maxVal), TRANSPARENT, null, LIGHT_RED));
//                        
//                        maxVal = getMaxErrorValue();
//                    }
//                    if (!Double.isNaN(getMaxWarningValue()))
//                    {
//                        ((MeterPlot)plot).addInterval(
//                                new MeterInterval(translate("Warning"), new Range(getMaxWarningValue(), maxVal), TRANSPARENT, null, LIGHT_ORANGE));
//                        
//                        maxVal = getMaxWarningValue();
//                    }
//                    ((MeterPlot)plot).addInterval(
//                            new MeterInterval(translate("Normal"), new Range(minVal, maxVal), TRANSPARENT, null, LIGHT_GREEN));
//				    
//				    ((MeterPlot)plot).setNeedlePaint(Color.darkGray);
//				    ((MeterPlot)plot).setDialBackgroundPaint(Color.white);
//				    ((MeterPlot)plot).setDialOutlinePaint(null);
//				    ((MeterPlot)plot).setMeterAngle(260);
//				    ((MeterPlot)plot).setTickLabelsVisible(true);
//				    ((MeterPlot)plot).setTickLabelFont(new Font("Dialog", 1, 14));
//				    ((MeterPlot)plot).setTickLabelPaint(Color.darkGray);
//				    ((MeterPlot)plot).setTickSize(1.0d);
//				    ((MeterPlot)plot).setTickPaint(TRANSPARENT);
//				    
//				    String axisText = dataRow.getRowDefinition().getColumnDefinition(sColumnName).getLabel();
//				    ((MeterPlot)plot).setUnits(translate(axisText));
//                    ((MeterPlot)plot).setValuePaint(Color.darkGray);
//				    ((MeterPlot)plot).setValueFont(new Font("Dialog", 1, 17));
				}
				else // if (getGaugeStyle() == STYLE_SPEEDOMETER)
                {
                    plot = new DialPlot();
                    dataset = new DataRowValueDataset(dataRow, sColumnName);

                    ((DialPlot)plot).setDataset((ValueDataset)dataset);

                    ((DialPlot)plot).setBackground(new DialBackground());

                    ((DialPlot)plot).setDialFrame(new StandardDialFrame());
                    
                    double minVal = getMinValue();
                    if (!Double.isNaN(getMinErrorValue()))
                    {
                        StandardDialRange range = new StandardDialRange(minVal, getMinErrorValue(), LIGHT_RED);
                        range.setInnerRadius(0.83d);
                        range.setOuterRadius(0.84d);
                        ((DialPlot)plot).addLayer(range);
                        
                        minVal = getMinErrorValue();
                    }
                    if (!Double.isNaN(getMinWarningValue()))
                    {
                        StandardDialRange range = new StandardDialRange(minVal, getMinWarningValue(), LIGHT_ORANGE);
                        range.setInnerRadius(0.83d);
                        range.setOuterRadius(0.84d);
                        ((DialPlot)plot).addLayer(range);
                        
                        minVal = getMinWarningValue();
                    }
                    double maxVal = getMaxValue();
                    if (!Double.isNaN(getMaxErrorValue()))
                    {
                        StandardDialRange range = new StandardDialRange(getMaxErrorValue(), maxVal, LIGHT_RED);
                        range.setInnerRadius(0.83d);
                        range.setOuterRadius(0.84d);
                        ((DialPlot)plot).addLayer(range);
                        
                        maxVal = getMaxErrorValue();
                    }
                    if (!Double.isNaN(getMaxWarningValue()))
                    {
                        StandardDialRange range = new StandardDialRange(getMaxWarningValue(), maxVal, LIGHT_ORANGE);
                        range.setInnerRadius(0.83d);
                        range.setOuterRadius(0.84d);
                        ((DialPlot)plot).addLayer(range);
                        
                        maxVal = getMaxWarningValue();
                    }
                    StandardDialRange range = new StandardDialRange(minVal, maxVal, LIGHT_GREEN);
                    range.setInnerRadius(0.83d);
                    range.setOuterRadius(0.84d);
                    ((DialPlot)plot).addLayer(range);
                    
                    String axisText = dataRow.getRowDefinition().getColumnDefinition(sColumnName).getLabel();
                    DialTextAnnotation annotation = new DialTextAnnotation(translate(axisText));
                    annotation.setFont(new Font("Dialog", 1, 14));
                    annotation.setRadius(0.7d);
                    ((DialPlot)plot).addLayer(annotation);

                    DialValueIndicator dvi = new DialValueIndicator(0);
                    dvi.setRadius(0.5d);
                    ((DialPlot)plot).addLayer(dvi);

                    StandardDialScale scale = new StandardDialScale(getMinValue(), getMaxValue(), -120, -300, (getMaxValue() - getMinValue()) / 10, 3);
                    scale.setTickRadius(0.92d);
                    scale.setTickLabelOffset(0.18d);
                    scale.setTickLabelFont(new Font("Dialog", 0, 14));
                    ((DialPlot)plot).addScale(0, scale);
                    
                    ((DialPlot)plot).addPointer(new DialPointer.Pointer());
                    
                    DialCap cap = new DialCap();
                    ((DialPlot)plot).setCap(cap);
                }
//                plot.setForegroundAlpha(0.75f);
				
				JFreeChart chart = new JFreeChart(sTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
				
				// Hide the legend if it is a pie chart.
//				chart.getLegend().setVisible(getBaseStyle() != STYLE_PIE);
				
				chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(null);
                
                chartPanel.getChart().setTitle(translate(sTitle));

                add(chartPanel, BorderLayout.CENTER);
				
				dataRow.addControl(this);
				
				notifyRepaint();
			}
			catch (Exception ex)
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
     * The {@link DataRowValueDataset} extends the
     * {@link DefaultValueDataset} and provides data from an
     * {@link IDataRow}.
     * 
     * @author Martin Handsteiner
     */
    public static class DataRowValueDataset extends DefaultValueDataset
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** The DataBook that serves as data source. */
        private IDataRow dataRow;
        
        /** Column name for the x axis. */
        private String columnName;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of {@link DataRowValueDataset}.
         *
         * @param pDataRow the {@link IDataBook databook}.
         * @param pColumnName the {@link String X column name}.
         */
        public DataRowValueDataset(IDataRow pDataRow, String pColumnName)
        {
            dataRow = pDataRow;
            columnName = pColumnName;
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
            try
            {
                setValue((Number)dataRow.getValue(columnName));
            }
            catch (ModelException e)
            {
                throw new InvalidObjectException(e.getMessage());
            }
            
            super.validateObject();
        }
        
    }   // DataRowValueDataset

	/**
	 * The {@link DataRowPieDataset} extends the
	 * {@link DefaultPieDataset} and provides data from an
	 * {@link IDataRow}.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class DataRowPieDataset extends DefaultPieDataset
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	    /** The DataBook that serves as data source. */
	    private IDataRow dataRow;
	    
	    /** Column name for the value. */
	    private String columnName;
	    
	    /** Column name for the text. */
	    private String columnNameText;

	    /** The gauge. */
	    private JVxGauge gauge;
	    
        /** The ring plot. */
        private RingPlot plot;
        
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
		/**
		 * Creates a new instance of {@link DataRowPieDataset}.
		 *
		 * @param pDataRow the {@link IDataBook databook}.
		 * @param pValueColumnName the the column name of the value
		 * @param pTextColumnName the the column name of the text
		 * @param pGauge the gauge.
         * @param pPlot the plot.
		 */
    	public DataRowPieDataset(IDataRow pDataRow, String pValueColumnName, String pTextColumnName, JVxGauge pGauge, RingPlot pPlot)
		{
			dataRow = pDataRow;
			columnName = pValueColumnName;
			columnNameText = pTextColumnName;
			gauge = pGauge;
			plot = pPlot;
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
			try
			{
                double value = Math.max(gauge.minValue, Math.min(gauge.maxValue, ((Number)dataRow.getValue(columnName)).doubleValue()));
                
                if (!Double.isNaN(gauge.minErrorValue) && value <= gauge.minErrorValue)
                {
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_RED);
                }
                else if (!Double.isNaN(gauge.minWarningValue) && value <= gauge.minWarningValue)
                {
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_ORANGE);
                }
                else if (!Double.isNaN(gauge.maxErrorValue) && value >= gauge.maxErrorValue)
                {
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_RED);
                }
                else if (!Double.isNaN(gauge.maxWarningValue) && value >= gauge.maxWarningValue)
                {
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_ORANGE);
                }
                else
                {
                    ((RingPlot)plot).setSectionPaint("VALUE", LIGHT_GREEN);
                }
                
                double empty = gauge.maxValue - value;
                
                setValue("VALUE", value - gauge.minValue);
                setValue("EMPTY", empty);
                
                String sLabel;
                
                if (columnNameText != null)
                {
                	sLabel = gauge.translate(dataRow.getValueAsString(columnNameText));
                }
                else
                {
	                String labelText = gauge.translate(dataRow.getRowDefinition().getColumnDefinition(gauge.sColumnName).getLabel());
	                
	                if (!StringUtil.isEmpty(labelText))
	                {
	                	sLabel = dataRow.getValueAsString(columnName) + "  " + labelText;
	                }
	                else
	                {
	                	sLabel = dataRow.getValueAsString(columnName);
	                }
                }
                
                plot.setCenterText(sLabel);
			}
			catch (ModelException e)
			{
				throw new InvalidObjectException(e.getMessage());
			}
			
			super.validateObject();
		}
		
	}	// DataRowValueDataset

    /**
     * Half ring plot. 
     * 
     * @author Martin Handsteiner
     */
    public static class HalfRingPlot extends RingPlot
    {
        /**
         * Constructs a <code>HalfRingPlot</code>.
         */
        public HalfRingPlot() 
        {
            this(null);
        }

        /**
         * Constructs a <code>HalfRingPlot</code>.
         * 
         * @param pDataset the data set
         */
        public HalfRingPlot(PieDataset pDataset) 
        {
            super(pDataset);
            
            setStartAngle(0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, Integer index, PlotRenderingInfo info) 
        {
            PiePlotState state = super.initialise(g2, plotArea, plot, index, info);
            
            state.setTotal(state.getTotal() * 2);
            
            return state;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected void drawPie(Graphics2D g2, Rectangle2D plotArea, PlotRenderingInfo info) 
        {
            Rectangle2D.Double plotAr = (Rectangle2D.Double)plotArea;
            plotAr.y += plotAr.height / 8;
            super.drawPie(g2, plotAr, info);
            plotAr.y -= plotAr.height / 8;
        }
    }
        
    /**
     * Fixed bar width on StackedBarRenderer. 
     * 
     * @author Martin Handsteiner
     */
    public static class FixedMeterPlot extends MeterPlot
    {
        
        /**
         * Constructs a <code>FixedMeterPlot</code>.
         */
        public FixedMeterPlot() 
        {
            this((ValueDataset)null);
        }

        /**
         * Constructs a <code>FixedMeterPlot</code>.
         * 
         * @param pDataset the dataset
         */
        public FixedMeterPlot(ValueDataset pDataset) 
        {
            super(pDataset);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void drawValueLabel(Graphics2D g2, Rectangle2D area) 
        {
            g2.setFont(getValueFont());
            g2.setPaint(getValuePaint());
            String valueStr = ". " + getUnits();
            if (getDataset() != null) 
            {
               Number n = getDataset().getValue();
               if (n != null) 
               {
                  valueStr = getTickLabelFormat().format(n.doubleValue()) + " " + getUnits();
               }
            }

            float x = (float)area.getCenterX();
            float y = (float)(area.getY() + area.getHeight() * 0.78 - getValueFont().getSize());
            
            TextUtilities.drawAlignedString(valueStr, g2, x, y, TextAnchor.TOP_CENTER);
         }

    }
	
}	// JVxGauge
