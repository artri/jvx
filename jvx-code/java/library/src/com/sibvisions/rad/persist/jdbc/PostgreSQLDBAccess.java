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
 * 30.05.2009 - [RH] - creation.
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 27.03.2010 - [JR] - #92: default value support 
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views.   
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented        
 * 21.07.2011 - [RH] - #436: OracleDBAccess and PostgresDBAccess should translate JVx quotes in specific insert                      
 * 18.11.2011 - [RH] - #510: All XXDBAccess should provide a SQLException format method 
 *                     #511: Postgres DBManipulation schema & table detection in getColumnMetaData fails
 * 16.12.2011 - [JR] - #498: enum detection supported                      
 *                   - #528: createStorage
 * 22.12.2011 - [RH] - #532: Like/Equals bug in Postgres fixed.
 * 08.05.2012 - [JR] - #575: convert value(s) to database specific value(s) 
 * 08.09.2013 - [RH] - #787: PostgreSQLDBAccess connect error
 * 12.04.2014 - [JR] - #1007: fixed enum detection
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used                            
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
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

import javax.rad.io.IFileHandle;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverse;
import javax.rad.model.condition.LikeReverseIgnoreCase;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.server.IConfiguration;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;

import org.postgresql.util.PGobject;

import com.sibvisions.rad.persist.jdbc.ServerMetaData.PrimaryKeyType;
import com.sibvisions.rad.persist.jdbc.param.AbstractParam;
import com.sibvisions.rad.persist.jdbc.param.OutParam;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.GroupHashtable;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.CaseSensitiveType;

/**
 * The <code>PostgreSQLDBAccess</code> is the implementation for Postgres databases.<br>
 * <br>
 * The last change detection for meta data has to be compiled as superuser.
 * So it is probably not created automatically, if the current connected user is not a super user.<br><br>
 * To support this, the following script has to be compiled as superuser into the database:
 * <pre><code>
 * create or replace function track_meta_data_change() returns event_trigger as $$ 
 * begin
 *   update track_meta_data_change set last_change = current_timestamp; 
 *   if not found then
 *     insert into track_meta_data_change (last_change) values (current_timestamp); 
 *   end if; 
 * end;
 * $$ language plpgsql;
 * 
 * create event trigger track_meta_data_change on ddl_command_end 
 *   when tag in ('CREATE TABLE', 'ALTER TABLE', 'CREATE VIEW') 
 * execute procedure track_meta_data_change();
 * 
 * create table track_meta_data_change (last_change timestamp);
 * </code></pre>                    
 *  
 * @see com.sibvisions.rad.persist.jdbc.DBAccess
 * 
 * @author Roland Hörmann
 */
public class PostgreSQLDBAccess extends DBAccess
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The user list to be ignored. */
    public static final String IGNORE_USER_LIST =   "'information_schema', 'pg_catalog'";

    /** the enum datatype. */
    public static final int TYPE_ENUM = -900;

    /** Checks when the last meta data was changed. */
    private static String sMetaDataChangedSelect = "select c.last_change " +
                                                     "from track_meta_data_change c";
    
    /** The select statement to get the Primary, Unique and Foreign keys in Postgres. */
    private static String sConstraintSelect = 
           "select c.table_schema as table_schem " + 
                 ",c.constraint_name " + 
                 ",substr(c.constraint_type, 1, 1) as constraint_type " + 
                 ",c.table_name " + 
                 ",rc.r_table_schem " + 
                 ",rc.r_constraint_name " + 
                 ",cc.column_name " + 
            "from information_schema.table_constraints c " + 
                 "join information_schema.key_column_usage cc on c.table_schema = cc.table_schema and c.table_name = cc.table_name and c.constraint_name = cc.constraint_name " + 
                 "left join (select fkn.nspname as table_schema, fkc.relname as table_name, con.conname as constraint_name " + 
                                  ",pkn.nspname as r_table_schem, pkic.relname as r_constraint_name " + 
                              "from pg_catalog.pg_namespace fkn " + 
                                   "join pg_catalog.pg_class fkc on fkc.relnamespace = fkn.oid " + 
                                   "join pg_catalog.pg_constraint con on con.conrelid = fkc.oid and con.contype = 'f' " + 
                                   "join pg_catalog.pg_class pkc on pkc.oid = con.confrelid " + 
                                   "join pg_catalog.pg_namespace pkn on pkn.oid = pkc.relnamespace " + 
                                   "join pg_catalog.pg_depend dep on dep.objid = con.oid " +
                                                                "and dep.classid = 'pg_constraint'::regclass::oid and dep.refclassid = 'pg_class'::regclass::oid " + 
                                   "join pg_catalog.pg_class pkic on pkic.oid = dep.refobjid and pkic.relkind = 'i' " + 
                           ") rc on c.table_schema = rc.table_schema and c.table_name = rc.table_name and c.constraint_name = rc.constraint_name " + 
            "order by c.constraint_schema, c.table_name, c.constraint_name, rc.r_constraint_name, cc.ordinal_position"; 
//           "select c.table_schema as table_schem " +
//                 ",c.constraint_name" +
//                 ",c.constraint_type " +
//                 ",c.table_name " +
//                 ",rc.unique_constraint_schema as r_table_schem" +
//                 ",rc.unique_constraint_name as r_constraint_name" +
//                 ",cc.column_name " +
//             "from information_schema.table_constraints c " +
//                  "join information_schema.key_column_usage cc on c.constraint_schema = cc.constraint_schema and c.constraint_name = cc.constraint_name " +
//                  "left join information_schema.referential_constraints rc on c.constraint_schema = rc.constraint_schema and c.constraint_name = rc.constraint_name " +
//            "order by c.constraint_schema, c.constraint_name, cc.ordinal_position";

    /** The select statement to get the Check Constraint in Postgres. */
    private static String sCheckConstraintSelect = 
           "select tc.table_schema as table_schem " +
                 ",tc.table_name " +
                 ",cc.check_clause " + 
             "from information_schema.table_constraints tc " + 
                  "join information_schema.check_constraints cc " + 
                    "on tc.constraint_catalog = cc.constraint_catalog and tc.constraint_schema = cc.constraint_schema and  tc.constraint_name = cc.constraint_name " + 
            "where tc.constraint_name not like '%not_null'"; 

    /** The select statement to get the Check Constraint in Postgres. */
    private static String sEnumSelect = 
           "select t.typname " + 
                 ",e.enumlabel " + 
             "from pg_catalog.pg_type t " + 
                  "join pg_catalog.pg_enum e on e.enumtypid = t.oid " + 
            "where t.typtype = 'e' " +
            "order by t.typname, e.enumsortorder"; 
    /** The select statement to get the Check Constraint in Postgres. */
    private static String sEnumSelect8 = 
            "select t.typname " + 
                  ",e.enumlabel " + 
              "from pg_catalog.pg_type t " + 
                   "join pg_catalog.pg_enum e on e.enumtypid = t.oid " + 
             "where t.typtype = 'e' " +
             "order by t.typname"; 

    /** the select statement to get the default values in Postgres. */
    private static String sDefaultValueSelect = 
            "select c.table_schema as table_schem " + 
                  ",c.table_name " + 
                  ",c.column_name " + 
                  ",c.data_type " + 
                  ",c.column_default as data_default " + 
              "from information_schema.columns c " + 
             "where c.column_default is not null"; 

    /** the select statement to get the column meta data in Postgres. */
    private static String sColumnMetaDataSelect = 
            "select c.table_schema as table_schem " + 
                  ",case when c.table_schema = any (current_schemas(false)) then 'Y' else 'N' end is_search_path " +
                  ",c.table_name " + 
                  ",c.column_name " + 
                  ",case when c.domain_name is not null then 2001 " + 
                        "when c.udt_name in ('name', 'text', 'varchar') then 12 " + 
                        "when c.udt_name in ('char', 'bpchar') then 1 " + 
                        "when c.udt_name = 'numeric' then 2 " + 
                        "when c.udt_name in ('int4', 'serial') then 4 " + 
                        "when c.udt_name in ('int2', 'smallserial') then 5 " + 
                        "when c.udt_name = 'float4' then 7 " + 
                        "when c.udt_name = 'float8' then 8 " + 
                        "when substr(c.udt_name, 1, 9) = 'timestamp' then 93 " + 
                        "when c.udt_name = 'date' then 91 " + 
                        "when c.udt_name = 'bytea' then -2 " + 
                        "when c.udt_name in ('int8', 'oid', 'bigserial') then -5 " + 
                        "when c.udt_name = 'bool' then -7 " + 
                        "when c.udt_name = 'xml' then 2009 " + 
                        "when substr(c.udt_name, 1, 1) = '_' then 2003 " + 
                        "when substr(c.udt_name, 1, 3) in ('pg_', 'reg') or " + 
                             "c.udt_name in ('abstime', 'anyarray', 'inet', 'int2vector', 'interval', 'oidvector', 'xid', 'json') then 1111 " + 
                        "else 12 " +
                   "end as data_type " + 
                  ",case when c.domain_name is null then c.udt_name else '\"'||c.domain_schema||'\".\"'||c.domain_name||'\"' end as type_name " + 
                  ",case when c.is_nullable = 'NO' then 0 else 1 end as nullable " + 
                  ",case when c.udt_name = 'date' then 13 " + 
                        "when substr(c.udt_name, 1, 9) = 'timestamp' then " + 
                              "case when c.datetime_precision = 0 then 22 else 23 + c.datetime_precision end " + 
                   "else coalesce(c.character_maximum_length, ceil(c.numeric_precision * ln(c.numeric_precision_radix) / ln(10)), 0) " +
                   "end as column_size " + 
                  ",coalesce(c.numeric_scale, c.datetime_precision, 0) as decimal_digits " + 
                  ",case when lower(c.column_default) like 'nextval(%)' then 'Y' else 'N' end as is_autoincrement " + 
              "from information_schema.columns c " + 
             "where c.table_schema not in (" + IGNORE_USER_LIST + ") " + 
             "order by c.table_schema, c.table_name, c.ordinal_position";
    
    /** True, if the procedure check was already performed. */
    protected static GroupHashtable<String, String, Boolean> ghtHasTrackMetaDataChangeTrigger = new GroupHashtable<String, String, Boolean>();

    /** Cache of <code>ForeignKey</code>'s to improve performance. */
    private static GroupHashtable<String, String, Hashtable<String, List<String>>> ghtCheckValuesCache = new GroupHashtable<String, String, Hashtable<String, List<String>>>();

    /** Cache of <code>ForeignKey</code>'s to improve performance. */
    private static GroupHashtable<String, String, Object[]> ghtEnumCache = new GroupHashtable<String, String, Object[]>();

    /** Global setting for creating fast long procedures. */
    private static boolean globalCreateTrackMetaDataChangeTrigger = true;

    /** Uses limit/ offset for fetch, to get faster query response. */
    private boolean limitFetchEnabled = true;
    
    /** Prevents fetching byte arrays immediately. The byte array is fetched later similar to blobs. */
    private boolean blobSimulationEnabled = true;
    
    /** The maximum fetch amount when using limit paging. */
    private int maximumFetchAmount = 10000;

    /** The maximum number of rows to fetch when doing an additional fetch. */
    private int maximumAdditionalFetchAmount = -1;
    
    /** Enable/ disable creation of meta data trigger. */
    private boolean createTrackMetaDataChangeTrigger;
    
    /** 
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice. 
     */
    private int minimumFetchAmount = 60;
    
    /** detection, if the current query uses autoLimit. */
    private boolean autoLimit = false;
    /** detection, if the current query uses autoLimit. */
    private boolean firstCallOfParameterizedSelectStatement = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        registerCache(ghtCheckValuesCache);
        registerCache(ghtEnumCache);
