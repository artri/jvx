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
 * 04.01.2011 - [RH] - creation
 */
package com.sibvisions.rad.persist.event;

/**
 * The <code>StorageHandler</code> is a <code>EventHandler</code> that 
 * handles <code>IStorageListener</code>. 
 * 
 * @author Roland Hörmann
 */
public class StorageHandler extends StorageEventHandler<IStorageListener>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new StorageHandler.
	 */
	public StorageHandler()
	{
		super(IStorageListener.class);
	}

}	// StorageHandler
