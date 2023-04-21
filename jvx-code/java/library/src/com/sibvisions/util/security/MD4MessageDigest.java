/*
 * Copyright 2012 SIB Visions GmbH
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
 * 08.07.2012 - [TK] - creation
 */
package com.sibvisions.util.security;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Implementation of the MD4 Message-Digest Algorithm based upon the reference implementation in RFC1320.
 * 
 * @author Thomas Krautinger
 */
public class MD4MessageDigest extends MessageDigest
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** A four-word (A, B, C, D) buffer to compute the message digest. */
	private int[] contextState = new int[4];
	
	/** Number of bits, modulo 2^64 (lsb first). */
	private long contextCount = 0;
	
	/** Input buffer (64 bytes). */
	private byte[] contextBuffer = new byte[64];
	
	/** Padding (64 bytes). */
	static byte[] padding = new byte[64];
	
	/** Work buffer. */
	private int[] x = new int[16];
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates a new instance of <code>MD4MessageDigest</code>.
	 */
	public MD4MessageDigest()
	{
		super("MD4");
		
		engineReset();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void engineUpdate(byte pInput)
	{
		//compute number of bytes still unhashed; ie. present in buffer
        int i = (int) (contextCount % 64);
        contextCount++;
        
        contextBuffer[i] = pInput;
        
        if (i == 64 - 1)
        {
            transform(contextBuffer, 0);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void engineUpdate(byte[] pInput, int pOffset, int pLength) 
	{
		if (pOffset < 0 || pLength < 0 || ((pOffset + pLength) > pInput.length))
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int index = (int) contextCount % 64;	
		contextCount += pLength;
		
		int partLength = 64 - index;
		
		int i = 0;
		
		//transform as many times as possible.
		if (pLength >= partLength)
		{
			System.arraycopy(pInput, pOffset, contextBuffer, index, partLength);
			transform(contextBuffer, 0);
			
			for (i = partLength; (i + 63) < pLength; i += 64)
			{
				transform(pInput, pOffset + i);
			}
			
			index = 0;
		}
		
		//buffer remaining input
		if (i < pLength)
		{    
			System.arraycopy(pInput, pOffset + i, contextBuffer, index, pLength - i);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] engineDigest() 
	{
		byte[] bits = new byte[8];
		byte[] digest;
		
		//save number of bits	
		for (int i = 0; i < 8; i++)
		{
		   bits[i] = (byte)((contextCount * 8) >>> (8 * i));
		}
		
		int index = (int) contextCount % 64;
		int paddingLength = (index < 56) ? (56 - index) : (120 - index);
		
		engineUpdate(padding, 0, paddingLength);
		
		//append length (befor padding)
		engineUpdate(bits, 0, 8);
		
		digest = encode(contextState, 0, 4);

		//reset sensitivie informations
		engineReset();
		
		return digest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void engineReset()
	{
		//reset state
		contextState[0] = 0x67452301;
		contextState[1] = 0xefcdab89;
		contextState[2] = 0x98badcfe; 
		contextState[3] = 0x10325476;
		
		//reset count
		contextCount = 0;
		
		//reset buffer
		Arrays.fill(contextBuffer, (byte) 0);
		
		//reset padding
		Arrays.fill(padding, (byte) 0);
		padding[0] = (byte) 0x80;
		
		//reset x (working buffer)
		Arrays.fill(x, 0);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * MD4 basic transformation based on block.
	 * 
	 * @param pBlock the data to transform.
	 * @param pOffset the offset to start from in the array of bytes.
	 */
	private void transform(byte[] pBlock, int pOffset)
	{
		int a = contextState[0];
		int b = contextState[1];
		int c = contextState[2];
		int d = contextState[3];
		
		x = decode(pBlock, pOffset, 64);
		
		//round 1
		a = ff(a, b, c, d, x[ 0], 3);  /* 1 */
		d = ff(d, a, b, c, x[ 1], 7);  /* 2 */
		c = ff(c, d, a, b, x[ 2], 11); /* 3 */
		b = ff(b, c, d, a, x[ 3], 19); /* 4 */
		a = ff(a, b, c, d, x[ 4], 3);  /* 5 */
		d = ff(d, a, b, c, x[ 5], 7);  /* 6 */
		c = ff(c, d, a, b, x[ 6], 11); /* 7 */
		b = ff(b, c, d, a, x[ 7], 19); /* 8 */
		a = ff(a, b, c, d, x[ 8], 3);  /* 9 */
		d = ff(d, a, b, c, x[ 9], 7);  /* 10 */
		c = ff(c, d, a, b, x[10], 11); /* 11 */
		b = ff(b, c, d, a, x[11], 19); /* 12 */
		a = ff(a, b, c, d, x[12], 3);  /* 13 */
		d = ff(d, a, b, c, x[13], 7);  /* 14 */
		c = ff(c, d, a, b, x[14], 11); /* 15 */
		b = ff(b, c, d, a, x[15], 19); /* 16 */

		//round 2
		a = gg(a, b, c, d, x[ 0], 3);  /* 17 */
		d = gg(d, a, b, c, x[ 4], 5);  /* 18 */
		c = gg(c, d, a, b, x[ 8], 9);  /* 19 */
		b = gg(b, c, d, a, x[12], 13); /* 20 */
		a = gg(a, b, c, d, x[ 1], 3);  /* 21 */
		d = gg(d, a, b, c, x[ 5], 5);  /* 22 */
		c = gg(c, d, a, b, x[ 9], 9);  /* 23 */
		b = gg(b, c, d, a, x[13], 13); /* 24 */
		a = gg(a, b, c, d, x[ 2], 3);  /* 25 */
		d = gg(d, a, b, c, x[ 6], 5);  /* 26 */
		c = gg(c, d, a, b, x[10], 9);  /* 27 */
	  	b = gg(b, c, d, a, x[14], 13); /* 28 */
	  	a = gg(a, b, c, d, x[ 3], 3);  /* 29 */
	  	d = gg(d, a, b, c, x[ 7], 5);  /* 30 */
	  	c = gg(c, d, a, b, x[11], 9);  /* 31 */
	  	b = gg(b, c, d, a, x[15], 13); /* 32 */

	  	//round 3
	  	a = hh(a, b, c, d, x[ 0], 3);  /* 33 */
	  	d = hh(d, a, b, c, x[ 8], 9);  /* 34 */
	  	c = hh(c, d, a, b, x[ 4], 11); /* 35 */
	  	b = hh(b, c, d, a, x[12], 15); /* 36 */
	  	a = hh(a, b, c, d, x[ 2], 3);  /* 37 */
	  	d = hh(d, a, b, c, x[10], 9);  /* 38 */
	  	c = hh(c, d, a, b, x[ 6], 11); /* 39 */
	  	b = hh(b, c, d, a, x[14], 15); /* 40 */
	  	a = hh(a, b, c, d, x[ 1], 3);  /* 41 */
	  	d = hh(d, a, b, c, x[ 9], 9);  /* 42 */
	  	c = hh(c, d, a, b, x[ 5], 11); /* 43 */
	  	b = hh(b, c, d, a, x[13], 15); /* 44 */
	  	a = hh(a, b, c, d, x[ 3], 3);  /* 45 */
	  	d = hh(d, a, b, c, x[11], 9);  /* 46 */
	  	c = hh(c, d, a, b, x[ 7], 11); /* 47 */
	  	b = hh(b, c, d, a, x[15], 15); /* 48 */

	  	contextState[0] += a;
	  	contextState[1] += b;
	  	contextState[2] += c;
	  	contextState[3] += d;

		//zeroize sensitive information
	  	Arrays.fill(x, 0);
	}
	
	/**
	 * Encodes an array of ints into a array of bytes entities.
	 * 
	 * @param pInput the array of ints.
	 * @param pOffset the offset to start from in the array of ints.
	 * @param pLength the number of ints to use, starting at <code>pOffset</code>.
	 * @return the array of encoded bytes
	 */
	private byte[] encode(int[] pInput, int pOffset, int pLength)
	{
		if (pOffset < 0 || pLength < 0 || ((pOffset + pLength) > pInput.length))
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		byte[] output = new byte[pLength * 4];
		
		for (int i = 0, j = 0; j <  (pLength * 4); i++, j += 4)
		{
			output[j] = (byte)(pInput[i] & 0xff);
			output[j + 1] = (byte)((pInput[i] >> 8) & 0xff);
			output[j + 2] = (byte)((pInput[i] >> 16) & 0xff);
			output[j + 3] = (byte)((pInput[i] >> 24) & 0xff);
		}
		
		return output;
	}
	
	/**
	 * Decodes an array of bytes into a array of int entities.
	 * 
	 * @param pInput the array of bytes.
	 * @param pOffset the offset to start from in the array of bytes.
	 * @param pLength the number of bytes to use, starting at <code>pOffset</code>.
	 * @return the array of decoded ints
	 */
	private int[] decode(byte[] pInput, int pOffset, int pLength)
	{
		if (pOffset < 0 || pLength < 0 || ((pOffset + pLength) > pInput.length))
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		if ((pLength % 4) != 0)
		{
			throw new java.lang.IllegalArgumentException("The value of the parameter pLength must be a multiple of 4.");
		}
		
	  
		int[] output = new int[pLength / 4];
	  
		for (int i = 0, j = 0; j < pLength; i++, j += 4)
		{
			output[i] = (pInput[pOffset + j] & 0xFF) 
	    			    | ((pInput[pOffset + j + 1] & 0xFF) << 8) 
	    			    | ((pInput[pOffset + j + 2] & 0xFF) << 16) 
	    			    | ((pInput[pOffset + j + 3] & 0xFF) << 24);
		}
	  
		return output;
	}

	
	/**
	 * Rotates x left n bits.
	 * 
	 * @param pX 32-bit register
	 * @param pN 32-bit register
	 * @return the rotated 32-bit register
	 */
	private int rotateLeft(int pX, int pN)
	{
		return ((pX << pN) | (pX >>> (32 - pN)));
	}
	
	/**
	 * Auxiliary function for transformations in round 1.
	 * 
	 * @param pA 32-bit register
	 * @param pB 32-bit register
	 * @param pC 32-bit register
	 * @param pD 32-bit register
	 * @param pX 32-bit register
	 * @param pS 32-bit register
	 * @return the transformed 32-bit register
	 */
    private int ff(int pA, int pB, int pC, int pD, int pX, int pS)
    {
	    int r = pA + ((pB & pC) | (~pB & pD)) + pX;
	    return rotateLeft(r, pS);
	}
	
    /**
     * Auxiliary function for transformations in round 2.
     * 
	 * @param pA 32-bit register
	 * @param pB 32-bit register
	 * @param pC 32-bit register
	 * @param pD 32-bit register
	 * @param pX 32-bit register
	 * @param pS 32-bit register
     * @return the transformed 32-bit register
     */
	private int gg(int pA, int pB, int pC, int pD, int pX, int pS)
	{
		int r = pA + ((pB & pC) | (pB & pD) | (pC & pD)) + pX + 0x5a827999;
	    return rotateLeft(r, pS);
	}
	
	/**
	 * Auxiliary function for transformations in round 3.
	 * 
	 * @param pA 32-bit register
	 * @param pB 32-bit register
	 * @param pC 32-bit register
	 * @param pD 32-bit register
	 * @param pX 32-bit register
	 * @param pS 32-bit register
	 * @return the transformed 32-bit register
	 */
	private int hh(int pA, int pB, int pC, int pD, int pX, int pS)
	{
		int r = pA + (pB ^ pC ^ pD) + pX + 0x6ed9eba1;
	    return rotateLeft(r, pS);
	}
	
}	// MD4MessageDigest

