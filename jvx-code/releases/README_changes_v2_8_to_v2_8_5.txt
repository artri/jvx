Changelog: JVx 2.8.5

##################################################################
#                          General                               #
##################################################################

https://oss.sibvisions.com

- New Features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

[NEW]

    #1922: RESTAdapter: support for public zone
    #1928: CallBackBroker should support sending to master session
    #1971: Register custom admin service command handlers
    #1972: JVxDateCellEditor: transfer focus to next component when date is selected 
           in popup
    #1981: Setup doesn't recognize classloader
    #1985: Storage put additional objects
    #1986: findNamedParameters in DBStorage is fixed toUpperCase
    #1990: SessionContext: initial method and object name
    #1992: GenericBean with Inherit annotation support
    #1996: ICallBackBroker refactoring
    #2000: Define ToolTips on table cells
    #2006: ILinkedCellEditor: add Feature doNotClearColumns
    #2009: EventHandler: add compatibility in searching methods
    #2021: PostgreSQLDBAccess: add support for check constraints
    #2023: change log levels of DefaultSessionManager
    #2035: Accessible/NotAccessible per environment
    #2036: REST: disable admin serivces
    #2037: DBStorage: include restrict to primary key filter in refetch statement
    #2041: Keep event resource
    #2042: XmlSecurityManager should support authkey
    #2046: DBAccess events
    #2050: DataRow putObject support
    #2055: Security domain
    #2061: Setting a custom Bean class on the BeanConverter.
    #2126: AbstractStyledCellEditor: improve placeholder visible logic
    #2173: UIDynamicCellEditor: support for different cell editors in same column for 
           different rows
    #2174: Better Logging API configuration
    #2176: IDataType: new functionality convertToUnifiedString 
    #2212: UITable: setting rowHeight has no immediate effect
    #2239: Support custom Bean classes to be serialized
    #2285: Filter: add support for creating an extended full text filter
    #2287: Decouple swingx dependency
    #2315: IMap implementation
    #2316: OracleDBAccess, PostgreSQLDBAccess: improve meta data speed
    #2329: events for notifyVisible, notifyDestroy
    #2409: LCO classloader access
    #2464: RemoteDataBook: set default the tree column view with not ignored 
           representation columns.
    #2469: UI: add new Interface IEditable for all components with is/setEditable
    #2471: New Interface IEditableControl for all controls with startEditing
    #2501: XmlNode: a node with TYPE_CDATA should also write null as CDATA
    #2506: ApplicationUtil.IMAGE_VIEWER: preserve aspect ratio by default
    #2514: NumberUtil: Improve parsing, add same API as DateUtil
    #2556: DateUtil: add functions for date calculation
    #2558: AbstractUIActionComponent: get method name of lambda method references
    #2563: IRowDefinition: add containsColumnName
    #2565: NumberUtil: add feature for strict number format check
    #2568: Support postgres type casts
    #2572: DateUtil: add support for Eras (G)
    #2605: RESt call IFileHandle filename 
    #2606: Rest should support ISO8601 date format
    #2609: DBAccess: add InParam Support to all executeXXX methods
    #2621: Lifecycle email function
    #2646: DBStorage: add new property get/setAfterOrderByClause
    #2678: TreePathFinder better Exception
    #2690: AbstractConnection reopen with properties
    #2743: UIComponent, UIContainer: improve beforeAddNotify to get all parents
    #2745: Support for failed session creation
    #2748: ISessionListener in DefaultObjectProvider too late notified
    #2750: public CallBackBroker
    #2756: DBStorage: alternative primary key columns for refetch
    #2771: application parameter support
    #2817: OracleDBAccess: Support for ROWID
    #2820: GenericBean: Objects should not be unset on destroy
    #2823: DBSTorage: do not throw exception, link reference cannot be joined
    #2837: FileUtil, BinaryDataType: Reduce memory consumption in method getContent
    #2857: Shorter timeouts in http connection

  

