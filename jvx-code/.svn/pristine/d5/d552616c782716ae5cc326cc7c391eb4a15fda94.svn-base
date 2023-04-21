Changelog: JVx 2.7

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

  - #1868: mark sessions as expired before removing them
  - #1860: CellEditor cache should be configurable
  - #1858: DBAccess needs an easy way to override the metadata 
           where clause
  - #1847: OracleDBAccess: Param support for Struct in 
           executeProcedure and executeFunction
  - #1845: DBAccess: support for x.* in query columns and null dummy 
           columns
  - #1831: IEditor implementation should call ICellFormatter also on 
           IDataRow
  - #1827: MacOS internal frame border problem
  - #1820: createSubSession via SessionContext
  - #1819: Suppress CallBack if session is closed
  - #1812: CallBack shouldn't throw an Exception
  - #1811: openWorkScreen with parameter[]
  - #1799: DirectServerSession creation with additional properties
  - #1778: UIContainer, UIComponent: calculate insets
  - DBAccess.formatParameter introduced
  - <Execute>.disableSend introduced
  - <DefaultObjectProvider>.hasObject introduced
  - Version got an invalid number check
  - Version.toNumber now returns a comparable version number 
    (max 4 digits per group)
  - Version: improved short version representation
  - StringUtil: lpad and rpad now supports a custom fill character
  - ResourceUtil.getThreadClassLoader introduced
  - ResourceUtil.getDefaultClassLoader introduced
  - Session creation error notification
  - TiberoDBAccess implemented
  - DBAccess: boolean support
  - Message: enable access to buttons
  - Fetch cause for caching statements is now detected at a readahead of 500
  - Server: install plugin if enabled attribute is missing in config.xml 
    (old behavior)
  - ImageUtil: re-use image type if possible
  - SimpleJavaSource introduced (simple eval/execute)
  - HttpUtil now with implicite post support (content)
  - REST service now supports ICondition, SortDefinition, ...
  - CodecUtil.encodeURLPart(String, String) introduced, which accepts the 
    encoding
  - <Record>.toString implemented
  - BigDecimalDataType: Use trim scale instead of removeExponent for 
    doubles to avoid 0.0
  - HttpUtil: getETag, getLastModified introduced
  - isDestroyed added to IContent
  - timeout parameter for HttpUtil
  - <AbstractParam>.toString implementation
  - DBAccess: log parameters of procedure/function calls
  - don't set access time in lock/unlock because the master session 
    wouldn't expire
  - SwingTextArea: keep horizontal scroll bar position in text area (optional)
  - SwingApplication: fullscreen support
  - SwingApplication: Add helper, to allow other main classes to simply create 
    the SwingApplication in AWT Thread
  

[BUG]

  - #1874: StringUtil.convertNameToText fails with StringIndexOutOfBounds
  - #1871: DateUtil consumes too much memory
  - #1857: JVxLinkedCellEditor, UIEnumCellEditor: different datatypes 
           have problems
  - #1856: Session expires during upload
  - #1855: WindowClosed event fires twice in the SwingApplication when 
           SystemExit has been disabled
  - #1853: DateUtil fails with certain formats
  - #1852: DBStorage: link references are not set on columns, if 
           autoLinkReference is false
  - #1851: MemDataBook: notifyRepaint, cancelEditing and saveEditing 
           without factory does not work
  - #1849: DBAccess: return value of executeFunction is not set in 
           OutParam
  - #1844: DBAccess: replace * and ? with % and _ also in parameters
  - #1842: Call returns http status 500
  - #1841: DBStorage: parameter in subqueries causes exception
  - #1840: SwingApplication: Fix possible reason for focus problem in 
           login dialog
  - #1829: DBAccess: eventHandler fields may not be static
  - #1824: NullpointerException for internal frames with SynthLookAndFeel
  - #1823: JVxDesktopPane: remove clearFramesCache workaround
  - #1822: ApplicationUtil: restore window bounds does not check, if it is 
           inside the device
  - #1818: OracleDBAccess: double NOWAIT due to new AbstractOracleDBAccess
  - #1815: DateUtil fails with a format with only one date part, like "yyyy"
  - #1806: JVxChoiceCellEditor.convertAllowedValuesToString can cause 
           ArrayStoreException
  - #1805: DirectServerSession not ICloseable
  - #1803: XmlWorker does not correctly read multiline text
  - #1802: Using UIInternalFrame.setModal(true) in two unrelated windows 
           blocks the first
  - #1801: UIInternalFrame.setModal(true) does nothing
  - #1798: DBAccess: possible leak of closing cursor in fetch method
  - #1797: MemDataBook: avoid unnecessary sync of master when detail is 
           closed
  - #1793: AbstractNumberCellEditor, AbstractDateCellEditor: wrong and 
           unchecked format
  - #1790: JVxGridLayout: AIOOBE in case of position outside the grid size
  - #1789: UIComponent: eventKey(Key) does not work
  - #1787: UIContainer: insets are calculated wrong
  - #1786: HttpConnection: Missing getter for setter
  - #1782: MemDataPage: searchPrevious causes unnecessary fetch
  - #1781: JVxChart does not support Strings for the X axis of the bar chart
  - #1776: JVxSplitPane: wrong overrule calculation of minSize
  - #1775: DateUtil: remove java date parser
  - #1774: UIComponent: direct set translationmap is not set on uiComponent
  - #1773: DBStorage: isAutomaticLinkNullable not always correct
  - #1768: Possible memleak
  - #1755: Possible problems with AquaLookAndFeel
  - #1754: NullPointerException when setting properties of JVxTabbedPane with 
           AquaLookAndFeel
  - #1752: DBAccess: wrong query in case or condition and where clause
  - #1751: DBStorage: possible bug apply boolean default value
  - #1750: DBStorage: possible bug default value detection
  - #1747: JVxXXXCellEditor: try to avoid dead lock
  - #1744: Remote call doesn't work as expected (without param)
  - #1743: JVxDesktopPane: modal frame does not restore the focus when closed
  - #1742: DBAccess.findAndCreateReducedCondition(...) does not correctly 
           reduce conditions
  - #1741: DBAccess: lock statement fails in case of substorages with restrict 
           condition
  - #1739: JVxInternalFrame: isSelected wrong in case of tabbed mode		   
  - #1737: NullPointer with dynamic parameters in from clause		   
  - #1735: Quotes in MySQL are set to an empty string		   
  - #1689: PostgreSQL enum problem
  - DBStorage doesn't add empty And conditions anymore
  - AbstractSerializedConnection: decrease communication id even if disabled 
    during call execution
  - Cache in MemDataBooks is not always cleared
  - Server: don't remove response cache for closed sub sessions
  - FileViewer: fixed open command for Fedora
  - Fixed that the textbox containing the error message might be too big in 
    certain technologies 
  - Fixed that labels of the Swing Pie chart would not be translated
  - Fixed that the JVxGridLayout would not calculate the correct preferred size
  - The labels for the PieChart do now contain the percentage
  - JVxChart: Fixed that y column names would not be translated
  - The legend of the JVxChart is now hidden by default if it is a pie chart
  - Fix switching from tabmode to internalframemode does not work
  - JVxDesktopPane: fixed possible NPE in frame mode
  - Fix DataModel for Pie Chart and categories