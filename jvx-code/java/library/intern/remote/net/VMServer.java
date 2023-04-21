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
 * 04.02.2009 - [JR] - used ByteArrayInputStream and ByteArrayOutputStream instead of piped streams to
 *                     solve read/write delays and 'broken pipe' exceptions
 */
package remote.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.sibvisions.rad.server.IRequest;
import com.sibvisions.rad.server.IResponse;
import com.sibvisions.rad.server.Server;

/**
 * The <code>VMServer</code> is a remote server implementation with
 * interprocess communication.
 *   
 * @author René Jahn
 */
public final class VMServer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** singleton of <code>VMServer</code>. */
	private static VMServer ipcserver = null;
	
	/** remote server implementation. */
	private Server server = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * The <code>VMServer</code> is a singleton, so it's not possible
	 * to create an instance from the outside.
	 */
	private VMServer()
	{
		server = Server.getInstance();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the singleton of <code>VMServer</code>.
	 * 
	 * @return the instance of <code>VMServer</code>
	 */
	public static VMServer getInstance()
	{
		if (ipcserver == null)
		{
			ipcserver = new VMServer();
		}
		
		return ipcserver;
	}
	
	/**
	 * Opens a new communication channel for a client connection.
	 * 
	 * @return communication tunnel
	 */
	public IChannel open()
	{
		ClientConnection ccon = new ClientConnection();
				
		return ccon;
	}
	
	/**
	 * Returns the remote server. This is a potential security breach,
	 * but is nice for unit tests.
	 * 
	 * @return the remote server
	 */
	final Server getServer()
	{
		return server;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Simple client communication implementation.
	 * 
	 * @author René Jahn
	 */
	private class ClientConnection implements IChannel
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the output stream for the client request. */
		private ByteArrayOutputStream baosRequest = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public InputStream getInputStream() throws Throwable
		{
			ByteArrayOutputStream baosResponse = new ByteArrayOutputStream();
			
			ByteArrayInputStream baisRequest = new ByteArrayInputStream(baosRequest.toByteArray());
			
			server.process(new VMRequest(baisRequest), new VMResponse(baosResponse));
			
			baosRequest.close();
			baisRequest.close();
			
			ByteArrayInputStream baisResponse = new ByteArrayInputStream(baosResponse.toByteArray());

			baosResponse.close();
							
			return baisResponse;
		}

		/**
		 * {@inheritDoc}
		 */
		public OutputStream getOutputStream() throws Throwable
		{
			baosRequest = new ByteArrayOutputStream(); 
			
			return baosRequest;
		}

	}	// ClientConnection
	
	/**
	 * Simple <code>IRequest</code> implementation with basic functionality.
	 * 
	 * @author René Jahn
	 * @see IRequest
	 */
	private final class VMRequest implements IRequest
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the input stream for the server. */
		private InputStream in;
		
		/** whether the request was closed. */
		private boolean bClosed;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>VMRequest</code> as tunnel for an
		 * input stream.
		 * 
		 * @param pIn the input stream
		 */
		private VMRequest(InputStream pIn)
		{
			this.in = pIn;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public InputStream getInputStream() throws IOException
		{
            if (isClosed())
            {
                throw new IOException("Stream is closed!");
            }

            return in;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public Object getProperty(String pKey)
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public Hashtable<String, Object> getProperties()
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void close()
		{
			bClosed = true;
		}

        /**
         * {@inheritDoc}
         */
		public boolean isClosed()
		{
		    return bClosed;
		}
		
	}	// VMRequest
	
	/**
	 * Simple <code>IResponse</code> implementation with basic functionality.
	 * 
	 * @author René Jahn
	 * @see IResponse
	 */
	private final class VMResponse implements IResponse
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the output stream for the server. */
		private OutputStream out;

		/** whether the response was closed. */
		private boolean bClosed;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>VMResponse</code> as tunnel for an
		 * output stream.
		 * 
		 * @param pOut the output stream
		 */
		private VMResponse(OutputStream pOut)
		{
			this.out = pOut;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public OutputStream getOutputStream() throws IOException
		{
		    if (isClosed())
		    {
		        throw new IOException("Stream is closed!");
		    }
		    
			return out;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setProperty(String pKey, Object pValue)
		{
			//nothing to be done
		}
		
        /**
         * {@inheritDoc}
         */
        public void close()
        {
            bClosed = true;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isClosed()
        {
            return bClosed;
        }
		
	}	// VMResponse
	
}	// VMServer
