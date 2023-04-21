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
 * 03.04.2012 - [JR] - #84: enum serialization support
 */
package com.sibvisions.rad.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.sibvisions.rad.remote.serializer.ArraySerializer;
import com.sibvisions.rad.remote.serializer.BeanSerializer;
import com.sibvisions.rad.remote.serializer.BeanTypeSerializer;
import com.sibvisions.rad.remote.serializer.BooleanArraySerializer;
import com.sibvisions.rad.remote.serializer.BooleanSerializer;
import com.sibvisions.rad.remote.serializer.ByteArraySerializer;
import com.sibvisions.rad.remote.serializer.ByteSerializer;
import com.sibvisions.rad.remote.serializer.CharArraySerializer;
import com.sibvisions.rad.remote.serializer.CharacterSerializer;
import com.sibvisions.rad.remote.serializer.DateSerializer;
import com.sibvisions.rad.remote.serializer.DecimalSerializer;
import com.sibvisions.rad.remote.serializer.DoubleArraySerializer;
import com.sibvisions.rad.remote.serializer.DoubleSerializer;
import com.sibvisions.rad.remote.serializer.EnumSerializer;
import com.sibvisions.rad.remote.serializer.FloatArraySerializer;
import com.sibvisions.rad.remote.serializer.FloatSerializer;
import com.sibvisions.rad.remote.serializer.ITypeSerializer;
import com.sibvisions.rad.remote.serializer.IntArraySerializer;
import com.sibvisions.rad.remote.serializer.IntegerSerializer;
import com.sibvisions.rad.remote.serializer.ListSerializer;
import com.sibvisions.rad.remote.serializer.LocaleSerializer;
import com.sibvisions.rad.remote.serializer.LongArraySerializer;
import com.sibvisions.rad.remote.serializer.LongSerializer;
import com.sibvisions.rad.remote.serializer.MapSerializer;
import com.sibvisions.rad.remote.serializer.NullSerializer;
import com.sibvisions.rad.remote.serializer.ObjectSerializer;
import com.sibvisions.rad.remote.serializer.SetSerializer;
import com.sibvisions.rad.remote.serializer.ShortArraySerializer;
import com.sibvisions.rad.remote.serializer.ShortSerializer;
import com.sibvisions.rad.remote.serializer.StringSerializer;
import com.sibvisions.rad.remote.serializer.ThrowableSerializer;
import com.sibvisions.rad.remote.serializer.TimeZoneSerializer;
import com.sibvisions.rad.remote.serializer.TypeCache;
import com.sibvisions.rad.remote.serializer.XmlNodeSerializer;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>UniversalSerializer</code> converts an object state into a byte stream in such a way that 
 * the byte stream can be converted back into a copy of the object.
 * 
 * @author Martin Handsteiner
 * @see ISerializer
 */
