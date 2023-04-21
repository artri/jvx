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
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITree;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.condition.Equals;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.IImage;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.apps.components.FilterEditor;
import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import javax.rad.genui.UIInsets;
import java.lang.String;

/**
 * The ArticleWorkScreen shows the article categories in a tree and all articles
 * of this category in a table. It is possible to add, update or delete
 * articles.
 */
public class ArticleWorkScreen extends DataSourceWorkScreen implements INodeFormatter
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Folder Image.
	 */
	private static final UIImage	FOLDER						= UIImage.getImage("/com/sibvisions/apps/demoerp/images/folder.png");

	/**
	 * Open folder Image.
	 */
	private static final UIImage	FOLDER_OPEN					= UIImage.getImage("/com/sibvisions/apps/demoerp/images/folder_open.png");

	/**
	 * treeArticleCategory.
	 */
	private UITree					treeArticleCategory			= new UITree();

	/**
	 * editArticleArticleNr.
	 */
	private UIEditor				editArticleArticleNr		= new UIEditor();

	/**
	 * editArticleArticle.
	 */
	private UIEditor				editArticleArticle			= new UIEditor();

	/**
	 * editArticleDescription.
	 */
	private UIEditor				editArticleDescription		= new UIEditor();

	/**
	 * editArticlePrice.
	 */
	private UIEditor				editArticlePrice			= new UIEditor();

	/**
	 * editArticleUnitUnit.
	 */
	private UIEditor				editArticleUnitUnit			= new UIEditor();

	/**
	 * tableArticle.
	 */
	private NavigationTable			tableArticle				= new NavigationTable();

	/**
	 * buttonExport.
	 */
	private UIButton				buttonExport				= new UIButton();

	/**
	 * editSearch.
	 */
	private FilterEditor			editSearch					= new FilterEditor();

	/**
	 * labelSearch.
	 */
	private UILabel					labelSearch					= new UILabel();

	/**
	 * labelArticleNr.
	 */
	private UILabel					labelArticleNr				= new UILabel();

	/**
	 * labelArticle.
	 */
	private UILabel					labelArticle				= new UILabel();

	/**
	 * labelDescription.
	 */
	private UILabel					labelDescription			= new UILabel();

	/**
	 * labelPrice.
	 */
	private UILabel					labelPrice					= new UILabel();

	/**
	 * labelEuro.
	 */
	private UILabel					labelEuro					= new UILabel();

	/**
	 * labelUnit.
	 */
	private UILabel					labelUnit					= new UILabel();

	/**
	 * formLayoutSearch.
	 */
	private UIFormLayout			formLayoutSearch			= new UIFormLayout();

	/**
	 * groupPanelArticle.
	 */
	private UIGroupPanel			groupPanelArticle			= new UIGroupPanel();

	/**
	 * groupPanelArticleCategory.
	 */
	private UIGroupPanel			groupPanelArticleCategory	= new UIGroupPanel();

	/**
	 * groupPanelArticleDetail.
	 */
	private UIGroupPanel			groupPanelArticleDetail		= new UIGroupPanel();

	/**
	 * panelArticles.
	 */
	private UIPanel					panelArticles				= new UIPanel();

	/**
	 * formLayoutArticles.
	 */
	private UIFormLayout			formLayoutArticles			= new UIFormLayout();

	/**
	 * Its a article remote databook.
	 */
	private RemoteDataBook			rdbArticle					= new RemoteDataBook();

	/**
	 * Its a offerarticle remote databook.
	 */
	private RemoteDataBook			rdbOfferarticle				= new RemoteDataBook();

	/**
	 * Its a articlecategory remote databook.
	 */
	private RemoteDataBook			rdbArticlecategory			= new RemoteDataBook();

	/**
	 * borderLayoutMain.
	 */
	private UIBorderLayout			borderLayoutMain			= new UIBorderLayout();

	/**
	 * borderLayoutFirst.
	 */
	private UIBorderLayout			borderLayoutFirst			= new UIBorderLayout();

	/**
	 * borderLayoutSecond.
	 */
	private UIBorderLayout			borderLayoutSecond			= new UIBorderLayout();

	/**
	 * borderLayoutArticle.
	 */
	private UIFormLayout			formLayoutArticle			= new UIFormLayout();

	/**
	 * formLayoutArticleDetail.
	 */
	private UIFormLayout			formLayoutArticleDetail		= new UIFormLayout();

	/**
	 * borderLayoutArticleCategory.
	 */
	private UIFormLayout			formLayoutArticleCategory	= new UIFormLayout();

	/**
	 * splitPanelMain.
	 */
	private UISplitPanel			splitPanelMain				= new UISplitPanel();

	/**
	 * splitPanelMainFirst.
	 */
	private UIPanel					splitPanelMainFirst			= new UIPanel();

	/**
	 * splitPanelMainSecond.
	 */
	private UIPanel					splitPanelMainSecond		= new UIPanel();

	/**
	 * panelSearch.
	 */
	private UIPanel					panelSearch					= new UIPanel();

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
	public ArticleWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Throwable
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();

		rdbArticle.eventAfterInserted().addListener(this, "doCreateArticleNr");

		// deselect tree
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
		rdbArticle.setDataSource(getDataSource());
		rdbArticle.open();

		rdbArticle.getRowDefinition().getColumnDefinition("DESCRIPTION").getDataType().setCellEditor(ProjXUtil.MULTILINE_EDITOR);
		rdbOfferarticle.setName("offerarticle");
		rdbOfferarticle.setMasterReference(new ReferenceDefinition(new String[] { "ARTICLE_ID" }, rdbArticle, new String[] { "ID" }));
		rdbOfferarticle.setDataSource(getDataSource());
		rdbOfferarticle.open();

		rdbArticlecategory.setName("articlecategory");
		rdbArticlecategory.setDataSource(getDataSource());
		rdbArticlecategory.setMasterReference(new ReferenceDefinition(new String[] { "ARTICLECATEGORY_ID" }, rdbArticlecategory, new String[] { "ID" }));
		rdbArticlecategory.open();

		rdbOfferarticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbArticle.eventAfterInserting().addListener(this, "doAfterInserting");

		rdbArticle.getRowDefinition().getColumnDefinition("ARTICLE_NR").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("CURRENCY_CURRENCY").setReadOnly(true);
		rdbArticle.getRowDefinition().getColumnDefinition("PRICE").getDataType().setCellEditor(ProjXUtil.CURRENCY);

		rdbArticlecategory.eventAfterRowSelected().addListener(this, "doFilterCategory");

		rdbArticle.getRowDefinition().setColumnView(ITableControl.class, new ColumnView(new String[] { "ARTICLE_NR", "ARTICLE", "PRICE" }));

	}

	/**
	 * Initializes the UI.
	 * 
	 * @throws Throwable if the initialization throws an error
	 */
	private void initializeUI() throws Throwable
	{
		labelSearch.setText("Search");

		panelSearch.setLayout(formLayoutSearch);
		panelSearch.add(labelSearch, formLayoutSearch.getConstraints(0, 0));
		panelSearch.add(editSearch, formLayoutSearch.getConstraints(1, 0, -1, 0));

		editSearch.setDataRow(rdbArticle);

		groupPanelArticleCategory.setText("Article Category");

		groupPanelArticleCategory.setLayout(formLayoutArticleCategory);
		groupPanelArticleCategory.add(treeArticleCategory, formLayoutArticleCategory.getConstraints(0, 0, -1, -1));
		treeArticleCategory.setNodeFormatter(this);

		borderLayoutFirst.setMargins(new UIInsets(5, 5, 5, 5));
		splitPanelMainFirst.setLayout(borderLayoutFirst);
		splitPanelMainFirst.add(groupPanelArticleCategory, UIBorderLayout.CENTER);

		groupPanelArticle.setText("Articles");
		groupPanelArticle.setLayout(formLayoutArticle);
		groupPanelArticle.add(panelSearch, formLayoutArticle.getConstraints(0, 0, -1, 0));
		groupPanelArticle.add(tableArticle, formLayoutArticle.getConstraints(0, 1, -1, -2));
		groupPanelArticle.add(buttonExport, formLayoutArticle.getConstraints(-1, -1));

		groupPanelArticleDetail.setLayout(formLayoutArticleDetail);
		groupPanelArticleDetail.setText("Article Detail");
		groupPanelArticleDetail.setPreferredSize(new UIDimension(281, 219));
		groupPanelArticleDetail.add(labelArticleNr, formLayoutArticleDetail.getConstraints(0, 0));
		groupPanelArticleDetail.add(editArticleArticleNr, formLayoutArticleDetail.getConstraints(1, 0));
		groupPanelArticleDetail.add(labelArticle, formLayoutArticleDetail.getConstraints(0, 1));
		groupPanelArticleDetail.add(editArticleArticle, formLayoutArticleDetail.getConstraints(1, 1, -1, 1));
		groupPanelArticleDetail.add(labelDescription, formLayoutArticleDetail.getConstraints(0, 2));
		groupPanelArticleDetail.add(editArticleDescription, formLayoutArticleDetail.getConstraints(1, 2, -1, 2));
		groupPanelArticleDetail.add(labelPrice, formLayoutArticleDetail.getConstraints(0, 3));
		groupPanelArticleDetail.add(editArticlePrice, formLayoutArticleDetail.getConstraints(1, 3));
		groupPanelArticleDetail.add(labelEuro, formLayoutArticleDetail.getConstraints(2, 3));
		groupPanelArticleDetail.add(labelUnit, formLayoutArticleDetail.getConstraints(0, 4));
		groupPanelArticleDetail.add(editArticleUnitUnit, formLayoutArticleDetail.getConstraints(1, 4));

		buttonExport.setText("Export Articles to Webshop");
		buttonExport.eventAction().addListener(this, "doExportArticlesToWebshop");
		buttonExport.setImageTextGap(2);
		buttonExport.setImage(UIImage.getImage("/com/sibvisions/apps/demoerp/images/export_database2.png"));

		labelArticleNr.setText("Article Nr");

		labelArticle.setText("Article");

		labelDescription.setText("Description");
		labelDescription.setVerticalAlignment(0);

		labelPrice.setText("Price");

		labelEuro.setText("Euro");

		labelUnit.setText("Unit");

		formLayoutArticleDetail.setAnchorConfiguration("r1=160");

		formLayoutArticles.setAnchorConfiguration("t-1=-246");
		formLayoutArticles.setMargins(new UIInsets(5, 5, 10, 5));

		formLayoutSearch.setMargins(new UIInsets(0, 0, 10, 0));

		formLayoutArticle.setAnchorConfiguration("b-2=-15");

		tableArticle.setAutoResize(false);
		tableArticle.setDataBook(rdbArticle);
		tableArticle.setPreferredSize(new UIDimension(400, 350));

		editArticleArticleNr.setColumnName("ARTICLE_NR");
		editArticleArticleNr.setDataRow(rdbArticle);

		editArticleArticle.setColumnName("ARTICLE");
		editArticleArticle.setDataRow(rdbArticle);

		editArticleDescription.setColumnName("DESCRIPTION");
		editArticleDescription.setDataRow(rdbArticle);

		editArticlePrice.setColumnName("PRICE");
		editArticlePrice.setDataRow(rdbArticle);

		editArticleUnitUnit.setColumnName("UNIT_UNIT");
		editArticleUnitUnit.setDataRow(rdbArticle);

		treeArticleCategory.setDataBooks(new IDataBook[] { rdbArticlecategory });

		panelArticles.setLayout(formLayoutArticles);
		panelArticles.add(groupPanelArticle, formLayoutArticles.getConstraints(0, 0, -1, -2));
		panelArticles.add(groupPanelArticleDetail, formLayoutArticles.getConstraints(0, -1, -1, -1));

		splitPanelMainSecond.setLayout(borderLayoutSecond);
		splitPanelMainSecond.add(panelArticles, UIBorderLayout.CENTER);

		splitPanelMain.setDividerPosition(266);

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
	 * Downloads the created list report.
	 */
	public void doExportArticlesToWebshop(UIActionEvent pEvent) throws Throwable
	{
		getApplication().getLauncher().showFileHandle(getListReportArticle());
	}

	/**
	 * Erstellt einen List Report Article.
	 */
	public IFileHandle getListReportArticle() throws Throwable
	{
		save();
		return (IFileHandle)getConnection().callAction("createListReportArticle", rdbArticle.getFilter(), rdbArticle.getSort());
	}

} // ArticleWorkScreen
