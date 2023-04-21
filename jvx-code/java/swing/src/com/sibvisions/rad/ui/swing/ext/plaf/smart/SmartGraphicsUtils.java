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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartPainter;

/**
 * The <code>SmartGraphicsUtils</code> uses utility functions of {@link SynthGraphicsUtils}
 * to changed the defaults.
 * 
 * @author René Jahn
 */
public class SmartGraphicsUtils extends SynthGraphicsUtils
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String layoutText
    (
    	SynthContext pContext, 
    	FontMetrics pFontMetrics, 
    	String pText, 
    	Icon pIcon, 
    	int pAlignHorizontal, 
    	int pAlignVertical, 
    	int pTextAlignHorizontal, 
        int pTextAlignVertical, 
        Rectangle pRectIconText, 
        Rectangle pRectIcon, 
        Rectangle pRectText, 
        int pIconTextGap
    )
    {
        JComponent comp = pContext.getComponent();
        
        //Tabbed-Pane Icons werden links vom Text dargestellt!
        if (comp instanceof JTabbedPane)
        {
        	pTextAlignHorizontal = SwingConstants.TRAILING;
        }
        
        String sLayout = super.layoutText
        (
        	pContext, 
        	pFontMetrics, 
        	pText, 
        	pIcon, 
        	pAlignHorizontal, 
        	pAlignVertical, 
        	pTextAlignHorizontal, 
        	pTextAlignVertical, 
        	pRectIconText, 
        	pRectIcon, 
        	pRectText, 
        	pIconTextGap
        );
        
        if ((comp instanceof JMenuItem) && (comp.getParent() instanceof JPopupMenu))
        {
        	fixMenuIndent((JMenuItem)comp, pRectIcon, pRectText, pIconTextGap);
        }
        
        return sLayout;
    }	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintText(SynthContext pContext, Graphics pGraphics, String pText, int pX, int pY, int pMnemonicIndex)
	{
		//Ab jdk 1.6 wird der Title von InternalFrames mit dieser Methode dargestellt -> verhindern, da der Title im
		//SmartPainter dargestelllt wird
		if (pContext.getRegion() == Region.INTERNAL_FRAME_TITLE_PANE && !SmartLookAndFeel.isJava5() && pMnemonicIndex == -1)
		{
			return;
		}
		
		super.paintText(pContext, pGraphics, pText, pX, pY, pMnemonicIndex);
	}
	
	/**
	 * Fixes the text and icon indention of menuitems, if needed.
	 *  
	 * @param pItem the menu item
	 * @param pRectIcon the calculated icon bounds
	 * @param pRectText the calculated text bounds
	 * @param pIconTextGap the current gap between icon and text
	 */
	private void fixMenuIndent(JMenuItem pItem, Rectangle pRectIcon, Rectangle pRectText, int pIconTextGap)
	{
		JPopupMenu menu = (JPopupMenu)pItem.getParent();
		
		int iIconWidth = getIconWidth(menu);
		int iCheckRadioWidth = getCheckRadioWidth(menu);

		int iCurrentRadioWidth = 0;
		int iCurrentCheckWidth = 0;
		
		int iDefGap = UIManager.getInt("MenuItem.textIconGap"); 
		int iGap = 0;
		
		
		//Ermitteln der Breite von RadioButton oder CheckBox des aktuellen Items (falls nötig)
		if (pItem instanceof JRadioButtonMenuItem)
		{
			iCurrentRadioWidth = SmartPainter.getRadioButtonIconSize(pItem) + pIconTextGap;
			
			iGap = 2 * iDefGap;
		}
		else if (pItem instanceof JCheckBoxMenuItem)
		{
			iCurrentCheckWidth = SmartPainter.getCheckBoxIconSize(pItem) + pIconTextGap;
			
			iGap = 2 * iDefGap;
		} 
		
		//Eine Anpassung ist nur nötig, wenn das Menü einen RadioButton/CheckBox oder 
		//Icon darstellen muss
		if (iIconWidth > 0 || iCheckRadioWidth > 0)
		{
			if (iCurrentRadioWidth == 0 && iCurrentCheckWidth == 0)
			{
				iGap += iDefGap;
			}

			//Icon-Position nur ändern wenn es auch ein Icon gibt!
			if (iIconWidth > 0)
			{
				pRectIcon.x = (iCheckRadioWidth - iCurrentCheckWidth - iCurrentRadioWidth) + iGap;
				pRectIcon.width = iIconWidth;
			}

			//ohne Icon muss nicht nochmal zusätzlich der Abstand aufgerechnet werden (passierte bereits!
			if (iIconWidth > 0)
			{
				iGap += iDefGap;
			}
			
			int iOldX = pRectText.x;
			
			pRectText.x = (iCheckRadioWidth - iCurrentCheckWidth - iCurrentRadioWidth) + iGap + iIconWidth;
			pRectText.width += Math.max(0, pRectText.x - iOldX);
		}
	}
	
	/**
	 * Gets the maximum width of all menuitem icons of a menu.
	 * 
	 * @param pMenu the menu
	 * @return the maximum icon width or 0
	 */
	private int getIconWidth(JPopupMenu pMenu)
	{
		Component com;
		
		int iWidth = 0;
		
		for (int i = 0, anz = pMenu.getComponentCount(); i < anz; i++)
		{
			com = pMenu.getComponent(i); 
			
			if (com instanceof JMenuItem && ((JMenuItem)com).getIcon() != null)
			{
				iWidth = Math.max(iWidth, ((JMenuItem)com).getIcon().getIconWidth());
			}
		}
		
		return iWidth;
	}
	
	/**
	 * Gets the maximum width of all checkbox/radio button images of a menu.
	 * 
	 * @param pMenu the menu
	 * @return the maximum image width or 0
	 */
	private int getCheckRadioWidth(JPopupMenu pMenu)
	{
		Component com;
		
		int iWidth = 0;
		
		for (int i = 0, anz = pMenu.getComponentCount(); i < anz; i++)
		{
			com = pMenu.getComponent(i); 
			
			if (com instanceof JRadioButtonMenuItem)
			{
				iWidth = Math.max(iWidth, SmartPainter.getRadioButtonIconSize(com));
			}
			else if (com instanceof JCheckBoxMenuItem)
			{
				iWidth = Math.max(iWidth, SmartPainter.getCheckBoxIconSize(com));
			}
		}
		
		return iWidth;
	}
	
}	//SmartGraphicsUtils
