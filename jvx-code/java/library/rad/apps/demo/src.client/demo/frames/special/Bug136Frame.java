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
 * 02.11.2008 - [RH] - DataBook -> StorageDatabook changed
 * 02.06.2009 - [JR] - redesign
 */
package demo.frames.special;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.rad.genui.UIDimension;
import javax.rad.genui.UIInsets;
import javax.rad.genui.celleditor.UIDateCellEditor;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.genui.celleditor.UINumberCellEditor;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.IDataBook.SelectionMode;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.MasterConnection;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ImageUtil;

import demo.Demo;

/**
 * The <code>Bug136Frame</code> shows master/detail without refetching id of master,
 * after insert.
 * 
 * @author diapankarray
 */
public class Bug136Frame extends UIInternalFrame
{
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** application. */
	private Demo application;
	/** connection. */
	private AbstractConnection connection;
	
	/** the DataSource for fetching table data. */
	private RemoteDataSource dataSource = new RemoteDataSource();

	/** storage for contacts. */
	private RemoteDataBook rdbHdr = new RemoteDataBook();
	/** storage for contacts educations. */
	private RemoteDataBook rdbInventry = new RemoteDataBook();
	/** storage for contacts. */
	private RemoteDataBook rdbParty = new RemoteDataBook();
	/** storage for doctors. */
	private RemoteDataBook rdbDoctors = new RemoteDataBook();
	/** storace for stock. */
	private RemoteDataBook rdbStock = new RemoteDataBook();
	/** search row. */
	private DataRow drSearch = null;

	/** the frames layout. */
	private UIBorderLayout blThis = new UIBorderLayout();
	/** the split between contacts and details. */
	private UISplitPanel splitMain = new UISplitPanel();
	/** the navigator for contacts. */
	private UITable navContacts;
	/** the navigagor for showing educations. */
	private UITable navContEdu;

	/** the layout for details. */
	private UIFormLayout flDetails = new UIFormLayout();
	/** the details. */
	private UIPanel panDetails = new UIPanel();
	/** the details. */
	private UIGroupPanel gpanDedails = new UIGroupPanel();
	/** the details. */
	private UIGroupPanel gpanEducations = new UIGroupPanel();

	/** contacts layout. */
	private UIBorderLayout blContacts = new UIBorderLayout();
	/** contacts panel. */
	private UIPanel panContacts = new UIPanel();
	/** search panel. */
	private UIPanel panSearch = new UIPanel();

	/** Label. */
	private UILabel lblDocNo = new UILabel();
	/** Label. */
	private UILabel lblTrnDt = new UILabel();
	/** Label. */
	private UILabel lblPARTY = new UILabel();
	/** Label. */
	private UILabel lblDOCTOR = new UILabel();
	/** Label. */
	private UILabel lblTotTax = new UILabel();
	/** Label. */
	private UILabel lblAmtWoTax = new UILabel();
	/** Label. */
	private UILabel lblAMOUNT = new UILabel();
	/** Label. */
	private UILabel lblDISC = new UILabel();
	/** Label. */
	private UILabel lblSpDisc = new UILabel();
	/** Label. */
	private UILabel lblADJUSTMENT = new UILabel();
	/** Label. */
	private UILabel lblNetAmount = new UILabel();
	/** Label. */
	private UILabel lblAmountPaid = new UILabel();
	/** labelSuchen. */
	private UILabel lblSearch = new UILabel();

	/** Editor. */
	private UIEditor edtDocNo = new UIEditor();
	/** Editor. */
	private UIEditor edtTrnDt = new UIEditor();
	/** Editor. */
	private UIEditor edtPARTY = new UIEditor();
	/** Editor. */
	private UIEditor edtDOCTOR = new UIEditor();
	/** Editor. */
	private UIEditor edtTotTax = new UIEditor();
	/** Editor. */
	private UIEditor editAmtWoTax = new UIEditor();
	/** Editor. */
	private UIEditor edtAMOUNT = new UIEditor();
	/** Editor. */
	private UIEditor edtDISC = new UIEditor();
	/** Editor. */
	private UIEditor edtSpDisc = new UIEditor();
	/** Editor. */
	private UIEditor edtADJUSTMENT = new UIEditor();
	/** Editor. */
	private UIEditor edtNetAmount = new UIEditor();
	/** Editor. */
	private UIEditor edtAmountPaid = new UIEditor();
	/** editSuchen. */
	private UIEditor edtSearch = new UIEditor();
	/** Editor.  */
	private UIEditor edtHdrId = new UIEditor();
	/** contact image. */
	private UIEditor icoImage = new UIEditor();

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	* Constructs a new instance of <code>ContactsFrame</code>.
	*
	* @param pDemo the application.
	* @throws Throwable if the initialization throws an error
	*/
	public Bug136Frame(Demo pDemo) throws Throwable 
	{
		super(pDemo.getDesktopPane());
	
		application = pDemo;
		initializeModel();
		initializeUI();
	}

