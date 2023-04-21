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
 * 12.12.2011 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sibvisions.rad.server.http.rest.service.jackson.FileHandleSerializer;

/**
 * The <code>JSONUtil</code> is an internal utility class for REST handling.
 * 
 * @author René Jahn
 */
public final class JSONUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the internal module. */
    private static SimpleModule module = null;
    
	/** the object mapper. */
	private static ObjectMapper mapper = createObjectMapper();
	
	/** the pretty-format mapper. */
	private static ObjectMapper ompPretty = null;
	
	/** the dump stream (default: System.out). */
	private static PrintStream stream = System.out;
	
	/** whether the JSON stream should be dumped. */
	private static boolean bDump = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>BeanUtil</code> is a utility
	 * class.
	 */
	private JSONUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new {@link ObjectMapper} with preconfigured settings.
	 * 
	 * @return the object mapper
	 */
	public static ObjectMapper createObjectMapper()
	{
		ObjectMapper omap = new ObjectMapper();
		
		configureObjectMapper(omap);

		return omap;
	}
	
	/**
	 * Configures the given object mapper.
	 * 
	 * @param pMapper the mapper
	 */
	public static void configureObjectMapper(ObjectMapper pMapper)
	{
        pMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        pMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        pMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        pMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //not by default
        //enableArrays(pMapper);
        
        if (module == null)
        {
            module = new SimpleModule(JSONUtil.class.getName(), new Version(1, 0, 0, null, null, null));
            module.addSerializer(new FileHandleSerializer());
        }
        
        pMapper.registerModule(module);
	}
	
	/**
	 * Configures the given object mapper to return Java arrays <code>Object[]</code> instead of <code>List</code>s.
	 * 
	 * @param pMapper the object mapper
	 * @return the configured objexct mapper
	 */
	public static ObjectMapper enableArrays(ObjectMapper pMapper)
	{
		pMapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
		
		return pMapper;
	}
	
	/**
	 * Gets the result object from a representation, using default object mapper.
	 * 
	 * @param pRepresentation the object representation
	 * @return the object from the representation
	 * @throws IOException if object creation fails
	 */
	public static Object getObject(Representation pRepresentation) throws IOException
	{
		return getObject(pRepresentation, Object.class, mapper);
	}
	
	/**
	 * Gets the result object from a content, using default object mapper.
	 * 
	 * @param pContent the content
	 * @return the object from the representation
	 * @throws IOException if object creation fails
	 */
	public static Object getObject(byte[] pContent) throws IOException
	{
		return getObject(pContent, Object.class, mapper);
	}

	/**
	 * Gets the result object from a representation, using default object mapper.
	 * 
	 * @param <T> the result object type
	 * @param pRepresentation the object representation
	 * @param pClass the expected class type
	 * @return the object from the representation
	 * @throws IOException if object creation fails
	 */
	public static <T> T getObject(Representation pRepresentation, Class pClass) throws IOException
	{
		return (T)getObject(pRepresentation, pClass, mapper);
	}
	
	/**
	 * Gets the result object from a representation, using default object mapper.
	 * 
	 * @param <T> the result object type
	 * @param pContent the content
	 * @param pClass the expected class type
	 * @return the content
	 * @throws IOException if object creation fails
	 */
	public static <T> T getObject(byte[] pContent, Class pClass) throws IOException
	{
		return (T)getObject(pContent, pClass, mapper);
	}

	/**
	 * Gets the result object from a representation, using a custom object mapper.
	 * 
	 * @param <T> the result object type
	 * @param pRepresentation the object representation
	 * @param pClass the expected class type
	 * @param pMapper the object mapper for the object deserialization
	 * @return the object from the representation
	 * @throws IOException if object creation fails
	 */
	public static <T> T getObject(Representation pRepresentation, Class pClass, ObjectMapper pMapper) throws IOException
	{
		if (pRepresentation == null)
		{
			return null;
		}
		
		// the standard would be UTF_8
		if (pRepresentation.getCharacterSet() == null)
		{
			pRepresentation.setCharacterSet(CharacterSet.UTF_8);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pRepresentation.write(baos);

		return (T)getObject(baos.toByteArray(), pClass, pMapper);
	}
	
	/**
	 * Gets the result object from a content, using a custom object mapper.
	 * 
	 * @param <T> the result object type
	 * @param pContent the content
	 * @param pClass the expected class type
	 * @param pMapper the object mapper for the object deserialization
	 * @return the object from the representation
	 * @throws IOException if object creation fails
	 */
	public static <T> T getObject(byte[] pContent, Class pClass, ObjectMapper pMapper) throws IOException
	{
		if (pContent != null && pContent.length > 0)
		{
			T result = (T)pMapper.readValue(pContent, pClass);
			
			if (bDump)
			{
				stream.println(pMapper.writer().withDefaultPrettyPrinter().writeValueAsString(result));
			}
			
			return result;
		}
		else
		{
			return null;
		}		
	}
	
	/**
	 * Sets whether the JSON stream should be dumped.
	 * 
	 * @param pDump <code>true</code> to enable dumps, <code>false</code> otherwise
	 */
	public static void setDumpStreamEnabled(boolean pDump)
	{
		bDump = pDump;
	}
	
	/**
	 * Gets whether JSTOM stream dumping is enabled.
	 * 
	 * @return <code>true</code> if stream dumping is enabled, <code>false</code> otherwise
	 */
	public static boolean isDumpStreamEnabled()
	{
		return bDump;
	}

	/**
	 * Sets the dump stream.
	 * 
	 * @param pStream the stream or <code>null</code> to reset the stream to its default value (System.out)
	 */
	public static void setDumpStream(PrintStream pStream)
	{
		if (pStream != null)
		{
			stream = pStream;
		}
		else
		{
			stream = System.out;
		}
	}
	
	/**
	 * Gets the dump stream.
	 * 
	 * @return the stream
	 */
	public static PrintStream getDumpStream()
	{
		return stream;
	}
	
	/**
	 * Formats the given object as pretty JSON string.
	 * 
	 * @param pObject the object
	 * @return the formatted string or <code>pJson</code> if formatting fails
	 */
	public static String prettyJson(Object pObject)
	{
		try
		{
			if (ompPretty == null)
			{
				ompPretty = JSONUtil.createObjectMapper();
			}
			
			if (pObject instanceof String)
			{
				String sValue = ((String)pObject).trim();
				
				if ((sValue.startsWith("{") && sValue.endsWith("}"))
					|| sValue.startsWith("[") && sValue.endsWith("]")) 
				{
					Object jsonObject = ompPretty.readValue((String)pObject, Object.class);
				
					return ompPretty.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
				}
			}
			
			return ompPretty.writerWithDefaultPrettyPrinter().writeValueAsString(pObject);
		}
		catch (Throwable th)
		{
			return pObject.toString();
		}
	}
	
	/**
	 * Formats the given object as pretty JSON string.
	 * 
	 * @param pRepresentation the representation
	 * @return the formatted string or <code>pJson</code> if formatting fails
	 * @throws IOException if converting representation to json fails
	 */
	public static String prettyJson(Representation pRepresentation) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		pRepresentation.write(baos);

		if (pRepresentation.getMediaType() == MediaType.APPLICATION_JSON)
		{
			return prettyJson(new String(baos.toByteArray()));
		}
		else
		{
			return new String(baos.toByteArray());
		}
	}
	
}	// JSONUtil
