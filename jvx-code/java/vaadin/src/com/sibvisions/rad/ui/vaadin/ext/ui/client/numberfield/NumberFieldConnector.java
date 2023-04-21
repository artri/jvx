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
 * 02.03.2018 - [JR] - update styles with textfield style as well
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.numberfield;

import com.google.gwt.core.client.JsArrayString;
import com.sibvisions.rad.ui.vaadin.ext.ui.NumberField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * The <code>NumberFieldConnector</code> class is the connector for the
 * {@link com.sibvisions.rad.ui.vaadin.ext.ui.NumberField} widget.
 * 
 * @author Thomas Krautinger
 */
@Connect(value = NumberField.class, loadStyle = LoadStyle.EAGER)
public class NumberFieldConnector extends TextFieldConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the style names. */
    private JsArrayString styleNames = JsArrayString.createArray().cast();
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
//    native static void consoleLog(String message) /*-{
//        try {
//            console.log(message);
//        } catch (e) {
//        }
//    }-*/;    

	@Override
	public void onStateChanged(StateChangeEvent pStateChangeEvent)
	{
        super.onStateChanged(pStateChangeEvent);
		
		VNumberField field = getWidget();
		
		NumberFieldState state = getState();
		
		field.setDecimalSeperator(state.decimalSeperator);
		field.setGroupingSeperator(state.groupingSeperator);
		field.setExponentSeperator(state.exponentSeperator);
		field.setMinusSign(state.minusSign);
		field.setGroupingSeperatorAllowed(state.groupingSeperatorAllowed);
		field.setPrecision(state.precision);
		field.setScale(state.scale);
		
		// The text is set by annotation
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NumberFieldState getState()
	{
		return (NumberFieldState)super.getState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VNumberField getWidget()
	{
		return (VNumberField)super.getWidget();
	}
	
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
	public void updateWidgetStyleNames()
	{
        AbstractComponentState state = getState();
        
        super.updateWidgetStyleNames();
	    
        // Remove all old stylenames
        for (int i = 0; i < styleNames.length(); i++) 
        {
            String oldStyle = styleNames.get(i);
            
            setWidgetStyleName(oldStyle, false);
            setWidgetStyleNameWithPrefix(VTextField.CLASSNAME + "-", oldStyle, false);
        }
        
        styleNames.setLength(0);

        if (ComponentStateUtil.hasStyles(state)) 
        {
            // add new style names
            for (String newStyle : state.styles) 
            {
                setWidgetStyleName(newStyle, true);
                setWidgetStyleNameWithPrefix(VTextField.CLASSNAME + "-", newStyle, true);
                
                styleNames.push(newStyle);
            }
        }
	}
	
}   // NumberFieldConnector
