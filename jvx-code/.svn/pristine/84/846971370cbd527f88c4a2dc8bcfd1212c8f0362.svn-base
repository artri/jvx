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
 * 23.03.2020 - [JR] - #2239: custom bean class support
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.rad.type.bean.AbstractBean;
import javax.rad.type.bean.BeanType;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The serializer for {@link Object}. It uses the {@link BeanType} for object
 * serialization.
 *  
 * @author Martin Handsteiner
 */
public class BeanSerializer implements ITypeSerializer<AbstractBean>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** value of type. */
	public static final int TYPE_BEAN = 70;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Class<AbstractBean> getTypeClass()
	{
		return AbstractBean.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMinValue()
	{
		return TYPE_BEAN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getMaxValue()
	{
		return TYPE_BEAN;
	}

	/**
	 * {@inheritDoc}
	 */
	public AbstractBean read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception
	{
		BeanType beanType = (BeanType)pSerializer.read(pIn, pCache);

		AbstractBean bean = (AbstractBean)beanType.newInstance();

		int propertyCount = beanType.getPropertyCount();

        if (bean.getBeanType() == beanType)
        {
			for (int i = 0; i < propertyCount; i++)
			{
			    Object value = pSerializer.read(pIn, pCache);
			    if (!beanType.getPropertyDefinition(i).isTransient())
			    {
			        beanType.put(bean, i, value);
			    }
			} 
		}
		else
		{
		    String[] sNames = beanType.getPropertyNames();
			for (int i = 0; i < propertyCount; i++)
			{
                Object value = pSerializer.read(pIn, pCache);
                if (!beanType.getPropertyDefinition(i).isTransient())
                {
                    bean.put(sNames[i], value);
                }
			}
		}

		return bean;
	}

	/**
	 * {@inheritDoc}
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, AbstractBean pObject, TypeCache pCache) throws Exception
	{
		BeanType beanType = BeanType.getBeanType(pObject);
		
		int propertyCount = beanType.getPropertyCount();
		
		pOut.writeByte(TYPE_BEAN);
		
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
	
}	// ObjectSerializer
