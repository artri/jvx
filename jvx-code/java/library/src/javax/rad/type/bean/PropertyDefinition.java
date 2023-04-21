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
package javax.rad.type.bean;

import javax.rad.type.IType;

/**
 * The <code>PropertyDefinition</code> is the property descriptor for a property in {@link IBeanType}.
 * 
 * @author Martin Handsteiner
 */
public class PropertyDefinition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The property name. */
	protected String name;
	
	/** The property type. */
	protected IType<?> type;
	
	/** The type class. */
	protected Class<?> typeClass;
	
    /** The transient flag. */
    protected boolean bTransient;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new <code>PropertyDefinition</code>.
	 * 
	 * @param pName the property name.
	 */
	public PropertyDefinition(String pName)
	{
		this(pName, null, null);
	}
	
	/**
	 * Constructs a new <code>PropertyDefinition</code> for a property name and 
	 * the property type.
	 * 
	 * @param pName the property name.
	 * @param pType the property type.
	 */
	public PropertyDefinition(String pName, IType<?> pType)
	{
		this(pName, pType, null);
	}
	
	/**
	 * Constructs a new <code>PropertyDefinition</code> for a property name and 
	 * the property type.
	 * 
	 * @param pName the property name.
	 * @param pType the property type.
	 * @param pTypeClass the type class.
	 */
	public PropertyDefinition(String pName, IType<?> pType, Class<?> pTypeClass)
	{
		if (pName == null)
		{
			throw new IllegalArgumentException("The name of a property definition may not be null!");
		}
		
		name = pName;
		
		if (pType == null)
		{
			type = IType.UNKNOWN_TYPE;
		}
		else
		{
			type = pType;
		}
		
		if (pTypeClass == null)
		{
			typeClass = type.getTypeClass();
		}
		else
		{
			typeClass = pTypeClass;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the property name.
	 * 
	 * @return the property name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the property type.
	 * 
	 * @return the property type.
	 */
	public IType<?> getType()
	{
		return type;
	}
	
	/**
	 * Gets the detailed property type class.
	 * 
	 * @return the detailed property type class.
	 */
	public Class<?> getTypeClass()
	{
		return typeClass;
	}
	
    /**
     * True, if the property is transient.
     * 
     * @return if the property is transient.
     */
    public boolean isTransient()
    {
        return bTransient;
    }
    
    /**
     * True, if the property is transient.
     * 
     * @param pTransient if the property is transient.
     */
    public void setTransient(boolean pTransient)
    {
        bTransient = pTransient;
    }
    
}	// PropertyDefinition
