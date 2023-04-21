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
 * 12.10.2008 - [JR] - dispose: moved getParent().remove to internalFrameClosed -> support for resource.dispose calls!
 * 23.01.2009 - [JR] - setIconImage: used parameter for setting the image [BUGFIX]
 * 04.06.2009 - [JR] - setMenuBar: setParent for the menubar
 * 21.07.2009 - [JR] - dispose(): checked parent [BUGFIX]
 *                   - dispose: isDisposed checked
 * 13.08.2013 - [JR] - #756: changed set menu                   
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Component;
import java.beans.PropertyVetoException;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.util.SilentAbortException;
import javax.rad.util.TranslationMap;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.ext.JVxInternalFrame;

/**
 * The <code>SwingInternalFrame</code> is the <code>IInternalFrame</code>
 * implementation for swing.<br>
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JInternalFrame
 * @see IInternalFrame
 */
public class SwingInternalFrame extends SwingAbstractFrame<JVxInternalFrame> 
                                implements IInternalFrame,
                                		   InternalFrameListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingInternalFrame</code>.
	 * 
	 * @param pDesktopPanel the DesktopPanel
	 */
	public SwingInternalFrame(IDesktopPanel pDesktopPanel)
	{
		super(new JVxInternalFrame());
		
		resource.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		resource.setIconifiable(true);
		resource.setMaximizable(true);
		resource.setResizable(true);
		resource.setClosable(true);
		
		pDesktopPanel.add(this, 0);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    protected void addWindowListenerToResource()
    {
        resource.addInternalFrameListener(this);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// IInternalFrame
	
	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
	{
		return resource.getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
	{
		resource.setTitle(pTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIconImage(IImage pImage)
	{
    	if (pImage == null)
    	{
    		resource.setFrameIcon(null);
    	}
    	else
    	{
    		resource.setFrameIcon((ImageIcon)pImage.getResource());
    	}

		iconImage = pImage;
	}

	/**
	 * {@inheritDoc}
	 */
    public int getState() 
    {
    	if (resource.isIcon())
    	{
    		return ICONIFIED;
    	}
    	else if (resource.isMaximum())
    	{
    		return MAXIMIZED_BOTH;
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
    	try
    	{
    		resource.setIcon(pState == ICONIFIED);
    		resource.setMaximum(pState == MAXIMIZED_BOTH);
    	}
    	catch (Exception ex)
    	{
    		// Nothing to do
    	}
    }

	/**
	 * {@inheritDoc}
	 */
	public void setClosable(boolean pClosable)
	{
		resource.setClosable(pClosable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isClosable()
	{
		return resource.isClosable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIconifiable(boolean pIconifiable)
	{
		resource.setIconifiable(pIconifiable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isIconifiable()
	{
		return resource.isIconifiable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaximizable(boolean pMaximizable)
	{
		resource.setMaximizable(pMaximizable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximizable()
	{
		return resource.isMaximizable();
	}

	/**
	 * {@inheritDoc}
	 */
    public boolean isResizable()
    {
    	return resource.isResizable();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setResizable(boolean pResizable)
    {
    	resource.setResizable(pResizable);
    }

	/**
	 * {@inheritDoc}
	 */	
	public void pack()
	{
		resource.pack();
	}

	/**
	 * {@inheritDoc}
	 */	
	public void dispose()
	{
		if (!isDisposed())
		{
			super.dispose();
			
			// Ticket 757: component remove in applets is very slow.
			resource.setVisible(false);
			resource.dispose();
			
			IContainer con = getParent();
			
			if (con != null)
			{
				con.remove(this);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close()
	{
		try
		{
			//sends windowClosing
			resource.setClosed(true);
			
			if (resource.isDisposed())
			{
	            IContainer con = getParent();
	            
	            if (con != null)
	            {
	                con.remove(this);
	            }
			}
		}
		catch (PropertyVetoException pve)
		{
			//nothing to be done
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosed()
	{
		return resource.isClosed();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isActive()
	{
		return resource.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public void toFront()
	{
		resource.toFront();
	}

	/**
	 * {@inheritDoc}
	 */
	public void toBack()
	{
		resource.toBack();
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
				resource.setJMenuBar((JMenuBar)pMenuBar.getResource());
			}
			else
			{
				resource.setJMenuBar(null);
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
		if (pComponent != null)
		{
			resource.setLocationRelativeTo((Component)pComponent.getResource());
		}
		else
		{
			resource.setLocationRelativeTo(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setModal(boolean pModal)
    {
    	resource.setModal(pModal);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isModal()
    {
    	return resource.isModal();
    }
    	
	// InternalFrameListener

	/**
	 * {@inheritDoc}
	 */
	public void internalFrameOpened(InternalFrameEvent pEvent)
	{
    	if (eventWindowOpened != null)
    	{
    		eventWindowOpened.dispatchEvent(new UIWindowEvent(eventSource, 
    														  UIWindowEvent.WINDOW_OPENED,
    														  AwtFactory.getMostRecentEventTime(),
    														  AwtFactory.getCurrentModifiers()));
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void internalFrameClosing(InternalFrameEvent pEvent)
	{
    	if (eventWindowClosing != null)
    	{
    		try
    		{
	    		eventWindowClosing.dispatchEvent(new UIWindowEvent(eventSource, 
	    														   UIWindowEvent.WINDOW_CLOSING, 
	    														   AwtFactory.getMostRecentEventTime(),
	    														   AwtFactory.getCurrentModifiers()));
    		}
    		catch (SilentAbortException e)
    		{
    			// Prevent graphical glitches by catching the event. There are
    			// listener behind this, that has to be executed, to ensure
    			// a proper state in the gui.
				// The exception is already delegated to event handler in
    			// dispatch event.
    		}
    	}
	}

	/**
	 * Removes the internal frame from it's parent.
	 * 
	 * @param pEvent the triggering event 
	 */
	public void internalFrameClosed(InternalFrameEvent pEvent)
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
	public void internalFrameActivated(InternalFrameEvent pEvent)
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
	public void internalFrameDeactivated(InternalFrameEvent pEvent)
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
	public void internalFrameIconified(InternalFrameEvent pEvent)
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
	public void internalFrameDeiconified(InternalFrameEvent pEvent)
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
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTranslation(TranslationMap pTranslation)
    {
        resource.setTranslation(pTranslation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TranslationMap getTranslation()
    {
        return resource.getTranslation();
    }
	
}	// SwingInternalFrame
