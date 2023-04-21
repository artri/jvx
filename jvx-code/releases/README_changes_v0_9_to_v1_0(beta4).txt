Changelog: JVx 1.0 (beta-4)

##################################################################
#                          General                               #
##################################################################

http://support.sibvisions.com

- New features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #406: moved IDataBook.search to IDataPage
  - #457: reduce databook calls for detail databooks on fetch
  - #459: metadata caching on/off switching via connection/session
          properties
  - #460: clear metadata cache
  - #462: refresh metadata
  - #463: saveControls added to DataRow
  - #464: load resources from local/in memory archives
  - #465: JNLP service access (clipboard, open/save files, ...)
  - #466: CellEditorHandler exception handling changed
  - en/disable metadata caching
  - added searchPrevious to IDataPage
  - Better exception if column does not exist
  - CompareCondition optimization
  - Ensure root log level after load
  - changed default font of TextArea
  - FileUtil.getContent wrapper added
  - FileUtil: used reader encoding
  - AbstractMemStorage: enabled access to internal databook


[BUG]

  - #381: insert record failed
  - #436: change quotes
  - #557: getMaxColumnNameLength() - 0 means unlimited
  - #451: Remove proxy listeners does not work
  - #458: clone Name arrays
  - #467: MemDataBook does not always call executeFetch
  - StringUtil - fixed mapping from 'Ä' to 'AE' and 'ß' to 'SS'
  - StringUtil.formatInitCap: handled '_' as whitespace (trim check)

------------------------------------------------------------------

Changelog: JVx 1.0 (beta-3)

##################################################################
#                          General                               #
##################################################################

http://support.sibvisions.com

- New features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #79:  AbstractBean serializer
  - #261: DetachedSession introduced, 
          AbstractSecurityManager.createSecuritymanager implemented
  - #407: liveconfig option
  - #408: BigDecimalDataType is default signed
  - #411: TranslationMap non recursive option (~*0)
  - #412: isColumnIgnored implemented
  - #413: Hide MasterLink detection moved to RowDefinition
  - #414: formatInitCap trim
  - #421: ApplicationUtil has a new ChoiceCellEditor for Boolean
  - #422: Reflective supports vararg detection
  - #434: limit size/bounds for UITable
  - #435: Name got setters
  - #445: support for virual filesystems (e.g. JBoss)
  - #446: get validated connection from DBSecurityManager
  - CommonUtil.getCauseList
  - getDefaultLabel() trims the label now
  - Default constructor for ColumnDefinition (serialization)
  - SilentAbortException logging
  - New system color INVALID_EDITOR_BACKGROUND
  - KeyValueList now has containsValue with key and value
  - ApplicationUtil got a default IMAGE_VIEWER
  - Editor detects invalid column or row (INVALID_EDITOR_BACKGROUND)
  - hide Exception class when class is not available


[BUG]

  - #398: LinkEditor number alignment
  - #418: getPK, getUK, getFK, getDefaultValues for mysql
  - #420: DBStorage creates too long alias names (oracle)
  - #422: MemDataBook.setRowDefinition(null) add Controls
  - #425: MemDataBook.close should not close detail books
  - #426: EventHandler.dispatchEvent NullPointer with 
          SilentAbortException
  - #432: wrong UKs for oracle
  - #436: OracleDBAccess and PostgresDBAccess quotes
  - #437: MSSQLDBAccess quotes
  - #440: insert returns null without auto increment column
  - #441: DB2 does not support catalogs
  - #442: PK detection for mysql
  - #443: ThrowableSerializer used wrong cause

------------------------------------------------------------------

Changelog: JVx 1.0 (beta-2)

##################################################################
#                          General                               #
##################################################################

http://support.sibvisions.com

- New features
- Bugfixes
- Other

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #381: used real column name for writeable check and
          used real column name where needed (e.g. dummy column)
  - #393: restoreAllDataRows optimization (DATA_ROW isolation level)
  - #400: exception message changed
  - #403: new methods for EventHandler API
  - UICellFormat constructor for image
  - StringUtil.convertToMemberName
  - StringUtil.convertToMethodName
  - StringUtil.convertToName
  - StringUtil method formatting without prefix
  - StringUtil convert methods checks java characters
  - FileUtil.save now creates missing directories
  - FileUtil.save with InputStream
  - DateUtil.getDate
  - UIFont.getDefaultFont now uses the label font
  - Image type detection without window system
  - DBSecurityManager statement cleanup for autologin
  

