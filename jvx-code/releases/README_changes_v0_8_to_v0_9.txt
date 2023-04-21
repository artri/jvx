Changelog: JVx 0.9

##################################################################
#                          General                               #
##################################################################

- New features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

http://support.sibvisions.com

[NEW]

  - #100: sort property for link cell editors 
  - #115: Model API should have also Default Values and Allowed Values 
  - #161: translation for Date/Time picker 
  - #163: IChangeableDataRow should support isWritableColumnChanged 
  - #297: SessionContext addObject is temporary
  - #302: ITable should be able to handle own ColumnView
  - #306: AFTER_RELOAD should also occur if detail page changes 
  - #307: allow on/off for global metadata cache 
  - #308: DB specific automatic quoting
  - #313: component moved and resized events added
  - #314: border visible option for TextField/Area added
  - #318: setReadOnly for RowDefinition of DataRow does not change UIEditor 
  - #322: Default constructor of DataRow creates an empty RowDefinition
  - #323: selectAll for ITextField added
  - #326: set default sort for AbstractMemStorage 
  - Message box layout changed
  - UIBorderLayout is now the default layout for UILauncher
  - UIFormLayout has new constraint getters for top, left, bottom, right 
    alignment
  - FileUtil.replace added
  - FileSearch supports directory filter patterns
  - StringUtil.getCharacterType added
  - StringUtil.getCaseSensitiveType added
  - XmlWorker supports namespaces (simple)
  - XmlWorker writes line-feeds between tags, if enabled
  - XmlNode: toString(boolean), getXmlValue added
  - DefaultSessionManager checks if a security manager is configured
  - automatic and manual clear meta data cache support
  - added stack info of life-cycle access before ObjectProvider
    call (not after)
  


[BUGFIX]

  - #171: Object injection restriction for specific lifecycle 
          objects 
  - #172: Inject objects with class name
  - #295: getPK, getUKs, getFKs return all PK, UKs, FKs over all 
          schemas
  - #298: Visible columns are wrong in RemoteDataBooks, where the 
          Master/Detail removes auto LinkColumns 
  - #299: AFTER_RELOAD Event is thrown to early - DataBook in wrong 
          state 
  - #300: Thread safe lazy initialization of life-cylce objects
  - #301: BigDecimalType checks Precision and Scale in wrong way 
  - #304: saveFileHandle does not work with IE
  - #309: isNullable will be determined from the writebackTable instead 
          of the fromClause 
  - #315: saveSelectedRow() bug with more then one leave with 
          isInsertung() ==true in DATASOURCE level 
  - #316: Not all Rows removed with deleteAllDataRows in combination 
          with saveAllRows in DATASOURCE_LEVEL
  - Message box text wrapping          
  - Async callbacks via IConnection throws a CommunicationException
  - UIColor.createColor should allow null as parameter without
    Exception
    

--------------------------------------------------------------------------------
Changelog: JVx 0.9 beta-4

##################################################################
#                          General                               #
##################################################################

- New features
- Bugfixes


##################################################################
#                          Library                               #
##################################################################

http://support.sibvisions.com

[NEW]

  - #59:  moved getFileHandle(Object, String) t UILauncher
  - #60:  ITable/ITree move setNodeFormatter(Object, String) to 
          UITable/UITree
  - #61:  ITable/ITree move setCellFormatter(Object, String) to 
          UITable/UITree 
  - #62:  Image change saveAs(String filename) to 
          save(InputStream, Enum type) 
  - #96:  user-defined condition for link cell editor 
  - #199: Set the automatic link editor invisible for 
          MasterReference
  - #207: use compression option for SubConnection from 
          MasterConnection
  - #211: automatic allowed values for Boolean columns
  - #259: Column not found instead of ... not open
  - #273: color creation with hex string
  - #285: detect security manger changes
  - #289: server-side exception for LCO access 
  - #290: DataBook in DATASOURCE level shouldn't fetch details to 
          master that is currently inserting
  - #294: Guarantee 1 data row read ahead, to support loops with 
          getRowCount  
  - CommonUtil.getFirstCause added
  - ResourceUtil.getResource added
  - FileUtil.unzip added
  - singleton anchors and constraints for form layout
  - ServletServer unsets cookies if propertie was set to null


