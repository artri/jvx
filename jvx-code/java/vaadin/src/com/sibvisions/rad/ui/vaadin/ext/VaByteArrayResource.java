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
 * 24.09.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.sibvisions.util.type.FileUtil;
import com.vaadin.server.StreamResource;

/**
 * The <code>VaByteArrayResource</code> is a data source with byte[] as stream data.
 * 
 * @author René Jahn
 */
public class VaByteArrayResource extends StreamResource
                                 implements INamedResource,
                                            IDownloadStream
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the style name. */
	private String sStyleName;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaByteArrayResource</code>.
	 * 
	 * @param pName the image name
	 * @param pData the image data
	 */
	public VaByteArrayResource(String pName, byte[] pData)
	{
		super(new DataSource(pData), FileUtil.getName(pName));
	}

	/**
	 * Creates a new instance of <code>VaByteArrayResource</code> with the given
	 * style name.
	 * 
	 * @param pName the image name
	 * @param pData the image data
	 * @param pStyleName the style name
	 */
	public VaByteArrayResource(String pName, byte[] pData, String pStyleName)
	{
		this(pName, pData);
		
		sStyleName = pStyleName;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getResourceName()
	{
		return getFilename();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyleName()
	{
		return sStyleName;
	}
	
    /**
     * {@inheritDoc}
     */
	public void setStyleName(String pStyleName)
	{
		sStyleName = pStyleName;
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>DataSource</code> is a {@link StreamSource} with byte[] as input.
	 * 
	 * @author René Jahn
	 */
	private static final class DataSource implements StreamSource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class Members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the stream data. */
		private byte[] byData;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>DataSource</code>.
		 * 
		 * @param pData the image data
		 */
		private DataSource(byte[] pData)
		{
			byData = pData;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public InputStream getStream()
		{
			return new ByteArrayInputStream(byData);
		}
		
	}	// DataSource
	
}	// VaByteArrayResource
