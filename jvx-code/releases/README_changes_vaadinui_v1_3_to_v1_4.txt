Changelog: VaadinUI 1.4

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

  - #1470: add column name to table-cell style
  - #1462: FontIcon support
  - #1456: Update to vaadin 7.5.7
  - Introduced FileUploadHandler
  - Maximizable Feature (en/disable) support for Windows
  - Performance tuning (LinkedCellEditor, CssExtension)
  - Experimental Grid support
  - Lazy initialization of ChoiceCellEditor images
  - invokeLater handling changed (review, tuning)
  - clean session locking
  - load external css from classpath (jar!...)
  - improved detached check (invokeLater)

[BUG]

  #1542: Memory leak in POST mode
  #1524: VaadinLinkedCellEditor: No data
  #1520: FormLayout sets max-size of component in any case
  #1517: Setting size on editor with image doesn't work
  #1506: NumberEditor doesn't check for number
  #1504: VaadinColor doesn't init color values correctly
  #1501: Creating an image without image name isn't supported
  #1499: Alpha value not recognized for cell format
  #1498: Setting a margin on a layout in UIGroupPanel doesn't work
  #1497: Setting the font of a label doesn't work
  #1496: Setting a background image on a panel doesn't work
  #1484: Editor doesn't update itself after updloading a file
  #1245: ImageViewer too large
