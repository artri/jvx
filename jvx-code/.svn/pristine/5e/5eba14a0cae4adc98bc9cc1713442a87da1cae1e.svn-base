/*
 * Copyright 2013 SIB Visions GmbH
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
 * 25.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import java.lang.reflect.Method;
import java.util.Map;

import javax.rad.model.ui.ICellEditorListener;

import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.vaadin.event.ConnectorEventListener;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.util.ReflectTools;

/**
 * The <code>ExtendedPopupDateField</code> class is the server-side implementation of 
 * the PopupDateField component, which displays the actual date selector as a popup.
 * The class has the information if the popup is open or not.
 * 
 * @author Stefan Wurm
 */
public class ExtendedPopupDateField extends DateTimeField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link RegistrationContainer} for all registrations. */
	private RegistrationContainer registrations = new RegistrationContainer();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * Creates a new instance of {@link ExtendedPopupDateField}.
	 */
	public ExtendedPopupDateField()
	{
		super();
		
		setReadOnly(false);
		setResolution(DateTimeResolution.SECOND);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
    public void changeVariables(Object source, Map<String, Object> variables) 
    {
    	super.changeVariables(source, variables);

    	if (variables.containsKey(ExtendedBlurEvent.EVENT_ID)) 
        {
            fireEvent(new ExtendedBlurEvent(this, (String)variables.get(ExtendedBlurEvent.EVENT_ID)));
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Adds the extendedBlurListener to the component.
	 * 
	 * @param listener the extended blur listener.
	 */
    public void addExtendedBlurListener(ExtendedBlurListener listener) 
    {
        registrations.add(addListener(ExtendedBlurEvent.EVENT_ID, ExtendedBlurEvent.class, listener, ExtendedBlurListener.METHOD_BLUR));
    }

	/**
	 * Removes the extendedBlurListener to the component.
	 * 
	 * @param listener the extended blur listener.
	 */
    public void removeExtendedBlurListener(ExtendedBlurListener listener) 
    {
        registrations.removeAll();
    }

	//****************************************************************
	// Subclass definition
	//****************************************************************
    
    /**
     * <code>BlurListener</code> interface for listening for
     * <code>BlurEvent</code> fired by a <code>Field</code>.
     * 
     * @see com.vaadin.event.FieldEvents.BlurEvent
     */
    public interface ExtendedBlurListener extends ConnectorEventListener 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Constants
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** Exended Blur Method. **/
        public static final Method METHOD_BLUR = ReflectTools.findMethod(ExtendedBlurListener.class, "extendedBlur", ExtendedBlurEvent.class);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Method definitions
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Component has been blurred.
         * 
         * @param event
         *            Component blur event.
         */
        public void extendedBlur(ExtendedBlurEvent event);
        
    }   // ExtendedBlurListener
    
    /**
     * <code>BlurEvent</code> class for holding additional event information.
     * Fired when a <code>Field</code> loses keyboard focus.
     */
    public static class ExtendedBlurEvent extends Component.Event 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Constants
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** Identifier for event that can be used in {@link com.vaadin.event.EventRouter}. */
        public static final String EVENT_ID = "extendedBlur";
        /** The complete type. */
        private String completeType;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Method definitions
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Initiailize.
         * 
         * @param pSource the source component.
         * @param pCompleteType the completion type.
         */
        public ExtendedBlurEvent(Component pSource, String pCompleteType) 
        {
            super(pSource);
            
            if (ICellEditorListener.FOCUS_LOST.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.FOCUS_LOST;
            }
            else if (ICellEditorListener.ENTER_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.ENTER_KEY;
            }
            else if (ICellEditorListener.ESCAPE_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.ESCAPE_KEY;
            }
            else if (ICellEditorListener.SHIFT_ENTER_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.SHIFT_ENTER_KEY;
            }
            else if (ICellEditorListener.TAB_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.TAB_KEY;
            }
            else if (ICellEditorListener.SHIFT_TAB_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.SHIFT_TAB_KEY;
            }
            else if (ICellEditorListener.ACTION_KEY.equals(pCompleteType))
            {
            	completeType = ICellEditorListener.ACTION_KEY;
            }
        }
        
        /**
         * The complete type.
         * @return The complete type.
         */
        public String getCompleteType()
        {
        	return completeType;
        }
        
    }   // ExtendedBlurEvent

}	// ExtendedPopupDateField
