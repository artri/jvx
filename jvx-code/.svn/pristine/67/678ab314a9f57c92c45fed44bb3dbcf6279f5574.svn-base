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
 * 21.12.2014 - [HM] - creation
 */
package javax.rad.type;

import java.util.Date;

import com.sibvisions.util.type.DateUtil;

/**
 * The <code>AbstractDateType</code> is the base implementation for all numeric types.
 * 
 * @param <T> the {@link Date} type.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractDateType<T extends Date> extends AbstractType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The format to convert a String to a Timestamp. */
	protected static final String FORMAT = "0000-01-01 00:00:00.000000000";
	
	/** The cell renderer. */
	protected DateUtil dateUtil = new DateUtil();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(T pObject1, Object pObject2)
	{
		if (pObject1 == pObject2)
		{
			return 0;
		}
		else if (pObject1 != null)
		{
			if (pObject2 == null)
			{
				return 1;
			}
			else
			{
				return pObject1.compareTo(valueOf(pObject2));
			}
		}
		else
		{
			return -1;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the current <code>DateFormat</code>.
	 * 
	 * @return the current <code>DateFormat</code>.
	 */
	public String getDateFormat()
	{
		return dateUtil.getDatePattern();
	}
	
	/**
	 * Sets the current <code>DateFormat</code>.
	 * 
	 * @param pDateFormat
	 * 				the new <code>DateFormat</code>.
	 */
	public void setDateFormat(String pDateFormat)
	{
		dateUtil.setDatePattern(pDateFormat);
	}
	
}	// AbstractDateType
