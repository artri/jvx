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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.numberfield;

import com.vaadin.shared.ui.textfield.TextFieldState;

/**
 * The <code>NumberFieldState</code> class is the state for the {@link com.sibvisions.rad.ui.vaadin.ext.ui.NumberField} widget.
 * 
 * @author Thomas Krautinger
 */
public class NumberFieldState extends TextFieldState
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	{
        primaryStyleName = "v-numberfield";
    }
	
	/** The exponent seperator. */
	public String exponentSeperator;
	
    /** the precision. */
    public int precision;
    
    /** the scale. */
    public int scale;

    /** The decimal seperator. */
    public char decimalSeperator;
    
    /** The grouping seperator. */
    public char groupingSeperator;
    
    /** The minus sign. */
	public char minusSign;
	
	/** <code>True</code> if the input of an grouping seperator is allowed, otherwise <code>false</code>. */
	public boolean groupingSeperatorAllowed;
	
}   // NumberFieldState
