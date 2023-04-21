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
 * 22.06.2009 - [JR] - creation
 * 22.09.2009 - [JR] - closeFrame: save before close [BUGFIX]
 * 04.10.2009 - [JR] - doChangePassword: not allowed [FEATURE]
 * 22.04.2014 - [RZ] - #1014: added test for the UILinkedCellEditor
 * 18.09.2014 - [RZ] - added a table to the UILinkedCellEditor test page
 */
package com.sibvisions.apps.simpleapp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.rad.application.IContent;
import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.IFontAwesome;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIComponent;
import javax.rad.genui.UICursor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UICheckBoxCellEditor;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.celleditor.UIDateCellEditor;
import javax.rad.genui.celleditor.UIImageViewer;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.genui.celleditor.UINumberCellEditor;
import javax.rad.genui.celleditor.UITextCellEditor;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UICheckBox;
import javax.rad.genui.component.UICustomComponent;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIMap;
import javax.rad.genui.component.UIPasswordField;
import javax.rad.genui.component.UIRadioButton;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.control.UICellFormat;
import javax.rad.genui.control.UIChart;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.control.UITree;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.genui.menu.UICheckBoxMenuItem;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.genui.menu.UIPopupMenu;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataBook.WriteBackIsolationLevel;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataRowListener;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.event.CallBackEvent;
import javax.rad.remote.event.ICallBackListener;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.IMap;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;
import javax.rad.util.event.IExceptionListener;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFactory;
import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * The <code>SimpleApplication</code> class is a customized {@link Application} for
 * demonstration purposes.
 * 
 * @author René Jahn
 */
public class SimpleApplication extends Application
                               implements IExceptionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// Data
	
	/** Statistic data book. **/
	private MemDataBook memStatistic = new MemDataBook();
	
	/** Substatistic data book from statistic data book. **/
	private MemDataBook memSubStatistic = new MemDataBook();
	
	/** Continent data book. **/
	private MemDataBook memContinent = new MemDataBook();
	
	/** Sub data book from memContinent data book. **/
	private MemDataBook memCountry = new MemDataBook();
	
	/** Sub data book from memCountry data book. **/
	private MemDataBook memCity = new MemDataBook();

	/** The data book for the data. **/
	private MemDataBook memData = new MemDataBook();
	
	/** The location data book. **/
	private MemDataBook memLocation = new MemDataBook();
	
	/** The persons data book. **/
	private MemDataBook memPersons = new MemDataBook();
	
	/** The base data book for the linked cell editor. **/
	private MemDataBook memLinkedBase = new MemDataBook();
	
	/** The "referred to" data book for the linked cell editor. **/
	private MemDataBook memLinkedReferred = new MemDataBook();
	
	// UI components
	
	/** the main/content panel. */
	private UIPanel panelMain;
	
	/** the font awesome panel. */
	private UIPanel panelFontAwesome;
	
	/** the panel for the charts. **/
	private UIPanel panelCharts;
	
	/** the panel for the tree. **/
	private UIPanel panelTree;
	
	/** the panel for the map. **/
	private UIPanel panelMap;

	/** the panel for the table. **/
	private UIPanel panelTable;
	
	/** the panel for the components. **/
	private UIPanel panelComponents;
	
	/** the panel for the components. **/
	private UIPanel panelFormLayout;	
	
	/** the panel for the linked cell editor. **/
	private UIPanel panelLinkedCellEditor;
	
    /** the panel for the save editor. **/
    private UIPanel panelSaveEditors;

    /** If popup menu is already created. **/
	private boolean create = false;
	
	/** Changes Color when mouse entered. **/
	private UILabel labelChangeColor = new UILabel("Label: Background red. Change to blue when button clicked");
	
	/** Popup Menu. **/
	private UIPopupMenu popupMenu = new UIPopupMenu();
	
    /** UI Image for "empty" check. **/
    private UIImage imgCheck;

    /** UI Image for check no. **/
	private UIImage imgCheckNo;
	
	/** UI Image for check yes. **/
	private UIImage imgCheckYes;
	
	/** The center panel. **/
	private UIPanel panelCenter;
	
	/** the toolbar. */
	private UIToolBar toolBar;
	
	/** the charts button. */
	private UIButton butCharts;
	/** the map button. */
	private UIButton butMap;
    /** the font awesome button. */
    private UIButton butFontAwesome;
	/** the tree button. */
	private UIButton butTree;
	/** the table button. */
	private UIButton butTable;
	/** the components button. */
	private UIButton butComponents;
	/** the form layout button. */
	private UIButton butFormLayout;
	/** the linked cell editor button. */
	private UIButton butLinkedCellEditor;
	/** the thread push button. */
	private UIToggleButton butThread;
	/** the callback push button. */
	private UIButton butCallBack;
    /** the download window button. */
    private UIButton butDownloadWindow;
    /** the save editors button. */
    private UIButton butSaveEditors;
    /** the hugo button. */
    private UIButton butHugo;

	/** the application connection. */
	private MasterConnection macon;

	/** the current desktop. */
	private UIDesktopPanel desktop;
	
	/** the current internal frame. */
	private UIInternalFrame frame;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Showcase</code>.
	 * 
	 * @param pLauncher the launcher
	 * @throws Throwable if the initialization failed
	 */
	public SimpleApplication(UILauncher pLauncher) throws Throwable
	{
		super(pLauncher);
		
		setName("Simple application");
		
		Properties propLang = new Properties();
		propLang.loadFromXML(ResourceUtil.getResourceAsStream("/com/sibvisions/apps/simpleapp/translation_de.xml"));
		
		TranslationMap tmap = new TranslationMap();
		tmap.setLanguage("de");
		tmap.setAsProperties(propLang);
		
		pLauncher.setTranslation(tmap);

//		UIBorderLayout blLayout = new UIBorderLayout();
//		
//		setLayout(blLayout);
//		
//		UILabel lblTop = new UILabel("TOP");
//		lblTop.setBackground(UIColor.red);
//
//        UITextField lblCenter = new UITextField("CENTER");
//        lblTop.setBackground(UIColor.red);
//
//        UILabel lblBottom = new UILabel("BOTTOM");
//        lblBottom.setBackground(UIColor.orange);
//		
//		
//		add(lblTop, UIBorderLayout.NORTH);
//        add(lblCenter, UIBorderLayout.CENTER);
//        add(lblBottom, UIBorderLayout.SOUTH);
		
		initDataBooks();
		initUI();
	}

	/**
	 * Initializes the application.
	 * 
	 * @throws Throwable if the initialization failed
	 */
	private void initUI() throws Throwable
	{
		imgCheckNo = UIImage.getImage(UIImage.CHECK_NO_LARGE);
		imgCheckYes = UIImage.getImage(UIImage.CHECK_YES_LARGE);
		imgCheck = UIImage.getImage(UIImage.CHECK_LARGE);
		
		setLayout(new UIBorderLayout());
		
		UIBorderLayout borderLayout = new UIBorderLayout();
		panelMain = new UIPanel(borderLayout);	
		borderLayout.setMargins(10, 10, 10, 10);
		
		getLauncher().setMenuBar(createMenu());
		getLauncher().setToolBarArea(IToolBarPanel.AREA_LEFT);

		getLauncher().addToolBar(createToolBar());
		
		add(panelMain);
		
		panelMain.setBackgroundImage(UIImage.getImage("/com/sibvisions/apps/simpleapp/images/img1.jpg"));
		
		setPreferredSize(1024, 768);
	}
	
	/**
	 * Initializes all data books.
	 * 
	 * @throws ModelException 
	 */
	private void initDataBooks() throws ModelException
	{
		initStatisticDataBook();
		initSelfJoinedTreeDataBooks();
		initTableDataBooks();
		initLinkedCellEditorBooks();
	}
	
	/**
	 * Sets the data for the tree data books.
	 * 
	 * @throws ModelException 
	 */
	private void initSelfJoinedTreeDataBooks() throws ModelException
	{
		memContinent.setName("continent");
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memContinent.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT", new StringDataType()));
		memContinent.open();
		
		memCountry.setName("country");
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY", new StringDataType()));
		memCountry.getRowDefinition().addColumnDefinition(new ColumnDefinition("CONTINENT_ID", new BigDecimalDataType()));
		memCountry.setMasterReference(new ReferenceDefinition(new String[] {"CONTINENT_ID"}, memContinent, new String[] {"ID"}));
		memCountry.open();
		
		memCity.setName("city");
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY", new StringDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("CITY_ID", new BigDecimalDataType()));
		memCity.getRowDefinition().addColumnDefinition(new ColumnDefinition("COUNTRY_ID", new BigDecimalDataType()));
		memCity.setRootReference(new ReferenceDefinition(new String[] {"COUNTRY_ID"}, memCountry, new String[] {"ID"}));
		memCity.setMasterReference(new ReferenceDefinition(new String[] {"COUNTRY_ID", "CITY_ID"}, memCity, new String[] {"CITY_ID", "ID"}));
		memCity.open();		

		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(1), "Europe"});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(1), "Austria", BigDecimal.valueOf(1)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(2), "Germany", BigDecimal.valueOf(1)});
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(1), "City " + 1});
	
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(2), "City " + 2});
		
		memCity.setTreePath(new TreePath(1));
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(3), "SubCity " + 3});
		
		memCity.insert(false);
		memCity.setValues(new String[] {"ID", "CITY"}, new Object[] {BigDecimal.valueOf(4), "SubCity " + 4});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(3), "England", BigDecimal.valueOf(1)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(4), "Italy", BigDecimal.valueOf(1)});

		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(2), "Asia"});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(5), "China", BigDecimal.valueOf(2)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(6), "Japan", BigDecimal.valueOf(2)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(7), "Russia", BigDecimal.valueOf(2)});
		
		
		memContinent.insert(false);
		memContinent.setValues(new String[] {"ID", "CONTINENT"}, new Object[] {BigDecimal.valueOf(3), "America"});

		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(8), "USA", BigDecimal.valueOf(3)});
		
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(9), "Brasilien", BigDecimal.valueOf(3)});
		memCountry.insert(false);
		memCountry.setValues(new String[] {"ID", "COUNTRY", "CONTINENT_ID"}, new Object[] {BigDecimal.valueOf(10), "Chile", BigDecimal.valueOf(3)});
	}
		
	/**
	 * Initializes the data books for the tables.
	 * 
	 * @throws ModelException 
	 */
	private void initTableDataBooks() throws ModelException
	{
		memLocation.setName("location");
		memLocation.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memLocation.getRowDefinition().addColumnDefinition(new ColumnDefinition("ZIP", new BigDecimalDataType()));
		memLocation.getRowDefinition().addColumnDefinition(new ColumnDefinition("LOCATION", new StringDataType()));
		memLocation.open();
		
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(7083), "Purbach"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(2), BigDecimal.valueOf(1200), "Wien"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(3), BigDecimal.valueOf(1201), "St. Aegyd"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(4), BigDecimal.valueOf(1201), "Hohenberg"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(5), BigDecimal.valueOf(1202), "Traisen"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(6), BigDecimal.valueOf(1202), "Hohenberg 2"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(7), BigDecimal.valueOf(1203), "Wien 2"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(8), BigDecimal.valueOf(1203), "Wien 3"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(9), BigDecimal.valueOf(1203), "St. Aegyd am"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(10), BigDecimal.valueOf(1203), "St. Pölten"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(11), BigDecimal.valueOf(1204), "Wien 4"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(12), BigDecimal.valueOf(1205), "Kilb"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(13), BigDecimal.valueOf(1206), "Kilb 2"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(14), BigDecimal.valueOf(1207), "Lilienfeld"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(15), BigDecimal.valueOf(1208), "Lilienfeld 1"});
		memLocation.insert(false);
		memLocation.setValues(new String[] {"ID", "ZIP", "LOCATION"}, new Object[] {BigDecimal.valueOf(16), BigDecimal.valueOf(1208), "Wien 5"});
		
		memLocation.saveAllRows();
		
		memData.setName("memData");
        memData.setWritebackIsolationLevel(WriteBackIsolationLevel.DATASOURCE);
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("LOCATION_ID", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("LOCATION", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("NUMBER", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("STARTOFCONSTRUCTION", new TimestampDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("HOLE", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("REQUESTED", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("INFO", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("CHECKIT", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("HTMLCODE", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("MULTILINE", new StringDataType(new UITextCellEditor(UITextCellEditor.TEXT_PLAIN_WRAPPEDMULTILINE))));
		memData.open();
		
		memPersons.setName("persons");
		memPersons.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, memData, new String[] {"ID"}));
		memPersons.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memPersons.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		memPersons.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRSTNAME", new StringDataType()));
		memPersons.getRowDefinition().addColumnDefinition(new ColumnDefinition("SURNAME", new StringDataType()));
		memPersons.open();
		
		memData.insert(false);
		memData.setValues(new String[] {"ID", "LOCATION_ID", "LOCATION", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "REQUESTED", "CHECKIT"}, 
				          new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "Purbach", "René", BigDecimal.valueOf(25), 
				                        DateUtil.getTimestamp(2010, 01, 01, 10, 0, 0), "J", "Y"});

		memPersons.insert(false);
		memPersons.setValues(new String[] {"ID", "MASTER_ID", "FIRSTNAME", "SURNAME"}, new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "René", "Jahn"});
		memPersons.insert(false);
		memPersons.setValues(new String[] {"ID", "MASTER_ID", "FIRSTNAME"}, new Object[] {BigDecimal.valueOf(2), BigDecimal.valueOf(1), "Daniel"});
		memPersons.saveAllRows();

		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "CHECKIT"}, 
				         new Object[] {BigDecimal.valueOf(2), "Martin", BigDecimal.valueOf(12), DateUtil.getTimestamp(2009, 05, 01, 14, 0, 0), "Y"});

		memPersons.insert(false);
		memPersons.setValues(new String[] {"ID", "MASTER_ID", "FIRSTNAME", "SURNAME"}, new Object[] {BigDecimal.valueOf(3), BigDecimal.valueOf(2), "Martin", "Handsteiner"});
		memPersons.saveAllRows();
		
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "REQUESTED"}, 
				          new Object[] {BigDecimal.valueOf(3), "Roland", BigDecimal.valueOf(23), DateUtil.getTimestamp(2007, 03, 05, 20, 0, 0), "J"});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "REQUESTED"}, 
				          new Object[] {BigDecimal.valueOf(4), "René", BigDecimal.valueOf(25), DateUtil.getTimestamp(2010, 01, 01, 10, 0, 0), "J"});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION"}, 
				         new Object[] {BigDecimal.valueOf(5), "Martin", BigDecimal.valueOf(12), DateUtil.getTimestamp(2009, 05, 01, 14, 0, 0)});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "REQUESTED", "INFO"}, 
				          new Object[] {BigDecimal.valueOf(6), "Roland", BigDecimal.valueOf(23), DateUtil.getTimestamp(2007, 03, 05, 20, 0, 0), 
				                        "J", "/com/sibvisions/apps/simpleapp/images/img1.jpg"});

		for (int i = 7; i < 10000; i++)
		{
			memData.insert(false);
			memData.setValues(new String[] {"ID", "NAME", "NUMBER", "STARTOFCONSTRUCTION", "REQUESTED", "INFO"}, 
			                  new Object[] {BigDecimal.valueOf(i), "Name: " + i, BigDecimal.valueOf(i), null, "N", null});
		}
		
		memData.saveAllRows();
		memData.setSelectedRow(0);
	}
	
	/**
	 * Sets the data for the statistic mem data book.
	 * 
	 * @throws ModelException 
	 */
	private void initStatisticDataBook() throws ModelException
	{
		memStatistic.setName("statistic");
		memStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("NUMBER_OF_ADULTS", new BigDecimalDataType()));
		memStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("NUMBER_OF_CHILDREN", new BigDecimalDataType()));
		memStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("START_DATE", new TimestampDataType()));
		memStatistic.open();
		
		memSubStatistic.setName("substatistic");
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("STATISTIC_ID", new BigDecimalDataType()));
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("POPULATION", new BigDecimalDataType()));
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("FEMALE", new BigDecimalDataType()));
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("MALE", new BigDecimalDataType()));
		memSubStatistic.getRowDefinition().addColumnDefinition(new ColumnDefinition("MONTH", new BigDecimalDataType()));
		memSubStatistic.setMasterReference(new ReferenceDefinition(new String[] {"STATISTIC_ID"}, memStatistic, new String[] {"ID"}));
		memSubStatistic.open();

		memStatistic.insert(false);
		memStatistic.setValues(new String[] {"ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE"}, 
				               new Object[] {BigDecimal.valueOf(1), "1342", "587", new GregorianCalendar(2001, 1, 1).getTime()});
		
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "846", "400", "446", "1"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(2), BigDecimal.valueOf(1), "946", "500", "446", "2"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(3), BigDecimal.valueOf(1), "1006", "500", "506", "3"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(4), BigDecimal.valueOf(1), "1146", "600", "546", "4"});	
		
		memStatistic.insert(false);
		memStatistic.setValues(new String[] {"ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE"}, 
				               new Object[] {BigDecimal.valueOf(2), "1442", "622", new GregorianCalendar(2002, 1, 1).getTime()});
		
		
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(5), BigDecimal.valueOf(2), "1946", "1000", "946", "1"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(6), BigDecimal.valueOf(2), "4046", "2000", "2046", "2"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(7), BigDecimal.valueOf(2), "5001", "3000", "2001", "3"});
		memSubStatistic.insert(false);
		memSubStatistic.setValues(new String[] {"ID", "STATISTIC_ID", "POPULATION", "FEMALE", "MALE", "MONTH"}, 
				                  new Object[] {BigDecimal.valueOf(8), BigDecimal.valueOf(2), "2202", "1100", "1102", "4"});
		
		memStatistic.insert(false);
		memStatistic.setValues(new String[] {"ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE"}, 
				               new Object[] {BigDecimal.valueOf(3), "1586", "601", new GregorianCalendar(2003, 1, 1).getTime()});
		memStatistic.insert(false);
		memStatistic.setValues(new String[] {"ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE"}, 
				               new Object[] {BigDecimal.valueOf(4), "1645", "487", new GregorianCalendar(2004, 1, 1).getTime()});
	
