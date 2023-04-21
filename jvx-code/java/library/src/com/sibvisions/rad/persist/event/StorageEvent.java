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
 * 04.01.2011 - [RH] - creation
 * 06.01.2011 - [JR] - forward POJO handling to AbstractStorage because of name conversions
 * 07.01.2011 - [JR] - isNewModified, isOldModified implemented
 */
package com.sibvisions.rad.persist.event;

import javax.rad.persist.IStorage;
import javax.rad.type.bean.IBean;

import com.sibvisions.rad.persist.bean.BeanConverter;

/**
 * The <code>StorageEvent</code> contains information about changes in an
 * {@link IStorage}.
 * 
 * @author Roland Hörmann
 */
public class StorageEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * The type of the row.
     */
    private enum RowType
    {
        /** Array. */
        ARRAY,
        /** Bean. */
        BEAN,
        /** Object. */
        OBJECT,
        /** Original Object. */
        ORIGINAL_OBJECT
        
    }   // RowType
    /**
	 * Specifies the type of change.
	 */
	public enum ChangedType
	{
		/**
		 * Calculate row. This is called after fetch, refetchRow, insert and
		 * update method calls.
		 */
		CALCULATE_ROW,
		
		/** instead-of insert. */
		INSTEADOF_INSERT,
		/** before insert. */
		BEFORE_INSERT,
		/** after insert. */
		AFTER_INSERT,
		
        /** instead-of update. */
        INSTEADOF_UPDATE,
		/** before update. */
		BEFORE_UPDATE,
		/** after update. */
		AFTER_UPDATE,
		
        /** instead-of delete. */
        INSTEADOF_DELETE,
		/** before delete. */
		BEFORE_DELETE,
		/** after delete. */
		AFTER_DELETE,
	}
	
	/** The {@link BeanConverter} used by this event. */
	private BeanConverter beanConverter;
	
	/** The changed type. */
	private ChangedType type;
	
	/** The changed storage. */
	private IStorage storage;
	
	/** The old row as object array. */
	private Object[] oldAsArray;
	
	/** The old row as bean. */
	private IBean oldAsBean;
	
	/** The old row as object. */
	private Object oldAsObject;
	
	/** The old row's original object. */
	private Object oldOriginalObject;
	
	/** The new row as object array. */
	private Object[] newAsArray;
	
	/** The new row as bean. */
	private IBean newAsBean;
	
	/** The new row as object. */
	private Object newAsObject;
	
	/** The new row's original object. */
	private Object newOriginalObject;
	
	/** The type of the last touched old row. */
	private RowType dirtyTypeOld;
	
	/** The type of the last touched new row. */
	private RowType dirtyTypeNew;
	
	/** whether the new object is modified. */
	private boolean bOldModified = false;
	
	/** whether the new object is modified. */
	private boolean bNewModified = false;
	
	/** whether the event is a bean based event. */
	private boolean bBeanMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link StorageEvent}.
	 * 
	 * @param pStorage the storage.
	 * @param pBeanConverter the bean converter.
	 * @param pType the type.
	 * @param pOld the old.
	 * @param pNew the new.
	 */
	public StorageEvent(IStorage pStorage, BeanConverter pBeanConverter, ChangedType pType, Object[] pOld, Object[] pNew)
	{
		this(pStorage, pBeanConverter, pType);
		
		oldAsArray = pOld;
		newAsArray = pNew;
		
		bBeanMode = false;
	}
	
	/**
	 * Constructs the DataRowEvent.
	 * 
	 * @param pStorage the changed storage.
	 * @param pBeanConverter the bean converter to use.
	 * @param pType the type of change.
	 * @param pOld the old bean or <code>null</code> if old bean is not allowed
	 * @param pNew the new bean or <code>null</code> if new bean is not allowed
	 */
	public StorageEvent(IStorage pStorage, BeanConverter pBeanConverter, ChangedType pType, IBean pOld, IBean pNew)
	{
		this(pStorage, pBeanConverter, pType);
		
		oldAsBean = pOld;
		newAsBean = pNew;
		
		bBeanMode = true;
	}
	
	/**
	 * Creates a new instance of {@link StorageEvent}.
	 * 
	 * @param pStorage the storage.
	 * @param pBeanConverter the bean converter.
	 * @param pType the type.
	 * @param pOld the old.
	 * @param pNew the new.
	 */
	public StorageEvent(IStorage pStorage, BeanConverter pBeanConverter, ChangedType pType, Object pOld, Object pNew)
	{
		this(pStorage, pBeanConverter, pType);
		
		oldOriginalObject = pOld;
		newOriginalObject = pNew;
		
		bBeanMode = true;
	}
	
	/**
	 * Creates a new instance of {@link StorageEvent}.
	 * 
	 * @param pStorage the storage.
	 * @param pBeanConverter the bean converter.
	 * @param pType the type.
	 */
	private StorageEvent(IStorage pStorage, BeanConverter pBeanConverter, ChangedType pType)
	{
		storage = pStorage;
		beanConverter = pBeanConverter;
		type = pType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the IStorage that is changed.
	 * 
	 * @return the IStorage that is changed.
	 */
	public IStorage getStorage()
	{
		return storage;
	}
	
	/**
	 * Gets the type of change.
	 * 
	 * @return the type of change.
	 */
	public ChangedType getType()
	{
		return type;
	}
	
	/**
	 * Sets the old bean properties.
	 * 
	 * @param pOld the current old bean.
	 * @deprecated With 2.1 onwards it is not necessary anymore to set the old
	 *             row to propagate changes.
	 */
	@Deprecated
	public void setOld(Object pOld)
	{
		// Deprecated
		bOldModified = true;
	}
	
	/**
	 * Gets the old IBean.
	 * 
	 * @return the old IBean.
	 */
	public IBean getOld()
	{
		if (dirtyTypeOld != null && dirtyTypeOld != RowType.BEAN)
		{
			update(dirtyTypeOld, oldAsArray, oldAsBean, oldAsObject, oldOriginalObject);
		}
		
        //possible, if event was created with an array
		if (oldAsBean == null)
		{
			oldAsBean = createBean(oldAsArray, oldOriginalObject);
		}
		
		dirtyTypeOld = RowType.BEAN;
		
		return oldAsBean;
	}
	
	/**
	 * Gets the old POJO from type &lt;T&gt;.
	 * 
	 * @param <T> the type of the POJO.
	 * @param pClass the class of the POJO.
	 * @return the new POJO from type &lt;T&gt;.
	 */
	public <T> T getOld(Class<T> pClass)
	{
		if (oldOriginalObject != null && pClass.isAssignableFrom(oldOriginalObject.getClass()))
		{
			if (dirtyTypeOld != null && dirtyTypeOld != RowType.ORIGINAL_OBJECT)
			{
				update(dirtyTypeOld, oldAsArray, oldAsBean, oldAsObject, oldOriginalObject);
			}
			
			dirtyTypeOld = RowType.ORIGINAL_OBJECT;
			
			return (T) oldOriginalObject;
		}
		else
		{
			if (dirtyTypeOld != null && dirtyTypeOld != RowType.OBJECT)
			{
				update(dirtyTypeOld, oldAsArray, oldAsBean, oldAsObject, oldOriginalObject);
			}
			
			if (oldAsObject == null)
			{
				oldAsObject = createPOJO(pClass, oldAsArray, oldAsBean, oldOriginalObject);
			}
			else if (!pClass.isAssignableFrom(oldAsObject.getClass()))
			{
				update(dirtyTypeOld, oldAsArray, oldAsBean, oldAsObject, oldOriginalObject);
				oldAsObject = createPOJO(pClass, oldAsArray, oldAsBean, oldOriginalObject);
			}
			
			dirtyTypeOld = RowType.OBJECT;
			
			return (T) oldAsObject;
		}
	}
	
	/**
	 * Gets the old row as object array.
	 * 
	 * @return the old row as object array.
	 */
	public Object[] getOldAsArray()
	{
		if (dirtyTypeOld != null && dirtyTypeOld != RowType.ARRAY)
		{
			update(dirtyTypeOld, oldAsArray, oldAsBean, oldAsObject, oldOriginalObject);
		}
		
        //possible, if event was created with bean/POJO
		if (oldAsArray == null)
		{
			oldAsArray = createArray(oldAsBean, oldOriginalObject);
		}
		
		dirtyTypeOld = RowType.ARRAY;
		
		return oldAsArray;
	}
	
	/**
	 * Gets the new IBean.
	 * 
	 * @return the new IBean.
	 */
	public IBean getNew()
	{
		if (dirtyTypeNew != null && dirtyTypeNew != RowType.BEAN)
		{
			update(dirtyTypeNew, newAsArray, newAsBean, newAsObject, newOriginalObject);
		}
		
        //possible, if event was created with an array
		if (newAsBean == null)
		{
			newAsBean = createBean(newAsArray, newOriginalObject);
		}
		
		dirtyTypeNew = RowType.BEAN;
		
		return newAsBean;
	}
	
	/**
	 * Gets the new POJO from type &lt;T&gt;.
	 * 
	 * @param <T> the type of the POJO.
	 * @param pClass the class of the POJO.
	 * @return the new POJO from type &lt;T&gt;.
	 */
	public <T> T getNew(Class<T> pClass)
	{
		if (newOriginalObject != null && pClass.isAssignableFrom(newOriginalObject.getClass()))
		{
			if (dirtyTypeNew != null && dirtyTypeNew != RowType.ORIGINAL_OBJECT)
			{
				update(dirtyTypeNew, newAsArray, newAsBean, newAsObject, newOriginalObject);
			}
			
			dirtyTypeNew = RowType.ORIGINAL_OBJECT;
			
			return (T) newOriginalObject;
		}
		else
		{
			if (dirtyTypeNew != null && dirtyTypeNew != RowType.OBJECT)
			{
				update(dirtyTypeNew, newAsArray, newAsBean, newAsObject, newOriginalObject);
			}
			
			if (newAsObject == null)
			{
				newAsObject = createPOJO(pClass, newAsArray, newAsBean, newOriginalObject);
			}
			else if (!pClass.isAssignableFrom(newAsObject.getClass()))
			{
				update(dirtyTypeNew, newAsArray, newAsBean, newAsObject, newOriginalObject);
				newAsObject = createPOJO(pClass, newAsArray, newAsBean, newOriginalObject);
			}
			
			dirtyTypeNew = RowType.OBJECT;
			
			return (T) newAsObject;
		}
	}
	
	/**
	 * Gets the new row as object array.
	 * 
	 * @return the new row as object array.
	 */
	public Object[] getNewAsArray()
	{
		if (dirtyTypeNew != null && dirtyTypeNew != RowType.ARRAY)
		{
			update(dirtyTypeNew, newAsArray, newAsBean, newAsObject, newOriginalObject);
		}
		
		//possible, if event was created with bean/POJO
		if (newAsArray == null)
		{
			newAsArray = createArray(newAsBean, newOriginalObject);
		}
		
		dirtyTypeNew = RowType.ARRAY;
		
		return newAsArray;
	}
	
	/**
	 * Sets the new bean properties.
	 * 
	 * @param pNew the current new bean.
	 * @deprecated With 2.1 onwards it is not necessary anymore to set the new
	 *             row to propagate changes.
	 */
	@Deprecated
	public void setNew(Object pNew)
	{
		// Deprecated
		bNewModified = true;
	}
	
	/**
	 * Gets whether the new object was modified after creation.
	 * 
	 * @return <code>true</code> if the new object was modified,
	 *         <code>false</code> otherwise
	 * @deprecated With 2.1 onwards.
	 */
	@Deprecated
	public boolean isNewModified()
	{
		// Deprecated
		return bNewModified;
	}
	
	/**
	 * Gets whether the old object was modified after creation.
	 * 
	 * @return <code>true</code> if the old object was modified,
	 *         <code>false</code> otherwise
	 * @deprecated With 2.1 onwards.
	 */
	@Deprecated
	public boolean isOldModified()
	{
		// Deprecated
		return bOldModified;
	}
	
	/**
	 * Creates an array from either the given bean or the given POJO.
	 * 
	 * @param pBean the bean.
	 * @param pObject the POJO.
	 * @return the array.
	 */
	private Object[] createArray(IBean pBean, Object pObject)
	{
		if (pBean != null)
		{
			return beanConverter.createArray(pBean);
		}
		else if (pObject != null)
		{
			return beanConverter.createArray(pObject);
		}
		
		return null;
	}
	
	/**
	 * Creates a bean from either the given array or the given POJO.
	 * 
	 * @param pArray the array.
	 * @param pObject the POJO.
	 * @return the bean.
	 */
	private IBean createBean(Object[] pArray, Object pObject)
	{
		if (pArray != null)
		{
			return beanConverter.createBean(pArray);
		}
		else if (pObject != null)
		{
			return beanConverter.createBean(pObject);
		}
		
		return null;
	}
	
	/**
	 * Creates a POJO from either the given array or the given bean.
	 * 
	 * @param <T> the type of the POJO.
	 * @param pClass the class to use.
	 * @param pArray the array.
	 * @param pBean the bean.
	 * @param pObject the object.
	 * @return the POJO.
	 */
	private <T> T createPOJO(Class<T> pClass, Object[] pArray, IBean pBean, Object pObject)
	{
		if (pBean != null)
		{
			return beanConverter.createPOJO(pClass, pBean);
		}
		else if (pArray != null)
		{
			return beanConverter.createPOJO(pClass, pArray);
		}
		else if (pObject != null)
		{
			return beanConverter.createPOJO(pClass, pObject);
		}
		
		return null;
	}
	
	/**
	 * Updates the already existing instances with values from the given type.
	 * 
	 * @param pDirtyType the type that is dirty and from which the changes will
	 *                   propagate.
	 * @param pArray the array.
	 * @param pBean the bean.
	 * @param pObject the object.
	 * @param pOriginalObject the original object.
	 */
	private void update(RowType pDirtyType, Object[] pArray, IBean pBean, Object pObject, Object pOriginalObject)
	{
		if (pDirtyType == RowType.ARRAY && pArray != null)
		{
			if (pBean != null)
			{
				beanConverter.updateBean(pBean, pArray);
			}
			
			if (pObject != null)
			{
				beanConverter.updatePOJO(pObject, pArray);
			}
			
			if (pOriginalObject != null)
			{
				beanConverter.updatePOJO(pOriginalObject, pArray);
			}
		}
		else if (pDirtyType == RowType.BEAN && pBean != null)
		{
			if (pArray != null)
			{
				beanConverter.updateArray(pArray, pBean);
			}
			
			if (pObject != null)
			{
				beanConverter.updatePOJO(pObject, pBean);
			}
			
			if (pOriginalObject != null)
			{
				beanConverter.updatePOJO(pOriginalObject, pBean);
			}
		}
		else if (pDirtyType == RowType.OBJECT && pObject != null)
		{
			if (pArray != null)
			{
				beanConverter.updateArray(pArray, pObject);
			}
			
			if (pBean != null)
			{
				beanConverter.updateBean(pBean, pObject);
			}
			
			if (pOriginalObject != null)
			{
				beanConverter.updatePOJO(pOriginalObject, pObject);
			}
		}
		else if (pDirtyType == RowType.ORIGINAL_OBJECT && pOriginalObject != null)
		{
			if (pArray != null)
			{
				beanConverter.updateArray(pArray, pOriginalObject);
			}
			
			if (pBean != null)
			{
				beanConverter.updateBean(pBean, pOriginalObject);
			}
			
			if (pObject != null)
			{
				beanConverter.updatePOJO(pObject, pOriginalObject);
			}
		}
	}
	
	/**
	 * Gets whether this event is based on a bean/object.
	 * 
	 * @return <code>true</code> if this event was created with a bean or POJO, <code>false</code> if
	 *         it was created with an array of objects
	 */
	public boolean isBeanMode()
	{
	    return bBeanMode;
	}
	
}	// StorageEvent
