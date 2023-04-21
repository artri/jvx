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
 * 17.09.2021 - [DJ] - creation 
 */
package com.sibvisions.rad.ui.vaadin.impl.component.chart;

import java.util.concurrent.locks.Lock;

import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.byteowls.vaadin.chartjs.Gauge;
import com.byteowls.vaadin.chartjs.utils.JUtils;
import com.sibvisions.rad.ui.vaadin.impl.IVaadinContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.server.ClientConnector.AttachListener;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;

import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonNull;

/**
 * The <code>VaadinGaugeJs</code> is the <code>IGauge</code>
 * implementation for vaadin. It uses https://github.com/sibvisions/gauges library.
 * 
 * @author Jozef Dorko
 */
public class VaadinGaugeJs extends VaadinComponent<Gauge>
                           implements IGauge,
                                      Runnable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The gauge style like meter, speedometer. **/
    private int            gaugeStyle              = IGauge.STYLE_SPEEDOMETER;

    /** The min value. */
    private double         minValue                = 0;

    /** The max value. */
    private double         maxValue                = 100;

    /** The min warning value. */
    private double         minWarningValue         = Double.NaN;

    /** The max warning value. */
    private double         maxWarningValue         = Double.NaN;

    /** The min error value. */
    private double         minErrorValue           = Double.NaN;

    /** The max error value. */
    private double         maxErrorValue           = Double.NaN;

    /** The last sent value. */
    private Number         lastValue               = null;

    /** whether the gauge is attached. */
    private boolean        bAttached               = false;

    /** whether the translation is enabled. */
    private boolean        bTranslationEnabled     = true;

    /** Tells, if notifyRepaint is called the first time. */
    private boolean        bFirstNotifyRepaintCall = true;

	/** The column name for the value. */
	private String 		   sColumnName 			   = null;
	
	/** The (optional) column name for the label. */
	private String 		   sLabelColumnName 	   = null;
    
    /** The gauge title. */
    private String         sTitle                  = null;

    /** The {@link IDataRow} to be shown. */
    private IDataRow       dataRow                 = null;

    /** The translation mapping. */
    private TranslationMap tmpTranslation          = null;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinGaugeJs</code>.
     */
    public VaadinGaugeJs()
    {
        super(new Gauge());
        
        initConfig();
    }
    
    /**
     * Initializes the configuration.
     */
    private void initConfig()
    {
        resource.addAttachListener(new AttachListener()
        {
            
            @Override
            public void attach(AttachEvent event)
            {
                bAttached = true;
                
                install();
            }
            
        });
        
        resource.addDetachListener(new DetachListener()
        {
            
            @Override
            public void detach(DetachEvent event)
            {
                uninstall();
                
                bAttached = false;
            }
            
        });
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public int getGaugeStyle()
    {
        return gaugeStyle;
    }

    /**
     * {@inheritDoc}
     */
    public void setGaugeStyle(int pGaugeStyle)
    {
        if (pGaugeStyle != gaugeStyle)
        {
            uninstall();
            
            gaugeStyle = pGaugeStyle;
            
            install(true);
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
    public double getMinValue()
    {
        return minValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMinValue(double pMinValue)
    {
        uninstall();
        
        minValue = pMinValue;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxValue()
    {
        return maxValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxValue(double pMaxValue)
    {
        uninstall();
        
        maxValue = pMaxValue;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public double getMinErrorValue()
    {
        return minErrorValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMinErrorValue(double pMinErrorValue)
    {
        uninstall();
        
        minErrorValue = pMinErrorValue;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxErrorValue()
    {
        return maxErrorValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxErrorValue(double pMaxErrorValue)
    {
        uninstall();
        
        maxErrorValue = pMaxErrorValue;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public double getMinWarningValue()
    {
        return minWarningValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMinWarningValue(double pMinWarningValue)
    {
        uninstall();
        
        minWarningValue = pMinWarningValue;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxWarningValue()
    {
        return maxWarningValue;
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxWarningValue(double pMaxWarningValue)
    {
        uninstall();
        
        maxWarningValue = pMaxWarningValue;
        
        install();
    }
    
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
    public IDataRow getDataRow()
    {
        return dataRow;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataRow(IDataRow pDataRow) throws ModelException
    {
        uninstall();
        
        dataRow = pDataRow;
        
        install();
    }

    /**
     * {@inheritDoc}
     */
    public String getColumnName()
    {
        return sColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public void setColumnName(String pColumnName) throws ModelException
    {
        uninstall();
        
        sColumnName = pColumnName;
        
        install();
    }
    
    /**
     * Gets the column name which will be used for the label.
     * 
     * @return the column name
     */
    public String getLabelColumnName()
    {
    	return sLabelColumnName;
    }	

    /**
     * Sets the column name which should be used for the label.
     * 
     * @param pColumnName the column name
     */
    public void setLabelColumnName(String pColumnName)
    {
    	sLabelColumnName = pColumnName;
    	
    	notifyRepaint();
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
        // do nothing
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
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        if (tmpTranslation != pTranslation)
        {
            uninstall();
            
            tmpTranslation = pTranslation;
            
            install();
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
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Installs the CellEditor and its CellEditorComponent.
     */
    private void install()
    {
        install(false);
    }
    
    /**
     * Installs the CellEditor and its CellEditorComponent.
     * 
     * @param pRecreate true, if chart component should be recreated.
     */
    private void install(boolean pRecreate)
    {
        if (dataRow != null && sColumnName != null && bAttached)
        {
            if (pRecreate)
            {
                IVaadinContainer container = (IVaadinContainer)getParent();
                if (container != null)
                {
                    int index = container.indexOf(this);
                    container.removeFromVaadin(this);
                    resource = new Gauge();
                    initConfig();
                    container.addToVaadin(this, getConstraints(), index);
                    return;
                }
            }   

            dataRow.addControl(this);
        }
        
        resource.setGaugeStyle(gaugeStyle);
        lastValue = Double.valueOf(Double.NaN);
        notifyRepaint();
    }
    
    /**
     * Uninstalls the gauge component.
     */
    private void uninstall()
    {
        if (dataRow != null)
        {
            dataRow.removeControl(this);
        }
    }
    
    /**
     * Initializes the data. For gauge it means single value.
     * 
     * @throws ModelException if initializing fails
     */
    private void initializeData() throws ModelException
    {
        if (dataRow != null && sColumnName != null)
        {
            Number value = (Number)dataRow.getValue(sColumnName);
            
            if (bAttached && !CommonUtil.equals(value, lastValue))
            {
                lastValue = value;

                JsonObject options = Json.createObject();
                
                Double dValue = Double.valueOf(-1);
                
                if (value != null)
                {
                    dValue = Double.valueOf(value.doubleValue());
                }
                
                JUtils.putNotNull(options, "value", dValue);
                
                JUtils.putNotNull(options, "min", Double.valueOf(minValue));
                JUtils.putNotNull(options, "max", Double.valueOf(maxValue));
                JUtils.putNotNull(options, "size", Double.valueOf(300));
                JUtils.putNotNull(options, "title", sTitle);
                
                String sLabel;
                
                if (sLabelColumnName != null)
                {
                	sLabel = translate(dataRow.getValueAsString(sLabelColumnName));
                	
                	JUtils.putNotNull(options, "hideValue", Boolean.TRUE);
                }
                else
                {
	                sLabel = translate(dataRow.getRowDefinition().getColumnDefinition(sColumnName).getLabel());
                }
                
            	JUtils.putNotNull(options, "label", CommonUtil.nvl(sLabel, ""));
                
                ArrayUtil<Double> auSteps = new ArrayUtil<Double>();
                
                if (!Double.isNaN(minErrorValue))
                {
                    auSteps.add(Double.valueOf(minErrorValue));
                }
                
                if (!Double.isNaN(minWarningValue))
                {
                    auSteps.add(Double.valueOf(minWarningValue));
                }
                
                if (!Double.isNaN(maxWarningValue))
                {
                    auSteps.add(Double.valueOf(maxWarningValue));
                }
                
                if (!Double.isNaN(maxErrorValue))
                {
                    auSteps.add(Double.valueOf(maxErrorValue));
                }
                
                if (auSteps.size() > 0)
                {
                    JUtils.putNotNullNumbers(options, "steps", auSteps);
                }
                else
                {
                    options.put("steps", new JreJsonNull());
                }
                
                resource.setConfiguration(options);
            }
        }
    }

}   // VaadinGaugeJs