public final class UniversalSerializer implements ISerializer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Caches all standard types for serialization. */
	private HashMap<Class, ITypeSerializer> serializerTypes = new HashMap<Class, ITypeSerializer>();
	
	/** Null serializer. */
	private ITypeSerializer nullSerializer;
	
	/** Caches standard types for deserialization. */
	private ITypeSerializer[] deserializerTypes = new ITypeSerializer[256];
	
	/** Instance of types in correct order. */
	private ArrayUtil<ITypeSerializer> instanceOfTypesOrder = new ArrayUtil<ITypeSerializer>();

	/** Caches all standard types for serialization. */
	private HashMap<Class, ITypeSerializer> instanceOfCache = new HashMap<Class, ITypeSerializer>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UniversalSerializer</code> with default
	 * serializer registrations.
	 */
	public UniversalSerializer()
	{
		registerTypeSerializer(new NullSerializer());             //   0
		registerTypeSerializer(new ByteSerializer());             //   1
		registerTypeSerializer(new CharacterSerializer());        //   2
		registerTypeSerializer(new BooleanSerializer());          //   3 -   4
		registerTypeSerializer(new FloatSerializer());            //   5 -   6
		registerTypeSerializer(new DoubleSerializer());           //   7 -   8
		registerTypeSerializer(new ShortSerializer());            //   9 -  10
		registerTypeSerializer(new IntegerSerializer());          //  11 -  12
		registerTypeSerializer(new LongSerializer());             //  13 -  14
        registerTypeSerializer(new LocaleSerializer());           //  15
        registerTypeSerializer(new TimeZoneSerializer());         //  16
		registerTypeSerializer(new DateSerializer());             //  17 -  19
		registerTypeSerializer(new DecimalSerializer());          //  79 -  98
		registerTypeSerializer(new StringSerializer());           //  99 - 255
		registerTypeSerializer(new ByteArraySerializer());        //  30 -  32
        registerTypeSerializer(new IntArraySerializer());         //  33 -  35
        registerTypeSerializer(new CharArraySerializer());        //  36 -  38
        registerTypeSerializer(new ArraySerializer());            //  39 -  41
        registerTypeSerializer(new ListSerializer());             //  42 -  44
        registerTypeSerializer(new MapSerializer());              //  45 -  47
		registerTypeSerializer(new BooleanArraySerializer());     //  53 -  55
		registerTypeSerializer(new FloatArraySerializer());       //  56 -  58
		registerTypeSerializer(new DoubleArraySerializer());      //  59 -  61
		registerTypeSerializer(new ShortArraySerializer());       //  62 -  64
		registerTypeSerializer(new LongArraySerializer());        //  65 -  67
        registerTypeSerializer(new SetSerializer());              //  73 -  75
		registerTypeSerializer(new BeanSerializer());             //  70
		registerTypeSerializer(new BeanTypeSerializer());         //  48 -  52
		registerTypeSerializer(new ObjectSerializer());           //  68
		registerTypeSerializer(new EnumSerializer());             //  71
		registerTypeSerializer(new ThrowableSerializer());        //  69
		registerTypeSerializer(new XmlNodeSerializer());          //  72
		// Free: 20 - 29, 76 - 78
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public final Object read(DataInputStream pIn) throws Exception 
	{
		TypeCache cache = new TypeCache();
		
		return read(pIn, cache);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void write(DataOutputStream pOut, Object pObject) throws Exception
	{
		TypeCache cache = new TypeCache();

		write(pOut, pObject, cache);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Reads in a serialized object from a stream.
	 * 
	 * @param pIn stream with serialized content
	 * @param pCache the cache for optimized bean transfer
	 * @return deserialized object
	 * @throws Exception if deserialization fails
	 */
	public final Object read(DataInputStream pIn, TypeCache pCache) throws Exception
	{
		int typeValue = pIn.readUnsignedByte();
		
		return getTypeSerializer(typeValue).read(this, pIn, typeValue, pCache);
	}
	
	/**
	 * Writes a serialized object to a stream.
	 *  
	 * @param pOut output stream for the object
	 * @param pObject serializable object
	 * @param pCache the cache for optimized bean transfer
	 * @throws Exception if serialization fails
	 */
	public final void write(DataOutputStream pOut, Object pObject, TypeCache pCache) throws Exception
	{
		getTypeSerializer(pObject).write(this, pOut, pObject, pCache);		
	}
	
	/**
	 * Registers a type serializer.
	 * 
	 * @param pTypeSerializer the type serializer.
	 * @throws IllegalArgumentException if the byte range is already used
	 */
	public void registerTypeSerializer(ITypeSerializer pTypeSerializer)
	{
		unregisterTypeSerializer(pTypeSerializer);
		
		int min = pTypeSerializer.getMinValue();
		int max = pTypeSerializer.getMaxValue();
		
		for (int i = min; i <= max; i++)
		{
			if (deserializerTypes[i] != null)
			{
				throw new IllegalArgumentException("The identification byte " + i + " of " + 
						pTypeSerializer.getClass().getName() + " is already used by " + 
						deserializerTypes[i].getClass().getName() + "!");
			}
		}

		for (int i = min; i <= max; i++)
		{
			deserializerTypes[i] = pTypeSerializer;
		}

		Class<?> typeClass = pTypeSerializer.getTypeClass();

		if (typeClass == null)
		{
			nullSerializer = pTypeSerializer;
		}
		else
		{
			if (typeClass != Object.class)
			{
				serializerTypes.put(typeClass, pTypeSerializer);
			}
			
			if (!Modifier.isFinal(typeClass.getModifiers()) || typeClass == Object[].class)
			{
				int maxIndexSubClass = -1;
				
				for (int i = 0;  i < instanceOfTypesOrder.size(); i++)
				{
					if (typeClass.isAssignableFrom(instanceOfTypesOrder.get(i).getTypeClass()))
					{
						maxIndexSubClass = i;
					}
				}
				instanceOfTypesOrder.add(maxIndexSubClass + 1, pTypeSerializer);
			}
		}
	}
	
	/**
	 * Unregisters a type serializer.
	 * 
	 * @param pTypeSerializer the type serializer.
	 */
	public void unregisterTypeSerializer(ITypeSerializer pTypeSerializer)
	{
		Class<?> typeClass = pTypeSerializer.getTypeClass();
		
		boolean cleanOld;
		if (typeClass == null)
		{
			cleanOld = nullSerializer != null;
			if (cleanOld)
			{
				nullSerializer = null;
			}
		}
		else
		{
			cleanOld = serializerTypes.remove(typeClass) != null;
			
			instanceOfTypesOrder.remove(pTypeSerializer);

			instanceOfCache.clear();
		}
		
		if (cleanOld)
		{
			int min = pTypeSerializer.getMinValue();
			int max = pTypeSerializer.getMaxValue();
			
			for (int i = min; i <= max; i++)
			{
				deserializerTypes[i] = null;
			}
		}
	}
	
	/**
	 * Gets the ITypeSerializer for a type value.
	 * 
	 * @param pTypeValue the type value.
	 * @return the ITypeSerializer.
	 * @throws IOException if the type value is unknown.
	 */
	public ITypeSerializer getTypeSerializer(int pTypeValue) throws IOException
	{
		ITypeSerializer typeSerializer = deserializerTypes[pTypeValue];
	
		if (typeSerializer == null)
		{
			throw new IOException("There is no ITypeSerializer registered for type value " + pTypeValue + "!");
		}
		else
		{
			return typeSerializer;
		}
	}
	
	/**
	 * Gets the ITypeSerializer for a type value.
	 * 
	 * @param pTypeClass the type value.
	 * @return the ITypeSerializer.
	 * @throws IOException if the type value is unknown.
	 */
	public ITypeSerializer getTypeSerializer(Class<?> pTypeClass) throws IOException
	{
		if (pTypeClass == null)
		{
			return nullSerializer;
		}
		else
		{
			ITypeSerializer typeSerializer = serializerTypes.get(pTypeClass);
			
			if (typeSerializer == null)
			{
				typeSerializer = instanceOfCache.get(pTypeClass);
				
				if (typeSerializer == null)
				{
					int i = 0;
					int size = instanceOfTypesOrder.size();
					
					while (i < size && typeSerializer == null)
					{
						ITypeSerializer typSer = instanceOfTypesOrder.get(i);
						if (typSer.getTypeClass().isAssignableFrom(pTypeClass))
						{
							typeSerializer = typSer;
							instanceOfCache.put(pTypeClass, typeSerializer);
						}
						i++;
					}
	                
	                if (typeSerializer == null)
	                {
	                    throw new IOException("There is no ITypeSerializer registered for Objects of instance " + pTypeClass + "!");
	                }
				}
			}

			return typeSerializer;
		}
	}
	
	/**
	 * Gets the ITypeSerializer for a type value.
	 * 
	 * @param pObject the type value.
	 * @return the ITypeSerializer.
	 * @throws IOException if the type value is unknown.
	 */
	public ITypeSerializer getTypeSerializer(Object pObject) throws IOException
	{
		if (pObject == null)
		{
			return nullSerializer;
		}
		else
		{
			return getTypeSerializer(pObject.getClass());
		}
	}
	
}	// UniversalSerializer
