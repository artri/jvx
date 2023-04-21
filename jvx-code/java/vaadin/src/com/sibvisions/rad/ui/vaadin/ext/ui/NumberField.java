/*
 * Copyright 2014 SIB Visions GmbH
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
 * 08.04.2014 - [TK] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.numberfield.NumberFieldState;
import com.sibvisions.util.type.NumberUtil;
import com.vaadin.ui.TextField;

/**
 * The <code>NumberField</code> class is the server-side component of an number field.
 *  
 * @author Thomas Krautinger
 */
public class NumberField extends TextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The number util. */
	private NumberUtil numberUtil = new NumberUtil();
	
	/** the precision. */
	private int iPrecision = -1;
	
	/** the scale. */
	private int iScale = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>NumberField</code>.
	 */
    public NumberField()
    {
        super();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected NumberFieldState getState()
    {
        return (NumberFieldState)super.getState();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String pValue)
    {
        super.setValue(pValue);
        
        updateState();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  	// User-defined methods
  	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
    /**
     * Gets the current used decimal format.
     * 
     * @return the current used decimal format.
     */
    public DecimalFormat getDecimalFormat()
    {
        return numberUtil.getDecimalFormat();
    }

    /**
     * Gets the locale.
     * 
     * @return the locale.
     */
    public Locale getLocale()
    {
        return numberUtil.getLocale();
    }

  	/**
  	 * Gets the number pattern.
  	 * 
  	 * @return the number pattern.
  	 */
  	public String getNumberPattern()
  	{
  		return numberUtil.getNumberPattern();
  	}

  	/**
  	 * Gets the number pattern.
  	 * 
  	 * @param pNumberPattern the number pattern.
  	 */
  	public void setNumberPattern(String pNumberPattern)
  	{
  		numberUtil.setNumberPattern(pNumberPattern);
  		
  		updateState();
  	}  
  	
    /**
     * Sets the number pattern and locale.
     * 
     * @param pNumberPattern the number pattern.
     * @param pLocale the locale.
     */
    public void setNumberPattern(String pNumberPattern, Locale pLocale)
    {
        numberUtil.setNumberPattern(pNumberPattern, pLocale);
        
        updateState();
    }  
    
  	/**
  	 * Sets the precision.
  	 * 
  	 * @param pPrecision the precision
  	 */
  	public void setPrecision(int pPrecision)
  	{
  	    iPrecision = pPrecision;
  	    
  	    updateState();
  	}
  	
  	/**
  	 * Sets the scale.
  	 * 
  	 * @param pScale the scale
  	 */
  	public void setScale(int pScale)
  	{
  	    iScale = pScale;
  	    
  	    updateState();
  	}
    
    /**
     * Updates the widget state.
     */
    private void updateState()
    {
        if (numberUtil != null)
        {
        	DecimalFormat numberFormat = getDecimalFormat();
        	
        	DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        	
        	NumberFieldState state = getState();
        	
        	state.decimalSeperator = symbols.getDecimalSeparator();
        	state.groupingSeperator = symbols.getGroupingSeparator();
        	state.exponentSeperator = symbols.getExponentSeparator();
        	state.minusSign = symbols.getMinusSign();
        	state.groupingSeperatorAllowed = numberFormat.isGroupingUsed();
        	state.precision = iPrecision;
        	state.scale = iScale;
        }
    }
    
}   // NumberField
