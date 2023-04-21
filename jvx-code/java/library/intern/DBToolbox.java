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
 * 23.06.2009 - [JR] - transformStatement implemented
 * 12.11.2011 - [JR] - used DBImporter
 * 16.10.2013 - [JR] - file encoding parameter
 */
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sibvisions.util.db.DBImporter;

/**
 * The <code>DBToolbox</code> is an internal class to do crazy things with
 * hypersonic databases.<br>
 * Feel free to be crazy ;-)
 * 
 * @author René Jahn
 */
public class DBToolbox
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Constant value for creating a new database. */
	public static final String NEW = "NEW";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts the <code>DBToolbox</code> as application.
	 * 
	 * @param sArgs commandline arguments
	 * @throws Throwable if the execution of the application fails
	 */
	public static void main(String[] sArgs) throws Throwable
	{
		new DBToolbox(sArgs);
		
		System.exit(0);
	}

	/**
	 * Creates a new instance of <code>DBToolbox</code> with special arguments.
	 * 
	 * @param sArgs execution arguments
	 * @throws Throwable if the execution of the <code>DBToolbox</code> fails
	 */
	protected DBToolbox(String[] sArgs) throws Throwable
	{
		if (sArgs.length <= 1)
		{
			printUsage();
		}
		else
		{		
			if (DBToolbox.NEW.equals(sArgs[0]))
			{
				if (sArgs.length == 3)
				{
					doNew(sArgs[1], sArgs[2], null);
				}
				else if (sArgs.length == 4)
				{
					doNew(sArgs[1], sArgs[2], sArgs[3]);
				}
				else
				{
					printUsage();
				}
			}
			else
			{
				printUsage();
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Print out the usage of the application. It contains all possible parameters.
	 */
	private void printUsage()
	{
		System.out.println("Usage: java DBToolbox <options>");
		System.out.println();
		System.out.println("Available options:");
		System.out.println();
		System.out.println(DBToolbox.NEW + " <script> <output>");
		System.out.println("(Executes a script for creating a new database)");
		System.out.println();		
		System.out.println("    script   - path to the script file");
		System.out.println("    output   - path and databasename e.g. \tmp\root");
		System.out.println("    encoding - file encoding (=charset name) e.g. ISO-8859-15");
		System.out.println();
		System.out.println();
	}

	/**
	 * Executes a script to create a new database.
	 * 
	 * @param sScript path to the script with statements
	 * @param sOutput Path of the new database
	 * @param pEncoding the file-encoding
	 * @throws Throwable if the new database can not be created
	 */
	private void doNew(String sScript, final String sOutput, String pEncoding) throws Throwable
	{
		Connection con = null;
		
		File[] fiDB;
		

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("INVOKE doNew: " + sScript + "(" + new File(sScript).getAbsolutePath() + "), " + sOutput);
		System.out.println("-----------------------------------------------------------------------------------");

		fiDB = new File(sOutput).getParentFile().listFiles
		(
			new FileFilter()
			{
				public boolean accept(File pFile)
				{
					return pFile.getName().startsWith(new File(sOutput).getName() + ".");
				}
			}
		);
		
		if (fiDB != null && fiDB.length > 0)
		{
			System.out.println("Database exists -> try to delete");
		
			for (int i = 0, anz = fiDB.length; i < anz; i++)
			{
				if (!fiDB[i].delete())
				{
					throw new Exception("TYPE_ERROR: Can't delete existing database file: " + fiDB[i].getAbsolutePath());					
				}
			}
		}
		
		try
		{
	        Class.forName("org.hsqldb.jdbcDriver");

	        con = DriverManager.getConnection("jdbc:hsqldb:file:" + sOutput + ";shutdown=true", "sa", "");
	        
	        DBImporter dbi = new DBImporter();
	        dbi.setFileEncoding(pEncoding);
	        dbi.execute(con, new FileInputStream(new File(sScript)));
	        
			System.out.println("Database created");
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			
			throw th;
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (SQLException ex)
				{
					//Irrelevant
				}
			}
		}
	}
    
}	//DBToolbox
