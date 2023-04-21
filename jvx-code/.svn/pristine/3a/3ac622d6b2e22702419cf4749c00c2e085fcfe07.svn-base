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
 * 18.03.2009 - [JR] - creation
 * 10.10.2011 - [JR] - #479: open files on linux (kde, gnome)
 * 16.10.2013 - [JR] - removed quotes (didn't work on mac os)
 * 08.11.2017 - [JR] - fallback support (Fedora 26 problem with gnome-open)
 */
package com.sibvisions.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>FileViewer</code> opens files/documents with the platform dependent viewer
 * application.
 * 
 * @author René Jahn
 */
public final class FileViewer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>FileViewer</code> class is a utility class.
	 */
	private FileViewer()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens a document with the platform dependent viewer.
	 * 
	 * @param pDocument the document as File reference.
	 * @throws IOException if the viewer can not open the document
	 */
	public static void open(File pDocument) throws IOException
	{
		open(pDocument.getAbsolutePath());
	}
	
	/**
	 * Opens a document with the platform dependent viewer.
	 * 
	 * @param pDocumentName the absolute path to the document
	 * @throws IOException if the viewer can not open the document
	 */
	public static void open(String pDocumentName) throws IOException
	{
	    String documentNameLower = pDocumentName == null ? null : pDocumentName.toLowerCase();
	    
	    boolean isUrl = documentNameLower != null 
	                    && (documentNameLower.startsWith("http://") || documentNameLower.startsWith("https://") || documentNameLower.startsWith("file:/"));
	    boolean isMail = documentNameLower != null 
                		&& (documentNameLower.startsWith("mailto:"));
	    
	    Desktop desktop = Desktop.getDesktop();
	    
	    if (Desktop.isDesktopSupported() && desktop.isSupported(Action.OPEN) && !isUrl && !isMail)
	    {
	        try
	        {
	            desktop.open(new File(pDocumentName));
	        }
	        catch (Throwable ex)
	        {
	            throw new IOException("The document [" + pDocumentName + "] can not be opened!", ex);
	        }
	    }
	    else if (Desktop.isDesktopSupported() && desktop.isSupported(Action.BROWSE) && isUrl)
	    {
            try
            {
                desktop.browse(new URI(pDocumentName));
            }
            catch (Throwable ex)
            {
                throw new IOException("Browser with URL [" + pDocumentName + "] can not be opened!", ex);
            }
	    }
	    else if (Desktop.isDesktopSupported() && desktop.isSupported(Action.MAIL) && isMail)
	    {
	        try
	        {
	            desktop.mail(new URI(pDocumentName));
	        }
	        catch (Throwable ex)
	        {
	            throw new IOException("The document [" + pDocumentName + "] can not be opened!", ex);
	        }
	    }
	    else
	    {
    		String sOs = System.getProperty("os.name").toLowerCase(); 
    		
    		int iExitValue = -1;
    		
    		if (sOs.startsWith("windows"))
    		{
    			iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", "\"" + pDocumentName + "\""}));
    		}
    		else if (sOs.indexOf("linux") >= 0)
    		{
    			String sWM = System.getenv("GDMSESSION");
    			
    			if (sWM == null)
    			{
    				sWM = System.getenv("DESKTOP_SESSION");
    			}
    			
    			if (sWM == null)
    			{
    				throw new IOException("WindowManager detection failed!");
    			}
    			
    			sWM = sWM.toLowerCase();
    			
    			if (sWM.indexOf("gnome") >= 0)
    			{
    			    try
    			    {
    			        iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"gnome-open", pDocumentName}));
    			    }
    			    catch (IOException ioe)
    			    {
    			        LoggerFactory.getInstance(FileViewer.class).debug(ioe);
    			        
    			        try
    			        {
    			            iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"gio", "open", pDocumentName}));
    			        }
    			        catch (IOException ioex)
    			        {
                            LoggerFactory.getInstance(FileViewer.class).debug(ioex);
    
                            try
    			            {
    			                iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"gvfs-open", pDocumentName}));
    			            }
    			            catch (IOException ioexe)
    			            {
                                LoggerFactory.getInstance(FileViewer.class).debug(ioexe);
        			            
        			            iExitValue = -2;
    			            }
    			        }
    			    }
    			}
    			else if (sWM.indexOf("kde") >= 0)
    			{
    			    try
    			    {
    			        iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"kde-open", pDocumentName}));
    			    }
    			    catch (IOException ioe)
    			    {
    			        LoggerFactory.getInstance(FileViewer.class).debug(ioe);
    			        
    			        iExitValue = -2;
    			    }
    			}
    			
    			if (iExitValue != 0)
    			{
    				iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"xdg-open", pDocumentName}));
    			}
    		}
    		else if (sOs.indexOf("mac") >= 0)
    		{
    			iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"open", pDocumentName}));
    			
    			if (iExitValue != 0)
    			{
    				//try quotes
    				iExitValue = checkResult(Runtime.getRuntime().exec(new String[] {"open", "\"" + pDocumentName + "\""}));
    			}
    		}
    
    		if (iExitValue != 0)
    		{
    			throw new IOException("The document [" + pDocumentName + "] can not be opened!");
    		}
	    }
	}

	/**
	 * Checks command execution result.
	 * 
	 * @param pProcess the command process 
	 * @return the execution result. The value 0 means successful execution, all other values represents a failure
	 */
	private static int checkResult(Process pProcess)
	{
		try
		{
			return pProcess.waitFor();
		}
		catch (InterruptedException ie)
		{
			return -1;
		}
	}
	
}	//FileViewer
