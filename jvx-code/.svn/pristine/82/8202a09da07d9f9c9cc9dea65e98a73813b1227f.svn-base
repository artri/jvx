/*
 * Copyright 2021 SIB Visions GmbH
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
 * 29.04.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VButton;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.Upload;

/**
 * The <code>UploadConnector</code> extends the {@link com.vaadin.client.ui.upload.UploadConnector} and adds
 * icon support.
 * 
 * @author René Jahn
 */
@Connect(Upload.class)
public class UploadConnector extends com.vaadin.client.ui.upload.UploadConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void updateFromUIDL(UIDL pUidl, ApplicationConnection pClient) 
	{
		super.updateFromUIDL(pUidl, pClient);
		
        if (!isRealUpdate(pUidl)
        	|| pUidl.hasAttribute("notStarted")) 
        {
            return;
        }

        VButton button = getWidget().submitButton;
        
        if (button.icon != null) 
        {
        	button.wrapper.removeChild(button.icon.getElement());
        	button.icon = null;
        }
        
        Icon icon = getIcon();
        
        if (icon != null) 
        {
        	button.icon = icon;

        	button.wrapper.insertBefore(icon.getElement(), button.captionElement);
        }
	}

}   // UploadConnector
