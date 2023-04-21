/*
 * Copyright 2009 SIB Visions GmbH
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
 * 15.09.2009 - [MH] - creation
 * 17.04.2021 - [JR] - #2678: better exception handling if no PK is set
 */
package javax.rad.model;

import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;

import com.sibvisions.util.ArrayUtil;


/**
 * A <code>TreePathFinder</code> stores the primary key columns recursively for all master detail dependencies.
 * 
 * @author Martin Handsteiner
 */
public class TreePathFinder
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The parent tree path finder. */
	private TreePathFinder parent;
	/** The databook to navigate to. */
	private IDataBook      dataBook;
	/** The primaryKey of this row. */
	private Object[]       primaryKeyValues;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new TreePathFinder.
	 * 
	 * @param pDataRow the IChangeableDataRow
	 * @throws ModelException if an exception occurs.
	 */
	public TreePathFinder(IChangeableDataRow pDataRow) throws ModelException
	{
		this(null, pDataRow);
	}
	
	/**
	 * Constructs a new TreePathFinder.
	 * 
	 * @param pParentTreePathFinder the parent TreePathFinder.
	 * @param pDataRow the IChangeableDataRow
	 * @throws ModelException if an exception occurs.
	 */
	public TreePathFinder(TreePathFinder pParentTreePathFinder, IChangeableDataRow pDataRow) throws ModelException
	{
		parent = pParentTreePathFinder;
		if (pDataRow == null)
		{
			dataBook = parent.getDataBook();
			primaryKeyValues = null;
		}
		else
		{
			dataBook = pDataRow.getDataPage().getDataBook();
			if (pDataRow.getRowIndex() < 0)
			{
				primaryKeyValues = null;
			}
			else
			{
				primaryKeyValues = pDataRow.getValues(pDataRow.getRowDefinition().getPrimaryKeyColumnNames());
			}
		}
	}
	
    /**
     * Constructs a new TreePathFinder.
     * 
     * @param pParentTreePathFinder the parent TreePathFinder.
     * @param pDataBook in which should be searched.
     * @param pDataRow the IDataRow that should be searched, or null for deselection
     * @throws ModelException if an exception occurs.
     */
    public TreePathFinder(TreePathFinder pParentTreePathFinder, IDataBook pDataBook, IDataRow pDataRow) throws ModelException
    {
        parent = pParentTreePathFinder;
        if (pDataBook == null)
        {
            dataBook = parent.getDataBook();
        }
        else
        {
            dataBook = pDataBook;
        }
        if (pDataRow == null)
        {
            primaryKeyValues = null;
        }
        else
        {
            primaryKeyValues = pDataRow.getValues(pDataRow.getRowDefinition().getPrimaryKeyColumnNames());
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the root TreePathFinder.
	 * 
	 * @return the root TreePathFinder.
	 */
	public TreePathFinder getRoot()
	{
		TreePathFinder cur = this;
		TreePathFinder par = parent;
		
		while (par != null)
		{
			cur = par;
			par = cur.parent;
		}
		return cur;
	}
	
	/**
	 * Gets the parent TreePathFinder which has the given IDataBook.
	 * 
	 * @param pDataBook the databook to search
	 * @return the root TreePathFinder.
	 */
	public TreePathFinder getParentWithDataBook(IDataBook pDataBook)
	{
		TreePathFinder par = parent;
		
		while (par != null && par.dataBook != pDataBook)
		{
			par = par.parent;
		}
		return par;
	}
	
	/**
	 * Gets the parent TreePathFinder.
	 * 
	 * @return the parent TreePathFinder.
	 */
	public TreePathFinder getParent()
	{
		return parent;
	}
	
	/**
	 * Gets the current DataBook the current DataBook.
	 * 
	 * @return the row index.
	 */
	public IDataBook getDataBook()
	{
		return dataBook;
	}
	
	/**
	 * Gets the primary key.
	 * 
	 * @return the primary key.
	 */
	public Object[] getPrimaryKeyValues()
	{
		return primaryKeyValues;
	}
	
	/**
	 * Gets the child TreePathFinder to the given data row.
	 * @param pDataRow the DataRow.
	 * @return the child TreePathFinder to the given data row.
	 * @throws ModelException if an exception occurs.
	 */
	public TreePathFinder getChild(IChangeableDataRow pDataRow) throws ModelException
	{
		return new TreePathFinder(this, pDataRow);
	}
	
    /**
     * Gets the child TreePathFinder to the given data row.
     * @param pDataBook the DataBook.
     * @param pDataRow the DataRow.
     * @return the child TreePathFinder to the given data row.
     * @throws ModelException if an exception occurs.
     */
    public TreePathFinder getChild(IDataBook pDataBook, IDataRow pDataRow) throws ModelException
    {
        return new TreePathFinder(this, pDataBook, pDataRow);
    }
    
	/**
	 * Selects this tree path in all IDataBook's specified by this TreePathFinder.
	 * 
	 * @return true if the selection succeeded.
	 * @throws ModelException if an exception occurs.
	 */
	public boolean selectTreePath() throws ModelException
	{
		ArrayUtil<TreePathFinder> path = new ArrayUtil<TreePathFinder>();
		
		TreePathFinder current = this;
		do
		{
			path.add(0, current);
			
			current = current.parent;
		}
		while (current != null);
		
		return selectTreePathIntern(path);
	}
	
	/**
	 * Selects this tree path in all IDataBook's specified by this TreePathFinder.
	 * 
	 * @param pPath path list for selection.
	 * @return true if the selection succeeded.
	 * @throws ModelException if an exception occurs.
	 */
	private boolean selectTreePathIntern(ArrayUtil<TreePathFinder> pPath) throws ModelException
	{
		TreePathFinder current = pPath.remove(0);
		
		if (current.dataBook.isSelfJoined())
		{
			if (current.parent == null || current.parent.dataBook != current.dataBook)
			{
				current.dataBook.setTreePath(null);
			}
			else
			{
				current.dataBook.setTreePath(current.dataBook.getTreePath().getChildPath(current.dataBook.getSelectedRow()));
			}
		}
		
		int index = current.getIndexOfPrimaryKey();
		
		current.dataBook.setSelectedRow(index);

		if (pPath.size() > 0)
		{
			if (index < 0)
			{
				return false;
			}
			else
			{
				return selectTreePathIntern(pPath);
			}
		}
		else
		{
			return index >= 0 || current.primaryKeyValues == null;
		}
	}
	
	/**
	 * Gets the index of the primary key in this node.
	 * 
	 * @return the index of this primary key in this IDataBook.
	 * @throws ModelException if an exception occurs.
	 */
	public int getIndexOfPrimaryKey() throws ModelException
	{
		if (primaryKeyValues == null)
		{
			return -1;
		}
		else
		{
			String[] primaryKeyColumnNames = dataBook.getRowDefinition().getPrimaryKeyColumnNames();
			
			//#2678
			if (primaryKeyColumnNames == null)
			{
				throw new ModelException("No primary keys set for databook " + dataBook.getName() + "!");
			}
			
			ICondition condition = new Equals(primaryKeyColumnNames[0], primaryKeyValues[0]);
			
			for (int i = 1; i < primaryKeyColumnNames.length; i++)
			{
				condition = condition.and(new Equals(primaryKeyColumnNames[i], primaryKeyValues[i]));
			}
			
			return dataBook.searchNext(condition);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		toString(this, result);
		
		return result.toString();
		
	}

	/**
	 * Build internal the toString.
	 * @param pPath the path.
	 * @param pBuilder the StringBuilder.
	 */
	private static void toString(TreePathFinder pPath, StringBuilder pBuilder)
	{
		if (pPath != null)
		{
			toString(pPath.parent, pBuilder);
			
			pBuilder.append("DataBook: ");
			pBuilder.append(pPath.dataBook.getName());
			
			String[] pks = pPath.dataBook.getRowDefinition().getPrimaryKeyColumnNames();
			
			for (int i = 0; i < pks.length; i++)
			{
				pBuilder.append(", ");
				pBuilder.append(pks[i]);
				pBuilder.append("=");
				if (pPath.primaryKeyValues == null || i >= pPath.primaryKeyValues.length)
				{
					pBuilder.append("null");
				}
				else
				{
					pBuilder.append(pPath.primaryKeyValues[i]);
				}
			}
			pBuilder.append("\n");
		}
	}
	
}	// TreePathFinder
