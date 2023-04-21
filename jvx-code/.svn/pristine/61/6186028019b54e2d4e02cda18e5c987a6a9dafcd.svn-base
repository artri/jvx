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
 * 02.11.2008 - [RH] - renamed to TestRemoteDataBook
 * 24.11.2008 - [RH] - changed to get/setReferencedDataBook()
 * 07.04.2009 - [RH] - interface review - Test cases adapted
 * 28.04.2011 - [RH] - #341 -  LikeReverse Condition, LikeReverseIgnoreCase Condition   
 * 23.12.2011 - [JR] - fixed testForumF2P705
 */
package com.sibvisions.rad.model.remote;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.rad.model.ColumnDefinition;
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
import javax.rad.model.condition.And;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LessEquals;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverse;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SubConnection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.rad.model.EventProtocol;
import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.persist.jdbc.HSQLDBAccess;
import com.sibvisions.rad.util.DirectObjectConnection;
import com.sibvisions.util.ArrayUtil;

import remote.net.VMConnection;

/**
 * Tests all Functions of com.sibvisions.rad.model.StorageDataBook. <br>
 * Run first the TestDBAccess Test class.
 * 
 * @author Roland Hörmann
 * @see com.sibvisions.rad.model.mem.DataBook
 * @see com.sibvisions.rad.model.mem.DataPage
 */
