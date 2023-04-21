Changelog: JVx 2.8

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

  - #1951: Placeholder support in Editors
  - #1945: REST service - Java code marker
  - #1939: InternalFrame not focusable
  - #1937: DBAccess: negative max fetch time disables optimistic readAhead
  - #1935: XmlNode: better clone support
  - #1920: DBAccess: performance improvements for oracle,  
           add executeInsertStatement
  - #1908: New IPopupMenuButton
  - #1907: DBAccess: give access to metadata cache
  - #1902: IFormLayout: enable setRelatedAnchor
  - #1898: ILinkedCellEditor: get/setConcatMask
  - #1894: UIEditor/ UICellEditor: add helper functions for getting 
           the cell editor
  - #1892: defaultLinkedCellEditor should show the editing column
  - #1886: FontAwesome improvements
  - #1870: Use savepoints for Postgres special transaction handling
  - #1103: Components should have a unique name by default
  - CheckConstraintSupport: Ignore, if column is unknown from parsing
  - OrderedHashtable: keep order - JSON
  - UIContainer
    * Give UILayout access to addInternal for translation
    * Notify the layout when the container is added/removed to/from UI
  - EventHandler: added addListener(IRunnable, int)
  - FileUtil
    * added equals(File, File)
    * support for hasGZIPHeader
  - SimpleJavaSource now supports information for class name, 
    super class and interface implementations
  - SimpleJavaSource: 
    * better enum and inner class support
    * .class was wrong interpreted, if a method call followed
      (String.class.getName())
    * function calls still have to be eliminated.
  - AbstractLinkedCellEditor: Additional clear columns
  - Proper Layout usage in genUI: 
    * Constraints could be genUI constraints
    * set constraints to genUI Layout on add component
      and clear them on remove
  - set session properties for REST session (language, environment)
  - RESTAdapter: session configuration for sub classes 
  - REST Service:
    * introduced RestServiceException
    * introduced visible columns in MetaData
    * support visible columns in REST Services
    * support custom exception handling
    * support _firstRow and _maxRows URL parameter for REST fetch
    * create HttpContext for authentication
    * _admin zone introduced
    * support OPTIONS with CORS
    * _sort parameter
    * list to array action call fallback
    * IAccessChecker introduced
    * support zone name configuration
    * SessionContext now offers IAccessChecker
    * support for Replacement of lifecycle object names
    * authentication forwarding to SecurityManager without browser authentication (SSO)
  - IDataConnector introduced
  - AbstractSerializedConnection: better retry delay (delta calculation)
  - server stop method and uninstall plugins (if possible)
  - MacOS
    * menubar support
    * workaround for ComboBox draw problems
    * improved TextArea font and Calendar button size
    * Button size in general is now better (margins, border)
  - Updated PostgreSql driver (42.2.2)
  - PoC for disabling text input of comboboxes
  

[BUG]
 
  - #1966: SilentAbortException doesn't prevent row change
  - #1965: GenericBean.put(String, Object) is being called twice for every object
  - #1959: Stackoverflow on DBAccess.close
  - #1957: MSSqlDBAccess allowed value detection not working
  - #1956: ConnectorJ 8 not working
  - #1954: DBAccess: Extrem slow fetch with oracle jdbc
  - #1947: Exception while fetching Metadata
  _ #1944: DBStorage: ensure, that mem column values persist on refetching Row
  - #1938: MetaData, ServerMetaData: remove Cloneable Interface
  - #1936: XmlNode: prevent exceptions on empty node path
  - #1934: REST Service: getUserName on current session is null when calling in 
           storage events
  - #1930: BinaryDataType: add encoding feature
  - #1929: JSONUtil: use default UTF-8
  - #1928: CallBackBroker should support sending to master session
  - #1921: UILinkedCellEditor on columns with datatype BigDecimalDataType allows 
           input of alphanumeric values
  - #1920: GenericBean get(Object) not recognized
  - #1913: RowDefinition: NPE on setColumnView(null, null)
  - #1910: JVxTable: ESCAPE Key is consumed in any case
  - #1906: DBStorage: use simple join on writeback table when possible
  - #1905: DBStorage: IndexArrayOutOfBoundsExeption in case of fully 
           overlapping FK definition
  - #1901: CodecUtil htmlMappings maps an apostrophe to "&apos;"
           which is incorrect
  - #1895: Xml autodecrypt with umlauts fails
  - #1893: StringUtil: like doesn't work for ** at the end
  - #1890: CellEditors: ReadOnly Background should only be shown, if no other 
           color is set
  - #1888: DBAccess: prefix detection fails, if column has quotes
  - #1887: And/Or behave unexpected when empty
  - #1885: SimpleJavaSource: operators in method parameters do not work
  - #1876: Master reports changes when Detail row is deleted and both 
           databooks have been saved
  - #1873: DateUtil fails with certain locales
  - #1671: JVxSplitPane: JSplitPane binds F6 key
  - #1594: Fetching of a query with blobs fail if there is no PK column
  - JVxEditor: Fixed that the editor would be in an inconsistent state after a 
    failed save
  - RemoteWorkScreen: Fixed that modality of the workscreen to open would not be 
    passed correctly
  - ImageUtil: Ensure image is not created unnecessarily
  - ApplicationUtil: Avoid NPE. The title, or at least the launcher 
    simple name is used as fall back
  - AbstractUIFactoryResource: Fix: cachekey may never be cached, as it deliveres the 
    current factory/factory class for which the resource has to be looked for