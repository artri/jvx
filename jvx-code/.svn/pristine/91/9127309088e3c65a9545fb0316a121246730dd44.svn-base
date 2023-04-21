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
 * 01.10.2008 - [JR] - creation
 * 05.10.2008 - [JR] - changePassword implemented
 * 28.09.2009 - [JR] - ISession instead of AbstractSession
 * 04.10.2009 - [JR] - changePassword: old password is required
 * 07.10.2009 - [JR] - AutoLogin support
 * 14.10.2009 - [JR] - prepared statements cleanup
 *                   - added missing commit calls
 *                   - added missing rollback calls
 *                   - openConnection: setAutoCommit(false)
 * 21.10.2009 - [JR] - "KEY" renamed to "LOGINKEY" for table AUTOLOGIN (reserved keyword in derby db)
 * 23.10.2009 - [HM] - openConnection: used DBAccess to check the driver name
 *            - [JR] - openConnection: exception handling (NullPointer) 
 * 14.11.2009 - [JR] - #7
 *                     changePassword: validatePassword called 
 * 06.06.2010 - [JR] - changePassword: don't check password during update (paranoid)
 *                   - #132: encryption support     
 * 20.06.2010 - [JR] - #137: initStatements() implemented to cache prepared statements  
 * 23.10.2010 - [JR] - AutoLogin is not required
 * 01.12.2010 - [JR] - #219: used DBCredentials and supported datasource      
 * 11.02.2011 - [JR] - openConnection: fixed private member access through derived classes
 * 06.05.2011 - [JR] - #344: custom access controller support  
 * 11.05.2011 - [JR] - release implemented       
 * 18.06.2011 - [JR] - close AutoLogin statements when an exception occurs during statement creation
 * 31.07.2011 - [JR] - #446: getConnection implemented and table/column names cached
 * 22.09.2011 - [JR] - #475: removeAccess implemented  
 * 22.11.2011 - [JR] - #515: use DBAccess to create the connection (takes all optimizations e.g. transaction isolation level)
 * 07.12.2011 - [JR] - renamed initStatementsIntern to initStatements and made it protected
 * 16.02.2013 - [JR] - #634: initStatements: removed alias of delete statements 
 * 26.02.2013 - [JR] - #642: getCredentials(ISession) implemented for derived classes
 * 08.09.2013 - [JR] - #788: check environment in getAccessController        
 * 30.01.2014 - [JR] - extends AbstractDBSecurityManager
 * 12.06.2014 - [JR] - removed table alias from AUTOLOGIN  
 * 19.09.2014 - [JR] - #1115: moved DBAccessController to DefaultAccessController      
 * 21.04.2015 - [JR] - postAuthentication implemented
 * 02.02.2018 - [JR] - #1887: removed alias in set group
 */
package com.sibvisions.rad.server.security;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.rad.application.ILauncher;
import javax.rad.remote.ChangePasswordException;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ExpiredException;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.InactiveException;
import javax.rad.server.InvalidPasswordException;
import javax.rad.server.NotFoundException;

import com.sibvisions.rad.server.config.DBObjects;
import com.sibvisions.rad.server.security.reset.IResetPasswordManager;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DBSecurityManager</code> uses a database to validate/authenticate users. 
 * It requires the following information to establish a database connection:
 * <ul>
 *   <li>driver (jdbc driver classname)</li>
 *   <li>url (jdbc connect url)</li>
 *   <li>username (database username)</li>
 *   <li>database (database password)</li>
 * </ul>
 * 
 * To use automatic login the session property:
 * <code>IConnectionConstants.PREFIX_CLIENT + "login.auto"</code> should be set to <code>true</code> when the user logs on.
 * After a successful logon the property: <code>IConnectionConstants.PREFIX_CLIENT + "login.key"</code> will be set to
 * a unique login key. The client should store the key in its local registry. When the property 
 * <code>IConnectionConstants.PREFIX_CLIENT + "login.key"</code> is set before opening the connection, then the user will be logged in
 * if the login is possible! 
 * 
 * @author René Jahn
 */
