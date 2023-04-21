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

import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.demoerp.components.MobileSelectArticlesTabsetPanel;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * The MobileOffersWorkScreen shows all offers and the screen was optimized for mobile devices.
 */
public class MobileOffersWorkScreen extends DataSourceWorkScreen
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * tabsetPanel.
	 */
	private MobileSelectArticlesTabsetPanel	tabsetPanel;

	/**
	 * editV_offerCreationdate.
	 */
	private UIEditor		editV_offerCreationdate			= new UIEditor();

	/**
	 * editV_offerTitle.
	 */
	private UIEditor		editV_offerTitle				= new UIEditor();

	/**
	 * editV_offerValiduntil.
	 */
	private UIEditor		editV_offerValiduntil			= new UIEditor();

	/**
	 * editV_offerTotalprice.
	 */
	private UIEditor		editV_offerTotalprice			= new UIEditor();

	/**
	 * editV_offerTaxInPercent.
	 */
	private UIEditor		editV_offerTaxInPercent			= new UIEditor();

	/**
	 * editV_offerDiscountInPercent.
	 */
	private UIEditor		editV_offerDiscountInPercent	= new UIEditor();

	/**
	 * editV_offerOfferNr.
	 */
	private UIEditor		editV_offerOfferNr				= new UIEditor();

	/**
	 * editV_offerFixprice.
	 */
	private UIEditor		editV_offerFixprice				= new UIEditor();

	/**
	 * editV_offerGrosstotalprice.
	 */
	private UIEditor		editV_offerGrosstotalprice		= new UIEditor();

	/**
	 * editV_offerIsordered.
	 */
	private UIEditor		editV_offerIsordered			= new UIEditor();

	/**
	 * editV_offerCustomerNr.
	 */
	private UIEditor		editV_offerCustomerNr			= new UIEditor();

	/**
	 * labelCreationdate.
	 */
	private UILabel			labelCreationdate				= new UILabel();

	/**
	 * labelTitle.
	 */
	private UILabel			labelTitle						= new UILabel();

	/**
	 * labelValiduntil.
	 */
	private UILabel			labelValiduntil					= new UILabel();

	/**
	 * labelTotalprice.
	 */
	private UILabel			labelTotalprice					= new UILabel();

	/**
	 * labelTaxInPercent.
	 */
	private UILabel			labelTaxInPercent				= new UILabel();

	/**
	 * labelDiscountInPercent.
	 */
	private UILabel			labelDiscountInPercent			= new UILabel();

	/**
	 * labelOfferNr.
	 */
	private UILabel			labelOfferNr					= new UILabel();

	/**
	 * labelFixprice.
	 */
	private UILabel			labelFixprice					= new UILabel();

	/**
	 * labelGrosstotalprice.
	 */
	private UILabel			labelGrosstotalprice			= new UILabel();

	/**
	 * labelIsordered.
	 */
	private UILabel			labelIsordered					= new UILabel();

	/**
	 * labelCustomerNr.
	 */
	private UILabel			labelCustomerNr					= new UILabel();

	/**
	 * formLayout1.
	 */
	private UIFormLayout	formLayout1						= new UIFormLayout();

	/**
	 * groupPanelVOffer.
	 */
	private UIGroupPanel	groupPanelVOffer				= new UIGroupPanel();

	/**
	 * navigationTable.
	 */
	private NavigationTable	navigationTable					= new NavigationTable();

	/**
	 * Its a v_offer remote databook.
	 */
	private RemoteDataBook	rdbOffer						= new RemoteDataBook();

	/**
	 * Its a v_offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle					= new RemoteDataBook();

	/**
	 * borderLayout1.
	 */
	private UIBorderLayout	borderLayout1					= new UIBorderLayout();

	/**
	 * borderLayout2.
	 */
	private UIBorderLayout	borderLayout2					= new UIBorderLayout();

	/**
	 * borderLayout3.
	 */
	private UIBorderLayout	borderLayout3					= new UIBorderLayout();

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
	 * panel1.
	 */
	private UIPanel			panel1							= new UIPanel();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>MobileOffersWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public MobileOffersWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();
		
		rdbOffer.eventAfterInserted().addListener(this, "doCreateOfferNr");
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
		rdbOffer.open();

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("VALIDUNTIL").getDataType().setCellEditor(ProjXUtil.DATE_SHORT_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("TOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("FIXPRICE").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbOffer.getRowDefinition().getColumnDefinition("GROSSTOTALPRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbOffer.getRowDefinition().getColumnDefinition("ISORDERED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbOffer.getRowDefinition().getColumnView(ITableControl.class)
				.setColumnNames(new String[] { "OFFER_NR", "TITLE", "CREATIONDATE", "VALIDUNTIL", "CUSTOMER_NR", "ISORDERED" });

		rdbOffer.getRowDefinition().getColumnDefinition("CREATIONDATE").setLabel("Offerdate");
		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setDataSource(getDataSource());

		rdbOfferarticle.open();
		
		rdbOffer.getRowDefinition().getColumnDefinition("CUSTOMER_NR").setReadOnly(true);
		rdbOffer.getRowDefinition().getColumnDefinition("OFFER_NR").setReadOnly(true);

	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		navigationTable.setMaximumSize(new UIDimension(450, 350));
		navigationTable.setDataBook(rdbOffer);
		navigationTable.setAutoResize(false);

		labelCreationdate.setText("Offerdate");

		labelTitle.setText("Title");

		labelValiduntil.setText("Validuntil");

		labelTotalprice.setText("Totalprice");

		labelTaxInPercent.setText("Tax In Percent");

		labelDiscountInPercent.setText("Discount In Percent");

		labelOfferNr.setText("Offer Nr");

		labelFixprice.setText("Fixprice");

		labelGrosstotalprice.setText("Grosstotalprice");

		labelIsordered.setText("Isordered");

		labelCustomerNr.setText("Customer Nr");

		panel1.setLayout(new UIBorderLayout());
		
		tabsetPanel = new MobileSelectArticlesTabsetPanel(getDataSource(), rdbOffer);
		tabsetPanel.setPreferredSize(500, 320);

		editV_offerCreationdate.setDataRow(rdbOffer);
		editV_offerCreationdate.setColumnName("CREATIONDATE");

		editV_offerTitle.setDataRow(rdbOffer);
		editV_offerTitle.setColumnName("TITLE");

		editV_offerValiduntil.setDataRow(rdbOffer);
		editV_offerValiduntil.setColumnName("VALIDUNTIL");

		editV_offerTotalprice.setDataRow(rdbOffer);
		editV_offerTotalprice.setColumnName("TOTALPRICE");

		editV_offerTaxInPercent.setDataRow(rdbOffer);
		editV_offerTaxInPercent.setColumnName("TAX_IN_PERCENT");

		editV_offerDiscountInPercent.setDataRow(rdbOffer);
		editV_offerDiscountInPercent.setColumnName("DISCOUNT_IN_PERCENT");

		editV_offerOfferNr.setDataRow(rdbOffer);
		editV_offerOfferNr.setColumnName("OFFER_NR");

		editV_offerFixprice.setDataRow(rdbOffer);
		editV_offerFixprice.setColumnName("FIXPRICE");

		editV_offerGrosstotalprice.setDataRow(rdbOffer);
		editV_offerGrosstotalprice.setColumnName("GROSSTOTALPRICE");

		editV_offerIsordered.setDataRow(rdbOffer);
		editV_offerIsordered.setColumnName("ISORDERED");

		editV_offerCustomerNr.setDataRow(rdbOffer);
		editV_offerCustomerNr.setColumnName("CUSTOMER_NR");

		splitPanelMainFirst.setLayout(borderLayout2);
		splitPanelMainFirst.add(navigationTable, UIBorderLayout.CENTER);

		groupPanelVOffer.setText("Offer");
		groupPanelVOffer.setLayout(formLayout1);
		groupPanelVOffer.add(labelOfferNr, formLayout1.getConstraints(0, 0));
		groupPanelVOffer.add(editV_offerOfferNr, formLayout1.getConstraints(1, 0));
        groupPanelVOffer.add(labelTitle, formLayout1.getConstraints(0, 1));
        groupPanelVOffer.add(editV_offerTitle, formLayout1.getConstraints(1, 1, -1, 1));
		groupPanelVOffer.add(labelCustomerNr, formLayout1.getConstraints(0, 2));
		groupPanelVOffer.add(editV_offerCustomerNr, formLayout1.getConstraints(1, 2));
		groupPanelVOffer.add(labelCreationdate, formLayout1.getConstraints(0, 3));
		groupPanelVOffer.add(editV_offerCreationdate, formLayout1.getConstraints(1, 3));
		groupPanelVOffer.add(labelValiduntil, formLayout1.getConstraints(0, 4));
		groupPanelVOffer.add(editV_offerValiduntil, formLayout1.getConstraints(1, 4));
        groupPanelVOffer.add(labelFixprice, formLayout1.getConstraints(0, 5));
        groupPanelVOffer.add(editV_offerFixprice, formLayout1.getConstraints(1, 5));
		groupPanelVOffer.add(tabsetPanel, formLayout1.getConstraints(0, 6, -1, 6));
		groupPanelVOffer.add(labelTotalprice, formLayout1.getConstraints(0, 7));
		groupPanelVOffer.add(editV_offerTotalprice, formLayout1.getConstraints(1, 7));
		groupPanelVOffer.add(labelTaxInPercent, formLayout1.getConstraints(0, 8));
		groupPanelVOffer.add(editV_offerTaxInPercent, formLayout1.getConstraints(1, 8));
        groupPanelVOffer.add(labelDiscountInPercent, formLayout1.getConstraints(0, 9));
		groupPanelVOffer.add(editV_offerDiscountInPercent, formLayout1.getConstraints(1, 9));
		groupPanelVOffer.add(labelGrosstotalprice, formLayout1.getConstraints(0, 10));
		groupPanelVOffer.add(editV_offerGrosstotalprice, formLayout1.getConstraints(1, 10));
		groupPanelVOffer.add(labelIsordered, formLayout1.getConstraints(0, 11));
		groupPanelVOffer.add(editV_offerIsordered, formLayout1.getConstraints(1, 11));

		splitPanelMainSecond.setLayout(borderLayout3);
		splitPanelMainSecond.add(groupPanelVOffer, UIBorderLayout.CENTER);

		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);

		setLayout(borderLayout1);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}
	
	public void doCreateOfferNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbOffer.getValue("ID");

		String offerNr = "OFN - " + id;

		rdbOffer.setValue("OFFER_NR", offerNr);

		rdbOffer.saveSelectedRow();
	}

} // MobileOffersWorkScreen
