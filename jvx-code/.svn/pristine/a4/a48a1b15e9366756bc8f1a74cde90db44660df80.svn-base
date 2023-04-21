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
 * 20.01.2010 - [JR] - Properties constructor added
 */
package remote.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.rad.remote.ConnectionInfo;

import com.sibvisions.rad.remote.AbstractSerializedConnection;
import com.sibvisions.rad.remote.ISerializer;

/**
 * The <code>VMConnection</code> is a simple interprocess communication
 * implementation of <code>AbstractSerializedConnection</code>.
 * 
 * @author René Jahn
 * @see AbstractSerializedConnection
 */
public class VMConnection extends AbstractSerializedConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** IPC server of the current VM. */
	private VMServer vmserver = null;
	
	/** Communication channel to the server. */
	private IChannel channel = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VMConnection</code>.
	 */
	public VMConnection()
	{
		this((ISerializer)null);
	}
	
	/**
	 * Creates a new instance of <code>VMConnection</code> with 
	 * properties containing relevant information. The supported property
	 * keys are:
	 * <ul>
	 *   <li>VMConnection.PROP_SERIALIZER</li>
	 * </ul>
	 * 
	 * @param pProperties the properties for the connection
	 * @throws ClassNotFoundException if the serializer is defined and could not be created                                  
	 */
	public VMConnection(Properties pProperties) throws ClassNotFoundException
	{
		super(pProperties);

		vmserver = VMServer.getInstance();		
	}

	/**
	 * Creates a new instance of <code>VMConnection</code> with
	 * a user-defined <code>ISerializer</code>.
	 * 
	 * @param pSerializer the serializer implementation
	 * @see ISerializer
	 */
	public VMConnection(ISerializer pSerializer)
	{
		super(pSerializer);
		
		vmserver = VMServer.getInstance();		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream(ConnectionInfo pConnectionInfo) throws Throwable
	{
		return channel.getInputStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream(ConnectionInfo pConnectionInfo) throws Throwable
	{
		if (channel == null)
		{
			channel = vmserver.open(); 
		}
		
		return channel.getOutputStream();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the remote <code>VMServer</code>. This is a potential security breach,
	 * but is nice for unit tests.
	 * 
	 * @return the remote <code>VMServer</code>
	 */
	final VMServer getServer()
	{
		return vmserver;
	}

}	// VMConnection