public class TestRemoteDataBook extends EventProtocol
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The data book test instance. */
	private RemoteDataBook rdbMaster	   = new RemoteDataBook();

	/** The data book adressen instance. */
	private RemoteDataBook rdbAdressen = new RemoteDataBook();

	/** The remote data source test instance. */
	private RemoteDataSource rds		= new RemoteDataSource();


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Connect to the Test Database.
	 * 
	 * @throws Throwable if the connect to the DB fails
	 */
	@Before
	public void open() throws Throwable
	{		
		MasterConnection connection = new MasterConnection(new VMConnection());
		connection.setApplicationName("demo");
		connection.setUserName("rene");
		connection.setPassword("rene");
		connection.open();

		SubConnection sub = connection.createSubConnection("demo.StorageDataBookTest");
		sub.open();
		
		rds.setConnection(sub);
		rds.open();

		rdbMaster.setDataSource(rds);
		rdbAdressen.setDataSource(rds);
	}
	
	/**
	 * Closes all opened connections and statements.
	 */
	@After
	public void close()
	{
		rdbMaster.close();
		rdbAdressen.close();
		
		SubConnection con = (SubConnection)rds.getConnection(); 
		
		try
		{
			con.close();
			con.getMasterConnection().close();
		}
		catch (Throwable th)
		{
			//nothing to be done
		}
		
		rds.close();
	}
	
	/**
	 * Init the Database for the test.
	 * 
	 * @throws Throwable the database couldn't initialized.
	 */
	@BeforeClass
	public static void init() throws Throwable
	{
		HSQLDBAccess dbAccess	= new HSQLDBAccess();
		
		// set connect properties
		dbAccess.setUrl("jdbc:hsqldb:hsql://localhost/testdb");
		dbAccess.setUsername("sa");
		dbAccess.setPassword("");
	
		// open and check
		dbAccess.open();
		Assert.assertTrue(dbAccess.isOpen());
		
		// drop test table
		try
		{
			dbAccess.executeStatement("drop table detail");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		try
		{
			dbAccess.executeStatement("drop table test");
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}

		// create test table
		dbAccess.executeStatement("create table test (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100))");
		
		// create detail table
		dbAccess.executeStatement("create table detail (id integer GENERATED BY DEFAULT AS IDENTITY primary key, name varchar(100), " +
				"test_id INTEGER, FOREIGN KEY (test_id) REFERENCES test (id))");
		
		// Test db 2
		HSQLDBAccess dba2 = new HSQLDBAccess();
		dba2.setUrl("jdbc:hsqldb:hsql://localhost/personsdb"); 
		dba2.setUsername("sa");
		dba2.setPassword("");		
		
		dba2.open();		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	 * Test select, insert, update, delete, save, open, close,
	 * setDataCellValue, loadAllRows, setName in DataBook.
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */	
	@Test
	public void testInsertUpdateDelete() throws Exception
	{
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		int iSizeBefore = rdbMaster.getRowCount();

		// insert some Data
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		Assert.assertTrue(rdbMaster.isInserting());
		Assert.assertEquals(1, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		rdbMaster.saveSelectedRow();
		
		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		// update some Data
		rdbMaster.setSelectedRow(1);
		rdbMaster.setValue("NAME", "update()-test");	
		Assert.assertTrue(rdbMaster.isUpdating());
		Assert.assertEquals(1, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.saveSelectedRow();
		
		//delete
		rdbMaster.setSelectedRow(1);
		rdbMaster.delete();
		
		Assert.assertFalse(rdbMaster.isDeleting());
		Assert.assertEquals(0, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.saveAllRows();
		
		rdbMaster.fetchAll();
        rdbMaster.reload();
        rdbMaster.restoreSelectedRow();
        rdbMaster.restoreAllRows();
		
		rdbMaster.reload();	
		rdbMaster.fetchAll();
		Assert.assertEquals(iSizeBefore + 1, rdbMaster.getRowCount());
	}
	
	/**
	 * Test all typical DataBook methods.
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */
	@Test
	public void testBaseStatements() throws Exception
	{
		// set Table
		rdbMaster.setName("test");

		// init the RowDefinition
		RowDefinition rdRowDefinition = new RowDefinition();
		
		ColumnDefinition cdName = new ColumnDefinition("NAME");
		ColumnDefinition cdId = new ColumnDefinition("ID");
		
		rdRowDefinition.addColumnDefinition(cdName);
		rdRowDefinition.addColumnDefinition(cdId);		
		rdRowDefinition.setPrimaryKeyColumnNames(new String [] { cdId.getName() });
		
		rdbMaster.setRowDefinition(rdRowDefinition);
		
		rdbMaster.setName("TEST");
		Assert.assertTrue(rdbMaster.getName().equals("TEST"));		
		
		// read all Data and display it
		rdbMaster.setReadAhead(1000);
		rdbMaster.open();
		Assert.assertTrue(rdbMaster.isAllFetched());

		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		rdbMaster.saveSelectedRow();

		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test2");
		rdbMaster.saveSelectedRow();

		rdbMaster.close();
		rdbMaster.setReadAhead(0);
		rdbMaster.open();
		
		// check ActiveRow
		rdbMaster.setSelectedRow(1);
		Assert.assertEquals(1, rdbMaster.getSelectedRow());

		// insert some Data & check size()
		rdbMaster.fetchAll();
		int iSize = rdbMaster.getRowCount();
		rdbMaster.setSelectedRow(1);
		rdbMaster.insert(true);
		Assert.assertEquals(iSize + 1, rdbMaster.getRowCount());
		
		rdbMaster.setValue("NAME", "insert(before)-test");
		rds.saveAllDataBooks();
		
		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		rdbMaster.setSelectedRow(rdbMaster.getRowCount() - 1);
		rdbMaster.reload();
		
		// update some Data
		rdbMaster.setSelectedRow(1);
		IDataRow drOld = rdbMaster.getDataRow(rdbMaster.getSelectedRow());
		rdbMaster.setValue("NAME", "update()-testWWWW");
	    Assert.assertTrue(drOld.equals(rdbMaster.getOriginalDataRow()));
		
		//delete
		rdbMaster.setSelectedRow(2);
		rdbMaster.delete();
		
		rds.saveAllDataBooks();

		// check getRowCount()
		rdbMaster.fetchAll();
		rdbMaster.reload();
		
		Assert.assertEquals(iSize, rdbMaster.getRowCount());
				
		rdbMaster.setSelectedRow(1);		
		rdbMaster.setValues(new String[] {"NAME"},
							 new Object[] { rdbMaster.getDataRow(0).getValue("NAME")});
		Object oName = rdbMaster.getValue("NAME");
		rdbMaster.setSelectedRow(0);				
		Assert.assertEquals(oName, rdbMaster.getValue("NAME"));
				
		// check getDataCellValueForSelectedDataRows(String pColumnDefinitionName)
/*		for (int i = 0; i < dbDataBook.getRowCount(); i++)
		{
			dbDataBook.addSelectedDataRow(i);
		}
		Assert.assertEquals(MemDataBook.SELECTEDDATAROW_VALUES_NOT_EQUAL_CHARACTERS, 
				dbDataBook.getDataCellValueForSelectedDataRows("NAME"));
*/
	}

	/**
	 * Test the the Enable functions / Readonly of the RemoteDataBook.
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */
	@Test
	public void testReadonly() throws Exception
	{
		rdbMaster.setName("TEST");
		rdbMaster.open();
		
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test2");
		rdbMaster.saveSelectedRow();
		rdbMaster.close();
		
		rdbMaster.setReadOnly(true);
		rdbMaster.open();
		rdbMaster.fetchAll();
		rdbMaster.setSelectedRow(0);
		
		try
		{
			rdbMaster.insert(true);

			// no Exception means its allowed it, but it isn't
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().contains("Insert isn't allowed!"))			
			{
				throw new Exception(modelException);
			}
		}
		
		try
		{
			rdbMaster.setValue("NAME", "try update");

			// no Exception means its allowed it, but it isn't
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			if (!modelException.getMessage().contains("Update isn't allowed!"))
			{
				throw new Exception(modelException);
			}
		}
		
		rdbMaster.setReadOnly(false);
		rdbMaster.delete();

		rdbMaster.insert(true);
		
		rdbMaster.setValue("NAME", "try update");
	}

	/**
	 * Test the Filter and Sort of the DataBook.
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */
	@Test
	public void testFilterSort() throws Exception
	{
		// delete all
		rdbMaster.setName("TEST");		
		rdbMaster.open();
		rdbMaster.fetchAll();

		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setName("DETAIL");
		rdbDetail.setSelectionMode(SelectionMode.FIRST_ROW);
		rdbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "ID" }));
		rdbDetail.setDataSource(rds);
		rdbDetail.setDeleteCascade(true);
		rdbDetail.open();
		
		rdbMaster.deleteAllRows();
		rds.saveAllDataBooks();
		
		// insert()
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert()");
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert2()");
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "zinsert2()");

		rds.saveAllDataBooks();
		
		// select data from test table with filter
		DataRow drFilter = new DataRow(rdbMaster.getRowDefinition());
		drFilter.setValue("NAME", "%insert2%");

		ICondition cFilter = new Like(drFilter, "NAME");
		
		rdbMaster.close();
		rdbMaster.setFilter(cFilter);
		
		SortDefinition sSort = new SortDefinition(new String[] { "NAME"});
		rdbMaster.setSort(sSort);
		
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		rdbMaster.setSelectedRow(1);		
		Assert.assertTrue(rdbMaster.getValue("NAME").equals("zinsert2()"));
		Assert.assertFalse(rdbMaster.getRowCount() != 2);		
	}	

	/**
	 * Test the LikeReserve Filter. 
	 * 
	 * @throws Exception if not all DataBook methods work correctly
	 */
	@Test
	public void testFilterLikeReverse() throws Exception
	{
		// delete all
		rdbMaster.setName("TEST");		
		rdbMaster.open();
		rdbMaster.fetchAll();

		rdbMaster.deleteAllRows();
		rds.saveAllDataBooks();
		
		// insert()
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "*name*");
		rds.saveAllDataBooks();
		
		// select data from test table with filter
		rdbMaster.setFilter(new LikeReverse("NAME", "First name"));
				
		rdbMaster.setSelectedRow(1);		
		Assert.assertEquals(1, rdbMaster.getRowCount());		
	}
	
	/**
	 * Test the Master Detail behavior with a master and a detail DataBook.
	 * 
	 * @throws Exception if the master/detail support work incorrectly
	 */	
	@Test
	public void testMasterDetail() throws Exception
	{
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
		bdDETAILtoTEST.setReferencedDataBook(rdbMaster);
		bdDETAILtoTEST.setReferencedColumnNames(new String [] { "ID" });
		bdDETAILtoTEST.setColumnNames(new String [] { "TEST_ID" });
		
		RemoteDataBook dbDetail = new RemoteDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(bdDETAILtoTEST);
		dbDetail.setDataSource(rds);
		dbDetail.open();

		IDataBook[] eDataBooks = rds.getDataBooks();

		Assert.assertTrue(eDataBooks.length == 2);
		
		// insert some Data
		rdbMaster.setSelectedRow(0);				
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-insert-test");
		dbDetail.saveSelectedRow();
		Assert.assertEquals(dbDetail.getValue("TEST_ID"), rdbMaster.getValue("ID"));
		
		Object oRepageValue = dbDetail.getValue("TEST_ID");

		rdbMaster.setSelectedRow(1);
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-insert-toActiceRow-test");

		// update some Data
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-update()-test");
		Assert.assertEquals(dbDetail.getValue("TEST_ID"), rdbMaster.getValue("ID"));

		// repage detail through setDataCellValue of the FK column
		dbDetail.setValue("TEST_ID", oRepageValue);
		
		rds.saveAllDataBooks();

		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		dbDetail.open();
		
		for (int i = 0; i < rdbMaster.getRowCount(); i++)
		{
			rdbMaster.setSelectedRow(i);
		}
		
		//delete - not cascade delete with or without detail rows !!!
		rdbMaster.setSelectedRow(2);				
		rdbMaster.delete();
		rds.saveAllDataBooks();
		for (int i = 0; i < rdbMaster.getRowCount(); i++)
		{
			rdbMaster.setSelectedRow(i);
		}
		
		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		dbDetail.open();
		
		for (int i = 0; i < rdbMaster.getRowCount(); i++)
		{
			rdbMaster.setSelectedRow(i);
		}

		//delete - with cascade delete
		// load all details
		rdbMaster.setSelectedRow(rdbMaster.getRowCount() - 1);
		dbDetail.fetchAll();
		
		rdbMaster.setSelectedRow(rdbMaster.getRowCount() - 1);
		rdbMaster.setDeleteCascade(true);
		dbDetail.setDeleteCascade(true);
		rdbMaster.delete();
		rds.saveAllDataBooks();
		for (int i = 0; i < rdbMaster.getRowCount(); i++)
		{
			rdbMaster.setSelectedRow(i);
		}

		//delete detail
		rdbMaster.setSelectedRow(0);
		dbDetail.delete();
		rds.saveAllDataBooks();
		for (int i = 0; i < rdbMaster.getRowCount(); i++)
		{
			rdbMaster.setSelectedRow(i);
		}
		
		// check setDataCellsValues
		rdbMaster.setValues(new String[] { "NAME"},
				             new Object[] { rdbMaster.getDataRow(0).getValue("NAME")});
		Assert.assertEquals(rdbMaster.getDataRow(0).getValue("NAME"), 
				rdbMaster.getValue("NAME"));
		
		// insert 100000 details, and then test loadAllRows(200ms)
		rdbMaster.setSelectedRow(0);
		for (int i = 0; i < 100; i++)
		{
			dbDetail.insert(false);
			dbDetail.setValue("NAME", BigDecimal.valueOf(1));
		}
		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		dbDetail.open();

		rdbMaster.setSelectedRow(1);
		
		rdbMaster.setSelectedRow(0);
		dbDetail.deleteAllRows();
		rdbMaster.setSelectedRow(1);
		dbDetail.deleteAllRows();
		rds.saveAllDataBooks();
	}
	
	/**
	 * Test the Listeners of the DataBook.
	 * 
	 * @throws Exception if not all Listener methods work correctly
	 */
	@Test
	public void testListener() throws Exception
	{
		// register listeners
		rdbMaster.eventBeforeRowSelected().addListener(this);
		rdbMaster.eventAfterRowSelected().addListener(this);
		rdbMaster.eventBeforeUpdating().addListener(this);
		rdbMaster.eventAfterUpdating().addListener(this);
		rdbMaster.eventBeforeUpdated().addListener(this);
		rdbMaster.eventAfterUpdated().addListener(this);
		rdbMaster.eventBeforeDeleting().addListener(this);
		rdbMaster.eventAfterDeleting().addListener(this);
		rdbMaster.eventBeforeDeleted().addListener(this);
		rdbMaster.eventAfterDeleted().addListener(this);
		rdbMaster.eventBeforeInserting().addListener(this);
		rdbMaster.eventAfterInserting().addListener(this);
		rdbMaster.eventBeforeInserted().addListener(this);
		rdbMaster.eventAfterInserted().addListener(this);
		rdbMaster.eventBeforeRestore().addListener(this);
		rdbMaster.eventAfterRestore().addListener(this);
		rdbMaster.eventBeforeReload().addListener(this);
		rdbMaster.eventAfterReload().addListener(this);
		rdbMaster.addControl(this);
		
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		// insert some Data
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		rdbMaster.saveAllRows();
		
		// update some Data
		
		rdbMaster.setValue("NAME", "update()-test");
		rdbMaster.saveAllRows();
	
		//delete
		rdbMaster.delete();
		
		rds.saveAllDataBooks();
	}

	/**
	 * Tests mem filter with master detail.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testBug135() throws Exception
	{
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		RemoteDataBook dbDetail = new RemoteDataBook();
		dbDetail.setDataSource(rds);
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "ID" }));
		dbDetail.setMemFilter(true);
		dbDetail.open();
		
		//Insert Test data
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0L), "First"});
		
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0L), "First.First"});
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "First.Second"});
		
		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "Second"});
		
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2L), "Second.First"});
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3L), "Second.Second"});

		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2L), "Third"});
		
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(4L), "Third.First"});
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(5L), "Third.Second"});
		dbDetail.insert(false);
		dbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(6L), "Third.Third"});
		
		dbDetail.saveAllRows();
		dbDetail.saveAllRows();
		
		dbDetail.close();
		rdbMaster.close();
		
		rdbMaster.open();
		dbDetail.open();

		//start test
		rdbMaster.setSelectedRow(0);
		
		dbDetail.setFilter(new Like("NAME", "*.Third"));
		
		Assert.assertEquals(0, dbDetail.getRowCount());
		
		rdbMaster.setSelectedRow(2);
		
		Assert.assertEquals(1, dbDetail.getRowCount());
		
		dbDetail.setFilter(null);

		Assert.assertEquals(3, dbDetail.getRowCount());
	}
	
	/**
	 * Tests insert (DATASOURCE Level) of master and if the details have the correct ForeignKey (reference columns).
	 * It also tests now 
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testBug197Bug1639Bug1638Bug1640() throws Exception
	{	
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.setWritebackIsolationLevel(IDataBook.WriteBackIsolationLevel.DATASOURCE);
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
		bdDETAILtoTEST.setReferencedDataBook(rdbMaster);
		bdDETAILtoTEST.setReferencedColumnNames(new String [] { "ID" });
		bdDETAILtoTEST.setColumnNames(new String [] { "TEST_ID" });
		
		RemoteDataBook dbDetail = new RemoteDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(bdDETAILtoTEST);
		dbDetail.setDataSource(rds);
		dbDetail.setWritebackIsolationLevel(IDataBook.WriteBackIsolationLevel.DATASOURCE);
		dbDetail.open();
		
		// insert new row in master
		rdbMaster.setSelectedRow(0);				
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(DATASOURCE)-test");
		
		// insert new row in detail
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-(DATASOURCE)insert-test");
		
		dbDetail.saveSelectedRow();
		
		Assert.assertEquals(dbDetail.getValue("TEST_ID"), rdbMaster.getValue("ID"));  // Bug197

		dbDetail.fetchAll();

		Assert.assertEquals(1, dbDetail.getRowCount()); // Bug1639
		
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(DATASOURCE)-test2");
		
		// insert new row in detail
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-(DATASOURCE)insert-test2");
		dbDetail.insert(true);
		dbDetail.setValue("NAME", "detail-(DATASOURCE)insert-test3");

		rdbMaster.setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
		rdbMaster.setSelectedRow(0);
		rdbMaster.setSelectedRow(1);
		
		Assert.assertEquals(0, rdbMaster.getChangedRows().length); // Bug1640
	}
	
	/**
	 * Test isWritableColumnChanged() in DataBook/ChangeableDataRow.
	 * 
	 * @throws Exception if not all DataBook/ChangeableDataRow methods work correctly
	 */	
	@Test
	public void testWritableColumnChanged() throws Exception
	{
		// read all Data and display it
		rdbMaster.setName("TEST");

		// add some client Columns.
		RowDefinition rd = new RowDefinition();
		rd.addColumnDefinition(new ColumnDefinition("CLIENT"));
		rdbMaster.setRowDefinition(rd);

		rdbMaster.open();
		rdbMaster.fetchAll();
		
		// insert some Data
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		Assert.assertTrue(rdbMaster.isInserting());
		Assert.assertEquals(1, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.insert(true);
		rdbMaster.setValue("NAME", "insert(before)-test");
		rdbMaster.saveSelectedRow();
		
		rdbMaster.close();
		rdbMaster.open();
		rdbMaster.fetchAll();
		
		// update some client Data
		rdbMaster.setSelectedRow(1);
		rdbMaster.setValue("CLIENT", "client");	
		Assert.assertTrue(rdbMaster.isUpdating());
		Assert.assertFalse(rdbMaster.isWritableColumnChanged());
		Assert.assertEquals(1, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.saveSelectedRow();
		
		// update some server Data
		rdbMaster.setSelectedRow(1);
		String sUpdateTo = "update()-test";
		rdbMaster.setValue("NAME", sUpdateTo);	
		Assert.assertTrue(rdbMaster.isUpdating());
		Assert.assertTrue(rdbMaster.isWritableColumnChanged());
		Assert.assertEquals(1, getOnlyIUDChangedDataRows(rdbMaster).length);
		rdbMaster.saveSelectedRow();
		rdbMaster.reload();
		Assert.assertEquals(sUpdateTo, rdbMaster.getValue("NAME"));
	}

	/**
	 * A simple test case for <a href="http://forum.sibvisions.com/viewtopic.php?f=2&p=705">http://forum.sibvisions.com/viewtopic.php?f=2&p=705</a>.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testForumF2P705() throws Throwable
	{
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/testdb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");

		dba.open();
		
		DBStorage dbsTest = new DBStorage();
		dbsTest.setDBAccess(dba);
		dbsTest.setWritebackTable("TEST");
		dbsTest.open();

		DBStorage dbsDetail = new DBStorage();
		dbsDetail.setDBAccess(dba);
		dbsDetail.setWritebackTable("DETAIL");
		dbsDetail.open();
		
		DirectObjectConnection doc = new DirectObjectConnection();
		doc.put("test", dbsTest);
		doc.put("detail", dbsDetail);
		
		MasterConnection macon = new MasterConnection(doc);
		macon.open();
		
		RemoteDataSource rdsTest = new RemoteDataSource(macon);
		rdsTest.open();

		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setName("detail");
		rdbDetail.setDataSource(rdsTest);
		rdbDetail.open();

		rdbDetail.deleteAllRows();
		rdbDetail.saveAllRows();
		
		rdbMaster = new RemoteDataBook();
		rdbMaster.setName("test");
		rdbMaster.setDataSource(rdsTest);
		rdbMaster.open();

		rdbMaster.deleteAllRows();
		
		rdbMaster.insert(false);
		rdbMaster.setValue("NAME", "Karl Klamm");

		rdbMaster.insert(false);
		rdbMaster.setValue("NAME", "Max Mustermann");
		rdbMaster.saveAllRows();

		rdbMaster.setReadOnly(true);
		rdbMaster.setUpdateEnabled(false);
		
		IChangeableDataRow row = rdbMaster.getDataRow(0);
		
		row.setValue("NAME", "JVx changed");

		Assert.assertEquals(1, rdbMaster.getSelectedRow());
		
		rdbMaster.setSelectedRow(0);
		
		rdbMaster.setReadOnly(false);
		rdbMaster.setUpdateEnabled(true);
	
		Assert.assertEquals("Karl Klamm", rdbMaster.getDataRow(0).getValue("NAME"));
		
		rdbMaster.setValue("NAME", row.getValue("NAME"));

		Assert.assertEquals("JVx changed", rdbMaster.getDataRow(0).getValue("NAME"));

		rdbMaster.deleteAllRows();
		rdbMaster.saveAllRows();
	}	
	
	/**
	 * Test the correct selected row bug. Ticket 499.
	 * 
	 * @throws Exception if not all DataBook/ChangeableDataRow methods work correctly
	 */	
	@Test
	public void testCorrectSelectedRowBug499() throws Exception
	{
		rdbMaster.setName("TEST");
		rdbMaster.open();

		for (int i = 0; i < 20; i++)
		{
			rdbMaster.insert(false);
			rdbMaster.setValue("NAME", "Martin" + i);
		}
		rdbMaster.saveAllRows();
		
		rdbMaster.setSelectedRow(10);
		
		Object id = rdbMaster.getValue("ID");
		
		rdbMaster.reload();
		rdbMaster.reload();
		
		Assert.assertEquals(id, rdbMaster.getValue("ID"));
	}
	
	/**
	 * Test filtering with master/detail.
	 *  
	 * @throws Exception if test fails
	 */
	@Test
	public void testBug546() throws Exception
	{
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();

		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setName("DETAIL");
		rdbDetail.setSelectionMode(SelectionMode.FIRST_ROW);
		rdbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "ID" }));
		rdbDetail.setDataSource(rds);
		rdbDetail.setDeleteCascade(true);
		rdbDetail.open();
		
		rdbMaster.deleteAllRows();
		rdbMaster.fetchAll();
		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0), "First"});
		
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(0), "First - Detail 1", BigDecimal.valueOf(0)});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(1), "First - Detail 2", BigDecimal.valueOf(0)});
		rdbDetail.saveAllRows();

		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1), "Second"});

		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(2), "Second - Detail 1", BigDecimal.valueOf(1)});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(3), "Second - Detail 2", BigDecimal.valueOf(1)});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(4), "Second - Detail 3", BigDecimal.valueOf(1)});
		rdbDetail.saveAllRows();

		rdbMaster.saveAllRows();

		And filter = new And();
		filter.add(new LikeIgnoreCase("NAME", "*Detail 2*"));

		rdbDetail.setFilter(filter);
		
		Assert.assertEquals(1, rdbDetail.getRowCount());
		Assert.assertEquals("Second - Detail 2", rdbDetail.getValue("NAME"));

		rdbMaster.setSelectedRow(0);
		
		Assert.assertEquals(1, rdbDetail.getRowCount());
		Assert.assertEquals("First - Detail 2", rdbDetail.getValue("NAME"));
	}
	
	/**
	 * Tests for ticket #1190, that {@link RemoteDataBook#getEstimatedRowCount()}
	 * fails with an ArrayOutOfBoundsException.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void test1190GetEstimatedRowCountFails() throws ModelException
	{
		rdbMaster.setName("TEST");
		rdbMaster.open();
		
		rdbMaster.getEstimatedRowCount();
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
		// read all Data and display it
		rdbMaster.setName("TEST");
		rdbMaster.open();
		rdbMaster.fetchAll();

		ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
		bdDETAILtoTEST.setReferencedDataBook(rdbMaster);
		bdDETAILtoTEST.setReferencedColumnNames(new String [] { "ID" });
		bdDETAILtoTEST.setColumnNames(new String [] { "TEST_ID" });
		
		RemoteDataBook dbDetail = new RemoteDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(bdDETAILtoTEST);
		dbDetail.setDataSource(rds);
		dbDetail.setDeleteCascade(true);
		dbDetail.open();

		rdbMaster.deleteAllRows();
		rdbMaster.fetchAll();
		
		for (int i = 0; i < 100; i++)
		{
			rdbMaster.insert(false);
			rdbMaster.setValues(IRowDefinition.ALL_COLUMNS, new Object[] {BigDecimal.valueOf(i), "Name " + i});
			
			for (int j = 0; j < 5; j++)
			{
				dbDetail.insert(false);
				dbDetail.setValues(IRowDefinition.ALL_COLUMNS, new Object[] {BigDecimal.valueOf(i * 1000 + j), "Detail Name " + i + "  " + j, BigDecimal.valueOf(i)});
			}
		}
		
		rdbMaster.saveAllRows();
		dbDetail.saveAllRows();
		rdbMaster.setSelectedRow(0);
		
		rdbMaster.eventAfterRowSelected().addListener(this);
		rdbMaster.setFilter(new Equals("id", BigDecimal.valueOf(0)));
		rdbMaster.setFilter(new Equals("id", BigDecimal.valueOf(1)));
		rdbMaster.setFilter(new Equals("id", BigDecimal.valueOf(2)));
		rdbMaster.setFilter(null);
		rdbMaster.getSelectedRow();
		
		Assert.assertEquals(1, getEvents().size());
	}

	/**
	 * reload after set filter does not work in mem filter mode.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testBug1315ReloadAfterSetFilterInMemFilterMode() throws Exception
	{
		rdbMaster.setName("TEST");
		rdbMaster.setMemFilter(true);
		rdbMaster.open();
		rdbMaster.fetchAll();

		IDataPage page = rdbMaster.getDataPage();
		rdbMaster.reload();
		
		Assert.assertNotEquals(page, rdbMaster.getDataPage());
		
		page = rdbMaster.getDataPage();
		rdbMaster.setFilter(null);
		rdbMaster.reload();
		
		Assert.assertNotEquals(page, rdbMaster.getDataPage());
	}
	
	/**
	 * reload after set filter does not work in mem filter mode.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testTicket1424ReloadHasDummyImplementation() throws Exception
	{
		rdbMaster.setName("TEST");		
		rdbMaster.open();
		rdbMaster.fetchAll();

		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setDataSource(rds);
		rdbDetail.setName("DETAIL");
		rdbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "ID" }));
		rdbDetail.setDeleteCascade(true);
		rdbDetail.open();

		rdbMaster.deleteAllRows();

		//Insert Test data
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0L), "First"});
		
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0L), "First.First"});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "First.Second"});
		
		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "Second"});
		
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2L), "Second.First"});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(3L), "Second.Second"});

		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2L), "Third"});
		
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(4L), "Third.First"});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(5L), "Third.Second"});
		rdbDetail.insert(false);
		rdbDetail.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(6L), "Third.Third"});
		
		rdbMaster.saveAllRows();
		rdbDetail.saveAllRows();
		
		IDataPage oldTestDataPage = rdbMaster.getDataPage();
		int oldTestRow = rdbMaster.getSelectedRow();
		IDataPage oldDetailDataPage = rdbMaster.getDataPage();
		int oldDetailRow = rdbMaster.getSelectedRow();
		
		rdbMaster.reload();
		rdbDetail.reload();

		IDataPage currentTestDataPage = rdbMaster.getDataPage();
		int currentTestRow = rdbMaster.getSelectedRow();
		IDataPage currentDetailDataPage = rdbMaster.getDataPage();
		int currentDetailRow = rdbMaster.getSelectedRow();

		Assert.assertFalse(oldTestDataPage == currentTestDataPage);
		Assert.assertEquals(oldTestRow, currentTestRow);
		Assert.assertFalse(oldDetailDataPage == currentDetailDataPage);
		Assert.assertEquals(oldDetailRow, currentDetailRow);

		oldTestDataPage = currentTestDataPage;
		oldTestRow = currentTestRow;
		oldDetailDataPage = currentDetailDataPage;
		oldDetailRow = currentDetailRow;

		rdbMaster.reloadDataPage(SelectionMode.CURRENT_ROW);
		rdbDetail.reloadDataPage(SelectionMode.CURRENT_ROW);

		currentTestDataPage = rdbMaster.getDataPage();
		currentTestRow = rdbMaster.getSelectedRow();
		currentDetailDataPage = rdbMaster.getDataPage();
		currentDetailRow = rdbMaster.getSelectedRow();

		Assert.assertFalse(oldTestDataPage == currentTestDataPage);
		Assert.assertEquals(oldTestRow, currentTestRow);
		Assert.assertFalse(oldDetailDataPage == currentDetailDataPage);
		Assert.assertEquals(oldDetailRow, currentDetailRow);
	}
	
	/**
	 * reload after set filter does not work in mem filter mode.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testTicket1402ReloadDuringSync() throws Exception
	{
		rdbMaster.setName("TEST");		
		rdbMaster.open();
		rdbMaster.fetchAll();
		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setDataSource(rds);
		rdbDetail.setName("DETAIL");
		rdbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "ID" }));
		rdbDetail.setDeleteCascade(true);
		rdbDetail.open();

		rdbMaster.deleteAllRows();

		//Insert Test data
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(0L), "First"});
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "Second"});
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(2L), "Third"});
		rdbMaster.saveAllRows();

		rdbMaster.eventAfterRowSelected().addListener(new IDataBookListener() 
		{
			public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable 
			{
				rdbMaster.setSort(new SortDefinition(false, "NAME"));
			}
		});
		
		rdbMaster.eventAfterRowSelected().addListener(new IDataBookListener() 
		{
			public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable 
			{
				rdbMaster.setFilter(new LessEquals("NAME", "Second"));
			}
		});
		
		rdbMaster.eventAfterRowSelected().addListener(new IDataBookListener() 
		{
			public void dataBookChanged(DataBookEvent pDataBookEvent) throws Throwable 
			{
				rdbMaster.reload();
			}
		});
		
		rdbMaster.reload();
		rdbMaster.setSelectedRow(1);
	}
	
	/**
	 * Tests the fetch of a detail databook when master is inserted.
	 * 
	 * @throws Exception
	 *             if the master/detail support work incorrectly
	 */	
	@Test
	public void testDetailFetchOnMasterInsertTicket1560() throws Exception
	{
		RemoteDataBook rdbTest = new RemoteDataBook();
		rdbTest.setDataSource(rds);
		rdbTest.setName("TEST");		
		rdbTest.open();
		rdbTest.insert(false);
		rdbTest.setValues(new String[] {"ID", "NAME"}, new Object[] {BigDecimal.valueOf(1L), "First"});
		rdbTest.saveAllRows();
		
		rdbMaster.setName("DETAIL");
		rdbMaster.open();
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(1L), "First.First", BigDecimal.valueOf(1L)});
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(2L), "First.Second", BigDecimal.valueOf(1L)});
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(3L), "First.Third", BigDecimal.valueOf(1L)});
		rdbMaster.saveAllRows();
		
		RemoteDataBook rdbDetail = new RemoteDataBook();
		rdbDetail.setDataSource(rds);
		rdbDetail.setName("DETAIL");
		rdbDetail.setMasterReference(new ReferenceDefinition(new String [] { "TEST_ID" }, rdbMaster, new String [] { "TEST_ID" }));
		rdbDetail.setMemSort(true);
		rdbDetail.setSort(new SortDefinition(false, "ID"));
		rdbDetail.open();
		
		rdbMaster.insert(false);
		rdbMaster.setValues(new String[] {"ID", "NAME", "TEST_ID"}, new Object[] {BigDecimal.valueOf(4L), "First.Fourth", BigDecimal.valueOf(1L)});
		
		rdbDetail.reload();
		rdbDetail.fetchAll();
		
		rdbMaster.saveSelectedRow();
		rdbDetail.fetchAll();

		Assert.assertEquals(BigDecimal.valueOf(4L), rdbDetail.getDataRow(0).getValue("ID"));
	}

	/**
	 * Tests how the databook handles not sortable columns in a sort definition.
	 * 
	 * @throws Exception if the test fails.
	 */
	@Test
	public void testSettingNonSortableColumnsForSortTicket1092() throws Exception
	{
		RemoteDataBook rdbTest = new RemoteDataBook();
		rdbTest.setDataSource(rds);
		rdbTest.setName("TEST");
		rdbTest.getRowDefinition().addColumnDefinition(new ColumnDefinition("CLIENT_SIDE"));
		rdbTest.setSort(new SortDefinition("CLIENT_SIDE", "ID"));
		
		// Has the Sort been correctly applied?
		Assert.assertArrayEquals(new String[] { "CLIENT_SIDE", "ID" }, rdbTest.getSort().getColumns());
		
		// Now open the databook.
		rdbTest.open();
		
		// Has the sort been corrected?
		Assert.assertArrayEquals(new String[] { "ID" }, rdbTest.getSort().getColumns());
		
		// Force the databook to fetch.
		rdbTest.fetchAll();
		
		// Set the sort again to make sure that this branch also works.
		rdbTest.setSort(new SortDefinition("CLIENT_SIDE", "ID"));
		Assert.assertArrayEquals(new String[] { "ID" }, rdbTest.getSort().getColumns());
		
		// Switch to memory sort should show all columns.
		rdbTest.setMemSort(true);
		Assert.assertArrayEquals(new String[] { "CLIENT_SIDE", "ID" }, rdbTest.getSort().getColumns());
		
		// Setting the sort while memory sort is active should also work as expected.
		rdbTest.setSort(new SortDefinition("CLIENT_SIDE", "ID"));
		rdbTest.setMemSort(false);
		Assert.assertArrayEquals(new String[] { "ID" }, rdbTest.getSort().getColumns());
		
		// Setting null should always work.
		rdbTest.setSort(null);
		Assert.assertNull(rdbTest.getSort());
		rdbTest.setMemSort(true);
		Assert.assertNull(rdbTest.getSort());
		rdbTest.setSort(null);
		Assert.assertNull(rdbTest.getSort());
		rdbTest.setMemSort(false);
		
		// Let's try to set only not sortable columns, shall we?
		rdbTest.setSort(new SortDefinition("CLIENT_SIDE"));
		
		// Force a fetch, again.
		rdbTest.fetchAll();
		
		// Is the sort okay?
		Assert.assertNull(rdbTest.getSort());
		rdbTest.setMemSort(true);
		Assert.assertArrayEquals(new String[] { "CLIENT_SIDE" }, rdbTest.getSort().getColumns());
		rdbTest.setMemSort(false);
	}

} 	// TestDataBook
