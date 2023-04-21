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
 * 01.10.2008 - [JR] - creation
 * 13.11.2010 - [JR] - specific DATE values
 */
package http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SerializerServlet</code> is a simple servlet which reads objects from the 
 * request and writes the read object to the response. The bytes read from the request
 * will be set as hex string into the response header <code>X-INPUT</code> and the string
 * representation of the value will be set to the response header <code>X-INPUT-VALUE</code>. 
 * The response will be set as hex string into the response header <ocde>X-OUTPUT</code>.
 * The specify a specific {@link ISerializer} implementation, you have to use the request
 * header <code>X-SERIALIZER</code>. Without a specific serializer an exception will be
 * thrown and written to the response.
 * <p>
 * When the request doesn't contain data, the response will contain some test objects written
 * with the specified serializer and the response header contains <code>X-EXCEPTION</code>.
 * 
 * @author René Jahn
 */
public class SerializerServlet extends HttpServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the log instance. */
	private ILogger log = LoggerFactory.getInstance(getClass());
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException
	{
		try
		{
			System.out.println("doGet " + CodecUtil.encodeHex(FileUtil.getContent(pRequest.getInputStream())));
		}
		catch (IOException ioe)
		{
			throw new ServletException(ioe);
		}
	}
	
    @Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException
	{
    	System.out.println("doPost");
    	log.debug("Received POST request!");
    	
    	ISerializer serializer;
    	
    	try
    	{
    		//detect serializer
	    	String sClass = pRequest.getHeader("X-SERIALIZER");
	    	String sType  = pRequest.getHeader("X-TYPE");
	    	
	    	serializer = (ISerializer)Class.forName(sClass).newInstance();
	    	
			ByteArrayOutputStream baosOutput = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baosOutput);

			if (sType == null || sType.trim().length() == 0)
	    	{
				log.debug("  - NO specific type");
				
		    	//read from the client
				byte[] byInput = FileUtil.getContent(pRequest.getInputStream());
				
				pResponse.setContentType("application/octet-stream");
	
				Object object = null;
				
				if (byInput.length > 0)
				{
					log.debug("  - X-INPUT = ", CodecUtil.encodeHex(byInput));
					
					pResponse.setHeader("X-INPUT", CodecUtil.encodeHex(byInput));
	
					DataInputStream disInput = new DataInputStream(new ByteArrayInputStream(byInput));
	
			    	object = serializer.read(disInput);
					
					pResponse.setHeader("X-INPUT-VALUE", StringUtil.toString(object));
					
					log.debug("  - X-INPUT-VALUE = ", StringUtil.toString(object));
				}
				
				if (byInput.length > 0)
				{
					serializer.write(out, object);
				}
	    	}
			else
			{
				log.debug("  - specific type ", sType);
				
				if ("DATE_03_01_0001".equals(sType))
				{
					GregorianCalendar cal = new GregorianCalendar();
					cal.set(Calendar.DAY_OF_MONTH, 3);
					cal.set(Calendar.MONTH, Calendar.JANUARY);
					cal.set(Calendar.YEAR, 1);
					cal.set(Calendar.HOUR_OF_DAY, 1);
					
					serializer.write(out, new Timestamp(cal.getTimeInMillis()));
				}
				else if ("DATE_01_01_1582".equals(sType))
				{
					GregorianCalendar cal = new GregorianCalendar();
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.MONTH, Calendar.JANUARY);
					cal.set(Calendar.YEAR, 1582);
					cal.set(Calendar.HOUR_OF_DAY, 1);
					
					serializer.write(out, new Timestamp(cal.getTimeInMillis()));
				}
				else if ("DATE_03_01_500".equals(sType))
				{
					GregorianCalendar cal = new GregorianCalendar();
					cal.set(Calendar.DAY_OF_MONTH, 3);
					cal.set(Calendar.MONTH, Calendar.JANUARY);
					cal.set(Calendar.YEAR, 500);
					
					serializer.write(out, new Timestamp(cal.getTimeInMillis()));
				}
				else if ("STRING".equals(sType))
				{
					serializer.write(out, "äöüß ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz");
				}
				else if ("DOUBLE".equals(sType))
				{
					serializer.write(out, new Double(2.5d));
				}
				else if ("DOUBLE_NEG".equals(sType))
				{
					serializer.write(out, new Double(-15.5d));
				}
				else if ("FLOAT".equals(sType))
				{
					serializer.write(out, new Float(0.5f));
				}
				else if ("FLOAT_NEG".equals(sType))
				{
					serializer.write(out, new Float(-5.5f));
				}
				else if ("DECIMAL".equals(sType))
				{
					serializer.write(out, new BigDecimal(12345));
				}
				else if ("DECIMAL_NEG".equals(sType))
				{
					serializer.write(out, new BigDecimal(-9999));
				}
				else if ("DECIMAL_NK".equals(sType))
				{
					serializer.write(out, new BigDecimal("12345.6789"));
				}
				else if ("LIST".equals(sType))
				{
					serializer.write(out, new ArrayUtil<String>("First", "Second", "Third"));
				}
				else if ("CONDITION".equals(sType))
				{
					ICondition filter = new LikeIgnoreCase("FIRSTNAME", "*e*").or(
										new LikeIgnoreCase("LASTNAME", "*f*").or(
										new LikeIgnoreCase("STREET", "*g*").or(
										new LikeIgnoreCase("TOWN", "*h*")))); 
					
					serializer.write(out, filter);
				}
			}
			
			byte[] byOutput = baosOutput.toByteArray();
			
			pResponse.setHeader("X-OUTPUT", CodecUtil.encodeHex(byOutput));
			
			log.debug("  - X-OUTPUT = ", CodecUtil.encodeHex(byOutput));
			
			pResponse.getOutputStream().write(byOutput);
			
			out.flush();
			
			log.debug("Response sent!");
    	}
    	catch (Exception e)
    	{
    		log.debug(e);
    		
    		try
    		{
    			pResponse.setContentType("application/octet-stream");

    			pResponse.setHeader("X-EXCEPTION", "");
    			pResponse.getOutputStream().write(ExceptionUtil.dump(e, true).getBytes());
    		}
    		catch (IOException ioe)
    		{
    			throw new ServletException(ioe);
    		}
    	}
	}	
	
}	// SerializerServlet
