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
 * 11.09.2015 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.impl.control;

import java.util.List;
import java.util.Optional;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.ICellToolTip;
import javax.rad.ui.control.ITable;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.DataBookSelectionSetter;
import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.DataBookValuePresentationProvider;
import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.DataRowFromRowIndexValueProvider;
import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.DummyDataProvider;
import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.RowIndexDataProvider;
import com.sibvisions.rad.ui.vaadin.ext.grid.field.CellEditorField;
import com.sibvisions.rad.ui.vaadin.ext.grid.renderer.ClickableHtmlRenderer;
import com.sibvisions.rad.ui.vaadin.ext.grid.support.CheckBoxRendererClickListener;
import com.sibvisions.rad.ui.vaadin.ext.grid.support.ChoiceRendererClickListener;
import com.sibvisions.rad.ui.vaadin.ext.grid.support.EditingClickListener;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedGrid;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.ColumnReorderEvent;
import com.vaadin.ui.Grid.GridContextClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.ColumnReorderListener;
import com.vaadin.ui.components.grid.DescriptionGenerator;
import com.vaadin.ui.components.grid.EditorCancelEvent;
import com.vaadin.ui.components.grid.EditorCancelListener;
import com.vaadin.ui.components.grid.EditorOpenEvent;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.renderers.Renderer;

/**
 * The {@link VaadinGrid} is an {@link ITable} implementation which utilizes the
 * {@link ExtendedGrid}.
 * 
 * @author Robert Zenz
 */
