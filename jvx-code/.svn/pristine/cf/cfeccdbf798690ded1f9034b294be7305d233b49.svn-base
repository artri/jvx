/*
 * Copyright 2012 SIB Visions GmbH
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
 * 25.04.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.control;

import java.util.ArrayList;
import java.util.List;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.data.DummySetter;
import com.sibvisions.rad.ui.vaadin.ext.data.PassThroughValueProvider;
import com.sibvisions.rad.ui.vaadin.ext.grid.field.CellEditorField;
import com.sibvisions.rad.ui.vaadin.ext.treegrid.databinding.TreePathDataProvider;
import com.sibvisions.rad.ui.vaadin.ext.treegrid.databinding.ValueFromTreePathValueProvider;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedTreeGrid;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.IVaadinCellEditorHandler;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.ShortcutHandler;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinTextCellEditor;
import com.vaadin.event.Action;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.EditorCancelEvent;
import com.vaadin.ui.components.grid.EditorCancelListener;
import com.vaadin.ui.components.grid.EditorOpenEvent;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * The <code>VaadinTree</code> is the <code>ITree</code> implementation for
 * vaadin.
 * 
 * @author Stefan Wurm
 */
public class VaadinTreeGrid extends VaadinComponent<ExtendedTreeGrid>
                        implements ITree,
                                   ContextClickListener,
                                   SelectionListener<TreePath>,
                                   Runnable,
                                   ICellFormatter,
                                   ICellFormatterEditorListener,
                                   INodeFormatter,
                                   EditorCancelListener<TreePath>,
                                   EditorSaveListener<TreePath>,
                                   EditorOpenListener<TreePath>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** The IDataBook to be shown. */
	private IDataBook[] dataBooks = null;
	
	/** The selfjoined databooks. */
	private IDataBook selfJoinedDataBook = null;
	
	/** The selected data book. **/
	private IDataBook currentDataBook = null;
	
	/** The selected tree path. **/
	private TreePath selectedTreePath = null;
	
	/** The maximum length of not selfjoined databooks. */
	private int maxLength = 0;
	
	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The Node Formatter. **/
	private INodeFormatter nodeFormatter = null;
	
	/** The translation mapping. */
	private TranslationMap translation = null;
	
	/** Tells, if notifyRepaint is called the first time. */
	private boolean bFirstNotifyRepaintCall = true;
	
	/** Ignoring Events. */
	private volatile boolean bIgnoreEvent = false;
	
	/** If the table is editable. **/
	private boolean editable = false;
	
	/** whether the translation is enabled. */
	private boolean bTranslationEnabled = true;
	
	/** the visible column. */
	private Column column = null;
	
	/** the cell editor. */
	private SelfContainedCellEditor selfContainedCellEditor = new SelfContainedCellEditor();
	
	/** whether to commit the previous editor. */
	private boolean commitPreviousEditor = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * New instance of <code>VaadinTree</code>.
	 */
	public VaadinTreeGrid()
	{
		super(new ExtendedTreeGrid());
		
		resource.setHeaderVisible(false);
		resource.setSelectionMode(SelectionMode.SINGLE);
		resource.setStyleName("jvxtreegrid");
		
		resource.getEditor().setBuffered(false);
		resource.getEditor().addCancelListener(this);
		resource.getEditor().addOpenListener(this);
		resource.getEditor().addSaveListener(this);
		
		resource.addContextClickListener(this);
		resource.addSelectionListener(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public void contextClick(ContextClickEvent pEvent)
	{
		if (eventMouseReleased != null)
		{
			getFactory().synchronizedDispatchEvent(eventMouseReleased, 
					                               new UIMouseEvent(this,
																	UIMouseEvent.MOUSE_RELEASED,
																	System.currentTimeMillis(),
																	UIMouseEvent.BUTTON2_MASK,
																	pEvent.getClientX(),
																	pEvent.getClientY(),
																	pEvent.getMouseEventDetails().isDoubleClick() ? 2 : 1,
																	true));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		bFirstNotifyRepaintCall = true;
		
		if (!bIgnoreEvent && dataBooks != null)
		{
			try
			{
				bIgnoreEvent = true;
				
				resource.getDataProvider().refreshAll();
				
				updateSelectionFromDataBooks();
			}
			catch (ModelException e)
			{
				ExceptionHandler.raise(e);
			}
			finally
			{
				bIgnoreEvent = false;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook[] getDataBooks()
	{
		return dataBooks;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDataBooks(IDataBook... pDataBooks)
	{
		if (dataBooks != null)
		{
			for (int i = 0; i < dataBooks.length; i++)
			{
				dataBooks[i].removeControl(this);
			}
		}
		
		if (pDataBooks == null || pDataBooks.length == 0)
		{
			dataBooks = null;
		}
		else
		{
			dataBooks = pDataBooks;
		}
		
		if (dataBooks != null)
		{
			for (int i = 0; i < dataBooks.length; i++)
			{
				dataBooks[i].addControl(this);
			}
			
			IDataBook dataBook = dataBooks[dataBooks.length - 1];
			
			if (dataBook.isSelfJoined())
			{
				selfJoinedDataBook = dataBook;
				maxLength = dataBooks.length - 1;
			}
			else
			{
				maxLength = dataBooks.length;
			}
		}
		
		resource.setDataProvider(new TreePathDataProvider(dataBooks));
		
		column = resource.addColumn(PassThroughValueProvider.getInstance());
		column.setId("column");
		column.setEditorComponent(
				new CellEditorField(selfContainedCellEditor.createCellEditor(), this, null, null),
				DummySetter.getInstance());
		column.setRenderer(
				new ValueFromTreePathValueProvider(this),
				new HtmlRenderer());
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		return currentDataBook;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (bFirstNotifyRepaintCall && !bIgnoreEvent) // Check additionally if editing is started, to prevent immediate closing editor
		{
			bFirstNotifyRepaintCall = false;
			
			getFactory().invokeLater(this);
		}
	}
	
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
		if (pCompleteType == ICellEditorListener.ESCAPE_KEY)
		{
			cancelEditing();
		}
		else
		{
			try
			{
				saveEditing();
			}
			catch (ModelException ex)
			{
				cancelEditing();
				ExceptionHandler.raise(ex);
			}
		}
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
	public IControl getControl()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void startEditing()
	{
		if (isEditable())
		{
			resource.select(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		if (!bIgnoreEvent)
		{
			cancelEditing();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		if (!bIgnoreEvent)
		{
			resource.getEditor().cancel();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		if (translation != pTranslation)
		{
			translation = pTranslation;
			
			try
			{
				saveEditing();
			}
			catch (ModelException e)
			{
				cancelEditing();
			}
			
			notifyRepaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TranslationMap getTranslation()
	{
		return translation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTranslationEnabled(boolean pEnabled)
	{
		bTranslationEnabled = pEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTranslationEnabled()
	{
		return bTranslationEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String translate(String pText)
	{
		if (bTranslationEnabled && translation != null)
		{
			return translation.translate(pText);
		}
		else
		{
			return pText;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		cellFormatter = pCellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable
	{
		if (cellFormatter == null)
		{
			return null;
		}
		
		return cellFormatter.getCellFormat(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pColumn);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage getNodeImage(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, boolean pExpanded, boolean pLeaf)
	{
		if (nodeFormatter == null)
		{
			return null;
		}
		
		return nodeFormatter.getNodeImage(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pExpanded, pLeaf);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDetectEndNode()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		// TODO Disabled because not supported by the Vaadin model.
		// Or at least I could not make it work, the TreeGrid doesn't
		// behave very nicely when the model returns hasChildren() true
		// but there no actual children.
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEditable()
	{
		return editable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
		editable = pEditable;
		
		resource.getEditor().setEnabled(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public INodeFormatter getNodeFormatter()
	{
		return nodeFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNodeFormatter(INodeFormatter pNodeFormatter)
	{
		nodeFormatter = pNodeFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void selectionChange(SelectionEvent<TreePath> pEvent)
	{
		if (bIgnoreEvent)
		{
			return;
		}
		
		try
		{
			bIgnoreEvent = true;
			
			if (pEvent.getFirstSelectedItem().isPresent())
			{
				selectedTreePath = pEvent.getFirstSelectedItem().get();
				selectPathInDataBooks();
				
				selfContainedCellEditor.updateBinding(currentDataBook, getColumnName(currentDataBook));
			}
		}
		catch (Throwable thr)
		{
			bIgnoreEvent = false;
			notifyRepaint();
			ExceptionHandler.raise(thr);
		}
		finally
		{
			bIgnoreEvent = false;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthFull()
	{
		if (VaadinUtil.isParentWidthDefined(resource))
		{
			VaadinUtil.setComponentWidth(resource, 100, Unit.PERCENTAGE);
		}
		else
		{
			setWidthUndefined();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightFull()
	{
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight(resource, 100, Unit.PERCENTAGE);
		}
		else
		{
			setHeightUndefined();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthUndefined()
	{
		VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightUndefined()
	{
		VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorOpen(EditorOpenEvent<TreePath> pEvent)
	{
		if (commitPreviousEditor)
		{
			bIgnoreEvent = true;
			
			selfContainedCellEditor.commit();
			
			bIgnoreEvent = false;
		}
		
		if (pEvent.getBean() != null)
		{
			// We have to correct the selection.
			resource.select(pEvent.getBean());
		}
		
		selfContainedCellEditor.discard();
		
		commitPreviousEditor = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorSave(EditorSaveEvent<TreePath> pEvent)
	{
		selfContainedCellEditor.commit();
		
		commitPreviousEditor = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorCancel(EditorCancelEvent<TreePath> pEvent)
	{
		if (bIgnoreEvent)
		{
			return;
		}
		
		selfContainedCellEditor.discard();
		
		commitPreviousEditor = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Handles the key actions.
	 * 
	 * @param pAction the key action.
	 */
	public void handleAction(Action pAction)
	{
		if (isEditable())
		{
			if (pAction == ShortcutHandler.ACTION_TAB)
			{
				editingComplete(ICellEditorListener.TAB_KEY);
			}
			else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
			{
				editingComplete(ICellEditorListener.SHIFT_TAB_KEY);
			}
			else if (pAction == ShortcutHandler.ACTION_ENTER
					|| pAction == ShortcutHandler.ACTION_ALT_ENTER
					|| pAction == ShortcutHandler.ACTION_CTRL_ENTER
					|| pAction == ShortcutHandler.ACTION_META_ENTER)
			{
				editingComplete(ICellEditorListener.ENTER_KEY);
			}
			else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
			{
				editingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
			}
			else if (pAction == ShortcutHandler.ACTION_ESCAPE)
			{
				editingComplete(ICellEditorListener.ESCAPE_KEY);
			}
			else if (pAction == ShortcutHandler.ACTION_EDIT)
			{
				startEditing();
			}
		}
	}
	
	/**
	 * Gets the required column name form the given {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook} from which to get the column name.
	 * @return the column name from the {@link IDataBook}.
	 * @throws ModelException when accessing the information of the
	 *             {@link IDataBook} failed.
	 */
	private String getColumnName(IDataBook pDataBook) throws ModelException
	{
		return pDataBook.getRowDefinition().getColumnView(ITree.class).getColumnName(0);
	}
	
	/**
	 * Updates the current selection from the databooks.
	 * 
	 * @throws ModelException if accessing the databooks failed.
	 */
	private void updateSelectionFromDataBooks() throws ModelException
	{
		List<TreePath> treePaths = new ArrayList<TreePath>();
		TreePath lastTreePath = null;
		IDataBook lastDataBook = null;
		
		for (IDataBook dataBook : dataBooks)
		{
			if (dataBook.getSelectedRow() >= 0)
			{
				if (lastTreePath == null)
				{
					lastTreePath = new TreePath(dataBook.getSelectedRow());
				}
				else
				{
					int[] pieces = new int[lastTreePath.length() + 1];
					System.arraycopy(lastTreePath.toArray(), 0, pieces, 0, lastTreePath.length());
					pieces[pieces.length - 1] = dataBook.getSelectedRow();
					
					lastTreePath = new TreePath(pieces);
				}
				
				treePaths.add(lastTreePath);
				lastDataBook = dataBook;
			}
		}
		
		if (lastDataBook != null && lastDataBook.isSelfJoined())
		{
			TreePath treePath = lastDataBook.getTreePath();
			
			if (treePath != null)
			{
				for (int piece : treePath.toArray())
				{
					int[] pieces = new int[lastTreePath.length() + 1];
					System.arraycopy(lastTreePath.toArray(), 0, pieces, 0, lastTreePath.length());
					pieces[pieces.length - 1] = piece;
					
					lastTreePath = new TreePath(pieces);
				}
			}
		}
		
		resource.getSelectionModel().deselectAll();
		
		for (TreePath treePath : treePaths)
		{
			resource.expand(treePath.getParentPath());
			resource.select(treePath);
		}
	}
	
	/**
	 * Select the path in the databooks.
	 * 
	 * @throws ModelException if selecting path in databooks fails
	 */
	private void selectPathInDataBooks() throws ModelException
	{
		if (selectedTreePath != null)
		{
			int selLength = Math.min(maxLength, selectedTreePath.length());
			
			for (int i = 0; i < selLength; i++)
			{
				dataBooks[i].setSelectedDataPageRow(selectedTreePath.get(i));
				currentDataBook = dataBooks[i];
			}
			
			for (int i = selLength; i < maxLength; i++)
			{
				dataBooks[i].setSelectedRow(-1);
			}
			
			if (selfJoinedDataBook != null)
			{
				int pathLen = selectedTreePath.length() - dataBooks.length;
				
				TreePath treePath = new TreePath();
				int selRow = -1;
				if (pathLen >= 0)
				{
					for (int i = 0; i < pathLen; i++)
					{
						treePath = treePath.getChildPath(selectedTreePath.get(dataBooks.length - 1 + i));
					}
					
					selRow = selectedTreePath.getLast();
				}
				selfJoinedDataBook.setTreePath(treePath);
				selfJoinedDataBook.setSelectedDataPageRow(selRow);
				currentDataBook = selfJoinedDataBook;
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link SelfContainedCellEditor} is a simple container which allows to
	 * abuse the celleditor.
	 * 
	 * @author Robert Zenz
	 */
	private static final class SelfContainedCellEditor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The current {@link ICellEditor}. */
		private ICellEditor cellEditor = null;
		
		/** The current {@link ICellEditorHandler}. */
		private ICellEditorHandler cellEditorHandler = null;
		
		/** The internal {@link IDataRow}. */
		private IDataRow dataRow = null;
		
		/** The parent column name. */
		private String parentColumnName = null;
		
		/** The parent {@link IDataBook}. */
		private IDataBook parentDataBook = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link SelfContainedCellEditor}.
		 */
		public SelfContainedCellEditor()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Commits the changes.
		 */
		public void commit()
		{
			try
			{
				cellEditorHandler.saveEditing();
				
				parentDataBook.setValue(parentColumnName, dataRow.getValue("VALUE"));
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Creates the {@link ICellEditor} for use.
		 * 
		 * @return the {@link ICellEditor} for use.
		 */
		public ICellEditor createCellEditor()
		{
			if (dataRow == null)
			{
				dataRow = new DataRow();
				
				try
				{
					dataRow.getRowDefinition().addColumnDefinition(new ColumnDefinition("VALUE", new StringDataType()));
				}
				catch (ModelException e)
				{
					throw new RuntimeException(e);
				}
			}
			
			if (cellEditor == null)
			{
				cellEditor = new VaadinTextCellEditor()
				{
					@Override
					public IVaadinCellEditorHandler<Component> createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
					{
						cellEditorHandler = super.createCellEditorHandler(pCellEditorListener, dataRow, "VALUE");
						
						return (IVaadinCellEditorHandler<Component>)cellEditorHandler;
					}
					
				};
			}
			
			return cellEditor;
		}
		
		/**
		 * Discards the changes.
		 */
		public void discard()
		{
			if (parentDataBook == null || dataRow == null)
			{
				return;
			}
			
			try
			{
				dataRow.setValue("VALUE", parentDataBook.getValue(parentColumnName));
				
				cellEditorHandler.cancelEditing();
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Updates the bindings.
		 * 
		 * @param pDataBook the new parent {@link IDataBook}.
		 * @param pColumnName the new parent column name.
		 */
		public void updateBinding(IDataBook pDataBook, String pColumnName)
		{
			parentDataBook = pDataBook;
			parentColumnName = pColumnName;
		}
		
	}	// SelfContainedCellEditor
	
}	// VaadinTreeGrid
