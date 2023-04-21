/*
 * Copyright 2014 SIB Visions GmbH
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
 * 21.11.2014 - [RZ] - creation (mostly copied from AbstractStorage)
 */
package com.sibvisions.rad.persist.bean;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;
import javax.rad.type.bean.IBeanType;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link BeanConverter} class allows to convert between {@code Object[]}s,
 * {@link IBean}s and {@code POJO}s.
 * 
 * This class keeps three important pieces in sync:
 * <ol>
 * <li>The main {@link IBeanType} which is used for many operations.</li>
 * <li>An array of POJO property names, which are generated from the
 * {@link IBeanType}.</li>
 * <li>A map of property names which allows to remap names.</li>
 * </ol>
 * 
 * @author Robert Zenz
 */
public class BeanConverter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The logger. */
	private static final ILogger LOGGER = LoggerFactory.getInstance(BeanConverter.class);
	
	/** The {@link Class} of the {@link IBeans} which is being created. */
	private Class<? extends IBean> clsBean = Bean.class;

	/** The (cached) {@link Constructor} of the {@link #clsBean}. */
	private Constructor<? extends IBean> consBean = null;
	
	/** The IBeanType with all columns names from the metadata. */
	private IBeanType beanType;
	
	/** the mapping between column name and POJO property name. */
	private HashMap<String, String> hmpPropertyNames = null;
	
	/** the property names for POJOs in the same order as the meta data. */
	private String[] pojoPropertyNames;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link BeanConverter}.
	 * 
	 * Note that the created instance is still missing a {@link IBeanType}, so
	 * it needs to be set afterwards.
	 */
	public BeanConverter()
	{
	}
	
	/**
	 * Creates a new instance of {@link BeanConverter}.
	 * 
	 * @param pBeanType the {@link IBeanType} to use.
	 */
	public BeanConverter(IBeanType pBeanType)
	{
		setBeanType(pBeanType);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the {@link Class} of the {@link IBean} which is being created.
	 * 
	 * Note that the given {@link Class} must have a constructor which accepts
	 * an {@link IBeanType} as argument.
	 * 
	 * @param pClass the {@link Class} of the created {@link IBean}, can be
	 *               {@code null} for falling back to the default ({@link Bean}.
	 */
	public void setBeanClass(Class<? extends IBean> pClass)
	{
		clsBean = pClass;
		consBean = null;
		
		if (clsBean == null)
		{
			clsBean = Bean.class;
		}
	}	
	
 	/**
	 * Gets the current {@link Class} of the {@link IBean} which is being
	 * created.
	 *  
	 * @return the current {@link Class} of the {@link IBean} which is being
	 *         created.
	 */
	public Class<? extends IBean> getBeanClass()
	{
		return clsBean;
	}	
	
	/**
	 * Creates an array of values from a given object. The array has the same
	 * element count as the meta data column count.
	 * 
	 * @param pObject a POJO or bean
	 * @return an array containing the property values from the given object
	 */
	public Object[] createArray(Object pObject)
	{
		if (pObject == null)
		{
			return new Object[beanType.getPropertyCount()];
		}
		
		if (pObject instanceof Object[])
		{
			return (Object[])pObject;
		}
		
		IBean bnData;
		
		String[] sProps;
		
		if (pObject instanceof IBean)
		{
			bnData = (IBean)pObject;
			sProps = beanType.getPropertyNames();
		}
		else
		{
			bnData = new Bean(pObject);
			
			sProps = pojoPropertyNames;
		}
		
		//always ALL columns, not the columns contained in pObject
		Object[] oResult = new Object[sProps.length];
		
		for (int i = 0; i < sProps.length; i++)
		{
			try
			{
				oResult[i] = bnData.get(sProps[i]);
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
		
		return oResult;
	}
	
	/**
	 * Creates a bean with given values. The bean contains the property names
	 * from the column meta data and not more.
	 * 
	 * @param pValues the values in same order as the meta data
	 * @return a new bean
	 */
	public IBean createBean(Object[] pValues)
	{
		IBean bnRow = createEmptyBean();
		
		String[] sProps = beanType.getPropertyNames();
		
		for (int i = 0; i < sProps.length; i++)
		{
			if (pValues != null && i < pValues.length)
			{
				bnRow.put(sProps[i], pValues[i]);
			}
			else if (pValues == null)
			{
				bnRow.put(sProps[i], null);
			}
		}
		
		return bnRow;
	}
	
	/**
	 * Creates a bean from the given POJO. The bean contains the property names
	 * from the column meta data and not more.
	 * 
	 * @param pPOJO the POJO from which to create the bean.
	 * @return a new bean
	 */
	public IBean createBean(Object pPOJO)
	{
		IBean dbRow = createEmptyBean();
		
		IBean bnData = new Bean(pPOJO);
		
		String[] propertyNames = beanType.getPropertyNames();
		
		for (int i = 0; i < pojoPropertyNames.length; i++)
		{
			try
			{
				dbRow.put(propertyNames[i], bnData.get(pojoPropertyNames[i]));
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
		
		return dbRow;
	}
	
	/**
	 * Creates a new bean with all column names from the meta data. Only this
	 * column names are allowed.
	 * 
	 * @return a new instance of an {@link IBean} implementation
	 */
	public IBean createEmptyBean()
	{
		try
		{
			if (consBean == null)
			{
				consBean = clsBean.getConstructor(IBeanType.class);
			}
			
			return consBean.newInstance(beanType);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * Creates a POJO from the given type and with the values from a bean.
	 * 
	 * @param <T> the type of the POJO
	 * @param pClass the class of the POJO
	 * @param pBean the bean with values for the POJO
	 * @return the POJO
	 */
	public <T> T createPOJO(Class<T> pClass, IBean pBean)
	{
		Bean bean = new Bean(pClass);
		
		String[] sCols = beanType.getPropertyNames();
		
		for (int i = 0; i < sCols.length; i++)
		{
			try
			{
				bean.put(pojoPropertyNames[i], pBean.get(sCols[i]));
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
		
		return (T)bean.getObject();
	}
	
	/**
	 * Creates a POJO from the given type and with the values from a POJO.
	 * 
	 * @param <T> the type of the POJO
	 * @param pClass the class of the POJO
	 * @param pPOJO the POJO for the POJO
	 * @return the POJO
	 */
	public <T> T createPOJO(Class<T> pClass, Object pPOJO)
	{
		Bean bean = new Bean(pClass);
		Bean fromBean = new Bean(pPOJO);
		
		for (int i = 0; i < pojoPropertyNames.length; i++)
		{
			try
			{
				bean.put(pojoPropertyNames[i], fromBean.get(pojoPropertyNames[i]));
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
		
		return (T)bean.getObject();
	}
	
	/**
	 * Creates a POJO from the given type and with given values.
	 * 
	 * @param <T> the type of the POJO
	 * @param pClass the class of the POJO
	 * @param pValues the values for the properties in the same order as the
	 *            meta data
	 * @return the POJO
	 */
	public <T> T createPOJO(Class<T> pClass, Object[] pValues)
	{
		Bean bean = new Bean(pClass);
		
		for (int i = 0; i < pojoPropertyNames.length; i++)
		{
			try
			{
				bean.put(pojoPropertyNames[i], pValues[i]);
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
				
				// #959
				if (beanType.getPropertyIndex(pojoPropertyNames[i]) >= 0)
				{
					throw new IllegalArgumentException("Setting value for column [" + pojoPropertyNames[i] + "] failed!", e);
				}
			}
		}
		
		return (T)bean.getObject();
	}
	
	/**
	 * Gets the {@link IBeanType} used by this {@link BeanConverter}. Might
	 * return {@code null} if this hasn't been initialized by now.
	 * 
	 * @return the {@link IBeanType}. {@code null} if this hasn't been
	 *         initialized.
	 */
	public IBeanType getBeanType()
	{
		return beanType;
	}
	
	/**
	 * Returns the POJO property name at the given index.
	 * 
	 * @param pIndex the index of the property.
	 * @return the POJO property name.
	 */
	public String getPojoPropertyName(int pIndex)
	{
		return pojoPropertyNames[pIndex];
	}
	
	/**
	 * Gets the property name (Java standard) for the given column name.
	 * 
	 * @param pColumnName the column name e.g. FIRST_NAME
	 * @return the java property name e.g. firstName instead of FIRST_NAME
	 */
	public String getPropertyNameForColumn(String pColumnName)
	{
		if (hmpPropertyNames == null)
		{
			return null;
		}
		
		return hmpPropertyNames.get(pColumnName);
	}
	
	/**
	 * Returns {@code true} if this {@link BeanConverter} has been initialized.
	 * 
	 * @return {@code true} if this is initialized.
	 */
	public boolean isInitialized()
	{
		return beanType != null;
	}
	
	/**
	 * Removes the given column name.
	 * 
	 * @param pColumnName the column name to remove.
	 */
	public void removePropertyNameForColumn(String pColumnName)
	{
		if (hmpPropertyNames == null)
		{
			return;
		}
		
		hmpPropertyNames.remove(pColumnName);
	}
	
	/**
	 * Sets the given {@link IBeanType}.
	 * 
	 * @param pBeanType the {@link IBeanType} to use.
	 */
	public void setBeanType(IBeanType pBeanType)
	{
		beanType = pBeanType;
		
		String[] columnNames = beanType.getPropertyNames();
		
		//format the property names for POJOs
		pojoPropertyNames = new String[columnNames.length];
		
		String sPropName;
		
		for (int i = 0; i < pojoPropertyNames.length; i++)
		{
			if (hmpPropertyNames != null)
			{
				sPropName = hmpPropertyNames.get(columnNames[i]);
			}
			else
			{
				sPropName = null;
			}
			
			if (sPropName == null)
			{
				sPropName = StringUtil.convertToMemberName(columnNames[i]);
			}
			
			pojoPropertyNames[i] = sPropName;
		}
	}
	
	/**
	 * Sets the given POJO property name at the given index.
	 * 
	 * @param pIndex the index at which to set the name.
	 * @param pPropertyName the POJO property name.
	 */
	public void setPojoPropertyName(int pIndex, String pPropertyName)
	{
		pojoPropertyNames[pIndex] = pPropertyName;
	}
	
	/**
	 * Sets the property name (Java standard) for a given column name. The name
	 * will be used for synchronizing POJOs with beans.
	 * 
	 * @param pColumnName the column name
	 * @param pPropertyName the java property name e.g. firstName instead of
	 *            FIRST_NAME
	 */
	public void setPropertyNameForColumn(String pColumnName, String pPropertyName)
	{
		if (hmpPropertyNames == null)
		{
			hmpPropertyNames = new HashMap<String, String>();
		}
		
		hmpPropertyNames.put(pColumnName, pPropertyName);
	}
	
	/**
	 * Updates the array with the values from the POJO.
	 * 
	 * @param pArray the array.
	 * @param pPOJO the POJO.
	 */
	public void updateArray(Object[] pArray, Object pPOJO)
	{
		if (pArray == null || pArray.length == 0 || pPOJO == null)
		{
			return;
		}
		
		IBean bnData = null;
		String[] sColumnNames = null;
		
		if (pPOJO instanceof IBean)
		{
			bnData = (IBean)pPOJO;
			sColumnNames = beanType.getPropertyNames();
		}
		else
		{
			bnData = new Bean(pPOJO);
			sColumnNames = pojoPropertyNames;
		}
		
		for (int i = 0; i < Math.min(sColumnNames.length, pArray.length); i++)
		{
			pArray[i] = bnData.get(sColumnNames[i]);
		}
	}
	
	/**
	 * Updates a bean with values from a POJO. Only values from known properties
	 * will be updated. The property names from the meta data will be used.
	 * 
	 * @param pBean the bean
	 * @param pPOJO the POJO (or {@link IBean})
	 */
	public void updateBean(IBean pBean, Object pPOJO)
	{
		if (pBean == null || pPOJO == null)
		{
			return;
		}
		
		String[] sPropertyNames;
		String[] sCols = beanType.getPropertyNames();
		
		IBean bean;
		
		if (pPOJO instanceof IBean)
		{
			bean = (IBean)pPOJO;
			
			sPropertyNames = sCols;
		}
		else
		{
			bean = new Bean(pPOJO);
			
			sPropertyNames = pojoPropertyNames;
		}
		
		for (int i = 0; i < sCols.length; i++)
		{
			try
			{
				pBean.put(sCols[i], bean.get(sPropertyNames[i]));
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
	}
	
	/**
	 * Updates a bean with values from an array. Only values from known
	 * properties will be updated. The property names from the meta data will be
	 * used.
	 * 
	 * @param pBean the bean.
	 * @param pValues the array.
	 */
	public void updateBean(IBean pBean, Object[] pValues)
	{
		if (pBean == null || pValues == null || pValues.length == 0)
		{
			return;
		}
		
		String[] sProps = beanType.getPropertyNames();
		
		for (int i = 0; i < pValues.length; i++)
		{
			try
			{
				pBean.put(sProps[i], pValues[i]);
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
	}
	
	/**
	 * Updates a POJO with values from a POJO. Only values from known properties
	 * will be updated. The property names from the meta data will be used.
	 * 
	 * @param pPOJO the POJO to update.
	 * @param pNewPOJO the new POJO.
	 */
	public void updatePOJO(Object pPOJO, Object pNewPOJO)
	{
		if (pPOJO == null || pNewPOJO == null)
		{
			return;
		}
		
		IBean bean = new Bean(pPOJO);
		IBean newBean = null;
		String[] propertyNames = null;
		
		if (pNewPOJO instanceof IBean)
		{
			newBean = (IBean)pNewPOJO;
			propertyNames = beanType.getPropertyNames();
		}
		else
		{
			newBean = new Bean(pNewPOJO);
			propertyNames = pojoPropertyNames;
		}
		
		for (int i = 0; i < pojoPropertyNames.length; i++)
		{
			try
			{
				bean.put(pojoPropertyNames[i], newBean.get(propertyNames[i]));
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
	}
	
	/**
	 * Updates a POJO with values from an array. Only values from known
	 * properties will be updated. The property names from the meta data will be
	 * used.
	 * 
	 * @param pPOJO the POJO to update.
	 * @param pValues the array.
	 */
	public void updatePOJO(Object pPOJO, Object[] pValues)
	{
		if (pPOJO == null || pValues == null || pValues.length == 0)
		{
			return;
		}
		
		IBean bean = new Bean(pPOJO);
		
		for (int i = 0; i < pValues.length; i++)
		{
			try
			{
				bean.put(pojoPropertyNames[i], pValues[i]);
			}
			catch (Exception e)
			{
				LOGGER.debug(e);
			}
		}
	}
	
}	// BeanConverter
