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
 * 04.10.2009 - [JR] - creation
 * 14.11.2009 - [JR] - #7
 *                     * validatePassword implemented
 *                     * IPasswordValidator implemented   
 * 06.06.2010 - [JR] - #132: password encryption support
 * 31.07.2011 - [JR] - #261: createSecurityManager() implemented
 *                   - #16: prepareException implemented   
 * 27.09.2011 - [JR] - add/remove hidden package names  
 * 08.06.2013 - [JR] - #671: fixed NPE in getEncryptedPassword if password is null      
 * 21.11.2013 - [JR] - allow encrypted user passwords implemented        
 * 01.02.2014 - [JR] - #939: create security manager with custom class loader                                                
 */
package com.sibvisions.rad.server.security;

import java.util.Hashtable;

import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.OneTimePasswordException;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;
import javax.rad.util.RootCauseSecurityException;

import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.security.reset.DefaultOneTimePasswordHandler;
import com.sibvisions.rad.server.security.reset.IOneTimePasswordHandler;
import com.sibvisions.rad.server.security.validation.IPasswordValidator;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.ObjectCacheInstance;
import com.sibvisions.util.SecureHash;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractSecurityManager</code> is the base class for {@link ISecurityManager} implementations
 * but it does not implement the security methods.
 * It supports security managers with important and usable methods.
 *  
 * @author René Jahn
 */
