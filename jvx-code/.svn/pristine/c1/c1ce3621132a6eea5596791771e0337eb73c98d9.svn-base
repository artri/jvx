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
 * 03.08.2009 - [JR] - replaceAllowed: checked pText == null [BUGFIX]
 *                   - replaceAllowed: split into insert/replace [BUGFIX]
 * 05.02.2009 - [JR] - replaceAllowed: re-implemented with fixed calculation                   
 * 24.03.2011 - [JR] - #317: cancelEditing checks parents enabled state
 * 09.05.2011 - [JR] - #345: cancelEditing: check JViewport
 * 25.08.2011 - [JR] - #465: install actions
 * 21.02.2013 - [HM] - ignoreEvent during saveImmediate.
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
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
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.celleditor.AbstractTextCellEditor;
import com.sibvisions.rad.ui.swing.ext.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.swing.ext.JVxEditor;
import com.sibvisions.rad.ui.swing.ext.JVxHtmlEditor;
import com.sibvisions.rad.ui.swing.ext.JVxScrollPane;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.WrappedInsetsBorder;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxTextCellEditor</code> provides the generation of the 
 * physical text editor component, handles correct all events, and 
 * gives standard access to edited values.
 * 
 * @author Martin Handsteiner
 */
public class JVxTextCellEditor extends AbstractTextCellEditor 
                               implements ICellRenderer<JComponent>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The default renderer anyway. */
	private DefaultTableCellRenderer cellRenderer = null;
	
	/** the reference text field. */
	private static JTextField tfRef = new JTextField();
	
	/** the border. */
	private static WrappedInsetsBorder border;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
		border = new WrappedInsetsBorder(tfRef.getBorder());
		border.setPaintInsets(new Insets(0, 0, 0, 0));
    }
	
	/**
	 * Constructs a new JVxTextCellEditor.
	 */
	public JVxTextCellEditor()
	{
		this(null);
	}
	
	/**
	 * Constructs a new JVxTextCellEditor with the given content type.
	 * @param pContentType the content type.
	 */
	public JVxTextCellEditor(String pContentType)
	{
		super(pContentType);
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
	public JComponent getCellRendererComponent(JComponent pParentComponent, IDataPage pDataPage, int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected,
			boolean hasFocus)
	{
		if (cellRenderer == null)
		{
			cellRenderer = new DefaultTableCellRenderer();
			
			setEchoChar(new JPasswordField().getEchoChar());
		}
		
		cellRenderer.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(getHorizontalAlignment()));
		cellRenderer.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(getVerticalAlignment()));

		try
		{
			String value = pDataRow.getValueAsString(pColumnName);
			
			if (value != null && TEXT_PLAIN_PASSWORD.equals(contentType))
			{
				value = maskPassword(value);
			}

			cellRenderer.setText(value);
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
     * Sets the internal changed flag, and informs the CellEditorListener 
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
    	private JVxTextCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The column name of the edited column. */
    	private String columnName;
 
    	/** maximum character length. */
    	private int maxLength;

    	/** The physical component that is added to the parent container. */
    	private JComponent cellEditorComponent;
    	
    	/** The physical text editor. */
    	private JTextComponent textComponent;
    	
    	/** True, if editor does not already use the enter key. */
    	private boolean enterKeyIsUnused;
    	
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
    	public CellEditorHandler(JVxTextCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
    		
    		String contentType = cellEditor.getContentType();
    		if (TEXT_PLAIN_SINGLELINE.equals(contentType))
    		{
    			textComponent = new JTextField(10);
    			cellEditorComponent = textComponent;
    			enterKeyIsUnused = true;
    		}
    		else if (TEXT_PLAIN_PASSWORD.equals(contentType))
    		{
    			textComponent = new JPasswordField(10);
    			cellEditorComponent = textComponent;
    			enterKeyIsUnused = true;
    		}
    		else if (TEXT_PLAIN_MULTILINE.equals(contentType) || TEXT_PLAIN_WRAPPEDMULTILINE.equals(contentType))
    		{
    			textComponent = new JTextArea(5, 12);
    			textComponent.setFont(new JTextField().getFont());
    			
    			((JTextArea)textComponent).setWrapStyleWord(true);
    			((JTextArea)textComponent).setLineWrap(TEXT_PLAIN_WRAPPEDMULTILINE.equals(contentType));
    			enterKeyIsUnused = false;
    			
    			cellEditorComponent = new JVxScrollPane(textComponent)
				{
    				public boolean hasFocus()
    				{
    					return getViewportView().hasFocus();
    				}
				};
    		}
    		else
    		{
    		    if (cellEditorListener.getControl() instanceof IEditorControl)
    		    {
    		        JVxHtmlEditor htmlEditor = new JVxHtmlEditor();
    		        cellEditorComponent = htmlEditor;
    		        enterKeyIsUnused = false;
    		        textComponent = htmlEditor.getRichTextPane();
		        }
    		    else
    		    {
    		        textComponent = new JEditorPane(); 
    		        ((JEditorPane)textComponent).setContentType(contentType);
    		        enterKeyIsUnused = false;
    		        cellEditorComponent = new JVxScrollPane(textComponent)
                    {
                        public boolean hasFocus()
                        {
                            return getViewportView().hasFocus();
                        }
                    };
    		    }
    		}
    		
			JVxUtil.installActions(textComponent);
    		
			if (textComponent instanceof JTextField)
			{
				if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
				{	// use alignment of editors, if possible.
					dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
    			}
    			else
    			{
        			((JTextField)textComponent).setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment()));
    			}
			}
			
    		try
    		{
    			maxLength = ((StringDataType)dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType()).getSize();
    		}
    		catch (Exception pException)
    		{
    			maxLength = Integer.MAX_VALUE;
    		}

    		textComponent.getDocument().addDocumentListener(this);
    		((AbstractDocument)textComponent.getDocument()).setDocumentFilter(new TextDocumentFilter());

    		textComponent.setFocusTraversalKeysEnabled(false);
    		textComponent.addFocusListener(this);
    		textComponent.addKeyListener(this);
    		
    		if (SwingFactory.isMacLaF() && !(cellEditorComponent instanceof JVxHtmlEditor))
    		{
    			if (cellEditorComponent == textComponent)
    			{
    				textComponent.setBorder(new WrappedInsetsBorder(textComponent.getBorder()));
    			}
    			else if (cellEditorComponent instanceof JVxScrollPane)
    			{
    				textComponent.setBorder(null);
    				cellEditorComponent.setBorder(border);
    			}
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
    		textComponent.getDocument().removeDocumentListener(this);
    		textComponent.removeFocusListener(this);
    		textComponent.removeKeyListener(this);
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
    		IDataType dataType = dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType();
			
    		Object oldValue = dataRow.getRawValue(columnName);
    		Object newValue;
    		
    		if (cellEditorComponent instanceof JVxHtmlEditor)
    		{
    		    newValue = dataType.convertToTypeClass(((JVxHtmlEditor)cellEditorComponent).getText());
    		}
    		else
    		{    		    
    		    newValue = dataType.convertToTypeClass(textComponent.getText());
    		}
    		
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
	    		    if (cellEditorComponent instanceof JVxHtmlEditor)
	    		    {
	    		        ((JVxHtmlEditor)cellEditorComponent).setTranslation(cellEditorListener.getControl().getTranslation());
	                    ((JVxHtmlEditor)cellEditorComponent).setTranslationEnabled(cellEditorListener.getControl().isTranslationEnabled());
	    		    }

	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);
					Container conParent = cellEditorComponent.getParent();
					
					boolean bParentEnabled = conParent == null || conParent.isEnabled();
					boolean editable;
					if (dataRow instanceof IDataBook)
					{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			editable = bParentEnabled
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
					}
					else
					{
					    editable = bParentEnabled && !columnDef.isReadOnly();
					}

					String value = columnDef.getDataType().convertToString(editable ? dataRow.getRawValue(columnName) : dataRow.getValue(columnName));
                    
                    synchronized (textComponent.getTreeLock())
                    {
                        textComponent.setText(value);
                    }
                    if (textComponent instanceof JEditorPane)
                    {
                        JVxUtil.revalidateAllDelayed(textComponent);
                    }
                    textComponent.setEditable(editable);
					
					styleEditor(columnDef);
		    		
					if (cellEditorListener.getControl() instanceof IPlaceholder)
					{
						PromptSupport.setPrompt(textComponent, cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
					
					if (textComponent.isEditable())
					{
						if (textComponent.hasFocus() 
								&& (TEXT_PLAIN_SINGLELINE.equals(cellEditor.getContentType()) || TEXT_PLAIN_PASSWORD.equals(cellEditor.getContentType())))
						{
							textComponent.selectAll();
						}
						else
						{
							textComponent.select(0, 0);
						}
					}
					else
					{
						textComponent.select(0, 0);
					}

		    		if (conParent instanceof JComponent)
		    		{
		    			textComponent.putClientProperty("tabIndex", ((JComponent)conParent).getClientProperty("tabIndex"));
		    		}
	    		}
	    		catch (Exception pException)
	    		{
		    		synchronized (textComponent.getTreeLock())
		    		{
		    			textComponent.setText(null);
		    		}
	    			textComponent.setEditable(false);
	    			textComponent.setBackground(JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
	    			
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
	    			if (cellEditorComponent instanceof JVxHtmlEditor)
                    {
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                isCancelling = false;
                                try
                                {
                                    ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);
                                    Container conParent = cellEditorComponent.getParent();
                                    
                                    boolean bParentEnabled = conParent == null || conParent.isEnabled();
                                    boolean editable;
                                    if (dataRow instanceof IDataBook)
                                    {
                                        IDataBook dataBook = (IDataBook)dataRow;
                                        editable = bParentEnabled
                                                        && dataBook.isUpdateAllowed() 
                                                        && !columnDef.isReadOnly();
                                        if (editable && dataBook.getReadOnlyChecker() != null)
                                        {
                                            try
                                            {
                                                editable = !dataBook.getReadOnlyChecker().isReadOnly(
                                                        dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
                                            }
                                            catch (Throwable pTh)
                                            {
                                                // Ignore
                                            }
                                        }
                                    }
                                    else
                                    {
                                        editable = bParentEnabled && !columnDef.isReadOnly();
                                    }
                                    textComponent.setEditable(editable);
                                }
                                catch (Exception ex)
                                {
                                    // Ignore
                                }
                            }
                        });
                    }
	    			else
	    			{
	                    isCancelling = false;
	    			}
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
			
			textComponent.setFont(font);
			if (textComponent.isEditable())
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
			
    		if (SwingFactory.isMacLaF())
    		{
    			if (cellEditorComponent != textComponent)
    			{
    				if (textComponent.isEditable()
	    			    || !(background instanceof javax.swing.plaf.UIResource))
	    			{
	    				cellEditorComponent.setBackground(background);
	    			}
    				else if (!textComponent.isEditable())
    				{
    					cellEditorComponent.setBackground(Color.WHITE);
    				}
    			}
    		}
    		else
    		{
    			if (background instanceof javax.swing.plaf.UIResource)
    			{
    				background = new Color(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
    			}
    		}
    		
			textComponent.setBackground(background);
			textComponent.setForeground(foreground);

			if (dynamicAlignment != null)
			{
				int hAlign = dynamicAlignment.getHorizontalAlignment();
				if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
				{
					hAlign = cellEditor.getHorizontalAlignment();
				}
				((JTextField)textComponent).setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(hAlign));
			}
		}
    	
    	// DocumentListener
    	
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
                public void run()
                {
                    ignoreEvent = false;
                }
            });
        }
        
    	/**
    	 * {@inheritDoc}
    	 */
        public void insertUpdate(DocumentEvent pDocumentEvent) 
        {
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && textComponent.isEditable())
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
       		if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && textComponent.isEditable())
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
            if (!(EventQueue.getCurrentEvent() instanceof FocusEvent) && textComponent.isEditable())
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
			if (textComponent.isEditable() 
					&& (TEXT_PLAIN_SINGLELINE.equals(cellEditor.getContentType()) || TEXT_PLAIN_PASSWORD.equals(cellEditor.getContentType())))
			{
				textComponent.selectAll();
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
//						pKeyEvent.consume(); 
				        fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
				        break;
					case KeyEvent.VK_ENTER: 
						if (pKeyEvent.getModifiers() != 0 || enterKeyIsUnused)
						{
							pKeyEvent.consume(); 
							if (pKeyEvent.isShiftDown())
							{
						        fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
							}
							else
							{
						        fireEditingComplete(ICellEditorListener.ENTER_KEY);
							}
						}
				        break;
					case KeyEvent.VK_TAB: 
						if (pKeyEvent.getModifiers() != 0 || enterKeyIsUnused)
						{
							pKeyEvent.consume(); 
							if (pKeyEvent.isShiftDown())
							{
						        fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
							}
							else
							{
						        fireEditingComplete(ICellEditorListener.TAB_KEY);
							}
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
		 * Delegates the event to the {@link ICellEditorListener}.
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
		 * Delegates the event to the {@link ICellEditorListener}.
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
		
		/**
	     * Replaces only the allowed text.
	     *
	     * @param pFb Filter bypass
	     * @param pOffset Location in Document
	     * @param pLength Length of text to delete
	     * @param pText Text to insert, null indicates no text to insert
	     * @param pAttr AttributeSet indicating attributes of inserted text, null is legal.
	     * @exception BadLocationException  the given insert is not a valid position within the document
	     */
		private void replaceAllowed(DocumentFilter.FilterBypass pFb, int pOffset, int pLength, String pText, AttributeSet pAttr) throws BadLocationException 
		{
			Document doc = pFb.getDocument();
			
			int iNewLength = doc.getLength() - pLength + (pText != null ? pText.length() : 0);
			
			if (iNewLength <= maxLength)
			{
				pFb.replace(pOffset, pLength, pText, pAttr);
			}
			else
			{
				UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
			}
		}
		
		//****************************************************************
		// Subclass definition
		//****************************************************************

		/**
	     * TextDocumentFilter implementation that calls back to the replace
	     * method of DefaultFormatter.
	     * 
	     * @author Martin Handsteiner
	     */
	    private class TextDocumentFilter extends DocumentFilter 
	    {
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Overwritten methods
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	@Override
	        public void remove(FilterBypass pFb, int pOffset, int pLength) throws BadLocationException 
	        {
	        	replaceAllowed(pFb, pOffset, pLength, null, null);
	        }

	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	@Override
	        public void insertString(FilterBypass pFb, int pOffset, String pText, AttributeSet pAttr) throws BadLocationException 
	        {
	        	replaceAllowed(pFb, pOffset, 0, pText, pAttr);
	        }

	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	@Override
	        public void replace(FilterBypass pFb, int pOffset, int pLength, String pText, AttributeSet pAttr) throws BadLocationException 
	        {
	    		replaceAllowed(pFb, pOffset, pLength, pText, pAttr);
	        }
	    	
	    }	// NumberDocumentFilter

    }	// CellEditorHandler

}	// JVxTextCellEditor
