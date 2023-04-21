/*
 * Copyright 2009 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 01.10.2008 - [HM] - creation
 * 18.12.2008 - [JR] - keyPressed: default case -> consume only if editing
 * 05.06.2009 - [JR] - set/getTranslation implemented
 * 21.07.2009 - [JR] - setTranslation: notifyRepaint
 * 21.10.2009 - [JR] - calculateWidthFromData: ExceptionHandler.raise() [BUGFIX]
 * 25.10.2009 - [JR] - getRowCount: catched Exception instead of ModelException and don't throw Exception
 *                     from ExceptionHandler [BUGFIX]
 * 12.03.2010 - [JR] - #87: setBackground overwritten t set the viewports background
 * 06.08.2012 - [JR] - #595: mouse event handling changed (modifiers instead of getButton())
 * 12.09.2012 - [JR] - #598: fixed NPE in getRowCount of DataBookTableModel            
 * 15.09.2012 - [HM] - #599: fixed resize Column smaller than table size is not possible
 * 11.12.2012 - [JR] - setTranslation: removed instance compare because translation could change in same
 *                                     instance!   
 * 25.08.2016 - [JR] - #1675: re-register listeners   
 * 02.09.2016 - [JR] - #1678: createTableCellHandler introduced       
 * 03.04.2019 - [DJ] - #2000: table cell tooltip functionality added                  
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.event.DataBookEvent;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataBookListener;
import javax.rad.model.event.IDataRowListener;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IResource;
import javax.rad.ui.celleditor.IComboCellEditor;
import javax.rad.ui.celleditor.IInplaceCellEditor;
import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.ui.component.IEditable;
import javax.rad.ui.control.INavigationControl;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.SilentAbortException;
import javax.rad.util.TranslationMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxIconRenderer;
import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxRendererContainer;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.ext.format.ICellFormatter;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.tooltip.ICellToolTip;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.ArrayUtil;

/**
 * Scrollable JTable that implements ITableControl interface.
 *  
 * @author Martin Handsteiner
 */
