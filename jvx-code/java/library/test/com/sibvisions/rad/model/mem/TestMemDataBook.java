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
 * 11.10.2008 - [RH] - Master Detail tests optimized
 * 24.10.2008 - [RH] - refeshAllRows() -> to refresh renamed
 * 24.11.2008 - [RH] - changed to get/setReferencedDataBook()
 * 07.04.2009 - [RH] - interface review - Test cases adapted
 * 06.03.2011 - [JR] - #115: testDefaultValues
 * 28.02.2013 - [RH] - Sort Test added
 * 10.04.2013 - [RH] - Test case for saveAllRows add!!!!
 * 13.04.2013 - [RH] - #155 - Reload with SelectionMode==CURRENT and selfjoined tree's - testSelfjoinedReload()
 * 24.09.2013 - [RH] - #800 - MemDataBook ArrayIndexOutOfBoundsException during insert
 * 27.09.2013 - [RH] - #804 - MemDataBook for UITree with self-joined data
 */
package com.sibvisions.rad.model.mem;

import java.math.BigDecimal;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataBook.SelectionMode;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.TreePath;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.reference.ReferenceDefinition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.model.EventProtocol;
import com.sibvisions.util.ArrayUtil;

/**
 * Tests all Functions of com.sibvisions.rad.model.MemDataBook and
 * com.sibvisions.rad.model.MemDataPage. <br>
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.model.mem.DataBook
 * @see com.sibvisions.rad.model.mem.DataPage
 */
