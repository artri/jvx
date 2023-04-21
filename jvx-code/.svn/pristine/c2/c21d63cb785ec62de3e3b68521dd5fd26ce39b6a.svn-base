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
 * 24.09.2013 - [JR] - support images with byte[]
 * 27.11.2013 - [JR] - support push for standard Threads
 * 08.03.2014 - [LT] - included method to create a gridlayout
 * 18.03.2014 - [JR] - #980: ensure that push/access have an UI factory
 * 03.12.2014 - [JR] - PushThread, UIRunnable: check launcher because of automated GUI test
 * 03.01.2015 - [JR] - changed push thread UI handling because the UI wasn't the right one
 * 15.07.2015 - [JR] - #1437: changed invokeLater handling and push notification
 * 10.12.2015 - [JR] - #1542: cache invokeLater queue for current UI if push mode is disabled
 * 31.05.2016 - [JR] - style property support
 * 24.06.2016 - [JR] - performInvokeLater now checks both possible cache keys (in a loop)
 * 21.03.2018 - [JR] - IPopupMenuButton support
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Map;
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
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinCheckBoxCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinChoiceCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinDateCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinImageViewer;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinLinkedCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinNumberCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinTextCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinButton;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinCheckBox;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinCustomComponent;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinIcon;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinLabel;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinPasswordField;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinPopupMenuButton;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinRadioButton;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinTextArea;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinTextField;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinToggleButton;
import com.sibvisions.rad.ui.vaadin.impl.component.chart.VaadinGaugeJs;
import com.sibvisions.rad.ui.vaadin.impl.component.chart.v3.VaadinChartJs;
import com.sibvisions.rad.ui.vaadin.impl.component.map.google.VaadinGoogleMap;
import com.sibvisions.rad.ui.vaadin.impl.component.map.openstreet.VaadinOpenStreetMap;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinCustomContainer;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinCustomSingleContainer;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinDesktopPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinFrame;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinGroupPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinInternalFrame;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinScrollPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinSplitPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinTabsetPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinToolBar;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinToolBarPanel;
import com.sibvisions.rad.ui.vaadin.impl.container.VaadinWindow;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinCellFormat;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinEditor;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinGrid;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTree;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTreeGrid;
import com.sibvisions.rad.ui.vaadin.impl.feature.IAutoCompleteFeature;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinBorderLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientBorderLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientFlowLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientFormLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinClientGridLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinFlowLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinFormLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinGridLayout;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinCheckBoxMenuItem;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinMenu;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinMenuBar;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinMenuItem;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinPopupMenu;
import com.sibvisions.rad.ui.vaadin.impl.menu.VaadinSeparator;
import com.sibvisions.rad.ui.vaadin.server.VaadinContext;
import com.sibvisions.util.ThreadManager;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ExceptionUtil;
import com.vaadin.server.ErrorHandlingRunnable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.PushConfiguration;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.util.CurrentInstance;

/**
 * The <code>VaadinFactory</code> class encapsulates methods to create and
 * access vaadin components.
 * 
 * @author Benedikt Cermak
 * @author Stefan Wurm
 */
