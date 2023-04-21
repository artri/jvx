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
import com.sibvisions.rad.ui.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.communication.ServletUIInitHandler;
import com.vaadin.ui.UI;


/**
 * Handles an initial request from the client to initialize a {@link UI}.
 * 
 * @author Stefan Wurm
 */
public class VaadinServletUIInitHandler extends ServletUIInitHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean synchronizedHandleRequest(VaadinSession pSession, VaadinRequest pRequest, VaadinResponse pResponse) throws IOException
    {
        if (!isInitRequest(pRequest))
        {
            return false;
        }

        //if preserveUIOnRefresh was active and is now inactive -> we should close the "old" UI
        //this is relevant if the option will be used as URL parameter -> does nothing if configured via web.xml
        if (!((VaadinServletService)pSession.getService()).isPreserveUIOnRefresh())
        {
        	String embedId = getEmbedId(pRequest);
        	
        	UI retainedUI = pSession.getUIByEmbedId(embedId);
        	
        	if (retainedUI != null)
		    {
        		try
        		{
        			retainedUI.close();
        		}
        		finally
        		{
        		    try
        		    {
        		        pSession.removeUI(retainedUI);
        		    }
        		    catch (Throwable th)
        		    {
        		        //ignore
        		    }
        		}
		    }
        }
        
        return super.synchronizedHandleRequest(pSession, pRequest, pResponse);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected String getInitialUidl(VaadinRequest pRequest, UI pUI) throws IOException 
    {
		try
		{
			((VaadinUI)pUI).notifyBeforeUI();

			return super.getInitialUidl(pRequest, pUI);
		}
		finally
		{
			((VaadinUI)pUI).notifyAfterUI();
		}
    }
	
}	// VaadinServletUIInitHandler
