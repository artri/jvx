/*
 * Copyright 2009 SIB Visions GmbH
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
 * 04.01.2011 - [RH] - creation
 */
package com.sibvisions.rad.persist.event;

import javax.rad.persist.DataSourceException;
import javax.rad.util.EventHandler;
import javax.rad.util.SilentAbortException;

/**
 * The <code>StorageEventHandler</code> is a <code>EventHandler</code> that 
 * handles Events, and throws DataSourceExceptions. 
 * 
 * @author Roland Hörmann
 * 
 * @param <L> the Listener type
 */
public class StorageEventHandler<L> extends EventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new EventHandler, the listener type may only have 1 method.
	 *  
	 * @param pListenerType the listener type interface.
	 */
	public StorageEventHandler(Class<L> pListenerType)
	{
		super(pListenerType);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object dispatchEvent(Object... pEventParameter) throws DataSourceException
	{
		try
		{
			return super.dispatchEvent(pEventParameter);
		}
		catch (SilentAbortException pSilentAbortException)
		{
			throw pSilentAbortException;
		}
		catch (DataSourceException pDataSourceException)
		{
			throw pDataSourceException;
		}
		catch (Throwable pThrowable)
		{
			throw new DataSourceException("Exception in Listenermethod!", pThrowable);
		}
	}
	
}	// StorageEventHandler
