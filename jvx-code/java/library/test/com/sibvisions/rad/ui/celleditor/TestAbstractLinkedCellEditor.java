/*
 * Copyright 2015 SIB Visions GmbH
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
 * 01.12.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.model.mem.MemDataBook;

/**
 * Tests the functionality provided by {@link AbstractLinkedCellEditor}.
 * 
 * @author Robert Zenz
 */
public class TestAbstractLinkedCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the
	 * {@link AbstractLinkedCellEditor#getDisplayValue(IDataRow, String)}
	 * function.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void testGetDisplayValue() throws ModelException
	{
		IDataBook baseDataBook = createBaseDataBook();
		IDataBook refDataBook = createRefDataBook();
		
		ReferenceDefinition refDef = createReferenceDefinition(refDataBook);
		
		baseDataBook.setValue("BASE", null);
		
		TestingLinkedCellEditor cellEditor = new TestingLinkedCellEditor();
		
		// Testing of the "not configured" cell editor.
		Assert.assertNull(cellEditor.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", "C");
		Assert.assertEquals("C", cellEditor.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", null);
		
		cellEditor.setDisplayReferencedColumnName("DISPLAY_VALUE");
		
		Assert.assertNull(cellEditor.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", "C");
		Assert.assertEquals("C", cellEditor.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", null);
		
		cellEditor.setLinkReference(refDef);
		
		// Testing of the cell editor begins here.
		Assert.assertNull(cellEditor.getDisplayValue(baseDataBook, "BASE"));
		
		baseDataBook.setValue("BASE", "C");
		
		Assert.assertEquals("Contract", cellEditor.getDisplayValue(baseDataBook, "BASE"));
		
		baseDataBook.setValue("BASE", "N");
		
		Assert.assertEquals("N", cellEditor.getDisplayValue(baseDataBook, "BASE"));
		
		// Now let's test us with a completely new one.
		TestingLinkedCellEditor cellEditorNew = new TestingLinkedCellEditor();
		cellEditorNew.setDisplayReferencedColumnName("DISPLAY_VALUE");
		cellEditorNew.setLinkReference(refDef);
		
		baseDataBook.setValue("BASE", "C");
		
		Assert.assertEquals("Contract", cellEditorNew.getDisplayValue(baseDataBook, "BASE"));
		
		// And now with pure String values.
		baseDataBook.setValue("BASE", "A");
		Assert.assertEquals("Automat", cellEditorNew.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", "D");
		Assert.assertEquals("Daystrom", cellEditorNew.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", "N");
		Assert.assertEquals("N", cellEditorNew.getDisplayValue(baseDataBook, "BASE"));
		baseDataBook.setValue("BASE", (String)null);
		Assert.assertEquals(null, cellEditorNew.getDisplayValue(baseDataBook, "BASE"));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an IDataBook which can be used as base for a reference. By
	 * default it only contains one column with one value: "BASE" and "C".
	 * 
	 * @return an IDataBook which can be used as base for a reference.
	 * @throws ModelException if the creation fails.
	 */
	protected static IDataBook createBaseDataBook() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("BASE"));
		
		IDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("BASE");
		
		dataBook.open();
		
		dataBook.insert(false);
		dataBook.setValues(new String[] { "BASE" }, new Object[] { "C" });
		dataBook.saveAllRows();
		
		return dataBook;
	}
	
	/**
	 * Creates an IDataBook which can be used as "referred to" databook for a
	 * reference.
	 * 
	 * @return an IDataBook which can be used as "referred to" databook for a
	 *         reference.
	 * @throws ModelException if the creation fails.
	 */
	protected static IDataBook createRefDataBook() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("REF"));
		rowDef.addColumnDefinition(new ColumnDefinition("DISPLAY_VALUE"));
		
		IDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("Ref");
		
		dataBook.open();
		
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "A", "Automat" });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "B", "Bom" });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "C", "Contract" });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "D", "Daystrom" });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "E", "AAAAAAAQAAA" });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "N", null });
		dataBook.insert(false);
		dataBook.setValues(null, new Object[] { "O", "Oppression" });
		dataBook.saveAllRows();
		
		return dataBook;
	}
	
	/**
	 * Creates a ReferenceDefinition between a column named "BASE" and "REF".
	 * 
	 * @param refDataBook the "referred to" databook.
	 * @return a ReferenceDefinition between two columns.
	 */
	protected static ReferenceDefinition createReferenceDefinition(IDataBook refDataBook)
	{
		ReferenceDefinition refDef = new ReferenceDefinition();
		refDef.setColumnNames(new String[] { "BASE" });
		refDef.setReferencedColumnNames(new String[] { "REF" });
		refDef.setReferencedDataBook(refDataBook);
		
		return refDef;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A {@link AbstractLinkedCellEditor} extension used for testing.
	 * 
	 * @author Robert Zenz
	 */
	private static final class TestingLinkedCellEditor extends AbstractLinkedCellEditor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link TestingLinkedCellEditor}.
		 */
		public TestingLinkedCellEditor()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			return null;
		}
		
	}	// TestingLinkedCellEditor
	
}	// TestAbstractLinkedCellEditor
