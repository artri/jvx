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
 * 26.05.2009 - [JR] - creation
 */
package demo;

import java.lang.reflect.Constructor;

import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.remote.IConnection;
import javax.rad.ui.event.UIActionEvent;

import com.sibvisions.rad.application.About;
import com.sibvisions.rad.application.Application;
import com.sibvisions.rad.server.DirectServerConnection;

/**
 * The <code>Demo</code> class is a customized {@link Application} for
 * testing the JVx library code.
 * 
 * @author René Jahn
 */
public class Demo extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Demo</code>.
	 * 
	 * @param pLauncher the launcher
	 * @throws Throwable if the application can not be initialized
	 */
	public Demo(UILauncher pLauncher) throws Throwable
	{
		super(pLauncher);
		
		setName("Demo application");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	protected String getApplicationName()
	{
		return "demo";
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected IConnection createConnection() throws Exception
	{
		return new DirectServerConnection();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureAbout(About pAbout)
	{
		pAbout.setText("DEMO company\nDemostreet 1\nDemocity\n\nhttp://www.demopage.org");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterLogin()
	{
		super.afterLogin();
		
		//configure MenuBar
		
		UIMenu menuAdmin = new UIMenu();
		menuAdmin.setText("Administration");
		
		UIMenuItem miCompany = createMenuItem("doOpenFrame", "demo.frames.CompanyFrame", "Company", UIImage.getImage("/demo/images/company.png"));
		UIMenuItem miDetails = createMenuItem("doOpenFrame", "demo.frames.CompanyDetailFrame", "Company Details", UIImage.getImage("/demo/images/details.png"));
		UIMenuItem miAddress = createMenuItem("doOpenFrame", "demo.frames.special.AddressFrame", "Address", UIImage.getImage("/demo/images/address.png"));
		UIMenuItem miBug136 = createMenuItem("doOpenFrame", "demo.frames.special.Bug136Frame", "#136", UIImage.getImage(UIImage.CANCEL_LARGE));

		menuAdmin.add(miCompany);
		menuAdmin.add(miDetails);
		menuAdmin.addSeparator();
		menuAdmin.add(miAddress);
		menuAdmin.addSeparator();
		menuAdmin.add(miBug136);
		
		getMenuBar().add(menuAdmin, 1);
		
		//configure ToolBar

		UIToolBar barAdmin = new UIToolBar();
		
		UIButton butCompany = createToolBarButton(miCompany);
		UIButton butAddress = createToolBarButton(miAddress);
		UIButton butBug136 = createToolBarButton(miBug136);
		
		barAdmin.add(butCompany);
		barAdmin.add(butAddress);
		barAdmin.add(butBug136);
		
		getLauncher().addToolBar(barAdmin);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a button for the application toolbar, based on a menu item.
	 * 
	 * @param pMenuItem the referenced menu item
	 * @return the {@link UIButton}
	 */
	private UIButton createToolBarButton(UIMenuItem pMenuItem)
	{
		return createToolBarButton("doOpenFrame", pMenuItem.getActionCommand(), pMenuItem.getText(), (UIImage)pMenuItem.getImage());
	}
	
	/**
	 * Opens an internal frame for the application in the application desktop.
	 * 
	 * @param pFrameClass the frame class
	 * @return the internal frame
	 * @throws Exception if the frame can not be instantiated
	 */
	public UIInternalFrame openFrame(Class<?> pFrameClass) throws Exception
	{
		Constructor<?> conFrame = pFrameClass.getConstructor(Demo.class);
		
		UIInternalFrame frame = (UIInternalFrame)conFrame.newInstance(this);

		//maybe show the application icon
		configureFrame(frame);

		//use predefined size, if the frame has no size set!
		if (frame.getSize().getHeight() == 0)
		{
			frame.setSize(new UIDimension(640, 480));
		}
		
		frame.setVisible(true);
		
		return frame;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens an internal frame for the application in the application desktop.
	 * 
	 * @param pEvent the triggering event
	 * @throws Exception if the frame can not be instantiated
	 */
	public void doOpenFrame(UIActionEvent pEvent) throws Exception
	{
		openFrame(Class.forName(pEvent.getActionCommand()));
	}
	
}	// Demo
