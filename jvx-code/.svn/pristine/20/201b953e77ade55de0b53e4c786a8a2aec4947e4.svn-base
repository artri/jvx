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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

/**
 * The {@link TreePathDataProvider} is an
 * {@link AbstractBackEndHierarchicalDataProvider} extension which provides the
 * {@link TreePath} as value.
 * 
 * @author Robert Zenz
 */
public class TreePathDataProvider extends AbstractBackEndHierarchicalDataProvider<TreePath, Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link IDataBook}s. */
	private IDataBook[] dataBooks = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link TreePathDataProvider}.
	 *
	 * @param pDataBooks the {@link IDataBook}s.
	 */
	public TreePathDataProvider(IDataBook[] pDataBooks)
	{
		super();
		
		dataBooks = pDataBooks;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount(HierarchicalQuery<TreePath, Object> pQuery)
	{
		return getChildCount(pQuery.getParent());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasChildren(TreePath pItem)
	{
		return getChildCount(pItem) > 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Stream<TreePath> fetchChildrenFromBackEnd(HierarchicalQuery<TreePath, Object> pQuery)
	{
		try
		{
			int[] treePathTemplate = null;
			
			if (pQuery.getParent() == null)
			{
				treePathTemplate = new int[1];
			}
			else
			{
				int[] requestedTreePath = pQuery.getParent().toArray();
				
				treePathTemplate = new int[requestedTreePath.length + 1];
				System.arraycopy(requestedTreePath, 0, treePathTemplate, 0, requestedTreePath.length);
			}
			
			IDataPage dataPage = getDataPage(pQuery.getParent());
			
			if (dataPage != null)
			{
				int start = Math.max(0, pQuery.getOffset());
				int end = Math.min(dataPage.getRowCount(), start + pQuery.getLimit());
				
				List<TreePath> treePaths = new ArrayList<TreePath>(end - start);
				
				for (int index = start; index < end; index++)
				{
					int[] treePath = treePathTemplate.clone();
					treePath[treePath.length - 1] = index;
					
					treePaths.add(new TreePath(treePath));
				}
				
				return treePaths.stream();
			}
			else
			{
				return Stream.empty();
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the child count for the given {@link TreePath}.
	 * 
	 * @param pTreePath the {@link TreePath} for which to get the child count.
	 * @return the child count for the given {@link TreePath}.
	 */
	private int getChildCount(TreePath pTreePath)
	{
		try
		{
			IDataPage dataPage = getDataPage(pTreePath);
			
			if (dataPage != null)
			{
				return dataPage.getRowCount();
			}
			else
			{
				return 0;
			}
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
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
	
}	// TreePathDataProvider
