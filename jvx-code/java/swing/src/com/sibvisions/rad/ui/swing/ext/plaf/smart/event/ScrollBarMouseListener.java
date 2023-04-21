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
package com.sibvisions.rad.ui.swing.ext.plaf.smart.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollBar;

/**
 * The <code>ScrollBarMouseListener</code> notifies a <code>JScrollBar</code>
 * over mouse events.<p/>
 * 
 * @author René Jahn
 */
public class ScrollBarMouseListener implements MouseListener,
                                               MouseMotionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// MOUSELISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent pEvent)
	{
		JScrollBar bar = (JScrollBar)pEvent.getSource();
		
		Boolean bValue = (Boolean)bar.getClientProperty("Smart/LF.MOUSE_OVER");
		
		bar.putClientProperty("Smart/LF.MOUSE_OVER", Boolean.valueOf(true));
		
		//Nur bei tatsächlicher Änderung repainten
		if (bValue == null || !bValue.booleanValue())
		{
			bar.repaint();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent pEvent)
	{
		JScrollBar bar = (JScrollBar)pEvent.getSource();
		
		Boolean bValue = (Boolean)bar.getClientProperty("Smart/LF.MOUSE_OVER");
		
		bar.putClientProperty("Smart/LF.MOUSE_OVER", Boolean.valueOf(false));
		
		//Nur bei tatsächlicher Änderung repainten
		if (bValue == null || bValue.booleanValue())
		{
			bar.repaint();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent pEvent)
	{
		JScrollBar bar = (JScrollBar)pEvent.getSource();
		
		Boolean bValue = (Boolean)bar.getClientProperty("Smart/LF.PRESSED");
		
		bar.putClientProperty("Smart/LF.PRESSED", Boolean.valueOf(true));
		bar.putClientProperty("Smart/LF.mouse.point", pEvent.getPoint());
		
		//Nur bei tatsächlicher Änderung repainten
		if (bValue == null || !bValue.booleanValue())
		{
			bar.repaint();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pEvent)
	{
		JScrollBar bar = (JScrollBar)pEvent.getSource();
		
		Boolean bValue = (Boolean)bar.getClientProperty("Smart/LF.PRESSED");
		
		bar.putClientProperty("Smart/LF.PRESSED", Boolean.valueOf(false));
		bar.putClientProperty("Smart/LF.mouse.point", null);
		
		//Nur bei tatsächlicher Änderung repainten
		if (bValue == null || bValue.booleanValue())
		{
			bar.repaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseDragged(MouseEvent pEvent)
	{
		JScrollBar bar = (JScrollBar)pEvent.getSource();

		bar.putClientProperty("Smart/LF.mouse.point", pEvent.getPoint());
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(MouseEvent pEvent)
	{
	}

}	// ScrollBarMouseListener
