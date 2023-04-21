/*
 * Copyright 2012 SIB Visions GmbH
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
 * 08.03.2012 - [JR] - creation
 */
package com.sibvisions.rad.model;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Tests the functionality of {@link DataBookCSVExporter}.
 * 
 * @author René Jahn
 */
public class TestDataBookCSVExporter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default string encoding. */
	private static final String	ENCODING = "ISO-8859-15";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link DataBookCSVExporter#writeCSV(javax.rad.model.IDataBook, java.io.OutputStream, String[], String[], 
	 * javax.rad.model.condition.ICondition, javax.rad.model.SortDefinition)}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testWriteCSV() throws Exception
	{
	    DataBookCSVExporter.setDefaultEncoding(ENCODING);
	    
		MemDataBook mdb = new MemDataBook();
		mdb.setName("CSV");
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("REFERENCE"));
		mdb.open();
		
		mdb.insert(false);
		mdb.setValues(new String[] {"ID", "NAME", "REFERENCE"}, new Object[] {BigDecimal.valueOf(0), "JVx", "\"SIB Visions\""});
		mdb.insert(false);
		mdb.setValues(new String[] {"ID", "NAME", "REFERENCE"}, new Object[] {BigDecimal.valueOf(1), "DataBook", "1/4"});
		mdb.saveAllRows();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Locale locDefault = LocaleUtil.getDefault();
		
		try
		{
			LocaleUtil.setDefault(new Locale("de", "AT"));
			
			DataBookCSVExporter.writeCSV(mdb, baos, mdb.getRowDefinition().getColumnNames(), null, null, null);
			
			baos.close();
	
			Assert.assertEquals("\"Id\";\"Name\";\"Reference\"\n0;\"JVx\";\"\"\"SIB Visions\"\"\"\n1;\"DataBook\";\"1/4\"\n", new String(baos.toByteArray(), ENCODING));
			
			
			LocaleUtil.setDefault(new Locale("en", "US"));
	
			baos = new ByteArrayOutputStream();
			
			DataBookCSVExporter.writeCSV(mdb, baos, mdb.getRowDefinition().getColumnNames(), null, null, null);
			
			baos.close();
	
			Assert.assertEquals("\"Id\",\"Name\",\"Reference\"\n0,\"JVx\",\"\"\"SIB Visions\"\"\"\n1,\"DataBook\",\"1/4\"\n", new String(baos.toByteArray(), ENCODING));
		}
		finally
		{
			LocaleUtil.setDefault(locDefault);
		}
	}
	
	/**
	 * Tests CSV creation with null timestamp.
	 * see http://support.sibvisions.com/index.php?do=details&task_id=567
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testWriteCSVBug567() throws Exception
	{
	    DataBookCSVExporter.setDefaultEncoding(ENCODING);	    
	    
		MemDataBook mdb = new MemDataBook();
		mdb.setName("CSV");
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
		mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("DAY", new TimestampDataType()));
		mdb.open();
		
		Timestamp tsDay = DateUtil.getTimestamp(2012, 3, 25, 21, 4, 0);
		
		mdb.insert(false);
		mdb.setValues(new String[] {"ID", "NAME", "DAY"}, new Object[] {BigDecimal.valueOf(0), "JVx", null});
		mdb.insert(false);
		mdb.setValues(new String[] {"ID", "NAME", "DAY"}, new Object[] {BigDecimal.valueOf(1), "DataBook", tsDay});
		mdb.saveAllRows();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Locale locDefault = LocaleUtil.getDefault();

		DateUtil du = new DateUtil();
		
		try
		{
			LocaleUtil.setDefault(new Locale("en", "US"));
			
			DataBookCSVExporter.writeCSV(mdb, baos, mdb.getRowDefinition().getColumnNames(), null, null, null);
			
			baos.close();
	
			Assert.assertEquals("\"Id\",\"Name\",\"Day\"\n0,\"JVx\",\n1,\"DataBook\",\"" + du.format(tsDay) + "\"\n", new String(baos.toByteArray(), ENCODING));
		}
		finally
		{
			LocaleUtil.setDefault(locDefault);
		}
		
		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost/demodb");
		
		// set connect properties		
		dba.setUsername("sa");
		dba.setPassword("");

		dba.open();
		
		DBStorage dbsTest = new DBStorage();
		dbsTest.setDBAccess(dba);
		dbsTest.setWritebackTable("USERS");
		dbsTest.open();

		try
		{
			LocaleUtil.setDefault(new Locale("en", "US"));

			IFileHandle fh = dbsTest.createCSV(null, null, null, null);

			
			List<IBean> liRows = dbsTest.fetchBean(null, null, 0, -1);
			
			for (IBean bean : liRows)
			{
				System.out.println(StringUtil.toString(bean));
			}
			
			String sCSV = new String(FileUtil.getContent(fh.getInputStream()), ENCODING);
			
			Assert.assertEquals("\"Id\",\"Username\",\"Password\",\"Change Password\",\"Valid From\",\"Valid To\",\"Active\",\"First Name\"," +
					                       "\"Last Name\",\"Email\",\"Phone\"\n" +
					            "1,\"rene\",\"rene\",\"N\",,,\"Y\",\"René\",\"Jahn\",,\n" +
					            "2,\"martin\",\"martin\",\"N\",,,\"Y\",\"Martin\",\"Handsteiner\",,\n" +
					            "3,\"roland\",\"roland\",\"N\",,,\"Y\",\"Roland\",\"Hörmann\",,\n" +
					            "4,\"change_pwd\",\"change_pwd\",\"Y\",,,\"Y\",\"Change\",\"Password\",,\n" +
					            "5,\"valid_from\",\"valid_from\",\"N\",\"" + du.format(DateUtil.getTimestamp(9999, 11, 1, 12, 0, 0)) + "\",,\"Y\",\"Valid\",\"From\",,\n" +
					            "6,\"valid_from_to\",\"valid_from_to\",\"N\",\"" + du.format(DateUtil.getTimestamp(2008, 11, 1, 12, 0, 0)) + 
					                                              "\",\"" + du.format(DateUtil.getTimestamp(2008, 11, 1, 13, 0, 0)) + "\",\"Y\",\"Valid\",\"From_To\",,\n" +
					            "7,\"inactive\",\"inactive\",\"N\",,,\"N\",\"Inactive\",\"User\",,\n" +
					            "8,\"no_access\",\"no_access\",\"N\",,,\"Y\",\"NO\",\"Access\",,\n", sCSV);
		}
		finally
		{
			LocaleUtil.setDefault(locDefault);
		}
	}	
	
}	// TestDataBookUtil
