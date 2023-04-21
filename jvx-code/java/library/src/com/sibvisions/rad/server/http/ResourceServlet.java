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
 * 07.03.2011 - [JR] - creation
 * 22.12.2012 - [JR] - #614: request URL detection takes care of used client URL 
 * 17.05.2016 - [JR] - #1605: check resource access
 */
package com.sibvisions.rad.server.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ParameterParser;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>ResourceServlet</code> is a simple {@link HttpServlet} for accessing resource templates. 
 * 
 * @author René Jahn
 */
public abstract class ResourceServlet extends HttpServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the URL parameter for accessing the {@link com.sibvisions.util.ObjectCache}. */
	public static final String PARAM_KEY = "KEY";
	
	/** the URL parameter for accessing resource files. */
	public static final String PARAM_RESOURCE = "RESOURCE";
	
	/** the URL parameter for the upload button text. */
	public static final String PARAM_CANCELBUTTON = "CANCELBUTTON";

	/** the URL parameter for the application name. */
	public static final String PARAM_APPLICATION = "APPLICATION";

	/** the URL parameter for the title. */
	public static final String PARAM_TITLE = "TITLE";
	
	/** the URL parameter for the info text. */
	public static final String PARAM_INFOTEXT = "INFOTEXT";
	
	/** the connection URL from the client. */
	public static final String PARAM_CONURL = "CONURL";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of the resource directory.
	 * 
	 * @return the resource directory name
	 */
	protected abstract String getResourceDirectoryName();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the given content-disposition headers file name.
     * 
     * @param pContentDisposition The content-disposition headers value.
     * @return The file name
     */
    protected String getFileName(String pContentDisposition) 
    {
        String fileName = null;
        
        if (pContentDisposition != null) 
        {
            String cdl = pContentDisposition.toLowerCase();
            
            if (cdl.startsWith(FileUploadBase.FORM_DATA) || cdl.startsWith(FileUploadBase.ATTACHMENT)) 
            {
                ParameterParser parser = new ParameterParser();
                parser.setLowerCaseNames(true);
                
                // Parameter parser can handle null input
                Map<String, String> params = parser.parse(pContentDisposition, ';');
                
                fileName = params.get("filename");
                
                if (fileName != null) 
                {
                    fileName = fileName.trim();
                }
            }
        }
        return fileName;
    }
    
    /**
     * Gets the first FileItemStream inside a multipart upload.
     * 
     * @param pRequest the HttpServletRequest
     * @return the FileItemStream
     * @throws IOException if an IOException occurs.
     */
    protected FileItemStream getFileItemStream(HttpServletRequest pRequest) throws IOException
    {
		FileItemIterator items;
		
		try
		{
			items = new ServletFileUpload().getItemIterator(pRequest);
			
			while (items.hasNext())
			{
				FileItemStream fileItem = items.next();
			    if (!fileItem.isFormField()) 
			    {
			    	return fileItem;
			    }
			}	    
		}
		catch (FileUploadException pFileUploadException)
		{
			// File cannot be parsed!
		}
		
		return null;
    }
    
	/**
	 * Sends the content of a file to the client. If the file is a html file, then all found
	 * parameter-keys, e.g. [EID], from the file, will be replaced with the mapped parameters.
	 * If a parameter was not found the no replacement will be performed.
	 * 
	 * @param pApplication the application name
	 * @param pFile the name of the file to send
	 * @param pParams the key/value mapping for parameter replacement
	 * @param pResponse the http response
	 * @throws Exception if an error occurs during reading from the resource file, writing the response or accessing
	 *                   the configuration files
	 */
	protected void sendFile(String pApplication,
			                String pFile, 
			                Hashtable<String, String> pParams, 
				  	      	HttpServletResponse pResponse) throws Exception
	{
		String sResName = getResourceDirectoryName();
		
		String sPath;
		
		InputStream isResource = null;

		//searches the application config and if not defined, automatically searches the server config
		if (pApplication != null && pApplication.trim().length() > 0)
		{
			sPath = Configuration.getApplicationZone(pApplication).getProperty("/application/" + sResName + "/searchpath");

			//use the default value
			if (sPath == null)
			{
				sPath = "/com/sibvisions/rad/server/http/" + sResName + "/";
			}
			
			//add trailing separator
			if (!sPath.endsWith("/") && !sPath.endsWith("\\"))
			{
				sPath += "/";
			}

            if (!checkResourceAccess(pResponse, sPath, pFile))
			{
			    return;
			}
			
			isResource = ResourceUtil.getResourceAsStream(sPath + pFile);
		}
		
		//allow an user-defined resource path and if a file is not available, search
		//in the server resource path for the resource!
		if (isResource == null)
		{
			//try "only" the server configuration (without accessing through the application zone!
			
			sPath = Configuration.getServerZone().getProperty("/server/" + sResName + "/searchpath");

			//use the default value
			if (sPath == null)
			{
				sPath = "/com/sibvisions/rad/server/http/" + sResName + "/";
			}
			
			//add trailing separator
			if (!sPath.endsWith("/") && !sPath.endsWith("\\"))
			{
				sPath += "/";
			}

			if (!checkResourceAccess(pResponse, sPath, pFile))
			{
			    return;
			}
			
			isResource = ResourceUtil.getResourceAsStream(sPath + pFile);
		}
		
		if (isResource == null)
		{
			pResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else
		{
	        pResponse.setContentType(getServletContext().getMimeType(pFile));
		    
			OutputStream osResponse = pResponse.getOutputStream();
			
			try
			{
				if (pFile.toLowerCase().endsWith(".html"))
				{
	    			String sData = new String(FileUtil.getContent(isResource));

					String sParamName;
					String sParamValue;
					
					int iLast = 0;
					int iStart = 0;
					int iEnd;
					
					//search and replace all parameters
					iStart = sData.indexOf('['); 
					while (iStart >= 0)
					{
						iEnd = sData.indexOf(']', iStart + 1);
						
						if (iEnd >= 0)
						{
							osResponse.write(sData.substring(iLast, iStart).getBytes("UTF-8"));
							
							sParamName = sData.substring(iStart + 1, iEnd);
							sParamValue = null;
							
							//only replace mapped parameters 
							if (pParams != null)
							{
								sParamValue = pParams.get(sParamName);
								
								if (sParamValue != null)
								{
									osResponse.write(sParamValue.getBytes("UTF-8"));
								}
							}

							if (sParamValue == null)
							{
								osResponse.write('[');
								osResponse.write(sParamName.getBytes("UTF-8"));
								osResponse.write(']');
							}
							
							iLast  = iEnd + 1;
							iStart = sData.indexOf('[', iLast + 1);
						}
						else
						{
							iStart = -1;
						}
					}

					osResponse.write(sData.substring(iLast).getBytes("UTF-8"));
				}
				else
				{
					//don't replace parameters -> byte transfer
					FileUtil.copy(isResource, osResponse);
				}
			}
			finally
			{
				if (isResource != null)
				{
					isResource.close();
				}
				
				osResponse.flush();
				osResponse.close();
			}
		}
	}
	
	/**
	 * Gets the request URI that was used from the client to communicate to the server. It's not always
	 * possible to use the request URL from the http request because if mod_proxy was used, the URL
	 * may be different to the URL used from the client application! This method takes care of such 
	 * differences.
	 * 
	 * @param pRequest the client request
	 * @return the connection URL
	 */
	protected String getRequestURI(HttpServletRequest pRequest)
	{
		String sConUrl = pRequest.getParameter(PARAM_CONURL);
		
		//it's better to use the client URL, because it's possible that we are behind an apache web server
		//with mod_proxy instead of mod_jk. We use the "real" address, to avoid download problem
		if (sConUrl != null && sConUrl.trim().length() > 0)
		{
			String sReqUrl = pRequest.getRequestURL().toString();
			
			try
			{
				URL url = new URL(sConUrl);
				
				String sUrl = url.getProtocol() + "://" + url.getAuthority();
				
				//Use sent URL, if there is a difference!
				if (!sReqUrl.toLowerCase().startsWith(sUrl.toLowerCase()))
				{
					int iPos = sReqUrl.indexOf("://");
					
					iPos = sReqUrl.indexOf("/", iPos + 3);
					
					if (iPos >= 0)
					{
						return sUrl + sReqUrl.substring(iPos); 
					}
				}
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(getClass()).error(e);
			}
		}
		
		return pRequest.getRequestURL().toString();
	}
	
	/**
	 * Checks if resource/file access is allowed.
	 * 
	 * @param pResponse the http response
	 * @param pPath the (resource)path/directory
	 * @param pFile the file
	 * @return <code>true</code> if it's allowed to use the file, <code>false</code> otherwise
	 * @throws IOException if sending error status fails
	 */
	private boolean checkResourceAccess(HttpServletResponse pResponse, String pPath, String pFile) throws IOException
	{
	    //it's not allowed to access files by relative path
	    if (StringUtil.isEmpty(pFile)
	        || pFile.trim().startsWith(".") 
	        || pFile.trim().startsWith("/") 
	        || pFile.trim().startsWith("\\")
	        || pFile.indexOf("/../") >= 0
	        || pFile.indexOf("/./") >= 0)
	    {
            pResponse.sendError(HttpServletResponse.SC_FORBIDDEN, pFile);

            return false;
	    }
	    
	    //it's not allowed to access files directly
	    File fi = new File(pPath + pFile);
	    
	    try
	    {
    	    if (fi.exists() || fi.getCanonicalFile().exists())
    	    {
                pResponse.sendError(HttpServletResponse.SC_FORBIDDEN, pFile);

                return false;
    	    }
	    }
	    catch (IOException ioe)
	    {
            pResponse.sendError(HttpServletResponse.SC_FORBIDDEN, pFile);

            return false;
	    }
	    
	    return true;
	}
	
} 	// ResourceServlet
