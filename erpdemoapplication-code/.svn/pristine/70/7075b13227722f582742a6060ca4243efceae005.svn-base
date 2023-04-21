/*
 * Copyright 2013 SIB Visions GmbH
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
 */

package com.sibvisions.apps.demoerp.screens;

import java.math.BigDecimal;

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIDimension;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * The CustomerWorkScreen shows all customers in a list. It is possible to
 * create, update and delete customers. The difference to the CustomerWorkScreen
 * is, that it did not contains the toolbar actions to create a new offer, show
 * the offers and show orders.
 */
public class MobileCustomerWorkScreen extends DataSourceWorkScreen
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * filterEditor.
	 */
	private FilterEditor	filterEditor				= new FilterEditor();

	/**
	 * editCustomerCustomerNr.
	 */
	private UIEditor		editCustomerCustomerNr		= new UIEditor();

	/**
	 * editCustomerSalutation.
	 */
	private UIEditor		editCustomerSalutation		= new UIEditor();

	/**
	 * editCustomerTitle.
	 */
	private UIEditor		editCustomerTitle			= new UIEditor();

	/**
	 * editCustomerFirstname.
	 */
	private UIEditor		editCustomerFirstname		= new UIEditor();

	/**
	 * editCustomerSurname.
	 */
	private UIEditor		editCustomerSurname			= new UIEditor();

	/**
	 * editCustomerEmail.
	 */
	private UIEditor		editCustomerEmail			= new UIEditor();

	/**
	 * editCustomerCompany.
	 */
	private UIEditor		editCustomerCompany			= new UIEditor();

	/**
	 * editCustomerAddress.
	 */
	private UIEditor		editCustomerAddress			= new UIEditor();

	/**
	 * editCustomerZip.
	 */
	private UIEditor		editCustomerZip				= new UIEditor();

	/**
	 * editCustomerCity.
	 */
	private UIEditor		editCustomerCity			= new UIEditor();

	/**
	 * editCustomerCustomersince.
	 */
	private UIEditor		editCustomerCustomersince	= new UIEditor();

	/**
	 * labelCustomerNr.
	 */
	private UILabel			labelCustomerNr				= new UILabel();

	/**
	 * labelTitle.
	 */
	private UILabel			labelTitle					= new UILabel();

	/**
	 * labelSalutation.
	 */
	private UILabel			labelSalutation				= new UILabel();

	/**
	 * labelFirstname.
	 */
	private UILabel			labelFirstname				= new UILabel();

	/**
	 * labelSurname.
	 */
	private UILabel			labelSurname				= new UILabel();

	/**
	 * labelEmail.
	 */
	private UILabel			labelEmail					= new UILabel();

	/**
	 * labelCompany.
	 */
	private UILabel			labelCompany				= new UILabel();

	/**
	 * labelAddress.
	 */
	private UILabel			labelAddress				= new UILabel();

	/**
	 * labelZip.
	 */
	private UILabel			labelZip					= new UILabel();

	/**
	 * labelCity.
	 */
	private UILabel			labelCity					= new UILabel();

	/**
	 * labelCustomersince.
	 */
	private UILabel			labelCustomersince			= new UILabel();

	/**
	 * labelSuchen.
	 */
	private UILabel			labelSuchen					= new UILabel();

	/**
	 * groupPanelCustomer.
	 */
	private UIGroupPanel	groupPanelCustomer			= new UIGroupPanel();

	/**
	 * groupPanelCustomers.
	 */
	private UIGroupPanel	groupPanelCustomers			= new UIGroupPanel();

	/**
	 * tableCustomer.
	 */
	private NavigationTable	tableCustomer				= new NavigationTable();

	/**
	 * splitPanelMain.
	 */
	private UISplitPanel	splitPanelMain				= new UISplitPanel();

	/**
	 * splitPanelMainFirst.
	 */
	private UIPanel			splitPanelMainFirst			= new UIPanel();

	/**
	 * splitPanelMainSecond.
	 */
	private UIPanel			splitPanelMainSecond		= new UIPanel();

	/**
	 * panelSearch.
	 */
	private UIPanel			panelSearch					= new UIPanel();

	/**
	 * Its a customer remote databook.
	 */
	private RemoteDataBook	rdbCustomer					= new RemoteDataBook();

	/**
	 * formLayoutCustomer.
	 */
	private UIFormLayout	formLayoutCustomer			= new UIFormLayout();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout	formLayoutSearch			= new UIFormLayout();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout	borderLayoutMain			= new UIBorderLayout();

	/**
	 * borderLayout5.
	 */
	private UIBorderLayout	borderLayoutCustomer		= new UIBorderLayout();

	/**
	 * borderLayoutFirst.
	 */
	private UIBorderLayout	borderLayoutFirst			= new UIBorderLayout();

	/**
	 * borderLayoutSecond.
	 */
	private UIBorderLayout	borderLayoutSecond			= new UIBorderLayout();

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>CustomerWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public MobileCustomerWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		rdbCustomer.eventAfterInserted().addListener(this, "doCreateCustomerNr");
	}

	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		rdbCustomer.setName("customer");
		rdbCustomer.setDataSource(getDataSource());
		rdbCustomer.open();

		rdbCustomer.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "CUSTOMER_NR", "FIRSTNAME", "SURNAME", "COMPANY", "EMAIL" });

		rdbCustomer.getRowDefinition().getColumnDefinition("CUSTOMERSINCE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbCustomer.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		tableCustomer.setMaximumSize(new UIDimension(450, 350));
		tableCustomer.setDataBook(rdbCustomer);
		tableCustomer.setAutoResize(false);

		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSuchen, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(filterEditor, formLayoutSearch.getConstraints(1, 0, -1, 0));

		labelCustomerNr.setText("Customer Nr");

		labelTitle.setText("Title");

		labelSalutation.setText("Salutation");

		labelFirstname.setText("Firstname");

		labelSurname.setText("Surname");

		labelEmail.setText("Email");

		labelCompany.setText("Company");

		labelAddress.setText("Address");

		labelZip.setText("Zip");

		labelCity.setText("City");

		labelCustomersince.setText("Customersince");

		editCustomerCustomerNr.setDataRow(rdbCustomer);
		editCustomerCustomerNr.setColumnName("CUSTOMER_NR");

		editCustomerSalutation.setDataRow(rdbCustomer);
		editCustomerSalutation.setColumnName("SALU_SALUTATION");

		editCustomerTitle.setDataRow(rdbCustomer);
		editCustomerTitle.setColumnName("TITL_TITLE");

		editCustomerFirstname.setDataRow(rdbCustomer);
		editCustomerFirstname.setColumnName("FIRSTNAME");

		editCustomerSurname.setDataRow(rdbCustomer);
		editCustomerSurname.setColumnName("SURNAME");

		editCustomerEmail.setDataRow(rdbCustomer);
		editCustomerEmail.setColumnName("EMAIL");

		editCustomerCompany.setDataRow(rdbCustomer);
		editCustomerCompany.setColumnName("COMPANY");

		editCustomerAddress.setDataRow(rdbCustomer);
		editCustomerAddress.setColumnName("ADDRESS");

		editCustomerZip.setDataRow(rdbCustomer);
		editCustomerZip.setColumnName("ZIP");

		editCustomerCity.setDataRow(rdbCustomer);
		editCustomerCity.setColumnName("CITY");

		editCustomerCustomersince.setDataRow(rdbCustomer);
		editCustomerCustomersince.setColumnName("CUSTOMERSINCE");

		formLayoutCustomer.setAnchorConfiguration("t10=30");

		labelSuchen.setText("Search");

		groupPanelCustomer.setText("Customers");
		groupPanelCustomer.setLayout(borderLayoutCustomer);
		groupPanelCustomer.add(panelSearch, UIBorderLayout.NORTH);
		groupPanelCustomer.add(tableCustomer, UIBorderLayout.CENTER);

		filterEditor.setDataRow(rdbCustomer);

		borderLayoutFirst.setMargins(10, 10, 40, 10);
		splitPanelMainFirst.setLayout(borderLayoutFirst);
		splitPanelMainFirst.add(groupPanelCustomer, UIBorderLayout.CENTER);

		groupPanelCustomers.setText("Customer Detail");
		groupPanelCustomers.setLayout(formLayoutCustomer);
		groupPanelCustomers.add(labelCustomerNr, formLayoutCustomer.getConstraints(0, 0));
		groupPanelCustomers.add(editCustomerCustomerNr, formLayoutCustomer.getConstraints(1, 0, -1, 0));
		groupPanelCustomers.add(labelSalutation, formLayoutCustomer.getConstraints(0, 1));
		groupPanelCustomers.add(editCustomerSalutation, formLayoutCustomer.getConstraints(1, 1));
        groupPanelCustomers.add(labelTitle, formLayoutCustomer.getConstraints(0, 2));
		groupPanelCustomers.add(editCustomerTitle, formLayoutCustomer.getConstraints(1, 2));
		groupPanelCustomers.add(labelFirstname, formLayoutCustomer.getConstraints(0, 3));
		groupPanelCustomers.add(editCustomerFirstname, formLayoutCustomer.getConstraints(1, 3, -1, 3));
		groupPanelCustomers.add(labelSurname, formLayoutCustomer.getConstraints(0, 4));
		groupPanelCustomers.add(editCustomerSurname, formLayoutCustomer.getConstraints(1, 4, -1, 4));
		groupPanelCustomers.add(labelEmail, formLayoutCustomer.getConstraints(0, 5));
		groupPanelCustomers.add(editCustomerEmail, formLayoutCustomer.getConstraints(1, 5, -1, 5));
		groupPanelCustomers.add(labelCompany, formLayoutCustomer.getConstraints(0, 6));
		groupPanelCustomers.add(editCustomerCompany, formLayoutCustomer.getConstraints(1, 6, -1, 6));
		groupPanelCustomers.add(labelAddress, formLayoutCustomer.getConstraints(0, 7));
		groupPanelCustomers.add(editCustomerAddress, formLayoutCustomer.getConstraints(1, 7, -3, 7));
		groupPanelCustomers.add(labelZip, formLayoutCustomer.getConstraints(-2, 7));
		groupPanelCustomers.add(editCustomerZip, formLayoutCustomer.getConstraints(-1, 7));
		groupPanelCustomers.add(labelCity, formLayoutCustomer.getConstraints(0, 8));
		groupPanelCustomers.add(editCustomerCity, formLayoutCustomer.getConstraints(1, 8, -1, 8));
		groupPanelCustomers.add(labelCustomersince, formLayoutCustomer.getConstraints(0, 9));
		groupPanelCustomers.add(editCustomerCustomersince, formLayoutCustomer.getConstraints(1, 9, -1, 9));

		borderLayoutSecond.setMargins(10, 10, 10, 10);
		splitPanelMainSecond.setLayout(borderLayoutSecond);
		splitPanelMainSecond.add(groupPanelCustomers, UIBorderLayout.CENTER);

		splitPanelMain.setDividerPosition(395);
		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);

		setLayout(borderLayoutMain);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates the customer nr.
	 * 
	 * @throws Throwable
	 */
	public void doCreateCustomerNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbCustomer.getValue("ID");

		String customerNr = "CN - " + id;

		rdbCustomer.setValue("CUSTOMER_NR", customerNr);

		rdbCustomer.saveSelectedRow();
	}

} // MobileCustomerWorkScreen
