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
import java.util.Date;
import java.util.Hashtable;

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIInsets;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.io.IFileHandle;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.apps.util.Var;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * The MobileOrderWorkScreen shows all orders and the screen was optimized for mobile devices.
 */
public class MobileOrderWorkScreen extends DataSourceWorkScreen
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * tabsetPanel.
	 */
	private UITabsetPanel	tabsetPanel						= new UITabsetPanel();

	/**
	 * filterEditor.
	 */
	private FilterEditor	filterEditor					= new FilterEditor();

	/**
	 * editOrder_OrderNr.
	 */
	private UIEditor		editOrder_OrderNr				= new UIEditor();

	/**
	 * editOrder_Deliverydate.
	 */
	private UIEditor		editOrder_Deliverydate			= new UIEditor();

	/**
	 * editOrder_Orderdate.
	 */
	private UIEditor		editOrder_Orderdate				= new UIEditor();

	/**
	 * editOrder_Termsofpayment.
	 */
	private UIEditor		editOrder_Termsofpayment		= new UIEditor();

	/**
	 * editOrder_CustomerNr.
	 */
	private UIEditor		editOrder_CustomerNr			= new UIEditor();

	/**
	 * editOrder_Totalprice.
	 */
	private UIEditor		editOrder_Totalprice			= new UIEditor();

	/**
	 * editOrder_TaxInPercent.
	 */
	private UIEditor		editOrder_TaxInPercent			= new UIEditor();

	/**
	 * editOrder_DiscountInPercent.
	 */
	private UIEditor		editOrder_DiscountInPercent		= new UIEditor();

	/**
	 * editOrder_Grosstotalprice.
	 */
	private UIEditor		editOrder_Grosstotalprice		= new UIEditor();

	/**
	 * editOrder_OfferNr.
	 */
	private UIEditor		editOrder_OfferNr				= new UIEditor();

	/**
	 * editOrder_BillingNr.
	 */
	private UIEditor		editOrder_BillingNr				= new UIEditor();

	/**
	 * editOrder_Billingdate.
	 */
	private UIEditor		editOrder_Billingdate			= new UIEditor();

	/**
	 * editOrder_Billingcompany.
	 */
	private UIEditor		editOrder_Billingcompany		= new UIEditor();

	/**
	 * editOrder_Billingzip.
	 */
	private UIEditor		editOrder_Billingzip			= new UIEditor();

	/**
	 * editOrder_Billingaddress.
	 */
	private UIEditor		editOrder_Billingaddress		= new UIEditor();

	/**
	 * editOrder_Billingcity.
	 */
	private UIEditor		editOrder_Billingcity			= new UIEditor();

	/**
	 * editOrder_Ispaid.
	 */
	private UIEditor		editOrder_Ispaid				= new UIEditor();

	/**
	 * labelOrder_nr.
	 */
	private UILabel			labelOrder_nr					= new UILabel();

	/**
	 * labelOrderdate.
	 */
	private UILabel			labelOrderdate					= new UILabel();

	/**
	 * labelDeliverydate.
	 */
	private UILabel			labelDeliverydate				= new UILabel();

	/**
	 * labelTermsofpayment.
	 */
	private UILabel			labelTermsofpayment				= new UILabel();

	/**
	 * labelTermsofpayment1.
	 */
	private UILabel			labelTermsofpayment1			= new UILabel();

	/**
	 * labelTotalprice.
	 */
	private UILabel			labelTotalprice					= new UILabel();

	/**
	 * labelPercentage1.
	 */
	private UILabel			labelPercentage1				= new UILabel();

	/**
	 * labelCurrency2.
	 */
	private UILabel			labelCurrency2					= new UILabel();

	/**
	 * labelTaxInPercent.
	 */
	private UILabel			labelTaxInPercent				= new UILabel();

	/**
	 * labelDiscountInPercent.
	 */
	private UILabel			labelDiscountInPercent			= new UILabel();

	/**
	 * labelPercentage2.
	 */
	private UILabel			labelPercentage2				= new UILabel();

	/**
	 * labelDiscountInPercent1.
	 */
	private UILabel			labelDiscountInPercent1			= new UILabel();

	/**
	 * labelCurrency1.
	 */
	private UILabel			labelCurrency1					= new UILabel();

	/**
	 * labelOfferNr.
	 */
	private UILabel			labelOfferNr					= new UILabel();

	/**
	 * labelSearch.
	 */
	private UILabel			labelSearch						= new UILabel();

	/**
	 * labelBillingdate.
	 */
	private UILabel			labelBillingdate				= new UILabel();

	/**
	 * labelBillingdate1.
	 */
	private UILabel			labelBillingdate1				= new UILabel();

	/**
	 * labelBillingcompany.
	 */
	private UILabel			labelBillingcompany				= new UILabel();

	/**
	 * labelBillingzip.
	 */
	private UILabel			labelBillingzip					= new UILabel();

	/**
	 * labelBillingcompany1.
	 */
	private UILabel			labelBillingcompany1			= new UILabel();

	/**
	 * labelBillingcity.
	 */
	private UILabel			labelBillingcity				= new UILabel();

	/**
	 * labelIspaid.
	 */
	private UILabel			labelIspaid						= new UILabel();

	/**
	 * formLayoutOrder.
	 */
	private UIFormLayout	formLayoutOrder					= new UIFormLayout();

	/**
	 * formLayoutArticles.
	 */
	private UIFormLayout	formLayoutArticles				= new UIFormLayout();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout	formLayoutSearch				= new UIFormLayout();

	/**
	 * formLayoutBillingInner.
	 */
	private UIFormLayout	formLayoutBillingInner			= new UIFormLayout();

	/**
	 * formLayoutPrices.
	 */
	private UIFormLayout	formLayoutPrices				= new UIFormLayout();

	/**
	 * panelOrder.
	 */
	private UIPanel			panelOrder						= new UIPanel();

	/**
	 * groupPanelArticles.
	 */
	private UIGroupPanel	groupPanelArticles				= new UIGroupPanel();

	/**
	 * tableOrder_.
	 */
	private NavigationTable	tableOrder_						= new NavigationTable();

	/**
	 * tableOfferarticle.
	 */
	private NavigationTable	tableOfferarticle				= new NavigationTable();

	/**
	 * Its a order_ remote databook.
	 */
	private RemoteDataBook	rdbOrder_						= new RemoteDataBook();

	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle					= new RemoteDataBook();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout	borderLayoutMain				= new UIBorderLayout();

	/**
	 * borderLayoutLeft.
	 */
	private UIBorderLayout	borderLayoutLeft				= new UIBorderLayout();

	/**
	 * borderLayoutRight.
	 */
	private UIBorderLayout	borderLayoutRight				= new UIBorderLayout();

	/**
	 * borderLayoutOrderInformation.
	 */
	private UIBorderLayout	borderLayoutOrderInformation	= new UIBorderLayout();

	/**
	 * borderLayoutBillingInformation.
	 */
	private UIBorderLayout	borderLayoutBillingInformation	= new UIBorderLayout();

	/**
	 * Panel Right inner.
	 */
	private UIPanel			panelRightInner					= new UIPanel();

	/**
	 * Panel BorderLayoutRightInner.
	 */
	private UIBorderLayout	borderLayoutRightInner			= new UIBorderLayout();

	/**
	 * splitPanelMain.
	 */
	private UISplitPanel	splitPanelMain					= new UISplitPanel();

	/**
	 * splitPanelMainFirst.
	 */
	private UIPanel			splitPanelMainFirst				= new UIPanel();

	/**
	 * splitPanelMainSecond.
	 */
	private UIPanel			splitPanelMainSecond			= new UIPanel();

	/**
	 * panelSearch.
	 */
	private UIPanel			panelSearch						= new UIPanel();

	/**
	 * panelOrderInformation.
	 */
	private UIPanel			panelOrderInformation			= new UIPanel();

	/**
	 * panelBillingInformation.
	 */
	private UIPanel			panelBillingInformation			= new UIPanel();

	/**
	 * panelBillingInner.
	 */
	private UIPanel			panelBillingInner				= new UIPanel();

	/**
	 * panelPrices.
	 */
	private UIPanel			panelPrices						= new UIPanel();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>OrderWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public MobileOrderWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();
	}

	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		rdbOrder_.setName("order_");
		rdbOrder_.setDataSource(getDataSource());
		rdbOrder_.setInsertEnabled(false);
		rdbOrder_.open();

		rdbOrder_.getRowDefinition().getColumnDefinition("ORDER_NR").setLabel("order_nr");
		rdbOrder_.getRowDefinition().getColumnDefinition("ORDER_NR").setWidth(128);
		rdbOrder_.getRowDefinition().getColumnDefinition("ORDERDATE").setLabel("orderdate");
		rdbOrder_.getRowDefinition().getColumnDefinition("ORDERDATE").setWidth(149);

		rdbOrder_.getRowDefinition().getColumnDefinition("ORDERDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setDataSource(getDataSource());
		rdbOfferarticle.setMasterReference(new ReferenceDefinition(new String[] { "OFFER_ID" }, rdbOrder_, new String[] { "OFFER_ID" }));
		rdbOfferarticle.setUpdateEnabled(false);
		rdbOfferarticle.setInsertEnabled(false);
		rdbOfferarticle.setDeleteEnabled(false);
		rdbOfferarticle.open();

		rdbOfferarticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOrder_.getRowDefinition().getColumnDefinition("DELIVERYDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOrder_.getRowDefinition().getColumnDefinition("TOTALPRICE").setWidth(93);
		rdbOrder_.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").setWidth(122);

		rdbOrder_.getRowDefinition().getColumnDefinition("ISPAID").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbOrder_.getRowDefinition().getColumnDefinition("BILLINGDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOrder_.getRowDefinition().getColumnDefinition("OFFER_NR").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("TOTALPRICE").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("TAX_IN_PERCENT").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("DISCOUNT_IN_PERCENT").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").setReadOnly(true);

	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		tableOrder_.setMaximumSize(new UIDimension(450, 350));
		tableOrder_.setDataBook(rdbOrder_);
		tableOrder_.setAutoResize(false);

		tableOfferarticle.setToolBarVisible(false);
		tableOfferarticle.setMaximumSize(new UIDimension(450, 350));
		tableOfferarticle.setEditable(false);
		tableOfferarticle.setDataBook(rdbOfferarticle);
		tableOfferarticle.setAutoResize(false);
		tableOfferarticle.setShowSelection(false);
		tableOfferarticle.setExportVisible(false);

		formLayoutArticles.setMargins(new UIInsets(11, 10, 10, 10));

		formLayoutOrder.setAnchorConfiguration("r1=173,t3=6,b3=21,t5=7,b5=177");

		groupPanelArticles.setText("Articles");
		groupPanelArticles.setLayout(formLayoutArticles);
		groupPanelArticles.add(tableOfferarticle, formLayoutArticles.getConstraints(0, 0, -1, -1));

		panelPrices.setLayout(formLayoutPrices);
        panelPrices.add(labelTotalprice, formLayoutPrices.getConstraints(-3, 0));
        panelPrices.add(editOrder_Totalprice, formLayoutPrices.getConstraints(-2, 0));
        panelPrices.add(labelCurrency2, formLayoutPrices.getConstraints(-1, 0));
        panelPrices.add(labelTaxInPercent, formLayoutPrices.getConstraints(-3, 1));
        panelPrices.add(editOrder_TaxInPercent, formLayoutPrices.getConstraints(-2, 1));
        panelPrices.add(labelPercentage1, formLayoutPrices.getConstraints(-1, 1));
        panelPrices.add(labelDiscountInPercent, formLayoutPrices.getConstraints(-3, 2));
        panelPrices.add(editOrder_DiscountInPercent, formLayoutPrices.getConstraints(-2, 2));
        panelPrices.add(labelPercentage2, formLayoutPrices.getConstraints(-1, 2));
        panelPrices.add(labelDiscountInPercent1, formLayoutPrices.getConstraints(-3, 3));
		panelPrices.add(editOrder_Grosstotalprice, formLayoutPrices.getConstraints(-2, 3));
		panelPrices.add(labelCurrency1, formLayoutPrices.getConstraints(-1, 3));

		labelOrder_nr.setText("Order Nr");

		labelOrderdate.setText("Orderdate");

		labelDeliverydate.setText("Deliverydate");

		labelTermsofpayment1.setText("Customer Nr");

		labelOfferNr.setText("Offer Nr");

		labelSearch.setText("Search Offer");

		editOrder_OrderNr.setDataRow(rdbOrder_);
		editOrder_OrderNr.setColumnName("ORDER_NR");

		editOrder_Deliverydate.setDataRow(rdbOrder_);
		editOrder_Deliverydate.setColumnName("DELIVERYDATE");

		editOrder_Orderdate.setDataRow(rdbOrder_);
		editOrder_Orderdate.setColumnName("ORDERDATE");

		editOrder_CustomerNr.setDataRow(rdbOrder_);
		editOrder_CustomerNr.setColumnName("CUSTOMER_NR");

		editOrder_OfferNr.setDataRow(rdbOrder_);
		editOrder_OfferNr.setColumnName("OFFER_NR");

		splitPanelMainFirst.setLayout(borderLayoutLeft);
		splitPanelMainFirst.add(panelSearch, UIBorderLayout.NORTH);
		borderLayoutLeft.setMargins(10, 10, 10, 10);

		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSearch, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(filterEditor, formLayoutSearch.getConstraints(1, 0, -1, 0));

		splitPanelMainFirst.add(tableOrder_, UIBorderLayout.CENTER);

		borderLayoutRightInner.setMargins(10, 10, 10, 10);
		panelRightInner.setLayout(borderLayoutRightInner);
		panelRightInner.add(tabsetPanel, UIBorderLayout.CENTER);

		splitPanelMainSecond.setLayout(borderLayoutRight);
		splitPanelMainSecond.add(panelRightInner, UIBorderLayout.CENTER);

		panelOrder.setLayout(formLayoutOrder);
		panelOrder.add(labelOrder_nr, formLayoutOrder.getConstraints(0, 0));
		panelOrder.add(editOrder_OrderNr, formLayoutOrder.getConstraints(1, 0));
		panelOrder.add(labelOfferNr, formLayoutOrder.getConstraints(0, 1));
		panelOrder.add(editOrder_OfferNr, formLayoutOrder.getConstraints(1, 1));
		panelOrder.add(labelTermsofpayment1, formLayoutOrder.getConstraints(0, 2));
		panelOrder.add(editOrder_CustomerNr, formLayoutOrder.getConstraints(1, 2));
		panelOrder.add(labelOrderdate, formLayoutOrder.getConstraints(0, 3));
		panelOrder.add(editOrder_Orderdate, formLayoutOrder.getConstraints(1, 3));
		panelOrder.add(labelDeliverydate, formLayoutOrder.getConstraints(0, 4));
		panelOrder.add(editOrder_Deliverydate, formLayoutOrder.getConstraints(1, 4));
		panelOrder.add(groupPanelArticles, formLayoutOrder.getConstraints(0, 5, -1, 5));
		panelOrder.add(panelPrices, formLayoutOrder.getConstraints(0, 6, -1, 6));

		panelOrderInformation.setLayout(borderLayoutOrderInformation);
		panelOrderInformation.add(panelOrder, UIBorderLayout.CENTER);

		panelBillingInner.setLayout(formLayoutBillingInner);
		panelBillingInner.add(labelBillingdate, formLayoutBillingInner.getConstraints(0, 0));
        panelBillingInner.add(editOrder_BillingNr, formLayoutBillingInner.getConstraints(1, 0));
		panelBillingInner.add(labelBillingdate1, formLayoutBillingInner.getConstraints(0, 1));
		panelBillingInner.add(editOrder_Billingdate, formLayoutBillingInner.getConstraints(1, 1));
		panelBillingInner.add(labelBillingcompany, formLayoutBillingInner.getConstraints(0, 2));
        panelBillingInner.add(editOrder_Billingcompany, formLayoutBillingInner.getConstraints(1, 2, -1, 2));
		panelBillingInner.add(labelBillingcompany1, formLayoutBillingInner.getConstraints(0, 3));
		panelBillingInner.add(editOrder_Billingaddress, formLayoutBillingInner.getConstraints(1, 3, -1, 3));
		panelBillingInner.add(labelBillingzip, formLayoutBillingInner.getConstraints(0, 4));
		panelBillingInner.add(editOrder_Billingzip, formLayoutBillingInner.getConstraints(1, 4));
        panelBillingInner.add(labelBillingcity, formLayoutBillingInner.getConstraints(2, 4));
        panelBillingInner.add(editOrder_Billingcity, formLayoutBillingInner.getConstraints(3, 4, -1, 4));
		panelBillingInner.add(labelTermsofpayment, formLayoutBillingInner.getConstraints(0, 5));
		panelBillingInner.add(editOrder_Termsofpayment, formLayoutBillingInner.getConstraints(1, 5, -1, 5));
		panelBillingInner.add(labelIspaid, formLayoutBillingInner.getConstraints(0, 6));
		panelBillingInner.add(editOrder_Ispaid, formLayoutBillingInner.getConstraints(1, 6));

		panelBillingInformation.setLayout(borderLayoutBillingInformation);
		panelBillingInformation.add(panelBillingInner, UIBorderLayout.CENTER);

		tabsetPanel.setPreferredSize(new UIDimension(58, 642));
		tabsetPanel.add(panelOrderInformation, "Order Information");
		tabsetPanel.add(panelBillingInformation, "Billing Information");

		filterEditor.setDataRow(rdbOrder_);

		formLayoutPrices.setAnchorConfiguration("l-2=-139");

		formLayoutBillingInner.setAnchorConfiguration("r1=167");

		labelBillingdate.setText("Billing Nr");

		labelBillingdate1.setText("Billingdate");

		labelBillingcompany.setText("Company");

		labelBillingzip.setText("ZIP");

		labelBillingcompany1.setText("Address");

		labelBillingcity.setText("Billingcity");

		labelIspaid.setText("Ispaid");

		labelTermsofpayment.setText("Terms of payment");

		labelCurrency2.setText("Euro");

		labelTotalprice.setText("Totalprice");

		labelPercentage1.setText("(%)");

		labelTaxInPercent.setText("Tax In Percent");

		labelPercentage2.setText("(%)");

		labelDiscountInPercent.setText("Discount In Percent");

		labelCurrency1.setText("Euro");

		labelDiscountInPercent1.setText("Grosstotalprice");

		editOrder_BillingNr.setDataRow(rdbOrder_);
		editOrder_BillingNr.setColumnName("BILLING_NR");

		editOrder_Billingdate.setDataRow(rdbOrder_);
		editOrder_Billingdate.setColumnName("BILLINGDATE");

		editOrder_Billingcompany.setDataRow(rdbOrder_);
		editOrder_Billingcompany.setColumnName("BILLINGCOMPANY");

		editOrder_Billingzip.setDataRow(rdbOrder_);
		editOrder_Billingzip.setColumnName("BILLINGZIP");

		editOrder_Billingaddress.setDataRow(rdbOrder_);
		editOrder_Billingaddress.setColumnName("BILLINGADDRESS");

		editOrder_Billingcity.setDataRow(rdbOrder_);
		editOrder_Billingcity.setColumnName("BILLINGCITY");

		editOrder_Ispaid.setDataRow(rdbOrder_);
		editOrder_Ispaid.setColumnName("ISPAID");

		editOrder_Termsofpayment.setDataRow(rdbOrder_);
		editOrder_Termsofpayment.setColumnName("TERMSOFPAYMENT");

		editOrder_Totalprice.setDataRow(rdbOrder_);
		editOrder_Totalprice.setColumnName("TOTALPRICE");

		editOrder_TaxInPercent.setDataRow(rdbOrder_);
		editOrder_TaxInPercent.setColumnName("TAX_IN_PERCENT");

		editOrder_DiscountInPercent.setDataRow(rdbOrder_);
		editOrder_DiscountInPercent.setColumnName("DISCOUNT_IN_PERCENT");

		editOrder_Grosstotalprice.setDataRow(rdbOrder_);
		editOrder_Grosstotalprice.setColumnName("GROSSTOTALPRICE");

		splitPanelMain.setDividerPosition(533);

		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);

		setLayout(borderLayoutMain);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Do edit offer.
	 * 
	 * @param pEvent action event.
	 * @throws Throwable
	 */
	public void doEditOffer(UIActionEvent pEvent) throws Throwable
	{
		Hashtable<String, Object> htParams = new Hashtable<String, Object>();
		htParams.put("ID", rdbOrder_.getValue("OFFER_ID"));
		
		getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OfferWorkScreen", htParams);
	}

	/**
	 * Selects an order by index.
	 * 
	 * @param pIndex the row index
	 * @throws Throwable if selecting order fails
	 */
	public void setRowIndex(int pIndex) throws Throwable
	{
		rdbOrder_.setSelectedRow(pIndex);
	}

	/**
	 * Inserts an order.
	 * 
	 * @param customerId the customer id
	 * @param offerId the offer id
	 * @throws Throwable if insert fails
	 */
	public void doInsertOrder(Object customerId, Object offerId) throws Throwable
	{
		rdbOrder_.setInsertEnabled(true);

		rdbOrder_.insert(false);
		rdbOrder_.setValue("ORDERDATE", new Date(System.currentTimeMillis()));
		rdbOrder_.setValue("OFFER_ID", offerId);
		rdbOrder_.setValue("CUSTOMER_ID", customerId);

		rdbOrder_.saveSelectedRow();

		doCreateBillingNr();

		rdbOrder_.setValue("BILLINGCOMPANY", rdbOrder_.getValue("COMPANY"));
		rdbOrder_.setValue("BILLINGADDRESS", rdbOrder_.getValue("ADDRESS"));
		rdbOrder_.setValue("BILLINGZIP", rdbOrder_.getValue("ZIP"));
		rdbOrder_.setValue("BILLINGCITY", rdbOrder_.getValue("CITY"));

		rdbOrder_.setInsertEnabled(false);

		setFilter(rdbOrder_.getValue("OFFER_NR"));
		editOrder_OrderNr.requestFocus();
	}

	/**
	 * Sets the filter for customer nr.
	 *  
	 * @param pCustomerNr the customer nr
	 * @throws Throwable if filtering fails
	 */
	public void setFilter(Object pCustomerNr) throws Throwable
	{
		filterEditor.setValue(pCustomerNr);
		rdbOrder_.reload();
		rdbOfferarticle.reload();
	}

	/**
	 * Save.
	 */
	public void doSave(UIActionEvent pEvent) throws Throwable
	{
		save();
	}

	/**
	 * Creates a new billing nr.
	 * 
	 * @throws Throwable if creation fails
	 */
	public void doCreateBillingNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbOrder_.getValue("ID");

		String billingNr = "BNR - " + id;

		rdbOrder_.setValue("BILLING_NR", billingNr);
	}

	/**
	 * Upload Order Document.
	 */
	public void doUploadOrderDocument(UIActionEvent pEvent) throws Throwable
	{
		ProjXUtil.uploadFile(this, new Var(rdbOrder_, "ORDERDOCUMENTNAME"), new Var(rdbOrder_, "ORDERDOCUMENT"));
	}

	/**
	 * Download Order Document.
	 */
	public void doDownloadOrderDocument(UIActionEvent pEvent) throws Throwable
	{
		ProjXUtil.showFile(this, (String)rdbOrder_.getValue("ORDERDOCUMENTNAME"), rdbOrder_.getValue("ORDERDOCUMENT"));
	}

	/**
	 * Generate Offer Report.
	 */
	public void doGenerateBill(UIActionEvent pEvent) throws Throwable
	{
		if (rdbOrder_.getValue("BILLINGDATE") == null)
		{
			rdbOrder_.setValue("BILLINGDATE", new Date(System.currentTimeMillis()));
		}
		if (rdbOrder_.getValue("BILLING_NR") == null)
		{
			doCreateBillingNr();
		}

		getApplication().getLauncher().showFileHandle(getBillReport());
	}

	/**
	 * Erstellt einen Master Detail Report Order_ Offerarticle.
	 */
	public IFileHandle getBillReport() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createBillReport", rdbOrder_.getFilter(), rdbOrder_.getSort(), rdbOfferarticle.getFilter(), rdbOfferarticle.getSort(),
				rdbOrder_.getValue("ID"));
	}

} // MobileOrderWorkScreen
