/*
 * Copyright 2017 SIB Visions GmbH
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
 * 01.08.2017 - [RZ] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.SQLException;

import org.junit.Assert;

/**
 * Tests the {@link TestTiberoDBAccess} class.
 * 
 * @author René Jahn
 */
public class TestTiberoDBAccess extends TestOracleDBAccess
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected DBAccess createDBAccess()
    {
        return new TiberoDBAccess();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDBURL()
    {
        return "jdbc:tibero:thin:@" + getHostName() + ":8629:tibero";       
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getHostName()
    {
        return "192.168.1.133";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void testSqlVarrayAndObject() throws Exception
    {
        try
        {
            super.testSqlVarrayAndObject();
        }
        catch (SQLException e)
        {
            //unsupported operation
            Assert.assertEquals(-90201, e.getErrorCode());
        }
    }
	
}	// TestTiberoDBAccess
