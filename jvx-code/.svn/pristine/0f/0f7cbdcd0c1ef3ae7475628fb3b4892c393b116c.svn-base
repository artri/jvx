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
package com.sibvisions.rad.ui.vaadin.impl.component.chart.v3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import javax.rad.genui.UIColor;
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
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.control.IChart;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.byteowls.vaadin.chartjs.v3.ChartJs;
import com.byteowls.vaadin.chartjs.v3.ChartJs.DataPointClickListener;
import com.byteowls.vaadin.chartjs.v3.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.v3.config.ChartConfig;
import com.byteowls.vaadin.chartjs.v3.config.DonutChartConfig;
import com.byteowls.vaadin.chartjs.v3.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.v3.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.v3.data.BarDataset;
import com.byteowls.vaadin.chartjs.v3.data.Data;
import com.byteowls.vaadin.chartjs.v3.data.LineDataset;
import com.byteowls.vaadin.chartjs.v3.data.PieDataset;
import com.byteowls.vaadin.chartjs.v3.data.PointStyle;
import com.byteowls.vaadin.chartjs.v3.data.ScatterDataset;
import com.byteowls.vaadin.chartjs.v3.data.TimeLineDataset;
import com.byteowls.vaadin.chartjs.v3.options.DoughnutLabel;
import com.byteowls.vaadin.chartjs.v3.options.Position;
import com.byteowls.vaadin.chartjs.v3.options.scale.BaseScale;
import com.byteowls.vaadin.chartjs.v3.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.v3.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.v3.options.scale.LinearTicks;
import com.byteowls.vaadin.chartjs.v3.options.scale.TimeScale;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.NumberUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.server.ClientConnector.AttachListener;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;

/**
 * The <code>VaadinChartJs</code> is the <code>IChart</code>
 * implementation for vaadin, it uses Chart.js library.
 * 
 * @author Jozef Dorko
 */
