package com.sibvisions.jvx;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.rad.server.config.Configuration.ApplicationListOption;
import com.sibvisions.rad.server.http.rest.service.AbstractCustomService;
import com.sibvisions.rad.server.http.rest.service.ICustomServiceGetDelegate;
import com.sibvisions.rad.server.http.rest.service.ICustomServicePostDelegate;
import com.sibvisions.rad.server.http.rest.service.UserService;
import com.sibvisions.util.OrderedHashtable;

public class CustomRESTServices implements ServletContextListener,
                                           ICustomServiceGetDelegate,
                                           ICustomServicePostDelegate
{
	/** the health check command. */
	private static final String CMD_HEALTHCHECK = "healthCheck";
	
	
	public void contextInitialized(ServletContextEvent pEvent) 
	{
		UserService.register(getApplicationName(), CMD_HEALTHCHECK, this);
	}

	public void contextDestroyed(ServletContextEvent pEvent) 
	{
		UserService.unregister(getApplicationName(), CMD_HEALTHCHECK, this);
	}

	public Object call(AbstractCustomService pService, String pApplicationName, String pAction, String pParameter)throws Throwable 
	{
		//GET request

		OrderedHashtable<String, String> ohtResult = new OrderedHashtable<String, String>();
		
		ohtResult.put("code", "SUCCESS");
		ohtResult.put("message", "GET is working!");
		
		return ohtResult;
	}

	public Object call(AbstractCustomService pService, String pApplicationName, String pAction, String pParameter, Map<String, Object> pInput) throws Throwable 
	{
		//POST request

		OrderedHashtable<String, Object> ohtResult = new OrderedHashtable<String, Object>();

		ohtResult.put("code", "SUCCESS");
		ohtResult.put("message", "POST is working!");
		
		if (pInput != null)
		{
			ohtResult.putAll(pInput);
		}
		
		return ohtResult;
	}
	
	private String getApplicationName()
	{
		List<String> list = Configuration.listApplicationNames(ApplicationListOption.All);
		
		if (list.size() == 1)
		{
			return list.get(0);
		}
		else
		{
			return "demo";
		}
		
	}

}
