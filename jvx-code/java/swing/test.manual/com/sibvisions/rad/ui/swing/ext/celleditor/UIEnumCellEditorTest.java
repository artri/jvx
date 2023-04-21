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
import javax.rad.genui.component.UIButton;
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
import javax.rad.ui.component.IButton;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.swing.JFrame;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests the {@link UIEnumCellEditor}.
 * 
 * @author Robert Zenz
 */
public final class UIEnumCellEditorTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Should not be called.
	 */
	private UIEnumCellEditorTest()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Opens a simple frame which allows testing of the UIEnumCellEditor by
	 * hand.
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
		
		final UIEnumCellEditor cellEditor = new UIEnumCellEditor();
		cellEditor.setAllowedValues(new String[] { "A", "B", "C", "D" });
		cellEditor.setDisplayValues(new String[] { "Alpha", "Bravo", "Charlie", "Delta" });
		
		baseDataBook.getRowDefinition().getColumnDefinition("BASE").getDataType().setCellEditor(cellEditor);
		
		baseDataBook.setSelectedRow(0);
		
		IEditor editor = new UIEditor(baseDataBook, "BASE");
		
		IButton button = new UIButton("Swap Display Values");
		button.eventAction().addListener(new IActionListener()
		{
			
			public void action(UIActionEvent pActionEvent)
			{
				if (cellEditor.getDisplayValues().length == 4)
				{
					cellEditor.setAllowedValues(new String[] { "A", "B", "C", "D", "E", "F", "G" });
					cellEditor.setDisplayValues(new String[] { "Less", "Values" });
				}
				else
				{
					cellEditor.setAllowedValues(new String[] { "A", "B", "C", "D" });
					cellEditor.setDisplayValues(new String[] { "Alpha", "Bravo", "Charlie", "Delta" });
				}
			}
		});
		
		IFrame frame = new UIFrame();
		frame.setLayout(new UIBorderLayout());
		frame.add(editor, IBorderLayout.CENTER);
		frame.add(button, IBorderLayout.SOUTH);
		frame.setTitle("UIEnumCellEditor");
		((JFrame) frame.getResource()).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.centerRelativeTo(null);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an IDataBook which can be used as base for a reference. By
	 * default it only contains one column with one value: "Base" and "C".
	 * 
	 * @return an IDataBook which can be used as base for a reference.
	 * @throws ModelException if the creation fails.
	 */
	private static IDataBook createBaseDataBook() throws ModelException
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
	
}	// TestUIEnumCellEditor