public class VaadinFactory extends AbstractFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Defines that {@link #createTable()} should create an {@link ITable}
	 * containing the old Vaadin Table instead of the new Grid. The value of
	 * the property is expected to be either {@link Boolean#FALSE} or
	 * {@link Boolean#TRUE}.
	 */
	public static final String PROPERTY_COMPONENT_LEGACY_TABLE = "vaadin.component.legacy_table";
	
	/**
	 * Defines that {@link #createTree()} should create an {@link ITree}
	 * containing the old Vaadin Tree instead of the new TreeGrid. The value of
	 * the property is expected to be either {@link Boolean#FALSE} or
	 * {@link Boolean#TRUE}.
	 */
	public static final String PROPERTY_COMPONENT_LEGACY_TREE = "vaadin.component.legacy_tree";
	
	/**
     * Defines that {@link #createChart()} should create an {@link IChart}
     * using chart.js implementation instead of the commercial Vaadin Charts. The value of
     * the property is expected to be either {@link Boolean#FALSE} or
     * {@link Boolean#TRUE}.
     */
    public static final String PROPERTY_COMPONENT_CHARTJS = "vaadin.component.chartjs";
	
	/**
	 * Defines that the client-side layouts should be used, that means that
	 * {@link #createBorderLayout()}, {@link #createFlowLayout()},
	 * {@link #createGridLayout(int, int)} and {@link #createFormLayout()} are returning
	 * client-side layouts. The value of the property is expected to be either
	 * {@link Boolean#FALSE} or {@link Boolean#TRUE}.
	 */
	public static final String PROPERTY_COMPONENT_CLIENT_LAYOUTS = "vaadin.component.client_layouts";
	
	/**
	 * Defines whether the autocomplete attribute for input elements should be set.
	 */
	public static final String PROPERTY_BROWSER_AUTOCOMPLETE = "vaadin.browser.autocomplete";
	
	/**
	 * Defines that {@linkplain #createMap()} should create an {@linkplain IMap}
	 * containing a google map instead of a open street map. The value of the 
	 * property is expected to be either {@linkplain Boolean#FALSE} 
	 * or {@linkplain Boolean#TRUE}.
	 */
	public static final String PROPERTY_COMPONENT_MAP_GOOGLE = "vaadin.component.map.google";
	
	/** The Key needed to access the Google API in order to have a functioning google map. */
	public static final String PROPERTY_COMPONENT_MAP_GOOGLE_KEY = "vaadin.component.map.google.key";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the invoke later queue, per Thread. */
    private WeakHashMap<Object, ArrayList<Runnable>> whmpInvokeLaterQueue = new WeakHashMap<Object, ArrayList<Runnable>>();

    /** invokeLater thread. */
    private UIWorkerThread thInvokeLaterWorker = null;

    /** the thread manager. */
	private ThreadManager threadManager;
	
	/** the reference to the "UI". */
	private VaadinUI vaadinUI;

    /** special sync object for invokeLater. */
	private Object oSyncInvokeLater = new Object();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static 
	{
		VaadinColor.setSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND, new VaadinColor(255, 244, 210));
		VaadinColor.setSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND, new VaadinColor(239, 239, 239));
		VaadinColor.setSystemColor(IColor.INVALID_EDITOR_BACKGROUND,    new VaadinColor(209, 51, 51));
	}	
	
	/**
	 * Creates a new instance of <code>VaadinFactory</code>.
	 */
	public VaadinFactory()
	{
		super();
		
		setProperty(PROPERTY_COMPONENT_LEGACY_TABLE, Boolean.TRUE);
		setProperty(PROPERTY_COMPONENT_LEGACY_TREE, Boolean.TRUE);
		setProperty(PROPERTY_COMPONENT_CLIENT_LAYOUTS, Boolean.FALSE);
		setProperty(PROPERTY_COMPONENT_MAP_GOOGLE, Boolean.FALSE);
	}

