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
 * 06.08.2009 - [JR] - HttpSession inactive synchronization
 * 07.10.2009 - [JR] - Cookie support through session properties
 * 13.11.2009 - [JR] - #8
 *                     don't set empty cookis as session property
 * 25.01.2011 - [JR] - remove cookie if value is null      
 * 15.10.2013 - [JR] - inactive interval of ISession is now in seconds               
 */
package com.sibvisions.rad.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sibvisions.rad.server.IRequest;
import com.sibvisions.rad.server.IResponse;
import com.sibvisions.rad.server.Server;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.type.CodecUtil;

/**
 * The <code>ServletServer</code> handles the communication via http
 * from remote clients.
 * 
 * @author René Jahn
 */
public class ServletServer extends HttpServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** request server. */
	private Server server;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public void init()
	{
		server = Server.getInstance();
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public void destroy()
	{
        if (server != null)
        {
            try
            {
                server.stop();
            }
            finally
            {
                server = null;
            }
        }
        else
        {
            ThreadHandler.stop();
        }
	}

	/** 
	 * Handles object requests via http protocol. 
	 * 
	 * @param pRequest standard http servlet request
	 * @param pResponse standard http servlet response
	 * @throws ServletException if the object request contains invalid information
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException
	{
        HttpContext ctxt = new HttpContext(pRequest, pResponse);
        
		try
		{
	        pResponse.setContentType("application/octet-stream");

	        long lAccessTime = System.currentTimeMillis();

			ISession session = server.process(new HttpRequest(pRequest), new HttpResponse(pResponse));
			
			if (session != null)
			{
				HttpSession sessHttp = pRequest.getSession(false);
				
				if (sessHttp != null)
				{
					if (session.isInactive(lAccessTime) || !session.isAlive(lAccessTime))
					{
						sessHttp.invalidate();
					}
					else
					{
						sessHttp.setMaxInactiveInterval(session.getMaxInactiveInterval());
					}
				}
			}
		}
		catch (ServletException se)
		{
			throw se;
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		finally
		{
		    ctxt.release();
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>HttpRequest</code> encapsulates the access to the
	 * <code>HttpServletRequest</code>. It's needed for the remote server
	 * implementation.
	 * 
	 * @author René Jahn
	 */
	private static final class HttpRequest implements IRequest
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the original servlet request. */
		private HttpServletRequest request;
		
		/** the request relevant properties. */
		private Hashtable<String, Object> htProp;
		
		/** whether the request was closed. */
		private boolean bClosed;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>HttpRequest</code> for a
		 * http request.
		 * 
		 * @param pRequest the origin http request
		 */
		private HttpRequest(HttpServletRequest pRequest)
		{
			this.request = pRequest;
			
			htProp = new Hashtable<String, Object>();
			
			String sProp = request.getAuthType();
			
			if (sProp != null)
			{
				htProp.put(IRequest.PROP_AUTHENTICATION_TYPE, sProp);
			}
			
			sProp = request.getRemoteUser();
			
			if (sProp != null)
			{
				htProp.put(IRequest.PROP_REMOTE_USER, sProp);
			}
			
			htProp.put(IRequest.PROP_REMOTE_ADDRESS, request.getRemoteAddr());
			htProp.put(IRequest.PROP_REMOTE_HOST, request.getRemoteHost());
			htProp.put(IRequest.PROP_URI, request.getRequestURI());
			
			Cookie[] cookie = pRequest.getCookies();

			if (cookie != null)
			{
				for (int i = 0, anz = cookie.length; i < anz; i++)
				{
					if (cookie[i].getName().startsWith("0x"))
					{
						String sValue;
						
						try
						{
							if (cookie[i].getValue().startsWith("0x"))
							{
								sValue = CodecUtil.decodeHex(cookie[i].getValue().substring(2));
							}
							else
							{
								sValue = cookie[i].getValue();
							}

							if (sValue != null && sValue.length() > 0)
							{
								htProp.put(IRequest.PROP_COOKIE + "." + CodecUtil.decodeHex(cookie[i].getName().substring(2)), 
										   sValue);
							}
						}
						catch (Exception ex)
						{
							htProp.put(IRequest.PROP_COOKIE + "." + cookie[i].getName(), cookie[i].getValue());
						}
					}
					else
					{
						htProp.put(IRequest.PROP_COOKIE + "." + cookie[i].getName(), cookie[i].getValue());
					}
				}
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public InputStream getInputStream() throws IOException
		{
		    if (isClosed())
		    {
		        throw new IOException("Stream is closed!");
		    }
		    
		    return request.getInputStream();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public Object getProperty(String pKey)
		{
			return htProp.get(pKey);
		}

		/**
		 * {@inheritDoc}
		 */
		public Hashtable<String, Object> getProperties()
		{
			return htProp;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void close()
		{
		    bClosed = true;
		}
		
        /**
         * {@inheritDoc}
         */
        public boolean isClosed()
        {
            return bClosed;
        }
        
	}	// HttpRequest
	
	/**
	 * The <code>HttpResponse</code> encapsulates the access to the
	 * <code>HttpServletResponse</code>. It's needed for the remote server
	 * implementation.
	 * 
	 * @author René Jahn
	 */
	private static final class HttpResponse implements IResponse
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the cookie identifier for session properties. */
		private static final String COOKIE = IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_REQUEST + IRequest.PROP_COOKIE;

		/** the original servlet response. */
		private HttpServletResponse response;
		
		/** whehter the response is closed. */
		private boolean bClosed;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>HttpResponse</code> for a
		 * http response.
		 * 
		 * @param pResponse the origin http response
		 */
		private HttpResponse(HttpServletResponse pResponse)
		{
			this.response = pResponse;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public OutputStream getOutputStream() throws IOException
		{
		    if (isClosed())
		    {
		        throw new IOException("Stream is closed!");
		    }
		    
			return response.getOutputStream();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setProperty(String pKey, Object pValue)
		{
			if (pKey.startsWith(COOKIE) 
				&& (pValue == null || pValue instanceof String))
			{
				try
				{
					Cookie cookie;
					
					if (pValue == null)
					{
						cookie = new Cookie("0x" + CodecUtil.encodeHex(pKey.substring(COOKIE.length() + 1)), "");
						cookie.setMaxAge(0);
					}
					else
					{
						cookie = new Cookie("0x" + CodecUtil.encodeHex(pKey.substring(COOKIE.length() + 1)), "0x" + CodecUtil.encodeHex((String)pValue));
						cookie.setMaxAge(30758400);
					}
					
					cookie.setPath("/");
					
					response.addCookie(cookie);
				}
				catch (UnsupportedEncodingException use)
				{
					//nothing to be done
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void close()
		{
		    bClosed = true;
		}
		
        /**
         * {@inheritDoc}
         */
        public boolean isClosed()
        {
            return bClosed;
        }
		
	}	// HttpResponse
	
}	// ServletServer