[BUGFIX]

  - #262: update before clone app configuration
  - #275: UILauncher.setMenuBar does not unset parent of menubar 
  - #278: ToolBarPanels does not check null as parent 
  - #286: MemDataBook optimize getChangedDataRows() 
  - #287: server-side metadata caching - open() 
  - #291: Selection of current DataRow lost in DetailDataBook, 
          saving Inserting Master 
  - #292: borderonmouseover shown for disabled button 
  - #293: attribute quoting
  - ArrayUtil.addAll returns the correct object type
  - AbstractMemStorage restores in insert/update
  - DBSecurityManager closes only declared statements
  - missing reload event in MemDataBook on setFilter or setSort
  - NullPointerException in MemDataBook saveSelectedRow
  - ChangedHashtable.isChanged compare changed

  
[OTHER]

  - Changed checkstyle configuration
  - Added test cases for new features and bugfixes
  
--------------------------------------------------------------------------------
Changelog: JVx 0.9 beta-3 (plafo)

##################################################################
#                          General                               #
##################################################################

- New features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

http://support.sibvisions.com

[NEW]

  - #59: moved getFileHandle(Object, String) t UILauncher
  - new constant for PARAM_APPLICATIONLANGUAGE added to ILauncher
  - #242: don't throw Exceptions for already declared default choice 
          editors
  - #243: simple charting API and implementation for JFreeChart
  - #248: global metadata caching


[BUGFIX]

  - #235: default layout corrected
  - AbstractMemStorage now detects metadata in open()
  - javadoc fixes

--------------------------------------------------------------------------------
Changelog: JVx 0.9 (beta-2)

##################################################################
#                          General                               #
##################################################################

- New features
- Bugfixes
- Code reviews, Tests


##################################################################
#                          Library                               #
##################################################################

http://support.sibvisions.com
 
[NEW]  

  - #221: CoC for db datasource ("default")
  - #230: quoting in DBAcces 
  - #232: quoting on/off property - default off
  - #233: Server side trigger for Storages
  - Utility methods 
    * FileUtil: copy directories, copy mode enum, copy empty dirs.
                move, 
    * XmlWorker: read and write additional with streams
    * FileSearch: searchFirstFile
  - new UIColor(0xFFFFFF) is now default opaque
  - GenUI: 
    * default layouts
    * default first and second component for SplitPanel
  - TreePathFinder instead of TreeNode
  - changed constants in UIEvent for MouseButton
  - DefaultObjectProvider and DefaultSessionManager are now reusable
  - DefaultObjectProvider allows custom class loader
  - Application: login/logout event handler
  - allowed custom SessionManager
  - dispatchEvent in EventHandler uses a "clone" for notification


[BUGFIX]
  
  - #136: MySql refetch
  - #200: Allow empty DB passwords
  - #222: DBAccess, OracleDBAcces: getUKs returned PKs
  - #223: ArrayIndexOutOfBoundsException in sort
  - #224: name detection changed
  - #228: custom ObjectProvider detection and creation
  - #234: changed schema/table detection from #236
  - AbstractMemStorages tries to refetch if row was not found
  - DBAccess:
    * getFKs, getUKs returns empty list instead of null
    * getAutomaticLinkColumnName check null and _column
    * fixed missing space in getFromClause 
  - getPassword in AbstractConnection is now public
  - removeToolbar didn't remove from internal component cache
  - remove of Toolbars checked wrong parent
  - DBSecurityManager checks invalid configured
  - DataRow
    * no more Exception in constructor if DataRow has no columns
    * changed toString
    * compareTo changed
  - MemDataBook
    * clear masterchanged flag if treepath is set
    * no more Exception in getSelectedColumn if DataBook is not open
    * selfjoined with deselected root
    * reload sets treepath to toplevel
  - moved default choice editor configuration from ApplicationUtil to
    Application
  
  
