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
 * 23.12.2020 - [HM] - creation
 */
package com.sibvisions.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.TimeZoneUtil;

/**
 * The <code>ImmutableTimestamp</code> gives the functionality of immutable timestamp.
 * 
 * @author Martin Handsteiner
 */
public class ImmutableTimestamp extends Timestamp
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The timestamp format used to convert to string. */
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    
    /** Formatter for toString. */
    private static final HashMap<String, DateUtil> FORMATTER = new HashMap<String, DateUtil>();
    
    /** 
     * The time zone of the given timestamp.
     * It may be null, which means, that it should be shown always in default time zone. 
     */
    private TimeZone timeZone = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new Timestamp.
	 * 
	 * @param pTimeMillis the time in millis. 
	 */
	public ImmutableTimestamp(long pTimeMillis)
	{
	    this(pTimeMillis, null);
	}
	
    /**
     * Constructs a new Timestamp.
     * 
     * @param pTimeMillis the time in millis.
     * @param pTimeZone the time zone. 
     */
    public ImmutableTimestamp(long pTimeMillis, TimeZone pTimeZone)
    {
        super(pTimeMillis);
        
        timeZone = pTimeZone;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    @Deprecated
	public void setTime(long pTimeMillis)
	{
	    // Do nothing, it is immutable
	    throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setNanos(int pNanos)
    {
        // Do nothing, it is immutable
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setDate(int pDate) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setHours(int pHours) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setMinutes(int arg0) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setMonth(int arg0) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setSeconds(int arg0) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void setYear(int arg0) 
    {
        throw new UnsupportedOperationException("ImmutableTimestamp is immutable!");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pDate)
    {
        if (pDate instanceof ImmutableTimestamp) 
        {
            ImmutableTimestamp itz = (ImmutableTimestamp)pDate; 
            return equals((Timestamp)pDate)
                    && (timeZone == itz.timeZone || (timeZone != null && timeZone.equals(itz.timeZone)));
        } 
        else if (pDate instanceof Timestamp) 
        {
            return equals((Timestamp)pDate);
        } 
        else if (pDate instanceof Date) 
        {
            return getTime() == ((Date)pDate).getTime();
        }
        else
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        if (timeZone == null)
        {
            return super.hashCode();
        }
        else
        {
            return super.hashCode() + timeZone.hashCode() * 13;
        }
    }
    
    /**
     * Clone returns a writable Timestamp, because some JDBC driver clones parameters to change them. 
     * 
     * @return a writable not immutable Timestamp
     */
    @Override
    public Object clone()
    {
        Timestamp result = new Timestamp(getTime());
        result.setNanos(getNanos());
        
        return result;
    }
    
    /**
     * Gets the correct representation of LocalDateTime according to the time zone.
     * 
     * @return the correct representation of LocalDateTime according to the time zone.
     */
    @Override
    public LocalDateTime toLocalDateTime()
    {
        Calendar cal = TimeZoneUtil.getCalendar(timeZone);
        cal.setTime(this);
        
        return LocalDateTime.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                getNanos());
    }
    
    /**
     * Gets the correct representation according to the time zone.
     * 
     * @return the correct representation according to the time zone.
     */
    @Override
    public String toString()
    {
        TimeZone tz = getTimeZone();
        DateUtil formatter = FORMATTER.get(tz.getID());
        if (formatter == null)
        {
            formatter = new DateUtil(TIMESTAMP_FORMAT, null, tz);
            
            FORMATTER.put(tz.getID(), formatter);
        }
        
        return formatter.format(this);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the time zone of the given timestamp.
     * 
     * @return the time zone of the given timestamp.
     */
    public TimeZone getTimeZone()
    {
        if (timeZone == null)
        {
            return TimeZoneUtil.getDefault();
        }
        else
        {
            return timeZone;
        }
    }

    /**
     * True, if this timestamp has a fixed time zone.
     * 
     * @return True, if this timestamp has a fixed time zone.
     */
    public boolean isTimeZoneSet()
    {
        return timeZone != null;
    }

    /**
     * Gets the <code>ZonedDateTime</code> for the time zone.
     * 
     * @return the <code>ZonedDateTime</code>.
     */
    public ZonedDateTime toZonedDateTime()
    {
        TimeZone tZ = getTimeZone();
        
        Calendar cal = TimeZoneUtil.getCalendar(tZ);
        cal.setTime(this);
        
        return ZonedDateTime.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                getNanos(),
                tZ.toZoneId());
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given Instant.
     * 
     * @param pInstant the timestamp string.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(Instant pInstant)
    {
        return valueOf(pInstant, null);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given Instant and time zone.
     * 
     * @param pInstant the timestamp string.
     * @param pTimeZone the time zone.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(Instant pInstant, TimeZone pTimeZone)
    {
        return new ImmutableTimestamp(pInstant.getEpochSecond() * 1000 + pInstant.getNano() / 1000000, pTimeZone);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given LocalDateTime.
     * 
     * @param pLocalDateTime the LocalDateTime.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(LocalDateTime pLocalDateTime)
    {
        return valueOf(pLocalDateTime, null);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given LocalDateTime and time zone.
     * 
     * @param pLocalDateTime the timestamp string.
     * @param pTimeZone the time zone.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(LocalDateTime pLocalDateTime, TimeZone pTimeZone)
    {
        Calendar cal = TimeZoneUtil.getCalendar(pTimeZone);
        cal.set(Calendar.YEAR, pLocalDateTime.getYear());
        cal.set(Calendar.MONTH, pLocalDateTime.getMonthValue() - 1);
        cal.set(Calendar.DATE, pLocalDateTime.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, pLocalDateTime.getHour());
        cal.set(Calendar.MINUTE, pLocalDateTime.getMinute());
        cal.set(Calendar.SECOND, pLocalDateTime.getSecond());
        cal.set(Calendar.MILLISECOND, pLocalDateTime.getNano() / 1000000);
    
        return new ImmutableTimestamp(cal.getTimeInMillis(), pTimeZone);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given ZonedDateTime and time zone.
     * 
     * @param pZonedDateTime the timestamp string.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(ZonedDateTime pZonedDateTime)
    {
        TimeZone timeZone = TimeZoneUtil.forTimeZoneId(pZonedDateTime.getZone());
        
        Calendar cal = TimeZoneUtil.getCalendar(timeZone);
        cal.set(Calendar.YEAR, pZonedDateTime.getYear());
        cal.set(Calendar.MONTH, pZonedDateTime.getMonthValue() - 1);
        cal.set(Calendar.DATE, pZonedDateTime.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, pZonedDateTime.getHour());
        cal.set(Calendar.MINUTE, pZonedDateTime.getMinute());
        cal.set(Calendar.SECOND, pZonedDateTime.getSecond());
        cal.set(Calendar.MILLISECOND, pZonedDateTime.getNano() / 1000000);
    
        return new ImmutableTimestamp(cal.getTimeInMillis(), timeZone);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given string.
     * 
     * @param pTimestamp the timestamp string.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(String pTimestamp)
    {
        return valueOf(pTimestamp, null);
    }
    
    /**
     * Gets the <code>ImmutableTimestamp</code> for the given string and time zone.
     * 
     * @param pTimestamp the timestamp string.
     * @param pTimeZone the time zone.
     * @return the <code>ImmutableTimestamp</code>.
     */
    public static ImmutableTimestamp valueOf(String pTimestamp, TimeZone pTimeZone)
    {
        if (pTimeZone == null)
        {
            pTimeZone = TimeZoneUtil.getDefault();
        }
        DateUtil formatter = FORMATTER.get(pTimeZone.getID());
        if (formatter == null)
        {
            formatter = new DateUtil(TIMESTAMP_FORMAT, null, pTimeZone);
            
            FORMATTER.put(pTimeZone.getID(), formatter);
        }
        
        try
        {
            return (ImmutableTimestamp)formatter.parse(pTimestamp);
        }
        catch (Exception ex)
        {
            throw new java.lang.IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");            
        }
    }
    
}	// ImmutableTimestamp
