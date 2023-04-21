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
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.container.UIToolBarPanel;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SubConnection;

import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.model.remote.RemoteDataBook;

import demo.Demo;

/**
 * The <code>CompanyFrame</code> shows company information.
 * 
 * @author René Jahn
 */
public class CompanyFrame extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
	/** the application. */
	private Demo demo;
	
	/** the data source. */
	private RemoteDataSource rds;
	
	/** company data access. */
	private	RemoteDataBook	rdbCompany			= new RemoteDataBook();
	/** persons data access. */
	private	RemoteDataBook	rdbPersons			= new RemoteDataBook();

	/** company table. */
	private UITable			tblCompany			= new UITable();
	/** persons table. */
	private UITable			tblPersons			= new UITable();
	
	/** new company. */
	private UIButton		butNewCompany		= new UIButton();
	/** delete company. */
	private UIButton		butDeleteCompany	= new UIButton();
	/** export company. */
	private UIButton		butExport			= new UIButton();
	/** save company. */
	private UIButton		butSave				= new UIButton();
	/** reload company. */
	private UIButton		butReload			= new UIButton();

	/** new person. */
	private UIButton		butNewPerson		= new UIButton();
	/** delete person. */
	private UIButton		butDeletePerson		= new UIButton();

	/** label for company name. */
	private UILabel			lblCompanyName		= new UILabel();
	/** label for company address. */
	private UILabel			lblCompanyAddress 	= new UILabel();
	/** label for first name. */
	private UILabel			lblFirstName		= new UILabel();
	/** label for last name. */
	private UILabel			lblLastName			= new UILabel();
	/** label for date of birth. */
	private UILabel			lblBirthDate		= new UILabel();
	/** label for title. */
	private UILabel			lblTitle			= new UILabel();
	
	/** textfield for company name. */
	private UITextField		tfCompanyName		= new UITextField();
	/** textfield for company address. */
	private UITextField		tfCompanyAddress	= new UITextField();
	/** textfield for first name. */
	private UITextField		tfFirstName			= new UITextField();
	/** textfield for last name. */
	private UITextField		tfLastName			= new UITextField();
	/** textfield for date of birt. */
	private UITextField		tfBirthDate			= new UITextField();
	/** textfield for tite. */
	private UITextField		tfTitle				= new UITextField();
	
	/** the simple form layout panel with detail information. */
	private	UIPanel	panelDetails				= new UIPanel();
	/** the company panel. */
	private UIToolBarPanel	panelCompany		= new UIToolBarPanel();
	/** the persons panel. */
	private UIToolBarPanel	panelPersonen		= new UIToolBarPanel();
	/** the split between company and persons. */
	private UISplitPanel	splitV				= new UISplitPanel();
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>CompanyFrame</code> for the {@link Demo} application.
	 * 
	 * @param pDemo the application
	 * @throws Throwable if an exception occured during initialization
	 */
	public CompanyFrame(Demo pDemo) throws Throwable
	{
		super(pDemo.getDesktopPane());
		
		demo = pDemo;
		
		//open a connection for the screen
		SubConnection con = ((MasterConnection)pDemo.getConnection()).createSubConnection("demo.Company");
		con.open();
		
		rds = new RemoteDataSource(con);
		rds.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		rds.open();

		rdbCompany.setDataSource(rds);
		rdbCompany.setName("company");
		rdbCompany.open();
		
		rdbPersons.setDataSource(rds);
		rdbPersons.setName("persons");
		rdbPersons.setMasterReference(new ReferenceDefinition(new String[] {"PERS_ID"}, rdbCompany, new String[] {"ID"}));
		rdbPersons.open();
		
		rdbPersons.setSelectedRow(-1);
		
		initUI();
	}

	/**
	 * Initializes the UI.
	 */
	private void initUI()
	{
		tblCompany.setDataBook(rdbCompany);
		tblPersons.setDataBook(rdbPersons);

		//-------------------------------------------------------
		// ToolBars
		//-------------------------------------------------------
		
		UIToolBar tbManipulationCompany = new UIToolBar();
		
		configureToolBarButton(butNewCompany, "New", "doNewCompany", UIImage.ADD_SMALL);
		configureToolBarButton(butDeleteCompany, "Remove", "doDeleteCompany", UIImage.REMOVE_SMALL);
		
		tbManipulationCompany.add(butNewCompany);
		tbManipulationCompany.add(butDeleteCompany);
		
		UIToolBar tbConfig = new UIToolBar();
		
		configureToolBarButton(butExport, "Export", null, UIImage.EXPORT_SMALL);
		butExport.setEnabled(false);
		
		tbConfig.add(butExport);

		UIToolBar tbPersist = new UIToolBar();

		configureToolBarButton(butSave, "Save", "doSave", UIImage.SAVE_SMALL);
		configureToolBarButton(butReload, "Reload", "doReload", UIImage.RELOAD_SMALL);

		tbPersist.add(butSave);
		tbPersist.add(butReload);
		
		UIToolBar tbManipulationPersons = new UIToolBar();
		
		configureToolBarButton(butNewPerson, "New Personen", "doNewPerson", UIImage.ADD_SMALL);
		butNewPerson.setEnabled(false);
		
		configureToolBarButton(butDeletePerson, "Remove Personen", "doDeletePerson", UIImage.REMOVE_SMALL);
		butDeletePerson.setEnabled(false);
		
		tbManipulationPersons.add(butNewPerson);
		tbManipulationPersons.add(butDeletePerson);
		
		//-------------------------------------------------------
		// Panels
		//-------------------------------------------------------

		panelCompany.setLayout(new UIBorderLayout());
		panelPersonen.setLayout(new UIBorderLayout());
				
		panelCompany.setToolBarArea(AREA_LEFT);
		panelCompany.addToolBar(tbManipulationCompany);
		panelCompany.addToolBar(tbConfig);
		panelCompany.addToolBar(tbPersist);
		panelCompany.add(tblCompany, UIBorderLayout.CENTER);
		
		panelPersonen.setToolBarArea(AREA_LEFT);
		panelPersonen.addToolBar(tbManipulationPersons);
		panelPersonen.add(tblPersons, UIBorderLayout.CENTER);

		splitV.setOrientation(UISplitPanel.SPLIT_TOP_BOTTOM);
		splitV.add(panelCompany, null);
		splitV.add(panelPersonen, null);		
		
		splitV.setDividerPosition(300);
		
		// Detail
		
		panelDetails.setLayout(new UIFormLayout());
		
		lblCompanyName.setText("Firmen Name:");
		lblCompanyAddress.setText("Firmen Adresse:");
		lblTitle.setText("Titel:");
		lblFirstName.setText("Vorname:");
		lblLastName.setText("Nachname:");
		lblBirthDate.setText("Geburtsdatum:");
		
		panelDetails.add(lblCompanyName);
		panelDetails.add(tfCompanyName);
		panelDetails.add(lblCompanyAddress, UIFormLayout.NEWLINE);
		panelDetails.add(tfCompanyAddress);
		panelDetails.add(new UILabel(), UIFormLayout.NEWLINE);
		panelDetails.add(lblTitle, UIFormLayout.NEWLINE);
		panelDetails.add(tfTitle);
		panelDetails.add(lblFirstName, UIFormLayout.NEWLINE);
		panelDetails.add(tfFirstName);
		panelDetails.add(lblLastName, UIFormLayout.NEWLINE);
		panelDetails.add(tfLastName);
		panelDetails.add(lblBirthDate, UIFormLayout.NEWLINE);
		panelDetails.add(tfBirthDate);		
		
		UIButton butOpenDetail = new UIButton();
		butOpenDetail.setText("Open Detail frame");
		butOpenDetail.eventAction().addListener(this, "doOpenDetailFrame");
		
		panelDetails.add(butOpenDetail, UIFormLayout.NEWLINE);
		
		add(splitV, UIBorderLayout.CENTER);
		add(panelDetails, UIBorderLayout.EAST);
		
		//-------------------------------------------------------
		// Frame
		//-------------------------------------------------------

		setTitle("Company");
	}
	
	/**
	 * Configures a standard button for the toolbar layout.
	 * 
	 * @param pButton the button
	 * @param pText the button text
	 * @param pAction the action method for the button
	 * @param pIcon the name of the predefined icon
	 */
	private void configureToolBarButton(UIButton pButton, String pText, String pAction, String pIcon)
	{
		pButton.setImage(UIImage.getImage(pIcon));
		pButton.setToolTipText(pText);
		pButton.setMinimumSize(new UIDimension(0, 0));
		pButton.setPreferredSize(new UIDimension(23, 23));
		pButton.setFocusable(false);
		
		if (pAction != null)
		{
			pButton.eventAction().addListener(this, pAction);
		}
		
		//to be LaF independent
		pButton.setBorderOnMouseEntered(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Performs insert on the company DataBook.
	 *  
	 * @throws Exception if it's not possible to insert a new company
	 */
	public void doNewCompany() throws Exception
	{
		rdbCompany.insert(false);
	}

	/**
	 * Performs delete on the company DataBook.
	 *  
	 * @throws Exception if it's not possible to delete a company
	 */
	public void doDeleteCompany() throws Exception
	{
		rdbCompany.delete();
	}

	/**
	 * Saves all company changes.
	 *  
	 * @throws Exception if an error occurs
	 */
	public void doSave() throws Exception
	{
		rdbCompany.saveAllRows();
	}
	
	/**
	 * Reloads the company DataBook.
	 * 
	 * @throws Exception if an error occurs
	 */
	public void doReload() throws Exception
	{
		rdbCompany.reload();
	}	
	
	/**
	 * Performs insert on the persons DataBook.
	 *  
	 * @throws Exception if it's not possible to insert a new person
	 */
	public void doNewPerson() throws Exception
	{
		rdbPersons.insert(false);
	}
	
	/**
	 * Performs delete on the persons DataBook.
	 *  
	 * @throws Exception if it's not possible to delete a person
	 */
	public void doDeletePerson() throws Exception
	{
		rdbPersons.delete();
	}
	
	/**
	 * Opens the {@link CompanyDetailFrame}.
	 * 
	 * @throws Throwable if the frame could not be opened
	 */
	public void doOpenDetailFrame() throws Throwable
	{
		demo.openFrame(CompanyDetailFrame.class);
	}  
	
}	// CompanyFrame
