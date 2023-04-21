/*
 * Copyright 2013 SIB Visions GmbH
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
 * 22.01.2013 - [SW] - creation
 * 18.03.2013 - [TK] - #968
 * 18.09.2015 - [JR] - #1470: add column name to cell style
 */
package com.sibvisions.rad.ui.vaadin.impl.control;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IResource;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.IInplaceCellEditor;
import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.ICellToolTip;
import javax.rad.ui.control.ITable;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.rad.ui.celleditor.AbstractLinkedCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedTable;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.table.JVxContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.table.JVxContainer.JVxContainerProperty;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.IEditorComponent;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.IVaadinCellEditorHandler;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.ShortcutHandler;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable.TableComponent;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.event.Action;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.server.ClientConnector.AttachListener;
import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.FontIcon;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.RichTextArea;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;
import com.vaadin.v7.ui.Table.ColumnReorderListener;
import com.vaadin.v7.ui.Table.HeaderClickEvent;
import com.vaadin.v7.ui.Table.HeaderClickListener;
import com.vaadin.v7.ui.TextArea;

/**
 * The <code>VaadinTable</code> is the <code>ITable</code>
 * implementation for vaadin.
 *  
 * @author Stefan Wurm
 */
@SuppressWarnings("deprecation")
public class VaadinTable extends VaadinComponent<TableComponent> 
                         implements ITable, 
                                    ICellFormatterEditorListener,
									ItemClickListener, 
									ColumnReorderListener,
									ValueChangeListener,
									HeaderClickListener, 
									CellStyleGenerator,
									ItemDescriptionGenerator,
									Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The column icon for sort asc. **/
    private static final String ICON_ASC = "../jvxbase/images/asc-light.png";
    
    /** The column icon for sort desc. **/
    private static final String ICON_DESC = "../jvxbase/images/desc-light.png";
    
    /** The default size of a table if it is undefined. **/
    private static final int DEFAULT_TABLE_HEIGHT = 400;

    /** The IDataBook to be shown. */
	private IDataBook dataBook = null;

	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The cellToolTipListener. */
	private ICellToolTip cellToolTip;

	/** The translation mapping. */
	private TranslationMap translation = null;

	/** The container for the vaadin table with the data book. **/
	private JVxContainer jvxContainer;	
	
	/** The CssExtensionAttribute for the column rect. **/
	private static CssExtensionAttribute columnRectCssAttribute1 = new CssExtensionAttribute("border", "5px dashed rgba(81, 203, 238, 1)", "v-table-cell-content", 
																					CssExtensionAttribute.SEARCH_UP);
	/** Padding for the column rect. **/
	private static CssExtensionAttribute columnRectCssAttribute2 = new CssExtensionAttribute("padding-left", "5px", "v-table-cell-content", CssExtensionAttribute.SEARCH_UP);

	/** Padding for the column rect. **/
	private static CssExtensionAttribute columnRectCssAttribute3 = new CssExtensionAttribute("padding-right", "5px", "v-table-cell-content", CssExtensionAttribute.SEARCH_UP);
	
	/** All needed css attributes for the column rect. **/
	private static ArrayList<CssExtensionAttribute> columnRectCssAttributes = new ArrayList<CssExtensionAttribute>();
	
	/** The column view. */
	private ColumnView columnView = null;	
	
	/** The used CellEditor. */
	private ICellEditorHandler<Component> cellEditorHandler = null;
	
	/** ascending icon resource. */
	private ThemeResource thrIconAsc = new ThemeResource(ICON_ASC);
	/** descending icon resource. */
	private ThemeResource thrIconDesc = new ThemeResource(ICON_DESC);
	
	/** The itemId of the actual edited row. **/
	private Integer editItemId = null;
	
	/** The propertyId of the actual edited column. **/
	private String editPropertyId = null;
	
    /** The row height. Automatic row height is set with -1. */
    private int rowHeight = 25;
    
    /** The enter navigation mode. */
    private int enterNavigationMode = NAVIGATION_CELL_AND_FOCUS;
    
    /** The enter navigation mode. */
    private int tabNavigationMode = NAVIGATION_CELL_AND_FOCUS;
    
    /** The minimal row height. */
    private int minRowHeight = 25;
    
    /** The minimal row height. */
    private int maxRowHeight = 120;
    
    /** The calculated row height because of the Vaadin Images. */
    private int calculatedRowHeight = -1;

    /** If table is attached. **/
    private boolean isAttached = false;
    
    /** true, if a delete or insert occured. */
    private boolean dataChanged = false;

    /** If the container should be initialized in the run method. **/
	private boolean firstInitializeContainer = true;

    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
    /** Tells, if notifyRepaint is called the first time. */
    private boolean bFirstNotifyRepaintCall = true;

    /** Cell Editor started editing. */
    private boolean bEditingStarted = false;
    
    /** Ignoring Events. */
    private boolean bIgnoreEvent = false;

    /** If the table is editable. **/
    private boolean editable = false;
    
    /** If the autosize mode is on. **/
    private boolean bAutoResize = true;

    /** If sort on header is enabled. */
    private boolean bSortOnHeader = true;

    /** The last configured sort. */
    private SortDefinition lastSort = null;
    
    /** If the table shows the horizontal lines. **/
    private boolean showHorizontalLines = true;
    
    /** If the table shows the vertical lines. **/
    private boolean showVerticalLines = false;
    
    /** If the table selection. **/
    private boolean showSelection = true;
    
    /** If the focus rect around the column is showen. **/
    private boolean showFocusRect = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		columnRectCssAttributes.add(columnRectCssAttribute1);
		columnRectCssAttributes.add(columnRectCssAttribute2);
		columnRectCssAttributes.add(columnRectCssAttribute3);
	}
	
	/** 
	 * Creates a new instance of <code>VaadinTable</code>.
	 */
	public VaadinTable()
	{
		super(new TableComponent());
		
		((TableComponent)resource).table = this;
		resource.setImmediate(true);
		resource.setNullSelectionAllowed(false);
		resource.setSortEnabled(false);
		resource.setEditable(false);
		resource.setCacheRate(1f);
		resource.setColumnReorderingAllowed(true);
		resource.addColumnReorderListener(this);
		
		jvxContainer = new JVxContainer(this);
		
		addInternStyleName("components-inside");
		resource.addItemClickListener(this);
		resource.addHeaderClickListener(this);
		resource.addValueChangeListener(this);
//		resource.setPageLength(60);
		
		setSortOnHeaderEnabled(true);
		setEditable(true);
		setShowSelection(showSelection);
		
		resource.setCellStyleGenerator(this);
		
		setSizeFull();
		
		resource.addAttachListener(new AttachListener() 
		{
			public void attach(AttachEvent event)
			{
    			isAttached = true;
    			
    			resource.setPageLength(60); // reinitialize the page length to 60, to ensure, data is not fetched several times.

    			setDataChanged();
    			installTable();
			}
			
		});
		resource.addDetachListener(new DetachListener()
		{
			public void detach(DetachEvent event)
			{
    			uninstallTable();

    			isAttached = false;
			}
			
		});
	}

	/**
	 * Installs the table.
	 */
	private void installTable()
	{
        if (dataBook != null && isAttached)
        {
            jvxContainer.setDataBook(dataBook); 
            resource.setContainerDataSource(jvxContainer);
            jvxContainer.addItemSetChangeListener(resource);
            jvxContainer.addPropertySetChangeListener(resource);

            dataBook.eventAfterInserting().addInternalListener(this, "setDataChanged");
            dataBook.eventAfterInserted().addInternalListener(this, "setDataChanged");
            dataBook.eventAfterUpdated().addInternalListener(this, "setDataChanged");
            dataBook.eventAfterDeleted().addInternalListener(this, "setDataChanged");
            dataBook.eventAfterRestore().addInternalListener(this, "setDataChanged");
            dataBook.eventAfterReload().addInternalListener(this, "setDataChangedReload");
            dataBook.eventValuesChanged().addInternalListener(this, "setDataChanged");
            
            if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
            {
                columnView.addRowDefinition(dataBook.getRowDefinition());
            }
            
            firstInitializeContainer = true;
            notifyRepaint();
        }
	}

	/**
     * Installs the table.
     */
    private void uninstallTable()
    {
        if (dataBook != null)
        {
            dataBook.eventAfterInserting().removeInternalListener(this);
            dataBook.eventAfterInserted().removeInternalListener(this);
            dataBook.eventAfterUpdated().removeInternalListener(this);
            dataBook.eventAfterDeleted().removeInternalListener(this);
            dataBook.eventAfterRestore().removeInternalListener(this);
            dataBook.eventAfterReload().removeInternalListener(this);
            dataBook.eventValuesChanged().removeInternalListener(this);

            jvxContainer.removeItemSetChangeListener(resource);
            jvxContainer.removePropertySetChangeListener(resource);
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
	/**
	 * {@inheritDoc}
	 */	
	public void run()
	{
		bFirstNotifyRepaintCall = true;
		
    	if (!bIgnoreEvent 
    		&& dataBook != null
    		&& dataBook.isOpen())
    	{
    		bIgnoreEvent = true;
    		try
    		{
    			jvxContainer.clearPropertyCache();
    			
    			boolean columnViewChanged = jvxContainer.isColumnViewChanged();
    			boolean columnInfoChanged = jvxContainer.isColumnInfoChanged();
    			
    			if (firstInitializeContainer
    				|| columnViewChanged
    				|| columnInfoChanged)
    			{
    				firstInitializeContainer = false;
    				
   					jvxContainer.initializeProperties();
    			}
   				jvxContainer.initializeItems();

   				SortDefinition sort = dataBook.getSort();
   				
   				if (sort != lastSort)
   				{
   				    lastSort = sort;
   				
           	        for (String column : getColumnView().getColumnNames())
           	        {
           	            resource.setColumnIcon(column, null);
           	        }
           	        
                    if (sort != null)
                    {
                        String[] cols = sort.getColumns();
                        boolean[] asc = sort.isAscending();
                        
                        for (int i = 0; i < cols.length; i++)
                        {
                            String column = cols[i];
                            boolean isAscending = i >= asc.length || asc[i];
                            
                            if (isAscending)
                            {
                                resource.setColumnIcon(column, thrIconAsc);
                            }
                            else
                            {
                                resource.setColumnIcon(column, thrIconDesc);
                            }
                        }
                    }
   				}
    		}
    		catch (ModelException e)
    		{
    			ExceptionHandler.raise(e);
    		}
			finally
			{
				dataChanged = false;
                bIgnoreEvent = false;
			}
    	} 
	}	
		
	//ITable

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (bFirstNotifyRepaintCall
			&& !bIgnoreEvent
			&& !bEditingStarted
			&& isAttached) // Check additionally if editing is started, to prevent immediate closing editor
		{
			bFirstNotifyRepaintCall = false;
			getFactory().invokeLater(this);
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public void startEditing()
	{
        getFactory().invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                bIgnoreEvent = true;
                try
                {
                    if (isEditable()
                        && dataBook != null
                        && dataBook.isUpdateAllowed())
                    {
                        int selectedRow = dataBook.getSelectedRow();
                        Integer itemId = Integer.valueOf(selectedRow);
                        String propertyId = dataBook.getSelectedColumn();
                        
                        if (propertyId == null)
                        {
                            Iterator it = jvxContainer.getContainerPropertyIds().iterator();
                            
                            if (it.hasNext())
                            {
                                propertyId = (String)it.next();
                                dataBook.setSelectedColumn(propertyId);
                            }
                        }
                        
                        if (propertyId != null)
                        {
                            ColumnDefinition columnDef = dataBook.getRowDefinition().getColumnDefinition(propertyId);
                            boolean isEditable = !columnDef.isReadOnly();
                            if (isEditable && dataBook.getReadOnlyChecker() != null)
                            {
                                try
                                {
                                    isEditable = !dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, propertyId, selectedRow, -1);
                                }
                                catch (Throwable pTh)
                                {
                                    // Ignore
                                }
                            }

                            if (isEditable)
                            {
                                ICellEditor cellEditor = getCellEditor(propertyId);
                                
                                if (cellEditor instanceof ICellRenderer)
                                {
                                    // ensure isDirectCellEditor gets the correct data.
                                    ((ICellRenderer)cellEditor).getCellRendererComponent(null, dataBook, selectedRow, dataBook.getDataRow(selectedRow), propertyId, false, false);
                                }
                                if (cellEditor == null || !cellEditor.isDirectCellEditor())
                                {
                                    setCellEditorHandler(itemId, propertyId);
                                }
                            }
                        }
                    }
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
        });
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		if (cellEditorHandler != null)
		{
			bEditingStarted = false;
			
			Integer itemId = editItemId;
			String propertyId = editPropertyId;
			
			cleanCellEditorHandler();
			
			JVxContainerProperty property = jvxContainer.getContainerProperty(itemId, propertyId);
			property.setValue(getComponent(itemId, propertyId, null, property.getStyle()));
			jvxContainer.initializeOnlyVisibleItems();
            dataChanged = false;  // reset dataChanged, as we updated the table
		}		
	}	
		
	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		if (bEditingStarted)
		{
        	if (cellEditorHandler != null)
        	{
        		bEditingStarted = false;
        		
        		cellEditorHandler.saveEditing();
        	}
		}
		
		cancelEditing();
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
	
	    	
			if (isAttached)
			{
		    	try
				{
	    			initColumnHeaders();
				}
				catch (Exception e)
				{
					// Do Nothing
				}
			}
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
	public boolean isMouseEventOnSelectedCell()
	{
		return true;
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
        
        resource.setItemDescriptionGenerator((cellToolTip == null) ? null : this);
    }
	
	//ICellEditorListener
    
	/**
	 * {@inheritDoc}
	 */
	public void editingStarted() 
	{
		try
		{
			bEditingStarted = true; // first set bEditingStarted true, to prevent events on update.

			IDataRow oldDataRow = dataBook.createDataRow(null);
			
			dataBook.update();
			
			if (cellEditorHandler == null
			    || !oldDataRow.equals(dataBook, new String[] {cellEditorHandler.getColumnName()})) // Only if value is changed, cancel editing.
			{
				bEditingStarted = false;
				notifyRepaint();
			}
		}
		catch (ModelException e)
		{
			bEditingStarted = false;
			notifyRepaint();
			
			ExceptionHandler.raise(e);
		}
	}   
	
	/**
	 * {@inheritDoc}
	 */
	public void editingComplete(String pCompleteType) 
	{		
		if (pCompleteType == ICellEditorListener.ESCAPE_KEY)
		{
			if (cellEditorHandler != null)
			{
				try
				{	
					cellEditorHandler.cancelEditing();
					cancelEditing();
				}
				catch (ModelException e)
				{
					ExceptionHandler.raise(e);
				}
			}
		}
		else
		{
		 // bIgnoreEvent may not be true, as for some reason other changes in the same row will not be shown.
//			boolean beforeIgnore = bIgnoreEvent;
			try
			{
			    // if the current editor is from additional data row, DO NOT IGNORE, as the data has to be fully reloaded. 
//				if (dataBook == null || !dataBook.isAdditionalDataRowVisible() || dataBook.getSelectedRow() > 0)
//				{
//					bIgnoreEvent = true;
//				}

				saveEditing();
			
				getFactory().getUI().getShortcutHandler().setTargetComponent(resource);
				
				if (pCompleteType == ICellEditorListener.ENTER_KEY)
				{
					if (selectNextColumn())
					{
                        startEditing();
					}
				}
				else if (pCompleteType == ICellEditorListener.SHIFT_ENTER_KEY)
				{
					if (selectPreviousColumn())
					{
                        startEditing();
					}
				}
				else if (pCompleteType == ICellEditorListener.TAB_KEY)
				{
					selectNextColumn();
				}
				else if (pCompleteType == ICellEditorListener.SHIFT_TAB_KEY)
				{
					selectPreviousColumn();
				}
			}
			catch (ModelException e)
			{
				cancelEditing();
				
				ExceptionHandler.raise(e);
			}
//			finally
//			{
//				bIgnoreEvent = beforeIgnore;
//			}
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
    public IDataBook getDataBook()
    {
    	return dataBook;
    }
	
	/**
	 * {@inheritDoc}
	 */
    public void setDataBook(IDataBook pDataBook)
    {
		if (dataBook != null)
		{
            dataBook.removeControl(this);

	        // It has to be this order to avoid exceptions in the default models.
		    uninstallTable();
		    
			jvxContainer = new JVxContainer(this);

			resource.setContainerDataSource(null);
	        
    		if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.removeRowDefinition(dataBook.getRowDefinition());
    		}
		}
    		
		dataBook = pDataBook;
    		
        if (dataBook != null)
        {
            dataBook.addControl(this);
            
            installTable();
        }
    }

	/**
	 * {@inheritDoc}
	 */
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
    public void setColumnView(ColumnView pColumnView)
    {
    	if (columnView != pColumnView)
    	{
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.removeRowDefinition(dataBook.getRowDefinition());
    		}
    		
        	columnView = pColumnView;
    		
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.addRowDefinition(dataBook.getRowDefinition());
    		}
    		
    		firstInitializeContainer = true;
    		
    		notifyRepaint();
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
	public boolean isTableHeaderVisible()
	{
		return resource.getColumnHeaderMode() != ColumnHeaderMode.HIDDEN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		if (pTableHeaderVisible)
		{
			resource.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		}
		else
		{
			resource.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isSortOnHeaderEnabled()
	{
		return bSortOnHeader;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
	    bSortOnHeader = pSortOnHeaderEnabled;
	    
		if (pSortOnHeaderEnabled)
		{
	        resource.removeHeaderClickListener(this);
			resource.addHeaderClickListener(this);
		}
		else
		{
			resource.removeHeaderClickListener(this);
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoResize()
	{
		return bAutoResize;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoResize(boolean pAutoResize)
	{
		bAutoResize = pAutoResize;
	}	
	
	/**
	 * {@inheritDoc}
	 */
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
    public void setRowHeight(int pRowHeight)
	{
    	rowHeight = pRowHeight;
    	
        dataChanged = true;
        notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
    public int getMinRowHeight()
	{
		return minRowHeight;
	}

	/**
	 * {@inheritDoc}
	 */
    public void setMinRowHeight(int pMinRowHeight)
	{
    	minRowHeight = pMinRowHeight;
        
        dataChanged = true;
        notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
    public int getMaxRowHeight()
	{
		return maxRowHeight;
	}

	/**
	 * {@inheritDoc}
	 */
    public void setMaxRowHeight(int pMaxRowHeight)
	{
    	maxRowHeight = pMaxRowHeight;
        
        dataChanged = true;
        notifyRepaint();
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getEnterNavigationMode()
	{
		return enterNavigationMode;
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setEnterNavigationMode(int pNavigationMode)
	{
    	enterNavigationMode = pNavigationMode;
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getTabNavigationMode()
	{
		return tabNavigationMode;
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setTabNavigationMode(int pNavigationMode)
	{
    	tabNavigationMode = pNavigationMode;
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
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowSelection()
	{
		return showSelection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowSelection(boolean pShowSelection)
	{
		showSelection = pShowSelection;
		
		if (resource.isSelectable() != showSelection)
		{
			resource.setSelectable(showSelection);
		}
		
		if (showSelection)
		{
			if (dataBook != null)
			{
				try 
				{
					jvxContainer.setSelectedRow(false);
				}
				catch (Exception e) 
				{
				    // Ignore, we just wanted to show the selection again, if possible
//					ExceptionHandler.raise(e);
				}
			}
		}
		else
		{
			resource.select(resource.getNullSelectionItemId()); // unselect table row.
		}
	}
	
	/**
	 * Data in databook was changed.
	 */
	public void setDataChanged() 
	{
		dataChanged = true;
	}

    /**
     * Data in databook was changed.
     */
    public void setDataChangedReload() 
    {
        dataChanged = true;
        resource.reloadHappened = true;
    }

	/**
	 * True, if delete or insert occured.
	 * 
	 * @return True, if delete or insert occured.
	 */
	public boolean isDataChanged()
	{
		return dataChanged;
	}
	
	/**
	 * Gets if showing the focus rect or not.
	 * 
	 * @return showing the focus rect or not.
	 */
	public boolean isShowFocusRect()
	{
		return showFocusRect;
	}

	/**
	 * Sets if showing the focus rect or not.
	 * 
	 * @param pShowFocusRect showing the focus rect or not.
	 */
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		showFocusRect = pShowFocusRect;
	}	

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowVerticalLines()
	{
		return showVerticalLines;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		showVerticalLines = pShowVerticalLines;
		
		if (pShowVerticalLines) 
		{
			removeInternStyleName("v-table-cell-content-vborderhidden");
		}
		else
		{
			addInternStyleName("v-table-cell-content-vborderhidden");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isShowHorizontalLines()
	{
		return showHorizontalLines;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		showHorizontalLines = pShowHorizontalLines;
		
		if (pShowHorizontalLines) 
		{
			addInternStyleName("v-table-cell-content-hborder");
		}
		else
		{
			removeInternStyleName("v-table-cell-content-hborder");
		}		
	}	
		
	//Click Listener
	
	/**
	 * {@inheritDoc}
	 */
	public void itemClick(ItemClickEvent pEvent)
	{	
		if (bIgnoreEvent)
		{
			return;
		}
        bIgnoreEvent = true;
		try 
		{			
			if (cellEditorHandler != null)
			{
				editingComplete(ICellEditorListener.FOCUS_LOST);
			}

			Integer itemId = (Integer)pEvent.getItemId();
			String propertyId = (String)pEvent.getPropertyId();
			int rowIndex = jvxContainer.indexOfId(itemId);
			
			boolean rowChanged = rowIndex != dataBook.getSelectedRow();
			boolean columnChanged = !CommonUtil.equals(propertyId, dataBook.getSelectedColumn());
			boolean dChanged = isDataChanged();
			
            dataBook.setSelectedRow(rowIndex); // Always try to select the row, to allow the databook to dispatch selected event again, if wanted.
            dataBook.setSelectedColumn(propertyId);

			if (rowChanged || columnChanged)
			{
				jvxContainer.clearPropertyCache();
				
				if ((dataBook.getSort() != null && dataBook instanceof MemDataBook && ((MemDataBook)dataBook).isSortDataRowOnSave())
				        || resource.reloadHappened
				        || (!dChanged && isDataChanged()))
				{
				    dChanged = isDataChanged();
				}
				
				if (rowChanged && dChanged)
				{
                    jvxContainer.initializeOnlyVisibleItems();
					dataChanged = false;  // reset dataChanged, as we updated the table
				}
				else
				{
					dataChanged = dChanged; // remember DataChanges, but ignore this selection change. 
				}
			}
			
			resource.select(itemId);
			
			if (pEvent.getButton() == MouseButton.LEFT) // Only the left button should be used to edit
			{
				ICellEditor cellEditor = getCellEditor(propertyId);
				
				if ((cellEditor instanceof IInplaceCellEditor
				        && ((IInplaceCellEditor)cellEditor).getPreferredEditorMode() == IInplaceCellEditor.SINGLE_CLICK)
			        || pEvent.isDoubleClick())
    			{
					startEditing();
    			}
			}

			getFactory().invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    dispatchMouseClickedEvent(pEvent);
                }
            });
            bIgnoreEvent = false;
		}
		catch (Exception e)
		{
			bIgnoreEvent = false;
			notifyRepaint();
			
			ExceptionHandler.raise(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueChange(ValueChangeEvent pEvent)
	{
		if (bIgnoreEvent)
		{
			return;
		}
        bIgnoreEvent = true;
		try 
		{			
			editingComplete(ICellEditorListener.FOCUS_LOST);
	
			Integer itemId = (Integer)pEvent.getProperty().getValue();

			if (itemId != null)
			{
				dataBook.setSelectedRow(itemId.intValue());
				jvxContainer.setSelectedRow(false);
			}
			else
			{
				dataBook.setSelectedRow(-1);
			}

			jvxContainer.clearPropertyCache();
            if ((isDataChanged() && dataBook.getSort() != null && dataBook instanceof MemDataBook && ((MemDataBook)dataBook).isSortDataRowOnSave())
                    || resource.reloadHappened)
            {
                jvxContainer.initializeOnlyVisibleItems();
                dataChanged = false;  // reset dataChanged, as we updated the table
			}
            bIgnoreEvent = false;
		}
		catch (ModelException exc)
		{
			bIgnoreEvent = false;
			notifyRepaint();
			ExceptionHandler.raise(exc);
		}
	}
	
	// Header Click Listener

	/**
	 * Sets the clicked column header as sort column in the databook.
	 * 
	 * @param pEvent HeaderClickEvent
	 */
	public void headerClick(HeaderClickEvent pEvent)
	{
	    //shouldn't be possible because listener is removed if not sortable, but to be save
	    if (!bSortOnHeader)
	    {
	        return;
	    }
	    
		// PropertId is ColumnName
		String columnName = (String) pEvent.getPropertyId();
		
		try
		{
	        dataBook.setSelectedColumn(columnName);
	        
    		if (!dataBook.getRowDefinition().getColumnDefinition(columnName).isSortable())
    		{
    		    return;
    		}
		}
		catch (ModelException me)
		{
		    ExceptionHandler.raise(me);
		}
		
		SortDefinition sort = dataBook.getSort();
		
		try
		{
			if ((sort != null && sort.getColumns().length > 0) && (pEvent.isShiftKey() || pEvent.isCtrlKey()))
			{
				String[] sortColumns = sort.getColumns();
				boolean[] ascending = sort.isAscending();
				int index = ArrayUtil.indexOf(sortColumns, columnName);
				
				if (index < 0)
				{
					dataBook.setSort(new SortDefinition(ArrayUtil.add(sortColumns, columnName), ArrayUtil.add(ascending, true)));
				}
				else if (ascending.length <= index)
				{
					boolean[] asc = new boolean[sortColumns.length];
					System.arraycopy(ascending, 0, asc, 0, ascending.length);
					asc[index] = false; 
					dataBook.setSort(new SortDefinition(sortColumns, asc));
				}
				else if (ascending[index])
				{
					ascending[index] = false; 
					dataBook.setSort(new SortDefinition(sortColumns, ascending));
				}
				else if (sortColumns.length > 1)
				{
					dataBook.setSort(new SortDefinition(ArrayUtil.remove(sortColumns, index), ArrayUtil.remove(ascending, index)));
				}
				else
				{
					dataBook.setSort(null);
				}
			}
			else
			{
				if (sort == null || sort.getColumns().length != 1 || !columnName.equals(sort.getColumns()[0]))
				{
					dataBook.setSort(new SortDefinition(new String[] {columnName}, new boolean[] {true}));
				}
				else if (sort.isAscending().length == 0 || sort.isAscending()[0])
				{
					dataBook.setSort(new SortDefinition(new String[] {columnName}, new boolean[] {false}));
				}
				else
				{
					dataBook.setSort(null);
				}
			}

			firstInitializeContainer = true;

			notifyRepaint();
		}
		catch (ModelException pModelException)
		{
			ExceptionHandler.raise(pModelException);
		}
	}	

	//ColumnReorderListener
	
	/**
	 * {@inheritDoc}
	 */
	public void columnReorder(Table.ColumnReorderEvent pEvent)
	{
	    boolean bOldIgnoreEvent = bIgnoreEvent;
	    
	    bIgnoreEvent = true;
	    try
	    {
    	    Object[] oVisCols = resource.getVisibleColumns();
    	    
    	    if (oVisCols != null)
    	    {
    	        String[] liColumns = new String[oVisCols.length];
    	        System.arraycopy(oVisCols, 0, liColumns, 0, oVisCols.length);
    
        	    getColumnView().setColumnNames(liColumns);
    	    }
	    }
	    finally
	    {
	        bIgnoreEvent = bOldIgnoreEvent;
	    }
	}
	
	//StyleGenerator
	
	/**
	 * {@inheritDoc}
	 */
	public String getStyle(Table pSource, Object pItemId, Object pPropertyId)
	{
		if (pPropertyId == null || pItemId == null) 
		{
			return null;
		}

		try
		{
			if (dataBook.getDataRow(((Integer)pItemId).intValue()).isDeleting())
			{
				return "deleting";
			}
		}
		catch (ModelException e)
		{
        	LoggerFactory.getInstance(getClass()).debug(e);
		}

		return null;
	}
	
	// ItemDescriptionGenerator
	
	/**
	 * {@inheritDoc}
	 */
	public String generateDescription(Component pSource, Object pItemId, Object pPropertyId)
    {
        String description = null;
        
        if (cellToolTip != null)
        {
            try
            {
                int rowNum = ((Integer)pItemId).intValue();
                String columnName = (String)pPropertyId;
                
                description = cellToolTip.getToolTipText(dataBook,
                                                         dataBook.getDataPage(),
                                                         dataBook.getDataRow(rowNum),
                                                         columnName,
                                                         rowNum,
                                                         getColumnView().getColumnNameIndex(columnName));
            }
            catch (Throwable e) 
            {
            	LoggerFactory.getInstance(getClass()).debug(e);
            }
        }
        
        return description;
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
		VaadinUtil.setComponentHeight(resource, DEFAULT_TABLE_HEIGHT, Unit.PIXELS);
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * This method refreshes only the currently visible data.
	 * <p>
	 * Let me explain...in short, the refreshing mechanism of the Table is
	 * simply broken, just broken. Fullstop.
	 */
	public void refreshDataOnly()
	{
    	if (!bIgnoreEvent 
    		&& dataBook != null
    		&& dataBook.isOpen())
    	{
			jvxContainer.clearPropertyCache();
			jvxContainer.initializeOnlyVisibleItems();
			dataChanged = false;
    	}
	}
	
	/**
	 * If the given column (itemId, propertyId) is a cellEditorHandler.
	 * 
	 * @param pItemId the item id. Row number.
	 * @param pPropertyId the property id. Column name.
	 * @return true if the given column is editable.
	 */
	public boolean isEditableProperty(Integer pItemId, String pPropertyId)
	{
		if (cellEditorHandler != null && pItemId != null && pPropertyId != null)
		{
			if (pItemId.equals(editItemId) && pPropertyId.equals(editPropertyId))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sets the cellEditorHandler and the set edit itemId and propertyId to null.
	 */
	private void cleanCellEditorHandler()
	{
		cellEditorHandler = null;
		editItemId = null;
		editPropertyId = null;
	}
	

	/**
	 * Sets the cellEditorHandler for the given column (itemId, propertyId).
	 * 
	 * @param pItemId the item id. Row number.
	 * @param pPropertyId the property id. Column name.
	 * @throws ModelException 
	 */
	private void setCellEditorHandler(Integer pItemId, String pPropertyId) throws ModelException
	{
 		ICellEditor cellEditor = getCellEditor(pPropertyId);
 		
 		cellEditorHandler = cellEditor.createCellEditorHandler(this, dataBook, pPropertyId);
 		cellEditorHandler.cancelEditing();
 		if (cellEditorHandler instanceof IVaadinCellEditorHandler)
 		{
 			((IVaadinCellEditorHandler)cellEditorHandler).setWidth(100f, Unit.PERCENTAGE);
 		}
 		else
 		{
 			VaadinUtil.setComponentWidth(cellEditorHandler.getCellEditorComponent(), 100f, Unit.PERCENTAGE);
 		}
 		
 		editItemId = pItemId;
 		editPropertyId = pPropertyId;

        JVxContainerProperty property = jvxContainer.getContainerProperty(editItemId, editPropertyId);
        property.setValue(getComponent(editItemId, editPropertyId, null, property.getStyle()));
 		
		jvxContainer.initializeOnlyVisibleItems();
		dataChanged = false;
	}
	
	/**
	 * Returns the component for the given column (itemId, propertyId).
	 * If the availableComponent exists, the availableComponent is returned with the new value.
	 * 
	 * @param pItemId the item id. Row number.
	 * @param pPropertyId the property id. Column name.
	 * @param pAvailableComponent the available component.
     * @param pStyleInfo the style info.
	 * @return the component from the cellRendere
	 */
	public Object getComponent(Integer pItemId, String pPropertyId, Object pAvailableComponent, StyleInfo pStyleInfo)
	{
		Object component = "";
        HorizontalLayout wrapperComponent = null;
		
		try
		{
			if (isEditableProperty(pItemId, pPropertyId))
			{
				component = cellEditorHandler.getCellEditorComponent();
				
				if (component instanceof Focusable)
				{
					((Focusable) component).focus();
				}
			}
			else
			{
				ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(pPropertyId);
				
				ICellRenderer<Component> cellRenderer = columnDefinition.getDataType().getCellRenderer();
				
				if (cellRenderer == null)
				{
					ICellEditor cellEditor = (ICellEditor)getCellEditor(columnDefinition.getDataType());
					
					if (cellEditor instanceof IResource) // if the ICellEditor has a resource, the resource has to be an ICellEditor
					{
						cellEditor = (ICellEditor) ((IResource) cellEditor).getResource();
					}
					if (cellEditor instanceof ICellRenderer)
					{
						cellRenderer = (ICellRenderer)cellEditor;
					}
				}
				
				if (cellRenderer != null) 
				{				
					IDataRow dataRow = dataBook.getDataRow(pItemId.intValue());
					
					if (dataRow != null)
					{
						Component availableComponent = null;
						
						if (pAvailableComponent instanceof Component)
						{
							availableComponent = (Component)pAvailableComponent;
							if (availableComponent.isAttached())
							{
							    availableComponent = null;
							}
							
							if (availableComponent instanceof HorizontalLayout)
							{
							    wrapperComponent = (HorizontalLayout)availableComponent;
							    
							    availableComponent = wrapperComponent.getComponent(wrapperComponent.getComponentCount() - 1);
							}
						}
						
						component = cellRenderer.getCellRendererComponent(availableComponent,
																		  dataBook, 
																		  pItemId.intValue(), 
																	  	  dataRow,
																	  	  pPropertyId,
																	  	  isEditable(),
																	  	  false);
					}
				}		
			}
		}
		catch (Exception e)
		{
			// Nothing to do. Return empty string.
		}
		
		if (pStyleInfo != null)
		{
    		IImage image = pStyleInfo.getImage();
    		int leftIndent = pStyleInfo.getLeftIndent();
    		
    		if (image != null || leftIndent > 0)
    		{
                Image imageComponent = null;
    
                if (wrapperComponent == null)
    		    {
    		        wrapperComponent = new HorizontalLayout();
                    wrapperComponent.setStyleName("column-wrapper");
    		        wrapperComponent.setSpacing(true);
                    wrapperComponent.setMargin(false);
                    wrapperComponent.setSizeFull();
                    
                    wrapperComponent.addLayoutClickListener(new LayoutClickListener() 
                    {
                        public void layoutClick(LayoutClickEvent pEvent)
                        {
                            if (pEvent.getChildComponent() == null)
                            {
                                try
                                {
                                    dataBook.setSelectedRow(pItemId.intValue());
                                }
                                catch (ModelException e)
                                {
                                    // Ignore
                                }
                            }
                            else if (!(pEvent.getChildComponent() instanceof Image))
                            {
                                itemClick(new ItemClickEvent(pEvent.getChildComponent(), null, pItemId, pPropertyId, pEvent.getMouseEventDetails()));
                            }
                        }
                    });
    		    }
                else if (wrapperComponent.getComponentCount() > 0 && wrapperComponent.getComponent(0) instanceof Image)
                {
                    imageComponent = (Image)wrapperComponent.getComponent(0);
                }
    		    wrapperComponent.removeAllComponents();
    		    
                CssExtension cssExtension = VaadinUtil.getCssExtension(wrapperComponent);
                if (leftIndent <= 0)
                {
                    cssExtension.removeAttribute("padding-left");
                }
                else
                {
                    cssExtension.addAttribute("padding-left", leftIndent + "px");
                }
    		    
    		    if (image != null)
    		    {
    		        if (imageComponent == null)
    		        {
    		            imageComponent = new Image();
                        imageComponent.addClickListener(new ClickListener()
                        {
                            public void click(ClickEvent event)
                            {
                                try
                                {
                                    dataBook.setSelectedRow(pItemId.intValue());
                                }
                                catch (ModelException e)
                                {
                                    // Ignore
                                }
                            }
                        });
    		        }
    		        if (image.getResource() instanceof FontIcon)
    		        {
    		            imageComponent.setIcon((Resource)image.getResource());
    		            imageComponent.setSource(null);
    		        }
    		        else
    		        {
    		            imageComponent.setIcon(null);
    		            imageComponent.setSource((Resource)image.getResource());
    		        }
    		        if (image.getWidth() >= 0)
    		        {
    		            imageComponent.setWidth(image.getWidth(), Unit.PIXELS);
    		        }
    		        if (image.getHeight() >= 0)
    		        {
    		            imageComponent.setHeight(image.getHeight(), Unit.PIXELS);
    		        }
    		        
    		        wrapperComponent.addComponent(imageComponent);
    		        wrapperComponent.setExpandRatio(imageComponent, 0);
                    wrapperComponent.setComponentAlignment(imageComponent, Alignment.MIDDLE_LEFT);
    		    }
    		    if (component instanceof Component)
    		    {
    		        wrapperComponent.addComponent((Component)component);
    		        wrapperComponent.setExpandRatio((Component)component, 1);
                    wrapperComponent.setComponentAlignment((Component)component, Alignment.MIDDLE_LEFT);
    		    }
    		    
    		    component = wrapperComponent;
    		}
		}
		
		if (component instanceof com.vaadin.ui.Label && ((com.vaadin.ui.Label)component).getContentMode() == com.vaadin.shared.ui.ContentMode.TEXT)
        {
            component = ((com.vaadin.ui.Label)component).getValue();
        }
		else if (component instanceof Label && ((Label)component).getContentMode() == ContentMode.TEXT)
        {
            component = ((Label)component).getValue();
        }
        else if (component instanceof TextArea 
				|| component instanceof RichTextArea)
		{
			((Component) component).setHeight(getRowHeight() * 2 + "px");
		}
		else if (component instanceof AbstractComponent)
		{
		    AbstractComponent abstractComponent = (AbstractComponent)component;
		    
            if (abstractComponent instanceof HorizontalLayout)
            {
                HorizontalLayout wrapper = (HorizontalLayout)abstractComponent;
                abstractComponent = (AbstractComponent)wrapper.getComponent(wrapper.getComponentCount() - 1);
            }

            if (getRowHeight() != abstractComponent.getHeight())
			{
			    abstractComponent.setHeight(getRowHeight(), Unit.PIXELS);
			}
			
			VaadinUtil.getCssExtension(abstractComponent).addAttribute("line-height", getRowHeight() + "px");
				
			if (abstractComponent instanceof CssLayout)
			{
			    CssLayout cssLayout = (CssLayout)abstractComponent;
				for (int c = 0, count = cssLayout.getComponentCount(); c < count; c++)
				{
				    Component comp = cssLayout.getComponent(c);
				    
					if (comp instanceof AbstractComponent)
					{
						if (getRowHeight() != comp.getHeight())
						{
						    comp.setHeight(getRowHeight(), Unit.PIXELS);
						}
						VaadinUtil.getCssExtension((AbstractComponent)comp).addAttribute("line-height", getRowHeight() + "px");
					}
				}
			}
		}
		
		return component;
	}
	
	/**
	 * Returns the cell style for the given column (itemId, propertyId).
	 *  
	 * @param pItemId the item id. Row number.
	 * @param pPropertyId the property id. Column name.
	 * @return the cell style
	 */
	public StyleInfo getCellStyle(Integer pItemId, String pPropertyId)
	{
	    StyleInfo info = null;

		if (getCellFormatter() != null)
		{
			try 
			{
			    IDataRow dataRow = dataBook.getDataRow(pItemId.intValue());
                
                if (dataRow != null)
                {
    				ICellFormat cellFormat = getCellFormatter().getCellFormat(dataBook,
    																		  dataBook.getDataPage(),
    																		  dataRow,
    																		  pPropertyId,
    																		  pItemId.intValue(),
    																		  getColumnView().getColumnNameIndex(pPropertyId));
    				if (cellFormat != null)
    				{
    			        Map<String, String> hmpStyle = new HashMap<String, String>();
    					
    					if (cellFormat.getResource() instanceof VaadinCellFormat
    						&& ((VaadinCellFormat) cellFormat.getResource()).getAdditionalStyles() != null)
    					{
    						hmpStyle.putAll(((VaadinCellFormat)cellFormat.getResource()).getAdditionalStyles());
    					}
    	                IColor foreground = cellFormat.getForeground();
    	               
    	                if (foreground != null)
    					{
    						hmpStyle.put("color", ((VaadinColor)foreground.getResource()).getStyleValueRGB());
    					}
    					
    	                IColor background = cellFormat.getBackground();
    	
    	                if (background != null)
    					{
    						hmpStyle.put("backgroundColor", ((VaadinColor) background.getResource()).getStyleValueRGB());
    					}
    	
    	                IFont font = cellFormat.getFont();
    	
    	                if (font != null)
    					{
    						hmpStyle.putAll(((VaadinFont) font.getResource()).getStyleAttributes(true));
    					}
    	                
    	                Style style = cellFormat.getStyle();
    	                
    	                int leftIndent = cellFormat.getLeftIndent();
    	                IImage image = cellFormat.getImage();
    	                
    	                info = new StyleInfo(hmpStyle, style != null ? style.getStyleNames() : null, image, leftIndent);
    				}
                }
			} 
			catch (Throwable e) 
			{
				// Ignore the exception.
			}
			
		}
		
		return info;
	}

    /**
     * Gets the CellEditor for editing the given data type.
     * 
     * 
     * @param pDataType The DataType
     * @return the ICellEditor.
     * @throws ModelException if the column name is invalid
     */
    private ICellEditor getCellEditor(IDataType pDataType) throws ModelException
    {
    	ICellEditor cellEditor = pDataType.getCellEditor();
    	
    	if (cellEditor == null)
    	{
    		cellEditor = VaadinUtil.getDefaultCellEditor(pDataType.getTypeClass());
    	}
    	
    	return cellEditor;
    }		
	
    /**
     * Gets the cell editor for the given column.
     * 
     * @param pColumnName the column name
     * @return the cell editor
     * @throws ModelException if column doesn't exist
     */
    private ICellEditor getCellEditor(String pColumnName) throws ModelException
    {
        return getCellEditor(dataBook.getRowDefinition().getColumnDefinition(pColumnName).getDataType());
    }
    
	/**
     * Returns the vaadin table widget.
     * 
     * @return the vaadin table widget.
     */
    public TableComponent getTable()
    {
    	return resource;
    }
    
    /**
     * Initializes all the information for the column headers like name, width and alignment.
     * 
     * @throws ModelException if the initialization failed. 
     */
	public void initColumnHeaders() throws ModelException
	{
		for (String columnName : getColumnView().getColumnNames())
		{
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);
			
			ICellEditor cellEditor = (ICellEditor)getCellEditor(columnDefinition.getDataType());
			// initialize a cell editor handle, this allows GenUI Cell Editors to initialize the editor depending to the current mapped control and column.
			try
			{
				cellEditor.createCellEditorHandler(this, dataBook, columnName);
			}
			catch (Throwable ex)
			{
				// Do Nothing.
			}
		
			
			if (columnDefinition.getWidth() > 0 && !bAutoResize)
			{
				resource.setColumnWidth(columnName, columnDefinition.getWidth());
			}
			
			resource.setColumnHeader(columnName, translate(columnDefinition.getLabel()));

			if (cellEditor instanceof IStyledCellEditor)
			{
				int horizontalAlignment;
				if (cellEditor instanceof AbstractLinkedCellEditor)
				{
					horizontalAlignment = ((AbstractLinkedCellEditor)cellEditor).getDefaultHorizontalAlignment(dataBook, columnName);
				}
				else
				{
					horizontalAlignment = ((IStyledCellEditor)cellEditor).getHorizontalAlignment();
				}
				
				switch (horizontalAlignment)
				{
					case IAlignmentConstants.ALIGN_LEFT: 
					case IAlignmentConstants.ALIGN_DEFAULT: 
						resource.setColumnAlignment(columnName, Align.LEFT); 
						break;
					case IAlignmentConstants.ALIGN_CENTER: 
                    case IAlignmentConstants.ALIGN_STRETCH: 
						resource.setColumnAlignment(columnName, Align.CENTER); 
						break;
					case IAlignmentConstants.ALIGN_RIGHT: 
						resource.setColumnAlignment(columnName, Align.RIGHT); 
						break;
					default:
						throw new IllegalArgumentException("Horizontal alignment " + ((IStyledCellEditor)cellEditor).getHorizontalAlignment() + " is not supported!");
				}
			}
		}
		
		resource.setVisibleColumns((Object[])getColumnView().getColumnNames());
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
	 * Sets the focus rect for the column.
	 * 
	 * @param pItemId the row id
	 * @param pPropertyId the column id
	 */
//	private void setColumnActive(Integer pItemId, String pPropertyId)
//	{
//		if (showFocusRect)
//		{
//			AbstractComponent component = (AbstractComponent) ((Property) resource.getContainerProperty(pItemId, pPropertyId)).getValue();
//			
//			CssExtension cssExtension = VaadinUtil.getCssExtension(component);
//			
//			if (cssExtension != null)
//			{
////				cssExtension.addAttributes(columnRectCssAttributes);			
//			}		
//		}
//	}
	
	/**
	 * Removes the focus rect from the column.
	 * 
	 * @param pItemId the row id
	 * @param pPropertyId the column id
	 */
//	private void setColumnInActive(Integer pItemId, String pPropertyId)
//	{
//		if (showFocusRect)
//		{
//			AbstractComponent component = (AbstractComponent) ((Property) resource.getContainerProperty(pItemId, pPropertyId)).getValue();
//			
//			CssExtension cssExtension = VaadinUtil.getCssExtension(component);
//			
//			if (cssExtension != null)
//			{
////				cssExtension.removeAttributes(columnRectCssAttributes);	
//			}	
//		}
//	}	
	
	/**
	 * Selects the next columns in the dataBook. 
	 * 
	 * @return true: if startEditing should be fired.
	 */
	private boolean selectNextColumn()
	{	
		try
		{
			//int selectedRow = dataBook.getSelectedRow();
			String selectedColumn = dataBook.getSelectedColumn();

			int columnNameIndex = getColumnView().getColumnNameIndex(selectedColumn);

			if ((columnNameIndex + 1) < getColumnView().getColumnCount())
			{
				dataBook.setSelectedColumn(getColumnView().getColumnNames()[columnNameIndex + 1]);
				
				return true;
			}
			else
			{
//				{
//					dataBook.setSelectedRow(selectedRow + 1);
//					dataBook.setSelectedColumn(getColumnView().getColumnNames()[0]);
//					
//					resource.setValue(Integer.valueOf((selectedRow + 1)));
//				}
//				else
//				{
					cancelEditing();
//				}
					
				return false;
			}
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
			
			return false;
		}
	}
	
	/**
	 * Selects the previous column in the data book.
	 * 
	 * @return <code>true</code> if selection was successful, <code>false</code> otherwise
	 */
	private boolean selectPreviousColumn()
	{
		try
		{
			//int selectedRow = dataBook.getSelectedRow();
			String selectedColumn = dataBook.getSelectedColumn();

			int columnNameIndex = getColumnView().getColumnNameIndex(selectedColumn);
			
			if (columnNameIndex > 0)
			{
				dataBook.setSelectedColumn(getColumnView().getColumnNames()[columnNameIndex - 1]);
				
				return true;
			}
			else
			{
//				{
//					dataBook.setSelectedRow(selectedRow - 1);
//					dataBook.setSelectedColumn(getColumnView().getColumnNames()[getColumnView().getColumnCount() - 1]);
//					
//					resource.setValue(Integer.valueOf((selectedRow - 1)));
//				}
//				else
//				{
					cancelEditing();
//				}
					
				return false;
			}
			
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
			return false;
		}	
	}

	/**
	 * Sends the mouse click event.
	 * 
	 * @param pEvent the item click event.
	 */
	private void dispatchMouseClickedEvent(ItemClickEvent pEvent)
	{
		dispatchMouseEvent(eventMousePressed, pEvent, UIMouseEvent.MOUSE_PRESSED);
		dispatchMouseEvent(eventMouseClicked, pEvent, UIMouseEvent.MOUSE_CLICKED);
		dispatchMouseEvent(eventMouseReleased, pEvent, UIMouseEvent.MOUSE_RELEASED);
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * The <code>TableComponent</code> class handles all actions.
	 * 
	 * @author Stefan Wurm
	 */
	public static class TableComponent extends ExtendedTable 
	                                   implements IEditorComponent
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the connected table instance. */
		private VaadinTable table;
		
		/** the map for old items. */
	    private Map<String, Object> oldItemIdMapper;
	    
	    /** wether mark as Dirty is just now running. */
	    private boolean isMarkAsDirty = false;
	    
	    /** wether reload happenend. */
	    private boolean reloadHappened = false;
	    
	    /** The columnIdMapper. */
	    private KeyMapper<Object> columnIdMap;
	    
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Constructs a new <code>TableComponent</code>.
	     */
	    public TableComponent()
	    {
	        try
            {
                columnIdMap = (KeyMapper<Object>)Reflective.getValue(this, Table.class.getDeclaredField("columnIdMap"));
            }
            catch (Throwable e)
            {
                LoggerFactory.getInstance(getClass()).error(e);            
            }
	    }
	    
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Handles the key actions.
         * 
         * @param pAction the key action.
         */
        public void handleAction(Action pAction)
        {
            try
            {
                if (table.dataBook.getSelectedRow() >= 0 && table.isEditable())
                {
                    if (pAction == ShortcutHandler.ACTION_TAB)
                    {
//                      table.editingComplete(ICellEditorListener.TAB_KEY); 
                        
                        table.selectNextColumn();
                    }
                    else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
                    {
//                      table.editingComplete(ICellEditorListener.SHIFT_TAB_KEY);
                        
                        table.selectPreviousColumn();
                    }
                    else if (pAction == ShortcutHandler.ACTION_ENTER
                    		 || pAction == ShortcutHandler.ACTION_ALT_ENTER
   		     			 	 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
   		     			 	 || pAction == ShortcutHandler.ACTION_META_ENTER)
                    {
//                      table.editingComplete(ICellEditorListener.ENTER_KEY);   
                        
                        table.selectNextColumn();
                    }
                    else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
                    {
//                      table.editingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
                        
                        table.selectPreviousColumn();
                    }
                    else if (pAction == ShortcutHandler.ACTION_EDIT)
                    {
                        table.startEditing();
                    }
                    else if (pAction == ShortcutHandler.ACTION_ESCAPE)
                    {
                        table.dataBook.restoreSelectedRow();
                        table.jvxContainer.initializeOnlyVisibleItems();
                        table.dataChanged = false;
                    }
                    else if (pAction == ShortcutHandler.ACTION_DELETE)
                    {
                        table.dataBook.delete();
                        table.jvxContainer.initializeOnlyVisibleItems();
                        table.dataChanged = false;
                    }
                    else if (pAction == ShortcutHandler.ACTION_INSERT)
                    {
                        table.dataBook.insert(false);
                        table.jvxContainer.initializeOnlyVisibleItems();
                        table.dataChanged = false;
                    }
                }       
            }
            catch (Exception e)
            {
            	ExceptionHandler.raise(e);
            }
        }
	    
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void changeVariables(Object pSource, Map<String, Object> pVariables)
	    {			
			if (oldItemIdMapper != null
				&& pVariables.containsKey("clickEvent"))
			{
				String itemKey = (String) pVariables.get("clickedKey");
				
				if (itemIdMapper.get(itemKey) == null)
				{
					Object oldItemId = oldItemIdMapper.get(itemKey);
					String newItemKey = itemIdMapper.key(oldItemId);
					
					if (newItemKey != null)
					{
						pVariables.put("clickedKey", newItemKey);
					}
				}
			}

			int oldPageLength = getPageLength();
			
	    	super.changeVariables(pSource, pVariables);
	    	
	    	if (pVariables.containsKey("pagelength") && getPageLength() != oldPageLength) 
	    	{
	    	    try
	    	    {
	    	        table.jvxContainer.setSelectedRow(true);
                }
                catch (ModelException e)
                {
                    // Ignore
                }
	        }
	    	else if (pVariables.containsKey("columnResizeEventColumn")) 
	    	{
	            Object cid = pVariables.get("columnResizeEventColumn");
	            if (cid != null) 
	            {
	                String propertyId = (String)columnIdMap.get(cid.toString());

	                Object newWidth = pVariables.get("columnResizeEventCurr");
	                if (newWidth != null) 
	                {
	                    int width = Integer.parseInt(newWidth.toString());
	                    
	                    table.bIgnoreEvent = true;
	                    try
	                    {
	                        table.getDataBook().getRowDefinition().getColumnDefinition(propertyId).setWidth(width);
	                    }
	                    catch (ModelException ex)
	                    {
	                        // ignore
	                    }
	                    finally
	                    {
	                        table.bIgnoreEvent = false;
	                    }
	                }
	            }
	        }
	    }

	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public boolean removeAllItems()
	    {
	    	storeCurrentItemIdMapper();
	    	
	    	return super.removeAllItems();
	    }
	    

	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public boolean removeItem(Object pItemId)
	    {
	    	storeCurrentItemIdMapper();
	    	
	    	return super.removeItem(pItemId);
	    }
	    
	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public void setContainerDataSource(Container pNewDataSource)
	    {
	    	storeCurrentItemIdMapper();
	    	
	    	super.setContainerDataSource(pNewDataSource);
	    }
	    
	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public void containerItemSetChange(Container.ItemSetChangeEvent event)
	    {
	    	storeCurrentItemIdMapper();
	    	
	    	super.containerItemSetChange(event);
	    }
	    

	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public void paintContent(PaintTarget pTarget) throws PaintException
	    {
	        if (reloadHappened)
	        {
	            reloadHappened = false;
	            alwaysRecalculateColumnWidths = true;
	        }
	        
	    	super.paintContent(pTarget);
	    	
            alwaysRecalculateColumnWidths = false;
	    	
	    	Object[] visibleColumns = getVisibleColumns();

	    	if (visibleColumns != null
	    		&& visibleColumns.length > 0)
			{
	    		// Prepare column infos (readonly, nullable column infos)
	    		pTarget.startTag("columninfo");
	    		
	    		try
	    		{
		    		boolean readOnly = !table.dataBook.isUpdateEnabled() || table.dataBook.isReadOnly();
		    		
		    		for (Object propertyId : visibleColumns)
		    		{
		    			String columnId = columnIdMap.key(propertyId);
		    			
		    			if (columnId != null)
		            	{
		    				ColumnDefinition cd = table.dataBook.getRowDefinition().getColumnDefinition((String)propertyId);
		    				
		    				pTarget.startTag("column");
			            	pTarget.addAttribute("cid", columnId);
			            	pTarget.addAttribute("readonly", readOnly || cd.isReadOnly());
			            	pTarget.addAttribute("nullable", cd.isNullable());
			            	pTarget.addAttribute("name", (String)propertyId);
			            	pTarget.endTag("column");
		            	}
		    		}
	    		}
	    		catch (Exception ex)
	    		{
	    			// Do nothing!
	    		}
	    		
		    	pTarget.endTag("columninfo");
		    	
		    	// Prepare row styles
		    	Collection<?> itemIds = getVisibleItemIds();
		    	
		    	if (itemIds != null)
		    	{
		    		List<AbstractMap.SimpleEntry<String, String>> styleMappings = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
		    		List<String> classMappings = new ArrayList<String>();
		    
		    		pTarget.startTag("styles");
		    		
		    		for (Object itemId : itemIds)
		    		{
		    			Map<String, StyleInfo> cellStyles = new HashMap<String, StyleInfo>();
		    			
		    			for (Object propertyId : visibleColumns)
			    		{
		    				Property property = getContainerDataSource().getContainerProperty(itemId, propertyId);
		    				
		    				if (property != null
		    					&& property instanceof JVxContainerProperty
		    					&& ((JVxContainerProperty) property).getStyle() != null)
		    				{
		    					cellStyles.put(columnIdMap.key(propertyId), ((JVxContainerProperty) property).getStyle());
		    				}
			    		}
		    			
		    			if (cellStyles.size() > 0)
		    			{
			    			pTarget.startTag("tr");
					    	pTarget.addAttribute("key", itemIdMapper.key(itemId));
					    	
					    	for (String columnId : cellStyles.keySet())
					    	{
					    		pTarget.startTag("cell");
                                pTarget.addAttribute("cid", columnId);
					    		pTarget.startTag("styles");
						    	
						    	StyleInfo sinfo = cellStyles.get(columnId);
						    	
						    	Map<String, String> styles = sinfo.getStyles();
						    	
						    	for (String style : styles.keySet())
						    	{
						    		AbstractMap.SimpleEntry<String, String> styleMapping = new AbstractMap.SimpleEntry<String, String>(style, styles.get(style));
						    		int styleMappingId = styleMappings.indexOf(styleMapping);
						    		
						    		if (styleMappingId < 0)
						    		{
						    			styleMappings.add(styleMapping);
						    			styleMappingId = styleMappings.size() - 1;
						    		}
						    		
						    		pTarget.addText(String.valueOf(styleMappingId));
						    	}

						    	pTarget.endTag("styles");
						    	pTarget.startTag("classes");
                                
                                String classes = StringUtil.concat(" ", sinfo.getClasses());

                                if (!StringUtil.isEmpty(classes))
                                {
                                    int classMappingId = classMappings.indexOf(classes);
                                    
                                    if (classMappingId < 0)
                                    {
                                        classMappings.add(classes);
                                        classMappingId = classMappings.size() - 1;
                                    }
                                    
                                    pTarget.addText(String.valueOf(classMappingId));
                                }
                                
                                pTarget.endTag("classes");
						    	pTarget.endTag("cell");
					    	}
					    	
					    	pTarget.endTag("tr");
		    			}
		    		}
		    		
		    		pTarget.endTag("styles");
		    		
		    		// Append style mappings
		    		if (styleMappings.size() > 0)
		    		{
		    			pTarget.startTag("styleMappings");
		    			
		    			for (int i = 0, cnt = styleMappings.size(); i < cnt; i++)
		    			{
		    				AbstractMap.SimpleEntry<String, String> styleMapping = styleMappings.get(i);
		    				
		    				if (styleMapping != null)
		    				{
			    				pTarget.startTag("style");
			    				pTarget.addAttribute("sid", String.valueOf(i));
			    				pTarget.addAttribute("k", styleMapping.getKey());
			    				pTarget.addAttribute("v", styleMapping.getValue());
			    				pTarget.endTag("style");
		    				}
		    			}
		    			
		    			pTarget.endTag("styleMappings");
		    		}
		    		
		    		if (classMappings.size() > 0)
		    		{
                        pTarget.startTag("classMappings");
                        
                        for (int i = 0, cnt = classMappings.size(); i < cnt; i++)
                        {
                            pTarget.startTag("class");
                            pTarget.addAttribute("cid", String.valueOf(i));
                            pTarget.addAttribute("v", classMappings.get(i).split(" "));
                            pTarget.endTag("class");
                        }
                        
                        pTarget.endTag("classMappings");
		    		}
		    	}
	    	}
	    	
	    	pTarget.addAttribute("rowheight", table.getRowHeight());
	    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
        /**
         * Stores the current item ids in the map.
         */
	    private void storeCurrentItemIdMapper()
	    {
	    	if (items instanceof Container.Indexed)
	    	{
	    		if (oldItemIdMapper == null)
	    		{
	    			oldItemIdMapper = new HashMap<String, Object>();
	    		}
	    		else
	    		{
	    			oldItemIdMapper.clear();
	    		}
	    		
	    		Collection<?> itemIds = items.getItemIds();
	    		
	    		if (itemIds != null)
	    		{
	    			for (Object itemId : itemIds)
	    			{
	    				String itemIdKey = itemIdMapper.key(itemId);
	    				
	    				oldItemIdMapper.put(itemIdKey, itemId);
	    			}
	    		}
	    	}
	    }
	    
		/**
		 * Handles the action from the ShortcutHandler,
		 * if the target is null. 
		 * 
		 * @param pAction the action from the ShortcutHandler.
		 */
		public void handleActionForTargetIsNull(Action pAction)
		{
		    if (pAction == ShortcutHandler.ACTION_ENTER
		    	|| pAction == ShortcutHandler.ACTION_ALT_ENTER
	     		|| pAction == ShortcutHandler.ACTION_CTRL_ENTER
	     		|| pAction == ShortcutHandler.ACTION_META_ENTER)
			{
				if (table.selectNextColumn())
				{
					table.startEditing();
				}
			}
			else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
			{
				if (table.selectPreviousColumn())
				{
					table.startEditing();
				}
			}	
			else if (pAction == ShortcutHandler.ACTION_TAB)
			{
				table.selectNextColumn();
				
				table.cancelEditing();
			}
			else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
			{
				table.selectPreviousColumn();
				
				table.cancelEditing();
			}
		}
		
		/**
		 * Gets true, when mark as dirty is just now running.
		 * @return true, when mark as dirty is just now running.
		 */
		public boolean isMarkAsDirty()
		{
			return isMarkAsDirty;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void markAsDirtyRecursive() 
		{
			isMarkAsDirty = true;
			try
			{
				super.markAsDirtyRecursive();
			}
			finally
			{
				isMarkAsDirty = false;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void refreshRenderedCells()
		{
			// For usage from the parent class.
			super.refreshRenderedCells();
		}

		/**
		 * Gets the vaadin table.
		 * @return the vaadin table.
		 */
		public VaadinTable getVaadinTable()
		{
			return table;
		}
		
	}	// TableComponent
	
	/**
	 * The <code>StyleInfo</code> class is a container for simple key/value styles and style class names.
	 * 
	 * @author René Jahn
	 */
	public static class StyleInfo
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /** the key/value styles. */
	    private Map<String, String> styles;
	    /** the class names. */
	    private String[] classes;
	    /** the image. */
	    private IImage image;
	    /** the left indent. */
	    private int leftIndent;

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Creates a new instance of <code>StyleInfo</code>.
	     * 
	     * @param pStyles the key/value styles
	     * @param pClasses the class names
         * @param pImage the image
         * @param pLeftIndent the left indent
	     */
	    public StyleInfo(Map<String, String> pStyles, String[] pClasses, IImage pImage, int pLeftIndent)
	    {
	        styles = pStyles;
	        classes = pClasses;
	        image = pImage;
	        leftIndent = pLeftIndent;
	    }
	    
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Gets the style definitions.
	     * 
	     * @return key/value pairs.
	     */
	    public Map<String, String> getStyles()
	    {
	        return styles;
	    }
	    
	    /**
	     * Gets the style class names.
	     * 
	     * @return the class names
	     */
	    public String[] getClasses()
	    {
	        return classes;
	    }
	    
        /**
         * Gets the image.
         * 
         * @return the image
         */
        public IImage getImage()
        {
            return image;
        }
        
        /**
         * Gets the left indent.
         * 
         * @return the left indent
         */
        public int getLeftIndent()
        {
            return leftIndent;
        }
        
	}  // StyleInfo

} 	// VaadinTable

