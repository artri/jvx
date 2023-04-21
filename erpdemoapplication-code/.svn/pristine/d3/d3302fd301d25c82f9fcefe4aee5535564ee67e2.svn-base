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

import com.sibvisions.apps.demoerp.Session;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.io.IFileHandle;
import java.io.File;
import java.io.FileInputStream;
import javax.rad.io.RemoteFileHandle;
import javax.rad.server.SessionContext;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.report.AbstractWorker;
import com.sibvisions.report.persist.StorageNode;
import com.sibvisions.report.bean.ListNode;

/**
 * The Article class is the life-cycle object for ArticleWorkScreen.
 */
public class Article extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the offerarticle database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the offerarticle DBStorage.
	 */
	public DBStorage getOfferarticle() throws Exception
	{
		DBStorage dbsOfferarticle = (DBStorage)get("offerarticle");
		if (dbsOfferarticle == null)
		{
			dbsOfferarticle = new DBStorage();
			dbsOfferarticle.setWritebackTable("offerarticle");
			dbsOfferarticle.setDBAccess(getDBAccess());
			dbsOfferarticle.open();

			put("offerarticle", dbsOfferarticle);
		}
		return dbsOfferarticle;
	}

	/**
	 * Gets the article database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the article DBStorage.
	 */
	public DBStorage getArticle() throws Exception
	{
		DBStorage dbsArticle = (DBStorage)get("article");
		if (dbsArticle == null)
		{
			dbsArticle = new DBStorage();
			dbsArticle.setWritebackTable("article");
			dbsArticle.setDBAccess(getDBAccess());
			dbsArticle.open();

			put("article", dbsArticle);
		}
		return dbsArticle;
	}

	/**
	 * Gets the articlecategory database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the articlecategory DBStorage.
	 */
	public DBStorage getArticlecategory() throws Exception
	{
		DBStorage dbsArticlecategory = (DBStorage)get("articlecategory");
		if (dbsArticlecategory == null)
		{
			dbsArticlecategory = new DBStorage();
			dbsArticlecategory.setWritebackTable("articlecategory");
			dbsArticlecategory.setDBAccess(getDBAccess());
			dbsArticlecategory.open();

			put("articlecategory", dbsArticlecategory);
		}
		return dbsArticlecategory;
	}

	/**
	 * Erstellt einen List Report Article.
	 */
	public IFileHandle createListReportArticle(ICondition pFilter, SortDefinition pSort) throws Exception
	{
		ListNode lsnMaster = new ListNode();
		lsnMaster.setNullValue("");
		StorageNode snList = new StorageNode(getArticle());
		snList.setNullValue("");
		snList.setSort(pSort);
		snList.setFilter(pFilter);
		lsnMaster.setLoopDefinition("article", snList);
		AbstractWorker worker = AbstractWorker.getWorker("ArticleWorkScreen$ArticleList.xls");
		worker.loadDocument(new FileInputStream(new File(Configuration.getApplicationZone(SessionContext.getCurrentSession().getApplicationName()).getDirectory(),
				"/reports/screens/ArticleWorkScreen$ArticleList.xls")));
		worker.fillInData(lsnMaster);
		RemoteFileHandle rfh = new RemoteFileHandle("ArticleList.xls");
		worker.saveDocument(rfh.getOutputStream());
		return rfh;
	}

} // Article