	/**
	* Initializes the model.
	*
	* @throws Throwable
	* if the initialization throws an error
	*/
	private void initializeModel() throws Throwable 
	{
		connection = ((MasterConnection) application.getConnection())
		.createSubConnection("demo.special.Bug136");
		connection.open();
		dataSource.setConnection(connection);
		dataSource.open();
	
		rdbHdr.setDataSource(dataSource);
	
		rdbHdr.setName("hdr");
		rdbHdr.open();
		ICondition filter = new LikeIgnoreCase("TRN_TYPE", "SALE");
		rdbHdr.setFilter(filter);
		rdbHdr.setSort(new SortDefinition(false, "ID"));
		rdbHdr.getRowDefinition().setColumnView(null,  new ColumnView("ID", "TRN_DT", "DOC_NO", "PARTY_DESC", "NET_AMOUNT"));
		// set same labels as in details panel
		// rdbHdr.getRowDefinition().getColumnDefinition("ACTI_ACADEMIC_TITLE").setLabel("Academic
		// title");
		// rdbHdr.getRowDefinition().getColumnDefinition("PARTY").setLabel("First
		// name");
		// rdbHdr.getRowDefinition().getColumnDefinition("DOCTOR").setLabel("Last
		// name");
		// rdbHdr.getRowDefinition().getColumnDefinition("AMOUNT").setLabel("AMOUNT");
		// rdbHdr.getRowDefinition().getColumnDefinition("ADJUSTMENT").setLabel("DoB");
		// rdbHdr.getRowDefinition().getColumnDefinition("SOCIALSECAMT_WO_TAX").setLabel("Social
		// security AMT_WO_TAX");
		// rdbHdr.getRowDefinition().getColumnDefinition("HEIN_HEALTH_INSURANCE").setLabel("Health
		// insurance");
	
		rdbInventry.setDataSource(dataSource);
		rdbInventry.setName("inventry");
		// rdbInventry.setMasterReference(new ReferenceDefinition(new String[] {
		// "COMP_CODE","ACCT_YEAR","TRN_TYPE","DOC_NO","TRN_DT","PARTY"}, rdbHdr, new String[] { "COMP_CODE","ACCT_YEAR","TRN_TYPE","DOC_NO","TRN_DT","PARTY"}));
		rdbInventry.setMasterReference(new ReferenceDefinition(new String[] {"HDR_ID"}, rdbHdr, new String[] { "ID"}));
	
		rdbInventry.open();
		rdbInventry.getRowDefinition().setColumnView(null, new ColumnView("DESCRIPTION", "QTY_S", "MRP_PER_PCS", "BATCH", "EXP", "TAX", "DISC", "AMT"));
	
		rdbParty.setDataSource(dataSource);
		rdbParty.setName("parties");
		ICondition partyfilter = new LikeIgnoreCase("TYPE", "CUSTOMER");
		rdbParty.setFilter(partyfilter);
		rdbParty.setSort(new SortDefinition("PARTY_DESC"));
		rdbParty.open();
		rdbParty.getRowDefinition().setColumnView(null, new ColumnView("PARTY_DESC", "ADDRESS"));
	
		rdbDoctors.setDataSource(dataSource);
		rdbDoctors.setName("doctors");
		rdbDoctors.setSort(new SortDefinition("DOCT_NAME"));
		rdbDoctors.open();
		rdbDoctors.getRowDefinition().setColumnView(null, new ColumnView("DOCT_NAME"));
	
		rdbStock.setDataSource(dataSource);
		rdbStock.setName("stock");
		// rdbStock.setSort(new SortDefinition("DESCRIPTION"));
		rdbStock.open();
		rdbStock.getRowDefinition().setColumnView(null, new ColumnView("DESCRIPTION", "BATCH", "EXP_DT", "MRP_PER_PCS", "STOCK"));
		// UIImageViewer imageViewer = new UIImageViewer();
		// imageViewer.setDefaultImageName(NO_IMAGE);
		rdbHdr.setSelectionMode(SelectionMode.CURRENT_ROW);
		// rdbHdr.getRowDefinition().getColumnDefinition("FILENAME").setReadOnly(true);
		// rdbHdr.getRowDefinition().getColumnDefinition("IMAGE").getDataType().setCellEditor(imageViewer);
	
		rdbHdr.getRowDefinition().getColumnDefinition("TRN_DT").getDataType()
		.setCellEditor(new UIDateCellEditor("dd.MM.yyyy"));
		rdbHdr.getRowDefinition().getColumnDefinition("TOT_TAX").getDataType()
		.setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("AMT_WO_TAX")
		.getDataType().setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("AMOUNT").getDataType()
		.setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("DISC").getDataType()
		.setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("SP_DISC").getDataType()
		.setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("ADJUSTMENT")
		.getDataType().setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("NET_AMOUNT")
		.getDataType().setCellEditor(new UINumberCellEditor(".00"));
		rdbHdr.getRowDefinition().getColumnDefinition("AMOUNT_PAID")
		.getDataType().setCellEditor(new UINumberCellEditor(".00"));
	
	
		installLinkedCellEditor(rdbHdr, new String[] { "PARTY", "PARTY_DESC" },
		rdbParty, new String[] { "PARTY", "PARTY_DESC" });
	
		installLinkedCellEditor(rdbHdr, new String[] { "DOCTOR", "DOCT_NAME" },
		rdbDoctors, new String[] { "ID", "DOCT_NAME" });
	
		installLinkedCellEditor(rdbInventry, new String[] { "ITEM_CODE",
		"DESCRIPTION", "BATCH", "EXP", "MRP_PER_PCS" }, rdbStock,
		new String[] { "ITEM_CODE", "DESCRIPTION", "BATCH", "EXP_DT",
		"MRP_PER_PCS" });
	
		rdbInventry.setSelectionMode(SelectionMode.CURRENT_ROW);
		// rdbInventry
		// .setWritebackIsolationLevel(WriteBackIsolationLevel.DATA_ROW);
		rdbInventry.getRowDefinition().getColumnDefinition("BATCH").setReadOnly(true);
	
	
		RowDefinition definition = new RowDefinition();
		definition.addColumnDefinition(new ColumnDefinition("SEARCH",
		new StringDataType()));
	
		drSearch = new DataRow(definition);
		drSearch.eventValuesChanged().addListener(this, "doFilter");
	}

