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

import com.sibvisions.util.Internalize;

/**
 * The <code>DoubleType</code> is a platform independent definition of double.
 * 
 * @author Martin Handsteiner
 */
public class DoubleType extends AbstractNumberType<Double>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>DoubleType</code>.
	 */
	public DoubleType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Double> getTypeClass()
	{
		return Double.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Number) 
		{
			return Internalize.intern(Double.valueOf(((Number)pObject).doubleValue()));
		}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (sObject.length() == 0)
			{
				return null;
			}
			else
			{
				return Internalize.intern(Double.valueOf(sObject));
			}
		}
		return super.valueOf(pObject);
	}

}	// DoubleType
