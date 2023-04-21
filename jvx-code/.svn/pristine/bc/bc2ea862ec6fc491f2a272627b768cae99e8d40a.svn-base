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
 * 04.06.2009 - [JR] - setMenuBar: setParent for the menubar
 * 21.07.2009 - [JR] - dispose: isDisposed checked
 * 22.12.2010 - [JR] - setMenuBar: null check
 * 13.08.2013 - [JR] - #756: changed set menu
 * 17.09.2013 - [JR] - Memory.gc used
 */
package com.sibvisions.rad.ui.swing.impl.container;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.menu.IMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.awt.impl.AwtFactory;
import com.sibvisions.rad.ui.swing.ext.JVxDesktopPane;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.rad.ui.swing.impl.SwingImage;
import com.sibvisions.util.Memory;

/**
 * The <code>SwingFrame</code> is the <code>IFrame</code>
 * implementation for swing.<br>
 * I is a top-level window with a title and a border.
 * <p>
 * The size of the frame includes any area designated for the border.
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JFrame
 * @see javax.rad.ui.container.IFrame
 */
public class SwingFrame extends SwingAbstractFrame<JFrame> 
                        implements WindowListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class-members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The new root pane. */
	private JRootPane rootPane = new JRootPane();

	/** The modal desktop pane. */
	private JVxDesktopPane desktopModal = new JVxDesktopPane();

	/** The normalized bounds. */
	private Rectangle restoreBounds = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingFrame</code>.
	 */
	public SwingFrame()
	{
		super(new JFrame());

    	rootPane.setOpaque(false);
    	//keep the parents toolbarPanel!
    	//without we need a new ToolBarPanel and overwrite all addToolbar methods!
    	rootPane.setContentPane(resource.getContentPane());

    	desktopModal.add(rootPane);
    	
    	resource.setContentPane(desktopModal);
		resource.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addComponentListener();
		addWindowListener();
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void componentResized(ComponentEvent pEvent)
    {
        if (getState() == NORMAL)
        {
            restoreBounds = resource.getBounds();
        }
        
        super.componentResized(pEvent);
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void componentMoved(ComponentEvent pEvent)
    {
        if (getState() == NORMAL)
        {
            restoreBounds = resource.getBounds();
        }
        
        super.componentMoved(pEvent);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    protected void addWindowListenerToResource()
    {
        resource.addWindowListener(this);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
	{
		if (super.isVisible() != pVisible)
		{
			super.setVisible(pVisible);
			if (pVisible)
			{
				resource.getRootPane().repaint();
			}
		}
	}

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
	public void setIconImage(IImage pIconImage)
	{
		if (pIconImage == null)
		{
			resource.setIconImage(null);
		}
		else
		{
//			try
//			{
//				Method m = resource.getClass().getMethod("setIconImages", List.class);
//				
//				Image img = ((ImageIcon)pIconImage.getResource()).getImage();
//
//				List<Image> liImages = new ArrayList<Image>();
//				liImages.add(img);
//				liImages.add(ImageUtil.getScaledImage(img, 24, 24, true));
//				liImages.add(ImageUtil.getScaledImage(img, 20, 20, false));
//				liImages.add(ImageUtil.getScaledImage(img, 16, 16, false));
//				
//				m.invoke(resource, liImages);
//			}
//			catch (Exception e)
//			{
//				resource.setIconImage(((ImageIcon)pIconImage.getResource()).getImage());
//			}
			
			resource.setIconImage(((ImageIcon)pIconImage.getResource()).getImage());
		}

		iconImage = pIconImage;
	}

	/**
	 * {@inheritDoc}
	 */
    public int getState() 
    {
    	return resource.getExtendedState();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setState(int pState)
    {
    	resource.setExtendedState(pState);
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
			
			Memory.gc();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isActive()
	{
		return resource.isActive();
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
			connectMenuBar(pMenuBar);
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
	
    // WindowListener

    /**
     * {@inheritDoc}
     */
    public void windowOpened(WindowEvent pEvent)
    {
    	JVxUtil.setWindowWillBeActiveSoon(resource);
    	
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
    	JVxUtil.setWindowWillBeActiveSoon(resource);
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
     * Gets the bounds that will be used, when the frame is restored from maximized to normal size.
     * 
     * @return the restoreBounds
     */
    public Rectangle getRestoreBounds()
    {
        if (restoreBounds == null)
        {
            if (getState() == NORMAL)
            {
                restoreBounds = resource.getBounds();
            }
            else
            {
                Rectangle bounds = LauncherUtil.getDeviceBounds(resource.getGraphicsConfiguration());
                Dimension size =  resource.getPreferredSize();
                
                restoreBounds = new Rectangle(
                        bounds.x + (bounds.width - size.width) / 2, 
                        bounds.y + (bounds.height - size.height) / 2, 
                        size.width, size.height);
            }
        }
        
        return restoreBounds;
    }
    
	/**
	 * Returns the modal desktop pane.
	 * @return the modal desktop pane.
	 */
	protected JVxDesktopPane getModalDesktopPane()
	{
		return desktopModal;
	}

	/**
	 * Gets the internal root pane.
	 * 
	 * @return the internal root pane
	 */
	protected JRootPane getInternalRootPane()
	{
		return rootPane;
	}
	
    /**
     * Connects the menubar with the swing frame.
     * 
     * @param pMenuBar the menu bar
     */
    protected void connectMenuBar(IMenuBar pMenuBar)
	{
		boolean bMacBar = Boolean.getBoolean("apple.laf.useScreenMenuBar");
		
		if (pMenuBar != null)
		{
			if (SwingFactory.isMacLaF() && bMacBar)
			{
				((JMenuBar)pMenuBar.getResource()).setVisible(false);
			}
			
			resource.getRootPane().setJMenuBar((JMenuBar)pMenuBar.getResource());
		}
		else
		{
			if	(SwingFactory.isMacOS() && bMacBar)
			{
				resource.getRootPane().setJMenuBar(new JMenuBar());				
			}
			else
			{
				resource.getRootPane().setJMenuBar(null);
			}
		}
	}

}	// SwingFrame
