Changelog: JVx 1.1

##################################################################
#                          General                               #
##################################################################

https://oss.sibvisions.com

- New features
- Bugfixes

##################################################################
#                          Library                               #
##################################################################

[NEW]

  - #84:  enum serialization support
  - #381: insert in postgresql failed with case sensitive table 
  - #403: remove event listeners by method name
  - #451: Generic EventListener equals check
  - #454: EventHandler is/setDispatchEventsEnabled introduced
  - #513: overwrite file question in save file handle (SwingUI)
  - #535: Restrict object calls (introduced object access controller)
  - #536: larger Multiline editor
  - #538: set tables not editable
  - #539: automatic type conversion of allowed values in IChoiceCellEditor
  - #545: set exception listener by index
  - #548: IEditorControl needs cell editor get/set
  - #551: replaced string concatenate operations in XmlWorker (tuning)
  - #552: (API change)
  - #555: executeProcedure in DBAccess needs output parameter
  - #554: executeFunction in DBAccess needs output parameter
  - #556: list separatr detection (data export to CSV)
  - #558: get root or specific parent node
  - #559: BeanType supports clone of Beans
  - #561: don't send sort definition to server in mem sort mode
  - #572: Allow access to IApplication via frame
  - #574: Default Application should support createError/getError
  - #589: DBAccess get connection via JNDI
  - #594: Reflective.call should support class-types
  - #604: dot notation for DirectObjectConnection
  - #605: support for GenUI factory extensions
  - #606: introduced is/setDetectEndNode in ITree 
  - #607: DBAccess open() always opens connection
  - #611: AbstractStorage makes no difference between object and bean access
  - #614: IE download with unsigned applet and mod_proxy
  - #616: dynamic row height for JVxTable
  - #619: reduces updateTranslation calls (performance tuning)
  - #620: MasterSession and SubSession access via interfaces
  - #625: error access for Error dialogue
  - CodecUtil.encodeHex now supports InputStream
  - KeyValueList and IdentityKeyValueList are now jdk 1.5 (backward) compatible
  - Error now implements IContent
  - CodecUtil.encodeHex with InputStream
  - Mail with html support
  - configurable Factory via launcher parameter
  - ResourceUtil.getInterfaces introduced
  - fulltext filtering with optional columns
  - internal chooser for directories
  - full NTLMv2 support for ntlm authentication
  - DirectoryHandle introduced
  - AbstractMemStorage now allows client-API usage on server-side
    (all columns, not only client columns)
  - introduced close() for IRequest and IResponse 
  - public access to JSON' ObjectMapper via JSONUtil
  - AbstractSecurityManager got an option for force exception hiding
  

[BUG]

  - #489: JVxInternalFrame setLocationRelativeTo has wrong position
  - #534: REST LCO detection does not work as expected
  - #537: HSQLDB column name detection (changed since hsqldb 2.0)
  - #543: comment encoding
  - #544: default label detection in default lower case databases
  - #546: clone existing filter
  - #547: StringUtil.convertToMethodName - IndexOutOfBoundsException
  - #549: iconified InternalFrames are now visible
  - #550: 
    * Changed FK detection in DBStorage
    * Uppercase table name
  - #553: executeFunction in DBAccess doesn not work and is slow
  - #557: NullPointerException in setCursor with null parameter
  - #560: Deadlock if editors are added not in EventQueue Thread
  - #562: DBAccess.getColumnMetaData detects column names case insensitive
  - #563: changed Session is expired message
  - #565: Add DataBook to RowDefinition only if no error occurs
  - #566: JVxTable throws Exception if databook is not open
  - #567: null check
  - #570: modal frames are not resizable on MacOS
  - #575: java.util.Date not allowed as DBStorage condition
  - #568: preferences access on MacOSX
  - #590: AFTER_RESTORE event in delete funtion too early
  - #595: Table sort with <Ctrl> and OracleForms VM
  - #596: UIFactoryManager.setFactoryInstance checks wrong
  - #597: DBStorage should ignore BLOB columns as PK
  - #598: JVxTable throws NPE when cursor is not set
  - #599: resize column smaller than table size is not possible
  - #602: unknown column name in setFetchedRowsForBlock
  - #608: GenericBean initXxx call not always executed
  - #613: Automatic choice cell editor not detected
  - #621: JVxTable flickering scrollbars
  - #624: Filter.createFullTextFilter should use only writable columns
  - #626: InternalFrame illegal component state
  - ResourceUtil.getResourceAsStream now uses ClassLoader parameter
  - changed MetaData validation in AbstractMemStorage (after not before)
  - XmlNode ignored ß
  - DataRow should not compare unknown columns


[OTHER]

  - #564 
    * list separator detection moved
    * CSV export quote detection
  - KeyValueList and IdentityKeyValueList are now jdk 1.5 compatible
  - Error extends IContent
  - IEditorControl now has get/setCellEditor
  - Javadoc updates
  - cleaned package dependencies
  - test cases added (reached again > 80% coverage)
  - Tomcat <= 7 deployment support
  - FindBugs added to quality tasks
  - StopWatch added to build steps
  - PostgreSQL SSL connection test
  - Checkstyle update from 5.1 to 5.5