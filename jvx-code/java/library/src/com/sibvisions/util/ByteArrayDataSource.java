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
 * 14.10.2009 - [JR] - creation
 * 27.10.2009 - [JR] - send: parameter validation [BUGFIX]
 */
package com.sibvisions.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;

import com.sibvisions.util.type.FileUtil;

/**
 * The <code>ByteArrayDataSource</code> class is a utility class for <code>IFileHandle</code> data.
 * 
 * @author Martin Handsteiner
 */
public final class ByteArrayDataSource implements DataSource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name. */
	private String name;

	/** the file handle. */
	private byte[] content;
	
	/** the file type map. */
	private FileTypeMap fileTypeMap = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new <code>ByteArrayDataSource</code>.
	 * 
	 * @param pName the name.
	 * @param pContent the file handle.
	 */
	public ByteArrayDataSource(String pName, byte[] pContent)
	{
		name = pName;
		content = pContent;
	}
	
	/**
	 * Constructs a new <code>ByteArrayDataSource</code>.
	 * 
	 * @param pName the name.
	 * @param pContent the file handle.
	 */
	public ByteArrayDataSource(String pName, InputStream pContent)
	{
		name = pName;
		content = FileUtil.getContent(pContent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getContentType()
	{
		if (fileTypeMap == null)
		{
			return FileTypeMap.getDefaultFileTypeMap().getContentType(getName());
		}
		else
		{
			return fileTypeMap.getContentType(getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getInputStream() throws IOException
	{
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public OutputStream getOutputStream() throws IOException
	{
		throw new IOException("Datasource may not be changed!");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the FileTypeMap.
	 * 
	 * @return the FileTypeMap.
	 */
	public FileTypeMap getFileTypeMap()
	{
		return fileTypeMap;
	}

	/**
	 * Sets the FileTypeMap.
	 * 
	 * @param pFileTypeMap the FileTypeMap.
	 */
	public void setFileTypeMap(FileTypeMap pFileTypeMap)
	{
		fileTypeMap = pFileTypeMap;
	}

	/**
	 * Sets the name.
	 * 
	 * @param pName the name.
	 */
	public void setName(String pName)
	{
		name = pName;
	}
	
}	// ByteArrayDataSource
