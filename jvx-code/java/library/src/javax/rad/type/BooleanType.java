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

/**
 * The <code>BooleanType</code> is a platform independent definition of boolean.
 * 
 * @author Martin Handsteiner
 */
public class BooleanType extends AbstractComparableType<Boolean>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>BooleanType</code>.
	 */
	public BooleanType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Boolean> getTypeClass()
	{
		return Boolean.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof Boolean)
		{
			return ((Boolean)pObject).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
		}		
		else if (pObject instanceof Number)
		{
			Number nObject = (Number)pObject;
			return Boolean.valueOf(nObject.intValue() != 0);
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
				try
				{
					return Boolean.valueOf(Double.valueOf(sObject).doubleValue() != 0);
				}
				catch (Exception ex)
				{
					return Boolean.valueOf(sObject);
				}
			}
		}
		
		return super.valueOf(pObject);
	}

}	// BooleanType
