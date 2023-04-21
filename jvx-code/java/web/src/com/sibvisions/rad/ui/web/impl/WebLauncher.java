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
 * 02.12.2009 - [HM] - creation
 * 24.01.2011 - [JR] - #267: setComponentData: prepare link popups
 * 07.11.2011 - [JR] - use the context class loader for config and app loading
 * 17.07.2012 - [JR] - configurable factory class
 * 17.01.2013 - [JR] - dispose overwritten
 * 16.02.2013 - [JR] - getRegistryChanges check null
 *                   - prefixed initial registry keys with <default> (constructor)
 * 17.02.2013 - [JR] - finalize implemented 
 * 12.06.2013 - [JR] - has/clearDownloadParameter, has/clearUploadParameter implemented       
 * 27.09.2013 - [JR] - handleException throws RuntimeException
 * 21.12.2016 - [JR] - #1714 (JVx): debug log           
 */
package com.sibvisions.rad.ui.web.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import javax.rad.application.IApplication;
import javax.rad.application.IConnectable;
import javax.rad.application.IContent;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.application.IWorkScreen;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIResource;
import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.IDataSource;
import javax.rad.model.IRowDefinition;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IFactory;
import javax.rad.ui.ILayout;
import javax.rad.ui.IRectangle;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.util.EventHandler;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.web.impl.component.WebButton;
import com.sibvisions.rad.ui.web.impl.container.WebFrame;
import com.sibvisions.rad.ui.web.impl.control.WebTable;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ChangedHashtable;
import com.sibvisions.util.IInvalidator;
import com.sibvisions.util.IValidatable;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.WeakIdentityHashMap;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.TextType;
import com.sibvisions.util.type.TimeZoneUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * Web server implementation of {@link ILauncher}.
 * 
 * @author Martin Handsteiner
 */