//      Random rand = new Random();
// 		
//		for (int i = 5; i < 900; i++)
//		{
//			memStatistic.insert(false);
//			memStatistic.setValues(new String[] {"ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE"}, 
//                                 new Object[] {BigDecimal.valueOf(i), "" + rand.nextInt(1000), 
//                                               "" + rand.nextInt(1000), new GregorianCalendar(2000 + i, 1, 1).getTime()});			
//		}
		
		memStatistic.setSelectedRow(0);
	}
	
	/**
	 * Sets the data for the linked cell editor mem data book.
	 * 
	 * @throws ModelException 
	 */
	private void initLinkedCellEditorBooks() throws ModelException
	{
		memLinkedBase.setName("base");
		memLinkedBase.getRowDefinition().addColumnDefinition(new ColumnDefinition("VALUE"));
		memLinkedBase.getRowDefinition().addColumnDefinition(new ColumnDefinition("OTHER"));
		memLinkedBase.open();
		
		memLinkedReferred.setName("referred");
		memLinkedReferred.getRowDefinition().addColumnDefinition(new ColumnDefinition("VALUEB"));
		memLinkedReferred.getRowDefinition().addColumnDefinition(new ColumnDefinition("DISPLAY"));
		memLinkedReferred.open();
		
		// Insert the data.
		
		memLinkedBase.deleteAllRows();
		memLinkedBase.insert(false);
		memLinkedBase.setValues(new String[] {"VALUE", "OTHER"}, new Object[] {"C", "Other"});
		memLinkedBase.saveAllRows();
		
		memLinkedBase.setSelectedRow(0);
		
		memLinkedReferred.deleteAllRows();
		memLinkedReferred.insert(false);
		memLinkedReferred.setValues(new String[] {"VALUEB", "DISPLAY"}, new Object[] {"A", "Avalon"});
		memLinkedReferred.insert(false);
		memLinkedReferred.setValues(new String[] {"VALUEB", "DISPLAY"}, new Object[] {"B", "Babylon"});
		memLinkedReferred.insert(false);
		memLinkedReferred.setValues(new String[] {"VALUEB", "DISPLAY"}, new Object[] {"C", "Chile"});
		memLinkedReferred.insert(false);
		memLinkedReferred.setValues(new String[] {"VALUEB", "DISPLAY"}, new Object[] {"D", "Darmstadt"});
		memLinkedReferred.saveAllRows();
		
		// Create the default cell editor
		ReferenceDefinition refDef = new ReferenceDefinition();
		refDef.setColumnNames(new String[] {"VALUE"});
		refDef.setReferencedDataBook(memLinkedReferred);
		refDef.setReferencedColumnNames(new String[] {"VALUEB"});
		
		ILinkedCellEditor editor = new UILinkedCellEditor(refDef);
		
		editor.setDisplayReferencedColumnName("DISPLAY");
		memLinkedBase.getRowDefinition().getColumnDefinition("VALUE").getDataType().setCellEditor(editor);
	}
	
	/**
	 * Creates a ToolBar.
	 * 
	 * @return UIToolBar
	 * 
	 * @throws ModelException 
	 */
	public UIToolBar createToolBar() throws ModelException
	{
		toolBar = new UIToolBar();
		
		butFontAwesome = createToolBarButton("FontAwesome", false);
		butFontAwesome.setMouseOverImage(UIImage.getImage(IFontAwesome.CHECK_SQUARE_O_SMALL));
		butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
		
		butCharts = createToolBarButton("Charts", false);
		butTree = createToolBarButton("Tree", false);
		butMap = createToolBarButton("Map", false);
		butTable = createToolBarButton("Table", false);
		butComponents = createToolBarButton("Components", false);
		butFormLayout = createToolBarButton("Form Layout", false);
		butLinkedCellEditor = createToolBarButton("Linked Cell Editor", false);
		
		butThread = createToolBarButton("Thread push", true);
		butThread.setBorderPainted(true);
		butThread.setBorderOnMouseEntered(false);

		butCallBack = createToolBarButton("Call Back push", false);

		butDownloadWindow = createToolBarButton("Download...", false);
		butDownloadWindow.eventAction().addListener(this, "doDownloadReport");
		
        butSaveEditors = createToolBarButton("Save editors", false);

        butHugo = createToolBarButton("The definitive best Hugo...", false);
        butHugo.eventAction().addListener(new IActionListener()
        {
            public void action(UIActionEvent pActionEvent)
            {
                butFontAwesome.setImage(UIImage.getImage(IFontAwesome.CHECK_SQUARE_O_SMALL));
                butCharts.setImage(imgCheckNo);
                butTree.setImage(imgCheckNo);
                butMap.setImage(imgCheckNo);
                butTable.setImage(imgCheckNo);
                butComponents.setImage(imgCheckNo);
                butFormLayout.setImage(imgCheckNo);
                butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
                butHugo.setImage(imgCheckYes);

                if (panelCenter != null)
                {
                    panelMain.remove(panelCenter);
                }
                
                panelCenter = new UIPanel();
                panelCenter.setLayout(null);
                UIButton b1 = new UIButton("Test1");
                b1.setBackground(UIColor.blue);
                b1.setBounds(20, 20, 30, 30);
//                b1.setPreferredSize(30, 30);
                UIButton b2 = new UIButton("Test2");
                b2.setBackground(UIColor.red);
                b2.setBounds(60, 60, 40, 40);
//                b2.setPreferredSize(40, 40);
                UITable b3 = new UITable(memData);
                b3.setBackground(UIColor.green);
                b3.setBounds(120, 120, 150, 150);
//                b3.setPreferredSize(50, 50);
                panelCenter.add(b1);
                panelCenter.add(b2);
                panelCenter.add(b3);
                
                panelMain.add(panelCenter, UIBorderLayout.CENTER);  
            }
        });

        butFontAwesome.eventAction().addListener(new IActionListener()
        {
            public void action(UIActionEvent pActionEvent)
            {
                butFontAwesome.setImage(UIImage.getImage(IFontAwesome.CHECK_SQUARE_O_SMALL));
                butCharts.setImage(imgCheckNo);
                butTree.setImage(imgCheckNo);
                butMap.setImage(imgCheckNo);
                butTable.setImage(imgCheckNo);
                butComponents.setImage(imgCheckNo);
                butFormLayout.setImage(imgCheckNo);
                butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
                
                if (panelFontAwesome == null)
                {
                    createFontAwesomePanel();
                }
                
                if (panelCenter != null)
                {
                    panelMain.remove(panelCenter);
                }
                
                panelCenter = panelFontAwesome;
                
                panelMain.add(panelCenter, UIBorderLayout.CENTER);  
            }
        });
        
		butCharts.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckYes);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelCharts == null)
				{
					createChartsPanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelCharts;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});
		
		butTree.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckYes);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelTree == null)
				{
					createTreePanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelTree;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});		
		
		butMap.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckYes);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelMap == null)
				{
					createMapPanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelMap;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});			
		
		butTable.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckYes);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelTable == null)
				{
					createTablePanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
					
				panelCenter = panelTable;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});
		
		butComponents.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckYes);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelComponents == null)
				{
					createComponentsPanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelComponents;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});
		
		butFormLayout.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckYes);
				butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelFormLayout == null)
				{
					createFormLayoutMainPanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelFormLayout;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});
		
		butLinkedCellEditor.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
			    butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
				butCharts.setImage(imgCheckNo);
				butTree.setImage(imgCheckNo);
				butMap.setImage(imgCheckNo);
				butTable.setImage(imgCheckNo);
				butComponents.setImage(imgCheckNo);
				butFormLayout.setImage(imgCheckNo);
				butLinkedCellEditor.setImage(imgCheckYes);
                butSaveEditors.setImage(imgCheckNo);
				
				if (panelLinkedCellEditor == null)
				{
					createLinkedCellEditorMainPanel();
				}
				
				if (panelCenter != null)
				{
					panelMain.remove(panelCenter);
				}
				
				panelCenter = panelLinkedCellEditor;
				
				panelMain.add(panelCenter, UIBorderLayout.CENTER);	
			}
		});		
		
		butThread.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				UIComponent.invokeInThread(new Runnable()
				{
					public void run()
					{
						try
						{
							while (butThread.isSelected())
							{
								Thread.sleep(2000);
							
								System.out.println("SLEEP is over");

								UIComponent.invokeLater(new Runnable()
								{
									public void run()
									{
										butThread.setImage(UIImage.getImage(UIImage.SEARCH_LARGE));
									}
								});
								
								Thread.sleep(1000); 
			
								UIComponent.invokeLater(new Runnable()
								{
									public void run()
									{
										butThread.setImage(imgCheckNo);
									}
								});
							}
						}
						catch (InterruptedException ie)
						{
							error(ie);
						}
					}
				});
			}
		});		

		butCallBack.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				butCallBack.setImage(imgCheckNo);
				
				try
				{
					if (macon == null)
					{
						DirectServerConnection dscon = new DirectServerConnection();

						macon = new MasterConnection(dscon);
						macon.setApplicationName("simpleapp");
						macon.setUserName("admin");
						macon.setPassword("admin");
						macon.open();
					}

					macon.call(new ICallBackListener()
					{
						@Override
						public void callBack(CallBackEvent pEvent)
						{
							try
							{
								pEvent.getObject();
							}
							catch (Throwable th)
							{
								th.printStackTrace();
							}
							
							butCallBack.setImage(imgCheckYes);

							//tests if factory exists
							new UIButton();
							
							System.out.println("Callback done!");
						}
					}, 
					null, 
					"asyncServerAction");
				}
				catch (Throwable th)
				{
					th.printStackTrace();
					
					ExceptionHandler.raise(th);
				}
			}
		});		
		
        butSaveEditors.eventAction().addListener(new IActionListener()
        {
            public void action(UIActionEvent pActionEvent)
            {
                butFontAwesome.setImage(UIImage.getImage(IFontAwesome.SQUARE_O_SMALL));
                butCharts.setImage(imgCheckNo);
                butTree.setImage(imgCheckNo);
                butMap.setImage(imgCheckNo);
                butTable.setImage(imgCheckNo);
                butComponents.setImage(imgCheckNo);
                butFormLayout.setImage(imgCheckNo);
                butLinkedCellEditor.setImage(imgCheckNo);
                butSaveEditors.setImage(imgCheckYes);
                
                if (panelComponents == null)
                {
                    createSaveEditorsPanel();
                }
                
                if (panelCenter != null)
                {
                    panelMain.remove(panelCenter);
                }
                
                panelCenter = panelSaveEditors;
                
                panelMain.add(panelCenter, UIBorderLayout.CENTER);  
            }
        });
		
		toolBar.add(butCharts);
		toolBar.add(butTree);
		toolBar.add(butMap);
		toolBar.add(butTable);
		toolBar.add(butComponents);
		toolBar.add(butFormLayout);
		toolBar.add(butLinkedCellEditor);
		toolBar.add(butThread);
		toolBar.add(butCallBack);
		toolBar.add(butDownloadWindow);
		toolBar.add(butSaveEditors);
		toolBar.add(butHugo);
        toolBar.add(butFontAwesome);
	
		return toolBar;
	}
	
	/**
	 * Creates a button for the toolbar.
	 * 
	 * @param pText the text for the button
	 * @param pToggle whether the button should be a toggle button
	 * @return the toolbar button.
	 * @param <B> the button class
	 */
	private <B extends IButton> B createToolBarButton(String pText, boolean pToggle)
	{
		IButton bToolBarButton;
		
		if (!pToggle)
		{
			bToolBarButton = new UIButton();
			bToolBarButton.setImage(imgCheckNo);
			bToolBarButton.setMouseOverImage(imgCheckYes);
		}
		else
		{
			bToolBarButton = new UIToggleButton();
			bToolBarButton.setImage(imgCheckYes);
		}
		
		bToolBarButton.setText(pText);
		bToolBarButton.setSize(new UIDimension(120, 40));
		bToolBarButton.setHorizontalAlignment(IAlignmentConstants.ALIGN_LEFT);
		bToolBarButton.setBorderPainted(false);
		bToolBarButton.setBorderOnMouseEntered(true);
		
		return (B)bToolBarButton;
	}	
	
	/**
	 * Creates the font awesome panel.
	 */
	private void createFontAwesomePanel()
	{
	    UIFormLayout layout = new UIFormLayout();
	    layout.setNewlineCount(5);
	    
	    panelFontAwesome = new UIPanel();
	    panelFontAwesome.setLayout(layout);

        final UIIcon icon = new UIIcon(IFontAwesome.SEARCH_LARGE + ";size=60");
        UIIcon iconAdd = new UIIcon(UIImage.getImage(UIImage.ADD_LARGE));
	    
	    UIButton but = new UIButton("Styled icon");
	    but.setImage(UIImage.getImage(IFontAwesome.ADJUST_SMALL + ";color=orange;size=20px;"));
	    but.setMouseOverImage(UIImage.getImage(IFontAwesome.SAVE_SMALL + ";color=blue;size=20px;"));
	    but.eventAction().addListener(new IActionListener()
        {
            @Override
            public void action(UIActionEvent pActionEvent) throws Throwable
            {
                icon.setImage(UIImage.getImage(IFontAwesome.SEARCH_LARGE + ";size=100"));
            }
        });

	    //should work with Reindeer theme
	    Button bubu = new Button("Welcome Vaadin");
	    bubu.setIcon(VaadinIcons.ADD_DOCK);
	    
	    panelFontAwesome.add(but);
	    panelFontAwesome.add(icon);
	    panelFontAwesome.add(iconAdd);
	    panelFontAwesome.add(new UICustomComponent(bubu));
	}
	
	/**
	 * Creates the panel to show the function of the charts.
	 */
	private void createChartsPanel()
	{
		panelCharts = new UIPanel();
		UIFlowLayout layout = new UIFlowLayout();
		layout.setComponentAlignment(IAlignmentConstants.ALIGN_CENTER);
		layout.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
		panelCharts.setLayout(layout);
		
		UIPanel panelDetail = new UIPanel();
		UIFormLayout flDetail = new UIFormLayout();
		panelDetail.setLayout(flDetail);
		
		try
		{
		
			((BigDecimalDataType) memStatistic.getRowDefinition().getColumnDefinition("NUMBER_OF_ADULTS").getDataType()).setPrecision(10);
			((BigDecimalDataType) memStatistic.getRowDefinition().getColumnDefinition("NUMBER_OF_CHILDREN").getDataType()).setPrecision(10);
			((BigDecimalDataType) memStatistic.getRowDefinition().getColumnDefinition("NUMBER_OF_ADULTS").getDataType()).setScale(1);
			((BigDecimalDataType) memStatistic.getRowDefinition().getColumnDefinition("NUMBER_OF_CHILDREN").getDataType()).setScale(1);
			
			UIDateCellEditor dateEdit = new UIDateCellEditor("dd.MM.yyyy");
			dateEdit.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
					
			((TimestampDataType) memStatistic.getRowDefinition().getColumnDefinition("START_DATE").getDataType()).setCellEditor(dateEdit);
			((TimestampDataType) memStatistic.getRowDefinition().getColumnDefinition("START_DATE").getDataType()).setDateFormat("yyyy");
			
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("POPULATION").getDataType()).setPrecision(10);
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("POPULATION").getDataType()).setScale(1);
			
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("FEMALE").getDataType()).setPrecision(10);
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("FEMALE").getDataType()).setScale(1);
			
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("MALE").getDataType()).setPrecision(10);
			((BigDecimalDataType) memSubStatistic.getRowDefinition().getColumnDefinition("MALE").getDataType()).setScale(1);
		
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		
		// "ID", "NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN", "START_DATE", "END_DATE"
		
		UITable table = new UITable();
		table.setDataBook(memStatistic);
		table.setSize(600, 250);
		
		UITable subTable = new UITable();
		subTable.setDataBook(memSubStatistic);
		subTable.setSize(600, 250);
		
		UIChart lineChart = new UIChart();
		lineChart.setChartStyle(UIChart.STYLE_LINES);
		lineChart.setTitle("Line Chart");
		lineChart.setXAxisTitle("Year");
		lineChart.setYAxisTitle("Count");
		lineChart.setDataBook(memStatistic);
		lineChart.setXColumnName("START_DATE");
		lineChart.setYColumnNames(new String[] {"NUMBER_OF_ADULTS", "NUMBER_OF_CHILDREN"});
		lineChart.setSize(600, 250);
		
		UIChart barChart = new UIChart();
		barChart.setChartStyle(UIChart.STYLE_BARS);
		barChart.setTitle("Bar Chart");
		barChart.setXAxisTitle("Month");
		barChart.setYAxisTitle("Count");
		barChart.setDataBook(memSubStatistic);
		barChart.setXColumnName("MONTH");
		barChart.setYColumnNames(new String[] {"POPULATION", "FEMALE", "MALE"});
		barChart.setSize(600, 250);
		
		UIChart areaChart = new UIChart();
		areaChart.setChartStyle(UIChart.STYLE_AREA);
		areaChart.setTitle("Area Chart");
		areaChart.setXAxisTitle("Month");
		areaChart.setYAxisTitle("Count");
		areaChart.setDataBook(memSubStatistic);
		areaChart.setXColumnName("MONTH");
		areaChart.setYColumnNames(new String[] {"POPULATION", "FEMALE", "MALE"});
		areaChart.setSize(600, 250);
		

		
		panelDetail.add(table, flDetail.getConstraints(0, 0));
		panelDetail.add(lineChart, flDetail.getConstraints(1, 0));
		panelDetail.add(subTable, flDetail.getConstraints(0, 1, 0, 2));
		panelDetail.add(barChart, flDetail.getConstraints(1, 1, 1, 1));
		panelDetail.add(areaChart, flDetail.getConstraints(1, 2, 1, 2));
		
		panelCharts.add(panelDetail);
	}
	
	/**
	 * Creates the panel to show the function of the tree.
	 */
	private void createTreePanel()
	{
		panelTree = new UIPanel();
		UIFlowLayout layout = new UIFlowLayout();
		layout.setComponentAlignment(IAlignmentConstants.ALIGN_CENTER);
		layout.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
		panelTree.setLayout(layout);
		
		UIPanel panelDetail = new UIPanel();
		UIFormLayout flDetail = new UIFormLayout();
		panelDetail.setLayout(flDetail);
		
		try
		{
			memContinent.setSelectedRow(0);
			memCountry.setSelectedRow(1);
			memCity.setTreePath(new TreePath(0));
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		UITree tree = new UITree();
		tree.setDataBooks(memContinent, memCountry, memCity);
		tree.setEditable(true);
		tree.setSize(400, 100);
		
		tree.eventMousePressed().addListener(this, "doPopup");
		tree.eventMouseReleased().addListener(this, "doPopup");
		
		UITable tableContinent = new UITable();
		tableContinent.setDataBook(memContinent);
		tableContinent.setSize(600, 250);
		
		UITable tableCountry = new UITable();
		tableCountry.setDataBook(memCountry);
		tableCountry.setSize(600, 250);		
		
		UITable tableCity = new UITable();
		tableCity.setDataBook(memCity);
		tableCity.setSize(600, 250);
		
		panelDetail.add(tableContinent, flDetail.getConstraints(1, 0));
		panelDetail.add(tableCountry, flDetail.getConstraints(1, 1));
		panelDetail.add(tableCity, flDetail.getConstraints(1, 2));
		panelDetail.add(tree, flDetail.getConstraints(0, 0, 0, 2));
				
		panelTree.add(panelDetail);
	}
	
	/**
	 * Creates the map panel.
	 */
	private void createMapPanel()
	{
        try
        {
            MemDataBook groupsDataBook = new MemDataBook();
            groupsDataBook.setName("groups");
            groupsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_GROUP, new StringDataType()));
            groupsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_LATITUDE, new BigDecimalDataType(20, 17)));
            groupsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_LONGITUDE, new BigDecimalDataType(20, 17)));
            groupsDataBook.open();
            
            groupsDataBook.insert(false);
            groupsDataBook.setValues(new String[] {IMap.COLUMNNAME_GROUP, IMap.COLUMNNAME_LATITUDE, IMap.COLUMNNAME_LONGITUDE}, 
            		                 new Object[] {"TEST", BigDecimal.valueOf(48.2d), BigDecimal.valueOf(17.4d)});
            groupsDataBook.insert(false);
            groupsDataBook.setValues(new String[] {IMap.COLUMNNAME_GROUP, IMap.COLUMNNAME_LATITUDE, IMap.COLUMNNAME_LONGITUDE}, 
            		                 new Object[] {"TEST", BigDecimal.valueOf(48.2d), BigDecimal.valueOf(17.0d)});
            groupsDataBook.insert(false);
            groupsDataBook.setValues(new String[] {IMap.COLUMNNAME_GROUP, IMap.COLUMNNAME_LATITUDE, IMap.COLUMNNAME_LONGITUDE}, 
            		                 new Object[] {"TEST", BigDecimal.valueOf(49d), BigDecimal.valueOf(16.4d)});
            groupsDataBook.saveAllRows();

            MemDataBook pointsDataBook = new MemDataBook();
            pointsDataBook.setName("points");
            pointsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_LATITUDE, new BigDecimalDataType(20, 17)));
            pointsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_LONGITUDE, new BigDecimalDataType(20, 17)));
            pointsDataBook.getRowDefinition().addColumnDefinition(new ColumnDefinition(IMap.COLUMNNAME_MARKERIMAGE, new StringDataType()));
            pointsDataBook.open();
            
            pointsDataBook.insert(false);
            pointsDataBook.setValues(new String[] {IMap.COLUMNNAME_LATITUDE, IMap.COLUMNNAME_LONGITUDE, IMap.COLUMNNAME_MARKERIMAGE}, 
	                                 new Object[] {BigDecimal.valueOf(48.2d), BigDecimal.valueOf(17.4d), "/javax/rad/genui/images/16x16/add.png"});
	        pointsDataBook.insert(false);
	        pointsDataBook.setValues(new String[] {IMap.COLUMNNAME_LATITUDE, IMap.COLUMNNAME_LONGITUDE}, 
	                                 new Object[] {BigDecimal.valueOf(48.3d), BigDecimal.valueOf(17.5d)});
	        pointsDataBook.saveAllRows();

	        pointsDataBook.eventValuesChanged().addListener(new IDataRowListener() 
	        {
				@Override
				public void valuesChanged(DataRowEvent pDataRowEvent) throws Throwable 
				{
					System.out.println("Changed: " + StringUtil.toString(pDataRowEvent.getChangedDataRow().getValuesAsString(null)));
					
				}
			});
	        
	        UIMap map = new UIMap();
	        map.setGroupsDataBook(groupsDataBook);
	        map.setPointsDataBook(pointsDataBook);
	        map.setPointSelectionEnabled(true);
	        map.setPointSelectionLockedOnCenter(true);
	        
			panelMap = new UIPanel(new UIBorderLayout());
			panelMap.add(map, UIBorderLayout.CENTER);
        }
        catch (ModelException me)
        {
        	me.printStackTrace();
        }
	}
	
	/**
	 * Creates the panel to show the function of the tree.
	 */
	private void createTablePanel()
	{
		panelTable = new UIPanel();
		UIFlowLayout layout = new UIFlowLayout();
		layout.setComponentAlignment(IAlignmentConstants.ALIGN_CENTER);
		layout.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
		panelTable.setLayout(layout);

		UIPanel panelDetail = new UIPanel();
		UIFormLayout flDetail = new UIFormLayout();
		panelDetail.setLayout(flDetail);
		
		try
		{
			UILinkedCellEditor linkLocation = new UILinkedCellEditor();
			linkLocation.setLinkReference(new ReferenceDefinition(new String[] {"LOCATION_ID", "LOCATION"}, memLocation, new String[] {"ID", "LOCATION"}));
			linkLocation.setHorizontalAlignment(IAlignmentConstants.ALIGN_RIGHT);

			UIChoiceCellEditor choiceRequested = new UIChoiceCellEditor();
			choiceRequested.setAllowedValues(new String[] {"J", "N"});
			choiceRequested.setImageNames(new String [] {IFontAwesome.ALIGN_LEFT_SMALL + ";color=red;size=20", imgCheck.getImageName()});
			choiceRequested.setDefaultImageName(imgCheckNo.getImageName());
			choiceRequested.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
			
			UIImageViewer imgv = new UIImageViewer();
			
			UIEnumCellEditor enumHoleEditor = new UIEnumCellEditor(new String[] {"", "S", "M", "L"}, new String[] {"None", "Small", "Medium", "Large"});
			
			memData.getRowDefinition().getColumnDefinition("LOCATION").getDataType().setCellEditor(linkLocation);
			memData.getRowDefinition().getColumnDefinition("REQUESTED").getDataType().setCellEditor(choiceRequested);
			memData.getRowDefinition().getColumnDefinition("INFO").setReadOnly(true);
			memData.getRowDefinition().getColumnDefinition("INFO").getDataType().setCellEditor(imgv);
			memData.getRowDefinition().getColumnDefinition("LOCATION").setWidth(180);
			memData.getRowDefinition().getColumnDefinition("NAME").setWidth(180);
			memData.getRowDefinition().getColumnDefinition("STARTOFCONSTRUCTION").setWidth(150);
			memData.getRowDefinition().getColumnDefinition("HOLE").getDataType().setCellEditor(enumHoleEditor);
			memData.getRowDefinition().getColumnDefinition("LOCATION").setLabel("LOCATION LABEL");		
			memData.getRowDefinition().getColumnDefinition("CHECKIT").getDataType().setCellEditor(new UICheckBoxCellEditor("Y", "N"));
			memData.getRowDefinition().getColumnDefinition("HTMLCODE").getDataType().setCellEditor(new UITextCellEditor("text/html"));
			
			UIDateCellEditor dateEdit = new UIDateCellEditor("EEEE, dd.MM.yyyy");
			dateEdit.setHorizontalAlignment(UIDateCellEditor.ALIGN_CENTER);

			memData.getRowDefinition().getColumnDefinition("STARTOFCONSTRUCTION").getDataType().setCellEditor(dateEdit);
			

			UITable tblMaster = new UITable();
			tblMaster.setDataBook(memData);
			tblMaster.setAutoResize(false);
			tblMaster.setEditable(true);
			tblMaster.setShowVerticalLines(true);
			tblMaster.setShowHorizontalLines(false);
			tblMaster.setSize(1300, 300);
			
			tblMaster.eventMousePressed().addListener(this, "doPopup");
			tblMaster.eventMouseReleased().addListener(this, "doPopup");
			
			Style style = new Style("webinale", "full-cool", "outalimits");
			
			final UICellFormat format = new UICellFormat(UIColor.red, UIColor.blue, new UIFont("Courier", UIFont.BOLD, 14), null, style, 0);

			tblMaster.setCellFormatter(new ICellFormatter()
			{
				public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn)
				{
					if (pColumnName.equals("NAME"))
					{
						return format;
					}
	
					return null;
				}
			});
			
			UITable tblDetail = new UITable();
			tblDetail.setDataBook(memPersons);
			tblDetail.setPreferredSize(500, 200);

			UIEditor edtName = new UIEditor(memData, "NAME");
			edtName.setBackground(UIColor.orange);
			edtName.setToolTipText("<html>This is a HTML Styled Tooltip Text for the <br /></b>name field</b></html>");
			UILabel labelName = new UILabel("Name: ");
			
			UIEditor edtLocation = new UIEditor(memData, "LOCATION");
			edtLocation.setBackground(UIColor.yellow);
			UILabel labelLocation = new UILabel("Location: ");
			
			UIEditor edtNumber = new UIEditor(memData, "NUMBER");
			edtNumber.setBackground(UIColor.green);
			edtNumber.setForeground(UIColor.red);
			edtNumber.setCellEditor(new UINumberCellEditor(".00"));
			UILabel labelNumber = new UILabel("Number: ");
			
			UIEditor edtStartOfConstruction = new UIEditor(memData, "STARTOFCONSTRUCTION");
			UILabel labelStartOfConstruction = new UILabel("Start of construction: ");
			
			UIDateCellEditor cedTime = new UIDateCellEditor();
			cedTime.setDateFormat(((SimpleDateFormat)SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, LocaleUtil.getDefault())).toPattern());
			
            UIEditor edtStartOfConstructionTime = new UIEditor(memData, "STARTOFCONSTRUCTION");
            edtStartOfConstructionTime.setCellEditor(cedTime);
            edtStartOfConstructionTime.setBackground(UIColor.cyan);
            UILabel labelStartOfConstructionTime = new UILabel("Start of construction (time): ");

            UIEditor edtHole = new UIEditor(memData, "HOLE");
			UILabel labelHole = new UILabel("Size of hole: ");
			
            UIEditor edtCheckit = new UIEditor(memData, "CHECKIT");
			UILabel labelCheckit = new UILabel("Yes/No:");

            UIEditor edtHtml = new UIEditor(memData, "HTMLCODE");
			UILabel labelHtml = new UILabel("Html Code");

            UIEditor edtMulti = new UIEditor(memData, "MULTILINE");
			UILabel labelMulti = new UILabel("Multiline");

			final UIEditor edtRequested = new UIEditor(memData, "REQUESTED");
			UILabel labelRequested = new UILabel("Requested: ");
			
			UIGroupPanel gpanDetails = new UIGroupPanel();
			gpanDetails.setText("Details");
			
			UIFormLayout formLayout = new UIFormLayout();
			formLayout.setMargins(10, 10, 10, 10);
			
			gpanDetails.setLayout(formLayout);
			
			gpanDetails.add(labelName, formLayout.getConstraints(0, 0));
			gpanDetails.add(edtName, formLayout.getConstraints(1, 0));
			
			formLayout.setAnchorConfiguration("r1=150");
			
			gpanDetails.add(labelLocation, formLayout.getConstraints(0, 1));
			gpanDetails.add(edtLocation, formLayout.getConstraints(1, 1));

			gpanDetails.add(labelNumber, formLayout.getConstraints(0, 2));
			gpanDetails.add(edtNumber, formLayout.getConstraints(1, 2));
			
			gpanDetails.add(labelStartOfConstruction, formLayout.getConstraints(0, 3));
			gpanDetails.add(edtStartOfConstruction, formLayout.getConstraints(1, 3));
			
            gpanDetails.add(labelStartOfConstructionTime, formLayout.getConstraints(0, 4));
            gpanDetails.add(edtStartOfConstructionTime, formLayout.getConstraints(1, 4));

            gpanDetails.add(labelHole, formLayout.getConstraints(0, 5));
			gpanDetails.add(edtHole, formLayout.getConstraints(1, 5));
			
			gpanDetails.add(labelRequested, formLayout.getConstraints(0, 6));
			gpanDetails.add(edtRequested, formLayout.getConstraints(1, 6));
			
			gpanDetails.add(labelCheckit, formLayout.getConstraints(0, 7));
			gpanDetails.add(edtCheckit, formLayout.getConstraints(1, 7));

			gpanDetails.add(labelHtml, formLayout.getConstraints(0, 8));
			gpanDetails.add(edtHtml, formLayout.getConstraints(1, 8));

			gpanDetails.add(labelMulti, formLayout.getConstraints(0, 9));
			gpanDetails.add(edtMulti, formLayout.getConstraints(1, 9));

			final UIButton button = new UIButton("Hide Request");
			
			button.eventAction().addListener(new IActionListener()
			{
				
				public void action(UIActionEvent pActionEvent)
				{
					if (edtRequested.isVisible())
					{
						button.setText("Show Request");
						edtRequested.setVisible(false);
					}
					else
					{
						button.setText("Hide Request");
						edtRequested.setVisible(true);
					}
				}
			});
			
			gpanDetails.add(button, formLayout.getConstraints(0, 5));
			
