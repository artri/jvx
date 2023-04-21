/*
 * Copyright 2017 SIB Visions GmbH
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
 * 17.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.treegrid.databinding;

import javax.rad.genui.control.UICellFormat;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.ITree;

import com.sibvisions.rad.ui.vaadin.ext.grid.databinding.DataBookValuePresentationProvider;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTreeGrid;
import com.sibvisions.util.GroupHashtable;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.TreeGrid;

/**
 * The {@link ValueFromTreePathValueProvider} is an {@link ValueProvider}
 * implementation which returns readily formatted values form a
 * {@link TreePath}.
 * 
 * @author Robert Zenz
 */
public class ValueFromTreePathValueProvider implements ValueProvider<TreePath, String>, ICellFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cache for {@link ICellFormat}s. */
	private GroupHashtable<ICellFormat, IImage, ICellFormat> cache = new GroupHashtable<>();
	
	/** If the current item is expanded. */
	private boolean currentItemIsExpanded = false;
	
	/** If the current item is a leaf. */
	private boolean currentItemIsLeaf = false;
	
	/** The {@link IDataBook}s that are used as datasource. */
	private IDataBook[] dataBooks = null;
	
	/**
	 * The {@link DataBookValuePresentationProvider} that is used for formatting
	 * the value.
	 */
	private DataBookValuePresentationProvider dataBookValuePresentationProvider = null;
	
	/** The {@link VaadinTreeGrid} that is the parent. */
	private VaadinTreeGrid tree = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ValueFromTreePathValueProvider}.
	 *
	 * @param pTree the {@link VaadinTreeGrid tree}.
	 */
	public ValueFromTreePathValueProvider(VaadinTreeGrid pTree)
	{
		super();
		
		tree = pTree;
		dataBooks = tree.getDataBooks();
		dataBookValuePresentationProvider = new DataBookValuePresentationProvider(null, null, this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(TreePath pSource)
	{
		try
		{
			IDataPage dataPage = getDataPage(pSource.getParentPath());
			IChangeableDataRow dataRow = dataPage.getDataRow(pSource.getLast());
			IDataBook dataBook = dataPage.getDataBook();
			String columnName = dataBook.getRowDefinition().getColumnView(ITree.class).getColumnName(0);
			
			dataBookValuePresentationProvider.setDataBook(dataBook);
			dataBookValuePresentationProvider.setColumnName(columnName);
			
			currentItemIsExpanded = ((TreeGrid)tree.getResource()).isExpanded(pSource);
			currentItemIsLeaf = !((TreeGrid)tree.getResource()).getDataProvider().hasChildren(pSource);
			
			return dataBookValuePresentationProvider.apply(dataRow);
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable
	{
		return getCellFormat(
				tree.getCellFormat(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pColumn),
				tree.getNodeImage(pDataBook, pDataPage, pDataRow, pColumnName, pRow, currentItemIsExpanded, currentItemIsLeaf));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link ICellFormat} for the given values.
	 * 
	 * @param pCellFormat the {@link ICellFormat}.
	 * @param pNodeImage the {@link IImage}.
	 * @return the {@link ICellFormat} according to the given values.
	 */
	private ICellFormat getCellFormat(ICellFormat pCellFormat, IImage pNodeImage)
	{
		if (pNodeImage != null)
		{
			ICellFormat cachedCellFormat = cache.get(pCellFormat, pNodeImage);
			
			if (cachedCellFormat == null)
			{
				if (pCellFormat == null)
				{
					cachedCellFormat = new UICellFormat(pNodeImage);
				}
				else
				{
					cachedCellFormat = new UICellFormat(
							pCellFormat.getBackground(),
							pCellFormat.getForeground(),
							pCellFormat.getFont(),
							pNodeImage,
							pCellFormat.getStyle(),
							pCellFormat.getLeftIndent());
				}
				
				cache.put(pCellFormat, pNodeImage, cachedCellFormat);
			}
			
			return cachedCellFormat;
		}
		else
		{
			return pCellFormat;
		}
	}
	
	/**
	 * Gets the {@link DataBook} for the given level.
	 * 
	 * @param pLevel the level.
	 * @return the {@link IDataBook} for the given level.
	 */
	private IDataBook getDataBook(int pLevel)
	{
		if (pLevel < dataBooks.length)
		{
			return dataBooks[pLevel];
		}
		else if (dataBooks[dataBooks.length - 1].isSelfJoined())
		{
			return dataBooks[dataBooks.length - 1];
		}
		
		return null;
	}
	
	/**
	 * Gets the {@link IDataPage} for the given {@link TreePath}.
	 * 
	 * @param pTreePath the {@link TreePath} for which to get the
	 *            {@link IDataPage}.
	 * @return the {@link IDataPage} for the given {@link TreePath}.
	 */
	private IDataPage getDataPage(TreePath pTreePath)
	{
		try
		{
			IDataPage dataPage = null;
			
			if (dataBooks[0].isSelfJoined())
			{
				dataBooks[0].getSelectedRow(); // sync
				dataPage = dataBooks[0].getDataPage(new TreePath());
			}
			else
			{
				dataPage = dataBooks[0].getDataPage();
			}
			
			if (pTreePath != null)
			{
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
						else if (dataBook.isSelfJoined() && level == dataBooks.length - 1)
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
			
			return dataPage;
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
}	// ValueFromTreePathValueProvider
