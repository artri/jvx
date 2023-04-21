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
 * 01.10.2008 - [JR] - creation
 */
package demo;

import java.io.File;
import java.math.BigDecimal;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.StringDataType;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.persist.MemDataBookStorage;
import com.sibvisions.rad.server.GenericBean;
import com.sibvisions.util.type.ResourceUtil;

import remote.RemoteFile;

/**
 * Application object for unit tests.
 * 
 * @author René Jahn
 */
public class Application extends GenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** method call queue. */
	private StringBuilder sbCallQueue = new StringBuilder();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
	/**
	 * Creates a new instance of <code>Application</code>.
	 */
	public Application()
	{
	    InstanceChecker.add(Application.class);
	    
		addCall("Application()");
		
		System.out.println("CALL: Application.<init>");
	} 
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a call to the queue.
	 * 
	 * @param pCall the call statement
	 */
	protected void addCall(String pCall)
	{
		if (sbCallQueue.length() > 0)
		{
			sbCallQueue.append("\n");
		}
		
		sbCallQueue.append(pCall);
	}
	
	/**
	 * Returns the current call queue.
	 * 
	 * @return the call queue
	 */
	public String getCallQueue()
	{
		return sbCallQueue.toString();
	}

	/**
	 * Gets the remote application file.
	 *
	 * @return the remote application file
	 */
	public RemoteFile getApplication()
	{
		RemoteFile rfile = (RemoteFile)get("application");
		
		if (rfile == null)
		{
			rfile = new RemoteFile(new File("application.xml"));
			
			put("application", rfile);
		}
		
		addCall("getApplication()");
		
		return rfile;
	}
	
	/**
	 * Gets the binary storage.
	 * 
	 * @return the binary storage
	 * @throws Exception if access to binary storage fails
	 */
	public MemDataBookStorage getBinaryData() throws Exception
	{
		MemDataBookStorage dbs = (MemDataBookStorage)get("binaryData");
		
		if (dbs == null)
		{
			MemDataBook mdb = new MemDataBook();
			mdb.setName("bindat");
			mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("FILENAME", new StringDataType()));
			mdb.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTENT", new BinaryDataType()));
			mdb.getRowDefinition().setPrimaryKeyColumnNames(new String[] {"ID"});
			mdb.open();
			
			dbs = new MemDataBookStorage(mdb);
			dbs.open();
			
			put("binaryData", dbs);
			
			resetBinaryData();
		}
		
		return dbs;
	}
	
	/**
	 * Resets binary data.
	 * 
	 * @throws Exception if reset fails
	 */
	public void resetBinaryData() throws Exception
	{
		MemDataBookStorage mds = (MemDataBookStorage)get("binaryData");
		
		if (mds != null)
		{
			IDataBook book = mds.getDataBook();
			
			book.deleteAllRows();
			
			book.insert(true);
			book.setValues(new String[] {"ID", "FILENAME", "CONTENT"}, 
					       new Object[] {BigDecimal.valueOf(0), "dummy.png", 
					    		         ResourceUtil.getResourceAsStream("/com/sibvisions/rad/application/images/login.png")});
			book.saveAllRows();
		}
	}
	
}	// Application