//			gpanDetails.add(new UILabel("Info: "), formLayout.getConstraints(0, 5));
//			gpanDetails.add(new UIEditor(memData, "INFO"), formLayout.getConstraints(1, 5));

			panelDetail.add(tblMaster, flDetail.getConstraints(0, 0, 1, 0));
			panelDetail.add(gpanDetails, flDetail.getConstraints(0, 1, 0, 1));
			panelDetail.add(tblDetail, flDetail.getConstraints(1, 1, 1, 1));

		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		panelTable.add(panelDetail);
	}
	
	/**
	 * Creates the panel to show the function of the components like buttons, checkBoxes, text components, ....
	 */
	private void createComponentsPanel()
	{
		panelComponents = new UIPanel();
		UIBorderLayout layout = new UIBorderLayout();
		panelComponents.setLayout(layout);

		UITabsetPanel tabs = new UITabsetPanel();

		tabs.eventTabActivated().addListener(this, "doComponentTabActivated");
		tabs.eventTabDeactivated().addListener(this, "doComponentTabDeactivated");
		
		tabs.add(createFormLayoutPanel(), "FormLayout");
		tabs.add(createNoLayoutPanel(), "NoLayout");
		tabs.add(createFlowLayoutPanel(), "FlowLayout");
		tabs.add(createBorderLayoutPanel(), "BorderLayout");		
		tabs.add(createSplit(), "Split");
		tabs.add(createTextComponents(), "Text Component");
		tabs.add(createButtons(), "Buttons");
		tabs.add(createRadioButtons(), "Radio Buttons");
		tabs.add(createCheckBox(), "Check Box");
		
		panelComponents.add(tabs, UIBorderLayout.CENTER);		
	}
	
	/**
	 * Creates the save editors panel.
	 */
	private void createSaveEditorsPanel()
	{
	    try
	    {
    	    DataRow row = new DataRow();
    	    row.getRowDefinition().addColumnDefinition(new ColumnDefinition("FIRST", new BigDecimalDataType(2, 0)));
    	    row.getRowDefinition().addColumnDefinition(new ColumnDefinition("LAST", new BigDecimalDataType(2, 0)));
            row.getRowDefinition().addColumnDefinition(new ColumnDefinition("TEXT", new StringDataType(20)));
            row.getRowDefinition().addColumnDefinition(new ColumnDefinition("LONG", new BigDecimalDataType(10, 2)));
    	    
    	    final UIEditor editFirst = new UIEditor(row, "FIRST");
    	    final UIEditor editLast = new UIEditor(row, "LAST");
    	    final UIEditor editText = new UIEditor(row, "TEXT");
    	    final UIEditor editLong = new UIEditor(row, "LONG");
    	    
    	    UIButton butOnOff = new UIButton("OFF");
    	    butOnOff.setFocusable(false);
    	    butOnOff.eventAction().addListener(new IActionListener()
            {
                @Override
                public void action(UIActionEvent pActionEvent)
                {
                    editFirst.setEnabled(!editFirst.isEnabled());
                    editLast.setEnabled(!editLast.isEnabled());
                }
            });
    	    
    	    UIFormLayout folEditors = new UIFormLayout();
    	    
    	    UIPanel panEditors = new UIPanel(folEditors);
    	    
    	    panEditors.add(butOnOff, folEditors.getLeftAlignedConstraints(0, 0, 1, 0));
    	    panEditors.add(new UILabel("First (2,0)"), folEditors.getConstraints(0, 1));
    	    panEditors.add(editFirst, folEditors.getConstraints(1, 1));
            panEditors.add(new UILabel("Last (2,0)"), folEditors.getConstraints(0, 2));
    	    panEditors.add(editLast, folEditors.getConstraints(1, 2));
            panEditors.add(new UILabel("Text (20)"), folEditors.getConstraints(0, 3));
    	    panEditors.add(editText, folEditors.getConstraints(1, 3));
            panEditors.add(new UILabel("Long (10,2)"), folEditors.getConstraints(0, 4));
            panEditors.add(editLong, folEditors.getConstraints(1, 4));
    	    
    	    panelSaveEditors = panEditors;
	    }
	    catch (Exception e)
	    {
	        ExceptionHandler.raise(e);
	    }
	}
	
	/**
	 * Creates the panel to show the function of the form layout.
	 */
	private void createFormLayoutMainPanel()
	{
		panelFormLayout = new UIPanel();
		UIFormLayout layout = new UIFormLayout();
		panelFormLayout.setLayout(layout);
		
		UIGroupPanel gp1 = new UIGroupPanel("1. Case");
		UIFormLayout fl1 = new UIFormLayout();
		gp1.setLayout(fl1);
		gp1.add(new UIButton("1"), fl1.getConstraints(0, 0));
		gp1.add(new UIButton("2"), fl1.getConstraints(1, 0, -5, 0));
		gp1.add(new UIButton("3"), fl1.getConstraints(-4, 0, -4, 0));
		gp1.add(new UIButton("4"), fl1.getConstraints(-3, 0, -3, 0));
		gp1.add(new UIButton("5"), fl1.getConstraints(-2, 0, -2, 0));
	
		UIButton button6 = new UIButton("6");
		
		gp1.add(button6, fl1.getConstraints(-1, 0, -1, 0));
		gp1.add(new UIButton("7"), fl1.getConstraints(0, 1, 0, 1));
		gp1.add(new UIButton("8"), fl1.getConstraints(1, 1, 1, 1));
		gp1.add(new UIButton("9"), fl1.getConstraints(2, 1, 2, 1));
		gp1.add(new UIButton("10"), fl1.getConstraints(3, 1, 3, 1));
		gp1.add(new UIButton("11"), fl1.getConstraints(4, 1, 4, 1));
		gp1.add(new UIButton("12"), fl1.getConstraints(5, 1, -2, 1));
		gp1.add(new UIButton("13"), fl1.getConstraints(-1, 1, -1, 1));
		
		UIGroupPanel gp2 = new UIGroupPanel("2. Case");
		UIFormLayout fl2 = new UIFormLayout();
		gp2.setLayout(fl2);
		gp2.add(new UIButton("1"), fl2.getConstraints(0, 0));
		gp2.add(new UIButton("2"), fl2.getConstraints(1, 0));
		gp2.add(new UIButton("3"), fl2.getConstraints(-2, 0));
		gp2.add(new UIButton("4"), fl2.getConstraints(-1, 0));
		
		panelFormLayout.add(gp1, layout.getConstraints(0, 0, -1, 0));
		panelFormLayout.add(gp2, layout.getConstraints(0, 1, -1, 1));	
	}
	
	/**
	 * Creates the panel to show the function of the linked cell editor.
	 */
	private void createLinkedCellEditorMainPanel()
	{
		try
		{
			MemDataBook auswahl1 = new MemDataBook();
			auswahl1.setName("auswahl1");
			auswahl1.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			auswahl1.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUSWAHL1"));
		    auswahl1.open();
			MemDataBook auswahl2 = new MemDataBook();
			auswahl2.setName("auswahl2");
			auswahl2.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_ID", new BigDecimalDataType()));
			auswahl2.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_AUSWAHL1"));
			auswahl2.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			auswahl2.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUSWAHL2"));
			auswahl2.open();
			MemDataBook auswahl3 = new MemDataBook();
			auswahl3.setName("auswahl3");
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_ID", new BigDecimalDataType()));
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_AUSWAHL1"));
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS2_ID", new BigDecimalDataType()));
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS2_AUSWAHL2"));
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			auswahl3.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUSWAHL3"));
			auswahl3.open();
			MemDataBook auswahl4 = new MemDataBook();
			auswahl4.setName("auswahl4");
			auswahl4.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_ID", new BigDecimalDataType()));
			auswahl4.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_AUSWAHL1"));
			auswahl4.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			auswahl4.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUSWAHL4"));
			auswahl4.open();
			MemDataBook auswahl5 = new MemDataBook();
			auswahl5.setName("auswahl5");
			auswahl5.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
			auswahl5.getRowDefinition().addColumnDefinition(new ColumnDefinition("NUMMER"));
			auswahl5.getRowDefinition().addColumnDefinition(new ColumnDefinition("ARTIKEL"));
			auswahl5.open();

			
			
			UILinkedCellEditor lceAuswahl1 = new UILinkedCellEditor(new ReferenceDefinition(
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1"}, 
					auswahl1, 
					new String[] {"ID", "AUSWAHL1"}));
			UILinkedCellEditor lceAuswahl2 = new UILinkedCellEditor(new ReferenceDefinition(
							new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS2_ID", "AUS2_AUSWAHL2"}, 
							auswahl2, 
							new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "ID", "AUSWAHL2"}));
			lceAuswahl2.setSearchColumnMapping(new ColumnMapping(new String[] {"AUS1_ID", "AUS1_AUSWAHL1"}));
			UILinkedCellEditor lceAuswahl3 = new UILinkedCellEditor(new ReferenceDefinition(
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS2_ID", "AUS2_AUSWAHL2", "AUS3_ID", "AUS3_AUSWAHL3"}, 
					auswahl3, 
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS2_ID", "AUS2_AUSWAHL2", "ID", "AUSWAHL3"}));
			lceAuswahl3.setSearchColumnMapping(new ColumnMapping(new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS2_ID", "AUS2_AUSWAHL2"}));
			lceAuswahl3.setValidationEnabled(false);
			UILinkedCellEditor lceAuswahl4 = new UILinkedCellEditor(new ReferenceDefinition(
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS4_ID", "AUS4_AUSWAHL4"}, 
					auswahl4, 
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "ID", "AUSWAHL4"}));
			lceAuswahl4.setSearchColumnMapping(new ColumnMapping(new String[] {"AUS1_ID", "AUS1_AUSWAHL1"}));

			UILinkedCellEditor lceAuswahl4Mask = new UILinkedCellEditor(new ReferenceDefinition(
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "AUS4_ID", "AUS4_AUSWAHL4"}, 
					auswahl4, 
					new String[] {"AUS1_ID", "AUS1_AUSWAHL1", "ID", "AUSWAHL4"}));
			lceAuswahl4Mask.setSearchColumnMapping(new ColumnMapping(new String[] {"AUS1_ID", "AUS1_AUSWAHL1"}));
			lceAuswahl4Mask.setDisplayConcatMask(" - ");
			
			UILinkedCellEditor lceAuswahl5 = new UILinkedCellEditor(new ReferenceDefinition(
					new String[] {"ARTI_ID", "ARTI_NUMMER", "ARTI_ARTIKEL"}, 
					auswahl5, 
					new String[] {"ID", "NUMMER", "ARTIKEL"}));
			lceAuswahl5.setValidationEnabled(false);
			lceAuswahl5.setDoNotClearColumnNames("ARTI_NUMMER", "ARTI_ARTIKEL");
			
		    MemDataBook data = new MemDataBook();
		    data.setName("data");
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("DATA"));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_ID", new BigDecimalDataType(lceAuswahl1)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS1_AUSWAHL1", new StringDataType(lceAuswahl1)));
			data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS2_ID", new BigDecimalDataType(lceAuswahl2)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS2_AUSWAHL2", new StringDataType(lceAuswahl2)));
			data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS3_ID", new BigDecimalDataType(lceAuswahl3)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS3_AUSWAHL3", new StringDataType(lceAuswahl3)));
			data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS4_ID", new BigDecimalDataType(lceAuswahl4Mask)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("AUS4_AUSWAHL4", new StringDataType(lceAuswahl4)));
			data.getRowDefinition().addColumnDefinition(new ColumnDefinition("ARTI_ID", new BigDecimalDataType(lceAuswahl5)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("ARTI_NUMMER", new StringDataType(lceAuswahl5)));
		    data.getRowDefinition().addColumnDefinition(new ColumnDefinition("ARTI_ARTIKEL", new StringDataType(lceAuswahl5)));
			data.open();
			data.getRowDefinition().setColumnView(null, new ColumnView(data.getRowDefinition().getColumnNames()));

			auswahl1.insert(false);
			auswahl1.setValues(null, new Object[] {"1", "Auswahl 1"});
			auswahl1.insert(false);
			auswahl1.setValues(null, new Object[] {"2", "Auswahl 2"});
			auswahl1.saveAllRows();
			
			auswahl2.insert(false);
			auswahl2.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1"});
			auswahl2.insert(false);
			auswahl2.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2"});
			auswahl2.insert(false);
			auswahl2.setValues(null, new Object[] {"2", "Auswahl 2", "3", "Auswahl 2 - 1"});
			auswahl2.insert(false);
			auswahl2.setValues(null, new Object[] {"2", "Auswahl 2", "4", "Auswahl 2 - 2"});
			auswahl2.saveAllRows();
			
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "1", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "2", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "3", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "4", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "5", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "6", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "7", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "8", "Auswahl 2 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "9", "Auswahl 1 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl 1 - 1", "10", "Auswahl 1 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "11", "Auswahl 1 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl 1 - 2", "12", "Auswahl 1 - 2 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "13", "Auswahl 2 - 1 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "1", "Auswahl 2 - 1", "14", "Auswahl 2 - 1 - 2"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "15", "Auswahl 2 - 2 - 1"});
			auswahl3.insert(false);
			auswahl3.setValues(null, new Object[] {"2", "Auswahl 2", "2", "Auswahl 2 - 2", "16", "Auswahl 2 - 2 - 2"});
			auswahl3.saveAllRows();
			
			auswahl4.insert(false);
			auswahl4.setValues(null, new Object[] {"1", "Auswahl 1", "1", "Auswahl2 1 - 1"});
			auswahl4.insert(false);
			auswahl4.setValues(null, new Object[] {"1", "Auswahl 1", "2", "Auswahl2 1 - 2"});
			auswahl4.insert(false);
			auswahl4.setValues(null, new Object[] {"2", "Auswahl 2", "3", "Auswahl2 2 - 1"});
			auswahl4.insert(false);
			auswahl4.setValues(null, new Object[] {"2", "Auswahl 2", "4", "Auswahl2 2 - 2"});
			auswahl4.saveAllRows();

			auswahl5.insert(false);
			auswahl5.setValues(null, new Object[] {"1", "138.01.01", "Pistenbully 100"});
			auswahl5.insert(false);
			auswahl5.setValues(null, new Object[] {"2", "138.02.02", "Pistenbully 200"});
			auswahl5.saveAllRows();

			
			data.insert(false);
			
			UIEditor editAuswahl1 = new UIEditor(data, "AUS1_AUSWAHL1");
			UIEditor editAuswahl2 = new UIEditor(data, "AUS2_AUSWAHL2");
			UIEditor editAuswahl3 = new UIEditor(data, "AUS3_AUSWAHL3");
			UIEditor editAuswahl4 = new UIEditor(data, "AUS4_AUSWAHL4");
			UIEditor editAuswahl4Mask = new UIEditor(data, "AUS4_ID");
			UIEditor editAuswahl5Id = new UIEditor(data, "ARTI_ID");
			UIEditor editAuswahl5Nr = new UIEditor(data, "ARTI_NUMMER");
			UIEditor editAuswahl5Ar = new UIEditor(data, "ARTI_ARTIKEL");
			
			panelLinkedCellEditor = new UIPanel();
			UIFormLayout layout = new UIFormLayout();
			panelLinkedCellEditor.setLayout(layout);
			
			panelLinkedCellEditor.add(new UILabel("Auswahl1"),	layout.getConstraints(0, 0));
			panelLinkedCellEditor.add(editAuswahl1, 			layout.getConstraints(1, 0));
			panelLinkedCellEditor.add(new UILabel("Auswahl2"),	layout.getConstraints(2, 0));
			panelLinkedCellEditor.add(editAuswahl2, 			layout.getConstraints(3, 0));
			panelLinkedCellEditor.add(new UILabel("Auswahl3"),	layout.getConstraints(4, 0));
			panelLinkedCellEditor.add(editAuswahl3, 			layout.getConstraints(5, 0));
			panelLinkedCellEditor.add(new UILabel("Auswahl4"),	layout.getConstraints(6, 0));
			panelLinkedCellEditor.add(editAuswahl4, 			layout.getConstraints(7, 0));
			panelLinkedCellEditor.add(new UILabel("Auswahl4 Mask"),	layout.getConstraints(6, 1));
			panelLinkedCellEditor.add(editAuswahl4Mask, 			layout.getConstraints(7, 1, -1, 1));
			panelLinkedCellEditor.add(new UILabel("Artikel Id"),	layout.getConstraints(0, 2));
			panelLinkedCellEditor.add(editAuswahl5Id, 				layout.getConstraints(1, 2));
			panelLinkedCellEditor.add(new UILabel("Artikel Nummer"), layout.getConstraints(2, 2));
			panelLinkedCellEditor.add(editAuswahl5Nr, 				 layout.getConstraints(3, 2));
			panelLinkedCellEditor.add(new UILabel("Artikel"),		 layout.getConstraints(4, 2));
			panelLinkedCellEditor.add(editAuswahl5Ar, 				 layout.getConstraints(5, 2));
			panelLinkedCellEditor.add(new UITable(data),		     layout.getConstraints(0, 3, -1, -1));
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Creates a MenuBar with Menues and MenuItems. 
	 * 
	 * @return the UIMenuBar 
	 */
	public UIMenuBar createMenu()
	{
		UIMenuBar menuBar = new UIMenuBar();
		UIMenu subRemoveMenu;
		UIMenu filemenu;
		UIMenu helpmenu;
		
//		// Create Menus 
		filemenu = new UIMenu();		// Menu File
		filemenu.setText("File");
//		
		helpmenu = new UIMenu();     // Menu Help
		helpmenu.setText("Add Removed Item");
		
		UIMenuItem addItemtoSubMenu = createMenuItem("doClickMenuItem", null, "Edit", new UIImage("/com/sibvisions/apps/simpleapp/images/ok.png", null));
		helpmenu.add(addItemtoSubMenu);
		
		UIMenu emtypmenu = new UIMenu();
		emtypmenu.setText("Empty");
		
		// Create and add MenuItems to FileMenu
		UICheckBoxMenuItem edit = createCheckBoxMenuItem("doClickMenuItem", null, "Edit", 
				                                         new UIImage("ok.png", 
				                                                     FileUtil.getContent(ResourceUtil.getResourceAsStream("/com/sibvisions/apps/simpleapp/images/ok.png"))));
		filemenu.add(edit);
		
		UIMenuItem open =  createMenuItem("doClickMenuItem", null, "Open", null);
		filemenu.add(open);

		filemenu.addSeparator(1);	// MenuItemSeperator
		
		UIMenuItem close = createMenuItem("doClickMenuItem", null, "Close", null);
		filemenu.add(close, 0);
		
		subRemoveMenu = new UIMenu("Remove me");
		subRemoveMenu.eventAction().addListener(this, "doClickMenuItem");
		
		UIMenuItem subMenu1 = createMenuItem("doClickMenuItem", null, "Submenu 1", null);
		UIMenuItem subMenu2 = createMenuItem("doClickMenuItem", null, "Submenu 2", null);
		UIMenuItem subMenu3 = createMenuItem("doClickMenuItem", null, "Submenu 3", null);
		
		subRemoveMenu.add(subMenu1);
		subRemoveMenu.add(subMenu2);
		subRemoveMenu.add(subMenu3);
		
		filemenu.add(subRemoveMenu);
		
		menuBar.add(filemenu);
		menuBar.add(helpmenu);				
		
		menuBar.add(emtypmenu, 1);

		return menuBar;
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
		if (pImage != null)
		{
			item.setImage(pImage);
		}
		return item;
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
	public UICheckBoxMenuItem createCheckBoxMenuItem(String pAction, String pActionCommand, String pText, UIImage pImage)
	{
		UICheckBoxMenuItem item = new UICheckBoxMenuItem();
		
		item.setText(pText);
		item.eventAction().addListener(this, pAction);
		item.setActionCommand(pActionCommand);
		if (pImage != null)
		{
			item.setImage(pImage);
		}
		return item;
	}
	
	/**
	 * Creates the popup menu.
	 */
	private void createPopup()
	{
		if (!create)
		{
			UIMenu item1 = new UIMenu("Actions");
			
			UIMenuItem subMenuDelete = createMenuItem("doDelete", null, "Delete", null);
			UIMenuItem subMenuInsert = createMenuItem("doInsert", null, "Insert", null);
			UIMenuItem subMenuReadOnly = createMenuItem("doReadOnly", null, "ReadOnly", null);
			
			item1.add(subMenuInsert);
			item1.addSeparator();
			item1.add(subMenuDelete);
			item1.addSeparator();
			item1.add(subMenuReadOnly);				
	
			UIMenuItem item2 = new UIMenuItem();
			item2.setText("Do nothing with icon");
			item2.setImage(UIImage.getImage("/com/sibvisions/apps/simpleapp/images/ok.png"));
	
			popupMenu.add(item1);
			popupMenu.addSeparator();
			popupMenu.add(item2);
			popupMenu.addSeparator();
			
			create = true;
		}
	}

	/**
	 * Is called after the popup event.
	 * 
	 * @param pEvent 
	 */
	public void doPopup(UIMouseEvent pEvent)
	{
		if (pEvent.isPopupTrigger() && popupMenu != null)
		{	
			createPopup();
			
			popupMenu.show(pEvent.getSource(), pEvent.getX(), pEvent.getY());
		}
	}	
	
	/**
	 * Is called after insert is clicked in popup menue.
	 * 
	 * @throws Exception if inserting fails
	 */
	public void doInsert() throws Exception
	{
	    memData.insert(false);
	}
	
	/**
	 * Is called after delete is clicked in popup menue.
	 * 
	 * @throws Exception if deleting failed
	 */
	public void doDelete() throws Exception
	{
		memData.delete();
	}

	/**
	 * Is called after readonly is clicked in popup menue.
	 * 
	 * @throws Exception if readonly switching failed
	 */
	public void doReadOnly() throws Exception
	{
		memData.setReadOnly(!memData.isReadOnly());	
	}
	
	/**
	 * Creates the no layout pane.
	 * 
	 * @return the panel
	 */
	private UIPanel createFormLayoutPanel()
	{
		UILabel lab1 = new UILabel();
		lab1.setText("Label 1");
		lab1.setBackground(UIColor.red);
		lab1.setPreferredSize(50, 50);
		
		UILabel lab2 = new UILabel();
		lab2.setText("Label 2");
		lab2.setBackground(UIColor.blue);
		lab2.setPreferredSize(50, 50);
		
		UILabel lab3 = new UILabel();
		lab3.setText("Label 3");
		lab3.setBackground(UIColor.cyan);
		lab3.setPreferredSize(40, 40);
		
		UILabel lab4 = new UILabel();
		lab4.setText("Label 4");
		lab4.setBackground(UIColor.green);
		lab4.setPreferredSize(70, 70);
		
		UILabel lab5 = new UILabel();
		lab5.setText("Label 5");
		lab5.setBackground(UIColor.magenta);
		lab5.setPreferredSize(50, 30);
		
		UIFormLayout layout = new UIFormLayout();
		
		layout.setMargins(0, 0, 0, 0);
		layout.setHorizontalGap(10);
		layout.setVerticalGap(10);
		
		UIPanel panel = new UIPanel();
		panel.setLayout(layout);
		layout.setHorizontalGap(10);
		panel.add(lab1, layout.getConstraints(0, 0));
		layout.setHorizontalGap(60);
		panel.add(lab2, layout.getConstraints(1, 0));
		panel.add(lab3, layout.getConstraints(0, 1, 1, 1));

		panel.add(lab4, layout.getConstraints(2, 0, -1, 1));
		
		panel.add(lab5, layout.getConstraints(0, 2, -1, -1));
		
		return panel;
	}		
	
	/**
	 * Creates a Panel with a null layout.
	 * 
	 * @return panel with layout = null
	 */
	private UIPanel createNoLayoutPanel()
	{		
		final UIPanel panel = new UIPanel();
		panel.setLayout(null);
				
		UILabel lab1 = new UILabel();
		lab1.setText("Label 1");
		lab1.setBackground(UIColor.red);
		lab1.setPreferredSize(50, 50);
		
		UILabel lab2 = new UILabel();
		lab2.setText("Label 2");
		lab2.setBackground(UIColor.blue);
		lab2.setPreferredSize(50, 50);
		
		UILabel lab3 = new UILabel();
		lab3.setText("Label 3");
		lab3.setBackground(UIColor.cyan);
		lab3.setPreferredSize(40, 40);
		
		UILabel lab4 = new UILabel();
		lab4.setText("Label 4");
		lab4.setBackground(UIColor.green);
		lab4.setPreferredSize(70, 70);
		
		UILabel lab5 = new UILabel();
		lab5.setText("Label 5");
		lab5.setBackground(UIColor.magenta);
		lab5.setPreferredSize(50, 30);

		panel.add(lab1);
		panel.add(lab2);
		panel.add(lab3);
		panel.add(lab4);
		panel.add(lab5);
		
		lab1.setBounds(10, 10, 50, 50);
		lab2.setBounds(30, 30, 50, 50);
		lab3.setBounds(100, 10, 40, 40);
		lab4.setBounds(10, 100, 70, 70);
		lab5.setBounds(100, 100, 50, 30);
		
		final UIFlowLayout flowLayoutHorizontal = new UIFlowLayout(UIFlowLayout.HORIZONTAL);
		final UIFlowLayout flowLayoutVertical = new UIFlowLayout(UIFlowLayout.VERTICAL);
		final UIBorderLayout borderLayout = new UIBorderLayout();
		
		final UIRadioButton butNoLayout = new UIRadioButton("No Layout");
		final UIRadioButton butFlowLayoutHorizontal = new UIRadioButton("Flow Layout Horizontal");
		final UIRadioButton butFlowLayoutVertical = new UIRadioButton("Flow Layout Vertical");
		final UIRadioButton butBorderLayout = new UIRadioButton("Border Layout");
		
		UIGroupPanel panLayouts = new UIGroupPanel("Switch between Layouts");	
		panLayouts.setSize(160, 600);
		panLayouts.setLayout(newUIFlowLayout());
		panLayouts.add(butNoLayout);
		panLayouts.add(butFlowLayoutHorizontal);
		panLayouts.add(butFlowLayoutVertical);
		panLayouts.add(butBorderLayout);
		
		
		final ActionGroup agLayouts = new ActionGroup(butNoLayout, butFlowLayoutHorizontal, butFlowLayoutVertical, butBorderLayout);
		agLayouts.setSelectedIndex(0);
		agLayouts.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				if (pActionEvent.getSource().equals(butNoLayout))
				{
					panel.setLayout(null);
				}
				else if (pActionEvent.getSource().equals(butFlowLayoutHorizontal))
				{
					panel.setLayout(flowLayoutHorizontal);
				}
				else if (pActionEvent.getSource().equals(butFlowLayoutVertical))
				{
					panel.setLayout(flowLayoutVertical);
				}	
				else if (pActionEvent.getSource().equals(butBorderLayout))
				{
					panel.setLayout(borderLayout);
				}					
			}
		});		
		
		
		UIPanel panelComplete = new UIPanel(new UIBorderLayout(10, 10));
		panelComplete.setSize(600, 600);
		panelComplete.add(panLayouts, UIBorderLayout.EAST);
		panelComplete.add(panel, UIBorderLayout.CENTER);
		
		
		return panelComplete;
	}	

	/**
	 * Creates a Panel with a FlowLayout.
	 * 
	 * @return a Panel with a FlowLayout
	 */
	public UIPanel createFlowLayoutPanel() 
	{
		UILabel lab1 = new UILabel();
		lab1.setText("Label 1");
		lab1.setBackground(UIColor.red);
//		lab1.setSize(200, 40);
		
		UILabel lab2 = new UILabel();
		lab2.setText("Label 2");
		lab2.setBackground(UIColor.blue);
//		lab2.setSize(100, 30);
		
		UILabel lab3 = new UILabel();
		lab3.setText("Label 3");
		lab3.setBackground(UIColor.cyan);
//		lab3.setSize(40, 40);
		
		UILabel lab4 = new UILabel();
		lab4.setText("Label 4");
		lab4.setBackground(UIColor.green);
		lab4.setSize(340, 70);
		
		UILabel lab5 = new UILabel();
		lab5.setText("Label 5");
		lab5.setBackground(UIColor.magenta);
//		lab5.setSize(50, 30);
		
		UILabel lab6 = new UILabel();
		lab6.setText("Label 6 asdfasdf asdf sadfasd fasd asd");
		lab6.setBackground(UIColor.green);
//		lab6.setSize(340, 70);		
		
		final UIFlowLayout layout = new UIFlowLayout(UIFlowLayout.VERTICAL);
		layout.setHorizontalAlignment(UIFlowLayout.ALIGN_CENTER);
		layout.setVerticalAlignment(UIFlowLayout.ALIGN_CENTER);
		layout.setComponentAlignment(UIFlowLayout.ALIGN_CENTER);
		
//		layout.setOrientation(UIFlowLayout.VERTICAL);
//		layout.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
//		layout.setHorizontalAlignment(IAlignmentConstants.ALIGN_STRETCH);
//		layout.setComponentAlignment(IAlignmentConstants.ALIGN_STRETCH);
		
		
		layout.setMargins(10, 10, 10, 10);
		layout.setHorizontalGap(10);
		layout.setVerticalGap(10);
		
		final UIPanel panel = new UIPanel();
		panel.setLayout(layout);
		panel.add(lab1);
		panel.add(lab2);
		panel.add(lab3);
		panel.add(lab4);
		panel.add(lab5);
		panel.add(lab6);
		
		// Configuration Panel
		
		UIRadioButton butHLeft = new UIRadioButton("Left");
		UIRadioButton butHCenter = new UIRadioButton("Center");
		UIRadioButton butHRight = new UIRadioButton("Right");
		UIRadioButton butHStretch = new UIRadioButton("Stretch");
		UIGroupPanel panHorizontal = new UIGroupPanel("Horizontal Alignment");
		panHorizontal.setLayout(newUIFlowLayout());
		panHorizontal.add(butHLeft);
		panHorizontal.add(butHCenter);
		panHorizontal.add(butHRight);
		panHorizontal.add(butHStretch);
		
		final ActionGroup horizontalAlignment = new ActionGroup(butHLeft, butHCenter, butHRight, butHStretch);
		horizontalAlignment.setSelectedIndex(1);
		horizontalAlignment.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				layout.setHorizontalAlignment(horizontalAlignment.getSelectedIndex());
//				panel.setLayout(layout); // Force Rendering
			}
		});
		
		UIRadioButton butVLeft = new UIRadioButton("Top");
		UIRadioButton butVCenter = new UIRadioButton("Center");
		UIRadioButton butVRight = new UIRadioButton("Bottom");
		UIRadioButton butVStretch = new UIRadioButton("Stretch");
		UIGroupPanel panVertical = new UIGroupPanel("Vertical Alignment");	
		panVertical.setLayout(newUIFlowLayout());
		panVertical.add(butVLeft);
		panVertical.add(butVCenter);
		panVertical.add(butVRight);
		panVertical.add(butVStretch);
		final ActionGroup verticalAlignment = new ActionGroup(butVLeft, butVCenter, butVRight, butVStretch);
		verticalAlignment.setSelectedIndex(1);
		verticalAlignment.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				layout.setVerticalAlignment(verticalAlignment.getSelectedIndex());
