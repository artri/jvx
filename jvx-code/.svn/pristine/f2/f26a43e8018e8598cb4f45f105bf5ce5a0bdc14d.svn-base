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
 * 01.10.2008 - [HM] - creation
 * 01.11.2008 - [JR] - replaceParameter implemented
 * 20.11.2008 - [JR] - UI redesign
 * 10.02.2009 - [JR] - createFileChooser implemented
 * 26.02.2009 - [JR] - turned off the taskbar
 * 12.09.2011 - [JR] - don't dispose frame because it sends the CLOSED event to listeners
 * 04.10.2013 - [JR] - #820: isLaFOpaque now checks border
 * 18.11.2013 - [JR] - #874: setLookAndFeel: defaults only for built-in LaFs, additional config class
 * 08.03.2014 - [LT] - #913: include method to create a GridLayout
 * 31.05.2016 - [JR] - #1564: style property support
 * 21.03.2018 - [JR] - IPopupMenuButton support
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.rad.application.ILauncher;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.InvokeLaterThread;
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
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIKeyEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;

import com.sibvisions.rad.ui.awt.impl.AwtColor;
import com.sibvisions.rad.ui.awt.impl.AwtComponent;
import com.sibvisions.rad.ui.awt.impl.AwtContainer;
import com.sibvisions.rad.ui.awt.impl.AwtDimension;
import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.ext.JVxConstants;
import com.sibvisions.rad.ui.swing.ext.JVxInternalFrame;
import com.sibvisions.rad.ui.swing.ext.JVxPanel;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxCheckBoxCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxChoiceCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxDateCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxImageViewer;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxLinkedCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxNumberCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxTextCellEditor;
import com.sibvisions.rad.ui.swing.impl.component.SwingButton;
import com.sibvisions.rad.ui.swing.impl.component.SwingCheckBox;
import com.sibvisions.rad.ui.swing.impl.component.SwingIcon;
import com.sibvisions.rad.ui.swing.impl.component.SwingLabel;
import com.sibvisions.rad.ui.swing.impl.component.SwingPasswordField;
import com.sibvisions.rad.ui.swing.impl.component.SwingPopupMenuButton;
import com.sibvisions.rad.ui.swing.impl.component.SwingRadioButton;
import com.sibvisions.rad.ui.swing.impl.component.SwingTextArea;
import com.sibvisions.rad.ui.swing.impl.component.SwingTextField;
import com.sibvisions.rad.ui.swing.impl.component.SwingToggleButton;
import com.sibvisions.rad.ui.swing.impl.component.map.SwingMap;
import com.sibvisions.rad.ui.swing.impl.container.SwingDesktopPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingFrame;
import com.sibvisions.rad.ui.swing.impl.container.SwingGroupPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingInternalFrame;
import com.sibvisions.rad.ui.swing.impl.container.SwingPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingScrollPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingSplitPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingTabsetPanel;
import com.sibvisions.rad.ui.swing.impl.container.SwingToolBar;
import com.sibvisions.rad.ui.swing.impl.container.SwingToolBarPanel;
import com.sibvisions.rad.ui.swing.impl.control.SwingCellFormat;
import com.sibvisions.rad.ui.swing.impl.control.SwingChart;
import com.sibvisions.rad.ui.swing.impl.control.SwingEditor;
import com.sibvisions.rad.ui.swing.impl.control.SwingGauge;
import com.sibvisions.rad.ui.swing.impl.control.SwingTable;
import com.sibvisions.rad.ui.swing.impl.control.SwingTree;
import com.sibvisions.rad.ui.swing.impl.layout.AwtBorderLayout;
import com.sibvisions.rad.ui.swing.impl.layout.AwtFlowLayout;
import com.sibvisions.rad.ui.swing.impl.layout.AwtFormLayout;
import com.sibvisions.rad.ui.swing.impl.layout.AwtGridLayout;
import com.sibvisions.rad.ui.swing.impl.menu.SwingCheckBoxMenuItem;
import com.sibvisions.rad.ui.swing.impl.menu.SwingMenu;
import com.sibvisions.rad.ui.swing.impl.menu.SwingMenuBar;
import com.sibvisions.rad.ui.swing.impl.menu.SwingMenuItem;
import com.sibvisions.rad.ui.swing.impl.menu.SwingPopupMenu;
import com.sibvisions.rad.ui.swing.impl.menu.SwingSeparator;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SwingFactory</code> class encapsulates methods to
 * create and access Swing components.
 * 
 * @author Martin Handsteiner
 * @see AwtFactory
 */
