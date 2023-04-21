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
 * The <code>InParam</code> defines a simple input parameter. The value is not changed due to a JDBC call.
 * Only manual value updates are possible.
 * 
 * @author René Jahn
 */
public class InParam extends AbstractParam
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>InParam</code> with the given value.
	 * 
	 * @param pValue the input value
	 */
	public InParam(Object pValue)
	{
		super(ParameterType.In, SQLTYPE_AUTOMATIC, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InParam</code> for the given sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 */
	public InParam(int pSqlType)
	{
		super(ParameterType.In, pSqlType, null);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given value and sql type.
	 * 
	 * @param pValue the input value
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 */
	public InParam(int pSqlType, Object pValue)
	{
		super(ParameterType.In, pSqlType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InParam</code> for given property of bean.
	 * 
	 * @param pBean the bean
	 * @param pPropertyName the property name
	 */
	public InParam(IBean pBean, String pPropertyName)
	{
		super(ParameterType.In, SQLTYPE_AUTOMATIC, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for given data type and property of bean.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pBean the bean
	 * @param pPropertyName the property name
	 */
	public InParam(int pSqlType, IBean pBean, String pPropertyName)
	{
		super(ParameterType.In, pSqlType, pBean, pPropertyName);
	}	
	
	/**
	 * Creates a new instance of <code>InParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @deprecated use {@link #newArrayParam(String)} instead
	 */
	@Deprecated
	public InParam(String pArrayType)
	{
		super(ParameterType.In, Types.ARRAY, pArrayType, null);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given value and array type.
	 * 
	 * @param pValue the input value
	 * @param pArrayType the array type
	 * @deprecated use {@link #newArrayParam(String, Object)} instead 
	 */
	@Deprecated
	public InParam(String pArrayType, Object pValue)
	{
		super(ParameterType.In, Types.ARRAY, pArrayType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InParam</code> for the given value and array type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pStructType the array type
	 * @param pValue the input value
	 */
	protected InParam(int pSqlType, String pStructType, Object pValue)
	{
		super(ParameterType.In, pSqlType, pStructType, pValue);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given value and array type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pStructType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	protected InParam(int pSqlType, String pStructType, IBean pBean, String pPropertyName)
	{
		super(ParameterType.In, pSqlType, pStructType, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @return the new instance 
	 */
	public static InParam newArrayParam(String pArrayType)
	{
		return new InParam(Types.ARRAY, pArrayType, null);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @param pValue the input value
	 * @return the new instance.
	 */
	public static InParam newArrayParam(String pArrayType, Object pValue)
	{
		return new InParam(Types.ARRAY, pArrayType, pValue);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 * @return the new instance.
	 */
	public static InParam newArrayParam(String pArrayType, IBean pBean, String pPropertyName)
	{
		return new InParam(Types.ARRAY, pArrayType, pBean, pPropertyName);
	}
	
	/**
	 * Creates a new instance of <code>InParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @return the new instance 
	 */
	public static InParam newStructParam(String pStructType)
	{
		return new InParam(Types.STRUCT, pStructType, null);
	}

	/**
	 * Creates a new instance of <code>InParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @param pValue the input value
	 * @return the new instance 
	 */
	public static InParam newStructParam(String pStructType, Object pValue)
	{
		return new InParam(Types.STRUCT, pStructType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 * @return the new instance 
	 */
	public static InParam newStructParam(String pStructType, IBean pBean, String pPropertyName)
	{
		return new InParam(Types.STRUCT, pStructType, pBean, pPropertyName);
	}
	
}	// InParam
