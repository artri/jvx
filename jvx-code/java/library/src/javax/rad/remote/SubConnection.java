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
 * 02.02.2009 - [JR] - open: add the connection to the master
 * 13.12.2011 - [JR] - #523: open: don't copy the master properties (moved to server-side)
 * 04.08.2012 - [JR] - isOpen now checks if master connection is open
 */
package javax.rad.remote;

import javax.rad.util.UIInvoker;

/**
 * The <code>SubConnection</code> uses the connection of a
 * {@link MasterConnection} for transfering data to the server. It has
 * no alive check.
 * 
 * @author René Jahn
 * @see IConnection
 */
public class SubConnection extends AbstractConnection
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the reference for the application connection. */
	private MasterConnection mcParent = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SubConnection</code>.
	 * 
	 * @param pMaster the master connection
	 * @see MasterConnection
	 */
	SubConnection(MasterConnection pMaster)
	{
		super(pMaster.getConnection());
		
		mcParent = pMaster;
		
		mcParent.addSubConnection(this);

		//use the compression setting from our parent
		coninf.getProperties().put(IConnectionConstants.COMPRESSION, mcParent.coninf.getProperties().get(IConnectionConstants.COMPRESSION));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openConnection() throws Throwable
	{
		connection.openSub(mcParent.coninf, coninf);

		//-> necessary if the connection will be closed and opened again!
		mcParent.addSubConnection(this);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected UIInvoker getUIInvoker()
    {
        return mcParent.getUIInvoker();
    }	
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void reopen() throws Throwable
    {
        if (!mcParent.isOpen())
        {
            mcParent.removeSubConnection(this);
            mcParent.reopen();
        }
        
        super.reopen();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen()
    {
        return mcParent != null && mcParent.isOpen() && super.isOpen();
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void close(boolean pDoCall) throws Throwable
	{
		super.close(pDoCall);

		mcParent.removeSubConnection(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the master connection.
	 * 
	 * @return the master connection
	 */
	public MasterConnection getMasterConnection()
	{
		return mcParent;
	}
		
}	// SubConnection
