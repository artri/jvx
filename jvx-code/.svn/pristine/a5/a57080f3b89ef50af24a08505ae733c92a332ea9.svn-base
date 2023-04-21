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
 * 01.10.2008 - [RH] - creation
 * 11.10.2008 - [RH] - bug fixes, toString() optimized, NO_SELECTED_ROW optimized
 * 12.10.2008 - [RH] - bug fixes, sync()
 *                     writebackIsolationsLevel support added
 * 02.11.2008 - [RH] - InsertEnabled, UpdateEnabled, DeleteEnabled, ReadOnly added
 *                     getRowCount, SelectedDataRow support added
 *                     comments added
 *                     bugs fixed
 * 11.11.2008 - [RH] - use instead SetSelectedRow -> SetSelectRowInternal with
 *                     BEFORE & AFTER SELECTED Event-> bugs fixed
 *                     DeleteCascade added, notifyMasterReferenceColumnsChanged(IDataRow) add
 *                     MasterRow set in MemDataPage
 * 17.11.2008 - [RH] - get/setSelectionMode added; DefaultDeselected removed
 *                     delete bug with IDataBook.DATAROW WritebackIsolationLevel fixed
 * 16.04.2009 - [RH] - remove/add/getDetailDataBooks moved to IDataBook.
 *                     checks in open for PK columns and MasterReferenceDefinition added
 * 27.04.2009 - [RH] - interface review changes tested and fixed
 * 28.04.2009 - [RH] - javadoc added, NLS removed
 * 13.05.2009 - [RH] - sort/filter in memory added
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 02.07.2009 - [JR] - invokeDataBookListeners: finally block [BUGFIX]
 * 13.10.2009 - [RH] - WriteBackIsolationLevel.DATASOURCE is considered at saveAllDataRows() [BUGFIX]
 * 14.10.2009 - [RH] - In AfterDeleting event is the selected row corrected in WriteBackIsolationLevel.DATASOURCE [BUGFIX]
 * 15.10.2009 - [RH] - Insert over Master - Detail reference in WriteBackIsolationLevel.DATASOURCE fixed (UID missed) [BUGFIX]
 *            - [JR] - clearFilterSortInMemDataPages: isOutOfSync called
 *                   - clearFilterSortinMemDataPages renamed to clearFilterSortInMemDataPages
 * 19.03.2010 - [RH] - lock before delete only happens, if isLockAndRefetchEnabled() == true.
 * 25.03.2010 - [JR] - #92: createNewRow implemented
 * 21.07.2010 - [RH] - setTreePath = null if emptyPage is set.
 * 29.09.2010 - [RH] - countRows renamed to getEstimatedRowCount()
 * 15.10.2010 - [RH] - detailChanged renamed to notifyDetailChanged
 *                     #186: notifyDetailedChanged changed, to set the DetailedChanged state correct.
 *                           store(), restore() call now setDetailsChangedInMasterBook()
 * 28.10.2010 - [RH] - #197: insert bug with DATASOURCE level with master/detail. reference columns doesn't copy to details.
 *                           rehash is moved for getDataPageIntern to notifyMasterChanged. UID is correct reset to null.
 *                           updates on null PK (reference columns) not supported anymore. Won't work correctly.
 * 28.10.2010 - [HM] - open() sets the open state now correctly -> at the end of the open, and not before
 *                     setValues (null, xxx) uses now all columns as default.
 * 15.02.2011 - [RH] - #286: no DataPage to master row exists, then return null -> no changes can exits - performance tuning in getChangedDataRows()
 *                     #288: master DataRow rehash not working in DATASOUCE Level over more then 1 hierarchy ! - bug fixed.
 * 24.02.2011 - [RH] - #290: if the master DataBook is in DATASOURCE level && Inserting() no fetch is necessary, because no rows can exists!
 * 06.03.2011 - [JR] - #115:
 *                     * open: check allowed values
 *                     * createNewRow: use default value
 * 21.03.2011 - [RH] - #315: saveSelectedRow() bug with more then one leave with isInsertung state in DATASOURCE level
 * 31.03.2011 - [JR] - #318: setRowDefinition() clear/sets all controls to the row definition
 * 31.03.2011 - [RH] - #163 - IChangeableDataRow should support isWritableColumnChanged
 * 13.04.2011 - [RH] - #336 - First check if a DataPage with the UID exists. -> Reuse new DataPages in DATASOURCE level.
 * 12.05.2011 - [RH] - #347 - DataBook in DATASOURCE level should in notifyMasterChanged always check if rehash is required !
 *                     #348 - sync() fails if a seljoined MemDataBook has the TreePath wrong - null check added.
 * 13.05.2011 - [RH] - #350 - MemDataBook should remove all details in saveSelectedRow(), if a row is deleted
 * 23.05.2011 - [RH] - #357 - setSort on MemDataBook with setMemSort(true), doesn't setSelectedRow correct after sort
 * 30.05.2011 - [HM] - #374 - If an insert happens, after that more rows have to be fetched, then wrong rows will be fetched
 * 30.05.2011 - [HM] - #375 - delete, cancelEditing missing
 * 30.05.2011 - [HM] - #369 - delete all rows from a databook, Master changed has to be set.
 * 30.05.2011 - [HM] - #376 - restore row fails in DATASOURCE level with more then one detail level with isInserting rows
 * 31.05.2011 - [RH] - #377 - MemDataBook has invalid javadoc
 * 01.06.2011 - [RH] - #378 - Exception in AfterDeleted Event, if an insert() is called in the user event
 * 30.06.2011 - [RH] - #405 - NullPointer Exception in notifyMasterChanged(), if parent IDataBook out of sync
 * 14.07.2011 - [RH] - #424 - MemDataBook.setRowDefintion(null) should addControls
 *                     #425 - MemDataBook close should not close the detail DataBooks.
 * 31.10.2011 - [RH] - #492 - MemDataBook.restoreAllRows set wrong SelectedRow :: save the current row after the restore e.g is Inserting....
 * 02.11.2011 - [RH] - #495 - MemDataBook removeDataPage should setTreePath(null)
 * 13.11.2011 - [RH] - #505 - delete should should check the WritebackIsolation level
 * 15.11.2011 - [RH] - #507 - restore in MemDataBook delete() has be done after the BEFORE_DELETING event
 * 17.11.2011 - [JR] - #509 - setReadOnlyWithoutSave implemented
 * 10.12.2012 - [JR] - #615 - setFilter should not save in mem filter mode and datasource writeback isolation level
 * 19.12.2012 - [RH] - Code Review - setUpdating(), setDeleting(), setInserting(), setUID(), store(), restore(), setDetailChanged() moved to ChangeableDataRow
 *                                 - setValueInternal removed, functionality is moved to update()
 *                                 - Changes management in insert, update, delete, store, restore, setDetailChanged is moved to MemDataPage
 * 10.04.2013 - [RH] - #617 - saveAllDataBooks doesn't save all rows in DATASOURCE level - fixed
 * 10.04.2013 - [RH] - #617 - restoreAllRows fails with ArrayIndexOutOfBoundsException - fixed
 * 10.04.2013 - [RH] - #618 - restoreAllRows throws an Exception - fixed
 * 12.04.2013 - [RH] - #361 - Need isUpdate/Delete/InsertAllowed and isUpdate/Delete/InsertEnabled should only get what is set - realized!
 * 13.04.2013 - [RH] - #155 - Reload with SelectionMode==CURRENT and selfjoined tree's - fixed.
 * 31.07.2013 - [RH] - #746 - setFilter/reload on a master databook, doesn't sync details
 * 03.09.2013 - [RH] - #783 - MemDataBook rehash causes Exception
 * 06.09.2013 - [RH] - #770 - MemDataBook: rehash in an inserting row does not work
 * 24.09.2013 - [RH] - #800 - MemDataBook ArrayIndexOutOfBoundsException during insert
 * 27.09.2013 - [RH] - #804 - MemDataBook for UITree with self-joined data
 * 02.04.2014 - [RZ] - #993 - default cell editor now comes from UICellEditor
 * 03.04.2014 - [RZ] - #998 - allowed values are now padded with a null with the column is nullable
 * 13.11.2014 - [RZ] - #973 - fixed possible infinite loops regarding self-joined databooks
 * 10.09.2015 - [JR] - #1461: hasControl used to reduce invokeLater calls
 */
package com.sibvisions.rad.model.mem;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IDataSource;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.SortDefinition;
import javax.rad.model.TreePath;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataBookEvent.ChangedType;
import javax.rad.model.event.DataBookHandler;
import javax.rad.model.event.IReadOnlyChecker;
import javax.rad.model.event.IRowCalculator;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.model.ui.ITableControl;
import javax.rad.model.ui.ITreeControl;
import javax.rad.ui.IComponent;
import javax.rad.ui.IFactory;
import javax.rad.util.EventHandler;

import com.sibvisions.rad.genui.celleditor.UIEnumCellEditor;
import com.sibvisions.rad.model.Filter;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>MemDataBook</code> is a storage independent table, and handles all operations
 * to load, save and manipulate table oriented data. <br>
 * An <code>MemDataBook</code> has at least one <code>MemDataPage</code> to store all
 * <code>DataRow</code>'s of this <code>MemDataBook</code>. If an <code>MemDataBook</code>
 * has detail <code>IDataBook</code>'s, it handles for each detail <code>IDataBook</code>
 * the <code>IDataPage</code>'s. Thats necessary because every change of the selected
 * <code>IDataRow</code> needs to initiate the change of the corresponding details.
 * So the <code>IDataBook</code> adds the appropriate <code>IDataPage</code>'s into the
 * detail <code>IDataBook</code>'s.<br>
 * The <code>MemDataBook</code> is also a <code>IChangeableDataRow</code>, the selected row
 * of the <code>MemDataBook</code>.
 * 
 * @see com.sibvisions.rad.model.mem.MemDataPage
 * @see javax.rad.model.IDataPage
 * @see javax.rad.model.IDataBook
 * @see javax.rad.model.IRowDefinition
 * @see javax.rad.model.IChangeableDataRow
 * @see javax.rad.model.IDataSource
 * 
 * @author Roland Hörmann
 */
