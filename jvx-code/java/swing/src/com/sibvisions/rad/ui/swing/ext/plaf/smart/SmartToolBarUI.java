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
 * 14.10.2008 - [JR] - setBackground with default value
 * 14.11.2008 - [JR] - used JVxUtil.getPreferredSize
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToolBarUI;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartPainter;

/**
 * The Smart/LF implementation of ToolBarUI. This implementation is a "combined" view/controller.
 * It uses the idea of Synth L&F to draw the UI but has a special drag window handling. 
 * <p>
 *
 * @author René Jahn
 */
public class SmartToolBarUI extends BasicToolBarUI 
                            implements ActionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the original margins before installing the smart margin. */
	private Insets insMarginOrig = null;
	
	/** the fake icon for the toolbar drag handle. */
	private Icon icoDragHandle = null;
	
	/** the container where the toolbar was added initially. */
	private Container dockingSource;
	
	/** the close Button for the floating toolbar. */
	private JButton butCloseFloatingWindow = null;
	
	/** the real time info if the toolBar can dock to the parent. */
	private boolean bCanDock = false;	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent pEvent)
	{
		Window winFloat = getFloatingWindow(toolBar, false);
		
		if (winFloat != null)
		{
			ButtonModel bmClose = butCloseFloatingWindow.getModel();
			
			//durch dispatchEvent würde ansonsten der Button-State nicht zurückgesetzt werden
			bmClose.setPressed(false);
			bmClose.setArmed(false);
			bmClose.setRollover(false);
			bmClose.setSelected(false);
			
			//close auslösen!
			winFloat.dispatchEvent(new WindowEvent(winFloat, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns an instance of the UI delegate for the specified component.
	 * 
	 * @param pComponent the component
	 * @return the UI for the component
	 */
	//kein @Override möglich, da statische Methode
	public static ComponentUI createUI(JComponent pComponent)
	{
		return new SmartToolBarUI();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninstallUI(JComponent pComponent)
	{
		super.uninstallUI(pComponent);
		
		dockingSource = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void installDefaults()
	{
		//Übernommen zwecks Kompatibilität, wäre auch nicht unbedingt nötig gewesen!
		//Dadurch wird allerdings das DragWindow repainted. Denn das passiert nur
		//wenn sich die Rahmenfarbe ändert!
		if (dockingColor == null || dockingColor instanceof UIResource)
		{
		    dockingColor = UIManager.getColor("ToolBar.dockingBackground");
		}
		if (floatingColor == null || floatingColor instanceof UIResource)
		{
		    floatingColor = UIManager.getColor("ToolBar.floatingBackground");
		}
		if (dockingBorderColor == null || dockingBorderColor instanceof UIResource)
		{
		    dockingBorderColor = UIManager.getColor("ToolBar.dockingForeground");
		}
		if (floatingBorderColor == null || floatingBorderColor instanceof UIResource)
		{
		    floatingBorderColor = UIManager.getColor("ToolBar.floatingForeground");
		}
		
		toolBar.setLayout(new ToolBarLayoutManager());

		butCloseFloatingWindow = new JButton();
		butCloseFloatingWindow.setFocusable(false);
		butCloseFloatingWindow.addActionListener(this);
		butCloseFloatingWindow.setMargin(new Insets(0, 0, 0, 0));
		butCloseFloatingWindow.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/plaf/smart/images/close.png"));
		//dadurch kann im Painter der Button unterschieden werden!
		butCloseFloatingWindow.putClientProperty(SmartTheme.NAME_TOOLBAR_CLOSE_BUTTON, "X");
		
		if (!JFrame.isDefaultLookAndFeelDecorated() && !JDialog.isDefaultLookAndFeelDecorated())
		{
			toolBar.add(butCloseFloatingWindow);
		}
		
		//Die Insets können nicht gesetzt werden, daher werden die Margins "misbraucht".
		//-> Der LayoutManager verwendet ebenfalls die Margins anstatt der Insets
		
		insMarginOrig = toolBar.getMargin();
		
		toolBar.setMargin(UIManager.getInsets(SmartTheme.NAME_TOOLBAR_INSETS));
		
		//Default Background color!
		toolBar.setBackground(new ColorUIResource(SmartTheme.COL_TOOLBAR_BACKGROUND));
	
		if (icoDragHandle == null)
		{
			Dimension dim = UIManager.getDimension(SmartTheme.NAME_TOOLBAR_HANDLESIZE);
			
			//Erstellen eines quadratischen Dummy-Icons, das sowohl horizontal als auch
			//vertikal passt. Der Handle selbst wird mit dem Background gezeichnet!
			icoDragHandle = new ImageIcon(new BufferedImage(dim.width, dim.height, BufferedImage.BITMASK));
		}
		
		installKeyboardActions();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void uninstallDefaults()
	{
		icoDragHandle = null;

		toolBar.setLayout(null);
		toolBar.remove(butCloseFloatingWindow);
		
		butCloseFloatingWindow = null;
		
		toolBar.setMargin(insMarginOrig);
		
		uninstallKeyboardActions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void installComponents()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void uninstallComponents()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Graphics pGraphics, JComponent pComponent)
	{
		SmartPainter.paintToolBarBackground(toolBar, pGraphics, 0, 0, pComponent.getWidth(), pComponent.getHeight());
		paint(pGraphics);
		SmartPainter.paintToolBarBorder(toolBar, pGraphics, 0, 0, pComponent.getWidth(), pComponent.getHeight());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setBorderToNonRollover(Component pComponent)
	{
		// Overloaded to do nothing so we can share listeners.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setBorderToRollover(Component pComponent)
	{
		// Overloaded to do nothing so we can share listeners.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setBorderToNormal(Component pComponent)
	{
		// Overloaded to do nothing so we can share listeners.
	}

	/**
	 * Paint the content of the toolbar.
	 * 
	 * @param g the graphics context of the toolbar
	 */
	protected void paint(Graphics g)
	{
		if (icoDragHandle != null && toolBar.isFloatable())
		{
			icoDragHandle.paintIcon(toolBar, g, 0, 0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override	
	protected void paintDragWindow(Graphics pGraphics)
	{
		int w = dragWindow.getWidth();
		int h = dragWindow.getHeight();
		
		SmartPainter.paintToolBarDragWindowBackground(toolBar, pGraphics, 0, 0, w, h, bCanDock);
		SmartPainter.paintToolBarDragWindowBorder(toolBar, pGraphics, 0, 0, w, h, bCanDock);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
    protected void dragTo(Point pMousePositionOnComponent, Point pOriginLocationOnScreen)
    {
		if (!toolBar.isFloatable())
		{
			return;
		}
		
		boolean bIsLeftToRight = SmartLookAndFeel.isLeftToRightOrientation(toolBar);
		boolean bCallSuper = false;
		
	    if (dragWindow == null)
    	{
	    	dockingSource = toolBar.getParent();
	    	bCallSuper = true;
    	}
    	else if (!dragWindow.isVisible())
    	{
    		dragWindow = null;
    		bCallSuper = true;
    	}
	    
	    if (bCallSuper)
	    {
    		super.dragTo(pMousePositionOnComponent, pOriginLocationOnScreen);

    		//Resize des Window, weil das Fenster normalerweise die PreferredSize der ToolBar
    		//hat. Wenn jedoch eine ToolBar höher/breiter dargestellt wird als die PreferredSize
    		//ist, dann würde die ToolBar abgeschnitten aussehen!
    		//Die Darstellung übernimmt der LayoutManager
    		if (toolBar.getOrientation() == JToolBar.VERTICAL)
    		{
    			dragWindow.setSize(toolBar.getSize().width, toolBar.getPreferredSize().height);    			
    		}
    		else
    		{
    			dragWindow.setSize(toolBar.getPreferredSize().width, toolBar.getSize().height);    			
    		}
    		
    		if (bIsLeftToRight)
    		{
    			dragWindow.setOffset(pMousePositionOnComponent);
    		}
    		else
    		{
    			//so tun als wäre rechts = links
    			dragWindow.setOffset(new Point(toolBar.getWidth() - pMousePositionOnComponent.x, pMousePositionOnComponent.y));
    		}
    	}

	    Point offset = dragWindow.getOffset();
	    
	    Point global = new Point(pOriginLocationOnScreen.x + pMousePositionOnComponent.x, pOriginLocationOnScreen.y + pMousePositionOnComponent.y);
	    Point dragPoint;
	    
	    if (bIsLeftToRight)
	    {
	    	dragPoint = new Point(global.x - offset.x, global.y - offset.y);	    	
	    }
	    else
	    {
	    	//auch hier: der rechte Rand ist Location 0, 0
	    	dragPoint = new Point(global.x + offset.x - toolBar.getPreferredSize().width, global.y - offset.y);
	    }
	    
	    Point dockingPosition = dockingSource.getLocationOnScreen();
	    Point comparisonPoint = new Point(global.x - dockingPosition.x, global.y - dockingPosition.y);
    	
    	if (canDock(dockingSource, comparisonPoint)) 
	    {
	    	bCanDock = true;
	    	
	    	dragWindow.setBackground(getDockingColor());	
			dragWindow.setBorderColor(dockingBorderColor);
	    } 
	    else 
	    {
	    	bCanDock = false;
	    	
			dragWindow.setBackground(getFloatingColor());
			dragWindow.setBorderColor(floatingBorderColor);
	    }
    	
    	dragWindow.setLocation(dragPoint);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RootPaneContainer createFloatingWindow(JToolBar pToolBar) 
    {
    	RootPaneContainer rpc = super.createFloatingWindow(pToolBar);
    	
    	if (rpc instanceof JDialog)
    	{
    		((JDialog)rpc).setUndecorated(true);
    	}
    	else if (rpc instanceof JFrame)
    	{
    		((JFrame)rpc).setUndecorated(true);
    	}
    	
    	return rpc;
    }    
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WindowListener createFrameListener()
	{
		return new ClosingFrameListener();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFloatingLocation(int pX, int pY)
	{
		if (SmartLookAndFeel.isLeftToRightOrientation(toolBar))
		{
			super.setFloatingLocation(pX, pY);
		}
		else
		{
			Point pOffset = dragWindow.getOffset();
			
			super.setFloatingLocation(pX - toolBar.getPreferredSize().width + pOffset.x + pOffset.x, pY);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setFloating(boolean pFloat, Point pMousePositionOnComponent) 
    {    
		super.setFloating(pFloat, pMousePositionOnComponent);

		//resize the window (same as implemented in ClosingFrameListener)
    	Window winFloat = getFloatingWindow(dockingSource, false);
    	
    	if (winFloat != null)
    	{
    		winFloat.pack();
    	}
    }
    
    /**
     * Detects the floating window of the toolbar.
     *  
     * @param pContainer the container for which the floating window shoul be detected
     * @param bpgnoreFloating <code>true</code> to detect the floating window regardless
     *        of the floating state of the toolbar. If the toolbar is not floating, it's
     *        possible that the parent toolbar, if exists, is floating.
     *        Use <code>false</code> if the detection should only search the window of
     *        this toolbar.
     * @return the floating window or null if no window was found
     */
    private Window getFloatingWindow(Container pContainer, boolean bpgnoreFloating)
    {
    	if (bpgnoreFloating || isFloating())
    	{
        	Container conCurrentDock = pContainer;
        	
	    	while (conCurrentDock != null && !(conCurrentDock instanceof JDialog))
			{
	    		conCurrentDock = conCurrentDock.getParent();
			}
	    	
	    	return (Window)conCurrentDock;
    	}
    	
    	return null;
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
    
	/**
	 * The <code>ToolBarLayoutManager</code> includes the insets of the
	 * <code>JToolBar</code> when layouting the components of the toolbar.
	 * 
	 * @author René Jahn
	 */
	class ToolBarLayoutManager implements LayoutManager, 
	                                      UIResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the separator size. */
		private Dimension dimSeparatorSize = UIManager.getDimension("ToolBar.separatorSize");
		
		/** the toolbar handle size. */
		private Dimension dimHandleSize = UIManager.getDimension(SmartTheme.NAME_TOOLBAR_HANDLESIZE);
		
		/** Indicates that the toolbar close button was removed. */
		private boolean bReAddClosingButton = false;
		
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
	    	if (pComponent == butCloseFloatingWindow)
	    	{
	    		bReAddClosingButton = true;
	    	}
	    	
	    	if (bReAddClosingButton && toolBar.getComponentCount() == 0)
	    	{
	    		bReAddClosingButton = false;
	    		
	    		toolBar.add(butCloseFloatingWindow);
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */
	    public Dimension minimumLayoutSize(Container pParent) 
	    {
	    	return preferredLayoutSize(pParent);
	    }

		/**
		 * {@inheritDoc}
		 */
	    public Dimension preferredLayoutSize(Container pParent) 
	    {
	        Dimension dimPreferred = new Dimension();
	        
	        Insets insBar = toolBar.getMargin();
	        
	        
	        Component comp;
	        Dimension dimComp;

	        if (toolBar.getOrientation() == JToolBar.HORIZONTAL) 
	        {
        		dimPreferred.width = (toolBar.isFloatable() ? dimHandleSize.width : 0) + insBar.left + insBar.right;
	            
	            for (int i = 0, anz = toolBar.getComponentCount(), iVisible = 0; i < anz; i++) 
	            {
	            	comp = toolBar.getComponent(i);
	                dimComp = JVxUtil.getPreferredSize(comp);

	                if (comp.isVisible() && comp != butCloseFloatingWindow)
	                {
	                	dimPreferred.width += dimComp.width;
	                	
	                	//dadurch wird verhindert, daß bei visible SubToolBars ohne visible Komponenten,
	                	//die Insets addiert werden (sonst wäre ein ToolBar ohne Komponenten sichtbar!)
		                if (dimComp.height > 0)
	                	{
	                		dimPreferred.height = Math.max(dimPreferred.height,
	                				                       dimComp.height + insBar.top + insBar.bottom);
	                	}
		                
		            	if (iVisible > 0)
		            	{
			                if (comp instanceof JToolBar)
			                {
			                    //"Separator" zwischen Toolbars
			                	dimPreferred.width += dimSeparatorSize.width;
			                }
			                else
			                {
			                	//1 Pixel zwischen Komponenten
			                	dimPreferred.width += 1;
			                }
		            	}
		            	
		            	iVisible++;
	                }
	            }
	        } 
	        else 
	        {
        		dimPreferred.height = (toolBar.isFloatable() ? dimHandleSize.height : 0) + insBar.top + insBar.bottom;
	            
	            for (int i = 0, anz = toolBar.getComponentCount(), iVisible = 0; i < anz; i++) 
	            {
	            	comp = toolBar.getComponent(i);
	                dimComp = JVxUtil.getPreferredSize(comp);
	                
	                if (comp.isVisible() && comp != butCloseFloatingWindow)
	                {
		                dimPreferred.height += dimComp.height;

		                //dadurch wird verhindert, daß bei visible SubToolBars ohne visible Komponenten,
	                	//die Insets addiert werden (sonst wäre ein ToolBar ohne Komponenten sichtbar!)
	                	if (dimComp.width > 0)
	                	{
			                dimPreferred.width = Math.max(dimPreferred.width, dimComp.width + insBar.left + insBar.right);
	                	}
		                
		            	//alle Zwischenräume verbreitern (nicht vor dem ersten und nicht nach dem letzten)
		                if (iVisible > 0)
		                {
			                if (comp instanceof JToolBar)
			                {
			                    //"Separator" zwischen Toolbars
			                	dimPreferred.height += dimSeparatorSize.height;
				            }
			                else
			                {
			                	//1 Pixel zwischen Komponenten
			                	dimPreferred.height += 1;
			                }
		                }
		                
		                iVisible++;
	                }
	            }
	        }
	        
	        return dimPreferred;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void layoutContainer(Container pParent) 
	    {
	        Insets insets = toolBar.getMargin();

	        boolean bIsLeftToRight = SmartLookAndFeel.isLeftToRightOrientation(pParent);
	        
	        Component comp;
	        Dimension dimComp;

        	int iWidth;
        	int iHeight;
	        
        	int iToolBarWidth = toolBar.getWidth();
        	int iToolBarHeight = toolBar.getHeight();

        	
        	if (toolBar.getOrientation() == JToolBar.HORIZONTAL) 
	        {
	        	int iHandleWidth = (toolBar.isFloatable() ? dimHandleSize.width : 0);
	        	
	            int iX = bIsLeftToRight ? iHandleWidth + insets.left : iToolBarWidth - iHandleWidth - insets.right;
	            
	            for (int i = 0, anz = toolBar.getComponentCount(); i < anz; i++) 
	            {
	                comp = toolBar.getComponent(i);
	                dimComp = JVxUtil.getPreferredSize(comp);
	                
	                iWidth  = dimComp.width;
	                iHeight = Math.max(dimComp.height, iToolBarHeight - insets.top - insets.bottom);
	                
	                if (comp == butCloseFloatingWindow)
	                {
	                	if (isFloating())
	                	{
	                		comp.setVisible(true);
	                		
	                		comp.setBounds(bIsLeftToRight ? 0 : iToolBarWidth - 12, 0, 12, 12);
	                	}
	                	else
	                	{
	                		comp.setVisible(false);
	                	}
	                }
	                else if (comp.isVisible())
	                {
	                	comp.setBounds(bIsLeftToRight ? iX : iX - iWidth, (iToolBarHeight - iHeight) / 2, iWidth, iHeight);
	                	
		                iX = bIsLeftToRight ? iX + iWidth : iX - iWidth;
		                
		            	//alle Zwischenräume verbreiten (nicht vor dem ersten und nicht nach dem letzten)
		                if (i < anz - 1)
		                {
			                if (comp instanceof JToolBar)
			                {
			                	iX = bIsLeftToRight ? iX + dimSeparatorSize.width : iX - dimSeparatorSize.width;
			                }
			                else
			                {
			                	iX = bIsLeftToRight ? iX + 1 : iX - 1;
			                }
		                }
	                }
	            }
	        } 
	        else 
	        {
	            int iY = (toolBar.isFloatable() ? dimHandleSize.height : 0) + insets.top;
	            
	            for (int i = 0, anz = toolBar.getComponentCount(); i < anz; i++) 
	            {
	                comp = toolBar.getComponent(i);
	                dimComp = JVxUtil.getPreferredSize(comp);
	                
	                iWidth  = Math.max(dimComp.width, iToolBarWidth - insets.left - insets.right);
	                iHeight = dimComp.height;
	                
	                if (comp == butCloseFloatingWindow)
	                {
	                	if (isFloating())
	                	{
	                		comp.setVisible(true);
	                		comp.setBounds(iToolBarWidth - 12, 0, 12, 12);
	                	}
	                	else
	                	{
	                		comp.setVisible(false);
	                	}
	                }
	                else if (comp.isVisible())
	                {
		                comp.setBounds((iToolBarWidth - iWidth) / 2, iY, iWidth, iHeight);
		                iY += iHeight;
		                
		            	//alle Zwischenräume verbreiten (nicht vor dem ersten und nicht nach dem letzten)
		                if (i < anz - 1)
		                {
			                if (comp instanceof JToolBar)
			                {
			                	iY += dimSeparatorSize.height;
			                }
			                else
			                {
			                	iY += 1;
			                }
		                }
	                }
	            }
	        }
	    }
	    
	}	// ToolBarLayoutManager	

	/**
	 * The <code>ClosingFrameListener</code> extends the {@link BasicToolBarUI.FrameListener}
	 * class and updates the floating window of a parent toolbar, if available. The parent
	 * window repaints to the preferred size.<p/> 
	 * Without this implementation, the parent window doesn't resize!
	 * 
	 * @author René Jahn
	 */
	private class ClosingFrameListener extends BasicToolBarUI.FrameListener
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void windowClosing(WindowEvent pEvent)
		{
			super.windowClosing(pEvent);
			
			//resize the window (same idea as implemented in setFloating)
			Window win = getFloatingWindow(toolBar, true);
			
			if (win != null)
			{
				win.pack();
			}
		}
		
	}	// ClosingFrameListener
	
}	//SmartToolBarUI
