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