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
 * 27.05.2013 - [SW] - creation
 * 05.03.2014 - [JR] - show/download mode implemented
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.io.IOException;
import java.io.InputStream;

import javax.rad.io.IFileHandle;

import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;

/**
 * The <code>VaadinStreamResource</code> class sets the content-type for the {@link DownloadStream}
 * of the downloadable the file.
 * 
 * @author Stefan Wurm
 */
public class VaadinStreamResource extends StreamResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the file handle. **/ 
	private IFileHandle fileHandle = null;
	
	/** whether the file should be shown (<code>true</code>) or downloaded (<code>false</code>). */
	private boolean bShowMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinStreamResource</code>.
	 * 
	 * @param pFileHandle the file
	 * @param pShowMode <code>true</code> to try to show the file in browser, <code>false</code> to download the file
	 */
	public VaadinStreamResource(IFileHandle pFileHandle, boolean pShowMode)
	{
	    super(createStreamSource(pFileHandle), StringUtil.convertToName(FileUtil.removeExtension(pFileHandle.getFileName())) + "." + 
	                                           FileUtil.getExtension(pFileHandle.getFileName()));
	    
	    fileHandle = pFileHandle;
	    bShowMode = pShowMode;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public DownloadStream getStream() 
    {
    	try
    	{
	    	final DownloadStream stream = new DownloadStream(fileHandle.getInputStream(), getMIMEType(), fileHandle.getFileName());

	        if (!bShowMode) 
	        {
	            // Content-Disposition: attachment generally forces download
	            stream.setParameter("Content-Disposition", "attachment; filename=\"" + stream.getFileName() + "\"");		 
	            stream.setContentType("application/octet-stream;charset=UTF-8");
	        }

	        //no caching
	        stream.setCacheTime(0);
	        
	        return stream;
    	}
    	catch (Exception ex)
    	{
            // show the file, even if there's an exception
    	    LoggerFactory.getInstance(getClass()).debug(ex);
    	}
    	
        DownloadStream ds = new DownloadStream(getStreamSource().getStream(), getMIMEType(), getFilename());
        ds.setBufferSize(getBufferSize());
        
        return ds;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a {@link StreamSource} for the data transfer.
     * 
     * @param pFileHandle the content
     * @return the stream
     */
    private static StreamSource createStreamSource(final IFileHandle pFileHandle)
    {
        StreamResource.StreamSource source = new StreamResource.StreamSource() 
        {
            public InputStream getStream() 
            {
               try
               {
                   return pFileHandle.getInputStream();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            } 
        };
        
        return source;
    }
    
}	// VaadinStreamResource
