/*
 * Copyright 2012 SIB Visions GmbH
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
 * 20.12.2012 - [JR] - creation
 */
package javax.rad.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>DirectorHandle</code> is an {@link IFileHandle} representation for directories.
 * It does not allow file access. It is only an informative class.
 * 
 * @author René Jahn
 */
public class DirectoryHandle implements IFileHandle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the absolute directory path. */
	private String sAbsolutePath;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DirectoryHandle</code> without checks.
	 * 
	 * @param pDirectory the absolute directory path. If the path does not exist,
	 *                   it doesn't matter 
	 */
	public DirectoryHandle(String pDirectory)
	{
		sAbsolutePath = pDirectory;
	}
	
	/**
	 * Creates a new instance of <code>DirectoryHandle</code> and checks if the
	 * directory exists.
	 * 
	 * @param pDirectory the directory
	 * @throws IllegalArgumentException if directory does not exist
	 */
	public DirectoryHandle(File pDirectory)
	{
		if (!pDirectory.isDirectory())
		{
			throw new IllegalArgumentException("Given file is not a directory!");
		}
		
		sAbsolutePath = pDirectory.getAbsolutePath();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getFileName()
	{
		return sAbsolutePath;
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getInputStream() throws IOException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLength() throws IOException
	{
		return -1;
	}

}	// DirectoryHandle
