/*
 * Copyright 2014 SIB Visions GmbH
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
 * 22.04.2014 - [RZ] - #1014: creation
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataRowListener;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.control.IEditor;
import javax.swing.JFrame;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests {@link UILinkedCellEditor}.
 * 
 * @author Robert Zenz
 */
public final class UILinkedCellEditorTest
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Should not be called.
	 */
	private UILinkedCellEditorTest()
	{
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens a simple frame which allows testing of the UILinkedCellEditor by hand.
	 * 
	 * @param pArgs Application arguments.
	 * @throws Throwable if start fails.
	 */
	public static void main(String[] pArgs) throws Throwable
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		IDataBook baseDataBook = createBaseDataBook();

		baseDataBook.eventValuesChanged().addListener(new IDataRowListener()
		{
			public void valuesChanged(DataRowEvent pDataRowEvent) throws ModelException
			{
				IDataRow originalRow = pDataRowEvent.getOriginalDataRow();
				IDataRow changedRow = pDataRowEvent.getChangedDataRow();
				
				for (String columnName : pDataRowEvent.getChangedColumnNames())
				{
					System.out.format("Value changed: %s: %s -> %s\n", columnName, originalRow.getValueAsString(columnName), changedRow.getValue(columnName));
				}
			}
			
		});
		
		IDataBook refDataBook = createRefDataBook();

		ReferenceDefinition refDef = createReferenceDefinition(refDataBook);

		UILinkedCellEditor cellEditor = new UILinkedCellEditor(refDef);
		cellEditor.setDisplayReferencedColumnName("DisplayValue");
		
		baseDataBook.getRowDefinition().getColumnDefinition("Base").getDataType().setCellEditor(cellEditor);
		
		baseDataBook.setSelectedRow(0);
		
		IEditor editor = new UIEditor(baseDataBook, "Base");
				
		IFrame frame = new UIFrame();
		frame.setLayout(new UIBorderLayout());
		frame.add(editor, UIBorderLayout.CENTER);
		frame.setTitle("UILinkedCellEditor");
		((JFrame)frame.getResource()).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.centerRelativeTo(null);

		frame.pack();
		frame.setVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an IDataBook which can be used as base for a reference.
	 * By default it only contains one column with one value: "Base" and "C".
	 * 
	 * @return an IDataBook which can be used as base for a reference.
	 * @throws ModelException if the creation fails.
	 */
	private static IDataBook createBaseDataBook() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("Base"));
		
		IDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("Base");

		dataBook.open();
		
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Base"}, new Object[] {"C"});
		dataBook.saveAllRows();
		
		return dataBook;
	}
	
	/**
	 * Creates an IDataBook which can be used as "referred to" databook for a reference.
	 * 
	 * @return an IDataBook which can be used as "referred to" databook for a reference.
	 * @throws ModelException if the creation fails.
	 */
	private static IDataBook createRefDataBook() throws ModelException
	{
		RowDefinition rowDef = new RowDefinition();
		rowDef.addColumnDefinition(new ColumnDefinition("Ref"));
		rowDef.addColumnDefinition(new ColumnDefinition("DisplayValue"));
		
		IDataBook dataBook = new MemDataBook(rowDef);
		dataBook.setName("Ref");
		
		dataBook.open();
		
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Ref", "DisplayValue"}, new Object[] {"A", "Automat"});
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Ref", "DisplayValue"}, new Object[] {"B", "Bom"});
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Ref", "DisplayValue"}, new Object[] {"C", "Contract"});
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Ref", "DisplayValue"}, new Object[] {"D", "Daystrom"});
		dataBook.insert(false);
		dataBook.setValues(new String[] {"Ref", "DisplayValue"}, new Object[] {"E", "AAAAAAAQAAA"});
		dataBook.saveAllRows();
		
		return dataBook;
	}
	
	/**
	 * Creates a ReferenceDefinition between a column named "Base" and "Ref".
	 * 
	 * @param refDataBook the "referred to" databook.
	 * @return a ReferenceDefinition between two columns.
	 */
	private static ReferenceDefinition createReferenceDefinition(IDataBook refDataBook)
	{
		ReferenceDefinition refDef = new ReferenceDefinition();
		refDef.setColumnNames(new String[] {"Base"});
		refDef.setReferencedColumnNames(new String[] {"Ref"});
		refDef.setReferencedDataBook(refDataBook);
		
		return refDef;
	}
	
}	// TestMemDataBook
