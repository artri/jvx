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
 * 18.11.2014 - [RZ] - creation
 */
package javax.rad;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.component.UIButton;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests the {@link javax.rad.genui.UIButton} class.
 * 
 * @author Robert Zenz
 */
public class TestButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets called before any test and sets the Swing Factory.
	 */
	@Before
	public void setUp()
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the name creation of the button.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void testGeneratedName() throws Throwable
	{
		UIButton button = new UIButton();
		
		// Buttons should not have a default name.
		Assert.assertNull(button.getName());
		
		button.addNotify();
		
		Assert.assertEquals("B1", button.getName());
		
		button.removeNotify();
		
		assertCorrectNameFromActionListener(button, this, "doSimpleActionMethod");
		assertCorrectNameFromActionListener(button, this, "doSimpleActionMethodThatThrows");
		assertCorrectNameFromActionListener(button, this, "doSimpleActionMethodWithParam");
		assertCorrectNameFromActionListener(button, this, "doSimpleActionMethodWithParamThatThrows");
		
		// Test nested classes.
		button.eventAction().removeAllListeners();
		button.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				// Does nothing.
			}
			
		});
		
		button.addNotify();
		
		Assert.assertEquals("B1", button.getName());
		
		button.removeNotify();
		
		// Test if the first named listener wins.
		button.eventAction().addListener(this, "doSimpleActionMethod");
		button.addNotify();
		
		Assert.assertEquals("B_DOSIMPLEACTIONMETHOD", button.getName());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Asserts that the name of the given {@link UIButton} is composed of
	 * the prefix and the name of the given action listener method.
	 * 
	 * @param pButton the {@link UIButton} to test.
	 * @param pListener the listener with the method.
	 * @param pMethodName the name of the method.
	 */
	private void assertCorrectNameFromActionListener(UIButton pButton, Object pListener, String pMethodName)
	{
		pButton.eventAction().addListener(pListener, pMethodName);
		
		pButton.addNotify();
		
		Assert.assertEquals("B_" + pMethodName.toUpperCase(), pButton.getName());
		
		pButton.removeNotify();
		pButton.eventAction().removeAllListeners();
	}
	
	/**
	 * A stub method used for testing the action listeners.
	 */
	public void doSimpleActionMethod()
	{
		
	}
	
	/**
	 * A stub method used for testing the action listeners.
	 * 
	 * @throws Throwable never happens.
	 */
	public void doSimpleActionMethodThatThrows() throws Throwable
	{
		
	}
	
	/**
	 * A stub method used for testing the action listeners.
	 * 
	 * @param pEvent not used.
	 */
	public void doSimpleActionMethodWithParam(UIActionEvent pEvent)
	{
		
	}
	
	/**
	 * A stub method used for testing the action listeners.
	 * 
	 * @param pEvent not used.
	 * @throws Throwable never happens.
	 */
	public void doSimpleActionMethodWithParamThatThrows(UIActionEvent pEvent) throws Throwable
	{
		
	}
	
}	// TestButton
