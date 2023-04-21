/*
 * Copyright 2019 SIB Visions GmbH
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
 * 28.06.2019 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.lang.reflect.Method;
import java.util.List;

import javax.rad.application.ILauncher;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.SessionContext;

import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.rad.server.annotation.NotAccessible;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AccessHelper</code> is a utility class to check method access.
 * 
 * @author René Jahn
 */
public final class AccessHelper 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>AccessHelper</code> is a utility class.
	 */
	private AccessHelper()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks whether the given method is {@link Accessible}. It also checks if the environment
	 * matches, if available.
	 * 
	 * @param pMethod the method to check
	 * @return <code>true</code> if given method is accessible, <code>false</code> otherwise
	 */
	public static boolean isAccessible(Method pMethod)
	{
		if (pMethod == null)
		{
			return false;
		}
		
		String sEnv = getEnvironmentName();
		
		List<Accessible> liAccess = Reflective.getAnnotation(pMethod, Accessible.class);
		
		if (liAccess != null)
		{
			//no environment available -> only allow ALL environments and don't allow specific environments

			for (int i = 0, cnt = liAccess.size(); i < cnt; i++)
			{
				String sValidEnv = liAccess.get(i).environment();
				
				if (sValidEnv == null)
				{
					return true;
				}
				
				for (String env : StringUtil.separateList(sValidEnv, ",", true))
				{
					if (Accessible.ENVIRONMENT_ALL.equalsIgnoreCase(env))
					{
						return true;
					}

					//only compare environment, if environment is known - otherwise the environment is ignore completely
					if (sEnv != null)
					{
						if (sEnv.equalsIgnoreCase(env))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks whether the given method is {@link NotAccessible}. It also checks if the environment
	 * matches, if available.
	 * 
	 * @param pMethod the method to check
	 * @return <code>true</code> if given method is not accessible, <code>false</code> otherwise
	 */
	public static boolean isNotAccesible(Method pMethod)
	{
		if (pMethod == null)
		{
			return true;
		}
		
		String sEnv = getEnvironmentName();
		
		List<NotAccessible> liAccess = Reflective.getAnnotation(pMethod, NotAccessible.class);
		
		if (liAccess != null)
		{
			//no environment available -> only allow ALL environments and don't allow specific environments

			for (int i = 0, cnt = liAccess.size(); i < cnt; i++)
			{
				String sValidEnv = liAccess.get(i).environment();
				
				if (sValidEnv == null)
				{
					return true;
				}
				
				for (String env : StringUtil.separateList(sValidEnv, ",", true))
				{
					if (NotAccessible.ENVIRONMENT_ALL.equalsIgnoreCase(env))
					{
						return true;
					}

					//only compare environment, if environment is known - otherwise the environment is ignored completely
					if (sEnv != null)
					{
						if (sEnv.equalsIgnoreCase(env))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the environment name for the current master session from the {@link SessionContext} if set.
	 * 
	 * @return the environment name or <code>null</code> if no {@link SessionContext} is set or the
	 *         environment name is not set
	 */
	public static String getEnvironmentName()
	{
		SessionContext ctxt = SessionContext.getCurrentInstance();
		
		if (ctxt != null)
		{
			String sEnv;
			
			if (ctxt instanceof AbstractSessionContext)
			{
				sEnv = ((AbstractSessionContext)ctxt).getEnvironmentName();
			}
			else
			{
				sEnv = (String)ctxt.getMasterSession().getProperties().get(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT);
			}

			return sEnv;
		}
		
		return null;
	}
	
}	// AccessHelper
