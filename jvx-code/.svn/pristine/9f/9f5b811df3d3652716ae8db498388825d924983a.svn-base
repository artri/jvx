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
 * 07.05.2013 - [JR] - creation
 */
package demo.special;

import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;

import com.sibvisions.rad.server.GenericBean;

/**
 * Test class for #659.
 * 
 * @author René Jahn
 */
public class Bug659 extends GenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns a simple txt file.
	 * 
	 * @return the txt file.
	 * @throws Exception if file creation fails
	 */
	public IFileHandle getFile() throws Exception
	{
		RemoteFileHandle rfh = new RemoteFileHandle("My_application, and,some,other,data.txt");
		
		rfh.setContent("Welcome JVx!".getBytes());
		
		return rfh;
	}
	
}	// Bug659
