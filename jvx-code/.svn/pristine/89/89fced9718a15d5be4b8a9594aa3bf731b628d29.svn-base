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
 * 19.09.2014 - [JR] - creation
 * 19.09.2014 - [JR] - #1115: used DefaultAccessController
 */
package com.sibvisions.rad.server.security;

import com.sibvisions.rad.server.annotation.Replacement;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>DefaultAccessController</code> is the standard {@link IAccessController} implementation.
 * It manages allowed objects in a simple list.
 * 
 * @author René Jahn
 */
public class DefaultAccessController implements IAccessController
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the allowed lifecycle objects. */
    private ArrayUtil<String> auAllowedLCO = null;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public boolean isAllowed(String pLifeCycleName)
    {
        //all explicite allowed lifecycle objects are accessible
        if (pLifeCycleName != null && auAllowedLCO != null)
        {
            return auAllowedLCO.contains(pLifeCycleName);
        }
        
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addAccess(String pLifeCycleName)
    {
        if (pLifeCycleName == null)
        {
            return;
        }
        
        if (auAllowedLCO == null || !auAllowedLCO.contains(pLifeCycleName))
        {
            if (auAllowedLCO == null)
            {
                auAllowedLCO = new ArrayUtil<String>();
            }

            auAllowedLCO.add(pLifeCycleName);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeAccess(String pLifeCycleName)
    {
        if (pLifeCycleName == null || auAllowedLCO == null)
        {
            return;
        }
        
        auAllowedLCO.remove(pLifeCycleName);
        
        if (auAllowedLCO.size() == 0)
        {
            auAllowedLCO = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String find(ClassLoader pLoader, String pName)
    {
    	if (pName == null || auAllowedLCO == null)
        {
            return null;
        }
        
        String sAllowed;
        
        String sDotName = pName;
        
        if (!pName.startsWith("."))
        {
            sDotName = "." + sDotName;
        }
        
        int iFound = 0;
        
        String sFoundName = null;
        
        for (int i = 0, cnt = auAllowedLCO.size(); i < cnt; i++)
        {
            sAllowed = auAllowedLCO.get(i);
            
            if (pName.equals(sAllowed))
            {
                return sAllowed;
            }
            else if (sAllowed.endsWith(sDotName))
            {
                iFound++;
                
                sFoundName = sAllowed;
            }
        }
        
        //more than one object found -> not unique -> not found
        if (iFound == 1)
        {
            return sFoundName;
        }
        
    	String sSimpleName;
    	
    	if (pName.startsWith("."))
    	{
    		sSimpleName = pName.substring(1);
    	}
    	else
    	{
    		sSimpleName = pName;
    	}
        
        return findReplacement(pLoader, sSimpleName);
    }

    /**
     * Finds a lifecycle class with a {@link Replacement} definition that matches the given name.
     * 
     * @param pLoader the class loader to use or <code>null</code> to use the default class loader
     * @param pName the name of the replacement
     * @return the found lifecycle class (full qualified class name) or <code>null</code> if no lifecycle class
     *         with given name was found
     */
    protected String findReplacement(ClassLoader pLoader, String pName)
    {
    	if (auAllowedLCO == null)
    	{
    		return null;
    	}
    	
    	String sFoundName = null;
    	String sAllowed;
    	
    	Class<?> clazz;
    	
    	Replacement replace;
    	
    	int iFound = 0;

    	for (int i = 0, cnt = auAllowedLCO.size(); i < cnt; i++)
        {
        	sAllowed = auAllowedLCO.get(i);

        	try
        	{
        		if (pLoader == null)
        		{
        			clazz = Class.forName(sAllowed);
        		}
        		else
        		{
        			clazz = Class.forName(sAllowed, false, pLoader);
        		}
        		
        		replace = clazz.getAnnotation(Replacement.class);
        		
        		if (replace != null)
        		{
        			if (pName.equals(replace.name()))
        			{
        				iFound++;
        				
        				sFoundName = sAllowed;
        			}
        		}
        	}
        	catch (Exception e)
        	{
        		LoggerFactory.getInstance(DefaultAccessController.class).debug(e);
        	}
        }
        
        //more than one object found -> not unique -> not found
        if (iFound == 1)
        {
        	return sFoundName;
        }

        return null;
    }
    
    /**
     * Clears the access list.
     */
    protected void resetAccess()
    {
    	auAllowedLCO = null;
    }
    
    /**
     * Gets the number of available access rules.
     * 
     * @return the number of access rules
     */
    protected int getAccessCount()
    {
    	return auAllowedLCO == null ? 0 : auAllowedLCO.size();
    }
    
}   // DefaultAccessController
