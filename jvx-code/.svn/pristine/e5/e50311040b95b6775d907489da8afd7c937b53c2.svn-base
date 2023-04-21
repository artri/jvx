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
 * 17.02.2009 - [HM] - creation
 * 23.03.2020 - [JR] - #2239: custom bean class support (classloader)
 */
package javax.rad.type.bean;

import java.beans.Transient;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.WeakHashMap;

import javax.rad.type.AbstractType;
import javax.rad.type.IType;
import javax.rad.type.StringType;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The <code>BeanType</code> is a wrapper for dynamic/generic beans and POJOs. With this
 * class you can set/get properties without get/set methods.
 * 
 * @author Martin Handsteiner
 */
public class BeanType extends AbstractBeanType<Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The logger for protocol the performance. */
	private static ILogger logger = LoggerFactory.getInstance(BeanType.class);

	/** The property cache. */
	private static WeakHashMap<Class, WeakReference<BeanType>> beanTypeCache = new WeakHashMap<Class, WeakReference<BeanType>>();

	/** The type class. */
	protected transient Class typeClass;
	
    /** The bean class. */
    protected transient Constructor ibeanConstructor;
    
	/** The get methods. */
	protected transient Method[] getMethods;
	
	/** The set methods. */
	protected transient Method[] setMethods;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>BeanType</code>.
	 */
	public BeanType()
	{
		super(null, (String[])null);
	}
	
	/**
	 * Constructs a new <code>BeanType</code> with given property names.
	 * 
	 * @param pPropertyNames the bean properties.
	 */
	public BeanType(String... pPropertyNames)
	{
		this(null, pPropertyNames);
	}
	
	/**
	 * Constructs a new <code>BeanType</code> with given class name and property list.
	 * 
	 * @param pClassName the class name.
	 * @param pPropertyNames the bean properties.
	 */
	public BeanType(String pClassName, String[] pPropertyNames)
	{
		super(pClassName, pPropertyNames);

		if (pClassName != null && !Bean.class.getName().equals(pClassName))
		{
    		try
    		{
    			ClassLoader cls = ResourceUtil.getDefaultClassLoader();
    			
    			if (cls != null)
    			{
    			    typeClass = Class.forName(pClassName, true, cls);
    			}
    			else
    			{
    			    typeClass = Class.forName(pClassName);
    			}
    			
    			if (IBean.class.isAssignableFrom(typeClass))
    			{
    			    initBeanConstructor();
    			    
                    fixedProperties = false;
    			}
    			else
    			{
        			BeanType beanType = getBeanType(typeClass);
        			
        			int size = propertyNames.size();
        			propertyDefinitions = new PropertyDefinition[size];
        			getMethods = new Method[size];
        			setMethods = new Method[size];
        			
        			for (int i = 0; i < size; i++)
        			{
        			    String propertyName = propertyNames.get(i);
        				int index = beanType.getPropertyIndex(propertyName);
        				
        				if (index >= 0)
        				{
        				    propertyDefinitions[i] = beanType.propertyDefinitions[index];
        					getMethods[i] = beanType.getMethods[index];
        					setMethods[i] = beanType.setMethods[index];
        				}
        				else
        				{
        				    propertyDefinitions[i] = createPropertyDefinition(propertyName);
        				}
        			}
    			}
    		}
    		catch (Exception pException)
    		{
    		    // class not fully available, normal Bean will be used. 
    		}
		}
	}
	
	/**
	 * Constructs a new <code>BeanType</code> with given {@link PropertyDefinition}s.
	 * 
	 * @param pPropertyDefinitions the bean properties.
	 */
	public BeanType(PropertyDefinition... pPropertyDefinitions)
	{
		this(null, pPropertyDefinitions);
	}	
		
		
	/**
	 * Constructs a new <code>BeanType</code> with given class name and {@link PropertyDefinition}s.
	 * 
	 * @param pClassName the class name.
	 * @param pPropertyDefinitions the bean properties.
	 */
	public BeanType(String pClassName, PropertyDefinition[] pPropertyDefinitions)
	{
		super(pClassName, pPropertyDefinitions);
		
		if (pClassName != null && !Bean.class.getName().equals(pClassName))
		{
		    try
		    {
		        ClassLoader cls = ResourceUtil.getDefaultClassLoader();
	                
		        if (cls != null)
		        {
		            typeClass = Class.forName(pClassName, true, cls);
		        }
		        else
		        {
		            typeClass = Class.forName(pClassName);
		        }
	                
		        if (IBean.class.isAssignableFrom(typeClass))
		        {
		            initBeanConstructor();
                    
		            fixedProperties = false;
		        }
		        else
		        {
		            BeanType beanType = getBeanType(typeClass);
	                    
                    int size = propertyNames.size();
		            getMethods = new Method[size];
		            setMethods = new Method[size];
	                    
		            for (int i = 0; i < size; i++)
		            {
		                int index = beanType.getPropertyIndex(propertyNames.get(i));
	                        
		                if (index >= 0)
		                {
		                    getMethods[i] = beanType.getMethods[index];
		                    setMethods[i] = beanType.setMethods[index];
		                }
		            }
		        }
		    }
		    catch (Exception pException)
		    {
                // class not fully available, normal Bean will be used. 
		    }
		}
	}
	
	/**
	 * Constructs a new <code>BeanType</code> from a POJO.
	 * 
	 * @param pBeanClass the bean class.
	 */
	protected BeanType(Class pBeanClass)
	{
	    if (pBeanClass == Bean.class)
	    {
	        fixedProperties = false;
	    }
	    else
	    {
	        typeClass = pBeanClass;
    		className = typeClass.getName();
    		
    		if (IBean.class.isAssignableFrom(typeClass))
    		{
    		    initBeanConstructor();
    		    
                fixedProperties = false;
    		}
    		else
    		{
        		HashMap<String, PropertyDefinition> beanProps = new HashMap<String, PropertyDefinition>();
        		HashMap<String, Method> getMeth = new HashMap<String, Method>();
        		HashMap<String, Method> setMeth = new HashMap<String, Method>();
        
        		Method[] methods = pBeanClass.getMethods();
        		Field[]  fields = pBeanClass.getDeclaredFields();
        		
        		for (int i = 0; i < methods.length; i++)
        		{
        			Method method = methods[i];
        			
        			if (!Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0)
        			{
        				String methodName = method.getName();
        				
        				String propertyName;
        				if (methodName.startsWith("get"))
        				{
        					propertyName = methodName.substring(3);
        				}
        				else if (methodName.startsWith("is"))
        				{
        					propertyName = methodName.substring(2);
        				}
        				else
        				{
        				    propertyName = null;
        				}
        				if (propertyName != null)
        				{
        					try
        					{
        						Method setMethod = pBeanClass.getMethod("set" + propertyName, method.getReturnType());
        						if (!Modifier.isStatic(setMethod.getModifiers()))
        						{
        						    PropertyDefinition propertyDefinition = createPropertyDefinition(
        						            Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1), method.getReturnType());
        						    String propertyNameIntern = propertyDefinition.getName(); // interned name
        						    
        						    // Look, if the getter or setter is transient
        						    if (method.isAnnotationPresent(Transient.class) || setMethod.isAnnotationPresent(Transient.class))
        							{
        							    propertyDefinition.setTransient(true);
        							}
        						    else
        						    {    
                                        try // check, if there is a field with property name and correct type.
                                        {
                                            Field field = pBeanClass.getDeclaredField(propertyNameIntern);
                                            if (Modifier.isTransient(field.getModifiers())
                                                    && method.getReturnType().isAssignableFrom(field.getType()))
                                            {
                                                propertyDefinition.setTransient(true);
                                            }
                                        }
                                        catch (Exception ex)
                                        {
                                            try // check, if there is a field with prefix and property name and correct type.
                                            {
                                                Field propField = null;
                                                for (Field field : fields)
                                                {
                                                    if (field.getName().endsWith(propertyName) && method.getReturnType().isAssignableFrom(field.getType())
                                                            && (propField == null || field.getName().length() < propField.getName().length()))
                                                    {
                                                        propField = field;
                                                    }
                                                }
                                                if (propField != null && Modifier.isTransient(propField.getModifiers()))
                                                {
                                                    propertyDefinition.setTransient(true);
                                                }
                                            }
                                            catch (Exception ex2)
                                            {
                                                // ignore, there is no field with proper name
                                            }
                                        }
        						    }
        							
        						    propertyNames.add(propertyNameIntern); // property name is interned.
        							beanProps.put(propertyNameIntern, propertyDefinition);
        							getMeth.put(propertyNameIntern, method);
        							setMeth.put(propertyNameIntern, setMethod);
        						}
        					}
        					catch (NoSuchMethodException pNoSuchMethodException)
        					{
        						// Do nothing
        					}
        				}
        			}
        		}
        		Collections.sort(propertyNames);
        		
                fixedProperties = true;
                int size = propertyNames.size();
        		propertyDefinitions = new PropertyDefinition[size];
        		getMethods = new Method[size];
        		setMethods = new Method[size];
        		
        		for (int i = 0; i < size; i++)
        		{
        			String propertyName = propertyNames.get(i);
        			propertyDefinitions[i] = beanProps.get(propertyName);
        			getMethods[i] = getMeth.get(propertyName);
        			setMethods[i] = setMeth.get(propertyName);
        		}
    		}
	    }
	}

	/**
	 * Initializes the constructor for a bean class. 
	 * If no constructor exists, the bean class will be set top null, as it is not usable.
	 */
	protected void initBeanConstructor()
	{
	    if (Modifier.isPublic(typeClass.getModifiers()))
        {
            try
            {
                ibeanConstructor = typeClass.getConstructor(BeanType.class);
            }
            catch (Exception ex)
            {
                try
                {
                    ibeanConstructor = typeClass.getConstructor(IBeanType.class);
                }
                catch (Exception ex2)
                {
                    try
                    {
                        ibeanConstructor = typeClass.getConstructor();
                    }
                    catch (Exception ex3)
                    {
                        typeClass = null;
                    }
                }
            }
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// IType
	
	/**
	 * {@inheritDoc}
	 */
	public Class getTypeClass()
	{
		if (typeClass == null)
		{
			return Bean.class;
		}
		else
		{
			return typeClass;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Object object1, Object object2)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a singleton instance of BeanType for the given bean class.
	 * @param pBeanClass the POJO class.
	 * @return the POJO bean type.
	 */
	public static BeanType getBeanType(Class pBeanClass)
	{
		WeakReference<BeanType> weakPojoBeanClass = beanTypeCache.get(pBeanClass);
        
        BeanType beanType;
        if (weakPojoBeanClass == null)
        {
        	beanType = null;
        }
        else
        {
        	beanType = weakPojoBeanClass.get();
        }
		if (beanType == null)
		{
			beanType = new BeanType(pBeanClass);
			
			if (beanType.hasFixedProperties()) // We'll cache only fixed property beans.
			{
			    beanTypeCache.put(pBeanClass, new WeakReference(beanType));
			}
		}
		return beanType;
	}
	
	/**
	 * Gets the BeanType for the given bean.
	 * @param pObject the bean.
	 * @return the bean type.
	 */
	public static BeanType getBeanType(Object pObject)
	{
		if (pObject instanceof IBean)
		{
			return (BeanType)((IBean)pObject).getBeanType();
		}
		else
		{
			return getBeanType(pObject.getClass());
		}
	}
	
	/**
	 * Creates the PropertyDefinition.
	 * 
	 * @param pPropertyName the property name.
	 * @param pPropertyType the property type.
	 * @return the PropertyDefinition
	 */
	protected PropertyDefinition createPropertyDefinition(String pPropertyName, Class pPropertyType)
	{
	    IType<?> type = AbstractType.getTypeFromClass(pPropertyType);
	    if (type instanceof StringType) // In Method calls, the method is responsible to convert empty strings to null. Otherwise clone changes values.
	    {
	        ((StringType)type).setNullForEmptyString(false);
	    }
	    
		return new PropertyDefinition(pPropertyName, type, pPropertyType);
	}

    /**
     * Gets, if this BeanType is for a pojo type class.
     * 
     * @return true, if this BeanType is for a pojo type class.
     */
    public boolean isPojoTypeClass()
    {
        return typeClass != null && ibeanConstructor == null;
    }
    
	/**
	 * Creates a new instance of the bean.
	 * If it is possible, the pojo is returned.
	 * If not, a instance of Bean is returned. 
	 * 
	 * @return the pojo or a bean
	 */
	public Object newInstance()
	{
	    if (ibeanConstructor != null)
	    {
            try
            {
                if (ibeanConstructor.getParameterCount() == 0)
                {
                    return ibeanConstructor.newInstance();
                }
                else
                {
                    return ibeanConstructor.newInstance(this);
                }
            }
            catch (Throwable pThrowable)
            {
                logger.info("newInstance ", ibeanConstructor, " failed!", pThrowable);
            }
	    }
	    else if (typeClass != null)
	    {
    		try
    		{
    			return typeClass.newInstance();
    		}
    		catch (Throwable pThrowable)
    		{
    			logger.info("newInstance ", getClassName(), " failed!", pThrowable);
    		}
	    }
	    
        return new Bean(this);
	}
	
	/**
	 * Gets the value for a bean.
	 * 
	 * @param pObject the bean.
	 * @param pPropertyIndex the property index.
	 * @return pValue the value of the property index.
	 */
	public Object get(Object pObject, int pPropertyIndex)
	{
		if (pObject instanceof IBean)
		{
		    if (pObject instanceof AbstractBean)
	        {
	            return ((AbstractBean)pObject).get(pPropertyIndex);
	        }
	        else 
	        {
	            return ((IBean)pObject).get(propertyNames.get(pPropertyIndex));
	        }
		}
		else
		{
			try
			{
				Method method = getMethods[pPropertyIndex];
				// If the property does not exist, the value is not set (silently) for more compatibility between
				// Client - Server communication.
				if (method != null) 
				{
					return getMethods[pPropertyIndex].invoke(pObject);
				}
				else
				{
					return null;
				}
			}
			catch (Throwable pThrowable)
			{
				while (pThrowable instanceof InvocationTargetException
						|| pThrowable instanceof UndeclaredThrowableException)
				{
					pThrowable = pThrowable.getCause();
				}
				if (pThrowable instanceof RuntimeException)
				{
					throw (RuntimeException)pThrowable;
				}
				else
				{
					throw new RuntimeException(pThrowable);
				}
			}
			
		}
	}

	/**
	 * Sets the value for a bean.
	 * 
	 * @param pObject the bean.
	 * @param pPropertyIndex the property index.
	 * @param pValue the value of the property index.
	 */
	public void put(Object pObject, int pPropertyIndex, Object pValue)
	{
		if (pObject instanceof IBean)
		{
	        if (pObject instanceof AbstractBean)
	        {
	            ((AbstractBean)pObject).put(pPropertyIndex, pValue);
	        }
	        else
	        {
	            ((IBean)pObject).put(propertyNames.get(pPropertyIndex), pValue);
	        }
		}
		else
		{
			try
			{
				// UNKNOWN_TYPE is default, and has no type conversion or validation.
				if (propertyDefinitions != null) 
				{
					pValue = propertyDefinitions[pPropertyIndex].getType().validatedValueOf(pValue);
				}
				Method method = setMethods[pPropertyIndex];
				// If the property does not exist, the value is not set silently for more compatibility between
				// Client - Server communication.
				if (method == null)
				{
					logger.debug("put ", propertyNames.get(pPropertyIndex), " does not exist in this VM, it is silent ignored!");
				}
				else
				{
					method.invoke(pObject, pValue);
				}
			}
			catch (Throwable pThrowable)
			{
				while (pThrowable instanceof InvocationTargetException
						|| pThrowable instanceof UndeclaredThrowableException)
				{
					pThrowable = pThrowable.getCause();
				}
				if (pThrowable instanceof RuntimeException)
				{
					throw (RuntimeException)pThrowable;
				}
				else
				{
					throw new RuntimeException(pThrowable);
				}
			}
		}
	}
	
	/**
	 * Gets a value from a bean.
	 * 
	 * @param pObject the bean.
	 * @param pPropertyName the property name.
	 * @return the value of the property name.
	 */
	public Object get(Object pObject, String pPropertyName)
	{
		int index = getPropertyIndex(pPropertyName);
		if (index < 0)
		{
			return null;
		}
		else
		{
			return get(pObject, index);
		}
	}
	
	/**
	 * Sets the value for a bean.
	 * 
	 * @param pObject the bean.
	 * @param pPropertyName the property name.
	 * @param pValue the value of the property name.
	 */
	public void put(Object pObject, String pPropertyName, Object pValue)
	{
		int index = getPropertyIndex(pPropertyName);
		if (index < 0)
		{
			throw new IllegalArgumentException("The property [" + pPropertyName + "] does not exist!");
		}
		
		put(pObject, index, pValue);
	}
	
    /**
     * Gets the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
     * 
     * @param pPropertyIndex the property index.
     * @return the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
     */
    public Method getGetMethod(int pPropertyIndex)
    {
        if (getMethods != null)
        {
            return getMethods[pPropertyIndex];
        }
        return null;
    }
    
	/**
	 * Gets the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
	 * 
	 * @param pPropertyName the property name.
	 * @return the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
	 */
	public Method getGetMethod(String pPropertyName)
	{
		if (getMethods != null)
		{
			int index = getPropertyIndex(pPropertyName);
			if (index >= 0)
			{
				return getMethods[index];
			}
		}
		return null;
	}
	
    /**
     * Gets the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
     * 
     * @param pPropertyIndex the property index.
     * @return the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
     */
    public Method getSetMethod(int pPropertyIndex)
    {
        if (setMethods != null)
        {
            return setMethods[pPropertyIndex];
        }
        return null;
    }
    
	/**
	 * Gets the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
	 * 
	 * @param pPropertyName the property name.
	 * @return the method for getting a value, in case of a class based BeanType and the property exists, null otherwise.
	 */
	public Method getSetMethod(String pPropertyName)
	{
		if (setMethods != null)
		{
			int index = getPropertyIndex(pPropertyName);
			if (index >= 0)
			{
				return setMethods[index];
			}
		}
		return null;
	}
	
	/**
	 * Clones a bean.
	 * 
	 * @param pObject the bean.
	 * @return the cloned bean.
	 */
	public Object clone(Object pObject)
	{
   		Object result = newInstance();

   		for (int i = 0, count = getPropertyCount(); i < count; i++)
   		{
   			try
   			{
   				put(result, i, get(pObject, i));
   			}
   			catch (Exception ex)
   			{
   				// Do nothing
   			}
   		}

   		return result;
	}
	
}	// BeanType
