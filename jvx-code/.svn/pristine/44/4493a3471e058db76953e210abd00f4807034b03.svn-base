/*
 * Copyright 2015 SIB Visions GmbH
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
 * 29.09.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.swing.ext.fonts.FontAwesome;
import com.sibvisions.util.IntHashMap;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JVxFontAwesomeIcon</code> is an {@link ImageIcon} implementation for FontAwesome font.
 * 
 * @author René Jahn
 * @see <a href="http://fortawesome.github.io/Font-Awesome/cheatsheet/">http://fortawesome.github.io/Font-Awesome/cheatsheet/</a>
 */
public class JVxFontAwesomeIcon extends ImageIcon
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the font base. */
    private static final Font   BASEFONT;
    /** cached sized basefont. */
    private static final IntHashMap<Font> CACHED_SIZED_BASEFONT = new IntHashMap<Font>();
    
    /** the icon definition. */
    private FontAwesome         faIconDefinition;

    /** the set color. */
    private Color               color = null;

    /** the set size. */
    private int                 size;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    static
    {
        try
        {
            InputStream stream = ResourceUtil.getResourceAsStream("/com/sibvisions/rad/ui/swing/ext/fonts/fontawesome-webfont.ttf");
            
        	try
            {
            	if (stream != null)
            	{
            		BufferedInputStream bis = new BufferedInputStream(stream);
            		
            		try
            		{
	                    BASEFONT = Font.createFont(Font.TRUETYPE_FONT, bis);
	                    
	                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(BASEFONT);
            		}
            		finally
            		{
            			CommonUtil.close(bis);
            		}
            	}
            	else
            	{
            		throw new Exception("FontAwesome font resource was not found!");
            	}
            }
            finally
            {
            	CommonUtil.close(stream);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
    * Creates a new instance of <code>JVxFontAwesomeIcon</code> with default size of 16px.
    * 
    * @param pFont the icon definition
    */
    public JVxFontAwesomeIcon(FontAwesome pFont) 
    {
    	faIconDefinition = pFont;
        size = 16;
    }

    /**
     * Creates a new instance of <code>JVxFontAwesomeIcon</code>.
     * 
     * @param pDefinition The icon definition (only the name or followed by a list of properties:
     *                    server;size=20;color=0xFFAACC
     */
    public JVxFontAwesomeIcon(String pDefinition)
    {
        parse(pDefinition);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public synchronized void paintIcon(Component pComponent, Graphics pGraphics, int pX, int pY)
    {
        Graphics2D g2 = (Graphics2D)pGraphics;
        
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();
        Object oldAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object oldRendering = g2.getRenderingHint(RenderingHints.KEY_RENDERING);
        
        Font font = CACHED_SIZED_BASEFONT.get(size);
        if (font == null)
        {
        	font = BASEFONT.deriveFont(Font.PLAIN, size);
        	
        	CACHED_SIZED_BASEFONT.put(size,  font);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        g2.setFont(font);
        if (color != null)
        {
        	g2.setColor(color);
        }
        else
        {
        	Color col = null;
        	if (pComponent != null)
        	{
        		col = pComponent.getForeground();
        	}
        	if (col == null)
        	{
        		col = Color.BLACK;
        	}
        	g2.setColor(col);
        }
        
        
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        int drawAtX = (getIconWidth() - fontMetrics.charWidth(faIconDefinition.getCode())) / 2;
        int drawAtY = (getIconHeight() - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        g2.drawString(String.valueOf(faIconDefinition.getCode()), pX + (int)drawAtX, pY + (int)drawAtY);
        
        g2.setFont(oldFont);
        g2.setColor(oldColor);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, oldRendering);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
    }
    
    /**
     * {@inheritDoc}
     */
    public int getIconWidth()
    {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    public int getIconHeight()
    {
        return size;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getImage()
    {
        BufferedImage image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics gr = image.getGraphics();
        paintIcon(null, gr, 0, 0);
        gr.dispose();

        return image;    
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Parses given icon definition.
     * 
     * @param pDefinition the icon definition: fontname[;prop1=value1;prop2=value2]
     * @throws IllegalArgumentException if icon name wasn't set or parsing failed
     */
    private void parse(String pDefinition)
    {
        if (StringUtil.isEmpty(pDefinition))
        {
            throw new IllegalArgumentException("Invalid icon definition: " + pDefinition);
        }

        HashMap<String, String> hmpProps = LauncherUtil.splitImageProperties(pDefinition);
        faIconDefinition = FontAwesome.resolve(hmpProps.get("name"));
        
        String sSize = hmpProps.get("size");
        
        if (sSize == null)
        {
            sSize = hmpProps.get("font-size");
            
            if (sSize == null)
            {
                sSize = "16";
            }
        }
        
        try
        {
            size = Integer.parseInt(sSize);
        }
        catch (NumberFormatException nfe)
        {
            //remove optional unit
            sSize = StringUtil.getText(sSize, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '.');
            
            size = Integer.parseInt(sSize);
        }
        
        String sColor = hmpProps.get("color");
        
        int[] iaColor = StringUtil.parseColor(sColor);
        
        if (iaColor != null)
        {
            if (iaColor.length == 3)
            {
                color = new Color(iaColor[0], iaColor[1], iaColor[2]);
            }
            else
            {
                color = new Color(iaColor[0], iaColor[1], iaColor[2], iaColor[3]);
            }
        }
    }
    
    /**
     * Sets the size.
     * 
     * @param pSize the size
     */
    public void setSize(int pSize)
    {
        size = pSize;
    }
    
    /**
     * Gets the size.
     * 
     * @return the size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Sets the color.
     * 
     * @param pColor the color
     */
    public void setColor(Color pColor)
    {
        color = pColor;
    }
    
    /**
     * Gets the color.
     * 
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }
    
}   // JVxFontAwesomeIcon
