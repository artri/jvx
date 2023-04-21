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
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import javax.rad.genui.UIInsets;

/**
 * The CustomersWorkScreen shows all customers in a list. It is possible to
 * create, update and delete customers.
 */
public class CustomersWorkScreen extends DataSourceWorkScreen
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	 * filterEditor.
	 */
	private FilterEditor	filterEditor				= new FilterEditor();

	/**
	 * buttonNewOffer.
	 */
	private UIButton		buttonNewOffer				= new UIButton();

	/**
	 * buttonShowOffers.
	 */
	private UIButton		buttonShowOffers			= new UIButton();

	/**
	 * buttonShowOrders.
	 */
	private UIButton		buttonShowOrders			= new UIButton();

	/**
	 * labelSuchen.
	 */
	private UILabel			labelSuchen					= new UILabel();

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
	 * groupPanelCustomer.
	 */
	private UIGroupPanel	groupPanelCustomer			= new UIGroupPanel();

	/**
	 * groupPanelCustomers.
	 */
	private UIGroupPanel	groupPanelCustomers			= new UIGroupPanel();

	/**
	 * panelActions.
	 */
	private UIPanel			panelActions				= new UIPanel();

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
	 * panelCustomerDetail.
	 */
	private UIPanel			panelCustomerDetail			= new UIPanel();

	/**
	 * Its a customer remote databook.
	 */
	private RemoteDataBook	rdbCustomer					= new RemoteDataBook();

	/**
	 * formLayoutCustomer.
	 */
	private UIFormLayout	formLayoutActions			= new UIFormLayout();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout	formLayoutSearch			= new UIFormLayout();

	/**
	 * formLayoutCustomer.
	 */
	private UIFormLayout	formLayoutCustomer			= new UIFormLayout();

	/**
	 * formLayoutCustomerDetail.
	 */
	private UIFormLayout	formLayoutCustomerDetail	= new UIFormLayout();

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
	 * Constructs a new instance of <code>CustomersWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public CustomersWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
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

		rdbCustomer.getRowDefinition().getColumnDefinition("SURNAME").setLabel("Last name");
		rdbCustomer.getRowDefinition().getColumnDefinition("FIRSTNAME").setLabel("First name");

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

		buttonNewOffer.setText("New Offer");
		buttonNewOffer.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/invoice.png"));
		buttonNewOffer.eventAction().addListener(this, "doNewOffer");
		buttonNewOffer.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonNewOffer.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonNewOffer.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonNewOffer.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonNewOffer.setFont(new UIFont("Arial", UIFont.BOLD, 12));
		buttonNewOffer.setImageTextGap(2);
		buttonNewOffer.setPreferredSize(160, 60);

		buttonShowOffers.setText("Show Offers");
		buttonShowOffers.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/invoice.png"));
		buttonShowOffers.eventAction().addListener(this, "doShowOffers");
		buttonShowOffers.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOffers.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonShowOffers.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOffers.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOffers.setFont(new UIFont("Arial", UIFont.BOLD, 12));
		buttonShowOffers.setImageTextGap(2);
		buttonShowOffers.setPreferredSize(160, 60);

		buttonShowOrders.setText("Show Orders");
		buttonShowOrders.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/paypal.png"));
		buttonShowOrders.eventAction().addListener(this, "doShowOrders");
		buttonShowOrders.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOrders.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonShowOrders.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOrders.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonShowOrders.setFont(new UIFont("Arial", UIFont.BOLD, 12));
		buttonShowOrders.setImageTextGap(2);
		buttonShowOrders.setPreferredSize(160, 60);

		labelSuchen.setText("Suchen");

		labelCustomerNr.setText("Customer Nr");

		labelTitle.setText("Title");

		labelSalutation.setText("Salutation");

		labelFirstname.setText("First name");

		labelSurname.setText("Last name");

		labelEmail.setText("Email");

		labelCompany.setText("Company");

		labelAddress.setText("Address");

		labelZip.setText("Zip");

		labelCity.setText("City");

		labelCustomersince.setText("Customer since");

		groupPanelCustomer.setText("Customers");
		groupPanelCustomer.setLayout(borderLayoutCustomer);
		groupPanelCustomer.add(panelSearch, UIBorderLayout.NORTH);
		groupPanelCustomer.add(tableCustomer, UIBorderLayout.CENTER);

		filterEditor.setDataRow(rdbCustomer);

		borderLayoutFirst.setMargins(new UIInsets(5, 5, 5, 5));
		splitPanelMainFirst.setLayout(borderLayoutFirst);
		splitPanelMainFirst.add(groupPanelCustomer, UIBorderLayout.CENTER);

		panelActions.setLayout(formLayoutActions);
		panelActions.add(buttonNewOffer, formLayoutActions.getConstraints(-3, 0));
		panelActions.add(buttonShowOffers, formLayoutActions.getConstraints(-2, 0));
		panelActions.add(buttonShowOrders, formLayoutActions.getConstraints(-1, 0));

		formLayoutCustomerDetail.setAnchorConfiguration("t10=30");

		formLayoutCustomer.setMargins(new UIInsets(10, 5, 10, 4));

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

		groupPanelCustomers.setText("Customer Detail");
		groupPanelCustomers.setLayout(formLayoutCustomerDetail);
		groupPanelCustomers.add(labelCustomerNr, formLayoutCustomerDetail.getConstraints(0, 0));
		groupPanelCustomers.add(editCustomerCustomerNr, formLayoutCustomerDetail.getConstraints(1, 0, -1, 0));
        groupPanelCustomers.add(labelSalutation, formLayoutCustomerDetail.getConstraints(0, 1));
        groupPanelCustomers.add(editCustomerSalutation, formLayoutCustomerDetail.getConstraints(1, 1));
        groupPanelCustomers.add(labelTitle, formLayoutCustomerDetail.getConstraints(0, 2));
		groupPanelCustomers.add(editCustomerTitle, formLayoutCustomerDetail.getConstraints(1, 2));
		groupPanelCustomers.add(labelFirstname, formLayoutCustomerDetail.getConstraints(0, 3));
		groupPanelCustomers.add(editCustomerFirstname, formLayoutCustomerDetail.getConstraints(1, 3, -1, 3));
		groupPanelCustomers.add(labelSurname, formLayoutCustomerDetail.getConstraints(0, 4));
		groupPanelCustomers.add(editCustomerSurname, formLayoutCustomerDetail.getConstraints(1, 4, -1, 4));
		groupPanelCustomers.add(labelEmail, formLayoutCustomerDetail.getConstraints(0, 5));
		groupPanelCustomers.add(editCustomerEmail, formLayoutCustomerDetail.getConstraints(1, 5, -1, 5));
		groupPanelCustomers.add(labelCompany, formLayoutCustomerDetail.getConstraints(0, 6));
		groupPanelCustomers.add(editCustomerCompany, formLayoutCustomerDetail.getConstraints(1, 6, -1, 6));
		groupPanelCustomers.add(labelAddress, formLayoutCustomerDetail.getConstraints(0, 7));
		groupPanelCustomers.add(editCustomerAddress, formLayoutCustomerDetail.getConstraints(1, 7, -3, 7));
		groupPanelCustomers.add(labelZip, formLayoutCustomerDetail.getConstraints(-2, 7));
		groupPanelCustomers.add(editCustomerZip, formLayoutCustomerDetail.getConstraints(-1, 7));
		groupPanelCustomers.add(labelCity, formLayoutCustomerDetail.getConstraints(0, 8));
		groupPanelCustomers.add(editCustomerCity, formLayoutCustomerDetail.getConstraints(1, 8, -1, 8));
		groupPanelCustomers.add(labelCustomersince, formLayoutCustomerDetail.getConstraints(0, 9));
		groupPanelCustomers.add(editCustomerCustomersince, formLayoutCustomerDetail.getConstraints(1, 9, -1, 9));

		panelCustomerDetail.setLayout(formLayoutCustomer);
		panelCustomerDetail.add(groupPanelCustomers, formLayoutCustomer.getConstraints(0, 0, -1, -1));

		splitPanelMainSecond.setLayout(borderLayoutSecond);
		splitPanelMainSecond.add(panelCustomerDetail, UIBorderLayout.CENTER);
		splitPanelMainSecond.add(panelActions, UIBorderLayout.SOUTH);

		splitPanelMain.setDividerPosition(261);
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

	/**
	 * Opens the offer work screen and creates a new offer.
	 */
	public void doNewOffer(UIActionEvent pEvent) throws Throwable
	{
		OfferWorkScreen offerWorkScreen = (OfferWorkScreen)getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OfferWorkScreen");

		offerWorkScreen.doNewOffer((BigDecimal)rdbCustomer.getValue("ID"));
	}

	/**
	 * Opens the offers work screen and sets the filter for the selected
	 * customer.
	 */
	public void doShowOffers(UIActionEvent pEvent) throws Throwable
	{
		OffersWorkScreen offerWorkScreen = (OffersWorkScreen)getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OffersWorkScreen");

		offerWorkScreen.setFilter(rdbCustomer.getValue("CUSTOMER_NR"));
	}

	/**
	 * Opens the order work screen and sets the filter for the selected
	 * customer.
	 */
	public void doShowOrders(UIActionEvent pEvent) throws Throwable
	{
		OrderWorkScreen orderWorkScreen = (OrderWorkScreen)getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OrderWorkScreen");

		orderWorkScreen.setFilter(rdbCustomer.getValue("CUSTOMER_NR"));
	}

} // CustomersWorkScreen
