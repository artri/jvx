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
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.List;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.data.DataCommunicatorConnector;
import com.vaadin.client.ui.VComboBox;
import com.vaadin.client.ui.combobox.ComboBoxConnector;
import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

/**
 * The {@link EndlessDataCommunicatorConnector} is an
 * {@link DataCommunicatorConnector} extension which sets the
 * {@link ResetDelayingVaadinDataSource} on {@link ComboBoxConnector}s.
 * 
 * @author Robert Zenz
 */
@Connect(DataCommunicator.class)
public class EndlessDataCommunicatorConnector extends DataCommunicatorConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link EndlessDataCommunicatorConnector}.
	 */
	public EndlessDataCommunicatorConnector()
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
	protected void extend(ServerConnector pTarget)
	{
		if (getParent() instanceof ComboBoxConnector)
		{
			((ComboBoxConnector)getParent()).setDataSource(new ResetDelayingVaadinDataSource((ComboBoxConnector)getParent()));
		}
		else
		{
			super.extend(pTarget);
		}
	}

	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ResetDelayingVaadinDataSource} is delaying a reset when the
	 * data stream "suddenly ends" until the popup is closed.
	 * <p>
	 * This makes sure that the the popup does not jump back to the start once
	 * the end of the "endless" data stream has been reached.
	 * 
	 * @author Robert Zenz
	 */
	protected class ResetDelayingVaadinDataSource extends VaadinDataSource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link VComboBox} that is the target. */
		private VComboBox comboBox = null;
		
		/** The {@link ComboBoxConnector} that is the target. */
		private ComboBoxConnector connector = null;
		
		/** If a reset has been delayed. */
		private boolean delayedReset = false;
		
		/** True, if reset was forced. */
		private boolean resetForced = false;
        /** True, if reset was forced. */
        private boolean delayClick = false;
		
		/** The previously seen filter. */
		private String previousFilter;
		
		/** The current size of the data. */
		private int size = 0;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ResetDelayingVaadinDataSource}.
		 *
		 * @param pParent the {@link ComboBoxConnector parent}.
		 */
		public ResetDelayingVaadinDataSource(ComboBoxConnector pParent)
		{
			super();
			
			connector = pParent;
			comboBox = connector.getWidget();
			comboBox.suggestionPopup.addCloseHandler(this::doDelayedResetIfNeeded);
			
			// For some reason, some times the combobox stops fetching data.
			// We will force it to do so when it receives the focus.
			
			// At now using String ids this behaviour couldn't be reproduced.
			// We try for now without this workaround. If problems in fetching data occurs,
			// this hat to be enabled again.
			comboBox.tb.addHandler(this::doForceReset, FocusEvent.getType());
			
			previousFilter = comboBox.lastFilter;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size()
		{
			// Return our custom size so that even though no reset has been
			// triggered, the VComboBox still receives the correct data set
			// size and can behave correctly.
			return size;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void resetDataAndSize(int pNewSize)
		{
			// We only want to actually execute the reset when we are not
			// in the middle of displaying data. If we are on, say, page 30 and
			// the data is reset the ComboBox would jump back to page 0. So
			// if we are not on the first page, we do not want to trigger the
			// reset.
			// Additionally we can safely reset when the popup is not open and
			// we must also reset if the filter term has changed.
            size = pNewSize;
			if (!comboBox.suggestionPopup.isShowing()
					|| size == 0
					|| (previousFilter != comboBox.lastFilter
							&& (previousFilter == null
									|| !previousFilter.equals(comboBox.lastFilter))))
			{
				size = pNewSize;
				super.resetDataAndSize(pNewSize);
			}
			else
			{
				delayedReset = true;
	            delayClick = false;
			}
			
			previousFilter = comboBox.lastFilter;
		}
		
        /**
         * {@inheritDoc}
         */
        @Override
	    protected void setRowData(int firstRowIndex, List<JsonObject> rowData) 
        {
            super.setRowData(firstRowIndex, rowData);
            if (resetForced && !delayClick)
            {
                if (comboBox.suggestionPopup.isShowing())
                {
                    comboBox.getDataReceivedHandler().popupOpenerClicked();
                }
                resetForced = false;
            }
            delayClick = false;
        }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Invoked when the popup closes and does perform a reset if needed.
		 * 
		 * @param pEvent the event.
		 */
		private void doDelayedResetIfNeeded(CloseEvent<PopupPanel> pEvent)
		{
			if (delayedReset)
			{
				super.resetDataAndSize(size);
				
				delayedReset = false;
			}
		}
		
		/**
		 * Invoked when the textbox gains the focus to reset the data within.
		 *  
		 * @param pEvent the event.
		 */
		private void doForceReset(FocusEvent pEvent)
		{
		    // Mark, that focus was gained for delaying selection of current item
            resetForced = true;
            delayClick = true;
			// We have to reset last Filter search, otherwise, on focus gained the last search is used for fetching data.
			comboBox.filterOptions(0, "");
			// Force it to load at least one row.
			resetDataAndSize(50);
		}
		
	}	// ResetDelayingVaadinDataSource
	
}	// EndlessDataCommunicatorConnector
