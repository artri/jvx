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
 * 28.11.2008 - [RH] - creation
 * 18.10.2011 - [JR] - #487: set Content-Length
 * 22.12.2012 - [JR] - #614: use getRequestURL
 * 07.05.2013 - [JR] - #659: quote content-disposition
 */
package com.sibvisions.rad.server.http;

import java.io.IOException;
import java.util.Hashtable;

import javax.rad.io.IFileHandle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;

/**
 * It allows clients to download files from the {@link ObjectCache}.
 * under a given key.
 * 
 * @author Roland Hörmann
 */
public class DownloadServlet extends ResourceServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name of the download request page. */
	private static final String FILE_DOWNLOAD = "download.html";
	
	
	/** the URL parameter for the onload download option. */
	public static final String PARAM_ONLOAD = "ONLOAD";
	
	/** the URL parameter for the download button text. */
	public static final String PARAM_DOWNLOADBUTTON = "DOWNLOADBUTTON";
	
    /** the "no" content-disposition parameter. */
    public static final String PARAM_NO_CONTENT_DISPOSITION = "NO_CONTENT_DISPOSITION"; 
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getResourceDirectoryName()
	{
		return "download";
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, 
	                                                                                     IOException
	{
    	String key = pRequest.getParameter(PARAM_KEY);
    	
    	String sApplication = pRequest.getParameter(PARAM_APPLICATION); 
    	String sResource = pRequest.getParameter(PARAM_RESOURCE); 
    	
    	try
    	{
    		if (sResource != null)
	    	{
                sendFile(sApplication, sResource, null, pResponse);
	    		return;
	    	}
	    	else 
	    	{
	        	if (key == null) // There is no key specified.
	        	{
	        		pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
	        		return;
	        	}
	        	else
	        	{
	        		String sUserAgent = pRequest.getHeader("User-Agent");
	        		
	        		//only IE has a problem with content blocking, even if popup blocker is disabled
	        		if (sUserAgent != null 
	        			&& sUserAgent.indexOf(" MSIE ") >= 0 
	        			&& Boolean.parseBoolean(pRequest.getParameter(PARAM_ONLOAD)))
	        		{
    					String sUrl = getRequestURI(pRequest) + "?" + PARAM_KEY + "=" + key;
	    				
			        	String sTitle    = pRequest.getParameter(PARAM_TITLE); 
			    		String sInfoText = pRequest.getParameter(PARAM_INFOTEXT);
			    		String sUpload   = pRequest.getParameter(PARAM_DOWNLOADBUTTON);
			    		String sCancel   = pRequest.getParameter(PARAM_CANCELBUTTON);
			    		
			    		sApplication = CommonUtil.nvl(sApplication, "");
			    		sTitle    	 = CommonUtil.nvl(sTitle, "");
			    		sInfoText 	 = CommonUtil.nvl(sInfoText, "");
			    		sUpload   	 = CommonUtil.nvl(sUpload, "");
			    		sCancel   	 = CommonUtil.nvl(sCancel, "");
			    		
			    		Hashtable<String, String> htParams = new Hashtable<String, String>();
			    		htParams.put("URL", sUrl);
			    		htParams.put(PARAM_KEY, key);
			    		htParams.put(PARAM_APPLICATION, sApplication);
			    		htParams.put(PARAM_TITLE, sTitle);
			    		htParams.put(PARAM_INFOTEXT, sInfoText);
			    		htParams.put(PARAM_DOWNLOADBUTTON, sUpload);
			    		htParams.put(PARAM_CANCELBUTTON, sCancel);
			    		
			    		sendFile(sApplication, FILE_DOWNLOAD, htParams, pResponse);
			    		
	        			return;
	        		}
	        		else
	        		{
		        		IFileHandle tempFile = (IFileHandle)ObjectCache.get(key);
	
		    			if (tempFile == null)
		    			{
		    				pResponse.sendError(HttpServletResponse.SC_NO_CONTENT);
		    				return;
		    			}
		    			else
		    			{
                            pResponse.setContentType(getServletContext().getMimeType(tempFile.getFileName()));

                            if (!Boolean.parseBoolean(pRequest.getParameter(PARAM_NO_CONTENT_DISPOSITION)))
		    			    {
    		    				pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + tempFile.getFileName() + "\"");
    		    				pResponse.setHeader("Content-Length", "" + tempFile.getLength());
		    			    }
		    			    
		    				FileUtil.copy(tempFile.getInputStream(), true, pResponse.getOutputStream(), true);
		    			}
	        		}
	        	}
	    	}
		}
		catch (IOException ioe)
		{
			throw ioe;
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
	
} 	// DownloadServlet
