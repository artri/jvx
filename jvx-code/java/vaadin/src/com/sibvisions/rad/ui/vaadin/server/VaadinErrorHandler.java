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
 * 08.03.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.server;

import javax.rad.util.SilentAbortException;

import com.sibvisions.util.log.LoggerFactory;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;

/**
 * The <code>VaadinErrorHandler</code> class is a special handler that ignores {@link SilentAbortException}s.
 * 
 * @author Stefan Wurm
 */
public class VaadinErrorHandler extends DefaultErrorHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void error(ErrorEvent pEvent) 
	{
		Throwable th = pEvent.getThrowable();
    		
		while (th != null)
		{
			if (th instanceof SilentAbortException)
			{
				LoggerFactory.getInstance(VaadinErrorHandler.class).info(pEvent.getThrowable());
				
				return;
			}
			
			th = th.getCause();
		}
		
		doDefault(pEvent);
    }

}	// VaadinErrorHandler
