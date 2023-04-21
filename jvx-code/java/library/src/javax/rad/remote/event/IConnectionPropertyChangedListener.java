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
 * 04.04.2014 - [RZ] - creation
 */
package javax.rad.remote.event;

/**
 * The <code>IConnectionPropertyChangedListener</code> interface should be used
 * to get notifications when a property in the {@link javax.rad.remote.IConnection} changes.
 * 
 * @author Robert Zenz
 */
public interface IConnectionPropertyChangedListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * A property was changed.
	 * 
	 * @param pEvent the property information
	 * @throws Throwable if there is an error.
	 */
	public void propertyChanged(PropertyEvent pEvent) throws Throwable;
	
}	// IConnectionPropertyChangedListener
