/*
 * Copyright 2011 SIB Visions GmbH
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
 * 16.10.2013 - [JR] - file encoding support
 */
package com.sibvisions.util.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>DBImporter</code> executes DML or DDL statements. The statemens are read from simple text files.
 * Use an empty line or a semicolon to separate statements. Use <code>//</code>, <code>#</code> or <code>--</code>
 * to comment single lines. A comment line is completly ignored. 
 * It allows simple scripting like:
 * <pre>
 * ${VAR1} = select max(id) from table
 * 
 * insert into detail(id, name, table_id) values (1, 'JVx', ${VAR1})
 * </pre>
 * Use custom commands to extend the standard calls:
 * <pre>
 * insert into table(name, creation) values ('JVx', executeTime())
 * </pre>
 * The above command <code>executeTime</code> is mapped to <code>ExecuteTimeCommand</code>.
 * 
 * @author René Jahn
 */
public class DBImporter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(DBImporter.class);
	
	/** the list of exceptions. */
	private List<Exception> liExceptions = new ArrayUtil<Exception>();
	
	/** the user-defined parameters for script execution. */
	private HashMap<String, Object> hmpParameters = new HashMap<String, Object>();

	/** file encoding. */
	private String sEncoding;
	
	/** whether the execution should be canceled on a failure. */
	private boolean bCancelOnFailure = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets all statements from the given stream.
	 * 
	 * @param pInput the stream with statements
	 * @return the list of found statements
	 * @throws Exception if statement parsing fails
	 */
	public List<DBStatement> list(InputStream pInput) throws Exception
	{
        BufferedReader brContent = new BufferedReader(new InputStreamReader(pInput));

        StringBuilder sbStatement = new StringBuilder();
        
        ArrayList<DBStatement> alStatements = new ArrayList<DBStatement>();

        String sLine = brContent.readLine();
        
        DBStatement stmt;
        
        while (sLine != null)
        {
        	sLine = sLine.trim();

        	if (sLine.length() > 0 && !sLine.startsWith("--") && !sLine.startsWith("#") && !sLine.startsWith("//"))
        	{
        		if (sbStatement.length() > 0)
        		{
        			sbStatement.append('\n');
        		}
        		sbStatement.append(sLine);
        	}
        	
        	if ("".equals(sLine) || sLine.endsWith(";"))
        	{
                if (sbStatement.length() > 0)
                {
                	stmt = createDBStatement(sbStatement.toString());
                	
                	if (stmt != null)
                	{
	                    alStatements.add(stmt);
                	}
                    sbStatement.setLength(0);
                }
        	}
        	
            sLine = brContent.readLine();
        }
        
        if (sbStatement.length() > 0)
        {
        	stmt = createDBStatement(sbStatement.toString());
        	
        	if (stmt != null)
        	{
        		alStatements.add(stmt);
        	}
        }
        
        return alStatements;
	}
	
	/**
	 * Reads the statements from a stream and executes each statement.
	 * 
	 * @param pConnection the database connection
	 * @param pInput the statement stream
	 * @return <code>true</code> if executions was successful, <code>false</code> if an error occured and {@link #setCancelOnFailure(boolean)}
	 *         is enabled
	 * @throws Exception if execution of a statement failed	and {@link #setCancelOnFailure(boolean)} is disabled
	 */
	public boolean execute(Connection pConnection, InputStream pInput) throws Exception
	{
		liExceptions.clear();
		
		InputStreamReader isr;
		
		if (sEncoding != null)
		{
			isr = new InputStreamReader(pInput, sEncoding);
		}
		else
		{
			//system encoding
			isr = new InputStreamReader(pInput);
		}
		
        BufferedReader brContent = new BufferedReader(isr);

        StringBuilder sbStatement = new StringBuilder();
        
        String sLine = brContent.readLine();
        
        boolean bError = true;
        
        
        while (sLine != null)
        {
        	sLine = sLine.trim();

        	if (sLine.length() > 0 && !sLine.startsWith("--") && !sLine.startsWith("#") && !sLine.startsWith("//"))
        	{
        		if (sbStatement.length() > 0)
        		{
        			sbStatement.append('\n');
        		}
        		sbStatement.append(sLine);
        	}
        	
        	if ("".equals(sLine) || sLine.endsWith(";"))
        	{
                if (sbStatement.length() > 0)
                {
                	bError &= executeStatement(pConnection, sbStatement.toString());
                
                    sbStatement.setLength(0);
                }
        	}
        	
            sLine = brContent.readLine();
        }
        
        if (sbStatement.length() > 0)
        {
        	bError &= executeStatement(pConnection, sbStatement.toString());
        }
        
        return bError;
	}
	
	/**
	 * Executes the a statement in a database.
	 * 
	 * @param pConnection the database connection
	 * @param pStatement the statement
	 * @return <code>true</code> if executions was successful, <code>false</code> if an error occured and {@link #setCancelOnFailure(boolean)}
	 *         is enabled
	 * @throws Exception if execution of a statement failed	and {@link #setCancelOnFailure(boolean)} is disabled
	 */
	private boolean executeStatement(Connection pConnection, String pStatement) throws Exception
	{
		DBStatement stmt = createDBStatement(pStatement);
		
		if (stmt == null)
		{
			return true;
		}
		
		PreparedStatement ps = null;
		
		try
		{
			ps = pConnection.prepareStatement(stmt.statement);
			
			Object objParam;

			for (int i = 0, anz = stmt.params.size(); i < anz; i++) 
			{
				objParam = stmt.params.get(i);

				if (objParam instanceof InputStream)
				{
					ps.setBinaryStream(i + 1, (InputStream)objParam);
				}
				else if (objParam == null)
				{
					ps.setNull(i + 1, Types.VARCHAR);
				}
				else
				{
					ps.setObject(i + 1, objParam);
				}
			}

			if (ps.execute())
			{
				ResultSet res = ps.getResultSet();

				if (stmt.returnParameter != null)
				{
					if (res.next())
					{
						if (res.getMetaData().getColumnCount() > 1)
						{
							log.debug("Statement '", stmt.statement, "' returned more than one column. We use the first column: '", res.getMetaData().getColumnName(1), "'");
						}
						Object oValue = res.getObject(1);

						if (res.next())
						{
							throw new RuntimeException("Statement '" + stmt.statement + "' returned more than one row");
						}
						
						hmpParameters.put(stmt.returnParameter, oValue);
					}
					else
					{
						throw new RuntimeException("Statement '" + stmt.statement + "' returned no rows");
					}
				}

				if (res != null)
				{
					try
					{
						res.close();
					}
					catch (Exception e)
					{
						//nothing to be done
					}
				}
			}
			
			return true;
		}
		catch (Exception e)
		{
			log.error(stmt.statement, stmt.params, e);
			
			liExceptions.add(e);
			
			if (bCancelOnFailure)
			{
				throw e;
			}
			
			return false;
		}
		finally
		{
			if (ps != null)
			{
				try
				{
					ps.close();
				}
				catch (Exception e)
				{
					//nothing to be done
				}
			}
		}
	}
	
	/**
	 * Creates a statement and parses the parameters.
	 * 
	 * @param pStatement the complete statement
	 * @return the statement with parameter syntax
	 * @throws Exception if parameter detection fails
	 */
	protected DBStatement createDBStatement(String pStatement) throws Exception
	{
		String sStatement = pStatement;
		
		//----------------------------------------------------------------------
		// Command replacement
		//----------------------------------------------------------------------

		if (sStatement.endsWith(";"))
		{
			sStatement = sStatement.substring(0, sStatement.length() - 1);
		}
		
		//Check supported script functions

		int iStart;
		int iEnd = 0;
		int iPos;
		int iBracketStart;
		
		char ch;
		
		String sCommandName;
		String sCommand;
	
		String sPackage = getClass().getPackage().getName();
		String sClassName;
		
		List<ICommand> liCommands = new ArrayUtil<ICommand>();
		
		ICommand cmd;
		
		while ((iPos = sStatement.indexOf("(", iEnd)) >= 0)
		{
			iBracketStart = iPos;
			iStart = iEnd;
			
			for (int i = iPos - 1; i >= iStart; i--)
			{
				ch = sStatement.charAt(i);
				
				if ((Character.isWhitespace(ch) && ch != '\n' && ch != '\r') || ch == ',')
				{
					iStart = i + 1;
					i = -1;
				}
			}
			
			sCommandName = sStatement.substring(iStart, iPos).trim();
			
			for (int i = iPos + 1, iBracket = 1, anz = sStatement.length(); i < anz; i++)
			{
				ch = sStatement.charAt(i);
				
				if (ch == '(')
				{
					iBracket++;
				}
				else if (ch == ')')
				{
					iBracket--;
					
					if (iBracket == 0)
					{
						iPos = i;
						i = anz;
					}
				}
			}
			
			iEnd = iPos + 1;
			
			if (sCommandName.length() > 0)
			{
				sCommand = sStatement.substring(iStart, iEnd).trim();
			
				sClassName = sPackage + "." + Character.toUpperCase(sCommandName.charAt(0)) + sCommandName.substring(1) + "Command";
				
				try
				{
					Class<?> clazz = Class.forName(sClassName);
					
					cmd = (ICommand)clazz.newInstance();
					
					cmd.setCommand(sCommand);
					cmd.setPosition(iStart);
					
					liCommands.add(cmd);
				}
				catch (ClassNotFoundException cnfe)
				{
					//if we can't find a command handler, search in the parameters for commands
					iEnd = iBracketStart + 1;
					
					log.debug(cnfe);
				}
				catch (Exception ex)
				{
					log.debug(ex);
				}
			}
			else
			{
				iEnd = iBracketStart + 1; 
			}
		}
		
		List<Object> liParameters = new ArrayUtil<Object>();
		
		StringBuilder sbStatement = new StringBuilder(sStatement);
		
		for (int i = liCommands.size() - 1; i >= 0; i--)
		{
			cmd = liCommands.get(i);
			
			sbStatement.replace(cmd.getPosition(), cmd.getPosition() + cmd.getCommand().length(), "?");
			
			liParameters.add(0, cmd.getValue());
		}
		
		sStatement = sbStatement.toString().trim();
		
		//----------------------------------------------------------------------
		// User-defined parameter replacement
		//----------------------------------------------------------------------

		String sReturnParam = null;
		
		if (sStatement.startsWith("${"))
		{
			iPos = sStatement.indexOf("}");
			
			sReturnParam = sStatement.substring(0, iPos + 1).trim();
			
			//remove the parameter and equal sign from the statement
			
			iPos = sStatement.indexOf("=", iPos + 1);
			
			if (iPos < 0)
			{
				throw new RuntimeException("Can't assign a value to variable '" + sReturnParam + "' without '=' sign!");
			}
			
			for (int i = iPos + 1, anz = sStatement.length(); i < anz; i++)
			{
				ch = sStatement.charAt(i);
				
				if (!Character.isWhitespace(ch))
				{
					iPos = i;
					i = anz;
				}
			}
			
			sStatement = sStatement.substring(iPos);
			
			//only unset parameter 
			if ("null".equals(sStatement))
			{
				hmpParameters.remove(sReturnParam);
				
				return null;
			}
		}
		
		//replace parameters
		
		Object objValue;
		String sValue;
		
		for (Entry<String, Object> entry : hmpParameters.entrySet())
		{
			objValue = entry.getValue();
			
			if (objValue == null)
			{
				sValue = "null";
			}
			else if (objValue instanceof String)
			{
				sValue = "'" + objValue + "'";
			}
			else
			{
				sValue = objValue.toString();
			}
			
			sStatement = sStatement.replace(entry.getKey(), sValue);
		}
		
		//----------------------------------------------------------------------
		// Statement
		//----------------------------------------------------------------------

		DBStatement stmt = new DBStatement();
		stmt.statement       = sStatement;
		stmt.params          = liParameters;
		stmt.returnParameter = sReturnParam;
		
		return stmt;
	}
	
	/**
	 * Sets whether the complete script should be executed independent of errors.
	 * 
	 * @param pCancel <code>true</code> to ignore exceptions during execution, <code>false</code> to throw
	 *                exceptions immediate and stop execution
	 * @see #getExceptions()
	 */
	public void setCancelOnFailure(boolean pCancel)
	{
		bCancelOnFailure = pCancel;
	}
	
	/**
	 * Gets whether the script execution should be stopped if an error occurs.
	 * 
	 * @return <code>true</code> if execution is stopped after the first exception, <code>false</code> to
	 *         collect exceptions
	 * @see #getExceptions()
	 */
	public boolean isCancelOnFailure()
	{
		return bCancelOnFailure;
	}
	
	/**
	 * Gets the last occured exception(s).
	 * 
	 * @return the execution exception(s)
	 * @see #setCancelOnFailure(boolean)
	 */
	public Exception[] getExceptions()
	{
		return liExceptions.toArray(new Exception[liExceptions.size()]);
	}
	
	/**
	 * Sets a user-defined parameter.
	 * 
	 * @param pParameter the parameter name
	 * @param pValue the value
	 */
	public void setParameter(String pParameter, Object pValue)
	{
		hmpParameters.put("${" + pParameter + "}", pValue);
	}
	
	/**
	 * Gets the value of a user-defined parameter.
	 * 
	 * @param pParameter the parameter name
	 * @return the value. <code>null</code> is possible if the value is not set or the value itself is <code>null</code>.
	 * {@link #isParameterSet(String)}
	 */
	public Object getParameter(String pParameter)
	{
		return hmpParameters.get("${" + pParameter + "}");
	}
	
	/**
	 * Gets whether a parameter is set. This methods helps to find out if the value of a parameter is <code>null</code> or
	 * the parameter is not set.
	 * 
	 * @param pParameter the parameter name
	 * @return <code>true</code> if the parameter is set, <code>false</code> if the parameter is not available
	 */
	public boolean isParameterSet(String pParameter)
	{
		return hmpParameters.containsKey("${" + pParameter + "}");
	}
	
	/**
	 * Sets the file-encoding charset name.
	 * 
	 * @param pEncoding the charset name
	 */
	public void setFileEncoding(String pEncoding)
	{
		sEncoding = pEncoding;
	}
	
	/**
	 * Gets the file-encoding charset name.
	 * 
	 * @return the charset name or <code>null</code> if undefined (= system file encoding)
	 */
	public String getFileEncoding()
	{
		return sEncoding;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>DBStatement</code> is a simple POJO that stores the statement (with or without wildcards) and the 
	 * parameters, if available.
	 * 
	 * @author René Jahn
	 */
	protected static final class DBStatement
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the statement. */
		String statement;
		
		/** the list of parameters in the correct order. */
		List<Object> params;
		
		/** the name of the parameter which stores the execution result. */
		String returnParameter;
		
	}	// DBStatement
	
}	// DBImporter
