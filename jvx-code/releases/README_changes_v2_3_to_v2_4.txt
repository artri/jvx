Changelog: JVx 2.4

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

  - #1545: ensure Thread safety, avoid deadlocks 
           (MemDataBook, RemoteDataBook)
  - #1544: SessionCallHandler recording with invalid index
  - #1541: BeanConverter refactoring
  - #1538: Support for boolean and arrays in execute
  - #1535: Opaqueness of the component should be set according to 
           the background color
  - #1526: introduced eventParameterChanged for WorkScreen
  - #1521: Providing abstract cell editor implementations
  - #1513: introduced AbstractFactory and get/setProperty
  - #1503: don't init logging if already done (server-side)
  - #1479: FontAwesome support
  - #1478: used UIEnumCellEditor for allowed values
  - #1476: add getControl to ICellEditorListener
  - #1474: UIEditor.doEdit should request focus
  - #1472: support for translation of values
  - #1471: Modal frame support for Controllable
  - #1469: Faster default value detection (Oracle tuning)
  - #1468: improve JDKLogger line numbering
  - #1465: Introduced StartsWithIgnoreCase and ContainsIgnoreCase
           condition
  - #1461: Reduced invokeLater of MemDataBook
  - #1453: Introduced emptyToNull property in StringDataType
  - #1452: notifyDetailChanged optimization in MemDataBook
  - #1447: ZipInputStream support
  - #1435: Better support for Lambdas in Events
  - SendMail: sender, subject encoding
  - StringUtil.parseColor introduced
  - ApplicationUtil.splitImageProperties introduced
  - auto EOF in MagicByteInputStream
  - UILayout constructor for UIScrollPanel
  - Additional style definitions for internal frames
  - ObjectCache introduced infinite constant
  - Auto EOF detection in AbstractSerializedConnection
  - load resourced preferred as stream instead of URLs
  - AbstractTextCellEditor does now have the functionality for 
    masking passwords.


[BUG]

  - #1540: EventHandler stops working in case of default listener
  - #1532: varchar foreign key columns are not validation enabled 
  - #1531: Default value of validationEnabled in 
           AbstractLinkedCellEditor is incorrect.
  - #1530: Textfield can't get focus after exception on file dialog
  - #1522: changing the connection should add and remove listener
  - #1516: LinkedCellEditor throws IllegalArgumentException -1
  - #1512: add isGlobalActive takes account modal frames
  - #1511: selfjoined currentRow and detal changed flag does not work
  - #1505: Invalid state if call result can't be serialized
  - #1494: endless loop, if controller wants to be active controllable
  - #1489: Iterator modification check fails
  - #1488: Removing StorageReferenceDefinition is not allowed in loop 
           when not using an iterator
  - #1486: duplicate column names
  - #1485: missing after row selected event in sync when selfjoined
  - #1482: column selection does not work with shift and ctrl pressed
  - #1481: only use alignment of IEditorControls
  - #1475: missing space in convertMemberNameToText
  - #1467: BStorage: setMetaData has wrong open check
  - #1466: MetaData does expose its internal column names array
  - #1464: Extend the DBStorage with insteadOf* events
  - #1463: The storage event logic can't handle setting of values
  - #1460: DBStorage fails with NullPointerException if no writeback 
           table is set
  - #1459: Rollback would not work if the DBAccess is closed and 
           autocommit is turned off
  - #1458: Swapping a cell editor of an editor might not yield a GUI update           
  - #1455: key events are not supported, listeners are not always 
           removed           
  - #1450: getChangedDataRows gets rows without having changes
  - #1448: Fixed NPE of StringUtil.concat(String, Object...)
  - #1446: Not all listeners are throwing Throwable (Lambda support)
  - #1424: reloadDataPage has a dummy implementation
  - #1402: reload/setFilter/setSort during sync should cause exception
  - DefaultSessionManager hostname resolving try/catch
  - use offset in reading MagicInputStream and ShadowCopyInputStream
  - UITextField constructor calls refactored
  - ClusteredXYBarRenderer cannot handle negativ bar width
  - Memory leaks in J(Vx)InternalFrame, J(Vx)Table fixed

  
[OTHER]  

  - update to javamail API 1.5.4
  