[BUG]
 
    #1296: ITabsetPanel contains a typo, it should be "draggable" and not "dragable".
    #1677: Manual autolink won't work if autolink is disabled
    #1807: Invisible components in the FormLayout removes the margins and replaces 
           them with a gap.
    #1882: MetaData implements Cloneable, but no clone() method is implemented.
    #1932: JVxDesktopPane: CTRL+ALT may not be a hot key
    #1947: Exception while fetching Metadata
    #1965: GenericBean.put(String, Object) is being called twice for every object.
    #1973: Placeholders are not being translated.
    #1989: background image repaint on UIPanel
    #1991: CallBacks not thread safe with DirectServerConnection
    #1993: GenericBean get calls put
    #1994: DBAccess: Out Of Memory due to too large setFetchSize 
    #1995: DBAccess NPE and reopen feathre
    #1997: DBAccess bind parameter replacement
    #1999: DBAccess NPE
    #2002: tomcat war mode not working
    #2007: MemDataBook: insert returns wrong rownum
    #2013: DBAccess doesn't replace bind param with null
    #2017: Empty linked cell editor popup not visible
    #2020: ConcurrentModificationException on sessionDestroy
    #2030: TimeZoneUtil, LocaleUtil: Full JVx Support for global and threaded timeZone 
           and locale support
    #2033: SelectionMode CURRENT_ROW_SETFILTER does not work as expected
    #2034: ComboBox with openJDK 12 drawing issue
    #2043: DBStorage: improve representation column detection
    #2048: AliveIntervall - Session expired
    #2049: AbstractLinkedCellEditor: additionalCondition is changed by getSearchCondition
    #2051: UIContainer: removeNotify of Layout is not called
    #2056: SQL Type 2014 is not being handled.
    #2057: Fetching PKs after an insert fails on H2 if there are no PKs.
    #2063: MemDataBook: IndexArrayOfBoundsException in detail databook loops
    #2080: CheckboxCellEditor: Label (Caption) is not translated
    #2171: Connection timeout wrong
    #2201: DateUtil format parsing error
    #2202: MemDataBook: update() is invoked twice if events force it
    #2203: MemDataBook: wrong state of updateallowed in case of insert enabled true and 
           updateenabled false
    #2213: Tab text is not fine
    #2221: DBAccess,DBStorage: invalid character exception in case of columns with space
    #2237: Blob FileHandle with dynamic parameters fail
    #2252: CORS not working
    #2277: NPE in ComboBase
    #2279: MemDataBook: setSelectedRow forces all Detail DataBooks to fetch
    #2292: JVxFormLayout doesn't invalidate
    #2293: DirectServerConnection: remove syncWithServer on get/setProperty
    #2424: PostgreSQLDBAccess: Wrong foreign key view in informationschema
    #2426: Map - marker image not binary
    #2428: Map: No marker -> no default marker
    #2429: Map: Marker handling wrong?
    #2430: Map: Default position
    #2465: DBAccess: column meta data is not always cloned
    #2466: Server: Avoid "Invalid communication state!" if creating subsession fails.
    #2467: PostgreSQLDBAccess: Possible NPE in getAndStoreMetaDataIntern
    #2468: MemDataBook: Avoid unnecessary event after reload on update of master
    #2470: UITree: proper implementation of isInsertSubEnabled and doInsertSub
    #2476: StringDataType: if autoTrimEnd is enabled, empty strings are not mapped to null
    #2496: MemDataBook: notifyMasterChanged syncs unsynced parents
    #2500: XmlWorker: node with TYPE_CDATA and empty String is written wrong
    #2503: JVxIcon: Image not found, if style property is added.
    #2505: DBAccess: IndexArrayOutOfBoundsException in case of mem columns.
    #2507: DBAccess: Connection pool connections are not always released
    #2510: UIFormLayout: NPE and wrong label positions for attached components
    #2512: RemoteDataBook: Full support for disabled meta data
    #2515: PostgreSQLDBAccess: Wrong autoincrement detection
    #2538: AbstractLinkedCellEditor: bug with translated display values
    #2547: All IFormLayout Implementations: centered components should not change 
           preferredSize
    #2549: OracleDBAccess, PostgreSQLDBAccess: Possible NPE
    #2550: UIContainer: setLayout has to call addNotify and removeNotify if notified
    #2551: PostgreSQLDBAccess: Invalid constraints due to not unique constraint name
    #2557: PostgresSQLDBAccess: Exception if table has a column that starts with a digit
    #2559: JVxCheckBoxCellEditor: show disabled state in renderer
    #2560: ApplicationUtil.IMAGE_VIEWER: horizontal and vertical stretch by default
    #2564: PostgreSQLDBAccess: ignore tables with unknown data type
    #2567: OracleDBAccess, PostgreSQLDBAccess: avoid possible NPE when clear MetaData 
           in Thread
    #2570: DateUtil parsing IndexOutOfBounds
    #2571: DateUtil parsing NPE
    #2581: Column index out of range
    #2583: MemDataBook: AdditionalDataRow looses data, and does not notify controls
    #2588: DBAccess: clearMetaData(applicationName) does not always work
    #2589: JVxDateCellEditor, JVxNumberCellEditor: Unexpected Exception Update is not 
           allowed
    #2601: PostgreSQLDBAccess: wrong rows with limit fetch and no sort
    #2613: ExceptionUtil.dump doesn't dump deep enough
    #2620: REST with DBStorage and binary column fails
    #2622: DBAccess: Order by id causes id ambiguously exception
    #2640: FileUtil.save without parent file - NPE
    #2641: PostgreSQLDBAccess: implicit order by primarykey can cause exception
    #2675: DBAccess: fetch Blob fails in Postgres due to new limit clause.
    #2679: MemDataBook: Do not immediatelly notify all detail databooks on reload
    #2684: DBAccess: isAutoQuote does not detect spaces and special characters
    #2705: MemDataBook: not reproducible NPE 
    #2737: MemDataBook: missing after reload event on setTreePath.
    #2744: Bean: hashCode does not work
    #2749: VaadinLinkedCellEditor: sortByColumnName or direct sort on databook does 
           not work
    #2761: DBAccess: wrong length for CLOB, NCLOB, BLOB 
    #2765: MemDataBook: notify master really changed has to consider UID
    #2783: MemDataBook delete does not delete detail datapages when row is inserting
    #2784: MemDataBook: hasChanges for detail data books is very slow
    #2790: DBSecurityManager: MSSQL does not support alias in update statement
    #2793: DBAccess: no brackets around manual whereClause
    #2795: JVxTable: ClassCastException setRowHeight before setDataBook
    #2797: Problematic creation of database elements
    #2803: DBAccess: performance optimization, in case MinRowCount 1 or 2
    #2814: StorageService: Binary data is not supported in executeInsert and executeUpdate
    #2818: RESTServer: Database connections are not closed after REST call
    #2822: MemDataBook: Missing values changed event on additional data row
    #2826: DBAccess: better log in fetch, insert, update and delete
    #2831: AbstractControllable/ ControllerContent: doCommand delegates to wrong 
           controllable
    #2832: cloneResource not working in all circumstances
    #2835: DBAccess: Wrong metadata due to case insensitive cache.
    #2836: PostgreSQLDBAccess: byteArrays are not fetched if primary key is missing
    #2838: DBSTorage: manualLinkReferences may not be cleared on close!
    #2846: Colors to and from hex
    #2848: MemDataBook: NPE in case of reload afterInserted
    #2850: MemDataBook, JVxTable: Problems with startEditing and cancelEditing
    #2853: JVxTable: Exception with ImageViewer as CellRenderer
    #2855: Alive and retry makes session inactive
