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
 * 17.04.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.ITreeControl;
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IResource;
import javax.rad.ui.control.ICellFormat;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.IVaadinCellEditorHandler;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTree;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractContainer;
import com.vaadin.v7.ui.TreeTable;

/**
 * The <code>JVxHierarchicalContainer</code> class is a container for the vaadin
 * tree to interact with the dataBook from JVx.
 * 
 * @author Stefan Wurm
 */
@SuppressWarnings("deprecation")
public class JVxHierarchicalContainer extends AbstractContainer
		implements Container.Hierarchical,
		Container.Indexed
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The item icon property id. */
	public static final String ITEM_ICON_PROPERTY_ID = "ITEM_ICON_PROPERTY_ID";
	
	/** The item lable property id. */
	public static final String ITEM_LABEL_PROPERTY_ID = "ITEM_LABEL_PROPERTY_ID";
	
	/** The {@link ILogger} for the {@link JVxHierarchicalContainer} class. */
	private static final ILogger LOGGER = LoggerFactory.getInstance(JVxHierarchicalContainer.class);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The IDataBook to be shown. */
	private IDataBook[] dataBooks = null;
	
	/** The selfjoined databooks. */
	private IDataBook selfJoinedDataBook = null;
	
	/** Linked list of ordered Property IDs. */
	private ArrayList<String> propertyIds = new ArrayList<String>();
	
	/** The VaadinTable interacts as a EditorListener. */
	private VaadinTree vaadinTree;
	
	/** The used CellEditor. */
	private ICellEditorHandler<AbstractComponent> cellEditorHandler = null;
	
	/** the actual read components. **/
	private HashMap<TreePath, AbstractComponent> components = new HashMap<TreePath, AbstractComponent>();
	
	/** If the table is in editable mode. */
	private boolean editMode = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
	/**
	 * Creates a new instance of <code>JVxHierarchicalContainer</code> with the
	 * given tree.
	 * 
	 * @param pVaadinTree the vaadin tree
	 */
	public JVxHierarchicalContainer(VaadinTree pVaadinTree)
	{
		vaadinTree = pVaadinTree;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
	
	/**
	 * {@inheritDoc}
	 */
	public Item getItem(Object pItemId)
	{
		if (pItemId != null)
		{
			return new JVxContainerItem((TreePath)pItemId);
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getContainerPropertyIds()
	{
		return propertyIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Property getContainerProperty(Object pItemId, Object pPropertyId)
	{
		JVxContainerProperty jvxContainerProperty = new JVxContainerProperty((TreePath)pItemId, (String)pPropertyId);
		
		return jvxContainerProperty;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<?> getType(Object pPropertyId)
	{
		if (pPropertyId == ITEM_ICON_PROPERTY_ID)
		{
			return Resource.class;
		}
		
		return Component.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean containsId(Object pItemId)
	{
		if (pItemId != null)
		{
			TreePath treePath = (TreePath)pItemId;
			
			IDataPage dataPage = getDataPage(treePath.getParentPath());
			
			int row = treePath.getLast();
			
			try
			{
				if (dataPage.getDataRow(row) != null)
				{
					return true;
				}
			}
			catch (ModelException e)
			{
				LOGGER.error(e);
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getChildren(Object pItemId)
	{
		LinkedList<Object> children = new LinkedList<Object>();
		
		if (pItemId != null)
		{
			TreePath treePath = (TreePath)pItemId;
			
			try
			{
				IDataPage dataPage = getDataPage(treePath);
				
				if (dataPage != null)
				{
					dataPage.fetchAll();
					
					for (int i = 0; i < dataPage.getRowCount(); i++)
					{
						children.add(treePath.getChildPath(i));
					}
				}
			}
			catch (ModelException e)
			{
				LOGGER.error(e);
			}
		}
		
		return Collections.unmodifiableCollection(children);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getParent(Object pItemId)
	{
		if (pItemId != null)
		{
			TreePath treePath = (TreePath)pItemId;
			
			return treePath.getParentPath();
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> rootItemIds()
	{
		components.clear();
		
		return getChildren(TreePath.EMPTY);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean areChildrenAllowed(Object pItemId)
	{
		return hasChildren(pItemId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isRoot(Object pItemId)
	{
		if (pItemId != null)
		{
			TreePath treePath = (TreePath)pItemId;
			
			if (treePath.length() == 1)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasChildren(Object pItemId)
	{
		if (pItemId != null)
		{
			TreePath treePath = (TreePath)pItemId;
			
			try
			{
				IDataPage dataPage = getDataPage(treePath);
				
				if (dataPage != null && dataPage.getDataRow(0) != null)
				{
					return true;
				}
			}
			catch (ModelException e)
			{
				LOGGER.error(e);
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int size()
	{
		
		if (vaadinTree != null && ((TreeTable)vaadinTree.getResource()) != null && ((TreeTable)vaadinTree.getResource()).getValue() != null)
		{
			IDataPage dataPage = getDataPage((TreePath)((TreeTable)vaadinTree.getResource()).getValue());
			
			if (dataPage != null)
			{
				IDataBook dataBook = dataPage.getDataBook();
				
				try
				{
					if (dataBook.isAllFetched())
					{
						return dataBook.getRowCount();
					}
					else
					{
						return dataBook.getRowCount() + ((TreeTable)vaadinTree.getResource()).getPageLength();
					}
					
				}
				catch (ModelException e)
				{
					LOGGER.error(e);
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getItemIds()
	{
		throw new UnsupportedOperationException("getItemIds() not supported");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean setChildrenAllowed(Object pItemId, boolean pChildrenAllowed)
	{
		throw new UnsupportedOperationException("setChildrenAllowed(Object itemId, boolean childrenAllowed) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean setParent(Object pItemId, Object pNewParentId)
	{
		throw new UnsupportedOperationException("setParent(Object itemId, Object newParentId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Item addItem(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object addItem()
	{
		throw new UnsupportedOperationException("addItem() not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean addContainerProperty(Object pPropertyId, Class<?> pType, Object pDefaultValue)
	{
		throw new UnsupportedOperationException("addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeContainerProperty(Object pPropertyId)
	{
		throw new UnsupportedOperationException("removeContainerProperty(Object propertyId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeAllItems()
	{
		throw new UnsupportedOperationException("removeAllItems() not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeItem(Object pItemId)
	{
		throw new UnsupportedOperationException("removeItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object nextItemId(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object prevItemId(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object firstItemId()
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId()
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFirstId(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isLastId(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object addItemAfter(Object pPreviousItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Item addItemAfter(Object pPreviousItemId, Object pNewItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getIdByIndex(int pIndex)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<?> getItemIds(int pStartIndex, int pNumberOfItems)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object addItemAt(int pIndex)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Item addItemAt(int pIndex, Object pNewItemId)
	{
		throw new UnsupportedOperationException("addItem(Object itemId) not supported: use setDataBook");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
	/**
	 * Fires an itemSetChange event.
	 */
	public void initialize()
	{
		try
		{
			((TreeTable)vaadinTree.getResource()).refreshRowCache();
			
			setSelectedRow();
			
			super.fireItemSetChange();
		}
		catch (Exception e)
		{
			LOGGER.error(e);
		}
	}
	
	/**
	 * Selects the row in the table.
	 * 
	 * @throws ModelException if selection fails
	 */
	public void setSelectedRow() throws ModelException
	{
		TreePath treePath = TreePath.EMPTY;
		
		TreeTable ttable = vaadinTree.getResource();
		
		for (int i = 0; i < dataBooks.length; i++)
		{
			if (dataBooks[i].isSelfJoined())
			{
				TreePath tpath = dataBooks[i].getTreePath();
				
				if (tpath != null)
				{
					for (int level = 0; level < tpath.length(); level++)
					{
						treePath = treePath.getChildPath(tpath.get(level));
						
						if (ttable.isCollapsed(treePath.getParentPath()))
						{
							ttable.setCollapsed(treePath.getParentPath(), false);
						}
						
						ttable.select(treePath);
					}
					
					int selectedRow = dataBooks[i].getSelectedDataPageRow();
					
					if (dataBooks[i].getTreePath().length() == 0)
					{
						ttable.select(tpath.getChildPath(selectedRow));
					}
					
					if (selectedRow >= 0)
					{
						if (ttable.hasChildren(treePath) && ttable.isCollapsed(treePath))
						{
							ttable.setCollapsed(treePath, false);
						}
						
						int[] path = new int[treePath.length() + 1];
						
						if (treePath.length() > 0)
						{
							for (int p = 0; p < (path.length - 1); p++)
							{
								path[p] = treePath.get(p);
							}
						}
						
						path[path.length - 1] = selectedRow;
						
						ttable.select(new TreePath(path));
					}
				}
			}
			else
			{
				int row = dataBooks[i].getSelectedDataPageRow();
				
				if (row >= 0)
				{
					treePath = treePath.getChildPath(row);
					
					if (ttable.isCollapsed(treePath.getParentPath()))
					{
						ttable.setCollapsed(treePath.getParentPath(), false);
					}
					
					ttable.select(treePath);
				}
			}
		}
		
		ttable.setCurrentPageFirstItemId(treePath);
	}
	
	/**
	 * Adds an Container.ItemSetChangeListener to the container.
	 * 
	 * @param listener the listener
	 */
	public void addItemSetChangeListener(Container.ItemSetChangeListener listener)
	{
		super.addItemSetChangeListener(listener);
	}
	
	/**
	 * Sets the DataBooks displayed by this control.
	 * 
	 * @param pDataBooks the DataBooks
	 * @see #getDataBook(int)
	 */
	public void setDataBooks(IDataBook... pDataBooks)
	{
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
			IDataBook dataBook = dataBooks[dataBooks.length - 1];
			
			if (dataBook.isSelfJoined())
			{
				selfJoinedDataBook = dataBook;
			}
		}
		
		propertyIds.add(ITEM_LABEL_PROPERTY_ID);
		propertyIds.add(ITEM_ICON_PROPERTY_ID);
	}
	
	/**
	 * Sets the container in editMode.
	 * 
	 * @param pEditMode true if the container is in editMode.
	 */
	public void setEditMode(boolean pEditMode)
	{
		editMode = pEditMode;
	}
	
	/**
	 * True if the container is in edit mode.
	 * 
	 * @return true if the container is in edit mode.
	 */
	public boolean isEditMode()
	{
		return editMode;
	}
	
	/**
	 * Returns the used CellEditor.
	 * 
	 * @return the used cellEditor.
	 */
	public ICellEditorHandler getCellEditorHandler()
	{
		return cellEditorHandler;
	}
	
	/**
	 * Returns the node image resource.
	 * 
	 * @param pTreePath the tree path.
	 * @return the node image resource.
	 * @throws ModelException if accessing databook fails
	 */
	public Resource getNodeImage(TreePath pTreePath) throws ModelException
	{
		int row = pTreePath.getLast();
		
		IDataPage dataPage = getDataPage(pTreePath.getParentPath());
		
		if (dataPage != null)
		{
			IDataRow dataRow = dataPage.getDataRow(row);
			
			boolean bExpanded = !((TreeTable)vaadinTree.getResource()).isCollapsed(pTreePath);
			
			String columnName = dataPage.getDataBook().getRowDefinition().getColumnView(ITreeControl.class).getColumnName(0);
			
			if (vaadinTree.getNodeFormatter() != null)
			{
				IImage image = vaadinTree.getNodeFormatter().getNodeImage(dataPage.getDataBook(), dataPage, dataRow, columnName,
						row, bExpanded, !hasChildren(pTreePath));
				
				if (image != null)
				{
					return (Resource)image.getResource();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the editor component.
	 * 
	 * @param pTreePath the item id.
	 * @param pColumnName the property id.
	 * @return the editor component
	 * @throws ModelException if accessing databook fails
	 */
	private AbstractComponent getEditableComponent(TreePath pTreePath, String pColumnName) throws ModelException
	{
		AbstractComponent component = null;
		
		ColumnDefinition columnDefinition = null;
		
		IDataPage dataPage = getDataPage(pTreePath.getParentPath());
		
		if (dataPage != null)
		{
			IDataBook dataBook = dataPage.getDataBook();
			
			columnDefinition = dataBook.getRowDefinition().getColumnDefinition(pColumnName);
		}
		
		if (columnDefinition != null && !columnDefinition.isReadOnly())
		{
			
			ICellEditor cellEditor = getCellEditor(columnDefinition.getDataType());
			
			cellEditorHandler = (ICellEditorHandler<AbstractComponent>)cellEditor.createCellEditorHandler((ICellEditorListener)vaadinTree,
					vaadinTree.getActiveDataBook(), pColumnName);
			cellEditorHandler.uninstallEditor();
			cellEditorHandler.cancelEditing();
	 		if (cellEditorHandler instanceof IVaadinCellEditorHandler)
	 		{
	 			((IVaadinCellEditorHandler)cellEditorHandler).setWidth(100f, Unit.PERCENTAGE);
	 		}
	 		else
	 		{
	 			VaadinUtil.setComponentWidth(cellEditorHandler.getCellEditorComponent(), 100f, Unit.PERCENTAGE);
	 		}

			return (AbstractComponent)cellEditorHandler.getCellEditorComponent();
		}
		
		return component;
	}
	
	/**
	 * Gets the CellEditor for editing the given data type.
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
	 * Returns the label or the editor component.
	 * 
	 * @param pTreePath the item id.
	 * @return the label or the editor component.
	 * @throws ModelException if accessing databook fails
	 */
	private AbstractComponent getComponent(TreePath pTreePath) throws ModelException
	{
		IDataPage dataPage = getDataPage(pTreePath.getParentPath());
		
		IDataBook dataBook = dataPage.getDataBook();
		
		String columnName = dataBook.getRowDefinition().getColumnView(ITreeControl.class).getColumnName(0);
		
		AbstractComponent component = null;
		
		if (editMode && vaadinTree.getSelectedTreePath() != null && vaadinTree.getSelectedTreePath().equals(pTreePath))
		{
			component = getEditableComponent(pTreePath, columnName);
			
			if (component instanceof Focusable)
			{
				((Focusable) component).focus();
			}
		}
		else
		{
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);
			
			ICellRenderer<AbstractComponent> cellRenderer = columnDefinition.getDataType().getCellRenderer();
			
			if (cellRenderer == null)
			{
				ICellEditor cellEditor = (ICellEditor)getCellEditor(columnDefinition.getDataType());
				
				if (cellEditor instanceof ICellRenderer)
				{
					cellRenderer = (ICellRenderer)cellEditor;
				}
				else if (cellEditor instanceof IResource)
				{
					cellEditor = (ICellEditor)((IResource)cellEditor).getResource();
					if (cellEditor instanceof ICellRenderer)
					{
						cellRenderer = (ICellRenderer)cellEditor;
					}
				}
			}
			
			if (cellRenderer != null)
			{
				int row = pTreePath.getLast();
				
				IDataRow dataRow = dataPage.getDataRow(row);
				
				component = cellRenderer.getCellRendererComponent((TreeTable)vaadinTree.getResource(), dataPage, row, dataRow, columnName, vaadinTree.isEditable(), false);
			}
		}
		
		return component;
	}
	
	/**
	 * Formats the given component with the backgroundcolor, foregroundcolor and
	 * font.
	 * 
	 * @param component the component.
	 * @param pTreePath the tree path.
	 * @throws Throwable if accessing databook fails
	 */
	private void formatCellComponent(AbstractComponent component, TreePath pTreePath) throws Throwable
	{
		ICellFormat cellFormat = null;
		
		if (vaadinTree.getCellFormatter() != null)
		{
			IDataPage dataPage = getDataPage(pTreePath.getParentPath());
			
			if (dataPage != null)
			{
				IDataBook dataBook = dataPage.getDataBook();
				
				int rowIndex = pTreePath.getLast();
				
				IDataRow dataRow = dataPage.getDataRow(rowIndex);
				
				String columnName = dataBook.getRowDefinition().getColumnView(ITreeControl.class).getColumnName(0);
				
				int columnIndex = dataBook.getRowDefinition().getColumnDefinitionIndex(columnName);
				
				cellFormat = vaadinTree.getCellFormatter().getCellFormat(dataBook, dataPage, dataRow, columnName, rowIndex, columnIndex);
			}
		}
		
		IColor background;
		IColor foreground;
		IFont font;
		
		if (cellFormat == null)
		{
			background = null;
			foreground = null;
			font = null;
		}
		else
		{
			background = cellFormat.getBackground();
			foreground = cellFormat.getForeground();
			font = cellFormat.getFont();
		}
		
		CssExtension cssExtension = VaadinUtil.getCssExtension(component);
		
		if (cssExtension != null)
		{
			if (foreground != null)
			{
				cssExtension.addAttribute("color", ((VaadinColor)foreground.getResource()).getStyleValueRGB());
			}
			
			if (background != null)
			{
				cssExtension.addAttribute("background-color", ((VaadinColor)background.getResource()).getStyleValueRGB());
				
				CssExtensionAttribute attribute = new CssExtensionAttribute("background-color", ((VaadinColor)background.getResource()).getStyleValueRGB());
				
				attribute.setElementClassName("v-table-cell-content");
				attribute.setSearchDirection(CssExtensionAttribute.SEARCH_UP);
				
				cssExtension.addAttribute(attribute);
			}
			
			if (font != null)
			{
				cssExtension.addAttributes(((VaadinFont)font.getResource()).getStyleAttributes(null, CssExtensionAttribute.SELF));
			}
		}
	}
	
	/**
	 * Returns the IDataPage for the given treePath.
	 * 
	 * @param pTreePath the TreePath
	 * @return the IDataPage
	 */
	public IDataPage getDataPage(TreePath pTreePath)
	{
		IDataPage dataPage = null;
		
		try
		{
			if (dataBooks[0].isSelfJoined())
			{
				dataBooks[0].getSelectedRow(); // sync
				dataPage = dataBooks[0].getDataPage(new javax.rad.model.TreePath());
			}
			else
			{
				dataPage = dataBooks[0].getDataPage();
			}
			
			for (int level = 1, len = pTreePath.length(); level <= len; level++)
			{
				int row = pTreePath.get(level - 1);
				
				if (dataPage != null)
				{
					IDataRow dataRow = dataPage.getDataRow(row);
					
					IDataBook dataBook = getDataBook(level);
					
					if (dataBook == null || dataRow == null)
					{
						dataPage = null;
					}
					else if (dataBook.isSelfJoined() && dataBook != getDataBook(level - 1))
					{
						dataPage = dataBook.getDataPageWithRootRow(dataRow);
					}
					else
					{
						dataPage = dataBook.getDataPage(dataRow);
					}
				}
			}
			
		}
		catch (ModelException e)
		{
			LOGGER.error(e);
		}
		
		return dataPage;
	}
	
	/**
	 * Gets the IDataBook for the given level.
	 * 
	 * @param pLevel the level.
	 * @return the IDataBook.
	 */
	public IDataBook getDataBook(int pLevel)
	{
		if (pLevel < dataBooks.length)
		{
			return dataBooks[pLevel];
		}
		else
		{
			IDataBook dataBook = dataBooks[dataBooks.length - 1];
			
			if (dataBook.isSelfJoined())
			{
				return dataBooks[dataBooks.length - 1];
			}
			else
			{
				return null;
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************	
	
	/**
	 * The <code>JVxContainerItem</code> class is an {@link Item}.
	 * 
	 * @author Stefan Wurm
	 */
	private final class JVxContainerItem implements Item
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
		
		/** The item id. */
		private final TreePath itemId;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
		
		/**
		 * Creates a new instance of <code>JVxContainerItem</code>.
		 * 
		 * @param pItemId the Item ID of the new Item.
		 */
		private JVxContainerItem(TreePath pItemId)
		{
			// Gets the item contents from the host
			if (pItemId == null)
			{
				throw new NullPointerException();
			}
			
			itemId = pItemId;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
		
		/**
		 * Returns the Property for this item.
		 * 
		 * @param pId the propertyId.
		 * @return the Property.
		 */
		public Property getItemProperty(Object pId)
		{
			return new JVxContainerProperty(itemId, (String)pId);
		}
		
		/**
		 * The item property ids. The column names of the column view.
		 * 
		 * @return The item proerpty ids in a list.
		 */
		public Collection<?> getItemPropertyIds()
		{
			return Collections.unmodifiableCollection(propertyIds);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean addItemProperty(Object pId, Property pProperty)
		{
			throw new UnsupportedOperationException("Indexed container item does not support adding new properties");
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean removeItemProperty(Object pId)
		{
			throw new UnsupportedOperationException("Indexed container item does not support property removal");
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
		
		/**
		 * Calculates a integer hash-code for the Item that's unique inside the
		 * list. Two Items inside the same list have always different
		 * hash-codes, though Items in different lists may have identical
		 * hash-codes.
		 * 
		 * @return A locally unique hash-code as integer
		 */
		@Override
		public int hashCode()
		{
			return itemId.hashCode();
		}
		
		/**
		 * Tests if the given object is the same as the this object. Two Items
		 * got from a list container with the same ID are equal.
		 * 
		 * @param pObject an object to compare with this object
		 * @return <code>true</code> if the given object is the same as this
		 *         object, <code>false</code> if not
		 */
		@Override
		public boolean equals(Object pObject)
		{
			if (this == pObject)
			{
				return true;
			}
			
			if (pObject == null)
			{
				return false;
			}
			
			if (getClass() != pObject.getClass())
			{
				return false;
			}
			
			JVxContainerItem other = (JVxContainerItem)pObject;
			
			if (itemId == null)
			{
				if (other.itemId != null)
				{
					return false;
				}
			}
			else if (!itemId.equals(other.itemId))
			{
				return false;
			}
			
			return true;
		}
		
		/**
		 * Gets the <code>String</code> representation of the contents of the
		 * Item. The format of the string is a space separated catenation of the
		 * <code>String</code> representations of the values of the Properties
		 * contained by the Item.
		 * 
		 * @return <code>String</code> representation of the Item contents
		 */
		@Override
		public String toString()
		{
			String retValue = "";
			
			for (final Iterator<?> i = propertyIds.iterator(); i.hasNext();)
			{
				final Object propertyId = i.next();
				retValue += getItemProperty(propertyId).getValue();
				if (i.hasNext())
				{
					retValue += " ";
				}
			}
			
			return retValue;
		}
		
	} 	// JVxContainerItem
	
	/**
	 * The <code>JVxContainerProperty</code> is one Column in the tree.
	 * 
	 * @author Stefan Wurm
	 */
	private final class JVxContainerProperty implements Property<Object>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** ID of the Item, where this property resides. */
		private final TreePath itemId;
		
		/** Id of the Property. */
		private final String propertyId;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	    
		
		/**
		 * Creates a new instance of <code>JVxContainerProperty</code>.
		 * 
		 * @param pItemId the ID of the Item to connect the new Property to.
		 * @param pPropertyId the Property ID of the new Property.
		 */
		private JVxContainerProperty(TreePath pItemId, String pPropertyId)
		{
			if (pItemId == null || pPropertyId == null)
			{
				// Null ids are not accepted
				throw new NullPointerException("Container item or property ids can not be null");
			}
			
			propertyId = pPropertyId;
			itemId = pItemId;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
		
		/**
		 * {@inheritDoc}
		 */
		public Object getValue()
		{
			try
			{
				if (propertyId == ITEM_ICON_PROPERTY_ID)
				{
					return getNodeImage(itemId);
				}
				
				AbstractComponent abstractComponent = components.get(itemId);
				
				if (abstractComponent == null)
				{
					abstractComponent = getComponent(itemId);
					
					components.put(itemId, abstractComponent);
				}
				
				formatCellComponent(abstractComponent, itemId);
				
				return abstractComponent;
				
				//					return selfJoinedDataBook.getDataPage(itemId.getParentPath()).getDataRow(row).getValueAsString(propertyId);
				
			}
			catch (Throwable e)
			{
				LOGGER.error(e);
			}
			
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setValue(Object newValue)
		{
			throw new UnsupportedOperationException("setValue not supported. Use the setValue of the DataBook");
		}
		
		/**
		 * {@inheritDoc}
		 */
		public Class<Object> getType()
		{
			return Object.class;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isReadOnly()
		{
			try
			{
				if (selfJoinedDataBook != null && propertyId != ITEM_ICON_PROPERTY_ID)
				{
					return selfJoinedDataBook.getRowDefinition().getColumnDefinition(propertyId).isReadOnly();
				}
			}
			catch (ModelException e)
			{
				LOGGER.error(e);
			}
			
			return true;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setReadOnly(boolean newStatus)
		{
			throw new UnsupportedOperationException("setReadOnly not supported. Use the setReadonly from the ColumnDefinition of the DataBook");
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
		
		/**
		 * Calculates a integer hash-code for the Property that's unique inside
		 * the Item containing the Property. Two different Properties inside the
		 * same Item contained in the same list always have different
		 * hash-codes, though Properties in different Items may have identical
		 * hash-codes.
		 * 
		 * @return A locally unique hash-code as integer
		 */
		@Override
		public int hashCode()
		{
			return 7 * (itemId != null ? itemId.hashCode() : 0) + 7 * (propertyId != null ? propertyId.hashCode() : 0);
		}
		
		/**
		 * Tests if the given object is the same as the this object. Two
		 * Properties got from an Item with the same ID are equal.
		 * 
		 * @param obj an object to compare with this object
		 * @return <code>true</code> if the given object is the same as this
		 *         object, <code>false</code> if not
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (obj == null || !obj.getClass().equals(JVxContainerProperty.class))
			{
				return false;
			}
			final JVxContainerProperty lp = (JVxContainerProperty)obj;
			
			return lp.getHost() == getHost()
					&& lp.propertyId.equals(propertyId)
					&& lp.itemId.equals(itemId);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
		
		/**
		 * Returns the instance of the JVxContainerBase.
		 * 
		 * @return the JVxContainerBase.
		 */
		private JVxHierarchicalContainer getHost()
		{
			return JVxHierarchicalContainer.this;
		}
		
	} 	// JVxContainerProperty 
	
}	// JVxHierarchicalContainer
