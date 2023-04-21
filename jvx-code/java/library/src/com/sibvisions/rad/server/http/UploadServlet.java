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
 * 11.02.2009 - [JR] - creation
 * 14.02.2009 - [JR] - implemented: stylesheet, 1x1 image, disable buttons on submit
 * 14.02.2009 - [JR] - disable form buttons after submit (otherwise the file will not be transfered)
 * 16.02.2009 - [JR] - use templates from the application or server config
 * 22.04.2009 - [JR] - used ERROR_PLAIN and ERROR_ENCODED
 * 05.08.2009 - [JR] - support PARAM_CLOSE
 * 06.08.2009 - [JR] - search resource files and read the path from the application configuration
 *                   - doGet: avoid NullPointerExcptions [BUGFIX]
 * 18.08.2009 - [JR] - allow missing/empty APPLICATION parameter
 * 26.08.2009 - [JR] - default path for: /application/upload/searchpath
 * 07.09.2009 - [JR] - set correct MimeType    
 * 07.05.2010 - [JR] - #124: get filename with FileUtil 
 * 05.10.2010 - [JR] - #169: don't remove extensions
 * 06.06.2016 - [JR] - #1606: connection-id parameter introduced                
 */
package com.sibvisions.rad.server.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.rad.io.RemoteFileHandle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sibvisions.rad.server.AbstractSession;
import com.sibvisions.rad.server.Server;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.Zone;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>UploadServlet</code> handles the upload of files via http
 * connection. A client is able transmit a file as octet stream. 
 * stream.
 * 
 * @author René Jahn
 */