[OTHER]

  - test cases updated
  - better logging in executeXXX methods in DBAccess
  - changed visibility of isFullFilled in CompareCondition
  - removed SqlSupport
  - removed isNotified check in translate of UIComponent
  - build changes
  - changed some tests for CI

--------------------------------------------------------------------------------
Changelog: JVx 0.9 (beta-1)

##################################################################
#                          General                               #
##################################################################

- New features
- Bugfixes
- Code reviews
- Tests


##################################################################
#                          Library                               #
##################################################################

http://support.sibvisions.com
 
[NEW]  
  
  - #22:  set/getEventSource
  - #178: UI independent ITableControl, ITreeControl and 
          IEditorControl
  - #185: HttpConnection: addUrl, removeUrl (prepared for ssl 
          checks)
  - #187: notifyDetailChanged changed to set the DetailChanged 
          state correct
  - #195: create DBAccess with pre-configured java.sql.Connection
  - #196: DBAccess supports pre-configured java.sql.Connection
  - #206: changed ApplicationZone caching during Session creation 
          (code review)
  - #219
    * new class DataSourceHandler
    * new class DBCredentials
    * create DBAccess with DBCredentials
    * config supports declaration of datasources
    * DBSecurityManager supports DataSourceHandler
  - DBAccess supports DBCredentials
  - added text/image constructors to UICheckBox, UIToggleButton, 
    UIMenuItem, ...
  - Utility methods 
    * FileUtil: zip, delete file/directory, deleteEmpty
    * RessourceUtil: support different ClassLoader
    * Reflective: printFields, printMethods
    * StringUtil: replace
    * CommonUtil: equals
    * FileSearch: exclude pattern: "!*.java"
  - Configuration.listApplicationNames
  - changed UpToDateConfigFile now supports setNode, setProperty, 
    save and reload
  - Zone is now Cloneable
  - EventHandler allows add listener by index
  - XmlNode now supports node replacement via setNode
  - XmlNode supports indexOf
  - new class AbstractCachedStorage (superclass for ICachedStorage 
    implementations)
  - new class AbstractMemStorage (server side MemDataBook as 
    storage)
  - base export support for IStorage implementations
  - DBAccess allows executeSql
  
  
[BUGFIX]
  
  - #186: wrong row states
  - #188: UK detection fixed
  - #192: create empty default ColumnView
  - #197: UID handling fixed
  - #198: use precision for StringDataType and BinaryDataType
  - #200: insert into filtered MemDataBook
  - #205: getFetchColumnIndexes
  - #216: ROW_SELECTED event instead of COLUMN_SELECTED (code review)
  - #217: checked result value against MetaData
  - #218: insert/update: checked column count
  - MemDataSource: access to stored DataBooks is now synchronized 
    (avoid multi-threading problems)
  - changed UK detection for OracleDBAccess
  - getValues of MemDataBook now returns array with null values 
    instead of null
  - DataBook now sets states after open and not before
  - XmlNode does not clone() nodes during add/insert/set
  - removed fetchAll in setFilter in MemDataBook (code review)
  - UIFont: deriveFont - size not used
  - BigDecimalDataType precision, scale, internalize (code review)
  - NullPointerException in toString of MemDataBook/RemoteDataBook 
    if no referenced DataBook is set
  - synchronized DirectObjectConnection
  - AbstractType now always returns UNKNOWN_TYPE instead of null
  - SplitPanel bugfixes
 
  
[OTHER]

  - fixed all test cases
  - build creates empty dbs
  - db test cases performance tuning when db is not available
  - changed javadoc URLs (goodbye sun)
  - test cases works now with Mac OS
  - findbugs suggestions considered in some classes
  - additional serializer tests for different technologies
  - ApplicationUtil "extends Object" instead of UIComponent (code review)
  - smaller code reviews