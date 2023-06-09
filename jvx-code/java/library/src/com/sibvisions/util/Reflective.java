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
 * 01.10.2008 - [JR] - creation
 * 08.11.2008 - [JR] - used WeakReference cache
 * 22.11.2008 - [JR] - changed [] to vararg (...)
 * 01.10.2009 - [JR] - call: Object[] special handling
 *                     construct: Object[] special handling
 * 15.02.2010 - [JR] - construct/call: support for declared methods
 * 05.03.2010 - [JR] - call: support different class calls 
 * 18.05.2010 - [JR] - public constructor to allow factory access   
 * 27.10.2010 - [JR] - printFields, printMethods implemented        
 * 17.12.2010 - [JR] - printFields, printMethods supports Class objects     
 * 13.03.2012 - [JR] - changed log level in invokeLaster 
 * 01.08.2012 - [JR] - #594: Parameter class introduced
 * 17.10.2013 - [JR] - #842: getConstructor created with code from construct    
 * 12.02.2016 - [JR] - split call in getMethodForCall and call(Method) for better external usage
 */
package com.sibvisions.util;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.sibvisions.util.type.StringUtil;

/**
 * This is a utility class to call different methods of classes via 
 * java.lang.reflect package.
 *  
 * @author Ren� Jahn
 */
