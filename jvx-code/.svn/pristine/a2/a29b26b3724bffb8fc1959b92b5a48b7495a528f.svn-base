/*
 * Copyright 2014 SIB Visions GmbH
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
 * 14.11.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.protocol;

/**
 * The <code>IProtocolWriter</code> defines writing record logs. It should be used in combination
 * with a custom {@link ProtocolFactory}.
 * 
 * @author René Jahn
 */
public interface IProtocolWriter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Opens a new record.
     * 
     * @param pCategory the category
     * @param pCommand the command
     * @return the new record
     */
    public Record openRecord(String pCategory, String pCommand);
    
    /**
     * Closes a record and writes the protocol.
     * 
     * @param pRecord the record to write
     */
    public void closeRecord(Record pRecord);
    
}   // IProtocolWriter
