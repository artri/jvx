Changelog: JVx 2.2

##################################################################
#                          General                               #
##################################################################

https://oss.sibvisions.com

- New Features
- Bugfixes
- Other

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #1313: Optimize get(Server)ColumnMetaDataIndex
  - #1294: Add access to creation of ColumnDefinition
  - #1292: Better Lambda support
  - #1283: Remember server startup time
  - #1280: Introduced UnauthorizedException
  - #1279: Add convenience methods to EventHandler to check if 
           events should be dispatched
  - #1278: Removed static en/disable methods (timeout, alive)
  - #1272: Set default a maximum size like UITable
  - #1266: LongDataType
  - #1256: CSV export shouldn't fetch all columns in any case
  - #1255: DBAccess with custom property instance may fail
  - #1254: Use client or default charset for CSV creation
  - #1253: Remove IFlowLayout.get/isSquareComponents as it is not 
           implemented in any technology.
  - #1251: javax.rad.model: performance tuning of mem implementation
           AbstractMemStorage tuning (disable events)
  - #1249: Defaultvalues aren't as expected for AbstractMemStorage
  - #1241: Ignore Foreign key if overlapping manual Reference is set
  - #1238: Record should contain Exception property
  - #1231: LoggerFactory initialization should use real instance
  - #1230: OracleDBAccess: TIMESTAMP WITH TIMEZONE and 
           WITH LOCAL TIMEZONE not supported
  - #1229: En-/Disable server plugins
  - #1228: Listener for IDataSource
  - #1225: Listener for single connection properties
  - #1223: Log4j(2) support
  - #1219: New chart style: Pie
  - #1216: Accessible, NotAccessible annotation and support in 
           object provider
  - #1213: Refactoring Image cache (clearing technology image cache)
  - #1194: Remove MemDataBook logic from executeInsert, 
           executeDelete, ...
  - #1103: use the short name if full qualified
  - #987:  Test cases for lazy-fetching BLOB columns
  - #965:  Support @PostConstruct and @PreDestroy standard annotations
  - #943:  Insert/Update operations on view as writebacktable
  - #906:  Introduce timeout for prepared statement cache
  - NonClosingOutputStream introduced
  - Introduced parameter/object value changed listener in UIResource
  - Reflective.getMethodsByReturnValue introduced
  - CommonUtil.close support for BLOB and CLOB
  - StorageDataBook introduced
  - ProxyUtil introduced
  - XmlWorker got static methods for reading XML files
  - ImageUtil.save added
  - Hana FK, UK, Default value detection
  - JNDI support in DBAccess improved
  - Jackson (RESTlet) configuration updates
  - RESTlet mixins (security checks)
  - SessionCancelException introduced
  - IDownloader and IUploader introduced
  - TransferContext introduced (up/download)
  - TransferContext support in HttpConnection
  - introduced ByteCountInput/OutputStream, ShadowCopyInputStream
  - AbstractSerializedConnection now clears the streams 
    (gzip compression not streamable)
  - server now also clears the streams (gzip compression 
    not streamable)
  - changed FileHandle to use TransferContext
  - changed HttpConnection to support upload
  - removed URL handling from RemoteFileHandle
  - FileHandle transfer support
  - Server now handles closed input streams
  - BinaryDataType now supports InputStream and InputStreamReader
  - changed streaming server to send magic byte instead of stream length 
    (reduces byte[] creation)
  - introduced server identifier/instance key    
  - FileUtil.delete stopAfterError support
  - Record categories
  - NoSecurityManager introduced
  - load <application_name_lowercase>.xml from classpath

[BUG]

  - #1315: reload(), fetchAll() after setFilter()
  - #1314: FileHandle doesn't work correct with byte[] content
  - #1312: Ensure, that last notifyRepaint is sent, not first
  - #1293: UITabsetPanel does actually move the tab if it is 
           dragged/dropped (it shouldn't)
  - #1290: CellRenderer only works, if column is set
  - #1289: lockAndRefetch does not work after first update
  - #1287: repaint loop due to prevention of fetch, when master is 
           inserting
  - #1286: CCE String (BigDecimal)
  - #1285: NPE on setValues with not extended storage
  - #1284: Proper handling of RootDataBook and UIDs, remove datapage and 
           fetch uid details
  - #1282: correct selected row and restore does not always work on 
           sort, filter, reload
  - #1281: setFilter/ setSort causes to early or too often fetch
  - #1277: setValueIntern needs columndefinition
  - #1276: Nullable flag should not be changed on client
  - #1273: Temporary current data page is not cleared on close
  - #1267: Very slow unnecessary type detection
  - #1264: refetch sends all columns to the server
  - #1247: saveAllRows() after setFilter(null)
  - #1246: BLOB cache generates malformed URL
  - #1240: Bugfix and optimize RemoteFileHandle for Blob columns
  - #1239: Class cast exception if not a RemoteFileHandle
  - #1235: Ensure that only one statement is cached per storage 
           configuration
  - #1234: ORA-24816 if varchar2>1000 is after 
           lob column in32UTF8
  - #1233: NPE if encryption is removed and liEncryptedNodes is null
  - #1227: metadata cache doesn't working properly with temp-disabled 
           sessions
  - #1226: IDataBook.searchNext(ICondition) returns wrong index if the 
           additional row is visible
  - #1224: Server doesn't read logfactory correctly
  - #1218: possible NPE fixed (multi threading with different UI 
                               technologies)
  - #1220: Memory leak in JVxUtil#getIcon
  - #1217: removed object id from AbstractSession (used session id)
  - #1139: UISplitPane.setBackground(null) not consistent in SwingUI
  - #1171: JVxCalendarPane.setLocale() doesn't work on month names
  - #974:  REVERTED

  
  
[OTHER]  
  - Update to RESTlet 2.2.3
  - Update to oracle 12c JDBC driver
  - Requirement: JDK 1.6
  - HSQLDB update to 2.3.2
  - Set ByteSerializer deprecated
