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