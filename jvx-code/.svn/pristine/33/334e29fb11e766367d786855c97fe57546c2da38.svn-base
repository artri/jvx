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
 * 31.03.2013 - [JR] - creation
 */
package com.sibvisions.rad.application.event;

import com.sibvisions.rad.application.Error;

/**
 * The <code>IErrorListener</code> informs about {@link com.sibvisions.rad.application.Error} states.
 * 
 * @author René Jahn
 */
public interface IErrorListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Invoked when details will be shown.
     * 
     * @param pError the error
	 * @throws Throwable if there is an error.
     */
    public void showDetails(Error pError) throws Throwable;
    
	/**
     * Invoked when details will be hidden.
     * 
     * @param pError the error
	 * @throws Throwable if there is an error.
     */
    public void hideDetails(Error pError) throws Throwable;

}	// IErrorListener
