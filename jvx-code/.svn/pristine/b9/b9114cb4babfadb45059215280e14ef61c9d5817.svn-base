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
 * 03.07.2009 - [RH] - creation
 */
package javax.rad.model.event;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataSource;

/**
 * The <code>DataSourceEvent</code> gives information about changes in the 
 * {@link IDataSource}.
 * 
 * @see javax.rad.model.IDataSource
 * 
 * @author René Jahn
 */
public class DataSourceEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Specifies the type of event. */
	public enum EventType 
	{
		/** databook added. */
		ADD,
		/** databook removed. */
		REMOVE,
	}
	
	/** the datasource. */
    private IDataSource dataSource;

    /** the databook. */
	private IDataBook dataBook;
	
	/** the event type. */
	private EventType eventType;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DataSourceEvent</code>.
	 * 
	 * @param pDataSource the {@link IDataSource}.
	 * @param pDataBook the {@link IDataBook}.
	 * @param pEventType the event type.
	 */
	public DataSourceEvent(IDataSource pDataSource, IDataBook pDataBook, EventType pEventType)
	{
	    dataSource = pDataSource;
		dataBook = pDataBook;
		eventType = pEventType;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the datasource.
     * 
     * @return the datasource.
     */
    public IDataSource getDataSource()
    {
        return dataSource;
    }
    
	/**
	 * Gets the databook.
	 * 
	 * @return the databook.
	 */
	public IDataBook getDataBook()
	{
		return dataBook;
	}
	
	/**
	 * Gets the event type.
	 * 
	 * @return the type.
	 */
	public EventType getEventType()
	{
		return eventType;
	}
		
}	// DataSourceEvent
