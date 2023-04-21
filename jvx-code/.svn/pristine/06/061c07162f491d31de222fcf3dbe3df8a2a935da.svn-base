/*
 * Copyright 2015 SIB Visions GmbH 
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
 * 07.10.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;

import javax.rad.genui.UIFactoryManager;

import com.sibvisions.rad.ui.vaadin.impl.VaadinFactory;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.ui.UI;

/**
 * The <code>VaadinFileUploadHandler</code> triggers processing of our invoke later queue, if necessary.
 * 
 * @author René Jahn
 */
public class VaadinFileUploadHandler extends FileUploadHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    protected void sendUploadResponse(VaadinRequest pRequest, VaadinResponse pResponse) throws IOException 
    {
        UI ui = UI.getCurrent();
        
        if (ui.getPushConfiguration().getPushMode().isEnabled())
        {
            VaadinSession session = ui.getSession();
            session.lock();
            
            try
            {
                //We have to be sure that our invoke later queue was checked, because the upload itself doesn't write
                //the UI changes via UidlWriter. It writes some bytes to the servlet response. The vaadin client triggers
                //a new push request to update the UI in a separate thread. The second thread doesn't know anything
                //about our invoke later queue of the upload request, so we have to perform the invoke later queue here!
                ((VaadinFactory)UIFactoryManager.getFactory()).performInvokeLater();
            }
            finally
            {
                session.unlock();
            }
        }
        
        super.sendUploadResponse(pRequest, pResponse);
    }
    
}   // VaadinFileUploadHandler
