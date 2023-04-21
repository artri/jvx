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

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.ObjectDataType;

import org.junit.Test;

import com.sibvisions.rad.model.EventProtocol;

/**
 * Tests performance of Functions of com.sibvisions.rad.model.MemDataBook and
 * com.sibvisions.rad.model.MemDataPage. <br>
 * 
 * @author Roland Hörmann
 */
public class TestMemDataBookPerformance extends EventProtocol
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests performance of searchNext.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test 
	public void testSearchNextPerformance() throws ModelException
	{
		RowDefinition rowdefDetail = new RowDefinition();
		rowdefDetail.addColumnDefinition(new ColumnDefinition("ID"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("NAME"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("TYPE"));
		rowdefDetail.addColumnDefinition(new ColumnDefinition("DATA"));
		
		MemDataBook detail = new MemDataBook(rowdefDetail);
		detail.setName("detail");
		detail.open();
		
		for (int i = 0; i < 200; i++)
		{
			detail.insert(false);
			detail.setValue("ID", "" + i);
			detail.setValue("NAME", "name" + i);
			detail.setValue("TYPE", "type" + i);
			detail.setValue("DATA", "data" + i);
		}
		detail.saveSelectedRow();
		
		IDataRow search = detail.createEmptyDataRow(new String[] {"NAME", "TYPE"});
		
		search.setValue("NAME", new String("name"));
		search.setValue("TYPE", new String("type"));
		
		ICondition cond = new Equals("NAME", "name").and(new Equals("TYPE", "type")); 
			//new Equals(search, "NAME").and(new Equals(search, "TYPE"));
		
		long time = System.currentTimeMillis();
		
		for (int i = 0; i < 100000; i++)
		{
			detail.searchPrevious(cond);
		}
		
		System.out.println("Dauer: " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * Tests the general insert performance of the {@link MemDataBook}.
	 * 
	 * @throws ModelException if using the {@link MemDataBook} fails.
	 */
	@Test
	public void testInsertPerformance() throws ModelException
	{
		IDataBook dataBook = new MemDataBook();
		for (int columnCounter = 0; columnCounter < 26; columnCounter++)
		{
			dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(Integer.toString(columnCounter), new ObjectDataType()));
		}
		dataBook.setName("PERFORMANCE");
		dataBook.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		
//		System.out.print("Waiting for you...");
//		try
//		{
//			Thread.sleep(15000);
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//		System.out.println("Done");
		
		dataBook.open();
//		dataBook.setFilter(new Not(new Equals("0", null)));
		
		Object value = new Object();
		Object[] values = new Object[] {
				value, value, value, value, value, value, value, value, value, value, 
				value, value, value, value, value, value, value, value, value, value, 
				value, value, value, value, value, value };
		
		long start = System.nanoTime();
		int rowCount = 2000000;
		
		for (int counter = 0; counter < rowCount; counter++)
		{
			dataBook.insert(false);
			
			dataBook.setValues(null, values);
//			dataBook.setValue("0", value);
//			dataBook.setValue("1", value);
//			dataBook.setValue("2", value);
//			dataBook.setValue("3", value);
//			dataBook.setValue("4", value);
//			dataBook.setValue("5", value);
//			dataBook.setValue("6", value);
//			dataBook.setValue("7", value);
//			dataBook.setValue("8", value);
//			dataBook.setValue("9", value);
//			dataBook.setValue("10", value);
//			dataBook.setValue("11", value);
//			dataBook.setValue("12", value);
//			dataBook.setValue("13", value);
//			dataBook.setValue("14", value);
//			dataBook.setValue("15", value);
//			dataBook.setValue("16", value);
//			dataBook.setValue("17", value);
//			dataBook.setValue("18", value);
//			dataBook.setValue("19", value);
//			dataBook.setValue("20", value);
//			dataBook.setValue("21", value);
//			dataBook.setValue("22", value);
//			dataBook.setValue("23", value);
//			dataBook.setValue("24", value);
//			dataBook.setValue("25", value);
			
//			for (int columnCounter = 0; columnCounter < 26; columnCounter++)
//			{
//				dataBook.setValue(Integer.toString(columnCounter), value);
//			}
			
//			dataBook.saveSelectedRow();
		}
		
		long duration = (System.nanoTime() - start);
		
		System.out.println("Duration: " + ((double)duration / 1000000000d) + " s");
		System.out.println("Average: " + ((double)duration / (double)rowCount / 1000d) + " us");
		dataBook.close();
	}
	
	/**
	 * Tests performance of searchNext.
	 * 
	 * @param pArgs the arguments
	 * @throws ModelException 
	 */
	public static void main(String[] pArgs) throws ModelException
	{
		System.out.print("Waiting for you...");
		try
		{
			Thread.sleep(15000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Done");

		
		TestMemDataBookPerformance test = new TestMemDataBookPerformance();
		
		for (int i = 0; i < 10; i++)
		{
			test.testInsertPerformance();
		}
	}
	
} 	// TestMemDataBookPerformance