public class VaadinChartJs extends VaadinComponent<ChartJs>
                           implements IChart,
                                      Runnable,
                                      IDataRowListener,
                                      IDataBookListener,
                                      DataPointClickListener
{

    /** Colors. */
    public static final VaadinColor[] COLORS = new VaadinColor[] {
            new VaadinColor(255, 99, 132),
            new VaadinColor(54, 162, 235),
            new VaadinColor(255, 206, 86),
            new VaadinColor(75, 192, 192),
            new VaadinColor(153, 102, 255),
            new VaadinColor(255, 159, 64)
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

    /** The chart inner title. */
    private String               sInnerTitle             = null;
    
    /** The chart inner title color. */
    private IColor               colorInnerTitle         = null;
    
    /** The chart inner label color. */
    private IColor               colorInnerLabel         = null;
    
    /** The chart inner title font. */
    private IFont                fontInnerTitle          = null;
    
    /** The chart inner label font. */
    private IFont                fontInnerLabel          = null;
    
    /** The x axis title. */
    private String               sXTitle                 = null;

    /** The y axis title. */
    private String               sYTitle                 = null;
    
    /** The legend position. */
    private Position             posLegend               = Position.BOTTOM;

    /** The legend box width. */
    private int                  iLegendBoxWidth         = 12;
    
    /** The chart style like bar, line, area. **/
    private int                  chartStyle              = STYLE_LINES;
    
    /** The alpha value for charts with transparent colors. */
    private double               dForegroundAlpha        = 0.7;

    /** The y column names. */
    private String[]             saYColumnNames          = null;
    
    /** The chart colors. */
    private IColor[]             colors                  = COLORS;
    
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
    
    /** whether inner chart labels should be displayed. */
    private boolean              bInnerLabelsEnabled     = false;

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
     * Whether inner chart labels should be displayed or not.
     * 
     * @return <code>true</code> if inner labels enabled, otherwise <code>false</code>
     */
    public boolean isInnerLabelsEnabled()
    {
        return bInnerLabelsEnabled;
    }

    /**
     * Enables/Disables inner chart labels.
     * 
     * @param bDisplayInnerLabels <code>true</code> if inner labels should be enabled, otherwise <code>false</code>
     */
    public void setInnerLabelsEnabled(boolean bDisplayInnerLabels)
    {
        this.bInnerLabelsEnabled = bDisplayInnerLabels;
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
                config = createPieChartConfiguration();
                
                dataBook.eventAfterRowSelected().addInternalListener(this);
                dataBook.eventAfterColumnSelected().addInternalListener(this);
            }
            else if (getBaseStyle() == STYLE_AREA)
            {
                config = createLineChartConfiguration();
            }
            else if (getBaseStyle() == STYLE_BARS)
            {
                config = createBarChartConfiguration(isHorizontalStyle());
            }
            else // if (getBaseStyle() == STYLE_LINES)
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
        PieChartConfig pieConfig;
        
        if (getSubStyle() == STYLE_RING)
        {
            pieConfig = new DonutChartConfig();
        }
        else
        {
            pieConfig = new PieChartConfig();
        }
        
        DoughnutLabel doughnutLabel = null;
        
        if (!StringUtil.isEmpty(sInnerTitle))
        {
            doughnutLabel = new DoughnutLabel();
            doughnutLabel.text(sInnerTitle);
            
            if (colorInnerTitle != null)
            {
                doughnutLabel.color(UIColor.toHex(colorInnerTitle));
            }
            
            if (fontInnerTitle != null)
            {
                String sStyle = null;
                String sWeight = null;
                
                switch (fontInnerTitle.getStyle())
                {
                    case IFont.BOLD: sWeight = "bold"; break;
                    case IFont.ITALIC: sStyle = "italic"; break;
                    
                    default: sStyle = "normal"; sWeight = "normal";
                }
                
                doughnutLabel
                    .font()
                        .family(excludeDefaultFont(fontInnerTitle.getFamily()))
                        .size(fontInnerTitle.getSize())
                        .style(sStyle)
                        .weight(sWeight);
            }
        }
        
        pieConfig.options()
                    .responsive(true)
                    .maintainAspectRatio(false)
                    .title()
                        .display(true)
                        .text(translate(sTitle))
                        .and()
                    .legend()
                        .position(posLegend)
                        .labels()
                            .boxWidth(iLegendBoxWidth)
                            .and()
                        .and()
                    .dataLabels()
                        .display(bInnerLabelsEnabled)
                        .foregroundColor(colorInnerLabel == null ? null : UIColor.toHex(colorInnerLabel))
                        .align("center")
                        .and()
                    .doughnutPlugin()
                        .addLabel(doughnutLabel)
                        .and()
                  .done();
        
        if (fontInnerLabel != null)
        {
            String sStyle = null;
            String sWeight = null;
            
            switch (fontInnerLabel.getStyle())
            {
                case IFont.BOLD: sWeight = "bold"; break;
                case IFont.ITALIC: sStyle = "italic"; break;
                
                default: sStyle = "normal"; sWeight = "normal";
            }
            
            pieConfig.options()
                        .dataLabels()
                                .font()
                                    .family(excludeDefaultFont(fontInnerTitle.getFamily()))
                                    .size(fontInnerLabel.getSize())
                                    .style(sStyle)
                                    .weight(sWeight);
        }
        
        pieConfig.data().clear();
        
        pieConfig.data().addDataset(new PieDataset());
        
        return pieConfig;
    }

    /**
     * Creates a line chart configuration.
     * 
     * @return the {@link ChartConfig}
     */
    @SuppressWarnings("rawtypes")
    private ChartConfig createLineChartConfiguration()
    {
        LineChartConfig lineConfig = new LineChartConfig();

        lineConfig.options()
                      .responsive(true)
                      .maintainAspectRatio(false)
                      .title()
                          .display(true)
                          .text(translate(sTitle))
                          .and()
                      .legend()
                          .position(posLegend)
                          .labels()
                              .boxWidth(iLegendBoxWidth)
                              .and()
                          .and()
                      .dataLabels()
                          .display(bInnerLabelsEnabled)
                          .align("center")
                          .and()
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
        
        boolean bTransparent = (getBaseStyle() == STYLE_AREA && !(getSubStyle() == STYLE_STACKEDAREA || getSubStyle() == STYLE_STACKEDPERCENTAREA));
        
        if (dataTypeX instanceof TimestampDataType)
        {
            scale = new TimeScale();
            
            for (int i = 0; i < saYColumnNames.length; i++)
            {
                TimeLineDataset dataSet = new TimeLineDataset();
                
                dataSet.borderColor(getColor(i, bTransparent));
                dataSet.backgroundColor(getColor(i, bTransparent));
                dataSet.fill(getBaseStyle() == STYLE_AREA);
                
                if (getSubStyle() == STYLE_STEPLINES)
                {
                    dataSet.steppedLine(true);
                }
                
                dataSet.lineTension(0);
                dataSet.pointHitRadius(Integer.valueOf(20));
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == STYLE_LINES && getSubStyle() != STYLE_STEPLINES)
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
                dataSet.fill(getBaseStyle() == STYLE_AREA);
                dataSet.stepped(getSubStyle() == STYLE_STEPLINES);
                dataSet.lineTension(0);
                dataSet.pointHitRadius(Integer.valueOf(20));
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == STYLE_LINES && getSubStyle() != STYLE_STEPLINES)
                {
                    dataSet.pointStyle(PointStyle.valueOf(getPointStyle(i)));
                    dataSet.pointRadius(Integer.valueOf(4));
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
                dataSet.fill(getBaseStyle() == STYLE_AREA);
                dataSet.stepped(getSubStyle() == STYLE_STEPLINES);
                dataSet.lineTension(0);
                dataSet.pointHitRadius(Integer.valueOf(20));
                dataSet.borderWidth(1);
                
                if (getBaseStyle() == STYLE_LINES && getSubStyle() != STYLE_STEPLINES)
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
        
        scale.scaleLabel().display(true).text(translate(getXAxisTitle()));
        
        lineConfig.options()
        .scales()
        .add("x", scale);
        
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
            barConfig.options().indexAxis("y");
        }
        
        barConfig.data().clear();
        
        barConfig.options()
                    .responsive(true)
                    .maintainAspectRatio(false)
                    .title()
                        .display(true)
                        .text(translate(sTitle))
                        .and()
                    .legend()
                        .position(posLegend)
                        .labels()
                            .boxWidth(iLegendBoxWidth)
                            .and()
                        .and()
                    .dataLabels()
                        .display(bInnerLabelsEnabled)
                        .align("center")
                        .and()
                  .done();
        
        IDataType dataTypeX = null;
        
        try
        {
            dataTypeX = dataBook.getRowDefinition().getColumnDefinition(sXColumnName).getDataType();
        }
        catch (ModelException e)
        {
            ExceptionHandler.show(e);
        }
        
        BaseScale<?> scale;
        
        if (dataTypeX.getTypeIdentifier() == BigDecimalDataType.TYPE_IDENTIFIER)
        {
            scale = new LinearScale();
            scale.offset(false);
        }
        else
        {
            scale = new CategoryScale();
            scale.offset(true);
        }
        
        scale.stacked(isBarStackedStyle());
        scale.scaleLabel().display(true).text(translate(getXAxisTitle()));
        
        barConfig.data().extractLabelsFromDataset(true);
        
        barConfig.options()
                        .scales()
                        .add(isHorizontalStyle() ? "y" : "x", scale);
        
        for (int i = 0; i < saYColumnNames.length; i++)
        {
            BarDataset dataSet = new BarDataset();
            
            boolean bTransparent = (getBaseStyle() == STYLE_BARS && (getSubStyle() == STYLE_OVERLAPPEDBARS || getSubStyle() == STYLE_OVERLAPPEDHBARS));
            
            if (getSubStyle() == STYLE_OVERLAPPEDBARS || getSubStyle() == STYLE_OVERLAPPEDHBARS)
            {
                dataSet.borderColor(getColor(i, bTransparent));
            }
            
            dataSet.backgroundColor(getColor(i, bTransparent));
            
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
                LineChartConfig lineConfig = (LineChartConfig)config;
                
                lineConfig.options().scales().add("y", initializeCartesianData(rowdef, lineConfig.data()));
                
                resource.configure(lineConfig);
                
                logger.debug(lineConfig.buildJson());
            }
            else if (getBaseStyle() == STYLE_BARS)
            {
                BarChartConfig barConfig = (BarChartConfig)config;
                        
                barConfig.options().scales().add(isHorizontalStyle() ? "x" : "y", initializeCartesianData(rowdef, barConfig.data()));

                resource.configure(barConfig);
                
                logger.debug(barConfig.buildJson());
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
     * @return the column labels
     * @throws ModelException if fill fails
     */
    private ArrayUtil<String> fillPieDataSet(IRowDefinition pRowDefinition, IDataRow pDataRow, PieDataset pDataset, String pLabel) throws ModelException
    {
        ArrayUtil<String> auColors = new ArrayUtil<String>();
        ArrayUtil<String> auLabels = new ArrayUtil<String>();
        
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
            
            auLabels.add(yColumnTitle);
        }
        
        if (pLabel == null)
        {
            pDataset.backgroundColor(auColors.toArray(new String[auColors.size()]));
        }
        
        return auLabels;
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
        
        return iSubStyle == STYLE_STACKEDBARS
                                || iSubStyle == STYLE_STACKEDHBARS
                                || iSubStyle == STYLE_STACKEDPERCENTBARS
                                || iSubStyle == STYLE_STACKEDPERCENTHBARS
                                || iSubStyle == STYLE_OVERLAPPEDBARS
                                || iSubStyle == STYLE_OVERLAPPEDHBARS;
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
        IColor vcColor = getColors()[pIndex % getColors().length];
        
        if (vcColor == null)
        {
            vcColor = COLORS[pIndex % getColors().length];
        }
        
        if (pTransparent)
        {
            vcColor = new VaadinColor(vcColor.getRed(), vcColor.getGreen(), vcColor.getBlue(), Long.valueOf(Math.round(dForegroundAlpha * 255d)).intValue());
        }
        
        return VaadinColor.getStyleValueRGB(vcColor);
    }
    
    /**
     * Gets the current chart color scheme.
     * 
     * @return the colors
     */
    public IColor[] getColors()
    {
        return colors;
    }
    
    /**
     * Sets the colors.
     * 
     * @param pColors the colors
     */
    public void setColors(IColor[] pColors)
    {
        if (pColors == null)
        {
            colors = COLORS;
        }
        else
        {
            colors = pColors;
        }
    }
    
    /**
     * Gets the inner title.
     * 
     * @return the inner title.
     */
    public String getInnerTitle()
    {
        return sInnerTitle;
    }

    /**
     * Sets the inner title.
     * 
     * @param pInnerTitle the inner title
     */
    public void setInnerTitle(String pInnerTitle)
    {
        if (!CommonUtil.equals(sInnerTitle, pInnerTitle))
        {
            uninstallChart();
            
            sInnerTitle = pInnerTitle;
            
            installChart();
        }
    }

    /**
     * Gets the inner title color.
     * 
     * @return the inner title {@link IColor}
     */
    public IColor getInnerTitleColor()
    {
        return colorInnerTitle;
    }

    /**
     * Sets the inner title color.
     * 
     * @param pInnerTitleColor the inner title {@link IColor}
     */
    public void setInnerTitleColor(IColor pInnerTitleColor)
    {
        if (!CommonUtil.equals(colorInnerTitle, pInnerTitleColor))
        {
            uninstallChart();
            
            colorInnerTitle = pInnerTitleColor;
            
            installChart();
        }
    }

    /**
     * Gets the inner label color.
     * 
     * @return the inner label {@link IColor}
     */
    public IColor getInnerLabelColor()
    {
        return colorInnerLabel;
    }

    /**
     * Sets the inner label color.
     * 
     * @param pInnerLabelColor the inner label {@link IColor}
     */
    public void setInnerLabelColor(IColor pInnerLabelColor)
    {
        if (!CommonUtil.equals(colorInnerLabel, pInnerLabelColor))
        {
            uninstallChart();
            
            colorInnerLabel = pInnerLabelColor;
            
            installChart();
        }
    }

    /**
     * Gets the inner title font.
     * 
     * @return the inner title font
     */
    public IFont getInnerTitleFont()
    {
        return fontInnerTitle;
    }

    /**
     * Sets the inner title font. Only size and style supported.
     * Style is mapped to css properties style and weight of chart.js
     * Use IFont.PLAIN, IFont.BOLD, IFont.ITALIC constants.
     * 
     * @param pInnerTitleFont the inner title font
     */
    public void setInnerTitleFont(IFont pInnerTitleFont)
    {
        uninstallChart();
        
        fontInnerTitle = pInnerTitleFont;
        
        installChart();
    }

    /**
     * Gets the inner label font.
     * 
     * @return the inner label font.
     */
    public IFont getInnerLabelFont()
    {
        return fontInnerLabel;
    }

    /**
     * Sets the inner label font. Only size and style supported.
     * Style is mapped to css properties style and weight of chart.js
     * Use IFont.PLAIN, IFont.BOLD, IFont.ITALIC constants.
     * 
     * @param pInnerLabelFont the inner label font
     */
    public void setInnerLabelFont(IFont pInnerLabelFont)
    {
        uninstallChart();
        
        fontInnerLabel = pInnerLabelFont;
        
        installChart();
    }

    /**
     * Gets the chart legend position.
     * 
     * @return the position
     */
    public Position getLegendPosition()
    {
        return posLegend;
    }

    /**
     * Sets the legend position.
     * 
     * @param pLegendPosition the position
     */
    public void setLegendPosition(Position pLegendPosition)
    {
        if (posLegend != pLegendPosition)
        {
            uninstallChart();
            
            posLegend = pLegendPosition;
            
            installChart();
        }
    }

    /**
     * Gets the legend box width.
     * 
     * @return the legend box width
     */
    public int getLegendBoxWidth()
    {
        return iLegendBoxWidth;
    }

    /**
     * Sets the legend box width.
     * 
     * @param pLegendBoxWidth the legend box width
     */
    public void setLegendBoxWidth(int pLegendBoxWidth)
    {
        if (iLegendBoxWidth != pLegendBoxWidth)
        {
            uninstallChart();
            
            iLegendBoxWidth = pLegendBoxWidth;
            
            installChart();
        }
    }

    /**
     * Gets the foreground alpha. The transparency level of overlapping AREA and BAR charts.
     * 
     * @return the foreground alpha
     */
    public double getForegroundAlpha()
    {
        return dForegroundAlpha;
    }

    /**
     * Sets the foreground alpha. The transparency level of overlapping AREA and BAR charts.
     * Use a value between 0 and 1.
     * 
     * @param pForegroundAlpha the foreground alpha
     */
    public void setForegroundAlpha(double pForegroundAlpha)
    {
        if (pForegroundAlpha >= 0 && pForegroundAlpha <= 1 && dForegroundAlpha != pForegroundAlpha)
        {
            uninstallChart();
            
            dForegroundAlpha = pForegroundAlpha;
            
            installChart();
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
     * Initializes cartesian(line, area or bar) data.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @param pData the data
     * @return the {@link LinearScale}
     * @throws ModelException if initialization fails
     */
    private LinearScale initializeCartesianData(IRowDefinition pRowDefinition, Data<?> pData) throws ModelException
    {
        LinearScale linearScale;
        
        dataBook.fetchAll();
   
        IDataType dataTypeX = pRowDefinition.getColumnDefinition(sXColumnName).getDataType();
        
        Map<String, HashMap<String, BigDecimal>> mSums = getSums();
        
        for (int i = 0; i < dataBook.getRowCount(); i++)
        {
            IDataRow row = dataBook.getDataRow(i);
      
            Object xValue = row.getValue(sXColumnName);
            
            if (xValue == null)
            {
                continue;
            }
            
            String sValueX = row.getValueAsString(sXColumnName);
            
            BigDecimal bdPrev = null;
            Double dPercentage = null;
            
            for (int y = 0; y < saYColumnNames.length; y++)
            {
                BigDecimal yValue = NumberUtil.toBigDecimal((Number)row.getValue(saYColumnNames[y]));
                
                if (yValue == null)
                {
                    continue;
                }
                
                if (getSubStyle() == STYLE_STACKEDPERCENTAREA)
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
                        dPercentage = Double.valueOf(100d);
                    }
                    else
                    {
                        dPercentage = calculatePercentValue(mSums.get(sValueX).get(SUM_ALL_KEY), yValue);
                    }
                }
                
                if (dataTypeX instanceof TimestampDataType)
                {
                    LocalDateTime ldtDateX = Instant.ofEpochMilli(((Date)xValue).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    
                    TimeLineDataset dataSet = (TimeLineDataset)pData.getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));

                    if (getSubStyle() == STYLE_STACKEDPERCENTAREA)
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
                    
                    ScatterDataset dataSet = (ScatterDataset)pData.getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
                    
                    if (getBaseStyle() == STYLE_BARS)
                    {
                        BarDataset barDataSet = (BarDataset)dataSet;
                        
                        if (getSubStyle() != STYLE_STACKEDPERCENTBARS && getSubStyle() != STYLE_STACKEDPERCENTHBARS)
                        {
                            barDataSet.addLabeledData(sValueX, Double.valueOf(yValue.doubleValue()));
                        }
                        
                        if (getSubStyle() == STYLE_OVERLAPPEDBARS || getSubStyle() == STYLE_OVERLAPPEDHBARS)
                        {
                            barDataSet.barPercentage(0.9 - (y * 0.15));
                        }
                    }
                    else
                    {
                        if (getSubStyle() == STYLE_STACKEDPERCENTAREA)
                        {
                            dataSet.addData(Double.valueOf(numValueX.doubleValue()), dPercentage);
                        }
                        else
                        {
                            dataSet.addData(Double.valueOf(numValueX.doubleValue()), Double.valueOf(yValue.doubleValue()));
                        }
                    }
                }
                else
                {
                    ScatterDataset dataSet = (ScatterDataset)pData.getDatasetAtIndex(y);
                    dataSet.label(translate(pRowDefinition.getColumnDefinition(saYColumnNames[y]).getLabel()));
                    
                    if (getBaseStyle() == STYLE_BARS)
                    {
                        BarDataset barDataSet = (BarDataset)dataSet;
                        
                        if (getSubStyle() != STYLE_STACKEDPERCENTBARS && getSubStyle() != STYLE_STACKEDPERCENTHBARS)
                        {
                            barDataSet.addLabeledData(sValueX, Double.valueOf(yValue.doubleValue()));
                        }
                        
                        if (getSubStyle() == STYLE_OVERLAPPEDBARS || getSubStyle() == STYLE_OVERLAPPEDHBARS)
                        {
                            barDataSet.barPercentage(0.9 - (y * 0.15));
                        }
                    }
                    else
                    {
                        if (getSubStyle() == STYLE_STACKEDPERCENTAREA)
                        {
                            dataSet.addLabeledData(sValueX, dPercentage);
                        }
                        else
                        {
                            dataSet.addLabeledData(sValueX, Double.valueOf(yValue.doubleValue()));
                        }
                    }
                    
                    pData.extractLabelsFromDataset(true);
                }
            }
        }
        
        linearScale = new LinearScale();
        
        if (getSubStyle() == STYLE_STACKEDAREA || getSubStyle() == STYLE_STACKEDBARS || getSubStyle() == STYLE_STACKEDPERCENTBARS)
        {
            linearScale.stacked(true);
        }
        
        linearScale.scaleLabel().display(true).text(translate(getYAxisTitle()));
        
        if (getSubStyle() == STYLE_STACKEDPERCENTAREA || getSubStyle() == STYLE_STACKEDPERCENTBARS || getSubStyle() == STYLE_STACKEDPERCENTHBARS)
        {
            LinearTicks<LinearScale> ticks = linearScale.ticks();
            
            linearScale.min(0);
            linearScale.max(100);
            
            ticks.callback("function(value) { return value + '%' }");
            
            if (getBaseStyle() == STYLE_BARS)
            {
                for (Entry<String, HashMap<String, BigDecimal>> entry : mSums.entrySet())
                {
                    for (int y = 0; y < saYColumnNames.length; y++)
                    {
                        BarDataset dataSet = (BarDataset)pData.getDatasetAtIndex(y);
                        
                        BigDecimal yValue = entry.getValue().get(saYColumnNames[y]);
                        
                        if (yValue != null)
                        {
                            dataSet.addLabeledData(entry.getKey(), calculatePercentValue(entry.getValue().get(SUM_ALL_KEY), yValue));
                        }
                    }
                }
            }
        }
        else
        {
            linearScale.grace("3%");
        }
        
        return linearScale;
    }

    /**
     * Initializes pie data.
     * 
     * @param pRowDefinition the {@link IRowDefinition}
     * @throws ModelException if initialization fails
     */
    private void initializePieData(IRowDefinition pRowDefinition) throws ModelException
    {
        PieChartConfig pieConfig = (PieChartConfig)config;
        pieConfig.options().tooltips().callbacks().label(createTooltipJsFunction());
        if (bInnerLabelsEnabled)
        {
            pieConfig.options().dataLabels().formatter(createDataLabelJsFunction());
        }
        
        Data<?> data = pieConfig.data();
        
        if (saYColumnNames.length > 1)
        {
            data.labelsAsList(fillPieDataSet(pRowDefinition, dataBook, (PieDataset)data.getDatasetAtIndex(0), null));
        }
        else
        {
            ArrayUtil<String> auColors = new ArrayUtil<String>();
            ArrayList<String> auCategories = new ArrayList<String>();
            
            PieDataset dataSet = (PieDataset)data.getDatasetAtIndex(0);
            
            dataBook.fetchAll();
            
            HashMap<String, HashMap<String, BigDecimal>> hmSumMaps = getSums();
            
            for (int i = 0; i < dataBook.getRowCount(); i++)
            {
                IDataRow row = dataBook.getDataRow(i);
         
                String sValueX = row.getValueAsString(sXColumnName);
                
                if (!StringUtil.isEmpty(sValueX) && !auCategories.contains(sValueX))
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
                    BigDecimal yValue = entry.getValue().get(saYColumnNames[y]);
                    
                    if (yValue != null)
                    {
                        dataSet.addLabeledData(entry.getKey(), calculatePercentValue(bdPieSum, yValue));
                    }
                }
            }
        }
        
        resource.configure(config);
        
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
        if (BigDecimal.ZERO.equals(pSum))
        {
            return Double.valueOf(0d);
        }
        else
        {
            return Double.valueOf(pValue.multiply(BigDecimal.valueOf(100)).divide(pSum, 2, RoundingMode.HALF_UP).doubleValue());
        }
    }
    
    /**
     * Creates the tooltip javascript function.
     * 
     * @return the function code
     */
    private String createTooltipJsFunction()
    {
        return "function(context) { "
                + "var label = context.label || '';"
                
                + " if (label)"
                + " {"
                + "     label += ': ';"
                + " }"
                
                + " label += context.parsed + '%';"
                
                + " return label; }";
    }
    
    /**
     * Creates the tooltip javascript function.
     * 
     * @return the function code
     */
    private String createDataLabelJsFunction()
    {
        return "function(value, context) { "
                
                + " return value + '%'; }";
    }
    
    /**
     * Helper for excluding default font family.
     * 
     * @param pFamily the font family
     * @return the font family
     */
    private String excludeDefaultFont(String pFamily)
    {
        if ("Dialog".equals(pFamily) || "Default".equals(pFamily))
        {
            return null;
        }
        else
        {
            return pFamily;
        }
    }

}   // VaadinChartJs
