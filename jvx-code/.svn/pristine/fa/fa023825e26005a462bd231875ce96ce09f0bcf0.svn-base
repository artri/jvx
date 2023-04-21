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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;

/**
 * The <code>InternalFramePropertyChangeListener</code> will be used to detect changed
 * properties in an internal frame.
 * 
 * @author René Jahn
 */
public class InternalFramePropertyChangeListener implements PropertyChangeListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("frameIcon".equals(evt.getPropertyName()))
		{
			JInternalFrame frame = (JInternalFrame)evt.getSource();
			
	    	BasicInternalFrameTitlePane title = SmartLookAndFeel.getNorthPane(frame);
			
			Component comp;
			
			//Das Icon des Menüs muss manuell getauscht werden -> keine Implementierung
			//im synth l&f von sun, bis inkl. 1.6, vorgesehen!
			if (title != null)
			{
				for (int i = 0, anz = title.getComponentCount(); i < anz; i++)
				{
					comp = title.getComponent(i);
					
					if ("InternalFrameTitlePane.menuButton".equals(comp.getName()))
					{
						if (comp instanceof AbstractButton)
						{
							AbstractButton button = (AbstractButton)comp;
							
							button.setIcon(frame.getFrameIcon());
						}
					}
				}
			}
		}
	}

}	// InternalFramePropertyChangeListener