    /**
     * {@inheritDoc}
     */
    protected void initFactory()
    {
        // components
        registerComponent(ILabel.class,             VaadinLabel.class);
        registerComponent(ITextField.class,         VaadinTextField.class);
        registerComponent(IPasswordField.class,     VaadinPasswordField.class);
        registerComponent(ITextArea.class,          VaadinTextArea.class);
        registerComponent(IIcon.class,              VaadinIcon.class);
        registerComponent(IButton.class,            VaadinButton.class);
        registerComponent(IToggleButton.class,      VaadinToggleButton.class);
        registerComponent(IPopupMenuButton.class,   VaadinPopupMenuButton.class);
        registerComponent(ICheckBox.class,          VaadinCheckBox.class);
        registerComponent(IRadioButton.class,       VaadinRadioButton.class);
        // container
        registerComponent(IPanel.class,             VaadinPanel.class);
        registerComponent(IToolBarPanel.class,      VaadinToolBarPanel.class);
        registerComponent(IGroupPanel.class,        VaadinGroupPanel.class);
        registerComponent(IScrollPanel.class,       VaadinScrollPanel.class);
        registerComponent(ISplitPanel.class,        VaadinSplitPanel.class);
        registerComponent(ITabsetPanel.class,       VaadinTabsetPanel.class);
        registerComponent(IToolBar.class,           VaadinToolBar.class);
        registerComponent(IDesktopPanel.class,      VaadinDesktopPanel.class);
        // menu
        registerComponent(IMenuItem.class,          VaadinMenuItem.class);
        registerComponent(ICheckBoxMenuItem.class,  VaadinCheckBoxMenuItem.class);
        registerComponent(IMenu.class,              VaadinMenu.class);
        registerComponent(IMenuBar.class,           VaadinMenuBar.class);
        registerComponent(IMenu.class,              VaadinMenu.class);
        registerComponent(IPopupMenu.class,         VaadinPopupMenu.class);
        registerComponent(ISeparator.class,         VaadinSeparator.class);
        // controls
        registerComponent(IEditor.class,            VaadinEditor.class);
        registerComponent(ITable.class,             VaadinTable.class);
        registerComponent(ITree.class,              VaadinTree.class);
        registerComponent(IChart.class,             VaadinChartJs.class);
        registerComponent(IGauge.class,             VaadinGaugeJs.class);
        registerComponent(IMap.class,               VaadinOpenStreetMap.class);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public <C extends IComponent> Class<C> getComponentBaseClass()
    {
        return (Class<C>)VaadinComponentBase.class;
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
	public IFont createFont(String pName, int pStyle, int pSize)
	{
		return new VaadinFont(pName, pStyle, pSize);
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor createColor(int pRGB)
	{
		return new VaadinColor(pRGB);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getPredefinedCursor(int pType)
	{
		return VaadinCursor.getPredefinedCursor(pType);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getSystemCustomCursor(String pCursorName)
	{
		return VaadinCursor.getSystemCustomCursor(pCursorName);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint createPoint(int pX, int pY)
	{
		return new VaadinPoint(pX, pY);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension createDimension(int pWidth, int pHeight)
	{
		return new VaadinDimension(pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IRectangle createRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		return new VaadinRectangle(pX, pY, pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IInsets createInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		return new VaadinInsets(pTop, pLeft, pBottom, pRight);

	}

	/**
	 * {@inheritDoc}
	 */
	public IComponent createCustomComponent(Object pCustomComponent)
	{
		VaadinCustomComponent<AbstractComponent> result = new VaadinCustomComponent<AbstractComponent>((AbstractComponent)pCustomComponent);
		result.setFactory(this);

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IContainer createCustomContainer(Object pCustomContainer)
	{
		if (pCustomContainer instanceof SingleComponentContainer)
		{
			VaadinCustomSingleContainer result = new VaadinCustomSingleContainer((SingleComponentContainer)pCustomContainer);
			result.setFactory(this);
			
			return result;
		}
		else
		{
			VaadinCustomContainer result = new VaadinCustomContainer((ComponentContainer)pCustomContainer);
			result.setFactory(this);
			
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getSystemColor(String pType)
	{
		return VaadinColor.getSystemColor(pType);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSystemColor(String pType, IColor pSystemColor)
	{
		VaadinColor.setSystemColor(pType, (VaadinColor)pSystemColor);
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getImage(String pImageName)
	{
		return VaadinImage.getImage(pImageName);
	}

	/**
	 * {@inheritDoc}
	 */
	public IImage getImage(String pImageName, byte[] pData)
	{
		return VaadinImage.getImage(pImageName, pData); 
	}

	/**
	 * {@inheritDoc}
	 */
	public String getImageMapping(String pMappingName)
	{
		return VaadinImage.getImageMapping(pMappingName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setImageMapping(String pMappingName, String pImageName)
	{
		VaadinImage.setImageMapping(pMappingName, pImageName);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getImageMappingNames()
	{
		return VaadinImage.getImageMappingNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public ITable createTable()
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_LEGACY_TABLE))
		{
			VaadinTable result = new VaadinTable();
			result.setFactory(this);
			return result;
		}
		else
		{
			VaadinGrid grid = new VaadinGrid();
			grid.setFactory(this);
			return grid;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ITree createTree()
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_LEGACY_TREE))
		{
			VaadinTree result = new VaadinTree();
			result.setFactory(this);
			return result;
		}
		else
		{
			VaadinTreeGrid result = new VaadinTreeGrid();
			result.setFactory(this);
			return result;
		}
	}

    /**
     * {@inheritDoc}
     */
    public IMap createMap() 
    {
        if (isPropertyEnabled(PROPERTY_COMPONENT_MAP_GOOGLE)) 
        {
            VaadinGoogleMap result = new VaadinGoogleMap();
            result.setFactory(this);
            return result;   
        }
        else 
        {
            VaadinOpenStreetMap result = new VaadinOpenStreetMap();
            result.setFactory(this);
            return result;
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public ICellFormat createCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
	{
		return new VaadinCellFormat(pBackground, pForeground, pFont, pImage, pStyle, pLeftIndent);
	}

	/**
	 * {@inheritDoc}
	 */
	public IImageViewer createImageViewer()
	{
		return new VaadinImageViewer();
	}

	/**
	 * {@inheritDoc}
	 */
	public IChoiceCellEditor createChoiceCellEditor()
	{
		return new VaadinChoiceCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICheckBoxCellEditor createCheckBoxCellEditor()
	{
		return new VaadinCheckBoxCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDateCellEditor createDateCellEditor()
	{
		return new VaadinDateCellEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ILinkedCellEditor createLinkedCellEditor()
	{
		VaadinLinkedCellEditor result = new VaadinLinkedCellEditor();
		
		initAutoComplete(result);
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public INumberCellEditor createNumberCellEditor()
	{
		VaadinNumberCellEditor result = new VaadinNumberCellEditor();
		
		initAutoComplete(result);
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public ITextCellEditor createTextCellEditor()
	{
		VaadinTextCellEditor result = new VaadinTextCellEditor();
		
		initAutoComplete(result);
		
		return result;
	}

    /**
     * {@inheritDoc}
     */
    public IInternalFrame createInternalFrame(IDesktopPanel pDesktop)
    {
        VaadinInternalFrame frame = new VaadinInternalFrame(pDesktop);
        frame.setFactory(this);
        
        return frame;
    }

	/**
	 * {@inheritDoc}
	 */
	public IWindow createWindow()
	{
		VaadinWindow result = new VaadinWindow();
		result.setFactory(this);
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IFrame createFrame()
	{
		VaadinFrame result = new VaadinFrame();
		result.setFactory(this);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IBorderLayout createBorderLayout()
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_CLIENT_LAYOUTS))
		{
			return new VaadinClientBorderLayout();
		}
		else
		{
			return new VaadinBorderLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IFlowLayout createFlowLayout()
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_CLIENT_LAYOUTS))
		{
			return new VaadinClientFlowLayout();
		}
		else
		{
			return new VaadinFlowLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IFormLayout createFormLayout()
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_CLIENT_LAYOUTS))
		{
			return new VaadinClientFormLayout();
		}
		else
		{
			return new VaadinFormLayout();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridLayout createGridLayout(int columns, int rows) 
	{
		if (isPropertyEnabled(PROPERTY_COMPONENT_CLIENT_LAYOUTS))
		{
			return new VaadinClientGridLayout(columns, rows);
		}
		else
		{
			return new VaadinGridLayout(columns, rows);
		}
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
		    VaadinContext ctxt = VaadinContext.getCurrentInstance();

			if (vaadinUI != null 
				&& vaadinUI.getPushConfiguration().getPushMode().isEnabled()
				&& (ctxt == null 
				    || ctxt.getInitialThread() != thread
				    //e.g. push from UI1 to other UIs
				    || UIFactoryManager.getFactory() != this))
			{
			    //e.g. Thread t = new Thread(...);
			    //     t.start();
                PushThread.invokeLater(vaadinUI, pRunnable);
			}
			else
			{
			    Object oQueueKey = thread;
			    
                //if push is disabled and a invokeLater was called from custom thread
                if (ctxt == null || ctxt.getInitialThread() != thread)
                {
                    //use current UI as cache key because we don't have a better key
                    oQueueKey = vaadinUI;
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
    				liQueue.add(new UIRunnable(vaadinUI, pRunnable, true));
			    }
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void invokeAndWait(Runnable pRunnable) throws Exception
	{
		UIRunnable runnable = new UIRunnable(vaadinUI, pRunnable, true);
		
		runnable.run(); 
	}

	/**
	 * {@inheritDoc}
	 */
	public Thread invokeInThread(Runnable pRunnable)
	{	    
		Thread thread;
		
		if (vaadinUI != null && vaadinUI.getPushConfiguration().getPushMode().isEnabled())
		{
			thread = new PushThread(new UIRunnable(vaadinUI, pRunnable, false));
		}
		else
		{
			thread = new Thread(new UIRunnable(vaadinUI, pRunnable, false));
		}
		
		addThread(thread);
		
		thread.start();
		
		return thread;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getDefaultCellEditor(Class<?> pClass)
	{
		return VaadinUtil.getDefaultCellEditor(pClass);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
	{
		VaadinUtil.setDefaultCellEditor(pClass, pCellEditor);
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invokes the invoke later queue.
	 */
	public void performInvokeLater()
	{
	    Thread thread = Thread.currentThread();
	    
	    //Loop is important because it's possible that the execution with UI creates invokeLater entries for 
	    //the threaded cache...
	    do
	    {
    	    performInvokeLater(thread);
	    
    	    //e.g. custom Threads and push is disabled
    	    performInvokeLater(vaadinUI);
	    }
	    while (hasInvokeLaterEntries(thread) || hasInvokeLaterEntries(vaadinUI));
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
	 * Shows an error window.
	 * 
	 * @param pUI the UI to use or <code>null</code> if current UI should be detected
	 * @param pThrowable the Throwable error or exception.
	 */
	static void showError(UI pUI, Throwable pThrowable)
	{
		pThrowable.printStackTrace();

		UI ui = pUI;
		
		if (ui == null)
		{
			ui = UI.getCurrent();
		}
		
		if (ui != null)
		{
			Window window = new Window("InstantiationError");
	
			VerticalLayout vlayout = new VerticalLayout();
			vlayout.setMargin(true);
			
			String sMessage = ExceptionUtil.dump(pThrowable, true).replace("\n", "<br/>").replace("\t", "<span style='padding-left: 15px'/>");
			
			if (pThrowable != null)
			{
				sMessage = sMessage.replace(": " + pThrowable.getMessage(), ": <b>" + pThrowable.getMessage() + "</b>");
			}

			Label lbl = new Label(sMessage);
			lbl.setContentMode(ContentMode.HTML);
			
			vlayout.addComponent(lbl);

            window.setWidth(700, Unit.PIXELS);
            window.setHeight(420, Unit.PIXELS);
			window.setContent(vlayout);
			window.setModal(true);
			
			if (ui.getContent() == null)
			{
			    window.setClosable(false);
			}
			
			window.center();
		
			//otherwise, we won't see the window!
			if (!ui.isVisible())
			{
				ui.setVisible(true);
			}
			
			ui.addWindow(window);
		}
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
	 * Sets the UI.
	 * 
	 * @param pUI the UI
	 */
	protected synchronized void setUI(VaadinUI pUI)
	{
		vaadinUI = pUI;
	}

	/**
	 * Gets the UI.
	 * 
	 * @return the UI
	 */
	public synchronized VaadinUI getUI()
	{
		return vaadinUI;
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
		
		PushThread.cleanup(vaadinUI);
		
		thInvokeLaterWorker = null;
	}
	
	/**
	 * Initializes the autocomplete feature of components. The autocomplete feature will
	 * 
	 * @param pComponent the component
	 */
	private void initAutoComplete(IAutoCompleteFeature pComponent)
	{
		//only set autocomplete, if factory property is disabled because default it's enabled
		if (getProperty(PROPERTY_BROWSER_AUTOCOMPLETE) == Boolean.FALSE)
		{
			pComponent.setAutoComplete(false);
		}
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

		/** invokeLater queue per UI. */
        private static WeakHashMap<VaadinUI, ArrayList<Runnable>> whmpUIQueue = new WeakHashMap<VaadinUI, ArrayList<Runnable>>();

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
	            VaadinContext ctxt = new VaadinContext();

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
		    invokeLater(runnable.ui, pRunnable);
		}
		
		/**
		 * Adds the given runnable to the invoke later queue for push notification.
		 * 
		 * @param pUI the {@link VaadinUI}
		 * @param pRunnable the Runnable to be executed after the Thread is finished.
		 */
		public static void invokeLater(VaadinUI pUI, Runnable pRunnable)
		{
		    VaadinFactory factory = (VaadinFactory)pUI.getFactory();
		    
		    if (factory != null)
		    {
		        ArrayList<Runnable> liQueue;
		        
		        synchronized(factory.oSyncInvokeLater)
		        {
		            liQueue = whmpUIQueue.get(pUI);
		            
		            if (liQueue == null)
		            {
		                liQueue = new ArrayList<Runnable>();
		                whmpUIQueue.put(pUI, liQueue);
		            }
		            
	                liQueue.add(pRunnable);
		            
                    UIWorkerThread thWorker = factory.thInvokeLaterWorker;

                    if (factory.isThreadStopped(thWorker))
    	            {
                        thWorker = new UIWorkerThread(pUI, liQueue);
    	                    
                        factory.addThread(thWorker);
                        
                        factory.thInvokeLaterWorker = thWorker;
	                    
    	                thWorker.start();
    		        }
                    else
                    {
                        synchronized(thWorker)
                        {
                            thWorker.notify();
                        }
                    }
		        }
		    }
		}

		/**
		 * Clears all UI related caches.
		 * 
		 * @param pUI the UI
		 */
		public static void cleanup(VaadinUI pUI)
		{
			if (pUI != null)
			{
			    VaadinFactory factory = (VaadinFactory)pUI.getFactory();
			    
			    if (factory != null)
			    {
			        synchronized(factory.oSyncInvokeLater)
			        {
						whmpUIQueue.remove(pUI);
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
	                                          implements Runnable,
	                                                     com.sibvisions.rad.ui.vaadin.impl.VaadinFactory.UIRunnable.Counter
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /** the UI. */
	    private VaadinUI ui;

	    /** the factory. */
	    private VaadinFactory factory;
	    
	    /** the queue. */
	    private ArrayList<Runnable> liQueue;

	    /** the old push mode. */
	    private PushMode pushModeOld;
	    
	    /** the init stack helper Exception. */
	    private Exception exInit;
	    
	    /** the runnable run count. */
	    private int iRunnableRunCount = 0;
	    
	    /** the current count of done (queued) calls. */
	    private int iDoneCount;
	    
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Creates a new instance of <code>UIWorkerThread</code>.
	     * 
	     * @param pUI the UI
	     * @param pQueue the queue
	     */
	    private UIWorkerThread(VaadinUI pUI, ArrayList<Runnable> pQueue)
	    { 
	        ui = pUI;
	        liQueue = pQueue;

	        //the factory is != null here
            factory = (VaadinFactory)pUI.getFactory();
            
            setName(getName() + ", UI = " + System.identityHashCode(pUI));
            
            exInit = new Exception();
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

        /**
         * {@inheritDoc}
         */
        public void increase()
        {
            iRunnableRunCount++;
        }
        
        /**
         * {@inheritDoc}
         */
        public void decrease()
        {
            iRunnableRunCount--;
        }
        
        /**
         * {@inheritDoc}
         */
        public int count()
        {
            return iRunnableRunCount;
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
                while (ui.getLauncher() != null && iRunnableCount > 0)
                {
                    iDoneCount++;
                 
                    synchronized(factory.oSyncInvokeLater)
                    {
                        runner = liQueue.remove(0);
                        
                        iRunnableCount--;
                    }
                    
                    if (!bNotifyBeforeUI)
                    {
                        ui.notifyBeforeUI();
                        
                        bNotifyBeforeUI = true;
                    }

                    if (pushModeOld == null)
                    {
                        PushConfiguration config = ui.getPushConfiguration();
                    
                        VaadinSession session = ui.getSession();
                        session.lock();
                        
                        try
                        {
                            
                            pushModeOld = config.getPushMode();
                            
                            if (pushModeOld != PushMode.MANUAL)
                            {
                                //very important, because ALL UIs will be pushed indirectly via access(...) call.
                                //Because of mutli-threading it's not a good idea to reset the value too early 
                                config.setPushMode(PushMode.MANUAL);
                            }
                        }
                        finally
                        {
                            session.unlock();
                        }
                    }

                    ui.access(new UIRunnable(ui, runner, this, true));
                    
                    //push every 100 runs, to handle endless invokeLater calls
                    if (iDoneCount == 100)
                    {
                        push();
                    }
                    
                    if (iRunnableCount == 0)
                    {
                        boolean bDetached = false;
                        
                        do
                        {
                            Thread.sleep(100);
                            
                            synchronized(factory.oSyncInvokeLater)
                            {
                                iRunnableCount = liQueue.size();
                            }
                            
                            if (iRunnableCount == 0 && iRunnableRunCount > 0)
                            {
                                //no problem -> done count will be checked to avoid multiple push calls
                                if (!push())
                                {
                                    bDetached = true; 
                                }
                            }
                            
                            //The UI locks per Thread and this is the reason why we have to wait until all runnables
                            //were executed. Otherwise it could happen that a previous push is still running while a
                            //new push will be sent (from another Thread) -> throws an Exception
                        }
                        while (iRunnableCount == 0 && iRunnableRunCount > 0 && !bDetached);

                        //all queued runnables were executed and no more runnables are in the queue 
                        if (!bDetached && iRunnableRunCount == 0 && iRunnableCount == 0)
                        {
                            if (push())
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
                                bDetached = true;
                            }
                        }
                    }
                }

                //doesn't push again if already pushed
                push();
            }
            catch (UIDetachedException uide)
            {
                //could happen (e.g. setPushMode)
                LoggerFactory.getInstance(VaadinFactory.class).debug(uide); 
            }
            catch (InterruptedException ie)
            {
                //nothing to be done
            }
            finally
            {
                if (bNotifyBeforeUI)
                {
                    ui.notifyAfterUI();                                    
                }
            }
        }           
	    
        /**
         * Push, if necessary.
         * 
         * @return <code>true</code> if UI is attached, <code>false</code> if detached
         */
        private boolean push()
        {
            VaadinSession session = ui.getSession();

            if (session != null)
            {
                session.lock();

                try
                {
                    if (iDoneCount > 0)
                    {
                        iDoneCount = 0;
        
                        if (ui.isAttached())
                        {
                            try
                            {
                                ui.push();
                                
                                return true;
                            }
                            catch (UIDetachedException de)
                            {
                                //no sync needed because UI is detached
                            	factory.thInvokeLaterWorker = null;
                                
                                return false;
                            }
                        }
                        else
                        {
                        	factory.thInvokeLaterWorker = null;
                            
                            return false;
                        }
                    }
                }
                finally
                {
                    try
                    {
                        if (pushModeOld != null && pushModeOld != PushMode.MANUAL)
                        {
                            try
                            {
                                ui.getPushConfiguration().setPushMode(pushModeOld);
                            }
                            catch (UIDetachedException de)
                            {
                                //nothing to do if not attached
                            }
                        }

                        pushModeOld = null;
                    }
                    finally
                    {
                        session.unlock();
                    }
                }
                
                return true;
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
	private static class UIRunnable implements ErrorHandlingRunnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /** the counter interface. */
        private interface Counter
        {
            /** Increases the count. */
            void increase();
            
            /** Decreases the count. */
            void decrease();
            
            /** 
             * Returns the count.
             * 
             * @return the count
             */
            int count();
        }

        
        /** the original runnable. */
		private Runnable runnable;
		
		/** the calling UI. */
		private VaadinUI ui;
		
		/** the counter delegate. */
		private Counter counter;
		
        /** whether the execution should be synchronized. */
        private boolean bSync;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>UIRunnable</code>.
		 * 
		 * @param pUI the UI
		 * @param pRunnable the runnable
		 * @param pSync <code>true</code> to execute synchronized
		 */
		protected UIRunnable(VaadinUI pUI, Runnable pRunnable, boolean pSync)
		{
		    this(pUI, pRunnable, null, pSync);
		}

        /**
         * Creates a new instance of <code>UIRunnable</code>.
         * 
         * @param pUI the UI
         * @param pRunnable the runnable
         * @param pCounter the Runnable "executed" count object
         * @param pSync <code>true</code> to execute synchronized
         */
        protected UIRunnable(VaadinUI pUI, Runnable pRunnable, Counter pCounter, boolean pSync)
        {
            ui = pUI;
            runnable = pRunnable;
            counter = pCounter;
            bSync = pSync;
            
            if (counter != null)
            {
                counter.increase();
            }
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
    				if (!(runnable instanceof UIRunnable) && ui != null)
    				{
                        //launcher already destroyed -> nothing to do
    				    if (ui.getLauncher() == null)
    				    {
    				        return;
    				    }
    				    
    				    boolean bRestore = UI.getCurrent() == null;
    				    
    				    Map<Class<?>, CurrentInstance> mapOld = null;
    				    
    				    if (bRestore)
    				    {
    				        mapOld = CurrentInstance.setCurrent(ui);
    				    }
    				    
    					try
    					{
    						boolean bInitFactory = UIFactoryManager.getFactory() == null;
    						
    						if (bInitFactory)
    						{
    							ui.notifyBeforeUI();
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
    								ui.notifyAfterUI();
    							}
    						}
    					}
    					finally
    					{
    					    if (bRestore)
    					    {
    					        CurrentInstance.restoreInstances(mapOld);
    					    }
    					}
    				}
    				else
    				{
    					runnable.run();
    				}
    			}
		    }
		    finally
		    {
		        if (counter != null)
		        {
		            counter.decrease();
		        }
		    }
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void handleError(Exception pCause)
		{
		    Thread th = Thread.currentThread();
		    
		    if (th instanceof UIWorkerThread)
		    {
		        ThreadManager thman = ((VaadinFactory)ui.getFactory()).threadManager;
		        
		        if (thman != null && !thman.isStopped(th))
		        {
		            LoggerFactory.getInstance(VaadinFactory.class).error(((UIWorkerThread)th).exInit);
		        }
		    }
		    else
		    {
		        LoggerFactory.getInstance(VaadinFactory.class).error(pCause);
		    }
		}
	
	}	// UIRunnable
	
} 	// VaadinFactory
