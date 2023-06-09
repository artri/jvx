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
 * 12.05.2009 - [RH] - creation.
 * 23.11.2009 - [RH] - ColumnMetaData && PrimaryKey Column is replaced with MetaData class
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 10.03.2010 - [JR] - set NLS_COMP='BINARY'
 * 27.03.2010 - [JR] - #92: default value support    
 * 28.03.2010 - [JR] - #47: getAllowedValues implemented
 * 06.04.2010 - [JR] - #115: getUKs: prepared statement closed  
 * 06.05.2010 - [JR] - open: close statement(s)     
 * 09.10.2010 - [JR] - #114: used CheckConstraintSupport to detect allowed values       
 * 19.11.2010 - [RH] - getUKs, getPKs return Type changed to a <code>Key</code> based result.         
 * 29.11.2010 - [RH] - getUKs Oracle select statement fixed, that it only returns UKs - no PKs. 
 * 01.12.2010 - [RH] - getFKs Oracle select statement fixed, that also returns FKs over UKs.        
 * 14.12.2010 - [RH] - getUKS is solved in DBAccess.
 * 23.12.2010 - [RH] - #227: getFKs returned PK <-> FK columns wrong related, wrong sort fixed!
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views. 
 * 03.01.2011 - [RH] - schema detecting made better in getColumnMetaData()
 * 06.01.2011 - [JR] - #234: used ColumnMetaDataInfo
 * 24.02.2011 - [RH] - #295: just return the PK, UKs and FKs for the table && schema.
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented          
 * 19.07.2011 - [RH] - #432: OracleDBAccess return list of UKs wrong.   
 * 21.07.2011 - [RH] - #436: OracleDBAccess and PostgresDBAccess should translate JVx quotes in specific insert                      
 * 18.11.2011 - [RH] - #510: All XXDBAccess should provide a SQLException format method 
 * 18.03.2013 - [RH] - #632: DBStorage: Update on Synonym (Oracle) doesn't work - Synonym Support implemented
 * 15.10.2013 - [RH] - #837: DBOracleAccess MetaData determining is very slow in 11g
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used
 * 01.04.2015 - [JR] - TNS Names support via TNS_ADMIN environment property                            
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.model.datatype.TimestampDataType;
import javax.rad.persist.DataSourceException;
import javax.rad.server.IConfiguration;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.IBean;

import com.sibvisions.rad.persist.jdbc.param.AbstractParam;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.GroupHashtable;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.ROWID;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

/**
 * The <code>OracleDBAccess</code> is the implementation for Oracle databases.<br>
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland H�rmann
 */
public class OracleDBAccess extends AbstractOracleDBAccess
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // The ultimate Oracle Meta Data Optimization!
    /** The user list to be ignored. */
    public static final String IGNORE_USER_LIST =   "'SYS','SYSTEM','CTXSYS','MDSYS','XDB','HR','DBSNMP','OUTLN','TSMSYS','DIP','ANONYMOUS'," +
                                                    "'SYSBACKUP','SYSDG','SYSKM','SYSRAC','GSMUSER','REMOTE_SCHEDULER_AGENT','ORACLE_OCM','SYS$UMF'," +
                                                    "'GSMCATUSER','GGSYS','SI_INFORMTN_SCHEMA','MDDATA','DVF','ORDS_PUBLIC_USER'," +
                                                    "'EXFSYS','SYSMAN','WMSYS','OLAPSYS','ORDSYS','ORDPLUGINS','LBACSYS','DVSYS','XS$NULL','APPQOSSYS'," + 
                                                    "'AUDSYS','DBSFWUSER','DBSNMP','OJVMSYS','ORDDATA','ORDSTEST','GSMADMIN_INTERNAL'," +
                                                    "'FLOWS_030200','FLOWS_030100','FLOWS_030000','FLOWS_020200','FLOWS_020100','FLOWS_020000','FLOWS_010600','FLOWS_010500'," + 
                                                    "'FLOWS_FILES'," +
                                                    "'APEX_190200','APEX_190100','APEX_050100','APEX_050000','APEX_040200','APEX_040100','APEX_040000','APEX_030200'," +
                                                    "'APEX_PUBLIC_USER','APEX_LISTENER','APEX_REST_PUBLIC_USER','APEX_INSTANCE_ADMIN_USER'";
                                                    // ",'APPLSYS','APPS','APPS_NE'"; Oracle E-Business Suite
    

    /** Checks when the last meta data was changed. */
//    private static String sMetaDataChangedSelect = "SELECT max(c.last_ddl_time) last_change " +
//                                                     "FROM all_objects c " +
//                                                    "WHERE c.owner not in (" + IGNORE_USER_LIST + ")";
    // only check changes in own schema is always fast.
    private static String sMetaDataChangedSelect = "SELECT max(c.last_ddl_time) last_change " +
                                                     "FROM user_objects c " +
                                                    "WHERE c.object_type IN ('TABLE', 'VIEW', 'SYNONYM', 'MATERIALIZED VIEW')";
    
    /** The select statement to get the Primary, Unique and Foreign keys in Oracle. */
    private static String sConstraintSelect = "SELECT c.owner " +
                                                    ",c.constraint_name " + 
                                                    ",c.constraint_type " + 
                                                    ",c.table_name " + 
                                                    ",c.r_owner " + 
                                                    ",c.r_constraint_name " +
                                                    ",cc.column_name " +
                                                "FROM all_constraints c " +
                                                     "JOIN all_cons_columns cc on c.owner = cc.owner and c.constraint_name = cc.constraint_name " +
                                               "WHERE c.constraint_type in ('P', 'U', 'R') " +
                                                 "AND c.constraint_name not like 'BIN$%' " +
                                                 "AND c.owner not in (" + IGNORE_USER_LIST + ") " + 
                                               "ORDER BY c.owner, c.constraint_name, cc.position";

    /** The select statement to get the Check Constraint in Oracle. */
    private static String sCheckConstraintSelect = "SELECT c.owner " +
                                                         ",c.table_name " +
                                                         ",c.search_condition " +
                                                  "FROM all_constraints c " +
                                                 "WHERE c.constraint_type ='C' " +
                                                   "AND c.constraint_name not like 'SYS_C%' " +
                                                   "AND c.constraint_name not like 'BIN$%' " +
                                                   "AND c.owner not in (" + IGNORE_USER_LIST + ")"; 
            
    /** The select statement to get the Check Constraint in Oracle. */
    private static String sCheckConstraintFastSelect = "SELECT c.owner " +
                                                             ",c.table_name " +
                                                             ",getSearchCondition(c.owner, c.constraint_name) search_condition " +
                                                      "FROM all_constraints c " +
                                                     "WHERE c.constraint_type ='C' " +
                                                       "AND c.constraint_name not like 'SYS_C%' " +
                                                       "AND c.constraint_name not like 'BIN$%' " +
                                                       "AND c.owner not in (" + IGNORE_USER_LIST + ")"; 
            
 // Oracle XE 12 is very slow on select * from all_tab_cols or all_tab_columns.
 // The user_tab_cols and user_tab_columns view is fast, so we will only optimize meta data of own schema.
    /** the select statement to get the default values in Oracle. */
    private static String sDefaultValueSelect = "SELECT user owner " + // c.owner " +
                                                      ",c.table_name " + 
                                                      ",c.column_name " + 
                                                      ",c.data_type " + 
                                                      ",c.data_default " +
                                                  "FROM user_tab_columns c " + // all_tab_columns c " +
                                                 "WHERE c.data_default is not null"; // " +
                                                   // "AND c.owner not in (" + IGNORE_USER_LIST + ")"; 

    /** the select statement to get the default values in Oracle. */
    private static String sDefaultValueFastSelect = "SELECT user owner " + // c.owner " +
                                                          ",c.table_name " + 
                                                          ",c.column_name " + 
                                                          ",c.data_type " + 
                                                          ",getDataDefault(user, c.table_name, c.column_name) data_default " + 
                                                                        // c.owner, c.table_name, c.column_name) data_default " +
                                                      "FROM user_tab_columns c " + // all_tab_columns c " +
                                                     "WHERE c.data_default is not null"; // " +
                                                       // "AND c.owner not in (" + IGNORE_USER_LIST + ")";

    /** the select statement to get the default values in Oracle. */
    private static String sDefaultValuesForTableSelect = "SELECT c.column_name " + 
                                                      ",c.data_type " + 
                                                      ",c.data_default " +
                                                  "FROM all_tab_columns c " +
                                                 "WHERE c.data_default is not null " +
                                                   "AND c.owner = ? " +
                                                   "AND c.table_name = ?"; 

    /** the select statement to get the default values in Oracle. */
    private static String sDefaultValuesForTableFastSelect = "SELECT c.column_name " + 
                                                          ",c.data_type " + 
                                                          ",getDataDefault(c.owner, c.table_name, c.column_name) data_default " +
                                                      "FROM all_tab_columns c " +
                                                     "WHERE c.data_default is not null " +
                                                       "AND c.owner = ? " +
                                                       "AND c.table_name = ?"; 

