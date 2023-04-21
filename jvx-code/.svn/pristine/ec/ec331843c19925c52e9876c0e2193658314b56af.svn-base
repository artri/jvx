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
 * 13.09.2014 - [JR] - creation
 */
package demo;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>InstanceChecker</code> is a utility class to build a list of instances (only for tests).
 * 
 * @author René Jahn
 */
public final class InstanceChecker
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the instance information list. */
    private static ArrayUtil<String> auInstances = new ArrayUtil<String>();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invisible constructor becasue <code>InstanceChecker</code> is a utility class.
     */
    private InstanceChecker()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Adds an instance to the list.
     * 
     * @param pClass the class
     */
    public static void add(Class pClass)
    {
        auInstances.add(pClass.getName());
    }
    
    /**
     * Clears the instance list.
     */
    public static void clear()
    {
        auInstances.clear();
    }
    
    /**
     * Gets the list of instances as string with line-feeds.
     * 
     * @return the list of known instances
     */
    public static String string()
    {
        StringBuilder sb = new StringBuilder();
        
        for (String s : auInstances)
        {
            if (sb.length() > 0)
            {
                sb.append("\n");
            }
            
            sb.append(s);
        }
        
        return sb.toString();
    }
    
}   // InstanceChecker
