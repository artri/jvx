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
 * 06.03.2010 - [JR] - creation
 * 06.03.2010 - [JR] - copied methods from javax.rad.bean.BeanUtil 
 */
package com.sibvisions.util.type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;

/**
 * Implements the EL notation for a generic access to object methods 
 * or members.
 * 
 * @author Martin Handsteiner
 */
public final class BeanUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>BeanUtil</code> is a utility
	 * class.
	 */
	private BeanUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the property value from the given bean or sub bean.
	 * It can be an IBean, than get(String pPropertyName) is called, a POJO or an EJB.
	 * With a special dot notation is it possible to access properties from sub beans.
	 * An array or List access is possible with java like array notation.
	 * <code> 
	 *   Object result = BeanUtil.get(bean, "orders[5].ordernumber");
	 * </code>
	 * If the property name is null, the bean is returned.
	 * 
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @return the value of the given property name.
	 */
	public static Object get(Object pBean, String pPropertyName)
	{
		if (pPropertyName == null || pPropertyName.length() == 0)
		{
			return pBean;
		}
		else
		{
			int dotPos = pPropertyName.indexOf('.');
			
			if (dotPos < 0)
			{
				return getPropertyFromArray(pBean, pPropertyName);
			}
			else
			{
				Object object = getPropertyFromArray(pBean, pPropertyName.substring(0, dotPos));
				
				return get(object, pPropertyName.substring(dotPos + 1));
			}
		}
	}

	/**
	 * Gets an Object from an array or a list.
	 * 
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @return the object with the given index, if one exists.
	 */
	private static Object getPropertyFromArray(Object pBean, String pPropertyName)
	{
		if (pPropertyName.endsWith("]"))
		{
			int indexEnd = pPropertyName.length() - 1;
			int indexStart = pPropertyName.lastIndexOf('[');
			
			int index = Integer.parseInt(pPropertyName.substring(indexStart + 1, indexEnd));
			
			Object object = getProperty(pBean, pPropertyName.substring(0, indexStart));
			
		    if (object instanceof Object[])
		    {
		    	return ((Object[])object)[index];
		    }
		    else if (object instanceof List)
		    {
		    	return ((List)object).get(index);
		    }
		    else if (index == 0)
		    {
		    	return object;
		    }
		    else
		    {
		    	return null;
		    }
		}
		else
		{
			return getProperty(pBean, pPropertyName);
		}
	}
	
	/**
	 * Gets the property value from the given bean or sub bean.
	 * 
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @return the value of the given property name.
	 */
	private static Object getProperty(Object pBean, String pPropertyName)
	{
		if (pBean instanceof Map)
		{
			return ((Map<?, ?>)pBean).get(pPropertyName);
		}
		else
		{
			try
			{
				return Reflective.get(pBean, pPropertyName);
			}
			catch (Throwable pThrowable)
			{
				return null;
			}
		}
	}
	
	/**
	 * Sets the property value to the given bean or sub bean.
	 * It can be an IBean, than set(String pPropertyName, Object pValue) is called, a POJO or an EJB.
	 * With a special dot notation is it possible to access properties from sub beans.
	 * An array or List access is possible with java like array notation.
	 * <code> 
	 *   BeanUtil.set(bean, "orders[5].ordernumber", "4711");
	 * </code>
	 * 
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @param pValue the value to set.
	 */
	public static void set(Object pBean, String pPropertyName, Object pValue)
	{
		if (pPropertyName == null || pPropertyName.length() == 0)
		{
			throw new IllegalArgumentException("The property name my not be null!");
		}
		else
		{
			int dotPos = pPropertyName.indexOf('.');
			
			if (dotPos < 0)
			{
				setPropertyToArray(pBean, pPropertyName, pValue);
			}
			else
			{
				Object object = getPropertyFromArray(pBean, pPropertyName.substring(0, dotPos));
				
				set(object, pPropertyName.substring(dotPos + 1), pValue);
			}
		}
	}

	/**
	 * Sets an Object to an array or a list.
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @param pValue the object to set at the given index, if one exists.
	 */
	private static void setPropertyToArray(Object pBean, String pPropertyName, Object pValue)
	{
		if (pPropertyName.endsWith("]"))
		{
			int arrPos = pPropertyName.lastIndexOf('[');
			
			int index = Integer.parseInt(pPropertyName.substring(arrPos + 1, pPropertyName.length() - 1));
			
			String propertyName = pPropertyName.substring(0, arrPos);
			
			Object object = getProperty(pBean, propertyName);
			
		    if (object instanceof Object[])
		    {
		    	Object[] array = (Object[])object;
		    	if (index == array.length)
		    	{
		    		array = ArrayUtil.add(array, pValue);
		    		setProperty(pBean, propertyName, array);
		    	}
		    	else
		    	{
		    		array[index] = pValue;
		    	}
		    }
		    else if (object instanceof List)
		    {
		    	List list = (List)object;
		    	if (index == list.size())
		    	{
		    		list.add(pValue);
		    	}
		    	else
		    	{
		    		list.set(index, pValue);
		    	}
		    }
		    else 
		    {
		    	ArrayUtil list = new ArrayUtil();
		    	if (object != null)
		    	{
		    		list.add(object);
		    	}
		    	list.add(pValue);
		    	setProperty(pBean, propertyName, list);
		    }
		}
		else
		{
			setProperty(pBean, pPropertyName, pValue);
		}
	}
	
	/**
	 * Sets the property value to the given bean or sub bean.
	 * @param pBean the bean.
	 * @param pPropertyName the property name.
	 * @param pValue the value to set.
	 */
	private static void setProperty(Object pBean, String pPropertyName, Object pValue)
	{
		if (pBean instanceof Map)
		{
			((Map<Object, Object>)pBean).put(pPropertyName, pValue);
		}
		else
		{
			try
			{
				Reflective.set(pBean, pPropertyName, pValue);
			}
			catch (Throwable pThrowable)
			{
				pThrowable.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns an array containing all of the properties identified by <code>pPropertyName</code>
	 * from <code>pSource</code> in proper sequence. The runtime type of the returned array is that 
	 * of the specified <code>pDest</code> array.
	 * 
	 * @param <T> the type for the result array
	 * @param pSource the source object list
	 * @param pDest the result array
	 * @param pPropertyName the property name for getting the desired values
	 * @return an array with the size of <code>pSource</code> filled with values
	 */
	public static <T> T[] toArray(Object[] pSource, T[] pDest, String pPropertyName)
	{
		if (pSource == null)
		{
			return null;
		}
		
		int iElements = pSource.length;
		
        if (pDest.length < iElements)
        {
        	pDest = (T[])Array.newInstance(pDest.getClass().getComponentType(), iElements);
        }
        
        //get values
        for (int i = 0; i < iElements; i++)
        {
        	pDest[i] = (T)BeanUtil.get(pSource[i], pPropertyName);
        }

        //null termination
        if (pDest.length > iElements)
        {
        	pDest[iElements] = null;
        }
        
        return pDest;
	}
	
	/**
	 * Returns a list containing all of the properties identified by <code>pPropertyName</code>
	 * from <code>pSource</code> in proper sequence. The runtime type of the returned array is that 
	 * of the specified <code>pDest</code> array.
	 * 
	 * @param <T> the type for the result array
	 * @param pSource the source object list
	 * @param pPropertyName the property name for getting the desired values
	 * @return the list with the size of <code>pSource</code> filled with values
	 */
	public static <T> List<T> toList(Object[] pSource, String pPropertyName)
	{
        if (pSource != null)
        {
    		List<T> result = new ArrayList<T>(pSource.length);
		
	        for (int i = 0; i < pSource.length; i++)
	        {
	        	result.add((T)BeanUtil.get(pSource[i], pPropertyName));
	        }

	        return result;
		}
        else
        {
            return new ArrayList<T>(0);
        }
	}
	
	/**
	 * Returns a hashtable with a specific property as key and a specific property as value. If the key property
	 * is not unique then the last key overwrites all previous keys.
	 * 
	 * @param pObjects a list of objects with properties for the hashtable
	 * @param pKey the key property name
	 * @param pValue the value property name
	 * @return the hashtable with key/value column mapping
	 */
	public static Hashtable<Object, Object> toHashtable(List<?> pObjects, String pKey, String pValue)
	{
		if (pObjects == null)
		{
			return null;
		}
		
		Hashtable<Object, Object> htMapping = new Hashtable<Object, Object>(); 
		
		for (Object object : pObjects)
		{
			htMapping.put(BeanUtil.get(object, pKey), CommonUtil.nvl((String)BeanUtil.get(object, pValue), ""));
		}
		
		return htMapping;
	}
	
}	// BeanUtil
