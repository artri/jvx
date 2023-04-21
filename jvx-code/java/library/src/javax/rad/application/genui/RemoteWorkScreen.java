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
 * 21.11.2008 - [JR] - creation
 * 26.11.2008 - [JR] - BorderLayout as default layout
 * 17.10.2013 - [JR] - #842: constructor with Map created
 */
package javax.rad.application.genui;

import java.util.Map;

import javax.rad.application.IConnectable;
import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.remote.AbstractConnection;

/**
 * The <code>RemoteWorkScreen</code> extends the {@link WorkScreen} with
 * an {@link AbstractConnection}.
 * 
 * @author René Jahn
 */
public class RemoteWorkScreen extends WorkScreen 
                              implements IConnectable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the connection of the work-screen. */
	private transient AbstractConnection connection;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RemoteWorkScreen</code>.
	 */
	public RemoteWorkScreen()
	{
	}

	/**
	 * Creates a new instance of <code>RemoteWorkScreen</code> for a parent
	 * application.
	 * 
	 * @param pApplication the parent application
	 */
	public RemoteWorkScreen(IWorkScreenApplication pApplication)
	{
		this(pApplication, null, null);
	}

	/**
	 * Creates a new instance of <code>RemoteWorkScreen</code> for a parent
	 * application and screen parameters.
	 * 
	 * @param pApplication the parent application
	 * @param pParameter additional screen parameters
	 */
	public RemoteWorkScreen(IWorkScreenApplication pApplication, Map<String, Object> pParameter)
	{
		this(pApplication, null, pParameter);
	}
	
	/**
	 * Creates a new instance of <code>RemoteWorkScreen</code> with a specific
	 * launcher and connection.
	 * 
	 * @param pApplication the parent application
	 * @param pConnection the connection
	 */
	public RemoteWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection)
	{
		this(pApplication, pConnection, null);
	}

	/**
	 * Creates a new instance of <code>RemoteWorkScreen</code> with a specific
	 * launcher, connection and screen parameters.
	 * 
	 * @param pApplication the parent application
	 * @param pConnection the connection
	 * @param pParameter additional screen parameters
	 */
	public RemoteWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection, Map<String, Object> pParameter)
	{
		super(pApplication, pParameter);
		
		connection = pConnection;
		
		setLayout(new UIBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public AbstractConnection getConnection()
	{
		return connection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setConnection(AbstractConnection pConnection)
	{
		connection = pConnection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isConnected()
	{
		AbstractConnection con = getConnection();
		
		return con != null && con.isOpen();
	}

	/**
	 * {@inheritDoc}
	 */
	public RemoteWorkScreenApplication getApplication()
	{
		return (RemoteWorkScreenApplication)application;
	}
	
}	// RemoteWorkScreen
