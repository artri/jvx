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
 * 30.10.2008 - [JR] - setApplication, setUserName, setPassword implemented
 * 06.11.2008 - [JR] - extends WorkScreenBase instead of AbstractImplementationBase
 * 23.11.2008 - [JR] - genUI redesign
 * 23.04.2009 - [JR] - setMode: removed event listeners before adding [BUGFIX]
 * 04.10.2009 - [JR] - Change password: enter old password
 */
package com.sibvisions.rad.application;

import java.awt.Color;

import javax.rad.application.IConnectable;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIInsets;
import javax.rad.genui.UIRectangle;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIPasswordField;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The <code>Login</code> creates and layouts the UI components for
 * the login screen of the {@link Application} application. 
 * 
 * @author René Jahn
 */
public class Login extends UIInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mode for standard login UI. */
	public static final int MODE_LOGIN = 0;
	
	/** the mode for password change UI. */
	public static final int MODE_CHANGE_PASSWORD = 1;

	/** the welcome text. */
	private static final String WELCOME 		= "Welcome";
	
	/** the change password title. */
	private static final String CHANGE_PASSWORD	= "Change password";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the main connection for checking the login state. */
	private transient IConnectable	conMain;
	
	/** the panel for manual login. */
	private transient UIPanel 		panManual 			= new UIPanel();
	
	/** the panel for the login icon. */
	private	transient UIPanel 		panIcon 			= new UIPanel();

	/** the panel for the title and welcome message. */ 
	private transient UIPanel		panTitle			= new UIPanel();

	/** the panel for the buttons. */
	private transient UIPanel 		panButtons 			= new UIPanel();
	
	/** the user name for authentication. */
	private transient UITextField		tfUserName		= new UITextField();
	
	/** the password for authentication. */
	private transient UIPasswordField	tfPassword		= new UIPasswordField();
	
	/** the new password (only for change password mode). */
	private transient UIPasswordField	tfPasswordNew	= new UIPasswordField();

	/** the confirmed new password (only for change password mode). */
	private transient UIPasswordField	tfPasswordConfirm	= new UIPasswordField();

	/** the logon/change password button. */
	private transient UIButton		butOK				= new UIButton();
	
	/** the cancel button. */
	private transient UIButton		butCancel			= new UIButton();

	/** the welcome/error message. */
	private transient UILabel		lblInfoTitle		= new UILabel();
	
	/** the info text below the welcome/error message. */
	private transient UILabel 		lblInfoText 		= new UILabel();
	
	/** the username label. */
	private transient UILabel 		lblUserName 		= new UILabel();
	
	/** the password label. */
	private transient UILabel 		lblPassword 		= new UILabel();
	
	/** the password new label. */
	private transient UILabel 		lblPasswordNew 		= new UILabel();
	
	/** the password confirm label. */
	private transient UILabel		lblPasswordConfirm 	= new UILabel();
	
	/** the icon for the information image. */
	private transient UIIcon		icoInfo;

	/** the current error message. */
	private transient String		sError				= null;
	
	/** the current screen mode. */
	private transient int 			iMode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of the login UI.
	 * 
	 * @param pDesktop the desktop for showing the frame
	 * @param pConnectable the main connection
	 */
	public Login(UIDesktopPanel pDesktop, IConnectable pConnectable)
	{
		super(pDesktop);
		
		conMain = pConnectable;
		
		init();
	}
	
	/**
	 * Initializes the UI components.
	 */
	protected void init()
	{
		setLayout(new UIBorderLayout());

		panManual.setLayout(new UIBorderLayout());
		
		//----------------------------------------------------------------
		// ICON Panel
		//----------------------------------------------------------------

		panIcon.setLayout(null);
		panIcon.setVisible(false);

		icoInfo = new UIIcon();
		
		panIcon.add(icoInfo);
		
		panManual.add(panIcon, IBorderLayout.WEST);

		//----------------------------------------------------------------
		// TITEL Panel
		//----------------------------------------------------------------
		
		panTitle.setLayout(new UIFormLayout());

		UIPanel panCenter = new UIPanel();
		
		panCenter.setLayout(new UIBorderLayout());

//TODO [HM] Border
if (panTitle.getResource() instanceof JPanel)
{
	((JPanel)panTitle.getResource()).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
}
		
		lblInfoTitle.setFont(new UIFont("dialog", IFont.BOLD, 15));
		
		panTitle.add(lblInfoTitle);
		panTitle.add(lblInfoText, IFormLayout.NEWLINE);
		panTitle.setBackground(UIColor.white);

		panCenter.add(panTitle, IBorderLayout.NORTH);

		//----------------------------------------------------------------
		// TEXTFIELD Panel
		//----------------------------------------------------------------

		UIPanel panCenterCenter = new UIPanel();
		
		panCenterCenter.setLayout(new UIFormLayout());
		
		lblUserName.setText("Username");
		lblPassword.setText("Password");
		lblPasswordNew.setText("Password (new)");
		lblPasswordConfirm.setText("Password (confirm)");

		panCenterCenter.add(lblUserName);
		panCenterCenter.add(tfUserName);
		panCenterCenter.add(lblPassword, UIFormLayout.NEWLINE);
		panCenterCenter.add(tfPassword);
		panCenterCenter.add(lblPasswordNew, UIFormLayout.NEWLINE);
		panCenterCenter.add(tfPasswordNew);
		panCenterCenter.add(lblPasswordConfirm, UIFormLayout.NEWLINE);
		panCenterCenter.add(tfPasswordConfirm);

		panCenter.add(panCenterCenter, UIBorderLayout.CENTER);

		panManual.add(panCenter, UIBorderLayout.CENTER);
		
		//----------------------------------------------------------------
		// BUTTON Panel
		//----------------------------------------------------------------

		UIFormLayout flCenterSouth = new UIFormLayout();
		flCenterSouth.setMargins(new UIInsets(5, 5, 5, 5));
		flCenterSouth.setHorizontalAlignment(UIFormLayout.ALIGN_RIGHT);
		
		panButtons.setLayout(flCenterSouth);
		panButtons.setBackground(UIColor.white);

		butOK.setDefaultButton(true);
		butOK.setPreferredSize(new UIDimension(100, 25));
		
		butCancel.setText("Cancel");
		butCancel.setPreferredSize(new UIDimension(100, 25));
		
		panButtons.add(butCancel, flCenterSouth.getConstraints(flCenterSouth.getTopMarginAnchor(), flCenterSouth.getLeftMarginAnchor(), null, null)); 
		panButtons.add(butOK,     flCenterSouth.getConstraints(flCenterSouth.getTopMarginAnchor(), null, null, flCenterSouth.getRightMarginAnchor())); 
		
		
		panCenter.add(panButtons, IBorderLayout.SOUTH);
		
//TODO [HM] Border
if (panTitle.getResource() instanceof JPanel)
{
	((JPanel)panButtons.getResource()).setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
}

		//----------------------------------------------------------------
		// Defaults
		//----------------------------------------------------------------
		
		setResizable(false);
		setClosable(false);
		setMaximizable(false);
		setIconifiable(false);
		setIconImage(null);
		setTitle("Login");
		
		setMode(MODE_LOGIN);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the user name.
	 * 
	 * @param pUserName the user name
	 */
	public void setUserName(String pUserName)
	{
		tfUserName.setText(pUserName);
	}
	
	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName()
	{
		return tfUserName.getText();
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param pPassword the password
	 */
	public void setPassword(String pPassword)
	{
		tfPassword.setText(pPassword);
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword()
	{
		return tfPassword.getText();
	}
	
	/**
	 * Gets the new password.
	 * 
	 * @return the new password or <code>null</code> if ther is no new password
	 * @throws SecurityException if the new password is invalid
	 */
	public String getNewPassword()
	{
		if (iMode == MODE_CHANGE_PASSWORD)
		{
			if (tfPasswordNew.getText().length() == 0)
			{
				return null;
			}
			
			if (!tfPasswordNew.getText().equals(tfPasswordConfirm.getText()))
			{
				throw new SecurityException("The passwords are different!");
			}
			else
			{
				return tfPasswordNew.getText();
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the login-info image.
	 * 
	 * @param pImage the image
	 */
	public void setInfoImage(IImage pImage)
	{
		icoInfo.setImage(pImage);
		
		if (pImage != null)
		{			
			//use a null layout and the preferred size to align the image as desired.
			//With a layout, the image will not displayed as expected!
			icoInfo.setBounds(new UIRectangle(0, 0, pImage.getWidth(), pImage.getHeight()));
	
			panIcon.setPreferredSize(new UIDimension(pImage.getWidth(), 0));
			panIcon.setVisible(true);
		}
		else
		{
			panIcon.removeAll();
			panIcon.setVisible(false);
		}
	}
	
	/**
	 * Gets the login-info image.
	 * 
	 * @return the image
	 */
	public IImage getInfoImage()
	{
		return icoInfo.getImage();
	}

	/**
	 * Sets an error text instead of the welcome text.
	 * 
	 * @param pError the error text
	 */
	public void setError(String pError)
	{
		if (pError != null && pError.length() > 0)
		{
			sError = pError;

			lblInfoTitle.setForeground(UIColor.red);
			lblInfoTitle.setText(pError);
		}
		else
		{
			sError = null;
			
			lblInfoTitle.setForeground(UIColor.black);
			
			if (iMode == MODE_CHANGE_PASSWORD)
			{
				lblInfoTitle.setText(CHANGE_PASSWORD);
			}
			else
			{
				lblInfoTitle.setText(WELCOME);
			}
		}
	}
	
	/**
	 * Gets the current error text, if set.
	 *  
	 * @return the error text or <code>null</code> if no text is set
	 */
	public String getError()
	{
		return sError;
	}
	
	/**
	 * Sets the mode for the login. The frame depends on the mode. When the 
	 * password mode will be set, and the application is connected, the 
	 * user only can change its password. If the application is not connected, 
	 * the user have to enter the login data and the new password. 
	 * When the default mode will be set, the user have to enter the 
	 * login credentials.
	 * 
	 * @param pMode the mode can be one of the following: {@link #MODE_CHANGE_PASSWORD} or {@link #MODE_LOGIN}
	 */
	public void setMode(int pMode)
	{
		setError(null);
		
		removeAll();
		add(panManual);

		boolean bChangePwd = pMode == MODE_CHANGE_PASSWORD;
		
		if (bChangePwd)
		{
			lblInfoTitle.setText(CHANGE_PASSWORD);
			
			if (conMain.isConnected())
			{
				lblInfoText.setText("Please enter and confirm the new password.");

				tfUserName.setEnabled(false);

				//hide the dialog
				butCancel.eventAction().removeListener(conMain);
				butCancel.eventAction().addListener((Application)conMain, "doHideLogin");
				
				//change password
				butOK.eventAction().removeListener(conMain);
				butOK.eventAction().addListener((Application)conMain, "doChangePassword");
				butOK.setText("Change");
				
				tfPassword.requestFocus();
			}
			else
			{
				lblInfoText.setText("Please enter your username and passwords.");

				//show login
				butCancel.eventAction().removeListener(conMain);
				butCancel.eventAction().addListener((Application)conMain, "doHideChangePassword");

				//login
				butOK.eventAction().removeListener(conMain);
				butOK.eventAction().addListener((Application)conMain, "doLogin");
				butOK.setText("Logon");
				
				tfUserName.requestFocus();
			}
		}
		else
		{
			lblInfoTitle.setText(WELCOME);
			lblInfoText.setText("Please enter your username and password.");
			
			tfUserName.setEnabled(true);
			
			//simple login action
			butOK.eventAction().removeListener(conMain);
			butOK.eventAction().addListener((Application)conMain, "doLogin");
			butOK.setText("Logon");
			
			tfUserName.requestFocus();
		}
	
		lblPasswordNew.setVisible(bChangePwd);
		tfPasswordNew.setVisible(bChangePwd);
		
		lblPasswordConfirm.setVisible(bChangePwd);
		tfPasswordConfirm.setVisible(bChangePwd);
		
		butCancel.setVisible(bChangePwd);
		
		tfPasswordNew.setText("");
		tfPasswordConfirm.setText("");
		
		if (((Application)conMain).getLauncher().isWebEnvironment())
		{
            if (pMode == MODE_CHANGE_PASSWORD)
            {
                setSize(400, 280);
            }
            else
            {
                setSize(400, 220);
            }
		}
	
		iMode = pMode;
	}
	
	/**
	 * Returns the current mode.
	 * 
	 * @return one of the known modes: {@link #MODE_CHANGE_PASSWORD} or {@link #MODE_LOGIN}
	 */
	public int getMode()
	{
		return iMode;
	}
	
}	// Login
