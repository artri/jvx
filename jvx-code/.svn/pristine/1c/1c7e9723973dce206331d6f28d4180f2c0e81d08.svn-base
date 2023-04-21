Changelog: JVx 2.0

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

  - #986: JVxLinkedCellEditor: limit time for searching current row
  - #981: border visibility of editors
  - #966: new constructors added to UITable 
  - #964: IComponent supports FocusListener
  - #963: JVxLinkedCellEditor: flexible detection of depending link 
          reference
  - #959: throw Exception if property exists and putting value fails
  - #956: CacheMode for securityManagers introduced
  - #964: support for focus listener
  - #953: * IController and IControllable introduced for databook navigation 
            (Oracle Forms support)
          * ControllableContent introduced
  - #954: OracleDBAccess now supports TIMESTAMP
  - #950: UniversalSerializer supports XmlNode directly
  - #949: XmlNode is now serializable (Java standard)
  - #945: changed synchronized blocking for object provider
  - #944: IAllFetched introduced for fetchBean
  - #939: load security manager with custom class loader
  - #937: ITable and ITree isMouseEventOnSelectedCell
  - #935: * Don't allow sub sessions for isolated sessions
          * session isolation
  - #923: interpreted null as empty (NPE support)
  - #919: isDisposed introduced
  - #913: new IGridLayout, UIGridLayout and implementations for swing, 
          vaadin, web
  - #912: Added default constructor to WorkScreen
  - #904: Reuse DataBooks for LinkedCellEditor
  - #900: DBStorage support for overlapping ForeignKeys and automatic
          link references
  - #874: * defaults only for built-in LaFs (check additional config class)
          * introduced ILookAndFeelConfiguration
  - #802: Bind (native) checkbox to model          
  - #801: Master/Detail update on key navigation (super-lazy-loading)
  - #592: possibility to set PK to ServerMetaData in DBStorage
  - set custom objects before application is started
  - new AbstractDBSecurityManager
  - new utility methods added to database security manager
  - DBStorage createAutomaticLinkColumns instead of ForeignKeys
  - Support for component UI resource Offset
  - Support for component UI resource Insets
  - StringUtil.removeWhiteSpaces
  - StringUtil.convertMember replaces '_' with ' '
  - StringUtil.convertMethodNameToText replaces '_' with ' '
  - FileUtil additional zip methods
  - FileUtil.close
  - ResourceUtil also checks the default class loader for simple resource loading
  - Execute supports custom environment parameters
  - Proper representation columns


[BUG]

  - #992: JVxLinkedCellEditor' key navigation doesn't work
  - #989: missing setParseBigDecimal in NumberUtil 
  - #971: before/afterDeleting on deleting row
  - #961: AbstractStorage fixed column name property handling
  - #960: JVxUtil.revalidateAll with immediate option
  - #953: isDeleteEnabled should take care about isDeleting
  - #951: HsqlDBAccess autoincremental value is set on random column
  - #948: fixed directory search with one empty directory
  - #946: DBAccess createIdentifier doesn't take care of arrays
  - #942: NPE if DBAccess is used outside of session context
  - #936: location calculation corrected
  - #926: Wrong search columns in automatic LinkCellEditors
  - #927: additional clear columns only on existing search column mapping
  - #922: valuesChanged event occurs when no column is changed
  - #920: popup flickr and focus handled correctly
  - #917: JVxLinkedCellEditor' clear overlapping columns
  - #916: AutoLink columns may not exist in MetaData
  - #915: AutoLink reference can have a column twice
  - #898: PK columns were wrong if the table has case sensitive columns 
          and no defined PK
  - #899: PK/UK/FK with specified case sensitive table names
  - #893: removeTabListener fixed
  - #883: ILinkedCellEditor popup size is calculated too small
  - #877: * JVxTable' positioning corrected in case of image or left indent
          * Correct size of editor to fit grid lines
          * Color of indent is wrong
  - #865: AwtContainer add with illegal constraints
  - #864: JVxChart' cancelEditing should be silent ignored
  - #831: JVxDesktopPanel clearFramesCache refactored
  - #653: DBStorage: Meta data couldn't load from database!
  - Correct root data page in case of self joined data book
  - RemoteDataBook: Search columns have wrong referenced columns (not 
    generated from main reference)
  - All AutoLink were via INNER JOIN
  - XmlNode prepares tag names to allow numeric tag names
  
  
[OTHER]  
  - Codereview according to FindBugs suggestions