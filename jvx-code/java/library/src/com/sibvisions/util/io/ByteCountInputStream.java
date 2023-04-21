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
 */
package com.sibvisions.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>ByteCountInputStream</code> is an {@link InputStream} wrapper which counts read bytes
 * and also enables reading of expected bytes if known.
 *  
 * @author René Jahn
 */
public class ByteCountInputStream extends FilterInputStream
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the number of expected bytes for the underlying stream. */
    private long lExpectedBytes;
    
    /** the number of skipped bytes. */
    private volatile long lSkippedBytes = 0;
    
    /** the currently read number of bytes. */
    private volatile long lReadBytes = 0;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>ByteCountInputStream</code>.
	 * 
	 * @param pStream the wrapped stream
	 */
	public ByteCountInputStream(InputStream pStream)
	{
		this(pStream, -1);
	}

	/**
	 * Creates a new instance of <code>ByteCountInputStream</code> for the given stream
	 * and calculated number of expected bytes.
	 * 
	 * @param pStream the wrapped stream
	 * @param pExpectedBytes the number of expected bytes
	 */
	public ByteCountInputStream(InputStream pStream, long pExpectedBytes)
	{
	    super(pStream);
	    
	    lExpectedBytes = pExpectedBytes;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException
	{
	    readAvailableBytes();
	    
	    super.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException
	{
	    int iRead = super.read();
	    
	    if (iRead != -1)
	    {
	        lReadBytes++;
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
            lReadBytes += iRead;
        }
        
        return iRead;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long pBytes) throws IOException
    {
        long lSkipped = super.skip(pBytes);
        
        lSkippedBytes += lSkipped;
        
        return lSkipped;
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * If expected bytes count is set, this method will read the bytes
	 * which weren't read yet.
	 *  
	 * @throws IOException if reading bytes failed
	 */
	public void readAvailableBytes() throws IOException
	{
        if (lExpectedBytes > 0)
        {
            if (lReadBytes + lSkippedBytes < lExpectedBytes)
            {
                byte[] byTrash = new byte[(int)(lExpectedBytes - lReadBytes - lSkippedBytes)];
                
                read(byTrash, 0, byTrash.length);
                
                //It's possible that above read didn't read all, so lets read until 
                //we got expected number of bytes
                while (lExpectedBytes - lReadBytes - lSkippedBytes > 0)
                {
                    read();
                }
            }
        }
	}
	
	/**
	 * Gets the read bytes.
	 * 
	 * @return the number of read bytes
	 */
	public long getReadBytes()
	{
	    return lReadBytes;
	}
	
    /**
     * Gets the skipped bytes.
     * 
     * @return the number of read bytes
     */
	public long getSkippedBytes()
	{
	    return lSkippedBytes;
	}
	
	/**
	 * Gets the expected bytes.
	 * 
	 * @return the number of expected bytes
	 */
	public long getExpectedBytes()
	{
	    return lExpectedBytes;
	}
	
}	// ByteCountInputStream
