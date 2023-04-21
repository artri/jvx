/*
 * Copyright 2017 SIB Visions GmbH
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
 * 24.11.2017 - [RZ] - creation
 * 08.07.2019 - [JR] - aria-expanded recognized
 */
package com.vaadin.client.ui;

import com.google.gwt.aria.client.ExpandedValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Element;

/**
 * The {@link VEndlessSuggestionsComboBox} is an {@link VComboBox} extension
 * which always displays one more page of suggestions as long as the
 * {@link #getTotalSuggestions() total suggestion number} is
 * {@link Integer#MAX_VALUE}.
 * 
 * @author Robert Zenz
 */
public class VEndlessSuggestionsComboBox extends VComboBox
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VEndlessSuggestionsComboBox}.
	 */
	public VEndlessSuggestionsComboBox()
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
    public void updatePlaceholder() 
	{
        if (inputPrompt != null) //  && enabled && !readonly // removed, as VComboBox is the only component checking this
        {
            tb.getElement().setAttribute("placeholder", inputPrompt);
        } 
        else 
        {
            tb.getElement().removeAttribute("placeholder");
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SuggestionPopup createSuggestionPopup()
	{
		return new EndlessSuggestionPopup();
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link EndlessSuggestionPopup} is a {@link SuggestionPopup} extension
	 * which always displays one more page of suggestions as long as the
	 * {@link #getTotalSuggestions() total suggestion number} is
	 * {@link Integer#MAX_VALUE}.
	 * 
	 * @author Robert Zenz
	 */
	public class EndlessSuggestionPopup extends SuggestionPopup
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link Element} of the status line. */
		protected Element statusElement = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link EndlessSuggestionPopup}.
		 */
		protected EndlessSuggestionPopup()
		{
			super();
			
			statusElement = (Element)getContainerElement().getChild(getContainerElement().getChildCount() - 1);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showSuggestions(int pCurrentPage)
		{
			super.showSuggestions(pCurrentPage);
			
			String statusText = statusElement.getInnerText();
			
			if (getTotalSuggestions() == Integer.MAX_VALUE && statusText.contains("/"))
			{
				statusText = statusText.substring(0, statusText.indexOf("/") + 1);
				statusText = statusText + "?"; // Cached size ((VaadinDataSource)connector.getDataSource()).getCachedRange().getEnd() + "+";
				
				statusElement.setInnerText(statusText);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onAttach()
		{
			//set the state for the text field
			Roles.getButtonRole().setAriaExpandedState(super.getOwner().getElement(), ExpandedValue.TRUE);
			
			super.onAttach();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onDetach()
		{
			//set the state for the text field
			Roles.getButtonRole().setAriaExpandedState(super.getOwner().getElement(), ExpandedValue.FALSE);
			
			super.onDetach();
		}

	}	// EndlessSuggestionPopup
	
}	// VEndlessSuggestionsComboBox