//				panel.setLayout(layout); // Force Rendering
			}
		});
		
		UIRadioButton butCLeft = new UIRadioButton("Top");
		UIRadioButton butCCenter = new UIRadioButton("Center");
		UIRadioButton butCRight = new UIRadioButton("Bottom");
		UIRadioButton butCStretch = new UIRadioButton("Stretch");
		UIGroupPanel panComponent = new UIGroupPanel("Component Alignment");
		panComponent.setLayout(newUIFlowLayout());
		panComponent.add(butCLeft);
		panComponent.add(butCCenter);
		panComponent.add(butCRight);
		panComponent.add(butCStretch);
		final ActionGroup componentAlignment = new ActionGroup(butCLeft, butCCenter, butCRight, butCStretch);
		componentAlignment.setSelectedIndex(1);
		componentAlignment.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				layout.setComponentAlignment(componentAlignment.getSelectedIndex());
//				panel.setLayout(layout); // Force Rendering
			}
		});
		
		UIRadioButton butHorizontal = new UIRadioButton("Horizontal");
		UIRadioButton butVertical = new UIRadioButton("Vertical");
		UIGroupPanel panOrientation = new UIGroupPanel("Orientation");
		panOrientation.setLayout(newUIFlowLayout());			
		panOrientation.add(butHorizontal);
		panOrientation.add(butVertical);
		final ActionGroup orientation = new ActionGroup(butHorizontal, butVertical);
		orientation.setSelectedIndex(0);
		orientation.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				layout.setOrientation(orientation.getSelectedIndex());
