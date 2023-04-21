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
 * 05.02.2015 - [JR] - creation
 * 22.07.2015 - [JR] - clear and sync
 */
package com.sibvisions.util.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The <code>ShadowCopyOutputStream</code> is an {@link OutputStream} wrapper that copies
 * every written byte to an internal cache.
 *  
 * @author René Jahn
 */
public class ShadowCopyOutputStream extends FilterOutputStream
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the write cache. */
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();    
    
    /** the sync object. */
    private Object oSync = new Object();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ShadowCopyOutputStream</code>.
	 * 
	 * @param pStream the wrapped stream
	 */
	public ShadowCopyOutputStream(OutputStream pStream)
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
    public void write(int pByte) throws IOException
    {
        super.write(pByte);
        
        synchronized (oSync)
        {
            baos.write(pByte);
        }
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the written bytes.
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
	
}	// ShadowCopyOutputStream
