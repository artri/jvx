Changelog: JVx 1.2.1

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

  - #891: UISplitPanel: Default orientation 
  - #892: UISplitPanel: Additional constructors 
  - #885: lazy initialization of inject objects
  - #880: MemDataBook: setFilter if MemFilter/DataSourceLevel mode
  - #869: DBAccess: missing access to some properties
  - #863: DataSource/DataBook: proper checks in open/close
  - #842: added Parameters to work-screen
  - #837: DBOracleAccess MetaData determining is very slow in 11g
  - #832: Simplify MemDataBook, MemDataPage
  - #830: renamed DataBookUtil to DataBookCSVExporter
  - #820: Button transparency
  - lazy initialization of Vector in SessionContext (Tomcat sessions)
  - DefaultSessionManager destroys session if initialization fails
  - support server-side session timeout in seconds and minutes
  - configurable session-controller check interval
  - DBImporter with configurable encoding 
  - toString of DataBooks is now useful
  - also list external allplication directories
  - ThreadManager introduced
  - en/disable encrypted user passwords check
  - additional ignore/allow NULL values in equals filter creation
  - AbstractMemStorage creates a search condition that allows NULL values
  - Simple StringParser (CSV, tokenizer with quote support)
  - FileUtil.deleteSub introduced
  - FileUtil.moveDirectory -> fallback to copy
  - DBSecurityManager, null check for LCO class


[BUG]

  - #882: EventHandler add/remove looses proxy handler
  - #881: Not condition not serializable via UniversalSerializer
  - #875: NPE durin insert in self-join
  - #867: MemDataBook: wrong row is selected on saveSelectedRow
  - #859: ignore missing server zone
  - #856: OracleDBAccess reduced creation of prepared statements
  - #851: OracleDBAccess close all cursors
  - #844: DBStorage with table from different schema 
  - #843: Not all FKs create auto-link CellEditors 
  - #841: NPE in save
  - #829: fixed DTD validation and support dynamic DTD or XSD validation
  - #826: add separator via add instead of addSeparator
  - locale switching in TimestampDataType
  - null/empty db-user password shouldn't crash DBAccess
  - no inactivity while executing
  - DefaultSessionManager destroy session in exception case with id instead of session
  
  
[OTHER]  
  - fixed all red test-cases
  - moved performance tests to new test.manual source folder
  - FileViewer cleanup (removed quotes and replace command array)