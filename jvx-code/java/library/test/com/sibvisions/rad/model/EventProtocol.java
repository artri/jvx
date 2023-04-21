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
 * 01.10.2008 - [RH] - creation
 * 03.04.2014 - [RZ] - now holds a list of EventEntry's instead of strings. 
 */
package com.sibvisions.rad.model;

import java.util.List;

import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.event.IDataRowListener;
import javax.rad.model.ui.IControl;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.model.EventProtocol.EventEntry.EventType;
import com.sibvisions.util.ArrayUtil;

/**
 * Implements the Listener interface IDataBookListener, IDataRowListener, IControl
 * and protocol all events in an Array.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.model.mem.DataBook
 * @see com.sibvisions.rad.model.mem.DataRow
 * @see javax.rad.model.ui.IControl
 * @see javax.rad.model.event.IDataBookListener
 * @see javax.rad.model.event.IDataRowListener
 */
public class EventProtocol implements IDataBookListener, 
                                      IDataRowListener, 
                                      IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Array of all events. */
	private ArrayUtil<EventEntry> auEvents = new ArrayUtil<EventEntry>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void valuesChanged(DataRowEvent pDataRowEvent)
	{
		auEvents.add(new EventEntry(EventType.VALUES_CHANGED, pDataRowEvent));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void dataBookChanged(DataBookEvent pDataBookEvent)
	{
		auEvents.add(new EventEntry(EventType.DATABOOK_CHANGED, pDataBookEvent));
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		auEvents.add(new EventEntry(EventType.NOTIFY_REPAINT));
	}	

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		auEvents.add(new EventEntry(EventType.CANCEL_EDITING));
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing()
	{
		auEvents.add(new EventEntry(EventType.SAVE_EDITING));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TranslationMap getTranslation()
	{
		return null;
	}

    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return true;
    }
	
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        return null;
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the occurred events as string.
	 * 
	 * @return the event list as string
	 */
	public String getEventsAsString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < auEvents.size(); i++)
		{
			sb.append(auEvents.get(i).toString() + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the occurred events.
	 * 
	 * @return the event list
	 */
	public List<EventEntry> getEvents()
	{
		return auEvents;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Small helper class which basically holds the information about one event.
	 * 
	 * @author Robert Zenz
	 */
	public static final class EventEntry
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Specifies the type of the event.
		 */
		public enum EventType
		{
			/** The valuesChanged event. */
			VALUES_CHANGED,
			/** The dataBookChanged event. */
			DATABOOK_CHANGED,
			/** The notifyRepaint event. */
			NOTIFY_REPAINT,
			/** The cancelEditing event. */
			CANCEL_EDITING,
			/** The saveEditing event. */
			SAVE_EDITING
		}
		
		/** The type of the event. */
		private EventType eventType;

		/** The original event object. */
		private Object originalEvent;
				
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>EventEntry</code>.
		 * 
		 * @param pEventType the type of the event.
		 */
		public EventEntry(EventType pEventType)
		{
			eventType = pEventType;
		}
		
		/**
		 * Creates a new instance of <code>EventEntry</code>.
		 * 
		 * @param pEventType the type of the event.
		 * @param pOriginalEvent the original event object.
		 */
		public EventEntry(EventType pEventType, Object pOriginalEvent)
		{
			eventType = pEventType;
			originalEvent = pOriginalEvent;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the type of the event.
		 * 
		 * @return the type of the event.
		 */
		public EventType getEventType()
		{
			return eventType;
		}
		
		/**
		 * Gets the original event object.
		 * 
		 * @return the original event object.
		 */
		public Object getOriginalEvent()
		{
			return originalEvent;
		}
		
		@Override
		public String toString()
		{
			switch (eventType)
			{
				case VALUES_CHANGED:
					DataRowEvent drEvent = (DataRowEvent) originalEvent;
					return "IDataRowListener.changed(" + drEvent.getChangedDataRow() + "," + 
														 drEvent.getChangedColumnNames() + "," + 
														 drEvent.getOriginalDataRow() + ")";
					
				case DATABOOK_CHANGED:
					DataBookEvent dbEvent = (DataBookEvent) originalEvent;
					return "IDataBookListener.changed(" + dbEvent.getChangedType().name() + "," + dbEvent.getChangedDataBook().getName() + "," + dbEvent.getOriginalDataRow() + ")";
					
				case NOTIFY_REPAINT:
					return "IControl.notifyRepaint()";
					
				case CANCEL_EDITING:
					return "IControl.cancelEditing()";
				
				case SAVE_EDITING:
					return "IControl.saveEditing()";
				
				default:
					return "Unknown event.";
				
			}

		}
		
	}	// EventEntry
	
} 	// EventProtocol