// Oracle XE 12 is very slow on select * from all_tab_cols or all_tab_columns.
// The user_tab_cols and user_tab_columns view is fast, so we will only optimize meta data of own schema.
    /** the select statement to get column meta data. */
    private static String sColumnMetaDataSelect = 
            "SELECT user table_schem " + //c.owner table_schem " + 
            ",'Y' is_search_path "     + //case when owner = sys_context('userenv','current_schema') or owner = 'PUBLIC' then 'Y' else 'N' end is_search_path "
            ",c.table_name " + 
            ",c.column_name " + 
            ",case c.data_type " + 
               "when 'VARCHAR2' then 12 " + 
               "when 'CHAR' then 1 " + 
               "when 'NUMBER' then 3 " + 
               "when 'FLOAT' then 6 " + 
               "when 'DATE' then 91 " + 
               "when 'BLOB' then 2004 " + 
               "when 'CLOB' then 2005 " + 
               "when 'RAW' then -3 " + 
               "when 'LONG' then -1 " + 
               "when 'LONG RAW' then -4 " + 
               "when 'BFILE' then -13 " + 
               "when 'NCHAR' then -15 " + 
               "when 'NVARCHAR' then -9 " + 
               "when 'NVARCHAR2' then -9 " + 
               "when 'NCLOB' then 2011 " + 
               "when 'COLLECTION' then 2003 " + 
               "when 'VARRAY' then 2003 " + 
               "when 'OBJECT' then 2002 " + 
               "when 'BINARY_DOUBLE' then 101 " + 
               "when 'BINARY_FLOAT' then 100 " + 
               "when 'REF' then 2006 " + 
               "when 'ROWID' then -8 " + 
               "when 'UROWID' then -8 " + 
               "when 'XMLTYPE' then 2009 " + 
               "when 'SQLXML' then 2009 " + 
               "when 'OPAQUE/XMLTYPE' then 2009 " + 
             "else case " + 
               "when c.data_type like 'TIMESTAMP%LOCAL%' then -102 " + 
               "when c.data_type like 'TIMESTAMP%TIME%' then -101 " + 
               "when c.data_type like 'TIMESTAMP%' then 93 " + 
               "when c.data_type like 'INTERVAL DAY%' then -104 " + 
               "when c.data_type like 'INTERVAL YEAR%' then -103 " + 
             "else case (SELECT a.typecode FROM ALL_TYPES A WHERE a.type_name = c.data_type) " + 
               "when 'OBJECT' then 2002 " + 
               "when 'COLLECTION' then 2003 " + 
             "else 1111 " + 
             "end end end data_type " + 
            ",c.data_type type_name " + 
            ",case when c.nullable = 'N' then 0 else 1 end nullable " + 
            ",case when c.data_precision is null then " + 
               "case when c.data_type = 'NUMBER' then " + 
                 "case when c.data_scale is null then 0 else 38 end " + 
               "when c.data_type like '%CHAR%' then " + 
                 "c.char_length " + 
               "when c.data_type in ('BLOB', 'CLOB', 'LONG', 'LONG RAW', 'BFILE') then " +
                 "2147483647 " +  
               "else " + 
                 "c.data_length " +
               "end " + 
             "else c.data_precision end column_size " +
            ",case when c.data_type = 'NUMBER' and c.data_precision is null and c.data_scale is null then -127 else coalesce(c.data_scale, 0) end decimal_digits " + 
        "FROM user_tab_cols c " + // all_tab_cols c " + 
//       "WHERE c.owner not in (" + IGNORE_USER_LIST + ") " +
        "WHERE c.column_id is not null " + 
       "ORDER BY c.table_name, c.column_id"; // c.owner, c.table_name, c.column_id";    

    /** The select statement to get the Synonyms. */
    private static String sSynonymSelect = 
