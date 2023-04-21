/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.11.2021 - [JR] - creation
 */
package com.sibvisions.rad.application.event.type;

import com.sibvisions.rad.application.Dialog;

/**
 * The <code>IOkDialogListener</code> informs about dialog close state.
 * 
 * @author René Jahn
 */
public interface IOkDialogListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Invoked when dialog was closed.
     * 
     * @param pDialog the dialog
	 * @throws Throwable if there is an error.
     */
    public void dialogOk(Dialog pDialog) throws Throwable;

}	// IOkDialogListener
