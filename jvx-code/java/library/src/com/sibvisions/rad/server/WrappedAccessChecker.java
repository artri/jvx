/*
 * Copyright 2018 SIB Visions GmbH
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
 * 23.10.2018 - [JR] - creation
 */
package com.sibvisions.rad.server;

import javax.rad.server.security.IAccessChecker;

/**
 * The <code>WrappedAccessChecker</code> hides an implementation of {@link IAccessChecker} and
 * forwards all interface calls to the internal implementation.
 * 
 * @author René Jahn
 */
final class WrappedAccessChecker implements IAccessChecker
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the "hidden" session. */
    protected final IAccessChecker checker;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>WrappedAccessChecker</code> for an {@link IAccessChecker}.
     * 
     * @param pChecker an {@link IAccessChecker} implementation
     */
    WrappedAccessChecker(IAccessChecker pChecker)
    {
        checker = pChecker;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public boolean isAllowed(String pLifeCycleName)
    {
    	return checker != null ? checker.isAllowed(pLifeCycleName) : false;
    }
    
    /**
     * {@inheritDoc}
     */
    public String find(ClassLoader pLoader, String pName)
    {
    	return checker != null ? checker.find(pLoader, pName) : null;
    }
    
}	// WrappedAccessChecker
