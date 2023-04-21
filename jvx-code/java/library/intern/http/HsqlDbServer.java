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
package http;

import java.io.File;
import java.io.PrintWriter;

import org.hsqldb.DatabaseManager;
import org.hsqldb.Server;
import org.hsqldb.server.ServerConstants;

import com.sibvisions.rad.ui.LauncherUtil;

/**
 * The <code>HsqlDbServer</code> is a class for starting and stopping a
 * hsql database server. Maybe we can use it for database tests.
 * 
 * @author René Jahn
 */
public class HsqlDbServer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** server instance of a hsql database. */
	private Server servDb = null;

	/** database path. */
	private String[] sDbFile = null;
	
	/** database names. */
	private String[] sDbName = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static void main(String[] pArgs)
	{
		System.out.println(System.getProperty("os.name"));
		
		String sPath;
		
		
		if (LauncherUtil.isWindows())
		{
			sPath = "D:/Entwicklung";
		}
		else
		{
			sPath = "/Entwicklung";
		}
			
		
		HsqlDbServer server = new HsqlDbServer
		(
			new String[] {sPath + "/sibvisions/jvx/trunk/java/library/testdbs/demo/demodb", 
						  sPath + "/sibvisions/jvx/trunk/java/library/testdbs/persons/personsdb", 
						  sPath + "/sibvisions/jvx/trunk/java/library/testdbs/test/testdb"}, 
			new String[] {"demodb", "personsdb", "testdb"}
		);
		
		server.start();
	}
	
	/**
	 * Creates a new instance of <code>HsqlDbServer</code>.
	 * 
	 * @param pDbFile a path list for database files
	 * @param pDbName a list of access-names for the databases
	 */
	public HsqlDbServer(String[] pDbFile, String[] pDbName)
	{
		this.sDbFile = pDbFile;
		this.sDbName = pDbName;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts up the desired database in server mode.
	 */
	public synchronized void start()
	{
		if (servDb == null)
		{
	        servDb = new Server();
	
	        servDb.setLogWriter(new PrintWriter(System.out));
	        servDb.setErrWriter(new PrintWriter(System.out));
	        servDb.setSilent(true);
	        servDb.setNoSystemExit(true);
	        servDb.setTls(false);
	        
	        for (int i = 0, anz = sDbFile.length; i < anz; i++)
	        {
	        	//delete lock file if possible
	        	new File(sDbFile[i] + ".lck").delete();
	        	
	        	servDb.setDatabaseName(i, sDbName[i]);
	        	servDb.setDatabasePath(i, "file:" + sDbFile[i]);
	        }
	        
	        servDb.start();
		}
	}
	
	/**
	 * Stops the database server if running.
	 */
	public synchronized void stop()
	{
		if (servDb != null)
		{
	        DatabaseManager.closeDatabases(0);

	        servDb.signalCloseAllServerConnections();
			servDb.stop();

			long lStop = System.currentTimeMillis() + 10000L;
			
	        while (servDb.getState() != ServerConstants.SERVER_STATE_SHUTDOWN && System.currentTimeMillis() < lStop) 
	        {
	            try 
	            {
	                Thread.sleep(100);
	            } 
	            catch (InterruptedException e) 
	            {}
	        }
	        
			servDb = null;
		}
	}
	
}	// HsqlDbServer
