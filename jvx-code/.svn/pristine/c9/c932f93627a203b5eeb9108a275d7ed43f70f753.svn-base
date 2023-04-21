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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.sibvisions.util.Internalize;

/**
 * The <code>DecimalType</code> is a platform independent definition of a decimal.
 * 
 * @author Martin Handsteiner
 */
public class DecimalType extends AbstractNumberType<BigDecimal>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>DecimalType</code>.
	 */
	public DecimalType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<BigDecimal> getTypeClass()
	{
		return BigDecimal.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof BigDecimal) 
		{
			return Internalize.intern((BigDecimal)pObject);
		}
		else if (pObject instanceof Byte
				 || pObject instanceof Short
				 || pObject instanceof Integer
				 || pObject instanceof Long) 
		{
			Number nObject = (Number)pObject;
			return Internalize.intern(BigDecimal.valueOf(nObject.longValue()));
		}
		else if (pObject instanceof BigInteger) 
		{
			return Internalize.intern(new BigDecimal((BigInteger)pObject));
		}
		else if (pObject instanceof Number) 
		{
			Number nObject = (Number)pObject;		
			
			return Internalize.intern(BigDecimal.valueOf(nObject.doubleValue()));
		}
    	else if (pObject instanceof Date) 
    	{
    		Date dObject = (Date)pObject;	    		
    		return Internalize.intern(BigDecimal.valueOf(dObject.getTime()));
    	}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			
			return Internalize.intern(new BigDecimal(sObject));
		}		
		return super.valueOf(pObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(BigDecimal pObject1, Object pObject2)
	{
		return pObject1 == pObject2 || (pObject1 != null && pObject2 instanceof BigDecimal && pObject1.compareTo((BigDecimal)pObject2) == 0);
	}

}	// DecimalType
