Changelog: JVx 1.1 beta1

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

  - #381: insert in postgresql failed with case sensitive table 
  - #536: larger Multiline editor
  - #538: set tables not editable
  - #539: automatc type conversion of allowed values in IChoiceCellEditor
  - #545: set exception listener by index
  - #551: replaced string concatenate operations in XmlWorker (tuning)
  - #552: (API change)
  - #556: list separatr detection (data export to CSV)
  - #558: get root or specific parent node
  - #559: BeanType supports clone of Beans
  - #561: don't send sort definition to server in mem sort mode
  - CodecUtil.encodeHex now supports InputStream


[BUG]

  - #537: HSQLDB column name detection (changed since hsqldb 2.0)
  - #543: comment encoding
  - #544: default label detection in default lower case databases
  - #546: clone existing filter
  - #547: StringUtil.convertToMethodName - IndexOutOfBoundsException
  - #549: iconified InternalFrames are now visible
  - #550: 
    * Changed FK detection in DBStorage
    * Uppercase table name
  - #557: NullPointerException in setCursor with null parameter
  - #560: Deadlock if editors are added not in EventQueue Thread
  - #562: DBAccess.getColumnMetaData detects column names case insensitive
  - #565: Add DataBook to RowDefinition only if no error occurs
  - #566: JVxTable throws Exception if databook is not open
  - #567: null check
  - #570: modal frames are not resizable on MacOS
  - #568: preferences access on MacOSX


[OTHER]

  - #564 
    * list separator detection moved
    * CSV export quote detection
  - KeyValueList and IdentityKeyValueList are now jdk 1.5 compatible
  - Error extends IContent
  - IEditorControl now has get/setCellEditor