//				panel.setLayout(layout); // Force Rendering
			}
		});
		
		final UICheckBox butAutoWrap = new UICheckBox("Auto Wrap");
		butAutoWrap.eventAction().addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				layout.setAutoWrap(butAutoWrap.isSelected());
//				panel.setLayout(layout); // Force Rendering
			}
		});
		UIGroupPanel panOptions = new UIGroupPanel("Options");	
		UIFlowLayout layOptions = new UIFlowLayout(UIFlowLayout.VERTICAL);
		layOptions.setHorizontalAlignment(UIFlowLayout.ALIGN_LEFT);
		layOptions.setComponentAlignment(UIFlowLayout.ALIGN_LEFT);
		panOptions.setLayout(layOptions);
		panOptions.add(butAutoWrap);
		
		UIFlowLayout layConfig = new UIFlowLayout();
//		layConfig.setHorizontalAlignment(UIFlowLayout.ALIGN_STRETCH);
//		layConfig.setVerticalAlignment(UIFlowLayout.ALIGN_TOP);
//		layConfig.setComponentAlignment(UIFlowLayout.ALIGN_STRETCH);
		layConfig.setOrientation(UIFlowLayout.VERTICAL);
		UIPanel panConfig = new UIPanel();
//		panConfig.setSize(160, 600);
		panConfig.setLayout(layConfig);
		panConfig.add(panOrientation);
		panConfig.add(panHorizontal);
		panConfig.add(panVertical);
		panConfig.add(panComponent);
		panConfig.add(panOptions);
		
		UIPanel panComplete = new UIPanel();
