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
 * 08.06.2016 - [JR] - creation
 * 27.05.2018 - [JR] - #1928: publishMaster defined
 * 09.03.2019 - [JR] - #1996: refactoring and PublishMode introduced
 */
package javax.rad.server;

/**
 * The <code>ICallBackBroker</code> enables publishing of objects from the server to the client.
 * 
 * @author René Jahn
 */
public interface ICallBackBroker
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the publish mode. */
	public enum PublishMode
	{
		/** only the current session. */
		CurrentSession,
		/** other sessions from the current master session. */
		OtherSessions,
		/** only the master of the current session. */
		CurrentMasterSession,
		/** all sessions which are like the current session, and the current session. */
		AllCurrentSessions,
		/** all sessions which are like the current session, but not the current session. */
		AllOtherSessions,
		/** all master sessions. */
		AllMasterSessions,
		/** all master sessions but not the master session of the current session. */
		AllOtherMasterSessions
	}
	
    /** The publish state. */
    public enum PublishState
    {
        /** Published to all relevant sessions. */
        Completed,
        /** Published to some sessions, not all. */
        Partial,
        /** Not published. */
        Failed;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Publishs an object to one or more specific client.
     * 
     * @param pInstruction the instruction identifier
     * @param pObject the object to publish
     * @param pMode the mode defines which client will be notified
     * @return the publish state
     */
    public PublishState publish(String pInstruction, Object pObject, PublishMode... pMode);
    
}   // ICallBackBroker