public class UploadServlet extends ResourceServlet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Internal Cancel detection. */
	private static final String INTERNAL_CANCEL = "INTERNAL_CANCEL";
	
	/** the name of the upload request page. */
	private static final String FILE_UPLOAD = "upload.html";

	/** the name of the upload response page. */
	private static final String FILE_RESULT = "result.html";

	
	/** the URL parameter for waiting and aborting. */
	public static final String PARAM_WAIT = "WAIT";
	
	/** the URL parameter for closing the window. */
	public static final String PARAM_CLOSE = "CLOSE";

	/** the URL parameter for the upload button text. */
	public static final String PARAM_UPLOADBUTTON = "UPLOADBUTTON";
	
	/** the URL parameter for the webstart info. */
	public static final String PARAM_WEBSTART = "WEBSTART";

    /** the URL parameter for the connection id. */
    public static final String PARAM_CONNECTION_ID = "CONNECTION_ID";

    /** the directory which contains the resource files for the client. */
	public static final String UPLOADDIR = "/upload/";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getResourceDirectoryName()
	{
		return "upload";
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException,
	                                                                                     IOException
	{
        String conid = pRequest.getParameter(PARAM_CONNECTION_ID);

        if (StringUtil.isEmpty(conid))
        {
            pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        else
        {
            try
            {
                Server.getInstance().getSessionManager().get(CodecUtil.decodeHex(conid));
            }
            catch (Exception see)
            {
                pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        
        String key = pRequest.getParameter(PARAM_KEY);
    	String wait = pRequest.getParameter(PARAM_WAIT); 
    	
    	if ("true".equals(wait))
    	{
        	if (key == null)
        	{
    			pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
    			return;
        	}
        	else
        	{
	        	pResponse.setContentType("application/octet-stream");
				
				DataOutputStream out = new DataOutputStream(pResponse.getOutputStream());
	    		try
	    		{
	    			RemoteFileHandle tempFile = (RemoteFileHandle)ObjectCache.get(key);
	    			int cycles = 50;
	    			while (tempFile == null && cycles > 0)
	    			{
	    				Thread.sleep(100);
	    				tempFile = (RemoteFileHandle)ObjectCache.get(key);
	    				cycles--;
	    			}

	    			if (tempFile == null)
	    			{
                        throw new InterruptedException();
	    			}
	    			else
	    			{
    	    			synchronized (tempFile)
    	    			{
    	        			if (tempFile.getFileName() == null)
    	        			{
    	        				tempFile.wait(5000);
    	        			}
    	    			}
    	    			if (tempFile.getFileName() == INTERNAL_CANCEL)
    	    			{
    	    				ObjectCache.remove(key);
    	    				
    	    				throw new InterruptedException();
    	    			}
    	    			else if (tempFile.getFileName() == null)
    	    			{
    	        			out.writeUTF("WAIT");
    	    			}
    	    			else
    	    			{
    	        			out.writeUTF("FILENAME");
    	        			out.writeUTF(tempFile.getFileName());
    	    			}
	    			}
	    			
	    			out.flush();
	    			out.close();
	    			
	    			return;
	    		}
	    		catch (Exception ex)
	    		{
	    			out.writeUTF("CANCEL");
	    			out.flush();
	    			out.close();
	    		}
        	}
    	}
    	else if ("false".equals(wait))
    	{
        	if (key == null)
        	{
    			pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
    			return;
        	}
        	else
        	{
        		RemoteFileHandle tempFile = (RemoteFileHandle)ObjectCache.get(key);
	    		if (tempFile != null)
	    		{
	    			synchronized (tempFile)
	    			{
	    				tempFile.setFileName(INTERNAL_CANCEL);
		    			tempFile.notifyAll();
	    			}
	    		}
	    		
	    		if ("true".equals(pRequest.getParameter(PARAM_CLOSE)))
				{
	    			StringBuilder sb = new StringBuilder();
	    			sb.append("<html><script language=\"javascript\" type=\"text/javascript\">");
	    			sb.append("function closeMe() {window.clearTimeout(timer); self.close();} ");
	    			sb.append("timer = window.setTimeout('closeMe()', 70);");
	    			sb.append("</script><body></body></html>");
	    			
	    			byte[] byClose = sb.toString().getBytes();
	
	    			pResponse.setContentType("text/html");
	    			pResponse.setContentLength(byClose.length);
	    			
	    			//don't use a template for this!
	    			pResponse.getOutputStream().write(byClose);
	    			pResponse.flushBuffer();
	    			
	    			return;
				}
	    	}
    	}
    	else
    	{
    		if ("Y".equals(pRequest.getParameter(PARAM_WEBSTART)))
    		{
    			//Webstart Modus
    			//resend request as openWindow

    			//remove the WEBSTART parameter!
    			String sParams = pRequest.getQueryString().replace(PARAM_WEBSTART + "=Y&", ""); 
    			

    			//general check
    			if (sParams.indexOf(")") >= 0
    				|| sParams.indexOf("\"") >= 0
    				|| sParams.indexOf("'") >= 0
    				|| sParams.indexOf(">") >= 0)
    			{
	    			pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    			return;
    			}
    			else
    			{
    				String[] sValues;
    				
    				for (Enumeration<String> enNames = pRequest.getParameterNames(); enNames.hasMoreElements();)
    				{
    					sValues = pRequest.getParameterValues(enNames.nextElement());
    					
    					if (sValues != null)
    					{
    						//check decoded values as well
    						for (int i = 0; i < sValues.length; i++)
    						{
    							if (sValues[i].indexOf("'") >= 0
    								|| sValues[i].indexOf("\"") >= 0
    								|| sValues[i].indexOf(")") >= 0
    								|| sValues[i].indexOf(">") >= 0)
    							{
					    			pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
					    			return;
    							}
    						}
    					}
    				}
    			}
    			
    			StringBuilder sb = new StringBuilder();
    			sb.append("<html><script language=\"javascript\" type=\"text/javascript\">");
    			sb.append("");
    			sb.append("</script><body onLoad=\"javascript:window.open('" + getRequestURI(pRequest) + "?" + sParams + "','" + 
    					                                                  pRequest.getParameter(PARAM_TITLE) + 
    					                                                  "', 'status=no,location=no,menubar=no," +
    					                                                      "toolbar=no,dependent=yes,resizable=yes');\"></body></html>");
    			
    			byte[] byClose = sb.toString().getBytes();

    			pResponse.setContentType("text/html");
    			pResponse.setContentLength(byClose.length);
    			
    			//don't use a template for this!
    			pResponse.getOutputStream().write(byClose);
    			pResponse.flushBuffer();
    			
    			return;
    		}
    		else
    		{
    			//Standard mode
		    	String sApplication = pRequest.getParameter(PARAM_APPLICATION);
		    	String sResource = pRequest.getParameter(PARAM_RESOURCE); 
		     
		    	try
		    	{
                    if (sResource != null)
			    	{
                        Hashtable<String, String> htParams = new Hashtable<String, String>();
                        htParams.put(PARAM_CONNECTION_ID, CommonUtil.nvl(conid, ""));

                        sendFile(sApplication, sResource, htParams, pResponse);
			    		return;
			    	}
			    	else 
			    	{
			        	if (key == null)
			        	{
                            pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			    			return;
			        	}
			        	else
			        	{
				        	String sTitle    = pRequest.getParameter(PARAM_TITLE); 
				    		String sInfoText = pRequest.getParameter(PARAM_INFOTEXT);
				    		String sUpload   = pRequest.getParameter(PARAM_UPLOADBUTTON);
				    		String sCancel   = pRequest.getParameter(PARAM_CANCELBUTTON);
				    		
				    		sApplication = CommonUtil.nvl(sApplication, "");
				    		sTitle    	 = CommonUtil.nvl(sTitle, "");
				    		sInfoText 	 = CommonUtil.nvl(sInfoText, "");
				    		sUpload   	 = CommonUtil.nvl(sUpload, "");
				    		sCancel   	 = CommonUtil.nvl(sCancel, "");
				    		
					    	RemoteFileHandle tempFile = new RemoteFileHandle(null, key);
				    		ObjectCache.put(key, tempFile, tempFile.getTimeout());
			                
				    		Hashtable<String, String> htParams = new Hashtable<String, String>();
				    		htParams.put(PARAM_KEY, key);
				    		htParams.put(PARAM_APPLICATION, sApplication);
				    		htParams.put(PARAM_TITLE, sTitle);
				    		htParams.put(PARAM_INFOTEXT, sInfoText);
				    		htParams.put(PARAM_UPLOADBUTTON, sUpload);
				    		htParams.put(PARAM_CANCELBUTTON, sCancel);
				    		htParams.put(PARAM_CONNECTION_ID, conid);
				    		
				    		sendFile(sApplication, FILE_UPLOAD, htParams, pResponse);
				    		return;
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
    	}
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException,
	                                                                                      IOException
	{
        String conid = pRequest.getParameter(PARAM_CONNECTION_ID);

        String sConId;
        
        if (!StringUtil.isEmpty(conid))
        {
            try
            {
                sConId = CodecUtil.decodeHex(conid);
            }
            catch (Exception e)
            {
                pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        else
        {
            sConId = null;
        }
        
        AbstractSession sess = null;
        
        if (sConId != null)
        {
        	try
        	{
        		sess = Server.getInstance().getSessionManager().get(sConId);
        		sess.lock(true);
        	}
        	catch (Exception see)
        	{
                pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
        	}
        }
        
        try
        {
	        String key = pRequest.getParameter(PARAM_KEY);
	
	        // There is no key specified -> required
	    	if (key == null)
	    	{
	    		pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    		return;
	    	}
	
	        Object cacheObject = ObjectCache.get(key);
	
	        if (sConId == null && !(cacheObject instanceof RemoteFileHandle && ((RemoteFileHandle)cacheObject).getLength() < 0))
	        {            
	            pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }
	
	    	if ("application/octet-stream".equals(pRequest.getContentType()))
	    	{
	    		//used from SwingApplet.handleDownload!!
	    		
	    		String fileName = getFileName(pRequest.getHeader("Content-Disposition"));
	    		String sAppName = pRequest.getParameter(PARAM_APPLICATION);
	    				
    			if (!checkFileName(sAppName, fileName))
    			{
	    			pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    			return;
    			}
    			
    		    RemoteFileHandle tempFile;
    		    if (cacheObject instanceof RemoteFileHandle)
    		    {
    		        tempFile = (RemoteFileHandle)ObjectCache.get(key);
    		    }
    		    else
    		    {
    		        tempFile = new RemoteFileHandle(fileName, key);
    		        tempFile.setTimeout(30 * 60 * 1000); // 30 minutes
    		    }
        		
    		    copy(pRequest.getInputStream(), tempFile, sAppName);
	    	}    	
	    	else if (ServletFileUpload.isMultipartContent(pRequest))
	    	{
				RemoteFileHandle tempFile = (RemoteFileHandle)ObjectCache.get(key);
				
				if (tempFile != null)
				{
		    		FileItemStream fileItem = getFileItemStream(pRequest);
		    		
	    			try
	    			{
		    			synchronized (tempFile)
		    			{
		    				String fileName = fileItem.getName();
		    				String sAppName = pRequest.getParameter(PARAM_APPLICATION);
		    				
		    				if (!checkFileName(sAppName, fileName))
		    				{
		    					tempFile.setFileName(INTERNAL_CANCEL);
		    				}
		    				else
		    				{
			    				tempFile.setFileName(fileName);
			    				
			    				copy(fileItem.openStream(), tempFile, sAppName);
		    				}
		    				
		        			tempFile.notifyAll();
		    			}
	    			}
	    			catch (Exception ex)
	    			{
	    				synchronized (tempFile)
		    			{
	        				tempFile.setFileName(INTERNAL_CANCEL);
	    					tempFile.notifyAll();
		    			}
	    			}
				}
				
				try
				{
			        String application = pRequest.getParameter(PARAM_APPLICATION);
	
					sendFile(application, FILE_RESULT, null, pResponse);
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
	    	else
	    	{
				pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
	    	}
        }
        finally
        {
        	if (sess != null)
        	{
        		sess.unlock(true);
        	}
        }
	}
    
    /**
     * Checks whether the filename is supported.
     * 
     * @param pApplicationName the application name
     * @param pFileName the file name
     * @return <code>true</code> if the file extension is supported
     */
    protected boolean checkFileName(String pApplicationName, String pFileName)
    {
    	if (StringUtil.isEmpty(pFileName))
    	{
    		return false;
    	}
    	else
    	{
    		List<String> liExtensions;
    		
    		String sExtensions;
    		
			try
			{
	    		if (!StringUtil.isEmpty(pApplicationName))
	    		{
    				Zone zone = Configuration.getApplicationZone(pApplicationName);
    				
    				sExtensions = zone.getProperty("/application/" + getResourceDirectoryName() + "/extensions");
	    		}
	    		else
	    		{
    				Zone zone = Configuration.getServerZone();
    				
					sExtensions = zone.getProperty("/server/" + getResourceDirectoryName() + "/extensions");
	    		}
	    		
				liExtensions = StringUtil.separateList(StringUtil.upperCase(sExtensions), ",", true);
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(getClass()).error(e);
				
				return false;
			}
			
			if (liExtensions != null && liExtensions.size() > 0)
			{
				String sExtension = StringUtil.upperCase(FileUtil.getExtension(pFileName));
				
				if (StringUtil.isEmpty(sExtension))
				{
					return false;
				}
				
				return liExtensions.indexOf(sExtension) >= 0;
			}
    		
    		return true;
    	}
    }
    
    /**
     * Copies the upload content into the temporary "file". This method takes care of maximum file size.
     * 
     * @param pStream the content stream
     * @param pFile the output file
     * @param pApplicationName the application name (if set)
     * @throws ServletException if an unexpected error occurs
     * @throws IOException if copy operation fails or configured max size reached
     */
    protected void copy(InputStream pStream, RemoteFileHandle pFile, String pApplicationName) throws ServletException,
     																								 IOException
    {
    	if (pStream == null)
    	{
    		pFile.setContent((InputStream)null);
    	}
    	else
    	{
    		String sSize = null;
    		
    		try
    		{
	    		if (!StringUtil.isEmpty(pApplicationName))
	    		{
					Zone zone = Configuration.getApplicationZone(pApplicationName);
					
					sSize = zone.getProperty("/application/" + getResourceDirectoryName() + "/maxsize");
	    		}
	    		else
	    		{
					Zone zone = Configuration.getServerZone();
					
					sSize = zone.getProperty("/server/" + getResourceDirectoryName() + "/maxsize");
	    		}
    		}
    		catch (Exception e)
    		{
    			if (e instanceof IOException)
    			{
    				throw (IOException)e;
    			}
    			else
    			{
    				throw new ServletException(e);
    			}
    		}
    		
    		long lMaxSize;
    		
    		if (StringUtil.isEmpty(sSize))
    		{
    			lMaxSize = -1;
    		}
    		else
    		{
    			try
    			{
    				lMaxSize = Long.parseLong(sSize);
    			}
    			catch (Exception e)
    			{
    				LoggerFactory.getInstance(getClass()).error(e);
    				
    				lMaxSize = -1;
    			}
    		}
    		
			byte[] byContent = new byte[4096];
			
			OutputStream out = pFile.getOutputStream();

			try
			{
				long length = 0;
				int iLen;
	
				while ((iLen = pStream.read(byContent)) >= 0)
				{
					length += iLen;

					if (lMaxSize <= 0 || length <= lMaxSize)
					{
						out.write(byContent, 0, iLen);
					}
					else
					{
						LoggerFactory.getInstance(getClass()).error("Upload of " + pFile.getFileName() + " failed because " + length + 
								                                    "bytes reached max size of " + lMaxSize + " bytes!");
						
						throw new IOException("Maximum size for content reached!");
					}
				}
		
				out.flush();
			}
			finally
			{
			    CommonUtil.close(pStream, out);
			}
			
			LoggerFactory.getInstance(getClass()).info("Upload of " + pFile.getFileName() + " with " + pFile.getLength() + " bytes was successful!");
    	}
    }
    
} 	// UploadServlet
