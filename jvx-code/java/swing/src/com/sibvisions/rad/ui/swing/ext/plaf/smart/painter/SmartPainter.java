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
 * 14.10.2008 - [JR] - Toolbar background LaF switching support
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartRootTitlePane;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;

/**
 * The <code>SmartPainter</code> is the main class for drawing the surface.
 * It paints the border and background of all components.
 * 
 * @author René Jahn
 */
public final class SmartPainter extends SynthPainter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the roundness of the tab corner. */
	private static final int TABCORNER = 4;
	
	/** the space between the border and the button-symbol of buttons in the title pane. */
	private static final int SPACE_TITLEBUTTONS = 4;

	
	/** the type for line arrows (e.g. ScrollBar Buttons). */
	private static final int TYPE_ARROW_LINES = 0;
	/** the type for filled arrows (e.g. SplitPane Buttons). */
	private static final int TYPE_ARROW_FILLED = 1;
	
	/** the direction of a vertical gradient. */
	private static final int GRADIENT_VERTICAL = 0;
	/** the direction of a horizontal gradient. */
	private static final int GRADIENT_HORIZONTAL = 1;
	
	/** the close symbol identifier. */
	private static final int SYMBOL_CLOSE 	 = 0;
	/** the maximize symbol identifier. */
	private static final int SYMBOL_MAXIMIZE = 1;
	/** the minimize symbol identifier. */
	private static final int SYMBOL_MINIMIZE = 2;
	/** the restore symbol identifier. */
	private static final int SYMBOL_RESTORE  = 3;
	
	/** the singleton instance of the <code>SmartPainter</code>. */
    private static SynthPainter instance;
    
    /** the cache for used gradient images. */
	private static Hashtable<String, Image> htGradientImageCache = null;
	
	/** the cache for menu arrows. */
	//needed to change the color of the arrow
	private static Hashtable<JMenu, ImageIcon> htMenuArrowCache = new Hashtable<JMenu, ImageIcon>();
	
	/** the cache for menuitem icons, like radio button or checkbox. */
	private static Hashtable<String, ImageIcon> htMenuIconCache = new Hashtable<String, ImageIcon>();	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>SmartPainter</code> is a singleton.
	 * 
	 * @see #getInstance()
	 */
    public SmartPainter()
    {
    }

    /**
     * Creates a singleton instance of the <code>SmartPainter</code>.
     * 
     * @return the singleton instance
     */
    public static SynthPainter getInstance()
    {
        if (instance == null)
        {
            instance = new SmartPainter();
        }
        
        return instance;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Internal Frame
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintDesktopIconBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JInternalFrame frame = SmartLookAndFeel.getInternalFrame(pContext);
    	
    	Color color;
    	
    	if (frame.isSelected())
    	{
    		color = SmartTheme.COL_INTFRAME_OUTER_BORDER_ACTIVE;
    	}
    	else
    	{
    		color = SmartTheme.COL_INTFRAME_OUTER_BORDER_INACTIVE;
    	}

    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintDesktopIconBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JInternalFrame frame = SmartLookAndFeel.getInternalFrame(pContext);
    	
    	Color color;
    	
    	if (frame.isSelected())
    	{
    		color = SmartTheme.COL_INTFRAME_TITLE_BACKGROUND_ACTIVE;
    	}
    	else
    	{
    		color = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, color, null, -1, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintInternalFrameTitlePaneBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//An dieser Stelle nicht auf SmartLookAndFeel.getInternalFrame zurückgreifen,
    	//da der Border im iconified Zustand nicht gemalt wird!
    	Component conParent = pContext.getComponent().getParent();
    	
    	if (conParent instanceof JInternalFrame)
    	{
    		Color color;
    		
	    	if (((JInternalFrame)conParent).isSelected())
	    	{
        		color = SmartTheme.COL_INTFRAME_INNER_BORDER_ACTIVE;
	    	}
	    	else
	    	{
        		color = SmartTheme.COL_INTFRAME_INNER_BORDER_INACTIVE;
	    	}
	    	
	    	pGraphics.setColor(color);
	    	pGraphics.drawLine(pX, pY + pHeight - 1, pX + pWidth - 1, pY + pHeight - 1);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintInternalFrameTitlePaneBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	BasicInternalFrameTitlePane titlePane = (BasicInternalFrameTitlePane)pContext.getComponent();
    	
    	JInternalFrame frame = SmartLookAndFeel.getInternalFrame(pContext);
    	
   		//Wenn iconified, dann nichts malen!
    	if (titlePane.getParent() instanceof JInternalFrame)
    	{
       		Color color;
    		
	    	if (frame.isSelected())
	    	{
	    		Boolean bModal = (Boolean)frame.getClientProperty("JVxInternalFrame.intern_modal"); 
	    		
	    		if (bModal != null && bModal.booleanValue())
	    		{
		    		color = SmartTheme.COL_INTFRAME_MODAL_TITLE_BACKGROUND_ACTIVE;
	    		}
	    		else
	    		{
	    			color = SmartTheme.COL_INTFRAME_TITLE_BACKGROUND_ACTIVE;
	    		}
	    	}
	    	else
	    	{
	    		color = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
	    	}
	    	
	    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight - 1, 0, -1, color, null, -1, null);
    	}
    	
    	//Die Text-Darstellung wird vom Smart/LF übernommen und nicht vom Synth/LF
    	//Der Title wird immer hier und nicht im paintDesktopIconBackground gezeichnet,
    	//weil dort die TitlePane ermittelt werden müsste 
    	paintNorthPaneTitle(pContext, frame, titlePane, pGraphics, pWidth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintInternalFrameBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JInternalFrame frame = SmartLookAndFeel.getInternalFrame(pContext);
    	
    	Color color;
    	
    	if (frame.isSelected())
    	{
    		Boolean bModal = (Boolean)frame.getClientProperty("JVxInternalFrame.intern_modal"); 
    		
    		if (bModal != null && bModal.booleanValue())
    		{
	    		color = SmartTheme.COL_INTFRAME_MODAL_BACKGROUND_ACTIVE;
    		}
    		else
    		{
	    		color = SmartTheme.COL_INTFRAME_BACKGROUND_ACTIVE;
    		}
    	}
    	else
    	{
    		color = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, color, null, -1, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void paintInternalFrameBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JInternalFrame frame = SmartLookAndFeel.getInternalFrame(pContext);
    	
    	Color colorOuterBorder;
    	Color colorInnerBorder;
    	
    	if (frame.isSelected())
    	{
    		colorOuterBorder = SmartTheme.COL_INTFRAME_OUTER_BORDER_ACTIVE;
    		colorInnerBorder = SmartTheme.COL_INTFRAME_INNER_BORDER_ACTIVE;
    	}
    	else
    	{
    		colorOuterBorder = SmartTheme.COL_INTFRAME_OUTER_BORDER_INACTIVE;
    		colorInnerBorder = SmartTheme.COL_INTFRAME_INNER_BORDER_INACTIVE;
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, colorOuterBorder);
    	
    	BasicInternalFrameTitlePane title = SmartLookAndFeel.getNorthPane(frame);
    	
    	int iTitleHeight = title.getHeight();
    	
    	Insets insFrame = frame.getInsets();
    	
    	pGraphics.setColor(colorInnerBorder);
    	pGraphics.drawRect
    	(
    		pX + insFrame.left - 1, pY + insFrame.top + iTitleHeight - 1, 
    		pWidth - insFrame.left - insFrame.right + 1, pHeight - insFrame.top - iTitleHeight - insFrame.bottom + 1
    	);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintDesktopPaneBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
		pGraphics.setColor(pContext.getComponent().getBackground());
		pGraphics.fillRect(pX, pY, pWidth, pHeight);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Panel
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintPanelBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintPanelBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
        JPanel panel = (JPanel)pContext.getComponent();
        
        if (panel.isOpaque())
        {
        	pGraphics.setColor(getBackgroundColor(panel, SmartTheme.COL_BACKGROUND));
			pGraphics.fillRect(pX, pY, pWidth, pHeight);
        }
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Label
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintLabelBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JLabel label = (JLabel)pContext.getComponent();
    	
    	//TableHeader Darstellung
    	if (label.getName() != null && label.getName().startsWith("TableHeader."))
    	{
    		pGraphics.setColor(SmartTheme.COL_TABLE_HEADER_BACKGROUND);
//    		pGraphics.setColor(new Color(200, 200, 200));
    		pGraphics.fillRect(pX, pY, pWidth, pHeight);
//
//    		pGraphics.setColor(SmartTheme.COL_TOOLBAR_SUB_BORDER);
//    		pGraphics.drawLine(pX + pWidth - 2, pY + 2, pX + pWidth - 2, pY + pHeight - 5);
//
//    		
//    		pGraphics.setColor(SmartTheme.COL_TOOLBAR_SUB_BORDER);
//    		pGraphics.drawLine(pX, pY + pHeight - 1, pX + pWidth - 1, pY + pHeight - 1);
    		
    		
    		pGraphics.setColor(SmartTheme.COL_TABLE_HEADER_BOOTM_LINE[0]);
    		pGraphics.drawLine(pX + pWidth - 2, pY + 2, pX + pWidth - 2, pY + pHeight - 5);
    		pGraphics.setColor(Color.WHITE);
    		pGraphics.drawLine(pX + pWidth - 1, pY + 2, pX + pWidth - 1, pY + pHeight - 5);
    		
    		pGraphics.setColor(SmartTheme.COL_TABLE_HEADER_BOOTM_LINE[0]);
    		pGraphics.drawLine(pX, pY + pHeight - 3, pX + pWidth - 1, pY + pHeight - 3);
    		pGraphics.setColor(SmartTheme.COL_TABLE_HEADER_BOOTM_LINE[1]);
    		pGraphics.drawLine(pX, pY + pHeight - 2, pX + pWidth - 1, pY + pHeight - 2);
    		pGraphics.setColor(SmartTheme.COL_TABLE_HEADER_BOOTM_LINE[2]);
    		pGraphics.drawLine(pX, pY + pHeight - 1, pX + pWidth - 1, pY + pHeight - 1);
    	}
//    	else if (label.getName() != null && label.getName().startsWith("ComboBox."))
//    	{
//Nicht gesondert nötig    	
//            Color color = pGraphics.getColor();
//            pGraphics.setColor(label.getBackground());
//            pGraphics.fillRect(pX, pY, pWidth, pHeight);
//            pGraphics.setColor(color);
//    	}
    	else
    	{
    		if (isUserDefinedBackgroundColor(label))
        	{
        		paintRectangle(pGraphics, pX - 1, pY - 1, pWidth + 2, pHeight + 2, 1, -1, label.getBackground(), null, -1, null);
        	}
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// FormattedTextField
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintFormattedTextFieldBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//Beim Editor im Spinner wird der Border nicht dargestellt, daher muss der Hintergrund
    	//etwas vergrößert werden!
    	if (SmartLookAndFeel.isSpinnerComponent(pContext.getComponent().getName()))
		{
    		pX--;
    		pY--;
    		pWidth += 2;
    		pHeight += 2;
		}
    	
    	paintTextComponentBackground(pContext.getComponent(), pGraphics, pX, pY, pWidth, pHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintFormattedTextFieldBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	if (!SmartLookAndFeel.isSpinnerComponent(pContext.getComponent().getName()))
		{
    		paintTextComponentBorder(pContext.getComponent(), pGraphics, pX, pY, pWidth, pHeight);
		}
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// TextField
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTextFieldBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComponent comp = pContext.getComponent();
    	
    	if (comp instanceof JTextComponent)
    	{
	    	JTextComponent txtComp = (JTextComponent)comp;
	
	    	//Bei ComboBox übernimmt das paintComboBoxBackground
	    	//Für JViewports wird paintScrollPaneBackground
	    	if (txtComp.getParent() instanceof JComboBox || txtComp.getParent() instanceof JViewport)
	    	{
	    		return;
	    	}
    	}
    	
    	paintTextComponentBackground(comp, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTextFieldBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComponent comp = pContext.getComponent();
    	
    	if (comp instanceof JTextComponent)
    	{
    		JTextComponent txtComp = (JTextComponent)comp;

	    	//Bei ComboBox übernimmt das paintComboBoxBorder
	    	//Für JViewports wird paintScrollPaneBorder
	    	if (txtComp.getParent() instanceof JComboBox || txtComp.getParent() instanceof JViewport)
	    	{
	    		return;
	    	}
    	}
    	
    	paintTextComponentBorder(comp, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PasswordField
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintPasswordFieldBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintTextFieldBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintPasswordFieldBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintTextFieldBorder(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// TextArea
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTextAreaBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintTextFieldBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTextAreaBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//TextAreas haben ohne Viewport keinen Border -> dieser wird ggf. vom Viewport dargestellt!
    	//paintTextFieldBorder(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Spinner
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSpinnerBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintTextComponentBackground((JComponent)pContext.getComponent(), pGraphics, pX, pY, pWidth, pHeight);    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSpinnerBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintTextComponentBorder((JComponent)pContext.getComponent(), pGraphics, pX, pY, pWidth, pHeight);    	
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Tooltip
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void paintToolTipBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Color colBorder;
    	
    	if (SmartLookAndFeel.isState(pContext, SynthConstants.DISABLED, null))
    	{
    		colBorder = SmartTheme.COL_TOOLTIP_BORDER_DISABLED;
    	}
    	else
    	{
    		colBorder = SmartTheme.COL_TOOLTIP_BORDER_ENABLED;
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, colBorder);
    }    

    /**
     * {@inheritDoc}
     */
    public void paintToolTipBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComponent comp = (JComponent)pContext.getComponent().getParent();
    	
    	//nur bei 1.5 der Fall!
    	if (comp.isOpaque())
    	{
    		comp.setOpaque(false);
    		comp.repaint();
    	}
    	
    	Color colBack;
    	
    	if (SmartLookAndFeel.isState(pContext, SynthConstants.DISABLED, null))
    	{
    		colBack = SmartTheme.COL_TOOLTIP_BACKGROUND_DISABLED;
    	}
    	else
    	{
    		colBack = SmartTheme.COL_TOOLTIP_BACKGROUND_ENABLED;
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, colBack, null, -1, null);
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Buttons
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintArrowButtonForeground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int pDirection) 
    {
    	Container conParent = pContext.getComponent().getParent();
    	
    	if (conParent instanceof JScrollBar || conParent instanceof JTabbedPane)
    	{
        	int iMiddleX = (pX + pWidth - 1) / 2;
        	int iMiddleY = (pY + pHeight - 1) / 2;

        	pGraphics.setColor(SmartTheme.COL_ARROW);
        	
        	if (pDirection == SwingUtilities.WEST || pDirection == SwingUtilities.EAST)
        	{
        		paintArrow(pGraphics, iMiddleX, iMiddleY, 3, iMiddleX - pX - 2, pDirection, TYPE_ARROW_LINES, SmartTheme.COL_ARROW);
        	}
        	else
        	{
        		paintArrow(pGraphics, iMiddleX - 0, iMiddleY, 3, iMiddleY - pY - 2, pDirection, TYPE_ARROW_LINES, SmartTheme.COL_ARROW);
        	}
    		
    	}
    	else if (conParent instanceof JComboBox
    			 || conParent instanceof JSpinner)
    	{
        	int iMiddleX = (pX + pWidth) / 2;
        	int iMiddleY = (pY + pHeight) / 2;
    		
        	Color colArrow;
        	
        	if (!conParent.isEnabled())
        	{
        		colArrow = SmartTheme.COL_BUTTON_FOREGROUND_DISABLED;
        	}
        	else
        	{
        		colArrow = SmartTheme.COL_ARROW;
        	}

        	if (conParent instanceof JSpinner)
	    	{
        		//Pixelanpassung
        		if (pHeight % 2 == 1)
        		{
	        		if (pDirection == SwingConstants.NORTH)
	        		{
	        			iMiddleY++;
	        		}
	        		else
	        		{
	        			iMiddleY--;
	        		}
        		}
        	}
        	
        	paintArrow(pGraphics, iMiddleX, iMiddleY, 1, pWidth - 8, pDirection, TYPE_ARROW_FILLED, colArrow);
    	}
    	else
    	{
        	int iMiddleX = (pX + pWidth) / 2;
        	int iMiddleY = (pY + pHeight) / 2;
    		
        	if (pDirection == SwingUtilities.WEST || pDirection == SwingUtilities.EAST)
        	{
	    		paintArrow(pGraphics, iMiddleX, iMiddleY, 1, pWidth, pDirection, TYPE_ARROW_FILLED, SmartTheme.COL_ARROW);
        	}
        	else
        	{
	    		paintArrow(pGraphics, iMiddleX, iMiddleY, 1, pHeight, pDirection, TYPE_ARROW_FILLED, SmartTheme.COL_ARROW);
        	}
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintArrowButtonBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintButtonBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintArrowButtonBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintButtonBorder(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintButtonBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Component com = pContext.getComponent();
    	Container conParent = com.getParent();

    	Color color;
    	Color colorEnd = null;
    	
    	int iCorner = 2;
    	int iOuterBorder = 0;
    	
    	
    	if (conParent instanceof JScrollBar || conParent instanceof JTabbedPane)
    	{
    		Color colOuter;

    		if (conParent instanceof JScrollBar)
    		{
	    		JScrollBar bar = (JScrollBar)conParent;
	    		
	    		if (bar.getVisibleAmount() == bar.getMaximum())
	    		{
	    			//Wenn kein Thumb angezeigt wird -> komplette ScrollBar ausgrauen
	    			color = SmartTheme.COL_SCROLLBAR_INACTIVE;
	    			colOuter = SmartTheme.COL_SCROLLBAR_THUMB_OUTER_INACTIVE_BORDER; 
	    		}
	    		else
	    		{
	    			//ansonsten wird abhängig was mit der Maus passiert die Farbe gesetzt!
	    			if (SmartLookAndFeel.isState(pContext, SynthConstants.PRESSED, Boolean.valueOf(true)))
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_PRESSED_BACKGROUND;
	    			}
	    			else if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, Boolean.valueOf(true)))
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_OVER;
	    			}
	    			else
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_BACKGROUND;
	    			}
	    			
	    			colOuter = SmartTheme.COL_SCROLLBAR_THUMB_OUTER_ACTIVE_BORDER; 
	    		}
    		}
    		else
    		{
    			AbstractButton butScroll = (AbstractButton)com;
    			
    			if (butScroll.isEnabled())
    			{
	    			if (SmartLookAndFeel.isState(pContext, SynthConstants.PRESSED, Boolean.valueOf(true)))
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_PRESSED_BACKGROUND;
	    			}
	    			else if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, Boolean.valueOf(true)))
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_OVER;
	    			}
	    			else
	    			{
	    				color = SmartTheme.COL_SCROLLBAR_THUMB_BACKGROUND;
	    			}
	    			
	    			colOuter = SmartTheme.COL_SCROLLBAR_THUMB_OUTER_ACTIVE_BORDER; 
    			}
    			else
    			{
    				color = SmartTheme.COL_SCROLLBAR_INACTIVE;
	    			colOuter = SmartTheme.COL_SCROLLBAR_THUMB_OUTER_INACTIVE_BORDER; 
    			}
    		}
    		
    		//bei ScrollBar Buttons wird ein äußerer Rahmen auch noch gezeichnet
    		//nicht im paint...Border, damit der Hintergrund die nicht benötigten Striche übermalen kann!
    		iOuterBorder = 1;
	    	paintRectangle(pGraphics, pX, pY + 1, pWidth, pHeight - 1, iCorner, -1, null, null, -1, colOuter);
    	}
    	else if (conParent instanceof BasicSplitPaneDivider)
    	{
    		//kein Hintergrund für SplitPane Arrow Buttons!
    		color = null;
    	}
    	else if (conParent instanceof JToolBar || conParent.getParent() instanceof JToolBar)
    	{
    		AbstractButton button = (AbstractButton)com;
    		
    		if (button.getClientProperty(SmartTheme.NAME_TOOLBAR_CLOSE_BUTTON) != null)
    		{
    			color = null;
    		}
    		else
    		{
    			ButtonModel model = button.getModel(); 
    			
	    		if (model.isRollover() || model.isArmed() || model.isSelected())
	    		{
	    			if (button.isEnabled())
	    			{
						color = SmartTheme.COL_TOOLBAR_BUTTON_BACKGROUND_ENABLED[0];
						colorEnd = SmartTheme.COL_TOOLBAR_BUTTON_BACKGROUND_ENABLED[1];
	    			}
	    			else
	    			{
			    		color = SmartTheme.COL_TOOLBAR_BUTTON_BACKGROUND_DISABLED;
	    			}
	    			
		    		iCorner = 4;
	    		}
	    		else
	    		{
	    			color = null;
	    		}
    		}
    	}
    	else if (conParent instanceof BasicInternalFrameTitlePane)
    	{
    		String sButtonName = com.getName();

    		if (SmartLookAndFeel.isNorthPaneMenuButton(sButtonName))
    		{
    			//der Menü-Button ist ein Bild -> kein Handlungsbedarf
    			return;
    		}
    		
    		JInternalFrame frame = SmartLookAndFeel.getInternalFrame(conParent);
    		
        	if (frame != null)
        	{
        		Color colBack;

        		iCorner = 2;

        		int iStateIndex = 0;
        		
        		if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, null))
        		{
        			iStateIndex = 1;
        		}
	    			 
        		//Unterscheiden zwischen aktiven und inaktiven Farben
	    		if (frame.isSelected())
	    		{
	        		Boolean bModal = (Boolean)frame.getClientProperty("JVxInternalFrame.intern_modal"); 
	        		
	        		if (bModal != null && bModal.booleanValue())
	        		{
	        			colBack = SmartTheme.COL_INTFRAME_MODAL_TITLE_BACKGROUND_ACTIVE;
	        		}
	        		else
	        		{
	        			colBack = SmartTheme.COL_INTFRAME_TITLE_BACKGROUND_ACTIVE;
	        		}
	    			
	        		if (SmartLookAndFeel.isNorthPaneCloseButton(sButtonName))
	        		{
        				color = SmartTheme.COL_INTFRAME_CLOSE_BACKGROUND_ACTIVE[iStateIndex];
	        		}
	        		else
	        		{
	        			color = SmartTheme.COL_INTFRAME_BUTTONS_BACKGROUND_ACTIVE[iStateIndex];
	        		}
	    		}
	    		else
	    		{
	    			colBack = SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE;
	    			color = SmartTheme.COL_INTFRAME_BUTTONS_BACKGROUND_INACTIVE; 
	    		}
	    		
	    		//die Buttons sind opaque und können auch nicht ohne Listener im SmartStyle.initDefaults 
	    		//geändert werden, weil setOpaque im Konstruktor explizit gesetzt wird!
	    		//Daher ist diese Lösung in Ordnung.
	    		pGraphics.setColor(colBack);
	    		pGraphics.fillRect(pX, pY, pWidth, pHeight);
        	}
        	else
        	{
        		//kein Frame -> keine Darstellung
        		color = null;
        	}
    	}
    	else if (conParent instanceof SmartRootTitlePane)
    	{
    		iCorner = 2;

    		int iStateIndex = 0;
    		
    		if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, null))
    		{
    			iStateIndex = 1;
    		}
    			 
    		//Unterscheiden zwischen aktiven und inaktiven Farben
    		if (((SmartRootTitlePane)conParent).getWindow().isActive())
    		{
        		if (SmartLookAndFeel.isWindowCloseButton(com.getName()))
        		{
    				color = SmartTheme.COL_INTFRAME_CLOSE_BACKGROUND_ACTIVE[iStateIndex];
        		}
        		else
        		{
        			color = SmartTheme.COL_INTFRAME_BUTTONS_BACKGROUND_ACTIVE[iStateIndex];
        		}
    		}
    		else
    		{
    			color = SmartTheme.COL_INTFRAME_BUTTONS_BACKGROUND_INACTIVE; 
    		}
    	}
    	else
    	{
    		ButtonModel model = ((AbstractButton)com).getModel();
    		
    		if (model.isSelected())
    		{
    			if (model.isEnabled())
    			{
	    			if ((model.isPressed() && model.isArmed()) || (model.isRollover() && model.isPressed()))
	    			{
	    				//Wenn mit gedrückter Maustaste in und aus dem Button gefahren wird
	    				color = SmartTheme.COL_BUTTON_BACKGROUND_OVER_SELECTED;
	    			}
	    			else
	    			{
	    				//Default Selected Farbe
	    				color = SmartTheme.COL_BUTTON_BACKGROUND_SELECTED;
	    			}
    			}
    			else
    			{
    				//Disabled
    				color = SmartTheme.COL_BUTTON_BACKGROUND_DISABLED_SELECTED;
    			}
    		}
    		else
    		{
	    		if (model.isEnabled())
	    		{
	    			if ((model.isPressed() && model.isArmed()) || (model.isRollover() && model.isPressed()))
	    			{
	    				//Wenn mit gedrückter Maustaste über in und aus dem Button gefahren wird
        				color = SmartTheme.COL_BUTTON_BACKGROUND_PRESSED[0]; 
        				colorEnd = SmartTheme.COL_BUTTON_BACKGROUND_PRESSED[1]; 
	    			}
	    			else if (model.isRollover())    		
					{
	    				//Wenn in den Button gefahren wird
        				color = SmartTheme.COL_BUTTON_BACKGROUND_OVER[0]; 
        				colorEnd = SmartTheme.COL_BUTTON_BACKGROUND_OVER[1]; 
					}
					else
					{
						//Default-Farbe
        				color = SmartTheme.COL_BUTTON_BACKGROUND[0]; 
        				colorEnd = SmartTheme.COL_BUTTON_BACKGROUND[1]; 
					}
	    		}
	    		else
	    		{
	    			//Disabled
	    			color    = SmartTheme.COL_BUTTON_BACKGROUND_DISABLED[0];
	    			colorEnd = SmartTheme.COL_BUTTON_BACKGROUND_DISABLED[1];
	    		}
    		}
    		
    		//Spinner und ComboBox Buttons werden bis zum Rand gezeichnet
    		if (conParent instanceof JSpinner)
        	{
    			if (SmartLookAndFeel.isSpinnerPreviousButton(com.getName()))
    			{
    				//falls ein Button höher ist als der andere, wirkt es besser wenn der Farbverlauf von der
    				//Mitte zur Seite geht (da merkt man optisch die Höhe nicht)
	    			Color colTmp = color;
	    			color = colorEnd;
	    			colorEnd = colTmp;
	    			
	    			//eins nach oben, weil der Border nicht gemalt wird und sonst 1px fehlt!
    				pY--;
    			}
    			//Höher darstellen, weil der Border nicht gemalt wird und sonst 1px fehlt!
				pHeight++;
        	}
    	}
    	
    	if (((AbstractButton)com).isContentAreaFilled())
    	{
	    	//wenn der Border nicht dargestellt wird -> Button mit "runden" Ecken darstellen
	    	//und um den Rahmen vergrößern
	    	if (!((AbstractButton)com).isBorderPainted())
	    	{
	    		iCorner = 4;
	    		pX--;
	    		pY--;
	    		pWidth += 2;
	    		pHeight += 2;
	    	}
	    	
			paintRectangle(pGraphics, pX, pY, pWidth - iOuterBorder, pHeight - iOuterBorder, iCorner, -1, color, colorEnd, GRADIENT_VERTICAL, null);
    	}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintButtonBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Component com = pContext.getComponent();
    	Container conParent = com.getParent();

    	Color color;
    	
    	int iCorner = 2;
    	int iOuterBorder = 0;
    	int iOpened = -1;
    	
    	
    	if (conParent instanceof JScrollBar || conParent instanceof JTabbedPane)
    	{
			color = SmartTheme.COL_SCROLLBAR_THUMB_INNER_BORDER;
    		
    		iOuterBorder = 1;
    	}
    	else if (conParent instanceof BasicSplitPaneDivider)
    	{
    		//Kein Border bei SplitPane Arrow Buttons!
    		color = null;
    	}
    	else if (conParent instanceof JToolBar || conParent.getParent() instanceof JToolBar)
    	{
    		AbstractButton button = (AbstractButton)com;
    		
    		if (button.getClientProperty(SmartTheme.NAME_TOOLBAR_CLOSE_BUTTON) != null)
    		{
    			color = null;
    			iCorner = 1;
    		}
    		else
    		{
    			ButtonModel model = button.getModel();
    			
	    		if (model.isRollover() || model.isArmed() || model.isSelected())
	    		{
	    			if (button.isEnabled())
	    			{
	    				color = SmartTheme.COL_TOOLBAR_BUTTON_BORDER_ENABLED;
	    			}
	    			else
	    			{
	    				color = SmartTheme.COL_TOOLBAR_BUTTON_BORDER_DISABLED;
	    			}
	    			
		    		iCorner = 4;
	    		}
	    		else
	    		{
	    			color = null;
	    		}
    		}
    	}
    	else if (conParent instanceof BasicInternalFrameTitlePane)
    	{
    		String sButtonName = com.getName();
    		
    		if (SmartLookAndFeel.isNorthPaneMenuButton(sButtonName))
    		{
    			//der Menü-Button ist ein Bild -> kein Handlungsbedarf
    			return;
    		}
    		
    		JInternalFrame frame = SmartLookAndFeel.getInternalFrame(conParent);

        	if (frame != null)
        	{
        		Color colorInner = null;
        		Color colorSymbol = null;

        		iCorner = 2;
	    		
        		int iStateIndex = 0;
        		
        		if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, null))
        		{
        			iStateIndex = 3;
        		}
        		
	    		if (frame.isSelected())
	    		{
	        		if (SmartLookAndFeel.isNorthPaneCloseButton(sButtonName))
	        		{
	            		color       = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex];
	            		colorInner  = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex + 1];
	            		colorSymbol = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex + 2];
	        		}
	        		else
	        		{
	            		color       = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex];
	            		colorInner  = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex + 1];
		    			colorSymbol = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex + 2];
	        		}
	    		}
	    		else
	    		{
	    			color = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[0];
	    			colorInner = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[1];
	    			colorSymbol = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[2];
	    		}
	    		
	    		
	        	paintRectangle(pGraphics, pX, pY, pWidth - iOuterBorder - 1, pHeight - iOuterBorder - 1, 2, -1, null, null, -1, colorInner);
	        	
	        	//das Symbol malen
	        	paintNorthPaneButtonSymbol(frame, (JButton)com, pGraphics, pX, pY, pWidth, pHeight, colorSymbol);
        	}
        	else
        	{
        		//kein Frame -> keine Darstellung
        		color = null;
        	}
    	}
    	else if (conParent instanceof SmartRootTitlePane)
    	{
    		Color colorInner = null;
    		Color colorSymbol = null;

    		iCorner = 2;
    		
    		int iStateIndex = 0;

    		if (((SmartRootTitlePane)conParent).getWindow().isActive())
    		{
    			String sButtonName = com.getName();
    			
    			if (SmartLookAndFeel.isWindowCloseButton(sButtonName))
        		{
            		color       = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex];
            		colorInner  = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex + 1];
            		colorSymbol = SmartTheme.COL_INTFRAME_CLOSE_BORDER_ACTIVE[iStateIndex + 2];
        		}
        		else
        		{
            		color       = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex];
            		colorInner  = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex + 1];
	    			colorSymbol = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_ACTIVE[iStateIndex + 2];
        		}
    		}
    		else
    		{
    			color = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[0];
    			colorInner = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[1];
    			colorSymbol = SmartTheme.COL_INTFRAME_BUTTONS_BORDER_INACTIVE[2];
    		}

    		paintRectangle(pGraphics, pX, pY, pWidth - iOuterBorder - 1, pHeight - iOuterBorder - 1, 2, -1, null, null, -1, colorInner);
        	
        	//das Symbol malen
    		paintSmartTitlePaneButtonSymbol(((SmartRootTitlePane)conParent).getWindow(), (JButton)com, pGraphics, pX, pY, pWidth, pHeight, colorSymbol);    		
    	}
    	else if (conParent instanceof JComboBox
    			 || conParent instanceof JSpinner)
    	{
        	if (!conParent.isEnabled())
        	{
        		color = SmartTheme.COL_TEXT_BORDER_DISABLED;
        	}
        	else
        	{
        		color = SmartTheme.COL_TEXT_BORDER;
        	}    	
    		
    		pGraphics.setColor(color);

    		int iX;
    		
    		if (SmartLookAndFeel.isLeftToRightOrientation(conParent))
    		{
    			iX = pX;
    		}
    		else
    		{
    			iX = pX + pWidth - 1;
    		}
    		
    		pGraphics.drawLine(iX, pY, iX, pY + pHeight - 1);
    		
    		return;
    	}
    	else
    	{
    		ButtonModel model = ((AbstractButton)com).getModel();
    		
    		if (model.isEnabled())
    		{
	    		if ((model.isPressed() && model.isArmed()) || (model.isRollover() && model.isPressed()))
	    		{
	    			color = SmartTheme.COL_BUTTON_BORDER_OVER;
	    		}
	    		else
	    		{
	    			//Default
	    			color = SmartTheme.COL_BUTTON_BORDER;
	    		}
    		}
    		else
    		{
    			//Disabled
    			color = SmartTheme.COL_BUTTON_BORDER_DISABLED;
    		}
    		
    		iCorner = 2;
    	}

		paintRectangle(pGraphics, pX, pY, pWidth - iOuterBorder, pHeight - iOuterBorder, iCorner, iOpened, null, null, -1, color);
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintToggleButtonBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
        paintButtonBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
   public void paintToggleButtonBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
        paintButtonBorder(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }    

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ScrollPane
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollPaneBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight) 
    {
    	if (((JScrollPane)pContext.getComponent()).getViewport().getComponentCount() > 0)
    	{
    		Component com = ((JScrollPane)pContext.getComponent()).getViewport().getView();
    		
	    	//Wenn zB eine TextArea beinhaltet ist, so muss nur der Rahmen gemalt werden, da der Hintergrund
    		//durch die Textkomponente gemalt wird!
    		if (com instanceof JTextComponent)
    		{
    			paintTextComponentBackground((JTextComponent)com, pGraphics, pX, pY, pWidth, pHeight);
    			
    			/*
    			 * Nachfolgender Code würde den Hintergrund des Viewport separat zeichnen
    			 * 
    			JViewport viewport = ((JScrollPane)pContext.getComponent()).getViewport();
    			
    			//Hintergrund der ScrollPane
    			paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, SmartTheme.COL_SCROLLPANE_BACKGROUND, null, -1, null);
    			
    			//Hintergrund der Textkomponente
    			paintTextComponentBackground((JTextComponent)com, pGraphics, pX, pY, viewport.getWidth() + 2, viewport.getHeight() + 2);
    			*/
    		}
    		else
    		{
    			paintRectangle(pGraphics, pX, pY, pWidth, pHeight, -1, -1, SmartTheme.COL_SCROLLPANE_BACKGROUND, null, -1, null);
    		}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollPaneBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight) 
    {
    	//siehe SmartLookAndFell.initialize (für Table-Umrandung)
    	
    	Component comView = ((JScrollPane)pContext.getComponent()).getViewport().getView();
    	
    	if (comView instanceof JTextComponent)
    	{
    		paintTextComponentBorder((JTextComponent)comView, pGraphics, pX, pY, pWidth, pHeight);
    	}
    	//else
    	//{
    	//	nix, da ansonsten bei Scrollable Panels ein Border erscheint!
    	//}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ScrollBar
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollBarBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//den kompletten Hintergrund (auch hinter den Buttons) einfärben
    	if (SmartTheme.COL_SCROLLBAR_BACKGROUND == null || SmartTheme.COL_SCROLLBAR_BACKGROUND.length == 0)
    	{
    		pGraphics.setColor(SmartTheme.COL_SCROLLPANE_BACKGROUND);
    		pGraphics.fillRect(pX, pY, pWidth, pHeight);
    	}
    	else
    	{
    		int iOrientation = ((JScrollBar)pContext.getComponent()).getOrientation();
    		
    		if (iOrientation == JScrollBar.HORIZONTAL)
    		{
    			//+3 und - 6, damit links und rechts vom Button der Gradient nicht sichtbar ist
    			paintRectangle
    			(
    				pGraphics, 
    				pX + 3, 
    				pY, 
    				pWidth - 6, 
    				pHeight, 
    				0, 
    				-1, 
    				SmartTheme.COL_SCROLLBAR_BACKGROUND[0], 
    				SmartTheme.COL_SCROLLBAR_BACKGROUND[1], 
    				GRADIENT_VERTICAL, 
    				null
    			);
    		}
    		else
    		{
    			//+3 und - 6, damit oben und unten vom Button der Gradient nicht sichtbar ist
    			paintRectangle
    			(
    				pGraphics, 
    				pX, 
    				pY + 3, 
    				pWidth, 
    				pHeight - 6, 
    				0, 
    				-1, 
    				SmartTheme.COL_SCROLLBAR_BACKGROUND[0], 
    				SmartTheme.COL_SCROLLBAR_BACKGROUND[1], 
    				GRADIENT_HORIZONTAL, 
    				null
    			);
    		}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollBarThumbBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int pOrientation) 
    {
    	if (pHeight > 0)
    	{
    		int iHandleSteps = 6;

    		Color colBack;
    		
			JScrollBar bar = (JScrollBar)pContext.getComponent();

			if (SmartLookAndFeel.isState(pContext, SynthConstants.PRESSED, Boolean.valueOf(true)))
    		{
    			if (((BasicScrollBarUI)bar.getUI()).isThumbRollover())
    			{
    				colBack = SmartTheme.COL_SCROLLBAR_THUMB_PRESSED_BACKGROUND;
    			}
    			else
    			{
	    			colBack = SmartTheme.COL_SCROLLBAR_THUMB_BACKGROUND;
    			}
    		}
    		else
    		{
    			if (((BasicScrollBarUI)bar.getUI()).isThumbRollover())
    			{
    				colBack = SmartTheme.COL_SCROLLBAR_THUMB_OVER;
	    		}
	    		else
	    		{
	    			colBack = SmartTheme.COL_SCROLLBAR_THUMB_BACKGROUND;
	    		}
    		}
    		
	    	if (pOrientation == JScrollBar.VERTICAL)
	    	{
	    		paintRectangle(pGraphics, pX, pY + 1, pWidth, pHeight - 1, 2, -1, null, null, -1, SmartTheme.COL_SCROLLBAR_THUMB_OUTER_ACTIVE_BORDER);
	    	}
	    	else
	    	{
	    		paintRectangle(pGraphics, pX + 1, pY, pWidth - 1, pHeight, 2, -1, null, null, -1, SmartTheme.COL_SCROLLBAR_THUMB_OUTER_ACTIVE_BORDER);
	    	}
	    	
        	paintRectangle(pGraphics, pX, pY, pWidth - 1, pHeight - 1, 2, -1, colBack, null, -1, null);

	    	if (pOrientation == JScrollBar.VERTICAL)
	    	{
		    	//Der Slider muss etwas mehr Platz bieten als nur für die Anfasser,
		    	//damit der Anfasser sinn macht!
				if (pHeight / iHandleSteps < 4)
				{
					iHandleSteps /= 2;
				}

				//3px benötigt der Border des Tracks!
				//6px bedeutet 3px Abstand oben und unten
		    	if (pHeight > iHandleSteps + 9)
		    	{
			    	// Anfasser
			    	int iMiddle = pHeight / 2;
			    	
			    	for (int i = 0, anz = iHandleSteps, j = anz - 1; i < anz; i++, j -= 2)
			    	{
				    	pGraphics.setColor(SmartTheme.COL_SCROLLBAR_GRIP[0]);
			        	pGraphics.drawLine(pX + 3, pY + iMiddle + j, pX + pWidth - 5, pY + iMiddle + j);
				    	pGraphics.setColor(SmartTheme.COL_SCROLLBAR_GRIP[1]);
			        	pGraphics.drawLine(pX + 4, pY + iMiddle + j + 1, pX + pWidth - 4, pY + iMiddle + j + 1);
			    	}
		    	}
	    	}
	    	else
	    	{
		    	//Der Slider muss etwas mehr Platz bieten als nur für die Anfasser,
		    	//damit der Anfasser sinn macht!
				if (pWidth / iHandleSteps < 4)
				{
					iHandleSteps /= 2;
				}

				//3px benötigt der Border des Tracks!
				//6px bedeutet 3px Abstand oben und unten
		    	if (pWidth > iHandleSteps + 9)
		    	{
			    	// Anfasser
			    	int iMiddle = pWidth / 2;
			    	
			    	for (int i = 0, anz = iHandleSteps, j = anz - 1; i < anz; i++, j -= 2)
			    	{
				    	pGraphics.setColor(SmartTheme.COL_SCROLLBAR_GRIP[0]);
			        	pGraphics.drawLine(pX + iMiddle + j, pY + 3, pX + iMiddle + j, pY + pHeight - 5);
				    	pGraphics.setColor(SmartTheme.COL_SCROLLBAR_GRIP[1]);
			        	pGraphics.drawLine(pX + iMiddle + j + 1, pY + 4, pX + iMiddle + j + 1, pY + pHeight - 4);
			    	}
		    	}
	    	}
    	}
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollBarThumbBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int pOrientation) 
    {
    	paintRectangle(pGraphics, pX, pY, pWidth - 1, pHeight - 1, 2, -1, null, null, -1, SmartTheme.COL_SCROLLBAR_THUMB_INNER_BORDER);
    	
    	//Es gibt keinen Zugriff auf das Thumb rectangle, daher muss das hier gesichert werden!
    	pContext.getComponent().putClientProperty("Smart/LF.thumb.rec", new Rectangle(pX, pY, pWidth, pHeight));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintScrollBarTrackBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//es wird immer der komplette Hintergrund (auch hinter den Buttons) eingefärbt
    	//daher muss hier nur der freie Thumb Bereich markiert werden

    	JScrollBar bar = (JScrollBar)pContext.getComponent();
    	
    	Rectangle recThumb = (Rectangle)bar.getClientProperty("Smart/LF.thumb.rec");
    	
    	//Wenn im Track auf einen freien Bereich gedrückt und die Maustaste gehalten wird, dann
    	//wird der Bereich (unter, ober, links oder rechts) von dem Thumb markiert!
    	if (bar.getValueIsAdjusting() && !((BasicScrollBarUI)bar.getUI()).isThumbRollover() && recThumb != null)
    	{
    		Integer iPressedValue = (Integer)bar.getClientProperty("Smart/LF.adjusting");
    		
    		int iPressed;
    		if (iPressedValue == null)
    		{
    			iPressed = -1;
    		}
    		else
    		{
    			iPressed = iPressedValue.intValue();
    		}
    		
    		Point poMouse = (Point)bar.getClientProperty("Smart/LF.mouse.point");
    		
    		if (bar.getOrientation() == JScrollBar.VERTICAL)
    		{
	    		//Wenn der Wert verkleinert wird -> oberen Bereich markieren
	    		//iPressed ist nötig, da theoretisch mit der Maus von unten nach oben gefahren werden kann
	    		//der Value ändert sich dann nicht und der freie Bereich darf auch nicht eingefärbt werden!
	    		if (poMouse.y < recThumb.y 
	    			&& (iPressed == -1 || iPressed == 0 || iPressed == 1))
	    		{
	        		bar.putClientProperty("Smart/LF.adjusting", Integer.valueOf(1));
	
	        		//Value wird verringert
		        	paintRectangle
		        	(
		        		pGraphics, 
		        		pX - 1, 
		        		pY - 1,
		        		pWidth + 1,
		        		recThumb.y - 1 - pY + 1, 
		        		2, 
		        		-1, 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[0], 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[1], 
		        		SmartPainter.GRADIENT_HORIZONTAL, 
		        		null
		        	);
	    		}
	    		else if (poMouse.y > recThumb.y + recThumb.height && (iPressed == -1 || iPressed == 0 || iPressed == 2))
	    		{
	        		bar.putClientProperty("Smart/LF.adjusting", Integer.valueOf(2));
	        		
		    		//Value wird erhöht
		        	paintRectangle
		        	(
		        		pGraphics, 
		        		recThumb.x - 1, 
		        		recThumb.y + recThumb.height + 1, 
		        		pWidth - 1 + 1,
		        		pHeight - recThumb.y + recThumb.height + 2,
		        		2, 
		        		-1, 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[0], 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[1], 
		        		SmartPainter.GRADIENT_HORIZONTAL, 
		        		null
		        	);
	    		}
    		}
    		else
    		{
	    		if (poMouse.x < recThumb.x 
	    			&& (iPressed == -1 || iPressed == 0 || iPressed == 3))
	    		{
	        		bar.putClientProperty("Smart/LF.adjusting", Integer.valueOf(3));
	
	        		//Value wird verringert
		        	paintRectangle
		        	(
		        		pGraphics, 
		        		pX - 1, 
		        		pY - 1,
		        		recThumb.x - pX - recThumb.width + 1,
		        		pHeight + 1, 
		        		2, 
		        		-1,
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[0], 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[1], 
		        		SmartPainter.GRADIENT_VERTICAL, 
		        		null
		        	);
	    		}
	    		else if (poMouse.x > recThumb.x + recThumb.width && (iPressed == -1 || iPressed == 0 || iPressed == 4))
	    		{
	        		bar.putClientProperty("Smart/LF.adjusting", Integer.valueOf(4));
	        		
		    		//Value wird erhöht
		        	paintRectangle
		        	(
		        		pGraphics, 
		        		pX + recThumb.x + 1, 
		        		recThumb.y - 1, 
		        		pWidth - recThumb.x, 
		        		pHeight - 1, 
		        		2, 
		        		-1, 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[0], 
		        		SmartTheme.COL_SCROLLBAR_BACKGROUND_PRESSED[1], 
		        		SmartPainter.GRADIENT_VERTICAL, 
		        		null
		        	);
	    		}
    		}
    	}
    	else if (!bar.getValueIsAdjusting())
    	{
    		//keine Bewegung -> Hilfsproperty reset
    		bar.putClientProperty("Smart/LF.adjusting", Integer.valueOf(0));
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Table
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTableBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//Bereich unterhalb der Table, wenn keine ScrollPane verwendet wird!
    	pGraphics.setColor(SmartTheme.COL_TABLE_BACKGROUND);
    	pGraphics.fillRect(pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTableBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTableHeaderBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTableHeaderBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// TabbedPane
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
   @Override
   public void paintTabbedPaneTabAreaBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	//wird in paintTabbedPaneTabAreaBackground mitgezeichnet, da der Viewport den Border nicht darstellt bzw.
    	//die Methoden paintViewportBorder nicht aufgerufen wird!
    }

    /**
     * {@inheritDoc}
     */
   @Override
   public void paintTabbedPaneTabAreaBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
		//Hoher Block am unteren Ende der Tabs

		JTabbedPane tab = (JTabbedPane)pContext.getComponent();
		
		//Beim Scroll-Layout wird der Background vom Viewport gezeichnet!
		if (tab.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT)
		{
			Insets insTab = pContext.getStyle().getInsets(pContext, null);
			
			paintTabbedPaneTabArea(tab, insTab, pGraphics, pX, pY, pWidth, pHeight, false);
		}
    }
    
    /**
     * {@inheritDoc}
     */
   @Override
    public void paintTabbedPaneTabBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int pIndex)
    {
    	JTabbedPane tab = (JTabbedPane)pContext.getComponent();

    	boolean bSelected = tab.getSelectedIndex() == pIndex; 

    	Color colFirst;
    	Color colSecond;
    	
    	if (bSelected && tab.isEnabledAt(pIndex) && tab.isEnabled())
    	{
    		Boolean bDrag = (Boolean)tab.getClientProperty("TabbedPane.dragging"); 
    		
    		if (bDrag != null && bDrag.booleanValue())
    		{
	    		colFirst = SmartTheme.COL_TAB_ACTIVE_BACKGROUND;
    		}
    		else
    		{
	    		colFirst = SmartTheme.COL_BACKGROUND;
    		}
    		
			colSecond = SmartTheme.COL_TAB_ACTIVE_BACKGROUND;
    	}
    	else
    	{
    		colSecond = SmartTheme.COL_BACKGROUND;
    		colFirst  = SmartTheme.COL_TAB_INACTIVE_BACKGROUND;
    	}

    	int iTabSpace = 2; 	//Platz links und rechts neben den Tabs
    	int iHeight = 4;		//Höhenanpassung
    	
    	if (bSelected)
    	{
    		iHeight = 1;
    		iTabSpace = 1;
    	}    	
    	
		switch (tab.getTabPlacement())
		{
			case JTabbedPane.TOP:
		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX + iTabSpace, 
		    		pY + iHeight - 1, 
		    		pWidth - iTabSpace - iTabSpace + 1, 
		    		pHeight - iHeight + 2, 
		    		TABCORNER, 
		    		SwingConstants.BOTTOM, 
		    		colFirst, 
		    		colSecond,
		    		GRADIENT_VERTICAL,
		    		null
		    	);
		    	break;
		    	
			case JTabbedPane.LEFT:
		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX + iHeight - 1, 
		    		pY + iTabSpace, 
		    		pWidth - iHeight + 2, 
		    		pHeight - iTabSpace - iTabSpace + 1, 
		    		TABCORNER, 
		    		SwingConstants.RIGHT, 
		    		colFirst, 
		    		colSecond,
		    		GRADIENT_HORIZONTAL,
		    		null
		    	);
		    	break;
		    	
			case JTabbedPane.BOTTOM:
				paintRectangle
				(
					pGraphics, 
					pX + iTabSpace, 
					pY - 1, 
					pWidth - iTabSpace - iTabSpace + 1, 
					pHeight - iHeight + 2, 
					TABCORNER, 
					SwingConstants.TOP, 
					colSecond,
					colFirst,
					GRADIENT_VERTICAL,
					null
				);
				break;
				
			case JTabbedPane.RIGHT:
		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX - 1, 
		    		pY + iTabSpace, 
		    		pWidth - iHeight + 2, 
		    		pHeight - iTabSpace - iTabSpace + 1, 
		    		TABCORNER, 
		    		SwingConstants.LEFT, 
		    		colSecond,
		    		colFirst,
		    		GRADIENT_HORIZONTAL,
		    		null
		    	);			
		    	break;
		    	
			default:
				//not supported
				break;
		}
    }
    
    /**
     * {@inheritDoc}
     */
   @Override
   public void paintTabbedPaneTabBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int iIndex)
    {
    	JTabbedPane tab = (JTabbedPane)pContext.getComponent();

    	boolean bSelected = tab.getSelectedIndex() == iIndex;
    	boolean bEnabled  = tab.isEnabled() && tab.isEnabledAt(iIndex);
    	
    	int iTabSpace = 2; 	//Platz links und rechts neben den Tabs
    	int iHeight = 4;		//Höhenanpassung

    	int iSelectionHeight = 5;
    	
    	if (bSelected)
    	{
    		iHeight = 1;	//nicht 0, da sonst die obere Linie bei Mehrzeiligen Karteireitern übermalt wird!
    		iTabSpace = 1;
    	}
    	
    	Color colBorder;
    	
    	if (bEnabled)
    	{
    		colBorder = SmartTheme.COL_TAB_BORDER_ENABLED;
    	}
    	else
    	{
    		colBorder = SmartTheme.COL_TAB_BORDER_DISABLED;
    	}
    	
		switch (tab.getTabPlacement())
		{
			case JTabbedPane.TOP:
				//Korrektur nötig, sonst stimmt der Abstand nicht!
				iHeight--;

				//Markierung beim überfahren mit der Maus
				if (bEnabled && !bSelected && SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, "" + iIndex))
				{
			    	paintRectangle
			    	(
			    		pGraphics, 
			    		pX + iTabSpace, 
			    		pY + iHeight, 
			    		pWidth - iTabSpace - iTabSpace + 1, 
			    		iSelectionHeight, 
			    		TABCORNER, 
			    		SwingConstants.BOTTOM, 
			    		SmartTheme.COL_TAB_ACTIVE_BACKGROUND,
			    		null,
			    		-1,
			    		null
			    	);
				}
				
		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX + iTabSpace, 
		    		pY + iHeight, 
		    		pWidth - iTabSpace - iTabSpace + 1, 
		    		pHeight - iHeight, 
		    		TABCORNER, 
		    		SwingConstants.BOTTOM, 
		    		null, 
		    		null,
		    		-1,
		    		colBorder
		    	);
		    	
				//Grundlinie "öffnen" bzw. löschen
				if (bSelected)
				{
					if (bEnabled)
					{
						pGraphics.setColor(SmartTheme.COL_TAB_ACTIVE_BACKGROUND);
					}
					else
					{
						pGraphics.setColor(SmartTheme.COL_BACKGROUND);
					}				
					
					pGraphics.drawLine(pX  + iTabSpace, pY + pHeight, pX + pWidth - iTabSpace, pY + pHeight);
				}
				
		    	break;
		    	
			case JTabbedPane.LEFT:
				//Korrektur nötig, sonst stimmt der Abstand nicht!
				iHeight--;

				//Markierung beim überfahren mit der Maus
				if (bEnabled && !bSelected && SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, "" + iIndex))
				{
			    	paintRectangle
			    	(
			    		pGraphics, 
			    		pX + iHeight, 
			    		pY + iTabSpace, 
			    		iSelectionHeight, 
			    		pHeight - iTabSpace - iTabSpace + 1, 
			    		TABCORNER, 
			    		SwingConstants.RIGHT, 
			    		SmartTheme.COL_TAB_ACTIVE_BACKGROUND, 
			    		null,
			    		-1,
			    		null
			    	);				
				}

		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX + iHeight, 
		    		pY + iTabSpace, 
		    		pWidth - iHeight, 
		    		pHeight - iTabSpace - iTabSpace + 1, 
		    		TABCORNER, 
		    		SwingConstants.RIGHT, 
		    		null, 
		    		null,
		    		-1,
		    		colBorder
		    	);
		    	
				//Grundlinie "öffnen" bzw. löschen
				if (bSelected)
				{
					if (bEnabled)
					{
						pGraphics.setColor(SmartTheme.COL_TAB_ACTIVE_BACKGROUND);
					}
					else
					{
						pGraphics.setColor(SmartTheme.COL_TAB_INACTIVE_BACKGROUND);
					}				
					
					pGraphics.drawLine(pX + pWidth, pY + 1, pX + pWidth, pY + pHeight - 1);
				}
				break;

			case JTabbedPane.BOTTOM:
				
				//Markierung beim überfahren mit der Maus
				if (bEnabled && !bSelected && SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, "" + iIndex))
				{
			    	paintRectangle
			    	(
			    		pGraphics, 
			    		pX + iTabSpace, 
			    		pY + pHeight - iHeight - iSelectionHeight + 1, 
			    		pWidth - iTabSpace - iTabSpace + 1, 
			    		iSelectionHeight, 
			    		TABCORNER, 
			    		SwingConstants.TOP, 
			    		SmartTheme.COL_TAB_ACTIVE_BACKGROUND,
			    		null,
			    		-1,
			    		null
			    	);
				}

		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX + iTabSpace, 
		    		pY, 
		    		pWidth - iTabSpace - iTabSpace + 1, 
		    		pHeight - iHeight + 1, 
		    		TABCORNER, 
		    		SwingConstants.TOP, 
		    		null, 
		    		null,
		    		-1,
		    		colBorder
		    	);
		    	
				//Grundlinie "öffnen" bzw. löschen
				if (bSelected)
				{
					if (bEnabled)
					{
						pGraphics.setColor(SmartTheme.COL_TAB_ACTIVE_BACKGROUND);
					}
					else
					{
						pGraphics.setColor(SmartTheme.COL_TAB_INACTIVE_BACKGROUND);
					}				
					
					pGraphics.drawLine(pX + iTabSpace, pY - 1, pX + pWidth - iTabSpace, pY - 1);
				}
				break;

			case JTabbedPane.RIGHT:
				//Markierung beim überfahren mit der Maus
				if (bEnabled && !bSelected && SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, "" + iIndex))
				{
			    	paintRectangle
			    	(
			    		pGraphics, 
			    		pX + pWidth - iHeight - iSelectionHeight + 1, 
			    		pY + iTabSpace, 
			    		iSelectionHeight, 
			    		pHeight - iTabSpace - iTabSpace + 1, 
			    		TABCORNER, 
			    		SwingConstants.LEFT, 
			    		SmartTheme.COL_TAB_ACTIVE_BACKGROUND,
			    		null,
			    		-1,
			    		null
			    	);				
				}

		    	paintRectangle
		    	(
		    		pGraphics, 
		    		pX, 
		    		pY + iTabSpace, 
		    		pWidth - iHeight + 1, 
		    		pHeight - iTabSpace - iTabSpace + 1, 
		    		TABCORNER, 
		    		SwingConstants.LEFT, 
		    		null, 
		    		null,
		    		-1,
		    		colBorder
		    	);			
		    	
				//Grundlinie "öffnen" bzw. löschen
				if (bSelected)
				{
					if (bEnabled)
					{
						pGraphics.setColor(SmartTheme.COL_TAB_ACTIVE_BACKGROUND);
					}
					else
					{
						pGraphics.setColor(SmartTheme.COL_TAB_INACTIVE_BACKGROUND);
					}				
					
					pGraphics.drawLine(pX - 1, pY + 1, pX - 1, pY + pHeight - 1);
				}
				break;
				
			default:
				//not supported
				break;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTabbedPaneContentBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JTabbedPane tab = (JTabbedPane)pContext.getComponent(); 
    	
    	Color colDot;
    	
    	if (tab.isEnabled())
    	{
    		pGraphics.setColor(SmartTheme.COL_TAB_BORDER_ENABLED);
    		
    		colDot = SmartTheme.COL_TAB_ACTIVE_BACKGROUND;
    	}
    	else
    	{
    		pGraphics.setColor(SmartTheme.COL_TAB_BORDER_DISABLED);
    		
    		colDot = SmartTheme.COL_TAB_INACTIVE_BACKGROUND;
    	}
    	
    	pGraphics.drawRect(pX, pY, pWidth - 1, pHeight - 1);
    	
    	switch (tab.getTabPlacement())
    	{
    		case JTabbedPane.TOP:
    	    	//pGraphics.drawRect(pX + 2, pY, pWidth - 5, pHeight - 3);
    	    	
    	    	//Schnittpunkt des inneren mit dem äußeren Rahmen übermalen
    	    	pGraphics.setColor(colDot);
    	    	pGraphics.drawLine(pX + 1, pY, pX + 1, pY);
    	    	pGraphics.drawLine(pX + pWidth - 2, pY, pX + pWidth - 2, pY);
    			break;
    			
    		case JTabbedPane.LEFT:
    	    	//pGraphics.drawRect(pX, pY + 2, pWidth - 3, pHeight - 5);
    	    	
    	    	//Schnittpunkt des inneren mit dem äußeren Rahmen übermalen
    	    	pGraphics.setColor(colDot);
    	    	pGraphics.drawLine(pX, pY + 1, pX, pY + 1);
    	    	pGraphics.drawLine(pX, pY + pHeight - 2, pX, pY + pHeight - 2);
    			break;

    		case JTabbedPane.BOTTOM:
    	    	//pGraphics.drawRect(pX + 2, pY + 2, pWidth - 5, pHeight - 3);
    	    	
    	    	//Schnittpunkt des inneren mit dem äußeren Rahmen übermalen
    	    	pGraphics.setColor(colDot);
    	    	pGraphics.drawLine(pX + 1, pY + pHeight - 1, pX + 1, pY + pHeight - 1);
    	    	pGraphics.drawLine(pX + pWidth - 2, pY + pHeight - 1, pX + pWidth - 2, pY + pHeight - 1);
    			break;
    			
    		case JTabbedPane.RIGHT:
    	    	//pGraphics.drawRect(pX + 2, pY + 2, pWidth - 3, pHeight - 5);
    	    	
    	    	//Schnittpunkt des inneren mit dem äußeren Rahmen übermalen
    	    	pGraphics.setColor(colDot);
    	    	pGraphics.drawLine(pX + pWidth - 1, pY + 1, pX + pWidth - 1, pY + 1);
    	    	pGraphics.drawLine(pX + pWidth - 1, pY + pHeight - 2, pX + pWidth - 1, pY + pHeight - 2);
    			break;
    			
    		default:
    			//not supported
    			break;
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintTabbedPaneContentBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JTabbedPane tab = (JTabbedPane)pContext.getComponent(); 
    	
    	if (tab.isEnabled())
    	{
    		pGraphics.setColor(SmartTheme.COL_TAB_ACTIVE_BACKGROUND);
    	}
    	else
    	{
    		pGraphics.setColor(SmartTheme.COL_TAB_INACTIVE_BACKGROUND);
    	}
    	
    	switch (tab.getTabPlacement())
    	{
    		case JTabbedPane.TOP:
    			pGraphics.drawRect(pX + 1, pY, pWidth - 3, pHeight - 2);
    			break;
    		case JTabbedPane.LEFT:
    			pGraphics.drawRect(pX, pY + 1, pWidth - 2, pHeight - 3);
    			break;
    		case JTabbedPane.BOTTOM:
    			pGraphics.drawRect(pX + 1, pY + 1, pWidth - 3, pHeight - 2);
    			break;
    		case JTabbedPane.RIGHT:
    			pGraphics.drawRect(pX + 1, pY + 1, pWidth - 2, pHeight - 3);
    			break;
    		default:
    			//keine weitere Variante
    			break;
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// SplitPane
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSplitPaneDragDivider(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, int pOrientation) 
	{
    	paintSplitPaneDividerBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
	}    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSplitPaneDividerBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	int iX;
    	int iY;
    	int iWidth;
    	int iHeight;
    	
		pGraphics.setColor(SmartTheme.COL_BACKGROUND);
		pGraphics.fillRect(pX, pY, pWidth, pHeight);

		if (((JSplitPane)pContext.getComponent()).getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
    	{
    		iX = pX;
    		iY = pY;
    		iWidth = pWidth;
    		iHeight = pHeight;
    		
    		// linke Trennlinie des Dividers
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	pGraphics.drawLine(iX, iY, iX, iY + iHeight);
        	
        	// rechte Trennlinie des Dividers
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	pGraphics.drawLine(iX + iWidth - 1, iY, iX + iWidth - 1, iY + iHeight);
        	
        	// Anfasser
        	int iMiddle = iHeight / 2;
        	
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	for (int i = 0, anz = 8, j = anz - 1; i < anz; i++, j -= 2)
        	{
            	pGraphics.drawLine(iX + 2, pY + iMiddle + j, iX + iWidth - 3, pY + iMiddle + j);
        	}
    	}
    	else
    	{
    		iX = pX;
    		iY = pY;
    		iWidth = pWidth;
    		iHeight = pHeight;
    		
    		// obere Trennlinie des Dividers
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	pGraphics.drawLine(iX, iY, iX + pWidth - 1, iY);
        	
        	// untere Trennlinie des Dividers
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	pGraphics.drawLine(iX, iY + iHeight - 1, iX + pWidth, iY + iHeight - 1);
        	
        	// Anfasser
        	int iMiddle = pWidth / 2;
        	
        	pGraphics.setColor(SmartTheme.COL_SPLIT_BORDER);
        	for (int i = 0, anz = 8, j = anz - 1; i < anz; i++, j -= 2)
        	{
            	pGraphics.drawLine(pX + iMiddle + j, iY + 2, pX + iMiddle + j, iY + pHeight - 3);
        	}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSplitPaneBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintSplitPaneBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Viewport
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintViewportBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComponent comp = pContext.getComponent();

    	//wenn eine JTabbedPane mit Scroll-Pfeilen verwendet wird
    	if ("TabbedPane.scrollableViewport".equals(comp.getName()))
    	{
			JTabbedPane tab = (JTabbedPane)comp.getParent(); 
			
			Rectangle rectOrigClip = pGraphics.getClipBounds();

			Insets insTab = tab.getInsets();
			
			int iScrollButtonSpace = 0;
			int iRealWidth;
			int iRealHeight;

			Component com;
			
			//das Clipping muss geändert werden, da ansonsten der Rahmen nicht bis zu den Scroll-Buttons reicht!
			if (tab.getTabPlacement() == JTabbedPane.TOP || tab.getTabPlacement() == JTabbedPane.BOTTOM)
			{
				iRealWidth = tab.getWidth();
				iRealHeight = pHeight;
				
				//die Breite der Buttons ermitteln, um diese fürs Clipping zu verwenden
				for (int i = 0, anz = tab.getComponentCount(); i < anz; i++)
				{
					com = tab.getComponent(i); 
					
					if (com instanceof JButton)
					{
						if (com.isShowing())
						{
							iScrollButtonSpace += com.getWidth(); 
						}
					}
				}

				iRealWidth = iRealWidth - iScrollButtonSpace - insTab.left - insTab.right;
			}
			else
			{
				iRealWidth  = pWidth;
				iRealHeight = tab.getHeight();
				
				//die Höhe der Buttons ermitteln, um diese fürs Clipping zu verwenden
				for (int i = 0, anz = tab.getComponentCount(); i < anz; i++)
				{
					com = tab.getComponent(i); 
					
					if (com instanceof JButton)
					{
						if (com.isShowing())
						{
							iScrollButtonSpace += com.getHeight();
						}
					}
				}

				iRealHeight = iRealHeight - iScrollButtonSpace - insTab.top - insTab.bottom;
			}
		
			//Die Insets der TabArea verwenden und nicht die des Viewport, da diese ev. nicht ident sind!
			Insets insTabArea = SynthLookAndFeel.getStyle(tab, Region.TABBED_PANE_TAB_AREA).getInsets(pContext, null);

			pGraphics.setClip
			(
				pX, 
				pY, 
				iRealWidth, 
				iRealHeight
			);
			
			paintTabbedPaneTabArea(tab, insTabArea, pGraphics, pX, pY, iRealWidth, iRealHeight, iScrollButtonSpace > 0);
			
			//das Clipping wieder zurücksetzen!
			pGraphics.setClip(rectOrigClip);
    	}
    	else if (((JViewport)comp).getView() instanceof JTextComponent)
    	{
    		//Wenn der Viewport eine Text Komponente beinhaltet, dann wird der Viewport und die ScrollPane 
    		//transparent da ansonsten die Ecken gezeichnet werden würden!
    		if (comp.isOpaque())
    		{
    			((JComponent)comp.getParent()).setOpaque(false);
    			comp.setOpaque(false);
    			comp.getParent().repaint();
    		}
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintViewportBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }    
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Menu
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintMenuBarBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Container con = pContext.getComponent().getParent().getParent().getParent();
    	
    	//Die ToolBar in einem internal frame wird ebenfalls inaktiv dargestellt, da es sonst
    	//nicht gut aussieht!
    	if (con instanceof JInternalFrame && !((JInternalFrame)con).isSelected())
    	{
        	pGraphics.setColor(SmartTheme.COL_INTFRAME_BACKGROUND_INACTIVE);
    	}
    	else
    	{
	    	pGraphics.setColor(SmartTheme.COL_MENU_BAR_BACKGROUND);
    	}
    	
    	pGraphics.fillRect(pX, pY, pWidth, pHeight - 1);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintMenuBarBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Container con = pContext.getComponent().getParent().getParent().getParent();
    	
    	//Die ToolBar in einem internal frame wird ebenfalls inaktiv dargestellt, da es sonst
    	//nicht gut aussieht. Aus diesem Grund wird auch die Farbe der Rahmenlinie geändert!
    	if (con instanceof JInternalFrame && !((JInternalFrame)con).isSelected())
    	{
        	pGraphics.setColor(SmartTheme.COL_INTFRAME_INNER_BORDER_INACTIVE);
    	}
    	else
    	{
    		pGraphics.setColor(SmartTheme.COL_MENU_BAR_BORDER);
    	}
    	
    	pGraphics.drawLine(pX, pY + pHeight - 1, pX + pWidth - 1, pY + pHeight - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintMenuBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JMenu menu = (JMenu)pContext.getComponent();
    	
    	ImageIcon ico = htMenuArrowCache.get(menu);

    	Color colArrow;
    	
    	Graphics gImage = null;
    	
		if (ico != null)
		{
			gImage = ico.getImage().getGraphics();
		}
    	
    	//Beim überfahren oder wenn gedrückt wurde, wird die Markierung angezeigt
    	if ((SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, Boolean.valueOf(true))
    		 || SmartLookAndFeel.isState(pContext, SynthConstants.SELECTED, null))
    		&& menu.isEnabled())
    	{
    		colArrow = SmartTheme.COL_MENU_ARROW_MOUSE_OVER;
    		
        	pGraphics.setColor(SmartTheme.COL_MENU_BACKGROUND_OVER);
        	pGraphics.fillRect(pX, pY, pWidth, pHeight - 1);
    	}
    	else
    	{
    		if (menu.isEnabled())
    		{
    			colArrow = SmartTheme.COL_MENU_ARROW;
    		}
    		else
    		{
    			colArrow = SmartTheme.COL_MENU_DISABLED;
    		}
    	}
    	
		if (gImage != null)
		{
    		//Trick: Der Arrow könnte normalerweise nicht geändert werden, aber da die Image-Referenz im Cache
    		//       ist, kann das Bild beliebig geändert werden!
    		SmartPainter.paintArrow(gImage, 8, 3, 1, 7, SwingUtilities.EAST, SmartPainter.TYPE_ARROW_FILLED, colArrow);
		}
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintMenuBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public void paintMenuItemBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	if (SmartLookAndFeel.isState(pContext, SynthConstants.MOUSE_OVER, null))
    	{
        	pGraphics.setColor(SmartTheme.COL_MENU_BACKGROUND_OVER);
        	pGraphics.fillRect(pX, pY, pWidth, pHeight);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void paintCheckBoxMenuItemBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintMenuItemBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }

    /**
     * {@inheritDoc}
     */
    public void paintRadioButtonMenuItemBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintMenuItemBackground(pContext, pGraphics, pX, pY, pWidth, pHeight);
    }
    
    /**
     * {@inheritDoc}
     */
    public void paintSeparatorBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	pGraphics.setColor(SmartTheme.COL_MENU_SEPARATOR);
    	pGraphics.drawLine(pX, pY, pWidth, pY);
    }
    
    /**
     * {@inheritDoc}
     */
    public void paintPopupMenuBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComponent comParent = (JComponent)pContext.getComponent().getParent();
    	
    	//nur bei 1.5 der Fall!
    	if (comParent.isOpaque())
    	{
    		comParent.setOpaque(false);
    		comParent.repaint();
    	}
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, SmartTheme.COL_MENU_BACKGROUND, null, -1, null);
    }

    /**
     * {@inheritDoc}
     */
    public void paintPopupMenuBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, SmartTheme.COL_MENU_BORDER);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ComboBox
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void paintComboBoxBackground(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComboBox box = (JComboBox)pContext.getComponent();

    	Color colBack;
    	
    	if (!box.isEnabled())
    	{
    		colBack = SmartTheme.COL_TEXT_DISABLED_BACKGROUND;
    	}
    	else
    	{
    		colBack = SmartTheme.COL_BACKGROUND_TEXT;
    	}       	
    	
    	//ComboBox Hintergrund
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, colBack, null, -1, null);
    }
    
    /**
     * {@inheritDoc}
     */
	public void paintComboBoxBorder(SynthContext pContext, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	JComboBox box = (JComboBox)pContext.getComponent();

    	Color colBorder;
    	
    	if (!box.isEnabled())
    	{
    		colBorder = SmartTheme.COL_TEXT_BORDER_DISABLED;
    	}
    	else
    	{
    		colBorder = SmartTheme.COL_TEXT_BORDER;
    	}    	
  
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, colBorder);
    }    
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Paints the border of the drag toolbar panel.
     * 
     * @param pToolBar the toolbar
     * @param pGraphics the graphics context
     * @param pX the x coordinate in the canvas
     * @param pY the y coordinate in the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     * @param pCanDock <code>true</code> if the toolbar can dock at the current position
     */
    public static void paintToolBarDragWindowBorder(JToolBar pToolBar, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, boolean pCanDock)
    {
    	if (pCanDock)
    	{
        	pGraphics.setColor(SmartTheme.COL_TOOLBAR_DOCKING);
        	pGraphics.drawRect(pX, pY, pWidth - 1, pHeight - 1);
    	}
    	else
    	{
        	pGraphics.setColor(SmartTheme.COL_TOOLBAR_FLOATING);
        	pGraphics.drawRect(pX, pY, pWidth - 1, pHeight - 1);
    	}
    }    	
    
    /**
     * Paints the background of the drag toolbar panel.
     * 
     * @param pToolBar the toolbar
     * @param pGraphics the graphics context
     * @param pX the x coordinate in the canvas
     * @param pY the y coordinate in the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     * @param pCanDock <code>true</code> if the toolbar can dock at the current position
     */
    public static void paintToolBarDragWindowBackground(JToolBar pToolBar, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight, boolean pCanDock)
    {
		pToolBar.paint(pGraphics);

//		pGraphics.setColor(SmartTheme.COL_TOOLBAR_BACKGROUND);
//    	pGraphics.fillRect(pX, pY, pWidth, pHeight);
    }

    /**
     * Paints the border of the toolbar panel.
     * 
     * @param pToolBar the toolbar
     * @param pGraphics the graphics context
     * @param pX the x coordinate in the canvas
     * @param pY the y coordinate in the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     */
    public static void paintToolBarBorder(JToolBar pToolBar, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	if (pToolBar.getParent() instanceof JToolBar)
    	{
    		paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 4, -1, null, null, -1, SmartTheme.COL_TOOLBAR_SUB_BORDER);
    	}
    }

    /**
     * Paints the background of the toolbar panel.
     * 
     * @param pToolBar the toolbar
     * @param pGraphics the graphics context
     * @param pX the x coordinate in the canvas
     * @param pY the y coordinate in the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     */
    public static void paintToolBarBackground(JToolBar pToolBar, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	if (pToolBar.getParent() instanceof JToolBar)
    	{
    		//ev. hat der Parent eine benutzerdefinierte Farbe!
    		Color color = getBackgroundColor(pToolBar.getParent(), SmartTheme.COL_TOOLBAR_BACKGROUND);
    		
        	pGraphics.setColor(color);
        	pGraphics.fillRect(pX, pY, pWidth, pHeight);

        	Color colorEnd;
        	int iGradient;

        	//benutzerdefinierte Farben werden voll gemalt!
        	if (!(color instanceof ColorUIResource))
        	{
        		color = pToolBar.getBackground();
        		colorEnd = null;
        		
        		iGradient = -1;
        	}
        	else
        	{
        		//Gradient malen
	        	if (pToolBar.getOrientation() == JToolBar.HORIZONTAL)
	        	{
	            	//ev. die Benutzerdefinierte Farbe verwenden
	            	colorEnd = SmartTheme.COL_TOOLBAR_SUB_BACKGROUND[1];
	            	color = SmartTheme.COL_TOOLBAR_SUB_BACKGROUND[0];
	            	
	            	iGradient = GRADIENT_VERTICAL;
	        	}
	        	else
	        	{
	            	color = SmartTheme.COL_TOOLBAR_SUB_BACKGROUND[0];
	            	colorEnd = SmartTheme.COL_TOOLBAR_SUB_BACKGROUND[1];
	            	
	            	iGradient = GRADIENT_HORIZONTAL;
	        	}
        	}
        	
    		paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 4, -1, color, colorEnd, iGradient, null);
    	}
    	else
    	{
    		Color color = getBackgroundColor(pToolBar, SmartTheme.COL_TOOLBAR_BACKGROUND);
    			
    		if (!(color instanceof ColorUIResource))
    		{
    			color = pToolBar.getBackground();
    		}
    		
        	pGraphics.setColor(color);
        	pGraphics.fillRect(pX, pY, pWidth, pHeight);
    	}
    	
    	//Anfasser nur anzeigen, wenn aktiviert
    	if (pToolBar.isFloatable())
    	{
	    	pGraphics.setColor(SmartTheme.COL_TOOLBAR_HANDLE);
	    	
	    	int iHandleSteps = 4;
	    	
	    	Dimension dimHandleSize = UIManager.getDimension(SmartTheme.NAME_TOOLBAR_HANDLESIZE);
	    	
	    	int iX;
	    	int iY;
	    	
	    	if (pToolBar.getOrientation() == JToolBar.HORIZONTAL)
	    	{
	    		iY = pY;

	    		//Orientation rechts nach links berücksichtigen!
	    		if (SmartLookAndFeel.isLeftToRightOrientation(pToolBar))
		    	{
		    		iX = pX;
		    	}
		    	else
		    	{
		    		iX = pX + pWidth - dimHandleSize.width - 1;
		    	}

		    	int iMiddle = pHeight / 2;
	    		
		    	for (int i = 0, anz = iHandleSteps, j = anz - 1; i < anz; i++, j -= 2)
		    	{
		        	pGraphics.drawLine(iX + 2, iY + iMiddle + j, iX + dimHandleSize.width - 2, iY + iMiddle + j);
		    	}
	    	}
	    	else
	    	{
	    		int iMiddle = pWidth / 2;
	    		
		    	for (int i = 0, anz = iHandleSteps, j = anz - 1; i < anz; i++, j -= 2)
		    	{
		        	pGraphics.drawLine(pX + iMiddle + j, pY + 2, pX + iMiddle + j, pY + dimHandleSize.height - 2);
		    	}
	    	}
    	}
    }
    
    /**
     * Paints a rectangle with or without rounded coners.
     * 
     * <pre>
     * Corner: 1
     * 
     *  XXXXXXX
     * X       X
     * X       X
     * X       X
     *  XXXXXXX
     *  
     * Corner: 2
     * 
     *   XXXXXXXX
     *  X        X
     * X          X
     * X          X 
     * X          X
     * X          X
     *  X        X
     *   XXXXXXXX
     *   
     * Corner: 3
     * 
     *    XXXXXXXXX
     *  XX         XX
     *  X           X
     * X             X
     * X             X
     * X             X
     * X             X
     *  X           X
     *  XX         XX
     *    XXXXXXXXX
     *    
     * Corner: 4
     * 
     *     XXXXXXXXXXXXX
     *   XX             XX
     *  X                 X
     *  X                 X
     * X                   X
     * X                   X
     * X                   X
     * X                   X
     *  X                 X
     *  X                 X
     *   XX             XX
     *     XXXXXXXXXXXXX
     * </pre> 
     * 
     * @param pGraphics the graphics context for the component
     * @param pX the x coordinate
     * @param pY the y coordinate
     * @param pWidth the canvas width
     * @param pHeight the canvas height
     * @param pCorner the corner style
     * @param pOpened the position whitout border. One of the following constants: 
     *                {@link SwingConstants#TOP}, {@link SwingConstants#LEFT},
     *                {@link SwingConstants#BOTTOM}, {@link SwingConstants#RIGHT}
     *                or <code>-1</code> if the border is closed 
     * @param pBackgroundStart the background color. <code>null</code> for  
     *                         a transparent background (or start color for a gradient)
     * @param pBackgroundEnd the background for the end of a gradient color or 
     *                       <code>null</code> for a transparent background
     * @param pGradientDirection the direction for the gradient, if wished. One of the
     *                           following: {@link #GRADIENT_HORIZONTAL}, {@link #GRADIENT_VERTICAL}
     * @param pBorder the border color. <code>null</code> for a transparent border
     */
    public static void paintRectangle(Graphics pGraphics, 
    								  int pX, 
    								  int pY, 
    								  int pWidth, 
    								  int pHeight, 
    								  int pCorner,
    								  int pOpened,
    								  Color pBackgroundStart,
    								  Color pBackgroundEnd,
    								  int pGradientDirection,
    								  Color pBorder)
    {
		//Hintergrund darstellen
    	if (pBackgroundStart != null)
    	{
    		if (pBackgroundEnd == null)
    		{
        		pGraphics.setColor(pBackgroundStart);
    		}
    		
    		switch (pCorner)
    		{
    			case 1:
    				if (pBackgroundEnd != null)
    				{
    					Image image = createLinearGradient(pWidth - 1, pHeight - 1, pBackgroundStart, pBackgroundEnd, pGradientDirection);
    					
    					pGraphics.drawImage(image, pX + 1, pY + 1, pWidth - 1, pHeight - 1, null);
    				}
    				else
    				{
    					pGraphics.fillRect(pX + 1, pY + 1, pWidth - 2, pHeight - 2);
    				}
		    		break;
    			case 2:
    				if (pBackgroundEnd != null)
    				{
    					Image image = createLinearGradient(pWidth - 2, pHeight - 2, pBackgroundStart, pBackgroundEnd, pGradientDirection);
    					
    					pGraphics.drawImage(image, pX + 1, pY + 1, pWidth - 2, pHeight - 2, null);
    				}
    				else
    				{
			    		pGraphics.fillRect(pX + 1, pY + 1, pWidth - 2, pHeight - 2);
    				}
					break;
    			case 3:
    				if (pBackgroundEnd != null)
    				{
    					Image image = createLinearGradient(pWidth - 2, pHeight - 2, pBackgroundStart, pBackgroundEnd, pGradientDirection);
    					
    					pGraphics.drawImage(image, pX + 1, pY + 1, pWidth - 2, pHeight - 2, null);
    				}
    				else
    				{
    					pGraphics.fillRect(pX + 1, pY + 1, pWidth - 2, pHeight - 2);
    				}
					break;
    			case 4:
    				
    				int iTop    = 0;
    				int iLeft   = 0;
    				int iBottom = 0;
    				int iRight  = 0;
    				
    	    		switch (pOpened)
    	    		{
    	    			case SwingConstants.TOP:
    	    				iTop = 1;
    	    				break;
    	    			case SwingConstants.LEFT:
    	    				iLeft = 1;
    	    				break;
    	    			case SwingConstants.BOTTOM:
    	    				iBottom = 1;
    	    				break;
    	    			case SwingConstants.RIGHT:
    	    				iRight = 1;
    	    				break;
    	    			default:
    	    				//gibts nicht
    	    				break;
    	    		}
    				
    				if (pBackgroundEnd != null)
    				{
						if (pGradientDirection == GRADIENT_HORIZONTAL)
						{
							//Bei horizontalem Gradient muss links und rechts eine Linie gezogen werden
							//oben und unten würde nicht zum gradient passen
							if (iLeft == 0)
							{
	    						pGraphics.setColor(pBackgroundStart);
	    						pGraphics.drawLine(pX + 1, pY + 2 - iTop, pX + 1, pY + pHeight - 3 + iBottom);
							}
							
							Image image = createLinearGradient(pWidth - 4 + iLeft + iRight, pHeight - 2, pBackgroundStart, pBackgroundEnd, pGradientDirection);
	    					pGraphics.drawImage(image, pX + 2 - iLeft, pY + 1, pWidth - 4 + iLeft + iRight, pHeight - 2, null);

							if (iRight == 0)
							{
	    						pGraphics.setColor(pBackgroundEnd);
	    						pGraphics.drawLine(pX + pWidth - 2, pY + 2 - iTop, pX + pWidth - 2, pY + pHeight - 3 + iBottom);
							}
						}
						else
						{
							//Bei vertikalem Gradient muss oben und unten eine Linie gezogen werden
							//links und rechts würde nicht zum gradient passen
	    					if (iTop == 0)
	    					{
	    						pGraphics.setColor(pBackgroundStart);
	    						pGraphics.drawLine(pX + 2 - iLeft, pY + 1, pX + pWidth - 3 + iRight, pY + 1);
	    					}
	
	    					Image image = createLinearGradient(pWidth - 2, pHeight - 4 + iBottom + iTop, pBackgroundStart, pBackgroundEnd, pGradientDirection);
	    					pGraphics.drawImage(image, pX + 1, pY + 2 - iTop, pWidth - 2, pHeight - 4 + iBottom + iTop, null);
	    					
	    					if (iBottom == 0)
	    					{
	    						pGraphics.setColor(pBackgroundEnd);
	    						pGraphics.drawLine(pX + 2 - iLeft, pY + pHeight - 2, pX + pWidth - 3 + iRight, pY + pHeight - 2);
	    					}
						}
    				}
    				else
    				{
    					//Vollton Darstellung
    					
    					if (iTop == 0)
    					{
    						pGraphics.drawLine(pX + 2 - iLeft, pY + 1, pX + pWidth - 3 + iRight, pY + 1);
    					}
    					
    					pGraphics.fillRect(pX + 1, pY + 2 - iTop, pWidth - 2, pHeight - 4 + iBottom + iTop);
    					
    					if (iBottom == 0)
    					{
    						pGraphics.drawLine(pX + 2 - iLeft, pY + pHeight - 2, pX + pWidth - 3 + iRight, pY + pHeight - 2);
    					}
    				}
    				break;

    			default:
    				if (pBackgroundEnd != null)
    				{
    					Image image = createLinearGradient(pWidth, pHeight, pBackgroundStart, pBackgroundEnd, pGradientDirection);
    					
    					pGraphics.drawImage(image, pX, pY, pWidth, pHeight, null);
    				}
    				else
    				{
			    		pGraphics.fillRect(pX, pY, pWidth, pHeight);
    				}
					break;
    		}
    	}
    	
    	if (pBorder != null)
    	{
    		pGraphics.setColor(pBorder);
    		
    		int iLeftWidth   = 0;
    		int iRightWidth  = 0;
    		int iTopHeight    = 0;
    		int iBottomHeight = 0;

    		switch (pOpened)
    		{
    			case SwingConstants.TOP:
    				iTopHeight = pCorner;
    				break;
    			case SwingConstants.LEFT:
    				iLeftWidth = pCorner;
    				break;
    			case SwingConstants.BOTTOM:
    				iBottomHeight = pCorner;
    				break;
    			case SwingConstants.RIGHT:
    				iRightWidth = pCorner;
    				break;
    			default:
    				//gibts nicht
    				break;
    		}
    		
    		switch (pCorner)
    		{
    			case 2:
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.LEFT)
    				{
	    				pGraphics.drawLine(pX + 1, pY + 1, pX + 1, pY + 1);											//links oben
    				}
    				
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.RIGHT)
    				{
	    				pGraphics.drawLine(pX + pWidth - 2, pY + 1, pX + pWidth - 2, pY + 1);						//rechts oben
    				}
    				
    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.LEFT)
    				{
    					pGraphics.drawLine(pX + 1, pY + pHeight - 2, pX + 1, pY + pHeight - 2);						//links unten
    				}

    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.RIGHT)
    				{
    					pGraphics.drawLine(pX + pWidth - 2, pY + pHeight - 2, pX + pWidth - 2, pY + pHeight - 2);	//rechts unten
    				}
    				break;
    				
    			case 3:
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.LEFT)
    				{
	    				pGraphics.drawLine(pX + 1, pY + 1, pX + 2, pY + 1);											//1. Reihe, links oben
	    				pGraphics.drawLine(pX + 1, pY + 2, pX + 1, pY + 2);											//2. Reihe, links oben
    				}
    				
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.RIGHT)
    				{
	    				pGraphics.drawLine(pX + pWidth - 3, pY + 1, pX + pWidth - 2, pY + 1);						//1. Reihe, rechts oben
	    				pGraphics.drawLine(pX + pWidth - 2, pY + 2, pX + pWidth - 2, pY + 2);						//1. Reihe, rechts oben
    				}
    				
    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.LEFT)
    				{
    					pGraphics.drawLine(pX + 1, pY + pHeight - 3, pX + 1, pY + pHeight - 3);						//1. Reihe, links unten
    					pGraphics.drawLine(pX + 1, pY + pHeight - 2, pX + 2, pY + pHeight - 2);						//1. Reihe, links unten
    				}

    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.RIGHT)
    				{
    					pGraphics.drawLine(pX + pWidth - 2, pY + pHeight - 3, pX + pWidth - 2, pY + pHeight - 3);	//1. Reihe, rechts unten
    					pGraphics.drawLine(pX + pWidth - 3, pY + pHeight - 2, pX + pWidth - 2, pY + pHeight - 2);	//2. Reihe, rechts unten
    				}
    				break;
    			case 4:
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.LEFT)
    				{
	    				pGraphics.drawLine(pX + 2, pY + 1, pX + 3, pY + 1);											//1. Reihe, links oben
	    				pGraphics.drawLine(pX + 1, pY + 2, pX + 1, pY + 3);											//2. Reihe, links oben
    				}
    				
    				if (pOpened != SwingConstants.TOP && pOpened != SwingConstants.RIGHT)
    				{
	    				pGraphics.drawLine(pX + pWidth - 4, pY + 1, pX + pWidth - 3, pY + 1);						//1. Reihe, rechts oben
	    				pGraphics.drawLine(pX + pWidth - 2, pY + 2, pX + pWidth - 2, pY + 3);						//1. Reihe, rechts oben
    				}
    				
    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.LEFT)
    				{
    					pGraphics.drawLine(pX + 1, pY + pHeight - 4, pX + 1, pY + pHeight - 3);						//1. Reihe, links unten
    					pGraphics.drawLine(pX + 2, pY + pHeight - 2, pX + 3, pY + pHeight - 2);						//1. Reihe, links unten
    				}

    				if (pOpened != SwingConstants.BOTTOM && pOpened != SwingConstants.RIGHT)
    				{
    					pGraphics.drawLine(pX + pWidth - 2, pY + pHeight - 4, pX + pWidth - 2, pY + pHeight - 3);	//1. Reihe, rechts unten
    					pGraphics.drawLine(pX + pWidth - 4, pY + pHeight - 2, pX + pWidth - 3, pY + pHeight - 2);	//2. Reihe, rechts unten
    				}
    				break;
    			default:
    				//nichts zu malen
    				break;
    		}
    		
    		//Seitenlinien
    		if (pCorner >= 0)
    		{
				if (pOpened != SwingConstants.TOP)
				{
					pGraphics.drawLine(pX + pCorner - iLeftWidth, pY, pX + pWidth - 1 - pCorner + iRightWidth, pY);											//oben
				}
				
				if (pOpened != SwingConstants.LEFT)
				{
					pGraphics.drawLine(pX, pY + pCorner - iTopHeight, pX, pY + pHeight - 1 - pCorner + iBottomHeight);											//links
				}
				
				if (pOpened != SwingConstants.BOTTOM)
				{
					pGraphics.drawLine(pX + pCorner - iLeftWidth, pY + pHeight - 1, pX + pWidth - 1 - pCorner + iRightWidth, pY + pHeight - 1);	//unten
				}
	
				if (pOpened != SwingConstants.RIGHT)
				{
					pGraphics.drawLine(pX + pWidth - 1, pY + pCorner - iTopHeight, pX + pWidth - 1, pY + pHeight - 1 - pCorner + iBottomHeight);	//rechts
				}
    		}
    	}
    }

    /**
     * Paints an arrow.
     * 
     * @param pGraphics the graphics context
     * @param pX the x coordinate as center point
     * @param pY the y coordinate as center point
     * @param pStroke the thickness of the arrow (not used for filled arrows)
     * @param pLength the length of the arrow
     * @param pOrientation the orientation of the arrow is one of 
     *        {@link SwingUtilities#NORTH}, {@link SwingUtilities#SOUTH}, 
     *        {@link SwingUtilities#WEST}, {@link SwingUtilities#EAST} 
     * @param iType the arrow type is one of {@link #TYPE_ARROW_FILLED}, {@link #TYPE_ARROW_LINES}
     * @param pColor the arrow color
     */
    private static void paintArrow(Graphics pGraphics, 
    		                       int pX, 
    		                       int pY, 
    		                       int pStroke, 
    		                       int pLength, 
    		                       int pOrientation, 
    		                       int iType, Color pColor)
    {
    	int iLength = pLength;
    	
    	pGraphics.setColor(pColor);

    	switch (iType)
    	{
    		case TYPE_ARROW_LINES:
		    	for (int i = 0, nr = 0; i < pStroke; i++, nr++)
		    	{
		    		if (nr == 1)
		    		{
		    			iLength--;    			
		    		}
		    		
		    		switch (pOrientation)
			    	{
			    		case SwingUtilities.WEST:
			    			
				    		for (int j = 0, x = pX - 3 + i, y = pY; j < iLength; j++)
					    	{
					    		pGraphics.drawLine(x + j, y - j, x + j, y - j);
					    		pGraphics.drawLine(x + j, y + j, x + j, y + j);
					    	}
				    		break;
			    		case SwingUtilities.EAST:
				    		for (int j = 0, x = pX + 3 - i, y = pY; j < iLength; j++)
					    	{
					    		pGraphics.drawLine(x - j, y - j, x - j, y - j);
					    		pGraphics.drawLine(x - j, y + j, x - j, y + j);
					    	}
				    		break;
			    		case SwingUtilities.NORTH:
				    		for (int j = 0, x = pX, y = pY - 3 + i; j < iLength; j++)
					    	{
					    		pGraphics.drawLine(x + j, y + j, x + j, y + j);
					    		pGraphics.drawLine(x - j, y + j, x - j, y + j);
					    	}
				    		break;
			    		case SwingUtilities.SOUTH:
				    		for (int j = 0, x = pX, y = pY + 3 - i; j < iLength; j++)
					    	{
					    		pGraphics.drawLine(x + j, y - j, x + j, y - j);
					    		pGraphics.drawLine(x - j, y - j, x - j, y - j);
					    	}
				    		break;
				    	default:
				    		//nicht unterstützt
				    		break;
				    }
		    	}
		    	break;
    		case TYPE_ARROW_FILLED:
    			//Anzahl der Linien errechnen
    			int iLineCount = pLength / 2 + pLength % 2;
    			int iXPos;
    			int iYPos;

	    		switch (pOrientation)
		    	{
		    		case SwingUtilities.WEST:
		    			iXPos = pX + iLineCount / 2;
		    			iYPos = pY - pLength / 2;
		    			
				    	for (int i = 0; i < iLineCount; i++)
				    	{
				    		pGraphics.drawLine(iXPos - i, iYPos + i, iXPos - i, iYPos + iLength - i - 1);
				    	}
			    		break;
		    		case SwingUtilities.EAST:
		    			iXPos = pX - iLineCount / 2;
		    			iYPos = pY - pLength / 2;
		    			
				    	for (int i = 0; i < iLineCount; i++)
				    	{
				    		pGraphics.drawLine(iXPos + i, iYPos + i, iXPos + i, iYPos + iLength - i - 1);
				    	}
			    		break;
		    		case SwingUtilities.NORTH:
		    			iXPos = pX - pLength / 2;
		    			iYPos = pY + iLineCount / 2;
		    			
				    	for (int i = 0; i < iLineCount; i++)
				    	{
				    		pGraphics.drawLine(iXPos + i, iYPos - i, iXPos + iLength - i - 1, iYPos - i);
				    	}
			    		break;
		    		case SwingUtilities.SOUTH:
		    			iXPos = pX - pLength / 2;
		    			iYPos = pY - iLineCount / 2;
		    			
				    	for (int i = 0; i < iLineCount; i++)
				    	{
				    		pGraphics.drawLine(iXPos + i, iYPos + i, iXPos + iLength - i - 1, iYPos + i);
				    	}
			    		break;
			    	default:
			    		//nicht unterstützt
			    		break;
			    }
    			break;
    		default:
    			//nicht unterstützt
    			break;
    	}
    }
    
    /**
     * Paints a border under the tabs, in the tab area. The height of the border will be calculated through the
     * tab area insets.
     *  
     * @param pTab the {@link JTabbedPane}
     * @param pInsets the insets of the tab area
     * @param pGraphics the prepared graphics context
     * @param pX the x coordinate
     * @param pY the y coordinate
     * @param pWidth the width of the area
     * @param pHeight the height of the area
     * @param pButtonsAvailable <code>true</code> if the scroll buttons are visible
     */
    private void paintTabbedPaneTabArea(JTabbedPane pTab, 
    									Insets pInsets, 
    									Graphics pGraphics, 
    									int pX, 
    									int pY, 
    									int pWidth, 
    									int pHeight, 
    									boolean pButtonsAvailable)
    {
    	Color colBack;
    	Color colBorder;
    	
    	//Über den synthcontext bekommt man leider nicht den Enabled Status des Tabs mit!
		if (pTab.isEnabled())
		{
			colBack = SmartTheme.COL_TAB_ACTIVE_BACKGROUND;
			colBorder = SmartTheme.COL_TAB_BORDER_ENABLED;
		}
		else
		{
			colBack = SmartTheme.COL_BACKGROUND;
			colBorder = SmartTheme.COL_TAB_BORDER_DISABLED;
		}
		
		int iSpace = 0;

		//Der Rahmen wird mitgezeichnet (muss hier passieren, da paintViewportBorder nicht aufgerufen wird)
		
		switch (pTab.getTabPlacement())
		{
			case JTabbedPane.TOP:
		    	//abhängig von den Insets wird die Höhe des Blocks dargestellt
				iSpace = pInsets.bottom;
				
				pGraphics.setColor(colBack);
		    	pGraphics.fillRect(pX, pY + pHeight - iSpace, pWidth, iSpace);
		    	
				pGraphics.setColor(colBorder);
		    	//obere Linie auf der Grundlinie der Tabs
		    	pGraphics.drawLine(pX, pY + pHeight - iSpace, pX + pWidth - 1, pY + pHeight - iSpace);
		    	//linke Begrenzung
		    	pGraphics.drawLine(pX, pY + pHeight - iSpace, pX, pY + pHeight);
		    	
		    	if (!pButtonsAvailable)
		    	{
			    	//rechte Begrenzung
			    	pGraphics.drawLine(pX + pWidth - 1, pY + pHeight - iSpace, pX + pWidth - 1, pY + pHeight);
		    	}
		    	
		    	break;
		    	
			case JTabbedPane.LEFT:
		    	//abhängig von den Insets wird die Höhe des Blocks dargestellt
				iSpace = pInsets.right;

				pGraphics.setColor(colBack);
				pGraphics.fillRect(pX + pWidth - iSpace, pY, iSpace, pHeight);
		    	
				pGraphics.setColor(colBorder);
		    	//linke Linie am rechten Rand der Tabs
		    	pGraphics.drawLine(pX + pWidth - iSpace, pY, pX + pWidth - iSpace, pY + pHeight - 1);
		    	//obere Begrenzung
		    	pGraphics.drawLine(pX + pWidth - iSpace, pY, pX + pWidth - 1, pY);
		    	
		    	if (!pButtonsAvailable)
		    	{
			    	//untere Begrenzung
			    	pGraphics.drawLine(pX + pWidth - iSpace, pY + pHeight - 1, pX + pWidth - 1, pY + pHeight - 1);
		    	}
				break;

			case JTabbedPane.BOTTOM:
		    	//abhängig von den Insets wird die Höhe des Blocks dargestellt
				iSpace = pInsets.bottom;
				
				pGraphics.setColor(colBack);
		    	pGraphics.fillRect(pX, pY, pWidth, iSpace);
		    	
				pGraphics.setColor(colBorder);
		    	//untere Linie auf der Grundlinie der Tabs
		    	pGraphics.drawLine(pX, pY + iSpace - 1, pX + pWidth - 1, pY + iSpace - 1);
		    	//linke Begrenzung
		    	pGraphics.drawLine(pX, pY - 1, pX, pY + iSpace - 1);
		    	
		    	if (!pButtonsAvailable)
		    	{
			    	//rechte Begrenzung
			    	pGraphics.drawLine(pX + pWidth - 1, pY - 1, pX + pWidth - 1, pY + iSpace - 1);
		    	}
				break;
				
			case JTabbedPane.RIGHT:
		    	//abhängig von den Insets wird die Höhe des Blocks dargestellt
				iSpace = pInsets.left;

				pGraphics.setColor(colBack);
				pGraphics.fillRect(pX, pY, iSpace, pHeight);
		    	
				pGraphics.setColor(colBorder);
		    	//rechte Linie am linken Rand der Tabs
		    	pGraphics.drawLine(pX + iSpace - 1, pY, pX + iSpace - 1, pY + pHeight - 1);
		    	//obere Begrenzung
		    	pGraphics.drawLine(pX, pY, pX + iSpace - 1, pY);
		    	
		    	if (!pButtonsAvailable)
		    	{
		    		//untere Begrenzung
		    		pGraphics.drawLine(pX, pY + pHeight - 1, pX + iSpace - 1, pY + pHeight - 1);
		    	}
				break;
		    	
			default:
				//not supported
				break;
		}
    }
    
    /**
     * Paints the symbols for buttons in the title pane of an internal frame.
     * 
     * @param pFrame the internal frame
     * @param pButton the button
     * @param pGraphics the graphics context of the button
     * @param pX the x coordinate of the button
     * @param pY the y coordinate of the button
     * @param pWidth the width of the button
     * @param pHeight the height of the button
     * @param pColor the color of the symbol
     */
    private void paintNorthPaneButtonSymbol(JInternalFrame pFrame, 
    										JButton pButton, 
    										Graphics pGraphics, 
    										int pX, 
    										int pY, 
    										int pWidth, 
    										int pHeight, 
    										Color pColor)
    {
    	String sName = pButton.getName();
    	
    	boolean bRestore = false;
    	
    	if (SmartLookAndFeel.isNorthPaneCloseButton(sName))
    	{
    		paintButtonSymbol(SYMBOL_CLOSE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    	}
    	else if (SmartLookAndFeel.isNorthPaneMinButton(sName))
    	{
    		if (pFrame.isIcon())
    		{
    			bRestore = true;
    		}
    		else
    		{
    			paintButtonSymbol(SYMBOL_MINIMIZE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    		}
    	}
    	else if (SmartLookAndFeel.isNorthPaneMaxButton(sName))
    	{
    		//Wenn das Fenster im minimierten(iconified) Zustand maximiert ist, dann wird der
    		//max Button dennoch wie gewohnt dargestellt, ansonsten wären 2 restores vorhanden :)
    		if (pFrame.isMaximum() && !pFrame.isIcon())
    		{
    			bRestore = true;
    		}
    		else
    		{
    			paintButtonSymbol(SYMBOL_MAXIMIZE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    		}
    	}
    	
    	//das Restore-Symbol darstellen (kann bei Min und Max Button der Fall sein)
    	if (bRestore)
    	{
    		paintButtonSymbol(SYMBOL_RESTORE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    	}
    }
    
    /**
     * Paints the symbols for buttons in the title pane of a frame or dialog.
     * 
     * @param pWindow the window (frame, dialog)
     * @param pButton the button
     * @param pGraphics the graphics context of the button
     * @param pX the x coordinate of the button
     * @param pY the y coordinate of the button
     * @param pWidth the width of the button
     * @param pHeight the height of the button
     * @param pColor the color of the symbol
     */
    private void paintSmartTitlePaneButtonSymbol(Window pWindow, 
    											 JButton pButton, 
    											 Graphics pGraphics, 
    											 int pX, 
    											 int pY, 
    											 int pWidth, 
    											 int pHeight, 
    											 Color pColor)
    {
    	String sName = pButton.getName();
    	
    	if (SmartLookAndFeel.isWindowCloseButton(sName))
    	{
    		paintButtonSymbol(SYMBOL_CLOSE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    	}
    	else if (SmartLookAndFeel.isWindowMinButton(sName))
    	{
    		paintButtonSymbol(SYMBOL_MINIMIZE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    	}
    	else if (SmartLookAndFeel.isWindowMaxButton(sName))
    	{
    		if (pWindow instanceof Frame && (((Frame)pWindow).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
    		{
    			paintButtonSymbol(SYMBOL_RESTORE, pGraphics, pX, pY, pWidth, pHeight, pColor);
    		}
    		else
    		{
    			paintButtonSymbol(SYMBOL_MAXIMIZE, pGraphics, pX, pY, pWidth, pHeight, pColor);    			
    		}
    	}
    }
    
    /**
     * Paints special symbols for buttons (close, minimize, maximize, ...).
     * 
     * @param iSymbolType the symbol type : 
     * @param pGraphics the graphics context of the canvas
     * @param pX the x coordinate of the canvas
     * @param pY the y coordinate of the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     * @param pColor the color of the symbol
     */
    private void paintButtonSymbol(int iSymbolType,
    		                       Graphics pGraphics, 
								   int pX, 
								   int pY, 
								   int pWidth, 
								   int pHeight, 
								   Color pColor)
    {
    	switch (iSymbolType)
    	{
    		case SYMBOL_CLOSE:
        		pGraphics.setColor(pColor);
        		
        		//links oben -> rechts unten (mittlere Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS, pY + SPACE_TITLEBUTTONS, pX + pWidth - SPACE_TITLEBUTTONS - 1, pY + pHeight - SPACE_TITLEBUTTONS - 1);
        		//links oben -> rechts unten (linke Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS - 1, pY + SPACE_TITLEBUTTONS, pX + pWidth - SPACE_TITLEBUTTONS - 2, pY + pHeight - SPACE_TITLEBUTTONS - 1);
        		//links oben -> rechts unten (rechte Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS + 1, pY + SPACE_TITLEBUTTONS, pX + pWidth - SPACE_TITLEBUTTONS, pY + pHeight - SPACE_TITLEBUTTONS - 1);

        		
        		//links unten -> rechts oben (mittlere Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS, pY + pHeight - SPACE_TITLEBUTTONS - 1, pX + pWidth - SPACE_TITLEBUTTONS - 1, pY + SPACE_TITLEBUTTONS);
        		//links unten -> rechts oben (linke Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS - 1, pY + pHeight - SPACE_TITLEBUTTONS - 1, pX + pWidth - SPACE_TITLEBUTTONS - 2, pY + SPACE_TITLEBUTTONS);
        		//links unten -> rechts oben (rechte Linie)
        		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS + 1, pY + pHeight - SPACE_TITLEBUTTONS - 1, pX + pWidth - SPACE_TITLEBUTTONS, pY + SPACE_TITLEBUTTONS);
        		break;
        		
    		case SYMBOL_MAXIMIZE:
	    		pGraphics.setColor(pColor);

	    		//äußeres Rechteck
    			pGraphics.drawRect
    			(
    				pX + SPACE_TITLEBUTTONS, pY + SPACE_TITLEBUTTONS, 
    				pWidth - SPACE_TITLEBUTTONS - SPACE_TITLEBUTTONS - 1, pHeight - SPACE_TITLEBUTTONS - SPACE_TITLEBUTTONS - 1
    			);
    			//obere Linie (an der Oberseite des Rechtecks)
    			pGraphics.drawLine(pX + SPACE_TITLEBUTTONS, pY + SPACE_TITLEBUTTONS + 1, pX + pWidth - SPACE_TITLEBUTTONS - 1, pY + SPACE_TITLEBUTTONS + 1);
    			break;
    			
    		case SYMBOL_MINIMIZE:
	    		pGraphics.setColor(pColor);
	    		
	    		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS, pY + pHeight - SPACE_TITLEBUTTONS - 2, pX + pWidth - SPACE_TITLEBUTTONS - 1, pY + pHeight - SPACE_TITLEBUTTONS - 2);
	    		pGraphics.drawLine(pX + SPACE_TITLEBUTTONS, pY + pHeight - SPACE_TITLEBUTTONS - 1, pX + pWidth - SPACE_TITLEBUTTONS - 1, pY + pHeight - SPACE_TITLEBUTTONS - 1);
	    		break;
	    		
    		case SYMBOL_RESTORE:
        		pGraphics.setColor(pColor);
        		
        		int iSpace = Math.max(SPACE_TITLEBUTTONS, pWidth / 5);

        		int iSize = pWidth - iSpace - iSpace - iSpace - 1;
        		
        		int iFirstX = pX + iSpace; 
        		int iFirstY = pY + iSpace + iSpace;
        		
        		int iX = iFirstX;
        		int iY = iFirstY;
        		
        		//vorderes Fenster (Rahmen)
        		pGraphics.drawRect(iX, iY, iSize, iSize);
        		//Linie für oberen Balken
        		pGraphics.drawLine(iX, iY + 1, iX + iSize, iY + 1);

        		iX = pWidth - iSpace - 1;
        		iY -= iSpace;

        		//hinteres Fenster (Rahmen)
        		pGraphics.drawLine(iX - iSize, iSpace, iX, iSpace);
        		//oberer Balken
        		pGraphics.drawLine(iX - iSize, iSpace + 1, iX, iSpace + 1);
        		//rechte Linie
        		pGraphics.drawLine(iX, iSpace, iX, iSpace + iSize);
        		
        		iX -= iSize;
        		
        		//linke Linie
        		pGraphics.drawLine(iX, iSpace + 2, iX, iFirstY - 1);
        		
        		iY = iSpace + iSize;
        		
        		//"Nase" rechts
        		pGraphics.drawLine(iFirstX + iSize + 1, iY, pWidth - iSpace - 1, iY);
        		break;
        		
        	default:
        		//do nothing
    	}
    }

	/**
     * Paints the title of an internal frame.
     * 
     * @param pContext the components context (JDesktopIcon or JInternalFrame context)
     * @param pFrame the internal frame
     * @param pNorthPane the title pane of the internal frame
     * @param pGraphics the graphics context
     * @param pWidth the width of the canvas
     */
    private void paintNorthPaneTitle(SynthContext pContext, 
    								 JInternalFrame pFrame, 
    								 BasicInternalFrameTitlePane pNorthPane, 
    								 Graphics pGraphics, 
    								 int pWidth)
    {
        String sTitle = pFrame.getTitle();

        //Vorsicht: Ein ähnlicher Code wir auch in SmartRootTitlePane verwendet!
    	if (sTitle != null) 
    	{
    		Component comp = pContext.getComponent();
    		
    		SynthStyle style = pContext.getStyle();

	        pGraphics.setColor(style.getColor(pContext, ColorType.TEXT_FOREGROUND));
	        pGraphics.setFont(style.getFont(pContext));

            // Vertikal zentrieren
    	    FontMetrics fm = pNorthPane.getFontMetrics(pNorthPane.getFont());
    	    
            int baseline = (comp.getHeight() + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
            
			AbstractButton butLastVisible = null;
			AbstractButton[] buttons = SmartLookAndFeel.getNorthPaneButtons(pNorthPane);
			
			Insets insNorth = pNorthPane.getInsets();
			
			int iMaxX;
			int iMinX;
			int iTitleAlignment = SmartTheme.ALIGN_INTFRAME_TITLE;
			
			boolean bNoMenuIcon = pFrame.getFrameIcon() == null;
			boolean bLeftToRight = SmartLookAndFeel.isLeftToRightOrientation(pFrame); 
			
			if (pFrame.isIconifiable()) 
			{
			    butLastVisible = buttons[1];
			}
			else if (pFrame.isMaximizable()) 
			{
			    butLastVisible = buttons[2];
			}
			else if (pFrame.isClosable()) 
			{
			    butLastVisible = buttons[3];
			}

			//MIN, MAX Grenze ermitteln (abhängig von den Buttons und der Ausrichtung)
			if (bLeftToRight) 
			{
				if (butLastVisible != null) 
			    {
			        iMaxX = butLastVisible.getX() - SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			    }
			    else 
			    {
			        iMaxX = pNorthPane.getWidth() - insNorth.right;
			    }
			    
			    iMinX = bNoMenuIcon ? insNorth.left : buttons[0].getX() + buttons[0].getWidth() + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			}
			else 
			{
				if (butLastVisible != null) 
			    {
				    iMinX = butLastVisible.getX() + butLastVisible.getWidth() + SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			    }
			    else 
			    {
			        iMinX = insNorth.left;
			    }
			    
				iMaxX = bNoMenuIcon ? pNorthPane.getWidth() - insNorth.right : buttons[0].getX() - SmartTheme.SPACE_INTFRAME_TITLE_TEXT;
			    
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
			String sClippedTitle = clipTitle(sTitle, iMinX, iMaxX, fm);
			int iX;
                
            if (iTitleAlignment == SwingConstants.CENTER) 
            {
                int iWidth = style.getGraphicsUtils(pContext).computeStringWidth(pContext, pGraphics.getFont(), fm, sClippedTitle);
                
                iX = (comp.getWidth() - iWidth) / 2;

            	//Zentrierung, solange der Text sich in den Grenzen befindet, andernfalls
                //links oder rechts Ausrichtung durchführen!
                //
                //Der Text MUSS immer innerhalb der min und max Position dargestellt werden. 
                if (bLeftToRight)
                {
    	            if (iX > iMaxX - iWidth)
    	            {
    	            	iX = iMaxX - iWidth;
    	            }
    	            
    	            if (iX < iMinX)
    	            {
    	            	iX = iMinX;
    	            }
                }
                else
                {
                	if (iX + iWidth > iMaxX)
                	{
                		iX = iMaxX - iWidth;
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
    				iX = iMaxX - style.getGraphicsUtils(pContext).computeStringWidth(pContext, pGraphics.getFont(), fm, sClippedTitle);
    			}
            }
			
			pGraphics.setColor(pContext.getStyle().getColor(pContext, ColorType.FOREGROUND));
			
			//der letzte Parameter MUSS -2 sein, da -1 im SmartGraphicsUtils abgefangen wird
			//-1 wird ab 1.6 verwendet, damit der Title vom Smart/LF gemalt wird ist -2 nötig!
            style.getGraphicsUtils(pContext).paintText(pContext, pGraphics, sClippedTitle, iX, baseline - fm.getAscent(), -2);
    	}
    }
    
    /**
     * Checks if a string fits to a rectangle. If the string doesn't fit, it will be
     * truncated and "..." will be added.
     * 
     * @param pTitle the string
     * @param pMinX the minimum x location (rectangle start)
     * @param pMaxX the maximum x location (rectangle end)
     * @param pMetrics the font metrics
     * @return the string which will fit to the reactangle
     */
    public static String clipTitle(String pTitle, int pMinX, int pMaxX, FontMetrics pMetrics)
    {
    	String sClippedTitle = pTitle;
    	
    	int iMaxTextWidth = pMaxX - pMinX;
    	
        //Wenn der Text länger ist als der darstellbare Bereich wird er mit "..." dargestellt
        if (pMetrics.stringWidth(sClippedTitle) > iMaxTextWidth)
        {
        	//Berechnen der maximal darstellbaren Zeichen
        	int iWidth = pMetrics.stringWidth("...");
        	int iLastChar = 0;
        	
        	for (int anz = pTitle.length(); iLastChar < anz && iWidth <= iMaxTextWidth; iLastChar++)
        	{
        		iWidth += pMetrics.charWidth(pTitle.charAt(iLastChar));
        	}

        	if (iLastChar > 0)
        	{
        		sClippedTitle = sClippedTitle.substring(0, iLastChar - 1) + "...";
        	}
        	else
        	{
        		sClippedTitle = "...";
        	}
        }
        
        return sClippedTitle;
    }
    
    /**
     * Gets the usable color. 
     * 
     * @param pComponent the component
     * @param pDefaultColor the default color
     * @return if <code>pColor</code> is an instance of {@link ColorUIResource}
     *         or is <code>null</code> then the <code>pDefaultColor</code> will be returned, otherwise
     *         <code>pColor</code>
     */
    private static Color getBackgroundColor(Component pComponent, Color pDefaultColor)
    {
    	if (isUserDefinedBackgroundColor(pComponent))
    	{
    		return pComponent.getBackground();
    	}
    	else
    	{
        	return pDefaultColor;
    	}    		
    	
    }
    
    /**
     * Checks if a color is user-defined or defined through the look and feel.
     * 
     * @param pComponent the component
     * @return <code>true</code> only if <code>pColor</code> is not <code>null</code> and is
     *         not an instance of {@link ColorUIResource}
     */
    private static boolean isUserDefinedBackgroundColor(Component pComponent)
    {
    	Color color = pComponent.getBackground();
    	
    	return pComponent.isBackgroundSet() || (color != null && !(color instanceof ColorUIResource));
    }

    /**
     * Creates the image for a linear gradient.
     * 
     * @param pWidth the width of the gradient area
     * @param pHeight the height of the gradient area
     * @param colStart the start color
     * @param colEnd the end color
     * @param pDirection the direction for the gradient can be {@link #GRADIENT_HORIZONTAL} or {@link #GRADIENT_VERTICAL}
     * @return the image with the gradient
     */
    private static Image createLinearGradient(int pWidth, int pHeight, Color colStart, Color colEnd, int pDirection)
    {
    	Image image = null;
    	
    	int iLength;

    	//1 Pixel breite bzw. Höhe ist ausreichend. Abhängig davon in welche Richtung gemalt wird
		if (pDirection == GRADIENT_VERTICAL)
		{
			iLength = pHeight;
			pWidth = 1;
		}
		else
		{
			iLength = pWidth;
			pHeight = 1;
		}
    	
    	if (htGradientImageCache != null)
    	{
    		image = htGradientImageCache.get(pWidth + "x" + pHeight); 
    	}

    	if (image == null)
    	{
			image = new BufferedImage(pWidth, pHeight, BufferedImage.BITMASK);
			
			//Image in den cache legen!
			if (htGradientImageCache == null)
			{
				htGradientImageCache = new Hashtable<String, Image>();
			}
			
			htGradientImageCache.put(pWidth + "x" + pHeight, image);
    	}
    	
    	Graphics gImage = image.getGraphics();
    	
		for (int i = 0; i < iLength; i++)
		{
			gImage.setColor
			(
				new Color
				(
					(int)Math.max(0, colStart.getRed() - (((colEnd.getRed() - colStart.getRed()) / (float)-iLength) * i)),
					(int)Math.max(0, colStart.getGreen() - (((colEnd.getGreen() - colStart.getGreen()) / (float)-iLength) * i)),
					(int)Math.max(0, colStart.getBlue() - (((colEnd.getBlue() - colStart.getBlue()) / (float)-iLength) * i))
				)
			);
			
			if (pDirection == GRADIENT_VERTICAL)
			{
				gImage.drawLine(0, i, 1, i);
			}
			else
			{
				gImage.drawLine(i, 0, i, 1);
			}
		}
		gImage.dispose();
		
    	return image;
    }

    /**
     * Gets a dummy image for the arrow icon of a sub menu.
     * 
     * @param pMenu the menu which needs the arrow icon
     * @return an empty image
     * @see #paintMenuBackground(SynthContext, Graphics, int, int, int, int)
     */
    public static Icon getMenuArrowIcon(JMenu pMenu)
    {
    	ImageIcon ico = htMenuArrowCache.get(pMenu);
    	
    	//Caching des Images, damit die Instanz wiederverwendet werden und ggf. 
    	//ein neues Symbol gemalt werden kann.
    	//Die andere Möglichkeit wäre reflective das arrowIcon im UI zu ändern,
    	//allerdings würde das mit Applets nicht funktionieren
    	if (ico == null)
    	{
			ico = new ImageIcon(new BufferedImage(10, 7, BufferedImage.BITMASK));
			
			htMenuArrowCache.put(pMenu, ico);
    	}
    	
    	return ico;
    }

    /**
     * Calculates the size of a radio button dependent of the components font size.
     * 
     * @param pComponent the radio button component
     * @return the size of the radio button
     */
    public static int getRadioButtonIconSize(Component pComponent)
    {
    	int iSize = pComponent.getFont().getSize();
    	
    	//bei ungeraden Größen wirkt das icon wie eine Melone
    	iSize -= iSize % 2;
    	
    	return iSize - 1;
    }
    
    /**
     * Gets a cached or creates a new radio button icon with or without
     * selection.
     * 
     * @param pContext the context
     * @return the icon as image
     */
    public static Icon getRadioButtonIcon(SynthContext pContext)
    {
    	Component comp = pContext.getComponent();
    	
    	int iSize = getRadioButtonIconSize(comp);
    	int iState = 0;
    	
    	String sType;
    	
    	Color colBack;
    	Color colBorder;
    	Color colSelected = null;
    	
    	
    	
    	if (comp instanceof JMenuItem)
    	{
    		colBack   = SmartTheme.COL_MENU_BACKGROUND;

    		if (comp.isEnabled())
    		{
	    		colBorder = SmartTheme.COL_MENU_RADIOCHECK_BORDER;
    		}
    		else
    		{
        		colBorder = SmartTheme.COL_MENU_DISABLED;
    		}
    		
    		//Menü-Radio-Buttons haben kein MOUSE_OVER & co.
    		if (SmartLookAndFeel.isState(pContext, SynthConstants.SELECTED, null))
    		{
    			if (comp.isEnabled())
    			{
    				colSelected = SmartTheme.COL_MENU_RADIOCHECK_SELECTED;
    			}
    			else
    			{
    				colSelected = SmartTheme.COL_MENU_DISABLED;
    			}
    			
    			iState = 1;
    		}
    		
    		//Radio-Menü
    		sType = "RM";
    	}
    	else
    	{
    		colBack = Color.white;

    		if (comp.isEnabled())
    		{
        		colBorder = SmartTheme.COL_RADIOCHECK_BORDER;
    		}
    		else
    		{
    			colBorder = SmartTheme.COL_RADIOCHECK_DISABLED;
    		}

    		//Menü-Radio-Buttons haben kein MOUSE_OVER & co.
    		if (SmartLookAndFeel.isState(pContext, SynthConstants.SELECTED, null))
    		{
    			if (comp.isEnabled())
    			{
    				colSelected = SmartTheme.COL_RADIOCHECK_SELECTED;
    			}
    			else
    			{
    				colSelected = SmartTheme.COL_RADIOCHECK_DISABLED;
    			}

    			iState = 1;
    		}
    		
    		//Radio-Other
    		sType = "RO";
    	}

    	ImageIcon ico = htMenuIconCache.get(iSize + sType + iState + (comp.isEnabled() ? "E" : "D"));
    	
    	if (ico == null)
    	{
	    	BufferedImage img = new BufferedImage(iSize, iSize, BufferedImage.BITMASK);
	    	
	    	ico = new ImageIcon(img);
    	
	    	Graphics2D gImg = (Graphics2D)img.getGraphics();
	    	gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	
	    	gImg.setColor(colBack);
	    	gImg.fillOval(1, 1, iSize - 2, iSize - 2);
	    	gImg.setColor(colBorder);
	    	gImg.drawOval(0, 0, iSize - 1, iSize - 1);
	    	
	    	if (iState == 1)
	    	{
				gImg.setColor(colSelected);
	    		gImg.fillOval(3, 3, iSize - 6, iSize - 6);
	    	}
	    	
	    	gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			
			htMenuIconCache.put(iSize + sType + iState, ico);
    	}    	
    	
    	return ico;
    }
    
    /**
     * Calculates the size of a checkbox dependent of the components font size.
     * 
     * @param pComponent the checkbox component
     * @return the size of the checkbox
     */
    public static int getCheckBoxIconSize(Component pComponent)
    {
    	return pComponent.getFont().getSize();
    }
    
    /**
     * Gets a cached or creates a new checkbox icon with or without
     * selection.
     * 
     * @param pContext the context
     * @return the icon as image
     */
    public static Icon getCheckBoxIcon(SynthContext pContext)
    {
    	Component comp = pContext.getComponent();
    	
    	int iSize = getCheckBoxIconSize(comp);
    	int iState = 0;
    	
    	String sType;
    	
    	Color colBack;
    	Color colBorder;
    	Color colSelected = null;
    	
    	
    	if (comp instanceof JMenuItem)
    	{
    		colBack   = SmartTheme.COL_MENU_BACKGROUND;
    		
    		if (comp.isEnabled())
    		{
        		colBorder = SmartTheme.COL_MENU_RADIOCHECK_BORDER;
    		}
    		else
    		{
    			colBorder = SmartTheme.COL_MENU_DISABLED;
    		}

    		//Menü-Radio-Buttons haben kein MOUSE_OVER & co.
    		if (SmartLookAndFeel.isState(pContext, SynthConstants.SELECTED, null))
    		{
    			if (comp.isEnabled())
    			{
    				colSelected = SmartTheme.COL_MENU_RADIOCHECK_SELECTED;
    			}
    			else
    			{
    				colSelected = SmartTheme.COL_MENU_DISABLED;
    			}

    			iState = 1;
    		}
    		
    		//Check-Menü
    		sType = "CM";
    	}
    	else
    	{
    		colBack = Color.white;
    		
    		if (comp.isEnabled())
    		{
        		colBorder = SmartTheme.COL_RADIOCHECK_BORDER;
    		}
    		else
    		{
    			colBorder = SmartTheme.COL_RADIOCHECK_DISABLED;
    		}

    		//Menü-Radio-Buttons haben kein MOUSE_OVER & co.
    		if (SmartLookAndFeel.isState(pContext, SynthConstants.SELECTED, null))
    		{
    			if (comp.isEnabled())
    			{
    				colSelected = SmartTheme.COL_RADIOCHECK_SELECTED;
    			}
    			else
    			{
    				colSelected = SmartTheme.COL_RADIOCHECK_DISABLED;
    			}

    			iState = 1;
    		}

    		//Check-Other
    		sType = "CO";
    	}

    	ImageIcon ico = htMenuIconCache.get(iSize + sType + iState + (comp.isEnabled() ? "E" : "D"));
    	
    	if (ico == null)
    	{
    		BufferedImage img = new BufferedImage(iSize, iSize, BufferedImage.BITMASK);
	    	
	    	ico = new ImageIcon(img);
    	
	    	Graphics2D gImg = (Graphics2D)img.getGraphics();
	    	
			paintRectangle(gImg, 0, 0, iSize, iSize, 1, -1, colBack, null, -1, colBorder);
			
			if (iState == 1)
	    	{
				gImg.setColor(colSelected);

				//drittel vom inneren Bereich (2px Rahmen, 2px Abstand zum Rahmen (=links und rechts je 1px))
				int iDrittel = (iSize - 4) / 3;
				
				for (int i = 0; i <= iDrittel; i++)
				{
					//linke Hälfte
					gImg.drawLine(2, iSize - 3 - iDrittel - i, 2 + iDrittel, iSize - 3 - i);
		    		//rechte Hälfte
					gImg.drawLine(2 + iDrittel, iSize - 3 - i, iSize - 3, 2 + iDrittel - i);
				}
	    	}
			
			htMenuIconCache.put(iSize + sType + iState, ico);
    	}    	
    	
    	return ico;
    }
    
    /**
     * Paints the background of a text component like JTextArea or JTextField.
     * 
     * @param pComponent the text component
     * @param pGraphics the graphics context of the canvas 
     * @param pX the x coordinate of the canvas
     * @param pY the y coordinate of the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     */
    private void paintTextComponentBackground(JComponent pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
    	Color colBack;
    	
    	if (!pComponent.isEnabled())
    	{
    		//Wenn der Editor in einer ComboBox disabled ist, dann wird der Editor erst
    		//disabled dargestellt, wenn auch die ComboBox disabled ist
    		if (SmartLookAndFeel.isComboBoxTextField(pComponent.getName()))
    		{
    			if (!pComponent.getParent().isEnabled())
    			{
    				colBack = getBackgroundColor(pComponent.getParent(), SmartTheme.COL_TEXT_DISABLED_BACKGROUND);
    			}
    			else
    			{
    				colBack = getBackgroundColor(pComponent.getParent(), SmartTheme.COL_BACKGROUND_TEXT);
    			}
    		}
    		else
    		{
	    		colBack = getBackgroundColor(pComponent, SmartTheme.COL_TEXT_DISABLED_BACKGROUND);
    		}
    	}
    	else
    	{
        	colBack = getBackgroundColor(pComponent, SmartTheme.COL_BACKGROUND_TEXT);
    	}
    	
    	paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, colBack, null, -1, null);
    }
    
    /**
     * Paints the border of a text component like JTextArea or JTextField.
     * 
     * @param pComponent the text component
     * @param pGraphics the graphics context of the canvas 
     * @param pX the x coordinate of the canvas
     * @param pY the y coordinate of the canvas
     * @param pWidth the width of the canvas
     * @param pHeight the height of the canvas
     */
    private void paintTextComponentBorder(JComponent pComponent, Graphics pGraphics, int pX, int pY, int pWidth, int pHeight)
    {
		Color colBorder;
		
		boolean bEditable;
		
		
		if (pComponent instanceof JTextComponent)
		{
			bEditable = ((JTextComponent)pComponent).isEditable();
		}
		else
		{
			bEditable = true;
		}
		
		if (!bEditable || !pComponent.isEnabled())
		{
			colBorder = SmartTheme.COL_TEXT_BORDER_DISABLED;
		}
		else
		{
			colBorder = SmartTheme.COL_TEXT_BORDER;
		}
		
		paintRectangle(pGraphics, pX, pY, pWidth, pHeight, 1, -1, null, null, -1, colBorder);
    }    
    
}	// SmartPainter
