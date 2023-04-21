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
 * 25.07.2018 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedTreeTable;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>TreeTableConnector</code> extends the {@link com.vaadin.v7.client.ui.treetable.TreeTableConnector} and adds
 * support for focus events.
 * 
 * @author Martin Handsteiner
 */
@Connect(ExtendedTreeTable.class)
public class TreeTableConnector extends com.vaadin.v7.client.ui.treetable.TreeTableConnector
                         implements FocusHandler, BlurHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init()
	{
		super.init();
		
		getWidget().scrollBodyPanel.addBlurHandler(this);
		getWidget().scrollBodyPanel.addFocusHandler(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBlur(BlurEvent pEvent)
	{
		getFocusAndBlurServerRpc().blur();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFocus(FocusEvent pEvent)
	{
		getFocusAndBlurServerRpc().focus();
	}

	/**
	 * Gets the focus and blur server rpc.
	 * @return the focus and blur server rpc.
	 */
	@SuppressWarnings("deprecation")
	private FocusAndBlurServerRpc getFocusAndBlurServerRpc() 
    {
        return getRpcProxy(FocusAndBlurServerRpc.class);
    }

}   // TableConnector
