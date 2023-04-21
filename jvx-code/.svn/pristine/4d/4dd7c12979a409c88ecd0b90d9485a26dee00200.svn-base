/*
 * Copyright 2022 SIB Visions GmbH
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
 * 25.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>PropertyException</code> is a base exception which allows wrapping of key/value pairs (= properties)
 * in the stack trace.
 * 
 * @author René Jahn
 */
public class PropertyException extends RuntimeException
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the property serializer. */
	private static UniversalSerializer ser = new UniversalSerializer();
	
	/** optional properties. */
	protected HashMap<String, Object> hmpProperties = new HashMap<String, Object>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>PropertyException</code>.
	 */
	public PropertyException()
	{
	}
	
	/**
	 * Creates a new instance of <code>PropertyException</code> with given message.
	 * 
	 * @param pMessage the message
	 */
	public PropertyException(String pMessage)
	{
		super(pMessage);
	}
	
	/**
	 * Creates a new instance of <code>PropertyException</code> with given cause.
	 * 
	 * @param pCause the wrapped cause
	 */
	public PropertyException(Throwable pCause)
	{
		super(pCause);
	}

	/**
	 * Creates a new instance of <code>PropertyException</code> with given cause and message.
	 * 
	 * @param pMessage the message
	 * @param pCause the wrapped cause
	 */
	public PropertyException(String pMessage, Throwable pCause)
	{
		super(pMessage, pCause);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStackTrace(StackTraceElement[] pStack)
	{
		StackTraceElement[] stack = pStack;
		
		if (stack != null)
		{
			StackTraceElement element = stack[stack.length - 1];
			
			if (PropertyException.class.getName().equals(element.getClassName())
				&& "properties".equals(element.getMethodName()) 
				&& element.getLineNumber() == 42)
			{
				stack = ArrayUtil.remove(stack, stack.length - 1);
				
				updateProperties(element.getFileName());
			}
		}
		
		super.setStackTrace(stack);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StackTraceElement[] getStackTrace()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			try
			{
				GZIPOutputStream gzos = new GZIPOutputStream(baos);
	
				try
				{
					DataOutputStream daos = new DataOutputStream(gzos);
	
					try
					{
						ser.write(daos, hmpProperties);
					}
					finally
					{
						CommonUtil.close(daos);
					}
				}
				finally
				{
					CommonUtil.close(gzos);
				}
			}
			finally
			{
				CommonUtil.close(baos);
			}
			
			String sValue = CodecUtil.encodeBase64(baos.toByteArray());
			
			StackTraceElement element = new StackTraceElement(PropertyException.class.getName(), "properties", sValue, 42);
					
			StackTraceElement[] stack = super.getStackTrace();

			stack = ArrayUtil.add(stack, element);
			
			return stack;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Puts a property by name. If given value is <code>null</code>, the property
	 * will be removed.
	 * 
	 * @param pName the name
	 * @param pValue the value
	 */
	public void put(String pName, Object pValue)
	{
		if (pName == null)
		{
			hmpProperties.remove(pName);
		}
		else
		{
			hmpProperties.put(pName, pValue);
		}
	}
	
	/**
	 * Gets the value of a property.
	 * 
	 * @param pName the name
	 * @return the value or <code>null</code> if not found
	 */
	public Object get(String pName)
	{
		return hmpProperties.get(pName);
	}
	
	/**
	 * Gets an entry set of current properties.
	 * 
	 * @return the entry set
	 */
	public Set<Entry<String, Object>> properties()
	{
		return hmpProperties.entrySet();
	}
	
	/**
	 * Updates the current properties with parsed key/value pairs of given value.
	 * 
	 * @param pValue the (encoded) value
	 */
	private void updateProperties(String pValue)
	{
		try
		{
			byte[] byZip = CodecUtil.decodeBase64(pValue);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(byZip);
			
			try
			{
				GZIPInputStream gzis = new GZIPInputStream(bais);

				try
				{
					DataInputStream dis = new DataInputStream(gzis);
	
					try
					{
						hmpProperties = (HashMap<String, Object>)ser.read(dis);
					}
					finally
					{
						CommonUtil.close(dis);
					}
				}
				finally
				{
					CommonUtil.close(gzis);
				}
			}
			finally
			{
				CommonUtil.close(bais);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
}	// PropertyException
