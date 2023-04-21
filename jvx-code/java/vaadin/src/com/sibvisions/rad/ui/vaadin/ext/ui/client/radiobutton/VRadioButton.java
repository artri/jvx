/*
 * Copyright 2012 SIB Visions GmbH
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
 * 04.01.2013 - [SW] - creation
 * 07.08.2015 - [JR] - #1445: redesign
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.radiobutton;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.Util;
import com.vaadin.client.VTooltip;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.aria.AriaHelper;
import com.vaadin.client.ui.aria.HandlesAriaInvalid;
import com.vaadin.client.ui.aria.HandlesAriaRequired;

/**
 * The client-side (GWT) widget that will render the component {@link com.sibvisions.rad.ui.vaadin.ext.ui.RadioButton} in the browser.
 * It is a copy of  {@link com.vaadin.client.ui.VCheckBox} widget but extends {@link com.google.gwt.user.client.ui.RadioButton}.
 * 
 * @author Stefan Wurm
 */
public class VRadioButton extends com.google.gwt.user.client.ui.RadioButton 
                          implements Field,
                                     HandlesAriaInvalid, 
                                     HandlesAriaRequired
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** The classname. **/
    public static final String CLASSNAME = "v-radiobutton";

	/** For internal use only. May be removed or replaced in the future. */
	public String id;
	
	/** For internal use only. May be removed or replaced in the future. */
	public boolean immediate;
	
	/** For internal use only. May be removed or replaced in the future. */
	public ApplicationConnection client;
	
	/** For internal use only. May be removed or replaced in the future. */
	public Element errorIndicatorElement;
	
	/** the input element. */
	public InputElement elInput;
	
	/** For internal use only. May be removed or replaced in the future. */
	Icon icon;
	
	/** the old value. */
	private boolean oldValue;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
    /**
     * Creates a new instance of <code>VRadioButton</code>.
     */
    public VRadioButton() 
    {
        super("");
        
        setStyleName(CLASSNAME);

        Element el = getElement();
        
        Element elCurrent = DOM.getFirstChild(el);
        
        while (elCurrent != null) 
        {
            DOM.sinkEvents(elCurrent, (DOM.getEventsSunk(el) | VTooltip.TOOLTIP_EVENTS));
            elCurrent = DOM.getNextSibling(elCurrent);
        }
        
        for (int i = 0, cnt = el.getChildCount(); i < cnt && icon == null; i++)
        {
        	if (el.getChild(i) instanceof InputElement)
        	{
        		elInput = (InputElement)el.getChild(0);
        	}
        }

        if (BrowserInfo.get().isWebkit()) 
        {
            // Webkit does not focus non-text input elements on click
            // (#11854)
            addClickHandler(new ClickHandler() 
            {
                @Override
                public void onClick(ClickEvent event) 
                {
                    setFocus(true);
                }
            });
        }	
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void setAriaRequired(boolean required) 
    {
        AriaHelper.handleInputRequired(elInput, required);
    }

    /**
     * {@inheritDoc}
     */
    public void setAriaInvalid(boolean invalid) 
    {
        AriaHelper.handleInputInvalid(elInput, invalid);
    }    
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Boolean pValue)
    {
    	super.setValue(pValue);
    	
        oldValue = getValue().booleanValue();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
	@Override
    public void onBrowserEvent(Event pEvent) 
    {
    	int type = pEvent.getTypeInt();
    	
    	Element target = DOM.eventGetTarget(pEvent);
    	
    	switch (type)
    	{
	        case Event.ONMOUSEUP:
	        case Event.ONBLUR:
	        case Event.ONKEYDOWN:
	          // Note the old value for onValueChange purposes (in ONCLICK case)
	          oldValue = getValue().booleanValue();
	          break;
	        case Event.ONCLICK:
	        	if (icon != null && target == icon.getElement()) 
	        	{
	        		// Click on icon should do nothing if widget is disabled
	        		if (isEnabled()) 
	        		{
	        			setValue(Boolean.valueOf(!getValue().booleanValue()));
	        		}
	        	}
	          
	        	if (target == elInput)
	        	{
	        		if (isEnabled())
	        		{
	        			if (oldValue)
	        			{
	        	    		setValue(Boolean.FALSE);
	        			}
	        		}
	        	}
	        	
	        	break;
	        default:
	        	//nothing
    	}
    	
    	
		super.onBrowserEvent(pEvent);

		if (pEvent.getTypeInt() == Event.ONLOAD) 
		{
			Util.notifyParentOfSizeChange(this, true);
		}
    }    
    
} 	// VRadioButton
