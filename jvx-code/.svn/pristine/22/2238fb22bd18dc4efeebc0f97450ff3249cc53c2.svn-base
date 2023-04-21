/*
 * Copyright 2014 SIB Visions GmbH
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
 * 25.04.2014 - [JR] - creation
 */
package com.sibvisions.rad.model;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.RowDefinition;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.BooleanDataType;
import javax.rad.model.datatype.ObjectDataType;
import javax.rad.model.datatype.TimestampDataType;

import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>DataBookBuilder</code> is a utility class for building/creating pre-configured databooks.
 * 
 * @author René Jahn
 */
public final class DataBookBuilder
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** number value type detected. */
    private boolean bNumber;
    /** string value type detected. */
    private boolean bString;
    /** timestamp/date value type detected. */
    private boolean bTimestamp;
    /** boolean value type detected. */
    private boolean bBoolean;
    /** binary value type detected. */
    private boolean bBinary;
    /** object value type detected. */
    private boolean bObject;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor because <code>DataBookBuilder</code> is a utility
     * class.
     */
    private DataBookBuilder()
    {
        bNumber = false;
        bString = false;
        bTimestamp = false;
        bBoolean = false;
        bBinary = false;
        bObject = false;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Checks the given object type and updates the internal value type detection.
     * 
     * @param pValue the value
     */
    private void checkType(Object pValue)
    {
        if (pValue != null)
        {
            if (pValue instanceof String)
            {
                bString = true;
            }
            else if (pValue instanceof Number)
            {
                bNumber = true;
            }
            else if (pValue instanceof Date)
            {
                bTimestamp = true;
            }
            else if (pValue instanceof Boolean)
            {
                bBoolean = true;
            }
            else if (pValue instanceof byte[]
                     || pValue instanceof InputStream)
            {
                bBinary = true;
            }
            else
            {
                bObject = true;
            }
        }
    }

    /**
     * Creates a column definition based on the current value detection.
     * 
     * @param pColumnName the column name
     * @return the column definition
     */
    private ColumnDefinition createColumnDefinition(String pColumnName)
    {
        //mixed values -> always string
        
        boolean[] ba = new boolean[] {bString, bNumber, bTimestamp, bBoolean, bBinary, bObject};
        
        int iTypeCount = 0;
        
        for (int i = 0; i < ba.length; i++)
        {
            if (ba[i])
            {
                iTypeCount++;
            }
        }
        
        if (iTypeCount == 1)
        {
            if (bNumber)
            {
                return new ColumnDefinition(pColumnName, new BigDecimalDataType());
            }
            else if (bTimestamp)
            {
                return new ColumnDefinition(pColumnName, new TimestampDataType());
            }
            else if (bBoolean)
            {
                return new ColumnDefinition(pColumnName, new BooleanDataType());
            }
            else if (bBinary)
            {
                return new ColumnDefinition(pColumnName, new BinaryDataType());   
            }
            else if (bObject)
            {
                return new ColumnDefinition(pColumnName, new ObjectDataType());
            }
        }
        
        return new ColumnDefinition(pColumnName);
    }
    
    /**
     * Builds a data book with given values. All values will be used.
     * 
     * @param pValues the list of values
     * @return the book
     * @throws ModelException if data book creation fails
     */
    public static IDataBook build(Object[] pValues) throws ModelException
    {
        return build(pValues, false);
    }
    
    /**
     * Builds a data book with given values. Only unique values will be used.
     * 
     * @param pValues the list of values
     * @param pUnique <code>true</code> to ignore duplicate values, <code>false</code> to use all values
     * @return the book
     * @throws ModelException if data book creation fails
     */
    public static IDataBook build(Object[] pValues, boolean pUnique) throws ModelException
    {
        DataBookBuilder builder = new DataBookBuilder();

        if (pValues != null)
        {
            for (int i = 0; i < pValues.length; i++)
            {
                builder.checkType(pValues[i]);
            }
        }
        
        RowDefinition rowdef = new RowDefinition();
        rowdef.addColumnDefinition(new ColumnDefinition("ID", new BigDecimalDataType()));
        rowdef.addColumnDefinition(builder.createColumnDefinition("VALUE"));
        
        MemDataBook mdb = new MemDataBook(rowdef);
        mdb.setName("arrayOfValues");
        mdb.open();

        if (pValues != null)
        {
            for (int i = 0; i < pValues.length; i++)
            {
                if (!pUnique || ArrayUtil.indexOf(pValues, pValues[i], 0, i) < 0)
                {
                    mdb.insert(false);
                    mdb.setValues(new String[] {"ID", "VALUE"},
                                  new Object[] {BigDecimal.valueOf(i), pValues[i]});
                }
            }
        }
        
        mdb.saveAllRows();
        
        return mdb;
    }
    
    /**
     * Builds a data book with given values. All values will be used.
     * 
     * @param pValues the list of values
     * @return the book
     * @throws ModelException if data book creation fails
     */
    public static IDataBook build(List<Object> pValues) throws ModelException
    {
        return build(pValues, false);
    }

    /**
     * Builds a data book with given values. Only unique values will be used.
     * 
     * @param pValues the list of values
     * @param pUnique <code>true</code> to ignore duplicate values, <code>false</code> to use all values
     * @return the book
     * @throws ModelException if data book creation fails
     */
    public static IDataBook build(List<Object> pValues, boolean pUnique) throws ModelException
    {
        Object[] values;
        
        if (pValues != null)
        {
            values = pValues.toArray(new Object[pValues.size()]);
        }
        else
        {
            values = null;
        }

        return build(values, pUnique);
    }
    
    /**
     * Builds a data book with given key/value pairs.
     * 
     * @param pMap the key/value mapping
     * @return the book
     * @throws ModelException if data book creation fails
     */
    public static IDataBook build(Map<?, ?> pMap) throws ModelException
    {
        DataBookBuilder bldKey = new DataBookBuilder();
        DataBookBuilder bldValue = new DataBookBuilder();
        
        if (pMap != null)
        {
            for (Entry<?, ?> entry : pMap.entrySet())
            {
                bldKey.checkType(entry.getKey());
                bldValue.checkType(entry.getValue());
            }
        }
        
        RowDefinition rowdef = new RowDefinition();
        rowdef.addColumnDefinition(bldKey.createColumnDefinition("ID"));
        rowdef.addColumnDefinition(bldValue.createColumnDefinition("VALUE"));
        
        MemDataBook mdb = new MemDataBook(rowdef);
        mdb.setName("mapOfValues");
        mdb.open();

        if (pMap != null)
        {
            for (Entry<?, ?> entry : pMap.entrySet())
            {
                mdb.insert(false);
                mdb.setValues(new String[] {"ID", "VALUE"},
                              new Object[] {entry.getKey(), entry.getValue()});
            }
        }
        
        mdb.saveAllRows();
        
        return mdb;
    }
    
}   // DataBookBuilder
