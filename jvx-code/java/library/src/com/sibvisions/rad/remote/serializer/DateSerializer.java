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
 * 20.01.2010 - [HM] - creation
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;
import java.util.TimeZone;

import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.util.ImmutableTimestamp;

/**
 * The serializer for {@link Date}.
 *  
 * @author Martin Handsteiner
 */
public class DateSerializer implements ITypeSerializer<Date>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** <code>java.sql.Timestamp</code> type. */
    private static final int TYPE_TIMESTAMP_TIMEZONE_NANOS = 17; // reserved for future use, in case nanos are needed

	/** <code>java.sql.Timestamp</code> type. */
	private static final int TYPE_TIMESTAMP_TIMEZONE = 18;

    /** <code>java.sql.Timestamp</code> type. */
    private static final int TYPE_TIMESTAMP = 19;
    
	/** Caches the timestamp. */
	private static ImmutableTimestamp[] timestampCache = new ImmutableTimestamp[256];
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Date> getTypeClass()
	{
		return Date.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_TIMESTAMP_TIMEZONE_NANOS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_TIMESTAMP;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		long time = pIn.readLong();
		TimeZone tz;
		if (pTypeValue == TYPE_TIMESTAMP_TIMEZONE)
		{
		    tz = (TimeZone)pSerializer.read(pIn, pCache);
		}
		else
		{
		    tz = null;
		}
		
		int index = (int)(time >>> 8);
		index ^= (index >>> 20) ^ (index >>> 12);
		index ^= (index >>> 7) ^ (index >>> 4);
		index &= 0xff;
		
		ImmutableTimestamp t = timestampCache[index];
		TimeZone ttz = t != null && t.isTimeZoneSet() ? t.getTimeZone() : null;

		if (t == null || time != t.getTime() || (tz != ttz && (tz == null || !tz.equals(ttz))))
		{
			t = new ImmutableTimestamp(time, tz);
			
			timestampCache[index] = t;
		}
		
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Date pObject, TypeCache pCache) throws Exception
	{
	    if (pObject instanceof ImmutableTimestamp && ((ImmutableTimestamp)pObject).isTimeZoneSet())
	    {
	        pOut.writeByte(TYPE_TIMESTAMP_TIMEZONE);
	        pOut.writeLong(pObject.getTime());
	        pSerializer.write(pOut, ((ImmutableTimestamp)pObject).getTimeZone(), pCache);
	    }
	    else
	    {
	        pOut.writeByte(TYPE_TIMESTAMP);
	        pOut.writeLong(pObject.getTime());
	    }
	}
	
}	// DateSerializer
