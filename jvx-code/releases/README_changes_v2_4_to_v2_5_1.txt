Changelog: JVx 2.5.1

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

  - #1637: UIComponent.invokeInThread doesn't return the thread
  - #1635: Simple push mechanism for callbacks
  - #1625: UITable: set individual cells read-only
  - #1624: IDataSource: add getDataBook(String pName)
  - #1622: RowDefinition should have the method 
           getColumnDefinitions()
  - #1619: ILauncher: set properties for client locale and encoding
  - #1618: StringUtil.toString(Object) should be able to handle 
           Iterators, Iterables and Enumerations
  - #1617: CommonUtil.equals(Object, Object) should also check the 
           values of References
  - #1611: Add a H2 DBAccess
  - #1600: Message should use anchors for text area
  - #1598: MySQL paging requires fine-tuning
  - #1587: Support for paging results in MySQL by using the limit 
           statement
  - #1580: SessionCallHandler with source session
  - #1576: catch Error in AbstractSession.addCallInfo
  - #1572: EventHandler, Reflective: improve vararg calls and null 
           parameter
  - #1570: JavaDoc of IDataBook needs to be improved
  - #1568: Support @NotAccessible for action calls
  - #1565: Inherit methods from Session LCO (@Inherit introduced)
  - #1529: Add a SQLite DBAccess
  - #1527: DBStorage: support automatic link storage with restrict 
           condition
  - #1491: UIIcon should have an option to preserve the aspect ratio 
           of the image
  - #1208: IContainer should have a method that returns its children 
           as Iterable or similar
  - #1143: DBAccess/ DBStorage: better locking support when autocommit 
           is false
  - #714:  JNDI support for <Include> in config.xml
  - #700:  Pie Control
  - #644:  ICallBackListener with object event handler
  - #450:  EventDispatcher should support same parameter types
  - #324:  Code review IDataPage, IDataBook
  - #29:   show client stack when action was called with exception
  - #25:   CallBackListener for IConnection
  - StringDataType: AutoTrim moved to checkAndConvertToTypeClass
  - AbstractSerializedConnection now reads all results from the server, 
    also if an exception occurs
  - ArrayUtil.indexOf(T, T, int, int) is now utilizing the 
    CommonUtil.equals(Object, Object) method
  - public access to BigDecimalDataType util methods 
  - introduced callBack methods in AbstractConnection
  - added no-content-disposition option to DownloadServlet
  - ObjectCacheInstance returns cached object instead of wrapper
  - EventHandler autoboxing support
  - EventHandler allows flexible parameter types
  - introducec ICommandConstants
  - introduced resource classloader for current thread
  - replaced deprecated method calls
  

[BUG]

  - #1643: StringDataType, RemoteDataPage: do not trim on fetch, 
           only on setValue
  - #1640: MemDataBook: save row due to setSelectedRow in DATAROW mode
  - #1638: MemDataBook: Inconstistent state on setWritebackIsolationLevel 
           and changed rows
  - #1634: Date cell editors become empty when set to readonly and can 
           still be edited
  - #1621: In OracleDBAcces the primary keys are not refetched if 
           one value is set
  - #1608: DataRow, RowDefinition: addControl has to check with 
           indexOfReference
  - #1606: UploadServlet has potential security risk
  - #1605: ResourceServlet has potential security risk
  - #1603: session dies when file cannot be read
  - #1601: NullPointerException when reloading databook for UITree
           table
  - #1597: MySQLDBAccess delivers additional rows with null termination
  - #1595: StringUtil: fix replace
  - #1594: Fetching of a query with blobs fail if there is no 
           PK column
  - #1592: DBAccess: NullPointer in fetch
  - #1591: ImageUtil produces artifacts when resizing a gif
  - #1590: EventHandler can no longer handle primitive types
  - #1585: CellEditor of UITable does't consider Font (Font-Size) of the 
  - #1584: Reflective: support assignable primitive types
  - #1583: Reflective: missing bracket in if
  - #1581: JVxTable: selection is broken, if component is removed 
           during mouse pressed
  - #1577: Setting of background color has no effect on UIEditors 
           with UIComboCellEditor
  - #1574: Exception Handling causes invalid UI state
  - #1571: JVxTable: column width is not recalculated on changes, 
           when not notified
  - #1569: Names are not consistently applied to extended components
  - #1567: Remove references to second related anchor and relative 
           position in form layouts
  - #1566: InParam, OutParam: Wrong Param Type in case of array
  - #1564: Style property for CellFormat introduced
  - #1562: StartsWithIgnoreCase, ContainsIgnoreCase: NPE if value is 
           null
  - #1561: JVxEditor: deadlock due to cancelediting
  - #1560: MemDataPage: MemSort/Filter on Detail DataBook does not 
           resort/filter after store
  - #1559: UIImage.getImage(String) should allow to circumvent the cache		   
  - #1558: Setting the background color on an UIFrame does nothing in 
           Swing
  - #1555: Throwable CallInfo added multiple times
  - #1554: MemDataBook: NullPointer in close
  - #1553: DBAccess: getRealQueryColumnName does not always work
  - #1550: SessionCallHandler has NPE in redispatchBeforeCall
  - #1548: MemDataBook: revert Send only last notifyRepaint
  - #1547: JVxTable: exception on initializing the column headers
  - #1546: UIEnumCellEditor: wrong display value when using keyboard
  - #1543: ArrayUtil.equals(Object) is not correct
  - #1451: MemDataBook: improve repositionCurrentDataRow
  - #1449: StringUtil needs to receive better test coverage and behavior
  - Add missing Search calls in doCommand and isCommandEnabled
  - write callback-id for callbackresult results
  
  
[OTHER]

  - fixed MSSql test cases
  - javadoc improvements