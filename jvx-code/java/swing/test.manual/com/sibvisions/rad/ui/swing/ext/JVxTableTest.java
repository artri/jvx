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
 * 05.01.2012 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.rad.model.ColumnDefinition;
import javax.swing.JFrame;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * A simple {@link JVxTable} test.
 * 
 * @author René Jahn
 */
public class JVxTableTest extends JFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Start method.
	 * 
	 * @param pArgs Application arguments
	 * @throws Throwable if start fails
	 */
	public static void main(String[] pArgs) throws Throwable
	{
		new JVxTableTest();
	}
	
	/**
	 * Creates a new instance of <code>JVxTableTest</code>.
	 * 
	 * @throws Throwable if creation fails
	 */
	JVxTableTest() throws Throwable
	{
//		//--------------------------------
//		// Database handling
//		//--------------------------------
//
//		//DB Connection
//		DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:hsql://localhost:33000/showcasedb");
//		dba.setUsername("sa");
//		dba.setPassword("");
//		dba.open();
//		
//		//Table access
//		DBStorage dsFiles = new DBStorage();
//		dsFiles.setDBAccess(dba);
//		dsFiles.setWritebackTable("FILES");
//		dsFiles.setAutoLinkReference(false);
//		dsFiles.open();
//		
//		//--------------------------------
//		// Communication
//		//--------------------------------
//
//		//Connection handling
//		DirectObjectConnection con = new DirectObjectConnection();
//		con.put("files", dsFiles);
//
//		MasterConnection macon = new MasterConnection(con);
//		macon.setApplicationName("showcase");
//		macon.setUserName("admin");
//		macon.setPassword("admin");
//		macon.open();
//		
//		//Connect to the "remote" table
//		RemoteDataSource rds = new RemoteDataSource(macon);
//		rds.open();
//
//		RemoteDataBook rdbData = new RemoteDataBook();
//		rdbData.setDataSource(rds);
//		rdbData.setName("files");
//		rdbData.open();

		MemDataBook mdbData = new MemDataBook();
		mdbData.setName("person");
		mdbData.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRSTNAME"));
		mdbData.getRowDefinition().addColumnDefinition(new ColumnDefinition("LASTNAME"));
		mdbData.getRowDefinition().addColumnDefinition(new ColumnDefinition("PHONE"));
		mdbData.open();
		
		mdbData.insert(true);
		mdbData.setValues(new String[] {"FIRSTNAME", "LASTNAME", "PHONE"}, new String[] {"JVx", "rocks", "+43 000 / 1234 567"});

		mdbData.saveAllRows();
		
		//--------------------------------
		// Swing
		//--------------------------------
		
		JVxTable table = new JVxTable();
		table.setDataBook(mdbData);

		setLayout(new BorderLayout());
		
		add(table, BorderLayout.CENTER);
		
		setPreferredSize(new Dimension(500, 400));
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}	// JVxTableTest
