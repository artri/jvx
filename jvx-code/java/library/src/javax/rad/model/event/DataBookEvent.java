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
 * 03.07.2009 - [RH] - creation
 */
package javax.rad.model.event;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.TreePath;
import javax.rad.model.condition.ICondition;

/**
 * The <code>DataBookEvent</code> gives information about changes in the 
 * {@link IDataBook}.
 * 
 * @see javax.rad.model.IDataBook
 * 
 * @author Martin Handsteiner
 */
public class DataBookEvent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Specifies the type of change. */
	public enum ChangedType 
	{
		/** selected row will be change. */
		BEFORE_ROW_SELECTED,
		/** selected row has been changed. */
		AFTER_ROW_SELECTED,
		
		/** <code>IDataRow</code> will be inserted in memory. */
		BEFORE_INSERTING,
		/** <code>IDataRow</code> has been inserted in memory. */
		AFTER_INSERTING,
		/** <code>IDataRow</code> will be inserted into the storage. */
		BEFORE_INSERTED,
		/** <code>IDataRow</code> has been inserted into the storage. */
		AFTER_INSERTED,

		/** <code>IDataRow</code> will be updated in memory. */
		BEFORE_UPDATING,
		/** <code>IDataRow</code> has been updated in memory. */
		AFTER_UPDATING,
		/** <code>IDataRow</code> will be updated into the storage. */
		BEFORE_UPDATED,
		/** <code>IDataRow</code> has been updated into the storage. */
		AFTER_UPDATED,
		
		/** <code>IDataRow</code> will be deleted in memory. */
		BEFORE_DELETING,
		/** <code>IDataRow</code> has been deleted in memory. */
		AFTER_DELETING,
		/** <code>IDataRow</code> will be deleted into the storage. */
		BEFORE_DELETED,
		/** <code>IDataRow</code> has been deleted into the storage. */
		AFTER_DELETED,

		/** <code>IDataRow</code> will be restored. */
		BEFORE_RESTORE,
		/** <code>IDataRow</code> has been restored. */
		AFTER_RESTORE,

		/** <code>IDataBook</code> will be refreshed. */
		BEFORE_RELOAD,
		/** <code>IDataBook</code> has been refreshed. */
		AFTER_RELOAD,

		/** Filter will be changed. */
		BEFORE_FILTER_CHANGED,
		/** Filter has been changed. */
		AFTER_FILTER_CHANGED,

		/** Sort will be changed. */
		BEFORE_SORT_CHANGED,
		/** Sort has been changed. */
		AFTER_SORT_CHANGED,

		/** selected column will be change. */
		BEFORE_COLUMN_SELECTED,
		/** selected column has been changed. */
		AFTER_COLUMN_SELECTED,

        /** the <code>IDataBook</code> is opened. */
        OPENED,
        /** the <code>IDataBook</code> is closed. */
        CLOSED
	}
	
	/** The changed IDataBook. */
	private IDataBook changedDataBook;
	
	/** The changed type. */
	private ChangedType changedType;
	
	/** The original IDataRow. */
	private IDataRow originalDataRow;
	
    /** The old filter. */
    private ICondition oldFilter;
    
    /** The new filter. */
    private ICondition newFilter;
    
    /** The old sort. */
    private SortDefinition oldSort;
    
    /** The new sort. */
    private SortDefinition newSort;
    
    /** The old tree path. */
    private TreePath oldTreePath;
    
    /** The new tree path. */
    private TreePath newTreePath;
    
    /** The old selected row. */
    private int oldSelectedRow;
    
    /** The new selected row. */
    private int newSelectedRow;
    
    /** The old selected column. */
    private String oldSelectedColumn;
    
    /** The new selected column. */
    private String newSelectedColumn;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>DataBookEvent</code>.
     * This constructor is for events without tracking changes, like OPENED, CLOSED, BEFORE_RELOAD or AFTER_RELOAD.
     * 
     * Only filter and sort will deliver correct data for convenience.
     * Original data row, tree path, selected row and column are not set.
     * 
     * @param pChangedDataBook the changed <code>IDataBook</code>.
     * @param pChangedType the type of change.
     */
    public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType)
    {
        changedDataBook = pChangedDataBook;
        changedType = pChangedType;

        originalDataRow = null;
        oldFilter = changedDataBook.getFilter();
        newFilter = oldFilter;
        oldSort = changedDataBook.getSort();
        newSort = oldSort;
        oldTreePath       = null;
        newTreePath       = oldTreePath;
        oldSelectedRow    = -1;
        newSelectedRow    = oldSelectedRow;
        oldSelectedColumn = null;
        newSelectedColumn = oldSelectedColumn;
    }
    
    /**
     * Creates a new instance of <code>DataBookEvent</code>.
     * This constructor is for tracking row selection changes.
     * 
     * @param pChangedDataBook the changed <code>IDataBook</code>.
     * @param pChangedType the type of change.
     * @param pOriginalDataRow the old <code>IDataRow</code>.
     * @param pOldTreePath the old <code>TreePath</code>.
     * @param pNewTreePath the new <code>TreePath</code>.
     * @param pOldSelectedRow the old selected row.
     * @param pNewSelectedRow the new selected row.
     */
    public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType, IDataRow pOriginalDataRow, 
                         TreePath pOldTreePath, TreePath pNewTreePath,
                         int pOldSelectedRow, int pNewSelectedRow)
    {
        changedDataBook = pChangedDataBook;
        changedType = pChangedType;
        originalDataRow = pOriginalDataRow;

        oldTreePath       = pOldTreePath;
        newTreePath       = pNewTreePath;
        oldSelectedRow    = pOldSelectedRow;
        newSelectedRow    = pNewSelectedRow;

        oldFilter = changedDataBook.getFilter();
        newFilter = oldFilter;
        oldSort = changedDataBook.getSort();
        newSort = oldSort;
        try
        {
            oldSelectedColumn = changedDataBook.getSelectedColumn();
        }
        catch (ModelException ex)
        {
            oldSelectedColumn = null;
        }
        newSelectedColumn = oldSelectedColumn;
    }

    /**
     * Creates a new instance of <code>DataBookEvent</code>.
     * This constructor is for tracking column selection changes.
     * 
     * @param pChangedDataBook the changed <code>IDataBook</code>.
     * @param pChangedType the type of change.
     * @param pOriginalDataRow the old <code>IDataRow</code>.
     * @param pTreePath the <code>TreePath</code>.
     * @param pSelectedRow the selected row.
     * @param pOldSelectedColumn the old selected column.
     * @param pNewSelectedColumn the new selected column.
     */
    public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType, IDataRow pOriginalDataRow, 
                         TreePath pTreePath, int pSelectedRow,
                         String pOldSelectedColumn, String pNewSelectedColumn)
    {
        changedDataBook = pChangedDataBook;
        changedType = pChangedType;
        originalDataRow = pOriginalDataRow;

        oldSelectedColumn = pOldSelectedColumn;
        newSelectedColumn = pNewSelectedColumn;

        oldFilter = changedDataBook.getFilter();
        newFilter = oldFilter;
        oldSort = changedDataBook.getSort();
        newSort = oldSort;
        oldTreePath       = pTreePath;
        newTreePath       = pTreePath;
        oldSelectedRow    = pSelectedRow;
        newSelectedRow    = pSelectedRow;
    }

    /**
     * Creates a new instance of <code>DataBookEvent</code>.
     * This constructor is for tracking filter changes.
     * 
     * @param pChangedDataBook the changed <code>IDataBook</code>.
     * @param pChangedType the type of change.
     * @param pOldFilter the old filter.
     * @param pNewFilter the new filter.
     * @param pOldTreePath the old tree path.
     */
    public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType, 
                         ICondition pOldFilter, ICondition pNewFilter, TreePath pOldTreePath)
    {
        changedDataBook = pChangedDataBook;
        changedType = pChangedType;

        oldFilter = pOldFilter;
        newFilter = pNewFilter;
        
        originalDataRow = null;
        oldSort = changedDataBook.getSort();
        newSort = oldSort;
        oldTreePath       = pOldTreePath;
        newTreePath       = TreePath.EMPTY;
        oldSelectedRow    = -1;
        newSelectedRow    = oldSelectedRow;
        oldSelectedColumn = null;
        newSelectedColumn = oldSelectedColumn;
    }

    /**
     * Creates a new instance of <code>DataBookEvent</code>.
     * This constructor is for tracking sort changes.
     * 
     * @param pChangedDataBook the changed <code>IDataBook</code>.
     * @param pChangedType the type of change.
     * @param pOldSort the old sort.
     * @param pNewSort the new sort.
     * @param pOldTreePath the old tree path.
     */
    public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType, 
            SortDefinition pOldSort, SortDefinition pNewSort, TreePath pOldTreePath)
    {
        changedDataBook = pChangedDataBook;
        changedType = pChangedType;

        oldSort = pOldSort;
        newSort = pNewSort;
        
        originalDataRow = null;
        oldFilter = changedDataBook.getFilter();
        newFilter = oldFilter;
        oldTreePath       = pOldTreePath;
        newTreePath       = TreePath.EMPTY;
        oldSelectedRow    = -1;
        newSelectedRow    = oldSelectedRow;
        oldSelectedColumn = null;
        newSelectedColumn = oldSelectedColumn;
    }

	/**
	 * Creates a new instance of <code>DataBookEvent</code>.
	 * This constructor is for compatibility reasons. One of the other constructors should be used.
	 * 
	 * @param pChangedDataBook the changed <code>IDataBook</code>.
	 * @param pChangedType the type of change.
	 * @param pOriginalDataRow the old <code>IDataRow</code>.
	 */
    @Deprecated
	public DataBookEvent(IDataBook pChangedDataBook, ChangedType pChangedType, IDataRow pOriginalDataRow)
	{
		changedDataBook = pChangedDataBook;
		changedType = pChangedType;
		originalDataRow = pOriginalDataRow;

        oldFilter = changedDataBook.getFilter();
        newFilter = oldFilter;
        oldSort = changedDataBook.getSort();
        newSort = oldSort;
        oldTreePath       = null;
        newTreePath       = oldTreePath;
        oldSelectedRow    = -1;
        newSelectedRow    = oldSelectedRow;
        oldSelectedColumn = null;
        newSelectedColumn = oldSelectedColumn;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the {@link IDataBook} that is changed.
	 * 
	 * @return the {@link IDataBook} that is changed.
	 */
	public IDataBook getChangedDataBook()
	{
		return changedDataBook;
	}
	
	/**
	 * Gets the type of change.
	 * 
	 * @return the type of change.
	 */
	public ChangedType getChangedType()
	{
		return changedType;
	}
	
	/**
	 * Gets the original <code>IDataRow</code> before change.
	 * 
	 * @return the original <code>IDataRow</code> before change.
	 */
	public IDataRow getOriginalDataRow()
	{
		return originalDataRow;
	}
	
    /**
     * Gets the old filter before change.
     * 
     * @return the old filter before change.
     */
	public ICondition getOldFilter()
	{
	    return oldFilter;
	}
	
    /**
     * Gets the new filter after change.
     * 
     * @return the new filter after change.
     */
	public ICondition getNewFilter()
	{
	    return newFilter;
	}

    /**
     * Gets the old sort before change.
     * 
     * @return the old sort before change.
     */
	public SortDefinition getOldSort()
	{
	    return oldSort;
	}
	
    /**
     * Gets the new sort after change.
     * 
     * @return the new sort after change.
     */
	public SortDefinition getNewSort()
	{
	    return newSort;
	}
	
    /**
     * Gets the old selected row before change.
     * 
     * @return the old selected row before change.
     */
	public int getOldSelectedRow()
	{
	    return oldSelectedRow;
	}
	
    /**
     * Gets the new selected row after change.
     * 
     * @return the new selected row after change.
     */
    public int getNewSelectedRow()
    {
        return newSelectedRow;
    }
    
    /**
     * Gets the old tree path before change.
     * 
     * @return the old tree path before change.
     */
    public TreePath getOldTreePath()
    {
        return oldTreePath;
    }
    
    /**
     * Gets the new tree path after change.
     * 
     * @return the new tree path after change.
     */
    public TreePath getNewTreePath()
    {
        return newTreePath;
    }
    
    /**
     * Gets the old selected column before change.
     * 
     * @return the old selected column before change.
     */
    public String getOldSelectedColumn()
    {
        return oldSelectedColumn;
    }
    
    /**
     * Gets the new selected column after change.
     * 
     * @return the new selected column after change.
     */
    public String getNewSelectedColumn()
    {
        return newSelectedColumn;
    }
    
}	// DataBookEvent
