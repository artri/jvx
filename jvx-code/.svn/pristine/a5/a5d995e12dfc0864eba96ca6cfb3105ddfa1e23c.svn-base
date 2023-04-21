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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VAbstractCalendarPanel.SubmitListener;
import com.vaadin.client.ui.VPopupTimeCalendar;

/**
 * Represents a date selection component with a text field and a popup date
 * selector.
 * 
 * <b>Note:</b> To change the keyboard assignments used in the popup dialog you
 * should extend <code>com.vaadin.client.ui.VCalendarPanel</code> and then pass
 * set it by calling the <code>setCalendarPanel(VCalendarPanel panel)</code>
 * method.
 */
public class VExtendedPopupDateField extends VPopupTimeCalendar implements BlurHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** popup is showing. */
	private boolean isShowing = false;

	/** ignore focus. */
	private boolean ignoreFocus = false;

	/** Reason for closing. */
	private String blurType = "FOCUS_LOST";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>VExtendedPopupDateField</code>.
     */
	 public VExtendedPopupDateField() 
	 {
		super();

		text.addBlurHandler(this);
		addDomHandler(this, KeyDownEvent.getType());
		 
		calendar.setSubmitListener(new SubmitListener() 
		{
			@Override
			public void onSubmit() 
			{
				blurType = "ENTER_KEY";

				updateValue(calendar.getDate());

				buildDate(true);

				closeCalendarPanel();
			}

			@Override
			public void onCancel() 
			{
		  		blurType = "ESCAPE_KEY";

				closeCalendarPanel();
			}
        });
	 }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	@SuppressWarnings("deprecation")
	@Override
	public void onBlur(final BlurEvent event)
	{
	  	if (getClient() != null && getClient().hasEventListeners(this, "extendedBlur")) 
		{
			Scheduler.get().scheduleDeferred(new Command() // It has to be deferred, to ensure active element is already new one.
			{
				@Override
				public void execute()
				{
			    	  if (getActiveElement() != calendarToggle.getElement() // Send extended blur only, if next active element is not popup panel
			    			  && getActiveElement() != calendar.getElement()
			    			  && (getActiveElement() != text.getElement() || !"FOCUS_LOST".equals(blurType)))
			    	  {
						  getClient().updateVariable(getId(), "extendedBlur", blurType, true);
			    	  }
			    	  blurType = "FOCUS_LOST";
				}
			});
		}
	}
	
    @Override
    public void focus() 
    {
    	if (!ignoreFocus) // ensure that under ie Vaadin is not calling setFocus(true) on text, as it leads in dual focus.
    	{
    		text.setFocus(true);
    	}
    }

    /**
     * Opens the calendar panel popup.
     */
    public void openCalendarPanel() 
    {
    	blurType = "FOCUS_LOST";
    	
    	isShowing = true;
    	super.openCalendarPanel();
    }

	@SuppressWarnings("deprecation")
	@Override
    public void onClose(CloseEvent<PopupPanel> event) 
    {
		isShowing = false;

		ignoreFocus = true;
		super.onClose(event);
		ignoreFocus = false;

    	Scheduler.get().scheduleDeferred(new Command() // It has to be deferred, to ensure active element is already new one.
		{
			@Override
			public void execute()
			{
		    	if (getActiveElement() != calendarToggle.getElement()
						&& (getActiveElement() != text.getElement())) // Send extended blur only, if next active element is not text panel
				{
					if ("ESCAPE_KEY".equals(blurType))
				  	{
				  		focus();
				  	}
					else
					{
					  	if (getClient() != null && getClient().hasEventListeners(VExtendedPopupDateField.this, "extendedBlur")) 
						{
					  		if (BrowserInfo.get().isIE() && "FOCUS_LOST".equals(blurType) && getActiveElement() == null)
					  		{
					  			focus();
					  		}
					  		else
					  		{
					  			getClient().updateVariable(getId(), "extendedBlur", blurType, true);
					  		}
						}
					}
				}
		    	blurType = "FOCUS_LOST";
			}
		});
    }

    @Override
    public void onClick(ClickEvent event) 
    {
    	super.onClick(event);
    	
    	if (!isShowing && !(BrowserInfo.get().isAndroid() || BrowserInfo.get().isIOS()))
    	{
    		focus();
    	}
    }

    @Override
    public void onKeyDown(KeyDownEvent event) 
    {
    	if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
        {
        	if (event.isShiftKeyDown())
        	{
        		blurType = "SHIFT_ENTER_KEY";
        	}
        	else
        	{
        		blurType = "ENTER_KEY";
        	}
        }
        else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) 
        {
        	blurType = "ESCAPE_KEY";
        }

    	super.onKeyDown(event);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Gets the current active element. 
	 * @return the current active element. 
	 */
	public static native Element getActiveElement() /*-{
     	return $doc.activeElement; 
    }-*/; 

}  	// VExtendedPopupDateField
