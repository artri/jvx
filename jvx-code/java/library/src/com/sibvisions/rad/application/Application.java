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
 * 30.07.2009 - [JR] - setApplicationTitle
 * 31.07.2009 - [JR] - cancelPendingThreads called
 * 04.10.2009 - [JR] - Change password: old password is required now
 * 09.11.2010 - [JR] - handleException: logging
 * 28.12.2010 - [JR] - #229: afterLogin/afterLogout overwritten 
 * 07.01.2010 - [JR] - added default choice editors 
 * 24.04.2012 - [JR] - #574: createError/getError
 * 03.04.2014 - [RZ] - #998: added default choice cell editors which can handle null values 
 */
package com.sibvisions.rad.application;

import javax.rad.application.ILauncher;
import javax.rad.application.genui.RemoteApplication;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIContainer;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.container.AbstractFrame;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.ChangePasswordException;
import javax.rad.remote.CommunicationException;
import javax.rad.remote.IConnection;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.SessionExpiredException;
import javax.rad.remote.event.CallErrorEvent;
import javax.rad.remote.event.CallEvent;
import javax.rad.remote.event.ConnectionEvent;
import javax.rad.remote.event.IConnectionListener;
import javax.rad.remote.event.PropertyEvent;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IContainer;
import javax.rad.ui.IRectangle;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIKeyEvent;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.menu.IMenu;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.event.IExceptionListener;

/**
 * The <code>Application</code> is the default {@link RemoteApplication}. It shows
 * a login screen, has an error dialog and handles the menu- and toolbar.
 * It's the base class for user-defined applications.
 * 
 * @author René Jahn
 */
