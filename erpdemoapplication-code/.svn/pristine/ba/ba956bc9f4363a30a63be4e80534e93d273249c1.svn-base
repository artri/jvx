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
import javax.rad.io.IFileHandle;
import javax.rad.model.condition.Equals;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * The MobileArticleWorkScreen shows the article categories and all articles of
 * this category in a table. It is possible to add, update or delete articles.
 */
public class MobileArticleWorkScreen extends DataSourceWorkScreen
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * editSearch.
	 */
	private FilterEditor	editSearch					= new FilterEditor();

	/**
	 * editArticleArticle.
	 */
	private UIEditor		editArticleArticle			= new UIEditor();

	/**
	 * editArticleDescription.
	 */
	private UIEditor		editArticleDescription		= new UIEditor();

	/**
	 * editArticleArticleNr.
	 */
	private UIEditor		editArticleArticleNr		= new UIEditor();

	/**
	 * editArticlePrice.
	 */
	private UIEditor		editArticlePrice			= new UIEditor();

	/**
	 * editArticleUnitUnit.
	 */
	private UIEditor		editArticleUnitUnit			= new UIEditor();

	/**
	 * editArticleCurrencyCurrency.
	 */
	private UIEditor		editArticleCurrencyCurrency	= new UIEditor();

	/**
	 * labelArticle.
	 */
	private UILabel			labelArticle				= new UILabel();

	/**
	 * labelDescription.
	 */
	private UILabel			labelDescription			= new UILabel();

	/**
	 * labelArticleNr.
	 */
	private UILabel			labelArticleNr				= new UILabel();

	/**
	 * labelSuchen.
	 */
	private UILabel			labelSuchen					= new UILabel();

	/**
	 * labelPrice.
	 */
	private UILabel			labelPrice					= new UILabel();

	/**
	 * labelUnit.
	 */
	private UILabel			labelUnit					= new UILabel();

	/**
	 * labelCurrency.
	 */
	private UILabel			labelCurrency				= new UILabel();

	/**
	 * formLayoutArticleDetail.
	 */
	private UIFormLayout	formLayoutArticleDetail		= new UIFormLayout();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout	formLayoutSearch			= new UIFormLayout();

	/**
	 * groupPanelArticle.
	 */
	private UIGroupPanel	groupPanelArticle			= new UIGroupPanel();

	/**
	 * groupPanelArticleDetail.
	 */
	private UIGroupPanel	groupPanelArticleDetail		= new UIGroupPanel();

	/**
	 * groupPanelArticleCategory.
	 */
	private UIGroupPanel	groupPanelArticleCategory	= new UIGroupPanel();

	/**
	 * tableArticle.
	 */
	private NavigationTable	tableArticle				= new NavigationTable();

	/**
	 * navigationTable.
	 */
	private NavigationTable	navigationTable				= new NavigationTable();

	/**
	 * Its a article remote databook.
	 */
	private RemoteDataBook	rdbArticle					= new RemoteDataBook();

	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle				= new RemoteDataBook();

	/**
	 * Its a articlecategory remote databook.
	 */
	private RemoteDataBook	rdbArticlecategory			= new RemoteDataBook();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout	borderLayoutMain			= new UIBorderLayout();

	/**
	 * borderLayoutFirst.
	 */
	private UIBorderLayout	borderLayoutFirst			= new UIBorderLayout();

	/**
	 * borderLayoutSecond.
	 */
	private UIBorderLayout	borderLayoutSecond			= new UIBorderLayout();

	/**
	 * borderLayoutArticle.
	 */
	private UIBorderLayout	borderLayoutArticle			= new UIBorderLayout();

	/**
	 * borderLayoutArticleCategory.
	 */
	private UIBorderLayout	borderLayoutArticleCategory	= new UIBorderLayout();

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

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new instance of <code>ArticleWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection.
	 * @throws Throwable if an error occurs.
	 */
	public MobileArticleWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
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
		rdbArticle.setName("article");
		rdbArticle.setDataSource(getDataSource());
		rdbArticle.open();

		rdbArticle.getRowDefinition().getColumnDefinition("DESCRIPTION").getDataType().setCellEditor(ProjXUtil.MULTILINE_EDITOR);
		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setMasterReference(new ReferenceDefinition(new String[] { "ARTICLE_ID" }, rdbArticle, new String[] { "ID" }));
		rdbOfferarticle.setDataSource(getDataSource());
		rdbOfferarticle.open();

		rdbArticle.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "ARTICLE_NR", "ARTICLE" });

		rdbArticlecategory.setName("articlecategory");
		rdbArticlecategory.setDataSource(getDataSource());
		rdbArticlecategory.setUpdateEnabled(false);
		rdbArticlecategory.setInsertEnabled(false);
		rdbArticlecategory.setDeleteEnabled(false);
		rdbArticlecategory.open();

		rdbOfferarticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbArticle.eventAfterInserting().addListener(this, "doAfterInserting");
		rdbArticle.eventAfterInserting().addListener(this, "doAfterInserting");

		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE_NR").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("CURRENCY_CURRENCY").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbArticlecategory.eventAfterRowSelected().addListener(this, "doFilterCategory");

		rdbArticlecategory.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "TITLE" });

	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		labelArticle.setText("Article");

		labelDescription.setVerticalAlignment(0);
		labelDescription.setText("Description");

		labelArticleNr.setText("Article Nr");

		labelSuchen.setText("Search");

		labelPrice.setText("Price");

		labelUnit.setText("Unit");

		labelCurrency.setText("Currency");

		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSuchen, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(editSearch, formLayoutSearch.getConstraints(1, 0, -1, 0));

		editArticleArticle.setDataRow(rdbArticle);
		editArticleArticle.setColumnName("ARTICLE");

		editArticleDescription.setDataRow(rdbArticle);
		editArticleDescription.setColumnName("DESCRIPTION");

		editArticleArticleNr.setDataRow(rdbArticle);
		editArticleArticleNr.setColumnName("ARTICLE_NR");

		editArticlePrice.setDataRow(rdbArticle);
		editArticlePrice.setColumnName("PRICE");

		editArticleUnitUnit.setDataRow(rdbArticle);
		editArticleUnitUnit.setColumnName("UNIT_UNIT");

		editArticleCurrencyCurrency.setDataRow(rdbArticle);
		editArticleCurrencyCurrency.setColumnName("CURRENCY_CURRENCY");

		editSearch.setDataRow(rdbArticle);

		tableArticle.setPreferredSize(new UIDimension(400, 350));
		tableArticle.setDataBook(rdbArticle);
		tableArticle.setAutoResize(false);
		tableArticle.setExportVisible(false);

		groupPanelArticleCategory.setText("Article Category");

		groupPanelArticleCategory.setLayout(borderLayoutArticleCategory);
		groupPanelArticleCategory.add(navigationTable, UIBorderLayout.CENTER);

		borderLayoutFirst.setMargins(10, 10, 10, 10);
		splitPanelMainFirst.setLayout(borderLayoutFirst);
		splitPanelMainFirst.add(groupPanelArticleCategory, UIBorderLayout.CENTER);

		groupPanelArticle.setPreferredSize(400, 300);
		groupPanelArticle.setText("Articles");
		groupPanelArticle.setLayout(borderLayoutArticle);
		groupPanelArticle.add(panelSearch, UIBorderLayout.NORTH);
		groupPanelArticle.add(tableArticle, UIBorderLayout.CENTER);

		groupPanelArticleDetail.setText("Article Detail");
		groupPanelArticleDetail.setLayout(formLayoutArticleDetail);
		groupPanelArticleDetail.add(labelArticleNr, formLayoutArticleDetail.getConstraints(0, 0));
		groupPanelArticleDetail.add(editArticleArticleNr, formLayoutArticleDetail.getConstraints(1, 0));
		groupPanelArticleDetail.add(labelArticle, formLayoutArticleDetail.getConstraints(0, 1));
		groupPanelArticleDetail.add(editArticleArticle, formLayoutArticleDetail.getConstraints(1, 1, -1, 1));
		groupPanelArticleDetail.add(labelDescription, formLayoutArticleDetail.getConstraints(0, 2));
		groupPanelArticleDetail.add(editArticleDescription, formLayoutArticleDetail.getConstraints(1, 2, -1, 2));
		groupPanelArticleDetail.add(labelPrice, formLayoutArticleDetail.getConstraints(0, 3));
		groupPanelArticleDetail.add(editArticlePrice, formLayoutArticleDetail.getConstraints(1, 3));
		groupPanelArticleDetail.add(labelCurrency, formLayoutArticleDetail.getConstraints(0, 4));
		groupPanelArticleDetail.add(editArticleCurrencyCurrency, formLayoutArticleDetail.getConstraints(1, 4));
		groupPanelArticleDetail.add(labelUnit, formLayoutArticleDetail.getConstraints(0, 5));
		groupPanelArticleDetail.add(editArticleUnitUnit, formLayoutArticleDetail.getConstraints(1, 5));

		borderLayoutSecond.setVerticalGap(10);
		borderLayoutSecond.setMargins(10, 10, 10, 10);

		navigationTable.setMaximumSize(new UIDimension(450, 350));
		navigationTable.setDataBook(rdbArticlecategory);
		navigationTable.setAutoResize(false);
		navigationTable.setToolBarVisible(false);

		splitPanelMainSecond.setLayout(borderLayoutSecond);
		splitPanelMainSecond.add(groupPanelArticle, UIBorderLayout.NORTH);
		splitPanelMainSecond.add(groupPanelArticleDetail, UIBorderLayout.CENTER);

		splitPanelMain.setDividerPosition(310);

		splitPanelMain.add(splitPanelMainFirst, UISplitPanel.FIRST_COMPONENT);
		splitPanelMain.add(splitPanelMainSecond, UISplitPanel.SECOND_COMPONENT);

		setLayout(borderLayoutMain);
		add(splitPanelMain, UIBorderLayout.CENTER);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Set values in rdbArticles after the insert.
	 */
	public void doAfterInserting() throws Throwable
	{
		rdbArticle.setValue("CURRENCY_ID", new BigDecimal(1));
		rdbArticle.setValue("CURRENCY_CURRENCY", "EURO");
		rdbArticle.setValue("ARTICLECATEGORY_ID", rdbArticlecategory.getValue("ID"));
	}

	/**
	 * Sets the filter to the selected category. Is called after an item in the
	 * tree is selected.
	 */
	public void doFilterCategory() throws Throwable
	{
		if (rdbArticlecategory.getSelectedRow() >= 0)
		{
			rdbArticle.setInsertEnabled(true);

			editSearch.setValue(null);

			rdbArticle.setFilter(new Equals("ARTICLECATEGORY_ID", rdbArticlecategory.getValue("ID")));
		}
		else
		{
			rdbArticle.setInsertEnabled(false);
		}
	}

	/**
	 * Creates a new article number from the generated id.
	 * 
	 * @throws Throwable
	 */
	public void doCreateArticleNr() throws Throwable
	{
		BigDecimal id = (BigDecimal)rdbArticle.getValue("ID");

		String articleNr = "AT - " + id;

		rdbArticle.setValue("ARTICLE_NR", articleNr);

		rdbArticle.saveSelectedRow();
	}

	/**
	 * Creates a List - Report.
	 */
	public IFileHandle getListReportArticle() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createListReportArticle", rdbArticle.getFilter(), rdbArticle.getSort());
	}

	/**
	 * Downloads the created list report.
	 */
	public void doExportArticlesToWebshop(UIActionEvent pEvent) throws Throwable
	{
		getApplication().getLauncher().showFileHandle(getListReportArticle());
	}

} // MobileArticleWorkScreen
