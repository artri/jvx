/*
 * Copyright 2012 SIB Visions GmbH
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
 * 17.10.2012 - [CB] - creation
 * 28.03.2013 - [JR] - set language from request
 * 20.03.2013 - [JR] - isPortletMode and isServletMode implemented
 * 29.04.2013 - [JR] - notifyReload implemented
 * 23.07.2013 - [JR] - use request language only if no language was configured/set
 * 11.08.2013 - [JR] - made it easier to extend this class
 * 11.08.2013 - [JR] - set error handler
 * 12.08.2013 - [JR] - parseUrlParams: specific server base in servlet mode 
 * 27.11.2013 - [JR] - support manual push mode support for notifyWriteRespone
 * 04.12.2013 - [JR] - #888: use portlet context for accessing "global" properties
 * 14.12.2014 - [TK] - getFileHandle: close listener added to stop manual pooling
 * 18.03.2014 - [JR] - #977: check if launcher is factory is available (no factory -> no UI)
 * 12.04.2014 - [JR] - #1008: downloader extension for portlet mode
 * 25.04.2014 - [JR] - #1020: fixed temp file creation
 * 10.09.2014 - [JR] - detach overwritten because of destroying via heartbeat and "manually" close() calls
 * 31.12.2014 - [JR] - replaced mobile detection
 * 06.02.2015 - [JR] - phase controller integrated
 * 29.10.2015 - [JR] - performInvokeLater in case of upload without push
 * 21.12.2016 - [JR] - #1714 (JVx): debug log
 * 10.01.2017 - [JR] - #1738: en/disable cookie support 
 * 28.02.2018 - [JR] - #1835: configurable cookie max age and set default to 30 days
 * 15.05.2019 - [JR] - #2029: use Page location instead of request URL
 * 11.08.2019 - [JR] - init factory in reload, if not already initialized
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;

import javax.rad.application.IApplication;
import javax.rad.application.IDataConnector;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIFactoryManager;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.rad.remote.IConnectionConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFactory;
import javax.rad.ui.ILayout;
import javax.rad.ui.IRectangle;
import javax.rad.ui.UIException;
import javax.rad.ui.event.UIComponentEvent;
import javax.rad.util.EventHandler;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.SilentAbortException;
import javax.servlet.http.Cookie;

import org.vaadin.viritin.util.BrowserCookie;

import com.handinteractive.mobile.UAgentInfo;
import com.sibvisions.rad.server.IRequest;
import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.vaadin.api.IVaadinUIPhaseController;
import com.sibvisions.rad.ui.vaadin.api.event.ApplicationEvent;
import com.sibvisions.rad.ui.vaadin.api.event.LauncherEvent;
import com.sibvisions.rad.ui.vaadin.api.event.UIPhaseEvent;
import com.sibvisions.rad.ui.vaadin.ext.VaCachedResource;
import com.sibvisions.rad.ui.vaadin.ext.VaClassResource;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.DownloaderExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.UIExtension;
import com.sibvisions.rad.ui.vaadin.impl.annotation.Configuration;
import com.sibvisions.rad.ui.vaadin.impl.annotation.Parameter;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.ShortcutHandler;
import com.sibvisions.rad.ui.vaadin.impl.container.AbstractVaadinFrame;
import com.sibvisions.rad.ui.vaadin.impl.container.AbstractVaadinWindow;
import com.sibvisions.rad.ui.vaadin.impl.event.UIHandler;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;
import com.sibvisions.rad.ui.vaadin.server.VaadinErrorHandler;
import com.sibvisions.rad.ui.vaadin.server.VaadinServletService;
import com.sibvisions.rad.ui.vaadin.server.VaadinStreamResource;
import com.sibvisions.rad.ui.vaadin.server.communication.StaticDownloadHandler;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.KeyValueList;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.SecureHash;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.TimeZoneUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;
import com.vaadin.annotations.Theme;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinPortlet;
import com.vaadin.server.VaadinPortletRequest;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * The <code>VaadinUI</code> is the topmost component of any JVx vaadin Application. It provides access to the 
 * {@link UI} and adds the vaadin {@link ILauncher} implementation as {@link Component} to itself.
 * 
 * The UI uses jvx as theme name.
 * 
 * @author Benedikt Cermak
 * @see com.vaadin.ui.UI
 */
