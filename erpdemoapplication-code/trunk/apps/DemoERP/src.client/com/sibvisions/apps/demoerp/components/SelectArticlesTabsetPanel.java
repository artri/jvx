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

import java.math.BigDecimal;

import javax.rad.genui.UIDimension;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.control.UITree;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IDataSource;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.Equals;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IImage;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.util.SilentAbortException;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.util.Calc;
import com.sibvisions.apps.util.Var;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 *  A tabset panel to select articles and to show the selected articles in a table.
 */
public class SelectArticlesTabsetPanel extends UITabsetPanel implements INodeFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** 
	 * Folder Image. 
	 */
	private static final UIImage FOLDER 			= UIImage.getImage("/com/sibvisions/apps/demoerp/images/folder.png"); 
	
	/** 
	 * Folder Image. 
	 */
	private static final UIImage FOLDER_OPEN 		= UIImage.getImage("/com/sibvisions/apps/demoerp/images/folder_open.png"); 
	
	
	/**
	 * labelSuchen.
	 */
	private UILabel			labelSearch				= new UILabel();
	
	/**
	 * labelInfo.
	 */
	private UILabel			labelInfo 				= new UILabel();
	
	/**
	 * editArticlenull.
	 */
	private FilterEditor	editArticleSearch		= new FilterEditor();
	
	/**
	 * treeArticleCategory.
	 */
	private UITree			treeArticleCategory		= new UITree();

	/**
	 * panelInner
	 */
	private UIPanel panelInner 						= new UIPanel();
	
	/**
	 * panelOverview.
	 */
	private UIPanel	panelOverview					= new UIPanel();
	
	/**
	 * panelSelectedArticles.
	 */
	private UIPanel	panelSelectedArticles			= new UIPanel();
	
	/**
	 * panelSearch.
	 */
	private UIPanel	panelSearch						= new UIPanel();
	
	/**
	 * tableArticle.
	 */
	private NavigationTable	tableArticle			= new NavigationTable();
	
	/**
	 * tableOfferarticle.
	 */
	private NavigationTable	tableOfferarticle		= new NavigationTable();
	
	/**
	 * borderLayoutOverview.
	 */
	private UIBorderLayout	borderLayoutOverview	= new UIBorderLayout();
	
	/**
	 * borderLayoutSelectedArticles.
	 */
	private UIBorderLayout	borderLayoutSelectedArticles	= new UIBorderLayout();
	
	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout	formLayoutSearch				= new UIFormLayout();
	
	/**
	 * flowLayoutInner.
	 */
	private UIFormLayout formLayoutInner 			= new UIFormLayout();
	
	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook	rdbOfferarticle			= new RemoteDataBook();
	
	/**
	 * Its a articlecategory remote databook.
	 */
	private RemoteDataBook	rdbArticlecategory		= new RemoteDataBook();
	
	/**
	 * Its a article remote databook.
	 */
	private RemoteDataBook	rdbArticle				= new RemoteDataBook();
	
	/**
	 * Its a offer remote databook.
	 */
	private RemoteDataBook	rdbOffer;
	
	/**
	 * The dataSource.
	 */
	private IDataSource dataSource;
	
	/**
	 * Is add command.
	 */
	private boolean isAdd = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * New Instance
	 * 
	 * @throws Throwable 
	 */
	public SelectArticlesTabsetPanel(IDataSource pDataSource, RemoteDataBook pRdbOffer) throws Throwable
	{
		dataSource = pDataSource;
		rdbOffer = pRdbOffer;
		
		initializeModel();
		initializeUI();
		
		rdbArticle.eventValuesChanged().addListener(this, "doAddOrRemoveArticlesFromSelectedArticles");
		rdbArticlecategory.eventAfterRowSelected().addListener(this, "doFilterCategory");
		rdbOfferarticle.eventValuesChanged().addListener(this, "doCalculateTotalPrice");
		rdbOfferarticle.eventAfterDeleted().addListener(this, "doReload");
		rdbOfferarticle.eventBeforeInserting().addListener(this, "doSetAddTabsetPanel");
		
		rdbArticlecategory.setSelectedRow(-1);
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
		rdbArticle.setFilter(new Equals("ID", BigDecimal.valueOf(-1)));
		rdbArticle.open();

		rdbArticle.getRowDefinition().getColumnDefinition("SELECTED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE_NR").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("PRICE").setReadOnly(true);
		
		rdbArticle.getRowDefinition().getColumnDefinition("SELECTED").setLabel("Add to offer");
		rdbArticle.getRowDefinition().getColumnDefinition("SELECTED").setWidth(100);
		
		rdbArticle.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "SELECTED", "ARTICLE_NR", "ARTICLE", "PRICE" });

		rdbArticle.getRowDefinition().getColumnDefinition("DESCRIPTION").getDataType().setCellEditor(ProjXUtil.MULTILINE_EDITOR);
		
		rdbArticlecategory.setName("articlecategory");
		rdbArticlecategory.setDataSource(dataSource);
		rdbArticlecategory.setMasterReference(new ReferenceDefinition(new String[] { "ARTICLECATEGORY_ID" }, rdbArticlecategory, new String[] { "ID" }));
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
		tableOfferarticle.setExportVisible(false);
		tableOfferarticle.setEditVisible(false);
		
		
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
		
		treeArticleCategory.setPreferredSize(new UIDimension(200, 190));
		treeArticleCategory.setDataBooks(new IDataBook[] { rdbArticlecategory });
		treeArticleCategory.setNodeFormatter(this);
		
		tableArticle.setPreferredSize(new UIDimension(450, 190));
		tableArticle.setDataBook(rdbArticle);
		tableArticle.setAutoResize(false);
		tableArticle.setToolBarVisible(false);
		
		panelInner.setLayout(formLayoutInner);
		panelInner.add(treeArticleCategory, formLayoutInner.getConstraints(0, 0));
		panelInner.add(tableArticle, formLayoutInner.getConstraints(1, 0, -1, 0));
		
		borderLayoutSelectedArticles.setMargins(10, 10, 10, 10);
		panelSelectedArticles.setLayout(borderLayoutSelectedArticles);
		panelSelectedArticles.add(panelSearch, UIBorderLayout.NORTH);
		panelSelectedArticles.add(panelInner, UIBorderLayout.CENTER);
	
		add(panelOverview, "Added Articles");
		add(panelSelectedArticles, "Add articles to offer");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IImage getNodeImage(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, boolean pExpanded, boolean pLeaf)
	{
		if (pExpanded)
		{
			return FOLDER_OPEN;
		}
		else
		{
			return FOLDER;
		}
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
			isAdd = true;
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
	public void setOffer(BigDecimal pId) throws Throwable
	{
		editArticleSearch.setAdditionalCondition(new Equals("OFFER_ID", pId));
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
	
	/**
	 * Selects the second tab.
	 *  
	 * @throws Throwable
	 */
	public void doSetAddTabsetPanel() throws Throwable
	{
		setSelectedIndex(1);
		
		if (isAdd)
		{
			isAdd = false;
		}
		else
		{
			throw new SilentAbortException(); // Ignore inserted.
		}
	}

} // SelectArticlesTabsetPanel
