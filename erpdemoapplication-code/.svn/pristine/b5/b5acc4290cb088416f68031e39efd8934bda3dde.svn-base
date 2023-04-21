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
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UIScrollPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnView;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.apps.util.Var;
import com.sibvisions.apps.vaadin.web.IExpandableView;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import java.lang.String;

/**
 * The OrderWorkScreen shows all orders and the details to the orders.
 */
public class OrderWorkScreen extends DataSourceWorkScreen implements IExpandableView
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * tabsetPanel.
	 */
	private UITabsetPanel		tabsetPanel						= new UITabsetPanel();

	/**
	 * filterEditor.
	 */
	private FilterEditor		filterEditor					= new FilterEditor();

	/**
	 * editOrder_OrderNr.
	 */
	private UIEditor			editOrder_OrderNr				= new UIEditor();

	/**
	 * editOrder_Deliverydate.
	 */
	private UIEditor			editOrder_Deliverydate			= new UIEditor();

	/**
	 * editOrder_Orderdate.
	 */
	private UIEditor			editOrder_Orderdate				= new UIEditor();

	/**
	 * editOrder_Termsofpayment.
	 */
	private UIEditor			editOrder_Termsofpayment		= new UIEditor();

	/**
	 * editOrder_CustomerNr.
	 */
	private UIEditor			editOrder_CustomerNr			= new UIEditor();

	/**
	 * editOrder_Totalprice.
	 */
	private UIEditor			editOrder_Totalprice			= new UIEditor();

	/**
	 * editOrder_TaxInPercent.
	 */
	private UIEditor			editOrder_TaxInPercent			= new UIEditor();

	/**
	 * editOrder_DiscountInPercent.
	 */
	private UIEditor			editOrder_DiscountInPercent		= new UIEditor();

	/**
	 * editOrder_Grosstotalprice.
	 */
	private UIEditor			editOrder_Grosstotalprice		= new UIEditor();

	/**
	 * editOrder_OfferNr.
	 */
	private UIEditor			editOrder_OfferNr				= new UIEditor();

	/**
	 * editOrder_Orderdocumentname.
	 */
	private UIEditor			editOrder_Orderdocumentname		= new UIEditor();

	/**
	 * editOrder_BillingNr.
	 */
	private UIEditor			editOrder_BillingNr				= new UIEditor();

	/**
	 * editOrder_Billingdate.
	 */
	private UIEditor			editOrder_Billingdate			= new UIEditor();

	/**
	 * editOrder_Billingcompany.
	 */
	private UIEditor			editOrder_Billingcompany		= new UIEditor();

	/**
	 * editOrder_Billingzip.
	 */
	private UIEditor			editOrder_Billingzip			= new UIEditor();

	/**
	 * editOrder_Billingaddress.
	 */
	private UIEditor			editOrder_Billingaddress		= new UIEditor();

	/**
	 * editOrder_Billingcity.
	 */
	private UIEditor			editOrder_Billingcity			= new UIEditor();

	/**
	 * editOrder_Ispaid.
	 */
	private UIEditor			editOrder_Ispaid				= new UIEditor();

	/**
	 * labelOrder_nr.
	 */
	private UILabel				labelOrder_nr					= new UILabel();

	/**
	 * labelOrderdate.
	 */
	private UILabel				labelOrderdate					= new UILabel();

	/**
	 * labelDeliverydate.
	 */
	private UILabel				labelDeliverydate				= new UILabel();

	/**
	 * labelTermsofpayment.
	 */
	private UILabel				labelTermsofpayment				= new UILabel();

	/**
	 * labelTermsofpayment1.
	 */
	private UILabel				labelTermsofpayment1			= new UILabel();

	/**
	 * labelTotalprice.
	 */
	private UILabel				labelTotalprice					= new UILabel();

	/**
	 * labelPercentage1.
	 */
	private UILabel				labelPercentage1				= new UILabel();

	/**
	 * labelCurrency2.
	 */
	private UILabel				labelCurrency2					= new UILabel();

	/**
	 * labelTaxInPercent.
	 */
	private UILabel				labelTaxInPercent				= new UILabel();

	/**
	 * labelDiscountInPercent.
	 */
	private UILabel				labelDiscountInPercent			= new UILabel();

	/**
	 * labelPercentage2.
	 */
	private UILabel				labelPercentage2				= new UILabel();

	/**
	 * labelGrosstotalprice.
	 */
	private UILabel				labelGrosstotalprice			= new UILabel();

	/**
	 * labelCurrency1.
	 */
	private UILabel				labelCurrency1					= new UILabel();

	/**
	 * labelOfferNr.
	 */
	private UILabel				labelOfferNr					= new UILabel();

	/**
	 * labelSearch.
	 */
	private UILabel				labelSearch						= new UILabel();

	/**
	 * labelOrderdocumentname.
	 */
	private UILabel				labelOrderdocumentname			= new UILabel();

	/**
	 * labelBillingdate.
	 */
	private UILabel				labelBillingdate				= new UILabel();

	/**
	 * labelBillingdate1.
	 */
	private UILabel				labelBillingdate1				= new UILabel();

	/**
	 * labelBillingcompany.
	 */
	private UILabel				labelBillingcompany				= new UILabel();

	/**
	 * labelBillingzip.
	 */
	private UILabel				labelBillingzip					= new UILabel();

	/**
	 * labelBillingcompany1.
	 */
	private UILabel				labelBillingcompany1			= new UILabel();

	/**
	 * labelBillingcity.
	 */
	private UILabel				labelBillingcity				= new UILabel();

	/**
	 * labelIspaid.
	 */
	private UILabel				labelIspaid						= new UILabel();

	/**
	 * buttonSave.
	 */
	private UIButton			buttonSave						= new UIButton();

	/**
	 * buttonGenerateReport.
	 */
	private UIButton			buttonGenerateReport			= new UIButton();

	/**
	 * buttonEditOffer.
	 */
	private UIButton			buttonEditOffer					= new UIButton();

	/**
	 * buttonUpload.
	 */
	private UIButton			buttonUpload					= new UIButton();

	/**
	 * buttonDownload.
	 */
	private UIButton			buttonDownload					= new UIButton();

	/**
	 * panelActions.
	 */
	private UIPanel				panelActions					= new UIPanel();

	/**
	 * formLayoutActions.
	 */
	private UIFormLayout		formLayoutActions				= new UIFormLayout();

	/**
	 * formLayoutOrder.
	 */
	private UIFormLayout		formLayoutOrder					= new UIFormLayout();

	/**
	 * formLayoutArticles.
	 */
	private UIFormLayout		formLayoutArticles				= new UIFormLayout();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout		formLayoutSearch				= new UIFormLayout();

	/**
	 * formLayoutBillingInner.
	 */
	private UIFormLayout		formLayoutBillingInner			= new UIFormLayout();

	/**
	 * formLayoutPrices.
	 */
	private UIFormLayout		formLayoutPrices				= new UIFormLayout();

	/**
	 * panelOrder.
	 */
	private UIScrollPanel		panelOrder						= new UIScrollPanel();

	/**
	 * groupPanelArticles.
	 */
	private UIGroupPanel		groupPanelArticles				= new UIGroupPanel();

	/**
	 * tableOrder_.
	 */
	private NavigationTable		tableOrder_						= new NavigationTable();

	/**
	 * tableOfferarticle.
	 */
	private NavigationTable		tableOfferarticle				= new NavigationTable();

	/**
	 * Its a order_ remote databook.
	 */
	private RemoteDataBook		rdbOrder_						= new RemoteDataBook();

	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook		rdbOfferarticle					= new RemoteDataBook();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout		borderLayoutMain				= new UIBorderLayout();

	/**
	 * borderLayoutLeft.
	 */
	private UIBorderLayout		borderLayoutLeft				= new UIBorderLayout();

	/**
	 * borderLayoutRight.
	 */
	private UIFormLayout		formLayoutRight					= new UIFormLayout();

	/**
	 * borderLayoutOrderInformation.
	 */
	private UIBorderLayout		borderLayoutOrderInformation	= new UIBorderLayout();

	/**
	 * borderLayoutBillingInformation.
	 */
	private UIBorderLayout		borderLayoutBillingInformation	= new UIBorderLayout();

	/**
	 * Panel Right inner.
	 */
	private UIPanel				panelRightInner					= new UIPanel();

	/**
	 * Panel BorderLayoutRightInner.
	 */
	private UIBorderLayout		borderLayoutRightInner			= new UIBorderLayout();

	/**
	 * borderLayout1.
	 */
	private UIBorderLayout		borderLayout1					= new UIBorderLayout();

	/**
	 * splitPanelMain.
	 */
	private UISplitPanel		splitPanelMain					= new UISplitPanel();

	/**
	 * splitPanelMainFirst.
	 */
	private UIPanel				splitPanelMainFirst				= new UIPanel();

	/**
	 * splitPanelMainSecond.
	 */
	private UIPanel				splitPanelMainSecond			= new UIPanel();

	/**
	 * panelSearch.
	 */
	private UIPanel				panelSearch						= new UIPanel();

	/**
	 * panelOrderInformation.
	 */
	private UIPanel				panelOrderInformation			= new UIPanel();

	/**
	 * panelBillingInformation.
	 */
	private UIPanel				panelBillingInformation			= new UIPanel();

	/**
	 * panelBillingInner.
	 */
	private UIPanel				panelBillingInner				= new UIPanel();

	/**
	 * panelPrices.
	 */
	private UIPanel				panelPrices						= new UIPanel();

	/**
	 * The current view state.
	 */
	private ExpandableViewState	evsCurrent						= ExpandableViewState.Default;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>OrderWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public OrderWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		rdbOrder_.eventAfterRowSelected().addListener(this, "doConfigureButtonsAndEditors");
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

		rdbOrder_.getRowDefinition().getColumnDefinition("ORDER_NR").setWidth(128);

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

		rdbOrder_.getRowDefinition().getColumnDefinition("ISPAID").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbOrder_.getRowDefinition().getColumnDefinition("BILLINGDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOrder_.getRowDefinition().getColumnDefinition("OFFER_NR").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("TOTALPRICE").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("TAX_IN_PERCENT").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("DISCOUNT_IN_PERCENT").setReadOnly(true);
		rdbOrder_.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").setReadOnly(true);

		rdbOrder_.getRowDefinition().setColumnView(ITableControl.class, new ColumnView(new String[] { "ORDER_NR", "CUSTOMER_NR", "ISPAID" }));

		ProjXUtil.addAskDeleteDialog(rdbOrder_);

		rdbOrder_.getRowDefinition().getColumnDefinition("ISPAID").setLabel("Is paid");

		rdbOfferarticle.getRowDefinition().getColumnDefinition("NUMBEROF").setLabel("Number of");
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

		buttonSave.setText("Save");
		buttonSave.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/diskette.png"));
		buttonSave.eventAction().addListener(this, "doSave");
		buttonSave.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonSave.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonSave.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonSave.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonSave.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonSave.setImageTextGap(2);
		buttonSave.setPreferredSize(160, 60);

		buttonEditOffer.setText("Show Offer");
		buttonEditOffer.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/export_database2.png"));
		buttonEditOffer.eventAction().addListener(this, "doEditOffer");
		buttonEditOffer.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonEditOffer.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonEditOffer.setImageTextGap(2);
		buttonEditOffer.setPreferredSize(160, 60);

		buttonGenerateReport.setText("Create Bill");
		buttonGenerateReport.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/invoice.png"));
		buttonGenerateReport.eventAction().addListener(this, "doGenerateBill");
		buttonGenerateReport.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonGenerateReport.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonGenerateReport.setImageTextGap(2);
		buttonGenerateReport.setPreferredSize(160, 60);

		formLayoutArticles.setMargins(new UIInsets(10, 10, 10, 10));

		groupPanelArticles.setText("Articles");
		groupPanelArticles.setLayout(formLayoutArticles);
		groupPanelArticles.add(tableOfferarticle, formLayoutArticles.getConstraints(0, 0, -1, -1));

		panelPrices.setLayout(formLayoutPrices);

		panelPrices.add(labelTotalprice, formLayoutPrices.getConstraints(-3, 0));
		panelPrices.add(editOrder_Totalprice, formLayoutPrices.getConstraints(-2, 0));
		panelPrices.add(labelCurrency2, formLayoutPrices.getConstraints(-1, 0));

		panelPrices.add(labelDiscountInPercent, formLayoutPrices.getConstraints(-3, 1));
		panelPrices.add(editOrder_DiscountInPercent, formLayoutPrices.getConstraints(-2, 1));
		panelPrices.add(labelPercentage2, formLayoutPrices.getConstraints(-1, 1));

		panelPrices.add(labelTaxInPercent, formLayoutPrices.getConstraints(-3, 2));
		panelPrices.add(editOrder_TaxInPercent, formLayoutPrices.getConstraints(-2, 2));
		panelPrices.add(labelPercentage1, formLayoutPrices.getConstraints(-1, 2));

		panelPrices.add(labelGrosstotalprice, formLayoutPrices.getConstraints(-3, 3));
		panelPrices.add(editOrder_Grosstotalprice, formLayoutPrices.getConstraints(-2, 3));
		panelPrices.add(labelCurrency1, formLayoutPrices.getConstraints(-1, 3));

		labelOrder_nr.setText("Order Nr");

		labelOrderdate.setText("Order date");

		labelDeliverydate.setText("Delivery date");

		labelTermsofpayment1.setText("Customer Nr");

		labelOfferNr.setText("Offer Nr");

		labelSearch.setText("Search Order");

		labelOrderdocumentname.setText("Order document");

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

		editOrder_Orderdocumentname.setDataRow(rdbOrder_);
		editOrder_Orderdocumentname.setColumnName("ORDERDOCUMENTNAME");

		splitPanelMainFirst.setLayout(borderLayoutLeft);
		splitPanelMainFirst.add(panelSearch, UIBorderLayout.NORTH);
		borderLayoutLeft.setMargins(10, 10, 10, 10);

		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSearch, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(filterEditor, formLayoutSearch.getConstraints(1, 0, -1, 0));

		splitPanelMainFirst.add(tableOrder_, UIBorderLayout.CENTER);

		borderLayoutRightInner.setMargins(10, 10, 10, 10);
		panelRightInner = new UIPanel(borderLayoutRightInner);
		panelRightInner.setLayout(borderLayout1);
		panelRightInner.add(tabsetPanel, UIBorderLayout.CENTER);

		splitPanelMainSecond.setLayout(formLayoutRight);
		splitPanelMainSecond.add(panelRightInner, formLayoutRight.getConstraints(0, 0, -1, -2));
		splitPanelMainSecond.add(panelActions, formLayoutRight.getConstraints(0, -1, -1, -1));

		formLayoutActions.setMargins(10, 0, 0, 0);
		panelActions.setLayout(formLayoutActions);
		panelActions.add(buttonSave, formLayoutActions.getConstraints(-3, 0));
		panelActions.add(buttonGenerateReport, formLayoutActions.getConstraints(-2, 0));
		panelActions.add(buttonEditOffer, formLayoutActions.getConstraints(-1, 0));

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
        panelOrder.add(labelOrderdocumentname, formLayoutOrder.getConstraints(0, 5));
		panelOrder.add(editOrder_Orderdocumentname, formLayoutOrder.getConstraints(1, 5, -1, 5));
		panelOrder.add(buttonDownload, formLayoutOrder.getConstraints(1, 6));
		panelOrder.add(buttonUpload, formLayoutOrder.getConstraints(2, 6));
		panelOrder.add(groupPanelArticles, formLayoutOrder.getConstraints(0, 7, -1, 7));
		panelOrder.add(panelPrices, formLayoutOrder.getConstraints(0, 8, -1, 8));

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

		buttonUpload.setText("Upload order document");
		buttonUpload.eventAction().addListener(this, "doUploadOrderDocument");
		buttonUpload.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/import_database2.png"));

		buttonDownload.setText("Show order document");
		buttonDownload.eventAction().addListener(this, "doDownloadOrderDocument");
		buttonDownload.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/export_database2.png"));

		borderLayout1.setMargins(new UIInsets(0, 0, 0, 0));

		formLayoutPrices.setAnchorConfiguration("l-2=-160");

		formLayoutBillingInner.setAnchorConfiguration("r1=160");

		formLayoutOrder.setAnchorConfiguration("r1=180,t6=12,t7=12,b7=205");

		formLayoutSearch.setMargins(new UIInsets(0, 0, 10, 0));

		labelBillingdate.setText("Billing Nr");

		labelBillingdate1.setText("Billing date");

		labelBillingcompany.setText("Company");

		labelBillingzip.setText("Zip");

		labelBillingcompany1.setText("Address");

		labelBillingcity.setText("City");

		labelIspaid.setText("Is paid");

		labelTermsofpayment.setText("Terms of payment");

		labelCurrency2.setText("Euro");

		labelTotalprice.setText("Total price");

		labelPercentage1.setText("(%)");

		labelTaxInPercent.setText("Tax");

		labelPercentage2.setText("(%)");

		labelDiscountInPercent.setText("Discount");

		labelCurrency1.setText("Euro");

		labelGrosstotalprice.setText("Gross total price");

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

		splitPanelMain.setDividerPosition(353);

		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);

		setLayout(borderLayoutMain);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setViewState(ExpandableViewState pViewState)
	{
		evsCurrent = pViewState;
	}

	/**
	 * {@inheritDoc}
	 */
	public ExpandableViewState getViewState()
	{
		return evsCurrent;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens the offer work screen for the selected order.
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
	 * Inserts an order.
	 * 
	 * @param customerId the customer id.
	 * @param offerId the offer id.
	 * @throws Throwable
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
	 * Sets the value to the filter editor.
	 * 
	 * @param pCustomerNr
	 * @throws Throwable
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
	 * Creates a billing nr.
	 * 
	 * @throws Throwable
	 */
	public void doCreateBillingNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbOrder_.getValue("ID");

		String billingNr = "BNR - " + id;

		rdbOrder_.setValue("BILLING_NR", billingNr);

		rdbOrder_.saveSelectedRow();
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
	 * Downloads the bill.
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
	 * Creates a bill.
	 */
	public IFileHandle getBillReport() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createBillReport", rdbOrder_.getFilter(), rdbOrder_.getSort(), rdbOfferarticle.getFilter(), rdbOfferarticle.getSort(),
				rdbOrder_.getValue("ID"));
	}

	/**
	 * doConfigureButtonsAndEditors
	 * 
	 * @throws Throwable
	 */
	public void doConfigureButtonsAndEditors() throws Throwable
	{
		if (rdbOrder_.getSelectedRow() >= 0)
		{
			buttonEditOffer.setEnabled(true);
			buttonDownload.setEnabled(true);
			buttonUpload.setEnabled(true);
			buttonSave.setEnabled(true);
			buttonGenerateReport.setEnabled(true);
		}
		else
		{
			buttonEditOffer.setEnabled(false);
			buttonDownload.setEnabled(false);
			buttonUpload.setEnabled(false);
			buttonSave.setEnabled(false);
			buttonGenerateReport.setEnabled(false);
		}
	}

	/**
	 * onShow Offers.
	 */
	public void onShow() throws Throwable
	{
		doConfigureButtonsAndEditors();
	}

} // OrderWorkScreen
