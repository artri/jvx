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

import javax.rad.model.SortDefinition;

import com.sibvisions.apps.demoerp.Session;
import com.sibvisions.rad.persist.jdbc.DBStorage;

/**
 * The Statistic class is the life-cycle object for StatisticWorkScreen.
 */
public class Statistic extends Session
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the v_statistic_order_offer_month database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the v_statistic_order_offer_month DBStorage.
	 */
	public DBStorage getV_statistic_order_offer_month() throws Exception
	{
		DBStorage dbsV_statistic_order_offer_month = (DBStorage)get("v_statistic_order_offer_month");
		if (dbsV_statistic_order_offer_month == null)
		{
			dbsV_statistic_order_offer_month = new DBStorage();
			dbsV_statistic_order_offer_month.setWritebackTable("v_statistic_order_offer_month");
			dbsV_statistic_order_offer_month.setDBAccess(getDBAccess());
			dbsV_statistic_order_offer_month.setDefaultSort(new SortDefinition("MONTH"));
			dbsV_statistic_order_offer_month.open();

			put("v_statistic_order_offer_month", dbsV_statistic_order_offer_month);
		}
		return dbsV_statistic_order_offer_month;
	}

	/**
	 * Gets the v_statistic_order_offer_year database storage.<br>
	 * 
	 * @throws Exception if the DBStorage couldn't initialized.<br>
	 * @return the v_statistic_order_offer_year DBStorage.
	 */
	public DBStorage getV_statistic_order_offer_year() throws Exception
	{
		DBStorage dbsV_statistic_order_offer_year = (DBStorage)get("v_statistic_order_offer_year");
		if (dbsV_statistic_order_offer_year == null)
		{
			dbsV_statistic_order_offer_year = new DBStorage();
			dbsV_statistic_order_offer_year.setWritebackTable("v_statistic_order_offer_year");
			dbsV_statistic_order_offer_year.setDBAccess(getDBAccess());
			dbsV_statistic_order_offer_year.setDefaultSort(new SortDefinition(true, "YEAR_"));
			dbsV_statistic_order_offer_year.open();

			put("v_statistic_order_offer_year", dbsV_statistic_order_offer_year);
		}
		return dbsV_statistic_order_offer_year;
	}

} // Statistic