@Theme("jvx")
public class VaadinUI extends UI 
                      implements Serializable,
                                 BootstrapListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the property name for the main class. */
    protected static final String PARAM_MAIN = "main";
    /** the property name for the config. */
    protected static final String PARAM_CONFIG = "config";
    /** the property name for the cookies. */
    protected static final String PARAM_COOKIES = "cookies";
    /** the property name for the cookies max age. */
    protected static final String PARAM_COOKIES_MAXAGE = "cookies.maxAge";
    
    /** the property name for th unique identifier. */
    public static final String PARAM_UNIQUEIDENTIFIER = "Launcher.uniqueIdentifier";
    /** the property name for the portlet portal information. */
    public static final String PARAM_PORTLET = "portlet";
    /** the property name for the request URI. */
    public static final String PARAM_REQUESTURI = "Application.requestURI";
    /** the property name for the request URL. */
    public static final String PARAM_REQUESTURL = "Application.requestURL";
    /** the property name for the preserve on refresh option. */
    public static final String PARAM_PRESERVEONREFRESH = "preserveOnRefresh";
    /** the property name of an external CSS file. */
    public static final String PARAM_EXTERNALCSS = "externalCss";
    /** the property name for mobile view customization. */
    public static final String PARAM_MOBILEVIEW = "mobileView";
    /** the property name for mobile view scaling. */
    public static final String PARAM_MOBILEVIEW_SCALE = "mobileView.scale";
    /** the property name for phase controller. */
    public static final String PARAM_PHASE_CONTROLLER = "phaseController";
    /** the property name of table implementation. */
    public static final String PARAM_TABLEIMPL = "tableImpl";
    /** the property name of tree implementation. */
    public static final String PARAM_TREEIMPL = "treeImpl";
    /** the property name of tree implementation. */
    public static final String PARAM_CHARTIMPL = "chartImpl";
    /** the property name of layout implementation. */
    public static final String PARAM_LAYOUTIMPL = "layoutImpl";
    /** the property name of map implementation. */
    public static final String PARAM_MAPIMPL = "mapImpl";
    /** the property name for the polling interval in push mode (-1 to disable). */
    public static final String PARAM_POLLINGINTERVAL = "push.pollingInterval";
    /** the property name for the session timeout in seconds (-1 to disable). */
    public static final String PARAM_TIMEOUT = "sessionTimeout";

    /** the name of the mobile property. */
    public static final String PROP_MOBILE = "Web.mobile";
    /** the name of the mobile phone property. */
    public static final String PROP_PHONE = "Web.mobile.phone";
    /** the name of the tablet property. */
    public static final String PROP_TABLET = "Web.mobile.tablet";
    /** the name of the user-agent property. */
    public static final String PROP_USERAGENT = "Web.useragent";
    /** the name of the theme property. */
    public static final String PROP_THEME = "Web.theme";

    /** the name of the IOS property. */
    private static final String PROP_IOS = "Web.mobile.ios";
    /** the name of the Android property. */
    private static final String PROP_ANDROID = "Web.mobile.android";
    
    /** the root node identifier for application configuration xml files. */
    private static final String CONFIG_ROOT_NODE = "application";

    /** the ILauncher that represents this applet. */
    private transient VaadinUILauncher launcher;
    
    /** a Map with all windows. Every window is linked to the VaadinWindow. **/
    private transient HashMap<Window, AbstractVaadinWindow> hmpWindows = new HashMap<Window, AbstractVaadinWindow>();
    
    /** the object cache map. */
    private transient HashMap<String, Object> hmpObjects = new HashMap<String, Object>();

    /** a Map with all known cached resources. */
    private static transient HashMap<String, VaCachedResource> hmpResourceCache = new HashMap<String, VaCachedResource>();
    
    /** a Map with all cached resource names. */
    private static transient HashMap<String, String> hmpResourceNames = new HashMap<String, String>();
    
    /** a Map with all cached md5 resource names. */
    private static transient KeyValueList<String, String> kvlResourceNameMd5 = new KeyValueList<String, String>();
    
    /** the browser agent info. */
    private UAgentInfo agentInfo;
    
    /** the "reload" event. */
    private UIHandler eventReload;
    
    /** the ShortcutHandler for the ui. **/
    private ShortcutHandler shortcutHandler = new ShortcutHandler();

    /** the connector tracker. */
    private InternalConnectorTracker contrack;
    
    /** the destroy sync object. */
    private Object oSyncDestroy = new Object();

    /** the phase controller, if used. */
    private IVaadinUIPhaseController controller;
    
    /** the UI extension. */
    private UIExtension uiext;
    
    /** The {@link Registration} for the bootstrap listener. */
    private Registration bootstrapListenerRegistration = null;

    /** the unique UI identifier. */
    private String sIdentifier = UUID.randomUUID().toString();
    
    /** the identity object (JVM unique). */
    private static Object oIdentity = new Object();

    /** the cache for cached resource count. */
    private static int iCachedResourcesCount = 0;

    /** max age of a cookie in seconds (default: 30 days). */
    private int iCookieMaxAge = 2592000;

    /** application start time. */
    private long lStartTime;

    /** the last UI request time. */
    private long lLastRequest = -1; 
    
    /** Ignore performClosing. **/
    private boolean bIgnoreRemove = false;   
    
    /** whether {@link #doInit(VaadinRequest, int, String)} was finished. */
    private boolean bInitDone = false;
    
    /** whether to use cookies. */
    private boolean bUseCookies = true;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doInit(VaadinRequest pRequest, int pUUId, String pEmbedId) 
    {
        bInitDone = false;

        try
        {
            super.doInit(pRequest, pUUId, pEmbedId);
        }
        finally
        {
            bInitDone = true;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doRefresh(VaadinRequest pRequest) 
    {
    	super.doRefresh(pRequest);
    	
    	VaadinService service = pRequest.getService();
    	
    	if (service instanceof VaadinServletService)
    	{
    		if (((VaadinServletService)service).isPreserveUIOnRefresh())
    		{
    			boolean bInit = UIFactoryManager.getFactory() == null;
    			
    			if (bInit)
    			{
    				notifyBeforeUI();
    			}
    			    			
    			Page page = getPage();
    			page.updateLocation(page.getLocation().toString(), false, true);
    			
    			if (bInit)
    			{
    				notifyAfterUI();
    			}
    		}
    	}
    }

    /**
     * Initializes this UI.
     * 
     * The parameter "main" should give the full qualified class name of
     * the application to run. The parameter "config" should give the full qualified
     * resource name of the application configuration xml file.
     * 
     * @param pRequest the {@link VaadinRequest}
     */
    @Override
    public void init(VaadinRequest pRequest)
    {
        setErrorHandler(new VaadinErrorHandler());

        controller = createPhaseController();
        
        //URL Parameter
        HashMap<String, String> hmpParams = parseUrlParams(pRequest);
        
        //Parameter from annotation
        mergeParameter(hmpParams, this);
        
        agentInfo = checkBrowser(hmpParams);

        ApplicationConfig config = getApplicationConfig(hmpParams);
        
        String sApplicationClassName = config.getClassName();
        String sConfig = config.getConfigPath();
        
        String sCookies = null;
        String sCookieMaxAge = null;
        
        //Servlet, Portlet parameter
        if (VaadinServlet.getCurrent() != null)
        {
            VaadinServlet servlet = VaadinServlet.getCurrent();
            
            sCookies = servlet.getInitParameter(PARAM_COOKIES);
            sCookieMaxAge = servlet.getInitParameter(PARAM_COOKIES_MAXAGE);
            
            if (sApplicationClassName == null)
            {
                sApplicationClassName = servlet.getInitParameter(PARAM_MAIN);
            }
            
            if (sConfig == null)
            {
                sConfig = servlet.getInitParameter(PARAM_CONFIG);
            }

            mergeParameter(hmpParams, servlet);

            String sCss = hmpParams.get(PARAM_EXTERNALCSS);
            
            if (StringUtil.isEmpty(sCss))
            {
                sCss = ((VaadinServletService)VaadinService.getCurrent()).getHotDeploymentProperty(PARAM_EXTERNALCSS);
            }
            
            if (!StringUtil.isEmpty(sCss))
            {
                String sURI = hmpParams.get(PARAM_REQUESTURI);
                
                if (!sURI.endsWith("/"))
                {
                    //the URI should end with / (loading a stylesheet with ../ wouldn't work if not ending with /) 
                    if (sCss.startsWith("../"))
                    {
                        sCss = sCss.substring(3);
                    }
                }
                
                addCustomCss(sCss);
            }
        }
        else if (VaadinPortlet.getCurrent() != null)
        {
            //Use portlet context parameter as first choice (= global for all portlets)
            //Second choice are init parameter
            
            //"web.xml" context-param
            
            VaadinPortlet portlet = VaadinPortlet.getCurrent();

            javax.portlet.PortletContext ctxt = portlet.getPortletContext();

            sCookies = ctxt.getInitParameter("vaadinui." + PARAM_COOKIES);
            sCookieMaxAge = ctxt.getInitParameter("vaadinui." + PARAM_COOKIES_MAXAGE);
            
            //We use special prefix to avoid problems with other libraries!
            
            if (sApplicationClassName == null)
            {
                sApplicationClassName = (String)ctxt.getInitParameter("vaadinui." + PARAM_MAIN);
                sConfig = (String)ctxt.getInitParameter("vaadinui." + PARAM_CONFIG);
            }

            String sName;
            String sParamName;
            
            for (Enumeration<?> en = ctxt.getInitParameterNames(); en.hasMoreElements();)
            {
                sName = (String)en.nextElement();
                
                if (sName.startsWith("vaadinui."))
                {
                    sParamName = sName.substring(9);
                    
                    if (!hmpParams.containsKey(sParamName))
                    {
                        hmpParams.put(sParamName, ctxt.getInitParameter(sName));
                    }
                }
            }
            
            //"portlet.xml" init-param

            if (StringUtil.isEmpty(sCookies))
            {
                sCookies = portlet.getInitParameter(PARAM_COOKIES);
            }
            
            if (!StringUtil.isEmpty(sCookieMaxAge))
            {
                try
                {
                    iCookieMaxAge = Integer.parseInt(sCookieMaxAge);
                }
                catch (Exception e)
                {
                    //ignore
                }
            }
            
            if (sApplicationClassName == null)
            {
                sApplicationClassName = portlet.getInitParameter(PARAM_MAIN);
                sConfig = portlet.getInitParameter(PARAM_CONFIG);
            }
            
            for (Enumeration<?> en = portlet.getInitParameterNames(); en.hasMoreElements();)
            {
                sName = (String)en.nextElement();
                
                if (!hmpParams.containsKey(sName))
                {
                    hmpParams.put(sName, portlet.getInitParameter(sName));
                }
            }
            
            //Portlet
            hmpParams.put(PARAM_PORTLET, Boolean.TRUE.toString());
            
            //reload support
            bootstrapListenerRegistration = getSession().addBootstrapListener(this);
        }   
        
        if (!StringUtil.isEmpty(sCookies))
        {
            bUseCookies = Boolean.parseBoolean(sCookies);
        }
        
        //not available for the application
        hmpParams.remove(PARAM_MAIN);
        hmpParams.remove(PARAM_CONFIG);
        hmpParams.remove(PARAM_COOKIES);

        if (sApplicationClassName == null || sApplicationClassName.length() == 0)
        {
            throw new UIException("The '" + PARAM_MAIN + "' parameter was not found!");
        }
        
        Locale clientLocale = pRequest.getLocale();
        
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_LANGUAGE, clientLocale.getLanguage());
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_COUNTRY, clientLocale.getCountry());
        hmpParams.put(IConnectionConstants.CLIENT_LOCALE_VARIANT, clientLocale.getVariant());
        
        int offsetInMillis = getPage().getWebBrowser().getTimezoneOffset();
        int rawOffsetInMillis = getPage().getWebBrowser().getRawTimezoneOffset();

        hmpParams.put(IConnectionConstants.CLIENT_TIMEZONE_OFFSET, TimeZoneUtil.formatOffsetInMillis(offsetInMillis));
        hmpParams.put(IConnectionConstants.CLIENT_TIMEZONE_RAWOFFSET, TimeZoneUtil.formatOffsetInMillis(rawOffsetInMillis));
        hmpParams.put(IConnectionConstants.CLIENT_TIMEZONE, TimeZoneUtil.getBestMatchTimeZoneId(rawOffsetInMillis, clientLocale, offsetInMillis));
        
        if (agentInfo.detectWindows())
        {
            hmpParams.put(IConnectionConstants.CLIENT_FILE_ENCODING, "Cp1252");
        }
        else
        {
        	hmpParams.put(IConnectionConstants.CLIENT_FILE_ENCODING, System.getProperty("file.encoding"));
        }
        
        hmpParams.put(PARAM_UNIQUEIDENTIFIER, sIdentifier);
        
        if ((agentInfo.detectChrome() || agentInfo.detectFirefox()) && !Boolean.parseBoolean(hmpParams.get(PROP_MOBILE)))
        {
            //see https://github.com/vaadin/framework/issues/9846
            setMobileHtml5DndEnabled(true);
        }
        
        if (isServletMode())
        {
            setStyleName("jvxui");
        }
        else
        {
            setStyleName("jvxPortlet jvxui");
        }

        addRequestProperties(hmpParams, pRequest);
        
        String sPollingInterval = hmpParams.get(PARAM_POLLINGINTERVAL);
        
        if (!StringUtil.isEmpty(sPollingInterval))
        {
	        if (getPushConfiguration().getPushMode().isEnabled())
	        {
	        	try
	        	{
	        		setPollInterval(Integer.parseInt(sPollingInterval));
	        	}
	        	catch (Exception e)
	        	{
	        		//ignore
	        	}
	        }
        }
        
        String sTimeout = hmpParams.get(PARAM_TIMEOUT);
        
        if (!StringUtil.isEmpty(sTimeout))
        {
        	try
        	{
        		getSession().getSession().setMaxInactiveInterval(Integer.parseInt(sTimeout));
        	}
        	catch (Exception e)
        	{
                LoggerFactory.getInstance(VaadinUI.class).debug("Setting sessionTimeout in seconds failed! " + e.getMessage());
        	}
        }
        else
        {
            String sTimeoutInMinutes = hmpParams.get("session-timeout");
            
            if (sTimeoutInMinutes != null)
            {
                try
                {
                    int iTimeout = Integer.parseInt(sTimeoutInMinutes);
                    
                    if (iTimeout > 0)
                    {
                        iTimeout = iTimeout * 60;
                    }
                    
                    getSession().getSession().setMaxInactiveInterval(iTimeout);
                }
                catch (Exception e)
                {
                    LoggerFactory.getInstance(VaadinUI.class).debug("Setting session-timeout in minutes failed! " + e.getMessage());
                }
            }
        }
        
        setLauncher(createLauncher(sApplicationClassName, sConfig, hmpParams, readCookies(pRequest)));
        
        addActionHandler(shortcutHandler);
        
        lStartTime = System.currentTimeMillis();
    }

    /**
     * Refresh the appliacation if possible.
     * 
     * @param pRequest the refresh request
     */
    @Override
    public void refresh(VaadinRequest pRequest)
    {
    	if (launcher != null && launcher.application != null)
        {
    		IApplication app = launcher.application;
    		
    		if (app instanceof IDataConnector)
    		{
    			boolean bInit = UIFactoryManager.getFactory() == null;
    			
    			if (bInit)
    			{
    				notifyBeforeUI();
    			}
    			
    			try
    			{
	    			try
	    			{
	    				((IDataConnector)app).reload();
	    			}
	    			catch (Throwable th)
	    			{
	                    LoggerFactory.getInstance(VaadinUI.class).debug("Refresh UI failed!", th);
	    			}
    			}
    			finally
    			{
    				if (bInit)
    				{
    					notifyAfterUI();
    				}
    			}
    		}
        }
    	
    	super.refresh(pRequest);
    }
    
    /**
     * Removes the given window from the UI.
     * 
     * @param window the window.
     * @return true if the window is removed.
     */
    @Override
    public boolean removeWindow(Window window) 
    {
        //NO window closing event if "we" remove the window via our internal APIs,
        //but the event is important if Vaadin calls removeWindow!
        if (!bIgnoreRemove)
        {
            AbstractVaadinWindow<?> win = hmpWindows.get(window);
            
            if (win != null)
            {
                if (!win.isDisposed())
                {
                    win.performWindowClosing();
                }
            }
        }

        return super.removeWindow(window);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalConnectorTracker getConnectorTracker() 
    {
        if (contrack == null)
        {
            contrack = new InternalConnectorTracker(this);
        }
        
        return contrack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        super.close();
        
        destroy();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void detach()
    {
        try
        {
            super.detach();
        }
        finally
        {
            destroy();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTheme(String pTheme)
    {
        super.setTheme(pTheme);
        
        if (launcher != null && launcher.application != null)
        {
            launcher.setParameter(PROP_THEME, pTheme);
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBootstrapPage(BootstrapPageResponse pResponse)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse pResponse)
    {
        notifyBeforeUI();
        
        try
        {
            notifyReload();
        }
        finally
        {
            notifyAfterUI();
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

    /**
     * Gets the unique UI identifier.
     * 
     * @return the identifier
     */
    public String getIdentifier()
    {
    	return sIdentifier;
    }
    
    /**
     * Triggers forceLayout with a delay of 300ms.
     */
    public static void forceLayout()
    {
    	JavaScript js = JavaScript.getCurrent();
    	
    	if (js != null)
    	{
	        //ensure layout is correct
	        js.execute("setTimeout(function(){vaadin.forceLayout();}, 300);");
    	}
    }
    
    /**
     * Creates the launcher.
     * 
     * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run
     * @param pConfigName the config
     * @param pParams the parameters
     * @param pRegistry the Cookies
     * @return the launcher
     */
    protected VaadinUILauncher createLauncher(String pApplicationClassName, String pConfigName, HashMap<String, String> pParams, Hashtable<String, String> pRegistry)
    {
        return new VaadinUILauncher(this, pApplicationClassName, pConfigName, pParams, pRegistry);
    }
    
    /**
     * Sets the launcher.
     * 
     * @param pLauncher the launcher
     */
    protected void setLauncher(VaadinUILauncher pLauncher)
    {
        launcher = pLauncher;
    }
    
    /**
     * Gets the UI start time.
     * 
     * @return the time in millis
     */
    public long getStartTime()
    {
        return lStartTime;
    }
    
    /**
     * Gets the application configuration.
     * 
     * @param pURLParameter the parsed URL parameters
     * @return the application configuration
     */
    protected ApplicationConfig getApplicationConfig(HashMap<String, String> pURLParameter)
    {
        return new ApplicationConfig(pURLParameter.get("main"), pURLParameter.get("config"));       
    }
    
    /**
     * Notifies the launcher that it is being reclaimed and that it should destroy any allocated resources.
     */
    public void destroy()
    {
        synchronized(oSyncDestroy)
        {
            if (launcher != null)
            {
                try
                {
                    notifyBeforeUI();
    
                    try
                    {
                        VaadinSession session = getSession();
                                
                        boolean bUnlock = false;
                        
                        if (session != null)
                        {
                        	if (bootstrapListenerRegistration != null)
                        	{
                        		bootstrapListenerRegistration.remove();
                        	}
                            
                            if (!session.hasLock())
                            {
                                bUnlock = true;
                                
                                //possible, if called via SessionListener
                                session.lock();
                            }
                        }
                        
                        try
                        {
                            if (uiext != null)
                            {
                                uiext.remove();
                            }
                        }
                        catch (Exception e)
                        {
                            LoggerFactory.getInstance(VaadinUI.class).debug("Removing UIExtension from target failed!", e);
                        }
                        
                        try
                        {
                            launcher.dispose();
                            launcher = null;
                        }
                        finally
                        {
                            if (bUnlock)
                            {
                                session.unlock();
                            }
                        }
                    }
                    finally
                    {
                        notifyAfterUI();
                    }
                }
                catch (Throwable t)
                {
                    LoggerFactory.getInstance(VaadinUI.class).error("Destroying application failed!", t);
                    
                    try
                    {
                        launcher.dispose();
                        launcher = null;
                    }
                    catch (Throwable th)
                    {
                        LoggerFactory.getInstance(VaadinUI.class).error("Destroying application (force) failed!", t);
                    }
                }
            }
            else
            {
                try
                {
                    if (uiext != null)
                    {
                        uiext.remove();
                    }
                }
                catch (Exception e)
                {
                    //ignore
                }
            }
        }
    }

    /**
     * Notification before an UI action is performed.
     */
    public void notifyBeforeUI()
    {
        IFactory factory = getFactory();
        
        //this is possible if session will be destroyed, because our application will be destroyed before super call 
        //see VaadinServletService#fireSessionDestroy.
        //
        //Because of PushHandler.disconnect it's possible that this method will be called, but the UI is already destroyed!
        if (factory != null)
        {
            UIFactoryManager.registerThreadFactoryInstance(factory);
            LocaleUtil.setThreadDefault(launcher.locale);
            TimeZoneUtil.setThreadDefault(launcher.timeZone);
        }
    }

    /**
     * Notification about a "standard" request start.
     */
    public void notifyRequestStart()
    {
        getConnectorTracker().setExecuted(false);
    }
    
    /**
     * Notification about a "standard" request end.
     */
    public void notifyRequestEnd()
    {
        getConnectorTracker().setExecuted(false);
    }
    
    /**
     * Notification after an UI action was performed.
     */
    public void notifyAfterUI()
    {
        LocaleUtil.setThreadDefault(null);
        TimeZoneUtil.setThreadDefault(null);
        UIFactoryManager.unregisterThreadFactoryInstance();
    }
    
    /**
     * Notifies all registered listeners that the UI should be reloaded.
     */
    public void notifyReload()
    {
        //reset "cached file", if not downloaded
        if (launcher != null)
        {
            launcher.resetInstances();
        }
        
        if (eventReload != null)
        {
            try
            {
                VaadinFactory factory = (VaadinFactory)getFactory(); 
                
                if (factory != null)
                {
                    factory.synchronizedDispatchEvent(eventReload, this);
                }
            }
            catch (Throwable th)
            {
                LoggerFactory.getInstance(VaadinUI.class).error("Reload UI failed!", th);
            }
        }
    }
    
    /**
     * Gets the UI factory.
     * 
     * @return the factory. This should be an instance of {@link VaadinFactory}.
     */
    public IFactory getFactory()
    {
        if (launcher != null)
        {
            return launcher.getFactory();
        }
        
        return null;
    }
    
    /**
     * Gets the current application instance.
     * 
     * @return the application instance
     */
    public IApplication getApplication()
    {
        if (launcher != null)
        {
            return launcher.application;
        }
        
        return null;
    }

    /**
     * Gets the agent info.
     * 
     * @return agent info
     */
    public UAgentInfo getAgentInfo()
    {
        return agentInfo;
    }
    
    /**
     * Adds the given window to the Hashmap <code>windowMap</code> and adds the window to the UI.
     * 
     * @param pWindow the vaadin window.
     * @param pVaadinWindow the <code>AbstractVaadinWindow</code> instance.
     */
    public void addWindow(Window pWindow, AbstractVaadinWindow pVaadinWindow)
    {
        hmpWindows.put(pWindow, pVaadinWindow);
        
        addWindow(pWindow);
    }

    /**
     * Removes the given window from the Hashmap <code>windowMap</code>.
     * 
     * @param pWindow the vaadin window.
     * @param pVaadinWindow the <code>AbstractVaadinWindow</code> instance.
     */
    public void removeWindow(Window pWindow, AbstractVaadinWindow pVaadinWindow)
    {
        bIgnoreRemove = true;
        
        try
        {
            removeWindow(pWindow);
        }
        finally
        {
            bIgnoreRemove = false;
        }
        
        hmpWindows.remove(pWindow);
    }    
    
    /**
     * Reads all cookies from the request.
     * 
     * @param pRequest the http request
     * @return all available cookies as key/value pair
     */
    private Hashtable<String, String> readCookies(VaadinRequest pRequest)
    {
        if (bUseCookies)
        {
            Hashtable<String, String> htCookies = new Hashtable<String, String>();
            
            Cookie[] cookie = pRequest.getCookies();
    
            if (cookie != null)
            {
                for (int i = 0, anz = cookie.length; i < anz; i++)
                {
                    if (cookie[i].getName().startsWith("0x"))
                    {
                        String sValue;
                        
                        try
                        {
                            if (cookie[i].getValue().startsWith("0x"))
                            {
                                sValue = CodecUtil.decodeHex(cookie[i].getValue().substring(2));
                            }
                            else
                            {
                                sValue = cookie[i].getValue();
                            }
    
                            if (sValue != null && sValue.length() > 0)
                            {
                                htCookies.put(CodecUtil.decodeHex(cookie[i].getName().substring(2)), sValue);
                            }
                        }
                        catch (Exception ex)
                        {
                            htCookies.put(cookie[i].getName(), cookie[i].getValue());
                        }
                    }
                    else
                    {
                        htCookies.put(cookie[i].getName(), cookie[i].getValue());
                    }
                }
            }
            
            return htCookies;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Writes all available cookies.
     */
    private void writeCookies()
    {
        if (launcher != null)
        {
            writeCookies(VaadinService.getCurrentResponse(), launcher.getRegistryChanges());
        }
    }
    
    /**
     * Sets key/value pairs as response cookies.
     * 
     * @param pResponse the http response
     * @param pChanges the key/value pairs
     */
    private void writeCookies(VaadinResponse pResponse, List<Entry<String, String>> pChanges)
    {
        if (bUseCookies && pChanges != null)
        {
        	//this is possible if another Thread uses a cached UI instance in a seperate Thread to push to the client
        	//e.g. service calls
        	if (UI.getCurrent() != null)
        	{
	            for (Entry<String, String> entry : pChanges)
	            {
	                try
	                {
	                    Cookie cookie;
	                    
	                    if (entry.getValue() == null)
	                    {
	                        //remove
	                        
	                        cookie = new Cookie("0x" + CodecUtil.encodeHex(entry.getKey()), "");
	                        cookie.setMaxAge(0);
	                    }
	                    else
	                    {
	                        //set/change
	
	                        cookie = new Cookie("0x" + CodecUtil.encodeHex(entry.getKey()), "0x" + CodecUtil.encodeHex(entry.getValue()));
	                        cookie.setMaxAge(iCookieMaxAge);
	                    }                    
	
	                    cookie.setPath("/");
	
	                    if (pResponse != null)
	                    {
	                        pResponse.addCookie(cookie);
	                    }
	                    else
	                    {
	                        BrowserCookie.setCookie(cookie.getName(), cookie.getValue(), cookie.getPath());
	                    }
	                }
	                catch (UnsupportedEncodingException use)
	                {
	                    //nothing to be done
	                }
	            }
        	}
        }
    }   
    
    /**
     * Parses the parameters from the current request. The parameters are available
     * through the {@link ILauncher} implementation.
     * 
     * @param pRequest the request to use or <code>null</code> to use the current request
     * @return HashMap with the parameters
     * @see ILauncher#getParameter(String)
     */
    public static HashMap<String, String> parseUrlParams(VaadinRequest pRequest)
    {
        HashMap<String, String> hmpParams = new HashMap<String, String>();
        
        try 
        {
        
            VaadinRequest request = pRequest;
            
            if (request == null)
            {
            	request = VaadinService.getCurrentRequest();
            }
            
            String sUrl = null;
            
            if (request instanceof VaadinServletRequest)
            {
                VaadinServletRequest servletRequest = (VaadinServletRequest)VaadinService.getCurrentRequest();

                if (servletRequest != null)
                {
	                String sURI = servletRequest.getRequestURI();
	                String sURL = servletRequest.getRequestURL().toString();
	                
	                
	                hmpParams.put(PARAM_REQUESTURI, sURI);
	                hmpParams.put(PARAM_REQUESTURL, sURL);
	                
	                String sServletPath = servletRequest.getServletPath();
	                
	                //#2029
	                //the Page Location is better than the request URL because it'll work if we are behind a proxy like mod_proxy
	                Page page = Page.getCurrent();
	                
	                if (page != null)
	                {
		                URI url = page.getLocation();
		                
		                if (url != null)
		                {
		                	sURL = url.toString();
		                }
	                }
	
	                int iPathPos = sURL.indexOf(sServletPath);
	                
	                if (iPathPos >= 0)
	                {
	                    //http://server:port/context/uipath/ui
	                    //possible: http://server:port/name/name/ui
	                    //It's possible that the context and uipath are equal
	                    int iNextPathPos = sURL.indexOf(servletRequest.getServletPath(), iPathPos + sServletPath.length());
	                    
	                    if (iNextPathPos >= 0)
	                    {
	                        iPathPos = iNextPathPos;
	                    }
	                    
	                    hmpParams.put(ILauncher.PARAM_SERVERBASE, sURL.substring(0, iPathPos));
	                }
	                else
	                {
	                    hmpParams.put(ILauncher.PARAM_SERVERBASE, sURL);
	                }
	                
	                sUrl = servletRequest.getQueryString();
                }
            }
            else
            {
                URI uriDocBase = Page.getCurrent().getLocation();

                if (uriDocBase != null)
                {
	                String sServer = uriDocBase.getScheme() + "://" + uriDocBase.getAuthority();
	                
	                String sPath = uriDocBase.getPath();
	                
	                if (sPath != null)
	                {
	                    int iPos = sPath.lastIndexOf('/');
	                    
	                    if (iPos >= 0)
	                    {
	                    	sServer += sPath.substring(0, iPos);
	                    }
	                }
	            
	                hmpParams.put(PARAM_REQUESTURI, sServer);
	                hmpParams.put(ILauncher.PARAM_SERVERBASE, sServer);
	                
	                sUrl = uriDocBase.getQuery();
                }
            }
    
            if (sUrl != null && sUrl.trim().length() > 0)
            {
                StringTokenizer tok = new StringTokenizer(sUrl, "&");
                
                String sParam;
                
                int iPos;
                
                
                while (tok.hasMoreTokens())
                {
                    sParam = tok.nextToken();
                    
                    iPos = sParam.indexOf('=');
                    
                    if (iPos > 0)
                    {
                        hmpParams.put(sParam.substring(0, iPos), CodecUtil.decodeURLParameter(sParam.substring(iPos + 1)));
                    }
                }
            }
            
            if (request != null)
            {
				String sUser = request.getRemoteUser();
				
				if (sUser != null)
				{
					hmpParams.put(IRequest.PROP_REMOTE_USER, sUser);
				}
				
				hmpParams.put(IRequest.PROP_REMOTE_ADDRESS, request.getRemoteAddr());
				hmpParams.put(IRequest.PROP_REMOTE_HOST, request.getRemoteHost());
            }
        }
        catch (Exception e)
        {
            LoggerFactory.getInstance(VaadinUI.class).error("Parameter load error!", e);
        }
        
        return hmpParams;
    }

    /**
     * Checks mobile browser support.
     * 
     * @param pParams the application parameter
     * @return the parsed agent info
     */
    public static UAgentInfo checkBrowser(HashMap<String, String> pParams)
    {
        VaadinRequest request = VaadinService.getCurrentRequest();

        String sAgent = request.getHeader("User-Agent");
        
        pParams.put(PROP_USERAGENT, sAgent);
        
        //http://blog.mobileesp.com/
        //http://www.hand-interactive.com/m/resources/detect-mobile-java.htm
        UAgentInfo ua = new UAgentInfo(sAgent, request.getHeader("Accept"));
        
        if (ua.isTierTablet || ua.isMobilePhone)
        {
            pParams.put(PROP_MOBILE, "true");
        }
        
        if (ua.isMobilePhone)
        {
            pParams.put(PROP_PHONE, "true");
        }

        if (ua.isTierTablet)
        {
            pParams.put(PROP_TABLET, "true");
        }

        if (ua.detectAndroid())
        {
            pParams.put(PROP_ANDROID, "true");
        }
        
        if (ua.detectIos())
        {
            pParams.put(PROP_IOS, "true");
        }
        
        return ua;
    }
    
    /**
     * Adds request parameter to the given parameters.
     * 
     * @param pParams the parameters
     * @param pRequest the initial request
     */
    protected void addRequestProperties(HashMap<String, String> pParams, VaadinRequest pRequest)
    {
        String sProp = pRequest.getAuthType();
        
        if (sProp != null)
        {
            pParams.put(IRequest.PROP_AUTHENTICATION_TYPE, sProp);
        }
        
        sProp = pRequest.getRemoteUser();
        
        if (sProp != null)
        {
            pParams.put(IRequest.PROP_REMOTE_USER, sProp);
        }
        
        pParams.put(IRequest.PROP_REMOTE_ADDRESS, pRequest.getRemoteAddr());
        pParams.put(IRequest.PROP_REMOTE_HOST, pRequest.getRemoteHost());

        if (pRequest instanceof VaadinServletRequest)
        {
            VaadinServletRequest servletRequest = (VaadinServletRequest)VaadinService.getCurrentRequest();
        
            pParams.put(IRequest.PROP_URI, servletRequest.getRequestURI());
        }
    }
    
    /**
     * Merges the given parameter with servlet init parameter.
     * 
     * @param pParameter current parameter
     * @param pServlet the servlet to use
     */
    public static void mergeParameter(HashMap<String, String> pParameter, VaadinServlet pServlet)
    {
        String sName;
        
        for (Enumeration<?> en = pServlet.getInitParameterNames(); en.hasMoreElements();)
        {
            sName = (String)en.nextElement();
            
            if (!pParameter.containsKey(sName))
            {
                pParameter.put(sName, pServlet.getInitParameter(sName));
            }
        }
    }
    
    /**
     * Reads all configuration annotations from the given UI and merges the fond key/value pairs with
     * already available parameters. Only new parameters will be added.
     * 
     * @param pParameter the already available parameters
     * @param pUI the annotated UI
     */
    private void mergeParameter(HashMap<String, String> pParameter, UI pUI)
    {
        Configuration cfg = pUI.getClass().getAnnotation(Configuration.class);
        
        if (cfg != null)
        {
            Parameter[] param = cfg.value();
            
            if (param != null)
            {
                String sName;

                for (Parameter par : param)
                {
                    sName = par.name();
                    
                    if (!pParameter.containsKey(sName))
                    {
                        pParameter.put(sName, par.value());
                    }
                }
            }
        }
    }   
    
    /**
     * Gets the launcher.
     * 
     * @return the launcher.
     */
    public VaadinUILauncher getLauncher()
    {
        return launcher;
    }
    
    /**
     * Gets whether we are in portlet mode. Don't use this method with standard application servers (without portlet API)
     * otherwise a {@link ClassNotFoundException} will occur.
     * 
     * @return <code>true</code> if the last request was a portlet request, <code>false</code> otherwise
     */
    public static boolean isPortletMode()
    {
        VaadinRequest request = VaadinService.getCurrentRequest();
        
        return request != null && request instanceof VaadinPortletRequest;
    }
    
    /**
     * Gets whether we are in servlet mode.
     * 
     * @return <code>true</code> if the last request was a servlet request, <code>false</code> otherwise
     */
    public static boolean isServletMode()
    {
        VaadinRequest request = VaadinService.getCurrentRequest();

        return request == null || request instanceof VaadinServletRequest;
    }
    
    /**
     * Gets whether {@link #doInit(VaadinRequest, int, String)} was finished.
     * 
     * @return <code>true</code> if init was finished, <code>false</code> otherwise (e.g. an Exceptin occured during init)
     */
    public boolean isInitDone()
    {
        return bInitDone;
    }
    
    /**
     * Creates a new instance of {@link IVaadinUIPhaseController} with application class loader.
     * 
     * @return the phase controller or <code>null</code> if no controller class was configured or
     *         initialization failed
     */
    protected IVaadinUIPhaseController createPhaseController()
    {
        String sPhaseControllerClass;
        
        if (VaadinServlet.getCurrent() != null)
        {
            VaadinServlet servlet = VaadinServlet.getCurrent();
            
            sPhaseControllerClass = servlet.getInitParameter(PARAM_PHASE_CONTROLLER);
            
            if (StringUtil.isEmpty(sPhaseControllerClass))
            {
                VaadinService service = VaadinService.getCurrent();
                
                sPhaseControllerClass = ((VaadinServletService)service).getHotDeploymentProperty(PARAM_PHASE_CONTROLLER);
            }
        }
        else if (VaadinPortlet.getCurrent() != null)
        {
            //Use portlet context parameter as first choice (= global for all portlets)
            //Second choice are init parameter
            
            //"web.xml" context-param
            
            VaadinPortlet portlet = VaadinPortlet.getCurrent();

            javax.portlet.PortletContext ctxt = portlet.getPortletContext();

            //We use special prefix to avoid problems with other libraries!
            sPhaseControllerClass = (String)ctxt.getInitParameter("vaadinui." + PARAM_PHASE_CONTROLLER);
        }
        else
        {
            sPhaseControllerClass = null;
        }
        
        if (!StringUtil.isEmpty(sPhaseControllerClass))
        {
            try
            {
                Class<?> clazz = Class.forName(sPhaseControllerClass, true, getApplicationClassLoader());
                
                return (IVaadinUIPhaseController)clazz.newInstance();
            }
            catch (Exception e)
            {
                LoggerFactory.getInstance(VaadinUI.class).error("Initialization of phase controller '", sPhaseControllerClass, "' failed!", e);
            }
        }
        
        return null;
    }
    
    /**
     * Creates the application instance.
     * 
     * @param pLauncher the launcher
     * @param pApplicationClassName the application class name
     * @return the application instance
     * @throws Throwable if application creation fails
     */
    protected IApplication createApplication(UILauncher pLauncher, String pApplicationClassName) throws Throwable
    {
        return LauncherUtil.createApplication(pLauncher, getApplicationClassLoader(), pApplicationClassName);
    }
    
    /**
     * Gets the application class loader. This is usually the current context class loader.
     * 
     * @return the class loader
     */
    protected ClassLoader getApplicationClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }
    
    /**
     * Creates a new instance of {@link VaClassResource} for the given resource. The resource will be loaded
     * with the application class loader.
     * 
     * @param pResourceName the resource name/path
     * @return the class resource
     */
    public VaClassResource createClassResource(String pResourceName)
    {
        return new VaClassResource(getApplicationClassLoader(), pResourceName);     
    }
    
    /**
     * Creates a new instance of {@link VaCachedResource} for the given resource or gets an already 
     * created instance. The resource will be loaded with the application class loader.
     * 
     * @param pResourceName the resource name/path
     * @return the cached or a new instance
     */
    public VaCachedResource createCachedResource(String pResourceName)
    {
        return createCachedResource(pResourceName, null);
    }
    
    /**
     * Creates a new instance of {@link VaCachedResource} for the given content or gets an already
     * created instance.
     * 
     * @param pResourceName the resource name
     * @param pContent the content
     * @return the cached or a new instance
     */
    public VaCachedResource createCachedResource(String pResourceName, byte[] pContent)
    {
        if (pResourceName == null)
        {
            return null;
        }
        
        synchronized (hmpResourceNames)
        {
            String sResourceName = getIdentityCode() + "_" + pResourceName;
            
            if (pContent != null)
            {
                String sNewResourceName = sResourceName;
                
                try
                {
                    //cached by checksum
                    sNewResourceName += "_" + SecureHash.getHash("MD5", pContent);
                }
                catch (Exception e)
                {
                    //always new
                    sNewResourceName += "_" + System.currentTimeMillis();
                }
                
                kvlResourceNameMd5.put(sResourceName, sNewResourceName);
                
                sResourceName = sNewResourceName;
            }

            String sKey = hmpResourceNames.get(sResourceName);
            
            if (sKey != null)
            {
                return hmpResourceCache.get(sKey);
            }
            else
            {
                sKey = "" + iCachedResourcesCount;
                iCachedResourcesCount++;
        
                VaCachedResource cres;
                
                if (pContent == null)
                {
                    cres = new VaCachedResource(sKey, getApplicationClassLoader(), pResourceName);
                }
                else
                {
                    cres = new VaCachedResource(sKey, pResourceName, pContent);                
                }
    
                hmpResourceNames.put(sResourceName, sKey);
                hmpResourceCache.put(sKey, cres);
                
                return cres;
            }
        }
    }
    
    /**
     * Gets a cached resource from the cache.
     * 
     * @param pCacheKey the cache key
     * @return the resource or <code>null</code> if no resource was found
     */
    public static VaCachedResource getCachedResource(String pCacheKey)
    {
        return hmpResourceCache.get(pCacheKey);
    }
    
    /**
     * Removes a resource from the cache, if available.
     * 
     * @param pResourceName the resource name
     */
    public static void removeCachedResource(String pResourceName)
    {
        //don't remove resource from cache because client's could request the "old" resource
        synchronized (hmpResourceNames)
        {
            int iCode = getIdentityCode();

            String sResourceName = iCode + "_" + pResourceName;
            
            List<String> liNames = kvlResourceNameMd5.remove(sResourceName);
            
            if (liNames != null)
            {
                for (String name : liNames)
                {
                    hmpResourceNames.remove(name);
                }
            }
            
            hmpResourceNames.remove(sResourceName);
        }
    }

    /**
     * Cleans the resource cache. Only resources with static (byte[]) will be removed.
     */
    public static void cleanupResourceCache()
    {
        synchronized (hmpResourceNames)
        {
            for (String key : kvlResourceNameMd5.keySet())
            {
                cleanupResourceCache(key);
            }
            
            kvlResourceNameMd5.clear();
        }
    }
    
    /**
     * Cleans the resource cache. Only the given resource will be removed, if it contains
     * static (byte[]) content.
     * 
     * @param pResourceName the resource name
     */
    public static void cleanupResourceCache(String pResourceName)
    {
        synchronized (hmpResourceNames)
        {
            List<String> liNames = kvlResourceNameMd5.remove(pResourceName);
            
            if (liNames != null)
            {
                for (String name : liNames)
                {
                    hmpResourceCache.remove(hmpResourceNames.remove(name));
                }
            }
        }
    }
    
    /**
     * Returns the ShortcutHandler.
     * 
     * @return the ShortcutHandler.
     */
    public ShortcutHandler getShortcutHandler()
    {
        return shortcutHandler;
    }
    
    /**
     * Gets the identity hash code of VaadinUI. The code is the JVM identity hash code of
     * a static object.
     * 
     * @return the identity code
     */
    public static int getIdentityCode()
    {
        return System.identityHashCode(oIdentity);
    }
    
    /**
     * Adds a custom CSS file to the page.
     * 
     * @param pURL the CSS URL
     */
    protected void addCustomCss(String pURL)
    {
        if (pURL.startsWith("jar!"))
        {
            String sCssName = pURL.substring(4);

            VaClassResource resource = new VaClassResource(sCssName);
            resource.setFilename(sCssName.substring(0, sCssName.length() - 4) + "_" + createCssIdentifier(pURL) + ".css");
            
            //use an internal resource
            Page.getCurrent().getStyles().add(resource);
        }
        else
        {
            //add an id to force reloading after every server restart
            Page.getCurrent().getStyles().add(new ExternalResource(pURL + "?id=" + createCssIdentifier(pURL)));
        }
    }

    /**
     * Creates a unique css identifier.
     * 
     * @param pURL the configured CSS URL
     * @return the css identifier
     */
    protected String createCssIdentifier(String pURL)
    {
        return Integer.toString(System.identityHashCode(VaadinService.getCurrent()));
    }
    
    /**
     * Executes tasks which should be executed after all dirty connectors were detected and before
     * invokeLater will be called.
     */
    protected void executePostConnectorTasks()
    {
        //for sub classes
    }
    
    /**
     * Sets the last request time.
     * 
     * @param pTimestamp the timestamp in millis
     */
    public void setLastRequestTimestamp(long pTimestamp)
    {
        lLastRequest = pTimestamp;
    }
    
    /**
     * Gets the last request time.
     * 
     * @return the timestamp in millis
     */
    public long getLastRequestTimestamp()
    {
        if (lLastRequest == -1)
        {
            lLastRequest = getSession().getLastRequestTimestamp();
        }
        
        return lLastRequest;
    }
    
    /**
     * Puts an object to the cache.
     * 
     * @param pName the name of the object
     * @param pValue the value
     */
    public void putObject(String pName, Object pValue)
    {
        if (pValue == null)
        {
            hmpObjects.remove(pName);
        }
        else
        {
            hmpObjects.put(pName, pValue);
        }
    }
    
    /**
     * Gets an object from the cache.
     * 
     * @param pName the name of the object
     * @return the cached value or <code>null</code> if no object was found with given name
     */
    public Object getObject(String pName)
    {
        return hmpObjects.get(pName);
    }

    /**
     * Gets the UI extension.
     * 
     * @return the {@link UIExtension}
     */
    public UIExtension getExtension()
    {
        if (uiext == null)
        {
            uiext = new UIExtension();
            uiext.extend(this);
        }
        
        return uiext;
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Events
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the event handler for the reload event.
     * 
     * @return the event handler
     */
    public UIHandler eventReload()
    {
        if (eventReload == null)
        {
            eventReload = new UIHandler("reload");
        }
        
        return eventReload;
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************


    /**
     * The <code>VaadinUILauncher</code> class is the vaadin implementation of {@link ILauncher}.
     * It has full access to the {@link UI}.
     * 
     * @author Benedikt Cermak
     */
    public static class VaadinUILauncher extends AbstractVaadinFrame<SingleComponentContainer> 
                                         implements ILauncher, 
                                                    Serializable
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** the {@link UILauncher} instance. */
        private UILauncher uilauncher;

        /** the UI. */
        private VaadinUI ui;

        /** the application implementation base. */
        private IApplication application = null;
        
        /** the locale. */
        private Locale locale = null;
        
        /** the time zone. */
        private TimeZone timeZone = null;
        
        /** the parameters of the ui. */
        private HashMap<String, String> hmpParams = null;
        
        /** the changed hashtable for registry values. */
        private ChangedHashtable<String, String> chtRegistry = null;
        
        /** Handels the Download. **/
        private VaadinRequestHandler requestHandler = new VaadinRequestHandler();
        
        /** the download extension (for portlets). */
        private DownloaderExtension downloader;

        /** the resize listener. */
        private BrowserWindowResizeListener bwinResizeListener;
        
        /** The {@link Registration} for the browser window resize listener. */
        private Registration windowResizeListenerRegistration = null;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>VaadinUILauncher</code>.
         * 
         * @param pUI the VaadinUI.
         * @param pApplicationClassName the full qualified class name of 
         *        the {@link IApplication} to run.
         * @param pConfigName the config
         * @param pParams the parameters
         * @param pRegistry the Cookies
         */
        public VaadinUILauncher(VaadinUI pUI, String pApplicationClassName, String pConfigName, HashMap<String, String> pParams, Hashtable<String, String> pRegistry)
        {				
            super(pUI);
            
            //needed for push during constructor, otherwise we don't have a launcher and can't set the factory
            pUI.launcher = this;
            
            ui = pUI;
            
            hmpParams = pParams;
                        
            mergeConfiguration(pConfigName);
            
            //if no custom language was configured -> use the request language
            if (StringUtil.isEmpty(hmpParams.get(ILauncher.PARAM_APPLICATIONLANGUAGE)))
            {
                hmpParams.put(ILauncher.PARAM_APPLICATIONLANGUAGE, hmpParams.get(IConnectionConstants.CLIENT_LOCALE_LANGUAGE));
            }
            
            //if no custom language was configured -> use the request language
            if (StringUtil.isEmpty(hmpParams.get(ILauncher.PARAM_APPLICATIONTIMEZONE)))
            {
                hmpParams.put(ILauncher.PARAM_APPLICATIONTIMEZONE, hmpParams.get(IConnectionConstants.CLIENT_TIMEZONE));
            }
            
            hmpParams.put(PROP_THEME, ui.getTheme());
            
            //use initial registry parameters
            if (pRegistry != null)
            {
                chtRegistry = new ChangedHashtable<String, String>();
                
                for (Map.Entry<String, String> entry : pRegistry.entrySet())
                {
                    chtRegistry.put(entry.getKey(), entry.getValue(), false);
                }
            }           
    
            VaadinFactory factory;
                
            try
            {
                factory = (VaadinFactory)Reflective.construct(ui.getApplicationClassLoader(), getParameter(ILauncher.PARAM_UIFACTORY));
            }
            catch (Throwable th)
            {
                factory = new VaadinFactory();
                
                LoggerFactory.getInstance(VaadinUI.class).debug("Fallback to VaadinFactory", th);
            }
            
            //configure factory
            
            String sTableImpl = hmpParams.get(PARAM_TABLEIMPL);

            if (sTableImpl != null)
            {
                factory.setProperty(VaadinFactory.PROPERTY_COMPONENT_LEGACY_TABLE, "grid".equals(sTableImpl) ? Boolean.FALSE : Boolean.TRUE);
            }
            
            String sTreeImpl = hmpParams.get(PARAM_TREEIMPL);

            if (sTreeImpl != null)
            {
                factory.setProperty(VaadinFactory.PROPERTY_COMPONENT_LEGACY_TREE, "treegrid".equals(sTreeImpl) ? Boolean.FALSE : Boolean.TRUE);
            }
            
            String sChartImpl = hmpParams.get(PARAM_CHARTIMPL);
            
            if (sChartImpl != null)
            {
                factory.setProperty(VaadinFactory.PROPERTY_COMPONENT_CHARTJS, "chartjs".equals(sChartImpl) ? Boolean.TRUE : Boolean.FALSE);
            }
            
            String sLayoutImpl = hmpParams.get(PARAM_LAYOUTIMPL);
            
            if (sLayoutImpl != null)
            {
                factory.setProperty(VaadinFactory.PROPERTY_COMPONENT_CLIENT_LAYOUTS, "client".equals(sLayoutImpl) ? Boolean.TRUE : Boolean.FALSE);
            }
            
            String sMapImpl = hmpParams.get(PARAM_MAPIMPL);
            
            if (sMapImpl != null)
            {
                factory.setProperty(VaadinFactory.PROPERTY_COMPONENT_MAP_GOOGLE, "google".equals(sMapImpl) ? Boolean.TRUE : Boolean.FALSE);
            }            
            
            try
            {
            	factory.setUI(ui);
            }
            catch (Throwable th)
            {
            	handleException(th);
            	
            	return;
            }
            
            UIFactoryManager.registerThreadFactoryInstance(factory);
            
            pUI.getSession().addRequestHandler(requestHandler);
            
            setFactory(factory);
            
            uilauncher = new UILauncher(this);

            if (pUI.controller != null)
            {
                pUI.controller.phaseChanged(new LauncherEvent(UIPhaseEvent.PHASE_CONFIGURE_LAUNCHER, pUI, uilauncher));
            }
            
            try
            {
                application = ui.createApplication(uilauncher, pApplicationClassName);

                if (pUI.controller != null)
                {
                    pUI.controller.phaseChanged(new ApplicationEvent(UIPhaseEvent.PHASE_CONFIGURE_APPLICATION, pUI, application));
                }
                
                uilauncher.setTitle(application.getName());
                
                //add the application
                uilauncher.setLayout(new VaadinClientBorderLayout());
                uilauncher.add(application, VaadinClientBorderLayout.CENTER); 

                Component panRoot = getRootPane().getResource();
                
                panRoot.setStyleName("jvxapp");

                if (isServletMode())
                {
                    //Portlet defines the size. Don't set it here!
                    pUI.setSizeFull();
                }
                
                pUI.setContent(panRoot);

                getRootPane().setSizeFull();

                bwinResizeListener = new BrowserWindowResizeListener() 
                {
                    @Override
                    public void browserWindowResized(BrowserWindowResizeEvent event)
                    {
                        getRootPane().performLayout();
                        
                        if (eventComponentResized != null)
                        {
                        	VaadinFactory fact = getFactory();
                        	
                        	if (fact != null)
                        	{
                        		fact.synchronizedDispatchEvent(eventComponentResized, 
                        				                       new UIComponentEvent(eventSource, 
										                                            UIComponentEvent.COMPONENT_RESIZED, 
										                                            System.currentTimeMillis(),
										                                            0));
                        	}
                        }
                    }
                };
                
                windowResizeListenerRegistration = ui.getPage().addBrowserWindowResizeListener(bwinResizeListener);
        
                uilauncher.setVisible(true);

                if (pUI.controller != null)
                {
                    pUI.controller.phaseChanged(new ApplicationEvent(UIPhaseEvent.PHASE_BEFORE_NOTIFYVISIBLE, pUI, application));
                }
                
                application.notifyVisible();
            }
            catch (Throwable th)
            {
                handleException(th);
            }
            
            if (!isServletMode() || isAndroidDevice())
            {
                downloader = new DownloaderExtension();
                downloader.extend(ui);
            }

//            forceLayout();
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // IFRAME
        
        /**
         * {@inheritDoc}
         */
        public void toFront()
        {
        }   
        
        /**
         * {@inheritDoc}
         */
        public void centerRelativeTo(IComponent pComponent)
        {
        }
        
        //ILAUNCHER
        
        /**
         * {@inheritDoc}
         */
        public String getParameter(String pName) 
        {
            String sValue = null;
            
            //Use userdefined parameters before using the applet parameters
            if (hmpParams.containsKey(pName))
            {
                sValue = hmpParams.get(pName);
            }
            
            if (sValue != null)
            {
                sValue = sValue.replace("\\n", "\n");
                sValue = sValue.replace("<br>", "\n");
            }
            
            return LauncherUtil.replaceParameter(sValue, this);
        }
        
        /**
         * {@inheritDoc}
         */
        public void setParameter(String pName, String pValue) 
        {
            hmpParams.put(pName, pValue);
        }
        
        /**
         * {@inheritDoc}
         */
       
        public void showDocument(String pDocumentname, IRectangle pBounds, String pTarget) throws Exception
        {
            if (pBounds != null)
            {
                ui.getPage().open(pDocumentname, pTarget, pBounds.getWidth(), pBounds.getHeight(), BorderStyle.DEFAULT);
            }
            else
            {
                ui.getPage().open(pDocumentname, pTarget, true);
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("deprecation")
        public void showFileHandle(final IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
        {
            if (isAndroidDevice() && downloader != null)
            {
        		downloader.setDownloadResource(StaticDownloadHandler.createResource(ui, pFileHandle, StaticDownloadHandler.TYPE_OPEN));
            }
            else if (isIOSDevice())
            {
                saveFileHandle(pFileHandle, null);
            }
            else
            {
                VaadinStreamResource resource = new VaadinStreamResource(pFileHandle, true);
    
                if (pBounds != null)
                {
                    ui.getPage().open(resource, pTarget, pBounds.getWidth(), pBounds.getHeight(), BorderStyle.DEFAULT);
                }
                else
                {
                    ui.getPage().open(resource, pTarget, true);
                }
            }
        }
    
        /**
         * {@inheritDoc}
         */
        public void showFileHandle(IFileHandle pFileHandle) throws Throwable
        {
            showFileHandle(pFileHandle, null, "_blank");
        }
        
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("deprecation")
        public void saveFileHandle(final IFileHandle pFileHandle, String pTitle) throws Throwable
        {
            if (downloader != null)
            {
                if (isAndroidDevice())
                {
                    downloader.setDownloadResource(StaticDownloadHandler.createResource(ui, pFileHandle, StaticDownloadHandler.TYPE_SAVE));
                }
                else
                {
                    downloader.setDownloadResource(new VaadinStreamResource(pFileHandle, false));
                }
            }
            //#1632
            else if (isServletMode() && isMobileDevice())
            {
                if (isIOSDevice() && !((VaadinServletService)VaadinService.getCurrent()).isPreserveUIOnRefresh())
                {
                    final Window win = new Window(StringUtil.isEmpty(pTitle) ? uilauncher.translate("Options") : uilauncher.translate(pTitle));
                    
                    FileDownloader down = new FileDownloader(StaticDownloadHandler.createResource(ui, pFileHandle, StaticDownloadHandler.TYPE_SAVE));
                    
                    ClickListener lisCloseWindow = new ClickListener()
                    {
                        @Override
                        public void buttonClick(ClickEvent event)
                        {
                            win.close();
                        }
                    };
                    
                    Button butContinue = new Button(uilauncher.translate("Continue"));
                    butContinue.addStyleName("continue");
                    butContinue.addClickListener(lisCloseWindow);
                    butContinue.setWidth("100%");
                    
                    down.extend(butContinue);

                    Button butExit = new Button(uilauncher.translate("Cancel"));
                    butExit.addStyleName("cancel");
                    butExit.addClickListener(lisCloseWindow);
                    butExit.setWidth("100%");
                    
                    VerticalLayout vlay = new VerticalLayout();
                    vlay.setSpacing(true);
                    vlay.addStyleName("optionbuttons");
                    vlay.setMargin(true);
                    vlay.setSizeFull();
                    vlay.addComponent(butContinue);
                    vlay.addComponent(butExit);

                    win.addStyleName("save");
                    win.setClosable(false);
                    win.setDraggable(false);
                    win.setResizable(false);
                    win.setModal(true);
                    win.setContent(vlay);
                    win.center();
                    
                    win.setWidth(150, Unit.PIXELS);
                    win.setHeight(170, Unit.PIXELS);
                    
                    ui.addWindow(win);
                }
                else
                {
                    requestHandler.fileHandle = pFileHandle;
                    
                    // Starts a request and so the handleRequest from the VaadinRequestHandler is called.
                    ui.getPage().setLocation(getParameter(PARAM_REQUESTURI));
                }
            }
            else
            {
                ui.getPage().open(new VaadinStreamResource(pFileHandle, false), "_blank", true);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void getFileHandle(final IFileHandleReceiver pFileHandleReceiver, String pTitle) throws Throwable
        {
            final Window window = new Window(pTitle == null ? "" : uilauncher.translate(pTitle));
            window.setModal(true);
            window.addStyleName("upload");
            window.setResizable(false);
            
            final ProgressBar progressBar = new ProgressBar();
            progressBar.setWidth("100%");
            progressBar.setVisible(false);
            progressBar.setIndeterminate(false);

            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);

            final Upload upload = new Upload();
            upload.setReceiver(new UploadReceiver());

            if (!ui.getPushConfiguration().getPushMode().isEnabled()) // Set PollInterval if no push mode is set.
            {
                ui.setPollInterval(500);
            }
            
            window.addCloseListener(new CloseListener()
            {
                @Override
                public void windowClose(CloseEvent pEvent)
                {
                    // Disable manual polling on window closing
                    if (!ui.getPushConfiguration().getPushMode().isEnabled())
                    {
                        performInvokeLater();

                        ui.setPollInterval(-1);
                    }
                }
            });

            upload.addStartedListener(new StartedListener() 
            {
                public void uploadStarted(StartedEvent pEvent)
                {
                    ui.access(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            progressBar.setValue(0.0f);
                            progressBar.setVisible(true);
                            
                            if (ui.getPushConfiguration().getPushMode().isEnabled())
                            {
                                ui.push(); // Is needed because when push mode is manual
                            }
                        } 
                    });
                }
            });

            upload.addProgressListener(new ProgressListener() 
            {
                public void updateProgress(final long readBytes, final long contentLength)
                {
                    float newValue = readBytes / (float)contentLength;
                    
                    if ((newValue - progressBar.getValue()) >= 0.05f || readBytes >= contentLength)
                    {
                        ui.access(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                progressBar.setValue(newValue);
                                
                                if (ui.getPushConfiguration().getPushMode().isEnabled())
                                {                               
                                    ui.push(); // Is needed because when push mode is manual
                                }
                            } 
                        });
                    }
                }
            });
            
            upload.addSucceededListener(new SucceededListener() 
            {
                public void uploadSucceeded(SucceededEvent pEvent) 
                {
                    if (!ui.getPushConfiguration().getPushMode().isEnabled())
                    {
                        performInvokeLater();
                        
                        ui.setPollInterval(-1);
                    }
                }
            });     
            
           upload.addFailedListener(new Upload.FailedListener() 
           {
                public void uploadFailed(FailedEvent pEvent) 
                {
                    try
                    {
                        ((UploadReceiver)upload.getReceiver()).delete();
                    }
                    finally
                    {
                        if (!ui.getPushConfiguration().getPushMode().isEnabled())
                        {
                            performInvokeLater();
                            
                            ui.setPollInterval(-1);
                        }
                    }
                }
            });         
            
            upload.addFinishedListener(new FinishedListener() 
            {
                public void uploadFinished(FinishedEvent pEvent)
                {
                    if (!ui.getPushConfiguration().getPushMode().isEnabled())
                    {
                        performInvokeLater();
                        
                        ui.setPollInterval(-1);
                    }
                    
                    UploadReceiver receiver = (UploadReceiver)upload.getReceiver();

                    InputStream inputStream = null;
                    
                    try
                    {
                        receiver.free();
                        
                        //avoid multiple calls (possible if an error occurs)
                        if (receiver.uploadFile != null)
                        {
                            inputStream = new FileInputStream(receiver.uploadFile);
    
                            pFileHandleReceiver.receiveFileHandle(new FileHandle(pEvent.getFilename(), inputStream));
                        }
                    }
                    catch (Throwable th)
                    {
                        Throwable thWrapped = EventHandler.getWrappedExceptionAllowSilent(th);
                        
                        if (!(thWrapped instanceof SilentAbortException))
                        {
                            ExceptionHandler.raise(thWrapped);
                        }
                        else
                        {
                            LoggerFactory.getInstance(VaadinUI.class).debug(th);
                        }
                    }
                    finally
                    {
                        window.close();

                        if (inputStream != null)
                        {
                            try
                            {
                                inputStream.close();
                            }
                            catch (Exception e)
                            {
                                //nothing to be done
                            }
                        }
                        
                        receiver.delete();
                    }
                }

            });
            
            upload.setButtonCaption(uilauncher.translate("Upload"));
            upload.setWidth(100, Unit.PERCENTAGE);
            //NO additional caption needed inside the window
            //upload.setCaption(uilauncher.translate("Please choose the file:"));           

            layout.setWidth(100, Unit.PERCENTAGE);
            layout.addComponent(upload);
            layout.addComponent(progressBar);
            
            window.setContent(layout);
            
            layout.setMargin(true);
            
            ui.addWindow(window);
        }
         
        /**
         * {@inheritDoc}
         */
        public void cancelPendingThreads()
        {
        }
        
        /**
         * {@inheritDoc}
         */
        public void setRegistryKey(String pKey, String pValue)
        {
            if (chtRegistry == null)
            {
                chtRegistry = new ChangedHashtable<String, String>();
            }
            
            String sName = LauncherUtil.getRegistryApplicationName(this);
            
            if (pValue == null)
            {
                chtRegistry.remove(sName + "." + pKey);
                
                if (chtRegistry.size() == 0)
                {
                    chtRegistry = null;
                }
            }
            else
            {
                chtRegistry.put(sName + "." + pKey, pValue);
            }       
        }
        
        /**
         * {@inheritDoc}
         */
        public String getRegistryKey(String pKey)
        {
            if (chtRegistry == null)
            {
                return null;
            }
            
            String sName = LauncherUtil.getRegistryApplicationName(this);
            
            return chtRegistry.get(sName + "." + pKey);
        }
        
        /**
         * {@inheritDoc}
         */
        public String getEnvironmentName()
        {
            return ILauncher.ENVIRONMENT_WEB + ":vaadin";
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public IApplication getApplication()
        {
        	return application;
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public Locale getLocale()
        {
        	return locale;
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void setLocale(Locale pLocale)
        {
        	locale = pLocale;
        	
        	LocaleUtil.setThreadDefault(locale);
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public TimeZone getTimeZone()
        {
        	return timeZone;
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void setTimeZone(TimeZone pTimeZone)
        {
        	timeZone = pTimeZone;
        	
        	TimeZoneUtil.setThreadDefault(timeZone);
        }
        
        //IEXCEPTIONLISTENER
        
        /**
         * {@inheritDoc}
         */
        public void handleException(Throwable pThrowable)
        {
            VaadinFactory.showError(ui, pThrowable);
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        @Override
        public void setLayout(ILayout pLayout)
        {
            if (uilauncher != null)
            {
                super.setLayout(pLayout);
            }
        }       

        /**
         * {@inheritDoc}
         */
        @Override
        public void addToVaadin(IComponent pComponent, Object pConstraints, int pIndex)
        {
            if (uilauncher != null)
            {
                getRootPane().addComponentToContent(pComponent, pConstraints, pIndex);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeFromVaadin(IComponent pComponent)
        {
            if (uilauncher != null)
            {
                getRootPane().removeComponentFromContent(pComponent);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose()
        {
        	VaadinSession session = ui.getSession();
        	
        	if (session != null)
        	{
        		session.removeRequestHandler(requestHandler);
        	}
            
            if (windowResizeListenerRegistration != null)
            {
            	windowResizeListenerRegistration.remove();
            }
            
            if (downloader != null)
            {
                downloader.remove();
            }
            
            if (application != null)
            {
                try
                {
                    application.notifyDestroy();
                }
                catch (Exception e)
                {
                    //force closing the application
                    LoggerFactory.getInstance(VaadinUI.class).info("Forced application destroy failed", e);
                }
            }
            
            try
            {
                VaadinFactory factory = ((VaadinFactory)getFactory());
                        
                //possible if init factory fails
                if (factory != null)
                {
                	factory.destroyThreads();
                }
            }
            finally
            {
                super.dispose();
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void setTitle(String pTitle)
        {
            //e.g. don't change the title in portlet mode
            if (VaadinUI.isServletMode())
            {
                super.setTitle(pTitle);
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public IDimension getSize()
        {
            Page page = ui.getPage();
            
            if (page != null)
            {
                int iWidth = page.getBrowserWindowWidth();
                int iHeight = page.getBrowserWindowHeight();
                
                if (iWidth > 0 && iHeight > 0)
                {
                    return new VaadinDimension(iWidth, iHeight);
                }
            }

            return super.getSize();
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public IRectangle getBounds()
        {
            Page page = ui.getPage();
            
            if (page != null)
            {
                if (page != null)
                {
                    int iWidth = page.getBrowserWindowWidth();
                    int iHeight = page.getBrowserWindowHeight();
                    
                    if (iWidth > 0 && iHeight > 0)
                    {
                        return new VaadinRectangle(0, 0, iWidth, iHeight);
                    }
                }
            }
            
            return super.getBounds();
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Loads and merges the application configuration with current values. New values will be added and existing
         * values won't be replaced.
         * 
         * @param pConfig the resource path of the application configuration
         */
        protected void mergeConfiguration(String pConfig)
        {
            String sConfig = pConfig;
            
            //the configuration can be configured via an URL Parameter
            if (sConfig == null)
            {
                sConfig = "application.xml";
            }
                
            try
            {
                InputStream isConfig = ResourceUtil.getResourceAsStream(ui.getApplicationClassLoader(), sConfig);
                
                if (isConfig != null)
                {
                    try
                    {
                        XmlWorker xmw = new XmlWorker();
                        
                        XmlNode xmnAppConfig = xmw.read(isConfig);
                        
                        if (xmnAppConfig != null)
                        {
                            List<XmlNode> liNodes = xmnAppConfig.getNode(CONFIG_ROOT_NODE).getNodes();
                            
                            String sKey;
                            
                            for (XmlNode xmnSub : liNodes)
                            {
                                sKey = xmnSub.getName();
                                
                                //don't overwrite parameters, configured via URL or Deployment descriptor!!!
                                if (!hmpParams.containsKey(sKey))
                                {
                                    hmpParams.put(sKey, xmnSub.getValue());
                                }
                            }
                        }
                    }
                    finally
                    {
                        CommonUtil.close(isConfig);
                    }
                }
            }
            catch (Exception e)
            {
                LoggerFactory.getInstance(VaadinUI.class).error("Configuration load error!", e);
            }
        }
        
        /**
         * Gets a list of all registry key/value pairs which have changed since last access.
         *  
         * @return the list of changed key/value pairs or <code>null</code> if no changes are available
         */
        List<Entry<String, String>> getRegistryChanges()
        {
            if (chtRegistry != null)
            {
                return chtRegistry.getChanges();
            }
            else
            {
                return null;
            }
        }
        
        /**
         * Resets the internal instances.
         */
        protected void resetInstances()
        {
            if (downloader != null)
            {
                downloader.setDownloadResource(null);
            }
            
            requestHandler.fileHandle = null;
        }
        
        /**
         * Performs invokeLater without session locking.
         */
        private void performInvokeLater()
        {
            IFactory factory = getFactory();
                
            if (factory != null)
            {
                ((VaadinFactory)factory).performInvokeLater();
            }
        }
        
        /**
         * Gets whether the client is an android device.
         * 
         * @return <code>true</code> if android device is used, <code>false</code> otherwise
         */
        private boolean isAndroidDevice()
        {
            return "true".equals(hmpParams.get(PROP_ANDROID));
        }
        
        /**
         * Gets whether the client is an iOS device.
         * 
         * @return <code>true</code> if iOS device is used, <code>false</code> otherwise
         */
        private boolean isIOSDevice()
        {
            return "true".equals(hmpParams.get(PROP_IOS));
        }
        
        /**
         * Gets whether the client is a mobile device.
         * 
         * @return <code>true</code> if a mobile device is used, <code>false</code> otherwise
         */
        private boolean isMobileDevice()
        {
            return "true".equals(hmpParams.get(PROP_MOBILE));
        }
        
    }   // VaadinUILauncher
    
    /**
     * Handles the upload of a file.
     * 
     * @author Stefan Wurm
     */
    private static final class VaadinRequestHandler implements RequestHandler 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the file handle. **/ 
        private IFileHandle fileHandle = null;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        public boolean handleRequest(VaadinSession pSession, VaadinRequest pRequest, VaadinResponse pResponse) throws IOException
        {
            if (fileHandle != null)
            {
                try 
                {
                    final DownloadStream stream = new DownloadStream(fileHandle.getInputStream(), "", fileHandle.getFileName());
                    
                    if (stream.getParameter("Content-Disposition") == null) 
                    {
                        // Content-Disposition: attachment generally forces download
                        stream.setParameter("Content-Disposition",
                                "attachment; filename=\"" + stream.getFileName() + "\"");        
                        
                        stream.setContentType("application/octet-stream;charset=UTF-8");
                    }

                    stream.writeResponse(pRequest, pResponse);
                    
                    fileHandle = null;
                    
                    return true; // We wrote a response
                
                }
                finally
                {
                    fileHandle = null;
                }
            
            }
            else
            {
                return false;
            }
        }
        
    }   // VaadinRequestHandler     
    
    /**
     * Receives an Upload.
     * 
     * @author Stefan Wurm
     */
    private static final class UploadReceiver implements Upload.Receiver 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** The uploaded file. **/
        private File uploadFile = null;

        /** The file output stream. */
        private FileOutputStream fos = null;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public OutputStream receiveUpload(String pFileName, String pMimeType)
        {
            try 
            {
                String sExt = FileUtil.getExtension(pFileName);
                
                if (StringUtil.isEmpty(sExt))
                {
                    sExt = null;
                }
                
                uploadFile = java.io.File.createTempFile("upload_" + FileUtil.getName(pFileName), sExt);

                fos = new FileOutputStream(uploadFile);
            } 
            catch (IOException e)
            {
                ExceptionHandler.raise(e);
                
                return null;
            }

            return fos;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
        
        /**
         * Deletes the cached file.
         */
        public void delete()
        {
            free();

            if (uploadFile != null)
            {
                if (!uploadFile.delete())
                {
                    uploadFile.deleteOnExit();
                }
                
                uploadFile = null;
            }
        }
        
        /**
         * Frees all used resources.
         */
        public void free()
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (Exception e)
                {
                    //nothing to be done
                }
                finally
                {
                    fos = null;
                }
            }           
        }
        
    }   // UploadReceiver
    
    /**
     * The <code>ApplicationConfig</code> is the global configuration for an application.
     * It contains startup information.
     * 
     * @author Ren� Jahn
     */
    public final class ApplicationConfig
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the main class name. */
        private String main;
        
        /** the path to the configuration file. */
        private String config;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>ApplicationConfig</code>.
         * 
         * @param pMain the main class name
         * @param pConfig the path to the configuration file
         */
        public ApplicationConfig(String pMain, String pConfig)
        {
            main = pMain;
            config = pConfig;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

        /**
         * Gets the main class name.
         * 
         * @return the class name
         */
        public String getClassName()
        {
            return main;
        }
        
        /**
         * Gets the path to the configuration file.
         * 
         * @return the config path
         */
        public String getConfigPath()
        {
            return config;
        }
        
    }   // ApplicationConfig
    
    /**
     * The <code>InternalConnectorTracker</code> stores whether the response was already written.
     * 
     * @author Ren� Jahn
     */
    public static class InternalConnectorTracker extends ConnectorTracker
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** the UI. */
        private VaadinUI ui;
        
        /** whether invoke later was executed. */
        private boolean bExecuted = false;
        
        /** whether performInvokeLater should be ignored. */
        private boolean bIgnore = false;
        
        /** whether we should unregister the UI. */
        private boolean bUnregisterUI = false;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of <code>InternalConnectorTracker</code> for the given UI.
         * 
         * @param pUI the connected UI
         */
        public InternalConnectorTracker(VaadinUI pUI)
        {
            super(pUI);
            
            ui = pUI;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Override
        public Collection<ClientConnector> getDirtyConnectors()
        {
            if (!bIgnore && !isWritingResponse() && !bExecuted)
            {
                if (ui.launcher != null)
                {
                    VaadinFactory fact = ui.launcher.getFactory();
        
                    if (fact != null)
                    {
                        ui.executePostConnectorTasks();
                        
                        fact.performInvokeLater();
                    
                        bExecuted = true;
                    }
                }
                
                ui.writeCookies();
            }
            
            return super.getDirtyConnectors(); 
        }

        @Override
        public boolean hasDirtyConnectors() 
        {
            //don't perform invokeLater
            bIgnore = true;
            
            try
            {
                return super.hasDirtyConnectors();
            }
            finally
            {
                bIgnore = false;
            }
        }

        @Override
        public void setWritingResponse(boolean pWritingResponse)
        {
            //we need the factory in any case, because LegacyUidlWriter via push is out of our before/after mechanism
            if (pWritingResponse)
            {
                if (UIFactoryManager.getFactory() == null)
                {
                    bUnregisterUI = true;
                    
                    ui.notifyBeforeUI();
                }
            }
            else if (bUnregisterUI)
            {
                //only unregister if UI was registered from this tracker
                bUnregisterUI = false;
                
                ui.notifyAfterUI();
            }
            
            super.setWritingResponse(pWritingResponse);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Sets executed flag.
         * 
         * @param pExecuted <code>false</code> to reset execution
         */
        public void setExecuted(boolean pExecuted)
        {
            bExecuted = pExecuted;
        }
        
        /**
         * Gets whether invoke later was executed.
         * 
         * @return <code>true</code> if it was executed, <code>false</code> otherwise
         */
        public boolean isExecuted()
        {
            return bExecuted;
        }
        
    }   // InternalConnectorTracker   
    
}   // VaadinUI
