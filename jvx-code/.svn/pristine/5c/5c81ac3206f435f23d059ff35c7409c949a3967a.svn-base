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
 * 01.09.2014 - [RZ] - creation
 */
package javax.rad;

import java.util.List;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.menu.IMenuBar;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * Tests the naming of the {@link javax.rad.genui.UIComponent} class.
 * 
 * @author Robert Zenz
 */
public final class ComponentNamingTest
{
	/** the expected naming structure. */
	private static final String EXPECTED_STRUCTURE = "" +
			"    F1_MB1 : UIMenuBar [\n" +
			"        F1_MB1_MI1 : UIMenuItem\n" +
			"        F1_MI_COM-SIB-BUT-SOMWORSCR : UIMenuItem\n" +
			"        F1_MI_DOMENUITEMCLICKED : UIMenuItem\n" +
			"        F1_MB1_MI2 : UIMenuItem\n" +
			"    ]\n" +
			"    F1 : UIFrame [\n" +
			"        NamTes-O9 : NamingTestWorkScreen [\n" +
			"            NamTes-O9_SP1 : UISplitPanel [\n" +
			"                NamTes-O9_SP1_P1 : UIPanel [\n" +
			"                    NamTes-O9_SP1_P1_P1 : UIPanel [\n" +
			"                        NamTes-O9_SP1_P1_P1_L1 : UILabel\n" +
			"                        NamTes-O9_B_DOBUTTONACTION_COM-SIB-BUT-SOMWORSCR : UIButton\n" +
			"                        NamTes-O9_L_MEMDATABOOKTEST_FIRST : UILabel\n" +
			"                        NamTes-O9_E_MEMDATABOOKTEST_FIRST : UIEditor\n" +
			"                        NamTes-O9_L_MEMDATABOOKTEST_SECOND : UILabel\n" +
			"                        NamTes-O9_E_MEMDATABOOKTEST_SECOND : UIEditor\n" +
			"                        NamTes-O9_T_MEMDATABOOKTEST : UITable\n" +
			"                    ]\n" +
			"                ]\n" +
			"                NamTes-O9_SP1_P2 : UIPanel [\n" +
			"                    NamTes-O9_SP1_P2_P1 : UIPanel [\n" +
			"                        NamTes-O9_SP1_P2_P1_L1 : UILabel\n" +
			"                        NamTes-O9_SP1_P2_P1_L2 : UILabel\n" +
			"                        NamTes-O9_SP1_P2_P1_GP1 : UIGroupPanel [\n" +
			"                            NamTes-O9_L_MEMDATABOOKTEST_SECOND2 : UILabel\n" +
			"                            NamTes-O9_E_MEMDATABOOKTEST_SECOND2 : UIEditor\n" +
			"                            NamTes-O9_L_MEMDATABOOKTEST_SECOND3 : UILabel\n" +
			"                            NamTes-O9_E_MEMDATABOOKTEST_SECOND3 : UIEditor\n" +
			"                            NamTes-O9_L_MEMDATABOOKTEST_THIRD : UILabel\n" +
			"                            NamTes-O9_E_MEMDATABOOKTEST_THIRD : UIEditor\n" +
			"                            NamTes-O9_E_MEMDATABOOKTEST_CELLEDITOR : UIEditor\n" +
			"                        ]\n" +
			"                        NamTes-O9_SP1_P2_P1_GP2 : UIGroupPanel [\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L1 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L2 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_CB1 : UICheckBox\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_RB1 : UIRadioButton\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L3 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L4 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_CB2 : UICheckBox\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_RB2 : UIRadioButton\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L5 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_CB3 : UICheckBox\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_RB3 : UIRadioButton\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L6 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_CB4 : UICheckBox\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_RB4 : UIRadioButton\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_L7 : UILabel\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_TB1 : UIToggleButton\n" +
			"                            NamTes-O9_SP1_P2_P1_GP2_TB2 : UIToggleButton\n" +
			"                        ]\n" +
			"                        NamTes-O9_Tr_MEMDATABOOKTEST : UITree\n" +
			"                    ]\n" +
			"                ]\n" +
			"            ]\n" +
			"        ]\n" +
			"    ]\n";
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instances needed.
	 */
	private ComponentNamingTest()
	{
	}
	
