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
 * 03.11.2008 - [HM] - creation
 * 07.04.2009 - [JR] - cancelEditing: setEditorEditable instead of setEditable 
 * 24.03.2011 - [JR] - #317: cancelEditing checks parents enabled state
 * 31.03.2011 - [JR] - #161: forward translation change to combobase
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.component.IPlaceholder;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.sibvisions.rad.ui.celleditor.AbstractDateCellEditor;
import com.sibvisions.rad.ui.swing.ext.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.swing.ext.JVxCalendarPane;
import com.sibvisions.rad.ui.swing.ext.JVxDateCombo;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxIconRenderer;
import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxRendererContainer;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.text.DateFormatter;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>JVxDateCellEditor</code> provides the generation of the 
 * physical Date editor component, handles correct all events, and 
 * gives standard access to edited values.
 * 
 * @author Martin Handsteiner
 */
public class JVxDateCellEditor extends AbstractDateCellEditor 
                               implements ICellRenderer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default Renderer anyway!. */
	private JVxRendererContainer cellRenderer = null;
	/** The text renderer. */
	private DefaultTableCellRenderer textRenderer = null;
	/** The text renderer. */
	private JVxIconRenderer iconRenderer = null;

	/** The cell renderer. */
	private DateFormatter dateFormatter = new DateFormatter(dateUtil);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new JVxDateCellEditor.
	 */
	public JVxDateCellEditor()
	{
		super();
	}
	
	/**
	 * Constructs a new JVxDateCellEditor with the given date format.
	 * @param pDateFormat the date format.
	 */
	public JVxDateCellEditor(String pDateFormat)
	{
		super(pDateFormat);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler<JComponent> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
			                                                      IDataRow pDataRow, String pColumnName)
	{
		return new CellEditorHandler(this, (ICellFormatterEditorListener)pCellEditorListener, pDataRow, pColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getCellRendererComponent(Component pParentComponent,
								              IDataPage pDataPage,
									          int       pRowNumber,
									          IDataRow  pDataRow,
									          String    pColumnName,
									          boolean   pIsSelected,
									          boolean   pHasFocus)
	{
		if (cellRenderer == null)
		{
			cellRenderer = new JVxRendererContainer();
			
			textRenderer = new DefaultTableCellRenderer();
			textRenderer.setFont(null);
			textRenderer.setOpaque(false);
			
			iconRenderer = new JVxIconRenderer();
			iconRenderer.setImage(JVxUtil.getImage("/com/sibvisions/rad/ui/swing/ext/images/combobox.png"));
			iconRenderer.setOpaque(false);
			
			cellRenderer.add(textRenderer, JVxBorderLayout.CENTER);
			cellRenderer.add(iconRenderer, JVxBorderLayout.EAST);
		}
		
		textRenderer.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(getHorizontalAlignment()));
		textRenderer.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(getVerticalAlignment()));

		IDataBook dataBook = pDataPage.getDataBook();
		
		try
		{
			iconRenderer.setVisible(pIsSelected && !dataBook.isReadOnly() && dataBook.isUpdateAllowed()
					&& !pDataRow.getRowDefinition().getColumnDefinition(pColumnName).isReadOnly());
		}
		catch (Exception ex)
		{
			iconRenderer.setVisible(false);
		}
		
		try
		{
			textRenderer.setText(dateFormatter.valueToString(pDataRow.getValue(pColumnName)));
		}
		catch (Exception pException)
		{
			textRenderer.setText(null);
		}

		return cellRenderer;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * Sets the internal changed flag, and informs the CellEditorListener 
     * if editing is completed.
     * 
     * @author Martin Handsteiner
     */
    public static class CellEditorHandler implements ICellEditorHandler<JComponent>,
                                                     DocumentListener, 
                                                     KeyListener,
                                                     PopupMenuListener,
                                                     FocusListener,
                                                     ActionListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private JVxDateCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The column name of the edited column. */
    	private String columnName;

    	/** The physical component that is added to the parent container. */
    	private JVxDateCombo cellEditorComponent;
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** Tells the listener to ignore the events. */
    	private boolean ignoreEvent = false;
    	
    	/** True, if it's the first editing started event. */
    	private boolean firstEditingStarted = true;
    	
    	/** Detection, if popup is closed by Enter key or by focus lost. */
    	private boolean popupClosedByKeyAction = false;    	
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Constructs a new CellEditorHandler.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(JVxDateCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener, 
                				 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
            try
            {
                if (!Date.class.isAssignableFrom(dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType().getTypeClass()))
                {
                    LoggerFactory.getInstance(AbstractDateCellEditor.class).error("DateCellEditor is used for a column, that does not store dates!");
                }
            }
            catch (ModelException me)
            {
                //nothing to be done
            }

    		cellEditorComponent = new JVxDateCombo(cellEditor.getDateFormat());
    		if (cellEditorComponent.getEditorComponent() instanceof JTextField)
        	{
    			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
    			{	// use alignment of editors, if possible.
    				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
    			}
    			else
    			{
    				((JTextField)cellEditorComponent.getEditorComponent()).setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment()));
    			}
        	}

    		cellEditorComponent.getEditorComponent().getDocument().addDocumentListener(this);
    		cellEditorComponent.getEditorComponent().addKeyListener(this);
    		cellEditorComponent.getEditorComponent().addFocusListener(this);
    		cellEditorComponent.getEditorComponent().setFocusTraversalKeysEnabled(false);
    		((JVxCalendarPane)cellEditorComponent.getPopupComponent()).addActionListener(this);
    		cellEditorComponent.addPopupMenuListener(this);
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	// ICellEditorHandler
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
    		cellEditorComponent.getEditorComponent().getDocument().removeDocumentListener(this);
    		cellEditorComponent.getEditorComponent().removeKeyListener(this);
    		cellEditorComponent.getEditorComponent().removeFocusListener(this);
    		cellEditorComponent.removePopupMenuListener(this);
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditor getCellEditor()
    	{
    		return cellEditor;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditorListener getCellEditorListener()
    	{
    		return cellEditorListener;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public IDataRow getDataRow()
    	{
    		return dataRow;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public String getColumnName()
    	{
    		return columnName;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public JComponent getCellEditorComponent()
    	{
    		return cellEditorComponent;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void saveEditing() throws ModelException
    	{
    		ignoreEvent = true;
    		synchronized (cellEditorComponent.getTreeLock())
    		{
    			cellEditorComponent.setSelectedItem(cellEditorComponent.getEditor().getItem());
    		}
    		ignoreEvent = false;
   			dataRow.setValue(columnName, cellEditorComponent.getSelectedItem());
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{
    		if (!ignoreEvent)
    		{
	    		ignoreEvent = true;

	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);

	        		cellEditorComponent.setTranslation(cellEditorListener.getControl().getTranslation());
	        		cellEditorComponent.setTranslationEnabled(cellEditorListener.getControl().isTranslationEnabled());

	    			cellEditorComponent.setDateFormat(cellEditor.getDateFormat());
	    			
	        		synchronized (cellEditorComponent.getTreeLock())
		    		{
	        			cellEditorComponent.setSelectedItem(dataRow.getValue(columnName));
		    		}

					CellFormat cellFormat = null;

					Container conParent = cellEditorComponent.getParent();
					
					boolean bParentEnabled = conParent == null || conParent.isEnabled();
					
					if (dataRow instanceof IDataBook)
		    		{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			boolean editable = bParentEnabled
		    					        && dataBook.isUpdateAllowed() 
		    							&& !columnDef.isReadOnly();
		    			if (editable && dataBook.getReadOnlyChecker() != null)
		    			{
		    				try
							{
		    					editable = !dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
							}
							catch (Throwable pTh)
							{
								// Ignore
							}
		    			}
		    			cellEditorComponent.setEditorEditable(editable);
		    		}
					else
					{
						cellEditorComponent.setEditorEditable(bParentEnabled && !columnDef.isReadOnly());
					}
					
					if (cellEditorListener.getCellFormatter() != null)
					{
						IDataBook curDataBook = null;
						IDataPage curDataPage = null;
						int curSelectedRow = -1;
						
						if (dataRow instanceof IDataBook)
						{
							curDataBook = (IDataBook)dataRow;
							curDataPage = curDataBook.getDataPage();
							curSelectedRow = curDataBook.getSelectedRow();
						}
						else if (dataRow instanceof IChangeableDataRow)
						{
							curDataPage = ((IChangeableDataRow)dataRow).getDataPage();
							curSelectedRow = ((IChangeableDataRow)dataRow).getRowIndex();
							if (curDataPage != null)
							{
								curDataBook = curDataPage.getDataBook();
							}
						}
						
						try
						{
							cellFormat = cellEditorListener.getCellFormatter().getCellFormat(
									curDataBook, curDataPage, dataRow, columnName, curSelectedRow, -1);
						}
						catch (Throwable pThrowable)
						{
							// Do nothing
						}
					}
					
					Color background;
					Color foreground;
					Font  font;
					if (cellFormat == null)
					{
						background = null;
						foreground = null;
						font = null;
					}
					else
					{
						background = cellFormat.getBackground();
						foreground = cellFormat.getForeground();
						font = cellFormat.getFont();
					}
					if (font == null)
					{
						font = ((Component)cellEditorListener).getFont();
					}
					if (foreground == null && ((Component)cellEditorListener).isForegroundSet())
					{
						foreground = ((Component)cellEditorListener).getForeground();
					}
					if (background == null && ((Component)cellEditorListener).isBackgroundSet())
					{
						background = ((Component)cellEditorListener).getBackground();
					}

					cellEditorComponent.getEditorComponent().setFont(font);
		    		if (cellEditorComponent.isEditorEditable())
		    		{
		    			if (background == null)
		    			{
			    			if (columnDef.isNullable())
			    			{
			    				background = JVxUtil.getSystemColor(IColor.CONTROL_BACKGROUND);
			    			}
			    			else
			    			{
			    				background = JVxUtil.getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND);
			    			}
		    			}
		    			if (cellEditorComponent.getEditorComponent().hasFocus())
						{
							cellEditorComponent.getEditorComponent().selectAll();
						}
						else
						{
							cellEditorComponent.getEditorComponent().select(0, 0);
						}
		    		}
		    		else if (background == null)
		    		{
		    			background = JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
						cellEditorComponent.getEditorComponent().select(0, 0);
		    		}
	    			cellEditorComponent.getEditorComponent().setBackground(background);
	    			cellEditorComponent.getEditorComponent().setForeground(foreground);
	    			cellEditorComponent.setBackground(background); // Synthetica Look&Feel ignores the editor colors.
	    			cellEditorComponent.setForeground(foreground);
    				
	    			if (dynamicAlignment != null)
		    		{
		    			int hAlign = dynamicAlignment.getHorizontalAlignment();
		    			if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				hAlign = cellEditor.getHorizontalAlignment();
		    			}
		    			((JTextField)cellEditorComponent.getEditorComponent()).setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(hAlign));
		    		}
		    		
		    		if (conParent instanceof JComponent)
		    		{
		    			cellEditorComponent.getEditorComponent().putClientProperty("tabIndex", ((JComponent)conParent).getClientProperty("tabIndex"));
		    		}
		    		
					if (cellEditorListener.getControl() instanceof IPlaceholder)
					{
						PromptSupport.setPrompt(cellEditorComponent.getEditorComponent(), 
								                cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
	    		}
	    		catch (Exception pException)
	    		{
	        		synchronized (cellEditorComponent.getTreeLock())
		    		{
	        			cellEditorComponent.setSelectedItem(null);
		    		}
	    			cellEditorComponent.setEditorEditable(false);
	    			cellEditorComponent.getEditorComponent().setBackground(JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
	    			
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
	    			firstEditingStarted = true;
	    			ignoreEvent = false;
	    		}
    		}
    	}
    	
    	// DocumentListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
        public void insertUpdate(DocumentEvent pDocumentEvent) 
        {
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && cellEditorComponent.isEditorEditable())
       		{
       			fireEditingStarted();
       		}
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void removeUpdate(DocumentEvent pDocumentEvent) 
        {
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && cellEditorComponent.isEditorEditable())
       		{
       			fireEditingStarted();
       		}
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void changedUpdate(DocumentEvent pDocumentEvent) 
        {
        }

    	// KeyListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
		public void keyPressed(KeyEvent pKeyEvent)
		{
			if (!pKeyEvent.isConsumed())
			{
				switch (pKeyEvent.getKeyCode())
				{
					case KeyEvent.VK_ESCAPE: 
						pKeyEvent.consume(); 
				        fireEditingComplete(ICellEditorListener.ESCAPE_KEY, true);
				        break;
					case KeyEvent.VK_ENTER: 
						pKeyEvent.consume();
						if (pKeyEvent.isShiftDown())
						{
					        fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY, true);
						}
						else
						{
					        fireEditingComplete(ICellEditorListener.ENTER_KEY, true);
						}
				        break;
					case KeyEvent.VK_TAB: 
						pKeyEvent.consume(); 
						if (pKeyEvent.isShiftDown())
						{
					        fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY, true);
						}
						else
						{
					        fireEditingComplete(ICellEditorListener.TAB_KEY, true);
						}
				        break;
				    default:
				    	// Nothing to do
				}
			}
		}
		
    	/**
    	 * {@inheritDoc}
    	 */
		public void keyReleased(KeyEvent pKeyEvent)
		{
		}
		
    	/**
    	 * {@inheritDoc}
    	 */
		public void keyTyped(KeyEvent pKeyEvent)
		{
		}

		// ActionListener
		
    	/**
    	 * {@inheritDoc}
    	 */
		public void actionPerformed(ActionEvent pEvent)
		{
			// Detect if Popup is closed by Action Event in Popup.
			// This includes Mouse Pressed on a day button.
			// It is similar to JVxLinkedCellEditor, which also fires ENTER_KEY when selection is done by mouse
			popupClosedByKeyAction = true;
		}
		
    	// PopupMenuListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
		public void popupMenuCanceled(PopupMenuEvent pPopupMenuEvent)
		{
			fireEditingComplete(ICellEditorListener.ESCAPE_KEY, false);
		}

    	/**
    	 * {@inheritDoc}
    	 */
		public void popupMenuWillBecomeInvisible(PopupMenuEvent pPopupMenuEvent)
		{
			if (!cellEditorComponent.isPopupCanceled())
			{
				if (!popupClosedByKeyAction) // If Popup is closed by focus lost, not by action event in calendar pane
				{
					fireEditingStarted();
					
					fireEditingComplete(ICellEditorListener.FOCUS_LOST, false);
				}
			}
		}

    	/**
    	 * {@inheritDoc}
    	 */
		public void popupMenuWillBecomeVisible(PopupMenuEvent pPopupMenuEvent)
		{
		}

    	// FocusListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
		public void focusGained(FocusEvent pFocusEvent)
		{
			// if popup is closed by action event in calendar pane
			// we have to wait on focus gained of editor component.
			// Then the focus can be transferred to next component.
			if (popupClosedByKeyAction)
			{
				popupClosedByKeyAction = false;

				fireEditingStarted();
				
				fireEditingComplete(ICellEditorListener.ENTER_KEY, false);
			}
			else if (cellEditorComponent.isEditorEditable())
			{
				// Sets JFormattedTextFields edited true, this prevents setFormatter and therefore document changes on focusGained.
				cellEditorComponent.getEditorComponent().setText(cellEditorComponent.getEditorComponent().getText());
				
	    		cellEditorComponent.getEditorComponent().selectAll();
			}
		}

    	/**
    	 * {@inheritDoc}
    	 */
		public void focusLost(FocusEvent pFocusEvent)
		{
			if (!pFocusEvent.isTemporary() && !cellEditorComponent.isPopupFocusEvent(pFocusEvent))
			{
				fireEditingComplete(ICellEditorListener.FOCUS_LOST, true);
			}
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Delegates the event to the ICellEditorListener.
		 * It takes care, that the event occurs only one time.
		 */
		protected void fireEditingStarted()
		{
        	if (firstEditingStarted && !ignoreEvent && cellEditorListener != null)
        	{
           		firstEditingStarted = false;
           		cellEditorListener.editingStarted();
        	}
		}
		
		/**
		 * Delegates the event to the ICellEditorListener.
		 * It takes care, that editing started will be called before,
		 * if it is not called until jet.
		 * 
		 * @param pCompleteType the editing complete type.
		 * @param pClosePopup try closing the popup.
		 */
		protected void fireEditingComplete(String pCompleteType, boolean pClosePopup)
		{
			if (!ignoreEvent && cellEditorListener != null)
			{
				if (pClosePopup && cellEditorComponent.isPopupVisible())
				{
					ignoreEvent = true;
					cellEditorComponent.setPopupCanceled(true);
					cellEditorComponent.setPopupVisible(false);
					ignoreEvent = false;
				}
				cellEditorListener.editingComplete(pCompleteType);
			}
		}
		
    }	// CellEditorHandler
	
}	// JVxDateCellEditor
