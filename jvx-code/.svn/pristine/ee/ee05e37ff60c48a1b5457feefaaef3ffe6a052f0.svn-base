/*
 * Copyright 2022 SIB Visions GmbH
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
 * 29.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa;

import javax.rad.remote.ChangePasswordException;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

import com.sibvisions.rad.server.security.AbstractSecurity;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.rad.server.security.IManagedSecurityManager;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.rad.server.security.reset.IResetPasswordManager;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>MFASecurityManager</code> is a {@link IManagedSecurityManager} and supports wrapping another
 * security manager. The wrapped security manager will be used for authentication. The {@link MFASecurityManager}
 * simply delegates and manages multi-factor authentication support.
 * 
 * @author René Jahn
 */
public class MFASecurityManager extends AbstractSecurity 
                                implements IManagedSecurityManager,
                                		   IResetPasswordManager,
										   ICloseable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the class loader. */
	private ClassLoader loader;
	
	/** the configuration. */
	private IConfiguration config;
	
	/** the "used" security manager. */
	private ISecurityManager secman;
	
	/** the multi-factor authenticator. */
	private IMFAuthenticator authenticator;
	
	/** whether the security manager is initialized. */
	private boolean bInitialized = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setClassLoader(ClassLoader pLoader)
	{
		loader = pLoader;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ClassLoader getClassLoader()
	{
		return loader;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setConfiguration(IConfiguration pConfig)
	{
		config = pConfig;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IConfiguration getConfiguration()
	{
		return config;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void validateAuthentication(ISession pSession) throws Exception
	{
		IMFAuthenticator auth = getAuthenticator();
		
		boolean bMFASuccess = false;
		
		if (auth != null)
		{
			bMFASuccess = auth.validate(pSession);
			
			//don't return here, some security managers require authentication (e.g. per session cached ones)
		}
		
		ISecurityManager sec = getSecurityManager();
		
		if (sec != null)
		{
			try
			{
				secman.validateAuthentication(pSession);
			}
			catch (ChangePasswordException cpe)
			{
				//if password is not set -> start change password
				//if password is set -> change password already started -> start MFA
				if (pSession.getProperties().get(IConnectionConstants.NEWPASSWORD) == null)
				{
					throw cpe;
				}
			}
		}

		if (auth != null)
		{
			if (!bMFASuccess)
			{
				auth.init(pSession, AbstractSecurityManager.getUserInfo(pSession));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void changePassword(ISession pSession) throws Exception
	{
		ISecurityManager sec = getSecurityManager();
				
		if (sec != null)
		{
			sec.changePassword(pSession);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void logout(ISession pSession)
	{
		ISecurityManager sec = getSecurityManager();
		
		if (sec != null)
		{
			sec.logout(pSession);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void resetPassword(ISession pSession) throws Exception
	{
		ISecurityManager sec = getSecurityManager();
		
		if (sec instanceof IResetPasswordManager)
		{
			((IResetPasswordManager)sec).resetPassword(pSession);
		}
		else
		{
			throw new SecurityException("Resetting password is not supported!");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAccessController getAccessController(ISession pSession) throws Exception
	{
		ISecurityManager sec = getSecurityManager();
		
		if (sec != null)
		{
			return sec.getAccessController(pSession);
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void release()
	{
		if (secman != null)
		{
			secman.release();
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
	 * Gets the configured security manager. If no security manager is configured, no security manager is used.
	 * 
	 * @return the configured security manager instance or <code>null</code> if not configured
	 * @throws RuntimeException if security manager creation fails
	 */
	protected ISecurityManager getSecurityManager()
	{
		if (!bInitialized)
		{
			String sSecManClass = config.getProperty("/application/securitymanager/mfa/class");
			
			if (sSecManClass == null)
			{
				bInitialized = true;
				
				return null;
			}
			
			ISecurityManager manager;
			
			try
			{			
				if (loader != null)
				{
					manager = (ISecurityManager)Class.forName(sSecManClass, true, loader).newInstance();
				}
				else
				{
					manager = (ISecurityManager)Class.forName(sSecManClass).newInstance();
				}
				
				if (manager instanceof IManagedSecurityManager)
				{
					((IManagedSecurityManager)manager).setClassLoader(loader);
					((IManagedSecurityManager)manager).setConfiguration(config);
				}
				
				secman = manager;
				
				bInitialized = true;
				
			}
			catch (Exception ex)
			{
				throw new RuntimeException("Error during instantiation of security manager", ex);
			}
		}
			
		return secman;
	}
	
	/**
	 * Gets the multi-factor authenticator.
	 * 
	 * @return the authenticator 
	 */
	protected IMFAuthenticator getAuthenticator()
	{
		if (Boolean.parseBoolean(config.getProperty("/application/securitymanager/mfa/enabled")))
		{
			if (authenticator == null)
			{
				String sClass = config.getProperty("/application/securitymanager/mfa/authenticator/class");

				if (!StringUtil.isEmpty(sClass))
				{
					try
					{
						authenticator = (IMFAuthenticator)Class.forName(sClass).newInstance();
					}
					catch (ClassNotFoundException cnfe)
					{
						throw new RuntimeException("MF authenticator '" + sClass + "' was not found!");
					}
					catch (InstantiationException ie)
					{
						throw new RuntimeException("Can't instantiate MF authenticator '" + sClass + "'!");
					}
					catch (IllegalAccessException iae)
					{
						throw new RuntimeException("MF authenticator '" + sClass + "' not accessible!");
					}
				}	
			}
		}
		else
		{
			authenticator = null;
		}
		
		return authenticator;
	}
	
}	// MFASecurityManager
