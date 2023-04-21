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

import javax.rad.application.IWorkScreenApplication;
import javax.rad.genui.UIDimension;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ITableControl;
import javax.rad.remote.AbstractConnection;
import javax.rad.ui.IAlignmentConstants;

import com.sibvisions.apps.components.NavigationTable;
import com.sibvisions.apps.projx.ProjXUtil;
import com.sibvisions.apps.projx.screens.DataSourceWorkScreen;
import com.sibvisions.rad.model.remote.RemoteDataBook;

/**
 * The UsersWorkScreen manages application users.
 */
public class UsersWorkScreen extends DataSourceWorkScreen
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Users DataBook. */
	private RemoteDataBook	rdbUsers			= new RemoteDataBook();

	/** Roles DataBook. */
	private RemoteDataBook	rdbRoles			= new RemoteDataBook();

	/** UserRole DataBook. */
	private RemoteDataBook	rdbUserRole			= new RemoteDataBook();

	/** Users Navigation table. */
	private NavigationTable	navUsers			= new NavigationTable();

	/** Roles Navigation table. */
	private UITable			navRoles			= new UITable();

	/** Label for username. */
	private UILabel			labUsername			= new UILabel();

	/** Editor for username. */
	private UIEditor		editUsername		= new UIEditor();

	/** Label for active. */
	private UILabel			labActive			= new UILabel();

	/** Editor for active. */
	private UIEditor		editActive			= new UIEditor();

	/** Label for password. */
	private UILabel			labPassword			= new UILabel();

	/** Editor for password. */
	private UIEditor		editPassword		= new UIEditor();

	/** Label for ChangePassword. */
	private UILabel			labChangePassword	= new UILabel();

	/** Editor for ChangePassword. */
	private UIEditor		editChangePassword	= new UIEditor();

	/** Label for firstname. */
	private UILabel			labFirstName		= new UILabel();

	/** Editor for firstname. */
	private UIEditor		editFirstName		= new UIEditor();

	/** Label for lastname. */
	private UILabel			labLastName			= new UILabel();

	/** Editor for lastname. */
	private UIEditor		editLastName		= new UIEditor();

	/** Label for email. */
	private UILabel			labEmail			= new UILabel();

	/** Editor for email. */
	private UIEditor		editEmail			= new UIEditor();

	/** Label for Phone. */
	private UILabel			labPhone			= new UILabel();

	/** Editor for Phone. */
	private UIEditor		editPhone			= new UIEditor();

	/** Label for Fax. */
	private UILabel			labFax				= new UILabel();

	/** Editor for Fax. */
	private UIEditor		editFax				= new UIEditor();

	/** The Border Layout for the container. */
	private UIBorderLayout	layoutThis			= new UIBorderLayout();

	/** Main Split Panel. */
	private UISplitPanel	spanMain			= new UISplitPanel();

	/** The Layout for users container. */
	private UIFormLayout	layUsers			= new UIFormLayout();

	/** The Panel for the users. */
	private UIPanel			panUsers			= new UIPanel();

	/** The Layout for details. */
	private UIFormLayout	layDetails			= new UIFormLayout();

	/** The Panel for details. */
	private UIPanel			panDetails			= new UIPanel();

	/** The Layout for details. */
	private UIFormLayout	layDetail			= new UIFormLayout();

	/** The Panel for detail. */
	private UIGroupPanel	groupDetail			= new UIGroupPanel();

	/** The Layout for the roles. */
	private UIFormLayout	layRoles			= new UIFormLayout();

	/** The Panel for the roles. */
	private UIGroupPanel	groupRoles			= new UIGroupPanel();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Construct the <code>UsersWorkScreen</code>.
	 * 
	 * @param pApplication the application.
	 * @param pConnection the connection
	 * @throws Exception if the init fails
	 */
	public UsersWorkScreen(IWorkScreenApplication pApplication, AbstractConnection pConnection) throws Exception
	{
		super(pApplication, pConnection);

		initializeModel();
		initializeUI();
	}

	/**
	 * Initialize model.
	 * 
	 * @throws Exception if the model couldn't initialized.
	 */
	private void initializeModel() throws Exception
	{
		rdbUsers.setDataSource(getDataSource());
		rdbUsers.setName("users");
		rdbUsers.open();
		rdbUsers.getRowDefinition().getColumnView(ITableControl.class).setColumnNames(new String[] { "USERNAME", "ACTIVE", "EMAIL" });
		rdbUsers.getRowDefinition().getColumnDefinition("ACTIVE").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);
		rdbUsers.getRowDefinition().getColumnDefinition("CHANGE_PASSWORD").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbRoles.setDataSource(getDataSource());
		rdbRoles.setName("roles");
		rdbRoles.setMasterReference(new ReferenceDefinition(new String[] { "USER_ID" }, rdbUsers, new String[] { "ID" }));
		rdbRoles.setWritebackEnabled(false);
		rdbRoles.open();
		rdbRoles.setInsertEnabled(false);
		rdbRoles.setDeleteEnabled(false);
		rdbRoles.eventValuesChanged().addListener(this, "doRelateUsers");

		rdbRoles.getRowDefinition().getColumnDefinition("NAME").setLabel("Role");
		rdbRoles.getRowDefinition().getColumnDefinition("NAME").setReadOnly(true);

		rdbRoles.getRowDefinition().getColumnDefinition("ROLE_GRANTED").getDataType().setCellEditor(ProjXUtil.YESNO_EDITOR);

		rdbUserRole.setDataSource(getDataSource());
		rdbUserRole.setName("userRoles");
		rdbUserRole.setMasterReference(new ReferenceDefinition(new String[] { "USER_ID", "ROLE_ID" }, rdbRoles, new String[] { "USER_ID", "ID" }));
		rdbUserRole.open();
	}

	/**
	 * Initialize UI.
	 * 
	 * @throws Exception if the UI couldn't initialized.
	 */
	private void initializeUI() throws Exception
	{
		navUsers.setDataBook(rdbUsers);
		navUsers.setMaximumSize(new UIDimension(Integer.MAX_VALUE, 400));

		layUsers.setMargins(7, 2, 5, 5);
		panUsers.setLayout(layUsers);
		panUsers.add(navUsers, layUsers.getConstraints(0, 0, -1, -1));

		labUsername.setText("User name");
		editUsername.setDataRow(rdbUsers);
		editUsername.setColumnName("USERNAME");

		labActive.setText("Active");
		editActive.setDataRow(rdbUsers);
		editActive.setColumnName("ACTIVE");

		labPassword.setText("Password");
		editPassword.setCellEditor(ProjXUtil.PASSWORD_EDITOR);
		editPassword.setDataRow(rdbUsers);
		editPassword.setColumnName("PASSWORD");

		labChangePassword.setText("Password expired");
		editChangePassword.setDataRow(rdbUsers);
		editChangePassword.setColumnName("CHANGE_PASSWORD");

		labFirstName.setText("First name");
		editFirstName.setDataRow(rdbUsers);
		editFirstName.setColumnName("FIRST_NAME");

		labLastName.setText("Last name");
		editLastName.setDataRow(rdbUsers);
		editLastName.setColumnName("LAST_NAME");

		labEmail.setText("Email");
		editEmail.setDataRow(rdbUsers);
		editEmail.setColumnName("EMAIL");

		labPhone.setText("Phone");
		editPhone.setDataRow(rdbUsers);
		editPhone.setColumnName("PHONE");

		labFax.setText("Fax");
		editFax.setDataRow(rdbUsers);
		editFax.setColumnName("FAX");

		navRoles.setDataBook(rdbRoles);
		navRoles.setTableHeaderVisible(false);
		navRoles.setPreferredSize(450, 300);

		groupDetail.setText("User details");
		groupDetail.setLayout(layDetail);
		groupDetail.add(labUsername, layDetail.getConstraints(0, 0));
		groupDetail.add(editUsername, layDetail.getConstraints(1, 0, 2, 0));
		groupDetail.add(labActive, layDetail.getConstraints(-2, 0));
		groupDetail.add(editActive, layDetail.getConstraints(-1, 0));
		groupDetail.add(labPassword, layDetail.getConstraints(0, 1));
		groupDetail.add(editPassword, layDetail.getConstraints(1, 1, 2, 1));
		groupDetail.add(labChangePassword, layDetail.getConstraints(-2, 1));
		groupDetail.add(editChangePassword, layDetail.getConstraints(-1, 1));
		groupDetail.add(labEmail, layDetail.getConstraints(0, 2));
		groupDetail.add(editEmail, layDetail.getConstraints(1, 2, -1, 2));
		groupDetail.add(labFirstName, layDetail.getConstraints(0, 3));
		groupDetail.add(editFirstName, layDetail.getConstraints(1, 3));
		groupDetail.add(labLastName, layDetail.getConstraints(2, 3));
		groupDetail.add(editLastName, layDetail.getConstraints(3, 3, -1, 3));
		groupDetail.add(labPhone, layDetail.getConstraints(0, 4, 0, 4));
		groupDetail.add(editPhone, layDetail.getConstraints(1, 4, 1, 4));
		groupDetail.add(labFax, layDetail.getConstraints(2, 4, 2, 4));
		groupDetail.add(editFax, layDetail.getConstraints(3, 4, -1, 4));

		groupRoles.setText("Roles");
		groupRoles.setLayout(layRoles);
		groupRoles.add(navRoles, layRoles.getConstraints(0, 0, -1, 0));

		layDetails.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
		layDetails.setMargins(5, 5, 5, 5);
		panDetails.setLayout(layDetails);
		panDetails.add(groupDetail, layDetails.getConstraints(0, 0, -1, 0));
		panDetails.add(groupRoles, layDetails.getConstraints(0, 1, -1, -1));

		spanMain.add(panUsers, UISplitPanel.FIRST_COMPONENT);
		spanMain.add(panDetails, UISplitPanel.SECOND_COMPONENT);

		setLayout(layoutThis);
		add(spanMain);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Stores the relation between the Roles und Users.
	 * 
	 * @param pDataRowEvent the dataRow Event
	 * @throws Exception if a DB error occurs.
	 */
	public void doRelateUsers(DataRowEvent pDataRowEvent) throws Exception
	{
		String[] changedColumnNames = pDataRowEvent.getChangedColumnNames();
		if ("ROLE_GRANTED".equals(changedColumnNames[0]))
		{
			if ("N".equals(rdbRoles.getValue("ROLE_GRANTED")))
			{
				String sUserName = getConnection().getUserName();

				if ("Administrator".equals(rdbRoles.getValue("NAME")) && sUserName != null && sUserName.equals(rdbUsers.getValue("USERNAME")))
				{
					rdbRoles.restoreSelectedRow();

					throw new Exception("You can't remove the administrator role of yourself!");
				}
				else
				{
					rdbUserRole.delete();
				}
			}
			else
			{
				rdbUserRole.insert(false);
				rdbUserRole.saveSelectedRow();
			}
		}
	}

} // UsersWorkScreen
