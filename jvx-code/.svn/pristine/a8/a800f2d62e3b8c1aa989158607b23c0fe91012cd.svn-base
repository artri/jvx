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
import java.util.TimeZone;

import com.sibvisions.rad.remote.UniversalSerializer;
import com.sibvisions.util.type.TimeZoneUtil;

/**
 * The serializer for {@link TimeZone}.
 *  
 * @author Martin Handsteiner
 */
public class TimeZoneSerializer implements ITypeSerializer<TimeZone>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>java.util.TimeZone</code> type. */
	private static final int TYPE_TIMEZONE = 16;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<TimeZone> getTypeClass()
	{
		return TimeZone.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_TIMEZONE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_TIMEZONE;
	}

	/**
	 * {@inheritDoc}
	 */
	public TimeZone read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
        return TimeZoneUtil.forTimeZoneId((String)pSerializer.read(pIn, pCache));
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, TimeZone pObject, TypeCache pCache) throws Exception
	{
        pOut.writeByte(TYPE_TIMEZONE);
        pSerializer.write(pOut, pObject.getID(), pCache);
	}
	
}	// TimeZoneSerializer
