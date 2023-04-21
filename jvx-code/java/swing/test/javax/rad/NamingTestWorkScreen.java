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

import javax.rad.application.genui.WorkScreen;
import javax.rad.genui.UIDimension;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UICheckBox;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIRadioButton;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.control.UITree;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * A simple workscreen that is used for testing.
 */
class NamingTestWorkScreen extends WorkScreen
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * tree1.
	 */
	UITree tree1 = new UITree();
	
	/**
	 * toggleButtonToggleButton.
	 */
	UIToggleButton toggleButtonToggleButton = new UIToggleButton();
	
	/**
	 * toggleButtonToggleButton1.
	 */
	UIToggleButton toggleButtonToggleButton1 = new UIToggleButton();
	
	/**
	 * radioButtonRadioButton.
	 */
	UIRadioButton radioButtonRadioButton = new UIRadioButton();
	
	/**
	 * radioButtonRadioButton1.
	 */
	UIRadioButton radioButtonRadioButton1 = new UIRadioButton();
	
	/**
	 * radioButtonRadioButton2.
	 */
	UIRadioButton radioButtonRadioButton2 = new UIRadioButton();
	
	/**
	 * radioButtonRadioButton3.
	 */
	UIRadioButton radioButtonRadioButton3 = new UIRadioButton();
	
	/**
	 * checkBoxCheckBox.
	 */
	UICheckBox checkBoxCheckBox = new UICheckBox();
	
	/**
	 * checkBoxCheckBox1.
	 */
	UICheckBox checkBoxCheckBox1 = new UICheckBox();
	
	/**
	 * checkBoxCheckBox2.
	 */
	UICheckBox checkBoxCheckBox2 = new UICheckBox();
	
	/**
	 * checkBoxCheckBox3.
	 */
	UICheckBox checkBoxCheckBox3 = new UICheckBox();
	
	/**
	 * groupPanelGruppierung.
	 */
	UIGroupPanel groupPanelGruppierung = new UIGroupPanel();
	
	/**
	 * groupPanelGruppierung1.
	 */
	UIGroupPanel groupPanelGruppierung1 = new UIGroupPanel();
	
	/**
	 * navigationTable.
	 */
	UITable navigationTable = new UITable();
	
	/**
	 * editMEMDATABOOKTESTSecond2.
	 */
	UIEditor editMEMDATABOOKTESTSecond2 = new UIEditor();
	
	/**
	 * editMEMDATABOOKTESTFirst.
	 */
	UIEditor editMEMDATABOOKTESTFirst = new UIEditor();
	
	/**
	 * editMEMDATABOOKTESTSecond.
	 */
	UIEditor editMEMDATABOOKTESTSecond = new UIEditor();
	
	/**
	 * editMEMDATABOOKTESTSecond3.
	 */
	UIEditor editMEMDATABOOKTESTSecond3 = new UIEditor();
	
	/**
	 * editMEMDATABOOKTESTThird.
	 */
	UIEditor editMEMDATABOOKTESTThird = new UIEditor();
	
	/**
	 * editMEMDATABOOKTESTFourth.
	 */
	UIEditor editMEMDATABOOKTESTFourth = new UIEditor();
	
	/**
	 * borderLayout1.
	 */
	UIBorderLayout borderLayout1 = new UIBorderLayout();
	
	/**
	 * borderLayout2.
	 */
	UIBorderLayout borderLayout2 = new UIBorderLayout();
	
	/**
	 * borderLayout3.
	 */
	UIBorderLayout borderLayout3 = new UIBorderLayout();
	
	/**
	 * splitPanelMain.
	 */
	UISplitPanel splitPanelMain = new UISplitPanel();
	
	/**
	 * splitPanelMainFirst.
	 */
	UIPanel splitPanelMainFirst = new UIPanel();
	
	/**
	 * splitPanelMainSecond.
	 */
	UIPanel splitPanelMainSecond = new UIPanel();
	
	/**
	 * panel1.
	 */
	UIPanel panel1 = new UIPanel();
	
	/**
	 * panel2.
	 */
	UIPanel panel2 = new UIPanel();
	
	/**
	 * formLayout1.
	 */
	UIFormLayout formLayout1 = new UIFormLayout();
	
	/**
	 * formLayout2.
	 */
	UIFormLayout formLayout2 = new UIFormLayout();
	
	/**
	 * formLayout3.
	 */
	UIFormLayout formLayout3 = new UIFormLayout();
	
	/**
	 * formLayout4.
	 */
	UIFormLayout formLayout4 = new UIFormLayout();
	
	/**
	 * labelBezeichner.
	 */
	UILabel labelBezeichner = new UILabel();
	
	/**
	 * labelBezeichner1.
	 */
	UILabel labelBezeichner1 = new UILabel();
	
	/**
	 * labelBezeichner2.
	 */
	UILabel labelBezeichner2 = new UILabel();
	
	/**
	 * labelBezeichner3.
	 */
	UILabel labelBezeichner3 = new UILabel();
	
	/**
	 * labelBezeichner4.
	 */
	UILabel labelBezeichner4 = new UILabel();
	
	/**
	 * labelBezeichner5.
	 */
	UILabel labelBezeichner5 = new UILabel();
	
	/**
	 * labelBezeichner6.
	 */
	UILabel labelBezeichner6 = new UILabel();
	
	/**
	 * labelBezeichner7.
	 */
	UILabel labelBezeichner7 = new UILabel();
	
	/**
	 * labelBezeichner8.
	 */
	UILabel labelBezeichner8 = new UILabel();
	
	/**
	 * labelBezeichner9.
	 */
	UILabel labelBezeichner9 = new UILabel();
	
	/**
	 * labelBezeichner10.
	 */
	UILabel labelBezeichner10 = new UILabel();
	
	/**
	 * labelBezeichner11.
	 */
	UILabel labelBezeichner11 = new UILabel();
	
	/**
	 * labelBezeichner12.
	 */
	UILabel labelBezeichner12 = new UILabel();
	
	/**
	 * labelBezeichner13.
	 */
	UILabel labelBezeichner13 = new UILabel();
	
	/**
	 * labelBezeichner14.
	 */
	UILabel labelBezeichner14 = new UILabel();
	
	/**
	 * buttonButton.
	 */
	UIButton buttonButton = new UIButton();
	
	/**
	 * dataBook.
	 */
	IDataBook dataBook = new MemDataBook();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new instance of <code>NamingTestWorkScreen</code>.
	 * 
	 * @throws Throwable if an error occurs.
	 */
	public NamingTestWorkScreen() throws Throwable
	{
		super();
		
		initializeModel();
		initializeUI();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		dataBook.setName("MEMDATABOOKTEST");
		
		dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRST"));
		dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("SECOND"));
		dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("THIRD"));
		dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("MULTICHOICE"));
		dataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition("CELLEDITOR"));
		
		Object[] allowedValues = new Object[] { "A", "B", "C" };
		String[] displayValues = new String[] { "Avalon", "Babylon", "Connecticut" };
		UIEnumCellEditor enumCellEditor = new UIEnumCellEditor(allowedValues, displayValues);
		dataBook.getRowDefinition().getColumnDefinition("CELLEDITOR").getDataType().setCellEditor(enumCellEditor);
		
		dataBook.open();
	}
	
	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		labelBezeichner.setText("Left of Button");
		
		labelBezeichner1.setText("Editor for FIRST");
		
		labelBezeichner2.setText("Editor for SECOND");
		
		labelBezeichner3.setText("Right top level label");
		
		labelBezeichner4.setText("Right second level label");
		
		labelBezeichner5.setText("Editor for SECOND 2");
		
		labelBezeichner6.setText("Editor for SECOND 3");
		
		labelBezeichner7.setText("Editor for THIRD");
		
		labelBezeichner8.setText("0-0");
		
		labelBezeichner9.setText("0-1");
		
		labelBezeichner10.setText("0-2");
		
		labelBezeichner11.setText("0-3");
		
		labelBezeichner12.setText("0-4");
		
		labelBezeichner13.setText("1-0");
		
		labelBezeichner14.setText("1-1");
		
		groupPanelGruppierung1.setText("Second GroupPanel");
		groupPanelGruppierung1.setLayout(formLayout4);
		groupPanelGruppierung1.add(labelBezeichner8, formLayout4.getConstraints(0, 0));
		groupPanelGruppierung1.add(labelBezeichner13, formLayout4.getConstraints(1, 0));
		groupPanelGruppierung1.add(checkBoxCheckBox, formLayout4.getVCenterConstraints(2, 0));
		groupPanelGruppierung1.add(radioButtonRadioButton, formLayout4.getVCenterConstraints(3, 0));
		groupPanelGruppierung1.add(labelBezeichner9, formLayout4.getConstraints(0, 1));
		groupPanelGruppierung1.add(labelBezeichner14, formLayout4.getConstraints(1, 1));
		groupPanelGruppierung1.add(checkBoxCheckBox1, formLayout4.getVCenterConstraints(2, 1));
		groupPanelGruppierung1.add(radioButtonRadioButton1, formLayout4.getVCenterConstraints(3, 1));
		groupPanelGruppierung1.add(labelBezeichner10, formLayout4.getConstraints(0, 2));
		groupPanelGruppierung1.add(checkBoxCheckBox2, formLayout4.getVCenterConstraints(2, 2));
		groupPanelGruppierung1.add(radioButtonRadioButton2, formLayout4.getVCenterConstraints(3, 2));
		groupPanelGruppierung1.add(labelBezeichner11, formLayout4.getConstraints(0, 3));
		groupPanelGruppierung1.add(checkBoxCheckBox3, formLayout4.getVCenterConstraints(2, 3));
		groupPanelGruppierung1.add(radioButtonRadioButton3, formLayout4.getVCenterConstraints(3, 3));
		groupPanelGruppierung1.add(labelBezeichner12, formLayout4.getConstraints(0, 4));
		groupPanelGruppierung1.add(toggleButtonToggleButton, formLayout4.getVCenterConstraints(2, 4));
		groupPanelGruppierung1.add(toggleButtonToggleButton1, formLayout4.getVCenterConstraints(3, 4));
		
		groupPanelGruppierung.setText("First GroupPanel");
		groupPanelGruppierung.setLayout(formLayout3);
		groupPanelGruppierung.add(labelBezeichner5, formLayout3.getConstraints(0, 0));
		groupPanelGruppierung.add(editMEMDATABOOKTESTSecond2, formLayout3.getConstraints(1, 0));
		groupPanelGruppierung.add(labelBezeichner6, formLayout3.getConstraints(0, 1));
		groupPanelGruppierung.add(editMEMDATABOOKTESTSecond3, formLayout3.getConstraints(1, 1));
		groupPanelGruppierung.add(labelBezeichner7, formLayout3.getConstraints(0, 2));
		groupPanelGruppierung.add(editMEMDATABOOKTESTThird, formLayout3.getConstraints(1, 2));
		groupPanelGruppierung.add(editMEMDATABOOKTESTFourth, formLayout3.getConstraints(1, 3));
		
		buttonButton.eventAction().addListener(this, "doButtonAction");
		buttonButton.setActionCommand("com.sibvisions.buttontest.SomeWorkScreen");
		buttonButton.setText("Button");
		
		editMEMDATABOOKTESTSecond2.setDataRow(dataBook);
		editMEMDATABOOKTESTSecond2.setColumnName("SECOND");
		
		editMEMDATABOOKTESTFirst.setDataRow(dataBook);
		editMEMDATABOOKTESTFirst.setColumnName("FIRST");
		
		editMEMDATABOOKTESTSecond.setDataRow(dataBook);
		editMEMDATABOOKTESTSecond.setColumnName("SECOND");
		
		editMEMDATABOOKTESTSecond3.setDataRow(dataBook);
		editMEMDATABOOKTESTSecond3.setColumnName("SECOND");
		
		editMEMDATABOOKTESTThird.setDataRow(dataBook);
		editMEMDATABOOKTESTThird.setColumnName("THIRD");
		
		editMEMDATABOOKTESTFourth.setDataRow(dataBook);
		editMEMDATABOOKTESTFourth.setColumnName("CELLEDITOR");
		
		navigationTable.setMaximumSize(new UIDimension(450, 350));
		navigationTable.setDataBook(dataBook);
		
		checkBoxCheckBox.setText("Check Box A");
		
		checkBoxCheckBox1.setText("Check Box B");
		
		checkBoxCheckBox2.setText("Check Box C");
		
		checkBoxCheckBox3.setText("Check Box D");
		
		radioButtonRadioButton.setText("Radio Button A");
		
		radioButtonRadioButton1.setText("Radio Button B");
		
		radioButtonRadioButton2.setText("Radio Button C");
		
		radioButtonRadioButton3.setText("Radio Button D");
		
		formLayout4.setAnchorConfiguration("l3=15");
		
		formLayout1.setAnchorConfiguration("l2=15");
		
		toggleButtonToggleButton.setText("Toggle Button");
		
		toggleButtonToggleButton1.setText("Toggle Button");
		
		tree1.setPreferredSize(new UIDimension(100, 150));
		tree1.setDataBooks(new IDataBook[] { dataBook });
		
		panel1.setLayout(formLayout1);
		panel1.add(labelBezeichner, formLayout1.getConstraints(0, 0));
		panel1.add(buttonButton, formLayout1.getVCenterConstraints(1, 0));
		panel1.add(labelBezeichner1, formLayout1.getConstraints(0, 1));
		panel1.add(editMEMDATABOOKTESTFirst, formLayout1.getConstraints(1, 1));
		panel1.add(labelBezeichner2, formLayout1.getConstraints(0, 2));
		panel1.add(editMEMDATABOOKTESTSecond, formLayout1.getConstraints(1, 2));
		panel1.add(navigationTable, formLayout1.getConstraints(0, 3, -1, -1));
		
		splitPanelMainFirst.setLayout(borderLayout2);
		splitPanelMainFirst.add(panel1, UIBorderLayout.CENTER);
		
		panel2.setLayout(formLayout2);
		panel2.add(labelBezeichner3, formLayout2.getConstraints(0, 0));
		panel2.add(labelBezeichner4, formLayout2.getConstraints(0, 1));
		panel2.add(groupPanelGruppierung, formLayout2.getConstraints(0, 2, -1, 2));
		panel2.add(groupPanelGruppierung1, formLayout2.getConstraints(0, 3, -1, 3));
		panel2.add(tree1, formLayout2.getConstraints(0, 4, -1, -1));
		
		splitPanelMainSecond.setLayout(borderLayout3);
		splitPanelMainSecond.add(panel2, UIBorderLayout.CENTER);
		
		splitPanelMain.setMinimumSize(new UIDimension(20, 20));
		splitPanelMain.setDividerPosition(294);
		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);
		
		setLayout(borderLayout1);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}
	
	/**
	 * Performs the button action.
	 */
	public void doButtonAction()
	{
		System.out.println("This does nothing.");
	}
	
} // NamingTestWorkScreen
