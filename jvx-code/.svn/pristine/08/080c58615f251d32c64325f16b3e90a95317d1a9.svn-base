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

/**
 * The <code>IPushHandler</code> interface defines the handler for the push mechanism.
 * The handler is responsible for the notification of push receivers.
 * 
 * @author René Jahn
 */
public interface IPushHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Registers a push receiver.
     * 
     * @param pConnectionId the connection identifier
     * @param pReceiver the receiver
     */
    public void registerPushReceiver(Object pConnectionId, IPushReceiver pReceiver);
    
    /**
     * Unregisters a push receiver.
     * 
     * @param pConnectionId the connection identifier
     */
    public void unregisterPushReceiver(Object pConnectionId);
    
    /**
     * Pushes a message to the registered receiver.
     * 
     * @param pMessage the message
     */
    public void push(PushMessage pMessage);
    
}   // IPushHandler
