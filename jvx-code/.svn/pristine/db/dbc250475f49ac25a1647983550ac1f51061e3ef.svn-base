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
 * 12.11.2011 - [JR] - creation
 */
package com.sibvisions.util.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.db.DBImporter.DBStatement;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The <code>TestDBImporter</code> tests the functionality of {@link DBImporter}.
 * 
 * @author René Jahn
 */
public class TestDBImporter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests loading of sql statements from a resource file.
	 * 
	 * @throws Exception if the test fails
	 */
	@Test
	public void testLoadStatements() throws Exception
	{
		DBImporter dbi = new DBImporter();
		List<DBStatement> liStmt = dbi.list(ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/test.sql"));
		
		Assert.assertEquals("insert into orders (id, name) values (0, 'zero')", liStmt.get(0).statement);
		Assert.assertEquals("insert into orders (id, name)\nvalues (1, 'first')", liStmt.get(1).statement);
		Assert.assertEquals("insert into orders (id, name)\nvalues (2, 'second')", liStmt.get(2).statement);
	}
	
	/**
	 * Tests custom commands in db scripts.
	 * 
	 * @throws Exception if command detection fails
	 */
	@Test
	public void testCommandParsing() throws Exception
	{
		String sPath = ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(this));
		
		DBImporter dbi = new DBImporter();
		DBStatement stmt = dbi.createDBStatement("insert into table(id, name, image1, image2, image3) " + 
				                                 "values(1, 'JVx', loadBinary('" + sPath + "/com/sibvisions/util/db/checkbox_no.png')," +
				                                         "loadBinary(\"" + sPath + "/com/sibvisions/util/db/checkbox_yes.png\"), null)");
		
		Assert.assertEquals("insert into table(id, name, image1, image2, image3) values(1, 'JVx', ?,?, null)", stmt.statement);
		
		Assert.assertEquals(2, stmt.params.size());
		Assert.assertTrue(stmt.params.get(0) instanceof InputStream);
		Assert.assertTrue(stmt.params.get(1) instanceof InputStream);
		
		stmt = dbi.createDBStatement("insert into ROLE_WOSC (ROLE_ID, WOSC_ID, CREATED_BY, CREATED_ON, CHANGED_BY, CHANGED_ON) " +
				                     "VALUES (${ROLEID}, ${WOSCID}, ${VISIONX_USERNAME}, executeTime(), ${VISIONX_USERNAME}, executeTime())");
		
		Assert.assertEquals("insert into ROLE_WOSC (ROLE_ID, WOSC_ID, CREATED_BY, CREATED_ON, CHANGED_BY, CHANGED_ON) " + 
				            "VALUES (${ROLEID}, ${WOSCID}, ${VISIONX_USERNAME}, ?, ${VISIONX_USERNAME}, ?)",
				            stmt.statement);
		
		Assert.assertEquals(2, stmt.params.size());
		Assert.assertTrue(stmt.params.get(0) instanceof Timestamp);
		Assert.assertTrue(stmt.params.get(1) instanceof Timestamp);
	}

	/**
	 * Tests parsing of return variables like ${VAR1} = ...
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testParseReturnVariable() throws Exception
	{
		DBImporter dbi = new DBImporter();
		DBStatement stmt = dbi.createDBStatement("${VAR1} = select id from test;");
		
		Assert.assertEquals("select id from test", stmt.statement);
		
		try
		{
			stmt = dbi.createDBStatement("${VAR1} select id from test;");
		}
		catch (RuntimeException re)
		{
			Assert.assertEquals("Can't assign a value to variable '${VAR1}' without '=' sign!", re.getMessage());
		}
	}
	
	/**
	 * Tests parameter replacement.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testParameterReplacement() throws Exception
	{
		Class.forName("org.hsqldb.jdbcDriver");
		
		Connection con = DriverManager.getConnection("jdbc:hsqldb:mem:dbimport", "sa", "");
		  
		try
		{
			DBImporter dbi = new DBImporter();
			dbi.execute(con, ResourceUtil.getResourceAsStream("/" + getClass().getPackage().getName().replace(".", "/") + "/vars.sql"));
			
			Assert.assertEquals(Integer.valueOf(2), dbi.getParameter("MAX"));
			Assert.assertEquals(Integer.valueOf(5), dbi.getParameter("RESULT"));
		}
		finally
		{
			con.close();
		}
	}
	
}	// TestDBImporter
