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
 * 24.07.2018 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IFactory;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.BlurNotifier;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.shared.Registration;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.v7.ui.Table;

/**
 * Extended Table with focus events. 
 * 
 * @author Martin Handsteiner
 */
@SuppressWarnings("deprecation")
public class ExtendedTable extends Table implements FocusNotifier, BlurNotifier, Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** InvokeLater is registered. */
	boolean invokeLater = false;
	
	/** The last event when invoke later is called. */
	protected boolean focus = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new ExtendedTable.
	 */
	public ExtendedTable()
	{
		super();
		
		registerRpc(new PropagatingFocusAndBlurServerRpc());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Registration addBlurListener(BlurListener pBlurListener)
	{
        return addListener(BlurEvent.EVENT_ID, BlurEvent.class, pBlurListener, BlurListener.blurMethod);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Registration addFocusListener(FocusListener pFocusListener)
	{
        return addListener(FocusEvent.EVENT_ID, FocusEvent.class, pFocusListener, FocusListener.focusMethod);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run()
	{
		invokeLater = false;
		
		if (!focus)
		{
			fireEvent(new BlurEvent(this));
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Removes a blur listener.
	 * 
	 * @param pBlurListener the blur listener.
	 */
	public void removeBlurListener(BlurListener pBlurListener)
	{
        removeListener(BlurEvent.EVENT_ID, BlurEvent.class, pBlurListener);
	}
	
	/**
	 * Removes a focus listener.
	 * 
	 * @param pFocusListener the focus listener.
	 */
	public void removeFocusListener(FocusListener pFocusListener)
	{
        removeListener(FocusEvent.EVENT_ID, FocusEvent.class, pFocusListener);
	}
	
	/**
	 * Fires the blur event.
	 */
	protected void fireBlurEvent()
	{
		focus = false;
		
		if (!invokeLater)
		{
			IFactory factory = UIFactoryManager.getFactory();

			if (factory != null)
			{			
				invokeLater = true;
				
				factory.invokeLater(this);
			}
		}
	}
	
	/**
	 * Fires the focus event.
	 */
	protected void fireFocusEvent()
	{
		focus = true;
		
		if (!invokeLater)
		{
			IFactory factory = UIFactoryManager.getFactory();

			if (factory != null)
			{
				invokeLater = true;
				
				factory.invokeLater(this);						
			}
			
			fireEvent(new FocusEvent(this));			
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * Focus and Blur Rpc.
	 * 
	 * @author Robert Zenz
	 */
	private final class PropagatingFocusAndBlurServerRpc implements FocusAndBlurServerRpc
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void blur()
		{
			fireBlurEvent();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void focus()
		{
			fireFocusEvent();
		}
		
	}	// PropagatingFocusAndBlurServerRpc
	
}	// ExtendedTable
