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
 * 17.02.2010 - [JR] - creation
 * 25.08.2011 - [JR] - #465: access clipboard content 
 * 28.08.2013 - [JR] - #782: set/getProperty replaces slashes with underscores
 */
package com.sibvisions.rad.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jnlp.BasicService;
import javax.jnlp.ClipboardService;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;

import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * The <code>WebstartImpl</code> class encapsulates the access to JNLP services. It's a simple
 * wrapper class to support the same code base for desktop applications, JNLP applets/applications and
 * Browser applets. It's not possible to use JNLP services through with reflective calls because
 * the security manager throws an exception.
 * 
 * @author René Jahn
 */
final class WebstartImpl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** plain text flavor. */
	private static final DataFlavor FLAVOR_TEXT = createDataFlavor("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");

	/** java.lang.String flavor. */
	private static final DataFlavor FLAVOR_STRING = createDataFlavor(java.lang.String.class, "Unicode String");
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Hidden constructor because <code>Webstart</code> is a utility class.
	 */
	private WebstartImpl()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a {@link DataFlavor} for the given mimetype.
	 * 
	 * @param pMimeType the mimetype
	 * @param pHumanPresentableName the human-readable string used to identify this flavor
	 * @return the {@link DataFlavor}
	 */
	private static DataFlavor createDataFlavor(String pMimeType, String pHumanPresentableName)
	{
        try 
        {
            return new DataFlavor(pMimeType, pHumanPresentableName);
        } 
        catch (Exception e) 
        {
            return null;
        }		
	}
	
	/**
	 * Creates a {@link DataFlavor} for the given class.
	 * 
	 * @param pClass the class
	 * @param pHumanPresentableName the human-readable string used to identify this flavor
	 * @return the {@link DataFlavor}
	 */
	private static DataFlavor createDataFlavor(Class<?> pClass, String pHumanPresentableName)
	{
        try 
        {
            return new DataFlavor(java.lang.String.class, "Unicode String");
        } 
        catch (Exception e) 
        {
            return null;
        }		
	}
	
	/**
	 * Shows a document via the JNLP BasicService.
	 * 
	 * @param pURL the name/path of the document
	 * @return <code>true</code> if the request succeded, otherwise <code>false</code>
	 * @throws Exception if the service execution is not possible
	 */
	public static boolean showDocument(String pURL) throws Exception
	{
		BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
		
		URL url;
		
		try
		{
			url = new URL(pURL);
		}
		catch (MalformedURLException mfue)
		{
			url = new URL("file://" + pURL);
		}
		
		return bs.showDocument(url);
	}
	
	/**
	 * Opens a file chooser dialog via the JNLP FileOpenService.
	 * 
	 * @param pMultiSelection <code>true</code> to allowe multi file selection, <code>false</code> for single
	 *                        file selection
	 * @return the list of selected {@link IFileHandle}
	 * @throws Exception if the service execution is not possible
	 */
	public static IFileHandle[] openFileDialog(boolean pMultiSelection) throws Exception
	{
		FileOpenService fos = (FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");
		

		if (pMultiSelection)
		{
			FileContents[] files = fos.openMultiFileDialog(null, null);

			if (files != null)
			{
				FileHandle[] fhResult = new FileHandle[files.length];
					
				for (int i = 0; i < files.length; i++)
				{
					fhResult[i] = new FileHandle(files[i].getName(), files[i].getInputStream());
				}
					
				return fhResult;
			}
			else
			{
				return null;
			}
		}
		else
		{
			FileContents file = fos.openFileDialog(null, null);
			
			if (file != null)
			{
				return new FileHandle[] {new FileHandle(file.getName(), file.getInputStream())};
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Opens a file save dialog with the JNLP FileSaveService.
	 * 
	 * @param pFileHandle the {@link IFileHandle} to store
	 * @throws Exception if the service execution is not possible
	 */
	public static void saveFileDialog(IFileHandle pFileHandle) throws Exception
	{
		FileSaveService fss = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
		
		fss.saveFileDialog(null, null, pFileHandle.getInputStream(), pFileHandle.getFileName());
	}

	/**
	 * Gets the codebase from the JNLP BasicService.
	 * 
	 * @return the codebase or <code>null</code> if the codebase is not set
	 * @throws Exception if the service execution is not possible
	 */
	public static String getCodeBase() throws Exception
	{
		BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
		
		return bs.getCodeBase().toString();
	}
	
	/**
	 * Sets the given text to the clipboard.
	 * 
	 * @param pText the text
	 * @throws Exception if clipboard service access fails
	 */
	public static void setClipboard(String pText) throws Exception
	{
		ClipboardService cbs = (ClipboardService)ServiceManager.lookup("javax.jnlp.ClipboardService");
		
		cbs.setContents(new StringSelection(pText));
	}
	
	/**
	 * Gets text from the clipboard.
	 * 
	 * @return the text
	 * @throws Exception if clipboard service access fails
	 */
	public static String getClipboard() throws Exception
	{
		ClipboardService cbs = (ClipboardService)ServiceManager.lookup("javax.jnlp.ClipboardService");
		
		Transferable trans = cbs.getContents();
		
		if (trans != null)
		{
			Object obj = trans.getTransferData(FLAVOR_TEXT);
			
			if (obj != null)
			{
				if (obj instanceof Reader)
				{
					StringBuilder sb = new StringBuilder();
					
					char[] chars = new char[256];
					
					int iLen;
					
					Reader reader = (Reader)obj;
					
					try
					{
						while ((iLen = reader.read(chars)) >= 0)
						{
							sb.append(chars, 0, iLen);
						}
					}
					finally
					{
						try
						{
							reader.close();
						}
						catch (Exception e)
						{
							//nothing to be done
						}
					}
					
					return sb.toString();
				}
				else if (obj instanceof InputStream)
				{
					return new String(FileUtil.getContent((InputStream)obj));
				}
			}
			else
			{
				return (String)trans.getTransferData(FLAVOR_STRING);
			}
			
		}
		
		return null;
	}
	
	/**
	 * Gets specific data from the clipboard.
	 * 
	 * @param pFlavor the data flavor
	 * @return the data
	 * @throws Exception if clipboard service access fails
	 */
	public static Object getClipboard(DataFlavor pFlavor) throws Exception
	{
		ClipboardService cbs = (ClipboardService)ServiceManager.lookup("javax.jnlp.ClipboardService");
		
		Transferable trans = cbs.getContents();
		
		if (trans != null)
		{
			return trans.getTransferData(pFlavor);
		}
		
		return null;
	}
	
	/**
	 * Set the value of a property through the PersistenceService.
	 * 
	 * @param pKey the property name
	 * @param pValue the value set
	 */
	public static void setProperty(String pKey, String pValue)
	{
		try
		{
			PersistenceService ps = (PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
	
			URL url = new URL(getCodeBase());
			
			FileContents file;
			
			XmlWorker xmw = new XmlWorker();
			
			XmlNode node = null;
			
			try
			{
				file = ps.get(url);
				
				node = xmw.read(file.getInputStream());
			}
			catch (FileNotFoundException fnfe)
			{
				//max length: 2MB
				ps.create(url, 2 * 1024 * 1000);
				file = ps.get(url);
			}

			if (node == null)
			{
				node =  XmlNode.createXmlDeclaration();
			}

			node.setNode("/registry/" + pKey.replace("/", "_"), pValue);

			OutputStream out = file.getOutputStream(true); 
			
			xmw.write(out, node);
			
			out.flush();
			out.close();
			
			ps.setTag(url, PersistenceService.CACHED);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
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
		try
		{
			PersistenceService ps = (PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
			
			URL url = new URL(getCodeBase());
			
			FileContents file;
			
			XmlWorker xmw = new XmlWorker();
			
			XmlNode node;
			
			try
			{
				file = ps.get(url);
				
				node = xmw.read(file.getInputStream());
				
				if (node != null)
				{
					node = node.getNode("/registry/" + pKey.replace("/", "_"));
					
					if (node != null)
					{
						return node.getValue();
					}
				}
				
				return null;
			}
			catch (FileNotFoundException fnfe)
			{
				return null;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
}	// WebstartImpl
