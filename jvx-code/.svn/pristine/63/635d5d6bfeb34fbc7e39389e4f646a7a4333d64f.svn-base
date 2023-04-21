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
 * 03.04.2009 - [HM] - creation
 * 25.08.2016 - [JR] - #1674: putAll implemented
 */
package javax.rad.type.bean;

import javax.rad.type.bean.event.PropertyChangeHandler;
import javax.rad.type.bean.event.PropertyChangedEvent;
import javax.rad.util.EventHandler;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>Bean</code> is a bean with dynamic properties. It's a sort of dynamic/generic
 * bean without special get/set implementation.
 * 
 * @author Martin Handsteiner
 */
public class Bean extends AbstractBean<BeanType>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The array extension size. Formally it was 1 and therefore slow. */
    private static final int ARRAY_EXTENSION_SIZE = 8;
    
	/** The logger for protocol the performance. */
	private static ILogger logger = LoggerFactory.getInstance(Bean.class);
	
	/** The property change handler. */
    private PropertyChangeHandler eventPropertyChanged;

	/** The values for the properties. */
	private Object values;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates an empty <code>Bean</code>.
	 */
	public Bean()
	{
	    super(); // special case, we want to create beantype with own class, but getClass() is not allowed in super();

	    beanType = new BeanType(getClass());
	}
	
    /**
     * Creates a <code>Bean</code> with a {@link IBeanType}.
     * 
     * @param pBeanType the bean type.
     * @throws ClassCastException if given type is not an instance of {@link BeanType}
     */
    public Bean(IBeanType pBeanType)
    {
        super((BeanType)pBeanType);
    }

    /**
	 * Creates a <code>Bean</code> with a {@link BeanType}.
	 * 
	 * @param pBeanType the bean type.
	 */
	public Bean(BeanType pBeanType)
	{
		super(pBeanType);
	}
	
    /**
	 * Creates a <code>Bean</code> with a {@link BeanType}.
	 * 
	 * @param pBeanType the bean type.
	 * @param pValues the values.
	 */
	public Bean(BeanType pBeanType, Object... pValues)
	{
		super(pBeanType);
		
		if (pValues != null)
		{
			for (int i = 0; i < pValues.length; i++)
			{
				put(i, pValues[i]);
			}
		}
	}
	
	/**
	 * Creates a <code>Bean</code> with an object.
	 * 
	 * @param pObject the object.
	 */
	public Bean(Object pObject)
	{
		super(BeanType.getBeanType(pObject));

		if (pObject instanceof Bean)
		{
		    Bean bean = (Bean)pObject;
		    if (bean.values != null)
		    {
		        if (bean.values instanceof Object[])
	            {
	                values = ((Object[])bean.values).clone();
	            }
	            else 
	            {
	                values = beanType.clone(bean.values);
	            }
	        }
		}
		else
		{
		    values = pObject;
		}
	}
	
	/**
	 * Creates a <code>Bean</code> with a bean class.
	 * 
	 * @param pBeanClass the bean class.
	 */
	public Bean(Class pBeanClass)
	{
		super(BeanType.getBeanType(pBeanClass));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Object pObject)
	{
	    if (pObject instanceof Object[])
	    {
	        Object[] arr = (Object[])pObject;
            for (int i = 0; i < arr.length; i++)
            {
                put(i, arr[i]);
            }
	    }
	    else
	    {
    	    IBean bean;
    	    
    	    if (pObject instanceof IBean)
    	    {
    	        bean = (IBean)pObject;
    	    }
    	    else
    	    {
    	        bean = new Bean(pObject);
    	    }
    	    
    	    for (String property : bean.getBeanType().getPropertyNames())
    	    {
    	        put(property, bean.get(property));
    	    }
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void put(int pIndex, Object pValue)
    {
        if (values == null)
        {
            if (beanType.isPojoTypeClass())
            {
                try
                {
                    values = beanType.typeClass.newInstance();
                }
                catch (Throwable pThrowable)
                {
                    logger.info("newInstance ", beanType.getClassName(), " failed!", pThrowable);
                    
                    values = new Object[Math.max(ARRAY_EXTENSION_SIZE, beanType.getPropertyCount())];
                }
            }
            else
            {
                values = new Object[Math.max(ARRAY_EXTENSION_SIZE, beanType.getPropertyCount())];
            }
        }

        if (beanType.propertyDefinitions != null)
        {
            pValue = beanType.propertyDefinitions[pIndex].getType().validatedValueOf(pValue);
        }
        
        boolean bPropertyChangedDispatchable = EventHandler.isDispatchable(eventPropertyChanged);
        
        Object oldValue = bPropertyChangedDispatchable ? get(pIndex) : null;
        
        if (values.getClass() == Object[].class)
        {
            Object[] val = (Object[])values;

            if (pIndex >= val.length)
            {
                Object[] newVal = new Object[((beanType.getPropertyCount() - 1) / ARRAY_EXTENSION_SIZE + 1) * ARRAY_EXTENSION_SIZE];
                System.arraycopy(val, 0, newVal, 0, val.length);
                
                val = newVal;
                values = val;
            }
            
            val[pIndex] = pValue;
        }
        else
        {
            beanType.put(values, pIndex, pValue);
        }
        
        if (bPropertyChangedDispatchable)
        {
            try
            {
                eventPropertyChanged.dispatchEvent(new PropertyChangedEvent(beanType.propertyNames.get(pIndex), oldValue, pValue));
            }
            catch (Throwable e)
            {
                logger.error(e);
            }
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bean clone()
	{
		Bean clone = (Bean)super.clone();
		
		if (values != null)
		{
    		if (values instanceof Object[])
    		{
    			clone.values = ((Object[])values).clone();
    		}
    		else 
    		{
    			clone.values = beanType.clone(values);
    		}
		}
		
		return clone;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the Object that stores the values.
	 * It depends on the bean type, whether it is a pojo or an object array.
	 * 
	 * @return the values storage.
	 */
	public Object getObject()
	{
		return values;
	}
	
	/**
	 * Gets the value of a property. The property is identified by the index
	 * from the property list.
	 * 
	 * @param pIndex the property index
	 * @return the value of the property or <code>null</code> if the property was not found
	 */
	public Object get(int pIndex)
	{
		if (values == null)
		{
			return null;
		}
		else if (values.getClass() == Object[].class)
		{
		    try
		    {
		        return ((Object[])values)[pIndex];
		    }
		    catch (Exception ex)
		    {
		        return null;
		    }
		}
		else
		{
			return beanType.get(values, pIndex);
		}
	}
	
	/**
     * Gets the {@link PropertyChangeHandler} for property changed event.
     * 
     * @return the {@link PropertyChangeHandler}
     */
    public PropertyChangeHandler eventPropertyChanged()
    {
        if (eventPropertyChanged == null)
        {
            eventPropertyChanged = new PropertyChangeHandler();
        }
        
        return eventPropertyChanged;
    }

}	// Bean