public class VaadinGrid extends VaadinComponent<ExtendedGrid<Integer>> implements ITable,
                                                                    	  IDataBookListener,
                                                                    	  Runnable,
                                                                    	  ContextClickListener,
                                                                    	  SelectionListener,
                                                                    	  ICellFormatter,
                                                                    	  ColumnReorderListener,
                                                                    	  EditorCancelListener<Integer>,
                                                                    	  EditorSaveListener<Integer>,
                                                                    	  EditorOpenListener<Integer>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The columns are automatically resizing. */
	private boolean autoResize = true;
	
	/** The calculated row height. */
	private int calculatedRowHeight;
	
	/** The {@link ICellFormatter} that is used. */
	private ICellFormatter cellFormatter;
	
	/** The {@link ICellToolTip} that is used. */
	private ICellToolTip cellToolTip;
	
	/** The {@link ColumnView} used. */
	private ColumnView columnView;
	
	/** the data provoder. */
	private RowIndexDataProvider provider = null;
	
	/** The {@link IDataBook} that is used. */
	private IDataBook dataBook;
	
	/** If this is editable. */
	private boolean editable = true;
	
	/** The enter navigation mode. */
	private int enterNavigationMode = NAVIGATION_ROW_AND_FOCUS;
	
	/** If selection events should be ignored. */
	private boolean ignoreSelectionEvents = false;
	
    /** If reorder events should be ignored. */
    private boolean ignoreReorderEvents = false;

    /** The maximum row height. */
	private int maxRowHeight;
	
	/** The minimum row height. */
	private int minRowHeight;
	
	/** If the grid has been notified. */
	private boolean notified = false;
	
	/** If a rebuild is required. */
	private volatile boolean rebuildRequired = false;
	
	/** The row height. */
	private int rowHeight;
	
	/** If the focus rectangle should be visible. */
	private boolean showFocusRect;
	
	/** If the horizontal lines are visible. */
	private boolean showHorizontalLines;
	
	/** If the selection is visible/possible. */
	private boolean showSelection = true;
	
	/** If the vertical lines are visible. */
	private boolean showVerticalLines;
	
	/** If sorting by clicking the headers is enabled. */
	private boolean sortOnHeaders = true;
	
	/** The tab navigation mode. */
	private int tabNavigationMode = NAVIGATION_CELL_AND_ROW_AND_FOCUS;
	
	/** The {@link TranslationMap} that is used. */
	private TranslationMap translation;
	
	/** If the translation is enabled. */
	private boolean translationEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VaadinGrid}.
	 */
	public VaadinGrid()
	{
		super(new ExtendedGrid<>());
		
		resource.getEditor().setBuffered(false);
		resource.getEditor().addCancelListener(this);
		resource.getEditor().addOpenListener(this);
		resource.getEditor().addSaveListener(this);
		
		resource.setColumnReorderingAllowed(true);
		resource.setSizeFull();
		
		resource.addContextClickListener(this);
		resource.addSelectionListener(this);
		resource.addColumnReorderListener(this);
		
		resource.setStyleName("jvxgrid");
		
		setShowHorizontalLines(false);
		setShowVerticalLines(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelEditing()
	{
		resource.getEditor().cancel();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextClick(ContextClickEvent pEvent)
	{
		if (((GridContextClickEvent<Integer>)pEvent).getItem() != null)
		{
			resource.getSelectionModel().select(((GridContextClickEvent<Integer>)pEvent).getItem());
		}
		
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
	@Override
	public void dataBookChanged(DataBookEvent pDataBookEvent) throws ModelException
	{
		updateSelectionFromDataBook();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable
	{
		if (cellFormatter != null)
		{
			return cellFormatter.getCellFormat(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pColumn);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnView getColumnView()
	{
        if (columnView == null && dataBook != null)
        {
            return dataBook.getRowDefinition().getColumnView(ITableControl.class);
        }
        else
        {
            return columnView;
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataBook getDataBook()
	{
		return dataBook;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getEnterNavigationMode()
	{
		return enterNavigationMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxRowHeight()
	{
		return maxRowHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinRowHeight()
	{
		return minRowHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowHeight()
	{
		if (calculatedRowHeight < 0)
		{
			if (rowHeight < 0)
			{
				return minRowHeight;
			}
			
			return rowHeight;
		}
		else if (calculatedRowHeight > maxRowHeight)
		{
			return maxRowHeight;
		}
		
		return calculatedRowHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabNavigationMode()
	{
		return tabNavigationMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return translation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoResize()
	{
		return autoResize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable()
	{
		return editable;
	}
	
	/**
	 * This method is a stub and return always {@code true}.
	 * 
	 * @return always {@code true}.
	 */
	@Override
	public boolean isMouseEventOnSelectedCell()
	{
		// TODO This method is a stub and always returns true.
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowFocusRect()
	{
		return showFocusRect;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowHorizontalLines()
	{
		return showHorizontalLines;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowSelection()
	{
		return showSelection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowVerticalLines()
	{
		return showVerticalLines;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSortOnHeaderEnabled()
	{
		return sortOnHeaders;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTableHeaderVisible()
	{
		return resource.isHeaderVisible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTranslationEnabled()
	{
		return translationEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyRepaint()
	{
		if (!notified)
		{
			notified = true;
			
			getFactory().invokeLater(this);
		}
	}
	
	/**
	 * Updates and refreshes the grid.
	 */
	@Override
	public void run()
	{
		// TODO We should only suppress this if the changes originated from us.
		if (!resource.getEditor().isOpen())
		{
			if (rebuildRequired)
			{
				updateControl();
				rebuildRequired = false;
			}
			
			updateColumnProperties();
			
			provider.refreshAll();
			
			updateSelectionFromDataBook();
		}
		
		notified = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorOpen(EditorOpenEvent<Integer> pEvent)
	{
		discardAllEditors();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorSave(EditorSaveEvent<Integer> pEvent)
	{
		commitAllEditors();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEditorCancel(EditorCancelEvent<Integer> pEvent)
	{
		// TODO Keep this in mind.
		// If not in buffered mode, we receive the cancel event when the line is
		// closed because a different row is selected or the Return key has been
		// pressed. Most ironically, we are not receiving any event when the ESC
		// is pressed, also known as "cancel".
		//
		// So we must save the editors here.
		commitAllEditors();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditing() throws ModelException
	{
		if (resource.getEditor().isOpen())
		{
			resource.getEditor().save();
			
			// Cancel closes the editors, save does not.
			resource.getEditor().cancel();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChange(SelectionEvent pEvent)
	{
		if (!ignoreSelectionEvents
				&& dataBook != null)
		{
			ignoreSelectionEvents = true;
			
			try
			{
				Optional<Integer> selection = resource.getSelectionModel().getFirstSelectedItem();
				
				if (selection.isPresent())
				{
					dataBook.setSelectedRow(selection.get().intValue());
				}
				else
				{
					dataBook.setSelectedRow(-1);
				}
				
				discardAllEditors();
			}
			catch (ModelException e)
			{
				ExceptionHandler.raise(e);
			}
			finally
			{
				ignoreSelectionEvents = false;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoResize(boolean pAutoResize)
	{
		autoResize = pAutoResize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		cellFormatter = pCellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnView(ColumnView pColumnView)
	{
		columnView = pColumnView;
		
		rebuildRequired = true;
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataBook(IDataBook pDataBook)
	{
		if (dataBook != null)
		{
			dataBook.removeControl(this);
			dataBook.eventAfterRowSelected().removeListener(this);
		}
		
		dataBook = pDataBook;
		
		if (dataBook != null)
		{
			dataBook.addControl(this);
			dataBook.eventAfterRowSelected().addListener(this);
		}
		
		rebuildRequired = true;
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		editable = pEditable;
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pNavigationMode the new navigation mode, the value is saved but
	 *            ignored.
	 */
	@Override
	public void setEnterNavigationMode(int pNavigationMode)
	{
		// TODO This method is a stub.
		enterNavigationMode = pNavigationMode;
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pMaxRowHeight the new value, it is saved but does not have any
	 *            effect.
	 */
	@Override
	public void setMaxRowHeight(int pMaxRowHeight)
	{
		// TODO This method is a stub.
		maxRowHeight = pMaxRowHeight;
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pMinRowHeight the new value, it is saved but does not have any
	 *            effect.
	 */
	@Override
	public void setMinRowHeight(int pMinRowHeight)
	{
		// TODO This method is a stub.
		minRowHeight = pMinRowHeight;
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pRowHeight the new value, it is saved but does not have any
	 *            effect.
	 */
	@Override
	public void setRowHeight(int pRowHeight)
	{
		// TODO This method is a stub.
		rowHeight = pRowHeight;
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pShowFocusRect if the focus rectangle should be visible, the value
	 *            is saved but ignored.
	 */
	@Override
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		// TODO This method is a stub.
		showFocusRect = pShowFocusRect;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		showHorizontalLines = pShowHorizontalLines;
		
		if (showHorizontalLines)
		{
			addInternStyleName("v-table-cell-bottom-border");
			removeInternStyleName("v-table-cell-bottom-border-none");
		}
		else
		{
			addInternStyleName("v-table-cell-bottom-border-none");
			removeInternStyleName("v-table-cell-bottom-border");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowSelection(boolean pShowSelection)
	{
		showSelection = pShowSelection;
		
		if (showSelection)
		{
			resource.setSelectionMode(SelectionMode.SINGLE);
		}
		else
		{
			resource.setSelectionMode(SelectionMode.NONE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		showVerticalLines = pShowVerticalLines;
		
		if (showVerticalLines)
		{
			addInternStyleName("v-table-cell-left-Left");
			removeInternStyleName("v-table-cell-left-border-none");
		}
		else
		{
			addInternStyleName("v-table-cell-left-border-none");
			removeInternStyleName("v-table-cell-left-border");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
		sortOnHeaders = pSortOnHeaderEnabled;
		
		updateColumnProperties();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		resource.setHeaderVisible(pTableHeaderVisible);
	}
	
	/**
	 * This method is a stub/unsupported.
	 * 
	 * @param pNavigationMode the new navigation mode, the value is saved but
	 *            ignored.
	 */
	@Override
	public void setTabNavigationMode(int pNavigationMode)
	{
		// TODO This method is a stub.
		tabNavigationMode = pNavigationMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		translation = pTranslation;
		
		updateColumnProperties();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslationEnabled(boolean pEnabled)
	{
		translationEnabled = pEnabled;
		
		updateColumnProperties();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startEditing()
	{
		try
		{
			if (isEditable()
					&& dataBook != null
					&& !dataBook.isReadOnly()
					&& dataBook.isUpdateAllowed()
					&& !resource.getEditor().isEnabled()
					&& !resource.getSelectedItems().isEmpty())
			{
				// TODO We should start the editing here.
			}
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String translate(String pText)
	{
		if (translationEnabled && translation != null)
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
	public void columnReorder(ColumnReorderEvent pEvent)
	{
	    if (ignoreReorderEvents)
	    {
	        return;
	    }
	    
	    boolean oldIgnore = ignoreSelectionEvents;
	    
	    ignoreSelectionEvents = true;
	    
	    try
	    {
	        List<Column<Integer, ?>> liCols = resource.getColumns();
	        
	        if (liCols != null)
	        {
	            String[] liColumnNames = new String[liCols.size()]; 
	            
	            for (int i = 0, size = liCols.size(); i < size; i++)
	            {
	                liColumnNames[i] = liCols.get(i).getId();
	            }
	            
	            getColumnView().setColumnNames(liColumnNames);
	        }
	    }
	    finally
	    {
	        ignoreSelectionEvents = oldIgnore;
	    }
	}
	
	/**
     * {@inheritDoc}
     */
    public ICellToolTip getCellToolTip()
    {
        return cellToolTip;
    }

    /**
     * {@inheritDoc}
     */
    public void setCellToolTip(ICellToolTip pCellTooltip)
    {
        cellToolTip = pCellTooltip;
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Commits all editors.
	 */
	private void commitAllEditors()
	{
		for (Column column : resource.getColumns())
		{
			((CellEditorField)column.getEditorBinding().getField()).commit();
		}
	}
	
	/**
	 * Creates all columns.
	 * 
	 * @throws ModelException if column creation fails
	 */
	private void createColumns() throws ModelException
	{
		// These classes are not having any necessity to know which column they
		// are operating on, so they can be reused throughout all columns.
		DataRowFromRowIndexValueProvider valueProvider = new DataRowFromRowIndexValueProvider(dataBook);
		DataBookSelectionSetter setter = new DataBookSelectionSetter(dataBook);
		
		for (String columnName : getColumnNames())
		{
			Column column = resource.addColumn(valueProvider);
			column.setCaption(columnName);
			column.setId(columnName);
			column.setDescriptionGenerator(new CellToolTipGenerator(columnName));
			
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);
			IDataType dataType = columnDefinition.getDataType();
			
			Renderer renderer = null;
			
			renderer = getCorrectRenderer(column, dataType.getCellRenderer());
			
			if (renderer == null)
			{
				renderer = getCorrectRenderer(column, dataType.getCellEditor());
			}
			
			if (renderer == null)
			{
				renderer = new ClickableHtmlRenderer(new EditingClickListener(this));
			}
			
			// Setting the renderer will overwrite an already set presentation
			// provider with a default value, so we need to set both at the same
			// time.
			column.setRenderer(
					new DataBookValuePresentationProvider(dataBook, columnName, this),
					renderer);
			
			ICellEditor cellEditor = null;
			
			if (dataType.getCellEditor() != null)
			{
				cellEditor = dataType.getCellEditor();
			}
			else
			{
				cellEditor = getFactory().getDefaultCellEditor(dataType.getTypeClass());
			}
			
			column.setEditorComponent(
					new CellEditorField(cellEditor, this, dataBook, columnName),
					setter);
		}
		
		updateColumnProperties();
	}
	
	/**
	 * Applies the correct converters and renderers to the given {@link Column}
	 * based upon the given cell editor.
	 * 
	 * @param pColumn the {@link Column}.
	 * @param pCellEditor the {@link ICellEditor}.
	 * @return {@code true} if a converter/renderer was applied.
	 * @throws ModelException if accessing column information failed.
	 */
	private Renderer getCorrectRenderer(Column pColumn, Object pCellEditor) throws ModelException
	{
		if (pCellEditor instanceof ICheckBoxCellEditor<?>)
		{
			ICheckBoxCellEditor<?> checkBoxCellEditor = (ICheckBoxCellEditor<?>)pCellEditor;
			return new ClickableHtmlRenderer(new CheckBoxRendererClickListener(this, checkBoxCellEditor));
		}
		else if (pCellEditor instanceof IChoiceCellEditor<?>)
		{
			IChoiceCellEditor<?> choiceBoxCellEditor = (IChoiceCellEditor<?>)pCellEditor;
			return new ClickableHtmlRenderer(new ChoiceRendererClickListener(this, choiceBoxCellEditor));
		}
		
		return null;
	}
	
	/**
	 * Discards all editors.
	 */
	private void discardAllEditors()
	{
		for (Column column : resource.getColumns())
		{
			((CellEditorField)column.getEditorBinding().getField()).discard();
		}
	}
	
	/**
	 * Gets the names of the columns that should be displayed in this control.
	 * 
	 * @return the names of the columns that should be displayed in this
	 *         control.
	 */
	private String[] getColumnNames()
	{
		IRowDefinition rowDefinition = dataBook.getRowDefinition();
		
		if (columnView != null)
		{
			return columnView.getColumnNames();
		}
		else if (rowDefinition.getColumnView(ITableControl.class) != null)
		{
			return rowDefinition.getColumnView(ITableControl.class).getColumnNames();
		}
		else if (rowDefinition.getColumnView(null) != null)
		{
			return rowDefinition.getColumnView(null).getColumnNames();
		}
		
		return rowDefinition.getColumnNames();
	}
	
	/**
	 * Updates the properties of all columns, like the caption and the width.
	 */
	private void updateColumnProperties()
	{
		if (dataBook == null)
		{
			return;
		}
		
		IRowDefinition rowDefinition = dataBook.getRowDefinition();
		
		for (Column column : resource.getColumns())
		{
			try
			{
				String columnName = column.getId();
				ColumnDefinition columnDefinition = rowDefinition.getColumnDefinition(columnName);
				
				column.setEditable(editable
						&& !dataBook.isReadOnly()
						&& !columnDefinition.isReadOnly());
				
				column.setCaption(translate(columnDefinition.getLabel()));
				column.setSortable(sortOnHeaders && columnDefinition.isSortable());
				
				if (columnDefinition.getWidth() > 0 && !autoResize)
				{
					column.setWidth(columnDefinition.getWidth());
				}
				else
				{
					column.setWidthUndefined();
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.raise(e);
			}
		}
	}
	
	/**
	 * Updates the complete controls, like refreshing all internal state and
	 * columns.
	 */
	private void updateControl()
	{
	    ignoreReorderEvents = true;
	    
	    try
	    {
    		// Remove all columns.
    		resource.setColumns();
    		
    		if (dataBook != null)
    		{
    			try
    			{
    				resource.getEditor().setEnabled(editable
    						&& !dataBook.isReadOnly()
    						&& dataBook.isUpdateAllowed());
    			}
    			catch (ModelException e)
    			{
    				ExceptionHandler.raise(e);
    			}
    			
    			provider = new RowIndexDataProvider(dataBook);
    			resource.setDataProvider(provider);
    			
    			try
    			{
    				createColumns();
    			}
    			catch (ModelException e)
    			{
    				ExceptionHandler.raise(e);
    			}
    			
    			updateSelectionFromDataBook();
    		}
    		else
    		{
    			resource.getEditor().setEnabled(false);
    			resource.setDataProvider(DummyDataProvider.INSTANCE);
    			provider = null;
    		}
	    }
	    finally
	    {
	        ignoreReorderEvents = false;
	    }
	}
	
	/**
	 * Updates the selection from the databook.
	 */
	private void updateSelectionFromDataBook()
	{
		try
		{
			if (!ignoreSelectionEvents
					&& dataBook != null
					&& provider != null)
			{
				ignoreSelectionEvents = true;
				
				try
				{
					if (dataBook.getSelectedRow() >= 0)
					{
						Integer currentRowId = Integer.valueOf(dataBook.getSelectedRow());
						resource.getSelectionModel().select(currentRowId);
					}
					else
					{
						resource.getSelectionModel().deselectAll();
					}
					
					discardAllEditors();
				}
				finally
				{
					ignoreSelectionEvents = false;
				}
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.raise(e);
		}
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Helper class which stores column name. We need column name in the tooltip generation method, but it has only one parameter of type Integer.
	 * 
	 * @author Jozef Dorko
	 */
	private class CellToolTipGenerator implements DescriptionGenerator<Integer>
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
		/** the grid column name. */
		private String columnName;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>CellToolTipGenerator</code> for a specific column name.
		 * 
		 * @param pColumnName the grid column name
		 */
		public CellToolTipGenerator(String pColumnName)
		{
			columnName = pColumnName;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public String apply(Integer pRowNum)
		{
			String description = null;
			
			if (cellToolTip != null)
			{
				try
				{
					int rowNum = pRowNum.intValue();
					
					description = cellToolTip.getToolTipText(dataBook,
															 dataBook.getDataPage(),
															 dataBook.getDataRow(rowNum),
															 columnName,
															 rowNum,
															 getColumnView().getColumnNameIndex(columnName));
				}
				catch (Throwable e) 
				{
					// Ignore the exception.
				}
			}
			
			return description;
		}
		
	}	// CellToolTipGenerator
	
}	// VaadinGrid
