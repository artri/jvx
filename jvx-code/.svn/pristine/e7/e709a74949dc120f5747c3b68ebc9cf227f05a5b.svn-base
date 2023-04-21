/*
 * Copyright 2011 SIB Visions GmbH
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
 * 06.03.2011 - [JR] - creation
 * 02.04.2014 - [RZ] - #993 - added testAllowedValuesFallbackToLinkedCellEditor and testTicket993
 * 03.04.2014 - [RZ] - #998 - added testTicket998 and testTicket998FallbackToNonNullCellEditor
 * 04.09.2014 - [RZ] - Extended test of default cell editors
 */
package com.sibvisions.rad.model.mem;

import java.awt.Rectangle;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIRectangle;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.genui.celleditor.UICheckBoxCellEditor;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.ui.ICellEditor;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests {@link MemDataBook} with UI features.
 * 
 * @author René Jahn
 */
public class TestMemDataBook
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests if the allowed values from the {@link ColumnDefinition} are used.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testPerformance() throws ModelException
	{
		UIFactoryManager.setFactoryInstance(new SwingFactory());
		UIRectangle rect = new UIRectangle(1, 2, 3, 4);
		Rectangle natRect = (Rectangle)rect.getResource();

		int result = 0;
		long start = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			result += rect.getX() + rect.getY() + rect.getHeight() + rect.getWidth();
		}
		System.out.println("Duration 1: " + (System.nanoTime() - start) / 1000);

		start = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			result += natRect.x + natRect.y + natRect.height + natRect.width;
		}
		System.out.println("Duration 2: " + (System.nanoTime() - start) / 1000);

		start = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			result += rect.getX() + rect.getY() + rect.getHeight() + rect.getWidth();
		}
		System.out.println("Duration 3: " + (System.nanoTime() - start) / 1000);

		start = System.nanoTime();
		for (int i = 0; i < 10000000; i++)
		{
			result += natRect.x + natRect.y + natRect.height + natRect.width;
		}
		System.out.println("Duration 4: " + (System.nanoTime() - start) / 1000);

		System.out.println(result);
	}
	
	/**
	 * Tests if the allowed values from the {@link ColumnDefinition} are used.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testAllowedValues() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		UIChoiceCellEditor cced = getAllowedValuesChoiceCellEditor();
		
		UIChoiceCellEditor.addDefaultChoiceCellEditor(cced);
		
		RowDefinition rowdef = new RowDefinition();
		
		ColumnDefinition coldef = new ColumnDefinition("ALLOWED");
		coldef.setAllowedValues(new String[] {"Y", "N"});
		
		rowdef.addColumnDefinition(coldef);
		rowdef.addColumnDefinition(new ColumnDefinition("EMPTY"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("allowed");
		mdb.open();

		Assert.assertSame(cced, mdb.getRowDefinition().getColumnDefinition("ALLOWED").getDataType().getCellEditor());
		Assert.assertNull(mdb.getRowDefinition().getColumnDefinition("EMPTY").getDataType().getCellEditor());
	}
	
	/**
	 * Tests if the {@link MemDataBook} falls back to the {@link UIEnumCellEditor}
	 * if it encounters a {@link ColumnDefinition} with allowed values but
	 * no default editor.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testAllowedValuesFallbackToEnumCellEditor() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		UICellEditor.removeAllDefaultCellEditors();
		
		RowDefinition rowdef = new RowDefinition();
		
		ColumnDefinition coldef = new ColumnDefinition("ALLOWED");
		coldef.setAllowedValues(new String[] {"Y", "N"});
		
		rowdef.addColumnDefinition(coldef);
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("allowed");
		mdb.open();
		
		ICellEditor cellEditor = mdb.getRowDefinition().getColumnDefinition("ALLOWED").getDataType().getCellEditor();
		
		Assert.assertNotNull(cellEditor);
		Assert.assertTrue(cellEditor.getClass().getSimpleName() + " is not a UIEnumCellEditor.", cellEditor instanceof UIEnumCellEditor);
	}
	
	/**
	 * Test for ticket #993.
	 * Added a pool of default {@link ICellEditor}s to {@UICellEditor} which
	 * can be used to set preferred editors for allowed values. 
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket993() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		UICellEditor.removeAllDefaultCellEditors();
				
		UIChoiceCellEditor cced = getAllowedValuesChoiceCellEditor();
		UIChoiceCellEditor.addDefaultChoiceCellEditor(cced);
		
		UICheckBoxCellEditor cbced = new UICheckBoxCellEditor("T", "F");
		UICheckBoxCellEditor.addDefaultCheckBoxCellEditor(cbced);
		
		Assert.assertEquals(2, UICellEditor.getDefaultCellEditors(null).length);
		
		UICheckBoxCellEditor.addDefaultCheckBoxCellEditor(cbced);
		UICheckBoxCellEditor.addDefaultCheckBoxCellEditor(cbced);
		
		Assert.assertEquals(2, UICellEditor.getDefaultCellEditors(null).length);
		
		RowDefinition rowdef = new RowDefinition();
		
		ColumnDefinition coldef = new ColumnDefinition("ALLOWED");
		coldef.setAllowedValues(new Object[] {"Y", "N"});
		
		ColumnDefinition coldefCheck = new ColumnDefinition("ALLOWED_CHECKBOX");
		coldefCheck.setAllowedValues(new Object[] {"T", "F"});
		coldefCheck.setNullable(false);
		
		rowdef.addColumnDefinition(coldef);
		rowdef.addColumnDefinition(coldefCheck);
		rowdef.addColumnDefinition(new ColumnDefinition("EMPTY"));
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("allowed");
		mdb.open();
		
		Assert.assertSame(cced, mdb.getRowDefinition().getColumnDefinition("ALLOWED").getDataType().getCellEditor());
		Assert.assertSame(cbced, mdb.getRowDefinition().getColumnDefinition("ALLOWED_CHECKBOX").getDataType().getCellEditor());
		Assert.assertNull(mdb.getRowDefinition().getColumnDefinition("EMPTY").getDataType().getCellEditor());
		
		Assert.assertEquals(1, UICellEditor.getDefaultCellEditors(UICheckBoxCellEditor.class).length);
		
		UICellEditor.removeAllDefaultCellEditors(ICellEditor.class);
	}
	
	/**
	 * Test for ticket #998.
	 * Added default {@link ICellEditor}s which allow to set null values if
	 * the {@ColumnDefinition} says that the column is nullable.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket998() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		UICellEditor.removeAllDefaultCellEditors();
		
		UIChoiceCellEditor cced = getAllowedValuesChoiceCellEditor();
		cced.setAllowedValues(new Object[] {"Y", "N"});
		
		UIChoiceCellEditor ccedNull = getAllowedValuesChoiceCellEditor();
		
		UIChoiceCellEditor.addDefaultChoiceCellEditor(cced);
		UIChoiceCellEditor.addDefaultChoiceCellEditor(ccedNull);
				
		RowDefinition rowdef = new RowDefinition();
		
		ColumnDefinition coldef = new ColumnDefinition("ALLOWED");
		coldef.setAllowedValues(new Object[] {"Y", "N"});
		coldef.setNullable(false);
		
		ColumnDefinition coldefNull = new ColumnDefinition("NULLABLE");
		coldefNull.setAllowedValues(new Object[] {"Y", "N"});
		coldefNull.setNullable(true);
		
		rowdef.addColumnDefinition(coldef);
		rowdef.addColumnDefinition(coldefNull);
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("allowed");
		mdb.open();
		
		Assert.assertSame(cced, mdb.getRowDefinition().getColumnDefinition("ALLOWED").getDataType().getCellEditor());
		Assert.assertSame(ccedNull, mdb.getRowDefinition().getColumnDefinition("NULLABLE").getDataType().getCellEditor());
	}
	
	/**
	 * Test for ticket #998 fallback to nun-null cell editor.
	 * If there is no default cell editor with null available {@link MemDataBook}
	 * should fallback to a cell editor without a null value.
	 * 
	 * @throws ModelException if the test fails
	 */
	@Test
	public void testTicket998FallbackToNonNullCellEditor() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		UICellEditor.removeAllDefaultCellEditors();
		
		UIChoiceCellEditor cced = getAllowedValuesChoiceCellEditor();
		cced.setAllowedValues(new Object[] {"Y", "N"});
		
		UIChoiceCellEditor.addDefaultChoiceCellEditor(cced);
				
		RowDefinition rowdef = new RowDefinition();
		
		ColumnDefinition coldef = new ColumnDefinition("ALLOWED");
		coldef.setAllowedValues(new Object[] {"Y", "N"});
		coldef.setNullable(true);
		
		rowdef.addColumnDefinition(coldef);
		
		MemDataBook mdb = new MemDataBook(rowdef);
		mdb.setName("allowed");
		mdb.open();
		
		Assert.assertSame(cced, mdb.getRowDefinition().getColumnDefinition("ALLOWED").getDataType().getCellEditor());
	}
	
	/**
	 * Creates a {@link UIChoiceCellEditor} with the default options.
	 * 
	 * @return a new {@link UIChoiceCellEditor} with the default options
	 */
	private static UIChoiceCellEditor getAllowedValuesChoiceCellEditor()
	{
		return new UIChoiceCellEditor(new Object [] {"Y", "N", null},
             						  new String [] {UIImage.CHECK_YES_SMALL,
										             UIImage.CHECK_SMALL},
                                                     UIImage.CHECK_SMALL);
	}
	
}	// TestMemDataBook
