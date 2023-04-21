/*
 * Copyright 2015 SIB Visions GmbH
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
 * 03.02.2015 - [JR] - creation
 * 22.07.2015 - [JR] - clear and sync
 */
package com.sibvisions.util.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>ShadowCopyInputStream</code> is an {@link InputStream} wrapper that copies
 * every read byte to an internal cache.
 *  
 * @author René Jahn
 */
public class ShadowCopyInputStream extends FilterInputStream
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the read cache. */
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();    
    
    /** the sync object. */
    private Object oSync = new Object();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ShadowCopyInputStream</code>.
	 * 
	 * @param pStream the wrapped stream
	 */
	public ShadowCopyInputStream(InputStream pStream)
	{
		super(pStream);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException
	{
	    int iRead = super.read();
	    
	    if (iRead != -1)
	    {
	        synchronized (oSync)
	        {
	            baos.write(iRead);
	        }
	    }
	    
	    return iRead;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] pContent, int pOffset, int pLength) throws IOException
    {
        int iRead = super.read(pContent, pOffset, pLength);
        
        if (iRead >= 0)
        {
            synchronized (oSync)
            {
                baos.write(pContent, pOffset, iRead);
            }
        }
        
        return iRead;
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the read bytes.
	 *  
	 * @return the bytes
	 */
	public byte[] dump()
	{
	    synchronized (oSync)
	    {
	        return baos.toByteArray();
	    }
	}
	
    /**
     * Clears the internal cache.
     */
    public void clear()
    {
        synchronized (oSync)
        {
            baos = new ByteArrayOutputStream();
        }
    }
	
}	// ShadowCopyInputStream
