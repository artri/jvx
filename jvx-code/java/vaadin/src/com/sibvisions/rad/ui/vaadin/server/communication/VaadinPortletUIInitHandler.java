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
 * 28.10.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.communication.PortletUIInitHandler;
import com.vaadin.ui.UI;

/**
 * Handles an initial request from the client to initialize a {@link UI}.
 * 
 * @author Stefan Wurm
 */
public class VaadinPortletUIInitHandler extends PortletUIInitHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	@Override
    protected String getInitialUidl(VaadinRequest pRequest, UI pUI) throws IOException 
    {
	    try
		{
			((VaadinUI) pUI).notifyBeforeUI();

			return super.getInitialUidl(pRequest, pUI);
		}
		finally
		{
			((VaadinUI)pUI).notifyAfterUI();
		}
    }
	
}   // VaadinPortletUIInitHandler
