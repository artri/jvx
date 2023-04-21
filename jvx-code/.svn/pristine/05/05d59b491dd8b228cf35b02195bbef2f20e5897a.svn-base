/*
 * Copyright 2013 SIB Visions GmbH
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
 * 09.04.2014 - [HM] - creation
 */
package javax.rad.genui;

import java.io.Serializable;
import java.util.WeakHashMap;

/**
 * The <code>AbstractUIFactoryResource</code> holds the resource per factory. It creates a resource
 * copy if the resource was initialized as static resource.
 * 
 * @author Martin Handsteiner
 *
 * @param <UI> the resource type
 */
public abstract class AbstractUIFactoryResource<UI> implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The resource. */
	private transient WeakHashMap<Object, UI> whmpResources = new WeakHashMap<Object, UI>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>AbstractUIFactoryResource</code>.
	 * 
	 * @param pResource the resource
	 */
	protected AbstractUIFactoryResource(UI pResource)
	{
		setUIResource(pResource);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the UI resource corresponding with this wrapper object.
	 * 
	 * @return the original resource
	 */
	public final UI getUIResource()
	{
		UI res;
		
		Object oCacheKey = createCacheKey();
		
		synchronized (whmpResources)
		{
			res = whmpResources.get(oCacheKey);
		}
		
		if (res == null)
		{
	        UI uiResource = null;
	        synchronized (whmpResources)
	        {
	            // class alone is not enough, because different technologies have different classes
	            uiResource = whmpResources.entrySet().iterator().next().getValue();
	        }
	        
			res = UIFactoryManager.cloneResource(uiResource);
			
	        synchronized (whmpResources)
	        {
	            whmpResources.put(oCacheKey, res);
	        }
		}
		
		return res;
	}

	/**
	 * Gets the UI resource corresponding with this wrapper object.
	 * 
	 * @param pResource the original resource
	 */
	protected final void setUIResource(UI pResource)
	{
        Object oCacheKey = createCacheKey(); 
        
        Object oClass = UIFactoryManager.getFactory().getClass();
        
		synchronized(whmpResources)
		{
		    // ensure, that all instances create a new clone of the new value.
		    whmpResources.clear();

		    if (oClass != oCacheKey)
		    {
    		    // we need at least one cloneable reference in the weak hash map. 
		    	// the class will live, as long as the classloader lives.
    		    whmpResources.put(oClass, pResource);
		    }
		    
			whmpResources.put(oCacheKey, pResource);
		}
	}

	/**
	 * Creates the cache key.
	 * 
	 * @return the cache key
	 */
	protected Object createCacheKey()
	{
	    return UIFactoryManager.getFactory().getClass();	    
	}
	
}	// AbstractUIFactoryResource
