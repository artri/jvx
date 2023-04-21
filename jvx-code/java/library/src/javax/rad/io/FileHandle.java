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
 * 22.04.2008 - [HM] - creation
 * 17.07.2009 - [JR] - getLength, getInputStream, getGZIPContent: used fileName property as option
 *                   - getLength: (zippedBytes[zippedBytes.length - 2] & 0xff) << 24) replaced with 
 *                                (zippedBytes[zippedBytes.length - 1] & 0xff) << 24) [BUGFIX]
 * 05.10.2010 - [JR] - #169: remove directories from filenames
 */
package javax.rad.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;

/**
 * FileHandle handles data in memory or in the file system.
 * The Bean standard guarantees with the properties "fileName" and "gZIPContent" that it
 * can be transported with a connection. 
 * 
 * For compatibility reasons is the FileHandle also Serializable.
 * 
 * @author Martin Handsteiner
 */
public class FileHandle implements IFileHandle, 
                                   Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the simple file name. */ 
	private String fileName;

	/** the temporary file reference. */
	private transient File fiTempOut;
	
	/** the temporary output stream. */
	private transient FileOutputStream fosTempOut;
	
	/** the content. */
	private transient byte[] byContent;
	
	/** the file handle content. */
	private transient IFileHandle fileHandle;
	
	/** the content in File. */ 
	private transient File file;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new FileHandle.
	 */
	public FileHandle()
	{
		fileName = null;
		file = null;
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pAbsolutePath the absolute path of the file.
	 */
	public FileHandle(String pAbsolutePath)
	{
        setContent(new File(pAbsolutePath));
	}
	
	/**
	 * Constructs a new file handle with the content from a <code>byte[]</code>.
	 * 
	 * @param pFileName the file name.
	 * @param pContent the content.
	 * @throws IOException if an IOException occurs.
	 */
	public FileHandle(String pFileName, byte[] pContent) throws IOException
	{
		fileName = FileUtil.getName(pFileName);
		setContent(pContent);
	}
	
	/**
	 * Constructs a file handle with the content form an {@link InputStream}.
	 * 
	 * @param pFileName the file name.
	 * @param pContent the content.
	 * @throws IOException if an IOException occurs.
	 */
	public FileHandle(String pFileName, InputStream pContent) throws IOException
	{
		fileName = FileUtil.getName(pFileName);
		setContent(pContent);
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pContent the content.
	 * @throws IOException if an IOException occurs.
	 */
	public FileHandle(File pContent) throws IOException
	{
		setContent(pContent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getInputStream() throws IOException
	{
        if (fiTempOut != null)
        {
            return new FileInputStream(fiTempOut);
        }
        else if (fileHandle != null)
	    {
	        return fileHandle.getInputStream();
	    }
	    else if (byContent != null)
	    {
	        return new ByteArrayInputStream(byContent);
	    }
	    else if (file != null)
		{
			return new FileInputStream(file);
		}

	    return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long getLength() throws IOException
	{
        if (fiTempOut != null)
        {
            return fiTempOut.length();
        }
        else if (fileHandle != null)
	    {
	        return fileHandle.getLength();
	    }
	    else if (byContent != null)
	    {
	        return byContent.length;
	    }
	    else if (file != null)
		{
			return file.length();
		}

	    return -1;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        if (fiTempOut != null)
        {
            CommonUtil.close(fosTempOut);
            
            try
            {
                fiTempOut.delete();
            }
            catch (Throwable pThrowable)
            {
                // Can do nothing!
            }
        }
        
        super.finalize();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the file name of this file handle.
	 * @param pFileName the file name of this file handle.
	 */
	public void setFileName(String pFileName)
	{
		fileName = FileUtil.getName(pFileName);
	}

	/**
	 * Gets the file that was set as content.
	 * 
	 * @return the file that was set as content.
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Gets the content (definition). The content is dynamic and can be an {@link IFileHandle} or
	 * an GZIP compressed <code>byte[]</code>. It depends on the current state, whether the file was used only
	 * locally or sent to a remote server. This method is primarily for serialization support.
	 * 
	 * @return the content (definition)
	 * @throws IOException if accessing content failed
	 */
	public Object getContentDefinition() throws IOException
	{
	    if (fileHandle != null)
	    {
	        return fileHandle;
	    }
	    else
	    {
	        IUploadExecutor upload = TransferContext.getCurrentUploadExecutor();
	    
    	    if (upload != null)
    	    {
    	        return upload.writeContent(this);
    	    }
    	    else
    	    {
                return getGZIPContent();
    	    }
	    }
	}
	
	/**
	 * Sets the content (definition).
	 * 
	 * @param pDefinition an {@link IFileHandle} or a GZIP compressed <code>byte[]</code>.
	 * @throws IOException if accessing content failed
	 * @see #getContentDefinition()
	 */
	public void setContentDefinition(Object pDefinition) throws IOException
	{
	    if (pDefinition instanceof IFileHandle)
	    {
	        fileHandle = (IFileHandle)pDefinition;
	    }
	    else if (pDefinition instanceof byte[])
	    {
	        setGZIPContent((byte[])pDefinition);
	    }
	}
	
	/**
	 * Gets the gzipped content. 
	 * 
	 * @return the gzipped content. 
	 * @throws IOException if an IOException occurs.
	 */
	private byte[] getGZIPContent() throws IOException
	{
		ByteArrayOutputStream result = new ByteArrayOutputStream();
			
		FileInputStream fis;
			
		if (file != null || fiTempOut != null)
		{
		    if (fiTempOut != null)
		    {
		        fis = new FileInputStream(fiTempOut);
		    }
		    else
		    {
                fis = new FileInputStream(file);
		    }
			
			FileUtil.copy(fis, true, new GZIPOutputStream(result), true);
			
			return result.toByteArray();
		}
		else if (byContent != null)
		{
		    ByteArrayInputStream bis = new ByteArrayInputStream(byContent);
		    
		    FileUtil.copy(bis, true, new GZIPOutputStream(result), true);
		    
		    return result.toByteArray();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the gzipped content.
	 * 
	 * @param pGZIPContent the gzipped content.
	 * @throws IOException if an IOException occurs.
	 */
	private void setGZIPContent(byte[] pGZIPContent) throws IOException
	{
	    ByteArrayInputStream bais = new ByteArrayInputStream(pGZIPContent);
	    
	    GZIPInputStream gzis = new GZIPInputStream(bais);
	    
	    FileUtil.copy(gzis, true, getOutputStream(), true);
	}

	/**
	 * Resets the content to zero, and gets an OutputStresm to write the content.
	 *  
	 * @return an OutputStresm to write the content.
	 * @throws IOException if an IOException occurs.
	 */
	public OutputStream getOutputStream() throws IOException
	{
	    if (file == null)
	    {
	        CommonUtil.close(fosTempOut);
	        
	        fiTempOut = File.createTempFile(getClass().getSimpleName(), ".tmp");
	        fiTempOut.deleteOnExit();
	        
	        fosTempOut = new FileOutputStream(fiTempOut); 
	        
	        return fosTempOut;
	    }
	    else
	    {
	        return new FileOutputStream(file);
	    }
	}
	
	/**
	 * Sets a new content with a byte array.
	 * @param pContent the new content.
	 * @throws IOException if an IOException occurs.
	 */
	public void setContent(byte[] pContent) throws IOException
	{
	    byContent = pContent;
	 
	    /*
	    bytes are already in memory, so we don't need an optimization here (but would be possible)
	    
		OutputStream outStream = getOutputStream();
		outStream.write(pContent);
		outStream.close();
		*/
	}
	
	/**
	 * Sets a new content with an {@link InputStream}.
	 * 
	 * @param pContent the new content.
	 * @throws IOException if an IOException occurs.
	 */
	public void setContent(InputStream pContent) throws IOException
	{
		FileUtil.copy(pContent, true, getOutputStream(), true);
	}
	
	/**
	 * Sets a new content with a File.
	 * 
	 * @param pContent the new content.
	 */
	public void setContent(File pContent)
	{
		if (pContent != null && fileName == null)
		{
			fileName = pContent.getName();
		}
		
		file = pContent;
	}

	/**
	 * Writes this Object to the ObjectOutputStream.
	 * 
	 * @param pObjectOutputStream the ObjectOutputStream.
	 * @throws IOException if an IOException occurs.
	 */
	private void writeObject(ObjectOutputStream pObjectOutputStream) throws IOException
	{
		pObjectOutputStream.defaultWriteObject();
		
		byte[] data = getGZIPContent();
		
		if (data == null)
		{
			pObjectOutputStream.writeInt(-1);
		}
		else
		{
			pObjectOutputStream.writeInt(data.length);
			pObjectOutputStream.write(data);
		}
	}
	
	/**
	 * Reads this Object to the pObjectInputStream.
	 * 
	 * @param pObjectInputStream the pObjectInputStream.
	 * @throws IOException if an IOException occurs.
	 * @throws ClassNotFoundException if a class does not exist.
	 */
	private void readObject(ObjectInputStream pObjectInputStream) throws IOException, ClassNotFoundException
	{
		pObjectInputStream.defaultReadObject();
		file = null;
		
		int len = pObjectInputStream.readInt();
		if (len >= 0)
		{
			byte[] data = new byte[len];
			pObjectInputStream.readFully(data);
			setGZIPContent(data);
		}
	}
	
}	// FileHandle
