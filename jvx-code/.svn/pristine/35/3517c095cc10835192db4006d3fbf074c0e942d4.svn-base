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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartPainter;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartRootTitlePaneBorder;


/**
 * Class that manages a JLF awt.Window-descendant class's title bar.  
 * <p>
 * This class assumes it will be created with a particular window
 * decoration style, and that if the style changes, a new one will
 * be created.
 *
 * @author René Jahn
 */
public class SmartRootTitlePane extends JComponent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the border for the title pane. */
	private static Border border = new SmartRootTitlePaneBorder();
	
    /** PropertyChangeListener added to the JRootPane. */
    private PropertyChangeListener pclListener;

    /** JMenuBar, typically renders the system menu items. */
    private JMenuBar menuBar;
    
    /** Action used to close the Window. */
    private Action actionClose;

    /** Action used to iconify the Frame. */
    private Action actionIconify;

    /** Action to restore the Frame size. */
    private Action actionRestore;

    /** Action to restore the Frame size. */
    private Action actionMaximize;

    /** Button used to maximize or restore the Frame. */
    private JButton butMaximize;

    /** Button used to maximize or restore the Frame. */
    private JButton butIconify;

    /** Button used to maximize or restore the Frame. */
    private JButton butClose;

    /** Image used for the system menu icon. */
    private Image icoSystemMenu;

    /** the insets of the title pane. */
    private Insets insets;
    
    /**
     * Listens for changes in the state of the Window listener to update
     * the state of the widgets.
     */
    private WindowListener wlListener;

    /** Window we're currently in. */
    private Window window;

    /** JRootPane rendering for. */
    private JRootPane rootPane;

    /**
     * Buffered Frame.state property. As state isn't bound, this is kept
     * to determine when to avoid updating widgets.
     */
    private int state;
    
    /** SmartRootPaneUI that created us. */
    private SmartRootPaneUI rootPaneUI;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>SmartRootTitlePane</code> for a specified 
     * <code>JRootPane</code> and <code>SmartRootPaneUI</code>.
     * 
     * @param pRoot the root pane
     * @param pUI the root pane UI
     */
    public SmartRootTitlePane(JRootPane pRoot, SmartRootPaneUI pUI) 
    {
        rootPane = pRoot;
        rootPaneUI = pUI;

        state = -1;

        installSubcomponents();
        
        installDefaults();

        setLayout(createLayout());
        
        insets = SynthLookAndFeel.getStyle(pRoot, Region.INTERNAL_FRAME_TITLE_PANE).getInsets(null, null);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() 
    {
        super.addNotify();

        uninstallListeners();

        window = SwingUtilities.getWindowAncestor(this);
        if (window != null) 
        {
            if (window instanceof Frame) 
            {
                setState(((Frame)window).getExtendedState());
            }
            else 
            {
                setState(0);
            }
            
            setActive(window.isActive());
            installListeners();
            updateSystemIcon();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() 
    {
        super.removeNotify();

        uninstallListeners();
        window = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getInsets()
    {
    	return insets;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    /**
//     * Uninstalls the necessary state.
//     */
//Not needed: If the style changes, a new one will be created.    
//    private void uninstall() 
//    {
//        uninstallListeners();
//        window = null;
//        removeAll();
//    }


//    /**
//	 * Uninstalls any previously installed UI values.
//	 */
//Not needed: If the style changes, a new one will be created.    
//	private void uninstallDefaults() 
//	{
//		setBorder(null);
//	}

    /**
     * Installs the necessary listeners.
     */
    private void installListeners() 
    {
        if (window != null) 
        {
            wlListener = createWindowListener();
            
            window.addWindowListener(wlListener);
            
            pclListener = createWindowPropertyChangeListener();
            
            window.addPropertyChangeListener(pclListener);
        }
    }

    /**
     * Uninstalls the necessary listeners.
     */
    private void uninstallListeners() 
    {
        if (window != null) 
        {
            window.removeWindowListener(wlListener);
            window.removePropertyChangeListener(pclListener);
        }
    }

    /**
     * Creates a new instance of <code>WindowHandler</code>.
     * 
     * @return the window listener/handler
     */
    private WindowListener createWindowListener() 
    {
        return new WindowHandler();
    }

    /**
     * Creates a new instance of <code>PropertyChangeHandler</code>.
     * 
     * @return the property change listener/handler
     */
    private PropertyChangeListener createWindowPropertyChangeListener() 
    {
        return new PropertyChangeHandler();
    }

    /**
     * Returns the <code>JRootPane</code> this was created for.
     * 
     * @return the root pane
     */
    public JRootPane getRootPane() 
    {
        return rootPane;
    }

    /**
     * Returns the window decoration style of the <code>JRootPane</code>.
     * 
     * @return the decoration style of the root pane
     * @see JRootPane#getWindowDecorationStyle()
     */
    private int getWindowDecorationStyle() 
    {
        return getRootPane().getWindowDecorationStyle();
    }

    /**
     * Adds any sub-Components contained in the <code>SmartRootTitlePane</code>.
     */
    private void installSubcomponents() 
    {
        int decorationStyle = getWindowDecorationStyle();
        
        if (decorationStyle == JRootPane.FRAME) 
        {
            createActions();
            
            menuBar = createMenuBar();
            
            add(menuBar);
            
            createButtons();
            
            add(butIconify);
            add(butMaximize);
            add(butClose);
        } 
        else if (decorationStyle == JRootPane.PLAIN_DIALOG 
        		 || decorationStyle == JRootPane.INFORMATION_DIALOG 
        		 || decorationStyle == JRootPane.ERROR_DIALOG 
        		 || decorationStyle == JRootPane.COLOR_CHOOSER_DIALOG 
        		 || decorationStyle == JRootPane.FILE_CHOOSER_DIALOG 
        		 || decorationStyle == JRootPane.QUESTION_DIALOG 
        		 || decorationStyle == JRootPane.WARNING_DIALOG) 
        {
            createActions();
            
            createButtons();
            
            add(butClose);
        }
    }

    /**
     * Installs the fonts and necessary properties on the MetalTitlePane.
     */
    private void installDefaults() 
    {
        setFont(SmartTheme.FONT_INTFRAME_TITLE);
        
        setBorder(border);
    }
    
    /**
     * Creats the <code>JMenuBar</code>.
     * 
     * @return the menu bar displaying the appropriate system menu items.
     */
    protected JMenuBar createMenuBar() 
    {
        menuBar = new SystemMenuBar();
        
        menuBar.setFocusable(false);
        menuBar.setBorderPainted(true);
        menuBar.add(createMenu());
        
        return menuBar;
    }

    /**
     * Closes the Window.
     */
    private void close() 
    {
        Window winClose = getWindow();

        if (winClose != null) 
        {
            winClose.dispatchEvent(new WindowEvent(winClose, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the frame.
     */
    private void iconify() 
    {
        Frame frIconify = getFrame();
        
        if (frIconify != null) 
        {
            frIconify.setExtendedState(state | Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the frame.
     */
    private void maximize() 
    {
        Frame frMax = getFrame();
        
        if (frMax != null) 
        {
            frMax.setExtendedState(state | Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Restores the frame size.
     */
    private void restore() 
    {
        Frame frRestore = getFrame();

        if (frRestore == null) 
        {
            return;
        }

        if ((state & Frame.ICONIFIED) != 0) 
        {
            frRestore.setExtendedState(state & ~Frame.ICONIFIED);
        } 
        else 
        {
            frRestore.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Create the <code>Action</code>s that get associated with the
     * buttons and menu items.
     */
    private void createActions() 
    {
        actionClose = new CloseAction();
        
        if (getWindowDecorationStyle() == JRootPane.FRAME) 
        {
            actionIconify = new IconifyAction();
            actionRestore = new RestoreAction();
            actionMaximize = new MaximizeAction();
        }
    }

    /**
     * Creates the <code>JMenu</code> displaying the appropriate menu items
     * for manipulating the frame.
     * 
     * @return the menu
     */
    private JMenu createMenu() 
    {
        JMenu menu = new JMenu("");
        
        if (getWindowDecorationStyle() == JRootPane.FRAME) 
        {
            addMenuItems(menu);
        }
        
        return menu;
    }

    /**
     * Adds the necessary <code>JMenuItem</code>s to the passed in menu.
     * 
     * @param pMenu the menu
     */
    private void addMenuItems(JMenu pMenu) 
    {
        JMenuItem mi = pMenu.add(actionRestore);
        
        int mnemonic = getInt("InternalFrameTitlePane.restoreMnemonic", -1);

        if (mnemonic != -1) 
        {
            mi.setMnemonic(mnemonic);
        }

        mi = pMenu.add(actionIconify);
        
        mnemonic = getInt("InternalFrameTitlePane.minimizeMnemonic", -1);
        if (mnemonic != -1) 
        {
            mi.setMnemonic(mnemonic);
        }

        if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) 
        {
            mi = pMenu.add(actionMaximize);
            
            mnemonic = getInt("InternalFrameTitlePane.maximizeMnemonic", -1);
            if (mnemonic != -1) 
            {
                mi.setMnemonic(mnemonic);
            }
        }

        pMenu.add(new JSeparator());

        mi = pMenu.add(actionClose);
        
        mnemonic = getInt("InternalFrameTitlePane.closeMnemonic", -1);
        if (mnemonic != -1) 
        {
            mi.setMnemonic(mnemonic);
        }
    }
    
    /**
     * Gets an int valuf out of the UI defaults.
     * 
     * @param key the mapped name for the value
     * @param defaultValue the default value if the value is not a number
     * @return the value as number or the <code>defaultValue</code> if the value is not a number
     * @see UIManager#get(Object, java.util.Locale)
     */
    private int getInt(Object key, int defaultValue) 
    {
        Object oValue = UIManager.get(key, getRootPane().getLocale());
        
        if (oValue instanceof Integer) 
        {            
        	return ((Integer)oValue).intValue();
        }
        
        if (oValue instanceof String) 
        {
            try 
            {
                return Integer.parseInt((String)oValue);
            } 
            catch (NumberFormatException nfe) 
            {
            	//-> defaultValue
            }
        }
        
        return defaultValue;
    }    

    /**
     * Creates a <code>JButton</code> appropriate for placement on the
     * TitlePane.
     * 
     * @param pName the buttons name
     * @param pAction the default button action
     * @return a button for the title
     */
    private JButton createTitleButton(String pName, Action pAction) 
    {
        JButton button = new JButton();

        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setName(pName);
        button.setAction(pAction);
        button.setText(null);
        
        
        return button;
    }

    /**
     * Creates the Buttons that will be placed on the TitlePane.
     */
    private void createButtons() 
    {
        butClose = createTitleButton("SmartRootTitlePane.closeButton", actionClose);
        
        //bei Metal hilft das zB bei Webstart applets um gewisse Properties via System.getProperty
        //abfragen zu dürfen. Normalerweise hat man access$0 und dadurch schafft es sun den access$400
        //zu bekommen -> was solls!
        butClose.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Close");
        butClose.getAccessibleContext().setAccessibleName("Close");

        if (getWindowDecorationStyle() == JRootPane.FRAME) 
        {
            butIconify = createTitleButton("SmartRootTitlePane.iconifyButton", actionIconify);
            butIconify.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Iconify");
            butIconify.getAccessibleContext().setAccessibleName("Iconify");
            
            butMaximize = createTitleButton("SmartRootTitlePane.maximizeButton", actionRestore);
            butMaximize.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Maximize");            
            butMaximize.getAccessibleContext().setAccessibleName("Maximize");
        }
    }

    /**
     * Creates the layout for this title pane.
     * 
     * @return the <code>LayoutManager</code> that should be installed on
     * the <code>SmartRootTitlePane</code>.
     */
    private LayoutManager createLayout() 
    {
        return new InternLayout();
    }

    /**
     * Updates state dependant upon the windows active state.
     * 
     * @param pActive the windows state
     */
    private void setActive(boolean pActive)
    {
        Boolean bActive = pActive ? Boolean.TRUE : Boolean.FALSE;

        butClose.putClientProperty("paintActive", bActive);
        if (getWindowDecorationStyle() == JRootPane.FRAME) 
        {
            butIconify.putClientProperty("paintActive", bActive);
            butMaximize.putClientProperty("paintActive", bActive);
        }
        
        // Repaint the whole thing as the Borders that are used have
        // different colors for active vs inactive
        getRootPane().repaint();
    }

    /**
     * Sets the state of the Window and update the window only if the
     * state has changed.
     * 
     * @param pState the state as bitmask
     * @see Frame#setExtendedState(int)
     */
    private void setState(int pState) 
    {
        setState(pState, false);
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is
     * true and the state has not changed, this will update anyway.
     * 
     * @param pState the state as bitmask
     * @param pUpdateRegardless <code>true</code> to update anyway regardless of the state
     */
    private void setState(int pState, boolean pUpdateRegardless) 
    {
        Window winParent = getWindow();

        //nur für Frames relevant!
        if (winParent != null && getWindowDecorationStyle() == JRootPane.FRAME) 
        {
            if (this.state == pState && !pUpdateRegardless) 
            {
                return;
            }
            
            Frame frParent = getFrame();

            if (frParent != null) 
            {
                JRootPane rpParent = getRootPane();

                if (((pState & Frame.MAXIMIZED_BOTH) != 0) 
                	  && (rpParent.getBorder() == null 
                		  || (rpParent.getBorder() instanceof UIResource)) 
                	  && frParent.isShowing()) 
                {
                    rpParent.setBorder(null);
                }
                else if ((pState & Frame.MAXIMIZED_BOTH) == 0) 
                {
                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    rootPaneUI.installBorder(rpParent);
                }
                if (frParent.isResizable()) 
                {
                    if ((pState & Frame.MAXIMIZED_BOTH) != 0) 
                    {
                        butMaximize.setAction(actionRestore);
                        butMaximize.setText(null);
                        actionMaximize.setEnabled(false);
                        actionRestore.setEnabled(true);
                    }
                    else 
                    {
                        butMaximize.setAction(actionMaximize);
                        butMaximize.setText(null);
                        actionMaximize.setEnabled(true);
                        actionRestore.setEnabled(false);
                    }
                    if (butMaximize.getParent() == null 
                    	|| butIconify.getParent() == null) 
                    {
                        add(butMaximize);
                        add(butIconify);
                        revalidate();
                        repaint();
                    }
                }
                else 
                {
                    actionMaximize.setEnabled(false);
                    actionRestore.setEnabled(false);
                    
                    if (butMaximize.getParent() != null) 
                    {
                        remove(butMaximize);
                        revalidate();
                        repaint();
                    }
                }
            }
            else 
            {
                // es handelt sich um keinen Frame -> nur close ist verfügbar
                actionMaximize.setEnabled(false);
                actionRestore.setEnabled(false);
                actionIconify.setEnabled(false);
                
                remove(butMaximize);
                remove(butIconify);
                
                revalidate();
                repaint();
            }
            
            actionClose.setEnabled(true);
            
            this.state = pState;
        }
    }

    /**
     * Gets the Frame rendering in. This will return null if the
     * <code>JRootPane</code> is not contained in a <code>Frame</code>.
     * 
     * @return the frame or <code>null</code> if the pane is not contained in a frame.
     */
    private Frame getFrame() 
    {
        Window winParent = getWindow();

        if (winParent instanceof Frame) 
        {
            return (Frame)winParent;
        }
        
        return null;
    }

    /**
     * Gets the <code>Window</code> the <code>JRootPane</code> is
     * contained in. This will return null if there is no parent ancestor
     * of the <code>JRootPane</code>.
     * 
     * @return the window or <code>null</code> if there is no parent ancestor of the 
     *         pane
     */
    public Window getWindow() 
    {
        return window;
    }

    /**
     * Gets the string to display as the title.
     * 
     * @return the title string or <code>null</code> if there is no parent ancestor of the 
     *         pane
     */
    private String getTitle() 
    {
        Window winParent = getWindow();

        if (winParent instanceof Frame) 
        {
            return ((Frame)winParent).getTitle();
        }
        else if (winParent instanceof Dialog) 
        {
            return ((Dialog)winParent).getTitle();
        }
        
        return null;
    }

    /**
     * Renders the title pane.
     * 
     * @param pGraphics the graphics context
     */
    public void paintComponent(Graphics pGraphics)  
    {
        //Vorsicht: Ein ähnlicher Code wir auch in SmartPainter.paintNorthPaneTitle verwendet!
    	String sTitle = getTitle();
    	
    	if (sTitle != null)
    	{
	        // As state isn't bound, we need a convenience place to check
	        // if it has changed. Changing the state typically changes the
	        if (getFrame() != null) 
	        {
	            setState(getFrame().getExtendedState());
	        }
	        
	        JRootPane rpParent  = getRootPane();
	        Window    winParent = getWindow();
	        
	        JButton butLastVisible = null;
	        
	        boolean bLeftToRight = SmartLookAndFeel.isLeftToRightOrientation(winParent == null ? rpParent : winParent);
	        boolean bActive = (winParent == null) ? true : winParent.isActive();
	        
	        int iWidth = getWidth();
	        int iHeight = getHeight();
	
	        Color background;
	        Color foreground;
	
	
	        if (bActive) 
	        {
	        	if (winParent instanceof Dialog && ((Dialog)winParent).isModal())
	        	{
        			background = SmartTheme.COL_INTFRAME_MODAL_TITLE_BACKGROUND_ACTIVE;
	        	}
	        	else
	        	{
	        		background = SmartTheme.COL_INTFRAME_TITLE_BACKGROUND_ACTIVE;
	        	}
	        	
	            foreground = SmartTheme.COL_INTFRAME_TITLE_ACTIVE;
	        } 
	        else 
	        {
	            background = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
	            foreground = SmartTheme.COL_INTFRAME_TITLE_INACTIVE;
	        }
	
	        //Hintergrund-Balken
	        SmartPainter.paintRectangle(pGraphics, 0, 0, iWidth, iHeight - 1, 0, -1, background, null, -1, null);
	
	        if (butIconify != null && butIconify.isVisible())
	        {
	        	butLastVisible = butIconify;
	        }
	        else if (butMaximize != null && butMaximize.isVisible())
	        {
	        	butLastVisible = butMaximize;
	        }
	        else if (butClose != null && butClose.isVisible())
	        {
	        	butLastVisible = butClose;
	        }
	
	        int iMinX;
	        int iMaxX;
			int iTitleAlignment = SmartTheme.ALIGN_INTFRAME_TITLE;
	
			//MIN, MAX Grenze ermitteln (abhängig von den Buttons und der Ausrichtung)
	        if (bLeftToRight)
	        {
				if (butLastVisible != null) 
			    {
			        iMaxX = butLastVisible.getX() - SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			    }
			    else 
			    {
			        iMaxX = iWidth - insets.right;
			    }
			    
				iMinX  = insets.left;
	        	
		        if (getWindowDecorationStyle() == JRootPane.FRAME && icoSystemMenu != null) 
		        {
		        	int iWidthImage = icoSystemMenu.getWidth(null);
		        	
		            iMinX += iWidthImage + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
		        }
	        }
	        else
	        {
				if (butLastVisible != null) 
			    {
				    iMinX = butLastVisible.getX() + butLastVisible.getWidth() + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			    }
			    else 
			    {
			        iMinX = insets.left;
			    }
	        	
	        	iMaxX = iWidth - insets.right;
	        	
		        if (getWindowDecorationStyle() == JRootPane.FRAME && icoSystemMenu != null) 
		        {
		        	int iWidthImage = icoSystemMenu.getWidth(null);
		        	
		        	iMaxX -= iWidthImage + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
		        }
		        
			    if (iTitleAlignment == SwingConstants.LEADING) 
			    {
			        iTitleAlignment = SwingConstants.TRAILING;
			    }
			    else if (iTitleAlignment == SwingConstants.TRAILING) 
			    {
			        iTitleAlignment = SwingConstants.LEADING;
			    }
	        }
			
			// Textausgabe
			FontMetrics fm = pGraphics.getFontMetrics(getFont());
			
	        int baseline = (iHeight + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
			
			String sClippedTitle = SmartPainter.clipTitle(sTitle, iMinX, iMaxX, fm);
			
			int iX;	
	        
			if (iTitleAlignment == SwingConstants.CENTER) 
	        {
	            int iTitleWidth = fm.stringWidth(sClippedTitle);
	            
	            iX = (iWidth - iTitleWidth) / 2;
	        	//Zentrierung, solange der Text sich in den Grenzen befindet, andernfalls
	            //links oder rechts Ausrichtung durchführen!
	            //
	            //Der Text MUSS immer innerhalb der min und max Position dargestellt werden. 
	            if (bLeftToRight)
	            {
		            if (iX > iMaxX - iTitleWidth)
		            {
		            	iX = iMaxX - iTitleWidth;
		            }
		            
		            if (iX < iMinX)
		            {
		            	iX = iMinX;
		            }
	            }
	            else
	            {
	            	if (iX + iTitleWidth > iMaxX)
	            	{
	            		iX = iMaxX - iTitleWidth;
	            	}
	            	
	            	if (iX < iMinX)
	            	{
	            		iX = iMinX;
	            	}
	            }
	        }
	        else 
	        {
				if (bLeftToRight)
				{
					iX = iMinX;
				}
				else
				{
					iX = iMaxX - fm.stringWidth(sClippedTitle);
				}
	        }
	        
			pGraphics.setColor(foreground);
	
			//der letzte Parameter MUSS -2 sein, da -1 im SmartGraphicsUtils abgefangen wird
			//-1 wird ab 1.6 verwendet, damit der Title vom Smart/LF gemalt wird ist -2 nötig!
			pGraphics.drawString(sClippedTitle, iX, baseline);
    	}
    }
    
    /**
     * Update the image used for the system icon.
     */
    private void updateSystemIcon() 
    {
        Frame frParent = getFrame();
        
        if (frParent == null) 
        {
            icoSystemMenu = null;
            
            return;
        }

        icoSystemMenu = frParent.getIconImage();
    }

	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * The <code>CloseAction</code> is used to <code>close</code> the <code>Window</code>.
     * 
     * @author René Jahn
     */
    private class CloseAction extends AbstractAction
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates a new instance of <code>CloseAction</code>.
    	 */
        public CloseAction() 
        {
            super(UIManager.getString("InternalFrameTitlePane.closeButtonText", getLocale()));
        }

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent pEvent) 
        {
            close();
        }      
        
    }	// CloseAction

    /**
     * The <code>IconifyAction</code> is used to <code>iconfiy</code> the <code>Frame</code>.
     * 
     * @author René Jahn
     */
    private class IconifyAction extends AbstractAction 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates a new instance of <code>IconifyAction</code>.
    	 */
    	public IconifyAction() 
        {
            super(UIManager.getString("InternalFrameTitlePane.minimizeButtonText", getLocale()));
        }

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent pEvent) 
        {
            iconify();
        }
        
    }	// IconifyAction 


    /**
     * The <code>RestoreAction</code> is used to <code>restore</code> the <code>Frame</code>.
     * 
     * @author René Jahn
     */
    private class RestoreAction extends AbstractAction 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates a new instance of <code>RestoreAction</code>.
    	 */
        public RestoreAction() 
        {
            super(UIManager.getString("InternalFrameTitlePane.restoreButtonText", getLocale()));
        }

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent pEvent) 
        {
            restore();
        }
        
    }	// RestoreAction


    /**
     * The <code>MaximizeAction</code> is used to <code>maximize</code> the <code>Frame</code>.
     * 
     * @author René Jahn
     */
    private class MaximizeAction extends AbstractAction 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates a new instance of <code>MaximizeAction</code>.
    	 */
        public MaximizeAction() 
        {
            super(UIManager.getString("InternalFrameTitlePane.maximizeButtonText", getLocale()));
        }

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent pEvent) 
        {
            maximize();
        }
        
    }	// MaximizeAction


    /**
     * The <code>SystemMenuBar</code> is responsible for drawing the system menu. 
     * Looks up the image to draw from the Frame associated with the <code>JRootPane</code>.
     * 
     * @author René Jahn
     */
    private class SystemMenuBar extends JMenuBar 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public void paint(Graphics pGraphics) 
        {
            if (icoSystemMenu != null) 
            {
                pGraphics.drawImage(icoSystemMenu, 
                		            SmartLookAndFeel.isLeftToRightOrientation(rootPane) ? insets.left : insets.right, 
                		            (getHeight() - insets.bottom - icoSystemMenu.getHeight(null)) / 2, 
                		            null);
            } 
        }
    	
    	/**
    	 * {@inheritDoc}
    	 * @see #getPreferredSize()
    	 */
    	@Override
        public Dimension getMinimumSize() 
    	{
            return getPreferredSize();
        }
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public Dimension getPreferredSize() 
    	{
            Dimension dimPreferred = super.getPreferredSize();

            return new Dimension(Math.max(SmartTheme.SIZE_INTFRAME_BUTTONS, dimPreferred.width),
                                 Math.max(dimPreferred.height, SmartTheme.SIZE_INTFRAME_BUTTONS));
        }
    	
    }	// SystemMenuBar

    /**
     * The <code>InternLayout</code> is the layout manager for the pane.
     * 
     * @author René Jahn
     */
    private class InternLayout implements LayoutManager 
    {  
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
        public Dimension preferredLayoutSize(Container pContainer)  
        {
            FontMetrics fm = rootPane.getFontMetrics(getFont());

            int iHeight = Math.max(fm.getHeight(), SmartTheme.SIZE_INTFRAME_BUTTONS) + insets.top + insets.bottom;

            int iWidth = insets.left + insets.right;
            int iButtonCount = 0;
            
	        if (butIconify != null && butIconify.isVisible())
	        {
	        	iButtonCount++;
	        }
	        
	        if (butMaximize != null && butMaximize.isVisible())
	        {
	        	iButtonCount++;
	        }
	        
	        if (butClose != null && butClose.isVisible())
	        {
	        	iButtonCount++;
	        }
            
	        //Abstände zwischen den Buttons berücksichtigen
	        iWidth += iButtonCount * SmartTheme.SIZE_INTFRAME_BUTTONS + (iButtonCount - 1) * SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
	        
	        if (icoSystemMenu != null)
	        {
	        	iWidth += icoSystemMenu.getWidth(null);
	        }
	        
	        String sTitle = getTitle();
	        if (sTitle != null)
	        {
	        	//15px. Breite (Annahme)
	        	iWidth += Math.min(15, fm.stringWidth(sTitle));
	        	
	        	if (iButtonCount > 0)
	        	{
	        		iWidth += SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
	        	}
	        	
	        	if (icoSystemMenu != null)
	        	{
	        		iWidth += SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
	        	}
	        }
            
            return new Dimension(iWidth, iHeight);
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public Dimension minimumLayoutSize(Container pContainer)
        {
            return preferredLayoutSize(pContainer);
        } 
                    
    	/**
    	 * {@inheritDoc}
    	 */
        public void layoutContainer(Container pContainer) 
        {
        	boolean bLeftToRight = SmartLookAndFeel.isLeftToRightOrientation(window != null ? window : rootPane);
            int iWidth = getWidth();
            int iX; 
            int iY = insets.top;
            int spacing;
            int iHeightButton = SmartTheme.SIZE_INTFRAME_BUTTONS; 
            int iWidthButton  = SmartTheme.SIZE_INTFRAME_BUTTONS;
            
                
            iX = bLeftToRight ? 0 : iWidth - iWidthButton;
            
            if (menuBar != null) 
            {
                menuBar.setBounds(iX - (bLeftToRight ? 0 : insets.right), 
                		          iY, 
                		          (icoSystemMenu != null ? icoSystemMenu.getWidth(null) : 0) + (bLeftToRight ? insets.left : insets.right), 
                		          iHeightButton + insets.bottom);
            }

            iX = bLeftToRight ? iWidth - insets.right - iWidthButton : insets.left;
            
            if (butClose != null) 
            {
                butClose.setBounds(iX, iY, iWidthButton, iHeightButton);
            }

            if (!bLeftToRight) 
            {
            	iX += iWidthButton;
            }

            if (getWindowDecorationStyle() == JRootPane.FRAME) 
            {
                if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) 
                {
                    if (butMaximize.getParent() != null) 
                    {
                        spacing = SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
                        
                        iX += bLeftToRight ? -spacing - iWidthButton : spacing;
                        
                        butMaximize.setBounds(iX, iY, iWidthButton, iHeightButton);
                        
                        if (!bLeftToRight) 
                        {
                            iX += iWidthButton;
                        }
                    }
                }

                if (butIconify != null && butIconify.getParent() != null) 
                {
                    spacing = SmartTheme.SPACE_INTFRAME_TITLE_BUTTONS;
                    
                    iX += bLeftToRight ? -spacing - iWidthButton : spacing;
                    
                    butIconify.setBounds(iX, iY, iWidthButton, iHeightButton);
                    
                    if (!bLeftToRight) 
                    {
                        iX += iWidthButton;
                    }
                }
            }
        }
        
    }	// InternLayout

    /**
     * The <code>PropertyChangeHandler</code> updates the necessary state
     * as the state of the window changes. It will be installed on the 
     * window.
     * 
     * @author René Jahn
     */
    private class PropertyChangeHandler implements PropertyChangeListener 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
        public void propertyChange(PropertyChangeEvent pEvent) 
        {
            String name = pEvent.getPropertyName();

            // Frame.state isn't currently bound.
            if ("resizable".equals(name) || "state".equals(name)) 
            {
                Frame frParent = getFrame();

                if (frParent != null) 
                {
                    setState(frParent.getExtendedState(), true);
                }
                if ("resizable".equals(name)) 
                {
                    getRootPane().repaint();
                }
            }
            else if ("title".equals(name)) 
            {
                repaint();
            }
            else if ("componentOrientation" == name) 
            {
                revalidate();
                repaint();
            }
            else if ("iconImage" == name) 
            {
                updateSystemIcon();
                revalidate();
                repaint();
            }
        }
    }

    /**
     * The <code>WindowHandler</code> will be installed on the ancestor window and
     * updates the state if necessary.
     * 
     * @author René Jahn
     */
    private class WindowHandler extends WindowAdapter 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public void windowActivated(WindowEvent ev) 
        {
            setActive(true);
        }

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
        public void windowDeactivated(WindowEvent ev) 
        {
            setActive(false);
        }
        
    }	// WindowHandler
    
}	// SmartRootTitlePane  

