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
 * 01.09.2014 - [RZ] - creation
 */
package javax.rad;

import java.util.HashSet;
import java.util.Set;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.menu.IMenuBar;
import javax.swing.JComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests the {@link javax.rad.genui.UIComponent} class.
 * 
 * @author Robert Zenz
 */
public class TestComponent
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
	 * Tests if names that are set by the client are preserved and not changed.
	 */
	@Test
	public void testCustomNameIsPreserved()
	{
		UILabel label = new UILabel("Label");
		label.setName("LabelNameTest");
		
		label.addNotify();
		
		Assert.assertEquals("LabelNameTest", label.getName());
	}
	
	/**
	 * Tests if all names in the test screen are unique.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void testIfNamesAreUnique() throws Throwable
	{
		UIFrame frame = new UIFrame();
		NamingTestWorkScreen testScreen = new NamingTestWorkScreen();

		IMenuBar menuBar = new UIMenuBar();
		menuBar.add(new UIMenuItem("Something"));
		menuBar.add(new UIMenuItem("Another"));
		menuBar.add(new UIMenuItem("Menu"));
		menuBar.add(new UIMenuItem("Item"));
		frame.setMenuBar(menuBar);
		
		frame.add(testScreen);
		frame.addNotify();
		
		assertComponentNamesAreUnique(frame.getMenuBar(), new HashSet<String>());
		assertComponentNamesAreUnique(frame, new HashSet<String>());
	}
	
	/**
	 * Tests a few components if they have the correct name.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void testSpecialNames() throws Throwable
	{
		NamingTestWorkScreen testScreen = new NamingTestWorkScreen();
		testScreen.addNotify();
		
		Assert.assertEquals("NamTes-O9_B_DOBUTTONACTION_COM-SIB-BUT-SOMWORSCR", testScreen.buttonButton.getName());
		Assert.assertEquals("NamTes-O9_E_MEMDATABOOKTEST_FIRST", testScreen.editMEMDATABOOKTESTFirst.getName());
		Assert.assertEquals("NamTes-O9_T_MEMDATABOOKTEST", testScreen.navigationTable.getName());
	}
	
	/**
	 * Tests the names of a wrapped component.
	 * 
	 * @throws Throwable if the test fails.
	 */
	@Test
	public void testWrappedNames() throws Throwable
	{
		WrappedComponent wrapped = new WrappedComponent();
		wrapped.addNotify();
		
		Assert.assertEquals("WC1", wrapped.getName());
		Assert.assertEquals("WC1", ((JComponent)wrapped.getResource()).getName());
		Assert.assertEquals("WC1-wrapper", ((JComponent)wrapped.getResource()).getParent().getName());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Asserts that all component names are unique.
	 * 
	 * @param pComponent the component to start with.
	 * @param pNames the {@link Set} containing all previous names.
	 */
	private void assertComponentNamesAreUnique(IComponent pComponent, Set<String> pNames)
	{
		if (pNames.contains(pComponent.getName()))
		{
			Assert.fail("Duplicate name: " + pComponent.getName());
		}
		
		pNames.add(pComponent.getName());
		
		if (pComponent instanceof IContainer)
		{
			IContainer container = (IContainer) pComponent;
			
			if (container.getComponentCount() > 0)
			{
				for (int idx = 0, count = container.getComponentCount(); idx < count; idx++)
				{
					assertComponentNamesAreUnique(container.getComponent(idx), pNames);
				}
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple "wrapped" component.
	 * 
	 * @author Robert Zenz
	 */
	private static final class WrappedComponent extends UILabel
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link WrappedComponent}.
		 */
		public WrappedComponent()
		{
			super("Content");
			
			UIGroupPanel wrapper = new UIGroupPanel("Wrapper");
			wrapper.setLayout(new UIBorderLayout());
			wrapper.getUIResource().add(getUIResource(), UIBorderLayout.CENTER);
			
			setUIComponent(wrapper);
		}
		
	}	// WrappedComponent
	
}	// TestComponent