	/**
	* Initializes the UI.
	*
	* @throws Throwable
	* if the initialization throws an error
	*/
	private void initializeUI() throws Throwable 
	{
		lblSearch.setText("Search");
		edtSearch.setDataRow(drSearch);
		edtSearch.setColumnName("SEARCH");
	
		UIFormLayout layoutSearch = new UIFormLayout();
	
		panSearch.setLayout(layoutSearch);
		panSearch.add(lblSearch, layoutSearch.getConstraints(0, 0));
		panSearch.add(edtSearch, layoutSearch.getConstraints(1, 0, -1, 0));
	
		navContacts = new UITable();
		navContacts.setDataBook(rdbHdr);
	//	navContacts.getTable().setAutoResize(false);
	
		panContacts.setLayout(blContacts);
		panContacts.add(panSearch, UIBorderLayout.NORTH);
		panContacts.add(navContacts, UIBorderLayout.CENTER);
	
		navContEdu = new UITable();
		navContEdu.setDataBook(rdbInventry);
		navContEdu.setPreferredSize(new UIDimension(150, 150));
	//	navContEdu.eventNewDetail().addListener(this, "doNewEducations");
	
		// icoImage.setPreferredSize(new UIDimension(75, 75));
		// icoImage.setDataRow(rdbHdr);
		// icoImage.setColumnName("IMAGE");
	
		lblDocNo.setText("Bill No");
		lblTrnDt.setText("Bill Date");
		lblPARTY.setText("Customer");
		lblDOCTOR.setText("Doctor");
		lblTotTax.setText("Tax");
		lblAmtWoTax.setText("Amt w/o Tax");
		lblAMOUNT.setText("Amount");
		lblDISC.setText("Discount");
		lblSpDisc.setText("SP_DISC");
		lblADJUSTMENT.setText("Adjustment");
		lblNetAmount.setText("Net Amt");
		lblAmountPaid.setText("Paid Amt");
		// lblFilename.setText("Filename");
	
		edtDocNo.setDataRow(rdbHdr);
		edtDocNo.setColumnName("DOC_NO");
		edtDocNo.setPreferredSize(new UIDimension(75, 21));
		edtTrnDt.setDataRow(rdbHdr);
		edtTrnDt.setColumnName("TRN_DT");
		edtTrnDt.setPreferredSize(new UIDimension(100, 21));
		edtPARTY.setDataRow(rdbHdr);
		edtPARTY.setColumnName("PARTY_DESC");
		edtPARTY.setPreferredSize(new UIDimension(200, 21));
		edtDOCTOR.setDataRow(rdbHdr);
		edtDOCTOR.setColumnName("DOCT_NAME");
		edtDOCTOR.setPreferredSize(new UIDimension(200, 21));
		edtTotTax.setDataRow(rdbHdr);
		edtTotTax.setColumnName("TOT_TAX");
		edtTotTax.setPreferredSize(new UIDimension(75, 21));
		editAmtWoTax.setDataRow(rdbHdr);
		editAmtWoTax.setColumnName("AMT_WO_TAX");
		editAmtWoTax.setPreferredSize(new UIDimension(75, 21));
		edtAMOUNT.setDataRow(rdbHdr);
		edtAMOUNT.setColumnName("AMOUNT");
		edtAMOUNT.setPreferredSize(new UIDimension(75, 21));
		edtDISC.setDataRow(rdbHdr);
		edtDISC.setColumnName("DISC");
		edtDISC.setPreferredSize(new UIDimension(75, 21));
		edtSpDisc.setDataRow(rdbHdr);
		edtSpDisc.setColumnName("SP_DISC");
		edtSpDisc.setPreferredSize(new UIDimension(75, 21));
		edtADJUSTMENT.setDataRow(rdbHdr);
		edtADJUSTMENT.setColumnName("ADJUSTMENT");
		edtADJUSTMENT.setPreferredSize(new UIDimension(75, 21));
		edtNetAmount.setDataRow(rdbHdr);
		edtNetAmount.setColumnName("AMT_WO_TAX");
		edtNetAmount.setPreferredSize(new UIDimension(75, 21));
		edtAmountPaid.setDataRow(rdbHdr);
		edtAmountPaid.setColumnName("AMOUNT_PAID");
		edtAmountPaid.setPreferredSize(new UIDimension(75, 21));
		edtHdrId.setDataRow(rdbHdr);
		edtHdrId.setColumnName("ID");
		edtHdrId.setPreferredSize(new UIDimension(75, 21));
		// edtFilename.setDataRow(rdbHdr);
		// edtFilename.setColumnName("FILENAME");
	
		// butLoadImage.setText("Upload");
		// butLoadImage.eventAction().addListener(this, "doUpload");
		// butLoadImage.setFocusable(false);
	
		flDetails.setMargins(new UIInsets(20, 20, 20, 20));
	
		gpanDedails.setText("Sale Bill");
	
		gpanDedails.setLayout(flDetails);
		//gpanDedails.add(icoImage, flDetails.getConstraints(0, 0, 1, 7));
		//gpanDedails.add(butLoadImage, flDetails.getConstraints(0, 8));
		//gpanDedails.add(edtFilename, flDetails.getConstraints(1, 8));
		//col,row
		flDetails.setHorizontalGap(15);
		gpanDedails.add(lblDocNo, flDetails.getConstraints(0, 0));
		gpanDedails.add(edtDocNo, flDetails.getConstraints(1, 0));
		gpanDedails.add(lblTrnDt, flDetails.getConstraints(0, 1));
		gpanDedails.add(edtTrnDt, flDetails.getConstraints(1, 1));
		gpanDedails.add(lblPARTY, flDetails.getConstraints(3, 0));
		gpanDedails.add(edtPARTY, flDetails.getConstraints(4, 0));
		// gpanDedails.add(lblPARTY, flDetails.getConstraints(3, 0));
		gpanDedails.add(edtHdrId, flDetails.getConstraints(5, 0));
		gpanDedails.add(lblDOCTOR, flDetails.getConstraints(3, 1));
		gpanDedails.add(edtDOCTOR, flDetails.getConstraints(4, 1));
	
		gpanDedails.add(lblNetAmount, flDetails.getConstraints(2, 4));
		gpanDedails.add(edtNetAmount, flDetails.getConstraints(3, 4));
		gpanDedails.add(lblADJUSTMENT, flDetails.getConstraints(4, 4));
		gpanDedails.add(edtADJUSTMENT, flDetails.getConstraints(5, 4));
	
		gpanDedails.add(lblAmountPaid, flDetails.getConstraints(2, 5));
		gpanDedails.add(edtAmountPaid, flDetails.getConstraints(3, 5));
	
		gpanDedails.add(lblTotTax, flDetails.getConstraints(2, 6));
		gpanDedails.add(edtTotTax, flDetails.getConstraints(3, 6));
		gpanDedails.add(lblAmtWoTax, flDetails.getConstraints(-2, 6));
		gpanDedails.add(editAmtWoTax, flDetails.getConstraints(-1, 6));
		gpanDedails.add(lblAMOUNT, flDetails.getConstraints(2, 7));
		gpanDedails.add(edtAMOUNT, flDetails.getConstraints(3, 7));
		gpanDedails.add(lblDISC, flDetails.getConstraints(4, 7));
		gpanDedails.add(edtDISC, flDetails.getConstraints(5, 7));
	
		UIFormLayout layoutSchulung = new UIFormLayout();
	
		gpanEducations.setText("Bill Detail");
		gpanEducations.setLayout(layoutSchulung);
		gpanEducations.add(navContEdu, layoutSchulung.getConstraints(0, 0, -1,
		-1));
	
		UIFormLayout layout = new UIFormLayout();
	
		panDetails.setLayout(layout);
		panDetails.add(gpanDedails, layout.getConstraints(0, 0, -1, 0));
		panDetails.add(gpanEducations, layout.getConstraints(0, 1, -1, -1));
	
		splitMain.setDividerPosition(250);
		splitMain.setDividerAlignment(UISplitPanel.DIVIDER_TOP_LEFT);
		splitMain.setFirstComponent(panContacts);
		splitMain.setSecondComponent(panDetails);
	
		setTitle("Sale Bill");
		setLayout(blThis);
		add(splitMain, UIBorderLayout.CENTER);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	* {@inheritDoc}
	*/
	public void save() throws ModelException 
	{
		dataSource.saveAllDataBooks();
	}

	/**
	* {@inheritDoc}
	*/
	public void reload() throws ModelException 
	{
		dataSource.reloadAllDataBooks();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	* Saves the image to the contact.
	*
	* @param pFileHandle
	* the file.
	* @throws Throwable
	* if an error occures.
	*/
	public void storeFile(IFileHandle pFileHandle) throws Throwable
	{
		String sFormat = FileUtil.getExtension(pFileHandle.getFileName().toLowerCase());

		if ("png".equals(sFormat) || "jpg".equals(sFormat) || "gif".equals(sFormat))
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			ImageUtil.createScaledImage(pFileHandle.getInputStream(), icoImage.getSize().getWidth(), icoImage.getSize().getHeight(), true, stream, sFormat);

			stream.close();

			rdbHdr.setValue("FILENAME", pFileHandle.getFileName());
			rdbHdr.setValue("IMAGE", stream.toByteArray());

			try
			{
				rdbHdr.saveSelectedRow();
			}
			catch (Exception pException)
			{
				// Silent Save of current row.
			}
		}
		else
		{
			throw new IOException("Image format '" + sFormat + "' not supported. Use 'png', 'jpg' or 'gif'!");
		}

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	* Starts the image upload.
	*
	* @throws Throwable
	* if an error occures.
	*/
	public void doUpload() throws Throwable 
	{
		if (rdbHdr.getSelectedRow() >= 0) 
		{
			application.getLauncher().getFileHandle(this, "storeFile");
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	* Searches the contacts with the search text.
	*
	* @throws ModelException
	* if the search fails
	*/
	public void doFilter() throws ModelException 
	{
		String suche = (String)drSearch.getValue("SEARCH");
		if (suche == null)
		{
			rdbHdr.setFilter(null);
		}
		else
		{
			ICondition filter = new LikeIgnoreCase("PARTY_DESC", suche + "*");
			rdbHdr.setFilter(filter);
		}
	}

	/**
	 * ?.
	 * 
	 * @param pDataBook ?
	 * @param pColumnNames ?
	 * @param pReferencedDataBook ?
	 * @param pReferenceColumnNames ?
	 * @throws Throwable ?
	 */
	private void installLinkedCellEditor(RemoteDataBook pDataBook, 
			                             String[] pColumnNames, 
			                             RemoteDataBook pReferencedDataBook, 
			                             String[] pReferenceColumnNames) throws Throwable 
	{
		ReferenceDefinition referenceDefinition = new ReferenceDefinition();
		referenceDefinition.setReferencedDataBook(pReferencedDataBook);
		referenceDefinition.setReferencedColumnNames(pReferenceColumnNames);
		referenceDefinition.setColumnNames(pColumnNames);
	
		UILinkedCellEditor linkedCellEditor = new UILinkedCellEditor();
		linkedCellEditor.setLinkReference(referenceDefinition);
		linkedCellEditor.setTableHeaderVisible(true);
		linkedCellEditor.setTableReadonly(true);
		linkedCellEditor.setValidationEnabled(true);
		linkedCellEditor.setPopupSize(new UIDimension(700, 150));
		pDataBook.getRowDefinition().getColumnDefinition(pColumnNames[1]).getDataType().setCellEditor(linkedCellEditor);
	}

}	// Bug136Frame
