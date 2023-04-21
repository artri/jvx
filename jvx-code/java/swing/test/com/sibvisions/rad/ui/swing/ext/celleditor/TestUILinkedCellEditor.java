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

import java.awt.Component;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.IControl;
import javax.rad.util.TranslationMap;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableCellRenderer;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.ui.celleditor.TestAbstractLinkedCellEditor;
import com.sibvisions.rad.ui.swing.ext.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.swing.ext.JVxComboBase;
import com.sibvisions.rad.ui.swing.ext.format.ICellFormatter;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * Tests {@link UILinkedCellEditor}.
 * 
 * @author Robert Zenz
 */
public class TestUILinkedCellEditor extends TestAbstractLinkedCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Test if the displayed values match the values in the DataBook.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void testDisplayedValues() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		IDataBook baseDataBook = createBaseDataBook();
		IDataBook refDataBook = createRefDataBook();
		
		ReferenceDefinition refDef = createReferenceDefinition(refDataBook);
		
		// Let's begin with the testing
		UILinkedCellEditor cellEditor = new UILinkedCellEditor(refDef);
		
		baseDataBook.getRowDefinition().getColumnDefinition("BASE").getDataType().setCellEditor(cellEditor);
		
		baseDataBook.setSelectedRow(0);
		
		// Extract that CellEditor for testing
		JVxLinkedCellEditor jvxCellEditor = (JVxLinkedCellEditor)cellEditor.getUIResource();
		JComponent cellRenderer = jvxCellEditor.getCellRendererComponent(null, baseDataBook, 0, baseDataBook, "BASE", false, false);
		DefaultTableCellRenderer textRenderer = (DefaultTableCellRenderer)cellRenderer.getComponent(0);
		
		Assert.assertEquals("C", textRenderer.getText());
		
		// Extract the CellEditorHandler for testing
		ICellEditorHandler cellEditorHandler = jvxCellEditor.createCellEditorHandler(new StubCellEditorListener(), baseDataBook, "BASE");
		JVxLinkedCellEditor.CellEditorHandler jvxCellEditorHandler = (JVxLinkedCellEditor.CellEditorHandler)cellEditorHandler;
		jvxCellEditorHandler.cancelEditing();
		
		JVxComboBase cellEditorHandlerComponent = (JVxComboBase)jvxCellEditorHandler.getCellEditorComponent();
		
		Assert.assertEquals("C", cellEditorHandlerComponent.getSelectedItem());
	}
	
	/**
	 * Test for ticket #1014. Setting a {@code displayColumnName} should change
	 * the displayed values to these in the given column.
	 * 
	 * @throws ModelException if the test fails.
	 */
	@Test
	public void testTicket1014() throws ModelException
	{
		UIFactoryManager.getFactoryInstance(SwingFactory.class);
		
		IDataBook baseDataBook = createBaseDataBook();
		IDataBook refDataBook = createRefDataBook();
		
		ReferenceDefinition refDef = createReferenceDefinition(refDataBook);
		
		// Let's begin with the testing
		UILinkedCellEditor cellEditor = new UILinkedCellEditor(refDef);
		cellEditor.setDisplayReferencedColumnName("DISPLAY_VALUE");
		
		baseDataBook.getRowDefinition().getColumnDefinition("BASE").getDataType().setCellEditor(cellEditor);
		
		baseDataBook.setSelectedRow(0);
		
		// Extract that CellEditor for testing
		JVxLinkedCellEditor jvxCellEditor = (JVxLinkedCellEditor)cellEditor.getUIResource();
		JComponent cellRenderer = jvxCellEditor.getCellRendererComponent(null, baseDataBook, 0, baseDataBook, "BASE", false, false);
		DefaultTableCellRenderer textRenderer = (DefaultTableCellRenderer)cellRenderer.getComponent(0);
		
		Assert.assertEquals("Contract", textRenderer.getText());
		
		// Extract the CellEditorHandler for testing
		ICellEditorHandler cellEditorHandler = jvxCellEditor.createCellEditorHandler(new StubCellEditorListener(), baseDataBook, "BASE");
		JVxLinkedCellEditor.CellEditorHandler jvxCellEditorHandler = (JVxLinkedCellEditor.CellEditorHandler)cellEditorHandler;
		jvxCellEditorHandler.cancelEditing();
		
		JVxComboBase cellEditorHandlerComponent = (JVxComboBase)jvxCellEditorHandler.getCellEditorComponent();
		
		Assert.assertEquals("Contract", cellEditorHandlerComponent.getSelectedItem());
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A stub that extends {@link Component} and implements {@link IControl} and
	 * {@link ICellFormatterEditorListener}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class StubCellEditorListener extends Component
			implements IControl,
			ICellFormatterEditorListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void editingStarted()
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void editingComplete(String pCompleteType)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isSavingImmediate()
		{
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public ICellFormatter getCellFormatter()
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setTranslation(TranslationMap pTranslation)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public TranslationMap getTranslation()
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setTranslationEnabled(boolean pEnabled)
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isTranslationEnabled()
		{
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public String translate(String pText)
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void notifyRepaint()
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void saveEditing() throws ModelException
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void cancelEditing()
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		public IControl getControl()
		{
			return this;
		}
		
	}	// StubCellEditorListener
	
}	// TestMemDataBook