//            "SELECT user as owner, s.synonym_name, s.table_owner, s.table_name, s.db_link FROM user_synonyms s ";
            "SELECT s.owner, s.synonym_name, s.table_owner, s.table_name, s.db_link FROM all_synonyms s " +
              "WHERE s.owner not in (" + IGNORE_USER_LIST + ") " +
                "AND s.table_owner not in (" + IGNORE_USER_LIST + ") " +
                     // ensure, that synonym does not overwrite own tables or views
                "AND (s.owner <> 'PUBLIC' or s.synonym_name not in (select ut.table_name from user_tables ut))"; 

    /** True, if the procedure check was already performed. */
    protected static GroupHashtable<String, String, Boolean> ghtHasFastLongProcedures = new GroupHashtable<String, String, Boolean>();
    
    /** Cache of <code>ForeignKey</code>'s to improve performance. */
    private static GroupHashtable<String, String, Hashtable<String, List<String>>> ghtCheckValuesCache = new GroupHashtable<String, String, Hashtable<String, List<String>>>();

    /** Global setting for creating fast long procedures. */
    private static boolean bGlobalCreateFastLongProcedures = true;
    
    /** Setting for creating fast long procedures. */
    private boolean bCreateFastLongProcedures;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        registerCache(ghtCheckValuesCache);
        registerCache(ghtHasFastLongProcedures);
    }
    
    /**
     * Constructs a new OracleDBAccess Object.
     */
    public OracleDBAccess()
    {
        setDriver("oracle.jdbc.OracleDriver");
        
        configureTnsAdmin();
        
        IConfiguration cfg = SessionContext.getCurrentServerConfig();
        
        if (cfg != null)
        {
            String sCaching = cfg.getProperty("/server/globalcreatefastlongprocedures", "ON");

            setGlobalCreateFastLongProcedures("ON".equals(sCaching));
        }
        
        setCreateFastLongProcedures(isGlobalCreateFastLongProcedures()); 
    }
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @SuppressWarnings("deprecation")
    @Override
    protected Object convertToArray(AbstractParam pParam) throws SQLException
    {
        Object val = pParam.getValue();
        
        if (pParam.getSqlType() == Types.ARRAY && val != null)
        {
            oracle.sql.ArrayDescriptor arrDescr = oracle.sql.ArrayDescriptor.createDescriptor(pParam.getTypeName(), getConnection()); 
            Object[] data;
            if (val instanceof List)
            {
                data = ((List)val).toArray();
            }
            else if (val instanceof Object[])
            {
                data = ((Object[])val).clone();
            }
            else
            {
                throw new SQLException("Unsupported array value,only List and Object[] are supported!");
            }
            
            if (arrDescr.getBaseType() == Types.STRUCT)
            {
                oracle.sql.StructDescriptor structDescr = oracle.sql.StructDescriptor.createDescriptor(arrDescr.getBaseName(), getConnection());
                ResultSetMetaData rsmd = structDescr.getMetaData();
    
                String[] propertyNames = new String[rsmd.getColumnCount()];
                String[] pojoNames = new String[propertyNames.length];
                for (int j = 0; j < propertyNames.length; j++)
                {
                    propertyNames[j] = rsmd.getColumnName(j + 1);
                    pojoNames[j] = StringUtil.convertToMemberName(propertyNames[j]);
                }
                
                for (int i = 0; i < data.length; i++)
                {
                    Object item = data[i];
                    
                    if (!(item instanceof Object[]))
                    {
                        Object[] itemResult = new Object[propertyNames.length];
                        String[] cols;
                        IBean bean;
                        if (item instanceof IBean)
                        {
                            bean = (IBean)item;
                            cols = propertyNames;
                        }
                        else
                        {
                            bean = new Bean(item);
                            cols = pojoNames;
                        }
                        for (int j = 0; j < cols.length; j++)
                        {
                            itemResult[j] = bean.get(cols[j]);
                        }
                        data[i] = itemResult;
                    }
                }
            }
            
            return ((OracleConnection)getConnection()).createARRAY(pParam.getTypeName(), data);
        }
        else if (pParam.getSqlType() == Types.STRUCT && val != null)
        {
            oracle.sql.StructDescriptor structDescr = oracle.sql.StructDescriptor.createDescriptor(pParam.getTypeName(), getConnection());
            ResultSetMetaData rsmd = structDescr.getMetaData();

            String[] propertyNames = new String[rsmd.getColumnCount()];
            String[] pojoNames = new String[propertyNames.length];
            for (int j = 0; j < propertyNames.length; j++)
            {
                propertyNames[j] = rsmd.getColumnName(j + 1);
                pojoNames[j] = StringUtil.convertToMemberName(propertyNames[j]);
            }
            
            Object[] data;

            if (val instanceof Object[])
            {
                data = (Object[])val;
            }
            else
            {
                data = new Object[propertyNames.length];
                String[] cols;
                IBean bean;
                if (val instanceof IBean)
                {
                    bean = (IBean)val;
                    cols = propertyNames;
                }
                else
                {
                    bean = new Bean(val);
                    cols = pojoNames;
                }
                for (int j = 0; j < cols.length; j++)
                {
                    data[j] = bean.get(cols[j]);
                }
            }
            return ((OracleConnection)getConnection()).createStruct(pParam.getTypeName(), data);
        }
        else
        {
            return val;
        }
    }

    /**
     * Converts arrays to {@link List} of {@link IBean}.
     * @param pParam the param to check
     * @return the param or a list in case of array.
     * @throws SQLException the exception
     */
    @SuppressWarnings("deprecation")
    @Override
    protected Object convertArrayToList(Object pParam) throws SQLException
    {
        if (pParam instanceof oracle.sql.ARRAY)
        {
            oracle.sql.ARRAY array = (oracle.sql.ARRAY)pParam;
            
            BeanType beanType = null;
            List<Object> result = new ArrayList<Object>();
            
            Object[] arr = (Object[])array.getArray();
            
            for (int i = 0; i < arr.length; i++)
            {
                Object item = arr[i];
                
                if (item instanceof oracle.sql.STRUCT)
                {
                    oracle.sql.STRUCT struct = (oracle.sql.STRUCT)item;
                    
                    if (beanType == null)
                    {
                        ResultSetMetaData rsmd = struct.getDescriptor().getMetaData();

                        String[] propertyNames = new String[rsmd.getColumnCount()];
                        for (int j = 0; j < propertyNames.length; j++)
                        {
                            propertyNames[j] = rsmd.getColumnName(j + 1);
                        }
                        beanType = new BeanType(propertyNames);
                    }
                    
                    Object[] attributes = struct.getAttributes();
                    Bean bean = new Bean(beanType);
                    for (int j = 0; j < attributes.length; j++)
                    {
                        bean.put(j, attributes[j]);
                    }
                    
                    result.add(bean);
                }
                else
                {
                    result.add(item);
                }
            }
            
            return result;
        }
        else if (pParam instanceof oracle.sql.STRUCT)
        {
            oracle.sql.STRUCT struct = (oracle.sql.STRUCT)pParam;
            
            ResultSetMetaData rsmd = struct.getDescriptor().getMetaData();

            String[] propertyNames = new String[rsmd.getColumnCount()];
            for (int j = 0; j < propertyNames.length; j++)
            {
                propertyNames[j] = rsmd.getColumnName(j + 1);
            }
            BeanType beanType = new BeanType(propertyNames);
            
            Object[] attributes = struct.getAttributes();
            Bean bean = new Bean(beanType);
            for (int j = 0; j < attributes.length; j++)
            {
                bean.put(j, attributes[j]);
            }
            
            return bean;
        }
        else if (pParam instanceof ResultSet)
        {
            List<Bean> liResult = new ArrayUtil<Bean>();
            fillResultSetIntoListOfBean((ResultSet)pParam, liResult, null);
            
            return liResult;
        }
        else
        {
            return pParam;
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    protected Object getObjectFromResultSet(ResultSet pResultSet, int pIndex) throws SQLException
    {
        Object result = super.getObjectFromResultSet(pResultSet, pIndex);
        
        if (result instanceof oracle.sql.BFILE)
        {
            result = new BlobFromBFILE((oracle.sql.BFILE)result);
        }
        else if (result instanceof TIMESTAMPLTZ
                || result instanceof TIMESTAMPTZ
                || result instanceof TIMESTAMP
                || result instanceof DATE)
        {
            result = pResultSet.getTimestamp(pIndex);
        }
        else if (result instanceof ROWID)
        {
            result = result.toString();
        }
        
        return result;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    protected Object convertDatabaseSpecificObjectToValue(ServerColumnMetaData pColumnMetaData, Object pValue) throws SQLException
    {
        if (pValue instanceof TIMESTAMPLTZ)
        {
            return ((TIMESTAMPLTZ)pValue).timestampValue(getConnectionIntern());
        }
        else if (pValue instanceof TIMESTAMPTZ)
        {
            return ((TIMESTAMPTZ)pValue).timestampValue(getConnectionIntern());
        }
        else if (pValue instanceof Datum && pColumnMetaData.getTypeIdentifier() == TimestampDataType.TYPE_IDENTIFIER)
        {
            return ((Datum)pValue).timestampValue();
        }
        
        return pValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTableForSynonymIntern(String pSynonym) throws DataSourceException
    {
        checkMetaDataChange();
        
        String identifier = getIdentifier();
        
        Hashtable<String, String> tableNameCache = getTableNameCache(identifier);

        if (tableNameCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();
            
            tableNameCache = new Hashtable<String, String>();
            
            try
            {   
                List<Bean> allSynonyms = getSynonymData();
                
                String defaultSchema = getConnectionIntern().getMetaData().getUserName().toUpperCase();
                
                for (Bean synonym : allSynonyms)
                {
                    String sOwner = (String)synonym.get("OWNER");
                    String sSynonymName = (String)synonym.get("SYNONYM_NAME");
                    String sSchema = (String)synonym.get("TABLE_OWNER");
                    String sTable  = (String)synonym.get("TABLE_NAME");
                    String sDBLink = (String)synonym.get("DB_LINK");
    
                    StringBuilder sRealTable = new StringBuilder();
                    
                    if (sSchema != null && (!defaultSchema.equals(sSchema) || sDBLink != null))
                    {
                        sRealTable.append(quote(sSchema));                 
                        sRealTable.append('.');                 
                    }
    
                    sRealTable.append(quote(sTable));
    
                    if (sDBLink != null)
                    {
                        sRealTable.append('@');                 
                        sRealTable.append(quote(sDBLink));                 
                    }
                    
                    if (!"PUBLIC".equals(sOwner))
                    {
                        tableNameCache.put(createIgnoreCaseIdentifier(quote(sOwner) + "." + quote(sSynonymName)), sRealTable.toString());
                    }
                    if (defaultSchema.equals(sSchema) || "PUBLIC".equals(sOwner))
                    {
                        tableNameCache.put(createIgnoreCaseIdentifier(quote(sSynonymName)), sRealTable.toString());
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("Synonyms couldn't be determined from database!", formatSQLException(sqlException));
            }       
    
            setTableNameCache(identifier, tableNameCache);
            
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllTablesforSynonyms in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }
        
        String tableIdentifier = createIgnoreCaseIdentifier(pSynonym);
    
        String tableName = tableNameCache.get(tableIdentifier);
        
        if (tableName == null || tableName == TABLENAME_NULL)
        {
            return pSynonym;
        }
        else
        {
            return tableName;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Key> getUniqueKeysIntern(String pCatalog, 
                                            String pSchema, 
                                            String pTable) throws DataSourceException
    {
        getAllPrimaryUniqueForeignKeys();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

        Hashtable<String, List<Key>> uniqueKeyCache = getUniqueKeyCache(identifier);
        
        if (uniqueKeyCache != null)
        {
            List<Key> uniqueKeys = uniqueKeyCache.get(tableIdentifier);
            if (uniqueKeys != null)
            {
                return uniqueKeys;
            }
        }
        
        return new ArrayUtil<Key>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Key getPrimaryKeyIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        getAllPrimaryUniqueForeignKeys();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

        Hashtable<String, Key> primaryKeyCache = getPrimaryKeyCache(identifier);
        
        if (primaryKeyCache != null)
        {
            Key primaryKey = primaryKeyCache.get(tableIdentifier);
            if (primaryKey != PKS_NULL)
            {
                return primaryKey;
            }
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        getAllPrimaryUniqueForeignKeys();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

        Hashtable<String, List<ForeignKey>> foreignKeyCache = getForeignKeyCache(identifier);
        
        if (foreignKeyCache != null)
        {
            List<ForeignKey> foreignKeys = foreignKeyCache.get(tableIdentifier);
            if (foreignKeys != null)
            {
                return foreignKeys;
            }
        }
        
        return new ArrayUtil<ForeignKey>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object[]> getAllowedValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        getAllCheckConstraints();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
        
        try
        {
            String defaultSchema = getConnectionIntern().getMetaData().getUserName().toUpperCase();
            
            if (pSchema == null || pSchema.equals(defaultSchema))
            {
                getAndStoreMetaDataIntern(quote(pTable), null, null, null, null);
            }
            else
            {
                getAndStoreMetaDataIntern(quote(pSchema) + "." + quote(pTable), null, null, null, null);
            }
        }
        catch (Exception ex)
        {
            debug(ex); // Asking allowed values for an unknown table should not throw an exception 
        }
        if (cachedColumnMetaData != null)
        {
            return CheckConstraintSupport.translateValues(this, cachedColumnMetaData, ghtCheckValuesCache.get(identifier, tableIdentifier));
        }

        return null;
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        getAllDefaultValues();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

        Hashtable<String, Map<String, Object>> defaultValuesCache = getDefaultValuesCache(identifier);
        Map<String, Object> defaultValues = defaultValuesCache.get(tableIdentifier);

        if (defaultValues == null)
        {
            try
            {
                String defaultSchema = getConnectionIntern().getMetaData().getUserName().toUpperCase();

                if (!defaultSchema.equals(pSchema)) // getAllDefaultValues already loaded all default values of current schema.
                {
                    List<Bean> allDefaultValueData = getDefaultValuesForTableData(pSchema, pTable);
                    
                    for (Bean defaultValueData : allDefaultValueData)
                    {
                        String columnName      = (String)defaultValueData.get("COLUMN_NAME");
                        String dataType        = (String)defaultValueData.get("DATA_TYPE");
                        String dataDefault     = (String)defaultValueData.get("DATA_DEFAULT");
    
                        int type = Types.VARCHAR;
                        if (dataType.contains("DATE") || dataType.contains("TIME") || dataType.contains("INTERVAL"))
                        {
                            type = Types.DATE;
                        }
                        else if (dataType.contains("NUMBER") || dataType.contains("FLOAT") || dataType.contains("INTEGER"))
                        {
                            type = Types.DECIMAL;
                        }
                            
                        try
                        {
                            Object objValue = translateDefaultValue(columnName, type, dataDefault.trim());
    
                            if (objValue != null)
                            {
                                if (defaultValues == null)
                                {
                                    defaultValues = new Hashtable<String, Object>();
                                }

                                defaultValues.put(columnName, objValue);
                            }
                        }
                        catch (Exception e)
                        {
                            //no default value
                            //debug(value, e);
                        }
                    }
                }
                
                if (defaultValues == null)
                {
                    defaultValues = DEFAULT_VALUES_NULL;
                }

                defaultValuesCache.put(tableIdentifier, defaultValues);
            }
            catch (Exception ex)
            {
                debug(ex);
            }
        }

        if (defaultValues == DEFAULT_VALUES_NULL)
        {
            return null;
        }
        else
        {
            return defaultValues;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void getAndStoreMetaDataIntern(String pFromClause, 
            String[] pQueryColumns,
            String pBeforeQueryColumns, 
            String pWhereClause, 
            String pAfterWhereClause) throws DataSourceException
    {
        checkMetaDataChange();

        String identifier = getIdentifier();

        Hashtable<String, ServerColumnMetaData[]> columnMetaDataCache = getColumnMetaDataCache(identifier);
        Hashtable<String, TableInfo> tableInfoCache = getTableInfoCache(identifier);

        // Oracle XE 12 does not return on select * from all_tab_cols or all_tab_columns.
        if (columnMetaDataCache == null || tableInfoCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();

            columnMetaDataCache = new Hashtable<String, ServerColumnMetaData[]>();
            tableInfoCache = new Hashtable<String, TableInfo>();
            setColumnMetaDataCache(identifier, columnMetaDataCache);
            setTableInfoCache(identifier, tableInfoCache);

            try
            {
                List<Bean> allColumnMetaData = getColumnMetaDataData();
                
                String lastTableSchem = null;
                boolean lastIsSearchPath = false;
                String lastTableName = null;
                
                ResultSetMetaDataImpl resultSetMetaData = new ResultSetMetaDataImpl();
                ArrayUtil<ServerColumnMetaData> auCmd = new ArrayUtil<ServerColumnMetaData>();
                
                for (int i = 0, size = allColumnMetaData.size(); i <= size; i++)
                {
                    Bean columnMetaData = i < size ? allColumnMetaData.get(i) : null;

                    ResultSetMetaDataColumn column = new ResultSetMetaDataColumn();
                    boolean isSearchPath = false;
                    if (columnMetaData != null)
                    {
                        column.setSchemaName((String)columnMetaData.get("TABLE_SCHEM"));
                        isSearchPath = "Y".equals(columnMetaData.get("IS_SEARCH_PATH"));
                        column.setTableName((String)columnMetaData.get("TABLE_NAME"));
                        column.setColumnName((String)columnMetaData.get("COLUMN_NAME"));
                        column.setColumnLabel((String)columnMetaData.get("COLUMN_NAME"));
                        column.setColumnType(((Number)columnMetaData.get("DATA_TYPE")).intValue());
                        column.setColumnTypeName((String)columnMetaData.get("TYPE_NAME"));
                        column.setNullable(((Number)columnMetaData.get("NULLABLE")).intValue());
                        column.setPrecision(((Number)columnMetaData.get("COLUMN_SIZE")).intValue());
                        column.setScale(((Number)columnMetaData.get("DECIMAL_DIGITS")).intValue());
                        column.setSigned(true);
                        column.setWritable(true);
                    }

                    if (i == size || !column.getSchemaName().equals(lastTableSchem) || !column.getTableName().equals(lastTableName))
                    {
                        if (resultSetMetaData.getColumnCount() > 0)
                        {
                            String sColName = "";
                            try
                            {
                                for (int j = 1, count = resultSetMetaData.getColumnCount(); j <= count; j++)
                                {
                                    sColName = getColumnName(resultSetMetaData, j);
                                    auCmd.add(createServerColumnMetaData(resultSetMetaData, j, null));
                                }
                                sColName = "";
                                
                                ServerColumnMetaData[] metaData = auCmd.toArray(new ServerColumnMetaData[auCmd.size()]);
                                
                                TableInfo tableInfo = new TableInfo(null, lastTableSchem, lastTableName);
                                
                                String writeBackTable = quote(lastTableSchem) + "." + quote(lastTableName);

                                columnMetaDataCache.put(createIgnoreCaseIdentifier(writeBackTable, null, null), metaData);
                                tableInfoCache.put(createIgnoreCaseIdentifier(writeBackTable), tableInfo);
                                createAndStorePossibleQueryMetaData(writeBackTable, metaData);
    
                                if (lastIsSearchPath)
                                {
                                    writeBackTable = quote(lastTableName);
                                    
                                    columnMetaDataCache.put(createIgnoreCaseIdentifier(writeBackTable, null, null), metaData);
                                    tableInfoCache.put(createIgnoreCaseIdentifier(writeBackTable), tableInfo);
                                    createAndStorePossibleQueryMetaData(writeBackTable, metaData);
                                }
                            }
                            catch (Exception ex)
                            {
                                debug("Couldn't preload MetaData for \"", 
                                        lastTableSchem, "\".\"", lastTableName, "".equals(sColName) ? "" : "\" Column: \"", sColName, "\"! ", ex.getMessage());
                            }
                        }
                        
                        lastTableSchem = column.getSchemaName();
                        lastIsSearchPath = isSearchPath;
                        lastTableName = column.getTableName();
                        
                        resultSetMetaData.removeAllColumns(); 
                        auCmd.clear();
                    }
                    resultSetMetaData.addColumn(column);
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("MetaData couldn't be preloaded from database!", formatSQLException(sqlException));
            }
            
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllColumnMetaData in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }

        String tableIdentifier = createIgnoreCaseIdentifier(pFromClause, pQueryColumns, pBeforeQueryColumns);
                
        ServerColumnMetaData[] columnMetaData = columnMetaDataCache.get(tableIdentifier);
        TableInfo tableInfo = tableInfoCache.get(createIgnoreCaseIdentifier(pFromClause));
        
        if (columnMetaData != null)
        {
            cachedMetaDataIdentifier = tableIdentifier;
            cachedColumnMetaData = columnMetaData == COLUMNMETADATA_NULL ? null : columnMetaData;
            if (tableInfo == null || tableInfo == TABLEINFO_NULL)
            {
                cachedCatalogInfo = null;
                cachedSchemaInfo  = null;
                cachedTableInfo = null;
            }
            else
            {
                cachedCatalogInfo = tableInfo.getCatalog();
                cachedSchemaInfo  = tableInfo.getSchema();
                cachedTableInfo = tableInfo.getTable();
            }
        }
        else
        {
            super.getAndStoreMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, pAfterWhereClause);

            columnMetaData = cachedColumnMetaData;
            
            columnMetaDataCache.put(tableIdentifier, columnMetaData == null ? COLUMNMETADATA_NULL : columnMetaData);
            
            // Store default MetaData for Query, they are the same.
            if (pQueryColumns == null && pBeforeQueryColumns == null && columnMetaData != null)
            {
                createAndStorePossibleQueryMetaData(pFromClause, columnMetaData);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Date getLastMetaDataChange() throws Exception
    {
        return (Date)executeQuery(sMetaDataChangedSelect).get(0).get(0);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Global setting for creating fast long procedures.
     * 
     * @return true, if fast long procedures should be created.
     */
    public static boolean isGlobalCreateFastLongProcedures()
    {
        return bGlobalCreateFastLongProcedures;
    }

    /**
     * Global setting for creating fast long procedures.
     * 
     * @param pEnabled true, if fast long procedures should be created.
     */
    public static void setGlobalCreateFastLongProcedures(boolean pEnabled)
    {
        bGlobalCreateFastLongProcedures = pEnabled;
    }
    
    /**
     * Setting for creating fast long procedures.
     * 
     * @return true, if fast long procedures should be created.
     */
    public boolean isCreateFastLongProcedures()
    {
        return bCreateFastLongProcedures;
    }

    /**
     * Setting for creating fast long procedures.
     * 
     * @param pEnabled true, if fast long procedures should be created.
     */
    public void setCreateFastLongProcedures(boolean pEnabled)
    {
        bCreateFastLongProcedures = pEnabled;
    }
    
    /**
     * Checks, if there are fast long procedures for faster meta data.<br>
     * <br>
     * If procedures will not be created automatically the following script can be used:<br>
     * <pre>
     * <code>
     * create or replace function getSearchCondition(pOwner in varchar2,  pConstraintName in varchar2) return varchar2 is
     *   vSearchCondition all_constraints.search_condition%type;
     * begin
     *   select search_condition into vSearchCondition
     *     from all_constraints
     *    where owner = pOwner and constraint_name = pConstraintName;
     *   return substr(vSearchCondition, 1, 4000);
     * end;
     * 
     * create or replace function getDataDefault(pOwner in varchar2,  pTableName in varchar2,  pColumnName in varchar2) return varchar2 is
     *     vDataDefault all_tab_columns.data_default%type;
     * begin
     *     select data_default into vDataDefault
     *       from all_tab_columns
     *      where owner = pOwner and table_name = pTableName and column_name = pColumnName;
     *     return substr(vDataDefault, 1, 4000);
     * end;
     * </code>
     * </pre>
     * 
     * @return true, if there are fast long procedures for faster meta data.
     */
    protected boolean hasFastLongProcedures()
    {
        String identifier = getIdentifier();
        
        Boolean hasProcedures = ghtHasFastLongProcedures.get(identifier, "");
        if (hasProcedures == null)
        {
            hasProcedures = Boolean.TRUE;
            try 
            {
                executeStatement("declare res varchar2(100); begin select getSearchCondition(null, null) into res from dual; end;");
            }
            catch (Exception ex)
            {
                if (isCreateFastLongProcedures())
                {
                    try
                    {
                        executeStatement(
                                "create or replace function getSearchCondition(pOwner in varchar2,  pConstraintName in varchar2) return varchar2 is\n" + 
                                "  vSearchCondition all_constraints.search_condition%type;\n" + 
                                "begin\n" + 
                                "  select search_condition into vSearchCondition\n" + 
                                "    from all_constraints\n" + 
                                "   where owner = pOwner and constraint_name = pConstraintName;\n" + 
                                "  return substr(vSearchCondition, 1, 4000);\n" + 
                                "end;\n");
                    }
                    catch (Exception ex2)
                    {
                        hasProcedures = Boolean.FALSE;
                    }
                }
                else
                {
                    hasProcedures = Boolean.FALSE;
                }
            }
            try 
            {
                executeStatement("declare res varchar2(100); begin select getDataDefault(null, null, null) into res from dual; end;");
            }
            catch (Exception ex)
            {
                if (isCreateFastLongProcedures())
                {
                    try
                    {
                        executeStatement(
                                "create or replace function getDataDefault(pOwner in varchar2,  pTableName in varchar2,  pColumnName in varchar2) return varchar2 is\n" + 
                                "    vDataDefault all_tab_columns.data_default%type;\n" + 
                                "begin\n" + 
                                "    select data_default into vDataDefault\n" + 
                                "      from all_tab_columns\n" + 
                                "     where owner = pOwner and table_name = pTableName and column_name = pColumnName;\n" + 
                                "    return substr(vDataDefault, 1, 4000);\n" + 
                                "end;\n");
                    }
                    catch (Exception ex2)
                    {
                        hasProcedures = Boolean.FALSE;
                    }
                }
                else
                {
                    hasProcedures = Boolean.FALSE;
                }
            }
            
            ghtHasFastLongProcedures.put(identifier, "", hasProcedures);
        }
        
        return hasProcedures.booleanValue();
    }
    
    
    /**
     * Gets the data from default values select.
     * 
     * @return the data from default values select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getDefaultValueData() throws SQLException 
    {
        if (hasFastLongProcedures())
        {
            return executeQuery(sDefaultValueFastSelect); 
        }
        else
        {
            return executeQuery(sDefaultValueSelect); 
        }
    }
      
    /**
     * Gets the data from default values for table select.
     * 
     * @param pSchema the schema
     * @param pTable the table
     * @return the data from default values for table select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getDefaultValuesForTableData(String pSchema, String pTable) throws SQLException 
    {
        if (hasFastLongProcedures())
        {
            return executeQuery(sDefaultValuesForTableFastSelect, pSchema, pTable); 
        }
        else
        {
            return executeQuery(sDefaultValuesForTableSelect, pSchema, pTable); 
        }
    }
      
    
    /**
     * Gets the data from synonym select.
     * 
     * @return the data from synonym select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getSynonymData() throws SQLException 
    {
        return executeQuery(sSynonymSelect);
    }
      
    /**
     * Gets the data from check constraint select.
     *  
     * @return the data from check constraint select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getCheckConstraintData() throws SQLException 
    {
        if (hasFastLongProcedures())
        {
            return executeQuery(sCheckConstraintFastSelect); 
        }
        else
        {
            return executeQuery(sCheckConstraintSelect); 
        }
    }

    /**
     * Gets the data from constraint select.
     *  
     * @return the data from constraint select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getConstraintData() throws SQLException 
    {
        return executeQuery(sConstraintSelect);
    }  

    /**
     * Gets the data from column meta data select.
     *  
     * @return the data from column meta data select.
     * @throws SQLException if it fails.
     */
    protected List<Bean> getColumnMetaDataData() throws SQLException 
    {
      return executeQuery(sColumnMetaDataSelect);
    }
    
    /**
     * Gets all constraints at once, as it is faster in Oracle.
     * 
     * @throws DataSourceException the exception
     */
    protected void getAllPrimaryUniqueForeignKeys() throws DataSourceException
    {
        checkMetaDataChange();

        String identifier = getIdentifier();
        
        Hashtable<String, Key> primaryKeyCache = getPrimaryKeyCache(identifier);
        Hashtable<String, List<Key>> uniqueKeyCache = getUniqueKeyCache(identifier);
        Hashtable<String, List<ForeignKey>> foreignKeyCache = getForeignKeyCache(identifier);
        
        if (primaryKeyCache == null || uniqueKeyCache == null || foreignKeyCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();

            primaryKeyCache = new Hashtable<String, Key>();
            uniqueKeyCache = new Hashtable<String, List<Key>>();
            foreignKeyCache = new Hashtable<String, List<ForeignKey>>();

            try
            {
                List<Bean> allConstraintData = getConstraintData(); 
                
                String oldOwner             = null;
                String oldConstraintName    = null;
                String oldConstraintType    = null;
                String oldTableName         = null;
                String oldROwner            = null;
                String oldRConstraintName   = null;

                ArrayUtil<Name> auKeyColumns = new ArrayUtil<Name>();
                Map<String, Name[]> constraintColumns = new HashMap<String, Name[]>();
                Map<String, String[]> constraintSchemaTable = new HashMap<String, String[]>();
                List<String[]> foreignKeyConstraints = new ArrayUtil<String[]>();
                
                for (int i = 0, size = allConstraintData.size(); i <= size; i++)
                {
                    Bean constraintData   = i < size ? allConstraintData.get(i) : new Bean();
                    String owner          = (String)constraintData.get("OWNER");
                    String constraintName = (String)constraintData.get("CONSTRAINT_NAME");
                    String tableName       = (String)constraintData.get("TABLE_NAME");
                    String rConstraintName = (String)constraintData.get("R_CONSTRAINT_NAME");
                    String columnName     = (String)constraintData.get("COLUMN_NAME");
                    
                    if (i == size || !owner.equals(oldOwner) || !constraintName.equals(oldConstraintName)
                            || !tableName.equals(oldTableName) || !CommonUtil.equals(rConstraintName, oldRConstraintName))
                    {
                        if (auKeyColumns.size() > 0)
                        {
                            String tableIdentifier = createIdentifier(null, oldOwner, oldTableName);
                            
                            Name[] columns = auKeyColumns.toArray(new Name[auKeyColumns.size()]);
                            auKeyColumns.clear();
                            
                            String fullConstraintName = oldOwner + "." + oldConstraintName;
                            constraintColumns.put(fullConstraintName, columns);
                            constraintSchemaTable.put(fullConstraintName, new String[] {oldOwner, oldTableName});
    
                            if ("P".equals(oldConstraintType))
                            {
                                Key primaryKey = new Key(oldConstraintName, columns);
                            
                                primaryKeyCache.put(tableIdentifier, primaryKey);
                            }
                            else if ("U".equals(oldConstraintType))
                            {
                                Key uniqueKey = new Key(oldConstraintName, columns);
                            
                                List<Key> uniqueKeys = uniqueKeyCache.get(tableIdentifier);
                                if (uniqueKeys == null)
                                {
                                    uniqueKeys = new ArrayUtil<Key>();
                                    uniqueKeyCache.put(tableIdentifier, uniqueKeys);
                                }
                                uniqueKeys.add(uniqueKey);
                            }
                            else if ("F".equals(oldConstraintType) || "R".equals(oldConstraintType))
                            {
                                String fullRConstraintName = oldROwner + "." + oldRConstraintName;
                                
                                foreignKeyConstraints.add(new String[] {tableIdentifier, oldConstraintName, fullConstraintName, fullRConstraintName});
                            }
                        }
    
                        oldOwner           = owner;
                        oldConstraintName  = constraintName;
                        oldConstraintType  = (String)constraintData.get("CONSTRAINT_TYPE");
                        oldTableName       = tableName;
                        oldROwner          = (String)constraintData.get("R_OWNER");
                        oldRConstraintName = rConstraintName;
                    }
                    
                    auKeyColumns.add(new Name(columnName, quote(columnName)));
                }
    
                for (String[] foreignKeyConstraint : foreignKeyConstraints)
                {
                    String tableIdentifier = foreignKeyConstraint[0];
                    String constraintName = foreignKeyConstraint[1];
                    String fullConstraintName = foreignKeyConstraint[2];
                    String fullRConstraintName = foreignKeyConstraint[3];

                    Name[] columns = constraintColumns.get(fullConstraintName);
                    Name[] rColumns = constraintColumns.get(fullRConstraintName);
                    String[] rSchemaTable = constraintSchemaTable.get(fullRConstraintName);
                    if (rSchemaTable != null) // We might not have the right to read the primary key constraint
                    {
                        String rSchema = rSchemaTable[0];
                        String rTable = rSchemaTable[1];
    
                        ForeignKey foreignKey = new ForeignKey(new Name(rTable, quote(rTable)), 
                                new Name(null, null), 
                                new Name(rSchema, quote(rSchema)));
                        foreignKey.setFKName(constraintName);
                        foreignKey.setFKColumns(columns);
                        foreignKey.setPKColumns(rColumns);
                        
                        List<ForeignKey> foreignKeys = foreignKeyCache.get(tableIdentifier);
                        if (foreignKeys == null)
                        {
                            foreignKeys = new ArrayUtil<ForeignKey>();
                            foreignKeyCache.put(tableIdentifier, foreignKeys);
                        }
                        foreignKeys.add(foreignKey);
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("Primary, Unique and Foreign Keys couldn't be determined from database!", formatSQLException(sqlException));
            }
            
            setPrimaryKeyCache(identifier, primaryKeyCache);
            setUniqueKeyCache(identifier, uniqueKeyCache);
            setForeignKeyCache(identifier, foreignKeyCache);

            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllPrimaryUniqueForeignKeys in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }
    }

    /**
     * Gets all constraints at once, as it is faster in Oracle.
     * 
     * @throws DataSourceException the exception
     */
    protected void getAllCheckConstraints() throws DataSourceException
    {
        checkMetaDataChange();

        String identifier = getIdentifier();
        
        Hashtable<String, Hashtable<String, List<String>>> checkValueCache = ghtCheckValuesCache.get(identifier);
        
        if (checkValueCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();

            checkValueCache = new Hashtable<String, Hashtable<String, List<String>>>();
            
            try
            {
                List<Bean> allConstraintData = getCheckConstraintData();
                
                for (Bean constraintData : allConstraintData)
                {
                    String owner            = (String)constraintData.get("OWNER");
                    String tableName        = (String)constraintData.get("TABLE_NAME");
                    String searchCondition  = (String)constraintData.get("SEARCH_CONDITION");
                
                    String tableIdentifier = createIdentifier(null, owner, tableName);
                    
                    Hashtable<String, List<String>> checkValue = checkValueCache.get(tableIdentifier);
                    
                    checkValue = CheckConstraintSupport.parseCondition(searchCondition, checkValue, true);
                    
                    if (checkValue != null)
                    {
                        checkValueCache.put(tableIdentifier, checkValue);
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("Check Constraints couldn't be determined from database!", formatSQLException(sqlException));
            }
            
            ghtCheckValuesCache.put(identifier, checkValueCache);

            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllCheckConstraints in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }
    }

    /**
     * Gets all default values at once, as it is faster in Oracle.
     * 
     * @throws DataSourceException the exception
     */
    protected void getAllDefaultValues() throws DataSourceException
    {
        checkMetaDataChange();

        String identifier = getIdentifier();
        
        Hashtable<String, Map<String, Object>> defaultValuesCache = getDefaultValuesCache(identifier);
        
        if (defaultValuesCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();

            defaultValuesCache = new Hashtable<String, Map<String, Object>>();
            
            try
            {
                List<Bean> allDefaultValueData = getDefaultValueData();
                
                for (Bean defaultValueData : allDefaultValueData)
                {
                    String owner            = (String)defaultValueData.get("OWNER");
                    String tableName        = (String)defaultValueData.get("TABLE_NAME");
                    String columnName      = (String)defaultValueData.get("COLUMN_NAME");
                    String dataType        = (String)defaultValueData.get("DATA_TYPE");
                    String dataDefault     = (String)defaultValueData.get("DATA_DEFAULT");

                    String tableIdentifier = createIdentifier(null, owner, tableName);
                    
                    Map<String, Object> defaultValues = defaultValuesCache.get(tableIdentifier);
                    
                    int type = Types.VARCHAR;
                    if (dataType.contains("DATE") || dataType.contains("TIME") || dataType.contains("INTERVAL"))
                    {
                        type = Types.DATE;
                    }
                    else if (dataType.contains("NUMBER") || dataType.contains("FLOAT") || dataType.contains("INTEGER"))
                    {
                        type = Types.DECIMAL;
                    }
                        
                    try
                    {
                        Object objValue = translateDefaultValue(columnName, type, dataDefault.trim());

                        if (objValue != null)
                        {
                            if (defaultValues == null)
                            {
                                defaultValues = new Hashtable<String, Object>();
                                defaultValuesCache.put(tableIdentifier, defaultValues);
                            }
                            
                            defaultValues.put(columnName, objValue);
                        }
                    }
                    catch (Exception e)
                    {
                        //no default value
                        //debug(value, e);
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("Default Values couldn't be determined from database!", formatSQLException(sqlException));
            }
            
            setDefaultValuesCache(identifier, defaultValuesCache);

            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllDefaultValues in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }
    }

    /**
     * Sets the correct environment for oracle jdbc driver, to support tns names files.
     */
    private void configureTnsAdmin()
    {
        try
        {
            if (System.getProperty("oracle.net.tns_admin") == null)
            {
                String tnsAdmin = System.getenv("TNS_ADMIN");
                
                if (tnsAdmin == null)
                {
                    String oracleHome = System.getenv("ORACLE_HOME");
                    
                    if (oracleHome != null)
                    {
                        tnsAdmin = oracleHome + "/network/admin";
                    }
                    else
                    {
                        String fullPath = System.getenv("PATH");
                        
                        if (fullPath != null)
                        {
                            String[] paths = fullPath.split(File.pathSeparator);
                            
                            for (int i = 0; tnsAdmin == null && i < paths.length; i++)
                            {
                                String path = paths[i].toLowerCase().replace('\\', '/');
                                if (path.contains("oracle") && path.endsWith("/bin"))
                                {
                                    path = paths[i].substring(0, path.length() - 4) + "/network/admin";
                                    
                                    if (new File(path).exists())
                                    {
                                        tnsAdmin = path;
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (tnsAdmin != null)
                {
                    System.setProperty("oracle.net.tns_admin", tnsAdmin);
                }
            }
        }
        catch (Exception ex)
        {
            debug("Configure tns admin failed!" + ex);
        }
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>Blob</code> is the implementation for Oracle <code>BFILE</code>.<br>
     *  
     * @author Martin Handsteiner
     */
    @SuppressWarnings("deprecation")
    public static class BlobFromBFILE implements java.sql.Blob
    {
        /** The bfile. */
        private oracle.sql.BFILE bFile;
        
        /**
         * Gets a Blob compatible Object for BFile.
         * @param pBFILE the bFile.
         */
        public BlobFromBFILE(oracle.sql.BFILE pBFILE)
        {
            bFile = pBFILE;
        }

        /**
         * {@inheritDoc}
         */
        public long length() throws SQLException 
        {
            return bFile.length();
        }

        /**
         * {@inheritDoc}
         */
        public byte[] getBytes(long pPos, int pLength) throws SQLException 
        {
            try
            {
                bFile.openFile();

                return bFile.getBytes(pPos, pLength);
            }
            finally
            {
                bFile.closeFile();
            }
        }

        /**
         * {@inheritDoc}
         */
        public InputStream getBinaryStream() throws SQLException 
        {
            return new ByteArrayInputStream(getBytes(1, (int)length()));
        }

        /**
         * {@inheritDoc}
         */
        public InputStream getBinaryStream(long pPos, long pLength) throws SQLException 
        {
            return bFile.getBinaryStream(pPos);
        }
        
        /**
         * {@inheritDoc}
         */
        public long position(byte[] pPattern, long pStart) throws SQLException 
        {
            return bFile.position(pPattern, pStart);
        }

        /**
         * {@inheritDoc}
         */
        public long position(Blob pPattern, long pStart) throws SQLException 
        {
            // TODO Auto-generated method stub
            return bFile.position(((BlobFromBFILE)pPattern).bFile, pStart);
        }

        /**
         * {@inheritDoc}
         */
        public int setBytes(long pPos, byte[] pBytes) throws SQLException 
        {
            throw new UnsupportedOperationException("Changing BFILE is not supported.");
        }

        /**
         * {@inheritDoc}
         */
        public int setBytes(long pPos, byte[] pBytes, int offset, int len) throws SQLException 
        {
            throw new UnsupportedOperationException("Changing BFILE is not supported.");
        }

        /**
         * {@inheritDoc}
         */
        public OutputStream setBinaryStream(long pos) throws SQLException 
        {
            throw new UnsupportedOperationException("Changing BFILE is not supported.");
        }

        /**
         * {@inheritDoc}
         */
        public void truncate(long len) throws SQLException 
        {
            throw new UnsupportedOperationException("Changing BFILE is not supported.");
        }

        /**
         * {@inheritDoc}
         */
        public void free() throws SQLException 
        {
            throw new UnsupportedOperationException("Changing BFILE is not supported.");
        }

    }  // BlobFromBFILE
    
}   // OracleDBAccess
