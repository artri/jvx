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
 */
package com.sibvisions.apps.simpleapp;

import java.math.BigDecimal;
import java.util.Date;

import javax.rad.application.IContent;
import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.celleditor.UIDateCellEditor;
import javax.rad.genui.celleditor.UIImageViewer;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.control.UICellFormat;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.IContainer;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.event.IExceptionListener;

import com.sibvisions.apps.components.FormPanel;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.util.type.CommonUtil;

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

	/** the main/content panel. */
	private UIPanel panMain;
	
	/** the master table. */
	private UITable tblMaster;
	
	/** the detail table. */
	private UITable tblDetail;

	/** the text area to display errors. */
	private UITextArea taError;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Showcase</code>.
	 * 
	 * @param pLauncher the launcher
	 * @throws ModelException if the initialization failed
	 */
	public SimpleApplication(UILauncher pLauncher) throws ModelException
	{
		super(pLauncher);
		
		setName("Simple application");
		
		init();
	}

	/**
	 * Initializes the application.
	 * 
	 * @throws ModelException if the initialization failed
	 */
	@SuppressWarnings("deprecation")
	private void init() throws ModelException
	{
		ExceptionHandler.addExceptionListener(this);
		
		panMain = new UIPanel();
		panMain.setLayout(new UIBorderLayout());
		
		MemDataBook memOrte = new MemDataBook();
		memOrte.setName("orte");
		memOrte.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memOrte.getRowDefinition().addColumnDefinition(new ColumnDefinition("PLZ", new BigDecimalDataType()));
		memOrte.getRowDefinition().addColumnDefinition(new ColumnDefinition("ORT", new StringDataType()));
		memOrte.open();

		memOrte.insert(false);
		memOrte.setValues(new String[] {"ID", "PLZ", "ORT"}, new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(7083), "Purbach"});
		memOrte.insert(false);
		memOrte.setValues(new String[] {"ID", "PLZ", "ORT"}, new Object[] {BigDecimal.valueOf(2), BigDecimal.valueOf(1200), "Wien"});
		memOrte.saveAllRows();
		
		MemDataBook memData = new MemDataBook();
		memData.setName("memData");
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("ORT_ID", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("ORT", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("HAUSNUMMER", new BigDecimalDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("BAUSTART", new TimestampDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("BEANTRAGT", new StringDataType()));
		memData.getRowDefinition().addColumnDefinition(new ColumnDefinition("INFO", new StringDataType()));
		memData.open();
	
//		memData.getRowDefinition().getColumnDefinition("NAME").setReadOnly(true);
		
		MemDataBook memPersonen = new MemDataBook();
		memPersonen.setName("personen");
		memPersonen.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, memData, new String[] {"ID"}));
		memPersonen.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		memPersonen.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		memPersonen.getRowDefinition().addColumnDefinition(new ColumnDefinition("VORNAME", new StringDataType()));
		memPersonen.getRowDefinition().addColumnDefinition(new ColumnDefinition("NACHNAME", new StringDataType()));
		memPersonen.open();
		
		memData.insert(false);
		memData.setValues(new String[] {"ID", "ORT_ID", "ORT", "NAME", "HAUSNUMMER", "BAUSTART", "BEANTRAGT"}, 
				          new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "Purbach", "René", BigDecimal.valueOf(25), new Date(110, 01, 01, 10, 0, 0), "J"});

			memPersonen.insert(false);
			memPersonen.setValues(new String[] {"ID", "MASTER_ID", "VORNAME", "NACHNAME"}, new Object[] {BigDecimal.valueOf(1), BigDecimal.valueOf(1), "René", "Jahn"});
			memPersonen.insert(false);
			memPersonen.setValues(new String[] {"ID", "MASTER_ID", "VORNAME"}, new Object[] {BigDecimal.valueOf(2), BigDecimal.valueOf(1), "Daniel"});
			memPersonen.saveAllRows();

		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART"}, 
				         new Object[] {BigDecimal.valueOf(2), "Martin", BigDecimal.valueOf(12), new Date(109, 05, 01, 14, 0, 0)});

			memPersonen.insert(false);
			memPersonen.setValues(new String[] {"ID", "MASTER_ID", "VORNAME", "NACHNAME"}, new Object[] {BigDecimal.valueOf(3), BigDecimal.valueOf(2), "Martin", "Handsteiner"});
			memPersonen.saveAllRows();
		
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART", "BEANTRAGT"}, 
				          new Object[] {BigDecimal.valueOf(3), "Roland", BigDecimal.valueOf(23), new Date(107, 03, 05, 20, 0, 0), "J"});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART", "BEANTRAGT"}, 
				          new Object[] {BigDecimal.valueOf(4), "René", BigDecimal.valueOf(25), new Date(110, 01, 01, 10, 0, 0), "J"});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART"}, 
				         new Object[] {BigDecimal.valueOf(5), "Martin", BigDecimal.valueOf(12), new Date(109, 05, 01, 14, 0, 0)});
		memData.insert(false);
		memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART", "BEANTRAGT", "INFO"}, 
				          new Object[] {BigDecimal.valueOf(6), "Roland", BigDecimal.valueOf(23), new Date(107, 03, 05, 20, 0, 0), "J", UIImage.SAVE_SMALL});

