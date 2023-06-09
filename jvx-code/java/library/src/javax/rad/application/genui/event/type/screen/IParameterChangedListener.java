/*
 * Copyright 2015 SIB Visions GmbH
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
 * 21.11.2015 - [JR] - creation
 */
package javax.rad.application.genui.event.type.screen;

import javax.rad.application.genui.WorkScreen;

/**
 * The <code>IParameterChangedListener</code> notifies about changed {@link WorkScreen} parameters.
 * 
 * @author Ren� Jahn
 */
public interface IParameterChangedListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invoked when a work-screen parameter was changed.
     * 
     * @param pScreen the work-screen
     * @param pParameter the parameter name
     * @param pOldValue the old value
     * @param pNewValue the new value
	 * @throws Throwable if there is an error.
     */
    public void parameterChanged(WorkScreen pScreen, String pParameter, Object pOldValue, Object pNewValue) throws Throwable;

}	// IParameterChangedListener
