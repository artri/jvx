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
 * 07.10.2008 - [RH] - created 
 * 17.11.2008 - [RH] - reloadAllDataBooks calls reload on all DataBooks
 * 19.04.2009 - [RH] - interface review.
 * 07.05.2009 - [RH] - open/isOpen/close added
 */
package com.sibvisions.rad.model.mem;

import java.lang.ref.WeakReference;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.IDataSource;
import javax.rad.model.ModelException;
import javax.rad.model.event.DataSourceEvent;
import javax.rad.model.event.DataSourceEvent.EventType;
import javax.rad.model.event.DataSourceHandler;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>MemDataSource</code> is a base class for all DataSources.
 * It handle the store operations for all changes made in the <code>IDataBook</code>'s.
 * It knows the write back isolation level, which handles when a 
 * <code>DataSource</code> has to save implicit during manipulation operations.
 * 
 * @see com.sibvisions.rad.model.mem.MemDataBook  
 * @see com.sibvisions.rad.model.mem.MemDataPage
 * 
 * @author Roland Hörmann
 */
public class MemDataSource implements IDataSource
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The write back isolation level. */
	private transient IDataBook.WriteBackIsolationLevel	iWritebackIsolationLevel = IDataBook.WriteBackIsolationLevel.DATA_ROW;
	
	/** the list of all registered <code>IDataBook</code>'s. */
	private transient ArrayUtil<WeakReference<IDataBook>>	auDataBooks	= new ArrayUtil<WeakReference<IDataBook>>();
	
	/** The <code>EventHandler</code> for databook added event. */
    private transient DataSourceHandler                   eventDataBookAdded;
	
    /** The <code>EventHandler</code> for databook removed event. */
    private transient DataSourceHandler                   eventDataBookRemoved;

    /** Determines if the <code>DataSource</code> is open. */
	private transient boolean			                    bIsOpen = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a MemDataSource.
	 */
	public MemDataSource()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void open() throws ModelException
	{
		if (!isOpen())
		{
			bIsOpen = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOpen()
	{
		return bIsOpen;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void close()
	{
		if (isOpen())
		{
			bIsOpen = false;
	
			for (int i = auDataBooks.size() - 1; i >= 0; i--)
			{
				IDataBook dataBook = auDataBooks.get(i).get();
				if (dataBook == null)
				{
					auDataBooks.remove(i);
				}
				else
				{
					dataBook.close();
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void saveAllDataBooks() throws ModelException
	{
		for (int i = 0, size = auDataBooks.size(); i < size; i++)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook != null)
			{
				dataBook.saveAllRows();
			}
		}
	}		
		
	/**
	 * {@inheritDoc}
	 */
	public synchronized void reloadAllDataBooks() throws ModelException
	{
		restoreAllDataBooks();
		for (int i = 0, size = auDataBooks.size(); i < size; i++)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook != null)
			{
				dataBook.reload();
			}
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void restoreAllDataBooks()
	{
		for (int i = 0, size = auDataBooks.size(); i < size; i++)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook != null)
			{
				try
				{
					dataBook.restoreAllRows();
				}
				catch (ModelException pModelException)
				{
					// Silent restore!
				}
			}
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void addDataBook(IDataBook pDataBook)
	{
		int index = auDataBooks.size();
		for (int i = index - 1; i >= 0; i--)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook == null)
			{
				auDataBooks.remove(i);
				if (index > i)
				{
					index--;
				}
			}
			else if (dataBook.getMasterReference() != null
					&& dataBook.getMasterReference().getReferencedDataBook() == pDataBook)
			{
				index = i;
			}
		}

		auDataBooks.add(index, new WeakReference<IDataBook>(pDataBook));
		
        if (eventDataBookAdded != null)
        {
            eventDataBookAdded.dispatchEvent(new DataSourceEvent(this, pDataBook, EventType.ADD));
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void removeDataBook(IDataBook pDataBook)
	{
		for (int i = auDataBooks.size() - 1; i >= 0; i--)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook == null || dataBook == pDataBook)
			{
				auDataBooks.remove(i);
			}
		}
		
        if (eventDataBookRemoved != null)
        {
            eventDataBookRemoved.dispatchEvent(new DataSourceEvent(this, pDataBook, EventType.REMOVE));
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized IDataBook[] getDataBooks()
	{
		ArrayUtil<IDataBook> result = new ArrayUtil<IDataBook>();
		for (int i = auDataBooks.size() - 1; i >= 0; i--)
		{
			IDataBook dataBook = auDataBooks.get(i).get();
			if (dataBook == null)
			{
				auDataBooks.remove(i);
			}
			else
			{
				result.add(0, dataBook);
			}
		}
		return result.toArray(new IDataBook[result.size()]);
	}	

	/**
	 * {@inheritDoc}
	 */
	public synchronized IDataBook getDataBook(String pName)
	{
		if (pName != null)
		{
			for (int i = 0, size = auDataBooks.size(); i < size; i++)
			{
				IDataBook dataBook = auDataBooks.get(i).get();
				
				if (dataBook != null && pName.equals(dataBook.getName()))
				{
					return dataBook;
				}
			}
		}
		return null;
	}	

	/**
	 * {@inheritDoc}
	 */
	public void setWritebackIsolationLevel(IDataBook.WriteBackIsolationLevel pIsolationLevel)
	{
	    if (pIsolationLevel == null)
	    {
	        iWritebackIsolationLevel = WriteBackIsolationLevel.DATA_ROW;
	    }
	    else
	    {
            iWritebackIsolationLevel = pIsolationLevel;
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataBook.WriteBackIsolationLevel getWritebackIsolationLevel()
	{	
		return iWritebackIsolationLevel;
	}	
	
    /**
     * {@inheritDoc}
     */
    public DataSourceHandler eventDataBookAdded()
    {
        if (eventDataBookAdded == null)
        {
            eventDataBookAdded = new DataSourceHandler();
        }
        
        return eventDataBookAdded;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataSourceHandler eventDataBookRemoved()
    {
        if (eventDataBookRemoved == null)
        {
            eventDataBookRemoved = new DataSourceHandler();
        }
        
        return eventDataBookRemoved;
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
		try
		{
			close();
		}
		catch (Throwable pThrowable)
		{
			// Silent close
		}
		
		super.finalize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String sOutput = "MemDataSource :: iWritebackIsolationLevel=";

		if (iWritebackIsolationLevel == IDataBook.WriteBackIsolationLevel.DATA_ROW)
		{
			sOutput += "DATA_ROW\n";
		} 
		else if (iWritebackIsolationLevel == IDataBook.WriteBackIsolationLevel.DATASOURCE)
		{
			sOutput += "DATASOURCE\n";
		} 
		
        return sOutput;
	}

} 	// MemDataSource
