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
 * 01.10.2008 - [RH] - creation
 * 07.04.2009 - [RH] - interface review - Test cases adapted
 */
package javax.rad.model;

import javax.rad.genui.control.UITable;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IDimension;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ILinkedCellEditor;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * Tests all Functions of {@link RowDefinition} .<br>
 * 
 * @see javax.rad.model.RowDefinition
 * @author Roland Hörmann
 */
public class TestRowDefinition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test some base functions in the RowDefinition.
	 * 
	 * @throws Exception
	 *             if not all RowDefinition methods work correctly
	 */	
	@Test
	public void testBaseFunctions() throws Exception
	{
		// construct a RowDefinition
		RowDefinition rdRowDefinition = new RowDefinition();
		
		// construct some ColumnDefinitions
		ColumnDefinition cdId   = new ColumnDefinition("id");
		ColumnDefinition cdName = new ColumnDefinition("name");
		
		rdRowDefinition.addColumnDefinition(cdId);		
		rdRowDefinition.addColumnDefinition(cdName);

		// search and access the CellDefintions
		Assert.assertEquals(cdId.getName(), rdRowDefinition.getColumnDefinition("id").getName());
		Assert.assertEquals(1, rdRowDefinition.getColumnDefinitionIndex("name"));
		Assert.assertEquals(2, rdRowDefinition.getColumnCount());
		System.out.println(rdRowDefinition);
		
		try
		{
			rdRowDefinition.getColumnDefinition("nameXXX");
			
			// no Exception means it get a correct ColumnDefinition, but it shouldn't exists
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			// OK
		}

		Assert.assertTrue(rdRowDefinition.getColumnNames().length > 0);
		
		// check PrimaryKey
		rdRowDefinition.setPrimaryKeyColumnNames(new String[] { cdId.getName() });
		Assert.assertEquals(cdId.getName(),
				rdRowDefinition.getPrimaryKeyColumnNames()[0]);
				
		// check UniqueKey
		/* TODO [RH] DBRowDefinition Test
		rdRowDefinition.addUniqueKeyCells("UK1", new ArrayList<ColumnDefinition>(),
				new ArrayList<Boolean>());
		System.out.println(rdRowDefinition);		
		
		// check ForeignKey
		rdRowDefinition.addForeignKeyCells(new ArrayList<ColumnDefinition>(),
				"MASTERTABLE", new ArrayList<ColumnDefinition>());
		System.out.println(rdRowDefinition);		
*/
		// clone RowDefinition
		IRowDefinition rdRowDefinitionClone = rdRowDefinition.createRowDefinition(new String[] {"id"});
		Assert.assertTrue(rdRowDefinitionClone.getColumnDefinition("id") != null);
		try
		{
			rdRowDefinitionClone.getColumnDefinition("name");
			
			// no Exception means it get a correct ColumnDefinition, but it shouldn't exists
			Assert.assertTrue(false);
		}
		catch (ModelException modelException)
		{
			// OK
		}
		System.out.println(rdRowDefinitionClone);	

		rdRowDefinitionClone = rdRowDefinition.createRowDefinition(null);
		Assert.assertTrue(rdRowDefinitionClone.getColumnDefinition("id") != null);
		Assert.assertTrue(rdRowDefinitionClone.getColumnDefinition("name") != null);
		System.out.println(rdRowDefinitionClone);	
		/*
		rdRowDefinition.removeForeignKey("MASTERTABLE");
		Assert.assertTrue(rdRowDefinition.getAllForeignKeysCells().isEmpty());

		rdRowDefinition.removeUniqueKey("UK1");
		Assert.assertTrue(rdRowDefinition.getAllUniqueKeys().isEmpty());*/
	}
	
	/**
	 * Tests whether {@link RowDefinition#isMasterLinkColumn(String)} works.
	 * 
	 * @throws ModelException if test fails
	 */
	@Test
	public void testIsMasterLinkColumnWithoutCellEditor() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();
		rowdef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("CLASSNAME", new StringDataType()));

		//first check!
		rowdef.getColumnView(null).setColumnNames("NAME");
		
		
		MemDataBook mdbMaster = new MemDataBook(rowdef);
		mdbMaster.setName("master");
		mdbMaster.open();
		
		MemDataBook mdbDetail = new MemDataBook();
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
		mdbDetail.setName("detail");
		mdbDetail.open();
		
		Assert.assertFalse("ID should not be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("ID"));
		Assert.assertTrue("MASTER_ID should be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("MASTER_ID"));
	}
	
	/**
	 * Tests whether {@link RowDefinition#isMasterLinkColumn(String)} works.
	 * 
	 * @throws ModelException if test fails
	 */
	@Test
	public void testIsMasterLinkColumnWithCellEditor() throws ModelException
	{
		RowDefinition rowdef = new RowDefinition();
		rowdef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		rowdef.addColumnDefinition(new ColumnDefinition("CLASSNAME", new StringDataType()));

		//first check!
		rowdef.getColumnView(null).setColumnNames("NAME");
		
		
		MemDataBook mdbMaster = new MemDataBook(rowdef);
		mdbMaster.setName("master");
		mdbMaster.open();
		
		MemDataBook mdbDetail = new MemDataBook();
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_ID", new BigDecimalDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("MASTER_NAME", new StringDataType()));
		mdbDetail.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME", new StringDataType()));
		mdbDetail.setMasterReference(new ReferenceDefinition(new String[] {"MASTER_ID"}, mdbMaster, new String[] {"ID"}));
		mdbDetail.setName("detail");
		mdbDetail.open();
		
		ILinkedCellEditor edit = new ILinkedCellEditor()
		{
			private ReferenceDefinition ref;
			public void setVerticalAlignment(int pVerticalAlignment) 
			{ }
			public void setHorizontalAlignment(int pHorizontalAlignment) 
			{ }
			public int getVerticalAlignment() 
			{ return 0; }
			public int getHorizontalAlignment()	
			{ return 0; }
			public boolean isDirectCellEditor() 
			{ return false; }
			public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName) 
			{	return null; }
			public void setValidationEnabled(boolean pValidationEnabled) 
			{ }
			public void setTableReadonly(boolean pTableReadonly) 
			{ }
			public void setTableHeaderVisible(boolean pTableHeaderVisible) 
			{ }
			public void setSortByColumnName(boolean pSortByColumnName) 
			{ }
			public void setSearchColumnMapping(ColumnMapping pSearchColumnNames) 
			{ }
			public void setPopupSize(IDimension pPopupSize) 
			{ }
			public void setLinkReference(ReferenceDefinition pReferenceDefinition) 
			{ 
				ref = pReferenceDefinition;
			}
			public void setAdditionalCondition(ICondition pCondition) 
			{ }
			public boolean isValidationEnabled() 
			{ return false; }
			public boolean isTableReadonly() 
			{ return false; }
            public boolean isAutoTableHeaderVisibility() 
            { return false; }
			public boolean isTableHeaderVisible() 
			{ return false; }
			public boolean isSortByColumnName() 
			{ return false; }
			public ColumnMapping getSearchColumnMapping() 
			{ return null; }
			public IDimension getPopupSize() 
			{ return null; }
			public ReferenceDefinition getLinkReference()
			{
				return ref;
			}
			public ICondition getAdditionalCondition()
			{ return null; }
			public boolean isAutoOpenPopup()
			{ return false; }
			public void setAutoOpenPopup(boolean pAutoOpenPopup)
			{ }
			public int getPreferredEditorMode()
			{ return 0; }
			public void setPreferredEditorMode(int pPreferredEditorMode)
			{ }
			public boolean isSearchTextAnywhere()
			{ return false; }
			public void setSearchTextAnywhere(boolean pSearchTextAnywhere)
			{ }
			public boolean isSearchInAllTableColumns()
			{ return false; }
			public void setSearchInAllTableColumns(boolean pSearchInAllTableColumns)
			{ }
		    public ColumnView getColumnView()
		    { return null; }
		    public void setColumnView(ColumnView pColumnView)
		    { }
			public String getDisplayReferencedColumnName()
			{ return null; }
			public void setDisplayReferencedColumnName(String pDisplayReferencedColumnName)
			{ }
			public String getDisplayConcatMask()
			{ return null; }
			public void setDisplayConcatMask(String pDisplayConcatMask)
			{ }
			public String[] getDoNotClearColumnNames()
			{ return null; }
			public void setDoNotClearColumnNames(String... pDoNotClearColumnNames)
			{ }
			public void setStyle(Style pStyle)
			{ }
			public Style getStyle()
			{ return null; }
		};
		
		edit.setLinkReference(new ReferenceDefinition(new String[] {"MASTER_ID", "MASTER_NAME"}, 
				                                      mdbMaster, 
				                                      new String[] {"ID", "NAME"}));
		mdbDetail.getRowDefinition().getColumnDefinition("MASTER_NAME").getDataType().setCellEditor(edit);
		
		Assert.assertFalse("ID should not be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("ID"));
		Assert.assertTrue("MASTER_ID should be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("MASTER_ID"));
		Assert.assertTrue("MASTER_NAME should be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("MASTER_NAME"));
		Assert.assertFalse("NAME should not be a master link column!", ((RowDefinition)mdbDetail.getRowDefinition()).isMasterLinkColumn("NAME"));
	}
	
	/**
	 * Tests setting of ColumnViews.
	 * 
	 * @throws ModelException if test fails
	 */
	@Test
	public void testColumnView() throws ModelException
	{
		RowDefinition rowDefinition = new RowDefinition();
		rowDefinition.addColumnDefinition(new ColumnDefinition("NAME1"));
		rowDefinition.addColumnDefinition(new ColumnDefinition("NAME2"));
		rowDefinition.addColumnDefinition(new ColumnDefinition("NAME3"));
		
		ColumnView name1 = new ColumnView("NAME1");
		ColumnView name2 = new ColumnView("NAME2");
		ColumnView name3 = new ColumnView("NAME3");
		
		rowDefinition.setColumnView(ITableControl.class, name1);
		
		rowDefinition.setColumnView(IMobilTableControl.class, name2);

		rowDefinition.setColumnView(UITable.class, name3);
		
		Assert.assertArrayEquals(new Class[] {IMobilTableControl.class, UITable.class, ITableControl.class}, rowDefinition.getColumnViewClasses());
		
		Assert.assertEquals(name1, rowDefinition.getColumnView(ITableControl.class));
		Assert.assertEquals(name2, rowDefinition.getColumnView(IMobilTableControl.class));
		Assert.assertEquals(name3, rowDefinition.getColumnView(UITable.class));
		
		rowDefinition.setColumnView(UITable.class, null);
		rowDefinition.setColumnView(IMobilTableControl.class, null);
		rowDefinition.setColumnView(ITableControl.class, null);
		
		Assert.assertArrayEquals(new Class[] {}, rowDefinition.getColumnViewClasses());
		
		rowDefinition.setColumnView(IMobilTableControl.class, name2);

		rowDefinition.setColumnView(ITableControl.class, name1);
		
		rowDefinition.setColumnView(UITable.class, name3);
		
		Assert.assertArrayEquals(new Class[] {IMobilTableControl.class, UITable.class, ITableControl.class}, rowDefinition.getColumnViewClasses());
		
		Assert.assertEquals(name1, rowDefinition.getColumnView(ITableControl.class));
		Assert.assertEquals(name2, rowDefinition.getColumnView(IMobilTableControl.class));
		Assert.assertEquals(name3, rowDefinition.getColumnView(UITable.class));
	}

	/** 
	 * for testing derived Controls.
	 */
	public static interface IMobilTableControl extends ITableControl
	{
		
	}
	
} 	// RowDefinition
