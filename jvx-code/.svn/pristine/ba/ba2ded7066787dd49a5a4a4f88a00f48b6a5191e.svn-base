/*
 * Copyright 2013 SIB Visions GmbH 
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
 * 19.02.2013 - [SW] - creation
 * 23.05.2015 - [JR] - #1396: CORS support
 * 02.06.2015 - [JR] - #1401: added script, needed for CORS
 */
package com.sibvisions.rad.ui.vaadin.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.server.communication.StaticDownloadHandler;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinService;

/**
 * The <code>VaadinServlet</code> class creates the {@link VaadinServletService}.
 * 
 * @author Stefan Wurm
 */
public class VaadinServlet extends com.vaadin.server.VaadinServlet
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the property name for the portlet portal information. */
    public static final String PARAM_CORSORIGIN = "cors.origin";

    /** the allowed origin. */
    private String[] saParamCorsOrigin;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ServletConfig pConfig) throws ServletException
    {
        super.init(pConfig);
        
        String sParamCorsOrigin = pConfig.getInitParameter(PARAM_CORSORIGIN);
        
        if (!StringUtil.isEmpty(sParamCorsOrigin))
        {
            List<String> liOrigin = StringUtil.separateList(sParamCorsOrigin, ",", true);
            
            saParamCorsOrigin = liOrigin.toArray(new String[liOrigin.size()]);
        }
        else
        {
            saParamCorsOrigin = null;
        }
    }
    
	/**
	 * Creates the {@link VaadinServletService}.
	 * 
	 * @param pDeploymentConfiguration the deployment configuration.
	 * @return VaadinServletService the Vaadin Servlet Service.
	 * @throws ServiceException if the creation failed.
	 */
	@Override
    protected VaadinServletService createServletService(DeploymentConfiguration pDeploymentConfiguration) throws ServiceException
    {
        VaadinServletService service = new VaadinServletService(this, pDeploymentConfiguration);
        service.init();
        
        return service;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void service(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException 
    {
        if (pRequest.getSession(false) == null)
        {
            pRequest.getSession(); // Causes already a new session id that is not the possibly requested session id.
//            pRequest.changeSessionId(); // Not necessary, a second change of session id.
        }
        if (saParamCorsOrigin != null)
        {
            // Origin is needed for all CORS requests
            String sOrigin = pRequest.getHeader("Origin");
            
            if (sOrigin != null && isAllowedRequestOrigin(sOrigin)) 
            {
                String sMethod = pRequest.getMethod().toUpperCase();
                
                // Handle preflight "option" requests
                if ("OPTIONS".equals(sMethod)) 
                {
                    pResponse.addHeader("Access-Control-Allow-Origin", sOrigin);
                    pResponse.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
     
                    // allow the requested method
                    String method = pRequest.getHeader("Access-Control-Request-Method");
                    pResponse.addHeader("Access-Control-Allow-Methods", method);
     
                    // allow the requested headers
                    String headers = pRequest.getHeader("Access-Control-Request-Headers");
                    pResponse.addHeader("Access-Control-Allow-Headers", headers);
                    pResponse.addHeader("Access-Control-Allow-Credentials", "true");
                    pResponse.setContentType("text/plain");
                    pResponse.setCharacterEncoding("utf-8");
                    pResponse.getWriter().flush();
                    
                    return;
                } 
                // Handle UIDL post requests
                else if ("POST".equals(sMethod) || "GET".equals(sMethod)) 
                {
                    pResponse.addHeader("Access-Control-Allow-Origin", sOrigin);
                    pResponse.addHeader("Access-Control-Allow-Credentials", "true");
                }
            }
        }
        
        HttpContext ctxt = new HttpContext(pRequest, pResponse);

        try
        {
            if (StaticDownloadHandler.isStaticDownload(pRequest))
            {
                StaticDownloadHandler.send(pRequest, pResponse);
            }
            else
            {
                super.service(pRequest, pResponse);
            }
        }
        finally
        {
            ctxt.release();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void servletInitialized() throws ServletException 
    {
        super.servletInitialized();
        
        getService().addSessionInitListener(new SessionInitListener()
        {
            public void sessionInit(SessionInitEvent pEvent) throws ServiceException 
            {
                pEvent.getSession().addBootstrapListener(new BootstrapListener() 
                {
                    @Override
                    public void modifyBootstrapPage(BootstrapPageResponse pResponse) 
                    {
                        HashMap<String, String> hmpParams = VaadinUI.parseUrlParams(null);
                     
                        VaadinUI.checkBrowser(hmpParams);
                        
                        if (Boolean.parseBoolean(hmpParams.get(VaadinUI.PROP_MOBILE)))
                        {
                            VaadinUI.mergeParameter(hmpParams, VaadinServlet.this);                    
                            
                            Document doc = pResponse.getDocument();
    
                            Element elHead = doc.head();
    
                            String sPath = VaadinService.getCurrentRequest().getContextPath();
                            
                            if (!StringUtil.isEmpty(sPath) && !sPath.endsWith("/"))
                            {
                                sPath = sPath + "/";
                            }
                            else
                            {
                                sPath = "";
                            }
                            
                            //iOS
                            elHead.appendChild(doc.createElement("meta").attr("name", "apple-mobile-web-app-capable").attr("content", "yes"));

                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "apple-touch-icon.png").
                                                                         attr("rel", "apple-touch-icon"));
                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "apple-touch-icon-76x76.png").
                                                                         attr("rel", "apple-touch-icon").attr("sizes", "76x76"));
                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "apple-touch-icon-120x120.png").
                                                                         attr("rel", "apple-touch-icon").attr("sizes", "120x120"));
                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "apple-touch-icon-152x152.png").
                                                                         attr("rel", "apple-touch-icon").attr("sizes", "152x152"));
                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "apple-touch-icon-180x180.png").
                                                                         attr("rel", "apple-touch-icon").attr("sizes", "180x180"));
    
                            //Android
                            elHead.appendChild(doc.createElement("meta").attr("name", "mobile-web-app-capable").attr("content", "yes"));
                            elHead.appendChild(doc.createElement("meta").attr("name", "format-detection").attr("content", "no"));
                            elHead.appendChild(doc.createElement("meta").attr("name", "msapplication-tap-highlight").attr("content", "no"));

                            elHead.appendChild(doc.createElement("link").attr("rel", "manifest").attr("href", sPath + "manifest.json"));

                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "icon-normal.png").attr("rel", "icon").
                                                                         attr("sizes", "128x128"));
                            elHead.appendChild(doc.createElement("link").attr("href", sPath + "icon-hires.png").attr("rel", "icon").
                                                                         attr("sizes", "192x192"));
                            
                            
                            VaadinServletService service = (VaadinServletService)getService();
                            
                            if (Boolean.parseBoolean(hmpParams.get(VaadinUI.PROP_PHONE)) 
                                && (Boolean.parseBoolean(service.getHotDeploymentProperty(VaadinUI.PARAM_MOBILEVIEW)) 
                                    || Boolean.parseBoolean(hmpParams.get(VaadinUI.PARAM_MOBILEVIEW))))
                            {
                                if (Boolean.parseBoolean(service.getHotDeploymentProperty(VaadinUI.PARAM_MOBILEVIEW_SCALE)) 
                                    || Boolean.parseBoolean(hmpParams.get(VaadinUI.PARAM_MOBILEVIEW_SCALE)))
                                {
                                    elHead.appendChild(doc.createElement("meta").attr("name", "viewport").attr("content", "initial-scale=1.0"));
                                }
                                else
                                {
                                    elHead.appendChild(doc.createElement("meta").attr("name", "viewport").attr("content", "user-scalable=no,initial-scale=1.0"));
                                }
                            }
                        }
                        
                        if (saParamCorsOrigin != null)
                        {
                            // Origin is needed for all CORS requests
                            String sOrigin = pResponse.getRequest().getHeader("Origin");
                            
                            if (sOrigin != null && isAllowedRequestOrigin(sOrigin)) 
                            {
                                Document doc = pResponse.getDocument();
                                
                                Element elScript = doc.createElement("script");
                                elScript.appendText("XMLHttpRequest.prototype._originalSend = XMLHttpRequest.prototype.send;");
                                elScript.appendText("var sendWithCredentials = function(data) {"); 
                                elScript.appendText("  this.withCredentials = true;"); 
                                elScript.appendText("  this._originalSend(data);"); 
                                elScript.appendText("};"); 
                                elScript.appendText("XMLHttpRequest.prototype.send = sendWithCredentials;");

                                List<Element> liChildren = new ArrayList<Element>();
                                liChildren.add(elScript);
                                
                                Element elHead = doc.head();
                                elHead.insertChildren(0, liChildren);
                            }
                        }
                    }

                    @Override
                    public void modifyBootstrapFragment(BootstrapFragmentResponse pResponse) 
                    {
                    }
                });
            }
        });
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Check that the page Origin header is allowed.
     * 
     * @param pOrigin the origin
     * @return <code>true</code> if request origin is allowed, <code>false</code> otherwise
     */
    private boolean isAllowedRequestOrigin(String pOrigin) 
    {
        for (int i = 0; i < saParamCorsOrigin.length; i++)
        {
            if (StringUtil.like(pOrigin, saParamCorsOrigin[i]))
            {
                return true;
            }
        }
        
        return false;
    }    
    
}	// VaadinServlet
