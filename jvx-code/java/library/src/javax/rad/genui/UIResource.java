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
 * 14.11.2008 - [HM] - creation
 * 22.07.2009 - [JR] - getResource: check null [BUGFIX]
 * 24.10.2012 - [JR] - #604: check resource
 * 25.10.2012 - [JR] - removed resource check because it must be possible to wrap UI resources!
 */
package javax.rad.genui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.rad.genui.event.ResourceEvent;
import javax.rad.genui.event.ResourceEvent.EventType;
import javax.rad.genui.event.ResourceHandler;
import javax.rad.ui.IResource;
import javax.rad.util.IObjectStore;

/**
 * Platform and technology independent wrapper for IFactory Resource.
 * 
 * @author Martin Handsteiner
 * 
 * @param <UI> the corresponding UI Resource.
 */
public abstract class UIResource<UI extends IResource> implements IResource, IObjectStore, Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The IFactory Resource. */
	protected transient UI uiResource;
	
	/** Properties. */
	private transient HashMap<String, Object> hmObjects = null; 
	
    /** The map which maps property/object names to {@link ResourceHandler}s. */
    private transient Map<String, ResourceHandler> hmpResourceHandler;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Constructs a new <code>UIResource</code>.
	 * 
	 * @param pUIResource the IFactory Resource.  
	 */
	protected UIResource(UI pUIResource)
	{
		uiResource = pUIResource;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	public Object getResource()
	{
		if (uiResource == null)
		{
			return null;
		}
		else
		{
			return uiResource.getResource();
		}
	}

    /**
     * {@inheritDoc}
     */
    public Object getObject(String pObjectName)
    {
        if (hmObjects == null)
        {
            return null;
        }
        else
        {
            return hmObjects.get(pObjectName);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<String> getObjectNames()
    {
        if (hmObjects == null)
        {
            return Collections.emptySet();
        }
        else
        {
            return new ArrayList(hmObjects.keySet());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object putObject(String pObjectName, Object pObject)
    {
        if (pObject == null)
        {
            if (hmObjects == null)
            {
                return null;
            }
            else
            {
                Object value = hmObjects.remove(pObjectName);
                if (hmObjects.isEmpty())
                {
                    hmObjects = null;
                }
                
                fireResourceChanged(EventType.Object, pObjectName, value, null);
                
                return value;
            }
        }
        else
        {
            if (hmObjects == null)
            {
                hmObjects = new HashMap<String, Object>();
            }
            
            Object value = hmObjects.put(pObjectName, pObject);
            
            fireResourceChanged(EventType.Object, pObjectName, value, pObject);
            
            return value;
        }
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
		if (pObject instanceof IResource) 
		{
			return getResource().equals(((IResource)pObject).getResource());
		}
		else 
		{
		  return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return getResource().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getClass().getName() + "[" + String.valueOf(getResource()) + "]";
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Gets the UI resource corresponding with this wrapper object.
	 * 
	 * @return the original resource
	 */
	public UI getUIResource()
	{
		return uiResource;
	}

	/**
	 * Notifies all listeners about a change.
	 * 
	 * @param pType the event type
	 * @param pName the parameter/object name
	 * @param pOld the old value
	 * @param pNew the new value
	 */
	protected void fireResourceChanged(EventType pType, String pName, Object pOld, Object pNew)
	{
	    if (hmpResourceHandler != null)
	    {
            if (hmpResourceHandler.containsKey(null))
            {
                hmpResourceHandler.get(null).dispatchEvent(new ResourceEvent(this, pType, pName, pOld, pNew));
            }
            
            if (hmpResourceHandler.containsKey(pName))
            {
                hmpResourceHandler.get(pName).dispatchEvent(new ResourceEvent(this, pType, pName, pOld, pNew));
            }
	    }
	}
	
	/**
	 * Gets whether the resource has at least one {@link ResourceHandler}.
	 * 
	 * @return <code>true</code> if at least one resource handler is configured, <code>false</code> otherwise
	 */
	protected boolean hasResourceHandler()
	{
	    return hmpResourceHandler != null && hmpResourceHandler.size() > 0;
	}
	
	/**
	 * Gets the resource changed event handler.
	 * 
	 * @return the handler
	 */
	public ResourceHandler eventResourceChanged()
	{
	    return eventResourceChanged(null);
	}

    /**
     * Gets the resource handler for a single parameter/object name.
     * 
     * @param pName the parameter/object name
     * @return the handler
     */
    public ResourceHandler eventResourceChanged(String pName)
    {
        if (hmpResourceHandler == null)
        {
            hmpResourceHandler = new HashMap<String, ResourceHandler>();
        }
        
        ResourceHandler handler = hmpResourceHandler.get(pName);
        
        if (handler == null)
        {
            handler = new ResourceHandler();
            
            hmpResourceHandler.put(pName, handler);
        }
        
        return handler;
    }
	
}	// UIResource
