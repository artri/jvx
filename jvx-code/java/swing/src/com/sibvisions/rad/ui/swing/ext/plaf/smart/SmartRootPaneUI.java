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
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;

/**
 * Provides the Smart/LF implementation of <code>RootPaneUI</code>.
 * <p>
 * <code>SmartRootPaneUI</code> provides support for the
 * <code>windowDecorationStyle</code> property of <code>JRootPane</code>.
 * <code>SmartRootPaneUI</code> does this by way of installing a custom
 * <code>LayoutManager</code>, a private <code>Component</code> to render
 * the appropriate widgets, and a private <code>Border</code>. The
 * <code>LayoutManager</code> is always installed, regardless of the value of
 * the <code>windowDecorationStyle</code> property, but the
 * <code>Border</code> and <code>Component</code> are only installed/added if
 * the <code>windowDecorationStyle</code> is other than
 * <code>JRootPane.NONE</code>.
 *
 * @author René Jahn
 */
public class SmartRootPaneUI extends BasicRootPaneUI 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] CURSORMAPPING = new int[] { Cursor.NW_RESIZE_CURSOR, 
    													   Cursor.NW_RESIZE_CURSOR, 
    													   Cursor.N_RESIZE_CURSOR,
    													   Cursor.NE_RESIZE_CURSOR, 
    													   Cursor.NE_RESIZE_CURSOR,
    													   Cursor.NW_RESIZE_CURSOR, 
    													   0, 
    													   0, 
    													   0, 
    													   Cursor.NE_RESIZE_CURSOR,
    													   Cursor.W_RESIZE_CURSOR, 
    													   0, 
    													   0, 
    													   0, 
    													   Cursor.E_RESIZE_CURSOR,
    													   Cursor.SW_RESIZE_CURSOR, 
    													   0, 
    													   0, 
    													   0, 
    													   Cursor.SE_RESIZE_CURSOR,
    													   Cursor.SW_RESIZE_CURSOR, 
    													   Cursor.SW_RESIZE_CURSOR, 
    													   Cursor.S_RESIZE_CURSOR,
    													   Cursor.SE_RESIZE_CURSOR, 
    													   Cursor.SE_RESIZE_CURSOR
    													 };

    /** the amount of space (in pixels) that the cursor is changed on. */
    private static final int CORNER_DRAG_WIDTH = 16;

    
    /** window the <code>JRootPane</code> is in. */
    private Window window;

    /** 
     * <code>JComponent</code> providing window decorations. This will be
     * null if not providing window decorations.
     */
    private JComponent titlePane;

    /**
     * <code>MouseInputListener</code> that is added to the parent
     * <code>Window</code> the <code>JRootPane</code> is contained in.
     */
    private MouseInputListener mouseInputListener;

    /**
     * The <code>LayoutManager</code> that is set on the
     * <code>JRootPane</code>.
     */
    private LayoutManager layout;

    /**
     * <code>LayoutManager</code> of the <code>JRootPane</code> before we
     * replaced it.
     */
    private LayoutManager layoutOld;

    /** <code>JRootPane</code> providing the look and feel for. */
    private JRootPane rootPane;

    /**
     * <code>Cursor</code> used to track the cursor set by the user.  
     * This is initially <code>Cursor.DEFAULT_CURSOR</code>.
     */
    private Cursor curLast = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    /** the regions from edges that dragging is active from. */
    private Insets insBorderSize; 

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a UI for a <code>JRootPane</code>.
     *
     * @param pComponent the JRootPane the RootPaneUI will be created for
     * @return the RootPaneUI implementation for the passed in JRootPane
     */
    public static ComponentUI createUI(JComponent pComponent) 
    {
        return new SmartRootPaneUI();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invokes supers implementation of <code>installUI</code> to install
     * the necessary state onto the passed in <code>JRootPane</code>
     * to render the Smart/LF implementation of <code>RootPaneUI</code>. If
     * the <code>windowDecorationStyle</code> property of the
     * <code>JRootPane</code> is other than <code>JRootPane.NONE</code>,
     * this will add a custom <code>Component</code> to render the widgets to
     * <code>JRootPane</code>, as well as installing a custom
     * <code>Border</code> and <code>LayoutManager</code> on the
     * <code>JRootPane</code>.
     *
     * @param pComponent the JRootPane to install state onto
     */
    @Override
    public void installUI(JComponent pComponent) 
    { 
        super.installUI(pComponent);
        
        rootPane = (JRootPane)pComponent;
        
        int style = rootPane.getWindowDecorationStyle();
        
        if (style != JRootPane.NONE) 
        {
            installClientDecorations(rootPane);
        }
        else
        {
        	window = SwingUtilities.getWindowAncestor(rootPane);
        }

        installDefaultIcon();
    }

    /**
     * Invokes supers implementation to uninstall any of its state. This will
     * also reset the <code>LayoutManager</code> of the <code>JRootPane</code>.
     * If a <code>Component</code> has been added to the <code>JRootPane</code>
     * to render the window decoration style, this method will remove it.
     * Similarly, this will revert the Border and LayoutManager of the
     * <code>JRootPane</code> to what it was before <code>installUI</code>
     * was invoked.
     *
     * @param pComponent the <code>JRootPane</code> to uninstall state from
     */
    @Override
    public void uninstallUI(JComponent pComponent) 
    {
        super.uninstallUI(pComponent);
        
        uninstallClientDecorations(rootPane);

        //reset der Komponenten
        layout = null;
        mouseInputListener = null;
        rootPane = null;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Installs the <code>Border</code> onto the <code>JRootPane</code>.
     * 
     * @param pRoot the <code>JRootPane</code> to install the border 
     */
    void installBorder(JRootPane pRoot) 
    {
        int style = pRoot.getWindowDecorationStyle();

        if (style == JRootPane.NONE) 
        {
            LookAndFeel.uninstallBorder(pRoot);
        }
        else 
        {
        	LookAndFeel.installBorder(pRoot, SmartLookAndFeel.NAME_ROOTPANE_BORDER);
        }
        
        //Insets des Borders verwenden!
        insBorderSize = pRoot.getBorder().getBorderInsets(pRoot);
    }

    /**
     * Removes any border that may have been installed.
     * 
     * @param pRoot the <code>JRootPane</code> to uninstall the border 
     */
    private void uninstallBorder(JRootPane pRoot) 
    {
        LookAndFeel.uninstallBorder(pRoot);
    }

    /**
     * Installs the necessary Listeners for client decorations.
     * 
     * @param pRoot the <code>JRootPane</code> to install the necessary listeners
     */
    private void installClientDecorationListeners(JRootPane pRoot) 
    {
    	Window winOld = window;
    	
        window = SwingUtilities.getWindowAncestor(pRoot);
        
        if (winOld != window)
        {
        	installDefaultIcon();
        }
        
        if (window != null) 
        {
            if (mouseInputListener == null) 
            {
                mouseInputListener = createMouseInputListener(pRoot);
            }
            
            pRoot.addMouseListener(mouseInputListener);
            pRoot.addMouseMotionListener(mouseInputListener);
            
            if (titlePane != null)
            {
	            titlePane.addMouseListener(mouseInputListener);
	            titlePane.addMouseMotionListener(mouseInputListener);
            }
        }
    }
    
    /**
     * Installs a default icon to a frame, if it has not set an icon.
     */
    private void installDefaultIcon()
    {
        //immer ein Default-Image setzen, solange durch den User dieses nicht geändert wird!
        //Im Gegensatz zu allen anderen LaFs ist es möglich bei einem Frame kein Icon anzuzeigen!
        if (window != null && window instanceof Frame)
        {
        	((Frame)window).setIconImage(JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/plaf/smart/images/frame.png"));
        }
    }

    /**
     * Uninstalls the necessary Listeners for client decorations. This may
     * be invoked without a corresponding installClientDecorationListeners.
     * 
     * @param pRoot the <code>JRootPane</code> to uninstall the listeners
     */
    private void uninstallClientDecorationListeners(JRootPane pRoot) 
    {
        if (window != null) 
        {
            pRoot.removeMouseListener(mouseInputListener);
            pRoot.removeMouseMotionListener(mouseInputListener);
            
            if (titlePane != null) 
            {
                titlePane.removeMouseListener(mouseInputListener);
                titlePane.removeMouseMotionListener(mouseInputListener);
            }
        }
    }

    /**
     * Installs the appropriate LayoutManager on the <code>JRootPane</code>
     * to render the window decorations.
     * 
     * @param pRoot the <code>JRootPane</code> to install the layout
     */
    private void installLayout(JRootPane pRoot) 
    {
        layoutOld = pRoot.getLayout();

        if (layout == null) 
        {
            layout = createLayoutManager();
        }
        
        pRoot.setLayout(layout);
    }

    /**
     * Uninstalls the previously installed <code>LayoutManager</code>.
     * 
     * @param pRoot the <code>JRootPane</code> to uninstall the layout
     */
    private void uninstallLayout(JRootPane pRoot) 
    {
        if (layoutOld != null) 
        {
            pRoot.setLayout(layoutOld);
            
            layoutOld = null;
        }
    }

    /**
     * Installs the necessary state onto the JRootPane to render client
     * decorations. This is ONLY invoked if the <code>JRootPane</code>
     * has a decoration style other than <code>JRootPane.NONE</code>.
     * 
     * @param pRoot the <code>JRootPane</code> to install the decoration
     */
    private void installClientDecorations(JRootPane pRoot) 
    {
    	installBorder(pRoot);

        setTitlePane(pRoot, createTitlePane(pRoot));
        installClientDecorationListeners(pRoot);
        installLayout(pRoot);
        
        if (window != null) 
        {
            pRoot.revalidate();
            pRoot.repaint();
        }
    }

    /**
     * Uninstalls any state that <code>installClientDecorations</code> has
     * installed.
     * <p>
     * NOTE: This may be called if you haven't installed client decorations
     * yet (e.g. before <code>installClientDecorations</code> has been invoked).
     * 
     * @param pRoot the <code>JRootPane</code> to uninstall the decoration
     */
    private void uninstallClientDecorations(JRootPane pRoot) 
    {
        uninstallBorder(pRoot);
        uninstallClientDecorationListeners(pRoot);
        
        setTitlePane(pRoot, null);
        
        uninstallLayout(pRoot);
        
        if (pRoot.getWindowDecorationStyle() == JRootPane.NONE) 
        {
    		// We have to revalidate/repaint root if the style is JRootPane.NONE
    		// only. When we needs to call revalidate/repaint with other styles
    		// the installClientDecorations is always called after this method
    		// immediatly and it will cause the revalidate/repaint at the proper
    		// time.
        	pRoot.repaint();
        	pRoot.revalidate();
        }
        
        // Reset the cursor, as we may have changed it to a resize cursor
        if (window != null) 
        {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
        window = null;
    }

    /**
     * Returns the <code>JComponent</code> to render the window decoration
     * style.
     * 
     * @param pRoot the <code>JRootPane</code> for which the title component 
     *              will be created
     * @return the title component
     */
    private JComponent createTitlePane(JRootPane pRoot) 
    {
    	return new SmartRootTitlePane(pRoot, this);
    }

    /**
     * Returns a <code>MouseListener</code> that will be added to the
     * <code>JRootPane</code> and title pane.
     * 
     * @param pRoot the <code>JRootPane</code> for which the mouse listener 
     *              will be created
     * @return the mouse listene for the <code>JRootPane</code> 
     */
    private MouseInputListener createMouseInputListener(JRootPane pRoot) 
    {
        return new InternalMouseInputListener();
    }

    /**
     * Returns a <code>LayoutManager</code> that will be set on the
     * <code>JRootPane</code>.
     * 
     * @return the <code>JRootPane</code> layout manager
     */
    private LayoutManager createLayoutManager() 
    {
        return new InternalRootLayout();
    }

    /** 
     * Sets the window title pane, the JComponent used to provide a plaf a
     * way to override the native operating system's window title pane with
     * one whose look and feel are controlled by the plaf.  The plaf creates 
     * and sets this value; the default is null, implying a native operating
     * system window title pane.
     * 
     * @param pRoot the <code>JRootPane</code> to set the title component
     * @param pTitlePane the <code>JComponent</code> to use for the window title pane.
     */
    private void setTitlePane(JRootPane pRoot, JComponent pTitlePane) 
    {
        JLayeredPane layeredPane = pRoot.getLayeredPane();
        JComponent oldTitlePane = getTitlePane();

        if (oldTitlePane != null) 
        {
            oldTitlePane.setVisible(false);
            layeredPane.remove(oldTitlePane);
        }
        
        if (pTitlePane != null) 
        {
            layeredPane.add(pTitlePane, JLayeredPane.FRAME_CONTENT_LAYER);
            pTitlePane.setVisible(true);
        }
        
        this.titlePane = pTitlePane;
    }

    /**
     * Returns the <code>JComponent</code> rendering the title pane. If this
     * returns null, it implies there is no need to render window decorations.
     *
     * @return the current window title pane, or null
     * @see #setTitlePane
     */
    public JComponent getTitlePane() 
    {
        return titlePane;
    }

    /**
     * Returns the <code>JRootPane</code> we're providing the look and
     * feel for.
     * 
     * @return the root pane
     */
    private JRootPane getRootPane() 
    {
        return rootPane;
    }

    /**
     * Invoked when a property changes. <code>SmartRootPaneUI</code> is
     * primarily interested in events originating from the
     * <code>JRootPane</code> it has been installed on identifying the
     * property <code>windowDecorationStyle</code>. If the 
     * <code>windowDecorationStyle</code> has changed to a value other
     * than <code>JRootPane.NONE</code>, this will add a <code>Component</code>
     * to the <code>JRootPane</code> to render the window decorations, as well
     * as installing a <code>Border</code> on the <code>JRootPane</code>.
     * On the other hand, if the <code>windowDecorationStyle</code> has
     * changed to <code>JRootPane.NONE</code>, this will remove the
     * <code>Component</code> that has been added to the <code>JRootPane</code>
     * as well resetting the Border to what it was before <code>installUI</code> 
     * was invoked.
     *
     * @param pEvent a PropertyChangeEvent object describing the event source 
     *               and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent pEvent) 
    {
        super.propertyChange(pEvent);
        
        String sPropertyName = pEvent.getPropertyName();
        
        if (sPropertyName == null) 
        {
            return;
        }
    
        if ("windowDecorationStyle".equals(sPropertyName)) 
        {
            JRootPane rpSource = (JRootPane)pEvent.getSource();
            
            int style = rpSource.getWindowDecorationStyle();

            // This is potentially more than needs to be done,
            // but it rarely happens and makes the install/uninstall process
            // simpler. SmartRootTitlePane also assumes it will be recreated if
            // the decoration style changes.
            uninstallClientDecorations(rpSource);
            
            if (style != JRootPane.NONE) 
            {
                installClientDecorations(rpSource);
            }
        }
        else if (sPropertyName.equals("ancestor")) 
        {
            uninstallClientDecorationListeners(rootPane);
            
            if (((JRootPane)pEvent.getSource()).getWindowDecorationStyle() != JRootPane.NONE) 
            {
                installClientDecorationListeners(rootPane);
            }
        }
        return;
    } 

    //****************************************************************
	// Subclass definition
	//****************************************************************

    /** 
     * A custom layout manager that is responsible for the layout of 
     * layeredPane, glassPane, menuBar and titlePane, if one has been
     * installed.
     * 
     * @author René Jahn
     */
    private static class InternalRootLayout implements LayoutManager2 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
         * Returns the amount of space the layout would like to have.
         *
         * @param pParent the Container for which this layout manager is being used
         * @return a {@link Dimension} object containing the layout's preferred size
         */ 
        public Dimension preferredLayoutSize(Container pParent) 
        {
            Dimension dimContentPane;
            Dimension dimMenuBar;
            Dimension dimTitlePane;
            
            int iWidthContentPane  	= 0;
            int iHeightContentPane 	= 0;
            int iWidthMenuBar 		= 0;
            int iHeightMenuBar 		= 0;
            int iWidthTitlePane 	= 0;
            
            Insets insParent = pParent.getInsets();

            JRootPane rpLayout = (JRootPane)pParent;
    
            if (rpLayout.getContentPane() != null) 
            {
                dimContentPane = rpLayout.getContentPane().getPreferredSize();
            } 
            else 
            {
                dimContentPane = rpLayout.getSize();
            }
            
            if (dimContentPane != null) 
            {
                iWidthContentPane = dimContentPane.width;
                iHeightContentPane = dimContentPane.height;
            }

            //Menü berücksichtigen
            if (rpLayout.getJMenuBar() != null) 
            {
                dimMenuBar = rpLayout.getJMenuBar().getPreferredSize();
                
                if (dimMenuBar != null) 
                {
                    iWidthMenuBar = dimMenuBar.width;
                    iHeightMenuBar = dimMenuBar.height;
                }
            } 

            //Title-Pane berücksichtigen 
            if (rpLayout.getWindowDecorationStyle() != JRootPane.NONE 
            	&& (rpLayout.getUI() instanceof SmartRootPaneUI)) 
            {
                JComponent compTitlePane = ((SmartRootPaneUI)rpLayout.getUI()).getTitlePane();
                
                if (compTitlePane != null) 
                {
                    dimTitlePane = compTitlePane.getPreferredSize();
                    
                    if (dimTitlePane != null) 
                    {
                        iWidthTitlePane  = dimTitlePane.width;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(iWidthContentPane, iWidthMenuBar), iWidthTitlePane) + insParent.left + insParent.right, 
                                 iHeightContentPane + iHeightMenuBar + iWidthTitlePane + insParent.top + insParent.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param pParent the Container for which this layout manager is being used
         * @return a {@link Dimension} object containing the layout's minimum size
         */ 
        public Dimension minimumLayoutSize(Container pParent) 
        {
            Dimension dimContentPane;
            Dimension dimMenuBar;
            Dimension dimTitlePane;
            
            int iWidthContentPane 	= 0;
            int iHeightContentPane 	= 0;
            int iWidthMenuBar 		= 0;
            int iHeightMenuBar 		= 0;
            int iWidthTitlePane 	= 0;
            int iHeightTitlePane     = 0;
            
            Insets insParent = pParent.getInsets();
            
            JRootPane rpLayout = (JRootPane)pParent;
            
        
            if (rpLayout.getContentPane() != null) 
            {
                dimContentPane = rpLayout.getContentPane().getMinimumSize();
            } 
            else 
            {
                dimContentPane = rpLayout.getSize();
            }
            
            if (dimContentPane != null) 
            {
                iWidthContentPane = dimContentPane.width;
                iHeightContentPane = dimContentPane.height;
            }

            //Menü berücksichtigen
            if (rpLayout.getJMenuBar() != null) 
            {
                dimMenuBar = rpLayout.getJMenuBar().getMinimumSize();
                
                if (dimMenuBar != null) 
                {
                    iWidthMenuBar = dimMenuBar.width;
                    iHeightMenuBar = dimMenuBar.height;
                }
            }   
            
            //Title-Pane berücksichtigen
            if (rpLayout.getWindowDecorationStyle() != JRootPane.NONE 
            	&& (rpLayout.getUI() instanceof SmartRootPaneUI)) 
            {
                JComponent compTitlePane = ((SmartRootPaneUI)rpLayout.getUI()).getTitlePane();
                
                if (compTitlePane != null) 
                {
                    dimTitlePane = compTitlePane.getMinimumSize();
                    
                    if (dimTitlePane != null) 
                    {
                        iWidthTitlePane = dimTitlePane.width;
                        iHeightTitlePane = dimTitlePane.height;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(iWidthContentPane, iWidthMenuBar), iWidthTitlePane) + insParent.left + insParent.right, 
                                 iHeightContentPane + iHeightMenuBar + iHeightTitlePane + insParent.top + insParent.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param pParent the Container for which this layout manager is being used
         * @return a {@link Dimension} object containing the layout's maximum size
         */ 
        public Dimension maximumLayoutSize(Container pParent) 
        {
            Dimension dimContentPane;
            Dimension dimMenuBar; 
            Dimension dimTitlePane;
            
            int iWidthContentPane 	= Integer.MAX_VALUE;
            int iHeightContentPane 	= Integer.MAX_VALUE;
            int iWidthMenuBar 		= Integer.MAX_VALUE;
            int iHeightMenuBar 		= Integer.MAX_VALUE;
            int iWidthTitlePane 	= Integer.MAX_VALUE;
            int iHeightTitlePane 	= Integer.MAX_VALUE;
            
            Insets insParent = pParent.getInsets();
            
            JRootPane rpLayout = (JRootPane)pParent;
        
            
            if (rpLayout.getContentPane() != null) 
            {
                dimContentPane = rpLayout.getContentPane().getMaximumSize();
                
                if (dimContentPane != null) 
                {
                    iWidthContentPane = dimContentPane.width;
                    iHeightContentPane = dimContentPane.height;
                }
            }

            //Menü berücksichtigen
            if (rpLayout.getJMenuBar() != null) 
            {
                dimMenuBar = rpLayout.getJMenuBar().getMaximumSize();
                
                if (dimMenuBar != null) 
                {
                    iWidthMenuBar = dimMenuBar.width;
                    iHeightMenuBar = dimMenuBar.height;
                }
            }

            //Title-Pane berücksichtigen
            if (rpLayout.getWindowDecorationStyle() != JRootPane.NONE 
            	&& (rpLayout.getUI() instanceof SmartRootPaneUI)) 
            {
                JComponent compTitlePane = ((SmartRootPaneUI)rpLayout.getUI()).getTitlePane();
                
                if (compTitlePane != null)
                {
                    dimTitlePane = compTitlePane.getMaximumSize();
                    
                    if (dimTitlePane != null) 
                    {
                        iWidthTitlePane = dimTitlePane.width;
                        iHeightTitlePane = dimTitlePane.height;
                    }
                }
            }

            int maxHeight = Math.max(Math.max(iHeightContentPane, iHeightMenuBar), iHeightTitlePane);
            
            // Only overflows if 3 real non-MAX_VALUE heights, sum to > MAX_VALUE
            // Only will happen if sums to more than 2 billion units.  Not likely.
            if (maxHeight != Integer.MAX_VALUE) 
            {
                maxHeight = iHeightContentPane + iHeightMenuBar + iHeightTitlePane + insParent.top + insParent.bottom;
            }
    
            int maxWidth = Math.max(Math.max(iWidthContentPane, iWidthMenuBar), iWidthTitlePane);
            
            // Similar overflow comment as above
            if (maxWidth != Integer.MAX_VALUE) 
            {
                maxWidth += insParent.left + insParent.right;
            }

            return new Dimension(maxWidth, maxHeight);
        }
    
        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param pParent the Container for which this layout manager is being used
         */ 
        public void layoutContainer(Container pParent) 
        {
            JRootPane rpLayout = (JRootPane)pParent;
            Rectangle rectParent = rpLayout.getBounds();
            Insets insParent = rpLayout.getInsets();
            
            int iNextY = 0;
            int iWidth = rectParent.width - insParent.right - insParent.left;
            int iHeight = rectParent.height - insParent.top - insParent.bottom;
    
            
            if (rpLayout.getLayeredPane() != null) 
            {
                rpLayout.getLayeredPane().setBounds(insParent.left, insParent.top, iWidth, iHeight);
            }
            
            if (rpLayout.getGlassPane() != null) 
            {
                rpLayout.getGlassPane().setBounds(insParent.left, insParent.top, iWidth, iHeight);
            }
            
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our children.
            if (rpLayout.getWindowDecorationStyle() != JRootPane.NONE 
            	&& (rpLayout.getUI() instanceof SmartRootPaneUI)) 
            {
                JComponent compTitlePane = ((SmartRootPaneUI)rpLayout.getUI()).getTitlePane();
                
                if (compTitlePane != null) 
                {
                    Dimension dimTitlePane = compTitlePane.getPreferredSize();
                    
                    if (dimTitlePane != null) 
                    {
                        int iHeightTitlePane = dimTitlePane.height;
                        
                        compTitlePane.setBounds(0, 0, iWidth, iHeightTitlePane);
                        
                        iNextY += iHeightTitlePane;
                    }                    
                }
            }
            
            if (rpLayout.getJMenuBar() != null) 
            {
                Dimension dimMenuBar = rpLayout.getJMenuBar().getPreferredSize();
                
                rpLayout.getJMenuBar().setBounds(0, iNextY, iWidth, dimMenuBar.height);
                
                iNextY += dimMenuBar.height;
            }
            
            if (rpLayout.getContentPane() != null) 
            {
                rpLayout.getContentPane().setBounds(0, iNextY, iWidth, (iHeight < iNextY ? 0 : iHeight - iNextY));
            }
        }
    
        /**
         * {@inheritDoc}
         */
        public void addLayoutComponent(String name, Component comp) 
        {
        }
        
        /**
         * {@inheritDoc}
         */
        public void removeLayoutComponent(Component comp) 
        {
        }
        
        /**
         * {@inheritDoc}
         */
        public void addLayoutComponent(Component comp, Object constraints)
        {
        }
        
        /**
         * {@inheritDoc}
         */
        public float getLayoutAlignmentX(Container target) 
        { 
        	return 0.0f; 
        }
        
        /**
         * {@inheritDoc}
         */
        public float getLayoutAlignmentY(Container target) 
        { 
        	return 0.0f; 
        }
        
        /**
         * {@inheritDoc}
         */
        public void invalidateLayout(Container target)
        {
        }
        
    }	// InternalRootLayout

    /**
     * The <code>MouseInputHandler</code> is responsible for handling 
     * resize/moving of the Window. It sets the cursor directly on the 
     * Window when the mouse moves over a hot spot.
     * 
     * @author René Jahn
     */
    private class InternalMouseInputListener implements MouseInputListener 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** set to true if the drag operation is moving the window. */
        private boolean bWindowIsMoving;

        /** used to determine the corner the resize is occuring from. */
        private int iDragCursor;

        /** the x location the mouse went down on for a drag operation. */
        private int iDragOffsetX;

        /** the y location the mouse went down on for a drag operation. */
        private int iDragOffsetY;

        /** the width of the window when the drag started. */
        private int iDragWidth;

        /** the height of the window when the drag started. */
        private int iDragHeight;

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
        public void mousePressed(MouseEvent pEvent) 
        {
            JRootPane rpRoot = getRootPane();

        	if (rpRoot.getWindowDecorationStyle() == JRootPane.NONE) 
            {
                return;
            }
            
            Point ptDragWindowOffset = pEvent.getPoint();
            Component source 		 = (Component)pEvent.getSource();
            Window winEvent			 = getWindow(pEvent);
            
            if (winEvent != null) 
            {
                winEvent.toFront();
            }
            
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(source, ptDragWindowOffset, getTitlePane());
            ptDragWindowOffset = SwingUtilities.convertPoint(source, ptDragWindowOffset, winEvent);

            Frame frame = null;
            Dialog dialog = null;

            if (winEvent instanceof Frame) 
            {
                frame = (Frame)winEvent;
            } 
            else if (winEvent instanceof Dialog) 
            {
                dialog = (Dialog)winEvent;
            }

            int frameState = (frame != null) ? frame.getExtendedState() : 0;

            if (getTitlePane() != null 
            	&& getTitlePane().contains(convertedDragWindowOffset)) 
            {
                if ((frame != null 
                	 && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                         || (dialog != null))
                     && ptDragWindowOffset.y >= insBorderSize.left
                     && ptDragWindowOffset.x >= insBorderSize.top
                     && ptDragWindowOffset.x < winEvent.getWidth() - insBorderSize.right) 
                {
                    bWindowIsMoving = true;
                    
                    iDragOffsetX = ptDragWindowOffset.x;
                    iDragOffsetY = ptDragWindowOffset.y;
                }
            }
            else if (frame != null && frame.isResizable()
                    && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                    || (dialog != null && dialog.isResizable())) 
            {
                iDragOffsetX = ptDragWindowOffset.x;
                iDragOffsetY = ptDragWindowOffset.y;
                
                iDragWidth  = winEvent.getWidth();
                iDragHeight = winEvent.getHeight();
                iDragCursor = getCursor(calculateCorner(winEvent, ptDragWindowOffset.x, ptDragWindowOffset.y));
            }
        }

        /**
         * {@inheritDoc}
         */
        public void mouseReleased(MouseEvent pEvent) 
        {
        	//RESET
        	
            if (iDragCursor != 0 && window != null && !window.isValid()) 
            {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                window.validate();
                getRootPane().repaint();
            }
            
            bWindowIsMoving = false;
            iDragCursor = 0;
        }

        /**
         * {@inheritDoc}
         */
        public void mouseMoved(MouseEvent pEvent) 
        {
            JRootPane rpRoot = getRootPane();

            if (rpRoot.getWindowDecorationStyle() == JRootPane.NONE) 
            {
                return;
            }

            Component source 	= (Component)pEvent.getSource();
            Window winEvent 	= getWindow(pEvent);
            Point ptEvent 		= SwingUtilities.convertPoint(source, pEvent.getPoint(), winEvent);
            
            Frame frame = null;
            Dialog dialog = null;

            if (winEvent instanceof Frame) 
            {
                frame = (Frame)winEvent;
            } 
            else if (winEvent instanceof Dialog) 
            {
                dialog = (Dialog)winEvent;
            }

            // Update the cursor
            int cursor = getCursor(calculateCorner(winEvent, ptEvent.x, ptEvent.y));

            if (cursor != 0 
            	&& ((frame != null 
            		 && (frame.isResizable() 
            		 && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))
                    || (dialog != null && dialog.isResizable()))) 
            {
                winEvent.setCursor(Cursor.getPredefinedCursor(cursor));
            }
            else 
            {
                winEvent.setCursor(curLast);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void mouseDragged(MouseEvent pEvent) 
        {
            Component source 	= (Component)pEvent.getSource();
            Window winEvent 	= getWindow(pEvent);
            Point ptEvent 		= SwingUtilities.convertPoint(source, pEvent.getPoint(), winEvent);

            
            if (bWindowIsMoving) 
            {
                winEvent.setLocation(rootPane.getLocationOnScreen().x + pEvent.getX() + 8 - iDragOffsetX, 
                					 rootPane.getLocationOnScreen().y + pEvent.getY() + 8 - iDragOffsetY); 
            }
            else if (iDragCursor != 0) 
            {
                Rectangle rectEventWindow = winEvent.getBounds();
                Rectangle startBounds = new Rectangle(rectEventWindow);
                Dimension min = winEvent.getMinimumSize();

                switch (iDragCursor) 
                {
	                case Cursor.E_RESIZE_CURSOR:
	                    adjust(rectEventWindow, min, 0, 0, ptEvent.x + (iDragWidth - iDragOffsetX) - rectEventWindow.width, 0);
	                    break;
	                case Cursor.S_RESIZE_CURSOR:
	                    adjust(rectEventWindow, min, 0, 0, 0, ptEvent.y + (iDragHeight - iDragOffsetY) - rectEventWindow.height);
	                    break;
	                case Cursor.N_RESIZE_CURSOR:
	                    adjust(rectEventWindow, min, 0, ptEvent.y - iDragOffsetY, 0, -(ptEvent.y - iDragOffsetY));
	                    break;
	                case Cursor.W_RESIZE_CURSOR:
	                    adjust(rectEventWindow, min, ptEvent.x - iDragOffsetX, 0, -(ptEvent.x - iDragOffsetX), 0);
	                    break;
	                case Cursor.NE_RESIZE_CURSOR:
	                    adjust
	                    (
	                    	rectEventWindow, 
	                    	min, 
	                    	0, 
	                    	ptEvent.y - iDragOffsetY, 
	                    	ptEvent.x + (iDragWidth - iDragOffsetX) - rectEventWindow.width, 
	                    	-(ptEvent.y - iDragOffsetY)
	                    );
	                    break;
	                case Cursor.SE_RESIZE_CURSOR:
	                    adjust
	                    (
	                    	rectEventWindow, 
	                    	min, 
	                    	0, 
	                    	0, 
	                    	ptEvent.x + (iDragWidth - iDragOffsetX) - rectEventWindow.width, 
	                    	ptEvent.y + (iDragHeight - iDragOffsetY) - rectEventWindow.height
	                    );
	                    break;
	                case Cursor.NW_RESIZE_CURSOR:
	                    adjust
	                    (
	                    	rectEventWindow, 
	                    	min, 
	                    	ptEvent.x - iDragOffsetX, 
	                    	ptEvent.y - iDragOffsetY, 
	                    	-(ptEvent.x - iDragOffsetX), 
	                    	-(ptEvent.y - iDragOffsetY)
	                    );
	                    break;
	                case Cursor.SW_RESIZE_CURSOR:
	                    adjust
	                    (
	                    	rectEventWindow, 
	                    	min, 
	                    	ptEvent.x - iDragOffsetX, 
	                    	0, 
	                    	-(ptEvent.x - iDragOffsetX), 
	                    	ptEvent.y + (iDragHeight - iDragOffsetY) - rectEventWindow.height
	                    );
	                    break;
	                default:
	                    break;
                }
                
                if (!rectEventWindow.equals(startBounds)) 
                {
                    winEvent.setBounds(rectEventWindow);
                    
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) 
                    {
                        winEvent.validate();
                        getRootPane().repaint();
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public void mouseEntered(MouseEvent pEvent) 
        {
            curLast = getWindow(pEvent).getCursor();
            
            mouseMoved(pEvent);
        }

        /**
         * {@inheritDoc}
         */
        public void mouseExited(MouseEvent pEvent) 
        {
        	getWindow(pEvent).setCursor(curLast);
        }

        /**
         * {@inheritDoc}
         */
        public void mouseClicked(MouseEvent ev) 
        {
            Component source = (Component)ev.getSource();
            
            Window winEvent = getWindow(ev);
            Frame frame = null;

            if (winEvent instanceof Frame) 
            {
                frame = (Frame)winEvent;
            } 
            else 
            {
                return;
            }

            //Resizing bei Doppelklick auf den Title
            
            Point convertedPoint = SwingUtilities.convertPoint(source, ev.getPoint(), getTitlePane());

            int state = frame.getExtendedState();
            
            if (getTitlePane() != null 
            	&& getTitlePane().contains(convertedPoint)) 
            {
                if ((ev.getClickCount() % 2) == 0 
                	 && ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) 
                {
                    if (frame.isResizable()) 
                    {
                        if ((state & Frame.MAXIMIZED_BOTH) != 0) 
                        {
                            frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                        }
                        else 
                        {
                            frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                        }
                        return;
                    }
                }
            }
        }

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Adjusts the bounds of the drag windows based on the mouse
         * position.
         * 
         * @param pBounds the adjustable window bounds
         * @param pMin the minimum size of the window
         * @param pDeltaX the delta for the x location 
         * @param pDeltaY the delta for the y location
         * @param pDeltaWidth the delta for the width
         * @param pDeltaHeight the delta for the height
         */
        private void adjust(Rectangle pBounds, 
        					Dimension pMin, 
        					int pDeltaX,
                            int pDeltaY, 
                            int pDeltaWidth, 
                            int pDeltaHeight) 
        {
            pBounds.x += pDeltaX;
            pBounds.y += pDeltaY;
            pBounds.width += pDeltaWidth;
            pBounds.height += pDeltaHeight;
            
            if (pMin != null) 
            {
                if (pBounds.width < pMin.width) 
                {
                    int iCorrection = pMin.width - pBounds.width;
                    
                    if (pDeltaX != 0) 
                    {
                        pBounds.x -= iCorrection;
                    }
                    
                    pBounds.width = pMin.width;
                }
                
                if (pBounds.height < pMin.height) 
                {
                    int iCorrection = pMin.height - pBounds.height;
                    
                    if (pDeltaY != 0) 
                    {
                        pBounds.y -= iCorrection;
                    }
                    
                    pBounds.height = pMin.height;
                }
            }
        }

        /**
         * Gets the corner for a specified point, if the point is in the corner.
         * 
         * @param pWindow the window for checking the location
         * @param pX the x location of the point
         * @param pY the y location of the point
         * @return the corner that contains the point <code>pX</code>,
         *         <code>pY</code>, or -1 if the position doesn't match a corner
         */
        private int calculateCorner(Window pWindow, int pX, int pY) 
        {
            Insets insWindow = pWindow.getInsets();
            
            int xPosition = calculatePosition(pX - insWindow.left, 
            		                          pWindow.getWidth() - insWindow.left - insWindow.right);
            int yPosition = calculatePosition(pY - insWindow.top,
                                              pWindow.getHeight() - insWindow.top - insWindow.bottom);

            if (xPosition == -1 || yPosition == -1) 
            {
                return -1;
            }
            
            return yPosition * 5 + xPosition;
        }

        /**
         * Gets the position of the spot in the width. 
         *
         * @param pSpot the spot location
         * @param pSize the width/height
         * @return an integer indicating the position of <code>spot</code>
         *         in <code>width</code>.
         *         <ul>
         *           <li>0 if < BORDER_DRAG_THICKNESS</li>
         *           <li>1 if < CORNER_DRAG_WIDTH</li>
         *           <li>2 if >= CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS</li>
         *           <li>3 if >= width - CORNER_DRAG_WIDTH</li>
         *           <li>4 if >= width - BORDER_DRAG_THICKNESS</li>
         *           <li>5 otherwise</li>
         *         </ul>
         */
        private int calculatePosition(int pSpot, int pSize) 
        {
            if (pSpot < insBorderSize.left) 
            {
                return 0;
            }
            
            if (pSpot < CORNER_DRAG_WIDTH) 
            {
                return 1;
            }
            
            if (pSpot >= (pSize - insBorderSize.right)) 
            {
                return 4;
            }
            
            if (pSpot >= (pSize - CORNER_DRAG_WIDTH)) 
            {
                return 3;
            }
            
            return 2;
        }

        /**
         * Detects the window where a <code>MouseEvent</code> occured.
         * 
         * @param pEvent the mouse event
         * @return the window
         */
        private Window getWindow(MouseEvent pEvent) 
        {
            Component source = (Component)pEvent.getSource();
            
            return source instanceof Window ? (Window)source : SwingUtilities.getWindowAncestor(source);
        }
        
        /**
         * Returns the Cursor to render for the specified corner.
         * 
         * @param pCorner the specified corner
         * @return 0 if the corner doesn't map to a valid Cursor otherwise the
         *         cursor for the specified corner
         */
        private int getCursor(int pCorner) 
        {
            if (pCorner == -1) 
            {
                return 0;
            }
            
            return CURSORMAPPING[pCorner];
        }
        
    }	// InternalMouseInputListener
    
}	// SmartRootPaneUI
