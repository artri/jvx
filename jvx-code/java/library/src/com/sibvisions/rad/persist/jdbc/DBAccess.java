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
 * 01.10.2008 - [RH] - creation.
 * 02.11.2008 - [RH] - extends now MemDataSource 
 * 12.11.2008 - [RH] - getPrimaryKey bug with Oracle fixed. (Databases without Catalogs)
 * 17.11.2008 - [RH] - setFilterParameters changes to allow null Parameters
 * 19.11.2008 - [RH] - filter redesign
 * 23.11.2008 - [RH] - Statement cache disabled, Oracle Insert with RETURNING added
 * 26.11.2008 - [RH] - lockAndRefetch added()
 * 27.11.2008 - [RH] - throw Exceptions optimized
 * 30.01.2009 - [RH] - fetch bug with SelectColumns fixed
 * 13.05.2009 - [RH] - reviewed, separated into one DBAccess for each different Database
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 * 14.07.2009 - [JR] - getSelectStatement: missing from clause exception throwed
 *                     insert, update, delete: missing writebackTable exception throwed
 *                     getDatabaseSpecificLockStatement: missing writebackTable exception throwed
 * 06.10.2009 - [RH] - supportsGetGeneratedKeys add, used in DerbyDBAccess to set it to true, because of the derby bug
 * 14.10.2009 - [RH] - sql to java type conversion added (->serializeable!)
 *                     bug fix CLOB, -> CLOB is interpreted as a StringType instead of BinaryTyp.
 *                     CLOB, BLOB size is default Integer.MAX_VALUE
 * 15.10.2009 - [JR] - fetch: pMinimumRowCount < 0                     
 * 23.10.2009 - [HM] - Properties changes to get/set username, url, driver, password                      
 * 23.10.2009 - [HM] - static getDBAccess added
 *            - [JR] - getDBAccess: checked null  
 * 27.10.2009 - [RH] - setNull, for empty insert/update [BUGFIX]
 * 23.11.2009 - [RH] - use of MetaData instead of MetaDataColumn[] and PK column String[].
 *                     code optimization      
 * 04.12.2009 - [RH] - QueryColumns with alias bug fixed            
 *                     getUKs, getPK return null if none is found. Exceptions will be logged!
 * 09.12.2009 - [JR] - set/getDBProperty implemented
 *                   - open, toString: hide username password form the property output
 * 02.03.2010 - [RH] - reorganized MetaData -> ServerMetaData, ColumnMetaData -> ServerColumnMetaData
 * 08.03.2010 - [RH] - if in update() is determined, that no values are changed, then it returns instead of null, 
 *                     the old value.
 * 10.03.2010 - [RH] - Exception didn't thrown with duplicate column names in getColumnMetaData()
 *                     Ticket #81 fixed
 * 13.03.2010 - [JR] - #90: fetch: handled different Number instances as BigDecimal          
 * 19.03.2010 - [RH] - if in update() no rows changed, then try to find that row, if that doesn't exist, then insert it.
 * 25.03.2010 - [JR] - #92: getDefaultValues, translateDefaultValue implemented            
 * 28.03.2010 - [JR] - #47: getAllowedValues implemented  
 * 30.03.2010 - [RH] - updateAnsiSQL() + updateDatabaseSpecific add for DB specific support.
 * 06.04.2010 - [JR] - #115: closed all ResultSets
 * 21.04.2010 - [JR] - createDataType replaced with (ServerColumnMetaData).getDataType()   
 * 18.06.2010 - [JR] - fetch: optimized type detection and reduced method calls in for loop        
 * 11.10.2010 - [RH] - #91: the automatic link column name is now build independent (foregnkey column name) and with an optional TranslationMap.
 * 20.10.2010 - [JR] - #188: UK detection returns an empty row if no UK was found     
 * 25.10.2010 - [JR] - #196: setConnection implemented and used in open/close
 *                   - #195: getDBAccess(java.sql.Connection) implemented
 * 19.11.2010 - [RH] - getUKs, getPKs return Type changed to a <code>Key</code> based result.     
 * 22.11.2010 - [RH] - executeSQL added.          
 * 23.11.2010 - [RH] - setSQLTypeName on ServerColumnMetaData used.   
 * 01.12.2010 - [RH] - getPKs set PKName corrected.
 * 14.12.2010 - [RH] - #222: getUKs return no PK anymore, just UKs
 * 15.12.2010 - [RH] - better logging in executeXXX methods.
 * 23.12.2010 - [RH] - #224: wrong AutomaticLinkColumnName fixed (if PK with one column, and FK with more the one column used, + all _ID are remove)
 *                                                                 duplicate use of ForeignKeys over partly the same columns fixed.   
 * 28.12.2010 - [RH] - #230: quoting of all DB objects like columns, tables, views. 
 * 03.01.2011 - [RH] - Schema detecting made better in getColumnMetaData()
 *                   - #232: get/setQuoting() on/off , 
 *                   - #136: Mysql PK refetch not working -> extract the plan table without schema.
 * 06.01.2011 - [JR] - #234: introduced ColumnMetaDataInfo 
 * 29.01.2011 - [JR] - #211: getDefaultAllowedValues implemented
 *                   - translateValue: check null explicitly
 * 16.02.2011 - [JR] - getQueryColumns: code review      
 * 11.03.2011 - [RH] - #308: DB specific automatic quoting implemented       
 * 25.03.2011 - [RH] - removeDBSpecificquotes added.   
 * 28.04.2011 - [RH] - #341:  LikeReverse Condition, LikeReverseIgnoreCase Condition   
 *                     createReplace for DB added. Standard implementation is ANSI SQL.
 * 06.06.2011 - [JR] - #381: fixed column name usage      
 * 12.07.2011 - [RH] - #420: DBStorage creates too long alias names for oracle: ORA-00972: Bezeichner ist zu lang      
 * 22.07.2011 - [RH] - #440: DBAccess.insert return null if no auto increment column in table
 * 31.07.2011 - [RH] - #447: if getMaxColumnNameLength() return 0, we should interprete it as unlimmited
 * 14.09.2011 - [JR] - #470: splitSchemaTable, set/getDefaultSchema implemented
 * 18.11.2011 - [RH] - #510: All XXDBAccess should provide a SQLException format method 
 * 22.11.2011 - [JR] - #515: open now checks the transaction isolation level
 * 14.12.2011 - [JR] - insert/update logging
 * 17.12.2011 - [JR] - #528: createStorage implemented
 * 30.01.2012 - [RH] - #544: In databases with default lower case column names, the default labels in the RowDefinition are wrong  -> setLabel removed!
 * 05.03.2012 - [HM] - #552:  Reverted createStorage, added functionality in DBAccess
 * 13.03.2012 - [RH] - #562: DBAccess getColumnMetaData() should determine writeable columns case insensitive
 * 08.05.2012 - [JR] - #575: convert value(s) to database specific value(s) 
 * 20.11.2012 - [JR] - #589: use JNDI for connection/dbaccess detection
 *                   - open now checks isOpen
 * 09.03.2012 - [JR] - getSQL: beautifying spaces                   
 * 18.03.2013 - [RH] - #632: DBStorage: Update on Synonym (Oracle) doesn't work - Synonym Support implemented
 * 11.07.2013 - [JR] - #727: insert checks PrimaryKeyType
 * 19.07.2013 - [RH] - #733: AfterWhereClause in DBStorage makes SQL Exception
 * 08.09.2013 - [JR] - #789: set url and username
 * 18.10.2013 - [RH] - #843: Not all FKs creates Auto Link CellEditors
 * 27.03.2014 - [JR] - set auto commit to true
 * 31.03.2014 - [JR] - #996: simple modification state
 * 06.05.2014 - [RZ] - #1029: added overload for fetch() which also accepts an ORDER BY string.
 * 15.05.2014 - [JR] - #1038: CommonUtil.close used   
 * 16.10.2014 - [JR] - #1141: setAutoCommit added       
 *                   - #1142: close now calls commit or rollback   
 * 13.12.2014 - [JR] - refactored JNDI handling a little bit (especiall DBCredentials support)
 * 15.01.2015 - [JR] - #1227: update metadata cache if necessary and temp-disabled
 * 06.02.1015 - [JR] - #1255: fixed properties  
 * 01.02.2016 - [JR] - #1751: numeric support for BIT type   
 * 02.02.2017 - [JR] - formatParameter introduced            
 * 27.11.2017 - [JR] - #1858: getMetaDataWhereClause implemented   
 * 09.02.2019 - [JR] - #1986: named parameters ignore case support     
 * 18.03.2019 - [JR] - #1999: fixed NPE in formatSQLException     
 * 04.10.2019 - [JR] - #2056: support additional JDBC types (2013 and 2014)
 * 20.02.2020 - [DJ] - #2207: is alive query
 * 24.02.2020 - [JR] - #2046: storage events
 * 17.03.2020 - [JR] - #2237: use all available parameters for BLOB selects
 */
package com.sibvisions.rad.persist.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.MetaDataCacheOption;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.And;
import javax.rad.model.condition.CompareCondition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.Greater;
import javax.rad.model.condition.GreaterEquals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.condition.Less;
import javax.rad.model.condition.LessEquals;
import javax.rad.model.condition.Like;
import javax.rad.model.condition.LikeIgnoreCase;
import javax.rad.model.condition.LikeReverse;
import javax.rad.model.condition.LikeReverseIgnoreCase;
import javax.rad.model.condition.Not;
import javax.rad.model.condition.OperatorCondition;
import javax.rad.model.condition.Or;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.BooleanDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.util.EventHandler;
import javax.rad.util.TranslationMap;
import javax.sql.DataSource;

import com.sibvisions.rad.model.Filter;
import com.sibvisions.rad.persist.AbstractCachedStorage;
import com.sibvisions.rad.persist.jdbc.ServerMetaData.PrimaryKeyType;
import com.sibvisions.rad.persist.jdbc.event.ConnectionEvent;
import com.sibvisions.rad.persist.jdbc.event.type.IClearMetaDataListener;
import com.sibvisions.rad.persist.jdbc.event.type.ICloseDBAccessListener;
import com.sibvisions.rad.persist.jdbc.event.type.IConfigureConnectionListener;
import com.sibvisions.rad.persist.jdbc.event.type.IOpenDBAccessListener;
import com.sibvisions.rad.persist.jdbc.event.type.IOpenDBStorageListener;
import com.sibvisions.rad.persist.jdbc.event.type.IUnconfigureConnectionListener;
import com.sibvisions.rad.persist.jdbc.param.AbstractParam;
import com.sibvisions.rad.persist.jdbc.param.OutParam;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.GroupHashtable;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.IValidatable;
import com.sibvisions.util.KeyValueList;
import com.sibvisions.util.ObjectCache;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.ILogger.LogLevel;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.CaseSensitiveType;

/**
 * The <code>DBAccess</code> is the implementation for most used SQL databases.<br>
 * Standard ANSI SQL Databases are anyways supported.<br>
 * Its used to read/write data between the storage and the <code>DataBook/DataPage</code>.
 * It has database type specific implementations.<br>
 * 
 * <br><br>Example:
 * <pre>
 * <code>
 * DBAccess dba = new DBAccess();
 * 
 * // set connect properties
 * dba.setDriver("org.hsqldb.jdbcDriver");
 * dba.setUrl("jdbc:hsqldb:file:testdbs/test/testdb");
 * dba.setUsername("sa");
 * dba.setPassword("");
 * 
 * Properties pDBProperties = new Properties();
 * pDBProperties.put("shutdown", true);
 * dba.setDBProperties(pDBProperties);
 * 
 * // open
 * dba.open();
 * 
 * To get Database independent DBAccess, it is better to use:
 * 
 * DBAccess dba = DBAccess.getDBAccess("jdbc:hsqldb:file:testdbs/test/testdb");
 * 
 * // insert data into test table
 * PreparedStatement psPreparedStatement = dba.getPreparedStatement(
 * 	             "insert into test (id, name) values(?,?)", false);
 * psPreparedStatement.setInt(1, 1);
 * psPreparedStatement.setString(2, "projectX");
 * dba.executeUpdate(psPreparedStatement);
 * 
 * // select data from test table
 * psPreparedStatement = dba.getPreparedStatement("select * from test", false);
 * ResultSet rs = dba.executeQuery(psPreparedStatement);
 * 
 * while (rs.next())
 * {
 *    System.out.println(rs.getInt("id") + "," + rs.getString("name"));
 * }
 * </code>
 * </pre>
 *  
 * @see com.sibvisions.rad.persist.jdbc.IDBAccess
 * @see com.sibvisions.rad.model.remote.RemoteDataBook
 * 
 * @author Roland H�rmann
 */