public class MemDataBook extends ChangeableDataRow
                         implements IDataBook
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * The internal unique for the MemDataBook. Its used for new DataRow's as identifier
     * till the get a unique Primary Key value.
     */
    private static int iUID = 0;
    
    /** The cell formatter provider. */
    private static EventHandler<IRowCalculator> rowCalculatorProvider = new EventHandler<IRowCalculator>(IRowCalculator.class);

    /** The node formatter provider. */
    private static EventHandler<IReadOnlyChecker> readOnlyCheckerProvider = new EventHandler<IReadOnlyChecker>(IReadOnlyChecker.class);

    /** The TreePath inside self joined IDataBook's. */
    private transient TreePath        treePath = TreePath.EMPTY;

    /** It holds the default SelectionMode of the MemDataBook. Default is SelectionMode.CURRENT_ROW. */
    private transient SelectionMode   iSelectionMode = SelectionMode.CURRENT_ROW;
    
    /** It holds the current SelectionMode of the MemDataBook. Default is SelectionMode.CURRENT_ROW. */
    private transient SelectionMode   currentSelectionMode = null;
    /** For ignoring the selected Row initialization. */
    private transient boolean   ignoreCorrectSelectedRow = false;
    /** Flag to detect, that in any case should be synced. */
    private transient boolean   syncOnNotifyRepaintControls = false;
    
    /** True, if the after row selected event is also dispatched, when the same row is selected again. */
    private transient boolean   alwaysDispatchSelectionEvent = false;
    
    /** The Filter of the MemDataBook. */
    private transient ICondition      cFilter;
    
    /** The Sort of the MemDataBook. */
    private transient SortDefinition  sSort;
    
    /** The Sort of the MemDataBook. */
    private transient SortDefinition  sSortSortableColumnsOnly;
    
    /** The ReferenceDefinition to the master IDataBook. */
    private transient ReferenceDefinition rdMasterReference;
    
    /** The ReferenceDefinition to the master IDataBook. */
    private transient ReferenceDefinition rdTreeRootReference;

    /** The ReferenceDefinition to the master IDataBook. */
    private transient String[] saTreeRootMasterColumnNames;

    /** The write back isolation level. */
    private transient WriteBackIsolationLevel iWritebackIsolationLevel;
    
    /** The <code>ArrayUtil</code> of all detail <code>IDataBooks</code>. */
    private transient ArrayUtil<WeakReference<IDataBook>> auDetailDataBooks;
    /** referenced master columns by details. */
    private transient String[] saMasterColumns = null;
    
    /** The DataSource of the MemDataBook. */
    private transient IDataSource dsDataSource;

    /** It stores for the lazy reload, the last selected Row PK column values. */
    private transient ArrayUtil<IDataRow> auStoredSelection = new ArrayUtil<IDataRow>();
    
    /**
     * The current MemDataPage of the MemDataBook.
     * It gets synchronized at the first use. (lazy load)
     */
    private transient MemDataPage dpCurrentDataPage;
    
    /** The Empty MemDataPage, which is used as long no DataRows are available. */
    private transient MemDataPage dpEmptyDataPage;

    /** The current calculated data row. */
    private transient ChangeableDataRow currentCalculatedDataRow;
    
    /** The additional data row. */
    private transient ChangeableDataRow additionalDataRow;
    /** The additional row definition. */
    private transient IRowDefinition additionalRowDefinition;
    
    /** True, if the additional data row should be visible. */
    private transient boolean additionalDataRowVisible = false;

    /** True, if the row should be moved to the correct possition after save. */
    private transient boolean sortDataRowOnSave = true;

    /**
     * The Hashtable of all MemDataPages of the MemDataBook. It holds for each master DataRow
     * a MemDataPage. If the master DataRow is Inserting (new), the MemDataPage is stored
     * under the UID.
     */
    private transient Hashtable<Object, MemDataPage>   htDataPagesCache   = new Hashtable<Object, MemDataPage>();
    /** gives the amount of changed data pages. */
    private transient int changeCounter = 0;
    /** Stores temporary the currentDataPage of a master data book during refresh. */
    private transient MemDataPage   temporaryMasterDataPage   = null;
    
    /** The row calculator. */
    private transient IRowCalculator rowCalculator;
    /** The row calculator. */
    private transient IReadOnlyChecker readOnlyChecker;
    
    /** The <code>EventHandler</code> for before selected event. */
    private transient DataBookHandler eventBeforeRowSelected;
    /** The <code>EventHandler</code> for after selected event. */
    private transient DataBookHandler eventAfterRowSelected;
    /** The <code>EventHandler</code> for before inserting event. */
    private transient DataBookHandler eventBeforeInserting;
    /** The <code>EventHandler</code> for after inserting event. */
    private transient DataBookHandler eventAfterInserting;
    /** The <code>EventHandler</code> for before inserted event. */
    private transient DataBookHandler eventBeforeInserted;
    /** The <code>EventHandler</code> for after inserted event. */
    private transient DataBookHandler eventAfterInserted;
    /** The <code>EventHandler</code> for before updating event. */
    private transient DataBookHandler eventBeforeUpdating;
    /** The <code>EventHandler</code> for after updating event. */
    private transient DataBookHandler eventAfterUpdating;
    /** The <code>EventHandler</code> for before updated event. */
    private transient DataBookHandler eventBeforeUpdated;
    /** The <code>EventHandler</code> for after updated event. */
    private transient DataBookHandler eventAfterUpdated;
    /** The <code>EventHandler</code> for before deleting event. */
    private transient DataBookHandler eventBeforeDeleting;
    /** The <code>EventHandler</code> for after deleting event. */
    private transient DataBookHandler eventAfterDeleting;
    /** The <code>EventHandler</code> for before deleted event. */
    private transient DataBookHandler eventBeforeDeleted;
    /** The <code>EventHandler</code> for after deleted event. */
    private transient DataBookHandler eventAfterDeleted;
    /** The <code>EventHandler</code> for before restore event. */
    private transient DataBookHandler eventBeforeRestore;
    /** The <code>EventHandler</code> for after restore event. */
    private transient DataBookHandler eventAfterRestore;
    /** The <code>EventHandler</code> for before reload event. */
    private transient DataBookHandler eventBeforeReload;
    /** The <code>EventHandler</code> for after reload event. */
    private transient DataBookHandler eventAfterReload;
    /** The <code>EventHandler</code> for before filter changed event. */
    private transient DataBookHandler eventBeforeFilterChanged;
    /** The <code>EventHandler</code> for after filter changed event. */
    private transient DataBookHandler eventAfterFilterChanged;
    /** The <code>EventHandler</code> for before sort changed event. */
    private transient DataBookHandler eventBeforeSortChanged;
    /** The <code>EventHandler</code> for after sort changed event. */
    private transient DataBookHandler eventAfterSortChanged;
    /** The <code>EventHandler</code> for before selected event. */
    private transient DataBookHandler eventBeforeColumnSelected;
    /** The <code>EventHandler</code> for after selected event. */
    private transient DataBookHandler eventAfterColumnSelected;
    /** The <code>EventHandler</code> for before selected event. */
    private transient DataBookHandler eventOpened;
    /** The <code>EventHandler</code> for after selected event. */
    private transient DataBookHandler eventClosed;
    
    /** Name of the MemDataBook. */
    private String          sName;
    /** The current selected row index. */
    private String          sSelectedColumn = null;
    
    /**
     * The read a head row count of the <code>RemoteDataBook</code>. This means how many rows are
     * read ahead in the <code>MemDataPage</code>.
     */
    private transient int iReadAheadRowCount  = 35;
    /** The current selected row index. */
    private int iSelectedRowIndex   = -1;
    
    /** For selecting correct row in saveAllRows. */
    private transient int iOldSelectedRowSaveAllRows;
    /** For selecting correct row in saveAllRows. */
    private transient IDataPage dpOldCurrentDataPageSaveAllRows;

    /**
     * It determines that the master has changed and this IDataBoolk needs to synchronize
     * to the current MemDataPage and selected row depending on the Selection Mode.
     */
    private transient boolean         bMasterChanged  = false;
    /** It determines that insert()'s are allowed. */
    private transient boolean         bInsertEnabled  = true;
    /** It determines that update()'s (setValueXXX) are allowed. */
    private transient boolean         bUpdateEnabled  = true;
    /** It determines that delete()'s are allowed. */
    private transient boolean         bDeleteEnabled  = true;
    /** It determines that lock on refetch is enabled. */
    private transient boolean         bLockAndRefetchEnabled = false;
    /** It determines if the MemDataBook is read only. -&gt; all change operations are allowed. */
    private transient boolean         bReadOnly       = false;
    /** It defines if all details will be before delete cascade. */
    private transient boolean         bDeleteCascade  = true;
    /** Determines if the sort should be made in memory. */
    protected transient boolean       bMemSort        = true;
    /** Determines if the filter should be made in memory. */
    protected transient boolean       bMemFilter      = true;
    /** Determines if the RemoteDataBook should write back the changes to the AbstractStorage. */
    private transient boolean         bWritebackEnabled = false;
    /** It determines if the MemDataBook is open. */
    private transient boolean         bIsOpen         = false;

    /** Ignore recursiv reload events. */
    private transient boolean ignoreReload = false;

    /** The ui factory. */
    private transient IFactory factory = null;
    
    /** invoke repaint listeners. */
    private transient boolean invokeRepaintListeners = true;
    /** invoke repaint listeners. */
    private transient boolean invokeLaterRepaintListenersCalled = false;
    
    /** Optimization for fast row access with singleton instance of data row for search, filter, sort. */
    protected transient DataRow rowInstance1 = null;
    /** Optimization for fast row access with singleton instance of data row for search, filter, sort. */
    protected transient DataRow rowInstance2 = null;
    
    /** The root data book. */
    protected transient IDataBook rootDataBook = this;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Contructs a new <code>MemDataBook</code> with an empty {@link RowDefinition}.
     */
    public MemDataBook()
    {
    }

    /**
     * Constructs a new <code>MemDataBook</code> with the given {@link RowDefinition}.
     * 
     * @param pRowDefinition the row definition for the data book
     */
    public MemDataBook(IRowDefinition pRowDefinition)
    {
        super(pRowDefinition);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public IDataBook getDataBook()
    {
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return sName;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setName(String pName) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                throw new ModelException(getName() + ": Setting the name of an already open databook is not allowed.");
            }
            sName = pName;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setWritebackIsolationLevel(WriteBackIsolationLevel pIsolationLevel) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen && iWritebackIsolationLevel != pIsolationLevel && pIsolationLevel == WriteBackIsolationLevel.DATA_ROW)
            {
                sync();
                if (hasChanges())
                {
                    boolean isAnythingElseChanged = false;
                    for (IDataPage dataPage : getDataPages())
                    {
                        if (dataPage == dpCurrentDataPage)
                        {
                            int[] changedRows = dataPage.getChangedRows();
                            if (changedRows.length > 1 || (changedRows.length == 1 && changedRows[0] != iSelectedRowIndex))
                            {
                                isAnythingElseChanged = true;
                                break;
                            }
                        }
                        else if (dataPage.getChangedRows().length > 0)
                        {
                            isAnythingElseChanged = true;
                            break;
                        }
                    }
                    if (isAnythingElseChanged)
                    {
                        saveAllRows();
                    }
                 }
            }
            iWritebackIsolationLevel = pIsolationLevel;
        }
    }

    /**
     * {@inheritDoc}
     */
    public WriteBackIsolationLevel getWritebackIsolationLevel()
    {
        if (iWritebackIsolationLevel == null)
        {
            IDataSource dataSource = getDataSource();
            if (dataSource != null)
            {
                return dataSource.getWritebackIsolationLevel();
            }
            return WriteBackIsolationLevel.DATA_ROW;
        }
        return iWritebackIsolationLevel;
    }
        
    /**
     * {@inheritDoc}
     */
    public void setRowDefinition(IRowDefinition pRowDefinition) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                throw new ModelException(getName() + ": Setting the row definition of an already open databook is not allowed!");
            }
            
            IControl[] controls = null;
            
            //cleanup old row definition
            if (rdRowDefinition != null)
            {
                controls = getControls();
                
                for (int i = 0; i < controls.length; i++)
                {
                    rdRowDefinition.removeControl(controls[i]);
                }
            }
            
            rdRowDefinition = pRowDefinition;
            
            // #424 - MemDataBook.setRowDefintion(null) should addControls
            if (rdRowDefinition == null)
            {
                rdRowDefinition = new RowDefinition();
            }
            
            //configure new row definition
            if (rdRowDefinition != null)
            {
                if (controls == null)
                {
                    controls = getControls();
                }
                
                for (int i = 0; i < controls.length; i++)
                {
                    rdRowDefinition.addControl(controls[i]);
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataSource getDataSource()
    {
        return dsDataSource;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSource(IDataSource pDataSource) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                throw new ModelException(getName() + ": Setting the datasource of an already open databook is not allowed.");
            }
            dsDataSource = pDataSource;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ReferenceDefinition getMasterReference()
    {
        return rdMasterReference;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMasterReference(ReferenceDefinition pMasterReference) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                throw new ModelException(getName() + ": Setting the master reference of an already open databook is not allowed.");
            }
            rdMasterReference = pMasterReference;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelfJoined()
    {
        return rdMasterReference != null && rdMasterReference.getReferencedDataBook() == this;
    }

    /**
     * {@inheritDoc}
     */
    public IDataPage getDataPage(TreePath pTreePath) throws ModelException
    {
        if (rdTreeRootReference == null)
        {
            return getDataPage(null, pTreePath);
        }
        else
        {
            return getDataPage(rdTreeRootReference.getReferencedDataBook(), pTreePath);
        }
    }

    /**
     * {@inheritDoc}
     */
    public IDataPage getDataPage(IDataRow pRootRow, TreePath pTreePath) throws ModelException
    {
        synchronized (rootDataBook)
        {
            IDataPage dpDataPage = getDataPageWithRootRow(pRootRow);
            
            if (pTreePath != null)
            {
                for (int i = 0; i < pTreePath.length(); i++)
                {
                    // #348 - sync() fails if a seljoined MemDataBook has the TreePath wrong
                    IDataRow masterRow = dpDataPage.getDataRow(pTreePath.get(i));
                    if (masterRow == null)
                    {
                        return null;
                    }
                    else
                    {
                        dpDataPage = getDataPageIntern(masterRow);
                    }
                }
            }
    
            return dpDataPage;
        }
    }

    /**
     * {@inheritDoc}
     */
    public IDataPage getDataPageWithRootRow(IDataRow pRootDataRow) throws ModelException
    {
        synchronized (rootDataBook)
        {
            // if no master reference exits, then, don't call getDataPageIntern!!!!
            if (rdMasterReference == null)
            {
                sync();
                return dpCurrentDataPage;
            }
            return getDataPageIntern(getMasterDataRowFromRootDataRow(pRootDataRow));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ReferenceDefinition getRootReference()
    {
        return rdTreeRootReference;
    }

    /**
     * {@inheritDoc}
     */
    public void setRootReference(ReferenceDefinition pTreeRootReference) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                throw new ModelException(getName() + ": Setting the root reference of an already open databook is not allowed.");
            }
            rdTreeRootReference = pTreeRootReference;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public TreePath getTreePath()
    {
        synchronized (rootDataBook)
        {
            try
            {
                sync();
            }
            catch (ModelException e)
            {
                // Ignore it!
            }
            return treePath;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setTreePath(TreePath pTreePath) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for setting the tree path.");
            }
            
            if (isSelfJoined() && (!treePath.equals(pTreePath) || bMasterChanged))
            {
                invokeTreeSaveEditingControls();
                
                // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
                // therefore an sync is needed!
                sync();
                
                if (pTreePath == null)
                {
                    pTreePath = TreePath.EMPTY;
                }
                boolean hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
                boolean hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
                IDataRow drOriginalDataRow = hasBeforeRowSelected || hasAfterRowSelected ? createDataRow(null) : null;
                TreePath oldTreePath = treePath;
                TreePath newTreePath = pTreePath;
                int oldSelectedRow = getSelectedRow();
                int newSelectedRow;
                if (getSelectionMode() == SelectionMode.DESELECTED
                        || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED
                        || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
                {
                    newSelectedRow = -1;
                }
                else
                {
                    newSelectedRow = 0;
                }

                if (hasBeforeRowSelected)
                {
                    eventBeforeRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_ROW_SELECTED, drOriginalDataRow,
                            oldTreePath, newTreePath, oldSelectedRow, newSelectedRow));
                    
                    hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
                }

                saveDataRowLevel(null);

                treePath = pTreePath;

                dpCurrentDataPage = (MemDataPage)getDataPage(treePath);
                if (dpCurrentDataPage == null)
                {
                    treePath = TreePath.EMPTY;
                    newTreePath = treePath;
                    
                    dpCurrentDataPage = (MemDataPage)getDataPage(treePath);
                    if (dpCurrentDataPage == null)
                    {
                        dpCurrentDataPage = dpEmptyDataPage;
                    }
                }

                setSelectedRowInternal(newSelectedRow);
                newSelectedRow = getSelectedRow();

                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                {
                    eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));

                    hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
                }
                if (hasAfterRowSelected)
                {
                    eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                            oldTreePath, newTreePath, oldSelectedRow, newSelectedRow));
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void notifyMasterChanged()
    {
        synchronized (rootDataBook)
        {
            boolean isMasterReallyChanged = true; 
            try
            {
                if (rdMasterReference != null)
                {
                    IDataBook referencedDataBook = rdMasterReference.getReferencedDataBook();
                    boolean isSelfJoined = isSelfJoined();
                    IDataBook treeRootReferencedDataBook;
                    if (isSelfJoined && rdTreeRootReference != null)
                    {
                        treeRootReferencedDataBook = rdTreeRootReference.getReferencedDataBook();
                    }
                    else
                    {
                        treeRootReferencedDataBook = null;
                    }
                    
                    // #347 - DataBook in DATASOURCE level should in notifyMasterChanged always check if rehash is required !
                    // if master row is inserted and saved, then rehash!
                    if (referencedDataBook != null
                            && !referencedDataBook.isOutOfSync()
                            && referencedDataBook.getUID() != null
                            && !referencedDataBook.isInserting())
                    {
                        // rehash current DataPage, because PK changed after insert in Master
                        // #405 - NullPointer Exception in notifyMasterChanged(), if parent IDataBook out of sync
                        if (dpCurrentDataPage != null && referencedDataBook.getUID() != null)
                        {
                            MemDataPage mdpRehash = htDataPagesCache.remove(referencedDataBook.getUID());
            
                            if (mdpRehash != null)
                            {
                                // [HM] Force refetch of the Detail DataPage, if it is possible and not changed
                                // otherwise rehash the page.
                                if (!isDataPageRefetchPossible() || mdpRehash.getChangedRows().length > 0)
                                {
                                    IDataRow drMaster = getMasterDataRow(referencedDataBook);
                                    mdpRehash.setMasterDataRow(drMaster);
                                    if (mdpRehash.getChangedRows().length > 0)
                                    {
                                        mdpRehash.fetchToRow(mdpRehash.getRowCount());
                                    }
                                    htDataPagesCache.put(drMaster, mdpRehash);
                                }
                                else if (mdpRehash == dpCurrentDataPage) // drop current data page, if the rehashed data page is the current
                                {
                                    // [HM] enable selecting current datarow!
                                    handleStoreSelection(iSelectionMode);
                                    
                                    if (mdpRehash.hasChanges())
                                    {
                                        changeCounter--;
                                    }
                                    dpCurrentDataPage = null;
                                }
                                else if (mdpRehash.hasChanges())
                                {
                                    changeCounter--;
                                }
                            }
                        }
                    }
                    else if (treeRootReferencedDataBook != null
                            && !treeRootReferencedDataBook.isOutOfSync()
                            && treeRootReferencedDataBook.getUID() != null
                            && !treeRootReferencedDataBook.isInserting())
                    {
                        MemDataPage mdpRehash = htDataPagesCache.remove(treeRootReferencedDataBook.getUID());
                        if (mdpRehash != null)
                        {
                            // [HM] Force refetch of the Detail DataPage, if it is possible and not changed
                            // otherwise rehash the page.
                            if (!isDataPageRefetchPossible() || mdpRehash.getChangedRows().length > 0)
                            {
                                IDataRow drMaster = getMasterDataRowFromRootDataRow(treeRootReferencedDataBook).createDataRow(null);
        
                                mdpRehash.setMasterDataRow(drMaster);
                                htDataPagesCache.put(drMaster, mdpRehash);
                            }
                            else if (mdpRehash == dpCurrentDataPage) // drop current data page, if the rehashed data page is the current
                            {
                                // [HM] enable selecting current datarow!
                                handleStoreSelection(iSelectionMode);
                                
                                if (mdpRehash.hasChanges())
                                {
                                    changeCounter--;
                                }
                                dpCurrentDataPage = null;
                            }
                            else if (mdpRehash.hasChanges())
                            {
                                changeCounter--;
                            }
                        }
                    }
                    else if (!isSelfJoined && dpCurrentDataPage != null
                            && !referencedDataBook.isOutOfSync()
                            && ((referencedDataBook.getUID() != null 
                                    && htDataPagesCache.get(referencedDataBook.getUID()) == dpCurrentDataPage) 
                             || (referencedDataBook.getUID() == null && getUID(dpCurrentDataPage.getMasterDataRow()) == null
                                    && referencedDataBook.equals(dpCurrentDataPage.getMasterDataRow(), rdMasterReference.getReferencedColumnNames()))))
                    {
                        isMasterReallyChanged = false;
                    }
                    else if (treeRootReferencedDataBook != null && dpCurrentDataPage != null
                            && !treeRootReferencedDataBook.isOutOfSync()
                            && ((treeRootReferencedDataBook.getUID() != null 
                                    && htDataPagesCache.get(treeRootReferencedDataBook.getUID()) == dpCurrentDataPage) 
                             || (treeRootReferencedDataBook.getUID() == null && getUID(dpCurrentDataPage.getMasterDataRow()) == null
                                    && getMasterDataRowFromRootDataRow(treeRootReferencedDataBook).equals(dpCurrentDataPage.getMasterDataRow(), saTreeRootMasterColumnNames))))
                    {
                        isMasterReallyChanged = false;
                    }
                }
            }
            catch (ModelException e)
            {
                throw new RuntimeException(e);
            }

            if (!bMasterChanged && isMasterReallyChanged)
            {
                try
                {
                    // master changed is only allowed to be set after rehashing, because we have to
                    // prevent changing the currentPage in sync.
                    bMasterChanged = true;

                    invokeRepaintListeners = true; // ensure the event is published to controls

                    invokeMasterChangedDetailsListeners();
                    
                    if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                    {
                        eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                    }
                }
                catch (ModelException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    /**
     * Gets the UID of the given data row.
     * 
     * @param pDataRow the data row
     * @return the uid
     * @throws ModelException if it fails
     */
    private static Object getUID(IDataRow pDataRow) throws ModelException
    {
        if (pDataRow instanceof IChangeableDataRow)
        {
            return ((IChangeableDataRow)pDataRow).getUID();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDataPage(IDataRow pMasterDataRow, TreePath pTreePath) throws ModelException
    {
        synchronized (rootDataBook)
        {
            IDataRow  drMaster = null;
            MemDataPage dpCurrent = null;
            
            if (isSelfJoined())
            {
                if (pTreePath == null)
                {
                    pTreePath = TreePath.EMPTY;
                }
                IDataRow drRoot = null;
                if (rdTreeRootReference != null)
                {
                    drRoot = rdTreeRootReference.getReferencedDataBook();
                }
                
                if (hasDataPage(drRoot, pTreePath))
                {
                    dpCurrent = (MemDataPage)getDataPage(drRoot, pTreePath);
                    drMaster  = dpCurrent.getMasterDataRow();
                    
                    for (int i = 0, iSize = dpCurrent.getRowCountInternal(); i < iSize; i++)
                    {
                        removeDataPage(null, pTreePath.getChildPath(i));
                    }
                }
                else
                {
                    return;
                }
            }
            else
            {
                if (hasDataPage(pMasterDataRow))
                {
                    dpCurrent = (MemDataPage)getDataPage(pMasterDataRow);
                    drMaster = dpCurrent.getMasterDataRow();
                }
                else
                {
                    return;
                }
            }
            
            for (int i = 0, iSize = dpCurrent.getRowCountInternal(); i < iSize; i++)
            {
                IDataRow drCurrent = dpCurrent.getDataRow(i);
                
                if (auDetailDataBooks != null)
                {
                    for (IDataBook detailDataBook : getDetailDataBooks())
                    {
                        detailDataBook.removeDataPage(drCurrent, null);
                    }
                }
            }
                
            Object oUID = getUID(drMaster);
            if (oUID != null)
            {
                MemDataPage mdp = htDataPagesCache.remove(oUID);
                if (mdp.hasChanges())
                {
                    changeCounter--;
                }
            }
            else
            {
                MemDataPage mdp = htDataPagesCache.remove(drMaster);
                if (mdp.hasChanges())
                {
                    changeCounter--;
                }
            }
            
            // #495 - MemDataBook removeDataPage should setTreePath(null)
            if (isSelfJoined()
                    && treePath != null && treePath.length() > 0)
            {
                // if current TreePath is a detail from the deleted one, then set the treepath to null
                if (treePath.length() >= pTreePath.length())
                {
                    boolean bEquals = true;
                    for (int i = 0; bEquals && i < treePath.length() && i < pTreePath.length(); i++)
                    {
                        if (treePath.get(i) != pTreePath.get(i))
                        {
                            bEquals = false;
                        }
                    }
                    if (bEquals)
                    {
                        setTreePath(TreePath.EMPTY);
                    }
                }
            }
            
            if (dpCurrent == dpCurrentDataPage)
            {
                dpCurrentDataPage = null;
                
                // #369 - delete all rows from a databook
                // Master change has to be set, to sync the page!!!
                bMasterChanged = true;
                
                // Ensure controls are notified.
                notifyRepaintControls();

                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                {
                    eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public SelectionMode getSelectionMode()
    {
        return iSelectionMode;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelectionMode(SelectionMode pSelectionMode)
    {
        iSelectionMode = pSelectionMode;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isInsertAllowed()
    {
        synchronized (rootDataBook)
        {
            return bInsertEnabled && !isReadOnly() && dpCurrentDataPage != dpEmptyDataPage;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isInsertEnabled()
    {
        return bInsertEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setInsertEnabled(boolean pInsertEnabled)
    {
        synchronized (rootDataBook)
        {
            bInsertEnabled = pInsertEnabled;
            
            notifyRepaintControls();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isUpdateAllowed() throws ModelException
    {
        synchronized (rootDataBook)
        {
            return (bUpdateEnabled || (bInsertEnabled && isInserting())) && !isReadOnly() && getSelectedRow() >= 0 && !isDeleting();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isUpdateEnabled() throws ModelException
    {
        return bUpdateEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setUpdateEnabled(boolean pUpdateEnabled)
    {
        synchronized (rootDataBook)
        {
            bUpdateEnabled = pUpdateEnabled;
    
            notifyRepaintControls();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDeleteAllowed() throws ModelException
    {
        synchronized (rootDataBook)
        {
            return bDeleteEnabled && !isReadOnly() && getSelectedRow() >= (additionalDataRowVisible ? 1 : 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDeleteEnabled() throws ModelException
    {
        return bDeleteEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public void setDeleteEnabled(boolean pDeleteEnabled)
    {
        synchronized (rootDataBook)
        {
            bDeleteEnabled = pDeleteEnabled;
    
            notifyRepaintControls();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isReadOnly()
    {
        return bReadOnly;
    }

    /**
     * {@inheritDoc}
     */
    public void setReadOnly(boolean pReadOnly) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen && !bReadOnly && pReadOnly)
            {
                if (!isMemFilter() || getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
                {
                    // it saves always if not MemFilter (Data come from remote Storage, a filter would destroy all changes) or
                    // if the IsolationLevel DATAROW, save also always.
                    saveAllRows();
                }
                else
                {
                    // Editors has to save first to recognize changed Rows!
                    invokeTreeSaveEditingControls();
                }
            }
            
            setReadOnlyWithoutSave(pReadOnly);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isOpen()
    {
        return bIsOpen;
    }
    
    /**
     * {@inheritDoc}
     */
    public void open() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                if (getName() == null)
                {
                    throw new ModelException("Can not open the databook, there is no name set!!");
                }
    
                if (rdRowDefinition.getColumnCount() == 0)
                {
                    throw new ModelException(getName() + ": Can not open the databook, the RowDefinition does not contain any columns!");
                }
                
                // Performance improvement. The factory will not change for this data book.
                factory = UIFactoryManager.getFactory();
                //use default values for choice cell editors
                if (factory != null)
                {
                    ColumnDefinition coldef;
                    
                    IDataType datatype;
                    
                    for (int i = 0, anz = rdRowDefinition.getColumnCount(); i < anz; i++)
                    {
                        coldef = rdRowDefinition.getColumnDefinition(i);
                        Object[] allowedValues = coldef.getAllowedValues();
                        
                        datatype = coldef.getDataType();
                        
                        if (datatype != null
                            && datatype.getCellEditor() == null
                            && allowedValues != null && allowedValues.length > 0)
                        {
                            ICellEditor editor = null;
                            
                            if (coldef.isNullable())
                            {
                                // #998 - extend the array by one null value at the end to get the correct default editor
                                editor = UICellEditor.getDefaultCellEditor(ArrayUtil.add(allowedValues, null));
                            }
                            
                            if (editor == null)
                            {
                                editor = UICellEditor.getDefaultCellEditor(allowedValues);
                            }
                            
                            if (editor != null)
                            {
                                datatype.setDefaultCellEditor(editor);
                            }
                            else
                            {
                                datatype.setDefaultCellEditor(new UIEnumCellEditor(allowedValues));
                            }
                        }
                    }
                }
                
                // check PK Columns in RowDefinition
                String[] saPKColumnNames = rdRowDefinition.getPrimaryKeyColumnNames();
                for (int i = 0; saPKColumnNames != null && i < saPKColumnNames.length; i++)
                {
                    if (rdRowDefinition.getColumnDefinitionIndex(saPKColumnNames[i]) < 0)
                    {
                        throw new ModelException(getName() + ": Primary key column '" + saPKColumnNames[i] +
                                                 "' doesn't exist in RowDefinition of this DataBook!");
                    }
                }
    
                if (rdMasterReference != null)
                {
                    IDataBook dbMaster = rdMasterReference.getReferencedDataBook();
                    if (!isSelfJoined() && !dbMaster.isOpen())
                    {
                        throw new ModelException(getName() + ": The master DataBook has to be opened first! - " + dbMaster.getName());
                    }
                    // check if Master Reference Definition is correct.
                    String[] saColumns = rdMasterReference.getColumnNames();
                    for (int i = 0; i < saColumns.length; i++)
                    {
                        if (rdRowDefinition.getColumnDefinitionIndex(saColumns[i]) < 0)
                        {
                            throw new ModelException(getName() + ": Column '" + saColumns[i] +
                                                     "' doesn't exist in detail DataBook '" + getName() + "' !");
                        }
                    }
                    String[] saRefColumns = rdMasterReference.getReferencedColumnNames();
                    for (int i = 0; i < saRefColumns.length; i++)
                    {
                        if (dbMaster.getRowDefinition().getColumnDefinitionIndex(saRefColumns[i]) < 0)
                        {
                            throw new ModelException(getName() + ": Column '" + saRefColumns[i] +
                                                     "' doesn't exist in master DataBook '" +
                                                     dbMaster.getName() + "' !");
                        }
                    }
                    if (isSelfJoined())
                    {
                        if (getRootReference() != null)
                        {
                            if (getRootReference().getReferencedDataBook() == dbMaster)
                            {
                                throw new ModelException(getName() + ": The RootReference DataBook '"
                                        + getRootReference().getReferencedDataBook().getName()
                                        + "' must be != MasterReference DataBook '"
                                        + dbMaster.getName() + "' !");
                            }
                            
                            String[] columnNames = getRootReference().getColumnNames();
                            saTreeRootMasterColumnNames = new String[columnNames.length];
                            
                            for (int i = 0; i < columnNames.length; i++)
                            {
                                int index = ArrayUtil.indexOf(saColumns, columnNames[i]);
                                
                                if (index < 0)
                                {
                                    throw new ModelException(getName() + ": Column name " + columnNames[i] + " of root reference is not in master reference!");
                                }
                                saTreeRootMasterColumnNames[i] = saRefColumns[index];
                            }
                            
                            getRootReference().getReferencedDataBook().addDetailDataBook(this);
                            getRootReference().setConnected();
                        }
                        else
                        {
                            saTreeRootMasterColumnNames = null;
                        }
                    }
                    else
                    {
                        if (getRootReference() != null)
                        {
                            throw new ModelException(getName() + ": Only a self joined DataBook may have a RootReference!");
                        }
                        
                        rdMasterReference.getReferencedDataBook().addDetailDataBook(this);
                    }
                    rdMasterReference.setConnected();
                }
                else if (getRootReference() != null)
                {
                    throw new ModelException(getName() + ": Only a self joined DataBook may have a RootReference!");
                }
                
                if (getDataSource() != null)
                {
                    if (!getDataSource().isOpen())
                    {
                        throw new ModelException(getName() + ": DataSource is not open!");
                    }
                    getDataSource().addDataBook(this);
                }
                
                rdRowDefinition.addDataBook(this);
                
                if (additionalDataRowVisible)
                {
                    iSelectedRowIndex = -2;
                }
                else
                {
                    iSelectedRowIndex = -1;
                }

                rowInstance1 = new DataRow(rdRowDefinition);
                rowInstance2 = new DataRow(rdRowDefinition);
    
                rootDataBook = getRootDataBookIntern();
                
                bMasterChanged = true;
                bIsOpen = true;
    
                if (sSort != null && sSortSortableColumnsOnly == null)
                {
                    sSortSortableColumnsOnly = keepSortableColumns(sSort);
                }
                
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventOpened))
                {
                    eventOpened.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.OPENED));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close()
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                cancelEditingControls(); // Ensure, that editors do not fire events afterwards.
                
                IDataBook masterDataBook = null;
                boolean hasToNotifyMaster = false;
                if (rdMasterReference != null)
                {
                    if (!isSelfJoined())
                    {
                        masterDataBook = rdMasterReference.getReferencedDataBook();
                    }
                    else if (getRootReference() != null)
                    {
                        masterDataBook = getRootReference().getReferencedDataBook();
                    }
                    // only notify master databook that details are changed, if there are any changes.
                    // Otherwise, the master is forced to sync
                    if (masterDataBook != null && masterDataBook.isOpen() && hasChanges())
                    {
                        hasToNotifyMaster = true;
                    }
                }
                
                rootDataBook = this;
                
                bIsOpen                   = false;
                dpCurrentDataPage         = null;
                temporaryMasterDataPage   = null;
                bMasterChanged = false;
                htDataPagesCache.clear();
                changeCounter = 0;
                if (additionalDataRowVisible)
                {
                    iSelectedRowIndex = -2;
                }
                else
                {
                    iSelectedRowIndex = -1;
                }
                additionalDataRow   = null;
                additionalRowDefinition = null;
                oaStorage           = null;
    
                if (masterDataBook != null)
                {
                    masterDataBook.removeDetailDataBook(this);
                    if (hasToNotifyMaster)
                    {
                        masterDataBook.notifyDetailChanged();
                    }
                }
                
                if (dsDataSource != null)
                {
                    dsDataSource.removeDataBook(this);
                }
                if (rdRowDefinition != null)
                {
                    rdRowDefinition.removeDataBook(this);
                }
                rowInstance1 = null;
                rowInstance2 = null;

                sSortSortableColumnsOnly = null;
                
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventClosed))
                {
                    try
                    {
                        eventClosed.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.CLOSED));
                    }
                    catch (Throwable ex)
                    {
                        // will be ignored. Close may not throw any exception
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen)
            {
                sync();
    
                if (additionalDataRowVisible)
                {
                    return dpCurrentDataPage.getRowCount() + 1;
                }
                else
                {
                    return dpCurrentDataPage.getRowCount();
                }
            }
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectedRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (additionalDataRowVisible)
            {
                return getSelectedDataPageRow() + 1;
            }
            else
            {
                return getSelectedDataPageRow();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelectedRow(int pDataRowIndex) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for selecting a row.");
            }
    
            sync();
            
            int oldSelectedRow = getSelectedRow();
            boolean selectedRowChanged = oldSelectedRow != pDataRowIndex;
            boolean hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
            boolean hasAfterRowSelected = (selectedRowChanged || isAlwaysDispatchAfterRowSelectedEvent())
                            && DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            IDataRow drOriginalDataRow = hasBeforeRowSelected || hasAfterRowSelected ? createDataRow(null) : null;

            if (selectedRowChanged)
            {
                invokeTreeSaveEditingControls();
                
                // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
                // therefore an sync is needed!
                sync();
                
                
                if (hasBeforeRowSelected)
                {
                    eventBeforeRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_ROW_SELECTED, drOriginalDataRow,
                            treePath, treePath, oldSelectedRow, pDataRowIndex));
                    
                    hasAfterRowSelected = (selectedRowChanged || isAlwaysDispatchAfterRowSelectedEvent()) 
                            && DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected); 
                }
    
                if (getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
                {
                    saveDataRowLevelDetails(null);

                    // sync has to be after saveDataRowLevelDetails, because events could cause an empty dbCurrentDataPage
                    // therefore an sync is needed!
                    sync();
                }
    
                setSelectedRowInternal(pDataRowIndex);
                pDataRowIndex = getSelectedRow();
                
                if (hasAfterRowSelected)
                {
                    eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                            treePath, treePath, oldSelectedRow, pDataRowIndex));
                }
            }
            else if (isAlwaysDispatchAfterRowSelectedEvent() && hasAfterRowSelected)
            {
                eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, pDataRowIndex));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectedColumn() throws ModelException
    {
        // sync() not mandatory !
        return sSelectedColumn;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelectedColumn(String pColumnName) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for selecting a column.");
            }
    
            sync();
            
            if (pColumnName != null)
            {
                rdRowDefinition.getColumnDefinition(pColumnName); // Check if the column exists.
            }
            
            if ((sSelectedColumn == null && pColumnName != null)
                    || (sSelectedColumn != null && !sSelectedColumn.equals(pColumnName)))
            {
                boolean hasBeforeColumnSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeColumnSelected);
                boolean hasAfterColumnSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterColumnSelected);
                IDataRow drOriginalDataRow = hasBeforeColumnSelected || hasAfterColumnSelected ? createDataRow(null) : null;
                int selectedRow = getSelectedRow();
                String oldSelectedColumn = sSelectedColumn;
                
                if (hasBeforeColumnSelected)
                {
                    eventBeforeColumnSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_COLUMN_SELECTED, drOriginalDataRow,
                            treePath, selectedRow, oldSelectedColumn, pColumnName));
                    
                    hasAfterColumnSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterColumnSelected);
                }
                
                sSelectedColumn = pColumnName;
                
                notifyRepaintControls();
                
                if (hasAfterColumnSelected)
                {
                    eventAfterColumnSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_COLUMN_SELECTED, drOriginalDataRow,
                            treePath, selectedRow, oldSelectedColumn, pColumnName));
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDeleteCascade()
    {
        return bDeleteCascade;
    }

    /**
     * {@inheritDoc}
     */
    public void setDeleteCascade(boolean pDeleteCascade)
    {
        bDeleteCascade = pDeleteCascade;
    }

    /**
     * {@inheritDoc}
     */
    public int insert(boolean pBeforeRow) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for inserting a row.");
            }
            sync();
            if (dpCurrentDataPage == dpEmptyDataPage)
            {
                throw new ModelException(getName() + ": Master DataBook has no selected row! - " +
                                         rdMasterReference.getReferencedDataBook().getName());
            }
            if (!isInsertAllowed())
            {
                throw new ModelException(getName() + ": Insert isn't allowed!");
            }
            
            invokeTreeSaveEditingControls();
    
            saveDataRowLevel(null);
    
            // #374 - If an insert happens, after that more rows have to be fetched, then wrong rows will be fetched
            // saveDataRowLevel can set the dpCurrentDataPage to null, because notifyMasterChange rehash the details, and force lazy refetch!
            sync();
    
            // #374 - If an insert happens, after that more rows have to be fetched, then wrong rows will be fetched
            // if first insert without anything fetched, begin fetch, to prevent that a
            // cursor deliveres the new inserted row, what happens, if the fetch is started after inserting a row.
            if (!dpCurrentDataPage.isAllFetched() && dpCurrentDataPage.getRowCount() == 0)
            {
                getDataRow(0);
            }
            
            boolean hasBeforeInserting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeInserting);
            boolean hasAfterInserting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterInserting);
            boolean hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
            boolean hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            IDataRow drOriginalDataRow = hasBeforeInserting || hasAfterInserting || hasBeforeRowSelected || hasAfterRowSelected ? createDataRow(null) : null;
            int additionRow = additionalDataRowVisible ? 1 : 0;
            int oldSelectedRow = getSelectedRow();
            int newSelectedRow = oldSelectedRow;
            if (!pBeforeRow)
            {
                newSelectedRow++;
            }
            if (newSelectedRow < additionRow)
            {
                newSelectedRow = additionRow;
            }
            
            if (hasBeforeInserting)
            {
                eventBeforeInserting.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_INSERTING, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterInserting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterInserting);
                hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            
            additionRow = additionalDataRowVisible ? 1 : 0;
            oldSelectedRow = getSelectedRow();
            newSelectedRow = oldSelectedRow;
            if (!pBeforeRow)
            {
                newSelectedRow++;
            }
            if (newSelectedRow < additionRow)
            {
                newSelectedRow = additionRow;
            }
            
            if (hasBeforeRowSelected)
            {
                eventBeforeRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_ROW_SELECTED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterInserting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterInserting);
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            
            sync();
            if (!pBeforeRow)
            {
                iSelectedRowIndex++;
            }
            if (iSelectedRowIndex < 0)
            {
                iSelectedRowIndex = 0;
            }
            
            insertInternal();
            copyMasterColumnsToCurrentDetail(false);

            newSelectedRow = getSelectedRow(); // The additional datarow has to be included, if visible.

            if (hasAfterInserting)
            {
                eventAfterInserting.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_INSERTING, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            if (hasAfterRowSelected)
            {
                eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));
            }
            
            return newSelectedRow;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!isChanged())
            {
                if (!bIsOpen)
                {
                    throw new ModelException(getName() + ": The databook must be open for updating a row.");
                }
                sync();
                if (additionalDataRowVisible && getSelectedRow() == 0)
                {
                    return;
                }
                if (!isUpdateAllowed())
                {
                    throw new ModelException(getName() + ": Update isn't allowed!");
                }
                
                boolean hasBeforeUpdating = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeUpdating);
                boolean hasAfterUpdating = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterUpdating);
                IDataRow drOriginalDataRow = hasBeforeUpdating || hasAfterUpdating ? createDataRow(null) : null;

                saveDataRowLevel(this);

                // Either saveDataRowLevel, or before updating event can trigger update of this data book again.
                // It has to be prevented, that update is called twice, as lock will happen twice, and the old row will be cloned again with an already updated state.
                if (!isChanged())
                {
                    if (hasBeforeUpdating)
                    {
                        int selectedRow = getSelectedRow();
                        eventBeforeUpdating.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_UPDATING, drOriginalDataRow,
                                treePath, treePath, selectedRow, selectedRow));
                        
                        hasAfterUpdating = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterUpdating);
                    }
                    
                    // Either saveDataRowLevel, or before updating event can trigger update of this data book again.
                    // It has to be prevented, that update is called twice, as lock will happen twice, and the old row will be cloned again with an already updated state.
                    if (!isChanged())
                    {
                        if (isWritebackEnabled() && isLockAndRefetchEnabled())
                        {
                            lockAndRefetchIntern();
                        }
                        
                        // set current row in DataPage updating, and set the new updating row to the current row.
                        // Thats enough! setUpdating, setValueInternal removed from source! HM will love it
                        setUpdating();
                        changeCounter += dpCurrentDataPage.setDataRow(iSelectedRowIndex, this);
            
                        calculateRow();
        
                        setDetailsChangedInMasterBook();
            
                        if (hasAfterUpdating)
                        {
                            int selectedRow = getSelectedRow();
                            eventAfterUpdating.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_UPDATING, drOriginalDataRow,
                                    treePath, treePath, selectedRow, selectedRow));
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for deleting a row.");
            }
            sync();
            if (iSelectedRowIndex < 0)
            {
                return;
            }
            if (!isDeleteAllowed())
            {
                throw new ModelException(getName() + ": Delete isn't allowed!");
            }
            
            if (!isDeleting())
            {
                boolean hasBeforeDeleting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeDeleting);
                boolean hasAfterDeleting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterDeleting);
                IDataRow drOriginalDataRow = hasBeforeDeleting || hasAfterDeleting ? createDataRow(null) : null;
                int selectedRow = getSelectedRow();

                if (hasBeforeDeleting)
                {
                    eventBeforeDeleting.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_DELETING, drOriginalDataRow,
                            treePath, treePath, selectedRow, selectedRow));
                    
                    hasAfterDeleting = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterDeleting);
                }

                if (isInserting())
                {
                    // #215 - Wrong event order in delete on an inserting row
                    // AFTER_DELETING event is thrown before BEFORE_RESTORE, to get the old Row!
                    if (hasAfterDeleting)
                    {
                        eventAfterDeleting.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_DELETING, drOriginalDataRow,
                                treePath, treePath, selectedRow, selectedRow));
                    }
                    
                    restoreSelectedRow();
                }
                else
                {
                    if (isChanged())
                    {
                        restoreSelectedRowIntern();
                    }

                    // if this is an exiting row, then try to lock it first, that no other can modify it.
                    if (isWritebackEnabled() && isLockAndRefetchEnabled())
                    {
                        lockAndRefetchIntern();
                    }

                    deleteInternal();

                    if (hasAfterDeleting)
                    {
                        eventAfterDeleting.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_DELETING, drOriginalDataRow,
                                treePath, treePath, selectedRow, selectedRow));
                    }
                
                    // correct selected row
                    // #505 - delete should should check the WritebackIsolation level
                    if (WriteBackIsolationLevel.DATA_ROW.equals(getWritebackIsolationLevel()))
                    {
                        // store depended DataBooks, if the WritebackIsolationLevel == IDataSource.DATA_ROW
                        try
                        {
                            saveSelectedRow();
                        }
                        catch (ModelException modelException)
                        {
                            if (isDeleting())
                            {
                                restoreSelectedRowIntern();
                            }
                            throw modelException;
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public void deleteAllDataRows() throws ModelException
    {
        deleteAllRows();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAllRows() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for deleting all rows.");
            }
            sync();
    
            fetchAll();
            for (int i = getRowCount() - 1; i >= 0; i--)
            {
                setSelectedRow(i);
                delete();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void notifyDetailChanged()
    {
        synchronized (rootDataBook)
        {
            try
            {
                // set the Detail changed only, if the row still exists. -> Ticket #330
                if (iSelectedRowIndex >= 0 && iSelectedRowIndex < dpCurrentDataPage.getRowCount())
                {
                    // check if one detail is changed.
                    boolean bDetailChanged = false;
                    for (int i = auDetailDataBooks.size() - 1; !bDetailChanged && i >= 0; i--)
                    {
                        IDataBook detail = auDetailDataBooks.get(i).get();
                        if (detail != null)
                        {
                            IDataPage dataPage;
                            if (detail.isSelfJoined())
                            {
                                dataPage = detail.getDataPage(TreePath.EMPTY);
                            }
                            else
                            {
                                dataPage = detail;
                            }
                            int[] iChanges = dataPage.getChangedRows();
    
                            if (iChanges.length > 0)
                            {
                                bDetailChanged = true;
                            }
                        }
                    }
    
                    if (bDetailChanged != isDetailChanged())
                    {
                        setDetailChanged(bDetailChanged);
                        changeCounter += dpCurrentDataPage.setDataRow(iSelectedRowIndex, this);
                    }
                }
    
                setDetailsChangedInMasterBook();
            }
            catch (ModelException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public IChangeableDataRow getDataRow(int pDataRowIndex) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for getting a row.");
            }
            if (pDataRowIndex < 0)
            {
                return null;
            }
            sync();
            
            if (additionalDataRowVisible)
            {
                if (pDataRowIndex == 0)
                {
                    return getAdditionalDataRow();
                }
                else
                {
                    return getDataRowInternal(pDataRowIndex - 1);
                }
            }
            else
            {
                return getDataRowInternal(pDataRowIndex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ChangeableDataRow getAdditionalDataRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for getting the additional row.");
            }
            
            if (additionalDataRow == null)
            {
                additionalRowDefinition = rdRowDefinition.createRowDefinition(rdRowDefinition.getColumnNames());
    
                Class<? extends IControl>[] columnViewClasses = rdRowDefinition.getColumnViewClasses();
                
                additionalRowDefinition.setPrimaryKeyColumnNames(rdRowDefinition.getPrimaryKeyColumnNames());
                for (int i = 0; i < columnViewClasses.length; i++)
                {
                    additionalRowDefinition.setColumnView(columnViewClasses[i], rdRowDefinition.getColumnView(columnViewClasses[i]));
                }
                
                additionalRowDefinition.setColumnView(null, rdRowDefinition.getColumnView(null));
                
                IDataBook[] dataBooks = rdRowDefinition.getDataBooks();
                for (int i = 0; i < dataBooks.length; i++)
                {
                    additionalRowDefinition.addDataBook(dataBooks[i]);
                }
                
                additionalDataRow = new ChangeableDataRow(additionalRowDefinition, new Object[rdRowDefinition.getColumnCount()], null, -1);
            }
            return additionalDataRow;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAdditionalDataRowVisible()
    {
        return additionalDataRowVisible;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setAdditionalDataRowVisible(boolean pVisible)
    {
        synchronized (rootDataBook)
        {
            if (pVisible != additionalDataRowVisible)
            {
                additionalDataRowVisible = pVisible;
                
                if (pVisible)
                {
                    if (iSelectedRowIndex == -1)
                    {
                        iSelectedRowIndex = -2;
                    }
                }
                else
                {
                    if (iSelectedRowIndex < -1)
                    {
                        iSelectedRowIndex = -1;
                    }
                }
                
                notifyRepaintControls();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getSelectedDataPageRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return -1;
            }
            sync();
            
            return iSelectedRowIndex;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelectedDataPageRow(int pDataRowIndex) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (additionalDataRowVisible)
            {
                setSelectedRow(pDataRowIndex + 1);
            }
            else
            {
                setSelectedRow(pDataRowIndex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDataRow getOriginalDataRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            return super.getOriginalDataRow();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWritableColumnChanged() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return false;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return false;
            }
            return super.isWritableColumnChanged();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeleting() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return false;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return false;
            }
            return super.isDeleting();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdating() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return false;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return false;
            }
            return super.isUpdating();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInserting() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return false;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return false;
            }
            return super.isInserting();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDetailChanged() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return false;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return false;
            }
            return super.isDetailChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUID() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            // getSelectedRow does sync();
            if (getSelectedDataPageRow() < 0)
            {
                return null;
            }
            return super.getUID();
        }
    }

    /**
     * {@inheritDoc}
     */
    public ICondition getFilter()
    {
        return cFilter;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setFilter(ICondition pFilter) throws ModelException
    {
        synchronized (rootDataBook)
        {
            ICondition oldFilter = cFilter;
            ICondition newFilter = pFilter == null ? null : pFilter.clone();
            TreePath oldTreePath = treePath;
            
            if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeFilterChanged))
            {
                eventBeforeFilterChanged.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_FILTER_CHANGED, 
                        oldFilter, newFilter, oldTreePath));
            }
    
            if (bIsOpen && !ignoreReload)
            {
                //#615
                //memfilter means: all rows fetched and if datasource level is set, don't save
                //on filter change.
                //If you have a pseudo master-link view (no master reference between databooks, but master sets filter to
                //filter details). In this case, don't save on "pseudo master" change.
                
                if (!isMemFilter() || getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
                {
                    // it saves always if not MemFilter (Data come from remote Storage, a filter would destroy all changes) or
                    // if the IsolationLevel DATAROW, save also always.
                    saveAllRows();
                }
                else
                {
                    // Editors has to save first to recognize changed Rows!
                    invokeTreeSaveEditingControls();
                }
    
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeReload))
                {
                    eventBeforeReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_RELOAD));
                }
                
                // handle special set filter selection modes.
                if (iSelectionMode == SelectionMode.CURRENT_ROW_SETFILTER || iSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
                {
                    handleStoreSelection(iSelectionMode);
                }
                else if (iSelectionMode == SelectionMode.DESELECTED || iSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED)
                {
                    handleStoreSelection(SelectionMode.DESELECTED);
                }
                else
                {
                    handleStoreSelection(SelectionMode.FIRST_ROW);
                }
                
                if (!isMemFilter())
                {
                    restoreAllRows();
                }
            }
            
            // copy Filter, to preserve, User changes during filtering.
            cFilter = newFilter;
    
            if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterFilterChanged))
            {
                eventAfterFilterChanged.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_FILTER_CHANGED, 
                        oldFilter, newFilter, oldTreePath));
            }
    
            if (bIsOpen && !ignoreReload)
            {
                if (isMemFilter())
                {
                    clearFilterSortInMemDataPages();
                    
                    notifyRepaintControls();
                    
                    if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                    {
                        eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                    }
                }
                else
                {
                    reloadIntern();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public SortDefinition getSort()
    {
        if (isMemSort() || !bIsOpen)
        {
            return sSort;
        }
        else
        {
            return sSortSortableColumnsOnly;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSort(SortDefinition pSort) throws ModelException
    {
        synchronized (rootDataBook)
        {
            SortDefinition oldSort = sSort;
            SortDefinition newSort = pSort == null ? null : pSort.clone();
            TreePath oldTreePath = treePath;
            
            if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeSortChanged))
            {
                eventBeforeSortChanged.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_SORT_CHANGED, 
                        oldSort, newSort, oldTreePath));
            }
    
            if (bIsOpen && !ignoreReload)
            {
                if (!isMemSort() || getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
                {
                    // it saves always if not MemFilter (Data come from remote Storage, a filter would destroy all changes) or
                    // if the IsolationLevel DATAROW, save also always.
                    saveAllRows();
                }
                else
                {
                    // Editors has to save first to recognize changed Rows!
                    invokeTreeSaveEditingControls();
                }
    
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeReload))
                {
                    eventBeforeReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_RELOAD));
                }
                
                // Ensure that sort is set after clearing all.
                handleStoreSelection(iSelectionMode);
        
                if (!isMemSort())
                {
                    restoreAllRows();
                }
            }
    
            // copy Filter, to preserve, User changes during filtering.
            if (pSort != null)
            {
                sSort = pSort.clone();
                sSortSortableColumnsOnly = keepSortableColumns(sSort);
            }
            else
            {
                sSort = null;
                sSortSortableColumnsOnly = null;
            }
            
            if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterSortChanged))
            {
                eventAfterSortChanged.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_SORT_CHANGED, 
                        oldSort, newSort, oldTreePath));
            }
    
            if (bIsOpen && !ignoreReload)
            {
                if (isMemSort())
                {
                    clearFilterSortInMemDataPages();
                    
                    notifyRepaintControls();
                    
                    if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                    {
                        eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                    }
                }
                else
                {
                    reloadIntern();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reload() throws ModelException
    {
        reload(getSelectionMode());
    }
    
    /**
     * {@inheritDoc}
     */
    public void reload(SelectionMode pSelectionMode) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen && !ignoreReload)
            {
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeReload))
                {
                    eventBeforeReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_RELOAD));
                }

                handleStoreSelection(pSelectionMode);
                
                restoreAllRows();
                
                reloadIntern();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void restoreSelectedRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for restoring a row.");
            }
            sync();
            if (iSelectedRowIndex < 0)
            {
                return;
            }
            
            invokeTreeCancelEditingControls();
    
            if (isChanged() || isDetailChanged())
            {
                // store current TreePaths in Detail DataBooks.
                ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();
            
                restoreSelectedRowInternIncludeDetails();
                
                // restore Detail DataBook TreePaths
                setAllDetailsTreePath(auOldTreePath);
            }

            notifyRepaintControls();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void restoreAllRows() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for restoring all rows.");
            }
            
            if (!hasChanges())
            {
                return;
            }
            
            sync();
    
            invokeTreeCancelEditingControls();
    
            // #492 - MemDataBook.restoreAllRows set wrong SelectedRow :: save the current row after the restore e.g is Inserting....
            int iOldSelectedRow = getSelectedRow();
            ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();

            if (getSelectedRow() >= 0 && (isChanged() || isDetailChanged()))
            {
                restoreSelectedRowInternIncludeDetails();
            }
    
            if (hasChanges())
            {
                ICondition oldFilter = null;
                try
                {
                    // setFilter null, to see all changed rows!
                    if (isMemFilter() && getWritebackIsolationLevel() != WriteBackIsolationLevel.DATA_ROW
                            && getFilter() != null)
                    {
                        oldFilter = getFilter();
                        cFilter = null;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                        clearFilterSortInMemDataPages();
                        sync();
                    }
                    // Restore all rows in current DataPage, then in all others.
                    restoreDataPageIntern();
                    
                    // then restore all changes in all loaded DataPages
                    if (rdMasterReference != null && hasChanges())
                    {
                        IDataBook           dbRoot = this;
                        ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
                        while (dbParentReference != null)
                        {
                            dbRoot            = dbParentReference.getReferencedDataBook();
                            dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
                        }
                        
                        TreePath tpOld = null;
                        if (dbRoot.isSelfJoined())
                        {
                            // start at the root node of the hierarchy
                            tpOld = dbRoot.getTreePath();
                            dbRoot.setTreePath(null);
                        }
                        
                        restoreTopDownAllRows(dbRoot, this);
                        
                        if (dbRoot.isSelfJoined() && tpOld != null)
                        {
                            // restore Root TreePath
                            dbRoot.setTreePath(tpOld);
                        }
                    }
                }
                finally
                {
                    if (oldFilter != null)
                    {
                        cFilter = oldFilter;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                        clearFilterSortInMemDataPages();
                    }
                }
                
                if (getSelectedRow() != iOldSelectedRow)
                {
                    setSelectedRow(iOldSelectedRow);
                }
                // restore selection and TreePath
                if (iOldSelectedRow >= getRowCount())
                {
                    iSelectedRowIndex = iOldSelectedRow;
                    correctSelectedRowAfterDelete();
                }
                else
                {
                    if (getSelectedRow() != iOldSelectedRow)
                    {
                        setSelectedRow(iOldSelectedRow);
                    }
                    setAllDetailsTreePath(auOldTreePath);
                }
            }
            else
            {
                setAllDetailsTreePath(auOldTreePath);
            }
            
            notifyRepaintControls();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveSelectedRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for saving a row.");
            }
            if (getSelectedRow() < 0)
            {
                return;
            }
    
            invokeTreeSaveEditingControls();
            
            // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
            // therefore an sync is needed!
            sync();
            
            if (isChanged() || isDetailChanged())
            {
                // store current TreePaths in Detail DataBooks.
                ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();
            
                saveSelectedRowInternIncludeDetails();
                
                // restore Detail DataBook TreePaths
                setAllDetailsTreePath(auOldTreePath);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveDataPage() throws ModelException
    {
        synchronized (rootDataBook)
        {
            sync();
            
            // store Selection & TreePath
            int iOldSelectedRow = iSelectedRowIndex;
            ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();
            
            saveDataPageIntern();
            
            // restore selection and TreePath
            if (iOldSelectedRow >= getRowCount())
            {
                iSelectedRowIndex = iOldSelectedRow;
                correctSelectedRowAfterDelete();
            }
            else
            {
                if (iSelectedRowIndex != iOldSelectedRow)
                {
                    setSelectedRow(iOldSelectedRow);
                }
                setAllDetailsTreePath(auOldTreePath);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveAllRows() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for saving all rows.");
            }
    
            // Editors has to save first to recognize changed Rows!
            invokeTreeSaveEditingControls();
    
            if (!hasChanges())
            {
                //no pages, no changes return
                return;
            }
            
            // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
            // therefore an sync is needed!
            sync();
            
            // store current TreePaths in Detail DataBooks.
            ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();

            if (isChanged() || isDetailChanged())
            {
                saveSelectedRowInternIncludeDetails();
            }

            if (hasChanges())
            {
                iOldSelectedRowSaveAllRows = iSelectedRowIndex;
                dpOldCurrentDataPageSaveAllRows = dpCurrentDataPage;
                
                ICondition oldFilter = null;
                
                try
                {
                    // setFilter null, to see all changed rows!
                    if (isMemFilter() && getWritebackIsolationLevel() != WriteBackIsolationLevel.DATA_ROW
                            && getFilter() != null)
                    {
                        oldFilter = getFilter();
                        cFilter = null;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                        clearFilterSortInMemDataPages();
                        sync();
                    }
                    
                    // Save all rows in current DataPage, then in all others.
                    saveDataPageIntern();
                        
                    // save all changes in the Selfjoined hierarchy
                    if (isSelfJoined())
                    {
                        // select Root Node and go through all loaded detail Pages.
                        TreePath tpOld = getTreePath();
                        
                        // if I am not the Root Node, then select it
                        if (tpOld != null && tpOld.length() > 0)
                        {
                            setTreePathInternal(null);
                        }
                        
                        for (int i = 0, iRowCount = getRowCount(); i < iRowCount; i++)
                        {
                            setSelectedRow(i);
                            saveSelfJoinedDetailRows();
                        }
            
                        // if I am not the Root Node, then restore TreePath
                        if (tpOld != null && tpOld.length() > 0)
                        {
                            //select back to the current row.
                            setTreePathInternal(tpOld);
                        }
                    }
                    
                    // then save changes in all loaded DataPages
                    if (rdMasterReference != null && hasChanges())
                    {
                        IDataBook           dbRoot = this;
                        ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
                        while (dbParentReference != null)
                        {
                            dbRoot            = dbParentReference.getReferencedDataBook();
                            dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
                        }
                        
                        TreePath tpOld = null;
                        if (dbRoot.isSelfJoined())
                        {
                            // start at the root node of the hierarchy
                            tpOld = dbRoot.getTreePath();
                            dbRoot.setTreePath(null);
                        }
                        
                        saveTopDownAllRows(dbRoot, this);
                        
                        if (dbRoot.isSelfJoined() && tpOld != null)
                        {
                            // restore Root TreePath
                            dbRoot.setTreePath(tpOld);
                        }
                    }
                }
                finally
                {
                    if (oldFilter != null)
                    {
                        cFilter = oldFilter;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                        clearFilterSortInMemDataPages();
                    }
                }
        
                // restore selection and TreePath
                sync(); // ensure dpCurrentDataPage is correct. 
                if (iOldSelectedRowSaveAllRows >= dpCurrentDataPage.getRowCount())
                {
                    iSelectedRowIndex = iOldSelectedRowSaveAllRows;
                    correctSelectedRowAfterDelete();
                }
                else
                {
                    if (iSelectedRowIndex != iOldSelectedRowSaveAllRows)
                    {
                        setSelectedDataPageRow(iOldSelectedRowSaveAllRows);
                    }
                    setAllDetailsTreePath(auOldTreePath);
                }
            }
            else
            {
                setAllDetailsTreePath(auOldTreePath);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChanges()
    {
        synchronized (rootDataBook)
        {
            return changeCounter > 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isOutOfSync()
    {
        return dpCurrentDataPage == null || (rdMasterReference != null && bMasterChanged);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isAllFetched() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for checking if all rows have been fetched.");
            }
            sync();
            return dpCurrentDataPage.isAllFetched();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void fetchAll() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for fetching all rows.");
            }
            
            try
            {
                if (isOutOfSync())
                {
                    ignoreCorrectSelectedRow = true; // sync would force select with readAhead, before fetchAll is called afterwards.
                }
                sync();
                if (dpCurrentDataPage == dpEmptyDataPage)
                {
                    return;
                }
                dpCurrentDataPage.fetchAll();
            }
            finally
            {
                if (ignoreCorrectSelectedRow)
                {                    
                    ignoreCorrectSelectedRow = false;

                    correctSelectedRow(); // correctSelectedRow afterwards...
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public IDataRow getMasterDataRow() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            return dpCurrentDataPage.getMasterDataRow();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addDetailDataBook(IDataBook pDetailDataBook)
    {
        synchronized (rootDataBook)
        {
            HashSet<String> masterColumns = new HashSet<String>();
            if (auDetailDataBooks == null)
            {
                auDetailDataBooks = new ArrayUtil<WeakReference<IDataBook>>();
            }
            else
            {
                for (int i = auDetailDataBooks.size() - 1; i >= 0; i--)
                {
                    IDataBook dataBook = auDetailDataBooks.get(i).get();
                    if (dataBook == null)
                    {
                        auDetailDataBooks.remove(i);
                    }
                    else if (dataBook.getMasterReference() != null && dataBook.getMasterReference().getReferencedColumnNames() != null)
                    {
                        for (String col : dataBook.getMasterReference().getReferencedColumnNames())
                        {
                            masterColumns.add(col);
                        }
                    }
                }
            }
    
            auDetailDataBooks.add(new WeakReference(pDetailDataBook));
            
            if (pDetailDataBook.getMasterReference() != null && pDetailDataBook.getMasterReference().getReferencedColumnNames() != null)
            {
                for (String col : pDetailDataBook.getMasterReference().getReferencedColumnNames())
                {
                    masterColumns.add(col);
                }
            }
            int size = masterColumns.size();
            saMasterColumns = size == 0 ? null : masterColumns.toArray(new String[size]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeDetailDataBook(IDataBook pDetailDataBook)
    {
        synchronized (rootDataBook)
        {
            if (auDetailDataBooks != null)
            {
                HashSet<String> masterColumns = new HashSet<String>();

                for (int i = auDetailDataBooks.size() - 1; i >= 0; i--)
                {
                    IDataBook dataBook = auDetailDataBooks.get(i).get();
                    if (dataBook == null || dataBook == pDetailDataBook)
                    {
                        auDetailDataBooks.remove(i);
                    }
                    else if (dataBook.getMasterReference() != null && dataBook.getMasterReference().getReferencedColumnNames() != null)
                    {
                        for (String col : dataBook.getMasterReference().getReferencedColumnNames())
                        {
                            masterColumns.add(col);
                        }
                    }
                }
                int size = masterColumns.size();
                saMasterColumns = size == 0 ? null : masterColumns.toArray(new String[size]);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataBook[] getDetailDataBooks()
    {
        synchronized (rootDataBook)
        {
            ArrayUtil<IDataBook> result = new ArrayUtil<IDataBook>();
            if (auDetailDataBooks != null)
            {
                for (int i = auDetailDataBooks.size() - 1; i >= 0; i--)
                {
                    IDataBook dataBook = auDetailDataBooks.get(i).get();
                    if (dataBook == null)
                    {
                        auDetailDataBooks.remove(i);
                    }
                    else
                    {
                        result.add(0, dataBook);
                    }
                }
            }
            return result.toArray(new IDataBook[result.size()]);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Deprecated
    public int[] getChangedDataRows() throws ModelException
    {
        return getChangedRows();
    }
    
    /**
     * {@inheritDoc}
     */
    public int[] getChangedRows() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for getting the changed rows.");
            }
            sync();
            
            return dpCurrentDataPage.getChangedRows();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataPage getDataPage(IDataRow pMasterDataRow) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (rdMasterReference == null)
            {
                sync();
                return dpCurrentDataPage;
            }
            else
            {
                return getDataPageIntern(pMasterDataRow);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasDataPage(IDataRow pMasterDataRow) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (rdMasterReference == null)
            {
                if (pMasterDataRow == null)
                {
                    return true;
                }
                return false;
            }
            else
            {
                IDataRow drMaster = getMasterDataRow(pMasterDataRow);
                MemDataPage dpNewCurrentDataPage = htDataPagesCache.get(drMaster);
    
                if (dpNewCurrentDataPage == null)
                {
                    Object oUID = getUID(pMasterDataRow);
                    if (oUID != null)
                    {
                        dpNewCurrentDataPage = htDataPagesCache.get(oUID);
                    }
                }
            
                return dpNewCurrentDataPage != null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int searchNext(ICondition pCondition) throws ModelException
    {
        synchronized (rootDataBook)
        {
            sync();
            
            int rowNum = dpCurrentDataPage.searchNext(pCondition);
            // #1226, we need to be aware that the index might be incorrect
            // because of the additional data row.
            if (additionalDataRowVisible && rowNum >= 0)
            {
                return rowNum + 1;
            }
            else
            {
                return rowNum;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int searchNext(ICondition pCondition, int pRowNum) throws ModelException
    {
        synchronized (rootDataBook)
        {
            sync();
            
            int rowNum = dpCurrentDataPage.searchNext(pCondition, pRowNum);
            // #1226, we need to be aware that the index might be incorrect
            // because of the additional data row.
            if (additionalDataRowVisible && rowNum >= 0)
            {
                return rowNum + 1;
            }
            else
            {
                return rowNum;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int searchPrevious(ICondition pCondition) throws ModelException
    {
        synchronized (rootDataBook)
        {
            sync();
            
            int rowNum = dpCurrentDataPage.searchPrevious(pCondition);
            // #1226, we need to be aware that the index might be incorrect
            // because of the additional data row.
            if (additionalDataRowVisible && rowNum >= 0)
            {
                return rowNum + 1;
            }
            else
            {
                return rowNum;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int searchPrevious(ICondition pCondition, int pRowNum) throws ModelException
    {
        synchronized (rootDataBook)
        {
            sync();
            
            int rowNum = dpCurrentDataPage.searchPrevious(pCondition, pRowNum);
            // #1226, we need to be aware that the index might be incorrect
            // because of the additional data row.
            if (additionalDataRowVisible && rowNum >= 0)
            {
                return rowNum + 1;
            }
            else
            {
                return rowNum;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<IChangeableDataRow> iterator()
    {
        synchronized (rootDataBook)
        {
            try
            {
                sync();
            }
            catch (ModelException ex)
            {
                throw new RuntimeException(ex);
            }

            return dpCurrentDataPage.iterator();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IRowCalculator getRowCalculator()
    {
        return rowCalculator;
    }

    /**
     * {@inheritDoc}
     */
    public void setRowCalculator(IRowCalculator pRowCalculator)
    {
        rowCalculator = pRowCalculator;
    }

    /**
     * Sets the row calculator.
     *
     * @param pRowCalculatorObject the object.
     * @param pMethodName the method name.
     */
    public void setRowCalculator(Object pRowCalculatorObject, String pMethodName)
    {
        setRowCalculator(createRowCalculator(pRowCalculatorObject, pMethodName));
    }
    
    /**
     * Creates a row calculator instance with the given object and method name.
     * @param pRowCalculatorObject the object.
     * @param pMethodName the method name.
     * @return the row calculator
     */
    public static IRowCalculator createRowCalculator(Object pRowCalculatorObject, String pMethodName)
    {
        return rowCalculatorProvider.createListener(pRowCalculatorObject, pMethodName);
    }
    
    /**
     * Calculates the given row.
     */
    protected void calculateRow()
    {
        if (rowCalculator != null)
        {
            try 
            {
                if (currentCalculatedDataRow == null)
                {
                    currentCalculatedDataRow = new ChangeableDataRow(rdRowDefinition,
                             oaStorage,
                             dpCurrentDataPage,
                             iSelectedRowIndex);
                }
                else
                {
                    currentCalculatedDataRow.rdRowDefinition = rdRowDefinition;
                    currentCalculatedDataRow.oaStorage = oaStorage;
                    currentCalculatedDataRow.dpDataPage = dpCurrentDataPage;
                    currentCalculatedDataRow.iRowIndex = iSelectedRowIndex;
                }
                rowCalculator.calculateRow(this, dpCurrentDataPage, currentCalculatedDataRow);
            } 
            catch (Throwable e) 
            {
                // Row calculation is silent
            }
        }
    }

    
    /**
     * {@inheritDoc}
     */
    public IReadOnlyChecker getReadOnlyChecker()
    {
        return readOnlyChecker;
    }

    /**
     * {@inheritDoc}
     */
    public void setReadOnlyChecker(IReadOnlyChecker pReadOnlyChecker)
    {
        readOnlyChecker = pReadOnlyChecker;
    }

    /**
     * Sets the read only checker.
     *
     * @param pReadOnlyCheckerObject the object.
     * @param pMethodName the method name.
     */
    public void setReadOnlyChecker(Object pReadOnlyCheckerObject, String pMethodName)
    {
        setReadOnlyChecker(createReadOnlyChecker(pReadOnlyCheckerObject, pMethodName));
    }
    
    /**
     * Creates a read only checkerr instance with the given object and method name.
     * @param pReadOnlyCheckerObject the object.
     * @param pMethodName the method name.
     * @return the row calculator
     */
    public static IReadOnlyChecker createReadOnlyChecker(Object pReadOnlyCheckerObject, String pMethodName)
    {
        return readOnlyCheckerProvider.createListener(pReadOnlyCheckerObject, pMethodName);
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeRowSelected()
    {
        if (eventBeforeRowSelected == null)
        {
            eventBeforeRowSelected = new DataBookHandler();
        }
        return eventBeforeRowSelected;
    }

    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterRowSelected()
    {
        if (eventAfterRowSelected == null)
        {
            eventAfterRowSelected = new DataBookHandler();
        }
        return eventAfterRowSelected;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeInserting()
    {
        if (eventBeforeInserting == null)
        {
            eventBeforeInserting = new DataBookHandler();
        }
        return eventBeforeInserting;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterInserting()
    {
        if (eventAfterInserting == null)
        {
            eventAfterInserting = new DataBookHandler();
        }
        return eventAfterInserting;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeInserted()
    {
        if (eventBeforeInserted == null)
        {
            eventBeforeInserted = new DataBookHandler();
        }
        return eventBeforeInserted;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterInserted()
    {
        if (eventAfterInserted == null)
        {
            eventAfterInserted = new DataBookHandler();
        }
        return eventAfterInserted;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeUpdating()
    {
        if (eventBeforeUpdating == null)
        {
            eventBeforeUpdating = new DataBookHandler();
        }
        return eventBeforeUpdating;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterUpdating()
    {
        if (eventAfterUpdating == null)
        {
            eventAfterUpdating = new DataBookHandler();
        }
        return eventAfterUpdating;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeUpdated()
    {
        if (eventBeforeUpdated == null)
        {
            eventBeforeUpdated = new DataBookHandler();
        }
        return eventBeforeUpdated;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterUpdated()
    {
        if (eventAfterUpdated == null)
        {
            eventAfterUpdated = new DataBookHandler();
        }
        return eventAfterUpdated;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeDeleting()
    {
        if (eventBeforeDeleting == null)
        {
            eventBeforeDeleting = new DataBookHandler();
        }
        return eventBeforeDeleting;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterDeleting()
    {
        if (eventAfterDeleting == null)
        {
            eventAfterDeleting = new DataBookHandler();
        }
        return eventAfterDeleting;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeDeleted()
    {
        if (eventBeforeDeleted == null)
        {
            eventBeforeDeleted = new DataBookHandler();
        }
        return eventBeforeDeleted;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterDeleted()
    {
        if (eventAfterDeleted == null)
        {
            eventAfterDeleted = new DataBookHandler();
        }
        return eventAfterDeleted;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeRestore()
    {
        if (eventBeforeRestore == null)
        {
            eventBeforeRestore = new DataBookHandler();
        }
        return eventBeforeRestore;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterRestore()
    {
        if (eventAfterRestore == null)
        {
            eventAfterRestore = new DataBookHandler();
        }
        return eventAfterRestore;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeReload()
    {
        if (eventBeforeReload == null)
        {
            eventBeforeReload = new DataBookHandler();
        }
        return eventBeforeReload;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterReload()
    {
        if (eventAfterReload == null)
        {
            eventAfterReload = new DataBookHandler();
        }
        return eventAfterReload;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeFilterChanged()
    {
        if (eventBeforeFilterChanged == null)
        {
            eventBeforeFilterChanged = new DataBookHandler();
        }
        return eventBeforeFilterChanged;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterFilterChanged()
    {
        if (eventAfterFilterChanged == null)
        {
            eventAfterFilterChanged = new DataBookHandler();
        }
        return eventAfterFilterChanged;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeSortChanged()
    {
        if (eventBeforeSortChanged == null)
        {
            eventBeforeSortChanged = new DataBookHandler();
        }
        return eventBeforeSortChanged;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterSortChanged()
    {
        if (eventAfterSortChanged == null)
        {
            eventAfterSortChanged = new DataBookHandler();
        }
        return eventAfterSortChanged;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventBeforeColumnSelected()
    {
        if (eventBeforeColumnSelected == null)
        {
            eventBeforeColumnSelected = new DataBookHandler();
        }
        return eventBeforeColumnSelected;
    }

    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventAfterColumnSelected()
    {
        if (eventAfterColumnSelected == null)
        {
            eventAfterColumnSelected = new DataBookHandler();
        }
        return eventAfterColumnSelected;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventOpened()
    {
        if (eventOpened == null)
        {
            eventOpened = new DataBookHandler();
        }
        return eventOpened;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataBookHandler eventClosed()
    {
        if (eventClosed == null)
        {
            eventClosed = new DataBookHandler();
        }
        return eventClosed;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addControl(IControl pControl)
    {
        synchronized (rootDataBook)
        {
            super.addControl(pControl);

            invokeRepaintListeners = true; // ensure the event is published to controls
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowIndex()
    {
        synchronized (rootDataBook)
        {
            try
            {
                sync();
                return iSelectedRowIndex;
            }
            catch (ModelException e)
            {
                return -1;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDataPage getDataPage()
    {
        synchronized (rootDataBook)
        {
            try
            {
                sync();
                return dpCurrentDataPage;
            }
            catch (ModelException e)
            {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        try
        {
            close();
        }
        catch (Throwable pThrowable)
        {
            // Silent close();
        }
        super.finalize();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(int pColumnIndex) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            if (getSelectedRow() < 0)
            {
                return null;
            }

            return super.getValue(pColumnIndex);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(String pColumnName) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            if (getSelectedRow() < 0)
            {
                return null;
            }

            return super.getValue(pColumnName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRawValue(int pColumnIndex) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            if (getSelectedRow() < 0)
            {
                return null;
            }

            return super.getRawValue(pColumnIndex);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRawValue(String pColumnName) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
            if (getSelectedRow() < 0)
            {
                return null;
            }

            return super.getRawValue(pColumnName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String pColumnName, Object pValue) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for setting a value.");
            }
            sync();
            if (iSelectedRowIndex < -1 || (!additionalDataRowVisible && iSelectedRowIndex < 0))
            {
                throw new ModelException(getName() + ": No selected row!");
            }
    /*      if (isDeleting())
            {
                throw new ModelException("Row is already deleted!");
            }*/
            if (additionalDataRowVisible && iSelectedRowIndex == -1)
            {
                getAdditionalDataRow().setValue(pColumnName, pValue);
                
                notifyRepaintControls();
            }
            else
            {
                update();
        
                super.setValue(pColumnName, pValue);
                
                calculateRow();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getValues(String[] pColumnNames) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return null;
            }
            sync();
    
            if (getSelectedRow() < 0)
            {
                return null;
            }

            return super.getValues(pColumnNames);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDataRow createDataRow(String[] pColumnNames) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                return createEmptyDataRow(pColumnNames);
            }
            sync();
    
            return super.createDataRow(pColumnNames);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(String[] pColumnNames, Object[] pValues) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for setting values.");
            }
            if (pColumnNames == null || pColumnNames.length > 0)
            {
                sync();
                if (getSelectedRow() < 0)
                {
                    throw new ModelException(getName() + ": No selected row!");
                }
    /*          if (isDeleting())
                {
                    throw new ModelException("Row is already deleted!");
                }*/
                if (additionalDataRowVisible && getSelectedRow() == 0)
                {
                    getAdditionalDataRow().setValues(pColumnNames, pValues);
                    
                    notifyRepaintControls();
                }
                else 
                {
                    update();
            
                    super.setValues(pColumnNames, pValues);
                    
                    calculateRow();
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder sbResult = new StringBuilder();
        
        sbResult.append("Class           = ");
        sbResult.append(getClass().getName());
        sbResult.append("\nName            = ");
        sbResult.append(sName);
        sbResult.append("\nisOpen          = ");
        sbResult.append(bIsOpen);
        
        if (rdRowDefinition != null)
        {
            sbResult.append("\nRowDefinition   = ");
            sbResult.append(rdRowDefinition);
        }
        if (rdMasterReference != null)
        {
            sbResult.append("\nMasterReference = ");
            sbResult.append(rdMasterReference);
        }
        if (cFilter != null)
        {
            sbResult.append("\nFilter          = ");
            sbResult.append(cFilter);
        }
        if (sSort != null)
        {
            sbResult.append("\nSortDefinition  = ");
            sbResult.append(sSort);
        }
        
        if (auDetailDataBooks != null && auDetailDataBooks.size() > 0)
        {
            sbResult.append("\nDetailBooks     = [");
            
            for (int i = 0, j = 0, cnt = auDetailDataBooks.size(); i < cnt; i++)
            {
                IDataBook dataBook = auDetailDataBooks.get(i).get();
                
                if (dataBook != null)
                {
                    if (j > 0)
                    {
                        sbResult.append(", ");
                    }
                    
                    sbResult.append(dataBook.getName());
                    
                    j++;
                }
            }
            sbResult.append("]");
        }
        
        if (bIsOpen)
        {
            // Sync is not good in toString() as debugging or and other System.out will change behaviour.
            // toString should just refect current state!
//            try
//            {
//                sync();
//            }
//            catch (ModelException modelException)
//            {
//                sbResult.append("\n");
//                sbResult.append(ExceptionUtil.dump(modelException, true));
//                
//                return sbResult.toString();
//            }
                
            if (treePath != null && treePath.length() > 0)
            {
                sbResult.append("\nTreePath        = ");
                sbResult.append(treePath);
            }
            
            sbResult.append("\nSelectedRow     = ");
            sbResult.append(isAdditionalDataRowVisible() ? iSelectedRowIndex + 1 : iSelectedRowIndex);
            
            if (isAdditionalDataRowVisible())
            {
                sbResult.append("\nAdditionalDataRow = ");
                sbResult.append(additionalDataRow);
            }
            
            sbResult.append("\n\n== CURRENT DataPage ==\n\n");
            sbResult.append(dpCurrentDataPage);
            
            Enumeration<MemDataPage> dpCacheElements = htDataPagesCache.elements();
            Enumeration<Object> dpCacheKeys = htDataPagesCache.keys();
            
            boolean bFirst = true;
                
            while (dpCacheElements.hasMoreElements())
            {
                if (bFirst)
                {
                    sbResult.append("\n\n== DataPage CACHE ==\n");
                    
                    bFirst = false;
                }
                
                sbResult.append("\n");
                
                MemDataPage dpElement = dpCacheElements.nextElement();
                Object drKey = dpCacheKeys.nextElement();
                
                sbResult.append("Key      = ");
                sbResult.append(drKey);

                if (dpElement != dpCurrentDataPage)
                {
                    sbResult.append(", Class = ");
                    sbResult.append(drKey.getClass().getName());
                    sbResult.append("\n");
                    sbResult.append(dpElement.toString("           "));
                }
                else
                {
                    sbResult.append(" => (CURRENT DataPage)");
                    sbResult.append(", Class = ");
                    sbResult.append(drKey.getClass().getName());
                }
            }
        }
        
        return sbResult.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepaintControls()
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen && invokeRepaintListeners)
            {
                invokeRepaintListeners = false;
                
                try
                {
                    invokeRepaintListeners();
                }
                finally
                {
                    if (factory == null)
                    {
                        syncOnNotifyRepaintControls = false;
                        invokeRepaintListeners = true;
                    }
                    else
                    {
                        if (!invokeLaterRepaintListenersCalled)
                        {
                            invokeLaterRepaintListenersCalled = true;
            
                            factory.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    // To ensure, that #746 still works after changes of #2679 
                                    // the databook is synced invoke later, if there are no controls
                                    if (syncOnNotifyRepaintControls && bIsOpen && isOutOfSync() && !hasPaintableControls(MemDataBook.this))
                                    {
                                        try
                                        {
                                            sync();
                                        }
                                        catch (Throwable e)
                                        {
                                            e.printStackTrace(); // Normally the controls would log the sync
                                        } 
                                    }
                                    syncOnNotifyRepaintControls = false;
                                    
                                    invokeLaterRepaintListenersCalled = false;
                                    invokeRepaintListeners = true;
                                }
                            });
                        }
                    }
                }
            }
        }
    }
        
    /**
     * {@inheritDoc}
     */
    @Override
    public IRowDefinition getRowDefinition()
    {
        if (additionalDataRowVisible && iSelectedRowIndex == -1 && additionalRowDefinition != null)
        {
            return additionalRowDefinition;
        }
        return super.getRowDefinition();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setDispatchEventsEnabled(boolean pEnabled)
    {
    	super.setDispatchEventsEnabled(pEnabled);
    	
    	Field[] fields = getClass().getDeclaredFields();

    	Object oInstance;
    	
    	for (int i = 0; i < fields.length; i++)
    	{
    		if (fields[i].getType() == DataBookHandler.class)
    		{
    			try
    			{
	    			oInstance = fields[i].get(this);

	    			if (oInstance != null)
	    			{
	    				((DataBookHandler)oInstance).setDispatchEventsEnabled(pEnabled);
	    			}
    			}
    			catch (Exception e)
    			{
    				//next field
    			}
    		}
    	}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * True, if the after row selected event is also dispatched, when the same row is selected again.
     * 
     * @return true, if the after row selected event also is dispatched, when the same row is selected again.
     */
    public boolean isAlwaysDispatchAfterRowSelectedEvent()
    {
        return alwaysDispatchSelectionEvent;
    }
    
    /**
     * True, if the after row selected event is also dispatched, when the same row is selected again.
     * 
     * @param pAlwaysDispatchSelectionEvent true, if the after row selected event also is dispatched, when the same row is selected again.
     */
    public void setAlwaysDispatchAfterRowSelectedEvent(boolean pAlwaysDispatchSelectionEvent)
    {
        alwaysDispatchSelectionEvent = pAlwaysDispatchSelectionEvent;
    }
    
    /**
     * Restore all changes from top -> down to all detail Databooks. It just restires the changes in the specified pDataBook.
     * 
     * @param pCurrentDataBook  the Current DataBook in the Levels from Root DataBook to the last detail DataBook.
     * @param pDataBook         the DataBook in which the changes will be restored.
     * @throws ModelException   if an Error occur during save to storage.
     */
    private void restoreTopDownAllRows(IDataBook pCurrentDataBook, IDataBook pDataBook) throws ModelException
    {
        if (pCurrentDataBook == pDataBook)
        {
            pCurrentDataBook.restoreDataPage();
        }
        else
        {
            int         iOldRowIndex     = pCurrentDataBook.getSelectedRow();
            IDataBook[] auDetailDataBook = pCurrentDataBook.getDetailDataBooks();
            ArrayUtil<TreePath> auOldTreePath = new ArrayUtil<TreePath>();

            if (auDetailDataBook != null)
            {
                for (int i = 0, iSize = auDetailDataBook.length; i < iSize; i++)
                {
                    if (auDetailDataBook[i].isSelfJoined())
                    {
                        auOldTreePath.add(auDetailDataBook[i].getTreePath());
                    }
                }
            }
            
            boolean additionalRow = pCurrentDataBook.isAdditionalDataRowVisible();
            if (additionalRow)
            {
                pCurrentDataBook.setAdditionalDataRowVisible(false);
            }
            try
            {
                int[] iaChangedRows = pCurrentDataBook.getChangedRows();
                
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (pCurrentDataBook.getDataRow(iaChangedRows[i]).isDetailChanged())
                    {
                        pCurrentDataBook.setSelectedRow(iaChangedRows[i]);
                        if (auDetailDataBook != null)
                        {
                            //save all details, till the detail is not changed anymore!
                            for (int j = 0, jSize = auDetailDataBook.length; j < jSize && pCurrentDataBook.isDetailChanged(); j++)
                            {
                                if (auDetailDataBook[j].isSelfJoined())
                                {
                                    // start in Detail at the root node of the hierarchy
                                    auDetailDataBook[j].setTreePath(null);
                                }
                                restoreTopDownAllRows(auDetailDataBook[j], pDataBook);
                            }
                        }
                        
                        // if still changes exists, and self joined, then save the self joined hierarchy down too.
                        if (pCurrentDataBook.isSelfJoined()
                                && pCurrentDataBook.isDetailChanged()
                                && pCurrentDataBook.hasDataPage(pCurrentDataBook))
                        {
                            // get Master.
                            TreePath tpOld = pCurrentDataBook.getTreePath();
                            
                            // use the current row as master
                            pCurrentDataBook.setTreePath(tpOld.getChildPath(pCurrentDataBook.getSelectedRow()));
                            
                            restoreTopDownAllRows(pCurrentDataBook, pDataBook);
                            
                            //select back the treepath, selected row will set back later in this method.
                            pCurrentDataBook.setTreePath(tpOld);
                        }
                        int oldLength = iaChangedRows.length;
                        iaChangedRows = pCurrentDataBook.getChangedRows();
                        if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDetailChanged())
                        {
                            i--;
                        }
                    }
                }
            }
            finally
            {
                if (additionalRow)
                {
                    pCurrentDataBook.setAdditionalDataRowVisible(true);
                }
            }
            
            pCurrentDataBook.setSelectedRow(iOldRowIndex);
            
            if (auDetailDataBook != null)
            {
                int j = 0;
                for (int i = 0, iSize = auDetailDataBook.length, jSize = auOldTreePath.size();
                     i < iSize && j < jSize; i++)
                {
                    if (auDetailDataBook[i].isSelfJoined())
                    {
                        auDetailDataBook[i].setTreePath(auOldTreePath.get(j));
                        j++;
                    }
                }
            }
        }
    }
    
    /**
     * Restores all rows in the current DataPage.
     * 
     * @see #restoreAllRows()
     * @throws ModelException - if an ModelException happens during undo the changes.
     */
    public void restoreDataPage() throws ModelException
    {
        synchronized (rootDataBook)
        {
            // store Selection & TreePath
            int iOldSelectedRow = getSelectedRow();
            ArrayUtil<TreePath> auOldTreePath = getAllDetailsTreePath();
            
            restoreDataPageIntern();
            
            // restore selection and TreePath
            if (iOldSelectedRow >= getRowCount())
            {
                iSelectedRowIndex = iOldSelectedRow;
                correctSelectedRowAfterDelete();
            }
            else
            {
                if (getSelectedRow() != iOldSelectedRow)
                {
                    setSelectedRow(iOldSelectedRow);
                }
                setAllDetailsTreePath(auOldTreePath);
            }
            
            notifyRepaintControls();
        }
    }
    
    /**
     * Restores all rows in the current DataPage.
     * It stores/restores no selectedRow&TreePath
     * 
     * @throws ModelException if restoreSelected() fails
     */
    private void restoreDataPageIntern() throws ModelException
    {
        ICondition oldFilter = null;
        boolean additionalRow = isAdditionalDataRowVisible();
        if (additionalRow)
        {
            setAdditionalDataRowVisible(false);
        }
        try
        {
            // setFilter null, to see all changed rows!
            if (isMemFilter() && getWritebackIsolationLevel() != WriteBackIsolationLevel.DATA_ROW
                    && getFilter() != null)
            {
                oldFilter = getFilter();
                cFilter = null;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                clearFilterSortInMemDataPages();
                sync();
            }
            
            int[] iaChangedRows = dpCurrentDataPage.getChangedRows();

            for (int i = 0; i < iaChangedRows.length; i++)
            {
                setSelectedRow(iaChangedRows[i]);
                restoreSelectedRowInternIncludeDetails();
                sync();
                int oldLength = iaChangedRows.length;
                iaChangedRows = dpCurrentDataPage.getChangedRows();
                if (iaChangedRows.length < oldLength && i < iaChangedRows.length)
                {
                    i--;
                }
            }
        }
        finally
        {
            if (oldFilter != null)
            {
                cFilter = oldFilter;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                clearFilterSortInMemDataPages();
            }
            if (additionalRow)
            {
                setAdditionalDataRowVisible(true);
            }
        }
    }
    
    /**
     * Saves all rows in the current DataPage.
     * It stores/restores no selectedRow&TreePath
     * 
     * @throws ModelException if saveSelected() fails
     */
    private void saveDataPageIntern() throws ModelException
    {
        ICondition oldFilter = null;

        boolean additionRow = isAdditionalDataRowVisible();
        if (additionRow)
        {
            setAdditionalDataRowVisible(false);
        }
        try
        {
            // setFilter null, to see all changed rows!
            if (isMemFilter() && getWritebackIsolationLevel() != WriteBackIsolationLevel.DATA_ROW
                    && getFilter() != null)
            {
                oldFilter = getFilter();
                cFilter = null;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                clearFilterSortInMemDataPages();
                sync();
            }
            int[] iaChangedRows = dpCurrentDataPage.getChangedRows();
            
            if (iaChangedRows.length > 0)
            {
                // #391 - Wrong execution order in DataSource level
                // no sort, do/redo all changes in the following order
                // 1) all deletes
                // 2) all updates
                // 3) all inserts
                
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDeleting())
                    {
                         setSelectedRow(iaChangedRows[i]);
                         saveSelectedRowInternIncludeDetails();
                         sync();
                         int oldLength = iaChangedRows.length;
                         iaChangedRows = dpCurrentDataPage.getChangedRows();
                         if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDeleting())
                         {
                             i--;
                         }
                    }
                }
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (dpCurrentDataPage.getDataRow(iaChangedRows[i]).isUpdating())
                    {
                        setSelectedRow(iaChangedRows[i]);
                        saveSelectedRowInternIncludeDetails();
                        sync();
                        int oldLength = iaChangedRows.length;
                        iaChangedRows = dpCurrentDataPage.getChangedRows();
                        if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isUpdating())
                        {
                            i--;
                        }
                    }
                }
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (dpCurrentDataPage.getDataRow(iaChangedRows[i]).isInserting())
                    {
                        setSelectedRow(iaChangedRows[i]);
                        saveSelectedRowInternIncludeDetails();
                        sync();
                        int oldLength = iaChangedRows.length;
                        iaChangedRows = dpCurrentDataPage.getChangedRows();
                        if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isInserting())
                        {
                            i--;
                        }
                    }
                }
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDetailChanged())
                    {
                        setSelectedRow(iaChangedRows[i]);
                        saveSelectedRowInternIncludeDetails();
                        sync();
                        int oldLength = iaChangedRows.length;
                        iaChangedRows = dpCurrentDataPage.getChangedRows();
                        if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDetailChanged())
                        {
                            i--;
                        }
                    }
                }
            }
        }
        finally
        {
            if (oldFilter != null)
            {
                cFilter = oldFilter;                     // Do set Filter silent, as otherwise all Editors will save in a Master Detail relation
                clearFilterSortInMemDataPages();
            }
            if (additionRow)
            {
                setAdditionalDataRowVisible(true);
            }
        }
    }
    
    /**
     * Save all changes from top -> down to all detail Databooks. It just save the changes in the specified pDataBook.
     * 
     * @param pCurrentDataBook  the Current DataBook in the Levels from Root DataBook to the last detail DataBook.
     * @param pDataBook         the DataBook in which the changes will be saved.
     * @throws ModelException   if an Error occur during save to storage.
     */
    private void saveTopDownAllRows(IDataBook pCurrentDataBook, IDataBook pDataBook) throws ModelException
    {
        if (pCurrentDataBook == pDataBook)
        {
            pCurrentDataBook.saveDataPage();
        }
        else
        {
            int         iOldRowIndex     = pCurrentDataBook.getSelectedRow();
            IDataBook[] auDetailDataBook = pCurrentDataBook.getDetailDataBooks();
            ArrayUtil<TreePath> auOldTreePath = new ArrayUtil<TreePath>();

            if (auDetailDataBook != null)
            {
                for (int i = 0, iSize = auDetailDataBook.length; i < iSize; i++)
                {
                    if (auDetailDataBook[i].isSelfJoined())
                    {
                        auOldTreePath.add(auDetailDataBook[i].getTreePath());
                    }
                }
            }
            
            boolean additionalRow = pCurrentDataBook.isAdditionalDataRowVisible();
            if (additionalRow)
            {
                pCurrentDataBook.setAdditionalDataRowVisible(false);
            }
            try
            {
                int[] iaChangedRows = pCurrentDataBook.getChangedRows();
                
                for (int i = 0; i < iaChangedRows.length; i++)
                {
                    if (pCurrentDataBook.getDataRow(iaChangedRows[i]).isDetailChanged())
                    {
                        pCurrentDataBook.setSelectedRow(iaChangedRows[i]);
                        if (auDetailDataBook != null)
                        {
                            //save all details, till the detail is not changed anymore!
                            for (int j = 0, jSize = auDetailDataBook.length; j < jSize && pCurrentDataBook.isDetailChanged(); j++)
                            {
                                if (auDetailDataBook[j].isSelfJoined())
                                {
                                    // start in Detail at the root node of the hierarchy
                                    auDetailDataBook[j].setTreePath(null);
                                }
                                saveTopDownAllRows(auDetailDataBook[j], pDataBook);
                            }
                        }
                        
                        // if still changes exists, and self joined, then save the self joined hierarchy down too.
                        if (pCurrentDataBook.isSelfJoined()
                                && pCurrentDataBook.isDetailChanged()
                                && pCurrentDataBook.hasDataPage(pCurrentDataBook))
                        {
                            // get Master.
                            TreePath tpOld = pCurrentDataBook.getTreePath();
                            
                            // use the current row as master
                            pCurrentDataBook.setTreePath(tpOld.getChildPath(pCurrentDataBook.getSelectedRow()));
                            
                            saveTopDownAllRows(pCurrentDataBook, pDataBook);
                            
                            //select back the treepath, selected row will set back later in this method.
                            pCurrentDataBook.setTreePath(tpOld);
                        }
                        int oldLength = iaChangedRows.length;
                        iaChangedRows = pCurrentDataBook.getChangedRows();
                        if (iaChangedRows.length < oldLength && i < iaChangedRows.length && dpCurrentDataPage.getDataRow(iaChangedRows[i]).isDetailChanged())
                        {
                            i--;
                        }
                    }
                }
            }
            finally
            {
                if (additionalRow)
                {
                    pCurrentDataBook.setAdditionalDataRowVisible(true);
                }
                
            }
            
            pCurrentDataBook.setSelectedRow(iOldRowIndex);
            
            if (auDetailDataBook != null)
            {
                int j = 0;
                for (int i = 0, iSize = auDetailDataBook.length, jSize = auOldTreePath.size();
                     i < iSize && j < jSize; i++)
                {
                    if (auDetailDataBook[i].isSelfJoined())
                    {
                        auDetailDataBook[i].setTreePath(auOldTreePath.get(j));
                        j++;
                    }
                }
            }
        }
    }
    
    /**
     * Returns all current TreePaths of all Detail DataBooks in a List.
     * 
     * @return all current TreePaths of all Detail DataBooks in a List.
     */
    private ArrayUtil<TreePath> getAllDetailsTreePath()
    {
        if (auDetailDataBooks != null)
        {
            ArrayUtil<TreePath> auOldTreePath = new ArrayUtil<TreePath>();
            
            for (IDataBook detail : getDetailDataBooks())
            {
                if (detail.isSelfJoined())
                {
                    auOldTreePath.add(detail.getTreePath());
                }
            }
            return auOldTreePath;
        }
        return null;
    }
    
    /**
     * Sets all TreePaths of all Detail DataBooks from the List.
     * 
     * @param pOldTreePath      List of TreePath (only one for each self joined Detail DataBook)
     * @throws ModelException   If the TreePath couldn't set.
     */
    private void setAllDetailsTreePath(ArrayUtil<TreePath> pOldTreePath) throws ModelException
    {
        if (auDetailDataBooks != null && pOldTreePath != null)
        {
            int j = 0;
            for (int i = 0, iSize = auDetailDataBooks.size(), jSize = pOldTreePath.size();
                 i < iSize && j < jSize; i++)
            {
                IDataBook detail = auDetailDataBooks.get(i).get();
                if (detail.isSelfJoined())
                {
                    detail.setTreePath(pOldTreePath.get(j));
                    j++;
                }
            }
        }
    }
    
    /**
     * It saves the selectedRow intern and saves all detail rows under it.
     * 
     * @throws ModelException if the save fails.
     */
    private void saveSelectedRowInternIncludeDetails() throws ModelException
    {
        saveSelectedRowIntern();
        
        // save all Details -> selfjoined and detail DataBooks.
        if (isSelfJoined())
        {
            saveSelfJoinedDetailRows();
        }
        if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                dataBook.saveDataPage();
            }
        }
    }
    
    /**
     * It repositions the current DataRow depending on the current Filter and Sort.
     * 
     * @throws ModelException if the repositioning fails.
     */
    private void repositionCurrentDataRow() throws ModelException
    {
        // if sort set, the find correct position. Otherwise use selectedRow
        if (sSort != null && sortDataRowOnSave)
        {
            // Search, if row is smaller than a row before.
            // Only already saved rows have to be looked at.
            int iRow = iSelectedRowIndex;
            IChangeableDataRow currentRow;
            while (iRow - 1 >= 0
                    && !((currentRow = (IChangeableDataRow)dpCurrentDataPage.getDataRow(iRow - 1)).isDeleting() || currentRow.isUpdating() || currentRow.isInserting())
                    && compareTo(currentRow, sSort) < 0)
            {
                iRow--;
            }
            
            if (iRow == iSelectedRowIndex)
            {
                int size = dpCurrentDataPage.getRowCount();
                while (iRow + 1 < size
                        && !((currentRow = (IChangeableDataRow)dpCurrentDataPage.getDataRow(iRow + 1)).isDeleting() || currentRow.isUpdating() || currentRow.isInserting())
                        && compareTo(currentRow, sSort) > 0)
                {
                    iRow++;
                }
            }
            
            if (iRow != iSelectedRowIndex)
            {
                // delete row from current page
                changeCounter += dpCurrentDataPage.delete(iSelectedRowIndex);
                changeCounter += dpCurrentDataPage.insert(iRow, this);

                if (dpOldCurrentDataPageSaveAllRows == dpCurrentDataPage)
                {
                    if (iOldSelectedRowSaveAllRows == iSelectedRowIndex)
                    {
                        iOldSelectedRowSaveAllRows = iRow;
                    }
                    else if (iSelectedRowIndex > iOldSelectedRowSaveAllRows && iRow <= iOldSelectedRowSaveAllRows)
                    {
                        iOldSelectedRowSaveAllRows++;
                    }
                    else if (iSelectedRowIndex < iOldSelectedRowSaveAllRows && iRow > iOldSelectedRowSaveAllRows)
                    {
                        iOldSelectedRowSaveAllRows--;
                    }
                }
                    
                iSelectedRowIndex = iRow;
            }
        }
    }
    
    /**
     * It add pNewDataRow to the right position in the pDataPage.
     * 
     * @param pDataPage     the DataPage to use to add the DataRow.
     * @throws ModelException if the add of the DataRow fails.
     */
    private void addDataRowAtRightPosition(MemDataPage pDataPage) throws ModelException
    {
        boolean bInFilter = true;
        int row = 0;
        // if its refetchable(RemoteDataBook), then check filter, then sort it in.
        if (isDataPageRefetchPossible())
        {
            // if server filter or sort, try to find the correct position position in memory.
            if (getFilter() != null && !isMemFilter())
            {
                if (!getFilter().isFulfilled(this))
                {
                    bInFilter = false;
                }
            }
            if (bInFilter && sSort != null && sortDataRowOnSave && !isMemSort())
            {
                for (int iSize = pDataPage.getRowCount();
                        row < iSize && compareTo(pDataPage.getDataRow(row), sSort) > 0; row++)
                {
                    // nothing todo, it add +1 to iRow for the correct index to sort into.
                }
            }
        }
        // if in Filter then insert it.
        // if its MemDataBook (=!isDataPageRefetchPossible()), then insert always. After that the filter/sort is cleared to refilter/resort!
        if (bInFilter)
        {
            // insert in new page
            changeCounter += pDataPage.insert(row, this);
            
            if (dpOldCurrentDataPageSaveAllRows == pDataPage && row <= iOldSelectedRowSaveAllRows)
            {
                iOldSelectedRowSaveAllRows++;
            }
        }
        if (!isDataPageRefetchPossible())
        {
            // clear filter & sort; should be renewed if MemDataBook!
            pDataPage.clear();

            // find the same row by PrimaryKey
            String[] asPKColumns = rdRowDefinition.getPrimaryKeyColumnNames();
            if (asPKColumns == null)
            {
                asPKColumns = rdRowDefinition.getColumnNames();
            }
            row = pDataPage.searchNext(Filter.createEqualsFilter(this, asPKColumns, false));
        }
        if (bInFilter && isSelfJoined()) // row was inserted. TreePath has to checked and corrected, if row is inserted before a row in the treepath.
        {
            //check if the TreePath has to be changed, because of the move of the DataRow.
            // The reason is that the indices in TreePath change implicit!
            IDataRow drRoot = null;
            if (rdTreeRootReference != null)
            {
                drRoot = rdTreeRootReference.getReferencedDataBook();
            }
            IDataPage dpDataPage = getDataPageWithRootRow(drRoot);
            int parentLevel = 0;
            while (pDataPage != dpDataPage && dpDataPage != null
                    && parentLevel < treePath.length() - 1)
            {
                IDataRow masterRow = dpDataPage.getDataRow(treePath.get(parentLevel));
                if (masterRow == null)
                {
                    dpDataPage = null;
                }
                else
                {
                    dpDataPage = getDataPageIntern(masterRow);
                }
                parentLevel++;
            }
            if (pDataPage == dpDataPage && row <= treePath.get(parentLevel))
            {
                treePath = treePath.set(parentLevel, treePath.get(parentLevel) + 1);
            }
        }
    }
    
    /**
     * It moves the current DataRow to the master column values corresponding DataPage.
     * 
     * @return true, if data row was moved to another data page, false otherwise.
     * @throws ModelException move of the DataRow fails.
     */
    private boolean moveDataRowToCorrectMaster() throws ModelException
    {
        if (rdMasterReference != null)
        {
            IDataRow currentMaster = dpCurrentDataPage.getMasterDataRow();
            IDataRow newMasterRow = currentMaster.createEmptyDataRow(null);
            newMasterRow.setValues(null, getValues(rdMasterReference.getColumnNames()));

            if (!newMasterRow.equals(currentMaster))
            {
                // is new one already existing?
                MemDataPage dpNewDataPage = null;

                if (hasDataPage(newMasterRow))
                {
                    dpNewDataPage = (MemDataPage)getDataPage(newMasterRow);
                    
                    addDataRowAtRightPosition(dpNewDataPage);
                }
                else if (!isDataPageRefetchPossible())
                {
                    // getNew DataPage and add the Updated DataRow.
                    dpNewDataPage = (MemDataPage)getDataPage(newMasterRow);

                    changeCounter += dpNewDataPage.insert(0, this);
                }
                
                if (dpOldCurrentDataPageSaveAllRows == dpCurrentDataPage && iSelectedRowIndex < iOldSelectedRowSaveAllRows)
                {
                    iOldSelectedRowSaveAllRows--;
                }
                    
                // delete row from old page
                changeCounter += dpCurrentDataPage.delete(iSelectedRowIndex);
    
                correctSelectedRowAfterDelete();
                
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the selectedRow internal. See also saveSelectedRow()
     * 
     * @throws ModelException if the save fails.
     */
    private void saveSelectedRowIntern() throws ModelException
    {
        // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
        // therefore an sync is needed!
        sync();
        if (iSelectedRowIndex < 0)
        {
            return;
        }
        
        if (isDeleting())
        {
            boolean hasBeforeDeleted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeDeleted);
            boolean hasAfterDeleted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterDeleted);
            boolean hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
            boolean hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            IDataRow drOriginalDataRow = hasBeforeDeleted || hasAfterDeleted || hasBeforeRowSelected || hasAfterRowSelected ? createDataRow(null) : null;
            int oldSelectedRow = getSelectedRow();
            int newSelectedRow = oldSelectedRow;
            if (iSelectedRowIndex == dpCurrentDataPage.getRowCount() - 1)
            {
                if (getSelectionMode() == SelectionMode.DESELECTED
                        || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED
                        || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
                {
                    newSelectedRow = -1;
                }
                else
                {
                    newSelectedRow = getRowCount() - 1;
                }
            }

            if (hasBeforeDeleted)
            {
                eventBeforeDeleted.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_DELETED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterDeleted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterDeleted);
                hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            
            if (hasBeforeRowSelected)
            {
                eventBeforeRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_ROW_SELECTED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterDeleted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterDeleted);
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }

            // delete details first
            if (isSelfJoined() && hasDataPage(this))
            {
                if (isDeleteCascade())
                {
                    // get Master.
                    TreePath tpOld = getTreePath();
                    
                    // use the current row as master
                    setTreePath(tpOld.getChildPath(iSelectedRowIndex));
                    deleteAllRows();
                        
                    //select back to the current row.
                    setTreePath(tpOld);

                    saveSelfJoinedDetailRows();
                }
                else
                {
                    saveSelfJoinedDetailRows();
                    
                    // no Writeback enabled, just remove the DataPage from Storage to remove all details
                    if (treePath != null)
                    {
                        removeDataPage(null, treePath.getChildPath(getSelectedRow()));
                    }
                    else
                    {
                        removeDataPage(null, null);
                    }
                }
            }
            if (auDetailDataBooks != null)
            {
                for (IDataBook dataBook : getDetailDataBooks())
                {
                    if (dataBook.isDeleteCascade())
                    {
                        dataBook.deleteAllRows();
                        dataBook.saveDataPage();
                    }
                    else
                    {
                        dataBook.saveDataPage();
                        
                        dataBook.removeDataPage(this, null);
                    }
                }
            }

            sync();
            if (isDeleting()) // One of the triggered events above could already have saved this row...
            {
                if (isWritebackEnabled())
                {
                    executeDelete(this);
                }
                store();
    
                // #378 - Exception in AfterDeleted Event, if an insert() is called in the user event
                // correct selected row before the AfterDeleted Event, if the last row got deleted
                correctSelectedRowAfterDelete();
                
                setDetailsChangedInMasterBook();
                
                if (hasAfterDeleted)
                {
                    eventAfterDeleted.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_DELETED, drOriginalDataRow,
                            treePath, treePath, oldSelectedRow, newSelectedRow));

                    hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
                }
    
                if (hasAfterRowSelected)
                {
                    eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                            treePath, treePath, oldSelectedRow, newSelectedRow));
                }
            }
        }
        else if (isUpdating())
        {
            boolean hasBeforeUpdated = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeUpdated);
            boolean hasAfterUpdated = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterUpdated);
            IDataRow drOriginalDataRow = getOriginalDataRow();
            
            if (hasBeforeUpdated)
            {
                int selectedRow = getSelectedRow();
                eventBeforeUpdated.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_UPDATED, drOriginalDataRow,
                        treePath, treePath, selectedRow, selectedRow));
                
                hasAfterUpdated = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterUpdated);
            }
            
            sync();
            if (isUpdating()) // One of the triggered events above could already have saved this row...
            {
                // #163 - if no writeable column is affected, don't update to the server!
                // -> isWriteable() columns just came from server over the getMetaData() call in RemoteDataBook.
                if (isWritebackEnabled() && isWritableColumnChanged())
                {
                    IDataRow dataRow = executeUpdate(this);
                    
                    if (dataRow != null)
                    {
                        setValuesIntern(null, dataRow.getValues(null));
                    }
                }
                
                store();
                invokeMasterChangedDetailsListeners();
                if (saMasterColumns != null && !equals(drOriginalDataRow, saMasterColumns))
                {
                    notifyDetailChanged(); // detail page is changed, detail changed flag has to be resetted!
                }
                
                // reparent DataRow if it should moved, because of an Master-Link/Selfjoined column update!
                if (!moveDataRowToCorrectMaster())
                {
                    //store();
                    repositionCurrentDataRow();
    
                    calculateRow();
                }
                
                if (hasAfterUpdated)
                {
                    int selectedRow = getSelectedRow();
                    eventAfterUpdated.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_UPDATED, drOriginalDataRow,
                            treePath, treePath, selectedRow, selectedRow));
                }
            }
        }
        else if (isInserting())
        {
            boolean hasBeforeInserted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeInserted);
            boolean hasAfterInserted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterInserted);
            IDataRow drOriginalDataRow = hasBeforeInserted || hasAfterInserted ? getOriginalDataRow() : null;
            
            if (hasBeforeInserted)
            {
                int selectedRow = getSelectedRow();
                eventBeforeInserted.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_INSERTED, drOriginalDataRow,
                        treePath, treePath, selectedRow, selectedRow));
                
                hasAfterInserted = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterInserted);
            }
            
            // if insert Master first
            if (rdMasterReference != null)
            {
                ReferenceDefinition dbParentReference = isSelfJoined() ? getRootReference() : rdMasterReference;
                if (dbParentReference != null && dbParentReference.getReferencedDataBook().isInserting())
                {
                    ((MemDataBook)dbParentReference.getReferencedDataBook()).saveSelectedRowIntern();

                    // #315 - saveSelectedRow() bug with more then one leave with isInsertung state in DATASOURCE level
                    // -> sync all details , before we save, so that bMaster changed is false again, and the changes of this level can be
                    // propagated to the details!
                    syncDetails();
                }
            }

            // save Parent in Tree first.
            if (isSelfJoined())
            {
                // get Master.
                TreePath tpOld        = getTreePath();
                int      iOldRowIndex = iSelectedRowIndex;
                
                // if I am not the master
                if (tpOld != null && tpOld.length() > 0)
                {
                    // save the master
                    setTreePathInternal(tpOld.getParentPath());
                    setSelectedRow(tpOld.getLast());

                    saveSelectedRowIntern();
                    
                    //select back to the current row.
                    setTreePathInternal(tpOld);
                    setSelectedRowInternal(iOldRowIndex);
                }
            }
            
            if (isInserting()) // One of the triggered events above could already have saved this row...
            {
                copyMasterColumnsToCurrentDetail(true);
    
                if (isWritebackEnabled())
                {
                    IDataRow dataRow = executeInsert(this);
                    
                    if (dataRow != null)
                    {
                        setValuesIntern(null, dataRow.getValues(null));
                    }
                }
    
                store(); // Mark row as stored, so UID rehash works.
                
                if (isSelfJoined() && hasDataPage(this))
                {
                    notifyMasterChanged();
                    bMasterChanged = false;
                }
                invokeMasterChangedDetailsListeners();
                
                // #288 - master DataRow rehash not working in DATASOUCE Level over more then 1 hierarchy ! - bug fixed.
                setUID(null);
    
                // reparent DataRow if it should moved, because of an Master-Link/Selfjoined column update!
                if (!moveDataRowToCorrectMaster())
                {
                    //store();
                    repositionCurrentDataRow();
                    
                    calculateRow();
                }
                
                if (hasAfterInserted)
                {
                    int selectedRow = getSelectedRow();
                    eventAfterInserted.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_INSERTED, drOriginalDataRow,
                            treePath, treePath, selectedRow, selectedRow));
                }
            }
        }
    }
    
    /**
     * Save all Changed rows in the selfjoined details. It saves hierarchy down to the last leeve/row.
     * 
     * @throws ModelException if an Error occur during save to storage.
     */
    private void saveSelfJoinedDetailRows() throws ModelException
    {
        if (isSelfJoined() && iSelectedRowIndex >= 0 && hasDataPage(this))
        {
            TreePath tpOld        = getTreePath();
            int      iOldRowIndex = iSelectedRowIndex;
            
            if (isDetailChanged())
            {
                // use the current row as master
                setTreePathInternal(tpOld.getChildPath(iSelectedRowIndex));
                saveDataPageIntern();
                
                //select back to the current row.
                setTreePathInternal(tpOld);
                if (iOldRowIndex != iSelectedRowIndex)
                {
                    setSelectedRow(iOldRowIndex);
                }
            }
        }
    }
    
    /**
     * Gets true, if the data book has any paintable control.
     * @param pDataBook the data book.
     * @return true, if the data book has any paintable control.
     */
    protected boolean hasPaintableControls(IDataBook pDataBook)
    {
        for (IControl control : pDataBook.getControls())
        {
            if (control instanceof UIComponent<?>)
            {
                IComponent realComponent = ((UIComponent<?>)control).getUIResource();
                
                if (realComponent instanceof IControl)
                {
                    if (realComponent instanceof IEditorControl && ((IEditorControl)realComponent).getDataRow() == pDataBook
                            || realComponent instanceof ITableControl && ((ITableControl)realComponent).getDataBook() == pDataBook
                            || realComponent instanceof ITreeControl && ArrayUtil.containsReference(((ITreeControl)realComponent).getDataBooks(), pDataBook))
                    {
                        return true;
                    }
                }
                else if (control instanceof IEditorControl || control instanceof ITableControl || control instanceof ITreeControl)
                {
                    return true;
                }
                
            }
            else
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Detects, if any detail data book has a control to repaint.
     * @param pRootDataBook the root data book
     * @return true, if any detail data book has a control to repaint.
     */
    protected boolean hasSyncedDetailDataBookWithPaintableControls(IDataBook pRootDataBook)
    {
        for (IDataBook dataBook : pRootDataBook.getDetailDataBooks())
        {
            if ((!dataBook.isOutOfSync() && hasPaintableControls(dataBook)) || hasSyncedDetailDataBookWithPaintableControls(dataBook))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Reloads without sending before reload event.
     * 
     * @throws ModelException if reloading failed
     */
    protected void reloadIntern() throws ModelException
    {
        executeRefresh();

        // #2679 This causes immediate notification of detail databooks, while this data book is still out of sync.
        // So all the detail data books are resynced and repainted, as they cannot check, if this master databook is really changed.
        if (hasSyncedDetailDataBookWithPaintableControls(this))
        {
            // if any detail data book has a control attached, we wait for the sync of this data book, 
            // to give the detail data books a chance to check, if this data book really changed. 
            syncOnNotifyRepaintControls = true;
            notifyRepaintControls();
        }
        else
        {
            // Notify Master changed has to be before AFTER_RELOAD event, to prevent undefined states in event.
            // #746 - setFilter/reload on a master databook, doesn't sync details - use invokeMasterChangedDetailsListeners(); instead of notifyMasterChanged()
            invokeMasterChangedDetailsListeners();
        }
        
        if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
        {
            eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
        }
    }

    /**
     * Reload the current DataPage with the pSelectionMode.
     * 
     * @see #reload(javax.rad.model.IDataBook.SelectionMode)
     * @param pSelectionMode    pSelectionMode the Selection mode to use
     * @throws ModelException   if an DataSourceException happens during get the requested row.
     */
    public void reloadDataPage(SelectionMode pSelectionMode) throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (bIsOpen && !ignoreReload)
            {
                handleStoreSelection(pSelectionMode);
                
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeReload))
                {
                    eventBeforeReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_RELOAD));
                }
                
                if (!isOutOfSync())
                {
                    restoreDataPageIntern();
    
                    executeRefreshDataPage();
                }
    
                // correctSelectedRow has to be before AFTER_RELOAD event, to prevent undefined states in event.
                if (!isOutOfSync())
                {
                    correctSelectedRow();
                }
                
                notifyRepaintControls();
                
                // Notify Master changed has to be before AFTER_RELOAD event, to prevent undefined states in event.
                notifyMasterChanged();
                
                if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
                {
                    eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                }
            }
        }
    }
    
    /**
     * Returns true if an <code>IDataPage</code> to specified root row and TreePath
     * from the master <code>DataBook</code> exists.
     * 
     * @param pRootDataRow  the root <code>IDataRow</code> of the <code>DataBook</code> to check.
     * @param pTreePath     the <code>TreePath</code> to use.
     * @throws ModelException
     *            if the pRootDataRow don't contains the root columns from the DataBook.
     * @return true if an <code>IDataPage</code> to specified root row and TreePath
     *         from the master <code>DataBook</code> exists.
     */
    public boolean hasDataPage(IDataRow pRootDataRow, TreePath pTreePath) throws ModelException
    {
        synchronized (rootDataBook)
        {
            IDataRow drMaster = getMasterDataRowFromRootDataRow(pRootDataRow);
            MemDataPage dpNewCurrentDataPage = htDataPagesCache.get(drMaster);
            
            if (dpNewCurrentDataPage == null)
            {
                Object oUID = getUID(pRootDataRow);
                if (oUID != null)
                {
                    dpNewCurrentDataPage = htDataPagesCache.get(oUID);
                }
            }
            
            if (pTreePath == null)
            {
                return dpNewCurrentDataPage != null;
            }
    
            for (int i = 0; i < pTreePath.length(); i++)
            {
                int j = pTreePath.get(i);
                if (j >= 0 && j < dpNewCurrentDataPage.getRowCount())
                {
                    drMaster = getMasterDataRow(dpNewCurrentDataPage.getDataRow(j));
                    dpNewCurrentDataPage = htDataPagesCache.get(drMaster);
                    
                    if (dpNewCurrentDataPage == null)
                    {
                        Object oUID = getUID(drMaster);
                        if (oUID != null)
                        {
                            dpNewCurrentDataPage = htDataPagesCache.get(oUID);
                        }
                    }
                    
                    if (dpNewCurrentDataPage == null)
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * It constructs a new MemDataPage and puts it in the datapage cache.
     * 
     * Derived classed should override this and create the needed IDataPage.
     * 
     * @param pMasterDataRow the master IDataRow for this IDataPage.
     * @return the new MemDataPage to use.
     * @throws ModelException if getting the UID of the datarow failed.
     * @see #createDataPage(IDataRow)
     */
    protected MemDataPage createAndCacheDataPage(IDataRow pMasterDataRow) throws ModelException
    {
        MemDataPage dpNewCurrentDataPage = createDataPage(pMasterDataRow);
        
        Object oUID = getUID(pMasterDataRow);
        if (oUID != null)
        {
            htDataPagesCache.put(oUID, dpNewCurrentDataPage);
        }
        else
        {
            htDataPagesCache.put(pMasterDataRow, dpNewCurrentDataPage);
        }
        
        return dpNewCurrentDataPage;
    }
    
    /**
     * Gets all created data pages of this data book.
     * For master data books the list contains the current data page, if it is already created.
     * For detail data books the list contains all currently created data pages.
     * 
     * @return all created data pages.
     */
    public List<IDataPage> getDataPages()
    {
        ArrayList<IDataPage> result = new ArrayList<IDataPage>();
        
        if (rdMasterReference == null)
        {
            if (dpCurrentDataPage != null)
            {
                result.add(dpCurrentDataPage);
            }
        }
        else
        {
            result.addAll(htDataPagesCache.values());
        }
        
        return result;
    }
    
    
    /**
     * It constructs a new MemDataPage. Its every time used, when a new MemDataPage is
     * necessary.
     * Derived classed should override this and create the needed IDataPage.
     * 
     * @param pMasterDataRow    the master IDataRow for this IDataPage.
     * @return the new MemDataPage to use.
     */
    protected MemDataPage createDataPage(IDataRow pMasterDataRow)
    {
        return new MemDataPage(this, pMasterDataRow);
    }
    
    /**
     * Returns true if the sort is handled in memory.
     *
     * @return true if the sort is handled in memory.
     */
    protected boolean isMemSort()
    {
        return bMemSort;
    }

    /**
     * Sets if the sort is handled in memory.
     *
     * @param pMemSort true if the sort is handled in memory.
     */
    protected void setMemSort(boolean pMemSort)
    {
        bMemSort = pMemSort;
    }

    /**
     * Returns true if the filter is handled in memory.
     *
     * @return true if the filter is handled in memory.
     */
    protected boolean isMemFilter()
    {
        return bMemFilter;
    }

    /**
     * Sets if the sort is handled in memory.
     *
     * @param pMemFilter true if the filter is handled in memory.
     */
    protected void setMemFilter(boolean pMemFilter)
    {
        bMemFilter = pMemFilter;
    }
    
    /**
     * Returns true if this data book should write its changes to the storage.
     * 
     * @return true to write back all changes, false otherwise.
     */
    protected boolean isWritebackEnabled()
    {
        return bWritebackEnabled;
    }
    
    /**
     * Sets if this data book should write back its changes to the storage.
     * 
     * @param pWritebackEnabled determines if changes should be written to the store.
     */
    protected void setWritebackEnabled(boolean pWritebackEnabled)
    {
        bWritebackEnabled = pWritebackEnabled;
    }
    
    /**
     * Returns the selection that is stored at the last reload.
     * 
     * @return the selection that is stored at the last reload.
     */
    private ArrayUtil<IDataRow> getStoredSelection()
    {
        return auStoredSelection;
    }
    
    /**
     * Returns true if lock and refetch is enabled.
     * 
     * @return true if lock and refetch is enabled.
     */
    public boolean isLockAndRefetchEnabled()
    {
        return bLockAndRefetchEnabled;
    }

    /**
     * Sets whether if lock and refetch is enabled.
     * 
     * @param pLockAndRefetchEnabled true if lock and refetch is enabled.
     */
    public void setLockAndRefetchEnabled(boolean pLockAndRefetchEnabled)
    {
        bLockAndRefetchEnabled = pLockAndRefetchEnabled;
    }

    /**
     * Returns the count of rows which are read ahead from the IDBAccess.
     * A value of -1 means that all should be fetched.
     * 
     * @return the count of rows which are read ahead from the IDBAccess.
     */
    public int getReadAhead()
    {
        return iReadAheadRowCount;
    }

    /**
     * Sets the count of rows which are read ahead from the IDBAccess.
     * A value of -1 means that all should be fetched.
     * 
     * @param pReadAheadRowCount
     *            the row count to read ahead from the IDBAccess
     */
    public void setReadAhead(int pReadAheadRowCount)
    {
        iReadAheadRowCount = pReadAheadRowCount;
    }
    
    /**
     * Sets the the current selection before the reload.
     * 
     * @param pStoredSelection  the current selection as IDataRow (PK columns) to store
     * @throws ModelException   if in selfjoined DataBooks the parent row couldn't determined.
     */
    private void setStoredSelection(IDataRow pStoredSelection) throws ModelException
    {
        auStoredSelection.clear();
        if (pStoredSelection != null)
        {
            auStoredSelection.add(pStoredSelection);
    
            if (pStoredSelection != additionalDataRow && isSelfJoined() && treePath != null)
            {
                String[] asPKColumns = rdRowDefinition.getPrimaryKeyColumnNames();
                if (asPKColumns == null || asPKColumns.length == 0)
                {
                    asPKColumns = rdRowDefinition.getColumnNames();
                }
                TreePath currTreePath = treePath;
                    
                while (currTreePath.length() > 0)
                {
                    TreePath parent = currTreePath.getParentPath();
                    
                    IDataPage dataPage = getDataPage(parent);
                    if (dataPage != null)
                    {
                        IDataRow dataRow = dataPage.getDataRow(currTreePath.getLast());
                        if (dataRow != null)
                        {
                            auStoredSelection.add(dataRow.createDataRow(asPKColumns));
                        }
                    }
                        
                    currTreePath = parent;
                }
            }
        }
    }
        
    /**
     * Returns the row count in <code>IDBAccess</code> for this RemoteDataBook.
     * 
     * @return the row count of the DataBook.
     * @throws ModelException
     *              the the detail DataBook couldn't synchronized with the master DataBook
     */
    public int getEstimatedRowCount() throws ModelException
    {
        synchronized (rootDataBook)
        {
            if (!bIsOpen)
            {
                throw new ModelException(getName() + ": The databook must be open for getting the estimated rowcount.");
            }
            sync();
            
            return dpCurrentDataPage.getEstimatedRowCount();
        }
    }
    
    /**
     * Is true, if the current saved row should be moved to the correct possition according to the set SortDefinition.
     * Default is true.
     * @return true, if the current saved row should be moved to the correct possition according to the set SortDefinition.
     */
    public boolean isSortDataRowOnSave()
    {
        return sortDataRowOnSave;
    }
    
    /**
     * Is true, if the current saved row should be moved to the correct possition according to the set SortDefinition.
     * Default is true.
     * @param pSortDataRowOnSave true, if the current saved row should be moved to the correct possition according to the set SortDefinition.
     */
    public void setSortDataRowOnSave(boolean pSortDataRowOnSave)
    {
        sortDataRowOnSave = pSortDataRowOnSave;
    }
    
    /**
     * Its called when the inserting DataRow (new row) should be inserted.
     * Derived classed should override this to implemented the needed functionality.
     * e.g. store the new row in the storage.
     * 
     * @param pDataRow the data row to insert
     * @return the data row with current values from server, or null if the row is unchanged.
     * @throws ModelException   if an ModelException occur during insert.
     */
    protected IDataRow executeInsert(IChangeableDataRow pDataRow) throws ModelException
    {
        return null;
    }
    
    /**
     * Its called when the updating DataRow (changed row) should be updated
     * Derived classed should override this to implemented the needed functionality.
     * e.g. update the old row with the new row in the storage.
     * 
     * @param pDataRow the data row to update
     * @return the data row with current values from server, or null if the row is unchanged.
     * @throws ModelException   if an ModelException occur during update.
     */
    protected IDataRow executeUpdate(IChangeableDataRow pDataRow) throws ModelException
    {
        return null;
    }
    
    /**
     * Its called when the deleting DataRow (delete row) should be deleted.
     * Derived classed should override this to implemented the needed functionality.
     * e.g. delete the row in the storage.
     * 
     * @param pDataRow the data row to delete
     * @throws ModelException   if an ModelException occur during delete.
     */
    protected void executeDelete(IChangeableDataRow pDataRow) throws ModelException
    {
    }

    /**
     * Its called before the DataRow (existing row) will be changed.
     * Derived classed should override this to implemented the needed functionality.
     * e.g. lock and refetch the actual values of the row from storage
     * 
     * @param pDataRow the data row to lock
     * @return the data row with current values from server, or null if the row is unchanged.
     * @throws ModelException   if an ModelException occur during insert.
     */
    protected IDataRow executeLockAndRefetch(IChangeableDataRow pDataRow) throws ModelException
    {
        return null;
    }

    /**
     * Handles lock and refetch with updating current row, if it was changed.
     * 
     * @throws ModelException if lock fails.
     */
    private void lockAndRefetchIntern() throws ModelException
    {
        IDataRow dataRow = executeLockAndRefetch(this);
        
        if (dataRow != null && !dataRow.equals(this))
        {
            setValuesIntern(null, dataRow.getValues(null));
            
            notifyRepaintControls();
        }
    }
    
    /**
     * It invokes for all registered IConrols the notifyRepaint() method and calls for
     * all detail IDataBooks the the notifyMasterChanged method to determine that the
     * master has changed.
     */
    private void invokeMasterChangedDetailsListeners()
    {
        notifyRepaintControls();
        
        if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                dataBook.notifyMasterChanged();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataBook getRootDataBook()
    {
        return rootDataBook;
    }

    /**
     * Evaluates the current root data book.
     * @return the root data book.
     */
    protected IDataBook getRootDataBookIntern()
    {
        IDataBook           dbRoot = this;

        ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
        while (dbParentReference != null)
        {
            dbRoot            = dbParentReference.getReferencedDataBook();
            dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
        }
        
        return dbRoot;
    }
    
    
    /**
     * It invokes for each <code>IComponent</code>  the <code>saveEditing()</code> method.
     * @throws ModelException if saving the editors fails.
     */
    protected void invokeTreeSaveEditingControls() throws ModelException
    {
        IDataBook           dbRoot = this;

        if (!isReadOnly()) // Ensure Readonly databooks does not save editors of master databooks (eg in case of linkedcelleditors, they would immediately close).
        {
            ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
            while (dbParentReference != null)
            {
                dbRoot            = dbParentReference.getReferencedDataBook();
                dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
            }
        }
        
        invokeTopDownSaveEditingControls(dbRoot);
    }
    
    /**
     * It invokes for each <code>IComponent</code>  the <code>saveEditing()</code> method.
     * @param pDataBook the data book to be invoked.
     * @throws ModelException if saving the editors fails.
     */
    private void invokeTopDownSaveEditingControls(IDataBook pDataBook) throws ModelException
    {
        pDataBook.saveEditingControls();

        for (IDataBook detailDataBook : pDataBook.getDetailDataBooks())
        {
            invokeTopDownSaveEditingControls(detailDataBook);
        }
    }
    
    /**
     * Its invokes for each <code>IComponent</code>  the <code>cancelEditing()</code> method.<br>
     */
    protected void invokeTreeCancelEditingControls()
    {
        IDataBook           dbRoot = this;
        
        if (!isReadOnly()) // Ensure Readonly databooks does not save editors of master databooks (eg in case of linkedcelleditors, they would immediately close).
        {
            ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
            while (dbParentReference != null)
            {
                dbRoot            = dbParentReference.getReferencedDataBook();
                dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
            }
        }
        
        invokeTopDownCancelEditingControls(dbRoot);
    }
    
    /**
     * Its invokes for each <code>IComponent</code>  the <code>cancelEditing()</code> method.<br>
     * @param pDataBook the data book to be invoked.
     */
    private void invokeTopDownCancelEditingControls(IDataBook pDataBook)
    {
        pDataBook.cancelEditingControls();

        for (IDataBook detailDataBook : pDataBook.getDetailDataBooks())
        {
            invokeTopDownCancelEditingControls(detailDataBook);
        }
    }
    
    /**
     * Sync the DataBook an all it detail DataBooks. Its necessary in DATASOURCE level for saving cascades over varies levels.
     *
     * @throws ModelException if the sync isn't possible.
     */
    private void syncDetails() throws ModelException
    {
        sync();
        
        if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                dataBook.getSelectedRow(); // calls a sync!!!
            }
        }
    }

    /**
     * Returns the master row only with the primary key columns - like defined in
     * the getMasterReference().getReferencedColumns().
     * 
     * @param pMasterDataRow    the full/complete master IDataRow to use.
     * @return internal the master row only with the primary key columns - like defined in
     * the getMasterReference().getReferencedColumns().
     * @throws ModelException   if the master row couldn't created.
     */
    private IDataRow getMasterDataRow(IDataRow pMasterDataRow) throws ModelException
    {
        if (pMasterDataRow == null)
        {
            return new DataRow(rdMasterReference.getReferencedDataBook().getRowDefinition().createRowDefinition(rdMasterReference.getReferencedColumnNames()));
        }
        else
        {
            return pMasterDataRow.createDataRow(rdMasterReference.getReferencedColumnNames());
        }
    }
    
    /**
     * Returns the MemDataPage to the specified master IDataRow. If the none existing,
     * it will be a new one created and in the MemDataPage Hashtable stored.
     * 
     * @param pMasterDataRow    the master IDataRow to use.
     * @return the MemDataPage to the specified master IDataRow.
     * @throws ModelException   if master row or the MemDataPage couldn't created.
     */
    private MemDataPage getDataPageIntern(IDataRow pMasterDataRow) throws ModelException
    {
        // #336 - First check if a DataPage with the UID exists.
        MemDataPage dpNewCurrentDataPage = null;
        
        Object oUID = getUID(pMasterDataRow);
        if (oUID != null)
        {
            dpNewCurrentDataPage = htDataPagesCache.get(oUID);
            if (dpNewCurrentDataPage != null)
            {
                return dpNewCurrentDataPage;
            }
            else
            {
                String[] masterCols = rdMasterReference.getReferencedColumnNames();
                ChangeableDataRow masterRow = new ChangeableDataRow(
                        pMasterDataRow.getRowDefinition().createRowDefinition(masterCols), 
                        pMasterDataRow.getValues(masterCols), 
                        ((ChangeableDataRow)pMasterDataRow).getDataPage(), 
                        ((ChangeableDataRow)pMasterDataRow).getRowIndex());
                masterRow.setUID(oUID);
                    
                return createAndCacheDataPage(masterRow);
            }
        }
        
        IDataRow drMaster = getMasterDataRow(pMasterDataRow);
        dpNewCurrentDataPage = htDataPagesCache.get(drMaster);

        if (dpNewCurrentDataPage != null)
        {
            return dpNewCurrentDataPage;
        }
        else
        {
            return createAndCacheDataPage(drMaster);
        }
    }

    /**
     * Returns the the IChangeableDataRow synchronized for the specified row index.
     * 
     * @param pDataRowIndex the row index to use.
     * @return the the IChangeableDataRow synchronized for the specified row index
     * @throws ModelException if the IChangeableDataRow couldn't get from the MemDataPage.
     */
    private IChangeableDataRow getDataRowInternal(int pDataRowIndex) throws ModelException
    {
        if (pDataRowIndex < 0 || dpCurrentDataPage == dpEmptyDataPage)
        {
            return null;
        }
        
        return dpCurrentDataPage.getDataRow(pDataRowIndex);
    }

    /**
     * It correct after the sync() call the selected row under consideration of the
     * Selection Mode and the the stored selection of the last reload.
     * 
     * @throws ModelException   if the row couldn't selected.
     */
    private void correctSelectedRow() throws ModelException
    {
        if (ignoreCorrectSelectedRow)
        {
            return;
        }
        
        ArrayUtil<IDataRow> auCurrentDataRows = getStoredSelection();
        
        if (currentSelectionMode == null)
        {
            currentSelectionMode = iSelectionMode;
        }
        
        if (currentSelectionMode == SelectionMode.DESELECTED)
        {
            setSelectedRowInternal(-1);
        }
        else if ((currentSelectionMode == SelectionMode.CURRENT_ROW
                    || currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED
                    || currentSelectionMode == SelectionMode.CURRENT_ROW_SETFILTER
                    || currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
                && auCurrentDataRows != null && auCurrentDataRows.size() > 0)
        {
            if (auCurrentDataRows.size() > 0 && auCurrentDataRows.get(0) == this)
            {
                // if the databook was deselected the reload function stores this to identify, that it was deselected.
                setSelectedRowInternal(-1);
            }
            else if (auCurrentDataRows.size() == 1 && auCurrentDataRows.get(0) == additionalDataRow)
            {
                if (additionalDataRowVisible)
                {
                    setSelectedRowInternal(0);
                }
                else
                {
                    setSelectedRowInternal(-1);
                }
            }
            else
            {
                // find the same row by StoredSelection
                String[] asPKColumns = rdRowDefinition.getPrimaryKeyColumnNames();
                if (asPKColumns == null)
                {
                    asPKColumns = rdRowDefinition.getColumnNames();
                }
                
                if (isSelfJoined() && auCurrentDataRows.size() > 0)
                {
                    setTreePath(null);
                }
                int i = 0;
                IDataRow drCompare = getDataRow(i);
                IDataRow drCurrent;
                for (int j = auCurrentDataRows.size() - 1; j >= 0; j--)
                {
                    drCurrent = auCurrentDataRows.get(j);
                    while (drCompare != null
                            && !drCurrent.equals(drCompare, asPKColumns))
                    {
                        i++;
                        drCompare = getDataRow(i);
                    }
                    
                    if (drCompare == null)
                    {
                        j = -1; // Not found, so stop searching in case of self joined data book.
                        
                        if (currentSelectionMode == SelectionMode.CURRENT_ROW
                                || currentSelectionMode == SelectionMode.CURRENT_ROW_SETFILTER)
                        {
                            setSelectedRowInternal(0);
                        }
                        else
                        {
                            setSelectedRowInternal(-1);
                        }
                    }
                    else
                    {
                        if (isSelfJoined() && j - 1 >= 0)
                        {
                            setTreePath(getTreePath().getChildPath(i));
                        }
                        else
                        {
                            setSelectedRowInternal(i);
                        }
                        
                        i = 0;
                        drCompare = getDataRow(i);
                    }
                }
            }
        }
        else if (currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED
                || currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
        {
            setSelectedRowInternal(-1);
        }
        else
        {
            setSelectedRowInternal(0);
        }
        currentSelectionMode = null;
        setStoredSelection(null);
        if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected))
        {
            eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, createDataRow(null),
                    treePath, treePath, -1, getSelectedRow()));
        }
    }

    /**
     * Corrects the selected row after delete on the same or one row index before.
     * if the DESELECTED and the last row is deleted, it deselects.
     * 
     * @throws ModelException if selected row couldn't changed.
     */
    private void correctSelectedRowAfterDelete() throws ModelException
    {
        int iNewRowIndex = iSelectedRowIndex;
        if (additionalDataRowVisible)
        {
            iNewRowIndex++;
        }
        if (iNewRowIndex >= getRowCount())
        {
            if (getSelectionMode() == SelectionMode.DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
            {
                iNewRowIndex = -1;
            }
            else
            {
                iNewRowIndex = getRowCount() - 1;
            }
        }
        setSelectedRowInternal(iNewRowIndex);
    }
        
    /**
     * It synchronize the MemDataBook, at the first access after the (master) change.
     * It gets the corresponding MemDataPage from the Hashtable. If not possible it will
     * be created, or an empty MemDataPage will be filled in when the master has no
     * selected row. It also does the lazy load after the open()/reload call.
     * 
     * @throws ModelException   if the new MemDataPage couldn't determined.
     */
    private void sync() throws ModelException
    {
        // init the CurrentDataPage for a Master DataBook
        if (dpEmptyDataPage == null)
        {
            dpEmptyDataPage = new MemDataPage(this, null);
        }
       
        if (rdMasterReference == null)
        {
            if (dpCurrentDataPage == null)
            {
                ignoreReload = true; // only ignore reload when really syncing
                try
                {
                    dpCurrentDataPage = dpEmptyDataPage;
                    iSelectedRowIndex = -1;
    
                    if (temporaryMasterDataPage != null)
                    {
                        dpCurrentDataPage = temporaryMasterDataPage;
                        temporaryMasterDataPage = null;
                    }
                    else
                    {
                        dpCurrentDataPage  = createDataPage(null);
                    }
                    correctSelectedRow();
                }
                finally
                {
                    ignoreReload = false;
                }
            }
        }
        else if (bMasterChanged)
        {
            bMasterChanged = false;
            
            ignoreReload = true; // only ignore reload when really syncing
            try
            {
                if (dpCurrentDataPage == null)
                {
                    dpCurrentDataPage = dpEmptyDataPage;
                    iSelectedRowIndex = -1;
                    treePath = TreePath.EMPTY;
                }
    
                ReferenceDefinition dbParentReference = isSelfJoined() ? getRootReference() : rdMasterReference;
                if (dbParentReference != null && dbParentReference.getReferencedDataBook().getSelectedRow() < 0)
                {
                    if (dpCurrentDataPage != dpEmptyDataPage) // eventAfterSelected should only occur, if the data page changes.
                    {
                        dpCurrentDataPage = dpEmptyDataPage;
                        iSelectedRowIndex = -1;
                        treePath = TreePath.EMPTY;

                        setSelectedRowInternal(-1);
                        currentSelectionMode = null;
                        setStoredSelection(null);
                        
                        if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected))
                        {
                            eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, createDataRow(null),
                                    treePath, treePath, -1, getSelectedRow()));
                        }
                    }
                }
                else
                {
                    if (isSelfJoined())
                    {
                        if (dpCurrentDataPage != dpEmptyDataPage
                                && dbParentReference != null
                                && !getMasterDataRowFromRootDataRow(dbParentReference.getReferencedDataBook()).equals(
                                        dpCurrentDataPage.getMasterDataRow(), saTreeRootMasterColumnNames))
                        {
                            treePath = TreePath.EMPTY;
                        }

                        IDataPage dpNewCurrentDataPage = getDataPage(treePath);
    
                        if (dpNewCurrentDataPage != dpCurrentDataPage)
                        {
                            iSelectedRowIndex = -1;
                            treePath = TreePath.EMPTY;
                            dpCurrentDataPage = (MemDataPage)dpNewCurrentDataPage;
        
                            correctSelectedRow();
                        }
                    }
                    else
                    {
                        MemDataPage dpNewCurrentDataPage = getDataPageIntern(rdMasterReference.getReferencedDataBook());
                        
                        // different master row, so change the current DataPage
                        if (dpNewCurrentDataPage != dpCurrentDataPage)
                        {
                            dpCurrentDataPage = dpNewCurrentDataPage;
                            iSelectedRowIndex = -1;
                            treePath = TreePath.EMPTY;
    
                            correctSelectedRow();
                        }
                    }
                }
            }
            finally
            {
                ignoreReload = false;
            }
        }
    }

    /**
     * It will be called, when the MemDataBook will be completely refreshed.
     * In the MemDataBook, it does nothing, because all is in memory and not restoreable.
     * Derived classes maybe want to clear the memory.
     * 
     * @throws ModelException   if an ModelExcpetion happen during refresh
     */
    protected void executeRefresh()  throws ModelException
    {
        clearFilterSortInMemDataPages();
    }
    
    /**
     * It will be called, when the MemDataBook will be completely refreshed.
     * In the MemDataBook, it does nothing, because all is in memory and not restoreable.
     * Derived classes maybe want to clear the memory.
     * 
     * @throws ModelException   if an ModelExcpetion happen during refresh
     */
    protected void executeRefreshDataPage()  throws ModelException
    {
        clearFilterSortInCurrentMemDataPage();
    }
    
    /**
     * It clears the AbstractStorage of the MemDataBook. Derived classed maybe need it.
     * 
     * @throws ModelException   if an ModelExcpetion happen during refresh
     */
    protected void clear() throws ModelException
    {
        dpCurrentDataPage         = null;
        temporaryMasterDataPage   = null;
        bMasterChanged = true;
        htDataPagesCache.clear();
        changeCounter = 0;
        iSelectedRowIndex         = -1;
        oaStorage                 = null;

        invokeRepaintListeners = true; // ensure the event is published to controls
    }
    
    /**
     * It clears the AbstractStorage of the MemDataBook. Derived classed maybe need it.
     * 
     * @throws ModelException   if an ModelExcpetion happen during refresh
     */
    protected void clearCurrentDataPage() throws ModelException
    {
        if (!isOutOfSync())
        {
            if (rdMasterReference != null)
            {
                MemDataPage mdp = htDataPagesCache.remove(getMasterDataRow());
                if (mdp.hasChanges())
                {
                    changeCounter--;
                }
                
                bMasterChanged = true;
            }
            else
            {
                changeCounter = 0;
            }
            
            dpCurrentDataPage         = null;
            temporaryMasterDataPage   = null;

            invokeRepaintListeners = true; // ensure the event is published to controls
        }
    }
    
    /**
     * Sets the selected row with throwing any changed events.
     * 
     * @param pDataRowIndex     the new row index to use.
     * @throws ModelException   if new row couldn't determined or selected.
     */
    protected void setSelectedRowInternal(int pDataRowIndex) throws ModelException
    {
        if (additionalDataRowVisible)
        {
            pDataRowIndex--;
        }
        
        try
        {
            if (pDataRowIndex < 0)
            {
                oaStorage = null;
            }
            else
            {
                oaStorage = dpCurrentDataPage.getDataRowStorage(pDataRowIndex);
            }
        }
        finally
        {
            if (oaStorage != null)
            {
                iSelectedRowIndex = pDataRowIndex;
                
                calculateRow();
            }
            else
            {
                if (additionalDataRowVisible && pDataRowIndex == -1)
                {
                    oaStorage = getAdditionalDataRow().oaStorage;
                    iSelectedRowIndex = -1;
                }
                else
                {
                    if (additionalDataRowVisible)
                    {
                        iSelectedRowIndex = -2;
                    }
                    else
                    {
                        iSelectedRowIndex = -1;
                    }
                }
            }

            invokeMasterChangedDetailsListeners();
        }
    }
    
    /**
     * Returns the next unique ID over all DataBooks.
     * 
     * @return the next unique ID over all DataBooks.
     */
    static int getNextUID()
    {
        iUID++;
        return iUID;
    }
    
    /**
     * It inserts a new before or after the selected row and returns the row index where it is
     * inserted.
     * 
     * @throws ModelException   if a new row couldn't inserted.
     */
    private void insertInternal() throws ModelException
    {
        setInserting();
        setUID(Integer.valueOf(MemDataBook.getNextUID()));
        setDefaultValues();
        
        // insert new Row in current DataPage
        changeCounter += dpCurrentDataPage.insert(iSelectedRowIndex, this);
        
        calculateRow();
        
        // set master in master, that the details changed.
        setDetailsChangedInMasterBook();
                                
        invokeMasterChangedDetailsListeners();
    }
    
    /**
     * It deletes the current selected row and returns true if the row is removed from
     * the memory or false if the row is only marked as deleting. The first case happens if
     * the row is inserting and not stored, then it will be removed.
     * 
     * @return true if the row is removed from
     * the memory or false if the row is only marked as deleting.
     * @throws ModelException   if the delete operation fails.
     */
    private boolean deleteInternal() throws ModelException
    {
        boolean bResult = isInserting();
        if (bResult)
        {
            changeCounter += dpCurrentDataPage.delete(iSelectedRowIndex);
        }
        else
        {
            setDeleting();
            changeCounter += dpCurrentDataPage.setDataRow(iSelectedRowIndex, this);
            
            calculateRow();

            setDetailsChangedInMasterBook();
            
            notifyRepaintControls();
        }
        
        return bResult;
    }

    /**
     * It sets in the master IDataBook, that this detail has changed with the notifyDetailChanged()
     * method.
     *
     * @throws ModelException       if the DetailChanged couldn't work.
     */
    private void setDetailsChangedInMasterBook() throws ModelException
    {
        if (rdMasterReference != null)
        {
            boolean isChanged = isChanged() || isDetailChanged();
            int[] changedDataRows = dpCurrentDataPage.getChangedRows();
            
            if ((!isChanged || changedDataRows.length > 1)
                    && (isChanged || changedDataRows.length > 0))
            {
                return;
            }
            
            TreePath tpCurrentTreePath = treePath;
            
            // if selfjoined and not the root node, then set in all parent rows DetailChanged!
            if (isSelfJoined() && tpCurrentTreePath.length() > 0)
            {
                // set the detail change state correct.
                boolean bChanged = changedDataRows.length > 0;
                
                while (tpCurrentTreePath.length() > 0)
                {
                    MemDataPage dpParent = (MemDataPage)getDataPage(tpCurrentTreePath.getParentPath());
                    int selectedRowInParent = tpCurrentTreePath.getLast();
                    
                    if (dpParent != null)
                    {
                        ChangeableDataRow dr = (ChangeableDataRow)dpParent.getDataRow(selectedRowInParent);
                        
                        if (dr != null && auDetailDataBooks != null)
                        {
                            for (int i = auDetailDataBooks.size() - 1; !bChanged && i >= 0; i--)
                            {
                                IDataBook detail = auDetailDataBooks.get(i).get();
                                if (detail != null)
                                {
                                    int[] iChanges;
                                    if (detail.isSelfJoined())
                                    {
                                        iChanges = detail.getDataPage(dr, TreePath.EMPTY).getChangedRows();
                                    }
                                    else
                                    {
                                        iChanges = detail.getDataPage(dr).getChangedRows();
                                    }
                                    
                                    if (iChanges.length > 0)
                                    {
                                        bChanged = true;
                                    }
                                }
                            }
                        }
                        
                        if (dr != null && bChanged != dr.isDetailChanged())
                        {
                            dr.setDetailChanged(bChanged);
                            changeCounter += dpParent.setDataRow(selectedRowInParent, dr);
    
                            tpCurrentTreePath = tpCurrentTreePath.getParentPath();
                            
                            bChanged = dpParent.getChangedRows().length > 0;
                        }
                        else
                        {
                            tpCurrentTreePath = TreePath.EMPTY;
                        }
                    }
                    else
                    {
                        tpCurrentTreePath = tpCurrentTreePath.getParentPath();
                    }
                }
            }

            // inform master of the change
            ReferenceDefinition dbParentReference = isSelfJoined() ? getRootReference() : rdMasterReference;

            if (dbParentReference != null)
            {
                IDataBook dbMaster = dbParentReference.getReferencedDataBook();
                
                if (!isChanged || (isChanged && !dbMaster.isDetailChanged()))
                {
                    dbMaster.notifyDetailChanged();
                }
            }
        }
    }
    
    /**
     * After a insert of a new detail row, the master reference columns will be copied
     * from the master to this detail row.
     * 
     * @param pOnlyNull         if true, it will only be copied if the master columns == null.
     * @throws ModelException   if the values couldn't copied
     */
    private void copyMasterColumnsToCurrentDetail(boolean pOnlyNull) throws ModelException
    {
        if (rdMasterReference != null)
        {
            Object[] oaDetailValues = getValues(rdMasterReference.getColumnNames());
            boolean bNull = true;
            if (pOnlyNull)
            {
                if (oaDetailValues != null)
                {
                    for (int i = 0, iSize = oaDetailValues.length; bNull && i < iSize; i++)
                    {
                        if (oaDetailValues[i] != null)
                        {
                            bNull = false;
                        }
                    }
                }
            }
            
            // just copy it, if the master columns in detail are null.
            if (!pOnlyNull || (pOnlyNull && bNull))
            {
                Object[] oaMasterValues = dpCurrentDataPage.getMasterDataRow().getValues(rdMasterReference.getReferencedColumnNames());
                setValuesIntern(rdMasterReference.getColumnNames(), oaMasterValues);
                
                if (isSelfJoined() && rdTreeRootReference != null)
                {
                    oaMasterValues = rdTreeRootReference.getReferencedDataBook().getValues(rdTreeRootReference.getReferencedColumnNames());
                    setValuesIntern(rdTreeRootReference.getColumnNames(), oaMasterValues);
                }
            }
        }
    }
    
    /**
     * Its saves all required changes, if the write back isolation level is DATAROW.
     * It determines the root IDataBook and call saveDetails().
     * 
     * @param pExcludeDataBook  the IDataBook to exclude in the save process
     * @throws ModelException   if the changes couldn't saved.
     */
    private void saveDataRowLevel(IDataBook pExcludeDataBook) throws ModelException
    {
        // get Root Master first
        IDataBook dbRoot = this;
        ReferenceDefinition dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();

        // if this databook has WritebackIsolationLevel DATA_ROW, it has to ensure that all Details and all Masters
        // with WritebackIsolationLevel DATA_ROW are saved too, because in DATA_ROW level only one datarow over all
        // should be edited.
        // if this databook has WritebackIsolationLevel DATA_SOURCE, only all details with WritebackIsolationLevel DATA_ROW
        // are saved. The master is explizitly not saved.
        // in general it is guaranteed, if a databook with WritebackIsolationLevel DATA_ROW is saved, all its details,
        // ignoring the WritebackIsolationLevel, are saved.
        while (dbRoot.getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW && dbParentReference != null)
        {
            dbRoot = dbParentReference.getReferencedDataBook();
            
            dbParentReference = dbRoot.isSelfJoined() ? dbRoot.getRootReference() : dbRoot.getMasterReference();
        }

        // then save all details to this root DataBook, except the current DataBook
        if (dbRoot.getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
        {
            ((MemDataBook)dbRoot).saveDataRowLevelDetails(pExcludeDataBook);
        }
    }
    
    /**
     * It saves all required changes, if the write back isolation level is DATA_ROW.
     * It searches the current row and then hierarchical down over all details to the last detail if there is a
     * data book with WriteBackIsolationLevel DATA_ROW.
     * 
     * @param pExcludeDataBook  the IDataBook to exclude in the save process
     * @throws ModelException   if the changes couldn't saved.
     */
    private void saveDataRowLevelDetails(IDataBook pExcludeDataBook) throws ModelException
    {
        if (getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
        {
            saveDetails(pExcludeDataBook);
        }
        else if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                ((MemDataBook)dataBook).saveDataRowLevelDetails(pExcludeDataBook);
            }
        }
    }
    
    /**
     * It saves all required changes ignoring the write back isolation level.
     * It saves current row and then hierarchical down over all details to the last detail.
     * 
     * @param pExcludeDataBook  the IDataBook to exclude in the save process
     * @throws ModelException   if the changes couldn't saved.
     */
    private void saveDetails(IDataBook pExcludeDataBook) throws ModelException
    {
        if (this != pExcludeDataBook && hasChanges()) // && !isOutOfSync())
        {
            if (getWritebackIsolationLevel() == WriteBackIsolationLevel.DATA_ROW)
            {
                saveSelectedRowIntern();
            }
            else // Maybe saving just the selected row is not enough in any case ;)
            {
                saveDataPage();
            }
            // just save all current selectedRow of all DataBooks in this hierarchy is enough!!!!
            //saveAllRows();
        }
        
        if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                ((MemDataBook)dataBook).saveDetails(pExcludeDataBook);
            }
        }
    }
    
    /**
     * Stores the selection for a correct, reload, resort, ...
     * 
     * @param pSelectionMode    the SelectionMode to use.
     * @throws ModelException if getSelectedRow throw an exception.
     */
    private void handleStoreSelection(SelectionMode pSelectionMode) throws ModelException
    {
        currentSelectionMode = pSelectionMode;
        
        if (currentSelectionMode == SelectionMode.CURRENT_ROW
                || currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED
                || currentSelectionMode == SelectionMode.CURRENT_ROW_SETFILTER
                || currentSelectionMode == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
        {
            if (bIsOpen && !isOutOfSync())
            {
                if (getSelectedRow() < 0) // Mark that the databook is deselected.
                {
                    if (dpCurrentDataPage.getRowCount() == 0)
                    {
                        setStoredSelection(null);
                    }
                    else
                    {
                        setStoredSelection(this);
                    }
                }
                else if (additionalDataRowVisible && getSelectedRow() == 0)
                {
                    setStoredSelection(getAdditionalDataRow());
                }
                else
                {
                    // save current DataRow, PK cols or all
                    String[] asPKColumns = rdRowDefinition.getPrimaryKeyColumnNames();

                    if (asPKColumns == null || asPKColumns.length > 0)
                    {
                        IDataRow dataRow = dpCurrentDataPage.getDataRow(iSelectedRowIndex);
                        if (dataRow != null)
                        {
                            dataRow = dataRow.createDataRow(asPKColumns);
                        }
                        setStoredSelection(dataRow);
                    }
                    else
                    {
                        setStoredSelection(null);
                    }
                }
            }
        }
        else
        {
            setStoredSelection(null);
        }
    }
    
    /**
     * It saves the selectedRow intern and saves all detail rows under it.
     * 
     * @throws ModelException if the save fails.
     */
    private void restoreSelectedRowInternIncludeDetails() throws ModelException
    {
        // save all Details -> selfjoined and detail DataBooks.
        if (isSelfJoined())
        {
            restoreSelfJoinedDetailRows();
        }
        if (auDetailDataBooks != null)
        {
            for (IDataBook dataBook : getDetailDataBooks())
            {
                dataBook.restoreDataPage();
            }
        }

        if (isChanged())
        {
            restoreSelectedRowIntern();
        }
    }
    
    /**
     * Save all Changed rows in the selfjoined details. It saves hierarchy down to the last leeve/row.
     * 
     * @throws ModelException if an Error occur during save to storage.
     */
    private void restoreSelfJoinedDetailRows() throws ModelException
    {
        if (isSelfJoined() && iSelectedRowIndex >= 0 && hasDataPage(this))
        {
            TreePath tpOld        = getTreePath();
            int      iOldRowIndex = iSelectedRowIndex;
            
            if (isDetailChanged())
            {
                // use the current row as master
                setTreePathInternal(tpOld.getChildPath(iSelectedRowIndex));
                restoreDataPageIntern();
                
                //select back to the current row.
                setTreePathInternal(tpOld);
                if (iOldRowIndex != iSelectedRowIndex)
                {
                    setSelectedRow(iOldRowIndex);
                }
            }
        }
    }
    
    /**
     * It restores the changes on the selected row and remove the change state of
     * this row.
     * 
     * @throws ModelException   if the change couldn't restored in memory.
     */
    private void restoreSelectedRowIntern() throws ModelException
    {
        boolean hasBeforeRestore = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRestore);
        boolean hasAfterRestore = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRestore);
        boolean hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
        IDataRow drOriginalDataRow = hasBeforeRestore || hasAfterRestore || hasAfterRowSelected ? createDataRow(null) : null;
        int oldSelectedRow = getSelectedRow();
        int newSelectedRow = oldSelectedRow;
        if (isInserting() && iSelectedRowIndex == dpCurrentDataPage.getRowCount() - 1)
        {
            if (getSelectionMode() == SelectionMode.DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
            {
                newSelectedRow = -1;
            }
            else
            {
                newSelectedRow = getRowCount() - 1;
            }
        }

        if (hasBeforeRestore)
        {
            eventBeforeRestore.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_RESTORE, drOriginalDataRow,
                    treePath, treePath, oldSelectedRow, newSelectedRow));

            hasAfterRestore = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRestore);
            hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
        }

        if (isInserting())
        {
            if (isSelfJoined() && hasDataPage(this))
            {
                if (treePath != null)
                {
                    removeDataPage(null, treePath.getChildPath(getSelectedRow()));
                }
                else
                {
                    removeDataPage(null, null);
                }
            }
            if (auDetailDataBooks != null)
            {
                for (IDataBook dataBook : getDetailDataBooks())
                {
                    dataBook.removeDataPage(this, null);
                }
            }
            
            changeCounter += dpCurrentDataPage.delete(iSelectedRowIndex);
            
            correctSelectedRowAfterDelete();

//          setSelectedRowInternal(iSelectedRowIndex - 1);
            setDetailsChangedInMasterBook();
            
            if (hasAfterRestore)
            {
                eventAfterRestore.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RESTORE, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));

                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            if (hasAfterRowSelected)
            {
                eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));
            }
        }
        else
        {
            restore();
            changeCounter += dpCurrentDataPage.setDataRow(iSelectedRowIndex, this);
    
            setDetailsChangedInMasterBook();
            
            if (hasAfterRestore)
            {
                eventAfterRestore.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RESTORE, drOriginalDataRow,
                        treePath, treePath, oldSelectedRow, newSelectedRow));
            }
        }
    }
    
    /**
     * Returns a new {@link SortDefinition} with only the {@link ColumnDefinition#isSortable()} columns
     * from the given {@link SortDefinition}.
     * 
     * @param pSortDefinition the {@link SortDefinition} to process.
     * @return a new {@link SortDefinition} with only {@link ColumnDefinition#isSortable()} columns.
     *         {@code null} if no columns remained or if the given {@link SortDefinition} was {@code null}
     *         or if the this databook is currently not open.
     * @throws ModelException if accessing the column information failed.
     */
    protected SortDefinition keepSortableColumns(SortDefinition pSortDefinition) throws ModelException
    {
        if (pSortDefinition == null || !bIsOpen)
        {
            return null;
        }
        
        String[] sortableColumns = pSortDefinition.getColumns();
        boolean[] sortableColumnsOrder = pSortDefinition.isAscending();
        
        for (int index = sortableColumns.length - 1; index >= 0; index--)
        {
            String columnName = sortableColumns[index];
            ColumnDefinition columnDefinition = getRowDefinition().getColumnDefinition(columnName);
            
            if (columnDefinition == null || !columnDefinition.isSortable())
            {
                sortableColumns = ArrayUtil.remove(sortableColumns, index);
                sortableColumnsOrder = ArrayUtil.remove(sortableColumnsOrder, index);
            }
        }
        
        if (sortableColumns.length == 0)
        {
            return null;
        }
        
        return new SortDefinition(sortableColumns, sortableColumnsOrder);
    }
    
    /**
     * It stores the changes in memory as stored. That means the changes will be made and
     * the change state of the current DataRow (the DataBook it self) will be removed.
     * 
     * @throws ModelException   if a ModelException occurs.
     */
    protected void store() throws ModelException
    {
        if (isDeleting())
        {
            changeCounter += dpCurrentDataPage.delete(iSelectedRowIndex);
        }
        else
        {
            super.store();
            changeCounter += dpCurrentDataPage.setDataRow(iSelectedRowIndex, this);
            
            setDetailsChangedInMasterBook();
        }
    }

    /**
     * It clears all mem filter sorts in the MemDataPages.
     * 
     * @throws ModelException if a ModelException occurs.
     */
    protected void clearFilterSortInMemDataPages() throws ModelException
    {
        if (rdMasterReference != null)
        {
            Enumeration<MemDataPage> enumDataPages = htDataPagesCache.elements();
            while (enumDataPages.hasMoreElements())
            {
                enumDataPages.nextElement().clear();
            }
            
            dpCurrentDataPage = null;
            bMasterChanged = true;
            
            invokeRepaintListeners = true; // ensure the event is published to controls
        }
        else
        {
            if (!isOutOfSync())
            {
                dpCurrentDataPage.clear();

                // prevent immediate fetch, ensure, that the current data page is set in next sync again, and the correct row is selected.
                temporaryMasterDataPage = dpCurrentDataPage;
                
                dpCurrentDataPage = null;

                invokeRepaintListeners = true; // ensure the event is published to controls
            }
        }
    }

    /**
     * It clears all mem filter sorts in the MemDataPages.
     * 
     * @throws ModelException if a ModelException occurs.
     */
    protected void clearFilterSortInCurrentMemDataPage() throws ModelException
    {
        if (!isOutOfSync())
        {
            dpCurrentDataPage.clear();

            // prevent immediate fetch, ensure, that the current data page is set in next sync again, and the correct row is selected.
            temporaryMasterDataPage = dpCurrentDataPage;
            
            dpCurrentDataPage = null;
            bMasterChanged = true;
            
            invokeRepaintListeners = true; // ensure the event is published to controls
        }
    }

    /**
     * Sets the databook readonly but does not automatically save.
     * 
     * @param pReadOnly <code>true</code> to set the databook readonly, <code>false</code> otherwise
     */
    public void setReadOnlyWithoutSave(boolean pReadOnly)
    {
        synchronized (rootDataBook)
        {
            bReadOnly = pReadOnly;
            
            notifyRepaintControls();
        }
    }
    
    /**
     * Returns the master row only with the primary key columns - like defined in
     * the getMasterReference().getReferencedColumns() created form a root row.
     * 
     * @param pRootDataRow  the full/complete master IDataRow to use.
     * @return internal the master row only with the primary key columns - like defined in
     * the getMasterReference().getReferencedColumns().
     * @throws ModelException   if the master row couldn't created.
     */
    private IDataRow getMasterDataRowFromRootDataRow(IDataRow pRootDataRow) throws ModelException
    {
        ChangeableDataRow masterDataRow =
                new ChangeableDataRow(rdMasterReference.getReferencedDataBook().getRowDefinition().createRowDefinition(rdMasterReference.getReferencedColumnNames()));
        
        if (rdTreeRootReference != null && pRootDataRow != null)
        {
            masterDataRow.setValues(saTreeRootMasterColumnNames, pRootDataRow.getValues(rdTreeRootReference.getReferencedColumnNames()));
            masterDataRow.setUID(getUID(pRootDataRow));
        }
        
        return masterDataRow;
    }
    /**
     * Sets the TreePath internal. see setTreePath(...)
     * It doesn't call saveDataRowLevel(null), invokeSaveEditingControls()!
     * 
     * @param pTreePath         the tree path to set.
     * @throws ModelException   if the TreePath is invalid
     */
    private void setTreePathInternal(TreePath pTreePath) throws ModelException
    {
        if (!treePath.equals(pTreePath) || bMasterChanged)
        {
            // sync has to be after invokeSaveEditingControls, because events on editors could cause an empty dbCurrentDataPage
            // therefore an sync is needed!
            sync();
            
            if (pTreePath == null)
            {
                pTreePath = TreePath.EMPTY;
            }
            boolean hasBeforeRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventBeforeRowSelected);
            boolean hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            IDataRow drOriginalDataRow = hasBeforeRowSelected || hasAfterRowSelected ? createDataRow(null) : null;
            TreePath oldTreePath = treePath;
            TreePath newTreePath = pTreePath;
            int oldSelectedRow = getSelectedRow();
            int newSelectedRow;
            if (getSelectionMode() == SelectionMode.DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED
                    || getSelectionMode() == SelectionMode.CURRENT_ROW_DESELECTED_SETFILTER)
            {
                newSelectedRow = -1;
            }
            else
            {
                newSelectedRow = 0;
            }

            if (hasBeforeRowSelected)
            {
                eventBeforeRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.BEFORE_ROW_SELECTED, drOriginalDataRow,
                        oldTreePath, newTreePath, oldSelectedRow, newSelectedRow));
                
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }

            treePath = pTreePath;

            dpCurrentDataPage = (MemDataPage)getDataPage(treePath);
            if (dpCurrentDataPage == null)
            {
                treePath = TreePath.EMPTY;
                newTreePath = treePath;
                
                dpCurrentDataPage = (MemDataPage)getDataPage(treePath);
                if (dpCurrentDataPage == null)
                {
                    dpCurrentDataPage = dpEmptyDataPage;
                }
            }

            setSelectedRowInternal(newSelectedRow);
            newSelectedRow = getSelectedRow();

            if (DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterReload))
            {
                eventAfterReload.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_RELOAD));
                
                hasAfterRowSelected = DataBookHandler.isDispatchable(bDispatchEventsEnabled, eventAfterRowSelected);
            }
            if (hasAfterRowSelected)
            {
                eventAfterRowSelected.dispatchEvent(bDispatchEventsEnabled, new DataBookEvent(this, ChangedType.AFTER_ROW_SELECTED, drOriginalDataRow,
                        oldTreePath, newTreePath, oldSelectedRow, newSelectedRow));
            }
        }
    }
    
    /**
     * This function is used for rehashing data pages after insert of the master row.
     * If it is true, the page is dropped, and therefore refetched, if needed.
     * @return true, the page is dropped, and therefore refetched, if needed.
     */
    protected boolean isDataPageRefetchPossible()
    {
        return false;
    }
    
}   // MemDataBook
