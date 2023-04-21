/*
 * Copyright 2020 SIB Visions GmbH
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
 * 27.01.2020 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.sibvisions.rad.ui.swing.ext.layout.JVxSequenceLayout;

/**
 * A simple {@link JVxButton} test.
 * 
 * @author René Jahn
 */
public class JVxMenuButtonTest extends JFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Start method.
	 * 
	 * @param pArgs Application arguments
	 * @throws Throwable if start fails
	 */
	public static void main(String[] pArgs) throws Throwable
	{
		new JVxMenuButtonTest();
	}
	
	/**
	 * Creates a new instance of <code>JVxTableTest</code>.
	 * 
	 * @throws Throwable if creation fails
	 */
	JVxMenuButtonTest() throws Throwable
	{
		JPopupMenu pop = new JPopupMenu();
		pop.add(new JMenuItem("First"));
		pop.add(new JMenuItem("Second"));
		
		JVxButton button = new JVxButton();
		button.setText("Choose one");
		button.setPopupMenu(pop);
		
		setLayout(new JVxSequenceLayout());
		
		add(button);
		
		setPreferredSize(new Dimension(500, 400));
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}	// JVxTableTest
