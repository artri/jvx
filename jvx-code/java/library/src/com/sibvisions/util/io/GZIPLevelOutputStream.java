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
 * 27.05.2022 - [HM] - creation
 */
package com.sibvisions.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The <code>GZIPLevelOutputStream</code> extends the {@link GZIPOutputStream} and
 * allows to set the compression level.
 *  
 * @author Martin Handsteiner
 */
public class GZIPLevelOutputStream extends GZIPOutputStream
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Compression level for no compression. */
    public static final int NO_COMPRESSION = 0;

    /** Compression level for fastest compression. 
     *  About 3% less compression than best compression.
     *  means about 25% bigger files than best compression.
     */
    public static final int BESTSPEED_COMPRESSION = 1;

    /** Compression level for fast and acceptable compression. 
     *  About 9% slower than fastest compression
     *  About 2% less compression than best compression.
     *  means about 15% bigger files than best compression.
     */
    public static final int FAST_COMPRESSION = 3;

    /** Compression level for best compression.
      * About 230% slower than fastest compression
      */
    public static final int BEST_COMPRESSION = 9;

    /** Default compression level (normally 6).
      * About 175% slower than fastest compression
      * About 0.1% less compression than best compression.
      * means about 1% bigger files than best compression.
      */
    public static final int DEFAULT_COMPRESSION = -1;

    /** The compression level. */
    private int level;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>GZIPLevelOutputStream</code>.
	 * 
	 * @param pStream the output stream
     * @exception IOException If an I/O error has occurred.
	 */
	public GZIPLevelOutputStream(OutputStream pStream) throws IOException
	{
		this(pStream, DEFAULT_COMPRESSION);
	}

    /**
     * Creates a new instance of <code>GZIPLevelOutputStream</code>.
     * 
     * @param pStream the output stream
     * @param pLevel the compression level
     * @exception IOException If an I/O error has occurred.
     */
	public GZIPLevelOutputStream(OutputStream pStream, int pLevel) throws IOException
	{
	    super(pStream);

	    setCompressionLevel(pLevel);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the compression level.
	 * 
	 * @return the compression level.
	 */
	public long getCompressionLevel()
	{
	    return level;
	}
	
    /**
     * Sets the compression level.
     * 
     * @param pLevel the compression level.
     */
	public void setCompressionLevel(int pLevel)
	{
	    def.setLevel(pLevel);
	    level = pLevel;
	}
	
}	// GZIPLevelOutputStream
