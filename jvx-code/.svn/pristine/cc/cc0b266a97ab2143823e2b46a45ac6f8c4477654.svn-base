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
 * 29.04.2010 - [JR] - #119: init implemented
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.remote.UniversalSerializer;


/**
 * The serializer for {@link BeanType}.
 *  
 * @author Martin Handsteiner
 */
public class BeanTypeSerializer extends AbstractSizedSerializer 
                                implements ITypeSerializer<BeanType>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_BEAN_TYPE_MIN = 48;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_BEAN_TYPE_MAX = 52;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<BeanType> getTypeClass()
	{
		return BeanType.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_BEAN_TYPE_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_BEAN_TYPE_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public BeanType read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_BEAN_TYPE_MIN, TYPE_BEAN_TYPE_MAX);

		BeanType beanType;
		if (size == 0)
		{
			beanType = pCache.get(pIn.readUnsignedByte());
		}
		else if (size == 1)
		{
			beanType = pCache.get(pIn.readUnsignedShort());
		}
		else
		{
			String className = (String)pSerializer.read(pIn, pCache);
			
			String[] propertyNames = new String[size - 2];
				
			for (int i = 0; i < propertyNames.length; i++)
			{
				propertyNames[i] = ((String)pSerializer.read(pIn, pCache)).intern();
			}
			
			beanType = new BeanType(className, propertyNames);
			
			pCache.put(beanType);
		}

		return beanType;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void write(UniversalSerializer pSerializer, DataOutputStream pOut, BeanType pObject, TypeCache pCache) throws Exception
	{
		Integer index = pCache.indexOf(pObject);
		
		if (index == null)
		{
			String[] propertyNames = pObject.getPropertyNames();

			writeSize(pOut, propertyNames.length + 2, TYPE_BEAN_TYPE_MIN, TYPE_BEAN_TYPE_MAX);
			
			pSerializer.write(pOut, pObject.getClassName());
			
			for (int i = 0; i < propertyNames.length; i++)
			{
				pSerializer.write(pOut, propertyNames[i]);
			}
			
			pCache.put(pObject);
		}
		else
		{
			int ind = index.intValue();
			
			if (ind < 256)
			{
				writeSize(pOut, 0, TYPE_BEAN_TYPE_MIN, TYPE_BEAN_TYPE_MAX);

				pOut.writeByte(ind);
			}
			else
			{
				writeSize(pOut, 1, TYPE_BEAN_TYPE_MIN, TYPE_BEAN_TYPE_MAX);

				pOut.writeShort(ind);
			}
		}
	}
	
}	// BeanTypeSerializer
