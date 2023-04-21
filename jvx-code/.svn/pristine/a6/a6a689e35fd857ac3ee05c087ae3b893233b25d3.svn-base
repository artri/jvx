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
 * 14.11.2008 - [HM] - creation
 * 25.06.2009 - [JR] - setNumberFormat: forward the pattern to the formatter [BUGFIX]
 * 24.03.2011 - [JR] - #317: cancelEditing checks parents enabled state
 * 25.08.2011 - [JR] - #465: install actions
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.component.IPlaceholder;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.sibvisions.rad.ui.celleditor.AbstractNumberCellEditor;
import com.sibvisions.rad.ui.swing.ext.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.swing.ext.JVxEditor;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.WrappedInsetsBorder;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.ext.text.NumberFormatter;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>JVxTextCellEditor</code> provides the generation of the 
 * physical number editor component, handles correct all events, and 
 * gives standard access to edited values.
 * 
 * @author Martin Handsteiner
 */
public class JVxNumberCellEditor extends AbstractNumberCellEditor 
                                 implements ICellRenderer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cell renderer. */
	private JLabel cellRenderer = null;
	
	/** The cell renderer. */
	private NumberFormatter numberFormatter = new NumberFormatter(numberUtil);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new JVxTextCellEditor.
	 */
	public JVxNumberCellEditor()
	{
		this(null);
	}
	
	/**
	 * Constructs a new JVxNumberCellEditor with the given number format.
	 * @param pNumberFormat the number format.
	 */
	public JVxNumberCellEditor(String pNumberFormat)
	{
		super(pNumberFormat);
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
			cellRenderer = new DefaultTableCellRenderer();
		}
		cellRenderer.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(getHorizontalAlignment()));
		cellRenderer.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(getVerticalAlignment()));
		try
		{
			cellRenderer.setText(numberFormatter.valueToString(pDataRow.getValue(pColumnName)));
		}
		catch (Exception pException)
		{
			cellRenderer.setText(null);
		}
		return cellRenderer;
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * Sets the internal changed Flag, and informs the CellEditorListener 
     * if editing is completed.
     * 
     * @author Martin Handsteiner
     */
    public static class CellEditorHandler implements ICellEditorHandler<JComponent>,
                                                     DocumentListener, 
                                                     FocusListener, 
                                                     KeyListener,
                                                     Runnable
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private JVxNumberCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The column name of the edited column. */
    	private String columnName;

    	/** The physical component that is added to the parent container. */
    	private JFormattedTextField cellEditorComponent;

    	/** The data type to convert and check the numbers. */
        private IDataType dataType;

    	/** True, the Event should be ignored. */
    	private boolean ignoreEvent = false;
        /** True, cancel is performed. */
        private boolean isCancelling = false;
    	
    	/** True, if it's the first editing started event. */
    	private boolean firstEditingStarted = true;
    	
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
    	public CellEditorHandler(JVxNumberCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
            try
            {
                dataType = dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType();

                if (!Number.class.isAssignableFrom(dataType.getTypeClass()))
                {
                    LoggerFactory.getInstance(AbstractNumberCellEditor.class).error("NumberCellEditor is used for a column, that does not store numbers!");
                }
            }
            catch (ModelException me)
            {
                //nothing to be done
            }

    		NumberFormatter numberFormatter = new NumberFormatter();
    		numberFormatter.setNumberPattern(cellEditor.getNumberFormat());
            numberFormatter.setDataType(dataType);

    		cellEditorComponent = new JFormattedTextField(numberFormatter);
    		cellEditorComponent.setColumns(5);
    		
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
    		else
    		{
    			cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment()));
    		}
			
    		cellEditorComponent.getDocument().addDocumentListener(this);
    		cellEditorComponent.setFocusTraversalKeysEnabled(false);
    		cellEditorComponent.addFocusListener(this);
    		cellEditorComponent.addKeyListener(this);
    		
    		JVxUtil.installActions(cellEditorComponent);
    		
    		if (SwingFactory.isMacLaF())
    		{
    			cellEditorComponent.setBorder(new WrappedInsetsBorder(cellEditorComponent.getBorder()));
    		}
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
    		cellEditorComponent.getDocument().removeDocumentListener(this);
    		cellEditorComponent.removeFocusListener(this);
    		cellEditorComponent.removeKeyListener(this);
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
    		try
    		{
				cellEditorComponent.commitEdit();
    		}
    		catch (ParseException pEx)
    		{
    			// try to store
    		}
    		
    		Object oldValue = dataRow.getValue(columnName);
    		Object newValue = dataType.convertToTypeClass(cellEditorComponent.getValue());
    		
    		if (!cellEditorListener.isSavingImmediate() 
    				|| dataType.compareTo(oldValue, newValue) != 0)
    	    {
    			dataRow.setValue(columnName, newValue);
    	    }
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{
    		if (!ignoreEvent && !isCancelling)
    		{
    		    isCancelling = true;
	
	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);
	    			
		    		synchronized (cellEditorComponent.getTreeLock())
		    		{
		    			cellEditorComponent.setValue(dataRow.getValue(columnName));
		    		}
	
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
		    			cellEditorComponent.setEditable(editable);
		    		}
					else
					{
						cellEditorComponent.setEditable(bParentEnabled && !columnDef.isReadOnly());
					}
		    		
					styleEditor(columnDef);

					if (cellEditorListener.getControl() instanceof IPlaceholder)
					{
						PromptSupport.setPrompt(cellEditorComponent, cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
					
		    		if (cellEditorComponent.isEditable())
		    		{
						if (cellEditorComponent.hasFocus())
						{
							cellEditorComponent.selectAll();
						}
						else
						{
							cellEditorComponent.select(0, 0);
						}
		    		}
		    		else
		    		{
						cellEditorComponent.select(0, 0);
		    		}
		    		
		    		if (conParent instanceof JComponent)
		    		{
		    			cellEditorComponent.putClientProperty("tabIndex", ((JComponent)conParent).getClientProperty("tabIndex"));
		    		}
	    		}
	    		catch (Exception pException)
	    		{
		    		synchronized (cellEditorComponent.getTreeLock())
		    		{
		    			cellEditorComponent.setValue(null);
		    		}
	    			cellEditorComponent.setEditable(false);
	    			cellEditorComponent.setBackground(JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
	    			
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
	    			Container conParent = cellEditorComponent.getParent();
	    			if (conParent instanceof JVxEditor && !((JVxEditor)conParent).isBorderVisible())
	    			{
	    			    cellEditorComponent.setBorder(BorderFactory.createEmptyBorder());
	    			}
	    			firstEditingStarted = true;
	    			isCancelling = false;
	    		}
    		}
    	}
    	
    	/**
    	 * Styles the text editor.
    	 * @param columnDef the column definition
    	 * @throws ModelException if it fails.
    	 */
		private void styleEditor(ColumnDefinition columnDef) throws ModelException
		{
			CellFormat cellFormat = null;

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
			
			cellEditorComponent.setFont(font);
			if (cellEditorComponent.isEditable())
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
			}
			else if (background == null)
			{
				background = JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
			}
			cellEditorComponent.setBackground(background);
			cellEditorComponent.setForeground(foreground);

			if (dynamicAlignment != null)
			{
				int hAlign = dynamicAlignment.getHorizontalAlignment();
				if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
				{
					hAlign = cellEditor.getHorizontalAlignment();
				}
				cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(hAlign));
			}
		}
    	
    	/**
    	 * {@inheritDoc}
    	 */
        public void run()
        {
   			try
   			{
   				saveEditing();
   				
				styleEditor(dataRow.getRowDefinition().getColumnDefinition(columnName));
   			}
   			catch (Exception pException)
   			{
   				// Silent ignore the event
   			}
            // NotifyRepaint caused in data book events should be ignored, so invokeLater is necessary, as they call notifyRepaint. 
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    ignoreEvent = false;
                }
            });
        }
        
    	// DocumentListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
        public void insertUpdate(DocumentEvent pDocumentEvent) 
        {
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && cellEditorComponent.isEditable())
       		{
       			fireEditingStarted();
       			
       			if (!isCancelling && cellEditorListener.isSavingImmediate())
       			{
       				ignoreEvent = true;
       				SwingUtilities.invokeLater(this);
       			}
       		}
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void removeUpdate(DocumentEvent pDocumentEvent) 
        {
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && cellEditorComponent.isEditable())
       		{
       			fireEditingStarted();
       			
       			if (!isCancelling && cellEditorListener.isSavingImmediate())
       			{
       				ignoreEvent = true;
       				SwingUtilities.invokeLater(this);
       			}
       		}
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void changedUpdate(DocumentEvent pDocumentEvent) 
        {
            if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && cellEditorComponent.isEditable())
            {
                fireEditingStarted();
                
                if (!isCancelling && cellEditorListener.isSavingImmediate())
                {
                    ignoreEvent = true;
                    SwingUtilities.invokeLater(this);
                }
            }
        }

    	// FocusListener
    	
    	/**
    	 * {@inheritDoc}
    	 */
		public void focusGained(FocusEvent pFocusEvent)
		{
			if (cellEditorComponent.isEditable())
			{
	            SwingUtilities.invokeLater(new Runnable()
	            {
	                @Override
	                public void run()
	                {
	                    cellEditorComponent.selectAll();
	                }
	            });
			}
		}

    	/**
    	 * {@inheritDoc}
    	 */
		public void focusLost(FocusEvent pFocusEvent)
		{
			if (!pFocusEvent.isTemporary())
			{
				fireEditingComplete(ICellEditorListener.FOCUS_LOST);
			}
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
				        fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
				        break;
					case KeyEvent.VK_ENTER: 
						pKeyEvent.consume();
						if (pKeyEvent.isShiftDown())
						{
					        fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
						}
						else
						{
					        fireEditingComplete(ICellEditorListener.ENTER_KEY);
						}
				        break;
					case KeyEvent.VK_TAB: 
						pKeyEvent.consume(); 
						if (pKeyEvent.isShiftDown())
						{
					        fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
						}
						else
						{
					        fireEditingComplete(ICellEditorListener.TAB_KEY);
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
        
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Delegates the event to the ICellEditorListener.
		 * It takes care, that the event occurs only one time.
		 */
		protected void fireEditingStarted()
		{
        	if (firstEditingStarted && !ignoreEvent && !isCancelling && cellEditorListener != null)
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
		 */
		protected void fireEditingComplete(String pCompleteType)
		{
			if (!ignoreEvent && !isCancelling && cellEditorListener != null)
			{
				cellEditorListener.editingComplete(pCompleteType);
			}
		}
		
    }	// CellEditorHandler
	
}	// JVxTextCellEditor
