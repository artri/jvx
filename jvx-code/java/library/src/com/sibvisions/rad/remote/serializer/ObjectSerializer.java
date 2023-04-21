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
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link Object}. It uses the {@link BeanType} for object
 * serialization.
 *  
 * @author Martin Handsteiner
 */
public class ObjectSerializer implements ITypeSerializer<Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** value of type. */
	public static final int TYPE_OBJECT = 68;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<Object> getTypeClass()
	{
		return Object.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_OBJECT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_OBJECT;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		BeanType beanType = (BeanType)pSerializer.read(pIn, pCache);

		int propertyCount = beanType.getPropertyCount();
		
		Object bean = beanType.newInstance();
		
		for (int i = 0; i < propertyCount; i++)
		{
            Object value = pSerializer.read(pIn, pCache);
            if (!beanType.getPropertyDefinition(i).isTransient())
            {
                beanType.put(bean, i, value);
            }
		}
		
		return bean;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, Object pObject, TypeCache pCache) throws Exception
	{
		BeanType beanType = BeanType.getBeanType(pObject);
		
		int propertyCount = beanType.getPropertyCount();
		
        if (propertyCount == 0)
        {
            throw new IOException("There is no ITypeSerializer registered for Objects of instance " + beanType.getClassName() + "!");
        }
        else
        {
    		pOut.writeByte(TYPE_OBJECT);
    		
    		pSerializer.write(pOut, beanType, pCache);
    		
    		for (int i = 0; i < propertyCount; i++)
    		{
                if (beanType.getPropertyDefinition(i).isTransient())
                {
                    pSerializer.write(pOut, null, pCache); // Transient values are not sent, but for the property order, we have to fill in null
                }
                else
                {
                    pSerializer.write(pOut, beanType.get(pObject, i), pCache);
                }
    		}
        }
	}
	
}	// ObjectSerializer
