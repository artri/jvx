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
 * 28.12.2010 - [JR] - #229: login/logout event handling
 */
package javax.rad.application.genui;

import javax.rad.application.IConnectable;
import javax.rad.application.IDataSourceHandler;
import javax.rad.application.genui.event.RemoteApplicationHandler;
import javax.rad.application.genui.event.type.application.IAfterLoginApplicationListener;
import javax.rad.application.genui.event.type.application.IAfterLogoutApplicationListener;
import javax.rad.model.IDataSource;
import javax.rad.remote.AbstractConnection;

/**
 * The <code>RemoteApplication</code> extends the {@link Application} with
 * an {@link AbstractConnection}.
 * 
 * @author Ren� Jahn
 */
public abstract class RemoteApplication extends Application 
                                        implements IConnectable,
                                                   IDataSourceHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the "after login" event. */
	private transient RemoteApplicationHandler<IAfterLoginApplicationListener> eventAfterLogin;
	/** the "after logout" event. */
	private transient RemoteApplicationHandler<IAfterLogoutApplicationListener> eventAfterLogout;
	
	/** the connection of the application. */
	private transient AbstractConnection connection;
	
	/** the datasource. */
	private transient IDataSource dataSource;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>RemoteApplication</code> with
	 * a desired launcher.
	 * 
	 * @param pLauncher the launcher of this application
	 * @throws Exception if instance creation failss
	 */
	public RemoteApplication(UILauncher pLauncher) throws Exception
	{
		this(pLauncher, null);
	}
	
	/**
	 * Creates a new instance of <code>RemoteApplication</code> with a desired
	 * launcher and connection.
	 * 
	 * @param pLauncher the launcher of this application
	 * @param pConnection the connection
	 * @throws Exception if instance creation fails
	 */
	public RemoteApplication(UILauncher pLauncher, AbstractConnection pConnection) throws Exception
	{
		super(pLauncher);
		
		connection = pConnection;
		
		dataSource = createDataSource(connection);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Creates the datasource.
	 * 
	 * @param pConnection the connection or <code>null</code>
	 * @return the datasource or <code>null</code> if connection is <code>null</code>
	 * @throws Exception if opening datasource fails
	 */
	protected abstract IDataSource createDataSource(AbstractConnection pConnection) throws Exception;
	
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
		
		if (dataSource instanceof IConnectable)
		{
			((IConnectable)dataSource).setConnection(pConnection);
		}
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
	public IDataSource getDataSource()
	{
		return dataSource;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the event handler for the after login event.
	 * 
	 * @return the event handler
	 */
	public RemoteApplicationHandler<IAfterLoginApplicationListener> eventAfterLogin()
	{
		if (eventAfterLogin == null)
		{
			eventAfterLogin = new RemoteApplicationHandler<IAfterLoginApplicationListener>(IAfterLoginApplicationListener.class);
		}
		return eventAfterLogin;
	}

	/**
	 * Gets the event handler for the after logout event.
	 * 
	 * @return the event handler
	 */
	public RemoteApplicationHandler<IAfterLogoutApplicationListener> eventAfterLogout()
	{
		if (eventAfterLogout == null)
		{
			eventAfterLogout = new RemoteApplicationHandler<IAfterLogoutApplicationListener>(IAfterLogoutApplicationListener.class);
		}
		return eventAfterLogout;
	}
	
	/**
	 * Fires the after logout event.
	 */
	protected void afterLogout()
	{
		getLauncher().cancelPendingThreads();
		
		if (eventAfterLogout != null)
		{
			eventAfterLogout.dispatchEvent(this);
		}
	}
	
	/**
	 * Fires the after login event.
	 */
	protected void afterLogin()
	{
		if (eventAfterLogin != null)
		{
			eventAfterLogin.dispatchEvent(this);
		}
	}
	
}	// RemoteApplication
