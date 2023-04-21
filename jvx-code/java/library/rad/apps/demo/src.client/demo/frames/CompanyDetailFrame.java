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
 * 02.06.2009 - [JR] - redesign
 */
package demo.frames;

import javax.rad.genui.UIDimension;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.layout.UIFormLayout;

import demo.Demo;
import demo.frames.special.AddressFrame;

/**
 * The <code>CompanyDetailFrame</code> is a sample screen for
 * accessing other frames.
 * 
 * @author René Jahn
 */
public class CompanyDetailFrame extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the application. */
	private Demo demo;
	
	/** the textfield for the desired selection row for the opened address frame. */
	private UITextField tfRow = new UITextField();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>CompanyDetailFrame</code> for the {@link Demo} application.
	 * 
	 * @param pDemo the application
	 */
	public CompanyDetailFrame(Demo pDemo)
	{
		super(pDemo.getDesktopPane());
		
		demo = pDemo;
		
		initUI();
	}
	
	/**
	 * Initializes the UI.
	 */
	private void initUI()
	{
		//-------------------------------------------------------
		// Panels
		//-------------------------------------------------------

		setLayout(new UIFormLayout());
		
		tfRow.setText("2");
		tfRow.setColumns(3);
		
		UILabel lblRow = new UILabel();
		lblRow.setText("Desired row:");
		
		UIButton butOpenStamm = new UIButton();
		butOpenStamm.setText("Show address");
		butOpenStamm.eventAction().addListener(this, "doShowAddress");
		
		add(lblRow);
		add(tfRow);
		add(butOpenStamm);
		
		//-------------------------------------------------------
		// Frame
		//-------------------------------------------------------

		setTitle("Company details");
		setResizable(false);
		setMaximizable(false);
		setIconifiable(false);
		setSize(new UIDimension(250, 90));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens the Address frame.
	 * 
	 * @throws Exception if an error occurs
	 */
	public void doShowAddress() throws Exception
	{
		AddressFrame frame = (AddressFrame)demo.openFrame(AddressFrame.class);

		frame.setSelectedRow(Integer.valueOf(tfRow.getText()));
	}

}	// CompanyDetailFrame
