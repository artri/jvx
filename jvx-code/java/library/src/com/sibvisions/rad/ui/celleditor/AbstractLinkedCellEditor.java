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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.model.ColumnView;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.condition.And;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.OperatorCondition;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IDimension;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.celleditor.IStyledCellEditor;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link AbstractLinkedCellEditor} is an {@link ILinkedCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractLinkedCellEditor extends AbstractComboCellEditor 
                                               implements ILinkedCellEditor
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** True, if header should be visible depending of column number. */
    private transient boolean autoTableHeaderVisibility = true;
    
    /** The additional condition. */
    private transient ICondition additionalCondition;

    /** The {@link ColumnView}. */
    private transient ColumnView columnView;
    
    /** The name of the display referenced column. */
    protected transient String displayReferencedColumnName = null;
    
    /** The display concat mask. */
    protected transient String displayConcatMask = null;
    
    /** The link reference. */
    protected transient ReferenceDefinition linkReference;
    
    /** The size used for the popup. */
    protected transient IDimension popupSize;
    
    /** The {@link ColumnMapping}. */
    private transient ColumnMapping searchColumnMapping;
    
    /** If the text should be searched anywhere inside a column. */
    private transient boolean searchTextAnywhere = true;
    
    /** If the text should be searched in all visible table columns. */
    private transient boolean searchInAllTableColumns = false;
    
    /** If the values should be sorted by column name. */
    private transient boolean sortByColumnName;
    
    /** If the table header should be visible. */
    private transient boolean tableHeaderVisible = false;
    
    /** If the table should be read-only. */
    private transient boolean tableReadOnly = true;
    
    /** If only values from the table are allowed. */
    private transient boolean validationEnabled = true;
    
    /** Columns that should not be cleared, in case of non validation or dependend link cell editors. */
    private transient String[] doNotClearColumnNames = null;
    
    /** The index value cache is for <code>MemDataBook</code>, to detect changed values. */
    private transient Map<String, Integer> indexValueCache = new HashMap<String, Integer>();
    
    /** The display values will be cached in this {@link Map} so that the conversion to a String is only done once in a render cycle. */
    private transient Map<String, String> displayValueCache = new HashMap<String, String>();

    /** The last column, with which was searched. */
    private transient String lastColumnForSearch = null;
    
    /** The last display column. */
    private transient String lastDisplayColumn = null;
    
    /** The last data page, in which was searched. */
    private transient WeakReference<IDataPage> lastPageForSearch = null;
    
    /** A proper translated example, to check, if translation map changed. */
    private transient String checkTranslationDisplayValue = null;
    
    /** A proper translated example, to check, if translation map changed. */
    private transient IDataRow checkTranslationDataRow = null;
    
    /** The last search row, which is used when building the display value cache. */
    private transient int lastSearchRow = 0;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link AbstractLinkedCellEditor}.
     */
    protected AbstractLinkedCellEditor()
    {
        super();
        
        setHorizontalAlignment(ALIGN_DEFAULT);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public ICondition getAdditionalCondition()
    {
        return additionalCondition;
    }
    
    /**
     * {@inheritDoc}
     */
    public ColumnView getColumnView()
    {
        return columnView;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayReferencedColumnName()
    {
        return displayReferencedColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayConcatMask()
    {
        return displayConcatMask;
    }
    
    /**
     * {@inheritDoc}
     */
    public ReferenceDefinition getLinkReference()
    {
        return linkReference;
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] getDoNotClearColumnNames()
    {
        return doNotClearColumnNames;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDoNotClearColumnNames(String... pDoNotClearColumnNames)
    {
        doNotClearColumnNames = pDoNotClearColumnNames;
    }
    
    /**
     * {@inheritDoc}
     */
    public IDimension getPopupSize()
    {
        return popupSize;
    }
    
    /**
     * {@inheritDoc}
     */
    public ColumnMapping getSearchColumnMapping()
    {
        return searchColumnMapping;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isSearchTextAnywhere()
    {
        return searchTextAnywhere;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isSearchInAllTableColumns()
    {
        return searchInAllTableColumns;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isSortByColumnName()
    {
        return sortByColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isAutoTableHeaderVisibility()
    {
        return autoTableHeaderVisibility;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTableHeaderVisible()
    {
        return tableHeaderVisible;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTableReadonly()
    {
        return tableReadOnly;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isValidationEnabled()
    {
        return validationEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setAdditionalCondition(ICondition pCondition)
    {
        additionalCondition = pCondition;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setColumnView(ColumnView pColumnView)
    {
        columnView = pColumnView;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDisplayReferencedColumnName(String pDisplayReferencedColumnName)
    {
        displayReferencedColumnName = pDisplayReferencedColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setDisplayConcatMask(String pDisplayConcatMask)
    {
        displayConcatMask = pDisplayConcatMask;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLinkReference(ReferenceDefinition pReferenceDefinition)
    {
        linkReference = pReferenceDefinition;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPopupSize(IDimension pPopupSize)
    {
        popupSize = pPopupSize;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSearchColumnMapping(ColumnMapping pSearchColumnNames)
    {
        searchColumnMapping = pSearchColumnNames;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSearchTextAnywhere(boolean pSearchTextAnywhere)
    {
        searchTextAnywhere = pSearchTextAnywhere;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSearchInAllTableColumns(boolean pSearchInAllTableColumns)
    {
        searchInAllTableColumns = pSearchInAllTableColumns;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSortByColumnName(boolean pSortByColumnName)
    {
        sortByColumnName = pSortByColumnName;
    }
    
    /**
     * Does nothing, the header of a table can't be hidden.
     * 
     * @param pTableHeaderVisible ignored.
     */
    public void setTableHeaderVisible(boolean pTableHeaderVisible)
    {
        autoTableHeaderVisibility = false;
        
        tableHeaderVisible = pTableHeaderVisible;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTableReadonly(boolean pTableReadonly)
    {
        tableReadOnly = pTableReadonly;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setValidationEnabled(boolean pValidationEnabled)
    {
        validationEnabled = pValidationEnabled;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectCellEditor()
    {
        return false;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the default horizontal alignment based on data type.
     * If default alignment is unknown, ALIGN_LEFT is returned.
     * 
     * @param pDataRow the data row
     * @param pColumnName the column name
     * @return the default horizontal alignment
     */
    public int getDefaultHorizontalAlignment(IDataRow pDataRow, String pColumnName)
    {
        int hAlignment = getHorizontalAlignment();
        if (hAlignment == ALIGN_DEFAULT)
        {
            try
            {
                IDataType dataType = pDataRow.getRowDefinition().getColumnDefinition(pColumnName).getDataType();
                ICellEditor editor = dataType.getCellEditor();
                if (editor != this && editor instanceof IStyledCellEditor)
                {
                    hAlignment = ((IStyledCellEditor)editor).getHorizontalAlignment();
                }
                
                if (hAlignment == ALIGN_DEFAULT && displayReferencedColumnName == null && displayConcatMask == null)
                {
                    editor = UICellEditor.getDefaultCellEditor(dataType.getTypeClass());
                    
                    if (editor != this && editor instanceof IStyledCellEditor)
                    {
                        hAlignment = ((IStyledCellEditor)editor).getHorizontalAlignment();
                    }
                }
            }
            catch (Exception pException)
            {
                // Ignore
            }
            if (hAlignment == ALIGN_DEFAULT)
            {
                hAlignment = ALIGN_LEFT;
            }
        }

        return hAlignment;
    }

    /**
     * Gets the correct display value for the given row and column.
     * 
     * @param pDataRow the data row
     * @param pColumnName the column
     * @return the display value.
     * @throws ModelException if it fails.
     */
    protected String getDisplayValue(IDataRow pDataRow, String pColumnName) throws ModelException
    {
        Object value = pDataRow.getValue(pColumnName);
    
        if ((displayReferencedColumnName != null || (displayConcatMask != null && value != null)) 
                && linkReference != null
                && !(pDataRow instanceof IDataBook && ((IDataBook)pDataRow).getSelectedRow() < 0))
        {
            IDataBook refDataBook = linkReference.getReferencedDataBook();
            IRowDefinition refRowDef = refDataBook.getRowDefinition();
            String refColumn = linkReference.getReferencedColumnName(pColumnName);

            Map<String, Object> searchValues = getAllSearchColumnNamesAndValues(pDataRow);
            int searchValuesSize = searchValues.size();
            String[] columns = searchValues.keySet().toArray(new String[searchValuesSize + 1]);
            Object[] values = searchValues.values().toArray(new Object[searchValuesSize + 1]);
            
            columns[searchValuesSize] = refColumn;
            values[searchValuesSize] = value;
            
            boolean hasCondition = searchValuesSize > 0;
            
            String key = generateKey(values, refRowDef, columns);
            String singleKey = hasCondition ? generateSingleKey(value, refRowDef, refColumn) : null;
            
            String displayValue = displayValueCache.get(key);
            String displaySingleValue = hasCondition ? displayValueCache.get(singleKey) : null;

            IDataPage page = refDataBook.getDataPage();
            IDataRow refDataRow = null;
            String refKey = null;
            String refSingleKey = null;
            
            boolean isMemDataBook = refDataBook.getClass() == MemDataBook.class;

            Integer index = null;
            Integer singleIndex = null;

            ICondition originalFilter = null;
            IControl[] controls = null;
            int selectedRow = -1;
            if (isMemDataBook)
            {
                originalFilter = refDataBook.getFilter();
                if (originalFilter != null) // ensure, that the filter is null, otherwise value will not be found
                {
                    controls = refDataBook.getControls();
                    for (IControl control : controls)
                    {
                        refDataBook.removeControl(control);
                    }
                    
                    selectedRow = refDataBook.getSelectedRow();
                    refDataBook.setFilter(null);
                }
                index = indexValueCache.get(key);
                if (index != null)
                {
                    refDataRow = refDataBook.getDataRow(index.intValue());
                    if (refDataRow != null)
                    {
                        refKey = generateKey(refDataRow.getValues(columns), null, null);
                    }
                }
                else if (hasCondition)
                {
                    singleIndex = indexValueCache.get(singleKey);
                    if (singleIndex != null)
                    {
                        refDataRow = refDataBook.getDataRow(singleIndex.intValue());
                        if (refDataRow != null)
                        {
                            refSingleKey = generateSingleKey(refDataRow.getValue(refColumn), null, null);
                        }
                    }
                }
            }
            
            if (lastPageForSearch == null 
                    || lastPageForSearch.get() != page
                    || !CommonUtil.equals(refColumn, lastColumnForSearch)
                    || !CommonUtil.equals(displayReferencedColumnName, lastDisplayColumn)
                    || (checkTranslationDataRow != null && !CommonUtil.equals(checkTranslationDisplayValue, getDisplayValueFromRow(checkTranslationDataRow)))
                    || (isMemDataBook 
                            && ((index != null 
                                && (refDataRow == null || !CommonUtil.equals(key, refKey) 
                                    || !CommonUtil.equals(displayValue, getDisplayValueFromRow(refDataRow))))
                             || (singleIndex != null
                                && (refDataRow == null || !CommonUtil.equals(singleKey, refSingleKey) 
                                    || !CommonUtil.equals(displaySingleValue, getDisplayValueFromRow(refDataRow)))))))
            {
                indexValueCache.clear();
                displayValueCache.clear();
                
                lastPageForSearch = new WeakReference<IDataPage>(page);
                lastColumnForSearch = refColumn;
                lastDisplayColumn = displayReferencedColumnName;
                
                checkTranslationDisplayValue = null;
                checkTranslationDataRow = null;
                
                lastSearchRow = 0;
                displayValue = null;
            }
            
            if (displayValue == null)
            {
                if (!isMemDataBook)
                {
                    originalFilter = refDataBook.getFilter();
                    if (originalFilter != null) // ensure, that the filter is null, otherwise value will not be found
                    {
                        controls = refDataBook.getControls();
                        for (IControl control : controls)
                        {
                            refDataBook.removeControl(control);
                        }
                        
                        selectedRow = refDataBook.getSelectedRow();
                        refDataBook.setFilter(null);
                    }
                }
                
                IDataRow curDataRow = refDataBook.getDataRow(lastSearchRow);
                
                while (curDataRow != null 
                       && (displayValue == null 
                           || refDataBook.isAllFetched() 
                           || lastSearchRow < refDataBook.getRowCount() - 1)) // step through the data book beginning with the last search row
                {
                    
                    refKey = generateKey(curDataRow.getValues(columns), null, null);
                    
                    String curDisplayValue = getDisplayValueFromRow(curDataRow);
                    
                    displayValueCache.put(refKey, curDisplayValue);
                    if (hasCondition)
                    {
                        refSingleKey = generateSingleKey(curDataRow.getValue(refColumn), null, null);
                        displayValueCache.put(refSingleKey, curDisplayValue);
                    }
                    if (isMemDataBook)
                    {
                        indexValueCache.put(refKey, Integer.valueOf(lastSearchRow));
                        if (hasCondition)
                        {
                            indexValueCache.put(refSingleKey, Integer.valueOf(lastSearchRow));
                        }
                    }
                    else if (checkTranslationDataRow == null && !StringUtil.isEmpty(refKey) && !StringUtil.isEmpty(curDisplayValue))
                    {
                        checkTranslationDisplayValue = curDisplayValue;
                        checkTranslationDataRow = curDataRow;
                    }
                    
                    if (CommonUtil.equals(key, refKey))
                    {
                        displayValue = curDisplayValue;
                    }

                    lastSearchRow++;
                    curDataRow = refDataBook.getDataRow(lastSearchRow);
                }
            }
            if (originalFilter != null) // ensure, that the filter is back, for correct displaying
            {
                refDataBook.setFilter(originalFilter);
                refDataBook.setSelectedRow(selectedRow); // without this, no sync will occur, and endless repaint will start.
                
                for (IControl control : controls)
                {
                    refDataBook.addControl(control);
                }
                
                lastPageForSearch = new WeakReference<IDataPage>(refDataBook.getDataPage()); // ensure, that setting filter here will not discard search
            }
            if (hasCondition && displayValue == null)
            {
                displayValue = displayValueCache.get(singleKey);
            }
            if (displayValue != null)
            {
                return displayValue;
            }
        }

        return pDataRow.getValueAsString(pColumnName);
    }

    /**
     * This update will prevent unnecessary cache clear events, when filter changes during edit.
     */
    public void updateCurrentCachedPage()
    {
        lastPageForSearch = new WeakReference<IDataPage>(linkReference.getReferencedDataBook().getDataPage()); // ensure, that setting filter here will not discard search
    }
    
    /**
     * Gets the display value depending on displayConcatMask or displayReferencedColumnName.
     * @param pDataRow the data row.
     * @return the display value depending on displayConcatMask or displayReferencedColumnName.
     * @throws ModelException the model Exception.
     */
    protected String getDisplayValueFromRow(IDataRow pDataRow) throws ModelException
    {
        if (displayReferencedColumnName == null)
        {
            ColumnView cv = columnView;
            if (cv == null)
            {
                cv = linkReference.getReferencedDataBook().getRowDefinition().getColumnView(ITableControl.class);
            }
            int count = cv.getColumnCount();
            
            StringBuilder result = new StringBuilder();
            int index = displayConcatMask.indexOf('*');
            if (index < 0)
            {
                if (count == 0)
                {
                    return null;
                }
                else
                {
                    result.append(CommonUtil.nvl(pDataRow.getValueAsString(cv.getColumnName(0)), ""));
                    for (int i = 1; i < count; i++)
                    {
                        result.append(displayConcatMask);
                        result.append(CommonUtil.nvl(pDataRow.getValueAsString(cv.getColumnName(i)), ""));
                    }
                }
            }
            else
            {
                int i = 0;
                int start = 0;
                
                while (index >= 0)
                {
                    result.append(displayConcatMask.substring(start, index));
                    
                    if (i < count)
                    {
                        result.append(CommonUtil.nvl(pDataRow.getValueAsString(cv.getColumnName(i)), ""));
                        i++;
                    }
                    else
                    {
                        result.append("");
                    }
                    start = index + 1;
                    index = displayConcatMask.indexOf('*', start);
                }   
                result.append(displayConcatMask.substring(start));
            }
            
            return result.toString();
        }
        else
        {
            return pDataRow.getValueAsString(displayReferencedColumnName);
        }
    }
    
    /**
     * Searches for columns, that should be cleared, if the value of this linked cell editor is cleared, or not valid, 
     * as it does not exist in referenced data book.
     * The clear columns are the columns of link reference, minus the do not clear columns and values of parent linked cell editors.
     * 
     * @param pDataRow the data row
     * @param pColumnName the column name
     * @return the clear columns, that should be cleared, if the value of this linked cell editor is cleared
     * @throws ModelException if it fails.
     */
    protected String[] getClearColumns(IDataRow pDataRow, String pColumnName) throws ModelException
    {
        IRowDefinition rowDef = pDataRow.getRowDefinition();
        String[] allColumns = rowDef.getColumnNames();
        String[] linkColumns = getLinkReference().getColumnNames();
        String[] searchColumns = getAllSearchColumnNames(getSearchColumnMapping(), pDataRow, getAdditionalCondition()); 
        
        ArrayUtil<String> columns = new ArrayUtil<String>();
        columns.addAll(linkColumns);
        
        for (String colName : allColumns)
        {
            ICellEditor ce = rowDef.getColumnDefinition(colName).getDataType().getCellEditor();
            
            if (ce instanceof ILinkedCellEditor)
            {
                ILinkedCellEditor linkCe = (ILinkedCellEditor)ce;
                ReferenceDefinition refDef = linkCe.getLinkReference();
                
                if (refDef != null)
                {
                    String[] refDefCols = refDef.getColumnNames();
                    if (ArrayUtil.intersect(refDefCols, searchColumns).length > 0
                        && !ArrayUtil.contains(refDefCols, pColumnName))
                    {
                        columns.removeAll(refDefCols);
                    }
                }
            }
        }
        
        columns.removeAll(doNotClearColumnNames);

        return columns.toArray(new String[columns.size()]);
    }
    
    /**
     * Searches for additional columns to be cleared on save.
     * The additional clear columns are columns of sub link cell editors depending on this linked cell editor.
     * There values should be cleared by default, if this link cell editor selects a new value.
     * 
     * @param pDataRow the data row.
     * @return additional clear columns.
     * @throws ModelException if it fails.
     */
    protected String[] getAdditionalClearColumns(IDataRow pDataRow) throws ModelException
    {
        ArrayUtil<String> columns = new ArrayUtil<String>();
        
        IRowDefinition rowDef = pDataRow.getRowDefinition();
        String[] allColumns = rowDef.getColumnNames();
        String[] linkColumns = getLinkReference().getColumnNames();
        String[] searchColumns = getAllSearchColumnNames(getSearchColumnMapping(), pDataRow, getAdditionalCondition()); 
        
        for (String colName : allColumns)
        {
            ICellEditor ce = rowDef.getColumnDefinition(colName).getDataType().getCellEditor();
            
            if (ce instanceof ILinkedCellEditor)
            {
                ILinkedCellEditor linkCe = (ILinkedCellEditor)ce;
                ReferenceDefinition refDef = linkCe.getLinkReference();
                String[] searchCols = ArrayUtil.removeAll(
                        getAllSearchColumnNames(linkCe.getSearchColumnMapping(), pDataRow, linkCe.getAdditionalCondition()), searchColumns);
                
                if (refDef != null && searchCols != null
                        && ArrayUtil.intersect(linkColumns, searchCols).length > 0)
                {
                    String[] addCols = ArrayUtil.removeAll(refDef.getColumnNames(), linkColumns);
                            
                    for (String col : addCols)
                    {
                        if (!columns.contains(col))
                        {
                            columns.add(col);
                        }
                    }
                }
            }
        }

        columns.removeAll(doNotClearColumnNames);

        return columns.toArray(new String[columns.size()]);
    }

    /**
     * Gets all search columns using the given data row as filter.
     * 
     * @param pSearchColumnMapping the columnMapping.
     * @param pDataRow the data row.
     * @param pCondition the additional condition.
     * @return the list of all found condition columns.
     */
    protected String[] getAllSearchColumnNames(ColumnMapping pSearchColumnMapping, IDataRow pDataRow, ICondition pCondition)
    {
        ArrayUtil<String> conditionColumns = new ArrayUtil<String>();
        
        if (pSearchColumnMapping != null)
        {
            conditionColumns.addAll(pSearchColumnMapping.getColumnNames());
        }
        
        fillInConditionColumnNames(pCondition, pDataRow, conditionColumns);
        
        int size = conditionColumns.size();
        if (size == 0)
        {
            return null;
        }
        else
        {
            return conditionColumns.toArray(new String[size]);
        }
    }

    /**
     * Searches the condition columns.
     * 
     * @param pCondition the condition.
     * @param pDataRow the datarow.
     * @param pConditionColumns the list of all found condition columns.
     */
    private void fillInConditionColumnNames(ICondition pCondition, IDataRow pDataRow, List<String> pConditionColumns)
    {
        if (pCondition instanceof Equals)
        {
            Equals cond = (Equals)pCondition;
            String colName = cond.getDataRowColumnName();
            if (pDataRow == cond.getDataRow() && !pConditionColumns.contains(colName))
            {
                pConditionColumns.add(colName);
            }
        }
        else if (pCondition instanceof OperatorCondition)
        {
            ICondition[] conditions = ((OperatorCondition)pCondition).getConditions();
            
            for (int i = 0; i < conditions.length; i++)
            {
                fillInConditionColumnNames(conditions[i], pDataRow, pConditionColumns);
            }
        }
    }

    /**
     * Generates a key from the given values.
     * If rowDefinition and column names are not null the value will be converted to the type class configured in row definition.
     *  
     * @param pValue the value to build the key
     * @param pRowDefinition the optional row definition for converting the value
     * @param pColumnName the optional column name for converting the value
     * @return the generated key
     */
    protected String generateSingleKey(Object pValue, IRowDefinition pRowDefinition, String pColumnName)
    {
        if (pValue == null)
        {
            return "";
        }
        else if (pRowDefinition == null || pColumnName == null)
        {
            return String.valueOf(pValue);
        }
        else
        {
            try
            {
                return String.valueOf(pRowDefinition.getColumnDefinition(pColumnName).getDataType().convertToTypeClass(pValue));
            }
            catch (Exception ex)
            {
                return String.valueOf(pValue);
            }
        }
    }

    /**
     * Generates a key from the given values.
     * If rowDefinition and column names are not null the value will be converted to the type class configured in row definition.
     *  
     * @param pValues the values to build the key
     * @param pRowDefinition the optional row definition for converting the values
     * @param pColumnNames the optional column names for converting the values
     * @return the generated key
     */
    protected String generateKey(Object[] pValues, IRowDefinition pRowDefinition, String[] pColumnNames)
    {
        StringBuilder sbKey = new StringBuilder();
        
        for (int i = 0; i < pValues.length; i++)
        {
            if (i > 0)
            {
                sbKey.append('/');
            }
            
            Object val = pValues[i];
            if (val != null)
            {
                if (pRowDefinition == null || pColumnNames == null)
                {
                    sbKey.append(val);
                }
                else
                {
                    try
                    {
                        sbKey.append(pRowDefinition.getColumnDefinition(pColumnNames[i]).getDataType().convertToTypeClass(val));
                    }
                    catch (Exception ex)
                    {
                        sbKey.append(val);
                    }
                }
            }
        }

        return sbKey.toString();
    }
    
    /**
     * Gets all search column names and values for the given row.
     * 
     * @param pDataRow the row to use
     * @return all search column names and values for the given row.
     */
    protected Map<String, Object> getAllSearchColumnNamesAndValues(IDataRow pDataRow)
    {
        Map<String, Object> conditionColumns = new LinkedHashMap<String, Object>();
        
        if (searchColumnMapping != null)
        {
            String[] columnNames = searchColumnMapping.getColumnNames();
            String[] refColumnNames = searchColumnMapping.getReferencedColumnNames();
            for (int i = 0; i < columnNames.length; i++)
            {
                try
                {
                    conditionColumns.put(refColumnNames[i], pDataRow.getValue(columnNames[i]));
                }
                catch (ModelException e)
                {
                    conditionColumns.put(refColumnNames[i], null);
                }
            }
        }
        
        IDataRow refRow;
        if (pDataRow instanceof IDataBook)
        {
            refRow = pDataRow;
        }
        else if (pDataRow instanceof IChangeableDataRow)
        {
            refRow = ((IChangeableDataRow)pDataRow).getDataPage().getDataBook();
        }
        else
        {
            refRow = pDataRow;
        }
        
        fillInConditionReferencedColumnNames(additionalCondition, refRow, pDataRow, conditionColumns);
        
        return conditionColumns;
    }
    
    /**
     * Searches for all condition values and the corresponding column name.
     * 
     * @param pCondition the condition to search
     * @param pRefDataRow the edited data book or row
     * @param pDataRow the current data row
     * @param pConditionColumns the result to fill in
     */
    private void fillInConditionReferencedColumnNames(ICondition pCondition, IDataRow pRefDataRow, IDataRow pDataRow, Map<String, Object> pConditionColumns)
    {
        if (pCondition instanceof Equals)
        {
            Equals cond = (Equals)pCondition;
            String colName = cond.getColumnName();
            if (pRefDataRow == cond.getDataRow())
            {
                try
                {
                    pConditionColumns.put(colName, pDataRow.getValue(cond.getDataRowColumnName()));
                }
                catch (ModelException ex)
                {
                    pConditionColumns.put(colName, null);
                }
            }
            else
            {
                pConditionColumns.put(colName, cond.getValue());
            }
        }
        else if (pCondition instanceof OperatorCondition)
        {
            ICondition[] conditions = ((OperatorCondition)pCondition).getConditions();
            
            for (int i = 0; i < conditions.length; i++)
            {
                fillInConditionReferencedColumnNames(conditions[i], pRefDataRow, pDataRow, pConditionColumns);
            }
        }
    }

    /**
     * Creates a search string.
     * 
     * @param pItem item
     * @return a search string.
     */
    protected String getWildCardString(Object pItem)
    {
        if (isSearchTextAnywhere())
        {
            return "*" + pItem + "*";
        }
        else
        {
            return pItem + "*";
        }
    }
    
    /**
     * Creates a Condition including the search columns.
     * 
     * @param pDataRow   the base data row.
     * @param pCondition the base condition.
     * @return a Condition including the search columns.
     */
    protected ICondition getSearchCondition(IDataRow pDataRow, ICondition pCondition)
    {
        if (additionalCondition != null)
        {
            if (pCondition == null)
            {
                pCondition = new And(additionalCondition);
            }
            else
            {
                pCondition = new And(pCondition, additionalCondition);
            }
        }
        
        if (searchColumnMapping != null)
        {
            String[] searchColumns = searchColumnMapping.getColumnNames();
            String[] referencedSearchColumns = searchColumnMapping.getReferencedColumnNames();
            
            for (int i = 0; i < searchColumns.length; i++)
            {
                ICondition condition = new Equals(pDataRow, searchColumns[i], referencedSearchColumns[i]);
                
                if (pCondition == null)
                {
                    pCondition = condition;
                }
                else
                {
                    pCondition = pCondition.and(condition);
                }
            }
        }

        return pCondition;
    }

    /**
     * Gets the search condition for the input item.
     * 
     * @param pSearchWildCard true, if wildcard search should be done.
     * @param pRelevantSearchColumnName the relevant search column.
     * @param pItem the item to search.
     * @return the search condition for the input item.
     */
    protected ICondition getItemSearchCondition(boolean pSearchWildCard, String pRelevantSearchColumnName, Object pItem)
    {
        Object searchString = pSearchWildCard ? getWildCardString(pItem) : pItem;
        String[] columns = null;
        if (searchInAllTableColumns || displayConcatMask != null)
        {
            if (columnView == null)
            {
                columns = linkReference.getReferencedDataBook().getRowDefinition().getColumnView(ITableControl.class).getColumnNames();
            }
            else
            {
                columns = columnView.getColumnNames();
            }
        }
        if (columns == null || columns.length == 0)
        {
            columns = new String[] {pRelevantSearchColumnName};
        }

        ICondition result = pSearchWildCard ? new LikeIgnoreCase(columns[0], searchString) : new Like(columns[0], pItem);
        
        for (int i = 1; i < columns.length; i++)
        {
            result = result.or(pSearchWildCard ? new LikeIgnoreCase(columns[i], searchString) : new Like(columns[i], pItem));
        }
        
        return result;
    }
    
}   // AbstractLinkedCellEditor
