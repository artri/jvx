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
 * 20.01.2010 - [HM] - creation
 * 15.03.2010 - [JR] - read and write cause
 * 27.07.2011 - [JR] - #443: use correct exception cause
 *                   - don't show original exception class
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link Throwable}.
 *  
 * @author Martin Handsteiner
 */
public class ThrowableSerializer implements ITypeSerializer<Throwable>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>Throwable</code> type. */
	public static final int TYPE_THROWABLE = 69;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Throwable> getTypeClass()
	{
		return Throwable.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_THROWABLE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_THROWABLE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Throwable read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		String className = (String)pSerializer.read(pIn, pCache);
		String message = (String)pSerializer.read(pIn, pCache);
		
		int length = pIn.readUnsignedShort();
		StackTraceElement[] stackTrace = new StackTraceElement[length];
		
		for (int i = 0; i < length; i++)
		{
			stackTrace[i] = new StackTraceElement
								(
									(String)pSerializer.read(pIn, pCache),
									(String)pSerializer.read(pIn, pCache),
									(String)pSerializer.read(pIn, pCache),
									((Integer)pSerializer.read(pIn, pCache)).intValue()
								);
		}
		
		Throwable thCause = (Throwable)pSerializer.read(pIn, pCache);
		
		Throwable result;
		try
		{
			result = (Throwable)Class.forName(className).getConstructor(new Class[] {String.class, Throwable.class}).newInstance(message, thCause);
		}
		catch (Throwable th)
		{
			try
			{
				//NoSuchMethodException e.g. no constructor with parameter Throwable!
				result = (Throwable)Class.forName(className).getConstructor(new Class[] {String.class}).newInstance(message);
			}
			catch (Throwable thr)
			{
				result = new Exception(message, thCause);
			}
		}

		result.setStackTrace(stackTrace);
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Throwable pObject, TypeCache pCache) throws Exception
	{
		pOut.writeByte(TYPE_THROWABLE);
		
		pSerializer.write(pOut, pObject.getClass().getName(), pCache);
		pSerializer.write(pOut, pObject.getMessage(), pCache);

		StackTraceElement[] stackTrace = pObject.getStackTrace();
		
		pOut.writeShort(stackTrace.length);
		
		for (int i = 0, anz = stackTrace.length; i < anz; i++)
		{
			StackTraceElement element = stackTrace[i];
			pSerializer.write(pOut, element.getClassName(), pCache);
			pSerializer.write(pOut, element.getMethodName(), pCache);
			pSerializer.write(pOut, element.getFileName(), pCache);
			pSerializer.write(pOut, Integer.valueOf(element.getLineNumber()), pCache);
		}
		
		pSerializer.write(pOut, pObject.getCause(), pCache);
	}
	
}	// ThrowableSerializer
