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
 * 06.06.2010 - [JR] - creation
 * 15.07.2012 - [JR] - getRawHash methods implemented
 * 11.08.2015 - [JR] - #1447: zip support
 */
package com.sibvisions.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sibvisions.util.security.SecurityProvider;
import com.sibvisions.util.type.CodecUtil;

/**
 * The <code>SecureHash</code> class provides the functionality of a message digest algorithm, 
 * such as MD5 or SHA. Message digests are secure one-way hash functions that take 
 * arbitrary-sized data and output a fixed-length hash value.
 * 
 * @author René Jahn
 * @see MessageDigest
 */
public class SecureHash
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the MD2 hash algorithm. */
	public static final String MD2 = "MD2";
	/** the MD4 hash algorithm. */
	public static final String MD4 = "MD4";
	/** the MD5 hash algorithm. */
	public static final String MD5 = "MD5";
	/** the SHA hash algorithm. */
	public static final String SHA = "SHA";
	/** the SHA-256 hash algorithm. */
	public static final String SHA_256 = "SHA-256";
	/** the SHA-384 hash algorithm. */
	public static final String SHA_384 = "SHA-384";
	/** the SHA-512 hash algorithm. */
	public static final String SHA_512 = "SHA-512";
	
	
	/** the current hash algorithm. */
	private String sAlgorithm;
	
	/** the current raw hash. */
	private byte[] byHash = null;
	
    /** the current message digest. */
    private MessageDigest md = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
		SecurityProvider.init();
    }

    /**
     * Creates a new instance of <code>SecureHash</code> for a specific hash algorithm.
     * 
     * @param pAlgorithm the hash algorithm 
     * {@link #MD5}
     * {@link #SHA}
     */
    public SecureHash(String pAlgorithm)
    {
    	sAlgorithm = pAlgorithm;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the hash of all content read from an {@link InputStream}.
     * 
     * @param pAlgorithm the hash algorithm
     * @param pData the data stream
     * @return the hash
     * @throws NoSuchAlgorithmException specified algorithm is not available
     * @throws IOException if an error occurs during reading from the stream
     */
	public static String getHash(String pAlgorithm, InputStream pData) throws NoSuchAlgorithmException,
                                                                              IOException
	{
		return CodecUtil.encodeHex(getRawHash(pAlgorithm, pData));
	}    
    
    
    /**
     * Gets the raw hash of all content read from an {@link InputStream}.
     * 
     * @param pAlgorithm the hash algorithm
     * @param pData the data stream
     * @return the raw hash
     * @throws NoSuchAlgorithmException specified algorithm is not available
     * @throws IOException if an error occurs during reading from the stream
     */
	public static byte[] getRawHash(String pAlgorithm, InputStream pData) throws NoSuchAlgorithmException,
                                                                                 IOException
	{
		MessageDigest md = MessageDigest.getInstance(pAlgorithm);
		
		readStream(pData, md);
		
		return md.digest();
	}    
	
	/**
	 * Gets the hash of specific data.
	 * 
	 * @param pAlgorithm the hash algorithm
	 * @param pData the data
	 * @return the hash
	 * @throws NoSuchAlgorithmException specified algorithm is not available
	 */
	public static String getHash(String pAlgorithm, byte[] pData) throws NoSuchAlgorithmException
	{
		return CodecUtil.encodeHex(getRawHash(pAlgorithm, pData));
	}
	
	/**
	 * Gets the raw hash of specific data.
	 * 
	 * @param pAlgorithm the hash algorithm
	 * @param pData the data
	 * @return the raw hash
	 * @throws NoSuchAlgorithmException specified algorithm is not available
	 */
	public static byte[] getRawHash(String pAlgorithm, byte[] pData) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(pAlgorithm);
		
		
		md.update(pData);
		
		return md.digest();
	}
	
	/**
	 * Adds data to the digest.
	 * 
	 * @param pData the data stream
     * @throws NoSuchAlgorithmException specified algorithm is not available
     * @throws IOException if an error occurs during reading from the stream
	 */
	public void add(InputStream pData) throws NoSuchAlgorithmException,
						  			          IOException
	{
		if (md == null)
		{
			md = MessageDigest.getInstance(sAlgorithm);
		}
		
		readStream(pData, md);
	}

	/**
	 * Adds data to the digest.
	 * 
	 * @param pData the data
	 * @throws NoSuchAlgorithmException specified algorithm is not available
	 */
	public void add(byte[] pData) throws NoSuchAlgorithmException
	{
		if (pData != null)
		{
			if (md == null)
			{
				md = MessageDigest.getInstance(sAlgorithm);			
			}
		
			md.update(pData);
		}
	}	

	/**
	 * Resets the digest.
	 */
	public void reset()
	{
		byHash = null;
		
		if (md != null)
		{
			md.reset();
		}
	}
	
	/**
	 * Gets the current hash.
	 * 
	 * @return the hash for the current data
	 */
	public String getHash()
	{
		return CodecUtil.encodeHex(getRawHash());
	}	
	
	/**
	 * Gets the current raw hash.
	 * 
	 * @return the raw hash for the current data
	 */
	public byte[] getRawHash()
	{
		if (md != null)
		{
			//this call resets the digest -> its not null but contains no data and the hash
			//is different from the current one!
			byHash = md.digest();

			//allows the access to the current hash if the digest didn't change!
			md = null;
		}
		
		return byHash;
	}
	
	/**
	 * Reads all bytes of given stream and checks if the stream is a {@link ZipInputStream}. 
	 * If given stream is a zip, all entries will be read.
	 * 
	 * @param pStream the stream
	 * @param pDigest the digest where to add read bytes
	 * @throws IOException if reading failed
	 */
	private static void readStream(InputStream pStream, MessageDigest pDigest) throws IOException
	{
        int iLen;
        
        byte[] byContent = new byte[4096];

        if (pStream instanceof ZipInputStream)
	    {
	        ZipInputStream zis = (ZipInputStream)pStream;
	        
            ZipEntry zeCurrent;
            
            while ((zeCurrent = zis.getNextEntry()) != null)
            {
                pDigest.update(zeCurrent.getName().getBytes());
                
                if (!zeCurrent.isDirectory())
                {
                    while ((iLen = zis.read(byContent)) != -1)
                    {
                        pDigest.update(byContent, 0, iLen);
                    }
                }
            }
	    }
	    else
	    {
            while ((iLen = pStream.read(byContent)) != -1)
            {
                pDigest.update(byContent, 0, iLen);
            }
	    }
	}
    
}	// SecureHash
