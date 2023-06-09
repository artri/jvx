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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The <code>AbstractBean</code> is an {@link AbstractMap} and the default {@link IBean}
 * implementation.
 * 
 * @param <C> the bean class.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractBean<C extends IBeanType> extends AbstractMap<String, Object> 
                                                        implements IBean
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Stores the bean type. */
    protected C beanType;
    
    /** The entry set for this bean. */
    private transient EntrySet entrySet;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Special constructor for allowing to create dynamic bean types depending on own class.
     * It should be used carefully, the caller has to ensure, that the bean type is set!
     */
    protected AbstractBean()
    {
    }
    
    /**
     * Creates a <code>AbstractBean</code> with a specific {@link IBeanType}.
     * 
     * @param pBeanType the bean type.
     */
    protected AbstractBean(C pBeanType)
    {
        if (pBeanType == null)
        {
            throw new IllegalArgumentException("The bean type may not be null!");
        }
        
        beanType = pBeanType;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the value of the property index.
     * 
     * @param pPropertyIndex the property index.
     * @return the value of the property index or <code>null</code> if the property was not found.
     */
    public abstract Object get(int pPropertyIndex);
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public C getBeanType()
    {
        return beanType;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object get(String pPropertyName)
    {
        return get(beanType.getPropertyIndex(pPropertyName));
    }

    /**
     * {@inheritDoc}
     */
    public Object put(String pPropertyName, Object pValue)
    {
        Object oldValue;
        int index = beanType.getPropertyIndex(pPropertyName);
        if (index < 0)
        {
            if (beanType instanceof AbstractBeanType)
            {
                index = beanType.getPropertyCount();

                ((AbstractBeanType)beanType).addPropertyDefinition(pPropertyName);
                
                oldValue = null;
            }
            else
            {
                throw new IllegalArgumentException("The property [" + pPropertyName + "] does not exist!");
            }
        }
        else
        {
            oldValue = get(index);
        }
        
        put(index, pValue);

        return oldValue;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractBean clone()
    {
        try
        {
            AbstractBean clone = (AbstractBean)super.clone();
            
            // The entrySet needs to be removed so that it is regenerated.
            // Otherwise everything that uses the entrySet will be incorrect,
            // for example the toString() method, see #1502.
            clone.entrySet = null;
            
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            // Shouldn't happen.
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet()
    {
        if (entrySet == null)
        {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the value of a property. The property is identified by the index from the property
     * list.
     * 
     * @param pIndex the property index
     * @param pValue the value of the property
     */
    public void put(int pIndex, Object pValue) 
    {
        throw new UnsupportedOperationException();
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * Fast {@link AbstractSet}.
     *  
     * @author Martin Handsteiner
     */
    private class EntrySet extends AbstractSet<Map.Entry<String, Object>>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Abstract methods implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<Map.Entry<String, Object>> iterator()
        {
            return new EntryIterator();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size()
        {
            return beanType.getPropertyCount();
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean contains(Object pObject) 
        {
            if (pObject instanceof Map.Entry)
            {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>)pObject;
                
                int index = beanType.getPropertyIndex(entry.getKey());
                
                if (index >= 0)
                {
                    Object v1 = get(index);
                    Object v2 = entry.getValue();

                    return v1 == v2 || (v1 != null && v1.equals(v2));
                }
            }
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean remove(Object pObject) 
        {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void clear() 
        {
            throw new UnsupportedOperationException();
        }
        
    }   // EntrySet
    
    /**
     * Very fast {@link Iterator}.
     *  
     * @author Martin Handsteiner
     */
    private class EntryIterator implements Iterator<Map.Entry<String, Object>>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class Members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** The property names. */
        private String[] propertyNames = beanType.getPropertyNames();
        /** The index of the iterator. */
        private int index = 0;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface Implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            return index < propertyNames.length;
        }

        /**
         * {@inheritDoc}
         */
        public Map.Entry<String, Object> next()
        {
            return new Entry(index++);
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Map Entry {@link Map.Entry}.
         *  
         * @author Martin Handsteiner
         */
        private class Entry implements Map.Entry<String, Object>
        {
            /** The current index. */
            private int currentIndex;
            /** The current propertyName. */
            private String propertyName;
            /** The current propertyName. */
            private Object value = this;

            /**
             * constructs a new entry.
             * 
             * @param pCurrentIndex the current index
             */
            public Entry(int pCurrentIndex)
            {
                currentIndex = pCurrentIndex;
                propertyName = propertyNames[currentIndex];
            }

            /**
             * {@inheritDoc}
             */
            public String getKey()
            {
                return propertyName;
            }

            /**
             * {@inheritDoc}
             */
            public Object getValue()
            {
                if (value == this)
                {
                    value = get(currentIndex);
                }
                return value;
            }

            /**
             * {@inheritDoc}
             */
            public Object setValue(Object pValue)
            {
                Object oldValue = getValue();
                
                put(currentIndex, pValue);
                value = pValue;
                
                return oldValue;
            }
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Overwritten methods
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean equals(Object pObject)
            {
                if (pObject instanceof Map.Entry)
                {
                    Map.Entry entry = (Map.Entry)pObject;
                    Object val = getValue();

                    return propertyName.equals(entry.getKey()) && (val == null ? entry.getValue() == null : val.equals(entry.getValue()));
                }
                
                return false;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int hashCode() 
            {
                Object val = getValue();

                return propertyName.hashCode() ^ (val == null ? 0 : val.hashCode());
            }
            
            /**
             * {@inheritDoc}
             */
            @Override
            public String toString()
            {
                return propertyName + "=" + getValue();
            }
            
        } // Entry

    }   // EntryIterator
    
}   // AbstractBean
