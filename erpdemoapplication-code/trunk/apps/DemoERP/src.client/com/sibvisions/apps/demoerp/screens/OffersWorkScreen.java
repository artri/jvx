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

import java.util.Hashtable;

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIPanel;
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
import com.sibvisions.rad.model.remote.RemoteDataBook;
import java.lang.String;
import javax.rad.genui.UIInsets;

/**
 * The OffersWorkScreen shows all offers in a table.
 */
public class OffersWorkScreen extends DataSourceWorkScreen
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * buttonEditOffer.
	 */
	private UIButton		buttonEditOffer			= new UIButton();

	/**
	 * buttonGenerateReport.
	 */
	private UIButton		buttonGenerateReport	= new UIButton();

	/**
	 * buttonButtonOrder.
	 */
	private UIButton		buttonOrder				= new UIButton();

	/**
	 * navigationTable.
	 */
	private NavigationTable	navigationTable			= new NavigationTable();

	/**
	 * filterEditor.
	 */
	private FilterEditor	filterEditor			= new FilterEditor();

	/**
	 * labelSearch.
	 */
	private UILabel			labelSearch				= new UILabel();

	/**
	 * formLayoutMain.
	 */
	private UIFormLayout	formLayoutMain			= new UIFormLayout();

	/**
	 * formLayout2.
	 */
	private UIFormLayout	formLayout2				= new UIFormLayout();

	/**
	 * panelMain.
	 */
	private UIPanel			panelMain				= new UIPanel();

	/**
	 * panelActions.
	 */
	private UIPanel			panelActions			= new UIPanel();

	/**
	 * formLayoutActions.
	 */
	private UIFormLayout	formLayoutActions		= new UIFormLayout();

	/**
	 * panelSearch.
	 */
	private UIPanel			panelSearch				= new UIPanel();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout	borderLayoutMain		= new UIBorderLayout();

	/**
	 * Its a offer remote databook.
	 */
	private RemoteDataBook	rdbOffer				= new RemoteDataBook();

	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle			= new RemoteDataBook();

	/**
	 * Its a order_ remote databook.
	 */
	private RemoteDataBook	rdbOrder_				= new RemoteDataBook();

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>OffersWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public OffersWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		rdbOffer.eventAfterRowSelected().addListener(this, "doConfigureButtonsAndEditors");
	}

	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		rdbOffer.setName("offer");
		rdbOffer.setDataSource(getDataSource());
		rdbOffer.setInsertEnabled(false);
		rdbOffer.open();

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("VALIDUNTIL").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setLabel("Customer");

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").setLabel("Offer date");

		rdbOffer.getRowDefinition().getColumnDefinition("VALIDUNTIL").setLabel("Valid until");

		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").setLabel("Total price");

		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").setLabel("Fix price");

		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").setLabel("Is ordered");

		rdbOffer.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").setLabel("Gross totalprice");

		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbOrder_.setName("order_");
		rdbOrder_.setMasterReference(new ReferenceDefinition(new String[] { "OFFER_ID" }, rdbOffer, new String[] { "ID" }));
		rdbOrder_.setDataSource(getDataSource());

		rdbOrder_.open();

		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setMasterReference(new ReferenceDefinition(new String[] { "OFFER_ID" }, rdbOffer, new String[] { "ID" }));
		rdbOfferarticle.setDataSource(getDataSource());

		rdbOfferarticle.open();

		rdbOffer.getRowDefinition().getColumnDefinition("OFFER_NR").setWidth(150);

		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").setWidth(100);
		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").setWidth(100);

		rdbOffer.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("OFFER_NR").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").setReadOnly(true);

		rdbOffer.getRowDefinition()
				.setColumnView(
						ITableControl.class,
						new ColumnView(
								new String[] { "OFFER_NR", "TITLE", "CREATIONDATE", "VALIDUNTIL", "CUSTOMER_NR", "TOTALPRICE", "TAX_IN_PERCENT", "DISCOUNT_IN_PERCENT", "GROSSTOTALPRICE", "FIXPRICE", "ISORDERED" }));

		rdbOffer.getRowDefinition().getColumnDefinition("TAX_IN_PERCENT").setLabel("Tax in %");
		rdbOffer.getRowDefinition().getColumnDefinition("DISCOUNT_IN_PERCENT").setLabel("Discount in %");
	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		labelSearch.setText("Search");

		filterEditor.setDataRow(rdbOffer);

		navigationTable.setMaximumSize(new UIDimension(450, 350));
		navigationTable.setDataBook(rdbOffer);
		navigationTable.setAutoResize(false);
		navigationTable.setInsertVisible(false);

		buttonEditOffer.setText("Show Offer");
		buttonEditOffer.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/invoice.png"));
		buttonEditOffer.eventAction().addListener(this, "doEditOffer");
		buttonEditOffer.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonEditOffer.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonEditOffer.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonEditOffer.setImageTextGap(2);
		buttonEditOffer.setPreferredSize(160, 60);

		buttonGenerateReport.setText("Download Offer");
		buttonGenerateReport.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/export_database2.png"));
		buttonGenerateReport.eventAction().addListener(this, "doGenerateOfferReport");
		buttonGenerateReport.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		buttonGenerateReport.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		buttonGenerateReport.setFont(new UIFont("Arial", UIFont.BOLD, 13));
		buttonGenerateReport.setImageTextGap(2);
		buttonGenerateReport.setPreferredSize(160, 60);

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

		formLayout2.setMargins(new UIInsets(0, 0, 10, 0));

		panelActions.setLayout(formLayoutActions);
		panelActions.add(buttonEditOffer, formLayoutActions.getConstraints(-3, 0));
		panelActions.add(buttonOrder, formLayoutActions.getConstraints(-2, 0));
		panelActions.add(buttonGenerateReport, formLayoutActions.getConstraints(-1, 0));

		panelSearch.setLayout(formLayout2);
		panelSearch.add(labelSearch, formLayout2.getConstraints(0, 0));
		panelSearch.add(filterEditor, formLayout2.getConstraints(1, 0, -1, 0));

		panelMain.setLayout(formLayoutMain);
		panelMain.add(panelSearch, formLayoutMain.getConstraints(0, 0, -1, 0));
		panelMain.add(navigationTable, formLayoutMain.getConstraints(0, 1, -1, -1));

		setLayout(borderLayoutMain);
		add(panelMain, UIBorderLayout.CENTER);
		add(panelActions, UIBorderLayout.SOUTH);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates the offer report.
	 */
	public IFileHandle getOfferReport() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createOfferReport", rdbOffer.getValue("ID"));
	}

	/**
	 * Enables the toolbar buttons if no offer is selected.
	 * 
	 * @throws Throwable
	 */
	public void doConfigureButtonsAndEditors() throws Throwable
	{
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

		if (rdbOffer.getSelectedRow() >= 0)
		{
			buttonEditOffer.setEnabled(true);
			buttonOrder.setEnabled(true);
			buttonGenerateReport.setEnabled(true);
		}
		else
		{
			buttonEditOffer.setEnabled(false);
			buttonOrder.setEnabled(false);
			buttonGenerateReport.setEnabled(false);
		}
	}

	/**
	 * Generates the offer report.
	 */
	public void doGenerateOfferReport(UIActionEvent pEvent) throws Throwable
	{
		getApplication().getLauncher().showFileHandle(getOfferReport());
	}

	/**
	 * Opens the detail view workscreen for the offer.
	 * 
	 * @param pEvent action event.
	 * @throws Throwable
	 */
	public void doEditOffer(UIActionEvent pEvent) throws Throwable
	{
		Hashtable<String, Object> htParams = new Hashtable<String, Object>();
		htParams.put("ID", rdbOffer.getValue("ID"));

		getApplication().openWorkScreen("com.sibvisions.apps.demoerp.screens.OfferWorkScreen", htParams);
	}

	/**
	 * Opens the detail view workscreen for the order. It sets the status
	 * isordered to yes.
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
	 * Sets the value in the filter editor.
	 * 
	 * @param pCustomerNr
	 * @throws Throwable
	 */
	public void setFilter(Object pCustomerNr) throws Throwable
	{
		filterEditor.setValue(pCustomerNr);
	}

	/**
	 * onShow Offers.
	 */
	public void onShow() throws Throwable
	{
		doConfigureButtonsAndEditors();
	}

} // OffersWorkScreen
