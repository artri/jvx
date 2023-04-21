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
package com.sibvisions.module.user.screens;

import java.sql.Timestamp;
import java.util.List;

import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.IBean;

import com.sibvisions.rad.persist.TriggerAPI;
import com.sibvisions.rad.persist.event.StorageEvent;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.security.AbstractSecurityManager;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.apps.demoerp.Session;

/**
 * The Users class is the life-cycle object for user management. 
 */
public class Users extends Session
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns all roles.
	 * 
	 * @return all roles.
	 * @throws Exception if no ROLES tables existing.
	 */
	public DBStorage getRoles() throws Exception
	{
		DBStorage dbs = (DBStorage)get("roles");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getDBAccess());
			dbs.setFromClause("V_USER_USER_ROLES");
			dbs.open();

			dbs.getMetaData().setPrimaryKeyColumnNames(new String[] { "ID" });

			put("roles", dbs);
		}

		return dbs;
	}

	/**
	 * Returns all Users.
	 * 
	 * @return all Users.
	 * @throws Exception if no USERS tables existing.
	 */
	public DBStorage getUsers() throws Exception
	{
		DBStorage dbs = (DBStorage)get("users");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getDBAccess());
			dbs.setWritebackTable("USERS");
			dbs.setDefaultSort(new SortDefinition("USERNAME"));
			dbs.open();
			dbs.eventBeforeInsert().addListener(this, "doUserCreated");
			dbs.eventBeforeUpdate().addListener(this, "doUserChanged");
			dbs.eventBeforeDelete().addListener(this, "doDeleteAssignedRoles");

			put("users", dbs);
		}

		return dbs;
	}

	/**
	 * Returns all Role WorkScreens.
	 * 
	 * @return all WorkScreens.
	 * @throws Exception if no ROLE_WOSC tables existing.
	 */
	public DBStorage getRoleWorkScreens() throws Exception
	{
		DBStorage dbs = (DBStorage)get("roleWorkScreens");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getDBAccess());
			dbs.setWritebackTable("ROLE_WOSC");
			dbs.open();
			dbs.eventBeforeInsert().addListener(this, "doCreated");
			dbs.eventBeforeUpdate().addListener(this, "doChanged");

			put("roleWorkScreens", dbs);
		}

		return dbs;
	}

	/**
	 * Returns all user roles.
	 * 
	 * @return all user roles.
	 * @throws Exception if no USER_ROLE tables existing.
	 */
	public DBStorage getUserRoles() throws Exception
	{
		DBStorage dbs = (DBStorage)get("userRoles");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getDBAccess());
			dbs.setWritebackTable("USER_ROLE");
			dbs.open();
			dbs.eventBeforeInsert().addListener(this, "doCreated");
			dbs.eventBeforeUpdate().addListener(this, "doChanged");

			put("userRoles", dbs);
		}

		return dbs;
	}

	/**
	 * Returns all WorkScreens.
	 * 
	 * @return all WorkScreens.
	 * @throws Exception if no workscreens tables existing.
	 */
	public DBStorage getWorkScreens() throws Exception
	{
		DBStorage dbs = (DBStorage)get("workScreens");

		if (dbs == null)
		{
			dbs = new DBStorage();
			dbs.setDBAccess(getDBAccess());
			dbs.setWritebackTable("WORKSCREENS");
			dbs.open();

			put("workScreens", dbs);
		}

		return dbs;
	}

	/**
	 * Encrypts a password, if password is changed.
	 * 
	 * @param pEvent the storage event
	 * @throws Exception if encryption or data change fails
	 */
	private void encryptPwd(StorageEvent pEvent) throws Exception
	{
		IBean bn = pEvent.getNew();

		String sNew = (String)bn.get("PASSWORD");
		String sOld;

		IBean bnOld = pEvent.getOld();

		if (bnOld != null)
		{
			sOld = (String)bnOld.get("PASSWORD");
		}
		else
		{
			sOld = null;
		}

		if (!CommonUtil.equals(sOld, sNew))
		{
			bn.put("PASSWORD", AbstractSecurityManager.getEncryptedPassword(SessionContext.getCurrentSessionConfig(), sNew));
		}
	}

	// Actions
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the current User and date for CREATED_XXX + CHANGED_XXX.
	 * 
	 * @param pEvent the Storage event.
	 */
	public void doCreated(StorageEvent pEvent)
	{
		IBean newRow = pEvent.getNew();

		String sUser = TriggerAPI.getCurrentUserName();
		Timestamp timestamp = TriggerAPI.getCurrentTimestamp();

		newRow.put("CREATED_BY", sUser);
		newRow.put("CREATED_ON", timestamp);
		newRow.put("CHANGED_BY", sUser);
		newRow.put("CHANGED_ON", timestamp);
	}

	/**
	 * Sets the current User and date for CHANGED_XXX.
	 * 
	 * @param pEvent the Storage event.
	 */
	public void doChanged(StorageEvent pEvent)
	{
		IBean newRow = pEvent.getNew();

		newRow.put("CHANGED_BY", TriggerAPI.getCurrentUserName());
		newRow.put("CHANGED_ON", TriggerAPI.getCurrentTimestamp());
	}

	/**
	 * Sets the encrypted password.
	 * 
	 * @param pEvent the storage event
	 * @throws Exception if data change fails
	 */
	public void doUserChanged(StorageEvent pEvent) throws Exception
	{
		doChanged(pEvent);

		encryptPwd(pEvent);
	}

	/**
	 * Sets the encrypted password.
	 * 
	 * @param pEvent the storage event
	 * @throws Exception if data change fails
	 */
	public void doUserCreated(StorageEvent pEvent) throws Exception
	{
		doCreated(pEvent);

		encryptPwd(pEvent);
	}

	/**
	 * Deletes all assigned roles, to the current user.
	 * 
	 * @param pEvent the Storage event.
	 * @throws Exception if the roles in the USER_ROLE tables couln't deleted.
	 */
	public void doDeleteAssignedRoles(StorageEvent pEvent) throws Exception
	{
		IBean oldRow = pEvent.getOld();

		DBStorage userRoles = getUserRoles();
		List<IBean> roles = userRoles.fetchBean(new Equals("USER_ID", oldRow.get("ID")), null, 0, Integer.MAX_VALUE);

		for (int i = 0; i < roles.size(); i++)
		{
			userRoles.delete(roles.get(i));
		}
	}

} // Users