//        registerCache(ghtHasTrackMetaDataChangeTrigger); // We check user rights only once during a static life cycle.
    }
    
    /**
     * Constructs a new OracleDBAccess Object.
     */
    public PostgreSQLDBAccess()
    {
        setDriver("org.postgresql.Driver");
        setDBProperty("stringtype", "unspecified");
        
        setUseSavepoints(true);
        
        IConfiguration cfg = SessionContext.getCurrentServerConfig();
        
        if (cfg != null)
        {
            String sCaching = cfg.getProperty("/server/globalcreatetrackmetadatachangetrigger", "ON");

            setGlobalCreateTrackMetaDataChangeTrigger("ON".equals(sCaching));
        }
        
        setCreateTrackMetaDataChangeTrigger(isGlobalCreateTrackMetaDataChangeTrigger()); 
    }
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseSpecificLockStatement(String pWritebackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException
    {
        return super.getDatabaseSpecificLockStatement(pWritebackTable, pServerMetaData, pPKFilter) + " NOWAIT";                                         
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] insertDatabaseSpecific(String pWritebackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
                                           Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
        return insertPostgres(pWritebackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
    {
        getAllDefaultValues();
        
        String identifier = getIdentifier();
        String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable.toLowerCase());
        
        Map<String, Object> defaultValues =  getDefaultValuesCache(identifier).get(tableIdentifier);
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
    public SQLException formatSQLException(SQLException pSqlException)
    {
        return formatSQLException(pSqlException, pSqlException.getMessage(), pSqlException.getSQLState());
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
    protected int setColumnsToStore(PreparedStatement pInsert, ServerColumnMetaData[] pServerColumnMetaData, 
                                    int[] iaWriteables, Object[] pNew, Object[] pOld) throws DataSourceException
    {
        int i = 1;
        for (int j = 0; j < iaWriteables.length; j++)
        {
            int k = iaWriteables[j];
            ServerColumnMetaData scmd = pServerColumnMetaData[k];
            if (scmd.getDataType().compareTo(pNew[k], pOld == null ? null : pOld[k]) != 0)
            {
                try
                {
                    Object value = convertValueToDatabaseSpecificObject(pNew[k]);
                    
                    if (value instanceof IFileHandle)
                    {
                        value = FileUtil.getContent(((IFileHandle)value).getInputStream());
                    }
                    
                    if (scmd.getDetectedType() == TYPE_ENUM
                            || scmd.getSQLType() == Types.ARRAY
                            || scmd.getSQLType() == Types.STRUCT
                            || scmd.getSQLType() == Types.SQLXML)
                    {
                       pInsert.setObject(i, value, Types.OTHER);
                    }
                    else
                    {
                        // PostgresSQL jdbc feature, specify always the SQLType. 
                        pInsert.setObject(i, value, scmd.getSQLType());
                    }
                    
                    i++;
                }
                catch (Exception sqlException)
                {
                    if (sqlException instanceof SQLException)
                    {
                        sqlException = formatSQLException((SQLException)sqlException);
                    }
                    throw new DataSourceException("Set value into PreparedStatement failed!", sqlException);
                }
            }
        }
        return --i;
    }
    
    /* 
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoQuote(String pName)
    {
        CaseSensitiveType type = StringUtil.getCaseSensitiveType(pName);
        
        return type != CaseSensitiveType.LowerCase || !isValidIdentifier(pName);
    }   
    
    /* 
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String pUsername)
    {
        String sUserName = pUsername;
        if (pUsername != null)
        {
            // PostgreSQL has case sensitive Usernames during connect; 
            // without quoting the Username in create USER, the username is default lowercase
            // to prevent Connect FATAL errors, lowercase unquoted Usernames!
            if (sUserName.equals(DBAccess.removeQuotes(pUsername)))
            {
                sUserName = sUserName.toLowerCase();
            }
            else
            {
                sUserName = DBAccess.removeQuotes(pUsername);
            }
        }
        super.setUsername(sUserName);
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

        if (columnMetaDataCache == null || tableInfoCache == null) // first change is checked, to initialize last check time.
        {
            long lMillis = System.currentTimeMillis();

            columnMetaDataCache = new Hashtable<String, ServerColumnMetaData[]>();
            tableInfoCache = new Hashtable<String, TableInfo>();
            setColumnMetaDataCache(identifier, columnMetaDataCache);
            setTableInfoCache(identifier, tableInfoCache);

            try
            {
                List<Bean> allColumnMetaData = executeQuery(sColumnMetaDataSelect);
                
                String catalog = getConnectionIntern().getCatalog();

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
                        column.setAutoIncrement("Y".equals(columnMetaData.get("IS_AUTOINCREMENT")));
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
                                
                                TableInfo tableInfo = new TableInfo(catalog, lastTableSchem, lastTableName);
                                
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
                            catch (Throwable ex)
                            {
                                debug("Couldn't preload MetaData for \"", // Maybe there is a table with unsupported column types
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

    /* 
     * {@inheritDoc}
     */
    @Override
    protected ServerColumnMetaData[] getColumnMetaDataIntern(String pFromClause, 
                                                             String[] pQueryColumns,
                                                             String pBeforeQueryColumns, 
                                                             String pWhereClause, 
                                                             String pAfterWhereClause) throws DataSourceException
    {
        ServerColumnMetaData[] scmd = super.getColumnMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, 
                                                                    pAfterWhereClause);
        
//        checkMetaDataChange(); // super.getColumnMetaDataIntern calls directly getAndStoreMetaDataIntern, where checkMetaDataChange() is called.
        
        String identifier = getIdentifier();
        
        Hashtable<String, Object[]> enumCache = ghtEnumCache.get(identifier);
        
        if (enumCache == null)
        {
            long lMillis = System.currentTimeMillis();

            enumCache = new Hashtable<String, Object[]>();
            
            try
            {
                List<Bean> allEnumData = executeQuery(getConnectionIntern().getMetaData().getDatabaseMajorVersion() > 8 ? sEnumSelect : sEnumSelect8); 
                
                String oldTypname = null;
                ArrayList<Object> labels = new ArrayList<Object>();
                                
                for (Bean enumData : allEnumData)
                {
                    String typname      = (String)enumData.get("TYPNAME");
                    Object enumlabel    = enumData.get("ENUMLABEL");

                    if (oldTypname != null && !typname.equals(oldTypname))
                    {
                        enumCache.put(oldTypname, labels.toArray());
                        labels.clear();
                    }
                    
                    labels.add(enumlabel);
                    
                    oldTypname = typname;
                }
                if (oldTypname != null)
                {
                    enumCache.put(oldTypname, labels.toArray());
                }
            }
            catch (SQLException sqlException)
            {
                throw new DataSourceException("Enums couldn't be determined from database!", formatSQLException(sqlException));
            }
            ghtEnumCache.put(identifier, enumCache);

            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getAllEnums in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
        }
        
        //ENUM detection
        for (ServerColumnMetaData sdm : scmd)
        {
            int iType = sdm.getSQLType(); 
            
            if (iType == Types.VARCHAR || iType == Types.OTHER)
            { 
                String sTypeName = sdm.getSQLTypeName();
                
                Object[] enumValues = ghtEnumCache.get(identifier, sTypeName);

                if (enumValues != null)
                {
                    sdm.setDetectedType(TYPE_ENUM);
                    sdm.setAllowedValues(enumValues);
                }
            }
        }
        
        return scmd;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
    {
        TableInfo tableInfo = super.getTableInfoIntern(pWriteBackTable);
        
        if (tableInfo.getSchema() == null || tableInfo.getCatalog() == null)
        {
            String identifier = getIdentifier();
            String tableName  = tableInfo.getTable();

            TableInfo tI = getTableInfoCache(identifier).get(createIgnoreCaseIdentifier(quote(tableName)));

            if (tI == null)
            {
                String schema = tableInfo.getSchema();
                if (schema == null)
                {
                    try
                    {
                        List<Object> result = executeSql("select table_schema " +
                                                      "from information_schema.tables " +
                                                      "where table_name = ? and table_schema = any (current_schemas(false))", tableName);
                        if (result == null)
                        {
                            schema = "public";
                        }
                        else
                        {
                            schema = (String)result.get(0);
                        }
                    }
                    catch (Exception e)
                    {
                        schema = "public";
                        
                        error(e);
                    }
                }
                String catalog = tableInfo.getCatalog();
                if (catalog == null)
                {
                    try
                    {
                        catalog = getConnectionIntern().getCatalog();
                    }
                    catch (SQLException e)
                    {
                        error(e);
                    }
                }
                tableInfo = new TableInfo(catalog, schema, tableName);
            }
            else
            {
                tableInfo = tI;
            }
        }
        
        return tableInfo;
    }

    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected Object convertDatabaseSpecificObjectToValue(ServerColumnMetaData pColumnMetaData, Object pValue) throws SQLException
    {
        if (pValue instanceof PGobject)
        {
            return ((PGobject)pValue).getValue();
        }
        
        return pValue;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected void initializeDataType(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, ColumnMetaData pColumnMetaData) throws SQLException, DataSourceException
    {
        int columnType = pResultSetMetaData.getColumnType(pResultSetColumnIndex);

        switch (columnType)
        {
            case Types.OTHER:
            case Types.STRUCT:
            case Types.ARRAY:
                pColumnMetaData.setTypeIdentifier(StringDataType.TYPE_IDENTIFIER);
                pColumnMetaData.setPrecision(Integer.MAX_VALUE);
                break;
            default:
                super.initializeDataType(pResultSetMetaData, pResultSetColumnIndex, pColumnMetaData);
        }
        
        // Here is API missing, to be able, to tell the client, that the column is not sortable
//        String typeName = pResultSetMetaData.getColumnTypeName(pResultSetColumnIndex);
//        if ("xml".equals(typeName) || "json".equals(typeName))
//        {
//            // column meta data should not be sortable...
//        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createWhereParam(ServerMetaData pServerMetaData, CompareCondition pCompare)
    {
        if (pCompare != null)
        {
            ServerColumnMetaData scmd;
            int sqlType;
            int detectType;
            try
            {
                scmd = pServerMetaData.getServerColumnMetaData(pCompare.getColumnName());
                sqlType = scmd.getSQLType();
                detectType = scmd.getDetectedType();
            }
            catch (ModelException me)
            {
                scmd = null;
                sqlType = Types.OTHER;
                detectType = Types.OTHER;
            }
            
            Object oValue = pCompare.getValue();

            if ((!(oValue instanceof String) 
                    && (!isTypeEqual(scmd, pCompare)
                        || pCompare instanceof LikeReverse 
                        || pCompare instanceof LikeReverseIgnoreCase 
                        || pCompare instanceof Like 
                        || pCompare instanceof LikeIgnoreCase))
                 || detectType == TYPE_ENUM
                 || sqlType == Types.ARRAY
                 || sqlType == Types.STRUCT
                 || sqlType == Types.SQLXML)
            {
                return "cast(? as varchar)";
            }
        }
        
        return super.createWhereParam(pServerMetaData, pCompare);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createWhereColumn(ServerMetaData pServerMetaData, CompareCondition pCompare, String pColumnName)
    {
        if (pCompare != null)
        {
            ServerColumnMetaData scmd;
            int sqlType;
            int detectType;
            try
            {
                scmd = pServerMetaData.getServerColumnMetaData(pCompare.getColumnName());
                sqlType = scmd.getSQLType();
                detectType = scmd.getDetectedType();
            }
            catch (ModelException me)
            {
                scmd = null;
                sqlType = Types.OTHER;
                detectType = Types.OTHER;
            }
                
            if ((sqlType != Types.VARCHAR 
                    && (!isTypeEqual(scmd, pCompare)
                        || pCompare instanceof LikeReverse 
                        || pCompare instanceof LikeReverseIgnoreCase 
                        || pCompare instanceof Like 
                        || pCompare instanceof LikeIgnoreCase))
                 || detectType == TYPE_ENUM
                 || sqlType == Types.ARRAY
                 || sqlType == Types.STRUCT
                 || sqlType == Types.SQLXML)
            {
                return "cast(" + pColumnName + " as varchar)";
            }
        }
        
        return super.createWhereColumn(pServerMetaData, pCompare, pColumnName);
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
            if (pSchema == null || "public".equals(pSchema))
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
    protected Object getObjectFromResultSet(ResultSet pResultSet, int pIndex, ServerColumnMetaData pColumnMetaData) throws SQLException
    {
        if (pColumnMetaData.getTypeIdentifier() == BinaryDataType.TYPE_IDENTIFIER)
        {
            Object oValue = pResultSet.getObject(pIndex);
            
            if (oValue instanceof byte[])
            {
                return oValue;
            }
            else if (oValue instanceof Number)
            {
                return new PostgresBlob(((Number)oValue).intValue()); // ensure that a BlobFileHandle is created
            }
            else
            {
                return null;
            }
        }
        else
        {
            return super.getObjectFromResultSet(pResultSet, pIndex, pColumnMetaData);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getObjectFromResultSet(ResultSet pResultSet, int pIndex) throws SQLException
    {
        Object oValue = pResultSet.getObject(pIndex);

        if (oValue instanceof byte[])
        {
            return new PostgresBlob((byte[])oValue);
        }
        else
        {
            return super.getObjectFromResultSet(pResultSet, pIndex);
        }
    }
    
    /**
     * Returns true, if the clause contains a order by or a limit.
     * 
     * @param pFromClause the from clause
     * @param pWhereClause the where clause
     * @param pAfterWhereClause the after where clause
     * @param pSort the sort
     * @param pOrderByClause the order by clause
     * @param pAfterOrderByClause the after order by clause
     * @param pCheckContainsOrderBy if order by clause should be checked.
     * @return if the clause contains a order by or a limit.
     */
    protected boolean hasLimitOrOrderBy(String pFromClause, 
            String pWhereClause, 
            String pAfterWhereClause, 
            SortDefinition pSort, 
            String pOrderByClause,
            String pAfterOrderByClause,
            boolean pCheckContainsOrderBy)
    {
        StringBuilder clauseBuilder = new StringBuilder();
        
        if (pFromClause != null)
        {
            clauseBuilder.append(replaceWhitespace(pFromClause));
        }
        if (pWhereClause != null)
        {
            clauseBuilder.append(" ");
            clauseBuilder.append(replaceWhitespace(pWhereClause));
        }
        if (pAfterWhereClause != null)
        {
            clauseBuilder.append(" ");
            clauseBuilder.append(replaceWhitespace(pAfterWhereClause));
        }
        if (pSort == null && pOrderByClause != null)
        {
            clauseBuilder.append(" ");
            clauseBuilder.append(replaceWhitespace(pOrderByClause));
        }
        if (pAfterOrderByClause != null)
        {
            clauseBuilder.append(" ");
            clauseBuilder.append(replaceWhitespace(pAfterOrderByClause));
        }
        
        String clause = clauseBuilder.toString().toLowerCase();
        
        return clause.contains(" limit ") || (pCheckContainsOrderBy && clause.contains(" order by "));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object[]> fetch(
            ServerMetaData pServerMetaData,
            String pBeforeQueryColumns,
            String[] pQueryColumns,
            String pFromClause,
            ICondition pFilter,
            String pWhereClause,
            String pAfterWhereClause,
            SortDefinition pSort,
            String pOrderByClause,
            String pAfterOrderByClause,
            int pFromRow,
            int pMinimumRowCount,
            boolean pAllowLazyFetch) throws DataSourceException
    {
        long start = System.currentTimeMillis();
        
        if (getMaxTime() > 0 && pFromRow > 0 && pMinimumRowCount > 0)
        {
            // If there is a max time set we will fetch dynamically more rows.
            // This is needed because of the MySQL only supports the limit
            // clause. Which means that every fetch afterwards will have to
            // reselect already fetched lines to get the new once.
            // The fetch time would accumulate to a quite big value, so we are
            // fetching *way* more rows to keep it low.
            pMinimumRowCount = Math.max(pMinimumRowCount, pFromRow); 
            
            if (maximumFetchAmount > 0 && pMinimumRowCount > maximumFetchAmount)
            {
                pMinimumRowCount = maximumFetchAmount;
            }
        }
        if (pMinimumRowCount > 2 && getMaxTime() > 0) // Only, if it is not a refetch row or row count.
        {
            pMinimumRowCount = Math.max(pMinimumRowCount, minimumFetchAmount); 
        }

        if (blobSimulationEnabled && pAllowLazyFetch && pQueryColumns != null 
                && pServerMetaData.getPrimaryKeyColumnIndices() != null                       // Pk columns are needed, to fetch blob lazy!
                && pServerMetaData.getPrimaryKeyType() == PrimaryKeyType.PrimaryKeyColumns)   // Only in real PK mode DBAccess will map the blob, 
        {                                                                                     // because only then all values are not null
            HashMap<String, String> replaceBinaryColumns = new HashMap<String, String>();
            
            for (ServerColumnMetaData col : pServerMetaData.getServerColumnMetaData())
            {
                if (col != null && col.getTypeIdentifier() == BinaryDataType.TYPE_IDENTIFIER && col.getColumnMetaData().isFetchLargeObjectsLazy())
                {
                    String replaceColumn = "length(" + col.getRealQueryColumnName() + ") as " + col.getName();
                    replaceBinaryColumns.put(col.getRealQueryColumnName(), replaceColumn);
                    replaceBinaryColumns.put(col.getName(), replaceColumn);
                }
            }
            for (int i = 0; i < pQueryColumns.length; i++)
            {
                String replaceColumn = replaceBinaryColumns.get(pQueryColumns[i]);
                if (replaceColumn != null)
                {
                    pQueryColumns[i] = replaceColumn;
                }
            }
        }
        
//        // Only use primary key as sort, if there is no other sort defined. This allows to use default sort, to define own sort.
//        // The sort has to be added, for a postgres bug, that takes limit into account of optimizer, so the rows fetched may change
//        // randomly depending on fetched page.
//        if (limitFetchEnabled
//                && pMinimumRowCount > 2 // Only, if it is not a refetch row or row count.
//                && pServerMetaData.getPrimaryKeyColumnNames() != null
//                && pSort == null 
//                && StringUtil.isEmpty(pOrderByClause)
//                && !hasLimitOrOrderBy(pFromClause, pWhereClause, pAfterWhereClause, pSort, pOrderByClause, true))
//        {
//            pSort = new SortDefinition(pServerMetaData.getPrimaryKeyColumnNames());
//        }
        // Always append primary key to sort. No chance, this could be overruled.
        if (limitFetchEnabled 
                && pMinimumRowCount > 2 // Only, if it is not a refetch row or row count. 
                && pServerMetaData.getPrimaryKeyColumnNames() != null 
                && (pSort != null || StringUtil.isEmpty(pOrderByClause)) 
                && !hasLimitOrOrderBy(pFromClause, pWhereClause, pAfterWhereClause, pSort, pOrderByClause, pAfterOrderByClause, true))
        {
            if (pSort != null)
            {
                boolean[] origAscending = pSort.isAscending();  
                pSort = new SortDefinition(ArrayUtil.addAll(pSort.getColumns(), ArrayUtil.removeAll(pServerMetaData.getPrimaryKeyColumnNames(), pSort.getColumns())));
                boolean[] newAscending = pSort.isAscending();
                System.arraycopy(origAscending, 0, newAscending, 0, origAscending.length); // isAscending delivers directly the internal array, so it is changed. 
            }
            else
            {
                pSort = new SortDefinition(pServerMetaData.getPrimaryKeyColumnNames());
            }
        }
        
        firstCallOfParameterizedSelectStatement = true;
        List<Object[]> fetched = super.fetch(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                pAfterOrderByClause,
                pFromRow,
                pMinimumRowCount,
                pAllowLazyFetch);
        
        if (autoLimit
                && pMinimumRowCount > 2 // do not fetch further, as it is only for refetch row, row count or meta data
                && fetched.size() == pMinimumRowCount + 1
                && fetched.get(fetched.size() - 1) == null)
        {
            // Remove the null termination, there might be more rows to fetch.
            fetched.remove(fetched.size() - 1);
            
            long fetchTime = Math.max(1, System.currentTimeMillis() - start);
            
            if (// pMinimumRowCount > 2 && // do not fetch further, as it is only for refetch row, row count or meta data
                                           // the check moved up as there should in general not be fetched afterwards
                fetchTime < getMaxTime())
            {
                // If the first fetch took less time than the maximum fetch
                // time, we will fetch more rows.
                int nextFetchRowCount = calculateAdditionalFetchRowCount(fetched.size(), fetchTime);

                if (maximumFetchAmount > 0 && nextFetchRowCount > maximumFetchAmount)
                {
                    nextFetchRowCount = maximumFetchAmount;
                }
                
                if (maximumAdditionalFetchAmount > 0 && nextFetchRowCount > maximumAdditionalFetchAmount)
                {
                    nextFetchRowCount = maximumAdditionalFetchAmount;
                }

                if (nextFetchRowCount > 0)
                {
                    List<Object[]> additionalFetched = super.fetch(
                            pServerMetaData,
                            pBeforeQueryColumns,
                            pQueryColumns,
                            pFromClause,
                            pFilter,
                            pWhereClause, 
                            pAfterWhereClause,
                            pSort,
                            pOrderByClause,
                            pAfterOrderByClause,
                            pFromRow + fetched.size(),
                            nextFetchRowCount,
                            pAllowLazyFetch);

                    // Check if the additional fetched data is null terminated
                    // even though there might be more data which could be fetched.
                    if (additionalFetched.size() == nextFetchRowCount + 1
                            && additionalFetched.get(additionalFetched.size() - 1) == null)
                    {
                        // Remove the null termination.
                        additionalFetched.remove(additionalFetched.size() - 1);
                    }
                    
                    fetched.addAll(additionalFetched);
                }
            }
        }
        
        return fetched;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterizedStatement getParameterizedSelectStatement(
            ServerMetaData pServerMetaData,
            String pBeforeQueryColumns,
            String[] pQueryColumns,
            String pFromClause,
            ICondition pFilter,
            String pWhereClause,
            String pAfterWhereClause,
            SortDefinition pSort,
            String pOrderByClause,
            String pAfterOrderByClause,
            int pFromRow,
            int pMinimumRowCount) throws DataSourceException
    {
        ParameterizedStatement statement = super.getParameterizedSelectStatement(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                pAfterOrderByClause,
                pFromRow,
                pMinimumRowCount);
        
        boolean autoLimitDetect = limitFetchEnabled && !hasLimitOrOrderBy(pFromClause, pWhereClause, pAfterWhereClause, pSort, pOrderByClause, pAfterOrderByClause, false);
        if (autoLimitDetect)
        {
            if (pFromRow > 0 || pMinimumRowCount >= 0) // Only, if it is not a refetch row or row count.
            {
                // This workaround does not work, to avoid the Postgres limit fetch bug.
//                statement.setStatement("select * from (" + statement.getStatement() + " limit all offset 0) m limit ? offset ?");
                statement.setStatement(statement.getStatement() + " limit ? offset ?");
                statement.getValues().add(BigDecimal.valueOf(pMinimumRowCount > 0 ? pMinimumRowCount : Integer.MAX_VALUE));
                statement.getValues().add(BigDecimal.valueOf(pFromRow > 0 ? pFromRow : 0));
            }
            else
            {
                autoLimitDetect = false;
            }
        }
        if (firstCallOfParameterizedSelectStatement)
        {
            firstCallOfParameterizedSelectStatement = false;
            autoLimit = autoLimitDetect;
        }

        return statement;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int getDiscardRowCount(int pFromRow, int pMinimumRowCount)
    {
        return 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected NamedParameterDefinition findNamedParameterDefinition(String pStatement, int pStart, boolean pUpperCase)
    {
        if (pStatement.charAt(pStart) == ':')
        {
            //we support special type casting
            //
            //e.g. '-'::varchar
            NamedParameterDefinition result = super.findNamedParameterDefinition(pStatement, pStart + 1, pUpperCase);
            
            result.start = pStart;
            result.valid = false;
            
            return result;
        }
        else
        {
            return super.findNamedParameterDefinition(pStatement, pStart, pUpperCase);
        }
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSQL(ServerMetaData pServerMetaData, ICondition pCondition, boolean pUseRealColumnName) throws DataSourceException
    {
        String result = super.getSQL(pServerMetaData, pCondition, pUseRealColumnName);
        
        if (pCondition instanceof CompareCondition)
        {
            CompareCondition cCompare = (CompareCondition)pCondition;
            Object           oValue   = cCompare.getValue();
            
            if (!cCompare.isIgnoreNull() && oValue == null)
            {
                if (pServerMetaData != null)
                {
                    try
                    {
                        if (pServerMetaData.getServerColumnMetaData(cCompare.getColumnName()).getTypeIdentifier() == StringDataType.TYPE_IDENTIFIER)
                        {
                            return "(" + result + " OR " + result.replace(" IS NULL", " = ''") + ")";
                        }
                    }
                    catch (ModelException modelException)
                    {
                        // Column doesn't exit in MetaData, then use the column name of the Condition itself.
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object executeFunction(String pFunctionName, OutParam pReturnOutParam, Object... pParameters) throws SQLException
    {
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_FUNCTION);
        
        try
        {
            if (pParameters == null)
            {
                pParameters = new Object[1];
            }
            
            if (record != null)
            {
                if (pParameters != null && pParameters.length > 0)
                {
                    record.setParameter(pFunctionName, pParameters);
                }
                else
                {
                    record.setParameter(pFunctionName);
                }
            }
            
            checkIsOpen();

            ArrayList<Object> liInParameters = new ArrayList<Object>();
            ArrayList<AbstractParam> liOutParameters = new ArrayList<AbstractParam>();

            StringBuilder sqlStatement = new StringBuilder("SELECT * FROM ");
            sqlStatement.append(pFunctionName);
            
            sqlStatement.append("(");
            if (pParameters != null && pParameters.length > 0)
            {
                for (int i = 0; i < pParameters.length; i++)
                {
                    if (AbstractParam.isInParam(pParameters[i]))
                    {
                        if (i > 0)
                        {
                            sqlStatement.append(", ");
                        }
                        sqlStatement.append("?");
                        
                        liInParameters.add(pParameters[i]);
                    }
                    if (AbstractParam.isOutParam(pParameters[i]))
                    {
                        liOutParameters.add((AbstractParam)pParameters[i]);
                    }
                }
            }
            sqlStatement.append(")");
            
            List<Bean> result = executeQuery(sqlStatement.toString(), liInParameters.toArray(new Object[liInParameters.size()]));
            
            pReturnOutParam.setValue(result); // return result as fallback 
            
            if (result.size() > 0)
            {
                Bean resultParams = result.get(0);
                BeanType resultTypes = resultParams.getBeanType();
                
                if (liOutParameters.size() > 0)
                {
                    for (int i = 0, count = Math.min(resultTypes.getPropertyCount(), liOutParameters.size()); i < count; i++)
                    {
                        liOutParameters.get(i).setValue(resultParams.get(i));
                    }
                    
                    pReturnOutParam.setValue(resultParams);
                }
                else if (result.size() == 1 && resultTypes.getPropertyCount() == 1
                        && removeQuotes(pFunctionName).equalsIgnoreCase(resultTypes.getPropertyDefinition(0).getName()))
                {
                    pReturnOutParam.setValue(resultParams.get(0));
                }
            }
                
            return pReturnOutParam.getValue();
        }
        catch (Exception e)
        {
            throw newSQLException(record, e);
        }
        finally
        {
            CommonUtil.close(record);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeProcedure(String pProcedureName, Object... pParameters) throws SQLException
    {
        executeFunction(pProcedureName, new OutParam(Types.OTHER), pParameters);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Date getLastMetaDataChange() throws Exception
    {
        if (hasTrackMetaDataChangeTrigger())
        {
            return (Date)executeQuery(sMetaDataChangedSelect).get(0).get(0);
        }
        else
        {
            return null;
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Checks, if there are triggers to detect last meta data change.<br>
     * <br>
     * If trigger and table will not be created automatically the following script can be used:<br>
     * This will only work as superuser for now. No Workaround.
     * <pre>
     * <code>
     * create or replace function track_meta_data_change() returns event_trigger as $$ 
     * begin
     *   update track_meta_data_change set last_change = current_timestamp; 
     *   if not found then
     *     insert into track_meta_data_change (last_change) values (current_timestamp); 
     *   end if; 
     * exception when others then 
     *   null; 
     * end;
     * $$ language plpgsql;
     * 
     * create event trigger track_meta_data_change on ddl_command_end 
     *   when tag in ('CREATE TABLE', 'ALTER TABLE', 'CREATE VIEW') 
     * execute procedure track_meta_data_change();
     * 
     * create table track_meta_data_change (last_change timestamp);
     * </code>
     * </pre>
     * 
     * @return true, if there are triggers to detect last meta data change.
     */
    protected boolean hasTrackMetaDataChangeTrigger()
    {
        String identifier = getIdentifier();
        
        Boolean hasTrigger = ghtHasTrackMetaDataChangeTrigger.get(identifier, "");
        if (hasTrigger == null)
        {
            hasTrigger = Boolean.TRUE;

            try 
            {
                executeQuery(sMetaDataChangedSelect);
            }
            catch (Exception ex)
            {
                if (createTrackMetaDataChangeTrigger)
                {
                    try
                    {
                        try
                        {
                            executeStatement("drop function track_meta_data_change() cascade");
                        }
                        catch (Exception e)
                        {
                            // Do nothing
                        }
                        try
                        {
                            executeStatement("drop table track_meta_data_change");
                        }
                        catch (Exception e)
                        {
                            // Do nothing
                        }
    
                        executeStatement(
                                "create or replace function track_meta_data_change() returns event_trigger as $$\n" + 
                                "begin\n" + 
                                "  update track_meta_data_change set last_change = current_timestamp;\n" + 
                                "  if not found then\n" + 
                                "    insert into track_meta_data_change (last_change) values (current_timestamp);\n" + 
                                "  end if;\n" +
                                "exception when others then\n" + 
                                "  null;\n" + 
                                "end;\n" + 
                                "$$ language plpgsql;\n");
                        executeStatement(
                                "create event trigger track_meta_data_change on ddl_command_end\n" + 
                                "  when tag in ('CREATE TABLE', 'ALTER TABLE', 'CREATE VIEW')\n" + 
                                "execute procedure track_meta_data_change();\n");
                        executeStatement(
                                "create table track_meta_data_change (last_change timestamp)");
                    }
                    catch (Exception ex2)
                    {
                        try
                        {
                            executeStatement("drop function track_meta_data_change() cascade");
                        }
                        catch (Exception e)
                        {
                            // Do nothing
                        }

                        hasTrigger = Boolean.FALSE;
                    }
                }
                else
                {
                    hasTrigger = Boolean.FALSE;
                }
            }
            
            ghtHasTrackMetaDataChangeTrigger.put(identifier, "", hasTrigger);
        }
        
        return hasTrigger.booleanValue();
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
        
        if (primaryKeyCache == null || uniqueKeyCache == null || foreignKeyCache == null)
        {
            long lMillis = System.currentTimeMillis();

            primaryKeyCache = new Hashtable<String, Key>();
            uniqueKeyCache = new Hashtable<String, List<Key>>();
            foreignKeyCache = new Hashtable<String, List<ForeignKey>>();

            try
            {
                List<Bean> allConstraintData = executeQuery(sConstraintSelect); 
                
                String catalog = getConnectionIntern().getCatalog();
                
                String oldTableSchem        = null;
                String oldConstraintName    = null;
                String oldConstraintType    = null;
                String oldTableName         = null;
                String oldRTableSchem       = null;
                String oldRConstraintName   = null;

                ArrayUtil<Name> auKeyColumns = new ArrayUtil<Name>();
                Map<String, Name[]> constraintColumns = new HashMap<String, Name[]>();
                Map<String, String[]> constraintSchemaTable = new HashMap<String, String[]>();
                List<String[]> foreignKeyConstraints = new ArrayUtil<String[]>();
                
                for (int i = 0, size = allConstraintData.size(); i <= size; i++)
                {
                    Bean constraintData    = i < size ? allConstraintData.get(i) : new Bean();
                    String tableSchem      = (String)constraintData.get("TABLE_SCHEM");
                    String constraintName  = (String)constraintData.get("CONSTRAINT_NAME");
                    String tableName       = (String)constraintData.get("TABLE_NAME");
                    String rConstraintName = (String)constraintData.get("R_CONSTRAINT_NAME");
                    String columnName      = (String)constraintData.get("COLUMN_NAME");
                    
                    if (i == size || !tableSchem.equals(oldTableSchem) || !constraintName.equals(oldConstraintName)
                            || !tableName.equals(oldTableName) || !CommonUtil.equals(rConstraintName, oldRConstraintName))
                    {
                        if (auKeyColumns.size() > 0)
                        {
                            String tableIdentifier = createIdentifier(catalog, oldTableSchem, oldTableName);
                            
                            Name[] columns = auKeyColumns.toArray(new Name[auKeyColumns.size()]);
                            auKeyColumns.clear();
                            
                            String fullConstraintName = oldTableSchem + "." + oldConstraintName;
                            constraintColumns.put(fullConstraintName, columns);
                            constraintSchemaTable.put(fullConstraintName, new String[] {oldTableSchem, oldTableName});
    
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
                                String fullRConstraintName = oldRTableSchem + "." + oldRConstraintName;
                                
                                foreignKeyConstraints.add(new String[] {tableIdentifier, oldConstraintName, fullConstraintName, fullRConstraintName});
                            }
                        }
    
                        oldTableSchem      = tableSchem;
                        oldConstraintName  = constraintName;
                        oldConstraintType  = (String)constraintData.get("CONSTRAINT_TYPE");
                        oldTableName       = tableName;
                        oldRTableSchem     = (String)constraintData.get("R_TABLE_SCHEM");
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
        
        if (checkValueCache == null)
        {
            long lMillis = System.currentTimeMillis();

            checkValueCache = new Hashtable<String, Hashtable<String, List<String>>>();
            
            try
            {
                List<Bean> allConstraintData = executeQuery(sCheckConstraintSelect); 
                
                String catalog = getConnectionIntern().getCatalog();
                
                for (Bean constraintData : allConstraintData)
                {
                    String tableSchem   = (String)constraintData.get("TABLE_SCHEM");
                    String tableName    = (String)constraintData.get("TABLE_NAME");
                    String checkClause  = (String)constraintData.get("CHECK_CLAUSE");
                
                    String tableIdentifier = createIdentifier(catalog, tableSchem, tableName);
                    Hashtable<String, List<String>> checkValue = checkValueCache.get(tableIdentifier);
                    
                    String columnName = parseColumnName(checkClause);
                    if (columnName != null)
                    {
                        List<String> values = parseValues(checkClause);
                        
                        if (values != null)
                        {
                            if (checkValue == null)
                            {
                                checkValue = new Hashtable<String, List<String>>();
                                
                                checkValueCache.put(tableIdentifier, checkValue);
                            }
                            
                            checkValue.put(columnName, values);
                        }
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
        
        if (defaultValuesCache == null)
        {
            long lMillis = System.currentTimeMillis();

            defaultValuesCache = new Hashtable<String, Map<String, Object>>();
            
            try
            {
                List<Bean> allDefaultValueData = executeQuery(sDefaultValueSelect); 
                
                String catalog = getConnectionIntern().getCatalog();

                for (Bean defaultValueData : allDefaultValueData)
                {
                    String tableSchem      = (String)defaultValueData.get("TABLE_SCHEM");
                    String tableName       = (String)defaultValueData.get("TABLE_NAME");
                    String columnName      = ((String)defaultValueData.get("COLUMN_NAME")).toUpperCase();
                    String dataType        = (String)defaultValueData.get("DATA_TYPE");
                    String dataDefault     = (String)defaultValueData.get("DATA_DEFAULT");

                    String tableIdentifier = createIdentifier(catalog, tableSchem, tableName);
                    
                    Map<String, Object> defaultValues = defaultValuesCache.get(tableIdentifier);
                    
                    int type = Types.VARCHAR;
                    if (dataType.contains("date") || dataType.contains("time") || dataType.contains("interval"))
                    {
                        type = Types.DATE;
                    }
                    else if (dataType.contains("num") || dataType.contains("real") || dataType.contains("int"))
                    {
                        type = Types.DECIMAL;
                    }
                    else if (dataType.contains("bool"))
                    {
                        type = Types.BOOLEAN;
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
                                if ("public".equals(tableSchem))
                                {
                                    defaultValuesCache.put(createIdentifier(null, null, tableName), defaultValues);
                                }
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
     * Returns the newly inserted row from an Postgres Database. <br>
     * It uses RETURNING .. INTO to get the primary key values back from the database. 
     * 
     * @param pTablename        the table to use for the insert
     * @param pInsertStatement  the SQL Statement to use for the insert
     * @param pServerMetaData   the meta data to use.
     * @param pNewDataRow       the new IDataRow with the values to insert
     * @param pDummyColumn      true, if all writeable columns are null, but for a correct INSERT it have
     *                          to be minimum one column to use in the syntax.
     * @return the newly inserted row from an Postgres Database.
     * @throws DataSourceException
     *             if an <code>Exception</code> occur during insert the <code>IDataRow</code> 
     *             to the storage
     */ 
    private Object[] insertPostgres(String pTablename, String pInsertStatement, ServerMetaData pServerMetaData,
                                    Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
        int[] pPKColumnIndices = pServerMetaData.getPrimaryKeyColumnIndices();

        boolean returnPK = false;
        
        if (pPKColumnIndices != null)
        {
            // If there is at least one empty (null) PK column, we will return
            // all PK values.
            for (int i = 0; i < pPKColumnIndices.length && !returnPK; i++)
            {
                returnPK = (pNewDataRow[pPKColumnIndices[i]] == null);
            }
        }
    
        if (returnPK)
        {
            StringBuffer sInsertStatement = new StringBuffer(pInsertStatement);
            
            // use RETURNING to get all PK column values filled in in from the trigger
            sInsertStatement.append(" RETURNING ");
            
            for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
            {
                if (i > 0)
                {
                    sInsertStatement.append(", ");
                }
                
                sInsertStatement.append(pServerMetaData.getServerColumnMetaData(pPKColumnIndices[i]).getColumnName().getQuotedName());
            }
            
            pInsertStatement = sInsertStatement.toString();
        }
        
        CallableStatement csInsert = null;
        ResultSet rsResult = null;
        try
        {
            // #436 - OracleDBAccess and PostgresDBAccess should translate JVx quotes in specific insert
            String sSQL = translateQuotes(pInsertStatement);
            debug("executeSQL->", sSQL);
            csInsert = getConnectionIntern().prepareCall(sSQL);
        
            ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
            int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
            
            if (pDummyColumn == null)
            {
                setColumnsToStore(csInsert, cmdServerColumnMetaData, iaWriteables, pNewDataRow, null);
            }
            else
            {
                for (int i = 0; i < cmdServerColumnMetaData.length; i++)
                {
                    if (cmdServerColumnMetaData[i].getColumnName().getQuotedName().equals(pDummyColumn))
                    {
                        csInsert.setObject(1, null, cmdServerColumnMetaData[i].getSQLType());
                        break;
                    }
                }                   
            }

            setSavepoint();
            
            if (returnPK)
            {
                rsResult = csInsert.executeQuery();
            
                // use RETURNING to get the PK column values filled in by the trigger
                // get the out parameters, and set the PK columns
                if (rsResult.next())
                {
                    for (int i = 0; pPKColumnIndices != null && i < pPKColumnIndices.length; i++)
                    {
                        pNewDataRow[pPKColumnIndices[i]] = rsResult.getObject(i + 1);
                    }
                }
            }
            else
            {
                csInsert.executeUpdate();
            }
            
            return pNewDataRow;
        }
        catch (SQLException sqlException)
        {           
            rollbackToSavepoint();
            throw new DataSourceException("Insert failed! - " + pInsertStatement, formatSQLException(sqlException));
        }   
        finally
        {
            releaseSavepoint();
            
            CommonUtil.close(rsResult, csInsert);
        }
    }

    /**
     * Calculates the amount of rows to fetch additionally to an already happened fetch.
     * 
     * @param pFetchedRowCount the already fetched row count.
     * @param pFetchTimeInMilliSeconds the time the fetch took, in milliseconds.
     * @return the amount of rows to additionally fetch. Can be zero or less to fetch nothing.
     */
    protected int calculateAdditionalFetchRowCount(int pFetchedRowCount, long pFetchTimeInMilliSeconds)
    {
        return (int)(pFetchedRowCount * (getMaxTime() - pFetchTimeInMilliSeconds) / pFetchTimeInMilliSeconds);
    }
    
    /**
     * Uses limit/ offset for fetch, to get faster query response.
     * It is enabled by default.
     * 
     * @return if limit/ offset for paging is enabled.
     */
    public boolean isLimitFetchEnabled()
    {
        return limitFetchEnabled;
    }

    /**
     * Uses limit/ offset for fetch, to get faster query response.
     * It is enabled by default.
     * 
     * @param pLimitPagingEnabled true to enable limit/ offset for paging.
     */
    public void setLimitFetchEnabled(boolean pLimitPagingEnabled)
    {
        limitFetchEnabled = pLimitPagingEnabled;
    }

    /**
     * Prevents fetching byte arrays immediately. The byte array is fetched later similar to blobs.
     * Default this is enabled.
     * 
     * @return if blobs are simulated.
     */
    public boolean isBlobSimulationEnabled()
    {
        return blobSimulationEnabled;
    }

    /**
     * Prevents fetching byte arrays immediately. The byte array is fetched later similar to blobs.
     * Default this is enabled.
     * 
     * @param pBlobSimulationEnabled true to simulate blobs.
     */
    public void setBlobSimulationEnabled(boolean pBlobSimulationEnabled)
    {
        blobSimulationEnabled = pBlobSimulationEnabled;
    }

    /**
     * Global enable or disable creation track meta data change trigger.
     * 
     * @return if track meta data change trigger are created.
     */
    public static boolean isGlobalCreateTrackMetaDataChangeTrigger()
    {
        return globalCreateTrackMetaDataChangeTrigger;
    }

    /**
     * Global enable or disable creation track meta data change trigger.
     * 
     * @param pEnabled if track meta data change trigger are created.
     */
    public static void setGlobalCreateTrackMetaDataChangeTrigger(boolean pEnabled)
    {
        globalCreateTrackMetaDataChangeTrigger = pEnabled;
    }
    
    /**
     * Enable or disable creation track meta data change trigger.
     * 
     * @return if track meta data change trigger are created.
     */
    public boolean isCreateTrackMetaDataChangeTrigger()
    {
        return createTrackMetaDataChangeTrigger;
    }

    /**
     * Enable or disable creation track meta data change trigger.
     * 
     * @param pEnabled if track meta data change trigger are created.
     */
    public void setCreateTrackMetaDataChangeTrigger(boolean pEnabled)
    {
        createTrackMetaDataChangeTrigger = pEnabled;
    }

    /**
     * Enable or disable creation track meta data change trigger.
     * 
     * @return if track meta data change trigger are created.
     * @deprecated use {@link #isCreateTrackMetaDataChangeTrigger()}
     */
    @Deprecated
    public boolean isCreateTrackMetaDataChangeTriggerEnabled()
    {
        return isCreateTrackMetaDataChangeTrigger();
    }

    /**
     * Enable or disable creation track meta data change trigger.
     * 
     * @param pEnabled if track meta data change trigger are created.
     * @deprecated use {@link #setCreateTrackMetaDataChangeTrigger(boolean)}
     */
    @Deprecated
    public void setCreateTrackMetaDataChangeTriggerEnabled(boolean pEnabled)
    {
        setCreateTrackMetaDataChangeTrigger(pEnabled);
    }

    /**
     * The maximum fetch amount when using limit paging.
     * 
     * @return the maximum fetch amount
     */
    public int getMaximumFetchAmount()
    {
        return maximumFetchAmount;
    }

    /**
     * The maximum fetch amount when using limit paging.
     * 
     * @param pMaximumFetchAmount maximum fetch amount
     */
    public void setMaximumFetchAmount(int pMaximumFetchAmount)
    {
        maximumFetchAmount = pMaximumFetchAmount;
    }

    /**
     * Gets the maximum amount of additional rows to fetch.
     * 
     * @return the maximum amount of additional rows to fetch.
     * @see #setMaximumAdditionalFetchAmount(int)
     */
    public int getMaximumAdditionalFetchAmount()
    {
        return maximumAdditionalFetchAmount;
    }

    /**
     * Sets the maximum amount of additional rows to fetch.
     * <p>
     * If a fetch took less than {@link #getMaxTime()}, there will be
     * a second, additional fetch issues to fetch more data. This allows
     * to set an upper limit on that second, additional fetch.
     * <p>
     * When set to {@code 0}, no additional data will be fetched. When
     * set to {@code -1} or less, there will be no limit imposed.
     * 
     * @param pMaximumAdditionalFetchAmount the maximum amount for the additional
     *                                      fetch to fetch.
     * @see #getMaximumAdditionalFetchAmount()
     */
    public void setMaximumAdditionalFetchAmount(int pMaximumAdditionalFetchAmount)
    {
        maximumAdditionalFetchAmount = pMaximumAdditionalFetchAmount;
    }

    /**
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice.
     * 
     * @return the minimum fetch amount
     */
    public int getMinimumFetchAmount()
    {
        return minimumFetchAmount;
    }

    /**
     * The minimum fetch amount when using limit paging.
     * This is set to 60 to optimize read ahead for vaadin.
     * Vaadin tries to fetch 60 rows initially, and this prevents fetching twice.
     * 
     * @param pMinimumFetchAmount minimum fetch amount
     */
    public void setMinimumFetchAmount(int pMinimumFetchAmount)
    {
        minimumFetchAmount = pMinimumFetchAmount;
    }

    /**
     * Parses the column name of a check constraint. 
     * @param pCheckConstraint the check constraint.
     * @return the column name.
     */
    protected String parseColumnName(String pCheckConstraint)
    {
        //"(((active)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))"
        //"(((db)::text = ANY (ARRAY[('J'::character varying)::text, ('N'::character varying)::text])))"
        //"((mengeneinheit)::text = ANY (ARRAY[(NULL::character varying)::text, ('Zoll (inch)'::character varying)::text, 
        //   ('Quadratzoll'::character varying)::text, ('Kubikzoll'::character varying)::text, ('Performance unit'::character varying)::text, 
        //   ('Error'::character varying)::text, ('Layer'::character varying)::text, ('Acre'::character varying)::text, ('Kubikdezimeter'::character varying)::text, 
        //   ('Zentiliter'::character varying)::text, ('Zentimeter'::character varying)::text, ('Quadratzentimeter'::character varying)::text, 
        //   ('Kubikzentimeter'::character varying)::text, ('Dezimeter'::character varying)::text, ('Dutzend'::character varying)::text, ('each'::character varying)::text, 
        //   ('Flasche'::character varying)::text, ('Fluid Ounce US'::character varying)::text, ('Fuß'::character varying)::text, ('Quadratfuß'::character varying)::text, 
        //   ('Kubikfuß'::character varying)::text, ('Gramm'::character varying)::text, ('US-Gallone'::character varying)::text, ('Gross'::character varying)::text, 
        //   ('Stunde'::character varying)::text, ('Hektoliter'::character varying)::text, ('Kanister'::character varying)::text, ('Karton'::character varying)::text, 
        //   ('Kilogramm'::character varying)::text, ('Kiste'::character varying)::text, ('Kilometer'::character varying)::text, ('Quadratkilometer'::character varying)::text, 
        //   ('Kasten'::character varying)::text, ('Kilotonne'::character varying)::text, ('Liter'::character varying)::text, ('Pfund (Pound)'::character varying)::text, 
        //   ('Leistungseinheit'::character varying)::text, ('Länge pro Stück'::character varying)::text, ('Meter'::character varying)::text, 
        //   ('Quadratmeter'::character varying)::text, ('Kubikmeter'::character varying)::text, ('Millifarad'::character varying)::text, 
        //   ('Milligramm'::character varying)::text, ('Meile'::character varying)::text, ('Quadratmeile'::character varying)::text, 
        //   ('Milliliter'::character varying)::text, ('Millimeter'::character varying)::text, ('Quadratmillimeter'::character varying)::text, 
        //   ('Kubikmillimeter'::character varying)::text, ('Nanoampere'::character varying)::text, ('Nanometer'::character varying)::text, 
        //   ('Nanofarad'::character varying)::text, ('Unze'::character varying)::text, ('Paar'::character varying)::text, ('Pack'::character varying)::text, 
        //   ('Palette'::character varying)::text, ('Pikofarad'::character varying)::text, ('Parts per billion'::character varying)::text, ('Rolle'::character varying)::text, 
        //   ('Stück'::character varying)::text, ('Tonne'::character varying)::text, ('US-Tonne'::character varying)::text, ('Trommel'::character varying)::text, 
        //   ('Tausend'::character varying)::text, ('Tüte'::character varying)::text, ('Wertartikel'::character varying)::text, ('kg Kupfer'::character varying)::text, 
        //   ('Yard'::character varying)::text, ('Quadrat Yard'::character varying)::text, ('Kubik Yard'::character varying)::text, ('kg Edelstahl'::character varying)::text, 
        //   ('Gitterbox'::character varying)::text, ('Stück (mit Dezimalen)'::character varying)::text, ('Satz'::character varying)::text, 
        //   ('Zeitstück'::character varying)::text, ('Mikroampere'::character varying)::text]))"
        //"intern = ANY (ARRAY['J'::bpchar, 'N'::bpchar])"

        int index = pCheckConstraint.indexOf('=');
        if (index >= 0)
        {
            String columnString = pCheckConstraint.substring(0, index).trim();
            String definitionString = pCheckConstraint.substring(index + 1).trim();
            
            if (definitionString.toLowerCase().startsWith("any") 
                    && definitionString.length() > 3 && !Character.isLetterOrDigit(definitionString.charAt(3)))
            {
                int tIndex = columnString.indexOf("::");
                if (tIndex >= 0)
                {
                    columnString = columnString.substring(0, tIndex).trim();
                }
                return columnString.replace("(", "").replace(")", "").toUpperCase();
            }
        }
        
        return null;
    }
    
    /**
     * Parses the value list of a check constraint. Only char, varchar and numbers are supported for now. 
     * @param pCheckConstraint the check constraint.
     * @return the value list.
     */
    protected List<String> parseValues(String pCheckConstraint)
    {
        //"(((active)::text = ANY ((ARRAY['Y'::character varying, 'N'::character varying])::text[])))"
        //"(((todo_anzeigen)::text = ANY ((ARRAY['J'::character varying, 'N'::character varying])::text[])))"
        //"(((db)::text = ANY (ARRAY[('J'::character varying)::text, ('N'::character varying)::text])))"
        //"((mengeneinheit)::text = ANY (ARRAY[(NULL::character varying)::text, ('Zoll (inch)'::character varying)::text, 
        //   ('Quadratzoll'::character varying)::text, ('Kubikzoll'::character varying)::text, ('Performance unit'::character varying)::text, 
        //   ('Error'::character varying)::text, ('Layer'::character varying)::text, ('Acre'::character varying)::text, ('Kubikdezimeter'::character varying)::text, 
        //   ('Zentiliter'::character varying)::text, ('Zentimeter'::character varying)::text, ('Quadratzentimeter'::character varying)::text, 
        //   ('Kubikzentimeter'::character varying)::text, ('Dezimeter'::character varying)::text, ('Dutzend'::character varying)::text, ('each'::character varying)::text, 
        //   ('Flasche'::character varying)::text, ('Fluid Ounce US'::character varying)::text, ('Fuß'::character varying)::text, ('Quadratfuß'::character varying)::text, 
        //   ('Kubikfuß'::character varying)::text, ('Gramm'::character varying)::text, ('US-Gallone'::character varying)::text, ('Gross'::character varying)::text, 
        //   ('Stunde'::character varying)::text, ('Hektoliter'::character varying)::text, ('Kanister'::character varying)::text, ('Karton'::character varying)::text, 
        //   ('Kilogramm'::character varying)::text, ('Kiste'::character varying)::text, ('Kilometer'::character varying)::text, ('Quadratkilometer'::character varying)::text, 
        //   ('Kasten'::character varying)::text, ('Kilotonne'::character varying)::text, ('Liter'::character varying)::text, ('Pfund (Pound)'::character varying)::text, 
        //   ('Leistungseinheit'::character varying)::text, ('Länge pro Stück'::character varying)::text, ('Meter'::character varying)::text, 
        //   ('Quadratmeter'::character varying)::text, ('Kubikmeter'::character varying)::text, ('Millifarad'::character varying)::text, 
        //   ('Milligramm'::character varying)::text, ('Meile'::character varying)::text, ('Quadratmeile'::character varying)::text, 
        //   ('Milliliter'::character varying)::text, ('Millimeter'::character varying)::text, ('Quadratmillimeter'::character varying)::text, 
        //   ('Kubikmillimeter'::character varying)::text, ('Nanoampere'::character varying)::text, ('Nanometer'::character varying)::text, 
        //   ('Nanofarad'::character varying)::text, ('Unze'::character varying)::text, ('Paar'::character varying)::text, ('Pack'::character varying)::text, 
        //   ('Palette'::character varying)::text, ('Pikofarad'::character varying)::text, ('Parts per billion'::character varying)::text, ('Rolle'::character varying)::text, 
        //   ('Stück'::character varying)::text, ('Tonne'::character varying)::text, ('US-Tonne'::character varying)::text, ('Trommel'::character varying)::text, 
        //   ('Tausend'::character varying)::text, ('Tüte'::character varying)::text, ('Wertartikel'::character varying)::text, ('kg Kupfer'::character varying)::text, 
        //   ('Yard'::character varying)::text, ('Quadrat Yard'::character varying)::text, ('Kubik Yard'::character varying)::text, ('kg Edelstahl'::character varying)::text, 
        //   ('Gitterbox'::character varying)::text, ('Stück (mit Dezimalen)'::character varying)::text, ('Satz'::character varying)::text, 
        //   ('Zeitstück'::character varying)::text, ('Mikroampere'::character varying)::text]))"
        //"intern = ANY (ARRAY['J'::bpchar, 'N'::bpchar])"
        try
        {
            int startIndex = pCheckConstraint.indexOf("(ARRAY[") + 7;
            if (startIndex >= 7)
            {
                int endIndex = pCheckConstraint.indexOf("])", startIndex);
                if (endIndex >= 0)
                {
                    ArrayList<String> result = new ArrayList<String>();
                    
                    List<String> values = StringUtil.separateList(pCheckConstraint.substring(startIndex, endIndex), ",", true);
                    
                    for (String value : values)
                    {
                        if (value.startsWith("("))
                        {
                            value = value.substring(1);
                        }
                        if (value.endsWith(")"))
                        {
                            value = value.substring(0, value.length() - 1);
                        }
                        List<String> valueParts = StringUtil.separateList(value, "::", true);
                        
                        String valueStr = valueParts.get(0);
                        //String type = valueParts.get(1);
                        
//                      if (valueStr.startsWith("'") && valueStr.endsWith("'"))
//                      {
//                          result.add(valueStr.substring(1, valueStr.length() - 1));
//                      }
//                      else // if it is not a String, we try Number, and thats it.
//                      {
//                          result.add(new BigDecimal(valueStr));
//                      }
                        
                        result.add(valueStr);
                    }
                    
                    return result;
                }
            }
        }
        catch (Exception ex)
        {
            // Silent Ignore parsing failure
        }
        
        return null;
    }

    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * Blob Simulation for Postgres.
     *  
     * @author Martin Handsteiner
     */
    public static class PostgresBlob implements ILazyFetchBlob
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /** the content. */
        private byte[] content = null;
        /** the length. */
        private long length = 0;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
        
        /**
         * Constructs a new PostgresBlob with length.
         * @param pLength the length
         */
        public PostgresBlob(long pLength)
        {
            length = pLength;
        }

        /**
         * Constructs a new PostgresBlob with content.
         * @param pContent the content
         */
        public PostgresBlob(byte[] pContent)
        {
            content = pContent;
            if (content != null)
            {
                length = content.length;
            }
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
        
        /**
         * {@inheritDoc}
         */
        @Override
        public long length() throws SQLException
        {
            return length;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] getBytes(long pPos, int pLength) throws SQLException
        {
            if (content == null)
            {
                return null;
            }
            else
            {
                byte[] result = new byte[pLength];
                
                System.arraycopy(content, (int)pPos - 1, result, 0, pLength);
    
                return result;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public InputStream getBinaryStream() throws SQLException
        {
            if (content == null)
            {
                return null;
            }
            else
            {
                return new ByteArrayInputStream(content);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long position(byte[] pattern, long start) throws SQLException
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long position(Blob pattern, long start) throws SQLException
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int setBytes(long pos, byte[] bytes) throws SQLException
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public OutputStream setBinaryStream(long pos) throws SQLException
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void truncate(long len) throws SQLException
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void free() throws SQLException
        {
            content = null;
            length = 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public InputStream getBinaryStream(long pPos, long pLength) throws SQLException
        {
            if (content == null)
            {
                return null;
            }
            else
            {
                return new ByteArrayInputStream(getBytes(pPos, (int)pLength));
            }
        }
        
    }   // PostgresBlob
    
} // PostgreSQLDBAccess
