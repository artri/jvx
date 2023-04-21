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
 * 28.03.2010 - [JR] - creation
 * 18.03.2013 - [RH] - #632: DBStorage: Update on Synonym (Oracle) doesn't work - Synonym Support implemented
 */
package com.sibvisions.rad.persist.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.persist.jdbc.param.InOutParam;
import com.sibvisions.util.ArrayUtil;

/**
 * Test the functionality of {@link OracleDBAccess} with Oracle 12+.
 * 
 * @author René Jahn
 */
public class TestOracleDBAccessV12 extends TestOracleDBAccess 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getHostName()
	{
		return "192.168.1.236";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getUserName()
	{
		return "C##TEST";
	}
	
    /**
     * Tests varray access.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testPackageTypes() throws Exception
    {
        Connection con = dba.getConnection();
        
        try 
        { 
            dba.executeStatement("insert into users (id, username, created_by, created_on, changed_by, changed_on) values (1, 'admin', 'admin', sysdate, 'admin', sysdate)");
            dba.executeStatement("insert into users (id, username, created_by, created_on, changed_by, changed_on) values (2, 'martin', 'admin', sysdate, 'admin', sysdate)");
        } 
        catch (Exception ex) 
        { 
            // Do Nothing
        }
        
        dba.executeStatement(
                "create or replace package TestPackageType is " +
                "  type r_user is record (id number(16), username varchar2(200)); " +
                "  type t_users is table of r_user; " + 
                "  procedure test(pOutput in out t_users); " +
                "end;");
        dba.executeStatement(
                "create or replace package body TestPackageType is " + 
                "  procedure test(pOutput in out t_users) is " +
                "  begin " +
                "    FOR i IN pOutput.FIRST .. pOutput.LAST LOOP " +
                "      insert into users (id, username, created_by, created_on, changed_by, changed_on) " +
                "        values (pOutput(i).id, pOutput(i).username, 'admin', sysdate, 'admin', sysdate); " +
                "    end loop; " +
                "    select id, username " +
                "      bulk collect into pOutput " +
                "      from users" +
                "     order by id; " +
                "  end; " +
                "end;");
        dba.setAutoCommit(false);

        BeanType btUser = new BeanType("ID", "USERNAME");
        Bean bean1 = new Bean(btUser, "3", "Otto");
        Bean bean2 = new Bean(btUser, "4", "Walkes");
        
        InOutParam param = InOutParam.newArrayParam("TESTPACKAGETYPE.T_USERS", new Object[] {bean1, bean2});
        
        dba.executeProcedure("TESTPACKAGETYPE.TEST", param);
                
        Assert.assertEquals(new ArrayUtil(
                new Bean(btUser, BigDecimal.valueOf(1), "admin"), 
                new Bean(btUser, BigDecimal.valueOf(2), "martin"), 
                new Bean(btUser, BigDecimal.valueOf(3), "Otto"), 
                new Bean(btUser, BigDecimal.valueOf(4), "Walkes")), 
                param.getValue());

    }
	
	
}
