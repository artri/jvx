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

import java.io.UnsupportedEncodingException;

import com.sibvisions.util.Internalize;

/**
 * The <code>StringType</code> is a platform independent definition of String.
 * 
 * @author Martin Handsteiner
 */
public class StringType extends AbstractComparableType<String>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Empty strings will be converted to null. */
    private boolean nullForEmptyString = true;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * Constructs a new <code>StringType</code>.
	 */
	public StringType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<String> getTypeClass()
	{
		return String.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof CharSequence)
		{
			String sObject = pObject.toString();
			
			if (nullForEmptyString && sObject.length() == 0)
			{
				return null;
			}
			
			return Internalize.intern(sObject);
		}
		else if (pObject instanceof byte[])
		{
			byte[] baObject = (byte[])pObject;
			if (baObject.length == 0)
			{
				return null;
			}
			try
			{
				return Internalize.intern(new String(baObject, "UTF8"));
			}
			catch (UnsupportedEncodingException pUnsupportedEncodingException)
			{
				return Internalize.intern(new String(baObject));
			}
		
		}
		else
		{
			return Internalize.intern(pObject.toString());
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Empty strings will be converted to null.
	 *  
     * @return the nullForEmptyString
     */
    public boolean isNullForEmptyString()
    {
        return nullForEmptyString;
    }

    /**
     * Empty strings will be converted to null.
     *  
     * @param pNullForEmptyString the nullForEmptyString to set
     */
    public void setNullForEmptyString(boolean pNullForEmptyString)
    {
        nullForEmptyString = pNullForEmptyString;
    }


}	// StringType
