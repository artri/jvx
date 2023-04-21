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
 * 01.10.2008 - [HM] - creation
 * 01.12.2008 - [JR] - UI redesign
 */
package research;

import java.awt.Dimension;
import java.awt.Font;

import javax.rad.application.IContent;
import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIRectangle;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIPasswordField;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.container.UIToolBarPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.genui.menu.UICheckBoxMenuItem;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IFont;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.IPasswordField;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.component.ITextField;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.ui.menu.ICheckBoxMenuItem;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.MaskFormatter;

import com.sibvisions.rad.ui.swing.ext.JVxCalendarPane;
import com.sibvisions.rad.ui.swing.ext.JVxComboBase;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Test implementation of IApplication.
 * 
 * @author Martin Handsteiner
 */
public class LookAndFeelTest extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the selected tab at application start. */
	private static final int DEFAULT_TAB = 3;

	/** the main panel. */
	private IPanel main	= new UIPanel();
	
	
	/** the top tabs. */
	private ITabsetPanel tabTop = new UITabsetPanel();
	/** the left tabs. */
	private ITabsetPanel tabLeft = new UITabsetPanel();
	/** the right tabs. */
	private ITabsetPanel tabRight = new UITabsetPanel();
	/** the left tabs. */
	private ITabsetPanel tabBottom = new UITabsetPanel();
	
	
	/** the desktop panel for internal frames. */
	private IDesktopPanel dpanDesktop;
	/** the internal frame for icon switching. */
	private IInternalFrame ifrNoButtons;
	/** an empty internal frame. */
	private IInternalFrame ifrInActive;
	/** the internal frame for creating internal frames. */
	private IInternalFrame ifrActive;
	/** the current x coordinate for new internal frames. */
	private int iXNew = 0;
	/** the current y coordinate for new internal frames. */
	private int iYNew = 0;
	/** the switched icon state. */
	private boolean bIconSwitch = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** 
	 * Public Application Constructor. 
	 * 
	 * @param pLauncher the application launcher
	 */
	public LookAndFeelTest(UILauncher pLauncher)
	{
		super(pLauncher);
		
		setName("LookAndFeel Test");
		
		//-----------------------------------------------------------
		// MENU
		//-----------------------------------------------------------
		
		IMenuBar mbMain = createMainMenu();

		//-----------------------------------------------------------
		// TOOLBAR
		//-----------------------------------------------------------

		IToolBar tbToolBar = createMainToolBar();
		
		//-----------------------------------------------------------
		// TAB tab
		//-----------------------------------------------------------

		IPanel panTabs = createTabTabs();	
		
		//-----------------------------------------------------------
		// SPLIT tab
		//-----------------------------------------------------------
		
		ISplitPanel panSplit = createTabSplitPanel();
		
		//-----------------------------------------------------------
		// TABLE tab
		//-----------------------------------------------------------
		
		IPanel panTable = createTabTable();
		
		//-----------------------------------------------------------
		// TEXTCOMPONENT tab
		//-----------------------------------------------------------
		
		IPanel panTextComponents = createTabTextComponents();
		
		//-----------------------------------------------------------
		// RADIOBUTTON/CHECKBOX tab
		//-----------------------------------------------------------

		IPanel panRadioCheck = createTabRadioCheck();
		
		//-----------------------------------------------------------
		// DESKTOPPANEL tab
		//-----------------------------------------------------------
		
		IPanel panDesktop = createTabDesktopPanel();
		
		//-----------------------------------------------------------
		// TOOLTIP tab
		//-----------------------------------------------------------
		
		IToolBarPanel panToolTip = createTabToolTip();
		
		//-----------------------------------------------------------
		// BUTTONS tab
		//-----------------------------------------------------------
		
		IPanel panButtons = createTabButtons();
		
		//-----------------------------------------------------------
		// FRAME/DIALOG tab
		//-----------------------------------------------------------
		
		IPanel panDialogFrame = createTabFrameDialog(); 
		
		//-----------------------------------------------------------
		// Layout
		//-----------------------------------------------------------
		
		main.setLayout(new UIBorderLayout());

		pLauncher.setMenuBar(mbMain);
		
		ITabsetPanel tabMain = new UITabsetPanel();
		
		tabMain.add(panTabs, "TabbedPane");
		tabMain.add(panSplit, "SplitPane");
		tabMain.add(panTable, "JTable");
		tabMain.add(panTextComponents, "Textcomponents");
		tabMain.add(panRadioCheck, "Radio/Check");
		tabMain.add(panDesktop, "Desktop");
		tabMain.add(panToolTip, "Tooltip");
		tabMain.add(panButtons, "Buttons");
		tabMain.add(panDialogFrame, "Dialog/Frame");
		

		pLauncher.addToolBar(tbToolBar);
		
		setLayout(new UIBorderLayout());

		add(tabMain, IBorderLayout.CENTER);
		
		setPreferredSize(new UIDimension(1024, 550));
		
		tabMain.setSelectedIndex(DEFAULT_TAB);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public UIDesktopPanel getContentPane()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <OP> IContent showMessage(OP pOpener, 
								     int pIconType, 
								     int pButtonType, 
								     String pMessage, 
								     String pOkAction, 
								     String pCancelAction) throws Throwable
	{
		System.out.println(pMessage);
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyVisible()
	{
		ifrInActive.setVisible(true);
		ifrActive.setVisible(true);
		ifrNoButtons.setVisible(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyDestroy()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates the application menu.
	 * 
	 * @return the menu
	 */
	private IMenuBar createMainMenu()
	{
		//File
		
		IMenu menDatei = new UIMenu();
		menDatei.setText("File");

		IMenu menBearbeiten = new UIMenu();
		menBearbeiten.setText("Edit");
		
		IMenu menDateiSub = new UIMenu();
		menDateiSub.setText("Sub");
		
		IMenu menWindow = new UIMenu();
		menWindow.setText("Window");
		
		IMenu menSearch = new UIMenu();
		menSearch.setText("Search");
		
		IMenu menLaF = new UIMenu();
		menLaF.setText("Look and Feel");

		IMenu menHelp = new UIMenu();
		menHelp.setText("Help");
		
		
		IMenu menDateiSubDisabled = new UIMenu();
		menDateiSubDisabled.setText("Sub disabled");
		menDateiSubDisabled.setEnabled(false);
		
		IMenuItem miDateiNeu = new UIMenuItem();
		miDateiNeu.setText("New");
		miDateiNeu.setAccelerator(Key.getKey('N', UIEvent.CTRL_MASK));
		miDateiNeu.eventAction().addListener(this, "doMenuNew");

		IMenuItem miDateiDisabled = new UIMenuItem();
		miDateiDisabled.setText("Disabled");
		miDateiDisabled.setEnabled(false);
		
		IMenuItem miImage = new UIMenuItem();
		miImage.setText("Image");
		miImage.setImage(UIImage.getImage("/com/sibvisions/rad/application/images/16x16/view.png"));
		
		IMenuItem miDateiEnde = new UIMenuItem();
		miDateiEnde.setText("Exit");
		miDateiEnde.setEnabled(false);
		miDateiEnde.setAccelerator(Key.getKey('E', UIEvent.CTRL_MASK));
		
		IMenuItem miDateiSubInsert = new UIMenuItem();
		miDateiSubInsert.setText("Insert");
		
		IMenuItem miDateiSubDelete = new UIMenuItem();
		miDateiSubDelete.setText("Delete");
		
		IMenuItem miDateiSubDisabledInvisible = new UIMenuItem();
		miDateiSubDisabledInvisible.setText("invisible");
		
		menDateiSub.add(miDateiSubInsert);
		menDateiSub.add(miDateiSubDelete);
		
		menDateiSubDisabled.add(miDateiSubDisabledInvisible);

		menDatei.add(miDateiNeu);
		menDatei.add(miDateiDisabled);
		menDatei.add(menDateiSubDisabled);
		menDatei.add(menDateiSub);
		menDatei.add(miImage);
		menDatei.addSeparator();
		menDatei.add(miDateiEnde);

		//Edit
		
		ICheckBoxMenuItem cbmiCheck = new UICheckBoxMenuItem();
		cbmiCheck.setText("Checkbox");
		cbmiCheck.setImage(UIImage.getImage("/com/sibvisions/rad/application/images/16x16/help.png"));
		
		ICheckBoxMenuItem cbmiCheckDisabled = new UICheckBoxMenuItem();
		cbmiCheckDisabled.setText("Checkbox disabled");
		cbmiCheckDisabled.setEnabled(false);
		
		JRadioButtonMenuItem cbmiRadio = new JRadioButtonMenuItem("Radio");
		cbmiRadio.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/application/images/16x16/check.png"));
		
		JRadioButtonMenuItem cbmiRadio2 = new JRadioButtonMenuItem("Radio2");
		JRadioButtonMenuItem cbmiRadioDisabled = new JRadioButtonMenuItem("Radio disabled");
		
		IMenuItem miSimple = new UIMenuItem();
		miSimple.setText("Simple");

		cbmiCheckDisabled.setFont(new UIFont("dialog", IFont.BOLD, 20));
		cbmiCheckDisabled.setSelected(true);
		
		cbmiRadio2.setFont(new Font("dialog", Font.BOLD, 20));
		
		cbmiRadioDisabled.setSelected(true);
		cbmiRadioDisabled.setEnabled(false);
		
		IMenuItem miBearbeitenImage = new UIMenuItem();
		miBearbeitenImage.setText("Image");
		miBearbeitenImage.setImage(UIImage.getImage("/com/sibvisions/rad/application/images/16x16/help.png"));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(cbmiRadio);
		bg.add(cbmiRadio2);
		bg.add((JCheckBoxMenuItem)cbmiCheck.getResource());
		
		menBearbeiten.add(cbmiCheck);
		((JMenu)menBearbeiten.getResource()).add(cbmiRadio);
		((JMenu)menBearbeiten.getResource()).add(cbmiRadio2);
		menBearbeiten.add(cbmiCheckDisabled);
		((JMenu)menBearbeiten.getResource()).add(cbmiRadioDisabled);
		menBearbeiten.add(miBearbeitenImage);
		menBearbeiten.add(miSimple);
		
		JRadioButtonMenuItem miSearchWith = new JRadioButtonMenuItem("With");
		IMenuItem miSearchWithout = new UIMenuItem();
		miSearchWithout.setText("Without");
		
		((JMenu)menSearch.getResource()).add(miSearchWith);
		menSearch.add(miSearchWithout);
		
		//Window
		
		JCheckBoxMenuItem miWindowWith = new JCheckBoxMenuItem("With");
		IMenuItem miWindowWithout = new UIMenuItem();
		miWindowWithout.setText("Without");
		
		((JMenu)menWindow.getResource()).add(miWindowWith);
		menWindow.add(miWindowWithout);
		
		//Look and Feel
		IMenuItem miLaFSmart = new UIMenuItem();
		miLaFSmart.setText("Smart");
		miLaFSmart.eventAction().addListener(this, "doSmartLaF");
		
		IMenuItem miLaFWindows = new UIMenuItem();
		miLaFWindows.setText("Windows");
		miLaFWindows.eventAction().addListener(this, "doWindowsLaF");
		
		IMenuItem miLaFMetal = new UIMenuItem();
		miLaFMetal.setText("Metal");
		miLaFMetal.eventAction().addListener(this, "doMetalLaF");
		
		IMenuItem miLaFNimbus = new UIMenuItem();
		miLaFNimbus.setText("Nimbus");
		miLaFNimbus.eventAction().addListener(this, "doNimbusLaF");
		
		menLaF.add(miLaFSmart);
		menLaF.add(miLaFWindows);
		menLaF.add(miLaFMetal);
		menLaF.add(miLaFNimbus);
		
		//Help
		IMenuItem miHelpInfo = new UIMenuItem();
		miHelpInfo.setText("Info");
		
		IMenuItem miHelpAbout = new UIMenuItem();
		miHelpAbout.setText("About");
		
		menHelp.add(miHelpInfo);
		menHelp.add(miHelpAbout);
		
		IMenuBar mbMain = new UIMenuBar();
		
		mbMain.add(menDatei);
		mbMain.add(menBearbeiten);
		mbMain.add(menSearch);
		mbMain.add(menLaF);
		mbMain.add(menWindow);
		mbMain.add(menHelp);

		
		return mbMain;
	}
	
	/**
	 * Creates the application toolbar.
	 * 
	 * @return the toolbar
	 */
	private IToolBar createMainToolBar()
	{
		IToolBar tbToolBar = new UIToolBar();
		IToolBar tbDefault = new UIToolBar();
		IToolBar tbStyles  = new UIToolBar();
		
		IButton		  butExit	  = new UIButton();
		IToggleButton butLoggedOn = new UIToggleButton();
		IToggleButton butScroll   = new UIToggleButton();
		
		configureToolBarButton(butExit, "Exit", "doExit", "/com/sibvisions/rad/application/images/24x24/exit.png");
		configureToolBarButton(butLoggedOn, "Login", null, "/com/sibvisions/rad/application/images/24x24/lock_open.png");
		butLoggedOn.setPressedImage(UIImage.getImage("/com/sibvisions/rad/application/images/24x24/lock_ok.png"));
		butLoggedOn.setSelected(true);
		butLoggedOn.setEnabled(false);
		
		configureToolBarButton(butScroll, "Help", null, "/com/sibvisions/rad/application/images/24x24/information.png");
		butScroll.setSelected(true);

		tbDefault.add(butExit);
		tbDefault.add(butLoggedOn);

		tbStyles.add(butScroll);

		tbToolBar.add(tbDefault, null);
		tbToolBar.add(tbStyles, null);

		
		return tbToolBar;
	}
	
	/**
	 * Creates the tab for split panel tests.
	 * 
	 * @return the panel
	 */
	private ISplitPanel createTabSplitPanel()
	{
		ISplitPanel panSplit = new UISplitPanel();
		
		ISplitPanel panSplitVert = new UISplitPanel();
		panSplitVert.setOrientation(ISplitPanel.SPLIT_TOP_BOTTOM);

		panSplit.add(new UIPanel(), null);

		panSplitVert.add(new UIPanel(), null);
		panSplitVert.add(new UIPanel(), null);
		
		panSplit.add(panSplitVert, null);
		
		
		return panSplit;
	}
	
	/**
	 * Creates the tab for table tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabTable()
	{
		IPanel panTable = new UIPanel();
		
		panTable.setLayout(new UIBorderLayout());
		
		int iRows = 2500;
		
		Object[][] oValues = new Object[iRows][3];
		
		for (int i = 0; i < iRows; i++)
		{
			oValues[i][0] = "" + i;
			oValues[i][1] = "René";
			oValues[i][2] = "Jahn";
		}
		
		JTable tbl = new JTable(oValues, new String[] {"#", "Name", "Nachname"});
		tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbl.getColumnModel().getColumn(2).setMinWidth(100000);
		
		JScrollPane scp = new JScrollPane(tbl, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		((java.awt.Container)panTable.getResource()).setLayout(new java.awt.GridLayout());
		((java.awt.Container)panTable.getResource()).add(scp);
		
		
		return panTable;
	}
	
	/**
	 * Creates the tab for text component tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabTextComponents()
	{
		IPanel panTab = new UIPanel();
		panTab.setLayout(new UIBorderLayout());
		
		IPanel panTabDetails = new UIPanel();
		panTabDetails.setLayout(new UIFormLayout());
		
		ILabel lblInfo = new UILabel();
		lblInfo.setText("<html><ul><li>Optische Kontrolle: Disabled/Locked sehen ident aus, Editable/Not editable ebenfalls aber copy/pase ist möglich</li>" +
				        "<li>Spinner und Combo Buttons sind gleich breit und die Scrollbars der Combo Liste sind ebenfalls gleich breit</li>" +
				        "<li>Insets sind in allen Feldern ident</li>" +
				        "<li>ComboBase: geöffnete Liste wird geschlossen bei Click auf ComboBox not editable Editor oder ComboBox Arrow</li>" +
				        "<li>ComboBase: Bei Click auf ComboBox Arrow oder not editable Text wird die ComboBox Liste geöffnet</li>" +
				        "<li>ComboBase: Fokus/Cursor auf editable ComboBox Editor setzen, danach Click auf Button und danach click auf " +
				        "not editable Text in ComboBox öffnet die ComboBox Liste</li>" +
				 		"</ul></html>");
		
		panTabDetails.add(lblInfo);

		IPanel panTextComponents = new UIPanel();
		
		UIFormLayout flText = new UIFormLayout();
		flText.setNewlineCount(6);
		panTextComponents.setLayout(flText);
		
		ILabel lblDefault = new UILabel();
		lblDefault.setText("Default");
		
		ILabel lblNotEdit = new UILabel();
		lblNotEdit.setText("Not editable");
		
		ILabel lblTextFieldDisabled = new UILabel();
		lblTextFieldDisabled.setText("Disabled");

		ILabel lblLocked = new UILabel();
		lblLocked.setText("Locked");

		ILabel lblNoTextBorder = new UILabel();
		lblNoTextBorder.setText("No border");

		// ITextField
		
		ILabel lblITextField = new UILabel();
		lblITextField.setText("ITextField");

		ITextField tfDefault = new UITextField();
		tfDefault.setText("Default");
		tfDefault.setName("Default");
		
		ITextField tfNotEdit = new UITextField();
		tfNotEdit.setText("Not editable");
		tfNotEdit.setName("Not editable");
		tfNotEdit.setEditable(false);
		
		ITextField tfDisabled = new UITextField();
		tfDisabled.setText("Disabled");
		tfDisabled.setName("Disabled");
		tfDisabled.setEnabled(false);

		ITextField tfLocked = new UITextField();
		tfLocked.setText("Not editable & disabled");
		tfLocked.setName("Not editable & disabled");
		tfLocked.setEditable(false);
		tfLocked.setEnabled(false);
		
		// ITextField (user-defined color)
		ILabel lblITextFieldColor = new UILabel();
		lblITextFieldColor.setText("ITextField (color)");
		lblITextFieldColor.setBackground(new UIColor(255, 224, 192));

		ITextField tfDefaultColor = new UITextField();
		tfDefaultColor.setText("Default");
		tfDefaultColor.setName("Default");
		tfDefaultColor.setBackground(new UIColor(255, 224, 192));
		
		ITextField tfNotEditColor = new UITextField();
		tfNotEditColor.setText("Not editable");
		tfNotEditColor.setName("Not editable");
		tfNotEditColor.setEditable(false);
		tfNotEditColor.setBackground(new UIColor(255, 224, 192));
		
		ITextField tfDisabledColor = new UITextField();
		tfDisabledColor.setText("Disabled");
		tfDisabledColor.setName("Disabled");
		tfDisabledColor.setEnabled(false);
		tfDisabledColor.setBackground(new UIColor(255, 224, 192));

		ITextField tfLockedColor = new UITextField();
		tfLockedColor.setText("Not editable & disabled");
		tfLockedColor.setName("Not editable & disabled");
		tfLockedColor.setEditable(false);
		tfLockedColor.setEnabled(false);
		tfLockedColor.setBackground(new UIColor(255, 224, 192));
		
		//FormattedTextField
		ILabel lblFormatLabel = new UILabel();
		lblFormatLabel.setText("Formatted TextField");

		MaskFormatter mf;
		
		try
		{
			mf = new MaskFormatter("###.###.###.###");
		}
		catch (Exception e)
		{
			mf = null;
		}
		
		JFormattedTextField tffDefault = new JFormattedTextField(mf);
		tffDefault.setText("123456789012");
		tffDefault.setColumns(4);
		
		JFormattedTextField tffNotEdit = new JFormattedTextField(mf);
		tffNotEdit.setText("123456789012");
		tffNotEdit.setColumns(4);
		tffNotEdit.setEditable(false);
		
		JFormattedTextField tffDisabled = new JFormattedTextField(mf);
		tffDisabled.setText("123456789012");
		tffDisabled.setColumns(4);
		tffDisabled.setEnabled(false);
		
		JFormattedTextField tffLocked = new JFormattedTextField(mf);
		tffLocked.setText("123456789012");
		tffLocked.setColumns(4);
		tffLocked.setEditable(false);
		tffLocked.setEnabled(false);
		
		//ComboBox
		ILabel lblComboBox = new UILabel();
		lblComboBox.setText("ComboBox");
		
		ILabel lblComboBase = new UILabel();
		lblComboBase.setText("ComboBase");
		
		JComboBox cbxDefault = new JComboBox(new Object[] {"First", "Second", "Third", "4", "5", "6", "7", "8", "9", "10", "11"});
		cbxDefault.setEditable(true);
		
		JComboBox cbxNotEdit = new JComboBox(new Object[] {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"});
		JComboBox cbxDisabled = new JComboBox(new Object[] {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"});
		cbxDisabled.setEnabled(false);
		JComboBox cbxLocked = new JComboBox(new Object[] {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"});
		cbxLocked.setEnabled(false);
		cbxLocked.setEditable(false);
		
		//Password
		ILabel lblPassword = new UILabel();
		lblPassword.setText("Password");
		
		IPasswordField pwfDefault = new UIPasswordField();
		pwfDefault.setText("Hallo");
		
		IPasswordField pwfNotEdit = new UIPasswordField();
		pwfNotEdit.setEditable(false);
		pwfNotEdit.setText("Hallo");
		
		IPasswordField pwfDisabled = new UIPasswordField();
		pwfDisabled.setEnabled(false);
		pwfDisabled.setText("Hallo");
		
		IPasswordField pwfLocked = new UIPasswordField();
		pwfLocked.setEnabled(false);
		pwfLocked.setEditable(false);
		pwfLocked.setText("Hallo");
		
		//Spinner
		ILabel lblSpinner = new UILabel();
		lblSpinner.setText("Spinner");
		
		JSpinner spinDefault = new JSpinner();
		
		JSpinner spinDisabled = new JSpinner();
		spinDisabled.setEnabled(false);
		
		//TextArea
		ILabel lblTextArea = new UILabel();
		lblTextArea.setText("TextArea");
		
		JTextArea taDefault = new JTextArea();
		taDefault.setText("A\nB\nC\nD\nE\nF\nZZZZZZZZZZZZZZZZ");

		JTextArea taNotEdit = new JTextArea();
		taNotEdit.setText("A\nB\nC\nD\nE\nF\nZZZZZZZZZZZZZZZZ");
		taNotEdit.setEditable(false);
		
		JTextArea taDisabled = new JTextArea();
		taDisabled.setText("A\nB\nC\nD\nE\nF\nZZZZZZZZZZZZZZZZ");
		taDisabled.setEnabled(false);
		
		JTextArea taLocked = new JTextArea();
		taLocked.setText("A\nB\nC\nD\nE\nF\nZZZZZZZZZZZZZZZZ");
		taLocked.setEditable(false);
		taLocked.setEnabled(false);
		
		ITextArea taNoBorder = new UITextArea();
		taNoBorder.setText("A\nB\nC\nD\nE\nF\nZZZZZZZZZZZZZZZZ");

		JScrollPane spDefault = new JScrollPane(taDefault);
		JScrollPane spNotEdit = new JScrollPane(taNotEdit);
		JScrollPane spDisabled = new JScrollPane(taDisabled);
		JScrollPane spLocked = new JScrollPane(taLocked);

		panTextComponents.add(new UILabel());
		panTextComponents.add(lblDefault);
		panTextComponents.add(lblNotEdit);
		panTextComponents.add(lblTextFieldDisabled);
		panTextComponents.add(lblLocked);
		panTextComponents.add(lblNoTextBorder);
		
		panTextComponents.add(lblITextField, JVxFormLayout.NEWLINE);
		panTextComponents.add(tfDefault);
		panTextComponents.add(tfNotEdit);
		panTextComponents.add(tfDisabled);
		panTextComponents.add(tfLocked);
		
		panTextComponents.add(lblITextFieldColor, UIFormLayout.NEWLINE);
		panTextComponents.add(tfDefaultColor);
		panTextComponents.add(tfNotEditColor);
		panTextComponents.add(tfDisabledColor);
		panTextComponents.add(tfLockedColor);

		panTextComponents.add(lblFormatLabel, UIFormLayout.NEWLINE);
		((JPanel)panTextComponents.getResource()).add(tffDefault);
		((JPanel)panTextComponents.getResource()).add(tffNotEdit);
		((JPanel)panTextComponents.getResource()).add(tffDisabled);
		((JPanel)panTextComponents.getResource()).add(tffLocked);
		
		panTextComponents.add(lblComboBox, UIFormLayout.NEWLINE);
		((JComponent)panTextComponents.getResource()).add(cbxDefault);
		((JComponent)panTextComponents.getResource()).add(cbxNotEdit);
		((JComponent)panTextComponents.getResource()).add(cbxDisabled);
		((JComponent)panTextComponents.getResource()).add(cbxLocked);
		
		panTextComponents.add(lblComboBase, UIFormLayout.NEWLINE);
//		((JComponent)panTextComponents.getResource()).add(new javax.swing.JComboBox(), ISimpleFormLayout.DEFAULT);
		
		JVxComboBase base = new com.sibvisions.rad.ui.swing.ext.JVxComboBase();
//		base.setEditable(false);
		base.setPopupComponent(new JVxCalendarPane());
		
		((JComponent)panTextComponents.getResource()).add(base);
		
		panTextComponents.add(lblPassword, JVxFormLayout.NEWLINE);
		panTextComponents.add(pwfDefault);
		panTextComponents.add(pwfNotEdit);
		panTextComponents.add(pwfDisabled);
		panTextComponents.add(pwfLocked);
		
		panTextComponents.add(lblSpinner, JVxFormLayout.NEWLINE);
		((JComponent)panTextComponents.getResource()).add(spinDefault);
		panTextComponents.add(new UILabel());
		((JComponent)panTextComponents.getResource()).add(spinDisabled);

		panTextComponents.add(lblTextArea, JVxFormLayout.NEWLINE);
		((JComponent)panTextComponents.getResource()).add(spDefault);
		((JComponent)panTextComponents.getResource()).add(spNotEdit);
		((JComponent)panTextComponents.getResource()).add(spDisabled);
		((JComponent)panTextComponents.getResource()).add(spLocked);
		panTextComponents.add(taNoBorder);
		
		spDefault.setPreferredSize(new Dimension(100, 100));
		spNotEdit.setPreferredSize(new Dimension(100, 100));
		spDisabled.setPreferredSize(new Dimension(100, 100));
		spLocked.setPreferredSize(new Dimension(100, 100));
		
		panTab.add(panTextComponents, IBorderLayout.CENTER);
		panTab.add(panTabDetails, IBorderLayout.SOUTH);
		
		
		return panTab;
	}
	
	/**
	 * Creates the tab for radio and checkbox tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabRadioCheck()
	{
		IPanel panTab = new UIPanel();
		panTab.setLayout(new UIBorderLayout());
		
		IPanel panTabDetails = new UIPanel();
		panTabDetails.setLayout(new UIFormLayout());
		
		ILabel lblInfo = new UILabel();
		lblInfo.setText("<html><ul><li>Checkboxen: Fontunabhängiger Abstand zum Rahmen und 1/3 : 2/3 Verhältnis des Arrows</li>" +
				 		"</ul></html>");
		
		panTabDetails.add(lblInfo);

		IPanel panRadioCheck = new UIPanel();
		panRadioCheck.setLayout(new UIFormLayout());
		
		JRadioButton rb;
		JCheckBox cb;
		
		for (int i = 0; i < 15; i++)
		{
			rb = new JRadioButton("Radio button");
			rb.setFont(new Font("dialog", Font.PLAIN, 10 + i));
			
			cb = new JCheckBox("Checkbox");
			cb.setFont(new Font("dialog", i % 3 == 0 ? Font.BOLD : Font.PLAIN, 10 + i));
			
			cb.setEnabled(i % 2 == 0);
			cb.setSelected(i % 3 == 0);

			rb.setEnabled(i % 2 == 0);
			rb.setSelected(i % 3 == 0);
			
			((JComponent)panRadioCheck.getResource()).add(rb, i > 0 ? IFormLayout.NEWLINE : null);
			((JComponent)panRadioCheck.getResource()).add(cb, null);
		}

		panTab.add(panRadioCheck, IBorderLayout.CENTER);
		panTab.add(panTabDetails, IBorderLayout.SOUTH);
		
		return panTab;
	}
	
	/**
	 * Creates the tab for tabbed pane tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabTabs()
	{
		IPanel panTabDetails = new UIPanel();
		panTabDetails.setLayout(new UIFormLayout());
		
		ILabel lblInfo = new UILabel();
		lblInfo.setText("<html><ul><li>Vertikales draggen der Tabs (disabled und mehrzeilige Reiter sind nicht draggable)</li>" +
				        "<li>MouseOver bei close icons</li>" +
				        "<li>Scroll Modus: MouseOver bei hintersten Tabs muss funktionieren</li>" +
				        "<li>Scroll Modus: Nach Schließen eines Bottom Tabs sollte MouseOver wieder sichtbar sein</li>" +
				 		"</ul></html>");
		
		panTabDetails.add(lblInfo);
		
		IPanel panTabOptions = new UIPanel();
		panTabOptions.setLayout(new UIFormLayout());
		
		IToggleButton butScrollableTabs = new UIToggleButton();
		butScrollableTabs.setText("Scrollable");
		butScrollableTabs.eventAction().addListener(this, "doSwitchScrollOption");
		
		panTabOptions.add(butScrollableTabs);

		
		tabLeft.setTabPlacement(ITabsetPanel.PLACEMENT_LEFT);
		tabRight.setTabPlacement(ITabsetPanel.PLACEMENT_RIGHT);
		tabBottom.setTabPlacement(ITabsetPanel.PLACEMENT_BOTTOM);
		
		/* Tabset Panels */
		IPanel panTabs = new UIPanel();
		panTabs.setLayout(new UIBorderLayout());
		
		IPanel panTop = new UIPanel();
		panTop.setLayout(new UIBorderLayout());

		IPanel panLeft = new UIPanel();
		panLeft.setLayout(new UIBorderLayout());

		IPanel panRight = new UIPanel();
		panRight.setLayout(new UIBorderLayout());
		
		IPanel panBottom = new UIPanel();
		panBottom.setLayout(new UIBorderLayout());

		/* Tabs zusammensetzen */
		
		tabTop.add(panTop, "Top");
		for (int i = 0; i < 25; i++)
		{
			tabTop.add(new UIPanel(), "Top " + (i + 1));
		}
		tabTop.setEnabled(false);
		
		tabTop.setIconAt(0, UIImage.getImage("/com/sibvisions/rad/application/images/16x16/view.png"));
		
		panTop.add(tabLeft, IBorderLayout.CENTER);
		
		tabLeft.setDraggable(true);
		
		tabLeft.add(panLeft, "Left");
		for (int i = 0; i < 8; i++)
		{
			tabLeft.add(new UIPanel(), "Drag me - " + (i + 2));
		}		
		tabLeft.setEnabledAt(1, false);
		tabLeft.setIconAt(0, UIImage.getImage("/com/sibvisions/rad/application/images/16x16/view.png"));

		panLeft.add(tabBottom, IBorderLayout.CENTER);
		
		tabBottom.add(panBottom, "Bottom");
		for (int i = 0; i < 15; i++)
		{
			tabBottom.add(new UIPanel(), "Close me " + (i + 1));
			tabBottom.setClosableAt(i + 1, true);
		}
		tabBottom.setEnabledAt(3, false);
		tabBottom.setIconAt(0, UIImage.getImage("/com/sibvisions/rad/application/images/16x16/view.png"));

		panBottom.add(tabRight, IBorderLayout.CENTER);
		
		tabRight.add(panRight, "Right");
		for (int i = 0; i < 8; i++)
		{
			tabRight.add(new UIPanel(), "Right - " + (i + 2));
		}		
		tabRight.setEnabledAt(2, false);
		tabRight.setIconAt(0, UIImage.getImage("/com/sibvisions/rad/application/images/16x16/view.png"));
		
		panTabs.add(panTabOptions, IBorderLayout.NORTH);
		panTabs.add(tabTop, IBorderLayout.CENTER);
		panTabs.add(panTabDetails, IBorderLayout.SOUTH);

		
		return panTabs;
	}
	
	/**
	 * Creates the tab for desktop pane tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabDesktopPanel()
	{
		IPanel panTabDetails = new UIPanel();
		panTabDetails.setLayout(new UIFormLayout());
		
		ILabel lblInfo = new UILabel();
		lblInfo.setText("<html><ul><li>Umschalten in den Tab Modus</li>" +
				        "<li>Horizontales drag</li>" +
				        "<li>NO Buttons: Icon switchen und Tab drag, Tab Höhe und CLOSE an/aus prüfen</li>" +
				        "<li>INACTIVE: Neuen Frame/Tab erstellen und zwischen Tab und Frame Modus wechseln</li>" +
				        "<li>ACTIVE: Menü testen</li>" +
				        "<li>Wechsel zwischen Tab und Frame Ansicht muss das aktive Frame/Tab übernehmen</li>" +
				 		"</ul></html>");
		
		panTabDetails.add(lblInfo);
		
		
		IMenuBar mbIFrame = new UIMenuBar();
		
		IMenu menIfrDatei = new UIMenu();
		menIfrDatei.setText("File");
		
		IMenu menIfrBearbeiten = new UIMenu();
		menIfrBearbeiten.setText("Edit");
		
		IMenuItem menIfrDateiNeu = new UIMenuItem();
		menIfrDateiNeu.setText("New");
		
		IMenuItem menIfrDateiEnde = new UIMenuItem();
		menIfrDateiEnde.setText("Close");
		
		menIfrDatei.add(menIfrDateiNeu);
		menIfrDatei.addSeparator();
		menIfrDatei.add(menIfrDateiEnde);
		
		mbIFrame.add(menIfrDatei);
		mbIFrame.add(menIfrBearbeiten);
		
		
		IPanel panDesktop = new UIPanel();
		panDesktop.setLayout(new UIBorderLayout());
		
		IPanel panDesktopOptions = new UIPanel();
		panDesktopOptions.setLayout(new UIFormLayout());
		
		IToggleButton tbutTabView = new UIToggleButton();
		tbutTabView.setText("Tabbed view");
		tbutTabView.eventAction().addListener(this, "doSwitchTabView");
		
		dpanDesktop = new UIDesktopPanel();
		
		ifrInActive = new UIInternalFrame(dpanDesktop);
		ifrInActive.setBounds(new UIRectangle(500, 10, 200, 200));
		ifrInActive.setTitle("INACTIVE");
		
		ifrInActive.setLayout(new UIFormLayout());
		
		IButton butNewInternalFrame = new UIButton();
		butNewInternalFrame.setText("New internal frame");
		butNewInternalFrame.eventAction().addListener(this, "doNewInternalFrame");
		
		ifrInActive.add(butNewInternalFrame);

		ifrActive = new UIInternalFrame(dpanDesktop);
		ifrActive.setBounds(new UIRectangle(10, 10, 300, 200));
		ifrActive.setTitle("ACTIVE");
		ifrActive.setIconImage(null);
		
		ifrActive.setMenuBar(mbIFrame);
		
		ifrNoButtons = new UIInternalFrame(dpanDesktop);
		ifrNoButtons.setBounds(new UIRectangle(10, 250, 300, 100));
		ifrNoButtons.setMaximizable(false);
		ifrNoButtons.setIconifiable(false);
		ifrNoButtons.setClosable(false);
		ifrNoButtons.setIconImage(UIImage.getImage("/com/sibvisions/rad/swing/plaf/smart/images/close.png"));
		ifrNoButtons.setTitle("NO BUTTONS");

		IPanel panNoButtons = new UIPanel();
		panNoButtons.setLayout(new UIFormLayout());
		
		ifrNoButtons.setLayout(new UIBorderLayout());
		
		IButton butSwitchIcon = new UIButton();
		butSwitchIcon.setText("Switch frame icon");
		butSwitchIcon.eventAction().addListener(this, "doSwitchIcon");
		
		panNoButtons.add(butSwitchIcon);
		
		ifrNoButtons.add(panNoButtons, IBorderLayout.CENTER);
		
		panDesktopOptions.add(tbutTabView);
		
		panDesktop.add(panDesktopOptions, IBorderLayout.NORTH);
		panDesktop.add(dpanDesktop, IBorderLayout.CENTER);
		panDesktop.add(panTabDetails, IBorderLayout.SOUTH);

		
		return panDesktop;
	}
	
	/**
	 * Creates the tab for tooltip tests.
	 * 
	 * @return the panel
	 */
	private IToolBarPanel createTabToolTip()
	{
		IPanel panTabDetails = new UIPanel();
		panTabDetails.setLayout(new UIFormLayout());
		
		ILabel lblInfo = new UILabel();
		lblInfo.setText("<html><ul><li>Disabled und Enabled ToolTip bei MouseOver</li>" +
				 		"</ul></html>");
		
		panTabDetails.add(lblInfo);

		IToolBarPanel panToolTip = new UIToolBarPanel();
		panToolTip.setLayout(new UIBorderLayout());

		
		IToolBar tbToolTip  = new UIToolBar();

		IButton butToolBarEnabled  = new UIButton();
		IButton butToolBarDisabled = new UIButton();
		
		butToolBarDisabled.setEnabled(false);
		
		configureToolBarButton(butToolBarDisabled, null, null, "/com/sibvisions/rad/application/images/24x24/information.png");
		butToolBarDisabled.setToolTipText("Disabled text");
		
		configureToolBarButton(butToolBarEnabled, null, null, "/com/sibvisions/rad/application/images/24x24/lock_open.png");
		butToolBarEnabled.setToolTipText("Enabled text");
		
		tbToolTip.add(butToolBarDisabled, null);
		tbToolTip.add(butToolBarEnabled, null);
		
		panToolTip.addToolBar(tbToolTip);
		panToolTip.add(panTabDetails, IBorderLayout.SOUTH);
		
		return panToolTip;
	}
	
	/**
	 * Creates the tab for button tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabButtons()
	{
		UIFormLayout foButtons = new UIFormLayout();
		foButtons.setNewlineCount(3);
		
		IPanel panButtons = new UIPanel();
		panButtons.setLayout(foButtons);

		IButton butEnabled = new UIButton();
		butEnabled.setText("Enabled");
//		butEnabled.eventAction().addListener(this, "doEnabled");
		
		IButton butDisabled = new UIButton();
		butDisabled.setText("Disabled");
		butDisabled.setEnabled(false);
		
		IButton tbutEnabled = new UIToggleButton();
		tbutEnabled.setText("Enabled");
		
		IButton tbutDisabled = new UIToggleButton();
		tbutDisabled.setText("Disabled");
		tbutDisabled.setEnabled(false);

		IToggleButton tbutSelected = new UIToggleButton();
		tbutSelected.setText("Selected");
		tbutSelected.setSelected(true);
		
		IToggleButton tbutSelectedDisabled = new UIToggleButton();
		tbutSelectedDisabled.setText("Selected disabled");
		tbutSelectedDisabled.setSelected(true);
		tbutSelectedDisabled.setEnabled(false);

		ILabel lblButton = new UILabel();
		lblButton.setText("Button");
		
		ILabel lblToggleBLabel = new UILabel();
		lblToggleBLabel.setText("Toggle Button");
		
		ILabel lblEnabled = new UILabel();
		lblEnabled.setText("Enabled");
		
		ILabel lblDisabled = new UILabel();
		lblDisabled.setText("Disabled");
		
		ILabel lblSelected = new UILabel();
		lblSelected.setText("Selected");

		ILabel lblSelectedDisabled = new UILabel();
		lblSelectedDisabled.setText("Selected disabled");
		
		ILabel lblNoBorder = new UILabel();
		lblNoBorder.setText("No border");
		
		IButton butNoBorder = new UIButton();
		butNoBorder.setText("No border");
		butNoBorder.setBorderOnMouseEntered(true);
		
		IToggleButton tbutNoBorder = new UIToggleButton();
		tbutNoBorder.setText("No border");
		tbutNoBorder.setBorderOnMouseEntered(true);
		
		panButtons.add(new UILabel());
		panButtons.add(lblButton);
		panButtons.add(lblToggleBLabel);
		panButtons.add(lblEnabled, IFormLayout.NEWLINE);
		panButtons.add(butEnabled);
		panButtons.add(tbutEnabled);
		panButtons.add(lblDisabled, IFormLayout.NEWLINE);
		panButtons.add(butDisabled);
		panButtons.add(tbutDisabled);
		panButtons.add(lblSelected, IFormLayout.NEWLINE);
		panButtons.add(new UILabel());
		panButtons.add(tbutSelected);
		panButtons.add(lblSelectedDisabled, IFormLayout.NEWLINE);
		panButtons.add(new UILabel());
		panButtons.add(tbutSelectedDisabled);
		panButtons.add(lblNoBorder, IFormLayout.NEWLINE);
		panButtons.add(butNoBorder);
		panButtons.add(tbutNoBorder);

		return panButtons;
	}
	
	/**
	 * Creates the tab for dialog and frame tests.
	 * 
	 * @return the panel
	 */
	private IPanel createTabFrameDialog()
	{
		IPanel panDialogFrame =	new UIPanel();
		panDialogFrame.setLayout(new UIFormLayout());
		
		IButton butNewDialog = new UIButton();
		butNewDialog.setText("New dialog");
		butNewDialog.eventAction().addListener(this, "doNewDialog");
		
		IButton butNewModalDialog = new UIButton();
		butNewModalDialog.setText("New modal dialog");
		butNewModalDialog.eventAction().addListener(this, "doNewModalDialog");
		
		IButton butNewFrame = new UIButton();
		butNewFrame.setText("New frame");
		butNewFrame.eventAction().addListener(this, "doNewFrame");
		
		IButton butNewOptionPane = new UIButton();
		butNewOptionPane.setText("OptionPane");
		butNewOptionPane.eventAction().addListener(this, "doNewOptionPane");
		
		IButton butFileChooser = new UIButton();
		butFileChooser.setText("FileChooser");
		butFileChooser.eventAction().addListener(this, "doNewFileChooser");

		panDialogFrame.add(butNewDialog);
		panDialogFrame.add(butNewModalDialog, IFormLayout.NEWLINE);
		panDialogFrame.add(butNewFrame, IFormLayout.NEWLINE);
		panDialogFrame.add(butNewOptionPane, IFormLayout.NEWLINE);
		panDialogFrame.add(butFileChooser, IFormLayout.NEWLINE);
		
		return panDialogFrame;
	}
	
	/**
	 * Configures a standard button for the toolbar layout.
	 * 
	 * @param pButton the button
	 * @param pText the button text
	 * @param pAction the action method for the button
	 * @param pIconPath the path for the button icon
	 */
	private void configureToolBarButton(IButton pButton, String pText, String pAction, String pIconPath)
	{
		pButton.setImage(UIImage.getImage(pIconPath));
		pButton.setText(pText);
		pButton.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		pButton.setVerticalTextPosition(IAlignmentConstants.ALIGN_BOTTOM);
		pButton.setFont(new UIFont("dialog", UIFont.BOLD, 11));
		pButton.setImageTextGap(2);
		pButton.setMinimumSize(new UIDimension(50, 50));
		pButton.setFocusable(false);
		
		if (pAction != null)
		{
			pButton.eventAction().addListener(this, pAction);
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	
	/**
	 * Exits the application.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doExit(UIActionEvent pEvent)
	{
		System.exit(0);
	}

	/**
	 * Switches to smart look and feel.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doSmartLaF(UIActionEvent pEvent)
	{
		SwingFactory.setLookAndFeel(getLauncher(), SmartLookAndFeel.class.getName());
	}

	/**
	 * Switches to windows look and feel.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doWindowsLaF(UIActionEvent pEvent)
	{
		SwingFactory.setLookAndFeel(getLauncher(), "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	}
	
	/**
	 * Switches to metal look and feel.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doMetalLaF(UIActionEvent pEvent)
	{
		SwingFactory.setLookAndFeel(getLauncher(), MetalLookAndFeel.class.getName());
	}

	/**
	 * Switches to nimbus look and feel.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNimbusLaF(UIActionEvent pEvent)
	{
		SwingFactory.setLookAndFeel(getLauncher(), "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	}

	/**
	 * Sets the scroll property on tabbed panes.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doSwitchScrollOption(UIActionEvent pEvent)
	{
		int iTabPolicy = tabTop.getTabLayoutPolicy();
		
		
		if (iTabPolicy == ITabsetPanel.TAB_LAYOUT_SCROLL)
		{
			iTabPolicy = ITabsetPanel.TAB_LAYOUT_WRAP;
		}
		else
		{
			iTabPolicy = ITabsetPanel.TAB_LAYOUT_SCROLL;
		}
		
		tabTop.setTabLayoutPolicy(iTabPolicy);
		tabLeft.setTabLayoutPolicy(iTabPolicy);
		tabBottom.setTabLayoutPolicy(iTabPolicy);
		tabRight.setTabLayoutPolicy(iTabPolicy);
	}
	
	/**
	 * Switches the desktop pane from tabbed to frame view.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doSwitchTabView(UIActionEvent pEvent)
	{
		dpanDesktop.setTabMode(!dpanDesktop.isTabMode());
	}

	/**
	 * Switches the icon from the internal frame.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doSwitchIcon(UIActionEvent pEvent)
	{
		bIconSwitch = !bIconSwitch;
		
		if (bIconSwitch)
		{
			ifrNoButtons.setIconImage(UIImage.getImage("/com/sibvisions/rad/application/images/24x24/view.png"));
			
			ifrNoButtons.setMaximizable(true);
			ifrNoButtons.setClosable(true);
		}
		else
		{
			ifrNoButtons.setIconImage(UIImage.getImage("/com/sibvisions/rad/swing/plaf/smart/images/close.png"));
			ifrNoButtons.setClosable(false);
		}
	}

	/**
	 * Creates and shows a new internal frame.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewInternalFrame(UIActionEvent pEvent)
	{
		iXNew += 20;
		iYNew += 20;
		
		IInternalFrame frame = new UIInternalFrame(dpanDesktop);
		frame.setBounds(new UIRectangle(iXNew, iYNew, 100, 100));
		
		dpanDesktop.add(frame, null);
		
		frame.setVisible(true);
	}
	
	/**
	 * Creates and shows a new dialog.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewDialog(UIActionEvent pEvent)
	{
		JDialog dlg = new JDialog();
		
		dlg.setTitle("Dialog");
		dlg.setBounds(30, 30, 400, 200);
		dlg.setVisible(true);
	}
	
	/**
	 * Creates and shows a new modal dialog.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewModalDialog(UIActionEvent pEvent)
	{
		JDialog dlg = new JDialog();
		
		dlg.setTitle("Modal dialog");
		dlg.setBounds(30, 30, 400, 200);
		dlg.setModal(true);
		dlg.setVisible(true);
	}

	/**
	 * Creates and shows a new frame.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewFrame(UIActionEvent pEvent)
	{
		JFrame frame = new JFrame();
		
		frame.setTitle("Frame");
		frame.setBounds(30, 250, 400, 100);
//		frame.setIconImage(((ImageIcon)Tools.getResourceAsIcon("/com/sibvisions/rad/swing/laf/images/iframe.png")).getImage());
//		frame.setIconImage(null);
//		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * Creates and shows a new option pane.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewOptionPane(UIActionEvent pEvent)
	{
		JOptionPane optionPane = new JOptionPane("TEST", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);

		JDialog dlg = optionPane.createDialog(null, "HALLO");
		dlg.setVisible(true);
	}
	
	/**
	 * Creates and shows a file chooser.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doNewFileChooser(UIActionEvent pEvent)
	{
		JFileChooser fc = new JFileChooser();
		
		fc.setSize(300, 400);
		fc.showOpenDialog(null);
	}
	
	/**
	 * Shows an information when the new menu was pressed.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doMenuNew(UIActionEvent pEvent)
	{
		System.out.println("NEW pressed");
	}
	
}	// LookAndFeelTest
