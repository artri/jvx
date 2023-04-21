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
 * 13.02.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable.StyleInfo;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractContainer;
import com.vaadin.v7.ui.Table;

/**
 * The <code>JVxContainer</code> class is a container for the vaadin table to
 * interact with an {@link IDataBook} from JVx.
 * 
 * Usage:
 * 
 * <pre>
 * JVxContainer jvxContainer = new JVxContainer();
 * jvxContainer.setDataBook(dataBook);
 * jvxContainer.addItemSetChangeListener(table);
 * table.setContainerDataSource(jvxContainer);
 * </pre>
 * 
 * @author Stefan Wurm
 */
@SuppressWarnings("deprecation")
public class JVxContainer extends AbstractContainer
		implements Container.Indexed
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The maximum cached amount of rows. */
	private static final int MAX_CACHE_SIZE = 200;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The databook to be shown. */
	private IDataBook dataBook = null;
	
	/** Linked list of ordered Property IDs. */
	private ArrayList<String> propertyIds = new ArrayList<String>();
	
	/** The VaadinTable interacts as a EditorListener. */
	private VaadinTable vaadinTable;
	
	/** PropertyCache. */
	private HashMap<Integer, JVxContainerItem> propertyCache = new HashMap<Integer, JVxContainerItem>();
	
	/** True, if the items should be refreshed. */
	private boolean refreshItems = false;
	
	/** The items id cache. Holds all loaded item ids. */
	private List<Integer> itemIds = new ArrayList<Integer>();
	
	/** The map for read only properties. */
	private Map<Object, Boolean> readonlyProperties = new HashMap<Object, Boolean>();
	
	/** The map for nullable properties. */
	private Map<Object, Boolean> nullableProperties = new HashMap<Object, Boolean>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
	/**
	 * Creates a new instance of <code>JVxContainer</code> for the given table.
	 * 
	 * @param pVaadinTable the vaadin table.
	 */
	public JVxContainer(VaadinTable pVaadinTable)
	{
		vaadinTable = pVaadinTable;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public List<?> getItemIds(int pStartIndex, int pNumberOfItems)
	{
		if (vaadinTable.getColumnView().getColumnCount() == 0
				|| pStartIndex < 0
				|| pNumberOfItems < 1)
		{
			itemIds.clear();
		}
		else
		{
			try
			{
				dataBook.getDataRow(pStartIndex + pNumberOfItems - 1);
				
				if (dataBook.getRowCount() - pStartIndex < pNumberOfItems)
				{
					pNumberOfItems = dataBook.getRowCount() - pStartIndex;
				}
				
				if (pNumberOfItems != itemIds.size()
						|| itemIds.size() == 0
						|| itemIds.get(0).intValue() != pStartIndex)
				{
					itemIds.clear();
					
					for (int rowIndex = pStartIndex, endIndex = pStartIndex + pNumberOfItems; rowIndex < endIndex; rowIndex++)
					{
						itemIds.add(Integer.valueOf(rowIndex));
					}
				}
			}
			catch (ModelException e)
			{
				itemIds.clear();
				
				ExceptionHandler.raise(e);
			}
		}
		
		return itemIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JVxContainerProperty getContainerProperty(Object pItemId, Object pPropertyId)
	{
		Integer itemId = (Integer)pItemId;
		String propertyId = (String)pPropertyId;
		JVxContainerItem rowCache = propertyCache.get(itemId);
		
		if (rowCache == null && propertyCache.size() > MAX_CACHE_SIZE)
		{
			propertyCache.clear();
		}
		else if (refreshItems)
		{
			for (Map.Entry<Integer, JVxContainerItem> rows : propertyCache.entrySet())
			{
				Integer curIitemId = rows.getKey();
				
				JVxContainerItem row = rows.getValue();
				
				Iterator<Map.Entry<String, JVxContainerProperty>> itProp = row.entrySet().iterator();
				while (itProp.hasNext())
				{
					Map.Entry<String, JVxContainerProperty> entry = itProp.next();
					String curPropertyId = entry.getKey();
					
					if (!propertyIds.contains(curPropertyId))
					{
						itProp.remove();
					}
					else
					{
						JVxContainerProperty property = entry.getValue();
						
                        property.setStyle(vaadinTable.getCellStyle(curIitemId, curPropertyId));
						property.setValue(vaadinTable.getComponent(curIitemId, curPropertyId, property.getValue(), property.getStyle()));
					}
				}
			}
			
			refreshItems = false;
		}
		
		if (rowCache == null)
		{
			rowCache = new JVxContainerItem();
			
			propertyCache.put(itemId, rowCache);
		}
		
		JVxContainerProperty property = rowCache.get(pPropertyId);
		if (property == null)
		{
			property = new JVxContainerProperty(propertyId);
			
            property.setStyle(vaadinTable.getCellStyle(itemId, propertyId));
			property.setValue(vaadinTable.getComponent(itemId, propertyId, null, property.getStyle()));
			
			rowCache.put(propertyId, property);
		}
		
		return property;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public int size()
	{
		if (dataBook != null && dataBook.isOpen())
		{
			try
			{
			    if (vaadinTable.getTable().isMarkAsDirty()) // avoid sync! when is mark as dirty to avoid concurrent modification exception
			    {
			        if (dataBook.isOutOfSync())
			        {
			            return 0;
			        }
			        else
			        {
	                    return dataBook.getRowCount();
			        }
			    }
			    else if (dataBook.isAllFetched())
				{
					return dataBook.getRowCount();
				}
				else
				{
					return dataBook.getRowCount() + vaadinTable.getTable().getPageLength();
				}
				
			}
			catch (Throwable th)
			{
				ExceptionHandler.show(th);
			}
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object nextItemId(Object pItemId)
	{
		int index = ((Integer)pItemId).intValue();
		
		try
		{
			if (index < (dataBook.getRowCount() - 1))
			{
				return Integer.valueOf(index + 1);
			}
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object prevItemId(Object pItemId)
	{
		int index = ((Integer)pItemId).intValue();
		
		if (index > 0)
		{
			return Integer.valueOf(index - 1);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object firstItemId()
	{
		return Integer.valueOf(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object lastItemId()
	{
		try
		{
			return Integer.valueOf(dataBook.getRowCount() - 1);
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		return Integer.valueOf(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean isFirstId(Object pItemId)
	{
		return firstItemId().equals(pItemId);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean isLastId(Object pItemId)
	{
		return lastItemId().equals(pItemId);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object addItemAfter(Object pPreviousItemId)
	{
		throw new UnsupportedOperationException("addItemAfter(Object) is not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Item addItemAfter(Object pPreviousItemId, Object pNewItemId)
	{
		throw new UnsupportedOperationException("addItemAfter(Object, Object) is not supported: use setDataBook");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Item getItem(Object pItemId)
	{
		return propertyCache.get(pItemId);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Collection<?> getContainerPropertyIds()
	{
		return propertyIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Collection<?> getItemIds()
	{
		ArrayList<Integer> alItemIds = new ArrayList<Integer>();
		
		try
		{
			for (int rowIndex = 0, count = dataBook.getRowCount(); rowIndex < count; rowIndex++)
			{
				alItemIds.add(Integer.valueOf(rowIndex));
			}
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		return alItemIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Class<?> getType(Object pPropertyId)
	{
		return AbstractComponent.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean containsId(Object pItemId)
	{
		int index = ((Integer)pItemId).intValue();
		
		try
		{
			return index >= 0 && index < dataBook.getRowCount();
		}
		catch (ModelException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Item addItem(Object pItemId)
	{
		throw new UnsupportedOperationException("addItem(Object) is not supported: Use the data book.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object addItem()
	{
		throw new UnsupportedOperationException("addItem() is not supported: Use the data book.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean removeItem(Object pItemId)
	{
		throw new UnsupportedOperationException("removeItem(Object) is not supported: Use the data book.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean addContainerProperty(Object pPropertyId, Class<?> pType, Object pDefaultValue)
	{
		throw new UnsupportedOperationException("addContainerProperty(Object, Class<?>, Object) is not supported. Use the data book.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean removeContainerProperty(Object pPropertyId)
	{
		throw new UnsupportedOperationException("removeContainerProperty(Object) is not supported. Use the data book.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean removeAllItems()
	{
		throw new UnsupportedOperationException("addItemAt(int) is not supported. Use the data book to add items.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public int indexOfId(Object pItemId)
	{
		if (pItemId == null || pItemId == vaadinTable.getTable().getNullSelectionItemId())
		{
			return -1;
		}
		else
		{
			return ((Integer)pItemId).intValue();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object getIdByIndex(int pIndex)
	{
		return Integer.valueOf(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object addItemAt(int index)
	{
		throw new UnsupportedOperationException("addItemAt(int) not supported. Use the data book to add items.");
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public Item addItemAt(int pIndex, Object pNewItemId)
	{
		throw new UnsupportedOperationException("addItemAt(int, Object) not supported. Use the data book to add items.");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePropertySetChangeListener(PropertySetChangeListener listener)
	{
		super.removePropertySetChangeListener(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeItemSetChangeListener(ItemSetChangeListener listener)
	{
		super.removeItemSetChangeListener(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addItemSetChangeListener(Container.ItemSetChangeListener pListener)
	{
		super.addItemSetChangeListener(pListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPropertySetChangeListener(Container.PropertySetChangeListener pListener)
	{
		super.addPropertySetChangeListener(pListener);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * If the column view have changed.
	 * 
	 * @return true if the columns have changed.
	 */
	public boolean isColumnViewChanged()
	{
		return !Arrays.equals(propertyIds.toArray(), vaadinTable.getColumnView().getColumnNames());
	}
	
	/**
	 * Returns <code>true</code> if the column info (read only or nullable) have
	 * changed, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the column info (read only or nullable) have
	 *         changed, <code>false</code> otherwise.
	 * @throws ModelException if column access fails
	 */
	public boolean isColumnInfoChanged() throws ModelException
	{
		for (String columnName : vaadinTable.getColumnView().getColumnNames())
		{
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);

			if (!readonlyProperties.containsKey(columnName)
					|| readonlyProperties.get(columnName).booleanValue() != (!dataBook.isUpdateEnabled() || dataBook.isReadOnly() || columnDefinition.isReadOnly())
					|| !nullableProperties.containsKey(columnName)
					|| nullableProperties.get(columnName).booleanValue() != columnDefinition.isNullable()
					|| !CommonUtil.equals(vaadinTable.translate(columnDefinition.getLabel()), vaadinTable.getResource().getColumnHeader(columnName)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sets the DataBook.
	 * 
	 * @param pDataBook the dataBook
	 */
	public void setDataBook(IDataBook pDataBook)
	{
		dataBook = pDataBook;
	}
	
	/**
	 * Initializes the column properties.
	 * 
	 * @throws ModelException if the initialization fails.
	 */
	public void initializeProperties() throws ModelException
	{
		if (dataBook != null)
		{
			boolean columnViewChanged = isColumnViewChanged();
			if (columnViewChanged)
			{
				propertyIds.clear();
				
				for (String columnName : vaadinTable.getColumnView().getColumnNames())
				{
					propertyIds.add(columnName);
				}
				
				super.fireContainerPropertySetChange();
				
				vaadinTable.initColumnHeaders();
			}
			
			if (isColumnInfoChanged())
			{
				readonlyProperties.clear();
				nullableProperties.clear();
				
				for (String columnName : vaadinTable.getColumnView().getColumnNames())
				{
					ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);
					
					readonlyProperties.put(columnName, Boolean.valueOf(!dataBook.isUpdateEnabled() || dataBook.isReadOnly() || columnDefinition.isReadOnly()));
					nullableProperties.put(columnName, Boolean.valueOf(columnDefinition.isNullable()));
					if (!CommonUtil.equals(vaadinTable.translate(columnDefinition.getLabel()), vaadinTable.getResource().getColumnHeader(columnName)))
					{
						vaadinTable.getResource().setColumnHeader(columnName, vaadinTable.translate(columnDefinition.getLabel()));
					}
				}
				if (!columnViewChanged)
				{
					super.fireContainerPropertySetChange();
				}
			}
		}
	}
	
	/**
	 * Fires an itemSetChange event.
	 * 
	 * @throws ModelException if the initialization fails.
	 */
	public void initializeItems() throws ModelException
	{
		if (dataBook != null)
		{
			if (!setSelectedRow(false) || vaadinTable.isDataChanged())
			{
				initializeOnlyVisibleItems(); 
			}
		}
	} 
	
	/**
	 * Refreshes the visible.
	 */
	public void initializeOnlyVisibleItems()
	{
		try
		{
			Field field = Table.class.getDeclaredField("pageBuffer");
			field.setAccessible(true);
			field.set(vaadinTable.getTable(), null);
			
			vaadinTable.getTable().refreshRenderedCells();
		}
		catch (Exception e)
		{
			ExceptionHandler.raise(e);
		}

	}
	
	/**
	 * Clears the property cache.
	 */
	public void clearPropertyCache()
	{
		refreshItems = true;
	}
	
	/**
	 * Scrolls to the given item by setting the current page first item id.
	 * @param pItem the item
	 * @return true, if scrolling was done.
	 */
	public boolean scrollToItem(Object pItem)
	{
		if (!isItemShowing((Integer)pItem))
		{
			if (vaadinTable.getTable().getPageLength() < 60)
			{
				pItem = getIdByIndex(indexOfId(pItem) - vaadinTable.getTable().getPageLength() / 2);
			}
			
			if (indexOfId(pItem) < 0)
			{
				pItem = Integer.valueOf(0);
			}
			
			vaadinTable.getTable().setCurrentPageFirstItemId(pItem);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Selects the row in the table.
	 * 
	 * @param pForce if it should be done anyway
	 * @return true if the fireItemSetChanged event is already thrown
	 * @throws ModelException if settings the selected row fails.
	 */
	public boolean setSelectedRow(boolean pForce) throws ModelException
	{
		if (dataBook != null && (pForce || dataBook.getSelectedRow() != indexOfId(vaadinTable.getTable().getValue())))
		{
			if (dataBook.getSelectedRow() >= 0)
			{
				Integer itemId = (Integer)getIdByIndex(dataBook.getSelectedRow());
				
				if (vaadinTable.isShowSelection())
				{
					vaadinTable.getTable().select(itemId);
				}
				
				scrollToItem(itemId);
			}
			else
			{
				vaadinTable.getTable().setValue(vaadinTable.getTable().getNullSelectionItemId());
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Gets whether the row with the given item id is visible for the user.
	 * 
	 * @param pItemId the item id
	 * @return <code>true</code> if the row with the itemId is visible for the
	 *         user
	 */
	public boolean isItemShowing(Object pItemId)
	{
		int targetIndex = indexOfId(pItemId);
		
		// Get the index of the first item showing.
		int firstIndexShown = vaadinTable.getTable().getCurrentPageFirstItemIndex();
		int lastIndexShown = firstIndexShown + vaadinTable.getTable().getPageLength() - 2; // Minus two, to ignore rows partially scrolling off the bottom.
	
		// Determine if the desired item's index lies within the range of displayed indices, inclusively.
		return targetIndex >= firstIndexShown && targetIndex < lastIndexShown;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>JVxContainerItem</code> is an {@link Item} for dynamic data
	 * from the model.
	 * 
	 * @author Stefan Wurm
	 */
	private final class JVxContainerItem extends HashMap<String, JVxContainerProperty> implements Item
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
		
		/**
		 * Gets the Property for this item.
		 * 
		 * @param pId the property id
		 * @return the Property
		 */
		public Property getItemProperty(Object pId)
		{
			return get(pId);
		}
		
		/**
		 * The property ids of this item.
		 * 
		 * @return the list of item property ids.
		 */
		public Collection<?> getItemPropertyIds()
		{
			return propertyIds;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean addItemProperty(Object pId, Property pProperty)
		{
			throw new UnsupportedOperationException("Indexed container item does not support adding new properties.");
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean removeItemProperty(Object pId)
		{
			throw new UnsupportedOperationException("Indexed container item does not support property removal.");
		}
		
	} 	// JVxContainerItem
	
	/**
	 * The <code>JVxContainerProperty</code> class represents one column in the
	 * table.
	 * 
	 * @author Stefan Wurm
	 */
	public final class JVxContainerProperty implements Property<Object>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
		/** Id of the Property. */
		private String propertyId;
		
		/** The value. */
		private Object value;
		
		/** The style. */
		private StyleInfo styleInfo;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>JVxContainerProperty</code>.
		 * 
		 * @param pPropertyId the Property ID of the new Property
		 */
		private JVxContainerProperty(String pPropertyId)
		{
			propertyId = pPropertyId;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
		/**
		 * {@inheritDoc}
		 */
		public Object getValue()
		{
			return value;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setValue(Object pNewValue)
		{
			value = pNewValue;
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
			Boolean readOnly = readonlyProperties.get(propertyId);
			
			if (readOnly == null)
			{
				return true;
			}
			else
			{
				return readOnly.booleanValue();
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setReadOnly(boolean pNewStatus)
		{
			throw new UnsupportedOperationException("setReadOnly not supported. Use the setReadonly from the ColumnDefinition of the DataBook");
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~		
		
		/**
		 * Gets the style properties.
		 * 
		 * @return the style properties.
		 */
		public StyleInfo getStyle()
		{
			return styleInfo;
		}
		
		/**
		 * Gets the style properties.
		 * 
		 * @param pStyleInfo the style properties.
		 */
		public void setStyle(StyleInfo pStyleInfo)
		{
			styleInfo = pStyleInfo;
		}
		
	} 	// JVxContainerProperty
	
} 	// JVxContainer
