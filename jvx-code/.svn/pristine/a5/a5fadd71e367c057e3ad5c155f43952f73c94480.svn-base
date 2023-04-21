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
 * 30.10.2010 - [JR] - creation
 */
package com.sibvisions.rad.persist;

import java.math.BigDecimal;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.persist.DataSourceException;
import javax.rad.remote.MasterConnection;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.persist.event.IStorageListener;
import com.sibvisions.rad.persist.event.StorageEvent;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * Tests the functionality of {@link AbstractMemStorage}.
 * 
 * @author René Jahn
 */
public class TestAbstractMemStorage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the "server-side" mem storage. */
	private SimpleMemStorage sms;
	
	/** the applications databook. */
	private RemoteDataBook rdbApps;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initializes the applications remote databook.
	 * 
	 * @throws Throwable if the initialization failed
	 */
	@Before
	public void beforeTest() throws Throwable
	{
		sms = new SimpleMemStorage();
		sms.open();

		DirectObjectConnection con = new DirectObjectConnection();
		con.put("apps", sms);
		
		MasterConnection macon = new MasterConnection(con);
		macon.open();
		
		RemoteDataSource rds = new RemoteDataSource(macon);
		rds.open();
		
		rdbApps = new RemoteDataBook();
		rdbApps.setDataSource(rds);
		rdbApps.setName("apps");
		rdbApps.open();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link AbstractMemStorage#fetch(javax.rad.model.condition.ICondition, javax.rad.model.SortDefinition, int, int)}.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testFetch() throws Throwable
	{
		Assert.assertEquals(2, rdbApps.getRowDefinition().getColumnCount());
		Assert.assertArrayEquals(new String[] {"ID", "NAME"}, rdbApps.getRowDefinition().getColumnNames());
	}
	
	/**
	 * Tests insert with exception on server-side. The mem databook should have the same rowcount after the 
	 * insert as before the insert.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInsert() throws Throwable
	{
		try
		{
			rdbApps.insert(false);
			rdbApps.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3), "error"});
			rdbApps.saveSelectedRow();
			
			Assert.fail("Insert should throw an exception!");
		}
		catch (ModelException me)
		{
			Assert.assertEquals("not allowed", ExceptionUtil.getRootCause(me).getMessage());
		}
		
		//server must reload
		Assert.assertEquals(3, sms.getDataBook().getRowCount());
	}
	
	/**
	 * Tests insert with a bean which contains invisible columns.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInsertBean() throws Throwable
	{
		Bean bn = new Bean();
		bn.put("ID", BigDecimal.valueOf(10));
		bn.put("NAME", "Name: 10");
		bn.put("PATH", "/home/name");
		
		Bean bnResult = sms.insert(bn);
		
		Assert.assertEquals(bn.get("ID"), bnResult.get("ID"));
		Assert.assertEquals(bn.get("NAME"), bnResult.get("NAME"));
		Assert.assertEquals(bn.get("PATH"), bnResult.get("PATH"));
	}

	/**
	 * Tests update with exception on server-side. The mem databook should have the same rowcount after the 
	 * update as before the update.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testUpdate() throws Throwable
	{
		try
		{
			rdbApps.setSelectedRow(0);
			rdbApps.setValues(new String[] {"NAME"}, new Object[] {"error"});
			rdbApps.saveSelectedRow();
			
			Assert.fail("Update should throw an exception!");
		}
		catch (ModelException me)
		{
			Assert.assertEquals("not allowed",  ExceptionUtil.getRootCause(me).getMessage());
		}
		
		//server must reload
		Assert.assertEquals(3, sms.getDataBook().getRowCount());
		
		Object[] oValues = sms.getDataBook().getValues(new String[] {"ID", "NAME"});
		
		Assert.assertEquals(BigDecimal.valueOf(0), oValues[0]);
		Assert.assertEquals("First", oValues[1]);
	}

	/**
	 * Tests update of rows with specific columns (not all!).
	 * This tests also hidden mem columns in internal databook.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testUpdateBeanWithSpecificColumns() throws Throwable
	{
		sms.insert(new Object[] {BigDecimal.valueOf(0), "NAME", "/home/user"});
		
		Bean bn = new Bean();
		bn.put("ID", BigDecimal.valueOf(0));
		bn.put("NAME", "NAME-update");
        bn.put("PATH", "/home/user-update");
		
		Bean bnResult = sms.update(bn);
		
		Assert.assertEquals(bn.get("ID"), bnResult.get("ID"));
		Assert.assertEquals(bn.get("NAME"), bnResult.get("NAME"));
		
		//we didn't change PATH!!!
		Assert.assertEquals("/home/user-update", bnResult.get("PATH"));
	}
	
	/**
	 * Tests, if in any case the storage returns the correct array to the client.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInsertWithTrigger() throws Throwable
	{
		try
		{
			sms.eventAfterInsert().addListener(new IStorageListener()
			{
				public void storageChanged(StorageEvent pStorageEvent) throws DataSourceException
				{
					IBean bean = pStorageEvent.getNew();
					
					bean.put("NewColumn", "value");
				}
			});
			Object[] values = sms.insert(new Object[] {BigDecimal.valueOf(0), "NAME", "/home/user"});
			
			Assert.assertArrayEquals(new Object[] {BigDecimal.valueOf(0), "NAME", "/home/user"}, values);
	
			values = sms.insert(new Object[] {BigDecimal.valueOf(0), "NAME"});
			
			Assert.assertArrayEquals(new Object[] {BigDecimal.valueOf(0), "NAME"}, values);
		}
		finally
		{
			sms.eventAfterInsert().removeAllListeners();
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>SimpleMemStorage</code> is a {@link AbstractMemStorage} implementation for
	 * simple fetch tests.
	 * 
	 * @author René Jahn
	 */
	class SimpleMemStorage extends AbstractMemStorage
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RowDefinition getRowDefinition() throws ModelException
		{
			RowDefinition rowdef = new RowDefinition();
			rowdef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			rowdef.addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
			rowdef.addColumnDefinition(new ColumnDefinition("PATH", new StringDataType()));

			rowdef.setPrimaryKeyColumnNames(new String[] {"ID"});
			
			rowdef.setColumnView(null, new ColumnView("ID", "NAME"));
			
			return rowdef;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void loadData(MemDataBook pBook, ICondition pFilter) throws ModelException
		{
			pBook.deleteAllRows();
			
			pBook.insert(false);
			pBook.setValues(new String[] {"ID", "NAME", "PATH"}, new Object[] {BigDecimal.valueOf(0), "First", "/home/first"});
			pBook.insert(false);
			pBook.setValues(new String[] {"ID", "NAME", "PATH"}, new Object[] {BigDecimal.valueOf(1), "Second", "/home/second"});
			pBook.insert(false);
			pBook.setValues(new String[] {"ID", "NAME", "PATH"}, new Object[] {BigDecimal.valueOf(2), "Third", "/home/third"});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void insert(DataBookEvent pEvent) throws ModelException
		{
			if ("error".equals(pEvent.getChangedDataBook().getValueAsString("NAME")))
			{
				throw new ModelException("not allowed"); 
			}
		}

		@Override
		public void delete(DataBookEvent pEvent)
		{
		}

		@Override
		public void update(DataBookEvent pEvent) throws ModelException
		{
			if ("error".equals(pEvent.getChangedDataBook().getValueAsString("NAME")))
			{
				throw new ModelException("not allowed"); 
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Public access to data book.
		 * 
		 * @return the internal data book
		 */
		@Override
		public MemDataBook getDataBook()
		{
			return super.getDataBook();
		}
		
	}	// SimpleMemStorage
	
}	// TestAbstractMemStorage