public class DBAccess implements IDBAccess,
                                 ICloseable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// jdk 1.6 jdbc types
    /** A jdbc VARCHAR DB column data type constant. */
    public static final int    NCHAR 			            	= -15;
    /** A jdbc VARCHAR DB column data type constant. */
    public static final int    NVARCHAR 		            	= -9;
    /** A jdbc VARCHAR DB column data type constant. */
    public static final int    LONGNVARCHAR 	            	= -16;
    /** A jdbc VARCHAR DB column data type constant. */
    public static final int    ROWID                            = -8;
    /** A jdbc BFILE DB column data type constant. */
    public static final int    BFILE 			            	= -13;
    /** A jdbc CURSOR data type constant. */
    public static final int    CURSOR                           = -10;
    /** A jdbc timestamp DB column data type constant. */
    public static final int    TIMESTAMP_WITH_TIMEZONE 	    	= -101;
    /** A jdbc timestamp DB column data type constant. */
    public static final int    TIMESTAMP_WITH_LOCAL_TIMEZONE 	= -102;
    /** A jdbc timestamp DB column data type constant. */
    public static final int    TIME_WITH_TIMEZONE_V8    		= 2013;
    /** A jdbc timestamp DB column data type constant. */
    public static final int    TIMESTAMP_WITH_TIMEZONE_V8    	= 2014;
        
	/** general DB quote character. will be translated in the DB specific. */
    public static final String QUOTE = "`";
    
    /** Pk Reference values, to identify the correct pk index in statements. */
    private static final Object[] PRIMARYKEY_REFERENCE_VALUES = new Object[] {new BigDecimal(-123456), new Timestamp(-123456), new String("-123456")};
    
    /** The pattern to replace whitespace. */
    private static final Pattern REPLACE_WHITESPACE = Pattern.compile("\\s+"); 
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// static 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The Default translation Map for the automatic link column name for the specified ForeignKey and FK Column in the master table.*/
    private static TranslationMap                                         tmpAutoLinkColumnNames  = new TranslationMap(); 
    
    /** Stores suitable DBAccess classes for jdbc urls. */
    private static Hashtable<String, Class<? extends DBAccess>>           dbAccessClasses = new Hashtable<String, Class<? extends DBAccess>>();
    
    /** Stores all available caches. */
    private static List<GroupHashtable>                                   lRegisteredCaches = new ArrayList<GroupHashtable>();
    
	/** the mapping between cache groups and the application. */ 
	private static KeyValueList<String, String>                           kvlApplicationGroups = new KeyValueList<String, String>(); 

	/** the DBAccess instances per identifier. */
	private static KeyValueList<String, WeakReference<DBAccess>>          kvlDBAccessByIdentifier = new KeyValueList<String, WeakReference<DBAccess>>();
	
	/** Cache of <code>ForeignKey</code>'s to improve performance. */
	private static GroupHashtable<String, String, List<ForeignKey>>	      ghtFKsCache = new GroupHashtable<String, String, List<ForeignKey>>();
	/** Cache of <code>PrimaryKey</code>'s to improve performance. */
	private static GroupHashtable<String, String, Key>	                  ghtPKCache = new GroupHashtable<String, String, Key>();
	/** Cache of <code>UniqueKey</code>'s to improve performance. */
	private static GroupHashtable<String, String, List<Key>>	          ghtUKsCache = new GroupHashtable<String, String, List<Key>>();
	/** Cache of <code>AllowedValues</code>'s to improve performance. */
	private static GroupHashtable<String, String, Map<String, Object[]>>  ghtAllowedValuesCache = new GroupHashtable<String, String, Map<String, Object[]>>();
	/** Cache of <code>DefaultValues</code>'s to improve performance. */
	private static GroupHashtable<String, String, Map<String, Object>>	  ghtDefaultValuesCache = new GroupHashtable<String, String, Map<String, Object>>();
	/** Cache of <code>ColumnMetaData</code>'s to improve performance. */
	private static GroupHashtable<String, String, ServerColumnMetaData[]> ghtColumnMetaDataCache = new GroupHashtable<String, String, ServerColumnMetaData[]>();
	/** Cache of <code>TableInfo</code>'s to improve performance. */
	private static GroupHashtable<String, String, TableInfo>	          ghtTableInfoCache = new GroupHashtable<String, String, TableInfo>();
	/** Cache of <code>TableInfo</code>'s to improve performance. */
	private static GroupHashtable<String, String, String>	              ghtTableNameCache = new GroupHashtable<String, String, String>();
    /** Cache for last meta data check. */
    private static GroupHashtable<String, String, Long>                   ghtLastMetaDataCheck = new GroupHashtable<String, String, Long>();
    /** Cache for last meta data check. */
    private static GroupHashtable<String, String, Date>                   ghtLastMetaDataChange = new GroupHashtable<String, String, Date>();

    /** The minimum meta data check intervall in seconds. */
    private static int defaultMinMetaDataCheckInterval = 15;
    
    /** The event handler for open DBAccess. */
    private static EventHandler<IOpenDBAccessListener>					eventOpenedDBAccess = null;
    /** The event handler for close DBAccess. */
    private static EventHandler<ICloseDBAccessListener>			  	    eventClosedDBAccess = null;
    /** The event handler for close DBAccess. */
    private static EventHandler<IClearMetaDataListener>			  	    eventClearedMetaData = null;

    /** Constant for FKS are null. */
	public static final List<ForeignKey>                                 FKS_NULL = Collections.EMPTY_LIST;
	/** Constant for PKS are null. */
	public static final Key                                              PKS_NULL = new Key(null, null); 
	/** Constant for UKS are null. */
	public static final List<Key>                                        UKS_NULL = Collections.EMPTY_LIST; 
	/** Constant for Allowed Values are null. */
	public static final Map<String, Object[]>                            ALLOWED_VALUES_NULL = Collections.EMPTY_MAP;
	/** Constant for Default Values are null. */
	public static final Map<String, Object>                              DEFAULT_VALUES_NULL = Collections.EMPTY_MAP;
	/** Constant for column meta data are null. */
	public static final ServerColumnMetaData[]                           COLUMNMETADATA_NULL = new ServerColumnMetaData[0];
	/** Constant for column meta data are null. */
	public static final TableInfo                                        TABLEINFO_NULL = new TableInfo(null, null, null);
	/** Constant for column meta data are null. */
	public static final String                                           TABLENAME_NULL = new String("TABLENAME_NULL"); // new String for unique reference
	
	/** The logger for protocol the performance. */
	private static ILogger                                                logger = null;

	/** Last queried state whether the metadata cache is global (not update everytime). */
	private static boolean                                                lastGlobalMetaDataCache = false;
	
    /** The default limit for what is considered a "large object". */
    private static long                                                   lDefaultLargeObjectLimit = 16384L;

    /** The default cursor cache timeout is 15 minutes for now. */
	private static long                                                   lDefaultCursorCacheTimeout = 900000L;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The column metadata creator provider. */
    private EventHandler<IColumnMetaDataCreator>            columnMetaDataCreatorProvider = null;
    /** The event handler for configuring connections. */
    private EventHandler<IConfigureConnectionListener>		eventConfigureConnection = null;
    /** The event handler for unconfiguring connections. */
    private EventHandler<IUnconfigureConnectionListener>	eventUnconfigureConnection = null;
    /** the before open DBSTorage listener. */
    private EventHandler<IOpenDBStorageListener>			eventBeforeOpenDBStorage = null;
    /** the after open DBSTorage listener. */
    private EventHandler<IOpenDBStorageListener>			eventAfterOpenDBStorage = null;
    

	/**
	 * The {@link Savepoint} that is currently being held.
	 * 
	 * @see #releaseSavepoint()
	 * @see #rollbackToSavepoint()
	 * @see #setSavepoint()
	 * @see #useSavepoints
	 */
	protected Savepoint 					currentSavepoint = null;
	
	/** The connection pool for creating connections. */
	private IConnectionPool					connectionPool;
	/** The jdbc connection to the database. */
	private Connection						connection;
	
    /** Used class of . */
    private IColumnMetaDataCreator          columnMetaDataCreator = null;

    /** the metadata cache option for this instance (default: Default). */
    private MetaDataCacheOption             mdcCacheOption = MetaDataCacheOption.Default;

    /** Database specific properties. */
    private Properties                      properties = new Properties();

    /** Cache of <code>ResultSet</code>'s to reuse in the next fetch. */
    private Hashtable<String, Cursor>       htFetchResultSetCache = new Hashtable<String, Cursor>();
    
    /** the date util for string to date conversion. */
    private DateUtil                        dateUtil = new DateUtil("yyyy-MM-dd HH:mm:ss");

    /** The jdbc connection string. */
	private String							sUrl;
	/** Name of the jdbc driver <code>Class</code>. */
	private String							sDriver;
	/** User name to connect to the database. */
	private String							sUsername;
	/** Password to connect to the database. */
	private String							sPassword;

    /** The open quote character for database operations. */
    private String 							sOpenQuote;
    /** The close quote character for database operations. */
    private String 							sCloseQuote;
    
    /** Sets the default schema for the case that schema is not automatically detected. */
    private String							sDefaultSchema;

    /** The current application name. */
    private String							sApplicationName = "";
    /** stores the identifier for this DBAccess configuration. */
    private String                          sIdentifier = null;

    /** The cached identifier for column metadata. */
    protected String						cachedMetaDataIdentifier = null;
    /** The cached columnMetaData. */
    protected ServerColumnMetaData[]		cachedColumnMetaData = null;
    /** The cached catalog info. */
    protected String						cachedCatalogInfo = null;
    /** The cached schema info. */
    protected String						cachedSchemaInfo = null;
    /** The cached table info. */
    protected String                        cachedTableInfo = null;

    /** The maximum time in milliseconds to use, to try to fetch all rows. reduce open cursors, and increase performance. */
    private int                             iMaxTime = 100;
    /** Query time out, which is used as limit for fetching data. */
    private int                             iQueryTimeout = -1;
    /** Transaction time out, which is used as limit for insert, update and delete. */
    private int                             iTransactionTimeout = -1;
    /** stores the max. column length in this database. */
	private int 							iMaxColumnLength;

	/** The limit which is used to determine if something is a "large object". */
	private long							lLargeObjectLimit = lDefaultLargeObjectLimit;
    /** whether the connection should be automatically closed. */
    private boolean                         bAutoClose = true;
    /** whether the connection is an external connection. */
    private boolean                         bExternalConnection = false;
    /** whether the database access was modified. */
    private Boolean                         bModified = Boolean.FALSE;
    /** The minimum meta data check intervall in seconds. */
    private int minMetaDataCheckInterval = defaultMinMetaDataCheckInterval;

    /** Used class of . */
    private boolean							bConnectionPoolEnabled = false;
    /** True, if there is already a task waiting for releasing the connection after request. */
    private boolean 						bReleaseConnectionPending = false;
    
    /** Autocommit state. */
    private boolean                         bAutoCommit = true;
    /** Open state. */
    private boolean                         bOpen = false;
    /** DBAccess is just now closing. */
    private boolean                         bClosing = false;
    /** whether the connection should automatically re-opened if possible. */
    private boolean							bAutoReOpen = false;
    
                         
	/**
	 * Whether {@link Savepoint}s will be used or not.
	 * 
	 * @see #currentSavepoint
	 * @see #releaseSavepoint()
	 * @see #rollbackToSavepoint()
	 * @see #setSavepoint()
	 */
    private boolean useSavepoints = false;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		registerDBAccessClass("jdbc:oracle:", OracleDBAccess.class);
		registerDBAccessClass("jdbc:db2:", DB2DBAccess.class);
		registerDBAccessClass("jdbc:as400:", DB2AS400DBAccess.class);
		registerDBAccessClass("jdbc:derby:", DerbyDBAccess.class);
		registerDBAccessClass("jdbc:jtds:sqlserver:", MSSQLDBAccess.class);
        registerDBAccessClass("jdbc:sqlserver:", MicrosoftSQLDBAccess.class);
		registerDBAccessClass("jdbc:mysql:", MySQLDBAccess.class);
		registerDBAccessClass("jdbc:postgresql:", PostgreSQLDBAccess.class);
		registerDBAccessClass("jdbc:edb:", EDBDBAccess.class);
		registerDBAccessClass("jdbc:hsqldb:", HSQLDBAccess.class);
		registerDBAccessClass("jdbc:sap:", HanaDBAccess.class);
		registerDBAccessClass("jdbc:sqlite:", SQLiteDBAccess.class);
		registerDBAccessClass("jdbc:h2:", H2DBAccess.class);
		registerDBAccessClass("jdbc:tibero:", TiberoDBAccess.class);
		registerDBAccessClass("jdbc:mariadb:", MariaDBAccess.class);
		registerDBAccessClass("jdbc:informix-sqli:", InformixDBAccess.class);
		
		tmpAutoLinkColumnNames.put("*_id*", "*0*1");
		tmpAutoLinkColumnNames.put("*_ID*", "*0*1");

		registerCache(ghtFKsCache);
        registerCache(ghtPKCache);
        registerCache(ghtUKsCache);
        registerCache(ghtAllowedValuesCache);
        registerCache(ghtDefaultValuesCache);
        registerCache(ghtColumnMetaDataCache);
        registerCache(ghtTableInfoCache);
        registerCache(ghtTableNameCache);
        registerCache(ghtLastMetaDataChange); // Clear only the metaData change date, not the last check date, as we do not want to check more often as necessary.
	}
	
	/**
	 * Constructs a new DBAccess Object.
	 */
	public DBAccess()
	{
		super();
		
		ObjectCache.put(new DBAccessObserver(this));
		
		setQuoteCharacters("\"", "\"");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the suitable DBAcces for the given JdbcUrl.
     *  
     * @param pJdbcUrl the JdbcUrl.
     * @return the suitable DBAccess.
     * @throws DataSourceException if the instance name is unknown or the DBAccess object cannot be created.
     */
    public static DBAccess getDBAccess(String pJdbcUrl) throws DataSourceException
    {
        return getDBAccess(pJdbcUrl, null, null);
    }	
	
	/**
	 * Gets the suitable DBAcces for the given JdbcUrl.
	 *  
	 * @param pJdbcUrl the JdbcUrl.
	 * @param pOptionalUserName optional username for the connection
	 * @param pOptionalPassword optional password for the connection
	 * @return the suitable DBAccess.
	 * @throws DataSourceException if the instance name is unknown or the DBAccess object cannot be created.
	 */
	public static DBAccess getDBAccess(String pJdbcUrl, String pOptionalUserName, String pOptionalPassword) throws DataSourceException
	{
		if (pJdbcUrl == null)
		{
			return null;
		}
		
		//#589 check JNDI if the URL is not a jdbc url
		if (!isJdbc(pJdbcUrl))
		{
			try
			{
				InitialContext ctxt = new InitialContext();
			    try
			    {
					Object objInstance = ctxt.lookup(pJdbcUrl);
					
					if (objInstance instanceof DBAccess)
					{
						return (DBAccess)objInstance;
					}
					else if (objInstance instanceof Connection)
					{
					    DBAccess dba = DBAccess.getDBAccess((Connection)objInstance, true);
					    dba.setUrl(pJdbcUrl);
					    
					    return dba;
					}
					else if (objInstance instanceof DataSource)
					{
						return getDBAccess(new DataSourceConnectionPool((DataSource)objInstance, pOptionalUserName, pOptionalPassword));
					}
					else if (objInstance instanceof IConnectionPool)
					{
						return getDBAccess((IConnectionPool)objInstance);
					}
			    }
			    finally
			    {
			        ctxt.close();
			    }
			}
			catch (Exception ex)
			{
				throw new DataSourceException("JNDI URL used, but resource was not found!", ex);
			}
		}
		
		Class<? extends DBAccess> clazz = getDBAccessClass(pJdbcUrl);
		try
		{
			DBAccess dbAccess = clazz.newInstance();
			dbAccess.setUrl(pJdbcUrl);
			dbAccess.setUsername(pOptionalUserName);
			dbAccess.setPassword(pOptionalPassword);
			
			return dbAccess;
		}
		catch (Exception e)
		{
			throw new DataSourceException("Instantion of '" + clazz + "' failed!", e);
		}
	}
	
	/**
	 * Gets the suitable DBAccess for the given {@link DBCredentials}.
	 * 
	 * @param pCredentials the database credentials.
	 * @return the suitable DBAccess.
	 * @throws DataSourceException if the instance name is unknown or the DBAccess object cannot be created.
	 */
	public static DBAccess getDBAccess(DBCredentials pCredentials) throws DataSourceException
	{
		if (pCredentials == null)
		{
			return null;
		}
		
		DBAccess dbAccess = getDBAccess(pCredentials.getUrl(), pCredentials.getUserName(), pCredentials.getPassword());

		if (dbAccess == null)
		{
		    return null;
		}
		
		String sCustomDriver = pCredentials.getDriver();
		
		//allow driver overriding
		if (!StringUtil.isEmpty(sCustomDriver))
        {
		    dbAccess.setDriver(sCustomDriver);
        }
		
		return dbAccess;
	}
	
	/**
     * Gets the suitable DBAccess for the given external connection. The external won't be closed automatically. 
     * 
     * @param pConnection the database connection.
     * @return the suitable DBAccess.
     * @throws DataSourceException if the instance name is unknown or the DBAccess object cannot be created.
     */
    public static DBAccess getDBAccess(Connection pConnection) throws DataSourceException
    {
    	try
    	{
	    	DBAccess dBAccess = getDBAccess(pConnection, false);
	    	
	    	return dBAccess;
		}
		catch (Exception e)
		{
			throw new DataSourceException("Create DBAccess failed!", e);
		}
    }	
	
	/**
	 * Gets the suitable DBAccess for the given connection.
	 * 
	 * @param pConnection the database connection.
     * @param pAutoCloseConnection <code>true</code> whether the connection should be closed via {@link #close()}
	 * @return the suitable DBAccess.
	 * @throws Exception if the instance name is unknown or the DBAccess object cannot be created.
	 */
	private static DBAccess getDBAccess(Connection pConnection, boolean pAutoCloseConnection) throws Exception
	{
		if (pConnection == null)
		{
			throw new IllegalArgumentException("Connection is null");
		}
		
		DatabaseMetaData dbmd = pConnection.getMetaData();
		
		String sConURL = dbmd.getURL();
		
		Class<? extends DBAccess> clazz = getDBAccessClass(sConURL);
		
		DBAccess dbAccess = clazz.newInstance();
		dbAccess.setUrl(sConURL);
		dbAccess.setUsername(dbmd.getUserName());
		dbAccess.setConnection(pConnection);
		dbAccess.bAutoCommit = pConnection.getAutoCommit();
		dbAccess.bAutoClose = pAutoCloseConnection;
		dbAccess.bOpen = true;
		dbAccess.bExternalConnection = true;
		
		dbAccess.initializeMaxColumnLength();
		
		return dbAccess;
	}
	
	/**
	 * Gets the suitable DBAccess for the given connection pool.
	 * 
	 * @param pConnectionPool the connection pool.
	 * @return the suitable DBAccess.
	 * @throws DataSourceException if the instance name is unknown or the DBAccess object cannot be created.
	 */
	public static DBAccess getDBAccess(IConnectionPool pConnectionPool) throws DataSourceException
	{
		try
		{
		    DBAccess dba = DBAccess.getDBAccess(pConnectionPool.getConnection(), true);
		    
		    dba.setConnectionPool(pConnectionPool);
		    
		    return dba;
		}
		catch (Exception e)
		{
			throw new DataSourceException("Create DBAccess failed!", e);
		}
	}
	
	/**
	 * Gets the suitable DBAccess class for the jdbc url.
	 * 
	 * @param pJdbcUrl the jdbc url.
	 * @return the suitable DBAccess class.
	 */
	private static Class<? extends DBAccess> getDBAccessClass(String pJdbcUrl)
	{
		for (String key : dbAccessClasses.keySet())
		{
			if (pJdbcUrl.startsWith(key))
			{
				return dbAccessClasses.get(key);
			}
		}
		return DBAccess.class;
	}
	
	/**
	 * Registers the Class for the jdbc url prefix.
	 * 
	 * @param pJdbcUrlPrefix the jdbc url prefix.
	 * @param pClass the Class.
	 */
	public static void registerDBAccessClass(String pJdbcUrlPrefix, Class<? extends DBAccess> pClass)
	{
		dbAccessClasses.put(pJdbcUrlPrefix, pClass);
	}
	
	/**
	 * Sets the TranslationMap for the automatic link column name custom Translation. its used in the 
	 * getAutomaticLinkColName(ForeignKey pForeignKey) to change the column, thats determined. Default is *_ID to *, and *_id to *.
	 * 
	 * @param pTranslationMap	the TranslationMap to use.
	 */
	public static void setAutomaticLinkColumnNameTranslation(TranslationMap pTranslationMap)
	{
		tmpAutoLinkColumnNames = pTranslationMap;
	}

	/**
	 * Returns the TranslationMap for the automatic link column name custom Translation. its used in the 
	 * getAutomaticLinkColName(ForeignKey pForeignKey) to change the column, thats determined. Default is *_ID to *, and *_id to *.
	 * 
	 * @return the TranslationMap for the automatic link column name custom Translation.
	 */
	public static TranslationMap getAutomaticLinkColumnNameTranslation()
	{
		return tmpAutoLinkColumnNames;
	}
	
    /**
     * Gets the default cursor cache timeout in ms.
     * by default it is 1 hour.
     * 
     * @return the default cursor cache timeout in ms.
     */
	public static long getDefaultCursorCacheTimeout()
	{
		return lDefaultCursorCacheTimeout;
	}
	
    /**
     * Sets the default cursor cache timeout in ms.
     * by default it is 1 hour.
     * 
     * @param pDefaultCursorCacheTimeout the default cursor cache timeout in ms.
     */
	public static void setDefaultCursorCacheTimeout(long pDefaultCursorCacheTimeout)
	{
		lDefaultCursorCacheTimeout = pDefaultCursorCacheTimeout;
	}
	
	/**
	 * Gets the default value for the limit for what is considered a "large object".
	 * 
	 * This limit is used for columns which have the {@code fetchLargObjectsLazy}
	 * option set to determine if the current object should be lazily fetched or not.
	 * 
	 * "Lazy" means that the object is only send to the client if the value
	 * is actually requested. It is still fetched from the datasource and
	 * cached on the server side.
	 * 
	 * Note that the default limit is only applied to new instances of {@link DBAccess}.
	 * 
	 * @return the default limit for what is considered a "large object".
	 */
	public static long getDefaultLargeObjectLimit()
	{
		return lDefaultLargeObjectLimit;
	}
	
	/**
	 * Sets the default value for the limit for what is considered a "large object".
	 * 
	 * This limit is used for columns which have the {@code fetchLargObjectsLazy}
	 * option set to determine if the current object should be lazily fetched or not.
	 * 
	 * "Lazy" means that the object is only send to the client if the value
	 * is actually requested. It is still fetched from the datasource and
	 * cached on the server side.
	 * 
	 * Note that the default limit is only applied to new instances of {@link DBAccess}.
	 * 
	 * @param pDefaultLargeObjectLimit the new default limit.
	 */
	public static void setDefaultLargeObjectLimit(long pDefaultLargeObjectLimit)
	{
		lDefaultLargeObjectLimit = pDefaultLargeObjectLimit;
	}
	
	/**
	 * It gets all columns for each Unique Key and return it.
	 * 
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable				the table to use
	 * @return all columns for each Unique Key. 
	 * @throws DataSourceException	if an error occur during UK search process.
	 */
	protected List<Key> getUniqueKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		ResultSet rsResultSet = null;
		try
		{
			ArrayUtil<Key>  auResult           = new ArrayUtil<Key>();
			ArrayUtil<Name> auUniqueKeyColumns = new ArrayUtil<Name>();
			
			long lMillis = System.currentTimeMillis();
			DatabaseMetaData dbMetaData = getConnectionIntern().getMetaData();
			
			rsResultSet = dbMetaData.getIndexInfo(removeQuotes(pCatalog), removeQuotes(pSchema), removeQuotes(pTable), true, false);
			
			if (!rsResultSet.next())
			{
				CommonUtil.close(rsResultSet);
				
				return auResult;
			}
			
			String sUKName = null;
			do
			{
				if (rsResultSet.getString("COLUMN_NAME") != null)
				{
					if (sUKName != null && !rsResultSet.getString("INDEX_NAME").equals(sUKName))
					{
						Key uk = new Key(sUKName, auUniqueKeyColumns.toArray(new Name[auUniqueKeyColumns.size()]));
						auResult.add(uk);
						auUniqueKeyColumns.clear();
					}
					sUKName = rsResultSet.getString("INDEX_NAME");
					
					// Bug in Postgres, Unique Keys are delivered with quotes.
					String ukColumn = removeDBSpecificQuotes(rsResultSet.getString("COLUMN_NAME")); 
					
					auUniqueKeyColumns.add(new Name(ukColumn, quote(ukColumn)));
				}
			}
			while (rsResultSet.next());
			
			//[JR] #188
			if (auUniqueKeyColumns.size() > 0)
			{
				Key uk = new Key(sUKName, auUniqueKeyColumns.toArray(new Name[auUniqueKeyColumns.size()]));
				
				auResult.add(uk);
			}
			
			if (auResult.size() > 0)
			{
				// remove PKs, because a PK is also a index, but we don't wanna return them too.
				Key pk = getPrimaryKey(pCatalog, pSchema, pTable);
				if (pk != null)
				{
					for (int i = auResult.size() - 1; i >= 0; i--)
					{
						Name[] ukCols = auResult.get(i).getColumns();
						if (ArrayUtil.containsAll(ukCols, pk.getColumns()) && ukCols.length == pk.getColumns().length)
						{
							auResult.remove(i);
						}
					}
				}
			}
			
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getUKs(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }

			return auResult;
		}
		catch (SQLException sqlException)
		{
  			error("Unique Keys couldn't determined from database! - ", pTable, sqlException);
  			
  			return null;
		}
		finally
		{
		    CommonUtil.close(rsResultSet);
		}
	}
	
	/**
	 * Gets the value for the limit for what is considered a "large object".
	 * 
	 * This limit is used for columns which have the
	 * {@code fetchLargObjectsLazy} option set to determine
	 * if the current object should be lazily fetched or not.
	 * 
	 * "Lazy" means that the object is only send to the client if the value is
	 * actually requested. It is still fetched from the datasource and cached on
	 * the server side.
	 * 
	 * @return the current limit.
	 */
	public long getLargeObjectLimit()
	{
		return lLargeObjectLimit;
	}

	/**
	 * It's gets all Primary Key columns and return it as String[].
	 * Gets all Primary Key columns and return it as list of Strings.
	 * 
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable				the table to use
	 * @return all Primary Key columns and return it as String[].
	 * @throws DataSourceException	if an error occur during PK search process.
	 */
	protected Key getPrimaryKeyIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		ResultSet rsResultSet = null;
		try
		{
			long lMillis = System.currentTimeMillis();

			ArrayUtil<Name> auPrimaryKeyColumns = new ArrayUtil<Name>();
			String          sPKName             = null;
			
	        DatabaseMetaData dbMetaData = getConnectionIntern().getMetaData();
			rsResultSet = dbMetaData.getPrimaryKeys(removeQuotes(pCatalog), removeQuotes(pSchema), removeQuotes(pTable));
			
			if (!rsResultSet.next())
			{
				return null;
			}
		
			do
			{
				if (sPKName == null)
				{
					sPKName = rsResultSet.getString("PK_NAME");
				}
				
				auPrimaryKeyColumns.add(new Name(rsResultSet.getString("COLUMN_NAME"), quote(rsResultSet.getString("COLUMN_NAME"))));
			}
			while (rsResultSet.next());
			
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getPK(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }

			Key pk = new Key(sPKName, auPrimaryKeyColumns.toArray(new Name[auPrimaryKeyColumns.size()]));

			return pk;
		}
  		catch (SQLException sqlException)
  		{
  			error("PrimaryKey couldn't determined from database! - ", pTable, sqlException);	
  			
  			return null;
  		}
  		finally
  		{
  		    CommonUtil.close(rsResultSet);
  		}
	}
		
	/**
	 * Returns all Foreign Keys for the specified table.
	 *  
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable the table to use as base table.
	 * @return all Foreign Keys for the specified table.
	 * @throws DataSourceException	if an error occur in determining the ForeignKeys.
	 */
    protected List<ForeignKey> getForeignKeysIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		ResultSet rsResultSetMetaData = null;

		try
		{
			ArrayUtil<ForeignKey> auForeignKeys = new ArrayUtil<ForeignKey>();

			long lMillis = System.currentTimeMillis();
	        DatabaseMetaData dbMetaData = getConnectionIntern().getMetaData();

			rsResultSetMetaData = dbMetaData.getImportedKeys(removeQuotes(pCatalog), removeQuotes(pSchema), removeQuotes(pTable));
			
			if (!rsResultSetMetaData.next())
			{
				// find no FKs -> From Clause can't determined
				return auForeignKeys;
			}
			
			ArrayUtil<Name> auPKColumns = new ArrayUtil<Name>();
			ArrayUtil<Name> auFKColumns = new ArrayUtil<Name>();

			ForeignKey fkForeignKey = new ForeignKey(
					new Name(rsResultSetMetaData.getString("PKTABLE_NAME"), quote(rsResultSetMetaData.getString("PKTABLE_NAME"))), 
					new Name(rsResultSetMetaData.getString("PKTABLE_CAT"), quote(rsResultSetMetaData.getString("PKTABLE_CAT"))), 
					new Name(rsResultSetMetaData.getString("PKTABLE_SCHEM"), quote(rsResultSetMetaData.getString("PKTABLE_SCHEM"))));
			
			do
			{
				if (rsResultSetMetaData.getInt("KEY_SEQ") == 1 && !auPKColumns.isEmpty())
				{
					fkForeignKey.setFKColumns(auFKColumns.toArray(new Name[auFKColumns.size()]));
					fkForeignKey.setPKColumns(auPKColumns.toArray(new Name[auPKColumns.size()]));
					auForeignKeys.add(fkForeignKey);
					
					auPKColumns.clear();			
					auFKColumns.clear();	
					
					fkForeignKey = new ForeignKey(
							new Name(rsResultSetMetaData.getString("PKTABLE_NAME"), quote(rsResultSetMetaData.getString("PKTABLE_NAME"))), 
							new Name(rsResultSetMetaData.getString("PKTABLE_CAT"), quote(rsResultSetMetaData.getString("PKTABLE_CAT"))), 
							new Name(rsResultSetMetaData.getString("PKTABLE_SCHEM"), quote(rsResultSetMetaData.getString("PKTABLE_SCHEM"))));
				}
				
				auPKColumns.add(new Name(rsResultSetMetaData.getString("PKCOLUMN_NAME"), quote(rsResultSetMetaData.getString("PKCOLUMN_NAME"))));
				auFKColumns.add(new Name(rsResultSetMetaData.getString("FKCOLUMN_NAME"), quote(rsResultSetMetaData.getString("FKCOLUMN_NAME"))));
				
				fkForeignKey.setFKName(rsResultSetMetaData.getString("FK_NAME"));
			}
			while (rsResultSetMetaData.next());

			fkForeignKey.setFKColumns(auFKColumns.toArray(new Name[auFKColumns.size()]));
			fkForeignKey.setPKColumns(auPKColumns.toArray(new Name[auPKColumns.size()]));
			auForeignKeys.add(fkForeignKey);
			
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getFKs(", pTable, ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
			
			return auForeignKeys;
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Foreign Keys couldn't determined from database! - " + pTable, formatSQLException(sqlException));
		}
		finally
		{
		    CommonUtil.close(rsResultSetMetaData);
		}
	}
	
	/**
	 * Returns all columns for each Unique Key.
	 * 
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable				the table to use
	 * @return all columns for each Unique Key. 
	 * @throws DataSourceException	if an error occur during UK search process.
	 */
	public final List<Key> getUniqueKeys(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
	    List<Key> uks;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
			
			uks = ghtUKsCache.get(dbAccessIdentifier, tableIdentifier);
			if (uks == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_UKS, pCatalog, pSchema, pTable);

                try
                {
    				uks = getUniqueKeysIntern(pCatalog, pSchema, pTable);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				if (uks == null)
				{
					uks = UKS_NULL;
				}
				
				ghtUKsCache.put(dbAccessIdentifier, tableIdentifier, uks);
			}
			
			if (uks == UKS_NULL)
			{
				return null;
			}
			else
			{
				return uks;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_UKS, pCatalog, pSchema, pTable);
            
            try
            {
                uks = getUniqueKeysIntern(pCatalog, pSchema, pTable);
            }
            finally
            {
                CommonUtil.close(record);
            }

            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

                if (ghtUKsCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtUKsCache.put(dbAccessIdentifier, tableIdentifier, uks != null ? uks : UKS_NULL);
                }
            }
		    
			return uks;
		}
	}
	
	/**
	 * It's gets all Primary Key columns and return it as String[].
	 * 
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable				the table to use
	 * @return all Primary Key columns and return it as String[].
	 * @throws DataSourceException	if an error occur during PK search process.
	 */
	public final Key getPrimaryKey(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
	    Key pk;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
			
			pk = ghtPKCache.get(dbAccessIdentifier, tableIdentifier);
			if (pk == null)
			{
			    Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_PK, pCatalog, pSchema, pTable);

			    try
			    {
    				pk = getPrimaryKeyIntern(pCatalog, pSchema, pTable);
			    }
			    finally
			    {
			        CommonUtil.close(record);
			    }
				if (pk == null)
				{
					pk = PKS_NULL;
				}
				
				ghtPKCache.put(dbAccessIdentifier, tableIdentifier, pk);
			}

			if (pk == PKS_NULL)
			{
				return null;
			}
			else
			{
				return pk;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_PK, pCatalog, pSchema, pTable);
            
            try
            {
                pk = getPrimaryKeyIntern(pCatalog, pSchema, pTable);
            }
            finally
            {
                CommonUtil.close(record);
            }

            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

                if (ghtPKCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtPKCache.put(dbAccessIdentifier, tableIdentifier, pk != null ? pk : PKS_NULL);
                }
            }
		    
			return pk;
		}
	}

	/**
	 * Returns all Foreign Keys for the specified table.
	 *  
	 * @param pCatalog				the catalog to use
	 * @param pSchema				the schema to use
	 * @param pTable the table to use as base table.
	 * @return all Foreign Keys for the specified table.
	 * @throws DataSourceException	if an error occur in determining the ForeignKeys.
	 */
	public final List<ForeignKey> getForeignKeys(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
	    List<ForeignKey> fks;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
			
			fks = ghtFKsCache.get(dbAccessIdentifier, tableIdentifier);
			if (fks == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_FKS, pCatalog, pSchema, pTable);

                try
                {
                    fks = getForeignKeysIntern(pCatalog, pSchema, pTable);
                }
                finally
                {
                    CommonUtil.close(record);
                }
				
				if (fks == null)
				{
					fks = FKS_NULL;
				}
				
				ghtFKsCache.put(dbAccessIdentifier, tableIdentifier, fks);
			}
			
			if (fks == FKS_NULL)
			{
				return null;
			}
			else
			{
				return fks;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_FKS, pCatalog, pSchema, pTable);
            
            try
            {
                fks = getForeignKeysIntern(pCatalog, pSchema, pTable);
            }
            finally
            {
                CommonUtil.close(record);
            }
            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

                if (ghtFKsCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtFKsCache.put(dbAccessIdentifier, tableIdentifier, fks != null ? fks : FKS_NULL);
                }
            }
		    
			return fks;
		}
	}
	
	/**
	 * Returns the full qualified table name incl. schema/catalog/db link for the given synonym.
	 * If pSynonym is no synonym, then the pSynonym string is returned.
	 *   
	 * @param pSynonym	the synonym to use.
	 * @return the full qualified table name incl. schema/catalog/db link for the given synonym.
	 * @throws DataSourceException	if an error occur in determining the synonyms.
	 */
	public final String getTableForSynonym(String pSynonym) throws DataSourceException
	{
	    String tableName;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIgnoreCaseIdentifier(pSynonym);
			
			tableName = ghtTableNameCache.get(dbAccessIdentifier, tableIdentifier);
			if (tableName == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_TABLESYNONYM, pSynonym);

                try
                {
    				tableName = getTableForSynonymIntern(pSynonym);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				if (tableName == null)
				{
					tableName = TABLENAME_NULL;
				}
				
				ghtTableNameCache.put(dbAccessIdentifier, tableIdentifier, tableName);
			}
			
			if (tableName == TABLENAME_NULL)
			{
				return null;
			}
			else
			{
				return tableName;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_TABLESYNONYM, pSynonym);
            
            try
            {
                tableName = getTableForSynonymIntern(pSynonym);
            }
            finally
            {
                CommonUtil.close(record);
            }
            
            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIgnoreCaseIdentifier(pSynonym);

                if (ghtTableNameCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtTableNameCache.put(dbAccessIdentifier, tableIdentifier, tableName != null ? tableName : TABLENAME_NULL);
                }
            }
            
			return tableName;
		}
	}

	/**
	 * Returns the full qualified table name incl. schema/catalog/db link for the given synonym.
	 * If pSynonym is no synonym, then the pSynonym string is returned.
	 *   
	 * @param pSynonym	the synonym to use.
	 * @return the full qualified table name incl. schema/catalog/db link for the given synonym.
	 * @throws DataSourceException	if an error occur in determining the synonyms.
	 */
	protected String getTableForSynonymIntern(String pSynonym) throws DataSourceException
	{
		// String pCatalog, String pSchema, String pTable
		
		return pSynonym;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object[]> fetch(ServerMetaData pServerMetaData,
								String pBeforeQueryColumns,
								String[] pQueryColumns,
								String pFromClause,
								ICondition pFilter,
								String pWhereClause,
								String pAfterWhereClause, 
								SortDefinition pSort,
								int pFromRow,
								int pMinimumRowCount,
								boolean pAllowLazyFetch) throws DataSourceException
	{
		return fetch(
				pServerMetaData,
				pBeforeQueryColumns,
				pQueryColumns,
				pFromClause,
				pFilter,
				pWhereClause,
				pAfterWhereClause,
				pSort,
				null,
				null,
				pFromRow,
				pMinimumRowCount,
				pAllowLazyFetch);
	}

	/**
	 * Returns the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * parameters. It fetch's the the rows from pFromRow row index a minimum amount of 
	 * pMinimumRowCount rows.  Implementations should fetch as much rows as possible in a 
	 * proper amount of time, to get less requests from the client model to the server 
	 * IDBAccess. Implementation can cache the select cursor and reuse it for the next fetch
	 * operation, but they should take care, that's a state less call and in a fall over case
	 * it maybe loose on the fail over system the cursor. 
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pFilter	            the filter to use
	 * @param pWhereClause          the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
	 * @param pSort		            the sort order to use
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
	 * @param pServerMetaData		the MetaDataColumn array to use.
	 * @return the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * 		   parameters.
	 * @throws DataSourceException	if the fetch fails.
	 */
	public List<Object[]> fetch(ServerMetaData pServerMetaData,
								String pBeforeQueryColumns,
								String[] pQueryColumns,
								String pFromClause,
								ICondition pFilter,
								String pWhereClause,
								String pAfterWhereClause, 
								SortDefinition pSort,
								int pFromRow,
								int pMinimumRowCount) throws DataSourceException
	{
		return fetch(pServerMetaData,
    				 pBeforeQueryColumns,
    				 pQueryColumns,
    				 pFromClause,
    				 pFilter,
    				 pWhereClause,
    				 pAfterWhereClause,
    				 pSort,
    				 null,
    				 null,
    				 pFromRow,
    				 pMinimumRowCount,
    				 false);
	}
	
	/**
	 * Mostly same as {@link DBAccess#fetch(ServerMetaData, String, String[], String, ICondition, String, String, SortDefinition, int, int)}.
	 * 
	 * This fetch does accept an additional order by clause which will be used
	 * if the given default sort is null.
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pFilter	            the filter to use
	 * @param pWhereClause	        the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
	 * @param pOrderByClause		the order by clause
	 * @param pSort		            the sort order to use
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
	 * @param pServerMetaData		the MetaDataColumn array to use.
	 * @return the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * 		   parameters.
	 * @throws DataSourceException	if the fetch fails.
	 */
	public List<Object[]> fetch(ServerMetaData pServerMetaData,
								String pBeforeQueryColumns,
								String[] pQueryColumns,
								String pFromClause,
								ICondition pFilter,
								String pWhereClause,
								String pAfterWhereClause,
								SortDefinition pSort,
								String pOrderByClause,
								int pFromRow,
								int pMinimumRowCount) throws DataSourceException
	{
		return fetch(
				pServerMetaData,
				pBeforeQueryColumns,
				pQueryColumns,
				pFromClause,
				pFilter,
				pWhereClause,
				pAfterWhereClause,
				pSort,
				pOrderByClause,
				null,
				pFromRow,
				pMinimumRowCount,
				false);
	}

    /**
     * Mostly same as {@link DBAccess#fetch(ServerMetaData, String, String[], String, ICondition, String, String, SortDefinition, int, int)}.
     * 
     * This fetch does accept an additional order by clause which will be used
     * if the given default sort is null.
     * 
     * @param pBeforeQueryColumns   the before query columns
     * @param pQueryColumns         the query columns   
     * @param pFromClause           the from clause with query tables and join definitions
     * @param pFilter               the filter to use
     * @param pWhereClause          the last where condition in query
     * @param pAfterWhereClause     the after where clause in query
     * @param pOrderByClause        the order by clause
     * @param pSort                 the sort order to use
     * @param pFromRow              the row index from to fetch
     * @param pMinimumRowCount      the minimum count row to fetch
     * @param pServerMetaData       the MetaDataColumn array to use.
     * @param pAllowLazyFetch       if lazy fetch should be allowed.
     * @return the List of fetched rows (as List of Object[]) for the specified query tables and 
     *         parameters.
     * @throws DataSourceException  if the fetch fails.
     */
    public List<Object[]> fetch(ServerMetaData pServerMetaData,
                                String pBeforeQueryColumns,
                                String[] pQueryColumns,
                                String pFromClause,
                                ICondition pFilter,
                                String pWhereClause,
                                String pAfterWhereClause,
                                SortDefinition pSort,
                                String pOrderByClause,
                                int pFromRow,
                                int pMinimumRowCount,
                                boolean pAllowLazyFetch) throws DataSourceException
    {
        return fetch(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                null,
                pFromRow,
                pMinimumRowCount,
                pAllowLazyFetch);
    }
    
	/**
	 * Mostly same as {@link DBAccess#fetch(ServerMetaData, String, String[], String, ICondition, String, String, SortDefinition, int, int)}.
	 * 
	 * This fetch does accept an additional order by clause which will be used
	 * if the given default sort is null.
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pFilter	            the filter to use
	 * @param pWhereClause	        the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
	 * @param pOrderByClause		the order by clause
     * @param pAfterOrderByClause   the after order by clause
	 * @param pSort		            the sort order to use
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
	 * @param pServerMetaData		the MetaDataColumn array to use.
	 * @param pAllowLazyFetch		if lazy fetch should be allowed.
	 * @return the List of fetched rows (as List of Object[]) for the specified query tables and 
	 * 		   parameters.
	 * @throws DataSourceException	if the fetch fails.
	 */
    @SuppressWarnings("resource")
    public List<Object[]> fetch(ServerMetaData pServerMetaData,
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
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_FETCH);
        
        try
        {
        	checkIsOpen();

    		long lMillis = System.currentTimeMillis();
    		
            if (pFromClause.startsWith("(") && pFromClause.endsWith(")")) // some databases need an alias in sub selects
            {
                pFromClause += " m";
            }
    		
    		ParameterizedStatement selectStatement = getParameterizedSelectStatement(pServerMetaData,
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
    		
    		if (record != null)
    		{
    		    if (!selectStatement.getValues().isEmpty())
    		    {
        		    record.setParameter(selectStatement.getStatement(), formatParameter(selectStatement.getValuesAsArray()));
    		    }
    		    else
    		    {
    		        record.setParameter(selectStatement.getStatement());
    		    }
    		}
    		
    		ServerColumnMetaData[] scmd = pServerMetaData.getServerColumnMetaData();
    
            List<Object[]> auResult = new ArrayUtil<Object[]>();
            long executeQueryTime = -1;
            int size = 0;
    		
    		PreparedStatement psSelect = null;
    		ResultSet rsResultSet = null;
    		Cursor cCursor = null;
    		try 
    		{
    		    String cursorIdentifierPrefix = createIdentifier(pBeforeQueryColumns, pQueryColumns, pFromClause, pWhereClause, pAfterWhereClause);
    			String fetchCause;
    			if (pMinimumRowCount > 0 && pMinimumRowCount <= 2) // refetch of a row
    			{
    				fetchCause = "R";
    			}
    			else if (pMinimumRowCount >= 500  // csv export or reporting
    			        && (pFromRow == 0 || htFetchResultSetCache.get(createIdentifier(cursorIdentifierPrefix, "C")) != null)) // reports are fetching everything immediately
    			    
    			{
                    fetchCause = "C";
    			}
    			else
    			{
    				fetchCause = "F";
    			}
    			String cursorIdentifier = createIdentifier(cursorIdentifierPrefix, fetchCause);
    			cCursor = htFetchResultSetCache.remove(cursorIdentifier);
    			
    			if (cCursor == null || !cCursor.isUseable(selectStatement.getStatement(), selectStatement.getValuesAsArray(), pFromRow))
    			{
    				if (cCursor != null && selectStatement.getStatement().equals(cCursor.sSelectStatement))
    				{
    					psSelect = cCursor.statement;
    					
   						CommonUtil.close(cCursor.rsResultSet);
    				}
    				else
    				{	
    					CommonUtil.close(cCursor);
    					cCursor = null;

    					psSelect = getPreparedStatement(selectStatement.getStatement());
    				}
    
    				if (iQueryTimeout >= 0)
    				{
    					try
    					{
    						psSelect.setQueryTimeout(iQueryTimeout);
    					}
    					catch (Throwable ex)
    					{
    						// Ignore not implemented Exceptions.
    					}
    				}
    	
    				// set Filter Parameter Values
    				if (!selectStatement.getValues().isEmpty())
    				{
    					setFilterParameter(1, psSelect, selectStatement.getValuesAsArray());
    				}	

					setSavepoint();
					if (pMinimumRowCount < 0)
					{
						psSelect.setFetchSize(500);
					}
					else if (pMinimumRowCount < 3)
					{
					    psSelect.setFetchSize(Math.max(1, pMinimumRowCount));
					}
					else
					{
    					float faktor = Math.min(0.5f, pMinimumRowCount / 1000f);
    					psSelect.setFetchSize(Math.min(500, Math.round(pMinimumRowCount * (8f * faktor * faktor - 8f * faktor + 3))));
					}

                    rsResultSet = psSelect.executeQuery();
                    
                    executeQueryTime = System.currentTimeMillis() - lMillis;
                    
    				for (int j = 0, count = getDiscardRowCount(pFromRow, pMinimumRowCount); j < count; j++)
    				{
    					if (!rsResultSet.next())
    					{
    						auResult.add(null);	
    						
    						CommonUtil.close(rsResultSet, psSelect, cCursor);
    
    		                if (record != null)
    		                {
    		                    record.setCount(auResult.size() - 1);
    		                }
    						
    						return auResult;
    					}
    					rsResultSet.getObject(1); // We have to read any object per row, as otherwise some jdbc driver runs into an internal bug 
    				}
    				if (cCursor == null)
    				{
    					cCursor = new Cursor();
    				}
   					cCursor.init(psSelect, rsResultSet, selectStatement.getStatement(), selectStatement.getValuesAsArray(), 
    						(int)executeQueryTime);
    			}
    			else
    			{
    				rsResultSet = cCursor.rsResultSet;
    				psSelect = (PreparedStatement)rsResultSet.getStatement();
    			}
    			
    			ResultSetMetaData rmSelectMetaData = rsResultSet.getMetaData();
    
    			// try to fetch all rows in iMaxTime millis
    			long fetchUntil;
    			if (iMaxTime <= 0 || (pMinimumRowCount >= 0 && pMinimumRowCount < 3)) // No unnecessary fetch
    			{
    			    fetchUntil = Long.MIN_VALUE;
    			}
    			else
    			{
    			    //           lMillis could be used, to limit complete fetch time including cursor creation.
    			    //           but we use pure fetch time, and also a bit bigger max time, if the statement is very slow. 5% should not hurt.
    			    fetchUntil = System.currentTimeMillis() + Math.max(iMaxTime, cCursor.iExecutionTime / 20);
    			}
    
    			int columnCount = pServerMetaData.getMetaData().getColumnMetaDataCount();
    			int resultSetColumnCount = rmSelectMetaData.getColumnCount();
    			int[] columnIndexes = null;
    			boolean[] lazyIndexes = null;
    			
    			HashMap<String, Object[]> blobStatements = new HashMap<String, Object[]>();
    			int[] primaryKeyResultSetIndices = null;
				And primaryKeyCondition = null;

    			int i = 0;
    			
    			for (; pMinimumRowCount < 0 || i < pMinimumRowCount || System.currentTimeMillis() < fetchUntil; i++)				
    			{
    				Object[] oRow = null;
    				
    				if (rsResultSet.next())
    				{
    					oRow = new Object[columnCount];
    					
    					if (columnIndexes == null)
    					{
    						columnIndexes = new int[resultSetColumnCount];
    						lazyIndexes = new boolean[resultSetColumnCount];
    						for (int j = 0; j < resultSetColumnCount; j++)
    						{
    							int columnIndex = pServerMetaData.getServerColumnMetaDataIndex(getColumnName(rmSelectMetaData, j + 1).toUpperCase());
    							
    							if (columnIndex >= 0)
    							{
    								columnIndexes[j] = columnIndex;
    								lazyIndexes[j] = pServerMetaData.getServerColumnMetaData(columnIndex).getColumnMetaData().isFetchLargeObjectsLazy();
    							}
    							else
    							{
    								// Could happen for columns which are not in the metadata, like COUNT(*).
    								columnIndexes[j] = j;
    							}
    						}
    					}
    					
    					for (int j = 0; j < resultSetColumnCount; j++)
    					{
    						int columnIndex = columnIndexes[j];

    						Object oValue = getObjectFromResultSet(rsResultSet, j + 1, scmd[columnIndex]);
    						
    						// convert to Java types
    						if (oValue instanceof java.sql.Clob)
    						{
    							Clob cValue = (Clob)oValue;
    							oValue = cValue.getSubString(1, (int)cValue.length());
    							CommonUtil.close(cValue);
    						}  
    						else if (oValue instanceof java.sql.Blob)
    						{
    							Blob bValue = (Blob)oValue;
    							
    							long length = bValue.length();
        						
								int[] pKIndices = pServerMetaData.getPrimaryKeyColumnIndices();
    							
        						if (pAllowLazyFetch
        								&& lazyIndexes[j]
   								        && pServerMetaData.getPrimaryKeyType() == PrimaryKeyType.PrimaryKeyColumns
        								&& pKIndices != null
        								&& (length >= lLargeObjectLimit || bValue instanceof ILazyFetchBlob))
        						{
        							if (primaryKeyResultSetIndices == null)
        							{
        								primaryKeyCondition = new And();
        								
            							for (int idx = 0; idx < columnIndexes.length; idx++)
        								{
            								int colIndex = columnIndexes[idx];
        									if (ArrayUtil.contains(pKIndices, colIndex))
        									{
        									    Object refValue;
        									    switch (scmd[colIndex].getColumnMetaData().getTypeIdentifier()) 
        									    {
        									        case BigDecimalDataType.TYPE_IDENTIFIER: refValue = PRIMARYKEY_REFERENCE_VALUES[0]; break;
        									        case TimestampDataType.TYPE_IDENTIFIER:  refValue = PRIMARYKEY_REFERENCE_VALUES[1]; break;
        									        default:                                 refValue = PRIMARYKEY_REFERENCE_VALUES[2]; break;
        									    }
        									    
        										int resultSetIndex = idx + 1;
        										primaryKeyResultSetIndices = ArrayUtil.add(primaryKeyResultSetIndices, resultSetIndex);
        										primaryKeyCondition.add(new Equals(scmd[colIndex].getName(), refValue));
        									}
        								}
        							}
        								
        							Object[] parameters = new Object[primaryKeyResultSetIndices.length];
        							
        							for (int idx = 0; idx < primaryKeyResultSetIndices.length; idx++)
        							{
        								parameters[idx] = rsResultSet.getObject(primaryKeyResultSetIndices[idx]);
        							}
        							
        							String columnName = scmd[columnIndex].getName();
        							String   blobStatement = null;
        							Object[] blobParameter = null;
        							int      blobPkIndex   = -1;
        							
        							Object[] blobCache = blobStatements.get(columnName);
        							
        							if (blobCache == null)
        							{
                                        ParameterizedStatement pstmt = null;
                                        
        								if (scmd[columnIndex].isWritable()) // Writable Column, that means, there must be a writeBack table.
        								{									// Try to find it, and check, if it is not a complex query.
        									
        									String sFromClause = replaceWhitespace(pFromClause);
        									
        									int wbIndex = sFromClause.indexOf(" m ");
        									
        									if (wbIndex < 0 && sFromClause.endsWith(" m"))
        									{
        										wbIndex = sFromClause.length() - 2;
        									}
        									
        									if (wbIndex >= 0)
        									{
        										String writeBackTable = pFromClause.substring(0, wbIndex + 2).trim();
        										
        										if (!writeBackTable.contains("(") && !writeBackTable.contains(")"))
        										{
        										    pstmt = getParameterizedSelectStatement(pServerMetaData, null, 
                									                                        new String[] {scmd[columnIndex].getQuotedName()}, 
                									                                        writeBackTable, primaryKeyCondition, 
                                                                                            null, null, null, null, null,
                                                                                            0, 1);
        										}
        									}
        								}
        								
        								if (pBeforeQueryColumns == null 
        								        || !(replaceWhitespace(pBeforeQueryColumns).toLowerCase().contains(" select ")
        								                || pBeforeQueryColumns.contains("(")))
        								{	
        									// only if query columns are not a sub select due to beforeQueryColumns clause
        									// we can reduce the query columns in statement.

            								pstmt = getParameterizedSelectStatement(pServerMetaData, pBeforeQueryColumns, 
										            								new String[] {scmd[columnIndex].getRealQueryColumnName()}, 
										            								pFromClause, primaryKeyCondition, 
										            								pWhereClause, pAfterWhereClause, null, null, null,
										            								0, 1);
        								}
        								
        								if (pstmt == null)
        								{ 
        									// create subselect with only one column.
        									
        									String beforeQueryColumns = scmd[columnIndex].getQuotedName() + " from (select ";
        									if (pBeforeQueryColumns != null)
        									{
        										beforeQueryColumns = beforeQueryColumns + pBeforeQueryColumns;
        									}
        									String afterWhereClause = ")";
        									if (pAfterWhereClause != null)
        									{
        										afterWhereClause = pAfterWhereClause + afterWhereClause;
        									}
        									
            								pstmt = getParameterizedSelectStatement(pServerMetaData, beforeQueryColumns,
								            										pQueryColumns, 
								            										pFromClause, primaryKeyCondition, 
								            										pWhereClause, afterWhereClause, null, null, null,
								            										0, 1);
        								}
        								
    									blobStatement = pstmt.getStatement();
    									
    									blobParameter = pstmt.getValuesAsArray();
    									
    									for (int ip = 0; ip < blobParameter.length && blobPkIndex == -1; ip++)
    									{
    										if (ArrayUtil.containsReference(PRIMARYKEY_REFERENCE_VALUES, blobParameter[ip]))
    										{
    										    blobPkIndex = ip;
    										}
    									}
    									
        								blobStatements.put(columnName, new Object[] {blobStatement, blobParameter, new int[] {blobPkIndex}});
        							}
        							else
        							{
        								blobStatement = (String)blobCache[0];
        								blobParameter = (Object[])blobCache[1];
        								blobPkIndex   = ((int[])blobCache[2])[0];
        							}
                                    Object[] paramTemp = blobParameter.clone();
                                    System.arraycopy(parameters, 0, paramTemp, blobPkIndex, parameters.length);
                                    parameters = paramTemp;
        							
        							String cacheKeyBase = Integer.toHexString(blobStatement.hashCode());
        							String cacheKey = cacheKeyBase + createIdentifier(parameters) + "/" + columnName;
        							
        							BlobFileHandle handle = new BlobFileHandle(this, blobStatement, parameters, columnName, length);
        							
        							ObjectCache.put(cacheKey, handle);
        							oValue = new RemoteFileHandle(columnName, cacheKey);
        						}
        						else
        						{
        							oValue = bValue.getBytes(1, (int)bValue.length());
        							CommonUtil.close(bValue);
        						}
    						}
    						
    						oRow[columnIndex] = convertDatabaseSpecificObjectToValue(scmd[columnIndex], oValue);
    					}
    					auResult.add(oRow);
    				}
    				else
    				{
    					auResult.add(null);	
    					
    					CommonUtil.close(rsResultSet, psSelect, cCursor);

    	                if (record != null)
    	                {
    	                    record.setCount(auResult.size() - 1);
    	                }

                        size = auResult.size() - 1;

    					return auResult;
    				}				
    			}
    			
    			if (auResult.size() == 0)
    			{
    				auResult.add(null);

                    CommonUtil.close(rsResultSet, psSelect, cCursor);
                    
                    if (record != null)
                    {
                        record.setCount(0);
                    }
    			}
    			else
    			{
    				cCursor.iLastRow = pFromRow + i;
    				cCursor.iLastAccessTime = System.currentTimeMillis();
    				// save current ResultSetCursor and use the next time!!!
    				htFetchResultSetCache.put(cursorIdentifier, cCursor);
    				
                    if (record != null)
                    {
                        record.setCount(auResult.size());
                    }
                    
                    size = auResult.size();
    			}

    			return auResult;
    		}
    		catch (Exception ex)
    		{
    			rollbackToSavepoint();
    		    CommonUtil.close(rsResultSet, psSelect, cCursor);
    			
    			throw new DataSourceException("fetch statement failed! - "
    						+ selectStatement.getStatement(), (ex instanceof SQLException ? formatSQLException((SQLException)ex) : ex));
    		}
    		finally
    		{
                if (isLogEnabled(LogLevel.DEBUG))
                {
                    debug(selectStatement.getStatement(), selectStatement.getValuesAsArray(), 
                          "\nfrom row: ", Integer.valueOf(pFromRow), ", minimum row count: ", Integer.valueOf(pMinimumRowCount), 
                          ", rows fetched: ", Integer.valueOf(size), ", execute query time: ", executeQueryTime < 0 ? "none" : Long.valueOf(executeQueryTime) + "ms",  
                          ", complete time: ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
                }
                

    		    releaseSavepoint();
    		}
		}
		finally
		{
		    CommonUtil.close(record);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void lockRow(String pWriteBackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException
	{	
		checkIsOpen();
		
		if (pWriteBackTable == null)
		{
			throw new DataSourceException("Missing WriteBackTable!");
		}

		try
		{
			if (!isAutoCommit() && getConnectionIntern().getMetaData().supportsSelectForUpdate())
			{
				lockRowInternal(pWriteBackTable, pServerMetaData, pPKFilter);
			}
		}
		catch (SQLException se)
		{
			throw new DataSourceException("Execute locking failed!", formatSQLException(se));
		}
	}
	
	/**
	 * It locks the current row and return how many rows are affected.
	 * 
	 * @param pWriteBackTable the storage unit to use
	 * @param pPKFilter 	  the PrimaryKey in as an <code>ICondition</code> to identify the row to lock   
	 * @param pServerMetaData the MetaDataColumn array to use.
	 * @return the counts of affected rows
	 * @throws DataSourceException if an <code>Exception</code> occur during interacting with the storage 
	 */
	protected int lockRowInternal(String pWriteBackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException
	{
		int rowCount = getRowCount(getDatabaseSpecificLockStatement(pWriteBackTable, pServerMetaData, pPKFilter), pPKFilter, 1);
		
		setModified(Boolean.TRUE);
		
		return rowCount;
	}
	
    /**
	 * {@inheritDoc}
	 */
	public Object[] insert(String pWriteBackTable, ServerMetaData pServerMetaData, Object[] pNewDataRow) throws DataSourceException
	{
	    Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_INSERT, pWriteBackTable);
	    
	    try
	    {
	    	checkIsOpen();
	    	
	    	long lMillis = System.currentTimeMillis();

    		if (pWriteBackTable == null)
    		{
    			throw new DataSourceException("Missing WriteBackTable!");
    		}
    		
    		StringBuilder sInsertStatement = new StringBuilder("INSERT INTO ");
    		sInsertStatement.append(pWriteBackTable);
    		sInsertStatement.append(" (");
    
    		// add column names to insert
    		int iColumnCount = 0;
    		String sDummyColumn = null;
    		
    		ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
    		int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
    		
            boolean isDebugLogEnabled = isLogEnabled(LogLevel.DEBUG);
            Object[] params = isDebugLogEnabled ? new Object[iaWriteables.length] : null; 
    		
    		for (int i = 0; i < iaWriteables.length; i++)
    		{
    		    Object value = pNewDataRow[iaWriteables[i]];
    			if (value != null)
    			{
    			    if (isDebugLogEnabled)
    			    {
    			        params[iColumnCount] = value; 
    			    }
    				if (iColumnCount > 0)
    				{
    					sInsertStatement.append(", ");
    				}
    				sInsertStatement.append(cmdServerColumnMetaData[iaWriteables[i]].getColumnName().getQuotedName());
    				iColumnCount++;
    			}
    		}
    
    		if (iColumnCount == 0)
    		{
    			// if no storable columns, put in a dummy one
    			for (int i = 0; iColumnCount == 0 && i < iaWriteables.length; i++)
    			{
    				if (!cmdServerColumnMetaData[iaWriteables[i]].isAutoIncrement())
    				{
    					sDummyColumn = cmdServerColumnMetaData[iaWriteables[i]].getColumnName().getQuotedName();
    					sInsertStatement.append(sDummyColumn);
    					iColumnCount++;
    				}
    			}
    		}
    
    		// Add values '?' to insert
    		sInsertStatement.append(") VALUES (");
    		
    		if (iColumnCount > 0)
    		{
    			sInsertStatement.append("?");
    			
    			for (int i = 1; i < iColumnCount; i++)
    			{
    				sInsertStatement.append(", ?");
    			}
    		}
    		sInsertStatement.append(")");
    
    		if (record != null)
    		{
    		    if (pNewDataRow != null && pNewDataRow.length > 0)
    		    {
    		    	 record.setParameter(sInsertStatement, formatParameter(pNewDataRow));
    		    }
    		    else
    		    {
    		    	 record.setParameter(sInsertStatement);
    		    }
    		}
    		
    		pNewDataRow = insertDatabaseSpecific(pWriteBackTable, sInsertStatement.toString(), pServerMetaData, pNewDataRow, sDummyColumn);

    		setModified(Boolean.TRUE);
    		
    		if (isDebugLogEnabled)
    		{
    			debug(sInsertStatement, "\n", ArrayUtil.truncate(params, iColumnCount), "\ninserted row: ", pNewDataRow,
    			        "\ncomplete time: ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
    		}
    		
    		//NO PK, and all columns are null -> Exception -> but shouldn't happen if we want import
    		//empty records via API calls
    		if (pServerMetaData.getPrimaryKeyType() != PrimaryKeyType.AllColumns)
    		{
    			// check Empty PK and throw Exception
    			boolean bPKEmpty = true;
    			int[] iPKColsIndices = pServerMetaData.getPrimaryKeyColumnIndices();
    			
    			if (iPKColsIndices != null)
    			{
    				for (int i = 0; i < iPKColsIndices.length && bPKEmpty; i++)
    				{
    					if (pNewDataRow[iPKColsIndices[i]] != null)
    					{
    						bPKEmpty = false;
    					}
    				}
    			}
    			
    			if (bPKEmpty)
    			{
    				throw new DataSourceException("Primary key column empty after insert! " + pWriteBackTable);
    			}
    		}
    		
    		return pNewDataRow;
	    }
	    finally
	    {
	        CommonUtil.close(record);
	    }
	}
	
	/**
	 * Sets the value for the limit for what is considered a "large object".
	 * 
	 * This limit is used for columns which have the
	 * {@code fetchLargObjectsLazy} option set to determine
	 * if the current object should be lazily fetched or not.
	 * 
	 * "Lazy" means that the object is only send to the client if the value is
	 * actually requested. It is still fetched from the datasource and cached on
	 * the server side.
	 * 
	 * @param pLargeObjectLimit the new limit.
	 */
	public void setLargeObjectLimit(long pLargeObjectLimit)
	{
		lLargeObjectLimit = pLargeObjectLimit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object[] update(String pWriteBackTable, ServerMetaData pServerMetaData, Object[] pOld, Object[] pNew) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_UPDATE, pWriteBackTable);
        
        try
        {
            checkIsOpen();
            
            long lMillis = System.currentTimeMillis();

        	Object[] originalNew = pNew.clone();

    		// TODO [RH] Maybe check if the new row is different to the values in the database -> fetch first.
    
    		if (pWriteBackTable == null)
    		{
    			throw new DataSourceException("Missing WriteBackTable!");
    		}
    		
    		if (pServerMetaData.getPrimaryKeyColumnNames() == null || pServerMetaData.getPrimaryKeyColumnNames().length == 0)
    		{
    			throw new DataSourceException("PK Columns empty! - update not possible!");
    		}
    		
    		StringBuilder sUpdateStatement = new StringBuilder("UPDATE ");
    		sUpdateStatement.append(pWriteBackTable);
    		sUpdateStatement.append(" SET ");
    
    		// add column names to update
    		int iColumnCount = 0;
    		ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
    		int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
    		
    		boolean isDebugLogEnabled = isLogEnabled(LogLevel.DEBUG);
    		
            Object[] oParams = null;
            
            if (record != null || isDebugLogEnabled)
            {
                oParams = new Object[0];
            }

            for (int i = 0; i < iaWriteables.length; i++)
    		{
    			IDataType dtDataType = cmdServerColumnMetaData[iaWriteables[i]].getDataType();
    			Object newObject = pNew[iaWriteables[i]];
    			Object oldObject = pOld[iaWriteables[i]];
    			if (!(newObject instanceof RemoteFileHandle && oldObject instanceof RemoteFileHandle
    					&& CommonUtil.equals(((RemoteFileHandle)newObject).getObjectCacheKey(), ((RemoteFileHandle)oldObject).getObjectCacheKey())))
    			{
        			if (newObject instanceof IFileHandle)
        			{
        				try
        				{
        					newObject = FileUtil.getContent(((IFileHandle)newObject).getInputStream());
        					pNew[iaWriteables[i]] = newObject;
        				}
        				catch (Exception ex)
        				{
        					// Ignore it at this position, to ensure a proper exception is thrown at the correct place.
        				}
        			}
        			if (oldObject instanceof IFileHandle)
        			{
        				try
        				{
        					oldObject = FileUtil.getContent(((IFileHandle)oldObject).getInputStream());
        					pOld[iaWriteables[i]] = oldObject;
        				}
        				catch (Exception ex)
        				{
        					// Ignore it at this position, to ensure a proper exception is thrown at the correct place.
        				}
        			}
    			}
    			
    	        if (dtDataType.compareTo(newObject, oldObject) != 0)
    	        {
    				if (iColumnCount > 0)
    				{
    					sUpdateStatement.append(", ");
    				}
    				sUpdateStatement.append(cmdServerColumnMetaData[iaWriteables[i]].getColumnName().getQuotedName());
    				sUpdateStatement.append(" = ? ");
    				iColumnCount++;
    				
    				if (record != null || isDebugLogEnabled)
    				{
    				    oParams = ArrayUtil.add(oParams, newObject);
    				}
    	        }
    		}
    		// construct Filter over PK cols 
    		ICondition pPKFilter = Filter.createEqualsFilter(
    				pServerMetaData.getPrimaryKeyColumnNames(), pOld, pServerMetaData.getMetaData().getColumnMetaData());
    
            if (record != null || isDebugLogEnabled)
            {
                oParams = ArrayUtil.addAll(oParams, getParameter(pPKFilter));
            }

    		int iCount = 0;
    		if (iColumnCount == 0)
    		{
    			// no storable columns; then....
    			
    			try
    			{
    				// if select for update supported from db and not in autocommit mode, then 
    				// lock the record and determine the count of rows which exists to this PK!
    				
    				// the count of rows is important, to decide later if we make an update(iCount==1), insert(iCount==0) 
    				// or throw an error because to many rows (iCount>1) are affected from the update.
    				if (!isAutoCommit() && getConnectionIntern().getMetaData().supportsSelectForUpdate())
    				{
    					iCount = lockRowInternal(pWriteBackTable, pServerMetaData, pPKFilter);
    				}
    				else
    				{
    					// otherwise only determine the count of rows exists for this PK 
    					StringBuilder sbfSelect = new StringBuilder("SELECT 0 FROM ");
    					sbfSelect.append(pWriteBackTable);
    					sbfSelect.append(getWhereClause(pServerMetaData, pPKFilter, null, false));
    					
    					iCount = getRowCount(sbfSelect.toString(), pPKFilter, 1); 
    				}
    			}
    			catch (SQLException se)
    			{
    				throw new DataSourceException("Connection access failed!", formatSQLException(se));
    			}
    		}
    		else
    		{
    			// add WHERE Clause
    			sUpdateStatement.append(getWhereClause(pServerMetaData, pPKFilter, null, false));
    
    			iCount = updateDatabaseSpecific(pWriteBackTable, sUpdateStatement.toString(), pServerMetaData, 
    					                        pOld, pNew, pPKFilter);
    			
    			setModified(Boolean.TRUE);
    			
    			if (isDebugLogEnabled)
    			{
    				debug(sUpdateStatement, "\n", oParams, "\nold row: ", pOld, "\nnew row: ", pNew,
                            "\ncomplete time: ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
    			}
    		}

    		if (record != null)
    		{
    			record.setCount(iCount);
    			
    			if (oParams != null && oParams.length > 0)
    		    {
    		        record.setParameter(sUpdateStatement, formatParameter(oParams));
    		    }
    		    else
    		    {
    		        record.setParameter(sUpdateStatement);
    		    }	
    		}
    		
    		// the count of rows is important to decide if we make an update(iCount==1), insert(iCount==0) 
    		// or throw an error because to many rows (iCount>1) are affected from the update.
    		if (iCount == 0)
    		{
    			return insert(pWriteBackTable, pServerMetaData, originalNew);
    		}
    		else if (iCount != 1)
    		{
    			throw new DataSourceException("Update failed ! - Result row count != 1 ! - " +  sUpdateStatement);
    		}
    		
    		if (iColumnCount == 0)
    		{
    			// no storable columns; then.... return oldRow
    			return pOld;
    		}
    		
    		return originalNew;
        }
        finally
        {
            CommonUtil.close(record);
        }
	}
	
	/**
	 * Updates the specified row and returns the count of affected rows. <br>
	 * Database specific implementation should override this method to implement the specific update code.
	 * 
	 * @param pWriteBackTable	the table to use for the update
	 * @param sUpdateStatement	the SQL Statement to use for the update
	 * @param pServerMetaData	the meta data to use.
	 * @param pOld				the old row (values) to use.
	 * @param pNew				the new row (values) to use.
	 * @param pPKFilter			the PrimaryKey equals filter to use.
	 * @return the count of updates rows from a Ansi SQL Database.
	 * @throws DataSourceException
	 *             if an <code>Exception</code> occur during update to the storage
	 */
	public int updateDatabaseSpecific(String pWriteBackTable, String sUpdateStatement, 
			                          ServerMetaData pServerMetaData, Object[] pOld, 
			                          Object[] pNew, ICondition pPKFilter)  throws DataSourceException
	{
		return updateAnsiSQL(pWriteBackTable, sUpdateStatement, pServerMetaData, pOld, pNew, pPKFilter);
	}

	/**
	 * Updates the specified row and return the count of affected rows. <br>
	 * 
	 * @param pWriteBackTable	the table to use for the update
	 * @param sUpdateStatement	the SQL Statement to use for the update
	 * @param pServerMetaData	the meta data to use.
	 * @param pOld				the old row (values) to use.
	 * @param pNew				the new row (values) to use.
	 * @param pPKFilter			the PrimaryKey equals filter to use.
	 * @return the count of updates rows from a Ansi SQL Database.
	 * @throws DataSourceException
	 *             if an <code>Exception</code> occur during update to the storage
	 */
	public int updateAnsiSQL(String pWriteBackTable, String sUpdateStatement, 
                			 ServerMetaData pServerMetaData, Object[] pOld, Object[] pNew, 
                			 ICondition pPKFilter)  throws DataSourceException
	{
		ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
		int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
		
		// update DataRow into database
		PreparedStatement psUpdate = null;
		try
		{
			psUpdate = getPreparedStatement(sUpdateStatement);
			
			if (iTransactionTimeout >= 0)
			{
				try
				{
					psUpdate.setQueryTimeout(iTransactionTimeout);
				}
				catch (Throwable ex)
				{
					// Ignore not implemented Exceptions.
				}
			}

			// set Parameter with storable Column Values
			int iLastParameterIndex = setColumnsToStore(psUpdate, cmdServerColumnMetaData, iaWriteables, pNew, pOld) + 1;
			
			// set WHERE Parameter with Values from PK of the old DataRow
			setFilterParameter(iLastParameterIndex, psUpdate, getParameter(pPKFilter));
			
			setSavepoint();
			
			return psUpdate.executeUpdate();
		}
		catch (SQLException sqlException)
		{
			rollbackToSavepoint();
			
			throw new DataSourceException("Update AnsiSQL failed! - " + sUpdateStatement, formatSQLException(sqlException));
		}
		finally
		{
			releaseSavepoint();
		    CommonUtil.close(psUpdate);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delete(String pWriteBackTable, ServerMetaData pServerMetaData, Object[] pDelete) throws DataSourceException
	{
        Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_DELETE, pWriteBackTable);
        
        try
        {
        	checkIsOpen();
    
            long lMillis = System.currentTimeMillis();

    		if (pWriteBackTable == null)
    		{
    			throw new DataSourceException("Missing WriteBackTable!");
    		}
    
    		if (pServerMetaData.getPrimaryKeyColumnNames() == null || pServerMetaData.getPrimaryKeyColumnNames().length == 0)
    		{
    			throw new DataSourceException("PK Columns empty! - delete not possible!");
    		}
    		
    		ICondition pPKFilter = null;
    		
    		ServerColumnMetaData[] pServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
    		String[] pPKColumns = pServerMetaData.getPrimaryKeyColumnNames();
    		
    		if (pServerColumnMetaData != null)
    		{
    			pPKFilter = Filter.createEqualsFilter(pPKColumns, pDelete, pServerMetaData.getMetaData().getColumnMetaData());
    		}
    		else
    		{
    			// if pColumnMetaData is set, we use the pDelete columns as it is as values for the Equals
    			// over the primary key columns
    			pPKFilter = new Equals(pPKColumns[0], pDelete[0]);
    			for (int i = 1; i < pPKColumns.length; i++)
    			{
    				pPKFilter = pPKFilter.and(new Equals(pPKColumns[i], pDelete[i]));
    			}
    		}
    		
    		StringBuilder sDeleteStatement = new StringBuilder("DELETE FROM ");
    		sDeleteStatement.append(pWriteBackTable);
    		
    		String sWHERE = getWhereClause(pServerMetaData, pPKFilter, null, false);
    		sDeleteStatement.append(sWHERE);
    				
    		// delete DataRow in database
    		PreparedStatement psDelete = null;
    		
    		try
    		{
    			psDelete = getPreparedStatement(sDeleteStatement.toString());
    			
    			if (iTransactionTimeout >= 0)
    			{
    				try
    				{
    					psDelete.setQueryTimeout(iTransactionTimeout);
					}
					catch (Throwable ex)
					{
						// Ignore not implemented Exceptions.
					}
    			}

    		    Object[] oPKFilter = getParameter(pPKFilter);
    		    
    			// set Filter Parameter Values
    			setFilterParameter(1, psDelete, oPKFilter);
    			
    			setSavepoint();
    			
    			int iCount = psDelete.executeUpdate();
    			
    			setModified(Boolean.TRUE);
    			
    			if (isLogEnabled(LogLevel.DEBUG))
    			{
                    debug(sDeleteStatement, "\n", oPKFilter, "\ndeleted row: ", pDelete,
                            "\ncomplete time: ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
    			}

    			if (iCount > 1)
    			{
    				throw new DataSourceException("Delete failed ! - Result row count > 1 ! - " + iCount + "," + sDeleteStatement);
    			}
    			
        		if (record != null)
        		{
        		    if (oPKFilter != null && oPKFilter.length > 0)
        		    {
        		    	 record.setParameter(sDeleteStatement, formatParameter(oPKFilter));
        		    }
        		    else
        		    {
        		    	 record.setParameter(sDeleteStatement);
        		    }
        		}
    		}
    		catch (SQLException sqlException)
    		{
    			rollbackToSavepoint();
    			
    			throw new DataSourceException("Delete failed! - " + sDeleteStatement, formatSQLException(sqlException));
    		}
    		finally
    		{
    			releaseSavepoint();
    		    CommonUtil.close(psDelete);
    		}
        }
        finally
        {
            CommonUtil.close(record);
        }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sbResult = new StringBuilder();
		
		boolean bIsOpen = false;
		
		try 
		{
			bIsOpen = isOpen(false);
		}
		catch (DataSourceException dataSourceException)
		{
		    sbResult.append("::");
		    sbResult.append(dataSourceException.getMessage());

		    return sbResult.toString();
		}
		
        String sShowPassword;
        
        if (sPassword != null)
        {
            sShowPassword = ",Password set";
        }
        else
        {
            sShowPassword = ",Password not set";
        }

        Properties propCopy = new Properties();
		propCopy.putAll(properties);
		
		propCopy.remove("user");
		propCopy.remove("password");
		
		sbResult.append("DBAccess :: Connected=");
		sbResult.append(bIsOpen);
		sbResult.append(", ConnectionString=");
		sbResult.append(sUrl);
		sbResult.append(", DriverName=");
		sbResult.append(sDriver);
		sbResult.append(", UserName=");
		sbResult.append(sUsername);
		sbResult.append(sShowPassword);
		sbResult.append(",Properties=");
		sbResult.append(propCopy);
		sbResult.append("\n");
		
		sbResult.append("ResultSet Cache:\n");
		
		for (Map.Entry<String, Cursor> entry : htFetchResultSetCache.entrySet())
		{
			sbResult.append(entry.getKey());
			sbResult.append("\n");
		}

		sbResult.append(super.toString());
		
		return sbResult.toString();
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the database specific open quote character.
	 * 
	 * @return the open quote character
	 */
	public String getOpenQuoteCharacter()
	{
		return sOpenQuote;
	}
	
	/**
	 * Gets the database specific quote character.
	 * 
	 * @return the close quote character
	 */
	public String getCloseQuoteCharacter()
	{
		return sCloseQuote;
	}
	
	/**
	 * Sets the database specific quote characters.
	 * 
	 * @param pOpen the open quote character
	 * @param pClose the close quote character
	 */
	protected void setQuoteCharacters(String pOpen, String pClose)
	{
		sOpenQuote  = pOpen;
		sCloseQuote = pClose;
	}
	
	/**
	 * Detects for auto quote check, if the identifier starts with a valid character.
	 * 
	 * @param pName the identifier name
	 * @return true, if it is a valid identifier start
	 */
	public boolean isValidIdentifierStart(String pName)
	{
	    return pName.length() > 0 && Character.isJavaIdentifierStart(pName.charAt(0));
	}

	/**
     * Detects for auto qoute check, if the identifier starts with a valid character.
     * 
     * @param pName the identifier name
     * @return true, if it is a valid identifier start
     */
    public boolean isValidIdentifier(String pName)
    {
        if (isValidIdentifierStart(pName))
        {
            for (int i = 1, count = pName.length(); i < count; i++)
            {
                if (!Character.isJavaIdentifierPart(pName.charAt(i)))
                {
                    return false;
                }
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }

	/**
	 * It returns true if this name should be automated quoted. 
	 * e.g. in Oracle default all isUppercase(), so if the name has one loweCase character, then AutoQuote is true to quote this name.
	 * 
	 * @param pName the name to quote.
	 * @return true if this name should be automated quoted.
	 */
	public boolean isAutoQuote(String pName)
	{
		CaseSensitiveType type = StringUtil.getCaseSensitiveType(pName);
		
		return type != CaseSensitiveType.UpperCase || !isValidIdentifier(pName);
	}
	
	/**
	 * Gets whether the given name is quoted.
	 * 
	 * @param pName the name to check
	 * @return <code>true</code> if name is quoted, <code>false</code> otherwise
	 */
	public static boolean isQuoted(String pName)
	{
		if (pName == null)
		{
			return false;
		}
		
		String sName = pName.trim();
		
		if (sName.startsWith(QUOTE) && sName.endsWith(QUOTE))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Quotes a named DB object with our internal quote character, if it should be quoted in this database.
	 * Thats the case if the name != to the default case sensitiveness. e.g. in Oracle != upperCase()
	 * 
	 * @param pName	the name to use. 
	 * @return the quoted name if quoting in this database is necessary or otherwise just the name.
	 */
	public String quote(String pName)
	{
		if (pName == null)
		{
			return null;
		}
		
		if (isAutoQuote(pName))
		{
			if (!isQuoted(pName))
			{
				return quoteAlways(pName);
			}
		}
		
		return pName;
	}
	
	/**
	 * Quotes a named DB object with the JVx DB QUOTE character.
	 * 
	 * @param pName		the name to use. 
	 * @return the name quoted.
	 */
	@Deprecated
	public String quoteAllways(String pName)
	{
		return quoteAlways(pName);
	}
	
	/**
     * Quotes a named DB object with the JVx DB QUOTE character.
     * 
     * @param pName     the name to use. 
     * @return the name quoted.
     */
    public static String quoteAlways(String pName)
    {
        StringBuilder sbResult = pName == null ? new StringBuilder() : new StringBuilder(pName.length() + 8);
        sbResult.append(QUOTE);
        sbResult.append(StringUtil.replace(pName, QUOTE, "\\" + QUOTE));
        sbResult.append(QUOTE);
        return sbResult.toString();
    }

	/**
	 * Removes the JVx DB Quotes of a named DB object.
	 * 
	 * @param pName		the name to use. 
	 * @return the unquoted name.
	 */
	public static String removeQuotes(String pName)
	{
		if (pName == null)
		{
			return null;
		}

		StringBuilder sbResult = new StringBuilder(pName.length()); 
        int startIndex = 0;
        int endIndex = pName.indexOf(QUOTE);

        while (endIndex >= 0)
        {
            if (endIndex > 0 && pName.charAt(endIndex - 1) == '\\')
            {
                sbResult.append(pName.substring(startIndex, endIndex - 1));
                sbResult.append(QUOTE);
            }
            else
            {
                sbResult.append(pName.substring(startIndex, endIndex));
            }
            startIndex = endIndex + QUOTE.length();
            endIndex = pName.indexOf(QUOTE, startIndex);
        }
        sbResult.append(pName.substring(startIndex));
		
		return sbResult.toString();
	}
	
	/**
	 * Removes the DB specific quotes of a named DB object.
	 * 
	 * @param pName		the name to use. 
	 * @return the unquoted name.
	 */
	public String removeDBSpecificQuotes(String pName)
	{
		return StringUtil.removeQuotes(pName, sOpenQuote, sCloseQuote);
	}
	
	/**
	 * It replaces all JVx quotes with the database specific quote.
	 * 
	 * @param pStatement		the statement to use. 
	 * @return the database specific quoted statement.
	 */
	public String translateQuotes(String pStatement)
	{
		if (pStatement == null)
		{
			return null;
		}
		
		StringBuilder sbResult = new StringBuilder(); 
		boolean       bUseOpen = true;
        int startIndex = 0;
        int endIndex = pStatement.indexOf(QUOTE);

        while (endIndex >= 0)
        {
            if (endIndex > 0 && pStatement.charAt(endIndex - 1) == '\\')
            {
                sbResult.append(pStatement.substring(startIndex, endIndex - 1));
                sbResult.append(QUOTE);
            }
            else
            {
                sbResult.append(pStatement.substring(startIndex, endIndex));
                sbResult.append(bUseOpen ? sOpenQuote : sCloseQuote);
                bUseOpen = !bUseOpen;
            }
            
            startIndex = endIndex + QUOTE.length();
            endIndex = pStatement.indexOf(QUOTE, startIndex);
        }
        sbResult.append(pStatement.substring(startIndex));
        
        return sbResult.toString();
	}	
	
	/**
	 * It opens the database and stores the <code>Connection</code> object.
	 * 
	 * @throws DataSourceException
	 *             if the database couldn't opened
	 */
	public void open() throws DataSourceException
	{
		//#607
		if (!isOpen(false))
		{
			ISession sess = SessionContext.getCurrentSession();
			
			if (sess != null)
			{
				sApplicationName = sess.getApplicationName();
			}
			
			if (sApplicationName == null)
			{
				sApplicationName = "";
			}
			
			long lMillis = System.currentTimeMillis();
			
			try
			{

				if (!bExternalConnection)
				{
					if (sUrl == null)
					{
						throw new DataSourceException("Connection String is null!");
					}
				
				    String sPlainUserName = translateQuotes(sUsername);
				    
					Connection con = null;
					
					if (!isJdbc(sUrl))
					{
					    try
					    {
    		                InitialContext ctxt = new InitialContext();
    		                
		                    try
		                    {
		                        Object objInstance = ctxt.lookup(sUrl);
		                        
		                        if (objInstance instanceof Connection)
		                        {
		                            con = (Connection)objInstance;
		                            
		                            bAutoClose = true;
		                            bExternalConnection = true;
		                        }
		                        else if (objInstance instanceof DataSource)
		                        {
		                        	connectionPool = new DataSourceConnectionPool((DataSource)objInstance, sPlainUserName, sPassword);
		                        	
		                            con = connectionPool.getConnection();
		                            
	                                bAutoClose = true;
	                                bExternalConnection = true;
		                        }
		                        else if (objInstance instanceof IConnectionPool)
		                        {
		                        	connectionPool = (IConnectionPool)objInstance;
		                        	
		                        	con = connectionPool.getConnection();
		                            
	                                bAutoClose = true;
	                                bExternalConnection = true;
		                        }
		                        else
		                        {
		                            throw new DataSourceException("Configured JNDI resource '" + sUrl + "' has to be a Connection, but is " + 
		                                                          (objInstance != null ? objInstance.getClass().getName() : "null"));
		                        }
		                    }
		                    finally
		                    {
		                        ctxt.close();
		                    }
					    }
					    catch (Exception ex)
					    {
					        if (ex instanceof DataSourceException)
					        {
					            throw (DataSourceException)ex;
					        }
					        
					        throw new DataSourceException("JNDI URL used, but resource was not found!", ex);
					    }
					}
					else
					{
		                if (sDriver == null)
		                {
		                    throw new DataSourceException("Jdbc Driver is null!");
		                }

		                try
		                {
		                    Class.forName(sDriver);
		                }
		                catch (Exception exception)
		                {
		                    throw new DataSourceException("Jdbc driver not found!", exception);
		                }
					    
	                    Properties propCopy = new Properties();
	                    propCopy.putAll(properties);
	                    
	                    if (sPlainUserName != null)
	                    {
	                        propCopy.setProperty("user", sPlainUserName);
	                    }
	                    
	                    if (sPassword != null)
	                    {
	                        propCopy.setProperty("password", sPassword);
	                    }
					    
					    con = DriverManager.getConnection(sUrl, propCopy);
					}
					
					prepareConnection(con);
					setConnection(con);
				}
				
				initializeMaxColumnLength();
				
				setModified(Boolean.FALSE);
				
				bOpen = true;
				
				//default setting (not all JDBC drivers use auto-commit as default setting, e.g. PostgreSql)
			    connection.setAutoCommit(bAutoCommit);

				fireEventConfigureConnection();

				addDBAccessByIdentifier(getIdentifier(), this);
				
				fireEventAfterOpenDBAccess();
				
	            if (isLogEnabled(LogLevel.DEBUG))
	            {
	                debug("open(", sUrl, ",", translateQuotes(sUsername), ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
	            }
			}
			catch (SQLException sqlException)
			{
				String sShowPassword;
				if (sPassword != null)
				{
					sShowPassword = "Password set";
				}
				else
				{
					sShowPassword = "Password not set";
				}
				
				throw new DataSourceException("Connection failed! - " + sUrl + "; Username=" +
						                      sUsername + "; " + sShowPassword, formatSQLException(sqlException));
			}
		}
		else
		{
			debug("connection is already open!");
		}
	}

	/**
	 * Initalizes the max column length.
	 */
	private void initializeMaxColumnLength()
	{
		try
		{
			iMaxColumnLength = getConnectionIntern().getMetaData().getMaxColumnNameLength();
			// #447 - if getMaxColumnNameLength() return 0, we should interprete it as unlimmited 
			if (iMaxColumnLength == 0)
			{
				iMaxColumnLength = Integer.MAX_VALUE;
			}
		}
		catch (SQLException sqlException)
		{
			iMaxColumnLength = 30;
			
			debug(sqlException);
		}
	}
	
	/**
	 * Returns true, if the database is still open.
	 * 
	 * @return true, if the database is still open.
	 * @throws DataSourceException
	 *             if isClosed() on the DB <code>Connection</code> throws an Exception
	 */
	public boolean isOpen() throws DataSourceException
	{
		return isOpen(bAutoReOpen);
	}
	
	/**
	 * Returns true, if the database is still open.
	 * 
	 * @param pReOpen true, to open the connection if it was detected as closed
	 * @return true, if the database is still open.
	 * @throws DataSourceException
	 *             if isClosed() on the DB <code>Connection</code> throws an Exception
	 */
	private boolean isOpen(boolean pReOpen) throws DataSourceException
	{
		try
		{
			if (bOpen && isConnectionInvalid())
			{
				close();

				if (pReOpen && !bExternalConnection)
				{
					open();
				}
			}
			
			return bOpen;
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Open failed!", formatSQLException(sqlException));
		}
	}
	
	/**
	 * Checks if the internal connection is open and takes care of {@link #isAutoReOpen()}.
	 * 
	 * @throws DataSourceException if the connection is not open
	 */
	protected void checkIsOpen() throws DataSourceException
	{
		if (!isOpen(bAutoReOpen))
		{
			throw new DataSourceException("DBAccess is not open!");
		}
	}
	
	/**
	 * Checks whether the given Exception is an instance of {@link SQLException} throws this Exception in any case. If
	 * the given Exception is not an instance of {@link SQLException}, a {@link RuntimeException} will be thrown in any case.
	 * This method dosn't return anything because it throws an Exception. The return value is for better usage because the
	 * compiler shows a warning if you call this method without throws keyword.
	 * 
	 * @param pRecord the log record if available
	 * @param pException the {@link Exception} to check
	 * @return no return value
	 * @throws SQLException the {@link SQLException} if given <code>pException</code> is an instance of {@link SQLException}
	 * @throws RuntimeException if given <code>pException</code> is not an instance of {@link SQLException}
	 */
	protected SQLException newSQLException(Record pRecord, Exception pException) throws SQLException, 
	                                                                                    RuntimeException	//only for javadoc 
	{
		if (pRecord != null)
		{
			pRecord.setException(pException);
		}

		if (pException instanceof SQLException)
		{
			throw (SQLException)pException;
		}
		else if (pException instanceof RuntimeException)
		{
			throw (RuntimeException)pException;
		}

		throw new RuntimeException(pException);
	}
	
	/**
	 * Checks whether the internal connection is set and not closed.
	 * 
	 * @return <code>true</code> if the internal connection is set and not closed, <code>false</code> otherwise
	 * @throws SQLException if connection check fails
	 */
	protected boolean isConnectionValid() throws SQLException
	{
		return connection != null && !connection.isClosed();
	}
	
	/**
	 * Checks whether the internal connection is set and closed.
	 * 
	 * @return <code>true</code> if the internal connection is set and closed, <code>false</code> otherwise
	 * @throws SQLException if connection check fails
	 */
	protected boolean isConnectionInvalid() throws SQLException
	{
		return connection != null && connection.isClosed();
	}

	/**
	 * Closes the database <code>Connection</code> and releases all memory.
	 * 
	 * @throws DataSourceException if database couldn't closed.
	 */
	public void close() throws DataSourceException
	{
	    if (bOpen)
	    {
	        long lMillis = System.currentTimeMillis();
	        
	    	if (connection != null && !bClosing)
	    	{
		    	bClosing = true;
		    	try
		    	{
			        commitOrRollbackBeforeClose();
			        
			        // Set isOpen after trying to rollback/commit, because those
			        // functions will check if the current DBAccess is still open.
			    	bOpen = false;
		    	}
		    	finally
		    	{
		    		bClosing = false;
		    	}
	
	        	releaseConnection();
	        	
	            if (bAutoClose && connection != null)
	            {
	    	        clearFetchResultSetCache();
	    	        
	   				fireEventUnconfigureConnection();
		            
	            	CommonUtil.close(connection);
	                
	                setConnection(null);
	            }
	    	}
	    	
	    	removeDBAccessByIdentifier(getIdentifier(), this);

	    	fireEventClosedDBAccess();

	    	sIdentifier = null;
	    	
	    	if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("close(", sUrl, ",", translateQuotes(sUsername), ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
	    }
	}
	
	/**
	 * Closes all cached cursors.
	 */
	private void clearFetchResultSetCache()
	{
		synchronized (htFetchResultSetCache)
		{
			for (Map.Entry<String, Cursor> entry : htFetchResultSetCache.entrySet())
			{
				CommonUtil.close(entry.getValue());
			}
			htFetchResultSetCache.clear();
		}
	}
	
	/**
	 * Calls commit or rollback before closing the connection.
	 */
	protected void commitOrRollbackBeforeClose()
	{
        try
        {
            if (!isAutoCommit())
            {
            	rollback();
            }
        }
        catch (DataSourceException sqle)
        {
            debug(sqle);
        }
	}
	
	/**
	 * Sets auto-commit state.
	 * 
	 * @param pEnable <code>true</code> to enable, <code>false</code> to disable auto-commit
	 * @throws DataSourceException if setting state failed
	 */
	public void setAutoCommit(boolean pEnable) throws DataSourceException
	{
		bAutoCommit = pEnable; 
		
		setModified(Boolean.FALSE);
		
		if (isOpen())
		{
		    try
		    {
		    	getConnectionIntern().setAutoCommit(bAutoCommit);
	        }
	        catch (SQLException ex)
	        {
	            throw new DataSourceException("Setting autocommit state failed!", formatSQLException(ex));
	        }
		}
	}

    /**
     * Gets whether auto-commit is en-/disabled.
     * 
     * @return <code>true</code> if enabled, <code>false</code> otherwise
     */
	public boolean isAutoCommit()	
	{
	    return bAutoCommit;
	}
	
	/**
	 * Gets whether the current connection is alive. The connection is alive if
	 * the connection is open and a simple query against the db will work.
	 * 
	 * @return <code>true</code> if db connection is valid and queries will work, <code>false</code> otherwise
	 */
	public boolean isAlive()
	{
		try
		{
			if (isOpen(bAutoReOpen))
			{
				Statement stmt = null;
	    		
	    		try
	    		{
    				stmt = getConnectionIntern().createStatement();
    				
    				setSavepoint();
    				
    				stmt.executeQuery(getAliveQuery());
    				
    				return true;
	    		}
	    		catch (SQLException e)
	    		{
	    			rollbackToSavepoint();
	    			
	    			throw e;
	    		}
	    		finally
	    		{
	    			releaseSavepoint();
	    			
	    		    CommonUtil.close(stmt);		    
	    		}
			}
		}
		catch (Exception e)
		{
			debug(e);
		}
		
		return false;
	}
	
	/**
	 * Rollback the DB transaction.
	 * 
	 * @throws DataSourceException if the transaction couldn't rollback
	 */
	public void rollback() throws DataSourceException
	{
		checkIsOpen();

		try
		{
			// We don't need to rollback to the Savepoint (if any), because
			// the rollback on the connection level will undo everything.
			releaseSavepoint();
			
			if (isConnectionValid())
			{
				connection.rollback();	
			}
			
            setModified(Boolean.FALSE);

			releaseConnectionIntern();
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Rollback failed!", formatSQLException(sqlException));
		}
	}

	/**
	 * Commits the DB transaction.
	 * 
	 * @throws DataSourceException
	 *             if the transaction couldn't commit
	 */
	public void commit() throws DataSourceException
	{
		checkIsOpen();

		try
		{
			releaseSavepoint();
			
			if (isConnectionValid())
			{
				connection.commit();	
			}
			
			setModified(Boolean.FALSE);
			
			releaseConnectionIntern();
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Commit failed!", formatSQLException(sqlException));
		}
	}
	
	/**
	 * Gets the query time out.
	 * default is -1
	 * a value &lt; 0 means, it is not set on statement
	 * a value &gt; 0 means the time out in seconds.
	 * 
	 * @return the timeout in seconds
	 */
	public int getQueryTimeout()
	{
		return iQueryTimeout;
	}
	
	/**
	 * Sets the query time out in seconds.
	 * default is -1
	 * a value &lt; 0 means, it is not set on statement
	 * a value &gt; 0 means the time out in seconds.
	 * 
	 * @param pQueryTimeout
	 * 				the timeout in seconds
	 */
	public void setQueryTimeout(int pQueryTimeout)
	{
		iQueryTimeout = pQueryTimeout;
	}
	
	/**
	 * Gets the transaction time out.
	 * default is -1
	 * 0 (no timeout)
	 * a value &lt; 0 means, it is not set on statement
	 * a value &gt; 0 means the time out in seconds.
	 * 
	 * @return the timeout in seconds
	 */
	public int getTransactionTimeout()
	{
		return iTransactionTimeout;
	}
	
	/**
	 * Sets the transaction time out.
	 * default is -1
	 * 0 (no timeout)
	 * a value &lt; 0 means, it is not set on statement
	 * a value &gt; 0 means the time out in seconds.
	 * 
	 * @param pTransactionTimeout
	 * 				the timeout in seconds
	 */
	public void setTransactionTimeout(int pTransactionTimeout)
	{
		iTransactionTimeout = pTransactionTimeout;
	}
	
	/**
	 * Returns the <code>connection</code> to the database.
	 * 
	 * @return the <code>connection</code> to the database.
	 * @throws SQLException if connection is not available.
	 */
	public Connection getConnection() throws SQLException
	{
		cachedMetaDataIdentifier = null; // Clear temporary meta data, in case the statement changes ddl.

		Connection conn = getConnectionIntern();
		
		setModified(null); // Modified state is unknown
		
		return conn;
	}	
	
	/**
	 * Returns the <code>connection</code> to the database.
	 * This includes getting the connection from the pool, if needed.
	 * 
	 * @return the <code>connection</code> to the database.
	 * @throws SQLException if connection is not available.
	 */
	protected Connection getConnectionIntern() throws SQLException
	{
		if (connection == null && connectionPool != null)
		{
		    Connection conPool = connectionPool.getConnection();
		    
			prepareConnection(conPool);
			setConnection(conPool);
			
			connection.setAutoCommit(bAutoCommit);
			
			fireEventConfigureConnection();
		}
        releaseConnectionIntern();
		
		return connection;
	}	
	
	/**
	 * Returns the <code>connection</code> to the connection pool.
	 * 
	 * @throws DataSourceException if the DBAccess is in modified state, and the connection cannot be released.
	 */
	public void releaseConnection() throws DataSourceException
	{
		if (isModified())
		{
			throw new DataSourceException("Connection is modified and cannot be released!");
		}
		
		if (connectionPool != null && connection != null)
		{
			clearFetchResultSetCache();
			
			fireEventUnconfigureConnection();
			
			connectionPool.releaseConnection(connection);

			setConnection(null);
		}
	}	
	
	/**
	 * True, if there is a release connection task waiting.
	 * @return True, if there is a release connection task waiting.
	 */
	public boolean isReleaseConnectionPending()
	{
		return bReleaseConnectionPending;
	}
	
	/**
	 * Returns the <code>connection</code> to the connection pool.
	 */
	protected void releaseConnectionIntern()
	{
		if (connectionPool != null && connection != null 
				&& isConnectionPoolEnabled() && !isReleaseConnectionPending())
		{
			SessionContext sessionContext = SessionContext.getCurrentInstance();
			
			if (sessionContext != null)
			{
				bReleaseConnectionPending = true;
				
				sessionContext.getCallHandler().invokeFinally(new Runnable() 
				{
					public void run() 
					{
						bReleaseConnectionPending = false;

						if (isConnectionPoolEnabled() && !isModified())
						{
							try 
							{
								releaseConnection();
							} 
							catch (DataSourceException e) 
							{
								debug(e);
							}
						}
					}
				});
			}
		}
	}	
	
	/**
	 * Sets the internal connection to the database.
	 * 
	 * @param pConnection the connection to the database
	 */
	protected void setConnection(Connection pConnection)
	{
		connection = pConnection;
		
		setModified(Boolean.FALSE);
	}
	
	/**
	 * Returns the <code>conncetion</code> to the database.
	 * 
	 * @return the <code>conncetion</code> to the database.
	 */
	public IConnectionPool getConnectionPool()
	{
		return connectionPool;
	}	
	
	/**
	 * Sets the internal connection pool to the database.
	 * 
	 * @param pConnectionPool the connection pool to the database
	 */
	protected void setConnectionPool(IConnectionPool pConnectionPool)
	{
		connectionPool = pConnectionPool;
		
		releaseConnectionIntern();
	}
	
	/**
	 * Gets the method of connection pool usage.<br>
	 * ReleaseOnClose: releases the connection, when the DBAccess is closed.
	 * This is the default behaviour.<br>
	 * ReleaseOnCommitRollback: releases the connection, when the commit or rollback is closed.<br>
	 * ReleaseAfterLastCall: releases the connection, after the last call.
	 * 
	 * @return the connection pool method
	 */
	public boolean isConnectionPoolEnabled()
	{
		return bConnectionPoolEnabled;
	}
	
	/**
	 * Sets the method of connection pool usage.<br>
	 * ReleaseOnClose: releases the connection, when the DBAccess is closed.
	 * This is the default behaviour.<br>
	 * ReleaseOnCommitRollback: releases the connection, when the commit or rollback is closed.<br>
	 * ReleaseAfterLastCall: releases the connection, after the last call.
	 * 
	 * @param pConnectionPoolEnabled the connection pool method
	 */
	public void setConnectionPoolEnabled(boolean pConnectionPoolEnabled)
	{
		bConnectionPoolEnabled = pConnectionPoolEnabled;
		
		releaseConnectionIntern();
	}
	
	/**
	 * The event configure connection is always dispatched, when a new connection is used by this DBAccess.
	 * This can either be due to opening the DBAccess or due to getting a new connection from the connection pool.
	 * 
	 * @return return the event handler for configuring the
	 */
	public EventHandler<IConfigureConnectionListener> eventConfigureConnection()
	{
		if (eventConfigureConnection == null)
		{
			eventConfigureConnection = new EventHandler<IConfigureConnectionListener>(IConfigureConnectionListener.class);
		}
		
		return eventConfigureConnection;
	}
	
	/**
	 * The event configure connection is always dispatched, when a new connection is used by this DBAccess.
	 * This can either be due to opening the DBAccess or due to getting a new connection from the connection pool.
	 * 
	 * @return return the event handler for configuring the
	 */
	public EventHandler<IUnconfigureConnectionListener> eventUnconfigureConnection()
	{
		if (eventUnconfigureConnection == null)
		{
			eventUnconfigureConnection = new EventHandler<IUnconfigureConnectionListener>(IUnconfigureConnectionListener.class);
		}
		
		return eventUnconfigureConnection;
	}
	
	/**
	 * Fires the event configureConnection.
	 * 
	 * @throws SQLException if the event causes an exception.
	 */
	protected void fireEventConfigureConnection() throws SQLException
	{
		if (eventConfigureConnection != null && eventConfigureConnection.isDispatchable())
		{
			try 
			{
				eventConfigureConnection.dispatchEvent(new ConnectionEvent(this, connection));
			} 
			catch (Throwable ex) 
			{
				throw new SQLException("Configure connection failed!", ex);
			}
		}
	}
	
	/**
	 * Fires the event unconfigureConnection.
	 */
	protected void fireEventUnconfigureConnection()
	{
		if (eventUnconfigureConnection != null && eventUnconfigureConnection.isDispatchable())
		{
			try 
			{
				eventUnconfigureConnection.dispatchEvent(new ConnectionEvent(this, connection));
			} 
			catch (Throwable ex) 
			{
            	info(ex);
			}
		}
	}
	
	/**
	 * Gets the database driver name as <code>String</code>.
	 * 
	 * @return pDriverName the database driver name
	 */
	public String getDriver()
	{
		return sDriver;
	}

	/**
	 * Sets the database driver name as <code>String</code>.
	 * 
	 * @param pDriver the database driver name
	 */
	public void setDriver(String pDriver)
	{
		sDriver = pDriver;
	}

	/**
	 * Gets the jdbc url <code>String</code> for this database.
	 * 
	 * @return	the jdbc url <code>String</code>.
	 */
	public String getUrl()
	{
		return sUrl;
	}

	/**
	 * Sets the url <code>String</code> for this database.
	 * 
	 * @param pUrl	the jdbc url <code>String</code>.
	 */
	public void setUrl(String pUrl)
	{
		sUrl = pUrl;
	}

	/**
	 * Gets the user name to connect with.
	 * 
	 * @return pUser	the user name
	 */
	public String getUsername()
	{
		return sUsername;
	}

	/**
	 * Sets the user name to connect with.
	 * 
	 * @param pUsername  the user name
	 */
	public void setUsername(String pUsername)
	{
		sUsername = pUsername;
	}

	/**
	 * Sets the password to use for the connection to the database.
	 * 
	 * @return pPassword the password to use for the database
	 */
	public String getPassword()
	{
		return sPassword;
	}

	/**
	 * Sets the password to use for the connection to the database.
	 * 
	 * @param pPassword the password to use for the database
	 */
	public void setPassword(String pPassword)
	{
		sPassword = pPassword;
	}

	/**
	 * Sets a specific database property.
	 * 
	 * @param pName the property name
	 * @param pValue th value
	 */
	public void setDBProperty(String pName, String pValue)
	{
		if (pValue == null)
		{
			properties.remove(pName);
		}
		else
		{
			properties.setProperty(pName, pValue);
		}
	}
	
	/**
	 * Gets the value for a specific database property.
	 * 
	 * @param pName the property name
	 * @return the value or <code>null</code> if the property was not found
	 */
	public String getDBProperty(String pName)
	{
		return properties.getProperty(pName);
	}
	
	/**
	 * Sets DB specific initial parameters for the <code>Connection</code> creation.
	 * 
	 * @param pProperties
	 *            DB specific initial parameters
	 */
	public void setDBProperties(Properties pProperties)
	{
		properties = pProperties;
		
		Object oKey;
		Object oValue;
		
		//#1255
		//it's possible that there are non strings in the Properties because it's a Hashtable.
		//this could happen only programatically -> correct properties
		Properties propCopy = new Properties();
		propCopy.putAll(pProperties);
		
		for (Entry<Object, Object> entry : propCopy.entrySet())
		{
		    oKey = entry.getKey();
		    oValue = entry.getValue();
		    
		    if (!(oKey instanceof String))
		    {
                properties.remove(oKey);

                oKey = oKey.toString();
                oValue = oValue != null ? oValue.toString() : "";
                
                properties.put(oKey, oValue);
		    }
		    
		    if (!(oValue instanceof String))
		    {
		        properties.put(oKey, oValue != null ? oValue.toString() : "");
		    }
		}
	}

	/**
	 * Returns the DB specific initial parameters for the <code>Connection</code> creation.
	 * 
	 * @return the DB specific initial parameters for the <code>Connection</code> creation.
	 */
	public Properties getDBProperties()
	{
		return properties;
	}

	/**
	 * Returns the maximum time in milliseconds to use, to try to fetch all rows. reduce open cursors, and increase performance.
	 *
	 * @return the iMaxTime.
	 */
	public int getMaxTime()
	{
		return iMaxTime;
	}

	/**
	 * Sets the maximum time in milliseconds to use, to try to fetch all rows. reduce open cursors, and increase performance.
	 *
	 * @param pMaxTime the iMaxTime to set
	 */
	public void setMaxTime(int pMaxTime)
	{
		iMaxTime = pMaxTime;
	}
	
	/**
	 * Returns the maximum allowed column length.
	 *
	 * @return the iMaxColumnLength.
	 */
	public int getMaxColumnLength()
	{
		return iMaxColumnLength;
	}

	/**
	 * Gets the column meta data creator for custom column meta data support.
	 * 
	 * @return the column meta data creator for custom column meta data support.
	 */
	public IColumnMetaDataCreator getColumnMetaDataCreator()
	{
		return columnMetaDataCreator;
	}

	/**
	 * Sets the column meta data creator for custom column meta data support.
	 * 
	 * @param pColumnMetaDataCreator the column meta data creator for custom column meta data support.
	 */
	public void setColumnMetaDataCreator(IColumnMetaDataCreator pColumnMetaDataCreator)
	{
		columnMetaDataCreator = pColumnMetaDataCreator;
	}

	/**
	 * Sets the column meta data creator for custom column meta data support.
	 * 
	 * @param pListener the listener instance.
	 * @param pMethodName the method name.
	 */
	public void setColumnMetaDataCreator(Object pListener, String pMethodName)
	{
	    if (columnMetaDataCreatorProvider == null)
	    {
	        columnMetaDataCreatorProvider = new EventHandler<IColumnMetaDataCreator>(IColumnMetaDataCreator.class);	        
	    }
	    
		columnMetaDataCreator = columnMetaDataCreatorProvider.createListener(pListener, pMethodName);
	}

	/**
	 * Executes a DB procedure with the specified parameters.
	 * 
	 * @param pProcedureName the procedure (optional with package) name. 
	 * @param pParameters the parameters to use with the correct and corresponding java type.
	 * @throws SQLException if the call failed.
	 */
	public void executeProcedure(String pProcedureName, Object... pParameters) throws SQLException
	{
		Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_PROCEDURE);
		
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
        		    record.setParameter(pProcedureName, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pProcedureName);
    		    }
    		}
    		
    		checkIsOpen();
    		
    		StringBuilder sqlStatement = new StringBuilder("{ call ");
    		sqlStatement.append(pProcedureName);
    		
    		if (pParameters != null && pParameters.length > 0)
    		{
    			sqlStatement.append("(");
    			for (int i = 0; i < pParameters.length; i++)
    			{
    				if (i > 0)
    				{
    					sqlStatement.append(", ");			
    				}
    				sqlStatement.append("?");			
    			}
    			sqlStatement.append(")");
    		}
    		sqlStatement.append(" }");
    		
            String sStmt = translateQuotes(sqlStatement.toString());
            debug("executeProcedure -> ", sStmt, pParameters);
            
    		CallableStatement call = null;
    		
    		try
    		{
    			call = getConnection().prepareCall(sStmt);
    			
    			if (pParameters != null)
    			{
        			for (int i = 0; i < pParameters.length; i++)
        			{
        				if (pParameters[i] == null)
        				{
        					call.setNull(i + 1, Types.VARCHAR);
        				}
        				else
        				{
        					if (pParameters[i] instanceof AbstractParam)
        					{
        					    AbstractParam apParam = (AbstractParam)pParameters[i];
        						
        						if (AbstractParam.isOutParam(apParam.getType()))
        						{
        							call.registerOutParameter(i + 1, apParam.getSqlType());
        						}
        						
        						if (apParam.getValue() == null)
        						{
        							call.setNull(i + 1, apParam.getSqlType());
        						}
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    call.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam));
                                }
        						else
        						{
        							call.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
        						}
        					}
        					else
        					{
        						call.setObject(i + 1, convertValueToDatabaseSpecificObject(pParameters[i]));
        					}
        				}
        			}
    			}
    			
    			setSavepoint();
    			
    			call.execute();
    			
    			if (pParameters != null)
                {
        			for (int i = 0; i < pParameters.length; i++)
        			{
        				if (pParameters[i] instanceof AbstractParam)
        				{
        				    AbstractParam apParam = (AbstractParam)pParameters[i];
        					
                            if (AbstractParam.isOutParam(apParam.getType()))
        					{
        						apParam.setValue(call.getObject(i + 1));
        					}
        				}
        			}
                }
    		}
    		catch (SQLException e)
    		{
    			rollbackToSavepoint();
    			
    			throw e;
    		}
    		finally 
    		{
    			releaseSavepoint();
    			
    		    CommonUtil.close(call);
    		}
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
	 * Executes a DB function with the specified parameters and return the result.
	 * 
	 * @param pFunctionName the function (optional with package) name. 
	 * @param pReturnType the return SQL Type (see {@link Types}
	 * @param pParameters the parameters to use with the correct and corresponding java type.
	 * @return the result of the DB function call.
	 * @throws SQLException	if the call failed.
	 */
	public Object executeFunction(String pFunctionName, int pReturnType, Object... pParameters) throws SQLException
	{
		return executeFunction(pFunctionName, new OutParam(pReturnType), pParameters);
	}
	
	/**
	 * Executes a DB function with the specified parameters and return the result.
	 * 
	 * @param pFunctionName the function (optional with package) name. 
	 * @param pReturnOutParam the return SQL Type (see {@link Types}
	 * @param pParameters the parameters to use with the correct and corresponding java type.
	 * @return the result of the DB function call.
	 * @throws SQLException	if the call failed.
	 */
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
        		    record.setParameter(pFunctionName, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pFunctionName);
    		    }
    		}
    		
    		checkIsOpen();
    		
    		StringBuilder sqlStatement = new StringBuilder("{ ? = call ");
    		sqlStatement.append(pFunctionName);
    		
    		if (pParameters != null && pParameters.length > 0)
    		{
    			sqlStatement.append("(");
    			for (int i = 0; i < pParameters.length; i++)
    			{
    				if (i > 0)
    				{
    					sqlStatement.append(", ");
    				}
    				sqlStatement.append("?");
    			}
    			sqlStatement.append(")");
    		}
    		sqlStatement.append(" }");
    		
            String sStmt = translateQuotes(sqlStatement.toString());
            debug("executeFunction -> ", sStmt, pParameters);
            
    		CallableStatement call = null;

    		try
    		{
    			call = getConnection().prepareCall(sStmt);
    			call.registerOutParameter(1, pReturnOutParam.getSqlType());
    			
    			if (pParameters != null)
    			{
        			for (int i = 0; i < pParameters.length; i++)
        			{
        				if (pParameters[i] == null)
        				{
        					call.setNull(i + 2, Types.VARCHAR);
        				}
        				else
        				{
        					if (pParameters[i] instanceof AbstractParam)
        					{
        					    AbstractParam apParam = (AbstractParam)pParameters[i];
        						
        						if (AbstractParam.isOutParam(apParam.getType()))
        						{
        							call.registerOutParameter(i + 2, apParam.getSqlType());
        						}
        						
        						if (apParam.getValue() == null)
        						{
        							call.setNull(i + 2, apParam.getSqlType());
        						}
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    call.setObject(i + 2, convertValueToDatabaseSpecificObject(apParam));
                                }
        						else
        						{
        							call.setObject(i + 2, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
        						}
        					}
        					else
        					{
        						call.setObject(i + 2, convertValueToDatabaseSpecificObject(pParameters[i]));
        					}
        				}
        			}
    			}
    			
    			setSavepoint();
    			
    			if (call.execute())
    			{
    			    CommonUtil.close(call.getResultSet());
    			}
    			
    			Object oResult = call.getObject(1);
    			
    			for (int i = 0; pParameters != null && i < pParameters.length; i++)
    			{
    				if (pParameters[i] instanceof AbstractParam)
    				{
    				    AbstractParam apParam = (AbstractParam)pParameters[i];
    					
    					if (AbstractParam.isOutParam(apParam.getType()))
    					{
    						apParam.setValue(call.getObject(i + 2));
    					}
    				}
    			}
    			
    			pReturnOutParam.setValue(oResult);
    			
    			return oResult;
    		}
    		catch (SQLException e)
    		{
    			rollbackToSavepoint();
    			
    			throw e;
    		}
    		finally 
    		{
    			releaseSavepoint();
    		    CommonUtil.close(call);
    		}
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
	 * Executes a DDL or DML Statement.
	 * 
	 * @param pStatement the statement.
	 * @param pParameters the parameters.
	 * @return the number of affected rows.
	 * @throws SQLException	if an error occur during execution.
	 */
	public int executeStatement(String pStatement, Object... pParameters) throws SQLException
	{
		Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_STATEMENT, pStatement);
		
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
        		    record.setParameter(pStatement, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pStatement);
    		    }
    		}
    		
    		checkIsOpen();
    		
            StringBuilder sbStmt = new StringBuilder(pStatement);
            
            pParameters = replaceNullParameter(sbStmt, pParameters);
            
			String sStmt = translateQuotes(sbStmt.toString());
			debug("executeStatement -> ", sStmt, pParameters);

			if (pParameters == null || pParameters.length == 0)
			{
				Statement stmt = null;
	    		
	    		try
	    		{
    				stmt = getConnection().createStatement();
    				
    				setSavepoint();
    				
    				return stmt.executeUpdate(sStmt);
	    		}
	    		catch (SQLException e)
	    		{
	    			rollbackToSavepoint();
	    			
	    			throw e;
	    		}
	    		finally
	    		{
	    			releaseSavepoint();
	    		    CommonUtil.close(stmt);		    
	    		}
			}
			else
			{
				PreparedStatement psStmt = null;
				
				try
				{
					psStmt = getConnection().prepareStatement(sStmt);
				
    				for (int i = 0; i < pParameters.length; i++)
    				{
    					if (pParameters[i] == null)
    					{
    						psStmt.setNull(i + 1, Types.VARCHAR);
    					}
    					else
    					{
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (apParam.getValue() == null)
                                {
                                    psStmt.setNull(i + 1, apParam.getSqlType());
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else
                            {
                                psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
    					}
    				}
        			
    				setSavepoint();
    				
        			return psStmt.executeUpdate();
	    		}
	    		catch (SQLException e)
	    		{
	    			rollbackToSavepoint();
	    			
	    			throw e;
	    		}
	    		finally
	    		{
	    			releaseSavepoint();
	    		    CommonUtil.close(psStmt);		    
	    		}
			}
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
	 * Executes an Insert Statement, and returns the given columns.
	 * 
	 * @param pStatement the statement.
	 * @param pReturnColumns the columns to return.
	 * @param pParameters the parameters.
	 * @return the list of returned columns.
	 * @throws SQLException	if an error occur during execution.
	 */
	public List<Bean> executeInsertStatement(String pStatement, String[] pReturnColumns, Object... pParameters) throws SQLException
	{
		Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_STATEMENT, pStatement);
		
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
        		    record.setParameter(pStatement, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pStatement);
    		    }
    		}
    		
    		checkIsOpen();
    		
            StringBuilder sbStmt = new StringBuilder(pStatement);
            
            pParameters = replaceNullParameter(sbStmt, pParameters);
            
			String sStmt = translateQuotes(sbStmt.toString());
			debug("executeInsertStatement -> ", sStmt, pParameters);

			PreparedStatement psStmt = null;
			
			try
			{
			    if (pReturnColumns == null)
			    {
			        psStmt = getConnection().prepareStatement(sStmt, Statement.RETURN_GENERATED_KEYS);
			    }
			    else
			    {
			        psStmt = getConnection().prepareStatement(sStmt, pReturnColumns);
			    }
			
			    if (pParameters != null)
			    {
    				for (int i = 0; i < pParameters.length; i++)
    				{
    					if (pParameters[i] == null)
    					{
    						psStmt.setNull(i + 1, Types.VARCHAR);
    					}
    					else
    					{
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (apParam.getValue() == null)
                                {
                                    psStmt.setNull(i + 1, apParam.getSqlType());
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else
                            {
                                psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
    					}
    				}
			    }
    			
				List<Bean> liResult = new ArrayUtil<Bean>();
    			
    			setSavepoint();
    			
    			if (psStmt.executeUpdate() > 0)
    			{
    				fillResultSetIntoListOfBean(psStmt.getGeneratedKeys(), liResult, pReturnColumns);
				}
			
				return liResult;
    		}
    		catch (SQLException e)
    		{
    			rollbackToSavepoint();
    			
    			throw e;
    		}
    		finally
    		{
    			releaseSavepoint();
    		    CommonUtil.close(psStmt);		    
    		}
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
	 * Fills the ResuoltSet into the given list.
	 * @param pResultSet the ResultSet
	 * @param pList the list
	 * @param pColumnNames the column names
	 * @throws SQLException if it fails.
	 */
	protected void fillResultSetIntoListOfBean(ResultSet pResultSet, List<Bean> pList, String[] pColumnNames) throws SQLException
	{
		int count = 0;
		BeanType bt = null;
		try
		{
			while (pResultSet.next())
			{
				if (bt == null)
				{
					bt = new BeanType();
					if (pColumnNames == null)
					{
						ResultSetMetaData md = pResultSet.getMetaData();
						count = md.getColumnCount();
	
						for (int i = 1; i <= count; i++)
						{
							bt.addPropertyDefinition(md.getColumnLabel(i).toUpperCase()); // Queries should deliver uppercase columns, to match with DBStorage columns.
						}
					}
					else
					{
						count = pColumnNames.length;
						
						for (int i = 0; i < count; i++)
						{
							bt.addPropertyDefinition(pColumnNames[i]);
						}
					}
				}
				
				Bean row = new Bean(bt);
				for (int i = 0; i < count; i++)
				{
					row.put(i, getObjectFromResultSet(pResultSet, i + 1));
				}
				
				pList.add(row);
			}
		}
		finally
		{
		    CommonUtil.close(pResultSet);
		}
	}
	
	/**
	 * Executes a SQL query as prepared statement.
	 * 
	 * @param pStatement the statement with or without parameters
	 * @param pParameters the parameters to use
     * @return a list of bean for all selected columns and rows.
	 * @throws SQLException if an error occurs during execution
	 */
	public List<Bean> executeQuery(String pStatement, Object... pParameters) throws SQLException
	{
		Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_QUERY);
		
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
        		    record.setParameter(pStatement, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pStatement);
    		    }
    		}

    		checkIsOpen();
    		
    		StringBuilder sbStmt = new StringBuilder(pStatement);
    		
    		pParameters = replaceNullParameter(sbStmt, pParameters);
    		
            String sStmt = translateQuotes(sbStmt.toString());
            debug("executeQuery -> ", sStmt, pParameters);

    		PreparedStatement psStmt = null;
    		try
    		{
    			psStmt = getConnectionIntern().prepareStatement(sStmt);
    			
    			if (pParameters != null)
    			{
    				for (int i = 0; i < pParameters.length; i++)
    				{
    					if (pParameters[i] == null)
    					{
    						psStmt.setNull(i + 1, Types.VARCHAR);
    					}
    					else
    					{
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (apParam.getValue() == null)
                                {
                                    psStmt.setNull(i + 1, apParam.getSqlType());
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else
                            {
                                psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
    					}
    				}
    			}
    			
    			List<Bean> liResult = new ArrayUtil<Bean>();
    			
    			setSavepoint();
    			
    			psStmt.setFetchSize(500);

    			if (psStmt.execute())
    			{
    				fillResultSetIntoListOfBean(psStmt.getResultSet(), liResult, null);
    			}
    			
                if (record != null)
                {
                    record.setCount(liResult.size());
                }
    			return liResult;
    		}
    		catch (SQLException e)
    		{
    			rollbackToSavepoint();
    			
    			throw e;
    		}
    		finally
    		{
    			releaseSavepoint();
    		    CommonUtil.close(psStmt);		    
    		}
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
     * Executes a SQL command as prepared statement.
     * 
     * @param pStatement the statement with or without parameters
     * @param pParameters the parameters to use
     * @return the first parameter from the result set, for every row, or <code>null</code> if the command returned no
     *         result
     * @throws SQLException if an error occurs during execution
     */
	public List<Object> executeSql(String pStatement, Object... pParameters) throws SQLException
	{
	    return executeSqlMulti(pStatement, 0, pParameters);
	}
	
	/**
	 * Executes a SQL command as prepared statement.
	 * 
	 * @param pStatement the statement with or without parameters
	 * @param pResultColumn the column index which contains the result value. If you set <code>-1</code> all column
	 *                      values will be returned as <code>Object[]</code>
	 * @param pParameters the parameters to use
	 * @return the first parameter from the result set, for every row, or <code>null</code> if the command returned no
	 *         result
	 * @throws SQLException if an error occurs during execution
	 */
	protected List<Object> executeSqlMulti(String pStatement, int pResultColumn, Object... pParameters) throws SQLException
	{
		Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_EXEC_SQL);
		
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
        		    record.setParameter(pStatement, formatParameter(pParameters));
    		    }
    		    else
    		    {
    		        record.setParameter(pStatement);
    		    }
    		}

    		checkIsOpen();
    		
            StringBuilder sbStmt = new StringBuilder(pStatement);
            
            pParameters = replaceNullParameter(sbStmt, pParameters);
            
            String sStmt = translateQuotes(sbStmt.toString());
            debug("executeSql -> ", sStmt, pParameters);

    		PreparedStatement psStmt = null;
    		try
    		{
    			psStmt = getConnectionIntern().prepareStatement(sStmt);
    			
    			if (pParameters != null)
    			{
    				for (int i = 0; i < pParameters.length; i++)
    				{
    					if (pParameters[i] == null)
    					{
    						psStmt.setNull(i + 1, Types.VARCHAR);
    					}
    					else
    					{
                            if (pParameters[i] instanceof AbstractParam)
                            {
                                AbstractParam apParam = (AbstractParam)pParameters[i];
        
                                if (apParam.getValue() == null)
                                {
                                    psStmt.setNull(i + 1, apParam.getSqlType());
                                }
                                else if (apParam.getSqlType() == AbstractParam.SQLTYPE_AUTOMATIC)
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam));
                                }
                                else
                                {
                                    psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(apParam), apParam.getSqlType());
                                }
                            }
                            else
                            {
                                psStmt.setObject(i + 1, convertValueToDatabaseSpecificObject(pParameters[i]));
                            }
    					}
    				}
    			}
    			
    			setSavepoint();
    			
    			if (psStmt.execute())
    			{
    				ResultSet res = psStmt.getResultSet();
    				
    				try
    				{
        				if (res.next())
        				{
        					List<Object> liResult = new ArrayUtil<Object>();
        
        					do
        					{
        					    if (pResultColumn >= 0)
        					    {
        					        liResult.add(getObjectFromResultSet(res, pResultColumn + 1));
        					    }
        					    else
        					    {
        					        int iRecords = res.getMetaData().getColumnCount();
        					        
        					        Object[] oRecord = new Object[iRecords];
        					        
                                    for (int i = 0, cnt = iRecords; i < cnt; i++)
                                    {
                                        oRecord[i] = getObjectFromResultSet(res, i + 1);
                                    }
                                    
                                    liResult.add(oRecord);
        					    }
        					}
        					while (res.next());
        					
       		                if (record != null)
    		                {
    		                    record.setCount(liResult.size());
    		                }
        					
        					return liResult;
        				}
    				}
    				finally
    				{
    				    CommonUtil.close(res);
    				}
    			}
    			
    			return null;
    		}
    		catch (SQLException e)
    		{
    			rollbackToSavepoint();
    			
    			throw e;
    		}
    		finally
    		{
    			releaseSavepoint();
    		    CommonUtil.close(psStmt);		    
    		}
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
	 * Return a <code>PreparedStatement</code> for the given SQL statement.<br>
	 * 
	 * @param pSqlStatement
	 *            the SQL statement to prepare
	 * @return a <code>PreparedStatement</code> for the given SQL statement.
	 * @throws SQLException
	 *             if the statement couldn't prepared.
	 */
	public PreparedStatement getPreparedStatement(String pSqlStatement) throws SQLException
	{
		return getPreparedStatement(pSqlStatement, false);
	}
	
	/**
	 * Return a <code>PreparedStatement</code> for the given SQL statement.<br>
	 * 
	 * @param pSqlStatement
	 *            the SQL statement to prepare
	 * @param pReturnGeneratedKeys
	 *            if the generated key in insert statements should returned
	 * @return a <code>PreparedStatement</code> for the given SQL statement.
	 * @throws SQLException
	 *             if the statement couldn't prepared.
	 */
	protected PreparedStatement getPreparedStatement(String pSqlStatement, boolean pReturnGeneratedKeys) throws SQLException
	{
		PreparedStatement psSqlStatement;
		
		pSqlStatement = translateQuotes(pSqlStatement);

		if (pReturnGeneratedKeys)
		{
			try 
			{
				psSqlStatement = getConnectionIntern().prepareStatement(pSqlStatement, Statement.RETURN_GENERATED_KEYS);
			}
			catch (SQLException sqlException)
			{
				psSqlStatement = getConnectionIntern().prepareStatement(pSqlStatement);
			}					
		}
		else
		{
			psSqlStatement = getConnectionIntern().prepareStatement(pSqlStatement);
		}

		return psSqlStatement;
	}	

	/**
	 * Releases the {@link #currentSavepoint currently held Savepoint}.
	 * <p>
	 * This function should be called when the
	 * {@link #currentSavepoint currently held Savepoint} will no longer be used
	 * and can be safely disposed of. Ideally this is done after the execution
	 * was successfull.
	 * <p>
	 * The {@link Savepoint} will only be released when the
	 * {@link #isUseSavepoints() Savepoint support is currently enabled}. Errors
	 * during the release are only logged, {@link #currentSavepoint} will always
	 * be set to {@code #null}.
	 * 
	 * @see #currentSavepoint
	 * @see #isUseSavepoints()
	 * @see #rollbackToSavepoint()
	 * @see #setSavepoint()
	 * @see #setUseSavepoints(boolean)
	 */
	protected void releaseSavepoint()
	{
		if (currentSavepoint != null)
		{
			try
			{
				if (isConnectionValid())
				{
					connection.releaseSavepoint(currentSavepoint);
				}
			}
			catch (SQLException e)
			{
				error(e);
			}
			
			currentSavepoint = null;
		}
	}
	
	/**
	 * Rolls back to the {@link #currentSavepoint currently held Savepoint}.
	 * <p>
	 * This function should only be called when the execution of a statement
	 * fails.
	 * <p>
	 * The rollback will only be executed when the
	 * {@link #isUseSavepoints() Savepoint support is currently enabled}. Errors
	 * during the rollback are only logged, that is because this function should
	 * only be called in an error scenario so the connection is most likely
	 * already in an unusable state. If
	 * {@link #isUseSavepoints() Savepoints are disabled} or
	 * {@link #isAutoCommit() auto commit} is set to {@code false} this function
	 * will do nothing.
	 * 
	 * @see #currentSavepoint
	 * @see #isUseSavepoints()
	 * @see #releaseSavepoint()
	 * @see #setSavepoint()
	 * @see #setUseSavepoints(boolean)
	 */
	protected void rollbackToSavepoint()
	{
		if (currentSavepoint != null)
		{
            try
            {
                if (isConnectionValid())
                {
                    connection.rollback(currentSavepoint);
                }
			}
			catch (SQLException e)
			{
				error(e);
			}
		}
	}
	
	/**
	 * Whether the usage of {@link Savepoint}s is enabled or not.
	 * <p>
	 * {@link Savepoint}s are single steps in complete transaction and can be
	 * required by certain databases and are only available/used when
	 * {@link #isAutoCommit() auto commit} is set to {@code false}.
	 * 
	 * @return true when {@link Savepoint}s are used.
	 * 
	 * @see #currentSavepoint
	 * @see #releaseSavepoint()
	 * @see #rollbackToSavepoint()
	 * @see #setSavepoint()
	 * @see #setUseSavepoints(boolean)
	 */
	protected boolean isUseSavepoints()
	{
		return useSavepoints;
	}
	
	/**
	 * Sets a (new) {@link Savepoint} into {@link #currentSavepoint}.
	 * <p>
	 * Any previous held {@link Savepoint} is
	 * {@link #releaseSavepoint() released}. If
	 * {@link #isUseSavepoints() Savepoints are disabled} or
	 * {@link #isAutoCommit() auto commit} is set to {@code false} this function
	 * will do nothing.
	 * 
	 * @throws SQLException when setting the {@link Savepoint} failed.
	 * 
	 * @see #currentSavepoint
	 * @see #isUseSavepoints()
	 * @see #releaseSavepoint()
	 * @see #rollbackToSavepoint()
	 * @see #setUseSavepoints(boolean)
	 */
	protected void setSavepoint() throws SQLException
	{
		releaseSavepoint();
		
		Connection con = getConnectionIntern();
		
		if (useSavepoints && !con.getAutoCommit())
		{
			currentSavepoint = con.setSavepoint();
		}
	}

	/**
	 * Sets whether the usage of {@link Savepoint}s is enabled or not.
	 * <p>
	 * {@link Savepoint}s are single steps in complete transaction and can be
	 * required by certain databases and are only available/used when
	 * {@link #isAutoCommit() auto commit} is set to {@code false}.
	 * 
	 * @param pUseSavepoints true when {@link Savepoint}s should be used.
	 * 
	 * @see #currentSavepoint
	 * @see #isUseSavepoints()
	 * @see #releaseSavepoint()
	 * @see #rollbackToSavepoint()
	 * @see #setSavepoint()
	 */
	protected void setUseSavepoints(boolean pUseSavepoints)
	{
		useSavepoints = pUseSavepoints;
	}
	
	/**
	 * Adds the SQL Error Code into the message of the SQL Exception.
	 * 
	 * @param pSqlException	the SQL Exception to use.
	 * @return the SQLException with the modified error message with SQL Error Code.
	 */
	protected SQLException formatSQLException(SQLException pSqlException)
	{
		return formatSQLException(pSqlException, pSqlException.getMessage(), "" + pSqlException.getErrorCode());
	}

	/**
	 * Adds the SQL Error Code into the message of the SQL Exception.
	 * 
	 * @param pSqlException the SQL Exception to use.
	 * @param pMessage the message to use
	 * @param pCode the detected error code
	 * @return the SQLException with the modified error message with SQL Error Code.
	 */
	protected SQLException formatSQLException(SQLException pSqlException, String pMessage, String pCode)
	{
		if (pMessage != null && pCode != null && pMessage.indexOf(pCode) < 0)
		{
			String sVendor = getClass().getSimpleName();
			int    index   = sVendor.indexOf("DBAccess");
			
			if (index >= 0)
			{
				sVendor = sVendor.substring(0, index);
			}
			
			SQLException sqleNew = new SQLException(sVendor + "-" + pCode + ": " + pMessage);
			sqleNew.setStackTrace(pSqlException.getStackTrace());
			if (pSqlException.getCause() instanceof SQLException)
			{
			    sqleNew.initCause(formatSQLException((SQLException)pSqlException.getCause()));
			}
			else if (pSqlException.getCause() != null)
            {
                sqleNew.initCause(pSqlException.getCause());
            }
			else if (pSqlException.getNextException() != null)
			{
			    sqleNew.initCause(formatSQLException(pSqlException.getNextException()));
			}
			
			return sqleNew;
		}
		
		return pSqlException;
	}

    /**
     * Creates the {@link ParameterizedStatement} and returns it.
     * <p>
     * Note that this function is subject to be changed in a future version,
     * and can there for not be considered stable.
     * 
     * @param pServerMetaData the MetaDataColumn array to use.
     * @param pBeforeQueryColumns the string to place in the SELECT statement between the SELECT and the first query column.
     * @param pQueryColumns the list of query columns to use in the SELECT statement.
     * @param pFromClause the list of query tables to use in the SELECT statement.
     * @param pFilter the <code>Filter</code> to use
     * @param pWhereClause the string to place in the SELECT statement after the last WHERE condition from the
     *                     Filter or MasterReference (Master-Detail Condition).
     * @param pAfterWhereClause the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
     * @param pSort the sort definition.
     * @param pOrderByClause the order by clause.
     * @param pFromRow              the row index from to fetch
     * @param pMinimumRowCount      the minimum count row to fetch
     * @return the SELECT statement as String.
     * @throws DataSourceException if it's not possible to build the select statement in fact of missing elements    
     */
	@Deprecated
    public ParameterizedStatement getParameterizedSelectStatement(ServerMetaData pServerMetaData,
                                                                  String pBeforeQueryColumns,
                                                                  String[] pQueryColumns,
                                                                  String pFromClause,
                                                                  ICondition pFilter,
                                                                  String pWhereClause,
                                                                  String pAfterWhereClause,
                                                                  SortDefinition pSort,
                                                                  String pOrderByClause,
                                                                  int pFromRow,
                                                                  int pMinimumRowCount) throws DataSourceException
    {
        return getParameterizedSelectStatement(pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                null,
                pFromRow,
                pMinimumRowCount);
    }
	
	/**
	 * Creates the {@link ParameterizedStatement} and returns it.
	 * <p>
	 * Note that this function is subject to be changed in a future version,
	 * and can there for not be considered stable.
	 * 
	 * @param pServerMetaData the MetaDataColumn array to use.
	 * @param pBeforeQueryColumns the string to place in the SELECT statement between the SELECT and the first query column.
	 * @param pQueryColumns the list of query columns to use in the SELECT statement.
	 * @param pFromClause the list of query tables to use in the SELECT statement.
	 * @param pFilter the <code>Filter</code> to use
	 * @param pWhereClause the string to place in the SELECT statement after the last WHERE condition from the
	 * 			           Filter or MasterReference (Master-Detail Condition).
	 * @param pAfterWhereClause the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 * @param pSort the sort definition.
     * @param pOrderByClause the order by clause.
     * @param pAfterOrderByClause   the after order by clause
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
     * @return the SELECT statement as String.
     * @throws DataSourceException if it's not possible to build the select statement in fact of missing elements    
	 */
	public ParameterizedStatement getParameterizedSelectStatement(ServerMetaData pServerMetaData,
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
		if (pFromClause == null)
		{
			throw new DataSourceException("Missing FROM clause!");
		}
		
		List<String> liNames = new ArrayList<String>();
		int iFilterIndex = 0;
		
		findNamedParameters(pBeforeQueryColumns, liNames, true);
		findNamedParameters(pQueryColumns, liNames, true);
		findNamedParameters(pFromClause, liNames, true);
		
		iFilterIndex = liNames.size();
		
		findNamedParameters(pWhereClause, liNames, true);
		findNamedParameters(pAfterWhereClause, liNames, true);
		findNamedParameters(pOrderByClause, liNames, true);
        findNamedParameters(pAfterOrderByClause, liNames, true);
		
		Set<String> stUniqueNames = new HashSet<String>(liNames);
		Map<String, CompareCondition> mpNameToCondition = new HashMap<String, CompareCondition>();
				
		ICondition filter = Filter.findAndCreateReducedCondition(pFilter, stUniqueNames, mpNameToCondition, true);
		
		// add select column names to select
		StringBuilder sSelectStatement = new StringBuilder("SELECT ");

		if (!StringUtil.isEmpty(pBeforeQueryColumns))
		{
			sSelectStatement.append(pBeforeQueryColumns);
			sSelectStatement.append("\n       ");
		}

		if (pQueryColumns != null && pQueryColumns.length > 0 && pQueryColumns[0].length() > 0)
		{
			for (int i = 0; i < pQueryColumns.length; i++)
			{
				if (i > 0)
				{
					sSelectStatement.append(",\n       ");
				}
				sSelectStatement.append(pQueryColumns[i]);
			}
		}
		else
		{
			sSelectStatement.append("*");
		}

		sSelectStatement.append("\n  FROM ");

		sSelectStatement.append(pFromClause);
        sSelectStatement.append("\n");

		// add Filter
        String whereClause = getWhereClause(pServerMetaData, filter, pWhereClause, true);
        if (!StringUtil.isEmpty(whereClause))
        {
            sSelectStatement.append(whereClause);
            sSelectStatement.append("\n");
        }

		if (!StringUtil.isEmpty(pAfterWhereClause))
		{
			sSelectStatement.append(pAfterWhereClause);
            sSelectStatement.append("\n");
		}

		// add Sort
		if (pSort != null)
		{
			// use DB sorting algorithm
			sSelectStatement.append("ORDER BY ");
			
			String[]  saSortDefinitionNames = pSort.getColumns();
			boolean[] baSortDefinitionOrder = pSort.isAscending();
			
			for (int i = 0; i < saSortDefinitionNames.length; i++)
			{
                if (i > 0)
                {
                    sSelectStatement.append(", ");
                }

			    int index = pServerMetaData.getServerColumnMetaDataIndex(saSortDefinitionNames[i]);
				if (index < 0)
				{
					sSelectStatement.append(saSortDefinitionNames[i]);
				}
				else
				{
				    // The order by has to be the real query column name, as in case of a join there are 2 id columns...
					sSelectStatement.append(pServerMetaData.getServerColumnMetaData(index).getRealQueryColumnName());
				}
				if (baSortDefinitionOrder == null || i >= baSortDefinitionOrder.length || baSortDefinitionOrder[i])
				{
					sSelectStatement.append(" ASC");
				}
				else
				{
					sSelectStatement.append(" DESC");
				}
			}
			sSelectStatement.append("\n");
		}
		else if (!StringUtil.isEmpty(pOrderByClause))
		{
			sSelectStatement.append("ORDER BY ");
			sSelectStatement.append(pOrderByClause);
            sSelectStatement.append("\n");
		}
		
        if (!StringUtil.isEmpty(pAfterOrderByClause))
        {
            sSelectStatement.append(pAfterOrderByClause);
            sSelectStatement.append("\n");
        }

		
		String statement = sSelectStatement.toString();
		
		List<Object> values = new ArrayList<Object>();
		
		for (int index = 0, size = liNames.size(); index < size; index++)
		{
			String name = liNames.get(index);
			CompareCondition condition = mpNameToCondition.get(name);
			
			if (condition != null)
			{
				Object oValue = condition.getValue();
				if ((condition instanceof Like || condition instanceof LikeIgnoreCase)
						&& oValue instanceof String)
				{
					values.add(((String)oValue).replace('*', '%').replace('?', '_'));
				}						
				else 
				{
					values.add(oValue);
				}
			}
			else
			{
				values.add(null);
			}
		}
		
		statement = replaceNamedParameters(statement, mpNameToCondition, pServerMetaData, true);
		
        if (filter != null)
        {
    		List<Object> parameterValues = new ArrayList<Object>();
    		List<String> columnNames = new ArrayList<String>();
		
    		getParameter(filter, parameterValues, columnNames, mpNameToCondition);
    		
    		values.addAll(iFilterIndex, parameterValues);
    		liNames.addAll(iFilterIndex, columnNames);
		}

		return new ParameterizedStatement(statement, liNames, values, mpNameToCondition);
	}
	
	/**
	 * It initialize the select for a specified storage unit and return the SELECT statement.<br>
	 * It doesn't add the ORDER BY clause.<br>
	 * 
	 * @param pServerMetaData the MetaDataColumn array to use.
	 * @param pBeforeQueryColumns the string to place in the SELECT statement between the SELECT and the first query column.
	 * @param pQueryColumns the list of query columns to use in the SELECT statement.
	 * @param pFromClause the list of query tables to use in the SELECT statement.
	 * @param pFilter the <code>Filter</code> to use
	 * @param pWhereClause the string to place in the SELECT statement after the last WHERE condition from the
	 * 			           Filter or MasterReference (Master-Detail Condition).
	 * @param pAfterWhereClause the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 * @param pSort the sort definition.
     * @param pOrderByClause the order by clause.
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
     * @return the SELECT statement as String.
     * @throws DataSourceException if it's not possible to build the select statement in fact of missing elements    
	 */	
	@Deprecated
	public String getSelectStatement(ServerMetaData pServerMetaData,
                					 String pBeforeQueryColumns,
                					 String[] pQueryColumns,
                					 String pFromClause,
                					 ICondition pFilter,
                					 String pWhereClause,
                					 String pAfterWhereClause,
                					 SortDefinition pSort,
                					 String pOrderByClause,
                					 int pFromRow,
                					 int pMinimumRowCount) throws DataSourceException
	{
        return getParameterizedSelectStatement(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                pFromRow,
                pMinimumRowCount).getStatement();
	}
	
	/**
	 * It initialize the select for a specified storage unit and return the SELECT statement.<br>
	 * It doesn't add the ORDER BY clause.<br>
	 * 
	 * @param pServerMetaData the MetaDataColumn array to use.
	 * @param pBeforeQueryColumns the string to place in the SELECT statement between the SELECT and the first query column.
	 * @param pQueryColumns the list of query columns to use in the SELECT statement.
	 * @param pFromClause the list of query tables to use in the SELECT statement.
	 * @param pFilter the <code>Filter</code> to use
	 * @param pWhereClause the string to place in the SELECT statement after the last WHERE condition from the
	 * 			           Filter or MasterReference (Master-Detail Condition).
	 * @param pAfterWhereClause the string to place in the SELECT statement after the WHERE clause and before the ORDER BY clause.
	 * @param pSort the sort definition.
     * @param pOrderByClause the order by clause.
     * @return the SELECT statement as String.
     * @throws DataSourceException if it's not possible to build the select statement in fact of missing elements    
	 */	
    @Deprecated
	public String getSelectStatement(ServerMetaData pServerMetaData,
                					 String pBeforeQueryColumns,
                					 String[] pQueryColumns,
                					 String pFromClause,
                					 ICondition pFilter,
                					 String pWhereClause,
                					 String pAfterWhereClause,
                					 SortDefinition pSort,
                					 String pOrderByClause) throws DataSourceException
	{
        return getSelectStatement(
                pServerMetaData,
                pBeforeQueryColumns,
                pQueryColumns,
                pFromClause,
                pFilter,
                pWhereClause,
                pAfterWhereClause,
                pSort,
                pOrderByClause,
                0,
                -1);
	}

	/**
	 * Returns the WHERE clause for a UPDATE, DELETE or SELECT statement specified with
	 * a <code>Filter</code>.
	 * 
	 * @param pServerMetaData	the <code>MetaData</code> to use
	 * @param pFilter       	the <code>Filter</code> to use
	 * @param pWhereClause      the where clause to use
	 * @param pUsePrefix		<code>true</code> to use the prefixed column (real column name) and <code>false</code> to
	 *                      	ignore prefixes (the simple name of the column) 
	 * @throws DataSourceException if columns not existing
	 * @return the WHERE clause for a UPDATE, DELETE or SELECT statement specified with
	 *         a <code>Filter</code>.
	 */
	protected String getWhereClause(ServerMetaData pServerMetaData, ICondition pFilter, String pWhereClause, boolean pUsePrefix) throws DataSourceException
	{
		StringBuilder result = new StringBuilder();
		
		String sCondition = getSQL(pServerMetaData, pFilter, pUsePrefix);
		
		boolean hasWhereClause = !StringUtil.isEmpty(pWhereClause);
		boolean hasCondition = !StringUtil.isEmpty(sCondition);
		
		if (hasCondition)
		{
			boolean needsBracket = hasWhereClause && pFilter instanceof Or;
			
			result.append(" WHERE ");
			if (needsBracket)
			{
				result.append("(");
			}
			result.append(sCondition);
			if (needsBracket)
			{
				result.append(")");
			}
		}
		
		if (hasWhereClause)
		{
			if (hasCondition)
			{
                result.append(" AND ");
                result.append("(");
                result.append(pWhereClause);
                result.append(") ");
			}
			else
			{
                result.append(" WHERE ");
                result.append(pWhereClause);
                result.append(" ");
			}
		}

		return result.toString();
	}
	
	/**
	 * Returns the database specific statement to lock the specified row in the database.
	 *  
	 * @param pWriteBackTable	the table to use.
	 * @param pPKFilter			the PK filter with the values to use.
	 * @param pServerMetaData	the MetaDataColumn array to use.
	 * @return the database specific statement to lock the specified row in the database.
	 * @throws DataSourceException if some parts are missing for the statement
	 */
	protected String getDatabaseSpecificLockStatement(String pWriteBackTable, ServerMetaData pServerMetaData, ICondition pPKFilter) throws DataSourceException
	{
		if (pWriteBackTable == null)
		{
			throw new DataSourceException("Missing WriteBackTable!");
		}
		
		StringBuilder sbfSelectForUpdate = new StringBuilder("SELECT 0 FROM ");
		sbfSelectForUpdate.append(pWriteBackTable);
		sbfSelectForUpdate.append(getWhereClause(pServerMetaData, pPKFilter, null, false));
		sbfSelectForUpdate.append(" FOR UPDATE");
		return sbfSelectForUpdate.toString();
	}
	
	/**
	 * Returns the newly inserted row from a Database specific insert statement. <br>
	 * Database specific derivations of DBAccess need to implement it database specific. 
	 * 
	 * @param pWriteBackTable	the table to use for the insert
	 * @param pInsertStatement	the SQL Statement to use for the insert
	 * @param pServerMetaData	the meta data to use.
	 * @param pNewDataRow		the new row (Object[]) with the values to insert
	 * @param pDummyColumn		<code>null</code>, if all writeable columns are null, but for a correct INSERT it have
	 *                          to be minimum one column to use in the syntax.
	 * @return the newly inserted row from an Oracle Database.
	 * @throws DataSourceException if an <code>Exception</code> occur during insert to the storage
	 */		
	protected Object[] insertDatabaseSpecific(String pWriteBackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
                                              Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
    {
		return insertAnsiSQL(pWriteBackTable, pInsertStatement, pServerMetaData, pNewDataRow, pDummyColumn);
    }
	
	/**
	 * Returns if this Database specific supports generated keys. Because of Derby bug!
	 * @return if this Database specific supports generated keys.
	 */
	public boolean supportsGetGeneratedKeys()
	{
		try
		{
			return getConnectionIntern().getMetaData().supportsGetGeneratedKeys();
		}
		catch (SQLException sqlException)
		{
			return false;
		}
	}

	/**
	 * Returns the newly inserted row from a Ansi SQL Database. <br>
	 * It uses getGeneratedKeys to get the primary key values back from the database. Its recommend that
	 * the jdbc driver of the database support that clean.
	 * 
	 * @param pWriteBackTable	the table to use for the insert
	 * @param pInsertStatement	the SQL Statement to use for the insert
	 * @param pServerMetaData	the meta data to use.
	 * @param pNewDataRow		the new row (Object[]) with the values to insert
	 * @param pDummyColumn		true, if all writeable columns are null, but for a correct INSERT it have
	 *                          to be minimum one column to use in the syntax.
	 * @return the newly inserted row from a Ansi SQL Database.
	 * @throws DataSourceException
	 *             if an <code>Exception</code> occur during insert to the storage
	 */
	protected Object[] insertAnsiSQL(String pWriteBackTable, String pInsertStatement, ServerMetaData pServerMetaData, 
									Object[] pNewDataRow, String pDummyColumn)  throws DataSourceException
	{
		// insert DataRow to database
		boolean bSupportGeneratedKeys = supportsGetGeneratedKeys();

		PreparedStatement psInsert = null;
	    ResultSet rsPK = null;

		try
		{		
			psInsert = getPreparedStatement(pInsertStatement, bSupportGeneratedKeys);

			if (iTransactionTimeout >= 0)
			{
				try
				{
					psInsert.setQueryTimeout(iTransactionTimeout);
				}
				catch (Throwable ex)
				{
					// Ignore not implemented Exceptions.
				}
			}

			ServerColumnMetaData[] cmdServerColumnMetaData = pServerMetaData.getServerColumnMetaData();
			int[] iaWriteables = pServerMetaData.getWritableColumnIndices();
			
			if (pDummyColumn == null)
			{
				setColumnsToStore(psInsert, cmdServerColumnMetaData, iaWriteables, pNewDataRow, null);
			}
			else
			{
				// set null value for it!
				for (int i = 0; i < cmdServerColumnMetaData.length; i++)
				{
					if (cmdServerColumnMetaData[i].getColumnName().getQuotedName().equals(pDummyColumn))
					{
						psInsert.setObject(1, null, cmdServerColumnMetaData[i].getSQLType());
						break;
					}
				}
			}
			
			setSavepoint();
			
			if (psInsert.executeUpdate() == 1)
			{
				if (bSupportGeneratedKeys)
				{
					// Return GeneratedKeys (typical used in PK)
					try 
					{
					    rsPK = psInsert.getGeneratedKeys();
					    if (rsPK.next()) 
					    {
					    	updateColumnsWithGeneratedKeys(pNewDataRow, rsPK, pServerMetaData);
					    }
					}
					catch (SQLException sqlException)
					{
						throw new DataSourceException("The generated keys couldn't read! - " + pInsertStatement, formatSQLException(sqlException));
					}
				}
				return pNewDataRow;
			}
			throw new DataSourceException("Insert failed! - Result row count != 1" + pInsertStatement);
		}
		catch (SQLException sqlException)
		{
			rollbackToSavepoint();
			
			throw new DataSourceException("Insert failed! - " + pInsertStatement, formatSQLException(sqlException));
		}
		finally
		{
			releaseSavepoint();
		    CommonUtil.close(rsPK, psInsert);
		}
	}

	/**
	 * Updates the given data row with results from generated keys.
	 * 
	 * @param pNewDataRow the new data row
	 * @param pInsert the resultset of generated keys statement
	 * @param pMetaData the metadata for data row
	 * @throws SQLException if accessing resultset fails
	 */
	protected void updateColumnsWithGeneratedKeys(Object[] pNewDataRow, ResultSet pInsert, ServerMetaData pMetaData) throws SQLException
	{
		ResultSetMetaData mdat = pInsert.getMetaData();

	    String[] sPKColumns = pMetaData.getPrimaryKeyColumnNames();
	    
    	for (int i = 0, anz = mdat.getColumnCount(); i < anz && sPKColumns != null && i < sPKColumns.length; i++)
    	{
    		int columnIndex = pMetaData.getServerColumnMetaDataIndex(sPKColumns[i]);
    		if (columnIndex >= 0)
    		{
    			// #440 - DBAccess.insert return null if no auto increment column in table 
    			Object value = pInsert.getObject(i + 1);
					
				if (value != null)
				{
					//some values are numbers but the column type is text -> means that the returned value is an ID but the PK isn't a number
					if (getDataTypeIdentifier(mdat.getColumnType(i + 1)) == pMetaData.getServerColumnMetaData(columnIndex).getTypeIdentifier())
					{
						pNewDataRow[columnIndex] = value;
					}
				}
    		}
    	}
	}
	
	/**
	 * Returns the meta data information for the specified query, and configures all columns with defaults.
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pWhereClause			the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
	 * @throws DataSourceException 
	 *            if an <code>Exception</code> occur during getting the meta data or 
	 *            if the storage is not opened or 
	 *            if one columns SQL type is not supported
	 */
    protected void getAndStoreMetaDataIntern(String pFromClause, 
								     	             String[] pQueryColumns,
								     	             String pBeforeQueryColumns, 
								     	             String pWhereClause, 
								     	             String pAfterWhereClause) throws DataSourceException
	{
    	String tableIdentifier = createIgnoreCaseIdentifier(pFromClause, pQueryColumns, pBeforeQueryColumns);
    	
    	if (cachedMetaDataIdentifier == null || !cachedMetaDataIdentifier.equals(tableIdentifier))
		{
    		cachedMetaDataIdentifier = null;
    		cachedColumnMetaData = null;
    		cachedCatalogInfo = null;
    		cachedSchemaInfo = null;
    		cachedTableInfo = null;
    		
			long lMillis = System.currentTimeMillis();
	        if (pFromClause.contains("(select m.ID,"))
	        {
	            System.out.println("Halt");
	        }
			
			if (pWhereClause == null)
			{
				pWhereClause = getMetaDataWhereClause();
			}		
			else
			{
				pWhereClause += " AND " + getMetaDataWhereClause();
			}
	
			PreparedStatement psQueryMetaData = null;
			ResultSet rsQueryMetaData = null;
			String fromClauseWithAlias = pFromClause.trim();
			if (fromClauseWithAlias.startsWith("(") && fromClauseWithAlias.endsWith(")")) // some databases need an alias in sub selects
			{
			    fromClauseWithAlias += " m";
			}
			
			ParameterizedStatement selectStatement = getParameterizedSelectStatement(null,
	                                                                				 pBeforeQueryColumns,
	                                                                				 pQueryColumns,
	                                                                				 fromClauseWithAlias,
	                                                                				 null,
	                                                                				 pWhereClause,
	                                                                				 pAfterWhereClause,
	                                                                				 null,
	                                                                				 null,
	                                                                				 null,
	                                                                				 0,
	                                                                				 0);
			try
			{
				// get Columns
				psQueryMetaData = getPreparedStatement(selectStatement.getStatement());
				if (!selectStatement.getValues().isEmpty())
				{
					setFilterParameter(1, psQueryMetaData, selectStatement.getValuesAsArray());
				}
				
				setSavepoint();
				
				rsQueryMetaData = psQueryMetaData.executeQuery();
				ResultSetMetaData rsMetaData = rsQueryMetaData.getMetaData();
				
				ArrayUtil<ServerColumnMetaData> auCmd = new ArrayUtil<ServerColumnMetaData>();
				ArrayUtil<String> auColumnNames = new ArrayUtil<String>();
				
				String prefix = null;
				int index = 0;
				// set every ColumnMetaData
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
				{
					String sQueryColumn = null;
					
					if (pQueryColumns != null && index < pQueryColumns.length)
					{
						String columnName = getColumnName(rsMetaData, i);
						String queryColumn = pQueryColumns[index];
						if (queryColumn != null)
						{
    						if (!StringUtil.isEmpty(sOpenQuote))
    						{
    						    queryColumn = queryColumn.replace(sOpenQuote, QUOTE);
    						}
                            if (!StringUtil.isEmpty(sCloseQuote) && !sOpenQuote.equals(sCloseQuote))
                            {
                                queryColumn = queryColumn.replace(sCloseQuote, QUOTE);
                            }
    						String qc = removeQuotes(queryColumn.toLowerCase());
    						if (qc.endsWith(columnName.toLowerCase()) 
    								&& (qc.length() == columnName.length() || !Character.isJavaIdentifierPart(qc.charAt(qc.length() - columnName.length() - 1))))
    						{
    							sQueryColumn = queryColumn;
    
    							prefix = null;
    							index++;
    						}
    						else if (queryColumn.endsWith(".*"))
    						{
    							prefix = queryColumn.substring(0,  queryColumn.length() - 1);
    							index++;
    						}
    						if (sQueryColumn == null && prefix != null)
    						{
    							sQueryColumn = prefix + quote(columnName);
    						}
						}
					}
					
					ServerColumnMetaData cd = null;
					try
					{
					    cd = createServerColumnMetaData(rsMetaData, i, sQueryColumn);
					}
					catch (Exception ex) // We will ignore unsupported columns and just log the problem.
					{                    // Fetching data should work for the rest.
					    error(ex);
					}
					
					if (cd != null)
					{
    					if (auColumnNames.indexOf(cd.getName()) >= 0)
    					{
    						throw new DataSourceException("Duplicate definition of '" + cd.getName() + 
    								"' in DBStorage " + selectStatement.getStatement() + "!");
    					}
    					
    					auCmd.add(cd);
    					auColumnNames.add(cd.getName());
					}
				}
				
	            if (isLogEnabled(LogLevel.DEBUG))
	            {
	                debug("ColumnMetaData(", selectStatement.getStatement(), ") in ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
	            }

				cachedColumnMetaData = auCmd.toArray(new ServerColumnMetaData[auCmd.size()]);
				cachedCatalogInfo = rsMetaData.getCatalogName(1);
				cachedSchemaInfo  = rsMetaData.getSchemaName(1);
				cachedTableInfo   = rsMetaData.getTableName(1);
				
	    		cachedMetaDataIdentifier = tableIdentifier;
			}
			catch (SQLException sqlException)
			{    		
				rollbackToSavepoint();
				
				throw new DataSourceException("Meta data couldn't load from database! - " + 
						selectStatement.getStatement(), formatSQLException(sqlException));
			}
			finally
			{
				releaseSavepoint();
			    CommonUtil.close(rsQueryMetaData, psQueryMetaData);
			}
		}
	}
	
	/**
	 * Returns the meta data information for the specified query, and configures all columns with defaults.
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pWhereClause			the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
 	 * @return the meta data for the specified query, and initials all columns.
	 * @throws DataSourceException 
	 *            if an <code>Exception</code> occur during getting the meta data or 
	 *            if the storage is not opened or 
	 *            if one columns SQL type is not supported
	 */
    protected ServerColumnMetaData[] getColumnMetaDataIntern(String pFromClause, 
										     	             String[] pQueryColumns,
										     	             String pBeforeQueryColumns, 
										     	             String pWhereClause, 
										     	             String pAfterWhereClause) throws DataSourceException
	{
    	getAndStoreMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
    	
		return cachedColumnMetaData;
   	}

	/**
	 * Gets the real query column name without alias.
	 * @param pQueryColumn the full query column name with alias
	 * @return the real query column name without alias.
	 */
	protected String getRealQueryColumnName(String pQueryColumn)
	{
		String realQueryColumn = pQueryColumn.trim();
		
		int aliasIndex = -1;
		if (realQueryColumn.endsWith(QUOTE) && realQueryColumn.length() > 2)
		{
			aliasIndex = realQueryColumn.lastIndexOf(QUOTE, realQueryColumn.length() - 2) - 1;
			
			if (aliasIndex >= 0 && !Character.isWhitespace(realQueryColumn.charAt(aliasIndex)))
			{
			    aliasIndex = -1;
			}
		}
		else if (realQueryColumn.length() > 0 && Character.isJavaIdentifierPart(realQueryColumn.charAt(realQueryColumn.length() - 1)))
		{
			aliasIndex = realQueryColumn.lastIndexOf(' ');
		}
		
		if (aliasIndex >= 0)
		{
			realQueryColumn = realQueryColumn.substring(0, aliasIndex).trim();
		}
		
		if (realQueryColumn.length() > 3 && realQueryColumn.regionMatches(true, realQueryColumn.length() - 2, "as", 0, 2)
				&& Character.isWhitespace(realQueryColumn.charAt(realQueryColumn.length() - 3)))
		{
			realQueryColumn = realQueryColumn.substring(0, realQueryColumn.length() - 3).trim();
		}
		
		return realQueryColumn; 
	}
	
	/**
	 * Creates the <code>ServerColumnMetaData</code> based on <code>ResultSetMetaData</code>.
	 * 
	 * @param pResultSetMetaData the result set meta data.
	 * @param pResultSetColumnIndex the column index.
	 * @param pQueryColumn the specific query column of the select statement or null if none
	 * @return the created <code>ServerColumnMetaData</code>
	 * @throws SQLException if an unwanted SQLException occurs
	 * @throws DataSourceException if an know problem occurs during creation
	 */
	protected ServerColumnMetaData createServerColumnMetaData(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, String pQueryColumn)
			 throws SQLException, DataSourceException
	{
		String sColName = getColumnName(pResultSetMetaData, pResultSetColumnIndex);
		
		Name nColumnName = new Name(sColName, quote(sColName));
	
		ColumnMetaData columnMetaData;
		if (columnMetaDataCreator == null)
		{
			columnMetaData = new ColumnMetaData();
			
			initializeColumnMetaData(pResultSetMetaData, pResultSetColumnIndex, columnMetaData);
		}
		else
		{
			columnMetaData = columnMetaDataCreator.createColumnMetaData(this, pResultSetMetaData, pResultSetColumnIndex);
		}

		return initializeServerColumnMetaData(pResultSetMetaData, pResultSetColumnIndex, pQueryColumn, new ServerColumnMetaData(nColumnName, columnMetaData));
	}
		
	/**
	 * Fills data into the <code>ServerColumnMetaData</code> based on <code>ResultSetMetaData</code>.
	 * 
	 * @param pResultSetMetaData the result set meta data.
	 * @param pResultSetColumnIndex the column index.
	 * @param pQueryColumn the specific query column of the select statement or null if none
	 * @param pServerColumnMetaData the ServerColumnMetaData
	 * 
	 * @return the created <code>ServerColumnMetaData</code>
	 * 
	 * @throws SQLException if an unwanted SQLException occurs
	 * @throws DataSourceException if an know problem occurs during creation
	 */
	protected ServerColumnMetaData initializeServerColumnMetaData(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, String pQueryColumn, 
			ServerColumnMetaData pServerColumnMetaData) throws SQLException, DataSourceException
	{
		//set real DB column name, which is specified from the developer to use for query, filter and sort (only Query side)
		if (pQueryColumn == null)
		{
			pServerColumnMetaData.setRealQueryColumnName(pServerColumnMetaData.getColumnName().getQuotedName());
		}
		else
		{
			pServerColumnMetaData.setRealQueryColumnName(getRealQueryColumnName(pQueryColumn));
		}
		
		pServerColumnMetaData.setSQLType(pResultSetMetaData.getColumnType(pResultSetColumnIndex));
		pServerColumnMetaData.setSQLTypeName(pResultSetMetaData.getColumnTypeName(pResultSetColumnIndex));

		return pServerColumnMetaData;
	}

	/**
	 * Initilizes the <code>ColumnMetaData</code> with the given <code>ResultSetMetaData</code>.
	 * 
	 * @param pResultSetMetaData the result set meta data.
	 * @param pResultSetColumnIndex the column index.
	 * @param pColumnMetaData the ColumnMetaData
	 * 
	 * @throws SQLException if an unwanted SQLException occurs
	 * @throws DataSourceException if an know problem occurs during creation
	 */
	public void initializeColumnMetaData(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, ColumnMetaData pColumnMetaData) throws SQLException, DataSourceException
	{
		pColumnMetaData.setNullable(pResultSetMetaData.isNullable(pResultSetColumnIndex) != ResultSetMetaData.columnNoNulls);

		// #544 - In databases with default lower case column names, the default labels in the RowDefinition are wrong 
		// setLabel removed!
		pColumnMetaData.setAutoIncrement(pResultSetMetaData.isAutoIncrement(pResultSetColumnIndex));

		initializeDataType(pResultSetMetaData, pResultSetColumnIndex, pColumnMetaData);
	}

	/**
	 * Gets the type identifier for a given JDBC type.
	 * 
	 * @param pJDBCType the JDBC type
	 * @return the data type identifier or {@link Integer#MIN_VALUE} if detection failed
	 * @see IDataType#getTypeIdentifier()
	 */
	protected int getDataTypeIdentifier(int pJDBCType)
	{
		switch(pJDBCType)
		{
			case NCHAR:
			case NVARCHAR:
			case LONGNVARCHAR:
            case ROWID:
			case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            	return StringDataType.TYPE_IDENTIFIER;
            case Types.BIT:
			case Types.BOOLEAN:
				return BooleanDataType.TYPE_IDENTIFIER;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            	return BigDecimalDataType.TYPE_IDENTIFIER;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case TIME_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_TIMEZONE:
            case TIMESTAMP_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_LOCAL_TIMEZONE:
            	return TimestampDataType.TYPE_IDENTIFIER;
            case BFILE:
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            	return BinaryDataType.TYPE_IDENTIFIER;
            default:
            	return Integer.MIN_VALUE;
		}
	}
	
	/**
	 * Initializes the datatype data of the <code>ColumnMetaData</code> with the given <code>ResultSetMetaData</code>.
	 * 
	 * @param pResultSetMetaData the result set meta data.
	 * @param pResultSetColumnIndex the column index.
	 * @param pColumnMetaData the ColumnMetaData
	 * 
	 * @throws SQLException if an unwanted SQLException occurs
	 * @throws DataSourceException if an know problem occurs during creation
	 */
	protected void initializeDataType(ResultSetMetaData pResultSetMetaData, int pResultSetColumnIndex, ColumnMetaData pColumnMetaData) throws SQLException, DataSourceException
	{
	    int columnType = pResultSetMetaData.getColumnType(pResultSetColumnIndex);
	    
		switch (columnType)
		{
			case NCHAR:
			case NVARCHAR:
			case LONGNVARCHAR:
            case ROWID:
			case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.SQLXML:
            	pColumnMetaData.setTypeIdentifier(StringDataType.TYPE_IDENTIFIER);
            	if (columnType == Types.ROWID)
                {
                    pColumnMetaData.setPrecision(18);
                }
                else if (columnType == Types.CLOB || columnType == Types.NCLOB || columnType == Types.SQLXML
                        || pResultSetMetaData.getPrecision(pResultSetColumnIndex) <= 0)
                {
                    pColumnMetaData.setPrecision(Integer.MAX_VALUE);
                } 
                else
                {
                    pColumnMetaData.setPrecision(pResultSetMetaData.getPrecision(pResultSetColumnIndex));
                }
            	
            	break;
            case Types.BIT:
			case Types.BOOLEAN:
				pColumnMetaData.setTypeIdentifier(BooleanDataType.TYPE_IDENTIFIER);
            	break;
            	
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            	pColumnMetaData.setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
            	pColumnMetaData.setPrecision(0);
            	pColumnMetaData.setScale(-1);
            	pColumnMetaData.setSigned(pResultSetMetaData.isSigned(pResultSetColumnIndex)); 
            	break;
            	
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            	pColumnMetaData.setTypeIdentifier(BigDecimalDataType.TYPE_IDENTIFIER);
            	if (pResultSetMetaData.getPrecision(pResultSetColumnIndex) <= 0 // none, use decimal without any precision and scale -> unlimited!
            			|| pResultSetMetaData.getPrecision(pResultSetColumnIndex) > 30) // over 30 , we use decimal without any precision and scale -> unlimited!
            	{
            		pColumnMetaData.setPrecision(0);
            		pColumnMetaData.setScale(-1);
            	}
            	else
            	{
            		pColumnMetaData.setPrecision(pResultSetMetaData.getPrecision(pResultSetColumnIndex));
            		pColumnMetaData.setScale(pResultSetMetaData.getScale(pResultSetColumnIndex));
            	}
            	pColumnMetaData.setSigned(pResultSetMetaData.isSigned(pResultSetColumnIndex)); 
            	break;
            	
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case TIME_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_TIMEZONE:
            case TIMESTAMP_WITH_TIMEZONE_V8:
            case TIMESTAMP_WITH_LOCAL_TIMEZONE:
                pColumnMetaData.setScale(pResultSetMetaData.getScale(pResultSetColumnIndex));
            	pColumnMetaData.setTypeIdentifier(TimestampDataType.TYPE_IDENTIFIER);
            	break;
            	
            case BFILE:
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            	pColumnMetaData.setTypeIdentifier(BinaryDataType.TYPE_IDENTIFIER);

            	if (columnType == Types.BLOB 
            	        || pResultSetMetaData.getPrecision(pResultSetColumnIndex) <= 0)
            	{
                    pColumnMetaData.setPrecision(Integer.MAX_VALUE);
            	}
            	else
            	{
                    pColumnMetaData.setPrecision(pResultSetMetaData.getPrecision(pResultSetColumnIndex));
            	}
            	
            	break;
            	
            default:
    			throw new DataSourceException(pColumnMetaData.getName() + " :: SQL Type '" + pResultSetMetaData.getColumnType(pResultSetColumnIndex) + "' is not supported!");
		}
	}

	/**
	 * Clones the server meta data.
	 * @param pMetaData the meta data to clone
	 * @return the cloned meta data
	 */
	protected ServerColumnMetaData[] cloneServerColumnMetaData(ServerColumnMetaData[] pMetaData)
	{
	    if (pMetaData == COLUMNMETADATA_NULL || pMetaData == null)
        {
            return null;
        }
        else
        {
    	    ServerColumnMetaData[] result = new ServerColumnMetaData[pMetaData.length];
            
            for (int i = 0; i < pMetaData.length; i++)
            {
                result[i] = pMetaData[i].clone();
            }
            
            return result;
        }
	}
	
	/**
	 * Returns the meta data information for the specified query, and configures all columns with defaults.
	 * 
	 * @param pBeforeQueryColumns	the before query columns
	 * @param pQueryColumns			the query columns	
	 * @param pFromClause			the from clause with query tables and join definitions
	 * @param pWhereClause			the last where condition in query
	 * @param pAfterWhereClause		the after where clause in query
 	 * @return the meta data for the specified query, and initials all columns.
	 * @throws DataSourceException 
	 *            if an <code>Exception</code> occur during getting the meta data or 
	 *            if the storage is not opened or 
	 *            if one columns SQL type is not supported
	 */
	public final ServerColumnMetaData[] getColumnMetaData(String pFromClause,
        										     	  String[] pQueryColumns,
        										     	  String pBeforeQueryColumns, 
        										     	  String pWhereClause, 
        										     	  String pAfterWhereClause) throws DataSourceException
	{
	    ServerColumnMetaData[] metaData;
	    
		if (isMetaDataCacheEnabled())
		{
	        String dbAccessIdentifier = getIdentifier();
	        String tableIdentifier = createIgnoreCaseIdentifier(pFromClause, pQueryColumns, pBeforeQueryColumns);

	        metaData = ghtColumnMetaDataCache.get(dbAccessIdentifier, tableIdentifier);
	        if (metaData == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_COLUMNMETADATA, pFromClause);

                try
                {
    				metaData = getColumnMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				ghtColumnMetaDataCache.put(dbAccessIdentifier, tableIdentifier, metaData == null ? COLUMNMETADATA_NULL : metaData);
				
                // Store default MetaData for Query, they are the same.
				if (pQueryColumns == null && pBeforeQueryColumns == null && metaData != null)
				{
				    createAndStorePossibleQueryMetaData(pFromClause, metaData);
				}
			}
			
	        return cloneServerColumnMetaData(metaData);
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_COLUMNMETADATA, pFromClause);
            
            try
            {
                metaData = getColumnMetaDataIntern(pFromClause, pQueryColumns, pBeforeQueryColumns, pWhereClause, pAfterWhereClause);
            }
            finally
            {
                CommonUtil.close(record);
            }
            
			//it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIgnoreCaseIdentifier(pFromClause, pQueryColumns, pBeforeQueryColumns);

                if (ghtColumnMetaDataCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtColumnMetaDataCache.put(dbAccessIdentifier, tableIdentifier, metaData == null ? COLUMNMETADATA_NULL : metaData);
                }
            }

            return cloneServerColumnMetaData(metaData); // for the rare case, the same query is taken twice, and changed between...
		}
	}

	/**
	 * Creates and stores the possible meta data for a query based on the write back table.
	 * @param pWriteBackTable the write back table
	 * @param pMetaData the meta data
	 */
    protected void createAndStorePossibleQueryMetaData(String pWriteBackTable, ServerColumnMetaData[] pMetaData)
    {
        ArrayUtil<String> queryColumns = new ArrayUtil<String>();
        for (ServerColumnMetaData scmd : pMetaData)
        {
            queryColumns.add("m." + scmd.getColumnName().getQuotedName());
        }
        String additionalTableIdentifier = createIgnoreCaseIdentifier(pWriteBackTable + " m", queryColumns.toArray(), null);
        
        ServerColumnMetaData[] result = new ServerColumnMetaData[pMetaData.length];
        
        for (int i = 0; i < pMetaData.length; i++)
        {
            result[i] = pMetaData[i].clone();
            result[i].setRealQueryColumnName("m." + result[i].getRealQueryColumnName());
        }
        
        ghtColumnMetaDataCache.put(getIdentifier(), additionalTableIdentifier, result);
    }
	
	/**
	 * Returns the meta data information for the specified query, and configures all columns with defaults.
	 * 
	 * @param pWriteBackTable		the write back table to use for the isWriteable() state (Optional)
 	 * @return the meta data for the specified query, and initials all columns.
	 * @throws DataSourceException 
	 *            if an <code>Exception</code> occur during getting the meta data or 
	 *            if the storage is not opened or 
	 *            if one columns SQL type is not supported
	 */
	protected TableInfo getTableInfoIntern(String pWriteBackTable) throws DataSourceException
	{
    	getAndStoreMetaDataIntern(pWriteBackTable, null, null, null, null);

			// #136 - Mysql PK refetch not working -> extract the given table without schema. 
//			String sTable = rsMetaData.getTableName(1); // Table name is alias, if alias is set

    	String sCatalog = cachedCatalogInfo;
		if (StringUtil.isEmpty(sCatalog))
		{
			sCatalog = null;
		}
		
    	String sSchema = cachedSchemaInfo;
		if (StringUtil.isEmpty(sSchema))
		{
			sSchema = null;
		}

		String[] sSchemaTable = splitSchemaTable(pWriteBackTable);
		
		if (sSchema == null)
		{
			sSchema = removeQuotes(sSchemaTable[0]);
		}
		
		String sTable = sSchemaTable[1];
		
		if (sTable != null)
		{
			sTable = sTable.trim();
			
			if (sTable.endsWith(" m"))
			{
				sTable = sTable.substring(0, sTable.length() - 2).trim();
			}
			if (isQuoted(sTable))
			{
			    sTable = removeQuotes(sTable);
			}
			else
			{
			    if (isAutoQuote("A"))
			    {
			        sTable = sTable.toLowerCase();
			    }
			    else if (isAutoQuote("a"))
			    {
			        sTable = sTable.toUpperCase();
			    }
			}
		}
		if (cachedTableInfo != null && sTable != null && cachedTableInfo.equalsIgnoreCase(sTable))
		{
		    sTable = cachedTableInfo;
		}

		return new TableInfo(sCatalog, sSchema, sTable);
	}

	/**
	 * Returns the meta data information for the specified query, and configures all columns with defaults.
	 * 
	 * @param pWriteBackTable		the write back table to use for the isWriteable() state (Optional)
 	 * @return the meta data for the specified query, and initials all columns.
	 * @throws DataSourceException 
	 *            if an <code>Exception</code> occur during getting the meta data or 
	 *            if the storage is not opened or 
	 *            if one columns SQL type is not supported
	 */
	public final TableInfo getTableInfo(String pWriteBackTable) throws DataSourceException
	{
	    TableInfo tableInfo;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIgnoreCaseIdentifier(pWriteBackTable);
			
			tableInfo = ghtTableInfoCache.get(dbAccessIdentifier, tableIdentifier);
			if (tableInfo == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_TABLEINFO, pWriteBackTable);

                try
                {
    				tableInfo = getTableInfoIntern(pWriteBackTable);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				if (tableInfo == null)
				{
					tableInfo = TABLEINFO_NULL;
				}
				
				ghtTableInfoCache.put(dbAccessIdentifier, tableIdentifier, tableInfo);
			}
			
			if (tableInfo == TABLEINFO_NULL)
			{
				return null;
			}
			else
			{
				return tableInfo;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_TABLEINFO, pWriteBackTable);
            
            try
            {
    		    tableInfo = getTableInfoIntern(pWriteBackTable);
            }
            finally
            {
                CommonUtil.close(record);
            }
            
            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIgnoreCaseIdentifier(pWriteBackTable);

                if (ghtTableInfoCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtTableInfoCache.put(dbAccessIdentifier, tableIdentifier, tableInfo != null ? tableInfo : TABLEINFO_NULL);
                }
            }
		    
			return tableInfo;
		}
	}
	
	/**
	 * Sets all <code>Filter</code> parameter values into the <code>PreparedStatement</code>.
	 * 
	 * @param iStartParameterIndex 
	 *            the start index for the parameters to set.
	 * @param pStatement 
	 * 			  the <code>PreparedStatement</code> to initialize
	 * @param pParameter 
	 *            the <code>Filter</code> to get the values
	 * @throws DataSourceException     
	 *            if the values can't set into the <code>PreparedStatement</code>
	 */
	private void setFilterParameter(int iStartParameterIndex, PreparedStatement pStatement, Object[] pParameter) throws DataSourceException
	{
		for (int index = 0; index < pParameter.length; index++)
		{
			try
			{
				if (pParameter[index] != null)
				{
					pStatement.setObject(iStartParameterIndex + index, convertValueToDatabaseSpecificObject(pParameter[index]));
				}
				else
				{
					pStatement.setNull(iStartParameterIndex + index, Types.VARCHAR);
				}
			}
			catch (SQLException sqlException)
			{
				throw new DataSourceException("Set value into PreparedStatement failed!",
											  formatSQLException(sqlException));
			}			
		}
	}
	
	/**
	 * Sets the values of all changed columns to store from the value Object[]s into the 
	 * <code>PreparedStatement</code> and returns the last used parameter index.
	 * 
	 * @param pInsert 		  		the <code>PreparedStatement</code> to initialize
	 * @param pServerColumnMetaData	the column meta data to use.
	 * @param iaWriteables			the writable columns as int index array
	 * @param pNew             		the new values Object[]
	 * @param pOld  	        	the old values Object[]
	 * @throws DataSourceException	if the values can't set into the <code>PreparedStatement</code>     
	 * @return the last used parameter index of the <code>PreparedStatement</code>.           
	 */
	protected int setColumnsToStore(PreparedStatement pInsert, ServerColumnMetaData[] pServerColumnMetaData, 
			                        int[] iaWriteables, Object[] pNew, Object[] pOld) throws DataSourceException
	{
		int i = 1;
		for (int j = 0; j < iaWriteables.length; j++)
		{
			int k = iaWriteables[j];
			
			if (pServerColumnMetaData[k].getDataType().compareTo(pNew[k], pOld == null ? null : pOld[k]) != 0)
	        {
				try
				{
					if (pNew[k] == null)
					{
						pInsert.setNull(i, pServerColumnMetaData[k].getSQLType());
					}
					else
					{
						Object object = convertValueToDatabaseSpecificObject(pNew[k]);
						
						if (object instanceof IFileHandle)
						{
							IFileHandle fileHandle = (IFileHandle)object;
							pInsert.setObject(i, FileUtil.getContent(fileHandle.getInputStream()));
						}
						else
						{
							pInsert.setObject(i, convertValueToDatabaseSpecificObject(pNew[k]));
						}
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

	/**
	 * Gets all default column values of a specific table.
	 *  
	 * @param pCatalog the catalog name
	 * @param pSchema the schema name
	 * @param pTable the table name
	 * @return a {@link Hashtable} with the column name as key and the default value as value. It only contains columns
	 *         with a default value
	 * @throws DataSourceException if the database access throws an exception
	 */
	public final Map<String, Object> getDefaultValues(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
	    Map<String, Object> defaultValues;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
			
			defaultValues = ghtDefaultValuesCache.get(dbAccessIdentifier, tableIdentifier);
			if (defaultValues == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_DEFAULTVALUES, pCatalog, pSchema, pTable);

                try
                {
			        defaultValues = getDefaultValuesIntern(pCatalog, pSchema, pTable);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				if (defaultValues == null)
				{
					defaultValues = DEFAULT_VALUES_NULL;
				}

				ghtDefaultValuesCache.put(dbAccessIdentifier, tableIdentifier, defaultValues);
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
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_DEFAULTVALUES, pCatalog, pSchema, pTable);
            
            try
            {
                defaultValues = getDefaultValuesIntern(pCatalog, pSchema, pTable);
            }
            finally
            {
                CommonUtil.close(record);
            }
            
            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

                if (ghtDefaultValuesCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtDefaultValuesCache.put(dbAccessIdentifier, tableIdentifier, defaultValues != null ? defaultValues : DEFAULT_VALUES_NULL);
                }
            }
		    
			return defaultValues;
		}
	}
	
	/**
	 * Gets all default column values of a specific table.
	 *  
	 * @param pCatalog the catalog name
	 * @param pSchema the schema name
	 * @param pTable the table name
	 * @return a {@link Hashtable} with the column name as key and the default value as value. It only contains columns
	 *         with a default value
	 * @throws DataSourceException if the database access throws an exception
	 */
	protected Map<String, Object> getDefaultValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		Map<String, Object> htDefaults = new Hashtable<String, Object>();

		ResultSet res = null;
		
		try
		{
			long lMillis = System.currentTimeMillis();

			res = getConnectionIntern().getMetaData().getColumns(pCatalog, pSchema, pTable, null);
			
			while (res.next())
			{
				String sValue = res.getString("COLUMN_DEF");

				if (!StringUtil.isEmpty(sValue))
				{
					String sColumnName = res.getString("COLUMN_NAME");
					
					try
					{
						Object objValue = translateDefaultValue(sColumnName, res.getInt("DATA_TYPE"), sValue.trim());

						if (objValue != null)
						{
							htDefaults.put(new Name(sColumnName, quote(sColumnName)).getName(), objValue); // Use Name class, to have one point of change.
						}
					}
					catch (Exception e)
					{
						//no default value
						//debug(sValue, e);
					}
				}
			}
			
			if (isLogEnabled(LogLevel.DEBUG))
            {
                debug("getDefaultValuesIntern(", pTable, ") in ", Long.valueOf((System.currentTimeMillis() - lMillis)), "ms");
            }
			
			return htDefaults;
		}
		catch (SQLException sqlException)
		{
			throw new DataSourceException("Get default values failed!", formatSQLException(sqlException));
		}
		finally
		{
		    CommonUtil.close(res);
		}
	}
	
	/**
	 * Translates a default value from a column to the datatype object.
	 * 
	 * @param pColumnName the column name to translate
	 * @param pDataType the datatype of the column
	 * @param pDefaultValue the original default value from the database
	 * @return the default value with the datatype of the column or <code>null</code> if the default value is not valid
	 * @throws Exception if the type translation causes an error or the datatype is not supported
	 */
	protected Object translateDefaultValue(String pColumnName, int pDataType, String pDefaultValue) throws Exception
	{
	    if (pDefaultValue != null)
	    {
	        pDefaultValue = pDefaultValue.trim();
	    }
	    
	    pDefaultValue = removeBrackets(pDefaultValue); // MS SQL puts ever default value in brackets
        pDefaultValue = removeBrackets(pDefaultValue); // MS SQL puts numbers additionally in brackets 
	    
	    if (pDefaultValue != null // detect current date clauses 
	            && (pDataType == Types.DATE || pDataType == Types.TIME || pDataType == Types.TIMESTAMP))
	    {
	        String defaultValueLower = pDefaultValue.toLowerCase();
	        
	        if (defaultValueLower.startsWith("current_timestamp") || "current_date".equals(defaultValueLower) || "sysdate".equals(defaultValueLower)
	                || "now()".equals(defaultValueLower) || "getdate()".equals(defaultValueLower))
            {
                return pDefaultValue;
            }
	    }
        
		pDefaultValue = StringUtil.removeQuotes(pDefaultValue, "'"); // remove quotes of strings
		
		int castPos = pDefaultValue.indexOf("::"); // postgres has most likely a cast afterwards.
		if (castPos >= 0)
		{
		    pDefaultValue = pDefaultValue.substring(0, castPos).trim();
		}
		
		return translateValue(pDataType, pDefaultValue);
	}
	
	/**
	 * Removes brackets.
	 * 
	 * @param pDefaultValue the default value.
	 * @return the value with removed brackets.
	 */
	protected String removeBrackets(String pDefaultValue)
	{
	    if (pDefaultValue != null && pDefaultValue.startsWith("(") && pDefaultValue.endsWith(")"))
	    {
	        return pDefaultValue.substring(1, pDefaultValue.length() - 1); 
	    }
	    
	    return pDefaultValue;
	}
	
	/**
	 * Translates an object value to the datatype object.
	 * 
	 * @param pDataType the datatype of the column
	 * @param pValue the value from the database
	 * @return the value with the specified datatype or <code>null</code> if the value is not valid
	 * @throws Exception if the type translation causes an error or the datatype is not supported
	 */
	protected Object translateValue(int pDataType, String pValue) throws Exception
	{
		if (StringUtil.isEmpty(pValue) || pValue.equalsIgnoreCase("null"))
		{
			return null;
		}
		
		switch(pDataType)
		{
			case NCHAR:
			case NVARCHAR:
			case LONGNVARCHAR:
			case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            	return pValue;

            case Types.BIT:
			case Types.BOOLEAN:
                //#1751
                try
                {
                    return Integer.parseInt(pValue) == 0 ? Boolean.FALSE : Boolean.TRUE;
                }
                catch (Exception e)
                {
                	return Boolean.valueOf(pValue.substring(0, 1).equalsIgnoreCase("t"));
                }
            	
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            	if ("true".equalsIgnoreCase(pValue))
            	{
            		return Boolean.TRUE;
            	}
            	else if ("false".equalsIgnoreCase(pValue))
            	{
            		return Boolean.FALSE;
            	}
            	else
            	{
            		return new BigDecimal(pValue);
            	}
            	
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            	if (pValue.startsWith("0000-00-00"))
            	{
            		return null;
            	}
            	return new Timestamp(dateUtil.parse(pValue).getTime());
            	
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            	return null;
            	
            default:
    			throw new DataSourceException("SQL Type '" + pDataType + "' is not support!");
		}
	}
	
	/**
	 * Gets the allowed values from a specific table. The allowed values are defined through check constraints
	 * or other table related restrictions.
	 * 
	 * @param pCatalog the catalog name
	 * @param pSchema the schema name
	 * @param pTable the table to check
	 * @return a {@link Hashtable} with a column name as key and the allowed values as array of {@link Object}s or 
	 *         <code>null</code> if there are no allowed values
	 * @throws DataSourceException if the database access throws an exception          
	 */
	protected Map<String, Object[]> getAllowedValuesIntern(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
		return null;
	}
	
	/**
	 * Gets the allowed values from a specific table. The allowed values are defined through check constraints
	 * or other table related restrictions.
	 * 
	 * @param pCatalog the catalog name
	 * @param pSchema the schema name
	 * @param pTable the table to check
	 * @return a {@link Hashtable} with a column name as key and the allowed values as array of {@link Object}s or 
	 *         <code>null</code> if there are no allowed values
	 * @throws DataSourceException if the database access throws an exception          
	 */
	public final Map<String, Object[]> getAllowedValues(String pCatalog, String pSchema, String pTable) throws DataSourceException
	{
	    Map<String, Object[]> allowedValues;
	    
		if (isMetaDataCacheEnabled())
		{
			String dbAccessIdentifier = getIdentifier();
			String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);
			
			allowedValues = ghtAllowedValuesCache.get(dbAccessIdentifier, tableIdentifier);
			if (allowedValues == null)
			{
                Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_ALLOWEDVALUES, pCatalog, pSchema, pTable);

                try
                {
    				allowedValues = getAllowedValuesIntern(pCatalog, pSchema, pTable);
                }
                finally
                {
                    CommonUtil.close(record);
                }
                
				if (allowedValues == null)
				{
					allowedValues = ALLOWED_VALUES_NULL;
				}

				ghtAllowedValuesCache.put(dbAccessIdentifier, tableIdentifier, allowedValues);
			}
			
			if (allowedValues == ALLOWED_VALUES_NULL)
			{
				return null;
			}
			else
			{
				return allowedValues;
			}
		}
		else
		{
            Record record = ProtocolFactory.openRecord(ICategoryConstants.DATABASE, ICommandConstants.DB_GET_ALLOWEDVALUES, pCatalog, pSchema, pTable);
            
            try
            {
    		    allowedValues = getAllowedValuesIntern(pCatalog, pSchema, pTable);
            }
            finally
            {
                CommonUtil.close(record);
            }
            //it's still possible that global metadata cache is enabled, but caching is temporary disabled for the current session!
            if (DBStorage.isGlobalMetaDataCacheEnabled())
            {
                String dbAccessIdentifier = getIdentifier();
                String tableIdentifier = createIdentifier(pCatalog, pSchema, pTable);

                if (ghtAllowedValuesCache.get(dbAccessIdentifier, tableIdentifier) != null)
                {
                    ghtAllowedValuesCache.put(dbAccessIdentifier, tableIdentifier, allowedValues != null ? allowedValues : ALLOWED_VALUES_NULL);
                }
            }
		    
			return allowedValues;
		}
	}
	
	/**
	 * Gets the default allowed values for a given column from a given table. This method will be called if no other 
	 * default values for the given column are available.
	 * 
	 * @param pCatalog the catalog name
	 * @param pSchema the schema name
	 * @param pTable the table to check
	 * @param pMetaData the column meta data
	 * @return the default allowed values or <code>null</code> if there are no default values
	 */
	public Object[] getDefaultAllowedValues(String pCatalog, String pSchema, String pTable, ServerColumnMetaData pMetaData)
	{
		if (pMetaData.getTypeIdentifier() == BooleanDataType.TYPE_IDENTIFIER)
		{
			return new Boolean[] {Boolean.TRUE, Boolean.FALSE};
		}
		
		return null;
	}
	
	/**
	 * Returns the database specific statement to lock the specified row in the database.
	 *  
	 * @param pSelectStatement	the select statement to execute.
	 * @param pPKFilter			the PK filter with the values to use.
	 * @param pMaxRows          the maximum number of allowed rows. A negative number means no limit. 
	 * @return the number of affected rows
	 * @throws DataSourceException if some parts are missing for the statement
	 */
    private int getRowCount(String pSelectStatement, ICondition pPKFilter, int pMaxRows) throws DataSourceException
	{
		long lMillis = System.currentTimeMillis();
		
		// update DataRow into database
		PreparedStatement psSelect = null;
		ResultSet res = null;

		try
		{
			psSelect = getPreparedStatement(pSelectStatement);
			
			Object[] params = getParameter(pPKFilter);
			// set WHERE Parameter with Values from PK of the new DataRow
			setFilterParameter(1, psSelect, params);
	
			setSavepoint();
			
			res = psSelect.executeQuery();
				
            if (isLogEnabled(LogLevel.DEBUG))
            {
                debug(pSelectStatement, "\n", params, "\ncomplete time: ", Long.valueOf(System.currentTimeMillis() - lMillis), "ms");
            }
            
			int iCount = 0;
			
			while (res.next())
			{
				iCount++;
				
				if (pMaxRows >= 0 && iCount > pMaxRows)
				{
					throw new DataSourceException("Too many rows found! " + pSelectStatement);
				}
			}
			
			// if iCount == 0, do nothing!!!
			
			return iCount;
		}
		catch (SQLException se)
		{
			rollbackToSavepoint();
			throw new DataSourceException("Get row count failed! " + pSelectStatement, formatSQLException(se));
		}
		finally
		{
			releaseSavepoint();
		    CommonUtil.close(res, psSelect);
		}
	}
	
	/**
	 * Returns the ANSI SQL String for this <code>ICondition</code>.
	 * 
	 * @param pServerMetaData		the MetaDataColumn array to use.
	 * @param pCondition			the Condition to use.
	 * @param pUseRealColumnName	<code>true</code> to use the prefixed column (real column name) and <code>false</code> to
	 *                      		ignore prefixes (the simple name of the column)
	 * @throws DataSourceException if columns not existing
	 * @return the ANSI SQL String for this <code>ICondition</code>.
	 */
	protected String getSQL(ServerMetaData pServerMetaData, ICondition pCondition, boolean pUseRealColumnName) throws DataSourceException
	{
		StringBuilder result = new StringBuilder();
		
		if (pCondition instanceof CompareCondition)
		{
			CompareCondition cCompare = (CompareCondition)pCondition;
			Object           oValue   = cCompare.getValue();
			
			if (!cCompare.isIgnoreNull() || oValue != null)
			{
				String sColumnName = null;
				if (pServerMetaData != null)
				{
					try
					{
						if (pUseRealColumnName)
						{
							sColumnName = pServerMetaData.getServerColumnMetaData(cCompare.getColumnName()).getRealQueryColumnName();
						}
						else
						{
							sColumnName = pServerMetaData.getServerColumnMetaData(cCompare.getColumnName()).getColumnName().getQuotedName();
						}
					}
					catch (ModelException modelException)
					{
						// Column doesn't exit in MetaData, then use the column name of the Condition itself.
					}
				}
				if (sColumnName == null)
				{
					sColumnName = cCompare.getColumnName();
				}
				
				if ((cCompare instanceof LikeReverse || cCompare instanceof LikeReverseIgnoreCase)
					&& oValue != null)
				{
					if (cCompare instanceof LikeReverse)
					{
						result.append(createWhereParam(pServerMetaData, cCompare));
					}
					else if (cCompare instanceof LikeReverseIgnoreCase)
					{
						result.append("UPPER(");
						result.append(createWhereParam(pServerMetaData, cCompare));
						result.append(")");
					}						
				}
				else if (cCompare instanceof LikeIgnoreCase)
				{
					result.append("UPPER(");
					result.append(createWhereColumn(pServerMetaData, cCompare, sColumnName));
					result.append(")");
				}						
				else 
				{
					result.append(createWhereColumn(pServerMetaData, cCompare, sColumnName));
				}						
				
				result.append(' ');
				
				if (oValue == null)
				{
					result.append("IS NULL");
				}
				else
				{
					if (cCompare instanceof Equals)
					{
						result.append("= ");
					}
					else if (cCompare instanceof LikeIgnoreCase || cCompare instanceof LikeReverseIgnoreCase)
					{
						result.append("LIKE UPPER(");
					}
					else if (cCompare instanceof Like
							|| cCompare instanceof LikeReverse)
					{
						result.append("LIKE ");
					}
					else if (cCompare instanceof Greater)
					{
						result.append("> ");
					}
					else if (cCompare instanceof GreaterEquals)
					{
						result.append(">= ");
					}
					else if (cCompare instanceof Less)
					{
						result.append("< ");
					}
					else if (cCompare instanceof LessEquals)
					{
						result.append("<= ");
					}
					else
					{
						result.append(' ');
					}

					if (cCompare instanceof LikeReverse || cCompare instanceof LikeReverseIgnoreCase)
					{
						result.append(createReplace(createReplace(createWhereColumn(pServerMetaData, cCompare, sColumnName), "*", "%"), "?", "_"));
					}
					else
					{
						result.append(createWhereParam(pServerMetaData, cCompare));
					}
					
					if (cCompare instanceof LikeIgnoreCase || cCompare instanceof LikeReverseIgnoreCase)
					{
						result.append(")");
					}
				}
			}
		}
		else if (pCondition instanceof OperatorCondition)
		{
			OperatorCondition cOperator = (OperatorCondition) pCondition;
			
			ICondition[] caConditions = cOperator.getConditions();
			for (int i = 0; i < caConditions.length; i++)
			{
				String sTempSQL = getSQL(pServerMetaData, caConditions[i], pUseRealColumnName);
				
				if (sTempSQL != null && sTempSQL.length() > 0)
				{
					if (i > 0 && result.length() > 0)
					{
						if (cOperator instanceof And)
						{
							result.append(" AND ");
						}
						else if (cOperator instanceof Or)
						{
							result.append(" OR ");
						}
					}
					if (caConditions[i] instanceof OperatorCondition)
					{
						result.append("(");
						result.append(sTempSQL);
						result.append(")");
					}
					else
					{
						result.append(sTempSQL);
					}
				}
			}
		}
		else if (pCondition instanceof Not)
		{
			ICondition cCond = ((Not) pCondition).getCondition();
			String sTempSQL = getSQL(pServerMetaData, cCond, pUseRealColumnName);
			
            if (sTempSQL != null && sTempSQL.length() > 0)
            {
    			result.append("NOT ");
    			if (cCond instanceof OperatorCondition)
    			{
    				result.append("(");
    				result.append(sTempSQL);
    				result.append(")");
    			}
    			else
    			{
    				result.append(sTempSQL);
    			}
            }
		}
		
		return result.toString();
	}

	/**
	 * Create an DB specific replace command, which replacs in the pSource all pOld to pNew.
	 * This implementation use the ANSI SQL REPLACE command.
	 *  
	 * @param pSource	the source to replace.
	 * @param pOld		the old value.
	 * @param pNew		the new value.
	 * 
	 * @return	the SQL command to to this.
	 */
	protected String createReplace(String pSource, String pOld, String pNew) 
	{
		StringBuilder sb = new StringBuilder("REPLACE(");
		sb.append(pSource);
		sb.append(",\'");
		sb.append(pOld);
		sb.append("\',\'");
		sb.append(pNew);
		sb.append("\')");
		return sb.toString();
	}
	
	/**
	 * Creates the where parameter. That is normally a single question mark, but it depends on
	 * the database if conversions are needed.
	 * 
	 * @param pServerMetaData 	the server metadata
	 * @param pCompare 			the compare condition
	 * @return the parameter representation for where clause
	 */
	protected String createWhereParam(ServerMetaData pServerMetaData, CompareCondition pCompare)
	{
		return "?";
	}

	/**
	 * Creates the where column. That is normally just the column name, but it depends on
	 * the database if conversions are needed.
	 * 
	 * @param pServerMetaData 	the server metadata
	 * @param pCompare 			the compare condition
	 * @param pColumnName		the column name to use
	 * @return the column name representation for where clause
	 */
	protected String createWhereColumn(ServerMetaData pServerMetaData, CompareCondition pCompare, String pColumnName)
	{
		return pColumnName;
	}
	
	/**
	 * Check if the Type of the column and the value to compare is equal.
	 *  
	 * @param pServerColumnMetaData		the server column meta data for the column to compare. 
	 * @param pCompare					the compare condition to use.
	 * @return false if the type isn't equal.
	 */
	public boolean isTypeEqual(ServerColumnMetaData pServerColumnMetaData, CompareCondition pCompare)
	{
	    if (pServerColumnMetaData == null)
	    {
	        return true;
	    }

	    // check if type is !=
		Object    oValue   = pCompare.getValue();
		int typeIdentifier = pServerColumnMetaData.getTypeIdentifier();
		return (oValue instanceof String && typeIdentifier == StringDataType.TYPE_IDENTIFIER)
		        || (oValue instanceof Date && typeIdentifier == TimestampDataType.TYPE_IDENTIFIER)
				|| (oValue instanceof Number && typeIdentifier == BigDecimalDataType.TYPE_IDENTIFIER)
				|| (oValue instanceof Boolean && typeIdentifier == BooleanDataType.TYPE_IDENTIFIER);
	}
	
	/**
	 * Returns the parameters used for this <code>ICondition</code>.
	 * 
	 * @param pCondition		the Condition to use.
	 * @param pFromRow				the row index from to fetch
	 * @param pMinimumRowCount		the minimum count row to fetch
	 * @return the parameters used for this <code>ICondition</code>.
	 */
	protected Object[] getParameter(ICondition pCondition, int pFromRow, int pMinimumRowCount)
	{
		return getParameter(pCondition);
	}
	
	/**
	 * Returns the parameters used for this <code>ICondition</code>.
	 * 
	 * @param pCondition the Condition to use.
	 * @return the parameters used for this <code>ICondition</code>.
	 */
	protected Object[] getParameter(ICondition pCondition)
	{
		List<Object> values = new ArrayList<Object>();
		getParameter(pCondition, values, new ArrayList<String>(), new HashMap<String, CompareCondition>());
		
		return values.toArray(new Object[values.size()]);
	}
	
	/**
	 * Separates the schema and table from the given from clause. The separation is only 
	 * possible if the from clause is simple, e.g. it does not contain any quote characters
	 * and does not contain separators (',').
	 * 
	 * @param pFromClause the from clause
	 * @return [0]...schema, [1]...table
	 */
	protected String[] splitSchemaTable(String pFromClause)
	{
		boolean bComplex = false;
		
		//check if the from clause was special (means more than a view or table)
		if (pFromClause.indexOf(',') >= 0)
		{
			bComplex = true;
		}
		
		if (sOpenQuote != null
				&& sOpenQuote.length() > 0
				&& pFromClause.indexOf(sOpenQuote) >= 0)
		{
			bComplex = true;
		}
		
		int iFirstSpacePos = pFromClause.indexOf(' ');
		
		if (iFirstSpacePos > 0)
		{
			int iNextSpacePos = pFromClause.indexOf(' ', iFirstSpacePos + 1);
			
			if (iNextSpacePos > iFirstSpacePos)
			{
				bComplex = true;
			}
		}
		
		String[] sResult = new String[2];
		
		if (!bComplex)
		{
			//[schema.]table[ alias]

			if (iFirstSpacePos < 0)
			{
				iFirstSpacePos = pFromClause.length();
			}

			int iDotPos = pFromClause.indexOf('.');
			
			if (iDotPos > 0)
			{
				sResult[0] = pFromClause.substring(0, iDotPos);
				sResult[1] = pFromClause.substring(iDotPos + 1, iFirstSpacePos);
			}
			else
			{
				sResult[1] = pFromClause.substring(0, iFirstSpacePos);
			}
		}
		else
		{
			sResult[1] = pFromClause;
		}
		
		return sResult;
	}
	
	/**
	 * Sets the user-defined default schema.
	 * 
	 * @param pSchema the schema name
	 * @see #getDefaultSchema()
	 */
	public void setDefaultSchema(String pSchema)
	{
		sDefaultSchema = pSchema;
	}
	
	/**
	 * Gets the default schema name, if it was set manually.
	 * 
	 * @return the user-defined default schema name
	 * @see #setDefaultSchema(String)
	 */
	public String getDefaultSchema()
	{
		return sDefaultSchema;
	}
	
	/**
	 * Gets the Object from the result set, and ensures that numbers are always returned as BigDecimal, and dates as Timestamp.
	 * This is for database independency support.
	 * 
	 * @param pResultSet the result set
	 * @param pIndex the index
	 * @param pColumnMetaData the server column meta data
	 * @return the object
	 * @throws SQLException if it fails.
	 */
	protected Object getObjectFromResultSet(ResultSet pResultSet, int pIndex, ServerColumnMetaData pColumnMetaData) throws SQLException
	{
		if (pColumnMetaData.getColumnMetaData().getTypeIdentifier() == Types.BOOLEAN)
		{
			Object oValue = pResultSet.getObject(pIndex);
			
			if (oValue == null)
			{
				return null;
			}
			
			if (oValue instanceof Number)
			{
				return ((Number)oValue).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
			}
			else if (oValue instanceof String)
			{
				String strValue = ((String)oValue).trim();
				if (strValue.length() == 0)
				{
					return null;
				}
				
	            try
	            {
	                return Integer.parseInt(strValue) == 0 ? Boolean.FALSE : Boolean.TRUE;
	            }
	            catch (Exception e)
	            {
	            	return Boolean.valueOf(strValue.substring(0, 1).equalsIgnoreCase("t"));
	            }
			}
			
			return oValue;
		}
		
		return getObjectFromResultSet(pResultSet, pIndex);
	}
	
	/**
	 * Gets the Object from the result set, and ensures that numbers are always returned as BigDecimal, and dates as Timestamp.
	 * This is for database independency support.
	 * 
	 * @param pResultSet the result set
	 * @param pIndex the index
	 * @return the object
	 * @throws SQLException if it fails.
	 */
	protected Object getObjectFromResultSet(ResultSet pResultSet, int pIndex) throws SQLException
	{
		Object oValue = pResultSet.getObject(pIndex);
		
		// convert to Java types
		if (oValue instanceof Number
				 && !(oValue instanceof BigDecimal))
		{
			oValue = pResultSet.getBigDecimal(pIndex);
		}
		else if (oValue instanceof java.util.Date 
			&& !(oValue instanceof Timestamp))
		{
			oValue = pResultSet.getTimestamp(pIndex);
		}
        else if (oValue instanceof Temporal) // maybe we should extend this, to support time zone and more.
        {
            oValue = pResultSet.getTimestamp(pIndex);
        }
		else if (oValue instanceof SQLXML)
		{
		    oValue = ((SQLXML)oValue).getString();
		}
		
		return oValue;
	}
	
	/**
	 * Enables the database specific implementation to handle/convert special objects. Some databases have
	 * datatypes that are not defined in the standard. This datatypes have specific classes in the JDBC drivers.
	 * With this method it is possible to convert the values of specific JDBC driver classes into usable objects.
	 * We can not send any JDBC object to the client!
	 * 
	 * @param pColumnMetaData the column metadata
	 * @param pObject the read object
	 * @return the value to use
	 * @throws SQLException if it fails.
	 */
	protected Object convertDatabaseSpecificObjectToValue(ServerColumnMetaData pColumnMetaData, Object pObject) throws SQLException
	{
		return pObject;
	}
	
    /**
     * Converts a parameter to a standard value for the specific database. Not all values are supported from
     * the underlying database, e.g. java.sql.Timestamp is preferred instead of java.util.Date.
     *  
     * @param pParam any parameter
     * @return the database specific value
     * @throws SQLException if it fails
     */
    protected Object convertValueToDatabaseSpecificObject(AbstractParam pParam) throws SQLException
    {
        return convertValueToDatabaseSpecificObject(pParam.getValue());
    }

	/**
	 * Converts an object to a standard value for the specific database. Not all values are supported from
	 * the underlying database, e.g. java.sql.Timestamp is preferred instead of java.util.Date.
	 *  
	 * @param pValue any value
	 * @return the database specific value
	 */
	protected Object convertValueToDatabaseSpecificObject(Object pValue)
	{
		if (pValue instanceof Date && !(pValue instanceof Timestamp))
		{
			return new Timestamp(((Date)pValue).getTime());
		}
		else
		{
			return pValue;
		}
	}

	/**
	 * Replaces all kinds of whitespaces by a space.
	 * 
	 * @param pString the input string
	 * @return the string contains only spaces
	 */
	protected static final String replaceWhitespace(String pString)
	{
	    return REPLACE_WHITESPACE.matcher(pString).replaceAll(" ");
	}
	
    /**
     * Add this DBAccess from identifier list.
     * @param pIdentifier the identifier
     * @param pDBAccess the dbAccess
     */
    protected static synchronized void addDBAccessByIdentifier(String pIdentifier, DBAccess pDBAccess)
    {
        List<WeakReference<DBAccess>> dbAccesses = kvlDBAccessByIdentifier.get(pIdentifier);
        
        if (dbAccesses != null)
        {
            for (int i = dbAccesses.size() - 1; i >= 0; i--)
            {
                DBAccess dbAccess = dbAccesses.get(i).get();
                if (dbAccess == null)
                {
                    dbAccesses.remove(i);
                }
            }
        }
        
        kvlDBAccessByIdentifier.put(pIdentifier, new WeakReference(pDBAccess));
    }
    
	/**
	 * Removes this DBAccess from identifier list.
     * @param pIdentifier the identifier
     * @param pDBAccess the dbAccess
	 */
    protected static synchronized void removeDBAccessByIdentifier(String pIdentifier, DBAccess pDBAccess)
    {
        List<WeakReference<DBAccess>> dbAccesses = kvlDBAccessByIdentifier.get(pIdentifier);
        
        if (dbAccesses != null)
        {
            for (int i = dbAccesses.size() - 1; i >= 0; i--)
            {
                DBAccess dbAccess = dbAccesses.get(i).get();
                if (dbAccess == null || dbAccess == pDBAccess)
                {
                    dbAccesses.remove(i);
                }
            }
        }
    }
	
	/**
	 * Gets the list of DBAccess open for the identifier.
	 * @param pIdentifier the identifier
	 * @return the list of DBAccess
	 */
	public static synchronized List<DBAccess> getDBAccessByIdentifier(String pIdentifier)
	{
		List<WeakReference<DBAccess>> dbAccesses = kvlDBAccessByIdentifier.get(pIdentifier);
		
		ArrayUtil<DBAccess> result = new ArrayUtil<DBAccess>();
		
		if (dbAccesses != null)
		{
			for (int i = dbAccesses.size() - 1; i >= 0; i--)
			{
				DBAccess dbAccess = dbAccesses.get(i).get();
				if (dbAccess == null)
				{
					dbAccesses.remove(i);
				}
				else
				{
					result.add(0, dbAccess);
				}
			}
		}
		return result;
	}
	
	/**
	 * Gets the application name for this DBAccess.
	 * @return the application name for this DBAccess.
	 */
	public String getApplicationName()
	{
		return sApplicationName;
	}

	/**
	 * Gets the identifier for this DBAccess.
	 * @return the identifier for this DBAccess.
	 */
	public String getIdentifier()
	{
		if (sIdentifier == null)
		{
			sIdentifier = createIdentifier(sUrl, sUsername);

			kvlApplicationGroups.put(sApplicationName, sIdentifier, true);
		}
		
		return sIdentifier;
	}
	
	/**
	 * The event after open DBAccess.
	 * 
	 * @return the event handler for after open DBAccess
	 */
	public static EventHandler<IOpenDBAccessListener> eventOpenedDBAccess()
	{
		if (eventOpenedDBAccess == null)
		{
			eventOpenedDBAccess = new EventHandler<IOpenDBAccessListener>(IOpenDBAccessListener.class);
		}
		
		return eventOpenedDBAccess;
	}
	
	/**
	 * The event after close DBAccess.
	 * 
	 * @return the event handler for after close DBAccess
	 */
	public static EventHandler<ICloseDBAccessListener> eventClosedDBAccess()
	{
		if (eventClosedDBAccess == null)
		{
			eventClosedDBAccess = new EventHandler<ICloseDBAccessListener>(ICloseDBAccessListener.class);
		}
		
		return eventClosedDBAccess;
	}
	
	/**
	 * The event after clear meta data.
	 * 
	 * @return the event handler for after clear meta data
	 */
	public static EventHandler<IClearMetaDataListener> eventClearedMetaData()
	{
		if (eventClearedMetaData == null)
		{
			eventClearedMetaData = new EventHandler<IClearMetaDataListener>(IClearMetaDataListener.class);
		}
		
		return eventClearedMetaData;
	}
	
	/**
	 * The event before open DBStorage.
	 * 
	 * @return the event handler for before open DBStorage
	 */
	public EventHandler<IOpenDBStorageListener> eventBeforeOpenDBStorage()
	{
		if (eventBeforeOpenDBStorage == null)
		{
			eventBeforeOpenDBStorage = new EventHandler<IOpenDBStorageListener>(IOpenDBStorageListener.class);
		}
		
		return eventBeforeOpenDBStorage;
	}
	
	/**
	 * The event after open DBStorage.
	 * 
	 * @return the event handler for after open DBStorage
	 */
	public EventHandler<IOpenDBStorageListener> eventAfterOpenDBStorage()
	{
		if (eventAfterOpenDBStorage == null)
		{
			eventAfterOpenDBStorage = new EventHandler<IOpenDBStorageListener>(IOpenDBStorageListener.class);
		}
		
		return eventAfterOpenDBStorage;
	}
	
	/**
	 * Fires the event after open DBAccess.
	 */
	protected void fireEventAfterOpenDBAccess()
	{
		if (eventOpenedDBAccess != null && eventOpenedDBAccess.isDispatchable())
		{
			try  
			{
				eventOpenedDBAccess.dispatchEvent(this);
			} 
			catch (Throwable ex) 
			{
				info(ex);
			}
		}
	}
	
	/**
	 * Fires the event closed DBAccess.
	 */
	protected void fireEventClosedDBAccess()
	{
		if (eventClosedDBAccess != null && eventClosedDBAccess.isDispatchable())
		{
			try 
			{
				eventClosedDBAccess.dispatchEvent(this);
			} 
			catch (Throwable ex) 
			{
            	info(ex);
			}
		}
	}

	/**
	 * Fires the event cleared meta data.
	 * 
	 * @param pApplicationName the application name.
	 * @param pIdentifier the identifier connected to the application name.
	 */
	protected static void fireEventClearedMetaData(String pApplicationName, List<String> pIdentifier)
	{
		if (eventClearedMetaData != null && eventClearedMetaData.isDispatchable())
		{
			try 
			{
				eventClearedMetaData.dispatchEvent(pApplicationName, pIdentifier);
			} 
			catch (Throwable ex) 
			{
            	info(ex);
			}
		}
	}
	
	/**
	 * Fires the event before open DBStorage.
	 * 
	 * @param pStorage the storage to use.
	 */
	protected void fireEventBeforeOpenDBStorage(DBStorage pStorage)
	{
		if (eventBeforeOpenDBStorage != null && eventBeforeOpenDBStorage.isDispatchable())
		{
			try  
			{
				eventBeforeOpenDBStorage.dispatchEvent(pStorage);
			} 
			catch (Throwable ex) 
			{
				info(ex);
			}
		}
	}

	/**
	 * Fires the event after open DBStorage.
	 * 
	 * @param pStorage the storage to use.
	 */
	protected void fireEventAfterOpenDBStorage(DBStorage pStorage)
	{
		if (eventAfterOpenDBStorage != null && eventAfterOpenDBStorage.isDispatchable())
		{
			try  
			{
				eventAfterOpenDBStorage.dispatchEvent(pStorage);
			} 
			catch (Throwable ex) 
			{
				info(ex);
			}
		}
	}

    /**
     * Registers a metadata cache.
     * This cache will be cleared with clearMetaData.
     * 
     * @param pCache the specific metadata cache.
     */
    public static void registerCache(GroupHashtable pCache)
    {
        if (!lRegisteredCaches.contains(pCache))
        {
            lRegisteredCaches.add(pCache);
        }
    }
	
	/**
	 * Gets the ForeignKey cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the ForeignKey cache for a given identifier.
	 */
	public static synchronized Hashtable<String, List<ForeignKey>> getForeignKeyCache(String pIdentifier)
	{
		return ghtFKsCache.get(pIdentifier);
	}
	
	/**
	 * Sets the ForeignKey cache for a given identifier.
	 * @param pIdentifier the identifier.
	 * @param pForeignKeyCache the ForeignKey cache for a given identifier.
	 */
	public static synchronized void setForeignKeyCache(String pIdentifier, Hashtable<String, List<ForeignKey>> pForeignKeyCache)
	{
		ghtFKsCache.put(pIdentifier, pForeignKeyCache);
	}
	
	/**
	 * Gets the PrimaryKey cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the PrimaryKey cache for a given identifier.
	 */
	public static synchronized Hashtable<String, Key> getPrimaryKeyCache(String pIdentifier)
	{
		return ghtPKCache.get(pIdentifier);
	}
	
	/**
	 * Sets the PrimaryKey cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pPrimaryKeyCache the PrimaryKey cache for a given identifier.
	 */
	public static synchronized void setPrimaryKeyCache(String pIdentifier, Hashtable<String, Key> pPrimaryKeyCache)
	{
		ghtPKCache.put(pIdentifier, pPrimaryKeyCache);
	}
	
	/**
	 * Gets the UniqueKey cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the UniqueKey cache for a given identifier.
	 */
	public static synchronized Hashtable<String, List<Key>> getUniqueKeyCache(String pIdentifier)
	{
		return ghtUKsCache.get(pIdentifier);
	}
	
	/**
	 * Sets the UniqueKey cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pUniqueKeyCache the UniqueKey cache for a given identifier.
	 */
	public static synchronized void setUniqueKeyCache(String pIdentifier, Hashtable<String, List<Key>> pUniqueKeyCache)
	{
		ghtUKsCache.put(pIdentifier, pUniqueKeyCache);
	}
	
	/**
	 * Gets the AllowedValues cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the AllowedValues cache for a given identifier.
	 */
	public static synchronized Hashtable<String, Map<String, Object[]>> getAllowedValuesCache(String pIdentifier)
	{
		return ghtAllowedValuesCache.get(pIdentifier);
	}
	
	/**
	 * Sets the AllowedValues cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pAllowedValuesCache the AllowedValues cache for a given identifier.
	 */
	public static synchronized void setAllowedValuesCache(String pIdentifier, Hashtable<String, Map<String, Object[]>> pAllowedValuesCache)
	{
		ghtAllowedValuesCache.put(pIdentifier, pAllowedValuesCache);
	}
	
	/**
	 * Gets the DefaultValues cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the DefaultValues cache for a given identifier.
	 */
	public static synchronized Hashtable<String, Map<String, Object>> getDefaultValuesCache(String pIdentifier)
	{
		return ghtDefaultValuesCache.get(pIdentifier);
	}
	
	/**
	 * Sets the DefaultValues cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pDefaultValuesCache the DefaultValues cache for a given identifier.
	 */
	public static synchronized void setDefaultValuesCache(String pIdentifier, Hashtable<String, Map<String, Object>> pDefaultValuesCache)
	{
		ghtDefaultValuesCache.put(pIdentifier, pDefaultValuesCache);
	}
	
	/**
	 * Gets the ColumnMetaData cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the ColumnMetaData cache for a given identifier.
	 */
	public static synchronized Hashtable<String, ServerColumnMetaData[]> getColumnMetaDataCache(String pIdentifier)
	{
		return ghtColumnMetaDataCache.get(pIdentifier);
	}
	
	/**
	 * Sets the ColumnMetaData cache for a given identifier.
	 * @param pIdentifier the identifier.
	 * @param pColumnMetaDataCache the ColumnMetaData cache for a given identifier.
	 */
	public static synchronized void setColumnMetaDataCache(String pIdentifier, Hashtable<String, ServerColumnMetaData[]> pColumnMetaDataCache)
	{
		ghtColumnMetaDataCache.put(pIdentifier, pColumnMetaDataCache);
	}
	
	/**
	 * Gets the TableInfo cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the TableInfo cache for a given identifier.
	 */
	public static synchronized Hashtable<String, TableInfo> getTableInfoCache(String pIdentifier)
	{
		return ghtTableInfoCache.get(pIdentifier);
	}
	
	/**
	 * Sets the TableInfo cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pTableInfoCache the TableInfo cache for a given identifier.
	 */
	public static synchronized void setTableInfoCache(String pIdentifier, Hashtable<String, TableInfo> pTableInfoCache)
	{
		ghtTableInfoCache.put(pIdentifier, pTableInfoCache);
	}
	
	/**
	 * Gets the TableName cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @return the TableName cache for a given identifier.
	 */
	public static synchronized Hashtable<String, String> getTableNameCache(String pIdentifier)
	{
		return ghtTableNameCache.get(pIdentifier);
	}
	
	/**
	 * Sets the TableName cache for a given identifier.
	 * 
	 * @param pIdentifier the identifier.
	 * @param pTableNameCache the TableNameC cache for a given identifier.
	 */
	public static synchronized void setTableNameCache(String pIdentifier, Hashtable<String, String> pTableNameCache)
	{
		ghtTableNameCache.put(pIdentifier, pTableNameCache);
	}
    
    /**
     * The default minimum meta data check interval.
     * A negative value means, that it will not be checked.
     * 
     * @return default minimum meta data check interval.
     */
    public static int getDefaultMinMetaDataCheckInterval()
    {
        return defaultMinMetaDataCheckInterval;
    }
    
    /**
     * The default minimum meta data check interval.
     * A negative value means, that it will not be checked.
     * 
     * @param pDefaultMinMetaDataCheckInterval default minimum meta data check interval.
     */
    public static void setDefaultMinMetaDataCheckInterval(int pDefaultMinMetaDataCheckInterval)
    {
        defaultMinMetaDataCheckInterval = pDefaultMinMetaDataCheckInterval;
    }
    
    /**
     * The minimum meta data check interval.
     * A negative value means, that it will not be checked.
     * 
     * @return minimum meta data check interval.
     */
    public int getMinMetaDataCheckInterval()
    {
        return minMetaDataCheckInterval;
    }
    
    /**
     * The minimum meta data check interval.
     * A negative value means, that it will not be checked.
     * 
     * @param pMinMetaDataCheckInterval minimum meta data check interval.
     */
    public void setMinMetaDataCheckInterval(int pMinMetaDataCheckInterval)
    {
        minMetaDataCheckInterval = pMinMetaDataCheckInterval;
    }
    
    /**
     * Gets the last meta data change in database.
     * Null means, that the database cannot detect the change date of table and views ddl.
     * 
     * @return the last meta data change in database, or <code>null</code>, if it cannot be detected.
     * @throws Exception if last metadata change access fails
     */
    protected Date getLastMetaDataChange() throws Exception
    {
        return null;
    }
    
    /**
     * If meta data is changed the cache will be cleared.
     * It checks only all defaultMinMetaDataCheckInterval seconds.
     */
    protected void checkMetaDataChange()
    {
        if (minMetaDataCheckInterval >= 0)
        {
            String identifier = getIdentifier();
            
            Long lastMetaDataCheck = ghtLastMetaDataCheck.get(identifier, "");
            long checkTime = System.currentTimeMillis();
            
            if (lastMetaDataCheck == null || checkTime > lastMetaDataCheck.longValue() + minMetaDataCheckInterval * 1000L)
            {
                ghtLastMetaDataCheck.put(identifier, "", Long.valueOf(checkTime));
                
                try
                {
                    Date lastChange = getLastMetaDataChange();
                    if (lastChange == null)
                    {
                        minMetaDataCheckInterval = -1; // Not supported, disable check generally
                    }
                    else
                    {
                        Date lastMetaDataChange = ghtLastMetaDataChange.get(identifier, "");
                                    
                        if (lastMetaDataChange == null)
                        {
                            ghtLastMetaDataChange.put(identifier, "", lastChange);
                        }
                        else if (lastChange.getTime() > lastMetaDataChange.getTime())
                        {
                            ghtLastMetaDataChange.put(identifier, "", lastChange);
    
                            clearOwnMetaData();
                        }
                    }
                }
                catch (Exception ex)
                {
                    minMetaDataCheckInterval = -1; // Not supported, disable check generally

                    debug(ex);
                }
            }
        }
    }
	
	/**
	 * Clears the meta data cache.
	 */
	public static synchronized void clearMetaData()
	{
		AbstractCachedStorage.clearMetaData();
		
		HashSet<String> allIdentifiers = new HashSet<String>();
		for (List<String> identifiers : kvlApplicationGroups.values())
		{
			allIdentifiers.addAll(identifiers);
		}
		
		kvlApplicationGroups.clear();
		
        for (String identifier : allIdentifiers)
        {
            List<DBAccess> dbAccesses = getDBAccessByIdentifier(identifier);
            
            for (DBAccess dbAccess : dbAccesses)
            {
                kvlApplicationGroups.put(dbAccess.getApplicationName(), identifier, true);
            }
        }
        
		for (GroupHashtable cache : lRegisteredCaches)
		{
		    cache.clear();
		}
		
		fireEventClearedMetaData(null, new ArrayList<String>(allIdentifiers));
	}
	
	/**
	 * Clears the meta data cache for the given application.
	 * 
	 * @param pApplicationName the application name
	 */
	public static synchronized void clearMetaData(String pApplicationName)
	{
        String sKey = CommonUtil.nvl(pApplicationName, "");
	    
		AbstractCachedStorage.clearMetaData(sKey);
	
		List<String> liIdentifier = kvlApplicationGroups.remove(sKey);
	
		if (liIdentifier != null)
		{
			for (String sIdentifier : liIdentifier)
			{
			    if (getDBAccessByIdentifier(sIdentifier).size() > 0)
			    {
                    kvlApplicationGroups.put(sKey, sIdentifier, true);
			    }

			    for (GroupHashtable cache : lRegisteredCaches)
			    {
			        cache.remove(sIdentifier);
			    }
			}
		}
		
		fireEventClearedMetaData(pApplicationName, liIdentifier);
	}

    /**
     * Clears the meta data cache for the given application.
     * 
     * @param pApplicationName the application name
     * @param pIdentifier the identifier
     */
    protected static synchronized void clearMetaData(String pApplicationName, String pIdentifier)
    {
        for (GroupHashtable cache : lRegisteredCaches)
        {
            cache.remove(pIdentifier);
        }
        
        ArrayList<String> liIdentifiers = new ArrayList<String>();
        liIdentifiers.add(pIdentifier);
        
        fireEventClearedMetaData(pApplicationName, liIdentifiers);
    }

	/**
	 * Clears the meta data cached from this DBAccess.
	 */
	public void clearOwnMetaData()
	{
	    clearMetaData(sApplicationName, getIdentifier());
	}
	
	/**
	 * Creates an identifier string from the given parameter.
	 * 
	 * @param pIdentifier the identifier
	 * @return the identifier string
	 */
	public static String createIdentifier(Object... pIdentifier)
	{
		return createIdentifier(false, pIdentifier);
	}
	
    /**
     * Creates an identifier string from the given parameter.
     * 
     * @param pIdentifier the identifier
     * @return the identifier string
     */
    public static String createIgnoreCaseIdentifier(Object... pIdentifier)
    {
        return createIdentifier(true, pIdentifier);
    }
    
    /**
     * Creates an identifier string from the given parameter.
     * 
     * @param pIgnoreCase true, if case should be ignored when not quoted.
     * @param pIdentifier the identifier
     * @return the identifier string
     */
    protected static String createIdentifier(boolean pIgnoreCase, Object... pIdentifier)
    {
        if (pIdentifier == null || pIdentifier.length == 0)
        {
            return "/";
        }

        StringBuilder identString = new StringBuilder(512);
            
        for (int i = 0; i < pIdentifier.length; i++)
        {
            identString.append('/');
            
            Object identifier = pIdentifier[i];
            
            if (identifier instanceof Object[])
            {
                Object[] oaIdentifiers = (Object[])identifier;
                
                for (int j = 0; j < oaIdentifiers.length; j++)
                {
                    if (j > 0)
                    {
                        identString.append(";");
                    }
                   
                    identString.append(toIdentifier(oaIdentifiers[j], pIgnoreCase));
                }
            }
            else 
            {
                identString.append(toIdentifier(identifier, pIgnoreCase));
            }
        }
        
        return identString.toString();
    }
    
    /**
     * Converts the object to a identifier string.
     * If pIgnoreCase is true, all not quoted parts will be upper case.
     *  
     * @param pObject the object
     * @param pIgnoreCase true, all not quoted parts will be upper case.
     * @return converted object
     */
    protected static String toIdentifier(Object pObject, boolean pIgnoreCase)
    {
        if (pObject == null)
        {
            return "";
        }
        String result = StringUtil.removeWhitespaces(pObject.toString());
        
        if (pIgnoreCase)
        {
            StringBuilder uppercase = new StringBuilder(result.length());
            
            boolean isQuoted = false;
            int startIndex = 0;
            int endIndex = result.indexOf(QUOTE);
            while (endIndex >= 0)
            {
                if (endIndex > 0 && result.charAt(endIndex - 1) == '\\')
                {
                    uppercase.append(isQuoted ? result.substring(startIndex, endIndex - 1) : result.substring(startIndex, endIndex - 1).toUpperCase());
                    uppercase.append(QUOTE);
                }
                else if (endIndex == 0 || result.charAt(endIndex - 1) != '\\')
                {
                    uppercase.append(isQuoted ? result.substring(startIndex, endIndex) : result.substring(startIndex, endIndex).toUpperCase());
                    
                    isQuoted = !isQuoted;
                }
                startIndex = endIndex + QUOTE.length();
                endIndex = result.indexOf(QUOTE, startIndex);
            }
            uppercase.append(isQuoted ? result.substring(startIndex) : result.substring(startIndex).toUpperCase());
            
            return uppercase.toString();
        }
        else
        {
            return result;
        }
    }
    
	/**
	 * Sets the metadata cache option for this instance. If a session cache option is set, the
	 * instance cache option overrules the session setting.
	 * 
	 * @param pOption the metadata cache option 
	 * @see #isMetaDataCacheEnabled()
	 */
	public void setMetaDataCacheOption(MetaDataCacheOption pOption)
	{
		if (pOption == null)
		{
			mdcCacheOption = MetaDataCacheOption.Default;
		}
		else
		{
			mdcCacheOption = pOption;
		}
	}
	
	/**
	 * Gets the metadata cache option for this instance.
	 * 
	 * @return the metadata cache option
	 * @see #setMetaDataCacheOption(MetaDataCacheOption)
	 */
	public MetaDataCacheOption getMetaDataCacheOption()
	{
		return mdcCacheOption;
	}

	/**
	 * Gets whether the meta data cache should be used.
	 * 
	 * @return whether the meta data cache should be used.
	 */
	protected boolean isMetaDataCacheEnabled()
	{
		//switch from Cache-ON to Cache-OFF -> clear cache
		if (lastGlobalMetaDataCache && !DBStorage.isGlobalMetaDataCacheEnabled())
		{
			clearMetaData(getApplicationName());
		}
		lastGlobalMetaDataCache = DBStorage.isGlobalMetaDataCacheEnabled();

		MetaDataCacheOption option;
		
		//instance option overrules session option!
		
		if (mdcCacheOption != MetaDataCacheOption.Default)
		{
			option = mdcCacheOption;
		}
		else
		{
	        //get the cache option from the session
	        ISession sess = SessionContext.getCurrentSession();
	        
	        if (sess != null)
	        {
	            Object oOption = (String)sess.getProperty(IConnectionConstants.METADATA_CACHEOPTION);
	            
	            if (oOption == null)
	            {
	                option = MetaDataCacheOption.Default;
	            }
	            else
	            {
	                option = MetaDataCacheOption.resolve((String)oOption);
	            }
	        }
	        else
	        {
	            option = MetaDataCacheOption.Default;
	        }
		}
		
		return option == MetaDataCacheOption.On || (DBStorage.isGlobalMetaDataCacheEnabled() && option == MetaDataCacheOption.Default);		
	}
	
	/**
	 * Prepares the given {@link Connection} for being used by this
	 * {@link DBAccess}.
	 * 
	 * @param pConnection the {@link Connection} to configure.
	 * @throws SQLException if the configuring the {@link Connection} fails.
	 */
	protected void prepareConnection(Connection pConnection) throws SQLException
	{
		// #515
		if (pConnection.getTransactionIsolation() != Connection.TRANSACTION_READ_COMMITTED)
		{
			pConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		}
	}
	
	/**
	 * Sets modified state of the connection. The connection should be set modified if insert/update or delete was executed.
	 * If the database connection is in auto-commit mode, it's not possible to set the modified flag.
	 * 
	 * @param pModified <code>true</code> to set the connection modified, <code>false</code> to set it not modified
	 */
	protected void setModified(Boolean pModified)
	{
		if (isAutoCommit())
		{
			bModified = Boolean.FALSE;
		}
		else
		{
			bModified = pModified;
		}
	}
	
	/**
	 * Gets whether the database access is modified.
	 * 
	 * @return <code>true</code> if modified, <code>false</code> otherwise
	 */
	public boolean isModified()
	{
		if (bModified == null)
		{
			if (connection == null)
			{
				bModified = Boolean.FALSE;
			}
			else
			{
				if (detectModified())
				{
					bModified = Boolean.TRUE;
				}
				else
				{
					return false;
				}
			}
		}

		return bModified.booleanValue();
	}
	
	/**
	 * Detects if a transaction is open directly in the database.
	 * 
	 * @return true, if a transaction is open directly in the database.
	 */
	protected boolean detectModified()
	{
		return true;
	}
	
	/**
	 * Gets whether the given log level is enabled.
	 * 
	 * @param pLevel the level to check
	 * @return <code>true</code> if the log level is enabled, <code>false</code> otherwise
	 */
	protected static boolean isLogEnabled(LogLevel pLevel)
	{
	    if (logger == null)
	    {
	    	logger = LoggerFactory.getInstance(DBAccess.class);
	    }
	    
	    return logger.isEnabled(pLevel);
	}
	
    /**
     * Logs debug information.
     * 
     * @param pInfo the debug information
     */
    protected static void debug(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(DBAccess.class);
        }
        
        logger.debug(pInfo);
    }

    /**
     * Logs information.
     * 
     * @param pInfo the information
     */
    protected static void info(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(DBAccess.class);
        }
        
        logger.info(pInfo);
    }
    
    /**
     * Logs error information.
     * 
     * @param pInfo the error information
     */
    protected static void error(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(DBAccess.class);
        }

        logger.error(pInfo);
    }   
	
    /**
     * Gets whether the given URL is a JDBC resource.
     * 
     * @param pUrl the URL
     * @return <code>true</code> if given URL is a jdbc resource
     */
    public static boolean isJdbc(String pUrl)
    {
        return pUrl == null || pUrl.toLowerCase().startsWith("jdbc:");        
    }
	
	/**
	 * Finds all named parameters in the given statement.
	 * 
	 * @param pStatement the statement.
	 * @return the found named parameters.
	 */
    public List<String> findNamedParameters(String pStatement)
    {
    	ArrayUtil<String> liParams = new ArrayUtil<String>();
    	
    	findNamedParameters(pStatement, liParams, false);
    	
    	return liParams;
    }
    
	/**
	 * Finds all named parameters in the given statement and adds the names to
	 * the given {@link List}.
	 * 
	 * @param pStatement the statement.
	 * @param pNameList the {@link List} to add the names to.
	 * @param pUpperCase whether the parameter names should be upper case.
	 */
	protected void findNamedParameters(String pStatement, List<String> pNameList, boolean pUpperCase)
	{
		if (StringUtil.isEmpty(pStatement))
		{
			return;
		}
		
		String sOpenQu = getOpenQuoteCharacter();
		
		if (StringUtil.isEmpty(sOpenQu))
		{
			sOpenQu = null;
		}
		
		String sCloseQu = getCloseQuoteCharacter();
		
		if (StringUtil.isEmpty(sCloseQu))
		{
			sCloseQu = null;
		}
		
		String sCloseMark = null;
		
		for (int idx = 0, cnt = pStatement.length(); idx < cnt; idx++)
		{
			if (sCloseMark != null)
			{
				if (pStatement.startsWith(sCloseMark, idx))
				{
					sCloseMark = null;
				}
			}
			else
			{
				if (pStatement.startsWith("\'", idx))
				{
					sCloseMark = "\'";
				}
				else if (sOpenQu != null && pStatement.startsWith(sOpenQu, idx))
				{
					sCloseMark = sCloseQu;
				}
				else if (pStatement.startsWith("--", idx))
				{
					sCloseMark = "\n";
				}
				else if (pStatement.startsWith("/*", idx))
				{
					sCloseMark = "*/";
				}
				else if (pStatement.startsWith(":", idx))
				{
					NamedParameterDefinition npd = findNamedParameterDefinition(pStatement, idx + 1, pUpperCase);
					
					if (npd.valid)
					{
						pNameList.add(npd.name);
					}
					
					idx = npd.end - 1;
				}
			}
		}
	}

	/**
	 * Replaces null parameters (?) directly with null, to avoid cast exceptions.
	 *  
	 * @param pStatement the original statement
	 * @param pParameters the parameters
	 * @return the new reduced parameters
	 */
	protected Object[] replaceNullParameter(StringBuilder pStatement, Object[] pParameters)
	{
        int[] paramIndexes = getParameterIndexes(pStatement.toString());
        
        for (int i = paramIndexes.length - 1; i >= 0; i--)
        {
            Object param = pParameters[i];
            
            if (AbstractParam.getValue(param) == null)
            {
                int index = paramIndexes[i];

                if (index < pStatement.length() - 1 && !Character.isWhitespace(pStatement.charAt(index + 1)))
                {
                    pStatement.insert(index + 1, " ");
                }
                pStatement.replace(index, index + 1, "null");
                if (index > 0 && !Character.isWhitespace(pStatement.charAt(index - 1)))
                {
                    pStatement.insert(index, " ");
                }
                pParameters = ArrayUtil.remove(pParameters, i);
            }
        }

        return pParameters;
	}
	
    /**
     * Gets the indexes of parameters (?).
     *  
     * @param pStatement the statement
     * @return the indexes of parameters (?)
     */
    protected int[] getParameterIndexes(String pStatement)
    {
        int[] result = new int[0];
        
        String sOpenQu = getOpenQuoteCharacter();
        String sCloseQu = getCloseQuoteCharacter();
        if (StringUtil.isEmpty(sOpenQu) || StringUtil.isEmpty(sCloseQu))
        {
            sOpenQu = null;
            sCloseQu = null;
        }
        
        String sCloseMark = null;
        
        for (int idx = 0, cnt = pStatement.length(); idx < cnt; idx++)
        {
            if (sCloseMark != null)
            {
                if (pStatement.startsWith(sCloseMark, idx))
                {
                    idx += sCloseMark.length() - 1;
                    sCloseMark = null;
                }
            }
            else
            {
                if (pStatement.startsWith("\'", idx))
                {
                    sCloseMark = "\'";
                }
                else if (pStatement.startsWith("\"", idx))
                {
                    sCloseMark = "\"";
                }
                else if (sOpenQu != null && pStatement.startsWith(sOpenQu, idx))
                {
                    idx += sOpenQu.length() - 1;
                    sCloseMark = sCloseQu;
                }
                else if (pStatement.startsWith("--", idx))
                {
                    idx++;
                    sCloseMark = "\n";
                }
                else if (pStatement.startsWith("/*", idx))
                {
                    idx++;
                    sCloseMark = "*/";
                }
                else if (pStatement.startsWith("?", idx))
                {
                    result = ArrayUtil.add(result, idx);
                }
            }
        }
        
        return result;
    }   
	
	/**
	 * Replaces the first occurence of a named parameter with a specific value.
	 *  
	 * @param pStatement the statement
	 * @param pCondition the compare conditions per named parameter
	 * @param pServerMetaData the server meta data
	 * @param pUpperCase whether the parameter names should be upper case
	 * @return the statement with the replaced value or the original statement if nothing was changed
	 */
	protected String replaceNamedParameters(String pStatement, Map<String, CompareCondition> pCondition, ServerMetaData pServerMetaData, boolean pUpperCase)
	{
		if (StringUtil.isEmpty(pStatement))
		{
			return null;
		}
		
		String sOpenQu = getOpenQuoteCharacter();
        String sCloseQu = getCloseQuoteCharacter();
		if (StringUtil.isEmpty(sOpenQu) || StringUtil.isEmpty(sCloseQu))
		{
            sOpenQu = null;
			sCloseQu = null;
		}
		
		String sCloseMark = null;
		
		StringBuilder sbStatement = new StringBuilder();
		
		int iStart = 0;
		
		for (int idx = 0, cnt = pStatement.length(); idx < cnt; idx++)
		{
			if (sCloseMark != null)
			{
				if (pStatement.startsWith(sCloseMark, idx))
				{
				    idx += sCloseMark.length() - 1;
					sCloseMark = null;
				}
			}
			else
			{
				if (pStatement.startsWith("\'", idx))
				{
					sCloseMark = "\'";
				}
				else if (pStatement.startsWith("\"", idx))
                {
                    sCloseMark = "\"";
                }
                else if (sOpenQu != null && pStatement.startsWith(sOpenQu, idx))
				{
				    idx += sOpenQu.length() - 1;
					sCloseMark = sCloseQu;
				}
				else if (pStatement.startsWith("--", idx))
				{
				    idx++;
					sCloseMark = "\n";
				}
				else if (pStatement.startsWith("/*", idx))
				{
                    idx++;
					sCloseMark = "*/";
				}
				else if (pStatement.startsWith(":", idx))
				{
					NamedParameterDefinition npd = findNamedParameterDefinition(pStatement, idx + 1, pUpperCase);
					
					if (npd.valid)
					{
						//if not found -> null will be returned and parameter will be replaced with null!!
						CompareCondition condition = pCondition.get(npd.name);
						
						sbStatement.append(pStatement.substring(iStart, idx));
						sbStatement.append(createWhereParam(pServerMetaData, condition));
						
						iStart = npd.end;
					}
					
					idx = npd.end - 1;
				}
			}
		}
		
		if (iStart < pStatement.length() - 1)
		{
			sbStatement.append(pStatement.substring(iStart));
		}
		
		return sbStatement.toString();
	}	

	/**
	 * Finds the named parameter, if parameter is a named parameter.
	 * 
	 * @param pStatement the statement
	 * @param pStart the parameter start position
	 * @param pUpperCase whether detected parameter should be upper case
	 * @return the named parameter end position of named parameter or <code>pPos</code> if parameter isn't a named parameter
	 */
	protected NamedParameterDefinition findNamedParameterDefinition(String pStatement, int pStart, boolean pUpperCase)
	{
		int iEnd = pStart;
		for (int iLength = pStatement.length(); iEnd < iLength; iEnd++)
		{
		    char ch = pStatement.charAt(iEnd);
		    
		    if (!Character.isJavaIdentifierPart(ch) && ch != '.' && ch != '#')
		    {
		        break;
		    }
		}

		NamedParameterDefinition result = new NamedParameterDefinition();
		result.start = pStart;
		result.end = iEnd;
		result.valid = iEnd > pStart;

		if (result.valid)
		{
			result.name = pStatement.substring(result.start, result.end);
			
			if (pUpperCase)
			{
				result.name = result.name.toUpperCase();
			}
		}
		
		return result;
	}
	
    /**
     * Gets the column name from the given resultset metadata.
     * 
     * @param pMetaData the metadata
     * @param pColumn the column index
     * @return the column name
     * @throws SQLException if accessing metadata failed
     */
    protected String getColumnName(ResultSetMetaData pMetaData, int pColumn) throws SQLException
    {
    	return pMetaData.getColumnName(pColumn);
    }
    
	/**
	 * Gets how many rows should be discarded based on the given values.
	 * 
	 * @param pFromRow the row index from to fetch
	 * @param pMinimumRowCount the minimum count row to fetch
	 * @return the amount of rows to discard.
	 */
	protected int getDiscardRowCount(int pFromRow, int pMinimumRowCount)
	{
		return pFromRow;
	}
	
	/**
	 * Gets all values from the given {@link ICondition} and adds it to
	 * the given {@link List}.
	 * <p>
	 * Note that this method is {@code private} because it is subject to change
	 * in a future version.
	 *  
	 * @param pCondition the {@link ICondition} from which to get the values.
	 * @param pValueList the {@link List} to which the values are added.
	 * @param pNameList the {@link List} to which the names are added.
	 * @param pNameToConditionMap the {@link Map} which maps the names to {@link CompareCondition}s.
	 */
	private void getParameter(ICondition pCondition,
			                  List<Object> pValueList,
			                  List<String> pNameList,
			                  Map<String, CompareCondition> pNameToConditionMap)
	{
		if (pCondition instanceof CompareCondition)
		{
			CompareCondition cCompare = (CompareCondition)pCondition;
			Object           oValue   = cCompare.getValue();
	
            if (oValue != null)
			{
                pNameList.add(cCompare.getColumnName());
                pNameToConditionMap.put(cCompare.getColumnName(), cCompare);

                if ((cCompare instanceof Like || cCompare instanceof LikeIgnoreCase)
					&& oValue instanceof String)
				{
					pValueList.add(((String)oValue).replace('*', '%').replace('?', '_'));
				}						
				else 
				{
					pValueList.add(oValue);
				}
			}
		}
		else if (pCondition instanceof OperatorCondition)
		{
			OperatorCondition cOperator = (OperatorCondition)pCondition;
			ICondition[] caConditions = cOperator.getConditions();
			
			for (int i = 0; i < caConditions.length; i++)
			{
				getParameter(caConditions[i], pValueList, pNameList, pNameToConditionMap);
			}
		}
		else if (pCondition instanceof Not)
		{
			getParameter(((Not)pCondition).getCondition(), pValueList, pNameList, pNameToConditionMap);
		}
	}
	
	/**
	 * Finds all named parameters in the given statements and adds the names to
	 * the given {@link List}.
	 * <p>
	 * Note that this function is subject to be changed in a future version,
	 * and can there for not be considered stable.
	 * 
	 * @param pStatements the statements.
	 * @param pNameList the {@link List} to add the names to.
	 * @param pUpperCase whether the parameter names should be upper-case        
	 */
	private void findNamedParameters(String[] pStatements, List<String> pNameList, boolean pUpperCase)
	{
		if (pStatements != null)
		{
			for (int index = 0; index < pStatements.length; index++)
			{
				findNamedParameters(pStatements[index], pNameList, pUpperCase);
			}
		}
	}
	
	/**
	 * Adds simple quotes to string parameters. The quote character is independent of DB quote characters.
	 * 
	 * @param pParameter the parameter list
	 * @return the quoted parameter list or <code>pParameter</code> if no string parameters were found
	 */
	private Object[] formatParameter(Object[] pParameter)
	{
	    if (pParameter != null)
	    {
	        Object[] oResult = null;
	        
	        for (int i = 0; i < pParameter.length; i++)
	        {
	            if (pParameter[i] instanceof String)
	            {
	                if (oResult == null)
	                {
	                    oResult = pParameter.clone();
	                }
	                
	                oResult[i] = "'" + pParameter[i] + "'";
	            }
	        }

	        if (oResult != null)
	        {
	            return oResult;
	        }
	    }
	    
	    return pParameter;
	}
	
	/**
	 * Gets the where clause for metadata detection.
	 * 
	 * @return the where clause
	 */
	protected String getMetaDataWhereClause()
	{
	    return "1=2";
	}
	
	/**
	 * Sets whether the connection should be automatically re-opened if a connection close is detected.
	 * 
	 * @param pAutoReOpen <code>true</code> to automatically re-open the connection, <code>false</code> to close it
	 *                    and don't open it again
	 */
	public void setAutoReOpen(boolean pAutoReOpen)
	{
		bAutoReOpen = pAutoReOpen;
	}
	
	/**
	 * Gets whether a closed connection should be re-opened automatically.
	 * 
	 * @return <code>true</code> if the connection will be automatically re-opened, <code>false</code> otherwise
	 * @see #setAutoReOpen(boolean)
	 */
	public boolean isAutoReOpen()
	{
		return bAutoReOpen;
	}
	
	/**
	 * Gets the query to check if the connection is alive.
	 * 
	 * @return the query
	 */
	protected String getAliveQuery()
    {
        return "select 1";
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ParameterizedStatement} is a simple, mutable container for
	 * a statement and its values.
	 * <p>
	 * Note that this is implementation, class and mechanism is subject to
	 * change in a future version. For its stability can and will not be
	 * guaranteed in any way.
	 *  
	 * @author Robert Zenz
	 */
	public static class ParameterizedStatement
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link List values}. */
		private List<String> names = null;
		
		/** The {@link Map} of names to {@link CompareCondition}s. */
		private Map<String, CompareCondition> nameToCondition = null;
		
		/** The statement. */
		private String statement = null;
		
		/** The values of the parameters. */
		private List<Object> values = null;
		
		/** The values of the parameters as array. */
		private Object[] valuesAsArray = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ParameterizedStatement}.
		 *
		 * @param pStatement the {@link String statement}.
		 * @param pNames the {@link List} of names.
		 * @param pValues the {@link List values}.
		 * @param pNameToCondition the {@link Map} of names to {@link CompareCondition}s.
		 */
		public ParameterizedStatement(String pStatement,
				                      List<String> pNames,
				                      List<Object> pValues,
				                      Map<String, CompareCondition> pNameToCondition)
		{
			statement = pStatement;
			names = pNames;
			values = pValues;
			nameToCondition = pNameToCondition;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link List names}.
		 *
		 * @return the {@link List names}.
		 */
		public List<String> getNames()
		{
			return names;
		}

		/**
		 * Gets the {@link Map name to condition}.
		 *
		 * @return the {@link Map name to condition}.
		 */
		public Map<String, CompareCondition> getNameToCondition()
		{
			return nameToCondition;
		}
		
		/**
		 * Gets the statement.
		 *
		 * @return the statement.
		 */
		public String getStatement()
		{
			return statement;
		}

		/**
		 * Gets the {@link List values}.
		 *
		 * @return the {@link List values}.
		 */
		public List<Object> getValues()
		{
			return values;
		}
		
		/**
		 * Gets the {@link #getValues()} as array.
		 * <p>
		 * Note that this method "bakes" the array, meaning that it is craeted
		 * only once on the first call.
		 *
		 * @return the {@link #getValues()} as array.
		 */
		public Object[] getValuesAsArray()
		{
			if (valuesAsArray == null)
			{
				valuesAsArray = values.toArray(new Object[values.size()]);
			}
			
			return valuesAsArray;
		}
		
		/**
		 * Sets the statement.
		 *
		 * @param pStatement the statement.
		 */
		public void setStatement(String pStatement)
		{
			statement = pStatement;
		}
		
		/**
		 * Sets the values of the parameters.
		 *
		 * @param pValues the values of the parameters.
		 */
		public void setValues(List<Object> pValues)
		{
			values = pValues;
		}
		
	}	// ParameterizedStatement
	
	/**
     * The {@link ILazyFetchBlob} forces the fetch method to load the Blob anyway lazy. 
     * 
     * @author Martin Handsteiner
     */
    public static interface ILazyFetchBlob extends Blob
	{
	}
	
    /**
     * The {@link BlobFileHandle} is a simple {@link IFileHandle} implementation
     * that stores a {@link Blob} and allows to retrieve it again. 
     * 
     * @author Robert Zenz
     */
	public static final class BlobFileHandle implements IFileHandle, 
	                                                    IValidatable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link Blob}. */
		private DBAccess dBAccess;
		
		/** The statement. */
		private String statement;

		/** The parameter. */
		private Object[] parameter;

		/** The columnName. */
		private String columnName;

		/** The length. */
		private long length;

		/** The last access time. */
		private long   		iLastAccessTime;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link BlobFileHandle}.
		 * 
		 * @param pDBAccess the dBAccess.
		 * @param pStatement the statement.
		 * @param pParameter the parameter.
		 * @param pColumnName the column name.
		 * @param pLength the length.
		 */
		public BlobFileHandle(DBAccess pDBAccess, String pStatement, Object[] pParameter, String pColumnName, long pLength)
		{
			dBAccess = pDBAccess;
			statement = pStatement;
			parameter = pParameter;
			columnName = pColumnName;
			length = pLength;
			
			iLastAccessTime = System.currentTimeMillis();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public String getFileName()
		{
			return columnName;
		}

		/**
		 * {@inheritDoc}
		 */
		public InputStream getInputStream() throws IOException
		{
			iLastAccessTime = System.currentTimeMillis();
			
			PreparedStatement prepStat = null;
			ResultSet res = null;
			
			try
			{
				DBAccess.debug(statement, parameter);
				
				prepStat = dBAccess.getPreparedStatement(statement);
				for (int i = 0; i < parameter.length; i++)
				{
					prepStat.setObject(i + 1, parameter[i]);
				}
				
				dBAccess.setSavepoint();
				
				res = prepStat.executeQuery();
				if (res.next())
				{
				    Blob blob = (Blob)dBAccess.getObjectFromResultSet(res, 1);
				    
				    length = blob.length();
				    
					return blob.getBinaryStream();
				}
			}
			catch (Exception e)
			{
				dBAccess.rollbackToSavepoint();
				
				if (e instanceof IOException)
				{
				    throw (IOException)e;
				}
				else
				{
				    throw new IOException(e.getMessage(), e);
				}
			}
			finally
			{
				dBAccess.releaseSavepoint();
				CommonUtil.close(prepStat, res);
				
	            if (SessionContext.getCurrentInstance() == null)
	            {
    				if (dBAccess.isConnectionPoolEnabled() && !dBAccess.isModified())
                    {
                        try 
                        {
                            dBAccess.releaseConnection();
                        } 
                        catch (DataSourceException e) 
                        {
                            debug(e);
                        }
                    }
	            }
			}
			throw new IOException("Row not found, fetch Blob failed!");
		}

		/**
		 * {@inheritDoc}
		 */
		public long getLength() throws IOException
		{
			return length;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isValid()
		{
			try
			{
				long deadLine = System.currentTimeMillis() - DBAccess.getDefaultCursorCacheTimeout();

				return dBAccess.isOpen(false) && iLastAccessTime >= deadLine;
			}
			catch (Exception e)
			{
				// Ignore the exception.
			}
			return false;
		}
	}	// BlobFileHandle

	/**
	 * It stores all relevant information of the cached ResultSet for the fetch(...).
	 *  
	 * @author Martin Handsteiner
	 */
	private static final class Cursor implements ICloseable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The statement. */
		private PreparedStatement	statement;
		
		/** The result set. */
		private ResultSet	rsResultSet;
		
		/** The last fetched row. */
		private int 		iExecutionTime;

		/** The select statement. */
		private String		sSelectStatement;  

		/** The parameter. */
		private Object[]	oaParameter;  
		
		/** The last row. */
		private int    		iLastRow;
		
		/** The last access time. */
		private long   		iLastAccessTime;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Inits the Select cache object.
		 * 
		 * @param pStatement		The statement.
		 * @param pResultSet		The result set.
		 * @param pSelectStatement	The select statement.
		 * @param pParameter		The parameter.
		 * @param pExecutionTime    The execution time
		 */
		void init(PreparedStatement pStatement, ResultSet pResultSet, String pSelectStatement, Object[] pParameter, int pExecutionTime)
		{
			statement = pStatement;
			rsResultSet = pResultSet;
			sSelectStatement = pSelectStatement;
			oaParameter = pParameter;	
			iExecutionTime = pExecutionTime;
		}
		
		/**
		 * Checks, if the Cursor is usable.
		 * @param pSelectStatement the statement
		 * @param pParameter the parameter
		 * @param pFromRow the from row
		 * @return true, if th ecursor is useable.
		 */
		private boolean isUseable(String pSelectStatement, Object[] pParameter, int pFromRow)
		{
			if (sSelectStatement.equals(pSelectStatement)
				&& iLastRow == pFromRow)
			{
				return Arrays.equals(oaParameter, pParameter);
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void close()
		{
			CommonUtil.close(rsResultSet, statement);
		}
		
	}	// Cursor

	/**
	 * Observes the cursors for discarding.
	 * 
	 * @author Martin Handsteiner
	 */
	private static class DBAccessObserver implements IValidatable
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
		/** the db access to observe. */
		private WeakReference<DBAccess> dBAccessToObserve;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>DBAccessObserver</code>.
		 * 
		 * @param pDBAccess the db access to observe.
		 */
		public DBAccessObserver(DBAccess pDBAccess)
		{
			dBAccessToObserve = new WeakReference(pDBAccess);
		}
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isValid()
		{
			try
			{
				DBAccess dBAccess = dBAccessToObserve.get();
				
				if (dBAccess != null)
				{
					long deadLine = System.currentTimeMillis() - DBAccess.getDefaultCursorCacheTimeout();
					
					synchronized (dBAccess.htFetchResultSetCache)
					{
						for (Map.Entry<String, Cursor> entry : dBAccess.htFetchResultSetCache.entrySet())
						{
							Cursor cursor = entry.getValue();
							if (cursor.iLastAccessTime < deadLine)
							{
								cursor.close();
								dBAccess.htFetchResultSetCache.remove(entry.getKey());
							}
						}
					}
					
					return true;
				}
			}
			catch (Exception e)
			{
				// Ignore the exception.
			}
			
			return false;
		}
		
	}  // DBAccessObserver
	
	/**
	 * The <code>NamedParameterDefinition</code> is a simple datatype for collecting 
	 * named parameter definition.
	 * 
	 * @author Ren� Jahn
	 */
	protected static class NamedParameterDefinition
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the name. */
		protected String name;
		
		/** the start index. */
		protected int start;
		/** the end index. */
		protected int end;
		
		/** whether parameter is valid. */
		protected boolean valid = true;
		
	}	// NamedParameterIndex
	
} 	// DBAccess
