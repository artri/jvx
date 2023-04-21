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
 * 20.10.2008 - [JR] - mouseDragged: unset MOUSE_OVER (important for tab dragging)
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTabbedPane;

/**
 * The <code>TabbedPaneMouseListener</code> notifies a <code>JTabbedPane</code>
 * when the mouse moves over a tab.<p/>
 * This class is needed only for JVMs 1.5. Since 1.6 the mouse handling is fixed
 * in the JVM.
 * 
 * @author René Jahn
 */
public class TabbedPaneMouseListener implements MouseListener,
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
		mouseMoved(pEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent pEvent)
	{
		mouseMoved(pEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pEvent)
	{
	}

	// MOUSEMOTIONLISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseDragged(MouseEvent pEvent)
	{
		JTabbedPane tab = (JTabbedPane)pEvent.getSource();

		String sValue = (String)tab.getClientProperty("Smart/LF.MOUSE_OVER");
		
		int iOldTab = -1;
		
		
		if (sValue != null)
		{
			iOldTab = Integer.parseInt((String)sValue);
		}
		
		if (iOldTab >= 0)
		{
			tab.putClientProperty("Smart/LF.MOUSE_OVER", "-1");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(MouseEvent pEvent)
	{
		JTabbedPane tab = (JTabbedPane)pEvent.getSource();

		String sValue = (String)tab.getClientProperty("Smart/LF.MOUSE_OVER");
		
		int iOldTab = -1;
		
		
		if (sValue != null)
		{
			iOldTab = Integer.parseInt((String)sValue);
		}
		
		if (tab.isEnabled())
		{
			int iTab = tab.getUI().tabForCoordinate(tab, pEvent.getX(), pEvent.getY());
			
			if (iTab >= 0)
			{
				tab.putClientProperty("Smart/LF.MOUSE_OVER", "" + iTab);
				
				tab.repaint(tab.getUI().getTabBounds(tab, iTab));
			}
			else
			{
				tab.putClientProperty("Smart/LF.MOUSE_OVER", "-1");
			}
		}
		else
		{
			tab.putClientProperty("Smart/LF.MOUSE_OVER", "-1");
		}
		
		if (iOldTab >= 0)
		{
			//es könnte sein, daß das Tab entfernt wurde in einem MouseListener,
			//dann würde man hier eine IndexOutOfBoundsException erhalten!
			if (tab.getTabCount() > iOldTab)
			{
				tab.repaint(tab.getUI().getTabBounds(tab, iOldTab));
			}
		}
	}

}	// TabbedPaneMouseListener
