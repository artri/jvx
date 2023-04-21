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
package com.sibvisions.apps.demoerp.components;

import javax.rad.genui.UIDimension;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.IDataSource;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.Equals;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.util.Calc;
import com.sibvisions.apps.util.Var;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 *  A tabset panel to select articles and to show the selected articles in a table.
 *  In mobile edition only tables and no trees are allowed.
 */
public class MobileSelectArticlesTabsetPanel extends UITabsetPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * labelSuchen.
	 */
	private UILabel			labelSearch					= new UILabel();
	
	/**
	 * labelInfo.
	 */
	private UILabel			labelInfo 					= new UILabel();
	
	/**
	 * editArticlenull.
	 */
	private FilterEditor	editArticleSearch			= new FilterEditor();
	
	/**
	 * treeArticleCategory.
	 */
	private NavigationTable		tableArticleCategory	= new NavigationTable();

	/**
	 * panelInner
	 */
	private UIPanel panelInner 							= new UIPanel();
	
	/**
	 * panelOverview.
	 */
	private UIPanel	panelOverview						= new UIPanel();
	
	/**
	 * panelSelectedArticles.
	 */
	private UIPanel	panelSelectedArticles				= new UIPanel();
	
	/**
	 * panelSearch.
	 */
	private UIPanel	panelSearch							= new UIPanel();
	
	/**
	 * tableArticle.
	 */
	private NavigationTable	tableArticle				= new NavigationTable();
	
	/**
	 * tableOfferarticle.
	 */
	private NavigationTable	tableOfferarticle			= new NavigationTable();
	
	/**
	 * borderLayoutOverview.
	 */
	private UIBorderLayout	borderLayoutOverview		= new UIBorderLayout();
	
	/**
	 * borderLayoutSelectedArticles.
	 */
	private UIBorderLayout borderLayoutSelectedArticles	= new UIBorderLayout();
	
	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout formLayoutSearch				= new UIFormLayout();
	
	/**
	 * flowLayoutInner.
	 */
	private UIFormLayout formLayoutInner 				= new UIFormLayout();
	
	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle				= new RemoteDataBook();
	
	/**
	 * Its a articlecategory remote databook.
	 */
	private RemoteDataBook	rdbArticlecategory			= new RemoteDataBook();
	
	/**
	 * Its a article remote databook.
	 */
	private RemoteDataBook	rdbArticle					= new RemoteDataBook();
	
	/**
	 * Its a offer remote databook.
	 */
	private RemoteDataBook rdbOffer;
	
	/**
	 * The dataSource.
	 */
	private IDataSource dataSource;


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * New Instance
	 * 
	 * @throws Throwable 
	 */
	public MobileSelectArticlesTabsetPanel(IDataSource pDataSource, RemoteDataBook pRdbOffer) throws Throwable
	{
		dataSource = pDataSource;
		rdbOffer = pRdbOffer;
		
		initializeModel();
		initializeUI();
		
		rdbArticle.eventValuesChanged().addListener(this, "doAddOrRemoveArticlesFromSelectedArticles");
		rdbArticlecategory.eventAfterRowSelected().addListener(this, "doFilterCategory");
		rdbOfferarticle.eventValuesChanged().addListener(this, "doCalculateTotalPrice");
		rdbOfferarticle.eventAfterDeleted().addListener(this, "doReload");
		
		rdbArticlecategory.setSelectedRow(-1);
		
		rdbOffer.eventAfterRowSelected().addListener(this, "doSetFilter");
	}
	
	/**
	 * Initializes the model.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeModel() throws Throwable
	{
		rdbArticle.setName("article");
		rdbArticle.setDataSource(dataSource);
		rdbArticle.setSort(new SortDefinition("ARTICLE_NR"));
		rdbArticle.open();

		rdbArticle.getRowDefinition().getColumnDefinition("SELECTED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE_NR").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE").setReadOnly(true);
		
		rdbArticle.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "ARTICLE_NR", "ARTICLE", "PRICE", "SELECTED" });

		rdbArticle.getRowDefinition().getColumnDefinition("DESCRIPTION").getDataType().setCellEditor(ProjXUtil.MULTILINE_EDITOR);
		
		rdbArticlecategory.setName("articlecategory");
		rdbArticlecategory.setDataSource(dataSource);
		rdbArticlecategory.setUpdateEnabled(false);
		rdbArticlecategory.setInsertEnabled(false);
		rdbArticlecategory.setDeleteEnabled(false);
		rdbArticlecategory.open();
				
		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setDataSource(dataSource);
		rdbOfferarticle.setMasterReference(new ReferenceDefinition(new String[] { "OFFER_ID" }, rdbOffer, new String[] { "ID" }));
		rdbOfferarticle.open();

		rdbOfferarticle.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "ARTICLE_NR", "ARTICLE", "NUMBEROF", "PRICE", "SUM" });

		rdbOfferarticle.getRowDefinition().getColumnDefinition("NUMBEROF").setLabel("Quantity");
		
		rdbOfferarticle.getRowDefinition().getColumnDefinition("ARTICLE_NR").setReadOnly(true);
		rdbOfferarticle.getRowDefinition().getColumnDefinition("ARTICLE").setReadOnly(true);
		rdbOfferarticle.getRowDefinition().getColumnDefinition("PRICE").setReadOnly(true);
		rdbOfferarticle.getRowDefinition().getColumnDefinition("SUM").setReadOnly(true);
		
		rdbOfferarticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);
		
		rdbArticlecategory.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "TITLE" });
	}
	
	/**
	 * Initializes the ui.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		tableOfferarticle.setPreferredSize(400, 223);
		tableOfferarticle.setDataBook(rdbOfferarticle);
		tableOfferarticle.setToolBarVisible(true);
		tableOfferarticle.setInsertVisible(false);
		tableOfferarticle.setEditVisible(false);
		tableOfferarticle.setExportVisible(false);
		
		borderLayoutOverview.setMargins(20, 20, 20, 20);
		panelOverview.setLayout(borderLayoutOverview);
		panelOverview.add(tableOfferarticle, UIBorderLayout.NORTH);
		panelOverview.add(labelInfo, UIBorderLayout.SOUTH);
		
		editArticleSearch.setDataRow(rdbArticle);
		
		labelInfo.setText("Click 'Add articles to offer' to add articles to the list.");
		labelSearch.setText("Search Articles:");
		
		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSearch, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(editArticleSearch, formLayoutSearch.getConstraints(1, 0, -1, 0));
		
		tableArticleCategory.setPreferredSize(new UIDimension(200, 190));
		tableArticleCategory.setDataBook(rdbArticlecategory);
		tableArticleCategory.setToolBarVisible(false);
		
		tableArticle.setPreferredSize(new UIDimension(450, 190));
		tableArticle.setDataBook(rdbArticle);
		tableArticle.setAutoResize(false);
		tableArticle.setToolBarVisible(false);
		
		panelInner.setLayout(formLayoutInner);
		panelInner.add(tableArticleCategory, formLayoutInner.getConstraints(0, 0));
		panelInner.add(tableArticle, formLayoutInner.getConstraints(1, 0, -1, 0));
		
		borderLayoutSelectedArticles.setMargins(10, 10, 10, 10);
		panelSelectedArticles.setLayout(borderLayoutSelectedArticles);
		panelSelectedArticles.add(panelSearch, UIBorderLayout.NORTH);
		panelSelectedArticles.add(panelInner, UIBorderLayout.CENTER);
	
		add(panelOverview, "Added Articles");
		add(panelSelectedArticles, "Add articles to offer");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Calculates the total price of all selected articles.
	 */
	public void doCalculateTotalPrice() throws Throwable
	{
		if ("N".equals(rdbOffer.getValue("FIXPRICE")))
		{
			Calc.set(new Var(rdbOffer, "TOTALPRICE"), Calc.sum(new Var(rdbOfferarticle, "SUM")));
		}
	}	
	
	/**
	 * Adds or removes articles from the selected articles.
	 * 
	 * @param pEvent
	 * @throws ModelException
	 */
	public void doAddOrRemoveArticlesFromSelectedArticles(DataRowEvent pEvent) throws ModelException
	{
		if ("Y".equals(rdbArticle.getValue("SELECTED")))
		{
			rdbOfferarticle.insert(false);
			rdbOfferarticle.setValue("ARTICLE_ID", rdbArticle.getValue("ID"));
			rdbOfferarticle.setValue("PRICE", rdbArticle.getValue("PRICE"));
			rdbOfferarticle.setValue("NUMBEROF", Integer.valueOf(1));
			rdbOfferarticle.saveSelectedRow();
		}
		else
		{
			int index = rdbOfferarticle.searchNext(new Equals("ARTICLE_ID", rdbArticle.getValue("ID")));
			rdbOfferarticle.setSelectedRow(index);
			rdbOfferarticle.delete();
		}
	}
	
	/**
	 * Sets the filter on the articles databook to filter articles only for the selected category.
	 * 
	 * @throws Throwable
	 */
	public void doFilterCategory() throws Throwable
	{
		editArticleSearch.setValue(null);

		if (rdbOffer.getValue("ID") == null)
		{
			rdbArticle.setFilter(new Equals("ARTICLECATEGORY_ID", rdbArticlecategory.getValue("ID")));
		}
		else
		{
			rdbArticle.setFilter(new And(new Equals("ARTICLECATEGORY_ID", rdbArticlecategory.getValue("ID")), new Equals("OFFER_ID", rdbOffer.getValue("ID"))));
		}

		// deselect tree
		rdbArticlecategory.setSelectedRow(-1);
	}	
	
	/**
	 * Sets the filter on the search editor.
	 * 
	 * @throws Throwable
	 */
	public void doSetFilter() throws Throwable
	{
		editArticleSearch.setAdditionalCondition(new Equals("OFFER_ID", rdbOffer.getValue("ID")));
	}
	
	/**
	 * Reloads the article data book.
	 * 
	 * @throws Throwable
	 */
	public void doReload() throws Throwable
	{
		rdbArticle.reload();
		
		doCalculateTotalPrice();
	}

} // MobileSelectArticlesTabsetPanel
