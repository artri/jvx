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
 * 30.07.2014 - [JR] - creation
 * 16.02.2015 - [TK] - #1275: adding additional application parameters
 * 17.02.2015 - [JR] - ServiceUtil used
 */
package com.sibvisions.rad.ui.vaadin.server.communication;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.sibvisions.rad.ui.vaadin.server.ServiceUtil;
import com.vaadin.server.VaadinPortlet;
import com.vaadin.server.VaadinPortletRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.communication.PortletBootstrapHandler;

import elemental.json.JsonObject;

/**
 * The <code>VaadinPortletBootstrapHandler</code> extends the {@link PortletBootstrapHandler} and
 * changes the main script tag and application parameters before sending it back to the client.
 * 
 * @author René Jahn
 */
public class VaadinPortletBootstrapHandler extends PortletBootstrapHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>VaadinPortletBootstrapHandler</code>.
     */
    public VaadinPortletBootstrapHandler()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Shows a centered loading indicator.
     * 
     * @param pContext {@inheritDoc}
     * @param pBuilder {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    protected void appendMainScriptTagContents(BootstrapContext pContext, StringBuilder pBuilder) throws IOException 
    {
        super.appendMainScriptTagContents(pContext, pBuilder);

        //We replace to standard loading indicator with a centered one (looks better)
        
        List<Node> fragmentNodes = pContext.getBootstrapResponse().getFragmentNodes();
        
        Element appLoadingDiv = new Element(Tag.valueOf("div"), "");
        appLoadingDiv.addClass("v-app-loading");
        appLoadingDiv.attr("style", "width: 100%; height: 100%; text-align: center; vertical-align: middle;");
        Element imgLoadingIndicator = new Element(Tag.valueOf("div"), "");
        imgLoadingIndicator.attr("id", "v-app-loading-indicator-image");
        appLoadingDiv.appendChild(imgLoadingIndicator);
        
        Node node;
        Element elMainDiv = null;
        
        for (int i = 0, cnt = fragmentNodes.size(); i < cnt && elMainDiv == null; i++)
        {
            node = fragmentNodes.get(i);
            
            if (node instanceof Element 
                && pContext.getAppId().equals(node.attr("id")))
            {
                elMainDiv = (Element)node;
            }
        }
        
        if (elMainDiv != null)
        {
            Elements elements = elMainDiv.children();
            
            for (int i = 0, cnt = elements.size(); i < cnt; i++)
            {
                if (elements.get(i).tagName().equals("noscript"))
                {
                    elMainDiv.child(i - 1).replaceWith(appLoadingDiv);
                }
            }
        }
    }
    
    /**
     * Appends additional application parameters.
     * 
     * @param pContext {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    protected JsonObject getApplicationParameters(BootstrapContext pContext)
    {
    	JsonObject parameters = super.getApplicationParameters(pContext);
    
        return ServiceUtil.configureApplicationParameter(pContext.getRequest().getService(), parameters);
    } 
    
    /**
     * Gets the theme name from servlet configuration, if available and not configured as URL parameter.
     * 
     * @return the theme name
     */
    @Override
    @SuppressWarnings("deprecation")    
    public String getThemeName(BootstrapContext pContext) 
    {
        String sTheme = ((VaadinPortletRequest)pContext.getRequest()).getPortalProperty(VaadinPortlet.PORTAL_PARAMETER_VAADIN_THEME);
        
        if (sTheme == null)
        {
            VaadinPortlet portlet = VaadinPortlet.getCurrent();
            
            sTheme = portlet.getInitParameter(VaadinServlet.URL_PARAMETER_THEME);
            
            if (sTheme == null)
            {
                sTheme = super.getThemeName(pContext);
            }
        }
        
        return sTheme;
    }
    
}   // VaadinPortletBootstrapHandler
