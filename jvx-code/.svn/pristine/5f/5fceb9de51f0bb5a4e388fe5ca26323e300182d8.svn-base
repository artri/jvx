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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

/**
 * The <code>TitlePaneActionListener</code> resets the {@link ButtonModel} after
 * the button was pressed. This is important in some circumstances because the
 * original implementation doesn't update the model. In that case the ui shows
 * wrong colors for the components.
 * 
 * @author René Jahn
 */
public class TitlePaneActionListener implements ActionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Resets the button model (selected, rollover, pressed, armed) of the event source.
	 * 
	 * @param pEvent the triggering event
	 */
	public void actionPerformed(ActionEvent pEvent)
	{
		ButtonModel model = ((AbstractButton)pEvent.getSource()).getModel();
		
		model.setSelected(false);
		model.setRollover(false);
		model.setPressed(false);
		model.setArmed(false);
	}
	
}	// TitlePaneActionListener
