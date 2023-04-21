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

import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IComponent;

/**
 * Platform and technology independent gauge definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IGauge extends IComponent, 
                                IEditorControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Style constant for showing a speedometer. */
    public static final int STYLE_SPEEDOMETER = 0;
    
    /** Style constant for showing a meter. */
    public static final int STYLE_METER = 1;
    
    /** Style constant for showing a ring. */
    public static final int STYLE_RING = 2;
    
    /** Style constant for showing a flat meter. */
    public static final int STYLE_FLAT = 3;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the gauge style.
	 * 
	 * @return the gauge style.
	 */
	public int getGaugeStyle();

	/**
	 * Sets the gauge style.
	 * 
	 * @param pGaugeStyle the gauge style.
	 */
	public void setGaugeStyle(int pGaugeStyle);

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
	 * Gets the min value.
	 * 
	 * @return the min value.
	 */
	public double getMinValue();
	
    /**
     * Gets the max value.
     * 
     * @param pMinValue the max value.
     */
    public void setMinValue(double pMinValue);
    
    /**
     * Gets the max value.
     * 
     * @return the max value.
     */
    public double getMaxValue();
    
    /**
     * Gets the min value.
     * 
     * @param pMaxValue the min value.
     */
    public void setMaxValue(double pMaxValue);

    /**
     * Gets the min error value.
     * 
     * @return the min error value.
     */
    public double getMinErrorValue();
    
    /**
     * Gets the min error value.
     * 
     * @param pMinErrorValue the min error value.
     */
    public void setMinErrorValue(double pMinErrorValue);
    
    /**
     * Gets the max error value.
     * 
     * @return the max error value.
     */
    public double getMaxErrorValue();
    
    /**
     * Gets the max error value.
     * 
     * @param pMaxErrorValue the max error value.
     */
    public void setMaxErrorValue(double pMaxErrorValue);
    
    /**
     * Gets the min warning value.
     * 
     * @return the min warning value.
     */
    public double getMinWarningValue();
    
    /**
     * Gets the min warning value.
     * 
     * @param pMinWarningValue the min warning value.
     */
    public void setMinWarningValue(double pMinWarningValue);
    
    /**
     * Gets the max value.
     * 
     * @return the max value.
     */
    public double getMaxWarningValue();
    
    /**
     * Gets the max warning value.
     * 
     * @param pMaxWarningValue the max warning value.
     */
    public void setMaxWarningValue(double pMaxWarningValue);
    
    /**
     * Sets the column name which should be used for the label.
     * 
     * @param pColumnName the column name
     */
    public void setLabelColumnName(String pColumnName);
    
    /**
     * Gets the column name which will be used for the label.
     * 
     * @return the column name
     */
    public String getLabelColumnName();
    
}	// IGauge
