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
 * 19.08.2009 - [JR] - creation
 * 06.10.2009 - [JR] - getCodeBase implemented
 * 17.02.2010 - [JR] - used WebstartImpl to avoid reflective JNLP calls 
 *                     (-> not possible to invoke reflective)
 * 25.08.2011 - [JR] - #465: access clipboard content                    
 */
package com.sibvisions.rad.ui;

import java.lang.reflect.Method;

import javax.rad.io.IFileHandle;

/**
 * The <code>Webstart</code> class allows encapsulated access to the javax.jnlp service
 * objects. It's safe to use the class within browser applets because the service detection
 * will be made with reflective calls.
 * 
 * @author René Jahn
 */
public final class Webstart
{
	/** the flag indicates whether the services are generally available. */
	private static boolean bAvailable;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		try
		{
			Class<?> clsServiceManager = Class.forName("javax.jnlp.ServiceManager");
			
			Method metLookup = clsServiceManager.getMethod("lookup", String.class);
			
			metLookup.invoke(null, "javax.jnlp.BasicService");
			metLookup.invoke(null, "javax.jnlp.FileOpenService");
			metLookup.invoke(null, "javax.jnlp.FileSaveService");
			metLookup.invoke(null, "javax.jnlp.ClipboardService");
			
			//JNLP application/applet started
			bAvailable = true;
		}
		catch (Throwable th)
		{
			//not jnlp
			bAvailable = false;
		}
	}
	
	/**
	 * Hidden constructor because <code>Webstart</code> is a utility class.
	 */
	private Webstart()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets if the jvm was started as JNLP webstart process.
	 * 
	 * @return <code>true</code> if started as JNLP, otherwise <code>false</code>
	 */
	public static boolean isJnlp()
	{
		return bAvailable;
	}
	
	/**
	 * Shows a document via the JNLP BasicService.
	 * 
	 * @param pDocumentName the name/path of the document
	 * @return <code>true</code> if the request succeded, otherwise <code>false</code>
	 * @throws Exception if the service execution is not possible
	 */
	public static boolean showDocument(String pDocumentName) throws Exception
	{
		checkJNLP();
		
		return WebstartImpl.showDocument(pDocumentName);
	}
	
	/**
	 * Opens a file chooser dialog via the JNLP FileOpenService.
	 * 
	 * @param pMultiSelection <code>true</code> to allowe multi file selection, <code>false</code> for single
	 *                        file selection
	 * @return the list of selected {@link IFileHandle}
	 * @throws Exception if the service execution is not possible
	 */
	public static IFileHandle[] showOpenDialog(boolean pMultiSelection) throws Exception
	{
		checkJNLP();

		return WebstartImpl.openFileDialog(pMultiSelection);
	}
	
	/**
	 * Opens a file save dialog with the JNLP FileSaveService.
	 * 
	 * @param pFileHandle the {@link IFileHandle} to store
	 * @throws Exception if the service execution is not possible
	 */
	public static void showSaveDialog(IFileHandle pFileHandle) throws Exception
	{
		checkJNLP();
		
		WebstartImpl.saveFileDialog(pFileHandle);
	}
	
	/**
	 * Gets the codebase from the JNLP BasicService.
	 * 
	 * @return the codebase or <code>null</code> if the codebase is not set
	 * @throws Exception if the service execution is not possible
	 */
	public static String getCodeBase() throws Exception
	{
		checkJNLP();
		
		return WebstartImpl.getCodeBase();
	}
	
	/**
	 * Sets the given text to the clipboard.
	 * 
	 * @param pText the text
	 * @throws Exception if clipboard access fails
	 */
	public static void setClipboard(String pText) throws Exception
	{
		checkJNLP();
		
		WebstartImpl.setClipboard(pText);
	}
	
	/**
	 * Gets the current text from the clipboard.
	 * 
	 * @return the text
	 * @throws Exception if clipboard access fails
	 */
	public static String getClipboard() throws Exception
	{
		checkJNLP();
		
		return WebstartImpl.getClipboard();
	}

	/**
	 * Set the value of a property through the PersistenceService.
	 * 
	 * @param pKey the property name
	 * @param pValue the value set
	 */
	public static void setProperty(String pKey, String pValue)
	{
		if (bAvailable)
		{
			WebstartImpl.setProperty(pKey, pValue);
		}
	}
	
	/**
	 * Gets the value of a property through the PersistenceService.
	 * 
	 * @param pKey the property name
	 * @return the value or <code>null</code> if the property was not found
	 */
	public static String getProperty(String pKey)
	{
		if (bAvailable)
		{
			return WebstartImpl.getProperty(pKey);
		}
		
		return null;
	}
	
	/**
	 * Checks if JNLP is available.
	 * 
	 * @throws Exception if JNLP is not available
	 * @see #isJnlp()
	 */
	private static void checkJNLP() throws Exception
	{
		if (!bAvailable)
		{
			throw new Exception("JNLP services are not available!");
		}
	}
	
} 	// Wwebstart
