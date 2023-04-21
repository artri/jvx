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
 * 25.08.2009 - [JR] - creation
 */
package apps.firstapp.frames;

import javax.rad.genui.UIDimension;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.MasterConnection;

import com.sibvisions.rad.application.Application;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;

/**
 * A simple database table editor.
 * <p/>
 * @author René Jahn
 */
public class DBEditFrame extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the application. */
	private Application application;
	
	/** the communication connection to the server. */
	private AbstractConnection connection;
	
	/** the DataSource for fetching table data. */
	private RemoteDataSource dataSource = new RemoteDataSource();
	
	/** the contacts tabl. */
	private RemoteDataBook rdbContacts = new RemoteDataBook();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>DBEditFrame</code> for a specific application.
	 * <p/>
	 * @param pApp the application
	 * @throws Throwable if the remote access fails
	 */
	public DBEditFrame(Application pApp) throws Throwable
	{
		super(pApp.getDesktopPane());
		
		application = pApp;
		
		initializeModel();
		initializeUI();
	}
	
	/**
	 * Initializes the model.
	 * <p/>
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		//we use a new "session" for the screen
		connection = ((MasterConnection)application.getConnection()).
		             createSubConnection("apps.firstapp.frames.DBEdit");
		connection.open();

		//data connection
		dataSource.setConnection(connection);
		dataSource.open();
		
		//table
		rdbContacts.setDataSource(dataSource);
		rdbContacts.setName("contacts");
		rdbContacts.open();
	}
	
	/**
	 * Initializes the UI.
	 * <p/>
	 * @throws Exception if the initialization throws an error
	 */
	private void initializeUI() throws Exception
	{
		UIGroupPanel group = new UIGroupPanel();
		group.setText("Available Contacts");
		
		UITable table = new UITable();
		table.setDataBook(rdbContacts);
		
		group.setLayout(new UIBorderLayout());
		group.add(table);
		
		//same behaviour as centered component in BorderLayout 
		setLayout(new UIBorderLayout());
		add(group);
		
		setTitle("Contacts");
		setSize(new UIDimension(600, 500));
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Closes the communication connection and disposes the frame.
	 */
	@Override
	public void dispose()
	{
		try
		{
			connection.close();
		}
		catch (Throwable th)
		{
			//nothing to be done
		}
		finally
		{
			super.dispose();
		}
	}
	
}	// DBEditFrame
