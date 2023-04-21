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

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.server.SessionContext;

import com.sibvisions.apps.demoerp.Session;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.report.AbstractWorker;
import com.sibvisions.report.bean.ListNode;
import com.sibvisions.report.persist.DynamicValue;
import com.sibvisions.report.persist.StorageNode;

/**
 * The Order class is the life-cycle object for OrderWorkScreen.
 */
public class Order extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the offer database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the offer DBStorage.
	 */
	public DBStorage getOffer() throws Exception
	{
		DBStorage dbsOffer = (DBStorage)get("offer");
		if (dbsOffer == null)
		{
			dbsOffer = new DBStorage();
			dbsOffer.setWritebackTable("offer");
			dbsOffer.setDBAccess(getDBAccess());
			dbsOffer.open();

			put("offer", dbsOffer);
		}
		return dbsOffer;
	}

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
			dbsOfferarticle.setFromClause("v_offerarticle");
			dbsOfferarticle.setDBAccess(getDBAccess());
			dbsOfferarticle.open();

			put("offerarticle", dbsOfferarticle);
		}
		return dbsOfferarticle;
	}

	/**
	 * Gets the customer database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the customer DBStorage.
	 */
	public DBStorage getCustomer() throws Exception
	{
		DBStorage dbsCustomer = (DBStorage)get("customer");
		if (dbsCustomer == null)
		{
			dbsCustomer = new DBStorage();
			dbsCustomer.setWritebackTable("customer");
			dbsCustomer.setDBAccess(getDBAccess());
			dbsCustomer.open();

			put("customer", dbsCustomer);
		}
		return dbsCustomer;
	}

	/**
	 * Gets the order_ database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the order_ DBStorage.
	 */
	public DBStorage getOrder_() throws Exception
	{
		DBStorage dbsOrder_ = (DBStorage)get("order_");
		if (dbsOrder_ == null)
		{
			dbsOrder_ = new DBStorage();
			dbsOrder_.setWritebackTable("order_");
			dbsOrder_.setFromClause("v_order_");
			dbsOrder_.setDBAccess(getDBAccess());
			dbsOrder_.setDefaultSort(new SortDefinition(false, "ID"));
			dbsOrder_.open();

			put("order_", dbsOrder_);
		}
		return dbsOrder_;
	}

	/**
	 * createBillReporte.
	 */
	public IFileHandle createBillReport(ICondition pMasterFilter, SortDefinition pMasterSort, ICondition pDetailFilter, SortDefinition pDetailSort,
			BigDecimal pId) throws Exception
	{
		ListNode lsnGroup = new ListNode();
		StorageNode snMaster = new StorageNode(getOrder_());
		snMaster.setNullValue("");
		snMaster.setSort(pMasterSort);
		ICondition condMasterFilter;
		condMasterFilter = new Equals("ID", pId);
		if (pMasterFilter != null)
		{
			condMasterFilter = condMasterFilter.and(pMasterFilter);
		}
		snMaster.setFilter(condMasterFilter);
		StorageNode snDetail = new StorageNode(getOfferarticle());
		snDetail.setNullValue("");
		snDetail.setSort(pDetailSort);
		ICondition condDetailFilter;
		condDetailFilter = new Equals("OFFER_ID", new DynamicValue("OFFER_ID"));
		if (pDetailFilter != null)
		{
			condDetailFilter = condDetailFilter.and(pDetailFilter);
		}
		snDetail.setFilter(condDetailFilter);
		lsnGroup.setLoopDefinition("order_", snMaster);
		snMaster.setLoopDefinition("offerarticle", snDetail);
		AbstractWorker worker = AbstractWorker.getWorker("OrderWorkScreen$Bill.rtf");
		worker.loadDocument(new FileInputStream(new File(Configuration.getApplicationZone(SessionContext.getCurrentSession().getApplicationName()).getDirectory(),
				"/reports/screens/OrderWorkScreen$Bill.rtf")));
		worker.fillInData(lsnGroup);

		RemoteFileHandle rfh = new RemoteFileHandle("Bill.rtf");
		worker.saveDocument(rfh.getOutputStream());
		return rfh;
	}

} // Order
