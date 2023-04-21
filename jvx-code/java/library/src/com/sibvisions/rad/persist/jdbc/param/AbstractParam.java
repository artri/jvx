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

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractParam</code> defines a parameter for JDBC calls.
 * 
 * @author René Jahn
 */
public abstract class AbstractParam
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Delegate the type decision to the JDBC driver. */
	public static final int SQLTYPE_AUTOMATIC = Integer.MAX_VALUE;

	/** VARCHAR SQL Type {@link Types#VARCHAR}. */
	public static final int SQLTYPE_VARCHAR = Types.VARCHAR;
	
	/** VARCHAR SQL Type {@link Types#DECIMAL}. */
	public static final int SQLTYPE_DECIMAL = Types.DECIMAL;
	
	/** VARCHAR SQL Type {@link Types#TIMESTAMP}. */
	public static final int SQLTYPE_TIMESTAMP = Types.TIMESTAMP;
	
	
	/** The possible parameter types. */
	public enum ParameterType
	{
		/** Input parameter. */
		In,
		/** Output parameter. */
		Out,
		/** Input and Output parameter. */
		InOut
	}
	
    /** the parameter type. */
    private ParameterType type;
    
    /** the SQL type. */
    private int iSqlType;
    
    /** the SQL type name. */
    private String sTypeName;
    
	/** the value. */
	private Object oValue;

	/** the connected bean. */
	private IBean bean;

	/** the property name of connected bean. */
	private String sPropertyName;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AbstractParam</code>.
	 * 
	 * @param pType the parameter type
	 * @param pSqlType the SQL type
	 * @param pValue the value
	 */
	protected AbstractParam(ParameterType pType, int pSqlType, Object pValue)
	{
		this(pType, pSqlType, null, pValue);
	}

	/**
	 * Creates a new instance of <code>AbstractParam</code>.
	 * 
	 * @param pType the parameter type
	 * @param pSqlType the SQL type
	 * @param pTypeName the array type
	 * @param pValue the value
	 */
	protected AbstractParam(ParameterType pType, int pSqlType, String pTypeName, Object pValue)
	{
		type = pType;
		iSqlType = pSqlType;
		sTypeName = pTypeName;
		oValue = pValue;
	}
	
	/**
	 * Creates a new instance of <code>AbstractParam</code>.
	 * 
	 * @param pType the parameter type
	 * @param pSqlType the SQL type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	protected AbstractParam(ParameterType pType, int pSqlType, IBean pBean, String pPropertyName)
	{
		this(pType, pSqlType, null, pBean, pPropertyName);
	}

	/**
	 * Creates a new instance of <code>AbstractParam</code>.
	 * 
	 * @param pType the parameter type
	 * @param pSqlType the SQL type
	 * @param pTypeName the array type
	 * @param pBean the connected bean
	 * @param pPropertyName the property name of connected bean
	 */
	protected AbstractParam(ParameterType pType, int pSqlType, String pTypeName, IBean pBean, String pPropertyName)
	{
		type = pType;
		iSqlType = pSqlType;
		sTypeName = pTypeName;

		bean = pBean;
		sPropertyName = pPropertyName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
	    StringBuilder sb = new StringBuilder();
	    sb.append(StringUtil.toString(oValue));
	    sb.append(" (");
	    sb.append(type);
	    
	    if (sTypeName != null)
	    {
	        sb.append(", ");
	        sb.append(sTypeName);
	    }
	    
	    sb.append(")");
	    
	    return sb.toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the parameter type.
	 * 
	 * @return the type
	 */
	public ParameterType getType()
	{
		return type;
	}
	
	/**
	 * Gets the SQL type.
	 * 
	 * @return the SQL Type
	 * @see java.sql.Types
	 */
	public int getSqlType()
	{
		if (oValue == null && iSqlType == SQLTYPE_AUTOMATIC)
		{
			//no specific SQL Type and the null as value -> try VARCHAR
			return Types.VARCHAR;
		}
		else
		{
			return iSqlType;
		}
	}
	
	/**
	 * Gets the SQL type name.
	 * 
	 * @return the SQL type name
	 * @see java.sql.Types
	 * @deprecated use {@link #getTypeName()} instead. 
	 */
	@Deprecated
	public String getArrayType()
	{
		return sTypeName;
	}
	
	/**
	 * Gets the SQL type name.
	 * 
	 * @return the SQL type name
	 * @see java.sql.Types
	 */
	public String getTypeName()
	{
		return sTypeName;
	}
	
	/**
	 * Gets the current value.
	 * 
	 * @return the value
	 */
	public Object getValue()
	{
	    if (bean != null && sPropertyName != null)
	    {
	        return bean.get(sPropertyName);
	    }
	    else
	    {
	        return oValue;
	    }
	}

	/**
	 * Sets the current value.
	 * 
	 * @param pValue the value
	 */
	public void setValue(Object pValue)
	{
		if (bean != null && sPropertyName != null)
		{
			bean.put(sPropertyName, pValue);
		}
		else
		{
		    oValue = pValue;
		}
	}

    /**
     * Gets, whether a type is an input parameter.
     *  
     * @param pType the type
     * @return true, if a parameter is an input parameter.
     */
    public static boolean isInParam(ParameterType pType)
    {
        return pType == ParameterType.In || pType == ParameterType.InOut;
    }
    
	/**
	 * Gets, whether a parameter is an input parameter.
	 *  
	 * @param pParam the parameter
	 * @return true, if a parameter is an input parameter.
	 */
	public static boolean isInParam(Object pParam)
	{
	    if (pParam instanceof AbstractParam)
	    {
	        return isInParam(((AbstractParam)pParam).getType());
	    }
	    else
	    {
	        return true;
	    }
	}
	
    /**
     * Gets, whether a type is an output parameter.
     *  
     * @param pType the type
     * @return true, if a parameter is an output parameter.
     */
    public static boolean isOutParam(ParameterType pType)
    {
        return pType == ParameterType.Out || pType == ParameterType.InOut;
    }
    
    /**
     * Gets, whether a parameter is an output parameter.
     *  
     * @param pParam the parameter
     * @return true, if a parameter is an output parameter.
     */
    public static boolean isOutParam(Object pParam)
    {
        if (pParam instanceof AbstractParam)
        {
            return isOutParam(((AbstractParam)pParam).getType());
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Gets the value, if it is either an AbstractParam, or directly a value.
     *  
     * @param pParam the parameter
     * @return the value, if it is either an AbstractParam, or directly a value.
     */
    public static Object getValue(Object pParam)
    {
        if (pParam instanceof AbstractParam)
        {
            return ((AbstractParam)pParam).getValue();
        }
        else
        {
            return pParam;
        }
    }
    
}	// AbstractParam
