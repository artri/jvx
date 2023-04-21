/*
 * Copyright 2016 SIB Visions GmbH
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
 * 22.06.2016 - [JR] - creation
 */
package javax.rad.server.push;

import javax.rad.server.ISession;
import javax.rad.server.ResultObject;

/**
 * The <code>PushMessage</code> class will be used for pushing objects to {@link IPushReceiver}.
 * 
 * @author René Jahn
 */
public class PushMessage
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the message type. */
    public enum MessageType
    {
        /** callback message. */
        Callback;
    }
    
    /** the session. */
    private ISession session;
    
    /** the message type. */
    private MessageType type;
    
    /** the object. */
    private ResultObject object;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>PushMessage</code>.
     * 
     * @param pSession the session
     * @param pType the message type
     * @param pObject the object
     */
    public PushMessage(ISession pSession, MessageType pType, ResultObject pObject)
    {
        session = pSession;
        type = pType;
        object = pObject;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the session.
     * 
     * @return the session
     */
    public ISession getSession()
    {
        return session;
    }
    
    /**
     * Gets the message type.
     * 
     * @return the type
     */
    public MessageType getType()
    {
        return type;
    }
    
    /**
     * Gets the object.
     * 
     * @return the object
     */
    public ResultObject getObject()
    {
        return object;
    }
    
}   // PushMessage
