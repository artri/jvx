/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.09.2021 - [DJ] - creation 
 */
package com.sibvisions.rad.ui.vaadin.impl.component.chart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.LongDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.event.IDataRowListener;
import javax.rad.ui.control.IChart;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.ChartJs.DataPointClickListener;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.config.ChartConfig;
import com.byteowls.vaadin.chartjs.config.DonutChartConfig;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Data;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.data.PointStyle;
import com.byteowls.vaadin.chartjs.data.ScatterDataset;
import com.byteowls.vaadin.chartjs.data.TimeLineDataset;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.BaseScale;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearTicks;
import com.byteowls.vaadin.chartjs.options.scale.TimeScale;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.NumberUtil;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.server.ClientConnector.AttachListener;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Sizeable.Unit;

/**
 * The <code>VaadinChartJs</code> is the <code>IChart</code>
 * implementation for vaadin, it uses older version of Chart.js library(2.7.2).
 * 
 * @author Jozef Dorko
 */
@Deprecated
public class VaadinChartJs extends VaadinComponent<ChartJs>
                           implements IChart,
                                      Runnable,
                                      IDataRowListener,
                                      IDataBookListener,
                                      DataPointClickListener
{

    /** Transparent colors. */
    public static final String[] TRANSPARENT_COLORS = new String[] {
            "rgba(255, 99, 132, 0.7)",
            "rgba(54, 162, 235, 0.7)",
            "rgba(255, 206, 86, 0.7)",
            "rgba(75, 192, 192, 0.7)",
            "rgba(153, 102, 255, 0.7)",
            "rgba(255, 159, 64, 0.7)"
    };
    
    /** Colors. */
    public static final String[] COLORS = new String[] {
            "rgba(255, 99, 132)",
            "rgba(54, 162, 235)",
            "rgba(255, 206, 86)",
            "rgba(75, 192, 192)",
            "rgba(153, 102, 255)",
            "rgba(255, 159, 64)"
    };
    
    /** The point styles. */
    public static final String[] POINT_STYLES = new String[] {
            "rect",
            "circle",
            "triangle",
            "star",
            "cross",
            "dash",
            "rectRot",
            "crossRot",
            "line",
            "rectRounded"
    };
    
    /** The sum all key. */
    private static final String  SUM_ALL_KEY             = "$sumALLtutti$?#.7(";

    /** The logger. */
    private static ILogger       logger                  = LoggerFactory.getInstance(VaadinChartJs.class);

    /** The DataBook to be shown. */
    private IDataBook            dataBook                = null;

    /** The current selected data page. **/
    private IDataPage            currentDataPage         = null;
    
    /** The x column name. */
    private String               sXColumnName            = null;

    /** The chart title. */
    private String               sTitle                  = null;

    /** The x axis title. */
    private String               sXTitle                 = null;

    /** The y axis title. */
    private String               sYTitle                 = null;

    /** The chart style like bar, line, area. **/
    private int                  chartStyle              = IChart.STYLE_LINES;

    /** The y column names. */
    private String[]             saYColumnNames          = null;

    /** The Configuration for the vaadin js chart. **/
    private ChartConfig          config;

    /** The translation mapping. */
    private TranslationMap       tmpTranslation          = null;

    /** whether the chart is attached. */
    private boolean              bAttached               = false;

    /** Tells, if notifyRepaint is called the first time. */
    private boolean              bFirstNotifyRepaintCall = true;

    /** Ignore repaint indicator. */
    private boolean              bIgnoreRepaint          = true;

    /** whether the translation is enabled. */
    private boolean              bTranslationEnabled     = true;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinChartJs</code>.
     */
    public VaadinChartJs()
    {
        super(new ChartJs());

        resource.addClickListener(this);
        
        resource.addAttachListener(new AttachListener()
        {
            
            @Override
            public void attach(AttachEvent event)
            {
                bAttached = true;
                
                installChart();
            }
        });
        
        resource.addDetachListener(new DetachListener()
        {
            
            @Override
            public void detach(DetachEvent event)
            {
                uninstallChart();
                
                bAttached = false;
            }
        });
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * The run method is invoked from AWT EventQueue. 
     * It enables events from the model again. 
     * Due to performance reasons the events are disabled from the first call of
     * notifyRepaint until the EventQueue calls the run method. 
     * This minimizes the repaints of the control. 
     */
    public void run()
    {
        try
        {
            cancelEditing();        
            
            Lock lockInstance = null;
            
            try
            {
                lockInstance = resource.getUI().getSession().getLockInstance();
            }
            catch (Exception e)
            {
                // Do nothing.
            }
            
            if (lockInstance != null)
            {
                lockInstance.lock();
            }
            
            try 
            {
                installChart();
                initializeData();
            }
            catch (Exception e)
            {
                ExceptionHandler.show(e);
            }
            finally 
            {
                if (lockInstance != null)
                {
                    lockInstance.unlock();
                }
            }
        }
        finally
        {
            bFirstNotifyRepaintCall = true;
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
        uninstallChart();
        
        dataBook = pDataBook;
        
        installChart();
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
    public void notifyRepaint()
    {
        if (bFirstNotifyRepaintCall)
        {
            bFirstNotifyRepaintCall = false;

            getFactory().invokeLater(this);
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
    public void cancelEditing()
    {
        // Do nothing
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
    public void setTranslation(TranslationMap pTranslation)
    {
        if (tmpTranslation != pTranslation)
        {
            uninstallChart();
            
            tmpTranslation = pTranslation;
            
            installChart();
        }
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
     * {@inheritDoc}
     */
    public int getChartStyle()
    {
        return chartStyle;
    }

    /**
     * {@inheritDoc}
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
    }

    /**
     * {@inheritDoc}
     */
    public String getXAxisTitle()
    {
        return sXTitle;
    }

    /**
     * {@inheritDoc}
     */
    public void setXAxisTitle(String pXAxisTitle)
    {
        sXTitle = pXAxisTitle;
    }

    /**
     * {@inheritDoc}
     */
    public String getYAxisTitle()
    {
        return sYTitle;
    }

    /**
     * {@inheritDoc}
     */
    public void setYAxisTitle(String pYAxisTitle)
    {
        sYTitle = pYAxisTitle;
    }

    /**
     * {@inheritDoc}
     */
    public String getXColumnName()
    {
        return sXColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public void setXColumnName(String pXColumnName)
    {
        uninstallChart();
        
        sXColumnName = pXColumnName;
        
        installChart();
    }

    /**
     * {@inheritDoc}
     */
    public String[] getYColumnNames()
    {
        return saYColumnNames;
    }

    /**
     * {@inheritDoc}
     */
    public void setYColumnNames(String[] pYColumnNames)
    {
        uninstallChart();
        
        saYColumnNames = pYColumnNames;
        
        installChart();
    }

    /**
     * {@inheritDoc}
     */
    public void valuesChanged(DataRowEvent pDataRowEvent) throws Throwable
    {
        IDataRow originalRow = pDataRowEvent.getOriginalDataRow();
        IDataRow changedRow = pDataRowEvent.getChangedDataRow();
        
        if (!originalRow.equals(changedRow, new String [] {sXColumnName}) || !originalRow.equals(changedRow, saYColumnNames))
        {
            bIgnoreRepaint = false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable
    {
        bIgnoreRepaint = false;
    }
    
    /**
     * {@inheritDoc}
     */ 
    public void onDataPointClick(int pDatasetIndex, int pDataIndex)
    {
        try
        {
            if (getBaseStyle() == STYLE_PIE && saYColumnNames.length > 1)
            {
                dataBook.setSelectedColumn(saYColumnNames[pDataIndex]);
            }
            else
            {
                IRowDefinition rowdef = dataBook.getRowDefinition();
                IDataType dataTypeX = rowdef.getColumnDefinition(sXColumnName).getDataType();

                if (dataTypeX instanceof StringDataType)
                {
                    if (getBaseStyle() == STYLE_PIE)
                    {
                        PieChartConfig pieConfig = (PieChartConfig)config;
                        Data<?> data = pieConfig.data();
                        String category = data.getLabels().get(pDataIndex);
                        
                        int selected = -1;
                        for (int i = 0, count = dataBook.getRowCount(); i < count && selected < 0; i++)
                        {
                            if (category.equals(translate(dataBook.getDataRow(i).getValueAsString(sXColumnName))))
                            {
                                selected = i;
                            }
                        }
                        
                        dataBook.setSelectedRow(selected);
                        dataBook.setSelectedColumn(saYColumnNames[0]);
                    }
                    else
                    {
                        Data<?> data = null;
                        if (getBaseStyle() == STYLE_LINES || getBaseStyle() == STYLE_AREA)
                        {
                            data = ((LineChartConfig)config).data();
                        }
                        else if (getBaseStyle() == STYLE_BARS)
                        {
                            data = ((BarChartConfig)config).data();
                        }
                        ScatterDataset dataSet = (ScatterDataset)data.getDatasetAtIndex(pDatasetIndex);
                        String category = dataSet.getDataLabels().get(pDataIndex);

                        int selected = -1;
                        for (int i = 0, count = dataBook.getRowCount(); i < count && selected < 0; i++)
                        {
                            if (category.equals(translate(dataBook.getDataRow(i).getValueAsString(sXColumnName))))
                            {
                                selected = i;
                            }
                        }

                        dataBook.setSelectedRow(selected);
                        dataBook.setSelectedColumn(saYColumnNames[pDatasetIndex]);
                    }
                }
                else
                {
                    dataBook.setSelectedRow(pDataIndex);
                    if (getBaseStyle() == STYLE_PIE)
                    {
                        dataBook.setSelectedColumn(sXColumnName);
                    }
                    else
                    {
                        dataBook.setSelectedColumn(saYColumnNames[pDatasetIndex]);
                    }
                }
            }

            if (eventMousePressed != null)
            {
                getFactory().synchronizedDispatchEvent(eventMousePressed, 
                                                        new UIMouseEvent(eventSource, UIMouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 
                                                                pDatasetIndex, pDataIndex, 1, false));
            }
            
            if (eventMouseReleased != null)
            {
                getFactory().synchronizedDispatchEvent(eventMouseReleased, 
                                                        new UIMouseEvent(eventSource, UIMouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 
                                                                pDatasetIndex, pDataIndex, 1, false));
            }
            
            if (eventMouseClicked != null)
            {
                getFactory().synchronizedDispatchEvent(eventMouseClicked, 
                                                        new UIMouseEvent(eventSource, UIMouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 
                                                                pDatasetIndex, pDataIndex, 1, false)); 
            }
        } 
        catch (ModelException e) 
        {
            // Do Nothing
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Installs the chart.
     */
    private void installChart()
    {
        if (dataBook != null && sXColumnName != null && saYColumnNames != null && bAttached)
        {
            dataBook.addControl(this);
            
            dataBook.eventValuesChanged().addInternalListener(this);
            dataBook.eventAfterInserting().addInternalListener(this);
            dataBook.eventAfterDeleted().addInternalListener(this);
            dataBook.eventAfterReload().addInternalListener(this);
            if (getBaseStyle() == STYLE_PIE)
            {
                dataBook.eventAfterRowSelected().addInternalListener(this);
                dataBook.eventAfterColumnSelected().addInternalListener(this);
            }
            
            if (getBaseStyle() == IChart.STYLE_PIE)
            {
                config = createPieChartConfiguration();
            }
            else if (getBaseStyle() == IChart.STYLE_AREA)
            {
                config = createLineChartConfiguration();
            }
            else if (getBaseStyle() == IChart.STYLE_BARS)
            {
                config = createBarChartConfiguration(isHorizontalStyle());
            }
            else // if (getBaseStyle() == IChart.STYLE_LINES)
            {
                config = createLineChartConfiguration();
            }

            bIgnoreRepaint = false;
            notifyRepaint();
        }
    }

    /**
     * Creates a pie chart configuration.
     * 
     * @return the {@link ChartConfig}
     */
    private ChartConfig createPieChartConfiguration()
    {
        if (getSubStyle() == IChart.STYLE_RING)
        {
            DonutChartConfig donutConfig = new DonutChartConfig();
            
            donutConfig.options()
                            .responsive(true)
                            .title()
                                .display(true)
                                .text(translate(sTitle))
                                .and()
                            .legend().display(false);
            
            donutConfig.data().clear();
            
            donutConfig.data().addDataset(new PieDataset());

            return donutConfig;
        }
        
        PieChartConfig pieConfig = new PieChartConfig();
        
        pieConfig.options()
                    .responsive(true)
                    .title()
                        .display(true)
                        .text(translate(sTitle))
                        .and()
                    .legend().display(false);
        
        pieConfig.data().clear();
        
        pieConfig.data().addDataset(new PieDataset());
        
        return pieConfig;
    }

    /**
     * Creates a line chart configuration.
     * 
     * @return the {@link ChartConfig}
     */
    private ChartConfig createLineChartConfiguration()
    {
        LineChartConfig lineConfig = new LineChartConfig();

        lineConfig.options()
                      .responsive(true)
                      .title()
                          .display(true)
                          .text(translate(sTitle))
                          .and()
                      .legend().position(Position.BOTTOM).and()
                   .done();
        
        lineConfig.data().clear();
        
        IRowDefinition rowdef = dataBook.getRowDefinition();
        
        IDataType dataTypeX = null;
        try
        {
            dataTypeX = rowdef.getColumnDefinition(sXColumnName).getDataType();
        }
        catch (ModelException e)
        {
            ExceptionHandler.show(e);
        }
        
        BaseScale scale;
        
        boolean bTransparent = (getBaseStyle() == IChart.STYLE_AREA && !(getSubStyle() == IChart.STYLE_STACKEDAREA || getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA));
        
        if (dataTypeX instanceof TimestampDataType)
        {
            scale = new TimeScale();
            
            for (int i = 0; i < saYColumnNames.length; i++)
            {
                TimeLineDataset dataSet = new TimeLineDataset();
                
                dataSet.borderColor(getColor(i, bTransparent));
                dataSet.backgroundColor(getColor(i, bTransparent));
                dataSet.fill(getBaseStyle() == IChart.STYLE_AREA);
                
                if (getSubStyle() == IChart.STYLE_STEPLINES)
                {
                    dataSet.steppedLine(true);
                }
                
                dataSet.lineTension(0);
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == IChart.STYLE_LINES && getSubStyle() != IChart.STYLE_STEPLINES)
                {
                    dataSet.pointStyle(PointStyle.valueOf(getPointStyle(i)));
                }
                else
                {
                    dataSet.pointRadius(Integer.valueOf(0));
                }
                
                lineConfig.data().addDataset(dataSet);
            }
        }
        else if (dataTypeX instanceof BigDecimalDataType || dataTypeX instanceof LongDataType)
        {
            scale = new LinearScale();
            
            for (int i = 0; i < saYColumnNames.length; i++)
            {
                ScatterDataset dataSet = new ScatterDataset();
                
                dataSet.borderColor(getColor(i, bTransparent));
                dataSet.backgroundColor(getColor(i, bTransparent));
                dataSet.fill(getBaseStyle() == IChart.STYLE_AREA);
                
                if (getSubStyle() == IChart.STYLE_STEPLINES)
                {
                    dataSet.steppedLine(true);
                }
                
                dataSet.lineTension(0);
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == IChart.STYLE_LINES && getSubStyle() != IChart.STYLE_STEPLINES)
                {
                    dataSet.pointStyle(PointStyle.valueOf(getPointStyle(i)));
                    dataSet.pointRadius(Integer.valueOf(4));
                    dataSet.pointHitRadius(Integer.valueOf(7));
                }
                else
                {
                    dataSet.pointRadius(Integer.valueOf(0));
                }
                
                lineConfig.data().addDataset(dataSet);
            }
        } 
        else
        {
            scale = new CategoryScale();
            
            for (int i = 0; i < saYColumnNames.length; i++)
            {
                LineDataset dataSet = new LineDataset();
                
                dataSet.borderColor(getColor(i, bTransparent));
                dataSet.backgroundColor(getColor(i, bTransparent));
                dataSet.fill(getBaseStyle() == IChart.STYLE_AREA);
                
                if (getSubStyle() == IChart.STYLE_STEPLINES)
                {
                    dataSet.steppedLine(true);
                }
                
                dataSet.lineTension(0);
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == IChart.STYLE_LINES && getSubStyle() != IChart.STYLE_STEPLINES)
                {
                    dataSet.pointStyle(PointStyle.valueOf(getPointStyle(i)));
                }
                else
                {
                    dataSet.pointRadius(Integer.valueOf(0));
                }
                
                lineConfig.data().addDataset(dataSet);
            }
        }
        
        scale.scaleLabel().display(true).labelString(translate(getXAxisTitle()));
        
        lineConfig.options()
        .scales()
        .add(Axis.X, scale);
        
        return lineConfig;
    }

    /**
     * Creates a bar chart configuration.
     * 
     * @param pHorizontalStyle whether bar chart should be horizontal
     * @return the bar {@link ChartConfig}
     */
    private ChartConfig createBarChartConfiguration(boolean pHorizontalStyle)
    {
        BarChartConfig barConfig = new BarChartConfig();
        
        if (pHorizontalStyle)
        {
            barConfig.horizontal();
        }
        
        barConfig.data().clear();
        
        barConfig.options()
                    .responsive(true)
                    .title()
                        .display(true)
                        .text(translate(sTitle))
                        .and()
                        .legend().position(Position.BOTTOM).and()
                    .done();
        
        CategoryScale scale = new CategoryScale();
        scale.stacked(isBarStackedStyle());
        scale.scaleLabel().display(true).labelString(translate(getXAxisTitle()));
        
        barConfig.data().extractLabelsFromDataset(true);
        
        barConfig.options()
                        .scales()
                        .add(isHorizontalStyle() ? Axis.Y : Axis.X, scale);
        
        for (int i = 0; i < saYColumnNames.length; i++)
        {
            BarDataset dataSet = new BarDataset();
            
            if (getSubStyle() == IChart.STYLE_OVERLAPPEDBARS || getSubStyle() == IChart.STYLE_OVERLAPPEDHBARS)
            {
                dataSet.borderColor(getColor(i));
            }
            
            dataSet.backgroundColor(getColor(i));
            
            barConfig.data().addDataset(dataSet);
        }
        
        return barConfig;
    }

    /**
     * Uninstalls the chart.
     */
    private void uninstallChart()
    {
        if (dataBook != null && bAttached)
        {
            dataBook.removeControl(this);
            dataBook.eventValuesChanged().removeInternalListener(this);
            dataBook.eventAfterInserting().removeInternalListener(this);
            dataBook.eventAfterDeleted().removeInternalListener(this);
            dataBook.eventAfterReload().removeInternalListener(this);
            if (getBaseStyle() == STYLE_PIE)
            {
                dataBook.eventAfterRowSelected().removeInternalListener(this);
                dataBook.eventAfterColumnSelected().removeInternalListener(this);
            }
        }
    }
    
    /**
     * Initializes the DataSeries.
     * 
     * @throws ModelException if initializing fails
     */
    private void initializeData() throws ModelException
    {
        if (bAttached && (!bIgnoreRepaint || currentDataPage != dataBook.getDataPage()))
        {
            currentDataPage = dataBook.getDataPage();
            bIgnoreRepaint = true;
            
            IRowDefinition rowdef = dataBook.getRowDefinition();
            
            if (getBaseStyle() == STYLE_LINES || getBaseStyle() == STYLE_AREA)
            {
                initializeLineOrAreaData(rowdef);
            }
            else if (getBaseStyle() == STYLE_BARS)
            {
                initializeBarData(rowdef);
            }
            else if (getBaseStyle() == STYLE_PIE)
            {
                initializePieData(rowdef);
            }
            
            resource.update();
        }
    }

    /**
     * Fills pie data set with values from data row.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @param pDataRow the {@link IDataRow}
     * @param pDataset the {@link PieDataset}
     * @param pLabel the label
     * @throws ModelException if fill fails
     */
    private void fillPieDataSet(IRowDefinition pRowDefinition, IDataRow pDataRow, PieDataset pDataset, String pLabel) throws ModelException
    {
        ArrayUtil<String> auColors = new ArrayUtil<String>();
        
        BigDecimal bdPieSum = BigDecimal.ZERO;
        
        for (String yColumnName : saYColumnNames)
        {
            Number numValue = (Number)pDataRow.getValue(yColumnName);
            
            if (numValue != null)
            {
            	bdPieSum = bdPieSum.add(NumberUtil.toBigDecimal(numValue));
            }
        }
        
        for (String yColumnName : saYColumnNames)
        {
            String yColumnTitle = translate(pRowDefinition.getColumnDefinition(yColumnName).getLabel());
            Number numValue = (Number)pDataRow.getValue(yColumnName);
            
            if (numValue != null)
            {
                pDataset.addLabeledData(pLabel == null ? yColumnTitle : pLabel, calculatePercentValue(bdPieSum, NumberUtil.toBigDecimal(numValue)));
                auColors.add(getColor(auColors.size()));
            }
        }
        
        if (pLabel == null)
        {
            pDataset.backgroundColor(auColors.toArray(new String[auColors.size()]));
        }
    }
    
    /**
     * Calculates the sums and gets them.
     * 
     * @return the sums {@link Map}
     * @throws ModelException if it fails
     */
    private HashMap<String, HashMap<String, BigDecimal>> getSums() throws ModelException
    {
        LinkedHashMap<String, HashMap<String, BigDecimal>> hmSumMaps = new LinkedHashMap<String, HashMap<String, BigDecimal>>();
        
        for (int i = 0; i < dataBook.getRowCount(); i++)
        {
            IDataRow dr = dataBook.getDataRow(i);
            
            String sValueX = dr.getValueAsString(sXColumnName);
            
            for (int y = 0; y < saYColumnNames.length; y++)
            {
                BigDecimal bdValueY = NumberUtil.toBigDecimal((Number)dr.getValue(saYColumnNames[y]));
                
                if (bdValueY != null)
                {
                    if (hmSumMaps.containsKey(sValueX))
                    {
                        HashMap<String, BigDecimal> hmSum = hmSumMaps.get(sValueX);
                        
                        BigDecimal bdYSum = hmSum.get(saYColumnNames[y]);
                        
                        if (bdYSum == null)
                        {
                            hmSum.put(saYColumnNames[y], bdValueY);
                        }
                        else
                        {
                            hmSum.put(saYColumnNames[y], bdYSum.add(bdValueY));
                        }
                        
                        BigDecimal bdAllSum = hmSum.get(SUM_ALL_KEY);
                        
                        if (bdAllSum == null)
                        {
                            hmSum.put(SUM_ALL_KEY, bdValueY);
                        }
                        else
                        {
                            hmSum.put(SUM_ALL_KEY, bdAllSum.add(bdValueY));
                        }
                    }
                    else
                    {
                        HashMap<String, BigDecimal> hmSum = new HashMap<String, BigDecimal>();
                        
                        hmSum.put(saYColumnNames[y], bdValueY);
                        hmSum.put(SUM_ALL_KEY, bdValueY);
                        
                        hmSumMaps.put(sValueX, hmSum);
                    }
                }
            }
        }
        
        return hmSumMaps;
    }

    /**
     * Gets the base style of the chart (the lowest 2 digits of constant).
     * 
     * @return the chart style ({@link #STYLE_LINES}, {@link #STYLE_AREA}, {@link #STYLE_BARS}).
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
     * Whether is bar stacked style or not.
     * 
     * @return <code>true</code> if the style is stacked bar, otherwise <code>false</code>
     */
    public boolean isBarStackedStyle()
    {
        int iSubStyle = getSubStyle();
        
        return iSubStyle == IChart.STYLE_STACKEDBARS
                                || iSubStyle == IChart.STYLE_STACKEDHBARS
                                || iSubStyle == IChart.STYLE_STACKEDPERCENTBARS
                                || iSubStyle == IChart.STYLE_STACKEDPERCENTHBARS
                                || iSubStyle == IChart.STYLE_OVERLAPPEDBARS
                                || iSubStyle == IChart.STYLE_OVERLAPPEDHBARS;
    }
    
    /**
     * Gets the non transparent color.
     * 
     * @param pIndex the index
     * @return the color string
     */
    public String getColor(int pIndex)
    {
        return getColor(pIndex, false);
    }
    
    /**
     * Gets the color.
     * 
     * @param pIndex the index
     * @param pTransparent whether the color should be transparent
     * @return the color string
     */
    public String getColor(int pIndex, boolean pTransparent)
    {
        if (pTransparent)
        {
            return TRANSPARENT_COLORS[pIndex % TRANSPARENT_COLORS.length];
        }
        else
        {
            return COLORS[pIndex % COLORS.length];
        }
    }
    
    /**
     * Gets the chart point style.
     * 
     * @param pIndex the index
     * @return the point style name
     */
    private String getPointStyle(int pIndex)
    {
        return POINT_STYLES[pIndex % POINT_STYLES.length];
    }
    
    /**
     * Initializes line or area data.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @throws ModelException if initialization fails
     */
    private void initializeLineOrAreaData(IRowDefinition pRowDefinition) throws ModelException
    {
        LineChartConfig lineConfig = (LineChartConfig)config;
        
        LinearScale linearScale;
        
        dataBook.fetchAll();
   
        IDataType dataTypeX = pRowDefinition.getColumnDefinition(sXColumnName).getDataType();
        
        ArrayList<String> categories = new ArrayList<String>();
        
        for (int i = 0; i < dataBook.getRowCount(); i++)
        {
            IDataRow row = dataBook.getDataRow(i);
      
            Object xValue = row.getValue(sXColumnName);
            
            if (xValue == null)
            {
                continue;
            }
            
            String sValueX = row.getValueAsString(sXColumnName);
            
            if (!categories.contains(sValueX))
            {
                categories.add(sValueX);
            }
            
            Map<String, HashMap<String, BigDecimal>> mSums = getSums();
            
            BigDecimal bdPrev = null;
            Double dPercentage = null;
            
            for (int y = 0; y < saYColumnNames.length; y++)
            {
                BigDecimal yValue = NumberUtil.toBigDecimal((Number)row.getValue(saYColumnNames[y]));
                
                if (getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA)
                {
                    if (bdPrev == null)
                    {
                        bdPrev = yValue;
                    }
                    else
                    {
                        yValue = bdPrev.add(yValue);
                        bdPrev = yValue;
                    }
                    
                    if (y == (saYColumnNames.length - 1))
                    {
                        dPercentage = Double.valueOf(100);
                    }
                    else
                    {
                        dPercentage = calculatePercentValue(mSums.get(sValueX).get(SUM_ALL_KEY), yValue);
                    }
                }
                
                if (dataTypeX instanceof TimestampDataType)
                {
                    LocalDateTime ldtDateX = Instant.ofEpochMilli(((Date)xValue).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    
                    TimeLineDataset dataSet = (TimeLineDataset)lineConfig.data().getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
                    
                    if (getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA)
                    {
                        dataSet.addData(ldtDateX, dPercentage);
                    }
                    else
                    {
                        dataSet.addData(ldtDateX, Double.valueOf(yValue.doubleValue()));
                    }
                }
                else if (dataTypeX instanceof BigDecimalDataType || dataTypeX instanceof LongDataType)
                {
                    Number numValueX = (Number)xValue;
                    
                    ScatterDataset dataSet = (ScatterDataset)lineConfig.data().getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
                    
                    if (getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA)
                    {
                        dataSet.addData(Double.valueOf(numValueX.doubleValue()), dPercentage);
                    }
                    else
                    {
                        dataSet.addData(Double.valueOf(numValueX.doubleValue()), Double.valueOf(yValue.doubleValue()));
                    }
                }
                else
                {
                    LineDataset dataSet = (LineDataset)lineConfig.data().getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
                    
                    if (getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA)
                    {
                        dataSet.addLabeledData(sValueX, dPercentage);
                    }
                    else
                    {
                        dataSet.addLabeledData(sValueX, Double.valueOf(yValue.doubleValue()));
                    }
                    
                    lineConfig.data().extractLabelsFromDataset(true);
                }
            }
        }
        
        linearScale = new LinearScale();
        
        if (getSubStyle() == IChart.STYLE_STACKEDAREA)
        {
            linearScale.stacked(true);
        }
        
        linearScale.scaleLabel().display(true).labelString(translate(getYAxisTitle()));
        
        if (getSubStyle() == IChart.STYLE_STACKEDPERCENTAREA)
        {
            LinearTicks<LinearScale> ticks = linearScale.ticks();
            
            ticks.beginAtZero(Boolean.TRUE);
            ticks.max(100);
            ticks.callback("function(value) { return value + '%' }");
        }
        
        lineConfig.options().scales().add(Axis.Y, linearScale);
        
        logger.debug(lineConfig.buildJson());
        
        resource.configure(lineConfig);
        
        resource.setHeight(getPreferredSize().getHeight(), Unit.PIXELS);
        resource.setWidth(getPreferredSize().getWidth(), Unit.PIXELS);
    }

    /**
     * Initializes bar data.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @throws ModelException if initialization fails
     */
    private void initializeBarData(IRowDefinition pRowDefinition) throws ModelException
    {
        BarChartConfig barConfig = (BarChartConfig)config;
        
        dataBook.fetchAll();
        
        HashMap<String, HashMap<String, BigDecimal>> hmSumMaps = getSums();
        
        for (int i = 0; i < dataBook.getRowCount(); i++)
        {
            IDataRow row = dataBook.getDataRow(i);
            
            Object xValue = row.getValue(sXColumnName);
            
            if (xValue == null)
            {
                continue;
            }
            
            String sValueX = row.getValueAsString(sXColumnName);
            
            for (int y = 0; y < saYColumnNames.length; y++)
            {
                BigDecimal yValue = NumberUtil.toBigDecimal((Number)row.getValue(saYColumnNames[y]));
                
                BarDataset dataSet = (BarDataset)barConfig.data().getDatasetAtIndex(y);
                
                if (getSubStyle() != IChart.STYLE_STACKEDPERCENTBARS && getSubStyle() != IChart.STYLE_STACKEDPERCENTHBARS)
                {
                    dataSet.addLabeledData(sValueX, Double.valueOf(yValue.doubleValue()));
                }
                
                dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
            }
        }
        
        LinearScale ls = new LinearScale();
        
        if (getSubStyle() == IChart.STYLE_STACKEDBARS || getSubStyle() == IChart.STYLE_STACKEDPERCENTBARS)
        {
            ls.stacked(true);
        }
        
        ls.scaleLabel().display(true).labelString(translate(getYAxisTitle()));
        
        if (getSubStyle() == IChart.STYLE_STACKEDPERCENTBARS || getSubStyle() == IChart.STYLE_STACKEDPERCENTHBARS)
        {
            LinearTicks<LinearScale> ticks = ls.ticks();
            
            ticks.beginAtZero(Boolean.TRUE);
            ticks.max(100);
            ticks.callback("function(value) { return value + '%' }");
            
            for (Entry<String, HashMap<String, BigDecimal>> entry : hmSumMaps.entrySet())
            {
                for (int y = 0; y < saYColumnNames.length; y++)
                {
                    BarDataset dataSet = (BarDataset)barConfig.data().getDatasetAtIndex(y);
                    
                    dataSet.addLabeledData(entry.getKey(), calculatePercentValue(entry.getValue().get(SUM_ALL_KEY), entry.getValue().get(saYColumnNames[y])));
                }
            }
        }
        
        if (isHorizontalStyle())
        {
            ls.position(Position.TOP);
        }
        
        barConfig.options().scales().add(isHorizontalStyle() ? Axis.X : Axis.Y, ls);
        
        resource.configure(barConfig);
        
        resource.setHeight(getPreferredSize().getHeight(), Unit.PIXELS);
        resource.setWidth(getPreferredSize().getWidth(), Unit.PIXELS);
        
        logger.debug(barConfig.buildJson());
    }
    
    /**
     * Initializes pie data.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @throws ModelException if initialization fails
     */
    private void initializePieData(IRowDefinition pRowDefinition) throws ModelException
    {
        Data<?> data;
        
        if (getSubStyle() == STYLE_RING)
        {
            DonutChartConfig donutConfig = (DonutChartConfig)config;
            donutConfig.options().tooltips().callbacks().label(createTooltipJsFunction());
            
            data = donutConfig.data();
        }
        else
        {
            PieChartConfig pieConfig = (PieChartConfig)config;
            pieConfig.options().tooltips().callbacks().label(createTooltipJsFunction());
            
            data = pieConfig.data();
        }
        
        if (saYColumnNames.length > 1)
        {
            fillPieDataSet(pRowDefinition, dataBook, (PieDataset)data.getDatasetAtIndex(0), null);
            
            data.labelsAsList(Arrays.asList(saYColumnNames));
        }
        else
        {
            ArrayUtil<String> auColors = new ArrayUtil<String>();
            ArrayList<String> auCategories = new ArrayList<String>();
            
            PieDataset dataSet = (PieDataset)data.getDatasetAtIndex(0);
            
            HashMap<String, HashMap<String, BigDecimal>> hmSumMaps = getSums();
            
            dataBook.fetchAll();
            
            for (int i = 0; i < dataBook.getRowCount(); i++)
            {
                IDataRow row = dataBook.getDataRow(i);
         
                String sValueX = row.getValueAsString(sXColumnName);
                
                if (!auCategories.contains(sValueX))
                {
                    auCategories.add(sValueX);
                    auColors.add(getColor(auColors.size()));
                }
            }
            
            dataSet.backgroundColor(auColors.toArray(new String[auColors.size()]));
            data.labelsAsList(auCategories);
            
            BigDecimal bdPieSum = BigDecimal.ZERO;
            
            for (Entry<String, HashMap<String, BigDecimal>> entry : hmSumMaps.entrySet())
            {
                bdPieSum = bdPieSum.add(entry.getValue().get(SUM_ALL_KEY));
            }
            
            for (Entry<String, HashMap<String, BigDecimal>> entry : hmSumMaps.entrySet())
            {
                for (int y = 0; y < saYColumnNames.length; y++)
                {
                    dataSet.addLabeledData(entry.getKey(), calculatePercentValue(bdPieSum, entry.getValue().get(saYColumnNames[y])));
                }
            }
        }
        
        resource.configure(config);
        
        resource.setHeight(getPreferredSize().getHeight(), Unit.PIXELS);
        resource.setWidth(getPreferredSize().getWidth(), Unit.PIXELS);
        
        logger.debug(config.buildJson());
    }
    
    /**
     * Calculates percent value.
     * 
     * @param pSum the sum
     * @param pValue the value
     * @return the percent value
     */
    private Double calculatePercentValue(BigDecimal pSum, BigDecimal pValue)
    {
        return Double.valueOf(pValue.multiply(BigDecimal.valueOf(100)).divide(pSum, 2, RoundingMode.HALF_UP).doubleValue());
    }
    
    /**
     * Creates the tooltip javascript function.
     * 
     * @return the function code
     */
    private String createTooltipJsFunction()
    {
        return "function(tooltipItem, data) { "
                + "var label = data.labels[tooltipItem.index];"
                
                + " if (label)"
                + " {"
                + "     label += ': ';"
                + " }"
                
                + " label += data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index] + '%';"
                
                + " return label; }";
    }

}   // VaadinChartJs