public class TestMemDataBook extends EventProtocol
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The DataBook test instance. */
	private MemDataBook	dbDataBook	= new MemDataBook();
	
	/** The ColumnDefintion Name for the RowDefintion. */
	private ColumnDefinition cdName = new ColumnDefinition("name");
	/** The ColumnDefintion Id for the RowDefintion. */
	private ColumnDefinition cdId = new ColumnDefinition("id");
	
	/** The MemDataSource test instance. */
	private MemDataSource	dba		= new MemDataSource();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * set the DataSource.
	 * 
	 * @throws Exception
	 *             if the setDataSource failed
	 */
	@Before
	public void initDataBook() throws Exception
	{
		dba.open();
		
		// init the RowDefinition
		dbDataBook.getRowDefinition().addColumnDefinition(cdName);
		dbDataBook.getRowDefinition().addColumnDefinition(cdId);		
		dbDataBook.getRowDefinition().setPrimaryKeyColumnNames(new String [] { cdId.getName() });
		 
		dbDataBook.getRowDefinition();
		
		dbDataBook.setName("TEST");
		Assert.assertTrue(dbDataBook.getName().equals("TEST"));			
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests if the selected row is correct if a {@link IDataBook#reload()}
	 * is fired in the {@link IDataBook#eventAfterRowSelected()}.
	 * 
	 * @throws ModelException if using the databook failed.
	 */
	@Test
	public void testSelectionAfterReloadInAfterRowSelected() throws ModelException
	{
		dbDataBook.close();
		dbDataBook.open();
		
		try
		{
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Roland Hörmann");
			dbDataBook.setValue("id", Integer.valueOf(1));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Rene Jahn");
			dbDataBook.setValue("id", Integer.valueOf(2));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Max Mustermann");
			dbDataBook.setValue("id", Integer.valueOf(3));
			dbDataBook.saveAllRows();
			
			dbDataBook.setFilter(new LikeIgnoreCase("name", "*mann"));
			
			dbDataBook.eventAfterRowSelected().addListener(new IDataBookListener()
			{
				public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
				{
					dbDataBook.reload();
				}
			});
			
			dbDataBook.setSelectedRow(1);
			
			Assert.assertEquals(1, dbDataBook.getSelectedRow());
		}
		finally
		{
			dbDataBook.eventAfterRowSelected().removeAllListeners();
			
			dbDataBook.setFilter(null);
		}
	}

	/**
	 * Tests saveAllRows after setFilter null on a Memdatabook. {@link IDataBook#eventAfterRowSelected()}.
	 * 
	 * @throws ModelException if using the databook failed.
	 */
	@Test
	public void testBug1247SaveAllRowsAftersetFilterNull() throws ModelException
	{
		dbDataBook.close();
		dbDataBook.open();
		
		try
		{
			dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Roland Hörmann");
			dbDataBook.setValue("id", Integer.valueOf(1));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Rene Jahn");
			dbDataBook.setValue("id", Integer.valueOf(2));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Max Mustermann");
			dbDataBook.setValue("id", Integer.valueOf(3));
			
			dbDataBook.setFilter(null);
			
			dbDataBook.saveAllRows();
			
			System.out.println(dbDataBook);
			Assert.assertEquals(0, dbDataBook.getChangedRows().length);
		}
		finally
		{
			dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
			dbDataBook.setFilter(null);
		}
	}
	
	/**
	 * Tests setFilter does not restore on a Memdatabook. {@link IDataBook#eventAfterRowSelected()}.
	 * 
	 * @throws ModelException if using the databook failed.
	 */
	@Test
	public void testNoRestoreOnSetFilterIfMemDataBook() throws ModelException
	{
		dbDataBook.close();
		dbDataBook.open();
		
		try
		{
			dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Roland Hörmann");
			dbDataBook.setValue("id", Integer.valueOf(1));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Rene Jahn");
			dbDataBook.setValue("id", Integer.valueOf(2));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Max Mustermann");
			dbDataBook.setValue("id", Integer.valueOf(3));
			
			dbDataBook.eventBeforeRestore().addListener(new IDataBookListener()
			{
				public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
				{
					throw new IllegalArgumentException("Do not restore!");
				}
			});
			
			dbDataBook.setFilter(new LikeIgnoreCase("name", "*mann"));
			
			dbDataBook.setSelectedRow(1);
			
			Assert.assertEquals(1, dbDataBook.getSelectedRow());
		}
		finally
		{
			dbDataBook.eventBeforeRestore().removeAllListeners();
			
			dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
			dbDataBook.setFilter(null);
		}
	}
	
	/**
	 * Test all typical DataBook methods.
	 * 
	 * @throws Exception
	 *             if not all DataBook methods work correctly
	 */
	@Test
	public void testBaseStatements() throws Exception
	{		
		// read all Data and display it
		dbDataBook.open();
		dbDataBook.fetchAll();
		Assert.assertTrue(dbDataBook.isAllFetched());
		
		// insert some test Data into the MemDataBook
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Roland Hörmann");
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Rene Jahn");
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Max Mustermann");
		dbDataBook.setValue("id", Integer.valueOf(3));
		dbDataBook.saveAllRows();
		
		// check ActiveRow
		dbDataBook.setSelectedRow(2);
		Assert.assertTrue(dbDataBook.getSelectedRow() == 2);
		System.out.println(dbDataBook);

		// insert some Data & check size()
		dbDataBook.fetchAll();
		int iSize = dbDataBook.getRowCount();
		dbDataBook.setSelectedRow(1);
		dbDataBook.insert(false);
		Assert.assertTrue(iSize + 1 == dbDataBook.getRowCount());
		
		dbDataBook.setValue("name", "insert(before)-test");
		System.out.println(dbDataBook);
		
		//dbDataBook.setSelectedRow(dbDataBook.getRowCount() - 1);
		dbDataBook.restoreAllRows();
		System.out.println(dbDataBook);
		
		// update some Data
		dbDataBook.setSelectedRow(1);
		IDataRow drOld = dbDataBook.getDataRow(dbDataBook.getSelectedRow());
		dbDataBook.setValue("name", "update()-testWWWW");
		System.out.println(drOld);
		System.out.println(dbDataBook.getOriginalDataRow());
	    Assert.assertTrue(drOld.equals(dbDataBook.getOriginalDataRow()));
		
		//delete
		dbDataBook.setSelectedRow(2);
		dbDataBook.delete();		
		System.out.println(dbDataBook);

		dbDataBook.setSelectedRow(1);		
		dbDataBook.setValues(new String[] {"name"},
				             new Object[] { dbDataBook.getDataRow(0).getValue("name")});
		Object oName = dbDataBook.getValue("name");
		dbDataBook.setSelectedRow(0);				
		Assert.assertEquals(oName, dbDataBook.getValue("name"));
				
		System.out.println(dbDataBook.hashCode());
		
		dbDataBook.restoreAllRows();		
		System.out.println(dbDataBook);				

		dbDataBook.setSelectedRow(0);
		dbDataBook.delete();		
		System.out.println(dbDataBook);
		dbDataBook.saveAllRows();
		System.out.println(dbDataBook);
		
		dbDataBook.close();			
	}

	/**
	 * Test the Master Detail behavior with a master and a detail DataBook.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testMasterDetail() throws Exception
	{
		dbDataBook.close();
		dbDataBook.setDataSource(dba);
			
		ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
		bdDETAILtoTEST.setReferencedDataBook(dbDataBook);
		bdDETAILtoTEST.setReferencedColumnNames(new String [] { "id" });
		bdDETAILtoTEST.setColumnNames(new String [] { "test_id" });
		
		MemDataBook dbDetail = new MemDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(bdDETAILtoTEST);
		dbDetail.setDataSource(dba);
		
		// init the RowDefinition
		RowDefinition rdRowDefinitionDetail = new RowDefinition();
		ColumnDefinition cdDetailName = new ColumnDefinition("name");
		ColumnDefinition cdTestId = new ColumnDefinition("test_id");
		rdRowDefinitionDetail.addColumnDefinition(cdDetailName);
		rdRowDefinitionDetail.addColumnDefinition(cdTestId);		
		rdRowDefinitionDetail.setPrimaryKeyColumnNames(new String [] { cdDetailName.getName() });
		
		dbDetail.setRowDefinition(rdRowDefinitionDetail);
		
		// read all Data and display it
		dbDataBook.setName("TEST");
		System.out.println(dbDataBook);		
		dbDataBook.open();

		System.out.println(dbDetail);
		dbDetail.open();
		
		// insert some test Data into the master MemDataBook
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Roland Hörmann");
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Rene Jahn");
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Max Mustermann");
		dbDataBook.setValue("id", Integer.valueOf(3));
		dbDataBook.saveAllRows();
		
		System.out.println(dbDataBook);
		System.out.println(dbDetail);

		IDataBook[] eDataBooks = dba.getDataBooks();

		Assert.assertTrue(eDataBooks.length == 2);
		
		// insert some Data
		dbDataBook.setSelectedRow(0);				
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "insert(before)-test");
		System.out.println(dbDataBook);
		
		dbDetail.insert(true);
		dbDetail.setValue("name", "detail-insert-test");
		System.out.println(dbDetail);
		Assert.assertEquals(dbDetail.getValue("test_id"), dbDataBook.getValue("id"));
		
		Object oRepageValue = dbDetail.getValue("test_id");

		dbDataBook.setSelectedRow(1);
		dbDetail.insert(true);
		dbDetail.setValue("name", "detail-insert-toActiceRow-test");
		System.out.println(dbDetail);

		// update some Data
		dbDetail.insert(true);
		dbDetail.setValue("name", "detail-update()-test");
		System.out.println(dbDetail);
		Assert.assertEquals(dbDetail.getValue("test_id"), dbDataBook.getValue("id"));

		// repage detail through setDataCellValue of the FK column
		System.out.println(dba);
		dbDetail.setValue("test_id", oRepageValue);
		System.out.println(dbDetail);
		System.out.println(dba);
		
		dba.saveAllDataBooks();

		System.out.println(dbDataBook);		
		for (int i = 0; i < dbDataBook.getRowCount(); i++)
		{
			dbDataBook.setSelectedRow(i);
			System.out.println(dbDetail);
		}
		
		//delete with or without detail rows !!!
		dbDataBook.setSelectedRow(2);				
		dbDataBook.delete();
		System.out.println(dbDataBook);
		dba.saveAllDataBooks();
		System.out.println(dbDataBook);
		
		for (int i = 0; i < dbDataBook.getRowCount(); i++)
		{
			dbDataBook.setSelectedRow(i);
			System.out.println(dbDetail);
		}

		// delete master with details
		dbDataBook.setSelectedRow(1);
		dbDataBook.delete();
		dba.saveAllDataBooks();
		System.out.println(dbDataBook);
		for (int i = 0; i < dbDataBook.getRowCount(); i++)
		{
			dbDataBook.setSelectedRow(i);
			System.out.println(dbDetail);
		}

		//delete detail
		dbDataBook.setSelectedRow(0);
		dbDetail.delete();
		dba.saveAllDataBooks();
		System.out.println(dbDataBook);
		for (int i = 0; i < dbDataBook.getRowCount(); i++)
		{
			dbDataBook.setSelectedRow(i);
			System.out.println(dbDetail);
		}
		
		// check setDataCellsValues
		dbDataBook.setValues(new String[] { "name"},
							 new Object[] { dbDataBook.getDataRow(0).getValue("name")});
		Assert.assertEquals(dbDataBook.getDataRow(0).getValue("name"), 
				dbDataBook.getValue("name"));
		
		// insert 100000 details
		dbDataBook.setSelectedRow(0);
		for (int i = 0; i < 100; i++)
		{
			dbDetail.insert(false);
			dbDetail.setValue("name", "" + i);
		}
		System.out.println(dbDataBook);	

		System.out.println(dbDetail);		
		dbDataBook.setSelectedRow(1);
		System.out.println(dbDetail.getRowCount());
		
		dbDataBook.setSelectedRow(0);
		dbDetail.deleteAllRows();
		dbDataBook.setSelectedRow(1);
		dbDetail.deleteAllRows();
		dba.saveAllDataBooks();
	}
	
	/**
	 * Test the Listeners of the DataBook.
	 * 
	 * @throws Exception
	 *             if not all Listener methods work correctly
	 */
	@Test
	public void testListener() throws Exception
	{
		// register listeners
		dbDataBook.eventBeforeRowSelected().addListener(this);
		dbDataBook.eventAfterRowSelected().addListener(this);
		dbDataBook.eventBeforeUpdating().addListener(this);
		dbDataBook.eventAfterUpdating().addListener(this);
		dbDataBook.eventBeforeUpdated().addListener(this);
		dbDataBook.eventAfterUpdated().addListener(this);
		dbDataBook.eventBeforeDeleting().addListener(this);
		dbDataBook.eventAfterDeleting().addListener(this);
		dbDataBook.eventBeforeDeleted().addListener(this);
		dbDataBook.eventAfterDeleted().addListener(this);
		dbDataBook.eventBeforeInserting().addListener(this);
		dbDataBook.eventAfterInserting().addListener(this);
		dbDataBook.eventBeforeInserted().addListener(this);
		dbDataBook.eventAfterInserted().addListener(this);
		dbDataBook.eventBeforeRestore().addListener(this);
		dbDataBook.eventAfterRestore().addListener(this);
		dbDataBook.eventBeforeReload().addListener(this);
		dbDataBook.eventAfterReload().addListener(this);
		dbDataBook.eventBeforeFilterChanged().addListener(this);
		dbDataBook.eventAfterFilterChanged().addListener(this);
		dbDataBook.eventBeforeSortChanged().addListener(this);
		dbDataBook.eventAfterSortChanged().addListener(this);
		dbDataBook.addControl(this);
		
		// read all Data and display it
		dbDataBook.setName("TEST");
		dbDataBook.open();
		dbDataBook.fetchAll();
		
		System.out.println(dbDataBook);

		// insert some Data
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "insert(before)-test");
		
		// update some Data
		dbDataBook.setSelectedRow(0);		
		dbDataBook.setValue("name", "update()-test");	
	
		dbDataBook.saveSelectedRow();
		
		dbDataBook.setSort(new SortDefinition("name"));
		dbDataBook.setFilter(new Equals("name", "eins"));
		
		dbDataBook.setFilter(null);
		dbDataBook.setSort(null);

		//delete
		dbDataBook.delete();
		
		dba.saveAllDataBooks();
		
		System.out.println(dbDataBook);

		dbDataBook.close();		
		
		System.out.println(getEventsAsString());		
	}
	
	/**
	 * Test the WriteBackIsolationLevel of the <code>MemDataSource</code>.
	 * 
	 * @throws Exception
	 *             if not all <code>MemDataSource</code> methods work correctly
	 */
	@Test
	public void testWriteBackIsolationLevel() throws Exception
	{		
		dbDataBook.close();
		dbDataBook.setDataSource(dba);
				
		dbDataBook.open();
		
		// Tests IDataSource.DATA_ROW
		////////////////////////////////
		
		System.out.println(dbDataBook);
		dba.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
		dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);

		// insert some test Data into the MemDataBook
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Roland Hörmann");
		dbDataBook.setValue("id", Integer.valueOf(1));
		Assert.assertTrue(dbDataBook.isInserting());
		
		// check if insert stores
		dbDataBook.insert(true);
		Assert.assertTrue(dbDataBook.getChangedRows()[0] == dbDataBook.getSelectedRow());
		Assert.assertTrue(dbDataBook.getChangedRows().length == 1);
		
		dbDataBook.setValue("name", "Rene Jahn");
		dbDataBook.setValue("id", Integer.valueOf(2));

		// check if setSelectedRow stores
		dbDataBook.setSelectedRow(1);
		Assert.assertTrue(getOnlyIUDChangedDataRows(dbDataBook).length == 0);
		
		dbDataBook.insert(true);
		Assert.assertTrue(getOnlyIUDChangedDataRows(dbDataBook).length == 1);
		dbDataBook.setValue("name", "Max Mustermann");
		dbDataBook.setValue("id", Integer.valueOf(3));

		// check if storeAllRows() stores
		dbDataBook.saveAllRows();		
		Assert.assertTrue(getOnlyIUDChangedDataRows(dbDataBook).length == 0);
		System.out.println(dbDataBook);

		// check if delete does store
		dbDataBook.delete();
		Assert.assertFalse(dbDataBook.isDeleting());
		dbDataBook.saveAllRows();		
		
		// check if setValue doesn't store
		dbDataBook.setValue("id", Integer.valueOf(100));
		Assert.assertTrue(dbDataBook.isUpdating());
		dbDataBook.saveAllRows();		
		
		System.out.println(dbDataBook);
		
		// Tests IDataBook.DATA_BOOK
		///////////////////////////////
		
		dba.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		
		// insert some test Data into the MemDataBook
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Roland Hörmann");
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Rene Jahn");
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Max Mustermann");
		dbDataBook.setValue("id", Integer.valueOf(3));
		
		dbDataBook.setSelectedRow(1);
		
		System.out.println(dbDataBook);
		Assert.assertTrue(getOnlyIUDChangedDataRows(dbDataBook).length == 3);
	}
	
	/**
	 * Test the mem filter of the DataBook.
	 * 
	 * @throws Exception
	 *             if not all DataBook methods work correctly
	 */
	@Test
	public void testMemFilter() throws Exception
	{		
		// read all Data and display it
		dbDataBook.close();
		dbDataBook.open();
		
		// insert some test Data into the MemDataBook
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Roland Hörmann");
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Rene Jahn");
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(true);
		dbDataBook.setValue("name", "Max Mustermann");
		dbDataBook.setValue("id", Integer.valueOf(3));
		dbDataBook.saveAllRows();

		System.out.println(dbDataBook);
		dbDataBook.setFilter(new LikeIgnoreCase("name", "r*"));
		System.out.println(dbDataBook);
		Assert.assertTrue(dbDataBook.getRowCount() == 2);		
	}
	
	/**
	 * Test the mem Sort of the DataBook.
	 * 
	 * @throws Exception
	 *             if not all DataBook methods work correctly
	 */
	@Test
	public void testMemSort() throws Exception
	{		
		// read all Data and display it
		dbDataBook.close();
		dbDataBook.open();
		
		// insert some test Data into the MemDataBook
		
		String sName5 = "Stefan Wurm";
		String sName4 = "Roland Hörmann";
		String sName3 = "Rene Jahn";
		String sName2 = "Peter Kofler";
		String sName1 = "Max Mustermann";
		String sName0 = "Martin Handsteiner";

		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName5);
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName4);
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName3);
		dbDataBook.setValue("id", Integer.valueOf(3));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName2);
		dbDataBook.setValue("id", Integer.valueOf(4));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName1);
		dbDataBook.setValue("id", Integer.valueOf(5));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName0);
		dbDataBook.setValue("id", Integer.valueOf(6));
		dbDataBook.saveAllRows();


		dbDataBook.setSort(new SortDefinition(new String[] {"name"}));

		Assert.assertTrue(dbDataBook.getDataRow(0).getValue("name").equals(sName0));		
		Assert.assertTrue(dbDataBook.getDataRow(1).getValue("name").equals(sName1));		
		Assert.assertTrue(dbDataBook.getDataRow(2).getValue("name").equals(sName2));		
		Assert.assertTrue(dbDataBook.getDataRow(3).getValue("name").equals(sName3));		
		Assert.assertTrue(dbDataBook.getDataRow(4).getValue("name").equals(sName4));		
		Assert.assertTrue(dbDataBook.getDataRow(5).getValue("name").equals(sName5));		
	}
	
	/**
	 * Test for bug #56.
	 * 
	 * @throws Exception
	 *             if master/detail work not correctly
	 */
	@Test
	public void test56() throws Exception
	{	
		final MemDataBook mdbMaster = new MemDataBook();
		mdbMaster.setName("categorylist");
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("IMAGE", new BigDecimalDataType()));
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
		mdbMaster.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
		mdbMaster.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
		mdbMaster.open();
	
		mdbMaster.insert(false);
		mdbMaster.setValues(new String[] {"ID", "IMAGE", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "TITEL1"});
		mdbMaster.insert(false);
		mdbMaster.setValues(new String[] {"ID", "IMAGE", "TITLE"}, new Object[] {BigDecimal.valueOf(2), new BigDecimal(2), "TITEL2"});
		mdbMaster.saveAllRows();
	
		System.out.println(mdbMaster);
		
		final MemDataBook mdbDetail = new MemDataBook();
		mdbDetail.setName("detail");
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
		mdbDetail.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
		mdbDetail.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
		mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
		mdbDetail.open();
	
		//mdbMaster.setSelectedRow(0);
		mdbDetail.insert(false);
		mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "DETAIL1"});
	
		System.out.println(mdbDetail);
		//mdbMaster.setSelectedRow(1);
		mdbDetail.insert(false);
		mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(2), new BigDecimal(2), "DETAIL2"});
	
		System.out.println(mdbDetail);
		//mdbMaster.setSelectedRow(0);
		mdbDetail.insert(false);
		mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(3), new BigDecimal(1), "DETAIL3"});
		mdbDetail.saveAllRows();
	
		mdbMaster.setSelectedRow(0);
		System.out.println(mdbDetail);
		mdbMaster.saveAllRows();
	}
	
	/**
	 * Test for bug #186.
	 * 
	 * @throws Exception
	 *             if changedDetail state is in master wrong, after Exception in detail.
	 */
	@Test
	public void test186() throws Exception
	{
		final MemDataBook mdbMaster = new MemDataBook();
		mdbMaster.setName("categorylist");
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("IMAGE", new BigDecimalDataType()));
		mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
		mdbMaster.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
		mdbMaster.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
		mdbMaster.open();
	
		mdbMaster.insert(false);
		mdbMaster.setValues(new String[] {"ID", "IMAGE", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "TITEL1"});
		mdbMaster.saveAllRows();
	
		System.out.println(mdbMaster);
		
		final MemDataBook mdbDetail = new MemDataBook();
		mdbDetail.setName("detail");
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
		mdbDetail.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
		mdbDetail.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
		mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
		mdbDetail.open();
	
		mdbDetail.insert(false);
		mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "DETAIL1"});
	
		System.out.println(mdbDetail);

		mdbDetail.insert(false);
		mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(2), new BigDecimal(2), "DETAIL2"});
		
		mdbDetail.saveAllRows();

		mdbDetail.eventAfterUpdating().addListener(new IDataBookListener() 
			{		
				public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
				{
					throw new ModelException("Expected Error to cause failure in updating");
				}
			});
		
		try 
		{
			mdbDetail.setValue("TITLE", "lalaal");
		}
		catch (ModelException e)
		{
			e.printStackTrace();
			
			Assert.assertTrue(mdbMaster.isDetailChanged());					
			mdbDetail.restoreSelectedRow();
			Assert.assertFalse(mdbMaster.isDetailChanged());		
		}
	}
	
	/**
	 * Test for bug #200.
	 * 
	 * @throws Exception if insert with filter fails
	 */	
	@Test
	public void test200() throws Exception
	{
		RowDefinition rowdef = new RowDefinition();
		rowdef.addColumnDefinition(new ColumnDefinition("APPL_ID", new StringDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("CLASSNAME", new StringDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("PATH", new StringDataType()));

		rowdef.setColumnView(null, new ColumnView("APPL_ID", "NAME", "CLASSNAME"));
		
		MemDataBook mdb = new MemDataBook();
		mdb.setName("test");
		mdb.setRowDefinition(rowdef);
		mdb.open();
		
		mdb.insert(false);
		mdb.setValues(new String[] {"APPL_ID"}, new Object[] {"Application"});

		mdb.setFilter(new Equals("APPL_ID", "Application"));
		mdb.deleteAllRows();
		
		mdb.insert(false);
		mdb.setValues(new String[] {"APPL_ID"}, new Object[] {"Application-2"});
	}

	/**
	 * Test for bug #223.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void test223() throws Exception
	{
		RowDefinition rdef = new RowDefinition();
		rdef.addColumnDefinition(new ColumnDefinition("FIRST"));

		MemDataBook mdb = new MemDataBook(rdef);
		mdb.setName("name");
		mdb.open();

		for (int i = 0; i < 2000; i++)
		{
		mdb.insert(false);
		mdb.setValues(new String[] {"FIRST"}, new String[] {"000" + i});
		}

		mdb.saveAllRows();

		mdb.setFilter(new LikeIgnoreCase("FIRST", "0000").or
		              (new LikeIgnoreCase("FIRST", "0001").or
		              (new LikeIgnoreCase("FIRST", "0003"))));

		mdb.setSort(new SortDefinition(false, "FIRST"));		
	}
	
	/**
	 * Tests for #1226, searchNext(ICondition) and searchPrevious(ICondition) are
	 * returning the index of the found row without adhering to the additional
	 * data row which might be visible.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void test1226SearchIndexWithAdditionalRow() throws Exception
	{
		dbDataBook.close();
		dbDataBook.open();
		
		try
		{
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Roland Hörmann");
			dbDataBook.setValue("id", Integer.valueOf(1));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Rene Jahn");
			dbDataBook.setValue("id", Integer.valueOf(2));
			dbDataBook.insert(false);
			dbDataBook.setValue("name", "Max Mustermann");
			dbDataBook.setValue("id", Integer.valueOf(3));
			dbDataBook.saveAllRows();
			
			// Sanity checks first, test if searchNext and searchPrevious are working at all.
			int index = dbDataBook.searchNext(new Equals("name", "Rene Jahn"));
			Assert.assertTrue("Searched for a row that should be there, test broken?", index >= 0);
			
			dbDataBook.setSelectedRow(index);
			Assert.assertEquals("Rene Jahn", dbDataBook.getValue("name"));
			
			index = dbDataBook.searchPrevious(new Equals("name", "Rene Jahn"));
			
			dbDataBook.setSelectedRow(index);
			Assert.assertEquals("Rene Jahn", dbDataBook.getValue("name"));
			
			// Activate the additional data row.
			dbDataBook.setAdditionalDataRowVisible(true);
			dbDataBook.getAdditionalDataRow().setValues(null, new Object[] { "Name", "ID" });
			
			// Now test searchNext and searchPrevious with the additional row visible.
			index = dbDataBook.searchNext(new Equals("name", "Rene Jahn"));
			
			dbDataBook.setSelectedRow(index);
			Assert.assertEquals("searchNext(ICondition) returns an incorrect index!", "Rene Jahn", dbDataBook.getValue("name"));
			
			index = dbDataBook.searchPrevious(new Equals("name", "Rene Jahn"));
			
			dbDataBook.setSelectedRow(index);
			Assert.assertEquals("searchPrevious(ICondition) returns an incorrect index!", "Rene Jahn", dbDataBook.getValue("name"));
		}
		finally
		{
			// Make sure that the additional row is removed
			dbDataBook.setAdditionalDataRowVisible(false);
			dbDataBook.getAdditionalDataRow().setDefaultValues();
		}
	}
	
	/**
	 * Tests if the default values from the ColumnDefinition are used.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testDefaultValues() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();

		ColumnDefinition coldef = new ColumnDefinition("DEFAULT");
		coldef.setDefaultValue("JVx");
		
		rowdef.addColumnDefinition(coldef);
		rowdef.addColumnDefinition(new ColumnDefinition("EMPTY"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("default");
		mdb.open();
		
		mdb.insert(false);
		
		Assert.assertEquals("JVx", mdb.getValue("DEFAULT"));
		Assert.assertNull("Invalid default value set!", mdb.getValue("EMPTY"));
	}
	
	/**
	 * Tests ticket 316, deleteAllRows and saveAllRows deletes not all rows in DataSource level.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket316() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();

		rowdef.addColumnDefinition(new ColumnDefinition("NAME"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("default");
		mdb.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdb.open();
		
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 1");
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 2");
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 3");
		mdb.saveAllRows();

		// Check if deleteAllRows and saveAllRows stores all rows!
		
		mdb.deleteAllRows();
		mdb.saveAllRows();
		
		Assert.assertEquals(0, mdb.getRowCount());
	}
	
	
	/**
	 * Tests ticket 350, if a deleteAllRows (mdb.isDeleteCascade() == false) and saveAllRows deletes not all rows in DataSource level.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket350() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();
		rowdef.addColumnDefinition(new ColumnDefinition("APPL"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("default");
		mdb.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdb.setDeleteCascade(false);
		mdb.open();
		
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp");
		mdb.saveSelectedRow();

		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("APPL"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_NAME"));
		
		MemDataBook detail = new MemDataBook(rowdefDetail);
		detail.setName("detail");
		detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		detail.setMasterReference(new ReferenceDefinition(new String[] { "APPL", "PARENT_NAME"}, detail, new String[] { "APPL", "NAME"}));
		detail.setRootReference(new ReferenceDefinition(new String[] { "APPL"}, mdb, new String[] { "APPL"}));
		detail.setDeleteCascade(false);
		detail.open();
		
		RowDefinition rowdefSubDetail = new RowDefinition();
		rowdefSubDetail.addColumnDefinition(new ColumnDefinition("APPL"));
		rowdefSubDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefSubDetail.addColumnDefinition(new ColumnDefinition("PROPERTY"));

		MemDataBook subdetail = new MemDataBook(rowdefSubDetail);
		subdetail.setName("subdetail");
		subdetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		subdetail.setMasterReference(new ReferenceDefinition(new String[] { "APPL", "NAME"}, detail, new String[] { "APPL", "NAME"}));
		subdetail.setDeleteCascade(false);
		subdetail.open();
		
		detail.insert(false);
		detail.setValue("NAME", "this");
		detail.saveSelectedRow();
		detail.setTreePath(new TreePath(0));
		detail.insert(false);
		detail.setValue("NAME", "field");
		detail.saveSelectedRow();

		subdetail.insert(false);
		subdetail.setValue("PROPERTY", "setName");
		subdetail.saveSelectedRow();
		
		detail.insert(false);
		detail.setValue("NAME", "field2");
		detail.saveSelectedRow();

		subdetail.insert(false);
		subdetail.setValue("PROPERTY", "setName2");
		subdetail.saveSelectedRow();
		
		// delete all, and check if we insert the master again, if the details are really deleted.		
		mdb.deleteAllRows();
		mdb.saveAllRows();
		Assert.assertEquals(0, mdb.getRowCount());
		
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp");
		mdb.saveSelectedRow();
		Assert.assertEquals(0, detail.getRowCount());
		Assert.assertEquals(0, subdetail.getRowCount());
		
		// delete in the hierachy
		
		detail.insert(false);
		detail.setValue("NAME", "this");
		detail.saveSelectedRow();
		detail.setTreePath(new TreePath(0));
		detail.insert(false);
		detail.setValue("NAME", "field");
		detail.saveSelectedRow();

		subdetail.insert(false);
		subdetail.setValue("PROPERTY", "setName");
		subdetail.saveSelectedRow();
		
		detail.insert(false);
		detail.setValue("NAME", "field2");
		detail.saveSelectedRow();

		subdetail.insert(false);
		subdetail.setValue("PROPERTY", "setName2");
		subdetail.saveSelectedRow();
		
		detail.setTreePath(null);
		detail.setSelectedRow(0);
		detail.delete();
		detail.saveSelectedRow();
		
		detail.insert(false);
		detail.setValue("NAME", "this");
		detail.saveSelectedRow();
		Assert.assertEquals(1, detail.getRowCount());
		Assert.assertEquals(0, subdetail.getRowCount());
	}

	/**
	 * Tests ticket 617, restoreAllRows fails with ArrayIndexOutOfBoundsException,
	 * -> change indices are wrong!
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket617() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();
		rowdef.addColumnDefinition(new ColumnDefinition("APPL"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("default");
		mdb.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdb.open();

		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("APPL"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		
		MemDataBook detail = new MemDataBook(rowdefDetail);
		detail.setName("detail");
		detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		detail.open();

		// insert two master with each two details 
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp1");
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp2");
		mdb.saveAllRows();

		detail.insert(false);
		detail.setValue("APPL", "neueApp1");
		detail.setValue("NAME", "neueApp1.Det1");
		detail.insert(false);
		detail.setValue("APPL", "neueApp1");
		detail.setValue("NAME", "neueApp1.Det2");
		detail.insert(false);
		detail.setValue("APPL", "neueApp2");
		detail.setValue("NAME", "neueApp2.Det1");
		detail.insert(false);
		detail.setValue("APPL", "neueApp2");
		detail.setValue("NAME", "neueApp2.Det2");
		detail.saveAllRows();

		//setfilter on detail for pseudo master-detail
		mdb.setSelectedRow(0);
		detail.setFilter(new Equals("APPL", "neueApp1"));
		
		// insert two examples
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp3");
		detail.setFilter(new Equals("APPL", "neueApp3"));
		
		mdb.insert(false);
		mdb.setValue("APPL", "neueApp4");
		detail.setFilter(new Equals("APPL", "neueApp3"));
		
		// delete the last one, and check if no index out of bounds exception happens
		detail.deleteAllRows();
		mdb.delete();
		detail.restoreAllRows();
	}
	
	/**
	 * Tests ticket 618, first make an insert, then change the last row, then make other changes,
	 * to force changes index adjustments.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket618() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();

		rowdef.addColumnDefinition(new ColumnDefinition("NAME"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("default");
		mdb.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdb.open();
		
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 1");
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 2");
		mdb.insert(false);
		mdb.setValue("NAME", "Zeile 3");
		mdb.saveAllRows();

		// Check if restoreAllRows don't get an index out of bounds exception.
		
		mdb.setSelectedRow(0);
		mdb.insert(false);
		mdb.setValue("NAME", "New nach Zeile 1");
		mdb.setSelectedRow(3);
		mdb.setValue("NAME", "last row chnaged");
		mdb.setSelectedRow(0);
		mdb.setValue("NAME", "Zeile 0 geändert");
		mdb.setSelectedRow(1);
		mdb.setValue("NAME", "New - Änderung");
		mdb.setSelectedRow(2);
		mdb.restoreAllRows();
		
		Assert.assertEquals(3, mdb.getRowCount());
	}
	
    /**
     * Returns only all Changed DataRows with the states isInserting(), isUpdating(), isDeleting(). 
     * Rows with isDetailChanged() will not be returned.
     * 
     * @param pDataBook the dataBook.
     * @return only all Changed DataRows with the states isInserting(), isUpdating(), isDeleting(). 
     * 
     * @throws ModelException if the changed rows couldn't determined.
     */
    public int[] getOnlyIUDChangedDataRows(IDataBook pDataBook) throws ModelException
    {
        int[] auChanges = pDataBook.getChangedRows();
        
        for (int i = 0; i < auChanges.length;)
        {
            IChangeableDataRow row = pDataBook.getDataRow(auChanges[i]);
            if (!(row.isDeleting() || row.isUpdating() || row.isInserting()))
            {
                auChanges = ArrayUtil.remove(auChanges, i);
            }
            else
            {
                i++;
            }
        }
        
        return auChanges;
    }       
    

	
	/**
	 * Tests if in Master- Detail - Detail incl. Selfjoined, realy all rows will be saved.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testSaveAllRows() throws ModelException
	{
		// init Master
		RowDefinition rowdefMaster = new RowDefinition();	
		rowdefMaster.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		rowdefMaster.addColumnDefinition(new ColumnDefinition("NAME"));
		 
		MemDataBook mdbMaster = new MemDataBook(rowdefMaster);
		mdbMaster.setName("master");
		mdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbMaster.open();
		
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(1), "Master 1"});
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(2), "Master 2"});

		// init Detail with selfjoined
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("DETAIL_ID", new BigDecimalDataType()));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_DETAIL_ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		
		MemDataBook mdbDetail = new MemDataBook(rowdefDetail);
		mdbDetail.setName("detail");
		mdbDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbDetail.setRootReference(new ReferenceDefinition(
				new String[] {"MASTER_ID"}, mdbMaster, new String[] {"MASTER_ID"}));
		mdbDetail.setMasterReference(new ReferenceDefinition(
				new String[] {"MASTER_ID", "PARENT_DETAIL_ID"}, mdbDetail, new String[] {"MASTER_ID", "DETAIL_ID"}));
		mdbDetail.open();
		
		String[] saColumnsDetail = new String [] {"DETAIL_ID", "NAME"};
		
		mdbMaster.setSelectedRow(0);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(1), "1 - Detail 1(1)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(2), "1 - Detail 2(2)"});

		mdbMaster.setSelectedRow(1);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(3), "2 - Detail 1(3)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(4), "2 - Detail 2(4)"});

		// add hierachy
		mdbDetail.setTreePath(new TreePath(1));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(5), "2 - Detail 2.1(5)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(6), "2 - Detail 2.2(6)"});
		
		// add hierachy
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(7), "2 - Detail 1.1(7)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(8), "2 - Detail 1.2(8)"});

		// init DetailDetail
		RowDefinition rowdefDetailDetail = new RowDefinition();
		rowdefDetailDetail.addColumnDefinition(new ColumnDefinition("DETAIL_DETAIL_ID", new BigDecimalDataType()));
		rowdefDetailDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetailDetail.addColumnDefinition(new ColumnDefinition("DETAIL_ID", new BigDecimalDataType()));
		
		MemDataBook mdbDetailDetail = new MemDataBook(rowdefDetailDetail);
		mdbDetailDetail.setName("detailDetail");
		mdbDetailDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbDetailDetail.setMasterReference(new ReferenceDefinition(
				new String[] {"DETAIL_ID"}, mdbDetail, new String[] {"DETAIL_ID"}));
		mdbDetailDetail.open();
		
		String[] saColumnsDetailDetail = new String [] {"DETAIL_DETAIL_ID", "NAME"};
		
		mdbMaster.setSelectedRow(0);
		mdbDetail.setSelectedRow(0);		
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(1), "1 - Detail 1 - DetailDetail 1"});
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(2), "1 - Detail 1 - DetailDetail 2"});		

		mdbMaster.setSelectedRow(1);				
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.setSelectedRow(0);		
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(3), "2 - Detail 1.1 - DetailDetail 1(3)"});
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(4), "2 - Detail 1.1 - DetailDetail 2(4)"});		

		mdbDetail.setSelectedRow(1);		
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(5), "2 - Detail 1.2 - DetailDetail 1(5)"});
		mdbDetailDetail.insert(false);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(6), "2 - Detail 1.2 - DetailDetail 2(6)"});	
		
		mdbMaster.setSelectedRow(0);
		mdbMaster.saveAllRows(); 
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbMaster).length);
		
		mdbDetail.saveAllRows();
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetail).length);
		
		mdbMaster.setSelectedRow(1);
		mdbDetail.setTreePath(null);
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetail).length);
		
		mdbDetailDetail.saveAllRows();
		mdbDetail.setTreePath(new TreePath(0));
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetail).length);
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetailDetail).length);
		
		// update && save
		mdbMaster.setSelectedRow(1);
		mdbDetail.setSelectedRow(0);
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetailDetail.setSelectedRow(1);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(2), "2 - Detail 1.1 - DetailDetail 2(4)- UPDATE"});	
		mdbDetailDetail.setSelectedRow(0);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(2), "2 - Detail 1.1 - DetailDetail 1(3)- UPDATE"});	

		mdbMaster.setSelectedRow(0);
		mdbDetail.setSelectedRow(0);
		mdbDetailDetail.setSelectedRow(1);
		mdbDetailDetail.setValues(saColumnsDetailDetail, new Object[] { new BigDecimal(1), "1 - Detail 1 - DetailDetail 1(1)- UPDATE"});

		mdbDetailDetail.saveAllRows();
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetailDetail).length);

		mdbMaster.setSelectedRow(1);
		mdbDetail.setTreePath(new TreePath(0));
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetailDetail).length);
		
		// delete && save	
		mdbMaster.setSelectedRow(1);
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.setSelectedRow(0);		
		mdbDetailDetail.deleteAllRows();
		
		mdbMaster.setSelectedRow(0);
		mdbDetailDetail.saveAllRows();
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetailDetail).length);
		
		// self joined delete
		mdbMaster.setSelectedRow(1);
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.deleteAllRows();
		mdbDetail.saveAllRows();
		mdbDetailDetail.saveAllRows();
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetail).length);
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(mdbDetailDetail).length);		
	}
	
	/**
	 * Tests for ticket #973. Inserting a row into a self-joined databook can
	 * lead to infinite loops as the new row receives its own datapage as detail
	 * datapage.
	 * <p/>
	 * <code><pre>
	 *   +----+-----+
	 *   | ID | PID |
	 *   +----+-----+
	 * I |  n |   n |
	 *   +----+-----+
	 * </pre></code> Here the newly inserted row is seen as master of itself.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void testTicket973SelfJoinedInfiniteTreePathProblem() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		rowDef.addColumnDefinition(new ColumnDefinition("PARENT_ID"));
		
		MemDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("data");
		dataBook.setMasterReference(new ReferenceDefinition(
				new String[] { "PARENT_ID" }, dataBook, new String[] { "ID" }));
		dataBook.open();
		
		dataBook.insert(false);
		
		Assert.assertNull(dataBook.getDataPage(new TreePath(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
	}
	
	/**
	 * Tests for ticket #973. Inserting a row into a self-joined databook can
	 * lead to infinite loops as the datapage of the new row might be seen as
	 * master of already existing rows.
	 * <p/>
	 * <code><pre>
	 *   +----+-----+
	 *   | ID | PID |
	 *   +----+-----+
	 *   |  1 |   n |
	 *   |  2 |   n |
	 *   |  3 |   1 |
	 *   |  4 |   3 |
	 * I |  n |   4 |
	 *   +----+-----+
	 * </pre></code> Here the newly inserted row is seen as master of the first.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void testTicket973SelfJoinedInfiniteTreePathProblemCircular() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		rowDef.addColumnDefinition(new ColumnDefinition("PARENT_ID"));
		
		MemDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("data");
		dataBook.setMasterReference(new ReferenceDefinition(
				new String[] { "PARENT_ID" }, dataBook, new String[] { "ID" }));
		dataBook.open();
		
		dataBook.insert(false);
		dataBook.setValue("ID", BigDecimal.valueOf(1));
		dataBook.setSelectedRow(0);
		dataBook.insert(false);
		dataBook.setValue("ID", BigDecimal.valueOf(2));
		dataBook.insert(false);
		dataBook.setValue("ID", BigDecimal.valueOf(3));
		dataBook.setSelectedRow(2);
		dataBook.insert(false);
		dataBook.setValue("ID", BigDecimal.valueOf(4));
		dataBook.saveAllRows();
		
		dataBook.setSelectedRow(4);
		dataBook.insert(false);
		
		Assert.assertNull(dataBook.getDataPage(new TreePath(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
	}
	
	/**
	 * Tests for ticket #973. Inserting a row into a self-joined databook can
	 * lead to infinite loops as the datapage of the new row might be seen as
	 * master of already existing rows.
	 * <p/>
	 * In the case of {@link IDataBook#restoreAllRows()} this can yield infinite
	 * recursion.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test(timeout = 500)
	public void testTicket973SelfJoinedInfiniteTreePathProblemComplex() throws ModelException
	{
		// init Master
		RowDefinition rowdefMaster = new RowDefinition();	
		rowdefMaster.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		rowdefMaster.addColumnDefinition(new ColumnDefinition("NAME"));
		 
		MemDataBook mdbMaster = new MemDataBook(rowdefMaster);
		mdbMaster.setName("master");
		mdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbMaster.open();
		
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(1), "Master 1"});
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(2), "Master 2"});

		// init Detail with selfjoined
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("DETAIL_ID", new BigDecimalDataType()));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_DETAIL_ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		
		MemDataBook mdbDetail = new MemDataBook(rowdefDetail);
		mdbDetail.setName("detail");
		mdbDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbDetail.setRootReference(new ReferenceDefinition(
				new String[] {"MASTER_ID"}, mdbMaster, new String[] {"MASTER_ID"}));
		mdbDetail.setMasterReference(new ReferenceDefinition(
				new String[] {"MASTER_ID", "PARENT_DETAIL_ID"}, mdbDetail, new String[] {"MASTER_ID", "DETAIL_ID"}));
		mdbDetail.open();
		
		String[] saColumnsDetail = new String [] {"DETAIL_ID", "NAME"};
		
		mdbMaster.setSelectedRow(0);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(1), "1 - Detail 1(1)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(2), "1 - Detail 2(2)"});

		mdbMaster.setSelectedRow(1);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(3), "2 - Detail 1(3)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(4), "2 - Detail 2(4)"});

		// add hierachy
		mdbDetail.setTreePath(new TreePath(1));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(5), "2 - Detail 2.1(5)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(6), "2 - Detail 2.2(6)"});
		
		// add hierachy
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(7), "2 - Detail 1.1(7)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(8), "2 - Detail 1.2(8)"});

		mdbMaster.saveAllRows();
		mdbDetail.saveAllRows();
		
		// Insert Sub
		IDataPage dataPage = mdbDetail.getDataPageWithRootRow(mdbDetail);
		mdbDetail.setTreePath(mdbDetail.getTreePath().getChildPath(mdbDetail.getRowIndex()));
		dataPage.getDataBook().insert(false);
		
		// Assert.assertNull(mdbDetail.getDataPage(new TreePath(0, 0, 0, 0, 0, 0, 0)));
		
		// Restore
		mdbDetail.restoreAllRows();
	}
	
	/**
	 * Tests if a reload in self joined table select the same current row!
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testSelfJoinedReload() throws ModelException
	{
		// init Master
		RowDefinition rowdefMaster = new RowDefinition();	
		rowdefMaster.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		rowdefMaster.addColumnDefinition(new ColumnDefinition("NAME"));
		 
		MemDataBook mdbMaster = new MemDataBook(rowdefMaster);
		mdbMaster.setName("master");
		mdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbMaster.open();
		
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(1), "Master 1"});
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(2), "Master 2"});

		// init Detail with selfjoined
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("DETAIL_ID", new BigDecimalDataType()));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_DETAIL_ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		
		MemDataBook mdbDetail = new MemDataBook(rowdefDetail);
		mdbDetail.setName("detail");
		mdbDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbDetail.setRootReference(new ReferenceDefinition(
				new String[] {"MASTER_ID"}, mdbMaster, new String[] {"MASTER_ID"}));
		mdbDetail.setMasterReference(new ReferenceDefinition(
				new String[] {"MASTER_ID", "PARENT_DETAIL_ID"}, mdbDetail, new String[] {"MASTER_ID", "DETAIL_ID"}));
		mdbDetail.open();
		
		String[] saColumnsDetail = new String [] {"DETAIL_ID", "NAME"};
		
		mdbMaster.setSelectedRow(0);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(1), "1 - Detail 1(1)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(2), "1 - Detail 2(2)"});

		mdbMaster.setSelectedRow(1);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(3), "2 - Detail 1(3)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(4), "2 - Detail 2(4)"});

		// add hierachy
		mdbDetail.setTreePath(new TreePath(1));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(5), "2 - Detail 2.1(5)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(6), "2 - Detail 2.2(6)"});
		
		// add hierachy
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(7), "2 - Detail 1.1(7)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(8), "2 - Detail 1.2(8)"});

		mdbMaster.saveAllRows();
		mdbDetail.saveAllRows();
		
		IDataRow drCurrent  = mdbDetail.getDataRow(mdbDetail.getSelectedRow());
		TreePath tpTreePath = mdbDetail.getTreePath();

		mdbDetail.reload(SelectionMode.CURRENT_ROW);
		Assert.assertEquals(tpTreePath, mdbDetail.getTreePath());
		Assert.assertEquals(drCurrent, mdbDetail.getDataRow(mdbDetail.getSelectedRow()));
	}
	
	
	/**
	 * Tests if update of a master-link column in a row, reparent to the new page after save.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testReparentAfterUpdateInsert() throws ModelException
	{
		// init Master
		RowDefinition rowdefMaster = new RowDefinition();	
		rowdefMaster.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		rowdefMaster.addColumnDefinition(new ColumnDefinition("NAME"));
		 
		MemDataBook mdbMaster = new MemDataBook(rowdefMaster);
		mdbMaster.setName("master");
		mdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbMaster.open();
		
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(1), "Master 1"});
		mdbMaster.insert(false);
		mdbMaster.setValues(rowdefMaster.getColumnNames(), new Object[] { new BigDecimal(2), "Master 2"});

		// init Detail with selfjoined
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("DETAIL_ID", new BigDecimalDataType()));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_DETAIL_ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		
		MemDataBook mdbDetail = new MemDataBook(rowdefDetail);
		mdbDetail.setName("detail");
		mdbDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		mdbDetail.setRootReference(new ReferenceDefinition(
				new String[] {"MASTER_ID"}, mdbMaster, new String[] {"MASTER_ID"}));
		mdbDetail.setMasterReference(new ReferenceDefinition(
				new String[] {"MASTER_ID", "PARENT_DETAIL_ID"}, mdbDetail, new String[] {"MASTER_ID", "DETAIL_ID"}));
		mdbDetail.setSort(new SortDefinition("NAME"));
		mdbDetail.open();
		
		String[] saColumnsDetail = new String [] {"DETAIL_ID", "NAME"};
		
		mdbMaster.setSelectedRow(0);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(1), "1 - Detail 1(1)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(2), "1 - Detail 2(2)"});

		mdbMaster.setSelectedRow(1);		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(3), "2 - Detail 1(3)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(4), "2 - Detail 2(4)"});

		// add hierachy
		mdbDetail.setTreePath(new TreePath(1));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(5), "2 - Detail 2.1(5)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(6), "2 - Detail 2.2(6)"});
		
		// add hierachy
		mdbDetail.setTreePath(new TreePath(0));
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(7), "2 - Detail 1.1(7)"});
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(8), "2 - Detail 1.2(8)"});

		mdbMaster.saveAllRows();
		mdbDetail.saveAllRows();
		
		// Update Detail
		mdbMaster.setSelectedRow(0);
		mdbDetail.setTreePath(null);
		mdbDetail.setSelectedRow(0);
		mdbDetail.setValue("MASTER_ID", new BigDecimal(2));
		mdbDetail.saveSelectedRow();		
		
		Assert.assertEquals(1, mdbDetail.getRowCount());
		
		mdbMaster.setSelectedRow(1);
		Assert.assertEquals(3, mdbDetail.getRowCount());
		
		mdbDetail.setSelectedRow(0);
		Assert.assertEquals("1 - Detail 1(1)", mdbDetail.getValue("NAME"));
		
		// insert new with different master id
		mdbMaster.setSelectedRow(0);
		mdbDetail.setTreePath(null);
		mdbDetail.setSelectedRow(0);
		
		mdbDetail.insert(false);
		mdbDetail.setValues(saColumnsDetail, new Object[] { new BigDecimal(9), "2 - Detail 3(9)"});
		mdbDetail.setValue("MASTER_ID", new BigDecimal(2));
		
		mdbDetail.saveAllRows();
		
		mdbMaster.setSelectedRow(1);
		Assert.assertEquals(4, mdbDetail.getRowCount());
	
		mdbDetail.setSelectedRow(3);
		Assert.assertEquals("2 - Detail 3(9)", mdbDetail.getValue("NAME"));		
	}
	
	/**
	 * Tests insert() and reparent.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testBug800() throws ModelException
	{	
		MemDataBook memContinent = new MemDataBook();
		MemDataBook memCountry = new MemDataBook();
		MemDataBook memCity = new MemDataBook();
	
		memContinent.setName("continent");
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT", new StringDataType()));
		memContinent.open();
			
		memCountry.setName("country");
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY", new StringDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT_ID", new BigDecimalDataType()));
		memCountry.setMasterReference(new ReferenceDefinition(new String[] {"CONTINENT_ID"}, memContinent, new String[] {"ID"}));
		memCountry.open();
			
		memCity.setName("city");
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY", new StringDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY_ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY_ID", new BigDecimalDataType()));
		memCity.setRootReference(new ReferenceDefinition(new String[] {"COUNTRY_ID"}, memCountry, new String[] {"ID"}));
		memCity.setMasterReference(new ReferenceDefinition(new String[] {"COUNTRY_ID", "CITY_ID"}, memCity, new String[] {"CITY_ID", "ID"}));
		memCity.open();		
	
		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(1), "Europe"});
			
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(1), "Austria", BigDecimal.valueOf(1)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(2), "Germany", BigDecimal.valueOf(1)});
			
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(1), "City " + 1});
	
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(2), "City " + 2});
		 
		memCity.setTreePath(new TreePath(1));
		 
		memCity.insert(false);
	}
	
	/**
	 * Tests insert() and reparent.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testBug875() throws ModelException
	{	
		MemDataBook memContinent = new MemDataBook();
		MemDataBook memCountry = new MemDataBook();
		MemDataBook memCity = new MemDataBook();

		memContinent.setName("continent");
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT", new StringDataType()));
		memContinent.open();
		
		memCountry.setName("country");
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY", new StringDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT_ID", new BigDecimalDataType()));
		memCountry.setMasterReference(new ReferenceDefinition(new String[] {"CONTINENT_ID"}, memContinent, new String[] {"ID"}));
		memCountry.open();
		
		memCity.setName("city");
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY", new StringDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY_ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY_ID", new BigDecimalDataType()));
		memCity.setRootReference(new ReferenceDefinition(new String[] {"COUNTRY_ID"}, memCountry, new String[] {"ID"}));
		memCity.setMasterReference(new ReferenceDefinition(new String[] {"COUNTRY_ID", "CITY_ID"}, memCity, new String[] {"CITY_ID", "ID"}));
		memCity.open();         
		
		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(1), "Europe"});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(1), "Austria", BigDecimal.valueOf(1)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(2), "Germany", BigDecimal.valueOf(1)});
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(1), "City " + 1});
		  
        memCity.insert(false);
        memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(2), "City " + 2});
		
		memCity.setTreePath(new TreePath(1));
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(3), "SubCity " + 3});
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(4), "SubCity " + 4});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(3), "England", BigDecimal.valueOf(1)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(4), "Italy", BigDecimal.valueOf(1)});
		
		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(2), "Asia"});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(5), "China", BigDecimal.valueOf(2)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(6), "Japan", BigDecimal.valueOf(2)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(7), "Russia", BigDecimal.valueOf(2)});
		
		
		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(3), "America"});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(8), "USA", BigDecimal.valueOf(3)});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(9), "Brasilien", BigDecimal.valueOf(3)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(10), "Chile", BigDecimal.valueOf(3)});
	}
	
	/**
	 * Tests insert() and selfjoined, without RootReference.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testBug804() throws ModelException
	{	
		MemDataBook mdbTree = new MemDataBook();
		mdbTree.setName("treeTableMem");
		mdbTree.getRowDefinition().addColumnDefinition(new ColumnDefinition("VG_ID", new BigDecimalDataType()));
		mdbTree.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new StringDataType()));
		mdbTree.setMasterReference(new ReferenceDefinition(new String[] {"VG_ID"}, mdbTree, new String[] {"ID"}));
		mdbTree.open();
		mdbTree.insert(false);
	}

	/**
	 * Tests if saveAllRows stores current selected row.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testSelectionInSaveAllRows() throws ModelException
	{
		// read all Data and display it
		dbDataBook.close();
		dbDataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		dbDataBook.open();
		
		// insert some test Data into the MemDataBook
		
		String sName5 = "Stefan Wurm";
		String sName4 = "Roland Hörmann";
		String sName3 = "Rene Jahn";
		String sName2 = "Peter Kofler";
		String sName1 = "Max Mustermann";
		String sName0 = "Martin Handsteiner";

		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName5);
		dbDataBook.setValue("id", Integer.valueOf(1));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName4);
		dbDataBook.setValue("id", Integer.valueOf(2));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName3);
		dbDataBook.setValue("id", Integer.valueOf(3));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName2);
		dbDataBook.setValue("id", Integer.valueOf(4));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName1);
		dbDataBook.setValue("id", Integer.valueOf(5));
		dbDataBook.insert(false);
		dbDataBook.setValue("name", sName0);
		dbDataBook.setValue("id", Integer.valueOf(6));
		dbDataBook.saveAllRows();


		dbDataBook.setSort(new SortDefinition(new String[] {"name"}));
System.out.println(dbDataBook);
		Assert.assertTrue(dbDataBook.getDataRow(0).getValue("name").equals(sName0));
		Assert.assertTrue(dbDataBook.getDataRow(1).getValue("name").equals(sName1));
		Assert.assertTrue(dbDataBook.getDataRow(2).getValue("name").equals(sName2));
		Assert.assertTrue(dbDataBook.getDataRow(3).getValue("name").equals(sName3));
		Assert.assertTrue(dbDataBook.getDataRow(4).getValue("name").equals(sName4));
		Assert.assertTrue(dbDataBook.getDataRow(5).getValue("name").equals(sName5));


		dbDataBook.setSelectedRow(5);
		dbDataBook.setValue("name", "A Wuam");
		
		dbDataBook.saveAllRows();
		
		Assert.assertEquals(0, dbDataBook.getSelectedRow());
		
		dbDataBook.setValue("name", "Stefan Wurm");
		
		dbDataBook.setSelectedRow(3);
		
		dbDataBook.saveAllRows();
		
		Assert.assertEquals(2, dbDataBook.getSelectedRow());
		
		dbDataBook.setSelectedRow(5);
		dbDataBook.setValue("name", "A Wuam");
		
		dbDataBook.setSelectedRow(3);
		
		dbDataBook.saveAllRows();
		
		Assert.assertEquals(4, dbDataBook.getSelectedRow());
	}
	
	/**
	 * Tests tree path in selfjoined data book.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTreePathWrongOnSave() throws ModelException
	{
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_ID"));
		
		MemDataBook detail = new MemDataBook(rowdefDetail);
		detail.setName("detail");
		detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		detail.setMasterReference(new ReferenceDefinition(new String[] {"PARENT_ID"}, detail, new String[] {"ID"}));
		detail.open();
		detail.setSort(new SortDefinition("NAME"));
		
		detail.insert(false);
		detail.setValues(null, new Object[] {"0", "Treepath: [] Row: 0", null});
		detail.insert(false);
		detail.setValues(null, new Object[] {"1", "Treepath: [] Row: 1", null});
		detail.insert(false);
		detail.setValues(null, new Object[] {"2", "Treepath: [] Row: 2", null});
		detail.insert(false);
		detail.setValues(null, new Object[] {"3", "Treepath: [1] Row: 0", "1"});
		detail.insert(false);
		detail.setValues(null, new Object[] {"4", "Treepath: [2] Row: 0", "2"});
		detail.insert(false);
		detail.setValues(null, new Object[] {"5", "Treepath: [2] Row: 1", "2"});
		detail.insert(false);
		detail.setValues(null, new Object[] {"6", "Treepath: [2,1] Row: 0", "5"});
		detail.insert(false);
		detail.setValues(null, new Object[] {"7", "Treepath: [2,1] Row: 1", "5"});
		detail.insert(false);
		detail.setValues(null, new Object[] {"8", "Treepath: [2,1] Row: 2", "5"});

		detail.saveAllRows();
		
		detail.setTreePath(new TreePath(2, 1));
		detail.setSelectedRow(2);
		detail.setValue("NAME", "A " + detail.getValue("NAME"));
		detail.setValue("PARENT_ID", null);
		detail.saveAllRows();
		
		Assert.assertEquals(new TreePath(3, 1), detail.getTreePath());
	}
		
	/**
	 * Tests UID rehash does not work.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testUIDRehash() throws ModelException
	{
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("PARENT_ID"));
		
		MemDataBook detail = new MemDataBook(rowdefDetail);
		detail.setName("detail");
		detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		detail.setMasterReference(new ReferenceDefinition(new String[] {"PARENT_ID"}, detail, new String[] {"ID"}));
		detail.open();
		detail.setSort(new SortDefinition("NAME"));
		
		detail.insert(false);
		detail.setValues(null, new Object[] {"0", "Treepath: [] Row: 0", null});
		detail.insert(false);
		detail.setValues(null, new Object[] {"1", "Treepath: [0] Row: 1", "0"});

		detail.saveAllRows();

		detail.setTreePath(new TreePath(0));

		Assert.assertNull(detail.getUID());
		
		System.out.println(detail);
	}

	/**
	 * Tests https://oss.sibvisions.com/index.php?do=details&task_id=1119.
	 * 
	 * @throws ModelException if test fails
	 */
	@Test
	public void test1119() throws ModelException
	{
	    BigDecimalDataType bdt = new BigDecimalDataType();
	    bdt.setPrecision(5);
	    bdt.setScale(0);
	    
	    StringDataType sdt = new StringDataType();
	    sdt.setSize(2);
	    
        RowDefinition rowdefDetail = new RowDefinition();
        rowdefDetail.addColumnDefinition(new ColumnDefinition("JVXTEST_ID", bdt));
        rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME", sdt));
	    
	    MemDataBook mdb = new MemDataBook(rowdefDetail);
	    mdb.setName("1119");
	    mdb.open();
	    
	    mdb.insert(false);
	    mdb.setValues(null, new Object[] {BigDecimal.valueOf(0), "2"});
	    
	    mdb.toString();
	    mdb.insert(false);
	}
	
	/**
	 * setFilter on master causes immediate fetch.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testSetFilterOnMasterCausesImmediateFetch() throws Exception
	{
		dbDataBook.close();
		dbDataBook.setDataSource(dba);
			
		ReferenceDefinition rdDETAILtoTEST = new ReferenceDefinition();
		rdDETAILtoTEST.setReferencedDataBook(dbDataBook);
		rdDETAILtoTEST.setReferencedColumnNames(new String [] { "id" });
		rdDETAILtoTEST.setColumnNames(new String [] { "test_id" });
		
		MemDataBook dbDetail = new MemDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(rdDETAILtoTEST);
		dbDetail.setDataSource(dba);
		
		// init the RowDefinition
		RowDefinition rdRowDefinitionDetail = new RowDefinition();
		ColumnDefinition cdDetailName = new ColumnDefinition("name");
		ColumnDefinition cdTestId = new ColumnDefinition("test_id");
		rdRowDefinitionDetail.addColumnDefinition(cdDetailName);
		rdRowDefinitionDetail.addColumnDefinition(cdTestId);		
		rdRowDefinitionDetail.setPrimaryKeyColumnNames(new String [] { cdDetailName.getName() });
		
		dbDetail.setRowDefinition(rdRowDefinitionDetail);
		
		// read all Data and display it
		dbDataBook.setName("TEST");
		dbDataBook.open();

		dbDetail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		dbDetail.open();
		
		for (int i = 0; i < 100; i++)
		{
			dbDataBook.insert(false);
			dbDataBook.setValues(IRowDefinition.ALL_COLUMNS, new Object[] {"Name " + i, BigDecimal.valueOf(i)});
			
			for (int j = 0; j < 5; j++)
			{
				dbDetail.insert(false);
				dbDetail.setValues(IRowDefinition.ALL_COLUMNS, new Object[] {"Detail Name " + i + "  " + j, BigDecimal.valueOf(i)});
			}
		}
		
		dbDataBook.saveAllRows();
		dbDetail.saveAllRows();
		dbDataBook.setSelectedRow(0);
		
		dbDataBook.eventAfterRowSelected().addListener(this);
		dbDataBook.setFilter(new Equals("id", BigDecimal.valueOf(0)));
		dbDataBook.setFilter(new Equals("id", BigDecimal.valueOf(1)));
		dbDataBook.setFilter(new Equals("id", BigDecimal.valueOf(2)));
		dbDataBook.setFilter(null);
		dbDataBook.getSelectedRow();
		
		Assert.assertEquals(1, getEvents().size());
	}
	
	/**
	 * Implicit save in insert does not always work.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testSaveWithEvents() throws Exception
	{
		final MemDataBook root = new MemDataBook();
		root.setName("root");
		root.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
		root.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		root.getRowDefinition().addColumnDefinition(new ColumnDefinition("CHANGED"));
		root.open();
		
		final MemDataBook master = new MemDataBook();
		master.setName("master");
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("CHANGED"));
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID"));
		master.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, root, new String[] {"ID"}));
		master.open();

		master.eventBeforeUpdating().addListener(new IDataBookListener()
		{
			public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
			{
				root.update();
				root.setValue("CHANGED", "Y");
			}
		});

		final MemDataBook detail = new MemDataBook();
		detail.setName("detail");
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID"));
		detail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, master, new String[] {"ID"}));
		detail.open();

		detail.eventBeforeInserting().addListener(new IDataBookListener()
		{
			public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
			{
				master.update();
				master.setValue("CHANGED", "Y");
			}
		});
		
		root.insert(false);
		root.setValues(null, new Object[] {"", "Root"});
		root.saveSelectedRow();

		master.insert(false);
		master.setValues(null, new Object[] {"1", "Master"});
		master.saveSelectedRow();
		
		
		detail.insert(false);
		detail.setValues(new String[] {"ID", "NAME"}, new Object[] {"a", "Detail1"});

		detail.insert(false);
		detail.setValues(new String[] {"ID", "NAME"}, new Object[] {"b", "Detail2"});
		
		System.out.println(detail);
	}
	
	/**
	 *  Tests if there are changed data rows without having a change.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testTicket1450ChangeDataRows() throws Exception
	{
		MemDataBook book = new MemDataBook();
		book.setName("book");
		book.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		book.setSort(new SortDefinition("NAME"));
		book.open();
		
		book.insert(false);
		book.setValue("NAME", "Beta");
		book.saveSelectedRow();
		
		book.insert(false);
		book.setValue("NAME", "Alpha");
		book.saveSelectedRow();
		
		Assert.assertEquals(0, book.getChangedRows().length);
	}

	/**
	 * Tests some kind of multithreading.
	 * 
	 * @throws Exception if some kind of multithreading fails
	 */	
	@Test
	public void testOpenMasterDetail() throws Exception
	{
		final MemDataBook master = new MemDataBook();
		master.setName("master");
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER"));
		
		final MemDataBook detail = new MemDataBook();
		detail.setName("detail");
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("DETAIL"));
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MAST_ID"));
		detail.setMasterReference(new ReferenceDefinition(new String[] {"MAST_ID"}, master, new String[] {"ID"}));
		detail.setDeleteCascade(true);
		
		
		master.open();

		detail.open();
		
		final long[] result = new long[1];
		
		final Exception[] threadExceptions = new Exception[3];
		
		final Thread thread0 = new Thread()
		{
			public void run()
			{
				try
				{
					for (int i = 0; i < 10000; i++)
					{
						synchronized (master.getRootDataBook())
						{
							master.insert(false);
							master.setValues(null, new Object[] {"" + i, "Master" + i});
						}	
						for (int j = 0; j < 10; j++)
						{
							int id = i * 10 + j;
							synchronized (detail.getRootDataBook())
							{
								detail.insert(false);
								detail.setValues(null, new Object[] {"" + id, "Detail" + id});
							}
						}
					}
				}
				catch (Exception ex)
				{
					threadExceptions[0] = ex;
				}
				
				synchronized (this)
				{
					this.notify();
				}
			}
		};
		
		final Thread thread1 = new Thread()
		{
			public void run()
			{
				try
				{
					while (master.getRowCount() > 0)
					{
						if (master.getSelectedRow() <= 0)
						{
							master.setSelectedRow(0);
						}
						if (thread0.isAlive() && master.getRowCount() < 10)
						{
							Thread.sleep(1);
						}
						
						master.delete();
					}
				}
				catch (Exception ex)
				{
					threadExceptions[1] = ex;
				}
				
				synchronized (this)
				{
					this.notify();
				}
			}
		};
		
		final Thread thread2 = new Thread()
		{
			public void run()
			{
				try
				{
					long sum = 0;

					while (master.getRowCount() > 0)
					{
						for (int i = 0, count = Math.min(20, master.getRowCount()); i < count; i++)
						{
							IDataRow row = master.getDataRow(i);
							if (row != null)
							{
								String id = (String)row.getValue("ID");
								
								if (id != null)
								{
									sum += Long.valueOf(id);
								}
							}
						}

						IDataPage dataPage = detail.getDataPage();

						for (int i = 0, count = Math.min(20, dataPage.getRowCount()); i < count; i++)
						{
							IDataRow row = dataPage.getDataRow(i);
							if (row != null)
							{
								String id = (String)row.getValue("ID");
								
								if (id != null)
								{
									sum += Long.valueOf(id);
								}
							}
						}
//						Thread.sleep(10);

					}
					result[0] = sum;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					threadExceptions[2] = ex;
				}
				
				synchronized (this)
				{
					this.notify();
				}
			}
		};
		
		thread0.start();
		Thread.sleep(100);
		thread1.start();
		thread2.start();
		
		synchronized (thread2)
		{
			thread2.wait();
		}
				
		for (int i = 0; i < threadExceptions.length; i++)
		{
			if (threadExceptions[i] != null)
			{
				throw threadExceptions[i];
			}
		}
		System.out.println("Finished: " + master.getRowCount() + "  " + result[0]);
	}
	
	/**
	 * Tests if reposition current data row is working correctly.
	 * The row should also only be repositioned, if it is necessary.
	 * 
	 * @throws ModelException if using the databook failed.
	 */
	@Test
	public void testRepositionDataRowTicket1451Ticket1563() throws ModelException
	{
		dbDataBook.close();
		dbDataBook.open();
		
		dbDataBook.setSort(new SortDefinition("name"));
		
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Martin");
		dbDataBook.setValue("id", "1");
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Martin");
		dbDataBook.setValue("id", "2");
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Martin");
		dbDataBook.setValue("id", "3");
		dbDataBook.saveAllRows();
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Adam");
		dbDataBook.setValue("id", "4");
		dbDataBook.saveAllRows();
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Robert");
		dbDataBook.setValue("id", "5");
		dbDataBook.saveAllRows();
		dbDataBook.setSelectedRow(1);
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Martin");
		dbDataBook.setValue("id", "6");
		dbDataBook.saveAllRows();
		
		Assert.assertEquals("4", dbDataBook.getDataRow(0).getValue("id"));
		Assert.assertEquals("1", dbDataBook.getDataRow(1).getValue("id"));
		Assert.assertEquals("6", dbDataBook.getDataRow(2).getValue("id"));
		Assert.assertEquals("2", dbDataBook.getDataRow(3).getValue("id"));
		Assert.assertEquals("3", dbDataBook.getDataRow(4).getValue("id"));
		Assert.assertEquals("5", dbDataBook.getDataRow(5).getValue("id"));
		
		dbDataBook.setSortDataRowOnSave(false);
		dbDataBook.setSelectedRow(1);
		dbDataBook.insert(false);
		dbDataBook.setValue("name", "Zenz");
		dbDataBook.setValue("id", "7");
		dbDataBook.saveAllRows();
		dbDataBook.setSortDataRowOnSave(true);

		Assert.assertEquals("7", dbDataBook.getDataRow(2).getValue("id"));
	}

    /**
     * Tests copy the records from one databook to another one.
     * 
     * @throws ModelException if the test fails
     */
    @Test
    public void testCopy() throws ModelException
    {
        RowDefinition rdSource = new RowDefinition();

        rdSource.addColumnDefinition(new ColumnDefinition("DEFAULT"));
        rdSource.addColumnDefinition(new ColumnDefinition("EMPTY"));
        
        RowDefinition rdTarget = new RowDefinition();

        rdTarget.addColumnDefinition(new ColumnDefinition("DEFAULT"));
        rdTarget.addColumnDefinition(new ColumnDefinition("EMPTY"));

        MemDataBook mdbSource = new MemDataBook(rdSource);
        mdbSource.setName("source");
        mdbSource.open();
        
        MemDataBook mdbTarget = new MemDataBook(rdTarget);
        mdbTarget.setName("target");
        mdbTarget.open();
        
        mdbSource.insert(false);
        mdbSource.setValues(null,  new String[] {"1", "NO"});
        mdbSource.insert(false);
        mdbSource.setValues(null,  new String[] {"2", "YES"});
        
        mdbSource.saveAllRows();
        
        for (int i = 0; i < mdbSource.getRowCount(); i++)
        {
            mdbTarget.insert(false);
            mdbTarget.setValues(rdTarget.getColumnNames(), mdbSource.getDataRow(i).getValues(rdTarget.getColumnNames()));
        }
        
        mdbTarget.saveAllRows();
        
        Assert.assertEquals(2, mdbTarget.getRowCount());
        Assert.assertEquals("YES", mdbSource.getDataRow(1).getValue("EMPTY"));
    }	
	
    /**
     * Tests that the Master is reporting an incorrect number of changed rows
     * after changes in a Detail.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testMasterReportsIncorrectChangesTicket1876() throws ModelException
    {
		IDataBook master = new MemDataBook();
		master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		master.setName("master");
		master.open();
		
		IDataBook detail = new MemDataBook();
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		detail.setMasterReference(new ReferenceDefinition(
				new String[] { "MASTER_ID" },
				master,
				new String[] { "ID" }));
		detail.setName("detail");
		detail.open();
		
		master.insert(false);
		detail.insert(false);
		detail.delete();
		
		detail.saveAllRows();
		master.saveAllRows();
		
		Assert.assertEquals(0, master.getChangedRows().length);
		Assert.assertFalse(master.isDeleting());
		Assert.assertFalse(master.isInserting());
		Assert.assertFalse(master.isUpdating());
		
		Assert.assertEquals(0, detail.getChangedRows().length);
		Assert.assertFalse(detail.isDeleting());
		Assert.assertFalse(detail.isInserting());
		Assert.assertFalse(detail.isUpdating());
    }
    
    /**
     * Tests updates to master data book in an event of a detail data book.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testUpdateIsInvokedTwiceTicket2202() throws ModelException
    {
        final IDataBook master = new MemDataBook();
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("VALUE", new BigDecimalDataType()));
        master.setName("master");
        master.open();
        
        final IDataBook detail = new MemDataBook();
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        detail.setMasterReference(new ReferenceDefinition(
                new String[] { "MASTER_ID" },
                master,
                new String[] { "ID" }));
        detail.setName("detail");
        detail.open();
        
        master.insert(false);
        master.setValues(null, new Object[] {"1", "100", null});

        detail.insert(false);
        detail.setValues(null, new Object[] {"1", "1"});
        detail.saveSelectedRow();
        
        detail.update();
        
        final IDataRow[] result = new IDataRow[2];
        
        detail.eventAfterUpdated().addListener(new IDataBookListener()
        {
            @Override
            public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable
            {
                master.setValue("VALUE", "200");
            }
        });
        
        master.eventBeforeUpdated().addListener(new IDataBookListener()
        {
            @Override
            public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable
            {
                result[0] = master.createDataRow(null);
                result[1] = pDataBookEvent.getOriginalDataRow();
            }
        });

        master.setValue("VALUE", "200");
        master.saveSelectedRow();
        
        Assert.assertEquals(BigDecimal.valueOf(100), result[1].getValue("VALUE"));
    }
    
    /**
     * Tests, if it is possible to edit data in an inserting row, if update enabled is false.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testInsertingRowOnUpdateEnabledFalseTicket2203() throws ModelException
    {
        final IDataBook master = new MemDataBook();
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("VALUE", new BigDecimalDataType()));
        master.setName("master");
        master.open();
        master.setUpdateEnabled(false);
        
        master.insert(false);
        System.out.println(master.isUpdateAllowed());
        master.saveSelectedRow();
        System.out.println(master.isUpdateAllowed());
    }

    /**
     * Tests, if empty Strings are mapped to null in case of autotrimEnd is enabled.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testAutotrimNullString() throws ModelException
    {
        StringDataType autoTrim = new StringDataType();
        autoTrim.setAutoTrimEnd(true);
        IDataBook db = new MemDataBook();
        db.setName("Test");
        db.getRowDefinition().addColumnDefinition(new ColumnDefinition("TEXT", autoTrim));
        db.open();
        
        db.insert(false);
        db.setValue("TEXT", "    ");
        db.saveSelectedRow();
        Assert.assertEquals(null, db.getValue("TEXT"));
    }
    
    /**
     * Tests, if selecting and changing values in master that are not part of master - detail relation causes an fetch in the detail data book.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testDoNotFetchIfMasterIsSelected() throws ModelException
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER"));
        master.open();

        MemDataBook detail = new MemDataBook();
        detail.setName("detail");
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("DETAIL"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID"));
        detail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, master, new String[] {"ID"}));
        detail.open();

        MemDataBook detailDetail = new MemDataBook();
        detailDetail.setName("detailDetail");
        detailDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        detailDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("DETAIL_DETAIL"));
        detailDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("DETAIL_ID"));
        detailDetail.setMasterReference(new ReferenceDefinition(new String[] {"DETAIL_ID"}, detail, new String[] {"ID"}));
        detailDetail.open();
        
        for (int i = 0; i < 10; i++)
        {
            master.insert(false);
            master.setValues(null, new Object[] {"" + i, "Master " + i});
            detail.insert(false);
            detail.setValues(null, new Object[] {"" + i, "Detail " + i, "" + i});
            detailDetail.insert(false);
            detailDetail.setValues(null, new Object[] {"" + i, "Detail Detail " + i, "" + i});
        }
        
        master.saveSelectedRow();
        detail.saveSelectedRow();
        detailDetail.saveSelectedRow();
        
        detail.eventAfterRowSelected().addListener(this);
        detailDetail.eventAfterRowSelected().addListener(this);
        
        for (int i = 0; i  < 10; i++)
        {
            master.setSelectedRow(i);
            master.setValue("MASTER", master.getValue("MASTER"));
        }

        master.setSelectedRow(0);

        Assert.assertEquals(0, getEvents().size());

    }

    /**
     * Tests, Ticket 2628.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testTicket2628() throws ModelException
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        master.open();
        
        MemDataBook detail = new MemDataBook();
        detail.setName("detail");
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID"));
        detail.setMasterReference(new ReferenceDefinition(
                new String[] {"MASTER_ID"}, master, new String[] {"ID"}));
        detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        detail.open();
        
        master.eventAfterInserted().addListener(master, "reload");
        // changed from AfterReload to BeforeReload, because reload calls restoreAllRows, which restors all details, 
        // so there is nothing to save AfterReload!
        master.eventBeforeReload().addListener(detail, "saveAllRows"); 
        
        final int[] counter = new int[1];
        
        detail.eventAfterInserted().addListener(new IDataBookListener()
        {
            public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable
            {
                counter[0]++;
            }
        });
        
        master.insert(false);
        master.setValues(null, new Object[] {"1", "master"});
        
        detail.insert(false);
        detail.setValues(null, new Object[] {"1", "detail", "1"});

        detail.saveSelectedRow();
        System.out.println(detail);
        Assert.assertEquals(1, counter[0]);
    }

    /**
     * Tests, Ticket 2765.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testTicket2765() throws ModelException
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        master.open();
        
        MemDataBook detail = new MemDataBook();
        detail.setName("detail");
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID"));
        detail.setMasterReference(new ReferenceDefinition(
                new String[] {"MASTER_ID"}, master, new String[] {"ID"}));
        detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        detail.open();
        
        master.insert(false);
        detail.insert(false);

        master.insert(false);
        detail.insert(false);

        master.insert(false);
        detail.insert(false);

        master.insert(false);
        detail.insert(false);

        master.setSelectedRow(0);
        
        Assert.assertEquals(1, detail.getRowCount());
    }

    /**
     * Tests, Ticket 2765.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testPerformance() throws ModelException
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRST_NAME"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("LAST_NAME"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("TYPE"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("INTERNAL_ID", new BigDecimalDataType()));
//        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        master.open();
        
        MemDataBook detail = new MemDataBook();
        detail.setName("detail");
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("COLUMN"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("REASON"));
        detail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, master, new String[] {"INTERNAL_ID"}));
//        detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        detail.open();
        

        long start = System.currentTimeMillis();
        int sumCount = 0;
        for (int i = 0; i < 1000000; i++)
        {
            master.insert(false);
            master.setValue("INTERNAL_ID", BigDecimal.valueOf(master.getRowCount()));
            
//            master.saveSelectedRow();
            
            sumCount += detail.getRowCount();
            
            master.delete();
            
            if (i % 100000 == 0)
            {
                System.out.println("Duration (" + i + "): " + (System.currentTimeMillis() - start) + "ms");
            }
        }
        
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");

        Assert.assertTrue("Duration has to be below 10 seconds", (System.currentTimeMillis() - start) < 10000);
    }
    
    /**
     * Tests, Ticket 2765.
     * 
     * @throws ModelException if the test fails.
     */
    @Test
    public void testDeleteEvent() throws ModelException
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRST_NAME"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("LAST_NAME"));
        master.open();
        
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        
        master.insert(false);
        master.setValues(null, new Object[] {"1", "Martin", "Handsteiner"});
        master.insert(false);
        master.setValues(null, new Object[] {"2", "Rene", "Jahn"});
        master.insert(false);
        master.setValues(null, new Object[] {"3", "Roland", "Hörmann"});
        
        master.saveSelectedRow();
         
//        master.setSelectedRow(0);
        
        master.eventBeforeRowSelected().addListener(this);
        master.eventAfterRowSelected().addListener(this);
        master.eventBeforeUpdating().addListener(this);
        master.eventAfterUpdating().addListener(this);
        master.eventBeforeUpdated().addListener(this);
        master.eventAfterUpdated().addListener(this);
        master.eventBeforeDeleting().addListener(this);
        master.eventAfterDeleting().addListener(this);
        master.eventBeforeDeleted().addListener(this);
        master.eventAfterDeleted().addListener(this);
        master.eventBeforeInserting().addListener(this);
        master.eventAfterInserting().addListener(this);
        master.eventBeforeInserted().addListener(this);
        master.eventAfterInserted().addListener(this);
        master.eventBeforeRestore().addListener(this);
        master.eventAfterRestore().addListener(this);
        master.eventBeforeReload().addListener(this);
        master.eventAfterReload().addListener(this);
        master.eventBeforeFilterChanged().addListener(this);
        master.eventAfterFilterChanged().addListener(this);
        master.eventBeforeSortChanged().addListener(this);
        master.eventAfterSortChanged().addListener(this);
        
        while (master.getRowCount() > 0)
        {
            master.delete();
            master.saveSelectedRow();
        }
        
        System.out.println(getEventsAsString());
        
    }
    
    /**
     * Ticket 2848, NPE on reload afterInserted on detail databook.
     * 
     * @throws Exception
     *             if master/detail work not correctly
     */
    @Test
    public void testNPEwhenReloadAfterInsertedTicket2848() throws Exception
    {   
        final MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("IMAGE", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
        mdbMaster.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
        mdbMaster.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
        mdbMaster.open();
    
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "IMAGE", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "TITEL1"});
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "IMAGE", "TITLE"}, new Object[] {BigDecimal.valueOf(2), new BigDecimal(2), "TITEL2"});
        mdbMaster.saveAllRows();
    
        final MemDataBook mdbDetail = new MemDataBook();
        mdbDetail.setName("detail");
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("TITLE"));
        mdbDetail.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
        mdbDetail.getRowDefinition().setColumnView(null, new ColumnView("TITLE"));
        mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
        mdbDetail.open();
    
        //mdbMaster.setSelectedRow(0);
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(1), new BigDecimal(1), "DETAIL1"});
    
        //mdbMaster.setSelectedRow(1);
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(2), new BigDecimal(2), "DETAIL2"});
    
        //mdbMaster.setSelectedRow(0);
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(3), new BigDecimal(1), "DETAIL3"});
        mdbDetail.saveAllRows();

        mdbDetail.eventAfterInserted().addListener(mdbDetail, "reload");
        
        mdbMaster.setSelectedRow(0);
        mdbDetail.setSelectedRow(0);
        
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "TITLE"}, new Object[] {BigDecimal.valueOf(4), new BigDecimal(1), "DETAIL4"});
        
        mdbDetail.setSelectedRow(2);
        System.out.println(mdbMaster);
        System.out.println(mdbDetail);
        
    }

    /**
     * Ticket 2877, detail changed flag in master wrong after delete in detail.
     * 
     * @throws Exception
     *             if master/detail work not correctly
     */
    @Test
    public void testDetailsChangedOnDeleteTicket2877() throws Exception
    {   
        MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbMaster.open();

        MemDataBook mdbDetail = new MemDataBook();
        mdbDetail.setName("detail");
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
        mdbDetail.open();

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});
        mdbMaster.saveAllRows();

        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "MASTER_ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "Detail 1"});
        mdbDetail.saveAllRows();

        mdbDetail.delete();
        
        Assert.assertFalse(mdbMaster.isDetailChanged());
    }

    /**
     * Ticket 2885, detail changed flag in master wrong after delete in detail.
     * 
     * @throws Exception
     *             if master/detail work not correctly
     */
    @Test
    public void testAfterReloadEventOnMasterDeleteTicket2885() throws Exception
    {   
        MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbMaster.open();
        
        MemDataBook mdbDetail = new MemDataBook();
        mdbDetail.setName("detail");
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
        mdbDetail.open();

        mdbDetail.eventAfterReload().addListener(this);
        
        mdbMaster.getSelectedRow();
        mdbDetail.getSelectedRow();

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});

        Assert.assertEquals(1, getEvents().size());

        mdbDetail.getSelectedRow();

        mdbMaster.restoreSelectedRow();
        
        Assert.assertEquals(2, getEvents().size());
        
        mdbDetail.getSelectedRow();
        
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2), "Master 2"});

        Assert.assertEquals(3, getEvents().size());

        mdbDetail.getSelectedRow();

        mdbMaster.delete();
        
        Assert.assertEquals(4, getEvents().size());

        mdbDetail.getSelectedRow();

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3), "Master 3"});

        mdbDetail.getSelectedRow();
        
        Assert.assertEquals(5, getEvents().size());

        mdbMaster.saveSelectedRow();
        
        Assert.assertEquals(6, getEvents().size());

        mdbDetail.getSelectedRow();

        mdbMaster.delete();
        
        Assert.assertEquals(7, getEvents().size());

        mdbDetail.getSelectedRow();

    }

    /**
     * Reload during save all rows.
     * 
     * @throws Exception if Event causes exception
     */
    @Test
    public void testReloadDuringSaveAllRowsTicket2950() throws Exception
    {   
        final MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        mdbMaster.open();

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2), "Master 2"});

        mdbMaster.eventAfterInserted().addListener(new IDataBookListener()
        {
            public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable
            {
                mdbMaster.reload();
            }
        });
        
        mdbMaster.saveAllRows();
        
        Assert.assertEquals(1, mdbMaster.getRowCount());
        Assert.assertArrayEquals(new int[0], mdbMaster.getChangedRows());
    }

    /**
     * Tests if saveSelectedRow in master does not save all rows.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testSaveSelectedRowOfMasterDataBookTicket2954() throws Throwable
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER"));
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        master.open();
        
        MemDataBook detail = new MemDataBook();
        detail.setName("detail");
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("DETAIL"));
        detail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MAST_ID"));
        detail.setMasterReference(new ReferenceDefinition(new String[] {"MAST_ID"}, master, new String[] {"ID"}));
        detail.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        detail.open();
        
        master.insert(false);
        master.setValues(null, new Object[] {"1", "Master 1"});
        
        detail.insert(false);
        detail.setValues(null, new Object[] {"1", "Detail 1-1", "1"});
        
        master.insert(false);
        master.setValues(null, new Object[] {"2", "Master 2"});
        
        detail.insert(false);
        detail.setValues(null, new Object[] {"2", "Detail 1-2", "2"});
        
        master.setSelectedRow(0);

        master.saveSelectedRow();
        
        Assert.assertArrayEquals(new int[] {1},   master.getChangedRows());
        Assert.assertArrayEquals(new int[] {},    detail.getChangedRows());

        master.setSelectedRow(1);

        Assert.assertArrayEquals(new int[] {1},   master.getChangedRows());
        Assert.assertArrayEquals(new int[] {0},    detail.getChangedRows());
    }
    
    /**
     * Tests if saveSelectedRow in master does not save all rows.
     * 
     * @throws Throwable if the test fails
     */
    @Test
    public void testWritebackIsolationLevelTicket3077() throws Throwable
    {
        MemDataBook master = new MemDataBook();
        master.setName("master");
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        master.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER"));
        master.open();
        
        master.insert(false);
        master.setValues(null, new Object[] {"1", "Master 1"});
        
        master.insert(false);
        master.setValues(null, new Object[] {"2", "Master 2"});

        master.saveAllRows();
        
        master.setSelectedRow(1);
        master.setValue("MASTER", "Master 2 Changed");
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        master.setSelectedRow(0);
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
   
        Assert.assertEquals(0, master.getChangedRows().length);

        master.setSelectedRow(1);
        master.setValue("MASTER", "Master 2 Changed");
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
        master.setSelectedRow(0);
        master.setSelectedRow(1);
        master.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
    
        Assert.assertEquals(1, master.getChangedRows().length);
    }

    /**
     * Reload during save all rows.
     * 
     * @throws Exception if Event causes exception
     */
    @Test
    public void testAfterRowSelectedInBeforeRowSelectedTicket2950() throws Exception
    {   
        final MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbMaster.open();

        final DataBookEvent[] event = new DataBookEvent[2]; 
        
        mdbMaster.eventBeforeRowSelected().addListener(new IDataBookListener()
        {
            public void dataBookChanged(DataBookEvent pEvent) throws Throwable
            {
                event[0] = pEvent;
                System.out.println("Before Row Selected! " + pEvent.getOldSelectedRow() + "  " + pEvent.getNewSelectedRow());
                
                mdbMaster.eventAfterRowSelected().addListener(new IDataBookListener()
                {
                    public void dataBookChanged(DataBookEvent pEvent) throws Throwable
                    {
                        event[1] = pEvent;
                        System.out.println("After Row Selected! " + pEvent.getOldSelectedRow() + "  " + pEvent.getNewSelectedRow());
                        
                        mdbMaster.eventAfterRowSelected().removeAllListeners();
                    }
                });
            }
        });
       
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});
        
        Assert.assertNotNull(event[0]);
        Assert.assertNotNull(event[1]);
        Assert.assertEquals(0, mdbMaster.eventAfterRowSelected().getListenerCount());
        
        event[0] = null;
        event[1] = null;

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});
        
        Assert.assertNotNull(event[0]);
        Assert.assertNotNull(event[1]);
        Assert.assertEquals(0, mdbMaster.eventAfterRowSelected().getListenerCount());

        event[0] = null;
        event[1] = null;
        
        mdbMaster.setSelectedRow(0);
        
        Assert.assertNotNull(event[0]);
        Assert.assertNotNull(event[1]);
        Assert.assertEquals(0, mdbMaster.eventAfterRowSelected().getListenerCount());

        event[0] = null;
        event[1] = null;
        
        mdbMaster.delete();
        
        Assert.assertNotNull(event[0]);
        Assert.assertNotNull(event[1]);
        Assert.assertEquals(0, mdbMaster.eventAfterRowSelected().getListenerCount());
    }

    /**
     * Reload during save all rows.
     * 
     * @throws Exception if Event causes exception
     */
    @Test
    public void testIteratorTicket1243() throws Exception
    {   
        final MemDataBook mdbMaster = new MemDataBook();
        mdbMaster.setName("categorylist");
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbMaster.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbMaster.open();

        MemDataBook mdbDetail = new MemDataBook();
        mdbDetail.setName("detail");
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
        mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
        mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
        mdbDetail.open();
        
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Master 1"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Detail 1-1"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2), "Detail 1-2"});

        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2), "Master 2"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3), "Detail 2-1"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(4), "Detail 2-2"});
        
        mdbMaster.insert(false);
        mdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3), "Master 3"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(5), "Detail 3-1"});
        mdbDetail.insert(false);
        mdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(6), "Detail 3-2"});
        
        mdbMaster.saveAllRows();
        
        for (IDataRow mRow : mdbMaster)
        {
            System.out.println(mRow);
            
            for (IDataRow dRow : mdbDetail.getDataPage(mRow))
            {
                System.out.println(dRow);
            }
        }
    }    
    
} 	// TestMemDataBook