public class SwingFactory extends AwtFactory 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** whether the LaF has opaque feature. */
	private static Boolean bIsLaFOpaque = null;
	
	/** whether the Laf is a mac LaF. */
	private static Boolean bIsMacLaf = null;
    /** whether the Laf is a windows LaF. */
    private static Boolean bIsWindowsLaf = null;
    /** whether the Laf is a flat LaF. */
    private static Boolean bIsFlatLaf = null;
    
    /** whether the OS is MacOS. */
    private static boolean bIsMacOS;
    /** whether the OS is windows. */
    private static boolean bIsWindows;
    /** whether the OS is linux. */
    private static boolean bIsLinux;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        try
        {
            String sOS = System.getProperty("os.name").toLowerCase(); 
            
            bIsMacOS = sOS.indexOf("mac") >= 0;
            bIsWindows = sOS.indexOf("windows") >= 0;
            bIsLinux = sOS.indexOf("linux") >= 0;
        }
        catch (Exception e)
        {
            //not allowed to access os.name property
        }
    }

	/**
	 * Creates a new instance of <code>SwingFactory</code>.
	 */
	public SwingFactory()
	{
	}

	/**
	 * {@inheritDoc}
	 */
    protected void initFactory()
    {
        // components
        registerComponent(ILabel.class,             SwingLabel.class);
        registerComponent(ITextField.class,         SwingTextField.class);
        registerComponent(IPasswordField.class,     SwingPasswordField.class);
        registerComponent(ITextArea.class,          SwingTextArea.class);
        registerComponent(IIcon.class,              SwingIcon.class);
        registerComponent(IButton.class,            SwingButton.class);
        registerComponent(IToggleButton.class,      SwingToggleButton.class);
        registerComponent(IPopupMenuButton.class,   SwingPopupMenuButton.class);
        registerComponent(ICheckBox.class,          SwingCheckBox.class);
        registerComponent(IRadioButton.class,       SwingRadioButton.class);
        // container
        registerComponent(IPanel.class,             SwingPanel.class);
        registerComponent(IToolBarPanel.class,      SwingToolBarPanel.class);
        registerComponent(IGroupPanel.class,        SwingGroupPanel.class);
        registerComponent(IScrollPanel.class,       SwingScrollPanel.class);
        registerComponent(ISplitPanel.class,        SwingSplitPanel.class);
        registerComponent(ITabsetPanel.class,       SwingTabsetPanel.class);
        registerComponent(IToolBar.class,           SwingToolBar.class);
        registerComponent(IDesktopPanel.class,      SwingDesktopPanel.class);
        // menu
        registerComponent(IMenuItem.class,          SwingMenuItem.class);
        registerComponent(ICheckBoxMenuItem.class,  SwingCheckBoxMenuItem.class);
        registerComponent(IMenu.class,              SwingMenu.class);
        registerComponent(IMenuBar.class,           SwingMenuBar.class);
        registerComponent(IMenu.class,              SwingMenu.class);
        registerComponent(IPopupMenu.class,         SwingPopupMenu.class);
        registerComponent(ISeparator.class,         SwingSeparator.class);
        // controls
        registerComponent(IEditor.class,            SwingEditor.class);
        registerComponent(ITable.class,             SwingTable.class);
        registerComponent(ITree.class,              SwingTree.class);
        registerComponent(IChart.class,             SwingChart.class);
        registerComponent(IGauge.class,             SwingGauge.class);
        registerComponent(IMap.class,               SwingMap.class);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public <C extends IComponent> Class<C> getComponentBaseClass()
    {
        return (Class<C>)SwingComponent.class;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IColor getSystemColor(String pType)
	{
		Color color = JVxUtil.getSystemColor(pType);
		
		if (color == null)
		{
			return null;
		}
		else
		{
			return new AwtColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSystemColor(String pType, IColor pSystemColor)
	{
		if (pSystemColor == null)
		{
			JVxUtil.setSystemColor(pType, null);
		}
		else
		{
			JVxUtil.setSystemColor(pType, (Color)pSystemColor.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
    public IImage getImage(String pImageName)
    {
    	ImageIcon icon = JVxUtil.createIcon(pImageName);
    	if (icon == null)
    	{
    		return null;
    	}
    	else
    	{
    		return new SwingImage(pImageName, icon);
    	}
    }
   
	/**
	 * {@inheritDoc}
	 */
    public IImage getImage(String pImageName, byte[] pData)
    {
    	ImageIcon icon = JVxUtil.getIcon(pImageName, pData);
    	if (icon == null)
    	{
    		return null;
    	}
    	else
    	{
    		return new SwingImage(pImageName, icon);
    	}
    }
    
    /**
	 * {@inheritDoc}
	 */
    public String getImageMapping(String pMappingName)
    {
    	return JVxUtil.getImageMapping(pMappingName);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setImageMapping(String pMappingName, String pImageName)
    {
    	JVxUtil.setImageMapping(pMappingName, pImageName);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public String[] getImageMappingNames()
    {
    	return JVxUtil.getImageMappingNames();
    }

    /**
	 * {@inheritDoc}
	 */
    public ICellFormat createCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent)
    {
    	return new SwingCellFormat(pBackground, pForeground, pFont, pImage, pStyle, pLeftIndent);
    }

    /**
	 * {@inheritDoc}
	 */
    public ICellEditor getDefaultCellEditor(Class<?> pClass)
    {
    	return JVxUtil.getDefaultCellEditor(pClass);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
    {
    	JVxUtil.setDefaultCellEditor(pClass, pCellEditor);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public IImageViewer createImageViewer()
    {
    	return new JVxImageViewer();
    }

    /**
	 * {@inheritDoc}
	 */
    public IChoiceCellEditor createChoiceCellEditor()
    {
    	return new JVxChoiceCellEditor();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public ICheckBoxCellEditor createCheckBoxCellEditor()
    {
    	return new JVxCheckBoxCellEditor();
    }
    
    /**
	 * {@inheritDoc}
	 */
    public IDateCellEditor createDateCellEditor()
    {
    	return new JVxDateCellEditor();
    }
   
    /**
	 * {@inheritDoc}
	 */
    public ILinkedCellEditor createLinkedCellEditor()
    {
    	return new JVxLinkedCellEditor();
    }
   
    /**
	 * {@inheritDoc}
	 */
    public INumberCellEditor createNumberCellEditor()
    {
    	return new JVxNumberCellEditor();
    }
   
    /**
	 * {@inheritDoc}
	 */
    public ITextCellEditor createTextCellEditor()
    {
    	return new JVxTextCellEditor();
    }

    /**
	 * {@inheritDoc}
	 */
    public IInternalFrame createInternalFrame(IDesktopPanel pDesktopPanel)
    {
    	SwingInternalFrame result = new SwingInternalFrame(pDesktopPanel);
    	result.setFactory(this);
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
		SwingFrame result = new SwingFrame();
		result.setFactory(this);
		return result;
	}

    /**
	 * {@inheritDoc}
	 */
	public IComponent createCustomComponent(Object pCustomComponent) 
	{
		AwtComponent result;
		if (pCustomComponent instanceof JScrollPane)
		{
		    result = new SwingScrollComponent<JScrollPane, JComponent>((JScrollPane)pCustomComponent);
		}
		else
		{
            result = new SwingScrollComponent<JComponent, JComponent>((JComponent)pCustomComponent);
		}
		result.setFactory(this);
		return result;
	}

    /**
	 * {@inheritDoc}
	 */
	public IContainer createCustomContainer(Object pCustomContainer) 
	{
        AwtContainer result;
        if (pCustomContainer instanceof JScrollPane)
        {
            result = new SwingScrollComponent<JScrollPane, JComponent>((JScrollPane)pCustomContainer);
        }
        else
        {
            result = new SwingScrollComponent<JComponent, JComponent>((JComponent)pCustomContainer);
        }
		result.setFactory(this);
		return result;
	}
	
    /**
	 * {@inheritDoc}
	 */
	public IBorderLayout createBorderLayout() 
	{
		return new AwtBorderLayout();
	}
	
    /**
	 * {@inheritDoc}
	 */
	public IFlowLayout createFlowLayout() 
	{
		return new AwtFlowLayout();
	}
	
    /**
	 * {@inheritDoc}
	 */
	public IFormLayout createFormLayout() 
	{
		return new AwtFormLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IGridLayout createGridLayout(int columns, int rows) 
	{
		return new AwtGridLayout(columns, rows);
	}
	
    /**
	 * {@inheritDoc}
	 */
	public void invokeLater(Runnable pRunnable)
	{
		JVxUtil.invokeLater(pRunnable);
	}
	
    /**
	 * {@inheritDoc}
	 */
	public void invokeAndWait(Runnable pRunnable) throws Exception
	{
        if (EventQueue.isDispatchThread()) 
        {
    		pRunnable.run();
        }
        else
        {
    		SwingUtilities.invokeAndWait(pRunnable);
        }
	}
	
    /**
	 * {@inheritDoc}
	 */
	public Thread invokeInThread(Runnable pRunnable)
	{
		return ThreadHandler.start(new InvokeLaterThread(this, pRunnable));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * Gets the vertical {@link IAlignmentConstants} alignment from given 
	 * {@link JVxConstants} alignment.
	 * 
	 * @param pAlign the alignment from {@link JVxConstants}
	 * @return alignment from {@link IAlignmentConstants}
	 */
	public static int getVerticalAlignment(int pAlign)
	{
		switch (pAlign) 
		{
			case JVxConstants.TOP:  
				return IAlignmentConstants.ALIGN_TOP;
			case JVxConstants.BOTTOM: 
				return IAlignmentConstants.ALIGN_BOTTOM;
			case JVxConstants.CENTER:
				return IAlignmentConstants.ALIGN_CENTER;
			case JVxConstants.STRETCH:
				return IAlignmentConstants.ALIGN_STRETCH;
			default:
				return pAlign;
		}
	}

	/**
	 * Gets the vertical {@link JVxConstants} alignment from given 
	 * {@link IAlignmentConstants} alignment.
	 * 
	 * @param pAlign the alignment from {@link IAlignmentConstants}
	 * @return alignment from <code>JComponent</code>
	 */
	public static int getVerticalSwingAlignment(int pAlign)
	{
		switch (pAlign) 
		{
			case IAlignmentConstants.ALIGN_TOP:  
				return JVxConstants.TOP;
			case IAlignmentConstants.ALIGN_BOTTOM: 
				return JVxConstants.BOTTOM;
			case IAlignmentConstants.ALIGN_CENTER:
				return JVxConstants.CENTER;
			case IAlignmentConstants.ALIGN_STRETCH:
				return JVxConstants.STRETCH;
			default:          
				return pAlign;
		}
	}

	/**
	 * Gets the horizontal {@link IAlignmentConstants} alignment from given 
	 * {@link JVxConstants} alignment.
	 * 
	 * @param pAlign the alignment from {@link JVxConstants}
	 * @return alignment from {@link IAlignmentConstants}
	 */
	public static int getHorizontalAlignment(int pAlign)
	{
		switch (pAlign) 
		{
			case JVxConstants.LEFT:  
				return IAlignmentConstants.ALIGN_LEFT;
			case JVxConstants.RIGHT: 
				return IAlignmentConstants.ALIGN_RIGHT;
			case JVxConstants.LEADING: 
				return IAlignmentConstants.ALIGN_LEFT;
			case JVxConstants.TRAILING: 
				return IAlignmentConstants.ALIGN_RIGHT;
			case JVxConstants.CENTER:
				return IAlignmentConstants.ALIGN_CENTER;
			case JVxConstants.STRETCH:
				return IAlignmentConstants.ALIGN_STRETCH;
			default:          
				return pAlign;
		}
	}

	/**
	 * Gets the horizontal {@link JVxConstants} alignment from given 
	 * {@link IAlignmentConstants} alignment.
	 * 
	 * @param pAlign the alignment from {@link IAlignmentConstants}
	 * @return alignment from <code>JComponent</code>
	 */
	public static int getHorizontalSwingAlignment(int pAlign)
	{
		switch (pAlign) 
		{
            case IAlignmentConstants.ALIGN_DEFAULT:
			case IAlignmentConstants.ALIGN_LEFT:
				return JVxConstants.LEFT;
			case IAlignmentConstants.ALIGN_RIGHT: 
				return JVxConstants.RIGHT;
			case IAlignmentConstants.ALIGN_CENTER:
				return JVxConstants.CENTER;
			case IAlignmentConstants.ALIGN_STRETCH:
				return JVxConstants.STRETCH;
			default:
				return pAlign;
		}
	}

	/**
     * Sets the look and feel which decorates all components.
     * 
     * @param pLauncher the launcher for which the LaF should be changed
     * @param pClassName the full qualified class name of the look and feel
     */
	public static void setLookAndFeel(ILauncher pLauncher, String pClassName)
	{
		String sOldName = UIManager.getLookAndFeel().getClass().getName();
		
		boolean bLafChanged = !sOldName.equals(pClassName);
		
		// We won't use the taskbar (GTK LaF enables the taskbar)
		UIManager.put("InternalFrame.useTaskBar", Boolean.FALSE);

		// Only for built-in LaFs
		if (pClassName == null 
			|| pClassName.startsWith("com.sun.java.swing.plaf")
			|| pClassName.startsWith("javax.swing.plaf"))
		{
			UIManager.put("SplitPane.oneTouchButtonSize", Integer.valueOf(4));

			// Use new modern titled border by default.
	        UIManager.put("TitledBorder.style", Integer.valueOf(JVxPanel.MODERN_TITLED_BORDER));
		}
		else if (pClassName.startsWith("com.formdev.flatlaf"))
		{
            // Use new modern titled border by default.
            UIManager.put("TitledBorder.style", Integer.valueOf(JVxPanel.MODERN_TITLED_BORDER));
		}
        
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.FALSE);
		
		String sLaFConfig = pLauncher.getParameter("LookAndFeel.config");
		
		if (!StringUtil.isEmpty(sLaFConfig))
		{
			try
			{
				//initialize class
				ILookAndFeelConfiguration lafconfig = (ILookAndFeelConfiguration)Reflective.construct(sLaFConfig);

				lafconfig.setDefaults(pLauncher, pClassName);
			}
			catch (Throwable th)
			{
				th.printStackTrace();
			}
		}

		if (bLafChanged)
		{
	        //re-init on next usage!
	        bIsLaFOpaque = null;
	        bIsMacLaf = null;
	        
    		boolean bUseSystemLaF = false;
    		
    		try
    		{
    			if (pClassName != null)
    			{
    				UIManager.setLookAndFeel(pClassName);
    			}
    			else
    			{
    				bUseSystemLaF = true;
    			}
    		}
    		catch (Exception e)
    		{
    			bUseSystemLaF = pClassName != null;
    		}
    		
    		if (bUseSystemLaF)
    		{
    			try
    			{
    				String sSystemLaF = UIManager.getSystemLookAndFeelClassName();
    				
    				// Don't set the same LaF again!
    				if (sOldName.equals(sSystemLaF))
    				{
    					return;
    				}
    
    				// Use the System-LaF
    				UIManager.setLookAndFeel(sSystemLaF);
    			}
    			catch (Exception ex)
    			{
    				// The java default LaF will be used!
    			}
    		}
		
        	Component comp = (Component)pLauncher.getResource();
        	
        	while (comp != null && !(comp instanceof JFrame))
    		{
        		comp = comp.getParent();
    		}
        	
        	if (comp != null)
        	{
        		boolean bSupportUndecoration = UIManager.getLookAndFeel().getSupportsWindowDecorations();
        		
        		if (comp.isVisible())
        		{
        			comp.removeNotify();
        			comp.setVisible(false);
    
        			if (bSupportUndecoration)
        			{
    		    		((JFrame)comp).setUndecorated(bSupportUndecoration);
    		    		((JFrame)comp).getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        			}
    	    		
        			comp.addNotify();
    	    		comp.setVisible(true);
        		}
        		else
        		{
        			boolean bNotified = false;
        			
        			if (comp.getParent() != null)
        			{
        				bNotified = true;
        			}
        			
        			comp.removeNotify();
        			comp.setVisible(false);
    
        			if (bSupportUndecoration)
        			{
    		    		((JFrame)comp).setUndecorated(bSupportUndecoration);
    		    		((JFrame)comp).getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        			}
    	    		
    	    		if (bNotified)
    	    		{
    	    			comp.addNotify();
    	    		}
        		}
        	}
    		
        	// Update from the top component!
        	comp = (Component)pLauncher.getResource();
    
        	if (comp != null)
        	{
    	    	while (comp.getParent() != null)
    			{
    	    		comp = comp.getParent();
    			}
    	
    	    	SwingUtilities.updateComponentTreeUI(comp);
        	}
        }
	}
	
	/**
	 * Gets the {@link KeyStroke} for a {@link Key}.
	 * 
	 * @param pKey the UI {@link Key}
	 * @return the swing {@link KeyStroke}
	 */
    public static KeyStroke getKeyStroke(Key pKey)
    {
    	if (pKey == null)
    	{
    		return null;
    	}
    	else if (pKey.getKeyEventType() == UIKeyEvent.KEY_TYPED)
    	{
        	return KeyStroke.getKeyStroke(pKey.getKeyChar(), pKey.getModifiers());
    	}
    	else
    	{
        	return KeyStroke.getKeyStroke(pKey.getKeyCode(), pKey.getModifiers(), pKey.getKeyEventType() == UIKeyEvent.KEY_RELEASED);
    	}
    }
	
    /**
     * Gets the {@link Key} for a {@link KeyStroke}.
     * 
     * @param pKeyStroke the swing {@link KeyStroke}
     * @return the UI {@link Key}
     */
    public static Key getKey(KeyStroke pKeyStroke)
    {
    	if (pKeyStroke == null)
    	{
    		return null;
    	}
    	else if (pKeyStroke.getKeyEventType() == KeyEvent.KEY_TYPED)
    	{
    		return Key.getKey(pKeyStroke.getKeyChar(), unmapNewModifiers(pKeyStroke.getModifiers()));
    	}
    	else if (pKeyStroke.getKeyEventType() == KeyEvent.KEY_PRESSED)
    	{
    		return Key.getKeyOnPressed(pKeyStroke.getKeyCode(), unmapNewModifiers(pKeyStroke.getModifiers()));
    	}
    	else // KeyEvent.KEY_RELEASED
    	{
    		return Key.getKeyOnReleased(pKeyStroke.getKeyCode(), unmapNewModifiers(pKeyStroke.getModifiers()));
    	}
    }

    /**
     * Gets the old style modifiers. The new style contains e.g. CTRL_DOWN_MASK and CTRL_MASK.
     * The old style modifier only contains CTRL_MASK.
     * 
     * @param pModifiers the new or old style modifier
     * @return the new style modifier
     */
    private static int unmapNewModifiers(int pModifiers) 
    {
    	int iModifier = 0;
    	
       	if ((pModifiers & InputEvent.SHIFT_DOWN_MASK) != 0) 
       	{
       		iModifier |= InputEvent.SHIFT_MASK;
       	}
       	
		if ((pModifiers & InputEvent.ALT_DOWN_MASK) != 0) 
		{
			iModifier |= InputEvent.ALT_MASK;
		}
	
		if ((pModifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) 
		{
			iModifier |= InputEvent.ALT_GRAPH_MASK;
		}
		
		if ((pModifiers & InputEvent.CTRL_DOWN_MASK) != 0) 
		{
			iModifier |= InputEvent.CTRL_MASK;
		}
		
		if ((pModifiers & InputEvent.META_DOWN_MASK) != 0) 
		{
			iModifier |= InputEvent.META_MASK;
		}
 	
		return iModifier;
    }

	/**
	 * Creates an internal frame on a desktop to show an exception.
	 * 
	 * @param pThrowable the exception
	 * @param pDesktop the desktop on which the frame should appear
	 */
	static void showError(Throwable pThrowable, JDesktopPane pDesktop)
	{
		pThrowable.printStackTrace();

		JPanel panError = new JPanel();
		panError.setLayout(new BorderLayout());

		SwingTextArea taError = new SwingTextArea();
		taError.setPreferredSize(new AwtDimension(470, 300));
		taError.setText(ExceptionUtil.dump(pThrowable, true));

		JVxInternalFrame frame = new JVxInternalFrame();
		frame.setIconifiable(false);
		frame.setResizable(true);
		frame.setTitle("InstantiationError");

		pDesktop.add(frame);
		
		frame.add(taError.getResource(), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(pDesktop);
		frame.setVisible(true);
	}
	
	/**
	 * Gets whether the current Look and Feel draws its own background independent of
	 * the background color and opaque setting.
	 * 
	 * @return <code>true</code> whether the LaF has its own opaque drawing mechanism
	 */
	public static boolean isLaFOpaque()
	{
		if (bIsLaFOpaque == null)
		{
			String sName = UIManager.getLookAndFeel().getClass().getName();

			if (sName.endsWith("NimbusLookAndFeel") 
				|| sName.endsWith("MetalLookAndFeel")
				|| sName.endsWith("WindowsClassicLookAndFeel")
				|| sName.startsWith("com.formdev.flatlaf.")
				|| (sName.endsWith("WindowsLookAndFeel")
				    && (UIManager.getLookAndFeel().getDefaults().getBorder("Button.border") instanceof CompoundBorderUIResource
				       || UIManager.getLookAndFeel().getDefaults().getBorder("Button.border") instanceof CompoundBorder)))
			{
				bIsLaFOpaque = Boolean.FALSE;
			}
			else
			{
			    bIsLaFOpaque = Boolean.TRUE;
			}
		}
		
		return bIsLaFOpaque.booleanValue();
	}
	
	/**
	 * Gets whether the current LaF is the MacOS standard LaF.
	 * 
	 * @return <code>true</code> if mac standard LaF is used, <code>false</code> otherwise
	 */
	public static boolean isMacLaF()
	{
		if (bIsMacLaf == null)
		{
			String sName = UIManager.getLookAndFeel().getClass().getName();
			
			if (sName.startsWith("com.apple.laf."))
			{
				bIsMacLaf = Boolean.TRUE;
			}
			else
			{
				bIsMacLaf = Boolean.FALSE;
			}
		}
		
		return bIsMacLaf.booleanValue();
	}
	
    /**
     * Gets whether the current LaF is the windows standard LaF.
     * 
     * @return <code>true</code> if windows standard LaF is used, <code>false</code> otherwise
     */
    public static boolean isWindowsLaF()
    {
        if (bIsWindowsLaf == null)
        {
            String sName = UIManager.getLookAndFeel().getClass().getName();
            
            if (sName.contains("Windows"))
            {
                bIsWindowsLaf = Boolean.TRUE;
            }
            else
            {
                bIsWindowsLaf = Boolean.FALSE;
            }
        }
        
        return bIsWindowsLaf.booleanValue();
    }
    
    /**
     * Gets whether flat LaF is used.
     * 
     * @return <code>true</code> if flat look and feel is used, <code>false</code> otherwise
     */
    public static boolean isFlatLaF()
    {
        if (bIsFlatLaf == null)
        {
            String sName = UIManager.getLookAndFeel().getClass().getName();
            
            if (sName.startsWith("com.formdev.flatlaf."))
            {
                bIsFlatLaf = Boolean.TRUE;
            }
            else
            {
                bIsFlatLaf = Boolean.FALSE;
            }
        }
        
        return bIsFlatLaf.booleanValue();
    }
    
	/**
	 * Gets whether the operating system is MacOS.
	 * 
	 * @return <code>true</code> if MacOS, <code>false</code> otherwise
	 */
	public static boolean isMacOS()
	{
	    return bIsMacOS;
	}
	
    /**
     * Gets whether the operating system is Linux.
     * 
     * @return <code>true</code> if Linux, <code>false</code> otherwise
     */
	public static boolean isLinux()
	{
	    return bIsLinux;
	}
	
    /**
     * Gets whether the operating system is Windows.
     * 
     * @return <code>true</code> if Windows, <code>false</code> otherwise
     */
	public static boolean isWindows()
	{
	    return bIsWindows;
	}	
	
}	// SwingFactory
