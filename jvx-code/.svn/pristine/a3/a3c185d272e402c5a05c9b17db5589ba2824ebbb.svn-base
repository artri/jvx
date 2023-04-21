Changelog: JVx 1.2

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

  - #820: Button transparency
  - #813: pre-defined Dialog class
  - #811: Environment support for UILauncher
  - #798: notifyActivate added to IWorkScreen
  - #797: reduced editor flickr (screen open)
  - #794: set/getParameter added to IWorkScreen
  - #793: get/putObject added to UILauncher
  - #792: registration of column views
  - #789: create DBAccess with Connection now sets username and url
  - #788: added environment support in security control
  - #786: getEnvironmentName added to ILauncher
  - #776: getImageName added to IImage
  - #748: tab access methods with if-only-exists check
  - #739: auto-close of server objects (used in LCOs)
  - #732: tabset selection changed events introduced
  - #730: Don't fill whole space of table (only JVxTable: 
          set/isAutoFillEmptySpace)
  - #729: Enter in last column of table (introduced new 
          cell navigation types: row, cell, ...)
  - #728: isCalling added to IConnection
  - #727: introduced PrimaryKeyType
  - #725: Access to MasterSession via AbstractSessionContext
  - #713: config.xml not in application directory
  - #673: support for external application folders
  - #670: MetaData cache per master connection
  - #665: DateUtil, NumberUtil should have a pattern constructor
  - #664: EventHandler.toString implementation of proxy
  - #662: XmlNode: store text parts as sub-nodes
  - #660: GenericBean sets name on object instance of INamedObject
  - #658: Get subpath for specific level
  - #651: EventHandler finds "similar methods"
  - #649: Programmatic configuration of an application
  - #642: DBSecurityManager.getCredentials non static
  - #641: MetaData contains storage features
  - #629: ICellFormatable introduced
  - #623: configurable row height for ITable
  - #514: DataBook, DataPage, DataRow toString improved
  - #472: LinkCellEditor start edit with one click
  - #361: isUpdate/Delete/InsertAllowed changed
  - #5:   save immediate on IDataBook
  - en/disable alive check globally
  - enum array serialization
  - hashCode and equals for MetaData
  - toString for conditions
  - WriteBack feature in MetaData
  - include/exclude columns in Filter
  - simplified Error dialog layout
  - Access to last dispatched event via EventHandler
  - zip now creates folder entries
  - get specific exeception (per type) from exception chain
  - show/hide error details event for Error dialog
  - LocaleUtil: Thread local
  - StringUtil.padLeft, padRight
  - StringUtil.containsWhitespace added
  - StringUtil.countCharacter added
  - StringUtil.firstCharUpper
  - ArrayUtil.containsOne added
  - ArrayUtil.first, last added
  - zip files support
  - ignore missing ServerZone
  - Support for getting system color name
  - thread-safe factory resource access
  - introduced NonClosingInputStream
  - freePort detection with any interface



[BUG]

  - #823: reload of databook in eventAfterInserted caused exception
  - #820: ignore Button background transparency for classic themes
  - #815: double definition of column with automatic joins
  - #800: ArrayIndexOutOfBounds during MemDataBook insert
  - #799: IndexOutOfBounds with separators in popup menus
  - #796: insert in Oracle table with only one column
  - #785: BigDecimalDataType scale trimming
  - #783: MemDataBook rehash caused Exception
  - #782: setRegistryKey: key too long
  - #775: Re-initialization of static fields per UI factory 
          (multi environment support)
  - #774: long running callback calls caused memory leaks
  - #773: GenericBean.destroy closed all properties
  - #770: MemDataBook rehash in an inserting row didn't work
  - #759: BigDecimalDataType scale unlimited check was incorrect
  - #758: NullPointer if reload was used in eventAfterRowSelected
  - #757: Swing' component remove is slow in Applet mode
  - #756: IndexOutOfBounds on TabsetActivated event
  - #752: TranslationMap prefers translanslations with less wildcards
  - #746: setFilter/reload on a master databook didn't sync details
  - #740: removed server-side Memory.gc calls
  - #726: StorageListener can cause 
          "Number of columns and values are different"
  - #674: Mixing factories
  - #672: Support different factories
  - #671: NullPointer in password encryption
  - #669: Clear metadata cache in case of DBStorage.open
  - #668: setSelectedRow(-1) fetches all rows?
  - #667: fetch back primary key fails
  - #666: XmlNode.toString creates String with wrong encoding
  - #663: BigDecimalDataType wrong size
  - #659: Download files with Chrome and comma in filename failed
  - #656: StackOverflow in setTreePath
  - #654: CSV export with millions of records didn't work
  - #650: DateUtil parses wrong
  - #647: DateUtil is slow on parsing a date from text
  - #646: isWritableColumnChanged is wrong
  - #645: AbstractConnection "duplicate" methods
  - #643: NullPointer in DirectServerConnection with callbacks
  - #639: NullPointer in MemDataBook save
  - #634: AutoLogin doesn't work with MySql
  - #631: DateCellEditor first key ignored
  - #628: Column flickrs on first show of auto-resize tables
  - #627: wrong initial preferred size in JVxTable
  - #618: restoreAllRows throws an Exception
  - #617: ArrayIndexOutOfBounds in restoreAllRows
  - #588: LinkCellEditor did not work
  - #391: wrong execution order in DataSource level
  - #215: wrong event order in delete on an inserting row
  - #157: saveAllDataBooks doesn't save all rows
  - #155: reload with selection mode == CURRENT
  - toFront/toBack: temporary disable closing event
  - Timestamp conversion uses Timestamp.valueOf first
  - don't update translation if tet is empty (TextFields)
  - remove columns in full-text filter
  - passwords without characters => null
  - set last alive time after call and not before call (long calls)
  - zip: crc/size for directories
  - close some opened db statements
  - ensure query cache removal


[OTHER]

  - daily Maven snapshots
  - RESTlet update
  - FileUpload update
