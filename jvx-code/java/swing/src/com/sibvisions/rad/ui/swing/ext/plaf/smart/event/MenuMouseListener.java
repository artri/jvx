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

import javax.swing.JMenu;

/**
 * The <code>MenuMouseListener</code> notifies a <code>JMenu</code>
 * over mouse events.<p/>
 * 
 * @author René Jahn
 */
public class MenuMouseListener implements MouseListener
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
		JMenu menu = (JMenu)pEvent.getSource();
		
		menu.putClientProperty("Smart/LF.MOUSE_OVER", Boolean.valueOf(true));
		menu.repaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent pEvent)
	{
		JMenu menu = (JMenu)pEvent.getSource();
		
		menu.putClientProperty("Smart/LF.MOUSE_OVER", Boolean.valueOf(false));
		menu.repaint();
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
	
}	// MenuMouseListener