public final class Reflective
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Array for null parameter in case of array type. */
	private static final Object[] NULL_PARAMETER = new Object[1];
	
	/** Stores the primitiveTypeClass to the java class. */
	private static HashMap<Class, Set<Class>> primitiveTypeClass = new HashMap<Class, Set<Class>>();
	/** Stores the primitiveTypeClass to the java class. */
	private static HashMap<Class, Class> autoboxClass = new HashMap<Class, Class>();
	
	/** cache for all used constructors. */
	private static Hashtable<Long, WeakReference<Constructor<?>>> htConstructorCache = null;
	
	/** cache for all used methods. */
	private static Hashtable<Long, WeakReference<Method>> htMethodCache = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		primitiveTypeClass.put(Void.TYPE, new HashSet(Arrays.asList(Void.class)));
		primitiveTypeClass.put(Boolean.TYPE, new HashSet(Arrays.asList(Boolean.class)));
		primitiveTypeClass.put(Character.TYPE, new HashSet(Arrays.asList(Character.class)));
		primitiveTypeClass.put(Byte.TYPE, new HashSet(Arrays.asList(Byte.class)));
		primitiveTypeClass.put(Short.TYPE, new HashSet(Arrays.asList(Short.class, Byte.class)));
		primitiveTypeClass.put(Integer.TYPE, new HashSet(Arrays.asList(Integer.class, Character.class, Short.class, Byte.class)));
		primitiveTypeClass.put(Long.TYPE, new HashSet(Arrays.asList(Long.class, Integer.class, Character.class, Short.class, Byte.class)));
		primitiveTypeClass.put(Float.TYPE, new HashSet(Arrays.asList(Float.class)));
		primitiveTypeClass.put(Double.TYPE, new HashSet(Arrays.asList(Double.class, Float.class)));

		autoboxClass.put(Void.TYPE, Void.class);
		autoboxClass.put(Boolean.TYPE, Boolean.class);
		autoboxClass.put(Character.TYPE, Character.class);
		autoboxClass.put(Byte.TYPE, Byte.class);
		autoboxClass.put(Short.TYPE, Short.class);
		autoboxClass.put(Integer.TYPE, Integer.class);
		autoboxClass.put(Long.TYPE, Long.class);
		autoboxClass.put(Float.TYPE, Float.class);
		autoboxClass.put(Double.TYPE, Double.class);
	}
	
	/**
	 * Invisible constructor, because the <code>Reflective</code> is a utility class.
	 */
	private Reflective()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the outerboxed class when primitive class or the object class itself.
	 * @param pClass the primitive or object class
	 * @return the outerboxed class when primitive class or the object class itself.
	 */
	public static Class getAutoboxClass(Class pClass)
	{
		Class result = autoboxClass.get(pClass);
		if (result == null)
		{
			return pClass;
		}
		else
		{
			return result;
		}
	}
	
	/**
	 * Gets the outerboxed class when primitive class or the object class itself.
	 * @param pClass the primitive or object class
	 * @return the outerboxed class when primitive class or the object class itself.
	 * @deprecated since 2.7, use {@link Reflective#getAutoboxClass(Class)} instead.
	 */
	@Deprecated
	public static Class getOuterBoxClass(Class pClass)
	{
		return getAutoboxClass(pClass);
	}
	
	/**
	 * Invokes the constructor of a desired class. The constructor will be found through its 
	 * parameter list.
	 *
	 * @param pClassName full qualified class name
	 * @param pParams parameters for the constructor
	 * @return object newly created object
	 * @throws Throwable if it is not possible to invoke the constructor
	 */
	public static final Object construct(String pClassName, Object... pParams) throws Throwable
	{
		return construct(Class.forName(pClassName), pParams);
	}	
	
	/**
	 * Invokes the constructor of a desired class. The constructor will be found through its 
	 * parameter list.
	 *
	 * @param pClassLoader an optional {@link ClassLoader} for dynamic class loading
	 * @param pClassName full qualified class name
	 * @param pParams parameters for the constructor
	 * @return object newly created object
	 * @throws Throwable if it is not possible to invoke the constructor
	 */
	public static final Object construct(ClassLoader pClassLoader, String pClassName, Object... pParams) throws Throwable
	{
		return construct(Class.forName(pClassName, true, pClassLoader), pParams);
	}
	
	/**
	 * Invokes the constructor of a pre-loaded class. The constructor will be found through its 
	 * parameter list.
	 *
	 * @param pClass the pre-loaded class
	 * @param pParams parameters for the constructor
	 * @return object newly created object
	 * @throws Throwable if it is not possible to invoke the constructor
	 */
	public static final Object construct(Class<?> pClass, Object... pParams) throws Throwable
	{
		return construct(pClass, false, pParams);
	}
	
	/**
	 * Invokes the constructor of a pre-loaded class. The constructor will be found through its 
	 * parameter list.
	 *
	 * @param pClass the pre-loaded class
	 * @param pOnlyDeclared <code>true</code> to use only declared constructors
	 * @param pParams parameters for the constructor
	 * @return object newly created object
	 * @throws Throwable if it is not possible to invoke the constructor
	 */
	public static final Object construct(Class<?> pClass, boolean pOnlyDeclared, Object... pParams) throws Throwable
	{
		try
		{
			Constructor constructor = getConstructor(pClass, pOnlyDeclared, pParams);
			
			return constructor.newInstance(convertParameters(constructor.getParameterTypes(), pParams, constructor.isVarArgs()));
		}
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
	}

	/**
	 * Invokes the method of an object without changing the access to the method. 
	 * The method needs the public modifier. If there are more than one methods
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pObject object with the desired method
	 * @param pMethod invocable method
	 * @param pParams parameters for the method
	 * @return return value from the method invocation
	 * @throws Throwable if it is not possible to invoke the method
	 */
	public static final Object call(Object pObject, String pMethod, Object... pParams) throws Throwable
	{
		return call(pObject, pObject.getClass(), false, pMethod, pParams);
	}
	
	/**
	 * Invokes the method of an object without changing the access to the method. 
	 * The method needs the public modifier. If there are more than one methods
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pObject object with the desired method
	 * @param pOnlyDeclared <code>true</code> to use only declared methods
	 * @param pMethod invocable method
	 * @param pParams parameters for the method
	 * @return return value from the method invocation
	 * @throws Throwable if it is not possible to invoke the method
	 */
	public static final Object call(Object pObject, boolean pOnlyDeclared, String pMethod, Object... pParams) throws Throwable
	{
		return call(pObject, pObject.getClass(), pOnlyDeclared, pMethod, pParams);
	}
	
	/**
	 * Invokes the method of an object without changing the access to the method. 
	 * The method needs the public modifier. If there are more than one methods
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pObject object with the desired method
	 * @param pBaseClass the object class or a super class from which the method will be called
	 * @param pOnlyDeclared <code>true</code> to use only declared methods
	 * @param pMethod invocable method
	 * @param pParams parameters for the method
	 * @return return value from the method invocation
	 * @throws Throwable if it is not possible to invoke the method
	 */
	public static final Object call(Object pObject, Class<?> pBaseClass, boolean pOnlyDeclared, String pMethod, Object... pParams) throws Throwable
	{
        Method method = getMethodForCall(pBaseClass, pOnlyDeclared, pMethod, pParams);

        if (method != null)
        {
            return call(pObject, method, pParams);
        }
        
        throw new NoSuchMethodException(getMethodDeclaration(pBaseClass, pMethod, pParams));
	}
	
	/**
	 * Invokes the given method. This method will be used in {@link #call(Object, Class, boolean, String, Object...)}.
	 * 
	 * @param pMethod the method to call
	 * @param pObject the caller object
	 * @param pParams the method parameters
	 * @return return value from the method invocation
	 * @throws Throwable if it is not possible to invoke the method
	 * @throws NullPointerException if <code>pMethod</code> is <code>null</code>
	 */
	public static final Object call(Object pObject, Method pMethod, Object... pParams) throws Throwable
	{
	    if (pMethod == null)
	    {
	        throw new NullPointerException("Method can't be null!");
	    }
	    
        try
        {
            return pMethod.invoke(pObject, convertParameters(pMethod.getParameterTypes(), pParams, pMethod.isVarArgs()));
        }
        catch (InvocationTargetException pInvocationTargetException)
        {
            throw pInvocationTargetException.getCause();
        }
	}
	
	/**
	 * Checks if the class definitions of a parameter list are equal to a
	 * desired class list.
	 * for easier usage all arrays at the end 
	 * 
	 * @param pTypes desired class list
	 * @param pParams parameter list
	 * @param pVarArg check varArg parameter.
	 * @return true if the class definitions are equal to the desired class list
	 */
	private static final boolean isParamListValid(Class<?>[] pTypes, Object[] pParams, boolean pVarArg)
	{
		int iParamCount = (pParams == null ? 0 : pParams.length);
		int iTypeCount = (pTypes == null ? 0 : pTypes.length);

		// This is done below, to be able to detect the null parameter in a better way.
		// In case of Object..., null is mapped to an new Object[] {null}, in case of an Object[] to null.
//		pVarArg = pVarArg || (iTypeCount > 0 && Object[].class.isAssignableFrom(pTypes[iTypeCount - 1]));
		
		if (pVarArg)
		{
			iTypeCount--;
		}
		else if (iTypeCount == 1)
		{
			if (pParams == null)
			{
				pParams = NULL_PARAMETER;
				iParamCount++;
			}
			else if (Object[].class.isAssignableFrom(pTypes[0]))
			{
				pVarArg = true;
				iTypeCount--;
			}
		}

		if (iParamCount == iTypeCount || (pVarArg && iParamCount > iTypeCount))
		{
			int iValidParams = 0;
			
			while (iValidParams < iTypeCount
				&& ((pTypes[iValidParams].isPrimitive() 
						&& ((pParams[iValidParams] != null 
								&& primitiveTypeClass.get(pTypes[iValidParams]).contains(pParams[iValidParams].getClass()))
							|| (pParams[iValidParams] instanceof Parameter 
								&& primitiveTypeClass.get(pTypes[iValidParams]).contains(pParams[iValidParams].getClass()))))
					|| (!pTypes[iValidParams].isPrimitive() 
							&& ((pParams[iValidParams] == null || pTypes[iValidParams].isInstance(pParams[iValidParams]))
									|| (pParams[iValidParams] instanceof Parameter && ((Parameter)pParams[iValidParams]).clazz != null
										&& pTypes[iValidParams].isAssignableFrom(((Parameter)pParams[iValidParams]).clazz))))))
    		{
    			iValidParams++;
    		}
			
			if (pVarArg && iValidParams == iTypeCount)
			{
				if (iParamCount == iTypeCount + 1 && pTypes[iTypeCount].isInstance(pParams[iValidParams]))
				{
					iValidParams++;
				}
				else
				{
					Class componentType = pTypes[iTypeCount].getComponentType();
					boolean isPrimitiv = componentType.isPrimitive();
					Set<Class> primitiveAssignableSet = primitiveTypeClass.get(componentType);
					
					while (iValidParams < iParamCount 
							&& ((isPrimitiv && pParams[iValidParams] != null && primitiveAssignableSet.contains(pParams[iValidParams].getClass()))
								|| (!isPrimitiv && (pParams[iValidParams] == null || componentType.isInstance(pParams[iValidParams])))))
					{
						iValidParams++;
					}
				}
			}
			
    		return iValidParams == iParamCount;
		}
		
		return false;
	}
	
	/**
	 * Converts the parameters to the correct array type, in case of varArgs. 
	 * 
	 * @param pTypes desired class list
	 * @param pParams parameter list
	 * @param pVarArg check varArg parameter.
	 * @return the correct array type, in case of varArgs. 
	 */
	private static final Object[] convertParameters(Class<?>[] pTypes, Object[] pParams, boolean pVarArg)
	{
		int iParamCount = (pParams == null ? 0 : pParams.length);
		int iTypeCount = (pTypes == null ? 0 : pTypes.length);

		pVarArg = pVarArg || (pParams != null && iTypeCount == 1 && Object[].class.isAssignableFrom(pTypes[0]));
		
		if (pVarArg)
		{
			iTypeCount--;
			if (iParamCount != iTypeCount + 1 || !pTypes[iTypeCount].isInstance(pParams[iTypeCount]))
			{
				Object[] params = new Object[iTypeCount + 1];
				
				if (pParams != null)
				{
					for (int i = 0; i < iTypeCount; i++)
					{
						params[i] = pParams[i];
					}
				}
				
				if (pVarArg || pParams != null || iParamCount > iTypeCount)
				{
					Class componentType = pTypes[iTypeCount].getComponentType();
	
					Object array = Array.newInstance(componentType, pParams == null && iTypeCount == 0 ? 1 : Math.max(0, iParamCount - iTypeCount));
	
					int index = 0;
					for (int i = iTypeCount; i < iParamCount; i++)
					{
						Array.set(array, index, pParams[i]);
						index++;
					}
					params[iTypeCount] = array;
				}
				return params;
			}
		}
		else
		{
			if (iTypeCount == 1 && pParams == null)
			{
				pParams = NULL_PARAMETER;
				iParamCount++;
			}
			for (int i = 0; i < iParamCount; i++)
			{
				if (pParams[i] instanceof Parameter)
				{
					pParams[i] = ((Parameter)pParams[i]).value;
				}
			}
		}
		
		return pParams;
	}

    /**
     * Gets the human readable method declaration of a method with its parameter list.
     * 
     * @param pClass the class
     * @param pMethod method or full qualified class name
     * @param pParams parameter list
     * @return human readable method declaration e.g. java.util.File(java.lang.String);
     */
    public static final String getMethodDeclaration(Class pClass, String pMethod, Object... pParams)
    {
		StringBuilder sbList = new StringBuilder();

		if (pMethod == null)
		{
			sbList.append("new ");
		}
		sbList.append(pClass.getCanonicalName());
		if (pMethod != null)
		{
			sbList.append('.');
			sbList.append(pMethod);
		}
		sbList.append('(');
    		
		for (int i = 0; pParams != null && i < pParams.length; i++)
		{
			if (i > 0)
			{
				sbList.append(", ");
			}
			
			if (pParams instanceof Class[])
			{
				sbList.append(((Class[])pParams)[i].getName());
			}
			else if (pParams[i] == null)
			{
				sbList.append("null");
			}
			else if (pParams[i] instanceof Parameter)
			{
				if (((Parameter)pParams[i]).clazz != null)
				{
					sbList.append(((Parameter)pParams[i]).clazz.getName());
				}
				else
				{
					sbList.append("null");
				}
			}
			else
			{
				sbList.append(pParams[i].getClass().getName());
			}
		}
    		
		sbList.append(')');
		
		return sbList.toString();
    }
    
    /**
     * Sets a field on a specified object value.
     * 
     * @param pObject the object with the field
     * @param pFieldName the field name
     * @param pValue the new value
     * @throws Throwable if it is not possible to set the desired field
     * @see #setValue(Object, Field, Object)
     */
    public static void setValue(Object pObject, String pFieldName, Object pValue) throws Throwable
    {
    	Field field = pObject.getClass().getDeclaredField(pFieldName);
    	
    	
    	Reflective.setValue(pObject, field, pValue);
    }
    
    /**
     * Sets a field on a specified object value. If the field is not accessible, the access will be granted
     * before, and revoked after the execution.
     * 
     * @param pObject the object with the field
     * @param pField the field
     * @param pValue the new value
     * @throws Throwable if it is not possible to set the desired field
     */
    public static void setValue(Object pObject, Field pField, Object pValue) throws Throwable
    {
    	boolean bAccess = pField.isAccessible();
    	
    	
    	if (!bAccess)
    	{
    		pField.setAccessible(true);
    	}
    	
    	try
    	{
    		pField.set(pObject, pValue);
    	}
    	finally
    	{
	    	if (!bAccess)
	    	{
	    		pField.setAccessible(false);
	    	}
    	}
    }

    /**
     * Gets the value from a field.
     * 
     * @param pObject the object with the field
     * @param pFieldName the field name
     * @return the value of the field
     * @throws Throwable if it is not possible to get the value of desired field or the field is not presend
     * @see #getValue(Object, Field)
     */
    public static Object getValue(Object pObject, String pFieldName) throws Throwable
    {
    	Field field = pObject.getClass().getDeclaredField(pFieldName);
    	
    	
    	return Reflective.getValue(pObject, field);
    }
    
    /**
     * Gets the value from a vield. If the field is not accessible, the access will be granted
     * before, and revoked after the execution.
     * 
     * @param pObject the object with the field
     * @param pField the field
     * @return the value of the field
     * @throws Throwable if it is not possible to get the value of desired field or the field is not presend
     */
    public static Object getValue(Object pObject, Field pField) throws Throwable
    {
    	boolean bAccess = pField.isAccessible();
    	
    	
    	if (!bAccess)
    	{
    		pField.setAccessible(true);
    	}
    	
    	try
    	{
    		return pField.get(pObject);
    	}
    	finally
    	{
	    	if (!bAccess)
	    	{
	    		pField.setAccessible(false);
	    	}
    	}
    }
    
    /**
     * Invokes a method with parameters. If the method is not accessible, the access will be granted
     * before, and revoked after the execution.
     * 
     * @param pObject the object with the method
     * @param pMethod the method
     * @param pParams the method parameters
     * @return the result of the method invokation
     * @throws Throwable if an error occurs during invocation
     */
    public static Object invoke(Object pObject, Method pMethod, Object... pParams) throws Throwable
    {
    	boolean bAccess = pMethod.isAccessible();
    	
    	
    	if (!bAccess)
    	{
    		pMethod.setAccessible(true);
    	}
    	
    	try
    	{
	    	return pMethod.invoke(pObject, pParams);
    	}
    	finally
    	{
	    	if (!bAccess)
	    	{
	    		pMethod.setAccessible(false);
	    	}
    	}    	
    }
    
    /**
     * Invokes the get method of the given property.
     * 
     * @param pObject the object with the method
     * @param pProperty the property
     * @return the value of the property.
     * @throws Throwable if an error occurs during invocation
     */
    public static Object get(Object pObject, String pProperty) throws Throwable
    {
		Class<?> clazz = pObject.getClass();
		
		long key = clazz.hashCode() | ((long)pProperty.hashCode() << 32) & 0x7fffffff00000000L;

		Method method = getMethod(key);
		
		try
		{
			if (method == null)
			{
				method = clazz.getMethod(StringUtil.formatMethodName("get", pProperty));
				
				putMethod(key, method);
			}
			
			return method.invoke(pObject);
		}
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
    }
    
    /**
     * Invokes the set method of the given property.
     * @param pObject the object with the method
     * @param pProperty the property
     * @param pValue the value of the property.
     * @throws Throwable if an error occurs during invocation
     */
    public static void set(Object pObject, String pProperty, Object pValue) throws Throwable
    {
		Class<?> clazz = pObject.getClass();
		
		long key = clazz.hashCode() | ((long)pProperty.hashCode() << 32) | 0x8000000000000000L;

		Method method = getMethod(key);
		
		try
		{
			if (method == null)
			{
				method = clazz.getMethod(StringUtil.formatMethodName("set", pProperty), 
						                 clazz.getMethod(StringUtil.formatMethodName("get", pProperty)).getReturnType());
				
				putMethod(key, method);
			}
			
			method.invoke(pObject, pValue);
		}
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
    }
    
    /**
     * Gets a {@link Method} from the method cache.
     * 
     * @param pKey the access key
     * @return the method or <code>null</code> if the method is not cached
     */
    private static final Method getMethod(long pKey)
    {
    	if (htMethodCache != null)
    	{
			Long key = Long.valueOf(pKey);
			
	    	WeakReference<Method> weakRef = htMethodCache.get(key); 
			
			if (weakRef != null)
			{
				Method method = weakRef.get();
				
				if (method == null)
				{
					htMethodCache.remove(key);
				}
				
				return method;
			}
    	}
		
		return null;
    }

    /**
     * Gets a {@link Constructor} from the constructor cache.
     * 
     * @param pKey the access key
     * @return the constructor or <code>null</code> if the constructor is not cached
     */
    private static final Constructor<?> getConstructor(long pKey)
    {
		if (htConstructorCache != null)
		{
			Long key = Long.valueOf(pKey);
			
			WeakReference<Constructor<?>> weakRef = htConstructorCache.get(key); 
			
			if (weakRef != null)
			{
				Constructor<?> constructor = weakRef.get();
				
				if (constructor == null)
				{
					htConstructorCache.remove(key);
				}
				
				return constructor;
			}
		}
		
		return null;
    }
    
    /**
     * Puts a {@link Method} to the method cache.
     * 
     * @param pKey the access key
     * @param pMethod the method to cache
     */
    private static final void putMethod(long pKey, Method pMethod) 
    {
		if (htMethodCache == null)
		{
			htMethodCache = new Hashtable<Long, WeakReference<Method>>();
		}
		
		htMethodCache.put(Long.valueOf(pKey), new WeakReference(pMethod));
    }
    
    /**
     * Puts a {@link Constructor} to the constructor cache.
     * 
     * @param pKey the access key
     * @param pConstructor the constructor to cache
     */
    private static final void putConstructor(long pKey, Constructor<?> pConstructor)
    {
		if (htConstructorCache == null)
		{
			htConstructorCache = new Hashtable<Long, WeakReference<Constructor<?>>>();							
		}
		
		htConstructorCache.put(Long.valueOf(pKey), new WeakReference(pConstructor));
    }

    /**
     * Prints out the fields and field values from a specific object. If the field is not accessible,
     * the access will be enabled.
     * 
     * @param pObject an object
     * @param pOnlyDeclared use only declared fields
     */
    public static final void printFields(Object pObject, boolean pOnlyDeclared)
    {
    	Field[] fields;
    	
    	if (pOnlyDeclared)
    	{
    		if (pObject instanceof Class)
    		{
    			fields = ((Class)pObject).getDeclaredFields();
    		}
    		else
    		{
    			fields = pObject.getClass().getDeclaredFields();
    		}
    	}
    	else
    	{
    		if (pObject instanceof Class)
    		{
    			fields = ((Class)pObject).getFields();
    		}
    		else
    		{
    			fields = pObject.getClass().getFields();
    		}
    	}
    	
    	for (Field field : fields)
    	{
			try
			{
	    		if (!field.isAccessible())
	    		{
    				field.setAccessible(true);
	    		}
	    		
	    		System.out.println(field.getName() + " = " + field.get(pObject));
    		}
			catch (Throwable th)
			{
				System.out.println(field.getName() + " = " + th.getMessage());
			}
    	}
    }
    
    /**
     * Prints out the methods and return values from a specific object. If the method is not accessible,
     * the access will be enabled. Only methods which starts with 'get', 'is' or 'has' and without parameters
     * will be used.
     * 
     * @param pObject an object
     * @param pOnlyDeclared use only declared methods
     */
    public static final void printMethods(Object pObject, boolean pOnlyDeclared)
    {
    	Method[] methods;
    	
    	if (pOnlyDeclared)
    	{
    		if (pObject instanceof Class)
    		{
    			methods = ((Class)pObject).getDeclaredMethods();
    		}
    		else
    		{
    			methods = pObject.getClass().getDeclaredMethods();
    		}
    	}
    	else
    	{
    		if (pObject instanceof Class)
    		{
    			methods = ((Class)pObject).getMethods();
    		}
    		else
    		{
    			methods = pObject.getClass().getMethods();
    		}
    	}

    	for (Method method : methods)
    	{
    		if (method.getReturnType() != null && (method.getParameterTypes() == null || method.getParameterTypes().length == 0)
    			&& (method.getName().startsWith("get") || method.getName().startsWith("is") || method.getName().startsWith("has")))
    		{
				try
				{
		    		if (!method.isAccessible())
		    		{
	    				method.setAccessible(true);
		    		}
		    		
		    		System.out.println(method.getName() + " = " + method.invoke(pObject));
	    		}
				catch (Throwable th)
				{
					System.out.println(method.getName() + " = " + th.getMessage());
				}
    		}
    	}
    }

	/**
	 * Checks if the class definitions of a parameter list are equal to a
	 * desired class list.
	 * for easier usage all arrays at the end 
	 * 
	 * @param pTypes desired class list
	 * @param pParamTypes parameter list
	 * @param pVarArg check varArg parameter.
	 * @return true if the class definitions are equal to the desired class list
	 */
	public static final boolean isParamTypeListValid(Class<?>[] pTypes, Class[] pParamTypes, boolean pVarArg)
	{
		int iParamCount = (pParamTypes == null ? 0 : pParamTypes.length);
		int iTypeCount = (pTypes == null ? 0 : pTypes.length);

		pVarArg = pVarArg || (iTypeCount > 0 && Object[].class.isAssignableFrom(pTypes[iTypeCount - 1]));
		
		if (pVarArg)
		{
			iTypeCount--;
		}
		
		if (iParamCount == iTypeCount || (pVarArg && iParamCount > iTypeCount))
		{
			int iValidParams = 0;

			while (iValidParams < iTypeCount
					&& ((pTypes[iValidParams].isPrimitive() 
							&& primitiveTypeClass.get(pTypes[iValidParams]).contains(getAutoboxClass(pParamTypes[iValidParams])))
						|| (!pTypes[iValidParams].isPrimitive() 
								&& pTypes[iValidParams].isAssignableFrom(getAutoboxClass(pParamTypes[iValidParams])))))
    		{
    			iValidParams++;
    		}
			
			if (pVarArg && iValidParams == iTypeCount)
			{
				if (iParamCount == iTypeCount + 1 && pTypes[iTypeCount].isAssignableFrom(pParamTypes[iValidParams]))
				{
					iValidParams++;
				}
				else
				{
					Class componentType = pTypes[iTypeCount].getComponentType();
					boolean isPrimitiv = componentType.isPrimitive();
					Set<Class> primitiveAssignableSet = primitiveTypeClass.get(componentType);
					
					while (iValidParams < iParamCount 
							&& ((isPrimitiv && primitiveAssignableSet.contains(getAutoboxClass(pParamTypes[iValidParams])))
								|| (!isPrimitiv && componentType.isAssignableFrom(getAutoboxClass(pParamTypes[iValidParams])))))
					{
						iValidParams++;
					}
				}
			}
			
    		return iValidParams == iParamCount;
		}
		
		return false;
	}
	
	/**
	 * Gets the method with the given types.  
	 * The method needs the public modifier. If there are more than one methods
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pBaseClass the object class or a super class from which the method will be called
	 * @param pMethod invocable method
	 * @param pParamTypes parameters for the method
	 * @return the method
	 * @throws NoSuchMethodException if Method does not exist. 
	 */
	public static final Method getMethod(Class<?> pBaseClass, String pMethod, Class... pParamTypes) throws NoSuchMethodException
	{
		return getMethod(pBaseClass, false, pMethod, pParamTypes);
	}
	
	/**
	 * Gets the method with the given types.  
	 * The method needs the public modifier. If there is more than one method
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pBaseClass the object class or a super class from which the method will be called
	 * @param pOnlyDeclared <code>true</code> to use only declared methods
	 * @param pMethod invocable method
	 * @param pParamTypes parameters for the method
	 * @return the method
	 * @throws NoSuchMethodException if Method does not exist. 
	 */
	public static final Method getMethod(Class<?> pBaseClass, boolean pOnlyDeclared, String pMethod, Class... pParamTypes) throws NoSuchMethodException
	{
		long keyClass = pBaseClass.hashCode() | ((long)pMethod.hashCode() << 32) & 0x00ffffff00000000L;

		Method method;

		if (pParamTypes == null || pParamTypes.length == 0)
		{
			method = getMethod(keyClass);
			
			if (method == null)
			{
				if (pOnlyDeclared)
				{
					method = pBaseClass.getDeclaredMethod(pMethod);
				}
				else
				{
					method = pBaseClass.getMethod(pMethod);
				}

				putMethod(keyClass, method);
			}
		}
		else
		{
			long keyParam = keyClass | (((long)pParamTypes.length) << 56);
			
			method = getMethod(keyParam);

			if (method == null || !method.getName().equals(pMethod) || !isParamTypeListValid(method.getParameterTypes(), pParamTypes, method.isVarArgs()))
			{
				try
				{
				    if (pOnlyDeclared)
				    {
				        method = pBaseClass.getDeclaredMethod(pMethod, pParamTypes);
				    }
				    else
				    {
                        method = pBaseClass.getMethod(pMethod, pParamTypes);
				    }
					
					putMethod(keyParam, method);
				}
				catch (NoSuchMethodException ex)
				{
					Method[] methods;
					
					if (pOnlyDeclared)
					{
						methods = pBaseClass.getDeclaredMethods();
					}
					else
					{
						methods = pBaseClass.getMethods();
					}
					
					method = null;
					
					for (int i = 0, anz = methods.length; i < anz && method == null; i++)
					{				
						if (methods[i].getName().equals(pMethod) && isParamTypeListValid(methods[i].getParameterTypes(), pParamTypes, methods[i].isVarArgs()))
						{
							method = methods[i];
							
							putMethod(keyParam, method);
						}
					}
				}
			}
		}
		if (method != null)
		{
			return method;
		}
		
		throw new NoSuchMethodException(getMethodDeclaration(pBaseClass, pMethod, (Object[])pParamTypes));
	}
	
	/**
	 * Gets the compatible method with the given types.
	 * Compatible Method means, if I search a method test(Number), test(BigDecimal) would be compatible
	 * and can also be used, as BigDecimal is a special case of Number as parameter.   
	 * The method needs the public modifier. If there is more than one method
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pBaseClass the object class or a super class from which the method will be called
	 * @param pMethod the compatible method
	 * @param pParamTypes parameters for the method
	 * @return the method
	 * @throws NoSuchMethodException if Method does not exist. 
	 */
	public static final Method getCompatibleMethod(Class<?> pBaseClass, String pMethod, Class... pParamTypes) throws NoSuchMethodException
	{
		return getCompatibleMethod(pBaseClass, false, pMethod, pParamTypes);
	}
	
	/**
	 * Gets the compatible method with the given types.
	 * Compatible Method means, if I search a method test(Number), test(BigDecimal) would be compatible
	 * and can also be used, as BigDecimal is a special case of Number as parameter.   
	 * The method needs the public modifier. If there is more than one method
	 * with the same name, the parameterlist will be used to find the fitting method.
	 *
	 * @param pBaseClass the object class or a super class from which the method will be called
	 * @param pOnlyDeclared <code>true</code> to use only declared methods
	 * @param pMethod the compatible method
	 * @param pParamTypes parameters for the method
	 * @return the method
	 * @throws NoSuchMethodException if Method does not exist. 
	 */
	public static final Method getCompatibleMethod(Class<?> pBaseClass, boolean pOnlyDeclared, String pMethod, Class... pParamTypes) throws NoSuchMethodException
	{
		long keyClass = pBaseClass.hashCode() | ((long)pMethod.hashCode() << 32) & 0x00ffffff00000000L;

		Method method;

		if (pParamTypes == null || pParamTypes.length == 0)
		{
			method = getMethod(keyClass);
			
			if (method == null)
			{
				if (pOnlyDeclared)
				{
					method = pBaseClass.getDeclaredMethod(pMethod);
				}
				else
				{
					method = pBaseClass.getMethod(pMethod);
				}

				putMethod(keyClass, method);
			}
		}
		else
		{
			long keyParam = keyClass | ((0x100 - (long)pParamTypes.length) << 56); // cache with different key, to avoid conflict with getMethod
			
			method = getMethod(keyParam);

			if (method == null || !method.getName().equals(pMethod) || !isParamTypeListValid(pParamTypes, method.getParameterTypes(), method.isVarArgs()))
			{
				try
				{
					method = getMethod(pBaseClass, pOnlyDeclared, pMethod, pParamTypes);
				}
				catch (NoSuchMethodException ex)
				{
					try
					{
					    if (pOnlyDeclared)
					    {
					        method = pBaseClass.getDeclaredMethod(pMethod, pParamTypes);
					    }
					    else
					    {
	                        method = pBaseClass.getMethod(pMethod, pParamTypes);
					    }
						
						putMethod(keyParam, method);
					}
					catch (NoSuchMethodException ex2)
					{
						Method[] methods;
						
						if (pOnlyDeclared)
						{
							methods = pBaseClass.getDeclaredMethods();
						}
						else
						{
							methods = pBaseClass.getMethods();
						}
						
						method = null;
						
						for (int i = 0, anz = methods.length; i < anz && method == null; i++)
						{				
							if (methods[i].getName().equals(pMethod) && isParamTypeListValid(pParamTypes, methods[i].getParameterTypes(), methods[i].isVarArgs()))
							{
								method = methods[i];
								
								putMethod(keyParam, method);
							}
						}
					}
				}
			}
		}
		if (method != null)
		{
			return method;
		}
		
		throw new NoSuchMethodException(getMethodDeclaration(pBaseClass, pMethod, (Object[])pParamTypes));
	}
	
	/**
	 * Gets the method for a possible call.
	 * 
     * @param pBaseClass the object class or a super class from which the method will be called
     * @param pMethod invocable method
     * @param pParams parameters for the method
     * @return return value from the method invocation
     * @throws Throwable if it is not possible to invoke the method
     * @see #getMethodForCall(Class, boolean, String, Object...)
	 */
    public static final Method getMethodForCall(Class<?> pBaseClass, String pMethod, Object... pParams) throws Throwable
    {
        return getMethodForCall(pBaseClass, false, pMethod, pParams);
    }
    
    /**
     * Gets the method for a possible call. This method will be used in {@link #call(Object, Class, boolean, String, Object...)}.
     * 
     * @param pBaseClass the object class or a super class from which the method will be called
     * @param pOnlyDeclared <code>true</code> to use only declared methods
     * @param pMethod invocable method
     * @param pParams parameters for the method
     * @return return value from the method invocation
     * @throws Throwable if it is not possible to invoke the method
     */
    public static final Method getMethodForCall(Class<?> pBaseClass, boolean pOnlyDeclared, String pMethod, Object... pParams) throws Throwable
    {
        long keyClass = pBaseClass.hashCode() | ((long)pMethod.hashCode() << 32) & 0x00ffffff00000000L;
        
        long keyParam = keyClass | ((pParams == null ? 0L : (long)pParams.length) << 56);
        
        Method method = getMethod(keyParam);

        if (method == null || !method.getName().equals(pMethod) || !isParamListValid(method.getParameterTypes(), pParams, method.isVarArgs()))
        {
            Method[] methods;
            
            if (pOnlyDeclared)
            {
                methods = pBaseClass.getDeclaredMethods();
            }
            else
            {
                methods = pBaseClass.getMethods();
            }
            
            method = null;
            Method arrayMethod = null;
            // Prefer arrayMethods, if a not array method and a array method are found with the same parameters.
            // There would be still a better method to decide which method should be called,
            // but it is not implemented yet!!!
            // in case of Object[] the caller has to cast to (Object).
            // This will ensure, that the Object[] will not be set directly -> the parameter is Object[][] instead of Object[]
            // Then all methods have to be searched, and the Array method should only be preferred, if the parameter is also an array.
            // There is no test case until now for doing that.
            // Anyway it would be best to ensure unique method names!!!
            
            for (int i = 0, anz = methods.length; i < anz && arrayMethod == null; i++)
            {               
                Class<?>[] parameterTypes = methods[i].getParameterTypes();
                
                if (methods[i].getName().equals(pMethod) && isParamListValid(parameterTypes, pParams, methods[i].isVarArgs()))
                {
                    if (parameterTypes.length > 0 
                            && parameterTypes[parameterTypes.length - 1].isArray()
                            && !parameterTypes[parameterTypes.length - 1].getComponentType().isPrimitive())
                    {
                        arrayMethod = methods[i];
                    }
                    else if (method == null)
                    {
                        method = methods[i];
                    }
                }
            }
            if (arrayMethod != null)
            {
                method = arrayMethod;
            }
            if (method != null)
            {
                putMethod(keyParam, method);
            }
        }

        return method;
    }
	
	/**
	 * Gets the best matching constructor from the given class.
	 * 
	 * @param pClassLoader the class loader that should load the class
	 * @param pClassName the full qualified class name "com.package.classname"
	 * @param pParams the parameters for the constructor
	 * @return the constructor
	 * @throws Exception if no constructor was found
	 */
	public static Constructor getConstructor(ClassLoader pClassLoader, String pClassName, Object... pParams) throws Exception
	{
		return getConstructor(Class.forName(pClassName, true, pClassLoader), false, pParams);		
	}
	
	/**
	 * Gets the best matching constructor from the given class.
	 * 
	 * @param pClass the class that should checked
	 * @param pOnlyDeclared <code>true</code> if only declared constructors should be used, <code>false</code> to search
	 *                      all constructors
	 * @param pParams the parameters for the constructor
	 * @return the constructor
	 * @throws Exception if no constructor was found
	 */
	public static Constructor getConstructor(Class<?> pClass, boolean pOnlyDeclared, Object... pParams) throws Exception
	{
		long keyClass = pClass.hashCode();

		Constructor<?> constructor;

		long keyParam = keyClass | ((pParams == null ? 0L : (long)pParams.length) << 32);
		
		constructor = getConstructor(keyParam);

		if (constructor == null || !isParamListValid(constructor.getParameterTypes(), pParams, constructor.isVarArgs()))
		{
			Constructor<?>[] constructors;
			
			if (pOnlyDeclared)
			{
				constructors = pClass.getDeclaredConstructors();
			}
			else
			{
				constructors = pClass.getConstructors();
			}
			
			constructor = null;
			
			for (int i = 0, anz = constructors.length; i < anz && constructor == null; i++)
			{					
				if (isParamListValid(constructors[i].getParameterTypes(), pParams, constructors[i].isVarArgs()))
				{
					constructor = constructors[i];
					
					putConstructor(keyParam, constructor);
				}
			}
		}

		if (constructor != null)
		{
			return constructor;
		}
		
		throw new NoSuchMethodException(getMethodDeclaration(pClass, null, pParams));
	}

	/**
	 * Gets all methods (without parameters) which has a return value that is assignable from the given
	 * return value.
	 * 
	 * @param pClass the class to check for the methods
	 * @param pReturn the expected return value
	 * @param pOnlyDeclared <code>true</code> to get only declared methods, <code>false</code> to get all methods
	 * @return the found methods or an empty array
	 */
	public static Method[] getMethodsByReturnValue(Class<?> pClass, Class<?> pReturn, boolean pOnlyDeclared)
	{
	    Method[] met;
	    
	    if (pOnlyDeclared)
	    {
	        met = pClass.getDeclaredMethods();
	    }
	    else
	    {
	        met = pClass.getMethods();
	    }
	    
	    Class<?>[] clsParam;
	    
	    ArrayUtil<Method> auMethods = new ArrayUtil<Method>();
	    
	    for (int i = 0; i < met.length; i++)
	    {
	        clsParam = met[i].getParameterTypes(); 
	        
	        if ((clsParam == null || clsParam.length == 0)
	            && pReturn.isAssignableFrom(met[i].getReturnType()))
	        {
	            auMethods.add(met[i]);
	        }
	    }
	    
	    return auMethods.toArray(new Method[auMethods.size()]);
	}
	
	/**
	 * Gets a list of all available annotations of a specific type.
	 * 
	 * @param <T> the annotation type to search
	 * @param pMethod the method to use
	 * @param pClass the annotation type
	 * @return the list of all found annotations or an empty list (never <code>null</code>)
	 */
	public static <T> List<T> getAnnotation(Method pMethod, Class<T> pClass)
	{
		Annotation[] annotations = pMethod.getAnnotations();
		
		if (annotations != null)
		{
			ArrayUtil<T> liFound = new ArrayUtil<T>();
			
			for (int i = 0; i < annotations.length; i++)
			{
				if (pClass.isAssignableFrom(annotations[i].getClass()))
				{
					liFound.add((T)annotations[i]);
				}
			}
			
			return liFound;
		}
		
		return new ArrayUtil<T>();
	}	
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>Parameter</code> class is a placeholder for parameters. It allows you to
	 * use a type-class if e.g. the value is null.
	 * 
	 * @author Ren� Jahn
	 */
	public static final class Parameter
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The parameter class-type. */
		private Class<?> clazz;
		
		/** The parameter value. */
		private Object value;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Ctreates a new instance of <code>Parameter</code>. The class-type is set to the type
		 * of the value.
		 * 
		 * @param pValue the value
		 */
		public Parameter(Object pValue)
		{
			if (pValue != null)
			{
				clazz = pValue.getClass();
			}
			
			value = pValue;
		}
		
		/**
		 * Creates a new instance of <code>Parameter</code>.
		 * 
		 * @param pClass the type-class. If the class is null, the class from the value is used if it is
		 *               not null
		 * @param pValue the value
		 */
		public Parameter(Class<?> pClass, Object pValue)
		{
			if (pClass != null)
			{
				clazz = pClass;
			}
			else if (pValue != null)
			{
				clazz = pValue.getClass();
			}
			
			value = pValue;
			
			if (pClass != null && pValue != null && !pClass.isAssignableFrom(pValue.getClass()))
			{
				throw new IllegalArgumentException("Type-class class is not assignable from value class!");
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the type class.
		 * 
		 * @return the class
		 */
		public Class<?> getTypeClass()
		{
			return clazz;
		}
		
		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public Object getValue()
		{
			return value;
		}
		
	}	// Parameter
    
}	// Reflective
