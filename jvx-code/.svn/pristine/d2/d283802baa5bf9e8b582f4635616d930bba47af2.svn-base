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
 * 09.11.2008 - [HM] - creation
 * 24.03.2011 - [JR] - #317: cancelEditing checks parents enabled state
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.swing.JComponent;

import com.sibvisions.rad.ui.celleditor.AbstractChoiceCellEditor;
import com.sibvisions.rad.ui.swing.ext.JVxChoice;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxChoiceRenderer;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>JVxChoiceCellEditor</code> provides the generation of the physical
 * choice editor component, handles correct all events, and gives standard
 * access to edited values.
 * 
 * @author Martin Handsteiner
 */
public class JVxChoiceCellEditor extends AbstractChoiceCellEditor<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cell renderer. */
	private JVxChoiceRenderer cellRenderer = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new JVxChoiceCellEditor.
	 */
	public JVxChoiceCellEditor()
	{
		this(null, null);
	}
	
	/**
	 * Constructs a new JVxChoiceCellEditor with the given allowed values and
	 * image names.
	 * 
	 * @param pAllowedValues the allowed values.
	 * @param pImageNames the image names.
	 */
	public JVxChoiceCellEditor(Object[] pAllowedValues, String[] pImageNames)
	{
		super(pAllowedValues, pImageNames);
		
		setHorizontalAlignment(ALIGN_CENTER);
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
		return new CellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Component getCellRendererComponent(Component pParentComponent,
			IDataPage pDataPage,
			int pRowNumber,
			IDataRow pDataRow,
			String pColumnName,
			boolean pIsSelected,
			boolean pHasFocus)
	{
		if (cellRenderer == null)
		{
			cellRenderer = new JVxChoiceRenderer();
			
			cellRenderer.setAllowedValues(convertAllowedValuesToString(allowedValues));
			cellRenderer.setImages(getImages());
			cellRenderer.setDefaultImage(getDefaultImage());
		}
		
		cellRenderer.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(getHorizontalAlignment()));
		cellRenderer.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(getVerticalAlignment()));
		
		try
		{
			Object item = pDataRow.getValue(pColumnName);
			if (item != null && item.getClass() != String.class)
			{
				item = item.toString();
			}
			cellRenderer.setSelectedItem(item);
		}
		catch (Exception pException)
		{
			cellRenderer.setSelectedItem(null);
		}
		
		return cellRenderer;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAllowedValues(Object[] pAllowedValues)
	{
		super.setAllowedValues(pAllowedValues);
		
		if (cellRenderer != null)
		{
			cellRenderer.setAllowedValues(convertAllowedValuesToString(getAllowedValues()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultImageName(String pDefaultImageName)
	{
		super.setDefaultImageName(pDefaultImageName);
		
		if (cellRenderer != null)
		{
			cellRenderer.setDefaultImage(getDefaultImage());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImageNames(String[] pImageNames)
	{
		super.setImageNames(pImageNames);
		
		if (cellRenderer != null)
		{
			cellRenderer.setImages(getImages());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the corresponding Image[] for the image names.
	 * 
	 * @return the corresponding Image[].
	 */
	private Image[] getImages()
	{
		if (imageNames == null)
		{
			return null;
		}
		else
		{
			Image[] images = new Image[imageNames.length];
			
			for (int i = 0; i < imageNames.length; i++)
			{
				images[i] = JVxUtil.getImage(imageNames[i]);
			}
			return images;
		}
	}
	
	/**
	 * Gets the default Image for the default image name.
	 * 
	 * @return the corresponding Image.
	 */
	private Image getDefaultImage()
	{
		if (defaultImageName == null)
		{
			return null;
		}
		else
		{
			return JVxUtil.getImage(defaultImageName);
		}
	}
	
	/**
	 * Convert allowed values to destination type.
	 * 
	 * @param pAllowedValues the allowed values
	 * @return the converted values
	 */
	private Object[] convertAllowedValuesToString(Object[] pAllowedValues)
	{
		if (pAllowedValues == null || pAllowedValues.length == 0)
		{
			return pAllowedValues;
		}
		else
		{
			Object[] result = new Object[pAllowedValues.length];
			
			for (int i = 0; i < result.length; i++)
			{
				Object item = pAllowedValues[i];
				if (item != null && item.getClass() != String.class)
				{
					result[i] = item.toString();
				}
				else
				{
					result[i] = item;
				}
			}
			
			return result;
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Sets the internal changed Flag, and informs the CellEditorListener if
	 * editing is completed.
	 * 
	 * @author Martin Handsteiner
	 */
	private static class CellEditorHandler implements ICellEditorHandler<JComponent>,
			ItemListener,
			KeyListener,
			FocusListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The CellEditor, that created this handler. */
		private JVxChoiceCellEditor cellEditor;
		
		/**
		 * The CellEditorListener to inform, if editing is started or completed.
		 */
		private ICellEditorListener cellEditorListener;
		
		/** The data row that is edited. */
		private IDataRow dataRow;
		
		/** The column name of the edited column. */
		private String columnName;
		
		/** The CellEditorListener to inform, if editing is completed. */
		private JVxChoice cellEditorComponent;
		
		/** Dynamic alignment. */
		private IAlignmentConstants dynamicAlignment = null;
		
		/** Tells the listener to ignore the events. */
		private boolean ignoreEvent = false;
		
		/** True, if it's the first editing started event. */
		private boolean firstEditingStarted = true;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Constructs a new CellEditorHandler.
		 * 
		 * @param pCellEditor the CellEditor that created this handler.
		 * @param pCellEditorListener CellEditorListener to inform, if editing
		 *            is started or completed.
		 * @param pDataRow the data row that is edited.
		 * @param pColumnName the column name of the edited column.
		 */
		public CellEditorHandler(JVxChoiceCellEditor pCellEditor, ICellEditorListener pCellEditorListener,
				IDataRow pDataRow, String pColumnName)
		{
			cellEditor = pCellEditor;
			cellEditorListener = pCellEditorListener;
			dataRow = pDataRow;
			columnName = pColumnName;
			
			cellEditorComponent = new JVxChoice();
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
				cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment()));
				cellEditorComponent.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(cellEditor.getVerticalAlignment()));
			}
			
			cellEditorComponent.setAllowedValues(convertAllowedValues(cellEditor.getAllowedValues()));
			cellEditorComponent.setImages(cellEditor.getImages());
			cellEditorComponent.setDefaultImage(cellEditor.getDefaultImage());
			
			cellEditorComponent.setFocusTraversalKeysEnabled(false);
			cellEditorComponent.addItemListener(this);
			cellEditorComponent.addKeyListener(this);
			cellEditorComponent.addFocusListener(this);
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
			cellEditorComponent.removeItemListener(this);
			cellEditorComponent.removeKeyListener(this);
			cellEditorComponent.removeFocusListener(this);
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
				
				Container conParent = cellEditorComponent.getParent();
				
				boolean bParentEnabled = conParent == null || conParent.isEnabled();
				
				try
				{
					if (!CommonUtil.equals(cellEditorComponent.getSelectedItem(), dataRow.getValue(columnName)))
					{
						cellEditorComponent.setSelectedItem(dataRow.getValue(columnName));
					}
					if (dataRow instanceof IDataBook)
					{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			boolean editable = bParentEnabled
		    					        && dataBook.isUpdateAllowed() 
		    							&& !dataBook.getRowDefinition().getColumnDefinition(columnName).isReadOnly();
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
						cellEditorComponent.setEditable(bParentEnabled
								&& !dataRow.getRowDefinition().getColumnDefinition(columnName).isReadOnly());
					}
					
					if (dynamicAlignment != null)
					{
						int hAlign = dynamicAlignment.getHorizontalAlignment();
						if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
						{
							hAlign = cellEditor.getHorizontalAlignment();
						}
						cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(hAlign));
						int vAlign = dynamicAlignment.getVerticalAlignment();
						if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
						{
							vAlign = cellEditor.getVerticalAlignment();
						}
						cellEditorComponent.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(vAlign));
					}
				}
				catch (Exception pException)
				{
					cellEditorComponent.setSelectedItem(null);
					cellEditorComponent.setEditable(false);
					
					throw new ModelException("Editor cannot be restored!", pException);
				}
				finally
				{
					firstEditingStarted = true;
					ignoreEvent = false;
				}
			}
		}
		
		// ItemListener
		
		/**
		 * {@inheritDoc}
		 */
		public void itemStateChanged(ItemEvent pItemEvent)
		{
			if (pItemEvent.getStateChange() == ItemEvent.SELECTED && !ignoreEvent)
			{
				fireEditingStarted();
				fireEditingComplete(ICellEditorListener.ACTION_KEY);
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
		
		// FocusListener
		
		/**
		 * {@inheritDoc}
		 */
		public void focusGained(FocusEvent pFocusEvent)
		{
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
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Convert allowed values to destination type.
		 * 
		 * @param pAllowedValues the allowed values
		 * @return the converted values
		 */
		private Object[] convertAllowedValues(Object[] pAllowedValues)
		{
			try
			{
				IDataType dataType = dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType();
				
				Object[] result = new Object[pAllowedValues.length];
				
				for (int i = 0; i < result.length; i++)
				{
					try
					{
						result[i] = dataType.convertToTypeClass(pAllowedValues[i]);
					}
					catch (ModelException e)
					{
						result[i] = pAllowedValues[i];
					}
				}
				
				return result;
			}
			catch (Exception ex)
			{
				return pAllowedValues;
			}
		}
		
		/**
		 * Delegates the event to the ICellEditorListener. It takes care, that
		 * the event occurs only one time.
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
		 * Delegates the event to the ICellEditorListener. It takes care, that
		 * editing started will be called before, if it is not called until jet.
		 * 
		 * @param pCompleteType the editing complete type.
		 */
		protected void fireEditingComplete(String pCompleteType)
		{
			if (!ignoreEvent && cellEditorListener != null)
			{
				cellEditorListener.editingComplete(pCompleteType);
			}
		}
		
	}	// CellEditorHandler
	
}	// JVxChoiceCellEditor