[BUGFIX]

  - #378: Exception in AfterDeleted Event, if an insert() is called 
          in the user event
  - #387: RemoteDataBook open check null name
  - #392: sync with existing TreePath
  - #397: masterchanged sync
  - #401: trim before number conversion
  - #405: MemDataBook notifyMasterChanged NullPointer fixed
  - UIImage image constants corrected


[OTHER]
  
  - #383: StringUtil cleanup
  - New Connection test cases
  - New Database test cases
  - javadoc review


------------------------------------------------------------------

Changelog: JVx 1.0 (beta-1)

##################################################################
#                          General                               #
##################################################################

http://support.sibvisions.com

- New features
- Bugfixes
- Other

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #334: 
    * LocaleUtil introduced
    * NumberUtil introduced
  - #341: LikeReverse, LikeReverseIgnoreCase conditions
  - #344: custom Access Contoller for DBSecurityManager
  - #346: InvokeLater that invokes even in any Threads "later"
  - #347: DataBook in DataSource level should in notifyMasterChanged 
          always check if rehash is required
  - #351: XmlNode - consistent value detection
  - #352: XmlNode.getNodeValue implemented
  - #363: ILifeCycleObject introduced
  - #364: Call logout of SecurityManager only for MasterSession
  - #365: create new lists instead of clear old ones
  - #366: UI Support form image in CellFormat to display an image in a
          table cell
  - #370: check case sensitive directory name     
  - DBAccess createReplace added
  - UIImage init/set default images
  - UIFormLayout.getMaximizedConstraints implemented
  - Form Layout get/setAnchorConfiguration, to configure all default 
    anchors with one property
  - UIComponent requestFocus has to be deleted to the 
    ComponentUIResource
  - UIImage clear image cache
  - UIImage get/setImageName
  - IScrollPanel introduced (temporary)
  - Globally set toolbars movable (or not)
  - AbstractStorage checks values against bean properties
  - RemoteDataPage: estimatedRowCount changed to getEstimatedRowCount
  - added database url and username to metadata cache key
  - Configuration.ApplicationListOption added
  - FileUtil list zip entries
  - FileUtil.copy(InputStream, File)
  - FileUtil.getDirectory
  - FileUil.like with support for **, *, ?
  - FileUtil.formatAsDirectory implemented
  - ArrayUtil.toArray(start, length) implemented
  - StringUtil.toString now supports Dictionary
  - ResourceUtil.getResourceClassLoader implemented
  - ResourceUtil resource detection changed: relative file search 
    only for "jar" case
  - ResourceUtil checks for empty resources
  - TriggerAPI introduced
  - ISecurityManager.release added
  - Monitoring: close application sessions
  - Execute utility introduced


[BUGFIX]

  - #330: restoreAllRows fails in DataSource Level
  - #331: Endless loop with Eventqueue if more than one JVxTable
          uses the same data book
  - #332: support for server-side insert/update/delete for all 
          available columns (not only visible/client columns)
  - #333: support for case-sensitive app names
  - #336: First check if a DataPage with the UID exists. Reuse new 
          DataPages in DataSource level.
  - #337: AbstractMemStorage updates checks changed columns
  - #339: ColumnView set/add/removeColumnNames does not call 
          notifyRepaint
  - #342: UIImage.getImage(...) should return null if image does not
          exist
  - #348: sync() fails if a seljoined MemDataBook has the TreePath 
          wrong
  - #349: IndexOutOf BoundsException due to UITabset component
  - #350: MemDataBook should remove all details in saveSelectedRow(), 
          if a row is deleted
  - #355: reverted Ticket 294 because of Load-on-demand
  - #356: capture components without content
  - #357: setSort on MemDataBook with setMemSort(true), doesn't 
          setSelectedRow correct after sort
  - #362: SessionManager caches subconnections too long 
  - #372: avoid recursive translation parents
  - #374: If an insert happens, after that more rows have to be fetched, 
          then wrong rows will be fetched
  - #375: delete, cancelEditing missing fixed
  - #369: delete all rows from a databook, Master changed has to be set
  - #376: restore row fails in DataSource level with more then one detail 
          level with isInserting rows
  - DBAccess writeback - schema caching
  - MetaData detection in AbstractStorage
  - AbstractMemStorage refetch fixed
  - null LifeCycle name check
  - cache configured securitymanager class name instead of 
    instance class name
  - avoid stack overflow exception with custom translation maps
  
  
[OTHER]

  - Removed RESTStorage and restlet libs  