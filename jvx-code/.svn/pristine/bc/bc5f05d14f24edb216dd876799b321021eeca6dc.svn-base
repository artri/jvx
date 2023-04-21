Changelog: JVx 2.3

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

  - #1435: Better support for Lambdas in Events
  - #1433: SessionContext got getMasterSession 
           (API change and AbstractSessionContext dropped)
  - #1432: EventHandler ListenerHandler proxy is too optimized
  - #1417: get/setDefaultCellEditor and get/setDefaultCellRenderer
  - #1413: FileHandle support for REST
  - #1411: CORS support for REST
  - #1410: update to RESTlet 2.3.2 (was 2.2.3), Jackson update
  - #1408: detectModified, executeStatement with parameter, 
           executeQuery support
  - #1407: connection pooling support
  - #1399: support for bcc mails
  - #1397: call listener for server-side event processing integrated
  - #1390: changed to autocommit due to locks in Postgres
  - #1389: XmlSecurity loads userlist via JNDI (optionally)
  - #1388: zip handling with support for ZipInputStream 
  - #1387: support VFS without hacks (WildFly, JBoss)
  - #1380: LoggerFactory.destroy set to public
  - #1379: default implementation of showMessage and getContentPane
  - #1349: support for tnsnames.ora entries (Oracle DB)
  - #1339: property file support for XML configurations
  - #1333: Filter/Sort changed events
  - #1209: Support for AM/PM format
  - #1207: UIContainer, UIComponent: better handling of internal 
           components
  - #957:  support for date and binary, FileHandleType
  - #26:   window translation (prepared for JavaFX, 
                               not with Swing LaFs)
  - support environment name with additional information 
    (separated by :)                               
  - Better support for custom DB security managers
    postAuthentication 
  - Filter.getEqualsValue introduced
  - RowDefinition now shows master link columns if no columns would
    be visible
  - equals and hashCode implementation for color
  - CommonUtil migrated to ExceptionUtil (only Exception relevant methods)
  - ExceptionUtil.getMessages methods introduced
  - external InjectObject support
  - UIComponent got an API for easy changing the componentUIResource
  - StringUtil.replacePlaceholder introduced
  - StringUtil.removeWhiteSpaces(String) optimized
  - StringUtil.concat introduced
  - ObjectCacheInstance introduced
  - MemDataBookStorage introduced
  - AbstractMemStorage now uses all columns if no user-defined 
    column view was set
  - ArrayUtil now supports char[]
  - Added setXXXAtIfExists for call setter with tab index (UITabsetPanel)
  - AutoScroll for Swing TextArea
  - 
  

[BUG]

  - #1443: remove search for similar linked cell editor databooks
  - #1442: missing notifyRepaint event when detail page is removed
  - #1441: row is not saved, in specific event circumstances
  - #1439: incompatible remove since #1207
  - #1436: Wrong component size since #1207
  - #1434: equals, hashCode, toString not implemented
  - #1423: Empty data page should have reference to data book
  - #1422: AbstractSerializedConnection problems with Multi-Threading
  - #1418: wrong preferred size before visible
  - #1409: getQuotedBoundaries does not check null String
  - #1400: JVxChoice, ChoiceField: selected index wrong if null in 
           allowed values
  - #1384: getQuotedBoundaries does not detect String only
  - #1378: Server.getInstance() doesn't init logging factory
  - #1373: Save editors only top down in case of readonly
  - #1368: Collapse of tree node when child node is selected needs 
           2 clicks
  - #1363: FK with Referenced Table null on missing grant
  - #1357: IndexArrayOutOfBoundsExeption in case of fully overlapping 
           FK definition
  - #1344: getDefaultCellEditor(pValues) depends on value order
  - #1343: infinite loop
  - #1340: JVxDesktopPane/ JVxInternalFrame: proper focus handling on 
           dispose
  - #1338: finish editing jumps to first column in some cases
  - #1332: Focus travesal does not work correctly in combination with 
           custom tab index
  - #1315: reload(), fetchAll() after setFilter()
  - XmlWorker: check unrecognized features
  - Thread safety in SessionManager
  - Avoid NPE, in case  removeNotify is called without addNotify
  - ClusteredXYBarRenderer cannot handle negativ bar width.
  
  
[OTHER]  
  - better JDK 8 compatibility
  - javadoc updates
  
