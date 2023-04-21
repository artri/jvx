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
 * 08.05.2009 - [JR] - creation
 * 04.10.2009 - [JR] - setProperty: security for CLIENT properties
 * 18.11.2009 - [JR] - #33: WrappedSession: put implemented
 * 02.03.2011 - [JR] - #297: implemented addObject, removeObject, getObject and renamed addObject to putObject
 * 10.07.2013 - [JR] - #725: extended AbstractSessionContext instead of SessionContext
 */
package com.sibvisions.rad.server;

import javax.rad.server.ICallBackBroker;
import javax.rad.server.ICallHandler;
import javax.rad.server.ISession;

/**
 * The <code>SessionContextImpl</code> is an internal {@link javax.rad.server.SessionContext} implementation for
 * the {@link AbstractSession}.
 * 
 * @author René Jahn
 */
final class SessionContextImpl extends AbstractSessionContext
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SessionContextImpl</code> for a specific
	 * {@link ISession}.
	 * 
	 * @param pSession the associated session for this {@link javax.rad.server.SessionContext}
	 */
	SessionContextImpl(ISession pSession)
	{
		super(pSession);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
	@Override
    public ICallHandler getCallHandler()
    {
		return ((WrappedSession)getSession()).session.getCallHandler();
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
	public ICallBackBroker getCallBackBroker()
	{
    	return ((WrappedSession)getSession()).session.getCallBackBroker();
	}
	
}	// SessionContextImpl
