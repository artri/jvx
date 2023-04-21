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
 */
package research;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Properties;

import javax.rad.application.IContent;
import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIRectangle;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.control.UICellFormat;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.genui.menu.UICheckBoxMenuItem;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIKeyEvent;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.layout.IBorderLayout;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.persist.jdbc.HSQLDBAccess;
import com.sibvisions.rad.ui.swing.ext.JVxEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxChoiceCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxDateCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxLinkedCellEditor;
import com.sibvisions.rad.ui.swing.impl.SwingApplication;
import com.sibvisions.rad.ui.swing.impl.container.SwingGroupPanel;

/**
 * Test implementation of IApplication.
 * 
 * @author Martin Handsteiner
 */
public class UITest extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Components. */
	private UILabel			label			= new UILabel();
	private UIIcon			icon			= new UIIcon();
	private UIButton		button			= new UIButton();
	private UIToggleButton	toggleButton	= new UIToggleButton();
	private UIButton		buttonToolBar	= new UIButton();

	private UIButton		buttonShowDesign= new UIButton();
	
	/** Controls. */
	private UITable			tableFirmen		= new UITable();
	private UITable			tablePersonen	= new UITable();
	
	/** Menu. */
	private UIMenuBar		menuBar			= new UIMenuBar();
	private UIMenu			menu			= new UIMenu();
	private UIMenuItem		menuItem		= new UIMenuItem();
	private UICheckBoxMenuItem	checkBoxMenuItem	= new UICheckBoxMenuItem();
	
	/** Container. */
	private UIPanel			panelFirmen		= new UIPanel();
	private UIPanel			panelPersonen	= new UIPanel();
	private UISplitPanel	splitH			= new UISplitPanel();
	private UISplitPanel	splitV			= new UISplitPanel();
	private UIDesktopPanel	desktop			= new UIDesktopPanel();
	private UIPanel			panelLayout		= new UIPanel();
	private UITabsetPanel	tabset 			= new UITabsetPanel();
	private UIToolBar		toolBar			= new UIToolBar();
	private UIPanel			directSwing		= new UIPanel();
	private UIPanel			directSwing2	= new UIPanel();
	private UIPanel			formLayoutTest	= new UIPanel();

	
	/** User-defined Controls. */
	private Navigation		navFirmen		= new Navigation();
	private Navigation		navPersonen		= new Navigation();
	
	/** Model definition. */
	private	MemDataBook		firmen			= new MemDataBook();
	private	MemDataBook		personen		= new MemDataBook();
	private	MemDataBook		personenLookup	= new MemDataBook();
	
	private UICellFormat        grayRowFormat	= new UICellFormat(new UIColor(232, 232, 232));
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** 
	 * Public Application Constructor. 
	 * 
	 * @param pLauncher the launcher configuration
	 */
	public UITest(UILauncher pLauncher) throws ModelException
	{
		super(pLauncher);
		
		pLauncher.setName("UITest");

		initDataBooks();
		
		UIPanel main			= new UIPanel();
		main.setLayout(new UIBorderLayout());
		
		IImage image = UIImage.getImage("/research/images/test.png");		

// ---- create Components
		label.setText("Mouse outside!");
		label.eventMouseEntered().addListener(this, "mouseEntered");
		label.eventMouseExited().addListener(this, "mouseExited");
		label.eventMousePressed().addListener(this, "mousePressed");
		label.eventMouseReleased().addListener(this, "mouseReleased");
		label.setName("LABEL");

		icon.setHorizontalAlignment(UIIcon.ALIGN_CENTER);
		icon.setImage(image);
		
		button.setText("Test Button");
		button.setImage(image);
		button.eventAction().addListener(this, "actionButton");
		button.eventKeyPressed().addListener(this, "keyPressed");
		button.eventKeyReleased().addListener(this, "keyReleased");
		button.eventKeyTyped().addListener(this, "keyTyped");
		button.setBorderOnMouseEntered(true);

		toggleButton.setText("Test ToggleButton");
		toggleButton.setImage(image);
		
		buttonToolBar.setImage(image);
		buttonToolBar.eventAction().addListener(this, "openInternalFrame");
		
		buttonShowDesign.setText("Show Designmode");
		buttonShowDesign.eventAction().addListener(this, "showDesignMode");

// ---- create Controls		
		tableFirmen.setDataBook(firmen);
		tableFirmen.setPreferredSize(new UIDimension(500, 300));
		tablePersonen.setDataBook(personen);
		tablePersonen.setPreferredSize(new UIDimension(500, 300));
		tablePersonen.setCellFormatter(this, "cellFormatter");
		
// ---- User-defined Controls		
		navFirmen.setDataBook(firmen);
		navPersonen.setDataBook(personen);
		
// ---- create Container		
		splitH.add(label);
		splitH.add(icon);
		
		panelFirmen.add(tableFirmen);
		panelFirmen.add(navFirmen);
//		((javax.swing.JComponent)panelFirmen.getResource()).add(new com.sibvisions.rad.ui.swing.JVxComboBase());

		panelPersonen.add(tablePersonen);
		panelPersonen.add(navPersonen);

		splitV.setOrientation(UISplitPanel.SPLIT_TOP_BOTTOM);
		splitV.add(panelFirmen);
		splitV.add(panelPersonen);
		
		JVxEditor editString = new JVxEditor();
//		editString.setCellEditor(new JVxTextCellEditor(JVxTextCellEditor.TEXT_PLAIN_MULTILINE));
		editString.setDataRow(personen);
		editString.setColumnName("VORNAME");
		
		JVxEditor editDate = new JVxEditor();
		editDate.setCellEditor(new JVxDateCellEditor());
		editDate.setDataRow(personen);
		editDate.setColumnName("GEBDAT");
		
		JVxEditor editNachname = new JVxEditor();
		editNachname.setDataRow(personen);
		editNachname.setColumnName("NACHNAME");
		
		JVxEditor editPersonen = new JVxEditor();
		editPersonen.setDataRow(personen);
		editPersonen.setColumnName("PERS_ID");
		
		SwingGroupPanel editorPanel = new SwingGroupPanel();
		editorPanel.setText("Hallo wo ist der Border?");
		editorPanel.getResource().setLayout(new FlowLayout());
		editorPanel.getResource().add(editString);
		editorPanel.getResource().add(editDate);
		editorPanel.getResource().add(editPersonen);
		editorPanel.getResource().add(editNachname);
		
		com.sibvisions.rad.ui.swing.ext.JVxTable swingTable = new com.sibvisions.rad.ui.swing.ext.JVxTable();
		swingTable.setDataBook(personen);
		
		((java.awt.Container)directSwing.getResource()).setLayout(new BorderLayout());
		((java.awt.Container)directSwing.getResource()).add(swingTable, BorderLayout.CENTER);
		directSwing.add(editorPanel, BorderLayout.EAST);
		
		com.sibvisions.rad.ui.swing.ext.JVxTree swingTree = new com.sibvisions.rad.ui.swing.ext.JVxTree();
		swingTree.setDataBooks(firmen, personen);
		
		((java.awt.Container)directSwing2.getResource()).setLayout(new BorderLayout());
		((java.awt.Container)directSwing2.getResource()).add(swingTree, BorderLayout.CENTER);
		
		UIFormLayout fl = new UIFormLayout();
		formLayoutTest.setLayout(fl);
		
		formLayoutTest.add(new UIButton("1"), fl.getConstraints(0, 0));
		formLayoutTest.add(new UIButton("2"), fl.getConstraints(1, 0));
		formLayoutTest.add(new UIButton("3"), fl.getConstraints(0, 1));
		formLayoutTest.add(new UIButton("4"), fl.getConstraints(-2, 0));
		formLayoutTest.add(new UIButton("5"), fl.getConstraints(-1, 0));
		formLayoutTest.add(new UIButton("6"), fl.getConstraints(-1, 1));
		formLayoutTest.add(new UIButton("7"), fl.getConstraints(0, -2));
		formLayoutTest.add(new UIButton("8"), fl.getConstraints(0, -1));
		formLayoutTest.add(new UIButton("9"), fl.getConstraints(1, -1));
		formLayoutTest.add(new UIButton("10"), fl.getConstraints(-1, -2));
		formLayoutTest.add(new UIButton("11"), fl.getConstraints(-2, -1));
		formLayoutTest.add(new UIButton("12"), fl.getConstraints(-1, -1));
		
		tabset.add(splitH, "Split Test");
		tabset.add(button, "Button Test");
		tabset.add(toggleButton, "ToggleButton Test");
		tabset.add(splitV, "Table Test");
		tabset.add(directSwing, "Direct Swing Table");
		tabset.add(directSwing2, "Direct Swing Tree");
		tabset.add(desktop, "Desktop Test");
		tabset.add(panelLayout, "Designmode Test");
		tabset.add(formLayoutTest, "FormLayout Test");
		tabset.add(new UIPanel(), "DISABLED");
		
		tabset.setEnabledAt(8, false);
		
		toolBar.add(buttonToolBar);
		toolBar.add(buttonShowDesign);
		
		main.add(toolBar, IBorderLayout.NORTH);
		main.add(tabset, IBorderLayout.CENTER);
		
		menuItem.setText("Menu Item");
		checkBoxMenuItem.setText("Check Box Menu Item");
		menu.setText("Menu");
		
		menu.add(menuItem);
		menu.add(checkBoxMenuItem);
		
		menuBar.add(menu);
		
		getLauncher().setMenuBar(menuBar);

		this.setLayout(new UIBorderLayout());
		add(main);


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
	public void notifyDestroy()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyVisible()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * The cell formatter.
	 * @param pDataBook the databook
	 * @param pDataPage the datapage
	 * @param pDataRow the datarow
	 * @param pColumnName the column name
	 * @param pRow the row number
	 * @param pColumn the column number
	 * @return the CellFormat
	 */
	public ICellFormat cellFormatter(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn)
	{
		if (pRow % 2 == 0)
		{
			return null;
		}
		else
		{
			return grayRowFormat;
		}
	}

	
	/**
     * Action for Button.
     * 
	 * @param pEvent the Event.
	 */
	public void actionButton(UIActionEvent pEvent)
	{
		System.out.println("Action: " + pEvent.getSource());
	}

    /**
     * MouseEntered.
	 * @param pEvent the Event.
	 */
	public void mouseEntered(UIMouseEvent pEvent)
	{
		label.setText("Mouse inside!");
		System.out.println("mouseEntered: " + pEvent.getX() + "  " + pEvent.getY());
	}

    /**
     * MouseExited.
	 * @param pEvent the Event.
	 */
	public void mouseExited(UIMouseEvent pEvent)
	{
		label.setText("Mouse outside!");
		System.out.println("mouseExited: " + pEvent.getX() + "  " + pEvent.getY());
	}

    /**
     * MousePressed.
     * 
	 * @param pEvent the Event.
	 */
	public void mousePressed(UIMouseEvent pEvent)
	{
		System.out.println("mousePressed: " + pEvent.getX() + "  " + pEvent.getY());
	}

    /**
     * MouseReleased.
     * 
	 * @param pEvent the Event.
	 */
	public void mouseReleased(UIMouseEvent pEvent)
	{
		System.out.println("mouseReleased: " + pEvent.getX() + "  " + pEvent.getY());
	}

    /**
     * KeyPressed.
     * 
	 * @param pEvent the Event.
	 */
	public void keyPressed(UIKeyEvent pEvent)
	{
		System.out.println("keyPressed: " + pEvent.getKeyCode() + "  " + pEvent.getKeyChar());
	}

    /**
     * KeyReleased.
     * 
	 * @param pEvent the Event.
	 */
	public void keyReleased(UIKeyEvent pEvent)
	{
		System.out.println("keyReleased: " + pEvent.getKeyCode() + "  " + pEvent.getKeyChar());
	}

    /**
    /**
     * KeyTyped.
     * 
	 * @param pEvent the Event.
	 */
	public void keyTyped(UIKeyEvent pEvent)
	{
		System.out.println("keyTyped: " + pEvent.getKeyCode() + "  " + pEvent.getKeyChar());
	}

    /**
     * Toolbar open new InternalFrame.
     * 
	 * @param pEvent the Event.
	 */
	public void openInternalFrame(UIActionEvent pEvent)
	{
        int count = desktop.getComponentCount();

        UILabel lVorname = new UILabel();
		lVorname.setText("Vorname:");
		UILabel lNachname = new UILabel();
		lNachname.setText("Nachname:");
		UILabel lAdresse = new UILabel();
		lAdresse.setText("Adresse:");
		UILabel lPLZ = new UILabel();
		lPLZ.setText("PLZ:");
		UILabel lOrt = new UILabel();
		lOrt.setText("Ort:");

		UITextField tVorname = new UITextField();
		tVorname.setColumns(6);
		UITextField tNachname = new UITextField();
		UITextField tAdresse = new UITextField();
		UITextField tPLZ = new UITextField();
		tPLZ.setColumns(4);
		UITextField tOrt = new UITextField();

		UIFormLayout formLayout = new UIFormLayout();
		
		UIPanel panel = new UIPanel();
		panel.setLayout(formLayout);

		panel.add(lVorname,  formLayout.getConstraints(0, 0));
		panel.add(tVorname,  formLayout.getConstraints(1, 0));
		panel.add(lNachname, formLayout.getConstraints(2, 0));
		panel.add(tNachname, formLayout.getConstraints(3, 0));
		panel.add(lAdresse,  formLayout.getConstraints(0, 1));
		panel.add(tAdresse,  formLayout.getConstraints(1, 1, 3, 1));
		panel.add(lPLZ,      formLayout.getConstraints(0, 2));
		panel.add(tPLZ,      formLayout.getConstraints(1, 2));
		panel.add(lOrt,      formLayout.getConstraints(2, 2));
		panel.add(tOrt,      formLayout.getConstraints(3, 2));
		
		UIInternalFrame internalFrame = new UIInternalFrame(desktop);
		internalFrame.setLayout(new UIBorderLayout());

		internalFrame.setTitle("Internal Frame Test " + count);
		
		internalFrame.add(panel, IBorderLayout.CENTER);

		internalFrame.setBounds(new UIRectangle(count * 20, count * 20, 350, 200));
		internalFrame.setVisible(true);
	}
	
    /**
     * Shows the Designmode Rects.
     * 
	 * @param pEvent the Event.
	 */
	public void showDesignMode(UIActionEvent pEvent)
	{
	}


    /**
	 * Gets a DataBook with Persons.
	 */
	private void initDataBooks()
	{
		try 
		{
			HSQLDBAccess dba	= new HSQLDBAccess();
			dba.setUrl("jdbc:hsqldb:file://c:/temp/personsdb"); // Wieso JdbcUrl notwendig, wenn HsqlDataSource?
			dba.setUsername("sa");
			dba.setPassword("");
//			dba.setWritebackIsolationLevel(MemDataBook.DATASOURCE);
	
			Properties pDBProperties = new Properties(); 
			pDBProperties.put("shutdown", Boolean.TRUE); 
			dba.setDBProperties(pDBProperties);
					
			dba.open();
			
//			firmen.setDataSource(dba);
			firmen.setName("FIRMEN");
			firmen.open();
			firmen.getRowDefinition().setColumnView(null, new ColumnView(firmen.getRowDefinition().getColumnNames()));
			//firmen.setSelectedRow(100000);
			
//			personen.setDataSource(dba);
			personen.setName("PERSONEN");
			personen.setMasterReference(new ReferenceDefinition(new String[] {"PERS_ID"}, firmen, new String[] {"ID"}));
            
			personen.open();
			
			personen.setSelectedRow(-1);

			personen.getRowDefinition().getColumnDefinition("NACHNAME").getDataType().setCellEditor(
				new JVxChoiceCellEditor(
	                   new Object[] {"Yes", 
	                		         "No"},
	                   new String[] {"/com/sibvisions/rad/application/images/16x16/add.png", 
                                     "/com/sibvisions/rad/application/images/16x16/delete.png"}));
			
//			personenLookup.setDataSource(dba);
			personenLookup.setName("PERSONEN");
			personenLookup.open();

			personen.getRowDefinition().getColumnDefinition("VORNAME").getDataType().setCellEditor(
					new JVxLinkedCellEditor(
							 new ReferenceDefinition(new String[] {"VORNAME"}, personenLookup, new String[] {"VORNAME"})));
			
			
			
//Test:			
//			dataBook.getRowDefinition().removeColumnDefinition(dataBook.getRowDefinition().getColumnDefinition("ID"));
//			System.out.println(dataBook);

			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	/** Application start temporary inApplication. 
	 * @param pArgs Application arguments.
	 * TODO: the Factory should start the Application */
	public static void main(String[] pArgs)
	{
/*		try 
		{            
			// JDK 1.6 u10(beta) Test
//	    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    	javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } 
     	catch (Exception e)
        {
        	e.printStackTrace();
        }*/		
//		System.setProperty("sun.awt.noerasebackground", "true");
//		Toolkit.getDefaultToolkit().setDynamicLayout(true);

     	SwingApplication.main(new String[] {"research.UITest"});
	}

	/**
	 * Userdefined Navigation Control.
	 *  
	 * @author mhandsteiner
	 */
	public static class Navigation extends UIPanel
	{
		/** model to navigate. */
		private IDataBook dataBook = null;
		
		/** Components. */
		private UIButton		bInsert			= new UIButton();
		private UIButton		bDelete			= new UIButton();
		private UIButton		bStore			= new UIButton();
		
		/** 
		 * Public Application Constructor. 
		 */
		public Navigation()
		{
			bInsert.setText("Insert");
			bInsert.eventAction().addListener(this, "insert");
	
			bDelete.setText("Delete");
			bDelete.eventAction().addListener(this, "delete");
	
			bStore.setText("Store");
			bStore.eventAction().addListener(this, "store");
	
			add(bInsert, null);
			add(bDelete, null);
			add(bStore, null);
		}
		
	    /**
	     * Action for Button.
	     * 
		 * @param pEvent the Event.
		 * @throws Exception if something is missing
		 */
		public void insert(UIActionEvent pEvent) throws Exception
		{
			dataBook.insert(false);
		}
		
	    /**
	     * Action for Button.
	     * 
		 * @param pEvent the Event.
		 * @throws Exception if something is missing
		 */
		public void delete(UIActionEvent pEvent) throws Exception
		{
			dataBook.delete();
		}
		
	    /**
	     * Action for Button.
	     * 
		 * @param pEvent the Event.
		 * @throws Exception if something is missing
		 */
		public void store(UIActionEvent pEvent) throws Exception
		{
			dataBook.getDataSource().saveAllDataBooks();
		}
		
		/** 
		 * Gets the DataBook. 
		 * 
		 * @return  the DataBook.
		 */
		public IDataBook getDataBook()
		{
			return dataBook;
		}
		
		/** 
		 * Sets the DataBook. 
		 * 
		 * @param pDataBook  the DataBook.
		 */
		public void setDataBook(IDataBook pDataBook)
		{
			dataBook = pDataBook;
		}
		
	}	// Navigation
	
}	// UITest

