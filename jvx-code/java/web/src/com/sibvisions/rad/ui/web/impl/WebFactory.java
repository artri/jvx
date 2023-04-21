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
 * 19.08.2009 - [JR] - creation
 * 02.11.2009 - [JR] - setLayout implemented
 * 10.04.2013 - [JR] - set/isAutomaticSizeCalculation implemented
 */
package com.sibvisions.rad.ui.web.impl;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;
import javax.rad.ui.IResource;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.celleditor.IDateCellEditor;
import javax.rad.ui.celleditor.IImageViewer;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.celleditor.INumberCellEditor;
import javax.rad.ui.celleditor.ITextCellEditor;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.component.IIcon;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.IMap;
import javax.rad.ui.component.IPasswordField;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.component.IRadioButton;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.component.ITextField;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IGroupPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.IChart;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.ui.control.ITable;
import javax.rad.ui.control.ITree;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFlowLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.ui.layout.IGridLayout;
import javax.rad.ui.menu.ICheckBoxMenuItem;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.ui.menu.ISeparator;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.RuntimeEventHandler;
import javax.rad.util.SilentAbortException;

import com.sibvisions.rad.ui.AbstractFactory;
import com.sibvisions.rad.ui.web.impl.celleditor.WebCheckBoxCellEditor;
import com.sibvisions.rad.ui.web.impl.celleditor.WebChoiceCellEditor;
import com.sibvisions.rad.ui.web.impl.celleditor.WebDateCellEditor;
import com.sibvisions.rad.ui.web.impl.celleditor.WebImageViewer;
import com.sibvisions.rad.ui.web.impl.celleditor.WebLinkedCellEditor;
import com.sibvisions.rad.ui.web.impl.celleditor.WebNumberCellEditor;
import com.sibvisions.rad.ui.web.impl.celleditor.WebTextCellEditor;
import com.sibvisions.rad.ui.web.impl.component.WebButton;
import com.sibvisions.rad.ui.web.impl.component.WebCheckBox;
import com.sibvisions.rad.ui.web.impl.component.WebCustomComponent;
import com.sibvisions.rad.ui.web.impl.component.WebIcon;
import com.sibvisions.rad.ui.web.impl.component.WebLabel;
import com.sibvisions.rad.ui.web.impl.component.WebMap;
import com.sibvisions.rad.ui.web.impl.component.WebPasswordField;
import com.sibvisions.rad.ui.web.impl.component.WebPopupMenuButton;
import com.sibvisions.rad.ui.web.impl.component.WebRadioButton;
import com.sibvisions.rad.ui.web.impl.component.WebTextArea;
import com.sibvisions.rad.ui.web.impl.component.WebTextField;
import com.sibvisions.rad.ui.web.impl.component.WebToggleButton;
import com.sibvisions.rad.ui.web.impl.container.WebCustomContainer;
import com.sibvisions.rad.ui.web.impl.container.WebDesktopPanel;
import com.sibvisions.rad.ui.web.impl.container.WebFrame;
import com.sibvisions.rad.ui.web.impl.container.WebGroupPanel;
import com.sibvisions.rad.ui.web.impl.container.WebInternalFrame;
import com.sibvisions.rad.ui.web.impl.container.WebPanel;
import com.sibvisions.rad.ui.web.impl.container.WebScrollPanel;
import com.sibvisions.rad.ui.web.impl.container.WebSplitPanel;
import com.sibvisions.rad.ui.web.impl.container.WebTabsetPanel;
import com.sibvisions.rad.ui.web.impl.container.WebToolBar;
import com.sibvisions.rad.ui.web.impl.container.WebToolBarPanel;
import com.sibvisions.rad.ui.web.impl.control.WebCellFormat;
import com.sibvisions.rad.ui.web.impl.control.WebChart;
import com.sibvisions.rad.ui.web.impl.control.WebEditor;
import com.sibvisions.rad.ui.web.impl.control.WebGauge;
import com.sibvisions.rad.ui.web.impl.control.WebTable;
import com.sibvisions.rad.ui.web.impl.control.WebTree;
import com.sibvisions.rad.ui.web.impl.layout.WebBorderLayout;
import com.sibvisions.rad.ui.web.impl.layout.WebFlowLayout;
import com.sibvisions.rad.ui.web.impl.layout.WebFormLayout;
import com.sibvisions.rad.ui.web.impl.layout.WebGridLayout;
import com.sibvisions.rad.ui.web.impl.menu.WebCheckBoxMenuItem;
import com.sibvisions.rad.ui.web.impl.menu.WebMenu;
import com.sibvisions.rad.ui.web.impl.menu.WebMenuBar;
import com.sibvisions.rad.ui.web.impl.menu.WebMenuItem;
import com.sibvisions.rad.ui.web.impl.menu.WebPopupMenu;
import com.sibvisions.rad.ui.web.impl.menu.WebSeparator;
import com.sibvisions.util.ThreadManager;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>WebFactory</code> class encapsulates methods to
 * create and access web components.
 * 
 * @author Martin Handsteiner
 */
