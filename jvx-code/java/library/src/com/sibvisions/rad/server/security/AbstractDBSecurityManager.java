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
 * 28.01.2014 - [JR] - creation
 * 15.03.2019 - [JR] - createDBAccess introduced
 */
package com.sibvisions.rad.server.security;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.rad.persist.DataSourceException;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBCredentials;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.Zone;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>AbstractDBSecurityManager</code> is the base class for all security managers that use a database for
 * authentication.
 * 
 * @author René Jahn
 */
public abstract class AbstractDBSecurityManager extends AbstractSecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the database credentials. */
	private DBCredentials credentials = null;
	
	/** database connection. */
	private Connection con = null;

	/** the last modified date of the configuration. */
	private long lConfigModified = -1;
	
	/** the list of registered statements. */
	private List<Statement> liStatements = null;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Updates relevant information after configuration was changed.
	 * 
	 * @param pConfig the session configuration
	 * @throws Exception if an exception occurs during statement creation
	 */
	protected abstract void updateConfiguration(IConfiguration pConfig) throws Exception;
	
	/**
	 * Initializes all statements after opening a database connection.
	 * 
	 * @param pConnection the connection to use
	 * @throws Exception if an exception occurs during statement creation
	 */
	protected abstract void initStatements(Connection pConnection) throws Exception;

	/**
	 * Gets the query which should be use for connection check. A simple query like
	 * <code>select 1 from dual</code> is enough.
	 * 
	 * @return the alive check query
	 */
	protected abstract String getAliveQuery();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void release()
	{
		try
		{
			closeConnection();
		}
		catch (Exception e)
		{
			error(e);
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable 
	{
	    CommonUtil.close(con);

		super.finalize();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Opens a database connection to the database of an application.
	 * 
	 * @param pApplicationName the name of the application
	 * @return the database connection 
	 * @throws Exception if the application zone is invalid or the connection can not be opened 
	 * @throws IllegalArgumentException if the database configuration is invalid (parameters are missing, ...)
	 */
	protected Connection openConnection(String pApplicationName) throws Exception
	{
		return openConnection(pApplicationName, Configuration.getApplicationZone(pApplicationName).getConfig());
	}		
	
	/**
	 * Opens a database connection to the database of an application.
	 * 
	 * @param pSession the session for which the connection should be opened
	 * @return a new or reused connection to the database
	 * @throws Exception if the application zone is invalid or the connection can not be opened 
	 * @throws IllegalArgumentException if the database configuration is invalid (parameters are missing, ...) 
	 */
	protected Connection openConnection(ISession pSession) throws Exception
	{
		return openConnection(pSession.getApplicationName(), pSession.getConfig());
	}
	
	/**
	 * Opens a database connection to the database of an application.
	 * 
	 * @param pApplicationName the name of the application
	 * @param pConfig the configuration
	 * @return the database connection 
	 * @throws Exception if the application zone is invalid or the connection can not be opened 
	 * @throws IllegalArgumentException if the database configuration is invalid (parameters are missing, ...)
	 */
	protected Connection openConnection(String pApplicationName, IConfiguration pConfig) throws Exception
	{
		DBCredentials dbcred = getCredentials(pConfig);
		
		
		if (dbcred == null)
		{
			throw new IllegalArgumentException("Database credentials were not found!"); 
		}
		
		//Validation
		
		if (dbcred.getUrl() == null)
		{
			throw new IllegalArgumentException("Parameter 'url' is missing for application '" + pApplicationName + "'");
		}

		if (DBAccess.isJdbc(dbcred.getUrl()))
		{
            if (dbcred.getUserName() == null)
            {
                throw new IllegalArgumentException("Parameter 'username' is missing for application '" + pApplicationName + "'");
            }

            if (dbcred.getPassword() == null)
            {
                throw new IllegalArgumentException("Parameter 'password' is missing for application '" + pApplicationName + "'");
            }

            if (dbcred.getDriver() == null)
    		{
    			DBAccess dba = DBAccess.getDBAccess(dbcred.getUrl());
    			
    			if (dba != null)
    			{
    			    //create a new instance with the detected driver, hopefully!
    				dbcred = new DBCredentials(dba.getDriver(), dbcred.getUrl(), dbcred.getUserName(), dbcred.getPassword());
    				
    				if (dbcred.getDriver() == null)
    				{
    					throw new IllegalArgumentException("Parameter 'driver' is missing for application '" + pApplicationName + "'");
    				}
    			}
    			else
    			{
    				throw new IllegalArgumentException("Parameter 'url' is missing for application '" + pApplicationName + "'");
    			}
    		}
		}		

		//Connection check
		boolean bOpenCon = false;

		long lModified;

        Zone zone = Configuration.getApplicationZone(pApplicationName); 
        
        if (zone != null && !zone.isVirtual())
        {
            lModified = zone.getFile().lastModified();
        }
        else
        {
            //always -1 to be safe
            lModified = -1;
        }
		
		if (con == null || !credentials.equals(dbcred) || lConfigModified != lModified)
		{
			//if settings were changed, the connection need to be re-established
			bOpenCon = true;
		}
		else
		{
			bOpenCon = !isConnectionAlive();
		}
		
		if (bOpenCon)
		{
			closeConnection();
			
			if (DBAccess.isJdbc(dbcred.getUrl()))
			{
    			try
    			{
    				Class.forName(dbcred.getDriver());
    			}
    			catch (ClassNotFoundException e)
    			{
    				throw new ClassNotFoundException("JDBC driver '" + dbcred.getDriver() + "' for application '" + pApplicationName + "' was not found!", e);
    			}
			}
			
			try
			{				
			    //Use DBAccess to use JVx default connection settings
				DBAccess dba = createDBAccess(dbcred);
				
				con = dba.getConnection();

				updateConfiguration(pConfig);
				
				initStatements(con);
			}
			catch (SQLException sqle)
			{
				closeConnection();
				
				throw new Exception("Can not open database connection with '" + dbcred.getUrl() + "' for application '" + pApplicationName + "'", sqle);
			}

			//Cache values for later validation
			credentials = dbcred;
			
			lConfigModified = lModified;
		}
		
		return con;
	}
	
	/**
	 * Close all statements and the connection.
	 * 
	 * @throws Exception if one statement can not be closed
	 */
	protected void closeConnection() throws Exception
	{
		if (con != null)
		{
		    closeStatements();
			
			con = CommonUtil.close(con);
		}
	}

	/**
	 * Close all registered statements.
	 */
	protected void closeStatements()
	{
        if (liStatements != null)
        {
            for (int i = 0, cnt = liStatements.size(); i < cnt; i++)
            {
                CommonUtil.close(liStatements.get(i));
            }
            
            liStatements = null;
        }
	}
	
	/**
	 * Closes and unregisters the given statements.
	 * 
	 * @param pStatement the statements to close and unregister
	 */
	protected void close(Statement... pStatement)
	{
	    CommonUtil.close((Object[])pStatement);
	    
        if (liStatements != null && pStatement != null)
        {
            for (int i = 0; i < pStatement.length; i++)
            {
                liStatements.remove(pStatement[i]);
            }
            
            if (liStatements.isEmpty())
            {
                liStatements = null;
            }
        }
	}
	
	/**
	 * Gets the configured database credentials for the given session.
	 * 
	 * @param pSession the session 
	 * @return the configured credentials
	 * @see #getCredentials(IConfiguration)
	 */
	protected DBCredentials getCredentials(ISession pSession)
	{
		return getCredentials(pSession.getConfig());
	}
	
	/**
	 * Gets the configured database credentials from a given configuration. This method handles
	 * credentials, set in the security manager and credentials configured as datasource.
	 * 
	 * @param pConfig the configuration 
	 * @return the configured credentials
	 */
	public static DBCredentials getCredentials(IConfiguration pConfig)
	{
		if (pConfig != null)
		{
			try
			{
				XmlNode xmnDb = pConfig.getNode("/application/securitymanager/database");
				String sEnvironment = getEnvironment(pConfig);
				
				if (xmnDb == null)
				{
					return DataSourceHandler.createDBCredentials(pConfig, "default", sEnvironment);
				}
				else
				{
					XmlNode xmnDs = xmnDb.getNode("/datasource"); 
				
					if (xmnDs == null)
					{
						return DataSourceHandler.createDBCredentials(xmnDb, sEnvironment);
					}
					else
					{
						return DataSourceHandler.createDBCredentials(pConfig, xmnDs.getValue(), sEnvironment);
					}
				}
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(AbstractDBSecurityManager.class).error(e);
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the current connection to the database. The connection is validated to ensure that it is usable.
	 * 
	 * @return the connection for the security manager or <code>null</code> if the security manager did
	 *         not open a connection
	 * @throws Exception if db access fails
	 */
	public Connection getConnection() throws Exception
	{
		if (con != null)
		{
			if (!isConnectionAlive())
			{
			    closeConnection();
			    
				//re-open the connection with cached credentials 
				try
				{			
					DBAccess dba = createDBAccess(credentials);

                    con = dba.getConnection();
					
					initStatements(con);
				}
				catch (SQLException sqle)
				{
					closeConnection();
					
					throw new Exception("Can not open database connection with '" + credentials.getUrl() + "'", sqle);
				}
			}
		}
		
		return con;
	}
	
	/**
	 * Gets the connection to the database. The connection is validated to ensure that it is usable.
	 * 
	 * @param pSession the session that wants access to the database
	 * @return the connection
	 * @throws Exception if db access fails
	 */
	public Connection getConnection(ISession pSession) throws Exception
	{
		return openConnection(pSession);
	}	

	/**
	 * Creates an access controller for a {@link ISession}.
	 * 
	 * @param pSession the session which requests the access controller
	 * @return the access controller
	 */
	protected IAccessController createAccessController(ISession pSession)
	{
		String sAccCtrl = pSession.getConfig().getProperty("/application/securitymanager/accesscontroller");
		
		if (sAccCtrl != null && sAccCtrl.trim().length() > 0)
		{
			try
			{
				Class<?> clazz = Class.forName(sAccCtrl);
				
				return (IAccessController)clazz.newInstance();
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new SecurityException("Access controller '" + sAccCtrl + "' was not found!");
			}
			catch (InstantiationException ie)
			{
				throw new SecurityException("Can't instantiate access controller '" + sAccCtrl + "'!");
			}
			catch (IllegalAccessException iae)
			{
				throw new SecurityException("Access controller '" + sAccCtrl + "' not accessible!");
			}
		}
		
		return new DefaultAccessController();
	}
	
	/**
	 * Checks whether the connection is still alive, means whether the connection can be used.
	 * 
	 * @return <code>true</code> if the connection is alive/valid
	 * @see #getAliveQuery()
	 */
	protected boolean isConnectionAlive()
	{
		String sQuery = getAliveQuery();
		
		//No query means no alive check and we assume that the connection is still alive
		if (sQuery == null)
		{
			return true;
		}
		
		Statement stmt = null;

		ResultSet res = null;
		
		try
		{
			stmt = con.createStatement();
			
			res = stmt.executeQuery(sQuery);
			res.next();
			
			return true;
		}
		catch (Throwable th)
		{
			return false;
		}
		finally
		{			
			stmt = CommonUtil.close(res, stmt);
		}
	}
	
	/**
	 * Registers a statment as closable statement. All closable statements will be closed
	 * if connection will be closed.
	 * 
	 * @param pStatement the statement
	 */
	protected void register(Statement pStatement)
	{
		if (liStatements == null)
		{
			liStatements = new ArrayUtil<Statement>();
		}
		
		if (!liStatements.contains(pStatement))
		{
			liStatements.add(pStatement);
		}
	}
	
	/**
	 * Unregisters a statement.
	 * 
	 * @param pStatement the statement
	 * @return <code>true</code> if unregistration was successful, <code>false</code> if statement was not registered
	 *         as closable
	 * @see #register(Statement)         
	 */
	protected boolean unregister(Statement pStatement)
	{
		if (liStatements == null)
		{
			return false;
		}
		
		return liStatements.remove(pStatement);
	}
	
	/**
	 * Creates a new instance of {@link PreparedStatement}. The statement will be registered for automatic close.
	 * 
	 * @param pConnection the database connection
	 * @param pSql the SQL statement, e.g. a query
	 * @return the new statement
	 * @throws SQLException if statement creation fails
	 */
	protected PreparedStatement prepareStatement(Connection pConnection, String pSql) throws SQLException
	{
		PreparedStatement stmt = pConnection.prepareStatement(pSql);
		
		register(stmt);
		
		return stmt;
	}
	
    /**
     * Creates a new instance of {@link CallableStatement}. The statement will be registered for automatic close.
     * 
     * @param pConnection the database connection
     * @param pSql the call statement
     * @return the new statement
     * @throws SQLException if statement creation fails
     */
	protected CallableStatement prepareCall(Connection pConnection, String pSql) throws SQLException
	{
	    CallableStatement stmt = pConnection.prepareCall(pSql);
	    
	    register(stmt);
	    
	    return stmt;
	}
	
	/**
	 * Creates and opens a new instance of {@link DBAccess} for the given credentials.
	 * 
	 * @param pCredentials the credentials
	 * @return the {@link DBAccess} instance
	 * @throws DataSourceException if creating or opening the instance failed
	 */
	protected DBAccess createDBAccess(DBCredentials pCredentials) throws DataSourceException
	{
		DBAccess dba = DBAccess.getDBAccess(pCredentials);
		
        if (dba == null)
        {
            throw new RuntimeException("Database configuration was not found!");
        }

        dba.open();
        
        return dba;
	}
	
}	// AbstractDBSecurityManager
