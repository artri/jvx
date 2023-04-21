/*
 * Copyright 2009 SIB Visions GmbH
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
 *
 *
 * History
 *
 * 01.10.2008 - [JR] - creation
 * 02.11.2008 - [RH] - DataBook -> StorageDatabook changed
 * 02.06.2009 - [JR] - redesign
 */
package demo.frames.special;

import javax.rad.genui.UIDimension;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SubConnection;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.rad.model.remote.RemoteDataBook;

import demo.Demo;

/**
 * The <code>AddressFrame</code> shows all address entries with simple navigation.
 * 
 * @author René Jahn
 */
public class AddressFrame extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the data source. */
	private RemoteDataSource rds;
	
	/** adress data access. */
	private	RemoteDataBook	rdbAddress	= new RemoteDataBook();

	/** address table. */
	private UITable			tblAddress	= new UITable();
	
	/** new address. */
	private UIButton		butNew		= new UIButton();
	/** delete address. */
	private UIButton		butDelete	= new UIButton();
	/** export data. */
	private UIButton		butExport	= new UIButton();
	/** save data. */
	private UIButton		butSave		= new UIButton();
	/** reload data. */
	private UIButton		butReload	= new UIButton();
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AddressFrame</code> for the {@link Demo} application.
	 * 
	 * @param pDemo the application
	 * @throws Throwable if an exception occured during initialization
	 */
	public AddressFrame(Demo pDemo) throws Throwable
	{
		super(pDemo.getDesktopPane());

		//open a connection for the screen
		SubConnection con = ((MasterConnection)pDemo.getConnection()).createSubConnection("demo.special.Address");
		con.open();
		
		rds = new RemoteDataSource(con);
		rds.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		rds.open();

		rdbAddress.setDataSource(rds);
		rdbAddress.setName("address");
		rdbAddress.open();
		
		initUI();
	}
    
	/**
	 * Initializes the UI.
	 */
	private void initUI()
	{
		tblAddress.setDataBook(rdbAddress);

		//-------------------------------------------------------
		// ToolBars
		//-------------------------------------------------------

		UIToolBar tbManipulation = new UIToolBar();
		
		configureToolBarButton(butNew, "New", "doNew", UIImage.ADD_SMALL);
		configureToolBarButton(butDelete, "Remove", "doRemove", UIImage.REMOVE_SMALL);

		tbManipulation.add(butNew);
		tbManipulation.add(butDelete);
		
		UIToolBar tbConfig = new UIToolBar();

		configureToolBarButton(butExport, "Export", "doExport", UIImage.EXPORT_SMALL);
		butExport.setEnabled(false);
		
		tbConfig.add(butExport);

		UIToolBar tbPersist = new UIToolBar();

		configureToolBarButton(butSave, "Save", "doSave", UIImage.SAVE_SMALL);
		configureToolBarButton(butReload, "Reload", "doReload", UIImage.RELOAD_SMALL);

		tbPersist.add(butSave);
		tbPersist.add(butReload);
		
		//-------------------------------------------------------
		// Panels
		//-------------------------------------------------------

		setToolBarArea(AREA_LEFT);
		addToolBar(tbManipulation);
		addToolBar(tbConfig);
		addToolBar(tbPersist);
		
		add(tblAddress, UIBorderLayout.CENTER);
		
		//-------------------------------------------------------
		// Frame
		//-------------------------------------------------------
		
		setTitle("Address");
	}	
	
	/**
	 * Configures a standard button for the toolbar layout.
	 * 
	 * @param pButton the button
	 * @param pText the button text
	 * @param pAction the action method for the button
	 * @param pIcon the name of the predefined icon
	 */
	private void configureToolBarButton(UIButton pButton, String pText, String pAction, String pIcon)
	{
		pButton.setImage(UIImage.getImage(pIcon));
		pButton.setToolTipText(pText);
		pButton.setFocusable(false);
		pButton.setMinimumSize(new UIDimension(0, 0));
		pButton.setPreferredSize(new UIDimension(24, 24));
		pButton.eventAction().addListener(this, pAction);
		
		//to be LaF independent
		pButton.setBorderOnMouseEntered(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets a preferred row in the adress table.
	 * 
	 * @param pRow the row
	 * @throws Exception if it's not possible to set the row
	 */
	public void setSelectedRow(Integer pRow) throws Exception
	{
		rdbAddress.setSelectedRow(pRow.intValue() - 1);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Performs insert on the current DataBook.
	 *  
	 * @param pEvent the event information from the button
	 * @throws Exception if an error occurs
	 */
	public void doNew(UIActionEvent pEvent) throws Exception
	{
		rdbAddress.insert(false);
	}

	/**
	 * Performs delete on the current DataBook.
	 *  
	 * @param pEvent the event information from the button
	 * @throws Exception if an error occurs
	 */
	public void doRemove(UIActionEvent pEvent) throws Exception
	{
		rdbAddress.delete();
	}

	/**
	 * Performs the CSV Export of the current DataBook.
	 *  
	 * @param pEvent the event information from the button
	 */
	public void doExport(UIActionEvent pEvent)
	{
	}
	
	/**
	 * Saves all databook changes.
	 *  
	 * @param pEvent the event information from the button
	 * @throws Exception if an error occurs
	 */
	public void doSave(UIActionEvent pEvent) throws Exception
	{
		rdbAddress.saveAllRows();
	}
	
	/**
	 * Reloads all databooks.
	 * 
	 * @param pEvent the event information from the button
	 * @throws Exception if an error occurs
	 */
	public void doReload(UIActionEvent pEvent) throws Exception
	{
		rdbAddress.reload();
	}	
	
}	// AddressFrame
