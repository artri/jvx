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
 * 16.09.2011 - [JR] - creation
 */
package com.sibvisions.util.event;

import com.sibvisions.util.Execute;

/**
 * Defines execution events.
 * 
 * @author René Jahn
 */
public interface IExecuteListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Notification for a destroyed process execution.
	 * 
	 * @param pExecute the executed process
	 * @throws Throwable if there is an error.
	 */
	public void destroyed(Execute pExecute) throws Throwable;
	
}	// IExecuteListener