public class JVxTable extends JVxScrollPane 
                      implements ITableControl, 
                                 IEditable,
                      			 INavigationControl,
                           		 ICellFormatterEditorListener,
                                 ListSelectionListener, 
                                 Runnable,
                                 KeyListener,
                                 FocusListener,
                                 IDataBookListener,
                                 IDataRowListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The JTable that shows the IDataBook. */
	private JTable table = new JTable()
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    @Override
		protected boolean processKeyBinding(KeyStroke pKeyStroke, KeyEvent pKeyEvent, int pCondition, boolean pPressed)
		{
			return false;
		}
		
		@Override
		public boolean editCellAt(int pRow, int pColumn, EventObject pEvent)
		{
			return super.editCellAt(pRow, pColumn, pEvent) && getEditorComponent() != null;
		}
		    
		@Override
		public void setShowHorizontalLines(boolean pShowHorizontalLines)
		{
			super.setShowHorizontalLines(pShowHorizontalLines);
			if (pShowHorizontalLines)
			{
				setRowMargin(1);
			}
			else
			{
				setRowMargin(0);
			}
		}

		@Override
		public void setShowVerticalLines(boolean pShowVerticalLines)
		{
			super.setShowVerticalLines(pShowVerticalLines);
			if (pShowVerticalLines)
			{
				getColumnModel().setColumnMargin(1);
			}
			else
			{
				getColumnModel().setColumnMargin(0);
			}
		}
		
		@Override
		public void setColumnModel(TableColumnModel pTableColumnModel)
		{
			super.setColumnModel(pTableColumnModel);
			if (getShowVerticalLines())
			{
				getColumnModel().setColumnMargin(1);
			}
			else
			{
				getColumnModel().setColumnMargin(0);
			}
		}

		@Override
		@SuppressWarnings("deprecation")
		public void doLayout()
		{
			if (autoFillEmptySpace && !isAutoResize())
			{
				TableColumn resizeColumn = getTableHeader().getResizingColumn();
				if (resizeColumn != null) 
				{
					TableColumn column = getColumnModel().getColumn(getColumnModel().getColumnCount() - 1);
					if (column != resizeColumn)
					{
						Dimension size = getViewport().getSize();
						int width = getColumnModel().getTotalColumnWidth();
						if (width < size.width)
						{
							column.setWidth(column.getWidth() + size.width - width);
						}
						else if (column.getWidth() > lastColumnWidth)
						{
							column.setWidth(Math.max(lastColumnWidth, column.getWidth() + size.width - width));
						}
						else
						{
							layout();
							return;
						}
					}
				}
		    }
			super.doLayout();
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			Dimension pref = super.getPreferredSize();
			if (autoFillEmptySpace && !isAutoResize() && calculateMinSize && getViewport() != null) // && (event == null || event.getID() != MouseEvent.MOUSE_DRAGGED))
			{
				Dimension size = getViewport().getSize();
				TableColumn resizeColumn = getTableHeader().getResizingColumn();
				if (resizeColumn != null) 
				{
					TableColumn column = getColumnModel().getColumn(getColumnModel().getColumnCount() - 1);
					if (resizeColumn != column && column.getWidth() > lastColumnWidth)
					{
						pref.width += lastColumnWidth - column.getWidth();
					}
				}
				if (size.width > pref.width)
				{
					pref.width = size.width;
				}
				if (size.height > pref.height)
				{
					pref.height = size.height;
				}
			}
			return pref;
		}
		
		@Override
		public void setUI(TableUI pUI) 
		{
            if (this.ui != pUI) 
	        {
                //#1675
                //ORDER matters, because setUI may add listeners.
                //
                //especially the mouse listener is important, because our listener uses the selected table row and
                //the UI mouse listener selects the correct row. If our listener fires first, the selected row is 
                //wrong
                removeKeyListener(JVxTable.this);
                removeMouseListener(JVxTable.this);
                removeFocusListener(JVxTable.this);

                addKeyListener(JVxTable.this);	 // add Listeners before UI, to ensure row is selected
                addMouseListener(JVxTable.this); // this should ensure, that isCellEditable is called after row
                addFocusListener(JVxTable.this); // is selected.

                super.setUI(pUI);
	        }
	    }
	};
	
	/** The IDataBook to be shown. */
	private IDataBook dataBook = null;

	/** The column view. */
	private ColumnView columnView = null;
	
	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The cellTooltip. */
	private ICellToolTip cellToolTip = null;
	
	/** The used CellEditor. */
	private ICellEditorHandler<JComponent> cellEditorHandler = null;
	
	/** The translation mapping. */
	private TranslationMap translation = null;
	
    /** KeyEvent from FocusListener. The focus key event is available on key typed until key released. */
    private static KeyEvent focusKeyEvent = null;

    /** The row height. Automatic row height is set with -1. */
	private int rowHeight = -1;

    /** The row height. */
    private boolean calculateHeightAndWidth = true;

    /** Ignore scroll position after reload to fetch rows. */
    private boolean ignoreScrollPositionAfterReload = false;

	/** The minimal row height. */
	private int minRowHeight = SwingFactory.isMacLaF() ? 23 : 20;
	
	/** The minimal row height. */
	private int maxRowHeight = 120;
	
    /** The enter navigation mode. */
    private int enterNavigationMode = NAVIGATION_CELL_AND_FOCUS;
    
    /** The enter navigation mode. */
    private int tabNavigationMode = NAVIGATION_CELL_AND_FOCUS;

    /** lastColumnWidth. */
    private int lastColumnWidth = 0;

    /** True, if sort on header click is enabled. */
	private boolean sortOnHeaderEnabled = true;
	
	/** True, empty space is automatic filled with existing columns. */
	private boolean autoFillEmptySpace = true;
	
	/** True, if the editable is shown. */
	private boolean editable = true;
	
	/** True, if the selection is shown. */
	private boolean showSelection = true;
	
	/** True, if the focus rect is shown. */
	private boolean showFocusRect = true;
	
	/** Tells, if notifyRepaint is called the first time. */
	private boolean firstNotifyRepaintCall = true;
	
	/** Cell Editor started editing. */
	private boolean editingStarted = false;
	
	/** Mouse pressed. */
	private boolean correctMouseSelection = false;

	/** Ignoring Events. */
	private boolean ignoreEvent = false;
	/** Ignoring Events. */
	private boolean ignoreMousePressed = false;
	
	/** DataBook is out of sync. Used for doRepaint to detect reload. */
	private boolean outOfSync = false;
	
	/** is notified. */
	private boolean isNotified = false;
	
	/** calculateMinSize. */
	private boolean calculateMinSize = true;
	
	/** true, if the mouse event is on selected cell. */
	private boolean mouseEventOnSelectedCell = false;

    /** Ignoring Events. */
    private boolean ignoreFocusEvent = false;
	
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			public boolean dispatchKeyEvent(KeyEvent pKeyEvent)
			{
				if (pKeyEvent.getID() == KeyEvent.KEY_TYPED)
				{
					focusKeyEvent = pKeyEvent;
				}
				else
				{
					focusKeyEvent = null;
				}
				return false;
			}
		});
	}
	
	/** 
	 * Constructs a JVxTable.
	 */
	public JVxTable()
	{
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusTraversalKeysEnabled(false);

		setViewportView(table);
		installMouseListenerToGainFocus();
		
		setBackground(table.getBackground());
		
		configureTableHeader();
		setAutoResize(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //RUNNABLE
    
	/**
	 * The run method is invoked from AWT EventQueue. 
	 * It enables events from the model again. 
	 * Due to performance reasons the events are disabled from the first call of
	 * notifyRepaint until the EventQueue calls the run method. 
	 * This minimizes the repaints of the control. 
	 */
	public void run()
	{
		firstNotifyRepaintCall = true;

		if (isNotified)
		{
			doRepaint();
		}
	}
	
    //LISTSELECTIONLISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent pMouseEvent)
	{
		if (!ignoreMousePressed && pMouseEvent.getSource() == table)
		{
			correctMouseSelection = true;
			
			if (!ignoreEvent && firstNotifyRepaintCall && dataBook != null && dataBook.isOpen()) 
			{
				ignoreEvent = true;
				try 
				{
					Point point = pMouseEvent.getPoint();
					int row = table.rowAtPoint(point);
					
                    if (row >= 0)
                    {
                        mouseEventOnSelectedCell = true;
                        
                        dataBook.setSelectedRow(row);

                        if (row != table.getSelectedRow())
                        {
                            table.getSelectionModel().clearSelection();
                            table.setRowSelectionInterval(row, row);
                        }

                        if (SwingUtilities.isRightMouseButton(pMouseEvent))
    					{
							int col = table.columnAtPoint(point);
							if (col >= 0)
							{
								dataBook.setSelectedColumn((String)table.getColumnModel().getColumn(col).getIdentifier());
							}
    					}
                    }
					repaint();
					ignoreEvent = false;
				}
				catch (Throwable pThrowable)
				{
					ignoreEvent = false;
					notifyRepaint();
					
					ExceptionHandler.raise(pThrowable);
				}
			}
		}
		super.mousePressed(pMouseEvent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pMouseEvent)
	{
		correctMouseSelection = false;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				mouseEventOnSelectedCell = false;
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(ListSelectionEvent pListSelectionEvent)
	{
		if (!ignoreEvent)
		{
			ignoreEvent = true;
			
			try 
			{
				if (EventQueue.getCurrentEvent() instanceof MouseEvent
						|| EventQueue.getCurrentEvent() instanceof InvocationEvent)
				{
					if (correctMouseSelection)
					{
						try 
						{
							int sel = dataBook.getSelectedRow();
							
							if (sel != table.getSelectedRow())
							{
								table.getSelectionModel().clearSelection();
								if (sel >= 0)
								{
									table.setRowSelectionInterval(sel, sel);
								}
							}
						}
						catch (Throwable pThrowable)
						{
							notifyRepaint();
							
							ExceptionHandler.raise(pThrowable);
						}
					}
				}
				else
				{
					dataBook.setSelectedRow(table.getSelectedRow());
				}
				ignoreEvent = false;
			}
			catch (Throwable pThrowable)
			{
				ignoreEvent = false;
				notifyRepaint();
				
				ExceptionHandler.raise(pThrowable);
			}
		}
	}

	//ITABLECONTROL

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (!ignoreEvent && !editingStarted)
		{
			if (!outOfSync && dataBook != null && dataBook.isOutOfSync())
			{
				outOfSync = true;
			}
			if (firstNotifyRepaintCall) // Check additionally if editing is started, to prevent immediate closing editor
			{
				firstNotifyRepaintCall = false;
				
				JVxUtil.invokeLater(this);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEditing()
	{
		if (cellEditorHandler == null) 
		{
		    try
		    {
		        table.editCellAt(dataBook.getSelectedRow(), getColumnView().getColumnNameIndex(getDataBook().getSelectedColumn()));
		    }
		    catch (Exception ex)
		    {
		        // Ignore
		    }
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		if (cellEditorHandler != null) 
		{
			editingStarted = false;

			cellEditorHandler.uninstallEditor();
			cellEditorHandler = null;
			table.editingCanceled(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		if (editingStarted)
		{
			// Avoid recursion, if DataRowListener stores again.
			editingStarted = false;

			cellEditorHandler.saveEditing();
		}
		cancelEditing();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (translation != pTranslation)
    	{
	    	translation = pTranslation;
	    	
	    	try 
	    	{
				saveEditing();
			} 
	    	catch (Throwable e) 
	    	{
				cancelEditing();
			}
	
	    	notifyRepaint();
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public TranslationMap getTranslation()
    {
    	return translation;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }

	//KeyListener
	
	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent pKeyEvent)
	{
		if (!pKeyEvent.isConsumed())
		{
			try
			{
				switch (pKeyEvent.getKeyCode())
				{
					case KeyEvent.VK_ENTER:
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();
							if (pKeyEvent.isShiftDown())
							{
								selectPrevious(enterNavigationMode);
							}
							else
							{
								selectNext(enterNavigationMode);
							}
						}
						break;
					case KeyEvent.VK_TAB: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();
							if (pKeyEvent.isShiftDown())
							{
								selectPrevious(tabNavigationMode);
							}
							else
							{
								selectNext(tabNavigationMode);
							}
						}
				        break;
					case KeyEvent.VK_PAGE_UP: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();

							ignoreEvent = true;
							selectPreviousPage(false);
						}
						break;
					case KeyEvent.VK_PAGE_DOWN: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();

							ignoreEvent = true;
							selectNextPage(false);
						}
						break;
					case KeyEvent.VK_UP: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();
							
							ignoreEvent = true;
							selectPreviousRow(false);
						}
						break;
					case KeyEvent.VK_DOWN: 
						if (pKeyEvent.isAltDown())
						{
							startEditing();
							openComboBox();
						}
						else if (!pKeyEvent.isControlDown())
						{
							pKeyEvent.consume();
							
							ignoreEvent = true;
							selectNextRow(false);
						}
						break;
					case KeyEvent.VK_LEFT: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();
							selectPreviousCell(false);
						}
						break;
					case KeyEvent.VK_RIGHT: 
						if (!pKeyEvent.isControlDown() && !pKeyEvent.isAltDown())
						{
							pKeyEvent.consume();
							selectNextCell(false);
						}
						break;
					case KeyEvent.VK_F2: 
						pKeyEvent.consume();
						startEditing();
						break;
					case KeyEvent.VK_INSERT: 
						pKeyEvent.consume();
						if (dataBook.isInsertAllowed())
						{
							dataBook.insert(false);
						}
						break;
					case KeyEvent.VK_DELETE:
						pKeyEvent.consume();
						if (dataBook.isDeleteAllowed())
						{
							dataBook.delete();
						}
						break;
					case KeyEvent.VK_ESCAPE: 
						if (dataBook.isUpdating() || dataBook.isInserting() || dataBook.isDeleting())
						{
							pKeyEvent.consume();
							dataBook.restoreSelectedRow();
						}
						break;
				    default:
				    	int row = table.getSelectedRow();
				    	int column = table.getSelectedColumn();
				    	if (row >= 0 && column >= 0)
				    	{
				    		char ch = pKeyEvent.getKeyChar();
					    	if (ch >= 0x20 && ch < 0xffff && ch != 0x7f && !pKeyEvent.isAltDown())
					    	{
								pKeyEvent.consume();

								table.editCellAt(table.getSelectedRow(), table.getSelectedColumn(), pKeyEvent);
					    	}
				    	}
				}
			}
			catch (Throwable pThrowable)
			{
				ExceptionHandler.raise(pThrowable);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent pKeyEvent)
	{
		if (ignoreEvent)
		{
			try
			{
				ignoreEvent = false; // Force a notify repaint, to ensure that state changes after setSelectedRow will be displayed correctly.
				dataBook.setSelectedRow(table.getSelectedRow());
			}
			catch (Throwable pThrowable)
			{
				ignoreEvent = false;
				notifyRepaint();
				
				ExceptionHandler.raise(pThrowable);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent pKeyEvent)
	{
	}
	
	//FocusListener
	
	/**
	 * {@inheritDoc}
	 */
	public void focusGained(FocusEvent pFocusEvent)
	{
		if (!ignoreFocusEvent && focusKeyEvent != null && table.getColumnModel().getColumnCount() > 0)
		{
			if ((focusKeyEvent.getKeyChar() == KeyEvent.VK_TAB && tabNavigationMode == NAVIGATION_CELL_AND_FOCUS)
					|| (focusKeyEvent.getKeyChar() == KeyEvent.VK_ENTER && enterNavigationMode == NAVIGATION_CELL_AND_FOCUS))
			{
				if (focusKeyEvent.isShiftDown())
				{
					table.setColumnSelectionInterval(table.getColumnCount() - 1, table.getColumnCount() - 1);
				}
				else
				{
					table.setColumnSelectionInterval(0, 0);
				}

				scrollToSelectedCell();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusLost(FocusEvent pFocusEvent)
	{
		ignoreFocusEvent = false;
	}

	//IDataBookListener
	
	/**
	 * {@inheritDoc}
	 */
	public void dataBookChanged(DataBookEvent pDataBookEvent)
	{
		calculateHeightAndWidth = true;
		
		ignoreScrollPositionAfterReload = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void valuesChanged(DataRowEvent pDataRowEvent)
	{
		calculateHeightAndWidth = true;
	}
	
	//ICellEditorListener
	
	/**
	 * {@inheritDoc}
	 */
	public void editingStarted() 
	{
		try
		{
			editingStarted = true; // first set editingStarted true, to prevent events on update.

			IDataRow oldDataRow = dataBook.createDataRow(null);
			
			dataBook.update();
			
			if (cellEditorHandler == null || !oldDataRow.equals(dataBook, new String[] {cellEditorHandler.getColumnName()})) // Only if value is changed, cancel editing.
			{
				editingStarted = false;
				notifyRepaint();
			}
		}
		catch (Throwable pThrowable)
		{
			editingStarted = false;
			notifyRepaint();
			ExceptionHandler.raise(pThrowable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void editingComplete(String pCompleteType) 
	{
		ignoreFocusEvent = true;

		if (pCompleteType == ICellEditorListener.ESCAPE_KEY)
		{
			cancelEditing();
			if (!table.hasFocus())
			{
				table.requestFocus();
			}
		}
		else
		{
			try
			{
				saveEditing();
			}
			catch (Throwable ex)
			{
				cancelEditing();
				if (!table.hasFocus())
				{
					table.requestFocus();
				}
				
				ExceptionHandler.raise(ex);
			}

			if (pCompleteType == ICellEditorListener.ENTER_KEY)
			{
				selectNext(enterNavigationMode);
			}
			else if (pCompleteType == ICellEditorListener.SHIFT_ENTER_KEY)
			{
				selectPrevious(enterNavigationMode);
			}
			else if (pCompleteType == ICellEditorListener.TAB_KEY)
			{
				selectNext(tabNavigationMode);
			}
			else if (pCompleteType == ICellEditorListener.SHIFT_TAB_KEY)
			{
				selectPrevious(tabNavigationMode);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSavingImmediate() 
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IControl getControl()
	{
		return this;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();

		if (dataBook != null)
		{
			dataBookChanged(null);								       // This forces the column model to recalculate column width. 
			dataBook.eventAfterReload().addInternalListener(this);     // All changes where not recognized, because no data book listener was added, 
	        dataBook.eventValuesChanged().addInternalListener(this);   // when this table was not notified.
		}
		
		ignoreEvent = true;
		table.scrollRectToVisible(new Rectangle());
		table.getSelectionModel().clearSelection();
		ignoreEvent = false;
		
		doRepaint(); // ensure correct selected rows immediate.
		
		isNotified = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		isNotified = false;

		if (dataBook != null)
		{
			dataBook.eventAfterReload().removeInternalListener(this);
	        dataBook.eventValuesChanged().removeInternalListener(this);
		}

		super.removeNotify();

		mouseReleased(null);
		
		configureTableHeader();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		calculateMinSize = false;
		try
		{
			return super.getPreferredSize();
		}
		finally
		{
			calculateMinSize = true;
		}
	}

	/**
	 * Sets the background of the table and the viewport.
	 * 
	 * @param pColor the background color
	 */
	@Override
	public void setBackground(Color pColor)
	{
		super.setBackground(pColor);

		if (table != null)
		{
			table.setBackground(pColor);
			
			getViewport().setBackground(pColor);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnabled)
	{
		super.setEnabled(pEnabled);

		if (table != null)
		{
			table.setEnabled(pEnabled);
			
			if (isEnabled())
			{
				getViewport().setBackground(table.getBackground());
			}
			else
			{
				getViewport().setBackground(JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
			}
		}
	}
	
	/**
     * {@inheritDoc}
     */
    @Deprecated
    public void reshape(int pX, int pY, int pWidth, int pHeight) 
    {
		super.reshape(pX, pY, pWidth, pHeight);
		if (!isAutoResize())
		{
			doLayout();
	    }
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * True, if this component is added in the component tree of a viewable root container (window or applet). 
     * @return True, if this component is added in the component tree of a viewable root container (window or applet). 
     */
    public boolean isNotified()
    {
    	return isNotified;
    }
    
    /**
     * True, if events should be ignored. 
     * @return True, if events should be ignored.  
     */
    protected boolean isIgnoreEvent()
    {
    	return ignoreEvent;
    }
    
    /**
     * True, if events should be ignored.
     * @param pIgnoreEvent True, if events should be ignored.  
     */
    protected void setIgnoreEvent(boolean pIgnoreEvent)
    {
    	ignoreEvent = pIgnoreEvent;
    }
    
	/**
	 * Configures the TableHeader, to guarantee the correct preferred size.
	 */
	private void configureTableHeader()
	{
        setColumnHeaderView(table.getTableHeader());
        Border border = getBorder();
        if (border == null || border instanceof UIResource) 
        {
            setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        }
	}

	/**
     * Returns the DataBook displayed by this control.
     *
     * @return the DataBook.
     * @see #setDataBook
     */
    public IDataBook getDataBook()
    {
    	return dataBook;
    }

    /**
     * Sets the DataBook displayed by this control.
     * 
	 * @param pDataBook the DataBook
     * @see #getDataBook
     */
    public void setDataBook(IDataBook pDataBook)
    {
    	if (pDataBook != dataBook)
    	{
    		if (dataBook != null)
    		{
    	        dataBook.removeControl(this);
    	        if (isNotified())
    	        {
	    	        dataBook.eventAfterReload().removeInternalListener(this);
	    	        dataBook.eventValuesChanged().removeInternalListener(this);
    	        }
    	        
    	        // It has to be this order to avoid exceptions in the default models.
    			table.getSelectionModel().removeListSelectionListener(this);
    	        table.setColumnModel(new DefaultTableColumnModel());
    			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			table.setAutoCreateColumnsFromModel(true);
    	        table.setModel(new DefaultTableModel());
    	        
        		if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
        		{
        			columnView.removeRowDefinition(dataBook.getRowDefinition());
        		}
    		}
    		
    		dataBook = pDataBook;
    		
    		if (dataBook != null)
    		{
    	        table.setModel(new DataBookTableModel(this, dataBook));
    			table.setAutoCreateColumnsFromModel(false);
    	        table.setColumnModel(new DataBookTableColumnModel(this));
    			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			table.getSelectionModel().addListSelectionListener(this);
    			
    			// Fields moved from model to global class, so we have to reinitialize them, as they would be inside new model.
    			ignoreScrollPositionAfterReload = false;  
    			calculateHeightAndWidth = true;
    			
    			dataBook.addControl(this);
    	        if (isNotified())
    	        {
	    			dataBook.eventAfterReload().addInternalListener(this);
	    	        dataBook.eventValuesChanged().addInternalListener(this);
    	        }

        		if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
        		{
        			columnView.addRowDefinition(dataBook.getRowDefinition());
        		}
    		}
    		notifyRepaint();
    	}
    }
	
    /**
     * Returns the {@link ColumnView} displayed by this control. If <code>null</code> is set, 
     * the default column view from the data book is returned.
     *
     * @return the column view.
     * @see #setColumnView
     */
    public ColumnView getColumnView()
    {
		if (columnView == null && dataBook != null)
		{
			return dataBook.getRowDefinition().getColumnView(ITableControl.class);
		}
		else
		{
			return columnView;
		}
    }

    /**
     * Sets the ColumnView displayed by this control.
     * 
	 * @param pColumnView the column view
     * @see #getColumnView
     */
    public void setColumnView(ColumnView pColumnView)
    {
    	if (columnView != pColumnView)
    	{
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.removeRowDefinition(dataBook.getRowDefinition());
    		}
    		
        	columnView = pColumnView;
    		
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.addRowDefinition(dataBook.getRowDefinition());
    		}
    		notifyRepaint();
    	}
    }
	
	/**
     * Gets the {@link ICellFormatter}.
     *
     * @return the cell formatter.
     * @see #setCellFormatter
     */
    public ICellFormatter getCellFormatter()
    {
    	return cellFormatter;
    }

    /**
     * Sets the {@link ICellFormatter}.
     * 
	 * @param pCellFormatter cell formatter
     * @see #getCellFormatter
     */
    public void setCellFormatter(ICellFormatter pCellFormatter)
    {
    	cellFormatter = pCellFormatter;
    }
    
    /**
     * Gets the {@link ICellToolTip}.
     *
     * @return the cell tooltip.
     * @see #setCellToolTip
     */
	public ICellToolTip getCellToolTip()
	{
		return cellToolTip;
	}

	/**
     * Sets the {@link ICellToolTip}.
     * 
	 * @param pCellToolTip cell tooltip
     * @see #getCellToolTip
     */
	public void setCellToolTip(ICellToolTip pCellToolTip)
	{
		cellToolTip = pCellToolTip;
	}

	/**
     * Gets the visibility of the table header.
     *
     * @return the visibility of the table header.
     */
    public boolean isTableHeaderVisible()
    {
    	return getColumnHeader().isVisible();
    }

	/**
     * Sets the visibility of the table header.
     *
     * @param pTableHeaderVisible the visibility of the table header.
     */
    public void setTableHeaderVisible(boolean pTableHeaderVisible)
    {
    	getColumnHeader().setVisible(pTableHeaderVisible);
    }
	
	/**
     * Gets true, if it is possible sorting the data by clicking on the header.
     *
     * @return true, if it is possible sorting the data by clicking on the header.
     */
    public boolean isSortOnHeaderEnabled()
	{
		return sortOnHeaderEnabled;
	}

	/**
     * Set true, if it should possible sorting the data by clicking on the header.
     *
     * @param pSortOnHeaderEnabled true, if it is possible sorting the data by clicking on the header.
     */
    public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
		sortOnHeaderEnabled = pSortOnHeaderEnabled;
	}

	/**
     * Gets true, empty space is automatically filled with existing columns.
     *
     * @return true, empty space is automatically filled with existing columns.
     */
    public boolean isAutoFillEmptySpace()
	{
		return autoFillEmptySpace;
	}

	/**
     * Set true, empty space is automatically filled with existing columns.
     *
     * @param pAutoFillEmptySpace true, empty space is automatically filled with existing columns.
     */
    public void setAutoFillEmptySpace(boolean pAutoFillEmptySpace)
	{
    	autoFillEmptySpace = pAutoFillEmptySpace;
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getEnterNavigationMode()
	{
		return enterNavigationMode;
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setEnterNavigationMode(int pNavigationMode)
	{
    	enterNavigationMode = pNavigationMode;
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getTabNavigationMode()
	{
		return tabNavigationMode;
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setTabNavigationMode(int pNavigationMode)
	{
    	tabNavigationMode = pNavigationMode;
	}

	/**
     * Get the row height. Automatic row height is set with -1.
     *
     * @return the row height.
     */
    public int getRowHeight()
	{
		return rowHeight;
	}

	/**
     * Set the row height. Automatic row height is set with -1.
     *
     * @param pRowHeight the row height.
     */
    public void setRowHeight(int pRowHeight)
	{
    	rowHeight = pRowHeight;
    	
    	calculateHeightAndWidth = true;
    	notifyRepaint();
	}

	/**
     * Gets the minimal row height.
     *
     * @return the minimal row height.
     */
    public int getMinRowHeight()
	{
		return minRowHeight;
	}

	/**
     * Set the minimal row height.
     *
     * @param pMinRowHeight the minimal row height.
     */
    public void setMinRowHeight(int pMinRowHeight)
	{
    	minRowHeight = pMinRowHeight;

    	calculateHeightAndWidth = true;
        notifyRepaint();
	}

	/**
     * Gets the maximal row height.
     *
     * @return the maximal row height.
     */
    public int getMaxRowHeight()
	{
		return maxRowHeight;
	}

	/**
     * Set the maximal row height.
     *
     * @param pMaxRowHeight the maximal row height.
     */
    public void setMaxRowHeight(int pMaxRowHeight)
	{
    	maxRowHeight = pMaxRowHeight;

    	calculateHeightAndWidth = true;
        notifyRepaint();
	}

    /**
     * Gets the JTable used by JVxTable.
     * @return the JTable used by JVxTable.
     */
    public JTable getJTable()
    {
    	return table;
    }
    
    /**
     * Gets true, if the JVxTable is in auto resize mode.
     * @return true, if the JVxTable is in auto resize mode.
     */
    public boolean isAutoResize()
    {
    	return table.getAutoResizeMode() != JTable.AUTO_RESIZE_OFF;
    }
    
    /**
     * Sets true, if the JVxTable is in auto resize mode.
     * @param pAutoResize true, if the JVxTable is in auto resize mode.
     */
    public void setAutoResize(boolean pAutoResize)
    {
    	if (pAutoResize)
    	{
        	table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    	}
    	else
    	{
        	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	}
    }
    
	/** 
	 * The current used CellEditor for editing. 
	 * @return The current used CellEditor for editing.
	 */
	public ICellEditorHandler<JComponent> getCellEditorHandler()
	{
		return cellEditorHandler;
	}
	
	/** 
	 * Sets the current used ICellEditorHandler for editing.
	 * This functionality is internal. It is necessary, because the underlaying
	 * JTable calls, the functions for start editing.
	 *  
	 * @param pCellEditorHandler the current used ICellEditorHandler for editing.
	 */
	protected void setCellEditorHandler(ICellEditorHandler pCellEditorHandler)
	{
		cellEditorHandler = pCellEditorHandler;
	}
	
	/**
	 * Scrolls the selected Cell in the visible Region of the JVxTable.
	 */
	public void scrollToSelectedCell()
	{
		doRepaint();

		table.scrollRectToVisible(table.getCellRect(Math.max(0, table.getSelectedRow()), Math.max(0, table.getSelectedColumn()), true));
	}
	
	/**
	 * Opens the combo box, if possible.
	 */
	public void openComboBox()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						if (cellEditorHandler != null && cellEditorHandler.getCellEditorComponent() instanceof JVxComboBase
								&& !((JVxComboBase)cellEditorHandler.getCellEditorComponent()).isPopupVisible())
						{
							((JVxComboBase)cellEditorHandler.getCellEditorComponent()).showPopup();
						}
					}
				});
			}
		});
	}

	
	/**
	 * Selects the next.
	 * @param pNavigationMode the navigation mode.
	 */
	public void selectNext(int pNavigationMode)
	{
		if (pNavigationMode == NAVIGATION_CELL_AND_FOCUS)
		{
			selectNextCell(true);
		}
		else if (pNavigationMode == NAVIGATION_ROW_AND_FOCUS)
		{
			selectNextRow(true);
		}
		else if (pNavigationMode == NAVIGATION_CELL_AND_ROW_AND_FOCUS)
		{
			selectNextCellAndRow(true);
		}
	}
	
	/**
	 * Selects the next.
	 * @param pNavigationMode the navigation mode.
	 */
	public void selectPrevious(int pNavigationMode)
	{
		if (pNavigationMode == NAVIGATION_CELL_AND_FOCUS)
		{
			selectPreviousCell(true);
		}
		else if (pNavigationMode == NAVIGATION_ROW_AND_FOCUS)
		{
			selectPreviousRow(true);
		}
		else if (pNavigationMode == NAVIGATION_CELL_AND_ROW_AND_FOCUS)
		{
			selectPreviousCellAndRow(true);
		}
	}
	
	/**
	 * Selects the next cell.
	 * if pDelegateFocus is true, after the last cell the next component is focused.
	 * @param pDelegateFocus if true, after the last cell the next component is focused.
	 */
	public void selectNextCell(boolean pDelegateFocus)
	{
		int selectedColumn = table.getSelectedColumn() + 1;
		if (selectedColumn < table.getColumnCount() && table.getSelectedRow() >= 0)
		{
			table.setColumnSelectionInterval(selectedColumn, selectedColumn);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(table);
		}
	}
	
	/**
	 * Selects the previous cell.
	 * if pDelegateFocus is true, after the first cell the previous component is focused.
	 * @param pDelegateFocus if true, after the first cell the previous component is focused.
	 */
	public void selectPreviousCell(boolean pDelegateFocus)
	{
		int selectedColumn = table.getSelectedColumn() - 1;
		if (selectedColumn >= 0 && table.getSelectedRow() >= 0)
		{
			table.setColumnSelectionInterval(selectedColumn, selectedColumn);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(table);
		}
	}
	
	/**
	 * Selects the next cell and row.
	 * if pDelegateFocus is true, after the last cell the next component is focused.
	 * @param pDelegateFocus if true, after the last cell the next component is focused.
	 */
	public void selectNextCellAndRow(boolean pDelegateFocus)
	{
		int selectedColumn = table.getSelectedColumn() + 1;
		int selectedRow = table.getSelectedRow() + 1;
		if (selectedColumn < table.getColumnCount() && table.getSelectedRow() >= 0)
		{
			table.setColumnSelectionInterval(selectedColumn, selectedColumn);
			scrollToSelectedCell();
		}
		else if (selectedRow < table.getRowCount())
		{
			table.setRowSelectionInterval(selectedRow, selectedRow);
			table.setColumnSelectionInterval(0, 0);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(table);
		}
	}
	
	/**
	 * Selects the previous cell and row.
	 * if pDelegateFocus is true, after the first cell the previous component is focused.
	 * @param pDelegateFocus if true, after the first cell the previous component is focused.
	 */
	public void selectPreviousCellAndRow(boolean pDelegateFocus)
	{
		int selectedColumn = table.getSelectedColumn() - 1;
		int selectedRow = table.getSelectedRow() - 1;
		if (selectedColumn >= 0 && table.getSelectedRow() >= 0)
		{
			table.setColumnSelectionInterval(selectedColumn, selectedColumn);
			scrollToSelectedCell();
		}
		else if (selectedRow >= 0)
		{
			table.setRowSelectionInterval(selectedRow, selectedRow);
			table.setColumnSelectionInterval(table.getColumnCount() - 1, table.getColumnCount() - 1);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(table);
		}
	}
	
	/**
	 * Selects the next row.
	 * if pDelegateFocus is true, after the last row the next component is focused.
	 * @param pDelegateFocus if true, after the last row the next component is focused.
	 */
	public void selectNextRow(boolean pDelegateFocus)
	{
		int selectedRow = table.getSelectedRow() + 1;
		if (selectedRow < table.getRowCount())
		{
			table.setRowSelectionInterval(selectedRow, selectedRow);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(table);
		}
	}
	
	/**
	 * Selects the previous cell.
	 * if pDelegateFocus is true, after the first cell the previous component is focused.
	 * @param pDelegateFocus if true, after the first cell the previous component is focused.
	 */
	public void selectPreviousRow(boolean pDelegateFocus)
	{
		int selectedRow = table.getSelectedRow() - 1;
		if (selectedRow >= 0)
		{
			table.setRowSelectionInterval(selectedRow, selectedRow);
			scrollToSelectedCell();
			table.revalidate();
			table.repaint();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(table);
		}
	}
	
	/**
	 * Gets the number of rows per page.
	 * @return the number of rows per page.
	 */
	private int getNumberOfRowsPerPage()
	{
		if (table.getParent() instanceof JViewport)
		{
			Rectangle viewRect = ((JViewport)table.getParent()).getViewRect();

			return Math.max(1, (viewRect.height - 1) / table.getRowHeight());
		}
		else
		{
			Rectangle viewRect = table.getBounds();
			
			return Math.max(1, (viewRect.height - 1) / table.getRowHeight());
		}
	}
	
	/**
	 * Selects the next row.
	 * if pDelegateFocus is true, after the last row the next component is focused.
	 * @param pDelegateFocus if true, after the last row the next component is focused.
	 */
	public void selectNextPage(boolean pDelegateFocus)
	{
		int selectedRow = table.getSelectedRow();
		if (selectedRow < table.getRowCount() - 1)
		{
			selectedRow += getNumberOfRowsPerPage();
			if (selectedRow >= table.getRowCount())
			{
				selectedRow = table.getRowCount() - 1;
			}
			table.setRowSelectionInterval(selectedRow, selectedRow);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(table);
		}
	}
	
	/**
	 * Selects the previous cell.
	 * if pDelegateFocus is true, after the first cell the previous component is focused.
	 * @param pDelegateFocus if true, after the first cell the previous component is focused.
	 */
	public void selectPreviousPage(boolean pDelegateFocus)
	{
		int selectedRow = table.getSelectedRow();
		if (selectedRow > 0)
		{
			selectedRow -= getNumberOfRowsPerPage();
			if (selectedRow < 0)
			{
				selectedRow = 0;
			}
			table.setRowSelectionInterval(selectedRow, selectedRow);
			scrollToSelectedCell();
		}
		else if (pDelegateFocus)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(table);
		}
	}
	
    /**
     * Repaints the table and updates the selection.
     */
    protected void doRepaint()
    {
    	if (!ignoreEvent && dataBook != null && dataBook.isOpen())
    	{
			ignoreEvent = true;
	    	try
	    	{
	    		table.getRowCount(); // Ensure Wait Cursor for fetch!
		    	((DataBookTableColumnModel)table.getColumnModel()).initTableColumns(true);

		    	int sel = dataBook.getSelectedRow();
		    	
	    		if (sel != table.getSelectedRow())
				{
					table.getSelectionModel().clearSelection();
					if (sel >= 0)
					{
						table.setRowSelectionInterval(sel, sel);
					}
					if (table.getColumnCount() > 0 && (dataBook.isInserting() ||  table.getSelectedColumn() < 0))
					{
						table.revalidate();

						table.setColumnSelectionInterval(0, 0);
					}
					outOfSync = true;
				}
	    		if (outOfSync)
	    		{
	    			outOfSync = false;
					scrollToSelectedCell();
	    		}
	    		ignoreScrollPositionAfterReload = false;

	    		if (isNotified)
	    		{
					table.revalidate();
					table.repaint();
					table.getTableHeader().resizeAndRepaint();
					
					JVxUtil.revalidateAllDelayed(this);
	    		}
				ignoreEvent = false;
	    	}
	    	catch (Exception pException)
	    	{
				ignoreEvent = false;
				ExceptionHandler.raise(pException);
	    	}
    	}
    }
    
    /**
     * {@inheritDoc}
     */
	public String translate(String pText)
	{
		if (bTranslationEnabled && translation != null)
		{
			return translation.translate(pText);
		}
		else
		{
			return pText;
		}
	}

	/**
	 * Gets if editable or not.
	 * 
	 * @return if editable or not.
	 */
	public boolean isEditable()
	{
		return editable;
	}

	/**
	 * Sets if editable or not.
	 * 
	 * @param pEditable if editable or not.
	 */
	public void setEditable(boolean pEditable)
	{
		editable = pEditable;
	}
    
	/**
	 * Gets if showing the selection or not.
	 * 
	 * @return showing the selection or not.
	 */
	public boolean isShowSelection()
	{
		return showSelection;
	}

	/**
	 * Sets if showing the selection or not.
	 * 
	 * @param pShowSelection showing the selection or not.
	 */
	public void setShowSelection(boolean pShowSelection)
	{
		showSelection = pShowSelection;
		repaint();
	}
    
	/**
	 * Gets if showing the focus rect or not.
	 * 
	 * @return showing the focus rect or not.
	 */
	public boolean isShowFocusRect()
	{
		return showFocusRect;
	}

	/**
	 * Sets if showing the focus rect or not.
	 * 
	 * @param pShowFocusRect showing the focus rect or not.
	 */
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		showFocusRect = pShowFocusRect;
		repaint();
	}

	/**
	 * True, if the mouse event occured on current selected cell.
	 * 
	 * @return True, if the mouse event occured on current selected cell.
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return mouseEventOnSelectedCell;
	}

	/**
	 * Creates the generic cell handler for a specific column.
	 * 
	 * @param pColumnName the name of the column
	 * @return the cell handler
	 */
	protected GenericTableCellHandler createTableCellHandler(String pColumnName)
	{
	    return new GenericTableCellHandler(this, pColumnName);
	}	
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Implements the TableModel interfaces with the given IDataBook. 
	 */
	public static class DataBookTableModel extends AbstractTableModel
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The Table for this model. */
		private JVxTable tableControl;
		
		/** The Table for this model. */
		private JTable table;
		
		/** The IDataBook, that provides the data. */
		private IDataBook dataBook;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Generates the TableModels with the given IDataBook.
		 * 
		 * @param pTableControl the JVxTable, that uses this model data.
		 * @param pDataBook the IDataBook, that provides the data.
		 */
		public DataBookTableModel(JVxTable pTableControl, IDataBook pDataBook)
		{
			tableControl = pTableControl;
			table = tableControl.getJTable();
			dataBook = pDataBook;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getColumnName(int pColumnIndex)
		{
			return dataBook.getRowDefinition().getColumnDefinition(pColumnIndex).getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getColumnClass(int pColumnIndex)
		{
			return String.class;
//			return dataBook.getRowDefinition().getColumn(pColumnIndex).getDataType().getTypeClass();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isCellEditable(int pRowIndex, int pColumnIndex)
		{
			ColumnDefinition columnDef = dataBook.getRowDefinition().getColumnDefinition(pColumnIndex);
			boolean editable = pColumnIndex >= 0 && tableControl.isEditable()
			        && !dataBook.isReadOnly()
					&& !columnDef.isReadOnly();
			if (editable && dataBook.getReadOnlyChecker() != null)
			{
				try
				{
					editable = !dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, columnDef.getName(), dataBook.getSelectedRow(), -1);
				}
				catch (Throwable pTh)
				{
					// Ignore
				}
			}
			return editable;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setValueAt(Object pValue, int pRowIndex, int pColumnIndex)
		{
			// Set Value is no longer to do by model. The ICellEditorHandler has to store. 
/*			if (pColumnIndex > 0)
			{
				try 
				{
					dataBook.setSelectedRow(pRowIndex);
					dataBook.setValue(getColumnName(pColumnIndex), pValue);
				}
				catch (Exception ex)
				{
					// Nothing to do.
				}
			}*/
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the row count. This method considers lazy loading and calculates the
		 * row count dependent of the loaded rows.
		 * 
		 * @return the desired row count or the row count if all rows were fetched
		 */
		public int getRowCount()
		{
			if (dataBook.isOpen())
			{
				try
				{
					if (dataBook.isOutOfSync() || !dataBook.isAllFetched())
					{
						int lastVisibleRowIndex;
						if (table.getParent() instanceof JViewport)
						{
							Rectangle viewRect = ((JViewport)table.getParent()).getViewRect();
		
							if (tableControl.ignoreScrollPositionAfterReload)
							{
							    lastVisibleRowIndex = (viewRect.height) / table.getRowHeight() + 1;
							}
							else
							{
                                lastVisibleRowIndex = (viewRect.y + viewRect.height) / table.getRowHeight() + 1;
							}
						}
						else
						{
							Rectangle viewRect = table.getBounds();
							
							lastVisibleRowIndex = viewRect.height / table.getRowHeight() + 1;
						}
						if (dataBook.isOutOfSync() || lastVisibleRowIndex >= dataBook.getRowCount())
						{
							Cursor cursor = JVxUtil.getGlobalCursor(table);
							
							boolean resetCursor = cursor == null || cursor.getType() != Cursor.WAIT_CURSOR;
							JVxUtil.setGlobalCursor(table, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							try
							{
								dataBook.getDataRow(lastVisibleRowIndex);
								table.revalidate();
							}
							finally
							{
								if (resetCursor)
								{
									JVxUtil.setGlobalCursor(table, null);
								}
							}
						}
					}
					
					return dataBook.getRowCount();
				}
				catch (Exception me)
				{
					ExceptionHandler.show(me);
				}
			}
			
			return 0;
		}

		/**
		 * Gets the column count.
		 * 
		 * @return the column count
		 */
		public int getColumnCount()
		{
			return dataBook.getRowDefinition().getColumnCount();
		}

		/**
		 * Gets the value from a cell.
		 * 
		 * @param pRowIndex the row index
		 * @param pColumnIndex the column index
		 * @return the value from the cell
		 */
		public Object getValueAt(int pRowIndex, int pColumnIndex)
		{
			try 
			{
				return dataBook.getRowDefinition().getColumnDefinition(pColumnIndex).getDataType().convertToString(
						  dataBook.getDataRow(pRowIndex).getValue(getColumnName(pColumnIndex)));
			}
			catch (Exception ex)
			{
				return null;
			}
		}

	}	// DataBookTableModel 

	/**
	 * Implements the JTable model interfaces with the given IDataBook. 
	 */
	public static class DataBookTableColumnModel extends DefaultTableColumnModel 
	                                             implements MouseListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The rowDefinition, that provides the data. */
		private JVxTable tableControl;
		
		/** The row height. */
		private int rowHeight;

		/** The ColumnView to be shown. */
		private ColumnView columnView = null;
		/** The columnViewCount. */
		private int columnViewCount = 0;
		
		/** true, if setSelectedColumnshould not be called on databook. */
		private boolean	ignoreSetSelectedColumn = false;
		/** true, if property change should be ignored. */
		private boolean	ignorePropertyChange = true;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Generates the TableColumnModel for the given JVxTable.
		 * 
		 * @param pTableControl the IDataBook, that provides the data.
		 */
		public DataBookTableColumnModel(JVxTable pTableControl)
		{
			tableControl = pTableControl;
			// This is done implicit by the getColumn and getColumns functions.
//			initTableColumns();
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public void mouseClicked(MouseEvent pMouseEvent)
		{
			if (!tableControl.isSortOnHeaderEnabled() || !tableControl.isEnabled())
			{
				return;
			}
			int x = pMouseEvent.getX();
			int y = pMouseEvent.getY();
			
			int column = getColumnIndexAtX(x);
			Rectangle bounds = tableControl.table.getTableHeader().getHeaderRect(column);
			
			if (y >= bounds.y + 1 && y <= bounds.y + bounds.height - 2)
			{
				if (x >= bounds.x + 3 && x <= bounds.x + bounds.width - 6)
				{
					String columnName = (String)getColumn(column).getIdentifier();
					
					IDataBook dataBook = tableControl.getDataBook();
					
					SortDefinition sort = dataBook.getSort();
					
					boolean hasFocus = tableControl.table.hasFocus();
					boolean changeColumn = column != tableControl.table.getSelectedColumn();
					
					if (!hasFocus)
					{
						tableControl.table.requestFocus();
					}
					if (changeColumn)
					{
						tableControl.table.setColumnSelectionInterval(column, column);
					}
					if (getColumn(column).getModelIndex() >= 0)
					{
						try
						{
						    if (dataBook.getRowDefinition().getColumnDefinition(columnName).isSortable())
				            {
    							if ((pMouseEvent.getModifiers() & (InputEvent.BUTTON1_MASK | InputEvent.BUTTON1_DOWN_MASK)) != 0 
    								&& pMouseEvent.isControlDown() 
    								&& sort != null && sort.getColumns().length > 0)
    							{
    								String[] sortColumns = sort.getColumns();
    								boolean[] ascending = sort.isAscending();
    								int index = ArrayUtil.indexOf(sortColumns, columnName);
    								
    								if (index < 0)
    								{
    									dataBook.setSort(new SortDefinition(ArrayUtil.add(sortColumns, columnName), ArrayUtil.add(ascending, true)));
    								}
    								else if (ascending.length <= index)
    								{
    									boolean[] asc = new boolean[sortColumns.length];
    									System.arraycopy(ascending, 0, asc, 0, ascending.length);
    									asc[index] = false; 
    									dataBook.setSort(new SortDefinition(sortColumns, asc));
    								}
    								else if (ascending[index])
    								{
    									ascending[index] = false; 
    									dataBook.setSort(new SortDefinition(sortColumns, ascending));
    								}
    								else if (sortColumns.length > 1)
    								{
    									dataBook.setSort(new SortDefinition(ArrayUtil.remove(sortColumns, index), ArrayUtil.remove(ascending, index)));
    								}
    								else
    								{
    									dataBook.setSort(null);
    								}
    							}
    							else if ((pMouseEvent.getModifiers() & (InputEvent.BUTTON1_MASK | InputEvent.BUTTON1_DOWN_MASK)) != 0)
    							{
    								if (sort == null || sort.getColumns().length != 1 || !columnName.equals(sort.getColumns()[0]))
    								{
    									dataBook.setSort(new SortDefinition(new String[] {columnName}, new boolean[] {true}));
    								}
    								else if (sort.isAscending().length == 0 || sort.isAscending()[0])
    								{
    									dataBook.setSort(new SortDefinition(new String[] {columnName}, new boolean[] {false}));
    								}
    								else
    								{
    									dataBook.setSort(null);
    								}
    							}
				            }
						}
						catch (Throwable pThrowable)
						{
							ExceptionHandler.raise(pThrowable);
						}
					}
				}
				else if (pMouseEvent.getClickCount() == 2)
				{
					if (x < bounds.x + 3)
					{
						column--;
					}
					if (column >= 0)
					{
						TableColumn tableColumn = tableColumns.get(column);
						
						tableControl.getDataBook().getRowDefinition()
						  .getColumnDefinition(tableColumn.getModelIndex())
						    .setWidth(0);
						
						initTableColumns(true);
						fireColumnMarginChanged();
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void mouseEntered(MouseEvent pMouseEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void mouseExited(MouseEvent pMouseEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void mousePressed(MouseEvent pMouseEvent)
		{
			ignorePropertyChange = false;
			tableControl.lastColumnWidth = getColumn(getColumnCount() - 1).getWidth();
		}

		/**
		 * {@inheritDoc}
		 */
		public void mouseReleased(MouseEvent pMouseEvent)
		{
			ignorePropertyChange = true;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		
		@Override
		public void addColumn(TableColumn pTableColumn)
		{
			try
			{
				Object identifier = pTableColumn.getIdentifier();
				if (identifier instanceof String)
				{
					int index = tableControl.getDataBook().getRowDefinition().getColumnDefinitionIndex((String)identifier);
					if (index >= 0)
					{
						pTableColumn.setModelIndex(index);
						
						addColumnIntern((String)identifier, pTableColumn);
					}
				}
				else 
				{
					int index = pTableColumn.getModelIndex();
					if (index >= 0 && index < tableControl.getDataBook().getRowDefinition().getColumnCount())
					{
						identifier = tableControl.getDataBook().getRowDefinition().getColumnDefinition(index).getName();
						
						pTableColumn.setIdentifier(identifier);
						
						addColumnIntern((String)identifier, pTableColumn);
					}
				}
			}
			catch (Throwable pException)
			{
				// The new Column can not be added!
			}
			throw new IllegalArgumentException("The given TableColumn is not allowed!");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void moveColumn(int pColumnIndex, int pNewColumnIndex)
		{
			columnView.moveColumnName(pColumnIndex, pNewColumnIndex);

			ignoreSetSelectedColumn = true;
			try
			{
				super.moveColumn(pColumnIndex, pNewColumnIndex);
			}
			finally
			{
				ignoreSetSelectedColumn = false;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeColumn(TableColumn pTableColumn)
		{
			int index = tableColumns.indexOf(pTableColumn);

			if (index >= 0)
			{
				columnView.removeColumnName(index);

				super.removeColumn(pTableColumn);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public TableColumn getColumn(int pColumnIndex)
		{
			initTableColumns(false);

			return super.getColumn(pColumnIndex);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getColumnCount()
		{
			return columnViewCount;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getColumnIndex(Object pColumn)
		{
			int index = tableControl.getDataBook().getRowDefinition().getColumnDefinitionIndex((String)pColumn);
			
			if (index < 0)
			{
				throw new IllegalArgumentException("Identifier not found");
			}
			
			return index;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumeration<TableColumn> getColumns()
		{
			initTableColumns(false);
			return super.getColumns();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
	    public void addColumnModelListener(TableColumnModelListener pTableColumnModelListener) 
	    {
	    	super.addColumnModelListener(pTableColumnModelListener);
	    	if (pTableColumnModelListener instanceof JTableHeader)
	    	{
	    		((JTableHeader)pTableColumnModelListener).addMouseListener(this);
	    	}
	    }

		/**
		 * {@inheritDoc}
		 */
		@Override
	    public void removeColumnModelListener(TableColumnModelListener pTableColumnModelListener) 
	    {
	    	if (pTableColumnModelListener instanceof JTableHeader)
	    	{
	    		((JTableHeader)pTableColumnModelListener).removeMouseListener(this);
	    	}
	    	super.removeColumnModelListener(pTableColumnModelListener);
	    }

		/**
		 * {@inheritDoc}
		 */
		@Override
	    public void propertyChange(PropertyChangeEvent pEvent) 
	    {
			super.propertyChange(pEvent);
			
			if (!ignorePropertyChange && pEvent.getPropertyName() == "width") 
			{
				TableColumn tableColumn = (TableColumn)pEvent.getSource();
				int width = ((Integer)pEvent.getNewValue()).intValue();
				
				tableColumn.setMaxWidth(Integer.MAX_VALUE - width);
				tableControl.getDataBook().getRowDefinition().getColumnDefinition(tableColumn.getModelIndex()).setWidth(width);
			}
	    }
	    
		/**
		 * {@inheritDoc}
		 */
		@Override
	    public void valueChanged(ListSelectionEvent pListSelectionEvent) 
	    {
			if (!ignoreSetSelectedColumn && tableControl.isEnabled())
			{
				int column = tableControl.table.getSelectedColumn();
				if (column < 0)
				{
					column = pListSelectionEvent.getFirstIndex();
				}
				
				if (getColumn(column).getModelIndex() >= 0)
				{
					String columnName = (String)getColumn(column).getIdentifier();
		
					boolean oldIgnoreEvent = tableControl.ignoreEvent;
					try
					{
						tableControl.ignoreEvent = true;
						
						tableControl.getDataBook().setSelectedColumn(columnName);
					}
					catch (Throwable pThrowable)
					{
						ExceptionHandler.raise(pThrowable);
					}
					finally
					{
						tableControl.ignoreEvent = oldIgnoreEvent;
					}
				}

				super.valueChanged(pListSelectionEvent);
			}
	    }
	    
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Adds the TableColumn.
		 * 
		 * @param pColumnName the column name
		 * @param pTableColumn the TableColumn
		 * @throws ModelException if it is not possible to set new TableColumns on RowDefinition.
		 */
		private void addColumnIntern(String pColumnName, TableColumn pTableColumn) throws ModelException
		{
			columnView.addColumnNames(pColumnName);

			super.addColumn(pTableColumn);
		}

		/**
		 * Gets the found or a new TableColumn.
		 * 
		 * @param pIdentifier the identifier to find.
		 * @param pIndex the visible index.
		 * @return the found or a new TableColumn.
		 */
		private TableColumn findOrCreateTableColumn(String pIdentifier, int pIndex)
		{
			int modelIndex = tableControl.getDataBook().getRowDefinition().getColumnDefinitionIndex(pIdentifier);
			ColumnDefinition columnDefinition;
			if (modelIndex < 0)
			{
				columnDefinition = null;
			}
			else
			{
				columnDefinition = tableControl.getDataBook().getRowDefinition().getColumnDefinition(modelIndex);
			}
			
			TableColumn tableColumn = null;
			
			for (int i = 0; i < tableColumns.size() && tableColumn == null; i++)
			{
				TableColumn tabCol = tableColumns.get(i);
				if (tabCol.getModelIndex() == modelIndex && pIdentifier.equals(tabCol.getIdentifier()))
				{
					tableColumn = tabCol;
					tableColumns.remove(i);
				}
			}
			if (tableColumn == null)
			{
				tableColumn = new TableColumn(modelIndex);
				tableColumn.setIdentifier(pIdentifier);

				GenericTableCellHandler tableCellHandler = tableControl.createTableCellHandler(pIdentifier);
				tableColumn.setHeaderRenderer(tableCellHandler);
				tableColumn.setCellRenderer(tableCellHandler);
				tableColumn.setCellEditor(tableCellHandler);
				
				// initialize a cell editor handle, this allows GenUI Cell Editors to initialize the editor depending to the current mapped control and column.
				try 
				{
					tableCellHandler.getCellEditor().createCellEditorHandler(tableControl, tableControl.getDataBook(), pIdentifier);
				}
				catch (Throwable ex)
				{
					// Ignore
				}
				
				tableControl.calculateHeightAndWidth = true;
			}
			if (columnDefinition == null)
			{
				tableColumn.setHeaderValue(tableControl.translate(pIdentifier));
				tableColumn.setResizable(true);
			}
			else
			{
				tableColumn.setHeaderValue(tableControl.translate(columnDefinition.getLabel()));
				tableColumn.setResizable(columnDefinition.isResizable());
	
				if (!tableControl.calculateHeightAndWidth && columnDefinition.getWidth() == 0 
						&& tableColumn.getMaxWidth() != Integer.MAX_VALUE)
				{
				    tableControl.calculateHeightAndWidth = true;
				}
				
				if (tableControl.calculateHeightAndWidth)
				{
					if (columnDefinition.getWidth() <= 0)
					{
						Dimension preferredSize;
						if (tableControl.isTableHeaderVisible())
						{
							preferredSize = tableColumn.getHeaderRenderer().
			                					getTableCellRendererComponent(null, tableColumn.getHeaderValue(), false, false, -1, pIndex).getPreferredSize();
							preferredSize.width += 20;
						}
						else
						{
							preferredSize = new Dimension(20, 0);
						}
						tableColumn.setMaxWidth(Integer.MAX_VALUE);
						tableColumn.setPreferredWidth(preferredSize.width);
						tableColumn.setWidth(preferredSize.width);
		
						calculateWidthAndHeightFromData(tableColumn, true);
					}
					else
					{
						calculateWidthAndHeightFromData(tableColumn, false);
						tableColumn.setWidth(columnDefinition.getWidth());
						tableColumn.setPreferredWidth(columnDefinition.getWidth());
						tableColumn.setMaxWidth(Integer.MAX_VALUE - columnDefinition.getWidth());
					}
				}
			}
			
			return tableColumn;
		}
		
		/**
		 * Calculates the width from data.
		 * 
		 * @param pTableColumn the table column.
		 * @param pCalculateWidth true, if width should be calculated.
		 */
		private void calculateWidthAndHeightFromData(TableColumn pTableColumn, boolean pCalculateWidth)
		{
			try
			{
				int size = tableControl.table.getRowCount(); // Ensure Wait Cursor for fetch!

				if (size == 0 && !tableControl.getDataBook().isAllFetched())
				{
					tableControl.getDataBook().getDataRow(9);
				}
				
				int i = 0;
				int valueCnt = 0;
				size = tableControl.getDataBook().getRowCount();
				
				int lastSize = pTableColumn.getPreferredWidth();
				
				while (i < size && valueCnt < 100)
				{
					Object value = tableControl.getDataBook().getDataRow(i).getValue(pTableColumn.getModelIndex());
					if (value != null)
					{
						Component comp = pTableColumn.getCellRenderer().
		                                  getTableCellRendererComponent(tableControl.table, value, false, false, i, pTableColumn.getModelIndex());
						Dimension vSize = comp.getPreferredSize();
						if (vSize.width + 10 > lastSize)
						{
							lastSize = vSize.width + 10;
						}
						if (vSize.height > rowHeight && rowHeight < tableControl.maxRowHeight)
						{
							rowHeight = Math.min(vSize.height, tableControl.maxRowHeight);
						}
						valueCnt++;
					}
					i++;
				}
				if (pCalculateWidth)
				{
					if (valueCnt > 0)
					{
						pTableColumn.setPreferredWidth(lastSize);
						pTableColumn.setWidth(lastSize);
					}
				}
			}
			catch (Exception pException)
			{
				//important because swing has problems during paint event without this!
				ExceptionHandler.show(pException);
			}
		}
		
		/**
		 * Inits the TableColumns.
		 * 
		 * @param pForceUpdate forces the column update.
		 */
		public void initTableColumns(boolean pForceUpdate)
		{
			// Force sync of IDataBook, to prevent selectionEvent during column synchronization, and ensures the wait cursor.
			tableControl.table.getRowCount();

			// Force sync of IDataBook, to prevent selectionEvent during column synchronization.
			// This method would be less aggressive on fetching data, but does not ensure the wait cursor.
/*			try
			{
				tableControl.dataBook.getSelectedRow();  
			}
			catch (ModelException ex)
			{
				// Do nothing
			}*/
			
			if (pForceUpdate || columnView == null) // != tableControl.getColumnView() || tableColumns.size() != columnView.getColumnCount())
			{
				if (tableControl.cellEditorHandler != null)
				{
					tableControl.editingComplete(FOCUS_LOST);
				}
			
				try
				{
					columnView = tableControl.getColumnView();
				}
				catch (Exception ex)
				{
					columnView = new ColumnView();
				}
				
				for (int i = 0; i < tableColumns.size(); i++)
				{
					tableColumns.get(i).removePropertyChangeListener(this);
				}
				if (tableControl.calculateHeightAndWidth)
				{
					rowHeight = tableControl.getMinRowHeight();
				}
				
				columnViewCount = columnView.getColumnCount();
				if (tableControl.dataBook != null && !tableControl.dataBook.isOpen())
				{
					columnViewCount = 0;
				}
				
				Vector<TableColumn> tempTableColumns = new Vector<TableColumn>();
				
				for (int i = 0; i < columnViewCount; i++)
				{
					tempTableColumns.add(findOrCreateTableColumn(columnView.getColumnName(i), i));
				}

				if (tableControl.calculateHeightAndWidth && tableControl.rowHeight >= 0)
				{
					rowHeight = tableControl.rowHeight;
				}
				
				tableColumns = tempTableColumns;
				totalColumnWidth = -1;
				tableControl.calculateHeightAndWidth = false;
				
				tableControl.table.doLayout(); // Recalculate autoresized tables.
				
				for (int i = 0; i < tableColumns.size(); i++)
				{
					tableColumns.get(i).addPropertyChangeListener(this);
				}

				if (tableControl.table.getRowHeight() != rowHeight)
				{
					tableControl.table.setRowHeight(rowHeight);
				}
			}
			int selectedColumn;
			try
			{
				selectedColumn = columnView.getColumnNameIndex(tableControl.getDataBook().getSelectedColumn());
			}
			catch (Exception ex)
			{
				selectedColumn = -1;
			}
			if (selectedColumn != getSelectionModel().getMinSelectionIndex())
			{
				ignoreSetSelectedColumn = true;
				try
				{
					getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
				}
				finally
				{
					ignoreSetSelectedColumn = false;
				}
			}
		}
		
	}	// DataBookTableColumnModel

	/**
	 * The GenericTableCellEditor is a wrapper, that provides ICellEditor functionality.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class GenericTableCellHandler implements TableCellRenderer, 
	                                                       TableCellEditor 
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The default Renderer anyway!. */
		private static DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
		
		/** The strikeThroughLineBorder. */
		private static StrikeThroughLineBorder strikeThroughLineBorder = new StrikeThroughLineBorder();
		
		/** The strikeThroughEmptyBorder. */
		private static StrikeThroughEmptyBorder strikeThroughEmptyBorder = new StrikeThroughEmptyBorder();
		
		/** The JVxTable to be edited. */
		private JVxTable tableControl;
		
		/** The column to be edited. */
		private String columnName;
		
		/** The JVxTable to be edited. */
		private JTableHeader tableHeader;

		/** The event that caused editing. */
		private EventObject eventObject;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** 
		 * Constructs a new GenericTableCellEditor.
		 * @param pTableControl the JVxTable.
		 * @param pColumnName the column name.
		 */
		public GenericTableCellHandler(JVxTable pTableControl, String pColumnName)
		{
			tableControl = pTableControl;
			columnName = pColumnName;
			
			tableHeader = tableControl.table.getTableHeader();
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public Component getTableCellRendererComponent(JTable pTable, Object pValue, boolean pIsSelected, boolean pHasFocus, int pRow, int pColumn)
		{
			if (pRow < 0 || pTable == null)
			{
				JComponent renderer = (JComponent)tableHeader.getDefaultRenderer().
				                           getTableCellRendererComponent(tableControl.table, pValue, pIsSelected, pHasFocus, pRow, pColumn);
				Insets insets = (Insets)renderer.getClientProperty("defaultInsets");
				boolean hasEmptyBorder = renderer.getBorder() instanceof EmptyBorder;

				// Windows Look&Feel Bugfix with to big empty border!
				if (insets == null)
				{
				    insets = renderer.getInsets();
				    if (hasEmptyBorder && (insets.top > 6 || insets.bottom > 5))
					{
						renderer.setBorder(new EmptyBorder(6, 5, 5, 5));
						insets = renderer.getInsets();
					}
					renderer.putClientProperty("defaultInsets", insets);
				}
				boolean isRightAligned = false;
				if (renderer instanceof JLabel)
				{
    				try
    				{
    				    IDataType dataType = tableControl.getDataBook().getRowDefinition().getColumnDefinition(columnName).getDataType();
                        ICellEditor cellEditor = dataType.getCellEditor();
                        if (cellEditor == null)
                        {
                            cellEditor = JVxUtil.getDefaultCellEditor(dataType.getTypeClass());
                        }
                        
                        if (cellEditor instanceof IStyledCellEditor)
                        {
                            int align;
                            switch (((IStyledCellEditor)cellEditor).getHorizontalAlignment()) 
                            {
                                case IAlignmentConstants.ALIGN_RIGHT:
                                    isRightAligned = true;
                                    align = JVxConstants.RIGHT; break;
                                case IAlignmentConstants.ALIGN_CENTER:
                                case IAlignmentConstants.ALIGN_STRETCH:
                                    align = JVxConstants.CENTER; break;
                                default:
                                    align = JVxConstants.LEFT; break;
                            }
                            
                            ((JLabel)renderer).setHorizontalAlignment(align);
                        }
                        else
                        {
                            ((JLabel)renderer).setHorizontalAlignment(JVxConstants.LEFT);
                        }
    				}
    				catch (Throwable ex)
    				{
    				    // Ignore
    				}
				}
				
				JLabel sortIcon = (JLabel)renderer.getClientProperty("sortIcon");
				if (sortIcon == null)
				{
					sortIcon = new DefaultTableCellRenderer();
					sortIcon.setOpaque(false);
					sortIcon.setBorder(null);
					sortIcon.setHorizontalAlignment(JVxIcon.RIGHT);
					sortIcon.setVerticalAlignment(JVxIcon.BOTTOM);
					sortIcon.setHorizontalTextPosition(JVxIcon.RIGHT);
					sortIcon.setVerticalTextPosition(JVxIcon.BOTTOM);
					sortIcon.setIconTextGap(0);
					sortIcon.setFont(renderer.getFont().deriveFont(Font.PLAIN, 9));
					
					renderer.putClientProperty("sortIcon", sortIcon);
					renderer.add(sortIcon);
				}
				SortDefinition sortDefinition = tableControl.getDataBook().getSort();
				int index;
				if (sortDefinition == null)
				{
					index = -1;
			    }
				else
				{
					index = ArrayUtil.indexOf(sortDefinition.getColumns(), columnName);
				}
				if (index >= 0)
				{
				    sortIcon.setVisible(true);
					sortIcon.setText(String.valueOf(index + 1));
					if (sortDefinition.isAscending().length <= index || sortDefinition.isAscending()[index])
					{
						sortIcon.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/up.png"));
					}
					else
					{
						sortIcon.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/down.png"));
					}
					if (isRightAligned)
					{
					    sortIcon.setHorizontalAlignment(JVxIcon.LEFT);
					}
					else
					{
					    sortIcon.setHorizontalAlignment(JVxIcon.RIGHT);
					}
				}
				else
				{
                    sortIcon.setVisible(false);
					sortIcon.setText(null);
					sortIcon.setIcon(null);
				}
				
				if (pTable != null && sortIcon.isVisible())
				{
					Rectangle bounds = tableHeader.getHeaderRect(pColumn);
					
					sortIcon.setBounds(insets.left, insets.top, bounds.width - insets.left - insets.right, bounds.height - insets.top - insets.bottom);
				}
				
				return renderer;
			}
			else
			{
				pHasFocus = tableControl.table.getSelectionModel().getMinSelectionIndex() == pRow 
						&& tableControl.table.getColumnModel().getSelectionModel().getMinSelectionIndex() == pColumn;
			
				IDataBook dataBook = tableControl.getDataBook();
				IChangeableDataRow dataRow = null;
				boolean deleting = false;
				boolean readOnly = false;
				boolean mandatory = false;
				ICellRenderer<Component> cellRenderer;
				
				JComponent innerComponent = null;
				try
				{
					ColumnDefinition columnDef = dataBook.getRowDefinition().getColumnDefinition(columnName);
					if (!tableControl.isEnabled())
					{
						readOnly = true;
					}
					else if (!dataBook.isReadOnly() && tableControl.isEnabled())
					{
						mandatory = !columnDef.isNullable();
						readOnly = columnDef.isReadOnly();
						if (!readOnly && dataBook.getReadOnlyChecker() != null)
						{
							try
							{
								readOnly = dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook.getDataRow(pRow), columnName, pRow, pColumn);
							}
							catch (Throwable pTh)
							{
								// Ignore
							}
						}
					}
					
					cellRenderer = columnDef.getDataType().getCellRenderer();
				
					if (cellRenderer == null)
					{
						ICellEditor cellEditor = getCellEditor();
						
						if (cellEditor instanceof ICellRenderer)
						{
							cellRenderer = (ICellRenderer)cellEditor;
						}
						else if (cellEditor instanceof IResource)
						{
							cellEditor = (ICellEditor)((IResource)cellEditor).getResource();
							if (cellEditor instanceof ICellRenderer)
							{
								cellRenderer = (ICellRenderer)cellEditor;
							}
						}
					}
					
					dataRow = dataBook.getDataRow(pRow);
					if (cellRenderer != null)
					{
						innerComponent = (JComponent)cellRenderer.getCellRendererComponent(pTable, dataBook, pRow, dataRow, columnName, pIsSelected, pHasFocus);
					}
					deleting = dataRow.isDeleting();
				}
				catch (Exception me)
				{
					// can do nothing
				}
				
				if (innerComponent == null)
				{
					innerComponent = (JComponent)defaultRenderer.getTableCellRendererComponent(pTable, pValue, pIsSelected, pHasFocus, pRow, pColumn);
					
					if (pTable.getColumnModel().getColumn(pColumn).getModelIndex() < 0)
					{
						innerComponent.setBackground(JVxUtil.getSystemColor(IColor.INVALID_EDITOR_BACKGROUND));
						
						return innerComponent;
					}
				}
				CellFormat cellFormat = null;
				if (tableControl.cellFormatter != null)
				{
					try
					{
						cellFormat = tableControl.cellFormatter.getCellFormat(dataBook, dataBook.getDataPage(), dataBook.getDataRow(pRow), columnName, pRow, pColumn);
					}
					catch (Throwable pTh)
					{
						// Do nothing
					}
				}
				Color background;
				Color foreground;
				Font font;
				Icon image;
				int leftIndent;
				if (cellFormat == null)
				{
					background = null;
					foreground = null;
					font = null;
					image = null;
					leftIndent = 0;
				}
				else
				{
					background = cellFormat.getBackground();
					foreground = cellFormat.getForeground();
					font = cellFormat.getFont();
					image = cellFormat.getImage();
					leftIndent = cellFormat.getLeftIndent();
				}

				JVxRendererContainer component = (JVxRendererContainer)innerComponent.getClientProperty("rendererContainer");
				JVxIconRenderer iconRenderer;
				if (component == null)
				{
					component = new JVxRendererContainer();
					((JVxBorderLayout)component.getLayout()).setHorizontalGap(3);
					
					innerComponent.setOpaque(false);
					
					iconRenderer = new JVxIconRenderer();
					iconRenderer.setOpaque(false);
					
					component.add(iconRenderer, JVxBorderLayout.WEST);
					
					innerComponent.putClientProperty("rendererContainer", component);
				}
				else
				{
					iconRenderer = (JVxIconRenderer)component.getComponent(0);
				}
				((JVxBorderLayout)component.getLayout()).getMargins().left = leftIndent;
				
				if (innerComponent.getParent() != component)
				{
					component.add(innerComponent, JVxBorderLayout.CENTER);
					innerComponent.setOpaque(false);
				}

				iconRenderer.setVisible(image != null);
				
				if (image == null)
				{
					iconRenderer.setImage(null);
				}
				else
				{
					iconRenderer.setImage(((ImageIcon)image).getImage());
				}

				if (background == null)
				{
					if (readOnly)
					{
						background = JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
					}
					else if (mandatory)
					{
						background = JVxUtil.getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND);
					}
				}
				else if (!tableControl.isEnabled())
				{
					background = JVxUtil.getAverageColor(background, JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
				}
				if (font == null)
				{
					font = tableControl.table.getFont();
				}
				
				boolean focused = pTable.hasFocus();
				if (pIsSelected && tableControl.showSelection) 
				{
					Color selFore;
					Color selBack;
					if (focused)
					{
						selFore = JVxUtil.getSystemColor(IColor.CONTROL_ACTIVE_SELECTION_FOREGROUND);
						selBack = JVxUtil.getSystemColor(IColor.CONTROL_ACTIVE_SELECTION_BACKGROUND);
					}
					else
					{
						selFore = JVxUtil.getSystemColor(IColor.CONTROL_INACTIVE_SELECTION_FOREGROUND);
						selBack = JVxUtil.getSystemColor(IColor.CONTROL_INACTIVE_SELECTION_BACKGROUND);
					}
					component.setForeground(JVxUtil.getAverageColor(foreground, selFore));
					innerComponent.setForeground(JVxUtil.getAverageColor(foreground, selFore));
					component.setBackground(JVxUtil.getAverageColor(background, selBack));
				}
				else
				{
					component.setForeground(foreground);
					innerComponent.setForeground(foreground);
					if (pRow % 2 == 0 || JVxUtil.getSystemColor(IColor.CONTROL_ALTERNATE_BACKGROUND) == null)
					{
						component.setBackground(background);
					}
					else
					{
						if (background == null)
						{
							background = tableControl.table.getBackground();
						}
						component.setBackground(JVxUtil.getAverageColor(background, 
										JVxUtil.getAverageColor(background, 
										JVxUtil.getAverageColor(background, JVxUtil.getSystemColor(IColor.CONTROL_ALTERNATE_BACKGROUND)))));
					}
				}
				component.setFont(font);
				innerComponent.setFont(font);
				
				Color col;
				if (focused)
				{
					col = tableControl.table.getForeground();
				}
				else
				{
					col = JVxUtil.getAverageColor(
							JVxUtil.getAverageColor(tableControl.table.getForeground(), tableControl.table.getGridColor()), tableControl.table.getGridColor());
				}
				if (pHasFocus && tableControl.showFocusRect)
				{
					strikeThroughLineBorder.setLineColor(col);
					strikeThroughLineBorder.setStrikeThrough(deleting);
					component.setBorder(strikeThroughLineBorder);
				}
				else
				{
					strikeThroughEmptyBorder.setLineColor(col);
					strikeThroughEmptyBorder.setStrikeThrough(deleting);
					component.setBorder(strikeThroughEmptyBorder);
				}

				String tooltipText = null;
				if (tableControl.cellToolTip != null)
				{
					try
					{
						tooltipText = tableControl.cellToolTip.getToolTipText(dataBook, dataBook.getDataPage(), dataBook.getDataRow(pRow), columnName, pRow, pColumn);
					}
					catch (Throwable pTh)
					{
						// Do nothing
					}
				}
				component.setToolTipText(tooltipText);
				
				return component;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
	    public Component getTableCellEditorComponent(JTable pTable, Object pValue, boolean pIsSelected, int pRow, int pColumn) 
	    {
/*	    	
			if (tableControl.getCellEditorHandler() != null)
	    	{
	    		new Exception("Please let us know if this case occurs!" tableControl.getCellEditorHandler()).printStackTrace();
	    	}
*/
			tableControl.ignoreEvent = true; // first set ignoreEvents true, to prevent events on update.
	    	try
	    	{
		    	IDataBook dataBook = tableControl.getDataBook();
		    	
		    	if (tableControl.table.getSelectedColumn() != pColumn)
		    	{
		    		tableControl.table.setColumnSelectionInterval(pColumn, pColumn);
		    	}

	    		dataBook.setSelectedRow(pRow);
	    		
		    	tableControl.ignoreEvent = false;

		    	tableControl.doRepaint(); // show the new selected Row directly, as ignoreEvent was set
	    		
		    	tableControl.setCellEditorHandler(getCellEditor().createCellEditorHandler(tableControl, dataBook, columnName));
		    	tableControl.getCellEditorHandler().cancelEditing();

				CellFormat cellFormat = null;
        		if (tableControl.getCellFormatter() != null)
				{
					try
					{
						cellFormat = tableControl.getCellFormatter().getCellFormat(
								dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
					}
					catch (Throwable pThrowable)
					{
						// Do nothing
					}
				}
		    	
        		tableControl.ignoreMousePressed = true;
        		SwingUtilities.invokeLater(new Runnable()
        		{
        			public void run()
        			{
        				tableControl.ignoreMousePressed = false;
        			}
        		});
        		
		    	return new CellEditorPane(tableControl, cellFormat, eventObject);
			}
			catch (Throwable pThrowable)
			{
				tableControl.ignoreEvent = false;
				tableControl.notifyRepaint();
				
				ExceptionHandler.raise(pThrowable);
				return null; // this code is never reached!
			}
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    public Object getCellEditorValue()
	    {
	    	return null;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public boolean isCellEditable(EventObject pEventObject)
	    {
	    	eventObject = pEventObject;
    		try
    		{
				ICellEditor editor = getCellEditor();
		    	if (eventObject instanceof MouseEvent)
		    	{
    				Point point = ((MouseEvent)eventObject).getPoint();
    				int row = tableControl.table.rowAtPoint(point);

    				if (editor instanceof IInplaceCellEditor)
	    			{
	    				Rectangle rect = tableControl.table.getCellRect(row, tableControl.table.columnAtPoint(point), true);
	    				if (rect.contains(point.x, point.y))
	    				{
		    				rect.x = rect.x + rect.width - 16;
		    				rect.width = 16;
		    				if (rect.contains(point.x, point.y) && editor instanceof IComboCellEditor)
		    				{
		    					tableControl.openComboBox();

		    					return true;
		    				}
		    				else if (((IInplaceCellEditor)editor).getPreferredEditorMode() == IInplaceCellEditor.SINGLE_CLICK)
		    				{
		    					if (editor instanceof IComboCellEditor && ((IComboCellEditor)editor).isAutoOpenPopup())
		    					{
		    						tableControl.openComboBox();
		    					}

		    					return true;
		    				}
	    				}
	    			}
	    			if (editor instanceof ICellRenderer)
	    			{
	    				// ensure isDirectCellEditor gets the correct data.
	    				((ICellRenderer)editor).getCellRendererComponent(tableControl.table, tableControl.dataBook, row, 
	    						                                         tableControl.dataBook.getDataRow(row), columnName, false, false);
	    			}
		    		if (!editor.isDirectCellEditor() && ((MouseEvent)eventObject).getClickCount() == 1)
		    		{
		    			return false;
		    		}
		    	}
    		}
    		catch (Throwable me)
    		{
    			ExceptionHandler.raise(me);
    		}
	    	
	    	return true;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public boolean shouldSelectCell(EventObject anEvent)
	    {
	    	return false;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public boolean stopCellEditing()
	    {
	    	tableControl.editingComplete(ICellEditorListener.FOCUS_LOST);
	    	return true;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void cancelCellEditing()
	    {
	    	tableControl.editingComplete(ICellEditorListener.ESCAPE_KEY);
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void addCellEditorListener(CellEditorListener l)
	    {
	    	Component component = tableControl.table.getEditorComponent();
	    	if (component != null && component.getParent() == null)
	    	{
	    		tableControl.cancelEditing();
	    		tableControl.table.editingCanceled(null);
	    		tableControl.table.repaint();
				if (!tableControl.table.hasFocus())
				{
					tableControl.table.requestFocus();
				}
	    	}
	    }
	    /**
	     * {@inheritDoc}
	     */
	    public void removeCellEditorListener(CellEditorListener l)
	    {
	    }
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Gets the CellEditor for editing the given data type.
	     * 
	     * @return the ICellEditor.
	     * @throws ModelException if the column name is invalid
	     */
	    private ICellEditor getCellEditor() throws ModelException
	    {
	    	IDataType dataType = tableControl.getDataBook().getRowDefinition().getColumnDefinition(columnName).getDataType();
	    	ICellEditor cellEditor = dataType.getCellEditor();
	    	
	    	if (cellEditor == null)
	    	{
	    		cellEditor = JVxUtil.getDefaultCellEditor(dataType.getTypeClass());
	    	}
	    	
	    	return cellEditor;
	    }
	    
	}	// GenericTableCellHandler

	/**
	 * CellEditorPane provides different editor sizes as the cell has.
	 *  
	 * @author Martin Handsteiner
	 */
	public static class CellEditorPane extends JComponent 
	                                   implements Runnable, 
	                                              FocusListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The Layout policy to findout, which component should get the focus. */
		private static final LayoutFocusTraversalPolicy LAYOUT_POLICY = new LayoutFocusTraversalPolicy();
		
		/** The parent component. */
		private JVxTable tableControl;

		/** The row margin. */
		private int rowMargin;
		/** The column margin. */
		private int columnMargin;
		/** The left indent. */
		private int leftIndent;
		/** The left indent. */
		private JComponent leftSpacer;
		/** The icon renderer. */
		private JVxIcon icon;
		/** The icon renderer. */
		private Component cellEditorComponent;
		/** The focus component. */
		private Component focusComponent;
		
		/** KeyEvent to deliver. */
		private EventObject eventObject;
		/** true, if it is a direct editor. */
		private boolean isDirectEditor;
		/** KeyEvent to deliver. */
		private KeyEvent keyEvent;
		/** KeyEvent to deliver. */
		private MouseEvent mouseEvent;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new CellEditorPane.
		 * @param pTableControl the table control that uses the editor.
		 */
		public CellEditorPane(JVxTable pTableControl)
		{
			this(pTableControl, null, null);
		}

		/**
		 * Constructs a new CellEditorPane.
		 * @param pTableControl the table control that uses the editor.
		 * @param pCellFormat the cell format.
		 * @param pEventObject the event object.
		 */
		public CellEditorPane(JVxTable pTableControl, CellFormat pCellFormat, EventObject pEventObject)
		{
			tableControl = pTableControl;
			eventObject = pEventObject;
			
			columnMargin = tableControl.table.getColumnModel().getColumnMargin();
			rowMargin = tableControl.table.getRowMargin();
			
			cellEditorComponent = tableControl.cellEditorHandler.getCellEditorComponent();
			isDirectEditor = tableControl.cellEditorHandler.getCellEditor().isDirectCellEditor();
			
			leftIndent = 0;
			icon = null;
			if (pCellFormat != null)
			{
				leftIndent = pCellFormat.getLeftIndent();
				Color backGround = pCellFormat.getBackground();
				
				if (leftIndent > 0 && backGround != null)
				{
					leftSpacer = new JLabel();
					leftSpacer.setOpaque(true);
					leftSpacer.setBackground(backGround);
					
					add(leftSpacer);
				}
				setBackground(pCellFormat.getBackground());
				
				if (pCellFormat.getImage() != null)
				{
					icon = new JVxIcon(((ImageIcon)pCellFormat.getImage()).getImage());
					icon.setBackground(backGround);
					
					add(icon);
				}
			}

			add(cellEditorComponent);
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void focusGained(FocusEvent pFocusEvent)
		{
			dispatchKeyEvent();
			
			focusComponent.removeFocusListener(this);
		}

		/**
		 * {@inheritDoc}
		 */
		public void focusLost(FocusEvent pFocusEvent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			if (!isDirectEditor)
			{
				repaint();
				if (focusComponent != null)
				{
					focusComponent.addFocusListener(this);
					focusComponent.requestFocus();
				}
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setBounds(int pX, int pY, int pWidth, int pHeight)
		{
			Dimension iconSize;
			int distance = leftIndent;
			if (icon == null)
			{
				iconSize = new Dimension();
			}
			else
			{
				iconSize = icon.getPreferredSize();
				distance += iconSize.width + 3;
			}			
					
			int x = pX - (columnMargin / 2 + 1);
			int y = pY - (rowMargin / 2 + 1);
			
			int width = pWidth + columnMargin + 1;
			int height = pHeight + rowMargin + 1;
			
			Rectangle bounds = tableControl.table.getVisibleRect();
			Dimension prefSize = cellEditorComponent.getPreferredSize();
			prefSize.width += distance;
			
			if (prefSize.width > width)
			{
				width = prefSize.width;
			}
			if (x + width > bounds.x + bounds.width)
			{
				x = bounds.x + bounds.width - width;
			}
			if (x < bounds.x)
			{
				width -= bounds.x - x;
				x = bounds.x;
				if (width > bounds.width)
				{
					width = bounds.width;
				}
			}
			if (prefSize.height > height)
			{
				height = prefSize.height;
			}
			if (y + height > bounds.y + bounds.height)
			{
				y = bounds.y + bounds.height - height;
			}
			if (y < bounds.y)
			{
				height -= bounds.y - y;
				y = bounds.y;
				if (height > bounds.height)
				{
					height = bounds.height;
				}
			}

			super.setBounds(x, y, width, height);
				
			if (leftSpacer != null)
			{
				leftSpacer.setBounds(pX - x, pY - y, leftIndent, pHeight);
			}
			if (icon != null)
			{
				icon.setBounds(leftIndent + pX - x, pY - y, iconSize.width + 3, pHeight);
			}
			
			cellEditorComponent.setBounds(distance, 0, width - distance, height);			
			cellEditorComponent.validate();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void requestFocus()
		{
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addNotify()
		{
			super.addNotify();
			
			focusComponent = LAYOUT_POLICY.getFirstComponent(this);

			if (eventObject instanceof KeyEvent)
			{
				keyEvent = (KeyEvent)eventObject;
				char ch = keyEvent.getKeyChar();
		    	if (ch < 0x20 || ch >= 0xffff || ch == 0x7f || keyEvent.isAltDown())
		    	{
					keyEvent = null;
		    	}
			}
			else if (eventObject instanceof MouseEvent)
			{
				mouseEvent = (MouseEvent)eventObject;
			}
			
			if (isDirectEditor)
			{
				dispatchMouseEvent();
				dispatchKeyEvent();
				
				Container parent = getParent();
				
				if (parent != null)
				{
					parent.remove(this);
				}
			}
			else
			{
				SwingUtilities.invokeLater(this);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeNotify()
		{
			super.removeNotify();

			if (!isDirectEditor)
			{
				tableControl.table.repaint(); // (getBounds()); // Everything has to be repainted.
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Dispatches the Event to deliver for the Component.
		 */
		private void dispatchMouseEvent()
		{
			if (focusComponent != null && mouseEvent != null)
			{
				try
				{
					Point point = SwingUtilities.convertPoint(tableControl.table, mouseEvent.getPoint(), focusComponent);
	
					focusComponent.dispatchEvent(new MouseEvent(focusComponent, MouseEvent.MOUSE_PRESSED, mouseEvent.getWhen(), 
							mouseEvent.getModifiers(), point.x, point.y, mouseEvent.getClickCount(), false, mouseEvent.getButton()));
					focusComponent.dispatchEvent(new MouseEvent(focusComponent, MouseEvent.MOUSE_RELEASED, mouseEvent.getWhen(), 
							mouseEvent.getModifiers(), point.x, point.y, mouseEvent.getClickCount(), false, mouseEvent.getButton()));
					focusComponent.dispatchEvent(new MouseEvent(focusComponent, MouseEvent.MOUSE_CLICKED, mouseEvent.getWhen(), 
							mouseEvent.getModifiers(), point.x, point.y, mouseEvent.getClickCount(), false, mouseEvent.getButton()));
	
		            mouseEvent = null;
				}
				catch (Throwable ex)
				{
					// Simulate Eventqueue Exception
					if (ex instanceof SilentAbortException && ex.getCause() != null)
					{
						ex = ex.getCause();
					}
					System.err.println("Exception in thread \"" + Thread.currentThread().getName() + " : " + ex.getMessage());
				}
			}
		}
		
		/**
		 * Dispatches the Event to deliver for the Component.
		 */
		private void dispatchKeyEvent()
		{
			if (focusComponent != null && keyEvent != null)
			{
				try
				{
					focusComponent.dispatchEvent(new KeyEvent(focusComponent, KeyEvent.KEY_PRESSED, keyEvent.getWhen(), 
							keyEvent.getModifiers(), keyEvent.getKeyCode(), keyEvent.getKeyChar()));
					focusComponent.dispatchEvent(new KeyEvent(focusComponent, KeyEvent.KEY_TYPED, keyEvent.getWhen(), 
							keyEvent.getModifiers(), 0, keyEvent.getKeyChar()));
					focusComponent.dispatchEvent(new KeyEvent(focusComponent, KeyEvent.KEY_RELEASED, keyEvent.getWhen(), 
							keyEvent.getModifiers(), keyEvent.getKeyCode(), keyEvent.getKeyChar()));
					keyEvent = null;
				}
				catch (Throwable ex)
				{
					// Simulate Eventqueue Exception
					if (ex instanceof SilentAbortException && ex.getCause() != null)
					{
						ex = ex.getCause();
					}
					System.err.println("Exception in thread \"" + Thread.currentThread().getName() + " : " + ex.getMessage());
				}
			}
		}
		
	}	// CellEditorPane

	/**
	 * LineBorder with strike through functionality. 
	 * @author Martin Handsteiner
	 */
	public static class StrikeThroughLineBorder extends LineBorder
	{
		/** true, if strike through should be painted. */
		private boolean strikeThrough = false;
		
		/**
		 * Constructs a new StrikeThroughLineBorder.
		 */
		public StrikeThroughLineBorder()
		{
			super(null, 1);
		}
		
		/**
		 * true, if strike through should be painted.
		 * @return true, if strike through should be painted.
		 */
		public boolean isStrikeThrough()
		{
			return strikeThrough;
		}

		/**
		 * true, if strike through should be painted.
		 * @param pStrikeThrough true, if strike through should be painted.
		 */
		public void setStrikeThrough(boolean pStrikeThrough)
		{
			strikeThrough = pStrikeThrough;
		}

	    /**
	     * Returns the color of the border.
	     * @param pLineColor the color of the border.
	     */
		public void setLineColor(Color pLineColor)
		{
			lineColor = pLineColor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) 
	    {
	    	super.paintBorder(c, g, x, y, width, height);
	    	if (strikeThrough)
	    	{
		    	g.setColor(lineColor);
		    	g.drawRect(x, y + height / 2, width, 0);
	    	}
	    }
	}
	
	/**
	 * EmptyBorder with strike through functionality. 
	 * @author Martin Handsteiner
	 */
	public static class StrikeThroughEmptyBorder extends EmptyBorder
	{
		/** true, if strike through should be painted. */
	    protected Color lineColor;
		/** the line color. */
		private boolean strikeThrough = false;
		
		/**
		 * Constructs a new StrikeThroughEmptyBorder.
		 */
		public StrikeThroughEmptyBorder()
		{
			super(1, 1, 1, 1);
		}
		
		/**
		 * true, if strike through should be painted.
		 * @return true, if strike through should be painted.
		 */
		public boolean isStrikeThrough()
		{
			return strikeThrough;
		}

		/**
		 * true, if strike through should be painted.
		 * @param pStrikeThrough true, if strike through should be painted.
		 */
		public void setStrikeThrough(boolean pStrikeThrough)
		{
			strikeThrough = pStrikeThrough;
		}

	    /**
	     * Returns the color of the border.
	     * @return the color of the border.
	     */
	    public Color getLineColor()
		{
			return lineColor;
		}

	    /**
	     * Returns the color of the border.
	     * @param pLineColor the color of the border.
	     */
		public void setLineColor(Color pLineColor)
		{
			lineColor = pLineColor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) 
	    {
	    	super.paintBorder(c, g, x, y, width, height);
	    	if (strikeThrough)
	    	{
		    	g.setColor(getLineColor());
		    	g.drawRect(x, y + height / 2, width, 0);
	    	}
	    }
	}

}	// JVxTable
