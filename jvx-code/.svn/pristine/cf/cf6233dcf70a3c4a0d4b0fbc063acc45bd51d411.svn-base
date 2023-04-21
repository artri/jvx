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
 * 22.02.2013 - [JR] - Enum array support
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Array;

import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for any kind of array.
 *  
 * @author Martin Handsteiner
 */
public class ArraySerializer extends AbstractSizedSerializer implements ITypeSerializer<Object[]>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_ARRAY_MIN = 39;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_ARRAY_MAX = 41;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Object[]> getTypeClass()
	{
		return Object[].class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_ARRAY_MIN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_ARRAY_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		int size = readSize(pIn, pTypeValue, TYPE_ARRAY_MIN, TYPE_ARRAY_MAX);

		int typ = pIn.readUnsignedByte();
		
		Class componentType;
		if (typ == 0)
		{
			componentType = Object.class;
		}
		else if (typ == ObjectSerializer.TYPE_OBJECT)
		{
			componentType = ((BeanType)pSerializer.read(pIn, pCache)).getTypeClass();
			if (componentType == null)
			{
				componentType = Object.class;
			}
		}
		else if (typ == EnumSerializer.TYPE_ENUM)
		{
			componentType = ((BeanType)pSerializer.read(pIn, pCache)).getTypeClass();
			if (componentType == null)
			{
				componentType = Enum.class;
			}
		}
		else
		{
			componentType = pSerializer.getTypeSerializer(typ).getTypeClass();
		}
		
		int dim = pIn.readUnsignedByte();
		
		if (dim > 0)
		{
			componentType = Array.newInstance(componentType, new int[dim]).getClass();
		}
		
		Object[] array = (Object[])Array.newInstance(componentType, size);
		for (int i = 0; i < size; i++)
		{
			Array.set(array, i, pSerializer.read(pIn, pCache));
		}
		
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Object[] pObject, TypeCache pCache) throws Exception
	{
		writeSize(pOut, pObject.length, TYPE_ARRAY_MIN, TYPE_ARRAY_MAX);
		
		int dim = 0;
		Class clazz = pObject.getClass().getComponentType();
		while (clazz.isArray() && !clazz.getComponentType().isPrimitive())
		{
			dim++;
			clazz = clazz.getComponentType();
		}
		int typ;
		if (clazz == Object.class)
		{
			typ = 0;
		}
		else
		{
			typ = pSerializer.getTypeSerializer(clazz).getMinValue();
		}
		
		if (typ == ObjectSerializer.TYPE_OBJECT || typ == EnumSerializer.TYPE_ENUM)
		{
			pOut.writeByte(typ);
			pSerializer.write(pOut, BeanType.getBeanType(clazz), pCache);
		}
		else
		{
			pOut.writeByte(typ);
		}
		
		pOut.writeByte(dim);
			
		for (int i = 0; i < pObject.length; i++)
		{
			pSerializer.write(pOut, pObject[i], pCache);
		}
	}
	
}	// ArraySerializer
