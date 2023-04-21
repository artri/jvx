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
 * 03.03.2011 - [JR] - #300: beanType creation changed to be thread safe
 */
package javax.rad.type.bean;

import java.util.ArrayList;
import java.util.Arrays;

import javax.rad.type.AbstractType;
import javax.rad.type.IType;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>AbstractBeanType</code> is a bean definition without a bean object/class. It holds
 * the information about property names and definitions
 * 
 * @param <T> the type.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractBeanType<T> extends AbstractType<T> 
                                          implements IBeanType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The bean class. */
	protected String className;
	
    /** Whether properties are fix and cannot be extended. */
    protected boolean fixedProperties;
    
	/** The cached property names. */
	protected ArrayList<String> propertyNames;
	
    /** The cached property names. */
    private String[] cachedPropertyNames;
    
	/** The property definitions. */
	protected transient PropertyDefinition[] propertyDefinitions;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>AbstractBeanType</code>
	 * .
	 */
	protected AbstractBeanType()
	{
	    propertyNames = new ArrayList<String>();
	}
	
	/**
	 * Constructs a new <code>AbstractBeanType</code> with class name and a property list.
	 * 
	 * @param pClassName the class name.
	 * @param pPropertyNames the bean properties.
	 */
	protected AbstractBeanType(String pClassName, String[] pPropertyNames)
	{
        className = pClassName;
	    fixedProperties = className != null;
		
		propertyNames = new ArrayList<String>();
		if (pPropertyNames != null)
		{
            propertyNames.addAll(Arrays.asList(pPropertyNames));
		}
	}
	
	/**
	 * Constructs a new <code>AbstractBeanType</code> with a class name and {@link PropertyDefinition}s.
	 * 
	 * @param pClassName the class name.
	 * @param pPropertyDefinitions the property definitions.
	 */
	protected AbstractBeanType(String pClassName, PropertyDefinition[] pPropertyDefinitions)
	{
        className = pClassName;
        fixedProperties = pClassName != null;
		
		propertyNames = new ArrayList<String>();
		if (pPropertyDefinitions != null)
		{
			for (int i = 0; i < pPropertyDefinitions.length; i++)
			{
				propertyNames.add(pPropertyDefinitions[i].name);
			}
			
			propertyDefinitions = pPropertyDefinitions;
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// IBeanType
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getPropertyNames()
	{
	    if (cachedPropertyNames == null)
	    {
	        cachedPropertyNames = propertyNames.toArray(new String[propertyNames.size()]);
	    }
		return cachedPropertyNames;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPropertyIndex(String pPropertyName)
	{
	    return propertyNames.indexOf(pPropertyName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getPropertyCount()
	{
		return propertyNames.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PropertyDefinition getPropertyDefinition(int pIndex)
	{
		if (propertyDefinitions == null)
		{
			propertyDefinitions = createPropertyDefinitions();
		}
		
		return propertyDefinitions[pIndex];
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertyDefinition getPropertyDefinition(String pPropertyName)
	{
		int index = getPropertyIndex(pPropertyName);
		
		if (index < 0)
		{
			throw new IllegalArgumentException("The property [" + pPropertyName + "] does not exist!");
		}
		else
		{
			if (propertyDefinitions == null)
			{
				propertyDefinitions = createPropertyDefinitions();
			}
			
			return propertyDefinitions[index];
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		int hashCode = className == null ? 0 : className.hashCode();
		
     	for (String value : propertyNames)
     	{
		    hashCode = 31 * hashCode + value.hashCode();
		}
     	
     	return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object pObject)
    {
    	if (pObject == this)
    	{
    	    return true;
    	}
    	else if (pObject instanceof AbstractBeanType)
    	{
    		AbstractBeanType classDef = (AbstractBeanType)pObject;
    		
    		if ((className != classDef.className && (className == null || !className.equals(classDef.className))) 
    		        || propertyNames.size() != classDef.propertyNames.size())
    		{
    			return false;
    		}
    		else
    		{
				for (int i = 0, size = propertyNames.size(); i < size; i++)
				{
					if (!propertyNames.get(i).equals(classDef.propertyNames.get(i)))
					{
						return false;
					}
				}
				return true;
    		}
    	}
    	else
    	{
    		return false;
    	}
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the class name or identifier of the bean class.
	 * This name has to be unique for every different bean class.
	 * 
	 * @return the class name.
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Whether properties are fix or can be extended.
	 * 
	 * @return whether properties are fix or can be extended.
	 */
	public boolean hasFixedProperties()
	{
	    return fixedProperties;
	}
	
	/**
	 * Creates the PropertyDefinitions.
	 * 
	 * @return the property definitions 
	 */
	protected PropertyDefinition[] createPropertyDefinitions()
	{
		PropertyDefinition[] pdef = new PropertyDefinition[propertyNames.size()];
		
		for (int i = 0; i < pdef.length; i++)
		{
		    if (propertyDefinitions != null && i < propertyDefinitions.length)
		    {
		        pdef[i] = propertyDefinitions[i];
		    }
		    else
		    {
		        pdef[i] = createPropertyDefinition(propertyNames.get(i));
		    }
		}
		
		return pdef;
	}
	
	/**
	 * Creates the PropertyDefinition.
	 * 
	 * @param pPropertyName the property name.
	 * @return the PropertyDefinition
	 */
	protected PropertyDefinition createPropertyDefinition(String pPropertyName)
	{
		return new PropertyDefinition(pPropertyName, IType.UNKNOWN_TYPE);
	}
	
	/**
	 * Adds a property definition.
	 * @param pPropertyDefinition the property definition.
	 */
	public void addPropertyDefinition(PropertyDefinition pPropertyDefinition)
	{
		if (fixedProperties)
		{
			throw new UnsupportedOperationException("A Property can not be added manually if the IBeanType handles a bean class!");
		}
		
		if (ArrayUtil.indexOfReference(propertyNames, pPropertyDefinition.name) >= 0)
		{
    		throw new IllegalArgumentException("The property " + pPropertyDefinition.name + " is defined more than once!");
		}

        if (propertyDefinitions == null)
		{
			propertyDefinitions = createPropertyDefinitions();
		}
		propertyDefinitions = ArrayUtil.add(propertyDefinitions, pPropertyDefinition);

        propertyNames.add(pPropertyDefinition.name);
        cachedPropertyNames = null;
	}
	
	/**
	 * Adds a property definition.
	 * @param pPropertyName the property definition.
	 */
	public void addPropertyDefinition(String pPropertyName)
	{
		if (fixedProperties)
		{
			throw new UnsupportedOperationException("The property " + pPropertyName + " does not exist!");
		}

		if (ArrayUtil.indexOfReference(propertyNames, pPropertyName) >= 0)
		{
    		throw new IllegalArgumentException("The property " + pPropertyName + " is defined more than once!");
		}
		
		propertyNames.add(pPropertyName);
        cachedPropertyNames = null;

		if (propertyDefinitions != null)
		{
			propertyDefinitions = ArrayUtil.add(propertyDefinitions, createPropertyDefinition(pPropertyName));
		}
	}
	
	/**
	 * Removes all property definitions.
	 */
	public void removeAllPropertyDefinitions()
	{
	    propertyNames.clear();
	    propertyDefinitions = null;
	    cachedPropertyNames = null;
	}
		
}	// AbstractBeanType
