/*
 * Copyright 2015 SIB Visions GmbH
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
 * 28.05.2015 - [JR] - creation
 */
package javax.rad.server.event;

import javax.rad.server.ISession;

/**
 * The <code>SessionEvent</code> is an event which contains an {@link ISession} as 
 * source. 
 * 
 * @author René Jahn
 */
public class SessionEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the session. */
    private ISession session;
    
    /** the source session. */
    private ISession sessionSource;
    
    /** the creation time. */
    private long lCreation;
    
    /** whether the event is marked faulty. */
    private boolean bHasError;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>SessionEvent</code>.
     * 
     * @param pSession the assigned session
     * @param pSource the source session
     */
    public SessionEvent(ISession pSession, ISession pSource)
    {
        this(pSession, pSource, false);
    }
    
    /**
     * Creates a new instance of <code>SessionEvent</code>.
     * 
     * @param pSession the assigned session
     * @param pSource the source session
     * @param pHasError whether the event should marked faulty
     */
    public SessionEvent(ISession pSession, ISession pSource, boolean pHasError)
    {
        session = pSession;
        sessionSource = pSource;
        
        bHasError = pHasError;
        
        lCreation = System.currentTimeMillis();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the event session.
     * 
     * @return the session
     */
    public ISession getSession()
    {
        return session;
    }
    
    /**
     * Gets the event source session. This session might not always the same as {@link #getSession()}.
     * 
     * @return the source/triggering session
     */
    public ISession getSourceSession()
    {
        return sessionSource;
    }
    
    /**
     * Gets the creation time of this event.
     * 
     * @return the time in millis
     */
    public long getCreationTime()
    {
        return lCreation;
    }
    
    /**
     * Gets whether this event was marked as error event.
     * 
     * @return <code>true</code> if marked faulty.
     */
    public boolean hasError()
    {
        return bHasError;
    }
    
}   // SessionEvent
