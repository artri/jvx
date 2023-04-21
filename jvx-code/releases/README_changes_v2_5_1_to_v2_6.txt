Changelog: JVx 2.6

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

  - #1722: stricter date parsing
  - #1714: Launchers are silently falling back to default factories
  - #1712: UITable scroll to selected row after reload/filter
  - #1711: UIContainer: improve addInternal avoiding 
           IndexOutOfBoundsException
  - #1710: support for java.util.logging.config.file system property
  - #1708: Save launcher bounds (multi screen support)
  - #1706: XmlNode: API improvement
  - #1705: Support for custom components which are wrapping 
           UIComponents
  - #1701: improve date parsing in DateUtil
  - #1690: Mail should support STARTTLS (properties support)
  - #1685: improved lazy loading of blob
  - #1684: BFILE support
  - #1676: insteadOf DBStorage without writeback table [replacement]
  - #1674: IBean putAll introduced
  - #1675: JVxTable listeners re-registration
  - #1663: better formatted select query
  - #1660: use createAutomaticLinkReference if no writeBackTable 
           is set
  - #1657: Allow to add additional query columns to the storage
  - #1654: DBAccess: add executeFunction with OutParam instead of 
           ReturnType
  - #1652: Allow to customize the MySQL refetch limits		   
  - #1646: RemoteDataBook: add fetchEnabled property		   
  - #1527: support automatic link storage with restrict condition
  - #1092: Order by custom column (RemoteDataBook)
  - support for custom Http request properties (HttpUtil)
  - additional Mail.send method without CC (Mail)
  - don't show SessionExpired if master-connection was re-opened
  - better application setup control (applySetup introduced in 
    Application)
  - mark session initializing
  - include/exclude support for apps.xml (external applications)
  - XmlWorker readAndClose
  - Module support (ServiceLoader)
  - set expired property for expired session
  - StringUtil.convertMethodNameToFieldName implemented
  - read system property for logger factory initialization
  - Message: Yes, No, Cancel mode
  - added system properties for server keys (instane, identifier)
  - introduced preAuthentication method in DBSecurityManager
  - improved logging for callback results
  - improved StorageDataBook configuration (grouping)
  

[BUG]

  - #1729: DBStorage: NPE in getFromClause if SubStorage has 
           subStorageConditions
  - #1727: New parameter support in DBAccess does not take quotes and 
           comments into account
  - #1725: RestrictConditions of masters are being ignored
  - #1716: RemoteDataBook: Storage Exception is not thrown
  - #1707: DBStorage: new row is null, if refetch fails
  - #1702: fetching on a DBStorage with set restrict condition alters 
           the restrict condition
  - #1700: searchNext(ICondition) delivers 0 for not found, if 
           additional datarow is visible
  - #1699: DBStorage: overdefined FKs, automaticLink can still throw 
           an Exception
  - #1697: writeCSV changes QueryColumns Array in ServerMetaData
  - #1696: DBStorage.setAdditionalQueryColumns(...) reduces the 
           columns to only the additional columns
  - #1691: JVxEditor: tooltip is not routed to the focused component
  - #1688: RestrictCondition of substorages does misbehave in certain 
           circumstances
  - #1687: NullPointerException in LifeCycleConnector (REST)
  - #1686: JVxGridLayout adds leftover space to last component
  - #1678: gui changes in JVxTable break custom LookAndFeel
  - #1676: don't remove WriteBack feature if insteadOf is used
  - #1672: Can't hide TextField border
  - #1670: JVxTabbedPane scrollable viewport
  - #1669: OracleDBAccess ArrayIndexOutOfBoundsException in
           executeFunction and executeProcedure
  - #1667: DBAccess with Connection is not open
  - #1666: JVxButton: setting BorderOnMouseOver does not hide border 
           on visible button
  - #1664: ConnectionPool: getConnectionIntern gets connection twice
  - #1661: DBStorage: sort link references
  - #1659: Postgres does not support query timeout
  - #1658: MemDataBook: reload(SelectionMode) does not always work
  - #1651: Inserting files into a PostgreSQL DB is not working		   
  - #1650: In PostgresSQLDBAccess the primary keys are not refetched 
           if one value is set
  - #1648: GridLayout shows gaps when being resized		   
  - #1625: added missing support for IRowCalculator		   
  - #1613: JVxTabbedPane: deactivate event has wrong new index
  - #1579: UILinkedCellEditor does't inherit horizontal alignment 
           datatype of the based column
  - removed correct callback listeners from connection
  - keep root name of a component after removeNotify
  
  
[OTHER]

  - fixed build performance (proxy)
