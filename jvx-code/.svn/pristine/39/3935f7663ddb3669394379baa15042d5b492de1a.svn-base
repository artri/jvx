Changelog: JVx 2.1.1

##################################################################
#                          General                               #
##################################################################

https://oss.sibvisions.com

- Bugfixes

##################################################################
#                          Library                               #
##################################################################

[BUG]

  - #1211: DBAccess/ BinaryDataType: Exception with RemoteFileHandle
  - #1212: reopen doesn't work


------------------------------------------------------------------

Changelog: JVx 2.1

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

  - #1195: Default Link table column names are not good
  - #1193: RowDefinition: Constant for ALL_COLUMNS
  - #1192: AbstractStorage should have an overload for 
           update that also accepts the old row. 
  - #1181: Beans should be cloneable
  - #1177: IServerPlugin introduced
  - #1175: add usefull constructors in all data types
  - #1155: Support for empty columns used by stretched components
  - #1152: Datasources for different environments (test, dev, prod)
  - #1150: rad detection doesn't work with tomcat
  - #1149: The generated name of UIComponents should be 
           somehow exposed.
  - #1144: Replacement support for action calls
  - #1142: DBAccess should commit or rollback before close
  - #1141: DBAccess should offer setAutoCommit
  - #1130: Put creation_time to connection properties
  - #1126: Flexible zone detection via Configuration (JNDI, Classpath)
  - #1121: Support for empty columns
  - #1115: IAccessController improvement (API change)
  - #1113: REST services refactored and UnknownObjectException 
           introduced
  - #1107: Remove session isolation control via session property
  - #1106: Strict session isolation support
  - #1105: Support creating master sessions without LCO
  - #1103: Components should have a unique name by default
  - #1100: reduce clone of String[] to improve memsort
  - #1097: optimize internalize
  - #1095: IComponent add get/setTabIndex
  - #1093: AbstractConnection communication logging
  - #1078: Introduced controller properties
  - #1067: JNDI Support for Security Manager connection
  - #1062: Support for automatic link with from clause and 
           different settings
  - #1059: Removed CallBackWorkers on server-side and introduced
           UIInvoker
  - #1056: CommunicationException now supports AbstractConnection
  - #1055: Used connection as event source
  - #1054: Event source via CallBackEvent
  - #1053: NEW Version class for version numbering
  - #1050: add alignment properties to IEditor (API change)
  - #655:  Event handler for UIPopupMenu
  - SAP' Hana DBAccess (PoC)
  - HttpUtil introduced
  - HttpContext introduced
  - ServerContext introduced
  - BeanConverter introduced
  - UICheckBoxMenuItem constructor with text
  - UILauncher.getParameterAsBoolean introduced
  - UIEnumCellEditor now uses Object datatypes
  - UIColor creation with hex string
  - StringUtil.firstCharLower introduced
  - StringUtil.getUpperChaseCharacters -> getText
  - StringUtil.getFirstWord introduced
  - StringUtil.sanitizeId introduced
  - StringUtil: added the levenshteinDistance function
  - ArrayUtil: merge methods introduced
  - Configuration: reset cache on configuration changes
  - Configuration.clearCache introduced
  - Custom security exceptions for security manager
  - save resourcePath in TranslationMap
  - ConnectionException introduced
  - Deprecated: StorageEvent.setNew


[BUG]

  - #1200: reload in current_row mode clears tree path
  - #1195: Wrong validation state in AbstractMemStorage
  - #1190: ArrayOutOfBoundsException when calling 
           AbstractStorage.getEstimatedRowCount(ICondition)
  - #1180: Update of POJOs in the AbstractStorage may fail with a 
           conversion failure
  - #1178: AbstractConnection listeners can't remove themselves 
           during the event.
  - #1176: additional datarow is created to early
  - #1160: missing notifyRepaint event when detail page is rehashed
  - #1156: delete master row restores all changes of a detail databook
  - #1140: Wrong ID
  - #1131: AbstractDBSecurityManager should close and 
           unregister statements
  - #1128: Avoid NPE in AbstractStorage if primary key is null
  - #1124: ModelEventHandler, StorageEventHandler: do not wrap 
           SilentAbortException!
  - #1123: IndexOutOfBoundsException in UIContainer 
           beforeAddNotify(IComponent) 
  - #1122: There's a typo in GroupHashtable.constainsKey(G, K).
  - #1120: isReadonly is wrong spelled, it has to be 
           isReadOnly (API change)
  - #1119: MemDataPage.toString() breaks the databook
  - #1112: Not all Exceptions are delegated to ExceptionHandler
  - #1110: UIButton.get/setActionCommand() should not be affected 
           by the text of the button.
  - #1099: Fix SecurityException in Applet
  - #1098: RemoteDataSource: exceptions if connection is closed
  - #1094: SubSession not removed from MasterSession
  - #1090: IndexOutOfBoundsException on selection of tab 0
  - #1089: Improved popup menu support
  - #1087: get/putObject to store additional data
  - #1086: problems with Object[] and varargs
  - #1085: close does not reset changed properties
  - #1084: fixed call hierarchy
  - #1083: don't use restrict condition in join statements
  - #1082: createSubStorageName returns wrong string
  - #1081: Additional datarow shows master columns
  - #1080: NPE in detail data books when additional row is visible
  - #1077: restoreAllRows can cause NPE in setTreepath
  - #1075: Update of null with IBean or POJO does not work
  - #1074: Insert fails if additional data row is visible.
  - #1073: Disable delete option for additional row
  - #1072: invalid join in automatic sub link reference
  - #1068: WHERE clause is not always used
  - #1061: NPE if query column does not exist
  - #1058: Popupmenu translation
  - #1052: Caching of default values
  - #1049: Search row changes visible columns
  - #987:  DBStorage: do not fetch blob and clob columns in 
           lookup storages
  - #958:  AbstractStorage: update, insert return bean standard
  - #974:  restore event has wrong originalRow
  - #973:  OutofMemory Exception in removeDataPage
  - #972:  setFilter on master Databook causes fetch
  - #880:  setFilter Bug if MemFilter/ DataSourceLevel mode
  - set email encoding
  - fixed session destroyment if no call was done
  - avoid ambiguous call of getBytes() and getBytes(String) 
    with Reflective.
  - UIImage now clears image cache via factory
  - synchronized default image mapping
  - clone metadata hashtable to avoid concurrent modification
  - removed direct calls of streaming server functions 
    from session classes
  - UIComponent.setSize supports null
  
  
[OTHER]  
  - Removed FileUtil.close because of CommonUtil.close
  - UICellFormat constructor with background and foreground
  - jdk 1.5 compatibility for jdk 6 compiler (java.sql.Wrapper)
  - Compiler problems in Netbeans