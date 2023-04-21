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
 * 02.11.2008 - [RH] - changed to RemoteDataBook
 */
package javax.rad.model.condition;

import java.math.BigDecimal;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SubConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;

import remote.net.VMConnection;

/**
 * Tests all functions of javax.rad.model.condition.*, and the filter functions of
 * com.sibvisions.rad.model.mem.DataBook.<br>
 * 
 * @author Roland Hörmann
 * @see javax.rad.model.condition.*
 * @see com.sibvisions.rad.model.mem.DataBook
 */
public class TestConditions
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The DataBook test instance. */
	private RemoteDataBook	dbDataBook	= new RemoteDataBook();

	/** The RemoteDataSource test instance. */
	private RemoteDataSource	dba		= new RemoteDataSource();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Connect to the Test Database and prepare the test data.
	 * 
	 * @throws Throwable
	 *             if the connect to the DB fails
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
		
		dba.setConnection(sub);
		dba.open();

		dbDataBook.setDataSource(dba);

		// delete all
		dbDataBook.setName("TEST");
		dbDataBook.open();
		
		ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
		bdDETAILtoTEST.setReferencedDataBook(dbDataBook);
		bdDETAILtoTEST.setReferencedColumnNames(new String [] { "ID" });
		bdDETAILtoTEST.setColumnNames(new String [] { "TEST_ID" });
		
		RemoteDataBook dbDetail = new RemoteDataBook();
		dbDetail.setName("DETAIL");
		dbDetail.setMasterReference(bdDETAILtoTEST);
		dbDetail.setDataSource(dba);
		dbDetail.setDeleteCascade(true);
		dbDetail.open();
		
		dbDataBook.fetchAll();
		dbDetail.fetchAll();
//		System.out.println(dbDataBook);

		dbDataBook.deleteAllRows();
		dba.saveAllDataBooks();

//		System.out.println(dbDataBook);

		// insert()
		dbDataBook.insert(true);
		dbDataBook.setValue("NAME", "insert()");
		dbDataBook.insert(true);
		dbDataBook.setValue("NAME", "insert2()");
		dbDataBook.insert(true);
		dbDataBook.setValue("NAME", "zinsert2()");

		dba.saveAllDataBooks();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the base function of the Condition, Filter and filter function of
	 * the DataBook.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void testFilter() throws Exception
	{
		System.out.println(dbDataBook);
		dbDataBook.close();
		
		ICondition filter = new Like("NAME", "i%");		
		filter = filter.and(new Less("ID", Integer.valueOf(2)));
		filter = filter.or(new And(new GreaterEquals("ID", "2"), new LessEquals("ID", "1000")));
		filter = filter.or(new Equals("ID", null, false));
		
		// select data from test table with filter
		dbDataBook.setFilter(filter);
		dbDataBook.open();
		dbDataBook.fetchAll();

		System.out.println(dbDataBook);

		Assert.assertTrue(dbDataBook.getValue("NAME").equals("insert()"));
		Assert.assertFalse(dbDataBook.getRowCount() != 3);
		System.out.println(filter);		
	}
	
	/**
	 * Test the base function of the Condition, Filter and filter function of
	 * the DataBook.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void likeReverseIgnoreCase() throws Exception
	{
		DataRow row = new DataRow();
		row.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		row.setValue("NAME", "*name*");
		
		LikeReverseIgnoreCase like = new LikeReverseIgnoreCase("NAME", "Mein Name ist Martin");
		
		Assert.assertTrue(like.isFulfilled(row));
	}

	/**
	 * Test the ContainsIgnoreCase condition.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void containsIgnoreCase() throws Exception
	{
		DataRow row = new DataRow();
		row.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		row.setValue("NAME", "Mein Name ist Martin");
		
		ContainsIgnoreCase contains = new ContainsIgnoreCase("NAME", "name");
		
		Assert.assertTrue(contains.isFulfilled(row));
		
		contains.setValue("named");

		Assert.assertFalse(contains.isFulfilled(row));
		
		contains.setValue("name");

		Assert.assertTrue(contains.isFulfilled(row));

		contains.setValue(null);

		Assert.assertFalse(contains.isFulfilled(row));

		contains.setIgnoreNull(true);

		Assert.assertTrue(contains.isFulfilled(row));
	}

	/**
	 * Test the ContainsIgnoreCase condition.
	 * 
	 * @throws Exception
	 *             if not all methods work correctly
	 */
	@Test
	public void startsWithIgnoreCase() throws Exception
	{
		DataRow row = new DataRow();
		row.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		row.setValue("NAME", "Mein Name ist Martin");
		
		StartsWithIgnoreCase startsWith = new StartsWithIgnoreCase("NAME", "name");
		
		Assert.assertFalse(startsWith.isFulfilled(row));
		
		startsWith.setValue("mein");

		Assert.assertTrue(startsWith.isFulfilled(row));

		startsWith.setValue(null);

		Assert.assertFalse(startsWith.isFulfilled(row));

		startsWith.setIgnoreNull(true);

		Assert.assertTrue(startsWith.isFulfilled(row));
	}

	/**
	 * Tests, if null conditions causes a NPE, and if it is ignored.
	 * 
	 * @throws Exception if it fails
	 */
    @Test
    public void testAnd() throws Exception
    {
        DataRow row = new DataRow();
        row.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        
        ICondition cond = new Equals("ID", null);
        
        cond = cond.and(null);
        cond = cond.and(new Not(null));
        
        Assert.assertEquals(cond.isFulfilled(row), cond.clone().isFulfilled(row));
    }

    /**
     * Tests, if null conditions causes a NPE, and if it is ignored.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void test1Equals2() throws Exception
    {
        DataRow row = new DataRow();
        row.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID"));
        
        Assert.assertEquals(false, new Equals("1", BigDecimal.valueOf(2)).isFulfilled(row));
    }

    /**
     * Tests, if empty condition clones to null.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testToCloneToNull() throws Exception
    {
        ICondition cond = new Equals("A", null, true).and(new Equals("B", null, true));
        
        Assert.assertNull(cond.clone());
    }

    
    
} 	// TestConditions
