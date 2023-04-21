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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The <code>RadContextListener</code> is a {@link ServletContextListener} which starts
 * and stops the test databases. The init/destroy method will be triggerd through
 * the application server.
 * 
 * @author René Jahn
 */
public final class RadContextListener implements ServletContextListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the database server instance. */
	private HsqlDbServer server = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public synchronized void contextInitialized(ServletContextEvent pEvent)
	{
		//DON'T REUSE THE 'server' instance, because the implementation has some problems!
		
		String sOs = System.getProperty("os.name").toLowerCase();
		
		
		if (sOs.indexOf("linux") >= 0 || sOs.indexOf("mac") >= 0)
		{
			String sDir = System.getProperty("user.home") + "/dbs/";
			
			//startup database(s)
			server = new HsqlDbServer
			(
				new String[] {sDir + "demo/demodb", sDir + "persons/personsdb", sDir + "test/testdb"}, 
				new String[] {"demodb", "personsdb", "testdb"}
			);			
		}
		else
		{
			//startup database(s)
			server = new HsqlDbServer
			(
				new String[] {"D:\\Entwicklung\\sibvisions\\jvx\\trunk\\java\\library\\testdbs\\demo\\demodb", 
							  "D:\\Entwicklung\\sibvisions\\jvx\\trunk\\java\\library\\testdbs\\persons\\personsdb", 
							  "D:\\Entwicklung\\sibvisions\\jvx\\trunk\\java\\library\\testdbs\\test\\testdb"}, 
				new String[] {"demodb", "personsdb", "testdb"}
			);
		}

		server.start();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void contextDestroyed(ServletContextEvent pEvent)
	{
		server.stop();
	}
	
}	//RadContextListener
