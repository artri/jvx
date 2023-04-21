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
 * 03.11.2008 - [JR] - switch between modal and not modal
 * 14.11.2008 - [JR] - setLocationRelativeTo: revalidateAll
 * 08.12.2008 - [JR] - removeNotify: handle default button
 *                   - dispose: checked icon state [BUGFIX]
 * 11.12.2008 - [JR] - cached focus component when openening a modal internal frame   
 *                   - setVisible: setVisible(true); setVisible(false); moved to the
 *                     end of the method. Otherwise modal frames change the selected
 *                     frame [BUGFIX]
 *                   - setSelectedIntern implemented (needed from JVxDesktopPane)
 *                   - focus handling
 *                     * cache previous component for modal frames
 *                     * set focus when the frame will become visible
 *                   - removeNotify: checked if rootPane is null [BUGFIX]
 *                   - store/restore/isFocusStored implemented
 * 18.12.2008 - [JR] - setIcon: set focus if the frame will be deiconified   
 * 05.02.2009 - [JR] - toBack, toFront overwritten -> set the selected state    
 * 17.02.2009 - [JR] - setModal: changed the z-order of the modal panel (the last modal frame is the top modal frame)
 * 22.02.2009 - [JR] - toFront, toBack now calls the desktopManager
 *                   - isModalPanelVisible implemented (needed for navigation keys in JVxDesktopPane)
 * 27.08.2009 - [JR] - toFront: NullPointerException when called after dispose was called [BUGFIX]
 * 05.02.2011 - [JR] - #276: setModalMode changed because we had problems with multiple modal frames (UI locked)
 * 11.02.2011 - [JR] - setInitialFocus: set the focus to the modal frame (intractable)
 * 03.03.2011 - [JR] - setModalMode: checked isShowing of parent and original container
 * 08.04.2011 - [JR] - #329: setModalMode - checked panModal != null   
 * 12.04.2011 - [JR] - #335: initModalLayer resizes the modal panel  
 * 06.07.2011 - [JR] - #417: modal frames are not iconifiable      
 * 22.07.2011 - [JR] - #417: reset iconifiable state only when it was changed automatically, otherwise it is possible
 *                     that a non resizable frame is iconifiable     
 *                   - #439: dispose: correct selection           
 * 01.04.2012 - [JR] - #570: update the resize-box on MacOS     
 * 03.08.2012 - [JR] - Memory leak prevention (frame cache, jdk 1.6; default button)     
 * 18.11.2013 - [JR] - #831: clearFramesCache moved to JVxDesktopPane
 * 22.01.2014 - [JR] - #919: isDisposed introduced     
 * 19.09.2017 - [JR] - #1824: isSelected now checks if frame instance is configured
 * 28.09.2017 - [JR] - #1827: MacOSX LaF border problem           
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyVetoException;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import javax.rad.util.TranslationMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.type.ImageUtil;

/**
 * The <code>JVxInternalFrame</code> extends a {@link JInternalFrame} to
 * support the modal mode and event queue blocking, like modal dialogs.
 * 
 * @author René Jahn
 */