public class WebFactory extends AbstractFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default number cell editor. */
	private static final ICellEditor NUMBER_CELL_EDITOR = new WebNumberCellEditor(); 
	
	/** the default number cell editor. */
	private static final ICellEditor DATE_CELL_EDITOR = new WebDateCellEditor(); 
	
	/** the default number cell editor. */
	private static final ICellEditor TEXT_CELL_EDITOR = new WebTextCellEditor(); 
	
	/**
	 * Defines that {@linkplain #createMap()} should create an {@linkplain IMap}
	 * containing a google map instead of a open street map. The value of the 
	 * property is expected to be either {@linkplain Boolean#FALSE} 
	 * or {@linkplain Boolean#TRUE}.
	 */
	public static final String PROPERTY_COMPONENT_MAP_GOOGLE = "web.component.map.google";
	
	/** The Key needed to access the Google API in order to have a functioning google map. */
	public static final String PROPERTY_COMPONENT_MAP_GOOGLE_KEY = "web.component.map.google.key";
	
	

	/** the default editors. */
	private static HashMap<Class<?>, ICellEditor> defaultCellEditors = new HashMap<Class<?>, ICellEditor>();
	
    /** the invoke later queue, per Thread. */
    private WeakHashMap<Object, ArrayList<Runnable>> whmpInvokeLaterQueue = new WeakHashMap<Object, ArrayList<Runnable>>();

    /** invokeLater thread. */
    private UIWorkerThread thInvokeLaterWorker = null;

    /** the thread manager. */
	private ThreadManager threadManager;
	
	/** the WebLauncher instance for this factory. */
	private WebLauncher launcher;
	
    /** special sync object for invokeLater. */
	private Object oSyncInvokeLater = new Object();
	
	/** the last focus component. */
	private WeakReference<WebComponent> wrefLastFocus = null;
	
	/** whether preferred size calculation should be done. */
	private static boolean bAutomaticSizeCalculation = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static 
	{
		defaultCellEditors.put(Number.class, NUMBER_CELL_EDITOR);
		defaultCellEditors.put(Date.class, DATE_CELL_EDITOR);
		
		WebColor.setSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND, new WebColor(255, 244, 210));
		WebColor.setSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND, new WebColor(239, 239, 239));
		WebColor.setSystemColor(IColor.INVALID_EDITOR_BACKGROUND,    new WebColor(209, 51, 51));
	}
	
	/**
	 * Constructs new WebLauncher.
	 * 
	 * @param pLauncher the launcher that created this factory.
	 */
	public WebFactory(WebLauncher pLauncher)
	{
		launcher = pLauncher;
	}
	
    /**
     * {@inheritDoc}
     */
    protected void initFactory()
    {
        // components
        registerComponent(ILabel.class,             WebLabel.class);
        registerComponent(ITextField.class,         WebTextField.class);
        registerComponent(IPasswordField.class,     WebPasswordField.class);
        registerComponent(ITextArea.class,          WebTextArea.class);
        registerComponent(IIcon.class,              WebIcon.class);
        registerComponent(IButton.class,            WebButton.class);
        registerComponent(IToggleButton.class,      WebToggleButton.class);
        registerComponent(IPopupMenuButton.class,   WebPopupMenuButton.class);
        registerComponent(ICheckBox.class,          WebCheckBox.class);
        registerComponent(IRadioButton.class,       WebRadioButton.class);
        // container
        registerComponent(IPanel.class,             WebPanel.class);
        registerComponent(IToolBarPanel.class,      WebToolBarPanel.class);
        registerComponent(IGroupPanel.class,        WebGroupPanel.class);
        registerComponent(IScrollPanel.class,       WebScrollPanel.class);
        registerComponent(ISplitPanel.class,        WebSplitPanel.class);
        registerComponent(ITabsetPanel.class,       WebTabsetPanel.class);
        registerComponent(IToolBar.class,           WebToolBar.class);
        registerComponent(IDesktopPanel.class,      WebDesktopPanel.class);
        // menu
        registerComponent(IMenuItem.class,          WebMenuItem.class);
        registerComponent(ICheckBoxMenuItem.class,  WebCheckBoxMenuItem.class);
        registerComponent(IMenu.class,              WebMenu.class);
        registerComponent(IMenuBar.class,           WebMenuBar.class);
        registerComponent(IMenu.class,              WebMenu.class);
        registerComponent(IPopupMenu.class,         WebPopupMenu.class);
        registerComponent(ISeparator.class,         WebSeparator.class);
        // controls
        registerComponent(IEditor.class,            WebEditor.class);
        registerComponent(ITable.class,             WebTable.class);
        registerComponent(ITree.class,              WebTree.class);
        registerComponent(IChart.class,             WebChart.class);
        registerComponent(IGauge.class,             WebGauge.class);
        registerComponent(IMap.class,               WebMap.class);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    public <C extends IComponent> Class<C> getComponentBaseClass()
    {
        return (Class<C>)WebComponent.class;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IBorderLayout createBorderLayout()
	{
		return new WebBorderLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IFlowLayout createFlowLayout()
	{
		return new WebFlowLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFormLayout createFormLayout()
	{
		return new WebFormLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridLayout createGridLayout(int columns, int rows) 
	{
		return new WebGridLayout(columns, rows);
	}
	
	/**
     * {@inheritDoc}
     */
    public IMap createMap() 
    {
        WebMap result = new WebMap();
        result.setFactory(this);
        
        if (isPropertyEnabled(PROPERTY_COMPONENT_MAP_GOOGLE))
        {
        	result.setProperty("tileProvider", "google");
        	
            //Factory prop
            String key = (String)getProperty(PROPERTY_COMPONENT_MAP_GOOGLE_KEY);
            
            //check properties file
            if (StringUtil.isEmpty(key)) 
            {
            	key = null;
            	
                InputStream fileStream = null;
                try 
                {
                    fileStream = ResourceUtil.getResourceAsStream("/googlemap.properties");
                    
                    if (fileStream != null) 
                    {
                		Properties prop = new Properties();
                		prop.load(fileStream);
                       
                		key = prop.getProperty("apikey");
                    }
                }
                catch (Exception e)
                { 
                    ExceptionHandler.show(e);
                } 
                finally 
                {
                    CommonUtil.close(fileStream);
                }
                
                if (key != null)
                {
	                setProperty(PROPERTY_COMPONENT_MAP_GOOGLE_KEY, key);
                }
            }    

            if (key != null)
            {
            	result.setProperty("apiKey", key);
            }
        }
        
        return result;
    }

	/**
	 * {@inheritDoc}
	 */
	public IWindow createWindow()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IFrame createFrame()
	{
		WebFrame result = new WebFrame();
		result.setFactory(this);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IInternalFrame createInternalFrame(IDesktopPanel pDesktop)
	{
		WebInternalFrame result = new WebInternalFrame(pDesktop);
		result.setFactory(this);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public ITextCellEditor createTextCellEditor()
	{
		return new WebTextCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public INumberCellEditor createNumberCellEditor()
	{
		return new WebNumberCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDateCellEditor createDateCellEditor()
	{
		return new WebDateCellEditor();
	}

    /**
	 * {@inheritDoc}
	 */
    public IImageViewer createImageViewer()
    {
    	return new WebImageViewer();
    }

	/**
	 * {@inheritDoc}
	 */
	public IChoiceCellEditor createChoiceCellEditor()
	{
		return new WebChoiceCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICheckBoxCellEditor createCheckBoxCellEditor()
	{
		return new WebCheckBoxCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ILinkedCellEditor createLinkedCellEditor()
	{
		return new WebLinkedCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellFormat createCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
	{
		return new WebCellFormat(pBackground, pForeground, pFont, pImage, pStyle, pLeftIndent);
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor createColor(int pRgb)
	{
		return new WebColor(pRgb);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension createDimension(int pWidth, int pHeight)
	{
		return new WebDimension(pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IInsets createInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		return new WebInsets(pTop, pLeft, pBottom, pRight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint createPoint(int pX, int pY)
	{
		return new WebPoint(pX, pY);
	}

	/**
	 * {@inheritDoc}
	 */
	public IRectangle createRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		return new WebRectangle(pX, pY, pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getAvailableFontFamilyNames()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getImage(String pImageName)
	{
		return WebImage.getImage(pImageName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getImage(String pImageName, byte[] pData)
	{
		return WebImage.getImage(pImageName, pData);
	}

	/**
	 * {@inheritDoc}
	 */
    public String getImageMapping(String pMappingName)
    {
    	return WebImage.getImageMapping(pMappingName);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setImageMapping(String pMappingName, String pImageName)
    {
    	WebImage.setImageMapping(pMappingName, pImageName);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public String[] getImageMappingNames()
    {
    	return WebImage.getImageMappingNames();
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IFont createFont(String pName, int pStyle, int pSize)
	{
		return new WebFont(pName, pStyle, pSize);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSystemColor(String pType, IColor pSystemColor)
	{
		WebColor.setSystemColor(pType, (WebColor)pSystemColor);
	}

	/**
	 * {@inheritDoc}
	 */
	public WebColor getSystemColor(String pType)
	{
		return WebColor.getSystemColor(pType);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getPredefinedCursor(int pType)
	{
		return WebCursor.getPredefinedCursor(pType);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getSystemCustomCursor(String pCursorName)
	{
		return WebCursor.getSystemCustomCursor(pCursorName);
	}

    /**
	 * {@inheritDoc}
	 */
	public ICellEditor getDefaultCellEditor(Class<?> pClass)
	{
		if (pClass == null)
		{
			return TEXT_CELL_EDITOR;
		}
		else
		{
            ICellEditor cellEditor = defaultCellEditors.get(pClass);
            
            if (cellEditor == null)
            {
                return getDefaultCellEditor(pClass.getSuperclass());
            }
            else
            {
                return cellEditor;
            }
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
	{
		if (pCellEditor == null)
		{
			defaultCellEditors.remove(pClass);
		}
		else
		{
			defaultCellEditors.put(pClass, pCellEditor);
		}
	}
	
    /**
	 * {@inheritDoc}
	 */
	public IComponent createCustomComponent(Object pCustomComponent) 
	{
		WebCustomComponent comp = new WebCustomComponent();
		comp.setFactory(this);
		
		if (pCustomComponent != null)
		{
			comp.setProperty("customResource", (WebComponent)pCustomComponent);
		}
		
		return comp;
	}

    /**
	 * {@inheritDoc}
	 */
	public IContainer createCustomContainer(Object pCustomContainer) 
	{
		WebCustomContainer cont = new WebCustomContainer();
		cont.setFactory(this);
		
		if (pCustomContainer != null)
		{
			cont.setProperty("customResource", (WebContainer)pCustomContainer);
		}
		
		return cont;
	}
	
    /**
	 * {@inheritDoc}
	 */
	public void invokeLater(Runnable pRunnable)
	{
	    Thread thread = Thread.currentThread();
	    
		if (thread instanceof PushThread)
		{
		    //e.g. factory.invokeInThread(...);
			((PushThread)thread).invokeLater(pRunnable);
		}
		else
		{
			WebContext ctxt = WebContext.getCurrentInstance();

			if (launcher != null 
				&& launcher.isPushEnabled()
				&& (ctxt == null 
				    || ctxt.getInitialThread() != thread
				    //e.g. push from UI1 to other UIs
				    || UIFactoryManager.getFactory() != this))
			{
			    //e.g. Thread t = new Thread(...);
			    //     t.start();
                PushThread.invokeLater(launcher, pRunnable);
			}
			else
			{
			    Object oQueueKey = thread;
			    
                //if push is disabled and a invokeLater was called from custom thread
                if (ctxt == null || ctxt.getInitialThread() != thread)
                {
                    //use current launcher as cache key because we don't have a better key
                    oQueueKey = launcher;
                }

                ArrayList<Runnable> liQueue;
                
                synchronized (whmpInvokeLaterQueue)
                {
                    liQueue = whmpInvokeLaterQueue.get(oQueueKey);
    			    
    			    if (liQueue == null)
    			    {
    			        liQueue = new ArrayList<Runnable>();
    			        whmpInvokeLaterQueue.put(oQueueKey, liQueue);
    			    }
                }
                
			    synchronized(liQueue)
			    {
    				liQueue.add(new UIRunnable(launcher, pRunnable, true));
			    }
			}
		}
	}

	/**
	 * Invokes the invoke later queue.
	 */
	public void performInvokeLater()
	{
	    Thread thread = Thread.currentThread();
	    
	    //Loop is important because it's possible that the execution creates invokeLater entries for 
	    //the threaded cache...
	    do
	    {
    	    performInvokeLater(thread);
	    
    	    //e.g. custom Threads and push is disabled
    	    performInvokeLater(launcher);
	    }
	    while (hasInvokeLaterEntries(thread) || hasInvokeLaterEntries(launcher));
	}
	
	/**
	 * Gets whether the invokelater queue contains entries.
	 * 
	 * @return <code>true</code> if invokelater entries are available
	 */
	public boolean hasInvokeLaterEntries()
	{
	    return hasInvokeLaterEntries(Thread.currentThread()) || hasInvokeLaterEntries(launcher);
	}
	
	/**
	 * Gets if the invokelater queue contains entries for the given key.
	 * 
	 * @param pQueueKey the cache key
	 * @return <code>true</code> if the queue contains entries for <code>pQueueKey</code>, <code>false</code> otherwise 
	 */
	private boolean hasInvokeLaterEntries(Object pQueueKey)
	{
	    ArrayList<Runnable> liQueue;
	    
        synchronized (whmpInvokeLaterQueue)
        {
            liQueue = whmpInvokeLaterQueue.get(pQueueKey);
        }
        
        if (liQueue == null)
        {
            return false;
        }
        
        synchronized(liQueue)
        {
            return !liQueue.isEmpty();
        }
	}
	
	/**
	 * Performs invoke later queue execution for a specific key.
	 * 
	 * @param pQueueKey the queue key to use
	 */
	private void performInvokeLater(Object pQueueKey)
	{
	    ArrayList<Runnable> liQueue;
	    
	    synchronized (whmpInvokeLaterQueue)
	    {
	        liQueue = whmpInvokeLaterQueue.get(pQueueKey);
	    }
	    
		if (liQueue == null)
	    {
	        return;
	    }
	    
	    int iSize;
	    
		synchronized(liQueue)
		{
		    iSize = liQueue.size();
		}

		while (iSize > 0)
		{
			try
			{
			    Runnable runnable;
			    
			    synchronized(liQueue)
			    {
			        runnable = liQueue.remove(0);
			        
			        iSize--;
			    }
			    
				runnable.run();
			}
			catch (Throwable th)
			{
				if (!(th instanceof SilentAbortException))
				{
					ExceptionHandler.show(th);
				}
			}
			
			if (iSize == 0)
			{
                synchronized(liQueue)
                {
                    iSize = liQueue.size();
                }
			}
		}
	}	
	
    /**
	 * {@inheritDoc}
	 */
	public void invokeAndWait(Runnable pRunnable)
	{
		UIRunnable runnable = new UIRunnable(launcher, pRunnable, true);
		
		runnable.run(); 
	}

    /**
	 * {@inheritDoc}
	 */
	public Thread invokeInThread(Runnable pRunnable)
	{
		Thread thread;
		
		if (launcher.isPushEnabled())
		{
			thread = new PushThread(new UIRunnable(launcher, pRunnable, false));
		}
		else
		{
			thread = new Thread(new UIRunnable(launcher, pRunnable, false));
		}
		
		addThread(thread);
		
		thread.start();
		
		return thread;
	}
	
	/**
	 * Dispatches an event to the given handler. The dispatching will be synchronized with this factory.
	 * 
	 * @param pHandler the event handler
	 * @param pParameter the event parameter
	 */
	public void synchronizedDispatchEvent(RuntimeEventHandler<?> pHandler, Object pParameter)
	{
		if (pHandler != null)
		{
			synchronized(this)
			{
				if (pParameter != null)
				{
					pHandler.dispatchEvent(pParameter);
				}
				else
				{
					pHandler.dispatchEvent();
				}
			}
		}
	}	
	
	/**
	 * Adds a thread to the internal thread manager.
	 * 
	 * @param pThread the thread to add
	 */
	private void addThread(Thread pThread)
	{
        if (threadManager == null)
        {
            threadManager = new ThreadManager();
        }
        
        threadManager.add(pThread);
	}
	
	/**
	 * Gets whether the given {@link Thread} is stopped.
	 * 
	 * @param pThread the thread
	 * @return <code>true</code> if stopped, <code>false</code> otherwise
	 */
	private boolean isThreadStopped(Thread pThread)
	{
	    return threadManager == null || threadManager.isStopped(pThread);
	}
	
	/**
	 * Destroys all threads created from this factory.
	 */
	public void destroyThreads()
	{
		if (threadManager != null)
		{
			threadManager.stop();
		}
		
		PushThread.cleanup(launcher);
		
		thInvokeLaterWorker = null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the launcher that created this factory.
	 * 
	 * @return the launcher 
	 */
	public WebLauncher getLauncher()
	{
		return launcher;
	}
	
	/**
	 * Gets the {@link WebResource} from an {@link IResource}.
	 * 
	 * @param <T> the resource class
	 * @param pWebResource an {@link IResource}
	 * @return the {@link WebResource} of <code>pWebResource</code>
	 */
	public static <T> T getWebResource(T pWebResource)
	{
		if (pWebResource instanceof IResource)
		{
			return (T)((IResource)pWebResource).getResource();
		}
		
		return pWebResource;
	}
	
	/**
	 * Gets the ICellEditor for the given class.
	 * 
	 * @param pClass the class
	 * @return the ICellEditor
	 */
	public static ICellEditor getCellEditor(Class<?> pClass)
	{
    	if (pClass == null)
    	{
    		return TEXT_CELL_EDITOR;
    	}
    	else
    	{
    		ICellEditor cellEditor = defaultCellEditors.get(pClass);
    		
    		if (cellEditor == null)
    		{
    			return getCellEditor(pClass.getSuperclass());
    		}
    		else
    		{
    			return cellEditor;
    		}
    	}
   	}
	
	/**
	 * Sets whether automatic preferred size calculation should be done.
	 * 
	 * @param pAutoSize <code>true</code> to calculate preferred size(s), <code>false</code> to do nothing
	 */
	public static void setAutomaticSizeCalculation(boolean pAutoSize)
	{
		bAutomaticSizeCalculation = pAutoSize;
	}
	
	/**
	 * Gets whether automatic preferred size calculation should be done.
	 * 
	 * @return <code>true</code> if preferred size calculation is enabled, <code>false</code> otherwise
	 */
	public static boolean isAutomaticSizeCalculation()
	{
		return bAutomaticSizeCalculation;
	}
	
	/**
	 * Gets the class name for the given object.
	 * 
	 * @param pInstance the object
	 * @return the class name
	 */
	public static String getClassName(Object pInstance)
	{
		String sName = pInstance.getClass().getSimpleName();
		
		if (sName.startsWith("Web"))
		{
			return sName.substring(3);
		}
		
		return sName;
	}
	
	/**
	 * Marks the given component as focus receiver. If a mark for another component is available,
	 * it will be removed.
	 * 
	 * @param pComponent the component which should receive the focus
	 */
	public void requestFocus(WebComponent pComponent)
	{
		//clear focus receiver
		if (wrefLastFocus != null)
		{
			WebComponent comp = wrefLastFocus.get();
			
			if (comp != null)
			{
				comp.removeCommandProperty("requestFocus");
			}
		}

		//remember last request-focus component
		wrefLastFocus = new WeakReference<WebComponent>(pComponent);
		
		//mark focus receiver
		pComponent.setCommandProperty("requestFocus", Boolean.TRUE);
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * The <code>PushThread</code> is a special thread, used for push operations. It's a marker
	 * thread that will be used to differentiate client requests and push requests started
	 * from the developer.
	 * 
	 * @author René Jahn
	 */
	private static final class PushThread extends Thread
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the runnable. */
		private UIRunnable runnable;

		/** invokeLater queue per launcher. */
        private static WeakHashMap<WebLauncher, ArrayList<Runnable>> whmpLauncherQueue = new WeakHashMap<WebLauncher, ArrayList<Runnable>>();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new thread with the given runnable.
		 * 
		 * @param pRunnable the runnable
		 */
		private PushThread(UIRunnable pRunnable)
		{
			super();
			
			runnable = pRunnable;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			if (runnable != null)
			{
	            WebContext ctxt = new WebContext();

                try
	            {
	                runnable.run();
	            }
	            finally
	            {
	                ctxt.release();
	            }
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		  
		/**
		 * Adds the given runnable to the invoke later queue for push notification.
		 * 
		 * @param pRunnable the Runnable to be executed after the Thread is finished.
		 */
		public void invokeLater(Runnable pRunnable)
		{
		    invokeLater(runnable.launcher, pRunnable);
		}
		
		/**
		 * Adds the given runnable to the invoke later queue for push notification.
		 * 
		 * @param pLauncher the {@link WebLauncher}
		 * @param pRunnable the Runnable to be executed after the Thread is finished.
		 */
		public static void invokeLater(WebLauncher pLauncher, Runnable pRunnable)
		{
		    WebFactory factory = pLauncher.getFactory();
		    
		    if (factory != null)
		    {
		        ArrayList<Runnable> liQueue;
		        
		        synchronized(factory.oSyncInvokeLater)
		        {
		            liQueue = whmpLauncherQueue.get(pLauncher);
		            
		            if (liQueue == null)
		            {
		                liQueue = new ArrayList<Runnable>();
		                whmpLauncherQueue.put(pLauncher, liQueue);
		            }
		            
	                liQueue.add(pRunnable);
		            
	                UIWorkerThread thCurrentInvokeLaterWorker = factory.thInvokeLaterWorker;
	                
                    if (factory.isThreadStopped(thCurrentInvokeLaterWorker))
    	            {
                    	thCurrentInvokeLaterWorker = new UIWorkerThread(pLauncher, liQueue);
    	                    
                        factory.addThread(thCurrentInvokeLaterWorker);
	                    
                        factory.thInvokeLaterWorker = thCurrentInvokeLaterWorker;
                        
                        thCurrentInvokeLaterWorker.start();
    		        }
                    else
                    {
                        synchronized(thCurrentInvokeLaterWorker)
                        {
                        	thCurrentInvokeLaterWorker.notify();
                        }
                    }
		        }
		    }
		}

		/**
		 * Clears all launcher related caches.
		 * 
		 * @param pLauncher the launcher
		 */
		public static void cleanup(WebLauncher pLauncher)
		{
			if (pLauncher != null)
			{
			    WebFactory factory = pLauncher.getFactory();
			    
			    if (factory != null)
			    {
			        synchronized(factory.oSyncInvokeLater)
			        {
						whmpLauncherQueue.remove(pLauncher);
			        }
			    }
			}
		}

	}	// PushThread
	
	/**
	 * The <code>UIWorkerThread</code> is responsible for executing invokeLater queue
	 * and pushing changes.
	 * 
	 * @author René Jahn
	 */
	private static final class UIWorkerThread extends Thread 
	                                          implements Runnable
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /** the launcher. */
	    private WebLauncher launcher;

	    /** the factory. */
	    private WebFactory factory;
	    
	    /** the queue. */
	    private ArrayList<Runnable> liQueue;

	    /** the current count of done (queued) calls. */
	    private int iDoneCount;
	    
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Creates a new instance of <code>UIWorkerThread</code>.
	     * 
	     * @param pLauncher the UI
	     * @param pQueue the queue
	     */
	    private UIWorkerThread(WebLauncher pLauncher, ArrayList<Runnable> pQueue)
	    { 
	        launcher = pLauncher;
	        liQueue = pQueue;

	        //the factory is != null here
            factory = pLauncher.getFactory();
            
            setName(getName() + ", Launcher = " + System.identityHashCode(pLauncher));
	    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * {@inheritDoc}
	     */
        public void run()
        {
            try
            {
                performInvokeLater();
            }
            finally
            {
                synchronized(factory.oSyncInvokeLater)
                {
                	factory.thInvokeLaterWorker = null;
                }
            }
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Executes all invokeLater runnables.
         */
        private void performInvokeLater()
        {
            int iRunnableCount = 0;

            synchronized(factory.oSyncInvokeLater)
            {
                iRunnableCount = liQueue.size();
                
                if (iRunnableCount == 0)
                {
                	factory.thInvokeLaterWorker = null;
                }
            }
            
            boolean bNotifyBeforeUI = false;

            iDoneCount = 0;
            
            try
            {
                Runnable runner;
               
                //launcher already destroyed -> nothing to do
                while (launcher != null && iRunnableCount > 0)
                {
                    iDoneCount++;
                 
                    synchronized(factory.oSyncInvokeLater)
                    {
                        runner = liQueue.remove(0);
                        
                        iRunnableCount--;
                    }
                    
                    if (!bNotifyBeforeUI)
                    {
                        launcher.notifyBeforeUI();
                        
                        bNotifyBeforeUI = true;
                    }

                    synchronized (UIFactoryManager.getFactory())
                    {
                    	runner.run();
                    }
                    
                    //push every 100 runs, to handle endless invokeLater calls
                    if (iDoneCount == 100)
                    {
                        pushUpdate();
                    }
                    
                    if (iRunnableCount == 0)
                    {
                        boolean bLauncherDestroyed = false;
                        
                        do
                        {
                            Thread.sleep(100);
                            
                            synchronized(factory.oSyncInvokeLater)
                            {
                                iRunnableCount = liQueue.size();
                            }
                            
                            if (iRunnableCount == 0)
                            {
                                //no problem -> done count will be checked to avoid multiple push calls
                                if (!pushUpdate())
                                {
                                    bLauncherDestroyed = true; 
                                }
                            }
                            
                            //The UI locks per Thread and this is the reason why we have to wait until all runnables
                            //were executed. Otherwise it could happen that a previous push is still running while a
                            //new push will be sent (from another Thread) -> throws an Exception
                        }
                        while (iRunnableCount == 0 && !bLauncherDestroyed);

                        //all queued runnables were executed and no more runnables are in the queue 
                        if (!bLauncherDestroyed && iRunnableCount == 0)
                        {
                            if (pushUpdate())
                            {
                                //wait some seconds because we should re-use the same thread as often as possible.
                                //It's not ideal to throw away the Thread after 1 call
                                synchronized (this)
                                {
                                    wait(5000);
                                }
                                
                                //final check
                                synchronized(factory.oSyncInvokeLater)
                                {
                                    iRunnableCount = liQueue.size();
                                
                                    if (iRunnableCount == 0)
                                    {
                                    	factory.thInvokeLaterWorker = null;
                                    }
                                }
                            }
                            else
                            {
                                bLauncherDestroyed = true;
                            }
                        }
                    }
                }

                //doesn't push again if already pushed
                pushUpdate();
            }
            catch (InterruptedException ie)
            {
                //nothing to be done
            }
            finally
            {
                WebLauncher.notifyAfterUI();                                    
            }
        }           
	    
        /**
         * Pushes update, if necessary.
         * 
         * @return <code>true</code> if push was done, <code>false</code> otherwise
         */
        private boolean pushUpdate()
        {
        	IPushHandler[] handlers = launcher.getPushHandlers();

        	if (!launcher.isDisposed()
        		&& launcher.getFactory() != null)
        	{
                if (iDoneCount > 0)
                {
                    iDoneCount = 0;
        		
		        	for (IPushHandler handler : handlers)
		        	{
		        		try
		        		{
		        			handler.pushUpdate();
		        		}
		        		catch (IOException ioe)
		        		{
		        			factory.thInvokeLaterWorker = null;
		        			
		        			return false;
		        		}
		            }
		        	
		        	return true;
        		}
        	}
            else
            {
            	factory.thInvokeLaterWorker = null;
            }
        	
        	return false;
        }
	    
	}  // UIWorkerThread
	
	/**
	 * The <code>UIRunnable</code> class is a wrapper class for another {@link Runnable}. It handles
	 * factory initialization before the run method of the wrapped runnable will be called.
	 * 
	 * @author René Jahn
	 */
	private static class UIRunnable implements Runnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** the original runnable. */
		private Runnable runnable;
		
		/** the calling launcher. */
		private WebLauncher launcher;
		
		/** the initialization stack. */
		private Exception exInit;
		
        /** whether the execution should be synchronized. */
        private boolean bSync;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>UIRunnable</code>.
         * 
         * @param pLauncher the launcher
         * @param pRunnable the runnable
         * @param pSync <code>true</code> to execute synchronized
         */
        protected UIRunnable(WebLauncher pLauncher, Runnable pRunnable, boolean pSync)
        {
            launcher = pLauncher;
            runnable = pRunnable;
            bSync = pSync;
            
            
            exInit = new Exception("invokeLater Init");
        }
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
		    try
		    {
    			if (runnable != null)
    			{
    				if (!(runnable instanceof UIRunnable) 
    					&& launcher != null)
    				{
    				    if (launcher.isDisposed())
    				    {
    				        return;
    				    }
    				    
						boolean bInitFactory = UIFactoryManager.getFactory() == null;
						
						if (bInitFactory)
						{
							launcher.notifyBeforeUI();
						}

						try
						{
							if (bSync)
							{
								synchronized(UIFactoryManager.getFactory())
								{
									runnable.run();
								}
							}
							else
							{
								runnable.run();
							}
						}
						finally
						{
							if (bInitFactory)
							{
								WebLauncher.notifyAfterUI();
							}
						}
    				}
    				else
    				{
    					runnable.run();
    				}
    			}
		    }
		    catch (RuntimeException re)
		    {
		    	LoggerFactory.getInstance(WebFactory.class).error(exInit);
		    	
		    	throw re;
		    }
		}
	
	}	// UIRunnable
	
}	// WebFactory
