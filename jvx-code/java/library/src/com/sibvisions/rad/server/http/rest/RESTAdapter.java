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
 * 11.12.2011 - [JR] - creation
 * 16.06.2015 - [JR] - #1411: CORS support by CorsFilter 
 */
package com.sibvisions.rad.server.http.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.rad.application.ILauncher;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.Parameter;
import org.restlet.engine.application.CorsFilter;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Resource;
import org.restlet.routing.Router;
import org.restlet.util.Series;

import com.sibvisions.rad.server.http.rest.security.BasicAuthenticator;
import com.sibvisions.rad.server.http.rest.security.ForwardAuthenticator;
import com.sibvisions.rad.server.http.rest.service.AdminService;
import com.sibvisions.rad.server.http.rest.service.CallService;
import com.sibvisions.rad.server.http.rest.service.StorageService;
import com.sibvisions.rad.server.http.rest.service.UserService;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>RESTAdapter</code> class enables access to application life-cycle objects of JVx.
 * 
 * @author René Jahn
 */
public class RESTAdapter extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name of the "application name" parameter. */
	public static final String PARAM_APP_NAME = "APP_NAME";

	/** the name of the "lifecycle class" parameter. */
	public static final String PARAM_LCO_CLASS = "LCO_CLASS";
	
	/** the name of the "object" parameter. */
	public static final String PARAM_OBJECT_NAME = "OBJECT_NAME";  
	
	/** the name of the "action" parameter. */
	public static final String PARAM_ACTION_NAME = "ACTION_NAME";  
	
	/** the name of the "admin action" parameter. */
	public static final String PARAM_ADMIN_ACTION = "ADMIN_ACTION"; 
	
	/** the name of the "admin action parameter" parameter. */
	public static final String PARAM_ADMIN_ACTION_PARAM = "ADMIN_ACTION_PARAM"; 

	/** the name of the "user action" parameter. */
	public static final String PARAM_USER_ACTION = "USER_ACTION"; 
	
	/** the name of the "user action parameter" parameter. */
	public static final String PARAM_USER_ACTION_PARAM = "USER_ACTION_PARAM"; 

	/** the name of the "primary key" parameter. */
	public static final String PARAM_PK = "PK";  
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 * 
	 * @return the inbound root restlet
	 */
	@Override
	public synchronized Restlet createInboundRoot() 
	{
		Context ctxt = getContext();
		
		Series<Parameter> serParam = ctxt.getParameters();

		Router routerApp = new Router(ctxt);
		Router routerLco = new Router(ctxt);
		
		BasicAuthenticator bauth = new BasicAuthenticator(ctxt);		
		bauth.setVerifier(new SecurityManagerVerifier(this));
		bauth.setNext(routerLco);
				
		Restlet rlAuthenticator = bauth;

		//basic   = Basic http authentication
		//forward = Security Manager authentication and forwarding to Basic http authentication
		String sAuthType = getConfig(serParam, "authtype", "basic");
		
		if (!StringUtil.isEmpty(sAuthType))
		{
			sAuthType = sAuthType.toLowerCase();
			
			if ("forward".equals(sAuthType))
			{
				bauth.setMultiAuthenticating(false);
				
				ForwardAuthenticator fauth = new ForwardAuthenticator(ctxt);
				fauth.setVerifier(new SecurityManagerVerifier(this, true));
				fauth.setNext(bauth);	
	
				rlAuthenticator = fauth;
			}
		}
		
		String sCorsOrigin = getConfig(serParam, "cors.origin", null);
		
		Restlet rlNext;
		
		String sAdminZone = getConfig(serParam, "zone.admin", "_admin");
		String sUserZone = getConfig(serParam, "zone.user", "_user");
		String sDataZone = getConfig(serParam, "zone.data", "data");
		String sActionZone = getConfig(serParam, "zone.action", "action");
		String sObjectZone = getConfig(serParam, "zone.object", "object");
		
		boolean bAdminAvailable = isAdminServiceAvailable();
		
        if (!StringUtil.isEmpty(sCorsOrigin))
        {
            AuthByPassCorsFilter corsFilterLCO = new AuthByPassCorsFilter(ctxt, rlAuthenticator);
            corsFilterLCO.setAllowedOrigins(new HashSet(StringUtil.separateList(sCorsOrigin, ",", true)));
            corsFilterLCO.setAllowedCredentials(true);
            
            rlNext = corsFilterLCO;
            
            if (bAdminAvailable)
            {
	            CorsFilter corsFilterAdmin = new CorsFilter(ctxt);
	            corsFilterAdmin.setNext(AdminService.class);
	            corsFilterAdmin.setAllowedOrigins(new HashSet(StringUtil.separateList(sCorsOrigin, ",", true)));
	            corsFilterAdmin.setAllowedCredentials(true);
	            corsFilterAdmin.setSkippingResourceForCorsOptions(true);
            
	            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sAdminZone + "/{" + PARAM_ADMIN_ACTION + "}", corsFilterAdmin);
	            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sAdminZone + "/{" + PARAM_ADMIN_ACTION + "}/{" + PARAM_ADMIN_ACTION_PARAM + "}", corsFilterAdmin);
            }
            
            CorsFilter corsFilterUser = new CorsFilter(ctxt);
            corsFilterUser.setNext(UserService.class);
            corsFilterUser.setAllowedOrigins(new HashSet(StringUtil.separateList(sCorsOrigin, ",", true)));
            corsFilterUser.setAllowedCredentials(true);
            corsFilterUser.setSkippingResourceForCorsOptions(true);

            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sUserZone + "/{" + PARAM_USER_ACTION + "}", corsFilterUser);
            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sUserZone + "/{" + PARAM_USER_ACTION + "}/{" + PARAM_USER_ACTION_PARAM + "}", corsFilterUser);
        }
        else
        {
            rlNext = rlAuthenticator;

            if (bAdminAvailable)
            {
	            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sAdminZone + "/{" + PARAM_ADMIN_ACTION + "}", AdminService.class);
	            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sAdminZone + "/{" + PARAM_ADMIN_ACTION + "}/{" + PARAM_ADMIN_ACTION_PARAM + "}", AdminService.class);
            }
            
            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sUserZone + "/{" + PARAM_USER_ACTION + "}", UserService.class);
            routerApp.attach("/{" + PARAM_APP_NAME + "}/" + sUserZone + "/{" + PARAM_USER_ACTION + "}/{" + PARAM_USER_ACTION_PARAM + "}", UserService.class);
        }
      
        //authentication zone
        routerApp.attach("/{" + PARAM_APP_NAME + "}/{" + PARAM_LCO_CLASS + "}", rlNext);
        
        //services
		routerLco.attach("/" + sDataZone + "/{" + PARAM_OBJECT_NAME + "}", StorageService.class);
        routerLco.attach("/" + sDataZone + "/{" + PARAM_OBJECT_NAME + "}/", StorageService.class);
		routerLco.attach("/" + sDataZone + "/{" + PARAM_OBJECT_NAME + "}/{" + PARAM_PK + "}", StorageService.class);

		routerLco.attach("/" + sActionZone + "/{" + PARAM_ACTION_NAME + "}", CallService.class);
		routerLco.attach("/" + sObjectZone + "/{" + PARAM_OBJECT_NAME + "}/{" + PARAM_ACTION_NAME + "}", CallService.class);
	
		return routerApp;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a value from the configuration.
	 * 
	 * @param pResource the resource
	 * @param pKey the key
	 * @param pDefault the default value if no or an empty value was found
	 * @return the found or default value
	 */
	public String getConfig(Resource pResource, String pKey, String pDefault)
	{
		return getConfig(pResource.getContext().getParameters(), pKey, pDefault);
	}
	
	/**
	 * Gets whether there is a configured value for given key.
	 * 
	 * @param pKey the key
	 * @return <code>true</code> if there's a configured value for given key
	 */
	protected boolean hasConfigValue(String pKey)
	{
		Parameter param = getContext().getParameters().getFirst(pKey, false);
		
		return param != null && param.getValue() != null;
	}
	
	/**
	 * Gets a value from the configuration.
	 * 
	 * @param pConfig the configuration
	 * @param pKey the key
	 * @param pDefault the default value if no or an empty value was found
	 * @return the found or default value
	 */
	private String getConfig(Series<Parameter> pConfig, String pKey, String pDefault)
	{
		String sValue = pConfig.getFirstValue(pKey);
		
		if (StringUtil.isEmpty(sValue))
		{
			return pDefault;
		}
		
		return sValue;
	}
	
	/**
	 * Gets the class loader for the given session.
	 * 
	 * @param pSession the session
	 * @return the class loader or <code>null</code> if there's no specific class loader
	 */
	protected ClassLoader getClassLoader(ISession pSession)
	{
	    return null;
	}

	/**
	 * Configures session paramaters before a session will be created.
	 * 
	 * @param pParameter the session parameter
	 */
    protected void configureSessionParameter(Map<String, Object> pParameter)
    {
    }

    /**
     * Configures a session after it was created.
     * 
     * @param pSession the session
     */
	protected void configureSession(ISession pSession)
	{
	}
	
	/**
	 * Creates the connection properties for a new connection.
	 * 
	 * @param pAdapter the application
	 * @param pRequest the request
	 * @return the properties
	 */
	public static final HashMap<String, Object> createConnectionProperties(RESTAdapter pAdapter, Request pRequest)
	{
        Locale clientLocale = ServletUtils.getRequest(pRequest).getLocale();
        
        //it's important to set the environment
        HashMap<String, Object> hmpParams = new HashMap<String, Object>();
        //application
        hmpParams.put(IConnectionConstants.PREFIX_CLIENT + ILauncher.PARAM_ENVIRONMENT, ILauncher.ENVIRONMENT_WEB + ":rest");
        hmpParams.put(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "language", clientLocale.getLanguage());
        //additional
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_LANGUAGE, clientLocale.getLanguage());
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_COUNTRY, clientLocale.getCountry());
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_VARIANT, clientLocale.getVariant());

        Map<String, String> mpQuery = pRequest.getResourceRef().getQueryAsForm().getValuesMap();
        
        if (mpQuery != null)
        {
        	for (Entry<String, String> entry : mpQuery.entrySet())
        	{
        		hmpParams.put(entry.getKey(), entry.getValue());
        	}
        }	
        
        pAdapter.configureSessionParameter(hmpParams);

        return hmpParams;
	}
	
	/**
	 * Gets whether admin service is generally available.
	 * 
	 * @return <code>true</code> if available, <code>false</code> otherwise
	 */
	public boolean isAdminServiceAvailable()
	{
		return Boolean.parseBoolean(getConfig(getContext().getParameters(), "adminservices.available", "true"));
	}
	
	/**
	 * Gets whether default admin service is generally available.
	 * 
	 * @param pServiceName the name of the default admin service
	 * @return <code>true</code> if available, <code>false</code> otherwise
	 */
	public boolean isDefaultAdminServiceEnabled(String pServiceName)
	{
		if (!isAdminServiceAvailable())
		{
			return false;
		}
		
		String sServiceName = "adminservice." + pServiceName + ".enabled";
		
		if (hasConfigValue(sServiceName))
		{
			return Boolean.parseBoolean(getConfig(getContext().getParameters(), sServiceName, "false"));
		}
		else
		{
			sServiceName = "adminservices.enabled";
			
			if (hasConfigValue(sServiceName))
			{
				return Boolean.parseBoolean(getConfig(getContext().getParameters(), sServiceName, "false"));
			}
		}
		
		return true;
	}
	
	
	/**
	 * Gets whether a single admin service is enabled.
	 * 
	 * @param pServiceName the name of the admin service
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isAdminServiceEnabled(String pServiceName)
	{
		if (!isAdminServiceAvailable())
		{
			return false;
		}

		String sServiceName = "adminservice." + pServiceName + ".enabled";

		//config value found -> use configured value
		if (hasConfigValue(sServiceName))
		{
			return Boolean.parseBoolean(getConfig(getContext().getParameters(), sServiceName, "false"));
		}

		//no config value found -> service is enabled
		return true;
	}
	
	/**
	 * Gets whether a single service is enabled.
	 * 
	 * @param pServiceName the name of the service
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isServiceEnabled(String pServiceName)
	{
		return Boolean.parseBoolean(getConfig(getContext().getParameters(), "service." + pServiceName + ".enabled", "true"));
	}
	
}	// RESTAdapter
