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
 * 28.04.2008 - [JR] - creation
 */
package javax.rad.application;

import javax.rad.io.IFileHandle;

/**
 * The <code>IFileHandleListener</code> get's a IFileHandle.
 * 
 * @author Martin Handsteiner
 */
public interface IFileHandleReceiver
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Receives a IFileHandle.
	 * @param pFileHandle the IFileHandle.
	 */
	public void receiveFileHandle(IFileHandle pFileHandle);
	
}	// IFileHandleReceiver
