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
 * 02.11.2008 - [RH] - tests the now the MemDataSource including the writebackIsolationLevel 
 */
package com.sibvisions.rad.model.mem;

import java.util.IdentityHashMap;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.event.DataSourceEvent;
import javax.rad.model.event.DataSourceEvent.EventType;
import javax.rad.model.event.IDataSourceListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.util.ArrayUtil;

/**
 * Tests all Functions of com.sibvisions.rad.model.mem.MemDataSource .<br>
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.model.mem.DataBook
 * @see com.sibvisions.rad.model.mem.DataPage
 */
public class TestMemDataSource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The DataBook test instance. */
	private MemDataBook	   book;

	/** The MeDataSource test instance. */
	private MemDataSource  dataSource;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Connects to the Test database.
	 * 
	 * @throws Exception if the connect to the DB fails
	 */
	@Before
	public void open() throws Exception
	{
	    dataSource = new MemDataSource();
	    dataSource.open();
	 
        RowDefinition rowdef = new RowDefinition();
        rowdef.addColumnDefinition(new ColumnDefinition("ID"));

        book = new MemDataBook(rowdef);
	    book.setName("book");
	    book.open();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the base statements of the <code>MemDataSource</code>.
	 */
	@Test
	public void testBaseStatements()
	{
		// check addDataBook/getDataBooks
		dataSource.addDataBook(new MemDataBook());
		dataSource.addDataBook(book);
		
		IDataBook[] enumDataBooks = dataSource.getDataBooks();

		Assert.assertTrue(ArrayUtil.indexOfReference(enumDataBooks, book) >= 0);
		
		// check remove DataBook
		dataSource.removeDataBook(book);

		enumDataBooks = dataSource.getDataBooks();
		Assert.assertTrue(ArrayUtil.indexOfReference(enumDataBooks, book) < 0);
		
		// check WriteBackIsolationLevel
		dataSource.setWritebackIsolationLevel(IDataBook.WriteBackIsolationLevel.DATA_ROW);
		Assert.assertTrue(dataSource.getWritebackIsolationLevel() == IDataBook.WriteBackIsolationLevel.DATA_ROW);
	}
		
	/**
	 * Tests event handling of {@link javax.rad.model.IDataSource}.
	 * 
	 * @throws ModelException if test fails
	 */
	@Test
	public void testListener() throws ModelException
	{
	    final IdentityHashMap<IDataBook, Boolean> books = new IdentityHashMap<IDataBook, Boolean>();
	    
	    IDataSourceListener listener = new IDataSourceListener()
        {
            public void dataSourceChanged(DataSourceEvent pDataSourceEvent)
            {
                if (pDataSourceEvent.getEventType() == EventType.ADD)
                {
                    books.put(pDataSourceEvent.getDataBook(), Boolean.TRUE);
                }
                else
                {
                    books.remove(pDataSourceEvent.getDataBook());
                }
            }
        };
	    
	    dataSource.eventDataBookAdded().addListener(listener);
        dataSource.eventDataBookRemoved().addListener(listener);

        RowDefinition rowdef = new RowDefinition();
        rowdef.addColumnDefinition(new ColumnDefinition("ID"));
        
        MemDataBook book2 = new MemDataBook(rowdef);
        book2.setName("book2");
        book2.open();
        
	    dataSource.addDataBook(book2);
        dataSource.addDataBook(book);
        dataSource.removeDataBook(book);
        
        Assert.assertEquals(1, books.size());
        Assert.assertFalse(books.containsKey(book));
	}
	
} 	// TestDataSource
