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
 * 08.04.2009 - [JR] - creation
 * 15.06.2009 - [JR] - encodeURL replaced with encodeURLPart
 *                     decodeURLPart implemented
 * 23.06.2009 - [JR] - implemented encodeHex with byte[] as parameter   
 * 02.08.2009 - [JR] - encodeURLPart: UTF-8 encoding      
 * 03.08.2009 - [JR] - encodeURLPart: ISO-8859-1 (as defined in RFC. If UTF-8, then the application server e.g. 
 *                     tomcat has to use URIEncoding="UTF-8")
 * 08.03.2012 - [JR] - encodeHex(InputStream) implemented                                  
 */
package com.sibvisions.util.type;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;

import com.sibvisions.util.OrderedHashtable;

/**
 * The <code>CodecUtil</code> contains methods for encode and decode operations.
 * 
 * @author René Jahn
 */
public final class CodecUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mapping for special html character enc/decoding. */ 
    private static OrderedHashtable<String, String> htHtmlMapping = new OrderedHashtable<String, String>();

    /** the mapping for special xml character enc/decoding. */ 
    private static OrderedHashtable<String, String> htXmlMapping = new OrderedHashtable<String, String>();

    /** The base 64 characters for RFC 2045 (and RFC 4648 §4). */
    private static final char[] TO_BASE64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    /** The base 64 characters for URL encoding RFC 4648 §5. */
    private static final char[] TO_BASE64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };
    /** The reverse map for base64 normal and url encoding. */
    private static final int[] FROM_BASE64 = new int[256];
    /** The line feed sequence RFC 2045. */
    private static final byte[] CRLF = new byte[] {'\r', '\n'};
    /** The max line length RFC 2045. */
    private static final int MIMELINEMAX = 76;

    static 
    {
        Arrays.fill(FROM_BASE64, -1);
        for (int i = 0; i < TO_BASE64.length; i++)
        {
            FROM_BASE64[TO_BASE64[i]] = i;
        }
        for (int i = TO_BASE64URL.length - 2; i < TO_BASE64URL.length; i++)
        {
            FROM_BASE64[TO_BASE64URL[i]] = i;
        }
        FROM_BASE64['='] = -2;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
    	//special order is important for specia characters
        htHtmlMapping.put("&", "&amp;");
        //not supported of XML parser
        //htHtmlMapping.put("/", "&frasl;");
        //standard characters
        htHtmlMapping.put("\"", "&quot;");
        //don't replace -> often used and shouldn't be a problem
        //htHtmlMapping.put("'", "&#39;");
        htHtmlMapping.put(String.valueOf((char)0x00A7), "&sect;");
        htHtmlMapping.put(String.valueOf((char)0x00A9), "&copy;");
        htHtmlMapping.put(String.valueOf((char)0x00AE), "&reg;");
        htHtmlMapping.put(String.valueOf((char)0x00BB), "&raquo;");
        htHtmlMapping.put(String.valueOf((char)0x00AB), "&laquo;");
        htHtmlMapping.put(String.valueOf((char)0x00B9), "&sup1;");
        htHtmlMapping.put(String.valueOf((char)0x00B2), "&sup2;");
        htHtmlMapping.put(String.valueOf((char)0x00B3), "&sup3;");
        htHtmlMapping.put(String.valueOf((char)0x00BC), "&frac14;");
        htHtmlMapping.put(String.valueOf((char)0x00BD), "&frac12;");
        htHtmlMapping.put(String.valueOf((char)0x00BF), "&iquest;");
        htHtmlMapping.put(String.valueOf((char)0x00A1), "&iexcl;");
        htHtmlMapping.put("<", "&lt;");
        htHtmlMapping.put(">", "&gt;");
        htHtmlMapping.put("ö", "&ouml;");
        htHtmlMapping.put("ü", "&uuml;");
        htHtmlMapping.put("ä", "&auml;");
        htHtmlMapping.put("Ö", "&Ouml;");
        htHtmlMapping.put("Ü", "&Uuml;");
        htHtmlMapping.put("Ä", "&Auml;");
        htHtmlMapping.put("ß", "&szlig;");

        htXmlMapping.put("&", "&amp;");
        htXmlMapping.put("\"", "&quot;");
        htXmlMapping.put("<", "&lt;");
        htXmlMapping.put(">", "&gt;");
    }
    
    /**
     * Invisible constructor because <code>CodecUtil</code> is a utility
     * class.
     */
    private CodecUtil()
    {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Encodes the part for an URL with UTF-8 encoding.
     * 
     * @param pPart the url part
     * @return the encoded part
     */
    public static String encodeURLPart(String pPart)
    {
        return encodeURLPart(pPart, "ISO-8859-1");
    }

    /**
     * Encodes the part for an URL with UTF-8 encoding.
     * 
     * @param pPart the url part
     * @param pEncoding the encoding
     * @return the encoded part
     */
    public static String encodeURLPart(String pPart, String pEncoding)
    {
        StringBuffer sbfUrl = new StringBuffer();
        
        int iChar;

        try
        {
            byte[] by = pPart.getBytes(pEncoding);
            
            for (int i = 0, anz = by.length; i < anz; i++)
            {
                iChar = by[i];
                
                if ((iChar >= 'a' && iChar <= 'z')
                    || (iChar >= 'A' && iChar <= 'Z')
                    || (iChar >= '0' && iChar <= '9')
                    || iChar == '$'
                    || iChar == '-'
                    || iChar == '_'
                    || iChar == '.'
                    || iChar == '!'
                    || iChar == '\''
                    || iChar == '('
                    || iChar == ')'
                    || iChar == ','
                    || iChar == '/')
                {
                    sbfUrl.append((char)iChar);
                }
                else
                {
                    sbfUrl.append('%');
                    sbfUrl.append(String.valueOf(Integer.toHexString(((iChar & 0xff) + 256))).substring(1));
                }
            }
            
            return sbfUrl.toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Encodes a plain value for an URL query parameter with ISO-8859-1.
     * 
     * @param pValue the plain query parameter value
     * @return the encoded value or <code>null</code> if ISO-8859-1 is not supported
     */
    public static String encodeURLParameter(String pValue)
    {
        return encodeURLParameter(pValue, "ISO-8859-1");
    }
    
    /**
     * Encodes a plain value for an URL query parameter with a specific charset.
     * 
     * @param pValue the plain query parameter value
     * @param pEncoding the charset e.g. UTF-8
     * @return the encoded value or <code>null</code> if <code>pEncoding</code> is not supported
     */
    public static String encodeURLParameter(String pValue, String pEncoding)
    {
        try
        {
            return URLEncoder.encode(pValue, pEncoding);
        }
        catch (UnsupportedEncodingException use)
        {
            return null;
        }
    }
    
    /**
     * Decodes the part of an URL with UTF-8.
     * 
     * @param pURL the url part
     * @return the decoded part
     */
    public static String decodeURLPart(String pURL)
    {
        try
        {
            String sHex;
            
            char ch;
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            for (int i = 0, anz = pURL.length(); i < anz;)
            {
                ch = pURL.charAt(i);

                i++;
                
                if (ch == '%')
                {
                    sHex = pURL.substring(i, i + 2);
                    
                    i += 2;

                    baos.write((byte)((Integer.parseInt(sHex, 16) - 256) & 0xff));
                }
                else
                {
                    baos.write(ch);
                }
            }
            
            baos.flush();
            baos.close();
            
            return new String(baos.toByteArray(), "ISO-8859-1");
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Decodes an encoded value from an URL query parameter with ISO-8859-1.
     * 
     * @param pValue the encoded query parameter value
     * @return the plain value or <code>null</code> if ISO-8859-1 is not supported
     */
    public static String decodeURLParameter(String pValue)
    {
        return decodeURLParameter(pValue, "ISO-8859-1");
    }
    
    /**
     * Decodes an encoded value from an URL query parameter with a specific charset.
     * 
     * @param pValue the encoded query parameter value
     * @param pEncoding the charset e.g. UTF-8
     * @return the plain value or <code>null</code> if <code>pEncoding</code> is not supported
     */
    public static String decodeURLParameter(String pValue, String pEncoding)
    {
        try
        {
            return URLDecoder.decode(pValue, pEncoding);
        }
        catch (UnsupportedEncodingException use)
        {
            return null;
        }
    }
    
    /**
     * Encodes a text to raw html.
     * 
     * @param pText the text
     * @return the html encoded text
     */
    public static String encodeHtml(String pText)
    {
        if (pText == null)
        {
            return null;
        }

        String sOld;
        String sNew;
        
		StringBuilder sbReplaced = new StringBuilder(pText);
		
		int index;
		
		boolean bReplace;

		for (Entry<String, String> entry : htHtmlMapping.entrySet())
        {
        	sOld = entry.getKey();
        	sNew = entry.getValue();
        	
    		index = sbReplaced.indexOf(sOld);
    		
    		if (index >= 0)
    		{
	    		do
	    		{
	        		bReplace = true;
	        		
	        		if ("/".equals(sOld))
	    			{
	        			int pos = sbReplaced.lastIndexOf("<", index - 1);
	        			
	    				if (pos >= 0)
	    				{
	    					if (pos == index - 1)
	    					{
	    						bReplace = false;
	    					}
	    					else
	    					{
	    						String sFiller = sbReplaced.substring(pos + 1, index);
	    						
	    						//only whitespaces between < and / 
	    						//e.g. <  /close>
	    						//e.g. stlye = 'test'
	    						if (sFiller.trim().length() == 0)
	    						{
	    							bReplace = false;
	    						}
	    					}
	    				}
	    			}
	    			
	        		if (bReplace)
	        		{
		    			sbReplaced.replace(index, index + sOld.length(), sNew);

		    			index = sbReplaced.indexOf(sOld, index + sNew.length());
	        		}
	        		else
	        		{
	        			index = sbReplaced.indexOf(sOld, index + sOld.length());
	        		}
	    			
	    		}
	    		while (index >= 0);
    		}
        }
        
        return sbReplaced.toString();
    }

    /**
     * Encodes a text to raw xml.
     * 
     * @param pText the text
     * @return the xml encoded text
     */
    public static String encodeXml(String pText)
    {
        if (pText == null)
        {
            return null;
        }

        String sText = pText;
        
        for (Entry<String, String> entry : htXmlMapping.entrySet())
        {
            sText = sText.replace(entry.getKey(), entry.getValue());
        }
        
        return sText;
    }

   /**
     * Decodes raw html to text.
     * 
     * @param pHtml the html code
     * @return the text
     */
    public static String decodeHtml(String pHtml)
    {
        return decodeIntern(htHtmlMapping, pHtml);
    }

    /**
     * Decodes raw xml to text.
     * 
     * @param pXml the xml code
     * @return the text
     */
    public static String decodeXml(String pXml)
    {
        return decodeIntern(htXmlMapping, pXml);
    }

    /**
     * Decodes raw encoded text to text.
     * 
     * @param pMap the map
     * @param pEncodedText the encoded text
     * @return the text
     */
    public static String decodeIntern(Hashtable<String, String> pMap, String pEncodedText)
    {
        if (pEncodedText == null)
        {
            return null;
        }

        String sResult = pEncodedText;
        
        for (Entry<String, String> entry : pMap.entrySet())
        {
            sResult = sResult.replace(entry.getValue(), entry.getKey());
        }
        
        return sResult;
    }
    
    /**
     * Encodes a text to a hex encoded string.
     * 
     * @param pText the plain text
     * @return the encoded text
     * @throws UnsupportedEncodingException if utf-8 encoding is not supported
     */
    public static String encodeHex(String pText) throws UnsupportedEncodingException
    {
        if (pText == null)
        {
            return null;
        }
        else
        {
            return encodeHex(pText.getBytes("UTF-8"));
        }
    }
    
    /**
     * Encodes a list of bytes to a hex encoded string.
     * 
     * @param pContent the list of bytes
     * @return the encoded text
     */
    public static String encodeHex(byte[] pContent)
    {
        if (pContent == null)
        {
            return null;
        }

        StringBuilder sbEncoded = new StringBuilder();
        
        for (int i = 0, anz = pContent.length; i < anz; i++)
        {
            sbEncoded.append(String.valueOf(Integer.toHexString(((pContent[i] & 0xff) + 256))).substring(1));
        }

        return sbEncoded.toString();
    }
    
    /**
     * Encodes a stream to a hex encoded string.
     * 
     * @param pStream the stream
     * @return the encoded text
     * @throws IOException if stream reading fails
     */
    public static String encodeHex(InputStream pStream) throws IOException
    {
        BufferedInputStream bis;
        
        boolean bAutoClose;
        
        if (pStream instanceof BufferedInputStream)
        {
            bAutoClose = false;
                    
            bis = (BufferedInputStream)pStream;
        }
        else
        {
            bAutoClose = true;
            
            bis = new BufferedInputStream(pStream);             
        }
        
        try
        {
            byte[] byTmp = new byte[8192];
    
            int iLen;
            
            StringBuilder sbEncoded = new StringBuilder();
            
            while ((iLen = bis.read(byTmp)) >= 0)
            {
                for (int i = 0; i < iLen; i++)
                {
                    sbEncoded.append(String.valueOf(Integer.toHexString(((byTmp[i] & 0xff) + 256))).substring(1));
                }
            }
            
            return sbEncoded.toString();
        }
        finally
        {
            if (bAutoClose)
            {
                try
                {
                    bis.close();
                }
                catch (Exception e)
                {
                    //nothing to be done
                }
            }
        }
    }

    /**
     * Decodes a hex, utf-8 encoded text to a string.
     * 
     * @param pHex the encoded text
     * @return the plain text
     * @throws UnsupportedEncodingException if utf-8 encoding is not supported
     */
    public static String decodeHex(String pHex) throws UnsupportedEncodingException
    {
        if (pHex == null)
        {
            return null;
        }

        return new String(decodeHexAsBytes(pHex), "UTF-8");
    }
    
    /**
     * Decodes a hex, utf-8 encoded text to a string.
     * 
     * @param pHex the encoded text
     * @return the plain text
     * @throws UnsupportedEncodingException if utf-8 encoding is not supported
     */
    public static byte[] decodeHexAsBytes(String pHex) throws UnsupportedEncodingException
    {
        if (pHex == null)
        {
            return null;
        }
        
        byte[] byDecoded = new byte[pHex.length() / 2];
        
        for (int i = 0, j = 0, anz = pHex.length(); i < anz; i += 2, j++)
        {
            byDecoded[j] = (byte)(Integer.parseInt(pHex.substring(i, i + 2), 16));
        }
        
        return byDecoded;
    }
    
    /**
     * Estimates the result length of encoded array.
     * 
     * @param pSrc the content
     * @param pDoPadding if padding should be done
     * @param pIsMime if it is converted for mime
     * @return the estimated length
     */
    private static int estimateEncodeLength(byte[] pSrc, boolean pDoPadding, boolean pIsMime) 
    {
        int len;
        if (pDoPadding) 
        {
            len = 4 * ((pSrc.length + 2) / 3);
        } 
        else 
        {
            int n = pSrc.length % 3;
            len = 4 * (pSrc.length / 3) + (n == 0 ? 0 : n + 1);
        }
        if (pIsMime)
        {
            len += (len - 1) / MIMELINEMAX * CRLF.length;
        }
        return len;
    }

    /**
     * Encodes bytes as base64 byte array RFC 2045 and RFC 4648.
     * 
     * @param pSrc the content
     * @param pIsMime for RFC 2045
     * @param pDoPadding to fill up with =
     * @param pUrl for RFC 4648 §5
     * @return the encoded byte array
     */
    public static byte[] encode(byte[] pSrc, boolean pIsMime, boolean pDoPadding, boolean pUrl) 
    {
        int len = estimateEncodeLength(pSrc, pDoPadding, pIsMime);
        byte[] dst = new byte[len];
        
        char[] base64 = pUrl ? TO_BASE64URL : TO_BASE64;
        int srcPos = 0;
        int srcLen = pSrc.length / 3 * 3;
        int srcMimeLen = srcLen;
        if (pIsMime && srcMimeLen > MIMELINEMAX / 4 * 3)
        {
            srcMimeLen = MIMELINEMAX / 4 * 3;
        }
        int dstPos = 0;
        while (srcPos < srcLen) 
        {
            int srcTempLen = Math.min(srcPos + srcMimeLen, srcLen);
            for (int srcTempPos = srcPos, dstTempPos = dstPos; srcTempPos < srcTempLen;) 
            {
                int bits = (pSrc[srcTempPos++] & 0xff) << 16 
                         | (pSrc[srcTempPos++] & 0xff) <<  8 
                         | (pSrc[srcTempPos++] & 0xff);
                dst[dstTempPos++] = (byte)base64[(bits >>> 18) & 0x3f];
                dst[dstTempPos++] = (byte)base64[(bits >>> 12) & 0x3f];
                dst[dstTempPos++] = (byte)base64[(bits >>> 6)  & 0x3f];
                dst[dstTempPos++] = (byte)base64[bits & 0x3f];
            }
            int dlen = (srcTempLen - srcPos) / 3 * 4;
            dstPos += dlen;
            srcPos = srcTempLen;
            if (pIsMime && dlen == MIMELINEMAX && srcPos < pSrc.length) 
            {
                for (byte b : CRLF)
                {
                    dst[dstPos++] = b;
                }
            }
        }
        if (srcPos < pSrc.length) 
        {
            int bits = pSrc[srcPos++] & 0xff;
            dst[dstPos++] = (byte)base64[bits >> 2];
            if (srcPos == pSrc.length) 
            {
                dst[dstPos++] = (byte)base64[(bits << 4) & 0x3f];
                if (pDoPadding) 
                {
                    dst[dstPos++] = '=';
                    dst[dstPos++] = '=';
                }
            }
            else 
            {
                int b1 = pSrc[srcPos++] & 0xff;
                dst[dstPos++] = (byte)base64[(bits << 4) & 0x3f | (b1 >> 4)];
                dst[dstPos++] = (byte)base64[(b1 << 2) & 0x3f];
                if (pDoPadding) 
                {
                    dst[dstPos++] = '=';
                }
            }
        }
        
        if (dstPos != dst.length)
        {
            return Arrays.copyOf(dst, dstPos);
        }
        return dst;
    }

    /**
     * Encodes bytes as base64 byte array RFC 4648 §4.
     * 
     * @param pSrc the content
     * @return the base64 byte array
     */
    public static byte[] encodeBase64Bytes(byte[] pSrc) 
    {
        return encode(pSrc, false, true, false);
    }

    /**
     * Encodes bytes as base64 byte array url conform RFC 4648 §5.
     * 
     * @param pSrc the content
     * @return the base64 byte array
     */
    public static byte[] encodeBase64UrlBytes(byte[] pSrc) 
    {
        return encode(pSrc, false, true, true);
    }

    /**
     * Encodes bytes as base64 string for mime RFC 2045.
     * 
     * @param pSrc the content
     * @return the base64 byte array
     */
    public static byte[] encodeMime(byte[] pSrc) 
    {
        return encode(pSrc, true, true, false);
    }

    /**
     * Encodes bytes as base64 byte array RFC 4648 §4.
     * 
     * @param pSrc the content
     * @return the base64 string
     */
    public static String encodeBase64(byte[] pSrc)
    {
        return new String(encodeBase64Bytes(pSrc));
    }

    /**
     * Encodes bytes as base64 byte array url conform RFC 4648 §5.
     * 
     * @param pSrc the content
     * @return the base64 string
     */
    public static String encodeBase64Url(byte[] pSrc)
    {
        return new String(encodeBase64UrlBytes(pSrc));
    }

    /**
     * Encodes bytes as base64 string for mime RFC 2045.
     * 
     * @param pSrc the content
     * @return the base64 string
     */
    public static String encodeBase64Mime(byte[] pSrc)
    {
        return new String(encodeMime(pSrc));
    }

    /**
     * Estimates the result length of decoded array.
     * 
     * @param pSrc the base64 content
     * @param pLineFeedCount the line feed sequence length in base64 content.
     * @return the estimated length
     */
    private static int estimateDecodeLength(byte[] pSrc, int pLineFeedCount) 
    {
        int len = pSrc.length;
        if (len == 0)
        {
            return 0;
        }
        if (len < 2) 
        {
            throw new IllegalArgumentException("Input should at least have 2 bytes");
        }
        int checkLen = len;
        if (pLineFeedCount > 0) // isMime 
        {
            // No Scan of bytes, we just want correct base64 data.
            int lineCount = len / (MIMELINEMAX + pLineFeedCount);
            
            if (len % (MIMELINEMAX + pLineFeedCount) == 0)
            {
                checkLen -= pLineFeedCount;
            }
            len -= lineCount * pLineFeedCount;
        }
        while (checkLen > 0 && pSrc[checkLen - 1] == '=')
        {
            checkLen--;
            len--;
        }
        
        return 3 * ((len + 3) / 4);
    }

    /**
     * Decodes base64 bytes as byte array RFC 2045 and RFC 4648.
     * Mime format and url encoding is automatically detected.
     * 
     * @param pSrc the base64 byte array
     * @return the decoded byte array
     */
    public static byte[] decodeBase64(byte[] pSrc) 
    {
        int lineFeedCount;
        if (pSrc.length > MIMELINEMAX + 1 && pSrc[MIMELINEMAX] == CRLF[0] && pSrc[MIMELINEMAX + 1] == CRLF[1])
        {
            lineFeedCount = 2;
        }
        else if (pSrc.length > MIMELINEMAX && pSrc[MIMELINEMAX] == CRLF[1])
        {
            lineFeedCount = 1;
        }
        else
        {
            lineFeedCount = 0;
        }
        boolean isMIME = lineFeedCount > 0;

        byte[] result = new byte[estimateDecodeLength(pSrc, lineFeedCount)];

        int srcPos = 0;
        int dstPos = 0;
        int bits = 0;
        int shiftto = 18;
        while (srcPos < pSrc.length) 
        {
            int b = FROM_BASE64[pSrc[srcPos++] & 0xff];
            if (b < 0) 
            {
                if (b == -2) 
                {   
                    if ((shiftto == 6 && (srcPos == pSrc.length || pSrc[srcPos++] != '=')) || shiftto == 18) 
                    {
                        throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit");
                    }
                    break;
                }
                if (isMIME)
                {
                    continue;
                }
                else
                {
                    throw new IllegalArgumentException("Illegal base64 character " + Integer.toString(pSrc[srcPos - 1], 16));
                }
            }
            bits |= (b << shiftto);
            shiftto -= 6;
            if (shiftto < 0) 
            {
                result[dstPos++] = (byte)(bits >> 16);
                result[dstPos++] = (byte)(bits >>  8);
                result[dstPos++] = (byte)(bits);
                shiftto = 18;
                bits = 0;
            }
        }
        if (shiftto == 6) 
        {
            result[dstPos++] = (byte)(bits >> 16);
        }
        else if (shiftto == 0) 
        {
            result[dstPos++] = (byte)(bits >> 16);
            result[dstPos++] = (byte)(bits >>  8);
        }
        else if (shiftto == 12) 
        {
            throw new IllegalArgumentException("Last unit does not have enough valid bits");
        }
        while (srcPos < pSrc.length) 
        {
            if (isMIME && FROM_BASE64[pSrc[srcPos++]] < 0)
            {
                continue;
            }
            throw new IllegalArgumentException("Input byte array has incorrect ending byte at " + srcPos);
        }
        
        if (dstPos != result.length) 
        {
            result = Arrays.copyOf(result, dstPos);
        }

        return result;
    }

    /**
     * Decodes base64 string as byte array RFC 2045 and RFC 4648.
     * Mime format and url encoding is automatically detected.
     * 
     * @param pSrc the base64 string
     * @return the decoded byte array
     */
    public static byte[] decodeBase64(String pSrc) 
    {
        try
        {
            return decodeBase64(pSrc.getBytes("ISO-8859-1"));
        }
        catch (UnsupportedEncodingException e)
        {
            return decodeBase64(pSrc.getBytes());
        }
    }
    
    
}   // CodecUtil