	/**
	 * Dumps the structure of the test workscreen to stdout.
	 * 
	 * @param args not used.
	 * @throws Throwable if something goes wrong.
	 */
	public static void main(String[] args) throws Throwable
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		UIFrame frame = new UIFrame();
		NamingTestWorkScreen testScreen = new NamingTestWorkScreen();
		
		UIMenuItem anotherMenuItem = new UIMenuItem("Another");
		anotherMenuItem.setActionCommand("com.sibvisions.buttontest.SomeWorkScreen");
		
		IMenuBar menuBar = new UIMenuBar();
		menuBar.add(new UIMenuItem("Something"));
		menuBar.add(anotherMenuItem);
		menuBar.add(new UIMenuItem("Menu", new ComponentNamingTest(), "doMenuItemClicked"));
		menuBar.add(new UIMenuItem("Item"));
		frame.setMenuBar(menuBar);
		
		frame.add(testScreen);
		frame.addNotify();
		
		System.out.println("Expected structure follows:");
		System.out.println("    (NAME : CLASSNAME)");
		System.out.println(EXPECTED_STRUCTURE);
		System.out.println();
		System.out.println();
		
		String actualStructure = dumpStructure(frame.getMenuBar(), 1)
				+ dumpStructure(frame, 1);
		
		System.out.println("Structure of the test workscreen follows:");
		System.out.println("    (NAME : CLASSNAME)");
		System.out.println(actualStructure);
		System.out.println();
		System.out.println();
		
		System.out.println("Comparison follows:");
		
		List<String> expectedLines = StringUtil.separateList(EXPECTED_STRUCTURE, "\n", false);
		List<String> actualLines = StringUtil.separateList(actualStructure, "\n", false);
		
		boolean differs = false;
		
		for (int index = 0; index < Math.min(expectedLines.size(), actualLines.size()); index++)
		{
			String expectedLine = expectedLines.get(index);
			String actualLine = actualLines.get(index);
			
			if (!expectedLine.equals(actualLine))
			{
				differs = true;
				
				System.out.println(Integer.toString(index) + ": - " + expectedLine);
				System.out.println(Integer.toString(index) + ": + " + actualLine);
				System.out.println();
			}
		}
		
		if (!differs)
		{
			System.out.println("No differences.");
		}
	}
	
	/**
	 * Menu item clicked event listener.
	 */
	public void doMenuItemClicked() 
	{
		// Nothing to do.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Dumps the structure of the given component into a string.
	 * 
	 * @param pComponent the component at which to start.
	 * @param pIndentLevel the indentation level.
	 * @return the structure as string.
	 */
	private static String dumpStructure(IComponent pComponent, int pIndentLevel)
	{
		StringBuilder structure = new StringBuilder();
		
		String padding = StringUtil.lpad("", pIndentLevel * 4);
		
		structure.append(padding);
		structure.append(pComponent.getName());
		structure.append(" : ");
		structure.append(pComponent.getClass().getSimpleName());
		
		if (pComponent instanceof IContainer)
		{
			IContainer container = (IContainer)pComponent;
			
			if (container.getComponentCount() > 0)
			{
				structure.append(" [\n");
				
				for (int idx = 0, count = container.getComponentCount(); idx < count; idx++)
				{
					structure.append(dumpStructure(container.getComponent(idx), pIndentLevel + 1));
				}
				structure.append(padding);
				structure.append("]\n");
			}
		}
		else
		{
			structure.append("\n");
		}
		
		return structure.toString();
	}
	
}	// ComponentNamingTest
