/*
 * Copyright 2021 SIB Visions GmbH
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
 * 03.09.2021 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;

import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

/**
 * A simple {@link JVxHtmlEditor} test.
 * 
 * @author René Jahn
 */
public class JVxHtmlEditorTest extends JFrame
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
		new JVxHtmlEditorTest();
	}
	
	/**
	 * Creates a new instance of <code>JVxHtmlEditorTest</code>.
	 * 
	 * @throws Throwable if creation fails
	 */
	JVxHtmlEditorTest() throws Throwable
	{
		//--------------------------------
		// Swing
		//--------------------------------
		
		JVxHtmlEditor editor = new JVxHtmlEditor();

		JVxBorderLayout layout = new JVxBorderLayout();
		layout.setMargins(new Insets(5, 5, 5, 5));
		
		setLayout(layout);
		
		add(editor, BorderLayout.CENTER);
		
		setPreferredSize(new Dimension(700, 400));
		pack();
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}	// JVxHtmlEditorTest
