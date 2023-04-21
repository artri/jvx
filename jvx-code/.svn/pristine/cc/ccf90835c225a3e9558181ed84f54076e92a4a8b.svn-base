/*
 * Copyright 2022 SIB Visions GmbH
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
 * 09.05.2022 - [JR] - creation
 */
package javax.rad.genui.component;

import javax.rad.application.DesktopApplication;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIImage;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.ui.IComponent;

import com.sibvisions.rad.ui.swing.impl.SwingApplication;

/**
 * A simple test application for font icons.
 * 
 * @author René Jahn
 */
public class IconTestApplication extends DesktopApplication 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Startup
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts the application.
	 * 
	 * @param pArgs arguments
	 */
	public static void main(String[] pArgs)
	{
		SwingApplication sapp = new SwingApplication();
		sapp.startup(IconTestApplication.class.getName(), null, null);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>IconTestApplication</code>.
	 * 
	 * @param pLauncher the UI launcher
	 */	
	public IconTestApplication(UILauncher pLauncher) 
	{
		super(pLauncher);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public IComponent getDefaultComponent() throws Exception 
	{
		UIFormLayout flMain = new UIFormLayout();
		
		UIPanel panMain = new UIPanel(flMain);
		panMain.add(new UIButton("Material", UIImage.getImage("JIconFont.material.CAMERA;size=40;color=orange")), flMain.getConstraints(0, 0));
		panMain.add(new UIButton("Elusive", UIImage.getImage("JIconFont.elusive.ADDRESS_BOOK;size=18;color=#ffaaee")), flMain.getConstraints(1, 0));
		panMain.add(new UIButton("Entypo", UIImage.getImage("JIconFont.entypo.ADDRESS;size=18")), flMain.getConstraints(2, 0));
		panMain.add(new UIButton("FontAwesome", UIImage.getImage("JIconFont.fontawesome.ADDRESS_CARD;size=18")), flMain.getConstraints(3, 0));
		panMain.add(new UIButton("Iconic", UIImage.getImage("JIconFont.iconic.BATTERY_CHARGING;size=25;color=blue")), flMain.getConstraints(4, 0));
		panMain.add(new UIButton("Typicons", UIImage.getImage("JIconFont.typicons.ANCHOR;size=32;color=200,120,109")), flMain.getConstraints(5, 0));
		
	    return panMain;
	}

}	// IconTestApplication
