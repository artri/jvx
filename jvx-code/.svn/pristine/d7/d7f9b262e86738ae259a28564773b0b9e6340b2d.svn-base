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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;

import com.sibvisions.util.type.FileUtil;

/**
 * The <code>FileHandleType</code> is a platform independent definition of byte[].
 * 
 * @author Martin Handsteiner
 */
public class FileHandleType extends AbstractType<IFileHandle>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>FileHandleType</code>.
	 */
	public FileHandleType()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<IFileHandle> getTypeClass()
	{
		return IFileHandle.class;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IFileHandle pObject1, Object pObject2)
	{
		// possible because of Internalize.intern()
		if (pObject1 == pObject2)
		{
			return 0;
		}		
		else if (pObject1 != null)
		{
			IFileHandle object2 = valueOf(pObject2);
			
			if (object2 == null)
			{
				return 1;
			}
			else
			{
				try
				{
					if (pObject1.getLength() < object2.getLength())
					{
						return -1;
					}
					else if (pObject1.getLength() > object2.getLength())
					{
						return 1;
					} 
					byte[] ba1 = FileUtil.getContent(pObject1.getInputStream());
					byte[] ba2 = FileUtil.getContent(object2.getInputStream());
					
					for (int i = 0; i < ba2.length; i++)
					{
						if (ba1[i] < ba2[i])
						{
							return -1;
						} 
						else if (ba1[i] > ba2[i])
						{
							return 1;
						} 
					}
					return 0;
				}
				catch (IOException ex)
				{
					return 1;
				}
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
	public IFileHandle valueOf(Object pObject)
	{
		try
		{
			if (pObject == null)
			{
				return null;
			}
			else if (pObject instanceof IFileHandle)
			{
				IFileHandle fhObject = (IFileHandle)pObject;
				
				if (fhObject.getLength() == 0)
				{
					return null;
				}
				return fhObject;
			}
			else if (pObject instanceof byte[])
			{
				byte[] baObject = (byte[])pObject;
				if (baObject.length == 0)
				{
					return null;
				}
				return new FileHandle(null, baObject);
			}
			else if (pObject instanceof String)
			{
				String sObject = (String)pObject;
				if (sObject.length() == 0)
				{
					return null;
				}
				return new FileHandle(null, sObject.getBytes());
			}
			else if (pObject instanceof InputStream)
			{
				FileHandle fhObject = new FileHandle(null, (InputStream)pObject); 
			    if (fhObject.getLength() == 0)
			    {
			        return null;
			    }
			    
			    return fhObject;
			}
			else if (pObject instanceof InputStreamReader)
			{
			    byte[] baObject = FileUtil.getContent((InputStreamReader)pObject);
	            
	            if (baObject.length == 0)
	            {
	                return null;
	            }
	            
	            return new FileHandle(null, baObject);
			}
		}
		catch (IOException ex)
		{
			// Do nothing
		}
		
		return super.valueOf(pObject);
	}

}	// FileHandleType