//		for (int i = 7; i < 10000; i++)
//		{
//			memData.insert(false);
//			memData.setValues(new String[] {"ID", "NAME", "HAUSNUMMER", "BAUSTART", "BEANTRAGT", "INFO"}, 
//			                  new Object[] {BigDecimal.valueOf(i), "Name: " + i, BigDecimal.valueOf(i), null, "N", null});
//		}
		
		memData.saveAllRows();
		memData.setSelectedRow(0);
		
memData.eventBeforeUpdating().addListener(new IDataBookListener()
{
	public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
	{
		throw new ModelException("Not allowed (update)");
	}
});		

memData.eventBeforeInserted().addListener(new IDataBookListener()
{
	public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
	{
		throw new ModelException("Not allowed (insert)");
	}
});		
		
		UILinkedCellEditor linkOrte = new UILinkedCellEditor();
		linkOrte.setLinkReference(new ReferenceDefinition(new String[] {"ORT_ID", "ORT"}, memOrte, new String[] {"ID", "ORT"}));
		
		UIChoiceCellEditor choiceBeantragt = new UIChoiceCellEditor();
		choiceBeantragt.setAllowedValues(new String[] {"J", "N"});
		choiceBeantragt.setImageNames(new String[] {UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL});
		choiceBeantragt.setDefaultImageName(UIImage.CHECK_NO_SMALL);
		
		UIImageViewer imgv = new UIImageViewer();
		imgv.setDefaultImageName(UIImage.REGISTER_SMALL);
		
		memData.getRowDefinition().getColumnDefinition("ORT").getDataType().setCellEditor(linkOrte);
		memData.getRowDefinition().getColumnDefinition("BEANTRAGT").getDataType().setCellEditor(choiceBeantragt);
		memData.getRowDefinition().getColumnDefinition("INFO").getDataType().setCellEditor(imgv);
	
		UIDateCellEditor dateEdit = new UIDateCellEditor("dd.MM.yyyy");
		dateEdit.setHorizontalAlignment(UIDateCellEditor.ALIGN_CENTER);

		memData.getRowDefinition().getColumnDefinition("BAUSTART").getDataType().setCellEditor(dateEdit);
		
//		memData.eventBeforeUpdated().addListener(this, "doException");
//		memData.eventBeforeInserting().addListener(this, "doException");
//		memData.eventBeforeDeleting().addListener(this, "doException");
		
		tblMaster = new UITable();
		tblMaster.setDataBook(memData);
		tblMaster.setAutoResize(false);
		tblMaster.setCellFormatter(new ICellFormatter()
		{
			public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn)
			{
				if (pRow <= 2)
				{
					try
					{
						if ("1".equals(pDataRow.getValue("NAME")))
						{
							return new UICellFormat(UIColor.red, UIColor.blue, new UIFont("Courier", UIFont.BOLD, 14));	
						}
					}
					catch (Exception e)
					{
						ExceptionHandler.raise(e);
					}
					
					return new UICellFormat(new UIColor(232, 241, 255), UIColor.black, null);
				}
				else
				{
					return null;
				}
			}
		});
		
		tblDetail = new UITable();
		tblDetail.setDataBook(memPersonen);
		tblDetail.setPreferredSize(400, 400);
		
		taError = new UITextArea();
		
		FormPanel panDetails = new FormPanel();
		panDetails.setDataRow(memData);
		
		panDetails.getEditor("HAUSNUMMER").setEnabled(false);
		
		panMain.add(taError, UIBorderLayout.NORTH);
		panMain.add(panDetails, UIBorderLayout.WEST);
		panMain.add(tblMaster, UIBorderLayout.CENTER);
		panMain.add(tblDetail, UIBorderLayout.EAST);
		
		setLayout(new UIBorderLayout());
		add(panMain);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IContainer getContentPane()
	{
		return panMain;
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
		taError.setText(CommonUtil.dump(pThrowable, false));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Throws an {@link RuntimeException}.
	 * 
	 * @param pEvent not used
	 */
	public void doException(DataBookEvent pEvent)
	{
//		System.out.println(tblMain.getDataBook().getSelectedRow());
//		
//		pEvent.getChangedDataBook().setValue("ORT", "Ort");
		
		throw new RuntimeException("END");
	}
	
}	// SimpleApplication
