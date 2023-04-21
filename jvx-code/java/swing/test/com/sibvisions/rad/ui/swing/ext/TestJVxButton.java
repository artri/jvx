/*
 * Copyright 2014 SIB Visions GmbH
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
 * 15.09.2014 - [RZ] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.component.UIButton;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests the {@link JVxButton}.
 * 
 * @author Robert Zenz
 */
public class TestJVxButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link TestJVxButton}.
	 */
	public TestJVxButton()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests that the {@link JVxButton#getActionCommand()} does not return the
	 * text of the button.
	 */
	@Test
	public void testActionCommandIsNotText()
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		UIButton button = new UIButton();
		JVxButton jvxButton = (JVxButton) button.getResource();
		
		button.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				Assert.assertNull(pActionEvent.getActionCommand());
			}
		});
		
		Assert.assertNull(button.getActionCommand());
		jvxButton.doClick();
		
		button.setText("Text");
		
		Assert.assertNull(button.getActionCommand());
		jvxButton.doClick();
		
		button.setActionCommand("ActionCommand");
		
		button.eventAction().removeAllListeners();
		
		button.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				Assert.assertEquals("ActionCommand", pActionEvent.getActionCommand());
			}
		});
		
		jvxButton.doClick();
	}
	
}	// TestJVxButton
