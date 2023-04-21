/*
 * Copyright 2012 SIB Visions GmbH
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
 * 03.04.2012 - [JR] - creation
 * 22.02.2013 - [JR] - send BeanType instead of class name
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link Enum}.
 *  
 * @author René Jahn
 */
public class EnumSerializer implements ITypeSerializer<Enum>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** value of type. */
	public static final int TYPE_ENUM = 71;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Enum> getTypeClass()
	{
		return Enum.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_ENUM;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_ENUM;
	}

	/**
	 * {@inheritDoc}
	 */
	public Enum read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		Object oClassType = pSerializer.read(pIn, pCache);
		String sName = (String)pSerializer.read(pIn, pCache);

		Class<?> clazz;
		
		if (oClassType instanceof BeanType)
		{
			clazz = ((BeanType)oClassType).getTypeClass();
		}
		else
		{
			//backwards compatibility to older EnumSerializer
			clazz = Class.forName((String)oClassType);
		}
		
		if (clazz.isEnum())
		{
			return (Enum)Enum.valueOf((Class<Enum>)clazz, sName);
		}
		else
		{
			throw new IllegalArgumentException("Class " + clazz.getName() + " is not an enum!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Enum pEnum, TypeCache pCache) throws Exception
	{
		pOut.writeByte(TYPE_ENUM);
		
		pSerializer.write(pOut, BeanType.getBeanType(pEnum.getClass()), pCache);
		pSerializer.write(pOut, pEnum.name(), pCache);
	}
	
}	// ObjectSerializer
