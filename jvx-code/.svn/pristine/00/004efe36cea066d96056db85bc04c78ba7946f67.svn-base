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
 * 17.07.2009 - [JR] - getLength, getInputStream: used fileName property as option
 * 05.10.2010 - [JR] - #169: remove directories from filenames
 * 11.10.2011 - [JR] - #480: always put this into the cache
 * 19.05.2014 - [JR] - #1041: set temporary download URL
 */
package javax.rad.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;

import com.sibvisions.util.IInvalidator;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.type.FileUtil;

/**
 * The <code>RemoteFileHandle</code> handles data that is stored in the ObjectCache.
 * Supported Objects are java.io.File, java.net.URL, javax.rad.io.IFileHandle and byte[]. 
 * The Bean standard guarantees with the properties "fileName" and "objectCacheKey" that it
 * can be transported with a connection. The Creator of the ObjectCacheHandle has to guarantee 
 * that the data is available under the given key.
 * 
 * For compatibility reasons is the ObjectCacheHandle also serializable.
 * 
 * @author Martin Handsteiner
 */
public class RemoteFileHandle implements IFileHandle,
                                         IInvalidator,
                                         Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default cache timeout (5 minutes). */
	private static final long DEFAULT_TIMEOUT = 300000;

	/** the content. */ 
	private String fileName;

	/** the key to find the content. */
	private Object objectCacheKey;
	
	/** the current cache timeout. */
	private long timeout = DEFAULT_TIMEOUT;
	
	/** the temporary file reference. */
	private transient File fiTempOut = null;
	
	/** the content file. */
	private transient File fiContent = null;
	
	/** the content URL. */
	private transient URL urlContent = null;
	
	/** the download executor. */
	private transient IDownloadExecutor download;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new FileHandle.
	 */
	public RemoteFileHandle()
	{
		fileName = null;
		objectCacheKey = null;
		
		download = TransferContext.getCurrentDownloadExecutor();
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pFileName the file name.
	 */
	public RemoteFileHandle(String pFileName)
	{
		fileName = FileUtil.getName(pFileName);
		objectCacheKey = null;
	}
	
	/**
	 * Constructs a new FileHandle. 
	 * 
	 * @param pFileName the filename.
	 * @param pObjectCacheKey the ObjectCache key.
	 */
	public RemoteFileHandle(String pFileName, Object pObjectCacheKey)
	{
		fileName = FileUtil.getName(pFileName);
		objectCacheKey = pObjectCacheKey;
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pFileName the filename.
	 * @param pContent the content.
	 */
	public RemoteFileHandle(String pFileName, URL pContent)
	{
		this(pFileName, null, pContent);
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pFileName the filename.
	 * @param pObjectCacheKey the ObjectCache key.
	 * @param pContent the content.
	 */
	public RemoteFileHandle(String pFileName, Object pObjectCacheKey, URL pContent)
	{
		fileName = FileUtil.getName(pFileName);
		objectCacheKey = pObjectCacheKey;
		setContent(pContent);
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pFileName the filename.
	 * @param pContent the content.
	 * @throws IOException if an IOException occurs.
	 */
	public RemoteFileHandle(String pFileName, InputStream pContent) throws IOException
	{
		this(pFileName, null, pContent);
	}
	
	/**
	 * Constructs a new FileHandle.
	 * 
	 * @param pFileName the filename.
	 * @param pObjectCacheKey the ObjectCache key.
	 * @param pContent the content.
	 * @throws IOException if an IOException occurs.
	 */
	public RemoteFileHandle(String pFileName, Object pObjectCacheKey, InputStream pContent) throws IOException
	{
		fileName = FileUtil.getName(pFileName);
		objectCacheKey = pObjectCacheKey;
		setContent(pContent);
	}
	
	/**
	 * Constructs a new FileHandle with the given file as content.
	 * 
	 * @param pContent the content.
	 */
	public RemoteFileHandle(File pContent)
	{
		fileName = pContent.getName();
		
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
		else if (objectCacheKey != null)
		{
			Object content = ObjectCache.get(objectCacheKey);

			if (content == null)
			{			    
			    if (download != null)
			    {
			        return download.readContent(this);
			    }
			}
			else if (content instanceof IFileHandle)
			{
				if (content != this)
				{
					return ((IFileHandle)content).getInputStream();
				}
				else
				{
					if (fiContent != null)
					{
						return new FileInputStream(fiContent);
					}
					else if (urlContent != null)
					{
						return urlContent.openConnection().getInputStream();
					}
				}
			}
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
		else if (objectCacheKey != null)
		{
			Object content = ObjectCache.get(objectCacheKey);

			if (content == null)
			{
			    if (download != null)
			    {
			        return download.getContentLength(this);
			    }
			}
			else if (content instanceof IFileHandle)
			{
				if (content != this)
				{
					return ((IFileHandle)content).getLength();
				}
				else
				{
					if (fiContent != null)
					{
						return fiContent.length();
					}
					else if (urlContent != null)
					{
						return urlContent.openConnection().getContentLength();
					}
				}
			}
		}
		
		return -1;
	}

    /**
     * {@inheritDoc}
     */
	public void invalidate(Object pObject)
	{
        synchronized (this)
        {
            notifyAll();
        }
        
        if (fiTempOut != null)
        {
            try
            {
                fiTempOut.delete();
            }
            catch (Throwable pThrowable)
            {
                // Can do nothing!
            }
            fiTempOut = null;
        }
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
        invalidate(this);
        
        super.finalize();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the access key for the object.
	 * 
	 * @return the access key for the object                 
	 */
	public static Object createObjectCacheKey()
	{
		return ObjectCache.createKey();
	}
	
	/**
	 * Sets the file name of this file handle.
	 * @param pFileName the file name of this file handle.
	 */
	public void setFileName(String pFileName)
	{
		fileName = FileUtil.getName(pFileName);
	}

	/**
	 * Gets the ObjectCache key. 
	 * @return the ObjectCache key. 
	 */
	public Object getObjectCacheKey()
	{
		return objectCacheKey;
	}
	
	/**
	 * Sets the ObjectCache key. 
	 * @param pObjectCacheKey the ObjectCache key. 
	 */
	public void setObjectCacheKey(Object pObjectCacheKey)
	{
		objectCacheKey = pObjectCacheKey;
	}

	/**
	 * Gets the ObjectCache key. 
	 * @return the ObjectCache key. 
	 */
	public long getTimeout()
	{
		return timeout;
	}
	
	/**
	 * Sets the ObjectCache key. 
	 * @param pTimeout the ObjectCache key. 
	 */
	public void setTimeout(long pTimeout)
	{
		timeout = pTimeout;
	}

	/**
	 * Resets the content to zero, and gets an OutputStresm to write the content.
	 * The content is stored temporary with the configured timeout.
	 * @return an OutputStresm to write the content.
	 * @throws IOException if an IOException occurs.
	 */
	public OutputStream getOutputStream() throws IOException
	{
	    if (fiTempOut != null)
	    {
            try
            {
                fiTempOut.delete();
            }
            catch (Throwable pThrowable)
            {
                // Can do nothing!
            }
	    }
	    
		fiTempOut = File.createTempFile(getClass().getSimpleName(), ".tmp");
		fiTempOut.deleteOnExit();
		
		if (objectCacheKey == null)
		{
			objectCacheKey = ObjectCache.put(this, timeout);
		}
		else
		{
			ObjectCache.put(objectCacheKey, this, timeout);
		}
		
		return new FileOutputStream(fiTempOut);
	}

	/**
	 * Sets a new content with a byte array.
	 * The content is stored temporary with the configured timeout.
	 * @param pContent the new content.
	 * @throws IOException if an IOException occurs.
	 */
	public void setContent(byte[] pContent) throws IOException
	{
		if (pContent == null)
		{
			if (objectCacheKey != null)
			{
				ObjectCache.remove(objectCacheKey);
			}
		}
		else
		{  
			OutputStream outStream = getOutputStream();
			try
			{
			    outStream.write(pContent);
			}
			finally
			{
			    outStream.close();
			}
		}
	}
	
	/**
	 * Sets a new content with a InputStream.
	 * The content is stored temporary with the configured timeout.
	 * @param pContent the new content.
	 * @throws IOException if an IOException occurs.
	 */
	public void setContent(InputStream pContent) throws IOException
	{
		if (pContent == null)
		{
			if (objectCacheKey != null)
			{
				ObjectCache.remove(objectCacheKey);
			}
		}
		else
		{
			FileUtil.copy(pContent, true, getOutputStream(), true);
		}
	}
	
	/**
	 * Sets a new content with a File.
	 * The content is stored temporary with the configured timeout.
	 * @param pContent the new content.
	 */
	public void setContent(File pContent)
	{
		urlContent = null;
		fiContent = pContent;
		
		if (pContent == null)
		{
			if (objectCacheKey != null)
			{
				ObjectCache.remove(objectCacheKey);
			}
		}
		else
		{
			if (objectCacheKey == null)
			{
				objectCacheKey = ObjectCache.put(this, timeout);
			}
			else
			{
				ObjectCache.put(objectCacheKey, this, timeout);
			}
		}
	}
	
	/**
	 * Sets a new content with a URL.
	 * The content is stored temporary with the configured timeout.
	 * @param pContent the new content.
	 */
	public void setContent(URL pContent)
	{
		fiContent = null;
		urlContent = pContent;
		
		if (pContent == null)
		{
			if (objectCacheKey != null)
			{
				ObjectCache.remove(objectCacheKey);
			}
		}
		else
		{
			if (objectCacheKey == null)
			{
				objectCacheKey = ObjectCache.put(this, timeout);
			}
			else
			{
				ObjectCache.put(objectCacheKey, this, timeout);
			}
		}
	}
	
}	// RemoteFileHandle
