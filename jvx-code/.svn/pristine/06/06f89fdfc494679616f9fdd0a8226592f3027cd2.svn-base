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
 * 22.12.2014 - [JR] - creation
 */
package com.sibvisions.rad.persist;

import javax.rad.model.IDataSource;
import javax.rad.model.ModelException;
import javax.rad.persist.IStorage;
import javax.rad.remote.MasterConnection;

import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>StorageDataBook</code> is a {@link RemoteDataBook} directly connected to an IStorage.
 * 
 * @author René Jahn
 */
public class StorageDataBook extends RemoteDataBook
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the internal object connection. */
    private DirectObjectConnection objectConnection;
    
    /** the internal connection. */
    private MasterConnection connection;
    
    /** the internal datasource. */
    private RemoteDataSource dataSource;
    
    /** the internal storage name. */
    private String sName;
    
    /** the internal instance count. */
    private static int iInstance = 0;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>StorageDataBook</code>.
     */
    public StorageDataBook()
    {
        this(null, null);
    }
    
    /**
     * Creates a new instance of <code>StorageDataBook</code> for the given storage.
     * 
     * @param pStorage the storage
     */
    public StorageDataBook(IStorage pStorage)
    {        
        this(null, pStorage);
    }

    /**
     * Creates a new instance of <code>StorageDataBook</code> for the given storage.
     * 
     * @param pBook the configuration to use or <code>null</code> to use a new configuration
     * @param pStorage the storage
     */
    public StorageDataBook(StorageDataBook pBook, IStorage pStorage)
    {        
        if (pBook == null)
        {
            objectConnection = new DirectObjectConnection();
        }
        else
        {
            if (!pBook.isOpen())
            {
                throw new RuntimeException("Given StorageDataBook isn't open!");
            }
            
            objectConnection = pBook.objectConnection;
            connection = pBook.connection;
            dataSource = pBook.dataSource;
        }
        
        sName = "storage" + (iInstance++);
        
        setStorageImplIntern(pStorage);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void open() throws ModelException
    {
        if (!isOpen())
        {
            if (getStorageImpl() == null)
            {
                throw new ModelException("Can't open databook without storage!");
            }
            
            try
            {
                if (connection == null)
                {
                    connection = new MasterConnection(objectConnection);
                    connection.setAliveInterval(-1);
                    connection.setTimeout(-1);
                    connection.open();
        
                    dataSource = new RemoteDataSource();
                    dataSource.setConnection(connection);
                    dataSource.open();
                }
                
                super.setDataSource(dataSource);
                super.setName(sName);
            }
            catch (Throwable th)
            {
                throw new ModelException(th);
            }
        }
        
        super.open();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        super.close();

        CommonUtil.close(dataSource, connection);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String pName)
    {
        throw new UnsupportedOperationException("It's not allowed to use a custom name!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataSource(IDataSource pDataSource)
    {
        throw new UnsupportedOperationException("It's not allowed to use a custom datasource!");
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the storage implementation which should be used to access data.
     * 
     * @param pStorage the storage
     * @throws IllegalArgumentException if storage is null
     */
    public void setStorageImpl(IStorage pStorage)
    {
        if (pStorage == null)
        {
            throw new IllegalArgumentException("Storage can't be null!");
        }
            
        setStorageImplIntern(pStorage);
    }

    /**
     * Sets the storage implementation which should be used to access data.
     * 
     * @param pStorage the storage
     */
    private void setStorageImplIntern(IStorage pStorage)
    {
        objectConnection.put(sName, pStorage);
    }
    
    /**
     * Gets the configured storage implementation.
     * 
     * @return the storage
     */
    public IStorage getStorageImpl()
    {
        return (IStorage)objectConnection.get(sName);
    }
    
}   // StorageDataBook