public class JVxInternalFrame extends JInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the parent before the frame was added to the modal layer. */
	private Container conOriginal = null;
	
	/** the cache for previous focus components in modal mode. */
	private WeakReference<Component> wrModalPreviousFocus = null;

	/** the cache for the current focus component (used for Tab Mode). */
	private WeakReference<Component> wrInternFocus = null;
	
	/** the default button. */
	private WeakReference<JButton> wrDefaultButton = null;

	/** the translation mapping. */
	private TranslationMap translation;
	
	/** the root/content pane if disposed. */
	private JPanel panDisposedRoot;
	
	/** the modal option of the internal frame. */
	private boolean bModal = false;
	
	/** the blocking option of the internal frame. */
	private boolean bKeepBlocking = false;
	
	/** the mark for avoiding removeNotify handling for modal frames. */
	private boolean bIgnoreRemove = false;
	
	/** the mark for dispose method call. */
	private boolean bDisposed = false;
	
	/** whether the frame was iconifiable before is was modal. */
	private Boolean bOldIconifiable = null;
	
	/** synchronize object for blocking the events. */
	private static Object oBlockingSync = new Object();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** 
     * Creates a non-resizable, non-closable, non-maximizable,
     * non-iconifiable <code>JVxInternalFrame</code> with no title.
     */
    public JVxInternalFrame() 
    {
        this("");
    }

    /** 
     * Creates a non-resizable, non-closable, non-maximizable,
     * non-iconifiable <code>JVxInternalFrame</code> with the specified title.
     * Note that passing in a <code>null</code> <code>title</code> results in
     * unspecified behavior and possibly an exception.
     *
     * @param pTitle  the non-<code>null</code> <code>String</code>
     *     to display in the title bar
     */
    public JVxInternalFrame(String pTitle) 
    {
        super(pTitle);
        
        if (SwingFactory.isMacLaF())
        {
            //#1827
            Border border = getBorder();
            
            if (border != null && border instanceof CompoundBorder)
            {
                border = new CompoundUIBorder(((CompoundBorder)border).getOutsideBorder(), ((CompoundBorder)border).getInsideBorder())
                {
                    public void paintBorder(Component pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
                    {
                        int iX = pX;
                        int iY = pY;
                        int iWidth = pWidth;
                        int iHeight = pHeight;
                        
                        if (outsideBorder != null)
                        {
                            outsideBorder.paintBorder(pComponent, pGraphics, iX, iY, iWidth, iHeight);
                            
                            Insets ins = outsideBorder.getBorderInsets(pComponent);

                            iX += ins.left;
                            iY += ins.top;
                            iWidth = iWidth - ins.left - ins.right;
                            iHeight = iHeight - ins.top - ins.bottom;
                        }
                        
                        if (insideBorder != null)
                        {
                            Insets ins = insideBorder.getBorderInsets(pComponent);
                            
                            //simple fix for border paint problem
                            insideBorder.paintBorder(pComponent, pGraphics, iX, iY, iWidth, iHeight - ins.top - ins.bottom);
                        }
                    }
                };
            
                
                setBorder(border);
            }
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUI(InternalFrameUI pUi)
	{
		super.setUI(pUi);
		
		if (pUi instanceof BasicInternalFrameUI)
		{
			//change layout because the size calculation is wrong!
			setRootPaneCheckingEnabled(false);
			setLayout(new InternalLayout((BasicInternalFrameUI)pUi));
			setRootPaneCheckingEnabled(true);
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
    {
        // Stop animation thread, if component is not showing anymore.
        return isShowing() && super.imageUpdate(img, infoflags, x, y, w, h);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(Color pColor)
	{
		if (isRootPaneCheckingEnabled() && getContentPane() != null)  // Prevent NPE in Synthetica
		{
			getContentPane().setBackground(pColor);
		}
		else
		{
			super.setBackground(pColor);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pack()
	{
		super.pack();
		
		//#320
		// Workaround for resizing layouts depending of the initial size.
		Container cont = getContentPane().getParent();
		int index = cont.getComponentZOrder(getContentPane());
		cont.remove(getContentPane());
		cont.add(getContentPane(), index);
		super.pack();
	}
	
	/**
	 * Makes the component visible or invisible.<br>
	 * It also handles the modal and blocking options, if set. 
	 * 
	 * @param pVisible <code>true</code> to make the component visible; 
	 *                 <code>false</code> to make it invisible
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		if (getClientProperty("temporaryVisibility") != null)
		{
			super.setVisible(pVisible);
			return;
		}
		
		if (pVisible && isModal() && wrModalPreviousFocus == null)
		{
			Component comFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();

			if (comFocus != null)
			{
				wrModalPreviousFocus = new WeakReference<Component>(comFocus);
			}
		}
		
		boolean bDelegated = false;
		
		Container conParent = getParent();
		
		if (conParent instanceof JDesktopPane)
		{
			Container cont = getDesktopPane();
			
			//try to delegate to the parent!
			if (cont instanceof JVxDesktopPane)
			{
				//maybe tab handling
				bDelegated = ((JVxDesktopPane)cont).setVisible(this, pVisible);
			}
			else
			{
				bDelegated = false;
			}
		}

		//If the desktop pane doesn't handle the visibility of frames -> use
		//the standard mechanism
		if (!bDelegated)
		{
			setModalMode(pVisible);			

			super.setVisible(pVisible);
	
			if (pVisible)
			{
				if (isDisplayable())
				{
					setInitialFocus();
				}
			}
			else
			{
				if (conOriginal != null)
				{
					conOriginal.requestFocusInWindow();
				}
				else if (conParent != null)
				{
					//invisible -> set to the parent to receive input events
					//needed to send events
					conParent.requestFocusInWindow();
				}
			}

			if (pVisible)
			{
				startBlocking();
			}
			else
			{
				stopBlocking();
			}
		}
		else
		{
			//set the focus (sometimes it will not be set through the API)
			KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle(this);
		}

		if (pVisible && !isModal())
		{
			SwingUtilities.invokeLater
			(
				new Runnable()
				{
					public void run()
					{
						updateResizeBox();						
					}
				}
			);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Component pComponent)
	{
		boolean bRestoreDefault = true;
		
		if (getRootPane() != null)
		{		
			JButton butDefault = getRootPane().getDefaultButton();
			
			if (butDefault != null)
			{
				wrDefaultButton = new WeakReference<JButton>(butDefault);
				
				//when the button is a child of this frame, don't
				//set it as default button
				Container conParent = butDefault.getParent(); 
				
				while (conParent != null && bRestoreDefault)
				{
					if (conParent == this)
					{
						bRestoreDefault = false;
					}
					
					conParent = conParent.getParent(); 
				}
			}
		}
		else
		{
			wrDefaultButton = null;
		}
		
		super.remove(pComponent);
		
		if (bRestoreDefault)
		{
			restoreDefaultButton();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		
		restoreDefaultButton();
	}
	
	/**
	 * Notifies this component that it no longer has a parent component. 
	 * When this method is invoked, any KeyboardActions set up in the the 
	 * chain of parent components are removed.<br>
	 * It also handles the modal and blocking option, if set. 
	 */
	@Override
	public void removeNotify()
	{
		if (!bIgnoreRemove)
		{
			setModalMode(false);

			Container cont = getDesktopPane();

			//try to delegate to the parent!
			if (cont instanceof JVxDesktopPane)
			{
				((JVxDesktopPane)cont).dispose(this);
			}		

			super.removeNotify();

			stopBlocking();
		}
		else
		{
			super.removeNotify();
		}
	}

    /**
     * Makes this internal frame:
     * <ul>
     *   <li>invisible</li>
     *   <li>unselected</li>
     *   <li>closed</li>
     * </ul>
     * If the frame is not already closed, this method fires an
     * <code>INTERNAL_FRAME_CLOSED</code> event. The results of 
     * invoking this method are similar to <code>setClosed(true)</code>,
     * but <code>dispose</code> always succeeds in closing the 
     * internal frame and does not fire an <code>INTERNAL_FRAME_CLOSING</code> 
     * event.<br>
     * It also handles the modal and blocking option, if set.
     *
     * @see javax.swing.event.InternalFrameEvent#INTERNAL_FRAME_CLOSED
     * @see #setVisible
     * @see #setSelected
     * @see #setClosed
     */
	@Override
	public void dispose()
	{
		if (!bDisposed)
		{
			//avoid recursive calls
			bDisposed = true;

			setModalMode(false);

			Container con;
			
			if (isIcon())
			{
				con = getDesktopIcon().getParent();
			}
			else
			{
				con = getParent();
			}
	
			final JDesktopPane dpane = getDesktopPane();
			
			//important to set the active frame
			if (dpane != null)
			{
				dpane.getDesktopManager().closeFrame(this);
			}
			
			//try to delegate to the parent!
			if (dpane instanceof JVxDesktopPane)
			{
				((JVxDesktopPane)dpane).dispose(this);
			}		
			
			if (con != null)
			{
    			if (isIcon())
    			{
    				con.remove(getDesktopIcon());
    			}
    			else
    			{
    				//ensure that the frame will be removed from the desktop!
    				con.remove(this);
    			}
			}
			
	    	//remove "references" (weak)
	    	wrInternFocus = null;

	    	super.dispose();
	    	
			//remove reference (avoids memory leak)!
	    	if (dpane != null && dpane.getSelectedFrame() == this)
	    	{
	    		dpane.setSelectedFrame(null);
	    	}

			stopBlocking();

			panDisposedRoot = new JPanel();
			
			//try to avoid references (gc tuning)
			setContentPane(panDisposedRoot);
			setJMenuBar(null);
			setRootPane(null);
			setBorder(null);
						
			//remove references (memory leak prevention)
			conOriginal = null;
			
			//clear default button cache after dispose
			wrDefaultButton = null;
			
			//unsets lastFocusOwner member (see getMostRecentFocusOwner)
			super.restoreSubcomponentFocus();
			
			InternalFrameListener[] listeners = getInternalFrameListeners();
			
			if (listeners != null)
			{
			    for (InternalFrameListener listener : listeners)
			    {
			        //remove references
			        removeInternalFrameListener(listener);
			    }
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getMostRecentFocusOwner()
	{
	    if (bDisposed)
	    {
	        //we have to override getContentPane()
	        return null;
	    }
	    
	    return super.getMostRecentFocusOwner();
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Container getContentPane()
	{
	    if (bDisposed)
	    {
	        return panDisposedRoot;
	    }
	    
	    return super.getContentPane();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelected()
	{
	    //guarantees that constructor is done (see #1824)
	    if (desktopIcon != null)
	    {
    		Container cont = getDesktopPane();
    
    		if (cont instanceof JVxDesktopPane && ((JVxDesktopPane)cont).isTabMode())
    		{
    			return ((JVxDesktopPane)cont).isSelected(this);
    		}
	    }
	    
		return super.isSelected();
	}
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelected(boolean pSelected) throws PropertyVetoException
	{
		//not possible to select a hidden frame
		if (pSelected && !isVisible())
		{
			return;
		}
		
		if (isSelected() == pSelected)
		{
			return; 
		}

		boolean bDelegated;
		
		Container cont = getDesktopPane();
		
		//try to delegate to the parent!
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			bDelegated = ((JVxDesktopPane)cont).setSelected(this, pSelected);
		}
		else
		{
			bDelegated = false;
		}

		//If the desktop pane doesn't handle the selection of frames -> use
		//the standard mechanism
		if (!bDelegated)
		{
			super.setSelected(pSelected);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnabled)
	{
		Container cont = getDesktopPane();
		
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			((JVxDesktopPane)cont).setEnabled(this, pEnabled);
		}
		
		super.setEnabled(pEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResizable(boolean pResizable)
	{
		Container cont = getDesktopPane();
		
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			((JVxDesktopPane)cont).setResizable(this, pResizable);
		}

		super.setResizable(pResizable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFrameIcon(Icon pIcon)
	{
		Container cont = getDesktopPane();
		
		if (pIcon != null && pIcon.getIconHeight() > 16)
        {
		    pIcon = ImageUtil.getScaledIcon(pIcon, pIcon.getIconWidth(), 16, true);
        }
		
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			((JVxDesktopPane)cont).setIcon(this, pIcon);
		}

		super.setFrameIcon(pIcon);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String pTitle)
	{
		Container cont = getDesktopPane();
		
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			((JVxDesktopPane)cont).setTitle(this, pTitle);
		}

		super.setTitle(pTitle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClosable(boolean pClosable)
	{
		Container cont = getDesktopPane();
		
		if (cont instanceof JVxDesktopPane)
		{
			//maybe tab handling
			((JVxDesktopPane)cont).setClosable(this, pClosable);
		}

		super.setClosable(pClosable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIcon(boolean pIcon) throws PropertyVetoException
	{
		if (isVisible())
		{
			super.setIcon(pIcon);
			
			if (!pIcon)
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle(this);
			}
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setRootPane(JRootPane pContainer)
	{
		super.setRootPane(pContainer);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void toFront()
	{
		//try to delegate to the desktop pane
		JDesktopPane pane = getDesktopPane();

		if (pane == null)
		{
			super.toFront();
		}
		else
		{
			if (!(pane instanceof JVxDesktopPane) || !((JVxDesktopPane)pane).toFront(this))
			{
				if (isIcon())
				{
					try
					{
						setIcon(false);
					}
					catch (PropertyVetoException pve)
					{
						//nothing to be done!
					}
				}
				
				super.toFront();
				
				getDesktopPane().getDesktopManager().activateFrame(this);
				
				setInitialFocus();
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void toBack()
	{
		getDesktopPane().getDesktopManager().deactivateFrame(this);

		super.toBack();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
    public void restoreSubcomponentFocus() 
    {
		// We have to ensure, that in all open/ close of an InternalFrame does not request whole window focus.
		// This behaviour can be reproduced with JDK 1.7.0_51.
		// deactivating the focusable window state solves the problem.
		// Preventing requestFocus by not calling super.restoreSubcomponentFocus() does not help, as
		// there seams to be native focus calls.
    	Component window = this;
        while (window != null && !(window instanceof Window))
        {
            window = window.getParent();
        }
    	final Window w = (Window)window;
        boolean notActive = w != null && !w.isActive() && w != JVxUtil.getWindowWillBeActiveSoon();
        
        if (notActive)
        {
        	w.setFocusableWindowState(false);
        }
    	super.restoreSubcomponentFocus();
        if (notActive)
        {
        	SwingUtilities.invokeLater(new Runnable()
        	{
        		public void run()
        		{
        			w.setFocusableWindowState(true);
        		}
        	});
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the previous focus owner. This is available in case of a modal dialog,
	 * otherwise it is null.
	 * 
	 * @return the previous focus owner
	 */
	public Component getPreviousFocusOwner()
	{
		if (wrModalPreviousFocus == null)
		{
			return null;
		}
		else
		{
			return wrModalPreviousFocus.get();
		}
	}
	
	/**
	 * Sets the focus to this internal frame and the first focusable component in the
	 * frame. It handles modal states as expected.
	 */
	private void setInitialFocus()
	{
		JPanel panModal = getModalLayerPanel(getModalDesktopPane());
		if (panModal == null || !panModal.isVisible())
		{
			//NOT modal: give the frame a chance to set its own focus
			if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentFocusCycleRoot() != this)
			{
				JVxInternalFrame.this.requestFocusInWindow();
				
				KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle(JVxInternalFrame.this);
			}	
		}
		else if (panModal != null && panModal.isVisible() && isModal())
		{
			//MODAL: strict mode -> we need the focus otherwise modality is not working because the focus is "under" 
			//                      the modal layer
			
			// e.g. Login Screen.
			// set the focus (sometimes it will not be set through the API)
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					ThreadHandler.start(new Runnable()
					{
						public void run()
						{
							int i = 0;
							while (!ThreadHandler.isStopped() && i <= 5)
							{
								i++;
								
								if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentFocusCycleRoot() != JVxInternalFrame.this)
								{
									try
									{
										JVxInternalFrame.this.requestFocusInWindow();
										KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle(JVxInternalFrame.this);
									}
									catch (Exception e)
									{
										//sometimes this is possible
									}
								}
								
								try
								{
									Thread.sleep(10);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					});
				}
			});
		}
	}
	
	/**
	 * Gets the {@link JDesktopPane} of the highest level from the internal
	 * frame parents.
	 * 
	 * @return the top level JDesktopPane or null if the internal frame or it's
	 *         parent(s) was not added to a {@link JDesktopPane}
	 */
	private JDesktopPane getModalDesktopPane()
	{
		Container com = this;
		
		JDesktopPane desktop = null;

		//needed for switching between modal an not modal
		if (conOriginal == null)
		{
			conOriginal = com.getParent();
		}
		
		while ((com = com.getParent()) != null)
		{
			if (com instanceof JDesktopPane)
			{
				desktop = (JDesktopPane)com;
			}
		}
		
		return desktop;
	}
	
	/**
	 * Initializes the panel for blocking the mouse events to all underlaying
	 * lightweight components. The panel will be added to the main desktop pane
	 * and is invisible until a modal frame will be used.
	 * 
	 * @param pDesktopPane the desktop pane, where to init.
	 * @return the modal layer panel
	 */
	private static JPanel initModalLayerPanel(JDesktopPane pDesktopPane)
	{
		JPanel panModal = getModalLayerPanel(pDesktopPane);
		
		if (pDesktopPane != null && panModal == null)
		{
			panModal = new JPanel(null);
			//mark for modal glass panel
			panModal.putClientProperty("_ignore_", Boolean.TRUE);
			panModal.putClientProperty("_modal_glass_", Boolean.TRUE);
			panModal.setOpaque(false);
			//panModal.setBackground(Color.orange);
			panModal.setVisible(false);
			panModal.setBounds(0, 0, pDesktopPane.getWidth(), pDesktopPane.getHeight());
			
			ModalMouseHandler mmh = new ModalMouseHandler();
			
			panModal.addMouseListener(mmh);
			panModal.addMouseWheelListener(mmh);
			panModal.addMouseMotionListener(mmh);
			
			pDesktopPane.add(panModal, JLayeredPane.MODAL_LAYER);
			pDesktopPane.addComponentListener(new ModalComponentListener());
			pDesktopPane.putClientProperty("modalLayerPanel", panModal);
		}
		
		return panModal;
	}
	
	/**
	 * Gets the modal layer panel for the desktop pane.
	 * 
	 * @param pDesktopPane the desktop pane
	 * @return the modal layer panel
	 */
	private static JPanel getModalLayerPanel(JDesktopPane pDesktopPane)
	{
		if (pDesktopPane != null)
		{
			return (JPanel)pDesktopPane.getClientProperty("modalLayerPanel");
		}
		
		return null;
	}
	
	/**
	 * Gets the modal layer panel for the desktop pane.
	 * 
	 * @param pDesktopPane the desktop pane
	 * @return true, if the modal layer panel is visible.
	 */
	static boolean isModalLayerPanelVisible(JDesktopPane pDesktopPane)
	{
		JPanel panModal = getModalLayerPanel(pDesktopPane);
		
		return panModal != null && panModal.isVisible();
	}
	
	/**
	 * Starts or stops the modal mode.  
	 * 
	 * @param pModal <code>true</code> to show the internal frame modal; <code>false</code> otherwise
	 */
	private void setModalMode(boolean pModal)
	{
		if (isModal())
		{
			JPanel panModal = null;
			
			JDesktopPane desktop = getModalDesktopPane();
			
			//set the size of the parent -> no resizing necessary
			if (pModal)
			{
				Container parent = getParent();

				//if the frame is already in the modal layer -> nothing to do
				if (desktop != null)
				{
					panModal = initModalLayerPanel(desktop);
					
					if (parent != desktop)
					{
						bIgnoreRemove = true;
						
						try
						{
							parent.remove(this);
							desktop.add(this, JLayeredPane.MODAL_LAYER);

							updateResizeBox();
						}
						finally
						{
							bIgnoreRemove = false;
						}
						
						Dimension dimParent = desktop.getSize();
						
						panModal.setBounds(0, 0, dimParent.width, dimParent.height);
						
						Point parentPos = getInternalLocationOnScreen(parent);
						Point desktopPos = getInternalLocationOnScreen(desktop);
						Point pos = getLocation();
						
						setLocation(pos.x + parentPos.x - desktopPos.x, pos.y + parentPos.y - desktopPos.y);
					}
					
					if (panModal != null)
					{
						panModal.setVisible(true);
						
						//always "on top" (support for more than one frame in the modal layer, but it's not possible
						//to switch between the frames)
						panModal.getParent().setComponentZOrder(panModal, 0);
					}
					
				}

				//move in front of the modal panel!!!
				if (isVisible())
				{
					toFront();
				}
			}
			else 
			{	
				panModal = getModalLayerPanel(desktop);
				
				if (panModal != null)
				{
					int iVisibleCount = 0;
					
					Component[] compModal = ((JDesktopPane)panModal.getParent()).getComponentsInLayer(JLayeredPane.MODAL_LAYER.intValue());
					Component comp;
					
					for (int i = 0, anz = compModal.length; i < anz; i++)
					{
						comp = compModal[i];
	
						if (comp != panModal && comp != this && comp.isVisible())
						{
							if (comp instanceof JComponent)
							{
								if (((JComponent)comp).getClientProperty("_ignore_") != Boolean.TRUE)
								{
									iVisibleCount++;
								}
							}
							else
							{
								iVisibleCount++;
							}
						}
					}
	
					//it's possible to use more than one non blocking modal frame.
					//Remove the blocking pane only when there are no more modal frames!
					panModal.setVisible(iVisibleCount > 0);
	
					if (iVisibleCount > 0)
					{
						//always "on top" (support for more than one frame in the modal layer, but it's not possible
						//to switch between the frames)
						panModal.getParent().setComponentZOrder(panModal, 0);
						
						//bring another frame to front (otherwise it is possible that we lock us
						JInternalFrame[] frames = ((JDesktopPane)panModal.getParent()).getAllFrames();
	
						for (int i = 0; i < frames.length; i++)
						{
							if (frames[i] != this && ((JVxInternalFrame)frames[i]).isModal() && frames[i].getParent() != null)
							{
								frames[i].toFront();
								i = frames.length;
							}
						}
					}
				}
			}
			
			//If screenmenu is in use, we should handle modal mode correctly (-> don't allow menu bar)
			if (panModal != null && SwingFactory.isMacOS() && Boolean.getBoolean("apple.laf.useScreenMenuBar"))
			{
				JFrame frame = getTopFrame();
				
				if (frame != null)
				{
					JRootPane rootPane = frame.getRootPane();

					if (rootPane != null)
					{							
						if (panModal.isVisible())
						{
							if (rootPane.getClientProperty("modal.JMenuBar.components") == null)
							{
								JMenuBar mbar = rootPane.getJMenuBar();
								
								if (mbar != null)
								{
									rootPane.putClientProperty("modal.JMenuBar.components", mbar.getComponents());

									mbar.removeAll();
								}
							}
						}
						else
						{
							Component[] comps = (Component[])rootPane.getClientProperty("modal.JMenuBar.components");
							
							if (comps != null)
							{
								rootPane.putClientProperty("modal.JMenuBar.components", null);
								
								JMenuBar mbar = rootPane.getJMenuBar();

								if (mbar != null)
								{
									if (mbar.getComponentCount() == 0)
									{
										for (Component comp : comps)
										{
											mbar.add(comp);
										}
									}
								}
							}
						}
					}
				}
			}
			// disable embedded menu in flat laf.
			else if (panModal != null && SwingFactory.isFlatLaF()) 
			{
                JFrame frame = getTopFrame();
                
                if (frame != null && !frame.isUndecorated()
                        && (System.getProperty("flatlaf.menuBarEmbedded") == null || Boolean.getBoolean("flatlaf.menuBarEmbedded")))
                {
                    JRootPane rootPane = frame.getRootPane();

                    if (rootPane != null)
                    {                           
                        JMenuBar menuBar = rootPane.getJMenuBar();
                        
                        if (menuBar != null)
                        {
                            List<Component> disabledComponents = (List<Component>)menuBar.getClientProperty("modal.JMenuBar.disabledComponents");
                            
                            if (!panModal.isVisible() && disabledComponents != null)
                            {
                                menuBar.putClientProperty("modal.JMenuBar.disabledComponents", null);
                                
                                for (Component comp : disabledComponents)
                                {
                                    comp.setEnabled(true);
                                }
                            }
                            else if (panModal.isVisible() && disabledComponents == null)
                            {
                                disabledComponents = new ArrayList<Component>();
                                menuBar.putClientProperty("modal.JMenuBar.disabledComponents", disabledComponents);
    
                                for (int i = 0, count = menuBar.getComponentCount(); i < count; i++)
                                {
                                    Component comp = menuBar.getComponent(i);
                                    
                                    if (comp.isEnabled())
                                    {
                                        disabledComponents.add(comp);
                                        
                                        comp.setEnabled(false);
                                    }
                                }
                            }
                        }
                    }
                }
			}
		}
	}
	
	/**
	 * Starts blocking the eventqueue while the internal frame is visible.
	 */
	private void startBlocking()
	{
		if (isKeepBlocking())
		{
			AccessController.doPrivileged(new PrivilegedAction()
			{
				public Object run()
				{
			        try 
			        {
			            if (SwingUtilities.isEventDispatchThread()) 
			            {
			                EventQueue theQueue = getToolkit().getSystemEventQueue();

			                while (isVisible()) 
			                {
			                    AWTEvent event = theQueue.getNextEvent();
			                    Object source = event.getSource();
			                    
			                    if (event instanceof ActiveEvent) 
			                    {
			                        ((ActiveEvent)event).dispatch();
			                    } 
			                    else if (source instanceof Component) 
			                    {
			                        ((Component)source).dispatchEvent(event);
			                    } 
			                    else if (source instanceof MenuComponent) 
			                    {
			                        ((MenuComponent)source).dispatchEvent(event);
			                    } 
			                    else 
			                    {
			                        System.err.println("Unable to dispatch: " + event);
			                    }
			                }
			            } 
			            else 
			            {
			                while (isVisible()) 
			                {
			                	synchronized(oBlockingSync)
			                	{
			                		oBlockingSync.wait();
			                	}
			                }
			            }
			        }
			        catch (SecurityException se)
			        {
			        	se.printStackTrace();
			        }
			        catch (InterruptedException ie) 
			        {
			        	//ignore this
			        }

			        return null;
				}
			});
		}
	}
	
	/**
	 * Stops blocking the event queue.
	 */
	private void stopBlocking()
	{
		if (isKeepBlocking())
		{
			synchronized(oBlockingSync)
			{
				oBlockingSync.notifyAll();
			}
		}
	}
	
    /**
     * Sets the frame as modal frame. When a modal frame is visible
     * it's not possible to use the underlaying components.
     * 
     * @param pModal <code>true</code> to set this internal frame modal, 
     *               otherwise <code>false</code>
     */
	public void setModal(boolean pModal)
	{
		boolean bOldModal = bModal;

		
		//when switching from modal to non modal mode -> change the layer for
		//the frame, because it's added in the modal desktop pane!
		if (!pModal && bOldModal && conOriginal != null)
		{
			boolean bIsSelected = isSelected;
			
			setModalMode(false);
			
			//remove selection from the desktopManager, otherwise
			//the manager has a reference and deselects the frame
			//if a new modal frame will be visible!
			if (bIsSelected)
			{
				try
				{
					setSelected(false);
				}
				catch (PropertyVetoException pve)
				{
					//do nothing
				}
			}
			
			bIgnoreRemove = true;
			
			try
			{
				Container parent = getParent();
				
				parent.remove(this);
				conOriginal.add(this);
				
				Point parentPos = getInternalLocationOnScreen(parent);
				Point originalPos = getInternalLocationOnScreen(conOriginal);
				Point pos = getLocation();
				
				setLocation(pos.x + parentPos.x - originalPos.x, pos.y + parentPos.y - originalPos.y);
				
				updateResizeBox();
			}
			finally
			{
				bIgnoreRemove = false;
			}

			//see above comment!
			if (bIsSelected)
			{
				try
				{
					setSelected(true);
				}
				catch (PropertyVetoException pve)
				{
					//do nothing
				}
			}
			
			conOriginal = null;
		}
		
		bModal = pModal;
		
		if (bModal)
		{
			bOldIconifiable = Boolean.valueOf(isIconifiable());
			
			setIconifiable(false);
		}
		else if (bOldIconifiable != null)
		{
			setIconifiable(bOldIconifiable.booleanValue());
		}
		
		//only relevant for Smart/LF
		putClientProperty("JVxInternalFrame.intern_modal", Boolean.valueOf(pModal));
	}
	
    /**
     * Returns the modal option of the internal frame.
     * 
     * @return <code>true</code> if the frame is modal otherwise <code>false</code>
     */
	public boolean isModal()
	{
		return bModal;
	}
    
    /**
     * Sets the modal internal frame blocking. If a modal internal frame 
     * is blocking, the mouse events will be blocked for all lightweight 
     * children except for the modal frame.
     * 
     * @param pKeepBlocking <code>true</code> to block all mouse events for all
     *                      lightweight children, otherwise <code>false</code>
     */
    public void setKeepBlocking(boolean pKeepBlocking)
    {
    	bKeepBlocking = pKeepBlocking;
    }
    
    /**
     * Returns the blocking option of the internal frame.
     * 
     * @return <code>true</code> if the internal frame uses mouse event blocking
     */
    public boolean isKeepBlocking()
    {
    	return bKeepBlocking;
    }

    /**
     * Gets the location on screen.
     * @param pComponent the component
     * @return the location on screen.
     */
    private static Point getInternalLocationOnScreen(Component pComponent)
    {
    	Point point = new Point();
    	while (pComponent != null)
    	{
    		point.x += pComponent.getX();
    		point.y += pComponent.getY();
    		
    		pComponent = pComponent.getParent();
    	}
    	
    	return point;
    }
    
    /**
     * Sets and calculates the position of this internal frame relative to
     * another component. The other component should be inside the same desktop
     * pane.
     * 
     * @param pComponent the component to calculate the position
     */
	public void setLocationRelativeTo(Component pComponent)
	{
		if (pComponent == null || pComponent.getWidth() <= 0 || pComponent.getHeight() <= 0)
		{
			//don't move the frame!
			return;
		}
		else
		{
			//All components should have the proper size
			JVxUtil.revalidateAll(this);

			Container parent = getParent();
			
			Point parentPos = getInternalLocationOnScreen(parent);
			Dimension size = getSize();
			
			Point componentPos = getInternalLocationOnScreen(pComponent);
			Dimension componentSize = pComponent.getSize();
			
			setLocation(componentPos.x - parentPos.x + (componentSize.width - size.width) / 2,
					    componentPos.y - parentPos.y + (componentSize.height - size.height) / 2);
		}
	}
	
	/**
	 * Stores the current focus owner in an internal component.
	 */
	public void storeFocus()
	{
		Component comFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		
		if (comFocus != null)
		{
			wrInternFocus = new WeakReference(comFocus);
		}
	}
	
	/**
	 * Checks if the frame has stored a focus component.
	 * 
	 * @return <code>true</code> if the frame has stored an available focus component
	 */
	public boolean isFocusStored()
	{
		return wrInternFocus != null && wrInternFocus.get() != null;
	}
	
	/**
	 * Sets the focus to an already stored component or does nothing if no focus
	 * component is stored.
	 */
	public void restoreFocus()
	{
		if (wrInternFocus != null)
		{
			Component com = wrInternFocus.get();
			
			if (com != null)
			{
				com.requestFocusInWindow();
			}
			
			wrInternFocus = null;
		}
	}
	
	/**
	 * Sets the visibility of this frame without checks.
	 * 
	 * @param pVisible <code>true</code> to set the frame visible, <code>false</code> to set it invisible
	 */
	void setVisibleIntern(boolean pVisible)
	{
		super.setVisible(pVisible);
	}
	
	/**
	 * Updates the resize box on MacOS.
	 */
	private void updateResizeBox()
	{
		if (SwingFactory.isMacLaF())
		{
			if (getRootPane() != null)
			{
				ComponentListener[] comp = getLayeredPane().getComponentListeners();
				
				for (int i = 0; i < comp.length; i++)
				{
					if (comp[i] instanceof JLabel)
					{
						comp[i].componentResized(new ComponentEvent(((JLabel)comp[i]), ComponentEvent.COMPONENT_RESIZED));
					}
				}
			}
		}
	}
	
	/**
	 * Restores the default button.
	 */
	private void restoreDefaultButton()
	{
		if (wrDefaultButton != null && getRootPane() != null)
		{
			JButton but = wrDefaultButton.get();

			if (but != null)
			{
				getRootPane().setDefaultButton(but);
			}
		}
	}
    
	/**
	 * Gets whether this frame was disposed.
	 * 
	 * @return <code>true</code> if this frame was disposed, <code>false</code> otherwise
	 */
	public boolean isDisposed()
	{
		return bDisposed;
	}
	
	/**
	 * Sets the translation mapping.
	 * 
	 * @param pTranslation the translation mapping
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
	    translation = pTranslation;
	}
	
	/**
	 * Gets the translation mapping.
	 * 
	 * @return the translation mapping
	 */
	public TranslationMap getTranslation()
	{
	    return translation;
	}
	
	/**
	 * Gets the top {@link JFrame}.
	 * 
	 * @return the top frame
	 */
	protected JFrame getTopFrame()
	{
		JFrame frame = null;
		Container com = this;
		
		while ((com = com.getParent()) != null)
		{
			if (com instanceof JFrame)
			{
				frame = (JFrame)com;
			}
		}
		
		return frame;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * The <code>ModalMouseHandler</code> is an empty {@link MouseListener} and
     * {@link MouseMotionListener} implementation for the modal panel.
     * 
     * @author René Jahn
     */
	static final class ModalMouseHandler implements MouseListener,
    												MouseMotionListener,
    												MouseWheelListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// MouseListener

	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseClicked(MouseEvent pEvent) 
	    {
	    }
	    
	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mousePressed(MouseEvent pEvent) 
	    {
	    }
	    
	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseReleased(MouseEvent pEvent)
	    {
	    }

	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseEntered(MouseEvent pEvent)
	    {
	    }

	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseExited(MouseEvent pEvent)
	    {
	    }

	    // MouseMotionListener

	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseDragged(MouseEvent pEvent)
	    {
	    }
	    
	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseMoved(MouseEvent pEvent) 
	    {
	    }
	    
	    //MouseWheelListener
	    
	    /**
	     * Not implemented.
	     * 
	     * @param pEvent {@inheritDoc}
	     */
	    public void mouseWheelMoved(MouseWheelEvent pEvent)
	    {
	    }
		
	}	// ModalMouseHandler
	
	/**
	 * The <code>InternalLayout</code> fixes the calculation of the preferred size
	 * for the root pane.
	 * 
	 * @author René Jahn
	 */
	private class InternalLayout implements LayoutManager
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the reference to the current UI. */
		private BasicInternalFrameUI ui;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>InternalLayout</code> for the current
		 * 
		 * cached UI.
		 * 
		 * @param pUi the UI of the internal frame
		 */
		protected InternalLayout(BasicInternalFrameUI pUi)
		{
			ui = pUi;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
        public void addLayoutComponent(String pName, Component pComponent) 
        {
        }
        
		/**
		 * {@inheritDoc}
		 */
        public void removeLayoutComponent(Component pComponent) 
        {
        }
        
		/**
		 * {@inheritDoc}
		 */
        public Dimension preferredLayoutSize(Container pComponent)  
        {
            Dimension dimSize;
            
            Insets insets = getInsets();
        
            
            JRootPane root = getRootPane();
            
            if (root == null)
            {
            	return new Dimension(0, 0);
            }
            
            dimSize = new Dimension(root.getPreferredSize());
            dimSize.width += insets.left + insets.right;
            dimSize.height += insets.top + insets.bottom;

            if (ui.getNorthPane() != null) 
            {
                Dimension dim = ui.getNorthPane().getPreferredSize();

                //BUGFIX (missing in original frame) 
                if (UIManager.getBoolean("InternalFrame.layoutTitlePaneAtOrigin"))
                {
                	dimSize.height -= insets.top;
                }

                dimSize.width = Math.max(dim.width, dimSize.width);
                dimSize.height += dim.height;
            }

            if (ui.getSouthPane() != null) 
            {
                Dimension dim = ui.getSouthPane().getPreferredSize();
                
                dimSize.width = Math.max(dim.width, dimSize.width);
                dimSize.height += dim.height;
            }

            if (ui.getEastPane() != null) 
            {
                Dimension dim = ui.getEastPane().getPreferredSize();
                
                dimSize.width += dim.width;
                dimSize.height = Math.max(dim.height, dimSize.height);
            }

            if (ui.getWestPane() != null) 
            {
                Dimension dim = ui.getWestPane().getPreferredSize();
                
                dimSize.width += dim.width;
                dimSize.height = Math.max(dim.height, dimSize.height);
            }
            
            return dimSize;
        }
    
		/**
		 * {@inheritDoc}
		 */
        public Dimension minimumLayoutSize(Container pComponent) 
        {
            // The minimum size of the internal frame only takes into
            // account the title pane since you are allowed to resize
            // the frames to the point where just the title pane is visible.
            Dimension dimResult = new Dimension();
            
            if (ui.getNorthPane() != null 
            	&& ui.getNorthPane() instanceof BasicInternalFrameTitlePane) 
            {
            	dimResult = new Dimension(ui.getNorthPane().getMinimumSize());
            }
            
            Insets insets = getInsets();
            
            dimResult.width += insets.left + insets.right;
            dimResult.height += insets.top + insets.bottom;
        
            return dimResult;
        }
    
		/**
		 * {@inheritDoc}
		 */
        public void layoutContainer(Container pContainer) 
        {
            Insets insets = getInsets();
            
            int iX = insets.left;
            int iY = insets.top;
            int iWidth  = getWidth() - insets.left - insets.right;
            int iHeight = getHeight() - insets.top - insets.bottom;
            
            if (ui.getNorthPane() != null) 
            {
                Dimension dim = ui.getNorthPane().getPreferredSize();
                
                if (UIManager.getBoolean("InternalFrame.layoutTitlePaneAtOrigin"))
                {
                    iY = 0;
                    iHeight += insets.top;
                    ui.getNorthPane().setBounds(0, 0, getWidth(), dim.height);
                }
                else 
                {
                    ui.getNorthPane().setBounds(iX, iY, iWidth, dim.height);
                }
                
                iY += dim.height;
                iHeight -= dim.height;
            }
    
            if (ui.getSouthPane() != null) 
            {
                Dimension dim = ui.getSouthPane().getPreferredSize();
                
                ui.getSouthPane().setBounds(iX, getHeight() - insets.bottom - dim.height, iWidth, dim.height);
                iHeight -= dim.height;
            }
    
            if (ui.getWestPane() != null) 
            {
                Dimension dim = ui.getWestPane().getPreferredSize();
                
                ui.getWestPane().setBounds(iX, iY, dim.width, iHeight);
                iWidth -= dim.width;
                iX += dim.width;           
            }
    
            if (ui.getEastPane() != null) 
            {
                Dimension dim = ui.getEastPane().getPreferredSize();
                
                ui.getEastPane().setBounds(iWidth - dim.width, iY, dim.width, iHeight);
                iWidth -= dim.width;           
            }
            
            if (getRootPane() != null) 
            {
                getRootPane().setBounds(iX, iY, iWidth, iHeight);
            }
        }
		
	}	// InternalLayout	
	
	/**
	 * The <code>ModalComponentListener</code> resizes the transparent panel for the modal desktop.
	 * 
	 * @author René Jahn
	 */
	public static class ModalComponentListener implements ComponentListener
	{
		/**
		 * {@inheritDoc}
		 */
		public void componentShown(ComponentEvent e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void componentResized(ComponentEvent e)
		{
			JPanel panModal = getModalLayerPanel((JDesktopPane)e.getSource());
			if (panModal.isVisible())
			{
				Container conParent = panModal.getParent();
				
				panModal.setBounds(0, 0, conParent.getWidth(), conParent.getHeight());
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void componentMoved(ComponentEvent e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void componentHidden(ComponentEvent e)
		{
		}
		
	}	// ModalComponentListener

	/**
	 * The <code>CompoundUIBorder</code> is a simple {@link CompoundBorder} that implements {@link javax.swing.plaf.UIResource}.
	 *  
	 * @author René Jahn
	 */
    static class CompoundUIBorder extends CompoundBorder 
                                  implements javax.swing.plaf.UIResource
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        /**
         * Creates a new instance of <code>CompoundUIBorder</code>.
         * 
         * @param pOuter the outer border
         * @param pInner the inner border
         */
        public CompoundUIBorder(Border pOuter, Border pInner)
        {
            super(pOuter, pInner);
        }
        
    }   // CompoundUIBorder
	
}	// JVxInternalFrame
