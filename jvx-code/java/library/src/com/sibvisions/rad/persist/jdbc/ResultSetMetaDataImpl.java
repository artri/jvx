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
 * 29.04.2020 - [HM] - creation
 */
package com.sibvisions.rad.persist.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * The <code>ResultSetMetaDataImpl</code> is a Helper Class to store Own Meta Data.<br>
 *  
 * @author Martin Handsteiner
 */
public class ResultSetMetaDataImpl implements ResultSetMetaData
{
    /** The columns. */
    private ArrayList<ResultSetMetaDataColumn> columns = new ArrayList<ResultSetMetaDataColumn>();

    /**
     * Constructs a ResultSetMetaDataImpl.
     */
    public ResultSetMetaDataImpl()
    {
    }
    
    /**
     * Adds a new column.
     * @param pColumn the column
     */
    public void addColumn(ResultSetMetaDataColumn pColumn)
    {
        columns.add(pColumn);
    }

    /**
     * Removes all columns.
     */
    public void removeAllColumns()
    {
        columns.clear();
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(Class<T> pInterface) throws SQLException
    {
        return (T)this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(Class<?> pInterface) throws SQLException
    {
        return true;
    }

    @Override
    public int getColumnCount() throws SQLException
    {
        return columns.size();
    }

    @Override
    public boolean isAutoIncrement(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isAutoIncrement();
    }

    @Override
    public boolean isCaseSensitive(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isCaseSensitive();
    }

    @Override
    public boolean isSearchable(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isSearchable();
    }

    @Override
    public boolean isCurrency(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isCurrency();
    }

    @Override
    public int isNullable(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isNullable();
    }

    @Override
    public boolean isSigned(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isSigned();
    }

    @Override
    public int getColumnDisplaySize(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnDisplaySize();
    }

    @Override
    public String getColumnLabel(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnLabel();
    }

    @Override
    public String getColumnName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnName();
    }

    @Override
    public String getSchemaName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getSchemaName();
    }

    @Override
    public int getPrecision(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getPrecision();
    }

    @Override
    public int getScale(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getScale();
    }

    @Override
    public String getTableName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getTableName();
    }

    @Override
    public String getCatalogName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getCatalogName();
    }

    @Override
    public int getColumnType(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnType();
    }

    @Override
    public String getColumnTypeName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnTypeName();
    }

    @Override
    public boolean isReadOnly(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isReadOnly();
    }

    @Override
    public boolean isWritable(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isWritable();
    }

    @Override
    public boolean isDefinitelyWritable(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).isDefinitelyWritable();
    }

    @Override
    public String getColumnClassName(int pColumn) throws SQLException
    {
        return columns.get(pColumn - 1).getColumnClassName();
    }
    
} 	// ResultSetMetaDataImpl