public abstract class Application extends RemoteApplication
                                  implements IExceptionListener,
                            			     IConnectionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the parameter name for showing the exit button. */
	public static final String PARAM_MENU_EXIT_VISIBLE = "Application.Menu.exit.visible";
	
	
	/** the connection. */
	private transient IConnection 	iconApplication;
	
	/** the desktop panel. */
	private transient UIDesktopPanel dpanDesktop;

	/** the applications menu bar. */
	private transient UIMenuBar 	menu;

	/** the application tool bar. */
	private transient UIToolBar 	toolbar;

	/** the login frame. */
	private transient Login			login;

	/** the error frame. */
	private transient Error 		error;
	
	/** the login menu item. */
	private transient UIMenuItem 	miFileLogin;
	
	/** the logout menu item. */
	private transient UIMenuItem 	miFileLogout;
	
	/** the change password menu item. */
	private transient UIMenuItem 	miFileChangePwd;
	
	/** the exit menu item. */
	private transient UIMenuItem 	miFileExit;
	
	/** the help contents menu item. */
	private transient UIMenuItem 	miHelp;
	
	/** the about menu item. */
	private transient UIMenuItem 	miHelpAbout;
	
	/** the login toolbar button. */
	private transient UIButton 		butLogin;
	
	/** the logout toolbar button. */
	private transient UIButton 		butLogout;
	
	/** the exit toolbar button. */
	private transient UIButton 		butExit;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>Main</code> and configures the
	 * user interface.
	 * 
	 * @param pLauncher the launcher
	 */
	public Application(UILauncher pLauncher)
	{
		super(pLauncher);

		ExceptionHandler.addExceptionListener(this);

		preConfigure();
		
		String sName = getApplicationName();
		
		pLauncher.setParameter(ILauncher.PARAM_APPLICATIONNAME, sName);
		
		if (sName != null && sName.length() > 0)
		{
			sName = sName.toLowerCase();
			sName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
		}
		
		setName(sName);
		setPreferredSize(new UIDimension(1024, 768));

		//---------------------------------------------------------------------
		// GUI configuration
		//---------------------------------------------------------------------

		UIChoiceCellEditor.addDefaultChoiceCellEditor(ApplicationUtil.YESNO_EDITOR);
		UIChoiceCellEditor.addDefaultChoiceCellEditor(ApplicationUtil.YESNONULL_EDITOR);
		
		/* application */

		configureFrame(pLauncher);
		
		/* the menu */
		
		menu = createMenu();
		configureMenu(menu);
		
		/* the toolbar */
		
		toolbar = createToolBar();
		configureToolBar(toolbar);

		/* the content pane (= the desktop) */
		
		dpanDesktop = createDesktopPane();
		configureDesktopPane(dpanDesktop);

		/* login frame */
		
		login = createLogin();
		configureLogin(login);
		
		/* the application pane */
		
		configureApplicationPane(getApplicationPane());
	}

	/**
	 * This method will be used from subclasses to configure the UI before the UI will
	 * be created.
	 */
	protected void preConfigure()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name for the application.
	 * 
	 * @return the application name
	 */
	protected abstract String getApplicationName();

	/**
	 * Creates a connection for the application.
	 * 
	 * @return the connection implementation
	 * @throws Exception if the "connection" can not be created
	 */
	protected abstract IConnection createConnection() throws Exception;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void handleException(Throwable pThrowable)
	{
		error(pThrowable);
		
		try
		{
			if (error == null || error.isClosed())
			{
				error = createError();
				error.eventWindowClosed().addListener(this, "doErrorClosed");

				configureFrame(error);
				
				invokeLater(this, "showErrorDialog");
			}
	
			error.addError(pThrowable);
		}
		catch (Throwable th)
		{
			//forwarding to the launcher is the only possibility
			getLauncher().handleException(th);
			
			getLauncher().handleException(pThrowable);
		}
	}
	
	// IConnectionListener
	
	/**
	 * {@inheritDoc}
	 */
	public void callError(CallErrorEvent pEvent)
	{
		//used to handle asynchronous exceptions
		Throwable pError = pEvent.getCause();
		
		if (pError instanceof CommunicationException)
		{
			logout();
		
			if (pError instanceof SessionExpiredException)
			{
				login.setError("Session expired!");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void connectionOpened(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionReOpened(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void connectionClosed(ConnectionEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionCalled(CallEvent pEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectCalled(CallEvent pEvent)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void propertyChanged(PropertyEvent pEvent)
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyVisible()
	{
		try
		{
			afterLogout();
		}
		finally
		{
			super.notifyVisible();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDestroy()
	{
		try
		{
			AbstractConnection con = getConnection();
			
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (Throwable th)
				{
					//nothing to be done
				}
				finally
				{
					setConnection(null);
				}
			}
		}
		finally
		{
			super.notifyDestroy();
		}
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public <OP> Message showMessage(OP pOpener, int pIconType, int pButtonType, String pMessage, String pOkAction, String pCancelAction) throws Throwable
    {
        Message message = new Message(getDesktopPane(), 
                                      pIconType, 
                                      pButtonType,
                                      pMessage,
                                      pOkAction,
                                      pCancelAction);

        configureFrame(message);
        
        message.pack();
        message.setOpener(pOpener);
        message.centerRelativeTo(this);
        message.setVisible(true);
        
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIContainer getContentPane()
    {
        return dpanDesktop;
    }
    
	/**
	 * Performs actions after successful logout from the application.
	 */
	@Override
	protected void afterLogout()
	{
		setTitle(getName());
		
		setLoginVisible(true);
		
		getLauncher().cancelPendingThreads();
		
		super.afterLogout();
	}
	
	/**
	 * Performs actions after successful login to the application.
	 */
	@Override
	protected void afterLogin()
	{
		setTitle(getName() + " [" + getConnection().getUserName() + "]");

		setLoginVisible(false);
		
		login.setPassword(null);
		
		super.afterLogin();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Configures the default behaviour of the application pane.
	 *  
	 * @param pApplicationPane the application pane
	 */
	protected void configureApplicationPane(IContainer pApplicationPane)
	{
		pApplicationPane.setLayout(new UIBorderLayout());
		
		pApplicationPane.add(getDesktopPane());
	}
	
	/**
	 * Creates the content pane for the application. 
	 * 
	 * @return the content panel
	 */
	protected UIDesktopPanel createDesktopPane()
	{
		return new UIDesktopPanel();
	}
	
	/**
	 * Configures the default behaviour of the content pane.
	 * 
	 * @param pDesktop the content pane (= the desktop)
	 */
	protected void configureDesktopPane(UIDesktopPanel pDesktop)
	{
		UIIcon ico = new UIIcon();
		ico.setImage(UIImage.getImage("/com/sibvisions/rad/application/images/background.png"));
		ico.setHorizontalAlignment(IAlignmentConstants.ALIGN_RIGHT);
		ico.setVerticalAlignment(IAlignmentConstants.ALIGN_BOTTOM);
		
		pDesktop.add(ico);
		
		pDesktop.setBackground(new UIColor(253, 253, 253));	
	}

	/**
	 * Gets the desktop panel for the application.
	 * 
	 * @return the desktop panel
	 */
	public UIDesktopPanel getDesktopPane()
	{
		return dpanDesktop;
	}
	
	/**
	 * Creates the menu for the application.
	 * 
	 * @return the menu
	 */
	protected UIMenuBar createMenu()
	{
		UIMenuBar bar = new UIMenuBar();
		
		getLauncher().setMenuBar(bar);
		
		return bar;
	}
	
	/**
	 * Configures the menu for the application.
	 * 
	 * @param pMenu the menu 
	 */
	protected void configureMenu(UIMenuBar pMenu)
	{
		boolean bExit = isExitVisible();

		
		// FILE 

		IMenu menFile = new UIMenu();
		menFile.setText("File");

		miFileLogin     = createMenuItem("doShowLogin", 
	                                     null, 
	                                     "Login", 
	                                     UIImage.getImage(UIImage.LOGIN_SMALL));

		miFileLogout    = createMenuItem("doLogout", 
								     	 null, 
									     "Logout", 
									     UIImage.getImage(UIImage.LOGOUT_SMALL));

		miFileChangePwd = createMenuItem("doShowChangePassword", 
										 null, 
				                         "Change password", 
				                         UIImage.getImage(UIImage.CHANGE_PASSWORD_SMALL));

		if (bExit)
		{
			miFileExit = createMenuItem("doExit", 
									    null, 
										"Exit", 
										UIImage.getImage(UIImage.EXIT_SMALL));
		}

		menFile.add(miFileLogin);
		menFile.add(miFileLogout);
		menFile.add(miFileChangePwd);
		
		if (bExit)
		{
			menFile.addSeparator();
			menFile.add(miFileExit);
		}

		//HELP 

		IMenu menHelp = new UIMenu();
		menHelp.setText("Help");
		
		miHelp = createMenuItem("doHelp", 
								null, 
				                "Help contents", 
				                UIImage.getImage(UIImage.HELP_SMALL));
		miHelp.setAccelerator(Key.getKeyOnReleased(UIKeyEvent.VK_F1));
		
		miHelpAbout = createMenuItem("doAbout", 
	                                 null, 
				                     "About", 
				                     UIImage.getImage(UIImage.ABOUT_SMALL));
		
		menHelp.add(miHelp);
		menHelp.addSeparator();
		menHelp.add(miHelpAbout);
	
		pMenu.removeAll();
		
		pMenu.add(menFile, -1);
		pMenu.add(menHelp, -1);
		
		//Defaults
		miFileLogin.setVisible(false);
		miFileLogout.setVisible(false);
	}
	
	/**
	 * Creates the toolbar for the application.
	 * 
	 * @return the application toolbar
	 */
	protected UIToolBar createToolBar()
	{
		UIToolBar bar = new UIToolBar();
		
		return bar;
	}
	
	/**
	 * Configures the application toolbar.
	 * 
	 * @param pToolBar the application toolbar
	 */
	protected void configureToolBar(UIToolBar pToolBar)
	{
		boolean bExit = isExitVisible();		
		
		butLogin = createToolBarButton("doShowLogin", 
									   null, 
								       "Login", 
								       UIImage.getImage(UIImage.LOGIN_LARGE));
		
		butLogout = createToolBarButton("doLogout", 
									    null, 
									    "Logout", 
									    UIImage.getImage(UIImage.LOGOUT_LARGE));
		
		if (bExit)
		{
			butExit = createToolBarButton("doExit", 
										  null, 
										  "Exit", 
										  UIImage.getImage(UIImage.EXIT_LARGE));
		}
		
		pToolBar.removeAll();
		
		if (bExit)
		{
			pToolBar.add(butExit);
		}
		pToolBar.add(butLogin);
		pToolBar.add(butLogout);
		
		getLauncher().removeAllToolBars();

		getLauncher().addToolBar(pToolBar);
		
		//Defaults
		butLogin.setVisible(false);
		butLogout.setVisible(false);
	}
	
	/**
	 * Creates the login component for entering the authentication credentials. 
	 * 
	 * @return the login component
	 */
	protected Login createLogin()
	{
		return new Login(getDesktopPane(), this);
	}
	
	/**
	 * Creates the about component.
	 * 
	 * @return the about component
	 */
	protected About createAbout()
	{
		return new About(getDesktopPane());
	}
	
	/**
	 * Configures the login component.
	 * 
	 * @param pLogin the login component
	 */
	protected void configureLogin(Login pLogin)
	{
		configureFrame(pLogin);
		
		pLogin.setInfoImage(UIImage.getImage("/com/sibvisions/rad/application/images/login.png"));
	}
	
	/**
	 * Logs out the current user and closes the connections. 
	 */
	private void logout()
	{
		// clean up the toolbar
		configureMenu(menu);
		
		configureToolBar(toolbar);
		
		AbstractConnection con = getConnection();
		
		if (con != null)
		{
			try
			{
				con.removeConnectionListener(this);
				con.close();
			}
			catch (Throwable th)
			{
				debug(th);
			}
			finally
			{
				setConnection(null);
				iconApplication = null;
			}
		}
		
		afterLogout();
	}
	
	/**
	 * Sets the application title. It's a wrapper method <code>getLauncher().setTitle(...)</code> and
	 * necessary for inherited classes, to avoid title flicker.
	 *  
	 * @param pName the application title
	 */
	protected void setTitle(String pName)
	{
		getLauncher().setTitle(pName);
	}
	
	/**
	 * Show or hide the change password fields in the login frame.
	 * 
	 * @param pVisible <code>true</code>to show the change password fields; <code>false</code> to
	 *                 hide it.
	 */
	private void setChangePasswordVisible(boolean pVisible)
	{
		if (pVisible)
		{
			login.setMode(Login.MODE_CHANGE_PASSWORD);
			
			setLoginFrameVisible(true, false);
		}
		else if (login.isVisible())
		{
			login.setMode(Login.MODE_LOGIN);
		
			setLoginFrameVisible(true, false);
		}		
	}
	
	/**
	 * Show or hide the login dialog.
	 * 
	 * @param pVisible <code>true</code> to show the login screen; <code>false</code> to
	 *                 hide it
	 */
	private void setLoginVisible(boolean pVisible)
	{
		miFileLogin.setVisible(pVisible);
		miFileLogout.setVisible(!pVisible);
		
		butLogin.setVisible(pVisible);
		butLogout.setVisible(!pVisible);

		login.setMode(Login.MODE_LOGIN);
		
		setLoginFrameVisible(pVisible, false);
	}
	
	/**
	 * Show or hide the frame with the login panel.
	 * 
	 * @param pVisible <code>true</code> to show the frame; <code>false</code> to
	 *                 hide it
	 * @param pCenter <code>true</code> to center the frame anyway; <code>false</code> to
	 *                use the prepared position
	 */
	private void setLoginFrameVisible(boolean pVisible, boolean pCenter)
	{
		if (pVisible && login.getParent() == null)
		{
			IRectangle bounds = login.getBounds();
			
			//re-add to the desktop, if needed
			getContentPane().add(login);
			
			//keep bounds
			login.setBounds(bounds);
		}

		if (pVisible)
		{
			login.pack();
			
			int iMode = login.getMode();
			
			login.setModal(iMode == Login.MODE_CHANGE_PASSWORD);

			//don't move the window if already visible!
			//(-> if the password change is active, then only hide the fields, but do not move the window!)
			//it's not good for the eyes if the position changes to often!
			if (pCenter || !login.isVisible())
			{
				login.centerRelativeTo(dpanDesktop);
			}
		}
		
		login.setVisible(pVisible);
		
		if (!pVisible)
		{
			//reuse and don't dispose, but remove it from the desktop
			getContentPane().remove(login);
		}
	}	
	
	/**
	 * Shows the error dialog.
	 */
	public void showErrorDialog()
	{
		configureFrame(error);

		error.pack();
		error.centerRelativeTo(dpanDesktop);
		error.setVisible(true);
	}

	/**
	 * Configures an internal frame for the application.
	 * 
	 * @param pFrame the internal frame
	 */
	protected void configureFrame(AbstractFrame pFrame)
	{
		pFrame.setIconImage(UIImage.getImage("/com/sibvisions/rad/application/images/frame.png"));
	}

	/**
	 * Creates a new menu item.
	 * 
	 * @param pAction the action method
	 * @param pActionCommand the action command when the menu gets selected
	 * @param pText the menu label
	 * @param pImage the image for the menu item
	 * @return the menu item
	 */
	public UIMenuItem createMenuItem(String pAction, String pActionCommand, String pText, UIImage pImage)
	{
		UIMenuItem item = new UIMenuItem();
		
		item.setText(pText);
		item.eventAction().addListener(this, pAction);
		item.setActionCommand(pActionCommand);
		item.setImage(pImage);
		
		return item;
	}
	
	/**
	 * Creates a new toolbar button.
	 * 
	 * @param pAction the action method
	 * @param pActionCommand the action command
	 * @param pText the button text or <code>null</code> if the button has no text
	 * @param pImage the button image or <code>null</code> if the button has no image
	 * @return a technology dependent button 
	 */
	public UIButton createToolBarButton(String pAction, String pActionCommand, String pText, UIImage pImage)
	{
		UIButton button = new UIButton();
		
		button.setText(pText);
		button.setImage(pImage);
		button.eventAction().addListener(this, pAction);
		button.setActionCommand(pActionCommand);
		button.setMinimumSize(new UIDimension(47, 47));
		button.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		button.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		button.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		button.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
		button.setFont(new UIFont("Arial", UIFont.BOLD, 11));
		button.setImageTextGap(2);
		button.setFocusable(false);
		button.setBorderOnMouseEntered(true);
		
		return button;
	}

	/**
	 * Configures the about dialog.
	 * 
	 * @param pAbout the {@link About}
	 */
	protected void configureAbout(About pAbout)
	{
	}
	
	/**
	 * Gets the application menu bar.
	 * 
	 * @return the menu bar
	 */
	public UIMenuBar getMenuBar()
	{
		return menu;
	}
	
	/**
	 * Gets the application tool bar.
	 * 
	 * @return the tool bar
	 */
	public UIToolBar getToolBar()
	{
		return toolbar;
	}
	
	/**
	 * Gets the login frame.
	 * 
	 * @return the login frame
	 */
	public Login getLogin()
	{
		return login;
	}
	
	/**
	 * Creates a new {@link Error} frame.
	 * 
	 * @return an error frame
	 * @throws Throwable if the error instance can not be created
	 */
	protected Error createError() throws Throwable
	{
		Error err = new Error(getDesktopPane());
		err.setOpener(this);
		
		return err;
	}
	
	/**
	 * Gets the error frame.
	 * 
	 * @return the frame or <code>null</code> if no error frame is open
	 */
	public Error getError()
	{
		return error;
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Unsets the error dialog.
	 * 
	 * @param pEvent the frame close event
	 */
	public void doErrorClosed(UIWindowEvent pEvent)
	{
		error = null;		
	}
	
	/**
	 * The event method for the logon button. It tries to logon the user
	 * with provided credentials.
	 * 
	 * @param pEvent the event information from the button
	 * @throws Throwable if the connection could not be established
	 */
	public void doLogin(UIActionEvent pEvent) throws Throwable
	{
		try
		{
			login.setError(null);
			
			iconApplication = createConnection();

			MasterConnection aconApplication = new MasterConnection(iconApplication);

			aconApplication.addConnectionListener(this);
			
			aconApplication.setApplicationName(getApplicationName());
			aconApplication.setUserName(login.getUserName());
			aconApplication.setPassword(login.getPassword());
			aconApplication.setNewPassword(login.getPassword(), login.getNewPassword());
			aconApplication.open();

			setConnection(aconApplication);
			
			afterLogin();
		}
		catch (Throwable th)
		{
			setConnection(null);
			iconApplication = null;

			if (th instanceof ChangePasswordException)
			{
				Message message = showMessage(this, 
											  MESSAGE_ICON_INFO, 
											  MESSAGE_BUTTON_OK, 
											  th.getMessage(), 
											  "doShowChangePassword", 
											  null);
				message.centerRelativeTo(login);
			}
			else
			{
				throw th;
			}
		}
	}
	
	/**
	 * Change the password.
	 * 
	 * @param pEvent the event information from the button
	 * @throws Throwable if the password info message causes an error
	 */
	public void doChangePassword(UIActionEvent pEvent) throws Throwable
	{
		 ((MasterConnection)getConnection()).setNewPassword(login.getPassword(), login.getNewPassword());
		 
		 Message message = showMessage(this, 
								       MESSAGE_ICON_INFO, 
								       MESSAGE_BUTTON_OK, 
								       "Password changed successful!",
								       "doHideLogin", 
								       "doHideLogin");
		 
		 message.centerRelativeTo(login);
	}
	
	/**
	 * Shows the login screen.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doShowLogin(UIActionEvent pEvent)
	{
		setLoginVisible(true);
	}
	 
	/**
	 * Hides the login screen.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doHideLogin(UIActionEvent pEvent)
	{
		setLoginVisible(false);
	}

	/**
	 * Shows the change password screen.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doShowChangePassword(UIActionEvent pEvent)
	{
		setChangePasswordVisible(true);
	}
	
	/**
	 * Shows the change password screen.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doHideChangePassword(UIActionEvent pEvent)
	{
		setChangePasswordVisible(false);
	}
	
	/**
	 * Performs the exit of the application. It closes the opened connections
	 * and the application itself.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doExit(UIActionEvent pEvent)
	{
		try
		{
			logout();
		}
		catch (Throwable th)
		{
			debug(th);
			
			// Exiting the application must always be possible.
		}
		
		getLauncher().dispose();
	}
	
	/**
	 * Closes the opened connections.
	 * 
	 * @param pEvent the event information from the button
	 */
	public void doLogout(UIActionEvent pEvent)
	{
		logout();
	}
	
	/**
	 * Opens the about dialog.
	 * 
	 * @param pEvent the triggering event
	 * @throws Throwable if the about content could not be initalized
	 */
	public void doAbout(UIActionEvent pEvent) throws Throwable
	{
		About about = createAbout();

		configureFrame(about);
		
		configureAbout(about);
		
		about.pack();
		about.centerRelativeTo(getContentPane());
		about.setVisible(true);
	}
	
	/**
	 * Opens the help system.
	 * 
	 * @param pEvent the event from the menu
	 * @throws Throwable if the help is not available
	 */
	public void doHelp(UIActionEvent pEvent) throws Throwable
	{
	}

	/**
	 * Gets whether the exit options are visible.
	 * 
	 * @return <code>true</code> if the exit options are visible, <code>false</code> otherwise
	 */
	protected boolean isExitVisible()
	{
		String sExit = getLauncher().getParameter(PARAM_MENU_EXIT_VISIBLE);
		
		return sExit == null || Boolean.parseBoolean(sExit);
	}
	
}	// Application
