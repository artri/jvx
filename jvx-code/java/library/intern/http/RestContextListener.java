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
 */
package http;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sibvisions.rad.server.http.rest.service.AbstractCustomService;
import com.sibvisions.rad.server.http.rest.service.AdminService;
import com.sibvisions.rad.server.http.rest.service.ICustomServicePostDelegate;
import com.sibvisions.rad.server.http.rest.service.UserService;

/**
 * The <code>RadContextListener</code> is a {@link ServletContextListener} which starts
 * and stops the test databases. The init/destroy method will be triggerd through
 * the application server.
 * 
 * @author René Jahn
 */
public final class RestContextListener implements ServletContextListener,
                                                  ICustomServicePostDelegate
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public synchronized void contextInitialized(ServletContextEvent pEvent)
	{
		AdminService.register("demo", "doClearDB", this);
		UserService.register("demo", "doUserClearDB", this);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void contextDestroyed(ServletContextEvent pEvent)
	{
		AdminService.unregister("demo", "doClearDB", this);
		UserService.unregister("demo", "doUserClearDB", this);
	}

	@Override
	public Map<String, Object> call(AbstractCustomService pService, String pApplicationName, String pAction, String pParameter, Map<String, Object> pInput) throws Throwable 
	{
		pService.createSession(pApplicationName, (String)pInput.get("username"), (String)pInput.get("password"));
		
		if (pService instanceof UserService)
		{
			HashMap<String, Object> hmpResult = new HashMap<String, Object>();
			hmpResult.put("done", Boolean.TRUE);
			
			return hmpResult;
		}
		else
		{
			return null;
		}
	}
	
}	//RadContextListener