public abstract class AbstractSecurityManager extends AbstractSecurity
                                              implements ISecurityManager,
                                                         IPasswordValidator,
                                                         ICloseable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the user information property name. */
	public static final String USERINFO     = IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "userInfo";
	
	/** default allowed password characters. */
	public static final String CHARS_PWD	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz._+-$!?&§%";
	/** default allowed token characters. */
	public static final String CHARS_TOKEN	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	
	/** the one time password cache. */
	private ObjectCacheInstance ocOTP = new ObjectCacheInstance();
	
	/** whether encrypted input passwords should be allowed. */
	private boolean bAllowEncryptedUserPassword = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void checkPassword(ISession pSession, String pPassword)
	{
		if (pPassword == null || pPassword.trim().length() == 0)
		{
			throw new SecurityException("The new password is empty");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close()
	{
		release();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets whether the session is authenticated with a one-time password.
	 * 
	 * @param pSession the session
	 * @return <code>true</code>
	 */
	protected boolean isOTPAuthentication(ISession pSession)
	{
		Hashtable<String, Object> htProps = pSession.getProperties();
		
        String sPassword = (String)htProps.get(IConnectionConstants.PASSWORD);
        String sOldPassword = (String)htProps.get(IConnectionConstants.OLDPASSWORD);
		String sNewPassword = (String)htProps.get(IConnectionConstants.NEWPASSWORD);
		
		if (!StringUtil.isEmpty(sPassword)
			&& !StringUtil.isEmpty(sNewPassword)
			&& CommonUtil.equals(sPassword, sOldPassword))
		{
			String sCachedPwd = (String)ocOTP.get(pSession.getUserName());

			return CommonUtil.equals(sCachedPwd, sPassword);
		}
		
		return false;
	}

	/**
	 * Compares two passwords to be identical.
	 * 
	 * @param pConfig the application configuration
	 * @param pPassword base password (plain text)
	 * @param pConfirmPassword confirmation password (encrypted or plain text)
	 * @return <code>true</code> if the passwords are identical, otherwise <code>false</code>
	 * @throws Exception if the password encryption causes an error  
	 */
	protected boolean comparePassword(IConfiguration pConfig, String pPassword, String pConfirmPassword) throws Exception
	{
		boolean bPassword = pPassword == null || pPassword.length() == 0;
		boolean bConfirmPassword = pConfirmPassword == null || pConfirmPassword.length() == 0;
		
		if (bPassword && bConfirmPassword)
		{
			return true;
		}
		else if (bPassword || bConfirmPassword)
		{
			return false;
		}
		
		//NO ENCRYPTION:
		//
		//PWD  | CONFIRM
		//--------------
		//pass | pass (OK)
		//0101 | pass (ERROR)
		
		//ENCRYPTION:
		//
		//PWD  | CONFIRM
		//--------------
		//pass | 0101 (OK -> default mode)
		//0101 | 0101 (ERROR)
		//pass | pass (OK -> compatibility mode)
		//0101 | pass (ERROR)
		
		String sEncPwd;
		
		if (isAllowEncryptedUserPassword() && isPasswordEncrypted(pPassword))
		{
			sEncPwd = pPassword;
		}
		else
		{
			sEncPwd = encryptPassword(pConfig, pPassword);
		}
		
		String sEncConfirm;

		if (isPasswordEncryptionEnabled(pConfig) && !isPasswordEncrypted(pConfirmPassword))
		{
			sEncConfirm = encryptPassword(pConfig, pConfirmPassword);
		}
		else
		{
			sEncConfirm = pConfirmPassword;
		}

		return sEncPwd.equals(sEncConfirm);
	}
	
	/**
	 * Gets the password validator from an application configuration.
	 * 
	 * @param pConfig the application configuration
	 * @return the {@link IPasswordValidator} or <code>null</code> if no validator is specified
	 */
	protected IPasswordValidator getPasswordValidator(IConfiguration pConfig)
	{
		String sValidator = pConfig.getProperty("/application/securitymanager/passwordvalidator/class");
		
		if (!StringUtil.isEmpty(sValidator))
		{
			try
			{
				return (IPasswordValidator)Class.forName(sValidator).newInstance();
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new RuntimeException("Password validator '" + sValidator + "' was not found!");
			}
			catch (InstantiationException ie)
			{
				throw new RuntimeException("Can't instantiate password validator '" + sValidator + "'!");
			}
			catch (IllegalAccessException iae)
			{
				throw new RuntimeException("Password validator '" + sValidator + "' not accessible!");
			}
		}	
		
		return null;
	}
	
	/**
	 * Validates a new password against an old password an uses a preconfigured password validator for checking
	 * the strength of the new password.
	 * 
	 * @param pSession the session which changes the password
	 * @param pOldPassword the old/current password
	 * @param pNewPassword the new password
	 * @throws Exception if the password validation failed, e.g. old = new, new is not strength enough, ...
	 */
	protected void validatePassword(ISession pSession, String pOldPassword, String pNewPassword) throws Exception
	{
		if (pOldPassword != null && pOldPassword.equals(pNewPassword))
		{
			throw new SecurityException("The old and new password are the same");
		}

		IPasswordValidator pwdval = getPasswordValidator(pSession.getConfig());
		
		if (pwdval != null)
		{
			pwdval.checkPassword(pSession, pNewPassword);
		}
		else
		{
			checkPassword(pSession, pNewPassword);
		}
	}
	
	/**
	 * This method tries to encrypt a password with configured password algorithm, via configured security manager, if possible.
	 * 
	 * @param pConfig the configuration
	 * @param pPassword the plain-text password
	 * @return the encrypted password if possible or the 
	 * @throws Exception if password encryption fails
	 */
	public static String getEncryptedPassword(IConfiguration pConfig, String pPassword) throws Exception
	{
		if (pPassword == null || pPassword.length() == 0)
		{
			return null;
		}

		ISession sess = SessionContext.getCurrentSession();
		
		ClassLoader loader = null;
		
		if (sess != null)
		{
			loader = sess.getClass().getClassLoader();
		}
		
		ISecurityManager secman = AbstractSecurityManager.createSecurityManager(loader, pConfig);
		
		if (secman instanceof AbstractSecurityManager)
		{
			return ((AbstractSecurityManager)secman).encryptPassword(pConfig, pPassword);
		}
		else
		{
			//encrypt with "standard" encryption
			return new PasswordEncryptor().encryptPassword(pConfig, pPassword); 
		}
	}
	
	/**
	 * Encrypts a plain-text password with configured password algorithm.
	 * 
	 * @param pConfig the config
	 * @param pPassword the plain-text password
	 * @return the encrypted password
	 * @throws Exception if password encryption fails
	 */
	protected String encryptPassword(IConfiguration pConfig, String pPassword) throws Exception
	{
		if (pPassword == null || pPassword.length() == 0)
		{
			return null;
		}
		
		String sAlgorithm = pConfig.getProperty("/application/securitymanager/passwordalgorithm");
		
		if (sAlgorithm != null && sAlgorithm.length() > 0)
		{
			if (!sAlgorithm.equalsIgnoreCase("PLAIN"))
			{
				return saltPassword(SecureHash.getHash(sAlgorithm, pPassword.getBytes()));
			}
		}
		
		return pPassword;
	}
	
	/**
	 * Salts an encrypted password.
	 * 
	 * @param pEncryptedPassword the encrypted password
	 * @return the salted password
	 * @throws Exception if password salt fails
	 */
	protected String saltPassword(String pEncryptedPassword) throws Exception
	{
		//special char: 127 (DEL) is a valid XML char too (http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char)
		return ((char)127) + pEncryptedPassword;
	}
	
	/**
	 * Checks if the password encryption is enabled. That means that the config parameter
	 * <code>/application/securitymanager/passwordalgorithm</code> contains an algorithm.
	 * PLAIN is not interpreted as algorithm.
	 * 
	 * @param pConfig the application configuration
	 * @return <code>true</code> if the password should be encrypted
	 */
	public static boolean isPasswordEncryptionEnabled(IConfiguration pConfig)
	{
		String sAlgorithm = pConfig.getProperty("/application/securitymanager/passwordalgorithm");
		
		return sAlgorithm != null && sAlgorithm.length() > 0 && !sAlgorithm.equalsIgnoreCase("PLAIN");
	}

	/**
	 * Creates a new {@link ISecurityManager} for the given session.
	 * 
	 * @param pSession the session
	 * @return the security manager for the application
	 * @throws Exception if the security manager is not set, the class was not found or the application is invalid
	 */
	public static ISecurityManager createSecurityManager(ISession pSession) throws Exception
	{
		return createSecurityManager(null, pSession.getConfig());
	}

	/**
	 * Creates a new {@link ISecurityManager} for the given session and class loader.
	 * 
	 * @param pLoader the class loader to use
	 * @param pSession the session
	 * @return the security manager for the application
	 * @throws Exception if the security manager is not set, the class was not found or the application is invalid
	 */
	public static ISecurityManager createSecurityManager(ClassLoader pLoader, ISession pSession) throws Exception
	{
		return createSecurityManager(pLoader, pSession.getConfig());
	}	
	
	/**
	 * Creates a new {@link ISecurityManager} for the given configuration and class loader.
	 * 
	 * @param pLoader the class loader to use
	 * @param pConfig the configuration
	 * @return the security manager for the application
	 * @throws Exception if the security manager is not set, the class was not found or the application is invalid
	 */
	public static ISecurityManager createSecurityManager(ClassLoader pLoader, IConfiguration pConfig) throws Exception
	{
		String sSecManClass = pConfig.getProperty("/application/securitymanager/class");
		
		if (sSecManClass == null)
		{
			throw new SecurityException("Security manager is not set!");
		}
		
		ISecurityManager manager;
		
		if (pLoader != null)
		{
			manager = (ISecurityManager)Class.forName(sSecManClass, true, pLoader).newInstance();
		}
		else
		{
			manager = (ISecurityManager)Class.forName(sSecManClass).newInstance();
		}
		
		if (manager instanceof IManagedSecurityManager)
		{
			((IManagedSecurityManager)manager).setClassLoader(pLoader);
			((IManagedSecurityManager)manager).setConfiguration(pConfig);
		}
		
		return manager;
	}

	/**
	 * Creates a new {@link ISecurityManager} for the given application.
	 * 
	 * @param pApplicationName the name of the application
	 * @return the security manager for the application
	 * @throws Exception if the security manager is not set, the class was not found or the application is invalid
	 */
	public static ISecurityManager createSecurityManager(String pApplicationName) throws Exception
	{
		return createSecurityManager(null, pApplicationName);
	}	
	
	/**
	 * Creates a new {@link ISecurityManager} for the given application and class loader.
	 * 
	 * @param pLoader the class loader to use
	 * @param pApplicationName the name of the application
	 * @return the security manager for the application
	 * @throws Exception if the security manager is not set, the class was not found or the application is invalid
	 */
	public static ISecurityManager createSecurityManager(ClassLoader pLoader, String pApplicationName) throws Exception
	{
		ApplicationZone zone = Configuration.getApplicationZone(pApplicationName);
		
		String sSecManClass = zone.getProperty("/application/securitymanager/class");
		
		if (sSecManClass == null)
		{
			throw new SecurityException("Security manager is not set!");
		}
		
		ISecurityManager manager;
		
		if (pLoader != null)
		{
			manager = (ISecurityManager)Class.forName(sSecManClass, true, pLoader).newInstance();
		}
		else
		{
			manager = (ISecurityManager)Class.forName(sSecManClass).newInstance();
		}
		
		if (manager instanceof IManagedSecurityManager)
		{
			((IManagedSecurityManager)manager).setClassLoader(pLoader);
			((IManagedSecurityManager)manager).setConfiguration(zone.getConfig());
		}
		
		return manager;
	}
	
	/**
	 * Sets the user information for the given session.
	 * 
	 * @param pSession the session
	 * @param pInfo the user information or <code>null</code> to unset
	 */
	public static void setUserInfo(ISession pSession, UserInfo pInfo)
	{
		if (pInfo == null)
		{
			pSession.getProperties().remove(USERINFO);
		}
		else
		{
			pSession.getProperties().put(USERINFO, pInfo);
		}
	}
	
	/**
	 * Gets the user information from the given session.
	 * 
	 * @param pSession the session
	 * @return the user information or <code>null</code> if not set
	 */
	public static UserInfo getUserInfo(ISession pSession)
	{
		return (UserInfo)pSession.getProperties().get(USERINFO);
	}
	
	/**
	 * Gets whether the given password is already encrypted.
	 * 
	 * @param pPassword the password to check
	 * @return <code>true</code> if the given password is already protected, <code>false</code> otherwise
	 */
	protected boolean isPasswordEncrypted(String pPassword)
	{
		return pPassword != null && pPassword.length() > 0 && pPassword.charAt(0) == (char)127;
	}
	
	/**
	 * Sets whether encrypted user passwords should be allowed. If encrypted user passwords are enabled,
	 * it's possible that the client sends the password encrypted. This is secure insecure, because if
	 * an attacker has the password hashcode, it's possible to authenticate.
	 * 
	 * @param pAllow <code>true</code> to allow encrypted user passwords, <code>false</code> to disable
	 *               encrypted user passwords (recommended)
	 */
	public void setAllowEncryptedUserPassword(boolean pAllow)
	{
		bAllowEncryptedUserPassword = pAllow;
	}
	
	/**
	 * Gets whether encrypted user passwords should be allowed.
	 * 
	 * @return <code>true</code> if encrypted user passwords are allowed, <code>false</code> otherwise
	 * @see #setAllowEncryptedUserPassword(boolean)
	 */
	public boolean isAllowEncryptedUserPassword()
	{
		return bAllowEncryptedUserPassword;
	}

    /**
     * Creates a "random" password with 10-15 characters.
     * 
     * @return a new "random" password
     */
    public static String createPassword()
    {
    	return StringUtil.createRandomText(CHARS_PWD, 10, 5);
    }
    
    /**
	 * Creates a "random" password.
	 * 
	 * @param pLength the password length
	 * @return a new "random" password
	 */
	public static String createPassword(int pLength)
	{
		return StringUtil.createRandomText(CHARS_PWD, pLength, 0);
	}    

    /**
     * Creates a "random" token with 8-13 characters.
     * 
     * @return a new "random" token
     */
    public static String createToken()
    {
    	return StringUtil.createRandomText(CHARS_TOKEN, 8, 5);
    }
    
    /**
	 * Creates a "random" token.
	 * 
	 * @param pLength the token length
	 * @return a new "random" token
	 */
    public static String createToken(int pLength)
    {
    	return StringUtil.createRandomText(CHARS_TOKEN, pLength, 0);
    }
    
    /**
     * Creates and sends a new one-time-password.
     * 
     * @param pSession the session
     * @param pUser the user information
     * @throws OneTimePasswordException in any case
     */
    protected void createOTP(ISession pSession, UserInfo pUser)
    {
    	String sToken = createToken();
    	
    	//valid for 5 minutes
    	ocOTP.put(pUser.getUniqueIdentifier(), sToken, 300000);
    	
    	sendOTP(pSession, pUser, sToken);
    	
		throw (OneTimePasswordException)prepareException(new OneTimePasswordException("Please enter your one-time password and set a new password."));
    }

    /**
     * Sends a one-time-password to the user. This method will use the configured {@link IOneTimePasswordHandler} from
     * the config (<code>/application/securitymanager/otphandler/class</code>). If no handler is configured, the
     * {@link DefaultOneTimePasswordHandler} will be used.
     * 
     * @param pSession the session
     * @param pUser the user information
     * @param pPassword the password
     */
	protected void sendOTP(ISession pSession, UserInfo pUser, String pPassword)
	{
		try
		{
			IConfiguration cfg = pSession.getConfig();
			
			String sCustomHandler = cfg.getProperty("/application/securitymanager/otphandler/class");
			
			if (!StringUtil.isEmpty(sCustomHandler))
			{
				try
				{
					IOneTimePasswordHandler hdDefault = (IOneTimePasswordHandler)Class.forName(sCustomHandler).newInstance();
					hdDefault.sendOneTimePassword(pSession, pUser, pPassword);
				}
				catch (ClassNotFoundException cnfe)
				{
					if (isHideFailureReason(cfg))
					{
						debug("One-Time Password handler '", sCustomHandler, "' was not found!");
						
						throw new SecurityException("Can't create one-time password handler!");
					}
					else
					{
						throw new SecurityException("One-Time Password handler '" + sCustomHandler + "' was not found!");
					}
				}
				catch (InstantiationException ie)
				{
					if (isHideFailureReason(cfg))
					{
						debug("Can't instantiate one-time password handler '", sCustomHandler, "'!");
						
						throw new SecurityException("Can't create one-time password handler!");
					}
					else
					{
						throw new SecurityException("Can't instantiate one-time password handler '" + sCustomHandler + "'!");
					}
				}
				catch (IllegalAccessException iae)
				{
					if (isHideFailureReason(cfg))
					{
						debug("One-Time Password handler '", sCustomHandler, "' not accessible!");
						
						throw new SecurityException("Can't create one-time password handler!");
					}
					else
					{
						throw new SecurityException("One-Time Password handler '" + sCustomHandler + "' not accessible!");
					}
				}
			}
			else if (!StringUtil.isEmpty(pUser.getEmailAddress()))
			{
				String sEnabled = cfg.getProperty("/application/securitymanager/otphandler/email/enabled");
				
				//null means enabled by default
				if (sEnabled == null
					|| Boolean.parseBoolean(sEnabled))
				{
					//send email - if enabled
					DefaultOneTimePasswordHandler handler = new DefaultOneTimePasswordHandler();
					handler.sendOneTimePassword(pSession, pUser, pPassword);
				}
			}
			else
			{
				if (isHideFailureReason(cfg))
				{
					debug("Can't send OTP because no custom handler was set and the email address is empty!");

					throw new SecurityException("Sending one-time password failed!");
				}
				else
				{
					throw new SecurityException("Can't send one-time password because no custom handler was set and the email address is empty!");
					
				}
			}
		}
		catch (Exception e)
		{
			error(e);
			
			if (e instanceof SecurityException)
			{
				throw (SecurityException)prepareException(e);
			}
			else
			{
				throw (RootCauseSecurityException)prepareException(new RootCauseSecurityException("Sending one-time password failed!", e));
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>PasswordEncryptor</code> is an internal class for standard password encryption via {@link AbstractSecurityManager}
	 * in a static context.
	 * 
	 * @author René Jahn
	 */
	private static final class PasswordEncryptor extends AbstractSecurityManager
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void validateAuthentication(ISession pSession) throws Exception 
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void changePassword(ISession pSession) throws Exception 
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void logout(ISession pSession) 
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public IAccessController getAccessController(ISession pSession) throws Exception 
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void release() 
		{
		}
		
	}	// PasswordEncryptor
	
}	// AbstractSecurityManager
