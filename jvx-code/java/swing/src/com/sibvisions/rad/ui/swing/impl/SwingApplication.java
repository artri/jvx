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
 * 01.11.2008 - [JR] - replaceParameter used
 *                   - setParameter implemented
 * 25.11.2008 - [JR] - removed setEventDispatcher                   
 * 04.12.2008 - [JR] - handled exceptions with error frame
 * 06.12.2008 - [JR] - getParameter: supported \n      
 * 17.02.2009 - [JR] - set/getCursor implemented   
 * 18.03.2009 - [JR] - used FileViewer  
 * 28.05.2009 - [JR] - use the content pane from the frame as content pane for the sub root pane [BUGFIX]
 * 24.06.2009 - [JR] - updateConfiguration: set the codebase parameter from JNLP  
 * 31.07.2009 - [JR] - cancelPendingThreads implemented 
 * 17.08.2009 - [JR] - getFileHandle/saveFileHandle: set last selected directory
 * 19.08.2009 - [JR] - save/open/show filehandle: webstart support
 * 20.10.2009 - [JR] - get/setRegistryKey: don't access application 
 *                     -> maybe called from applications constructor [BUGFIX]
 * 14.11.2009 - [JR] - get/setRegistryKey: used ApplicationUtil for application name detection  
 * 09.12.2009 - [JR] - #38: RemoteFileHandle.setDownloadURLSpec called                                   
 * 13.09.2010 - [JR] - used ApplicationUtil to load the configuration   
 * 05.11.2010 - [JR] - #202: check maximized after pack()  
 * 22.12.2010 - [JR] - setMenuBar: null check
 * 03.02.2011 - [JR] - invokeLater added (solves locking problem with main and AWT-Eventqueue thread)
 * 18.03.2011 - [JR] - logging instead of printStackTrace
 * 12.09.2011 - [JR] - startup and empty constructor implemented
 * 14.10.2011 - [JR] - #483: check if download URL is set
 * 15.04.2012 - [JR] - #572: put the application as client property
 * 17.07.2012 - [JR] - configurable factory class
 * 13.08.2013 - [JR] - #756: changed set menu
 * 14.09.2013 - [JR] - removed final from class
 * 10.01.2014 - [JR] - put/getObject implemented
 * 24.04.2014 - [JR] - #1018: use framebounds system property for frame location and size
 * 21.12.2016 - [JR] - #1714: debug log
 * 30.11.2017 - [JR] - #1855: don't dispatch windowClosed twice
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.rad.application.IApplication;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIFactoryManager;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.rad.remote.IConnectionConstants;
import javax.rad.ui.IRectangle;
import javax.rad.ui.UIException;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.util.ExceptionHandler;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.Webstart;
import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.impl.container.SwingFrame;
import com.sibvisions.util.FileViewer;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.TimeZoneUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>SwingApplication</code> is the {@link IApplication} implementation
 * for swing applications with full access to the {@link javax.swing.JFrame}.
 * 
 * @author René Jahn
 */
