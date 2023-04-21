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
 */
package com.sibvisions.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>MagicByteInputStream</code> is an {@link InputStream} wrapper which reads some magic
 * bytes from the end of the stream. It will read the byte sequence until found.
 *  
 * @author René Jahn
 */
public class MagicByteInputStream extends FilterInputStream
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the magic byte(s). */
    private byte[] byMagic;

    /** the cureent pos in the magic byte (for comparing reasons). */
    private int iBytePos;
    
    /** whether the magic byte sequence was already found. */
    private boolean bFound;
    
    /** whether EOF should be returned if magic byte was found. */
    private boolean bAutoEOF;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>MagicByteInputStream</code> for the given stream
	 * and magic byte(s).
	 * 
	 * @param pStream the wrapped stream
	 * @param pMagic the magic byte sequence
	 */
	public MagicByteInputStream(InputStream pStream, byte[] pMagic)
	{
	    this(pStream, pMagic, false);
	}

	/**
	 * Creates a new instance of <code>MagicByteInputStream</code> for the given stream
	 * and optional sets EOF if magic byte was detected. 
	 * 
	 * @param pStream the wrapped stream
	 * @param pMagic the magic byte sequence
	 * @param pAutoEOF <code>true</code> to set EOF if magic byte was found
	 */
    public MagicByteInputStream(InputStream pStream, byte[] pMagic, boolean pAutoEOF)
    {
        super(pStream);
        
        byMagic = pMagic;
        bAutoEOF = pAutoEOF;
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
        if (bFound && bAutoEOF)
        {
            return -1;
        }
        
        int iRead = super.read();
        
        if (iRead != -1)
        {
            if (bFound)
            {
                bFound = false;
                iBytePos = 0;
            }
            
            if (byMagic[iBytePos] == (byte)iRead)
            {
                iBytePos++;
            }
            else
            {
                iBytePos = 0;
            }
            
            bFound = iBytePos == byMagic.length;
        }
        
        return iRead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] pContent, int pOffset, int pLength) throws IOException
    {
        if (bFound && bAutoEOF)
        {
            return -1;
        }
        
        int iRead = super.read(pContent, pOffset, pLength);

        if (iRead != -1)
        {
            if (bFound)
            {
                bFound = false;
                iBytePos = 0;
            }
            
            int iStartPos = iRead - byMagic.length - iBytePos - 1;
            
            if (iStartPos < 0)
            {
                iStartPos = 0;
            }

            for (int i = iStartPos; i < iRead && !bFound; i++)
            {
                if (byMagic[iBytePos] == (byte)pContent[pOffset + i])
                {
                    iBytePos++;
                }
                else
                {
                    iBytePos = 0;
                }
                
                bFound = iBytePos == byMagic.length;
            }
        }
        
        return iRead;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException
	{
	    readMagicByte();
	    
	    super.close();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Reads until the magic byte was found.
	 *  
	 * @throws IOException if reading failed or EOF reached and magic byte wasn't found
	 */
	public void readMagicByte() throws IOException
	{
        if (byMagic != null && !bFound)
	    {
	        int iRead = 0;
	        
	        while (!bFound && iRead >= 0)
	        {
	            iRead = read();
	        }
	        
	        if (!bFound)
	        {
                throw new IOException("Magic byte was not found!");
	        }
	    }
	}
	
}	// MagicByteInputStream
