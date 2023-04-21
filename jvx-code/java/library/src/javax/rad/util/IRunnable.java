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
 * 09.12.2015 - [RZ] - creation
 */
package javax.rad.util;

import java.io.Serializable;

/**
 * {@link IRunnable} is a clone of {@link java.lang.Runnable Runnable} with the
 * exception that the {@link #run()} function can throw any {@link Throwable}.
 * 
 * @author Robert Zenz
 */
public interface IRunnable extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Runs the action.
	 * 
	 * @throws Throwable if an exception occurs.
	 */
	public void run() throws Throwable;
	
}	// IRunnable