//		panComplete.setSize(900, 600);
		panComplete.setLayout(new UIBorderLayout(10, 0));
		
		panComplete.add(panel, UIBorderLayout.CENTER);
		panComplete.add(panConfig, UIBorderLayout.EAST);

		return panComplete;		
	}
	
	/**
	 * Creates the no layout pane.
	 * 
	 * @return the panel
	 */
	private UIFlowLayout newUIFlowLayout()
	{
		UIFlowLayout layout = new UIFlowLayout(UIFlowLayout.VERTICAL);
		layout.setHorizontalAlignment(UIFlowLayout.ALIGN_LEFT);
		
		return layout;
	}	
	
	/**
	 * Creates a Panel with a BorderLayout.
	 * 
	 * @return a Panel with a BorderLayout.
	 */
	public UIPanel createBorderLayoutPanel() 
	{	
		UIPanel panel = new UIPanel();
		
		UIBorderLayout layout = new UIBorderLayout();
		layout.setMargins(5, 5, 5, 5);
		layout.setVerticalGap(10);
		layout.setHorizontalGap(10);
		
		panel.setLayout(layout);
		
		UILabel labelNorth = new UILabel("NORHT Label");
		labelNorth.setBackground(new UIColor(0, 153, 0));
		labelNorth.setFont(new UIFont("Verdana", UIFont.BOLD, 12));
//		labelNorth.setSize(new UIDimension(100, 400));
		
		UILabel labelSouth = new UILabel("SOUTH Label");
		labelSouth.setBackground(new UIColor(0, 153, 0));
		labelSouth.setFont(new UIFont("Verdana", UIFont.BOLD, 12));
		
		UILabel labelWest = new UILabel("WEST Label");
		labelWest.setBackground(new UIColor(204, 0, 0));
		labelWest.setFont(new UIFont("Verdana", UIFont.BOLD, 12));
//		labelWest.setSize(new UIDimension(200, 200));
		
		UILabel labelEeast = new UILabel("EAST Label");
		labelEeast.setBackground(new UIColor(204, 0, 0));
		labelEeast.setFont(new UIFont("Verdana", UIFont.BOLD, 12));
//		labelEeast.setSize(new UIDimension(300, 300));
		
		UILabel labelCenter = new UILabel("CENTER Label");
//		labelCenter.setSize(new UIDimension(100, 100));
		labelCenter.setBackground(new UIColor(120, 120, 0));
		labelCenter.setFont(new UIFont("Verdana", UIFont.BOLD, 20));
		
		panel.add(labelNorth, UIBorderLayout.NORTH);
		panel.add(labelSouth, UIBorderLayout.SOUTH);
		panel.add(labelWest, UIBorderLayout.WEST);
		panel.add(labelEeast, UIBorderLayout.EAST);
		panel.add(labelCenter, UIBorderLayout.CENTER);
		
		return panel;
		
	}	
	
	/**
	 * Creates a Test Split Panel Component.
	 * 
	 * @return UIPanel
	 */
	public UISplitPanel createSplit() 
	{

		UISplitPanel splitPanel = new UISplitPanel();

		UISplitPanel splitPanelVert = new UISplitPanel();
		splitPanelVert.setOrientation(UISplitPanel.SPLIT_TOP_BOTTOM);

		splitPanel.setFirstComponent(new UIPanel());
		
		
		UIPanel firstPanel = new UIPanel();
		
		UIFormLayout layoutFirst = new UIFormLayout();
		
		firstPanel.setLayout(layoutFirst);
		
		firstPanel.add(new UITextField(), layoutFirst.getConstraints(0, 0, 0, 0));
		firstPanel.add(new UITextField(), layoutFirst.getConstraints(0, 1, 0, 1));
		firstPanel.add(new UITextField(), layoutFirst.getConstraints(0, 2, 0, 2));
		
		
		UIPanel secondPanel = new UIPanel();
		
		UIFormLayout layoutSecond = new UIFormLayout();
		
		secondPanel.setLayout(layoutSecond);
		
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 0, 0, 0));
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 1, 0, 1));
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 2, 0, 2));	
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 3, 0, 3));	
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 4, 0, 4));	
		secondPanel.add(new UITextField(), layoutSecond.getConstraints(0, 5, 0, 5));	
		
		splitPanelVert.setFirstComponent(firstPanel);
		splitPanelVert.setSecondComponent(secondPanel);
		
		splitPanel.setSecondComponent(splitPanelVert);
		
		return splitPanel;
	}	
	
	/**
	 * Creates a Test Panel for the Button Component.
	 * 
	 * @return UIPanel
	 */
	public UIPanel createButtons() 
	{
		UIPanel panel = new UIPanel();
		
		UIButton butDisabled = new UIButton();
		butDisabled.setText("Disabled");
		butDisabled.setEnabled(false);
		
		panel.add(butDisabled, IFormLayout.NEWLINE);
		
		UIToggleButton toggleButton = new UIToggleButton();
		toggleButton.setText("Toggle Button with Image Gap 10");
		toggleButton.setImageTextGap(10);
		
		panel.add(toggleButton);
		
		UIButton button = new UIButton("Change Icon when MouseOver");
		button.setSize(700, 50);
		button.setDefaultButton(true);
//		button.setEnabled(false);
//		button.setAccelerator(Key.getKeyOnPressed(67));
		
		panel.add(button);
		
		UIImage okImg = UIImage.getImage("/com/sibvisions/apps/simpleapp/images/ok.png");
		UIImage imageExit = UIImage.getImage("/javax/rad/genui/images/16x16/exit.png");
		button.setVerticalTextPosition(IAlignmentConstants.ALIGN_TOP);
		button.setImage(okImg);
		button.eventAction().addListener(this, "doButtonClick");
		button.setMouseOverImage(imageExit);
		button.setBackground(UIColor.green);
		
		toggleButton.setImage(button.getImage());	
		
		UIButton buttonNoBorder = new UIButton("Button with no border");
		buttonNoBorder.setHorizontalTextPosition(IAlignmentConstants.ALIGN_LEFT);
		buttonNoBorder.setFont(new UIFont(UIFont.SANS_SERIF, UIFont.BOLD, 14));
		buttonNoBorder.setForeground(UIColor.red);
		buttonNoBorder.setBorderPainted(false);
		buttonNoBorder.setBorderOnMouseEntered(true);
		
		panel.add(buttonNoBorder);
		
		UIButton buttonAsChoice = new UIButton();
		buttonAsChoice.setHorizontalTextPosition(IAlignmentConstants.ALIGN_CENTER);
		buttonAsChoice.setImage(UIImage.getImage(UIImage.CHECK_LARGE));
		buttonAsChoice.setBorderPainted(false);
		buttonAsChoice.setCursor(UICursor.getPredefinedCursor(ICursor.HAND_CURSOR));
		
		panel.add(buttonAsChoice);
		
        UIButton butNoBorder = new UIButton("No Border - hover me");
        butNoBorder.setBorderPainted(false);

        panel.add(butNoBorder);
		
		return panel;
	}		
	
	/**
	 * Creates a Test Panel for the Text Component.
	 * 
	 * @return UIPanel
	 */
	public UIPanel createTextComponents() 
	{
		UIPanel panel = new UIPanel(new UIFlowLayout(UIFlowLayout.VERTICAL));
		
		UILabel label1 = new UILabel("Label: Dialog, Bold, 20");
		label1.setFont(new UIFont("dialog", UIFont.BOLD, 20));
		panel.add(label1);

		UILabel label2 = new UILabel("Label: Verdana, Italic, 25");
		label2.setFont(new UIFont("verdana", UIFont.ITALIC, 25));
		panel.add(label2);

		labelChangeColor.setBackground(UIColor.createColor("255,0,0"));
		panel.add(labelChangeColor);
				
		UIButton button = new UIButton("Click me!");
		button.eventAction().addListener(this, "doChangeLabelColor");
		
		panel.add(button);

		UITextField textField = new UITextField("TextField");
		textField.setBorderVisible(true);
		panel.add(textField);

		UITextField textFieldForground = new UITextField("TextField with Color: blue");
		textFieldForground.setForeground(UIColor.createColor("0,0,255"));
		panel.add(textFieldForground);

		UITextField textFieldWithToolTip = new UITextField("TextField with ToolTip: I am a Tooltip");
		textFieldWithToolTip.setToolTipText("I am a Tooltip");
		panel.add(textFieldWithToolTip);	

		UIPasswordField passwordField = new UIPasswordField("Password");
		panel.add(passwordField);

		UITextField textFieldNoBorder = new UITextField("TextField without Border");
		textFieldNoBorder.setBorderVisible(false);
		panel.add(textFieldNoBorder);

		UITextArea textArea = new UITextArea("T\ne\nx\nt\nA\nr\ne\na");		
		panel.add(textArea);
		
		return panel;
	}		
	
	/**
	 * Creates a Test Panel for the RadioButtons Component.
	 * 
	 * @return UIPanel
	 */
	public UIPanel createRadioButtons() 
	{
		UIPanel panel = new UIPanel(new UIFlowLayout(UIFlowLayout.VERTICAL));
		
		UIRadioButton radioButton1 = new UIRadioButton("Button 1 in Group");
		radioButton1.eventAction().addListener(new IActionListener() 
		{
			@Override
			public void action(UIActionEvent pActionEvent) throws Throwable 
			{
				System.out.println("Button 1");
			}
		});

		UIRadioButton radioButton2 = new UIRadioButton("Button 2 in Group");
		radioButton2.eventAction().addListener(new IActionListener() 
		{
			@Override
			public void action(UIActionEvent pActionEvent) throws Throwable 
			{
				System.out.println("Button 2");
			}
		});
		
		ActionGroup agRadio = new ActionGroup();
		agRadio.addButton(radioButton1);
		agRadio.addButton(radioButton2);
		agRadio.eventAction().addListener(new IActionListener() 
		{
			@Override
			public void action(UIActionEvent pActionEvent) throws Throwable 
			{
				System.out.println("Action group changed: " + ((UIRadioButton)pActionEvent.getSource()).getText());
			}
		});
		
		UIRadioButton radioButton3 = new UIRadioButton("Button 3 not in Group");
		radioButton3.eventAction().addListener(new IActionListener() 
		{
			@Override
			public void action(UIActionEvent pActionEvent) throws Throwable 
			{
				System.out.println("Button 3");
			}
		});
		
		UIRadioButton radioButton4 = new UIRadioButton("Button 4 not in Group");
		radioButton4.eventAction().addListener(new IActionListener() 
		{
			@Override
			public void action(UIActionEvent pActionEvent) throws Throwable 
			{
				System.out.println("Button 4");
			}
		});
		
		radioButton4.setImage(UIImage.getImage("/javax/rad/genui/images/16x16/exit.png"));
		radioButton4.setImageTextGap(50);
		
		panel.add(radioButton1);
		panel.add(radioButton2);
		panel.add(radioButton3);
		panel.add(radioButton4);	

		return panel;
	}		
	
	/**
	 * Creates a Test Panel for CheckBox Component.
	 * 
	 * @return UIPanel
	 */
	public UIPanel createCheckBox() 
	{
		UIPanel panel = new UIPanel(new UIFlowLayout(UIFlowLayout.VERTICAL));

		UICheckBox checkBox1 = new UICheckBox("CheckBox 1");
		panel.add(checkBox1);
		UICheckBox checkBox2 = new UICheckBox("CheckBox 2");
		panel.add(checkBox2);
		
		checkBox1.setImage(UIImage.getImage("/javax/rad/genui/images/16x16/exit.png"));
		checkBox1.setImageTextGap(20);
		
		return panel;
	}		
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IContainer getContentPane()
	{
		return panelMain;
	}

	/**
	 * {@inheritDoc}
	 */
	public <OP> IContent showMessage(OP pOpener, int pIconType, int pButtonType, String pMessage, String pOkAction, String pCancelAction) throws Throwable
	{
		System.out.println(pMessage);
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleException(Throwable pThrowable)
	{
		pThrowable.printStackTrace();
	}
			
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Throws a {@link RuntimeException}.
	 * 
	 * @param pEvent not used
	 */
	public void doException(DataBookEvent pEvent)
	{
		throw new RuntimeException("END");
	}

	/**
	 * Is called, when the any menu item is clicked.
	 */
	public void doClickMenuItem()
	{
		System.out.println("Click Menu Item");
	}
	
	/**
	 * Is called when the button is clicked.
	 */
	public void doButtonClick()
	{
		System.out.println("Button Click");
	}

	/**
	 * Changes the color of the label.
	 */
	public void doChangeLabelColor()
	{
		labelChangeColor.setBackground(null);		
	}
	
	/**
	 * Is called when a tab in the component tabset is activated.
	 * 
	 * @param pEvent the tabset event
	 */
	public void doComponentTabActivated(UITabsetEvent pEvent)
	{
		System.out.println("Tab Activated: " + pEvent.getNewIndex());
	}
	
	/**
	 * Is called when a tab in the component tabset is deactivated.
	 * 
	 * @param pEvent the tabset event
	 */
	public void doComponentTabDeactivated(UITabsetEvent pEvent)
	{
		System.out.println("Tab Deactivated: " + pEvent.getOldIndex());
	}

	/**
	 * Starts report download.
	 * 
	 * @throws Throwable if starting download fails
	 */
	public void doDownloadReport() throws Throwable
	{
        DirectServerConnection dscon = new DirectServerConnection();

        macon = new MasterConnection(dscon);
        macon.setApplicationName("simpleapp");
        macon.setUserName("admin");
        macon.setPassword("admin");
        macon.open();

        if (panelCenter != null)
        {
            panelMain.remove(panelCenter);
        }
            
        
        desktop = new UIDesktopPanel();

        if (frame != null)
        {
            frame.close();
        }
        
        frame = new UIInternalFrame(desktop);
        frame.setLayout(new UIBorderLayout());
        frame.add(new UILabel("Waiting for download..."));
        frame.setSize(200, 200);
        
        panelCenter = new UIPanel();
        panelCenter.add(desktop);
        
        panelMain.add(panelCenter, UIBorderLayout.CENTER);  
        
        frame.setVisible(true);
        
        Window window = (Window)frame.getResource();
        
        final UI ui = ((VaadinFactory)getFactory()).getUI();
        
        if (!ui.getPushConfiguration().getPushMode().isEnabled()) // Set PollInterval if no push mode is set.
        {
            ui.setPollInterval(500);
        }
        
        window.addCloseListener(new CloseListener()
        {
            public void windowClose(CloseEvent pEvent)
            {
                // Disable manual pooling on window closing
                if (!ui.getPushConfiguration().getPushMode().isEnabled())
                {
                    ui.setPollInterval(-1);
                }
            }
        });
        
        macon.callAction(new ICallBackListener()
        {
            @Override
            public void callBack(CallBackEvent pEvent)
            {
                System.out.println("Window CLOSE");
                
                frame.close();
            }
            
        }, "asyncDownloadAction");
	}
	
}	// SimpleApplication
