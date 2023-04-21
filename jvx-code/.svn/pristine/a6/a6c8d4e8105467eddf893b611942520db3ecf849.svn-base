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
 * 01.10.2008 - [RH] - creation
 * 17.11.2008 - [RH] - storeXXX -> saveXXXX; restoreXXXX -> reloadXXX changed
 * 19.04.2009 - [RH] - interface reviewed, write back isolation levels moved from IDataBook
 * 07.05.2009 - [RH] - open/isOpen/close added
 */
package javax.rad.model;

import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.event.DataSourceHandler;

/**
 * The {@link IDataSource} is a storage independent way to access table oriented
 * data, or any data that can be approximately represented as table.
 * 
 * @see IDataBook
 * 
 * @author Roland Hörmann
 */
public interface IDataSource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Opens this {@link IDataSource} so that it can be used. Only an open
	 * {@link IDataSource} can be used.
	 * <p>
	 * Implementing classes should perform their set up work here.
	 *
	 * @throws ModelException if there was an error during opening.
	 * @see #close()
	 * @see #isOpen()
	 */
	public void open() throws ModelException;
	
	/**
	 * Gets if this {@link IDataSource} is open.
	 * <p>
	 * Only an open {@link IDataSource} is a usable {@link IDataSource}.
	 * 
	 * @return {@code true} if this {@link IDataSource} is open.
	 * @see #open()
	 */
	public boolean isOpen();
	
	/**
	 * Closes this {@link IDataSource}. A closed {@link IDataSource} can no
	 * longer be used, however it can always be {@link #open() reopened}.
	 * <p>
	 * Implementing classes should perform their clean up work here. It closes
	 * the IDataSource and all its IDataBooks.
	 * 
	 * @see #isOpen()
	 * @see #open()
	 */
	public void close();
	
	/**
	 * Saves all changes in the {@link #addDataBook(IDataBook) added}
	 * {@link IDataBook}s by invoking {@link IDataBook#saveAllRows()}.
	 * 
	 * @throws ModelException if the changes could not be saved.
	 * @see #reloadAllDataBooks()
	 * @see #restoreAllDataBooks()
	 * @see IDataBook#saveAllRows()
	 */
	public void saveAllDataBooks() throws ModelException;
	
	/**
	 * Reloads all {@link #addDataBook(IDataBook) added} {@link IDataBook}s by
	 * invoking {@link IDataBook#reload()}.
	 * 
	 * @throws ModelException if the {@link IDataBook}s could not be reloaded.
	 * @see #restoreAllDataBooks()
	 * @see #saveAllDataBooks()
	 * @see IDataBook#reload()
	 */
	public void reloadAllDataBooks() throws ModelException;
	
	/**
	 * Restores all {@link #addDataBook(IDataBook) added} {@link IDataBook}s by
	 * invoking {@link IDataBook#restoreAllRows()}.
	 * 
	 * @throws ModelException if the {@link IDataBook}s could not be restored.
	 * @see #reloadAllDataBooks()
	 * @see #saveAllDataBooks()
	 * @see IDataBook#restoreAllRows()
	 */
	public void restoreAllDataBooks() throws ModelException;
	
	/**
	 * Adds the given {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook} to add.
	 */
	public void addDataBook(IDataBook pDataBook);
	
	/**
	 * Removes the given {@link IDataBook}.
	 * 
	 * @param pDataBook the given {@link IDataBook} to remove.
	 */
	public void removeDataBook(IDataBook pDataBook);
	
	/**
	 * Gets all the {@link IDataBook}s which have been added to this
	 * {@link IDataSource}.
	 * 
	 * @return all the {@link IDataBook}s which have been added to this
	 *         {@link IDataSource}.
	 */
	public IDataBook[] getDataBooks();
	
	/**
	 * Gets the (first) {@link IDataBook} with the given name.
	 * 
	 * @param pName the name of the {@link IDataBook} to get.
	 * @return the (first) {@link IDataBook} with the given name, {@code null}
	 *         if none was found.
	 */
	public IDataBook getDataBook(String pName);
	
	/**
	 * Sets the default {@link WriteBackIsolationLevel} for all
	 * {@link #addDataBook(IDataBook) added} {@link IDataBook}s.
	 * <p>
	 * The default {@link WriteBackIsolationLevel} is
	 * {@link WriteBackIsolationLevel#DATA_ROW}.
	 * 
	 * @param pIsolationLevel the default {@link WriteBackIsolationLevel} for
	 *            all {@link #addDataBook(IDataBook) added} {@link IDataBook}s.
	 * @see #getWritebackIsolationLevel()
	 */
	public void setWritebackIsolationLevel(WriteBackIsolationLevel pIsolationLevel);
	
	/**
	 * Gets the default {@link WriteBackIsolationLevel} for all
	 * {@link #addDataBook(IDataBook) added} {@link IDataBook}s.
	 * <p>
	 * The default {@link WriteBackIsolationLevel} is
	 * {@link WriteBackIsolationLevel#DATA_ROW}.
	 * 
	 * 
	 * @return the default {@link WriteBackIsolationLevel} for all
	 *         {@link #addDataBook(IDataBook) added} {@link IDataBook}s.
	 * @see #setWritebackIsolationLevel(IDataBook.WriteBackIsolationLevel)
	 */
	public WriteBackIsolationLevel getWritebackIsolationLevel();
	
	/**
	 * Gets the {@link DataSourceHandler} for databook added event.
	 * <p>
	 * This event is fired if an {@link IDataBook} has been
	 * {@link #addDataBook(IDataBook) added}.
	 * 
	 * @return the {@link DataSourceHandler} for databook added event.
	 */
	public DataSourceHandler eventDataBookAdded();
	
	/**
	 * Gets the {@link DataSourceHandler} for databook removed event.
	 * <p>
	 * This event is fired if an {@link IDataBook} has been
	 * {@link #removeDataBook(IDataBook) removed}.
	 * 
	 * @return the {@link DataSourceHandler} for databook removed event.
	 */
	public DataSourceHandler eventDataBookRemoved();
	
}	// IDataSource
