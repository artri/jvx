/*
 * Copyright 2014 SIB Visions GmbH
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
 * 23.11.2014 - [JR] - creation
 */
package javax.rad.io;

import javax.rad.remote.ConnectionInfo;

/**
 * The <code>ServerContextImpl</code> is an internal {@link javax.rad.server.ServerContext} implementation.
 * 
 * @author René Jahn
 */
public class TransferContext
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the current TransferContext instance. */
    private static ThreadLocal<TransferContext> instance = new ThreadLocal<TransferContext>();
    
    /** the connection info. */
    private ConnectionInfo info;
    
    /** the download executor. */
    private IDownloadExecutor download;
    
    /** the upload executor. */
    private IUploadExecutor upload;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>TransferContext</code>.
     * 
     * @param pInfo the connection info
     * @param pDownload the download executor
     * @param pUpload the upload executor
     */
    public TransferContext(ConnectionInfo pInfo, IDownloadExecutor pDownload, IUploadExecutor pUpload)
    {
        download = pDownload;
        upload = pUpload;
        info = pInfo;
        
        setInstance(this);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the connection info.
     * 
     * @return the connection info or <code>null</code> if info isn't available
     */
    public ConnectionInfo getConnectionInfo()
    {
        return info;
    }
    
    /**
     * Gets the download executor.
     * 
     * @return the executor or <code>null</code> if not available
     */
    public IDownloadExecutor getDownloadExecutor()
    {
        return download;
    }
    
    /**
     * Gets the upload executor.
     * 
     * @return the executor or <code>null</code> if not available
     */
    public IUploadExecutor getUploadExecutor()
    {
        return upload;
    }

    /**
     * Gets the current download executor.
     * 
     * @return the executor or <code>null</code> if not available
     */
    public static IDownloadExecutor getCurrentDownloadExecutor()
    {
        TransferContext ctxt = getCurrentInstance();
        
        if (ctxt != null)
        {
            return ctxt.getDownloadExecutor();
        }
        
        return null;
    }
    
    /**
     * Gets the current upload executor.
     * 
     * @return the executor or <code>null</code> if not available
     */
    public static IUploadExecutor getCurrentUploadExecutor()
    {
        TransferContext ctxt = getCurrentInstance();
        
        if (ctxt != null)
        {
            return ctxt.getUploadExecutor();
        }
        
        return null;
    }

    /**
     * Gets the current connection info.
     * 
     * @return the connection info or <code>null</code> if not available
     */
    public static ConnectionInfo getCurrentConnectionInfo()
    {
        TransferContext ctxt = getCurrentInstance();
        
        if (ctxt != null)
        {
            return ctxt.getConnectionInfo();
        }
        
        return null;
    }    
    
    /**
     * Gets the current instance of <code>TransferContext</code>.
     * 
     * @return the current instance
     */
    public static TransferContext getCurrentInstance()
    {
        return instance.get();
    }
    
    /**
     * Sets the current context instance.
     * 
     * @param pContext the context instance
     */
    protected synchronized void setInstance(TransferContext pContext)
    {
        instance.set(pContext);
    }
    
    /**
     * Release any resources associated with this <code>TransferContext</code> instance.
     *
     * @see #isReleased
     */
    public final synchronized void release()
    {
        instance.set(null);
    }    
    
    /**
     * Gets the release state of this <code>TransferContext</code>.
     *  
     * @return <code>true</code> if there is no current instance of <code>TransferContext</code> (means
     *         that the <code>TransferContext</code> is released); otherwise <code>false</code>
     */
    public boolean isReleased()
    {
        return instance.get() == null;
    }
    
}   // TransferContext
