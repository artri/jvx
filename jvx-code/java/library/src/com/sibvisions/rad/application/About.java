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
 * 02.12.2008 - [JR] - used dynamic about text
 * 04.02.2009 - [JR] - Session tab added for RemoteApplication implementations
 * 25.04.2009 - [JR] - added log tab
 * 06.05.2009 - [JR] - updateLogLevels implemented
 */
package com.sibvisions.rad.application;

import java.awt.Color;
import java.util.Map;
import java.util.Properties;

import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * The <code>About</code> class is the About dialog of the application. It
 * contains company information such as contact adress. It also contains
 * system information such as java system properties.
 * 
 * @author René Jahn
 */
public class About extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the text area. */
	private transient UITextArea taAbout 	= new UITextArea();
	
	/** the tite label. */
	private transient UILabel 	lblTitle 	= new UILabel();

	/** the subtitle label. */
	private transient UILabel 	lblSubTitle	= new UILabel();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>About</code> for the given application.
	 * 
	 * @param pDesktop the desktop panel
	 */
	public About(UIDesktopPanel pDesktop)
	{
		super(pDesktop);
		
		init();
	}
	
	/**
	 * Initializes the ui components.
	 */
	protected void init()
	{
		UITabsetPanel tabMain = new UITabsetPanel();
		
		//----------------------------------
		// Title Panel
		//----------------------------------

		UIPanel panTitle = new UIPanel();
		
		panTitle.setBackground(new UIColor(255, 255, 255));

		UIFormLayout foTitle = new UIFormLayout();
		foTitle.setMargins(new UIInsets(5, 5, 5, 5));
		foTitle.setHorizontalGap(10);
		foTitle.setHorizontalAlignment(IAlignmentConstants.ALIGN_LEFT);
		
		panTitle.setLayout(foTitle);
		
		lblTitle.setText("Application information");
		
		lblTitle.setFont(new UIFont("dialog", UIFont.BOLD, 14));
		
		lblSubTitle.setText("Click on the tabs below to get additional information.");
		
		UIIcon icoAbout = new UIIcon();
		icoAbout.setImage(UIImage.getImage(UIImage.ABOUT_LARGE));
		
		panTitle.add(icoAbout, foTitle.getConstraints(0, 0, 0, 1));
		panTitle.add(lblTitle, foTitle.getConstraints(1, 0));
		panTitle.add(lblSubTitle, foTitle.getConstraints(1, 1, 1, 1));
		
		//----------------------------------
		// About Tab
		//----------------------------------

		UIFormLayout foAbout = new UIFormLayout();
		foAbout.setMargins(new UIInsets(2, 2, 2, 2));
		
		UIPanel panAbout = new UIPanel();
		panAbout.setLayout(foAbout);
		
		taAbout.setText(" SIB Visions GmbH\n Meldemannstraße 18\n A-1200 Vienna\n\n http://www.sibvisions.com");
		taAbout.setEditable(false);
		
		panAbout.add(taAbout, foAbout.getConstraints(0, 0, -1, -1));
		
		//----------------------------------
		// System Tab
		//----------------------------------
		
		UIFormLayout foSystem = new UIFormLayout();
		foSystem.setMargins(new UIInsets(2, 2, 2, 2));
		
		UIPanel panSystem = new UIPanel();
		panSystem.setLayout(foSystem);

		try
		{
			MemDataBook mdbSystem = new MemDataBook();
			mdbSystem.setName("SYSTEM");
			
			IRowDefinition rowdef = mdbSystem.getRowDefinition();
			rowdef.addColumnDefinition(new ColumnDefinition("KEY"));
			rowdef.addColumnDefinition(new ColumnDefinition("VALUE"));
			rowdef.getColumnDefinition("KEY").setWidth(180);
			
			rowdef.getColumnDefinition("KEY").setLabel("Parameter");
			rowdef.getColumnDefinition("VALUE").setLabel("Value");
			
			mdbSystem.open();
			
			try
			{
				Properties props = System.getProperties();
				
				for (Map.Entry<Object, Object> entry : props.entrySet())
				{
					try
					{
						mdbSystem.insert(false);
						mdbSystem.setValue("KEY", entry.getKey());
						mdbSystem.setValue("VALUE", entry.getValue());
					}
					catch (SecurityException sec)
					{
						//nothing to be done
					}
				}
			}
			catch (SecurityException se)
			{
				String[] sProperties = {"java.version", "java.vendor", "java.vendor.url", "java.home",
										"java.vm.specification.version", "java.vm.specification.vendor",
										"java.vm.specification.name", "java.vm.version", "java.vm.vendor",
										"java.vm.name", "java.specification.version", 
										"java.specification.vendor", "java.specification.name",
										"java.class.version", "java.class.path", "java.library.path",
										"java.io.tmpdir", "java.compiler", "java.ext.dirs", "os.name",
										"os.arch", "os.version", "file.separator", "path.separator",
										"line.separator", "user.name", "user.home", "user.dir"};
				
				String sValue;
				
				for (int i = 0, anz = sProperties.length; i < anz; i++)
				{
					try
					{
						sValue = System.getProperty(sProperties[i]);
						
						mdbSystem.insert(false);
						mdbSystem.setValue("KEY", sProperties[i]);
						mdbSystem.setValue("VALUE", sValue);
					}
					catch (SecurityException sec)
					{
						//nothing to be done
					}
				}
			}
			
			mdbSystem.setReadOnly(true);
			mdbSystem.setSelectedRow(-1);
			
			UITable tblSystem = new UITable();
			tblSystem.setDataBook(mdbSystem);
			tblSystem.setAutoResize(false);
			
			panSystem.add(tblSystem, foSystem.getConstraints(0, 0, -1, -1));
		}
		catch (ModelException me)
		{
			//nothing to be done!
		}
		
		//----------------------------------
		// Button Panel
		//----------------------------------
		
		UIPanel panButtons = new UIPanel();
		
		UIFormLayout foButtons = new UIFormLayout();
		foButtons.setMargins(new UIInsets(8, 5, 5, 5));
		foButtons.setHorizontalAlignment(IAlignmentConstants.ALIGN_RIGHT);
		
		panButtons.setLayout(foButtons);
		
		UIButton butOK = new UIButton();
		butOK.setText("OK");
		butOK.setPreferredSize(new UIDimension(60, 25));
		butOK.eventAction().addListener(this, "doOk");
		
		panButtons.add(butOK);
		
		//----------------------------------
		// Tab
		//----------------------------------
		
		UIFormLayout foTab = new UIFormLayout();
		foTab.setMargins(new UIInsets(5, 2, 5, 2));
		
		UIPanel panTab = new UIPanel();
		panTab.setLayout(foTab);
		
		tabMain.add(panAbout, "About");
		tabMain.add(panSystem, "System");

		panTab.add(tabMain, foTab.getConstraints(0, 0, -1, -1));

		//TODO [HM] Border
		if (panTab.getResource() instanceof JPanel)
		{
			((JPanel)panTab.getResource()).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.DARK_GRAY));
		}

		//----------------------------------
		// Build UI
		//----------------------------------

		setLayout(new UIBorderLayout());
		
		add(panTitle, IBorderLayout.NORTH);
		add(panTab, IBorderLayout.CENTER);
		add(panButtons, IBorderLayout.SOUTH);
		
		
		setTitle("About");
		setPreferredSize(new UIDimension(450, 350));
		setIconImage(null);
		setMaximizable(false);
		setIconifiable(false);
		setModal(true);
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the short information.
	 * 
	 * @param pInfo the information
	 */
	public void setInfo(String pInfo)
	{
		lblTitle.setText(pInfo);
	}
	
	/**
	 * Sets the detailed information.
	 * 
	 * @param pSubInfo the information
	 */
	public void setSubInfo(String pSubInfo)
	{
		lblSubTitle.setText(pSubInfo);
	}
	
	/**
	 * Sets the about information.
	 * 
	 * @param pText the information
	 */
	public void setText(String pText)
	{
		taAbout.setText(pText);		
	}
	
	/**
	 * Sets the font for the text.
	 * 
	 * @param pFont the font
	 */
	public void setTextFont(UIFont pFont)
	{
		taAbout.setFont(pFont);
	}

	/**
	 * Gets the textarea for the text.
	 * 
	 * @return the text area
	 */
	protected ITextArea getTextArea()
	{
		return taAbout;
	}
	
	/**
	 * Gets the info label.
	 * 
	 * @return the label
	 */
	protected ILabel getInfoLabel()
	{
		return lblTitle;
	}
	
	/**
	 * Gets the subinfo label.
	 * 
	 * @return the label
	 */
	protected ILabel getSubInfoLabel()
	{
		return lblSubTitle;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Closes the dialog when the OK button was clicked.
	 * 
	 * @param pEvent the event from the button
	 */
	public void doOk(UIActionEvent pEvent)
	{
		dispose();
	}
	
}	// About
