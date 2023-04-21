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

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>AbstractType</code> is the default implementation of {@link IType}.
 * 
 * @param <T> the type.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractType<T> implements IType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The registered types. */
	private static HashMap<Class<?>, IType<?>> registeredTypes = new HashMap<Class<?>, IType<?>>();
	
	/** Stores the primitiveTypeClass to the java class. */
	private static HashMap<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		primitiveTypes.put(Void.class, Void.TYPE);
		primitiveTypes.put(Boolean.class, Boolean.TYPE);
		primitiveTypes.put(Character.class, Character.TYPE);
		primitiveTypes.put(Byte.class, Byte.TYPE);
		primitiveTypes.put(Short.class, Short.TYPE);
		primitiveTypes.put(Integer.class, Integer.TYPE);
		primitiveTypes.put(Long.class, Long.TYPE);
		primitiveTypes.put(Float.class, Float.TYPE);
		primitiveTypes.put(Double.class, Double.TYPE);

		registerType(new BooleanType());
		registerType(new IntegerType());
		registerType(new LongType());
		registerType(new FloatType());
		registerType(new DoubleType());
		registerType(new DecimalType());
		registerType(new StringType());
		registerType(new DateType());
		registerType(new TimestampType());
		registerType(new BinaryType());
		registerType(new FileHandleType());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public T valueOf(Object pObject)
	{
		throw new IllegalArgumentException("The value " + pObject + " cannot be converted to an instance of " + getTypeClass().getName() + "!");
	}

	/**
	 * {@inheritDoc}
	 */
	public T validatedValueOf(Object pObject)
	{
		return valueOf(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode(T pObject)
	{
		if (pObject == null)
		{
			return 0;
		}
		else
		{
			return pObject.hashCode();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(T pObject1, Object pObject2)
	{
		return pObject1 == pObject2 || (pObject1 != null && pObject1.equals(pObject2));
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString(T pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else
		{
			return pObject.toString();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Registers a type.
	 * 
	 * @param pType the type.
	 */
	public static void registerType(IType<?> pType)
	{
		registeredTypes.put(pType.getTypeClass(), pType);
		
		Class<?> primitiveType = primitiveTypes.get(pType.getTypeClass());
		
		if (primitiveType != null)
		{
			registeredTypes.put(primitiveType, pType);
		}
	}

	/**
	 * Gets the type for an Object.
	 * 
	 * @param pObject the object.
	 * @return the type for the object.
	 */
	public static IType<?> getTypeFromObject(Object pObject)
	{
		if (pObject == null)
		{
			return UNKNOWN_TYPE;
		}
		else
		{
			return getTypeFromClass(pObject.getClass());
		}
	}

	/**
	 * Gets the type for a Class.
	 * 
	 * @param pClass the class.
	 * @return the type for the class.
	 */
	public static IType<?> getTypeFromClass(Class<?> pClass)
	{
		IType<?> type = registeredTypes.get(pClass);
		
		if (type == null)
		{
			if (pClass != null)
			{
				for (Map.Entry<Class<?>, IType<?>> entry : registeredTypes.entrySet())
				{
					if (entry.getKey().isAssignableFrom(pClass))
					{
						return entry.getValue();
					}
				}
			}
			return UNKNOWN_TYPE;
		}
		else
		{
			return type;
		}
	}
	
}	// AbstractType
