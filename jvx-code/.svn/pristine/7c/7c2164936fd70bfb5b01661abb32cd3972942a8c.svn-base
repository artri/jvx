Changelog: JVx 2.0.2

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

  - #1044: set/isTranslationEnabled added to ITrasnlatable
  - #1043: ColumnDefinition flag for filterable
  - #1041: temporary download URL introduced for RemoteFileHandle
  - #1040: don't set empty tooltip
  - #1036: CommonUtil.close introduced
  - #1033: object name annotation introduced
  - #1029: Set ORDER BY as property of DBStorage
  - #1024: BigDecimalDataType avoids exponent
  - #1021: new selection modes: CURRENT_ROW_DESELECTED, CURRENT_ROW_SETFILTER
           CURRENT_ROW_DESELECTED_SETFILTER
  - #1018: System property framebounds for SwingApplication
  - #1015: empty getConstraints returned
  - #1014: set a display column on an ILinkedCellEditor
           Introduced UIEnumCellEditor
  - #1013: removed ExceptionHandler.raise from JVxTabbedPane
  - #1011: RemoteDataBook improved visible columns of linked cell editor
  - #1010: DBStorage now limits prefix to ensure readable column names
  - #1001: No translation if translation is disabled
  - #998:  added default choice cell editor with (yes, no, null)
  - #997:  added listener to IConnection for notifications about property
           changes
  - #996:  DBAccess isModified introduced
  - #993:  register default cell editors, not only UIChoiceCellEditors
  - #587:  Memory filter should replace special wildcard characters
  - #20:   The model should support a default null row (e.g. search row)
  - #2:    register value changed of single column
  - #1:    register key event listener for certain key
  - send connection class to server
  - allow to disable timeout check globally
  - set a name for error message 
  - OS detection via ApplicationUtil
  - DataBookBuilder introduced
  - FileUtil.getNotExistingFile support for prefix, suffix
  - ICloseable introduced


[BUG]

  - #1047: tree resize didn't render on selection change
  - #1046: moveDataRowToCorrectMaster caused NullPointerException
  - #1045: JVxFormLayout was not always validated
  - #1039: DBStorage exception in lock row
  - #1037: setSelectedRow in MemDataBook didn't save editors
  - #1035: ControllerContent doSearch fixed
  - #1034: PostgreSql problems with wrong column names
  - #1032: fixed NullPointerException in DataRow.compareTo
  - #1031: fixed column order in DBAccess
  - #1030: Filter.createFilter doesn't work
  - #1026: ensure correct data type for comparison
  - #1025: handle Exception in direct editor
  - #1022: check mem filter option
  - #1016: fixed removing datapage IndexOutOfBoundsException
  - #1012: use driver of DBCredentials if set
  - #1007: enum detection fix
  - #1006: 1 column means no header, more than one row shows header
  - #1004: ClassCastException in ChangeableDataRow.getUID
  - #1003: JVxTree exception if not all DataBooks were open
  - #1002: JVxTree InvalidCharacterException
  - #1000: JVxTree fetch ahead resets tree selection
  - UICellEditor has to be an IResource
  - fixed automatic DTD/Schema validation
  
  
[OTHER]  
  - DB unit tests reviewed
  - fixed REST unit tests
  - test all DBs with custom VMs