public class WebLauncher extends WebFrame
						 implements ILauncher
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** parameter for download URL. */
    public static final String 	PARAM_DOWNLOADURL = "Application.downloadUrl";

    /** parameter for upload URL. */
    public static final String 	PARAM_UPLOADURL = "Application.uploadUrl";
    
	/** the root node identifier for application configuration xml files. */
	private static final String CONFIG_ROOT_NODE = "application";

	/** the services path. */
	public static final String 	SERVICES_PATH = "/services/";
	
	/** the upload path. */
	private String sDownloadPath = SERVICES_PATH + "Download";
	
	/** the download path. */
	private String sUploadPath = SERVICES_PATH + "Upload";
	
	/** the unique launcher identifier. */
	private String sUniqueName;
	
	/** the {@link UILauncher} instance. */
	private UILauncher uilauncher;
	
	/** the application. */
	private IApplication application = null;
	
	/** the locale. */
	private Locale locale = null;
	
	/** the timeZone. */
	private TimeZone timeZone = null;
	
	/** the configuration of an application. */
	private XmlNode xmnAppConfig = null;
	
	/** the currently available components added to this launcher (cached by component id). */
	private HashMap<String, WebComponent> hmpCurrentCompsById = new HashMap<String, WebComponent>();
	
	/** the currently available components added to this launcher (cached by component name). */
	private HashMap<String, WebComponent> hmpCurrentCompsByName = new HashMap<String, WebComponent>();
	
	/** the currently available datarows. */
	private HashMap<String, WeakReference<IDataRow>> hmpCurrentDataRows = new HashMap<String, WeakReference<IDataRow>>();
	
	/** the currently available datarows by name. */
	private WeakIdentityHashMap<IDataRow, String> hmpCurrentDataRowNames = new WeakIdentityHashMap<IDataRow, String>();
	
	/** the only registered datarows. */
	private WeakIdentityHashMap<IDataRow, Boolean> hmpOnlyRegistered = new WeakIdentityHashMap<IDataRow, Boolean>();

	/** the currently available contents, cached per prefix. */
	private HashMap<String, WebComponent> hmpCurrentContent = new HashMap<String, WebComponent>();
	
	/** the available components since last change detection, added to this launcher (cached by component id). */
	private HashMap<String, WebComponent> hmpCompsById = null;
	
	/** the available components since last change detection, added to this launcher (cached by component name). */
	private HashMap<String, WebComponent> hmpCompsByName = null;
	
	/** all known components. */
	private WeakIdentityHashMap<WebComponent, WebComponent> hmpAllComponents = new WeakIdentityHashMap<WebComponent, WebComponent>();
	
	/** the push handler. */
	private List<WeakReference<IPushHandler>> liPushHandler = new ArrayUtil<WeakReference<IPushHandler>>();
	
	/** components marked for removal. */ 
	private HashSet<String> hstRemoveMark = new HashSet<String>();
	
	/** the changed parameters. */
	private ChangedHashtable<String, String> chtParameters;

	/** the components finalized. */
	private ArrayList<String> finalized = new ArrayList<String>();

	/** the changed hashtable for registry values. */
	private ChangedHashtable<String, String> chtRegistry = null;
	
	/** the additional information for upload. */
	private HashMap<Object, Object[]> hmpUpload = null;
	
	/** current changes. */
	private ChangedComponents changes = null;

	/** the remote file handle key if an upload should occur. */
	private List<String> liUploadParameter = null;
	
	/** the remote file handle key if a download should occur. */
	private List<String> liDownloadParameter = null;
	
	/** the target if an document should be shown. */
	private List<String> liDocumentParameter = null;
	
	/** upload sync. */
	private Object oSyncUpload = new Object();
	
	/** download sync. */
	private Object oSyncDownload = new Object();

	/** document sync. */
	private Object oSyncDocument = new Object();
	
	/** the restate id. */
	private Long lRestateId = Long.valueOf(0);
	
	/** the environment name. */
	private String sEnvironmentName;
	
	/** the last datarow cleanup time. */
	private long lLastDataRowCleanup;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingApplication</code> with the desired
	 * application.
	 * 
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pResourceName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file
	 * @param pRegistry initial registry params 				
	 */
	public WebLauncher(String pApplicationClassName, 
					   String pResourceName, 
					   Hashtable<String, String> pParams, 
			           Hashtable<String, String> pRegistry)
	{
		this(pApplicationClassName, pResourceName, pParams, pRegistry, null);
	}
	/**
	 * Creates a new instance of <code>SwingApplication</code> with the desired
	 * application.
	 * 
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pResourceName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file
	 * @param pRegistry initial registry params 		
	 * @param pEnvironmentName the optional environment name		
	 */
	protected WebLauncher(String pApplicationClassName, 
						  String pResourceName, 
						  Hashtable<String, String> pParams, 
				          Hashtable<String, String> pRegistry,
				          String pEnvironmentName)
	{
		if (StringUtil.isEmpty(pEnvironmentName))
		{
			sEnvironmentName = ILauncher.ENVIRONMENT_HEADLESS;
		}
		else
		{
			sEnvironmentName = pEnvironmentName;
		}
		
        //create a unique identifier
		sUniqueName = UUID.randomUUID().toString();
		
        loadConfiguration(pResourceName);
		
		updateConfiguration(xmnAppConfig, pParams);
		
		//use initial registry parameters
		if (pRegistry != null)
		{
			chtRegistry = new ChangedHashtable<String, String>();
			
			for (Map.Entry<String, String> entry : pRegistry.entrySet())
			{
				chtRegistry.put("<default>." + entry.getKey(), entry.getValue(), false);
			}
		}

		WebFactory factory;
		
		try
		{
			factory = (WebFactory)Reflective.construct(Thread.currentThread().getContextClassLoader(), getParameter(ILauncher.PARAM_UIFACTORY), this);
		}
		catch (Throwable th)
		{
			factory = new WebFactory(this);
			
            LoggerFactory.getInstance(WebLauncher.class).debug("Fallback to WebFactory", th);
		}
		
		UIFactoryManager.registerThreadFactoryInstance(factory);

		setFactory(factory);
    	
    	uilauncher = new UILauncher(this);
    	
    	try
    	{
    		chtParameters = new ChangedHashtable<String, String>(getParameters());

    		application = createApplication(uilauncher, Thread.currentThread().getContextClassLoader(), pApplicationClassName);    		

    		uilauncher.setTitle(application.getName());
        	  
        	uilauncher.add(application, IBorderLayout.CENTER);
    	}
    	catch (Throwable th)
    	{
    		uilauncher.handleException(th);
    	}
    	
    	eventWindowClosing().setDefaultListener(this, "dispose");

    	uilauncher.pack();
    	
    	uilauncher.setVisible(true);

    	if (application != null)
    	{
    		application.notifyVisible();
    	}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getParameter(String pName)
	{
		XmlNode xmn = xmnAppConfig.getNode(pName);
		
		
		if (xmn != null)
		{
			String sValue = LauncherUtil.replaceParameter(xmn.getValue(), this);
			
			if (sValue != null)
			{
				sValue = sValue.replace("\\n", "\n");
				sValue = sValue.replace("<br>", "\n");
			}
			
			return sValue;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setParameter(String pName, String pValue)
	{
		xmnAppConfig.setNode(pName, pValue);

		//possible during application creation!
		if (chtParameters != null)
		{
			if (pValue == null)
			{
				chtParameters.remove(pName);
			}
			else
			{
				chtParameters.put(pName, pValue);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void showDocument(String pDocumentName, IRectangle pBounds, String pTarget)  throws Throwable
	{
		String sParam = pDocumentName + ";" + 
		         	    (pBounds == null ? "" : ((WebResource)pBounds.getResource()).getAsString()) + ";" +
		                (pTarget == null ? "" : pTarget);
		
		synchronized (oSyncDocument)
		{
			liDocumentParameter = addParameter(liDocumentParameter, sParam);
		}
	}

	/**
	 * {@inheritDoc}
	 */
    public void showFileHandle(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
    {
		RemoteFileHandle tempFile;
		if (pFileHandle instanceof RemoteFileHandle)
		{
			tempFile = (RemoteFileHandle)pFileHandle;
		}
		else
		{
        	tempFile = new RemoteFileHandle(pFileHandle.getFileName(), RemoteFileHandle.createObjectCacheKey());
    		
        	tempFile.setContent(pFileHandle.getInputStream());
		}

		String sParameter = createDownloadURL() + "KEY=" + tempFile.getObjectCacheKey() + ";" + 
                								  "FILENAME=" + CodecUtil.encodeURLParameter(tempFile.getFileName()) + ";" + 
		                                          (pBounds == null ? "" : ((WebResource)pBounds.getResource()).getAsString()) + ";" + 
		                                          (pTarget == null ? "" : pTarget);
		
		synchronized (oSyncDownload)
		{
			liDownloadParameter = addParameter(liDownloadParameter, sParameter);
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public void showFileHandle(IFileHandle pFileHandle) throws Throwable
    {
    	showFileHandle(pFileHandle, null, null);
    }

	/**
	 * {@inheritDoc}
	 */
    public void saveFileHandle(IFileHandle pFileHandle, String pTitle) throws Throwable
    {
    	showFileHandle(pFileHandle, null, uilauncher.translate(pTitle == null ? "Save as..." : pTitle));
    }

	/**
	 * {@inheritDoc}
	 */
    public void getFileHandle(IFileHandleReceiver pFileHandleReceiver, String pTitle) throws Throwable
    {    	
    	Object key = RemoteFileHandle.createObjectCacheKey();

    	String applicationName;
		if (application instanceof IConnectable)
		{
			applicationName = ((IConnectable)application).getConnection().getApplicationName();
		}
		else
		{
			applicationName = getParameter(ILauncher.PARAM_APPLICATIONNAME); 
		}
		
		String sParameter = createUploadURL() + "APPLICATION=" + applicationName + "&KEY=" + key + ";" +
						                       uilauncher.translate(pTitle == null ? "Open file" : pTitle) + ";" +
						                       uilauncher.translate("Please choose the file:") + ";" +
						                       uilauncher.translate("Cancel") + ";" +
						                       uilauncher.translate("Upload");
		
    	UploadFile uploadFile = new UploadFile(key);

    	ObjectCache.put(key, uploadFile);
    	
		synchronized (oSyncUpload)
		{
			liUploadParameter = addParameter(liUploadParameter, sParameter);
			
	    	if (hmpUpload == null)
	    	{
	    		hmpUpload = new HashMap<Object, Object[]>();
	    	}
	    	
	    	hmpUpload.put(key, new Object[] {sParameter, pFileHandleReceiver, uploadFile});
		}
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
    	
    	String sValue = chtRegistry.get(sName + "." + pKey);
    	
    	if (sValue != null)
    	{
    		return sValue;
    	}
    	else
    	{
    		return chtRegistry.get("<default>." + pKey);
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public String getEnvironmentName()
    {
    	return sEnvironmentName;
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
    
	/**
	 * {@inheritDoc}
	 */
	public void handleException(Throwable pThrowable)
	{
		//throw it because we are a headless UI and otherwise the caller has no chance to handle
		//such exceptions!
		throw new RuntimeException(pThrowable);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		synchronized(this)
		{
			if (application != null)
			{
				try
				{
					application.notifyDestroy();
				}
				catch (Exception e)
				{
					//force closing the application
					LoggerFactory.getInstance(getClass()).info("Forced application destroy failed", e);
				}
			}

			synchronized (liPushHandler)
			{
				//notify push handler about close
				for (int i = 0, cnt = liPushHandler.size(); i < cnt; i++)
				{
					CommonUtil.close(liPushHandler.get(i));
				}
				
				liPushHandler.clear();
			}
			
			try
			{
                WebFactory factory = ((WebFactory)getFactory());
                
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

		WindowHandler windowClosed = (WindowHandler)getProperty("windowClosed", null);
		
		//send event here, otherwise no one will send this event!
		if (windowClosed != null)
		{
            WebFactory factory = ((WebFactory)getFactory());
            
            if (factory != null)
            {
            	factory.synchronizedDispatchEvent(windowClosed, new UIWindowEvent(getEventSource(), 
					                                                              UIWindowEvent.WINDOW_CLOSED,
														 						  System.currentTimeMillis(), 0));
            }
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isDisposed()
	{
		return super.isDisposed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalize() throws Throwable
	{
		if (application != null)
		{
			try
			{
				application.notifyDestroy();
			}
			catch (Throwable th)
			{
				//nothing to be done
			}
			
			application = null;
		}
		
		super.finalize();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the unique Launcher name.
	 * 
	 * @return the unique name
	 */
	public String getUniqueName()
	{
		return sUniqueName;
	}
	
	/**
	 * Gets all currently available parameters.
	 * 
	 * @return the map parameter name/value pairs
	 */
	public Map<String, String> getParameters()
	{
		HashMap<String, String> hmpResult = new HashMap<String, String>();

		List<XmlNode> liNodes = xmnAppConfig.getNodes();
		
		fillParameter(hmpResult, null, liNodes);
		
		return hmpResult;
	}
	
	/**
	 * Gets all changed parameters since last check.
	 * 
	 * @return all changed parameters
	 */
	public List<Map.Entry<String, String>> getChangedParameters()
	{
		return chtParameters.getChanges();
	}
	
	/**
	 * Fills a list of nodes as key/value pairs into a map.
	 * 
	 * @param pParameter the parameter map
	 * @param pPrefix the parameter name prefix. A prefix will be used for the parameter name and an additional <code>/</code> will be added
	 * @param pNodes the list of nodes to use
	 */
	private void fillParameter(HashMap<String, String> pParameter, String pPrefix, List<XmlNode> pNodes)
	{
		List<XmlNode> liSubNodes;
		
		for (XmlNode node : pNodes)
		{
			String sValue = node.getValue();

			liSubNodes = node.getNodes();
			
			String sName;
			
			if (pPrefix != null)
			{
				sName = pPrefix + "/" + node.getName(); 
			}
			else
			{
				sName = node.getName();
			}
			if (!liSubNodes.isEmpty())
			{
				if (!StringUtil.isEmpty(sValue))
				{
					pParameter.put(sName, sValue);
				}
				
				fillParameter(pParameter, sName, liSubNodes);
			}
			else
			{
				pParameter.put(sName, sValue);
			}
		}
	}
	
	/**
	 * Gets the component by id, from the current component cache.
	 * 
	 * @param pId the id.
	 * @return the WebComponent.
	 */
	public synchronized WebComponent getComponentById(String pId)
	{
		if (hmpCurrentCompsById == null)
		{
			return null;
		}
		else
		{
			return hmpCurrentCompsById.get(pId);
		}
	}
	
	/**
	 * Gets the component by name, from the current component cache.
	 * 
	 * @param pName the name.
	 * @return the WebComponent.
	 */
	public synchronized WebComponent getComponentByName(String pName)
	{
		if (hmpCurrentCompsByName == null)
		{
			return null;
		}
		else
		{
			return hmpCurrentCompsByName.get(pName);
		}
	}	
	
	/**
	 * Gets a datarow from the cache and cleans the cache if necessary.
	 * 
	 * @param pName the name
	 * @return the datarow or <code>null</code> if no datarow with given name was found
	 */
	public synchronized IDataRow getDataRowByName(String pName)
	{
		WeakReference<IDataRow> wref = hmpCurrentDataRows.get(pName);
		
		if (wref != null)
		{
			IDataRow dr = wref.get();
			
			if (dr != null)
			{
				return dr;
			}
			else
			{
				cleanupDataRowCache();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a datarow from the cache.
	 * 
	 * @param pName the name
	 * @return the datarow or <code>null</code> if no datarow with given name was found
	 */
	protected synchronized IDataRow getDataRowByNameIntern(String pName)
	{
		WeakReference<IDataRow> wref = hmpCurrentDataRows.get(pName);
		
		if (wref != null)
		{
			IDataRow dr = wref.get();
			
			if (dr != null)
			{
                return dr;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the name of a datarow from the cache.
	 * 
	 * @param pDataRow the datarow
	 * @return the cached name
	 */
	public String getDataRowName(IDataRow pDataRow)
	{
		return hmpCurrentDataRowNames.get(pDataRow);
	}
	
	/**
	 * Gets whether the given datarow is already in our cache.
	 * 
	 * @param pDataRow the datarow
	 * @return <code>true</code> if already in cache, <code>false</code> otherwise
	 */
	protected boolean isCached(IDataRow pDataRow)
	{
		return hmpCurrentDataRowNames.containsKey(pDataRow);
	}
	
	/**
	 * Gets the cach of data rows.
	 * 
	 * @return the data row cache
	 */
	protected HashMap<String, WeakReference<IDataRow>> getCachedDataRows()
	{
		return hmpCurrentDataRows;
	}
	
	/**
	 * Gets all known data rows.
	 * 
	 * @return the currently known data rows
	 */
	protected synchronized HashMap<String, IDataRow> getDataRows()
	{
		HashMap<String, WeakReference<IDataRow>> hmpClone = new HashMap<String, WeakReference<IDataRow>>(hmpCurrentDataRows);
		
		HashMap<String, IDataRow> hmpResult = new HashMap<String, IDataRow>();

		IDataRow row;
		
		for (Entry<String, WeakReference<IDataRow>> entry : hmpClone.entrySet())
		{
			row = entry.getValue().get();
			
			if (row != null)
			{
				hmpResult.put(entry.getKey(), row);
			}
		}			

		return hmpResult;
	}
	
	/**
	 * Gets the cached content per prefix.
	 * 
	 * @param pPrefix the prefix
	 * @return the content or <code>null</code> if no content was found
	 */
	public synchronized WebComponent getContent(String pPrefix)
	{
		return hmpCurrentContent.get(pPrefix);
	}
	
	/**
	 * Cleans the data row cache. All finalized data rows will be removed from the cache.
	 */
	private synchronized void cleanupDataRowCache()
	{
		HashMap<String, WeakReference<IDataRow>> hmpClone = new HashMap<String, WeakReference<IDataRow>>(hmpCurrentDataRows);
		
		for (Entry<String, WeakReference<IDataRow>> entry : hmpClone.entrySet())
		{
			if (entry.getValue().get() == null)
			{
				removeDataRowFromCache(entry.getKey());
			}
		}			
	}
	
	/**
	 * Removes all references to the given screen. Especially cached datarows will be removed.
	 * 
	 * @param pScreen the screen
	 */
	public synchronized void removeReferences(IWorkScreen pScreen)
	{
		String sPrefix = getPrefix(pScreen) + "/";
		
		HashMap<String, WeakReference<IDataRow>> hmpClone = new HashMap<String, WeakReference<IDataRow>>(hmpCurrentDataRows);
		
		for (Entry<String, WeakReference<IDataRow>> entry : hmpClone.entrySet())
		{
			if (entry.getKey().startsWith(sPrefix))
			{
				if (!removeDataRowFromCache(entry.getValue().get()))
				{
					removeDataRowFromCache(entry.getKey());
				}
			}
		}	
		
		HashMap<WebComponent, WebComponent> hmpCloneAll = new HashMap<WebComponent, WebComponent>(hmpAllComponents);

		WebComponent comp;
		
		String sCachedPrefix;
		
		for (Entry<WebComponent, WebComponent> entry : hmpCloneAll.entrySet())
		{
			comp = entry.getKey();
			
			sCachedPrefix = (String)comp.getObject(IWebFieldConstants.PREFIX); 
			
			if (sCachedPrefix != null && sCachedPrefix.startsWith(sPrefix))
			{
				hmpAllComponents.remove(comp);
			}
		}
	}
	
	/**
	 * Component has to be finalized.
	 * 
	 * @param pId the id.
	 */
	public void finalizeComponent(String pId)
	{
		synchronized (finalized)
		{
			finalized.add(pId);
		}
	}
	
	/**
	 * Gets the download parameter list, if a download should occur.
	 * 
	 * @return the download parameter list, if a download should occur.
	 */
	public List<String> getDownloadParameter()
	{
		synchronized (oSyncDownload)
		{
			List<String> parameter = liDownloadParameter;
	
			clearDownloadParameter();
	
			return parameter;
		}
	}

	/**
	 * Gets whether at least one download parameter is set. A parameter is set if {@link #showFileHandle(IFileHandle)} or 
	 * {@link #saveFileHandle(IFileHandle, String)} was called.
	 * 
	 * @return <code>true</code> if set, <code>false</code> otherwise
	 * @see #getDownloadParameter()
	 */
	public boolean hasDownloadParameter()
	{
		synchronized (oSyncDownload)
		{
			return liDownloadParameter != null && !liDownloadParameter.isEmpty();
		}
	}
	
	/**
	 * Clears the download parameter list.
	 */
	public void clearDownloadParameter()
	{
		synchronized (oSyncDownload)
		{
			liDownloadParameter = null;
		}
	}
	
	/**
	 * Gets the document parameter list, if a show document should occur.
	 * 
	 * @return the document parameter list, if a show document should occur.
	 */
	public List<String> getDocumentParameter()
	{
		synchronized (oSyncDocument)
		{
			List<String> parameter = liDocumentParameter;
	
			clearDocumentParameter();
	
			return parameter;
		}
	}

	/**
	 * Gets whether at least one document parameter is set. A parameter is set if {@link #showDocument(String, IRectangle, String)} 
	 * was called.
	 * 
	 * @return <code>true</code> if set, <code>false</code> otherwise
	 * @see #getDocumentParameter()
	 */
	public boolean hasDocumentParameter()
	{
		synchronized (oSyncDocument)
		{
			return liDocumentParameter != null && !liDocumentParameter.isEmpty();
		}
	}
	
	/**
	 * Clears the document parameter list.
	 */
	public void clearDocumentParameter()
	{
		synchronized (oSyncDocument)
		{
			liDocumentParameter = null;
		}
	}	
	
	/**
	 * Gets the upload parameter list, if an upload should occur.
	 * 
	 * @return the upload parameter, if an upload should occur.
	 */
	public List<String> getUploadParameter()
	{
		synchronized (oSyncUpload)
		{
			List<String> parameter = liUploadParameter;
			
			liUploadParameter = null;
	
			return parameter;
		}
	}
	
	/**
	 * Gets whether the upload parameter is set. A parameter is set if {@link #getFileHandle(IFileHandleReceiver, String)}
	 * was called.
	 * 
	 * @return <code>true</code> if set, <code>false</code> otherwise
	 * @see #getUploadParameter()
	 */
	public boolean hasUploadParameter()
	{
		synchronized (oSyncUpload)
		{
			return liUploadParameter != null;
		}
	}
	
	/**
	 * Clears the upload parameter list.
	 * 
	 * @param pKey the cache key or <code>null</code> to clear all
	 */
	public void clearUploadParameter(Object pKey)
	{
		synchronized (oSyncUpload) 
		{
			if (hmpUpload != null)
			{
				if (pKey != null)
				{
					Object[] content = hmpUpload.remove(pKey);
					
					if (liUploadParameter != null && content != null)
					{
						liUploadParameter.remove(content[0]);
					}
					
					if (hmpUpload.isEmpty() 
						|| liUploadParameter == null 
						|| liUploadParameter.isEmpty())
					{
						hmpUpload = null;
						liUploadParameter = null;
					}
				}
				else
				{
					liUploadParameter = null;
					hmpUpload = null;
				}
			}
		}
	}
	
	/**
	 * Upload is finished.
	 * 
	 * @param pKey the cache key or <code>null</code> to finish all
	 */
	public void uploadFinished(Object pKey)
	{
		synchronized (oSyncUpload)
		{
			if (hmpUpload != null)
			{
				if (pKey != null)
				{
					Object[] values = hmpUpload.get(pKey);
					
					if (values != null)
					{
						((IFileHandleReceiver)values[1]).receiveFileHandle((IFileHandle)values[2]);
						
						clearUploadParameter(pKey);
					}
				}
				else
				{
					for (Object[] values : hmpUpload.values())
					{
						if (values != null)
						{
							((IFileHandleReceiver)values[1]).receiveFileHandle((IFileHandle)values[2]);
						}						
					}
					
					clearUploadParameter(null);
				}
			}
			else
			{
				//be sure that everything is sync
				liUploadParameter = null;
			}
		}
	}
	
	/**
	 * Restates the launcher. All changes will be cleared and rebuilt on next access.
	 */
	public synchronized void restate()
	{
		hmpCurrentCompsById.clear();
		hmpCurrentCompsByName.clear();
		hmpCurrentContent.clear();
		hmpCurrentDataRows.clear();
		hmpCurrentDataRowNames.clear();

		hmpCompsById = null;
		hmpCompsByName = null;
		
		lRestateId = Long.valueOf(System.currentTimeMillis());
		
		restate(this);
	}
	
	/**
	 * Restates the given component with all sub components if necessary.
	 *
	 * @param pComponent the component to restate
	 */
	protected void restate(WebComponent pComponent)
	{
		WebComponent compFillIn = getFillInComponent(pComponent);
		
		restateProperties(compFillIn);
		
		if (isFillInComponentHierarchy(compFillIn))
		{
			List<WebComponent> liAdditionalComps = compFillIn.getAdditionalComponents();

			for (int i = 0, cnt = liAdditionalComps.size(); i < cnt; i++)
			{
				WebComponent component = liAdditionalComps.get(i);
				
				if (component.isVisible() || !(component instanceof IWindow))
				{
					restate(component);
				}
			}
		
			if (compFillIn instanceof IWebContainer)
			{
				IWebContainer contFillIn = (IWebContainer)compFillIn;
			
				for (int i = 0, count = contFillIn.getComponentCount(); i < count; i++)
				{
					WebComponent component = (WebComponent)contFillIn.getComponent(i);

					restate(component);
				}
			}
		}
	}
	
	/**
	 * Restates all relevant properties of given component.
	 * 
	 * @param pComponent the component
	 */
	protected void restateProperties(WebComponent pComponent)
	{
		//to check if an object is from the same restate (e.g. re-add a component later)
		pComponent.putObject(IWebFieldConstants.RESTATEID, lRestateId);
		
		//reset cached properties
		pComponent.putObject(IWebFieldConstants.INITIALIZED, null);
		pComponent.putObject(IWebFieldConstants.INDEXOF, null);
		
		pComponent.markAllPropertiesChanged();
	}	
	
	/**
	 * Get the changed components since the last call.
	 * 
	 * @return the changed components.
	 */
	public synchronized ChangedComponents getChangedComponents()
	{	
		//cleanup isn't that important, so once per minute is enough
		if (lLastDataRowCleanup + 60000 < System.currentTimeMillis())
		{
			cleanupDataRowCache();
			
			lLastDataRowCleanup = System.currentTimeMillis();
		}
		
		ArrayList<Map<String, Object>> changedComponents = new ArrayList<Map<String, Object>>();
		ArrayList<String> allComponents = new ArrayList<String>();
		
		HashMap<String, int[]> changeIndex = new HashMap<String, int[]>();
		
		HashMap<String, WebComponent> newCompsById   = new HashMap<String, WebComponent>();
		HashMap<String, WebComponent> newCompsByName = new HashMap<String, WebComponent>();
		
		HashMap<String, WeakReference<IDataRow>> newDataRows = new HashMap<String, WeakReference<IDataRow>>();
		
		HashSet<String> usedDataRowNames = new HashSet<String>();
		
		hmpCurrentContent.clear();
		hmpCurrentCompsById.clear();
		hmpCurrentCompsByName.clear();
				
		fillInChangedComponents(this, changedComponents, allComponents, changeIndex,
				                newCompsById, newCompsByName, newDataRows, usedDataRowNames, 
				                null, false);

		int[] index = new int[] {changedComponents.size(), -1};
		
		//all removed components in this check
		HashMap<String, WebComponent> oldRemovedById = new HashMap<String, WebComponent>();
		
		if (hmpCompsById != null)
		{
			WebComponent comp;
			WebComponent compFillIn;

			//It's possible that the list contains a component which is already added but one parent is not added.
			//Such components won't be sent to the client because only the removed component is important.
			//
			//But it's also possible that a component is invisible and the component-tree is still added.
			//It's important to send such components to the client
			
			//add all components to the change list which should be removed because they are not
			//available via object tree
			for (Entry<String, WebComponent> entry : hmpCompsById.entrySet())
			{
				comp = entry.getValue();
				compFillIn = getFillInComponent(comp);
				
				//not necessary to handle "intern" components because the fill-in component is also available
				if (comp == compFillIn)
				{
				    //don't remove from parent if parent is removed
					
					String sId = entry.getKey();
					
					if (comp.getParentIntern() == null)
					{
						Map<String, Object> map = new LinkedHashMap<String, Object>();
						map.put(IWebFieldConstants.REMOVE, Boolean.TRUE.toString());
						map.put("id", sId);
		
						changedComponents.add(map);
						
						oldRemovedById.put(sId, comp);
						
						hmpAllComponents.remove(compFillIn);
						
						hstRemoveMark.remove(sId);
					}
					else
					{
						//mark for later use in fillIn because we won't re-add such marked components
						comp.setProperty(IWebFieldConstants.REMOVE_LATER, Boolean.TRUE, true);

						if (comp.isChanged("visible") && !comp.isVisible())
						{
							comp.clearChangedProperty("visible");
							
							//visibility changed -> check if component-tree is "still added"
							
							IComponent cmpParent = comp.getParentIntern();
							
							while (cmpParent != null && !(cmpParent instanceof ILauncher))
							{
								cmpParent = WebComponent.getParent(cmpParent);
							}
								
							if (cmpParent != null)
							{
								//only send component, if not available in the list of changes
								
								Map<String, Object> map;
								
								boolean bFound = false;
								
								for (int i = 0, cnt = changedComponents.size(); i < cnt && !bFound; i++)
								{
									map = changedComponents.get(i);
									
									bFound = sId.equals(map.get("id"));
								}
								
								if (!bFound)
								{
									map = new LinkedHashMap<String, Object>();
									map.put("id", sId);
									map.put("visible", Boolean.FALSE);
									
									changedComponents.add(map);
									
									oldRemovedById.put(sId, comp);
								}
							}
						}
					}
				}
			}
		}

		List<WebComponent> liRemove = new ArrayUtil<WebComponent>();
		
		WebComponent compFillIn;
		WebComponent comp;
		
		//check if a known component is removed, because we don't get the information about removed 
		//components if a parent component is already removed
		for (Entry<WebComponent, WebComponent> entry : hmpAllComponents.entrySet())
		{
			compFillIn = entry.getKey();
			comp = entry.getValue();
			
			if (!(comp instanceof ILauncher)
				&& (compFillIn.getParentIntern() == null || comp.getParentIntern() == null))
			{
				String sOrigId = compFillIn.getComponentId();
				
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put(IWebFieldConstants.REMOVE, Boolean.TRUE.toString());
				map.put("id", sOrigId);

				changedComponents.add(map);
				
				oldRemovedById.put(sOrigId, comp);
				
				liRemove.add(compFillIn);

				hstRemoveMark.remove(sOrigId);
				
			}
		}
		
		//add marked for removal components
		for (String sCompRemoveId : hstRemoveMark)
		{
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put(IWebFieldConstants.REMOVE, Boolean.TRUE.toString());
			map.put("id", sCompRemoveId);

			changedComponents.add(map);
		}
		
		//cleanup
		for (WebComponent compRemove : liRemove)
		{
			hmpAllComponents.remove(compRemove);
		}
		
		HashMap<WebComponent, WebComponent> hmpClone = new HashMap<WebComponent, WebComponent>(hmpAllComponents);
		
		for (WebComponent cmp : hmpClone.keySet())
		{
			if (hstRemoveMark.contains(cmp.getComponentId()))
			{
				hmpAllComponents.remove(cmp);
			}
		}
		
		hstRemoveMark.clear();
		
		index[1] = changedComponents.size();

		if (index[0] != index[1])
		{
			changeIndex.put(IWebFieldConstants.REMOVE, index);
		}

		index = new int[] {index[1], -1};
		
		synchronized (finalized)
		{
			//destroy all objects because the gc finalized them
			for (String componentId : finalized)
			{
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put(IWebFieldConstants.DESTROY, Boolean.TRUE.toString());
				map.put("id", componentId);
				
				changedComponents.add(map);
			}
			
			finalized.clear();
		}
		
		index[1] = changedComponents.size();

		if (index[0] != index[1])
		{
			changeIndex.put(IWebFieldConstants.DESTROY, index);
		}
		
		hmpCompsById = newCompsById;
		hmpCompsByName = newCompsByName;
		
		return new ChangedComponents(changedComponents, newDataRows, usedDataRowNames,
                					 changeIndex, hmpCompsByName, oldRemovedById, changes);
	}
	
	/**
	 * Updates maps with component names and ids.
	 */
	public synchronized void updateComponents()
	{
		hmpCurrentContent.clear();
		hmpCurrentCompsById.clear();
		hmpCurrentCompsByName.clear();
		
		updateCurrentComponents(this, null);
	}
	
	/**
	 * Updates maps with component names and ids, recursive.
	 * 
	 * @param pComponent the start component
	 * @param pNamePrefix the name prefix
	 */
	private void updateCurrentComponents(WebComponent pComponent, String pNamePrefix)
	{
		String sPrefix = pNamePrefix;
		
		IComponent compSource = pComponent.getEventSource();

		WebComponent compFillIn = getFillInComponent(pComponent);

		boolean bIsContent = isContent(compSource);
		
		if (compSource instanceof IApplication
			|| compSource instanceof IWorkScreen
			|| bIsContent)
		{			
			sPrefix = getPrefix(pComponent, compSource, sPrefix, bIsContent);
			
			hmpCurrentContent.put(sPrefix, pComponent);
		}
		
		if (compFillIn != pComponent)
		{
			hmpCurrentCompsById.put(pComponent.getComponentId(), pComponent);
			hmpCurrentCompsByName.put(pComponent.propertyName(), pComponent);
		}

		String sCompId = compFillIn.getComponentId();
		
		hmpCurrentCompsById.put(sCompId, compFillIn);
		hmpCurrentCompsByName.put(compFillIn.propertyName(), compFillIn);
		
		if (hmpCurrentCompsById != null)
		{
			if (compFillIn != pComponent)
			{
				hmpCurrentCompsById.remove(pComponent.getComponentId());
			}
			
			// Remove Component from the old component list, to find out the missing components.
			hmpCurrentCompsById.remove(sCompId);
		}
		
		if (isFillInComponentHierarchy(compFillIn))
		{
			if (compFillIn instanceof IWebContainer)
			{
				IWebContainer contFillIn = (IWebContainer)compFillIn;
			
				for (int i = 0, cnt = contFillIn.getComponentCount(); i < cnt; i++)
				{
					updateCurrentComponents((WebComponent)contFillIn.getComponent(i), sPrefix);
				}
			}
			
			List<WebComponent> liAdditionalComps = compFillIn.getAdditionalComponents();
			
			//additional after "normal" components -> e.g. internal frames
			for (int i = 0, cnt = liAdditionalComps.size(); i < cnt; i++)
			{
				updateCurrentComponents(liAdditionalComps.get(i), sPrefix);
			}
		}		
	}
	
	/**
	 * Gets whether the given component is an {@link IContent}.
	 * 
	 * @param pSource the component
	 * @return <code>true</code> if given component is an {@link IContent}, <code>false</code> otherwise
	 */
	private boolean isContent(IComponent pSource)
	{
		if (pSource instanceof IContent 
			&& !(pSource instanceof IWorkScreen) 
			&& !(pSource instanceof IApplication))
		{
			IComponent comp = pSource;
			
			do
			{
				comp = WebComponent.getParent(comp);
			}
			while (comp != null && !(comp instanceof IWorkScreen));
			
			// if content is part of a work-screen -> ignore
			boolean bIsContent = comp == null;
			
			if (bIsContent)
			{
				comp = pSource;
				
				do
				{
					comp = WebComponent.getParent(comp);
				}
				while (comp != null && !(comp instanceof IContent));
				
				// if content is part of another content -> ignore
				bIsContent = comp instanceof IApplication;	
			}
			
			return bIsContent;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Gets the prefix for the given component.
	 * 
	 * @param pComponent the component
	 * @param pSource the (source) component
	 * @param pPrefix the current prefix
	 * @param pContent whether the component is a content
	 * @return the prefix
	 */
	private String getPrefix(IComponent pComponent, IComponent pSource, String pPrefix, boolean pContent)
	{
		String sName = null;
		
		if (pContent)
		{
			Object compOpener = ((IContent)pSource).getOpener();
			
			if (compOpener instanceof IComponent)
			{
				sName = ((IComponent)compOpener).getName();
				
				if (sName != null)
				{
					sName += "/" + pComponent.getName();
				}
			}
		}
		
		if (sName == null)
		{
			sName = pComponent.getName();
		}
		
		if (pSource instanceof IApplication)
		{
			sName = StringUtil.getText(sName, TextType.LettersDigits);
		}
		
		if (pPrefix == null)
		{
			return sName;
		}
		else
		{
			return pPrefix + "/" + sName;
		}
	}
	
	/**
	 * Fills in the changes.
	 * 
	 * @param pComponent the component.
	 * @param pChangedComponents the change list.
	 * @param pAllComponents the components list.
	 * @param pChangeIndex the change index.
	 * @param pNewCompsById the new components, cached by component ID.
	 * @param pNewCompsByName the new components, cached by component name.
	 * @param pNewDataRows the new data rows, cached by data row name.
	 * @param pUsedDataRowNames the used data row names.
	 * @param pNamePrefix the optional prefix for the component name.
	 * @param pAdditional whether the given component is additional.
	 */
	private void fillInChangedComponents(WebComponent pComponent, ArrayList<Map<String, Object>> pChangedComponents, ArrayList<String> pAllComponents,
										 HashMap<String, int[]> pChangeIndex, HashMap<String, WebComponent> pNewCompsById, 
										 HashMap<String, WebComponent> pNewCompsByName, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
										 HashSet<String> pUsedDataRowNames, String pNamePrefix, boolean pAdditional)
	{
		String sPrefix = pNamePrefix;
		
		IComponent compSource = pComponent.getEventSource();

		WebComponent compFillIn = getFillInComponent(pComponent);

		boolean bIsContent = isContent(compSource);
		
		if (compSource instanceof IApplication
			|| compSource instanceof IWorkScreen
			|| bIsContent)
		{			
			sPrefix = getPrefix(pComponent, compSource, sPrefix, bIsContent);
			
			if (compSource instanceof IApplication)
			{
				preAnalyzeApplication(compFillIn, (IApplication)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
			}
			else if (compSource instanceof IWorkScreen)
			{
				preAnalyzeWorkScreen(compFillIn, (IWorkScreen)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
			}
			else if (bIsContent)
			{
				preAnalyzeContent(compFillIn, (IContent)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
			}
			
			if (compSource instanceof UIComponent)
			{
				((UIComponent)compSource).putObject(IWebFieldConstants.PREFIX, sPrefix);
			}
			
			pComponent.putObject(IWebFieldConstants.PREFIX, sPrefix);
			
			hmpCurrentContent.put(sPrefix, pComponent);
		}
		
		String sCompId = compFillIn.getComponentId();
		
		//creates "the reference" to the screen
		compFillIn.putObject(IWebFieldConstants.PREFIX, sPrefix);
		
		hmpAllComponents.put(compFillIn, pComponent);

		pAllComponents.add(sCompId);
		
		boolean bCompInitialized = ((Boolean)compFillIn.getObject(IWebFieldConstants.INITIALIZED, Boolean.FALSE)).booleanValue();
		
		if (!bCompInitialized)
		{
			compFillIn.putObject(IWebFieldConstants.INITIALIZED, Boolean.TRUE);
			compFillIn.putObject(IWebFieldConstants.RESTATEID, lRestateId);
		}
		else
		{
			Long lCompRestateId = (Long)compFillIn.getObject(IWebFieldConstants.RESTATEID);
			
			//component was already initialized and the restateId is different -> restate, to send all properties again
			
			if (lRestateId != lCompRestateId)
			{
				restate(compFillIn);
			}
		}
		
		//added component can't be removed
		hstRemoveMark.remove(sCompId);		
		
		if (compFillIn != pComponent)
		{
			String sOrigId = pComponent.getComponentId();
			
			pNewCompsById.put(sOrigId, pComponent);
			pNewCompsByName.put(pComponent.propertyName(), pComponent);
			
			hmpCurrentCompsById.put(sOrigId, pComponent);
			hmpCurrentCompsByName.put(pComponent.propertyName(), pComponent);

			hstRemoveMark.remove(sOrigId);
		}

		pNewCompsById.put(sCompId, compFillIn);
		pNewCompsByName.put(compFillIn.propertyName(), compFillIn);
		
		hmpCurrentCompsById.put(sCompId, compFillIn);
		hmpCurrentCompsByName.put(compFillIn.propertyName(), compFillIn);
		
		updateChangedProperties(compFillIn, pComponent);
		
		List<Map.Entry<String, Object>> changedProperties = getChangedProperties(compFillIn, pComponent);

		int iInitialChangedComponentsCount = pChangedComponents.size();
		
		if (changedProperties != null)
		{
			Map.Entry<String, Object> entry;

			boolean bMarkedAsChanged = false;
			
			//remove internal property (if available)
			if (compFillIn.getProperty(IWebFieldConstants.REMOVE_LATER, null) != null)
			{
				for (int i = changedProperties.size() - 1; i >= 0; i--)
				{
					entry = changedProperties.get(i);
					
					if (entry.getKey().equals(IWebFieldConstants.REMOVE_LATER))
					{
						changedProperties.remove(i);
					}
					else if (entry.getKey().equals(IWebFieldConstants.MARK_CHANGED))
					{
						changedProperties.remove(i);
						
						bMarkedAsChanged = true;
					}
				}
			}
			
			//mark as changed is only relevant if there are no other changes than the mark itself
			//size check here is important for later use (not for the if check)
			bMarkedAsChanged &= changedProperties.size() == 0;
			
			if (changedProperties.size() > 0 || bMarkedAsChanged)
			{
				Map<String, Object> mpProperties = new LinkedHashMap<String, Object>();
				
				if (pAdditional && pComponent == compFillIn)
				{
					mpProperties.put(IWebFieldConstants.ADDITIONAL, Boolean.TRUE);
				}
				
				mpProperties.put("id", compFillIn.getComponentId());
				//keep the order (id, name, className, ...)
				mpProperties.put("name", null);
				mpProperties.put("className", null);

				if (compFillIn != pComponent)
				{
					//don't add the information for simple updates
					if (!bCompInitialized)
					{
						mpProperties.put("idComponentRef", pComponent.getComponentId());
						mpProperties.put("nameComponentRef", pComponent.propertyName());
						mpProperties.put("classNameComponentRef", pComponent.getProperty("className", null));
					}
				}

				//keep the order
				mpProperties.put("classNameEventSourceRef", null);

				int iPropertyCountBefore = mpProperties.size();
				
				initProperties(mpProperties, compFillIn);

				if (!bMarkedAsChanged || mpProperties.size() != iPropertyCountBefore)
				{
					String sKey;
					Object oValue;
					
					for (int i = 0, size = changedProperties.size(); i < size; i++)
					{
						entry = changedProperties.get(i);
						
						sKey = entry.getKey();
						oValue = entry.getValue();
						
						checkForDataRow(oValue, compFillIn, pNewDataRows, pUsedDataRowNames, sPrefix);
						
						fillInChangedProperty(mpProperties, compFillIn, sKey, oValue, sPrefix);
					}
					
					//name not changed -> remove from list because we don't need it for the sort-order
					if (mpProperties.get("name") == null)
					{
						mpProperties.remove("name");
					}
					
					//send additional information only once
					//we can't check the className via changedProperties because it contains an Entry (can't use contains("className"))
					if (mpProperties.get("className") != null)
					{
						if (compSource instanceof UIResource)
						{
							if (!bCompInitialized)
							{
								String sName = compSource.getClass().getSimpleName();
								
								if (sName.startsWith("UI"))
								{
									sName = sName.substring(2);
								}
								
								if (!sName.equals(compFillIn.getProperty("className", null)))
								{
									mpProperties.put("classNameEventSourceRef", sName);
								}
							}
						}
					}
					else
					{
						mpProperties.remove("className");
					}
					
					if (mpProperties.get("classNameEventSourceRef") == null)
					{
						mpProperties.remove("classNameEventSourceRef");
					}
					
					pChangedComponents.add(mpProperties);
				}
			}
		}
		else if (hmpCompsById != null && hmpCompsById.get(compFillIn.getComponentId()) == null)
		{
			// If this component is new it has to be sent to the client even if there are no properties changed,
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put(IWebFieldConstants.NEW, Boolean.TRUE.toString());

			if (pAdditional && pComponent == compFillIn)
			{
				map.put(IWebFieldConstants.ADDITIONAL, Boolean.TRUE);
			}
			
			map.put("id", compFillIn.getComponentId());
			map.put("name", compFillIn.propertyName());

			pChangedComponents.add(map);
		}
		
		if (hmpCompsById != null)
		{
			if (compFillIn != pComponent)
			{
				hmpCompsById.remove(pComponent.getComponentId());
			}
			
			// Remove Component from the old component list, to find out the missing components.
			hmpCompsById.remove(sCompId);
		}
		
		int[] iIndex = pChangeIndex.get(sCompId);
		
		if (iIndex == null)
		{
			int iCurrentSize = pChangedComponents.size();

			//no changes -> no index
			if (iInitialChangedComponentsCount != iCurrentSize)
			{
				iIndex = new int[] {iInitialChangedComponentsCount, iCurrentSize};
				
				pChangeIndex.put(sCompId, iIndex);
			}
		}
		
		if (isFillInComponentHierarchy(compFillIn))
		{
			if (compFillIn instanceof IWebContainer)
			{
				IWebContainer contFillIn = (IWebContainer)compFillIn;
			
				for (int i = 0, count = contFillIn.getComponentCount(); i < count; i++)
				{
					WebComponent component = (WebComponent)contFillIn.getComponent(i);
					
					//send the component if hidden but structure has changed or it's not send for the first time. 
					//It's important to send such components because the structure should be right
					if ((component.isVisible() 
						 || component.isChanged("constraints") 
						 || component.getObject(IWebFieldConstants.INITIALIZED) == null) 
						&& !(component instanceof IWindow))
					{
						Integer iOldIndex = component.getObject(IWebFieldConstants.INDEXOF);
						Integer iCurrentIndex = Integer.valueOf(i);
						
						if (iOldIndex == null || !iOldIndex.equals(iCurrentIndex))
						{
							component.putObject(IWebFieldConstants.INDEXOF, iCurrentIndex);
							
							component.setProperty("indexOf", iCurrentIndex);
						}
					
						if (component instanceof WebButton)
						{
							WebButton button = (WebButton)component;
							
							if (button.isDefaultButton())
							{
								IComponent compParent = component.getParentIntern();
								
								while (!(compParent instanceof IWindow))
								{
									compParent = WebComponent.getParent(compParent);
								}
								
								if (compParent instanceof WebComponent)
								{
									button.setProperty("defaultWindow", ((WebComponent)compParent).getComponentId());
								}
								else
								{
									button.setProperty("defaultWindow", null);
								}
							}
							else if (button.getProperty("defaultWindow", null) != null)
							{
								button.setProperty("defaultWindow", null);
							}
						}
					
						fillInChangedComponents(component, pChangedComponents, pAllComponents, pChangeIndex, pNewCompsById, 
								                pNewCompsByName, pNewDataRows, pUsedDataRowNames, sPrefix, false);
					}
					else if (!component.isVisible() && component.isChanged("visible"))
					{
						//clear change to avoid detection with next fillin
						component.clearChangedProperty("visible");
						
						//no recursion if component was visible and gets invisible now
						Map<String, Object> map = new LinkedHashMap<String, Object>();
						
						map.put("id", component.getComponentId());
						map.put("name", component.propertyName());
						map.put("visible", Boolean.FALSE);

						pChangedComponents.add(map);
					}
				}
			}
			
			List<WebComponent> liAdditionalComps = compFillIn.getAdditionalComponents();
			
			//additional after "normal" components -> e.g. internal frames
			for (int i = 0, cnt = liAdditionalComps.size(); i < cnt; i++)
			{
				WebComponent component = liAdditionalComps.get(i);
				
				if (component.isVisible() || !(component instanceof IWindow))
				{
					fillInChangedComponents(component, pChangedComponents, pAllComponents, pChangeIndex, pNewCompsById, 
							                pNewCompsByName, pNewDataRows, pUsedDataRowNames, sPrefix, true);
				}
			}
		}

		int iCurrentSize = pChangedComponents.size();
		
		//no changes -> no index update
		if (iInitialChangedComponentsCount != iCurrentSize)
		{
			if (iIndex == null)
			{
				iIndex = pChangeIndex.get(sCompId);
			}
			
			//oops, index isn't available but we have "sub" changes -> lets create a new one
			if (iIndex == null)
			{
				iIndex = new int[] {iInitialChangedComponentsCount, iCurrentSize};
				
				pChangeIndex.put(sCompId, iIndex);
			}
			else
			{
				iIndex[1] = iCurrentSize;
			}
		}
		
		if (compSource instanceof IApplication)
		{
			postAnalyzeApplication(compFillIn, (IApplication)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
		}
		else if (compSource instanceof IWorkScreen)
		{
			postAnalyzeWorkScreen(compFillIn, (IWorkScreen)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
		}
		else if (bIsContent)
		{
			postAnalyzeContent(compFillIn, (IContent)compSource, pNewDataRows, pUsedDataRowNames, sPrefix);
		}
	}
	
	/**
	 * Gets the web resource from an object.
	 * 
	 * @param pResource the resource
	 * @return the web resource
	 */
	public static WebResource getResource(Object pResource)
	{
		if (pResource instanceof WebResource)
		{
			return (WebResource) pResource;
		}
		else if (pResource instanceof UIResource)
		{
			return (WebResource)((UIResource)pResource).getUIResource();
		}
		else
		{
			return (WebResource)((UIResource)pResource).getResource();
		}
	}
	
	/**
	 * Gets the prefix of a work-screen.
	 * 
	 * @param pScreen the work-screen
	 * @return the prefix
	 */
	public static String getPrefix(IWorkScreen pScreen)
	{
		if (pScreen instanceof UIComponent)
		{ 
			return (String)((UIComponent)pScreen).getObject(IWebFieldConstants.PREFIX);
		}
		else
		{
			//same logic as in fillInChangedComponents
			WebComponent comp = (WebComponent)getResource(pScreen);
			
			return StringUtil.getText(pScreen.getApplication().getName(), TextType.LettersDigits) + "/" + comp.getName();
		}
	}
	
	/**
	 * Updates the changed properties before the changes will used for fill-in.
	 * 
	 * @param pFillInComponent the component to update
	 * @param pComponent the component
	 */
	protected void updateChangedProperties(WebComponent pFillInComponent, WebComponent pComponent)
	{
		if (pFillInComponent instanceof IWebContainer)
		{
			//Layout handling
			ILayout layout = ((IWebContainer)pFillInComponent).getLayout();
			
			if (layout instanceof WebLayout)
			{
				pFillInComponent.setProperty(IWebFieldConstants.LAYOUTDATA, ((WebLayout)layout).getData((IWebContainer)pFillInComponent));
			}
		}	
		else if (pFillInComponent instanceof WebTable)
		{
			pFillInComponent.setProperty(IWebFieldConstants.COLUMNNAMES, ((WebTable)pFillInComponent).getColumnView().getColumnNames());
		}
		
	}
	
	/**
	 * Gets "merged" changes of fill-in component and original component. Some properties are important from the original
	 * component and not from the fill-in component.
	 * 
	 * @param pFillInComponent the fill-in component
	 * @param pComponent the original component
	 * @return the merged changes or <code>null</code> if there are no changes
	 */
	protected List<Map.Entry<String, Object>> getChangedProperties(WebComponent pFillInComponent, WebComponent pComponent)
	{
		List<Map.Entry<String, Object>> liChanges = pFillInComponent.getChangedProperties();
		
		if (pFillInComponent != pComponent)
		{
			String sKey;
		
			if (liChanges != null)
			{
				//remove special properties from fill-in component because we must use the properties of original 
				//component instead
				for (int i = liChanges.size() - 1; i >= 0; i--)
				{
					sKey = liChanges.get(i).getKey();
				
					if ("parent".equals(sKey)
						|| "constraints".equals(sKey)
						|| "indexOf".equals(sKey)
						|| "defaultWindow".equals(sKey))
					{
						liChanges.remove(i);
					}
				}
			}
		
			//copy special values of original component into fill-in component

			if (liChanges == null)
			{
				liChanges = new ArrayUtil<Map.Entry<String, Object>>();
			}
			
			if (pComponent.isChanged("parent"))
			{
				liChanges.add(new ReplaceEntry("parent", pComponent.getParentIntern()));
			}
			
			if (pComponent.isChanged("constraints"))
			{
				liChanges.add(new ReplaceEntry("constraints", pComponent.getConstraints()));
			}
			
			if (pComponent.isChanged("indexOf"))
			{
				liChanges.add(new ReplaceEntry("indexOf", pComponent.getProperty("indexOf", null)));
			}
			
			if (pComponent.isChanged("defaultWindow"))
			{
				liChanges.add(new ReplaceEntry("defaultWindow", pComponent.getProperty("defaultWindow", null)));
			}
			
			//clear changes
			pComponent.getChangedProperties();
			
			//ignore empty changes
			if (liChanges.isEmpty())
			{
				return null;
			}
		}
		
		return liChanges;
	}
	
	/**
	 * Initializes the properties before the properties from the component will be set.
	 *  
	 * @param pProperties the properties cache
	 * @param pComponent the component to use
	 */
	protected void initProperties(Map<String, Object> pProperties, WebComponent pComponent)
	{
	}
	
	/**
	 * Checks if given value contains a datarow and adds the found datarow to the cached instances.
	 * 
	 * @param pValue the value to check
	 * @param pComponent the component which uses the value
	 * @param pNewDataRows the data row instance cache
	 * @param pUsedDataRowNames the used data row names
	 * @param pPrefix the name prefix
	 */
	protected void checkForDataRow(Object pValue, WebComponent pComponent, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
								   HashSet<String> pUsedDataRowNames, String pPrefix)
	{
		if (pValue instanceof IDataRow)
		{						
			handleDataRow((IDataRow)pValue, pComponent, pNewDataRows, pUsedDataRowNames, pPrefix);
		}
		else if (pValue instanceof IDataRow[])
		{
			for (IDataRow dr : (IDataRow[])pValue)
			{
				handleDataRow(dr, pComponent, pNewDataRows, pUsedDataRowNames, pPrefix);
			}
		}
		else if (pValue instanceof ICellEditor)
		{
			ICellEditor ced = (ICellEditor)pValue;
			
			if (ced instanceof UIEnumCellEditor)
			{
				ced = ((UIEnumCellEditor)ced).getUIResource();
			}

			if (ced instanceof ILinkedCellEditor)
			{
				ReferenceDefinition refdef = ((ILinkedCellEditor)pValue).getLinkReference();
				
				if (refdef != null)
				{
					IDataRow row = refdef.getReferencedDataBook();
					
					if (row != null)
					{
						handleDataRow(row, pComponent, pNewDataRows, pUsedDataRowNames, createDataRowName(pPrefix, row, pComponent));
					}
				}
			}
		}
	}
	
	/**
	 * Fills in a changed property.
	 * 
	 * @param pProperties the map of changed properties
	 * @param pComponent the component which is the owner of the properties
	 * @param pName the property name
	 * @param pValue the property value
	 * @param pNamePrefix the prefix for the component name
	 */
	protected void fillInChangedProperty(Map<String, Object> pProperties, WebComponent pComponent, String pName, Object pValue, String pNamePrefix)
	{
		if (pValue instanceof WebResource)
		{
			pProperties.put(pName, ((WebResource)pValue).getAsString());
		}
		else if (pValue instanceof EventHandler)
		{
			if (((EventHandler)pValue).isDispatchable())
			{
				pProperties.put("event" + StringUtil.firstCharUpper(pName), Boolean.TRUE);
			}
		}
		else if (pValue instanceof Style)
		{
			Style style = (Style)pValue;
			
			String[] styles = style.getStyleNames();
			
			pProperties.put(pName, styles != null && styles.length > 0 ? StringUtil.concat(",", styles) : null);
		}
		else if (pValue instanceof IDataRow)
		{
			//we can't access data rows directly
			pProperties.put(pName, hmpCurrentDataRowNames.get(pValue));
		}
		else
		{
			pProperties.put(pName, pValue);
		}
	}
	
	/**
	 * Analyzes an application component before all changed application components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pApplication the application component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void preAnalyzeApplication(WebComponent pComponent, IApplication pApplication, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
			                             HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	
	
	/**
	 * Analyzes a work-screen component before all changed screen components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pScreen the work-screen component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void preAnalyzeWorkScreen(WebComponent pComponent, IWorkScreen pScreen, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
										HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	
	
	/**
	 * Analyzes a aontent component before all changed components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pContent the content component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void preAnalyzeContent(WebComponent pComponent, IContent pContent, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
									 HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	

	/**
	 * Analyzes an application component after all changed application components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pApplication the application component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void postAnalyzeApplication(WebComponent pComponent, IApplication pApplication, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
										  HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	
	
	/**
	 * Analyzes a work-screen component after all changed screen components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pScreen the work-screen component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void postAnalyzeWorkScreen(WebComponent pComponent, IWorkScreen pScreen, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
										 HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	
	
	/**
	 * Analyzes a content component after all changed components were detected.
	 * 
	 * @param pComponent the web component
	 * @param pContent the content component
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void postAnalyzeContent(WebComponent pComponent, IContent pContent, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
									  HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
	}	

	/**
	 * Handles {@link IDataRow}s. This method puts the datarow into the cache.
	 * 
	 * @param pRow the data row
	 * @param pComponent the component which uses the data row
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void handleDataRow(IDataRow pRow, IComponent pComponent, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
			                     HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
		//check if datarow was pre-filled
		if (hmpCurrentDataRowNames.get(pRow) == null)
		{
			String sName = null;
			String sSimpleName = null;
			
			if (pRow instanceof IDataBook)
			{
				IDataSource ds = ((IDataBook)pRow).getDataSource();
	
				if (!handleDataSource(ds, pComponent, pNewDataRows, pUsedDataRowNames, pNamePrefix))
				{
					//DataBook without datasource shouldn't happen (only custom implementations)
					sSimpleName = createDataRowName(pNamePrefix, pRow, pComponent);
					sName = sSimpleName + "#+";
				}
			}
			else
			{
				//simple DataRow...
				sSimpleName = createDataRowName(pNamePrefix, pRow, pComponent);
				sName = sSimpleName + "#-";
			}
			
			if (sName != null)
			{
				sName = makeDataRowNameUnique(pNewDataRows, sName);
				
				putDataRowToCache(pNewDataRows, sName, pRow);
								
				handleRowDefinition(pComponent, pRow.getRowDefinition(), pNewDataRows, pUsedDataRowNames, sSimpleName);
			}
		}
		
		String sUsedName = hmpCurrentDataRowNames.get(pRow);
		
		if (sUsedName != null)
		{
			if (!pUsedDataRowNames.contains(sUsedName))
			{
				pUsedDataRowNames.add(sUsedName);
			}
		}
	}
	
	/**
	 * Handles an {@link IRowDefinition}. Every column will be checked if the datatype contains a cell editor which references a {@link IDataRow}. If found,
	 * the datarow will be added to the cache.
	 * 
	 * @param pComponent the base component
	 * @param pDefinition the row definition
	 * @param pNewDataRows the datarow cache
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 */
	protected void handleRowDefinition(IComponent pComponent, IRowDefinition pDefinition, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
									   HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
		if (pDefinition != null)
		{
			for (ColumnDefinition cdef : pDefinition.getColumnDefinitions())
			{
				ICellEditor ced = cdef.getDataType().getCellEditor();
				
				if (ced instanceof UIEnumCellEditor)
				{
					ced = ((UIEnumCellEditor)ced).getUIResource();
				}
				
				if (ced instanceof ILinkedCellEditor)
				{
					ReferenceDefinition refdef = ((ILinkedCellEditor)ced).getLinkReference();
					
					if (refdef != null)
					{
						IDataRow row = refdef.getReferencedDataBook();
						
						if (row != null)
						{
							handleDataRow(row, pComponent, pNewDataRows, pUsedDataRowNames, pNamePrefix + "/" + cdef.getName());
						}
					}
				}
			}
		}
	}
	
	/**
	 * Ensures that the given datarow name is a unique name and not already used.
	 * 
	 * @param pNewDataRows the list of known datarows
	 * @param pName the name to check
	 * @return the unique name
	 */
	protected String makeDataRowNameUnique(HashMap<String, WeakReference<IDataRow>> pNewDataRows, String pName)
	{
		String sName = pName;
		
		int iIndex = 0;

		while (pNewDataRows.get(sName) != null)
		{
			sName = pName.substring(0, pName.length() - 2) + "$" + (iIndex++) + pName.substring(pName.length() - 2);
		}
		
		return sName;
	}

	/**
	 * Handles {@link IDataSource}s. This method puts all datarows from the datasource into the cache.
	 * 
	 * @param pDataSource the datasource
	 * @param pComponent the component which uses the data row
	 * @param pNewDataRows the new data rows
	 * @param pUsedDataRowNames the used data row names
	 * @param pNamePrefix the name prefix
	 * @return <code>true</code> if datasource was handled, <code>false</code> otherwise, e.g. if it's <code>null</code>
	 */
	protected boolean handleDataSource(IDataSource pDataSource, IComponent pComponent, HashMap<String, WeakReference<IDataRow>> pNewDataRows, 
									   HashSet<String> pUsedDataRowNames, String pNamePrefix)
	{
		if (pDataSource != null)
		{
			int idx = 0;
			String sBookName;
			
			//pre-fill all databooks of the datasource
			for (IDataBook book : pDataSource.getDataBooks())
			{
				if (hmpCurrentDataRowNames.get(book) == null)
				{
					sBookName = createDataRowName(pNamePrefix, book, pComponent) + "#" + idx;
					
					putDataRowToCache(pNewDataRows, sBookName, book);
					
					handleRowDefinition(pComponent, book.getRowDefinition(), pNewDataRows, pUsedDataRowNames, sBookName);
				}
				idx++;
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets whether the component hierarchy should be filled in in the changed components list.
	 * 
	 * @param pComponent the component
	 * @return <code>true</code> to fill in components of given container, <code>false</code> to ignore
	 */
	protected boolean isFillInComponentHierarchy(WebComponent pComponent)
	{
		return true;
	}

	/**
	 * Gets the component which should be used for fill ins.
	 * 
	 * @param pComponent the preferred component
	 * @return the component to use
	 */
	protected WebComponent getFillInComponent(WebComponent pComponent)
	{
		return pComponent;
	}
	
	/**
	 * Loads the xml application configuration. 
	 * 
	 * @param pResourceName the path of the configuration file
	 */
	protected void loadConfiguration(String pResourceName)
	{
		if (pResourceName != null && pResourceName.trim().length() > 0)
		{
			InputStream isConfig = ResourceUtil.getResourceAsStream(Thread.currentThread().getContextClassLoader(), pResourceName);
	
			if (isConfig != null)
			{
				try
				{
					XmlWorker xmw = new XmlWorker();
					xmnAppConfig = xmw.read(isConfig);
					
					xmnAppConfig = xmnAppConfig.getNode(CONFIG_ROOT_NODE);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (isConfig != null)
					{
						try
						{
							isConfig.close();
						}
						catch (IOException ioe)
						{
							//egal
						}
					}
				}
			}
		}
		
		if (xmnAppConfig == null)
		{
			//can't load the config -> use empty configuration for parameter handling
			xmnAppConfig = new XmlNode(CONFIG_ROOT_NODE); 
		}		
	}

	/**
	 * Updates the configuration parameters with additional parameters.
	 * 
	 * @param pConfig the configuration
	 * @param pParams the parameter map
	 */
	protected void updateConfiguration(XmlNode pConfig, Hashtable<String, String> pParams)
	{		
		if (pParams != null)
		{
			for (Entry<String, String> entry : pParams.entrySet())
			{
				xmnAppConfig.setNode(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Gets a list of all registry key/value pairs which have changed since last access.
	 *  
	 * @return the list of changed key/value pairs or <code>null</code> if no changes are available
	 */
	public List<Entry<String, String>> getRegistryChanges()
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
	 * Searches a component by name. This methods performs a live search and is expensive because
	 * it iterates recursive, through all children, until a component was found.
	 * 
	 * @param pContainer The start container
	 * @param pName the component name
	 * @return the component, if found or <code>null</code> if not found in the given container
	 */
	public synchronized WebComponent getComponentByName(IContainer pContainer, String pName)
	{
		IComponent comp;
		WebComponent compFound;
		
		for (int i = 0, anz = pContainer.getComponentCount(); i < anz; i++)
		{
			comp = pContainer.getComponent(i);
			
			if (comp instanceof WebComponent && pName.equals(((WebComponent)comp).propertyName()))
			{
				return (WebComponent)comp;
			}
			
			if (comp instanceof IContainer)
			{
				compFound = getComponentByName((IContainer)comp, pName);
				
				if (compFound != null)
				{
					return compFound;
				}
			}
		}
		
		return null;
	}	
	
    /**
     * Creates an instance of an {@link IApplication}.
     * 
     * @param pLauncher the launch configuration
     * @param pClassLoader the class loader
     * @param pClassName the full qualified class name of the desired class
     * @return a new {@link IApplication} instance 
     * @throws Throwable if the instance can not be created
     */
	protected IApplication createApplication(ILauncher pLauncher, ClassLoader pClassLoader, String pClassName) throws Throwable
	{
	    return LauncherUtil.createApplication(pLauncher, pClassLoader, pClassName);
	}
	
	/**
	 * Creates a download URL.
	 * 
	 * @return the URL
	 */
	protected String createDownloadURL()
	{
	    return getURL(PARAM_DOWNLOADURL, sDownloadPath);
	}
	
	/**
	 * Creates an upload URL.
	 * 
	 * @return the URL
	 */
	protected String createUploadURL()
	{
	    return getURL(PARAM_UPLOADURL, sUploadPath);
	}
	
	/**
	 * Gets the download path.
	 * 
	 * @return the path
	 */
	protected String getDownloadPath()
	{
		return sDownloadPath;
	}
	
	/**
	 * Gets the upload path.
	 * 
	 * @return the path
	 */
	protected String getUploadPath()
	{
		return sUploadPath;
	}

	/**
	 * Gets an URL from the parameter and adds the <code>KEY</code> query parameter.
	 * 
	 * @param pParameter the parameter name
	 * @param pDefaultURL the default URL if parameter wasn't found
	 * @return the URL
	 */
	protected String getURL(String pParameter, String pDefaultURL)
	{
	    String sURLValue = getParameter(pParameter);
	    
	    if (!StringUtil.isEmpty(sURLValue))
	    {
	        if (sURLValue.indexOf('?') > 0)
	        {
	            sURLValue += "&";
	        }
	        else
	        {
                sURLValue += "?";
	        }
	    }
	    else
	    {
	        sURLValue = getParameter(ILauncher.PARAM_SERVERBASE) + pDefaultURL + "?";
	    }
	    
	    return sURLValue;
	}
	
	/**
	 * Creates a unique data row name.
	 * 
	 * @param pNamePrefix the name prefix
	 * @param pDataRow the data row
	 * @param pComponent the used component
	 * @return the name
	 */
	protected String createDataRowName(String pNamePrefix, IDataRow pDataRow, IComponent pComponent)
	{
		if (pDataRow instanceof IDataBook)
		{
			return pNamePrefix + "/" + ((IDataBook)pDataRow).getName();	
		}
		else
		{
			String sName;
			
			if (pDataRow instanceof DataRow)
			{
				sName = (String)((DataRow)pDataRow).getObject(IWebFieldConstants.NAME);
			}
			else
			{
				sName = null;
			}
			
			if (sName != null)
			{
				return pNamePrefix + "/" + sName;
			}
			else
			{
				return pNamePrefix;
			}
		}
	}
	
	/**
	 * Adds a datarow to the internal cache.
	 * 
	 * @param pNewDataRows the new data row cache
	 * @param pName the cache name
	 * @param pDataRow the data row
	 */
	protected void putDataRowToCache(HashMap<String, WeakReference<IDataRow>> pNewDataRows, String pName, IDataRow pDataRow)
	{
		if (pDataRow instanceof DataRow) 
		{
			//cache name for e.g. debug
			((DataRow)pDataRow).putObject(IWebFieldConstants.NAME_CACHED, pName);
		}
		
		WeakReference<IDataRow> wref = new WeakReference<IDataRow>(pDataRow);
		
		WeakReference<IDataRow> wrefOld = pNewDataRows.put(pName, wref);
		
		if (wrefOld != null)
		{
			//old can be null
			replaceDataRowInCache(wrefOld.get(), pDataRow);
		}
		
        hmpCurrentDataRows.put(pName, wref);
		hmpCurrentDataRowNames.put(pDataRow, pName);
		
    	//we found the datarow, so not only registered
    	hmpOnlyRegistered.remove(pDataRow);
	}
	
	/**
	 * Puts a datarow into the current cache.
	 * 
	 * @param pName the cache name for the datarow
	 * @param pDataRow the datarow
	 */
	protected synchronized void putDataRowToCurrentCache(String pName, IDataRow pDataRow)
	{
		if (pDataRow instanceof DataRow) 
		{
			//cache name for e.g. debug
			((DataRow)pDataRow).putObject(IWebFieldConstants.NAME_CACHED, pName);
		}

        hmpCurrentDataRows.put(pName, new WeakReference<IDataRow>(pDataRow));
		hmpCurrentDataRowNames.put(pDataRow, pName);
		
    	//we found the datarow, so not only registered
    	hmpOnlyRegistered.remove(pDataRow);
	}
	
	/**
	 * Replaces a datarow in our internal cache.
	 * 
	 * @param pOld the old row (maybe null if gc'ed)
	 * @param pNew the new row
	 */
	protected void replaceDataRowInCache(IDataRow pOld, IDataRow pNew)
	{
	}
	
	/**
	 * Removes a datarow from the internal cache.
	 * 
	 * @param pName the cache name
	 */
	protected void removeDataRowFromCache(String pName)
	{
		WeakReference<IDataRow> wref = hmpCurrentDataRows.remove(pName);
		
		if (wref != null)
		{
			IDataRow row = wref.get();
			
			if (row != null)
			{
				hmpCurrentDataRowNames.remove(row);
			}
		}
	}
	
	/**
	 * Removes a datarow from the internal cache.
	 * 
	 * @param pRow the data row
	 * @return <code>true</code> if row was removed or <code>false</code> if not
	 */
	protected boolean removeDataRowFromCache(IDataRow pRow)
	{
		if (pRow != null)
		{
			String sName = hmpCurrentDataRowNames.remove(pRow);
			
			if (sName != null)
			{
				hmpCurrentDataRows.remove(sName);
			}
			
			return true;
		}

		return false;
	}
	
	/**
	 * Adds a parameter to the given list and creates a new list if given list
	 * is <code>null</code>.
	 * 
	 * @param pList the list
	 * @param pParameter the parameter
	 * @return the list with added parameter
	 */
	private List<String> addParameter(List<String> pList, String pParameter)
	{
		List<String> list = pList;
		
		if (list == null)
		{
			list = new ArrayUtil<String>();
		}
		
		list.add(pParameter);

		return list;
	}
	
    /**
     * Adds a push handler if not already added.
     * 
     * @param pHandler the handler
     */
    public void addPushHandler(IPushHandler pHandler)
    {
    	synchronized (liPushHandler) 
    	{
	    	if (!liPushHandler.contains(pHandler))
	    	{
	    		liPushHandler.add(new WeakReference<IPushHandler>(pHandler));
	    	}
    	}
    }
    
    /**
     * Removes a push handler.
     * 
     * @param pHandler the handler
     */
    public void removePushHandler(IPushHandler pHandler)
    {
    	synchronized (liPushHandler) 
    	{
    		liPushHandler.remove(pHandler);
    	}
    }
    
    /**
     * Gets all available and active push handlers.
     * 
     * @return all available and active push handlers
     */
    public IPushHandler[] getPushHandlers()
    {
    	ArrayUtil<IPushHandler> result = new ArrayUtil<IPushHandler>();
    	
    	IPushHandler handler;
    	
    	synchronized (liPushHandler) 
    	{
	    	for (int i = liPushHandler.size() - 1; i >= 0; i--)
	    	{
				handler = liPushHandler.get(i).get();
				
				if (handler == null)
				{
					liPushHandler.remove(i);
				}
				else
				{
					result.add(0, handler);
				}
			}
    	}
    	
    	return result.toArray(new IPushHandler[result.size()]);
    }
    
    /**
     * Gets whether push is enabled. It's enabled if at least one handler is registered
     * 
     * @return if push is enabled
     */
    public boolean isPushEnabled()
    {
    	synchronized (liPushHandler) 
    	{
	    	return !liPushHandler.isEmpty();
    	}
    }
    
    /**
     * Notification before an UI action is performed.
     */
    public void notifyBeforeUI()
    {
    	IFactory factory = getFactory();
    	
    	if (factory != null)
    	{
			UIFactoryManager.registerThreadFactoryInstance(factory);
			LocaleUtil.setThreadDefault(getLocale());
			TimeZoneUtil.setThreadDefault(getTimeZone());
    	}
    }
    
    /**
     * Notification after an UI action was performed.
     */
    public static void notifyAfterUI()
    {
        LocaleUtil.setThreadDefault(null);
        TimeZoneUtil.setThreadDefault(null);
        
        UIFactoryManager.unregisterThreadFactoryInstance();
    } 
    
    /**
     * Gets the value for the given application parameter as boolean value.
     * 
     * @param pParameterName the name of the parameter
     * @param pDefault the default value if parameter was not set
     * @return <code>pDefault</code> if the parameter was not set, otherwise the boolean representation of the value
     */
    public boolean getParameterAsBoolean(String pParameterName, boolean pDefault)
    {
        String sValue = getParameter(pParameterName);
        
        if (StringUtil.isEmpty(sValue))
        {
            return pDefault;
        }
        
        return Boolean.parseBoolean(sValue);
    } 	
    
    /**
     * Marks a component removed.
     * 
     * @param pComponent the component
     */
    public synchronized void markRemoved(IComponent pComponent)
    {
    	hstRemoveMark.add(((WebComponent)pComponent).getComponentId());
    }
    
    /**
     * Registers a datarow for a specific control.
     * 
     * @param pRow the datarow
     * @param pControl the control or <code>null</code> for "unregister"
     */
    public synchronized void register(IDataRow pRow, IControl pControl)
    {
    	if (pControl != null && !isCached(pRow))
    	{
			hmpOnlyRegistered.put(pRow, Boolean.TRUE);
		}
    }
    
    /**
     * Gets whether the given datarow is registered, but not found in UI tree right now.
     * 
     * @param pRow the datarow
     * @return <code>true</code> if datarow was not found in UI tree right now,
     *         <code>false</code> otherwise
     */
    protected boolean isRegistered(IDataRow pRow)
    {
    	return hmpOnlyRegistered.containsKey(pRow);
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * The <code>ChangedComponents</code> class is a cache for UI changes. It contains
	 * all information about changes to properties.
	 * 
	 * @author René Jahn
	 */
	public static final class ChangedComponents
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the change list. */
		private ArrayList<Map<String, Object>> liChanges;
		
		/** the change index per component id. */
		private HashMap<String, int[]> hmpChangesIndex;

		/** the mapping for name to id. */
		private HashMap<String, String> hmpNameToId;
		
		/** the mapping for id to name. */
		private HashMap<String, String> hmpIdToName;

		/** the removed components by id. */
		private HashMap<String, WebComponent> hmpOldRemoved;
		
		/** the list of data row names. */
		private String[] saDataRowNames;

		/** the list of used data row names. */
		private String[] saUsedDataRowNames;
		
		/** whether destroy changes should be ignored. */
		private boolean bIgnoreDestroy = false;
		
		/** whether remove changes should be ignored. */
		private boolean bIgnoreRemove = false;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>ChangedComponent</code>.
		 * 
		 * @param pChanges the list of all changes
		 * @param pDataRows the new data rows
		 * @param pUsedRowNames the used data row names
		 * @param pChangesIndex the changes per component id
		 * @param pNameMap the mapping between name and id
		 * @param pOldRemoved the mapping between id and component for removed components
		 * @param pPrevious the previous changes
		 */
		private ChangedComponents(ArrayList<Map<String, Object>> pChanges,
				                  HashMap<String, WeakReference<IDataRow>> pDataRows, HashSet<String> pUsedRowNames,
				                  HashMap<String, int[]> pChangesIndex, HashMap<String, WebComponent> pNameMap, 
				                  HashMap<String, WebComponent> pOldRemoved, ChangedComponents pPrevious)
		{
			liChanges = pChanges;
			hmpChangesIndex = pChangesIndex;
			
			hmpOldRemoved = pOldRemoved;
			
			hmpNameToId = new HashMap<String, String>();
			hmpIdToName = new HashMap<String, String>();
			
			String sName;
			String sId;
			
			for (Entry<String, WebComponent> entry : pNameMap.entrySet())
			{
				sId = entry.getValue().getComponentId();
				sName = entry.getKey();
				
				hmpNameToId.put(sName, sId);
				hmpIdToName.put(sId, sName);
			}
			
			List<String> liDataRowNames = new ArrayUtil<String>();
			
			for (Entry<String, WeakReference<IDataRow>> entry : pDataRows.entrySet())
			{
				if (entry.getValue().get() != null)
				{
					liDataRowNames.add(entry.getKey());
				}
			}
			
			saDataRowNames = liDataRowNames.toArray(new String[liDataRowNames.size()]);
			
			saUsedDataRowNames = pUsedRowNames.toArray(new String[pUsedRowNames.size()]);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets a list of all changes.
		 * 
		 * @return the changes
		 */
		public List<Map<String, Object>> getChanges()
		{
			return (List<Map<String, Object>>)liChanges.clone();
		}
		
		/**
		 * Gets a list of all changes for a single component, by id. 
		 * The changes are recursive but flattened as list.
		 * 
		 * @param pComponentId the component id
		 * @return the changes
		 */
		public ChangeSet getChangesById(String pComponentId)
		{
			if (pComponentId == null)
			{
				return null;
			}
			
			List<Map<String, Object>> liIncluded = new ArrayUtil<Map<String, Object>>();
			
			int[] index;

			if (!bIgnoreRemove)
			{
				//add remove records only once per response
				bIgnoreRemove = true;
				
				index = hmpChangesIndex.get(IWebFieldConstants.REMOVE);
				
				if (index != null && index[0] != index[1])
				{
					liIncluded.addAll(liChanges.subList(index[0], index[1]));
				}
			}

			if (!bIgnoreDestroy)
			{
				//add destroy records only once per response
				bIgnoreDestroy = true;
				
				index = hmpChangesIndex.get(IWebFieldConstants.DESTROY);
				
				if (index != null && index[0] != index[1])
				{
					liIncluded.addAll(liChanges.subList(index[0], index[1]));
				}
			}
			
			index = hmpChangesIndex.get(pComponentId);

			//no index or same index means that there are no changes (but maybe global changes)
			if (index == null || index[0] == index[1])
			{
				if (liIncluded.isEmpty())
				{
					return null;
				}
				
				return new ChangeSet(liIncluded, null);
			}
			
			//add before global changes
			liIncluded.addAll(0, liChanges.subList(index[0], index[1]));

			List<Map<String, Object>> liExcluded = new ArrayUtil<Map<String, Object>>();
			liExcluded.addAll(liChanges.subList(0, index[0]));
			liExcluded.addAll(liChanges.subList(index[1] - 1, liChanges.size() - 1));
			
			return new ChangeSet(liIncluded, liExcluded);
		}
		
		/**
		 * Gets a list of all changes for a single component, by name. 
		 * The changes are recursive but flattened as list.
		 * 
		 * @param pComponentName the component name
		 * @return the changes
		 */
		public ChangeSet getChangesByName(String pComponentName)
		{
			return getChangesById(hmpNameToId.get(pComponentName));
		}
		
		/**
		 * Gets all changed data row names.
		 * 
		 * @return the list of names
		 */
		public String[] getChangedDataRowNames()
		{
			return saDataRowNames;
		}
		
		/**
		 * Gets all used data row names.
		 * 
		 * @return the list of names
		 */
		public String[] getUsedDataRowNames()
		{
			return saUsedDataRowNames;
		}
		
		/**
		 * Gets a component which was removed in this changes.
		 * 
		 * @param pId the component id
		 * @return the removed component
		 */
		public WebComponent getRemovedComponent(String pId)
		{
			if (hmpOldRemoved != null)
			{
				return hmpOldRemoved.get(pId);
			}
			else
			{
				return null;
			}
		}
		
	}	// ChangedComponents
	
	/**
	 * The <code>ChangeSet</code> contains information about included and excluded component changes.
	 * An included component is a component which will be relevant for building the UI and an excluded
	 * component is available but not relevant for building the UI.
	 *  
	 * @author René Jahn
	 */
	public static final class ChangeSet
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the included changes. */
		private List<Map<String, Object>> included;
		/** the excluded changes. */
		private List<Map<String, Object>> excluded;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>ChangeSet</code>.
		 * 
		 * @param pIncluded the included changes
		 * @param pExcluded the excluded changes
		 */
		private ChangeSet(List<Map<String, Object>> pIncluded, List<Map<String, Object>> pExcluded)
		{
			included = pIncluded != null ? new ArrayUtil<Map<String, Object>>(pIncluded) : null;
			excluded = pExcluded != null ? new ArrayUtil<Map<String, Object>>(pExcluded) : null;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the changes of included components.
		 * 
		 * @return the changes
		 */
		public List<Map<String, Object>> getIncluded()
		{
			return included;
		}
		
		/**
		 * Gets the changes of excluded components.
		 * 
		 * @return the changes
		 */
		public List<Map<String, Object>> getExcluded()
		{
			return excluded;
		}
		
	}	// ChangeSet
	
	/**
	 * The <code>UploadFile</code> is a validatable RemoteFileHandle.
	 * 
	 * @author René Jahn
	 */
	public class UploadFile extends RemoteFileHandle
	                        implements IValidatable,
	                                   IInvalidator
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the instance creation time. */
		private long creation = System.currentTimeMillis();
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>UploadFile</code>.
		 * 
		 * @param pKey the cache key
		 */
		public UploadFile(Object pKey)
		{
			super(null, pKey);
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public boolean isValid() 
		{
			return creation + getTimeout() >= System.currentTimeMillis();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void invalidate(Object pObject)
		{
			if (pObject == this)
			{
				clearUploadParameter(getObjectCacheKey());
			}
		}
		
	}	// UploadFile
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>ReplaceEntry</code> is a simple key/value mapping.
	 * 
	 * @author René Jahn
	 * 
	 * @param <KI> type of key
	 * @param <VI> type of entry
	 */
	private static final class ReplaceEntry<KI, VI> implements Map.Entry
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the key reference. */
		private KI key;
		
		/** the value reference. */
		private VI value;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>ReplaceEntry</code> with key and value
		 * references.
		 * 
		 * @param pKey the key reference
		 * @param pValue the value reference
		 */
		private ReplaceEntry(KI pKey, VI pValue)
		{
			key = pKey;
			value = pValue;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public KI getKey()
		{
			return key;
		}

		/**
		 * {@inheritDoc}
		 */
		public VI getValue()
		{
			return value;
		}

		/**
		 * Does nothing.
		 * 
		 * @param pValue the param will be ignored
		 * @return the value
		 * @see #getValue()
		 */
		public VI setValue(Object pValue)
		{
			return value;
		}
		
	}	// ReplaceEntry
		
}	// WebLauncher
