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
 * 12.10.2008 - [JR] - change listener if necessary and not already done
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.event;

import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.style.SmartComboBoxLayoutManager;

/**
 * The <code>ComboBoxPropertyChangeListener</code> will be used to detect changed
 * properties in a combo box.
 * 
 * @author René Jahn
 */
public class ComboBoxPropertyChangeListener implements PropertyChangeListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("UI".equals(evt.getPropertyName()))
		{
			JComboBox box = (JComboBox)evt.getSource();

			LayoutManager layout = box.getLayout();
			
			if (!(layout instanceof SmartComboBoxLayoutManager))
			{
				//Kennzeichnung, damit das Layout nicht erneut installiert wird!
				box.putClientProperty("Smart/LF.oldlayout", layout);
	
				box.setLayout(new SmartComboBoxLayoutManager(box));
			}
		}
	}

}	// ComboBoxPropertyChangeListener
