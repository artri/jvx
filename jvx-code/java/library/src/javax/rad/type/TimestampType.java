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
 * 21.12.2009 - [HM] - creation
 */
package javax.rad.type;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * The <code>TimestampType</code> is a platform independent definition of Timestamp.
 * 
 * @author Martin Handsteiner
 */
public class TimestampType extends AbstractDateType<Timestamp>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>TimestampType</code>.
	 */
	public TimestampType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Timestamp> getTypeClass()
	{
		return Timestamp.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Timestamp) 
		{
			return (Timestamp)pObject;
		}
		else if (pObject instanceof Date) 
		{
			return new Timestamp(((Date)pObject).getTime());
		}
		else if (pObject instanceof Number) 
		{
			return new Timestamp(((Number)pObject).longValue());
		}
		else if (pObject instanceof String)
		{
			String sObject = (String)pObject;
			
			if (sObject.length() == 0)
			{
				return null;
			}
			
			try
			{
				return new Timestamp(dateUtil.parse(sObject).getTime());
			}
			catch (ParseException e)
			{
				throw new IllegalArgumentException("Date cannot be parsed with format " + dateUtil.getDatePattern(), e);
			}
		}		

		return super.valueOf(pObject);
	}

}	// TimestampType