public class SwingApplication extends SwingFrame
                              implements ILauncher
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the property name for the application (client properties). */
	public static final String PROPERTY_APPLICATION = "application";
	
	/** the root node identifier for application configuration xml files. */
	private static final String CONFIG_ROOT_NODE = "application";

	
	/** the {@link UILauncher} instance. */
	private UILauncher uilauncher;
	
	/** the application. */
	private IApplication application = null;
	
	/** the configuration of an application. */
	private XmlNode xmnAppConfig = null;
	
	/** the object cache before a launcher is created. */
	private Hashtable<String, Object> htCachedObjects = null;
	
	/** the file chooser. */
	private FileChooser fileChooser = null;
	
	/** max frame. */
	private JFrame jfMax;
	
	/** whether exit should be called on dispose. */
	private boolean bSystemExitOnDispose = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		try
		{
			System.setProperty("sun.awt.noerasebackground", "true");
		}
		catch (SecurityException se)
		{
			//not allowed
		}		
		try
		{
			System.setProperty("sun.java2d.d3d", "false");
		}
		catch (SecurityException se)
		{
			//not allowed
		}		
		try
		{
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		catch (SecurityException se)
		{
			//not allowed
		}		
	}
	
	/**
	 * Starts the application as frame.
	 * The first argument should give the full qualified class name of
	 * the application to run.
	 * 
	 * @param pArguments the launcher arguments: main-class [config.xml key1=value1 key2=value2]
	 */
	public static void main(String[] pArguments)
	{
		// The first Parameter is the Application to start.
		if (pArguments.length == 0)
		{
			throw new UIException("No class name parameter found!");
		}
		
		//remove quote
		for (int i = 0, anz = pArguments.length; i < anz; i++)
		{
			if (pArguments[i].startsWith("\"") && pArguments[i].endsWith("\""))
			{
				pArguments[i] = pArguments[i].substring(1, pArguments[i].length() - 1);
			}
		}
		
		String sApplicationClassName = pArguments[0];

		String sConfigFileName;
		String[] sParams;
				
		if (pArguments.length >= 2)
		{
			sConfigFileName = pArguments[1];

			sParams = new String[pArguments.length - 2];
			System.arraycopy(pArguments, 2, sParams, 0, sParams.length);
		}
		else
		{
			//the default configuration
			sConfigFileName = "application.xml";
			sParams = null;
		}

		createSwingApplication(sApplicationClassName, sConfigFileName, sParams);
	}
		
	/**
	 * Creates the application in the UI thread.
	 * 
	 * @return the swing application		
	 */
	public static SwingApplication createSwingApplication()
	{
		final SwingApplication[] swingApplication = new SwingApplication[1];
		
		try 
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					SwingApplication app = null;
					
					String sClassName = getClassNameFromEnvironment();
					
					if (!StringUtil.isEmpty(sClassName))
					{
						try
						{
							app = (SwingApplication)Class.forName(sClassName).newInstance();
						}
						catch (Throwable th)
						{
							LoggerFactory.getInstance(SwingApplication.class).debug("Fallback to SwingApplication.class", th);
						}
					}
					
					if (app == null)
					{
						app = new SwingApplication();
					}
					
					swingApplication[0] = app;
				}
			});
		} 
		catch (Exception ex) 
		{
			throw new IllegalStateException("SwingApplication cannot be created!", ExceptionUtil.getRootCause(ex));
		}
		
		return swingApplication[0];
	}

	/**
	 * Creates the application in the UI thread.
	 * 
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pConfigFileName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file 
	 * @return the swing application		
	 */
	public static SwingApplication createSwingApplication(final String pApplicationClassName, final String pConfigFileName, final String[] pParams)
	{
		final SwingApplication[] swingApplication = new SwingApplication[1];
		
		try 
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					SwingApplication app = null;
					
					String sClassName = getClassNameFromEnvironment();
					
					if (!StringUtil.isEmpty(sClassName))
					{
						try
						{
							Class<?> clazz = Class.forName(sClassName);
							app = (SwingApplication)clazz.getConstructor(String.class, String.class, String[].class).newInstance(pApplicationClassName, pConfigFileName, pParams);
						}
						catch (Throwable th)
						{
							th.printStackTrace();
						}
					}
					
					if (app == null)
					{
						app = new SwingApplication(pApplicationClassName, pConfigFileName, pParams);
					}					
					
					swingApplication[0] = app;
				}
			});
		} 
		catch (Exception ex) 
		{
			throw new IllegalStateException("SwingApplication cannot be created!", ExceptionUtil.getRootCause(ex));
		}
		
		return swingApplication[0];
	}

	/**
	 * Creates a new instance of <code>SwingApplication</code>. Use this constructor
	 * if you need access to the frame before the application is started.
	 * 
	 * @see #startup(String, String, String[])
	 */
	public SwingApplication()
	{
	}
	
	/**
	 * Creates a new instance of <code>SwingApplication</code> with the desired
	 * application.
	 * 
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pConfigFileName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file 				
	 */
	public SwingApplication(String pApplicationClassName, String pConfigFileName, String[] pParams)
	{
		startup(pApplicationClassName, pConfigFileName, pParams);
	}
	
	/**
	 * Starts the application in the UI thread.
	 * 
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pConfigFileName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file 				
	 */
	public void startup(final String pApplicationClassName, final String pConfigFileName, final String[] pParams)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				startupInternal(pApplicationClassName, pConfigFileName, pParams);
			}
		});
	}

	/**
	 * Starts the application.
	 *  
	 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
	 * @param pConfigFileName gives the name of the configuration file (default: application.xml)
	 * @param pParams additional or preferred parameters. The parameters overrides the parameters from the config file 				
	 */
	private void startupInternal(String pApplicationClassName, String pConfigFileName, String[] pParams)
	{
		loadConfiguration(pConfigFileName);

		Locale clientLocale = Locale.getDefault();
		setParameter(IConnectionConstants.CLIENT_LOCALE_LANGUAGE, clientLocale.getLanguage());
		setParameter(IConnectionConstants.CLIENT_LOCALE_COUNTRY, clientLocale.getCountry());
		setParameter(IConnectionConstants.CLIENT_LOCALE_VARIANT, clientLocale.getVariant());
		try
		{
			setParameter(IConnectionConstants.CLIENT_FILE_ENCODING, System.getProperty("file.encoding"));
		}
		catch (Exception ex)
		{
			// Do nothing
		}

		updateConfiguration(pParams);
		
		Class<?> clazz; 

		try
		{
			clazz = Class.forName(getParameter(ILauncher.PARAM_UIFACTORY));
		}
		catch (Exception e)
		{
			clazz = SwingFactory.class;
			
			LoggerFactory.getInstance(SwingApplication.class).debug("Fallback to SwingFactory", e);
		}
		
		setFactory(UIFactoryManager.getFactoryInstance(clazz));
    	
		SwingFactory.setLookAndFeel(this, getParameter("Application.LookAndFeel"));
    	
    	uilauncher = createUILauncher(this);
    	
    	if (htCachedObjects != null)
    	{
	    	for (Entry<String, Object> entry : htCachedObjects.entrySet())
	    	{
	    		uilauncher.putObject(entry.getKey(), entry.getValue());
	    	}
	    	
	    	htCachedObjects = null;
    	}
    	
		try
    	{
    		application = createApplication(uilauncher, pApplicationClassName);

    		uilauncher.setTitle(application.getName());
        	  
        	//add the application
        	uilauncher.add(application, IBorderLayout.CENTER);
        	
        	resource.getRootPane().putClientProperty(PROPERTY_APPLICATION, application);

        	uilauncher.pack();
    	}
    	catch (Throwable th)
    	{
    		//to show the frame with a "minimum" size
    		getModalDesktopPane().setPreferredSize(new Dimension(800, 600));

    		uilauncher.pack();
    		
    		uilauncher.handleException(th);
    	}

		if (eventWindowClosing == null || eventWindowClosing.getDefaultListener() == null)
		{
		    eventWindowClosing().setDefaultListener(this, "dispose");
		}

    	int iState = getState();

    	if (iState == MAXIMIZED_BOTH 
    		|| iState == MAXIMIZED_HORIZ
        	|| iState == MAXIMIZED_VERT)
    	{
    		setState(iState);
    	}
    	
    	if (!LauncherUtil.configureFrameBounds(this, resource))
    	{
    	    resource.setLocationRelativeTo(null);
    	}
    	
    	uilauncher.setVisible(true);

    	if (application != null)
    	{
    		SwingUtilities.invokeLater(new Runnable()
    		{
    			public void run()
    			{
		    		application.notifyVisible();
    			}
    		});
    	}
	}
	
	/**
	 * Creates a new instance of UI launcher for this application.
	 * 
	 * @param pLauncher the launcher to use
	 * @return the UI launcher
	 */
	protected UILauncher createUILauncher(ILauncher pLauncher)
	{
		return new UILauncher(pLauncher);
	}
	
	/**
	 * Creates a new application instance.
	 * 
	 * @param pLauncher the launcher
	 * @param pClassName the application class name
	 * @return the application instance
	 * @throws Throwable if creation fails
	 */
	protected IApplication createApplication(UILauncher pLauncher, String pClassName) throws Throwable
	{
		return LauncherUtil.createApplication(uilauncher, pClassName);
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

        Rectangle rectBounds = getRestoreBounds(); // resource.getBounds();
        
        int iState = resource.getExtendedState();
        
        setRegistryKey("framebounds", rectBounds.x + "," + rectBounds.y + "," + rectBounds.width + "," + rectBounds.height);
        setRegistryKey("maximized", "" + (iState == MAXIMIZED_BOTH || iState == MAXIMIZED_HORIZ || iState == MAXIMIZED_VERT));
        
		// dispose SwingFrame
		super.dispose();

		if (bSystemExitOnDispose)
		{
	        //send event here, because dispose sends the CLOSED event via event dispatcher and because of
	        //the following System.exit, the event does not arrive
	        if (eventWindowClosed != null)
	        {
	            eventWindowClosed.dispatchEvent(new UIWindowEvent(eventSource,
	                                                              UIWindowEvent.WINDOW_CLOSED,
	                                                              AwtFactory.getMostRecentEventTime(),
	                                                              AwtFactory.getCurrentModifiers()));
	        }       

	        // Exit Process
			System.exit(0);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//ILAUNCHER
	
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
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void showDocument(String pDocumentName, IRectangle pBounds, String pTarget) throws Throwable
	{
		try
		{
			FileViewer.open(pDocumentName);
		}
		catch (Exception e)
		{
			if (Webstart.isJnlp())
			{
				Webstart.showDocument(pDocumentName);
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
    public void showFileHandle(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
    {
    	try
    	{
	    	File file = null;
	    	if (pFileHandle instanceof FileHandle)
	    	{
	    		file = ((FileHandle)pFileHandle).getFile();
	    	}
	    	if (file == null)
	    	{
	    		file = FileUtil.getNotExistingFile(new File(System.getProperty("java.io.tmpdir"), pFileHandle.getFileName()));
	    		file.deleteOnExit();
	    		
	    		FileUtil.copy(pFileHandle.getInputStream(), true, new FileOutputStream(file), true);
	    	}
	    	FileViewer.open(file);
    	}
    	catch (Throwable th)
    	{
    		if (Webstart.isJnlp())
    		{
    			Webstart.showSaveDialog(pFileHandle);
    		}
    		else
    		{
    			throw th;
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
    public void saveFileHandle(final IFileHandle pFileHandle, final String pTitle) throws Throwable
    {
    	try
    	{
    		if (fileChooser == null)
    		{
    			fileChooser = new FileChooser();
    		}
    		
    		if (EventQueue.isDispatchThread())
    		{
    			fileChooser.saveAs(uilauncher, resource, pFileHandle, pTitle);
    		}
    		else
    		{
	    		SwingUtilities.invokeAndWait(new Runnable()
				{
	    			public void run()
	    			{
	    				try
	    				{
	    					fileChooser.saveAs(uilauncher, resource, pFileHandle, pTitle);
	    				}
	    				catch (Exception e)
	    				{
	    					throw new RuntimeException(e);
	    				}
	    			}
				});
    		}
    	}
    	catch (SecurityException se)
    	{
    		if (Webstart.isJnlp())
    		{
    			Webstart.showSaveDialog(pFileHandle);
    		}
    		else
    		{
    			throw se;
    		}
    	}
    }
	    
	/**
	 * {@inheritDoc}
	 */
    public void getFileHandle(final IFileHandleReceiver pFileHandleReceiver, final String pTitle) throws Throwable
    {
    	IFileHandle[] files = null;
    	
    	try
    	{
    		if (fileChooser == null)
    		{
    			fileChooser = new FileChooser();
    		}

    		if (EventQueue.isDispatchThread())
    		{
	    		files = fileChooser.open(uilauncher, resource, pTitle);
    		}
    		else
    		{
	    		final List<IFileHandle[]> liExchange = new ArrayList<IFileHandle[]>();
	
	    		SwingUtilities.invokeAndWait(new Runnable()
				{
	    			public void run()
	    			{
	    				try
	    				{
	    		    		liExchange.add(fileChooser.open(uilauncher, resource, pTitle));
	    				}
	    				catch (Exception e)
	    				{
	    					throw new RuntimeException(e);
	    				}
	    			}
				});
	    		
	    		files = liExchange.get(0);
    		}
    	}
    	catch (SecurityException se)
    	{
    		if (Webstart.isJnlp())
    		{
    			files = Webstart.showOpenDialog(true);
    		}
    		else
    		{
    			throw se;
    		}
    	}

    	if (files != null)
		{
    		final IFileHandle[] finalSelectedFiles =  files;
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
		    		try
		    		{
						for (int i = 0; i < finalSelectedFiles.length; i++)
						{
							pFileHandleReceiver.receiveFileHandle(finalSelectedFiles[i]);
						}
		    		}
		    		catch (Throwable ex)
		    		{
		    			ExceptionHandler.show(ex);
		    		}
                }
            });
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
    	try
    	{
    		LauncherUtil.setRegistryKey(LauncherUtil.getRegistryApplicationName(this), pKey, pValue);
    	}
    	catch (SecurityException se)
    	{
    		if (Webstart.isJnlp())
    		{
    			Webstart.setProperty(pKey, pValue);
    		}
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public String getRegistryKey(String pKey)
    {
    	try
    	{
    		return LauncherUtil.getRegistryKey(LauncherUtil.getRegistryApplicationName(this), pKey);
    	}
    	catch (SecurityException se)
    	{
    		if (Webstart.isJnlp())
    		{
    			return Webstart.getProperty(pKey);
    		}
    		else
    		{
    			return null;
    		}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public String getEnvironmentName()
    {
    	return ILauncher.ENVIRONMENT_DESKTOP;
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
    	return LocaleUtil.getDefault();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setLocale(Locale pLocale)
    {
    	LocaleUtil.setDefault(pLocale);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public TimeZone getTimeZone()
    {
    	return TimeZoneUtil.getDefault();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setTimeZone(TimeZone pTimeZone)
    {
    	TimeZoneUtil.setDefault(pTimeZone);
    }
    
	//IEXCEPTIONLISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void handleException(Throwable pThrowable)
	{
		SwingFactory.showError(pThrowable, getModalDesktopPane());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Loads the xml application configuration. 
	 * 
	 * @param pFileName the filename of the configuration file
	 */
	private void loadConfiguration(String pFileName)
	{
		if (pFileName != null && pFileName.trim().length() > 0)
		{
			try
			{
				xmnAppConfig = LauncherUtil.getConfig(pFileName); 
				
				if (xmnAppConfig != null)
				{
					xmnAppConfig = xmnAppConfig.getNode(CONFIG_ROOT_NODE);
				}
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(getClass()).error("Configuration load error!", e);
			}
		}
		
		if (xmnAppConfig == null)
		{
			//can't load the config -> use empty configuration for parameter handling
			xmnAppConfig = new XmlNode(CONFIG_ROOT_NODE); 

			LoggerFactory.getInstance(getClass()).error("Configuration was not found -> empty configuration was created!");
		}		
	}

	/**
	 * Updates the configuration parameters with additional parameters.
	 * 
	 * @param pParams the parameter mapping: key=value
	 */
	private void updateConfiguration(String[] pParams)
	{		
		if (pParams != null)
		{
			StringTokenizer tok;
			
			for (int i = 0, anz = pParams.length; i < anz; i++)
			{
				tok = new StringTokenizer(pParams[i], "=");
				
				if (tok.countTokens() == 2)
				{
					xmnAppConfig.setNode(tok.nextToken(), tok.nextToken());
				}
			}
		}
		
		if (Webstart.isJnlp())
		{
			//include JNLP parameters
			try
			{
				String sCodeBase = Webstart.getCodeBase();
				
				if (sCodeBase != null)
				{
					xmnAppConfig.setNode(ILauncher.PARAM_CODEBASE, sCodeBase);
				}
			}
			catch (Throwable th)
			{
				//nothing to be done
				
				//don't log because the logger factory was not initialized yet!
			}
		}
	}
	
	/**
	 * Puts an external object into launchers object store.
	 * 
	 * @param pName the name of the object
	 * @param pObject the object or <code>null</code> to remove the object from the store
	 * @return the previous object
	 */
	public Object putObject(String pName, Object pObject)
	{
		if (uilauncher != null)
		{
			return uilauncher.putObject(pName, pObject);
		}
		else
		{
			if (pObject != null)
			{
				if (htCachedObjects == null)
				{
					htCachedObjects = new Hashtable<String, Object>();
				}
				
				htCachedObjects.put(pName, pObject);
			}
			else if (htCachedObjects != null)
			{
				Object obj = htCachedObjects.remove(pName);
				
				if (htCachedObjects.isEmpty())
				{
					htCachedObjects = null;
				}
				
				return obj;
			}
			
			return null;
		}
	}
	
	/**
	 * Gets an external object from launchers object store.
	 * 
	 * @param pName the name of the object
	 * @return the object or <code>null</code> if no object was found with the given name
	 */
	public Object getObject(String pName)
	{
		if (uilauncher != null)
		{
			return uilauncher.getObject(pName);
		}
		else if (htCachedObjects != null)
		{
			return htCachedObjects.get(pName);
		}
		
		return null;
	}
	
	/**
	 * Sets whether {@link System#exit(int)} should be called if dispose is called.
	 * 
	 * @param pExitOnDispose <code>true</code> to call {@link System#exit(int)}, <code>false</code> otherwise
	 */
	public void setSystemExitOnDispose(boolean pExitOnDispose)
	{
		bSystemExitOnDispose = pExitOnDispose;
	}
	
	/**
	 * Gets whether {@link System#exit(int)} will be called if dispose is called.
	 * 
	 * @return <code>true</code> if {@link System#exit(int)} will be called, <code>false</code> otherwise
	 */
	public boolean isSystemExitOnDispose()
	{
		return bSystemExitOnDispose;
	}
	
	/**
	 * Sets the full screen mode.
	 * 
	 * @param pFullScreen <code>true</code> to enable full screen mode, <code>false</code> to set normal mode
	 */
	public void setFullScreen(boolean pFullScreen)
	{
	    if (pFullScreen)
	    {
	        if (jfMax == null)
	        {
        	    jfMax = new JFrame(resource.getGraphicsConfiguration());
        	    jfMax.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        	    jfMax.setUndecorated(true);
        	    jfMax.setExtendedState(JFrame.MAXIMIZED_BOTH); 
                jfMax.setContentPane(resource.getContentPane());
                jfMax.setIconImage(resource.getIconImage());
                jfMax.setTitle(resource.getTitle());

                resource.setVisible(false);
                jfMax.setVisible(true);
	        }
	    }
	    else
	    {
	        resource.setContentPane(jfMax.getContentPane());
	        
            jfMax.setVisible(false);
            jfMax.dispose();
            jfMax = null;

            resource.setVisible(true);
            
	    }
	}

	/**
	 * Gets whether full screen mode is active.
	 * 
	 * @return <code>true</code> if full screen mode is active, <code>false</code> otherwise
	 */
	public boolean isFullScreen()
	{
	    return jfMax != null;
	}
	
	/**
	 * Gets the main class name from system environment.
	 * 
	 * @return the main class name
	 */
	private static String getClassNameFromEnvironment()
	{
		String sClassName = System.getProperty("SwingApplication.class.name");
		
		if (StringUtil.isEmpty(sClassName))
		{
			int iMax = 0;
			int iCurrent;
			
			for (Map.Entry<String, String> entry : System.getenv().entrySet())
			{
				if (entry.getKey().startsWith("JAVA_MAIN_CLASS")) // like JAVA_MAIN_CLASS_12345
				{
					try
					{
						iCurrent = Integer.parseInt(entry.getKey().substring(16));
					}
					catch (Exception e)
					{
						iCurrent = 1;
					}
					
					if (iCurrent > iMax)
					{
						iMax = iCurrent;
						
						sClassName = entry.getValue();
					}
				}
			}
		}
		
		return sClassName;
	}
	
}	// SwingApplication
