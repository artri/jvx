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
 * 09.04.2009 - [JR] - used InternalToolBarPanel
 * 21.07.2009 - [JR] - dispose, isDisposed implemented
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.LayoutManager;
import java.lang.reflect.Method;

import javax.rad.ui.ICursor;
import javax.rad.ui.IImage;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowClosedListener;
import javax.rad.ui.event.type.window.IWindowClosingListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;
import javax.rad.ui.event.type.window.IWindowDeiconifiedListener;
import javax.rad.ui.event.type.window.IWindowIconifiedListener;
import javax.rad.ui.event.type.window.IWindowOpenedListener;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.util.TranslationMap;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

import com.sibvisions.rad.ui.awt.impl.AwtContainer;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.focus.TabIndexFocusTraversalPolicy;

/**
 * The <code>SwingAbstractFrame</code> is the <code>IFrame</code>
 * implementation for swing.<br>
 * I is a top-level window with a title and a border.
 * <p>
 * The size of the frame includes any area designated for the border.
 * 
 * @param <C> the Container.
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JFrame
 * @see IFrame
 */
public abstract class SwingAbstractFrame<C extends Container> extends AwtContainer<C> 
                                           implements IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** EventHandler for windowOpened. */
	protected WindowHandler<IWindowOpenedListener> eventWindowOpened = null;
	/** EventHandler for windowClosing. */
	protected WindowHandler<IWindowClosingListener> eventWindowClosing = null;
	/** EventHandler for windowClosed. */
	protected WindowHandler<IWindowClosedListener> eventWindowClosed = null;
	/** EventHandler for windowActivated. */
	protected WindowHandler<IWindowActivatedListener> eventWindowActivated = null;
	/** EventHandler for windowDeactivated. */
	protected WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated = null;
	/** EventHandler for windowIconified. */
	protected WindowHandler<IWindowIconifiedListener> eventWindowIconified = null;
	/** EventHandler for windowDeiconified. */
	protected WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified = null;
	
	/** the icon image. */ 
	protected IImage iconImage = null;
	
	/** the menu bar. */ 
	protected IMenuBar menuBar = null;
	
    /** the panel for the toolbar(s). */
    private InternalToolBarPanel toolBarPanel = new InternalToolBarPanel();

    /** the translation mapping. */
    private TranslationMap translation;
    
    /** indicates whether the frame is disposed. */
	protected boolean bDisposed = false;
	
    /** whether the window listener was added. */
    protected boolean bWindowListener = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingAbstractFrame</code>.
	 * 
	 * @param pContainer the Container
	 */
	public SwingAbstractFrame(C pContainer)
	{
		super(pContainer);

		toolBarPanel.setToolBarOwner(this);
		toolBarPanel.setBackground(null);
		toolBarPanel.setForeground(null);
		toolBarPanel.setFont(null);
		toolBarPanel.setCursor(null);
		
		try
		{
			Method metRootPane = pContainer.getClass().getMethod("getRootPane");
			
			JRootPane rootPane = (JRootPane)metRootPane.invoke(pContainer);
			
			rootPane.setContentPane(toolBarPanel);
		}	
		catch (Exception e)
		{
			resource.setLayout(new BorderLayout());
			resource.add(toolBarPanel, BorderLayout.CENTER);
		}
		
		resource.setFocusTraversalPolicy(new TabIndexFocusTraversalPolicy());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// IFrame
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDisposed()
	{
		return bDisposed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getIconImage()
	{
		return iconImage;
	}
	
 	/**
 	 * {@inheritDoc}
 	 */
    public IMenuBar getMenuBar()
    {
    	 return menuBar;
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        translation = pTranslation;
    }
    
    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return translation;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar)
	{
		addToolBar(pToolBar, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		toolBarPanel.addUIToolBar(pToolBar, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		toolBarPanel.removeUIToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.removeUIToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		int iSize;
		while ((iSize = toolBarPanel.getUIToolBarCount()) > 0)
		{
			removeToolBar(iSize - 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return toolBarPanel.getUIToolBarCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IToolBar getToolBar(int pIndex)
	{
		return toolBarPanel.getUIToolBar(pIndex);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBarPanel.indexOfUIToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pArea)
	{
		toolBarPanel.setUIArea(pArea);
	}	

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return toolBarPanel.getUIArea();
	}	
	
	/**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowOpenedListener> eventWindowOpened()
    {
		if (eventWindowOpened == null)
		{
			eventWindowOpened = new WindowHandler<IWindowOpenedListener>(IWindowOpenedListener.class);
			
			addWindowListener();
		}
		
		return eventWindowOpened;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowClosingListener> eventWindowClosing()
    {
		if (eventWindowClosing == null)
		{
			eventWindowClosing = new WindowHandler<IWindowClosingListener>(IWindowClosingListener.class);
			
			addWindowListener();
		}
		
		return eventWindowClosing;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowClosedListener> eventWindowClosed()
    {
		if (eventWindowClosed == null)
		{
			eventWindowClosed = new WindowHandler<IWindowClosedListener>(IWindowClosedListener.class);
			
			addWindowListener();
		}
		
		return eventWindowClosed;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowIconifiedListener> eventWindowIconified()
    {
		if (eventWindowIconified == null)
		{
			eventWindowIconified = new WindowHandler<IWindowIconifiedListener>(IWindowIconifiedListener.class);
			
			addWindowListener();
		}
		
		return eventWindowIconified;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified()
    {
		if (eventWindowDeiconified == null)
		{
			eventWindowDeiconified = new WindowHandler<IWindowDeiconifiedListener>(IWindowDeiconifiedListener.class);
			
			addWindowListener();
		}
		
		return eventWindowDeiconified;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowActivatedListener> eventWindowActivated()
    {
		if (eventWindowActivated == null)
		{
			eventWindowActivated = new WindowHandler<IWindowActivatedListener>(IWindowActivatedListener.class);
			
			addWindowListener();
		}
		return eventWindowActivated;
    }

    /**
	 * {@inheritDoc}
	 */
    public WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated()
    {
		if (eventWindowDeactivated == null)
		{
			eventWindowDeactivated = new WindowHandler<IWindowDeactivatedListener>(IWindowDeactivatedListener.class);
			
			addWindowListener();
		}
		
		return eventWindowDeactivated;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Adds a window listener for this component.
     */
    protected abstract void addWindowListenerToResource();
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the cursor of the glass pane as "global" cursor.
	 * 
	 * @param pCursor the "global" cursor
	 */
    public void setCursor(ICursor pCursor)
    {
    	super.setCursor(pCursor);    	

    	if (pCursor == null)
    	{
    		JVxUtil.setGlobalCursor(resource, null);
    	}
    	else
    	{
    		JVxUtil.setGlobalCursor(resource, (Cursor)pCursor.getResource());
    	}
    }
	
    /**
     * {@inheritDoc}
     */
    public void setVisible(boolean pVisible)
    {
    	super.setVisible(pVisible);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutIntern(LayoutManager pLayoutManager)
	{
		toolBarPanel.setLayout(pLayoutManager);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addIntern(Component pComponent, Object pConstraints, int pIndex)
	{
		toolBarPanel.add(pComponent, pConstraints, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeIntern(Component pComponent)
	{
		toolBarPanel.remove(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Marks the frame as disposed.
	 */
	public void dispose()
	{
		bDisposed = true;
	}
	
	/**
	 * Adds the toolbar at a specific index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the position
	 */
	protected void addToolBarIntern(JToolBar pToolBar, int pIndex)
	{
		toolBarPanel.addToolBar(pToolBar, pIndex);
	}
	
	/**
	 * Removes the toolbar from the panel.
	 * 
	 * @param pToolBar the toolbar to be removed
	 */
	protected void removeToolBarIntern(JToolBar pToolBar)
	{
		toolBarPanel.removeToolBar(pToolBar);
	}

	/**
	 * Adds the window listener if not already added.
	 */
	protected void addWindowListener()
	{
        if (!bWindowListener)
        {
            bWindowListener = true;

            addWindowListenerToResource();
        }
	}
	
}	// SwingAbstractFrame
