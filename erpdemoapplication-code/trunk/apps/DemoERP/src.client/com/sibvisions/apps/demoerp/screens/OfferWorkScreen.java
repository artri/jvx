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

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.io.IFileHandle;
import javax.rad.model.condition.Equals;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.demoerp.components.SelectArticlesTabsetPanel;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.apps.util.Calc;
import com.sibvisions.apps.util.Var;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.util.type.CommonUtil;

/**
 * The OfferWorkScreen is the detail view of an offer.
 */
public class OfferWorkScreen extends DataSourceWorkScreen
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * labelOfferNr.
	 */
	private UILabel						labelOfferNr				= new UILabel();

	/**
	 * labelTitle.
	 */
	private UILabel						labelTitle					= new UILabel();

	/**
	 * labelCustomerNr.
	 */
	private UILabel						labelCustomerNr				= new UILabel();

	/**
	 * labelCreationdate.
	 */
	private UILabel						labelCreationdate			= new UILabel();

	/**
	 * labelValiduntil.
	 */
	private UILabel						labelValiduntil				= new UILabel();

	/**
	 * labelFixprice.
	 */
	private UILabel						labelFixprice				= new UILabel();

	/**
	 * labelTotalprice.
	 */
	private UILabel						labelTotalprice				= new UILabel();

	/**
	 * labelTaxInPercent.
	 */
	private UILabel						labelTaxInPercent			= new UILabel();

	/**
	 * labelDiscountInPercent.
	 */
	private UILabel						labelDiscountInPercent		= new UILabel();

	/**
	 * labelGrossTotalprice.
	 */
	private UILabel						labelGrossTotalprice		= new UILabel();

	/**
	 * labelIsOrdered.
	 */
	private UILabel						labelIsOrdered				= new UILabel();

	/**
	 * SelectArticlesTabsetPanel.
	 */
	private SelectArticlesTabsetPanel	tabsetPanelSelectArticles;

	/**
	 * editOfferOfferNr.
	 */
	private UIEditor					editOfferOfferNr			= new UIEditor();

	/**
	 * editOfferTitle.
	 */
	private UIEditor					editOfferTitle				= new UIEditor();

	/**
	 * editOfferCustomerCustomerNr.
	 */
	private UIEditor					editOfferCustomerCustomerNr	= new UIEditor();

	/**
	 * editOfferCreationdate.
	 */
	private UIEditor					editOfferCreationdate		= new UIEditor();

	/**
	 * editOfferValiduntil.
	 */
	private UIEditor					editOfferValiduntil			= new UIEditor();

	/**
	 * editOfferFixprice.
	 */
	private UIEditor					editOfferFixprice			= new UIEditor();

	/**
	 * editOfferTotalprice.
	 */
	private UIEditor					editOfferTotalprice			= new UIEditor();

	/**
	 * editOfferTaxInPercent.
	 */
	private UIEditor					editOfferTaxInPercent		= new UIEditor();

	/**
	 * editOfferDiscountInPercent.
	 */
	private UIEditor					editOfferDiscountInPercent	= new UIEditor();

	/**
	 * editOfferGrosstotalprice.
	 */
	private UIEditor					editOfferGrosstotalprice	= new UIEditor();

	/**
	 * editOfferIsordered.
	 */
	private UIEditor					editOfferIsordered			= new UIEditor();

	/**
	 * labelBezeichner4.
	 */
	private UILabel						labelCurrency1				= new UILabel();

	/**
	 * labelPercent1.
	 */
	private UILabel						labelPercent1				= new UILabel();

	/**
	 * labelPercent2.
	 */
	private UILabel						labelPercent2				= new UILabel();

	/**
	 * labelCurrency2.
	 */
	private UILabel						labelCurrency2				= new UILabel();

	/**
	 * buttonSave.
	 */
	private UIButton					buttonSave					= new UIButton();

	/**
	 * buttonGenerateDocument.
	 */
	private UIButton					buttonGenerateDocument		= new UIButton();

	/**
	 * buttonButtonOrder.
	 */
	private UIButton					buttonOrder					= new UIButton();

	/**
	 * formLayoutOffer.
	 */
	private UIFormLayout				formLayoutOffer				= new UIFormLayout();

	/**
	 * groupPanelOffer.
	 */
	private UIGroupPanel				groupPanelOffer				= new UIGroupPanel();

	/**
	 * main panel.
	 */
	private UIPanel						panelMain					= new UIPanel();

	/**
	 * panelActions.
	 */
	private UIPanel						panelActions				= new UIPanel();

	/**
	 * formLayoutPrices.
	 */
	private UIFormLayout				formLayoutPrices			= new UIFormLayout();

	/**
	 * panelPrices.
	 */
	private UIPanel						panelPrices					= new UIPanel();

	/**
	 * formLayoutActions.
	 */
	private UIFormLayout				formLayoutActions			= new UIFormLayout();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout				borderLayoutMain			= new UIBorderLayout();

	/**
	 * borderLayoutOffer.
	 */
	private UIBorderLayout				borderLayoutOffer			= new UIBorderLayout();

	/**
	 * Its a offer remote databook.
	 */
	private RemoteDataBook				rdbOffer					= new RemoteDataBook();

	/**
	 * Its a customer remote databook.
	 */
	private RemoteDataBook				rdbCustomer					= new RemoteDataBook();

	/**
	 * The current offer id.
	 */
	private BigDecimal					bdOldId						= null;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>OfferWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public OfferWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		rdbOffer.eventAfterInserted().addListener(this, "doCreateOfferNr");
		rdbOffer.eventAfterInserting().addListener(this, "doOfferInserting");
		rdbOffer.eventAfterInserting().addListener(this, "doOfferInserting");
		rdbOffer.eventValuesChanged().addListener(this, "doCalculatePrice");
		rdbOffer.eventAfterInserting().addListener(this, "doOfferInserting");
		rdbOffer.eventAfterRowSelected().addListener(this, "doConfigureButtonsAndEditors");
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

		rdbOffer.setName("offer");
		rdbOffer.setDataSource(getDataSource());
		rdbOffer.open();

		rdbOffer.getRowDefinition().getColumnDefinition("TITLE").setWidth(78);

		rdbOffer.getRowDefinition().getColumnDefinition("VALIDUNTIL").setWidth(105);

		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").setWidth(109);

		rdbOffer.getRowDefinition().getColumnDefinition("OFFER_NR").setWidth(98);
		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("VALIDUNTIL").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").setReadOnly(true);

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").setLabel("OFFER DATE");
		rdbOffer.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		tabsetPanelSelectArticles = new SelectArticlesTabsetPanel(getDataSource(), rdbOffer);
		tabsetPanelSelectArticles.setPreferredSize(500, 320);
		tabsetPanelSelectArticles.setTranslationEnabled(true);
		tabsetPanelSelectArticles.setTabPlacement(1);
		tabsetPanelSelectArticles.setTabLayoutPolicy(0);
		tabsetPanelSelectArticles.setNavigationKeysEnabled(false);
		tabsetPanelSelectArticles.setForeground(new UIColor(0x0));
		tabsetPanelSelectArticles.setFont(new UIFont("Tahoma", UIFont.PLAIN, 11));
		tabsetPanelSelectArticles.setFocusable(true);
		tabsetPanelSelectArticles.setEnabled(true);
		tabsetPanelSelectArticles.setDraggable(false);

		labelCreationdate.setText("Offer date");

		labelTitle.setText("Title");

		labelValiduntil.setText("Valid until");

		labelTotalprice.setText("Total price");

		labelTaxInPercent.setText("Tax");

		labelDiscountInPercent.setText("Discount");

		labelOfferNr.setText("Offer Nr");

		labelFixprice.setText("Fix price");

		labelGrossTotalprice.setText("Gross total price");

		labelPercent1.setText("(%)");

		labelPercent2.setText("(%)");

		labelCurrency1.setText("Euro");

		labelCurrency2.setText("Euro");

		labelCustomerNr.setText("Customer Nr");

		editOfferCreationdate.setDataRow(rdbOffer);
		editOfferCreationdate.setColumnName("CREATIONDATE");

		editOfferTitle.setDataRow(rdbOffer);
		editOfferTitle.setColumnName("TITLE");

		editOfferValiduntil.setDataRow(rdbOffer);
		editOfferValiduntil.setColumnName("VALIDUNTIL");

		editOfferTotalprice.setDataRow(rdbOffer);
		editOfferTotalprice.setColumnName("TOTALPRICE");

		editOfferTaxInPercent.setDataRow(rdbOffer);
		editOfferTaxInPercent.setColumnName("TAX_IN_PERCENT");

		editOfferDiscountInPercent.setDataRow(rdbOffer);
		editOfferDiscountInPercent.setColumnName("DISCOUNT_IN_PERCENT");

		editOfferOfferNr.setDataRow(rdbOffer);
		editOfferOfferNr.setColumnName("OFFER_NR");
		editOfferOfferNr.setEnabled(false);

		editOfferFixprice.setDataRow(rdbOffer);
		editOfferFixprice.setColumnName("FIXPRICE");
		editOfferFixprice.setSavingImmediate(true);

		editOfferGrosstotalprice.setDataRow(rdbOffer);
		editOfferGrosstotalprice.setColumnName("GROSSTOTALPRICE");
		editOfferGrosstotalprice.setEnabled(false);

		editOfferCustomerCustomerNr.setDataRow(rdbOffer);
		editOfferCustomerCustomerNr.setColumnName("CUSTOMER_NR");

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

		buttonGenerateDocument.setText("Download Offer");
		buttonGenerateDocument.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/export_database2.png"));
		buttonGenerateDocument.eventAction().addListener(this, "doGenerateOfferReport");
		buttonGenerateDocument.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateDocument.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonGenerateDocument.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateDocument.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateDocument.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonGenerateDocument.setImageTextGap(2);
		buttonGenerateDocument.setPreferredSize(160, 60);

		buttonOrder.setText("Generate Order");
		buttonOrder.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/paypal.png"));
		buttonOrder.eventAction().addListener(this, "doOrder");
		buttonOrder.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonOrder.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonOrder.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonOrder.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonOrder.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonOrder.setImageTextGap(2);
		buttonOrder.setPreferredSize(160, 60);

		panelActions.setLayout(formLayoutActions);
		panelActions.add(buttonSave, formLayoutActions.getConstraints(-3, 0));
		panelActions.add(buttonGenerateDocument, formLayoutActions.getConstraints(-2, 0));
		panelActions.add(buttonOrder, formLayoutActions.getConstraints(-1, 0));

		editOfferIsordered.setDataRow(rdbOffer);
		editOfferIsordered.setColumnName("ISORDERED");

		labelIsOrdered.setText("Is Ordered");

		formLayoutPrices.setAnchorConfiguration("l-2=-160");

		formLayoutOffer.setAnchorConfiguration("r1=160,l2=12,l4=12,t3=13,b3=318");

		borderLayoutOffer.setMargins(new UIInsets(5, 5, 5, 5));

		panelPrices.setLayout(formLayoutPrices);
		panelPrices.add(labelTotalprice, formLayoutPrices.getConstraints(-3, 0));
		panelPrices.add(editOfferTotalprice, formLayoutPrices.getConstraints(-2, 0));
		panelPrices.add(labelCurrency1, formLayoutPrices.getConstraints(-1, 0));

		panelPrices.add(labelDiscountInPercent, formLayoutPrices.getConstraints(-3, 1));
		panelPrices.add(editOfferDiscountInPercent, formLayoutPrices.getConstraints(-2, 1));
		panelPrices.add(labelPercent2, formLayoutPrices.getConstraints(-1, 1));

		panelPrices.add(labelTaxInPercent, formLayoutPrices.getConstraints(-3, 2));
		panelPrices.add(editOfferTaxInPercent, formLayoutPrices.getConstraints(-2, 2));
		panelPrices.add(labelPercent1, formLayoutPrices.getConstraints(-1, 2));

		panelPrices.add(labelGrossTotalprice, formLayoutPrices.getConstraints(-3, 3));
		panelPrices.add(editOfferGrosstotalprice, formLayoutPrices.getConstraints(-2, 3));
		panelPrices.add(labelCurrency2, formLayoutPrices.getConstraints(-1, 3));

		panelPrices.add(labelIsOrdered, formLayoutPrices.getConstraints(-3, 4));
		panelPrices.add(editOfferIsordered, formLayoutPrices.getConstraints(-2, 4));

		groupPanelOffer.setText("Offer");
		groupPanelOffer.setLayout(formLayoutOffer);
		groupPanelOffer.add(labelOfferNr, formLayoutOffer.getConstraints(0, 0));
		groupPanelOffer.add(editOfferOfferNr, formLayoutOffer.getConstraints(1, 0));
		groupPanelOffer.add(labelCreationdate, formLayoutOffer.getConstraints(2, 0));
		groupPanelOffer.add(editOfferCreationdate, formLayoutOffer.getConstraints(3, 0));
		groupPanelOffer.add(labelFixprice, formLayoutOffer.getConstraints(4, 0));
		groupPanelOffer.add(editOfferFixprice, formLayoutOffer.getConstraints(5, 0));
		groupPanelOffer.add(labelTitle, formLayoutOffer.getConstraints(0, 1));
		groupPanelOffer.add(editOfferTitle, formLayoutOffer.getConstraints(1, 1, -1, 1));
		groupPanelOffer.add(labelCustomerNr, formLayoutOffer.getConstraints(0, 2));
		groupPanelOffer.add(editOfferCustomerCustomerNr, formLayoutOffer.getConstraints(1, 2));
		groupPanelOffer.add(labelValiduntil, formLayoutOffer.getConstraints(2, 2));
		groupPanelOffer.add(editOfferValiduntil, formLayoutOffer.getConstraints(3, 2));
		groupPanelOffer.add(tabsetPanelSelectArticles, formLayoutOffer.getConstraints(0, 3, -1, 3));
		groupPanelOffer.add(panelPrices, formLayoutOffer.getConstraints(0, 4, -1, 4));

		panelMain.setLayout(borderLayoutOffer);
		panelMain.add(groupPanelOffer, UIBorderLayout.CENTER);

		setLayout(borderLayoutMain);
		add(panelMain, UIBorderLayout.CENTER);
		add(panelActions, UIBorderLayout.SOUTH);

		doConfigureButtonsAndEditors();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void onLoad() throws Throwable
	{
		super.onLoad();

		filterOffer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void onActivate() throws Throwable
	{
		if (getParameter("ID") != null)
		{
			filterOffer();
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Filters the offer.
	 * 
	 * @throws Throwable
	 */
	private void filterOffer() throws Throwable
	{
		BigDecimal bdId = (BigDecimal)getParameter("ID");

		if (!CommonUtil.equals(bdId, bdOldId))
		{
			rdbOffer.setFilter(new Equals("ID", bdId));

			rdbCustomer.setFilter(new Equals("ID", rdbOffer.getValue("CUSTOMER_ID")));

			tabsetPanelSelectArticles.setOffer(bdId);

			setParameter("ID", null);

			bdOldId = bdId;
		}
	}

	/**
	 * Creates a offer number from the generated id.
	 * 
	 * @throws Throwable
	 */
	public void doCreateOfferNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbOffer.getValue("ID");

		String offerNr = "OFN - " + id;

		rdbOffer.setValue("OFFER_NR", offerNr);

		rdbOffer.saveSelectedRow();
	}

	/**
	 * Is called before the row is inserted in the database.
	 * 
	 * @throws Throwable
	 */
	public void doOfferInserting() throws Throwable
	{
		rdbOffer.setValue("CREATIONDATE", new Date(System.currentTimeMillis()));
		rdbOffer.setValue("FIXPRICE", "N");
		rdbOffer.setValue("TAX_IN_PERCENT", Integer.valueOf(20));
		rdbOffer.setValue("DISCOUNT_IN_PERCENT", Integer.valueOf(3));

		rdbOffer.saveSelectedRow();
	}

	/**
	 * Calculates the price for the articles.
	 */
	public void doCalculatePrice() throws Throwable
	{
		doConfigureButtonsAndEditors();

		if ("N".equals(rdbOffer.getValue("FIXPRICE")))
		{
			tabsetPanelSelectArticles.doCalculateTotalPrice();
		}

		Calc.set(
				new Var(rdbOffer, "GROSSTOTALPRICE"),
				Calc.val(new Var(rdbOffer, "TOTALPRICE")) * (1 - Calc.val(new Var(rdbOffer, "DISCOUNT_IN_PERCENT")) / 100) * (1 + Calc.val(new Var(rdbOffer, "TAX_IN_PERCENT")) / 100));
	}

	/**
	 * doConfigureButtonsAndEditors
	 * 
	 * @throws Throwable
	 */
	public void doConfigureButtonsAndEditors() throws Throwable
	{
		if ("Y".equals(rdbOffer.getValue("FIXPRICE")))
		{
			editOfferTotalprice.setEnabled(true);
		}
		else
		{
			editOfferTotalprice.setEnabled(false);
		}

		if ("Y".equals(rdbOffer.getValue("ISORDERED")))
		{
			buttonOrder.setText("Show Order");
			buttonOrder.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/paypal.png"));
		}
		else
		{
			buttonOrder.setText("Generate Order");
			buttonOrder.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/paypal.png"));
		}
	}

	/**
	 * Creates a new offer.
	 * 
	 * @param pCustomerId the customer id.
	 * @throws Throwable
	 */
	public void doNewOffer(BigDecimal pCustomerId) throws Throwable
	{
		rdbOffer.insert(false);

		rdbOffer.setValue("CUSTOMER_ID", pCustomerId);

		rdbCustomer.setFilter(new Equals("ID", pCustomerId));

		rdbOffer.setValue("CUSTOMER_NR", rdbCustomer.getValue("CUSTOMER_NR"));
	}

	/**
	 * Generate Order.
	 */
	public void doOrder(UIActionEvent pEvent) throws Throwable
	{
		OrderWorkScreen orderWorkScreen = (OrderWorkScreen)getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OrderWorkScreen");

		if ("N".equals(rdbOffer.getValue("ISORDERED")))
		{
			rdbOffer.setValue("ISORDERED", "Y");
			rdbOffer.saveSelectedRow();

			orderWorkScreen.doInsertOrder(rdbOffer.getValue("CUSTOMER_ID"), rdbOffer.getValue("ID"));
		}
		else
		{
			orderWorkScreen.setFilter(rdbOffer.getValue("OFFER_NR"));
		}
	}

	/**
	 * Creates the offer report.
	 */
	public IFileHandle getOfferReport() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createOfferReport", rdbOffer.getValue("ID"));
	}

	/**
	 * Save the offer.
	 */
	public void doSave(UIActionEvent pEvent) throws Throwable
	{
		tabsetPanelSelectArticles.setSelectedIndex(0);
		save();
	}

	/**
	 * Downloads the offer report.
	 */
	public void doGenerateOfferReport(UIActionEvent pEvent) throws Throwable
	{
		getApplication().getLauncher().showFileHandle(getOfferReport());
	}

} // OfferWorkScreen
