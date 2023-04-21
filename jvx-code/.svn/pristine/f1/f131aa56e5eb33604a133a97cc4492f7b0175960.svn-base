/*
 * Copyright 2021 SIB Visions GmbH
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
 * 11.05.2021 - [MB] - creation
 */
package com.sibvisions.rad.persist.jdbc;

/**
 * Tests the {@link InformixDBAccess} class with a DB that has transactions enabled.
 * 
 * @author Michael Bartl
 */
public class TestInformixDBAccessTransactions extends TestInformixDBAccess
{
    /** LOCAL INFORMIX DB URL with transactions. */
    //private final String url = "jdbc:informix-sqli://sibnb9:9088/inflog:informixserver=ol_informix1210;DELIMIDENT=Y;";
    /** VM INFORMIX DB URL. */
    private final String url = "jdbc:informix-sqli://192.168.2.12:9088/inflog:informixserver=ol_informix1210;DELIMIDENT=Y;";
    
    /**
     * {@inheritDoc}
     */
    protected String getURL()
    {
        return url;
    }
}
