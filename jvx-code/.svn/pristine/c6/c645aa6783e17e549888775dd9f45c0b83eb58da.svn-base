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
 * 27.10.2008 - [JR] - parseUrlParameters implemented
 * 01.11.2008 - [JR] - replaceParameter used
 *                   - setParameter implemented
 * 14.11.2008 - [JR] - moved start() code to init() -> since 1.6u10 the applet is shown after init!
 * 25.11.2008 - [JR] - removed setEventDispatcher    
 * 04.12.2008 - [JR] - handled exceptions with error frame              
 * 06.12.2008 - [JR] - getParameter: supported \n     
 * 26.01.2009 - [JR] - showDocument: first - try to open a window (popup) with a predefined size         
 * 17.02.2009 - [JR] - set/getCursor implemented
 * 02.04.2009 - [JR] - setJSCallBackIntern: handled with invokeLater        
 * 24.04.2009 - [JR] - initialized the log factory     
 * 28.05.2009 - [JR] - use the content pane from the frame as content pane for the sub root pane [BUGFIX]
 * 04.06.2009 - [JR] - loadConfiguration: search xml configuration if configured 
 * 11.06.2009 - [JR] - loadConfiguration: don't overwrite applet tag parameters 
 * 20.06.2009 - [JR] - setIconImage: NullPointerException with JNLP [BUGFIX] 
 *                     getFileHandle: BUTTONTEXT übertragen
 *                     getFileHandle: INFOTEXT nicht als Parameter auslesen [BUGFIX]#
 * 23.06.2009 - [JR] - dispose: addNotify called [BUGFIX]   
 * 31.07.2009 - [JR] - getFileHandle: set WAIT cursor
 * 05.08.2009 - [JR] - getFileHandle: CancelButton
 * 06.08.2009 - [JR] - setMenuBar: setParent and set local instance [BUGFIX]
 * 13.08.2009 - [JR] - getFileHandle: don't show upload page when an error occured after file dialog open [BUGFIX]
 * 17.08.2009 - [JR] - getFileHandle: raise Exception when html upload has errors                   
 *                   - getFileHandle/saveFileHandle: set last selected directory
 * 19.08.2009 - [JR] - JNLP exit support, jdk 1.6 >= u13 addNotify/start changed call order supported
 *                   - save/open filehandle: webstart support                  
 * 03.09.2009 - [JR] - showDocument: don't call twice, because the frame is always != null [BUGFIX] 
 * 05.09.2009 - [JR] - getFileHandle: WEBSTART identifier added to query parameters                    
 * 20.10.2009 - [JR] - get/setRegistryKey: don't access application 
 *                     -> maybe called from applications constructor [BUGFIX]
 * 23.10.2009 - [JR] - setIconImage: cast to ImageIcon [BUGFIX]
 *                   - setRegistryKey: used parameter from launcher [BUGFIX]      
 * 28.10.2009 - [JR] - setTitle/getTitle/toFront: try jscript before frame because frame is always != null [BUGFIX]
 * 14.11.2009 - [JR] - get/setRegistryKey: used ApplicationUtil for application name detection    
 * 13.09.2010 - [JR] - used ApplicationUtil to load the configuration
 * 22.12.2010 - [JR] - setMenuBar: null check
 * 30.12.2010 - [JR] - "pack" works now in Browser mode
 * 03.02.2011 - [JR] - invokeLater added
 * 18.03.2011 - [JR] - logging instead of printStackTrace
 * 01.05.2011 - [JR] - removed setCursor because it is already implemented in SwingAbstractFrame
 * 15.04.2012 - [JR] - #572: put the application as client property
 * 17.07.2012 - [JR] - configurable factory class
 * 21.12.2012 - [JR] - getApplicationName now checks connection == null [BUGFIX]
 * 22.12.2012 - [JR] - #614: handleDownload, getFileHandle now send CONURL as additional parameter
 * 13.08.2013 - [JR] - #756: changed set menu
 * 14.09.2013 - [JR] - removed final from class
 * 24.04.2014 - [JR] - #1018: use framebounds system property for frame location and size
 * 21.12.2016 - [JR] - #1714: debug log
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.rad.application.IApplication;
import javax.rad.application.IConnectable;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIFactoryManager;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IImage;
import javax.rad.ui.IRectangle;
import javax.rad.ui.UIException;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.util.ExceptionHandler;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.Webstart;
import com.sibvisions.rad.ui.awt.impl.AwtCursor;
import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.ext.JVxDesktopPane;
import com.sibvisions.rad.ui.swing.impl.container.SwingAbstractFrame;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.FileViewer;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.TimeZoneUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>SwingApplet</code> is the {@link IApplication} implementation
 * for swing applets with full access to the {@link JApplet}.
 * 
 * The {@link ILauncher#PARAM_SERVERBASE} will be set to the URL path, e.g. http://localhost/demo
 * typed in the browsers adress bar. Additional the parameter {@link ILauncher#PARAM_CODEBASE} will be set
 * to the current codebase and the parameter <code>Launcher.documentBase</code> will
 * be set to the applets documentbase.
 * 
 * @author René Jahn
 */
public class SwingApplet extends JApplet
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the property name for the application (client properties). */
	public static final String PROPERTY_APPLICATION = "application";

	/** the root node identifier for application configuration xml files. */
	private static final String CONFIG_ROOT_NODE = "application";

	
	/** the ILauncher that represents this applet. */
	private SwingAppletLauncher launcher;
	
	/** the frame of the applet, if started via jnlp. */
	private Frame frame = null;
	
	/** the javascript bridge for the browser. */
	private JSBridge jscript = null;
	
	/** Speichert alle Upload Threads. */
	private Vector<Thread> uploadThreads = new Vector<Thread>();
	
	/** the startup thread when the appliet was notified. */
	private Thread thStartup;
	
	/** the old window listeners, if available. */
	private ArrayUtil<WindowListener> lisApplet;

	/** tha flag indicates that addNotify was called. */
	private boolean bNotified = false;

	/** tha flag indicates whether the application is already notified being started. */
	private boolean bApplicationStarted = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a SwingApplet.
	 */
	public SwingApplet()
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
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		catch (HeadlessException he)
		{
			//problems with GraphicsEnvironment
		}
		
		try
		{
			Class.forName("javax.swing.text.html.parser.ParserDelegator").newInstance();
		}
		catch (Throwable th)
		{
			// no ParserDelegator loaded.
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts an applet.
	 * The parameter "main" should give the full qualified class name of
	 * the application to run.
	 * <pre>
	 * &lt;PARAM NAME = "main" VALUE = "test.MyApplication"&gt;
	 * </pre>
	 */
	@Override
	public void init()
	{
		final String sApplicationClassName = getParameter("main");
		
		if (sApplicationClassName == null || sApplicationClassName.length() == 0)
		{
			throw new UIException("The application parameter 'main' was not found!");
		}

		if (bNotified)
		{
			initLauncher(sApplicationClassName);
		}
		else if (thStartup == null)
		{
			thStartup = new Thread()
			{
				public void run()
				{
					synchronized (thStartup)
					{
						thStartup.notifyAll();
						
						try
						{
							thStartup.wait(5000);
						}
						catch (InterruptedException ie)
						{
							//nothing to be done
						}
					}

					try
					{
						SwingUtilities.invokeAndWait(new Runnable()
						{
							public void run()
							{
								initLauncher(sApplicationClassName);
							}
						});
					}
					catch (InterruptedException e)
					{
						initLauncher(sApplicationClassName);
					}
					catch (InvocationTargetException e)
					{
						LoggerFactory.getInstance(getClass()).error("init launcher failed", e);
					}
					
					thStartup = null;
				}
			};
			
			synchronized (thStartup)
			{
				thStartup.start();
				
				try
				{
					thStartup.wait();
				}
				catch (InterruptedException ie)
				{
					//nothing to be done
				}
			}
		}
	}
	
	/**
	 * Inform the applet that it is being reclaimed and that it
	 * should destroy any resources that it has allocated.
	 * It also notifies the implementation base. 
	 */
	@Override
	public void destroy()
	{
		if (launcher != null)
		{
			launcher.destroy();
			launcher = null;
		}
		
		if (Webstart.isJnlp() && frame != null)
		{				
			//Webstart (JNLP) with a userdefined RootPane throws a SecurityException when
			//the EXIT button will be pressed. The problem occurs because the userdefined
			//RootPane doesn't have the privilege to access system properties like jnlpx.remove
			//This workaround is important!
			//
			//If the frame will be closed through the default way, the following statement
			//will be ignored :)
			try
			{
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			catch (Throwable th)
			{
				//nothing to be done!
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					//works with some VMs
					try
					{
						System.exit(0);
					}
					catch (Throwable th)
					{
						//nothing to be done
					}
				}
			});
		}
	}
	
	/**
	 * Tries to get the webstart frame and removes the status 
	 * label.
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();

		searchFrame();
		
		bNotified = true;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new <code>SwingAppletLauncher</code> instance.
	 * 
	 * @param pApplicationName the full qualified application name
	 */
	private void initLauncher(String pApplicationName)
	{
		launcher = new SwingAppletLauncher(this, pApplicationName);
	}
	
	/**
	 * Searches the applet frame and removes unwanted labels.
	 */
	private void searchFrame()
	{
		if (getParent() != null && frame == null)
		{
			Container cont = this;
	
			//search and cache the frame for later use
			while (cont != null && !(cont instanceof Frame))
			{
				cont = cont.getParent();
	
				if (cont != null)
				{
					//hide the status label!
					Component com;
					for (int i = 0, anz = cont.getComponentCount(); i < anz; i++)
					{
						com = cont.getComponent(i);
						if (com instanceof JLabel || com instanceof Label)
						{
							com.setVisible(false);
						}
					}
				}
			}
		
			if (cont != null)
			{
				frame = (Frame)cont;
				
				if (frame instanceof JFrame)
				{
			        ((JFrame)frame).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			        
			        //otherwise, closing would close
			        WindowListener[] lis = ((JFrame)frame).getWindowListeners();
			        
			        for (int i = 0; i < lis.length; i++)
			        {
			            if (lis[i] != this)
			            {
			                if (lisApplet == null)
			                {
			                    lisApplet = new ArrayUtil<WindowListener>();
			                }
			                
			                lisApplet.add(lis[i]);
			                
			                ((JFrame)frame).removeWindowListener(lis[i]);
			            }
			        }
				}
			}
		}
		
		if (thStartup != null && launcher == null)
		{
            synchronized (thStartup)
            {
                thStartup.notify();
            }
		}
	}

	/**
	 * Gets whether Javascript is available.
	 * 
	 * @return <code>true</code> if javascript calls are possible, <code>false</code> otherwise
	 */
	public boolean isJavaScriptAvailable()
	{
		return jscript != null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>SwingAppletLauncher</code> is the {@link ILauncher} implementation
	 * for swing applet applications with full access to the {@link JApplet}.
	 * 
	 * @author Martin Handsteiner
	 */
	public class SwingAppletLauncher extends SwingAbstractFrame<JApplet> 
	                                 implements ILauncher,
	                                        	ComponentListener,
	                                        	WindowListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the {@link UILauncher} instance. */
		private UILauncher uilauncher;

		/** the internal non modal root pane. */
		private JRootPane rootPane;
		
		/** the desktop pane for modal internal frames. */
		private JVxDesktopPane desktopModal;

		/** the application implementation base. */
		private IApplication application = null;
		
		/** the parameters of the applet. */
		private HashMap<String, String> hmpParams = null;
		
		/** the file chooser. */
		private FileChooser fileChooser = null;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>SwingFrame</code>.
		 * 
		 * @param pApplet the SwingApplet.
		 * @param pApplicationClassName the full qualified class name of the {@link IApplication} to run.
		 */
		public SwingAppletLauncher(JApplet pApplet, String pApplicationClassName)
		{
			super(pApplet);
			
			parseUrlParams();

			loadConfiguration();
			
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

            try
			{
				jscript = new JSBridge(pApplet);
				
				hmpParams.put("Launcher.canExit", "false");
			}
			catch (Throwable th)
			{
				//not possible, maybe webstart or appletviewer
				jscript = null;
			}
			
			SwingFactory.setLookAndFeel(this, getParameter("Application.LookAndFeel"));
			
			Class<?> clazz; 

			try
			{
				clazz = Class.forName(getParameter(ILauncher.PARAM_UIFACTORY));
			}
			catch (Exception e)
			{
				clazz = SwingFactory.class;
				
	            LoggerFactory.getInstance(SwingApplet.class).debug("Fallback to SwingFactory", e);
			}
			
			setFactory(UIFactoryManager.getFactoryInstance(clazz));
			
			//Create a root pane for the modal layer
	    	rootPane = new JRootPane();
	    	rootPane.setOpaque(false);
	    	//keep the parents toolbarPanel!
	    	//without we need a new ToolBarPanel and overwrite all addToolbar methods!
	    	rootPane.setContentPane(resource.getContentPane());
			
	    	desktopModal = new JVxDesktopPane();
	    	desktopModal.add(rootPane);
	    	
	    	resource.setContentPane(desktopModal);

	    	uilauncher = new UILauncher(this);
	    	
	    	try
	    	{
	    		application = LauncherUtil.createApplication(uilauncher, pApplicationClassName);
	    		
	    		uilauncher.setTitle(application.getName());
	    		
	    		//when started via JNLP the "frame" will be available, don't add the componentlistener here!
	    		//look at addNotify()
				if (frame == null)
				{
					resource.addComponentListener(this);
				}
		    	
		    	//add the application
				uilauncher.add(application, BorderLayout.CENTER);
				
	        	resource.getRootPane().putClientProperty(PROPERTY_APPLICATION, application);
				
				//ensure that the application gets notifyVisible when the frame is visible!
				if (frame != null)
				{
				    if (frame instanceof JFrame)
				    {
				        if (eventWindowClosing == null || eventWindowClosing.getDefaultListener() == null)
				        {
				            eventWindowClosing().setDefaultListener(this, "dispose");
				        }
				    }
				    
					startDelayed();
				}
	    	}
	    	catch (Throwable th)
	    	{
	    		//to show the error centered!
	    		desktopModal.setSize(resource.getSize());
	    		
	    		handleException(th);
	    	}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	 	/**
	 	 * {@inheritDoc}
	 	 */
	 	@Override
		protected void setLayoutIntern(LayoutManager pLayoutManager)
		{
			rootPane.getContentPane().setLayout(pLayoutManager);
		}	 	

	 	/**
	 	 * {@inheritDoc}
	 	 */
	 	@Override
	 	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	 	{
	 		rootPane.getContentPane().add(pComponent, pConstraints, pIndex);
	 	}

	 	/**
	 	 * {@inheritDoc}
	 	 */
	 	@Override
	 	protected void removeIntern(Component pComponent)
	 	{
	 		rootPane.getContentPane().remove(pComponent);
	 	}
	 	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IImage capture(int pWidth, int pHeight)
		{
			return new SwingImage(null, new ImageIcon(createImage(resource.getRootPane(), pWidth, pHeight)));
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Abstract methods implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
	    protected void addWindowListenerToResource()
	    {
	        if (frame != null)
	        {
	            frame.addWindowListener(this);
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
	    	String sValue;
	    	
	    	//Use userdefined parameters before using the applet parameters
	    	if (hmpParams.containsKey(pName))
	    	{
	    		sValue = hmpParams.get(pName);
	    	}
	    	else
	    	{
	    		sValue = resource.getParameter(pName);
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
	    	try 
	    	{
		    	FileViewer.open(pDocumentname);
	    	}
	    	catch (Throwable pThrowable)
	    	{
				if (jscript == null)
				{
					//Open the document via AppletContext
					resource.getAppletContext().showDocument(new URL(pDocumentname), pTarget);
				}
				else
				{
					try
					{
						int iX;
						int iY;
						int iWidth;
						int iHeight;
						
						if (pBounds == null)
						{
							iX = 20;
							iY = 20; 
							iWidth = 800;
							iHeight = 600;					
						}
						else
						{
							iX = pBounds.getX();
							iY = pBounds.getY();
							iWidth = pBounds.getWidth();
							iHeight = pBounds.getHeight();
						}
						
						jscript.openWindow(pDocumentname, pTarget, iX, iY, iWidth, iHeight, true);
					}
					catch (Throwable th)
					{
						//Open the document via AppletContext
						resource.getAppletContext().showDocument(new URL(pDocumentname), pTarget);
					}
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
	    		// Signed Applet tries to store the file local.
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
	    	catch (Throwable pThrowable)
	    	{
    			handleDownload(pFileHandle, pBounds, pTarget);
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
	    		//Signed Applet?
	    		
	    		if (fileChooser == null)
	    		{
	    			fileChooser = new FileChooser();
	    		}
	    		
	    		if (EventQueue.isDispatchThread())
	    		{
		    		fileChooser.saveAs(uilauncher, frame, pFileHandle, pTitle);
	    		}
	    		else
	    		{
		    		SwingUtilities.invokeAndWait(new Runnable()
					{
		    			public void run()
		    			{
		    				try
		    				{
		    		    		fileChooser.saveAs(uilauncher, frame, pFileHandle, pTitle);
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
	    			handleDownload(pFileHandle, null, "_blank");
	    		}
	    	}
	    }
		
		/**
		 * {@inheritDoc}
		 */
	    public void getFileHandle(final IFileHandleReceiver pFileHandleReceiver, final String pTitle) throws Throwable
	    {
	    	IFileHandle[] fhSelected = null;
	    	
	    	try
	    	{
	    		//Signed Applet?
	    		
	    		if (fileChooser == null)
	    		{
	    			fileChooser = new FileChooser();
	    		}
	    		
	    		if (EventQueue.isDispatchThread())
	    		{
	    			fhSelected = fileChooser.open(uilauncher, frame, pTitle);
	    		}
	    		else
	    		{
		    		final List<IFileHandle[]> liExchange = new ArrayUtil<IFileHandle[]>();
		
		    		SwingUtilities.invokeAndWait(new Runnable()
					{
		    			public void run()
		    			{
		    				try
		    				{
		    		    		liExchange.add(fileChooser.open(uilauncher, frame, pTitle));
		    				}
		    				catch (Exception e)
		    				{
		    					throw new RuntimeException(e);
		    				}
		    			}
					});
		    		
		    		fhSelected = liExchange.get(0);
	    		}
	    	}
	    	catch (SecurityException se)
	    	{
		    	if (Webstart.isJnlp())
		    	{
					fhSelected = Webstart.showOpenDialog(true);
		    	}
		    	else
		    	{
		    	    HttpConnection con = getHttpConnection();
		    	    
                    if (con != null)
                    {
                        String sURL = ((HttpConnection)con).getUploadURL();
                        
                        if (sURL != null)
                        {
                            final RemoteFileHandle tempFile = con.createRemoteFileHandle();
                            
                            String sAppName = getApplicationName();
                            
                            StringBuilder sbUrl = new StringBuilder();
                            sbUrl.append(sURL);

                            if (sURL.indexOf('?') > 0)
                            {
                                sbUrl.append("&");
                            }
                            else
                            {
                                sbUrl.append("?");
                            }
                            
                            //save for use it in Thread
                            final String sUploadURL = sbUrl.toString();
                            
                            if (sAppName != null)
                            {
                                sbUrl.append("APPLICATION=");
                                sbUrl.append(CodecUtil.encodeURLParameter(sAppName));
                                sbUrl.append("&");
                            }
                            
                            //Webstart -> don't call showDocument to upload a file, because a new tab will be opened in an existing browser, if a browser is open!
                            //-> "open the browser" via showDocument and let the browser open a window!
                            if (Webstart.isJnlp())
                            {
                                sbUrl.append("WEBSTART=Y&");
                            }
                            
                            Object oConId = getConnectionId();
                            
                            sbUrl.append("TITLE=");
                            sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate(pTitle == null ? "Open file" : pTitle)));
                            sbUrl.append("&INFOTEXT=");
                            sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Please choose the file:")));
                            sbUrl.append("&UPLOADBUTTON=");
                            sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Upload")));
                            sbUrl.append("&CANCELBUTTON=");
                            sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Cancel")));
                            sbUrl.append("&KEY=");
                            sbUrl.append(tempFile.getObjectCacheKey());
                            sbUrl.append("&CONNECTION_ID=");
                            sbUrl.append(CodecUtil.encodeHex(oConId != null ? oConId.toString() : null));
                            
                            String sServerBase = getParameter(ILauncher.PARAM_SERVERBASE);
                            
                            if (sServerBase != null)
                            {
                                sbUrl.append("&CONURL=");
                                sbUrl.append(CodecUtil.encodeURLParameter(sServerBase));
                            }
                            
                            showDocument(sbUrl.toString(), null, "Loaddialog");

                            uploadThreads.add(ThreadHandler.start(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        ICursor curOld = getCursor();
            
                                        try
                                        {
                                            URL url = new URL(sUploadURL + "WAIT=true&KEY=" + tempFile.getObjectCacheKey());
                                            String command = "WAIT";
                                            
                                            while ("WAIT".equals(command) && !ThreadHandler.isStopped())
                                            {
                                                //maybe reset by SwingButton or SwingMenuItem 
                                                setCursor(AwtCursor.getPredefinedCursor(ICursor.WAIT_CURSOR));
            
                                                URLConnection urlConnection = url.openConnection();
                                                urlConnection.setUseCaches(false);
                                                urlConnection.setDoInput(true);
                                                
                                                DataInputStream stream = new DataInputStream(urlConnection.getInputStream());
                                                command = stream.readUTF();
            
                                                if ("FILENAME".equals(command))
                                                {
                                                    tempFile.setFileName(stream.readUTF());
                                                }
                                                
                                                stream.close();
                                            }
                                            
                                            if ("FILENAME".equals(command))
                                            {
                                                SwingUtilities.invokeLater(new Runnable()
                                                {
                                                    public void run()
                                                    {
                                                    	try
                                                    	{
                                                    		pFileHandleReceiver.receiveFileHandle(tempFile);
                                                    	}
                                                    	catch (Throwable ex)
                                                    	{
                                                    		ExceptionHandler.show(ex);
                                                    	}
                                                    }
                                                });
                                            }
                                        }
                                        catch (final Exception ex)
                                        {
                                            try
                                            {
                                                //try to use the same UI thread
                                                SwingUtilities.invokeLater(new Runnable()
                                                {
                                                    public void run()
                                                    {
                                                        ExceptionHandler.show(ex);
                                                    }
                                                });
                                            }
                                            catch (Exception e)
                                            {
                                                //the only way to send the exception!
                                                ExceptionHandler.show(ex);
                                            }
                                        }
                                        finally
                                        {
                                            uploadThreads.remove(Thread.currentThread());
                                            setCursor(curOld);
                                        }
                                    }
                                }
                            ));
                        }
		            }
		    	}
	    	}
	    	
	    	if (fhSelected != null)
			{
	    		final IFileHandle[] finalSelectedFiles =  fhSelected;
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
	    	Vector<Thread> vCopy = (Vector<Thread>)uploadThreads.clone();
	    	
	    	for (Thread thread : vCopy)
	    	{
	    		ThreadHandler.stop(thread);
	    	}
	    }
	    
		/**
		 * {@inheritDoc}
		 */
	    public void setRegistryKey(String pKey, String pValue)
	    {
	    	String sName = LauncherUtil.getRegistryApplicationName(this);
	    	
	    	try
	    	{
	    		LauncherUtil.setRegistryKey(sName, pKey, pValue);
	    	}
	    	catch (SecurityException se)
	    	{
	    		if (jscript != null)
	    		{
	    			try
	    			{
	    				jscript.setCookie(sName + "." + pKey, pValue, 30758400L);
		    		}
		    		catch (Throwable th)
		    		{
		    			//nothing to be done
		    		}
	    		}
	    		else if (Webstart.isJnlp())
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
	    	String sName = LauncherUtil.getRegistryApplicationName(this);
	    	
	    	try
	    	{
	    		return LauncherUtil.getRegistryKey(sName, pKey);
	    	}
	    	catch (SecurityException se)
	    	{
	    		if (jscript != null)
	    		{
	    			try
	    			{
	    				return jscript.getCookie(sName + "." + pKey);
		    		}
		    		catch (Throwable th)
		    		{
		    			//nothing to be done
		    		}
	    		}
	    		else if (Webstart.isJnlp())
	    		{
	    			return Webstart.getProperty(pKey);
	    		}
	    		
	    		return null;
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
			SwingFactory.showError(pThrowable, desktopModal);
		}
		
		//IFRAME
		
		/**
		 * {@inheritDoc}
		 */
		public String getTitle()
		{
			if (jscript != null)
			{
				try
				{
					return jscript.getTitle();
				}
				catch (Throwable th)
				{
					//nothing to be done!
				}
			}
			else if (frame != null)
			{
				return frame.getTitle();
			}
			
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setTitle(String pTitle)
		{
			if (jscript != null)
			{
				try
				{
					jscript.setTitle(pTitle);
				}
				catch (Throwable th)
				{
					//nothing to be done!
				}
			}
			else if (frame != null)
			{
				frame.setTitle(pTitle);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void setIconImage(IImage pImage)
		{
			iconImage = pImage;
			
			if (frame != null)
			{
				if (pImage == null)
				{
					frame.setIconImage(null);
				}
				else
				{
					frame.setIconImage(((ImageIcon)pImage.getResource()).getImage());
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
	    public int getState() 
	    {
	    	if (frame != null)
	    	{
	    		return frame.getState();
	    	}
	    	else
	    	{
		    	return NORMAL;
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */
	    public void setState(int pState)
	    {
	    	if (frame != null)
	    	{
	    		frame.setState(pState);
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */
	    public boolean isResizable()
	    {
	    	if (frame != null)
	    	{
	    		return frame.isResizable();
	    	}
	    	else
	    	{
	    		return true;
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */
	    public void setResizable(boolean pResizable)
	    {
	    	// won't work in Browser
	    	
	    	if (Webstart.isJnlp() && frame != null)
	    	{
	    		frame.setResizable(pResizable);
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */	
		public void pack()
		{
			// won't work in Browser
			
			if (Webstart.isJnlp() && frame != null)
			{
				frame.pack();
			}
			else
			{
				resource.validate();
				resource.repaint();
			}
		}

		/**
		 * {@inheritDoc}
		 */	
		public void dispose()
		{
			if (application != null && bApplicationStarted)
			{
				bApplicationStarted = false;
				
				try
				{
					application.notifyDestroy();
				}
				catch (Exception e)
				{
					//force closing the application
					uilauncher.error(e);
				}
			}

			if (frame instanceof JFrame)
			{
    	        Rectangle rectBounds = frame.getBounds();
    	        
    	        int iState = ((JFrame)frame).getExtendedState();
    			
    	        setRegistryKey("framebounds", rectBounds.x + "," + rectBounds.y + "," + rectBounds.width + "," + rectBounds.height);
    	        setRegistryKey("maximized", "" + (iState == MAXIMIZED_BOTH || iState == MAXIMIZED_HORIZ || iState == MAXIMIZED_VERT));
			}
			
			//recover listeners
			if (lisApplet != null)
			{
			    ((JFrame)frame).removeWindowListener(this);
			    
			    for (int i = 0, cnt = lisApplet.size(); i < cnt; i++)
			    {
			        ((JFrame)frame).addWindowListener(lisApplet.get(i));
			    }
			}
			
			if (Webstart.isJnlp() && frame != null)
			{				
				//JNLP Applet is not allowed to call System.exit without security settings, so post the WINDOW_CLOSING
				//event -> Main.systemExit will be called from sun classes :)
				try
				{
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				catch (Throwable th)
				{
					//nothing to be done!
				}
			}
			
			try
			{
				System.exit(0);
			}
			catch (Exception e)
			{
				if (jscript != null)
				{
					try
					{
						jscript.close();
					}
					catch (Throwable th)
					{
						//nothing to be done!
					}
				}
			}
			
			//if the applet is still showing!!!
			if (isShowing())
			{
				uilauncher.addNotify();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isActive()
		{
			if (frame != null)
			{
				return frame.isActive();
			}
			else
			{
				return resource.isActive();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void toFront()
		{
			if (jscript != null)
			{
				try
				{
					jscript.requestFocus();
				}
				catch (Throwable th)
				{
					//nothing to be done!
				}
			}
			else if (frame != null)
			{
				frame.toFront();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void toBack()
		{
			// Can't be done with Browser
			
			if (frame != null)
			{
				frame.toBack();
			}
		}
		
	    /**
		 * {@inheritDoc}
		 */
		public void setMenuBar(IMenuBar pMenuBar)
		{
			if (menuBar != null)
			{
				menuBar.setParent(null);
			}

			IContainer conOldParent = null;

			if (pMenuBar != null)
			{
				conOldParent = pMenuBar.getParent();

				pMenuBar.setParent(this);
			}

			try
			{
				if (pMenuBar != null)
				{
					rootPane.setJMenuBar((JMenuBar)pMenuBar.getResource());
				}
				else
				{
					rootPane.setJMenuBar(null);
				}
			}
			catch (RuntimeException re)
			{
				if (pMenuBar != null)
				{
					pMenuBar.setParent(conOldParent);
				}

				throw re;
			}
			catch (Error e)
			{
				if (pMenuBar != null)
				{
					pMenuBar.setParent(conOldParent);
				}

				throw e;
			}

			menuBar = pMenuBar;
		}
	     
		/**
		 * {@inheritDoc}
		 */
		public void centerRelativeTo(IComponent pComponent)
		{
			// Can't be done with Browser
			
			if (frame != null)
			{
			    if (pComponent != null)
			    {
    	            frame.setLocationRelativeTo((Component)pComponent.getResource());
    	        }
    	        else
    	        {
    	            frame.setLocationRelativeTo(null);
    	        }
			}
		}
		
		//COMPONENTLISTENER
		
		/**
		 * {@inheritDoc}
		 */
		public void componentHidden(ComponentEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void componentMoved(ComponentEvent pEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void componentResized(ComponentEvent pEvent)
		{
		}

		/**
		 * Sends the visible notification to the implementation base.
		 * 
		 * @param pEvent the triggering event
		 */
		public void componentShown(ComponentEvent pEvent)
		{
			start();
		}		
		
	    // WindowListener

	    /**
	     * {@inheritDoc}
	     */
	    public void windowOpened(WindowEvent pEvent)
	    {
	        //too late because window is already open
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void windowClosing(WindowEvent pEvent)
	    {
	        if (eventWindowClosing != null)
	        {
	            eventWindowClosing.dispatchEvent(new UIWindowEvent(eventSource, 
	                                                               UIWindowEvent.WINDOW_CLOSING, 
	                                                               AwtFactory.getMostRecentEventTime(),
	                                                               AwtFactory.getCurrentModifiers()));
	        }
	    }

	    /**
	     * Removes the internal frame from it's parent.
	     * 
	     * @param pEvent the triggering event 
	     */
	    public void windowClosed(WindowEvent pEvent)
	    {
	        if (eventWindowClosed != null)
	        {
	            eventWindowClosed.dispatchEvent(new UIWindowEvent(eventSource,
	                                                              UIWindowEvent.WINDOW_CLOSED,
	                                                              AwtFactory.getMostRecentEventTime(),
	                                                              AwtFactory.getCurrentModifiers()));
	        }
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void windowActivated(WindowEvent pEvent)
	    {
	        if (eventWindowActivated != null)
	        {
	            eventWindowActivated.dispatchEvent(new UIWindowEvent(eventSource, 
	                                                                 UIWindowEvent.WINDOW_ACTIVATED, 
	                                                                 AwtFactory.getMostRecentEventTime(),
	                                                                 AwtFactory.getCurrentModifiers()));
	        }
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void windowDeactivated(WindowEvent pEvent)
	    {
	        if (eventWindowDeactivated != null)
	        {
	            eventWindowDeactivated.dispatchEvent(new UIWindowEvent(eventSource, 
	                                                                   UIWindowEvent.WINDOW_DEACTIVATED, 
	                                                                   AwtFactory.getMostRecentEventTime(),
	                                                                   AwtFactory.getCurrentModifiers()));
	        }
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void windowIconified(WindowEvent pEvent)
	    {
	        if (eventWindowIconified != null)
	        {
	            eventWindowIconified.dispatchEvent(new UIWindowEvent(eventSource, 
	                                                                 UIWindowEvent.WINDOW_ICONIFIED, 
	                                                                 AwtFactory.getMostRecentEventTime(),
	                                                                 AwtFactory.getCurrentModifiers()));
	        }
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void windowDeiconified(WindowEvent pEvent)
	    {
	        if (eventWindowDeiconified != null)
	        {
	            eventWindowDeiconified.dispatchEvent(new UIWindowEvent(eventSource, 
	                                                                   UIWindowEvent.WINDOW_DEICONIFIED, 
	                                                                   AwtFactory.getMostRecentEventTime(),
	                                                                   AwtFactory.getCurrentModifiers()));
	        }
	    }

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // User-defined methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	    /**
	     * Starts the application and tries to wait until the size is available. That's important
	     * that the application can be sure that the size is not 0 when layouting.
	     */
	    public void startDelayed()
	    {
			ThreadHandler.start(new Runnable()
			{
				public void run()
				{
					int iRetries = 0;
					
					try
					{
						while ((application.getSize().getWidth() <= 0 || application.getSize().getHeight() <= 0) 
							   && iRetries < 50)
						{
							iRetries++;
							
							Thread.sleep(20);
						}
	
						start();
					}
					catch (Throwable th)
					{
						start();
					}
				}
			});
	    }
	    
		/**
		 * Applet is visible.
		 */
		public synchronized void start()
		{
			if (!bApplicationStarted)
			{
				bApplicationStarted = true;

				//necessary to set here because the frame has the full size
				if (frame != null)
				{
					frame.setResizable(true);
					
					if (!LauncherUtil.configureFrameBounds(this, frame))
					{
					    frame.setLocationRelativeTo(null);
					}
				}
	
				//call addNotify -> not called through the Applet
		    	uilauncher.setVisible(true);
				
		    	if (eventWindowOpened != null)
		    	{
		    		eventWindowOpened.dispatchEvent(new UIWindowEvent(eventSource, 
		    														  UIWindowEvent.WINDOW_OPENED,
		    														  AwtFactory.getMostRecentEventTime(),
		    														  AwtFactory.getCurrentModifiers()));
		    	}
		    	
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
		 * Applet will be destroyed.
		 */
		public void destroy()
		{
			if (application != null && bApplicationStarted)
			{
				bApplicationStarted = false;
				
				application.notifyDestroy();
			}

	    	if (frame == null && eventWindowClosed != null)
	    	{
	    		eventWindowClosed.dispatchEvent(new UIWindowEvent(eventSource,
	    														  UIWindowEvent.WINDOW_CLOSED,
	    														  AwtFactory.getMostRecentEventTime(),
	    														  AwtFactory.getCurrentModifiers()));
	    	}
		}
		
		/**
		 * Loads the xml application configuration. The values from the xml does not override the
		 * params within the applet tag. 
		 */
		private void loadConfiguration()
		{
			String sUrlConfig = getParameter("config");
			
			//the configuration can be configured via an URL Parameter or an APPLET
			//parameter within the APPLET tag.
			if (sUrlConfig == null)
			{
				sUrlConfig = "application.xml";
			}
				
			try
			{
				XmlNode xmnAppConfig = LauncherUtil.getConfig(sUrlConfig);
				
				if (xmnAppConfig != null)
				{
					String sValue;
					
					for (XmlNode xmnSub : xmnAppConfig.getNode(CONFIG_ROOT_NODE).getNodes())
					{
						//don't overwrite parameters configured via URL!!!
						if (!hmpParams.containsKey(xmnSub.getName()))
						{
							//use the applet tag params before using the configuration
							sValue = resource.getParameter(xmnSub.getName());
							
							if (sValue == null)
							{
								hmpParams.put(xmnSub.getName(), xmnSub.getValue());
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(getClass()).error("Configuration load error!", e);
			}
		}
		
		/**
		 * Parses the parameters from the document base. The parameters are available
		 * through the {@link ILauncher} implementation.
		 * 
		 * @see ILauncher#getParameter(String)
		 */
		private void parseUrlParams()
		{
			URL urlDocBase = resource.getDocumentBase();
			String sUrl = urlDocBase.getQuery();
			
			hmpParams = new HashMap<String, String>();

			hmpParams.put(ILauncher.PARAM_CODEBASE, resource.getCodeBase().toString());
			hmpParams.put("Launcher.documentBase", urlDocBase.toString());

			//detect the applet serverbase
			String sServer = urlDocBase.getProtocol() + "://" + urlDocBase.getAuthority();
			
			if (urlDocBase.getPath() != null)
			{
				int iPos = urlDocBase.getPath().lastIndexOf('/');
				
				sServer += urlDocBase.getPath().substring(0, iPos);
			}
			
			//if SERVERBASE is defined in html, don't override it with Browser URL!
			//normally, the application uses its application.xml for URL definition,
			//but sometimes we have to change it outside the application. If we
			//set the value in any case, we have no chance to force another URL!
			if (StringUtil.isEmpty(resource.getParameter(ILauncher.PARAM_SERVERBASE)))
			{
				hmpParams.put(ILauncher.PARAM_SERVERBASE, sServer);
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
						hmpParams.put(sParam.substring(0, iPos), sParam.substring(iPos + 1));
					}
				}
			}
		}
		
	    /**
	     * Handles the download from the server. If the IFileHandle is not a TempFileHandle, the
	     * content has to be uploaded first.
	     *  
	     * @param pFileHandle the IFileHandle.
	     * @param  pBounds	  the bounds for the document, if supported from the implementation
	     * @param  pTarget	  a <code>String</code> indicating where to display the document.
	     * @throws Throwable if an Exception occurs.
	     */
	    private void handleDownload(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
	    {
    		RemoteFileHandle tempFile = null;

            HttpConnection con = getHttpConnection();
            
            if (con != null)
            {
                String sDownloadURL = con.getDownloadURL();
            
                if (sDownloadURL != null)
                {
            		if (pFileHandle instanceof RemoteFileHandle)
            		{
            			tempFile = (RemoteFileHandle)pFileHandle;
            		}
            		else
            		{
        		        tempFile = con.writeContent(pFileHandle);
            		}
        		
            		if (tempFile == null)
            		{
            		    throw new RuntimeException("Can't handle download without remote file handle!");
            		}

            		String sAppName = getApplicationName();
            		
            		StringBuilder sbUrl = new StringBuilder();
            		sbUrl.append(sDownloadURL);
            		
                    if (sDownloadURL.indexOf('?') > 0)
                    {
                        sbUrl.append("&");
                    }
                    else
                    {
                        sbUrl.append("?");
                    }
            		
            		sbUrl.append("ONLOAD=true");
            		
            		if (sAppName != null)
            		{
            			sbUrl.append("&APPLICATION=");
            			sbUrl.append(CodecUtil.encodeURLParameter(sAppName));
            		}
            		
            		sbUrl.append("&INFOTEXT=");
            		sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Please download your file")));
            		sbUrl.append("&DOWNLOADBUTTON=");
            		sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Download")));
            		sbUrl.append("&CANCELBUTTON=");
            		sbUrl.append(CodecUtil.encodeURLParameter(uilauncher.translate("Close")));
                    sbUrl.append("&KEY=");
                    sbUrl.append(tempFile.getObjectCacheKey());
            		
            		String sServerBase = getParameter(ILauncher.PARAM_SERVERBASE);
            		
            		if (sServerBase != null)
            		{
        	    		sbUrl.append("&CONURL=");
        	    		sbUrl.append(CodecUtil.encodeURLParameter(sServerBase));
            		}
            		
            		showDocument(sbUrl.toString(), pBounds, pTarget);
                }
            }
	    }
		
	    /**
	     * Gets the internal name of the application.
	     * 
	     * @return the application name
	     */
	    private String getApplicationName()
	    {
    		//try to detect the application name, if available
    		if (application instanceof IConnectable)
    		{
    			AbstractConnection con = ((IConnectable)application).getConnection(); 
    			
    			if (con != null)
    			{
    				return con.getApplicationName();
    			}
    		}

    		return getParameter(ILauncher.PARAM_APPLICATIONNAME); 
	    }
	    
	    /**
	     * Gets the internal {@link HttpConnection} of the application (if such a connection is used).
	     *  
	     * @return the connection or <code>null</code> if connection is not set or not a {@link HttpConnection}
	     */
	    private HttpConnection getHttpConnection()
	    {
            //try to detect the application name, if available
            if (application instanceof IConnectable)
            {
                AbstractConnection appcon = ((IConnectable)application).getConnection();

                if (appcon != null)
                {
                    IConnection con = appcon.getConnection();

                    if (con instanceof HttpConnection)
                    {
                        return (HttpConnection)con;
                    }
                }
            }
            
            return null;
	    }
	    
	    /**
	     * Gets the connection identifier of the application.
	     * 
	     * @return the connection identifier or <code>null</code> if application isn't an instance of {@link IConnectable}
	     */
	    private Object getConnectionId()
	    {
	        if (application instanceof IConnectable)
	        {
                AbstractConnection appcon = ((IConnectable)application).getConnection();

                if (appcon != null)
                {
                    return appcon.getConnectionId();
                }	            
	        }
	        
	        return null;
	    }
	    
	}   // SwingAppletLauncher
	
}	// SwingApplet