public class DBSecurityManager extends AbstractDBSecurityManager
                               implements IResetPasswordManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the name of the users table. */
	protected static final String TABLE_USERS = "USERS";
	
	/** the name of the autologin table. */
	protected static final String TABLE_AUTOLOGIN = "AUTOLOGIN";
	
	/** the name of the accessrules table. */
	protected static final String VIEW_ACCESSRULES = "V_ACCESSRULES";


	/** the check autologin statement. */
	private PreparedStatement psAutoLogin;
	
	/** the insert autologin statement. */
	private PreparedStatement psInsertAutoLogin;

	/** the delete autologin statement with key. */
	private PreparedStatement psDeleteAutoLoginKey;
	
	/** the delete autologin statement with username. */
	private PreparedStatement psDeleteAutoLoginUser;

	/** the user query by id. */
	private PreparedStatement psUserId;
	
	/** the user query by username. */
	private PreparedStatement psUserName;
	
	/** the accessrule query statement. */
	private PreparedStatement psAccessRule;
	
	/** the change password statement. */
	private PreparedStatement psChangePwd;
	
	/** the change password statement which unsets change password flag. */
	private PreparedStatement psChangePwdUnset;
	
	/** the user query by email. */
	private PreparedStatement psEmail;
	
	/** the name of the users table. */
	private String sUsersTable;
	/** the ID column name of the users table. */
	private String sUsersId;   
	/** the USERNAME column name of the users table. */
	private String sUsersName;
	/** the CHANGE_PASSWORD column name of the users table. */
	private String sUsersChgPwd;
	/** the PASSWORD column name of the users table. */
	private String sUsersPwd;
	/** the EMAIL column name of the users table. */
	private String sUsersEmail;   
	
	/** the name of the autologin table. */
	private String sAutoLoginTable;
	/** the USER_ID column name of the autologin table. */
	private String sAutoLoginId;
	/** the LOGINKEY column name of the autologin table. */
	private String sAutoLoginKey;

	/** the name of the accessrules table. */
	private String sAccessTable;
	/** the USERNAME column name of the accessrules table. */
	private String sAccessUser;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public synchronized void validateAuthentication(ISession pSession) throws Exception
	{
		String sApplication = pSession.getApplicationName();
		String sUserName = pSession.getUserName();
		
		IConfiguration cfgSession = pSession.getConfig();

		ResultSet resUser = null;
		
		boolean bAutoLogin = false;
		boolean bHideFailureReason = isHideFailureReason(cfgSession); 

		Connection conn = openConnection(pSession);

		preAuthentication(pSession);
		
		try
		{
			//Check if the user will be connected through automatic login key
			String sAutoKey = (String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "login.key");
			
			if (psAutoLogin != null)
			{
				if (sAutoKey != null)
				{
					ResultSet res = null;
					
					try
					{
						//check if the authentication key is present!
						psAutoLogin.clearParameters();
						psAutoLogin.setString(1, sAutoKey);
						
						res = psAutoLogin.executeQuery();
						
						if (res.next())
						{
							//get the user information for validation!
							psUserId.clearParameters();
							psUserId.setBigDecimal(1, res.getBigDecimal(1));
							
							resUser = psUserId.executeQuery();
							
							bAutoLogin = true;
						}
					}
					catch (Exception e)
					{
						debug(e);
					}
					finally
					{
					    res = CommonUtil.close(res);
					}
				}
			}
			
			if (resUser == null)
			{
				psUserName.clearParameters();
				psUserName.setString(1, sUserName);
				
				resUser = psUserName.executeQuery();
			}
								
			validateUser(pSession, resUser);

			if (!bAutoLogin)
			{
				if (sAutoKey != null || !isOTPAuthentication(pSession))
				{
					//Password check
					String sPassword;
					try
					{
						sPassword = resUser.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "PASSWORD"));
					}
					catch (SQLException sqle)
					{
						sPassword = null;
					}
					
					if (!isPasswordValid(pSession, sPassword))
					{
						if (bHideFailureReason)
						{
							debug("Invalid password for '", CommonUtil.nvl(sUserName, "<undefined>"), "' and application '", sApplication, "'");
							
							throw new SecurityException("Invalid username or password");
						}
						
						throw new InvalidPasswordException("Invalid password for '" + CommonUtil.nvl(sUserName, "<undefined>") + "' and application '" + sApplication + "'");
					}
				}
			}

			//Change Password check
			String sChangePwd;
			try
			{
				sChangePwd = resUser.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "CHANGE_PASSWORD"));
			}
			catch (SQLException sqle)
			{
				sChangePwd = null;
			}
			
			if (isChangePassword(pSession, sChangePwd))
			{
				//this is necessary for e.g. MFA handling
				preValidatePassword(pSession);

				setUserInfo(pSession, resUser, sUserName);
				
				//change password immediately
				throw new ChangePasswordException("Please change your password");
			}
			
			boolean bAutoLoginEnabled = Boolean.valueOf((String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "login.auto")).booleanValue();
			
            BigDecimal bdUserId = resUser.getBigDecimal(DBObjects.getColumnName(cfgSession, TABLE_USERS, "ID"));
			
			//check if the user allows automatic login with login key
			if (sAutoKey == null && bAutoLoginEnabled
				&& psAutoLogin != null && psDeleteAutoLoginUser != null && psInsertAutoLogin != null)
			{
				sAutoKey = UUID.randomUUID().toString();
				
				boolean isAutoCommit = conn.getAutoCommit();
				
				try
				{
					if (isAutoCommit)
					{
						conn.setAutoCommit(false);
					}
					
					//cleanup old autologin keys
					psDeleteAutoLoginUser.clearParameters();
					psDeleteAutoLoginUser.setBigDecimal(1, bdUserId);
					
					if (psDeleteAutoLoginUser.execute())
					{
					    CommonUtil.close(psDeleteAutoLoginUser.getResultSet());
					}
					
					//insert new autologin key
					psInsertAutoLogin.clearParameters();
					psInsertAutoLogin.setBigDecimal(1, bdUserId);
					psInsertAutoLogin.setString(2, sAutoKey);
					
					if (psInsertAutoLogin.execute())
					{
					    CommonUtil.close(psInsertAutoLogin.getResultSet());
					}
					
					pSession.setProperty(IConnectionConstants.PREFIX_CLIENT + "login.key", sAutoKey);
					
					conn.commit();
				}
				catch (Exception e)
				{
					conn.rollback();
					error(e);
				}
				finally
				{
					if (isAutoCommit)
					{
						conn.setAutoCommit(true);
					}
				}
			}
			
			String sSecEnv = getEnvironment(cfgSession);
			
			if (!StringUtil.isEmpty(sSecEnv))
			{
				pSession.getProperties().put(IConnectionConstants.SECURITY_ENVIRONMENT, sSecEnv);
			}

			if (bAutoLogin)
			{
				//set the username if autologin
				try
				{
					pSession.getProperties().put(IConnectionConstants.USERNAME, resUser.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "USERNAME")));
				}
				catch (SQLException sqle)
				{
					throw new SecurityException("USERNAME column for application '" + sApplication + "' was not found!");
				}
			}
			else if (!bAutoLoginEnabled)
			{
				//unset the key
				pSession.setProperty(IConnectionConstants.PREFIX_CLIENT + "login.key", null);
			}
			
			preValidatePassword(pSession);
			
			setUserInfo(pSession, resUser, sUserName);
			
            postAuthentication(pSession, bdUserId);
		}
		catch (SQLException sqle)
		{
			error(sqle);
			
			if (bHideFailureReason)
			{
				debug("Authentication for user '", CommonUtil.nvl(sUserName, "<undefined>"), "' and application '", sApplication, "' is not possible");
				
				throw new SecurityException("Authentication is not possible");
			}
			
			throw new SecurityException("Authentication for user '" + CommonUtil.nvl(sUserName, "<undefined>") + "' and application '" + sApplication + "' is not possible");
		}
		finally
		{
		    resUser = CommonUtil.close(resUser);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void changePassword(ISession pSession) throws Exception
	{
		String sOldPwd = (String)pSession.getProperty(IConnectionConstants.OLDPASSWORD);
		String sNewPwd = (String)pSession.getProperty(IConnectionConstants.NEWPASSWORD);
		
		//-------------------------------------------------
		// Password validation
		//-------------------------------------------------
		
		validatePassword(pSession, sOldPwd, sNewPwd);
		
		//-------------------------------------------------
		// Change password
		//-------------------------------------------------

		String sApplication = pSession.getApplicationName();
		String sUserName = pSession.getUserName();
		
		IConfiguration cfgSession = pSession.getConfig();

		
		openConnection(pSession);
		
		ResultSet resUser = null;		
		
		try
		{
			psUserName.clearParameters();
			psUserName.setString(1, sUserName);
			
			resUser = psUserName.executeQuery();
			
			validateUser(pSession, resUser);
			
			boolean bHideFailureReason = isHideFailureReason(cfgSession);

			boolean bOTP = isOTPAuthentication(pSession);
			
			String sPassword;

			if (!bOTP)
			{
				//Old Password check
				try
				{
					sPassword = resUser.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "PASSWORD"));
				}
				catch (SQLException sqle)
				{
					sPassword = null;
				}
			}
			else
			{
				sPassword = null;
			}
			
			String sAutoKey = (String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "login.key");
			
			//check current session password and the database password (double check)
			//autologin: no password available -> don't check the session password!
			if (((sAutoKey != null && pSession.getPassword() == null)
					 //no need for encryption because both passwords are plain text!
				  || comparePassword(cfgSession, pSession.getPassword(), sOldPwd))
				    //changed order because of encrypted "sPassword" (maybe)
				&& (comparePassword(cfgSession, sOldPwd, sPassword) 
					|| (sAutoKey == null && bOTP)))
			{
				//check change_password column and disable the check, if the column doesn't exist
				boolean bChangePwd;
				try
				{
					resUser.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "CHANGE_PASSWORD"));
					bChangePwd = true;
				}
				catch (Throwable the)
				{
					//missing field!
					bChangePwd = false;
				}

				PreparedStatement ps;
				
				if (bChangePwd)
				{
					ps = psChangePwdUnset;
				}
				else
				{
					ps = psChangePwd;
				}
				
				ps.clearParameters();
				ps.setString(1, encryptPassword(cfgSession, sNewPwd));
				ps.setString(2, sUserName);

				if (ps.execute())
				{
				    CommonUtil.close(ps.getResultSet());
				}
				
				if (ps.getUpdateCount() != 1)
				{
					if (bHideFailureReason)
					{
						debug("User '", CommonUtil.nvl(sUserName, "<undefined>"), "' was not found for application '", sApplication, "'");
						
						throw new SecurityException("Invalid username or password");
					}

					throw new NotFoundException("User '" + CommonUtil.nvl(sUserName, "<undefined>") + "' was not found for application '" + sApplication + "'");
				}
			}
			else
			{
				if (bHideFailureReason)
				{
					debug("Invalid password for '", CommonUtil.nvl(sUserName, "<undefined>"), "' and application '", sApplication, "'");
					
					throw new SecurityException("Invalid username or password");
				}

				throw new InvalidPasswordException("Invalid password for '" + CommonUtil.nvl(sUserName, "<undefined>") + "' and application '" + sApplication + "'");
			}
			
			//not valid anymore
			resetAutoLogin(pSession);
		}
		catch (SQLException sqle)
		{
			debug(sqle);

			throw new SecurityException("Error while changing password of '" + CommonUtil.nvl(sUserName, "<undefined>") + "' for application '" + sApplication + "'");
		}
		finally
		{
		    resUser = CommonUtil.close(resUser);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void logout(ISession pSession)
	{
		//don't use setProperty, because we won't change the access time
		if (Boolean.valueOf((String)pSession.getProperties().get("userlogout")).booleanValue())
		{		
			try
			{
				resetAutoLogin(pSession);
			}
			catch (Exception e)
			{
				error(e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized IAccessController getAccessController(ISession pSession) throws Exception
	{
		ResultSet res = null;
		
		openConnection(pSession);
		
		//NO access rules available -> no controller!
		if (psAccessRule == null)
		{
			return null;
		}
		
		try
		{
			psAccessRule.clearParameters();
			psAccessRule.setObject(1, pSession.getUserName());

			res = psAccessRule.executeQuery();

			IAccessController accessControl = createAccessController(pSession);

			configureAccessController(getSimpleEnvironmentName(pSession), pSession.getConfig(), accessControl, res);
			
			return accessControl;
		}
		catch (SQLException sqle)
		{
			debug(sqle);
			
			//no access control!
			return null;
		}
		finally
		{
		    res = CommonUtil.close(res);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void resetPassword(ISession pSession) throws Exception
	{
		IConfiguration cfg = pSession.getConfig();
		
		boolean bHideFailureReason = isHideFailureReason(cfg);
		
		//init everything
		openConnection(pSession.getApplicationName());

		//username could be an email or the username
		String sIdentifier = pSession.getUserName();
		
		BigDecimal bdUserId = null;
		
		String sUserName = null;
		String sEmail = null;

		
		//search by name
		
		psUserName.clearParameters();
		psUserName.setString(1, sIdentifier);
		
		UserInfo user = new UserInfo();
		
		ResultSet res = psUserName.executeQuery();
		
		try
		{
			if (res.next())
			{
				bdUserId = res.getBigDecimal(sUsersId);
				sEmail = res.getString(sUsersEmail);
				sUserName = res.getString(sUsersName);
				
				updateUserInfo(user, res);
			}
		}
		finally
		{
			CommonUtil.close(res);
		}
		
		if (psUserName.execute())
		{
		    CommonUtil.close(psUserName.getResultSet());
		}

		if (bdUserId == null)
		{
			//search by email address
			
			psEmail.clearParameters();
			psEmail.setString(1, sIdentifier);
			
			res = psEmail.executeQuery();
			
			try
			{
				while (res.next())
				{
					if (bdUserId != null)
					{
						if (bHideFailureReason)
						{
							debug("E-Mail address '" + sIdentifier + "' is not unique for application '" + pSession.getApplicationName() + "'");
							
							//don't throw because it's possible to find out if a user exists
							//throw new SecurityException("Invalid email address");
							
							return;
						}

						throw new SecurityException("E-Mail address '" + sIdentifier + "' is not unique for application '" + pSession.getApplicationName() + "'");
					}
					
					bdUserId = res.getBigDecimal(sUsersId);
					sUserName = res.getString(sUsersName);
					sEmail = sIdentifier;
					
					updateUserInfo(user, res);
				}
			}
			finally
			{
				CommonUtil.close(res);
			}
			
			if (psEmail.execute())
			{
			    CommonUtil.close(psDeleteAutoLoginKey.getResultSet());
			}
		}
		
		if (bdUserId == null)
		{
			if (bHideFailureReason)
			{
				debug("User '" + sIdentifier + "' was not found for application '" + pSession.getApplicationName() + "'");
				
				//don't throw because it's possible to find out if a user exists
				//throw new SecurityException("Invalid username");
				
				return;
				
			}
			
			throw new NotFoundException("User '" + sIdentifier + "' was not found for application '" + pSession.getApplicationName() + "'");
		}
		
		user.setUserName(sUserName);
		user.setEmailAddress(sEmail);

		createOTP(pSession, user);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateConfiguration(IConfiguration pConfig) throws Exception
	{
		sUsersTable  = DBObjects.getTableName(pConfig, TABLE_USERS);
		sUsersId     = DBObjects.getColumnName(pConfig, TABLE_USERS, "ID");   
		sUsersName   = DBObjects.getColumnName(pConfig, TABLE_USERS, "USERNAME");
		sUsersChgPwd = DBObjects.getColumnName(pConfig, TABLE_USERS, "CHANGE_PASSWORD");
		sUsersPwd    = DBObjects.getColumnName(pConfig, TABLE_USERS, "PASSWORD");
		sUsersEmail  = DBObjects.getColumnName(pConfig, TABLE_USERS, "EMAIL");
		
		sAutoLoginTable = DBObjects.getTableName(pConfig, TABLE_AUTOLOGIN);
		sAutoLoginId    = DBObjects.getColumnName(pConfig, TABLE_AUTOLOGIN, "USER_ID");
		sAutoLoginKey   = DBObjects.getColumnName(pConfig, TABLE_AUTOLOGIN, "LOGINKEY");

		sAccessTable = DBObjects.getTableName(pConfig, VIEW_ACCESSRULES);
		sAccessUser  = DBObjects.getColumnName(pConfig, VIEW_ACCESSRULES, "USERNAME");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void closeStatements()
	{
	    super.closeStatements();
	    
	    psUserId = null;
	    psUserName = null;
	    psChangePwd = null;
	    psChangePwdUnset = null;
        psEmail = null;

	    psAutoLogin = null;	   
        psInsertAutoLogin = null;
        psDeleteAutoLoginKey = null;
        psDeleteAutoLoginUser = null;
        
        psAccessRule = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initStatements(Connection pConnection) throws Exception
	{
		// USER
		
		psUserId = prepareStatement(pConnection, "select * from " + sUsersTable + " u where u." + sUsersId + " = ?");
		
		psUserName = prepareStatement(pConnection, "select * from " + sUsersTable + " u where u." + sUsersName + " = ?");
		
		psChangePwd = prepareStatement(pConnection, "update " + sUsersTable + " set " + sUsersPwd + " = ? " + " where " + sUsersName + " = ?");
		
		psChangePwdUnset = prepareStatement(pConnection, "update " + sUsersTable + 
						    	                         " set " + sUsersPwd + " = ?, " +
							                                     sUsersChgPwd + " = 'N' " +
							                             " where " + sUsersName + " = ?");
		
		psEmail = prepareStatement(pConnection, "select * from " + sUsersTable + " u where lower(u." + sUsersEmail + ") = lower(?)");
		
		// AUTOLOGIN

		try
		{
			psAutoLogin = prepareStatement(pConnection, "select " + sAutoLoginId + " from " + sAutoLoginTable + " where " + sAutoLoginKey + " = ?");
			
			psInsertAutoLogin = prepareStatement(pConnection, "insert into " + sAutoLoginTable + "(" + sAutoLoginId + ", " + 
			                                                                                      sAutoLoginKey + ") values (?, ?)");
	
			//don't use alias' in delete statements (Mysql doesn't like it, e.g. http://dev.mysql.com/doc/refman/5.5/en/delete.html)
			//correct syntax for mysql: delete a1 from table a1 where a1.key = value
			psDeleteAutoLoginKey = prepareStatement(pConnection, "delete from " + sAutoLoginTable + " where " + sAutoLoginKey + " = ?");
			
			psDeleteAutoLoginUser = prepareStatement(pConnection, "delete from " + sAutoLoginTable + " where " + sAutoLoginId + " = ?");
		}
		catch (SQLException sqle)
		{
		    close(psAutoLogin, psInsertAutoLogin, psDeleteAutoLoginKey, psDeleteAutoLoginUser);
		    
		    psAutoLogin = null;
		    psInsertAutoLogin = null;
		    psDeleteAutoLoginKey = null;
		    psDeleteAutoLoginUser = null;
		}
		
		// ACCESS
		
		try
		{
			psAccessRule = prepareStatement(pConnection, "select * from " + sAccessTable + " where " + sAccessUser + " = ?");
		}
		catch (SQLException sqle)
		{
			//nothing to be done
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getAliveQuery()
	{
		return "select ID from " + sUsersTable;		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if a user is allowed to authenticate. That means, if the user is active and the login
	 * is valid for the current date. 
	 * 
	 * @param pSession the session which needs access
	 * @param pUserInfo the user information from the database
	 * @throws Exception if the user is not allowed to authenticate
	 */
	private void validateUser(ISession pSession, ResultSet pUserInfo) throws Exception
	{
		String sApplication = pSession.getApplicationName();
		String sUserName = pSession.getUserName();

		IConfiguration cfgSession = pSession.getConfig();

		boolean bHideFailureReason = isHideFailureReason(cfgSession);
		
		if (pUserInfo.next())
		{
			String sActive;
			try
			{
				sActive = pUserInfo.getString(DBObjects.getColumnName(cfgSession, TABLE_USERS, "ACTIVE"));
			}
			catch (SQLException sqle)
			{
				sActive = null;
			}
			
			if (isActive(pSession, sActive))
			{
				Timestamp tsmpFrom;
				try
				{
					tsmpFrom = pUserInfo.getTimestamp(DBObjects.getColumnName(cfgSession, TABLE_USERS, "VALID_FROM"));
				}
				catch (SQLException sqle)
				{
					tsmpFrom = null;
				}
				
				Timestamp tsmpTo;
				try
				{
					tsmpTo = pUserInfo.getTimestamp(DBObjects.getColumnName(cfgSession, TABLE_USERS, "VALID_TO"));
				}
				catch (SQLException sqle)
				{
					tsmpTo = null;
				}
				
				if (!isValid(pSession, tsmpFrom, tsmpTo))
				{
					if (bHideFailureReason)
					{
						debug("User '", CommonUtil.nvl(sUserName, "<undefined>"), "' is expired for application '", sApplication, "'");
						
						throw new SecurityException("User is expired");
					}

					throw new ExpiredException("User '" + CommonUtil.nvl(sUserName, "<undefined>") + "' is expired for application '" + sApplication + "'");
				}
			}
			else
			{
				if (bHideFailureReason)
				{
					debug("User '", CommonUtil.nvl(sUserName, "<undefined>"), "' is inactive for application '", sApplication, "'");
					
					throw new SecurityException("User is inactive");
				}

				throw new InactiveException("User '" + CommonUtil.nvl(sUserName, "<undefined>") + "' is inactive for application '" + sApplication + "'");
			}
		}
		else
		{
			if (bHideFailureReason)
			{
				debug("User '", CommonUtil.nvl(sUserName, "<undefined>"), "' was not found for application '", sApplication, "'");
				
				throw new SecurityException("Invalid username or password");
			}
			
			throw new NotFoundException("User '" + CommonUtil.nvl(sUserName, "<undefined>") + "' was not found for application '" + sApplication + "'");
		}
	}
	
	/**
	 * Checks if a user is active.
	 * 
	 * @param pSession the session which needs access
	 * @param pActive the active flag or <code>null</code> if the flag is not available
	 * @return <code>true</code> if the active flag is missing or the flag equals the yes value
	 * @throws Exception if the configuration of the session is invalid
	 */
	protected boolean isActive(ISession pSession, String pActive) throws Exception
	{
		//not configured -> ignore
		if (pActive == null)
		{
			return true;
		}
		
		return DBObjects.getYesValue(pSession.getConfig()).equals(pActive);
	}
	
	/**
	 * Checks if a user is valid.
	 * 
	 * @param pSession the session which needs access
	 * @param pFrom the from date/time or <code>null</code> for undefined
	 * @param pTo the to date/time or <code>null</code> for undefined
	 * @return <code>true</code> if the from/to combination is possible, <code>false</code> otherwise
	 */
	protected boolean isValid(ISession pSession, Timestamp pFrom, Timestamp pTo)
	{
		long lNow = System.currentTimeMillis();
		
		return ((pFrom == null || pFrom.getTime() <= lNow)  
			    && (pTo == null || pTo.getTime() > lNow));
	}
	
	/**
	 * Checks if the user password is valid.
	 * 
	 * @param pSession the session which needs access
	 * @param pPassword the confirmation password (encrypted or plain text)
	 * @return <code>true</code> if the user password is valid
	 * @throws Exception if the password validation failed (e.g. encryption problems)
	 */
	protected boolean isPasswordValid(ISession pSession, String pPassword) throws Exception
	{
		return comparePassword(pSession.getConfig(), pSession.getPassword(), pPassword);
	}
	
	/**
	 * Checks if the change password flag is set.
	 * 
	 * @param pSession the session which needs access 
	 * @param pChangePassword the change password flag or <code>null</code> if the flag is not available
	 * @return <code>true</code> if the change password flag is set or <code>false</code> if the flag is
	 *         <code>null</code> or is not set
	 * @throws Exception if the configuration of the session is invalid
	 */
	protected boolean isChangePassword(ISession pSession, String pChangePassword) throws Exception
	{
		//not configured -> ignore
		if (pChangePassword == null)
		{
			return false;
		}
		
		return DBObjects.getYesValue(pSession.getConfig()).equals(pChangePassword);
	}
	
    /**
     * Allows additional checks before user will be validated. The database connection is open
     * if this method is called.
     * 
     * @param pSession the session
     * @throws Exception if a pre authentication error occurs
     * @throws SecurityException if authentication should fail
     */
    protected void preAuthentication(ISession pSession) throws Exception
    {
    }
	
	/**
	 * Allows additional checks after user was validated.
	 * 
	 * @param pSession the session
	 * @param pUserId the user id
	 * @throws Exception if a post authentication error occurs
	 * @throws SecurityException if authentication should fail
	 */
	protected void postAuthentication(ISession pSession, BigDecimal pUserId) throws Exception
	{
	}
	
    /**
     * Gets the name of the environment from the given session, without additional information. Only
     * the name part is important, e.g. NAME:PLATFORM -&gt; NAME will be returned.
     * 
     * @param pSession the session
     * @return the environment name without additional information
     */
    public static String getSimpleEnvironmentName(ISession pSession)
    {
        String sName = (String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT);
        
        if (!StringUtil.isEmpty(sName))
        {
            int iPos = sName.indexOf(":");
            
            if (iPos >= 0)
            {
                sName = sName.substring(0, iPos);
            }
        }
        
        return sName;
    }
    
    /**
     * Deletes the autologin information.
     * 
     * @param pSession the accessing session
     * @throws Exception if resetting autologin fails
     */
    protected void resetAutoLogin(ISession pSession) throws Exception
    {
		String sAutoKey = (String)pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "login.key");
		
		if (sAutoKey != null)
		{
			openConnection(pSession);

			psDeleteAutoLoginKey.clearParameters();
			psDeleteAutoLoginKey.setString(1, sAutoKey);
			if (psDeleteAutoLoginKey.execute())
			{
			    CommonUtil.close(psDeleteAutoLoginKey.getResultSet());
			}
			
			//unset the key
			pSession.setProperty(IConnectionConstants.PREFIX_CLIENT + "login.key", null);
		}
    }
    
	/**
	 * Configures the given access controller. All allowed lifecycle classes will be added.
	 * 
	 * @param pEnvironment the simple environment name
	 * @param pConfig the current session config
	 * @param pController the access controller
	 * @param pAccessRules the list of lifecycle classes
	 * @throws Exception if configuration or reading classes fails
	 */
	public static void configureAccessController(String pEnvironment, IConfiguration pConfig, IAccessController pController, ResultSet pAccessRules) throws Exception
	{
		boolean bValidScreen;
		
		String sYesValue = DBObjects.getYesValue(pConfig);

		String sLifeCycleName = DBObjects.getColumnName(pConfig, VIEW_ACCESSRULES, "LIFECYCLENAME");
		String sEnvDesktop = null;
		String sEnvWeb = null;
		String sEnvMobile = null;
		
		String sColName;
		
		boolean bUseEnv = false;
		
		if (pEnvironment != null)
		{
			sEnvDesktop = DBObjects.getColumnName(pConfig, VIEW_ACCESSRULES, "ENV_DESKTOP");
			sEnvWeb     = DBObjects.getColumnName(pConfig, VIEW_ACCESSRULES, "ENV_WEB");
			sEnvMobile  = DBObjects.getColumnName(pConfig, VIEW_ACCESSRULES, "ENV_MOBILE");

			ResultSetMetaData rsmd = pAccessRules.getMetaData();

			for (int i = 1, anz = rsmd.getColumnCount(); i <= anz && !bUseEnv; i++)
			{
				sColName = rsmd.getColumnName(i);

				if (sColName.equals(sEnvDesktop)
					|| sColName.equals(sEnvWeb)
					|| sColName.equals(sEnvMobile))
				{
					bUseEnv = true;
				}
			}
		}
		
		while (pAccessRules.next())
		{
			bValidScreen = true;
			
			if (bUseEnv)
			{
				if (pEnvironment.equals(ILauncher.ENVIRONMENT_DESKTOP))
				{
					if (!sYesValue.equals(pAccessRules.getString(sEnvDesktop)))
					{
						bValidScreen = false;
					}
				}
				else if (pEnvironment.equals(ILauncher.ENVIRONMENT_WEB))
				{
					if (!sYesValue.equals(pAccessRules.getString(sEnvWeb)))
					{
						bValidScreen = false;
					}
				}
				else if (pEnvironment.equals(ILauncher.ENVIRONMENT_MOBILE))
				{
					if (!sYesValue.equals(pAccessRules.getString(sEnvMobile)))
					{
						bValidScreen = false;
					}
				}
			}
			
			if (bValidScreen)
			{			
				pController.addAccess(pAccessRules.getString(sLifeCycleName));
			}
		}
	}
	
	/**
	 * Updates the user information with additional data from the user record.
	 *  
	 * @param pUser the user information
	 * @param pRecord the user record
	 * @throws SQLException if accessing user record fails
	 */
	protected void updateUserInfo(UserInfo pUser, ResultSet pRecord) throws SQLException
	{
		ResultSetMetaData rsmd = pRecord.getMetaData();
		
		//user found -> add additional columns to user info
		String sLabel;
		
		for (int i = 1, cnt = rsmd.getColumnCount(); i <= cnt; i++)
		{
			//we use the label instead of the label -> our security managers don't use a label,
			//but it should be possible to use custom views or other implementations where the
			//column name is different. Usually the label is the same as the name (if no alias 
			//was used)
			sLabel = rsmd.getColumnLabel(i);
			
			if (!sUsersPwd.equals(sLabel))
			{
				pUser.put(sLabel.toUpperCase(), pRecord.getObject(i));
			}
		}
	}
	
	/**
	 * Pre-validates old and new password.
	 * 
	 * @param pSession the session
	 * @throws Exception if passwords validation fails
	 */
	protected void preValidatePassword(ISession pSession) throws Exception
	{
		String sOldPwd = (String)pSession.getProperty(IConnectionConstants.OLDPASSWORD);
		String sNewPwd = (String)pSession.getProperty(IConnectionConstants.NEWPASSWORD);
		
		//-------------------------------------------------
		// Password pre-validation
		//-------------------------------------------------
		
		if (sOldPwd != null && sNewPwd != null)
		{
			validatePassword(pSession, sOldPwd, sNewPwd);
		}
	}
	
	/**
	 * Sets the user information.
	 * 
	 * @param pSession the session
	 * @param pUser the user result set
	 * @param pUserName the user name
	 * @throws Exception if setting user information fails
	 */
	private void setUserInfo(ISession pSession, ResultSet pUser, String pUserName) throws Exception
	{
		UserInfo user = new UserInfo();
		
		updateUserInfo(user, pUser);
		
		user.setUserName(pUserName);

		setUserInfo(pSession, user);
	}
    
}	// DBSecurityManager
