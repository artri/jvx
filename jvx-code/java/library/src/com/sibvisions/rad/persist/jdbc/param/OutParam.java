/*
 * Copyright 2012 SIB Visions GmbH
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
 * 20.07.2012 - [JR] - creation
 * 13.02.2023 - [JR] - #3142 bean support
 */
package com.sibvisions.rad.persist.jdbc.param;

import java.sql.Types;

import javax.rad.type.bean.IBean;

/**
 * The <code>OutParam</code> is a simple output parameter for JDBC calls.
 * 
 * @author René Jahn
 */
public class OutParam extends AbstractParam
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>OutParam</code> for the given sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 */
	public OutParam(int pSqlType)
	{
		super(ParameterType.Out, pSqlType, null);
	}
	
	/**
	 * Creates a new instance of <code>OutParam</code> for the given sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	public OutParam(int pSqlType, IBean pBean, String pPropertyName)
	{
		super(ParameterType.Out, pSqlType, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>OutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type.
	 * @deprecated use {@link #newArrayParam(String)} instead
	 */
	@Deprecated
	public OutParam(String pArrayType)
	{
		super(ParameterType.Out, Types.ARRAY, pArrayType, null);
	}

    /**
     * Creates a new instance of <code>OutParam</code> for the given array type.
     * 
     * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
     * @param pStructType the array type 
     */
    protected OutParam(int pSqlType, String pStructType)
    {
        super(ParameterType.Out, pSqlType, pStructType, null);
    }

    /**
     * Creates a new instance of <code>OutParam</code> for the given array type.
     * 
     * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
     * @param pStructType the array type 
     * @param pBean the connected bean
     * @param pPropertyName the property name of connected bean
     */
    protected OutParam(int pSqlType, String pStructType, IBean pBean, String pPropertyName)
    {
        super(ParameterType.Out, pSqlType, pStructType, pBean, pPropertyName);
    }

	/**
	 * Creates a new instance of <code>OutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @return the new instance 
	 */
	public static OutParam newArrayParam(String pArrayType)
	{
		return new OutParam(Types.ARRAY, pArrayType);
	}

    /**
     * Creates a new instance of <code>OutParam</code> for the given array type.
     * 
     * @param pArrayType the array type
     * @param pBean the connected bean
     * @param pPropertyName the property name of connected bean
     * @return the new instance 
     */
    public static OutParam newArrayParam(String pArrayType, IBean pBean, String pPropertyName)
    {
        return new OutParam(Types.ARRAY, pArrayType, pBean, pPropertyName);
    }

	/**
	 * Creates a new instance of <code>OutParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @return the new instance
	 */
	public static OutParam newStructParam(String pStructType)
	{
		return new OutParam(Types.STRUCT, pStructType);
	}

    /**
     * Creates a new instance of <code>OutParam</code> for the given struct type.
     * 
     * @param pStructType the array type
     * @param pBean the connected bean
     * @param pPropertyName the property name of connected bean
     * @return the new instance
     */
    public static OutParam newStructParam(String pStructType, IBean pBean, String pPropertyName)
    {
        return new OutParam(Types.STRUCT, pStructType, pBean, pPropertyName);
    }

}	// OutParam
