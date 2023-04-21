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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.rad.io.IFileHandle;

import com.sibvisions.util.type.FileUtil;

/**
 * The <code>BinaryType</code> is a platform independent definition of byte[].
 * 
 * @author Martin Handsteiner
 */
public class BinaryType extends AbstractType<byte[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>BinaryType</code>.
	 */
	public BinaryType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<byte[]> getTypeClass()
	{
		return byte[].class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(byte[] pObject1, Object pObject2)
	{
		// possible because of Internalize.intern()
		if (pObject1 == pObject2)
		{
			return 0;
		}		
		else if (pObject1 != null)
		{
			byte[] object2 = valueOf(pObject2);
			
			if (object2 == null)
			{
				return 1;
			}
			else
			{
				if (pObject1.length < object2.length)
				{
					return -1;
				}
				else if (pObject1.length > object2.length)
				{
					return 1;
				} 
				for (int i = 0; i < object2.length; i++)
				{
					if (pObject1[i] < object2[i])
					{
						return -1;
					} 
					else if (pObject1[i] > object2[i])
					{
						return 1;
					} 
				}
				return 0;
			}
		}
		else
		{
			return -1;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] valueOf(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof byte[])
		{
			byte[] baObject = (byte[])pObject;
			if (baObject.length == 0)
			{
				return null;
			}
			return baObject;
		}
		else if (pObject instanceof String)
		{
			String sObject = (String)pObject;
			if (sObject.length() == 0)
			{
				return null;
			}
			return sObject.getBytes();
		}
		else if (pObject instanceof IFileHandle)
		{
			try 
			{
     			byte[] baObject = FileUtil.getContent(((IFileHandle)pObject).getInputStream());
			    if (baObject.length == 0)
			    {
			        return null;
			    }
			    
			    return baObject;
			}
			catch (IOException e)
			{
				return null;
			}
		}
		else if (pObject instanceof InputStream)
		{
		    byte[] baObject = FileUtil.getContent((InputStream)pObject);
		    
		    if (baObject.length == 0)
		    {
		        return null;
		    }
		    
		    return baObject;
		}
		else if (pObject instanceof InputStreamReader)
		{
            byte[] baObject = FileUtil.getContent((InputStreamReader)pObject);
            
            if (baObject.length == 0)
            {
                return null;
            }
            
            return baObject;
		}
		
		return super.valueOf(pObject);
	}

}	// BinaryType
