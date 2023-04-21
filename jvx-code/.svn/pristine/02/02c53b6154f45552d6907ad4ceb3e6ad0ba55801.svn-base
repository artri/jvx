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
 * 02.06.2014 - [RZ] - creation
 * 18.09.2014 - [RZ] - extended with a table and an EnumCellEditor
 */
package com.sibvisions.rad.model.mem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataRowListener;
import javax.rad.ui.component.IButton;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.swing.JFrame;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * A simple test GUI for MemDataBook.
 * 
 * @author Robert Zenz
 */
public final class MemDataBookTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not supposed to be instantiated.
	 */
	private MemDataBookTest()
	{
	}
	
	/**
	 * The main method.
	 * 
	 * @param args Not used at all.
	 */
	public static void main(String[] args)
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		IFrame frame = new UIFrame();
		
		try
		{
			final IDataBook dataBook = createDataBook();
			
			initTestcases(dataBook, frame);
			
			dataBook.insert(false);
			
			IButton insertButton = new UIButton("Insert");
			
			insertButton.eventAction().addListener(new IActionListener()
			{
				public void action(UIActionEvent pActionEvent)
				{
					try
					{
						dataBook.insert(false);
					}
					catch (ModelException e)
					{
						System.err.println(e);
					}
				}
			});
			
			frame.add(insertButton, IBorderLayout.NORTH);
			
			IButton saveButton = new UIButton("Save");
			
			saveButton.eventAction().addListener(new IActionListener()
			{
				public void action(UIActionEvent pActionEvent)
				{
					try
					{
						dataBook.saveSelectedRow();
					}
					catch (ModelException e)
					{
						System.err.println(e);
					}
				}
			});
			
			frame.add(saveButton, IBorderLayout.SOUTH);
		}
		catch (ModelException e)
		{
			System.err.println(e);
			return;
		}
		
		((JFrame) frame.getResource()).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("MemDataBookTest");
		
		frame.pack();
		frame.setVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Inits all testcases.
	 * 
	 * @param pDataBook the used databook.
	 * @param pFrame the frame.
	 * @throws ModelException if something goes wrong.
	 */
	private static void initTestcases(final IDataBook pDataBook, IFrame pFrame) throws ModelException
	{
		List<ITestcase> testcases = new ArrayList<ITestcase>();
		
		// Add testcases here.
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setName("SimpleStringColumn");
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("Simple String column:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "SimpleStringColumn"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setDataType(new BigDecimalDataType());
				columnDefinition.setName("SimpleNumberColumn");
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("Simple number column:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "SimpleNumberColumn"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setDataType(new TimestampDataType());
				columnDefinition.setName("SimpleDateColumn");
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("Simple date column:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "SimpleDateColumn"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setAllowedValues(new Object[] { "A", "B" });
				columnDefinition.setName("ChoiceCellEditorWithImagesNullable");
				columnDefinition.setNullable(true);
				columnDefinition.setReadOnly(false);
				
				UIChoiceCellEditor.addDefaultChoiceCellEditor(new UIChoiceCellEditor(
						new Object[] { "A", "B", null },
						new String[] { UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL, UIImage.CHECK_SMALL },
						UIImage.CHECK_SMALL));
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("ChoiceCellEditor with images and null:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "ChoiceCellEditorWithImagesNullable"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setAllowedValues(new Object[] { "C", "D" });
				columnDefinition.setName("ChoiceCellEditorWithImages");
				columnDefinition.setNullable(false);
				columnDefinition.setReadOnly(false);
				
				UIChoiceCellEditor.addDefaultChoiceCellEditor(new UIChoiceCellEditor(
						new Object[] { "C", "D" },
						new String[] { UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL },
						UIImage.CHECK_SMALL));
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("ChoiceCellEditor with images:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "ChoiceCellEditorWithImages"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setName("ALotOfChoices");
				columnDefinition.setNullable(false);
				columnDefinition.setReadOnly(false);
				
				columnDefinition.getDataType().setCellEditor(new UIChoiceCellEditor(
						new Object[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" },
						new String[] { UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL,
								UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL },
						UIImage.CHECK_SMALL));
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("A Lot of Choices:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "ALotOfChoices"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setAllowedValues(new Object[] { "E", "F" });
				columnDefinition.setName("ChoiceCellEditorNullable");
				columnDefinition.setNullable(true);
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("ChoiceCellEditor and null:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "ChoiceCellEditorNullable"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setAllowedValues(new Object[] { "G", "H" });
				columnDefinition.setName("ChoiceCellEditor");
				columnDefinition.setNullable(false);
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("ChoiceCellEditor:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "ChoiceCellEditor"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		testcases.add(new ITestcase()
		{
			public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException
			{
				ColumnDefinition columnDefinition = new ColumnDefinition();
				columnDefinition.setDataType(new BigDecimalDataType());
				columnDefinition.getDataType().setCellEditor(new UIEnumCellEditor(
						new Object[] { new BigDecimal(10), new BigDecimal(20), new BigDecimal(30) },
						new String[] { "10 - Incoming", "20 - Outgoing", "30 - Paused" }));
				columnDefinition.setName("EnumCellEditor");
				columnDefinition.setNullable(false);
				columnDefinition.setReadOnly(false);
				
				List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
				columnDefinitions.add(columnDefinition);
				
				return columnDefinitions;
			}
			
			public IPanel getPanel(IDataBook pDataBook) throws ModelException
			{
				IPanel panel = new UIPanel();
				
				IFormLayout formLayout = new UIFormLayout();
				panel.setLayout(formLayout);
				
				panel.add(new UILabel("EnumCellEditor:"), formLayout.getConstraints(0, 0));
				panel.add(new UIEditor(pDataBook, "EnumCellEditor"), formLayout.getConstraints(1, 0));
				
				return panel;
			}
		});
		
		RowDefinition rowDefinition = new RowDefinition();
		
		for (ITestcase testcase : testcases)
		{
			for (ColumnDefinition columnDefinition : testcase.getColumnDefinitions())
			{
				rowDefinition.addColumnDefinition(columnDefinition);
			}
		}
		
		pDataBook.setRowDefinition(rowDefinition);
		
		pDataBook.open();
		
		int row = 0;
		
		ISplitPanel splitPanel = new UISplitPanel(ISplitPanel.SPLIT_LEFT_RIGHT);
		
		splitPanel.setFirstComponent(new UITable(pDataBook));
		
		IPanel editorsPanel = new UIPanel();
		IFormLayout editorsFormLayout = new UIFormLayout();
		editorsPanel.setLayout(editorsFormLayout);
		
		for (ITestcase testcase : testcases)
		{
			editorsPanel.add(testcase.getPanel(pDataBook), editorsFormLayout.getConstraints(0, row));
			
			row++;
		}
		
		splitPanel.setSecondComponent(editorsPanel);
		
		pFrame.setLayout(new UIBorderLayout());
		
		pFrame.add(splitPanel, IBorderLayout.CENTER);
	}
	
	/**
	 * Creates a databook for testing.
	 * 
	 * @return a MemDataBook.
	 * @throws ModelException if the creation fails.
	 */
	private static IDataBook createDataBook() throws ModelException
	{
		IDataBook dataBook = new MemDataBook();
		dataBook.setName("Test");
		
		dataBook.eventValuesChanged().addListener(new IDataRowListener()
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
		
		return dataBook;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple test case which gets added to the test UI.
	 * 
	 * @author Robert Zenz
	 */
	private static interface ITestcase
	{
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets all the column definitions for this testcase.
		 * 
		 * @return all column definitions.
		 * @throws ModelException if the creation of the column definitions
		 *             fails.
		 */
		public Collection<ColumnDefinition> getColumnDefinitions() throws ModelException;
		
		/**
		 * Creates a panel with all needed components bound against the given
		 * databook.
		 * 
		 * @param pDataBook the databook to use.
		 * @return the panel with all needed components.
		 * @throws ModelException if the creation of the panel fails.
		 */
		public IPanel getPanel(IDataBook pDataBook) throws ModelException;
		
	}	// ITestcase
	
}	// MemDataBookTest
