/*
 * Copyright 2011 SIB Visions GmbH
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
 * 22.09.2011 - [JR] - creation
 */
package demo.special;

import com.sibvisions.rad.server.annotation.StrictIsolation;

import demo.InstanceChecker;
import demo.Session;

/**
 * This class extends {@link ScreenIsolation} is an isolated LCO.
 * 
 * @author René Jahn
 */
@StrictIsolation
public class ScreenIsolation extends Session
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ScreenIsolation</code>.
     */
    public ScreenIsolation()
    {
        InstanceChecker.add(ScreenIsolation.class);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the instance string.
     * 
     * @return {@link InstanceChecker#string()}
     */
    public String getValue()
    {
        return InstanceChecker.string();
    }
    
}	// ScreenIsolation
