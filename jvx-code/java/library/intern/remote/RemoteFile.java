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
 * 01.10.2008 - [JR] - creation
 */
package remote;

import java.io.File;
import java.util.Arrays;

import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.ArrayUtil;

/**
 * Simple remote file object as prove of concept for server side 
 * lifecycle objects (Global, Application, Session, WorkScreen).
 * 
 * @author René Jahn
 */
public class RemoteFile
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the parent. */
	private RemoteFile parent = null;

	/** the file reference. */
	private File file = null;
	
	/** the user access list for the file reference. */
	private ArrayUtil<String> auUserACL = new ArrayUtil<String>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>RemoteFile</code> without file
	 * reference.
	 */
	public RemoteFile()
	{
	}
	
	/**
	 * Creates a new instance of <code>RemoteFile</code> for a file in
	 * the filesystem.
	 * 
	 * @param pFile the file
	 */
	public RemoteFile(File pFile)
	{
		this.file = pFile;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns the absolute pathname string of the remote file.
	 * 
	 * @return the absolute pathname string
	 * @see java.io.File#getAbsolutePath()
	 */
	public String getAbsolutePath()
	{
		return file.getAbsolutePath();
	}
	
	/**
	 * Returns the time that the file was last modified.
	 * 
	 * @return the last modified time in millis or 0L if an error occurs or 
	 *         the file does not exist
     * @see java.io.File#lastModified()
	 */
	public long lastModified()
	{
		return file.lastModified();
	}
	
	/**
	 * Removes all users from the access list.
	 */
	@Accessible
	public void clearACL()
	{
		auUserACL.clear();
	}
	
	/**
	 * Adds a user to the access list of the remote file.
	 * 
	 * @param pUserName the user name
	 */
	@Accessible
	public void addUserToACL(String pUserName)
	{
		auUserACL.add(pUserName);
	}
	
	/**
	 * Returns the user access list for the remote file.
	 * 
	 * @return list with usernames
	 */
	@Accessible
	public String getUserACL()
	{
		return Arrays.toString(auUserACL.toArray());
	}
	
	/**
	 * Returns a fictive parent file for this remote file.
	 * 
	 * @return the {@link Configuration#getApplicationsDir()}
	 */
	@Accessible
	public RemoteFile getParent()
	{
		if (parent == null)
		{
			return new RemoteFile(Configuration.getApplicationsDir());
		}
		else
		{
			return parent;
		}
	}
	
	public RemoteFile getNotAccessibleParent()
	{
	    return getParent();
	}

	/**
	 * Sets a parent file for this remote file.
	 * 
	 * @param pParent the {@link RemoteFile}
	 */
	public void setParent(RemoteFile pParent)
	{
		parent = pParent;
	}
	
	/**
	 * Returns the name of the remote file.
	 * 
	 * @return the file name
	 */
	@Accessible
	public String getName()
	{
		return file.getName();
	}
	
}	// RemoteFile
