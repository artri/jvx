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
 * 11.10.2009 - [JR] - creation
 * 21.02.2019 - [JR] - #1989: repaint
 * 04.05.2019 - [JR] - #2025: don't suppress super paintComponent
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.rad.ui.IAlignmentConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 * The <code>JVxPanel</code> is a {@link JPanel} with a background image.
 *  
 * @author René Jahn
 */
public class JVxPanel extends JPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Constant for titled border style. */
    public static final int DEFAULT_TITLED_BORDER = 0;
    /** Constant for titled border style. */
    public static final int MODERN_TITLED_BORDER = 1;
    
    /** Align top. */
    public static final int TITLE_TOP = 0;
    /** Align bottom. */
    public static final int TITLE_BOTTOM = 2;
    /** Align left. */
    public static final int TITLE_LEFT = 0;
    /** Align center. */
    public static final int TITLE_CENTER = 1;
    /** Align right. */
    public static final int TITLE_RIGHT = 2;
    
    /** The title of titled border. */
    private String title = null;
    /** The titled border style. */
    private Font titleFont = null;
    /** The titled border style. */
    private Color titleColor = null;
    /** The titled border style. */
    private int titledBorderStyle = UIManager.getInt("TitledBorder.style");
    /** The vertical alignment. */
    private int horizontalTitleAlignment = TITLE_LEFT; 
    /** The vertical alignment. */
    private int verticalTitleAlignment = TITLE_TOP; 

	/** the background image. */
	private ImageIcon imgBack = null;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a <code>JVxPanel</code>.
	 */
	public JVxPanel()
	{
	    super();
	}
	
    /**
     * Constructs a <code>JVxPanel</code> with layout.
     *
     * @param pLayout the LayoutManager to use
     */
    public JVxPanel(LayoutManager pLayout) 
    {
        super(pLayout);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	 * Paints the background image if set.
	 * 
	 * @param pGraphics {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics pGraphics)
	{
		super.paintComponent(pGraphics);

		if (imgBack != null)
		{
			//pGraphics.drawImage(imgBack.getImage(), 0, 0, imgBack.getIconWidth(), imgBack.getIconHeight(), this);
		    
		    Insets insets = getInsets();
			imgBack.paintIcon(this, pGraphics, insets.left, insets.top);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the background image.
	 * 
	 * @param pImage the background image or <code>null</code> to unset the background image
	 */
	public void setBackgroundImage(ImageIcon pImage)
	{
		imgBack = pImage;
		
		repaint();
	}
	
	/**
	 * Gets the current background image.
	 * 
	 * @return the background image or <code>null</code> if there is no background image set
	 */
	public ImageIcon getBackgroundImage()
	{
		return imgBack;
	}

    /**
     * Gets the title.
     * 
     * @return the title.
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Sets the title.
     * 
     * @param pTitle the title.
     */
    public void setTitle(String pTitle)
    {
        title = pTitle;
        
        applyTitledBorderProperties();
    }

    /**
     * Gets the title font.
     * 
     * @return the title font.
     */
    public Font getTitleFont()
    {
        return titleFont;
    }
    
    /**
     * Sets the title font.
     * 
     * @param pTitleFont the title font.
     */
    public void setTitleFont(Font pTitleFont)
    {
        titleFont = pTitleFont;
        
        applyTitledBorderProperties();
    }

    /**
     * Gets the title color.
     * 
     * @return the title color.
     */
    public Color getTitleColor()
    {
        return titleColor;
    }
    
    /**
     * Sets the title color.
     * 
     * @param pTitleColor the title color.
     */
    public void setTitleColor(Color pTitleColor)
    {
        titleColor = pTitleColor;
        
        applyTitledBorderProperties();
    }

    /**
     * Gets the horizontal alignment.
     * 
     * @return the horizontal alignment.
     */
    public int getHorizontalTitleAlignment()
    {
        return horizontalTitleAlignment;
    }
    
    /**
     * Sets the horizontal alignment.
     * 
     * @param pHorizontalTitleAlignment the horizontal alignment.
     */
    public void setHorizontalTitleAlignment(int pHorizontalTitleAlignment)
    {
        horizontalTitleAlignment = pHorizontalTitleAlignment;
        
        applyTitledBorderProperties();
    }

    /**
     * Gets the vertical alignment.
     * 
     * @return the vertical alignment.
     */
    public int getVerticalTitleAlignment()
    {
        return verticalTitleAlignment;
    }
    
    /**
     * Sets the vertical alignment.
     * 
     * @param pVerticalTitleAlignment the vertical alignment.
     */
    public void setVerticalTitleAlignment(int pVerticalTitleAlignment)
    {
        verticalTitleAlignment = pVerticalTitleAlignment;
        
        applyTitledBorderProperties();
    }

    /**
     * Applies the titled border properties.
     */
    protected void applyTitledBorderProperties()
    {
        if (isTitledBorderVisible())
        {
            TitledBorder titledBorder = (TitledBorder)getBorder();
            
            if (title == null || title.length() == 0)
            {
                titledBorder.setTitle(title);
            }
            else
            {
                titledBorder.setTitle(" " + title + " ");
            }
            
            TitledBorder defaultTitledBorder = createTitledBorder();
            
            titledBorder.setTitleFont(titleFont == null ? defaultTitledBorder.getTitleFont() : titleFont);
            titledBorder.setTitleColor(titleColor == null ? defaultTitledBorder.getTitleColor() : titleColor);
            
            switch (titledBorderStyle)
            {
                case MODERN_TITLED_BORDER: 
                    titledBorder.setTitleJustification(getModernTitleJustification(horizontalTitleAlignment));
                    titledBorder.setTitlePosition(getModernTitlePosition(verticalTitleAlignment));
                    titledBorder.setBorder(createModernBorder(verticalTitleAlignment));
                    break;
                default:
                    titledBorder.setTitleJustification(getDefaultTitleJustification(horizontalTitleAlignment));
                    titledBorder.setTitlePosition(getDefaultTitlePosition(verticalTitleAlignment));
                    break;
            }
            
            repaint();
        }
    }
    
	/**
	 * True, if titled border is visible.
	 * 
	 * @return True, if titled border is visible.
	 */
	public boolean isTitledBorderVisible()
	{
	    return getBorder() instanceof TitledBorder;
	}
	
    /**
     * True, if titled border is visible.
     * 
     * @param pTitledBorderVisible True, if titled border is visible.
     */
	public void setTitledBorderVisible(boolean pTitledBorderVisible)
	{
	    if (!isTitledBorderVisible())
	    {
	        applyTitledBorder();
	    }
	}

    /**
     * Gets the titled border style.
     * 
     * @return the titled border style.
     */
    public int getTitledBorderStyle()
    {
        return titledBorderStyle;
    }
    
    /**
     * Sets the titled border style.
     * 
     * @param pTitledBorderStyle the titled border style.
     */
    public void setTitledBorderStyle(int pTitledBorderStyle)
    {
        if (titledBorderStyle != pTitledBorderStyle)
        {
            titledBorderStyle = pTitledBorderStyle;
            
            if (isTitledBorderVisible())
            {
                applyTitledBorder();
            }
        }
    }

    /**
     * Creates the titled border.
     */
    public void applyTitledBorder()
    {
        setBorder(createTitledBorder());
        
        applyTitledBorderProperties();
    }
    
	/**
	 * Creates the titled border.
	 * 
	 * @return the titled border
	 */
	public TitledBorder createTitledBorder()
	{
	    TitledBorder titledBorder;
	    switch (titledBorderStyle)
	    {
	        case MODERN_TITLED_BORDER: 
	            titledBorder = createModernTitledBorder();
	            break;
	        default:
	            titledBorder = createDefaultTitledBorder();
	            break;
	    }
	    
	    return titledBorder;
	}
	
	/**
	 * Creates the default Swing titled border.
	 * @return the default Swing titled border.
	 */
	public static TitledBorder createDefaultTitledBorder()
	{
	    return BorderFactory.createTitledBorder("");
	}
	
    /**
     * Creates the default Swing titled border.
     * @return the default Swing titled border.
     */
    public static TitledBorder createModernTitledBorder()
    {
        TitledBorder border = BorderFactory.createTitledBorder(createModernBorder(TITLE_TOP), "");
        border.setTitlePosition(getModernTitlePosition(TITLE_TOP));
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));

        return border;
    }

    /**
     * Creates the MatteBorder for the vertical alignment.
     * @param pVerticalAlignment the vertical alignment.
     * @return the MatteBorder for the vertical alignment.
     */
    public static MatteBorder createModernBorder(int pVerticalAlignment)
    {
        switch (pVerticalAlignment) 
        {
            case TITLE_BOTTOM: 
                return BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(170, 170, 170));
            default:
                return BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(170, 170, 170));
        }
    }
    
    /**
     * Gets the default title justification.
     * 
     * @param pHorizontalAlignment the horizontal alignment.
     * @return the default title justification.
     */
    public static int getDefaultTitleJustification(int pHorizontalAlignment)
    {
        switch (pHorizontalAlignment) 
        {
            case TITLE_CENTER:  
                return TitledBorder.CENTER;
            case IAlignmentConstants.ALIGN_RIGHT: 
                return TitledBorder.RIGHT;
            default:
                return TitledBorder.LEFT;
        }
    }
    
    /**
     * Gets the modern title justification.
     * 
     * @param pHorizontalAlignment the horizontal alignment.
     * @return the modern title justification.
     */
    public static int getModernTitleJustification(int pHorizontalAlignment)
    {
        return getDefaultTitleJustification(pHorizontalAlignment);
    }
    
    /**
     * Gets the default title justification.
     * 
     * @param pVerticalAlignment the vertical alignment.
     * @return the default title justification.
     */
    public static int getDefaultTitlePosition(int pVerticalAlignment)
    {
        switch (pVerticalAlignment) 
        {
            case TITLE_BOTTOM: 
                return TitledBorder.BOTTOM;
            default:
                return TitledBorder.TOP;
        }
    }
    
    /**
     * Gets the modern title justification.
     * 
     * @param pVerticalAlignment the vertical alignment.
     * @return the modern title justification.
     */
    public static int getModernTitlePosition(int pVerticalAlignment)
    {
        switch (pVerticalAlignment) 
        {
            case TITLE_BOTTOM: 
                return TitledBorder.BELOW_BOTTOM;
            default:
                return TitledBorder.ABOVE_TOP;
        }
    }
    
}	// JVxPanel
