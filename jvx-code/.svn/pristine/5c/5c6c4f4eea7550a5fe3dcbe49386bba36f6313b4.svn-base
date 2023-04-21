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
 * The <code>InOutParam</code> defines an input and output parameter for JDBC calls.
 * 
 * @author René Jahn
 */
public class InOutParam extends AbstractParam
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 */
	public InOutParam(int pSqlType)
	{
		super(ParameterType.InOut, pSqlType, null);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given value and sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pValue the input value
	 */
	public InOutParam(int pSqlType, Object pValue)
	{
		super(ParameterType.InOut, pSqlType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InOutParam</code> for the given value and sql type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	public InOutParam(int pSqlType, IBean pBean, String pPropertyName)
	{
		super(ParameterType.InOut, pSqlType, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @deprecated use {@link #newArrayParam(String)} instead
	 */
	@Deprecated
	public InOutParam(String pArrayType)
	{
		super(ParameterType.InOut, Types.ARRAY, pArrayType, null);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given value and array type.
	 * 
	 * @param pArrayType the array type
	 * @param pValue the input value
	 * @deprecated use {@link #newArrayParam(String, Object)} instead.
	 */
	@Deprecated
	public InOutParam(String pArrayType, Object pValue)
	{
		super(ParameterType.InOut, Types.ARRAY, pArrayType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InOutParam</code> for the given value and array type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pStructType the array type
	 * @param pValue the input value
	 */
	protected InOutParam(int pSqlType, String pStructType, Object pValue)
	{
		super(ParameterType.InOut, pSqlType, pStructType, pValue);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given value and array type.
	 * 
	 * @param pSqlType the sql type. All {@link java.sql.Types} are supported.
	 * @param pStructType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	protected InOutParam(int pSqlType, String pStructType, IBean pBean, String pPropertyName)
	{
		super(ParameterType.InOut, pSqlType, pStructType, pBean, pPropertyName);
	}
	
	/**
	 * Creates a new instance of <code>InOutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @return the new instance
	 */
	public static InOutParam newArrayParam(String pArrayType)
	{
		return new InOutParam(Types.ARRAY, pArrayType, null);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @param pValue the input value
	 * @return the OutParam. 
	 */
	public static InOutParam newArrayParam(String pArrayType, Object pValue)
	{
		return new InOutParam(Types.ARRAY, pArrayType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InOutParam</code> for the given array type.
	 * 
	 * @param pArrayType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 * @return the new instance 
	 */
	public static InOutParam newArrayParam(String pArrayType, IBean pBean, String pPropertyName)
	{
		return new InOutParam(Types.ARRAY, pArrayType, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @return the new instance
	 */
	public static InOutParam newStructParam(String pStructType)
	{
		return new InOutParam(Types.STRUCT, pStructType, null);
	}

	/**
	 * Creates a new instance of <code>InOutParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @param pValue the input value
	 * @return the new instance
	 */
	public static InOutParam newStructParam(String pStructType, Object pValue)
	{
		return new InOutParam(Types.STRUCT, pStructType, pValue);
	}
	
	/**
	 * Creates a new instance of <code>InOutParam</code> for the given struct type.
	 * 
	 * @param pStructType the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 * @return the new instance 
	 */
	public static InOutParam newStructParam(String pStructType, IBean pBean, String pPropertyName)
	{
		return new InOutParam(Types.STRUCT, pStructType, pBean, pPropertyName);
	}

}	// InOutParam
