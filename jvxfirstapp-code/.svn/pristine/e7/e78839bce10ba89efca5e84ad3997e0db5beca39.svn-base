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
 * 25.08.2009 - [JR] - creation
 * 29.12.2011 - [JR] - version 1.2 (JVx 1.0)
 */
package apps.firstapp;

import javax.rad.application.ILauncher;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.remote.IConnection;

import apps.firstapp.frames.DBEditFrame;

import com.sibvisions.rad.application.Application;
import com.sibvisions.rad.remote.http.HttpConnection;
import com.sibvisions.rad.server.DirectServerConnection;

/**
 * First application with JVx, Enterprise Application Framework.
 * <p/>
 * @author René Jahn
 */
public class FirstApplication extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** constant version value. */ 
	public static final String VERSION = "1.5";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>FirstApplication</code> with a technology
	 * dependent launcher.
	 * <p/>
	 * @param pLauncher the technology dependent launcher
	 */
	public FirstApplication(UILauncher pLauncher)
	{
		super(pLauncher);
		
		setPreferredSize(new UIDimension(1024, 768));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IConnection createConnection() throws Exception
	{
		String sConClass = getLauncher().getParameter("Application.connectionClass");
		
		if (sConClass != null)
		{
			return new HttpConnection(getLauncher().getParameter(ILauncher.PARAM_SERVERBASE) + "/services/Server");
		}
		else
		{
			return new DirectServerConnection();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getApplicationName()
	{
		return "firstapp";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterLogin()
	{
		super.afterLogin();
	
		//configure MenuBar
		
		UIMenu menuMasterData = new UIMenu();
		menuMasterData.setText("Master data");
		
		UIMenuItem miDBEdit = createMenuItem
		                      ("doOpenDBEdit", null, "DB Edit", 
		                       UIImage.getImage(UIImage.SEARCH_LARGE));

		menuMasterData.add(miDBEdit);

		//insert before Help
		getMenuBar().add(menuMasterData, 1);
		
		//configure ToolBar

		UIToolBar tbMasterData = new UIToolBar();
		
		UIButton butDBEdit = createToolBarButton
		                     ("doOpenDBEdit", null, "DB Edit", 
		                      UIImage.getImage(UIImage.SEARCH_LARGE));
		
		tbMasterData.add(butDBEdit);
		
		getLauncher().addToolBar(tbMasterData);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Opens the edit screen.
	 * <p/>
	 * @throws Throwable if the edit frame can not be opened
	 */
	public void doOpenDBEdit() throws Throwable
	{
		DBEditFrame frame = new DBEditFrame(this);
		
		configureFrame(frame);
		
		frame.setVisible(true);
	}

}	// FirstApplication
