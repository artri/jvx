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
 * 08.11.2017 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.server.SerializableConsumer;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;

/**
 * The {@link LazyComboBox} does extend the {@link ComboBox} but surpresses any
 * initial data fetches.
 * <p>
 * This is achieved by listening for the first focus event and enabling the data
 * provider afterwards.
 * 
 * @author Robert Zenz
 * @param <T> item (bean) type in ComboBox
 */
public class LazyComboBox<T> extends ComboBox<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Registration} for the focus event. */
	private Registration focusListenerRegistration = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link LazyComboBox}.
	 */
	public LazyComboBox()
	{
		super();
		
		// Setting the initial fetch size to zero should minimize
		// the initial communication, also we are not delivering
		// any data anyway until the component has gained focus at least
		// once.
		getDataCommunicator().setMinPushSize(0);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <F> SerializableConsumer<F> internalSetDataProvider(DataProvider<T, F> pDataProvider, F pInitialFilter)
	{
		if (pDataProvider instanceof IToggleableDataProvider<?, ?>)
		{
			((IToggleableDataProvider<?, ?>)pDataProvider).setEnabled(false);
			
			if (focusListenerRegistration == null)
			{
				focusListenerRegistration = addFocusListener(this::onFocusGained);
			}
		}
		
		return super.internalSetDataProvider(pDataProvider, pInitialFilter);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked when the ComboBox gains the focus.
	 * 
	 * @param pFocusEvent the event.
	 */
	private void onFocusGained(FocusEvent pFocusEvent)
	{
		DataProvider<?, ?> dataProvider = getDataProvider();
		
		if (dataProvider instanceof IToggleableDataProvider<?, ?>)
		{
			// Enable the provider and inform the control that it can now fetch
			// the data it requires.
			if (!((IToggleableDataProvider<?, ?>)dataProvider).isEnabled())
			{
				((IToggleableDataProvider<?, ?>)dataProvider).setEnabled(true);
			}
			dataProvider.refreshAll();
		}
	}
	
	//****************************************************************
	// Subinterface definition
	//****************************************************************
	
	/**
	 * {@link IToggleableDataProvider} is an {@link DataProvider} extension
	 * which allows to disable the data provider.
	 * <p>
	 * When the data provider is disable, no data should be fetched or returned,
	 * that does include that any item count inquiry returns zero.
	 * 
	 * @param <T> the data type.
	 * @param <F> the filter type.
	 * @author Robert Zenz
	 */
	public static interface IToggleableDataProvider<T, F> extends DataProvider<T, F>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * If this is enabled.
		 * 
		 * @return {@code true} if it is enabled.
		 */
		public boolean isEnabled();
		
		/**
		 * Sets whether this is enabled.
		 * 
		 * @param pEnabled {@code true} to enable it.
		 */
		public void setEnabled(boolean pEnabled);
		
	}	// IToggleable
	
}	// LazyComboBox
